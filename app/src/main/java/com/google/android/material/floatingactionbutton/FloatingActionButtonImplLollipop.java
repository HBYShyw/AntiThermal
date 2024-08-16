package com.google.android.material.floatingactionbutton;

import a4.RippleUtils;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.Property;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.util.Preconditions;
import b4.ShadowViewDelegate;
import c4.MaterialShapeDrawable;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$color;
import java.util.ArrayList;

/* compiled from: FloatingActionButtonImplLollipop.java */
/* renamed from: com.google.android.material.floatingactionbutton.e, reason: use source file name */
/* loaded from: classes.dex */
class FloatingActionButtonImplLollipop extends FloatingActionButtonImpl {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: FloatingActionButtonImplLollipop.java */
    /* renamed from: com.google.android.material.floatingactionbutton.e$a */
    /* loaded from: classes.dex */
    public static class a extends MaterialShapeDrawable {
        a(ShapeAppearanceModel shapeAppearanceModel) {
            super(shapeAppearanceModel);
        }

        @Override // c4.MaterialShapeDrawable, android.graphics.drawable.Drawable
        public boolean isStateful() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FloatingActionButtonImplLollipop(FloatingActionButton floatingActionButton, ShadowViewDelegate shadowViewDelegate) {
        super(floatingActionButton, shadowViewDelegate);
    }

    private Animator i0(float f10, float f11) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ObjectAnimator.ofFloat(this.f8891w, "elevation", f10).setDuration(0L)).with(ObjectAnimator.ofFloat(this.f8891w, (Property<FloatingActionButton, Float>) View.TRANSLATION_Z, f11).setDuration(100L));
        animatorSet.setInterpolator(FloatingActionButtonImpl.D);
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    public void B() {
        e0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    public void D(int[] iArr) {
    }

    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    void E(float f10, float f11, float f12) {
        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(FloatingActionButtonImpl.E, i0(f10, f12));
        stateListAnimator.addState(FloatingActionButtonImpl.F, i0(f10, f11));
        stateListAnimator.addState(FloatingActionButtonImpl.G, i0(f10, f11));
        stateListAnimator.addState(FloatingActionButtonImpl.H, i0(f10, f11));
        AnimatorSet animatorSet = new AnimatorSet();
        ArrayList arrayList = new ArrayList();
        arrayList.add(ObjectAnimator.ofFloat(this.f8891w, "elevation", f10).setDuration(0L));
        arrayList.add(ObjectAnimator.ofFloat(this.f8891w, (Property<FloatingActionButton, Float>) View.TRANSLATION_Z, 0.0f).setDuration(100L));
        animatorSet.playSequentially((Animator[]) arrayList.toArray(new Animator[0]));
        animatorSet.setInterpolator(FloatingActionButtonImpl.D);
        stateListAnimator.addState(FloatingActionButtonImpl.I, animatorSet);
        stateListAnimator.addState(FloatingActionButtonImpl.J, i0(0.0f, 0.0f));
        this.f8891w.setStateListAnimator(stateListAnimator);
        if (Y()) {
            e0();
        }
    }

    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    boolean J() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    public void U(ColorStateList colorStateList) {
        Drawable drawable = this.f8871c;
        if (drawable instanceof RippleDrawable) {
            ((RippleDrawable) drawable).setColor(RippleUtils.d(colorStateList));
        } else {
            super.U(colorStateList);
        }
    }

    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    boolean Y() {
        return this.f8892x.b() || !a0();
    }

    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    void c0() {
    }

    BorderDrawable h0(int i10, ColorStateList colorStateList) {
        Context context = this.f8891w.getContext();
        BorderDrawable borderDrawable = new BorderDrawable((ShapeAppearanceModel) Preconditions.d(this.f8869a));
        borderDrawable.e(ContextCompat.c(context, R$color.design_fab_stroke_top_outer_color), ContextCompat.c(context, R$color.design_fab_stroke_top_inner_color), ContextCompat.c(context, R$color.design_fab_stroke_end_inner_color), ContextCompat.c(context, R$color.design_fab_stroke_end_outer_color));
        borderDrawable.d(i10);
        borderDrawable.c(colorStateList);
        return borderDrawable;
    }

    MaterialShapeDrawable j0() {
        return new a((ShapeAppearanceModel) Preconditions.d(this.f8869a));
    }

    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    public float m() {
        return this.f8891w.getElevation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    public void r(Rect rect) {
        if (this.f8892x.b()) {
            super.r(rect);
        } else if (!a0()) {
            int sizeDimension = (this.f8879k - this.f8891w.getSizeDimension()) / 2;
            rect.set(sizeDimension, sizeDimension, sizeDimension, sizeDimension);
        } else {
            rect.set(0, 0, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    public void w(ColorStateList colorStateList, PorterDuff.Mode mode, ColorStateList colorStateList2, int i10) {
        Drawable drawable;
        MaterialShapeDrawable j02 = j0();
        this.f8870b = j02;
        j02.setTintList(colorStateList);
        if (mode != null) {
            this.f8870b.setTintMode(mode);
        }
        this.f8870b.P(this.f8891w.getContext());
        if (i10 > 0) {
            this.f8872d = h0(i10, colorStateList);
            drawable = new LayerDrawable(new Drawable[]{(Drawable) Preconditions.d(this.f8872d), (Drawable) Preconditions.d(this.f8870b)});
        } else {
            this.f8872d = null;
            drawable = this.f8870b;
        }
        RippleDrawable rippleDrawable = new RippleDrawable(RippleUtils.d(colorStateList2), drawable, null);
        this.f8871c = rippleDrawable;
        this.f8873e = rippleDrawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.floatingactionbutton.FloatingActionButtonImpl
    public void z() {
    }
}
