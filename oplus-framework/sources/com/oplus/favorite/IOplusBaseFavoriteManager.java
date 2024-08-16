package com.oplus.favorite;

import android.app.Activity;
import android.common.IOplusCommonFeature;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusBaseFavoriteManager extends IOplusFavoriteConstans, IOplusCommonFeature {
    public static final String NAME = "OplusFavoriteManager";

    Handler getWorkHandler();

    boolean isSaved(Context context, String str, List<Bundle> list);

    void logActivityInfo(Activity activity);

    void logViewInfo(View view);

    void processClick(View view, OplusFavoriteCallback oplusFavoriteCallback);

    void processCrawl(View view, OplusFavoriteCallback oplusFavoriteCallback, String str);

    void processSave(View view, OplusFavoriteCallback oplusFavoriteCallback);

    void release();

    void setEngine(OplusFavoriteEngines oplusFavoriteEngines);
}
