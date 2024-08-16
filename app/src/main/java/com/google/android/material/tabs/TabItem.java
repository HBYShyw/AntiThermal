package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R$styleable;

/* loaded from: classes.dex */
public class TabItem extends View {

    /* renamed from: e, reason: collision with root package name */
    public final CharSequence f9261e;

    /* renamed from: f, reason: collision with root package name */
    public final Drawable f9262f;

    /* renamed from: g, reason: collision with root package name */
    public final int f9263g;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TintTypedArray v7 = TintTypedArray.v(context, attributeSet, R$styleable.TabItem);
        this.f9261e = v7.p(R$styleable.TabItem_android_text);
        this.f9262f = v7.g(R$styleable.TabItem_android_icon);
        this.f9263g = v7.n(R$styleable.TabItem_android_layout, 0);
        v7.x();
    }
}
