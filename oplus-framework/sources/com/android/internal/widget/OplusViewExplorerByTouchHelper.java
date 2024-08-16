package com.android.internal.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.IntArray;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/* loaded from: classes.dex */
public class OplusViewExplorerByTouchHelper extends ExploreByTouchHelper {
    private static final String VIEW_LOG_TAG = "ColorViewTouchHelper";
    private View mHostView;
    private OplusViewTalkBalkInteraction mOplusViewTalkBalkInteraction;
    private final Rect mTempRect;

    /* loaded from: classes.dex */
    public interface OplusViewTalkBalkInteraction {
        CharSequence getClassName();

        int getCurrentPosition();

        int getDisablePosition();

        void getItemBounds(int i, Rect rect);

        int getItemCounts();

        CharSequence getItemDescription(int i);

        int getVirtualViewAt(float f, float f2);

        void performAction(int i, int i2, boolean z);
    }

    public OplusViewExplorerByTouchHelper(View host) {
        super(host);
        this.mTempRect = new Rect();
        this.mHostView = null;
        this.mOplusViewTalkBalkInteraction = null;
        this.mHostView = host;
    }

    public void setFocusedVirtualView(int virtualViewId) {
        getAccessibilityNodeProvider(this.mHostView).performAction(virtualViewId, 64, null);
    }

    public void clearFocusedVirtualView() {
        int focusedVirtualView = getFocusedVirtualView();
        if (focusedVirtualView != Integer.MIN_VALUE) {
            getAccessibilityNodeProvider(this.mHostView).performAction(focusedVirtualView, 128, null);
        }
    }

    protected int getVirtualViewAt(float x, float y) {
        int position = this.mOplusViewTalkBalkInteraction.getVirtualViewAt(x, y);
        if (position >= 0) {
            return position;
        }
        return Integer.MIN_VALUE;
    }

    protected void getVisibleVirtualViews(IntArray virtualViewIds) {
        for (int day = 0; day < this.mOplusViewTalkBalkInteraction.getItemCounts(); day++) {
            virtualViewIds.add(day);
        }
    }

    protected void onPopulateEventForVirtualView(int virtualViewId, AccessibilityEvent event) {
        event.setContentDescription(this.mOplusViewTalkBalkInteraction.getItemDescription(virtualViewId));
    }

    protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfo node) {
        getItemBounds(virtualViewId, this.mTempRect);
        node.setContentDescription(this.mOplusViewTalkBalkInteraction.getItemDescription(virtualViewId));
        node.setBoundsInParent(this.mTempRect);
        if (this.mOplusViewTalkBalkInteraction.getClassName() != null) {
            node.setClassName(this.mOplusViewTalkBalkInteraction.getClassName());
        }
        node.addAction(16);
        if (virtualViewId == this.mOplusViewTalkBalkInteraction.getCurrentPosition()) {
            node.setSelected(true);
        }
        if (virtualViewId == this.mOplusViewTalkBalkInteraction.getDisablePosition()) {
            node.setEnabled(false);
        }
    }

    protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
        switch (action) {
            case 16:
                this.mOplusViewTalkBalkInteraction.performAction(virtualViewId, 16, false);
                return true;
            default:
                return false;
        }
    }

    private void getItemBounds(int position, Rect rect) {
        if (position >= 0 && position < this.mOplusViewTalkBalkInteraction.getItemCounts()) {
            this.mOplusViewTalkBalkInteraction.getItemBounds(position, rect);
        }
    }

    public void setOplusViewTalkBalkInteraction(OplusViewTalkBalkInteraction oplusViewTalkBalkInteraction) {
        this.mOplusViewTalkBalkInteraction = oplusViewTalkBalkInteraction;
    }
}
