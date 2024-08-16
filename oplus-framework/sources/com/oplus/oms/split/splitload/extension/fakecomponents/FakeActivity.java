package com.oplus.oms.split.splitload.extension.fakecomponents;

import android.app.Activity;
import android.os.Bundle;
import com.oplus.oms.split.common.SplitLog;

/* loaded from: classes.dex */
public class FakeActivity extends Activity {
    private static final String TAG = "FakeActivity";

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        int themeId = getResources().getIdentifier("Theme.noAnimation", "style", getPackageName());
        if (themeId > 0) {
            getTheme().applyStyle(themeId, true);
        } else {
            SplitLog.w(TAG, "R.style.Theme_noAnimation not found!", new Object[0]);
        }
        super.onCreate(savedInstanceState);
        setRequestedOrientation(-1);
        if (getIntent() != null) {
            setIntent(null);
        }
        finish();
    }
}
