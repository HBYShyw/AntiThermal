package com.oplus.theme;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusThemeStyle extends IOplusCommonFeature {
    public static final IOplusThemeStyle DEFAULT = new IOplusThemeStyle() { // from class: com.oplus.theme.IOplusThemeStyle.1
    };
    public static final String NAME = "IOplusThemeStyle";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusThemeStyle;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default int getSystemThemeStyle(int theme) {
        return theme;
    }

    default int getDialogThemeStyle(int theme) {
        return theme;
    }

    default int getDialogBootMessageThemeStyle(int theme) {
        return theme;
    }

    default int getDialogAlertShareThemeStyle(int theme) {
        return theme;
    }

    default String getMetaDataStyleTitle(boolean isNew) {
        return "";
    }
}
