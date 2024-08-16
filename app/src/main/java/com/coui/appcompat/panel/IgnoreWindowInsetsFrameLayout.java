package com.coui.appcompat.panel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.support.panel.R$styleable;

/* loaded from: classes.dex */
public class IgnoreWindowInsetsFrameLayout extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private boolean f6625e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f6626f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f6627g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f6628h;

    /* renamed from: i, reason: collision with root package name */
    private int f6629i;

    /* renamed from: j, reason: collision with root package name */
    private int f6630j;

    /* renamed from: k, reason: collision with root package name */
    private int f6631k;

    /* renamed from: l, reason: collision with root package name */
    private int f6632l;

    public IgnoreWindowInsetsFrameLayout(Context context) {
        super(context);
        this.f6625e = true;
        this.f6626f = true;
        this.f6627g = true;
        this.f6628h = true;
    }

    private void a(AttributeSet attributeSet) {
        if (getContext() != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.IgnoreWindowInsetsFrameLayout);
            this.f6625e = obtainStyledAttributes.getBoolean(R$styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsLeft, true);
            this.f6626f = obtainStyledAttributes.getBoolean(R$styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsTop, true);
            this.f6627g = obtainStyledAttributes.getBoolean(R$styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsRight, true);
            this.f6628h = obtainStyledAttributes.getBoolean(R$styleable.IgnoreWindowInsetsFrameLayout_ignoreWindowInsetsBottom, true);
            obtainStyledAttributes.recycle();
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        setPadding(this.f6625e ? 0 : Math.max(0, windowInsets.getSystemWindowInsetLeft() + this.f6629i), this.f6626f ? 0 : Math.max(0, windowInsets.getSystemWindowInsetTop() + this.f6630j), this.f6627g ? 0 : Math.max(0, windowInsets.getSystemWindowInsetRight() + this.f6631k), this.f6628h ? 0 : Math.max(0, windowInsets.getSystemWindowInsetBottom() + this.f6632l));
        this.f6629i = 0;
        this.f6630j = 0;
        this.f6631k = 0;
        this.f6632l = 0;
        return windowInsets.consumeSystemWindowInsets();
    }

    public void setIgnoreWindowInsetsBottom(boolean z10) {
        this.f6628h = z10;
    }

    public void setIgnoreWindowInsetsLeft(boolean z10) {
        this.f6625e = z10;
    }

    public void setIgnoreWindowInsetsRight(boolean z10) {
        this.f6627g = z10;
    }

    public void setIgnoreWindowInsetsTop(boolean z10) {
        this.f6626f = z10;
    }

    public void setWindowInsetsBottomOffset(int i10) {
        this.f6632l = i10;
    }

    public void setWindowInsetsLeftOffset(int i10) {
        this.f6629i = i10;
    }

    public void setWindowInsetsRightOffset(int i10) {
        this.f6631k = i10;
    }

    public void setWindowInsetsTopOffset(int i10) {
        this.f6630j = i10;
    }

    public IgnoreWindowInsetsFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f6625e = true;
        this.f6626f = true;
        this.f6627g = true;
        this.f6628h = true;
        a(attributeSet);
    }

    public IgnoreWindowInsetsFrameLayout(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f6625e = true;
        this.f6626f = true;
        this.f6627g = true;
        this.f6628h = true;
        a(attributeSet);
    }
}
