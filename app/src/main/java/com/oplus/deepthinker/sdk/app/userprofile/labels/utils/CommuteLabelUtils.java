package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.CommuteLabel;
import i6.IDeepThinkerBridge;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class CommuteLabelUtils {
    private static final String TAG = "CommuteLabelUtils";

    public static CommuteLabel getCommuteLabel(IDeepThinkerBridge iDeepThinkerBridge) {
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        bundle.putInt("label_id", UserProfileConstants.LabelId.COMMUTE_V2.getValue());
        bundle.putInt("data_cycle", 30);
        List<ContentValues> queryUserProfile = InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
        if (queryUserProfile != null && !queryUserProfile.isEmpty()) {
            Iterator<ContentValues> it = queryUserProfile.iterator();
            while (it.hasNext()) {
                ContentValues next = it.next();
                try {
                    String asString = next.getAsString(UserProfileConstants.COLUMN_LABEL_RESULT);
                    SDKLog.d(TAG, "labelResult = " + asString + " , labelDetail = " + next.getAsString(UserProfileConstants.COLUMN_DETAIL));
                    if (asString != null && !asString.isEmpty()) {
                        return CommuteLabel.parseCommuteLabel(asString);
                    }
                } catch (Exception e10) {
                    SDKLog.e(TAG, "getCommuteLabel Exception: " + e10.getMessage());
                }
            }
            return null;
        }
        SDKLog.w(TAG, "getCommuteLabel query is null or Empty");
        return null;
    }
}
