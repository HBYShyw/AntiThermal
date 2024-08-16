package com.oplus.deepthinker.sdk.app.aidl.proton.commuting;

import android.util.ArrayMap;
import java.util.Map;

/* loaded from: classes.dex */
public class LeaveHomeConstants {
    public static final int INDEX_10_PERCENT = 1;
    public static final int INDEX_20_PERCENT = 2;
    public static final int INDEX_30_PERCENT = 3;
    public static final int INDEX_40_PERCENT = 4;
    public static final int INDEX_50_PERCENT = 5;
    public static final int INDEX_60_PERCENT = 6;
    public static final int INDEX_70_PERCENT = 7;
    public static final int INDEX_80_PERCENT = 8;
    public static final int INDEX_90_PERCENT = 9;
    public static final int INDEX_MAX_VALUE = 10;
    public static final int INDEX_MIN_VALUE = 0;
    private static final double PERCENTILE_0 = 0.0d;
    private static final double PERCENTILE_10 = 0.1d;
    private static final double PERCENTILE_100 = 1.0d;
    private static final double PERCENTILE_20 = 0.2d;
    private static final double PERCENTILE_30 = 0.3d;
    private static final double PERCENTILE_40 = 0.4d;
    private static final double PERCENTILE_50 = 0.5d;
    private static final double PERCENTILE_60 = 0.6d;
    private static final double PERCENTILE_70 = 0.7d;
    private static final double PERCENTILE_80 = 0.8d;
    private static final double PERCENTILE_90 = 0.9d;
    private static final Map<Integer, Double> PERCENTILE_MAP;

    static {
        ArrayMap arrayMap = new ArrayMap();
        PERCENTILE_MAP = arrayMap;
        arrayMap.put(0, Double.valueOf(0.0d));
        arrayMap.put(1, Double.valueOf(PERCENTILE_10));
        arrayMap.put(2, Double.valueOf(PERCENTILE_20));
        arrayMap.put(3, Double.valueOf(PERCENTILE_30));
        arrayMap.put(4, Double.valueOf(PERCENTILE_40));
        arrayMap.put(5, Double.valueOf(PERCENTILE_50));
        arrayMap.put(6, Double.valueOf(PERCENTILE_60));
        arrayMap.put(7, Double.valueOf(PERCENTILE_70));
        arrayMap.put(8, Double.valueOf(PERCENTILE_80));
        arrayMap.put(9, Double.valueOf(PERCENTILE_90));
        arrayMap.put(10, Double.valueOf(PERCENTILE_100));
    }

    public static Map<Integer, Double> getPercentileMap() {
        return PERCENTILE_MAP;
    }
}
