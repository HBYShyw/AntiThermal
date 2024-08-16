package com.oplus.statistics.agent;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.statistics.data.AppLogBean;
import com.oplus.statistics.data.DynamicEventBean;
import com.oplus.statistics.data.StaticEventBean;
import com.oplus.statistics.g0;
import com.oplus.statistics.record.ProxyRecorder;
import com.oplus.statistics.storage.PreferenceHandler;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.TimeInfoUtil;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OnEventAgent {
    private static final String TAG = "OnEventAgent";

    public static JSONObject getDynamicEventObject(int i10, String str, Map<String, String> map, Map<String, String> map2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("statID", i10);
            jSONObject.put("clientTime", str);
            getDynamicInfo(jSONObject, map);
            getKVEventInfo(jSONObject, map2);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
        return jSONObject;
    }

    private static void getDynamicInfo(JSONObject jSONObject, Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        try {
            for (String str : map.keySet()) {
                jSONObject.put(str, map.get(str));
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static JSONObject getEventObject(String str, String str2, int i10, String str3, long j10) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("eventID", str);
            jSONObject.put("eventCount", i10);
            jSONObject.put("eventTime", str3);
            if (!TextUtils.isEmpty(str2)) {
                jSONObject.put("eventTag", str2);
            }
            if (j10 != 0) {
                jSONObject.put("duration", j10);
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
        return jSONObject;
    }

    private static void getKVEventInfo(JSONObject jSONObject, Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            for (String str : map.keySet()) {
                jSONObject2.put(str, map.get(str));
            }
            String replaceAll = jSONObject2.toString().replaceAll("\"", "");
            jSONObject.put("eventInfo", replaceAll.substring(1, replaceAll.length() - 1));
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static JSONObject getKVEventObject(String str, Map<String, String> map, String str2, long j10) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("eventID", str);
            jSONObject.put("eventTime", str2);
            if (j10 > 0) {
                jSONObject.put("duration", j10);
            }
            if (map != null && map.size() > 0) {
                for (String str3 : map.keySet()) {
                    jSONObject.put(str3, map.get(str3));
                }
            }
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
        return jSONObject;
    }

    public static JSONObject getStaticLogObject(int i10, String str, String str2, String str3, String str4, Map<String, String> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("statID", i10);
            jSONObject.put("clientTime", str);
            jSONObject.put("setID", str2);
            jSONObject.put("setValue", str3);
            if (!TextUtils.isEmpty(str4)) {
                jSONObject.put("remark", str4);
            }
            getKVEventInfo(jSONObject, map);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
        return jSONObject;
    }

    public static void onDynamicEvent(Context context, int i10, int i11, Map<String, String> map, Map<String, String> map2) {
        recordDynamicEvent(context, i10, i11, TimeInfoUtil.getFormatTime(), map, map2);
    }

    public static void onEvent(Context context, String str, String str2, int i10, long j10) {
        recordEvent(context, str, str2, i10, TimeInfoUtil.getFormatTime(), j10);
    }

    public static void onEventEnd(Context context, String str, String str2) {
        recordEventEnd(context, str, str2, TimeInfoUtil.getCurrentTime());
    }

    public static void onEventStart(Context context, String str, String str2) {
        PreferenceHandler.setEventStart(context, str, str2, TimeInfoUtil.getCurrentTime());
    }

    public static void onKVEvent(Context context, String str, Map<String, String> map, long j10) {
        recordKVEvent(context, str, map, TimeInfoUtil.getFormatTime(), j10);
    }

    public static void onKVEventEnd(Context context, String str, String str2) {
        recordKVEventEnd(context, str, str2, TimeInfoUtil.getCurrentTime());
    }

    public static void onKVEventStart(Context context, String str, Map<String, String> map, String str2) {
        long currentTime = TimeInfoUtil.getCurrentTime();
        PreferenceHandler.setKVEventStart(str, getKVEventObject(str, map, TimeInfoUtil.getFormatTime(currentTime), currentTime).toString(), str2);
    }

    public static void onStaticEvent(Context context, int i10, int i11, String str, String str2, String str3, Map<String, String> map) {
        recordStaticLog(context, i10, i11, TimeInfoUtil.getFormatTime(), str, str2, str3, map);
    }

    private static void recordAppLog(Context context, String str, JSONObject jSONObject) {
        ProxyRecorder.getInstance().addTrackEvent(context, new AppLogBean(context, str, jSONObject.toString()));
    }

    public static void recordDynamicEvent(Context context, int i10, int i11, String str, Map<String, String> map, Map<String, String> map2) {
        recordDynamicEventLog(context, i10, getDynamicEventObject(i11, str, map, map2));
    }

    private static void recordDynamicEventLog(Context context, int i10, JSONObject jSONObject) {
        ProxyRecorder.getInstance().addTrackEvent(context, new DynamicEventBean(context, i10, jSONObject.toString()));
    }

    public static void recordEvent(Context context, String str, String str2, int i10, String str3, long j10) {
        recordAppLog(context, "event", getEventObject(str, str2, i10, str3, j10));
    }

    public static void recordEventEnd(Context context, String str, String str2, long j10) {
        try {
            long eventStart = PreferenceHandler.getEventStart(context, str, str2);
            String formatTime = TimeInfoUtil.getFormatTime(eventStart);
            long j11 = j10 - eventStart;
            if (j11 <= TimeInfoUtil.MILLISECOND_OF_A_WEEK && j11 >= 0) {
                recordAppLog(context, "event", getEventObject(str, str2, 1, formatTime, j11));
                PreferenceHandler.setEventStart(context, str, str2, 0L);
                return;
            }
            PreferenceHandler.setEventStart(context, str, str2, 0L);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void recordKVEvent(Context context, String str, Map<String, String> map, String str2, long j10) {
        recordAppLog(context, "ekv", getKVEventObject(str, map, str2, j10));
    }

    public static void recordKVEventEnd(Context context, String str, String str2, long j10) {
        try {
            String kVEventStart = PreferenceHandler.getKVEventStart(context, str, str2);
            if (TextUtils.isEmpty(kVEventStart)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(kVEventStart);
            long j11 = j10 - jSONObject.getLong("duration");
            if (j11 <= TimeInfoUtil.MILLISECOND_OF_A_WEEK && j11 >= 0) {
                jSONObject.put("duration", j11);
                recordAppLog(context, "ekv", jSONObject);
                PreferenceHandler.setKVEventStart(str, "", str2);
                return;
            }
            PreferenceHandler.setKVEventStart(str, "", str2);
        } catch (Exception e10) {
            LogUtil.e(TAG, new g0(e10));
        }
    }

    public static void recordStaticLog(Context context, int i10, int i11, String str, String str2, String str3, String str4, Map<String, String> map) {
        recordStaticLog(context, i10, getStaticLogObject(i11, str, str2, str3, str4, map));
    }

    private static void recordStaticLog(Context context, int i10, JSONObject jSONObject) {
        ProxyRecorder.getInstance().addTrackEvent(context, new StaticEventBean(context, i10, jSONObject.toString()));
    }
}
