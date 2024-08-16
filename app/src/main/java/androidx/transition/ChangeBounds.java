package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import java.util.Map;

/* loaded from: classes.dex */
public class ChangeBounds extends Transition {

    /* renamed from: h, reason: collision with root package name */
    private static final String[] f3962h = {"android:changeBounds:bounds", "android:changeBounds:clip", "android:changeBounds:parent", "android:changeBounds:windowX", "android:changeBounds:windowY"};

    /* renamed from: i, reason: collision with root package name */
    private static final Property<Drawable, PointF> f3963i = new b(PointF.class, "boundsOrigin");

    /* renamed from: j, reason: collision with root package name */
    private static final Property<k, PointF> f3964j = new c(PointF.class, "topLeft");

    /* renamed from: k, reason: collision with root package name */
    private static final Property<k, PointF> f3965k = new d(PointF.class, "bottomRight");

    /* renamed from: l, reason: collision with root package name */
    private static final Property<View, PointF> f3966l = new e(PointF.class, "bottomRight");

    /* renamed from: m, reason: collision with root package name */
    private static final Property<View, PointF> f3967m = new f(PointF.class, "topLeft");

    /* renamed from: n, reason: collision with root package name */
    private static final Property<View, PointF> f3968n = new g(PointF.class, "position");

    /* renamed from: o, reason: collision with root package name */
    private static RectEvaluator f3969o = new RectEvaluator();

    /* renamed from: e, reason: collision with root package name */
    private int[] f3970e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f3971f;

    /* renamed from: g, reason: collision with root package name */
    private boolean f3972g;

    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f3973a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ BitmapDrawable f3974b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ View f3975c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ float f3976d;

        a(ViewGroup viewGroup, BitmapDrawable bitmapDrawable, View view, float f10) {
            this.f3973a = viewGroup;
            this.f3974b = bitmapDrawable;
            this.f3975c = view;
            this.f3976d = f10;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            d0.b(this.f3973a).remove(this.f3974b);
            d0.h(this.f3975c, this.f3976d);
        }
    }

    /* loaded from: classes.dex */
    static class b extends Property<Drawable, PointF> {

        /* renamed from: a, reason: collision with root package name */
        private Rect f3978a;

        b(Class cls, String str) {
            super(cls, str);
            this.f3978a = new Rect();
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PointF get(Drawable drawable) {
            drawable.copyBounds(this.f3978a);
            Rect rect = this.f3978a;
            return new PointF(rect.left, rect.top);
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(Drawable drawable, PointF pointF) {
            drawable.copyBounds(this.f3978a);
            this.f3978a.offsetTo(Math.round(pointF.x), Math.round(pointF.y));
            drawable.setBounds(this.f3978a);
        }
    }

    /* loaded from: classes.dex */
    static class c extends Property<k, PointF> {
        c(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PointF get(k kVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(k kVar, PointF pointF) {
            kVar.c(pointF);
        }
    }

    /* loaded from: classes.dex */
    static class d extends Property<k, PointF> {
        d(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PointF get(k kVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(k kVar, PointF pointF) {
            kVar.a(pointF);
        }
    }

    /* loaded from: classes.dex */
    static class e extends Property<View, PointF> {
        e(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, PointF pointF) {
            d0.g(view, view.getLeft(), view.getTop(), Math.round(pointF.x), Math.round(pointF.y));
        }
    }

    /* loaded from: classes.dex */
    static class f extends Property<View, PointF> {
        f(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, PointF pointF) {
            d0.g(view, Math.round(pointF.x), Math.round(pointF.y), view.getRight(), view.getBottom());
        }
    }

    /* loaded from: classes.dex */
    static class g extends Property<View, PointF> {
        g(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PointF get(View view) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, PointF pointF) {
            int round = Math.round(pointF.x);
            int round2 = Math.round(pointF.y);
            d0.g(view, round, round2, view.getWidth() + round, view.getHeight() + round2);
        }
    }

    /* loaded from: classes.dex */
    class h extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ k f3979a;
        private k mViewBounds;

        h(k kVar) {
            this.f3979a = kVar;
            this.mViewBounds = kVar;
        }
    }

    /* loaded from: classes.dex */
    class i extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f3981a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ View f3982b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ Rect f3983c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ int f3984d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f3985e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f3986f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ int f3987g;

        i(View view, Rect rect, int i10, int i11, int i12, int i13) {
            this.f3982b = view;
            this.f3983c = rect;
            this.f3984d = i10;
            this.f3985e = i11;
            this.f3986f = i12;
            this.f3987g = i13;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f3981a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f3981a) {
                return;
            }
            ViewCompat.s0(this.f3982b, this.f3983c);
            d0.g(this.f3982b, this.f3984d, this.f3985e, this.f3986f, this.f3987g);
        }
    }

    /* loaded from: classes.dex */
    class j extends TransitionListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        boolean f3989a = false;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ViewGroup f3990b;

        j(ViewGroup viewGroup) {
            this.f3990b = viewGroup;
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void b(Transition transition) {
            a0.c(this.f3990b, false);
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            if (!this.f3989a) {
                a0.c(this.f3990b, false);
            }
            transition.removeListener(this);
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void d(Transition transition) {
            a0.c(this.f3990b, false);
            this.f3989a = true;
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void e(Transition transition) {
            a0.c(this.f3990b, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class k {

        /* renamed from: a, reason: collision with root package name */
        private int f3992a;

        /* renamed from: b, reason: collision with root package name */
        private int f3993b;

        /* renamed from: c, reason: collision with root package name */
        private int f3994c;

        /* renamed from: d, reason: collision with root package name */
        private int f3995d;

        /* renamed from: e, reason: collision with root package name */
        private View f3996e;

        /* renamed from: f, reason: collision with root package name */
        private int f3997f;

        /* renamed from: g, reason: collision with root package name */
        private int f3998g;

        k(View view) {
            this.f3996e = view;
        }

        private void b() {
            d0.g(this.f3996e, this.f3992a, this.f3993b, this.f3994c, this.f3995d);
            this.f3997f = 0;
            this.f3998g = 0;
        }

        void a(PointF pointF) {
            this.f3994c = Math.round(pointF.x);
            this.f3995d = Math.round(pointF.y);
            int i10 = this.f3998g + 1;
            this.f3998g = i10;
            if (this.f3997f == i10) {
                b();
            }
        }

        void c(PointF pointF) {
            this.f3992a = Math.round(pointF.x);
            this.f3993b = Math.round(pointF.y);
            int i10 = this.f3997f + 1;
            this.f3997f = i10;
            if (i10 == this.f3998g) {
                b();
            }
        }
    }

    public ChangeBounds() {
        this.f3970e = new int[2];
        this.f3971f = false;
        this.f3972g = false;
    }

    private boolean a(View view, View view2) {
        if (!this.f3972g) {
            return true;
        }
        TransitionValues matchedTransitionValues = getMatchedTransitionValues(view, true);
        if (matchedTransitionValues == null) {
            if (view == view2) {
                return true;
            }
        } else if (view2 == matchedTransitionValues.f4153b) {
            return true;
        }
        return false;
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.f4153b;
        if (!ViewCompat.Q(view) && view.getWidth() == 0 && view.getHeight() == 0) {
            return;
        }
        transitionValues.f4152a.put("android:changeBounds:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
        transitionValues.f4152a.put("android:changeBounds:parent", transitionValues.f4153b.getParent());
        if (this.f3972g) {
            transitionValues.f4153b.getLocationInWindow(this.f3970e);
            transitionValues.f4152a.put("android:changeBounds:windowX", Integer.valueOf(this.f3970e[0]));
            transitionValues.f4152a.put("android:changeBounds:windowY", Integer.valueOf(this.f3970e[1]));
        }
        if (this.f3971f) {
            transitionValues.f4152a.put("android:changeBounds:clip", ViewCompat.r(view));
        }
    }

    public void b(boolean z10) {
        this.f3971f = z10;
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
        int i10;
        View view;
        int i11;
        Rect rect;
        ObjectAnimator objectAnimator;
        Animator c10;
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        Map<String, Object> map = transitionValues.f4152a;
        Map<String, Object> map2 = transitionValues2.f4152a;
        ViewGroup viewGroup2 = (ViewGroup) map.get("android:changeBounds:parent");
        ViewGroup viewGroup3 = (ViewGroup) map2.get("android:changeBounds:parent");
        if (viewGroup2 == null || viewGroup3 == null) {
            return null;
        }
        View view2 = transitionValues2.f4153b;
        if (a(viewGroup2, viewGroup3)) {
            Rect rect2 = (Rect) transitionValues.f4152a.get("android:changeBounds:bounds");
            Rect rect3 = (Rect) transitionValues2.f4152a.get("android:changeBounds:bounds");
            int i12 = rect2.left;
            int i13 = rect3.left;
            int i14 = rect2.top;
            int i15 = rect3.top;
            int i16 = rect2.right;
            int i17 = rect3.right;
            int i18 = rect2.bottom;
            int i19 = rect3.bottom;
            int i20 = i16 - i12;
            int i21 = i18 - i14;
            int i22 = i17 - i13;
            int i23 = i19 - i15;
            Rect rect4 = (Rect) transitionValues.f4152a.get("android:changeBounds:clip");
            Rect rect5 = (Rect) transitionValues2.f4152a.get("android:changeBounds:clip");
            if ((i20 == 0 || i21 == 0) && (i22 == 0 || i23 == 0)) {
                i10 = 0;
            } else {
                i10 = (i12 == i13 && i14 == i15) ? 0 : 1;
                if (i16 != i17 || i18 != i19) {
                    i10++;
                }
            }
            if ((rect4 != null && !rect4.equals(rect5)) || (rect4 == null && rect5 != null)) {
                i10++;
            }
            if (i10 <= 0) {
                return null;
            }
            if (!this.f3971f) {
                view = view2;
                d0.g(view, i12, i14, i16, i18);
                if (i10 == 2) {
                    if (i20 == i22 && i21 == i23) {
                        c10 = ObjectAnimatorUtils.a(view, f3968n, getPathMotion().a(i12, i14, i13, i15));
                    } else {
                        k kVar = new k(view);
                        ObjectAnimator a10 = ObjectAnimatorUtils.a(kVar, f3964j, getPathMotion().a(i12, i14, i13, i15));
                        ObjectAnimator a11 = ObjectAnimatorUtils.a(kVar, f3965k, getPathMotion().a(i16, i18, i17, i19));
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(a10, a11);
                        animatorSet.addListener(new h(kVar));
                        c10 = animatorSet;
                    }
                } else if (i12 == i13 && i14 == i15) {
                    c10 = ObjectAnimatorUtils.a(view, f3966l, getPathMotion().a(i16, i18, i17, i19));
                } else {
                    c10 = ObjectAnimatorUtils.a(view, f3967m, getPathMotion().a(i12, i14, i13, i15));
                }
            } else {
                view = view2;
                d0.g(view, i12, i14, Math.max(i20, i22) + i12, Math.max(i21, i23) + i14);
                ObjectAnimator a12 = (i12 == i13 && i14 == i15) ? null : ObjectAnimatorUtils.a(view, f3968n, getPathMotion().a(i12, i14, i13, i15));
                if (rect4 == null) {
                    i11 = 0;
                    rect = new Rect(0, 0, i20, i21);
                } else {
                    i11 = 0;
                    rect = rect4;
                }
                Rect rect6 = rect5 == null ? new Rect(i11, i11, i22, i23) : rect5;
                if (rect.equals(rect6)) {
                    objectAnimator = null;
                } else {
                    ViewCompat.s0(view, rect);
                    RectEvaluator rectEvaluator = f3969o;
                    Object[] objArr = new Object[2];
                    objArr[i11] = rect;
                    objArr[1] = rect6;
                    ObjectAnimator ofObject = ObjectAnimator.ofObject(view, "clipBounds", rectEvaluator, objArr);
                    ofObject.addListener(new i(view, rect5, i13, i15, i17, i19));
                    objectAnimator = ofObject;
                }
                c10 = TransitionUtils.c(a12, objectAnimator);
            }
            if (view.getParent() instanceof ViewGroup) {
                ViewGroup viewGroup4 = (ViewGroup) view.getParent();
                a0.c(viewGroup4, true);
                addListener(new j(viewGroup4));
            }
            return c10;
        }
        int intValue = ((Integer) transitionValues.f4152a.get("android:changeBounds:windowX")).intValue();
        int intValue2 = ((Integer) transitionValues.f4152a.get("android:changeBounds:windowY")).intValue();
        int intValue3 = ((Integer) transitionValues2.f4152a.get("android:changeBounds:windowX")).intValue();
        int intValue4 = ((Integer) transitionValues2.f4152a.get("android:changeBounds:windowY")).intValue();
        if (intValue == intValue3 && intValue2 == intValue4) {
            return null;
        }
        viewGroup.getLocationInWindow(this.f3970e);
        Bitmap createBitmap = Bitmap.createBitmap(view2.getWidth(), view2.getHeight(), Bitmap.Config.ARGB_8888);
        view2.draw(new Canvas(createBitmap));
        BitmapDrawable bitmapDrawable = new BitmapDrawable(createBitmap);
        float c11 = d0.c(view2);
        d0.h(view2, 0.0f);
        d0.b(viewGroup).add(bitmapDrawable);
        PathMotion pathMotion = getPathMotion();
        int[] iArr = this.f3970e;
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(bitmapDrawable, PropertyValuesHolderUtils.a(f3963i, pathMotion.a(intValue - iArr[0], intValue2 - iArr[1], intValue3 - iArr[0], intValue4 - iArr[1])));
        ofPropertyValuesHolder.addListener(new a(viewGroup, bitmapDrawable, view2, c11));
        return ofPropertyValuesHolder;
    }

    @Override // androidx.transition.Transition
    public String[] getTransitionProperties() {
        return f3962h;
    }

    @SuppressLint({"RestrictedApi"})
    public ChangeBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3970e = new int[2];
        this.f3971f = false;
        this.f3972g = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4131d);
        boolean e10 = TypedArrayUtils.e(obtainStyledAttributes, (XmlResourceParser) attributeSet, "resizeClip", 0, false);
        obtainStyledAttributes.recycle();
        b(e10);
    }
}
