package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;

/* loaded from: classes.dex */
public class OplusVibrationEffectSegment extends VibrationEffectSegment {
    public static final Parcelable.Creator<OplusVibrationEffectSegment> CREATOR = new Parcelable.Creator<OplusVibrationEffectSegment>() { // from class: android.os.vibrator.OplusVibrationEffectSegment.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusVibrationEffectSegment createFromParcel(Parcel in) {
            in.readInt();
            return new OplusVibrationEffectSegment(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusVibrationEffectSegment[] newArray(int size) {
            return new OplusVibrationEffectSegment[size];
        }
    };
    private static final String EMPTY_RINGTONE_PATH = "";
    private static final int PARCEL_TOKEN_OPLUS_VIBRATION_EFFECT = 1037;
    private static final int SETTINGS_VALUE_EFFECT_STRONG = 2400;
    private int mEffectId;
    private final int mEffectStrength;
    private String mRingtonePath;

    public OplusVibrationEffectSegment(int effectId, int effectStrength) {
        this.mEffectId = -1;
        this.mRingtonePath = "";
        this.mEffectId = effectId;
        this.mRingtonePath = "";
        this.mEffectStrength = effectStrength;
    }

    public OplusVibrationEffectSegment(String ringtonePath, int effectStrength) {
        this.mEffectId = -1;
        this.mRingtonePath = "";
        this.mEffectId = -1;
        this.mRingtonePath = ringtonePath;
        this.mEffectStrength = effectStrength;
    }

    public OplusVibrationEffectSegment(Parcel in) {
        this.mEffectId = -1;
        this.mRingtonePath = "";
        this.mEffectId = in.readInt();
        this.mEffectStrength = in.readInt();
        this.mRingtonePath = in.readString();
    }

    private static boolean isValidEffectStrength(int strength) {
        return strength == -1 || (strength >= 0 && strength <= 2400);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PARCEL_TOKEN_OPLUS_VIBRATION_EFFECT);
        dest.writeInt(this.mEffectId);
        dest.writeInt(this.mEffectStrength);
        dest.writeString(this.mRingtonePath);
    }

    public int getEffectId() {
        return this.mEffectId;
    }

    public String getRingtonePath() {
        return this.mRingtonePath;
    }

    public int getEffectStrength() {
        return this.mEffectStrength;
    }

    public long getDuration() {
        return -1L;
    }

    public boolean areVibrationFeaturesSupported(Vibrator vibrator) {
        return false;
    }

    public boolean isHapticFeedbackCandidate() {
        return false;
    }

    public boolean hasNonZeroAmplitude() {
        return true;
    }

    public void validate() {
        if (!isValidEffectStrength(this.mEffectStrength)) {
            throw new IllegalArgumentException("Unknown effect strength (value=" + this.mEffectStrength + ")");
        }
        if (this.mRingtonePath == null) {
            throw new IllegalArgumentException("Ringtone path is null");
        }
    }

    /* renamed from: resolve, reason: merged with bridge method [inline-methods] */
    public OplusVibrationEffectSegment m202resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public OplusVibrationEffectSegment m203scale(float scaleFactor) {
        return this;
    }

    /* renamed from: applyEffectStrength, reason: merged with bridge method [inline-methods] */
    public OplusVibrationEffectSegment m201applyEffectStrength(int effectStrength) {
        return this;
    }

    public String toString() {
        return "OplusVibrationEffectSegment{mEffectId=" + this.mEffectId + ", mEffectStrength=" + this.mEffectStrength + ", mRingtonePath='" + this.mRingtonePath + "'}";
    }
}
