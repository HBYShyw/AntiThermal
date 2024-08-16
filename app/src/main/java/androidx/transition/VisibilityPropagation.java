package androidx.transition;

import android.view.View;

/* compiled from: VisibilityPropagation.java */
/* renamed from: androidx.transition.k0, reason: use source file name */
/* loaded from: classes.dex */
public abstract class VisibilityPropagation extends TransitionPropagation {

    /* renamed from: a, reason: collision with root package name */
    private static final String[] f4121a = {"android:visibilityPropagation:visibility", "android:visibilityPropagation:center"};

    private static int d(TransitionValues transitionValues, int i10) {
        int[] iArr;
        if (transitionValues == null || (iArr = (int[]) transitionValues.f4152a.get("android:visibilityPropagation:center")) == null) {
            return -1;
        }
        return iArr[i10];
    }

    @Override // androidx.transition.TransitionPropagation
    public void a(TransitionValues transitionValues) {
        View view = transitionValues.f4153b;
        Integer num = (Integer) transitionValues.f4152a.get("android:visibility:visibility");
        if (num == null) {
            num = Integer.valueOf(view.getVisibility());
        }
        transitionValues.f4152a.put("android:visibilityPropagation:visibility", num);
        view.getLocationOnScreen(r1);
        int[] iArr = {iArr[0] + Math.round(view.getTranslationX())};
        iArr[0] = iArr[0] + (view.getWidth() / 2);
        iArr[1] = iArr[1] + Math.round(view.getTranslationY());
        iArr[1] = iArr[1] + (view.getHeight() / 2);
        transitionValues.f4152a.put("android:visibilityPropagation:center", iArr);
    }

    @Override // androidx.transition.TransitionPropagation
    public String[] b() {
        return f4121a;
    }

    public int e(TransitionValues transitionValues) {
        Integer num;
        if (transitionValues == null || (num = (Integer) transitionValues.f4152a.get("android:visibilityPropagation:visibility")) == null) {
            return 8;
        }
        return num.intValue();
    }

    public int f(TransitionValues transitionValues) {
        return d(transitionValues, 0);
    }

    public int g(TransitionValues transitionValues) {
        return d(transitionValues, 1);
    }
}
