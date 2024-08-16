package com.oplus.font;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ITypefaceExt;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.FileUtils;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import android.widget.TextView;
import com.oplus.util.OplusFontUtils;
import java.io.File;

/* loaded from: classes.dex */
public class OplusFontManager implements IOplusFontManager {
    private static final String TAG = "OplusFontManager";
    public static boolean sDebugfDetail = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static volatile OplusFontManager sInstance = null;
    private boolean mDynamicDebug = false;
    private boolean DEBUG_SWITCH = false | sDebugfDetail;

    public static OplusFontManager getInstance() {
        if (sInstance == null) {
            synchronized (OplusFontManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusFontManager();
                }
            }
        }
        return sInstance;
    }

    private OplusFontManager() {
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void createFontLink(String pkgName) {
        printLog("createFontLink");
        OplusFontUtils.createFontLink(pkgName);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void deleteFontLink(String pkgName) {
        printLog("deleteFontLink");
        OplusFontUtils.deleteFontLink(pkgName);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void handleFactoryReset() {
        printLog("handleFactoryReset");
        OplusFontUtils.handleFactoryReset();
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public Typeface flipTypeface(ITypefaceExt typeface, Paint paint) {
        return OplusFontUtils.flipTypeface(typeface, paint);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public boolean isFlipFontUsed() {
        return OplusFontUtils.isFlipFontUsed;
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void setCurrentAppName(String pkgName) {
        OplusFontUtils.setAppTypeFace(pkgName);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void setFlipFont(Configuration config, int changes) {
        if ((33554432 & changes) != 0) {
            OplusFontUtils.setFlipFont(config);
        }
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void setFlipFontWhenUserChange(Configuration config, int changes) {
        OplusFontUtils.setFlipFontWhenUserChange(config);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void replaceFakeBoldToMedium(TextView textView, ITypefaceExt typeface, int style) {
        OplusFontUtils.replaceFakeBoldToColorMedium(textView, typeface, style);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void updateTypefaceInCurrProcess(Configuration config, int changes) {
        OplusFontUtils.updateTypefaceInCurrProcess(config, changes);
    }

    private void printLog(String msg) {
        if (this.DEBUG_SWITCH) {
            Slog.d("OplusFontManager", "[impl] " + msg);
        }
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void onCleanupUserForFont(int userId) {
        if (userId != 0) {
            File fontFileForUser = new File(OplusFontUtils.DATA_FONT_DIRECTORY + userId);
            if (fontFileForUser.exists()) {
                boolean cleanUserFontResult = FileUtils.deleteContentsAndDir(fontFileForUser);
                if (this.DEBUG_SWITCH) {
                    Log.v("OplusFontManager", "onCleanupUserForFont result :" + cleanUserFontResult);
                }
            }
        }
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void initVariationFontVariable(Context context) {
        OplusFontUtils.initVariationFontVariable(context);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void updateLanguageLocale(Configuration config) {
        OplusFontUtils.updateLanguageLocale(config);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void updateConfigurationInUIMode(Context context, Configuration config, int userId) {
        OplusFontUtils.updateConfigurationInUIMode(context, config, userId);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public int getTypefaceIndex(int originIndex, int oplusIndex) {
        return oplusIndex;
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void setIMEFlag(boolean isIme) {
        OplusFontUtils.setIMEFlag(isIme);
    }

    @Override // com.oplus.font.IOplusBaseFontManager
    public void initFontsForserializeFontMap() {
        OplusFontUtils.initFontUtil();
    }
}
