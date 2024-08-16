package com.oplus.util;

import android.content.res.Resources;
import android.util.Log;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class UxScreenUtil {
    private static final String FEATURE_DRAGON_FLY = "oplus.software.fold_remap_display_disabled";
    private static final String FEATURE_FOLD = "oplus.hardware.type.fold";
    private static final String FEATURE_TABLET = "oplus.hardware.type.tablet";
    private static final String FEATURE_UXICON_WHITE_SWAN_SUPPORT = "oplus.software.uxicon.whiteswan.support";

    public static boolean isFoldDisplay() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.fold");
    }

    public static boolean isTabletDevices() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.hardware.type.tablet");
    }

    public static int getDefaultIconSize(Resources res) {
        if (isFoldDisplay() && isWhiteSwan()) {
            return res.getDimensionPixelSize(201654492);
        }
        if (isFoldDisplay() && !isDragonFly()) {
            return res.getDimensionPixelSize(201654491);
        }
        if (isTabletDevices()) {
            return res.getDimensionPixelSize(201654492);
        }
        return res.getDimensionPixelSize(201654414);
    }

    public static boolean isDragonFly() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.fold_remap_display_disabled");
    }

    public static boolean isWhiteSwan() {
        boolean isWhiteSwan = OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.uxicon.whiteswan.support");
        Log.d("UxScreenUtil", "isWhiteSwan -- " + isWhiteSwan);
        return isWhiteSwan;
    }
}
