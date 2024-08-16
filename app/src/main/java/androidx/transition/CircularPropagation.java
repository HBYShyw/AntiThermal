package androidx.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

/* compiled from: CircularPropagation.java */
/* renamed from: androidx.transition.c, reason: use source file name */
/* loaded from: classes.dex */
public class CircularPropagation extends VisibilityPropagation {

    /* renamed from: b, reason: collision with root package name */
    private float f4092b = 3.0f;

    private static float h(float f10, float f11, float f12, float f13) {
        float f14 = f12 - f10;
        float f15 = f13 - f11;
        return (float) Math.sqrt((f14 * f14) + (f15 * f15));
    }

    @Override // androidx.transition.TransitionPropagation
    public long c(ViewGroup viewGroup, Transition transition, TransitionValues transitionValues, TransitionValues transitionValues2) {
        int i10;
        int round;
        int i11;
        if (transitionValues == null && transitionValues2 == null) {
            return 0L;
        }
        if (transitionValues2 == null || e(transitionValues) == 0) {
            i10 = -1;
        } else {
            transitionValues = transitionValues2;
            i10 = 1;
        }
        int f10 = f(transitionValues);
        int g6 = g(transitionValues);
        Rect epicenter = transition.getEpicenter();
        if (epicenter != null) {
            i11 = epicenter.centerX();
            round = epicenter.centerY();
        } else {
            viewGroup.getLocationOnScreen(new int[2]);
            int round2 = Math.round(r5[0] + (viewGroup.getWidth() / 2) + viewGroup.getTranslationX());
            round = Math.round(r5[1] + (viewGroup.getHeight() / 2) + viewGroup.getTranslationY());
            i11 = round2;
        }
        float h10 = h(f10, g6, i11, round) / h(0.0f, 0.0f, viewGroup.getWidth(), viewGroup.getHeight());
        long duration = transition.getDuration();
        if (duration < 0) {
            duration = 300;
        }
        return Math.round((((float) (duration * i10)) / this.f4092b) * h10);
    }
}
