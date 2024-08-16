package android.graphics;

import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.util.Map;

/* loaded from: classes.dex */
public final class OplusTypefaceInjector {
    public static final String DEFAULT_FONT_CONFIG_FILE = "/system/etc/fonts.xml";
    public static final String FBE_FONT_CONFIG_FILE = "/system_ext/etc/fonts_base.xml";
    public static Typeface OPLUSUI_MEDIUM = null;
    public static Typeface OPLUSUI_VF = null;
    public static final String OPLUS_CUSTOMIZATION_FONTS_PATH = "/system_ext/fonts/";
    public static final String OPLUS_CUSTOMIZATION_FONTS_XML = "/system_ext/etc/fonts_customization.xml";
    public static final String SECOND_FONT_CONFIG_FILE = "/system_ext/etc/fonts_base.xml";
    public static final boolean sIsFBESupport = "file".equals(SystemProperties.get("ro.crypto.type", ""));

    public static boolean isSystemTypeface(Typeface typeface) {
        if (typeface == null) {
            typeface = Typeface.DEFAULT;
        }
        if (typeface != null && Typeface.sSystemFontMap != null && Typeface.sSystemFontMap.containsValue(typeface)) {
            return true;
        }
        return false;
    }

    public static boolean isSystemTypeface(String fontFamily) {
        if (!TextUtils.isEmpty(fontFamily) && Typeface.sSystemFontMap != null && Typeface.sSystemFontMap.containsKey(fontFamily)) {
            return true;
        }
        return false;
    }

    public static Typeface[] getSystemDefaultTypefaces() {
        return Typeface.sDefaults;
    }

    public static Map<String, Typeface> getSystemFontMap() {
        return Typeface.sSystemFontMap;
    }

    public static void dumpSysTypeface() {
        Map<String, Typeface> sSystemFontMap = Typeface.sSystemFontMap;
        if (sSystemFontMap != null) {
            for (Map.Entry<String, Typeface> entry : sSystemFontMap.entrySet()) {
                Log.d("FontUtilsCTI", "System typeface  family = " + entry.getKey() + " : " + entry.getValue());
            }
        }
    }
}
