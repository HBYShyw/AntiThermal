package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.ArrayMap;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import i6.IDeepThinkerBridge;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AppBgVectorLabelUtils {
    private static final int DATA_CYCLE = 7;
    private static final String TAG = "AppBgVectorLabelUtils";

    public static Map<String, Integer> getAppBgVectorLabelResult(IDeepThinkerBridge iDeepThinkerBridge) {
        List<ContentValues> queryLabel = queryLabel(iDeepThinkerBridge);
        if (queryLabel == null) {
            return null;
        }
        ArrayMap arrayMap = new ArrayMap();
        for (ContentValues contentValues : queryLabel) {
            if (contentValues != null) {
                try {
                    arrayMap.put(contentValues.getAsString(UserProfileConstants.COLUMN_PKG_NAME), contentValues.getAsInteger(UserProfileConstants.COLUMN_LABEL_RESULT));
                } catch (Exception e10) {
                    SDKLog.e(TAG, "getAppBgVectorLabelResult", e10);
                }
            }
        }
        return arrayMap;
    }

    private static List<ContentValues> queryLabel(IDeepThinkerBridge iDeepThinkerBridge) {
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 1);
        bundle.putInt("label_id", UserProfileConstants.LabelId.APP_BG_VECTOR.getValue());
        bundle.putInt("data_cycle", 7);
        return InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
    }
}
