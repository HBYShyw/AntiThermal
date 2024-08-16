package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$styleable;
import z3.MaterialResources;

/* compiled from: CalendarItemStyle.java */
/* renamed from: com.google.android.material.datepicker.a, reason: use source file name */
/* loaded from: classes.dex */
final class CalendarItemStyle {

    /* renamed from: a, reason: collision with root package name */
    private final Rect f8684a;

    /* renamed from: b, reason: collision with root package name */
    private final ColorStateList f8685b;

    /* renamed from: c, reason: collision with root package name */
    private final ColorStateList f8686c;

    /* renamed from: d, reason: collision with root package name */
    private final ColorStateList f8687d;

    /* renamed from: e, reason: collision with root package name */
    private final int f8688e;

    /* renamed from: f, reason: collision with root package name */
    private final ShapeAppearanceModel f8689f;

    private CalendarItemStyle(ColorStateList colorStateList, ColorStateList colorStateList2, ColorStateList colorStateList3, int i10, ShapeAppearanceModel shapeAppearanceModel, Rect rect) {
        Preconditions.b(rect.left);
        Preconditions.b(rect.top);
        Preconditions.b(rect.right);
        Preconditions.b(rect.bottom);
        this.f8684a = rect;
        this.f8685b = colorStateList2;
        this.f8686c = colorStateList;
        this.f8687d = colorStateList3;
        this.f8688e = i10;
        this.f8689f = shapeAppearanceModel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static CalendarItemStyle a(Context context, int i10) {
        Preconditions.a(i10 != 0, "Cannot create a CalendarItemStyle with a styleResId of 0");
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i10, R$styleable.MaterialCalendarItem);
        Rect rect = new Rect(obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialCalendarItem_android_insetLeft, 0), obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialCalendarItem_android_insetTop, 0), obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialCalendarItem_android_insetRight, 0), obtainStyledAttributes.getDimensionPixelOffset(R$styleable.MaterialCalendarItem_android_insetBottom, 0));
        ColorStateList a10 = MaterialResources.a(context, obtainStyledAttributes, R$styleable.MaterialCalendarItem_itemFillColor);
        ColorStateList a11 = MaterialResources.a(context, obtainStyledAttributes, R$styleable.MaterialCalendarItem_itemTextColor);
        ColorStateList a12 = MaterialResources.a(context, obtainStyledAttributes, R$styleable.MaterialCalendarItem_itemStrokeColor);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MaterialCalendarItem_itemStrokeWidth, 0);
        ShapeAppearanceModel m10 = ShapeAppearanceModel.b(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendarItem_itemShapeAppearance, 0), obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendarItem_itemShapeAppearanceOverlay, 0)).m();
        obtainStyledAttributes.recycle();
        return new CalendarItemStyle(a10, a11, a12, dimensionPixelSize, m10, rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f8684a.bottom;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c() {
        return this.f8684a.top;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(TextView textView) {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable();
        materialShapeDrawable.setShapeAppearanceModel(this.f8689f);
        materialShapeDrawable2.setShapeAppearanceModel(this.f8689f);
        materialShapeDrawable.a0(this.f8686c);
        materialShapeDrawable.l0(this.f8688e, this.f8687d);
        textView.setTextColor(this.f8685b);
        RippleDrawable rippleDrawable = new RippleDrawable(this.f8685b.withAlpha(30), materialShapeDrawable, materialShapeDrawable2);
        Rect rect = this.f8684a;
        ViewCompat.p0(textView, new InsetDrawable((Drawable) rippleDrawable, rect.left, rect.top, rect.right, rect.bottom));
    }
}
