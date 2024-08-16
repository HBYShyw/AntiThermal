package com.android.server.wm.utils;

import android.util.Size;
import android.view.DisplayCutout;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WmDisplayCutout {
    public static final WmDisplayCutout NO_CUTOUT = new WmDisplayCutout(DisplayCutout.NO_CUTOUT, null);
    private final Size mFrameSize;
    private final DisplayCutout mInner;

    public WmDisplayCutout(DisplayCutout displayCutout, Size size) {
        this.mInner = displayCutout;
        this.mFrameSize = size;
    }

    public static WmDisplayCutout computeSafeInsets(DisplayCutout displayCutout, int i, int i2) {
        if (displayCutout == DisplayCutout.NO_CUTOUT) {
            return NO_CUTOUT;
        }
        return new WmDisplayCutout(displayCutout.replaceSafeInsets(DisplayCutout.computeSafeInsets(i, i2, displayCutout)), new Size(i, i2));
    }

    public WmDisplayCutout computeSafeInsets(int i, int i2) {
        return computeSafeInsets(this.mInner, i, i2);
    }

    public DisplayCutout getDisplayCutout() {
        return this.mInner;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WmDisplayCutout)) {
            return false;
        }
        WmDisplayCutout wmDisplayCutout = (WmDisplayCutout) obj;
        return Objects.equals(this.mInner, wmDisplayCutout.mInner) && Objects.equals(this.mFrameSize, wmDisplayCutout.mFrameSize);
    }

    public int hashCode() {
        return Objects.hash(this.mInner, this.mFrameSize);
    }

    public String toString() {
        return "WmDisplayCutout{" + this.mInner + ", mFrameSize=" + this.mFrameSize + '}';
    }
}
