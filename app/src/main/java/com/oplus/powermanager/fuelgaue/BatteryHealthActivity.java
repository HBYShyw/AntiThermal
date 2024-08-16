package com.oplus.powermanager.fuelgaue;

import android.os.Bundle;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import m8.BatteryHealthFragment;

/* loaded from: classes.dex */
public class BatteryHealthActivity extends BaseAppCompatActivity {

    /* renamed from: e, reason: collision with root package name */
    private final String f10011e = "tag_batteryhealthfragment";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.common_preference_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        if (getSupportFragmentManager().j0("tag_batteryhealthfragment") == null) {
            getSupportFragmentManager().m().s(R.id.fragment_container, new BatteryHealthFragment(), "tag_batteryhealthfragment").i();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }
}
