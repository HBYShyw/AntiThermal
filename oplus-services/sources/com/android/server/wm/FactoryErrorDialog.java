package com.android.server.wm;

import android.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import com.android.server.am.BaseErrorDialog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
final class FactoryErrorDialog extends BaseErrorDialog {
    private final Handler mHandler;

    protected void closeDialog() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FactoryErrorDialog(Context context, CharSequence charSequence) {
        super(context);
        Handler handler = new Handler() { // from class: com.android.server.wm.FactoryErrorDialog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                throw new RuntimeException("Rebooting from failed factory test");
            }
        };
        this.mHandler = handler;
        setCancelable(false);
        setTitle(context.getText(R.string.keyguard_accessibility_expand_lock_area));
        setMessage(charSequence);
        setButton(-1, context.getText(R.string.keyguard_accessibility_pattern_area), handler.obtainMessage(0));
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.setTitle("Factory Error");
        getWindow().setAttributes(attributes);
    }
}
