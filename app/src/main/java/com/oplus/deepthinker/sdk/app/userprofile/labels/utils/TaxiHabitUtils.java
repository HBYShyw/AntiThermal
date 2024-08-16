package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.m0;
import ma.u;
import sd.j;
import sd.v;
import za.k;

/* compiled from: TaxiHabitUtils.kt */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J(\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020!0\u001c2\u0012\u0010\"\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020!0\u001cH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u001d\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00040\u001c¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\u000fX\u0082T¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/userprofile/labels/utils/TaxiHabitUtils;", "", "()V", "CODE_BAI_DU_MAP", "", "CODE_CAO_CAO_TRAVEL", "CODE_DIDI", "CODE_GAO_DE_MAP", "CODE_MEI_TUAN", "CODE_MEI_TUAN_CAR", "CODE_RU_QI_TRAVEL", "CODE_SHOU_QI_CAR", "CODE_T3_GO", "CODE_UNKNOWN", "COMMA", "", "PKG_BAI_DU_MAP", "PKG_CAO_CAO_TRAVEL", "PKG_DIDI", "PKG_GAO_DE_MAP", "PKG_IDX", "PKG_MEI_TUAN", "PKG_MEI_TUAN_CAR", "PKG_RU_QI_TRAVEL", "PKG_SHOU_QI_CAR", "PKG_SIZE", "PKG_T3_GO", "TAXI_MAPPING", "", "getTAXI_MAPPING", "()Ljava/util/Map;", "UNKNOWN", "convertToMapping", "", "map", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class TaxiHabitUtils {
    public static final int CODE_BAI_DU_MAP = 1;
    public static final int CODE_CAO_CAO_TRAVEL = 5;
    public static final int CODE_DIDI = 8;
    public static final int CODE_GAO_DE_MAP = 2;
    public static final int CODE_MEI_TUAN = 9;
    public static final int CODE_MEI_TUAN_CAR = 3;
    public static final int CODE_RU_QI_TRAVEL = 7;
    public static final int CODE_SHOU_QI_CAR = 4;
    public static final int CODE_T3_GO = 6;
    public static final int CODE_UNKNOWN = -1;
    private static final String COMMA = ",";
    public static final TaxiHabitUtils INSTANCE = new TaxiHabitUtils();
    public static final String PKG_BAI_DU_MAP = "11161003";
    public static final String PKG_CAO_CAO_TRAVEL = "cn.caocaokeji.user";
    public static final String PKG_DIDI = "com.sdu.didi.psnger";
    public static final String PKG_GAO_DE_MAP = "16111004";
    private static final int PKG_IDX = 1;
    public static final String PKG_MEI_TUAN = "com.sankuai.meituan";
    public static final String PKG_MEI_TUAN_CAR = "com.meituan.qcs.c.android";
    public static final String PKG_RU_QI_TRAVEL = "com.ruqi.travel";
    public static final String PKG_SHOU_QI_CAR = "com.ichinait.gbpassenger";
    private static final int PKG_SIZE = 2;
    public static final String PKG_T3_GO = "com.t3go.passenger";
    private static final Map<String, Integer> TAXI_MAPPING;
    private static final String UNKNOWN = "unknown";

    static {
        Map<String, Integer> l10;
        l10 = m0.l(u.a(PKG_MEI_TUAN, 9), u.a(PKG_SHOU_QI_CAR, 4), u.a(PKG_CAO_CAO_TRAVEL, 5), u.a(PKG_T3_GO, 6), u.a(PKG_RU_QI_TRAVEL, 7), u.a(PKG_DIDI, 8), u.a(PKG_MEI_TUAN_CAR, 3), u.a(PKG_BAI_DU_MAP, 1), u.a(PKG_GAO_DE_MAP, 2));
        TAXI_MAPPING = l10;
    }

    private TaxiHabitUtils() {
    }

    public static final Map<Integer, Double> convertToMapping(Map<String, Double> map) {
        List q02;
        Map<Integer, Double> i10;
        k.e(map, "map");
        if (map.isEmpty()) {
            i10 = m0.i();
            return i10;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            double doubleValue = entry.getValue().doubleValue();
            q02 = v.q0(entry.getKey(), new String[]{","}, false, 0, 6, null);
            Integer num = TAXI_MAPPING.get(q02.size() == 2 ? new j("\\s").c((CharSequence) q02.get(1), "") : UNKNOWN);
            int intValue = num == null ? -1 : num.intValue();
            linkedHashMap.put(Integer.valueOf(intValue), Double.valueOf(((Number) linkedHashMap.getOrDefault(Integer.valueOf(intValue), Double.valueOf(UserProfileInfo.Constant.NA_LAT_LON))).doubleValue() + doubleValue));
        }
        return linkedHashMap;
    }

    public final Map<String, Integer> getTAXI_MAPPING() {
        return TAXI_MAPPING;
    }
}
