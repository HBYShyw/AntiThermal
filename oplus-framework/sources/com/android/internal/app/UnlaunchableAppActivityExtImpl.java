package com.android.internal.app;

import android.common.OplusFeatureCache;
import com.oplus.theme.IOplusThemeStyle;
import com.oplus.theme.OplusThemeStyle;

/* loaded from: classes.dex */
public class UnlaunchableAppActivityExtImpl implements IUnlaunchableAppActivityExt {
    public UnlaunchableAppActivityExtImpl(Object obj) {
    }

    public int adjustThemeResIdForDialog() {
        return ((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getDialogThemeStyle(OplusThemeStyle.DEFAULT_DIALOG_THEME);
    }
}
