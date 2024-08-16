package com.oplus.favorite;

import android.os.SystemProperties;

/* loaded from: classes.dex */
public interface IOplusFavoriteConstans {
    public static final boolean DBG;
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    public static final String EXTRA_RESULT_DATA = "result_data";
    public static final String EXTRA_RESULT_ERROR = "result_error";
    public static final String EXTRA_RESULT_SAVED = "result_saved";
    public static final String EXTRA_RESULT_TITLES = "result_titles";
    public static final boolean LOG_FAVORITE;
    public static final boolean LOG_PANIC;
    public static final String TAG_UNIFY = "AnteaterFavorite";

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        LOG_PANIC = z;
        boolean z2 = SystemProperties.getBoolean("log.favorite", false);
        LOG_FAVORITE = z2;
        DBG = z || z2;
    }
}
