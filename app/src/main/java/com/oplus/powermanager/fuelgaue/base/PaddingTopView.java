package com.oplus.powermanager.fuelgaue.base;

import android.view.View;

/* loaded from: classes.dex */
public class PaddingTopView {
    private View view;

    public PaddingTopView(View view) {
        this.view = view;
    }

    public int getPaddingTop() {
        View view = this.view;
        if (view != null) {
            return view.getPaddingTop();
        }
        return 0;
    }

    public void setPaddingTop(int i10) {
        View view = this.view;
        if (view != null) {
            view.setPadding(view.getPaddingLeft(), i10, this.view.getPaddingRight(), this.view.getPaddingBottom());
        }
    }
}
