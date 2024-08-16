package android.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings;
import android.system.Os;
import android.util.Log;
import com.oplus.util.OplusFontUtils;
import java.io.File;
import java.util.Locale;

/* loaded from: classes.dex */
public class OplusBurmeseZgFlagHooksImpl implements IOplusBurmeseZgHooks {
    private static final String BURMESE_FONT_LINK_ON_DATA = "/data/format_unclear/font/OplusOSUI-Myanmar.ttf";
    private static final String SYSTEM_BURMESE_UNICODE_REAL_FONT_FILE = "/system/fonts/OplusOSUI-XThin.ttf";
    private static final String SYSTEM_BURMESE_ZG_REAL_FONT_FILE = "/system/fonts/MyanmarZg.ttf";
    private static final String TAG = "OplusZGSupport";

    @Override // android.view.IOplusBurmeseZgHooks
    public void initBurmeseZgFlag(Context context) {
        boolean zawgyiOn = isCurrentUseZgEncoding(context, context.getResources().getConfiguration());
        OplusFontUtils.setNeedReplaceAllTypefaceApp(zawgyiOn);
    }

    @Override // android.view.IOplusBurmeseZgHooks
    public void updateBurmeseZgFlag(Context context) {
        boolean zawgyiOn = isCurrentUseZgEncoding(context, null);
        OplusFontUtils.setNeedReplaceAllTypefaceApp(zawgyiOn);
    }

    @Override // android.view.IOplusBurmeseZgHooks
    public void updateBurmeseConfig(Configuration configuration) {
        int flipFont = configuration.getOplusExtraConfiguration().mFlipFont;
        if (flipFont == 10003) {
            configuration.getOplusExtraConfiguration().mBurmeseFontFlag = 1;
        } else if (flipFont == 10002) {
            configuration.getOplusExtraConfiguration().mBurmeseFontFlag = 2;
        }
    }

    @Override // android.view.IOplusBurmeseZgHooks
    public void initBurmeseConfigForUser(ContentResolver resolver, Configuration configuration) {
        boolean useZawgyiFont = false;
        String currentFontStr = Settings.System.getString(resolver, "current_typeface_burmese");
        String currentOldFontStr = Settings.System.getString(resolver, "current_typeface");
        if (currentFontStr != null && !currentFontStr.equals("")) {
            if (SYSTEM_BURMESE_ZG_REAL_FONT_FILE.equals(currentFontStr)) {
                useZawgyiFont = true;
            }
        } else if (SYSTEM_BURMESE_ZG_REAL_FONT_FILE.equals(currentOldFontStr)) {
            useZawgyiFont = true;
        }
        configuration.getOplusExtraConfiguration().mBurmeseFontFlag = useZawgyiFont ? 1 : 2;
    }

    @Override // android.view.IOplusBurmeseZgHooks
    public boolean getZgFlag() {
        return OplusFontUtils.getNeedReplaceAllTypefaceApp();
    }

    @Override // android.view.IOplusBurmeseZgHooks
    public void updateBurmeseEncodingForUser(Context context, Configuration config, int userId) {
        if (context != null) {
            flipBurmeseEncoding(context, config);
        } else {
            Log.d(TAG, "updateBurmeseEncodingForUser : WARNING context == null");
        }
    }

    private boolean flipBurmeseEncoding(Context context, Configuration config) {
        return relinkFontFile(BURMESE_FONT_LINK_ON_DATA, isCurrentUseZgEncoding(context, config) ? SYSTEM_BURMESE_ZG_REAL_FONT_FILE : SYSTEM_BURMESE_UNICODE_REAL_FONT_FILE);
    }

    private boolean relinkFontFile(String dataLink, String targetFont) {
        try {
            if (!Os.readlink(dataLink).equals(targetFont)) {
                File burmeseFont = new File(dataLink);
                burmeseFont.delete();
                Os.symlink(targetFont, dataLink);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "SELinux policy update malformed: " + e.getMessage());
            return false;
        } catch (Exception e2) {
            Log.d(TAG, "Could not update selinux policy: " + e2.getMessage());
            return false;
        }
    }

    private boolean isCurrentUseZgEncoding(Context context, Configuration config) {
        Locale defaultLocale;
        if (config == null) {
            return false;
        }
        if (config.getLocales().isEmpty() || config.getLocales().get(0) == null || !config.getLocales().get(0).getCountry().equals("ZG")) {
            return (config.getLocales().isEmpty() && (defaultLocale = Locale.getDefault()) != null && defaultLocale.getCountry().equals("ZG")) || config.getOplusExtraConfiguration().mBurmeseFontFlag == 1;
        }
        return true;
    }
}
