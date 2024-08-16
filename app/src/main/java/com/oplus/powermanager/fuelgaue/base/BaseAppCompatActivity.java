package com.oplus.powermanager.fuelgaue.base;

import android.graphics.Insets;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.FragmentManager;
import f6.f;
import y5.AppFeature;

/* loaded from: classes.dex */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements ActivityConfig {
    protected static final String KEY_ENABLE_TASKBAR = "enable_launcher_taskbar";
    private ActivityDelegate mActivityDelegate = null;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$onCreate$0(View view, WindowInsets windowInsets) {
        Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
        boolean z10 = Settings.System.getInt(getContentResolver(), KEY_ENABLE_TASKBAR, -1) == 1 && insetsIgnoringVisibility.bottom >= o3.a.d(30.0f, getResources()) && f.g1(this);
        boolean isGestureNavigation = StatusBarUtil.isGestureNavigation(this);
        if (!z10 && isGestureNavigation) {
            view.setPadding(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingEnd(), 0);
        } else {
            view.setPadding(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingEnd(), insetsIgnoringVisibility.bottom);
        }
        StatusBarUtil.setStatusBarTransparentAndBlackFont(this, z10);
        return windowInsets;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.ActivityConfig
    public int getStatusType() {
        return 1;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.ActivityConfig
    public boolean isHomeAsUpEnabled() {
        return true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.ActivityConfig
    public boolean isShowMenuDescription() {
        return true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.ActivityConfig
    public boolean isTitleNeedUpdate() {
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onContentChanged() {
        super.onContentChanged();
        this.mActivityDelegate.onContentChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        this.mActivityDelegate = new ActivityDelegate(this);
        super.onCreate(bundle);
        if (!f.g1(this)) {
            setRequestedOrientation(5);
        }
        this.mActivityDelegate.onCreate(getDelegate());
        if (AppFeature.s()) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.oplus.powermanager.fuelgaue.base.a
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets lambda$onCreate$0;
                    lambda$onCreate$0 = BaseAppCompatActivity.this.lambda$onCreate$0(view, windowInsets);
                    return lambda$onCreate$0;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ActionBar onGetActionBar() {
        return getSupportActionBar();
    }

    protected FragmentManager onGetFragmentManager() {
        return getSupportFragmentManager();
    }

    protected void onInvalidateOptionsMenu() {
        supportInvalidateOptionsMenu();
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        this.mActivityDelegate.onOptionsItemSelected(menuItem);
        return super.onOptionsItemSelected(menuItem);
    }

    protected ActionMode onStartActionMode(ActionMode.a aVar) {
        return startSupportActionMode(aVar);
    }
}
