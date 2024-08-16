package com.oplus.powermanager.fuelgaue.base;

import android.content.Context;
import android.util.AttributeSet;
import com.coui.appcompat.grid.COUIPercentWidthListView;

/* loaded from: classes.dex */
public class PercentWidthListView extends COUIPercentWidthListView {
    public PercentWidthListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void setPadding(int i10, int i11, int i12, int i13) {
        if (i10 == getPaddingStart() || i12 == getPaddingEnd()) {
            return;
        }
        super.setPadding(i10, i11, i12, i13);
    }

    public PercentWidthListView(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
    }
}
