package com.android.server.vibrator;

import android.os.Trace;
import android.util.Slog;
import com.android.server.job.controllers.JobStatus;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class FinishSequentialEffectStep extends Step {
    public final StartSequentialEffectStep startedStep;

    @Override // com.android.server.vibrator.Step
    public boolean isCleanUp() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FinishSequentialEffectStep(StartSequentialEffectStep startSequentialEffectStep) {
        super(startSequentialEffectStep.conductor, JobStatus.NO_LATEST_RUNTIME);
        this.startedStep = startSequentialEffectStep;
    }

    @Override // com.android.server.vibrator.Step
    public List<Step> play() {
        List<Step> asList;
        Trace.traceBegin(8388608L, "FinishSequentialEffectStep");
        try {
            if (VibrationThread.DEBUG) {
                Slog.d("VibrationThread", "FinishSequentialEffectStep for effect #" + this.startedStep.currentIndex);
            }
            VibrationStepConductor vibrationStepConductor = this.conductor;
            vibrationStepConductor.vibratorManagerHooks.noteVibratorOff(vibrationStepConductor.getVibration().callerInfo.uid);
            Step nextStep = this.startedStep.nextStep();
            if (nextStep == null) {
                asList = VibrationStepConductor.EMPTY_STEP_LIST;
            } else {
                asList = Arrays.asList(nextStep);
            }
            return asList;
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    @Override // com.android.server.vibrator.Step
    public List<Step> cancel() {
        cancelImmediately();
        return VibrationStepConductor.EMPTY_STEP_LIST;
    }

    @Override // com.android.server.vibrator.Step
    public void cancelImmediately() {
        VibrationStepConductor vibrationStepConductor = this.conductor;
        vibrationStepConductor.vibratorManagerHooks.noteVibratorOff(vibrationStepConductor.getVibration().callerInfo.uid);
    }
}
