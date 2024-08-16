package com.oplus.screenshot;

import android.content.Context;
import android.os.Bundle;
import com.oplus.screenshot.IOplusScreenshot;

/* loaded from: classes.dex */
public abstract class OplusScreenshotService extends IOplusScreenshot.Stub {
    protected final Context mContext;
    protected final Bundle mExtras;

    public OplusScreenshotService(Context context, Bundle extras) {
        this.mContext = context;
        this.mExtras = extras;
    }

    @Override // com.oplus.screenshot.IOplusScreenshot
    public void start(IOplusScreenshotCallback callback) {
    }

    @Override // com.oplus.screenshot.IOplusScreenshot
    public void stop() {
    }

    @Override // com.oplus.screenshot.IOplusScreenshot
    public boolean isEdit() {
        return false;
    }
}
