package com.oplus.powermanager.powerusage.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.OplusPackageManager;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManagerGlobal;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import b8.OplusIconUtils;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.powerusage.view.PowerCheckboxPreference;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import l8.IPowerInspectUpdate;
import u8.BasicPowerIssue;

/* loaded from: classes2.dex */
public class PowerCheckboxPreference extends COUIPreference implements View.OnClickListener {
    private final boolean F;
    private TextView G;
    private TextView H;
    private CheckBox I;
    private RelativeLayout J;
    private OneKeyGridView K;
    private a L;
    private b M;
    private BasicPowerIssue N;
    private ArrayList<PackageInfo> O;
    private String P;
    private String Q;
    private boolean R;
    private int S;
    private boolean T;
    private Context U;
    private IPowerInspectUpdate V;
    private View W;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class a extends BaseAdapter {

        /* renamed from: e, reason: collision with root package name */
        private final OplusPackageManager f10408e;

        /* renamed from: f, reason: collision with root package name */
        private final ConcurrentHashMap<String, Drawable> f10409f = new ConcurrentHashMap<>();

        /* renamed from: g, reason: collision with root package name */
        private ArrayList<PackageInfo> f10410g;

        /* renamed from: h, reason: collision with root package name */
        private Context f10411h;

        /* renamed from: i, reason: collision with root package name */
        private ExecutorService f10412i;

        /* renamed from: com.oplus.powermanager.powerusage.view.PowerCheckboxPreference$a$a, reason: collision with other inner class name */
        /* loaded from: classes2.dex */
        public static class C0027a {

            /* renamed from: a, reason: collision with root package name */
            String f10413a = "";

            /* renamed from: b, reason: collision with root package name */
            ImageView f10414b;
        }

        public a(Context context, ArrayList<PackageInfo> arrayList) {
            this.f10410g = arrayList;
            this.f10411h = context;
            e();
            this.f10408e = new OplusPackageManager(this.f10411h);
        }

        private void e() {
            this.f10412i = Executors.newCachedThreadPool();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void f(C0027a c0027a, String str, Drawable drawable) {
            if (c0027a.f10413a.equals(str)) {
                c0027a.f10414b.setImageDrawable(drawable);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void g(ApplicationInfo applicationInfo, final String str, final C0027a c0027a) {
            Drawable applicationIconCache = this.f10408e.getApplicationIconCache(applicationInfo);
            if (applicationIconCache == null) {
                return;
            }
            final Drawable b10 = OplusIconUtils.b(this.f10411h, applicationIconCache);
            this.f10409f.put(str, b10);
            this.f10411h.getMainThreadHandler().post(new Runnable() { // from class: com.oplus.powermanager.powerusage.view.a
                @Override // java.lang.Runnable
                public final void run() {
                    PowerCheckboxPreference.a.f(PowerCheckboxPreference.a.C0027a.this, str, b10);
                }
            });
        }

        @Override // android.widget.Adapter
        /* renamed from: d, reason: merged with bridge method [inline-methods] */
        public PackageInfo getItem(int i10) {
            return this.f10410g.get(i10);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.f10410g.size();
        }

        @Override // android.widget.Adapter
        public long getItemId(int i10) {
            return i10;
        }

        @Override // android.widget.Adapter
        public View getView(int i10, View view, ViewGroup viewGroup) {
            final C0027a c0027a;
            if (view == null) {
                view = View.inflate(this.f10411h, R.layout.one_key_gridview_layout, null);
                c0027a = new C0027a();
                c0027a.f10414b = (ImageView) view.findViewById(R.id.one_key_grid_img);
                view.setTag(c0027a);
            } else {
                c0027a = (C0027a) view.getTag();
            }
            final ApplicationInfo applicationInfo = getItem(i10).applicationInfo;
            final String str = applicationInfo.packageName;
            Drawable drawable = this.f10409f.get(str);
            if (drawable != null) {
                c0027a.f10414b.setImageDrawable(drawable);
            } else if (!c0027a.f10413a.equals(str)) {
                c0027a.f10414b.setImageDrawable(null);
                this.f10412i.execute(new Runnable() { // from class: com.oplus.powermanager.powerusage.view.b
                    @Override // java.lang.Runnable
                    public final void run() {
                        PowerCheckboxPreference.a.this.g(applicationInfo, str, c0027a);
                    }
                });
            }
            c0027a.f10413a = str;
            return view;
        }

        public void h() {
            if (this.f10412i == null) {
                e();
            }
        }

        public void i() {
            ExecutorService executorService = this.f10412i;
            if (executorService != null) {
                executorService.shutdownNow();
                this.f10412i = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public interface b {
        void f(BasicPowerIssue basicPowerIssue);
    }

    public PowerCheckboxPreference(Context context, boolean z10, int i10, boolean z11, IPowerInspectUpdate iPowerInspectUpdate) {
        super(context);
        this.O = new ArrayList<>();
        this.Q = "";
        this.R = false;
        this.U = context;
        this.S = i10;
        this.F = z10;
        this.T = z11;
        this.V = iPowerInspectUpdate;
        setSelectable(false);
        setLayoutResource(R.layout.one_key_unresolved_abnormal_issue_item);
    }

    private void h() {
        this.J = (RelativeLayout) this.W.findViewById(R.id.one_key_text_layout);
        this.G = (TextView) this.W.findViewById(R.id.one_key_unresolved_title);
        this.H = (TextView) this.W.findViewById(R.id.one_key_unresolved_sectitle);
        this.K = (OneKeyGridView) this.W.findViewById(R.id.one_key_unresolved_details);
        try {
            if (WindowManagerGlobal.getWindowManagerService().getInitialDisplayDensity(0) < getContext().getResources().getConfiguration().densityDpi) {
                this.K.setMaxColumns(5);
            }
        } catch (RemoteException e10) {
            e10.printStackTrace();
        }
        this.I = (CheckBox) this.W.findViewById(R.id.one_key_unresolved_checkbox);
        this.G.setText(this.P);
        if (this.H != null) {
            if (this.Q.equals("")) {
                this.H.setVisibility(8);
            } else {
                this.H.setText(this.Q);
                this.H.setVisibility(0);
            }
        }
        a aVar = this.L;
        if (aVar != null && aVar.f10410g != null) {
            if (this.K.getAdapter() == null || this.K.getAdapter() != this.L) {
                this.K.setAdapter((ListAdapter) this.L);
                LocalLog.a("PowerCheckboxPreference", "setAdapter");
            }
            if (this.K.getVisibility() != 0) {
                this.K.setVisibility(0);
            }
        } else if (this.K.getVisibility() == 0) {
            this.K.setVisibility(8);
        }
        n(this.R);
        if (this.T) {
            return;
        }
        this.I.setOnClickListener(this);
        RelativeLayout relativeLayout = this.J;
        if (relativeLayout != null) {
            relativeLayout.setOnClickListener(this);
        }
        TextView textView = this.G;
        if (textView != null) {
            textView.setOnClickListener(this);
        }
        TextView textView2 = this.H;
        if (textView2 != null) {
            textView2.setOnClickListener(this);
        }
    }

    public boolean i() {
        return this.R;
    }

    public boolean j() {
        BasicPowerIssue basicPowerIssue = this.N;
        return basicPowerIssue != null && (basicPowerIssue.g() == 0 || this.N.g() == 3);
    }

    public void l() {
        OneKeyGridView oneKeyGridView = this.K;
        if (oneKeyGridView == null || oneKeyGridView.getVisibility() != 0) {
            return;
        }
        this.K.a();
    }

    public void m(b bVar, BasicPowerIssue basicPowerIssue) {
        this.M = bVar;
        this.N = basicPowerIssue;
    }

    public void n(boolean z10) {
        CheckBox checkBox = this.I;
        if (checkBox != null) {
            checkBox.setChecked(z10);
        }
        this.R = z10;
    }

    public void o(String str) {
        if (!str.equals("")) {
            String str2 = "+" + str;
            this.Q = str2;
            TextView textView = this.H;
            if (textView != null) {
                textView.setText(str2);
                this.H.setVisibility(0);
                return;
            }
            return;
        }
        TextView textView2 = this.H;
        if (textView2 != null) {
            this.Q = "";
            textView2.setVisibility(8);
        }
    }

    @Override // androidx.preference.Preference
    public void onAttached() {
        super.onAttached();
        a aVar = this.L;
        if (aVar != null) {
            aVar.h();
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        LocalLog.a("PowerCheckboxPreference", "onBindViewHolder key=" + this.N.e() + " mItemTitle=" + this.P + " mItemSecTitle=" + this.Q + "holder.pos=" + preferenceViewHolder.getAbsoluteAdapterPosition());
        View view = preferenceViewHolder.itemView;
        this.W = view;
        if (view == null) {
            LocalLog.a("PowerCheckboxPreference", "setA inflate ");
            this.W = View.inflate(this.U, R.layout.one_key_unresolved_abnormal_issue_item, null);
        }
        h();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        b bVar;
        if (this.V.G() || (bVar = this.M) == null) {
            return;
        }
        bVar.f(this.N);
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onDetached() {
        super.onDetached();
        a aVar = this.L;
        if (aVar != null) {
            aVar.i();
        }
    }

    public void p(String str) {
        this.P = str;
        TextView textView = this.G;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void q(Context context, ArrayList<PackageInfo> arrayList) {
        this.O.clear();
        if (arrayList != null && arrayList.size() > 0) {
            this.O.addAll(arrayList);
            if (this.L == null) {
                a aVar = new a(context, this.O);
                this.L = aVar;
                OneKeyGridView oneKeyGridView = this.K;
                if (oneKeyGridView != null) {
                    oneKeyGridView.setAdapter((ListAdapter) aVar);
                }
            }
            OneKeyGridView oneKeyGridView2 = this.K;
            if (oneKeyGridView2 == null || oneKeyGridView2.getVisibility() == 0) {
                return;
            }
            this.K.setVisibility(0);
            return;
        }
        OneKeyGridView oneKeyGridView3 = this.K;
        if (oneKeyGridView3 != null && oneKeyGridView3.getVisibility() == 0) {
            this.K.setVisibility(8);
        }
        OneKeyGridView oneKeyGridView4 = this.K;
        if (oneKeyGridView4 != null && this.L != null) {
            oneKeyGridView4.setAdapter((ListAdapter) null);
        }
        this.L = null;
    }
}
