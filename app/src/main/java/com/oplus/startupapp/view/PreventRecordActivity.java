package com.oplus.startupapp.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import ca.PreventRecordFragment;
import com.oplus.battery.R;
import com.oplus.startupapp.common.base.BaseActivity;

/* loaded from: classes2.dex */
public class PreventRecordActivity extends BaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.startupapp.common.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.launch_record);
        g(this);
        getSupportFragmentManager().m().r(R.id.fragment_container, new PreventRecordFragment()).i();
    }

    @Override // com.oplus.startupapp.common.base.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        dispatchKeyEvent(new KeyEvent(0, 4));
        dispatchKeyEvent(new KeyEvent(1, 4));
        return true;
    }
}
