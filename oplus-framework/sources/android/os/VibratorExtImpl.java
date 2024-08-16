package android.os;

import android.media.AudioAttributes;
import android.os.VibrationAttributes;

/* loaded from: classes.dex */
public class VibratorExtImpl implements IVibratorExt {
    private static final long MAX_HAPTIC_FEEDBACK_DURATION = 5000;
    private static final String TAG = "VibratorExtImpl";
    private Vibrator mVibrator;

    public VibratorExtImpl(Object base) {
        this.mVibrator = (Vibrator) base;
    }

    public boolean vibrate(VibrationEffect vibe) {
        return vibrate(vibe, null);
    }

    public boolean vibrate(VibrationEffect vibe, AudioAttributes attributes) {
        VibrationAttributes temp;
        if (vibe != null) {
            long duration = vibe.getDuration();
            if (duration >= 0 && duration < MAX_HAPTIC_FEEDBACK_DURATION) {
                if (attributes == null) {
                    temp = new VibrationAttributes.Builder().build();
                } else {
                    temp = new VibrationAttributes.Builder(attributes).build();
                }
                if (temp.getUsage() == 0) {
                    VibrationAttributes vibrationAttributes = new VibrationAttributes.Builder(temp).setUsage(18).build();
                    this.mVibrator.vibrate(vibe, vibrationAttributes);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
