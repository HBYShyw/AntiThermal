package com.android.ims;

import android.content.Context;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusSesImsManagerExt {
    private static final String KEY_SES_MODIFY_IMS_CAP_BOOL = "config_ses_modify_ims_cap_bool";
    private static final String KEY_USING_IMS_PROVISION_AS_CAP_BOOL = "using_ims_provision_as_cap_bool";
    private static final String TAG = "OplusSesImsManagerExt";
    private Context mContext;

    public OplusSesImsManagerExt(Context context) {
        this.mContext = context;
    }

    public boolean getBooleanCarrierConfig(String key, int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        int subId = -1;
        if (subIds != null && subIds.length >= 1) {
            subId = subIds[0];
        }
        PersistableBundle b = null;
        CarrierConfigManager configManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        if (configManager != null) {
            b = configManager.getConfigForSubId(subId);
        }
        if (b != null) {
            return b.getBoolean(key);
        }
        return CarrierConfigManager.getDefaultConfig().getBoolean(key);
    }

    public boolean isFeatureEnabledByPlatform(Context context, int feature, int phoneId) {
        Log.d(TAG, "feature:" + feature + ", phoneId:" + phoneId);
        if (feature == 0 || feature == 2) {
            return isFeatureProvisionedOnDevice(feature, phoneId);
        }
        return true;
    }

    public boolean isFeatureProvisionedOnDevice(int feature, int phoneId) {
        if (!isMccMncReady(phoneId) || ((!isEntitlementEnabled() || !getBooleanCarrierConfig(KEY_SES_MODIFY_IMS_CAP_BOOL, phoneId)) && !getBooleanCarrierConfig(KEY_USING_IMS_PROVISION_AS_CAP_BOOL, phoneId))) {
            Log.d(TAG, "isFeatureProvisionedOnDevice not enabled");
            return true;
        }
        ImsManager imsManager = ImsManager.getInstance(this.mContext, phoneId);
        boolean result = false;
        if (imsManager != null) {
            try {
                ImsConfig imsConfig = imsManager.getConfigInterface();
                if (imsConfig != null) {
                    if (feature == 0) {
                        int value = imsConfig.getProvisionedValue(10);
                        Log.d(TAG, "VoLTE provisioned value = " + value);
                        if (value == 1 || !getBooleanCarrierConfig("carrier_ses_volte_available_bool", phoneId)) {
                            result = true;
                        }
                    } else {
                        int value2 = imsConfig.getProvisionedValue(28);
                        Log.d(TAG, "VoWifi provisioned value = " + value2);
                        if (value2 == 1 || !getBooleanCarrierConfig("carrier_ses_wfc_ims_available_bool", phoneId)) {
                            result = true;
                        }
                    }
                }
            } catch (ImsException e) {
                Log.e(TAG, "Volte not updated, ImsConfig null");
                e.printStackTrace();
            } catch (RuntimeException e2) {
                Log.e(TAG, "ImsConfig not ready");
                e2.printStackTrace();
            }
        } else {
            Log.e(TAG, "Volte not updated, ImsManager null");
        }
        Log.d(TAG, "isFeatureProvisionedOnDevice returns " + result);
        return result;
    }

    private static int getSubIdUsingPhoneId(int phoneId) {
        int[] values = SubscriptionManager.getSubId(phoneId);
        if (values == null || values.length <= 0) {
            return SubscriptionManager.getDefaultSubscriptionId();
        }
        Log.d(TAG, "getSubIdUsingPhoneId:" + values[0] + ", phoneId:" + phoneId);
        return values[0];
    }

    private static boolean isMccMncReady(int phoneId) {
        String mccMnc = getSimOperatorNumericForPhone(phoneId);
        if (TextUtils.isEmpty(mccMnc)) {
            Log.d(TAG, "sim not ready");
            return false;
        }
        return true;
    }

    private static String getSimOperatorNumericForPhone(int phoneId) {
        int subId = getSubIdUsingPhoneId(phoneId);
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            Log.d(TAG, "Is Invalid Subscription id.");
            return "";
        }
        String mccMnc = TelephonyManager.getDefault().getSimOperatorNumeric(subId);
        return mccMnc;
    }

    private static boolean isEntitlementEnabled() {
        boolean isEntitlementEnabled = 1 == SystemProperties.getInt("persist.vendor.entitlement_enabled", 1);
        Log.d(TAG, "isEntitlementEnabled:" + isEntitlementEnabled);
        return isEntitlementEnabled;
    }

    private static boolean isDisplayLog() {
        String logDbg = SystemProperties.get("persist.radio.entitlement.dbglog", "-1");
        return logDbg.equals("0");
    }
}
