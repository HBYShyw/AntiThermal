package n2;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.View;
import android.view.animation.PathInterpolator;
import fb._Ranges;
import java.util.Objects;
import kotlin.Metadata;
import ma.Unit;
import za.DefaultConstructorMarker;
import za.Lambda;
import za.k;

/* compiled from: PressFeedbackHelper.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001:\u0002\u0012\u0005B\u0007¢\u0006\u0004\b\u0010\u0010\u0011J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0002J\u0010\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J&\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\b2\u000e\b\u0002\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\r¨\u0006\u0013"}, d2 = {"Ln2/d;", "", "", "isPressed", "Lma/f0;", "b", "", "scaleValue", "Landroid/view/View;", "view", "g", "Ln2/d$b;", "f", "Lkotlin/Function0;", "onAnimEnd", "c", "<init>", "()V", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: n2.d, reason: use source file name */
/* loaded from: classes.dex */
public final class PressFeedbackHelper {

    /* renamed from: f, reason: collision with root package name */
    public static final a f15639f = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final PathInterpolator f15640a;

    /* renamed from: b, reason: collision with root package name */
    private final PathInterpolator f15641b;

    /* renamed from: c, reason: collision with root package name */
    private ObjectAnimator f15642c;

    /* renamed from: d, reason: collision with root package name */
    private float f15643d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f15644e;

    /* compiled from: PressFeedbackHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\t\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0016\u0010\u0017J\u0006\u0010\u0003\u001a\u00020\u0002J\u0006\u0010\u0004\u001a\u00020\u0002R\u0014\u0010\u0006\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\b\u0010\u0007R\u0014\u0010\t\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\t\u0010\u0007R\u0014\u0010\n\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\n\u0010\u0007R\u0014\u0010\u000b\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0007R\u0014\u0010\f\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\f\u0010\u0007R\u0014\u0010\r\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\r\u0010\u0007R\u0014\u0010\u000e\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u000e\u0010\u0007R\u0014\u0010\u0010\u001a\u00020\u000f8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0012\u0010\u0007R\u0014\u0010\u0013\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0013\u0010\u0007R\u0014\u0010\u0014\u001a\u00020\u00058\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0014\u0010\u0007R\u0014\u0010\u0015\u001a\u00020\u000f8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0015\u0010\u0011¨\u0006\u0018"}, d2 = {"Ln2/d$a;", "", "Landroid/view/animation/PathInterpolator;", "b", "a", "", "ANIM_DOWN_PATH_X1", "F", "ANIM_DOWN_PATH_X2", "ANIM_DOWN_PATH_Y1", "ANIM_DOWN_PATH_Y2", "ANIM_UP_PATH_X1", "ANIM_UP_PATH_X2", "ANIM_UP_PATH_Y1", "ANIM_UP_PATH_Y2", "", "DOWN_ANIMATION_TIME", "J", "SCALE_ANIM_VALUE_MAX", "SCALE_ANIM_VALUE_MIN", "SCALE_INIT_VALUE", "UP_ANIMATION_TIME", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: n2.d$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final PathInterpolator a() {
            return new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
        }

        public final PathInterpolator b() {
            return new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f);
        }
    }

    /* compiled from: PressFeedbackHelper.kt */
    @Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0007\b\u0086\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0006\u0010\u000e\u001a\u00020\t\u0012\u0006\u0010\u0010\u001a\u00020\t\u0012\u0006\u0010\u0012\u001a\u00020\t\u0012\u0006\u0010\u0015\u001a\u00020\u0014\u0012\u0006\u0010\u001a\u001a\u00020\u0019¢\u0006\u0004\b\u001e\u0010\u001fJ\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001HÖ\u0003R\u0017\u0010\n\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000b\u001a\u0004\b\u000f\u0010\rR\u0017\u0010\u0010\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u000b\u001a\u0004\b\u0011\u0010\rR\u0017\u0010\u0012\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u000b\u001a\u0004\b\u0013\u0010\rR\u0017\u0010\u0015\u001a\u00020\u00148\u0006¢\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u001a\u001a\u00020\u00198\u0006¢\u0006\f\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u001c\u0010\u001d¨\u0006 "}, d2 = {"Ln2/d$b;", "", "", "toString", "", "hashCode", "other", "", "equals", "", "scaleXStart", "F", "d", "()F", "scaleXEnd", "c", "scaleYStart", "f", "scaleYEnd", "e", "Landroid/view/animation/PathInterpolator;", "interpolator", "Landroid/view/animation/PathInterpolator;", "b", "()Landroid/view/animation/PathInterpolator;", "", "duration", "J", "a", "()J", "<init>", "(FFFFLandroid/view/animation/PathInterpolator;J)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: n2.d$b, reason: from toString */
    /* loaded from: classes.dex */
    public static final /* data */ class ScaleAnimParam {

        /* renamed from: a, reason: collision with root package name and from toString */
        private final float scaleXStart;

        /* renamed from: b, reason: collision with root package name and from toString */
        private final float scaleXEnd;

        /* renamed from: c, reason: collision with root package name and from toString */
        private final float scaleYStart;

        /* renamed from: d, reason: collision with root package name and from toString */
        private final float scaleYEnd;

        /* renamed from: e, reason: collision with root package name and from toString */
        private final PathInterpolator interpolator;

        /* renamed from: f, reason: collision with root package name and from toString */
        private final long duration;

        public ScaleAnimParam(float f10, float f11, float f12, float f13, PathInterpolator pathInterpolator, long j10) {
            k.e(pathInterpolator, "interpolator");
            this.scaleXStart = f10;
            this.scaleXEnd = f11;
            this.scaleYStart = f12;
            this.scaleYEnd = f13;
            this.interpolator = pathInterpolator;
            this.duration = j10;
        }

        /* renamed from: a, reason: from getter */
        public final long getDuration() {
            return this.duration;
        }

        /* renamed from: b, reason: from getter */
        public final PathInterpolator getInterpolator() {
            return this.interpolator;
        }

        /* renamed from: c, reason: from getter */
        public final float getScaleXEnd() {
            return this.scaleXEnd;
        }

        /* renamed from: d, reason: from getter */
        public final float getScaleXStart() {
            return this.scaleXStart;
        }

        /* renamed from: e, reason: from getter */
        public final float getScaleYEnd() {
            return this.scaleYEnd;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ScaleAnimParam)) {
                return false;
            }
            ScaleAnimParam scaleAnimParam = (ScaleAnimParam) other;
            return k.a(Float.valueOf(this.scaleXStart), Float.valueOf(scaleAnimParam.scaleXStart)) && k.a(Float.valueOf(this.scaleXEnd), Float.valueOf(scaleAnimParam.scaleXEnd)) && k.a(Float.valueOf(this.scaleYStart), Float.valueOf(scaleAnimParam.scaleYStart)) && k.a(Float.valueOf(this.scaleYEnd), Float.valueOf(scaleAnimParam.scaleYEnd)) && k.a(this.interpolator, scaleAnimParam.interpolator) && this.duration == scaleAnimParam.duration;
        }

        /* renamed from: f, reason: from getter */
        public final float getScaleYStart() {
            return this.scaleYStart;
        }

        public int hashCode() {
            return (((((((((Float.hashCode(this.scaleXStart) * 31) + Float.hashCode(this.scaleXEnd)) * 31) + Float.hashCode(this.scaleYStart)) * 31) + Float.hashCode(this.scaleYEnd)) * 31) + this.interpolator.hashCode()) * 31) + Long.hashCode(this.duration);
        }

        public String toString() {
            return "ScaleAnimParam(scaleXStart=" + this.scaleXStart + ", scaleXEnd=" + this.scaleXEnd + ", scaleYStart=" + this.scaleYStart + ", scaleYEnd=" + this.scaleYEnd + ", interpolator=" + this.interpolator + ", duration=" + this.duration + ')';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: PressFeedbackHelper.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0006\n\u0002\u0018\u0002\n\u0000\u0010\u0001\u001a\u00020\u0000H\n"}, d2 = {"Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 5, 1})
    /* renamed from: n2.d$c */
    /* loaded from: classes.dex */
    public static final class c extends Lambda implements ya.a<Unit> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f15651e = new c();

        c() {
            super(0);
        }

        public final void a() {
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* compiled from: Animator.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"androidx/core/animation/AnimatorKt$addListener$listener$1", "Landroid/animation/Animator$AnimatorListener;", "Landroid/animation/Animator;", "animator", "Lma/f0;", "onAnimationRepeat", "onAnimationEnd", "onAnimationCancel", "onAnimationStart", "core-ktx_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: n2.d$d */
    /* loaded from: classes.dex */
    public static final class d implements Animator.AnimatorListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ya.a f15652a;

        public d(ya.a aVar) {
            this.f15652a = aVar;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            k.e(animator, "animator");
            this.f15652a.invoke();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            k.e(animator, "animator");
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            k.e(animator, "animator");
        }
    }

    public PressFeedbackHelper() {
        a aVar = f15639f;
        this.f15640a = aVar.a();
        this.f15641b = aVar.b();
    }

    private final void b(boolean z10) {
        ObjectAnimator objectAnimator = this.f15642c;
        if (objectAnimator == null || !objectAnimator.isRunning()) {
            return;
        }
        boolean z11 = !z10 && ((float) objectAnimator.getCurrentPlayTime()) < ((float) objectAnimator.getDuration()) * 0.4f;
        this.f15644e = z11;
        if (z11) {
            return;
        }
        objectAnimator.cancel();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void d(PressFeedbackHelper pressFeedbackHelper, boolean z10, View view, ya.a aVar, int i10, Object obj) {
        if ((i10 & 4) != 0) {
            aVar = c.f15651e;
        }
        pressFeedbackHelper.c(z10, view, aVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void e(PressFeedbackHelper pressFeedbackHelper, boolean z10, View view, ValueAnimator valueAnimator) {
        k.e(pressFeedbackHelper, "this$0");
        k.e(view, "$view");
        Object animatedValue = valueAnimator.getAnimatedValue("scaleX");
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        pressFeedbackHelper.f15643d = ((Float) animatedValue).floatValue();
        if (pressFeedbackHelper.f15644e && z10 && ((float) valueAnimator.getCurrentPlayTime()) > ((float) valueAnimator.getDuration()) * 0.4f) {
            valueAnimator.cancel();
            d(pressFeedbackHelper, false, view, null, 4, null);
        } else {
            pressFeedbackHelper.g(pressFeedbackHelper.f15643d, view);
        }
    }

    private final ScaleAnimParam f(boolean isPressed) {
        if (isPressed) {
            return new ScaleAnimParam(1.0f, 0.92f, 1.0f, 0.92f, this.f15640a, 200L);
        }
        float f10 = this.f15643d;
        return new ScaleAnimParam(f10, 1.0f, f10, 1.0f, this.f15641b, 340L);
    }

    private final void g(float f10, View view) {
        float e10;
        float b10;
        e10 = _Ranges.e(1.0f, f10);
        b10 = _Ranges.b(0.92f, e10);
        view.setScaleX(b10);
        view.setScaleY(b10);
        view.invalidate();
    }

    public final void c(final boolean z10, final View view, ya.a<Unit> aVar) {
        k.e(view, "view");
        k.e(aVar, "onAnimEnd");
        this.f15644e = false;
        b(z10);
        if (this.f15644e) {
            return;
        }
        ScaleAnimParam f10 = f(z10);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat((Property<?, Float>) View.SCALE_X, f10.getScaleXStart(), f10.getScaleXEnd()), PropertyValuesHolder.ofFloat((Property<?, Float>) View.SCALE_Y, f10.getScaleYStart(), f10.getScaleYEnd()));
        this.f15642c = ofPropertyValuesHolder;
        ofPropertyValuesHolder.setInterpolator(f10.getInterpolator());
        ofPropertyValuesHolder.setDuration(f10.getDuration());
        ofPropertyValuesHolder.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: n2.c
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PressFeedbackHelper.e(PressFeedbackHelper.this, z10, view, valueAnimator);
            }
        });
        k.d(ofPropertyValuesHolder, "");
        ofPropertyValuesHolder.addListener(new d(aVar));
        ofPropertyValuesHolder.start();
    }
}
