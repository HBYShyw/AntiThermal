package com.oplus.font;

import android.common.OplusFeatureList;

/* loaded from: classes.dex */
public interface IOplusFontManager extends IOplusBaseFontManager {
    public static final IOplusFontManager DEFAULT = new IOplusFontManager() { // from class: com.oplus.font.IOplusFontManager.1
    };
    public static final String NAME = "OplusFontManager";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusFontManager;
    }

    default IOplusFontManager getDefault() {
        return DEFAULT;
    }
}
