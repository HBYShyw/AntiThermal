package com.coui.appcompat.snackbar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.coui.appcompat.snackbar.COUISnackBar;
import com.coui.component.responsiveui.ResponsiveUIModel;
import com.coui.component.responsiveui.layoutgrid.MarginType;
import com.support.control.R$color;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$styleable;
import h3.UIUtil;
import m1.COUIInEaseInterpolator;
import m1.COUIOutEaseInterpolator;

/* loaded from: classes.dex */
public class COUISnackBar extends RelativeLayout {
    private static final PathInterpolator A = new COUIOutEaseInterpolator();
    private static final PathInterpolator B = new PathInterpolator(0.0f, 0.0f, 0.15f, 1.0f);
    private static final PathInterpolator C = new COUIInEaseInterpolator();
    private static final PathInterpolator D = new COUIInEaseInterpolator();
    private static int E;

    /* renamed from: e, reason: collision with root package name */
    private final int f7680e;

    /* renamed from: f, reason: collision with root package name */
    private final int f7681f;

    /* renamed from: g, reason: collision with root package name */
    private final int f7682g;

    /* renamed from: h, reason: collision with root package name */
    private final int f7683h;

    /* renamed from: i, reason: collision with root package name */
    private final int f7684i;

    /* renamed from: j, reason: collision with root package name */
    private final int f7685j;

    /* renamed from: k, reason: collision with root package name */
    private final int f7686k;

    /* renamed from: l, reason: collision with root package name */
    private final int f7687l;

    /* renamed from: m, reason: collision with root package name */
    private ViewGroup f7688m;

    /* renamed from: n, reason: collision with root package name */
    private ViewGroup f7689n;

    /* renamed from: o, reason: collision with root package name */
    private TextView f7690o;

    /* renamed from: p, reason: collision with root package name */
    private TextView f7691p;

    /* renamed from: q, reason: collision with root package name */
    private ImageView f7692q;

    /* renamed from: r, reason: collision with root package name */
    private View f7693r;

    /* renamed from: s, reason: collision with root package name */
    private int f7694s;

    /* renamed from: t, reason: collision with root package name */
    private int f7695t;

    /* renamed from: u, reason: collision with root package name */
    private String f7696u;

    /* renamed from: v, reason: collision with root package name */
    private Runnable f7697v;

    /* renamed from: w, reason: collision with root package name */
    private e f7698w;

    /* renamed from: x, reason: collision with root package name */
    private Rect f7699x;

    /* renamed from: y, reason: collision with root package name */
    private ResponsiveUIModel f7700y;

    /* renamed from: z, reason: collision with root package name */
    private boolean f7701z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a extends ViewOutlineProvider {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ int f7702a;

        a(int i10) {
            this.f7702a = i10;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            int i10 = this.f7702a;
            Log.d("COUISnackBar", "getOutline radius: " + this.f7702a);
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        @SuppressLint({"UseCompatLoadingForDrawables"})
        public void onGlobalLayout() {
            COUISnackBar.this.f7690o.getViewTreeObserver().removeOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.coui.appcompat.snackbar.c
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public final void onGlobalLayout() {
                    COUISnackBar.b.this.onGlobalLayout();
                }
            });
            int measureText = (int) COUISnackBar.this.f7690o.getPaint().measureText(COUISnackBar.this.f7696u);
            int dimensionPixelOffset = COUISnackBar.this.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_snack_bar_child_margin_horizontal) * 2;
            COUISnackBar cOUISnackBar = COUISnackBar.this;
            cOUISnackBar.f7701z = measureText < cOUISnackBar.f7689n.getMeasuredWidth() - dimensionPixelOffset;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements Animator.AnimatorListener {
        c() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            COUISnackBar.this.f7689n.setVisibility(8);
            if (COUISnackBar.this.f7688m != null) {
                COUISnackBar.this.f7688m.removeView(COUISnackBar.this.f7693r);
            }
            if (COUISnackBar.this.f7698w != null) {
                COUISnackBar.this.f7698w.a(COUISnackBar.this);
            }
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class d implements Runnable {
        private d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            COUISnackBar.this.k();
        }

        /* synthetic */ d(COUISnackBar cOUISnackBar, com.coui.appcompat.snackbar.b bVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public interface e {
        void a(COUISnackBar cOUISnackBar);
    }

    public COUISnackBar(Context context) {
        super(context);
        this.f7680e = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_max_width);
        this.f7681f = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_vertical);
        this.f7682g = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_horizontal);
        this.f7683h = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_margin_vertical);
        this.f7684i = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_context_margin_start_with_icon);
        this.f7685j = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_icon_width);
        this.f7686k = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_margin_top_horizontal);
        this.f7687l = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_margin_horizontal);
        this.f7699x = new Rect();
        this.f7700y = new ResponsiveUIModel(getContext(), 0, 0);
        this.f7701z = true;
        m(context, null);
    }

    private int getContainerWidth() {
        int paddingLeft = this.f7694s + this.f7689n.getPaddingLeft() + this.f7689n.getPaddingRight();
        if (this.f7691p.getVisibility() == 0) {
            paddingLeft += this.f7691p.getMeasuredWidth() + (this.f7687l << 1);
        }
        return n() ? paddingLeft + this.f7685j + this.f7684i : paddingLeft;
    }

    private int getMaxWidth() {
        getWindowVisibleDisplayFrame(this.f7699x);
        this.f7700y.rebuild(Math.max(0, this.f7699x.width()), Math.max(0, this.f7699x.height())).chooseMargin(MarginType.MARGIN_SMALL);
        return this.f7700y.calculateGridWidth(6);
    }

    private void i(View view, int i10) {
        if (view == null || l(view) == i10) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int measuredHeight = (i10 - view.getMeasuredHeight()) / 2;
        view.offsetTopAndBottom(measuredHeight - layoutParams.topMargin);
        layoutParams.topMargin = measuredHeight;
        layoutParams.bottomMargin = measuredHeight;
    }

    private void j() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.f7689n, "alpha", 1.0f, 0.0f);
        ofFloat.setInterpolator(C);
        ofFloat.setDuration(180L);
        ofFloat.addListener(new c());
        ofFloat.start();
    }

    private int l(View view) {
        if (view == null) {
            return 0;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private void m(Context context, AttributeSet attributeSet) {
        View inflate = RelativeLayout.inflate(context, R$layout.coui_snack_bar_item, this);
        this.f7693r = inflate;
        this.f7689n = (ViewGroup) inflate.findViewById(R$id.snack_bar);
        this.f7690o = (TextView) this.f7693r.findViewById(R$id.tv_snack_bar_content);
        this.f7691p = (TextView) this.f7693r.findViewById(R$id.tv_snack_bar_action);
        this.f7692q = (ImageView) this.f7693r.findViewById(R$id.iv_snack_bar_icon);
        E = new ViewGroup.MarginLayoutParams(context, attributeSet).bottomMargin;
        setVisibility(8);
        this.f7697v = new d(this, null);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUISnackBar, 0, 0);
        try {
            try {
                int i10 = R$styleable.COUISnackBar_defaultSnackBarContentText;
                if (obtainStyledAttributes.getString(i10) != null) {
                    setContentText(obtainStyledAttributes.getString(i10));
                    setDuration(obtainStyledAttributes.getInt(R$styleable.COUISnackBar_snackBarDisappearTime, 0));
                }
                setIconDrawable(obtainStyledAttributes.getDrawable(R$styleable.COUISnackBar_couiSnackBarIcon));
            } catch (Exception e10) {
                Log.e("COUISnackBar", "Failure setting COUISnackBar " + e10.getMessage());
            }
            a aVar = new a(getContext().getResources().getDimensionPixelOffset(R$dimen.coui_snack_bar_radius_single_line));
            this.f7690o.getViewTreeObserver().addOnGlobalLayoutListener(new b());
            this.f7689n.setOutlineProvider(aVar);
            this.f7689n.setClipToOutline(true);
            UIUtil.k(this.f7689n, getContext().getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.support_shadow_size_level_four), getContext().getResources().getColor(R$color.coui_snack_bar_background_shadow_color), context.getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.support_shadow_size_level_one));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private boolean n() {
        return this.f7692q.getDrawable() != null;
    }

    private boolean o() {
        boolean z10 = getContainerWidth() > this.f7689n.getMeasuredWidth();
        boolean z11 = this.f7690o.getLineCount() > 1;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.f7690o.getLayoutParams();
        if (!z11 && !z10) {
            layoutParams.topMargin = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_vertical);
            layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_horizontal));
            return false;
        }
        layoutParams.topMargin = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_vertical_multi_lines);
        layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_horizontal_no_icon));
        return true;
    }

    private void p() {
        int l10 = l(this.f7690o);
        int l11 = l(this.f7691p);
        int max = Math.max(l10, l11);
        if (!n()) {
            if (l10 > l11) {
                i(this.f7691p, l10);
                return;
            } else {
                i(this.f7690o, l11);
                return;
            }
        }
        int l12 = l(this.f7692q);
        int max2 = Math.max(l12, max);
        if (max2 == l12) {
            i(this.f7690o, l12);
            i(this.f7691p, l12);
        } else if (max2 == l10) {
            i(this.f7692q, l10);
            i(this.f7691p, l10);
        } else {
            i(this.f7692q, l11);
            i(this.f7691p, l11);
        }
    }

    private void q() {
        if (n()) {
            ((RelativeLayout.LayoutParams) this.f7692q.getLayoutParams()).topMargin = ((this.f7690o.getMeasuredHeight() - this.f7692q.getMeasuredHeight()) / 2) + this.f7681f;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.f7691p.getLayoutParams();
        layoutParams.topMargin = this.f7681f + this.f7690o.getMeasuredHeight() + this.f7686k;
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_margin_bottom_multi_lines);
        this.f7691p.setLayoutParams(layoutParams);
    }

    private void setActionText(String str) {
        this.f7691p.setText(str);
    }

    private void setParent(ViewGroup viewGroup) {
        this.f7688m = viewGroup;
    }

    public String getActionText() {
        return String.valueOf(this.f7691p.getText());
    }

    public TextView getActionView() {
        return this.f7691p;
    }

    public String getContentText() {
        return String.valueOf(this.f7690o.getText());
    }

    public TextView getContentView() {
        return this.f7690o;
    }

    public int getDuration() {
        return this.f7695t;
    }

    public void h() {
        if (o()) {
            q();
        } else {
            p();
        }
    }

    public void k() {
        Runnable runnable = this.f7697v;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        j();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.f7697v);
        this.f7688m = null;
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        super.onLayout(z10, i10, i11, i12, i13);
        if (z10) {
            h();
        }
    }

    @Override // android.widget.RelativeLayout, android.view.View
    protected void onMeasure(int i10, int i11) {
        int mode = View.MeasureSpec.getMode(i10);
        int size = View.MeasureSpec.getSize(i10);
        this.f7694s = ((int) this.f7690o.getPaint().measureText(this.f7696u)) + (this.f7682g << 1);
        int maxWidth = getMaxWidth() + this.f7689n.getPaddingLeft() + this.f7689n.getPaddingRight();
        if (maxWidth > size) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.f7689n.getLayoutParams();
            Resources resources = getResources();
            int i12 = com.support.appcompat.R$dimen.grid_guide_column_card_margin_start;
            layoutParams.setMarginStart(resources.getDimensionPixelOffset(i12) - this.f7689n.getPaddingStart());
            layoutParams.setMarginEnd(getResources().getDimensionPixelOffset(i12) - this.f7689n.getPaddingEnd());
            this.f7689n.setLayoutParams(layoutParams);
        }
        if (maxWidth > 0 && mode != 0) {
            i10 = View.MeasureSpec.makeMeasureSpec(Math.min(maxWidth, size), mode);
        }
        super.onMeasure(i10, i11);
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x000d, code lost:
    
        if (r4 != 3) goto L18;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                }
            }
            if (this.f7697v != null && getDuration() != 0) {
                removeCallbacks(this.f7697v);
                postDelayed(this.f7697v, getDuration());
            }
            return true;
        }
        Runnable runnable = this.f7697v;
        if (runnable != null) {
            removeCallbacks(runnable);
        }
        return true;
    }

    public void setContentText(int i10) {
        setContentText(getResources().getString(i10));
    }

    public void setDuration(int i10) {
        this.f7695t = i10;
    }

    @Override // android.view.View
    public void setEnabled(boolean z10) {
        Runnable runnable;
        super.setEnabled(z10);
        this.f7691p.setEnabled(z10);
        this.f7690o.setEnabled(z10);
        this.f7692q.setEnabled(z10);
        if (getDuration() == 0 || (runnable = this.f7697v) == null) {
            return;
        }
        removeCallbacks(runnable);
        postDelayed(this.f7697v, getDuration());
    }

    public void setIconDrawable(int i10) {
        setIconDrawable(getResources().getDrawable(i10, getContext().getTheme()));
    }

    public void setOnStatusChangeListener(e eVar) {
        this.f7698w = eVar;
    }

    public void setContentText(String str) {
        if (TextUtils.isEmpty(str)) {
            this.f7690o.setVisibility(8);
            Runnable runnable = this.f7697v;
            if (runnable != null) {
                removeCallbacks(runnable);
                return;
            }
            return;
        }
        this.f7690o.setText(str);
        this.f7696u = str;
    }

    public void setIconDrawable(Drawable drawable) {
        if (drawable == null) {
            this.f7692q.setVisibility(8);
            ((RelativeLayout.LayoutParams) this.f7690o.getLayoutParams()).setMarginStart(getContext().getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_horizontal_no_icon));
        } else {
            this.f7692q.setVisibility(0);
            this.f7692q.setImageDrawable(drawable);
            ((RelativeLayout.LayoutParams) this.f7690o.getLayoutParams()).setMarginStart(getContext().getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_horizontal));
        }
    }

    public COUISnackBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7680e = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_max_width);
        this.f7681f = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_vertical);
        this.f7682g = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_child_margin_horizontal);
        this.f7683h = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_margin_vertical);
        this.f7684i = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_context_margin_start_with_icon);
        this.f7685j = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_icon_width);
        this.f7686k = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_margin_top_horizontal);
        this.f7687l = getResources().getDimensionPixelSize(R$dimen.coui_snack_bar_action_margin_horizontal);
        this.f7699x = new Rect();
        this.f7700y = new ResponsiveUIModel(getContext(), 0, 0);
        this.f7701z = true;
        m(context, attributeSet);
    }
}
