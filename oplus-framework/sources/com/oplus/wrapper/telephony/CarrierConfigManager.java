package com.oplus.wrapper.telephony;

import android.os.PersistableBundle;

/* loaded from: classes.dex */
public class CarrierConfigManager {
    public static final String KEY_CARRIER_ALLOW_TRANSFER_IMS_CALL_BOOL = getKeyCarrierAllowTransferImsCallBool();

    public static PersistableBundle getDefaultConfig() {
        return android.telephony.CarrierConfigManager.getDefaultConfig();
    }

    private static String getKeyCarrierAllowTransferImsCallBool() {
        return "carrier_allow_transfer_ims_call_bool";
    }
}
