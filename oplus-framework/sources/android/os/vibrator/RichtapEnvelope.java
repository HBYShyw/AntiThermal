package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import java.util.Arrays;

/* loaded from: classes.dex */
public class RichtapEnvelope extends VibrationEffectSegment {
    public static final Parcelable.Creator<RichtapEnvelope> CREATOR = new Parcelable.Creator<RichtapEnvelope>() { // from class: android.os.vibrator.RichtapEnvelope.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapEnvelope createFromParcel(Parcel in) {
            in.readInt();
            return new RichtapEnvelope(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapEnvelope[] newArray(int size) {
            return new RichtapEnvelope[size];
        }
    };
    private static final int ENVELOPE_ELE_SIZE = 3;
    private static final int ENVELOPE_SIZE = 4;
    private static final int PARCEL_TOKEN_ENVELOPE = 2032;
    private static final int RICHTAP_AMPLITUDE_MAX = 255;
    private int mAmplitude;
    private int[] mFreqArr;
    private int[] mRelativeTimeArr;
    private int[] mScaleArr;
    private boolean mSteepMode;

    public RichtapEnvelope(Parcel in) {
        this(in.createIntArray(), in.createIntArray(), in.createIntArray(), in.readInt() == 1, in.readInt());
    }

    public RichtapEnvelope(int[] relativeTimeArr, int[] scaleArr, int[] freqArr, boolean steepMode, int amplitude) {
        this.mRelativeTimeArr = Arrays.copyOf(relativeTimeArr, 4);
        this.mScaleArr = Arrays.copyOf(scaleArr, 4);
        this.mFreqArr = Arrays.copyOf(freqArr, 4);
        this.mSteepMode = steepMode;
        this.mAmplitude = amplitude;
    }

    public int[] getRelativeTimeArr() {
        return this.mRelativeTimeArr;
    }

    public int[] getScaleArr() {
        return this.mScaleArr;
    }

    public int[] getFreqArr() {
        return this.mFreqArr;
    }

    public int[] getPattern() {
        int[] params = new int[12];
        for (int i = 0; i < getRelativeTimeArr().length; i++) {
            params[i * 3] = getRelativeTimeArr()[i];
            params[(i * 3) + 1] = getScaleArr()[i];
            params[(i * 3) + 2] = getFreqArr()[i];
        }
        return params;
    }

    public boolean isSteepMode() {
        return this.mSteepMode;
    }

    public int getAmplitude() {
        return this.mAmplitude;
    }

    public int describeContents() {
        return 0;
    }

    public long getDuration() {
        if (this.mRelativeTimeArr.length > 0) {
            return r0[r0.length - 1];
        }
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
    public RichtapEnvelope m205resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public RichtapEnvelope m206scale(float scaleFactor) {
        return this;
    }

    public void validate() {
        for (int i = 0; i < 4; i++) {
            if (this.mRelativeTimeArr[i] < 0) {
                throw new IllegalArgumentException("relative time can not be negative");
            }
            if (this.mScaleArr[i] < 0) {
                throw new IllegalArgumentException("scale can not be negative");
            }
            if (this.mFreqArr[i] < 0) {
                throw new IllegalArgumentException("freq must be positive");
            }
        }
        int i2 = this.mAmplitude;
        if (i2 < -1 || i2 == 0 || i2 > 255) {
            throw new IllegalArgumentException("amplitude must either be DEFAULT_AMPLITUDE, or between 1 and 255 inclusive (amplitude=" + this.mAmplitude + ")");
        }
    }

    /* renamed from: applyEffectStrength, reason: merged with bridge method [inline-methods] */
    public RichtapEnvelope m204applyEffectStrength(int effectStrength) {
        return this;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RichtapEnvelope)) {
            return false;
        }
        RichtapEnvelope other = (RichtapEnvelope) o;
        int[] timeArr = other.getRelativeTimeArr();
        int[] scArr = other.getScaleArr();
        int[] frArr = other.getFreqArr();
        return this.mAmplitude == other.getAmplitude() && Arrays.equals(timeArr, this.mRelativeTimeArr) && Arrays.equals(scArr, this.mScaleArr) && Arrays.equals(frArr, this.mFreqArr) && other.isSteepMode() == this.mSteepMode;
    }

    public int hashCode() {
        return this.mRelativeTimeArr[2] + this.mScaleArr[2] + this.mFreqArr[2];
    }

    public String toString() {
        return "RichtapEnvelope: {relativeTimeArr=" + Arrays.toString(this.mRelativeTimeArr) + ", scaleArr = " + Arrays.toString(this.mScaleArr) + ", freqArr = " + Arrays.toString(this.mFreqArr) + ", SteepMode = " + this.mSteepMode + ", Amplitude = " + this.mAmplitude + "}";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(PARCEL_TOKEN_ENVELOPE);
        parcel.writeIntArray(this.mRelativeTimeArr);
        parcel.writeIntArray(this.mScaleArr);
        parcel.writeIntArray(this.mFreqArr);
        parcel.writeInt(this.mSteepMode ? 1 : 0);
        parcel.writeInt(this.mAmplitude);
    }
}
