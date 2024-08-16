package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class ChangeClipBounds extends Transition {

    /* renamed from: e, reason: collision with root package name */
    private static final String[] f3999e = {"android:clipBounds:clip"};

    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f4000a;

        a(View view) {
            this.f4000a = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ViewCompat.s0(this.f4000a, null);
        }
    }

    public ChangeClipBounds() {
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.f4153b;
        if (view.getVisibility() == 8) {
            return;
        }
        Rect r10 = ViewCompat.r(view);
        transitionValues.f4152a.put("android:clipBounds:clip", r10);
        if (r10 == null) {
            transitionValues.f4152a.put("android:clipBounds:bounds", new Rect(0, 0, view.getWidth(), view.getHeight()));
        }
    }

    @Override // androidx.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        ObjectAnimator objectAnimator = null;
        if (transitionValues != null && transitionValues2 != null && transitionValues.f4152a.containsKey("android:clipBounds:clip") && transitionValues2.f4152a.containsKey("android:clipBounds:clip")) {
            Rect rect = (Rect) transitionValues.f4152a.get("android:clipBounds:clip");
            Rect rect2 = (Rect) transitionValues2.f4152a.get("android:clipBounds:clip");
            boolean z10 = rect2 == null;
            if (rect == null && rect2 == null) {
                return null;
            }
            if (rect == null) {
                rect = (Rect) transitionValues.f4152a.get("android:clipBounds:bounds");
            } else if (rect2 == null) {
                rect2 = (Rect) transitionValues2.f4152a.get("android:clipBounds:bounds");
            }
            if (rect.equals(rect2)) {
                return null;
            }
            ViewCompat.s0(transitionValues2.f4153b, rect);
            objectAnimator = ObjectAnimator.ofObject(transitionValues2.f4153b, (Property<View, V>) d0.f4096c, (TypeEvaluator) new RectEvaluator(new Rect()), (Object[]) new Rect[]{rect, rect2});
            if (z10) {
                objectAnimator.addListener(new a(transitionValues2.f4153b));
            }
        }
        return objectAnimator;
    }

    @Override // androidx.transition.Transition
    public String[] getTransitionProperties() {
        return f3999e;
    }

    public ChangeClipBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
