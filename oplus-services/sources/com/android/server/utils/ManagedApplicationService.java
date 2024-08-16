package com.android.server.utils;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Slog;
import com.android.server.utils.ManagedApplicationService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ManagedApplicationService {
    private static final int MAX_RETRY_COUNT = 4;
    private static final long MAX_RETRY_DURATION_MS = 16000;
    private static final long MIN_RETRY_DURATION_MS = 2000;
    public static final int RETRY_BEST_EFFORT = 3;
    public static final int RETRY_FOREVER = 1;
    public static final int RETRY_NEVER = 2;
    private static final long RETRY_RESET_TIME_MS = 64000;
    private IInterface mBoundInterface;
    private final BinderChecker mChecker;
    private final int mClientLabel;
    private final ComponentName mComponent;
    private ServiceConnection mConnection;
    private final Context mContext;
    private final EventCallback mEventCb;
    private final Handler mHandler;
    private final boolean mIsImportant;
    private long mLastRetryTimeMs;
    private PendingEvent mPendingEvent;
    private int mRetryCount;
    private final int mRetryType;
    private boolean mRetrying;
    private final String mSettingsAction;
    private final int mUserId;
    private final String TAG = getClass().getSimpleName();
    private final Runnable mRetryRunnable = new Runnable() { // from class: com.android.server.utils.ManagedApplicationService$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            ManagedApplicationService.this.doRetry();
        }
    };
    private final Object mLock = new Object();
    private long mNextRetryDurationMs = MIN_RETRY_DURATION_MS;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface BinderChecker {
        IInterface asInterface(IBinder iBinder);

        boolean checkType(IInterface iInterface);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface EventCallback {
        void onServiceEvent(LogEvent logEvent);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface LogFormattable {
        String toLogString(SimpleDateFormat simpleDateFormat);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface PendingEvent {
        void runEvent(IInterface iInterface) throws RemoteException;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LogEvent implements LogFormattable {
        public static final int EVENT_BINDING_DIED = 3;
        public static final int EVENT_CONNECTED = 1;
        public static final int EVENT_DISCONNECTED = 2;
        public static final int EVENT_STOPPED_PERMANENTLY = 4;
        public final ComponentName component;
        public final int event;
        public final long timestamp;

        public static String eventToString(int i) {
            return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "Unknown Event Occurred" : "Permanently Stopped" : "Binding Died For" : "Disconnected" : "Connected";
        }

        public LogEvent(long j, ComponentName componentName, int i) {
            this.timestamp = j;
            this.component = componentName;
            this.event = i;
        }

        @Override // com.android.server.utils.ManagedApplicationService.LogFormattable
        public String toLogString(SimpleDateFormat simpleDateFormat) {
            StringBuilder sb = new StringBuilder();
            sb.append(simpleDateFormat.format(new Date(this.timestamp)));
            sb.append("   ");
            sb.append(eventToString(this.event));
            sb.append(" Managed Service: ");
            ComponentName componentName = this.component;
            sb.append(componentName == null ? "None" : componentName.flattenToString());
            return sb.toString();
        }
    }

    private ManagedApplicationService(Context context, ComponentName componentName, int i, int i2, String str, BinderChecker binderChecker, boolean z, int i3, Handler handler, EventCallback eventCallback) {
        this.mContext = context;
        this.mComponent = componentName;
        this.mUserId = i;
        this.mClientLabel = i2;
        this.mSettingsAction = str;
        this.mChecker = binderChecker;
        this.mIsImportant = z;
        this.mRetryType = i3;
        this.mHandler = handler;
        this.mEventCb = eventCallback;
    }

    public static ManagedApplicationService build(Context context, ComponentName componentName, int i, int i2, String str, BinderChecker binderChecker, boolean z, int i3, Handler handler, EventCallback eventCallback) {
        return new ManagedApplicationService(context, componentName, i, i2, str, binderChecker, z, i3, handler, eventCallback);
    }

    public int getUserId() {
        return this.mUserId;
    }

    public ComponentName getComponent() {
        return this.mComponent;
    }

    public boolean disconnectIfNotMatching(ComponentName componentName, int i) {
        if (matches(componentName, i)) {
            return false;
        }
        disconnect();
        return true;
    }

    public void sendEvent(PendingEvent pendingEvent) {
        IInterface iInterface;
        synchronized (this.mLock) {
            iInterface = this.mBoundInterface;
            if (iInterface == null) {
                this.mPendingEvent = pendingEvent;
            }
        }
        if (iInterface != null) {
            try {
                pendingEvent.runEvent(iInterface);
            } catch (RemoteException | RuntimeException e) {
                Slog.e(this.TAG, "Received exception from user service: ", e);
            }
        }
    }

    public void disconnect() {
        synchronized (this.mLock) {
            ServiceConnection serviceConnection = this.mConnection;
            if (serviceConnection == null) {
                return;
            }
            this.mContext.unbindService(serviceConnection);
            this.mConnection = null;
            this.mBoundInterface = null;
        }
    }

    public void connect() {
        synchronized (this.mLock) {
            if (this.mConnection != null) {
                return;
            }
            Intent component = new Intent().setComponent(this.mComponent);
            int i = this.mClientLabel;
            if (i != 0) {
                component.putExtra("android.intent.extra.client_label", i);
            }
            if (this.mSettingsAction != null) {
                component.putExtra("android.intent.extra.client_intent", PendingIntent.getActivity(this.mContext, 0, new Intent(this.mSettingsAction), 67108864));
            }
            AnonymousClass1 anonymousClass1 = new AnonymousClass1();
            this.mConnection = anonymousClass1;
            try {
                if (!this.mContext.bindServiceAsUser(component, anonymousClass1, this.mIsImportant ? 67108929 : 67108865, new UserHandle(this.mUserId))) {
                    Slog.w(this.TAG, "Unable to bind service: " + component);
                    startRetriesLocked();
                }
            } catch (SecurityException e) {
                Slog.w(this.TAG, "Unable to bind service: " + component, e);
                startRetriesLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.utils.ManagedApplicationService$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass1 implements ServiceConnection {
        AnonymousClass1() {
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            final long currentTimeMillis = System.currentTimeMillis();
            Slog.w(ManagedApplicationService.this.TAG, "Service binding died: " + componentName);
            synchronized (ManagedApplicationService.this.mLock) {
                if (ManagedApplicationService.this.mConnection != this) {
                    return;
                }
                ManagedApplicationService.this.mHandler.post(new Runnable() { // from class: com.android.server.utils.ManagedApplicationService$1$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ManagedApplicationService.AnonymousClass1.this.lambda$onBindingDied$0(currentTimeMillis);
                    }
                });
                ManagedApplicationService.this.mBoundInterface = null;
                ManagedApplicationService.this.startRetriesLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindingDied$0(long j) {
            ManagedApplicationService.this.mEventCb.onServiceEvent(new LogEvent(j, ManagedApplicationService.this.mComponent, 3));
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PendingEvent pendingEvent;
            final long currentTimeMillis = System.currentTimeMillis();
            Slog.i(ManagedApplicationService.this.TAG, "Service connected: " + componentName);
            synchronized (ManagedApplicationService.this.mLock) {
                if (ManagedApplicationService.this.mConnection != this) {
                    return;
                }
                ManagedApplicationService.this.mHandler.post(new Runnable() { // from class: com.android.server.utils.ManagedApplicationService$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ManagedApplicationService.AnonymousClass1.this.lambda$onServiceConnected$1(currentTimeMillis);
                    }
                });
                ManagedApplicationService.this.stopRetriesLocked();
                IInterface iInterface = null;
                ManagedApplicationService.this.mBoundInterface = null;
                if (ManagedApplicationService.this.mChecker != null) {
                    ManagedApplicationService managedApplicationService = ManagedApplicationService.this;
                    managedApplicationService.mBoundInterface = managedApplicationService.mChecker.asInterface(iBinder);
                    if (!ManagedApplicationService.this.mChecker.checkType(ManagedApplicationService.this.mBoundInterface)) {
                        ManagedApplicationService.this.mBoundInterface = null;
                        Slog.w(ManagedApplicationService.this.TAG, "Invalid binder from " + componentName);
                        ManagedApplicationService.this.startRetriesLocked();
                        return;
                    }
                    IInterface iInterface2 = ManagedApplicationService.this.mBoundInterface;
                    pendingEvent = ManagedApplicationService.this.mPendingEvent;
                    ManagedApplicationService.this.mPendingEvent = null;
                    iInterface = iInterface2;
                } else {
                    pendingEvent = null;
                }
                if (iInterface == null || pendingEvent == null) {
                    return;
                }
                try {
                    pendingEvent.runEvent(iInterface);
                } catch (RemoteException | RuntimeException e) {
                    Slog.e(ManagedApplicationService.this.TAG, "Received exception from user service: ", e);
                    ManagedApplicationService.this.startRetriesLocked();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceConnected$1(long j) {
            ManagedApplicationService.this.mEventCb.onServiceEvent(new LogEvent(j, ManagedApplicationService.this.mComponent, 1));
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            final long currentTimeMillis = System.currentTimeMillis();
            Slog.w(ManagedApplicationService.this.TAG, "Service disconnected: " + componentName);
            synchronized (ManagedApplicationService.this.mLock) {
                if (ManagedApplicationService.this.mConnection != this) {
                    return;
                }
                ManagedApplicationService.this.mHandler.post(new Runnable() { // from class: com.android.server.utils.ManagedApplicationService$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ManagedApplicationService.AnonymousClass1.this.lambda$onServiceDisconnected$2(currentTimeMillis);
                    }
                });
                ManagedApplicationService.this.mBoundInterface = null;
                ManagedApplicationService.this.startRetriesLocked();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceDisconnected$2(long j) {
            ManagedApplicationService.this.mEventCb.onServiceEvent(new LogEvent(j, ManagedApplicationService.this.mComponent, 2));
        }
    }

    private boolean matches(ComponentName componentName, int i) {
        return Objects.equals(this.mComponent, componentName) && this.mUserId == i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRetriesLocked() {
        if (checkAndDeliverServiceDiedCbLocked()) {
            disconnect();
        } else {
            if (this.mRetrying) {
                return;
            }
            this.mRetrying = true;
            queueRetryLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRetriesLocked() {
        this.mRetrying = false;
        this.mHandler.removeCallbacks(this.mRetryRunnable);
    }

    private void queueRetryLocked() {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - this.mLastRetryTimeMs > RETRY_RESET_TIME_MS) {
            this.mNextRetryDurationMs = MIN_RETRY_DURATION_MS;
            this.mRetryCount = 0;
        }
        this.mLastRetryTimeMs = uptimeMillis;
        this.mHandler.postDelayed(this.mRetryRunnable, this.mNextRetryDurationMs);
        this.mNextRetryDurationMs = Math.min(this.mNextRetryDurationMs * 2, MAX_RETRY_DURATION_MS);
        this.mRetryCount++;
    }

    private boolean checkAndDeliverServiceDiedCbLocked() {
        int i = this.mRetryType;
        if (i != 2 && (i != 3 || this.mRetryCount < 4)) {
            return false;
        }
        Slog.e(this.TAG, "Service " + this.mComponent + " has died too much, not retrying.");
        if (this.mEventCb == null) {
            return true;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        this.mHandler.post(new Runnable() { // from class: com.android.server.utils.ManagedApplicationService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ManagedApplicationService.this.lambda$checkAndDeliverServiceDiedCbLocked$0(currentTimeMillis);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkAndDeliverServiceDiedCbLocked$0(long j) {
        this.mEventCb.onServiceEvent(new LogEvent(j, this.mComponent, 4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRetry() {
        synchronized (this.mLock) {
            if (this.mConnection == null) {
                return;
            }
            if (this.mRetrying) {
                Slog.i(this.TAG, "Attempting to reconnect " + this.mComponent + "...");
                disconnect();
                if (checkAndDeliverServiceDiedCbLocked()) {
                    return;
                }
                queueRetryLocked();
                connect();
            }
        }
    }
}
