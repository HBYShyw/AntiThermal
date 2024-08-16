package com.oplus.powermanager.fuelgaue.base;

import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class HeightView {
    private View view;

    public HeightView(View view) {
        this.view = view;
    }

    public int getHeight() {
        View view = this.view;
        if (view != null) {
            return view.getHeight();
        }
        return 0;
    }

    public void setHeight(int i10) {
        View view = this.view;
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = i10;
            this.view.setLayoutParams(layoutParams);
        }
    }
}
