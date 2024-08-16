package com.oplus.grid;

import android.content.Context;

/* loaded from: classes.dex */
public class OplusGridHelper {
    private static final int PADDING_COUNT = 2;

    public static float calculateColumnWidth(Context context, float screenWidth, int useColumn) {
        int totalColumn = context.getResources().getInteger(202178604);
        float gridColumnDefaultMargin = context.getResources().getDimensionPixelSize(201654496);
        float gridColumnDefaultGap = context.getResources().getDimensionPixelSize(201654497);
        float gridSize = ((screenWidth - (2.0f * gridColumnDefaultMargin)) - ((totalColumn - 1) * gridColumnDefaultGap)) / totalColumn;
        float afterWidthSize = (useColumn * gridSize) + (Math.max(useColumn - 1, 0) * gridColumnDefaultGap);
        return afterWidthSize;
    }
}
