package com.android.server.sensors;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ConcurrentUtils;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.SystemServerInitThreadPool;
import com.android.server.SystemService;
import com.android.server.sensors.SensorManagerInternal;
import com.android.server.sensors.SensorService;
import com.android.server.utils.TimingsTraceAndSlog;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SensorService extends SystemService {
    private static final String START_NATIVE_SENSOR_SERVICE = "StartNativeSensorService";
    private final Object mLock;

    @GuardedBy({"mLock"})
    private final ArrayMap<SensorManagerInternal.ProximityActiveListener, ProximityListenerProxy> mProximityListeners;

    @GuardedBy({"mLock"})
    private long mPtr;

    @GuardedBy({"mLock"})
    private final Set<Integer> mRuntimeSensorHandles;
    private ISensorServiceExt mSensorServiceExtImpl;

    @GuardedBy({"mLock"})
    private Future<?> mSensorServiceStart;
    private ISensorServiceWrapper mSensorServiceWrapper;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeCleanUpProxEvents(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long[] nativeGetProximityEvents(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] nativeGetProximityOwner(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String[] nativeGetUltrasonicProximityUsage(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeNotifyProxWakeLockAcquired(long j, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeNotifyProxWakeLockReleased(long j, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void registerProximityActiveListenerNative(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int registerRuntimeSensorNative(long j, int i, int i2, String str, String str2, float f, float f2, float f3, int i3, int i4, int i5, SensorManagerInternal.RuntimeSensorCallback runtimeSensorCallback);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean sendRuntimeSensorEventNative(long j, int i, int i2, long j2, float[] fArr);

    private static native long startSensorServiceNative(SensorManagerInternal.ProximityActiveListener proximityActiveListener);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void unregisterProximityActiveListenerNative(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void unregisterRuntimeSensorNative(long j, int i);

    public SensorService(Context context) {
        super(context);
        Object obj = new Object();
        this.mLock = obj;
        this.mProximityListeners = new ArrayMap<>();
        this.mRuntimeSensorHandles = new HashSet();
        this.mSensorServiceExtImpl = (ISensorServiceExt) ExtLoader.type(ISensorServiceExt.class).base(this).create();
        this.mSensorServiceWrapper = new SensorServiceWrapper();
        synchronized (obj) {
            this.mSensorServiceStart = SystemServerInitThreadPool.submit(new Runnable() { // from class: com.android.server.sensors.SensorService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SensorService.this.lambda$new$0();
                }
            }, START_NATIVE_SENSOR_SERVICE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        TimingsTraceAndSlog newAsyncLog = TimingsTraceAndSlog.newAsyncLog();
        newAsyncLog.traceBegin(START_NATIVE_SENSOR_SERVICE);
        long startSensorServiceNative = startSensorServiceNative(new ProximityListenerDelegate());
        synchronized (this.mLock) {
            this.mPtr = startSensorServiceNative;
        }
        newAsyncLog.traceEnd();
    }

    public void onStart() {
        LocalServices.addService(SensorManagerInternal.class, new LocalService());
    }

    public void onBootPhase(int i) {
        if (i == 200) {
            ConcurrentUtils.waitForFutureNoInterrupt(this.mSensorServiceStart, START_NATIVE_SENSOR_SERVICE);
            synchronized (this.mLock) {
                this.mSensorServiceStart = null;
            }
        }
        getWrapper().getExtImpl().onBootPhase(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class LocalService extends SensorManagerInternal {
        LocalService() {
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public int createRuntimeSensor(int i, int i2, String str, String str2, float f, float f2, float f3, int i3, int i4, int i5, SensorManagerInternal.RuntimeSensorCallback runtimeSensorCallback) {
            int registerRuntimeSensorNative;
            synchronized (SensorService.this.mLock) {
                registerRuntimeSensorNative = SensorService.registerRuntimeSensorNative(SensorService.this.mPtr, i, i2, str, str2, f, f2, f3, i3, i4, i5, runtimeSensorCallback);
                SensorService.this.mRuntimeSensorHandles.add(Integer.valueOf(registerRuntimeSensorNative));
            }
            return registerRuntimeSensorNative;
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void removeRuntimeSensor(int i) {
            synchronized (SensorService.this.mLock) {
                if (SensorService.this.mRuntimeSensorHandles.contains(Integer.valueOf(i))) {
                    SensorService.this.mRuntimeSensorHandles.remove(Integer.valueOf(i));
                    SensorService.unregisterRuntimeSensorNative(SensorService.this.mPtr, i);
                }
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public boolean sendSensorEvent(int i, int i2, long j, float[] fArr) {
            synchronized (SensorService.this.mLock) {
                if (!SensorService.this.mRuntimeSensorHandles.contains(Integer.valueOf(i))) {
                    return false;
                }
                return SensorService.sendRuntimeSensorEventNative(SensorService.this.mPtr, i, i2, j, fArr);
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void addProximityActiveListener(Executor executor, SensorManagerInternal.ProximityActiveListener proximityActiveListener) {
            Objects.requireNonNull(executor, "executor must not be null");
            Objects.requireNonNull(proximityActiveListener, "listener must not be null");
            ProximityListenerProxy proximityListenerProxy = new ProximityListenerProxy(executor, proximityActiveListener);
            synchronized (SensorService.this.mLock) {
                if (SensorService.this.mProximityListeners.containsKey(proximityActiveListener)) {
                    throw new IllegalArgumentException("listener already registered");
                }
                SensorService.this.mProximityListeners.put(proximityActiveListener, proximityListenerProxy);
                if (SensorService.this.mProximityListeners.size() == 1) {
                    SensorService.registerProximityActiveListenerNative(SensorService.this.mPtr);
                }
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void removeProximityActiveListener(SensorManagerInternal.ProximityActiveListener proximityActiveListener) {
            Objects.requireNonNull(proximityActiveListener, "listener must not be null");
            synchronized (SensorService.this.mLock) {
                if (((ProximityListenerProxy) SensorService.this.mProximityListeners.remove(proximityActiveListener)) == null) {
                    throw new IllegalArgumentException("listener was not registered with sensor service");
                }
                if (SensorService.this.mProximityListeners.isEmpty()) {
                    SensorService.unregisterProximityActiveListenerNative(SensorService.this.mPtr);
                }
            }
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void notifyProxWakeLockAcquired(final String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.sensors.SensorService$LocalService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SensorService.LocalService.this.lambda$notifyProxWakeLockAcquired$0(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyProxWakeLockAcquired$0(String str) {
            SensorService.nativeNotifyProxWakeLockAcquired(SensorService.this.mPtr, str);
        }

        @Override // com.android.server.sensors.SensorManagerInternal
        public void notifyProxWakeLockReleased(final String str) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            FgThread.getExecutor().execute(new Runnable() { // from class: com.android.server.sensors.SensorService$LocalService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SensorService.LocalService.this.lambda$notifyProxWakeLockReleased$1(str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyProxWakeLockReleased$1(String str) {
            SensorService.nativeNotifyProxWakeLockReleased(SensorService.this.mPtr, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ProximityListenerProxy implements SensorManagerInternal.ProximityActiveListener {
        private final Executor mExecutor;
        private final SensorManagerInternal.ProximityActiveListener mListener;

        ProximityListenerProxy(Executor executor, SensorManagerInternal.ProximityActiveListener proximityActiveListener) {
            this.mExecutor = executor;
            this.mListener = proximityActiveListener;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProximityActive$0(boolean z) {
            this.mListener.onProximityActive(z);
        }

        @Override // com.android.server.sensors.SensorManagerInternal.ProximityActiveListener
        public void onProximityActive(final boolean z) {
            this.mExecutor.execute(new Runnable() { // from class: com.android.server.sensors.SensorService$ProximityListenerProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SensorService.ProximityListenerProxy.this.lambda$onProximityActive$0(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class ProximityListenerDelegate implements SensorManagerInternal.ProximityActiveListener {
        private ProximityListenerDelegate() {
        }

        @Override // com.android.server.sensors.SensorManagerInternal.ProximityActiveListener
        public void onProximityActive(boolean z) {
            int i;
            ProximityListenerProxy[] proximityListenerProxyArr;
            synchronized (SensorService.this.mLock) {
                proximityListenerProxyArr = (ProximityListenerProxy[]) SensorService.this.mProximityListeners.values().toArray(new ProximityListenerProxy[0]);
            }
            for (ProximityListenerProxy proximityListenerProxy : proximityListenerProxyArr) {
                proximityListenerProxy.onProximityActive(z);
            }
        }
    }

    public ISensorServiceWrapper getWrapper() {
        return this.mSensorServiceWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class SensorServiceWrapper implements ISensorServiceWrapper {
        private SensorServiceWrapper() {
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public ISensorServiceExt getExtImpl() {
            return SensorService.this.mSensorServiceExtImpl;
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public String[] getProximityOwnerInternal() {
            return SensorService.nativeGetProximityOwner(SensorService.this.mPtr);
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public long[] getProximityEventsInternal() {
            return SensorService.nativeGetProximityEvents(SensorService.this.mPtr);
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public void cleanUpProxEventsInternal() {
            SensorService.nativeCleanUpProxEvents(SensorService.this.mPtr);
        }

        @Override // com.android.server.sensors.ISensorServiceWrapper
        public String[] getUltrasonicProximityUsage() {
            return SensorService.nativeGetUltrasonicProximityUsage(SensorService.this.mPtr);
        }
    }
}
