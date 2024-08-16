package com.oplus.powermanager.fuelgaue;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.window.embedding.SplitController;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import f6.CommonUtil;
import m8.PowerConsumptionFragment;

/* loaded from: classes.dex */
public class PowerConsumptionActivity extends BaseAppCompatActivity {

    /* renamed from: e, reason: collision with root package name */
    private COUIToolbar f10012e;

    /* renamed from: f, reason: collision with root package name */
    private AppBarLayout f10013f;

    /* renamed from: g, reason: collision with root package name */
    private View f10014g = null;

    /* renamed from: h, reason: collision with root package name */
    private boolean f10015h = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f10016i = false;

    private boolean g() {
        if (!this.f10016i || CommonUtil.U() || !this.f10015h) {
            return false;
        }
        moveTaskToBack(true);
        finish();
        return true;
    }

    private void h() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().s((this.f10016i && this.f10015h) ? false : true);
        }
    }

    @Override // android.view.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        g();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.f10016i = SplitController.d().e(this);
        h();
        super.onConfigurationChanged(configuration);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.one_key_entry_activity);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        COUIToolbar cOUIToolbar = (COUIToolbar) findViewById(R.id.consumption_toolbar);
        this.f10012e = cOUIToolbar;
        cOUIToolbar.setTitle(getString(R.string.app_name));
        this.f10013f = (AppBarLayout) findViewById(R.id.consumption_appBarLayout);
        this.f10014g = findViewById(R.id.divider_line);
        setSupportActionBar(this.f10012e);
        onGetActionBar().s(true);
        getSupportFragmentManager().m().r(R.id.consumption_content, new PowerConsumptionFragment()).i();
        this.f10012e.setTitleTextColor(getResources().getColor(R.color.coui_color_primary_neutral));
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, 0));
        this.f10013f.addView(imageView, 0, imageView.getLayoutParams());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.f10015h = extras.getBoolean(":settings:oplus_from_main_page", false);
        }
        this.f10016i = SplitController.d().e(this);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        LocalLog.a("PowerConsumptionActivity", "onDestroy");
        super.onDestroy();
    }

    @Override // android.view.ComponentActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z10, Configuration configuration) {
        LocalLog.a("PowerConsumptionActivity", "onMultiWindowModeChanged: " + z10);
        super.onMultiWindowModeChanged(z10, configuration);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        LocalLog.a("PowerConsumptionActivity", "onPause");
        super.onPause();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        LocalLog.a("PowerConsumptionActivity", "onResume");
        h();
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
    }
}
