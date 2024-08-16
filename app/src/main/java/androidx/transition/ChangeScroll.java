package androidx.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class ChangeScroll extends Transition {

    /* renamed from: e, reason: collision with root package name */
    private static final String[] f4006e = {"android:changeScroll:x", "android:changeScroll:y"};

    public ChangeScroll() {
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.f4152a.put("android:changeScroll:x", Integer.valueOf(transitionValues.f4153b.getScrollX()));
        transitionValues.f4152a.put("android:changeScroll:y", Integer.valueOf(transitionValues.f4153b.getScrollY()));
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
        ObjectAnimator objectAnimator;
        ObjectAnimator objectAnimator2 = null;
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        View view = transitionValues2.f4153b;
        int intValue = ((Integer) transitionValues.f4152a.get("android:changeScroll:x")).intValue();
        int intValue2 = ((Integer) transitionValues2.f4152a.get("android:changeScroll:x")).intValue();
        int intValue3 = ((Integer) transitionValues.f4152a.get("android:changeScroll:y")).intValue();
        int intValue4 = ((Integer) transitionValues2.f4152a.get("android:changeScroll:y")).intValue();
        if (intValue != intValue2) {
            view.setScrollX(intValue);
            objectAnimator = ObjectAnimator.ofInt(view, "scrollX", intValue, intValue2);
        } else {
            objectAnimator = null;
        }
        if (intValue3 != intValue4) {
            view.setScrollY(intValue3);
            objectAnimator2 = ObjectAnimator.ofInt(view, "scrollY", intValue3, intValue4);
        }
        return TransitionUtils.c(objectAnimator, objectAnimator2);
    }

    @Override // androidx.transition.Transition
    public String[] getTransitionProperties() {
        return f4006e;
    }

    public ChangeScroll(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
