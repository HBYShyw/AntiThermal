package androidx.appcompat.widget;

import android.R;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.R$dimen;
import androidx.appcompat.R$id;
import androidx.appcompat.R$layout;
import androidx.appcompat.R$string;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.ActionProvider;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

/* loaded from: classes.dex */
public class ActivityChooserView extends ViewGroup {

    /* renamed from: e, reason: collision with root package name */
    final f f908e;

    /* renamed from: f, reason: collision with root package name */
    private final g f909f;

    /* renamed from: g, reason: collision with root package name */
    private final View f910g;

    /* renamed from: h, reason: collision with root package name */
    private final Drawable f911h;

    /* renamed from: i, reason: collision with root package name */
    final FrameLayout f912i;

    /* renamed from: j, reason: collision with root package name */
    private final ImageView f913j;

    /* renamed from: k, reason: collision with root package name */
    final FrameLayout f914k;

    /* renamed from: l, reason: collision with root package name */
    private final ImageView f915l;

    /* renamed from: m, reason: collision with root package name */
    private final int f916m;

    /* renamed from: n, reason: collision with root package name */
    ActionProvider f917n;

    /* renamed from: o, reason: collision with root package name */
    final DataSetObserver f918o;

    /* renamed from: p, reason: collision with root package name */
    private final ViewTreeObserver.OnGlobalLayoutListener f919p;

    /* renamed from: q, reason: collision with root package name */
    private ListPopupWindow f920q;

    /* renamed from: r, reason: collision with root package name */
    PopupWindow.OnDismissListener f921r;

    /* renamed from: s, reason: collision with root package name */
    boolean f922s;

    /* renamed from: t, reason: collision with root package name */
    int f923t;

    /* renamed from: u, reason: collision with root package name */
    private boolean f924u;

    /* renamed from: v, reason: collision with root package name */
    private int f925v;

    /* loaded from: classes.dex */
    public static class InnerLayout extends LinearLayout {

        /* renamed from: e, reason: collision with root package name */
        private static final int[] f926e = {R.attr.background};

        public InnerLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TintTypedArray v7 = TintTypedArray.v(context, attributeSet, f926e);
            setBackgroundDrawable(v7.g(0));
            v7.x();
        }
    }

    /* loaded from: classes.dex */
    class a extends DataSetObserver {
        a() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            super.onChanged();
            ActivityChooserView.this.f908e.notifyDataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            super.onInvalidated();
            ActivityChooserView.this.f908e.notifyDataSetInvalidated();
        }
    }

    /* loaded from: classes.dex */
    class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (ActivityChooserView.this.b()) {
                if (!ActivityChooserView.this.isShown()) {
                    ActivityChooserView.this.getListPopupWindow().dismiss();
                    return;
                }
                ActivityChooserView.this.getListPopupWindow().b();
                ActionProvider actionProvider = ActivityChooserView.this.f917n;
                if (actionProvider != null) {
                    actionProvider.k(true);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    class c extends View.AccessibilityDelegate {
        c() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            AccessibilityNodeInfoCompat.C0(accessibilityNodeInfo).S(true);
        }
    }

    /* loaded from: classes.dex */
    class d extends ForwardingListener {
        d(View view) {
            super(view);
        }

        @Override // androidx.appcompat.widget.ForwardingListener
        public ShowableListMenu b() {
            return ActivityChooserView.this.getListPopupWindow();
        }

        @Override // androidx.appcompat.widget.ForwardingListener
        protected boolean c() {
            ActivityChooserView.this.c();
            return true;
        }

        @Override // androidx.appcompat.widget.ForwardingListener
        protected boolean d() {
            ActivityChooserView.this.a();
            return true;
        }
    }

    /* loaded from: classes.dex */
    class e extends DataSetObserver {
        e() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            super.onChanged();
            ActivityChooserView.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class f extends BaseAdapter {

        /* renamed from: e, reason: collision with root package name */
        private int f932e = 4;

        /* renamed from: f, reason: collision with root package name */
        private boolean f933f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f934g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f935h;

        f() {
        }

        public int a() {
            throw null;
        }

        public ActivityChooserModel b() {
            return null;
        }

        public ResolveInfo c() {
            throw null;
        }

        public int d() {
            throw null;
        }

        public boolean e() {
            return this.f933f;
        }

        public void f(ActivityChooserModel activityChooserModel) {
            ActivityChooserView.this.f908e.b();
            notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            throw null;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i10) {
            int itemViewType = getItemViewType(i10);
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    return null;
                }
                throw new IllegalArgumentException();
            }
            if (this.f933f) {
                throw null;
            }
            throw null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i10) {
            return (this.f935h && i10 == getCount() - 1) ? 1 : 0;
        }

        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            int itemViewType = getItemViewType(i10);
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    if (view != null && view.getId() == 1) {
                        return view;
                    }
                    View inflate = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(R$layout.abc_activity_chooser_view_list_item, viewGroup, false);
                    inflate.setId(1);
                    ((TextView) inflate.findViewById(R$id.title)).setText(ActivityChooserView.this.getContext().getString(R$string.abc_activity_chooser_view_see_all));
                    return inflate;
                }
                throw new IllegalArgumentException();
            }
            if (view == null || view.getId() != R$id.list_item) {
                view = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(R$layout.abc_activity_chooser_view_list_item, viewGroup, false);
            }
            PackageManager packageManager = ActivityChooserView.this.getContext().getPackageManager();
            ImageView imageView = (ImageView) view.findViewById(R$id.icon);
            ResolveInfo resolveInfo = (ResolveInfo) getItem(i10);
            imageView.setImageDrawable(resolveInfo.loadIcon(packageManager));
            ((TextView) view.findViewById(R$id.title)).setText(resolveInfo.loadLabel(packageManager));
            if (this.f933f && i10 == 0 && this.f934g) {
                view.setActivated(true);
            } else {
                view.setActivated(false);
            }
            return view;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class g implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, PopupWindow.OnDismissListener {
        g() {
        }

        private void a() {
            PopupWindow.OnDismissListener onDismissListener = ActivityChooserView.this.f921r;
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (view != activityChooserView.f914k) {
                if (view == activityChooserView.f912i) {
                    activityChooserView.f922s = false;
                    activityChooserView.d(activityChooserView.f923t);
                    return;
                }
                throw new IllegalArgumentException();
            }
            activityChooserView.a();
            ActivityChooserView.this.f908e.c();
            ActivityChooserView.this.f908e.b();
            throw null;
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            a();
            ActionProvider actionProvider = ActivityChooserView.this.f917n;
            if (actionProvider != null) {
                actionProvider.k(false);
            }
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
            int itemViewType = ((f) adapterView.getAdapter()).getItemViewType(i10);
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    ActivityChooserView.this.d(Integer.MAX_VALUE);
                    return;
                }
                throw new IllegalArgumentException();
            }
            ActivityChooserView.this.a();
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (!activityChooserView.f922s) {
                activityChooserView.f908e.e();
                ActivityChooserView.this.f908e.b();
                throw null;
            }
            if (i10 <= 0) {
                return;
            }
            activityChooserView.f908e.b();
            throw null;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            ActivityChooserView activityChooserView = ActivityChooserView.this;
            if (view == activityChooserView.f914k) {
                if (activityChooserView.f908e.getCount() > 0) {
                    ActivityChooserView activityChooserView2 = ActivityChooserView.this;
                    activityChooserView2.f922s = true;
                    activityChooserView2.d(activityChooserView2.f923t);
                }
                return true;
            }
            throw new IllegalArgumentException();
        }
    }

    public ActivityChooserView(Context context) {
        this(context, null);
    }

    public boolean a() {
        if (!b()) {
            return true;
        }
        getListPopupWindow().dismiss();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (!viewTreeObserver.isAlive()) {
            return true;
        }
        viewTreeObserver.removeGlobalOnLayoutListener(this.f919p);
        return true;
    }

    public boolean b() {
        return getListPopupWindow().a();
    }

    public boolean c() {
        if (b() || !this.f924u) {
            return false;
        }
        this.f922s = false;
        d(this.f923t);
        return true;
    }

    void d(int i10) {
        this.f908e.b();
        throw new IllegalStateException("No data model. Did you call #setDataModel?");
    }

    void e() {
        if (this.f908e.getCount() > 0) {
            this.f912i.setEnabled(true);
        } else {
            this.f912i.setEnabled(false);
        }
        int a10 = this.f908e.a();
        int d10 = this.f908e.d();
        if (a10 != 1 && (a10 <= 1 || d10 <= 0)) {
            this.f914k.setVisibility(8);
        } else {
            this.f914k.setVisibility(0);
            ResolveInfo c10 = this.f908e.c();
            PackageManager packageManager = getContext().getPackageManager();
            this.f915l.setImageDrawable(c10.loadIcon(packageManager));
            if (this.f925v != 0) {
                this.f914k.setContentDescription(getContext().getString(this.f925v, c10.loadLabel(packageManager)));
            }
        }
        if (this.f914k.getVisibility() == 0) {
            this.f910g.setBackgroundDrawable(this.f911h);
        } else {
            this.f910g.setBackgroundDrawable(null);
        }
    }

    public ActivityChooserModel getDataModel() {
        this.f908e.b();
        return null;
    }

    ListPopupWindow getListPopupWindow() {
        if (this.f920q == null) {
            ListPopupWindow listPopupWindow = new ListPopupWindow(getContext());
            this.f920q = listPopupWindow;
            listPopupWindow.p(this.f908e);
            this.f920q.D(this);
            this.f920q.J(true);
            this.f920q.L(this.f909f);
            this.f920q.K(this.f909f);
        }
        return this.f920q;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f908e.b();
        this.f924u = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f908e.b();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this.f919p);
        }
        if (b()) {
            a();
        }
        this.f924u = false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z10, int i10, int i11, int i12, int i13) {
        this.f910g.layout(0, 0, i12 - i10, i13 - i11);
        if (b()) {
            return;
        }
        a();
    }

    @Override // android.view.View
    protected void onMeasure(int i10, int i11) {
        View view = this.f910g;
        if (this.f914k.getVisibility() != 0) {
            i11 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i11), 1073741824);
        }
        measureChild(view, i10, i11);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public void setActivityChooserModel(ActivityChooserModel activityChooserModel) {
        this.f908e.f(activityChooserModel);
        if (b()) {
            a();
            c();
        }
    }

    public void setDefaultActionButtonContentDescription(int i10) {
        this.f925v = i10;
    }

    public void setExpandActivityOverflowButtonContentDescription(int i10) {
        this.f913j.setContentDescription(getContext().getString(i10));
    }

    public void setExpandActivityOverflowButtonDrawable(Drawable drawable) {
        this.f913j.setImageDrawable(drawable);
    }

    public void setInitialActivityCount(int i10) {
        this.f923t = i10;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.f921r = onDismissListener;
    }

    public void setProvider(ActionProvider actionProvider) {
        this.f917n = actionProvider;
    }

    public ActivityChooserView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActivityChooserView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.f918o = new a();
        this.f919p = new b();
        this.f923t = 4;
        int[] iArr = R$styleable.ActivityChooserView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i10, 0);
        ViewCompat.j0(this, context, iArr, attributeSet, obtainStyledAttributes, i10, 0);
        this.f923t = obtainStyledAttributes.getInt(R$styleable.ActivityChooserView_initialActivityCount, 4);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ActivityChooserView_expandActivityOverflowButtonDrawable);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(getContext()).inflate(R$layout.abc_activity_chooser_view, (ViewGroup) this, true);
        g gVar = new g();
        this.f909f = gVar;
        View findViewById = findViewById(R$id.activity_chooser_view_content);
        this.f910g = findViewById;
        this.f911h = findViewById.getBackground();
        FrameLayout frameLayout = (FrameLayout) findViewById(R$id.default_activity_button);
        this.f914k = frameLayout;
        frameLayout.setOnClickListener(gVar);
        frameLayout.setOnLongClickListener(gVar);
        int i11 = R$id.image;
        this.f915l = (ImageView) frameLayout.findViewById(i11);
        FrameLayout frameLayout2 = (FrameLayout) findViewById(R$id.expand_activities_button);
        frameLayout2.setOnClickListener(gVar);
        frameLayout2.setAccessibilityDelegate(new c());
        frameLayout2.setOnTouchListener(new d(frameLayout2));
        this.f912i = frameLayout2;
        ImageView imageView = (ImageView) frameLayout2.findViewById(i11);
        this.f913j = imageView;
        imageView.setImageDrawable(drawable);
        f fVar = new f();
        this.f908e = fVar;
        fVar.registerDataSetObserver(new e());
        Resources resources = context.getResources();
        this.f916m = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R$dimen.abc_config_prefDialogWidth));
    }
}
