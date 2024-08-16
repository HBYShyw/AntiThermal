package com.oplus.favorite;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class OplusFavoriteCallback implements IOplusFavoriteCallback {
    protected static final boolean DBG = false;
    protected final String TAG = getClass().getSimpleName();

    @Override // com.oplus.favorite.IOplusFavoriteCallback
    public Handler createWorkHandler(Context context, String name, int priority) {
        HandlerThread thread = new HandlerThread("Favorite." + name, priority);
        thread.start();
        return new OplusFavoriteHandler(thread, "Client");
    }

    @Override // com.oplus.favorite.IOplusFavoriteCallback
    public void onLoadFinished(Context context, boolean noRule, boolean emptyRule, ArrayList<String> sceneList) {
    }

    @Override // com.oplus.favorite.IOplusFavoriteCallback
    public void onFavoriteStart(Context context, OplusFavoriteResult result) {
    }

    @Override // com.oplus.favorite.IOplusFavoriteCallback
    public void onFavoriteFinished(Context context, OplusFavoriteResult result) {
    }
}
