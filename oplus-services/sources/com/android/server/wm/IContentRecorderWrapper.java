package com.android.server.wm;

import android.graphics.Point;
import android.graphics.Rect;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IContentRecorderWrapper {
    default Point fetchSurfaceSizeIfPresent() {
        return null;
    }

    default DisplayContent getDisplayContent() {
        return null;
    }

    default WindowContainer getRecordedWindowContainer() {
        return null;
    }

    default Rect getRectBounds() {
        return null;
    }
}
