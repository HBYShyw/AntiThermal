package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public class RichtapHapticParameter extends VibrationEffectSegment {
    public static final Parcelable.Creator<RichtapHapticParameter> CREATOR = new Parcelable.Creator<RichtapHapticParameter>() { // from class: android.os.vibrator.RichtapHapticParameter.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapHapticParameter createFromParcel(Parcel in) {
            in.readInt();
            return new RichtapHapticParameter(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapHapticParameter[] newArray(int size) {
            return new RichtapHapticParameter[size];
        }
    };
    private static final int PARCEL_TOKEN_HAPTIC_PARAMETER = 2035;
    private final String TAG = "RichtapHapticParameter";
    private int mLength;
    private int[] mParam;

    public RichtapHapticParameter(Parcel in) {
        this.mParam = in.createIntArray();
        this.mLength = in.readInt();
        Log.d("RichtapHapticParameter", "parcel mLength:" + this.mLength);
    }

    public RichtapHapticParameter(int[] param, int length) {
        this.mParam = param;
        this.mLength = length;
        Log.d("RichtapHapticParameter", "parcel mLength:" + this.mLength);
    }

    public int[] getParam() {
        return this.mParam;
    }

    public int getLength() {
        return this.mLength;
    }

    /* renamed from: resolve, reason: merged with bridge method [inline-methods] */
    public RichtapHapticParameter m211resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public RichtapHapticParameter m212scale(float scaleFactor) {
        return this;
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

    public void validate() {
        int[] iArr = this.mParam;
        if (iArr == null || iArr.length == 0 || this.mLength != iArr.length) {
            throw new IllegalArgumentException("empty param");
        }
    }

    /* renamed from: applyEffectStrength, reason: merged with bridge method [inline-methods] */
    public RichtapHapticParameter m210applyEffectStrength(int effectStrength) {
        return this;
    }

    public boolean equals(Object o) {
        return (o instanceof RichtapHapticParameter) && getLength() == ((RichtapHapticParameter) o).getLength() && Arrays.equals(getParam(), ((RichtapHapticParameter) o).getParam());
    }

    public int hashCode() {
        return Objects.hash(this.mParam, Integer.valueOf(this.mLength));
    }

    public String toString() {
        return "RichtapHapticParameter: {mLength =" + this.mLength + ",mParam:" + Arrays.toString(this.mParam) + "}";
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(PARCEL_TOKEN_HAPTIC_PARAMETER);
        out.writeIntArray(this.mParam);
        out.writeInt(this.mLength);
        Log.d("RichtapHapticParameter", "writeToParcel, mLength:" + this.mLength);
    }
}
