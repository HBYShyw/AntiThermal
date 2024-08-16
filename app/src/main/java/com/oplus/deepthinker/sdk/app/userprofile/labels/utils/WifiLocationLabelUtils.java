package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.WifiLocationLabel;
import i6.IDeepThinkerBridge;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class WifiLocationLabelUtils {
    private static final String TAG = "WifiLocationLabelUtils";

    public static List<WifiLocationLabel> getWifiLocationLabels(IDeepThinkerBridge iDeepThinkerBridge) {
        ArrayList arrayList = new ArrayList();
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        bundle.putInt("label_id", UserProfileConstants.LabelId.WIFI_LOCATION.getValue());
        bundle.putInt("data_cycle", 30);
        List<ContentValues> queryUserProfile = InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
        if (queryUserProfile != null && !queryUserProfile.isEmpty()) {
            Iterator<ContentValues> it = queryUserProfile.iterator();
            while (it.hasNext()) {
                try {
                    arrayList.addAll(parseWifiLabels(it.next().getAsString(UserProfileConstants.COLUMN_LABEL_RESULT)));
                } catch (Exception e10) {
                    SDKLog.e(TAG, "getWifiLocationLabels Exception: " + e10.getMessage());
                }
            }
            return arrayList;
        }
        SDKLog.w(TAG, "getWifiLocationLabel result is null or Empty");
        return arrayList;
    }

    private static List<WifiLocationLabel> parseWifiLabels(String str) {
        SDKLog.d(TAG, "parseWifiLabels labelResult=" + str);
        ArrayList arrayList = new ArrayList();
        try {
            Iterator<JsonElement> it = new JsonParser().parse(str).getAsJsonArray().iterator();
            while (it.hasNext()) {
                WifiLocationLabel wifiLocationLabel = (WifiLocationLabel) new Gson().fromJson(it.next(), WifiLocationLabel.class);
                if (wifiLocationLabel != null) {
                    SDKLog.w(TAG, "parseWifiLabels, wifiLocationLabel: " + wifiLocationLabel.toString());
                    arrayList.add(wifiLocationLabel);
                }
            }
        } catch (Exception e10) {
            SDKLog.e(TAG, "parseWifiLabels Exception: " + e10.getMessage());
        }
        return arrayList;
    }
}
