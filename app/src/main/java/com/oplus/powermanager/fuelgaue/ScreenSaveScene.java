package com.oplus.powermanager.fuelgaue;

import android.os.Bundle;
import android.view.MenuItem;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import d8.ScreenSaveSceneFragment;

/* loaded from: classes.dex */
public class ScreenSaveScene extends BaseAppCompatActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.common_preference_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        getSupportFragmentManager().m().r(R.id.fragment_container, new ScreenSaveSceneFragment()).i();
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
