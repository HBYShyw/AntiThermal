package com.oplus.screenshot;

import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public class OplusLongshotViewRoot {
    private static final boolean DBG = true;
    private static final String TAG = "LongshotDump/OplusLongshotViewRoot";
    private boolean mIsConnected = false;

    public void setConnected(boolean isConnected) {
        OplusLog.d(true, TAG, "setConnected : " + isConnected);
        this.mIsConnected = isConnected;
    }

    public boolean isConnected() {
        return this.mIsConnected;
    }
}
