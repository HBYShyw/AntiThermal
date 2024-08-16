package s2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Insets;
import android.view.WindowInsetsAnimationControlListener;
import android.view.WindowInsetsAnimationController;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CustomWindowInsetsAnimationControlListener.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0017B\u001f\u0012\u0006\u0010\u0012\u001a\u00020\u0004\u0012\u0006\u0010\u0013\u001a\u00020\b\u0012\u0006\u0010\u0014\u001a\u00020\u000e¢\u0006\u0004\b\u0015\u0010\u0016J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0018\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0016J\u0010\u0010\f\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0012\u0010\r\u001a\u00020\n2\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0016R\u0014\u0010\u0011\u001a\u00020\u000e8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0018"}, d2 = {"Ls2/f;", "Landroid/view/WindowInsetsAnimationControlListener;", "Landroid/view/WindowInsetsAnimationController;", "controller", "", "show", "Landroid/animation/ValueAnimator;", "g", "", "types", "Lma/f0;", "onReady", "onFinished", "onCancelled", "Landroid/view/animation/Interpolator;", "f", "()Landroid/view/animation/Interpolator;", "alphaInterpolator", "mShow", "mDuration", "mInsetsInterpolator", "<init>", "(ZILandroid/view/animation/Interpolator;)V", "a", "coui-support-appcompat_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: s2.f, reason: use source file name */
/* loaded from: classes.dex */
public final class CustomWindowInsetsAnimationControlListener implements WindowInsetsAnimationControlListener {

    /* renamed from: e, reason: collision with root package name */
    public static final a f18030e = new a(null);

    /* renamed from: f, reason: collision with root package name */
    private static final Interpolator f18031f = new PathInterpolator(0.4f, 0.0f, 1.0f, 1.0f);

    /* renamed from: g, reason: collision with root package name */
    private static final TypeEvaluator<Insets> f18032g = new TypeEvaluator() { // from class: s2.c
        @Override // android.animation.TypeEvaluator
        public final Object evaluate(float f10, Object obj, Object obj2) {
            Insets d10;
            d10 = CustomWindowInsetsAnimationControlListener.d(f10, (Insets) obj, (Insets) obj2);
            return d10;
        }
    };

    /* renamed from: a, reason: collision with root package name */
    private final boolean f18033a;

    /* renamed from: b, reason: collision with root package name */
    private final int f18034b;

    /* renamed from: c, reason: collision with root package name */
    private final Interpolator f18035c;

    /* renamed from: d, reason: collision with root package name */
    private Animator f18036d;

    /* compiled from: CustomWindowInsetsAnimationControlListener.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Ls2/f$a;", "", "Landroid/view/animation/Interpolator;", "FAST_OUT_LINEAR_IN_INTERPOLATOR", "Landroid/view/animation/Interpolator;", "<init>", "()V", "coui-support-appcompat_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: s2.f$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: CustomWindowInsetsAnimationControlListener.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"s2/f$b", "Landroid/animation/AnimatorListenerAdapter;", "Landroid/animation/Animator;", "animation", "Lma/f0;", "onAnimationEnd", "coui-support-appcompat_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: s2.f$b */
    /* loaded from: classes.dex */
    public static final class b extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ WindowInsetsAnimationController f18037a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ boolean f18038b;

        b(WindowInsetsAnimationController windowInsetsAnimationController, boolean z10) {
            this.f18037a = windowInsetsAnimationController;
            this.f18038b = z10;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            k.e(animator, "animation");
            if (this.f18037a.isCancelled()) {
                return;
            }
            this.f18037a.finish(this.f18038b);
        }
    }

    public CustomWindowInsetsAnimationControlListener(boolean z10, int i10, Interpolator interpolator) {
        k.e(interpolator, "mInsetsInterpolator");
        this.f18033a = z10;
        this.f18034b = i10;
        this.f18035c = interpolator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Insets d(float f10, Insets insets, Insets insets2) {
        k.e(insets, "startValue");
        k.e(insets2, "endValue");
        return Insets.of((int) (insets.left + ((insets2.left - r0) * f10)), (int) (insets.top + ((insets2.top - r1) * f10)), (int) (insets.right + ((insets2.right - r2) * f10)), (int) (insets.bottom + (f10 * (insets2.bottom - r6))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final float e(float f10) {
        return Math.min(1.0f, 2 * f10);
    }

    private final Interpolator f() {
        return this.f18033a ? new Interpolator() { // from class: s2.e
            @Override // android.animation.TimeInterpolator
            public final float getInterpolation(float f10) {
                float e10;
                e10 = CustomWindowInsetsAnimationControlListener.e(f10);
                return e10;
            }
        } : f18031f;
    }

    private final ValueAnimator g(final WindowInsetsAnimationController controller, boolean show) {
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(this.f18034b);
        ofFloat.setInterpolator(new LinearInterpolator());
        final Interpolator interpolator = this.f18035c;
        final Interpolator f10 = f();
        final Insets hiddenStateInsets = show ? controller.getHiddenStateInsets() : controller.getShownStateInsets();
        final Insets shownStateInsets = show ? controller.getShownStateInsets() : controller.getHiddenStateInsets();
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: s2.d
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                CustomWindowInsetsAnimationControlListener.h(controller, ofFloat, this, interpolator, hiddenStateInsets, shownStateInsets, f10, valueAnimator);
            }
        });
        ofFloat.addListener(new b(controller, show));
        ofFloat.start();
        k.d(ofFloat, "animator");
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void h(WindowInsetsAnimationController windowInsetsAnimationController, ValueAnimator valueAnimator, CustomWindowInsetsAnimationControlListener customWindowInsetsAnimationControlListener, Interpolator interpolator, Insets insets, Insets insets2, Interpolator interpolator2, ValueAnimator valueAnimator2) {
        k.e(windowInsetsAnimationController, "$controller");
        k.e(customWindowInsetsAnimationControlListener, "this$0");
        k.e(interpolator, "$insetsInterpolator");
        k.e(interpolator2, "$alphaInterpolator");
        k.e(valueAnimator2, "animation");
        if (!windowInsetsAnimationController.isReady()) {
            valueAnimator.cancel();
            return;
        }
        float animatedFraction = valueAnimator2.getAnimatedFraction();
        windowInsetsAnimationController.setInsetsAndAlpha(f18032g.evaluate(interpolator.getInterpolation(animatedFraction), insets, insets2), interpolator2.getInterpolation(customWindowInsetsAnimationControlListener.f18033a ? animatedFraction : 1 - animatedFraction), animatedFraction);
    }

    @Override // android.view.WindowInsetsAnimationControlListener
    public void onCancelled(WindowInsetsAnimationController windowInsetsAnimationController) {
        Animator animator = this.f18036d;
        if (animator != null) {
            k.b(animator);
            animator.cancel();
        }
    }

    @Override // android.view.WindowInsetsAnimationControlListener
    public void onFinished(WindowInsetsAnimationController windowInsetsAnimationController) {
        k.e(windowInsetsAnimationController, "controller");
    }

    @Override // android.view.WindowInsetsAnimationControlListener
    public void onReady(WindowInsetsAnimationController windowInsetsAnimationController, int i10) {
        k.e(windowInsetsAnimationController, "controller");
        this.f18036d = g(windowInsetsAnimationController, this.f18033a);
    }
}
