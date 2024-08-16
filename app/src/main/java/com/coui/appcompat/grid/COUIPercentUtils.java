package com.coui.appcompat.grid;

import android.content.Context;
import android.content.res.Resources;
import com.support.appcompat.R$dimen;

/* compiled from: COUIPercentUtils.java */
@Deprecated
/* renamed from: com.coui.appcompat.grid.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIPercentUtils {
    @Deprecated
    public static float a(float f10, int i10, int i11, int i12, Context context) {
        int dimensionPixelOffset;
        if (i10 <= 0 || i10 > i11) {
            return f10;
        }
        boolean z10 = i12 == 1 || i12 == 2;
        if (i12 == 2) {
            dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.grid_guide_column_card_margin_start);
        } else {
            dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R$dimen.grid_guide_column_default_margin_start);
        }
        int i13 = dimensionPixelOffset * 2;
        Resources resources = context.getResources();
        int i14 = R$dimen.grid_guide_column_gap;
        return ((((f10 - i13) - ((i11 - 1) * resources.getDimensionPixelOffset(i14))) / i11) * i10) + (context.getResources().getDimensionPixelOffset(i14) * Math.max(i10 - 1, 0)) + (z10 ? i13 : 0);
    }
}
