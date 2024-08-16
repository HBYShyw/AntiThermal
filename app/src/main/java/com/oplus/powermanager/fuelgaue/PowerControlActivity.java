package com.oplus.powermanager.fuelgaue;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import h8.IPowerControlPresenter;
import k8.PowerControlPresenter;
import l8.IPowerControlViewUpdate;
import m8.PowerControlFragment;
import x5.UploadDataUtil;

/* loaded from: classes.dex */
public class PowerControlActivity extends BaseAppCompatActivity implements IPowerControlViewUpdate {

    /* renamed from: e, reason: collision with root package name */
    private COUIToolbar f10090e = null;

    /* renamed from: f, reason: collision with root package name */
    private AppBarLayout f10091f = null;

    /* renamed from: g, reason: collision with root package name */
    private View f10092g = null;

    /* renamed from: h, reason: collision with root package name */
    private MenuItem f10093h = null;

    /* renamed from: i, reason: collision with root package name */
    private MenuItem f10094i = null;

    /* renamed from: j, reason: collision with root package name */
    private Context f10095j = null;

    /* renamed from: k, reason: collision with root package name */
    private UploadDataUtil f10096k = null;

    /* renamed from: l, reason: collision with root package name */
    private String f10097l = null;

    /* renamed from: m, reason: collision with root package name */
    private String f10098m = null;

    /* renamed from: n, reason: collision with root package name */
    private String f10099n = null;

    /* renamed from: o, reason: collision with root package name */
    private boolean f10100o = false;

    /* renamed from: p, reason: collision with root package name */
    private boolean f10101p = false;

    /* renamed from: q, reason: collision with root package name */
    private boolean f10102q = false;

    /* renamed from: r, reason: collision with root package name */
    private AlertDialog f10103r = null;

    /* renamed from: s, reason: collision with root package name */
    private boolean f10104s = false;

    /* renamed from: t, reason: collision with root package name */
    private IPowerControlPresenter f10105t;

    private void g() {
        COUIToolbar cOUIToolbar = (COUIToolbar) findViewById(R.id.custom_toolbar);
        this.f10090e = cOUIToolbar;
        cOUIToolbar.setTitle(this.f10098m);
        this.f10090e.setOverflowIcon(ContextCompat.e(this, R.drawable.ic_more));
        this.f10091f = (AppBarLayout) findViewById(R.id.custom_appBarLayout);
        setSupportActionBar(this.f10090e);
        onGetActionBar().s(true);
        this.f10090e.setTitleTextColor(getResources().getColor(R.color.coui_color_primary_neutral));
        this.f10105t = new PowerControlPresenter(this.f10097l, this);
        getSupportFragmentManager().m().r(R.id.fragment_container, new PowerControlFragment()).i();
    }

    @Override // l8.IPowerControlViewUpdate
    public void a(boolean z10) {
        this.f10100o = z10;
        if (this.f10093h == null || isFinishing()) {
            return;
        }
        this.f10093h.setEnabled(this.f10100o);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.power_control_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        try {
            this.f10098m = getIntent().getStringExtra("title");
            this.f10097l = getIntent().getStringExtra("pkgName");
            this.f10099n = getIntent().getStringExtra("drainType");
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        if ("APP".equals(this.f10099n)) {
            this.f10101p = true;
        } else if ("SCREEN".equals(this.f10099n)) {
            this.f10101p = false;
        } else {
            this.f10102q = true;
        }
        g();
        this.f10095j = this;
        this.f10096k = UploadDataUtil.S0(this);
        if (this.f10101p) {
            this.f10100o = true;
            this.f10105t.c();
        }
        LocalLog.l("PowerControlActivity", "onCreate mTitle=" + this.f10098m + " mPkgName=" + this.f10097l + " mDrainType=" + this.f10099n + " mIsEnableToStop=" + this.f10100o);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.f10105t.j();
        super.onDestroy();
    }
}
