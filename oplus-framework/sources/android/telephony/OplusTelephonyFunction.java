package android.telephony;

import android.content.Context;
import android.os.OplusPropertyList;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
public class OplusTelephonyFunction {
    public static final int CONCATENATED_8_BIT_REFERENCE_LENGTH = 5;
    private static final boolean DBG = false;
    public static final int PORT_ADDRESS_16_REFERENCE_LENGTH = 6;
    private static String PROJECT_MULTISIM_CONFIG = SystemProperties.get("persist.radio.multisim.config", "unknow");
    private static final String TAG = "OplusTelephonyFunction";

    public static byte[][] divideDataMessage(byte[] data) {
        int copyLen;
        int dataLen = data.length;
        int bytePreSeg = 133;
        if (dataLen > 133) {
            bytePreSeg = 133 - 5;
        }
        int total = ((dataLen + bytePreSeg) - 1) / bytePreSeg;
        int remainLen = dataLen;
        int count = 0;
        byte[][] dataSegList = new byte[total];
        while (remainLen > 0) {
            if (remainLen > bytePreSeg) {
                copyLen = bytePreSeg;
            } else {
                copyLen = remainLen;
            }
            remainLen -= copyLen;
            dataSegList[count] = new byte[copyLen];
            System.arraycopy(data, count * bytePreSeg, dataSegList[count], 0, copyLen);
            count++;
        }
        return dataSegList;
    }

    public static ByteBuffer createBufferWithNativeByteOrder(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        buf.order(ByteOrder.nativeOrder());
        return buf;
    }

    public static int getMinMatch() {
        String region = SystemProperties.get(OplusPropertyList.PROPERTY_REGION, OplusPropertyList.OPLUS_VERSION);
        return region.equalsIgnoreCase(OplusPropertyList.OPLUS_VERSION) ? 11 : 7;
    }

    private static int countGsmSeptets(CharSequence s, boolean throwsException, int rfu) throws EncodeException {
        int sz = s.length();
        int count = 0;
        for (int charIndex = 0; charIndex < sz; charIndex++) {
            count += GsmAlphabet.countGsmSeptets(s.charAt(charIndex), throwsException);
        }
        return count;
    }

    public static byte[] stringToGsm8BitOrUCSPackedForADN(String s) {
        if (s == null) {
            return null;
        }
        try {
            int septets = countGsmSeptets(s, true, 1);
            byte[] ret = new byte[septets];
            GsmAlphabet.stringToGsm8BitUnpackedField(s, ret, 0, ret.length);
            return ret;
        } catch (EncodeException e) {
            try {
                byte[] temp = s.getBytes("utf-16be");
                byte[] ret2 = new byte[temp.length + 1];
                ret2[0] = Byte.MIN_VALUE;
                System.arraycopy(temp, 0, ret2, 1, temp.length);
                return ret2;
            } catch (UnsupportedEncodingException ex) {
                Log.e(TAG, "unsurport encoding.", ex);
                return null;
            }
        }
    }

    public static int dmAutoRegisterSmsOrigPort(String address) {
        int index;
        if (TextUtils.isEmpty(address) || -1 == (index = address.indexOf(":"))) {
            return 0;
        }
        try {
            int origPort = Integer.parseInt(address.substring(index + 1).toString());
            return origPort;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String dmAutoRegisterSmsAddress(String address) {
        int index;
        if (!TextUtils.isEmpty(address) && -1 != (index = address.indexOf(":"))) {
            if (index == 0) {
                return null;
            }
            try {
                return address.substring(0, index).toString();
            } catch (IndexOutOfBoundsException e) {
                return address;
            }
        }
        return address;
    }

    public static String oplusGetPlmnOverride(Context context, String operatorNumic, ServiceState ss) {
        String plmn;
        if (context == null) {
            Log.d(TAG, "oplusGetPlmnOverride context == null");
            return null;
        }
        if (!TextUtils.isEmpty(operatorNumic) && (plmn = context.getString(context.getResources().getIdentifier("mccmnc" + operatorNumic, "string", "com.android.phone"))) != null) {
            return plmn;
        }
        if (ss == null) {
            return null;
        }
        return ss.getOperatorAlpha();
    }

    public static boolean oplusGetSingleSimCard() {
        boolean singleSimCard = isOpenMarketSingleSimCard();
        if (!singleSimCard) {
            return isOperatorSingleSimCard();
        }
        return singleSimCard;
    }

    private static boolean isOpenMarketSingleSimCard() {
        return isOperatorSingleSimCard();
    }

    private static boolean isOperatorSingleSimCard() {
        if ("ssss".equals(PROJECT_MULTISIM_CONFIG) || "ss".equals(PROJECT_MULTISIM_CONFIG)) {
            return true;
        }
        return false;
    }

    public static boolean oplusIsSimLockedEnabledTH() {
        if (isOperatorSingleSimCard()) {
            return true;
        }
        return false;
    }
}
