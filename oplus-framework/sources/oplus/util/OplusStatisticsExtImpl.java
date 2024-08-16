package oplus.util;

import android.content.Context;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusStatisticsExtImpl implements IOplusStatisticsExt {
    private static final String IMPL_OPLUS = "com.oplus.util.OplusStatisticsImpl";
    private static final String TAG = "OplusStatistics--";
    private static volatile OplusStatisticsExtImpl sInstance = null;

    public OplusStatisticsExtImpl(Object obj) {
    }

    public static OplusStatisticsExtImpl getInstance(Object obj) {
        if (sInstance == null) {
            synchronized (OplusStatisticsExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new OplusStatisticsExtImpl(obj);
                }
            }
        }
        return sInstance;
    }

    public void onCommon(Context context, String logTag, String eventId, Map<String, String> logMap, boolean uploadNow) {
        OplusStatistics.onCommon(context, logTag, eventId, logMap, uploadNow);
    }

    public void onCommon(Context context, String appIdStr, String logTag, String eventId, Map<String, String> logMap, boolean uploadNow) {
        OplusStatistics.onCommon(context, appIdStr, logTag, eventId, logMap, uploadNow);
    }

    public void onCommon(Context context, int appId, String logTag, String eventId, Map<String, String> logMap, boolean uploadNow) {
        OplusStatistics.onCommon(context, appId, logTag, eventId, logMap, uploadNow);
    }

    public void onCommon(Context context, String logTag, String eventId, List<Map<String, String>> mapList, boolean uploadNow) {
        OplusStatistics.onCommon(context, logTag, eventId, mapList, uploadNow);
    }

    public void onCommon(Context context, String appIdStr, String logTag, String eventId, List<Map<String, String>> mapList, boolean uploadNow) {
        OplusStatistics.onCommon(context, appIdStr, logTag, eventId, mapList, uploadNow);
    }

    public void onCommon(Context context, int appId, String logTag, String eventId, List<Map<String, String>> mapList, boolean uploadNow) {
        OplusStatistics.onCommon(context, appId, logTag, eventId, mapList, uploadNow);
    }

    public void onCommon(Context context, String logTag, String eventId, Map<String, String> logMap, boolean uploadNow, int flagSendTo) {
        OplusStatistics.onCommon(context, logTag, eventId, logMap, uploadNow, flagSendTo);
    }

    public void onCommonSync(Context context, String logTag, String eventId, Map<String, String> logMap, boolean upLoadNow) {
        OplusStatistics.onCommonSync(context, logTag, eventId, logMap, upLoadNow);
    }
}
