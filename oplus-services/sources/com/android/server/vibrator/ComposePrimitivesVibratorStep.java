package com.android.server.vibrator;

import android.os.Trace;
import android.os.VibrationEffect;
import android.os.vibrator.PrimitiveSegment;
import android.os.vibrator.VibrationEffectSegment;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class ComposePrimitivesVibratorStep extends AbstractVibratorStep {
    private static final int DEFAULT_COMPOSITION_SIZE_LIMIT = 100;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComposePrimitivesVibratorStep(VibrationStepConductor vibrationStepConductor, long j, VibratorController vibratorController, VibrationEffect.Composed composed, int i, long j2) {
        super(vibrationStepConductor, Math.max(j, j2), vibratorController, composed, i, j2);
    }

    @Override // com.android.server.vibrator.Step
    public List<Step> play() {
        Trace.traceBegin(8388608L, "ComposePrimitivesStep");
        try {
            int compositionSizeMax = this.controller.getVibratorInfo().getCompositionSizeMax();
            VibrationEffect.Composed composed = this.effect;
            int i = this.segmentIndex;
            if (compositionSizeMax <= 0) {
                compositionSizeMax = 100;
            }
            List<PrimitiveSegment> unrollPrimitiveSegments = unrollPrimitiveSegments(composed, i, compositionSizeMax);
            if (unrollPrimitiveSegments.isEmpty()) {
                Slog.w("VibrationThread", "Ignoring wrong segment for a ComposePrimitivesStep: " + this.effect.getSegments().get(this.segmentIndex));
                return nextSteps(1);
            }
            if (VibrationThread.DEBUG) {
                Slog.d("VibrationThread", "Compose " + unrollPrimitiveSegments + " primitives on vibrator " + getVibratorId());
            }
            PrimitiveSegment[] primitiveSegmentArr = (PrimitiveSegment[]) unrollPrimitiveSegments.toArray(new PrimitiveSegment[unrollPrimitiveSegments.size()]);
            long on = this.controller.on(primitiveSegmentArr, getVibration().id);
            handleVibratorOnResult(on);
            getVibration().stats.reportComposePrimitives(on, primitiveSegmentArr);
            return nextSteps(unrollPrimitiveSegments.size());
        } finally {
            Trace.traceEnd(8388608L);
        }
    }

    private List<PrimitiveSegment> unrollPrimitiveSegments(VibrationEffect.Composed composed, int i, int i2) {
        ArrayList arrayList = new ArrayList(i2);
        int size = composed.getSegments().size();
        int repeatIndex = composed.getRepeatIndex();
        while (arrayList.size() < i2) {
            if (i == size) {
                if (repeatIndex < 0) {
                    break;
                }
                i = repeatIndex;
            }
            PrimitiveSegment primitiveSegment = (VibrationEffectSegment) composed.getSegments().get(i);
            if (!(primitiveSegment instanceof PrimitiveSegment)) {
                break;
            }
            arrayList.add(primitiveSegment);
            i++;
        }
        return arrayList;
    }
}
