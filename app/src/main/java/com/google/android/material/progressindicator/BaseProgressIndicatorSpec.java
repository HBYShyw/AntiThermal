package com.google.android.material.progressindicator;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ThemeEnforcement;
import r3.MaterialColors;
import z3.MaterialResources;

/* compiled from: BaseProgressIndicatorSpec.java */
/* renamed from: com.google.android.material.progressindicator.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseProgressIndicatorSpec {

    /* renamed from: a, reason: collision with root package name */
    public int f9061a;

    /* renamed from: b, reason: collision with root package name */
    public int f9062b;

    /* renamed from: c, reason: collision with root package name */
    public int[] f9063c = new int[0];

    /* renamed from: d, reason: collision with root package name */
    public int f9064d;

    /* renamed from: e, reason: collision with root package name */
    public int f9065e;

    /* renamed from: f, reason: collision with root package name */
    public int f9066f;

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i10, int i11) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.mtrl_progress_track_thickness);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R$styleable.BaseProgressIndicator, i10, i11, new int[0]);
        this.f9061a = MaterialResources.d(context, obtainStyledAttributes, R$styleable.BaseProgressIndicator_trackThickness, dimensionPixelSize);
        this.f9062b = Math.min(MaterialResources.d(context, obtainStyledAttributes, R$styleable.BaseProgressIndicator_trackCornerRadius, 0), this.f9061a / 2);
        this.f9065e = obtainStyledAttributes.getInt(R$styleable.BaseProgressIndicator_showAnimationBehavior, 0);
        this.f9066f = obtainStyledAttributes.getInt(R$styleable.BaseProgressIndicator_hideAnimationBehavior, 0);
        c(context, obtainStyledAttributes);
        d(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    private void c(Context context, TypedArray typedArray) {
        int i10 = R$styleable.BaseProgressIndicator_indicatorColor;
        if (!typedArray.hasValue(i10)) {
            this.f9063c = new int[]{MaterialColors.b(context, R$attr.colorPrimary, -1)};
            return;
        }
        if (typedArray.peekValue(i10).type != 1) {
            this.f9063c = new int[]{typedArray.getColor(i10, -1)};
            return;
        }
        int[] intArray = context.getResources().getIntArray(typedArray.getResourceId(i10, -1));
        this.f9063c = intArray;
        if (intArray.length == 0) {
            throw new IllegalArgumentException("indicatorColors cannot be empty when indicatorColor is not used.");
        }
    }

    private void d(Context context, TypedArray typedArray) {
        int i10 = R$styleable.BaseProgressIndicator_trackColor;
        if (typedArray.hasValue(i10)) {
            this.f9064d = typedArray.getColor(i10, -1);
            return;
        }
        this.f9064d = this.f9063c[0];
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R.attr.disabledAlpha});
        float f10 = obtainStyledAttributes.getFloat(0, 0.2f);
        obtainStyledAttributes.recycle();
        this.f9064d = MaterialColors.a(this.f9064d, (int) (f10 * 255.0f));
    }

    public boolean a() {
        return this.f9066f != 0;
    }

    public boolean b() {
        return this.f9065e != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void e();
}
