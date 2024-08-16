package android.app.dialog;

import android.app.AlertDialog;
import android.content.Context;

/* loaded from: classes.dex */
public class OplusAlertDialogBuilderExt implements IOplusAlertDialogBuilderExt {
    public OplusAlertDialogBuilderExt(Object base) {
    }

    public AlertDialog.Builder getCenterBuilder(Context context) {
        return OplusAlertDialogBuilder.createCenterBuilder(context);
    }

    public AlertDialog.Builder getBottomBuilder(Context context) {
        return OplusAlertDialogBuilder.createBottomBuilder(context);
    }
}
