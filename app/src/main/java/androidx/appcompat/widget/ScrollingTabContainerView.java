package androidx.appcompat.widget;

import android.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.widget.LinearLayoutCompat;

/* loaded from: classes.dex */
public class ScrollingTabContainerView extends HorizontalScrollView implements AdapterView.OnItemSelectedListener {

    /* renamed from: p, reason: collision with root package name */
    private static final Interpolator f1057p = new DecelerateInterpolator();

    /* renamed from: e, reason: collision with root package name */
    Runnable f1058e;

    /* renamed from: f, reason: collision with root package name */
    private c f1059f;

    /* renamed from: g, reason: collision with root package name */
    LinearLayoutCompat f1060g;

    /* renamed from: h, reason: collision with root package name */
    private Spinner f1061h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f1062i;

    /* renamed from: j, reason: collision with root package name */
    int f1063j;

    /* renamed from: k, reason: collision with root package name */
    int f1064k;

    /* renamed from: l, reason: collision with root package name */
    private int f1065l;

    /* renamed from: m, reason: collision with root package name */
    private int f1066m;

    /* renamed from: n, reason: collision with root package name */
    protected ViewPropertyAnimator f1067n;

    /* renamed from: o, reason: collision with root package name */
    protected final e f1068o;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f1069e;

        a(View view) {
            this.f1069e = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            ScrollingTabContainerView.this.smoothScrollTo(this.f1069e.getLeft() - ((ScrollingTabContainerView.this.getWidth() - this.f1069e.getWidth()) / 2), 0);
            ScrollingTabContainerView.this.f1058e = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b extends BaseAdapter {
        b() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return ScrollingTabContainerView.this.f1060g.getChildCount();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i10) {
            return ((d) ScrollingTabContainerView.this.f1060g.getChildAt(i10)).b();
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            if (view == null) {
                return ScrollingTabContainerView.this.d((ActionBar.b) getItem(i10), true);
            }
            ((d) view).a((ActionBar.b) getItem(i10));
            return view;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ((d) view).b().e();
            int childCount = ScrollingTabContainerView.this.f1060g.getChildCount();
            for (int i10 = 0; i10 < childCount; i10++) {
                View childAt = ScrollingTabContainerView.this.f1060g.getChildAt(i10);
                childAt.setSelected(childAt == view);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class d extends LinearLayout {

        /* renamed from: e, reason: collision with root package name */
        private final int[] f1073e;

        /* renamed from: f, reason: collision with root package name */
        private ActionBar.b f1074f;

        /* renamed from: g, reason: collision with root package name */
        private TextView f1075g;

        /* renamed from: h, reason: collision with root package name */
        private ImageView f1076h;

        /* renamed from: i, reason: collision with root package name */
        private View f1077i;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public d(Context context, ActionBar.b bVar, boolean z10) {
            super(context, null, r5);
            int i10 = R$attr.actionBarTabStyle;
            int[] iArr = {R.attr.background};
            this.f1073e = iArr;
            this.f1074f = bVar;
            TintTypedArray w10 = TintTypedArray.w(context, null, iArr, i10, 0);
            if (w10.s(0)) {
                setBackgroundDrawable(w10.g(0));
            }
            w10.x();
            if (z10) {
                setGravity(8388627);
            }
            c();
        }

        public void a(ActionBar.b bVar) {
            this.f1074f = bVar;
            c();
        }

        public ActionBar.b b() {
            return this.f1074f;
        }

        public void c() {
            ActionBar.b bVar = this.f1074f;
            View b10 = bVar.b();
            if (b10 != null) {
                ViewParent parent = b10.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(b10);
                    }
                    addView(b10);
                }
                this.f1077i = b10;
                TextView textView = this.f1075g;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.f1076h;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.f1076h.setImageDrawable(null);
                    return;
                }
                return;
            }
            View view = this.f1077i;
            if (view != null) {
                removeView(view);
                this.f1077i = null;
            }
            Drawable c10 = bVar.c();
            CharSequence d10 = bVar.d();
            if (c10 != null) {
                if (this.f1076h == null) {
                    AppCompatImageView appCompatImageView = new AppCompatImageView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams.gravity = 16;
                    appCompatImageView.setLayoutParams(layoutParams);
                    addView(appCompatImageView, 0);
                    this.f1076h = appCompatImageView;
                }
                this.f1076h.setImageDrawable(c10);
                this.f1076h.setVisibility(0);
            } else {
                ImageView imageView2 = this.f1076h;
                if (imageView2 != null) {
                    imageView2.setVisibility(8);
                    this.f1076h.setImageDrawable(null);
                }
            }
            boolean z10 = !TextUtils.isEmpty(d10);
            if (z10) {
                if (this.f1075g == null) {
                    AppCompatTextView appCompatTextView = new AppCompatTextView(getContext(), null, R$attr.actionBarTabTextStyle);
                    appCompatTextView.setEllipsize(TextUtils.TruncateAt.END);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
                    layoutParams2.gravity = 16;
                    appCompatTextView.setLayoutParams(layoutParams2);
                    addView(appCompatTextView);
                    this.f1075g = appCompatTextView;
                }
                this.f1075g.setText(d10);
                this.f1075g.setVisibility(0);
            } else {
                TextView textView2 = this.f1075g;
                if (textView2 != null) {
                    textView2.setVisibility(8);
                    this.f1075g.setText((CharSequence) null);
                }
            }
            ImageView imageView3 = this.f1076h;
            if (imageView3 != null) {
                imageView3.setContentDescription(bVar.a());
            }
            TooltipCompat.a(this, z10 ? null : bVar.a());
        }

        @Override // android.view.View
        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName("androidx.appcompat.app.ActionBar$Tab");
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName("androidx.appcompat.app.ActionBar$Tab");
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onMeasure(int i10, int i11) {
            super.onMeasure(i10, i11);
            if (ScrollingTabContainerView.this.f1063j > 0) {
                int measuredWidth = getMeasuredWidth();
                int i12 = ScrollingTabContainerView.this.f1063j;
                if (measuredWidth > i12) {
                    super.onMeasure(View.MeasureSpec.makeMeasureSpec(i12, 1073741824), i11);
                }
            }
        }

        @Override // android.view.View
        public void setSelected(boolean z10) {
            boolean z11 = isSelected() != z10;
            super.setSelected(z10);
            if (z11 && z10) {
                sendAccessibilityEvent(4);
            }
        }
    }

    /* loaded from: classes.dex */
    protected class e extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        private boolean f1079a = false;

        /* renamed from: b, reason: collision with root package name */
        private int f1080b;

        protected e() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f1079a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.f1079a) {
                return;
            }
            ScrollingTabContainerView scrollingTabContainerView = ScrollingTabContainerView.this;
            scrollingTabContainerView.f1067n = null;
            scrollingTabContainerView.setVisibility(this.f1080b);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ScrollingTabContainerView.this.setVisibility(0);
            this.f1079a = false;
        }
    }

    public ScrollingTabContainerView(Context context) {
        super(context);
        this.f1068o = new e();
        setHorizontalScrollBarEnabled(false);
        ActionBarPolicy b10 = ActionBarPolicy.b(context);
        setContentHeight(b10.f());
        this.f1064k = b10.e();
        LinearLayoutCompat c10 = c();
        this.f1060g = c10;
        addView(c10, new ViewGroup.LayoutParams(-2, -1));
    }

    private Spinner b() {
        AppCompatSpinner appCompatSpinner = new AppCompatSpinner(getContext(), null, R$attr.actionDropDownStyle);
        appCompatSpinner.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
        appCompatSpinner.setOnItemSelectedListener(this);
        return appCompatSpinner;
    }

    private LinearLayoutCompat c() {
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(getContext(), null, R$attr.actionBarTabBarStyle);
        linearLayoutCompat.setMeasureWithLargestChildEnabled(true);
        linearLayoutCompat.setGravity(17);
        linearLayoutCompat.setLayoutParams(new LinearLayoutCompat.LayoutParams(-2, -1));
        return linearLayoutCompat;
    }

    private boolean e() {
        Spinner spinner = this.f1061h;
        return spinner != null && spinner.getParent() == this;
    }

    private void f() {
        if (e()) {
            return;
        }
        if (this.f1061h == null) {
            this.f1061h = b();
        }
        removeView(this.f1060g);
        addView(this.f1061h, new ViewGroup.LayoutParams(-2, -1));
        if (this.f1061h.getAdapter() == null) {
            this.f1061h.setAdapter((SpinnerAdapter) new b());
        }
        Runnable runnable = this.f1058e;
        if (runnable != null) {
            removeCallbacks(runnable);
            this.f1058e = null;
        }
        this.f1061h.setSelection(this.f1066m);
    }

    private boolean g() {
        if (!e()) {
            return false;
        }
        removeView(this.f1061h);
        addView(this.f1060g, new ViewGroup.LayoutParams(-2, -1));
        setTabSelected(this.f1061h.getSelectedItemPosition());
        return false;
    }

    public void a(int i10) {
        View childAt = this.f1060g.getChildAt(i10);
        Runnable runnable = this.f1058e;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        a aVar = new a(childAt);
        this.f1058e = aVar;
        post(aVar);
    }

    d d(ActionBar.b bVar, boolean z10) {
        d dVar = new d(getContext(), bVar, z10);
        if (z10) {
            dVar.setBackgroundDrawable(null);
            dVar.setLayoutParams(new AbsListView.LayoutParams(-1, this.f1065l));
        } else {
            dVar.setFocusable(true);
            if (this.f1059f == null) {
                this.f1059f = new c();
            }
            dVar.setOnClickListener(this.f1059f);
        }
        return dVar;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.f1058e;
        if (runnable != null) {
            post(runnable);
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ActionBarPolicy b10 = ActionBarPolicy.b(getContext());
        setContentHeight(b10.f());
        this.f1064k = b10.e();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.f1058e;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onItemSelected(AdapterView<?> adapterView, View view, int i10, long j10) {
        ((d) view).b().e();
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    public void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        boolean z10 = mode == 1073741824;
        setFillViewport(z10);
        int childCount = this.f1060g.getChildCount();
        if (childCount > 1 && (mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            if (childCount > 2) {
                this.f1063j = (int) (View.MeasureSpec.getSize(i10) * 0.4f);
            } else {
                this.f1063j = View.MeasureSpec.getSize(i10) / 2;
            }
            this.f1063j = Math.min(this.f1063j, this.f1064k);
        } else {
            this.f1063j = -1;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.f1065l, 1073741824);
        if (!z10 && this.f1062i) {
            this.f1060g.measure(0, makeMeasureSpec);
            if (this.f1060g.getMeasuredWidth() > View.MeasureSpec.getSize(i10)) {
                f();
            } else {
                g();
            }
        } else {
            g();
        }
        int measuredWidth = getMeasuredWidth();
        super.onMeasure(i10, makeMeasureSpec);
        int measuredWidth2 = getMeasuredWidth();
        if (!z10 || measuredWidth == measuredWidth2) {
            return;
        }
        setTabSelected(this.f1066m);
    }

    @Override // android.widget.AdapterView.OnItemSelectedListener
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void setAllowCollapse(boolean z10) {
        this.f1062i = z10;
    }

    public void setContentHeight(int i10) {
        this.f1065l = i10;
        requestLayout();
    }

    public void setTabSelected(int i10) {
        this.f1066m = i10;
        int childCount = this.f1060g.getChildCount();
        int i11 = 0;
        while (i11 < childCount) {
            View childAt = this.f1060g.getChildAt(i11);
            boolean z10 = i11 == i10;
            childAt.setSelected(z10);
            if (z10) {
                a(i10);
            }
            i11++;
        }
        Spinner spinner = this.f1061h;
        if (spinner == null || i10 < 0) {
            return;
        }
        spinner.setSelection(i10);
    }
}
