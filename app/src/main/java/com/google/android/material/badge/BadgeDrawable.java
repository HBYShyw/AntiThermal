package com.google.android.material.badge;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import c4.MaterialShapeDrawable;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.badge.BadgeState;
import com.google.android.material.internal.TextDrawableHelper;
import com.google.android.material.internal.ThemeEnforcement;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import z3.TextAppearance;

/* compiled from: BadgeDrawable.java */
/* renamed from: com.google.android.material.badge.a, reason: use source file name */
/* loaded from: classes.dex */
public class BadgeDrawable extends Drawable implements TextDrawableHelper.TextDrawableDelegate {

    /* renamed from: r, reason: collision with root package name */
    private static final int f8297r = R$style.Widget_MaterialComponents_Badge;

    /* renamed from: s, reason: collision with root package name */
    private static final int f8298s = R$attr.badgeStyle;

    /* renamed from: e, reason: collision with root package name */
    private final WeakReference<Context> f8299e;

    /* renamed from: f, reason: collision with root package name */
    private final MaterialShapeDrawable f8300f;

    /* renamed from: g, reason: collision with root package name */
    private final TextDrawableHelper f8301g;

    /* renamed from: h, reason: collision with root package name */
    private final Rect f8302h;

    /* renamed from: i, reason: collision with root package name */
    private final BadgeState f8303i;

    /* renamed from: j, reason: collision with root package name */
    private float f8304j;

    /* renamed from: k, reason: collision with root package name */
    private float f8305k;

    /* renamed from: l, reason: collision with root package name */
    private int f8306l;

    /* renamed from: m, reason: collision with root package name */
    private float f8307m;

    /* renamed from: n, reason: collision with root package name */
    private float f8308n;

    /* renamed from: o, reason: collision with root package name */
    private float f8309o;

    /* renamed from: p, reason: collision with root package name */
    private WeakReference<View> f8310p;

    /* renamed from: q, reason: collision with root package name */
    private WeakReference<FrameLayout> f8311q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BadgeDrawable.java */
    /* renamed from: com.google.android.material.badge.a$a */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f8312e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ FrameLayout f8313f;

        a(View view, FrameLayout frameLayout) {
            this.f8312e = view;
            this.f8313f = frameLayout;
        }

        @Override // java.lang.Runnable
        public void run() {
            BadgeDrawable.this.A(this.f8312e, this.f8313f);
        }
    }

    private BadgeDrawable(Context context, int i10, int i11, int i12, BadgeState.State state) {
        this.f8299e = new WeakReference<>(context);
        ThemeEnforcement.checkMaterialTheme(context);
        this.f8302h = new Rect();
        this.f8300f = new MaterialShapeDrawable();
        TextDrawableHelper textDrawableHelper = new TextDrawableHelper(this);
        this.f8301g = textDrawableHelper;
        textDrawableHelper.getTextPaint().setTextAlign(Paint.Align.CENTER);
        x(R$style.TextAppearance_MaterialComponents_Badge);
        this.f8303i = new BadgeState(context, i10, i11, i12, state);
        v();
    }

    private void B() {
        Context context = this.f8299e.get();
        WeakReference<View> weakReference = this.f8310p;
        View view = weakReference != null ? weakReference.get() : null;
        if (context == null || view == null) {
            return;
        }
        Rect rect = new Rect();
        rect.set(this.f8302h);
        Rect rect2 = new Rect();
        view.getDrawingRect(rect2);
        WeakReference<FrameLayout> weakReference2 = this.f8311q;
        FrameLayout frameLayout = weakReference2 != null ? weakReference2.get() : null;
        if (frameLayout != null || BadgeUtils.f8315a) {
            if (frameLayout == null) {
                frameLayout = (ViewGroup) view.getParent();
            }
            frameLayout.offsetDescendantRectToMyCoords(view, rect2);
        }
        a(context, rect2, view);
        BadgeUtils.f(this.f8302h, this.f8304j, this.f8305k, this.f8308n, this.f8309o);
        this.f8300f.X(this.f8307m);
        if (rect.equals(this.f8302h)) {
            return;
        }
        this.f8300f.setBounds(this.f8302h);
    }

    private void C() {
        this.f8306l = ((int) Math.pow(10.0d, i() - 1.0d)) - 1;
    }

    private void a(Context context, Rect rect, View view) {
        int i10;
        float f10;
        float f11;
        int m10 = m();
        int f12 = this.f8303i.f();
        if (f12 != 8388691 && f12 != 8388693) {
            this.f8305k = rect.top + m10;
        } else {
            this.f8305k = rect.bottom - m10;
        }
        if (j() <= 9) {
            float f13 = !n() ? this.f8303i.f8276c : this.f8303i.f8277d;
            this.f8307m = f13;
            this.f8309o = f13;
            this.f8308n = f13;
        } else {
            float f14 = this.f8303i.f8277d;
            this.f8307m = f14;
            this.f8309o = f14;
            this.f8308n = (this.f8301g.getTextWidth(e()) / 2.0f) + this.f8303i.f8278e;
        }
        Resources resources = context.getResources();
        if (n()) {
            i10 = R$dimen.mtrl_badge_text_horizontal_edge_offset;
        } else {
            i10 = R$dimen.mtrl_badge_horizontal_edge_offset;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(i10);
        int l10 = l();
        int f15 = this.f8303i.f();
        if (f15 != 8388659 && f15 != 8388691) {
            if (ViewCompat.x(view) == 0) {
                f11 = ((rect.right + this.f8308n) - dimensionPixelSize) - l10;
            } else {
                f11 = (rect.left - this.f8308n) + dimensionPixelSize + l10;
            }
            this.f8304j = f11;
            return;
        }
        if (ViewCompat.x(view) == 0) {
            f10 = (rect.left - this.f8308n) + dimensionPixelSize + l10;
        } else {
            f10 = ((rect.right + this.f8308n) - dimensionPixelSize) - l10;
        }
        this.f8304j = f10;
    }

    public static BadgeDrawable b(Context context) {
        return new BadgeDrawable(context, 0, f8298s, f8297r, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BadgeDrawable c(Context context, BadgeState.State state) {
        return new BadgeDrawable(context, 0, f8298s, f8297r, state);
    }

    private void d(Canvas canvas) {
        Rect rect = new Rect();
        String e10 = e();
        this.f8301g.getTextPaint().getTextBounds(e10, 0, e10.length(), rect);
        canvas.drawText(e10, this.f8304j, this.f8305k + (rect.height() / 2), this.f8301g.getTextPaint());
    }

    private String e() {
        if (j() <= this.f8306l) {
            return NumberFormat.getInstance(this.f8303i.o()).format(j());
        }
        Context context = this.f8299e.get();
        return context == null ? "" : String.format(this.f8303i.o(), context.getString(R$string.mtrl_exceed_max_badge_number_suffix), Integer.valueOf(this.f8306l), "+");
    }

    private int l() {
        return (n() ? this.f8303i.k() : this.f8303i.l()) + this.f8303i.b();
    }

    private int m() {
        return (n() ? this.f8303i.q() : this.f8303i.r()) + this.f8303i.c();
    }

    private void o() {
        this.f8301g.getTextPaint().setAlpha(getAlpha());
        invalidateSelf();
    }

    private void p() {
        ColorStateList valueOf = ColorStateList.valueOf(this.f8303i.e());
        if (this.f8300f.w() != valueOf) {
            this.f8300f.a0(valueOf);
            invalidateSelf();
        }
    }

    private void q() {
        WeakReference<View> weakReference = this.f8310p;
        if (weakReference == null || weakReference.get() == null) {
            return;
        }
        View view = this.f8310p.get();
        WeakReference<FrameLayout> weakReference2 = this.f8311q;
        A(view, weakReference2 != null ? weakReference2.get() : null);
    }

    private void r() {
        this.f8301g.getTextPaint().setColor(this.f8303i.g());
        invalidateSelf();
    }

    private void s() {
        C();
        this.f8301g.setTextWidthDirty(true);
        B();
        invalidateSelf();
    }

    private void t() {
        this.f8301g.setTextWidthDirty(true);
        B();
        invalidateSelf();
    }

    private void u() {
        boolean t7 = this.f8303i.t();
        setVisible(t7, false);
        if (!BadgeUtils.f8315a || g() == null || t7) {
            return;
        }
        ((ViewGroup) g().getParent()).invalidate();
    }

    private void v() {
        s();
        t();
        o();
        p();
        r();
        q();
        B();
        u();
    }

    private void w(TextAppearance textAppearance) {
        Context context;
        if (this.f8301g.getTextAppearance() == textAppearance || (context = this.f8299e.get()) == null) {
            return;
        }
        this.f8301g.setTextAppearance(textAppearance, context);
        B();
    }

    private void x(int i10) {
        Context context = this.f8299e.get();
        if (context == null) {
            return;
        }
        w(new TextAppearance(context, i10));
    }

    private void y(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup == null || viewGroup.getId() != R$id.mtrl_anchor_parent) {
            WeakReference<FrameLayout> weakReference = this.f8311q;
            if (weakReference == null || weakReference.get() != viewGroup) {
                z(view);
                FrameLayout frameLayout = new FrameLayout(view.getContext());
                frameLayout.setId(R$id.mtrl_anchor_parent);
                frameLayout.setClipChildren(false);
                frameLayout.setClipToPadding(false);
                frameLayout.setLayoutParams(view.getLayoutParams());
                frameLayout.setMinimumWidth(view.getWidth());
                frameLayout.setMinimumHeight(view.getHeight());
                int indexOfChild = viewGroup.indexOfChild(view);
                viewGroup.removeViewAt(indexOfChild);
                view.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                frameLayout.addView(view);
                viewGroup.addView(frameLayout, indexOfChild);
                this.f8311q = new WeakReference<>(frameLayout);
                frameLayout.post(new a(view, frameLayout));
            }
        }
    }

    private static void z(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        viewGroup.setClipChildren(false);
        viewGroup.setClipToPadding(false);
    }

    public void A(View view, FrameLayout frameLayout) {
        this.f8310p = new WeakReference<>(view);
        boolean z10 = BadgeUtils.f8315a;
        if (z10 && frameLayout == null) {
            y(view);
        } else {
            this.f8311q = new WeakReference<>(frameLayout);
        }
        if (!z10) {
            z(view);
        }
        B();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (getBounds().isEmpty() || getAlpha() == 0 || !isVisible()) {
            return;
        }
        this.f8300f.draw(canvas);
        if (n()) {
            d(canvas);
        }
    }

    public CharSequence f() {
        Context context;
        if (!isVisible()) {
            return null;
        }
        if (n()) {
            if (this.f8303i.j() == 0 || (context = this.f8299e.get()) == null) {
                return null;
            }
            if (j() <= this.f8306l) {
                return context.getResources().getQuantityString(this.f8303i.j(), j(), Integer.valueOf(j()));
            }
            return context.getString(this.f8303i.h(), Integer.valueOf(this.f8306l));
        }
        return this.f8303i.i();
    }

    public FrameLayout g() {
        WeakReference<FrameLayout> weakReference = this.f8311q;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f8303i.d();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f8302h.height();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f8302h.width();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public int h() {
        return this.f8303i.l();
    }

    public int i() {
        return this.f8303i.m();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return false;
    }

    public int j() {
        if (n()) {
            return this.f8303i.n();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BadgeState.State k() {
        return this.f8303i.p();
    }

    public boolean n() {
        return this.f8303i.s();
    }

    @Override // android.graphics.drawable.Drawable, com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public boolean onStateChange(int[] iArr) {
        return super.onStateChange(iArr);
    }

    @Override // com.google.android.material.internal.TextDrawableHelper.TextDrawableDelegate
    public void onTextSizeChange() {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i10) {
        this.f8303i.v(i10);
        o();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }
}
