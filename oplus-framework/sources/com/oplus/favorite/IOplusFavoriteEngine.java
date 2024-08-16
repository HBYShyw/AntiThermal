package com.oplus.favorite;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

/* loaded from: classes.dex */
public interface IOplusFavoriteEngine extends IOplusFavoriteConstans {
    Handler getWorkHandler();

    void init();

    boolean isDebugOn();

    boolean isLogOn();

    boolean isTestOn();

    void loadRule(Context context, String str, OplusFavoriteCallback oplusFavoriteCallback);

    void logActivityInfo(Activity activity);

    void logViewInfo(View view);

    void processClick(View view, OplusFavoriteCallback oplusFavoriteCallback);

    void processCrawl(View view, OplusFavoriteCallback oplusFavoriteCallback, String str);

    void processSave(View view, OplusFavoriteCallback oplusFavoriteCallback);

    void release();
}
