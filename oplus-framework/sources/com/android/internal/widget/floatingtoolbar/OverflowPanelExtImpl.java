package com.android.internal.widget.floatingtoolbar;

import android.view.View;

/* loaded from: classes.dex */
public class OverflowPanelExtImpl implements IOverflowPanelExt {
    public OverflowPanelExtImpl(Object base) {
    }

    public boolean hookOverflowPanel() {
        return true;
    }

    public void hookSetScrollerbar(View overflowPanel) {
        if (overflowPanel != null) {
            overflowPanel.setVerticalScrollbarThumbDrawable(overflowPanel.getContext().getResources().getDrawable(201851003, overflowPanel.getContext().getTheme()));
        }
    }
}
