package com.oplus.util;

import android.app.OplusActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.OplusBaseConfiguration;
import android.graphics.Canvas;
import android.graphics.IPaintWrapper;
import android.graphics.ITypefaceExt;
import android.graphics.OplusTypefaceInjector;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.graphics.fonts.FontVariationAxis;
import android.os.LocaleList;
import android.os.Process;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.text.FontConfig;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import com.oplus.util.OplusBaseFontUtils;
import com.oplus.view.OplusWindowUtils;
import com.oplus.wrapper.content.pm.PackageInstaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import oplus.content.res.OplusExtraConfiguration;

/* loaded from: classes.dex */
public class OplusFontUtils extends OplusBaseFontUtils {
    public static final String CUSTOM_FONT_FAMILY_NAME = "individual-font";
    public static final String CUSTOM_FONT_POSTSCRIPTNAME = "Customized-Regular";
    static Typeface sIndividualTypeface = null;

    public static void setFlipFont(Configuration configuration) {
        OplusBaseConfiguration baseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, configuration);
        if (baseConfiguration != null && baseConfiguration.mOplusExtraConfiguration != null && sFlipFont != baseConfiguration.mOplusExtraConfiguration.mFlipFont) {
            logd("SetFlipFont -- sFlipFont=" + sFlipFont + ", sIsROM6d0FlipFont= " + sIsROM6d0FlipFont + " --> mFlipFont=" + baseConfiguration.mOplusExtraConfiguration.mFlipFont);
            sFlipFont = baseConfiguration.mOplusExtraConfiguration.mFlipFont;
            checkAndCorrectFlipFontLink(false);
            doUpdateTypeface(configuration);
        }
    }

    private static void renameFontFileName() {
        String fontFilePath = getCustomizedFontFile(true);
        File oldFile = new File(fontFilePath);
        if (oldFile.exists() && !oldFile.renameTo(new File(getCustomizedFontFile(false)))) {
            Log.w("FontUtils", "Failed to rename to new font file!");
        }
    }

    public static void setFlipFontWhenUserChange(Configuration configuration) {
        OplusBaseConfiguration baseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, configuration);
        if (baseConfiguration != null && baseConfiguration.mOplusExtraConfiguration.mFontUserId >= 0) {
            int currentUserId = baseConfiguration.mOplusExtraConfiguration.mFontUserId;
            logd("setFlipFontWhenUserChange -- mUserId in mExtraConfiguration = " + currentUserId + ", sUserId = " + sUserId);
            sFlipFont = baseConfiguration.mOplusExtraConfiguration.mFlipFont;
            sUserId = currentUserId;
            checkAndCorrectFlipFontLink(true);
            doUpdateTypeface(configuration);
        }
    }

    public static void updateTypefaceInCurrProcess(Configuration configuration, int changes) {
        boolean changed = updateThemeStoreFontConfig(configuration, changes);
        if (changed | updateOSansConfig(configuration, changes)) {
            freeCaches();
            if (sIsIme && mFontVariationStatus != 2) {
                Log.d("FontUtils", "kill:IME " + mPackageName + " " + configuration);
                Process.killProcess(Process.myPid());
            }
        }
    }

    private static boolean updateThemeStoreFontConfig(Configuration configuration, int changes) {
        OplusBaseConfiguration baseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, configuration);
        if (baseConfiguration == null) {
            return false;
        }
        if (baseConfiguration.mOplusExtraConfiguration.mFontUserId == -1) {
            logd("invalid mFontUserId in extraConfiguration -1, abandon");
            return false;
        }
        if (sFlipFont == baseConfiguration.mOplusExtraConfiguration.mFlipFont && sUserId == baseConfiguration.mOplusExtraConfiguration.mFontUserId) {
            return false;
        }
        sFlipFont = baseConfiguration.mOplusExtraConfiguration.mFlipFont;
        sUserId = baseConfiguration.mOplusExtraConfiguration.mFontUserId;
        doUpdateTypeface(configuration);
        logd("<updateTypefaceInCurrProcess> myTid = " + Process.myTid() + " , fliped = " + isFlipFontUsed + ", myUid = " + Process.myUid() + ", myPid = " + Process.myPid() + ", mFontUserId = " + baseConfiguration.mOplusExtraConfiguration.mFontUserId);
        return true;
    }

    public static void setAppTypeFace(String sAppName) {
        if (FLITER_CTS_APP_PKG_LIST.contains(sAppName)) {
            sIsCheckCTS = true;
        }
        if (FLITER_NOT_REPLACEFONT_APP_PKG_LIST.contains(sAppName)) {
            sReplaceFont = false;
        }
    }

    public static void setNeedReplaceAllTypefaceApp(boolean flag) {
        sNeedReplaceAllTypefaceApp = flag;
    }

    public static boolean getNeedReplaceAllTypefaceApp() {
        return sNeedReplaceAllTypefaceApp;
    }

    public static Typeface flipTypeface(ITypefaceExt typefaceExt, Paint paint) {
        Typeface tf = flipTypeface(typefaceExt);
        if (shouldReplaceToOSans(typefaceExt)) {
            Typeface tf2 = replaceTypefaceWithVariation(typefaceExt.getTypeface() == null ? Typeface.DEFAULT : typefaceExt.getTypeface(), paint);
            return tf2;
        }
        return tf;
    }

    public static Typeface flipTypeface(ITypefaceExt typefaceExt) {
        Typeface typeface = null;
        if (typefaceExt != null) {
            typeface = typefaceExt.getTypeface();
        }
        if (isFlipFontUsed && sReplaceFont && sCurrentTypefaces != null && (typeface == null || ((typefaceExt != null && typefaceExt.isSystemTypeface()) || OplusTypefaceInjector.isSystemTypeface(typeface)))) {
            int style = typeface == null ? 0 : typeface.getStyle();
            return sCurrentTypefaces[style];
        }
        if (!isFlipFontUsed && typeface != null && sCurrentTypefacesArray != null && sCurrentTypefacesArray.contains(typeface)) {
            return defaultFromStyle(typeface.getStyle());
        }
        if (!isFlipFontUsed && typeface != null && OplusTypefaceInjector.OPLUSUI_MEDIUM != null && typeface.equals(OplusTypefaceInjector.OPLUSUI_MEDIUM) && !isCurrentLanguageSupportMediumFont) {
            return Typeface.DEFAULT_BOLD;
        }
        return typeface;
    }

    public static void replaceFakeBoldToColorMedium(TextView textView, ITypefaceExt typefaceExt, int style) {
        if (textView == null) {
            return;
        }
        Typeface typeface = null;
        if (typefaceExt != null) {
            typeface = typefaceExt.getTypeface();
        }
        if (!sIsCheckCTS && mFontVariationStatus != 0 && ((style == 1 || style == 3) && ((typeface == null || ((typefaceExt != null && typefaceExt.isSystemTypeface()) || Typeface.SANS_SERIF.equals(typeface) || (OplusTypefaceInjector.OPLUSUI_MEDIUM != null && typeface.equals(OplusTypefaceInjector.OPLUSUI_MEDIUM)))) && isCurrentLanguageSupportMediumFont))) {
            textView.setTypeface(OplusTypefaceInjector.OPLUSUI_MEDIUM, style == 3 ? 2 : 0);
        } else {
            textView.setTypeface(typeface, style);
        }
    }

    public static Typeface create(Typeface typeface, String familyName, int style) {
        return Typeface.create(typeface, style);
    }

    public static Typeface defaultFromStyle(int style) {
        Typeface[] systemDefaults = OplusTypefaceInjector.getSystemDefaultTypefaces();
        if (systemDefaults != null && style < systemDefaults.length && style > -1) {
            return systemDefaults[style];
        }
        return null;
    }

    private static void checkAndCorrectFlipFontLink(boolean userChange) {
        File flipedFontFile = new File(getCustomizedFontFile(false));
        if (flipedFontFile.exists()) {
            isFlipFontUsed = relinkDataFontToTarget(2, flipedFontFile);
            oplus.content.res.OplusFontUtils.isFlipFontUsed = isFlipFontUsed;
        } else {
            logd("checkAndCorreectFlipFontLink flipedFontFile NOT exists");
            isFlipFontUsed = false;
            oplus.content.res.OplusFontUtils.isFlipFontUsed = isFlipFontUsed;
        }
        if (!isFlipFontUsed) {
            relinkDataFontToTarget(1, flipedFontFile);
        }
    }

    private static boolean relinkDataFontToTarget(int target, File customFontFile) {
        for (int i = 0; i < sFontLinkInfos.size(); i++) {
            OplusBaseFontUtils.FontLinkInfo fontLinkInfo = sFontLinkInfos.get(i);
            File dataFontLinkFile = new File(fontLinkInfo.mDataFontName);
            String targetFont = "";
            if (target == 1) {
                try {
                    try {
                        targetFont = fontLinkInfo.mSystemFontName;
                    } catch (IllegalArgumentException e) {
                        loge("SELinux policy update check and correct flipfont", e);
                        return false;
                    }
                } catch (ErrnoException e2) {
                    loge("Could not update selinux policy check and correct flipfont: " + fontLinkInfo.mDataFontName, e2);
                    return false;
                }
            } else if (target == 2) {
                targetFont = getCustomizedFontFile(false);
                if (customFontFile.exists()) {
                    try {
                        new Font.Builder(customFontFile).build();
                        Log.d("FontUtils", "invalidate font file check  pass ! " + customFontFile.getAbsolutePath());
                    } catch (IOException ioException) {
                        Log.d("FontUtils", "invalidate font file check  failed ioException " + customFontFile.getAbsolutePath() + " , " + ioException.getMessage());
                        customFontFile.delete();
                    } catch (IllegalArgumentException illegalArgumentException) {
                        Log.d("FontUtils", "invalidate font file check  failed illegalArgumentException " + customFontFile.getAbsolutePath() + " , " + illegalArgumentException.getMessage());
                        customFontFile.delete();
                    }
                }
            }
            if (!dataFontLinkFile.exists()) {
                dataFontLinkFile.delete();
                logd("relink font targetFont = " + targetFont + ", " + fontLinkInfo.mDataFontName);
                Os.symlink(targetFont, fontLinkInfo.mDataFontName);
            } else if (dataFontLinkFile.exists() && !Os.readlink(fontLinkInfo.mDataFontName).equals(targetFont)) {
                dataFontLinkFile.delete();
                Os.symlink(targetFont, fontLinkInfo.mDataFontName);
            }
        }
        return true;
    }

    public static void handleFactoryReset() {
        if (sIsROM6d0FlipFont) {
            try {
                File flipFontFile = new File(getCustomizedFontFile(false));
                if (flipFontFile.exists()) {
                    flipFontFile.delete();
                }
            } catch (Exception e) {
                loge("Failed handleFactoryReset", e);
            }
        }
    }

    public static boolean isCurrentLanguageSupportMediumFont() {
        Locale currentLcale = Locale.getDefault();
        if (currentLcale != null && SUPPORT_MEDIUM_FONT_LANGUAGE_LIST.contains(currentLcale.getLanguage())) {
            return true;
        }
        return false;
    }

    public static boolean isCurrentLanguageSupportVariationFont() {
        Locale currentLcale = Locale.getDefault();
        if (currentLcale != null && SUPPORT_FONT_VARIATION_LIST.contains(currentLcale.getLanguage())) {
            return true;
        }
        return false;
    }

    private static void freeCaches() {
        Canvas.freeCaches();
        Canvas.freeTextLayoutCaches();
    }

    private static String getCustomizedFontFile(boolean oldFile) {
        String userIdStr = sUserId + "/";
        if (sUserId == 0) {
            userIdStr = "";
        }
        String result = DATA_FONT_DIRECTORY + userIdStr + (oldFile ? "ColorOS-Regular.ttf" : "Customized-Regular.ttf");
        if (oldFile) {
            Log.d("FontUtils", "getCustomizedFontFile with name " + result);
        }
        return result;
    }

    public static Typeface replaceTypefaceWithVariation(Typeface typeface, Paint paint) {
        String fv = paint.getFontVariationSettings();
        if (fv == null || sLastFontVariationSettings.equals(fv)) {
            IPaintWrapper wrapper = paint.getWrapper();
            wrapper.setFontVariationSettings(mFontVariationSettings);
            fv = mFontVariationSettings;
        }
        return getVariationFontFromCache(typeface, fv);
    }

    public static Typeface createTypefaceWithVariation(Typeface typeface, String fontVariationSettings) {
        Typeface typeface2 = Typeface.create(getReplaceTypeface(), typeface.getWeight(), typeface.isItalic());
        FontVariationAxis[] axesf = FontVariationAxis.fromFontVariationSettings(fontVariationSettings);
        ArrayList<FontVariationAxis> filteredAxes = new ArrayList<>();
        for (FontVariationAxis axis : axesf != null ? axesf : new FontVariationAxis[0]) {
            if (typeface2.isSupportedAxes(axis.getOpenTypeTagValue())) {
                filteredAxes.add(axis);
            }
        }
        if (!filteredAxes.isEmpty()) {
            Typeface typeface3 = Typeface.createFromTypefaceWithVariation(typeface2, filteredAxes);
            if (typeface3.mTypefaceExt != null) {
                typeface3.mTypefaceExt.setSystemTypeface(true);
            }
            return typeface3;
        }
        return typeface2;
    }

    private static boolean isOSansChanged(OplusExtraConfiguration extraCfg, int changes) {
        if (extraCfg == null) {
            return false;
        }
        boolean checkType = (16777216 & changes) != 0;
        boolean settingsChanged = sPreOSansSettings != ((long) extraCfg.mFontVariationSettings);
        return checkType && settingsChanged;
    }

    public static boolean updateOSansConfig(Configuration config, int changes) {
        boolean localeChanged = (changes & 4) != 0;
        if (localeChanged) {
            updateLanguageLocale(config);
        }
        OplusBaseConfiguration baseCfg = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, config);
        if (baseCfg == null) {
            return false;
        }
        OplusExtraConfiguration extraCfg = baseCfg.mOplusExtraConfiguration;
        if (!isOSansChanged(extraCfg, changes)) {
            return false;
        }
        mFontVariation = extraCfg.mFontVariationSettings & 4095;
        mFontVariationStatus = (extraCfg.mFontVariationSettings & PackageInstaller.SessionParamsWrapper.COMPILE_MODE_MASK) >> 12;
        updateFontVariationRes();
        sPreOSansSettings = extraCfg.mFontVariationSettings;
        return true;
    }

    public static void updateLanguageLocale(Configuration config) {
        isCurrentLanguageSupportVariationFont = isCurrentLanguageSupportVariationFont();
        isCurrentLanguageSupportMediumFont = isCurrentLanguageSupportMediumFont();
    }

    public static void initVariationFontVariable(Context context) {
        mPackageName = context.getPackageName();
        updateFontVariationRes();
    }

    private static void updateFontVariationRes() {
        String newFv;
        switch (mFontVariationStatus) {
            case 0:
            case 3:
                newFv = convertIntToString(OplusBaseFontUtils.FONT_VARIATION_DEFAULT);
                break;
            case 1:
                newFv = convertIntToString(mFontVariation);
                break;
            case 2:
                if (mFontVariationAdaption == 0 && !isSearched && mPackageName != null) {
                    mFontVariationAdaption = getAdaptionValue(mPackageName);
                    isSearched = true;
                }
                if (mFontVariationAdaption != 0) {
                    newFv = convertIntToString(mFontVariationAdaption);
                    break;
                } else {
                    newFv = convertIntToString(mFontVariation);
                    break;
                }
                break;
            default:
                newFv = mFontVariationSettings;
                break;
        }
        if (!newFv.equals(mFontVariationSettings)) {
            sLastFontVariationSettings = mFontVariationSettings;
            mFontVariationSettings = newFv;
        }
        if (OplusTypefaceInjector.OPLUSUI_VF == null || OplusTypefaceInjector.OPLUSUI_MEDIUM == null || sOneplusVf == null) {
            OplusTypefaceInjector.OPLUSUI_VF = OplusTypefaceInjector.getSystemFontMap().get(OplusBaseFontUtils.OPLUS_SANS_VARIATION_FONT);
            OplusTypefaceInjector.OPLUSUI_MEDIUM = OplusTypefaceInjector.getSystemFontMap().get("sans-serif-medium");
            sOneplusVf = OplusTypefaceInjector.getSystemFontMap().get(OplusBaseFontUtils.ONEPLUS_SANS_VARIATION_FONT);
        }
        logd("updateFontVariationConfiguration: mFontVariation = " + mFontVariation + " mFontVariationStatus = " + mFontVariationStatus + " mPackageName = " + mPackageName + " mFontVariationAdaption = " + mFontVariationAdaption);
    }

    public static String convertIntToString(int value) {
        return OplusBaseFontUtils.FONT_VARIATION_WEIGHT + value;
    }

    public static int convertStringToInt(String fontVariationSettings) {
        if (TextUtils.isEmpty(fontVariationSettings)) {
            return 0;
        }
        String[] radius = fontVariationSettings.split(" ");
        if (radius.length == 2) {
            return Integer.parseInt(radius[1]);
        }
        return 0;
    }

    public static boolean shouldReplaceToOSans(ITypefaceExt typefaceExt) {
        return OplusTypefaceInjector.OPLUSUI_VF != null && mFontVariationStatus != 0 && isCurrentLanguageSupportVariationFont && !isFlipFontUsed && typefaceExt.isSystemTypeface() && !sIsCheckCTS && !(sReplaceFont ^ true);
    }

    private static Typeface getVariationFontFromCache(Typeface typeface, String FontVariationSettings) {
        int fv = convertStringToInt(FontVariationSettings);
        int fv2 = (fv / 10) * 10;
        int weight = typeface.getWeight();
        Integer wght = FONT_WEIGHT_CAST_WGHT.get(Integer.valueOf(weight));
        int fv3 = fv2 + (wght == null ? weight - 550 : wght.intValue() - 550);
        if (fv3 >= 1000) {
            fv3 = 1000;
        }
        if (fv3 <= 100) {
            fv3 = 100;
        }
        String key = createTypefaceKey(getReplaceTypefaceName(), fv3, typeface.getStyle(), typeface.getWeight());
        Typeface newTypeface = sOplusVariationCacheMap.get(key);
        if (newTypeface == null) {
            Typeface newTypeface2 = createTypefaceWithVariation(typeface, convertIntToString(fv3));
            sOplusVariationCacheMap.put(key, newTypeface2);
            return newTypeface2;
        }
        return newTypeface;
    }

    private static String getReplaceTypefaceName() {
        if ((mFontVariationStatus == 1) | (mFontVariationStatus == 2)) {
            return OplusBaseFontUtils.OPLUS_SANS_VARIATION_FONT;
        }
        if (mFontVariationStatus == 3) {
            return OplusBaseFontUtils.ONEPLUS_SANS_VARIATION_FONT;
        }
        return "DEFAULT";
    }

    private static Typeface getReplaceTypeface() {
        if (mFontVariationStatus == 1 || mFontVariationStatus == 2) {
            return OplusTypefaceInjector.OPLUSUI_VF;
        }
        if (mFontVariationStatus == 3) {
            return sOneplusVf;
        }
        return Typeface.DEFAULT;
    }

    public static String createTypefaceKey(String name, int fv, int style, int weight) {
        return name + "-" + Integer.toString(fv) + "-" + Integer.toString(style) + "-" + Integer.toString(weight);
    }

    public static void updateConfigurationInUIMode(Context context, Configuration config, int userId) {
        boolean isNightMode = (config.uiMode & 32) != 0;
        ContentResolver contentResolver = context.getContentResolver();
        int i = OplusBaseFontUtils.FONT_VARIATION_DEFAULT;
        int fontVariationSettings = Settings.System.getIntForUser(contentResolver, OplusBaseFontUtils.FONT_VARIATION_SETTINGS, OplusBaseFontUtils.FONT_VARIATION_DEFAULT, userId);
        int vectorDrawableConfig = (-65536) & fontVariationSettings;
        int variation = fontVariationSettings & 4095;
        int status = (fontVariationSettings & PackageInstaller.SessionParamsWrapper.COMPILE_MODE_MASK) >> 12;
        if (status == 2) {
            if (isNightMode) {
                i = 500;
            }
            variation = i;
        }
        int status2 = (status << 12) & PackageInstaller.SessionParamsWrapper.COMPILE_MODE_MASK;
        OplusBaseConfiguration baseConfiguration = (OplusBaseConfiguration) OplusTypeCastingHelper.typeCasting(OplusBaseConfiguration.class, config);
        logd("updateConfigurationInUIMode:  isNightMode=" + isNightMode + " status=" + status2 + " variation=" + variation);
        int fontConfig = variation | status2;
        baseConfiguration.mOplusExtraConfiguration.mFontVariationSettings = vectorDrawableConfig | fontConfig;
    }

    public static void setIMEFlag(boolean isIme) {
        if (mPackageName != null && OplusWindowUtils.PACKAGE_TALKBACK.equals(mPackageName)) {
            sIsIme = false;
        } else {
            sIsIme = isIme;
        }
    }

    public static void initFontUtil() {
        renameFontFileName();
        File fontFile = new File(getCustomizedFontFile(false));
        sFlipFont = -1;
        if (fontFile.exists()) {
            Random randomizer = new Random(System.currentTimeMillis());
            sFlipFont = randomizer.nextInt((10000 - 0) + 1) + 0;
            isFlipFontUsed = true;
            oplus.content.res.OplusFontUtils.isFlipFontUsed = isFlipFontUsed;
        }
        sUserId = Process.myUserHandle().hashCode();
        checkAndCorrectFlipFontLink(false);
    }

    public static void apendIndividualFontFamily(List<FontConfig.NamedFamilyList> xmlFamilies) {
        if (isFlipFontUsed && xmlFamilies != null) {
            xmlFamilies.add(createCustomizeFontFamily());
        }
    }

    public static FontConfig.NamedFamilyList createCustomizeFontFamily() {
        FontConfig.FontFamily family = new FontConfig.FontFamily(Arrays.asList(new FontConfig.Font(new File(getCustomizedFontFile(false)), (File) null, CUSTOM_FONT_POSTSCRIPTNAME, new FontStyle(400, 0), 0, "", (String) null)), LocaleList.getEmptyLocaleList(), 0);
        return new FontConfig.NamedFamilyList(Collections.singletonList(family), CUSTOM_FONT_FAMILY_NAME);
    }

    private static void doUpdateTypeface(Configuration configuration) {
        Typeface tf = null;
        File flipedFontFile = new File(getCustomizedFontFile(false));
        if (flipedFontFile.exists()) {
            isFlipFontUsed = true;
        } else {
            isFlipFontUsed = false;
        }
        oplus.content.res.OplusFontUtils.isFlipFontUsed = isFlipFontUsed;
        try {
            String path = Os.readlink(FONTINFOARRAY_ROM6D0[0].mDataFontName);
            if (!TextUtils.isEmpty(path)) {
                tf = Typeface.createFromFile(new File(path));
            }
        } catch (ErrnoException e) {
            loge("Could not update selinux policy initFont createFromFile ", e);
        } catch (RuntimeException e2) {
            loge("RuntimeException initFont() createFromFile fail", e2);
        }
        if (sCurrentTypefaces == null) {
            sCurrentTypefaces = new Typeface[4];
        }
        if (isFlipFontUsed) {
            if (tf != null) {
                sCurrentTypefaces[0] = Typeface.create(tf, 0);
                sCurrentTypefaces[1] = Typeface.create(tf, 1);
                sCurrentTypefaces[2] = Typeface.create(tf, 2);
                sCurrentTypefaces[3] = Typeface.create(tf, 3);
                sCurrentTypefacesArray = new ArrayList(Arrays.asList(sCurrentTypefaces));
                return;
            }
            return;
        }
        logd("Not using flip font");
    }

    public static void createIndividualTypefae() {
        if (Typeface.getSystemFontMap() != null && Typeface.getSystemFontMap().containsKey(CUSTOM_FONT_FAMILY_NAME)) {
            isFlipFontUsed = true;
        }
        if (isFlipFontUsed) {
            if (sCurrentTypefaces == null) {
                sCurrentTypefaces = new Typeface[4];
            }
            Typeface create = Typeface.create(CUSTOM_FONT_FAMILY_NAME, 0);
            sIndividualTypeface = create;
            if (create != null) {
                sCurrentTypefaces[0] = sIndividualTypeface;
                sCurrentTypefaces[1] = Typeface.create(CUSTOM_FONT_FAMILY_NAME, 1);
                sCurrentTypefaces[2] = Typeface.create(CUSTOM_FONT_FAMILY_NAME, 2);
                sCurrentTypefaces[3] = Typeface.create(CUSTOM_FONT_FAMILY_NAME, 3);
                sCurrentTypefacesArray = new ArrayList(Arrays.asList(sCurrentTypefaces));
                return;
            }
            return;
        }
        sIndividualTypeface = null;
        sCurrentTypefacesArray = null;
        sCurrentTypefaces = null;
    }

    public static int getAdaptionValue(String name) {
        try {
            OplusActivityManager mOppoActivityManager = new OplusActivityManager();
            int data = mOppoActivityManager.getFontVariationAdaption(name);
            return data;
        } catch (Exception e) {
            Log.e("FontUtils", "init data Exception , " + e);
            return 0;
        }
    }

    public static boolean createTypefaceForCustom(Map<String, Typeface> outSystemFontMap, Map.Entry<String, FontFamily[]> entry) {
        if (isFlipFontUsed && CUSTOM_FONT_FAMILY_NAME.equals(entry.getKey())) {
            File flipedFontFile = new File(getCustomizedFontFile(false));
            if (flipedFontFile.exists()) {
                Typeface tf = null;
                try {
                    tf = Typeface.createFromFile(flipedFontFile);
                } catch (RuntimeException e) {
                    loge("RuntimeException createTypefaceForCustom fail", e);
                }
                if (tf != null) {
                    outSystemFontMap.put(CUSTOM_FONT_FAMILY_NAME, tf);
                    return true;
                }
            }
        }
        return false;
    }

    public static Typeface replaceSysSans(Typeface typeface, String familyName) {
        if ((OplusBaseFontUtils.OPLUS_SANS_VARIATION_FONT.equals(familyName) || OplusBaseFontUtils.ONEPLUS_SANS_VARIATION_FONT.equals(familyName)) && !isCurrentLanguageSupportVariationFont()) {
            return Typeface.DEFAULT;
        }
        return typeface;
    }
}
