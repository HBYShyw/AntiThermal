package android.os.vibrator;

import android.os.Parcel;

/* loaded from: classes.dex */
public class VibrationEffectSegmentExtImpl implements IVibrationEffectSegmentExt {
    private static final int PARCEL_TOKEN_ENVELOPE = 2031;
    private static final int PARCEL_TOKEN_EXT_PREBAKED = 2030;
    private static final int PARCEL_TOKEN_HAPTIC_PARAMETER = 2034;
    private static final int PARCEL_TOKEN_OPLUS_NATIVE_ONESHOT = 1036;
    private static final int PARCEL_TOKEN_OPLUS_VIBRATION_EFFECT = 1037;
    private static final int PARCEL_TOKEN_PATTERN_HE = 2032;
    private static final int PARCEL_TOKEN_PATTERN_HE_LOOP_PARAMETER = 2033;
    private static final String TAG = "VibrationEffectSegmentExtImpl";

    public VibrationEffectSegmentExtImpl(Object base) {
    }

    public VibrationEffectSegment createExtendedEffect(Parcel in) {
        switch (in.readInt()) {
            case PARCEL_TOKEN_OPLUS_NATIVE_ONESHOT /* 1036 */:
                return new OplusPrebakedSegment(in);
            case PARCEL_TOKEN_OPLUS_VIBRATION_EFFECT /* 1037 */:
                return new OplusVibrationEffectSegment(in);
            case PARCEL_TOKEN_EXT_PREBAKED /* 2030 */:
                return new RichtapExtPrebaked(in);
            case PARCEL_TOKEN_ENVELOPE /* 2031 */:
                return new RichtapEnvelope(in);
            case PARCEL_TOKEN_PATTERN_HE /* 2032 */:
                return new RichtapPatternHe(in);
            case PARCEL_TOKEN_PATTERN_HE_LOOP_PARAMETER /* 2033 */:
                return new RichtapPatternHeParameter(in);
            case PARCEL_TOKEN_HAPTIC_PARAMETER /* 2034 */:
                return new RichtapHapticParameter(in);
            default:
                int offset = in.dataPosition() - 4;
                in.setDataPosition(offset);
                return null;
        }
    }
}
