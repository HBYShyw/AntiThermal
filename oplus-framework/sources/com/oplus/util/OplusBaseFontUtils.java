package com.oplus.util;

import android.graphics.OplusTypefaceInjector;
import android.graphics.Typeface;
import android.os.SystemProperties;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class OplusBaseFontUtils {
    public static String DATA_FONT_DIRECTORY = null;
    protected static final String DATA_FONT_DIRECTORY_5D0 = "/data/system/font/";
    public static final String DATA_FONT_DIRECTORY_6D0 = "/data/format_unclear/font/";
    protected static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    protected static final List<String> DEFAULT_OPLUS_FONT_SYSTEM_LINKS;
    protected static final String FLIPED_CUSTOMIZED_FONT_NAME = "Customized-Regular.ttf";
    protected static final String FLIPED_OPLUSOS_FONT_NAME = "ColorOS-Regular.ttf";
    public static boolean FLIP_APP_ALL_FONTS = false;
    protected static final int FLIP_FONT_FLAG_MAX = 10001;
    public static final List<String> FLITER_CTS_APP_PKG_LIST;
    public static final List<String> FLITER_NOT_REPLACEFONT_APP_PKG_LIST;
    protected static final FontLinkInfo[] FONTINFOARRAY_ROM6D0;
    public static final int FONT_VARIATION_DEFAULT = 550;
    public static final int FONT_VARIATION_END = 1000;
    public static final int FONT_VARIATION_NIGHT = -50;
    public static final String FONT_VARIATION_SETTINGS = "font_variation_settings";
    public static final int FONT_VARIATION_START = 100;
    public static final int FONT_VARIATION_STEP = 10;
    public static final String FONT_VARIATION_WEIGHT = "'wght' ";
    public static final int FONT_WEIGHT_BOLD = 700;
    public static final Map<Integer, Integer> FONT_WEIGHT_CAST_WGHT;
    public static final int FONT_WEIGHT_MEDIUM = 500;
    public static final int FONT_WEIGHT_NORMAL = 400;
    protected static final int INVALID_FLIP_FONT = -1;
    protected static final int LINK_TARGET_FLIPFONT = 2;
    protected static final int LINK_TARGET_SYSTEM = 1;
    public static final String ONEPLUS_SANS_VARIATION_FONT = "op-sans-en";
    public static final String OPLUS_SANS_VARIATION_FONT = "sys-sans-en";
    public static final String SECOND_FONT_CONFIG_FILE = "/system_ext/etc/fonts_base.xml";
    public static final int STATUS_DISABLE = 0;
    public static final int STATUS_ONEPLUS_ENABLE = 3;
    public static final int STATUS_OPLUS_AUTO = 2;
    public static final int STATUS_OPLUS_ENABLE = 1;
    protected static final List<String> SUPPORT_FONT_VARIATION_LIST;
    protected static final List<String> SUPPORT_MEDIUM_FONT_LANGUAGE_LIST;
    protected static final String SYSTEM_FONT_DIRECTORY = "/system/fonts/";
    protected static final String TAG = "FontUtils";
    public static boolean isCurrentLanguageSupportMediumFont;
    public static boolean isCurrentLanguageSupportVariationFont;
    public static boolean isFlipFontUsed;
    public static boolean isSearched;
    public static int mFontVariation;
    public static int mFontVariationAdaption;
    public static String mFontVariationSettings;
    public static int mFontVariationStatus;
    public static String mPackageName;
    protected static Typeface[] sCurrentTypefaces;
    protected static List<Typeface> sCurrentTypefacesArray;
    public static int sFlipFont;
    protected static List<FontLinkInfo> sFontLinkInfos;
    public static boolean sIsCheckCTS;
    public static boolean sIsIme;
    public static final boolean sIsROM6d0FlipFont;
    public static String sLastFontVariationSettings;
    protected static boolean sNeedReplaceAllTypefaceApp;
    protected static Typeface sOneplusVf;
    public static ConcurrentHashMap<String, Typeface> sOplusVariationCacheMap;
    static long sPreOSansSettings;
    public static boolean sReplaceFont;
    public static int sUserId;

    static {
        boolean z = OplusTypefaceInjector.sIsFBESupport;
        sIsROM6d0FlipFont = z;
        FLITER_CTS_APP_PKG_LIST = new ArrayList(Arrays.asList("android.theme.app", "android.graphics.cts", "android.widget.cts", "android.uirendering.cts", "android.text.cts"));
        FLITER_NOT_REPLACEFONT_APP_PKG_LIST = new ArrayList(Arrays.asList("com.eterno"));
        DATA_FONT_DIRECTORY = z ? DATA_FONT_DIRECTORY_6D0 : DATA_FONT_DIRECTORY_5D0;
        sCurrentTypefaces = null;
        sCurrentTypefacesArray = null;
        isFlipFontUsed = false;
        FLIP_APP_ALL_FONTS = false;
        sIsCheckCTS = false;
        sReplaceFont = true;
        sNeedReplaceAllTypefaceApp = false;
        DEFAULT_OPLUS_FONT_SYSTEM_LINKS = new ArrayList(Arrays.asList("/system/fonts/SysFont-Regular.ttf", "/system/fonts/SysFont-Myanmar.ttf"));
        FontLinkInfo[] fontLinkInfoArr = {new FontLinkInfo(DATA_FONT_DIRECTORY + "OplusOSUI-Regular.ttf", "/system/fonts/Roboto-Regular.ttf"), new FontLinkInfo(DATA_FONT_DIRECTORY + "OplusOSUI-Static-Regualr.ttf", "/system/fonts/RobotoStatic-Regular.ttf")};
        FONTINFOARRAY_ROM6D0 = fontLinkInfoArr;
        sFontLinkInfos = new ArrayList(Arrays.asList(fontLinkInfoArr));
        SUPPORT_MEDIUM_FONT_LANGUAGE_LIST = new ArrayList(Arrays.asList("en", "zh", "ja", "ko", "fr", "it", "de", "sv", "nl", "es", "ru", "kk"));
        SUPPORT_FONT_VARIATION_LIST = new ArrayList(Arrays.asList("en", "zh"));
        sOplusVariationCacheMap = new ConcurrentHashMap<>();
        mFontVariationSettings = "'wght' 550";
        sLastFontVariationSettings = "'wght' 550";
        FONT_WEIGHT_CAST_WGHT = Map.of(100, 100, 200, 200, 300, 300, 400, Integer.valueOf(FONT_VARIATION_DEFAULT), 500, 700, 600, 900, 700, 900, 800, 900, 900, 1000, 1000, 1000);
        mFontVariationAdaption = 0;
        mFontVariation = 0;
        mFontVariationStatus = 0;
        sPreOSansSettings = 0L;
    }

    /* loaded from: classes.dex */
    protected static class FontLinkInfo {
        String mDataFontName;
        String mSystemFontName;

        FontLinkInfo(String dataFontName, String robotoFontName) {
            this.mDataFontName = dataFontName;
            this.mSystemFontName = robotoFontName;
        }
    }

    public static void deleteFontLink(String pkgName) {
    }

    public static void createFontLink(String pkgName) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void logd(String content) {
        if (DEBUG) {
            Log.d(TAG, content);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void loge(String content, Throwable e) {
        if (DEBUG) {
            Log.e(TAG, content + ":" + e.getMessage(), e);
        }
    }
}
