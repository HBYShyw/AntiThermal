package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.AppTypePreferenceLabel;
import i6.IDeepThinkerBridge;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AppTypePreferenceLabelUtils {
    private static final String TAG = "AppTypePreferenceLabelUtils";

    public static AppTypePreferenceLabel getRecentPreference(IDeepThinkerBridge iDeepThinkerBridge) {
        List<ContentValues> queryShortTermLabel = queryShortTermLabel(iDeepThinkerBridge);
        if (queryShortTermLabel != null && queryShortTermLabel.size() >= 1) {
            ContentValues contentValues = queryShortTermLabel.get(0);
            try {
                return new AppTypePreferenceLabel(parseResult(contentValues.getAsString(UserProfileConstants.COLUMN_LABEL_RESULT)), parseDetail(contentValues.getAsString(UserProfileConstants.COLUMN_DETAIL)));
            } catch (Exception e10) {
                SDKLog.e(TAG, "on assemble AppTypePreferenceLabel", e10);
            }
        }
        return null;
    }

    private static Map<Integer, AppTypePreferenceLabel.Detail> parseDetail(String str) {
        HashMap hashMap = new HashMap();
        if (TextUtils.isEmpty(str)) {
            return hashMap;
        }
        try {
            for (String str2 : str.split(";")) {
                if (!TextUtils.isEmpty(str2)) {
                    String[] split = str2.split(InnerUtils.EQUAL);
                    if (split.length == 2) {
                        int parseInt = Integer.parseInt(split[0]);
                        String[] split2 = split[1].split(",");
                        if (split2.length == 2) {
                            AppTypePreferenceLabel.Detail detail = new AppTypePreferenceLabel.Detail();
                            detail.useCount = Integer.parseInt(split2[0]);
                            detail.useTime = Long.parseLong(split2[1]);
                            hashMap.put(Integer.valueOf(parseInt), detail);
                        }
                    }
                }
            }
        } catch (Exception e10) {
            SDKLog.e(TAG, "parse detail error ", e10);
        }
        return hashMap;
    }

    private static List<Integer> parseResult(String str) {
        LinkedList linkedList = new LinkedList();
        if (TextUtils.isEmpty(str)) {
            return linkedList;
        }
        try {
            for (String str2 : str.split(",")) {
                linkedList.add(Integer.valueOf(Integer.parseInt(str2)));
            }
        } catch (Exception e10) {
            SDKLog.e(TAG, "parse result error", e10);
        }
        return linkedList;
    }

    private static List<ContentValues> queryShortTermLabel(IDeepThinkerBridge iDeepThinkerBridge) {
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        bundle.putInt("label_id", UserProfileConstants.LabelId.APP_PREFERENCE.getValue());
        bundle.putInt("data_cycle", 1);
        return InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
    }
}
