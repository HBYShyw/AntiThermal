package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class Fade extends Visibility {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends TransitionListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f4040a;

        a(View view) {
            this.f4040a = view;
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            d0.h(this.f4040a, 1.0f);
            d0.a(this.f4040a);
            transition.removeListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class b extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private final View f4042a;

        /* renamed from: b, reason: collision with root package name */
        private boolean f4043b = false;

        b(View view) {
            this.f4042a = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            d0.h(this.f4042a, 1.0f);
            if (this.f4043b) {
                this.f4042a.setLayerType(0, null);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (ViewCompat.M(this.f4042a) && this.f4042a.getLayerType() == 0) {
                this.f4043b = true;
                this.f4042a.setLayerType(2, null);
            }
        }
    }

    public Fade(int i10) {
        g(i10);
    }

    private Animator h(View view, float f10, float f11) {
        if (f10 == f11) {
            return null;
        }
        d0.h(view, f10);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, d0.f4095b, f11);
        ofFloat.addListener(new b(view));
        addListener(new a(view));
        return ofFloat;
    }

    private static float i(TransitionValues transitionValues, float f10) {
        Float f11;
        return (transitionValues == null || (f11 = (Float) transitionValues.f4152a.get("android:fade:transitionAlpha")) == null) ? f10 : f11.floatValue();
    }

    @Override // androidx.transition.Visibility
    public Animator c(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        float i10 = i(transitionValues, 0.0f);
        return h(view, i10 != 1.0f ? i10 : 0.0f, 1.0f);
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        transitionValues.f4152a.put("android:fade:transitionAlpha", Float.valueOf(d0.c(transitionValues.f4153b)));
    }

    @Override // androidx.transition.Visibility
    public Animator e(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        d0.e(view);
        return h(view, i(transitionValues, 1.0f), 0.0f);
    }

    public Fade() {
    }

    @SuppressLint({"RestrictedApi"})
    public Fade(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4133f);
        g(TypedArrayUtils.g(obtainStyledAttributes, (XmlResourceParser) attributeSet, "fadingMode", 0, a()));
        obtainStyledAttributes.recycle();
    }
}
