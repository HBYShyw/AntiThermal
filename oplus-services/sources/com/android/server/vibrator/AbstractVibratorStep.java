package com.android.server.vibrator;

import android.os.SystemClock;
import android.os.VibrationEffect;
import android.util.Slog;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class AbstractVibratorStep extends Step {
    public final VibratorController controller;
    public final VibrationEffect.Composed effect;
    long mPendingVibratorOffDeadline;
    boolean mVibratorCompleteCallbackReceived;
    long mVibratorOnResult;
    public final int segmentIndex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractVibratorStep(VibrationStepConductor vibrationStepConductor, long j, VibratorController vibratorController, VibrationEffect.Composed composed, int i, long j2) {
        super(vibrationStepConductor, j);
        this.controller = vibratorController;
        this.effect = composed;
        this.segmentIndex = i;
        this.mPendingVibratorOffDeadline = j2;
    }

    public int getVibratorId() {
        return this.controller.getVibratorInfo().getId();
    }

    @Override // com.android.server.vibrator.Step
    public long getVibratorOnDuration() {
        return this.mVibratorOnResult;
    }

    @Override // com.android.server.vibrator.Step
    public boolean acceptVibratorCompleteCallback(int i) {
        if (getVibratorId() != i) {
            return false;
        }
        boolean z = this.mPendingVibratorOffDeadline > SystemClock.uptimeMillis();
        if (VibrationThread.DEBUG) {
            Slog.d("VibrationThread", "Received completion callback from " + i + ", accepted = " + z);
        }
        this.mPendingVibratorOffDeadline = 0L;
        this.mVibratorCompleteCallbackReceived = true;
        return z;
    }

    @Override // com.android.server.vibrator.Step
    public List<Step> cancel() {
        return Arrays.asList(new CompleteEffectVibratorStep(this.conductor, SystemClock.uptimeMillis(), true, this.controller, this.mPendingVibratorOffDeadline));
    }

    @Override // com.android.server.vibrator.Step
    public void cancelImmediately() {
        if (this.mPendingVibratorOffDeadline > SystemClock.uptimeMillis()) {
            stopVibrating();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long handleVibratorOnResult(long j) {
        this.mVibratorOnResult = j;
        if (VibrationThread.DEBUG) {
            Slog.d("VibrationThread", "Turned on vibrator " + getVibratorId() + ", result = " + this.mVibratorOnResult);
        }
        if (this.mVibratorOnResult > 0) {
            this.mPendingVibratorOffDeadline = SystemClock.uptimeMillis() + this.mVibratorOnResult + 1000;
        } else {
            this.mPendingVibratorOffDeadline = 0L;
        }
        return this.mVibratorOnResult;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void stopVibrating() {
        if (VibrationThread.DEBUG) {
            Slog.d("VibrationThread", "Turning off vibrator " + getVibratorId());
        }
        this.controller.off();
        getVibration().stats.reportVibratorOff();
        this.mPendingVibratorOffDeadline = 0L;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void changeAmplitude(float f) {
        if (VibrationThread.DEBUG) {
            Slog.d("VibrationThread", "Amplitude changed on vibrator " + getVibratorId() + " to " + f);
        }
        this.controller.setAmplitude(f);
        getVibration().stats.reportSetAmplitude();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<Step> nextSteps(int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = this.mVibratorOnResult;
        if (j > 0) {
            uptimeMillis += j;
        }
        return nextSteps(uptimeMillis, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<Step> nextSteps(long j, int i) {
        int i2 = this.segmentIndex + i;
        int size = this.effect.getSegments().size();
        int repeatIndex = this.effect.getRepeatIndex();
        if (i2 >= size && repeatIndex >= 0) {
            int i3 = size - repeatIndex;
            getVibration().stats.reportRepetition((i2 - repeatIndex) / i3);
            i2 = ((i2 - size) % i3) + repeatIndex;
        }
        AbstractVibratorStep nextVibrateStep = this.conductor.nextVibrateStep(j, this.controller, this.effect, i2, this.mPendingVibratorOffDeadline);
        return nextVibrateStep == null ? VibrationStepConductor.EMPTY_STEP_LIST : Arrays.asList(nextVibrateStep);
    }
}
