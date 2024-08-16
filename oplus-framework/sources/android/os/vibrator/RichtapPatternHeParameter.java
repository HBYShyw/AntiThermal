package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;

/* loaded from: classes.dex */
public class RichtapPatternHeParameter extends VibrationEffectSegment {
    public static final Parcelable.Creator<RichtapPatternHeParameter> CREATOR = new Parcelable.Creator<RichtapPatternHeParameter>() { // from class: android.os.vibrator.RichtapPatternHeParameter.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapPatternHeParameter createFromParcel(Parcel in) {
            in.readInt();
            return new RichtapPatternHeParameter(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapPatternHeParameter[] newArray(int size) {
            return new RichtapPatternHeParameter[size];
        }
    };
    private static final int HASH_CODE = 14;
    private static final int HASH_CODE_BASE = 37;
    private static final int PARCEL_TOKEN_PATTERN_HE_LOOP_PARAMETER = 2034;
    private static final int RICHTAP_AMPLITUDE_MAX = 255;
    private final String TAG = "RichtapPatternHeParameter";
    private int mAmplitude;
    private int mFreq;
    private int mInterval;

    public RichtapPatternHeParameter(Parcel in) {
        this.mInterval = in.readInt();
        this.mAmplitude = in.readInt();
        this.mFreq = in.readInt();
        Log.d("RichtapPatternHeParameter", "parcel mInterval:" + this.mInterval + " mAmplitude:" + this.mAmplitude + " mFreq:" + this.mFreq);
    }

    public RichtapPatternHeParameter(int interval, int amplitude, int freq) {
        this.mInterval = interval;
        this.mAmplitude = amplitude;
        this.mFreq = freq;
        Log.d("RichtapPatternHeParameter", "mInterval:" + this.mInterval + " mAmplitude:" + this.mAmplitude + " mFreq:" + this.mFreq);
    }

    public int getInterval() {
        return this.mInterval;
    }

    public int getAmplitude() {
        return this.mAmplitude;
    }

    public int getFreq() {
        return this.mFreq;
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
    public RichtapPatternHeParameter m217resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public RichtapPatternHeParameter m218scale(float scaleFactor) {
        return this;
    }

    public void validate() {
        int i = this.mAmplitude;
        if (i < -1 || i > 255 || this.mInterval < -1 || this.mFreq < -1) {
            throw new IllegalArgumentException("mAmplitude=" + this.mAmplitude + " mInterval=" + this.mInterval + " mFreq=" + this.mFreq);
        }
    }

    /* renamed from: applyEffectStrength, reason: merged with bridge method [inline-methods] */
    public RichtapPatternHeParameter m216applyEffectStrength(int effectStrength) {
        return this;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RichtapPatternHeParameter)) {
            return false;
        }
        RichtapPatternHeParameter other = (RichtapPatternHeParameter) o;
        int interval = other.getInterval();
        int amplitude = other.getAmplitude();
        int freq = other.getFreq();
        return interval == this.mInterval && amplitude == this.mAmplitude && freq == this.mFreq;
    }

    public int hashCode() {
        int result = 14 + (this.mInterval * 37);
        return result + (this.mAmplitude * 37);
    }

    public String toString() {
        return "RichtapPatternHeParameter: {mAmplitude=" + this.mAmplitude + "}{mInterval=" + this.mInterval + "}{mFreq=" + this.mFreq + "}";
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(PARCEL_TOKEN_PATTERN_HE_LOOP_PARAMETER);
        out.writeInt(this.mInterval);
        out.writeInt(this.mAmplitude);
        out.writeInt(this.mFreq);
        Log.d("RichtapPatternHeParameter", "writeToParcel mInterval:" + this.mInterval + " mAmplitude:" + this.mAmplitude + " mFreq:" + this.mFreq);
    }
}
