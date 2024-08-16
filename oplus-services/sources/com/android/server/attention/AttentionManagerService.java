package com.android.server.attention;

import android.app.ActivityThread;
import android.attention.AttentionManagerInternal;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.hardware.SensorPrivacyManager;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.ShellCommand;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.service.attention.IAttentionCallback;
import android.service.attention.IAttentionService;
import android.service.attention.IProximityUpdateCallback;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.IDeviceIdleControllerExt;
import com.android.server.SystemService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AttentionManagerService extends SystemService {

    @VisibleForTesting
    protected static final int ATTENTION_CACHE_BUFFER_SIZE = 5;
    private static final long CONNECTION_TTL_MILLIS = 60000;
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_SERVICE_ENABLED = true;

    @VisibleForTesting
    static final long DEFAULT_STALE_AFTER_MILLIS = 1000;

    @VisibleForTesting
    static final String KEY_SERVICE_ENABLED = "service_enabled";

    @VisibleForTesting
    static final String KEY_STALE_AFTER_MILLIS = "stale_after_millis";
    private static final String LOG_TAG = "AttentionManagerService";
    private static final long SERVICE_BINDING_WAIT_MILLIS = 1000;
    private static String sTestAttentionServicePackage;

    @GuardedBy({"mLock"})
    private AttentionCheckCacheBuffer mAttentionCheckCacheBuffer;
    private AttentionHandler mAttentionHandler;

    @GuardedBy({"mLock"})
    private boolean mBinding;

    @VisibleForTesting
    ComponentName mComponentName;
    private final AttentionServiceConnection mConnection;
    private final Context mContext;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    AttentionCheck mCurrentAttentionCheck;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    ProximityUpdate mCurrentProximityUpdate;

    @VisibleForTesting
    boolean mIsServiceEnabled;
    private final Object mLock;
    private final PowerManager mPowerManager;
    private final SensorPrivacyManager mPrivacyManager;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    protected IAttentionService mService;
    private CountDownLatch mServiceBindingLatch;

    @VisibleForTesting
    long mStaleAfterMillis;

    public AttentionManagerService(Context context) {
        this(context, (PowerManager) context.getSystemService("power"), new Object(), null);
        this.mAttentionHandler = new AttentionHandler();
    }

    @VisibleForTesting
    AttentionManagerService(Context context, PowerManager powerManager, Object obj, AttentionHandler attentionHandler) {
        super(context);
        this.mConnection = new AttentionServiceConnection();
        Objects.requireNonNull(context);
        this.mContext = context;
        this.mPowerManager = powerManager;
        this.mLock = obj;
        this.mAttentionHandler = attentionHandler;
        this.mPrivacyManager = SensorPrivacyManager.getInstance(context);
        this.mServiceBindingLatch = new CountDownLatch(1);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            this.mContext.registerReceiver(new ScreenStateReceiver(), new IntentFilter("android.intent.action.SCREEN_OFF"));
            readValuesFromDeviceConfig();
            DeviceConfig.addOnPropertiesChangedListener("attention_manager_service", ActivityThread.currentApplication().getMainExecutor(), new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.attention.AttentionManagerService$$ExternalSyntheticLambda0
                public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                    AttentionManagerService.this.lambda$onBootPhase$0(properties);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBootPhase$0(DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("attention", new BinderService());
        publishLocalService(AttentionManagerInternal.class, new LocalService());
    }

    public static boolean isServiceConfigured(Context context) {
        return !TextUtils.isEmpty(getServiceConfigPackage(context));
    }

    @VisibleForTesting
    protected boolean isServiceAvailable() {
        if (this.mComponentName == null) {
            this.mComponentName = resolveAttentionService(this.mContext);
        }
        return this.mComponentName != null;
    }

    private boolean getIsServiceEnabled() {
        return DeviceConfig.getBoolean("attention_manager_service", KEY_SERVICE_ENABLED, true);
    }

    @VisibleForTesting
    protected long getStaleAfterMillis() {
        long j = DeviceConfig.getLong("attention_manager_service", KEY_STALE_AFTER_MILLIS, 1000L);
        if (j >= 0 && j <= IDeviceIdleControllerExt.ADVANCE_TIME) {
            return j;
        }
        Slog.w(LOG_TAG, "Bad flag value supplied for: stale_after_millis");
        return 1000L;
    }

    private void onDeviceConfigChange(Set<String> set) {
        for (String str : set) {
            str.hashCode();
            if (str.equals(KEY_STALE_AFTER_MILLIS) || str.equals(KEY_SERVICE_ENABLED)) {
                readValuesFromDeviceConfig();
                return;
            }
            Slog.i(LOG_TAG, "Ignoring change on " + str);
        }
    }

    private void readValuesFromDeviceConfig() {
        this.mIsServiceEnabled = getIsServiceEnabled();
        this.mStaleAfterMillis = getStaleAfterMillis();
        Slog.i(LOG_TAG, "readValuesFromDeviceConfig():\nmIsServiceEnabled=" + this.mIsServiceEnabled + "\nmStaleAfterMillis=" + this.mStaleAfterMillis);
    }

    @VisibleForTesting
    boolean checkAttention(long j, AttentionManagerInternal.AttentionCallbackInternal attentionCallbackInternal) {
        Objects.requireNonNull(attentionCallbackInternal);
        if (!this.mIsServiceEnabled) {
            Slog.w(LOG_TAG, "Trying to call checkAttention() on an unsupported device.");
            return false;
        }
        if (!isServiceAvailable()) {
            Slog.w(LOG_TAG, "Service is not available at this moment.");
            return false;
        }
        if (this.mPrivacyManager.isSensorPrivacyEnabled(2)) {
            Slog.w(LOG_TAG, "Camera is locked by a toggle.");
            return false;
        }
        if (!this.mPowerManager.isInteractive() || this.mPowerManager.isPowerSaveMode()) {
            return false;
        }
        synchronized (this.mLock) {
            freeIfInactiveLocked();
            bindLocked();
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        awaitServiceBinding(Math.min(1000L, j));
        synchronized (this.mLock) {
            AttentionCheckCacheBuffer attentionCheckCacheBuffer = this.mAttentionCheckCacheBuffer;
            AttentionCheckCache last = attentionCheckCacheBuffer == null ? null : attentionCheckCacheBuffer.getLast();
            if (last != null && uptimeMillis < last.mLastComputed + this.mStaleAfterMillis) {
                attentionCallbackInternal.onSuccess(last.mResult, last.mTimestamp);
                return true;
            }
            AttentionCheck attentionCheck = this.mCurrentAttentionCheck;
            if (attentionCheck != null && (!attentionCheck.mIsDispatched || !this.mCurrentAttentionCheck.mIsFulfilled)) {
                return false;
            }
            this.mCurrentAttentionCheck = new AttentionCheck(attentionCallbackInternal, this);
            if (this.mService != null) {
                try {
                    cancelAfterTimeoutLocked(j);
                    this.mService.checkAttention(this.mCurrentAttentionCheck.mIAttentionCallback);
                    this.mCurrentAttentionCheck.mIsDispatched = true;
                } catch (RemoteException unused) {
                    Slog.e(LOG_TAG, "Cannot call into the AttentionService");
                    return false;
                }
            }
            return true;
        }
    }

    @VisibleForTesting
    void cancelAttentionCheck(AttentionManagerInternal.AttentionCallbackInternal attentionCallbackInternal) {
        synchronized (this.mLock) {
            if (!this.mCurrentAttentionCheck.mCallbackInternal.equals(attentionCallbackInternal)) {
                Slog.w(LOG_TAG, "Cannot cancel a non-current request");
            } else {
                cancel();
            }
        }
    }

    @VisibleForTesting
    boolean onStartProximityUpdates(AttentionManagerInternal.ProximityUpdateCallbackInternal proximityUpdateCallbackInternal) {
        Objects.requireNonNull(proximityUpdateCallbackInternal);
        if (!this.mIsServiceEnabled) {
            Slog.w(LOG_TAG, "Trying to call onProximityUpdate() on an unsupported device.");
            return false;
        }
        if (!isServiceAvailable()) {
            Slog.w(LOG_TAG, "Service is not available at this moment.");
            return false;
        }
        if (!this.mPowerManager.isInteractive()) {
            Slog.w(LOG_TAG, "Proximity Service is unavailable during screen off at this moment.");
            return false;
        }
        synchronized (this.mLock) {
            freeIfInactiveLocked();
            bindLocked();
        }
        awaitServiceBinding(1000L);
        synchronized (this.mLock) {
            ProximityUpdate proximityUpdate = this.mCurrentProximityUpdate;
            if (proximityUpdate != null && proximityUpdate.mStartedUpdates) {
                if (this.mCurrentProximityUpdate.mCallbackInternal == proximityUpdateCallbackInternal) {
                    Slog.w(LOG_TAG, "Provided callback is already registered. Skipping.");
                    return true;
                }
                Slog.w(LOG_TAG, "New proximity update cannot be processed because there is already an ongoing update");
                return false;
            }
            ProximityUpdate proximityUpdate2 = new ProximityUpdate(proximityUpdateCallbackInternal);
            this.mCurrentProximityUpdate = proximityUpdate2;
            return proximityUpdate2.startUpdates();
        }
    }

    @VisibleForTesting
    void onStopProximityUpdates(AttentionManagerInternal.ProximityUpdateCallbackInternal proximityUpdateCallbackInternal) {
        synchronized (this.mLock) {
            ProximityUpdate proximityUpdate = this.mCurrentProximityUpdate;
            if (proximityUpdate != null && proximityUpdate.mCallbackInternal.equals(proximityUpdateCallbackInternal) && this.mCurrentProximityUpdate.mStartedUpdates) {
                this.mCurrentProximityUpdate.cancelUpdates();
                this.mCurrentProximityUpdate = null;
                return;
            }
            Slog.w(LOG_TAG, "Cannot stop a non-current callback");
        }
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    protected void freeIfInactiveLocked() {
        this.mAttentionHandler.removeMessages(1);
        this.mAttentionHandler.sendEmptyMessageDelayed(1, 60000L);
    }

    @GuardedBy({"mLock"})
    private void cancelAfterTimeoutLocked(long j) {
        this.mAttentionHandler.sendEmptyMessageDelayed(2, j);
    }

    private static String getServiceConfigPackage(Context context) {
        return context.getPackageManager().getAttentionServicePackageName();
    }

    private void awaitServiceBinding(long j) {
        try {
            this.mServiceBindingLatch.await(j, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Slog.e(LOG_TAG, "Interrupted while waiting to bind Attention Service.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ComponentName resolveAttentionService(Context context) {
        int i;
        String str;
        ServiceInfo serviceInfo;
        String serviceConfigPackage = getServiceConfigPackage(context);
        if (!TextUtils.isEmpty(sTestAttentionServicePackage)) {
            str = sTestAttentionServicePackage;
            i = 128;
        } else {
            if (!TextUtils.isEmpty(serviceConfigPackage)) {
                i = AudioDevice.OUT_FM;
                str = serviceConfigPackage;
            }
            return null;
        }
        ResolveInfo resolveService = context.getPackageManager().resolveService(new Intent("android.service.attention.AttentionService").setPackage(str), i);
        if (resolveService == null || (serviceInfo = resolveService.serviceInfo) == null) {
            Slog.wtf(LOG_TAG, String.format("Service %s not found in package %s", "android.service.attention.AttentionService", serviceConfigPackage));
            return null;
        }
        if ("android.permission.BIND_ATTENTION_SERVICE".equals(serviceInfo.permission)) {
            return serviceInfo.getComponentName();
        }
        Slog.e(LOG_TAG, String.format("Service %s should require %s permission. Found %s permission", serviceInfo.getComponentName(), "android.permission.BIND_ATTENTION_SERVICE", serviceInfo.permission));
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("Attention Manager Service (dumpsys attention) state:\n");
        indentingPrintWriter.println("isServiceEnabled=" + this.mIsServiceEnabled);
        indentingPrintWriter.println("mStaleAfterMillis=" + this.mStaleAfterMillis);
        indentingPrintWriter.println("AttentionServicePackageName=" + getServiceConfigPackage(this.mContext));
        indentingPrintWriter.println("Resolved component:");
        if (this.mComponentName != null) {
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("Component=" + this.mComponentName.getPackageName());
            indentingPrintWriter.println("Class=" + this.mComponentName.getClassName());
            indentingPrintWriter.decreaseIndent();
        }
        synchronized (this.mLock) {
            indentingPrintWriter.println("binding=" + this.mBinding);
            indentingPrintWriter.println("current attention check:");
            AttentionCheck attentionCheck = this.mCurrentAttentionCheck;
            if (attentionCheck != null) {
                attentionCheck.dump(indentingPrintWriter);
            }
            AttentionCheckCacheBuffer attentionCheckCacheBuffer = this.mAttentionCheckCacheBuffer;
            if (attentionCheckCacheBuffer != null) {
                attentionCheckCacheBuffer.dump(indentingPrintWriter);
            }
            ProximityUpdate proximityUpdate = this.mCurrentProximityUpdate;
            if (proximityUpdate != null) {
                proximityUpdate.dump(indentingPrintWriter);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class LocalService extends AttentionManagerInternal {
        private LocalService() {
        }

        public boolean isAttentionServiceSupported() {
            return AttentionManagerService.this.mIsServiceEnabled;
        }

        public boolean checkAttention(long j, AttentionManagerInternal.AttentionCallbackInternal attentionCallbackInternal) {
            return AttentionManagerService.this.checkAttention(j, attentionCallbackInternal);
        }

        public void cancelAttentionCheck(AttentionManagerInternal.AttentionCallbackInternal attentionCallbackInternal) {
            AttentionManagerService.this.cancelAttentionCheck(attentionCallbackInternal);
        }

        public boolean onStartProximityUpdates(AttentionManagerInternal.ProximityUpdateCallbackInternal proximityUpdateCallbackInternal) {
            return AttentionManagerService.this.onStartProximityUpdates(proximityUpdateCallbackInternal);
        }

        public void onStopProximityUpdates(AttentionManagerInternal.ProximityUpdateCallbackInternal proximityUpdateCallbackInternal) {
            AttentionManagerService.this.onStopProximityUpdates(proximityUpdateCallbackInternal);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AttentionCheckCacheBuffer {
        private final AttentionCheckCache[] mQueue = new AttentionCheckCache[5];
        private int mStartIndex = 0;
        private int mSize = 0;

        AttentionCheckCacheBuffer() {
        }

        public AttentionCheckCache getLast() {
            int i = this.mStartIndex;
            int i2 = ((i + r1) - 1) % 5;
            if (this.mSize == 0) {
                return null;
            }
            return this.mQueue[i2];
        }

        public void add(AttentionCheckCache attentionCheckCache) {
            int i = this.mStartIndex;
            int i2 = this.mSize;
            this.mQueue[(i + i2) % 5] = attentionCheckCache;
            if (i2 == 5) {
                this.mStartIndex = i + 1;
            } else {
                this.mSize = i2 + 1;
            }
        }

        public AttentionCheckCache get(int i) {
            if (i >= this.mSize) {
                return null;
            }
            return this.mQueue[(this.mStartIndex + i) % 5];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("attention check cache:");
            for (int i = 0; i < this.mSize; i++) {
                AttentionCheckCache attentionCheckCache = get(i);
                if (attentionCheckCache != null) {
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.println("timestamp=" + attentionCheckCache.mTimestamp);
                    indentingPrintWriter.println("result=" + attentionCheckCache.mResult);
                    indentingPrintWriter.decreaseIndent();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AttentionCheckCache {
        private final long mLastComputed;
        private final int mResult;
        private final long mTimestamp;

        AttentionCheckCache(long j, int i, long j2) {
            this.mLastComputed = j;
            this.mResult = i;
            this.mTimestamp = j2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AttentionCheck {
        private final AttentionManagerInternal.AttentionCallbackInternal mCallbackInternal;
        private final IAttentionCallback mIAttentionCallback;
        private boolean mIsDispatched;
        private boolean mIsFulfilled;

        AttentionCheck(final AttentionManagerInternal.AttentionCallbackInternal attentionCallbackInternal, final AttentionManagerService attentionManagerService) {
            this.mCallbackInternal = attentionCallbackInternal;
            this.mIAttentionCallback = new IAttentionCallback.Stub() { // from class: com.android.server.attention.AttentionManagerService.AttentionCheck.1
                public void onSuccess(int i, long j) {
                    if (AttentionCheck.this.mIsFulfilled) {
                        return;
                    }
                    AttentionCheck.this.mIsFulfilled = true;
                    attentionCallbackInternal.onSuccess(i, j);
                    logStats(i);
                    attentionManagerService.appendResultToAttentionCacheBuffer(new AttentionCheckCache(SystemClock.uptimeMillis(), i, j));
                }

                public void onFailure(int i) {
                    if (AttentionCheck.this.mIsFulfilled) {
                        return;
                    }
                    AttentionCheck.this.mIsFulfilled = true;
                    attentionCallbackInternal.onFailure(i);
                    logStats(i);
                }

                private void logStats(int i) {
                    FrameworkStatsLog.write(143, i);
                }
            };
        }

        void cancelInternal() {
            this.mIsFulfilled = true;
            this.mCallbackInternal.onFailure(3);
        }

        void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("is dispatched=" + this.mIsDispatched);
            indentingPrintWriter.println("is fulfilled:=" + this.mIsFulfilled);
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ProximityUpdate {
        private final AttentionManagerInternal.ProximityUpdateCallbackInternal mCallbackInternal;
        private final IProximityUpdateCallback mIProximityUpdateCallback;
        private boolean mStartedUpdates;

        ProximityUpdate(AttentionManagerInternal.ProximityUpdateCallbackInternal proximityUpdateCallbackInternal) {
            this.mCallbackInternal = proximityUpdateCallbackInternal;
            this.mIProximityUpdateCallback = new IProximityUpdateCallback.Stub() { // from class: com.android.server.attention.AttentionManagerService.ProximityUpdate.1
                public void onProximityUpdate(double d) {
                    ProximityUpdate.this.mCallbackInternal.onProximityUpdate(d);
                    synchronized (AttentionManagerService.this.mLock) {
                        AttentionManagerService.this.freeIfInactiveLocked();
                    }
                }
            };
        }

        boolean startUpdates() {
            synchronized (AttentionManagerService.this.mLock) {
                if (this.mStartedUpdates) {
                    Slog.w(AttentionManagerService.LOG_TAG, "Already registered to a proximity service.");
                    return false;
                }
                IAttentionService iAttentionService = AttentionManagerService.this.mService;
                if (iAttentionService == null) {
                    Slog.w(AttentionManagerService.LOG_TAG, "There is no service bound. Proximity update request rejected.");
                    return false;
                }
                try {
                    iAttentionService.onStartProximityUpdates(this.mIProximityUpdateCallback);
                    this.mStartedUpdates = true;
                    return true;
                } catch (RemoteException e) {
                    Slog.e(AttentionManagerService.LOG_TAG, "Cannot call into the AttentionService", e);
                    return false;
                }
            }
        }

        void cancelUpdates() {
            synchronized (AttentionManagerService.this.mLock) {
                if (this.mStartedUpdates) {
                    IAttentionService iAttentionService = AttentionManagerService.this.mService;
                    if (iAttentionService == null) {
                        this.mStartedUpdates = false;
                        return;
                    }
                    try {
                        iAttentionService.onStopProximityUpdates();
                        this.mStartedUpdates = false;
                    } catch (RemoteException e) {
                        Slog.e(AttentionManagerService.LOG_TAG, "Cannot call into the AttentionService", e);
                    }
                }
            }
        }

        void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("is StartedUpdates=" + this.mStartedUpdates);
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appendResultToAttentionCacheBuffer(AttentionCheckCache attentionCheckCache) {
        synchronized (this.mLock) {
            if (this.mAttentionCheckCacheBuffer == null) {
                this.mAttentionCheckCacheBuffer = new AttentionCheckCacheBuffer();
            }
            this.mAttentionCheckCacheBuffer.add(attentionCheckCache);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AttentionServiceConnection implements ServiceConnection {
        private AttentionServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            init(IAttentionService.Stub.asInterface(iBinder));
            AttentionManagerService.this.mServiceBindingLatch.countDown();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            cleanupService();
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            cleanupService();
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName componentName) {
            cleanupService();
        }

        void cleanupService() {
            init(null);
            AttentionManagerService.this.mServiceBindingLatch = new CountDownLatch(1);
        }

        private void init(IAttentionService iAttentionService) {
            synchronized (AttentionManagerService.this.mLock) {
                AttentionManagerService attentionManagerService = AttentionManagerService.this;
                attentionManagerService.mService = iAttentionService;
                attentionManagerService.mBinding = false;
                AttentionManagerService.this.handlePendingCallbackLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void handlePendingCallbackLocked() {
        AttentionCheck attentionCheck = this.mCurrentAttentionCheck;
        if (attentionCheck != null && !attentionCheck.mIsDispatched) {
            IAttentionService iAttentionService = this.mService;
            if (iAttentionService != null) {
                try {
                    iAttentionService.checkAttention(this.mCurrentAttentionCheck.mIAttentionCallback);
                    this.mCurrentAttentionCheck.mIsDispatched = true;
                } catch (RemoteException unused) {
                    Slog.e(LOG_TAG, "Cannot call into the AttentionService");
                }
            } else {
                this.mCurrentAttentionCheck.mCallbackInternal.onFailure(2);
            }
        }
        ProximityUpdate proximityUpdate = this.mCurrentProximityUpdate;
        if (proximityUpdate == null || !proximityUpdate.mStartedUpdates) {
            return;
        }
        IAttentionService iAttentionService2 = this.mService;
        if (iAttentionService2 != null) {
            try {
                iAttentionService2.onStartProximityUpdates(this.mCurrentProximityUpdate.mIProximityUpdateCallback);
                return;
            } catch (RemoteException e) {
                Slog.e(LOG_TAG, "Cannot call into the AttentionService", e);
                return;
            }
        }
        this.mCurrentProximityUpdate.cancelUpdates();
        this.mCurrentProximityUpdate = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AttentionHandler extends Handler {
        private static final int ATTENTION_CHECK_TIMEOUT = 2;
        private static final int CHECK_CONNECTION_EXPIRATION = 1;

        AttentionHandler() {
            super(Looper.myLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                synchronized (AttentionManagerService.this.mLock) {
                    AttentionManagerService.this.cancelAndUnbindLocked();
                }
            } else {
                if (i != 2) {
                    return;
                }
                synchronized (AttentionManagerService.this.mLock) {
                    AttentionManagerService.this.cancel();
                }
            }
        }
    }

    @GuardedBy({"mLock"})
    @VisibleForTesting
    void cancel() {
        if (this.mCurrentAttentionCheck.mIsFulfilled) {
            return;
        }
        IAttentionService iAttentionService = this.mService;
        if (iAttentionService == null) {
            this.mCurrentAttentionCheck.cancelInternal();
            return;
        }
        try {
            iAttentionService.cancelAttentionCheck(this.mCurrentAttentionCheck.mIAttentionCallback);
        } catch (RemoteException unused) {
            Slog.e(LOG_TAG, "Unable to cancel attention check");
            this.mCurrentAttentionCheck.cancelInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void cancelAndUnbindLocked() {
        synchronized (this.mLock) {
            if (this.mCurrentAttentionCheck != null) {
                cancel();
            }
            ProximityUpdate proximityUpdate = this.mCurrentProximityUpdate;
            if (proximityUpdate != null) {
                proximityUpdate.cancelUpdates();
            }
            if (this.mService == null) {
                return;
            }
            this.mAttentionHandler.post(new Runnable() { // from class: com.android.server.attention.AttentionManagerService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AttentionManagerService.this.lambda$cancelAndUnbindLocked$1();
                }
            });
            this.mConnection.cleanupService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelAndUnbindLocked$1() {
        try {
            this.mContext.unbindService(this.mConnection);
        } catch (IllegalArgumentException e) {
            Slog.e(LOG_TAG, "Cannot set mBinding to false", e);
        } catch (IllegalStateException e2) {
            Slog.e(LOG_TAG, "Cannot set mBinding to false", e2);
        }
    }

    @GuardedBy({"mLock"})
    private void bindLocked() {
        if (this.mBinding || this.mService != null) {
            return;
        }
        this.mBinding = true;
        this.mAttentionHandler.post(new Runnable() { // from class: com.android.server.attention.AttentionManagerService$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AttentionManagerService.this.lambda$bindLocked$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindLocked$2() {
        this.mContext.bindServiceAsUser(new Intent("android.service.attention.AttentionService").setComponent(this.mComponentName), this.mConnection, 67112961, UserHandle.CURRENT);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class ScreenStateReceiver extends BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                synchronized (AttentionManagerService.this.mLock) {
                    AttentionManagerService.this.cancelAndUnbindLocked();
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class AttentionManagerServiceShellCommand extends ShellCommand {
        final TestableAttentionCallbackInternal mTestableAttentionCallback;
        final TestableProximityUpdateCallbackInternal mTestableProximityUpdateCallback;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public class TestableAttentionCallbackInternal extends AttentionManagerInternal.AttentionCallbackInternal {
            private int mLastCallbackCode = -1;

            TestableAttentionCallbackInternal() {
            }

            public void onSuccess(int i, long j) {
                this.mLastCallbackCode = i;
            }

            public void onFailure(int i) {
                this.mLastCallbackCode = i;
            }

            public void reset() {
                this.mLastCallbackCode = -1;
            }

            public int getLastCallbackCode() {
                return this.mLastCallbackCode;
            }
        }

        private AttentionManagerServiceShellCommand() {
            this.mTestableAttentionCallback = new TestableAttentionCallbackInternal();
            this.mTestableProximityUpdateCallback = new TestableProximityUpdateCallbackInternal();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public class TestableProximityUpdateCallbackInternal implements AttentionManagerInternal.ProximityUpdateCallbackInternal {
            private double mLastCallbackCode = -1.0d;

            TestableProximityUpdateCallbackInternal() {
            }

            public void onProximityUpdate(double d) {
                this.mLastCallbackCode = d;
            }

            public void reset() {
                this.mLastCallbackCode = -1.0d;
            }

            public double getLastCallbackCode() {
                return this.mLastCallbackCode;
            }
        }

        public int onCommand(String str) {
            char c;
            if (str == null) {
                return handleDefaultCommands(str);
            }
            PrintWriter errPrintWriter = getErrPrintWriter();
            try {
                char c2 = 0;
                switch (str.hashCode()) {
                    case -1208709968:
                        if (str.equals("getLastTestCallbackCode")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1002424240:
                        if (str.equals("getAttentionServiceComponent")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case -415045819:
                        if (str.equals("setTestableAttentionService")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 3045982:
                        if (str.equals("call")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1048378748:
                        if (str.equals("getLastTestProximityUpdateCallbackCode")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1193447472:
                        if (str.equals("clearTestableAttentionService")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0) {
                    return cmdResolveAttentionServiceComponent();
                }
                if (c != 1) {
                    if (c == 2) {
                        return cmdSetTestableAttentionService(getNextArgRequired());
                    }
                    if (c == 3) {
                        return cmdClearTestableAttentionService();
                    }
                    if (c == 4) {
                        return cmdGetLastTestCallbackCode();
                    }
                    if (c == 5) {
                        return cmdGetLastTestProximityUpdateCallbackCode();
                    }
                    return handleDefaultCommands(str);
                }
                String nextArgRequired = getNextArgRequired();
                switch (nextArgRequired.hashCode()) {
                    case -1571871954:
                        if (nextArgRequired.equals("onStartProximityUpdates")) {
                            c2 = 2;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 685821932:
                        if (nextArgRequired.equals("onStopProximityUpdates")) {
                            c2 = 3;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 763077136:
                        if (nextArgRequired.equals("cancelCheckAttention")) {
                            c2 = 1;
                            break;
                        }
                        c2 = 65535;
                        break;
                    case 1485997302:
                        if (nextArgRequired.equals("checkAttention")) {
                            break;
                        }
                        c2 = 65535;
                        break;
                    default:
                        c2 = 65535;
                        break;
                }
                if (c2 == 0) {
                    return cmdCallCheckAttention();
                }
                if (c2 == 1) {
                    return cmdCallCancelAttention();
                }
                if (c2 == 2) {
                    return cmdCallOnStartProximityUpdates();
                }
                if (c2 == 3) {
                    return cmdCallOnStopProximityUpdates();
                }
                throw new IllegalArgumentException("Invalid argument");
            } catch (IllegalArgumentException e) {
                errPrintWriter.println("Error: " + e.getMessage());
                return -1;
            }
        }

        private int cmdSetTestableAttentionService(String str) {
            PrintWriter outPrintWriter = getOutPrintWriter();
            if (TextUtils.isEmpty(str)) {
                outPrintWriter.println("false");
                return 0;
            }
            AttentionManagerService.sTestAttentionServicePackage = str;
            resetStates();
            outPrintWriter.println(AttentionManagerService.this.mComponentName != null ? "true" : "false");
            return 0;
        }

        private int cmdClearTestableAttentionService() {
            AttentionManagerService.sTestAttentionServicePackage = "";
            this.mTestableAttentionCallback.reset();
            this.mTestableProximityUpdateCallback.reset();
            resetStates();
            return 0;
        }

        private int cmdCallCheckAttention() {
            getOutPrintWriter().println(AttentionManagerService.this.checkAttention(2000L, this.mTestableAttentionCallback) ? "true" : "false");
            return 0;
        }

        private int cmdCallCancelAttention() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            AttentionManagerService.this.cancelAttentionCheck(this.mTestableAttentionCallback);
            outPrintWriter.println("true");
            return 0;
        }

        private int cmdCallOnStartProximityUpdates() {
            getOutPrintWriter().println(AttentionManagerService.this.onStartProximityUpdates(this.mTestableProximityUpdateCallback) ? "true" : "false");
            return 0;
        }

        private int cmdCallOnStopProximityUpdates() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            AttentionManagerService.this.onStopProximityUpdates(this.mTestableProximityUpdateCallback);
            outPrintWriter.println("true");
            return 0;
        }

        private int cmdResolveAttentionServiceComponent() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            ComponentName resolveAttentionService = AttentionManagerService.resolveAttentionService(AttentionManagerService.this.mContext);
            outPrintWriter.println(resolveAttentionService != null ? resolveAttentionService.flattenToShortString() : "");
            return 0;
        }

        private int cmdGetLastTestCallbackCode() {
            getOutPrintWriter().println(this.mTestableAttentionCallback.getLastCallbackCode());
            return 0;
        }

        private int cmdGetLastTestProximityUpdateCallbackCode() {
            getOutPrintWriter().println(this.mTestableProximityUpdateCallback.getLastCallbackCode());
            return 0;
        }

        private void resetStates() {
            synchronized (AttentionManagerService.this.mLock) {
                AttentionManagerService attentionManagerService = AttentionManagerService.this;
                attentionManagerService.mCurrentProximityUpdate = null;
                attentionManagerService.cancelAndUnbindLocked();
            }
            AttentionManagerService attentionManagerService2 = AttentionManagerService.this;
            attentionManagerService2.mComponentName = AttentionManagerService.resolveAttentionService(attentionManagerService2.mContext);
        }

        public void onHelp() {
            PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("Attention commands: ");
            outPrintWriter.println("  setTestableAttentionService <service_package>: Bind to a custom implementation of attention service");
            outPrintWriter.println("  ---<service_package>:");
            outPrintWriter.println("       := Package containing the Attention Service implementation to bind to");
            outPrintWriter.println("  ---returns:");
            outPrintWriter.println("       := true, if was bound successfully");
            outPrintWriter.println("       := false, if was not bound successfully");
            outPrintWriter.println("  clearTestableAttentionService: Undo custom bindings. Revert to previous behavior");
            outPrintWriter.println("  getAttentionServiceComponent: Get the current service component string");
            outPrintWriter.println("  ---returns:");
            outPrintWriter.println("       := If valid, the component string (in shorten form) for the currently bound service.");
            outPrintWriter.println("       := else, empty string");
            outPrintWriter.println("  call checkAttention: Calls check attention");
            outPrintWriter.println("  ---returns:");
            outPrintWriter.println("       := true, if the call was successfully dispatched to the service implementation. (to see the result, call getLastTestCallbackCode)");
            outPrintWriter.println("       := false, otherwise");
            outPrintWriter.println("  call cancelCheckAttention: Cancels check attention");
            outPrintWriter.println("  call onStartProximityUpdates: Calls onStartProximityUpdates");
            outPrintWriter.println("  ---returns:");
            outPrintWriter.println("       := true, if the request was successfully dispatched to the service implementation. (to see the result, call getLastTestProximityUpdateCallbackCode)");
            outPrintWriter.println("       := false, otherwise");
            outPrintWriter.println("  call onStopProximityUpdates: Cancels proximity updates");
            outPrintWriter.println("  getLastTestCallbackCode");
            outPrintWriter.println("  ---returns:");
            outPrintWriter.println("       := An integer, representing the last callback code received from the bounded implementation. If none, it will return -1");
            outPrintWriter.println("  getLastTestProximityUpdateCallbackCode");
            outPrintWriter.println("  ---returns:");
            outPrintWriter.println("       := A double, representing the last proximity value received from the bounded implementation. If none, it will return -1.0");
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class BinderService extends Binder {
        AttentionManagerServiceShellCommand mAttentionManagerServiceShellCommand;

        private BinderService() {
            this.mAttentionManagerServiceShellCommand = new AttentionManagerServiceShellCommand();
        }

        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            this.mAttentionManagerServiceShellCommand.exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(AttentionManagerService.this.mContext, AttentionManagerService.LOG_TAG, printWriter)) {
                AttentionManagerService.this.dumpInternal(new IndentingPrintWriter(printWriter, "  "));
            }
        }
    }
}
