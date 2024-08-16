package com.android.server.vibrator;

import android.os.VibrationEffect;
import android.os.VibratorInfo;
import com.android.server.vibrator.VibrationEffectAdapters;
import java.util.Arrays;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class DeviceVibrationEffectAdapter implements VibrationEffectAdapters.EffectAdapter<VibratorInfo> {
    private final List<VibrationEffectAdapters.SegmentsAdapter<VibratorInfo>> mSegmentAdapters;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceVibrationEffectAdapter(VibrationSettings vibrationSettings) {
        this.mSegmentAdapters = Arrays.asList(new RampToStepAdapter(vibrationSettings.getRampStepDuration()), new StepToRampAdapter(), new RampDownAdapter(vibrationSettings.getRampDownDuration(), vibrationSettings.getRampStepDuration()), new ClippingAmplitudeAndFrequencyAdapter());
    }

    @Override // com.android.server.vibrator.VibrationEffectAdapters.EffectAdapter
    public VibrationEffect apply(VibrationEffect vibrationEffect, VibratorInfo vibratorInfo) {
        return VibrationEffectAdapters.apply(vibrationEffect, this.mSegmentAdapters, vibratorInfo);
    }
}
