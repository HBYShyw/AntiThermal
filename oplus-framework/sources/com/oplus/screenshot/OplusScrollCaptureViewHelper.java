package com.oplus.screenshot;

import android.graphics.Rect;
import android.view.View;
import com.android.internal.view.ScrollCaptureViewHelper;

/* loaded from: classes.dex */
public abstract class OplusScrollCaptureViewHelper<V extends View> implements ScrollCaptureViewHelper<V> {
    public Rect onComputeScrollBounds(V view) {
        Rect bounds = new Rect(0, 0, view.getWidth(), view.getHeight());
        bounds.inset(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        return bounds;
    }
}
