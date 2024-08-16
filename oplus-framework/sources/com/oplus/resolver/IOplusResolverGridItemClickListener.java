package com.oplus.resolver;

import android.view.View;

/* loaded from: classes.dex */
public interface IOplusResolverGridItemClickListener {
    void onItemClick(int i, int i2);

    void onItemLongClick(int i, int i2);

    default void onItemLongClick(int pagerNumber, int position, View icon) {
    }
}
