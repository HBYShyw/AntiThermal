package com.android.server.vibrator;

import android.os.Trace;
import android.os.VibrationEffect;
import android.os.vibrator.PrebakedSegment;
import android.os.vibrator.VibrationEffectSegment;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PerformPrebakedVibratorStep extends AbstractVibratorStep {
    /* JADX INFO: Access modifiers changed from: package-private */
    public PerformPrebakedVibratorStep(VibrationStepConductor vibrationStepConductor, long j, VibratorController vibratorController, VibrationEffect.Composed composed, int i, long j2) {
        super(vibrationStepConductor, Math.max(j, j2), vibratorController, composed, i, j2);
    }

    @Override // com.android.server.vibrator.Step
    public List<Step> play() {
        Trace.traceBegin(8388608L, "PerformPrebakedVibratorStep");
        try {
            PrebakedSegment prebakedSegment = (VibrationEffectSegment) this.effect.getSegments().get(this.segmentIndex);
            if (!(prebakedSegment instanceof PrebakedSegment)) {
                Slog.w("VibrationThread", "Ignoring wrong segment for a PerformPrebakedVibratorStep: " + prebakedSegment);
                return nextSteps(1);
            }
            PrebakedSegment prebakedSegment2 = prebakedSegment;
            if (VibrationThread.DEBUG) {
                Slog.d("VibrationThread", "Perform " + VibrationEffect.effectIdToString(prebakedSegment2.getEffectId()) + " on vibrator " + this.controller.getVibratorInfo().getId());
            }
            VibrationEffect fallback = getVibration().getFallback(prebakedSegment2.getEffectId());
            long on = this.controller.on(prebakedSegment2, getVibration().id);
            handleVibratorOnResult(on);
            getVibration().stats.reportPerformEffect(on, prebakedSegment2);
            if (on == 0 && prebakedSegment2.shouldFallback() && (fallback instanceof VibrationEffect.Composed)) {
                if (VibrationThread.DEBUG) {
                    Slog.d("VibrationThread", "Playing fallback for effect " + VibrationEffect.effectIdToString(prebakedSegment2.getEffectId()));
                }
                AbstractVibratorStep nextVibrateStep = this.conductor.nextVibrateStep(this.startTime, this.controller, replaceCurrentSegment((VibrationEffect.Composed) fallback), this.segmentIndex, this.mPendingVibratorOffDeadline);
                List<Step> play = nextVibrateStep.play();
                handleVibratorOnResult(nextVibrateStep.getVibratorOnDuration());
                return play;
            }
            return nextSteps(1);
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    private VibrationEffect.Composed replaceCurrentSegment(VibrationEffect.Composed composed) {
        ArrayList arrayList = new ArrayList(this.effect.getSegments());
        int repeatIndex = this.effect.getRepeatIndex();
        arrayList.remove(this.segmentIndex);
        arrayList.addAll(this.segmentIndex, composed.getSegments());
        if (this.segmentIndex < this.effect.getRepeatIndex()) {
            repeatIndex += composed.getSegments().size() - 1;
        }
        return new VibrationEffect.Composed(arrayList, repeatIndex);
    }
}
