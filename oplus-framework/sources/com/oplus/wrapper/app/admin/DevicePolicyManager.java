package com.oplus.wrapper.app.admin;

import android.content.Intent;

/* loaded from: classes.dex */
public class DevicePolicyManager {
    private final android.app.admin.DevicePolicyManager mDevicePolicyManager;
    public static final int STATE_USER_UNMANAGED = getStateUserUnmanaged();
    public static final int STATE_USER_SETUP_INCOMPLETE = getStateUserSetupIncomplete();
    public static final int STATE_USER_SETUP_COMPLETE = getStateUserSetupComplete();
    public static final int STATE_USER_SETUP_FINALIZED = getStateUserSetupFinalized();
    public static final int STATE_USER_PROFILE_COMPLETE = getStateUserProfileComplete();
    public static final int STATE_USER_PROFILE_FINALIZED = getStateUserProfileFinalized();

    public DevicePolicyManager(android.app.admin.DevicePolicyManager devicePolicyManager) {
        this.mDevicePolicyManager = devicePolicyManager;
    }

    public Intent createProvisioningIntentFromNfcIntent(Intent nfcIntent) {
        return this.mDevicePolicyManager.createProvisioningIntentFromNfcIntent(nfcIntent);
    }

    public int getUserProvisioningState() {
        return this.mDevicePolicyManager.getUserProvisioningState();
    }

    private static int getStateUserUnmanaged() {
        return 0;
    }

    private static int getStateUserSetupIncomplete() {
        return 1;
    }

    private static int getStateUserSetupComplete() {
        return 2;
    }

    private static int getStateUserSetupFinalized() {
        return 3;
    }

    private static int getStateUserProfileComplete() {
        return 4;
    }

    private static int getStateUserProfileFinalized() {
        return 5;
    }
}
