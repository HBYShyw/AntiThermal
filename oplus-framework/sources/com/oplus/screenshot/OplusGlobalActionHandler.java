package com.oplus.screenshot;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes.dex */
public class OplusGlobalActionHandler extends Handler implements IOplusScreenshotHelper {
    public OplusGlobalActionHandler(Looper looper) {
        super(looper);
    }

    @Override // com.oplus.screenshot.IOplusScreenshotHelper
    public String getSource() {
        return "GlobalAction";
    }

    @Override // com.oplus.screenshot.IOplusScreenshotHelper
    public boolean isGlobalAction() {
        return false;
    }
}
