package com.oplus.powermanager.fuelgaue;

import android.os.Bundle;
import android.view.MenuItem;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import d8.PowerAppsBgSettingFragment;

/* loaded from: classes.dex */
public class PowerAppsBgSetting extends BaseAppCompatActivity {
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.common_preference_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        getSupportFragmentManager().m().r(R.id.fragment_container, new PowerAppsBgSettingFragment()).i();
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
