package com.oplus.font;

import android.common.IOplusCommonFeature;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ITypefaceExt;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;

/* loaded from: classes.dex */
public interface IOplusBaseFontManager extends IOplusCommonFeature {
    default void createFontLink(String pkgName) {
    }

    default void deleteFontLink(String pkgName) {
    }

    default void handleFactoryReset() {
    }

    default Typeface flipTypeface(ITypefaceExt typeface, Paint paint) {
        return typeface != null ? typeface.getTypeface() : Typeface.DEFAULT;
    }

    default String getSystemFontConfig() {
        return "/system_ext/etc/fonts_base.xml";
    }

    default boolean isFlipFontUsed() {
        return false;
    }

    default void setCurrentAppName(String pkgName) {
    }

    default void setFlipFont(Configuration config, int changes) {
    }

    default void setFlipFontWhenUserChange(Configuration config, int changes) {
    }

    default void replaceFakeBoldToMedium(TextView textView, ITypefaceExt typeface, int style) {
    }

    default void updateTypefaceInCurrProcess(Configuration config, int changes) {
    }

    default void onCleanupUserForFont(int userId) {
    }

    default void initVariationFontVariable(Context context) {
    }

    default void updateLanguageLocale(Configuration config) {
    }

    default void updateConfigurationInUIMode(Context context, Configuration config, int userId) {
    }

    default int getTypefaceIndex(int originIndex, int oplusIndex) {
        return originIndex;
    }

    default void setIMEFlag(boolean isIme) {
    }

    default void initFontsForserializeFontMap() {
    }

    default void updateOpSansConfig(Context context, Configuration config, int userId) {
    }
}
