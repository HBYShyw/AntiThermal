package com.android.internal.telephony.util;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;

/* loaded from: classes.dex */
public class OplusCarrierConfigHelper {
    public static final String KEY_OPLUS_CARRIER_IS_SUPPORT_ECHOLOCATE = "oplus_carrier_is_support_echolocate";
    public static final String KEY_OPLUS_CARRIER_IS_SUPPORT_UST_CARRIER_CONFIG = "oplus_carrier_is_support_ust_carrier_config";
    public static final String KEY_OPLUS_CARRIER_IS_USA = "oplus_carrier_is_usa";
    public static final String KEY_OPLUS_CARRIER_IS_UST = "oplus_carrier_is_ust";
    public static final String KEY_OPLUS_CARRIER_IS_USV = "oplus_carrier_is_usv";
    public static final String KEY_OPLUS_CARRIER_NAME = "oplus_carrier_name";

    public static PersistableBundle getCarrierConfigBundle(Context context, int phoneId) {
        CarrierConfigManager carrierConfigManager;
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds != null && subIds.length > 0 && (carrierConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config")) != null) {
            return carrierConfigManager.getConfigForSubId(subIds[0]);
        }
        return null;
    }

    public static boolean getBoolean(Context context, int phoneId, String key) {
        return getBoolean(context, phoneId, key, false);
    }

    public static boolean getBoolean(Context context, int phoneId, String key, boolean defaultValue) {
        PersistableBundle bundle = getCarrierConfigBundle(context, phoneId);
        if (bundle != null) {
            return bundle.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    public static int getInt(Context context, int phoneId, String key) {
        return getInt(context, phoneId, key, 0);
    }

    public static int getInt(Context context, int phoneId, String key, int defaultValue) {
        PersistableBundle bundle = getCarrierConfigBundle(context, phoneId);
        if (bundle != null) {
            return bundle.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public static String getString(Context context, int phoneId, String key) {
        return getString(context, phoneId, key, null);
    }

    public static String getString(Context context, int phoneId, String key, String defaultValue) {
        PersistableBundle bundle = getCarrierConfigBundle(context, phoneId);
        if (bundle != null) {
            return bundle.getString(key, defaultValue);
        }
        return defaultValue;
    }

    public static boolean isUsvCard(Context context, int phoneId) {
        return getBoolean(context, phoneId, KEY_OPLUS_CARRIER_IS_USV);
    }

    public static boolean isUstCard(Context context, int phoneId) {
        return getBoolean(context, phoneId, KEY_OPLUS_CARRIER_IS_UST);
    }

    public static boolean isUsaCard(Context context, int phoneId) {
        return getBoolean(context, phoneId, KEY_OPLUS_CARRIER_IS_USA);
    }

    public static boolean isSupportEcholocate(Context context, int phoneId) {
        return getBoolean(context, phoneId, KEY_OPLUS_CARRIER_IS_SUPPORT_ECHOLOCATE);
    }

    public static boolean isSupportUstCarrierConfig(Context context, int phoneId) {
        return getBoolean(context, phoneId, KEY_OPLUS_CARRIER_IS_SUPPORT_UST_CARRIER_CONFIG);
    }
}
