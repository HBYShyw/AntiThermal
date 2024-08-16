package com.oplus.powermanager.fuelgaue.base;

import android.view.ViewGroup;

/* loaded from: classes.dex */
public class ViewGroupUtil {
    private static final String TAG = "ViewGroupUtil";

    public static void setChildVisibility(ViewGroup viewGroup, int i10) {
        if (viewGroup == null) {
            return;
        }
        for (int i11 = 0; i11 < viewGroup.getChildCount(); i11++) {
            viewGroup.getChildAt(i11).setVisibility(i10);
        }
    }

    public static void setVisibilityWithChild(ViewGroup viewGroup, int i10) {
        if (viewGroup == null) {
            return;
        }
        viewGroup.setVisibility(i10);
        setChildVisibility(viewGroup, i10);
    }
}
