package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class Choreographer {
    private final android.view.Choreographer mChoreographer;

    public Choreographer(android.view.Choreographer choreographer) {
        this.mChoreographer = choreographer;
    }

    public void postCallback(int callbackType, Runnable action, Object token) {
        this.mChoreographer.postCallback(callbackType, action, token);
    }

    public void removeCallbacks(int callbackType, Runnable action, Object token) {
        this.mChoreographer.removeCallbacks(callbackType, action, token);
    }
}
