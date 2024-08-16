package com.oplus.wrapper.telephony;

/* loaded from: classes.dex */
public class PhoneNumberUtils {
    public static String cdmaCheckAndProcessPlusCode(String dialStr) {
        return android.telephony.PhoneNumberUtils.cdmaCheckAndProcessPlusCode(dialStr);
    }

    public static boolean isUriNumber(String number) {
        return android.telephony.PhoneNumberUtils.isUriNumber(number);
    }

    public static String getUsernameFromUriNumber(String number) {
        return android.telephony.PhoneNumberUtils.getUsernameFromUriNumber(number);
    }

    public static String extractNetworkPortionAlt(String phoneNumber) {
        return android.telephony.PhoneNumberUtils.extractNetworkPortionAlt(phoneNumber);
    }

    public static boolean isEmergencyNumber(int subId, String number) {
        return android.telephony.PhoneNumberUtils.isEmergencyNumber(subId, number);
    }
}
