package com.oplus.startupapp.common.base;

import android.graphics.Insets;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import androidx.appcompat.app.AppCompatActivity;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.StatusBarUtil;
import com.oplus.startupapp.common.base.BaseActivity;
import f6.f;
import v9.a;
import v9.b;
import x9.AppFeatureUtils;

/* loaded from: classes2.dex */
public abstract class BaseActivity extends AppCompatActivity implements a {

    /* renamed from: e, reason: collision with root package name */
    private b f10480e = null;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets h(AppCompatActivity appCompatActivity, View view, WindowInsets windowInsets) {
        Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
        boolean z10 = Settings.System.getInt(getContentResolver(), "enable_launcher_taskbar", -1) == 1 && insetsIgnoringVisibility.bottom >= o3.a.d(30.0f, getResources()) && f.g1(this);
        boolean isGestureNavigation = StatusBarUtil.isGestureNavigation(this);
        if (!z10 && isGestureNavigation) {
            view.setPadding(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingEnd(), 0);
        } else {
            view.setPadding(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingEnd(), insetsIgnoringVisibility.bottom);
        }
        StatusBarUtil.setStatusBarTransparentAndBlackFont(appCompatActivity, z10);
        return windowInsets;
    }

    public void g(final AppCompatActivity appCompatActivity) {
        if (AppFeatureUtils.a(getApplicationContext())) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: v9.c
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets h10;
                    h10 = BaseActivity.this.h(appCompatActivity, view, windowInsets);
                    return h10;
                }
            });
        } else {
            StatusBarUtil.setStatusBarTransparentAndBlackFont(appCompatActivity);
        }
    }

    @Override // v9.a
    public int getStatusType() {
        return 1;
    }

    @Override // v9.a
    public boolean isHomeAsUpEnabled() {
        return true;
    }

    @Override // v9.a
    public boolean isTitleNeedUpdate() {
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onContentChanged() {
        super.onContentChanged();
        this.f10480e.b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        this.f10480e = new b(this);
        super.onCreate(bundle);
        COUIThemeOverlay.i().b(this);
        setTheme(R.style.DarkForceStyle);
        this.f10480e.c(getDelegate());
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        this.f10480e.d(menuItem);
        return super.onOptionsItemSelected(menuItem);
    }
}
