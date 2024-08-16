package com.android.internal.telephony;

import android.os.OplusPropertyList;
import android.provider.oplus.Telephony;
import com.android.internal.telephony.OperatorInfo;

/* loaded from: classes.dex */
public class OplusOperatorInfo extends OperatorInfo {
    private String mNetworktype;
    private String networktype;
    private String statestring;

    public OplusOperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, OperatorInfo.State state, String networktype) {
        super(operatorAlphaLong, operatorAlphaShort, operatorNumeric, state);
        this.mNetworktype = networktype;
    }

    public OplusOperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, String stateString, String networktype) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, OplusRILStateToState(stateString), networktype);
        this.statestring = stateString;
    }

    public String getNetworktype() {
        return this.mNetworktype;
    }

    public String getStatestring() {
        return this.statestring;
    }

    public void setNetworktype(String type) {
        this.mNetworktype = type;
    }

    public void setNetworktypeFromOperatorNumeric() {
        String strAlphaLong = getOperatorAlphaLong();
        if (strAlphaLong.contains("+")) {
            String networktype = strAlphaLong.substring(strAlphaLong.indexOf("+"));
            setNetworktype(networktype);
        }
    }

    public static OperatorInfo.State OplusRILStateToState(String s) {
        if (s.equals(OplusPropertyList.UNKNOWN)) {
            return OperatorInfo.State.UNKNOWN;
        }
        if (s.equals("available")) {
            return OperatorInfo.State.AVAILABLE;
        }
        if (s.equals(Telephony.Carriers.CURRENT)) {
            return OperatorInfo.State.CURRENT;
        }
        if (s.equals("forbidden")) {
            return OperatorInfo.State.FORBIDDEN;
        }
        throw new RuntimeException("RIL impl error: Invalid network state '" + s + "'");
    }

    public String toString() {
        return "OperatorInfo " + getOperatorAlphaLong() + "/" + getOperatorAlphaShort() + "/" + getOperatorNumeric() + "/" + this.statestring + "/" + this.mNetworktype;
    }
}
