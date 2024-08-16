package com.oplus.wrapper.view;

/* loaded from: classes.dex */
public class DragEvent {
    private final android.view.DragEvent mDragEvent;

    public DragEvent(android.view.DragEvent dragEvent) {
        this.mDragEvent = dragEvent;
    }

    public android.view.SurfaceControl getDragSurface() {
        return this.mDragEvent.getDragSurface();
    }

    public float getOffsetX() {
        return this.mDragEvent.getOffsetX();
    }

    public float getOffsetY() {
        return this.mDragEvent.getOffsetY();
    }
}
