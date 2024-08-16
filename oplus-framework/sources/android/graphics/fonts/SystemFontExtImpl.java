package android.graphics.fonts;

import android.graphics.OplusTypefaceInjector;
import android.graphics.fonts.ISystemFontExt;
import android.text.FontConfig;
import com.oplus.util.OplusFontUtils;

/* loaded from: classes.dex */
public class SystemFontExtImpl implements ISystemFontExt {

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements ISystemFontExt.IStaticExt {
        private StaticExtImpl() {
        }

        public static StaticExtImpl getInstance(Object obj) {
            return LazyHolder.INSTANCE;
        }

        /* loaded from: classes.dex */
        private static class LazyHolder {
            private static final StaticExtImpl INSTANCE = new StaticExtImpl();

            private LazyHolder() {
            }
        }

        public FontConfig.NamedFamilyList getIndividualFontFamily() {
            return OplusFontUtils.createCustomizeFontFamily();
        }

        public FontConfig apendIndividualFontFamily(FontConfig fontconfig) {
            if (fontconfig != null && fontconfig.getNamedFamilyLists() != null) {
                OplusFontUtils.apendIndividualFontFamily(fontconfig.getNamedFamilyLists());
            }
            return fontconfig;
        }

        public String getSystemFontConfig(String fonConfig) {
            return "/system_ext/etc/fonts_base.xml";
        }

        public String getOemCustomizationConfigXml(String oemXml) {
            return OplusTypefaceInjector.OPLUS_CUSTOMIZATION_FONTS_XML;
        }

        public String getOemCustomizationFilePath(String oemPath) {
            return OplusTypefaceInjector.OPLUS_CUSTOMIZATION_FONTS_PATH;
        }

        public boolean isFlipfontUsed() {
            return OplusFontUtils.isFlipFontUsed;
        }
    }
}
