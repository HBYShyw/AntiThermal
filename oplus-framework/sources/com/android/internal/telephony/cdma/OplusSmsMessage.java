package com.android.internal.telephony.cdma;

import android.content.res.Resources;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.OplusTelephonyProperties;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.cdma.SmsMessage;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.OplusBearerData;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.HexDump;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class OplusSmsMessage {
    private static final String LOGGABLE_TAG = "cdma-OplusSmsMessage";
    private static final String LOG_TAG = "cdma-OplusSmsMessage";
    private static final int NUMBER_244 = 244;
    private static final int NUMBER_255 = 255;

    public static GsmAlphabet.TextEncodingDetails calculateLengthOem(CharSequence messageBody, boolean use7bitOnly, boolean isEntireMsg, int encodingType) {
        CharSequence newMsgBody = null;
        Resources r = Resources.getSystem();
        if (r.getBoolean(17891830)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(messageBody, true);
            Rlog.d("cdma-OplusSmsMessage", "search calculateLengthCDMA for help!");
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = messageBody;
        }
        return OplusBearerData.calcTextEncodingDetailsOem(newMsgBody, use7bitOnly, isEntireMsg, encodingType);
    }

    public static SmsMessage.SubmitPdu getSubmitPduOem(String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader, int priority, int validityPeriod, int encodingType, boolean use7BitAscii) {
        int validityPeriod2 = validityPeriod;
        if (message != null && destAddr != null) {
            if (destAddr.isEmpty()) {
                Log.e("cdma-OplusSmsMessage", "getSubmitPduOem, destination address is empty. do nothing.");
                return null;
            }
            if (message.isEmpty()) {
                Log.e("cdma-OplusSmsMessage", "getSubmitPduOem, message text is empty. do nothing.");
                return null;
            }
            if (validityPeriod2 > 244 && validityPeriod2 <= 255) {
                validityPeriod2 = 244;
            }
            UserData uData = new UserData();
            uData.payloadStr = message;
            uData.userDataHeader = smsHeader;
            if (encodingType == 1) {
                uData.msgEncoding = use7BitAscii ? 2 : 9;
            } else if (encodingType == 2) {
                uData.msgEncoding = 0;
            } else {
                uData.msgEncoding = 4;
            }
            uData.msgEncodingSet = true;
            return privateGetSubmitPduOem(destAddr, statusReportRequested, uData, 0L, validityPeriod2, priority);
        }
        Log.e("cdma-OplusSmsMessage", "getSubmitPduOem, null sms text or destination address. do nothing.");
        return null;
    }

    private static SmsMessage.SubmitPdu privateGetSubmitPduOem(String destAddrStr, boolean statusReportRequested, UserData userData, long timeStamp, int validityPeriod, int priority) {
        CdmaSmsAddress destAddr = CdmaSmsAddress.parse(PhoneNumberUtils.cdmaCheckAndProcessPlusCodeForSms(destAddrStr));
        if (destAddr == null) {
            return null;
        }
        if (destAddr.numberOfDigits > 36) {
            Rlog.d("cdma-OplusSmsMessage", "number of digit exceeds the SMS_ADDRESS_MAX");
            return null;
        }
        BearerData bearerData = new BearerData();
        bearerData.messageType = 2;
        bearerData.messageId = SmsMessage.getNextMessageId();
        bearerData.deliveryAckReq = statusReportRequested;
        bearerData.userAckReq = false;
        bearerData.readAckReq = false;
        bearerData.reportReq = false;
        if (validityPeriod >= 0) {
            bearerData.validityPeriodRelativeSet = true;
            bearerData.validityPeriodRelative = validityPeriod;
        } else {
            bearerData.validityPeriodRelativeSet = false;
        }
        if (priority >= 0 && priority <= 3) {
            bearerData.priorityIndicatorSet = true;
            bearerData.priority = priority;
        }
        bearerData.userData = userData;
        byte[] encodedBearerData = BearerData.encode(bearerData);
        if (encodedBearerData == null) {
            return null;
        }
        if (Rlog.isLoggable("cdma-OplusSmsMessage", 2)) {
            Rlog.d("cdma-OplusSmsMessage", "MO (encoded) BearerData = " + bearerData);
            Rlog.d("cdma-OplusSmsMessage", "MO raw BearerData = '" + HexDump.toHexString(encodedBearerData) + "'");
        }
        int teleservice = (!bearerData.hasUserDataHeader || userData.msgEncoding == 2) ? 4098 : 4101;
        SmsEnvelope envelope = new SmsEnvelope();
        envelope.messageType = 0;
        envelope.teleService = teleservice;
        envelope.destAddress = destAddr;
        envelope.bearerReply = 1;
        envelope.bearerData = encodedBearerData;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(envelope.teleService);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.write(destAddr.digitMode);
            dos.write(destAddr.numberMode);
            dos.write(destAddr.ton);
            dos.write(destAddr.numberPlan);
            dos.write(destAddr.numberOfDigits);
            dos.write(destAddr.origBytes, 0, destAddr.origBytes.length);
            dos.write(0);
            dos.write(0);
            dos.write(0);
            dos.write(encodedBearerData.length);
            dos.write(encodedBearerData, 0, encodedBearerData.length);
            dos.close();
            SmsMessage.SubmitPdu pdu = new SmsMessage.SubmitPdu();
            pdu.encodedMessage = baos.toByteArray();
            pdu.encodedScAddress = null;
            return pdu;
        } catch (IOException ex) {
            Rlog.e("cdma-OplusSmsMessage", "creating SubmitPdu failed: " + ex);
            return null;
        }
    }

    public static boolean oemGetSubmitPdu() {
        try {
            boolean isCtIms = SystemProperties.get(OplusTelephonyProperties.PROPERTY_CT_AUTOREG_IMS_PROP, "0").equals("1");
            Rlog.d("cdma-OplusSmsMessage", "isCtIms=" + isCtIms);
            if (isCtIms) {
                SystemProperties.set(OplusTelephonyProperties.PROPERTY_CT_AUTOREG_IMS_PROP, "0");
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
