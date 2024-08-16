package android.telephony;

import android.content.res.Resources;
import android.text.TextUtils;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.OplusFeature;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsMessageBase;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class OplusSmsMessage {
    private static final String LOG_TAG = "OplusSmsMessage";
    private static final int NUMBER_10 = 10;

    public static ArrayList<String> oemFragmentText(String text, int subid) {
        return SmsMessage.fragmentText(text, subid);
    }

    public static ArrayList<String> oemFragmentText(String text, int subid, int encodingType) {
        GsmAlphabet.TextEncodingDetails ted;
        int limit;
        int nextPos;
        int udhLength;
        boolean isCdma = SmsMessage.oemUseCdmaFormatForMoSms(subid);
        if (isCdma) {
            ted = com.android.internal.telephony.cdma.OplusSmsMessage.calculateLengthOem(text, false, true, encodingType);
        } else {
            ted = com.android.internal.telephony.gsm.OplusSmsMessage.calculateLengthOem(text, encodingType == 1, encodingType);
        }
        Rlog.d("sms", "ted.codeUnitSize=" + ted.codeUnitSize + " isCdma=" + isCdma + " subid=" + subid);
        if (ted.codeUnitSize == 1) {
            if (ted.languageTable != 0 && ted.languageShiftTable != 0) {
                udhLength = 7;
            } else if (ted.languageTable != 0 || ted.languageShiftTable != 0) {
                udhLength = 4;
            } else {
                udhLength = 0;
            }
            if (ted.msgCount > 1) {
                udhLength += 6;
            }
            if (udhLength != 0) {
                udhLength++;
            }
            limit = 160 - udhLength;
        } else if (ted.msgCount > 1) {
            limit = 134;
            if (!SmsMessage.hasEmsSupport() && ted.msgCount < 10) {
                limit = 134 - 2;
            }
        } else {
            limit = 140;
        }
        String newMsgBody = null;
        Resources r = Resources.getSystem();
        if (encodingType == 1 || r.getBoolean(17891830)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(text, isCdma);
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = text;
        }
        int pos = 0;
        int textLen = newMsgBody.length();
        ArrayList<String> result = new ArrayList<>(ted.msgCount);
        while (pos < textLen) {
            if (ted.codeUnitSize == 1) {
                if (isCdma && ted.msgCount == 1) {
                    nextPos = Math.min(limit, textLen - pos) + pos;
                } else {
                    int nextPos2 = ted.languageTable;
                    nextPos = GsmAlphabet.findGsmSeptetLimitIndex(newMsgBody, pos, limit, nextPos2, ted.languageShiftTable);
                }
            } else {
                nextPos = SmsMessageBase.findNextUnicodePosition(pos, limit, newMsgBody);
            }
            if (nextPos <= pos || nextPos > textLen) {
                Rlog.e(LOG_TAG, "fragmentText failed (" + pos + " >= " + nextPos + " or " + nextPos + " >= " + textLen + ")");
                break;
            }
            result.add(newMsgBody.substring(pos, nextPos));
            pos = nextPos;
        }
        Rlog.d(LOG_TAG, "size=" + result.size());
        return result;
    }

    public static int[] calculateLengthOem(String messageBody, boolean use7bitOnly, int encodingType) {
        return calculateLengthOem((CharSequence) messageBody, use7bitOnly, encodingType);
    }

    public static int[] calculateLengthOem(String messageBody, boolean use7bitOnly, int subId, int encodingType) {
        return calculateLengthOem((CharSequence) messageBody, use7bitOnly, subId, encodingType);
    }

    public static int[] calculateLengthOem(CharSequence msgBody, boolean use7bitOnly, int encodingType) {
        GsmAlphabet.TextEncodingDetails ted;
        if (!OplusFeature.OPLUS_FEATURE_SMS_7BIT16BIT || (encodingType != 1 && encodingType != 3)) {
            return SmsMessage.calculateLength(msgBody, use7bitOnly);
        }
        boolean isCdma = SmsMessage.oemUseCdmaFormatForMoSms();
        if (isCdma) {
            ted = com.android.internal.telephony.cdma.OplusSmsMessage.calculateLengthOem(msgBody, use7bitOnly, true, encodingType);
        } else {
            ted = com.android.internal.telephony.gsm.OplusSmsMessage.calculateLengthOem(msgBody, use7bitOnly, encodingType);
        }
        int[] ret = {ted.msgCount, ted.codeUnitCount, ted.codeUnitsRemaining, ted.codeUnitSize, ted.languageTable, ted.languageShiftTable};
        return ret;
    }

    public static int[] calculateLengthOem(CharSequence msgBody, boolean use7bitOnly, int subId, int encodingType) {
        GsmAlphabet.TextEncodingDetails ted;
        if (!OplusFeature.OPLUS_FEATURE_SMS_7BIT16BIT || (encodingType != 1 && encodingType != 3)) {
            return SmsMessage.calculateLength(msgBody, use7bitOnly, subId);
        }
        boolean isCdma = SmsMessage.oemUseCdmaFormatForMoSms(subId);
        if (isCdma) {
            ted = com.android.internal.telephony.cdma.OplusSmsMessage.calculateLengthOem(msgBody, use7bitOnly, true, encodingType);
        } else {
            ted = com.android.internal.telephony.gsm.OplusSmsMessage.calculateLengthOem(msgBody, use7bitOnly, encodingType);
        }
        int[] ret = {ted.msgCount, ted.codeUnitCount, ted.codeUnitsRemaining, ted.codeUnitSize, ted.languageTable, ted.languageShiftTable};
        return ret;
    }
}
