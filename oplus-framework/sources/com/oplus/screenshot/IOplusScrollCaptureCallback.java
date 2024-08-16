package com.oplus.screenshot;

import android.graphics.Rect;
import android.os.CancellationSignal;
import android.view.ScrollCaptureCallback;
import android.view.ScrollCaptureSession;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public interface IOplusScrollCaptureCallback extends ScrollCaptureCallback {
    void onScrollCaptureImageRequest2(ScrollCaptureSession scrollCaptureSession, CancellationSignal cancellationSignal, Rect rect, Consumer<Rect[]> consumer);
}
