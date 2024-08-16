package android.widget;

import android.content.Context;

/* loaded from: classes.dex */
public class OplusSuperTextHelper {
    public static void startInsertionActionMode(TextView textView) {
        Context context;
        if (textView != null && (context = textView.getContext()) != null) {
            context.enforceCallingOrSelfPermission("com.oplus.permission.safe.PROTECT", "SuperText Permission");
            Editor editor = ((ITextViewWrapper) textView.getWrapper()).getEditor();
            if (editor != null) {
                editor.startInsertionActionMode();
            }
        }
    }
}
