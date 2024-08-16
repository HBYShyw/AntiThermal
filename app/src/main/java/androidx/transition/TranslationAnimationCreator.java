package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.util.Property;
import android.view.View;
import androidx.transition.Transition;

/* compiled from: TranslationAnimationCreator.java */
/* renamed from: androidx.transition.x, reason: use source file name */
/* loaded from: classes.dex */
class TranslationAnimationCreator {

    /* compiled from: TranslationAnimationCreator.java */
    /* renamed from: androidx.transition.x$a */
    /* loaded from: classes.dex */
    private static class a extends AnimatorListenerAdapter implements Transition.g {

        /* renamed from: a, reason: collision with root package name */
        private final View f4159a;

        /* renamed from: b, reason: collision with root package name */
        private final View f4160b;

        /* renamed from: c, reason: collision with root package name */
        private final int f4161c;

        /* renamed from: d, reason: collision with root package name */
        private final int f4162d;

        /* renamed from: e, reason: collision with root package name */
        private int[] f4163e;

        /* renamed from: f, reason: collision with root package name */
        private float f4164f;

        /* renamed from: g, reason: collision with root package name */
        private float f4165g;

        /* renamed from: h, reason: collision with root package name */
        private final float f4166h;

        /* renamed from: i, reason: collision with root package name */
        private final float f4167i;

        a(View view, View view2, int i10, int i11, float f10, float f11) {
            this.f4160b = view;
            this.f4159a = view2;
            this.f4161c = i10 - Math.round(view.getTranslationX());
            this.f4162d = i11 - Math.round(view.getTranslationY());
            this.f4166h = f10;
            this.f4167i = f11;
            int i12 = R$id.transition_position;
            int[] iArr = (int[]) view2.getTag(i12);
            this.f4163e = iArr;
            if (iArr != null) {
                view2.setTag(i12, null);
            }
        }

        @Override // androidx.transition.Transition.g
        public void a(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void b(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            this.f4160b.setTranslationX(this.f4166h);
            this.f4160b.setTranslationY(this.f4167i);
            transition.removeListener(this);
        }

        @Override // androidx.transition.Transition.g
        public void d(Transition transition) {
        }

        @Override // androidx.transition.Transition.g
        public void e(Transition transition) {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.f4163e == null) {
                this.f4163e = new int[2];
            }
            this.f4163e[0] = Math.round(this.f4161c + this.f4160b.getTranslationX());
            this.f4163e[1] = Math.round(this.f4162d + this.f4160b.getTranslationY());
            this.f4159a.setTag(R$id.transition_position, this.f4163e);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationPause(Animator animator) {
            this.f4164f = this.f4160b.getTranslationX();
            this.f4165g = this.f4160b.getTranslationY();
            this.f4160b.setTranslationX(this.f4166h);
            this.f4160b.setTranslationY(this.f4167i);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationResume(Animator animator) {
            this.f4160b.setTranslationX(this.f4164f);
            this.f4160b.setTranslationY(this.f4165g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Animator a(View view, TransitionValues transitionValues, int i10, int i11, float f10, float f11, float f12, float f13, TimeInterpolator timeInterpolator, Transition transition) {
        float f14;
        float f15;
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        if (((int[]) transitionValues.f4153b.getTag(R$id.transition_position)) != null) {
            f14 = (r4[0] - i10) + translationX;
            f15 = (r4[1] - i11) + translationY;
        } else {
            f14 = f10;
            f15 = f11;
        }
        int round = i10 + Math.round(f14 - translationX);
        int round2 = i11 + Math.round(f15 - translationY);
        view.setTranslationX(f14);
        view.setTranslationY(f15);
        if (f14 == f12 && f15 == f13) {
            return null;
        }
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_X, f14, f12), PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, f15, f13));
        a aVar = new a(view, transitionValues.f4153b, round, round2, translationX, translationY);
        transition.addListener(aVar);
        ofPropertyValuesHolder.addListener(aVar);
        AnimatorUtils.a(ofPropertyValuesHolder, aVar);
        ofPropertyValuesHolder.setInterpolator(timeInterpolator);
        return ofPropertyValuesHolder;
    }
}
