package com.oplus.theme;

/* loaded from: classes.dex */
public class OplusThemeStyle implements IOplusThemeStyle {
    public static final int DEFAULT_DIALOG_BOOTMSG_THEME = 201523238;
    public static final int DEFAULT_DIALOG_SHARE_THEME = 201523232;
    public static final int DEFAULT_DIALOG_THEME = 201523207;
    public static final int DEFAULT_SYSTEM_THEME = 201523202;
    private static final String NEW_METADATA_STYLE_TITLE = "oplus.support.options";
    private static final String OLD_METADATA_STYLE_TITLE = "color.support.options";

    @Override // com.oplus.theme.IOplusThemeStyle
    public int getSystemThemeStyle(int theme) {
        return DEFAULT_SYSTEM_THEME;
    }

    @Override // com.oplus.theme.IOplusThemeStyle
    public int getDialogThemeStyle(int theme) {
        return DEFAULT_DIALOG_THEME;
    }

    @Override // com.oplus.theme.IOplusThemeStyle
    public int getDialogBootMessageThemeStyle(int theme) {
        return DEFAULT_DIALOG_BOOTMSG_THEME;
    }

    @Override // com.oplus.theme.IOplusThemeStyle
    public int getDialogAlertShareThemeStyle(int theme) {
        return DEFAULT_DIALOG_SHARE_THEME;
    }

    @Override // com.oplus.theme.IOplusThemeStyle
    public String getMetaDataStyleTitle(boolean isNew) {
        return isNew ? NEW_METADATA_STYLE_TITLE : OLD_METADATA_STYLE_TITLE;
    }
}
