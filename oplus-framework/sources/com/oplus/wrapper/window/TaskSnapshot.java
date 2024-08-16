package com.oplus.wrapper.window;

import android.graphics.ColorSpace;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class TaskSnapshot {
    private final android.window.TaskSnapshot mTaskSnapshot;

    public TaskSnapshot(android.window.TaskSnapshot taskSnapshot) {
        this.mTaskSnapshot = taskSnapshot;
    }

    public Parcelable getTaskSnapshot() {
        return this.mTaskSnapshot;
    }

    public ColorSpace getColorSpace() {
        return this.mTaskSnapshot.getColorSpace();
    }

    public HardwareBuffer getHardwareBuffer() {
        return this.mTaskSnapshot.getHardwareBuffer();
    }

    public Rect getContentInsets() {
        return this.mTaskSnapshot.getContentInsets();
    }

    public long getId() {
        return this.mTaskSnapshot.getId();
    }

    public Rect getLetterboxInsets() {
        return this.mTaskSnapshot.getLetterboxInsets();
    }

    public int getOrientation() {
        return this.mTaskSnapshot.getOrientation();
    }

    public int getRotation() {
        return this.mTaskSnapshot.getRotation();
    }

    public Point getTaskSize() {
        return this.mTaskSnapshot.getTaskSize();
    }

    public int getWindowingMode() {
        return this.mTaskSnapshot.getWindowingMode();
    }

    public boolean isLowResolution() {
        return this.mTaskSnapshot.isLowResolution();
    }

    public boolean isRealSnapshot() {
        return this.mTaskSnapshot.isRealSnapshot();
    }

    public boolean isTranslucent() {
        return this.mTaskSnapshot.isTranslucent();
    }

    public int getAppearance() {
        return this.mTaskSnapshot.getAppearance();
    }
}
