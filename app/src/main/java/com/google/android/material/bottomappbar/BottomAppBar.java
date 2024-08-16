package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.customview.view.AbsSavedState;
import c4.MaterialShapeDrawable;
import c4.MaterialShapeUtils;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$animator;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import d4.MaterialThemeOverlay;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p3.TransformationCallback;
import z3.MaterialResources;

/* loaded from: classes.dex */
public class BottomAppBar extends Toolbar implements CoordinatorLayout.b {
    private static final int A = R$style.Widget_MaterialComponents_BottomAppBar;

    /* renamed from: e, reason: collision with root package name */
    private Integer f8338e;

    /* renamed from: f, reason: collision with root package name */
    private final int f8339f;

    /* renamed from: g, reason: collision with root package name */
    private final MaterialShapeDrawable f8340g;

    /* renamed from: h, reason: collision with root package name */
    private Animator f8341h;

    /* renamed from: i, reason: collision with root package name */
    private Animator f8342i;

    /* renamed from: j, reason: collision with root package name */
    private int f8343j;

    /* renamed from: k, reason: collision with root package name */
    private int f8344k;

    /* renamed from: l, reason: collision with root package name */
    private boolean f8345l;

    /* renamed from: m, reason: collision with root package name */
    private final boolean f8346m;

    /* renamed from: n, reason: collision with root package name */
    private final boolean f8347n;

    /* renamed from: o, reason: collision with root package name */
    private final boolean f8348o;

    /* renamed from: p, reason: collision with root package name */
    private int f8349p;

    /* renamed from: q, reason: collision with root package name */
    private ArrayList<j> f8350q;

    /* renamed from: r, reason: collision with root package name */
    private int f8351r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f8352s;

    /* renamed from: t, reason: collision with root package name */
    private boolean f8353t;

    /* renamed from: u, reason: collision with root package name */
    private Behavior f8354u;

    /* renamed from: v, reason: collision with root package name */
    private int f8355v;

    /* renamed from: w, reason: collision with root package name */
    private int f8356w;

    /* renamed from: x, reason: collision with root package name */
    private int f8357x;

    /* renamed from: y, reason: collision with root package name */
    AnimatorListenerAdapter f8358y;

    /* renamed from: z, reason: collision with root package name */
    TransformationCallback<FloatingActionButton> f8359z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        int f8365e;

        /* renamed from: f, reason: collision with root package name */
        boolean f8366f;

        /* loaded from: classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c, reason: merged with bridge method [inline-methods] */
            public SavedState[] newArray(int i10) {
                return new SavedState[i10];
            }
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            super.writeToParcel(parcel, i10);
            parcel.writeInt(this.f8365e);
            parcel.writeInt(this.f8366f ? 1 : 0);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f8365e = parcel.readInt();
            this.f8366f = parcel.readInt() != 0;
        }
    }

    /* loaded from: classes.dex */
    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (BottomAppBar.this.f8352s) {
                return;
            }
            BottomAppBar bottomAppBar = BottomAppBar.this;
            bottomAppBar.T(bottomAppBar.f8343j, BottomAppBar.this.f8353t);
        }
    }

    /* loaded from: classes.dex */
    class b implements TransformationCallback<FloatingActionButton> {
        b() {
        }

        @Override // p3.TransformationCallback
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public void a(FloatingActionButton floatingActionButton) {
            BottomAppBar.this.f8340g.b0(floatingActionButton.getVisibility() == 0 ? floatingActionButton.getScaleY() : 0.0f);
        }

        @Override // p3.TransformationCallback
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public void b(FloatingActionButton floatingActionButton) {
            float translationX = floatingActionButton.getTranslationX();
            if (BottomAppBar.this.getTopEdgeTreatment().h() != translationX) {
                BottomAppBar.this.getTopEdgeTreatment().o(translationX);
                BottomAppBar.this.f8340g.invalidateSelf();
            }
            float max = Math.max(0.0f, -floatingActionButton.getTranslationY());
            if (BottomAppBar.this.getTopEdgeTreatment().c() != max) {
                BottomAppBar.this.getTopEdgeTreatment().i(max);
                BottomAppBar.this.f8340g.invalidateSelf();
            }
            BottomAppBar.this.f8340g.b0(floatingActionButton.getVisibility() == 0 ? floatingActionButton.getScaleY() : 0.0f);
        }
    }

    /* loaded from: classes.dex */
    class c implements ViewUtils.OnApplyWindowInsetsListener {
        c() {
        }

        @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat, ViewUtils.RelativePadding relativePadding) {
            boolean z10;
            if (BottomAppBar.this.f8346m) {
                BottomAppBar.this.f8355v = windowInsetsCompat.i();
            }
            boolean z11 = false;
            if (BottomAppBar.this.f8347n) {
                z10 = BottomAppBar.this.f8357x != windowInsetsCompat.j();
                BottomAppBar.this.f8357x = windowInsetsCompat.j();
            } else {
                z10 = false;
            }
            if (BottomAppBar.this.f8348o) {
                boolean z12 = BottomAppBar.this.f8356w != windowInsetsCompat.k();
                BottomAppBar.this.f8356w = windowInsetsCompat.k();
                z11 = z12;
            }
            if (z10 || z11) {
                BottomAppBar.this.I();
                BottomAppBar.this.X();
                BottomAppBar.this.W();
            }
            return windowInsetsCompat;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d extends AnimatorListenerAdapter {
        d() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BottomAppBar.this.M();
            BottomAppBar.this.f8341h = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BottomAppBar.this.N();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class e extends FloatingActionButton.b {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f8371a;

        /* loaded from: classes.dex */
        class a extends FloatingActionButton.b {
            a() {
            }

            @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.b
            public void b(FloatingActionButton floatingActionButton) {
                BottomAppBar.this.M();
            }
        }

        e(int i10) {
            this.f8371a = i10;
        }

        @Override // com.google.android.material.floatingactionbutton.FloatingActionButton.b
        public void a(FloatingActionButton floatingActionButton) {
            floatingActionButton.setTranslationX(BottomAppBar.this.R(this.f8371a));
            floatingActionButton.r(new a());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends AnimatorListenerAdapter {
        f() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BottomAppBar.this.M();
            BottomAppBar.this.f8352s = false;
            BottomAppBar.this.f8342i = null;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BottomAppBar.this.N();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class g extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        public boolean f8375a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ ActionMenuView f8376b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f8377c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ boolean f8378d;

        g(ActionMenuView actionMenuView, int i10, boolean z10) {
            this.f8376b = actionMenuView;
            this.f8377c = i10;
            this.f8378d = z10;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f8375a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f8375a) {
                return;
            }
            boolean z10 = BottomAppBar.this.f8351r != 0;
            BottomAppBar bottomAppBar = BottomAppBar.this;
            bottomAppBar.V(bottomAppBar.f8351r);
            BottomAppBar.this.b0(this.f8376b, this.f8377c, this.f8378d, z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class h implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ActionMenuView f8380e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ int f8381f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ boolean f8382g;

        h(ActionMenuView actionMenuView, int i10, boolean z10) {
            this.f8380e = actionMenuView;
            this.f8381f = i10;
            this.f8382g = z10;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f8380e.setTranslationX(BottomAppBar.this.Q(r0, this.f8381f, this.f8382g));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class i extends AnimatorListenerAdapter {
        i() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BottomAppBar.this.f8358y.onAnimationStart(animator);
            FloatingActionButton O = BottomAppBar.this.O();
            if (O != null) {
                O.setTranslationX(BottomAppBar.this.getFabTranslationX());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface j {
        void a(BottomAppBar bottomAppBar);

        void b(BottomAppBar bottomAppBar);
    }

    public BottomAppBar(Context context) {
        this(context, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void H(FloatingActionButton floatingActionButton) {
        floatingActionButton.d(this.f8358y);
        floatingActionButton.e(new i());
        floatingActionButton.f(this.f8359z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void I() {
        Animator animator = this.f8342i;
        if (animator != null) {
            animator.cancel();
        }
        Animator animator2 = this.f8341h;
        if (animator2 != null) {
            animator2.cancel();
        }
    }

    private void K(int i10, List<Animator> list) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(O(), "translationX", R(i10));
        ofFloat.setDuration(300L);
        list.add(ofFloat);
    }

    private void L(int i10, boolean z10, List<Animator> list) {
        ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView == null) {
            return;
        }
        Animator ofFloat = ObjectAnimator.ofFloat(actionMenuView, "alpha", 1.0f);
        if (Math.abs(actionMenuView.getTranslationX() - Q(actionMenuView, i10, z10)) <= 1.0f) {
            if (actionMenuView.getAlpha() < 1.0f) {
                list.add(ofFloat);
            }
        } else {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(actionMenuView, "alpha", 0.0f);
            ofFloat2.addListener(new g(actionMenuView, i10, z10));
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(150L);
            animatorSet.playSequentially(ofFloat2, ofFloat);
            list.add(animatorSet);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void M() {
        ArrayList<j> arrayList;
        int i10 = this.f8349p - 1;
        this.f8349p = i10;
        if (i10 != 0 || (arrayList = this.f8350q) == null) {
            return;
        }
        Iterator<j> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().b(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void N() {
        ArrayList<j> arrayList;
        int i10 = this.f8349p;
        this.f8349p = i10 + 1;
        if (i10 != 0 || (arrayList = this.f8350q) == null) {
            return;
        }
        Iterator<j> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FloatingActionButton O() {
        View P = P();
        if (P instanceof FloatingActionButton) {
            return (FloatingActionButton) P;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View P() {
        if (!(getParent() instanceof CoordinatorLayout)) {
            return null;
        }
        for (View view : ((CoordinatorLayout) getParent()).w(this)) {
            if ((view instanceof FloatingActionButton) || (view instanceof ExtendedFloatingActionButton)) {
                return view;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float R(int i10) {
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        if (i10 == 1) {
            return ((getMeasuredWidth() / 2) - (this.f8339f + (isLayoutRtl ? this.f8357x : this.f8356w))) * (isLayoutRtl ? -1 : 1);
        }
        return 0.0f;
    }

    private boolean S() {
        FloatingActionButton O = O();
        return O != null && O.n();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void T(int i10, boolean z10) {
        if (!ViewCompat.Q(this)) {
            this.f8352s = false;
            V(this.f8351r);
            return;
        }
        Animator animator = this.f8342i;
        if (animator != null) {
            animator.cancel();
        }
        ArrayList arrayList = new ArrayList();
        if (!S()) {
            i10 = 0;
            z10 = false;
        }
        L(i10, z10, arrayList);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        this.f8342i = animatorSet;
        animatorSet.addListener(new f());
        this.f8342i.start();
    }

    private void U(int i10) {
        if (this.f8343j == i10 || !ViewCompat.Q(this)) {
            return;
        }
        Animator animator = this.f8341h;
        if (animator != null) {
            animator.cancel();
        }
        ArrayList arrayList = new ArrayList();
        if (this.f8344k == 1) {
            K(i10, arrayList);
        } else {
            J(i10, arrayList);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        this.f8341h = animatorSet;
        animatorSet.addListener(new d());
        this.f8341h.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void W() {
        ActionMenuView actionMenuView = getActionMenuView();
        if (actionMenuView == null || this.f8342i != null) {
            return;
        }
        actionMenuView.setAlpha(1.0f);
        if (!S()) {
            a0(actionMenuView, 0, false);
        } else {
            a0(actionMenuView, this.f8343j, this.f8353t);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void X() {
        getTopEdgeTreatment().o(getFabTranslationX());
        View P = P();
        this.f8340g.b0((this.f8353t && S()) ? 1.0f : 0.0f);
        if (P != null) {
            P.setTranslationY(getFabTranslationY());
            P.setTranslationX(getFabTranslationX());
        }
    }

    private void a0(ActionMenuView actionMenuView, int i10, boolean z10) {
        b0(actionMenuView, i10, z10, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b0(ActionMenuView actionMenuView, int i10, boolean z10, boolean z11) {
        h hVar = new h(actionMenuView, i10, z10);
        if (z11) {
            actionMenuView.post(hVar);
        } else {
            hVar.run();
        }
    }

    private ActionMenuView getActionMenuView() {
        for (int i10 = 0; i10 < getChildCount(); i10++) {
            View childAt = getChildAt(i10);
            if (childAt instanceof ActionMenuView) {
                return (ActionMenuView) childAt;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getBottomInset() {
        return this.f8355v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getFabTranslationX() {
        return R(this.f8343j);
    }

    private float getFabTranslationY() {
        return -getTopEdgeTreatment().c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getLeftInset() {
        return this.f8357x;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getRightInset() {
        return this.f8356w;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BottomAppBarTopEdgeTreatment getTopEdgeTreatment() {
        return (BottomAppBarTopEdgeTreatment) this.f8340g.D().p();
    }

    private Drawable maybeTintNavigationIcon(Drawable drawable) {
        if (drawable == null || this.f8338e == null) {
            return drawable;
        }
        Drawable l10 = DrawableCompat.l(drawable.mutate());
        DrawableCompat.h(l10, this.f8338e.intValue());
        return l10;
    }

    protected void J(int i10, List<Animator> list) {
        FloatingActionButton O = O();
        if (O == null || O.m()) {
            return;
        }
        N();
        O.k(new e(i10));
    }

    protected int Q(ActionMenuView actionMenuView, int i10, boolean z10) {
        if (i10 != 1 || !z10) {
            return 0;
        }
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int measuredWidth = isLayoutRtl ? getMeasuredWidth() : 0;
        for (int i11 = 0; i11 < getChildCount(); i11++) {
            View childAt = getChildAt(i11);
            if ((childAt.getLayoutParams() instanceof Toolbar.LayoutParams) && (((Toolbar.LayoutParams) childAt.getLayoutParams()).f320a & 8388615) == 8388611) {
                if (isLayoutRtl) {
                    measuredWidth = Math.min(measuredWidth, childAt.getLeft());
                } else {
                    measuredWidth = Math.max(measuredWidth, childAt.getRight());
                }
            }
        }
        return measuredWidth - ((isLayoutRtl ? actionMenuView.getRight() : actionMenuView.getLeft()) + (isLayoutRtl ? this.f8356w : -this.f8357x));
    }

    public void V(int i10) {
        if (i10 != 0) {
            this.f8351r = 0;
            getMenu().clear();
            inflateMenu(i10);
        }
    }

    public void Y(int i10, int i11) {
        this.f8351r = i11;
        this.f8352s = true;
        T(i10, this.f8353t);
        U(i10);
        this.f8343j = i10;
    }

    boolean Z(int i10) {
        float f10 = i10;
        if (f10 == getTopEdgeTreatment().g()) {
            return false;
        }
        getTopEdgeTreatment().n(f10);
        this.f8340g.invalidateSelf();
        return true;
    }

    public ColorStateList getBackgroundTint() {
        return this.f8340g.H();
    }

    public float getCradleVerticalOffset() {
        return getTopEdgeTreatment().c();
    }

    public int getFabAlignmentMode() {
        return this.f8343j;
    }

    public int getFabAnimationMode() {
        return this.f8344k;
    }

    public float getFabCradleMargin() {
        return getTopEdgeTreatment().e();
    }

    public float getFabCradleRoundedCornerRadius() {
        return getTopEdgeTreatment().f();
    }

    public boolean getHideOnScroll() {
        return this.f8345l;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.f(this, this.f8340g);
        if (getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).setClipChildren(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (z10) {
            I();
            X();
        }
        W();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.f8343j = savedState.f8365e;
        this.f8353t = savedState.f8366f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f8365e = this.f8343j;
        savedState.f8366f = this.f8353t;
        return savedState;
    }

    public void setBackgroundTint(ColorStateList colorStateList) {
        DrawableCompat.i(this.f8340g, colorStateList);
    }

    public void setCradleVerticalOffset(float f10) {
        if (f10 != getCradleVerticalOffset()) {
            getTopEdgeTreatment().i(f10);
            this.f8340g.invalidateSelf();
            X();
        }
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        this.f8340g.Z(f10);
        getBehavior().h(this, this.f8340g.C() - this.f8340g.B());
    }

    public void setFabAlignmentMode(int i10) {
        Y(i10, 0);
    }

    public void setFabAnimationMode(int i10) {
        this.f8344k = i10;
    }

    void setFabCornerSize(float f10) {
        if (f10 != getTopEdgeTreatment().d()) {
            getTopEdgeTreatment().j(f10);
            this.f8340g.invalidateSelf();
        }
    }

    public void setFabCradleMargin(float f10) {
        if (f10 != getFabCradleMargin()) {
            getTopEdgeTreatment().k(f10);
            this.f8340g.invalidateSelf();
        }
    }

    public void setFabCradleRoundedCornerRadius(float f10) {
        if (f10 != getFabCradleRoundedCornerRadius()) {
            getTopEdgeTreatment().l(f10);
            this.f8340g.invalidateSelf();
        }
    }

    public void setHideOnScroll(boolean z10) {
        this.f8345l = z10;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationIcon(Drawable drawable) {
        super.setNavigationIcon(maybeTintNavigationIcon(drawable));
    }

    public void setNavigationIconTint(int i10) {
        this.f8338e = Integer.valueOf(i10);
        Drawable navigationIcon = getNavigationIcon();
        if (navigationIcon != null) {
            setNavigationIcon(navigationIcon);
        }
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitle(CharSequence charSequence) {
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(CharSequence charSequence) {
    }

    public BottomAppBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.bottomAppBarStyle);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public Behavior getBehavior() {
        if (this.f8354u == null) {
            this.f8354u = new Behavior();
        }
        return this.f8354u;
    }

    /* loaded from: classes.dex */
    public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {

        /* renamed from: e, reason: collision with root package name */
        private final Rect f8360e;

        /* renamed from: f, reason: collision with root package name */
        private WeakReference<BottomAppBar> f8361f;

        /* renamed from: g, reason: collision with root package name */
        private int f8362g;

        /* renamed from: h, reason: collision with root package name */
        private final View.OnLayoutChangeListener f8363h;

        /* loaded from: classes.dex */
        class a implements View.OnLayoutChangeListener {
            a() {
            }

            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
                BottomAppBar bottomAppBar = (BottomAppBar) Behavior.this.f8361f.get();
                if (bottomAppBar != null && (view instanceof FloatingActionButton)) {
                    FloatingActionButton floatingActionButton = (FloatingActionButton) view;
                    floatingActionButton.i(Behavior.this.f8360e);
                    int height = Behavior.this.f8360e.height();
                    bottomAppBar.Z(height);
                    bottomAppBar.setFabCornerSize(floatingActionButton.getShapeAppearanceModel().r().a(new RectF(Behavior.this.f8360e)));
                    CoordinatorLayout.e eVar = (CoordinatorLayout.e) view.getLayoutParams();
                    if (Behavior.this.f8362g == 0) {
                        ((ViewGroup.MarginLayoutParams) eVar).bottomMargin = bottomAppBar.getBottomInset() + (bottomAppBar.getResources().getDimensionPixelOffset(R$dimen.mtrl_bottomappbar_fab_bottom_margin) - ((floatingActionButton.getMeasuredHeight() - height) / 2));
                        ((ViewGroup.MarginLayoutParams) eVar).leftMargin = bottomAppBar.getLeftInset();
                        ((ViewGroup.MarginLayoutParams) eVar).rightMargin = bottomAppBar.getRightInset();
                        if (ViewUtils.isLayoutRtl(floatingActionButton)) {
                            ((ViewGroup.MarginLayoutParams) eVar).leftMargin += bottomAppBar.f8339f;
                            return;
                        } else {
                            ((ViewGroup.MarginLayoutParams) eVar).rightMargin += bottomAppBar.f8339f;
                            return;
                        }
                    }
                    return;
                }
                view.removeOnLayoutChangeListener(this);
            }
        }

        public Behavior() {
            this.f8363h = new a();
            this.f8360e = new Rect();
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: p, reason: merged with bridge method [inline-methods] */
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, int i10) {
            this.f8361f = new WeakReference<>(bottomAppBar);
            View P = bottomAppBar.P();
            if (P != null && !ViewCompat.Q(P)) {
                CoordinatorLayout.e eVar = (CoordinatorLayout.e) P.getLayoutParams();
                eVar.f2065d = 49;
                this.f8362g = ((ViewGroup.MarginLayoutParams) eVar).bottomMargin;
                if (P instanceof FloatingActionButton) {
                    FloatingActionButton floatingActionButton = (FloatingActionButton) P;
                    if (floatingActionButton.getShowMotionSpec() == null) {
                        floatingActionButton.setShowMotionSpecResource(R$animator.mtrl_fab_show_motion_spec);
                    }
                    if (floatingActionButton.getHideMotionSpec() == null) {
                        floatingActionButton.setHideMotionSpecResource(R$animator.mtrl_fab_hide_motion_spec);
                    }
                    floatingActionButton.addOnLayoutChangeListener(this.f8363h);
                    bottomAppBar.H(floatingActionButton);
                }
                bottomAppBar.X();
            }
            coordinatorLayout.M(bottomAppBar, i10);
            return super.onLayoutChild(coordinatorLayout, bottomAppBar, i10);
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: q, reason: merged with bridge method [inline-methods] */
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomAppBar bottomAppBar, View view, View view2, int i10, int i11) {
            return bottomAppBar.getHideOnScroll() && super.onStartNestedScroll(coordinatorLayout, bottomAppBar, view, view2, i10, i11);
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f8363h = new a();
            this.f8360e = new Rect();
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public BottomAppBar(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r6), attributeSet, i10);
        int i11 = A;
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        this.f8340g = materialShapeDrawable;
        this.f8349p = 0;
        this.f8351r = 0;
        this.f8352s = false;
        this.f8353t = true;
        this.f8358y = new a();
        this.f8359z = new b();
        Context context2 = getContext();
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.BottomAppBar, i10, i11, new int[0]);
        ColorStateList a10 = MaterialResources.a(context2, obtainStyledAttributes, R$styleable.BottomAppBar_backgroundTint);
        int i12 = R$styleable.BottomAppBar_navigationIconTint;
        if (obtainStyledAttributes.hasValue(i12)) {
            setNavigationIconTint(obtainStyledAttributes.getColor(i12, -1));
        }
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.BottomAppBar_elevation, 0);
        float dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.BottomAppBar_fabCradleMargin, 0);
        float dimensionPixelOffset2 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.BottomAppBar_fabCradleRoundedCornerRadius, 0);
        float dimensionPixelOffset3 = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.BottomAppBar_fabCradleVerticalOffset, 0);
        this.f8343j = obtainStyledAttributes.getInt(R$styleable.BottomAppBar_fabAlignmentMode, 0);
        this.f8344k = obtainStyledAttributes.getInt(R$styleable.BottomAppBar_fabAnimationMode, 0);
        this.f8345l = obtainStyledAttributes.getBoolean(R$styleable.BottomAppBar_hideOnScroll, false);
        this.f8346m = obtainStyledAttributes.getBoolean(R$styleable.BottomAppBar_paddingBottomSystemWindowInsets, false);
        this.f8347n = obtainStyledAttributes.getBoolean(R$styleable.BottomAppBar_paddingLeftSystemWindowInsets, false);
        this.f8348o = obtainStyledAttributes.getBoolean(R$styleable.BottomAppBar_paddingRightSystemWindowInsets, false);
        obtainStyledAttributes.recycle();
        this.f8339f = getResources().getDimensionPixelOffset(R$dimen.mtrl_bottomappbar_fabOffsetEndMode);
        materialShapeDrawable.setShapeAppearanceModel(ShapeAppearanceModel.a().F(new BottomAppBarTopEdgeTreatment(dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3)).m());
        materialShapeDrawable.i0(2);
        materialShapeDrawable.d0(Paint.Style.FILL);
        materialShapeDrawable.P(context2);
        setElevation(dimensionPixelSize);
        DrawableCompat.i(materialShapeDrawable, a10);
        ViewCompat.p0(this, materialShapeDrawable);
        ViewUtils.doOnApplyWindowInsets(this, attributeSet, i10, i11, new c());
    }
}
