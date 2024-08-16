package com.oplus.screenshot;

import android.graphics.Rect;

/* loaded from: classes.dex */
public interface IOplusScrollCaptureCallbacks {
    void onCaptureEnded();

    void onCaptureStarted();

    default void onImageRequestCompleted(int flags, Rect capturedArea) {
    }

    default void onImageRequestCompleted(int flags, Rect capturedArea, Rect screenArea) {
        onImageRequestCompleted(flags, capturedArea);
    }
}
