package com.oplus.internal.telephony.gsm;

import android.os.SystemProperties;
import android.provider.oplus.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;

/* loaded from: classes.dex */
public class SuppCrssNotification {
    public static final int CRSS_CALLED_LINE_ID_PREST = 1;
    public static final int CRSS_CALLING_LINE_ID_PREST = 2;
    public static final int CRSS_CALL_WAITING = 0;
    public static final int CRSS_CONNECTED_LINE_ID_PREST = 3;
    public static final boolean SDBG = !SystemProperties.get("ro.build.type").equals(Telephony.Carriers.USER);
    public String alphaid;
    public int cli_validity;
    public int code;
    public String number;
    public int type;

    public String toString() {
        return super.toString() + " CRSS Notification: code: " + this.code + " \"" + Rlog.pii(SDBG, PhoneNumberUtils.stringFromStringAndTOA(this.number, this.type)) + "\" " + this.alphaid + " cli_validity: " + this.cli_validity;
    }
}
