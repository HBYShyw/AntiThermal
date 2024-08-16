package android.os.vibrator;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import java.util.Locale;

/* loaded from: classes.dex */
public class RichtapPatternHe extends VibrationEffectSegment {
    private static final int CONTINUOUS_EVENT = 4096;
    private static final int CONTINUOUS_EVENT_POINT_MAX = 16;
    private static final int CONTINUOUS_EVENT_POINT_SIZE = 3;
    public static final Parcelable.Creator<RichtapPatternHe> CREATOR = new Parcelable.Creator<RichtapPatternHe>() { // from class: android.os.vibrator.RichtapPatternHe.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapPatternHe createFromParcel(Parcel in) {
            in.readInt();
            return new RichtapPatternHe(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RichtapPatternHe[] newArray(int size) {
            return new RichtapPatternHe[size];
        }
    };
    private static final int DEFAULT_DURATION = 100;
    private static final int EVENT_TRANSIENT_DURATION = 48;
    private static final int HASH_CODE = 17;
    private static final int HASH_CODE_BASE = 37;
    private static final int HE_1_0_EVENT_DURATION_INDEX = 4;
    private static final int HE_1_0_HEADER_LENGTH = 7;
    private static final int HE_1_0_PATTERN_ITEM_LENGTH = 55;
    private static final int HE_1_0_PATTERN_RELATIVE_TIME_INDEX = 1;
    private static final int HE_1_0_VERSION_1_EVENT_DURATION_INDEX = 4;
    private static final int HE_1_0_VERSION_1_ITEM_LENGTH = 17;
    private static final int HE_2_0_EVENT_DURATION_INDEX = 6;
    private static final int HE_2_0_EVENT_HEADER_LENGTH = 2;
    private static final int HE_2_0_EVENT_RELATIVE_TIME_INDEX = 3;
    private static final int HE_2_0_HEADER_LENGTH = 5;
    private static final int HE_2_0_PATTERN_ABSOLUTION_TIME_INDEX = 1;
    private static final int HE_2_0_PATTERN_EVENT_AMOUNT_INDEX = 2;
    private static final int HE_2_0_PATTERN_HEADER_LENGTH = 3;
    private static final int HE_2_0_PATTERN_INDEX = 0;
    private static final int HE_2_0_PATTERN_RELATIVE_TIME_INDEX = 1;
    private static final int HE_PATTERN_FORMAT_VERSION_1 = 1;
    private static final int HE_PATTERN_FORMAT_VERSION_2 = 2;
    private static final int HE_PATTERN_FORMAT_VERSION_3 = 3;
    private static final String HE_VERSION_1_0 = "He_1.0";
    private static final String HE_VERSION_2_0 = "He_2.0";
    private static final String INVALID_HE_PATTERN = "Invalid He Pattern";
    private static final int PARCEL_TOKEN_PATTERN_HE = 2033;
    private static final int SDK_HAL_NEW_FORMAT_DATA_VERSION = 2;
    private static final String TAG = "RichtapPatternHe";
    private static final int TRANSIENT_EVENT = 4097;
    private int mAmplitude;
    private long mDuration;
    private int mEventCount;
    private int mFreq;
    private int mInterval;
    private int mLooper;
    private int[] mPatternInfo;

    public RichtapPatternHe(Parcel in) {
        this.mDuration = 100L;
        this.mPatternInfo = in.createIntArray();
        this.mLooper = in.readInt();
        this.mInterval = in.readInt();
        this.mAmplitude = in.readInt();
        this.mFreq = in.readInt();
        this.mDuration = getPatternDuration(this.mPatternInfo);
    }

    public RichtapPatternHe(int[] patternInfo, int looper, int interval, int amplitude, int freq) {
        this.mDuration = 100L;
        this.mPatternInfo = patternInfo;
        this.mLooper = looper;
        this.mInterval = interval;
        this.mFreq = freq;
        this.mAmplitude = amplitude;
        this.mDuration = getPatternDuration(patternInfo);
        this.mEventCount = 0;
    }

    public long getDuration() {
        return getPatternDuration(this.mPatternInfo);
    }

    private int getHe20PatternDuration(int[] patternData) {
        int eventEndTime;
        if (patternData == null || patternData.length == 0) {
            Log.e(TAG, "pattern data is null!");
            return -1;
        }
        if (2 == patternData[0]) {
            int duration = 100;
            int loc = 8;
            int eventAmount = patternData[7];
            int absoluteTime = patternData[6];
            int eventCount = 0;
            int maxEventEndTime = 0;
            while (loc + 6 <= patternData.length - 1 && eventCount <= eventAmount) {
                eventCount++;
                if (4097 == patternData[loc]) {
                    eventEndTime = patternData[loc + 3] + 48;
                } else if (4096 == patternData[loc]) {
                    eventEndTime = patternData[loc + 3] + patternData[loc + 6];
                } else {
                    Log.e(TAG, "unknown event type! loc = " + loc);
                    return 100;
                }
                maxEventEndTime = Math.max(maxEventEndTime, eventEndTime);
                loc += patternData[loc + 1] + 2;
                if (eventCount == eventAmount) {
                    duration = absoluteTime + maxEventEndTime;
                    if (loc + 3 <= patternData.length - 1) {
                        absoluteTime = patternData[loc + 1];
                        eventAmount = patternData[loc + 2];
                        eventCount = 0;
                        maxEventEndTime = 0;
                        loc += 3;
                    }
                }
            }
            return duration;
        }
        Log.e(TAG, "invalid pattern data!");
        return -1;
    }

    private int getHe10PatternDuration(int[] patternData) {
        if (patternData == null || patternData.length == 0) {
            Log.e(TAG, "pattern data is null!");
            return -1;
        }
        if (1 == patternData[0]) {
            if ((patternData.length - 1) % 17 != 0) {
                Log.e(TAG, "invalid length!");
                return -1;
            }
            int eventNum = (patternData.length - 1) / 17;
            int lastEventLoc = ((eventNum - 1) * 17) + 1;
            if (4097 == patternData[lastEventLoc]) {
                return patternData[lastEventLoc + 1] + 48;
            }
            if (4096 == patternData[lastEventLoc]) {
                return patternData[lastEventLoc + 1] + patternData[lastEventLoc + 4];
            }
            Log.e(TAG, "unknown event type! loc = " + lastEventLoc);
            return -1;
        }
        if (3 == patternData[0]) {
            if ((patternData.length - 1) % 55 != 0) {
                Log.e(TAG, "invalid length!");
                return -1;
            }
            int eventNum2 = (patternData.length - 1) / 55;
            int lastEventLoc2 = ((eventNum2 - 1) * 55) + 1;
            if (4097 == patternData[lastEventLoc2]) {
                return patternData[lastEventLoc2 + 1] + 48;
            }
            if (4096 == patternData[lastEventLoc2]) {
                return patternData[lastEventLoc2 + 1] + patternData[lastEventLoc2 + 4];
            }
            Log.e(TAG, "unknown event type! loc = " + lastEventLoc2);
            return -1;
        }
        Log.e(TAG, "invalid pattern data!");
        return -1;
    }

    private int getPatternDuration(int[] patternData) {
        if (patternData == null || patternData.length == 0) {
            Log.e(TAG, "pattern data is null!");
            return -1;
        }
        if (2 == patternData[0]) {
            return getHe20PatternDuration(patternData);
        }
        if (1 == patternData[0] || 3 == patternData[0]) {
            return getHe10PatternDuration(patternData);
        }
        Log.e(TAG, "invalid pattern data!");
        return -1;
    }

    public boolean checkIfEffectHe20(int[] pattern) {
        if (pattern == null || pattern.length <= 0) {
            return false;
        }
        int versionOrType = pattern[0];
        return versionOrType == 2;
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
    public RichtapPatternHe m214resolve(int defaultAmplitude) {
        return this;
    }

    /* renamed from: scale, reason: merged with bridge method [inline-methods] */
    public RichtapPatternHe m215scale(float scaleFactor) {
        return this;
    }

    public int getEventCount() {
        return this.mEventCount;
    }

    public int describeContents() {
        return 0;
    }

    public int[] getPatternInfo() {
        return this.mPatternInfo;
    }

    public int getLooper() {
        return this.mLooper;
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

    public void validate() {
        long j = this.mDuration;
        if (j <= 0 && j != -1) {
            throw new IllegalArgumentException("duration must be positive (duration=" + this.mDuration + ")");
        }
    }

    /* renamed from: applyEffectStrength, reason: merged with bridge method [inline-methods] */
    public RichtapPatternHe m213applyEffectStrength(int effectStrength) {
        return this;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RichtapPatternHe)) {
            return false;
        }
        RichtapPatternHe other = (RichtapPatternHe) o;
        return other.mDuration == this.mDuration && other.mPatternInfo == this.mPatternInfo;
    }

    public int hashCode() {
        int result = 17 + (((int) this.mDuration) * 37);
        return result + (this.mEventCount * 37);
    }

    public String toString() {
        String patternDetail = INVALID_HE_PATTERN;
        int[] iArr = this.mPatternInfo;
        if (iArr != null && iArr.length > 0) {
            int i = iArr[0];
            if (2 == i) {
                int patternTotal = -1;
                int patternCount = -1;
                if (iArr.length >= 5) {
                    int patternNumDetail = iArr[4];
                    patternTotal = patternNumDetail & 65535;
                    patternCount = ((-65536) & patternNumDetail) >> 16;
                }
                patternDetail = String.format(Locale.US, "%s [%d/%d]", HE_VERSION_2_0, Integer.valueOf(patternCount), Integer.valueOf(patternTotal));
            } else {
                patternDetail = (1 == i || 3 == i) ? HE_VERSION_1_0 : String.format(Locale.US, "Unknown He Version [%d]", Integer.valueOf(this.mPatternInfo[0]));
            }
        }
        return "RichtapPatternHe{mLooper=" + this.mLooper + ", mInterval=" + this.mInterval + ", mAmplitude=" + this.mAmplitude + ", mFreq=" + this.mFreq + ", mDuration=" + this.mDuration + ", " + patternDetail + "}";
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(PARCEL_TOKEN_PATTERN_HE);
        out.writeIntArray(this.mPatternInfo);
        out.writeInt(this.mLooper);
        out.writeInt(this.mInterval);
        out.writeInt(this.mAmplitude);
        out.writeInt(this.mFreq);
    }
}
