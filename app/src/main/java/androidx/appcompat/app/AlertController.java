package androidx.appcompat.app;

import android.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$id;
import androidx.appcompat.R$styleable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import java.lang.ref.WeakReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AlertController {
    NestedScrollView A;
    private Drawable C;
    private ImageView D;
    private TextView E;
    private TextView F;
    private View G;
    ListAdapter H;
    private int J;
    private int K;
    int L;
    int M;
    int N;
    int O;
    private boolean P;
    Handler R;

    /* renamed from: a, reason: collision with root package name */
    private final Context f321a;

    /* renamed from: b, reason: collision with root package name */
    final AppCompatDialog f322b;

    /* renamed from: c, reason: collision with root package name */
    private final Window f323c;

    /* renamed from: d, reason: collision with root package name */
    private final int f324d;

    /* renamed from: e, reason: collision with root package name */
    private CharSequence f325e;

    /* renamed from: f, reason: collision with root package name */
    private CharSequence f326f;

    /* renamed from: g, reason: collision with root package name */
    ListView f327g;

    /* renamed from: h, reason: collision with root package name */
    private View f328h;

    /* renamed from: i, reason: collision with root package name */
    private int f329i;

    /* renamed from: j, reason: collision with root package name */
    private int f330j;

    /* renamed from: k, reason: collision with root package name */
    private int f331k;

    /* renamed from: l, reason: collision with root package name */
    private int f332l;

    /* renamed from: m, reason: collision with root package name */
    private int f333m;

    /* renamed from: o, reason: collision with root package name */
    Button f335o;

    /* renamed from: p, reason: collision with root package name */
    private CharSequence f336p;

    /* renamed from: q, reason: collision with root package name */
    Message f337q;

    /* renamed from: r, reason: collision with root package name */
    private Drawable f338r;

    /* renamed from: s, reason: collision with root package name */
    Button f339s;

    /* renamed from: t, reason: collision with root package name */
    private CharSequence f340t;

    /* renamed from: u, reason: collision with root package name */
    Message f341u;

    /* renamed from: v, reason: collision with root package name */
    private Drawable f342v;

    /* renamed from: w, reason: collision with root package name */
    Button f343w;

    /* renamed from: x, reason: collision with root package name */
    private CharSequence f344x;

    /* renamed from: y, reason: collision with root package name */
    Message f345y;

    /* renamed from: z, reason: collision with root package name */
    private Drawable f346z;

    /* renamed from: n, reason: collision with root package name */
    private boolean f334n = false;
    private int B = 0;
    int I = -1;
    private int Q = 0;
    private final View.OnClickListener S = new a();

    /* loaded from: classes.dex */
    public static class RecycleListView extends ListView {

        /* renamed from: e, reason: collision with root package name */
        private final int f347e;

        /* renamed from: f, reason: collision with root package name */
        private final int f348f;

        public RecycleListView(Context context) {
            this(context, null);
        }

        public void a(boolean z10, boolean z11) {
            if (z11 && z10) {
                return;
            }
            setPadding(getPaddingLeft(), z10 ? getPaddingTop() : this.f347e, getPaddingRight(), z11 ? getPaddingBottom() : this.f348f);
        }

        public RecycleListView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RecycleListView);
            this.f348f = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.RecycleListView_paddingBottomNoButtons, -1);
            this.f347e = obtainStyledAttributes.getDimensionPixelOffset(R$styleable.RecycleListView_paddingTopNoTitle, -1);
        }
    }

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Message obtain;
            Message message;
            Message message2;
            Message message3;
            AlertController alertController = AlertController.this;
            if (view == alertController.f335o && (message3 = alertController.f337q) != null) {
                obtain = Message.obtain(message3);
            } else if (view == alertController.f339s && (message2 = alertController.f341u) != null) {
                obtain = Message.obtain(message2);
            } else {
                obtain = (view != alertController.f343w || (message = alertController.f345y) == null) ? null : Message.obtain(message);
            }
            if (obtain != null) {
                obtain.sendToTarget();
            }
            AlertController alertController2 = AlertController.this;
            alertController2.R.obtainMessage(1, alertController2.f322b).sendToTarget();
        }
    }

    /* loaded from: classes.dex */
    public static class b {
        public int A;
        public int B;
        public int C;
        public int D;
        public boolean[] F;
        public boolean G;
        public boolean H;
        public DialogInterface.OnMultiChoiceClickListener J;
        public Cursor K;
        public String L;
        public String M;
        public AdapterView.OnItemSelectedListener N;
        public e O;

        /* renamed from: a, reason: collision with root package name */
        public final Context f350a;

        /* renamed from: b, reason: collision with root package name */
        public final LayoutInflater f351b;

        /* renamed from: d, reason: collision with root package name */
        public Drawable f353d;

        /* renamed from: f, reason: collision with root package name */
        public CharSequence f355f;

        /* renamed from: g, reason: collision with root package name */
        public View f356g;

        /* renamed from: h, reason: collision with root package name */
        public CharSequence f357h;

        /* renamed from: i, reason: collision with root package name */
        public CharSequence f358i;

        /* renamed from: j, reason: collision with root package name */
        public Drawable f359j;

        /* renamed from: k, reason: collision with root package name */
        public DialogInterface.OnClickListener f360k;

        /* renamed from: l, reason: collision with root package name */
        public CharSequence f361l;

        /* renamed from: m, reason: collision with root package name */
        public Drawable f362m;

        /* renamed from: n, reason: collision with root package name */
        public DialogInterface.OnClickListener f363n;

        /* renamed from: o, reason: collision with root package name */
        public CharSequence f364o;

        /* renamed from: p, reason: collision with root package name */
        public Drawable f365p;

        /* renamed from: q, reason: collision with root package name */
        public DialogInterface.OnClickListener f366q;

        /* renamed from: s, reason: collision with root package name */
        public DialogInterface.OnCancelListener f368s;

        /* renamed from: t, reason: collision with root package name */
        public DialogInterface.OnDismissListener f369t;

        /* renamed from: u, reason: collision with root package name */
        public DialogInterface.OnKeyListener f370u;

        /* renamed from: v, reason: collision with root package name */
        public CharSequence[] f371v;

        /* renamed from: w, reason: collision with root package name */
        public ListAdapter f372w;

        /* renamed from: x, reason: collision with root package name */
        public DialogInterface.OnClickListener f373x;

        /* renamed from: y, reason: collision with root package name */
        public int f374y;

        /* renamed from: z, reason: collision with root package name */
        public View f375z;

        /* renamed from: c, reason: collision with root package name */
        public int f352c = 0;

        /* renamed from: e, reason: collision with root package name */
        public int f354e = 0;
        public boolean E = false;
        public int I = -1;
        public boolean P = true;

        /* renamed from: r, reason: collision with root package name */
        public boolean f367r = true;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class a extends ArrayAdapter<CharSequence> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ RecycleListView f376e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(Context context, int i10, int i11, CharSequence[] charSequenceArr, RecycleListView recycleListView) {
                super(context, i10, i11, charSequenceArr);
                this.f376e = recycleListView;
            }

            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public View getView(int i10, View view, ViewGroup viewGroup) {
                View view2 = super.getView(i10, view, viewGroup);
                boolean[] zArr = b.this.F;
                if (zArr != null && zArr[i10]) {
                    this.f376e.setItemChecked(i10, true);
                }
                return view2;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: androidx.appcompat.app.AlertController$b$b, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public class C0001b extends CursorAdapter {

            /* renamed from: e, reason: collision with root package name */
            private final int f378e;

            /* renamed from: f, reason: collision with root package name */
            private final int f379f;

            /* renamed from: g, reason: collision with root package name */
            final /* synthetic */ RecycleListView f380g;

            /* renamed from: h, reason: collision with root package name */
            final /* synthetic */ AlertController f381h;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            C0001b(Context context, Cursor cursor, boolean z10, RecycleListView recycleListView, AlertController alertController) {
                super(context, cursor, z10);
                this.f380g = recycleListView;
                this.f381h = alertController;
                Cursor cursor2 = getCursor();
                this.f378e = cursor2.getColumnIndexOrThrow(b.this.L);
                this.f379f = cursor2.getColumnIndexOrThrow(b.this.M);
            }

            @Override // android.widget.CursorAdapter
            public void bindView(View view, Context context, Cursor cursor) {
                ((CheckedTextView) view.findViewById(R.id.text1)).setText(cursor.getString(this.f378e));
                this.f380g.setItemChecked(cursor.getPosition(), cursor.getInt(this.f379f) == 1);
            }

            @Override // android.widget.CursorAdapter
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return b.this.f351b.inflate(this.f381h.M, viewGroup, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class c implements AdapterView.OnItemClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ AlertController f383e;

            c(AlertController alertController) {
                this.f383e = alertController;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
                b.this.f373x.onClick(this.f383e.f322b, i10);
                if (b.this.H) {
                    return;
                }
                this.f383e.f322b.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class d implements AdapterView.OnItemClickListener {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ RecycleListView f385e;

            /* renamed from: f, reason: collision with root package name */
            final /* synthetic */ AlertController f386f;

            d(RecycleListView recycleListView, AlertController alertController) {
                this.f385e = recycleListView;
                this.f386f = alertController;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
                boolean[] zArr = b.this.F;
                if (zArr != null) {
                    zArr[i10] = this.f385e.isItemChecked(i10);
                }
                b.this.J.onClick(this.f386f.f322b, i10, this.f385e.isItemChecked(i10));
            }
        }

        /* loaded from: classes.dex */
        public interface e {
            void a(ListView listView);
        }

        public b(Context context) {
            this.f350a = context;
            this.f351b = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        private void b(AlertController alertController) {
            int i10;
            ListAdapter listAdapter;
            RecycleListView recycleListView = (RecycleListView) this.f351b.inflate(alertController.L, (ViewGroup) null);
            if (this.G) {
                if (this.K == null) {
                    listAdapter = new a(this.f350a, alertController.M, R.id.text1, this.f371v, recycleListView);
                } else {
                    listAdapter = new C0001b(this.f350a, this.K, false, recycleListView, alertController);
                }
            } else {
                if (this.H) {
                    i10 = alertController.N;
                } else {
                    i10 = alertController.O;
                }
                int i11 = i10;
                if (this.K != null) {
                    listAdapter = new SimpleCursorAdapter(this.f350a, i11, this.K, new String[]{this.L}, new int[]{R.id.text1});
                } else {
                    listAdapter = this.f372w;
                    if (listAdapter == null) {
                        listAdapter = new d(this.f350a, i11, R.id.text1, this.f371v);
                    }
                }
            }
            e eVar = this.O;
            if (eVar != null) {
                eVar.a(recycleListView);
            }
            alertController.H = listAdapter;
            alertController.I = this.I;
            if (this.f373x != null) {
                recycleListView.setOnItemClickListener(new c(alertController));
            } else if (this.J != null) {
                recycleListView.setOnItemClickListener(new d(recycleListView, alertController));
            }
            AdapterView.OnItemSelectedListener onItemSelectedListener = this.N;
            if (onItemSelectedListener != null) {
                recycleListView.setOnItemSelectedListener(onItemSelectedListener);
            }
            if (this.H) {
                recycleListView.setChoiceMode(1);
            } else if (this.G) {
                recycleListView.setChoiceMode(2);
            }
            alertController.f327g = recycleListView;
        }

        public void a(AlertController alertController) {
            View view = this.f356g;
            if (view != null) {
                alertController.l(view);
            } else {
                CharSequence charSequence = this.f355f;
                if (charSequence != null) {
                    alertController.q(charSequence);
                }
                Drawable drawable = this.f353d;
                if (drawable != null) {
                    alertController.n(drawable);
                }
                int i10 = this.f352c;
                if (i10 != 0) {
                    alertController.m(i10);
                }
                int i11 = this.f354e;
                if (i11 != 0) {
                    alertController.m(alertController.d(i11));
                }
            }
            CharSequence charSequence2 = this.f357h;
            if (charSequence2 != null) {
                alertController.o(charSequence2);
            }
            CharSequence charSequence3 = this.f358i;
            if (charSequence3 != null || this.f359j != null) {
                alertController.k(-1, charSequence3, this.f360k, null, this.f359j);
            }
            CharSequence charSequence4 = this.f361l;
            if (charSequence4 != null || this.f362m != null) {
                alertController.k(-2, charSequence4, this.f363n, null, this.f362m);
            }
            CharSequence charSequence5 = this.f364o;
            if (charSequence5 != null || this.f365p != null) {
                alertController.k(-3, charSequence5, this.f366q, null, this.f365p);
            }
            if (this.f371v != null || this.K != null || this.f372w != null) {
                b(alertController);
            }
            View view2 = this.f375z;
            if (view2 != null) {
                if (this.E) {
                    alertController.t(view2, this.A, this.B, this.C, this.D);
                    return;
                } else {
                    alertController.s(view2);
                    return;
                }
            }
            int i12 = this.f374y;
            if (i12 != 0) {
                alertController.r(i12);
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class c extends Handler {

        /* renamed from: a, reason: collision with root package name */
        private WeakReference<DialogInterface> f388a;

        public c(DialogInterface dialogInterface) {
            this.f388a = new WeakReference<>(dialogInterface);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i10 = message.what;
            if (i10 == -3 || i10 == -2 || i10 == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick(this.f388a.get(), message.what);
            } else {
                if (i10 != 1) {
                    return;
                }
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d extends ArrayAdapter<CharSequence> {
        public d(Context context, int i10, int i11, CharSequence[] charSequenceArr) {
            super(context, i10, i11, charSequenceArr);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }
    }

    public AlertController(Context context, AppCompatDialog appCompatDialog, Window window) {
        this.f321a = context;
        this.f322b = appCompatDialog;
        this.f323c = window;
        this.R = new c(appCompatDialog);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, R$styleable.AlertDialog, R$attr.alertDialogStyle, 0);
        this.J = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_android_layout, 0);
        this.K = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_buttonPanelSideLayout, 0);
        this.L = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_listLayout, 0);
        this.M = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_multiChoiceItemLayout, 0);
        this.N = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_singleChoiceItemLayout, 0);
        this.O = obtainStyledAttributes.getResourceId(R$styleable.AlertDialog_listItemLayout, 0);
        this.P = obtainStyledAttributes.getBoolean(R$styleable.AlertDialog_showTitle, true);
        this.f324d = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AlertDialog_buttonIconDimen, 0);
        obtainStyledAttributes.recycle();
        appCompatDialog.h(1);
    }

    static boolean a(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int childCount = viewGroup.getChildCount();
        while (childCount > 0) {
            childCount--;
            if (a(viewGroup.getChildAt(childCount))) {
                return true;
            }
        }
        return false;
    }

    private void b(Button button) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams(layoutParams);
    }

    private ViewGroup i(View view, View view2) {
        if (view == null) {
            if (view2 instanceof ViewStub) {
                view2 = ((ViewStub) view2).inflate();
            }
            return (ViewGroup) view2;
        }
        if (view2 != null) {
            ViewParent parent = view2.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view2);
            }
        }
        if (view instanceof ViewStub) {
            view = ((ViewStub) view).inflate();
        }
        return (ViewGroup) view;
    }

    private int j() {
        int i10 = this.K;
        if (i10 == 0) {
            return this.J;
        }
        return this.Q == 1 ? i10 : this.J;
    }

    private void p(ViewGroup viewGroup, View view, int i10, int i11) {
        View findViewById = this.f323c.findViewById(R$id.scrollIndicatorUp);
        View findViewById2 = this.f323c.findViewById(R$id.scrollIndicatorDown);
        ViewCompat.C0(view, i10, i11);
        if (findViewById != null) {
            viewGroup.removeView(findViewById);
        }
        if (findViewById2 != null) {
            viewGroup.removeView(findViewById2);
        }
    }

    private void u(ViewGroup viewGroup) {
        int i10;
        Button button = (Button) viewGroup.findViewById(R.id.button1);
        this.f335o = button;
        button.setOnClickListener(this.S);
        if (TextUtils.isEmpty(this.f336p) && this.f338r == null) {
            this.f335o.setVisibility(8);
            i10 = 0;
        } else {
            this.f335o.setText(this.f336p);
            Drawable drawable = this.f338r;
            if (drawable != null) {
                int i11 = this.f324d;
                drawable.setBounds(0, 0, i11, i11);
                this.f335o.setCompoundDrawables(this.f338r, null, null, null);
            }
            this.f335o.setVisibility(0);
            i10 = 1;
        }
        Button button2 = (Button) viewGroup.findViewById(R.id.button2);
        this.f339s = button2;
        button2.setOnClickListener(this.S);
        if (TextUtils.isEmpty(this.f340t) && this.f342v == null) {
            this.f339s.setVisibility(8);
        } else {
            this.f339s.setText(this.f340t);
            Drawable drawable2 = this.f342v;
            if (drawable2 != null) {
                int i12 = this.f324d;
                drawable2.setBounds(0, 0, i12, i12);
                this.f339s.setCompoundDrawables(this.f342v, null, null, null);
            }
            this.f339s.setVisibility(0);
            i10 |= 2;
        }
        Button button3 = (Button) viewGroup.findViewById(R.id.button3);
        this.f343w = button3;
        button3.setOnClickListener(this.S);
        if (TextUtils.isEmpty(this.f344x) && this.f346z == null) {
            this.f343w.setVisibility(8);
        } else {
            this.f343w.setText(this.f344x);
            Drawable drawable3 = this.f346z;
            if (drawable3 != null) {
                int i13 = this.f324d;
                drawable3.setBounds(0, 0, i13, i13);
                this.f343w.setCompoundDrawables(this.f346z, null, null, null);
            }
            this.f343w.setVisibility(0);
            i10 |= 4;
        }
        if (z(this.f321a)) {
            if (i10 == 1) {
                b(this.f335o);
            } else if (i10 == 2) {
                b(this.f339s);
            } else if (i10 == 4) {
                b(this.f343w);
            }
        }
        if (i10 != 0) {
            return;
        }
        viewGroup.setVisibility(8);
    }

    private void v(ViewGroup viewGroup) {
        NestedScrollView nestedScrollView = (NestedScrollView) this.f323c.findViewById(R$id.scrollView);
        this.A = nestedScrollView;
        nestedScrollView.setFocusable(false);
        this.A.setNestedScrollingEnabled(false);
        TextView textView = (TextView) viewGroup.findViewById(R.id.message);
        this.F = textView;
        if (textView == null) {
            return;
        }
        CharSequence charSequence = this.f326f;
        if (charSequence != null) {
            textView.setText(charSequence);
            return;
        }
        textView.setVisibility(8);
        this.A.removeView(this.F);
        if (this.f327g != null) {
            ViewGroup viewGroup2 = (ViewGroup) this.A.getParent();
            int indexOfChild = viewGroup2.indexOfChild(this.A);
            viewGroup2.removeViewAt(indexOfChild);
            viewGroup2.addView(this.f327g, indexOfChild, new ViewGroup.LayoutParams(-1, -1));
            return;
        }
        viewGroup.setVisibility(8);
    }

    private void w(ViewGroup viewGroup) {
        View view = this.f328h;
        if (view == null) {
            view = this.f329i != 0 ? LayoutInflater.from(this.f321a).inflate(this.f329i, viewGroup, false) : null;
        }
        boolean z10 = view != null;
        if (!z10 || !a(view)) {
            this.f323c.setFlags(131072, 131072);
        }
        if (z10) {
            FrameLayout frameLayout = (FrameLayout) this.f323c.findViewById(R$id.custom);
            frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -1));
            if (this.f334n) {
                frameLayout.setPadding(this.f330j, this.f331k, this.f332l, this.f333m);
            }
            if (this.f327g != null) {
                ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) viewGroup.getLayoutParams())).weight = 0.0f;
                return;
            }
            return;
        }
        viewGroup.setVisibility(8);
    }

    private void x(ViewGroup viewGroup) {
        if (this.G != null) {
            viewGroup.addView(this.G, 0, new ViewGroup.LayoutParams(-1, -2));
            this.f323c.findViewById(R$id.title_template).setVisibility(8);
            return;
        }
        this.D = (ImageView) this.f323c.findViewById(R.id.icon);
        if ((!TextUtils.isEmpty(this.f325e)) && this.P) {
            TextView textView = (TextView) this.f323c.findViewById(R$id.alertTitle);
            this.E = textView;
            textView.setText(this.f325e);
            int i10 = this.B;
            if (i10 != 0) {
                this.D.setImageResource(i10);
                return;
            }
            Drawable drawable = this.C;
            if (drawable != null) {
                this.D.setImageDrawable(drawable);
                return;
            } else {
                this.E.setPadding(this.D.getPaddingLeft(), this.D.getPaddingTop(), this.D.getPaddingRight(), this.D.getPaddingBottom());
                this.D.setVisibility(8);
                return;
            }
        }
        this.f323c.findViewById(R$id.title_template).setVisibility(8);
        this.D.setVisibility(8);
        viewGroup.setVisibility(8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void y() {
        View findViewById;
        ListAdapter listAdapter;
        View findViewById2;
        View findViewById3 = this.f323c.findViewById(R$id.parentPanel);
        int i10 = R$id.topPanel;
        View findViewById4 = findViewById3.findViewById(i10);
        int i11 = R$id.contentPanel;
        View findViewById5 = findViewById3.findViewById(i11);
        int i12 = R$id.buttonPanel;
        View findViewById6 = findViewById3.findViewById(i12);
        ViewGroup viewGroup = (ViewGroup) findViewById3.findViewById(R$id.customPanel);
        w(viewGroup);
        View findViewById7 = viewGroup.findViewById(i10);
        View findViewById8 = viewGroup.findViewById(i11);
        View findViewById9 = viewGroup.findViewById(i12);
        ViewGroup i13 = i(findViewById7, findViewById4);
        ViewGroup i14 = i(findViewById8, findViewById5);
        ViewGroup i15 = i(findViewById9, findViewById6);
        v(i14);
        u(i15);
        x(i13);
        boolean z10 = viewGroup.getVisibility() != 8;
        boolean z11 = (i13 == null || i13.getVisibility() == 8) ? 0 : 1;
        boolean z12 = (i15 == null || i15.getVisibility() == 8) ? false : true;
        if (!z12 && i14 != null && (findViewById2 = i14.findViewById(R$id.textSpacerNoButtons)) != null) {
            findViewById2.setVisibility(0);
        }
        if (z11 != 0) {
            NestedScrollView nestedScrollView = this.A;
            if (nestedScrollView != null) {
                nestedScrollView.setClipToPadding(true);
            }
            View findViewById10 = (this.f326f == null && this.f327g == null) ? null : i13.findViewById(R$id.titleDividerNoCustom);
            if (findViewById10 != null) {
                findViewById10.setVisibility(0);
            }
        } else if (i14 != null && (findViewById = i14.findViewById(R$id.textSpacerNoTitle)) != null) {
            findViewById.setVisibility(0);
        }
        ListView listView = this.f327g;
        if (listView instanceof RecycleListView) {
            ((RecycleListView) listView).a(z11, z12);
        }
        if (!z10) {
            View view = this.f327g;
            if (view == null) {
                view = this.A;
            }
            if (view != null) {
                p(i14, view, z11 | (z12 ? 2 : 0), 3);
            }
        }
        ListView listView2 = this.f327g;
        if (listView2 == null || (listAdapter = this.H) == null) {
            return;
        }
        listView2.setAdapter(listAdapter);
        int i16 = this.I;
        if (i16 > -1) {
            listView2.setItemChecked(i16, true);
            listView2.setSelection(i16);
        }
    }

    private static boolean z(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R$attr.alertDialogCenterButtons, typedValue, true);
        return typedValue.data != 0;
    }

    public Button c(int i10) {
        if (i10 == -3) {
            return this.f343w;
        }
        if (i10 == -2) {
            return this.f339s;
        }
        if (i10 != -1) {
            return null;
        }
        return this.f335o;
    }

    public int d(int i10) {
        TypedValue typedValue = new TypedValue();
        this.f321a.getTheme().resolveAttribute(i10, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView e() {
        return this.f327g;
    }

    public void f() {
        this.f322b.setContentView(j());
        y();
    }

    public boolean g(int i10, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.A;
        return nestedScrollView != null && nestedScrollView.s(keyEvent);
    }

    public boolean h(int i10, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.A;
        return nestedScrollView != null && nestedScrollView.s(keyEvent);
    }

    public void k(int i10, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message, Drawable drawable) {
        if (message == null && onClickListener != null) {
            message = this.R.obtainMessage(i10, onClickListener);
        }
        if (i10 == -3) {
            this.f344x = charSequence;
            this.f345y = message;
            this.f346z = drawable;
        } else if (i10 == -2) {
            this.f340t = charSequence;
            this.f341u = message;
            this.f342v = drawable;
        } else {
            if (i10 == -1) {
                this.f336p = charSequence;
                this.f337q = message;
                this.f338r = drawable;
                return;
            }
            throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void l(View view) {
        this.G = view;
    }

    public void m(int i10) {
        this.C = null;
        this.B = i10;
        ImageView imageView = this.D;
        if (imageView != null) {
            if (i10 != 0) {
                imageView.setVisibility(0);
                this.D.setImageResource(this.B);
            } else {
                imageView.setVisibility(8);
            }
        }
    }

    public void n(Drawable drawable) {
        this.C = drawable;
        this.B = 0;
        ImageView imageView = this.D;
        if (imageView != null) {
            if (drawable != null) {
                imageView.setVisibility(0);
                this.D.setImageDrawable(drawable);
            } else {
                imageView.setVisibility(8);
            }
        }
    }

    public void o(CharSequence charSequence) {
        this.f326f = charSequence;
        TextView textView = this.F;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void q(CharSequence charSequence) {
        this.f325e = charSequence;
        TextView textView = this.E;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void r(int i10) {
        this.f328h = null;
        this.f329i = i10;
        this.f334n = false;
    }

    public void s(View view) {
        this.f328h = view;
        this.f329i = 0;
        this.f334n = false;
    }

    public void t(View view, int i10, int i11, int i12, int i13) {
        this.f328h = view;
        this.f329i = 0;
        this.f334n = true;
        this.f330j = i10;
        this.f331k = i11;
        this.f332l = i12;
        this.f333m = i13;
    }
}
