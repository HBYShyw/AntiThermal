package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/* loaded from: classes.dex */
public class Explode extends Visibility {

    /* renamed from: h, reason: collision with root package name */
    private static final TimeInterpolator f4037h = new DecelerateInterpolator();

    /* renamed from: i, reason: collision with root package name */
    private static final TimeInterpolator f4038i = new AccelerateInterpolator();

    /* renamed from: g, reason: collision with root package name */
    private int[] f4039g;

    public Explode() {
        this.f4039g = new int[2];
        setPropagation(new CircularPropagation());
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.f4153b;
        view.getLocationOnScreen(this.f4039g);
        int[] iArr = this.f4039g;
        int i10 = iArr[0];
        int i11 = iArr[1];
        transitionValues.f4152a.put("android:explode:screenBounds", new Rect(i10, i11, view.getWidth() + i10, view.getHeight() + i11));
    }

    private static float h(float f10, float f11) {
        return (float) Math.sqrt((f10 * f10) + (f11 * f11));
    }

    private static float i(View view, int i10, int i11) {
        return h(Math.max(i10, view.getWidth() - i10), Math.max(i11, view.getHeight() - i11));
    }

    private void j(View view, Rect rect, int[] iArr) {
        int centerY;
        int i10;
        view.getLocationOnScreen(this.f4039g);
        int[] iArr2 = this.f4039g;
        int i11 = iArr2[0];
        int i12 = iArr2[1];
        Rect epicenter = getEpicenter();
        if (epicenter == null) {
            i10 = (view.getWidth() / 2) + i11 + Math.round(view.getTranslationX());
            centerY = (view.getHeight() / 2) + i12 + Math.round(view.getTranslationY());
        } else {
            int centerX = epicenter.centerX();
            centerY = epicenter.centerY();
            i10 = centerX;
        }
        float centerX2 = rect.centerX() - i10;
        float centerY2 = rect.centerY() - centerY;
        if (centerX2 == 0.0f && centerY2 == 0.0f) {
            centerX2 = ((float) (Math.random() * 2.0d)) - 1.0f;
            centerY2 = ((float) (Math.random() * 2.0d)) - 1.0f;
        }
        float h10 = h(centerX2, centerY2);
        float i13 = i(view, i10 - i11, centerY - i12);
        iArr[0] = Math.round((centerX2 / h10) * i13);
        iArr[1] = Math.round(i13 * (centerY2 / h10));
    }

    @Override // androidx.transition.Visibility
    public Animator c(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues2 == null) {
            return null;
        }
        Rect rect = (Rect) transitionValues2.f4152a.get("android:explode:screenBounds");
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        j(viewGroup, rect, this.f4039g);
        int[] iArr = this.f4039g;
        return TranslationAnimationCreator.a(view, transitionValues2, rect.left, rect.top, translationX + iArr[0], translationY + iArr[1], translationX, translationY, f4037h, this);
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Visibility
    public Animator e(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        float f10;
        float f11;
        if (transitionValues == null) {
            return null;
        }
        Rect rect = (Rect) transitionValues.f4152a.get("android:explode:screenBounds");
        int i10 = rect.left;
        int i11 = rect.top;
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        int[] iArr = (int[]) transitionValues.f4153b.getTag(R$id.transition_position);
        if (iArr != null) {
            f10 = (iArr[0] - rect.left) + translationX;
            f11 = (iArr[1] - rect.top) + translationY;
            rect.offsetTo(iArr[0], iArr[1]);
        } else {
            f10 = translationX;
            f11 = translationY;
        }
        j(viewGroup, rect, this.f4039g);
        int[] iArr2 = this.f4039g;
        return TranslationAnimationCreator.a(view, transitionValues, i10, i11, translationX, translationY, f10 + iArr2[0], f11 + iArr2[1], f4038i, this);
    }

    public Explode(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4039g = new int[2];
        setPropagation(new CircularPropagation());
    }
}
