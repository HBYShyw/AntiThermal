package com.oplus.powermanager.fuelgaue;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import b6.LocalLog;
import b9.PowerSipper;
import com.coui.appcompat.tablayout.COUITab;
import com.coui.appcompat.tablayout.COUITabLayout;
import com.coui.appcompat.tablayout.COUITabLayoutMediator;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.coui.appcompat.viewpager.COUIViewPager2;
import com.coui.appcompat.viewpager.adapter.COUIFragmentStateAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import com.oplus.powermanager.powercurve.graph.UsageGraph;
import f3.COUIToolTips;
import h8.IPowerRankingPresenter;
import java.util.ArrayList;
import java.util.List;
import k8.PowerRankingPresenter;
import l8.IPowerRankingUpdate;
import m8.PowerRankingFragment;
import x5.UploadDataUtil;

/* loaded from: classes.dex */
public class PowerRankingActivity extends BaseAppCompatActivity implements IPowerRankingUpdate {

    /* renamed from: e, reason: collision with root package name */
    private COUITabLayout f10110e;

    /* renamed from: f, reason: collision with root package name */
    private COUIToolbar f10111f;

    /* renamed from: g, reason: collision with root package name */
    private AppBarLayout f10112g;

    /* renamed from: j, reason: collision with root package name */
    private PowerRankingFragment f10115j;

    /* renamed from: k, reason: collision with root package name */
    private PowerRankingFragment f10116k;

    /* renamed from: l, reason: collision with root package name */
    private COUIToolTips f10117l;

    /* renamed from: m, reason: collision with root package name */
    private COUIViewPager2 f10118m;

    /* renamed from: n, reason: collision with root package name */
    private COUIFragmentStateAdapter f10119n;

    /* renamed from: q, reason: collision with root package name */
    private IPowerRankingPresenter f10122q;

    /* renamed from: h, reason: collision with root package name */
    private UsageGraph f10113h = null;

    /* renamed from: i, reason: collision with root package name */
    private ArrayList<String> f10114i = new ArrayList<>();

    /* renamed from: o, reason: collision with root package name */
    private boolean f10120o = false;

    /* renamed from: p, reason: collision with root package name */
    private boolean f10121p = false;

    /* renamed from: r, reason: collision with root package name */
    private boolean f10123r = true;

    /* renamed from: s, reason: collision with root package name */
    private int f10124s = 0;

    /* renamed from: t, reason: collision with root package name */
    private int f10125t = 0;

    /* renamed from: u, reason: collision with root package name */
    private boolean f10126u = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements COUITabLayoutMediator.a {
        a() {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayoutMediator.a
        public void a(COUITab cOUITab, int i10) {
            cOUITab.n((CharSequence) PowerRankingActivity.this.f10114i.get(i10));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements COUITabLayout.c {
        b() {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void a(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void b(COUITab cOUITab) {
        }

        @Override // com.coui.appcompat.tablayout.COUITabLayout.c
        public void c(COUITab cOUITab) {
            LocalLog.a("PowerRankingActivity", "tab " + cOUITab.d());
            int d10 = cOUITab.d();
            if (d10 == 0) {
                PowerRankingActivity.this.u();
            } else {
                if (d10 != 1) {
                    return;
                }
                PowerRankingActivity.this.v();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f10129e;

        c(View view) {
            this.f10129e = view;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PowerRankingActivity.this.w(this.f10129e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class d implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ View f10131e;

        d(View view) {
            this.f10131e = view;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PowerRankingActivity.this.w(this.f10131e);
        }
    }

    /* loaded from: classes.dex */
    class e implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ boolean f10133e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ List f10134f;

        e(boolean z10, List list) {
            this.f10133e = z10;
            this.f10134f = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f10133e) {
                PowerRankingActivity.this.f10116k.o0(this.f10134f, PowerRankingActivity.this.f10121p, this.f10133e != PowerRankingActivity.this.f10120o);
            } else {
                PowerRankingActivity.this.f10115j.o0(this.f10134f, PowerRankingActivity.this.f10121p, this.f10133e != PowerRankingActivity.this.f10120o);
            }
            PowerRankingActivity.this.f10121p = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class f extends COUIFragmentStateAdapter {
        public f(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override // com.coui.appcompat.viewpager.adapter.COUIFragmentStateAdapter
        public Fragment e(int i10) {
            LocalLog.a("PowerRankingActivity", "createFragment: ");
            if (PowerRankingActivity.this.f10115j == null) {
                PowerRankingActivity.this.f10115j = new PowerRankingFragment();
            }
            if (PowerRankingActivity.this.f10116k == null) {
                PowerRankingActivity.this.f10116k = new PowerRankingFragment();
            }
            PowerRankingActivity powerRankingActivity = PowerRankingActivity.this;
            return i10 == 0 ? powerRankingActivity.f10115j : powerRankingActivity.f10116k;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.h
        public int getItemCount() {
            return PowerRankingActivity.this.f10114i.size();
        }
    }

    private void p() {
        View findViewById = findViewById(R.id.tips_text);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.curve_help_layout);
        View findViewById2 = findViewById(R.id.curve_help_icon);
        relativeLayout.setOnClickListener(new c(findViewById2));
        findViewById.setOnClickListener(new d(findViewById2));
    }

    private void q() {
        COUITabLayout cOUITabLayout = (COUITabLayout) findViewById(R.id.tab_layout);
        this.f10110e = cOUITabLayout;
        cOUITabLayout.S(0).h();
        this.f10110e.setOnTabSelectedListener(new b());
    }

    private void r() {
        COUIToolbar cOUIToolbar = (COUIToolbar) findViewById(R.id.layout_toolbar);
        this.f10111f = cOUIToolbar;
        cOUIToolbar.setTitle(R.string.app_power_consumer_title_new);
        this.f10112g = (AppBarLayout) findViewById(R.id.appBarLayout);
        setSupportActionBar(this.f10111f);
        onGetActionBar().s(true);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void s() {
        this.f10110e = (COUITabLayout) findViewById(R.id.tab_layout);
        COUIViewPager2 cOUIViewPager2 = (COUIViewPager2) findViewById(R.id.viewpager);
        this.f10118m = cOUIViewPager2;
        cOUIViewPager2.setOffscreenPageLimit(1);
        this.f10118m.getChildAt(0).setNestedScrollingEnabled(false);
        f fVar = new f(this);
        this.f10119n = fVar;
        this.f10118m.setAdapter(fVar);
        new COUITabLayoutMediator(this.f10110e, this.f10118m, new a()).a();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.f10115j = (PowerRankingFragment) supportFragmentManager.j0("f0");
        this.f10116k = (PowerRankingFragment) supportFragmentManager.j0("f1");
        if (this.f10115j == null) {
            LocalLog.a("PowerRankingActivity", "onCreate: fragment null");
            this.f10115j = new PowerRankingFragment();
        }
        if (this.f10116k == null) {
            this.f10116k = new PowerRankingFragment();
        }
    }

    private boolean t(long j10, long j11) {
        PowerRankingFragment powerRankingFragment;
        PowerRankingFragment powerRankingFragment2 = this.f10116k;
        if (powerRankingFragment2 == null || (powerRankingFragment = this.f10115j) == null) {
            return true;
        }
        return this.f10120o ? powerRankingFragment2.j0(j10, j11) : powerRankingFragment.j0(j10, j11);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void w(View view) {
        if (this.f10117l == null) {
            this.f10117l = new COUIToolTips(this, 1);
        }
        this.f10117l.y(View.inflate(this, R.layout.power_ranking_help, null));
        this.f10117l.A(true);
        this.f10117l.B(view, 4);
    }

    private void x() {
        COUIViewPager2 cOUIViewPager2 = this.f10118m;
        if (cOUIViewPager2 == null || !(cOUIViewPager2.getChildAt(0) instanceof RecyclerView)) {
            return;
        }
        ((RecyclerView) this.f10118m.getChildAt(0)).scrollToPosition(this.f10118m.getCurrentItem());
    }

    private void y(long j10, long j11) {
        PowerRankingFragment powerRankingFragment = this.f10116k;
        if (powerRankingFragment != null && this.f10115j != null) {
            powerRankingFragment.l0(j10, j11);
            this.f10115j.l0(j10, j11);
        } else {
            LocalLog.b("PowerRankingActivity", "setFragmentUpdateTime null error");
        }
    }

    @Override // l8.IPowerRankingUpdate
    public void B(long j10, long j11, boolean z10) {
        if (j10 == j11 && j10 == 0) {
            LocalLog.a("PowerRankingActivity", " startTime is endTime, time=" + j10);
        }
        if (t(j10, j11)) {
            LocalLog.a("PowerRankingActivity", "updateSipperList2 showScreen=" + this.f10120o + " startTime=" + j10 + " endTime=" + j11);
            this.f10122q.a(j10, j11, this.f10120o);
            y(j10, j11);
        }
    }

    @Override // l8.IPowerRankingUpdate
    public void O(long j10, long j11) {
        B(j10, j11, this.f10120o);
    }

    @Override // l8.IPowerRankingUpdate
    public void Y(List<PowerSipper> list, boolean z10) {
        runOnUiThread(new e(z10, list));
    }

    @Override // l8.IPowerRankingUpdate
    public void c(int i10, int i11) {
        PowerRankingFragment powerRankingFragment = this.f10116k;
        if (powerRankingFragment != null && this.f10115j != null) {
            powerRankingFragment.c(i10, i11);
            this.f10115j.c(i10, i11);
        } else {
            LocalLog.a("PowerRankingActivity", "fragment is null");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0011, code lost:
    
        if (r0 != 3) goto L18;
     */
    @Override // android.app.Activity, android.view.Window.Callback
    @SuppressLint({"ClickableViewAccessibility"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (this.f10118m.h()) {
                        int x10 = ((int) motionEvent.getX()) - this.f10124s;
                        int y4 = ((int) motionEvent.getY()) - this.f10125t;
                        LocalLog.a("PowerRankingActivity", "dispatchTouchEvent: disX=" + x10 + " disY=" + y4);
                        if (Math.abs(x10) < Math.abs(y4)) {
                            LocalLog.a("PowerRankingActivity", "dispatchTouchEvent: move false");
                            this.f10118m.setUserInputEnabled(false);
                        }
                    }
                }
            }
            this.f10118m.setUserInputEnabled(true);
            LocalLog.a("PowerRankingActivity", "dispatchTouchEvent: move end true");
        } else {
            this.f10124s = (int) motionEvent.getX();
            this.f10125t = (int) motionEvent.getY();
            LocalLog.a("PowerRankingActivity", "dispatchTouchEvent: down");
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        x();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LocalLog.a("PowerRankingActivity", "onCreate: ");
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.activity_power_consumption_rankings);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        this.f10122q = new PowerRankingPresenter(this, this);
        this.f10114i.clear();
        this.f10114i.add(getResources().getString(R.string.battery_ui_power_consumption));
        this.f10114i.add(getResources().getString(R.string.battery_ui_usage_time));
        UsageGraph usageGraph = (UsageGraph) findViewById(R.id.usage_graph);
        this.f10113h = usageGraph;
        usageGraph.setPowerRankingUpdate(this);
        r();
        q();
        s();
        p();
        View W0 = f6.f.W0(this);
        this.f10112g.addView(W0, 0, W0.getLayoutParams());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        UsageGraph usageGraph = this.f10113h;
        if (usageGraph != null) {
            usageGraph.setPowerRankingUpdate(null);
            this.f10113h = null;
        }
        IPowerRankingPresenter iPowerRankingPresenter = this.f10122q;
        if (iPowerRankingPresenter != null) {
            iPowerRankingPresenter.onDestroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.f10122q.b();
    }

    public void u() {
        this.f10121p = this.f10120o;
        this.f10120o = false;
        PowerRankingFragment powerRankingFragment = this.f10115j;
        if (powerRankingFragment != null) {
            powerRankingFragment.k0(true);
        }
        PowerRankingFragment powerRankingFragment2 = this.f10116k;
        if (powerRankingFragment2 != null) {
            powerRankingFragment2.k0(false);
        }
        if (this.f10123r) {
            this.f10123r = false;
        } else {
            UploadDataUtil.S0(this).o0();
        }
    }

    public void v() {
        this.f10121p = !this.f10120o;
        this.f10120o = true;
        PowerRankingFragment powerRankingFragment = this.f10115j;
        if (powerRankingFragment != null) {
            powerRankingFragment.k0(false);
        }
        PowerRankingFragment powerRankingFragment2 = this.f10116k;
        if (powerRankingFragment2 != null) {
            powerRankingFragment2.k0(true);
        }
        if (this.f10123r) {
            this.f10123r = false;
        } else {
            UploadDataUtil.S0(this).p0();
        }
    }
}
