package x1;

import android.R;
import android.app.Dialog;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import com.coui.appcompat.buttonBar.COUIButtonBarLayout;
import com.coui.appcompat.dialog.widget.COUIAlertDialogMaxLinearLayout;
import com.coui.appcompat.dialog.widget.COUIAlertDialogMaxScrollView;
import com.coui.appcompat.dialog.widget.COUIMaxHeightNestedScrollView;
import com.coui.appcompat.grid.COUIResponsiveUtils;
import com.coui.appcompat.statement.COUIMaxHeightScrollView;
import com.support.appcompat.R$dimen;
import com.support.appcompat.R$id;
import com.support.appcompat.R$style;
import com.support.appcompat.R$styleable;
import v1.COUIContextUtil;
import y1.COUIListDialogAdapter;
import y1.ChoiceListAdapter;
import y1.SummaryAdapter;

/* compiled from: COUIAlertDialogBuilder.java */
/* renamed from: x1.b, reason: use source file name */
/* loaded from: classes.dex */
public class COUIAlertDialogBuilder extends AlertDialog.a {
    private static final int J = R$attr.alertDialogStyle;
    private static final int K = R$style.AlertDialogBuildStyle;
    private static final int L = R$style.Animation_COUI_Dialog_Alpha;
    private int A;
    private boolean B;
    private boolean C;
    private boolean D;
    private int E;
    private boolean F;
    private boolean G;
    private boolean H;
    private ComponentCallbacks I;

    /* renamed from: c, reason: collision with root package name */
    private AlertDialog f19458c;

    /* renamed from: d, reason: collision with root package name */
    private int f19459d;

    /* renamed from: e, reason: collision with root package name */
    private int f19460e;

    /* renamed from: f, reason: collision with root package name */
    private int f19461f;

    /* renamed from: g, reason: collision with root package name */
    private int f19462g;

    /* renamed from: h, reason: collision with root package name */
    private int f19463h;

    /* renamed from: i, reason: collision with root package name */
    private boolean f19464i;

    /* renamed from: j, reason: collision with root package name */
    private CharSequence[] f19465j;

    /* renamed from: k, reason: collision with root package name */
    private CharSequence[] f19466k;

    /* renamed from: l, reason: collision with root package name */
    private DialogInterface.OnClickListener f19467l;

    /* renamed from: m, reason: collision with root package name */
    private boolean f19468m;

    /* renamed from: n, reason: collision with root package name */
    private boolean f19469n;

    /* renamed from: o, reason: collision with root package name */
    private boolean f19470o;

    /* renamed from: p, reason: collision with root package name */
    private boolean f19471p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f19472q;

    /* renamed from: r, reason: collision with root package name */
    private COUIListDialogAdapter f19473r;

    /* renamed from: s, reason: collision with root package name */
    private boolean f19474s;

    /* renamed from: t, reason: collision with root package name */
    private int f19475t;

    /* renamed from: u, reason: collision with root package name */
    private ChoiceListAdapter f19476u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f19477v;

    /* renamed from: w, reason: collision with root package name */
    private View f19478w;

    /* renamed from: x, reason: collision with root package name */
    public int[] f19479x;

    /* renamed from: y, reason: collision with root package name */
    private Point f19480y;

    /* renamed from: z, reason: collision with root package name */
    private Point f19481z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIAlertDialogBuilder.java */
    /* renamed from: x1.b$a */
    /* loaded from: classes.dex */
    public class a implements View.OnAttachStateChangeListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Window f19482e;

        a(Window window) {
            this.f19482e = window;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            COUIAlertDialogBuilder.this.U();
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            this.f19482e.getDecorView().removeOnAttachStateChangeListener(this);
            COUIAlertDialogBuilder.this.V();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIAlertDialogBuilder.java */
    /* renamed from: x1.b$b */
    /* loaded from: classes.dex */
    public class b implements COUIMaxHeightNestedScrollView.a {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ ViewGroup f19484a;

        b(ViewGroup viewGroup) {
            this.f19484a = viewGroup;
        }

        @Override // com.coui.appcompat.dialog.widget.COUIMaxHeightNestedScrollView.a
        public void a() {
            this.f19484a.setPadding(0, COUIAlertDialogBuilder.this.b().getResources().getDimensionPixelOffset(R$dimen.bottom_dialog_scroll_padding_top), 0, COUIAlertDialogBuilder.this.b().getResources().getDimensionPixelOffset(R$dimen.bottom_dialog_scroll_padding_bottom));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIAlertDialogBuilder.java */
    /* renamed from: x1.b$c */
    /* loaded from: classes.dex */
    public class c implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ COUIMaxHeightScrollView f19486e;

        /* compiled from: COUIAlertDialogBuilder.java */
        /* renamed from: x1.b$c$a */
        /* loaded from: classes.dex */
        class a implements View.OnTouchListener {
            a() {
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        }

        c(COUIMaxHeightScrollView cOUIMaxHeightScrollView) {
            this.f19486e = cOUIMaxHeightScrollView;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f19486e.getHeight() < this.f19486e.getMaxHeight()) {
                this.f19486e.setOnTouchListener(new a());
            }
        }
    }

    /* compiled from: COUIAlertDialogBuilder.java */
    /* renamed from: x1.b$d */
    /* loaded from: classes.dex */
    class d implements ComponentCallbacks {
        d() {
        }

        @Override // android.content.ComponentCallbacks
        public void onConfigurationChanged(Configuration configuration) {
            if (COUIAlertDialogBuilder.this.F) {
                COUIAlertDialogBuilder.this.l0(configuration);
            }
        }

        @Override // android.content.ComponentCallbacks
        public void onLowMemory() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIAlertDialogBuilder.java */
    /* renamed from: x1.b$e */
    /* loaded from: classes.dex */
    public static class e implements View.OnTouchListener {

        /* renamed from: e, reason: collision with root package name */
        private final Dialog f19490e;

        /* renamed from: f, reason: collision with root package name */
        private final int f19491f;

        public e(Dialog dialog) {
            this.f19490e = dialog;
            this.f19491f = ViewConfiguration.get(dialog.getContext()).getScaledWindowTouchSlop();
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.findViewById(R$id.parentPanel) == null) {
                return this.f19490e.onTouchEvent(motionEvent);
            }
            if (new RectF(r0.getLeft() + r0.getPaddingLeft(), r0.getTop() + r0.getPaddingTop(), r0.getRight() - r0.getPaddingRight(), r0.getBottom() - r0.getPaddingBottom()).contains(motionEvent.getX(), motionEvent.getY())) {
                return false;
            }
            MotionEvent obtain = MotionEvent.obtain(motionEvent);
            if (motionEvent.getAction() == 1) {
                obtain.setAction(4);
            }
            view.performClick();
            boolean onTouchEvent = this.f19490e.onTouchEvent(obtain);
            obtain.recycle();
            return onTouchEvent;
        }
    }

    public COUIAlertDialogBuilder(Context context) {
        this(context, R$style.COUIAlertDialog_BottomWarning);
        F();
    }

    private void D(AlertDialog alertDialog) {
        COUIMaxHeightScrollView cOUIMaxHeightScrollView = (COUIMaxHeightScrollView) alertDialog.findViewById(R$id.alert_title_scroll_view);
        if (cOUIMaxHeightScrollView == null) {
            return;
        }
        cOUIMaxHeightScrollView.post(new c(cOUIMaxHeightScrollView));
    }

    private void F() {
        TypedArray obtainStyledAttributes = b().obtainStyledAttributes(null, R$styleable.COUIAlertDialogBuilder, J, K);
        this.f19459d = obtainStyledAttributes.getInt(R$styleable.COUIAlertDialogBuilder_android_gravity, 17);
        this.f19460e = obtainStyledAttributes.getResourceId(R$styleable.COUIAlertDialogBuilder_windowAnimStyle, L);
        this.f19461f = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIAlertDialogBuilder_contentMaxWidth, 0);
        this.f19462g = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.COUIAlertDialogBuilder_contentMaxHeight, 0);
        this.f19463h = obtainStyledAttributes.getResourceId(R$styleable.COUIAlertDialogBuilder_customContentLayout, 0);
        this.f19464i = obtainStyledAttributes.getBoolean(R$styleable.COUIAlertDialogBuilder_isNeedToAdaptMessageAndList, false);
        this.D = obtainStyledAttributes.getBoolean(R$styleable.COUIAlertDialogBuilder_isTinyDialog, false);
        this.f19471p = obtainStyledAttributes.getBoolean(R$styleable.COUIAlertDialogBuilder_hasLoading, false);
        this.f19472q = obtainStyledAttributes.getBoolean(R$styleable.COUIAlertDialogBuilder_isAssignMentLayout, false);
        this.H = obtainStyledAttributes.getBoolean(R$styleable.COUIAlertDialogBuilder_isForceCenterStyleInLargeScreen, false);
        obtainStyledAttributes.recycle();
    }

    private void G(View view) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = -1;
        view.setLayoutParams(layoutParams);
    }

    private void H(Window window) {
        if (this.f19462g <= 0) {
            return;
        }
        View findViewById = window.findViewById(R$id.parentPanel);
        if (findViewById instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) findViewById).setMaxHeight(this.f19462g);
        } else if (findViewById instanceof COUIAlertDialogMaxScrollView) {
            ((COUIAlertDialogMaxScrollView) findViewById).setMaxHeight(this.f19462g);
        }
    }

    private void I(Window window) {
        if (this.f19461f <= 0) {
            return;
        }
        View findViewById = window.findViewById(R$id.parentPanel);
        if (findViewById instanceof COUIAlertDialogMaxLinearLayout) {
            ((COUIAlertDialogMaxLinearLayout) findViewById).setMaxWidth(this.f19461f);
        } else if (findViewById instanceof COUIAlertDialogMaxScrollView) {
            ((COUIAlertDialogMaxScrollView) findViewById).setMaxWidth(this.f19461f);
        }
    }

    private void J() {
        int i10;
        if (this.f19474s || (i10 = this.f19463h) == 0) {
            return;
        }
        u(i10);
    }

    private void K(Window window) {
        if (this.f19474s) {
            ViewGroup viewGroup = (ViewGroup) window.findViewById(R$id.customPanel);
            if (viewGroup != null) {
                viewGroup.setVisibility(0);
            }
            ViewGroup viewGroup2 = (ViewGroup) window.findViewById(R$id.custom);
            if (viewGroup2 != null) {
                viewGroup2.setVisibility(0);
            }
            if (this.D || this.f19471p) {
                return;
            }
            viewGroup2.setPaddingRelative(viewGroup2.getPaddingStart(), viewGroup2.getPaddingTop(), viewGroup2.getPaddingEnd(), b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_custom_padding_bottom));
        }
    }

    private void L(Window window) {
        ViewGroup viewGroup = (ViewGroup) window.findViewById(R$id.listPanel);
        AlertDialog alertDialog = this.f19458c;
        ListView j10 = alertDialog != null ? alertDialog.j() : null;
        if (j10 != null) {
            j10.setScrollIndicators(0);
        }
        boolean z10 = (!this.f19469n || viewGroup == null || j10 == null) ? false : true;
        if (z10) {
            viewGroup.addView(j10, new ViewGroup.LayoutParams(-1, -1));
        }
        ViewGroup viewGroup2 = (ViewGroup) window.findViewById(R$id.scrollView);
        if (viewGroup2 != null) {
            viewGroup2.setScrollIndicators(0);
            if (this.f19464i && z10) {
                j0(viewGroup2, 1);
                j0(viewGroup, 1);
            }
            if (viewGroup2 instanceof COUIMaxHeightNestedScrollView) {
                boolean c10 = AppFeatureUtil.c(b());
                if (this.f19470o && !c10) {
                    ((COUIMaxHeightNestedScrollView) viewGroup2).setMaxHeight(b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_content_max_height_with_adapter));
                }
                if (window.getAttributes().gravity == 80 && this.f19469n) {
                    if (this.f19471p || this.D) {
                        ((COUIMaxHeightNestedScrollView) viewGroup2).setConfigChangeListener(new b(viewGroup2));
                    }
                }
            }
        }
    }

    private void M() {
        boolean z10;
        AlertDialog alertDialog = this.f19458c;
        if (alertDialog == null) {
            return;
        }
        View findViewById = alertDialog.findViewById(R$id.scrollView);
        if (!this.f19468m && findViewById != null && ((z10 = this.D) || this.f19471p)) {
            if (z10) {
                findViewById.setPadding(findViewById.getPaddingLeft(), b().getResources().getDimensionPixelOffset(R$dimen.coui_tiny_dialog_scroll_padding_top_without_title), findViewById.getPaddingRight(), findViewById.getPaddingBottom());
            } else if (this.f19471p) {
                findViewById.setPadding(findViewById.getPaddingLeft(), findViewById.getPaddingTop(), findViewById.getPaddingRight(), b().getResources().getDimensionPixelOffset(R$dimen.center_dialog_scroll_padding_bottom_withouttitle));
            }
        }
        if (this.D || this.f19471p || !this.f19469n || findViewById == null) {
            return;
        }
        TextView textView = (TextView) this.f19458c.findViewById(R.id.message);
        textView.setTextColor(COUIContextUtil.a(b(), com.support.appcompat.R$attr.couiColorPrimaryNeutral));
        int dimensionPixelOffset = b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_scroll_padding_top_message);
        int dimensionPixelOffset2 = b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_scroll_padding_bottom_message);
        int dimensionPixelOffset3 = b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_scroll_padding_top_message_no_title);
        int dimensionPixelOffset4 = b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_scroll_padding_bottom_message_no_title);
        int dimensionPixelOffset5 = b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_message_min_height);
        if (this.f19468m) {
            findViewById.setPadding(findViewById.getPaddingLeft(), dimensionPixelOffset, findViewById.getPaddingRight(), dimensionPixelOffset2);
        } else {
            findViewById.setPadding(findViewById.getPaddingLeft(), dimensionPixelOffset3, findViewById.getPaddingRight(), dimensionPixelOffset4);
            dimensionPixelOffset5 = (dimensionPixelOffset5 - (dimensionPixelOffset3 - dimensionPixelOffset)) + (dimensionPixelOffset2 - dimensionPixelOffset4);
        }
        if (this.f19472q) {
            return;
        }
        textView.setMinimumHeight(dimensionPixelOffset5);
    }

    private void N(Window window) {
        View findViewById = window.findViewById(R$id.buttonPanel);
        CharSequence[] charSequenceArr = this.f19465j;
        boolean z10 = this.f19468m || this.f19469n || this.f19474s || this.f19470o || (charSequenceArr != null && charSequenceArr.length > 0);
        if (this.D) {
            if (findViewById == null || z10) {
                return;
            }
            findViewById.setPadding(findViewById.getPaddingLeft(), b().getResources().getDimensionPixelOffset(R$dimen.coui_tiny_dialog_btn_bar_padding_vertical), findViewById.getPaddingRight(), findViewById.getPaddingBottom());
            return;
        }
        if (!(findViewById instanceof COUIButtonBarLayout) || findViewById.getVisibility() == 8) {
            return;
        }
        int i10 = window.getAttributes().gravity;
        COUIButtonBarLayout cOUIButtonBarLayout = (COUIButtonBarLayout) findViewById;
        int buttonCount = cOUIButtonBarLayout.getButtonCount();
        boolean z11 = buttonCount == 1;
        if (i10 != 17) {
        }
        ViewGroup viewGroup = (ViewGroup) window.findViewById(R$id.listPanel);
        AlertDialog alertDialog = this.f19458c;
        boolean z12 = (viewGroup == null || (alertDialog != null ? alertDialog.j() : null) == null) ? false : true;
        if ((findViewById.getParent() instanceof NestedScrollView) && buttonCount >= 1) {
            if (!this.D && !this.f19471p) {
                int dimensionPixelOffset = b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_with_button_min_height_hor);
                ((NestedScrollView) findViewById.getParent()).setMinimumHeight(dimensionPixelOffset);
                findViewById.setMinimumHeight(dimensionPixelOffset);
            } else {
                ((NestedScrollView) findViewById.getParent()).setMinimumHeight(b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_with_button_min_height));
            }
            if (z12 && (((NestedScrollView) findViewById.getParent()).getLayoutParams() instanceof LinearLayoutCompat.LayoutParams)) {
                LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) ((NestedScrollView) findViewById.getParent()).getLayoutParams();
                ((LinearLayout.LayoutParams) layoutParams).weight = 0.0f;
                ((LinearLayout.LayoutParams) layoutParams).height = -2;
                ((NestedScrollView) findViewById.getParent()).setLayoutParams(layoutParams);
                ((NestedScrollView) findViewById.getParent()).requestLayout();
            }
        }
        if (z11 && !z10 && (this.D || this.f19471p)) {
            cOUIButtonBarLayout.setVerButVerPadding(b().getResources().getDimensionPixelOffset(R$dimen.coui_free_alert_dialog_single_btn_padding_vertical));
            cOUIButtonBarLayout.setVerButPaddingOffset(0);
        }
        cOUIButtonBarLayout.setDynamicLayout(this.C);
    }

    private void O(Window window) {
        View findViewById;
        if (this.D || this.f19471p || (findViewById = window.findViewById(R$id.title_template)) == null || !(findViewById instanceof LinearLayout)) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById.getLayoutParams();
        layoutParams.topMargin = b().getResources().getDimensionPixelOffset(R$dimen.coui_no_message_alert_dialog_title_margin_top);
        layoutParams.bottomMargin = b().getResources().getDimensionPixelOffset(R$dimen.coui_no_message_alert_dialog_title_margin_bottom);
        findViewById.setLayoutParams(layoutParams);
        P(window, window.findViewById(R$id.alert_title_scroll_view));
        G(window.findViewById(R$id.alertTitle));
    }

    private void P(Window window, View view) {
        if (view == null || !(view instanceof COUIMaxHeightScrollView)) {
            return;
        }
        int dimensionPixelOffset = window.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_title_scroll_max_height_normal);
        Resources resources = b().getResources();
        int i10 = R$dimen.coui_no_message_alert_dialog_title_margin_top;
        int dimensionPixelOffset2 = dimensionPixelOffset - resources.getDimensionPixelOffset(i10);
        Resources resources2 = b().getResources();
        int i11 = R$dimen.coui_no_message_alert_dialog_title_margin_bottom;
        int dimensionPixelOffset3 = dimensionPixelOffset2 - resources2.getDimensionPixelOffset(i11);
        int dimensionPixelOffset4 = (window.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_title_scroll_min_height) - b().getResources().getDimensionPixelOffset(i10)) - b().getResources().getDimensionPixelOffset(i11);
        COUIMaxHeightScrollView cOUIMaxHeightScrollView = (COUIMaxHeightScrollView) view;
        cOUIMaxHeightScrollView.setMaxHeight(dimensionPixelOffset3);
        cOUIMaxHeightScrollView.setMinHeight(dimensionPixelOffset4);
        cOUIMaxHeightScrollView.setFillViewport(true);
        View findViewById = window.findViewById(R$id.parentPanel);
        if (findViewById instanceof COUIAlertDialogMaxLinearLayout) {
            COUIAlertDialogMaxLinearLayout cOUIAlertDialogMaxLinearLayout = (COUIAlertDialogMaxLinearLayout) findViewById;
            cOUIAlertDialogMaxLinearLayout.setScrollMinHeight((b().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_title_scroll_min_height_no_message) - b().getResources().getDimensionPixelOffset(i10)) - b().getResources().getDimensionPixelOffset(i11));
            cOUIAlertDialogMaxLinearLayout.setNeedMinHeight(window.getContext().getResources().getDimensionPixelOffset(R$dimen.coui_alert_dialog_builder_parent_panel_min_height_normal));
            cOUIAlertDialogMaxLinearLayout.setNeedReMeasureLayoutId(cOUIMaxHeightScrollView.getId());
        }
    }

    private void Q(Window window) {
        if (R()) {
            COUIBottomAlertDialogAdjustUtil.d(window, this.f19478w, this.f19480y, this.f19481z);
            window.getDecorView().setVisibility(4);
        } else {
            k0(window.getContext().getResources().getConfiguration());
        }
        window.getDecorView().addOnAttachStateChangeListener(new a(window));
        window.getDecorView().setOnTouchListener(new e(this.f19458c));
        WindowManager.LayoutParams attributes = window.getAttributes();
        int i10 = this.f19475t;
        if (i10 > 0) {
            attributes.type = i10;
        }
        attributes.width = R() ? -2 : -1;
        window.setAttributes(attributes);
    }

    private boolean R() {
        return (this.f19478w == null && this.f19480y == null) ? false : true;
    }

    private boolean S(Configuration configuration) {
        return T(configuration) && this.H;
    }

    private boolean T(Configuration configuration) {
        if (this.B) {
            return true;
        }
        return !COUIResponsiveUtils.j(configuration.screenWidthDp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void U() {
        b().registerComponentCallbacks(this.I);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void V() {
        if (this.I != null) {
            b().unregisterComponentCallbacks(this.I);
        }
    }

    private void j0(View view, int i10) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            layoutParams.height = 0;
            ((LinearLayout.LayoutParams) layoutParams).weight = i10;
            view.setLayoutParams(layoutParams);
        }
    }

    private void k0(Configuration configuration) {
        if (S(configuration)) {
            this.G = true;
            this.f19458c.getWindow().setGravity(17);
            this.f19458c.getWindow().setWindowAnimations(L);
        } else {
            this.G = false;
            this.f19458c.getWindow().setGravity(this.f19459d);
            this.f19458c.getWindow().setWindowAnimations(this.f19460e);
        }
    }

    public static Context n0(Context context, int i10, int i11) {
        return new ContextThemeWrapper(new ContextThemeWrapper(context, i10), i11);
    }

    public AlertDialog A(View view, int i10, int i11) {
        return C(view, i10, i11, 0, 0);
    }

    public AlertDialog B(View view, Point point) {
        return A(view, point.x, point.y);
    }

    public AlertDialog C(View view, int i10, int i11, int i12, int i13) {
        if (T(b().getResources().getConfiguration())) {
            this.f19478w = view;
            if (i10 != 0 || i11 != 0) {
                Point point = new Point();
                this.f19480y = point;
                point.set(i10, i11);
            }
            if (i12 != 0 || i13 != 0) {
                Point point2 = new Point();
                this.f19481z = point2;
                point2.set(i12, i13);
            }
        }
        return a();
    }

    protected void E() {
        COUIListDialogAdapter cOUIListDialogAdapter = this.f19473r;
        if (cOUIListDialogAdapter != null) {
            cOUIListDialogAdapter.e((this.f19468m || this.f19469n) ? false : true);
            this.f19473r.d((this.f19474s || this.f19477v) ? false : true);
        }
        ChoiceListAdapter choiceListAdapter = this.f19476u;
        if (choiceListAdapter != null) {
            choiceListAdapter.m((this.f19468m || this.f19469n) ? false : true);
            this.f19476u.l((this.f19474s || this.f19477v) ? false : true);
        }
        if (this.f19470o) {
            return;
        }
        CharSequence[] charSequenceArr = this.f19465j;
        if (charSequenceArr != null && charSequenceArr.length > 0) {
            c(new SummaryAdapter(b(), (this.f19468m || this.f19469n) ? false : true, (this.f19474s || this.f19477v) ? false : true, this.f19465j, this.f19466k, this.f19479x), this.f19467l);
        }
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    /* renamed from: W, reason: merged with bridge method [inline-methods] */
    public COUIAlertDialogBuilder c(ListAdapter listAdapter, DialogInterface.OnClickListener onClickListener) {
        this.f19470o = listAdapter != null;
        if (listAdapter instanceof COUIListDialogAdapter) {
            this.f19473r = (COUIListDialogAdapter) listAdapter;
        }
        super.c(listAdapter, onClickListener);
        return this;
    }

    public void X(boolean z10) {
        this.f19477v = z10;
    }

    public COUIAlertDialogBuilder Y(int i10) {
        this.f19469n = !TextUtils.isEmpty(b().getString(i10));
        super.g(i10);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    /* renamed from: Z, reason: merged with bridge method [inline-methods] */
    public COUIAlertDialogBuilder h(CharSequence charSequence) {
        this.f19469n = !TextUtils.isEmpty(charSequence);
        super.h(charSequence);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    public AlertDialog a() {
        J();
        E();
        AlertDialog a10 = super.a();
        this.f19458c = a10;
        Q(a10.getWindow());
        return this.f19458c;
    }

    public COUIAlertDialogBuilder a0(int i10, DialogInterface.OnClickListener onClickListener) {
        super.j(i10, onClickListener);
        X(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    /* renamed from: b0, reason: merged with bridge method [inline-methods] */
    public COUIAlertDialogBuilder k(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        super.k(charSequence, onClickListener);
        X(true);
        return this;
    }

    public COUIAlertDialogBuilder c0(int i10, DialogInterface.OnClickListener onClickListener) {
        super.l(i10, onClickListener);
        X(true);
        return this;
    }

    public COUIAlertDialogBuilder d0(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        super.m(charSequence, onClickListener);
        X(true);
        return this;
    }

    public COUIAlertDialogBuilder e0(int i10, DialogInterface.OnClickListener onClickListener) {
        super.o(i10, onClickListener);
        X(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    /* renamed from: f0, reason: merged with bridge method [inline-methods] */
    public COUIAlertDialogBuilder p(CharSequence charSequence, DialogInterface.OnClickListener onClickListener) {
        super.p(charSequence, onClickListener);
        X(true);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    /* renamed from: g0, reason: merged with bridge method [inline-methods] */
    public COUIAlertDialogBuilder q(ListAdapter listAdapter, int i10, DialogInterface.OnClickListener onClickListener) {
        this.f19470o = listAdapter != null;
        super.q(listAdapter, i10, onClickListener);
        return this;
    }

    public COUIAlertDialogBuilder h0(int i10) {
        this.f19468m = !TextUtils.isEmpty(b().getString(i10));
        super.s(i10);
        return this;
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    /* renamed from: i0, reason: merged with bridge method [inline-methods] */
    public COUIAlertDialogBuilder t(CharSequence charSequence) {
        this.f19468m = !TextUtils.isEmpty(charSequence);
        super.t(charSequence);
        return this;
    }

    public void l0(Configuration configuration) {
        if (this.G == S(configuration) || this.f19458c == null) {
            return;
        }
        k0(configuration);
    }

    public void m0() {
        AlertDialog alertDialog = this.f19458c;
        if (alertDialog == null) {
            return;
        }
        O(alertDialog.getWindow());
        M();
        K(this.f19458c.getWindow());
        L(this.f19458c.getWindow());
        I(this.f19458c.getWindow());
        H(this.f19458c.getWindow());
        N(this.f19458c.getWindow());
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    public AlertDialog.a u(int i10) {
        this.f19474s = true;
        return super.u(i10);
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    public AlertDialog.a v(View view) {
        this.f19474s = true;
        return super.v(view);
    }

    @Override // androidx.appcompat.app.AlertDialog.a
    public AlertDialog w() {
        AlertDialog w10 = super.w();
        D(w10);
        m0();
        return w10;
    }

    public COUIAlertDialogBuilder(Context context, int i10) {
        super(new ContextThemeWrapper(context, i10));
        this.f19468m = false;
        this.f19469n = false;
        this.f19470o = false;
        this.f19471p = false;
        this.f19472q = false;
        this.f19473r = null;
        this.f19474s = false;
        this.f19475t = 0;
        this.f19476u = null;
        this.f19477v = false;
        this.f19478w = null;
        this.f19480y = null;
        this.f19481z = null;
        this.A = -1;
        this.C = true;
        this.D = false;
        this.F = true;
        this.H = false;
        this.I = new d();
        this.E = i10;
        F();
    }

    public COUIAlertDialogBuilder(Context context, int i10, int i11) {
        super(n0(context, i10, i11));
        this.f19468m = false;
        this.f19469n = false;
        this.f19470o = false;
        this.f19471p = false;
        this.f19472q = false;
        this.f19473r = null;
        this.f19474s = false;
        this.f19475t = 0;
        this.f19476u = null;
        this.f19477v = false;
        this.f19478w = null;
        this.f19480y = null;
        this.f19481z = null;
        this.A = -1;
        this.C = true;
        this.D = false;
        this.F = true;
        this.H = false;
        this.I = new d();
        F();
    }
}
