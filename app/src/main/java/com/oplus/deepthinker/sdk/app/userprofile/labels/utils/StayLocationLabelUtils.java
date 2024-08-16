package com.oplus.deepthinker.sdk.app.userprofile.labels.utils;

import android.content.ContentValues;
import android.os.Bundle;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import com.oplus.deepthinker.sdk.app.userprofile.labels.StayLocationLabel;
import i6.IDeepThinkerBridge;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StayLocationLabelUtils {
    private static final int INDEX_KEY_LOCATION_ONE = 0;
    private static final int INDEX_KEY_LOCATION_TWO = 1;
    private static final String TAG = "StayLocationLabelUtils";

    public static List<StayLocationLabel> getStayLocationLabels(IDeepThinkerBridge iDeepThinkerBridge) {
        ArrayList arrayList = new ArrayList();
        Bundle bundle = new Bundle();
        bundle.putInt(UserProfileConstants.KEY_LABEL_TYPE, 0);
        bundle.putInt("label_id", UserProfileConstants.LabelId.RESIDENCE.getValue());
        bundle.putInt("data_cycle", 30);
        List<ContentValues> queryUserProfile = InnerUtils.queryUserProfile(iDeepThinkerBridge, bundle);
        if (queryUserProfile != null && !queryUserProfile.isEmpty()) {
            for (ContentValues contentValues : queryUserProfile) {
                try {
                    String asString = contentValues.getAsString(UserProfileConstants.COLUMN_LABEL_RESULT);
                    String asString2 = contentValues.getAsString(UserProfileConstants.COLUMN_DETAIL);
                    SDKLog.w(TAG, "labelResult = " + asString + " , labelDetail = " + asString2);
                    if (asString != null && !asString.isEmpty()) {
                        for (String str : asString.split(";")) {
                            StayLocationLabel parseResidenceLabel = StayLocationLabel.parseResidenceLabel(str);
                            if (parseResidenceLabel != null) {
                                arrayList.add(parseResidenceLabel);
                            }
                        }
                    }
                    if (asString2 != null && !asString2.isEmpty()) {
                        String[] split = asString2.split(";");
                        StayLocationLabel parseResidenceLabel2 = StayLocationLabel.parseResidenceLabel(split[0]);
                        if (parseResidenceLabel2 != null) {
                            parseResidenceLabel2.setTag(100);
                            arrayList.add(parseResidenceLabel2);
                        }
                        StayLocationLabel parseResidenceLabel3 = StayLocationLabel.parseResidenceLabel(split[1]);
                        if (parseResidenceLabel3 != null) {
                            parseResidenceLabel3.setTag(101);
                            arrayList.add(parseResidenceLabel3);
                        }
                    }
                } catch (Exception e10) {
                    SDKLog.e(TAG, "getResidenceLabels Exception: " + e10.getMessage());
                }
            }
            return arrayList;
        }
        SDKLog.w(TAG, "getResidenceLabels query is null or Empty");
        return arrayList;
    }
}
