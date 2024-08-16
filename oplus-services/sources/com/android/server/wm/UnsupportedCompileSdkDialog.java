package com.android.server.wm;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.android.server.utils.AppInstallerUtil;
import com.android.server.wm.AppWarnings;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class UnsupportedCompileSdkDialog extends AppWarnings.BaseDialog {
    /* JADX INFO: Access modifiers changed from: package-private */
    public UnsupportedCompileSdkDialog(final AppWarnings appWarnings, final Context context, ApplicationInfo applicationInfo) {
        super(appWarnings, applicationInfo.packageName);
        AlertDialog.Builder view = new AlertDialog.Builder(context).setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) null).setMessage(context.getString(17041778, applicationInfo.loadSafeLabel(context.getPackageManager(), 1000.0f, 5))).setView(17367374);
        final Intent createIntent = AppInstallerUtil.createIntent(context, applicationInfo.packageName);
        if (createIntent != null) {
            view.setNeutralButton(17041777, new DialogInterface.OnClickListener() { // from class: com.android.server.wm.UnsupportedCompileSdkDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    context.startActivity(createIntent);
                }
            });
        }
        AlertDialog create = view.create();
        this.mDialog = create;
        create.create();
        Window window = this.mDialog.getWindow();
        window.setType(2002);
        window.getAttributes().setTitle("UnsupportedCompileSdkDialog");
        CheckBox checkBox = (CheckBox) this.mDialog.findViewById(R.id.body);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.server.wm.UnsupportedCompileSdkDialog$$ExternalSyntheticLambda1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                UnsupportedCompileSdkDialog.this.lambda$new$1(appWarnings, compoundButton, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(AppWarnings appWarnings, CompoundButton compoundButton, boolean z) {
        appWarnings.setPackageFlag(this.mPackageName, 2, !z);
    }
}
