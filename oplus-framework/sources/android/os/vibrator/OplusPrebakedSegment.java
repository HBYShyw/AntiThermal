package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.VibrationAttributes;
import android.os.Vibrator;

/* loaded from: classes.dex */
public class OplusPrebakedSegment extends VibrationEffectSegment {
    public static final Parcelable.Creator<OplusPrebakedSegment> CREATOR = new Parcelable.Creator<OplusPrebakedSegment>() { // from class: android.os.vibrator.OplusPrebakedSegment.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPrebakedSegment createFromParcel(Parcel in) {
            in.readInt();
            return new OplusPrebakedSegment(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusPrebakedSegment[] newArray(int size) {
            return new OplusPrebakedSegment[size];
        }
    };
    private static final int PARCEL_TOKEN_OPLUS_NATIVE_ONESHOT = 1036;
    public static final int SETTINGS_VALUE_EFFECT_STRONG = 2400;
    private static final String TAG = "OplusPrebakedSegment";
    private int mDefaultUsage;
    private long mDuration;
    private int mEffectStrength;
    private boolean mIsRTPType;
    private final int mWaveformId;

    public OplusPrebakedSegment(int waveformId, int effectStrength, boolean isRTPType) {
        this.mDuration = -1L;
        this.mIsRTPType = true;
        this.mDefaultUsage = 0;
        this.mWaveformId = waveformId;
        this.mEffectStrength = effectStrength;
        this.mIsRTPType = isRTPType;
    }

    public OplusPrebakedSegment(int waveformId, long duration, int effectStrength, boolean isRTPType) {
        this.mDuration = -1L;
        this.mIsRTPType = true;
        this.mDefaultUsage = 0;
        this.mWaveformId = waveformId;
        this.mDuration = duration;
        this.mEffectStrength = effectStrength;
        this.mIsRTPType = isRTPType;
    }

    public OplusPrebakedSegment(Parcel in) {
        this.mDuration = -1L;
        this.mIsRTPType = true;
        this.mDefaultUsage = 0;
        this.mWaveformId = in.readInt();
        this.mDuration = in.readLong();
        this.mEffectStrength = in.readInt();
        this.mIsRTPType = in.readBoolean();
        this.mDefaultUsage = in.readInt();
    }

    public int getDefaultUsage() {
        return this.mDefaultUsage;
    }

    public void setDefaultUsage(int usage) {
        this.mDefaultUsage = usage;
    }

    private static boolean isValidEffectStrength(int strength) {
        return strength == -1 || (strength >= 0 && strength <= 2400);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PARCEL_TOKEN_OPLUS_NATIVE_ONESHOT);
        dest.writeInt(this.mWaveformId);
        dest.writeLong(this.mDuration);
        dest.writeInt(this.mEffectStrength);
        dest.writeBoolean(this.mIsRTPType);
        dest.writeInt(this.mDefaultUsage);
    }

    public int getWaveformId() {
        return this.mWaveformId;
    }

    public int getEffectStrength() {
        return this.mEffectStrength;
    }

    public void setEffectStrength(int effectStrength) {
        this.mEffectStrength = effectStrength;
    }

    public boolean isRTPType() {
        return this.mIsRTPType;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public boolean areVibrationFeaturesSupported(Vibrator vibrator) {
        return false;
    }

    public boolean isHapticFeedbackCandidate() {
        return getDefaultUsage() == 18;
    }

    public boolean hasNonZeroAmplitude() {
        return true;
    }

    public void validate() {
        long j = this.mDuration;
        if (j < 0 && j != -1) {
            throw new IllegalArgumentException("duration must be >= 0 (timing=" + this.mDuration + ")");
        }
        if (!isValidEffectStrength(this.mEffectStrength)) {
            throw new IllegalArgumentException("Unknown effect strength (value=" + this.mEffectStrength + ")");
        }
    }

    /* renamed from: resolve, reason: merged with bridge method [inline-methods] */
    public OplusPrebakedSegment m199resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public OplusPrebakedSegment m200scale(float scaleFactor) {
        return this;
    }

    /* renamed from: applyEffectStrength, reason: merged with bridge method [inline-methods] */
    public OplusPrebakedSegment m198applyEffectStrength(int effectStrength) {
        return this;
    }

    public String toString() {
        return "OplusPrebakedSegment{mWaveformId=" + this.mWaveformId + ", mDuration=" + this.mDuration + ", mEffectStrength=" + this.mEffectStrength + ", mDefaultUsage=" + VibrationAttributes.usageToString(this.mDefaultUsage) + '}';
    }
}
