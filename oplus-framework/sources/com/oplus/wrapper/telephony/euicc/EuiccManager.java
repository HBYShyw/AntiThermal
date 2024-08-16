package com.oplus.wrapper.telephony.euicc;

/* loaded from: classes.dex */
public class EuiccManager {
    public static final String ACTION_PROVISION_EMBEDDED_SUBSCRIPTION = getActionProvisionEmbeddedSubscription();
    public static final String EXTRA_FORCE_PROVISION = getExtraForceProvision();

    private EuiccManager() {
    }

    private static String getActionProvisionEmbeddedSubscription() {
        return "android.telephony.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION";
    }

    private static String getExtraForceProvision() {
        return "android.telephony.euicc.extra.FORCE_PROVISION";
    }
}
