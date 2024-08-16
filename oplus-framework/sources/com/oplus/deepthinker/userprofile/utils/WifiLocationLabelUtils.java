package com.oplus.deepthinker.userprofile.utils;

import android.content.ContentValues;
import android.os.Bundle;
import com.oplus.deepthinker.platform.server.FrameworkInvokeDelegate;
import com.oplus.deepthinker.sdk.aidl.proton.userprofile.WifiLocationLabel;
import com.oplus.deepthinker.sdk.common.utils.SDKLog;
import com.oplus.deepthinker.userprofile.UserProfileConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class WifiLocationLabelUtils {
    private static final int CONFIG_KEY_MIN_LEN = 4;
    private static final int CONFIG_KEY_PREFIX_LEN = 2;
    private static final int CONFIG_KEY_START_INDEX = 0;
    private static final int CONFIG_KEY_SUFFIX_LEN = 2;
    private static final int INDEX_ACCURACY = 3;
    private static final int INDEX_BSSID = 1;
    private static final int INDEX_LATITUDE = 1;
    private static final int INDEX_LONGITUDE = 0;
    private static final int INDEX_POINT_SIZE = 2;
    private static final int INDEX_RADIUS = 4;
    private static final int INDEX_SSID = 0;
    private static final int LABEL_RESULT_LENGTH = 7;
    private static final String LABEL_SPLITER = "-";
    private static final String TAG = "WifiLocationLabelUtils";

    public static List<WifiLocationLabel> getWifiLocationLabels(FrameworkInvokeDelegate delegate) {
        List<WifiLocationLabel> result = new ArrayList<>();
        Bundle query = new Bundle();
        query.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        query.putInt("label_id", UserProfileConstants.LabelId.WIFI_LOCATION.getValue());
        query.putInt("data_cycle", 30);
        List<ContentValues> queryList = InnerUtils.queryUserProfile(delegate, query);
        if (queryList == null || queryList.isEmpty()) {
            SDKLog.w(TAG, "getWifiLocationLabel result is null or Empty");
            return result;
        }
        for (ContentValues cv : queryList) {
            try {
                String labelResult = cv.getAsString(UserProfileConstants.COLUMN_LABEL_RESULT);
                SDKLog.w(TAG, "labelResult = " + labelResult);
                result.addAll(parseWifiLabels(labelResult));
            } catch (Exception e) {
                SDKLog.e(TAG, "getWifiLocationLabels Exception: " + e.getMessage());
            }
        }
        return result;
    }

    private static List<WifiLocationLabel> parseWifiLabels(String labelResult) {
        List<WifiLocationLabel> result = new ArrayList<>();
        try {
            try {
                JSONArray jsonArray = new JSONArray(labelResult);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    double longitude = jsonObject.getDouble("mLongitude");
                    double latitude = jsonObject.getDouble("mLatitude");
                    int radius = jsonObject.getInt("mRadius");
                    int clusterPointsNum = jsonObject.getInt("mClusterPointsNum");
                    double accuracy = jsonObject.getDouble("mAccuracy");
                    Set<String> bssidSet = strToSet(jsonObject.getString("mBssidSet"));
                    Set<String> ssidSet = strToSet(jsonObject.getString("mSsidSet"));
                    Set<String> configKey = strToSet(jsonObject.getString("mConfigName"));
                    int survivalTime = jsonObject.getInt("mSurvivalTime");
                    WifiLocationLabel wifiLocationLabel = new WifiLocationLabel(longitude, latitude, radius, clusterPointsNum, accuracy, bssidSet, ssidSet, configKey);
                    wifiLocationLabel.setSurvivalTime(survivalTime);
                    result.add(wifiLocationLabel);
                }
            } catch (Exception e) {
                e = e;
                SDKLog.e(TAG, "parseWifiLabels Exception: " + e.getMessage());
                return result;
            }
        } catch (Exception e2) {
            e = e2;
        }
        return result;
    }

    private static Set<String> strToSet(String str) {
        SDKLog.d(TAG, "strToSet: input str=" + str);
        Set<String> result = new HashSet<>();
        StringBuilder sb = new StringBuilder(str.replaceAll("\\\\\"", "\""));
        if (sb.length() < 4) {
            SDKLog.d(TAG, "strToSet: str.len < CONFIG_KEY_MIN_LEN, return");
            return result;
        }
        sb.delete(0, 2);
        sb.delete(sb.length() - 2, sb.length());
        String[] strArray = sb.toString().split("\",\"");
        for (String element : strArray) {
            result.add(element);
        }
        SDKLog.d(TAG, "strToSet: output str=" + result.toString());
        return result;
    }
}
