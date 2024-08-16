package com.google.android.material.button;

import a4.RippleDrawableCompat;
import a4.RippleUtils;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import c4.Shapeable;
import com.google.android.material.R$attr;
import com.google.android.material.R$styleable;
import com.google.android.material.internal.ViewUtils;
import r3.MaterialColors;
import z3.MaterialResources;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MaterialButtonHelper.java */
/* renamed from: com.google.android.material.button.a, reason: use source file name */
/* loaded from: classes.dex */
public class MaterialButtonHelper {

    /* renamed from: t, reason: collision with root package name */
    private static final boolean f8498t = true;

    /* renamed from: u, reason: collision with root package name */
    private static final boolean f8499u = false;

    /* renamed from: a, reason: collision with root package name */
    private final MaterialButton f8500a;

    /* renamed from: b, reason: collision with root package name */
    private ShapeAppearanceModel f8501b;

    /* renamed from: c, reason: collision with root package name */
    private int f8502c;

    /* renamed from: d, reason: collision with root package name */
    private int f8503d;

    /* renamed from: e, reason: collision with root package name */
    private int f8504e;

    /* renamed from: f, reason: collision with root package name */
    private int f8505f;

    /* renamed from: g, reason: collision with root package name */
    private int f8506g;

    /* renamed from: h, reason: collision with root package name */
    private int f8507h;

    /* renamed from: i, reason: collision with root package name */
    private PorterDuff.Mode f8508i;

    /* renamed from: j, reason: collision with root package name */
    private ColorStateList f8509j;

    /* renamed from: k, reason: collision with root package name */
    private ColorStateList f8510k;

    /* renamed from: l, reason: collision with root package name */
    private ColorStateList f8511l;

    /* renamed from: m, reason: collision with root package name */
    private Drawable f8512m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f8513n = false;

    /* renamed from: o, reason: collision with root package name */
    private boolean f8514o = false;

    /* renamed from: p, reason: collision with root package name */
    private boolean f8515p = false;

    /* renamed from: q, reason: collision with root package name */
    private boolean f8516q;

    /* renamed from: r, reason: collision with root package name */
    private LayerDrawable f8517r;

    /* renamed from: s, reason: collision with root package name */
    private int f8518s;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaterialButtonHelper(MaterialButton materialButton, ShapeAppearanceModel shapeAppearanceModel) {
        this.f8500a = materialButton;
        this.f8501b = shapeAppearanceModel;
    }

    private void E(int i10, int i11) {
        int C = ViewCompat.C(this.f8500a);
        int paddingTop = this.f8500a.getPaddingTop();
        int B = ViewCompat.B(this.f8500a);
        int paddingBottom = this.f8500a.getPaddingBottom();
        int i12 = this.f8504e;
        int i13 = this.f8505f;
        this.f8505f = i11;
        this.f8504e = i10;
        if (!this.f8514o) {
            F();
        }
        ViewCompat.A0(this.f8500a, C, (paddingTop + i10) - i12, B, (paddingBottom + i11) - i13);
    }

    private void F() {
        this.f8500a.setInternalBackground(a());
        MaterialShapeDrawable f10 = f();
        if (f10 != null) {
            f10.Z(this.f8518s);
        }
    }

    private void G(ShapeAppearanceModel shapeAppearanceModel) {
        if (f8499u && !this.f8514o) {
            int C = ViewCompat.C(this.f8500a);
            int paddingTop = this.f8500a.getPaddingTop();
            int B = ViewCompat.B(this.f8500a);
            int paddingBottom = this.f8500a.getPaddingBottom();
            F();
            ViewCompat.A0(this.f8500a, C, paddingTop, B, paddingBottom);
            return;
        }
        if (f() != null) {
            f().setShapeAppearanceModel(shapeAppearanceModel);
        }
        if (n() != null) {
            n().setShapeAppearanceModel(shapeAppearanceModel);
        }
        if (e() != null) {
            e().setShapeAppearanceModel(shapeAppearanceModel);
        }
    }

    private void H() {
        MaterialShapeDrawable f10 = f();
        MaterialShapeDrawable n10 = n();
        if (f10 != null) {
            f10.l0(this.f8507h, this.f8510k);
            if (n10 != null) {
                n10.k0(this.f8507h, this.f8513n ? MaterialColors.d(this.f8500a, R$attr.colorSurface) : 0);
            }
        }
    }

    private InsetDrawable I(Drawable drawable) {
        return new InsetDrawable(drawable, this.f8502c, this.f8504e, this.f8503d, this.f8505f);
    }

    private Drawable a() {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.f8501b);
        materialShapeDrawable.P(this.f8500a.getContext());
        DrawableCompat.i(materialShapeDrawable, this.f8509j);
        PorterDuff.Mode mode = this.f8508i;
        if (mode != null) {
            DrawableCompat.j(materialShapeDrawable, mode);
        }
        materialShapeDrawable.l0(this.f8507h, this.f8510k);
        MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(this.f8501b);
        materialShapeDrawable2.setTint(0);
        materialShapeDrawable2.k0(this.f8507h, this.f8513n ? MaterialColors.d(this.f8500a, R$attr.colorSurface) : 0);
        if (f8498t) {
            MaterialShapeDrawable materialShapeDrawable3 = new MaterialShapeDrawable(this.f8501b);
            this.f8512m = materialShapeDrawable3;
            DrawableCompat.h(materialShapeDrawable3, -1);
            RippleDrawable rippleDrawable = new RippleDrawable(RippleUtils.d(this.f8511l), I(new LayerDrawable(new Drawable[]{materialShapeDrawable2, materialShapeDrawable})), this.f8512m);
            this.f8517r = rippleDrawable;
            return rippleDrawable;
        }
        RippleDrawableCompat rippleDrawableCompat = new RippleDrawableCompat(this.f8501b);
        this.f8512m = rippleDrawableCompat;
        DrawableCompat.i(rippleDrawableCompat, RippleUtils.d(this.f8511l));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{materialShapeDrawable2, materialShapeDrawable, this.f8512m});
        this.f8517r = layerDrawable;
        return I(layerDrawable);
    }

    private MaterialShapeDrawable g(boolean z10) {
        LayerDrawable layerDrawable = this.f8517r;
        if (layerDrawable == null || layerDrawable.getNumberOfLayers() <= 0) {
            return null;
        }
        if (f8498t) {
            return (MaterialShapeDrawable) ((LayerDrawable) ((InsetDrawable) this.f8517r.getDrawable(0)).getDrawable()).getDrawable(!z10 ? 1 : 0);
        }
        return (MaterialShapeDrawable) this.f8517r.getDrawable(!z10 ? 1 : 0);
    }

    private MaterialShapeDrawable n() {
        return g(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(ColorStateList colorStateList) {
        if (this.f8510k != colorStateList) {
            this.f8510k = colorStateList;
            H();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B(int i10) {
        if (this.f8507h != i10) {
            this.f8507h = i10;
            H();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C(ColorStateList colorStateList) {
        if (this.f8509j != colorStateList) {
            this.f8509j = colorStateList;
            if (f() != null) {
                DrawableCompat.i(f(), this.f8509j);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D(PorterDuff.Mode mode) {
        if (this.f8508i != mode) {
            this.f8508i = mode;
            if (f() == null || this.f8508i == null) {
                return;
            }
            DrawableCompat.j(f(), this.f8508i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f8506g;
    }

    public int c() {
        return this.f8505f;
    }

    public int d() {
        return this.f8504e;
    }

    public Shapeable e() {
        LayerDrawable layerDrawable = this.f8517r;
        if (layerDrawable == null || layerDrawable.getNumberOfLayers() <= 1) {
            return null;
        }
        if (this.f8517r.getNumberOfLayers() > 2) {
            return (Shapeable) this.f8517r.getDrawable(2);
        }
        return (Shapeable) this.f8517r.getDrawable(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaterialShapeDrawable f() {
        return g(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList h() {
        return this.f8511l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShapeAppearanceModel i() {
        return this.f8501b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList j() {
        return this.f8510k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        return this.f8507h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList l() {
        return this.f8509j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode m() {
        return this.f8508i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o() {
        return this.f8514o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean p() {
        return this.f8516q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(TypedArray typedArray) {
        this.f8502c = typedArray.getDimensionPixelOffset(R$styleable.MaterialButton_android_insetLeft, 0);
        this.f8503d = typedArray.getDimensionPixelOffset(R$styleable.MaterialButton_android_insetRight, 0);
        this.f8504e = typedArray.getDimensionPixelOffset(R$styleable.MaterialButton_android_insetTop, 0);
        this.f8505f = typedArray.getDimensionPixelOffset(R$styleable.MaterialButton_android_insetBottom, 0);
        int i10 = R$styleable.MaterialButton_cornerRadius;
        if (typedArray.hasValue(i10)) {
            int dimensionPixelSize = typedArray.getDimensionPixelSize(i10, -1);
            this.f8506g = dimensionPixelSize;
            y(this.f8501b.w(dimensionPixelSize));
            this.f8515p = true;
        }
        this.f8507h = typedArray.getDimensionPixelSize(R$styleable.MaterialButton_strokeWidth, 0);
        this.f8508i = ViewUtils.parseTintMode(typedArray.getInt(R$styleable.MaterialButton_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN);
        this.f8509j = MaterialResources.a(this.f8500a.getContext(), typedArray, R$styleable.MaterialButton_backgroundTint);
        this.f8510k = MaterialResources.a(this.f8500a.getContext(), typedArray, R$styleable.MaterialButton_strokeColor);
        this.f8511l = MaterialResources.a(this.f8500a.getContext(), typedArray, R$styleable.MaterialButton_rippleColor);
        this.f8516q = typedArray.getBoolean(R$styleable.MaterialButton_android_checkable, false);
        this.f8518s = typedArray.getDimensionPixelSize(R$styleable.MaterialButton_elevation, 0);
        int C = ViewCompat.C(this.f8500a);
        int paddingTop = this.f8500a.getPaddingTop();
        int B = ViewCompat.B(this.f8500a);
        int paddingBottom = this.f8500a.getPaddingBottom();
        if (typedArray.hasValue(R$styleable.MaterialButton_android_background)) {
            s();
        } else {
            F();
        }
        ViewCompat.A0(this.f8500a, C + this.f8502c, paddingTop + this.f8504e, B + this.f8503d, paddingBottom + this.f8505f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(int i10) {
        if (f() != null) {
            f().setTint(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s() {
        this.f8514o = true;
        this.f8500a.setSupportBackgroundTintList(this.f8509j);
        this.f8500a.setSupportBackgroundTintMode(this.f8508i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(boolean z10) {
        this.f8516q = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(int i10) {
        if (this.f8515p && this.f8506g == i10) {
            return;
        }
        this.f8506g = i10;
        this.f8515p = true;
        y(this.f8501b.w(i10));
    }

    public void v(int i10) {
        E(this.f8504e, i10);
    }

    public void w(int i10) {
        E(i10, this.f8505f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(ColorStateList colorStateList) {
        if (this.f8511l != colorStateList) {
            this.f8511l = colorStateList;
            boolean z10 = f8498t;
            if (z10 && (this.f8500a.getBackground() instanceof RippleDrawable)) {
                ((RippleDrawable) this.f8500a.getBackground()).setColor(RippleUtils.d(colorStateList));
            } else {
                if (z10 || !(this.f8500a.getBackground() instanceof RippleDrawableCompat)) {
                    return;
                }
                ((RippleDrawableCompat) this.f8500a.getBackground()).setTintList(RippleUtils.d(colorStateList));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(ShapeAppearanceModel shapeAppearanceModel) {
        this.f8501b = shapeAppearanceModel;
        G(shapeAppearanceModel);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z(boolean z10) {
        this.f8513n = z10;
        H();
    }
}
