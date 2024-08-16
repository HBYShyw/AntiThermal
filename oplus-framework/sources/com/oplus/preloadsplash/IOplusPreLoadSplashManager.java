package com.oplus.preloadsplash;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public interface IOplusPreLoadSplashManager extends IOplusCommonFeature {
    public static final IOplusPreLoadSplashManager DEFAULT = new IOplusPreLoadSplashManager() { // from class: com.oplus.preloadsplash.IOplusPreLoadSplashManager.1
    };
    public static final String NAME = "IOplusPreLoadSplashManager";
    public static final boolean debug = false;

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusPreLoadSplashManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void init(Context context) {
    }

    default void preloadCacheDrawable() {
    }

    default Drawable getDrawableCache(int resourceID, int density, Resources.Theme theme, Resources wrapper) {
        return null;
    }

    default boolean saveDrawableCache(int resourceID, Drawable drawable, int density) {
        return false;
    }
}
