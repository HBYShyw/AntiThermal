package android.os;

import android.os.VibrationEffect;
import android.os.vibrator.RichtapEnvelope;
import android.os.vibrator.RichtapExtPrebaked;
import android.os.vibrator.RichtapHapticParameter;
import android.os.vibrator.RichtapPatternHe;
import android.os.vibrator.RichtapPatternHeParameter;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class RichTapVibrationEffect {
    private static final int AAC_CLIENT = 16711680;
    private static final int EFFECT_ID_START = 4096;
    private static final int MAJOR_RICHTAP_VERSION = 8192;
    private static final int MINOR_RICHTAP_VERSION = 16;
    private static final int OPLUS_CLIENT = 65536;
    private static final String TAG = "RichTapVibrationEffect";
    private static final int VIBRATION_EFFECT_SUPPORT_NO = 2;
    private static final int VIBRATION_EFFECT_SUPPORT_UNKNOWN = 0;
    private static final int VIBRATION_EFFECT_SUPPORT_YES = 1;

    private RichTapVibrationEffect() {
    }

    public static VibrationEffect createExtPreBaked(int effectId, int strength) {
        VibrationEffect.Composed composed = new VibrationEffect.Composed(new RichtapExtPrebaked(effectId + 4096, strength));
        composed.validate();
        return composed;
    }

    public static VibrationEffect createEnvelope(int[] relativeTimeArr, int[] scaleArr, int[] freqArr, boolean steepMode, int amplitude) {
        VibrationEffect.Composed composed = new VibrationEffect.Composed(new RichtapEnvelope(relativeTimeArr, scaleArr, freqArr, steepMode, amplitude));
        composed.validate();
        return composed;
    }

    public static VibrationEffect createPatternHeParameter(int interval, int amplitude, int freq) {
        VibrationEffect.Composed composed = new VibrationEffect.Composed(new RichtapPatternHeParameter(interval, amplitude, freq));
        composed.validate();
        return composed;
    }

    public static VibrationEffect createHapticParameter(int[] param, int length) {
        VibrationEffect.Composed composed = new VibrationEffect.Composed(new RichtapHapticParameter(param, length));
        composed.validate();
        return composed;
    }

    public static VibrationEffect createPatternHeWithParam(int[] patternInfo, int looper, int interval, int amplitude, int freq) {
        VibrationEffect.Composed composed = new VibrationEffect.Composed(new RichtapPatternHe(patternInfo, looper, interval, amplitude, freq));
        composed.validate();
        return composed;
    }

    public static int checkIfRichTapSupport() {
        if (!OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_RICHTAP_SUPPORT)) {
            return 2;
        }
        Log.d(TAG, "checkIfRichTapSupport mRichTapSupport:73744");
        return 73744;
    }

    /* loaded from: classes.dex */
    public static class SenderId {
        int mPid;
        int mSeq;

        public SenderId() {
            this.mPid = 0;
            this.mSeq = 0;
        }

        public SenderId(int pid, int seq) {
            this.mPid = 0;
            this.mSeq = 0;
            this.mPid = pid;
            this.mSeq = seq;
        }

        public int getPid() {
            return this.mPid;
        }

        public void setPid(int pid) {
            this.mPid = pid;
        }

        public int getSeq() {
            return this.mSeq;
        }

        public void setSeq(int seq) {
            this.mSeq = seq;
        }

        public void reset() {
            this.mPid = -1;
            this.mSeq = -1;
        }
    }
}
