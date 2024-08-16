package com.android.internal.telephony.gsm;

import android.content.res.Resources;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsMessageBase;

/* loaded from: classes.dex */
public class OplusSmsMessage {
    private static final String LOG_TAG = "gsm-OplusSmsMessage";
    private static final int NUMBER_0x04 = 4;

    public static GsmAlphabet.TextEncodingDetails calculateLengthOem(CharSequence msgBody, boolean use7bitOnly, int encodingType) {
        Rlog.d(LOG_TAG, "use7bitOnly=" + use7bitOnly + ",encodingType=" + encodingType);
        CharSequence newMsgBody = null;
        Resources r = Resources.getSystem();
        if (use7bitOnly || r.getBoolean(17891830)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(msgBody, false);
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = msgBody;
        }
        GsmAlphabet.TextEncodingDetails ted = GsmAlphabet.countGsmSeptets(newMsgBody, use7bitOnly);
        if (encodingType == 3) {
            Rlog.d(LOG_TAG, "input mode is unicode");
            ted = null;
        }
        if (ted == null) {
            Rlog.d(LOG_TAG, "7-bit encoding fail");
            return SmsMessageBase.calcUnicodeEncodingDetails(newMsgBody);
        }
        return ted;
    }
}
