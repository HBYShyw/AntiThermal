package android.os;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes.dex */
public final class DynamicEffect extends VibrationEffect implements Parcelable {
    public static final Parcelable.Creator<DynamicEffect> CREATOR = new Parcelable.Creator<DynamicEffect>() { // from class: android.os.DynamicEffect.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DynamicEffect createFromParcel(Parcel in) {
            return new DynamicEffect(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DynamicEffect[] newArray(int size) {
            return new DynamicEffect[size];
        }
    };
    private static final String TAG = "DynamicEffect";
    String mPatternJson;

    public DynamicEffect(Parcel in) {
        this.mPatternJson = in.readString();
    }

    public DynamicEffect(String patternJson) {
        this.mPatternJson = patternJson;
    }

    public static DynamicEffect create(String json) {
        if (TextUtils.isEmpty(json)) {
            Log.e(TAG, "empty pattern,do nothing");
            return null;
        }
        return new DynamicEffect(json);
    }

    @Override // android.os.VibrationEffect, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getPatternInfo() {
        return this.mPatternJson;
    }

    public boolean areVibrationFeaturesSupported(Vibrator vibrator) {
        return false;
    }

    public long[] computeCreateWaveformOffOnTimingsOrNull() {
        return null;
    }

    public void validate() {
    }

    public int hashCode() {
        String str = this.mPatternJson;
        if (str != null) {
            int result = 17 + (str.hashCode() * 37);
            return result;
        }
        return 17;
    }

    public long getDuration() {
        return -1L;
    }

    /* renamed from: resolve, reason: merged with bridge method [inline-methods] */
    public DynamicEffect m132resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public DynamicEffect m133scale(float scaleFactor) {
        return this;
    }

    public String toString() {
        return "DynamicEffect{mPatternJson=" + this.mPatternJson + "}";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mPatternJson);
    }
}
