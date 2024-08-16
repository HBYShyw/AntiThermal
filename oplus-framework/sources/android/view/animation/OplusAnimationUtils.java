package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.util.OplusPlatformLevelUtils;

/* loaded from: classes.dex */
public class OplusAnimationUtils {
    private static final boolean OPLUS_FEATURE_WINDOW_ANIM_LOW_END_CONFIG = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_WINDOW_ANIM_LOW_END_CONFIG);
    private static final String TAG = "OplusAnimationUtils";

    public static BaseInterpolator createInterpolatorFromXml(String name, Resources res, Resources.Theme theme, AttributeSet attrs) {
        if (name.equals("oplusDecelerateInterpolator")) {
            BaseInterpolator interpolator = new OplusDecelerateInterpolator();
            return interpolator;
        }
        if (name.equals("oplusAccelerateDecelerateInterpolator")) {
            BaseInterpolator interpolator2 = new OplusAccelerateDecelerateInterpolator();
            return interpolator2;
        }
        if (!name.equals("oplusBezierInterpolator")) {
            return null;
        }
        BaseInterpolator interpolator3 = new OplusBezierInterpolator(res, theme, attrs);
        return interpolator3;
    }

    public static int getPlatformAnimation(Context context, int animResId) {
        int platformLevel = OplusPlatformLevelUtils.getInstance(context).getPlatformAnimationLevel();
        StringBuilder append = new StringBuilder().append("getPlatformAnimation platformLevel ").append(platformLevel).append(" LEVEL_LOW ").append(1).append(" needAnimForLowEndDevice ");
        boolean z = OPLUS_FEATURE_WINDOW_ANIM_LOW_END_CONFIG;
        Log.d(TAG, append.append(z).toString());
        int resId = animResId;
        if (platformLevel == 1 || z) {
            switch (animResId) {
                case 201981966:
                    resId = 201982011;
                    break;
                case 201981969:
                    resId = 201982012;
                    break;
                case 201981972:
                    resId = 201982013;
                    break;
                case 201981973:
                    resId = 201982014;
                    break;
            }
            Log.d(TAG, "Using resId " + Integer.toHexString(resId) + " for animResId " + Integer.toHexString(animResId));
        }
        return resId;
    }
}
