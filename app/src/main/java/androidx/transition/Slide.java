package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class Slide extends Visibility {

    /* renamed from: i, reason: collision with root package name */
    private static final TimeInterpolator f4047i = new DecelerateInterpolator();

    /* renamed from: j, reason: collision with root package name */
    private static final TimeInterpolator f4048j = new AccelerateInterpolator();

    /* renamed from: k, reason: collision with root package name */
    private static final g f4049k = new a();

    /* renamed from: l, reason: collision with root package name */
    private static final g f4050l = new b();

    /* renamed from: m, reason: collision with root package name */
    private static final g f4051m = new c();

    /* renamed from: n, reason: collision with root package name */
    private static final g f4052n = new d();

    /* renamed from: o, reason: collision with root package name */
    private static final g f4053o = new e();

    /* renamed from: p, reason: collision with root package name */
    private static final g f4054p = new f();

    /* renamed from: g, reason: collision with root package name */
    private g f4055g;

    /* renamed from: h, reason: collision with root package name */
    private int f4056h;

    /* loaded from: classes.dex */
    static class a extends h {
        a() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            return view.getTranslationX() - viewGroup.getWidth();
        }
    }

    /* loaded from: classes.dex */
    static class b extends h {
        b() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            if (ViewCompat.x(viewGroup) == 1) {
                return view.getTranslationX() + viewGroup.getWidth();
            }
            return view.getTranslationX() - viewGroup.getWidth();
        }
    }

    /* loaded from: classes.dex */
    static class c extends i {
        c() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float a(ViewGroup viewGroup, View view) {
            return view.getTranslationY() - viewGroup.getHeight();
        }
    }

    /* loaded from: classes.dex */
    static class d extends h {
        d() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            return view.getTranslationX() + viewGroup.getWidth();
        }
    }

    /* loaded from: classes.dex */
    static class e extends h {
        e() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            if (ViewCompat.x(viewGroup) == 1) {
                return view.getTranslationX() - viewGroup.getWidth();
            }
            return view.getTranslationX() + viewGroup.getWidth();
        }
    }

    /* loaded from: classes.dex */
    static class f extends i {
        f() {
            super(null);
        }

        @Override // androidx.transition.Slide.g
        public float a(ViewGroup viewGroup, View view) {
            return view.getTranslationY() + viewGroup.getHeight();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface g {
        float a(ViewGroup viewGroup, View view);

        float b(ViewGroup viewGroup, View view);
    }

    /* loaded from: classes.dex */
    private static abstract class h implements g {
        private h() {
        }

        /* synthetic */ h(a aVar) {
            this();
        }

        @Override // androidx.transition.Slide.g
        public float a(ViewGroup viewGroup, View view) {
            return view.getTranslationY();
        }
    }

    /* loaded from: classes.dex */
    private static abstract class i implements g {
        private i() {
        }

        /* synthetic */ i(a aVar) {
            this();
        }

        @Override // androidx.transition.Slide.g
        public float b(ViewGroup viewGroup, View view) {
            return view.getTranslationX();
        }
    }

    public Slide() {
        this.f4055g = f4054p;
        this.f4056h = 80;
        h(80);
    }

    private void captureValues(TransitionValues transitionValues) {
        int[] iArr = new int[2];
        transitionValues.f4153b.getLocationOnScreen(iArr);
        transitionValues.f4152a.put("android:slide:screenPosition", iArr);
    }

    @Override // androidx.transition.Visibility
    public Animator c(ViewGroup viewGroup, View view, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues2 == null) {
            return null;
        }
        int[] iArr = (int[]) transitionValues2.f4152a.get("android:slide:screenPosition");
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        return TranslationAnimationCreator.a(view, transitionValues2, iArr[0], iArr[1], this.f4055g.b(viewGroup, view), this.f4055g.a(viewGroup, view), translationX, translationY, f4047i, this);
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
        if (transitionValues == null) {
            return null;
        }
        int[] iArr = (int[]) transitionValues.f4152a.get("android:slide:screenPosition");
        return TranslationAnimationCreator.a(view, transitionValues, iArr[0], iArr[1], view.getTranslationX(), view.getTranslationY(), this.f4055g.b(viewGroup, view), this.f4055g.a(viewGroup, view), f4048j, this);
    }

    public void h(int i10) {
        if (i10 == 3) {
            this.f4055g = f4049k;
        } else if (i10 == 5) {
            this.f4055g = f4052n;
        } else if (i10 == 48) {
            this.f4055g = f4051m;
        } else if (i10 == 80) {
            this.f4055g = f4054p;
        } else if (i10 == 8388611) {
            this.f4055g = f4050l;
        } else if (i10 == 8388613) {
            this.f4055g = f4053o;
        } else {
            throw new IllegalArgumentException("Invalid slide direction");
        }
        this.f4056h = i10;
        SidePropagation sidePropagation = new SidePropagation();
        sidePropagation.j(i10);
        setPropagation(sidePropagation);
    }

    @SuppressLint({"RestrictedApi"})
    public Slide(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4055g = f4054p;
        this.f4056h = 80;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4135h);
        int g6 = TypedArrayUtils.g(obtainStyledAttributes, (XmlPullParser) attributeSet, "slideEdge", 0, 80);
        obtainStyledAttributes.recycle();
        h(g6);
    }
}
