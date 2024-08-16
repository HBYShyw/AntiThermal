package com.google.android.material.card;

import a4.RippleUtils;
import android.R;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import c4.CornerTreatment;
import c4.CutCornerTreatment;
import c4.MaterialShapeDrawable;
import c4.RoundedCornerTreatment;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$attr;
import com.google.android.material.R$id;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import r3.MaterialColors;
import z3.MaterialResources;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MaterialCardViewHelper.java */
/* renamed from: com.google.android.material.card.a, reason: use source file name */
/* loaded from: classes.dex */
public class MaterialCardViewHelper {

    /* renamed from: u, reason: collision with root package name */
    private static final double f8528u = Math.cos(Math.toRadians(45.0d));

    /* renamed from: v, reason: collision with root package name */
    private static final Drawable f8529v = null;

    /* renamed from: a, reason: collision with root package name */
    private final MaterialCardView f8530a;

    /* renamed from: c, reason: collision with root package name */
    private final MaterialShapeDrawable f8532c;

    /* renamed from: d, reason: collision with root package name */
    private final MaterialShapeDrawable f8533d;

    /* renamed from: e, reason: collision with root package name */
    private int f8534e;

    /* renamed from: f, reason: collision with root package name */
    private int f8535f;

    /* renamed from: g, reason: collision with root package name */
    private int f8536g;

    /* renamed from: h, reason: collision with root package name */
    private int f8537h;

    /* renamed from: i, reason: collision with root package name */
    private Drawable f8538i;

    /* renamed from: j, reason: collision with root package name */
    private Drawable f8539j;

    /* renamed from: k, reason: collision with root package name */
    private ColorStateList f8540k;

    /* renamed from: l, reason: collision with root package name */
    private ColorStateList f8541l;

    /* renamed from: m, reason: collision with root package name */
    private ShapeAppearanceModel f8542m;

    /* renamed from: n, reason: collision with root package name */
    private ColorStateList f8543n;

    /* renamed from: o, reason: collision with root package name */
    private Drawable f8544o;

    /* renamed from: p, reason: collision with root package name */
    private LayerDrawable f8545p;

    /* renamed from: q, reason: collision with root package name */
    private MaterialShapeDrawable f8546q;

    /* renamed from: r, reason: collision with root package name */
    private MaterialShapeDrawable f8547r;

    /* renamed from: t, reason: collision with root package name */
    private boolean f8549t;

    /* renamed from: b, reason: collision with root package name */
    private final Rect f8531b = new Rect();

    /* renamed from: s, reason: collision with root package name */
    private boolean f8548s = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MaterialCardViewHelper.java */
    /* renamed from: com.google.android.material.card.a$a */
    /* loaded from: classes.dex */
    public class a extends InsetDrawable {
        a(Drawable drawable, int i10, int i11, int i12, int i13) {
            super(drawable, i10, i11, i12, i13);
        }

        @Override // android.graphics.drawable.Drawable
        public int getMinimumHeight() {
            return -1;
        }

        @Override // android.graphics.drawable.Drawable
        public int getMinimumWidth() {
            return -1;
        }

        @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public boolean getPadding(Rect rect) {
            return false;
        }
    }

    public MaterialCardViewHelper(MaterialCardView materialCardView, AttributeSet attributeSet, int i10, int i11) {
        this.f8530a = materialCardView;
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(materialCardView.getContext(), attributeSet, i10, i11);
        this.f8532c = materialShapeDrawable;
        materialShapeDrawable.P(materialCardView.getContext());
        materialShapeDrawable.g0(-12303292);
        ShapeAppearanceModel.b v7 = materialShapeDrawable.D().v();
        TypedArray obtainStyledAttributes = materialCardView.getContext().obtainStyledAttributes(attributeSet, R$styleable.CardView, i10, R$style.CardView);
        int i12 = R$styleable.CardView_cardCornerRadius;
        if (obtainStyledAttributes.hasValue(i12)) {
            v7.o(obtainStyledAttributes.getDimension(i12, 0.0f));
        }
        this.f8533d = new MaterialShapeDrawable();
        V(v7.m());
        obtainStyledAttributes.recycle();
    }

    private Drawable B(Drawable drawable) {
        int i10;
        int i11;
        if (this.f8530a.getUseCompatPadding()) {
            int ceil = (int) Math.ceil(d());
            i10 = (int) Math.ceil(c());
            i11 = ceil;
        } else {
            i10 = 0;
            i11 = 0;
        }
        return new a(drawable, i10, i11, i10, i11);
    }

    private boolean E() {
        return (this.f8536g & 80) == 80;
    }

    private boolean F() {
        return (this.f8536g & 8388613) == 8388613;
    }

    private boolean Z() {
        return this.f8530a.getPreventCornerOverlap() && !e();
    }

    private float a() {
        return Math.max(Math.max(b(this.f8542m.q(), this.f8532c.I()), b(this.f8542m.s(), this.f8532c.J())), Math.max(b(this.f8542m.k(), this.f8532c.s()), b(this.f8542m.i(), this.f8532c.r())));
    }

    private boolean a0() {
        return this.f8530a.getPreventCornerOverlap() && e() && this.f8530a.getUseCompatPadding();
    }

    private float b(CornerTreatment cornerTreatment, float f10) {
        if (cornerTreatment instanceof RoundedCornerTreatment) {
            return (float) ((1.0d - f8528u) * f10);
        }
        if (cornerTreatment instanceof CutCornerTreatment) {
            return f10 / 2.0f;
        }
        return 0.0f;
    }

    private float c() {
        return this.f8530a.getMaxCardElevation() + (a0() ? a() : 0.0f);
    }

    private float d() {
        return (this.f8530a.getMaxCardElevation() * 1.5f) + (a0() ? a() : 0.0f);
    }

    private boolean e() {
        return this.f8532c.S();
    }

    private void e0(Drawable drawable) {
        if (this.f8530a.getForeground() instanceof InsetDrawable) {
            ((InsetDrawable) this.f8530a.getForeground()).setDrawable(drawable);
        } else {
            this.f8530a.setForeground(B(drawable));
        }
    }

    private Drawable f() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        MaterialShapeDrawable h10 = h();
        this.f8546q = h10;
        h10.a0(this.f8540k);
        stateListDrawable.addState(new int[]{R.attr.state_pressed}, this.f8546q);
        return stateListDrawable;
    }

    private Drawable g() {
        if (RippleUtils.f36a) {
            this.f8547r = h();
            return new RippleDrawable(this.f8540k, null, this.f8547r);
        }
        return f();
    }

    private void g0() {
        Drawable drawable;
        if (RippleUtils.f36a && (drawable = this.f8544o) != null) {
            ((RippleDrawable) drawable).setColor(this.f8540k);
            return;
        }
        MaterialShapeDrawable materialShapeDrawable = this.f8546q;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.a0(this.f8540k);
        }
    }

    private MaterialShapeDrawable h() {
        return new MaterialShapeDrawable(this.f8542m);
    }

    private Drawable r() {
        if (this.f8544o == null) {
            this.f8544o = g();
        }
        if (this.f8545p == null) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{this.f8544o, this.f8533d, this.f8539j});
            this.f8545p = layerDrawable;
            layerDrawable.setId(2, R$id.mtrl_card_checked_layer_id);
        }
        return this.f8545p;
    }

    private float t() {
        if (this.f8530a.getPreventCornerOverlap() && this.f8530a.getUseCompatPadding()) {
            return (float) ((1.0d - f8528u) * this.f8530a.getCardViewRadius());
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect A() {
        return this.f8531b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean C() {
        return this.f8548s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean D() {
        return this.f8549t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G(TypedArray typedArray) {
        ColorStateList a10 = MaterialResources.a(this.f8530a.getContext(), typedArray, R$styleable.MaterialCardView_strokeColor);
        this.f8543n = a10;
        if (a10 == null) {
            this.f8543n = ColorStateList.valueOf(-1);
        }
        this.f8537h = typedArray.getDimensionPixelSize(R$styleable.MaterialCardView_strokeWidth, 0);
        boolean z10 = typedArray.getBoolean(R$styleable.MaterialCardView_android_checkable, false);
        this.f8549t = z10;
        this.f8530a.setLongClickable(z10);
        this.f8541l = MaterialResources.a(this.f8530a.getContext(), typedArray, R$styleable.MaterialCardView_checkedIconTint);
        N(MaterialResources.e(this.f8530a.getContext(), typedArray, R$styleable.MaterialCardView_checkedIcon));
        Q(typedArray.getDimensionPixelSize(R$styleable.MaterialCardView_checkedIconSize, 0));
        P(typedArray.getDimensionPixelSize(R$styleable.MaterialCardView_checkedIconMargin, 0));
        this.f8536g = typedArray.getInteger(R$styleable.MaterialCardView_checkedIconGravity, 8388661);
        ColorStateList a11 = MaterialResources.a(this.f8530a.getContext(), typedArray, R$styleable.MaterialCardView_rippleColor);
        this.f8540k = a11;
        if (a11 == null) {
            this.f8540k = ColorStateList.valueOf(MaterialColors.d(this.f8530a, R$attr.colorControlHighlight));
        }
        K(MaterialResources.a(this.f8530a.getContext(), typedArray, R$styleable.MaterialCardView_cardForegroundColor));
        g0();
        d0();
        h0();
        this.f8530a.setBackgroundInternal(B(this.f8532c));
        Drawable r10 = this.f8530a.isClickable() ? r() : this.f8533d;
        this.f8538i = r10;
        this.f8530a.setForeground(B(r10));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(int i10, int i11) {
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        if (this.f8545p != null) {
            int i19 = 0;
            if (this.f8530a.getUseCompatPadding()) {
                i12 = (int) Math.ceil(d() * 2.0f);
                i19 = (int) Math.ceil(c() * 2.0f);
            } else {
                i12 = 0;
            }
            if (F()) {
                i13 = ((i10 - this.f8534e) - this.f8535f) - i19;
            } else {
                i13 = this.f8534e;
            }
            if (E()) {
                i14 = this.f8534e;
            } else {
                i14 = ((i11 - this.f8534e) - this.f8535f) - i12;
            }
            int i20 = i14;
            if (F()) {
                i15 = this.f8534e;
            } else {
                i15 = ((i10 - this.f8534e) - this.f8535f) - i19;
            }
            if (E()) {
                i16 = ((i11 - this.f8534e) - this.f8535f) - i12;
            } else {
                i16 = this.f8534e;
            }
            int i21 = i16;
            if (ViewCompat.x(this.f8530a) == 1) {
                i18 = i15;
                i17 = i13;
            } else {
                i17 = i15;
                i18 = i13;
            }
            this.f8545p.setLayerInset(2, i18, i21, i17, i20);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(boolean z10) {
        this.f8548s = z10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(ColorStateList colorStateList) {
        this.f8532c.a0(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void K(ColorStateList colorStateList) {
        MaterialShapeDrawable materialShapeDrawable = this.f8533d;
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        materialShapeDrawable.a0(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(boolean z10) {
        this.f8549t = z10;
    }

    public void M(boolean z10) {
        Drawable drawable = this.f8539j;
        if (drawable != null) {
            drawable.setAlpha(z10 ? 255 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N(Drawable drawable) {
        if (drawable != null) {
            Drawable mutate = DrawableCompat.l(drawable).mutate();
            this.f8539j = mutate;
            DrawableCompat.i(mutate, this.f8541l);
            M(this.f8530a.isChecked());
        } else {
            this.f8539j = f8529v;
        }
        LayerDrawable layerDrawable = this.f8545p;
        if (layerDrawable != null) {
            layerDrawable.setDrawableByLayerId(R$id.mtrl_card_checked_layer_id, this.f8539j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O(int i10) {
        this.f8536g = i10;
        H(this.f8530a.getMeasuredWidth(), this.f8530a.getMeasuredHeight());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P(int i10) {
        this.f8534e = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Q(int i10) {
        this.f8535f = i10;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R(ColorStateList colorStateList) {
        this.f8541l = colorStateList;
        Drawable drawable = this.f8539j;
        if (drawable != null) {
            DrawableCompat.i(drawable, colorStateList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void S(float f10) {
        V(this.f8542m.w(f10));
        this.f8538i.invalidateSelf();
        if (a0() || Z()) {
            c0();
        }
        if (a0()) {
            f0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T(float f10) {
        this.f8532c.b0(f10);
        MaterialShapeDrawable materialShapeDrawable = this.f8533d;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.b0(f10);
        }
        MaterialShapeDrawable materialShapeDrawable2 = this.f8547r;
        if (materialShapeDrawable2 != null) {
            materialShapeDrawable2.b0(f10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U(ColorStateList colorStateList) {
        this.f8540k = colorStateList;
        g0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void V(ShapeAppearanceModel shapeAppearanceModel) {
        this.f8542m = shapeAppearanceModel;
        this.f8532c.setShapeAppearanceModel(shapeAppearanceModel);
        this.f8532c.f0(!r0.S());
        MaterialShapeDrawable materialShapeDrawable = this.f8533d;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
        MaterialShapeDrawable materialShapeDrawable2 = this.f8547r;
        if (materialShapeDrawable2 != null) {
            materialShapeDrawable2.setShapeAppearanceModel(shapeAppearanceModel);
        }
        MaterialShapeDrawable materialShapeDrawable3 = this.f8546q;
        if (materialShapeDrawable3 != null) {
            materialShapeDrawable3.setShapeAppearanceModel(shapeAppearanceModel);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void W(ColorStateList colorStateList) {
        if (this.f8543n == colorStateList) {
            return;
        }
        this.f8543n = colorStateList;
        h0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void X(int i10) {
        if (i10 == this.f8537h) {
            return;
        }
        this.f8537h = i10;
        h0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Y(int i10, int i11, int i12, int i13) {
        this.f8531b.set(i10, i11, i12, i13);
        c0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b0() {
        Drawable drawable = this.f8538i;
        Drawable r10 = this.f8530a.isClickable() ? r() : this.f8533d;
        this.f8538i = r10;
        if (drawable != r10) {
            e0(r10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c0() {
        int a10 = (int) ((Z() || a0() ? a() : 0.0f) - t());
        MaterialCardView materialCardView = this.f8530a;
        Rect rect = this.f8531b;
        materialCardView.k(rect.left + a10, rect.top + a10, rect.right + a10, rect.bottom + a10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d0() {
        this.f8532c.Z(this.f8530a.getCardElevation());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f0() {
        if (!C()) {
            this.f8530a.setBackgroundInternal(B(this.f8532c));
        }
        this.f8530a.setForeground(B(this.f8538i));
    }

    void h0() {
        this.f8533d.l0(this.f8537h, this.f8543n);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        Drawable drawable = this.f8544o;
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            int i10 = bounds.bottom;
            this.f8544o.setBounds(bounds.left, bounds.top, bounds.right, i10 - 1);
            this.f8544o.setBounds(bounds.left, bounds.top, bounds.right, i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MaterialShapeDrawable j() {
        return this.f8532c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList k() {
        return this.f8532c.w();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList l() {
        return this.f8533d.w();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable m() {
        return this.f8539j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int n() {
        return this.f8536g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o() {
        return this.f8534e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int p() {
        return this.f8535f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList q() {
        return this.f8541l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float s() {
        return this.f8532c.I();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float u() {
        return this.f8532c.x();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList v() {
        return this.f8540k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShapeAppearanceModel w() {
        return this.f8542m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int x() {
        ColorStateList colorStateList = this.f8543n;
        if (colorStateList == null) {
            return -1;
        }
        return colorStateList.getDefaultColor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList y() {
        return this.f8543n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int z() {
        return this.f8537h;
    }
}
