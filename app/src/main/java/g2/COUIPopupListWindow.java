package g2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.core.view.ViewCompat;
import com.coui.appcompat.list.COUIForegroundListView;
import com.coui.appcompat.poplist.COUIPopupWindow;
import com.coui.component.responsiveui.unit.Dp;
import com.coui.component.responsiveui.window.WindowHeightSizeClass;
import com.coui.component.responsiveui.window.WindowSizeClass;
import com.coui.component.responsiveui.window.WindowWidthSizeClass;
import com.support.appcompat.R$anim;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$color;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$drawable;
import com.support.appcompat.R$id;
import com.support.appcompat.R$integer;
import com.support.appcompat.R$layout;
import h3.FollowHandManager;
import h3.UIUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import v1.COUIContextUtil;

/* compiled from: COUIPopupListWindow.java */
/* renamed from: g2.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPopupListWindow extends COUIPopupWindow implements View.OnLayoutChangeListener {
    private Interpolator A;
    private boolean B;
    private Point C;
    private Rect D;
    private boolean E;
    private int F;
    private int G;
    private int H;
    private int I;
    private int J;
    private boolean K;
    private int[] L;
    private int M;
    private Animation.AnimationListener N;
    private final AdapterView.OnItemClickListener O;

    /* renamed from: c, reason: collision with root package name */
    private Context f11498c;

    /* renamed from: d, reason: collision with root package name */
    private BaseAdapter f11499d;

    /* renamed from: e, reason: collision with root package name */
    private BaseAdapter f11500e;

    /* renamed from: f, reason: collision with root package name */
    private BaseAdapter f11501f;

    /* renamed from: g, reason: collision with root package name */
    private View f11502g;

    /* renamed from: h, reason: collision with root package name */
    private Rect f11503h;

    /* renamed from: i, reason: collision with root package name */
    private List<PopupListItem> f11504i;

    /* renamed from: j, reason: collision with root package name */
    private ViewGroup f11505j;

    /* renamed from: k, reason: collision with root package name */
    private ListView f11506k;

    /* renamed from: l, reason: collision with root package name */
    private ListView f11507l;

    /* renamed from: m, reason: collision with root package name */
    private AdapterView.OnItemClickListener f11508m;

    /* renamed from: n, reason: collision with root package name */
    private int[] f11509n;

    /* renamed from: o, reason: collision with root package name */
    private int f11510o;

    /* renamed from: p, reason: collision with root package name */
    private int f11511p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f11512q;

    /* renamed from: r, reason: collision with root package name */
    private COUIPopupListWindow f11513r;

    /* renamed from: s, reason: collision with root package name */
    private COUISubMenuClickListener f11514s;

    /* renamed from: t, reason: collision with root package name */
    private int f11515t;

    /* renamed from: u, reason: collision with root package name */
    private int f11516u;

    /* renamed from: v, reason: collision with root package name */
    private float f11517v;

    /* renamed from: w, reason: collision with root package name */
    private float f11518w;

    /* renamed from: x, reason: collision with root package name */
    private int f11519x;

    /* renamed from: y, reason: collision with root package name */
    private int f11520y;

    /* renamed from: z, reason: collision with root package name */
    private Interpolator f11521z;

    /* compiled from: COUIPopupListWindow.java */
    /* renamed from: g2.b$a */
    /* loaded from: classes.dex */
    class a implements Animation.AnimationListener {
        a() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            COUIPopupListWindow.this.U();
            COUIPopupListWindow.this.B = false;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            COUIPopupListWindow.this.B = true;
        }
    }

    /* compiled from: COUIPopupListWindow.java */
    /* renamed from: g2.b$b */
    /* loaded from: classes.dex */
    class b implements AdapterView.OnItemClickListener {
        b() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
            COUIPopupListWindow.this.f11508m.onItemClick(adapterView, view, i10, j10);
            if (COUIPopupListWindow.this.f11504i.isEmpty() || COUIPopupListWindow.this.f11504i.size() <= i10 || COUIPopupListWindow.this.f11504i.get(i10) == null || !((PopupListItem) COUIPopupListWindow.this.f11504i.get(i10)).h()) {
                return;
            }
            Context context = COUIPopupListWindow.this.x().getContext();
            COUIPopupListWindow.this.v(i10, context);
            if (!COUIPopupListWindow.F(COUIPopupListWindow.this.f11498c, FollowHandManager.i().width(), FollowHandManager.i().height())) {
                view.setBackgroundColor(COUIContextUtil.a(context, R$attr.couiColorCardPressed));
                int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.coui_sub_action_menu_rtl_offset);
                int dimensionPixelOffset2 = context.getResources().getDimensionPixelOffset(R$dimen.coui_sub_action_menu_offset_top);
                view.getLocationOnScreen(r3);
                int[] iArr = {iArr[0] - FollowHandManager.k()[0], iArr[1] - FollowHandManager.k()[1]};
                int width = ((iArr[0] - COUIPopupListWindow.this.f11513r.getWidth()) - dimensionPixelOffset) + COUIPopupListWindow.this.f11515t;
                int width2 = iArr[0] + adapterView.getWidth() + dimensionPixelOffset + COUIPopupListWindow.this.f11515t;
                boolean z10 = ViewCompat.x(COUIPopupListWindow.this.x()) == 1;
                if ((width < 0 || z10) && COUIPopupListWindow.this.f11513r.getWidth() + width2 <= FollowHandManager.i().right) {
                    width = width2;
                }
                int i11 = (iArr[1] - dimensionPixelOffset2) + COUIPopupListWindow.this.f11516u;
                if (COUIPopupListWindow.this.A() - i11 > COUIPopupListWindow.this.f11513r.getHeight()) {
                    COUIPopupListWindow.this.f11513r.u(COUIPopupListWindow.this.x(), false);
                    COUIPopupListWindow.this.f11513r.showAtLocation(COUIPopupListWindow.this.x(), 0, width, i11);
                    return;
                } else {
                    COUIPopupListWindow.this.dismiss();
                    COUIPopupListWindow.this.f11513r.L(COUIPopupListWindow.this.f11509n[0], COUIPopupListWindow.this.f11509n[1], COUIPopupListWindow.this.f11509n[2], COUIPopupListWindow.this.f11509n[3]);
                    COUIPopupListWindow.this.f11513r.R(COUIPopupListWindow.this.x());
                    return;
                }
            }
            COUIPopupListWindow.this.dismiss();
            COUIPopupListWindow.this.f11513r.L(COUIPopupListWindow.this.f11509n[0], COUIPopupListWindow.this.f11509n[1], COUIPopupListWindow.this.f11509n[2], COUIPopupListWindow.this.f11509n[3]);
            COUIPopupListWindow.this.f11513r.R(COUIPopupListWindow.this.x());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIPopupListWindow.java */
    /* renamed from: g2.b$c */
    /* loaded from: classes.dex */
    public class c implements AdapterView.OnItemClickListener {
        c() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
            if (COUIPopupListWindow.this.f11514s != null) {
                COUIPopupListWindow.this.f11514s.onItemClick(adapterView, view, i10, j10);
            }
            COUIPopupListWindow.this.f11513r.dismiss();
            COUIPopupListWindow.this.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIPopupListWindow.java */
    /* renamed from: g2.b$d */
    /* loaded from: classes.dex */
    public class d implements PopupWindow.OnDismissListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f11525e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Context f11526f;

        d(int i10, Context context) {
            this.f11525e = i10;
            this.f11526f = context;
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            COUIPopupListWindow.this.z().getChildAt(this.f11525e).setBackgroundColor(COUIContextUtil.a(this.f11526f, R$attr.couiColorSurfaceTop));
        }
    }

    public COUIPopupListWindow(Context context) {
        super(context);
        this.f11503h = new Rect(0, 0, 0, 0);
        this.f11509n = new int[4];
        this.f11512q = false;
        this.f11515t = 0;
        this.f11516u = 0;
        this.C = new Point();
        this.E = true;
        this.K = false;
        this.L = new int[2];
        this.N = new a();
        this.O = new b();
        this.f11498c = context;
        this.f11504i = new ArrayList();
        this.f11510o = context.getResources().getDimensionPixelSize(R$dimen.coui_popup_list_window_min_width);
        this.F = this.f11498c.getResources().getDimensionPixelSize(R$dimen.coui_popup_list_window_item_icon_extra_width);
        this.M = this.f11498c.getResources().getDimensionPixelSize(R$dimen.coui_popup_list_divider_height);
        ListView listView = new ListView(context);
        this.f11507l = listView;
        listView.setDivider(null);
        this.f11507l.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.K = true;
        this.f11505j = w(context);
        setExitTransition(null);
        setEnterTransition(null);
        Resources resources = context.getResources();
        int i10 = R$integer.coui_animation_time_move_veryfast;
        this.f11519x = resources.getInteger(i10);
        this.f11520y = context.getResources().getInteger(i10);
        int i11 = R$anim.coui_curve_opacity_inout;
        this.f11521z = AnimationUtils.loadInterpolator(context, i11);
        this.A = AnimationUtils.loadInterpolator(context, i11);
        setAnimationStyle(0);
        if (!this.K) {
            setBackgroundDrawable(new ColorDrawable(0));
        } else {
            d(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int A() {
        return ((FollowHandManager.c() - FollowHandManager.f()) - FollowHandManager.j().top) - FollowHandManager.j().bottom;
    }

    private int B() {
        int i10 = ((FollowHandManager.i().right - FollowHandManager.i().left) - FollowHandManager.j().right) - FollowHandManager.j().left;
        Rect rect = this.f11503h;
        return (i10 - rect.left) - rect.right;
    }

    private boolean C() {
        return FollowHandManager.l();
    }

    private boolean D() {
        return getAnimationStyle() != 0;
    }

    private boolean E() {
        return this.f11502g.getRootView().findViewById(R$id.parentPanel) != null;
    }

    public static boolean F(Context context, int i10, int i11) {
        WindowSizeClass.Companion companion = WindowSizeClass.INSTANCE;
        Dp.Companion companion2 = Dp.INSTANCE;
        WindowSizeClass calculateFromSize = companion.calculateFromSize(companion2.pixel2Dp(context, Math.abs(i10)), companion2.pixel2Dp(context, Math.abs(i11)));
        return calculateFromSize.getWindowWidthSizeClass() == WindowWidthSizeClass.Compact || calculateFromSize.getWindowHeightSizeClass() == WindowHeightSizeClass.Compact;
    }

    private void K() {
        View findViewById;
        if (this.K) {
            int i10 = this.G;
            FollowHandManager.r(new Rect(i10, this.I, i10, this.H));
        }
        if (!this.E || (findViewById = this.f11502g.getRootView().findViewById(R$id.design_bottom_sheet)) == null) {
            return;
        }
        Rect rect = new Rect();
        this.D = rect;
        findViewById.getGlobalVisibleRect(rect);
        FollowHandManager.q(this.D);
    }

    private void O(int i10, int i11) {
        this.C.set(i10, i11);
    }

    private void S() {
        Point a10 = FollowHandManager.a(this.f11502g.getContext(), getWidth(), getHeight(), false);
        T(0, a10.x, a10.y, true);
    }

    private void T(int i10, int i11, int i12, boolean z10) {
        if (z10) {
            i12 = Math.max(FollowHandManager.f() + FollowHandManager.j().top, i12);
        }
        O(i11, i12);
        this.f11502g.getLocationOnScreen(this.L);
        super.showAtLocation(this.f11502g, i10, i11, i12);
    }

    private void V(boolean z10, int i10, int i11) {
        if (isShowing()) {
            if (i11 == getHeight() && i10 == getWidth()) {
                return;
            }
            if (z10) {
                Point s7 = s(i10, i11);
                update(s7.x, s7.y, i10, i11);
            } else {
                Point a10 = FollowHandManager.a(this.f11502g.getContext(), i10, i11, false);
                update(a10.x, a10.y, i10, i11);
            }
        }
    }

    private void p() {
        if (D()) {
            return;
        }
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, 1, this.f11517v, 1, this.f11518w);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        scaleAnimation.setDuration(this.f11519x);
        scaleAnimation.setInterpolator(this.f11521z);
        alphaAnimation.setDuration(this.f11520y);
        alphaAnimation.setInterpolator(this.A);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        this.f11505j.startAnimation(animationSet);
    }

    private void q() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(this.f11520y);
        alphaAnimation.setInterpolator(this.A);
        alphaAnimation.setAnimationListener(this.N);
        this.f11505j.startAnimation(alphaAnimation);
    }

    private void r() {
        if (this.C.x + (getWidth() / 2) == FollowHandManager.g()) {
            this.f11517v = 0.5f;
        } else {
            this.f11517v = ((FollowHandManager.g() - r0) / getWidth()) + 0.5f;
        }
        if (this.C.y >= FollowHandManager.h()) {
            this.f11518w = 0.0f;
        } else {
            this.f11518w = (FollowHandManager.h() - this.C.y) / getHeight();
        }
    }

    private Point s(int i10, int i11) {
        int centerX = FollowHandManager.b().centerX() - (i10 / 2);
        return new Point(Math.max(FollowHandManager.d() + FollowHandManager.j().left, Math.min(centerX, (FollowHandManager.e() - FollowHandManager.j().right) - getWidth())), (FollowHandManager.b().top - this.H) - i11);
    }

    private void t() {
        BaseAdapter baseAdapter = this.f11500e;
        if (baseAdapter == null) {
            this.f11501f = this.f11499d;
        } else {
            this.f11501f = baseAdapter;
        }
        this.f11506k.setAdapter((ListAdapter) this.f11501f);
        if (this.f11508m != null) {
            this.f11506k.setOnItemClickListener(this.O);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v(int i10, Context context) {
        if (this.f11513r == null) {
            COUIPopupListWindow cOUIPopupListWindow = new COUIPopupListWindow(context);
            this.f11513r = cOUIPopupListWindow;
            cOUIPopupListWindow.setInputMethodMode(2);
            this.f11513r.b(true);
            this.f11513r.J(this.f11504i.get(i10).d());
            this.f11513r.M(new c());
            this.f11513r.setOnDismissListener(new d(i10, context));
            this.f11513r.u(x(), false);
        }
    }

    private ViewGroup w(Context context) {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(this.K ? R$layout.coui_popup_list_window_layout : R$layout.coui_popup_list_window_layout_compat, (ViewGroup) null);
        this.f11506k = (ListView) frameLayout.findViewById(R$id.coui_popup_list_view);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{R$attr.couiPopupWindowBackground});
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        if (drawable == null) {
            drawable = context.getResources().getDrawable(R$drawable.coui_popup_window_background);
        }
        this.f11506k.setBackground(drawable);
        obtainStyledAttributes.recycle();
        if (this.K) {
            this.G = context.getResources().getDimensionPixelOffset(R$dimen.coui_popup_list_window_margin_horizontal);
            this.H = context.getResources().getDimensionPixelOffset(R$dimen.coui_popup_list_window_margin_bottom_new);
            this.I = context.getResources().getDimensionPixelOffset(R$dimen.coui_popup_list_window_margin_top);
        }
        this.J = context.getResources().getDimensionPixelOffset(R$dimen.coui_popup_list_window_content_radius);
        if (!this.K) {
            Resources resources = context.getResources();
            int i10 = R$dimen.support_shadow_size_level_three;
            int dimensionPixelOffset = resources.getDimensionPixelOffset(i10);
            this.f11503h.set(dimensionPixelOffset, context.getResources().getDimensionPixelOffset(R$dimen.coui_popup_list_window_margin_top), dimensionPixelOffset, context.getResources().getDimensionPixelOffset(R$dimen.coui_popup_list_window_margin_bottom));
            UIUtil.j(this.f11506k, context.getResources().getDimensionPixelOffset(i10), context.getResources().getColor(R$color.coui_popup_outline_spot_shadow_color));
        }
        ((COUIForegroundListView) this.f11506k).setRadius(this.J);
        return frameLayout;
    }

    private int y() {
        Drawable a10;
        for (PopupListItem popupListItem : this.f11504i) {
            if (popupListItem.b() == 0 && popupListItem.a() == null) {
                if (popupListItem.e().length() > 5) {
                    return this.F;
                }
            } else {
                if (popupListItem.a() == null) {
                    a10 = this.f11498c.getResources().getDrawable(popupListItem.b());
                } else {
                    a10 = popupListItem.a();
                }
                return a10.getIntrinsicWidth() + this.F;
            }
        }
        return 0;
    }

    public void G(boolean z10) {
        BaseAdapter baseAdapter = this.f11501f;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(B(), Integer.MIN_VALUE);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = baseAdapter.getCount();
        int i10 = 0;
        int i11 = makeMeasureSpec2;
        int i12 = 0;
        for (int i13 = 0; i13 < count; i13++) {
            View view = baseAdapter.getView(i13, null, this.f11507l);
            int i14 = ((AbsListView.LayoutParams) view.getLayoutParams()).height;
            if (i14 != -2) {
                i11 = View.MeasureSpec.makeMeasureSpec(i14, 1073741824);
            }
            view.measure(makeMeasureSpec, i11);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            if (measuredWidth > i12) {
                i12 = measuredWidth;
            }
            i10 += measuredHeight;
        }
        int i15 = this.f11511p;
        if (i15 != 0) {
            i10 = i15;
        }
        int A = A();
        int c10 = ((FollowHandManager.c() - FollowHandManager.j().bottom) - FollowHandManager.j().top) - FollowHandManager.b().bottom;
        if (this.f11512q && A > c10) {
            A = c10;
        }
        int max = Math.max(i12, this.f11510o);
        Rect rect = this.f11503h;
        int i16 = max + rect.left + rect.right;
        int min = Math.min(A, i10 + rect.top + rect.bottom);
        if (z10) {
            int h10 = C() ? FollowHandManager.h() : FollowHandManager.b().top;
            if (E()) {
                h10 += FollowHandManager.k()[1];
            }
            min = Math.min(h10 - UIUtil.f(this.f11498c), min);
        }
        int min2 = Math.min(i16 + y(), ((FollowHandManager.e() - FollowHandManager.d()) - FollowHandManager.j().left) - FollowHandManager.j().right);
        V(z10, min2, min);
        int i17 = min + (this.M * (count - 1));
        setWidth(min2);
        setHeight(i17);
    }

    public void H(boolean z10) {
        this.f11512q = z10;
    }

    public void I(View view) {
        this.f11502g = view;
    }

    public void J(List<PopupListItem> list) {
        if (list != null) {
            this.f11504i = list;
            this.f11499d = new DefaultAdapter(this.f11498c, list);
        }
    }

    public void L(int i10, int i11, int i12, int i13) {
        int[] iArr = this.f11509n;
        iArr[0] = i10;
        iArr[1] = i11;
        iArr[2] = i12;
        iArr[3] = i13;
    }

    public void M(AdapterView.OnItemClickListener onItemClickListener) {
        this.f11508m = onItemClickListener;
    }

    public void N(int i10) {
        BaseAdapter baseAdapter = this.f11499d;
        if (baseAdapter instanceof DefaultAdapter) {
            ((DefaultAdapter) baseAdapter).d(i10);
        }
    }

    public void P(COUISubMenuClickListener cOUISubMenuClickListener) {
        this.f11514s = cOUISubMenuClickListener;
    }

    public void Q(int i10, int i11) {
        this.f11515t = i10;
        this.f11516u = i11;
    }

    public void R(View view) {
        Context context = this.f11498c;
        if (((context instanceof Activity) && ((Activity) context).isFinishing()) || view.getWindowToken() == null) {
            return;
        }
        u(view, false);
        S();
        r();
        p();
    }

    public void U() {
        View view = this.f11502g;
        if (view != null) {
            view.getRootView().removeOnLayoutChangeListener(this);
        }
        super.setContentView(null);
        super.dismiss();
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        if (!this.B && !D()) {
            q();
        } else {
            U();
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
        Rect rect = new Rect(i10, i11, i12, i13);
        Rect rect2 = new Rect(i14, i15, i16, i17);
        int[] iArr = new int[2];
        View view2 = this.f11502g;
        if (view2 != null) {
            view2.getLocationOnScreen(iArr);
        }
        if (isShowing() && (!rect.equals(rect2) || !Arrays.equals(this.L, iArr))) {
            dismiss();
        } else {
            this.L = iArr;
        }
    }

    public void u(View view, boolean z10) {
        if (view != null) {
            if (this.f11499d == null && this.f11500e == null) {
                return;
            }
            this.f11502g = view;
            t();
            this.f11502g.getRootView().removeOnLayoutChangeListener(this);
            this.f11502g.getRootView().addOnLayoutChangeListener(this);
            int[] iArr = this.f11509n;
            FollowHandManager.o(view, -iArr[0], -iArr[1]);
            K();
            G(z10);
            setContentView(this.f11505j);
        }
    }

    public View x() {
        return this.f11502g;
    }

    public ListView z() {
        return this.f11506k;
    }
}
