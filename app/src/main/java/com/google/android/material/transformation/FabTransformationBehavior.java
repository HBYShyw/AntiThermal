package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$id;
import com.google.android.material.circularreveal.CircularRevealCompat;
import com.google.android.material.circularreveal.CircularRevealHelper;
import com.google.android.material.circularreveal.CircularRevealWidget;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import p3.AnimationUtils;
import p3.AnimatorSetCompat;
import p3.ArgbEvaluatorCompat;
import p3.ChildrenAlphaProperty;
import p3.DrawableAlphaProperty;
import p3.MotionSpec;
import p3.MotionTiming;
import p3.Positioning;

@Deprecated
/* loaded from: classes.dex */
public abstract class FabTransformationBehavior extends ExpandableTransformationBehavior {

    /* renamed from: c, reason: collision with root package name */
    private final Rect f9538c;

    /* renamed from: d, reason: collision with root package name */
    private final RectF f9539d;

    /* renamed from: e, reason: collision with root package name */
    private final RectF f9540e;

    /* renamed from: f, reason: collision with root package name */
    private final int[] f9541f;

    /* renamed from: g, reason: collision with root package name */
    private float f9542g;

    /* renamed from: h, reason: collision with root package name */
    private float f9543h;

    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ boolean f9544a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f9545b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f9546c;

        a(boolean z10, View view, View view2) {
            this.f9544a = z10;
            this.f9545b = view;
            this.f9546c = view2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f9544a) {
                return;
            }
            this.f9545b.setVisibility(4);
            this.f9546c.setAlpha(1.0f);
            this.f9546c.setVisibility(0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.f9544a) {
                this.f9545b.setVisibility(0);
                this.f9546c.setAlpha(0.0f);
                this.f9546c.setVisibility(4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f9548a;

        b(View view) {
            this.f9548a = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.f9548a.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ CircularRevealWidget f9550a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ Drawable f9551b;

        c(CircularRevealWidget circularRevealWidget, Drawable drawable) {
            this.f9550a = circularRevealWidget;
            this.f9551b = drawable;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f9550a.setCircularRevealOverlayDrawable(null);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f9550a.setCircularRevealOverlayDrawable(this.f9551b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ CircularRevealWidget f9553a;

        d(CircularRevealWidget circularRevealWidget) {
            this.f9553a = circularRevealWidget;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            CircularRevealWidget.e revealInfo = this.f9553a.getRevealInfo();
            revealInfo.f8637c = Float.MAX_VALUE;
            this.f9553a.setRevealInfo(revealInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        public MotionSpec f9555a;

        /* renamed from: b, reason: collision with root package name */
        public Positioning f9556b;
    }

    public FabTransformationBehavior() {
        this.f9538c = new Rect();
        this.f9539d = new RectF();
        this.f9540e = new RectF();
        this.f9541f = new int[2];
    }

    private void A(View view, long j10, int i10, int i11, float f10, List<Animator> list) {
        if (j10 > 0) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view, i10, i11, f10, f10);
            createCircularReveal.setStartDelay(0L);
            createCircularReveal.setDuration(j10);
            list.add(createCircularReveal);
        }
    }

    private void B(View view, View view2, boolean z10, boolean z11, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2, RectF rectF) {
        ObjectAnimator ofFloat;
        ObjectAnimator ofFloat2;
        float p10 = p(view, view2, eVar.f9556b);
        float q10 = q(view, view2, eVar.f9556b);
        Pair<MotionTiming, MotionTiming> m10 = m(p10, q10, z10, eVar);
        MotionTiming motionTiming = (MotionTiming) m10.first;
        MotionTiming motionTiming2 = (MotionTiming) m10.second;
        if (z10) {
            if (!z11) {
                view2.setTranslationX(-p10);
                view2.setTranslationY(-q10);
            }
            ofFloat = ObjectAnimator.ofFloat(view2, (Property<View, Float>) View.TRANSLATION_X, 0.0f);
            ofFloat2 = ObjectAnimator.ofFloat(view2, (Property<View, Float>) View.TRANSLATION_Y, 0.0f);
            k(view2, eVar, motionTiming, motionTiming2, -p10, -q10, 0.0f, 0.0f, rectF);
        } else {
            ofFloat = ObjectAnimator.ofFloat(view2, (Property<View, Float>) View.TRANSLATION_X, -p10);
            ofFloat2 = ObjectAnimator.ofFloat(view2, (Property<View, Float>) View.TRANSLATION_Y, -q10);
        }
        motionTiming.a(ofFloat);
        motionTiming2.a(ofFloat2);
        list.add(ofFloat);
        list.add(ofFloat2);
    }

    private int C(View view) {
        ColorStateList p10 = ViewCompat.p(view);
        if (p10 != null) {
            return p10.getColorForState(view.getDrawableState(), p10.getDefaultColor());
        }
        return 0;
    }

    private ViewGroup E(View view) {
        if (view instanceof ViewGroup) {
            return (ViewGroup) view;
        }
        return null;
    }

    private ViewGroup j(View view) {
        View findViewById = view.findViewById(R$id.mtrl_child_content_container);
        if (findViewById != null) {
            return E(findViewById);
        }
        if (!(view instanceof TransformationChildLayout) && !(view instanceof TransformationChildCard)) {
            return E(view);
        }
        return E(((ViewGroup) view).getChildAt(0));
    }

    private void k(View view, e eVar, MotionTiming motionTiming, MotionTiming motionTiming2, float f10, float f11, float f12, float f13, RectF rectF) {
        float r10 = r(eVar, motionTiming, f10, f12);
        float r11 = r(eVar, motionTiming2, f11, f13);
        Rect rect = this.f9538c;
        view.getWindowVisibleDisplayFrame(rect);
        RectF rectF2 = this.f9539d;
        rectF2.set(rect);
        RectF rectF3 = this.f9540e;
        s(view, rectF3);
        rectF3.offset(r10, r11);
        rectF3.intersect(rectF2);
        rectF.set(rectF3);
    }

    private void l(View view, RectF rectF) {
        s(view, rectF);
        rectF.offset(this.f9542g, this.f9543h);
    }

    private Pair<MotionTiming, MotionTiming> m(float f10, float f11, boolean z10, e eVar) {
        MotionTiming h10;
        MotionTiming h11;
        if (f10 == 0.0f || f11 == 0.0f) {
            h10 = eVar.f9555a.h("translationXLinear");
            h11 = eVar.f9555a.h("translationYLinear");
        } else if ((z10 && f11 < 0.0f) || (!z10 && f11 > 0.0f)) {
            h10 = eVar.f9555a.h("translationXCurveUpwards");
            h11 = eVar.f9555a.h("translationYCurveUpwards");
        } else {
            h10 = eVar.f9555a.h("translationXCurveDownwards");
            h11 = eVar.f9555a.h("translationYCurveDownwards");
        }
        return new Pair<>(h10, h11);
    }

    private float n(View view, View view2, Positioning positioning) {
        RectF rectF = this.f9539d;
        RectF rectF2 = this.f9540e;
        l(view, rectF);
        s(view2, rectF2);
        rectF2.offset(-p(view, view2, positioning), 0.0f);
        return rectF.centerX() - rectF2.left;
    }

    private float o(View view, View view2, Positioning positioning) {
        RectF rectF = this.f9539d;
        RectF rectF2 = this.f9540e;
        l(view, rectF);
        s(view2, rectF2);
        rectF2.offset(0.0f, -q(view, view2, positioning));
        return rectF.centerY() - rectF2.top;
    }

    private float p(View view, View view2, Positioning positioning) {
        float centerX;
        float centerX2;
        float f10;
        RectF rectF = this.f9539d;
        RectF rectF2 = this.f9540e;
        l(view, rectF);
        s(view2, rectF2);
        int i10 = positioning.f16575a & 7;
        if (i10 == 1) {
            centerX = rectF2.centerX();
            centerX2 = rectF.centerX();
        } else if (i10 == 3) {
            centerX = rectF2.left;
            centerX2 = rectF.left;
        } else if (i10 == 5) {
            centerX = rectF2.right;
            centerX2 = rectF.right;
        } else {
            f10 = 0.0f;
            return f10 + positioning.f16576b;
        }
        f10 = centerX - centerX2;
        return f10 + positioning.f16576b;
    }

    private float q(View view, View view2, Positioning positioning) {
        float centerY;
        float centerY2;
        float f10;
        RectF rectF = this.f9539d;
        RectF rectF2 = this.f9540e;
        l(view, rectF);
        s(view2, rectF2);
        int i10 = positioning.f16575a & 112;
        if (i10 == 16) {
            centerY = rectF2.centerY();
            centerY2 = rectF.centerY();
        } else if (i10 == 48) {
            centerY = rectF2.top;
            centerY2 = rectF.top;
        } else if (i10 == 80) {
            centerY = rectF2.bottom;
            centerY2 = rectF.bottom;
        } else {
            f10 = 0.0f;
            return f10 + positioning.f16577c;
        }
        f10 = centerY - centerY2;
        return f10 + positioning.f16577c;
    }

    private float r(e eVar, MotionTiming motionTiming, float f10, float f11) {
        long c10 = motionTiming.c();
        long d10 = motionTiming.d();
        MotionTiming h10 = eVar.f9555a.h("expansion");
        return AnimationUtils.a(f10, f11, motionTiming.e().getInterpolation(((float) (((h10.c() + h10.d()) + 17) - c10)) / ((float) d10)));
    }

    private void s(View view, RectF rectF) {
        rectF.set(0.0f, 0.0f, view.getWidth(), view.getHeight());
        view.getLocationInWindow(this.f9541f);
        rectF.offsetTo(r3[0], r3[1]);
        rectF.offset((int) (-view.getTranslationX()), (int) (-view.getTranslationY()));
    }

    private void t(View view, View view2, boolean z10, boolean z11, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ViewGroup j10;
        ObjectAnimator ofFloat;
        if (view2 instanceof ViewGroup) {
            if (((view2 instanceof CircularRevealWidget) && CircularRevealHelper.f8621j == 0) || (j10 = j(view2)) == null) {
                return;
            }
            if (z10) {
                if (!z11) {
                    ChildrenAlphaProperty.f16561a.set(j10, Float.valueOf(0.0f));
                }
                ofFloat = ObjectAnimator.ofFloat(j10, ChildrenAlphaProperty.f16561a, 1.0f);
            } else {
                ofFloat = ObjectAnimator.ofFloat(j10, ChildrenAlphaProperty.f16561a, 0.0f);
            }
            eVar.f9555a.h("contentFade").a(ofFloat);
            list.add(ofFloat);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void u(View view, View view2, boolean z10, boolean z11, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator ofInt;
        if (view2 instanceof CircularRevealWidget) {
            CircularRevealWidget circularRevealWidget = (CircularRevealWidget) view2;
            int C = C(view);
            int i10 = 16777215 & C;
            if (z10) {
                if (!z11) {
                    circularRevealWidget.setCircularRevealScrimColor(C);
                }
                ofInt = ObjectAnimator.ofInt(circularRevealWidget, CircularRevealWidget.d.f8634a, i10);
            } else {
                ofInt = ObjectAnimator.ofInt(circularRevealWidget, CircularRevealWidget.d.f8634a, C);
            }
            ofInt.setEvaluator(ArgbEvaluatorCompat.b());
            eVar.f9555a.h("color").a(ofInt);
            list.add(ofInt);
        }
    }

    private void v(View view, View view2, boolean z10, e eVar, List<Animator> list) {
        float p10 = p(view, view2, eVar.f9556b);
        float q10 = q(view, view2, eVar.f9556b);
        Pair<MotionTiming, MotionTiming> m10 = m(p10, q10, z10, eVar);
        MotionTiming motionTiming = (MotionTiming) m10.first;
        MotionTiming motionTiming2 = (MotionTiming) m10.second;
        Property property = View.TRANSLATION_X;
        float[] fArr = new float[1];
        if (!z10) {
            p10 = this.f9542g;
        }
        fArr[0] = p10;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, (Property<View, Float>) property, fArr);
        Property property2 = View.TRANSLATION_Y;
        float[] fArr2 = new float[1];
        if (!z10) {
            q10 = this.f9543h;
        }
        fArr2[0] = q10;
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, (Property<View, Float>) property2, fArr2);
        motionTiming.a(ofFloat);
        motionTiming2.a(ofFloat2);
        list.add(ofFloat);
        list.add(ofFloat2);
    }

    @TargetApi(21)
    private void w(View view, View view2, boolean z10, boolean z11, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator ofFloat;
        float t7 = ViewCompat.t(view2) - ViewCompat.t(view);
        if (z10) {
            if (!z11) {
                view2.setTranslationZ(-t7);
            }
            ofFloat = ObjectAnimator.ofFloat(view2, (Property<View, Float>) View.TRANSLATION_Z, 0.0f);
        } else {
            ofFloat = ObjectAnimator.ofFloat(view2, (Property<View, Float>) View.TRANSLATION_Z, -t7);
        }
        eVar.f9555a.h("elevation").a(ofFloat);
        list.add(ofFloat);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void x(View view, View view2, boolean z10, boolean z11, e eVar, float f10, float f11, List<Animator> list, List<Animator.AnimatorListener> list2) {
        Animator animator;
        if (view2 instanceof CircularRevealWidget) {
            CircularRevealWidget circularRevealWidget = (CircularRevealWidget) view2;
            float n10 = n(view, view2, eVar.f9556b);
            float o10 = o(view, view2, eVar.f9556b);
            ((FloatingActionButton) view).h(this.f9538c);
            float width = this.f9538c.width() / 2.0f;
            MotionTiming h10 = eVar.f9555a.h("expansion");
            if (z10) {
                if (!z11) {
                    circularRevealWidget.setRevealInfo(new CircularRevealWidget.e(n10, o10, width));
                }
                if (z11) {
                    width = circularRevealWidget.getRevealInfo().f8637c;
                }
                animator = CircularRevealCompat.a(circularRevealWidget, n10, o10, w3.a.b(n10, o10, 0.0f, 0.0f, f10, f11));
                animator.addListener(new d(circularRevealWidget));
                A(view2, h10.c(), (int) n10, (int) o10, width, list);
            } else {
                float f12 = circularRevealWidget.getRevealInfo().f8637c;
                Animator a10 = CircularRevealCompat.a(circularRevealWidget, n10, o10, width);
                int i10 = (int) n10;
                int i11 = (int) o10;
                A(view2, h10.c(), i10, i11, f12, list);
                z(view2, h10.c(), h10.d(), eVar.f9555a.i(), i10, i11, width, list);
                animator = a10;
            }
            h10.a(animator);
            list.add(animator);
            list2.add(CircularRevealCompat.b(circularRevealWidget));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void y(View view, View view2, boolean z10, boolean z11, e eVar, List<Animator> list, List<Animator.AnimatorListener> list2) {
        ObjectAnimator ofInt;
        if ((view2 instanceof CircularRevealWidget) && (view instanceof ImageView)) {
            CircularRevealWidget circularRevealWidget = (CircularRevealWidget) view2;
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable == null) {
                return;
            }
            drawable.mutate();
            if (z10) {
                if (!z11) {
                    drawable.setAlpha(255);
                }
                ofInt = ObjectAnimator.ofInt(drawable, DrawableAlphaProperty.f16562b, 0);
            } else {
                ofInt = ObjectAnimator.ofInt(drawable, DrawableAlphaProperty.f16562b, 255);
            }
            ofInt.addUpdateListener(new b(view2));
            eVar.f9555a.h("iconFade").a(ofInt);
            list.add(ofInt);
            list2.add(new c(circularRevealWidget, drawable));
        }
    }

    private void z(View view, long j10, long j11, long j12, int i10, int i11, float f10, List<Animator> list) {
        long j13 = j10 + j11;
        if (j13 < j12) {
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(view, i10, i11, f10, f10);
            createCircularReveal.setStartDelay(j13);
            createCircularReveal.setDuration(j12 - j13);
            list.add(createCircularReveal);
        }
    }

    protected abstract e D(Context context, boolean z10);

    @Override // com.google.android.material.transformation.ExpandableTransformationBehavior
    protected AnimatorSet i(View view, View view2, boolean z10, boolean z11) {
        e D = D(view2.getContext(), z10);
        if (z10) {
            this.f9542g = view.getTranslationX();
            this.f9543h = view.getTranslationY();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        w(view, view2, z10, z11, D, arrayList, arrayList2);
        RectF rectF = this.f9539d;
        B(view, view2, z10, z11, D, arrayList, arrayList2, rectF);
        float width = rectF.width();
        float height = rectF.height();
        v(view, view2, z10, D, arrayList);
        y(view, view2, z10, z11, D, arrayList, arrayList2);
        x(view, view2, z10, z11, D, width, height, arrayList, arrayList2);
        u(view, view2, z10, z11, D, arrayList, arrayList2);
        t(view, view2, z10, z11, D, arrayList, arrayList2);
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSetCompat.a(animatorSet, arrayList);
        animatorSet.addListener(new a(z10, view2, view));
        int size = arrayList2.size();
        for (int i10 = 0; i10 < size; i10++) {
            animatorSet.addListener(arrayList2.get(i10));
        }
        return animatorSet;
    }

    @Override // com.google.android.material.transformation.ExpandableBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
        if (view.getVisibility() != 8) {
            if (!(view2 instanceof FloatingActionButton)) {
                return false;
            }
            int expandedComponentIdHint = ((FloatingActionButton) view2).getExpandedComponentIdHint();
            return expandedComponentIdHint == 0 || expandedComponentIdHint == view.getId();
        }
        throw new IllegalStateException("This behavior cannot be attached to a GONE view. Set the view to INVISIBLE instead.");
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
        if (eVar.f2069h == 0) {
            eVar.f2069h = 80;
        }
    }

    public FabTransformationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f9538c = new Rect();
        this.f9539d = new RectF();
        this.f9540e = new RectF();
        this.f9541f = new int[2];
    }
}
