package com.oplus.statistics.agent;

import android.content.Context;
import com.oplus.statistics.data.PeriodDataBean;
import com.oplus.statistics.data.SettingKeyBean;
import com.oplus.statistics.data.SettingKeyDataBean;
import com.oplus.statistics.g0;
import com.oplus.statistics.record.ProxyRecorder;
import com.oplus.statistics.util.LogUtil;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class StaticPeriodDataRecord extends BaseAgent {
    private static final String TAG = "StaticPeriodDataRecord";

    public static JSONArray list2JsonObject(List<SettingKeyBean> list) {
        JSONArray jSONArray = new JSONArray();
        if (list != null && !list.isEmpty()) {
            try {
                for (SettingKeyBean settingKeyBean : list) {
                    if (settingKeyBean != null) {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put(SettingKeyBean.SETTING_KEY, settingKeyBean.getSettingKey());
                        jSONObject.put(SettingKeyBean.HTTP_POST_KEY, settingKeyBean.getHttpPostKey());
                        jSONObject.put(SettingKeyBean.METHOD_NAME, settingKeyBean.getMethodName());
                        jSONObject.put(SettingKeyBean.DEFAULE_VALUE, settingKeyBean.getDefaultValue());
                        jSONArray.put(jSONObject);
                    }
                }
            } catch (Exception e10) {
                LogUtil.e(TAG, new g0(e10));
            }
        }
        return jSONArray;
    }

    public static void updateData(Context context, PeriodDataBean periodDataBean) {
        ProxyRecorder.getInstance().addTrackEvent(context, periodDataBean);
    }

    public static void updateSettingKey(Context context, SettingKeyDataBean settingKeyDataBean) {
        ProxyRecorder.getInstance().addTrackEvent(context, settingKeyDataBean);
    }

    public static void updateSettingKeyList(Context context, SettingKeyDataBean settingKeyDataBean) {
        ProxyRecorder.getInstance().addTrackEvent(context, settingKeyDataBean);
    }
}
