package com.oplus.wrapper.widget;

/* loaded from: classes.dex */
public class AbsListView {
    private static final String TAG = "AbsListView";
    private final android.widget.AbsListView mAbsListView;

    public AbsListView(android.widget.AbsListView absListView) {
        this.mAbsListView = absListView;
    }

    public int getTouchMode() {
        return this.mAbsListView.getWrapper().getExtImpl().getTouchMode();
    }

    public void startSpringback() {
        this.mAbsListView.getWrapper().getExtImpl().startSpringback();
    }

    public void setFlingMode(int mode) {
        this.mAbsListView.getWrapper().getExtImpl().setFlingMode(mode);
    }
}
