package com.android.internal.telephony.cdma;

import com.android.internal.telephony.OplusRlog;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;

/* loaded from: classes.dex */
public class CdmaSmsMessageExtImpl implements ICdmaSmsMessageExt {
    private static final byte DESTINATION_ADDRESS = 4;
    private static final String TAG = "CdmaSmsMessageExtImpl";

    public CdmaSmsMessageExtImpl(Object base) {
        OplusRlog.Rlog.d(TAG, "CdmaSmsMessageExtImpl new");
    }

    public void setAddrOEM(SmsEnvelope env, int parameterId, SmsAddress originatingAddress, SmsAddress recipientAddress, CdmaSmsAddress addr) {
        env.origAddress = addr;
        if (parameterId == 4) {
            env.destAddress = addr;
        }
    }
}
