package com.oplus.view;

import android.util.Log;
import android.view.Choreographer;

/* loaded from: classes.dex */
public class OplusChoreographerHelper {
    private static final String TAG = "OplusChoreographerHelper";

    public void addVsyncDataCallback(Choreographer choreographer, IOplusVsyncCallback callback) {
        try {
            choreographer.getWrapper().getExtImpl().addOplusVsyncCallback(callback);
        } catch (Exception e) {
            Log.d(TAG, "failed to addVsyncDataCallback" + e.getMessage());
        }
    }

    public void removeVsyncDataCallback(Choreographer choreographer, IOplusVsyncCallback callback) {
        try {
            choreographer.getWrapper().getExtImpl().removeOplusVsyncCallback(callback);
        } catch (Exception e) {
            Log.d(TAG, "failed to removeVsyncDataCallback" + e.getMessage());
        }
    }

    /* loaded from: classes.dex */
    public interface IOplusVsyncCallback {
        default void onVsync(long timestampNanos, long vsyncEventData) {
        }
    }
}
