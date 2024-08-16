package com.oplus.powermanager.fuelgaue;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.coui.appcompat.toolbar.COUIToolbar;
import com.google.android.material.appbar.AppBarLayout;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.PowerInspectActivity;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import f6.f;
import m8.PowerInspectFragment;

/* loaded from: classes.dex */
public class PowerInspectActivity extends BaseAppCompatActivity {

    /* renamed from: e, reason: collision with root package name */
    private final String f10106e = "tag_powerInspectFragment";

    /* renamed from: f, reason: collision with root package name */
    private COUIToolbar f10107f;

    /* renamed from: g, reason: collision with root package name */
    private AppBarLayout f10108g;

    /* renamed from: h, reason: collision with root package name */
    private Button f10109h;

    private void h() {
        COUIToolbar cOUIToolbar = (COUIToolbar) findViewById(R.id.power_onekey_layout_toolbar);
        this.f10107f = cOUIToolbar;
        cOUIToolbar.setTitle(R.string.battery_ui_power_optimization);
        this.f10108g = (AppBarLayout) findViewById(R.id.power_onekey_appBarLayout);
        setSupportActionBar(this.f10107f);
        onGetActionBar().s(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsets lambda$onCreate$0(View view, WindowInsets windowInsets) {
        view.setPadding(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingEnd(), windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars()).bottom);
        return windowInsets;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ViewGroup.LayoutParams layoutParams = this.f10109h.getLayoutParams();
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.one_key_main_width);
        this.f10109h.setLayoutParams(layoutParams);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LocalLog.a("PowerInspectActivity", "onCreate");
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.one_key_main_activity);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        h();
        if (getSupportFragmentManager().j0("tag_powerInspectFragment") == null) {
            getSupportFragmentManager().m().s(R.id.inspect_content, new PowerInspectFragment(), "tag_powerInspectFragment").i();
        }
        this.f10107f.setTitleTextColor(getResources().getColor(R.color.coui_color_primary_neutral));
        View W0 = f.W0(this);
        this.f10108g.addView(W0, 0, W0.getLayoutParams());
        this.f10109h = (Button) findViewById(R.id.power_use_operation_menu);
        getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: y7.d
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                WindowInsets lambda$onCreate$0;
                lambda$onCreate$0 = PowerInspectActivity.lambda$onCreate$0(view, windowInsets);
                return lambda$onCreate$0;
            }
        });
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }
}
