package android.view;

import com.oplus.dynamicframerate.DynamicFrameRateController;
import com.oplus.view.IOplusWindowManagerConstans;

/* loaded from: classes.dex */
public class SurfaceControlExtImpl implements ISurfaceControlExt {
    public static final int ANIMATION_TYPE_APP_TRANSITION = 1;
    public static final int ANIMATION_TYPE_INVALID = -1;
    public static final int ANIMATION_TYPE_NONE = 0;
    public static final int ANIMATION_TYPE_PAIR_TASK = 3;
    public static final int ANIMATION_TYPE_RECENTS = 2;
    public static final String STRING_ANIMATION_TYPE_APP_TRANSITION = "app_transition";
    public static final String STRING_ANIMATION_TYPE_RECENTS = "recents_animation";
    public static final String STRING_PAIR_TASK_ANIMATION = "PairTaskAnimationContainer";
    private static final String TYPE_SYSTEM_ONSCREEN_FINGERPRINT = "type=" + String.valueOf(IOplusWindowManagerConstans.BaseLayoutParams.TYPE_SYSTEM_ONSCREEN_FINGERPRINT);
    private int mAnimationType = -1;

    public SurfaceControlExtImpl(Object base) {
    }

    public void onSetMatrix(SurfaceControl sc, float[] float9) {
        DynamicFrameRateController.getInstance().getAnimationSpeedAware().onSetMatrix(sc, float9);
    }

    public int getAnimationType(SurfaceControl sc) {
        if (this.mAnimationType == -1) {
            this.mAnimationType = animationStringToType(sc.toString());
        }
        return this.mAnimationType;
    }

    private int animationStringToType(String desc) {
        if (desc.contains(STRING_ANIMATION_TYPE_APP_TRANSITION)) {
            return 1;
        }
        if (desc.contains(STRING_ANIMATION_TYPE_RECENTS)) {
            return 2;
        }
        if (desc.contains(STRING_PAIR_TASK_ANIMATION)) {
            return 3;
        }
        return 0;
    }

    public boolean isFingerprintType(String name) {
        if (name != null && name.contains(TYPE_SYSTEM_ONSCREEN_FINGERPRINT)) {
            return true;
        }
        return false;
    }
}
