package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import c4.ShapeAppearanceModel;
import com.google.android.material.R$animator;
import com.google.android.material.R$attr;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import d4.MaterialThemeOverlay;
import java.util.Iterator;
import java.util.List;
import p3.MotionSpec;

/* loaded from: classes.dex */
public class ExtendedFloatingActionButton extends MaterialButton implements CoordinatorLayout.b {
    private static final int L = R$style.Widget_MaterialComponents_ExtendedFloatingActionButton_Icon;
    static final Property<View, Float> M = new d(Float.class, "width");
    static final Property<View, Float> N = new e(Float.class, "height");
    static final Property<View, Float> O = new f(Float.class, "paddingStart");
    static final Property<View, Float> P = new g(Float.class, "paddingEnd");
    private final MotionStrategy A;
    private final MotionStrategy B;
    private final MotionStrategy C;
    private final int D;
    private int E;
    private int F;
    private final CoordinatorLayout.Behavior<ExtendedFloatingActionButton> G;
    private boolean H;
    private boolean I;
    private boolean J;
    protected ColorStateList K;

    /* renamed from: x, reason: collision with root package name */
    private int f8802x;

    /* renamed from: y, reason: collision with root package name */
    private final AnimatorTracker f8803y;

    /* renamed from: z, reason: collision with root package name */
    private final MotionStrategy f8804z;

    /* loaded from: classes.dex */
    class a implements l {
        a() {
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int a() {
            return ExtendedFloatingActionButton.this.getMeasuredHeight();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int b() {
            return ExtendedFloatingActionButton.this.F;
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int c() {
            return (ExtendedFloatingActionButton.this.getMeasuredWidth() - (ExtendedFloatingActionButton.this.getCollapsedPadding() * 2)) + ExtendedFloatingActionButton.this.E + ExtendedFloatingActionButton.this.F;
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public ViewGroup.LayoutParams d() {
            return new ViewGroup.LayoutParams(-2, -2);
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int e() {
            return ExtendedFloatingActionButton.this.E;
        }
    }

    /* loaded from: classes.dex */
    class b implements l {
        b() {
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int a() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int b() {
            return ExtendedFloatingActionButton.this.getCollapsedPadding();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int c() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public ViewGroup.LayoutParams d() {
            return new ViewGroup.LayoutParams(c(), a());
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int e() {
            return ExtendedFloatingActionButton.this.getCollapsedPadding();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f8810a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ MotionStrategy f8811b;

        c(MotionStrategy motionStrategy, j jVar) {
            this.f8811b = motionStrategy;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f8810a = true;
            this.f8811b.a();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f8811b.g();
            if (this.f8810a) {
                return;
            }
            this.f8811b.j(null);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f8811b.onAnimationStart(animator);
            this.f8810a = false;
        }
    }

    /* loaded from: classes.dex */
    class d extends Property<View, Float> {
        d(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(View view) {
            return Float.valueOf(view.getLayoutParams().width);
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, Float f10) {
            view.getLayoutParams().width = f10.intValue();
            view.requestLayout();
        }
    }

    /* loaded from: classes.dex */
    class e extends Property<View, Float> {
        e(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(View view) {
            return Float.valueOf(view.getLayoutParams().height);
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, Float f10) {
            view.getLayoutParams().height = f10.intValue();
            view.requestLayout();
        }
    }

    /* loaded from: classes.dex */
    class f extends Property<View, Float> {
        f(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(View view) {
            return Float.valueOf(ViewCompat.C(view));
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, Float f10) {
            ViewCompat.A0(view, f10.intValue(), view.getPaddingTop(), ViewCompat.B(view), view.getPaddingBottom());
        }
    }

    /* loaded from: classes.dex */
    class g extends Property<View, Float> {
        g(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Float get(View view) {
            return Float.valueOf(ViewCompat.B(view));
        }

        @Override // android.util.Property
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void set(View view, Float f10) {
            ViewCompat.A0(view, ViewCompat.C(view), view.getPaddingTop(), f10.intValue(), view.getPaddingBottom());
        }
    }

    /* loaded from: classes.dex */
    class h extends BaseMotionStrategy {

        /* renamed from: g, reason: collision with root package name */
        private final l f8813g;

        /* renamed from: h, reason: collision with root package name */
        private final boolean f8814h;

        h(AnimatorTracker animatorTracker, l lVar, boolean z10) {
            super(ExtendedFloatingActionButton.this, animatorTracker);
            this.f8813g = lVar;
            this.f8814h = z10;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public int c() {
            if (this.f8814h) {
                return R$animator.mtrl_extended_fab_change_size_expand_motion_spec;
            }
            return R$animator.mtrl_extended_fab_change_size_collapse_motion_spec;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public void d() {
            ExtendedFloatingActionButton.this.H = this.f8814h;
            ViewGroup.LayoutParams layoutParams = ExtendedFloatingActionButton.this.getLayoutParams();
            if (layoutParams == null) {
                return;
            }
            layoutParams.width = this.f8813g.d().width;
            layoutParams.height = this.f8813g.d().height;
            ViewCompat.A0(ExtendedFloatingActionButton.this, this.f8813g.e(), ExtendedFloatingActionButton.this.getPaddingTop(), this.f8813g.b(), ExtendedFloatingActionButton.this.getPaddingBottom());
            ExtendedFloatingActionButton.this.requestLayout();
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public boolean f() {
            return this.f8814h == ExtendedFloatingActionButton.this.H || ExtendedFloatingActionButton.this.getIcon() == null || TextUtils.isEmpty(ExtendedFloatingActionButton.this.getText());
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public void g() {
            super.g();
            ExtendedFloatingActionButton.this.I = false;
            ExtendedFloatingActionButton.this.setHorizontallyScrolling(false);
            ViewGroup.LayoutParams layoutParams = ExtendedFloatingActionButton.this.getLayoutParams();
            if (layoutParams == null) {
                return;
            }
            layoutParams.width = this.f8813g.d().width;
            layoutParams.height = this.f8813g.d().height;
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public AnimatorSet h() {
            MotionSpec m10 = m();
            if (m10.j("width")) {
                PropertyValuesHolder[] g6 = m10.g("width");
                g6[0].setFloatValues(ExtendedFloatingActionButton.this.getWidth(), this.f8813g.c());
                m10.l("width", g6);
            }
            if (m10.j("height")) {
                PropertyValuesHolder[] g10 = m10.g("height");
                g10[0].setFloatValues(ExtendedFloatingActionButton.this.getHeight(), this.f8813g.a());
                m10.l("height", g10);
            }
            if (m10.j("paddingStart")) {
                PropertyValuesHolder[] g11 = m10.g("paddingStart");
                g11[0].setFloatValues(ViewCompat.C(ExtendedFloatingActionButton.this), this.f8813g.e());
                m10.l("paddingStart", g11);
            }
            if (m10.j("paddingEnd")) {
                PropertyValuesHolder[] g12 = m10.g("paddingEnd");
                g12[0].setFloatValues(ViewCompat.B(ExtendedFloatingActionButton.this), this.f8813g.b());
                m10.l("paddingEnd", g12);
            }
            if (m10.j("labelOpacity")) {
                PropertyValuesHolder[] g13 = m10.g("labelOpacity");
                boolean z10 = this.f8814h;
                g13[0].setFloatValues(z10 ? 0.0f : 1.0f, z10 ? 1.0f : 0.0f);
                m10.l("labelOpacity", g13);
            }
            return super.l(m10);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public void j(j jVar) {
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            ExtendedFloatingActionButton.this.H = this.f8814h;
            ExtendedFloatingActionButton.this.I = true;
            ExtendedFloatingActionButton.this.setHorizontallyScrolling(true);
        }
    }

    /* loaded from: classes.dex */
    class i extends BaseMotionStrategy {

        /* renamed from: g, reason: collision with root package name */
        private boolean f8816g;

        public i(AnimatorTracker animatorTracker) {
            super(ExtendedFloatingActionButton.this, animatorTracker);
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public void a() {
            super.a();
            this.f8816g = true;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public int c() {
            return R$animator.mtrl_extended_fab_hide_motion_spec;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public void d() {
            ExtendedFloatingActionButton.this.setVisibility(8);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public boolean f() {
            return ExtendedFloatingActionButton.this.w();
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public void g() {
            super.g();
            ExtendedFloatingActionButton.this.f8802x = 0;
            if (this.f8816g) {
                return;
            }
            ExtendedFloatingActionButton.this.setVisibility(8);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public void j(j jVar) {
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            this.f8816g = false;
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.f8802x = 1;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class j {
    }

    /* loaded from: classes.dex */
    class k extends BaseMotionStrategy {
        public k(AnimatorTracker animatorTracker) {
            super(ExtendedFloatingActionButton.this, animatorTracker);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public int c() {
            return R$animator.mtrl_extended_fab_show_motion_spec;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public void d() {
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.setAlpha(1.0f);
            ExtendedFloatingActionButton.this.setScaleY(1.0f);
            ExtendedFloatingActionButton.this.setScaleX(1.0f);
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public boolean f() {
            return ExtendedFloatingActionButton.this.x();
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public void g() {
            super.g();
            ExtendedFloatingActionButton.this.f8802x = 0;
        }

        @Override // com.google.android.material.floatingactionbutton.MotionStrategy
        public void j(j jVar) {
        }

        @Override // com.google.android.material.floatingactionbutton.BaseMotionStrategy, com.google.android.material.floatingactionbutton.MotionStrategy
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.f8802x = 2;
        }
    }

    /* loaded from: classes.dex */
    interface l {
        int a();

        int b();

        int c();

        ViewGroup.LayoutParams d();

        int e();
    }

    public ExtendedFloatingActionButton(Context context) {
        this(context, null);
    }

    private boolean A() {
        return (ViewCompat.Q(this) || (!x() && this.J)) && !isInEditMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean w() {
        return getVisibility() == 0 ? this.f8802x == 1 : this.f8802x != 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean x() {
        return getVisibility() != 0 ? this.f8802x == 2 : this.f8802x != 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y(MotionStrategy motionStrategy, j jVar) {
        if (motionStrategy.f()) {
            return;
        }
        if (!A()) {
            motionStrategy.d();
            motionStrategy.j(jVar);
            return;
        }
        measure(0, 0);
        AnimatorSet h10 = motionStrategy.h();
        h10.addListener(new c(motionStrategy, jVar));
        Iterator<Animator.AnimatorListener> it = motionStrategy.i().iterator();
        while (it.hasNext()) {
            h10.addListener(it.next());
        }
        h10.start();
    }

    private void z() {
        this.K = getTextColors();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void B(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public CoordinatorLayout.Behavior<ExtendedFloatingActionButton> getBehavior() {
        return this.G;
    }

    int getCollapsedPadding() {
        return (getCollapsedSize() - getIconSize()) / 2;
    }

    int getCollapsedSize() {
        int i10 = this.D;
        return i10 < 0 ? (Math.min(ViewCompat.C(this), ViewCompat.B(this)) * 2) + getIconSize() : i10;
    }

    public MotionSpec getExtendMotionSpec() {
        return this.A.e();
    }

    public MotionSpec getHideMotionSpec() {
        return this.C.e();
    }

    public MotionSpec getShowMotionSpec() {
        return this.B.e();
    }

    public MotionSpec getShrinkMotionSpec() {
        return this.f8804z.e();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.button.MaterialButton, android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.H && TextUtils.isEmpty(getText()) && getIcon() != null) {
            this.H = false;
            this.f8804z.d();
        }
    }

    public void setAnimateShowBeforeLayout(boolean z10) {
        this.J = z10;
    }

    public void setExtendMotionSpec(MotionSpec motionSpec) {
        this.A.b(motionSpec);
    }

    public void setExtendMotionSpecResource(int i10) {
        setExtendMotionSpec(MotionSpec.d(getContext(), i10));
    }

    public void setExtended(boolean z10) {
        if (this.H == z10) {
            return;
        }
        MotionStrategy motionStrategy = z10 ? this.A : this.f8804z;
        if (motionStrategy.f()) {
            return;
        }
        motionStrategy.d();
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        this.C.b(motionSpec);
    }

    public void setHideMotionSpecResource(int i10) {
        setHideMotionSpec(MotionSpec.d(getContext(), i10));
    }

    @Override // android.widget.TextView, android.view.View
    public void setPadding(int i10, int i11, int i12, int i13) {
        super.setPadding(i10, i11, i12, i13);
        if (!this.H || this.I) {
            return;
        }
        this.E = ViewCompat.C(this);
        this.F = ViewCompat.B(this);
    }

    @Override // android.widget.TextView, android.view.View
    public void setPaddingRelative(int i10, int i11, int i12, int i13) {
        super.setPaddingRelative(i10, i11, i12, i13);
        if (!this.H || this.I) {
            return;
        }
        this.E = i10;
        this.F = i12;
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        this.B.b(motionSpec);
    }

    public void setShowMotionSpecResource(int i10) {
        setShowMotionSpec(MotionSpec.d(getContext(), i10));
    }

    public void setShrinkMotionSpec(MotionSpec motionSpec) {
        this.f8804z.b(motionSpec);
    }

    public void setShrinkMotionSpecResource(int i10) {
        setShrinkMotionSpec(MotionSpec.d(getContext(), i10));
    }

    @Override // android.widget.TextView
    public void setTextColor(int i10) {
        super.setTextColor(i10);
        z();
    }

    public ExtendedFloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.extendedFloatingActionButtonStyle);
    }

    /* loaded from: classes.dex */
    protected static class ExtendedFloatingActionButtonBehavior<T extends ExtendedFloatingActionButton> extends CoordinatorLayout.Behavior<T> {

        /* renamed from: a, reason: collision with root package name */
        private Rect f8805a;

        /* renamed from: b, reason: collision with root package name */
        private boolean f8806b;

        /* renamed from: c, reason: collision with root package name */
        private boolean f8807c;

        public ExtendedFloatingActionButtonBehavior() {
            this.f8806b = false;
            this.f8807c = true;
        }

        private static boolean f(View view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.e) {
                return ((CoordinatorLayout.e) layoutParams).f() instanceof BottomSheetBehavior;
            }
            return false;
        }

        private boolean i(View view, ExtendedFloatingActionButton extendedFloatingActionButton) {
            return (this.f8806b || this.f8807c) && ((CoordinatorLayout.e) extendedFloatingActionButton.getLayoutParams()).e() == view.getId();
        }

        private boolean k(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, ExtendedFloatingActionButton extendedFloatingActionButton) {
            if (!i(appBarLayout, extendedFloatingActionButton)) {
                return false;
            }
            if (this.f8805a == null) {
                this.f8805a = new Rect();
            }
            Rect rect = this.f8805a;
            DescendantOffsetUtils.getDescendantRect(coordinatorLayout, appBarLayout, rect);
            if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                j(extendedFloatingActionButton);
                return true;
            }
            d(extendedFloatingActionButton);
            return true;
        }

        private boolean l(View view, ExtendedFloatingActionButton extendedFloatingActionButton) {
            if (!i(view, extendedFloatingActionButton)) {
                return false;
            }
            if (view.getTop() < (extendedFloatingActionButton.getHeight() / 2) + ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.e) extendedFloatingActionButton.getLayoutParams())).topMargin) {
                j(extendedFloatingActionButton);
                return true;
            }
            d(extendedFloatingActionButton);
            return true;
        }

        protected void d(ExtendedFloatingActionButton extendedFloatingActionButton) {
            MotionStrategy motionStrategy;
            if (this.f8807c) {
                motionStrategy = extendedFloatingActionButton.A;
            } else {
                motionStrategy = extendedFloatingActionButton.B;
            }
            extendedFloatingActionButton.y(motionStrategy, null);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: e, reason: merged with bridge method [inline-methods] */
        public boolean getInsetDodgeRect(CoordinatorLayout coordinatorLayout, ExtendedFloatingActionButton extendedFloatingActionButton, Rect rect) {
            return super.getInsetDodgeRect(coordinatorLayout, extendedFloatingActionButton, rect);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: g, reason: merged with bridge method [inline-methods] */
        public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, ExtendedFloatingActionButton extendedFloatingActionButton, View view) {
            if (view instanceof AppBarLayout) {
                k(coordinatorLayout, (AppBarLayout) view, extendedFloatingActionButton);
                return false;
            }
            if (!f(view)) {
                return false;
            }
            l(view, extendedFloatingActionButton);
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: h, reason: merged with bridge method [inline-methods] */
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, ExtendedFloatingActionButton extendedFloatingActionButton, int i10) {
            List<View> v7 = coordinatorLayout.v(extendedFloatingActionButton);
            int size = v7.size();
            for (int i11 = 0; i11 < size; i11++) {
                View view = v7.get(i11);
                if (view instanceof AppBarLayout) {
                    if (k(coordinatorLayout, (AppBarLayout) view, extendedFloatingActionButton)) {
                        break;
                    }
                } else {
                    if (f(view) && l(view, extendedFloatingActionButton)) {
                        break;
                    }
                }
            }
            coordinatorLayout.M(extendedFloatingActionButton, i10);
            return true;
        }

        protected void j(ExtendedFloatingActionButton extendedFloatingActionButton) {
            MotionStrategy motionStrategy;
            if (this.f8807c) {
                motionStrategy = extendedFloatingActionButton.f8804z;
            } else {
                motionStrategy = extendedFloatingActionButton.C;
            }
            extendedFloatingActionButton.y(motionStrategy, null);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void onAttachedToLayoutParams(CoordinatorLayout.e eVar) {
            if (eVar.f2069h == 0) {
                eVar.f2069h = 80;
            }
        }

        public ExtendedFloatingActionButtonBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ExtendedFloatingActionButton_Behavior_Layout);
            this.f8806b = obtainStyledAttributes.getBoolean(R$styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoHide, false);
            this.f8807c = obtainStyledAttributes.getBoolean(R$styleable.ExtendedFloatingActionButton_Behavior_Layout_behavior_autoShrink, true);
            obtainStyledAttributes.recycle();
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ExtendedFloatingActionButton(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r9), attributeSet, i10);
        int i11 = L;
        this.f8802x = 0;
        AnimatorTracker animatorTracker = new AnimatorTracker();
        this.f8803y = animatorTracker;
        k kVar = new k(animatorTracker);
        this.B = kVar;
        i iVar = new i(animatorTracker);
        this.C = iVar;
        this.H = true;
        this.I = false;
        this.J = false;
        Context context2 = getContext();
        this.G = new ExtendedFloatingActionButtonBehavior(context2, attributeSet);
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, R$styleable.ExtendedFloatingActionButton, i10, i11, new int[0]);
        MotionSpec c10 = MotionSpec.c(context2, obtainStyledAttributes, R$styleable.ExtendedFloatingActionButton_showMotionSpec);
        MotionSpec c11 = MotionSpec.c(context2, obtainStyledAttributes, R$styleable.ExtendedFloatingActionButton_hideMotionSpec);
        MotionSpec c12 = MotionSpec.c(context2, obtainStyledAttributes, R$styleable.ExtendedFloatingActionButton_extendMotionSpec);
        MotionSpec c13 = MotionSpec.c(context2, obtainStyledAttributes, R$styleable.ExtendedFloatingActionButton_shrinkMotionSpec);
        this.D = obtainStyledAttributes.getDimensionPixelSize(R$styleable.ExtendedFloatingActionButton_collapsedSize, -1);
        this.E = ViewCompat.C(this);
        this.F = ViewCompat.B(this);
        AnimatorTracker animatorTracker2 = new AnimatorTracker();
        h hVar = new h(animatorTracker2, new a(), true);
        this.A = hVar;
        h hVar2 = new h(animatorTracker2, new b(), false);
        this.f8804z = hVar2;
        kVar.b(c10);
        iVar.b(c11);
        hVar.b(c12);
        hVar2.b(c13);
        obtainStyledAttributes.recycle();
        setShapeAppearanceModel(ShapeAppearanceModel.g(context2, attributeSet, i10, i11, ShapeAppearanceModel.f4815m).m());
        z();
    }

    @Override // android.widget.TextView
    public void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        z();
    }
}
