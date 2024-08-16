package com.oplus.wrapper.telephony;

import android.content.ContentResolver;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import java.util.List;

/* loaded from: classes.dex */
public class TelephonyManager {
    private final android.telephony.TelephonyManager mTelephonyManager;

    public TelephonyManager(android.telephony.TelephonyManager telephonyManager) {
        this.mTelephonyManager = telephonyManager;
    }

    public String getSimOperatorNameForPhone(int phoneId) {
        return this.mTelephonyManager.getSimOperatorNameForPhone(phoneId);
    }

    public boolean isMultiSimEnabled() {
        return this.mTelephonyManager.isMultiSimEnabled();
    }

    public static int getIntAtIndex(ContentResolver cr, String name, int index) throws Settings.SettingNotFoundException {
        return android.telephony.TelephonyManager.getIntAtIndex(cr, name, index);
    }

    public boolean hasIccCard(int slotIndex) {
        return this.mTelephonyManager.hasIccCard(slotIndex);
    }

    public int getSimCount() {
        return this.mTelephonyManager.getSimCount();
    }

    public static String getTelephonyProperty(int phoneId, String property, String defaultVal) {
        return android.telephony.TelephonyManager.getTelephonyProperty(phoneId, property, defaultVal);
    }

    public String getNetworkOperatorForPhone(int phoneId) {
        return this.mTelephonyManager.getNetworkOperatorForPhone(phoneId);
    }

    public String getSimOperatorNumericForPhone(int phoneId) {
        return this.mTelephonyManager.getSimOperatorNumericForPhone(phoneId);
    }

    public static String getSimCountryIso(int subId) {
        return android.telephony.TelephonyManager.getSimCountryIso(subId);
    }

    public int getCurrentPhoneTypeForSlot(int slotIndex) {
        return this.mTelephonyManager.getCurrentPhoneTypeForSlot(slotIndex);
    }

    public String getIccAuthentication(int subId, int appType, int authType, String data) {
        return this.mTelephonyManager.getIccAuthentication(subId, appType, authType, data);
    }

    public boolean setRoamingOverride(List<String> gsmRoamingList, List<String> gsmNonRoamingList, List<String> cdmaRoamingList, List<String> cdmaNonRoamingList) {
        return this.mTelephonyManager.setRoamingOverride(gsmRoamingList, gsmNonRoamingList, cdmaRoamingList, cdmaNonRoamingList);
    }

    public int getPreferredNetworkType(int subId) {
        return this.mTelephonyManager.getPreferredNetworkType(subId);
    }

    public int getSlotIndex() {
        return this.mTelephonyManager.getSlotIndex();
    }

    @Deprecated
    public int getCallStateForSlot(int slotIndex) {
        return 0;
    }

    public int getDataNetworkType(int subId) {
        return this.mTelephonyManager.getDataNetworkType(subId);
    }

    public String getSimOperator(int subId) {
        return this.mTelephonyManager.getSimOperator(subId);
    }

    public String getSubscriberId(int subId) {
        return this.mTelephonyManager.getSubscriberId(subId);
    }

    public boolean setPreferredNetworkType(int subId, int networkType) {
        return this.mTelephonyManager.setPreferredNetworkType(subId, networkType);
    }

    public boolean getDataEnabled() {
        return this.mTelephonyManager.getDataEnabled();
    }

    public boolean getDataEnabled(int subId) {
        return this.mTelephonyManager.getDataEnabled(subId);
    }

    public void setDataEnabled(int subId, boolean enable) {
        this.mTelephonyManager.setDataEnabled(subId, enable);
    }

    public String getVoiceMailNumber(int subId) {
        return this.mTelephonyManager.getVoiceMailNumber(subId);
    }

    public boolean isVolteAvailable() {
        return this.mTelephonyManager.isVolteAvailable();
    }

    public boolean isWifiCallingAvailable() {
        return this.mTelephonyManager.isWifiCallingAvailable();
    }

    public boolean isImsRegistered(int subId) {
        return this.mTelephonyManager.isImsRegistered(subId);
    }

    /* loaded from: classes.dex */
    public enum MultiSimVariants {
        DSDS(TelephonyManager.MultiSimVariants.DSDS),
        DSDA(TelephonyManager.MultiSimVariants.DSDA),
        TSTS(TelephonyManager.MultiSimVariants.TSTS),
        UNKNOWN(TelephonyManager.MultiSimVariants.UNKNOWN);

        private TelephonyManager.MultiSimVariants mOrigin;

        MultiSimVariants(TelephonyManager.MultiSimVariants origin) {
            this.mOrigin = origin;
        }
    }

    public MultiSimVariants getMultiSimConfiguration() {
        TelephonyManager.MultiSimVariants simConfig = this.mTelephonyManager.getMultiSimConfiguration();
        for (MultiSimVariants var : MultiSimVariants.values()) {
            if (var.mOrigin == simConfig) {
                return var;
            }
        }
        return MultiSimVariants.UNKNOWN;
    }

    public String getImsPrivateUserIdentity() {
        return this.mTelephonyManager.getImsPrivateUserIdentity();
    }

    public int getCallState(int subId) {
        return this.mTelephonyManager.getCallState(subId);
    }

    public android.telephony.ServiceState getServiceStateForSubscriber(int subId) {
        return this.mTelephonyManager.getServiceStateForSubscriber(subId);
    }

    public static String getNetworkTypeName(int type) {
        return android.telephony.TelephonyManager.getNetworkTypeName(type);
    }

    public String getSimSerialNumber(int subId) {
        return this.mTelephonyManager.getSimSerialNumber(subId);
    }
}
