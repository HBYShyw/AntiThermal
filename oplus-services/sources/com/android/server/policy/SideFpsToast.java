package com.android.server.policy;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SideFpsToast extends Dialog {
    /* JADX INFO: Access modifiers changed from: package-private */
    public SideFpsToast(Context context) {
        super(context);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.text_edit_suggestion_container_material);
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.dimAmount = 0.0f;
        attributes.flags |= 2;
        attributes.gravity = 80;
        window.setAttributes(attributes);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        Button button = (Button) findViewById(16909673);
        if (button != null) {
            button.setOnClickListener(onClickListener);
        }
    }
}
