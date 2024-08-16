package com.oplus.powermanager.fuelgaue;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.performance.GTModeTile;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import m8.WirelessReverseChargingFragment;

/* loaded from: classes.dex */
public class WirelessReverseChargingActivity extends BaseAppCompatActivity {
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        ComponentName componentName;
        super.onCreate(bundle);
        LocalLog.a("WirelessReverseChargingActivity", "onCreate");
        try {
            Intent intent = getIntent();
            if (intent != null && (componentName = (ComponentName) intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME")) != null && GTModeTile.class.getName().equals(componentName.getClassName())) {
                if (y5.b.E()) {
                    PackageManager packageManager = getPackageManager();
                    Intent intent2 = new Intent();
                    intent2.setClassName("com.oplus.gtmode", "com.oplus.gtmode.GtmodeActivity");
                    if (packageManager.resolveActivity(intent2, 0) != null) {
                        Intent intent3 = new Intent();
                        intent3.setComponent(new ComponentName("com.oplus.gtmode", "com.oplus.gtmode.GtmodeActivity"));
                        intent3.setFlags(603979776);
                        startActivity(intent3);
                    }
                }
                finish();
                return;
            }
        } catch (Exception e10) {
            LocalLog.b("WirelessReverseChargingActivity", "get intent error + " + e10);
        }
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.common_preference_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        getSupportFragmentManager().m().r(R.id.fragment_container, new WirelessReverseChargingFragment()).i();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        LocalLog.a("WirelessReverseChargingActivity", "onDestroy");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        LocalLog.a("WirelessReverseChargingActivity", "onPause");
    }
}
