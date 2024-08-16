package com.oplus.powermanager.fuelgaue;

import android.os.Bundle;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import d8.PerformanceModeFragment;

/* loaded from: classes.dex */
public class PerformanceModeActivity extends BaseAppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!y5.b.B()) {
            LocalLog.a("PerformanceModeActivity", "not support benchmode");
            finish();
        } else {
            COUIThemeOverlay.i().b(this);
            setContentView(R.layout.common_preference_layout);
            StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
            getSupportFragmentManager().m().r(R.id.fragment_container, new PerformanceModeFragment()).i();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }
}
