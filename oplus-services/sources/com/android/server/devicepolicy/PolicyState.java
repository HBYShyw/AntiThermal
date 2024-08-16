package com.android.server.devicepolicy;

import android.app.admin.PolicyValue;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.utils.Slogf;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class PolicyState<V> {
    private static final String TAG = "PolicyState";
    private static final String TAG_ADMIN_POLICY_ENTRY = "admin-policy-entry";
    private static final String TAG_ENFORCING_ADMIN_ENTRY = "enforcing-admin-entry";
    private static final String TAG_POLICY_DEFINITION_ENTRY = "policy-definition-entry";
    private static final String TAG_POLICY_VALUE_ENTRY = "policy-value-entry";
    private static final String TAG_RESOLVED_VALUE_ENTRY = "resolved-value-entry";
    private PolicyValue<V> mCurrentResolvedPolicy;
    private final LinkedHashMap<EnforcingAdmin, PolicyValue<V>> mPoliciesSetByAdmins;
    private final PolicyDefinition<V> mPolicyDefinition;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PolicyState(PolicyDefinition<V> policyDefinition) {
        this.mPoliciesSetByAdmins = new LinkedHashMap<>();
        Objects.requireNonNull(policyDefinition);
        this.mPolicyDefinition = policyDefinition;
    }

    private PolicyState(PolicyDefinition<V> policyDefinition, LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap, PolicyValue<V> policyValue) {
        LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap2 = new LinkedHashMap<>();
        this.mPoliciesSetByAdmins = linkedHashMap2;
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(linkedHashMap);
        this.mPolicyDefinition = policyDefinition;
        linkedHashMap2.putAll(linkedHashMap);
        this.mCurrentResolvedPolicy = policyValue;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addPolicy(EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue) {
        Objects.requireNonNull(enforcingAdmin);
        this.mPoliciesSetByAdmins.remove(enforcingAdmin);
        this.mPoliciesSetByAdmins.put(enforcingAdmin, policyValue);
        return resolvePolicy();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addPolicy(EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue, LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap) {
        LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap2 = this.mPoliciesSetByAdmins;
        Objects.requireNonNull(enforcingAdmin);
        linkedHashMap2.put(enforcingAdmin, policyValue);
        return resolvePolicy(linkedHashMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removePolicy(EnforcingAdmin enforcingAdmin) {
        Objects.requireNonNull(enforcingAdmin);
        if (this.mPoliciesSetByAdmins.remove(enforcingAdmin) == null) {
            return false;
        }
        return resolvePolicy();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removePolicy(EnforcingAdmin enforcingAdmin, LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap) {
        Objects.requireNonNull(enforcingAdmin);
        if (this.mPoliciesSetByAdmins.remove(enforcingAdmin) == null) {
            return false;
        }
        return resolvePolicy(linkedHashMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resolvePolicy(LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap) {
        if (this.mPolicyDefinition.isNonCoexistablePolicy()) {
            return false;
        }
        LinkedHashMap<EnforcingAdmin, PolicyValue<V>> linkedHashMap2 = new LinkedHashMap<>(linkedHashMap);
        linkedHashMap2.putAll(this.mPoliciesSetByAdmins);
        PolicyValue<V> resolvePolicy = this.mPolicyDefinition.resolvePolicy(linkedHashMap2);
        boolean z = !Objects.equals(resolvePolicy, this.mCurrentResolvedPolicy);
        this.mCurrentResolvedPolicy = resolvePolicy;
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LinkedHashMap<EnforcingAdmin, PolicyValue<V>> getPoliciesSetByAdmins() {
        return new LinkedHashMap<>(this.mPoliciesSetByAdmins);
    }

    private boolean resolvePolicy() {
        if (this.mPolicyDefinition.isNonCoexistablePolicy()) {
            return false;
        }
        PolicyValue<V> resolvePolicy = this.mPolicyDefinition.resolvePolicy(this.mPoliciesSetByAdmins);
        boolean z = !Objects.equals(resolvePolicy, this.mCurrentResolvedPolicy);
        this.mCurrentResolvedPolicy = resolvePolicy;
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PolicyValue<V> getCurrentResolvedPolicy() {
        return this.mCurrentResolvedPolicy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public android.app.admin.PolicyState<V> getParcelablePolicyState() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (EnforcingAdmin enforcingAdmin : this.mPoliciesSetByAdmins.keySet()) {
            linkedHashMap.put(enforcingAdmin.getParcelableAdmin(), this.mPoliciesSetByAdmins.get(enforcingAdmin));
        }
        return new android.app.admin.PolicyState<>(linkedHashMap, this.mCurrentResolvedPolicy, this.mPolicyDefinition.getResolutionMechanism().mo3346getParcelableResolutionMechanism());
    }

    public String toString() {
        return "PolicyState { mPolicyDefinition= " + this.mPolicyDefinition + ", mPoliciesSetByAdmins= " + this.mPoliciesSetByAdmins + ", mCurrentResolvedPolicy= " + this.mCurrentResolvedPolicy + " }";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public void saveToXml(TypedXmlSerializer typedXmlSerializer) throws IOException {
        typedXmlSerializer.startTag((String) null, TAG_POLICY_DEFINITION_ENTRY);
        this.mPolicyDefinition.saveToXml(typedXmlSerializer);
        typedXmlSerializer.endTag((String) null, TAG_POLICY_DEFINITION_ENTRY);
        if (this.mCurrentResolvedPolicy != null) {
            typedXmlSerializer.startTag((String) null, TAG_RESOLVED_VALUE_ENTRY);
            this.mPolicyDefinition.savePolicyValueToXml(typedXmlSerializer, this.mCurrentResolvedPolicy.getValue());
            typedXmlSerializer.endTag((String) null, TAG_RESOLVED_VALUE_ENTRY);
        }
        for (EnforcingAdmin enforcingAdmin : this.mPoliciesSetByAdmins.keySet()) {
            typedXmlSerializer.startTag((String) null, TAG_ADMIN_POLICY_ENTRY);
            if (this.mPoliciesSetByAdmins.get(enforcingAdmin) != null) {
                typedXmlSerializer.startTag((String) null, TAG_POLICY_VALUE_ENTRY);
                this.mPolicyDefinition.savePolicyValueToXml(typedXmlSerializer, this.mPoliciesSetByAdmins.get(enforcingAdmin).getValue());
                typedXmlSerializer.endTag((String) null, TAG_POLICY_VALUE_ENTRY);
            }
            typedXmlSerializer.startTag((String) null, TAG_ENFORCING_ADMIN_ENTRY);
            enforcingAdmin.saveToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((String) null, TAG_ENFORCING_ADMIN_ENTRY);
            typedXmlSerializer.endTag((String) null, TAG_ADMIN_POLICY_ENTRY);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005e A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0072 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00bd A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0049 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static <V> PolicyState<V> readFromXml(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int depth = typedXmlPullParser.getDepth();
        PolicyDefinition policyDefinition = null;
        PolicyValue<V> policyValue = null;
        while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
            String name = typedXmlPullParser.getName();
            name.hashCode();
            char c = 65535;
            switch (name.hashCode()) {
                case 394982067:
                    if (name.equals(TAG_POLICY_DEFINITION_ENTRY)) {
                        c = 0;
                    }
                    switch (c) {
                        case 0:
                            policyDefinition = PolicyDefinition.readFromXml(typedXmlPullParser);
                            if (policyDefinition != null) {
                                break;
                            } else {
                                Slogf.wtf(TAG, "Error Parsing TAG_POLICY_DEFINITION_ENTRY, PolicyDefinition is null");
                                break;
                            }
                        case 1:
                            int depth2 = typedXmlPullParser.getDepth();
                            EnforcingAdmin enforcingAdmin = null;
                            PolicyValue<V> policyValue2 = null;
                            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth2)) {
                                String name2 = typedXmlPullParser.getName();
                                name2.hashCode();
                                if (name2.equals(TAG_ENFORCING_ADMIN_ENTRY)) {
                                    enforcingAdmin = EnforcingAdmin.readFromXml(typedXmlPullParser);
                                    if (enforcingAdmin == null) {
                                        Slogf.wtf(TAG, "Error Parsing TAG_ENFORCING_ADMIN_ENTRY, EnforcingAdmin is null");
                                    }
                                } else if (name2.equals(TAG_POLICY_VALUE_ENTRY) && (policyValue2 = policyDefinition.readPolicyValueFromXml(typedXmlPullParser)) == null) {
                                    Slogf.wtf(TAG, "Error Parsing TAG_POLICY_VALUE_ENTRY, PolicyValue is null");
                                }
                            }
                            if (enforcingAdmin != null) {
                                linkedHashMap.put(enforcingAdmin, policyValue2);
                                break;
                            } else {
                                Slogf.wtf(TAG, "Error Parsing TAG_ADMIN_POLICY_ENTRY, EnforcingAdmin is null");
                                break;
                            }
                        case 2:
                            if (policyDefinition == null) {
                                Slogf.wtf(TAG, "Error Parsing TAG_RESOLVED_VALUE_ENTRY, policyDefinition is null");
                                break;
                            } else {
                                policyValue = policyDefinition.readPolicyValueFromXml(typedXmlPullParser);
                                if (policyValue != null) {
                                    break;
                                } else {
                                    Slogf.wtf(TAG, "Error Parsing TAG_RESOLVED_VALUE_ENTRY, currentResolvedPolicy is null");
                                    break;
                                }
                            }
                        default:
                            Slogf.wtf(TAG, "Unknown tag: " + name);
                            break;
                    }
                    break;
                case 695389653:
                    if (name.equals(TAG_ADMIN_POLICY_ENTRY)) {
                        c = 1;
                    }
                    switch (c) {
                    }
                    break;
                case 829992641:
                    if (name.equals(TAG_RESOLVED_VALUE_ENTRY)) {
                        c = 2;
                    }
                    switch (c) {
                    }
                    break;
                default:
                    switch (c) {
                    }
                    break;
            }
        }
        if (policyDefinition != null) {
            return new PolicyState<>(policyDefinition, linkedHashMap, policyValue);
        }
        Slogf.wtf(TAG, "Error parsing policyState, policyDefinition is null");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PolicyDefinition<V> getPolicyDefinition() {
        return this.mPolicyDefinition;
    }
}
