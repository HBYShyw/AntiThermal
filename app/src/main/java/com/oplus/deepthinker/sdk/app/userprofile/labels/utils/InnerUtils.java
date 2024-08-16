package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.IProviderFeature;
import com.oplus.deepthinker.sdk.app.SDKLog;
import i6.IDeepThinkerBridge;
import java.util.List;

/* loaded from: classes.dex */
public class InnerUtils {
    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String EQUAL = "=";
    public static final String SEMICOLON = ";";
    private static final String TAG = "UserLabelUtils";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<ContentValues> queryUserProfile(IDeepThinkerBridge iDeepThinkerBridge, Bundle bundle) {
        if (iDeepThinkerBridge == null) {
            return null;
        }
        try {
            Bundle call = iDeepThinkerBridge.call(IProviderFeature.FEATURE_ABILITY_USERPROFILE, IProviderFeature.USERPROFILE_QUERY, bundle);
            if (call != null) {
                return call.getParcelableArrayList(IProviderFeature.USERPROFILE_QUERY_RESULT);
            }
            return null;
        } catch (Throwable th) {
            SDKLog.e(TAG, "queryUserProfile: " + bundle.toString(), th);
            return null;
        }
    }
}
