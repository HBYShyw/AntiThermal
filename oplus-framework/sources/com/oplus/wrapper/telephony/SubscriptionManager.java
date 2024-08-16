package com.oplus.wrapper.telephony;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.SubscriptionInfo;
import java.util.List;

/* loaded from: classes.dex */
public class SubscriptionManager {
    private final android.telephony.SubscriptionManager mSubscriptionManager;

    public SubscriptionManager(android.telephony.SubscriptionManager subscriptionManager) {
        this.mSubscriptionManager = subscriptionManager;
    }

    public SubscriptionInfo getDefaultVoiceSubscriptionInfo() {
        return this.mSubscriptionManager.getDefaultVoiceSubscriptionInfo();
    }

    public SubscriptionInfo getDefaultDataSubscriptionInfo() {
        return this.mSubscriptionManager.getDefaultDataSubscriptionInfo();
    }

    public static int[] getSubId(int slotIndex) {
        return android.telephony.SubscriptionManager.getSubId(slotIndex);
    }

    public boolean isActiveSubId(int subId) {
        return this.mSubscriptionManager.isActiveSubId(subId);
    }

    public static int getPhoneId(int subId) {
        return android.telephony.SubscriptionManager.getPhoneId(subId);
    }

    public static int getDefaultVoicePhoneId() {
        return android.telephony.SubscriptionManager.getDefaultVoicePhoneId();
    }

    public static boolean isValidPhoneId(int phoneId) {
        return android.telephony.SubscriptionManager.isValidPhoneId(phoneId);
    }

    public static Resources getResourcesForSubId(Context context, int subId) {
        return android.telephony.SubscriptionManager.getResourcesForSubId(context, subId);
    }

    public int setDisplayName(String displayName, int subId, int nameSource) {
        return this.mSubscriptionManager.setDisplayName(displayName, subId, nameSource);
    }

    public List<SubscriptionInfo> getSelectableSubscriptionInfoList() {
        return this.mSubscriptionManager.getSelectableSubscriptionInfoList();
    }

    public List<SubscriptionInfo> getAvailableSubscriptionInfoList() {
        return this.mSubscriptionManager.getAvailableSubscriptionInfoList();
    }
}
