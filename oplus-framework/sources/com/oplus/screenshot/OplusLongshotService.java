package com.oplus.screenshot;

import android.content.Context;
import com.oplus.screenshot.IOplusLongshot;

/* loaded from: classes.dex */
public abstract class OplusLongshotService extends IOplusLongshot.Stub {
    protected final Context mContext;
    protected final boolean mNavBarVisible;
    protected final boolean mStatusBarVisible;

    public OplusLongshotService(Context context, boolean statusBarVisible, boolean navBarVisible) {
        this.mContext = context;
        this.mStatusBarVisible = statusBarVisible;
        this.mNavBarVisible = navBarVisible;
    }

    @Override // com.oplus.screenshot.IOplusLongshot
    public void start(IOplusLongshotCallback callback) {
    }

    @Override // com.oplus.screenshot.IOplusLongshot
    public void stop() {
    }

    @Override // com.oplus.screenshot.IOplusLongshot
    public void notifyOverScroll(OplusLongshotEvent event) {
    }
}
