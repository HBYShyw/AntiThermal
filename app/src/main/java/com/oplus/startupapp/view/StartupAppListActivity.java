package com.oplus.startupapp.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import ca.StartupAppListFragment;
import com.oplus.battery.R;
import com.oplus.startupapp.common.base.BaseActivity;

/* loaded from: classes2.dex */
public class StartupAppListActivity extends BaseActivity {
    private void i() {
        StartupAppListFragment startupAppListFragment = new StartupAppListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("show_scene", 0);
        startupAppListFragment.setArguments(bundle);
        getSupportFragmentManager().m().r(R.id.fragment_container, startupAppListFragment).i();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oplus.startupapp.common.base.BaseActivity, androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_permission_startup_top);
        g(this);
        i();
    }

    @Override // com.oplus.startupapp.common.base.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            dispatchKeyEvent(new KeyEvent(0, 4));
            dispatchKeyEvent(new KeyEvent(1, 4));
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }
}
