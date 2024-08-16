package com.android.server.vibrator;

import android.os.Build;
import android.os.CombinedVibration;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.vibrator.PrebakedSegment;
import android.os.vibrator.PrimitiveSegment;
import android.os.vibrator.RampSegment;
import android.os.vibrator.VibrationEffectSegment;
import android.util.IntArray;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.server.vibrator.Vibration;
import com.android.server.vibrator.VibrationThread;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class VibrationStepConductor implements IBinder.DeathRecipient {
    static final long CALLBACKS_EXTRA_TIMEOUT = 1000;
    private static final boolean DEBUG = VibrationThread.DEBUG;
    static final List<Step> EMPTY_STEP_LIST = new ArrayList();
    static final float RAMP_OFF_AMPLITUDE_MIN = 0.001f;
    private static final String TAG = "VibrationThread";
    public final DeviceVibrationEffectAdapter deviceEffectAdapter;
    private int mPendingVibrateSteps;
    private int mRemainingStartSequentialEffectSteps;

    @GuardedBy({"mLock"})
    private final IntArray mSignalVibratorsComplete;
    private int mSuccessfulVibratorOnSteps;
    private final HalVibration mVibration;
    public final VibrationSettings vibrationSettings;
    public final VibrationThread.VibratorManagerHooks vibratorManagerHooks;
    private final SparseArray<VibratorController> mVibrators = new SparseArray<>();
    private final PriorityQueue<Step> mNextSteps = new PriorityQueue<>();
    private final Queue<Step> mPendingOnVibratorCompleteSteps = new LinkedList();
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private Vibration.EndInfo mSignalCancel = null;

    @GuardedBy({"mLock"})
    private boolean mSignalCancelImmediate = false;
    private Vibration.EndInfo mCancelledVibrationEndInfo = null;
    private boolean mCancelledImmediately = false;
    private IVibrationStepConductorExt mVibrationStepConductorExt = (IVibrationStepConductorExt) ExtLoader.type(IVibrationStepConductorExt.class).base(this).create();
    private IVibrationStepConductorWrapper mVibrationStepConductorWrapper = new VibrationStepConductorWrapper();

    /* JADX INFO: Access modifiers changed from: package-private */
    public VibrationStepConductor(HalVibration halVibration, VibrationSettings vibrationSettings, DeviceVibrationEffectAdapter deviceVibrationEffectAdapter, SparseArray<VibratorController> sparseArray, VibrationThread.VibratorManagerHooks vibratorManagerHooks) {
        this.mVibration = halVibration;
        this.vibrationSettings = vibrationSettings;
        this.deviceEffectAdapter = deviceVibrationEffectAdapter;
        this.vibratorManagerHooks = vibratorManagerHooks;
        CombinedVibration effect = halVibration.getEffect();
        for (int i = 0; i < sparseArray.size(); i++) {
            if (effect.hasVibrator(sparseArray.keyAt(i))) {
                this.mVibrators.put(sparseArray.keyAt(i), sparseArray.valueAt(i));
            }
        }
        this.mSignalVibratorsComplete = new IntArray(this.mVibrators.size());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractVibratorStep nextVibrateStep(long j, VibratorController vibratorController, VibrationEffect.Composed composed, int i, long j2) {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        int repeatIndex = i >= composed.getSegments().size() ? composed.getRepeatIndex() : i;
        if (repeatIndex < 0) {
            return new CompleteEffectVibratorStep(this, j, false, vibratorController, j2);
        }
        VibrationEffectSegment vibrationEffectSegment = (VibrationEffectSegment) composed.getSegments().get(repeatIndex);
        AbstractVibratorStep nextVibrateStep = getWrapper().getExtImpl().nextVibrateStep(vibrationEffectSegment, j, vibratorController, composed, repeatIndex, j2);
        if (nextVibrateStep != null) {
            return nextVibrateStep;
        }
        if (vibrationEffectSegment instanceof PrebakedSegment) {
            return new PerformPrebakedVibratorStep(this, j, vibratorController, composed, repeatIndex, j2);
        }
        if (vibrationEffectSegment instanceof PrimitiveSegment) {
            return new ComposePrimitivesVibratorStep(this, j, vibratorController, composed, repeatIndex, j2);
        }
        if (vibrationEffectSegment instanceof RampSegment) {
            return new ComposePwleVibratorStep(this, j, vibratorController, composed, repeatIndex, j2);
        }
        return new SetAmplitudeVibratorStep(this, j, vibratorController, composed, repeatIndex, j2);
    }

    public void prepareToStart() {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        CombinedVibration.Sequential sequential = toSequential(this.mVibration.getEffect());
        this.mPendingVibrateSteps++;
        this.mRemainingStartSequentialEffectSteps = sequential.getEffects().size();
        this.mNextSteps.offer(new StartSequentialEffectStep(this, sequential));
        this.mVibration.stats.reportStarted();
    }

    public HalVibration getVibration() {
        return this.mVibration;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseArray<VibratorController> getVibrators() {
        return this.mVibrators;
    }

    public boolean isFinished() {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (this.mCancelledImmediately) {
            return true;
        }
        return this.mPendingOnVibratorCompleteSteps.isEmpty() && this.mNextSteps.isEmpty();
    }

    public Vibration.EndInfo calculateVibrationEndInfo() {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        Vibration.EndInfo endInfo = this.mCancelledVibrationEndInfo;
        if (endInfo != null) {
            return endInfo;
        }
        if (this.mPendingVibrateSteps > 0 || this.mRemainingStartSequentialEffectSteps > 0) {
            return null;
        }
        if (this.mSuccessfulVibratorOnSteps > 0) {
            return new Vibration.EndInfo(Vibration.Status.FINISHED);
        }
        return new Vibration.EndInfo(Vibration.Status.IGNORED_UNSUPPORTED);
    }

    public boolean waitUntilNextStepIsDue() {
        Step peek;
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        processAllNotifySignals();
        if (this.mCancelledImmediately) {
            return false;
        }
        if (!this.mPendingOnVibratorCompleteSteps.isEmpty() || (peek = this.mNextSteps.peek()) == null) {
            return true;
        }
        getWrapper().getExtImpl().updateVibrationAmplitude(peek);
        long calculateWaitTime = peek.calculateWaitTime();
        if (calculateWaitTime <= 0) {
            return true;
        }
        synchronized (this.mLock) {
            if (hasPendingNotifySignalLocked()) {
                return false;
            }
            try {
                this.mLock.wait(calculateWaitTime);
            } catch (InterruptedException unused) {
            }
            return false;
        }
    }

    private Step pollNext() {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (!this.mPendingOnVibratorCompleteSteps.isEmpty()) {
            return this.mPendingOnVibratorCompleteSteps.poll();
        }
        return this.mNextSteps.poll();
    }

    public void runNextStep() {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        Step pollNext = pollNext();
        if (pollNext != null) {
            if (DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Playing vibration id ");
                sb.append(getVibration().id);
                sb.append(pollNext instanceof AbstractVibratorStep ? " on vibrator " + ((AbstractVibratorStep) pollNext).getVibratorId() : "");
                sb.append(" ");
                sb.append(pollNext.getClass().getSimpleName());
                sb.append(pollNext.isCleanUp() ? " (cleanup)" : "");
                Slog.d(TAG, sb.toString());
            }
            List<Step> play = pollNext.play();
            if (pollNext.getVibratorOnDuration() > 0) {
                this.mSuccessfulVibratorOnSteps++;
            }
            if (pollNext instanceof StartSequentialEffectStep) {
                this.mRemainingStartSequentialEffectSteps--;
            }
            if (!pollNext.isCleanUp()) {
                this.mPendingVibrateSteps--;
            }
            for (int i = 0; i < play.size(); i++) {
                this.mPendingVibrateSteps += !play.get(i).isCleanUp() ? 1 : 0;
            }
            this.mNextSteps.addAll(play);
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        if (DEBUG) {
            Slog.d(TAG, "Binder died, cancelling vibration...");
        }
        notifyCancelled(new Vibration.EndInfo(Vibration.Status.CANCELLED_BINDER_DIED), false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0063, code lost:
    
        if (r4.mSignalCancelImmediate == false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void notifyCancelled(Vibration.EndInfo endInfo, boolean z) {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(false);
        }
        boolean z2 = DEBUG;
        if (z2) {
            Slog.d(TAG, "Vibration cancel requested with signal=" + endInfo + ", immediate=" + z);
        }
        if (endInfo == null || !endInfo.status.name().startsWith("CANCEL")) {
            Slog.w(TAG, "Vibration cancel requested with bad signal=" + endInfo + ", using CANCELLED_UNKNOWN_REASON to ensure cancellation.");
            endInfo = new Vibration.EndInfo(Vibration.Status.CANCELLED_BY_UNKNOWN_REASON);
        }
        synchronized (this.mLock) {
            if (z) {
            }
            Vibration.EndInfo endInfo2 = this.mSignalCancel;
            if (endInfo2 == null) {
                this.mSignalCancelImmediate = z | this.mSignalCancelImmediate;
                if (endInfo2 == null) {
                    this.mSignalCancel = endInfo;
                } else if (z2) {
                    Slog.d(TAG, "Vibration cancel request new signal=" + endInfo + " ignored as the vibration was already cancelled with signal=" + this.mSignalCancel + ", but immediate flag was updated to " + this.mSignalCancelImmediate);
                }
                this.mLock.notify();
                return;
            }
            if (z2) {
                Slog.d(TAG, "Vibration cancel request ignored as the vibration " + this.mVibration.id + "is already being cancelled with signal=" + this.mSignalCancel + ", immediate=" + this.mSignalCancelImmediate);
            }
        }
    }

    public void notifyVibratorComplete(int i) {
        if (DEBUG) {
            Slog.d(TAG, "Vibration complete reported by vibrator " + i);
        }
        synchronized (this.mLock) {
            this.mSignalVibratorsComplete.add(i);
            this.mLock.notify();
        }
    }

    public void notifySyncedVibrationComplete() {
        if (DEBUG) {
            Slog.d(TAG, "Synced vibration complete reported by vibrator manager");
        }
        synchronized (this.mLock) {
            for (int i = 0; i < this.mVibrators.size(); i++) {
                this.mSignalVibratorsComplete.add(this.mVibrators.keyAt(i));
            }
            this.mLock.notify();
        }
    }

    public boolean wasNotifiedToCancel() {
        boolean z;
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(false);
        }
        synchronized (this.mLock) {
            z = this.mSignalCancel != null;
        }
        return z;
    }

    @GuardedBy({"mLock"})
    private boolean hasPendingNotifySignalLocked() {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        if (this.mSignalCancel == null || this.mCancelledVibrationEndInfo != null) {
            return (this.mSignalCancelImmediate && !this.mCancelledImmediately) || this.mSignalVibratorsComplete.size() > 0;
        }
        return true;
    }

    private void processAllNotifySignals() {
        int[] iArr;
        Vibration.EndInfo endInfo;
        boolean z = true;
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        synchronized (this.mLock) {
            iArr = null;
            if (this.mSignalCancelImmediate) {
                if (this.mCancelledImmediately) {
                    Slog.wtf(TAG, "Immediate cancellation signal processed twice");
                }
                endInfo = this.mSignalCancel;
            } else {
                z = false;
                endInfo = null;
            }
            Vibration.EndInfo endInfo2 = this.mSignalCancel;
            if (endInfo2 != null && this.mCancelledVibrationEndInfo == null) {
                endInfo = endInfo2;
            }
            if (!z && this.mSignalVibratorsComplete.size() > 0) {
                iArr = this.mSignalVibratorsComplete.toArray();
                this.mSignalVibratorsComplete.clear();
            }
        }
        if (z) {
            processCancelImmediately(endInfo);
            return;
        }
        if (endInfo != null) {
            processCancel(endInfo);
        }
        if (iArr != null) {
            processVibratorsComplete(iArr);
        }
    }

    public void processCancel(Vibration.EndInfo endInfo) {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        this.mCancelledVibrationEndInfo = endInfo;
        ArrayList arrayList = new ArrayList();
        while (true) {
            Step pollNext = pollNext();
            if (pollNext != null) {
                arrayList.addAll(pollNext.cancel());
            } else {
                this.mPendingVibrateSteps = 0;
                this.mNextSteps.addAll(arrayList);
                return;
            }
        }
    }

    public void processCancelImmediately(Vibration.EndInfo endInfo) {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        this.mCancelledImmediately = true;
        this.mCancelledVibrationEndInfo = endInfo;
        while (true) {
            Step pollNext = pollNext();
            if (pollNext != null) {
                pollNext.cancelImmediately();
            } else {
                this.mPendingVibrateSteps = 0;
                return;
            }
        }
    }

    private void processVibratorsComplete(int[] iArr) {
        if (Build.IS_DEBUGGABLE) {
            expectIsVibrationThread(true);
        }
        for (int i : iArr) {
            Iterator<Step> it = this.mNextSteps.iterator();
            while (true) {
                if (it.hasNext()) {
                    Step next = it.next();
                    if (next.acceptVibratorCompleteCallback(i)) {
                        it.remove();
                        this.mPendingOnVibratorCompleteSteps.offer(next);
                        break;
                    }
                }
            }
        }
    }

    private static CombinedVibration.Sequential toSequential(CombinedVibration combinedVibration) {
        if (combinedVibration instanceof CombinedVibration.Sequential) {
            return (CombinedVibration.Sequential) combinedVibration;
        }
        return CombinedVibration.startSequential().addNext(combinedVibration).combine();
    }

    private static void expectIsVibrationThread(boolean z) {
        if ((Thread.currentThread() instanceof VibrationThread) != z) {
            Slog.wtfStack("VibrationStepConductor", "Thread caller assertion failed, expected isVibrationThread=" + z);
        }
    }

    public IVibrationStepConductorWrapper getWrapper() {
        return this.mVibrationStepConductorWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class VibrationStepConductorWrapper implements IVibrationStepConductorWrapper {
        private VibrationStepConductorWrapper() {
        }

        @Override // com.android.server.vibrator.IVibrationStepConductorWrapper
        public IVibrationStepConductorExt getExtImpl() {
            return VibrationStepConductor.this.mVibrationStepConductorExt;
        }

        @Override // com.android.server.vibrator.IVibrationStepConductorWrapper
        public void notifyVibrationAmplitudeUpdated() {
            Slog.d(VibrationStepConductor.TAG, "notifyVibrationAmplitudeUpdated");
            synchronized (VibrationStepConductor.this.mLock) {
                VibrationStepConductor.this.mLock.notify();
            }
        }
    }
}
