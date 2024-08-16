package com.oplus.powermanager.fuelgaue;

import android.os.Bundle;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import m8.WirelessChargingSettingsFragment;

/* loaded from: classes.dex */
public class WirelessChargingSettingsActivity extends BaseAppCompatActivity {
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LocalLog.a("WirelessChargingSettingsActivity", "onCreate");
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.common_preference_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        getSupportFragmentManager().m().r(R.id.fragment_container, new WirelessChargingSettingsFragment()).i();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        LocalLog.a("WirelessChargingSettingsActivity", "onDestroy");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        LocalLog.a("WirelessChargingSettingsActivity", "onPause");
    }
}
