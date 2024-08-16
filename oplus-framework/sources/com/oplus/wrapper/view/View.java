package com.oplus.wrapper.view;

import android.content.res.Configuration;

/* loaded from: classes.dex */
public class View {
    private final android.view.View mView;

    public View(android.view.View view) {
        this.mView = view;
    }

    public boolean isVisibleToUser() {
        return this.mView.isVisibleToUser();
    }

    public boolean requestAccessibilityFocus() {
        return this.mView.requestAccessibilityFocus();
    }

    public boolean isLayoutRtl() {
        return this.mView.isLayoutRtl();
    }

    public ViewRootImpl getViewRootImpl() {
        android.view.ViewRootImpl viewRootImpl = this.mView.getViewRootImpl();
        if (viewRootImpl == null) {
            return null;
        }
        return new ViewRootImpl(viewRootImpl);
    }

    public void notifyViewAccessibilityStateChangedIfNeeded(int changeType) {
        this.mView.notifyViewAccessibilityStateChangedIfNeeded(changeType);
    }

    public void onMovedToDisplay(int displayId, Configuration config) {
        this.mView.onMovedToDisplay(displayId, config);
    }

    public void clearAccessibilityFocus() {
        this.mView.clearAccessibilityFocus();
    }
}
