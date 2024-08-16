package com.oplus.powermanager.fuelgaue.base;

import android.app.Activity;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;

/* loaded from: classes.dex */
public class ActivityDelegate {
    private final Activity mActivity;
    private final ActivityConfig mActivityConfig;

    /* JADX WARN: Multi-variable type inference failed */
    public ActivityDelegate(Activity activity) {
        this.mActivity = activity;
        ActivityConfig activityConfig = (ActivityConfig) activity;
        this.mActivityConfig = activityConfig;
        if (activityConfig.getStatusType() == 2) {
            activity.getWindow().addFlags(67108864);
        }
    }

    public void onContentChanged() {
    }

    public void onCreate(AppCompatDelegate appCompatDelegate) {
        ActionBar s7 = appCompatDelegate.s();
        if (s7 != null) {
            s7.s(this.mActivityConfig.isHomeAsUpEnabled());
        }
        this.mActivityConfig.isTitleNeedUpdate();
    }

    public void onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return;
        }
        this.mActivity.finish();
    }
}
