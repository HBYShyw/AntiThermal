package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import org.xmlpull.v1.XmlPullParser;

/* loaded from: classes.dex */
public class ChangeTransform extends Transition {

    /* renamed from: h, reason: collision with root package name */
    private static final String[] f4007h = {"android:changeTransform:matrix", "android:changeTransform:transforms", "android:changeTransform:parentMatrix"};

    /* renamed from: i, reason: collision with root package name */
    private static final Property<e, float[]> f4008i = new a(float[].class, "nonTranslations");

    /* renamed from: j, reason: collision with root package name */
    private static final Property<e, PointF> f4009j = new b(PointF.class, "translations");

    /* renamed from: k, reason: collision with root package name */
    private static final boolean f4010k = true;

    /* renamed from: e, reason: collision with root package name */
    boolean f4011e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f4012f;

    /* renamed from: g, reason: collision with root package name */
    private Matrix f4013g;

    /* loaded from: classes.dex */
    static class a extends Property<e, float[]> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public float[] get(e eVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(e eVar, float[] fArr) {
            eVar.d(fArr);
        }
    }

    /* loaded from: classes.dex */
    static class b extends Property<e, PointF> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public PointF get(e eVar) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(e eVar, PointF pointF) {
            eVar.c(pointF);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f4014a;

        /* renamed from: b, reason: collision with root package name */
        private Matrix f4015b = new Matrix();

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ boolean f4016c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ Matrix f4017d;

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f4018e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ f f4019f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ e f4020g;

        c(boolean z10, Matrix matrix, View view, f fVar, e eVar) {
            this.f4016c = z10;
            this.f4017d = matrix;
            this.f4018e = view;
            this.f4019f = fVar;
            this.f4020g = eVar;
        }

        private void a(Matrix matrix) {
            this.f4015b.set(matrix);
            this.f4018e.setTag(R$id.transition_transform, this.f4015b);
            this.f4019f.a(this.f4018e);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f4014a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.f4014a) {
                if (this.f4016c && ChangeTransform.this.f4011e) {
                    a(this.f4017d);
                } else {
                    this.f4018e.setTag(R$id.transition_transform, null);
                    this.f4018e.setTag(R$id.parent_matrix, null);
                }
            }
            d0.f(this.f4018e, null);
            this.f4019f.a(this.f4018e);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationPause(Animator animator) {
            a(this.f4020g.a());
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationResume(Animator animator) {
            ChangeTransform.d(this.f4018e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d extends TransitionListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private View f4022a;

        /* renamed from: b, reason: collision with root package name */
        private GhostView f4023b;

        d(View view, GhostView ghostView) {
            this.f4022a = view;
            this.f4023b = ghostView;
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void b(Transition transition) {
            this.f4023b.setVisibility(4);
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            transition.removeListener(this);
            GhostViewUtils.b(this.f4022a);
            this.f4022a.setTag(R$id.transition_transform, null);
            this.f4022a.setTag(R$id.parent_matrix, null);
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void e(Transition transition) {
            this.f4023b.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class e {

        /* renamed from: a, reason: collision with root package name */
        private final Matrix f4024a = new Matrix();

        /* renamed from: b, reason: collision with root package name */
        private final View f4025b;

        /* renamed from: c, reason: collision with root package name */
        private final float[] f4026c;

        /* renamed from: d, reason: collision with root package name */
        private float f4027d;

        /* renamed from: e, reason: collision with root package name */
        private float f4028e;

        e(View view, float[] fArr) {
            this.f4025b = view;
            float[] fArr2 = (float[]) fArr.clone();
            this.f4026c = fArr2;
            this.f4027d = fArr2[2];
            this.f4028e = fArr2[5];
            b();
        }

        private void b() {
            float[] fArr = this.f4026c;
            fArr[2] = this.f4027d;
            fArr[5] = this.f4028e;
            this.f4024a.setValues(fArr);
            d0.f(this.f4025b, this.f4024a);
        }

        Matrix a() {
            return this.f4024a;
        }

        void c(PointF pointF) {
            this.f4027d = pointF.x;
            this.f4028e = pointF.y;
            b();
        }

        void d(float[] fArr) {
            System.arraycopy(fArr, 0, this.f4026c, 0, fArr.length);
            b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class f {

        /* renamed from: a, reason: collision with root package name */
        final float f4029a;

        /* renamed from: b, reason: collision with root package name */
        final float f4030b;

        /* renamed from: c, reason: collision with root package name */
        final float f4031c;

        /* renamed from: d, reason: collision with root package name */
        final float f4032d;

        /* renamed from: e, reason: collision with root package name */
        final float f4033e;

        /* renamed from: f, reason: collision with root package name */
        final float f4034f;

        /* renamed from: g, reason: collision with root package name */
        final float f4035g;

        /* renamed from: h, reason: collision with root package name */
        final float f4036h;

        f(View view) {
            this.f4029a = view.getTranslationX();
            this.f4030b = view.getTranslationY();
            this.f4031c = ViewCompat.H(view);
            this.f4032d = view.getScaleX();
            this.f4033e = view.getScaleY();
            this.f4034f = view.getRotationX();
            this.f4035g = view.getRotationY();
            this.f4036h = view.getRotation();
        }

        public void a(View view) {
            ChangeTransform.f(view, this.f4029a, this.f4030b, this.f4031c, this.f4032d, this.f4033e, this.f4034f, this.f4035g, this.f4036h);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof f)) {
                return false;
            }
            f fVar = (f) obj;
            return fVar.f4029a == this.f4029a && fVar.f4030b == this.f4030b && fVar.f4031c == this.f4031c && fVar.f4032d == this.f4032d && fVar.f4033e == this.f4033e && fVar.f4034f == this.f4034f && fVar.f4035g == this.f4035g && fVar.f4036h == this.f4036h;
        }

        public int hashCode() {
            float f10 = this.f4029a;
            int floatToIntBits = (f10 != 0.0f ? Float.floatToIntBits(f10) : 0) * 31;
            float f11 = this.f4030b;
            int floatToIntBits2 = (floatToIntBits + (f11 != 0.0f ? Float.floatToIntBits(f11) : 0)) * 31;
            float f12 = this.f4031c;
            int floatToIntBits3 = (floatToIntBits2 + (f12 != 0.0f ? Float.floatToIntBits(f12) : 0)) * 31;
            float f13 = this.f4032d;
            int floatToIntBits4 = (floatToIntBits3 + (f13 != 0.0f ? Float.floatToIntBits(f13) : 0)) * 31;
            float f14 = this.f4033e;
            int floatToIntBits5 = (floatToIntBits4 + (f14 != 0.0f ? Float.floatToIntBits(f14) : 0)) * 31;
            float f15 = this.f4034f;
            int floatToIntBits6 = (floatToIntBits5 + (f15 != 0.0f ? Float.floatToIntBits(f15) : 0)) * 31;
            float f16 = this.f4035g;
            int floatToIntBits7 = (floatToIntBits6 + (f16 != 0.0f ? Float.floatToIntBits(f16) : 0)) * 31;
            float f17 = this.f4036h;
            return floatToIntBits7 + (f17 != 0.0f ? Float.floatToIntBits(f17) : 0);
        }
    }

    public ChangeTransform() {
        this.f4011e = true;
        this.f4012f = true;
        this.f4013g = new Matrix();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v6, types: [androidx.transition.TransitionSet] */
    private void a(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        View view = transitionValues2.f4153b;
        Matrix matrix = new Matrix((Matrix) transitionValues2.f4152a.get("android:changeTransform:parentMatrix"));
        d0.k(viewGroup, matrix);
        GhostView a10 = GhostViewUtils.a(view, viewGroup, matrix);
        if (a10 == null) {
            return;
        }
        a10.a((ViewGroup) transitionValues.f4152a.get("android:changeTransform:parent"), transitionValues.f4153b);
        while (true) {
            ?? r12 = this.mParent;
            if (r12 == 0) {
                break;
            } else {
                this = r12;
            }
        }
        this.addListener(new d(view, a10));
        if (f4010k) {
            View view2 = transitionValues.f4153b;
            if (view2 != transitionValues2.f4153b) {
                d0.h(view2, 0.0f);
            }
            d0.h(view, 1.0f);
        }
    }

    private ObjectAnimator b(TransitionValues transitionValues, TransitionValues transitionValues2, boolean z10) {
        Matrix matrix = (Matrix) transitionValues.f4152a.get("android:changeTransform:matrix");
        Matrix matrix2 = (Matrix) transitionValues2.f4152a.get("android:changeTransform:matrix");
        if (matrix == null) {
            matrix = MatrixUtils.f4120a;
        }
        if (matrix2 == null) {
            matrix2 = MatrixUtils.f4120a;
        }
        Matrix matrix3 = matrix2;
        if (matrix.equals(matrix3)) {
            return null;
        }
        f fVar = (f) transitionValues2.f4152a.get("android:changeTransform:transforms");
        View view = transitionValues2.f4153b;
        d(view);
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        float[] fArr2 = new float[9];
        matrix3.getValues(fArr2);
        e eVar = new e(view, fArr);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(eVar, PropertyValuesHolder.ofObject(f4008i, new FloatArrayEvaluator(new float[9]), fArr, fArr2), PropertyValuesHolderUtils.a(f4009j, getPathMotion().a(fArr[2], fArr[5], fArr2[2], fArr2[5])));
        c cVar = new c(z10, matrix3, view, fVar, eVar);
        ofPropertyValuesHolder.addListener(cVar);
        AnimatorUtils.a(ofPropertyValuesHolder, cVar);
        return ofPropertyValuesHolder;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x001f, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x001a, code lost:
    
        if (r4 == r5) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0017, code lost:
    
        if (r5 == r3.f4153b) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001d, code lost:
    
        r1 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean c(ViewGroup viewGroup, ViewGroup viewGroup2) {
        boolean z10 = true;
        if (isValidTarget(viewGroup) && isValidTarget(viewGroup2)) {
            TransitionValues matchedTransitionValues = getMatchedTransitionValues(viewGroup, true);
            if (matchedTransitionValues == null) {
                return false;
            }
        }
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.f4153b;
        if (view.getVisibility() == 8) {
            return;
        }
        transitionValues.f4152a.put("android:changeTransform:parent", view.getParent());
        transitionValues.f4152a.put("android:changeTransform:transforms", new f(view));
        Matrix matrix = view.getMatrix();
        transitionValues.f4152a.put("android:changeTransform:matrix", (matrix == null || matrix.isIdentity()) ? null : new Matrix(matrix));
        if (this.f4012f) {
            Matrix matrix2 = new Matrix();
            d0.j((ViewGroup) view.getParent(), matrix2);
            matrix2.preTranslate(-r1.getScrollX(), -r1.getScrollY());
            transitionValues.f4152a.put("android:changeTransform:parentMatrix", matrix2);
            transitionValues.f4152a.put("android:changeTransform:intermediateMatrix", view.getTag(R$id.transition_transform));
            transitionValues.f4152a.put("android:changeTransform:intermediateParentMatrix", view.getTag(R$id.parent_matrix));
        }
    }

    static void d(View view) {
        f(view, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }

    private void e(TransitionValues transitionValues, TransitionValues transitionValues2) {
        Matrix matrix = (Matrix) transitionValues2.f4152a.get("android:changeTransform:parentMatrix");
        transitionValues2.f4153b.setTag(R$id.parent_matrix, matrix);
        Matrix matrix2 = this.f4013g;
        matrix2.reset();
        matrix.invert(matrix2);
        Matrix matrix3 = (Matrix) transitionValues.f4152a.get("android:changeTransform:matrix");
        if (matrix3 == null) {
            matrix3 = new Matrix();
            transitionValues.f4152a.put("android:changeTransform:matrix", matrix3);
        }
        matrix3.postConcat((Matrix) transitionValues.f4152a.get("android:changeTransform:parentMatrix"));
        matrix3.postConcat(matrix2);
    }

    static void f(View view, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17) {
        view.setTranslationX(f10);
        view.setTranslationY(f11);
        ViewCompat.F0(view, f12);
        view.setScaleX(f13);
        view.setScaleY(f14);
        view.setRotationX(f15);
        view.setRotationY(f16);
        view.setRotation(f17);
    }

    @Override // androidx.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // androidx.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
        if (f4010k) {
            return;
        }
        ((ViewGroup) transitionValues.f4153b.getParent()).startViewTransition(transitionValues.f4153b);
    }

    @Override // androidx.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null || !transitionValues.f4152a.containsKey("android:changeTransform:parent") || !transitionValues2.f4152a.containsKey("android:changeTransform:parent")) {
            return null;
        }
        ViewGroup viewGroup2 = (ViewGroup) transitionValues.f4152a.get("android:changeTransform:parent");
        boolean z10 = this.f4012f && !c(viewGroup2, (ViewGroup) transitionValues2.f4152a.get("android:changeTransform:parent"));
        Matrix matrix = (Matrix) transitionValues.f4152a.get("android:changeTransform:intermediateMatrix");
        if (matrix != null) {
            transitionValues.f4152a.put("android:changeTransform:matrix", matrix);
        }
        Matrix matrix2 = (Matrix) transitionValues.f4152a.get("android:changeTransform:intermediateParentMatrix");
        if (matrix2 != null) {
            transitionValues.f4152a.put("android:changeTransform:parentMatrix", matrix2);
        }
        if (z10) {
            e(transitionValues, transitionValues2);
        }
        ObjectAnimator b10 = b(transitionValues, transitionValues2, z10);
        if (z10 && b10 != null && this.f4011e) {
            a(viewGroup, transitionValues, transitionValues2);
        } else if (!f4010k) {
            viewGroup2.endViewTransition(transitionValues.f4153b);
        }
        return b10;
    }

    @Override // androidx.transition.Transition
    public String[] getTransitionProperties() {
        return f4007h;
    }

    @SuppressLint({"RestrictedApi"})
    public ChangeTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4011e = true;
        this.f4012f = true;
        this.f4013g = new Matrix();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4134g);
        XmlPullParser xmlPullParser = (XmlPullParser) attributeSet;
        this.f4011e = TypedArrayUtils.e(obtainStyledAttributes, xmlPullParser, "reparentWithOverlay", 1, true);
        this.f4012f = TypedArrayUtils.e(obtainStyledAttributes, xmlPullParser, "reparent", 0, true);
        obtainStyledAttributes.recycle();
    }
}
