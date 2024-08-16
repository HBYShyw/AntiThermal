package com.oplus.deepthinker.userprofile.utils;

import android.content.ContentValues;
import android.os.Bundle;
import com.oplus.deepthinker.platform.server.FrameworkInvokeDelegate;
import com.oplus.deepthinker.sdk.common.utils.SDKLog;
import com.oplus.deepthinker.userprofile.UserProfileConstants;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
class InnerUtils {
    private static final String TAG = "UserLabelUtils";

    InnerUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<ContentValues> queryUserProfile(FrameworkInvokeDelegate delegate, Bundle query) {
        if (delegate == null) {
            return null;
        }
        try {
            Bundle respond = delegate.call(UserProfileConstants.FEATURE_ABILITY_USERPROFILE, UserProfileConstants.USERPROFILE_QUERY, query);
            if (respond == null) {
                return null;
            }
            ArrayList<ContentValues> result = respond.getParcelableArrayList(UserProfileConstants.USERPROFILE_QUERY_RESULT);
            return result;
        } catch (Throwable e) {
            SDKLog.e(TAG, "queryUserProfile: " + query.toString(), e);
            return null;
        }
    }
}
