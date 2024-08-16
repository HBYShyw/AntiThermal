package com.android.server.vibrator;

import android.os.Binder;
import android.os.IVibratorStateListener;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.VibratorInfo;
import android.os.vibrator.PrebakedSegment;
import android.os.vibrator.PrimitiveSegment;
import android.os.vibrator.RampSegment;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.function.Consumer;
import libcore.util.NativeAllocationRegistry;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VibratorController {
    private static final String TAG = "VibratorController";
    private volatile float mCurrentAmplitude;
    private IVibratorControllerExt mIVibratorControllerExt;
    private volatile boolean mIsUnderExternalControl;
    private volatile boolean mIsVibrating;
    private final Object mLock;

    @GuardedBy({"mLock"})
    private final NativeWrapper mNativeWrapper;
    private OnVibrationCompleteListener mOnVibrationCompleteListener;
    private IVibratorControllerWrapper mVibratorControllerWrapper;
    private volatile VibratorInfo mVibratorInfo;
    private volatile boolean mVibratorInfoLoadSuccessful;
    private final RemoteCallbackList<IVibratorStateListener> mVibratorStateListeners;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface OnVibrationCompleteListener {
        void onComplete(int i, long j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VibratorController(int i, OnVibrationCompleteListener onVibrationCompleteListener) {
        this(i, onVibrationCompleteListener, new NativeWrapper());
    }

    @VisibleForTesting
    VibratorController(int i, OnVibrationCompleteListener onVibrationCompleteListener, NativeWrapper nativeWrapper) {
        this.mLock = new Object();
        this.mVibratorStateListeners = new RemoteCallbackList<>();
        this.mVibratorControllerWrapper = new VibratorControllerWrapper();
        this.mIVibratorControllerExt = (IVibratorControllerExt) ExtLoader.type(IVibratorControllerExt.class).base(this).create();
        this.mNativeWrapper = nativeWrapper;
        nativeWrapper.init(i, onVibrationCompleteListener);
        VibratorInfo.Builder builder = new VibratorInfo.Builder(i);
        this.mVibratorInfoLoadSuccessful = nativeWrapper.getInfo(builder);
        this.mVibratorInfo = builder.build();
        this.mOnVibrationCompleteListener = onVibrationCompleteListener;
        if (this.mVibratorInfoLoadSuccessful) {
            return;
        }
        Slog.e(TAG, "Vibrator controller initialization failed to load some HAL info for vibrator " + i);
    }

    public boolean registerVibratorStateListener(IVibratorStateListener iVibratorStateListener) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mLock) {
                if (this.mVibratorStateListeners.register(iVibratorStateListener)) {
                    lambda$notifyListenerOnVibrating$0(iVibratorStateListener, this.mIsVibrating);
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return true;
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return false;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    public boolean unregisterVibratorStateListener(IVibratorStateListener iVibratorStateListener) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return this.mVibratorStateListeners.unregister(iVibratorStateListener);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public void reloadVibratorInfoIfNeeded() {
        if (this.mVibratorInfoLoadSuccessful) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mVibratorInfoLoadSuccessful) {
                return;
            }
            int id = this.mVibratorInfo.getId();
            VibratorInfo.Builder builder = new VibratorInfo.Builder(id);
            this.mVibratorInfoLoadSuccessful = this.mNativeWrapper.getInfo(builder);
            this.mVibratorInfo = builder.build();
            if (!this.mVibratorInfoLoadSuccessful) {
                Slog.e(TAG, "Failed retry of HAL getInfo for vibrator " + id);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVibratorInfoLoadSuccessful() {
        return this.mVibratorInfoLoadSuccessful;
    }

    public VibratorInfo getVibratorInfo() {
        return this.mVibratorInfo;
    }

    public boolean isVibrating() {
        return this.mIsVibrating;
    }

    public float getCurrentAmplitude() {
        return this.mCurrentAmplitude;
    }

    public boolean isUnderExternalControl() {
        return this.mIsUnderExternalControl;
    }

    public boolean hasCapability(long j) {
        return this.mVibratorInfo.hasCapability(j);
    }

    public boolean isAvailable() {
        boolean isAvailable;
        synchronized (this.mLock) {
            isAvailable = this.mNativeWrapper.isAvailable();
        }
        return isAvailable;
    }

    public void setExternalControl(boolean z) {
        if (this.mVibratorInfo.hasCapability(8L)) {
            synchronized (this.mLock) {
                this.mIsUnderExternalControl = z;
                this.mNativeWrapper.setExternalControl(z);
            }
        }
    }

    public void updateAlwaysOn(int i, PrebakedSegment prebakedSegment) {
        if (this.mVibratorInfo.hasCapability(64L)) {
            synchronized (this.mLock) {
                if (prebakedSegment == null) {
                    this.mNativeWrapper.alwaysOnDisable(i);
                } else {
                    this.mNativeWrapper.alwaysOnEnable(i, prebakedSegment.getEffectId(), prebakedSegment.getEffectStrength());
                }
            }
        }
    }

    public void setAmplitude(float f) {
        synchronized (this.mLock) {
            if (this.mVibratorInfo.hasCapability(4L)) {
                this.mNativeWrapper.setAmplitude(f);
            }
            if (this.mIsVibrating) {
                this.mCurrentAmplitude = f;
            }
        }
    }

    public long on(long j, long j2) {
        long on;
        synchronized (this.mLock) {
            on = this.mNativeWrapper.on(j, j2);
            if (on > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return on;
    }

    public long on(PrebakedSegment prebakedSegment, long j) {
        long perform;
        synchronized (this.mLock) {
            perform = this.mNativeWrapper.perform(prebakedSegment.getEffectId(), prebakedSegment.getEffectStrength(), j);
            if (perform > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return perform;
    }

    public long on(PrimitiveSegment[] primitiveSegmentArr, long j) {
        long compose;
        if (!this.mVibratorInfo.hasCapability(32L)) {
            return 0L;
        }
        synchronized (this.mLock) {
            compose = this.mNativeWrapper.compose(primitiveSegmentArr, j);
            if (compose > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return compose;
    }

    public long on(RampSegment[] rampSegmentArr, long j) {
        long composePwle;
        if (!this.mVibratorInfo.hasCapability(1024L)) {
            return 0L;
        }
        synchronized (this.mLock) {
            composePwle = this.mNativeWrapper.composePwle(rampSegmentArr, this.mVibratorInfo.getDefaultBraking(), j);
            if (composePwle > 0) {
                this.mCurrentAmplitude = -1.0f;
                notifyListenerOnVibrating(true);
            }
        }
        return composePwle;
    }

    public void off() {
        synchronized (this.mLock) {
            this.mNativeWrapper.off();
            this.mCurrentAmplitude = 0.0f;
            notifyListenerOnVibrating(false);
        }
    }

    public void reset() {
        setExternalControl(false);
        off();
        getWrapper().richtapStop();
        getWrapper().linearMotorVibratorOff();
    }

    public String toString() {
        return "VibratorController{mVibratorInfo=" + this.mVibratorInfo + ", mVibratorInfoLoadSuccessful=" + this.mVibratorInfoLoadSuccessful + ", mIsVibrating=" + this.mIsVibrating + ", mCurrentAmplitude=" + this.mCurrentAmplitude + ", mIsUnderExternalControl=" + this.mIsUnderExternalControl + ", mVibratorStateListeners count=" + this.mVibratorStateListeners.getRegisteredCallbackCount() + '}';
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public void notifyListenerOnVibrating(final boolean z) {
        if (this.mIsVibrating != z) {
            this.mIsVibrating = z;
            this.mVibratorStateListeners.broadcast(new Consumer() { // from class: com.android.server.vibrator.VibratorController$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    VibratorController.this.lambda$notifyListenerOnVibrating$0(z, (IVibratorStateListener) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: notifyStateListener, reason: merged with bridge method [inline-methods] */
    public void lambda$notifyListenerOnVibrating$0(IVibratorStateListener iVibratorStateListener, boolean z) {
        try {
            iVibratorStateListener.onVibrating(z);
        } catch (RemoteException | RuntimeException e) {
            Slog.e(TAG, "Vibrator state listener failed to call", e);
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class NativeWrapper {
        private long mNativePtr = 0;

        private static native void alwaysOnDisable(long j, long j2);

        private static native void alwaysOnEnable(long j, long j2, long j3, long j4);

        private static native boolean getInfo(long j, VibratorInfo.Builder builder);

        private static native long getNativeFinalizer();

        private static native boolean isAvailable(long j);

        private static native long nativeInit(int i, OnVibrationCompleteListener onVibrationCompleteListener);

        private static native void off(long j);

        private static native long on(long j, long j2, long j3);

        private static native long performComposedEffect(long j, PrimitiveSegment[] primitiveSegmentArr, long j2);

        private static native long performEffect(long j, long j2, long j3, long j4);

        private static native long performPwleEffect(long j, RampSegment[] rampSegmentArr, int i, long j2);

        private static native void setAmplitude(long j, float f);

        private static native void setExternalControl(long j, boolean z);

        public void init(int i, OnVibrationCompleteListener onVibrationCompleteListener) {
            this.mNativePtr = nativeInit(i, onVibrationCompleteListener);
            long nativeFinalizer = getNativeFinalizer();
            if (nativeFinalizer != 0) {
                NativeAllocationRegistry.createMalloced(VibratorController.class.getClassLoader(), nativeFinalizer).registerNativeAllocation(this, this.mNativePtr);
            }
        }

        public boolean isAvailable() {
            return isAvailable(this.mNativePtr);
        }

        public long on(long j, long j2) {
            return on(this.mNativePtr, j, j2);
        }

        public void off() {
            off(this.mNativePtr);
        }

        public void setAmplitude(float f) {
            setAmplitude(this.mNativePtr, f);
        }

        public long perform(long j, long j2, long j3) {
            return performEffect(this.mNativePtr, j, j2, j3);
        }

        public long compose(PrimitiveSegment[] primitiveSegmentArr, long j) {
            return performComposedEffect(this.mNativePtr, primitiveSegmentArr, j);
        }

        public long composePwle(RampSegment[] rampSegmentArr, int i, long j) {
            return performPwleEffect(this.mNativePtr, rampSegmentArr, i, j);
        }

        public void setExternalControl(boolean z) {
            setExternalControl(this.mNativePtr, z);
        }

        public void alwaysOnEnable(long j, long j2, long j3) {
            alwaysOnEnable(this.mNativePtr, j, j2, j3);
        }

        public void alwaysOnDisable(long j) {
            alwaysOnDisable(this.mNativePtr, j);
        }

        public boolean getInfo(VibratorInfo.Builder builder) {
            return getInfo(this.mNativePtr, builder);
        }
    }

    public IVibratorControllerWrapper getWrapper() {
        return this.mVibratorControllerWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VibratorControllerWrapper implements IVibratorControllerWrapper {
        private static final float LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX = 2400.0f;
        private static final int PERFORM_EFFECT_SLOW_TIMING = 10;
        private static final float RICHTAP_STRENGTH_MAX = 255.0f;

        private VibratorControllerWrapper() {
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public IVibratorControllerExt getExtImpl() {
            return VibratorController.this.mIVibratorControllerExt;
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapSetAmplitude(int i) {
            synchronized (VibratorController.this.mLock) {
                getExtImpl().richtapSetAmplitude(i);
                if (VibratorController.this.mIsVibrating) {
                    VibratorController.this.mCurrentAmplitude = i / RICHTAP_STRENGTH_MAX;
                }
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapPerformHe(int i, int i2, int i3, int i4, int[] iArr, long j) {
            synchronized (VibratorController.this.mLock) {
                getExtImpl().setOnVibrationCompleteListener(VibratorController.this.mOnVibrationCompleteListener);
                getExtImpl().richtapPerformHe(i, i2, i3, i4, iArr, j);
                VibratorController.this.mCurrentAmplitude = i3 / RICHTAP_STRENGTH_MAX;
                VibratorController.this.notifyListenerOnVibrating(true);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapPerformEnvelope(int[] iArr, boolean z, int i, long j) {
            synchronized (VibratorController.this.mLock) {
                getExtImpl().setOnVibrationCompleteListener(VibratorController.this.mOnVibrationCompleteListener);
                getExtImpl().richtapPerformEnvelope(iArr, z, i, j);
                VibratorController.this.mCurrentAmplitude = i / RICHTAP_STRENGTH_MAX;
                VibratorController.this.notifyListenerOnVibrating(true);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public int richtapPerformEffect(int i, byte b, long j) {
            int richtapPerformEffect;
            synchronized (VibratorController.this.mLock) {
                getExtImpl().setOnVibrationCompleteListener(VibratorController.this.mOnVibrationCompleteListener);
                richtapPerformEffect = getExtImpl().richtapPerformEffect(i, b, j);
                if (richtapPerformEffect > 0) {
                    VibratorController.this.mCurrentAmplitude = b / RICHTAP_STRENGTH_MAX;
                    VibratorController.this.notifyListenerOnVibrating(true);
                }
            }
            return richtapPerformEffect;
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void richtapStop() {
            synchronized (VibratorController.this.mLock) {
                getExtImpl().richtapStop();
                VibratorController.this.mCurrentAmplitude = 0.0f;
                VibratorController.this.notifyListenerOnVibrating(false);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public long performExtPrebaked(int i, long j, int i2, long j2) {
            synchronized (VibratorController.this.mLock) {
                long uptimeMillis = SystemClock.uptimeMillis();
                VibratorController.this.mNativeWrapper.perform(i, 2L, j2);
                long uptimeMillis2 = SystemClock.uptimeMillis() - uptimeMillis;
                if (uptimeMillis2 > 10) {
                    Slog.d(VibratorController.TAG, "perform cost " + uptimeMillis2 + " ms");
                }
                if (j > 0) {
                    VibratorController.this.mCurrentAmplitude = (i2 / LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX) + 1.0f;
                    VibratorController.this.notifyListenerOnVibrating(true);
                }
            }
            return j;
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void linearMotorVibratorOff() {
            synchronized (VibratorController.this.mLock) {
                getExtImpl().linearMotorVibratorOff();
                VibratorController.this.mCurrentAmplitude = 0.0f;
                VibratorController.this.notifyListenerOnVibrating(false);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void linearMotorVibratorOn(int i, int i2, boolean z, long j) {
            synchronized (VibratorController.this.mLock) {
                getExtImpl().linearMotorVibratorOn(i, i2, z, j);
                VibratorController.this.mCurrentAmplitude = i2 / LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX;
                VibratorController.this.notifyListenerOnVibrating(true);
            }
        }

        @Override // com.android.server.vibrator.IVibratorControllerWrapper
        public void linearMotorVibratorSetVmax(int i) {
            synchronized (VibratorController.this.mLock) {
                getExtImpl().linearMotorVibratorSetVmax(i);
                if (VibratorController.this.mIsVibrating) {
                    VibratorController.this.mCurrentAmplitude = i / LINEAR_MOTOR_VIBRATOR_STRENGTH_MAX;
                }
            }
        }
    }
}
