package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.ViewCompat;
import androidx.core.widget.PopupWindowCompat;
import com.oplus.statistics.DataTypeConstants;

/* loaded from: classes.dex */
public class ListPopupWindow implements ShowableListMenu {
    final i A;
    private final h B;
    private final g C;
    private final e D;
    private Runnable E;
    final Handler F;
    private final Rect G;
    private Rect H;
    private boolean I;
    PopupWindow J;

    /* renamed from: e, reason: collision with root package name */
    private Context f1024e;

    /* renamed from: f, reason: collision with root package name */
    private ListAdapter f1025f;

    /* renamed from: g, reason: collision with root package name */
    DropDownListView f1026g;

    /* renamed from: h, reason: collision with root package name */
    private int f1027h;

    /* renamed from: i, reason: collision with root package name */
    private int f1028i;

    /* renamed from: j, reason: collision with root package name */
    private int f1029j;

    /* renamed from: k, reason: collision with root package name */
    private int f1030k;

    /* renamed from: l, reason: collision with root package name */
    private int f1031l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f1032m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f1033n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f1034o;

    /* renamed from: p, reason: collision with root package name */
    private int f1035p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f1036q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f1037r;

    /* renamed from: s, reason: collision with root package name */
    int f1038s;

    /* renamed from: t, reason: collision with root package name */
    private View f1039t;

    /* renamed from: u, reason: collision with root package name */
    private int f1040u;

    /* renamed from: v, reason: collision with root package name */
    private DataSetObserver f1041v;

    /* renamed from: w, reason: collision with root package name */
    private View f1042w;

    /* renamed from: x, reason: collision with root package name */
    private Drawable f1043x;

    /* renamed from: y, reason: collision with root package name */
    private AdapterView.OnItemClickListener f1044y;

    /* renamed from: z, reason: collision with root package name */
    private AdapterView.OnItemSelectedListener f1045z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            View t7 = ListPopupWindow.this.t();
            if (t7 == null || t7.getWindowToken() == null) {
                return;
            }
            ListPopupWindow.this.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements AdapterView.OnItemSelectedListener {
        b() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i10, long j10) {
            DropDownListView dropDownListView;
            if (i10 == -1 || (dropDownListView = ListPopupWindow.this.f1026g) == null) {
                return;
            }
            dropDownListView.setListSelectionHidden(false);
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class c {
        static int a(PopupWindow popupWindow, View view, int i10, boolean z10) {
            return popupWindow.getMaxAvailableHeight(view, i10, z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class d {
        static void a(PopupWindow popupWindow, Rect rect) {
            popupWindow.setEpicenterBounds(rect);
        }

        static void b(PopupWindow popupWindow, boolean z10) {
            popupWindow.setIsClippedToScreen(z10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class e implements Runnable {
        e() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ListPopupWindow.this.r();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class f extends DataSetObserver {
        f() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            if (ListPopupWindow.this.a()) {
                ListPopupWindow.this.b();
            }
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class g implements AbsListView.OnScrollListener {
        g() {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i10, int i11, int i12) {
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i10) {
            if (i10 != 1 || ListPopupWindow.this.A() || ListPopupWindow.this.J.getContentView() == null) {
                return;
            }
            ListPopupWindow listPopupWindow = ListPopupWindow.this;
            listPopupWindow.F.removeCallbacks(listPopupWindow.A);
            ListPopupWindow.this.A.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class h implements View.OnTouchListener {
        h() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            PopupWindow popupWindow;
            int action = motionEvent.getAction();
            int x10 = (int) motionEvent.getX();
            int y4 = (int) motionEvent.getY();
            if (action == 0 && (popupWindow = ListPopupWindow.this.J) != null && popupWindow.isShowing() && x10 >= 0 && x10 < ListPopupWindow.this.J.getWidth() && y4 >= 0 && y4 < ListPopupWindow.this.J.getHeight()) {
                ListPopupWindow listPopupWindow = ListPopupWindow.this;
                listPopupWindow.F.postDelayed(listPopupWindow.A, 250L);
                return false;
            }
            if (action != 1) {
                return false;
            }
            ListPopupWindow listPopupWindow2 = ListPopupWindow.this;
            listPopupWindow2.F.removeCallbacks(listPopupWindow2.A);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class i implements Runnable {
        i() {
        }

        @Override // java.lang.Runnable
        public void run() {
            DropDownListView dropDownListView = ListPopupWindow.this.f1026g;
            if (dropDownListView == null || !ViewCompat.P(dropDownListView) || ListPopupWindow.this.f1026g.getCount() <= ListPopupWindow.this.f1026g.getChildCount()) {
                return;
            }
            int childCount = ListPopupWindow.this.f1026g.getChildCount();
            ListPopupWindow listPopupWindow = ListPopupWindow.this;
            if (childCount <= listPopupWindow.f1038s) {
                listPopupWindow.J.setInputMethodMode(2);
                ListPopupWindow.this.b();
            }
        }
    }

    public ListPopupWindow(Context context) {
        this(context, null, R$attr.listPopupWindowStyle);
    }

    private void C() {
        View view = this.f1039t;
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.f1039t);
            }
        }
    }

    private void N(boolean z10) {
        d.b(this.J, z10);
    }

    private int q() {
        int i10;
        int i11;
        int makeMeasureSpec;
        int i12;
        if (this.f1026g == null) {
            Context context = this.f1024e;
            this.E = new a();
            DropDownListView s7 = s(context, !this.I);
            this.f1026g = s7;
            Drawable drawable = this.f1043x;
            if (drawable != null) {
                s7.setSelector(drawable);
            }
            this.f1026g.setAdapter(this.f1025f);
            this.f1026g.setOnItemClickListener(this.f1044y);
            this.f1026g.setFocusable(true);
            this.f1026g.setFocusableInTouchMode(true);
            this.f1026g.setOnItemSelectedListener(new b());
            this.f1026g.setOnScrollListener(this.C);
            AdapterView.OnItemSelectedListener onItemSelectedListener = this.f1045z;
            if (onItemSelectedListener != null) {
                this.f1026g.setOnItemSelectedListener(onItemSelectedListener);
            }
            View view = this.f1026g;
            View view2 = this.f1039t;
            if (view2 != null) {
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(1);
                ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
                int i13 = this.f1040u;
                if (i13 == 0) {
                    linearLayout.addView(view2);
                    linearLayout.addView(view, layoutParams);
                } else if (i13 != 1) {
                    Log.e("ListPopupWindow", "Invalid hint position " + this.f1040u);
                } else {
                    linearLayout.addView(view, layoutParams);
                    linearLayout.addView(view2);
                }
                int i14 = this.f1028i;
                if (i14 >= 0) {
                    i12 = Integer.MIN_VALUE;
                } else {
                    i14 = 0;
                    i12 = 0;
                }
                view2.measure(View.MeasureSpec.makeMeasureSpec(i14, i12), 0);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) view2.getLayoutParams();
                i10 = view2.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin;
                view = linearLayout;
            } else {
                i10 = 0;
            }
            this.J.setContentView(view);
        } else {
            View view3 = this.f1039t;
            if (view3 != null) {
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) view3.getLayoutParams();
                i10 = view3.getMeasuredHeight() + layoutParams3.topMargin + layoutParams3.bottomMargin;
            } else {
                i10 = 0;
            }
        }
        Drawable background = this.J.getBackground();
        if (background != null) {
            background.getPadding(this.G);
            Rect rect = this.G;
            int i15 = rect.top;
            i11 = rect.bottom + i15;
            if (!this.f1032m) {
                this.f1030k = -i15;
            }
        } else {
            this.G.setEmpty();
            i11 = 0;
        }
        int u7 = u(t(), this.f1030k, this.J.getInputMethodMode() == 2);
        if (this.f1036q || this.f1027h == -1) {
            return u7 + i11;
        }
        int i16 = this.f1028i;
        if (i16 == -2) {
            int i17 = this.f1024e.getResources().getDisplayMetrics().widthPixels;
            Rect rect2 = this.G;
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i17 - (rect2.left + rect2.right), Integer.MIN_VALUE);
        } else if (i16 != -1) {
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i16, 1073741824);
        } else {
            int i18 = this.f1024e.getResources().getDisplayMetrics().widthPixels;
            Rect rect3 = this.G;
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i18 - (rect3.left + rect3.right), 1073741824);
        }
        int d10 = this.f1026g.d(makeMeasureSpec, 0, -1, u7 - i10, -1);
        if (d10 > 0) {
            i10 += i11 + this.f1026g.getPaddingTop() + this.f1026g.getPaddingBottom();
        }
        return d10 + i10;
    }

    private int u(View view, int i10, boolean z10) {
        return c.a(this.J, view, i10, z10);
    }

    public boolean A() {
        return this.J.getInputMethodMode() == 2;
    }

    public boolean B() {
        return this.I;
    }

    public void D(View view) {
        this.f1042w = view;
    }

    public void E(int i10) {
        this.J.setAnimationStyle(i10);
    }

    public void F(int i10) {
        Drawable background = this.J.getBackground();
        if (background != null) {
            background.getPadding(this.G);
            Rect rect = this.G;
            this.f1028i = rect.left + rect.right + i10;
            return;
        }
        Q(i10);
    }

    public void G(int i10) {
        this.f1035p = i10;
    }

    public void H(Rect rect) {
        this.H = rect != null ? new Rect(rect) : null;
    }

    public void I(int i10) {
        this.J.setInputMethodMode(i10);
    }

    public void J(boolean z10) {
        this.I = z10;
        this.J.setFocusable(z10);
    }

    public void K(PopupWindow.OnDismissListener onDismissListener) {
        this.J.setOnDismissListener(onDismissListener);
    }

    public void L(AdapterView.OnItemClickListener onItemClickListener) {
        this.f1044y = onItemClickListener;
    }

    public void M(boolean z10) {
        this.f1034o = true;
        this.f1033n = z10;
    }

    public void O(int i10) {
        this.f1040u = i10;
    }

    public void P(int i10) {
        DropDownListView dropDownListView = this.f1026g;
        if (!a() || dropDownListView == null) {
            return;
        }
        dropDownListView.setListSelectionHidden(false);
        dropDownListView.setSelection(i10);
        if (dropDownListView.getChoiceMode() != 0) {
            dropDownListView.setItemChecked(i10, true);
        }
    }

    public void Q(int i10) {
        this.f1028i = i10;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public boolean a() {
        return this.J.isShowing();
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public void b() {
        int q10 = q();
        boolean A = A();
        PopupWindowCompat.b(this.J, this.f1031l);
        if (this.J.isShowing()) {
            if (ViewCompat.P(t())) {
                int i10 = this.f1028i;
                if (i10 == -1) {
                    i10 = -1;
                } else if (i10 == -2) {
                    i10 = t().getWidth();
                }
                int i11 = this.f1027h;
                if (i11 == -1) {
                    if (!A) {
                        q10 = -1;
                    }
                    if (A) {
                        this.J.setWidth(this.f1028i == -1 ? -1 : 0);
                        this.J.setHeight(0);
                    } else {
                        this.J.setWidth(this.f1028i == -1 ? -1 : 0);
                        this.J.setHeight(-1);
                    }
                } else if (i11 != -2) {
                    q10 = i11;
                }
                this.J.setOutsideTouchable((this.f1037r || this.f1036q) ? false : true);
                this.J.update(t(), this.f1029j, this.f1030k, i10 < 0 ? -1 : i10, q10 < 0 ? -1 : q10);
                return;
            }
            return;
        }
        int i12 = this.f1028i;
        if (i12 == -1) {
            i12 = -1;
        } else if (i12 == -2) {
            i12 = t().getWidth();
        }
        int i13 = this.f1027h;
        if (i13 == -1) {
            q10 = -1;
        } else if (i13 != -2) {
            q10 = i13;
        }
        this.J.setWidth(i12);
        this.J.setHeight(q10);
        N(true);
        this.J.setOutsideTouchable((this.f1037r || this.f1036q) ? false : true);
        this.J.setTouchInterceptor(this.B);
        if (this.f1034o) {
            PopupWindowCompat.a(this.J, this.f1033n);
        }
        d.a(this.J, this.H);
        PopupWindowCompat.c(this.J, t(), this.f1029j, this.f1030k, this.f1035p);
        this.f1026g.setSelection(-1);
        if (!this.I || this.f1026g.isInTouchMode()) {
            r();
        }
        if (this.I) {
            return;
        }
        this.F.post(this.D);
    }

    public void c(Drawable drawable) {
        this.J.setBackgroundDrawable(drawable);
    }

    public int d() {
        return this.f1029j;
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public void dismiss() {
        this.J.dismiss();
        C();
        this.J.setContentView(null);
        this.f1026g = null;
        this.F.removeCallbacks(this.A);
    }

    public void f(int i10) {
        this.f1029j = i10;
    }

    public Drawable i() {
        return this.J.getBackground();
    }

    @Override // androidx.appcompat.view.menu.ShowableListMenu
    public ListView j() {
        return this.f1026g;
    }

    public void l(int i10) {
        this.f1030k = i10;
        this.f1032m = true;
    }

    public int o() {
        if (this.f1032m) {
            return this.f1030k;
        }
        return 0;
    }

    public void p(ListAdapter listAdapter) {
        DataSetObserver dataSetObserver = this.f1041v;
        if (dataSetObserver == null) {
            this.f1041v = new f();
        } else {
            ListAdapter listAdapter2 = this.f1025f;
            if (listAdapter2 != null) {
                listAdapter2.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.f1025f = listAdapter;
        if (listAdapter != null) {
            listAdapter.registerDataSetObserver(this.f1041v);
        }
        DropDownListView dropDownListView = this.f1026g;
        if (dropDownListView != null) {
            dropDownListView.setAdapter(this.f1025f);
        }
    }

    public void r() {
        DropDownListView dropDownListView = this.f1026g;
        if (dropDownListView != null) {
            dropDownListView.setListSelectionHidden(true);
            dropDownListView.requestLayout();
        }
    }

    DropDownListView s(Context context, boolean z10) {
        return new DropDownListView(context, z10);
    }

    public View t() {
        return this.f1042w;
    }

    public Object v() {
        if (a()) {
            return this.f1026g.getSelectedItem();
        }
        return null;
    }

    public long w() {
        if (a()) {
            return this.f1026g.getSelectedItemId();
        }
        return Long.MIN_VALUE;
    }

    public int x() {
        if (a()) {
            return this.f1026g.getSelectedItemPosition();
        }
        return -1;
    }

    public View y() {
        if (a()) {
            return this.f1026g.getSelectedView();
        }
        return null;
    }

    public int z() {
        return this.f1028i;
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public ListPopupWindow(Context context, AttributeSet attributeSet, int i10, int i11) {
        this.f1027h = -2;
        this.f1028i = -2;
        this.f1031l = DataTypeConstants.APP_LOG;
        this.f1035p = 0;
        this.f1036q = false;
        this.f1037r = false;
        this.f1038s = Integer.MAX_VALUE;
        this.f1040u = 0;
        this.A = new i();
        this.B = new h();
        this.C = new g();
        this.D = new e();
        this.G = new Rect();
        this.f1024e = context;
        this.F = new Handler(context.getMainLooper());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ListPopupWindow, i10, i11);
        this.f1029j = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        this.f1030k = dimensionPixelOffset;
        if (dimensionPixelOffset != 0) {
            this.f1032m = true;
        }
        obtainStyledAttributes.recycle();
        AppCompatPopupWindow appCompatPopupWindow = new AppCompatPopupWindow(context, attributeSet, i10, i11);
        this.J = appCompatPopupWindow;
        appCompatPopupWindow.setInputMethodMode(1);
    }
}
