package com.android.server.vibrator;

import android.os.SystemClock;
import android.os.Trace;
import android.util.Slog;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class CompleteEffectVibratorStep extends AbstractVibratorStep {
    private final boolean mCancelled;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompleteEffectVibratorStep(VibrationStepConductor vibrationStepConductor, long j, boolean z, VibratorController vibratorController, long j2) {
        super(vibrationStepConductor, j, vibratorController, null, -1, j2);
        this.mCancelled = z;
    }

    @Override // com.android.server.vibrator.Step
    public boolean isCleanUp() {
        return this.mCancelled;
    }

    @Override // com.android.server.vibrator.AbstractVibratorStep, com.android.server.vibrator.Step
    public List<Step> cancel() {
        if (this.mCancelled) {
            return Arrays.asList(new TurnOffVibratorStep(this.conductor, SystemClock.uptimeMillis(), this.controller));
        }
        return super.cancel();
    }

    @Override // com.android.server.vibrator.Step
    public List<Step> play() {
        Trace.traceBegin(8388608L, "CompleteEffectVibratorStep");
        try {
            if (VibrationThread.DEBUG) {
                StringBuilder sb = new StringBuilder();
                sb.append("Running ");
                sb.append(this.mCancelled ? "cancel" : "complete");
                sb.append(" vibration step on vibrator ");
                sb.append(this.controller.getVibratorInfo().getId());
                Slog.d("VibrationThread", sb.toString());
            }
            if (this.mVibratorCompleteCallbackReceived) {
                stopVibrating();
                return VibrationStepConductor.EMPTY_STEP_LIST;
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            float currentAmplitude = this.controller.getCurrentAmplitude();
            long min = Math.min((this.mPendingVibratorOffDeadline - uptimeMillis) - 1000, this.conductor.vibrationSettings.getRampDownDuration());
            long rampStepDuration = this.conductor.vibrationSettings.getRampStepDuration();
            if (currentAmplitude >= 0.001f && min > rampStepDuration) {
                if (VibrationThread.DEBUG) {
                    Slog.d("VibrationThread", "Ramping down vibrator " + this.controller.getVibratorInfo().getId() + " from amplitude " + currentAmplitude + " for " + min + "ms");
                }
                float f = currentAmplitude / ((float) (min / rampStepDuration));
                return Arrays.asList(new RampOffVibratorStep(this.conductor, this.startTime, currentAmplitude - f, f, this.controller, this.mCancelled ? uptimeMillis + min : this.mPendingVibratorOffDeadline));
            }
            if (!this.mCancelled) {
                return Arrays.asList(new TurnOffVibratorStep(this.conductor, this.mPendingVibratorOffDeadline, this.controller));
            }
            stopVibrating();
            return VibrationStepConductor.EMPTY_STEP_LIST;
        } finally {
            Trace.traceEnd(8388608L);
        }
    }
}
