package com.oplus.screenshot;

import android.view.View;

/* loaded from: classes.dex */
public class OplusViewScrollable implements IOplusScrollable<View> {
    @Override // com.oplus.screenshot.IOplusScrollable
    public void scrollBy(View view, int x, int y) {
        view.scrollBy(x, y);
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public void scrollTo(View view, int x, int y) {
        view.scrollTo(x, y);
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getHorizontalScrollOffset(View view) {
        return view.getViewWrapper().computeHorizontalScrollOffset();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getHorizontalScrollExtent(View view) {
        return view.getViewWrapper().computeHorizontalScrollExtent();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getHorizontalScrollRange(View view) {
        return view.getViewWrapper().computeHorizontalScrollRange();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getVerticalScrollOffset(View view) {
        return view.getViewWrapper().computeVerticalScrollOffset();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getVerticalScrollExtent(View view) {
        return view.getViewWrapper().computeVerticalScrollExtent();
    }

    @Override // com.oplus.screenshot.IOplusScrollable
    public int getVerticalScrollRange(View view) {
        return view.getViewWrapper().computeVerticalScrollRange();
    }
}
