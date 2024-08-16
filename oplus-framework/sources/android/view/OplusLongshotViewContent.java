package android.view;

import android.graphics.Rect;

/* loaded from: classes.dex */
public class OplusLongshotViewContent {
    private final OplusLongshotViewContent mParent;
    private final Rect mRect;
    private final View mView;

    public OplusLongshotViewContent(View view, Rect rect, OplusLongshotViewContent parent) {
        Rect rect2 = new Rect();
        this.mRect = rect2;
        this.mView = view;
        rect2.set(rect);
        this.mParent = parent;
    }

    public String toString() {
        return "{Parent=" + this.mParent + ":" + this.mRect + ":" + this.mView + "}";
    }

    public OplusLongshotViewContent getParent() {
        return this.mParent;
    }

    public View getView() {
        return this.mView;
    }

    public Rect getRect() {
        return this.mRect;
    }
}
