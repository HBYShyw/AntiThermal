package com.android.server.soundtrigger;

import android.app.ActivityThread;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.PermissionChecker;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.soundtrigger.ConversionUtil;
import android.hardware.soundtrigger.IRecognitionStatusCallback;
import android.hardware.soundtrigger.ModelParams;
import android.hardware.soundtrigger.SoundTrigger;
import android.hardware.soundtrigger.SoundTriggerModule;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.permission.ClearCallingIdentityContext;
import android.media.permission.Identity;
import android.media.permission.IdentityContext;
import android.media.permission.PermissionUtil;
import android.media.permission.SafeCloseable;
import android.media.soundtrigger.ISoundTriggerDetectionService;
import android.media.soundtrigger.ISoundTriggerDetectionServiceClient;
import android.media.soundtrigger_middleware.ISoundTriggerInjection;
import android.media.soundtrigger_middleware.ISoundTriggerMiddlewareService;
import android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ParcelUuid;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.ISoundTriggerService;
import com.android.internal.app.ISoundTriggerSession;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.SoundTriggerInternal;
import com.android.server.SystemService;
import com.android.server.job.controllers.JobStatus;
import com.android.server.soundtrigger.DeviceStateHandler;
import com.android.server.soundtrigger.SoundTriggerEvent;
import com.android.server.soundtrigger.SoundTriggerService;
import com.android.server.utils.EventLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SoundTriggerService extends SystemService {
    private static final boolean DEBUG = true;
    private static final int SESSION_MAX_EVENT_SIZE = 128;
    private static final String TAG = "SoundTriggerService";
    private AppOpsManager mAppOpsManager;
    private final Context mContext;
    private SoundTriggerDbHelper mDbHelper;
    private final Deque<EventLogger> mDetachedSessionEventLoggers;
    private final EventLogger mDeviceEventLogger;
    private final DeviceStateHandler mDeviceStateHandler;
    private final Executor mDeviceStateHandlerExecutor;
    private final LocalSoundTriggerService mLocalSoundTriggerService;
    private final Object mLock;
    private ISoundTriggerMiddlewareService mMiddlewareService;

    @GuardedBy({"mLock"})
    private final ArrayMap<String, NumOps> mNumOpsPerPackage;
    private PackageManager mPackageManager;
    private PhoneCallStateHandler mPhoneCallStateHandler;
    private final EventLogger mServiceEventLogger;
    private final SoundTriggerServiceStub mServiceStub;
    private final Set<EventLogger> mSessionEventLoggers;
    private AtomicInteger mSessionIdCounter;
    private final SoundModelStatTracker mSoundModelStatTracker;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class SoundModelStatTracker {
        private final TreeMap<UUID, SoundModelStat> mModelStats = new TreeMap<>();

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class SoundModelStat {
            long mStartCount = 0;
            long mTotalTimeMsec = 0;
            long mLastStartTimestampMsec = 0;
            long mLastStopTimestampMsec = 0;
            boolean mIsStarted = false;

            SoundModelStat() {
            }
        }

        SoundModelStatTracker() {
        }

        public synchronized void onStart(UUID uuid) {
            SoundModelStat soundModelStat = this.mModelStats.get(uuid);
            if (soundModelStat == null) {
                soundModelStat = new SoundModelStat();
                this.mModelStats.put(uuid, soundModelStat);
            }
            if (soundModelStat.mIsStarted) {
                Slog.w(SoundTriggerService.TAG, "error onStart(): Model " + uuid + " already started");
                return;
            }
            soundModelStat.mStartCount++;
            soundModelStat.mLastStartTimestampMsec = SystemClock.elapsedRealtime();
            soundModelStat.mIsStarted = true;
        }

        public synchronized void onStop(UUID uuid) {
            SoundModelStat soundModelStat = this.mModelStats.get(uuid);
            if (soundModelStat == null) {
                Slog.i(SoundTriggerService.TAG, "error onStop(): Model " + uuid + " has no stats available");
                return;
            }
            if (!soundModelStat.mIsStarted) {
                Slog.w(SoundTriggerService.TAG, "error onStop(): Model " + uuid + " already stopped");
                return;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            soundModelStat.mLastStopTimestampMsec = elapsedRealtime;
            soundModelStat.mTotalTimeMsec += elapsedRealtime - soundModelStat.mLastStartTimestampMsec;
            soundModelStat.mIsStarted = false;
        }

        public synchronized void dump(PrintWriter printWriter) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            printWriter.println("Model Stats:");
            for (Map.Entry<UUID, SoundModelStat> entry : this.mModelStats.entrySet()) {
                UUID key = entry.getKey();
                SoundModelStat value = entry.getValue();
                long j = value.mTotalTimeMsec;
                if (value.mIsStarted) {
                    j += elapsedRealtime - value.mLastStartTimestampMsec;
                }
                printWriter.println(key + ", total_time(msec)=" + j + ", total_count=" + value.mStartCount + ", last_start=" + value.mLastStartTimestampMsec + ", last_stop=" + value.mLastStopTimestampMsec);
            }
        }
    }

    public SoundTriggerService(Context context) {
        super(context);
        this.mLock = new Object();
        this.mServiceEventLogger = new EventLogger(256, "Service");
        EventLogger eventLogger = new EventLogger(256, "Device Event");
        this.mDeviceEventLogger = eventLogger;
        this.mSessionEventLoggers = ConcurrentHashMap.newKeySet(4);
        this.mDetachedSessionEventLoggers = new LinkedBlockingDeque(4);
        this.mSessionIdCounter = new AtomicInteger(0);
        this.mNumOpsPerPackage = new ArrayMap<>();
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        this.mDeviceStateHandlerExecutor = newSingleThreadExecutor;
        this.mContext = context;
        this.mServiceStub = new SoundTriggerServiceStub();
        this.mLocalSoundTriggerService = new LocalSoundTriggerService(context);
        this.mSoundModelStatTracker = new SoundModelStatTracker();
        this.mDeviceStateHandler = new DeviceStateHandler(newSingleThreadExecutor, eventLogger);
    }

    public void onStart() {
        publishBinderService("soundtrigger", this.mServiceStub);
        publishLocalService(SoundTriggerInternal.class, this.mLocalSoundTriggerService);
    }

    public void onBootPhase(int i) {
        Slog.d(TAG, "onBootPhase: " + i + " : " + isSafeMode());
        if (600 == i) {
            this.mDbHelper = new SoundTriggerDbHelper(this.mContext);
            this.mAppOpsManager = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
            this.mPackageManager = this.mContext.getPackageManager();
            final PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
            this.mContext.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.soundtrigger.SoundTriggerService.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("android.os.action.POWER_SAVE_MODE_CHANGED".equals(intent.getAction())) {
                        SoundTriggerService.this.mDeviceStateHandler.onPowerModeChanged(powerManager.getSoundTriggerPowerSaveMode());
                    }
                }
            }, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
            this.mDeviceStateHandler.onPowerModeChanged(powerManager.getSoundTriggerPowerSaveMode());
            this.mPhoneCallStateHandler = new PhoneCallStateHandler((SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class), (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class), this.mDeviceStateHandler);
        }
        this.mMiddlewareService = ISoundTriggerMiddlewareService.Stub.asInterface(ServiceManager.waitForService("soundtrigger_middleware"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: listUnderlyingModuleProperties, reason: merged with bridge method [inline-methods] */
    public List<SoundTrigger.ModuleProperties> lambda$newSoundTriggerHelper$2(Identity identity) {
        Identity identity2 = new Identity();
        identity2.packageName = ActivityThread.currentOpPackageName();
        try {
            return (List) Arrays.stream(this.mMiddlewareService.listModulesAsMiddleman(identity2, identity)).map(new Function() { // from class: com.android.server.soundtrigger.SoundTriggerService$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    SoundTrigger.ModuleProperties aidl2apiModuleDescriptor;
                    aidl2apiModuleDescriptor = ConversionUtil.aidl2apiModuleDescriptor((SoundTriggerModuleDescriptor) obj);
                    return aidl2apiModuleDescriptor;
                }
            }).collect(Collectors.toList());
        } catch (RemoteException unused) {
            throw new ServiceSpecificException(SoundTrigger.STATUS_DEAD_OBJECT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SoundTriggerHelper newSoundTriggerHelper(SoundTrigger.ModuleProperties moduleProperties, EventLogger eventLogger) {
        return newSoundTriggerHelper(moduleProperties, eventLogger, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SoundTriggerHelper newSoundTriggerHelper(SoundTrigger.ModuleProperties moduleProperties, EventLogger eventLogger, final boolean z) {
        final Identity identity = new Identity();
        identity.packageName = ActivityThread.currentOpPackageName();
        final Identity nonNull = IdentityContext.getNonNull();
        List<SoundTrigger.ModuleProperties> lambda$newSoundTriggerHelper$2 = lambda$newSoundTriggerHelper$2(nonNull);
        int id = moduleProperties != null ? moduleProperties.getId() : -1;
        if (id != -1 && !lambda$newSoundTriggerHelper$2.contains(moduleProperties)) {
            throw new IllegalArgumentException("Invalid module properties");
        }
        final int i = id;
        return new SoundTriggerHelper(this.mContext, eventLogger, new Function() { // from class: com.android.server.soundtrigger.SoundTriggerService$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                SoundTriggerModule lambda$newSoundTriggerHelper$1;
                lambda$newSoundTriggerHelper$1 = SoundTriggerService.this.lambda$newSoundTriggerHelper$1(i, identity, nonNull, z, (SoundTrigger.StatusListener) obj);
                return lambda$newSoundTriggerHelper$1;
            }
        }, id, new Supplier() { // from class: com.android.server.soundtrigger.SoundTriggerService$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                List lambda$newSoundTriggerHelper$22;
                lambda$newSoundTriggerHelper$22 = SoundTriggerService.this.lambda$newSoundTriggerHelper$2(nonNull);
                return lambda$newSoundTriggerHelper$22;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ SoundTriggerModule lambda$newSoundTriggerHelper$1(int i, Identity identity, Identity identity2, boolean z, SoundTrigger.StatusListener statusListener) {
        return new SoundTriggerModule(this.mMiddlewareService, i, statusListener, Looper.getMainLooper(), identity, identity2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void detachSessionLogger(EventLogger eventLogger) {
        if (this.mSessionEventLoggers.remove(eventLogger)) {
            while (!this.mDetachedSessionEventLoggers.offerFirst(eventLogger)) {
                this.mDetachedSessionEventLoggers.pollLast();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class MyAppOpsListener implements AppOpsManager.OnOpChangedListener {
        private final Consumer<Boolean> mOnOpModeChanged;
        private final Identity mOriginatorIdentity;

        MyAppOpsListener(Identity identity, Consumer<Boolean> consumer) {
            Objects.requireNonNull(identity);
            this.mOriginatorIdentity = identity;
            Objects.requireNonNull(consumer);
            this.mOnOpModeChanged = consumer;
            try {
                int packageUid = SoundTriggerService.this.mPackageManager.getPackageUid(identity.packageName, PackageManager.PackageInfoFlags.of(0L));
                if (UserHandle.isSameApp(packageUid, identity.uid)) {
                    return;
                }
                throw new SecurityException("Uid " + identity.uid + " attempted to spoof package name " + identity.packageName + " with uid: " + packageUid);
            } catch (PackageManager.NameNotFoundException unused) {
                throw new SecurityException("Package name not found: " + this.mOriginatorIdentity.packageName);
            }
        }

        @Override // android.app.AppOpsManager.OnOpChangedListener
        public void onOpChanged(String str, String str2) {
            if (Objects.equals(str, "android:record_audio")) {
                AppOpsManager appOpsManager = SoundTriggerService.this.mAppOpsManager;
                Identity identity = this.mOriginatorIdentity;
                this.mOnOpModeChanged.accept(Boolean.valueOf(appOpsManager.checkOpNoThrow("android:record_audio", identity.uid, identity.packageName) == 0));
            }
        }

        void forceOpChangeRefresh() {
            onOpChanged("android:record_audio", this.mOriginatorIdentity.packageName);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class SoundTriggerServiceStub extends ISoundTriggerService.Stub {
        SoundTriggerServiceStub() {
        }

        public ISoundTriggerSession attachAsOriginator(Identity identity, SoundTrigger.ModuleProperties moduleProperties, IBinder iBinder) {
            int andIncrement = SoundTriggerService.this.mSessionIdCounter.getAndIncrement();
            SoundTriggerService.this.mServiceEventLogger.enqueue(new SoundTriggerEvent.ServiceEvent(SoundTriggerEvent.ServiceEvent.Type.ATTACH, identity.packageName + "#" + andIncrement));
            SafeCloseable establishIdentityDirect = PermissionUtil.establishIdentityDirect(identity);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("SoundTriggerSessionLogs for package: ");
                String str = identity.packageName;
                Objects.requireNonNull(str);
                sb.append(str);
                sb.append("#");
                sb.append(andIncrement);
                EventLogger eventLogger = new EventLogger(128, sb.toString());
                SoundTriggerService soundTriggerService = SoundTriggerService.this;
                SoundTriggerSessionStub soundTriggerSessionStub = new SoundTriggerSessionStub(iBinder, soundTriggerService.newSoundTriggerHelper(moduleProperties, eventLogger), eventLogger);
                if (establishIdentityDirect != null) {
                    establishIdentityDirect.close();
                }
                return soundTriggerSessionStub;
            } catch (Throwable th) {
                if (establishIdentityDirect != null) {
                    try {
                        establishIdentityDirect.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public ISoundTriggerSession attachAsMiddleman(Identity identity, Identity identity2, SoundTrigger.ModuleProperties moduleProperties, IBinder iBinder) {
            int andIncrement = SoundTriggerService.this.mSessionIdCounter.getAndIncrement();
            SoundTriggerService.this.mServiceEventLogger.enqueue(new SoundTriggerEvent.ServiceEvent(SoundTriggerEvent.ServiceEvent.Type.ATTACH, identity.packageName + "#" + andIncrement));
            SafeCloseable establishIdentityIndirect = PermissionUtil.establishIdentityIndirect(SoundTriggerService.this.mContext, "android.permission.SOUNDTRIGGER_DELEGATE_IDENTITY", identity2, identity);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("SoundTriggerSessionLogs for package: ");
                String str = identity.packageName;
                Objects.requireNonNull(str);
                sb.append(str);
                sb.append("#");
                sb.append(andIncrement);
                EventLogger eventLogger = new EventLogger(128, sb.toString());
                SoundTriggerService soundTriggerService = SoundTriggerService.this;
                SoundTriggerSessionStub soundTriggerSessionStub = new SoundTriggerSessionStub(iBinder, soundTriggerService.newSoundTriggerHelper(moduleProperties, eventLogger), eventLogger);
                if (establishIdentityIndirect != null) {
                    establishIdentityIndirect.close();
                }
                return soundTriggerSessionStub;
            } catch (Throwable th) {
                if (establishIdentityIndirect != null) {
                    try {
                        establishIdentityIndirect.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public List<SoundTrigger.ModuleProperties> listModuleProperties(Identity identity) {
            SoundTriggerService.this.mServiceEventLogger.enqueue(new SoundTriggerEvent.ServiceEvent(SoundTriggerEvent.ServiceEvent.Type.LIST_MODULE, identity.packageName));
            SafeCloseable establishIdentityDirect = PermissionUtil.establishIdentityDirect(identity);
            try {
                List<SoundTrigger.ModuleProperties> lambda$newSoundTriggerHelper$2 = SoundTriggerService.this.lambda$newSoundTriggerHelper$2(identity);
                if (establishIdentityDirect != null) {
                    establishIdentityDirect.close();
                }
                return lambda$newSoundTriggerHelper$2;
            } catch (Throwable th) {
                if (establishIdentityDirect != null) {
                    try {
                        establishIdentityDirect.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void attachInjection(ISoundTriggerInjection iSoundTriggerInjection) {
            if (PermissionChecker.checkCallingPermissionForPreflight(SoundTriggerService.this.mContext, "android.permission.MANAGE_SOUND_TRIGGER", (String) null) != 0) {
                throw new SecurityException();
            }
            try {
                ISoundTriggerMiddlewareService.Stub.asInterface(ServiceManager.waitForService("soundtrigger_middleware")).attachFakeHalInjection(iSoundTriggerInjection);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void setInPhoneCallState(boolean z) {
            Slog.i(SoundTriggerService.TAG, "Overriding phone call state: " + z);
            SoundTriggerService.this.mDeviceStateHandler.onPhoneCallStateChanged(z);
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(SoundTriggerService.this.mContext, SoundTriggerService.TAG, printWriter)) {
                printWriter.println("##Service-Wide logs:");
                SoundTriggerService.this.mServiceEventLogger.dump(printWriter, "  ");
                printWriter.println("\n##Device state logs:");
                SoundTriggerService.this.mDeviceStateHandler.dump(printWriter);
                SoundTriggerService.this.mDeviceEventLogger.dump(printWriter, "  ");
                printWriter.println("\n##Active Session dumps:\n");
                Iterator it = SoundTriggerService.this.mSessionEventLoggers.iterator();
                while (it.hasNext()) {
                    ((EventLogger) it.next()).dump(printWriter, "  ");
                    printWriter.println("");
                }
                printWriter.println("##Detached Session dumps:\n");
                Iterator it2 = SoundTriggerService.this.mDetachedSessionEventLoggers.iterator();
                while (it2.hasNext()) {
                    ((EventLogger) it2.next()).dump(printWriter, "  ");
                    printWriter.println("");
                }
                printWriter.println("##Enrolled db dump:\n");
                SoundTriggerService.this.mDbHelper.dump(printWriter);
                printWriter.println("\n##Sound Model Stats dump:\n");
                SoundTriggerService.this.mSoundModelStatTracker.dump(printWriter);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class SoundTriggerSessionStub extends ISoundTriggerSession.Stub {
        private final MyAppOpsListener mAppOpsListener;
        private final IBinder mClient;
        private final EventLogger mEventLogger;
        private final DeviceStateHandler.DeviceStateListener mListener;
        private final SoundTriggerHelper mSoundTriggerHelper;
        private final TreeMap<UUID, SoundTrigger.SoundModel> mLoadedModels = new TreeMap<>();
        private final Object mCallbacksLock = new Object();
        private final TreeMap<UUID, IRecognitionStatusCallback> mCallbacks = new TreeMap<>();
        private final Identity mOriginatorIdentity = IdentityContext.getNonNull();

        SoundTriggerSessionStub(IBinder iBinder, SoundTriggerHelper soundTriggerHelper, EventLogger eventLogger) {
            this.mSoundTriggerHelper = soundTriggerHelper;
            this.mClient = iBinder;
            this.mEventLogger = eventLogger;
            SoundTriggerService.this.mSessionEventLoggers.add(eventLogger);
            try {
                iBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        SoundTriggerService.SoundTriggerSessionStub.this.lambda$new$0();
                    }
                }, 0);
            } catch (RemoteException unused) {
                lambda$new$0();
            }
            DeviceStateHandler.DeviceStateListener deviceStateListener = new DeviceStateHandler.DeviceStateListener() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$$ExternalSyntheticLambda1
                @Override // com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener
                public final void onSoundTriggerDeviceStateUpdate(DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState) {
                    SoundTriggerService.SoundTriggerSessionStub.this.lambda$new$1(soundTriggerDeviceState);
                }
            };
            this.mListener = deviceStateListener;
            Identity identity = this.mOriginatorIdentity;
            SoundTriggerHelper soundTriggerHelper2 = this.mSoundTriggerHelper;
            Objects.requireNonNull(soundTriggerHelper2);
            MyAppOpsListener myAppOpsListener = new MyAppOpsListener(identity, new SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda2(soundTriggerHelper2));
            this.mAppOpsListener = myAppOpsListener;
            myAppOpsListener.forceOpChangeRefresh();
            SoundTriggerService.this.mAppOpsManager.startWatchingMode("android:record_audio", this.mOriginatorIdentity.packageName, 1, myAppOpsListener);
            SoundTriggerService.this.mDeviceStateHandler.registerListener(deviceStateListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState) {
            this.mSoundTriggerHelper.onDeviceStateChanged(soundTriggerDeviceState);
        }

        public int startRecognition(SoundTrigger.GenericSoundModel genericSoundModel, IRecognitionStatusCallback iRecognitionStatusCallback, SoundTrigger.RecognitionConfig recognitionConfig, boolean z) {
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid((SoundTrigger.SoundModel) genericSoundModel)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                if (genericSoundModel == null) {
                    this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid((SoundTrigger.SoundModel) genericSoundModel), "Invalid sound model").printLog(2, SoundTriggerService.TAG));
                    if (create == null) {
                        return Integer.MIN_VALUE;
                    }
                    create.close();
                    return Integer.MIN_VALUE;
                }
                if (z) {
                    enforceCallingPermission("android.permission.SOUND_TRIGGER_RUN_IN_BATTERY_SAVER");
                }
                int startGenericRecognition = this.mSoundTriggerHelper.startGenericRecognition(genericSoundModel.getUuid(), genericSoundModel, iRecognitionStatusCallback, recognitionConfig, z);
                if (startGenericRecognition == 0) {
                    SoundTriggerService.this.mSoundModelStatTracker.onStart(genericSoundModel.getUuid());
                }
                if (create != null) {
                    create.close();
                }
                return startGenericRecognition;
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int stopRecognition(ParcelUuid parcelUuid, IRecognitionStatusCallback iRecognitionStatusCallback) {
            this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION, getUuid(parcelUuid)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                int stopGenericRecognition = this.mSoundTriggerHelper.stopGenericRecognition(parcelUuid.getUuid(), iRecognitionStatusCallback);
                if (stopGenericRecognition == 0) {
                    SoundTriggerService.this.mSoundModelStatTracker.onStop(parcelUuid.getUuid());
                }
                if (create != null) {
                    create.close();
                }
                return stopGenericRecognition;
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public SoundTrigger.GenericSoundModel getSoundModel(ParcelUuid parcelUuid) {
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                SoundTrigger.GenericSoundModel genericSoundModel = SoundTriggerService.this.mDbHelper.getGenericSoundModel(parcelUuid.getUuid());
                if (create != null) {
                    create.close();
                }
                return genericSoundModel;
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void updateSoundModel(SoundTrigger.GenericSoundModel genericSoundModel) {
            this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.UPDATE_MODEL, getUuid((SoundTrigger.SoundModel) genericSoundModel)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                SoundTriggerService.this.mDbHelper.updateGenericSoundModel(genericSoundModel);
                if (create != null) {
                    create.close();
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void deleteSoundModel(ParcelUuid parcelUuid) {
            this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.DELETE_MODEL, getUuid(parcelUuid)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                this.mSoundTriggerHelper.unloadGenericSoundModel(parcelUuid.getUuid());
                SoundTriggerService.this.mSoundModelStatTracker.onStop(parcelUuid.getUuid());
                SoundTriggerService.this.mDbHelper.deleteGenericSoundModel(parcelUuid.getUuid());
                if (create != null) {
                    create.close();
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int loadGenericSoundModel(SoundTrigger.GenericSoundModel genericSoundModel) {
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.LOAD_MODEL;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid((SoundTrigger.SoundModel) genericSoundModel)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                if (genericSoundModel != null && genericSoundModel.getUuid() != null) {
                    synchronized (SoundTriggerService.this.mLock) {
                        SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(genericSoundModel.getUuid());
                        if (soundModel != null && !soundModel.equals(genericSoundModel)) {
                            this.mSoundTriggerHelper.unloadGenericSoundModel(genericSoundModel.getUuid());
                            synchronized (this.mCallbacksLock) {
                                this.mCallbacks.remove(genericSoundModel.getUuid());
                            }
                        }
                        this.mLoadedModels.put(genericSoundModel.getUuid(), genericSoundModel);
                    }
                    if (create == null) {
                        return 0;
                    }
                    create.close();
                    return 0;
                }
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid((SoundTrigger.SoundModel) genericSoundModel), "Invalid sound model").printLog(2, SoundTriggerService.TAG));
                if (create == null) {
                    return Integer.MIN_VALUE;
                }
                create.close();
                return Integer.MIN_VALUE;
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int loadKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel) {
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.LOAD_MODEL;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid((SoundTrigger.SoundModel) keyphraseSoundModel)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                if (keyphraseSoundModel != null && keyphraseSoundModel.getUuid() != null) {
                    if (keyphraseSoundModel.getKeyphrases() != null && keyphraseSoundModel.getKeyphrases().length == 1) {
                        synchronized (SoundTriggerService.this.mLock) {
                            SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(keyphraseSoundModel.getUuid());
                            if (soundModel != null && !soundModel.equals(keyphraseSoundModel)) {
                                this.mSoundTriggerHelper.unloadKeyphraseSoundModel(keyphraseSoundModel.getKeyphrases()[0].getId());
                                synchronized (this.mCallbacksLock) {
                                    this.mCallbacks.remove(keyphraseSoundModel.getUuid());
                                }
                            }
                            this.mLoadedModels.put(keyphraseSoundModel.getUuid(), keyphraseSoundModel);
                        }
                        if (create != null) {
                            create.close();
                        }
                        return 0;
                    }
                    this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid((SoundTrigger.SoundModel) keyphraseSoundModel), "Only one keyphrase supported").printLog(2, SoundTriggerService.TAG));
                    if (create != null) {
                        create.close();
                    }
                    return Integer.MIN_VALUE;
                }
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid((SoundTrigger.SoundModel) keyphraseSoundModel), "Invalid sound model").printLog(2, SoundTriggerService.TAG));
                if (create != null) {
                    create.close();
                }
                return Integer.MIN_VALUE;
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int startRecognitionForService(ParcelUuid parcelUuid, Bundle bundle, ComponentName componentName, SoundTrigger.RecognitionConfig recognitionConfig) {
            IRecognitionStatusCallback iRecognitionStatusCallback;
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION_SERVICE;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                Objects.requireNonNull(parcelUuid);
                Objects.requireNonNull(componentName);
                Objects.requireNonNull(recognitionConfig);
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                enforceDetectionPermissions(componentName);
                IRecognitionStatusCallback remoteSoundTriggerDetectionService = new RemoteSoundTriggerDetectionService(parcelUuid.getUuid(), bundle, componentName, Binder.getCallingUserHandle(), recognitionConfig);
                synchronized (SoundTriggerService.this.mLock) {
                    SoundTrigger.GenericSoundModel genericSoundModel = (SoundTrigger.SoundModel) this.mLoadedModels.get(parcelUuid.getUuid());
                    if (genericSoundModel == null) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model not loaded").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    synchronized (this.mCallbacksLock) {
                        iRecognitionStatusCallback = this.mCallbacks.get(parcelUuid.getUuid());
                    }
                    if (iRecognitionStatusCallback != null) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model already running").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    if (genericSoundModel.getType() != 1) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Unsupported model type").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    int startGenericRecognition = this.mSoundTriggerHelper.startGenericRecognition(genericSoundModel.getUuid(), genericSoundModel, remoteSoundTriggerDetectionService, recognitionConfig, false);
                    if (startGenericRecognition != 0) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model start fail").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return startGenericRecognition;
                    }
                    synchronized (this.mCallbacksLock) {
                        this.mCallbacks.put(parcelUuid.getUuid(), remoteSoundTriggerDetectionService);
                    }
                    SoundTriggerService.this.mSoundModelStatTracker.onStart(parcelUuid.getUuid());
                    if (create == null) {
                        return 0;
                    }
                    create.close();
                    return 0;
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int stopRecognitionForService(ParcelUuid parcelUuid) {
            IRecognitionStatusCallback iRecognitionStatusCallback;
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION_SERVICE;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (SoundTriggerService.this.mLock) {
                    SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(parcelUuid.getUuid());
                    if (soundModel == null) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model not loaded").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    synchronized (this.mCallbacksLock) {
                        iRecognitionStatusCallback = this.mCallbacks.get(parcelUuid.getUuid());
                    }
                    if (iRecognitionStatusCallback == null) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model not running").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    if (soundModel.getType() != 1) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Unknown model type").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    int stopGenericRecognition = this.mSoundTriggerHelper.stopGenericRecognition(soundModel.getUuid(), iRecognitionStatusCallback);
                    if (stopGenericRecognition != 0) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Failed to stop model").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return stopGenericRecognition;
                    }
                    synchronized (this.mCallbacksLock) {
                        this.mCallbacks.remove(parcelUuid.getUuid());
                    }
                    SoundTriggerService.this.mSoundModelStatTracker.onStop(parcelUuid.getUuid());
                    if (create == null) {
                        return 0;
                    }
                    create.close();
                    return 0;
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int unloadSoundModel(ParcelUuid parcelUuid) {
            int unloadKeyphraseSoundModel;
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.UNLOAD_MODEL;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (SoundTriggerService.this.mLock) {
                    SoundTrigger.KeyphraseSoundModel keyphraseSoundModel = (SoundTrigger.SoundModel) this.mLoadedModels.get(parcelUuid.getUuid());
                    if (keyphraseSoundModel == null) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model not loaded").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    int type2 = keyphraseSoundModel.getType();
                    if (type2 == 0) {
                        unloadKeyphraseSoundModel = this.mSoundTriggerHelper.unloadKeyphraseSoundModel(keyphraseSoundModel.getKeyphrases()[0].getId());
                    } else {
                        if (type2 != 1) {
                            this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Unknown model type").printLog(2, SoundTriggerService.TAG));
                            if (create != null) {
                                create.close();
                            }
                            return Integer.MIN_VALUE;
                        }
                        unloadKeyphraseSoundModel = this.mSoundTriggerHelper.unloadGenericSoundModel(keyphraseSoundModel.getUuid());
                    }
                    if (unloadKeyphraseSoundModel != 0) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Failed to unload model").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return unloadKeyphraseSoundModel;
                    }
                    this.mLoadedModels.remove(parcelUuid.getUuid());
                    if (create != null) {
                        create.close();
                    }
                    return 0;
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public boolean isRecognitionActive(ParcelUuid parcelUuid) {
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (this.mCallbacksLock) {
                    if (this.mCallbacks.get(parcelUuid.getUuid()) == null) {
                        if (create == null) {
                            return false;
                        }
                        create.close();
                        return false;
                    }
                    boolean isRecognitionRequested = this.mSoundTriggerHelper.isRecognitionRequested(parcelUuid.getUuid());
                    if (create != null) {
                        create.close();
                    }
                    return isRecognitionRequested;
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int getModelState(ParcelUuid parcelUuid) {
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.GET_MODEL_STATE;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (SoundTriggerService.this.mLock) {
                    SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(parcelUuid.getUuid());
                    int i = Integer.MIN_VALUE;
                    if (soundModel == null) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model is not loaded").printLog(2, SoundTriggerService.TAG));
                        if (create != null) {
                            create.close();
                        }
                        return Integer.MIN_VALUE;
                    }
                    if (soundModel.getType() == 1) {
                        i = this.mSoundTriggerHelper.getGenericModelState(soundModel.getUuid());
                    } else {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Unsupported model type").printLog(2, SoundTriggerService.TAG));
                    }
                    if (create != null) {
                        create.close();
                    }
                    return i;
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public SoundTrigger.ModuleProperties getModuleProperties() {
            SoundTrigger.ModuleProperties moduleProperties;
            this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.GET_MODULE_PROPERTIES, null));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (SoundTriggerService.this.mLock) {
                    moduleProperties = this.mSoundTriggerHelper.getModuleProperties();
                }
                if (create != null) {
                    create.close();
                }
                return moduleProperties;
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int setParameter(ParcelUuid parcelUuid, @ModelParams int i, int i2) {
            EventLogger eventLogger = this.mEventLogger;
            SoundTriggerEvent.SessionEvent.Type type = SoundTriggerEvent.SessionEvent.Type.SET_PARAMETER;
            eventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid)));
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (SoundTriggerService.this.mLock) {
                    SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(parcelUuid.getUuid());
                    if (soundModel == null) {
                        this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(type, getUuid(parcelUuid), "Model not loaded").printLog(2, SoundTriggerService.TAG));
                        int i3 = SoundTrigger.STATUS_BAD_VALUE;
                        if (create != null) {
                            create.close();
                        }
                        return i3;
                    }
                    int parameter = this.mSoundTriggerHelper.setParameter(soundModel.getUuid(), i, i2);
                    if (create != null) {
                        create.close();
                    }
                    return parameter;
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int getParameter(ParcelUuid parcelUuid, @ModelParams int i) throws UnsupportedOperationException, IllegalArgumentException {
            int parameter;
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (SoundTriggerService.this.mLock) {
                    SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(parcelUuid.getUuid());
                    if (soundModel == null) {
                        throw new IllegalArgumentException("sound model is not loaded");
                    }
                    parameter = this.mSoundTriggerHelper.getParameter(soundModel.getUuid(), i);
                }
                if (create != null) {
                    create.close();
                }
                return parameter;
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public SoundTrigger.ModelParamRange queryParameter(ParcelUuid parcelUuid, @ModelParams int i) {
            SafeCloseable create = ClearCallingIdentityContext.create();
            try {
                enforceCallingPermission("android.permission.MANAGE_SOUND_TRIGGER");
                synchronized (SoundTriggerService.this.mLock) {
                    SoundTrigger.SoundModel soundModel = this.mLoadedModels.get(parcelUuid.getUuid());
                    if (soundModel == null) {
                        if (create == null) {
                            return null;
                        }
                        create.close();
                        return null;
                    }
                    SoundTrigger.ModelParamRange queryParameter = this.mSoundTriggerHelper.queryParameter(soundModel.getUuid(), i);
                    if (create != null) {
                        create.close();
                    }
                    return queryParameter;
                }
            } catch (Throwable th) {
                if (create != null) {
                    try {
                        create.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: clientDied, reason: merged with bridge method [inline-methods] */
        public void lambda$new$0() {
            this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.DETACH, null));
            SoundTriggerService.this.mServiceEventLogger.enqueue(new SoundTriggerEvent.ServiceEvent(SoundTriggerEvent.ServiceEvent.Type.DETACH, this.mOriginatorIdentity.packageName, "Client died").printLog(2, SoundTriggerService.TAG));
            detach();
        }

        private void detach() {
            if (this.mAppOpsListener != null) {
                SoundTriggerService.this.mAppOpsManager.stopWatchingMode(this.mAppOpsListener);
            }
            SoundTriggerService.this.mDeviceStateHandler.unregisterListener(this.mListener);
            this.mSoundTriggerHelper.detach();
            SoundTriggerService.this.detachSessionLogger(this.mEventLogger);
        }

        private void enforceCallingPermission(String str) {
            if (PermissionUtil.checkPermissionForPreflight(SoundTriggerService.this.mContext, this.mOriginatorIdentity, str) == 0) {
                return;
            }
            throw new SecurityException("Identity " + this.mOriginatorIdentity + " does not have permission " + str);
        }

        private void enforceDetectionPermissions(ComponentName componentName) {
            if (SoundTriggerService.this.mPackageManager.checkPermission("android.permission.CAPTURE_AUDIO_HOTWORD", componentName.getPackageName()) == 0) {
                return;
            }
            throw new SecurityException(componentName.getPackageName() + " does not have permission android.permission.CAPTURE_AUDIO_HOTWORD");
        }

        private UUID getUuid(ParcelUuid parcelUuid) {
            if (parcelUuid != null) {
                return parcelUuid.getUuid();
            }
            return null;
        }

        private UUID getUuid(SoundTrigger.SoundModel soundModel) {
            if (soundModel != null) {
                return soundModel.getUuid();
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class RemoteSoundTriggerDetectionService extends IRecognitionStatusCallback.Stub implements ServiceConnection {
            private static final int MSG_STOP_ALL_PENDING_OPERATIONS = 1;
            private final ISoundTriggerDetectionServiceClient mClient;

            @GuardedBy({"mRemoteServiceLock"})
            private boolean mDestroyOnceRunningOpsDone;

            @GuardedBy({"mRemoteServiceLock"})
            private boolean mIsBound;

            @GuardedBy({"mRemoteServiceLock"})
            private boolean mIsDestroyed;
            private final NumOps mNumOps;

            @GuardedBy({"mRemoteServiceLock"})
            private int mNumTotalOpsPerformed;
            private final Bundle mParams;
            private final ParcelUuid mPuuid;
            private final SoundTrigger.RecognitionConfig mRecognitionConfig;
            private final PowerManager.WakeLock mRemoteServiceWakeLock;

            @GuardedBy({"mRemoteServiceLock"})
            private ISoundTriggerDetectionService mService;
            private final ComponentName mServiceName;
            private final UserHandle mUser;
            private final Object mRemoteServiceLock = new Object();

            @GuardedBy({"mRemoteServiceLock"})
            private final ArrayList<Operation> mPendingOps = new ArrayList<>();

            @GuardedBy({"mRemoteServiceLock"})
            private final ArraySet<Integer> mRunningOpIds = new ArraySet<>();
            private final Handler mHandler = new Handler(Looper.getMainLooper());

            public void onKeyphraseDetected(SoundTrigger.KeyphraseRecognitionEvent keyphraseRecognitionEvent) {
            }

            public void onRecognitionPaused() {
            }

            public void onRecognitionResumed() {
            }

            public RemoteSoundTriggerDetectionService(UUID uuid, Bundle bundle, ComponentName componentName, UserHandle userHandle, SoundTrigger.RecognitionConfig recognitionConfig) {
                this.mPuuid = new ParcelUuid(uuid);
                this.mParams = bundle;
                this.mServiceName = componentName;
                this.mUser = userHandle;
                this.mRecognitionConfig = recognitionConfig;
                this.mRemoteServiceWakeLock = ((PowerManager) SoundTriggerService.this.mContext.getSystemService("power")).newWakeLock(1, "RemoteSoundTriggerDetectionService " + componentName.getPackageName() + ":" + componentName.getClassName());
                synchronized (SoundTriggerService.this.mLock) {
                    NumOps numOps = (NumOps) SoundTriggerService.this.mNumOpsPerPackage.get(componentName.getPackageName());
                    if (numOps == null) {
                        numOps = new NumOps();
                        SoundTriggerService.this.mNumOpsPerPackage.put(componentName.getPackageName(), numOps);
                    }
                    this.mNumOps = numOps;
                }
                this.mClient = new ISoundTriggerDetectionServiceClient.Stub() { // from class: com.android.server.soundtrigger.SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.1
                    public void onOpFinished(int i) {
                        long clearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            synchronized (RemoteSoundTriggerDetectionService.this.mRemoteServiceLock) {
                                RemoteSoundTriggerDetectionService.this.mRunningOpIds.remove(Integer.valueOf(i));
                                if (RemoteSoundTriggerDetectionService.this.mRunningOpIds.isEmpty() && RemoteSoundTriggerDetectionService.this.mPendingOps.isEmpty()) {
                                    if (RemoteSoundTriggerDetectionService.this.mDestroyOnceRunningOpsDone) {
                                        RemoteSoundTriggerDetectionService.this.destroy();
                                    } else {
                                        RemoteSoundTriggerDetectionService.this.disconnectLocked();
                                    }
                                }
                            }
                        } finally {
                            Binder.restoreCallingIdentity(clearCallingIdentity);
                        }
                    }
                };
            }

            public boolean pingBinder() {
                return (this.mIsDestroyed || this.mDestroyOnceRunningOpsDone) ? false : true;
            }

            /* JADX INFO: Access modifiers changed from: private */
            @GuardedBy({"mRemoteServiceLock"})
            public void disconnectLocked() {
                ISoundTriggerDetectionService iSoundTriggerDetectionService = this.mService;
                if (iSoundTriggerDetectionService != null) {
                    try {
                        iSoundTriggerDetectionService.removeClient(this.mPuuid);
                    } catch (Exception e) {
                        Slog.e(SoundTriggerService.TAG, this.mPuuid + ": Cannot remove client", e);
                        SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": Cannot remove client"));
                    }
                    this.mService = null;
                }
                if (this.mIsBound) {
                    SoundTriggerService.this.mContext.unbindService(this);
                    this.mIsBound = false;
                    synchronized (SoundTriggerSessionStub.this.mCallbacksLock) {
                        this.mRemoteServiceWakeLock.release();
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void destroy() {
                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": destroy"));
                synchronized (this.mRemoteServiceLock) {
                    disconnectLocked();
                    this.mIsDestroyed = true;
                }
                if (this.mDestroyOnceRunningOpsDone) {
                    return;
                }
                synchronized (SoundTriggerSessionStub.this.mCallbacksLock) {
                    SoundTriggerSessionStub.this.mCallbacks.remove(this.mPuuid.getUuid());
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void stopAllPendingOperations() {
                synchronized (this.mRemoteServiceLock) {
                    if (this.mIsDestroyed) {
                        return;
                    }
                    if (this.mService != null) {
                        int size = this.mRunningOpIds.size();
                        for (int i = 0; i < size; i++) {
                            try {
                                this.mService.onStopOperation(this.mPuuid, this.mRunningOpIds.valueAt(i).intValue());
                            } catch (Exception e) {
                                Slog.e(SoundTriggerService.TAG, this.mPuuid + ": Could not stop operation " + this.mRunningOpIds.valueAt(i), e);
                                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": Could not stop operation " + this.mRunningOpIds.valueAt(i)));
                            }
                        }
                        this.mRunningOpIds.clear();
                    }
                    disconnectLocked();
                }
            }

            private void bind() {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    Intent intent = new Intent();
                    intent.setComponent(this.mServiceName);
                    ResolveInfo resolveServiceAsUser = SoundTriggerService.this.mContext.getPackageManager().resolveServiceAsUser(intent, 268435588, this.mUser.getIdentifier());
                    if (resolveServiceAsUser == null) {
                        Slog.w(SoundTriggerService.TAG, this.mPuuid + ": " + this.mServiceName + " not found");
                        SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": " + this.mServiceName + " not found"));
                        return;
                    }
                    if (!"android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE".equals(resolveServiceAsUser.serviceInfo.permission)) {
                        Slog.w(SoundTriggerService.TAG, this.mPuuid + ": " + this.mServiceName + " does not require android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE");
                        SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": " + this.mServiceName + " does not require android.permission.BIND_SOUND_TRIGGER_DETECTION_SERVICE"));
                        return;
                    }
                    boolean bindServiceAsUser = SoundTriggerService.this.mContext.bindServiceAsUser(intent, this, 67112961, this.mUser);
                    this.mIsBound = bindServiceAsUser;
                    if (bindServiceAsUser) {
                        this.mRemoteServiceWakeLock.acquire();
                    } else {
                        Slog.w(SoundTriggerService.TAG, this.mPuuid + ": Could not bind to " + this.mServiceName);
                        SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": Could not bind to " + this.mServiceName));
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            /* JADX WARN: Code restructure failed: missing block: B:32:0x00a9, code lost:
            
                r8 = move-exception;
             */
            /* JADX WARN: Code restructure failed: missing block: B:33:0x00aa, code lost:
            
                android.util.Slog.e(com.android.server.soundtrigger.SoundTriggerService.TAG, r7.mPuuid + ": Could not run operation " + r1, r8);
                r7.this$1.mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(r7.mPuuid + ": Could not run operation " + r1));
             */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            private void runOrAddOperation(Operation operation) {
                synchronized (this.mRemoteServiceLock) {
                    if (!this.mIsDestroyed && !this.mDestroyOnceRunningOpsDone) {
                        if (this.mService == null) {
                            this.mPendingOps.add(operation);
                            if (!this.mIsBound) {
                                bind();
                            }
                        } else {
                            long nanoTime = System.nanoTime();
                            this.mNumOps.clearOldOps(nanoTime);
                            Settings.Global.getInt(SoundTriggerService.this.mContext.getContentResolver(), "max_sound_trigger_detection_service_ops_per_day", Integer.MAX_VALUE);
                            this.mNumOps.getOpsAdded();
                            this.mNumOps.addOp(nanoTime);
                            int i = this.mNumTotalOpsPerformed;
                            do {
                                this.mNumTotalOpsPerformed++;
                            } while (this.mRunningOpIds.contains(Integer.valueOf(i)));
                            Slog.v(SoundTriggerService.TAG, this.mPuuid + ": runOp " + i);
                            SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": runOp " + i));
                            operation.run(i, this.mService);
                            this.mRunningOpIds.add(Integer.valueOf(i));
                            if (this.mPendingOps.isEmpty() && this.mRunningOpIds.isEmpty()) {
                                if (this.mDestroyOnceRunningOpsDone) {
                                    destroy();
                                } else {
                                    disconnectLocked();
                                }
                            } else {
                                this.mHandler.removeMessages(1);
                                this.mHandler.sendMessageDelayed(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda0
                                    @Override // java.util.function.Consumer
                                    public final void accept(Object obj) {
                                        ((SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService) obj).stopAllPendingOperations();
                                    }
                                }, this).setWhat(1), Settings.Global.getLong(SoundTriggerService.this.mContext.getContentResolver(), "sound_trigger_detection_service_op_timeout", JobStatus.NO_LATEST_RUNTIME));
                            }
                        }
                        return;
                    }
                    Slog.w(SoundTriggerService.TAG, this.mPuuid + ": Dropped operation as already destroyed or marked for destruction");
                    SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ":Dropped operation as already destroyed or marked for destruction"));
                    operation.drop();
                }
            }

            private AudioRecord createAudioRecordForEvent(SoundTrigger.GenericRecognitionEvent genericRecognitionEvent) throws IllegalArgumentException, UnsupportedOperationException {
                AudioAttributes.Builder builder = new AudioAttributes.Builder();
                builder.setInternalCapturePreset(1999);
                AudioAttributes build = builder.build();
                AudioFormat captureFormat = genericRecognitionEvent.getCaptureFormat();
                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent("createAudioRecordForEvent"));
                return new AudioRecord.Builder().setAudioAttributes(build).setAudioFormat(new AudioFormat.Builder().setChannelMask(captureFormat.getChannelMask()).setEncoding(captureFormat.getEncoding()).setSampleRate(captureFormat.getSampleRate()).build()).setSessionId(genericRecognitionEvent.getCaptureSession()).build();
            }

            public void onGenericSoundTriggerDetected(final SoundTrigger.GenericRecognitionEvent genericRecognitionEvent) {
                runOrAddOperation(new Operation(new Runnable() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.lambda$onGenericSoundTriggerDetected$0();
                    }
                }, new Operation.ExecuteOp() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda2
                    @Override // com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp
                    public final void run(int i, ISoundTriggerDetectionService iSoundTriggerDetectionService) {
                        SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.lambda$onGenericSoundTriggerDetected$1(genericRecognitionEvent, i, iSoundTriggerDetectionService);
                    }
                }, new Runnable() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.lambda$onGenericSoundTriggerDetected$2(genericRecognitionEvent);
                    }
                }));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGenericSoundTriggerDetected$0() {
                if (this.mRecognitionConfig.allowMultipleTriggers) {
                    return;
                }
                synchronized (SoundTriggerSessionStub.this.mCallbacksLock) {
                    SoundTriggerSessionStub.this.mCallbacks.remove(this.mPuuid.getUuid());
                }
                this.mDestroyOnceRunningOpsDone = true;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGenericSoundTriggerDetected$1(SoundTrigger.GenericRecognitionEvent genericRecognitionEvent, int i, ISoundTriggerDetectionService iSoundTriggerDetectionService) throws RemoteException {
                iSoundTriggerDetectionService.onGenericRecognitionEvent(this.mPuuid, i, genericRecognitionEvent);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGenericSoundTriggerDetected$2(SoundTrigger.GenericRecognitionEvent genericRecognitionEvent) {
                if (genericRecognitionEvent.isCaptureAvailable()) {
                    try {
                        AudioRecord createAudioRecordForEvent = createAudioRecordForEvent(genericRecognitionEvent);
                        createAudioRecordForEvent.startRecording();
                        createAudioRecordForEvent.release();
                    } catch (IllegalArgumentException | UnsupportedOperationException unused) {
                        Slog.w(SoundTriggerService.TAG, this.mPuuid + ": createAudioRecordForEvent(" + genericRecognitionEvent + "), failed to create AudioRecord");
                    }
                }
            }

            private void onError(final int i) {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onError: " + i);
                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": onError: " + i));
                runOrAddOperation(new Operation(new Runnable() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.lambda$onError$3();
                    }
                }, new Operation.ExecuteOp() { // from class: com.android.server.soundtrigger.SoundTriggerService$SoundTriggerSessionStub$RemoteSoundTriggerDetectionService$$ExternalSyntheticLambda5
                    @Override // com.android.server.soundtrigger.SoundTriggerService.Operation.ExecuteOp
                    public final void run(int i2, ISoundTriggerDetectionService iSoundTriggerDetectionService) {
                        SoundTriggerService.SoundTriggerSessionStub.RemoteSoundTriggerDetectionService.this.lambda$onError$4(i, i2, iSoundTriggerDetectionService);
                    }
                }, null));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onError$3() {
                synchronized (SoundTriggerSessionStub.this.mCallbacksLock) {
                    SoundTriggerSessionStub.this.mCallbacks.remove(this.mPuuid.getUuid());
                }
                this.mDestroyOnceRunningOpsDone = true;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onError$4(int i, int i2, ISoundTriggerDetectionService iSoundTriggerDetectionService) throws RemoteException {
                iSoundTriggerDetectionService.onError(this.mPuuid, i2, i);
            }

            public void onPreempted() {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onPreempted");
                onError(Integer.MIN_VALUE);
            }

            public void onModuleDied() {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onModuleDied");
                onError(SoundTrigger.STATUS_DEAD_OBJECT);
            }

            public void onResumeFailed(int i) {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onResumeFailed: " + i);
                onError(i);
            }

            public void onPauseFailed(int i) {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onPauseFailed: " + i);
                onError(i);
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onServiceConnected(" + iBinder + ")");
                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": onServiceConnected(" + iBinder + ")"));
                synchronized (this.mRemoteServiceLock) {
                    ISoundTriggerDetectionService asInterface = ISoundTriggerDetectionService.Stub.asInterface(iBinder);
                    this.mService = asInterface;
                    try {
                        asInterface.setClient(this.mPuuid, this.mParams, this.mClient);
                        while (!this.mPendingOps.isEmpty()) {
                            runOrAddOperation(this.mPendingOps.remove(0));
                        }
                    } catch (Exception e) {
                        Slog.e(SoundTriggerService.TAG, this.mPuuid + ": Could not init " + this.mServiceName, e);
                    }
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onServiceDisconnected");
                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": onServiceDisconnected"));
                synchronized (this.mRemoteServiceLock) {
                    this.mService = null;
                }
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(ComponentName componentName) {
                Slog.v(SoundTriggerService.TAG, this.mPuuid + ": onBindingDied");
                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(this.mPuuid + ": onBindingDied"));
                synchronized (this.mRemoteServiceLock) {
                    destroy();
                }
            }

            @Override // android.content.ServiceConnection
            public void onNullBinding(ComponentName componentName) {
                Slog.w(SoundTriggerService.TAG, componentName + " for model " + this.mPuuid + " returned a null binding");
                SoundTriggerSessionStub.this.mEventLogger.enqueue(new EventLogger.StringEvent(componentName + " for model " + this.mPuuid + " returned a null binding"));
                synchronized (this.mRemoteServiceLock) {
                    disconnectLocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NumOps {

        @GuardedBy({"mLock"})
        private long mLastOpsHourSinceBoot;
        private final Object mLock;

        @GuardedBy({"mLock"})
        private int[] mNumOps;

        private NumOps() {
            this.mLock = new Object();
            this.mNumOps = new int[24];
        }

        void clearOldOps(long j) {
            synchronized (this.mLock) {
                long convert = TimeUnit.HOURS.convert(j, TimeUnit.NANOSECONDS);
                long j2 = this.mLastOpsHourSinceBoot;
                if (j2 != 0) {
                    while (true) {
                        j2++;
                        if (j2 > convert) {
                            break;
                        } else {
                            this.mNumOps[(int) (j2 % 24)] = 0;
                        }
                    }
                }
            }
        }

        void addOp(long j) {
            synchronized (this.mLock) {
                long convert = TimeUnit.HOURS.convert(j, TimeUnit.NANOSECONDS);
                int[] iArr = this.mNumOps;
                int i = (int) (convert % 24);
                iArr[i] = iArr[i] + 1;
                this.mLastOpsHourSinceBoot = convert;
            }
        }

        int getOpsAdded() {
            int i;
            synchronized (this.mLock) {
                i = 0;
                for (int i2 = 0; i2 < 24; i2++) {
                    i += this.mNumOps[i2];
                }
            }
            return i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Operation {
        private final Runnable mDropOp;
        private final ExecuteOp mExecuteOp;
        private final Runnable mSetupOp;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public interface ExecuteOp {
            void run(int i, ISoundTriggerDetectionService iSoundTriggerDetectionService) throws RemoteException;
        }

        private Operation(Runnable runnable, ExecuteOp executeOp, Runnable runnable2) {
            this.mSetupOp = runnable;
            this.mExecuteOp = executeOp;
            this.mDropOp = runnable2;
        }

        private void setup() {
            Runnable runnable = this.mSetupOp;
            if (runnable != null) {
                runnable.run();
            }
        }

        void run(int i, ISoundTriggerDetectionService iSoundTriggerDetectionService) throws RemoteException {
            setup();
            this.mExecuteOp.run(i, iSoundTriggerDetectionService);
        }

        void drop() {
            setup();
            Runnable runnable = this.mDropOp;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class LocalSoundTriggerService implements SoundTriggerInternal {
        private final Context mContext;

        LocalSoundTriggerService(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public class SessionImpl implements SoundTriggerInternal.Session {
            private final MyAppOpsListener mAppOpsListener;
            private final IBinder mClient;
            private final EventLogger mEventLogger;
            private final DeviceStateHandler.DeviceStateListener mListener;
            private final SparseArray<UUID> mModelUuid;
            private final Identity mOriginatorIdentity;
            private final SoundTriggerHelper mSoundTriggerHelper;

            private SessionImpl(SoundTriggerHelper soundTriggerHelper, IBinder iBinder, EventLogger eventLogger, Identity identity) {
                this.mModelUuid = new SparseArray<>(1);
                this.mSoundTriggerHelper = soundTriggerHelper;
                this.mClient = iBinder;
                this.mOriginatorIdentity = identity;
                this.mEventLogger = eventLogger;
                SoundTriggerService.this.mSessionEventLoggers.add(eventLogger);
                try {
                    iBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.server.soundtrigger.SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda0
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            SoundTriggerService.LocalSoundTriggerService.SessionImpl.this.lambda$new$0();
                        }
                    }, 0);
                } catch (RemoteException unused) {
                    lambda$new$0();
                }
                DeviceStateHandler.DeviceStateListener deviceStateListener = new DeviceStateHandler.DeviceStateListener() { // from class: com.android.server.soundtrigger.SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda1
                    @Override // com.android.server.soundtrigger.DeviceStateHandler.DeviceStateListener
                    public final void onSoundTriggerDeviceStateUpdate(DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState) {
                        SoundTriggerService.LocalSoundTriggerService.SessionImpl.this.lambda$new$1(soundTriggerDeviceState);
                    }
                };
                this.mListener = deviceStateListener;
                SoundTriggerService soundTriggerService = SoundTriggerService.this;
                Identity identity2 = this.mOriginatorIdentity;
                SoundTriggerHelper soundTriggerHelper2 = this.mSoundTriggerHelper;
                Objects.requireNonNull(soundTriggerHelper2);
                MyAppOpsListener myAppOpsListener = new MyAppOpsListener(identity2, new SoundTriggerService$LocalSoundTriggerService$SessionImpl$$ExternalSyntheticLambda2(soundTriggerHelper2));
                this.mAppOpsListener = myAppOpsListener;
                myAppOpsListener.forceOpChangeRefresh();
                SoundTriggerService.this.mAppOpsManager.startWatchingMode("android:record_audio", this.mOriginatorIdentity.packageName, 1, myAppOpsListener);
                SoundTriggerService.this.mDeviceStateHandler.registerListener(deviceStateListener);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$new$1(DeviceStateHandler.SoundTriggerDeviceState soundTriggerDeviceState) {
                this.mSoundTriggerHelper.onDeviceStateChanged(soundTriggerDeviceState);
            }

            public int startRecognition(int i, SoundTrigger.KeyphraseSoundModel keyphraseSoundModel, IRecognitionStatusCallback iRecognitionStatusCallback, SoundTrigger.RecognitionConfig recognitionConfig, boolean z) {
                this.mModelUuid.put(i, keyphraseSoundModel.getUuid());
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.START_RECOGNITION, keyphraseSoundModel.getUuid()));
                return this.mSoundTriggerHelper.startKeyphraseRecognition(i, keyphraseSoundModel, iRecognitionStatusCallback, recognitionConfig, z);
            }

            public synchronized int stopRecognition(int i, IRecognitionStatusCallback iRecognitionStatusCallback) {
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.STOP_RECOGNITION, this.mModelUuid.get(i)));
                return this.mSoundTriggerHelper.stopKeyphraseRecognition(i, iRecognitionStatusCallback);
            }

            public SoundTrigger.ModuleProperties getModuleProperties() {
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.GET_MODULE_PROPERTIES, null));
                return this.mSoundTriggerHelper.getModuleProperties();
            }

            public int setParameter(int i, @ModelParams int i2, int i3) {
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.SET_PARAMETER, this.mModelUuid.get(i)));
                return this.mSoundTriggerHelper.setKeyphraseParameter(i, i2, i3);
            }

            public int getParameter(int i, @ModelParams int i2) {
                return this.mSoundTriggerHelper.getKeyphraseParameter(i, i2);
            }

            public SoundTrigger.ModelParamRange queryParameter(int i, @ModelParams int i2) {
                return this.mSoundTriggerHelper.queryKeyphraseParameter(i, i2);
            }

            public void detach() {
                detachInternal();
            }

            public int unloadKeyphraseModel(int i) {
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.UNLOAD_MODEL, this.mModelUuid.get(i)));
                return this.mSoundTriggerHelper.unloadKeyphraseSoundModel(i);
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* renamed from: clientDied, reason: merged with bridge method [inline-methods] */
            public void lambda$new$0() {
                SoundTriggerService.this.mServiceEventLogger.enqueue(new SoundTriggerEvent.ServiceEvent(SoundTriggerEvent.ServiceEvent.Type.DETACH, this.mOriginatorIdentity.packageName, "Client died").printLog(2, SoundTriggerService.TAG));
                detachInternal();
            }

            private void detachInternal() {
                if (this.mAppOpsListener != null) {
                    SoundTriggerService.this.mAppOpsManager.stopWatchingMode(this.mAppOpsListener);
                }
                this.mEventLogger.enqueue(new SoundTriggerEvent.SessionEvent(SoundTriggerEvent.SessionEvent.Type.DETACH, null));
                SoundTriggerService.this.detachSessionLogger(this.mEventLogger);
                SoundTriggerService.this.mDeviceStateHandler.unregisterListener(this.mListener);
                this.mSoundTriggerHelper.detach();
            }
        }

        public SoundTriggerInternal.Session attach(IBinder iBinder, SoundTrigger.ModuleProperties moduleProperties, boolean z) {
            Identity nonNull = IdentityContext.getNonNull();
            int andIncrement = SoundTriggerService.this.mSessionIdCounter.getAndIncrement();
            SoundTriggerService.this.mServiceEventLogger.enqueue(new SoundTriggerEvent.ServiceEvent(SoundTriggerEvent.ServiceEvent.Type.ATTACH, nonNull.packageName + "#" + andIncrement));
            EventLogger eventLogger = new EventLogger(128, "LocalSoundTriggerEventLogger for package: " + nonNull.packageName + "#" + andIncrement);
            return new SessionImpl(SoundTriggerService.this.newSoundTriggerHelper(moduleProperties, eventLogger, z), iBinder, eventLogger, nonNull);
        }

        public List<SoundTrigger.ModuleProperties> listModuleProperties(Identity identity) {
            SoundTriggerService.this.mServiceEventLogger.enqueue(new SoundTriggerEvent.ServiceEvent(SoundTriggerEvent.ServiceEvent.Type.LIST_MODULE, identity.packageName));
            SafeCloseable establishIdentityDirect = PermissionUtil.establishIdentityDirect(identity);
            try {
                List<SoundTrigger.ModuleProperties> lambda$newSoundTriggerHelper$2 = SoundTriggerService.this.lambda$newSoundTriggerHelper$2(identity);
                if (establishIdentityDirect != null) {
                    establishIdentityDirect.close();
                }
                return lambda$newSoundTriggerHelper$2;
            } catch (Throwable th) {
                if (establishIdentityDirect != null) {
                    try {
                        establishIdentityDirect.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }
}
