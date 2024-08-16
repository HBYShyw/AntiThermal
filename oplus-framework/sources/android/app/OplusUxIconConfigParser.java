package android.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Rect;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.PathParser;
import android.view.autolayout.IOplusAutoLayoutManager;
import com.oplus.os.OplusEnvironment;
import com.oplus.theme.OplusThemeUtil;
import com.oplus.util.OplusRoundRectUtil;
import com.oplus.util.UxScreenUtil;
import java.io.File;
import java.util.ArrayList;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusUxIconConfigParser {
    public static final int AND_NUM_FFFF = 65535;
    public static final int ICON_DEFAULT_SIZE_DP = 5000;
    private static final int ICON_DEFAULT_SIZE_DP_FOLD = 5400;
    private static final int ICON_DEFAULT_SIZE_DP_TABLET = 5600;
    private static final float MATERIAL_FOREGROUND_SCALE = 0.8375f;
    private static final String ONEPLUS_ICON_PACK_SETTINGS = "launcher_iconpack";
    private static final String PAC_MAN_ICON_PACK_APK = "OPIconpackPacman.apk";
    private static final String PAC_MAN_ICON_PACK_DIR = OplusEnvironment.getMyCompanyDirectory().getAbsolutePath() + File.separator + IOplusAutoLayoutManager.APP_POLICY_NAME + File.separator + "OPIconpackPacman";
    private static final String PAC_MAN_ICON_PACK_PACKAGE_NAME = "com.oneplus.iconpack.pacman";
    private static final String TAG = "OplusUxIconConfigParser";

    public static void parseConfig(OplusIconConfig config, OplusExtraConfiguration extraConfiguration, Resources res, ArrayList<Integer> commonStyleConfigArray, ArrayList<Integer> specialStyleConfigArray, String[] commonStylePathArray, String[] specialStlytPathArray) {
        int i;
        Path path;
        int themeCustomPos;
        int iconSize;
        ArrayList<Integer> arrayList = commonStyleConfigArray;
        if (extraConfiguration == null) {
            return;
        }
        Long uxIconConfig = Long.valueOf(extraConfiguration.mUxIconConfig);
        if (uxIconConfig.longValue() == -1) {
            config.setEmpty(true);
            return;
        }
        int darkModeIcon = Long.valueOf(uxIconConfig.longValue() >> 61).intValue() & 1;
        config.setEnableDarkModeIcon(darkModeIcon == 1);
        boolean isForeign = (uxIconConfig.intValue() & 15) == 1;
        config.setForeign(isForeign);
        Long uxIconConfig2 = Long.valueOf(uxIconConfig.longValue() >> 4);
        int theme = uxIconConfig2.intValue() & 15;
        Log.i("OplusUXIconLoader", "theme=" + theme + "; uxIconConfig = " + String.valueOf(uxIconConfig2));
        config.setTheme(theme);
        Long valueOf = Long.valueOf(uxIconConfig2.longValue() >> 4);
        Long uxIconConfig3 = valueOf;
        boolean artPlusOn = (valueOf.intValue() & 15) == 1;
        config.setArtPlusOn(artPlusOn);
        int defaultIconSizePx = res.getDimensionPixelSize(201654414);
        if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
            defaultIconSizePx = res.getDimensionPixelSize(201654492);
        } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
            defaultIconSizePx = res.getDimensionPixelSize(201654491);
        } else if (UxScreenUtil.isTabletDevices()) {
            defaultIconSizePx = res.getDimensionPixelSize(201654492);
        }
        int defaultIconSizeDp = getDpFromIconConfigPx(res, defaultIconSizePx);
        config.setIconSize(defaultIconSizeDp);
        config.setForegroundSize(defaultIconSizeDp);
        Path path2 = null;
        int themeCustomPos2 = commonStyleConfigArray.size() - 1;
        if (commonStylePathArray == null) {
            i = 0;
            path = null;
        } else {
            Log.i("OplusUXIconLoader", "themeCustomPos=" + themeCustomPos2 + ";commonStylePathArray size=" + commonStylePathArray.length);
            if (theme == 5) {
                int iconSize2 = Long.valueOf(uxIconConfig3.longValue() >> 8).intValue() & 65535;
                config.setIconSize(iconSize2);
                Path path3 = PathParser.createPathFromPathData(commonStylePathArray[0]);
                path = path3;
                i = 0;
            } else {
                i = 0;
                while (i < commonStyleConfigArray.size()) {
                    if (arrayList.get(i).intValue() != theme || i != themeCustomPos2) {
                        themeCustomPos = themeCustomPos2;
                        if (arrayList.get(i).intValue() != theme || i >= commonStylePathArray.length) {
                            iconSize = defaultIconSizeDp;
                        } else {
                            Long valueOf2 = Long.valueOf(uxIconConfig3.longValue() >> 8);
                            uxIconConfig3 = valueOf2;
                            int iconSize3 = valueOf2.intValue() & 65535;
                            config.setIconSize(iconSize3);
                            if (i == 1) {
                                config.setForegroundSize((int) (defaultIconSizeDp * MATERIAL_FOREGROUND_SCALE));
                                iconSize = defaultIconSizeDp;
                                path2 = OplusRoundRectUtil.getInstance().getPath(new Rect(0, 0, 150, 150), 8.0f);
                                uxIconConfig3 = uxIconConfig3;
                            } else {
                                iconSize = defaultIconSizeDp;
                                Log.i("UXIconPaser", "theme=" + theme + "; pathData =" + commonStylePathArray[i]);
                                path2 = PathParser.createPathFromPathData(commonStylePathArray[i]);
                            }
                        }
                    } else {
                        Long uxIconConfig4 = Long.valueOf(uxIconConfig3.longValue() >> 4);
                        int iconShape = uxIconConfig4.intValue() & 15;
                        config.setIconShape(iconShape);
                        Long uxIconConfig5 = Long.valueOf(uxIconConfig4.longValue() >> 4);
                        themeCustomPos = themeCustomPos2;
                        config.setIconSize(uxIconConfig5.intValue() & 65535);
                        Long valueOf3 = Long.valueOf(uxIconConfig5.longValue() >> 16);
                        uxIconConfig3 = valueOf3;
                        int iconSize4 = valueOf3.intValue() & 65535;
                        config.setForegroundSize(iconSize4);
                        path2 = parseCustomConfig(uxIconConfig3, res, iconShape);
                        iconSize = defaultIconSizeDp;
                    }
                    i++;
                    arrayList = commonStyleConfigArray;
                    themeCustomPos2 = themeCustomPos;
                    defaultIconSizeDp = iconSize;
                }
                path = path2;
            }
        }
        if (i == commonStyleConfigArray.size()) {
            for (int j = 0; j < specialStyleConfigArray.size(); j++) {
                if (specialStyleConfigArray.get(j).intValue() == theme && j < specialStlytPathArray.length) {
                    path = PathParser.createPathFromPathData(specialStlytPathArray[j]);
                }
            }
        }
        Log.i("OplusUXIconLoader", "path = " + path + "; config = " + config.toString());
        if (path == null) {
            path = PathParser.createPathFromPathData(res.getString(201588947));
        }
        config.setShapePath(path);
        config.setEmpty(false);
        config.removeUpdateConfig(1);
    }

    public static int getDpFromIconConfigPx(Resources resources, int px) {
        return float2int(((px * 1.0f) / resources.getDisplayMetrics().density) * 100.0f);
    }

    public static int getPxFromIconConfigDp(Resources resources, int dp) {
        return float2int(resources.getDisplayMetrics().density * ((dp * 1.0f) / 100.0f));
    }

    private static String productIconPack() {
        File file = new File(PAC_MAN_ICON_PACK_DIR + File.separator + PAC_MAN_ICON_PACK_APK);
        if (file.exists()) {
            Log.d(TAG, "productIconPack has exists:" + file.getAbsolutePath());
            return PAC_MAN_ICON_PACK_PACKAGE_NAME;
        }
        return null;
    }

    public static long getDefaultIconConfig(boolean z, Context context, int i) {
        int i2 = 2;
        Resources resources = context.getResources();
        ContentResolver contentResolver = context.getContentResolver();
        String string = Settings.System.getString(contentResolver, ONEPLUS_ICON_PACK_SETTINGS);
        if (TextUtils.isEmpty(string)) {
            string = productIconPack();
        }
        if (!TextUtils.isEmpty(string)) {
            Settings.System.putStringForUser(contentResolver, OplusThemeUtil.ICON_APCK_NAME, string, i);
            i2 = 5;
        }
        long dpFromIconConfigPx = ((0 | (1 & 1)) << 13) | (getDpFromIconConfigPx(resources, resources.getDimensionPixelSize(201654415)) & 4095);
        int dimensionPixelSize = resources.getDimensionPixelSize(201654414);
        if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
            dimensionPixelSize = resources.getDimensionPixelSize(201654492);
        } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
            dimensionPixelSize = resources.getDimensionPixelSize(201654491);
        } else if (UxScreenUtil.isTabletDevices()) {
            dimensionPixelSize = resources.getDimensionPixelSize(201654492);
        }
        long dpFromIconConfigPx2 = ((dpFromIconConfigPx << 16) | (getDpFromIconConfigPx(resources, dimensionPixelSize) & 65535)) << 16;
        int i3 = 5000;
        if (UxScreenUtil.isFoldDisplay() && UxScreenUtil.isWhiteSwan()) {
            i3 = ICON_DEFAULT_SIZE_DP_TABLET;
        } else if (UxScreenUtil.isFoldDisplay() && !UxScreenUtil.isDragonFly()) {
            i3 = ICON_DEFAULT_SIZE_DP_FOLD;
        } else if (UxScreenUtil.isTabletDevices()) {
            i3 = ICON_DEFAULT_SIZE_DP_TABLET;
        }
        long j = ((((((((dpFromIconConfigPx2 | (65535 & i3)) << 4) | 0) << 4) | 0) << 4) | i2) << 4) | (z ? 1L : 0L);
        Log.i("OplusUXIconLoader", "DefaultIconConfig[foreign =" + (z ? 1 : 0) + "; isDarkModeIcon =1; defaultTheme =" + i2 + "]");
        return j;
    }

    private static int float2int(float f) {
        int i = (int) f;
        float ferror = f - i;
        if (Math.abs(ferror) > 0.5d) {
            return i + 1;
        }
        return i;
    }

    private static Path parseCustomConfig(Long uxIconConfig, Resources res, int iconShape) {
        return parseCustomPlusConfig(uxIconConfig, res, iconShape);
    }

    private static Path parseCustomPlusConfig(Long uxIconConfig, Resources res, int iconShape) {
        switch (iconShape) {
            case 0:
                int iconRadiusPx = Long.valueOf(uxIconConfig.longValue() >> 16).intValue() & 4095;
                if (iconRadiusPx > 75) {
                    iconRadiusPx = getPxFromIconConfigDp(res, iconRadiusPx);
                }
                if (iconRadiusPx == 75) {
                    return PathParser.createPathFromPathData(res.getString(201588948));
                }
                return OplusRoundRectUtil.getInstance().getPath(new Rect(0, 0, 150, 150), iconRadiusPx);
            case 1:
                return PathParser.createPathFromPathData(res.getString(201588945));
            case 2:
                return PathParser.createPathFromPathData(res.getString(201588946));
            case 3:
                return PathParser.createPathFromPathData(res.getString(201588947));
            case 4:
                return PathParser.createPathFromPathData(res.getString(201588949));
            default:
                return null;
        }
    }
}
