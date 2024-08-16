package com.oplus.powermanager.fuelgaue;

import android.os.Bundle;
import android.text.format.DateFormat;
import b6.LocalLog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import java.util.HashMap;
import m8.IntellPowerSaveScenceFragment;
import x5.UploadDataUtil;

/* loaded from: classes.dex */
public class IntellPowerSaveScence extends BaseAppCompatActivity {
    private void g() {
        try {
            if (Boolean.valueOf(getIntent().getBooleanExtra("enter_from_notify", false)).booleanValue()) {
                HashMap hashMap = new HashMap();
                UploadDataUtil S0 = UploadDataUtil.S0(this);
                hashMap.put("clickNetOffNotify", String.valueOf(true));
                hashMap.put("clickNotifyTime", DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()).toString());
                LocalLog.a("IntellPowerSaveScence", "upload: eventMap = " + hashMap);
                S0.v(hashMap);
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LocalLog.a("IntellPowerSaveScence", "onCreate");
        COUIThemeOverlay.i().b(this);
        setContentView(R.layout.common_preference_layout);
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this);
        getSupportFragmentManager().m().r(R.id.fragment_container, new IntellPowerSaveScenceFragment()).i();
        g();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        LocalLog.a("IntellPowerSaveScence", "onDestroy");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        LocalLog.a("IntellPowerSaveScence", "onPause");
    }
}
