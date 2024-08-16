package com.oplus.telephony;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.OplusTelephonyManager;
import java.util.Objects;

/* loaded from: classes.dex */
public class TelephonyFeatureManagerExt {
    private static final String TAG = "TelephonyFeatureManagerExt";
    public static final String TELEPHONY_FEATURE_EXT = "telephony_feature_ext";
    private Context mContext;

    public TelephonyFeatureManagerExt(Context context) {
        Context appContext = context.getApplicationContext();
        if (appContext != null && Objects.equals(context.getAttributionTag(), appContext.getAttributionTag())) {
            this.mContext = appContext;
        } else {
            this.mContext = context;
        }
    }

    public static TelephonyFeatureManagerExt from(Context context) {
        return (TelephonyFeatureManagerExt) context.getSystemService(TELEPHONY_FEATURE_EXT);
    }

    public PersistableBundle getPlmnConfigForSlotId(int slotId) {
        OplusTelephonyManager oplusTelephonyManager = OplusTelephonyManager.getInstance(this.mContext);
        return oplusTelephonyManager.getPlmnConfigForSlotId(slotId);
    }

    public PersistableBundle getConfigForSubId(int subId) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        return carrierConfigManager.getConfigForSubId(subId);
    }
}
