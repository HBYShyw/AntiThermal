package com.oplus.favorite;

import android.content.Context;
import android.os.Handler;
import java.util.ArrayList;

/* loaded from: classes.dex */
public interface IOplusFavoriteCallback {
    Handler createWorkHandler(Context context, String str, int i);

    boolean isSettingOn(Context context);

    void onFavoriteFinished(Context context, OplusFavoriteResult oplusFavoriteResult);

    void onFavoriteStart(Context context, OplusFavoriteResult oplusFavoriteResult);

    void onLoadFinished(Context context, boolean z, boolean z2, ArrayList<String> arrayList);
}
