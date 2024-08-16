package f3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.support.control.R$attr;
import com.support.control.R$color;
import com.support.control.R$dimen;
import com.support.control.R$id;
import com.support.control.R$layout;
import com.support.control.R$style;
import com.support.control.R$styleable;
import h3.UIUtil;
import m1.COUIMoveEaseInterpolator;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;
import z2.COUIChangeTextUtil;

/* compiled from: COUIToolTips.java */
/* renamed from: f3.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIToolTips extends PopupWindow {
    private float A;
    private Interpolator B;
    private boolean C;
    private Rect G;
    private Rect H;
    private int I;
    private ColorStateList J;
    private ImageView K;

    /* renamed from: a, reason: collision with root package name */
    private final Context f11322a;

    /* renamed from: d, reason: collision with root package name */
    private int f11325d;

    /* renamed from: e, reason: collision with root package name */
    private View f11326e;

    /* renamed from: g, reason: collision with root package name */
    private Rect f11328g;

    /* renamed from: h, reason: collision with root package name */
    private ViewGroup f11329h;

    /* renamed from: i, reason: collision with root package name */
    private ViewGroup f11330i;

    /* renamed from: j, reason: collision with root package name */
    private TextView f11331j;

    /* renamed from: k, reason: collision with root package name */
    private ScrollView f11332k;

    /* renamed from: l, reason: collision with root package name */
    private ImageView f11333l;

    /* renamed from: n, reason: collision with root package name */
    private View f11335n;

    /* renamed from: o, reason: collision with root package name */
    private Drawable f11336o;

    /* renamed from: p, reason: collision with root package name */
    private Drawable f11337p;

    /* renamed from: q, reason: collision with root package name */
    private Drawable f11338q;

    /* renamed from: r, reason: collision with root package name */
    private Drawable f11339r;

    /* renamed from: t, reason: collision with root package name */
    private boolean f11341t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f11342u;

    /* renamed from: v, reason: collision with root package name */
    private int f11343v;

    /* renamed from: w, reason: collision with root package name */
    private int f11344w;

    /* renamed from: x, reason: collision with root package name */
    private g f11345x;

    /* renamed from: z, reason: collision with root package name */
    private float f11347z;

    /* renamed from: b, reason: collision with root package name */
    private final int[] f11323b = new int[2];

    /* renamed from: c, reason: collision with root package name */
    private final Point f11324c = new Point();

    /* renamed from: f, reason: collision with root package name */
    private Rect f11327f = new Rect();

    /* renamed from: m, reason: collision with root package name */
    private boolean f11334m = false;

    /* renamed from: s, reason: collision with root package name */
    private int f11340s = 4;

    /* renamed from: y, reason: collision with root package name */
    private int[] f11346y = new int[2];
    private int D = -1;
    private View.OnLayoutChangeListener E = new a();
    private PopupWindow.OnDismissListener F = new b();

    /* compiled from: COUIToolTips.java */
    /* renamed from: f3.a$a */
    /* loaded from: classes.dex */
    class a implements View.OnLayoutChangeListener {
        a() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
            Rect rect = new Rect(i10, i11, i12, i13);
            Rect rect2 = new Rect(i14, i15, i16, i17);
            if (!COUIToolTips.this.isShowing() || rect.equals(rect2) || COUIToolTips.this.f11335n == null) {
                return;
            }
            COUIToolTips.this.w();
        }
    }

    /* compiled from: COUIToolTips.java */
    /* renamed from: f3.a$b */
    /* loaded from: classes.dex */
    class b implements PopupWindow.OnDismissListener {
        b() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            COUIToolTips.this.f11329h.removeAllViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIToolTips.java */
    /* renamed from: f3.a$c */
    /* loaded from: classes.dex */
    public class c implements View.OnClickListener {
        c() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            COUIToolTips.this.dismiss();
            if (COUIToolTips.this.f11345x != null) {
                COUIToolTips.this.f11345x.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIToolTips.java */
    /* renamed from: f3.a$d */
    /* loaded from: classes.dex */
    public class d implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f11351e;

        d(int i10) {
            this.f11351e = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            Rect rect = new Rect();
            androidx.coordinatorlayout.widget.b.a(COUIToolTips.this.f11330i, COUIToolTips.this.K, rect);
            int i10 = this.f11351e;
            rect.inset(-i10, -i10);
            COUIToolTips.this.f11330i.setTouchDelegate(new TouchDelegate(rect, COUIToolTips.this.K));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIToolTips.java */
    /* renamed from: f3.a$e */
    /* loaded from: classes.dex */
    public class e implements Animation.AnimationListener {
        e() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            if (COUIToolTips.this.f11330i != null) {
                UIUtil.i(COUIToolTips.this.f11330i, COUIToolTips.this.f11322a.getResources().getColor(R$color.coui_tool_tips_shadow_color));
                UIUtil.h(COUIToolTips.this.f11330i, 0);
            }
            if (COUIToolTips.this.f11333l != null) {
                UIUtil.i(COUIToolTips.this.f11333l, COUIToolTips.this.f11322a.getResources().getColor(R$color.coui_tool_tips_shadow_color));
                UIUtil.h(COUIToolTips.this.f11333l, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIToolTips.java */
    /* renamed from: f3.a$f */
    /* loaded from: classes.dex */
    public class f implements Animation.AnimationListener {
        f() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            COUIToolTips.this.p();
            COUIToolTips.this.C = false;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            COUIToolTips.this.C = true;
            if (COUIToolTips.this.f11330i != null) {
                UIUtil.i(COUIToolTips.this.f11330i, 0);
                UIUtil.h(COUIToolTips.this.f11330i, 0);
            }
            if (COUIToolTips.this.f11333l != null) {
                UIUtil.i(COUIToolTips.this.f11333l, 0);
                UIUtil.h(COUIToolTips.this.f11333l, 0);
            }
        }
    }

    /* compiled from: COUIToolTips.java */
    /* renamed from: f3.a$g */
    /* loaded from: classes.dex */
    public interface g {
        void a();
    }

    public COUIToolTips(Context context, int i10) {
        this.f11322a = context;
        s(i10);
    }

    private void F() {
        Resources resources = this.f11322a.getResources();
        int i10 = R$dimen.tool_tips_max_width;
        int dimensionPixelSize = resources.getDimensionPixelSize(i10) + this.f11330i.getPaddingLeft() + this.f11330i.getPaddingRight();
        int i11 = this.f11340s;
        if (i11 == 8) {
            dimensionPixelSize = Math.min(this.f11327f.right - this.G.right, dimensionPixelSize);
        } else if (i11 == 16) {
            dimensionPixelSize = Math.min(this.G.left - this.f11327f.left, dimensionPixelSize);
        }
        Rect rect = this.f11327f;
        int min = Math.min(rect.right - rect.left, dimensionPixelSize);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f11332k.getLayoutParams();
        this.f11331j.setMaxWidth((((min - this.f11330i.getPaddingLeft()) - this.f11330i.getPaddingRight()) - layoutParams.leftMargin) - layoutParams.rightMargin);
        this.f11330i.measure(0, 0);
        setWidth(Math.min(this.f11330i.getMeasuredWidth(), min));
        setHeight(this.f11330i.getMeasuredHeight());
        if ((this.G.centerY() - (((q() + this.f11330i.getPaddingTop()) - this.f11330i.getPaddingBottom()) / 2)) + q() >= this.f11327f.bottom) {
            this.f11340s = 4;
            int dimensionPixelSize2 = this.f11322a.getResources().getDimensionPixelSize(i10) + this.f11330i.getPaddingLeft() + this.f11330i.getPaddingRight();
            Rect rect2 = this.f11327f;
            int min2 = Math.min(rect2.right - rect2.left, dimensionPixelSize2);
            this.f11331j.setMaxWidth((((min2 - this.f11330i.getPaddingLeft()) - this.f11330i.getPaddingRight()) - layoutParams.leftMargin) - layoutParams.rightMargin);
            this.f11330i.measure(0, 0);
            setWidth(Math.min(this.f11330i.getMeasuredWidth(), min2));
            setHeight(this.f11330i.getMeasuredHeight());
        }
    }

    private void G() {
        this.f11326e.removeOnLayoutChangeListener(this.E);
    }

    private void j(Rect rect, int i10, int i11) {
        int i12 = this.f11340s;
        if (i12 == 128 || i12 == 4) {
            i11 = 0;
        } else {
            i10 = 0;
        }
        this.f11333l = new ImageView(this.f11322a);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        int i13 = this.f11340s;
        if (i13 == 4 || i13 == 128) {
            this.f11326e.getRootView().getLocationOnScreen(this.f11323b);
            int i14 = this.f11323b[0];
            this.f11326e.getRootView().getLocationInWindow(this.f11323b);
            layoutParams.leftMargin = (((rect.centerX() - this.f11324c.x) - (i14 - this.f11323b[0])) - (this.f11336o.getIntrinsicWidth() / 2)) + i10;
            layoutParams.rightMargin = (getWidth() - layoutParams.leftMargin) - this.f11336o.getIntrinsicWidth();
            if (this.f11324c.y >= rect.top - this.f11346y[1]) {
                this.f11333l.setBackground(this.f11336o);
                this.f11334m = true;
                layoutParams.topMargin = (this.f11330i.getPaddingTop() - this.f11336o.getIntrinsicHeight()) + i11;
            } else {
                this.f11333l.setBackground(this.f11337p);
                layoutParams.gravity = 80;
                layoutParams.bottomMargin = (this.f11330i.getPaddingBottom() - this.f11337p.getIntrinsicHeight()) - i11;
            }
        } else if (i13 == 16) {
            this.f11334m = true;
            layoutParams.rightMargin = (this.f11330i.getPaddingRight() - this.f11339r.getIntrinsicWidth()) - i10;
            layoutParams.leftMargin = (getWidth() - layoutParams.rightMargin) - this.f11339r.getIntrinsicWidth();
            layoutParams.topMargin = (((rect.centerY() - this.f11324c.y) - this.f11346y[1]) - (this.f11339r.getIntrinsicHeight() / 2)) + i11;
            layoutParams.bottomMargin = (getHeight() - layoutParams.topMargin) - this.f11339r.getIntrinsicHeight();
            this.f11333l.setBackground(this.f11339r);
        } else {
            layoutParams.leftMargin = (this.f11330i.getPaddingLeft() - this.f11338q.getIntrinsicWidth()) + i10;
            layoutParams.rightMargin = (getWidth() - layoutParams.leftMargin) - this.f11338q.getIntrinsicWidth();
            layoutParams.topMargin = (((rect.centerY() - this.f11324c.y) - this.f11346y[1]) - (this.f11339r.getIntrinsicHeight() / 2)) + i11;
            layoutParams.bottomMargin = (getHeight() - layoutParams.topMargin) - this.f11339r.getIntrinsicHeight();
            this.f11333l.setBackground(this.f11338q);
        }
        this.f11329h.addView(this.f11333l, layoutParams);
        UIUtil.g(this.f11333l, false);
        UIUtil.k(this.f11333l, this.f11322a.getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.support_shadow_size_level_four), ContextCompat.c(this.f11322a, R$color.coui_tool_tips_shadow_color), this.f11322a.getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.support_shadow_size_level_two));
    }

    private void k() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, 1, this.f11347z, 1, this.A);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(this.B);
        animationSet.setDuration(300L);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setAnimationListener(new e());
        this.f11329h.startAnimation(animationSet);
    }

    private void l() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, 1, this.f11347z, 1, this.A);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(300L);
        animationSet.setInterpolator(this.B);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setAnimationListener(new f());
        this.f11329h.startAnimation(animationSet);
    }

    private void m() {
        int i10 = this.f11340s;
        if (i10 != 4 && i10 != 128) {
            this.f11347z = i10 == 16 ? 1.0f : 0.0f;
            this.A = ((this.G.centerY() - this.f11324c.y) - this.f11346y[1]) / q();
            return;
        }
        if ((this.G.centerX() - this.f11346y[0]) - this.f11324c.x >= r()) {
            this.f11347z = 1.0f;
        } else if (r() != 0) {
            int centerX = (this.G.centerX() - this.f11346y[0]) - this.f11324c.x;
            if (centerX <= 0) {
                centerX = -centerX;
            }
            this.f11347z = centerX / r();
        } else {
            this.f11347z = 0.5f;
        }
        if (this.f11324c.y >= this.G.top - this.f11346y[1]) {
            this.A = 0.0f;
        } else {
            this.A = 1.0f;
        }
    }

    private void n(View view, int i10, boolean z10, int i11, int i12, boolean z11) {
        this.f11341t = z10;
        this.f11342u = z11;
        this.f11343v = i11;
        this.f11344w = i12;
        this.f11340s = i10;
        if (i10 == 32 || i10 == 64) {
            if (t(view)) {
                this.f11340s = this.f11340s == 32 ? 8 : 16;
            } else {
                this.f11340s = this.f11340s != 32 ? 8 : 16;
            }
        }
        this.f11335n = view;
        this.f11326e.getWindowVisibleDisplayFrame(this.f11327f);
        x();
        Rect rect = new Rect();
        this.G = rect;
        view.getGlobalVisibleRect(rect);
        Rect rect2 = new Rect();
        this.H = rect2;
        this.f11326e.getGlobalVisibleRect(rect2);
        int[] iArr = new int[2];
        this.f11326e.getLocationOnScreen(iArr);
        this.G.offset(iArr[0], iArr[1]);
        this.H.offset(iArr[0], iArr[1]);
        Rect rect3 = this.f11327f;
        rect3.left = Math.max(rect3.left, this.H.left);
        Rect rect4 = this.f11327f;
        rect4.top = Math.max(rect4.top, this.H.top);
        Rect rect5 = this.f11327f;
        rect5.right = Math.min(rect5.right, this.H.right);
        Rect rect6 = this.f11327f;
        rect6.bottom = Math.min(rect6.bottom, this.H.bottom);
        F();
        v(this.G);
        if (z11) {
            u(this.G, z10, 0, 0);
        } else {
            u(this.G, z10, -i11, -i12);
        }
        setContentView(this.f11329h);
        m();
        k();
        Point point = this.f11324c;
        point.x += i11;
        point.y += i12;
    }

    private static ViewGroup o(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        return frameLayout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void p() {
        super.dismiss();
        G();
        this.f11329h.removeAllViews();
    }

    private int q() {
        int height = getHeight();
        Rect rect = this.f11328g;
        return (height - rect.top) + rect.bottom;
    }

    private int r() {
        int width = getWidth();
        Rect rect = this.f11328g;
        return (width - rect.left) + rect.right;
    }

    private void u(Rect rect, boolean z10, int i10, int i11) {
        this.f11329h.removeAllViews();
        this.f11329h.addView(this.f11330i);
        if (z10) {
            j(rect, i10, i11);
        }
    }

    private void v(Rect rect) {
        int r10;
        int centerY;
        int q10;
        int i10;
        this.D = -1;
        int i11 = this.f11340s;
        if (i11 == 4) {
            r10 = Math.min(rect.centerX() - (r() / 2), this.f11327f.right - r());
            int i12 = rect.top;
            Rect rect2 = this.f11327f;
            int i13 = i12 - rect2.top;
            int i14 = rect2.bottom - rect.bottom;
            q10 = q();
            if (i13 >= q10) {
                this.D = 4;
                i10 = rect.top;
                centerY = i10 - q10;
            } else if (i14 >= q10) {
                this.D = 128;
                centerY = rect.bottom;
            } else if (i13 > i14) {
                centerY = this.f11327f.top;
                setHeight(i13);
            } else {
                centerY = rect.bottom;
                setHeight(i14);
            }
        } else if (i11 == 128) {
            r10 = Math.min(rect.centerX() - (r() / 2), this.f11327f.right - r());
            int i15 = rect.top;
            Rect rect3 = this.f11327f;
            int i16 = i15 - rect3.top;
            int i17 = rect3.bottom - rect.bottom;
            q10 = q();
            if (i17 >= q10) {
                this.D = 128;
                centerY = rect.bottom;
            } else if (i16 >= q10) {
                this.D = 4;
                i10 = rect.top;
                centerY = i10 - q10;
            } else if (i16 > i17) {
                centerY = this.f11327f.top;
                setHeight(i16);
            } else {
                centerY = rect.bottom;
                setHeight(i17);
            }
        } else {
            r10 = i11 == 16 ? rect.left - r() : rect.right;
            centerY = rect.centerY() - (((q() + this.f11330i.getPaddingTop()) - this.f11330i.getPaddingBottom()) / 2);
        }
        this.f11326e.getRootView().getLocationOnScreen(this.f11323b);
        int[] iArr = this.f11323b;
        int i18 = iArr[0];
        int i19 = iArr[1];
        this.f11326e.getRootView().getLocationInWindow(this.f11323b);
        int[] iArr2 = this.f11323b;
        int i20 = iArr2[0];
        int i21 = iArr2[1];
        int[] iArr3 = this.f11346y;
        iArr3[0] = i18 - i20;
        iArr3[1] = i19 - i21;
        int i22 = r10 - iArr3[0];
        Rect rect4 = this.f11328g;
        int i23 = i22 - rect4.left;
        int i24 = (centerY - iArr3[1]) - rect4.top;
        int i25 = this.D;
        if (i25 == 4) {
            i24 -= this.I;
        }
        if (i25 == 128) {
            i24 += this.I;
        }
        this.f11324c.set(Math.max(0, i23), Math.max(0, i24));
    }

    private void x() {
        G();
        this.f11326e.addOnLayoutChangeListener(this.E);
    }

    public void A(boolean z10) {
        if (z10) {
            setTouchable(true);
            setFocusable(true);
            setOutsideTouchable(true);
        } else {
            setFocusable(false);
            setOutsideTouchable(false);
        }
        update();
    }

    public void B(View view, int i10) {
        C(view, i10, true);
    }

    public void C(View view, int i10, boolean z10) {
        D(view, i10, z10, 0, 0);
    }

    public void D(View view, int i10, boolean z10, int i11, int i12) {
        E(view, i10, z10, i11, i12, false);
    }

    public void E(View view, int i10, boolean z10, int i11, int i12, boolean z11) {
        if (isShowing()) {
            return;
        }
        this.f11326e = view.getRootView();
        n(view, i10, z10, i11, i12, z11);
        View view2 = this.f11326e;
        Point point = this.f11324c;
        showAtLocation(view2, 0, point.x, point.y);
        UIUtil.g(this.f11329h, false);
        for (ViewParent parent = this.f11329h.getParent(); parent != null && (parent instanceof ViewGroup); parent = parent.getParent()) {
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.setClipToOutline(false);
            viewGroup.setClipChildren(false);
            UIUtil.g((View) parent, false);
        }
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        if (!this.C) {
            l();
        } else {
            p();
            this.C = false;
        }
    }

    public void s(int i10) {
        int i11;
        int i12;
        this.f11325d = i10;
        if (i10 == 0) {
            i11 = R$attr.couiToolTipsStyle;
            i12 = COUIContextUtil.e(this.f11322a) ? R$style.COUIToolTips_Dark : R$style.COUIToolTips;
        } else {
            i11 = R$attr.couiToolTipsDetailFloatingStyle;
            i12 = COUIContextUtil.e(this.f11322a) ? R$style.COUIToolTips_DetailFloating_Dark : R$style.COUIToolTips_DetailFloating;
        }
        TypedArray obtainStyledAttributes = this.f11322a.obtainStyledAttributes(null, R$styleable.COUIToolTips, i11, i12);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.COUIToolTips_couiToolTipsBackground);
        drawable.setDither(true);
        this.f11336o = obtainStyledAttributes.getDrawable(R$styleable.COUIToolTips_couiToolTipsArrowUpDrawable);
        this.f11337p = obtainStyledAttributes.getDrawable(R$styleable.COUIToolTips_couiToolTipsArrowDownDrawable);
        this.f11338q = obtainStyledAttributes.getDrawable(R$styleable.COUIToolTips_couiToolTipsArrowLeftDrawable);
        this.f11339r = obtainStyledAttributes.getDrawable(R$styleable.COUIToolTips_couiToolTipsArrowRightDrawable);
        this.I = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsArrowOverflowOffset, 0);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsMinWidth, 0);
        int i13 = obtainStyledAttributes.getInt(R$styleable.COUIToolTips_couiToolTipsContainerLayoutGravity, 0);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsContainerLayoutMarginStart, 0);
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsContainerLayoutMarginTop, 0);
        int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsContainerLayoutMarginEnd, 0);
        int dimensionPixelSize5 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsContainerLayoutMarginBottom, 0);
        this.J = obtainStyledAttributes.getColorStateList(R$styleable.COUIToolTips_couiToolTipsContentTextColor);
        int dimensionPixelSize6 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsViewportOffsetStart, 0);
        int dimensionPixelSize7 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsViewportOffsetTop, 0);
        int dimensionPixelSize8 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsViewportOffsetEnd, 0);
        int dimensionPixelSize9 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.COUIToolTips_couiToolTipsViewportOffsetBottom, 0);
        int dimensionPixelOffset = this.f11322a.getResources().getDimensionPixelOffset(R$dimen.couiToolTipsCancelButtonInsects);
        obtainStyledAttributes.recycle();
        this.B = new COUIMoveEaseInterpolator();
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.f11322a).inflate(R$layout.coui_tool_tips_layout, (ViewGroup) null);
        this.f11330i = viewGroup;
        viewGroup.setBackground(drawable);
        this.f11330i.setMinimumWidth(dimensionPixelSize);
        UIUtil.k(this.f11330i, this.f11322a.getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.support_shadow_size_level_four), ContextCompat.c(this.f11322a, R$color.coui_tool_tips_shadow_color), this.f11322a.getResources().getDimensionPixelOffset(com.support.appcompat.R$dimen.support_shadow_size_level_two));
        ViewGroup o10 = o(this.f11322a);
        this.f11329h = o10;
        COUIDarkModeUtil.b(o10, false);
        TextView textView = (TextView) this.f11330i.findViewById(R$id.contentTv);
        this.f11331j = textView;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        ScrollView scrollView = (ScrollView) this.f11330i.findViewById(R$id.scrollView);
        this.f11332k = scrollView;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) scrollView.getLayoutParams();
        layoutParams.gravity = i13;
        layoutParams.setMargins(dimensionPixelSize2, dimensionPixelSize3, dimensionPixelSize4, dimensionPixelSize5);
        layoutParams.setMarginStart(dimensionPixelSize2);
        layoutParams.setMarginEnd(dimensionPixelSize4);
        this.f11332k.setLayoutParams(layoutParams);
        this.f11331j.setTextSize(0, (int) COUIChangeTextUtil.e(this.f11322a.getResources().getDimensionPixelSize(i10 == 0 ? R$dimen.tool_tips_content_text_size : R$dimen.detail_floating_content_text_size), this.f11322a.getResources().getConfiguration().fontScale, 4));
        ColorStateList colorStateList = this.J;
        if (colorStateList != null) {
            this.f11331j.setTextColor(colorStateList);
        }
        ImageView imageView = (ImageView) this.f11330i.findViewById(R$id.dismissIv);
        this.K = imageView;
        if (i10 == 0) {
            imageView.setVisibility(0);
            this.K.setOnClickListener(new c());
        } else {
            imageView.setVisibility(8);
        }
        this.K.post(new d(dimensionPixelOffset));
        if (!t(this.f11330i)) {
            this.f11328g = new Rect(dimensionPixelSize6, dimensionPixelSize7, dimensionPixelSize8, dimensionPixelSize9);
        } else {
            this.f11328g = new Rect(dimensionPixelSize8, dimensionPixelSize7, dimensionPixelSize6, dimensionPixelSize9);
        }
        setClippingEnabled(false);
        setAnimationStyle(0);
        setBackgroundDrawable(new ColorDrawable(0));
        setElevation(this.f11322a.getResources().getDimensionPixelOffset(r4));
        setOnDismissListener(this.F);
        ImageView imageView2 = this.f11333l;
        if (imageView2 != null) {
            int i14 = this.f11340s;
            if (i14 != 4 && i14 != 128) {
                imageView2.setBackground(this.f11334m ? this.f11339r : this.f11338q);
            } else {
                imageView2.setBackground(this.f11334m ? this.f11336o : this.f11337p);
            }
        }
    }

    public boolean t(View view) {
        return view.getLayoutDirection() == 1;
    }

    public void w() {
        n(this.f11335n, this.f11340s, this.f11341t, this.f11343v, this.f11344w, this.f11342u);
        Point point = this.f11324c;
        update(point.x, point.y, getWidth(), getHeight());
    }

    public void y(View view) {
        this.f11332k.removeAllViews();
        this.f11332k.addView(view);
    }

    public void z(CharSequence charSequence) {
        this.f11331j.setText(charSequence);
    }
}
