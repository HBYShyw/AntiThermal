package com.oplus.screenshot;

import android.view.View;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface IOplusScrollable<V extends View> {
    int getHorizontalScrollExtent(View view);

    int getHorizontalScrollOffset(View view);

    int getHorizontalScrollRange(View view);

    int getVerticalScrollExtent(View view);

    int getVerticalScrollOffset(View view);

    int getVerticalScrollRange(View view);

    void scrollBy(View view, int i, int i2);

    void scrollTo(View view, int i, int i2);
}
