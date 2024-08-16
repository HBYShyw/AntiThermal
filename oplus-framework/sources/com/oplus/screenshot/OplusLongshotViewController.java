package com.oplus.screenshot;

import android.view.IViewRootImplExt;
import android.view.ViewExtImpl;

/* loaded from: classes.dex */
public class OplusLongshotViewController extends OplusLongshotController {
    private ViewExtImpl mViewExt;

    public OplusLongshotViewController(OplusLongshotViewBase view) {
        super(view, "View");
        this.mViewExt = (ViewExtImpl) view;
    }

    @Override // com.oplus.screenshot.IOplusLongshotController
    public boolean isLongshotConnected() {
        IViewRootImplExt viewRoot = this.mViewExt.getViewRootImpl();
        if (viewRoot != null) {
            return viewRoot.isConnected();
        }
        return false;
    }

    @Override // com.oplus.screenshot.IOplusLongshotController
    public int getOverScrollMode(int overScrollMode) {
        return overScrollMode;
    }

    @Override // com.oplus.screenshot.IOplusLongshotController
    public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent, int oldScrollY, boolean result) {
        int top = -maxOverScrollY;
        int bottom = maxOverScrollY + scrollRangeY;
        boolean clampedY = false;
        int newScrollY = scrollY + deltaY;
        if (newScrollY > bottom) {
            newScrollY = bottom;
            clampedY = true;
        } else if (newScrollY < top) {
            newScrollY = top;
            clampedY = true;
        }
        if (newScrollY == oldScrollY) {
            return result;
        }
        int left = -maxOverScrollX;
        int right = maxOverScrollX + scrollRangeX;
        boolean clampedX = false;
        int newScrollX = scrollX + deltaX;
        if (newScrollX > right) {
            newScrollX = right;
            clampedX = true;
        } else if (newScrollX < left) {
            newScrollX = left;
            clampedX = true;
        }
        this.mViewExt.onLongshotOverScrolled(newScrollX, newScrollY, clampedX, clampedY);
        return clampedX || clampedY;
    }
}
