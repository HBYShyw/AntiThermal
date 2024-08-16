package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;

/* loaded from: classes.dex */
public class RichtapExtPrebaked extends VibrationEffectSegment {
    public static final Parcelable.Creator<RichtapExtPrebaked> CREATOR = new Parcelable.Creator<RichtapExtPrebaked>() { // from class: android.os.vibrator.RichtapExtPrebaked.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapExtPrebaked createFromParcel(Parcel in) {
            in.readInt();
            return new RichtapExtPrebaked(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapExtPrebaked[] newArray(int size) {
            return new RichtapExtPrebaked[size];
        }
    };
    private static final int PARCEL_TOKEN_EXT_PREBAKED = 2030;
    private static final int RICHTAP_PREBAKED_STRENGTH_MAX = 100;
    private int mEffectId;
    private int mStrength;

    public RichtapExtPrebaked(Parcel in) {
        this(in.readInt(), in.readInt());
    }

    public RichtapExtPrebaked(int effectId, int strength) {
        this.mEffectId = effectId;
        this.mStrength = strength;
    }

    public int getId() {
        return this.mEffectId;
    }

    public int getScale() {
        return this.mStrength;
    }

    public int describeContents() {
        return 0;
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
        return false;
    }

    /* renamed from: resolve, reason: merged with bridge method [inline-methods] */
    public RichtapExtPrebaked m208resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public RichtapExtPrebaked m209scale(float scaleFactor) {
        return this;
    }

    public void validate() {
        if (this.mEffectId < 0) {
            throw new IllegalArgumentException("Unknown RichtapExtPrebaked effect type (value=" + this.mEffectId + ")");
        }
        int i = this.mStrength;
        if (i < 1 || i > 100) {
            throw new IllegalArgumentException("mStrength must be between 1 and 100 inclusive (mStrength=" + this.mStrength + ")");
        }
    }

    /* renamed from: applyEffectStrength, reason: merged with bridge method [inline-methods] */
    public RichtapExtPrebaked m207applyEffectStrength(int effectStrength) {
        return this;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RichtapExtPrebaked)) {
            return false;
        }
        RichtapExtPrebaked other = (RichtapExtPrebaked) o;
        return this.mEffectId == other.mEffectId;
    }

    public int hashCode() {
        return this.mEffectId;
    }

    public String toString() {
        return "RichtapExtPrebaked{mEffectId=" + this.mEffectId + "mStrength = " + this.mStrength + "}";
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(PARCEL_TOKEN_EXT_PREBAKED);
        out.writeInt(this.mEffectId);
        out.writeInt(this.mStrength);
    }
}
