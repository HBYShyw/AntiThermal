package com.android.server.wm;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class LaunchWarningWindow extends Dialog {
    public LaunchWarningWindow(Context context, ActivityRecord activityRecord, ActivityRecord activityRecord2) {
        super(context, R.style.Theme.Material.Settings.Dialog.Presentation);
        requestWindowFeature(3);
        getWindow().setType(2003);
        getWindow().addFlags(24);
        setContentView(R.layout.notification_material_action_list);
        setTitle(context.getText(R.string.mmiErrorWhileRoaming));
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.alertDialogIcon, typedValue, true);
        getWindow().setFeatureDrawableResource(3, typedValue.resourceId);
        ((ImageView) findViewById(R.id.signed)).setImageDrawable(activityRecord2.info.applicationInfo.loadIcon(context.getPackageManager()));
        ((TextView) findViewById(R.id.silent)).setText(context.getResources().getString(R.string.mmiError, activityRecord2.info.applicationInfo.loadLabel(context.getPackageManager()).toString()));
        ((ImageView) findViewById(R.id.remote_input_progress)).setImageDrawable(activityRecord.info.applicationInfo.loadIcon(context.getPackageManager()));
        ((TextView) findViewById(R.id.remote_input_send)).setText(context.getResources().getString(R.string.mmiComplete, activityRecord.info.applicationInfo.loadLabel(context.getPackageManager()).toString()));
    }
}
