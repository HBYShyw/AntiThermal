package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.NotificationLabel;
import i6.IDeepThinkerBridge;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class NotificationLabelUtils {
    private static final String TAG = "NotificationLabelUtils";

    public static Map<String, NotificationLabel.Details> getPkgDetail(IDeepThinkerBridge iDeepThinkerBridge) {
        HashMap hashMap = new HashMap();
        List<ContentValues> queryNotificationLabel = queryNotificationLabel(iDeepThinkerBridge);
        if (queryNotificationLabel != null && !queryNotificationLabel.isEmpty()) {
            for (ContentValues contentValues : queryNotificationLabel) {
                try {
                    String asString = contentValues.getAsString(UserProfileConstants.COLUMN_PKG_NAME);
                    NotificationLabel.Details parseDetailString = NotificationLabel.Details.parseDetailString(contentValues.getAsString(UserProfileConstants.COLUMN_DETAIL));
                    if (!TextUtils.isEmpty(asString) && parseDetailString != null) {
                        hashMap.put(asString, parseDetailString);
                    }
                } catch (Exception e10) {
                    SDKLog.e(TAG, "getPkgDetail Exception: " + e10.getMessage());
                }
            }
            SDKLog.d(TAG, "getPkgDetail result size: " + hashMap.size());
            return hashMap;
        }
        SDKLog.w(TAG, "getPkgResultMap result is null or Empty");
        return null;
    }

    public static Map<String, Integer> getPkgResultMap(IDeepThinkerBridge iDeepThinkerBridge) {
        HashMap hashMap = new HashMap();
        List<ContentValues> queryNotificationLabel = queryNotificationLabel(iDeepThinkerBridge);
        if (queryNotificationLabel != null && !queryNotificationLabel.isEmpty()) {
            for (ContentValues contentValues : queryNotificationLabel) {
                try {
                    String asString = contentValues.getAsString(UserProfileConstants.COLUMN_PKG_NAME);
                    Integer asInteger = contentValues.getAsInteger(UserProfileConstants.COLUMN_LABEL_RESULT);
                    if (!TextUtils.isEmpty(asString) && asInteger != null) {
                        hashMap.put(asString, asInteger);
                    }
                } catch (Exception e10) {
                    SDKLog.e(TAG, "getPkgResultMap Exception: " + e10.getMessage());
                }
            }
            SDKLog.d(TAG, "getPkgResultMap result size: " + hashMap.size());
            return hashMap;
        }
        SDKLog.w(TAG, "getPkgResultMap result is null or Empty");
        return hashMap;
    }

    private static List<ContentValues> queryNotificationLabel(IDeepThinkerBridge iDeepThinkerBridge) {
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 1);
        bundle.putInt("label_id", UserProfileConstants.LabelId.APP_NOTIFICATION.getValue());
        bundle.putInt("data_cycle", 30);
        return InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
    }
}
