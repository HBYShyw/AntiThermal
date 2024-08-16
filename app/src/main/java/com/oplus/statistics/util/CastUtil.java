package com.oplus.statistics.util;

import com.oplus.statistics.g0;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CastUtil {
    private static final String TAG = "CastUtil";

    public static JSONObject map2JsonObject(Map<String, String> map) {
        JSONObject jSONObject = new JSONObject();
        if (map != null && !map.isEmpty()) {
            try {
                for (String str : map.keySet()) {
                    jSONObject.put(str, map.get(str));
                }
            } catch (Exception e10) {
                LogUtil.e(TAG, new g0(e10));
            }
        }
        return jSONObject;
    }
}
