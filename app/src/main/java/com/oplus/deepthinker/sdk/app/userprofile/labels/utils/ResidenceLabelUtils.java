package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.ResidenceLabel;
import i6.IDeepThinkerBridge;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ResidenceLabelUtils {
    private static final int DATA_CYCLE = 30;
    public static final int RESIDENCE_LOCATION_DAY_RATIO_LEVEL_HIGH = 3;
    public static final int RESIDENCE_LOCATION_DAY_RATIO_LEVEL_LOW = 1;
    public static final int RESIDENCE_LOCATION_DAY_RATIO_LEVEL_MEDIUM = 2;
    public static final int RESIDENCE_STAY_DURATION_THRESHOLD_LEVEL_HIGH = 3;
    public static final int RESIDENCE_STAY_DURATION_THRESHOLD_LEVEL_LOW = 1;
    public static final int RESIDENCE_STAY_DURATION_THRESHOLD_LEVEL_MEDIUM = 2;
    private static final String TAG = "ResidenceLabelUtils";

    public static List<ResidenceLabel> getCompanyLabels(IDeepThinkerBridge iDeepThinkerBridge) {
        ArrayList arrayList = new ArrayList();
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        bundle.putInt("label_id", UserProfileConstants.LabelId.COMPANY.getValue());
        bundle.putInt("data_cycle", 30);
        List<ContentValues> queryUserProfile = InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
        if (queryUserProfile != null && !queryUserProfile.isEmpty()) {
            Iterator<ContentValues> it = queryUserProfile.iterator();
            while (it.hasNext()) {
                String asString = it.next().getAsString(UserProfileConstants.COLUMN_LABEL_RESULT);
                if (asString != null && !asString.isEmpty()) {
                    arrayList.addAll(ResidenceLabel.parseResidenceLabels(asString));
                }
            }
            return arrayList;
        }
        SDKLog.w(TAG, "getCompanyLabels query is null or Empty");
        return arrayList;
    }

    public static List<ResidenceLabel> getHomeLabels(IDeepThinkerBridge iDeepThinkerBridge) {
        ArrayList arrayList = new ArrayList();
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        bundle.putInt("label_id", UserProfileConstants.LabelId.HOME.getValue());
        bundle.putInt("data_cycle", 30);
        List<ContentValues> queryUserProfile = InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
        if (queryUserProfile != null && !queryUserProfile.isEmpty()) {
            Iterator<ContentValues> it = queryUserProfile.iterator();
            while (it.hasNext()) {
                String asString = it.next().getAsString(UserProfileConstants.COLUMN_LABEL_RESULT);
                if (asString != null && !asString.isEmpty()) {
                    arrayList.addAll(ResidenceLabel.parseResidenceLabels(asString));
                }
            }
            return arrayList;
        }
        SDKLog.w(TAG, "getHomeLabels query is null or Empty");
        return arrayList;
    }

    public static List<ResidenceLabel> getResidenceLabels(IDeepThinkerBridge iDeepThinkerBridge) {
        ArrayList arrayList = new ArrayList();
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        bundle.putInt("label_id", UserProfileConstants.LabelId.RESIDENCE_V2.getValue());
        bundle.putInt("data_cycle", 30);
        List<ContentValues> queryUserProfile = InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
        if (queryUserProfile != null && !queryUserProfile.isEmpty()) {
            Iterator<ContentValues> it = queryUserProfile.iterator();
            while (it.hasNext()) {
                String asString = it.next().getAsString(UserProfileConstants.COLUMN_LABEL_RESULT);
                if (asString != null && !asString.isEmpty()) {
                    arrayList.addAll(ResidenceLabel.parseResidenceLabels(asString));
                }
            }
            return arrayList;
        }
        SDKLog.w(TAG, "getResidenceLabels query is null or Empty");
        return arrayList;
    }

    public static String getResidenceTypeString(int i10, int i11) {
        if (i10 == 0 || i11 == 0) {
            return null;
        }
        return (i10 != 1 ? i10 != 2 ? i10 != 3 ? "" : "H" : "M" : "L") + i11;
    }
}
