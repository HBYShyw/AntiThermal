package com.google.android.material.tabs;

import a4.RippleUtils;
import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import c.AppCompatResources;
import c4.MaterialShapeDrawable;
import c4.MaterialShapeUtils;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$layout;
import com.google.android.material.R$string;
import com.google.android.material.R$style;
import com.google.android.material.R$styleable;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.oplus.statistics.DataTypeConstants;
import d4.MaterialThemeOverlay;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import p3.AnimationUtils;
import z3.MaterialResources;

@ViewPager.e
/* loaded from: classes.dex */
public class TabLayout extends HorizontalScrollView {
    private static final int V = R$style.Widget_Design_TabLayout;
    private static final androidx.core.util.e<g> W = new androidx.core.util.g(16);
    int A;
    int B;
    int C;
    int D;
    boolean E;
    boolean F;
    int G;
    int H;
    boolean I;
    private TabIndicatorInterpolator J;
    private c K;
    private final ArrayList<c> L;
    private c M;
    private ValueAnimator N;
    ViewPager O;
    private PagerAdapter P;
    private DataSetObserver Q;
    private h R;
    private b S;
    private boolean T;
    private final androidx.core.util.e<TabView> U;

    /* renamed from: e, reason: collision with root package name */
    private final ArrayList<g> f9264e;

    /* renamed from: f, reason: collision with root package name */
    private g f9265f;

    /* renamed from: g, reason: collision with root package name */
    final f f9266g;

    /* renamed from: h, reason: collision with root package name */
    int f9267h;

    /* renamed from: i, reason: collision with root package name */
    int f9268i;

    /* renamed from: j, reason: collision with root package name */
    int f9269j;

    /* renamed from: k, reason: collision with root package name */
    int f9270k;

    /* renamed from: l, reason: collision with root package name */
    int f9271l;

    /* renamed from: m, reason: collision with root package name */
    ColorStateList f9272m;

    /* renamed from: n, reason: collision with root package name */
    ColorStateList f9273n;

    /* renamed from: o, reason: collision with root package name */
    ColorStateList f9274o;

    /* renamed from: p, reason: collision with root package name */
    Drawable f9275p;

    /* renamed from: q, reason: collision with root package name */
    private int f9276q;

    /* renamed from: r, reason: collision with root package name */
    PorterDuff.Mode f9277r;

    /* renamed from: s, reason: collision with root package name */
    float f9278s;

    /* renamed from: t, reason: collision with root package name */
    float f9279t;

    /* renamed from: u, reason: collision with root package name */
    final int f9280u;

    /* renamed from: v, reason: collision with root package name */
    int f9281v;

    /* renamed from: w, reason: collision with root package name */
    private final int f9282w;

    /* renamed from: x, reason: collision with root package name */
    private final int f9283x;

    /* renamed from: y, reason: collision with root package name */
    private final int f9284y;

    /* renamed from: z, reason: collision with root package name */
    private int f9285z;

    /* loaded from: classes.dex */
    public final class TabView extends LinearLayout {

        /* renamed from: e, reason: collision with root package name */
        private g f9286e;

        /* renamed from: f, reason: collision with root package name */
        private TextView f9287f;

        /* renamed from: g, reason: collision with root package name */
        private ImageView f9288g;

        /* renamed from: h, reason: collision with root package name */
        private View f9289h;

        /* renamed from: i, reason: collision with root package name */
        private BadgeDrawable f9290i;

        /* renamed from: j, reason: collision with root package name */
        private View f9291j;

        /* renamed from: k, reason: collision with root package name */
        private TextView f9292k;

        /* renamed from: l, reason: collision with root package name */
        private ImageView f9293l;

        /* renamed from: m, reason: collision with root package name */
        private Drawable f9294m;

        /* renamed from: n, reason: collision with root package name */
        private int f9295n;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a implements View.OnLayoutChangeListener {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ View f9297a;

            a(View view) {
                this.f9297a = view;
            }

            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
                if (this.f9297a.getVisibility() == 0) {
                    TabView.this.s(this.f9297a);
                }
            }
        }

        public TabView(Context context) {
            super(context);
            this.f9295n = 2;
            u(context);
            ViewCompat.A0(this, TabLayout.this.f9267h, TabLayout.this.f9268i, TabLayout.this.f9269j, TabLayout.this.f9270k);
            setGravity(17);
            setOrientation(!TabLayout.this.E ? 1 : 0);
            setClickable(true);
            ViewCompat.B0(this, PointerIconCompat.b(getContext(), DataTypeConstants.APP_LOG));
        }

        private void f(View view) {
            if (view == null) {
                return;
            }
            view.addOnLayoutChangeListener(new a(view));
        }

        private float g(Layout layout, int i10, float f10) {
            return layout.getLineWidth(i10) * (f10 / layout.getPaint().getTextSize());
        }

        private BadgeDrawable getBadge() {
            return this.f9290i;
        }

        private BadgeDrawable getOrCreateBadge() {
            if (this.f9290i == null) {
                this.f9290i = BadgeDrawable.b(getContext());
            }
            r();
            BadgeDrawable badgeDrawable = this.f9290i;
            if (badgeDrawable != null) {
                return badgeDrawable;
            }
            throw new IllegalStateException("Unable to create badge");
        }

        private void h(boolean z10) {
            setClipChildren(z10);
            setClipToPadding(z10);
            ViewGroup viewGroup = (ViewGroup) getParent();
            if (viewGroup != null) {
                viewGroup.setClipChildren(z10);
                viewGroup.setClipToPadding(z10);
            }
        }

        private FrameLayout i() {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            return frameLayout;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void j(Canvas canvas) {
            Drawable drawable = this.f9294m;
            if (drawable != null) {
                drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
                this.f9294m.draw(canvas);
            }
        }

        private FrameLayout k(View view) {
            if ((view == this.f9288g || view == this.f9287f) && BadgeUtils.f8315a) {
                return (FrameLayout) view.getParent();
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean l() {
            return this.f9290i != null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void m() {
            FrameLayout frameLayout;
            if (BadgeUtils.f8315a) {
                frameLayout = i();
                addView(frameLayout, 0);
            } else {
                frameLayout = this;
            }
            ImageView imageView = (ImageView) LayoutInflater.from(getContext()).inflate(R$layout.design_layout_tab_icon, (ViewGroup) frameLayout, false);
            this.f9288g = imageView;
            frameLayout.addView(imageView, 0);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void n() {
            FrameLayout frameLayout;
            if (BadgeUtils.f8315a) {
                frameLayout = i();
                addView(frameLayout);
            } else {
                frameLayout = this;
            }
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R$layout.design_layout_tab_text, (ViewGroup) frameLayout, false);
            this.f9287f = textView;
            frameLayout.addView(textView);
        }

        private void p(View view) {
            if (l() && view != null) {
                h(false);
                BadgeUtils.a(this.f9290i, view, k(view));
                this.f9289h = view;
            }
        }

        private void q() {
            if (l()) {
                h(true);
                View view = this.f9289h;
                if (view != null) {
                    BadgeUtils.d(this.f9290i, view);
                    this.f9289h = null;
                }
            }
        }

        private void r() {
            g gVar;
            g gVar2;
            if (l()) {
                if (this.f9291j != null) {
                    q();
                    return;
                }
                if (this.f9288g != null && (gVar2 = this.f9286e) != null && gVar2.f() != null) {
                    View view = this.f9289h;
                    ImageView imageView = this.f9288g;
                    if (view != imageView) {
                        q();
                        p(this.f9288g);
                        return;
                    } else {
                        s(imageView);
                        return;
                    }
                }
                if (this.f9287f != null && (gVar = this.f9286e) != null && gVar.h() == 1) {
                    View view2 = this.f9289h;
                    TextView textView = this.f9287f;
                    if (view2 != textView) {
                        q();
                        p(this.f9287f);
                        return;
                    } else {
                        s(textView);
                        return;
                    }
                }
                q();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void s(View view) {
            if (l() && view == this.f9289h) {
                BadgeUtils.e(this.f9290i, view, k(view));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v0, types: [android.graphics.drawable.RippleDrawable] */
        public void u(Context context) {
            int i10 = TabLayout.this.f9280u;
            if (i10 != 0) {
                Drawable b10 = AppCompatResources.b(context, i10);
                this.f9294m = b10;
                if (b10 != null && b10.isStateful()) {
                    this.f9294m.setState(getDrawableState());
                }
            } else {
                this.f9294m = null;
            }
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(0);
            if (TabLayout.this.f9274o != null) {
                GradientDrawable gradientDrawable2 = new GradientDrawable();
                gradientDrawable2.setCornerRadius(1.0E-5f);
                gradientDrawable2.setColor(-1);
                ColorStateList a10 = RippleUtils.a(TabLayout.this.f9274o);
                boolean z10 = TabLayout.this.I;
                if (z10) {
                    gradientDrawable = null;
                }
                gradientDrawable = new RippleDrawable(a10, gradientDrawable, z10 ? null : gradientDrawable2);
            }
            ViewCompat.p0(this, gradientDrawable);
            TabLayout.this.invalidate();
        }

        private void w(TextView textView, ImageView imageView) {
            g gVar = this.f9286e;
            Drawable mutate = (gVar == null || gVar.f() == null) ? null : DrawableCompat.l(this.f9286e.f()).mutate();
            if (mutate != null) {
                DrawableCompat.i(mutate, TabLayout.this.f9273n);
                PorterDuff.Mode mode = TabLayout.this.f9277r;
                if (mode != null) {
                    DrawableCompat.j(mutate, mode);
                }
            }
            g gVar2 = this.f9286e;
            CharSequence i10 = gVar2 != null ? gVar2.i() : null;
            if (imageView != null) {
                if (mutate != null) {
                    imageView.setImageDrawable(mutate);
                    imageView.setVisibility(0);
                    setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
            }
            boolean z10 = !TextUtils.isEmpty(i10);
            if (textView != null) {
                if (z10) {
                    textView.setText(i10);
                    if (this.f9286e.f9319g == 1) {
                        textView.setVisibility(0);
                    } else {
                        textView.setVisibility(8);
                    }
                    setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText((CharSequence) null);
                }
            }
            if (imageView != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                int dpToPx = (z10 && imageView.getVisibility() == 0) ? (int) ViewUtils.dpToPx(getContext(), 8) : 0;
                if (TabLayout.this.E) {
                    if (dpToPx != MarginLayoutParamsCompat.a(marginLayoutParams)) {
                        MarginLayoutParamsCompat.c(marginLayoutParams, dpToPx);
                        marginLayoutParams.bottomMargin = 0;
                        imageView.setLayoutParams(marginLayoutParams);
                        imageView.requestLayout();
                    }
                } else if (dpToPx != marginLayoutParams.bottomMargin) {
                    marginLayoutParams.bottomMargin = dpToPx;
                    MarginLayoutParamsCompat.c(marginLayoutParams, 0);
                    imageView.setLayoutParams(marginLayoutParams);
                    imageView.requestLayout();
                }
            }
            g gVar3 = this.f9286e;
            CharSequence charSequence = gVar3 != null ? gVar3.f9316d : null;
            if (!z10) {
                i10 = charSequence;
            }
            TooltipCompat.a(this, i10);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            int[] drawableState = getDrawableState();
            Drawable drawable = this.f9294m;
            boolean z10 = false;
            if (drawable != null && drawable.isStateful()) {
                z10 = false | this.f9294m.setState(drawableState);
            }
            if (z10) {
                invalidate();
                TabLayout.this.invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getContentHeight() {
            View[] viewArr = {this.f9287f, this.f9288g, this.f9291j};
            int i10 = 0;
            int i11 = 0;
            boolean z10 = false;
            for (int i12 = 0; i12 < 3; i12++) {
                View view = viewArr[i12];
                if (view != null && view.getVisibility() == 0) {
                    i11 = z10 ? Math.min(i11, view.getTop()) : view.getTop();
                    i10 = z10 ? Math.max(i10, view.getBottom()) : view.getBottom();
                    z10 = true;
                }
            }
            return i10 - i11;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getContentWidth() {
            View[] viewArr = {this.f9287f, this.f9288g, this.f9291j};
            int i10 = 0;
            int i11 = 0;
            boolean z10 = false;
            for (int i12 = 0; i12 < 3; i12++) {
                View view = viewArr[i12];
                if (view != null && view.getVisibility() == 0) {
                    i11 = z10 ? Math.min(i11, view.getLeft()) : view.getLeft();
                    i10 = z10 ? Math.max(i10, view.getRight()) : view.getRight();
                    z10 = true;
                }
            }
            return i10 - i11;
        }

        public g getTab() {
            return this.f9286e;
        }

        void o() {
            setTab(null);
            setSelected(false);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            BadgeDrawable badgeDrawable = this.f9290i;
            if (badgeDrawable != null && badgeDrawable.isVisible()) {
                accessibilityNodeInfo.setContentDescription(((Object) getContentDescription()) + ", " + ((Object) this.f9290i.f()));
            }
            AccessibilityNodeInfoCompat C0 = AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo);
            C0.Y(AccessibilityNodeInfoCompat.c.a(0, 1, this.f9286e.g(), 1, false, isSelected()));
            if (isSelected()) {
                C0.W(false);
                C0.O(AccessibilityNodeInfoCompat.a.f2322i);
            }
            C0.q0(getResources().getString(R$string.item_view_role_description));
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onMeasure(int i10, int i11) {
            Layout layout;
            int size = View.MeasureSpec.getSize(i10);
            int mode = View.MeasureSpec.getMode(i10);
            int tabMaxWidth = TabLayout.this.getTabMaxWidth();
            if (tabMaxWidth > 0 && (mode == 0 || size > tabMaxWidth)) {
                i10 = View.MeasureSpec.makeMeasureSpec(TabLayout.this.f9281v, Integer.MIN_VALUE);
            }
            super.onMeasure(i10, i11);
            if (this.f9287f != null) {
                float f10 = TabLayout.this.f9278s;
                int i12 = this.f9295n;
                ImageView imageView = this.f9288g;
                boolean z10 = true;
                if (imageView == null || imageView.getVisibility() != 0) {
                    TextView textView = this.f9287f;
                    if (textView != null && textView.getLineCount() > 1) {
                        f10 = TabLayout.this.f9279t;
                    }
                } else {
                    i12 = 1;
                }
                float textSize = this.f9287f.getTextSize();
                int lineCount = this.f9287f.getLineCount();
                int d10 = TextViewCompat.d(this.f9287f);
                if (f10 != textSize || (d10 >= 0 && i12 != d10)) {
                    if (TabLayout.this.D == 1 && f10 > textSize && lineCount == 1 && ((layout = this.f9287f.getLayout()) == null || g(layout, 0, f10) > (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight())) {
                        z10 = false;
                    }
                    if (z10) {
                        this.f9287f.setTextSize(0, f10);
                        this.f9287f.setMaxLines(i12);
                        super.onMeasure(i10, i11);
                    }
                }
            }
        }

        @Override // android.view.View
        public boolean performClick() {
            boolean performClick = super.performClick();
            if (this.f9286e == null) {
                return performClick;
            }
            if (!performClick) {
                playSoundEffect(0);
            }
            this.f9286e.l();
            return true;
        }

        @Override // android.view.View
        public void setSelected(boolean z10) {
            if (isSelected() != z10) {
            }
            super.setSelected(z10);
            TextView textView = this.f9287f;
            if (textView != null) {
                textView.setSelected(z10);
            }
            ImageView imageView = this.f9288g;
            if (imageView != null) {
                imageView.setSelected(z10);
            }
            View view = this.f9291j;
            if (view != null) {
                view.setSelected(z10);
            }
        }

        void setTab(g gVar) {
            if (gVar != this.f9286e) {
                this.f9286e = gVar;
                t();
            }
        }

        final void t() {
            g gVar = this.f9286e;
            View e10 = gVar != null ? gVar.e() : null;
            if (e10 != null) {
                ViewParent parent = e10.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(e10);
                    }
                    addView(e10);
                }
                this.f9291j = e10;
                TextView textView = this.f9287f;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.f9288g;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.f9288g.setImageDrawable(null);
                }
                TextView textView2 = (TextView) e10.findViewById(R.id.text1);
                this.f9292k = textView2;
                if (textView2 != null) {
                    this.f9295n = TextViewCompat.d(textView2);
                }
                this.f9293l = (ImageView) e10.findViewById(R.id.icon);
            } else {
                View view = this.f9291j;
                if (view != null) {
                    removeView(view);
                    this.f9291j = null;
                }
                this.f9292k = null;
                this.f9293l = null;
            }
            if (this.f9291j == null) {
                if (this.f9288g == null) {
                    m();
                }
                if (this.f9287f == null) {
                    n();
                    this.f9295n = TextViewCompat.d(this.f9287f);
                }
                TextViewCompat.n(this.f9287f, TabLayout.this.f9271l);
                ColorStateList colorStateList = TabLayout.this.f9272m;
                if (colorStateList != null) {
                    this.f9287f.setTextColor(colorStateList);
                }
                w(this.f9287f, this.f9288g);
                r();
                f(this.f9288g);
                f(this.f9287f);
            } else {
                TextView textView3 = this.f9292k;
                if (textView3 != null || this.f9293l != null) {
                    w(textView3, this.f9293l);
                }
            }
            if (gVar != null && !TextUtils.isEmpty(gVar.f9316d)) {
                setContentDescription(gVar.f9316d);
            }
            setSelected(gVar != null && gVar.j());
        }

        final void v() {
            setOrientation(!TabLayout.this.E ? 1 : 0);
            TextView textView = this.f9292k;
            if (textView == null && this.f9293l == null) {
                w(this.f9287f, this.f9288g);
            } else {
                w(textView, this.f9293l);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            TabLayout.this.scrollTo(((Integer) valueAnimator.getAnimatedValue()).intValue(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements ViewPager.h {

        /* renamed from: a, reason: collision with root package name */
        private boolean f9300a;

        b() {
        }

        @Override // androidx.viewpager.widget.ViewPager.h
        public void a(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            TabLayout tabLayout = TabLayout.this;
            if (tabLayout.O == viewPager) {
                tabLayout.H(pagerAdapter2, this.f9300a);
            }
        }

        void b(boolean z10) {
            this.f9300a = z10;
        }
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface c<T extends g> {
        void a(T t7);

        void b(T t7);

        void c(T t7);
    }

    /* loaded from: classes.dex */
    public interface d extends c<g> {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class e extends DataSetObserver {
        e() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            TabLayout.this.A();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            TabLayout.this.A();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class f extends LinearLayout {

        /* renamed from: e, reason: collision with root package name */
        ValueAnimator f9303e;

        /* renamed from: f, reason: collision with root package name */
        int f9304f;

        /* renamed from: g, reason: collision with root package name */
        float f9305g;

        /* renamed from: h, reason: collision with root package name */
        private int f9306h;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a implements ValueAnimator.AnimatorUpdateListener {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ View f9308a;

            /* renamed from: b, reason: collision with root package name */
            final /* synthetic */ View f9309b;

            a(View view, View view2) {
                this.f9308a = view;
                this.f9309b = view2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                f.this.h(this.f9308a, this.f9309b, valueAnimator.getAnimatedFraction());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class b extends AnimatorListenerAdapter {

            /* renamed from: a, reason: collision with root package name */
            final /* synthetic */ int f9311a;

            b(int i10) {
                this.f9311a = i10;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                f.this.f9304f = this.f9311a;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                f.this.f9304f = this.f9311a;
            }
        }

        f(Context context) {
            super(context);
            this.f9304f = -1;
            this.f9306h = -1;
            setWillNotDraw(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void e() {
            View childAt = getChildAt(this.f9304f);
            TabIndicatorInterpolator tabIndicatorInterpolator = TabLayout.this.J;
            TabLayout tabLayout = TabLayout.this;
            tabIndicatorInterpolator.c(tabLayout, childAt, tabLayout.f9275p);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void h(View view, View view2, float f10) {
            if (view != null && view.getWidth() > 0) {
                TabIndicatorInterpolator tabIndicatorInterpolator = TabLayout.this.J;
                TabLayout tabLayout = TabLayout.this;
                tabIndicatorInterpolator.d(tabLayout, view, view2, f10, tabLayout.f9275p);
            } else {
                Drawable drawable = TabLayout.this.f9275p;
                drawable.setBounds(-1, drawable.getBounds().top, -1, TabLayout.this.f9275p.getBounds().bottom);
            }
            ViewCompat.b0(this);
        }

        private void i(boolean z10, int i10, int i11) {
            View childAt = getChildAt(this.f9304f);
            View childAt2 = getChildAt(i10);
            if (childAt2 == null) {
                e();
                return;
            }
            a aVar = new a(childAt, childAt2);
            if (z10) {
                ValueAnimator valueAnimator = new ValueAnimator();
                this.f9303e = valueAnimator;
                valueAnimator.setInterpolator(AnimationUtils.f16556b);
                valueAnimator.setDuration(i11);
                valueAnimator.setFloatValues(0.0f, 1.0f);
                valueAnimator.addUpdateListener(aVar);
                valueAnimator.addListener(new b(i10));
                valueAnimator.start();
                return;
            }
            this.f9303e.removeAllUpdateListeners();
            this.f9303e.addUpdateListener(aVar);
        }

        void c(int i10, int i11) {
            ValueAnimator valueAnimator = this.f9303e;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.f9303e.cancel();
            }
            i(true, i10, i11);
        }

        boolean d() {
            int childCount = getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                if (getChildAt(i10).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            int height = TabLayout.this.f9275p.getBounds().height();
            if (height < 0) {
                height = TabLayout.this.f9275p.getIntrinsicHeight();
            }
            int i10 = TabLayout.this.C;
            int i11 = 0;
            if (i10 == 0) {
                i11 = getHeight() - height;
                height = getHeight();
            } else if (i10 == 1) {
                i11 = (getHeight() - height) / 2;
                height = (getHeight() + height) / 2;
            } else if (i10 != 2) {
                height = i10 != 3 ? 0 : getHeight();
            }
            if (TabLayout.this.f9275p.getBounds().width() > 0) {
                Rect bounds = TabLayout.this.f9275p.getBounds();
                TabLayout.this.f9275p.setBounds(bounds.left, i11, bounds.right, height);
                TabLayout tabLayout = TabLayout.this;
                Drawable drawable = tabLayout.f9275p;
                if (tabLayout.f9276q != 0) {
                    drawable = DrawableCompat.l(drawable);
                    DrawableCompat.h(drawable, TabLayout.this.f9276q);
                } else {
                    DrawableCompat.i(drawable, null);
                }
                drawable.draw(canvas);
            }
            super.draw(canvas);
        }

        void f(int i10, float f10) {
            ValueAnimator valueAnimator = this.f9303e;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.f9303e.cancel();
            }
            this.f9304f = i10;
            this.f9305g = f10;
            h(getChildAt(i10), getChildAt(this.f9304f + 1), this.f9305g);
        }

        void g(int i10) {
            Rect bounds = TabLayout.this.f9275p.getBounds();
            TabLayout.this.f9275p.setBounds(bounds.left, 0, bounds.right, i10);
            requestLayout();
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
            super.onLayout(z10, i10, i11, i12, i13);
            ValueAnimator valueAnimator = this.f9303e;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                i(false, this.f9304f, -1);
            } else {
                e();
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i10, int i11) {
            super.onMeasure(i10, i11);
            if (View.MeasureSpec.getMode(i10) != 1073741824) {
                return;
            }
            TabLayout tabLayout = TabLayout.this;
            boolean z10 = true;
            if (tabLayout.A == 1 || tabLayout.D == 2) {
                int childCount = getChildCount();
                int i12 = 0;
                for (int i13 = 0; i13 < childCount; i13++) {
                    View childAt = getChildAt(i13);
                    if (childAt.getVisibility() == 0) {
                        i12 = Math.max(i12, childAt.getMeasuredWidth());
                    }
                }
                if (i12 <= 0) {
                    return;
                }
                if (i12 * childCount <= getMeasuredWidth() - (((int) ViewUtils.dpToPx(getContext(), 16)) * 2)) {
                    boolean z11 = false;
                    for (int i14 = 0; i14 < childCount; i14++) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i14).getLayoutParams();
                        if (layoutParams.width != i12 || layoutParams.weight != 0.0f) {
                            layoutParams.width = i12;
                            layoutParams.weight = 0.0f;
                            z11 = true;
                        }
                    }
                    z10 = z11;
                } else {
                    TabLayout tabLayout2 = TabLayout.this;
                    tabLayout2.A = 0;
                    tabLayout2.O(false);
                }
                if (z10) {
                    super.onMeasure(i10, i11);
                }
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onRtlPropertiesChanged(int i10) {
            super.onRtlPropertiesChanged(i10);
        }
    }

    /* loaded from: classes.dex */
    public static class g {

        /* renamed from: a, reason: collision with root package name */
        private Object f9313a;

        /* renamed from: b, reason: collision with root package name */
        private Drawable f9314b;

        /* renamed from: c, reason: collision with root package name */
        private CharSequence f9315c;

        /* renamed from: d, reason: collision with root package name */
        private CharSequence f9316d;

        /* renamed from: f, reason: collision with root package name */
        private View f9318f;

        /* renamed from: h, reason: collision with root package name */
        public TabLayout f9320h;

        /* renamed from: i, reason: collision with root package name */
        public TabView f9321i;

        /* renamed from: e, reason: collision with root package name */
        private int f9317e = -1;

        /* renamed from: g, reason: collision with root package name */
        private int f9319g = 1;

        /* renamed from: j, reason: collision with root package name */
        private int f9322j = -1;

        public View e() {
            return this.f9318f;
        }

        public Drawable f() {
            return this.f9314b;
        }

        public int g() {
            return this.f9317e;
        }

        public int h() {
            return this.f9319g;
        }

        public CharSequence i() {
            return this.f9315c;
        }

        public boolean j() {
            TabLayout tabLayout = this.f9320h;
            if (tabLayout != null) {
                int selectedTabPosition = tabLayout.getSelectedTabPosition();
                return selectedTabPosition != -1 && selectedTabPosition == this.f9317e;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        void k() {
            this.f9320h = null;
            this.f9321i = null;
            this.f9313a = null;
            this.f9314b = null;
            this.f9322j = -1;
            this.f9315c = null;
            this.f9316d = null;
            this.f9317e = -1;
            this.f9318f = null;
        }

        public void l() {
            TabLayout tabLayout = this.f9320h;
            if (tabLayout != null) {
                tabLayout.F(this);
                return;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public g m(CharSequence charSequence) {
            this.f9316d = charSequence;
            s();
            return this;
        }

        public g n(int i10) {
            return o(LayoutInflater.from(this.f9321i.getContext()).inflate(i10, (ViewGroup) this.f9321i, false));
        }

        public g o(View view) {
            this.f9318f = view;
            s();
            return this;
        }

        public g p(Drawable drawable) {
            this.f9314b = drawable;
            TabLayout tabLayout = this.f9320h;
            if (tabLayout.A == 1 || tabLayout.D == 2) {
                tabLayout.O(true);
            }
            s();
            if (BadgeUtils.f8315a && this.f9321i.l() && this.f9321i.f9290i.isVisible()) {
                this.f9321i.invalidate();
            }
            return this;
        }

        void q(int i10) {
            this.f9317e = i10;
        }

        public g r(CharSequence charSequence) {
            if (TextUtils.isEmpty(this.f9316d) && !TextUtils.isEmpty(charSequence)) {
                this.f9321i.setContentDescription(charSequence);
            }
            this.f9315c = charSequence;
            s();
            return this;
        }

        void s() {
            TabView tabView = this.f9321i;
            if (tabView != null) {
                tabView.t();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class h implements ViewPager.i {

        /* renamed from: a, reason: collision with root package name */
        private final WeakReference<TabLayout> f9323a;

        /* renamed from: b, reason: collision with root package name */
        private int f9324b;

        /* renamed from: c, reason: collision with root package name */
        private int f9325c;

        public h(TabLayout tabLayout) {
            this.f9323a = new WeakReference<>(tabLayout);
        }

        void a() {
            this.f9325c = 0;
            this.f9324b = 0;
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageScrollStateChanged(int i10) {
            this.f9324b = this.f9325c;
            this.f9325c = i10;
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageScrolled(int i10, float f10, int i11) {
            TabLayout tabLayout = this.f9323a.get();
            if (tabLayout != null) {
                int i12 = this.f9325c;
                tabLayout.J(i10, f10, i12 != 2 || this.f9324b == 1, (i12 == 2 && this.f9324b == 0) ? false : true);
            }
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void onPageSelected(int i10) {
            TabLayout tabLayout = this.f9323a.get();
            if (tabLayout == null || tabLayout.getSelectedTabPosition() == i10 || i10 >= tabLayout.getTabCount()) {
                return;
            }
            int i11 = this.f9325c;
            tabLayout.G(tabLayout.w(i10), i11 == 0 || (i11 == 2 && this.f9324b == 0));
        }
    }

    /* loaded from: classes.dex */
    public static class i implements d {

        /* renamed from: a, reason: collision with root package name */
        private final ViewPager f9326a;

        public i(ViewPager viewPager) {
            this.f9326a = viewPager;
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void a(g gVar) {
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void b(g gVar) {
            this.f9326a.setCurrentItem(gVar.g());
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void c(g gVar) {
        }
    }

    public TabLayout(Context context) {
        this(context, null);
    }

    private void E(int i10) {
        TabView tabView = (TabView) this.f9266g.getChildAt(i10);
        this.f9266g.removeViewAt(i10);
        if (tabView != null) {
            tabView.o();
            this.U.a(tabView);
        }
        requestLayout();
    }

    private void L(ViewPager viewPager, boolean z10, boolean z11) {
        ViewPager viewPager2 = this.O;
        if (viewPager2 != null) {
            h hVar = this.R;
            if (hVar != null) {
                viewPager2.removeOnPageChangeListener(hVar);
            }
            b bVar = this.S;
            if (bVar != null) {
                this.O.removeOnAdapterChangeListener(bVar);
            }
        }
        c cVar = this.M;
        if (cVar != null) {
            D(cVar);
            this.M = null;
        }
        if (viewPager != null) {
            this.O = viewPager;
            if (this.R == null) {
                this.R = new h(this);
            }
            this.R.a();
            viewPager.addOnPageChangeListener(this.R);
            i iVar = new i(viewPager);
            this.M = iVar;
            c(iVar);
            PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                H(adapter, z10);
            }
            if (this.S == null) {
                this.S = new b();
            }
            this.S.b(z10);
            viewPager.addOnAdapterChangeListener(this.S);
            I(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.O = null;
            H(null, false);
        }
        this.T = z11;
    }

    private void M() {
        int size = this.f9264e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f9264e.get(i10).s();
        }
    }

    private void N(LinearLayout.LayoutParams layoutParams) {
        if (this.D == 1 && this.A == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
        } else {
            layoutParams.width = -2;
            layoutParams.weight = 0.0f;
        }
    }

    private void g(TabItem tabItem) {
        g z10 = z();
        CharSequence charSequence = tabItem.f9261e;
        if (charSequence != null) {
            z10.r(charSequence);
        }
        Drawable drawable = tabItem.f9262f;
        if (drawable != null) {
            z10.p(drawable);
        }
        int i10 = tabItem.f9263g;
        if (i10 != 0) {
            z10.n(i10);
        }
        if (!TextUtils.isEmpty(tabItem.getContentDescription())) {
            z10.m(tabItem.getContentDescription());
        }
        d(z10);
    }

    private int getDefaultHeight() {
        int size = this.f9264e.size();
        boolean z10 = false;
        int i10 = 0;
        while (true) {
            if (i10 < size) {
                g gVar = this.f9264e.get(i10);
                if (gVar != null && gVar.f() != null && !TextUtils.isEmpty(gVar.i())) {
                    z10 = true;
                    break;
                }
                i10++;
            } else {
                break;
            }
        }
        return (!z10 || this.E) ? 48 : 72;
    }

    private int getTabMinWidth() {
        int i10 = this.f9282w;
        if (i10 != -1) {
            return i10;
        }
        int i11 = this.D;
        if (i11 == 0 || i11 == 2) {
            return this.f9284y;
        }
        return 0;
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.f9266g.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    private void h(g gVar) {
        TabView tabView = gVar.f9321i;
        tabView.setSelected(false);
        tabView.setActivated(false);
        this.f9266g.addView(tabView, gVar.g(), p());
    }

    private void i(View view) {
        if (view instanceof TabItem) {
            g((TabItem) view);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }

    private void j(int i10) {
        if (i10 == -1) {
            return;
        }
        if (getWindowToken() != null && ViewCompat.Q(this) && !this.f9266g.d()) {
            int scrollX = getScrollX();
            int m10 = m(i10, 0.0f);
            if (scrollX != m10) {
                v();
                this.N.setIntValues(scrollX, m10);
                this.N.start();
            }
            this.f9266g.c(i10, this.B);
            return;
        }
        I(i10, 0.0f, true);
    }

    private void k(int i10) {
        if (i10 == 0) {
            Log.w("TabLayout", "MODE_SCROLLABLE + GRAVITY_FILL is not supported, GRAVITY_START will be used instead");
        } else if (i10 == 1) {
            this.f9266g.setGravity(1);
            return;
        } else if (i10 != 2) {
            return;
        }
        this.f9266g.setGravity(8388611);
    }

    private void l() {
        int i10 = this.D;
        ViewCompat.A0(this.f9266g, (i10 == 0 || i10 == 2) ? Math.max(0, this.f9285z - this.f9267h) : 0, 0, 0, 0);
        int i11 = this.D;
        if (i11 == 0) {
            k(this.A);
        } else if (i11 == 1 || i11 == 2) {
            if (this.A == 2) {
                Log.w("TabLayout", "GRAVITY_START is not supported with the current tab mode, GRAVITY_CENTER will be used instead");
            }
            this.f9266g.setGravity(1);
        }
        O(true);
    }

    private int m(int i10, float f10) {
        View childAt;
        int i11 = this.D;
        if ((i11 != 0 && i11 != 2) || (childAt = this.f9266g.getChildAt(i10)) == null) {
            return 0;
        }
        int i12 = i10 + 1;
        View childAt2 = i12 < this.f9266g.getChildCount() ? this.f9266g.getChildAt(i12) : null;
        int width = childAt.getWidth();
        int width2 = childAt2 != null ? childAt2.getWidth() : 0;
        int left = (childAt.getLeft() + (width / 2)) - (getWidth() / 2);
        int i13 = (int) ((width + width2) * 0.5f * f10);
        return ViewCompat.x(this) == 0 ? left + i13 : left - i13;
    }

    private void n(g gVar, int i10) {
        gVar.q(i10);
        this.f9264e.add(i10, gVar);
        int size = this.f9264e.size();
        while (true) {
            i10++;
            if (i10 >= size) {
                return;
            } else {
                this.f9264e.get(i10).q(i10);
            }
        }
    }

    private static ColorStateList o(int i10, int i11) {
        return new ColorStateList(new int[][]{HorizontalScrollView.SELECTED_STATE_SET, HorizontalScrollView.EMPTY_STATE_SET}, new int[]{i11, i10});
    }

    private LinearLayout.LayoutParams p() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        N(layoutParams);
        return layoutParams;
    }

    private TabView r(g gVar) {
        androidx.core.util.e<TabView> eVar = this.U;
        TabView b10 = eVar != null ? eVar.b() : null;
        if (b10 == null) {
            b10 = new TabView(getContext());
        }
        b10.setTab(gVar);
        b10.setFocusable(true);
        b10.setMinimumWidth(getTabMinWidth());
        if (TextUtils.isEmpty(gVar.f9316d)) {
            b10.setContentDescription(gVar.f9315c);
        } else {
            b10.setContentDescription(gVar.f9316d);
        }
        return b10;
    }

    private void s(g gVar) {
        for (int size = this.L.size() - 1; size >= 0; size--) {
            this.L.get(size).a(gVar);
        }
    }

    private void setSelectedTabView(int i10) {
        int childCount = this.f9266g.getChildCount();
        if (i10 < childCount) {
            int i11 = 0;
            while (i11 < childCount) {
                View childAt = this.f9266g.getChildAt(i11);
                boolean z10 = true;
                childAt.setSelected(i11 == i10);
                if (i11 != i10) {
                    z10 = false;
                }
                childAt.setActivated(z10);
                i11++;
            }
        }
    }

    private void t(g gVar) {
        for (int size = this.L.size() - 1; size >= 0; size--) {
            this.L.get(size).b(gVar);
        }
    }

    private void u(g gVar) {
        for (int size = this.L.size() - 1; size >= 0; size--) {
            this.L.get(size).c(gVar);
        }
    }

    private void v() {
        if (this.N == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.N = valueAnimator;
            valueAnimator.setInterpolator(AnimationUtils.f16556b);
            this.N.setDuration(this.B);
            this.N.addUpdateListener(new a());
        }
    }

    private boolean x() {
        return getTabMode() == 0 || getTabMode() == 2;
    }

    void A() {
        int currentItem;
        C();
        PagerAdapter pagerAdapter = this.P;
        if (pagerAdapter != null) {
            int count = pagerAdapter.getCount();
            for (int i10 = 0; i10 < count; i10++) {
                f(z().r(this.P.getPageTitle(i10)), false);
            }
            ViewPager viewPager = this.O;
            if (viewPager == null || count <= 0 || (currentItem = viewPager.getCurrentItem()) == getSelectedTabPosition() || currentItem >= getTabCount()) {
                return;
            }
            F(w(currentItem));
        }
    }

    protected boolean B(g gVar) {
        return W.a(gVar);
    }

    public void C() {
        for (int childCount = this.f9266g.getChildCount() - 1; childCount >= 0; childCount--) {
            E(childCount);
        }
        Iterator<g> it = this.f9264e.iterator();
        while (it.hasNext()) {
            g next = it.next();
            it.remove();
            next.k();
            B(next);
        }
        this.f9265f = null;
    }

    @Deprecated
    public void D(c cVar) {
        this.L.remove(cVar);
    }

    public void F(g gVar) {
        G(gVar, true);
    }

    public void G(g gVar, boolean z10) {
        g gVar2 = this.f9265f;
        if (gVar2 == gVar) {
            if (gVar2 != null) {
                s(gVar);
                j(gVar.g());
                return;
            }
            return;
        }
        int g6 = gVar != null ? gVar.g() : -1;
        if (z10) {
            if ((gVar2 == null || gVar2.g() == -1) && g6 != -1) {
                I(g6, 0.0f, true);
            } else {
                j(g6);
            }
            if (g6 != -1) {
                setSelectedTabView(g6);
            }
        }
        this.f9265f = gVar;
        if (gVar2 != null) {
            u(gVar2);
        }
        if (gVar != null) {
            t(gVar);
        }
    }

    void H(PagerAdapter pagerAdapter, boolean z10) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.P;
        if (pagerAdapter2 != null && (dataSetObserver = this.Q) != null) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.P = pagerAdapter;
        if (z10 && pagerAdapter != null) {
            if (this.Q == null) {
                this.Q = new e();
            }
            pagerAdapter.registerDataSetObserver(this.Q);
        }
        A();
    }

    public void I(int i10, float f10, boolean z10) {
        J(i10, f10, z10, true);
    }

    public void J(int i10, float f10, boolean z10, boolean z11) {
        int round = Math.round(i10 + f10);
        if (round < 0 || round >= this.f9266g.getChildCount()) {
            return;
        }
        if (z11) {
            this.f9266g.f(i10, f10);
        }
        ValueAnimator valueAnimator = this.N;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.N.cancel();
        }
        scrollTo(i10 < 0 ? 0 : m(i10, f10), 0);
        if (z10) {
            setSelectedTabView(round);
        }
    }

    public void K(ViewPager viewPager, boolean z10) {
        L(viewPager, z10, false);
    }

    void O(boolean z10) {
        for (int i10 = 0; i10 < this.f9266g.getChildCount(); i10++) {
            View childAt = this.f9266g.getChildAt(i10);
            childAt.setMinimumWidth(getTabMinWidth());
            N((LinearLayout.LayoutParams) childAt.getLayoutParams());
            if (z10) {
                childAt.requestLayout();
            }
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view) {
        i(view);
    }

    @Deprecated
    public void c(c cVar) {
        if (this.L.contains(cVar)) {
            return;
        }
        this.L.add(cVar);
    }

    public void d(g gVar) {
        f(gVar, this.f9264e.isEmpty());
    }

    public void e(g gVar, int i10, boolean z10) {
        if (gVar.f9320h == this) {
            n(gVar, i10);
            h(gVar);
            if (z10) {
                gVar.l();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    public void f(g gVar, boolean z10) {
        e(gVar, this.f9264e.size(), z10);
    }

    public int getSelectedTabPosition() {
        g gVar = this.f9265f;
        if (gVar != null) {
            return gVar.g();
        }
        return -1;
    }

    public int getTabCount() {
        return this.f9264e.size();
    }

    public int getTabGravity() {
        return this.A;
    }

    public ColorStateList getTabIconTint() {
        return this.f9273n;
    }

    public int getTabIndicatorAnimationMode() {
        return this.H;
    }

    public int getTabIndicatorGravity() {
        return this.C;
    }

    int getTabMaxWidth() {
        return this.f9281v;
    }

    public int getTabMode() {
        return this.D;
    }

    public ColorStateList getTabRippleColor() {
        return this.f9274o;
    }

    public Drawable getTabSelectedIndicator() {
        return this.f9275p;
    }

    public ColorStateList getTabTextColors() {
        return this.f9272m;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MaterialShapeUtils.e(this);
        if (this.O == null) {
            ViewParent parent = getParent();
            if (parent instanceof ViewPager) {
                L((ViewPager) parent, true, true);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.T) {
            setupWithViewPager(null);
            this.T = false;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        for (int i10 = 0; i10 < this.f9266g.getChildCount(); i10++) {
            View childAt = this.f9266g.getChildAt(i10);
            if (childAt instanceof TabView) {
                ((TabView) childAt).j(canvas);
            }
        }
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo).X(AccessibilityNodeInfoCompat.b.b(1, getTabCount(), false, 1));
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return x() && super.onInterceptTouchEvent(motionEvent);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0073, code lost:
    
        if (r0 != 2) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x007e, code lost:
    
        if (r7.getMeasuredWidth() != getMeasuredWidth()) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0080, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008a, code lost:
    
        if (r7.getMeasuredWidth() < getMeasuredWidth()) goto L29;
     */
    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i10, int i11) {
        int round = Math.round(ViewUtils.dpToPx(getContext(), getDefaultHeight()));
        int mode = View.MeasureSpec.getMode(i11);
        boolean z10 = false;
        if (mode != Integer.MIN_VALUE) {
            if (mode == 0) {
                i11 = View.MeasureSpec.makeMeasureSpec(round + getPaddingTop() + getPaddingBottom(), 1073741824);
            }
        } else if (getChildCount() == 1 && View.MeasureSpec.getSize(i11) >= round) {
            getChildAt(0).setMinimumHeight(round);
        }
        int size = View.MeasureSpec.getSize(i10);
        if (View.MeasureSpec.getMode(i10) != 0) {
            int i12 = this.f9283x;
            if (i12 <= 0) {
                i12 = (int) (size - ViewUtils.dpToPx(getContext(), 56));
            }
            this.f9281v = i12;
        }
        super.onMeasure(i10, i11);
        if (getChildCount() != 1) {
            return;
        }
        View childAt = getChildAt(0);
        int i13 = this.D;
        if (i13 != 0) {
            if (i13 != 1) {
            }
            if (z10) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), HorizontalScrollView.getChildMeasureSpec(i11, getPaddingTop() + getPaddingBottom(), childAt.getLayoutParams().height));
            }
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != 8 || x()) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    protected g q() {
        g b10 = W.b();
        return b10 == null ? new g() : b10;
    }

    @Override // android.view.View
    public void setElevation(float f10) {
        super.setElevation(f10);
        MaterialShapeUtils.d(this, f10);
    }

    public void setInlineLabel(boolean z10) {
        if (this.E != z10) {
            this.E = z10;
            for (int i10 = 0; i10 < this.f9266g.getChildCount(); i10++) {
                View childAt = this.f9266g.getChildAt(i10);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).v();
                }
            }
            l();
        }
    }

    public void setInlineLabelResource(int i10) {
        setInlineLabel(getResources().getBoolean(i10));
    }

    @Deprecated
    public void setOnTabSelectedListener(d dVar) {
        setOnTabSelectedListener((c) dVar);
    }

    void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        v();
        this.N.addListener(animatorListener);
    }

    public void setSelectedTabIndicator(Drawable drawable) {
        if (this.f9275p != drawable) {
            if (drawable == null) {
                drawable = new GradientDrawable();
            }
            this.f9275p = drawable;
            int i10 = this.G;
            if (i10 == -1) {
                i10 = drawable.getIntrinsicHeight();
            }
            this.f9266g.g(i10);
        }
    }

    public void setSelectedTabIndicatorColor(int i10) {
        this.f9276q = i10;
        O(false);
    }

    public void setSelectedTabIndicatorGravity(int i10) {
        if (this.C != i10) {
            this.C = i10;
            ViewCompat.b0(this.f9266g);
        }
    }

    @Deprecated
    public void setSelectedTabIndicatorHeight(int i10) {
        this.G = i10;
        this.f9266g.g(i10);
    }

    public void setTabGravity(int i10) {
        if (this.A != i10) {
            this.A = i10;
            l();
        }
    }

    public void setTabIconTint(ColorStateList colorStateList) {
        if (this.f9273n != colorStateList) {
            this.f9273n = colorStateList;
            M();
        }
    }

    public void setTabIconTintResource(int i10) {
        setTabIconTint(AppCompatResources.a(getContext(), i10));
    }

    public void setTabIndicatorAnimationMode(int i10) {
        this.H = i10;
        if (i10 == 0) {
            this.J = new TabIndicatorInterpolator();
            return;
        }
        if (i10 == 1) {
            this.J = new ElasticTabIndicatorInterpolator();
        } else {
            if (i10 == 2) {
                this.J = new FadeTabIndicatorInterpolator();
                return;
            }
            throw new IllegalArgumentException(i10 + " is not a valid TabIndicatorAnimationMode");
        }
    }

    public void setTabIndicatorFullWidth(boolean z10) {
        this.F = z10;
        this.f9266g.e();
        ViewCompat.b0(this.f9266g);
    }

    public void setTabMode(int i10) {
        if (i10 != this.D) {
            this.D = i10;
            l();
        }
    }

    public void setTabRippleColor(ColorStateList colorStateList) {
        if (this.f9274o != colorStateList) {
            this.f9274o = colorStateList;
            for (int i10 = 0; i10 < this.f9266g.getChildCount(); i10++) {
                View childAt = this.f9266g.getChildAt(i10);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).u(getContext());
                }
            }
        }
    }

    public void setTabRippleColorResource(int i10) {
        setTabRippleColor(AppCompatResources.a(getContext(), i10));
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.f9272m != colorStateList) {
            this.f9272m = colorStateList;
            M();
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter) {
        H(pagerAdapter, false);
    }

    public void setUnboundedRipple(boolean z10) {
        if (this.I != z10) {
            this.I = z10;
            for (int i10 = 0; i10 < this.f9266g.getChildCount(); i10++) {
                View childAt = this.f9266g.getChildAt(i10);
                if (childAt instanceof TabView) {
                    ((TabView) childAt).u(getContext());
                }
            }
        }
    }

    public void setUnboundedRippleResource(int i10) {
        setUnboundedRipple(getResources().getBoolean(i10));
    }

    public void setupWithViewPager(ViewPager viewPager) {
        K(viewPager, true);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    public g w(int i10) {
        if (i10 < 0 || i10 >= getTabCount()) {
            return null;
        }
        return this.f9264e.get(i10);
    }

    public boolean y() {
        return this.F;
    }

    public g z() {
        g q10 = q();
        q10.f9320h = this;
        q10.f9321i = r(q10);
        if (q10.f9322j != -1) {
            q10.f9321i.setId(q10.f9322j);
        }
        return q10;
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.tabStyle);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i10) {
        i(view);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateDefaultLayoutParams();
    }

    @Deprecated
    public void setOnTabSelectedListener(c cVar) {
        c cVar2 = this.K;
        if (cVar2 != null) {
            D(cVar2);
        }
        this.K = cVar;
        if (cVar != null) {
            c(cVar);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TabLayout(Context context, AttributeSet attributeSet, int i10) {
        super(MaterialThemeOverlay.c(context, attributeSet, i10, r4), attributeSet, i10);
        int i11 = V;
        this.f9264e = new ArrayList<>();
        this.f9275p = new GradientDrawable();
        this.f9276q = 0;
        this.f9281v = Integer.MAX_VALUE;
        this.G = -1;
        this.L = new ArrayList<>();
        this.U = new androidx.core.util.f(12);
        Context context2 = getContext();
        setHorizontalScrollBarEnabled(false);
        f fVar = new f(context2);
        this.f9266g = fVar;
        super.addView(fVar, 0, new FrameLayout.LayoutParams(-2, -1));
        int[] iArr = R$styleable.TabLayout;
        int i12 = R$styleable.TabLayout_tabTextAppearance;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context2, attributeSet, iArr, i10, i11, i12);
        if (getBackground() instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) getBackground();
            MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
            materialShapeDrawable.a0(ColorStateList.valueOf(colorDrawable.getColor()));
            materialShapeDrawable.P(context2);
            materialShapeDrawable.Z(ViewCompat.t(this));
            ViewCompat.p0(this, materialShapeDrawable);
        }
        setSelectedTabIndicator(MaterialResources.e(context2, obtainStyledAttributes, R$styleable.TabLayout_tabIndicator));
        setSelectedTabIndicatorColor(obtainStyledAttributes.getColor(R$styleable.TabLayout_tabIndicatorColor, 0));
        fVar.g(obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabIndicatorHeight, -1));
        setSelectedTabIndicatorGravity(obtainStyledAttributes.getInt(R$styleable.TabLayout_tabIndicatorGravity, 0));
        setTabIndicatorAnimationMode(obtainStyledAttributes.getInt(R$styleable.TabLayout_tabIndicatorAnimationMode, 0));
        setTabIndicatorFullWidth(obtainStyledAttributes.getBoolean(R$styleable.TabLayout_tabIndicatorFullWidth, true));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabPadding, 0);
        this.f9270k = dimensionPixelSize;
        this.f9269j = dimensionPixelSize;
        this.f9268i = dimensionPixelSize;
        this.f9267h = dimensionPixelSize;
        this.f9267h = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingStart, dimensionPixelSize);
        this.f9268i = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingTop, this.f9268i);
        this.f9269j = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingEnd, this.f9269j);
        this.f9270k = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabPaddingBottom, this.f9270k);
        int resourceId = obtainStyledAttributes.getResourceId(i12, R$style.TextAppearance_Design_Tab);
        this.f9271l = resourceId;
        TypedArray obtainStyledAttributes2 = context2.obtainStyledAttributes(resourceId, androidx.appcompat.R$styleable.TextAppearance);
        try {
            this.f9278s = obtainStyledAttributes2.getDimensionPixelSize(androidx.appcompat.R$styleable.TextAppearance_android_textSize, 0);
            this.f9272m = MaterialResources.a(context2, obtainStyledAttributes2, androidx.appcompat.R$styleable.TextAppearance_android_textColor);
            obtainStyledAttributes2.recycle();
            int i13 = R$styleable.TabLayout_tabTextColor;
            if (obtainStyledAttributes.hasValue(i13)) {
                this.f9272m = MaterialResources.a(context2, obtainStyledAttributes, i13);
            }
            int i14 = R$styleable.TabLayout_tabSelectedTextColor;
            if (obtainStyledAttributes.hasValue(i14)) {
                this.f9272m = o(this.f9272m.getDefaultColor(), obtainStyledAttributes.getColor(i14, 0));
            }
            this.f9273n = MaterialResources.a(context2, obtainStyledAttributes, R$styleable.TabLayout_tabIconTint);
            this.f9277r = ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R$styleable.TabLayout_tabIconTintMode, -1), null);
            this.f9274o = MaterialResources.a(context2, obtainStyledAttributes, R$styleable.TabLayout_tabRippleColor);
            this.B = obtainStyledAttributes.getInt(R$styleable.TabLayout_tabIndicatorAnimationDuration, 300);
            this.f9282w = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabMinWidth, -1);
            this.f9283x = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabMaxWidth, -1);
            this.f9280u = obtainStyledAttributes.getResourceId(R$styleable.TabLayout_tabBackground, 0);
            this.f9285z = obtainStyledAttributes.getDimensionPixelSize(R$styleable.TabLayout_tabContentStart, 0);
            this.D = obtainStyledAttributes.getInt(R$styleable.TabLayout_tabMode, 1);
            this.A = obtainStyledAttributes.getInt(R$styleable.TabLayout_tabGravity, 0);
            this.E = obtainStyledAttributes.getBoolean(R$styleable.TabLayout_tabInlineLabel, false);
            this.I = obtainStyledAttributes.getBoolean(R$styleable.TabLayout_tabUnboundedRipple, false);
            obtainStyledAttributes.recycle();
            Resources resources = getResources();
            this.f9279t = resources.getDimensionPixelSize(R$dimen.design_tab_text_size_2line);
            this.f9284y = resources.getDimensionPixelSize(R$dimen.design_tab_scrollable_min_width);
            l();
        } catch (Throwable th) {
            obtainStyledAttributes2.recycle();
            throw th;
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        i(view);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i10, ViewGroup.LayoutParams layoutParams) {
        i(view);
    }

    public void setSelectedTabIndicator(int i10) {
        if (i10 != 0) {
            setSelectedTabIndicator(AppCompatResources.b(getContext(), i10));
        } else {
            setSelectedTabIndicator((Drawable) null);
        }
    }
}
