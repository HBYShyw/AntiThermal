package com.oplus.favorite;

import android.app.Activity;
import android.common.OplusFeatureList;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.view.View;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusFavoriteManager extends IOplusBaseFavoriteManager {
    public static final boolean DBG;
    public static final IOplusFavoriteManager DEFAULT;
    public static final boolean LOG_DEBUG;
    public static final boolean LOG_PANIC;

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        LOG_PANIC = z;
        boolean z2 = SystemProperties.getBoolean("log.favorite.debug", false);
        LOG_DEBUG = z2;
        DBG = z || z2;
        DEFAULT = new IOplusFavoriteManager() { // from class: com.oplus.favorite.IOplusFavoriteManager.1
        };
    }

    default IOplusFavoriteManager getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusFavoriteManager;
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default void release() {
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default void processClick(View clickView, OplusFavoriteCallback callback) {
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default void processCrawl(View rootView, OplusFavoriteCallback callback, String param) {
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default void processSave(View rootView, OplusFavoriteCallback callback) {
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default void logActivityInfo(Activity activity) {
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default void logViewInfo(View view) {
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default Handler getWorkHandler() {
        return null;
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default void setEngine(OplusFavoriteEngines engine) {
    }

    @Override // com.oplus.favorite.IOplusBaseFavoriteManager
    default boolean isSaved(Context context, String packageName, List<Bundle> data) {
        return false;
    }
}
