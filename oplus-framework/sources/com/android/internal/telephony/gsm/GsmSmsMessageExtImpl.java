package com.android.internal.telephony.gsm;

import android.os.SystemProperties;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.OplusRlog;
import com.android.internal.telephony.OplusTelephonyProperties;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.gsm.SmsMessage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/* loaded from: classes.dex */
public class GsmSmsMessageExtImpl implements IGsmSmsMessageExt {
    private static final int NUMBER_0x04 = 4;
    private static final String TAG = "GsmSmsMessageExtImpl";
    private static final String amlPrefix = "415193d9";
    private static final int amlPrefixLength = 5;
    private Phone mPhone = null;

    public GsmSmsMessageExtImpl(Object base) {
        OplusRlog.Rlog.d(TAG, "GsmSmsMessageExtImpl new");
    }

    public SmsMessage.SubmitPdu oemGetSubmitPdu(ByteArrayOutputStream bo, SmsMessage.SubmitPdu ret, byte[] data) {
        boolean isCtIms = false;
        try {
            isCtIms = SystemProperties.get(OplusTelephonyProperties.PROPERTY_CT_AUTOREG_IMS_PROP, "0").equals("1");
            OplusRlog.Rlog.d(TAG, "isCtIms=" + isCtIms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (!isCtIms) {
            return null;
        }
        if (data.length > 140) {
            OplusRlog.Rlog.e(TAG, "SMS data message may only contain 140 bytes");
            return null;
        }
        if (bo == null) {
            return ret;
        }
        bo.write(4);
        bo.write(data.length);
        bo.write(data, 0, data.length);
        ret.encodedMessage = bo.toByteArray();
        try {
            SystemProperties.set(OplusTelephonyProperties.PROPERTY_CT_AUTOREG_IMS_PROP, "0");
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        return ret;
    }

    public String getUserDataOem8bit(byte[] mUserData, byte[] mPdu, int mCur, int byteCount) {
        if (mUserData == null || mPdu == null) {
            return null;
        }
        try {
            CharsetDecoder decoderUtf8 = StandardCharsets.UTF_8.newDecoder();
            int len = mUserData.length;
            byte[] userDataUtf8 = new byte[len];
            System.arraycopy(mUserData, 0, userDataUtf8, 0, len);
            ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(userDataUtf8);
            String ret = decoderUtf8.decode(byteBufferUtf8).toString();
            return ret;
        } catch (Exception e) {
            OplusRlog.Rlog.d(TAG, "UTF_8 parse error");
            try {
                String ret2 = GsmAlphabet.gsm8BitUnpackedToString(mPdu, mCur, byteCount);
                return ret2;
            } catch (Exception e2) {
                OplusRlog.Rlog.d(TAG, "GSM_8 parse error");
                return null;
            }
        }
    }

    public boolean isEnable8BitMtSms() {
        return true;
    }

    public Boolean isAmlDataMessage(byte[] data) {
        try {
            if (data.length < 5) {
                return false;
            }
            StringBuilder buf = new StringBuilder(10);
            for (int i = 0; i < 5; i++) {
                buf.append(String.format("%02x", new Integer(data[i] & 255)));
            }
            String userText = buf.toString();
            if (userText.startsWith(amlPrefix)) {
                OplusRlog.Rlog.d(TAG, " is Aml Data ");
                return true;
            }
            OplusRlog.Rlog.d(TAG, " is not Aml Data ");
            return false;
        } catch (Exception ex) {
            OplusRlog.Rlog.d(TAG, "message decode error " + ex);
            return false;
        }
    }
}
