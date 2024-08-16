package android.app;

import android.os.SystemProperties;
import com.oplus.os.OplusEnvironment;
import java.io.File;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class OplusUxIconConstants {
    public static final boolean DEBUG_UX_ICON = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final boolean DEBUG_UX_ICON_TRACE = true;
    public static final float DEFAULT_RELATIVE_BRIGHTNESS = 0.84f;

    /* loaded from: classes.dex */
    public static class IconTheme {
        public static final int ART_PLUS_BIT_LENGTH = 4;
        public static final int DARKMODE_ICON_BIT_LENGTH = 1;
        public static final int DARKMODE_ICON_TRANSLATE_BIT_LENGTH = 61;
        public static final int FOREGROUND_SIZE_BIT_LENGTH = 16;
        public static final int FOREIGN_BIT_LENGTH = 4;
        public static final int ICON_RADIUS_BIT_LENGTH = 12;
        public static final int ICON_RADIUS_PX_BIT_LENGTH = 1;
        public static final int ICON_SHAPE_BIT_LENGTH = 4;
        public static final int ICON_SHAPE_CUSTOM_SQUARE = 0;
        public static final int ICON_SHAPE_DESIGNED_LEAF = 2;
        public static final int ICON_SHAPE_DESIGNED_OCTAGON = 1;
        public static final int ICON_SHAPE_DESIGNED_PECULIAR = 4;
        public static final int ICON_SHAPE_DESIGNED_STICKER = 3;
        public static final int ICON_SIZE_BIT_LENGTH = 16;
        public static final String OPLUS_OFF_UXICON_META_DATA = "com.oplus.off_uxicon";
        public static final String OPLUS_UXIOCN_META_DATA = "com.oplus.support_uxonline";
        public static final int PREFIX_MATERIAL_POS = 2;
        public static final int PREFIX_PEBBLE_POS = 3;
        public static final int PREFIX_RECTANGLE_BG_POS = 1;
        public static final int PREFIX_RECTANGLE_FG_POS = 0;
        public static final int THEME_BIT_LENGTH = 4;
        public static final int THEME_CUSTOM = 3;
        public static final int THEME_ICON_PACK = 5;
        public static final int THEME_MATERIAL = 1;
        public static final int THEME_MATERIAL_POS = 1;
        public static final int THEME_MATERIAL_RADIUS_PX = 8;
        public static final int THEME_PEBBLE = 4;
        public static final int THEME_PEBBLE_POS = 2;
        public static final int THEME_RECTANGLE = 2;
        public static final int THEME_RECTANGLE_POS = 0;
    }

    /* loaded from: classes.dex */
    public static class SystemProperty {
        public static final String FEATURE_UX_ICON_DISABLE = "oplus.uxicons.disable.uxicons";
        public static final String KEY_THEME_FLAG = "persist.sys.themeflag";
        public static final String KEY_UX_ICON_CONFIG = "key_ux_icon_config";
        public static final String KEY_UX_ICON_THEME_FLAG = "persist.sys.themeflag.uxicon";
    }

    /* loaded from: classes.dex */
    public static class IconLoader {
        public static final String BASE_SYSTEM_DEFAULT_THEME_FILE_PATH = "/system/media/theme/default/";
        public static final String BASE_UX_ICONS_FILE_PATH = "/data/oplus/uxicons/";
        public static final String COM_ANDROID_CONTACTS = "com.android.contacts";
        public static final String COM_HEYTAP_MATKET = "com.heytap.market";
        public static final String DEFAULT_BACKGROUND_COLOR = "#FFFBFBFB";
        public static final String DIALER_PREFIX = "dialer_";
        public static final String FILE_SEPARATOR = "/";
        public static final int ICON_SIZE_THRESHOLD = 37;
        public static final float MATERIAL_FOREGROUND_SCALE = 1.25f;
        public static final int PIXEL_ALPHA_THRESHOLD = 220;
        public static final float PIXEL_ROUNDING_UP = 0.5f;
        public static final int PIXEL_SAMPLE = 4;
        public static final int PIXEL_THRESHOLD = 6;
        public static final String PNG_REG = ".png";
        public static final int TRANSPARENT_ICON_FG_SIZE_DP = 38;
        public static final String BASE_PRODUCT_DEFAULT_THEME_FILE_PATH = getMyProductDirectory() + "/media/theme/default/";
        public static final String MY_PRODUCT_ROOT_PATH = "" + OplusEnvironment.getMyProductDirectory().getAbsolutePath() + "";
        public static final String MY_COUNTRY_ROOT_PATH = "" + OplusEnvironment.getMyRegionDirectory().getAbsolutePath() + "";
        public static final String MY_CARRIER_ROOT_PATH = "" + OplusEnvironment.getMyCarrierDirectory().getAbsolutePath() + "";
        public static final String MY_STOCK_ROOT_PATH = "" + OplusEnvironment.getMyStockDirectory().getAbsolutePath() + "";
        public static final String MY_COUNTRY_DEFAULT_THEME_FILE_PATH = getMyCountryDirectory() + "/media/theme/uxicons/";
        public static final String MY_CARRIER_DEFAULT_THEME_FILE_PATH = getMyCarrierDirectory() + "/media/theme/uxicons/";
        public static final String MY_STOCK_DEFAULT_THEME_FILE_PATH = getMyStockDirectory() + "/media/theme/uxicons/";

        static String getMyProductDirectory() {
            try {
                Method method = OplusEnvironment.class.getMethod("getMyProductDirectory", new Class[0]);
                method.setAccessible(true);
                Object product = method.invoke(null, new Object[0]);
                if (product != null) {
                    return ((File) product).getAbsolutePath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MY_PRODUCT_ROOT_PATH;
        }

        static String getMyCountryDirectory() {
            try {
                Method method = OplusEnvironment.class.getMethod("getMyCountryDirectory", new Class[0]);
                method.setAccessible(true);
                Object product = method.invoke(null, new Object[0]);
                if (product != null) {
                    return ((File) product).getAbsolutePath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MY_COUNTRY_ROOT_PATH;
        }

        static String getMyCarrierDirectory() {
            try {
                Method method = OplusEnvironment.class.getMethod("getMyCarrierDirectory", new Class[0]);
                method.setAccessible(true);
                Object product = method.invoke(null, new Object[0]);
                if (product != null) {
                    return ((File) product).getAbsolutePath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MY_CARRIER_ROOT_PATH;
        }

        static String getMyStockDirectory() {
            try {
                Method method = OplusEnvironment.class.getMethod("getMyStockDirectory", new Class[0]);
                method.setAccessible(true);
                Object product = method.invoke(null, new Object[0]);
                if (product != null) {
                    return ((File) product).getAbsolutePath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return MY_STOCK_ROOT_PATH;
        }

        public static String getDensityName(int density) {
            if (density > 480) {
                return "xxxhdpi";
            }
            if (density > 320) {
                return "xxhdpi";
            }
            return "xhdpi";
        }
    }
}
