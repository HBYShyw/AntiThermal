package com.oplus.powermanager.fuelgaue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import b6.LocalLog;
import b9.SignatureUtils;
import com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class GAJumpActivity extends BaseAppCompatActivity {
    private void g() {
        boolean equals = "7e0f77a911ca01371fc013ef2668f7933490a492026d12bae946ece03f3321dd".equals(SignatureUtils.b(getApplicationContext(), h()));
        if ("com.google.android.googlequicksearchbox".equals(h()) && equals) {
            startActivity(new Intent(getApplicationContext(), (Class<?>) PowerConsumptionActivity.class));
            return;
        }
        LocalLog.a("GAJumpActivity", "referrer:" + h() + " is not allowed. finish!");
        finish();
    }

    private String h() {
        try {
            Field declaredField = Activity.class.getDeclaredField("mReferrer");
            declaredField.setAccessible(true);
            return (String) declaredField.get(this);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e10) {
            e10.printStackTrace();
            return "No referrer";
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.powermanager.fuelgaue.base.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        g();
        finish();
    }
}
