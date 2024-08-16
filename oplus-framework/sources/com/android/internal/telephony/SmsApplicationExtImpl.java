package com.android.internal.telephony;

import android.util.Log;

/* loaded from: classes.dex */
public class SmsApplicationExtImpl implements ISmsApplicationExt {
    private static final String LOG_TAG = "SmsApplicationExtImpl";
    private static final int MAIN_USER_ID = 0;
    private static final String[] OEM_PACKAGE_MO_SMS_NOT_SHOW_IN_UI = {"com.color.safecenter", "com.oppo.trafficmonitor", "com.oppo.activation", "com.nxp.wallet.oppo", "com.oppo.yellowpage", "com.oppo.systemhelper", "com.heytap.usercenter", "com.coloros.activation", "com.coloros.findmyphone", "com.oppo.oppopowermonitor", "com.oppo.usercenter", "com.oplus.vip", "com.heytap.vip", "com.oplus.account"};
    private static final String[] OEM_PACKAGE_ALLOW_WRITE_SMS = {"com.oplus.engineernetwork", "com.android.settings"};
    private static int mUserId = 0;

    public SmsApplicationExtImpl(Object base) {
        Log.d(LOG_TAG, "SmsApplicationExtImpl new");
    }

    public boolean shouldWriteMessageForPackage(String packageName) {
        try {
            Log.d(LOG_TAG, "sw pkg=" + packageName);
            String[] strArr = OEM_PACKAGE_MO_SMS_NOT_SHOW_IN_UI;
            if (strArr != null) {
                for (String smsPackage : strArr) {
                    if (smsPackage != null && smsPackage.equals(packageName)) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public String[] oemPackageName() {
        return OEM_PACKAGE_ALLOW_WRITE_SMS;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getUserId() {
        return mUserId;
    }

    public boolean isUserIdEqualMainUserId() {
        return mUserId != 0;
    }
}
