package f2;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import com.coui.appcompat.panel.COUIPanelContentLayout;
import com.coui.appcompat.panel.IgnoreWindowInsetsFrameLayout;
import com.support.panel.R$id;
import h3.UIUtil;
import m1.COUIInEaseInterpolator;
import m1.COUIOutEaseInterpolator;

/* compiled from: COUIPanelAdjustResizeHelperAfterR.java */
/* renamed from: f2.d, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPanelAdjustResizeHelperAfterR extends COUIAbsPanelAdjustResizeHelper {

    /* renamed from: d, reason: collision with root package name */
    private static final Interpolator f11308d = new COUIInEaseInterpolator();

    /* renamed from: e, reason: collision with root package name */
    private static final Interpolator f11309e = new COUIOutEaseInterpolator();

    /* renamed from: f, reason: collision with root package name */
    private static final Interpolator f11310f = new COUIInEaseInterpolator();

    /* renamed from: g, reason: collision with root package name */
    private static final Interpolator f11311g = new COUIOutEaseInterpolator();

    /* renamed from: a, reason: collision with root package name */
    private int f11312a = 2;

    /* renamed from: b, reason: collision with root package name */
    private boolean f11313b;

    /* renamed from: c, reason: collision with root package name */
    private ValueAnimator f11314c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIPanelAdjustResizeHelperAfterR.java */
    /* renamed from: f2.d$a */
    /* loaded from: classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f11315a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ int f11316b;

        /* renamed from: c, reason: collision with root package name */
        final /* synthetic */ int f11317c;

        a(View view, int i10, int i11) {
            this.f11315a = view;
            this.f11316b = i10;
            this.f11317c = i11;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int i10;
            if (this.f11315a.isAttachedToWindow()) {
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = this.f11315a.getLayoutParams();
                View findViewById = this.f11315a.findViewById(R$id.coui_panel_content_layout);
                if (this.f11316b > 0 && intValue >= (i10 = this.f11317c) && findViewById != null) {
                    findViewById.setPadding(0, 0, 0, Math.max(intValue - i10, 0));
                    intValue = i10;
                }
                View view = this.f11315a;
                if ((view instanceof IgnoreWindowInsetsFrameLayout) && layoutParams.height > 0) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = intValue;
                    view.setLayoutParams(layoutParams);
                } else if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = intValue;
                    view.setLayoutParams(layoutParams);
                }
                if (this.f11315a instanceof COUIPanelContentLayout) {
                    COUIViewMarginUtil.b(findViewById.findViewById(com.support.appcompat.R$id.design_bottom_sheet), 3, 0);
                } else {
                    COUIViewMarginUtil.b(findViewById, 3, 0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIPanelAdjustResizeHelperAfterR.java */
    /* renamed from: f2.d$b */
    /* loaded from: classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ View f11319a;

        b(View view) {
            this.f11319a = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (this.f11319a != null) {
                this.f11319a.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                if (COUIPanelAdjustResizeHelperAfterR.this.f11313b) {
                    return;
                }
                COUIPanelAdjustResizeHelperAfterR.this.f11313b = true;
            }
        }
    }

    private void g(ViewGroup viewGroup, int i10, WindowInsets windowInsets, Context context, View view) {
        j(viewGroup, i10, windowInsets, view);
    }

    private ValueAnimator h(View view) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new b(view));
        return ofFloat;
    }

    private void i(View view, int i10, boolean z10, int i11, View view2, int i12) {
        float abs;
        int a10 = COUIViewMarginUtil.a(view, 3);
        ValueAnimator valueAnimator = this.f11314c;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.f11314c.cancel();
        }
        if (i10 == 0 && a10 == 0 && (view instanceof COUIPanelContentLayout)) {
            View findViewById = view.findViewById(R$id.coui_panel_content_layout);
            if (findViewById != null) {
                findViewById.setPadding(0, 0, 0, Math.max(i11, 0));
                return;
            }
            return;
        }
        int max = Math.max(0, Math.max(i11, 0) + i10 + i12);
        int max2 = Math.max(0, a10);
        int c10 = UIUtil.c(view.getContext());
        this.f11314c = ValueAnimator.ofInt(max2, max);
        if (COUIPanelMultiWindowUtils.r(view.getContext(), null)) {
            if (z10) {
                abs = Math.abs((i10 * 150.0f) / c10) + 300.0f;
                this.f11314c.setInterpolator(f11310f);
            } else {
                abs = Math.abs((i10 * 117.0f) / c10) + 200.0f;
                this.f11314c.setInterpolator(f11311g);
            }
        } else if (z10) {
            abs = Math.abs((i10 * 132.0f) / c10) + 300.0f;
            this.f11314c.setInterpolator(f11308d);
        } else {
            abs = Math.abs((i10 * 133.0f) / c10) + 200.0f;
            this.f11314c.setInterpolator(f11309e);
        }
        this.f11314c.setDuration(abs);
        int i13 = com.support.appcompat.R$id.design_bottom_sheet;
        ValueAnimator h10 = h(view2.findViewById(i13));
        h10.setDuration(250L);
        h10.setInterpolator(this.f11314c.getInterpolator());
        this.f11314c.addUpdateListener(new a(view, i11, i10));
        this.f11314c.start();
        if (!z10) {
            this.f11313b = false;
        }
        if (z10 && !this.f11313b && view2.findViewById(i13).getAlpha() == 0.0f) {
            h10.start();
        }
    }

    private void j(View view, int i10, WindowInsets windowInsets, View view2) {
        int i11;
        if (view != null) {
            View rootView = view.getRootView();
            int i12 = R$id.coui_panel_content_layout;
            if (rootView.findViewById(i12) != null) {
                view.getRootView().findViewById(i12).setPadding(0, 0, 0, 0);
            }
            int measuredHeight = view2.findViewById(R$id.coordinator).getMeasuredHeight();
            int measuredHeight2 = view.getMeasuredHeight();
            if (i10 > measuredHeight * 0.9f) {
                return;
            }
            int i13 = (measuredHeight <= 0 || measuredHeight2 <= 0 || (i11 = measuredHeight2 + i10) <= measuredHeight) ? i10 : i10 - (i11 - measuredHeight);
            int f10 = ((measuredHeight2 + i10) - measuredHeight) - COUIPanelMultiWindowUtils.f(view.getContext(), view.getContext().getResources().getConfiguration());
            int g6 = COUIPanelMultiWindowUtils.g(view.getContext(), view.getContext().getResources().getConfiguration(), windowInsets);
            if (i10 == 0) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = g6;
                    view.setLayoutParams(layoutParams);
                    return;
                }
                return;
            }
            i(view, i13, windowInsets.getInsets(WindowInsets.Type.ime()).bottom != 0, f10, view2, g6);
        }
    }

    @Override // f2.COUIAbsPanelAdjustResizeHelper
    public void a(Context context, ViewGroup viewGroup, WindowInsets windowInsets, View view, boolean z10) {
        int i10 = 0;
        if (z10) {
            i10 = Math.max(0, windowInsets.getInsets(WindowInsets.Type.ime()).bottom - windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom);
        }
        g(viewGroup, i10, windowInsets, context, view);
    }

    @Override // f2.COUIAbsPanelAdjustResizeHelper
    public boolean b() {
        return true;
    }

    @Override // f2.COUIAbsPanelAdjustResizeHelper
    public void c() {
    }

    @Override // f2.COUIAbsPanelAdjustResizeHelper
    public void d(int i10) {
        this.f11312a = i10;
    }
}
