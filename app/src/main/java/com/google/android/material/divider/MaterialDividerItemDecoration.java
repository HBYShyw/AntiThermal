package com.google.android.material.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class MaterialDividerItemDecoration extends RecyclerView.o {

    /* renamed from: i, reason: collision with root package name */
    private static final int f8793i = R$style.Widget_MaterialComponents_MaterialDivider;

    /* renamed from: a, reason: collision with root package name */
    private Drawable f8794a;

    /* renamed from: b, reason: collision with root package name */
    private int f8795b;

    /* renamed from: c, reason: collision with root package name */
    private int f8796c;

    /* renamed from: d, reason: collision with root package name */
    private int f8797d;

    /* renamed from: e, reason: collision with root package name */
    private int f8798e;

    /* renamed from: f, reason: collision with root package name */
    private int f8799f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f8800g;

    /* renamed from: h, reason: collision with root package name */
    private final Rect f8801h;

    public MaterialDividerItemDecoration(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, R$attr.materialDividerStyle, i10);
    }

    private void j(Canvas canvas, RecyclerView recyclerView) {
        int height;
        int i10;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            i10 = recyclerView.getPaddingTop();
            height = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            canvas.clipRect(recyclerView.getPaddingLeft(), i10, recyclerView.getWidth() - recyclerView.getPaddingRight(), height);
        } else {
            height = recyclerView.getHeight();
            i10 = 0;
        }
        int i11 = i10 + this.f8798e;
        int i12 = height - this.f8799f;
        int childCount = recyclerView.getChildCount();
        for (int i13 = 0; i13 < childCount; i13++) {
            View childAt = recyclerView.getChildAt(i13);
            recyclerView.getLayoutManager().P(childAt, this.f8801h);
            int round = this.f8801h.right + Math.round(childAt.getTranslationX());
            this.f8794a.setBounds((round - this.f8794a.getIntrinsicWidth()) - this.f8795b, i11, round, i12);
            this.f8794a.draw(canvas);
        }
        canvas.restore();
    }

    private void k(Canvas canvas, RecyclerView recyclerView) {
        int width;
        int i10;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            i10 = recyclerView.getPaddingLeft();
            width = recyclerView.getWidth() - recyclerView.getPaddingRight();
            canvas.clipRect(i10, recyclerView.getPaddingTop(), width, recyclerView.getHeight() - recyclerView.getPaddingBottom());
        } else {
            width = recyclerView.getWidth();
            i10 = 0;
        }
        boolean z10 = ViewCompat.x(recyclerView) == 1;
        int i11 = i10 + (z10 ? this.f8799f : this.f8798e);
        int i12 = width - (z10 ? this.f8798e : this.f8799f);
        int childCount = recyclerView.getChildCount();
        if (!this.f8800g) {
            childCount--;
        }
        for (int i13 = 0; i13 < childCount; i13++) {
            View childAt = recyclerView.getChildAt(i13);
            recyclerView.getDecoratedBoundsWithMargins(childAt, this.f8801h);
            int round = this.f8801h.bottom + Math.round(childAt.getTranslationY());
            this.f8794a.setBounds(i11, (round - this.f8794a.getIntrinsicHeight()) - this.f8795b, i12, round);
            this.f8794a.draw(canvas);
        }
        canvas.restore();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void e(Rect rect, View view, RecyclerView recyclerView, RecyclerView.z zVar) {
        rect.set(0, 0, 0, 0);
        if (this.f8797d == 1) {
            rect.bottom = this.f8794a.getIntrinsicHeight() + this.f8795b;
        } else {
            rect.right = this.f8794a.getIntrinsicWidth() + this.f8795b;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void g(Canvas canvas, RecyclerView recyclerView, RecyclerView.z zVar) {
        if (recyclerView.getLayoutManager() == null) {
            return;
        }
        if (this.f8797d == 1) {
            k(canvas, recyclerView);
        } else {
            j(canvas, recyclerView);
        }
    }

    public void l(int i10) {
        this.f8796c = i10;
        Drawable l10 = DrawableCompat.l(this.f8794a);
        this.f8794a = l10;
        DrawableCompat.h(l10, i10);
    }

    public void m(int i10) {
        if (i10 != 0 && i10 != 1) {
            throw new IllegalArgumentException("Invalid orientation: " + i10 + ". It should be either HORIZONTAL or VERTICAL");
        }
        this.f8797d = i10;
    }

    public MaterialDividerItemDecoration(Context context, AttributeSet attributeSet, int i10, int i11) {
        this.f8801h = new Rect();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.MaterialDivider, i10, f8793i, new int[0]);
        this.f8796c = MaterialResources.a(context, obtainStyledAttributes, R$styleable.MaterialDivider_dividerColor).getDefaultColor();
        this.f8795b = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MaterialDivider_dividerThickness, context.getResources().getDimensionPixelSize(R$dimen.material_divider_thickness));
        this.f8798e = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialDivider_dividerInsetStart, 0);
        this.f8799f = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialDivider_dividerInsetEnd, 0);
        this.f8800g = obtainStyledAttributes.getBoolean(R$styleable.MaterialDivider_lastItemDecorated, true);
        obtainStyledAttributes.recycle();
        this.f8794a = new ShapeDrawable();
        l(this.f8796c);
        m(i11);
    }
}
