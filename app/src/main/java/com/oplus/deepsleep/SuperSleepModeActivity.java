package com.oplus.deepsleep;

import android.os.Bundle;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import com.oplus.util.OplusLog;
import y5.b;

/* loaded from: classes.dex */
public class SuperSleepModeActivity extends BaseAppCompatActivity {
    private static final String TAG = "SuperSleepModeActivity";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!b.D()) {
            OplusLog.d(TAG, "only rm device support SuperSleepMode");
            finish();
        } else {
            COUIThemeOverlay.i().b(this);
            setContentView(R.layout.common_preference_layout);
            StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
            getSupportFragmentManager().m().r(R.id.fragment_container, new SuperSleepModeFragment()).i();
        }
    }
}
