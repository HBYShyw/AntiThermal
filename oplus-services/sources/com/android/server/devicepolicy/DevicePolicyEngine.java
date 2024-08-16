package com.android.server.devicepolicy;

import android.app.AppGlobals;
import android.app.BroadcastOptions;
import android.app.admin.BooleanPolicyValue;
import android.app.admin.DevicePolicyIdentifiers;
import android.app.admin.DevicePolicyState;
import android.app.admin.IntentFilterPolicyKey;
import android.app.admin.PolicyKey;
import android.app.admin.PolicyValue;
import android.app.admin.UserRestrictionPolicyKey;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.pm.UserProperties;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.TelephonyManager;
import android.util.AtomicFile;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.utils.Slogf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DevicePolicyEngine {
    private static final String CELLULAR_2G_USER_RESTRICTION_ID = DevicePolicyIdentifiers.getIdentifierForUserRestriction("no_cellular_2g");
    static final String DEVICE_LOCK_CONTROLLER_ROLE = "android.app.role.SYSTEM_FINANCED_DEVICE_CONTROLLER";
    static final String TAG = "DevicePolicyEngine";
    private final Context mContext;
    private final DeviceAdminServiceController mDeviceAdminServiceController;
    private final SparseArray<Set<EnforcingAdmin>> mEnforcingAdmins;
    private final Map<PolicyKey, PolicyState<?>> mGlobalPolicies;
    private final SparseArray<Map<PolicyKey, PolicyState<?>>> mLocalPolicies;
    private final Object mLock;
    private final UserManager mUserManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DevicePolicyEngine(Context context, DeviceAdminServiceController deviceAdminServiceController, Object obj) {
        Objects.requireNonNull(context);
        this.mContext = context;
        Objects.requireNonNull(deviceAdminServiceController);
        this.mDeviceAdminServiceController = deviceAdminServiceController;
        Objects.requireNonNull(obj);
        this.mLock = obj;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mLocalPolicies = new SparseArray<>();
        this.mGlobalPolicies = new HashMap();
        this.mEnforcingAdmins = new SparseArray<>();
    }

    private void maybeForceEnforcementRefreshLocked(PolicyDefinition<?> policyDefinition) {
        try {
            if (shouldForceEnforcementRefresh(policyDefinition)) {
                forceEnforcementRefreshLocked(policyDefinition);
            }
        } catch (Throwable th) {
            Log.e(TAG, "Exception throw during maybeForceEnforcementRefreshLocked", th);
        }
    }

    private boolean shouldForceEnforcementRefresh(PolicyDefinition<?> policyDefinition) {
        PolicyKey policyKey;
        return (policyDefinition == null || (policyKey = policyDefinition.getPolicyKey()) == null || !(policyKey instanceof UserRestrictionPolicyKey)) ? false : true;
    }

    private void forceEnforcementRefreshLocked(final PolicyDefinition<Boolean> policyDefinition) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda5
            public final void runOrThrow() {
                DevicePolicyEngine.this.lambda$forceEnforcementRefreshLocked$0(policyDefinition);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceEnforcementRefreshLocked$0(PolicyDefinition policyDefinition) throws Exception {
        PolicyValue booleanPolicyValue = new BooleanPolicyValue(false);
        try {
            booleanPolicyValue = getGlobalPolicyStateLocked(policyDefinition).getCurrentResolvedPolicy();
        } catch (IllegalArgumentException unused) {
        }
        enforcePolicy(policyDefinition, booleanPolicyValue, -1);
        for (UserInfo userInfo : this.mUserManager.getUsers()) {
            PolicyValue booleanPolicyValue2 = new BooleanPolicyValue(false);
            try {
                booleanPolicyValue2 = getLocalPolicyStateLocked(policyDefinition, userInfo.id).getCurrentResolvedPolicy();
            } catch (IllegalArgumentException unused2) {
            }
            enforcePolicy(policyDefinition, booleanPolicyValue2, userInfo.id);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> void setLocalPolicy(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue, int i, boolean z) {
        boolean addPolicy;
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            PolicyState<V> localPolicyStateLocked = getLocalPolicyStateLocked(policyDefinition, i);
            if (policyDefinition.isNonCoexistablePolicy()) {
                setNonCoexistableLocalPolicyLocked(policyDefinition, localPolicyStateLocked, enforcingAdmin, policyValue, i, z);
                return;
            }
            if (hasGlobalPolicyLocked(policyDefinition)) {
                addPolicy = localPolicyStateLocked.addPolicy(enforcingAdmin, policyValue, getGlobalPolicyStateLocked(policyDefinition).getPoliciesSetByAdmins());
            } else {
                addPolicy = localPolicyStateLocked.addPolicy(enforcingAdmin, policyValue);
            }
            if (!z) {
                maybeForceEnforcementRefreshLocked(policyDefinition);
                if (addPolicy) {
                    onLocalPolicyChangedLocked(policyDefinition, enforcingAdmin, i);
                }
                boolean equals = Objects.equals(localPolicyStateLocked.getCurrentResolvedPolicy(), policyValue);
                int i2 = 0;
                if (!equals && policyDefinition.getPolicyKey().getIdentifier().equals("userControlDisabledPackages")) {
                    PolicyValue<V> currentResolvedPolicy = localPolicyStateLocked.getCurrentResolvedPolicy();
                    equals = (currentResolvedPolicy == null || policyValue == null || !((Set) currentResolvedPolicy.getValue()).containsAll((Collection) policyValue.getValue())) ? false : true;
                }
                if (!equals) {
                    i2 = 1;
                }
                sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, i2, i);
            }
            updateDeviceAdminServiceOnPolicyAddLocked(enforcingAdmin);
            write();
            applyToInheritableProfiles(policyDefinition, enforcingAdmin, policyValue, i);
        }
    }

    private <V> void setNonCoexistableLocalPolicyLocked(PolicyDefinition<V> policyDefinition, PolicyState<V> policyState, EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue, int i, boolean z) {
        if (policyValue == null) {
            policyState.removePolicy(enforcingAdmin);
        } else {
            policyState.addPolicy(enforcingAdmin, policyValue);
        }
        if (!z) {
            enforcePolicy(policyDefinition, policyValue, i);
        }
        if (policyState.getPoliciesSetByAdmins().isEmpty()) {
            removeLocalPolicyStateLocked(policyDefinition, i);
        }
        updateDeviceAdminServiceOnPolicyAddLocked(enforcingAdmin);
        write();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> void setLocalPolicy(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue, int i) {
        setLocalPolicy(policyDefinition, enforcingAdmin, policyValue, i, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> void removeLocalPolicy(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, int i) {
        boolean removePolicy;
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            maybeForceEnforcementRefreshLocked(policyDefinition);
            if (hasLocalPolicyLocked(policyDefinition, i)) {
                PolicyState<V> localPolicyStateLocked = getLocalPolicyStateLocked(policyDefinition, i);
                if (policyDefinition.isNonCoexistablePolicy()) {
                    setNonCoexistableLocalPolicyLocked(policyDefinition, localPolicyStateLocked, enforcingAdmin, null, i, false);
                    return;
                }
                if (hasGlobalPolicyLocked(policyDefinition)) {
                    removePolicy = localPolicyStateLocked.removePolicy(enforcingAdmin, getGlobalPolicyStateLocked(policyDefinition).getPoliciesSetByAdmins());
                } else {
                    removePolicy = localPolicyStateLocked.removePolicy(enforcingAdmin);
                }
                if (removePolicy) {
                    onLocalPolicyChangedLocked(policyDefinition, enforcingAdmin, i);
                }
                sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, 2, i);
                if (localPolicyStateLocked.getPoliciesSetByAdmins().isEmpty()) {
                    removeLocalPolicyStateLocked(policyDefinition, i);
                }
                updateDeviceAdminServiceOnPolicyRemoveLocked(enforcingAdmin);
                write();
                applyToInheritableProfiles(policyDefinition, enforcingAdmin, null, i);
            }
        }
    }

    private <V> void applyToInheritableProfiles(final PolicyDefinition<V> policyDefinition, final EnforcingAdmin enforcingAdmin, final PolicyValue<V> policyValue, final int i) {
        if (policyDefinition.isInheritable()) {
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda1
                public final void runOrThrow() {
                    DevicePolicyEngine.this.lambda$applyToInheritableProfiles$1(i, policyValue, policyDefinition, enforcingAdmin);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyToInheritableProfiles$1(int i, PolicyValue policyValue, PolicyDefinition policyDefinition, EnforcingAdmin enforcingAdmin) throws Exception {
        for (UserInfo userInfo : this.mUserManager.getProfiles(i)) {
            int identifier = userInfo.getUserHandle().getIdentifier();
            if (isProfileOfUser(identifier, i) && isInheritDevicePolicyFromParent(userInfo)) {
                if (policyValue != null) {
                    setLocalPolicy(policyDefinition, enforcingAdmin, policyValue, identifier);
                } else {
                    removeLocalPolicy(policyDefinition, enforcingAdmin, identifier);
                }
            }
        }
    }

    private boolean isProfileOfUser(int i, int i2) {
        UserInfo profileParent = this.mUserManager.getProfileParent(i);
        return (i == i2 || profileParent == null || profileParent.getUserHandle().getIdentifier() != i2) ? false : true;
    }

    private boolean isInheritDevicePolicyFromParent(UserInfo userInfo) {
        return this.mUserManager.getUserProperties(userInfo.getUserHandle()) != null && this.mUserManager.getUserProperties(userInfo.getUserHandle()).getInheritDevicePolicy() == 1;
    }

    private <V> void onLocalPolicyChangedLocked(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, int i) {
        PolicyState<V> localPolicyStateLocked = getLocalPolicyStateLocked(policyDefinition, i);
        enforcePolicy(policyDefinition, localPolicyStateLocked.getCurrentResolvedPolicy(), i);
        sendPolicyChangedToAdminsLocked(localPolicyStateLocked, enforcingAdmin, policyDefinition, i);
        if (hasGlobalPolicyLocked(policyDefinition)) {
            sendPolicyChangedToAdminsLocked(getGlobalPolicyStateLocked(policyDefinition), enforcingAdmin, policyDefinition, i);
        }
        sendDevicePolicyChangedToSystem(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> void setGlobalPolicy(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue) {
        setGlobalPolicy(policyDefinition, enforcingAdmin, policyValue, false);
    }

    <V> void setGlobalPolicy(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue, boolean z) {
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(enforcingAdmin);
        Objects.requireNonNull(policyValue);
        synchronized (this.mLock) {
            if (checkFor2gFailure(policyDefinition, enforcingAdmin)) {
                Log.i(TAG, "Device does not support capabilities required to disable 2g. Not setting global policy state.");
                return;
            }
            PolicyState<V> globalPolicyStateLocked = getGlobalPolicyStateLocked(policyDefinition);
            boolean addPolicy = globalPolicyStateLocked.addPolicy(enforcingAdmin, policyValue);
            boolean applyGlobalPolicyOnUsersWithLocalPoliciesLocked = applyGlobalPolicyOnUsersWithLocalPoliciesLocked(policyDefinition, enforcingAdmin, policyValue, z);
            if (!z) {
                maybeForceEnforcementRefreshLocked(policyDefinition);
                if (addPolicy) {
                    onGlobalPolicyChangedLocked(policyDefinition, enforcingAdmin);
                }
                boolean equals = Objects.equals(globalPolicyStateLocked.getCurrentResolvedPolicy(), policyValue);
                if (!equals && policyDefinition.getPolicyKey().getIdentifier().equals("userControlDisabledPackages")) {
                    PolicyValue<V> currentResolvedPolicy = globalPolicyStateLocked.getCurrentResolvedPolicy();
                    equals = currentResolvedPolicy != null && ((Set) currentResolvedPolicy.getValue()).containsAll((Collection) policyValue.getValue());
                }
                sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, equals && applyGlobalPolicyOnUsersWithLocalPoliciesLocked ? 0 : 1, -1);
            }
            updateDeviceAdminServiceOnPolicyAddLocked(enforcingAdmin);
            write();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> void removeGlobalPolicy(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin) {
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            PolicyState<V> globalPolicyStateLocked = getGlobalPolicyStateLocked(policyDefinition);
            boolean removePolicy = globalPolicyStateLocked.removePolicy(enforcingAdmin);
            maybeForceEnforcementRefreshLocked(policyDefinition);
            if (removePolicy) {
                onGlobalPolicyChangedLocked(policyDefinition, enforcingAdmin);
            }
            applyGlobalPolicyOnUsersWithLocalPoliciesLocked(policyDefinition, enforcingAdmin, null, false);
            sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, 2, -1);
            if (globalPolicyStateLocked.getPoliciesSetByAdmins().isEmpty()) {
                removeGlobalPolicyStateLocked(policyDefinition);
            }
            updateDeviceAdminServiceOnPolicyRemoveLocked(enforcingAdmin);
            write();
        }
    }

    private <V> void onGlobalPolicyChangedLocked(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin) {
        PolicyState<V> globalPolicyStateLocked = getGlobalPolicyStateLocked(policyDefinition);
        enforcePolicy(policyDefinition, globalPolicyStateLocked.getCurrentResolvedPolicy(), -1);
        sendPolicyChangedToAdminsLocked(globalPolicyStateLocked, enforcingAdmin, policyDefinition, -1);
        sendDevicePolicyChangedToSystem(-1);
    }

    private <V> boolean applyGlobalPolicyOnUsersWithLocalPoliciesLocked(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, PolicyValue<V> policyValue, boolean z) {
        boolean equals;
        if (policyDefinition.isGlobalOnlyPolicy()) {
            return true;
        }
        boolean z2 = true;
        for (int i = 0; i < this.mLocalPolicies.size(); i++) {
            int keyAt = this.mLocalPolicies.keyAt(i);
            if (hasLocalPolicyLocked(policyDefinition, keyAt)) {
                PolicyState<V> localPolicyStateLocked = getLocalPolicyStateLocked(policyDefinition, keyAt);
                if (localPolicyStateLocked.resolvePolicy(getGlobalPolicyStateLocked(policyDefinition).getPoliciesSetByAdmins()) && !z) {
                    enforcePolicy(policyDefinition, localPolicyStateLocked.getCurrentResolvedPolicy(), keyAt);
                    sendPolicyChangedToAdminsLocked(localPolicyStateLocked, enforcingAdmin, policyDefinition, keyAt);
                }
                if (policyDefinition.getPolicyKey().getIdentifier().equals("userControlDisabledPackages")) {
                    if (!Objects.equals(policyValue, localPolicyStateLocked.getCurrentResolvedPolicy())) {
                        PolicyValue<V> currentResolvedPolicy = localPolicyStateLocked.getCurrentResolvedPolicy();
                        equals = (currentResolvedPolicy == null || policyValue == null || !((Set) currentResolvedPolicy.getValue()).containsAll((Collection) policyValue.getValue())) ? false : true;
                    }
                } else {
                    equals = Objects.equals(policyValue, localPolicyStateLocked.getCurrentResolvedPolicy());
                }
                z2 &= equals;
            }
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> V getResolvedPolicy(PolicyDefinition<V> policyDefinition, int i) {
        V v;
        PolicyValue<V> currentResolvedPolicy;
        Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            v = null;
            if (hasLocalPolicyLocked(policyDefinition, i)) {
                currentResolvedPolicy = getLocalPolicyStateLocked(policyDefinition, i).getCurrentResolvedPolicy();
            } else {
                currentResolvedPolicy = hasGlobalPolicyLocked(policyDefinition) ? getGlobalPolicyStateLocked(policyDefinition).getCurrentResolvedPolicy() : null;
            }
            if (currentResolvedPolicy != null) {
                v = (V) currentResolvedPolicy.getValue();
            }
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> V getLocalPolicySetByAdmin(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, int i) {
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            V v = null;
            if (!hasLocalPolicyLocked(policyDefinition, i)) {
                return null;
            }
            PolicyValue<V> policyValue = getLocalPolicyStateLocked(policyDefinition, i).getPoliciesSetByAdmins().get(enforcingAdmin);
            if (policyValue != null) {
                v = (V) policyValue.getValue();
            }
            return v;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> V getGlobalPolicySetByAdmin(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin) {
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            V v = null;
            if (!hasGlobalPolicyLocked(policyDefinition)) {
                return null;
            }
            PolicyValue<V> policyValue = getGlobalPolicyStateLocked(policyDefinition).getPoliciesSetByAdmins().get(enforcingAdmin);
            if (policyValue != null) {
                v = (V) policyValue.getValue();
            }
            return v;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> LinkedHashMap<EnforcingAdmin, PolicyValue<V>> getLocalPoliciesSetByAdmins(PolicyDefinition<V> policyDefinition, int i) {
        Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            if (!hasLocalPolicyLocked(policyDefinition, i)) {
                return new LinkedHashMap<>();
            }
            return getLocalPolicyStateLocked(policyDefinition, i).getPoliciesSetByAdmins();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> LinkedHashMap<EnforcingAdmin, PolicyValue<V>> getGlobalPoliciesSetByAdmins(PolicyDefinition<V> policyDefinition) {
        Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            if (!hasGlobalPolicyLocked(policyDefinition)) {
                return new LinkedHashMap<>();
            }
            return getGlobalPolicyStateLocked(policyDefinition).getPoliciesSetByAdmins();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> Set<PolicyKey> getLocalPolicyKeysSetByAdmin(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin, int i) {
        Objects.requireNonNull(policyDefinition);
        Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            if (!policyDefinition.isGlobalOnlyPolicy() && this.mLocalPolicies.contains(i)) {
                HashSet hashSet = new HashSet();
                for (PolicyKey policyKey : this.mLocalPolicies.get(i).keySet()) {
                    if (policyKey.hasSameIdentifierAs(policyDefinition.getPolicyKey()) && this.mLocalPolicies.get(i).get(policyKey).getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                        hashSet.add(policyKey);
                    }
                }
                return hashSet;
            }
            return Set.of();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> Set<PolicyKey> getLocalPolicyKeysSetByAllAdmins(PolicyDefinition<V> policyDefinition, int i) {
        Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            if (!policyDefinition.isGlobalOnlyPolicy() && this.mLocalPolicies.contains(i)) {
                HashSet hashSet = new HashSet();
                for (PolicyKey policyKey : this.mLocalPolicies.get(i).keySet()) {
                    if (policyKey.hasSameIdentifierAs(policyDefinition.getPolicyKey())) {
                        hashSet.add(policyKey);
                    }
                }
                return hashSet;
            }
            return Set.of();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<UserRestrictionPolicyKey> getUserRestrictionPolicyKeysForAdmin(EnforcingAdmin enforcingAdmin, int i) {
        Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            if (i == -1) {
                return getUserRestrictionPolicyKeysForAdminLocked(this.mGlobalPolicies, enforcingAdmin);
            }
            if (!this.mLocalPolicies.contains(i)) {
                return Set.of();
            }
            return getUserRestrictionPolicyKeysForAdminLocked(this.mLocalPolicies.get(i), enforcingAdmin);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public <V> void transferPolicies(EnforcingAdmin enforcingAdmin, EnforcingAdmin enforcingAdmin2) {
        synchronized (this.mLock) {
            Iterator it = new HashSet(this.mGlobalPolicies.keySet()).iterator();
            while (it.hasNext()) {
                PolicyState<?> policyState = this.mGlobalPolicies.get((PolicyKey) it.next());
                if (policyState.getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                    setGlobalPolicy(policyState.getPolicyDefinition(), enforcingAdmin2, policyState.getPoliciesSetByAdmins().get(enforcingAdmin));
                }
            }
            for (int i = 0; i < this.mLocalPolicies.size(); i++) {
                int keyAt = this.mLocalPolicies.keyAt(i);
                Iterator it2 = new HashSet(this.mLocalPolicies.get(keyAt).keySet()).iterator();
                while (it2.hasNext()) {
                    PolicyState<?> policyState2 = this.mLocalPolicies.get(keyAt).get((PolicyKey) it2.next());
                    if (policyState2.getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                        setLocalPolicy(policyState2.getPolicyDefinition(), enforcingAdmin2, policyState2.getPoliciesSetByAdmins().get(enforcingAdmin), keyAt);
                    }
                }
            }
        }
        removePoliciesForAdmin(enforcingAdmin);
    }

    private Set<UserRestrictionPolicyKey> getUserRestrictionPolicyKeysForAdminLocked(Map<PolicyKey, PolicyState<?>> map, EnforcingAdmin enforcingAdmin) {
        PolicyValue<?> policyValue;
        HashSet hashSet = new HashSet();
        Iterator<PolicyKey> it = map.keySet().iterator();
        while (it.hasNext()) {
            UserRestrictionPolicyKey userRestrictionPolicyKey = (PolicyKey) it.next();
            if (map.get(userRestrictionPolicyKey).getPolicyDefinition().isUserRestrictionPolicy() && (policyValue = map.get(userRestrictionPolicyKey).getPoliciesSetByAdmins().get(enforcingAdmin)) != null && ((Boolean) policyValue.getValue()).booleanValue()) {
                hashSet.add(userRestrictionPolicyKey);
            }
        }
        return hashSet;
    }

    private <V> boolean hasLocalPolicyLocked(PolicyDefinition<V> policyDefinition, int i) {
        if (!policyDefinition.isGlobalOnlyPolicy() && this.mLocalPolicies.contains(i) && this.mLocalPolicies.get(i).containsKey(policyDefinition.getPolicyKey())) {
            return !this.mLocalPolicies.get(i).get(policyDefinition.getPolicyKey()).getPoliciesSetByAdmins().isEmpty();
        }
        return false;
    }

    private <V> boolean hasGlobalPolicyLocked(PolicyDefinition<V> policyDefinition) {
        if (!policyDefinition.isLocalOnlyPolicy() && this.mGlobalPolicies.containsKey(policyDefinition.getPolicyKey())) {
            return !this.mGlobalPolicies.get(policyDefinition.getPolicyKey()).getPoliciesSetByAdmins().isEmpty();
        }
        return false;
    }

    private <V> PolicyState<V> getLocalPolicyStateLocked(PolicyDefinition<V> policyDefinition, int i) {
        if (policyDefinition.isGlobalOnlyPolicy()) {
            throw new IllegalArgumentException(policyDefinition.getPolicyKey() + " is a global only policy.");
        }
        if (!this.mLocalPolicies.contains(i)) {
            this.mLocalPolicies.put(i, new HashMap());
        }
        if (!this.mLocalPolicies.get(i).containsKey(policyDefinition.getPolicyKey())) {
            this.mLocalPolicies.get(i).put(policyDefinition.getPolicyKey(), new PolicyState<>(policyDefinition));
        }
        return getPolicyStateLocked(this.mLocalPolicies.get(i), policyDefinition);
    }

    private <V> void removeLocalPolicyStateLocked(PolicyDefinition<V> policyDefinition, int i) {
        if (this.mLocalPolicies.contains(i)) {
            this.mLocalPolicies.get(i).remove(policyDefinition.getPolicyKey());
        }
    }

    private <V> PolicyState<V> getGlobalPolicyStateLocked(PolicyDefinition<V> policyDefinition) {
        if (policyDefinition.isLocalOnlyPolicy()) {
            throw new IllegalArgumentException(policyDefinition.getPolicyKey() + " is a local only policy.");
        }
        if (!this.mGlobalPolicies.containsKey(policyDefinition.getPolicyKey())) {
            this.mGlobalPolicies.put(policyDefinition.getPolicyKey(), new PolicyState<>(policyDefinition));
        }
        return getPolicyStateLocked(this.mGlobalPolicies, policyDefinition);
    }

    private <V> void removeGlobalPolicyStateLocked(PolicyDefinition<V> policyDefinition) {
        this.mGlobalPolicies.remove(policyDefinition.getPolicyKey());
    }

    private static <V> PolicyState<V> getPolicyStateLocked(Map<PolicyKey, PolicyState<?>> map, PolicyDefinition<V> policyDefinition) {
        try {
            return (PolicyState) map.get(policyDefinition.getPolicyKey());
        } catch (ClassCastException unused) {
            throw new IllegalArgumentException();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <V> void enforcePolicy(PolicyDefinition<V> policyDefinition, PolicyValue<V> policyValue, int i) {
        policyDefinition.enforcePolicy(policyValue == null ? null : policyValue.getValue(), this.mContext, i);
    }

    private void sendDevicePolicyChangedToSystem(final int i) {
        final Intent intent = new Intent("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        intent.setFlags(1073741824);
        final Bundle bundle = new BroadcastOptions().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda2
            public final void runOrThrow() {
                DevicePolicyEngine.this.lambda$sendDevicePolicyChangedToSystem$2(intent, i, bundle);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendDevicePolicyChangedToSystem$2(Intent intent, int i, Bundle bundle) throws Exception {
        this.mContext.sendBroadcastAsUser(intent, new UserHandle(i), null, bundle);
    }

    private <V> void sendPolicyResultToAdmin(final EnforcingAdmin enforcingAdmin, final PolicyDefinition<V> policyDefinition, final int i, final int i2) {
        final Intent intent = new Intent("android.app.admin.action.DEVICE_POLICY_SET_RESULT");
        intent.setPackage(enforcingAdmin.getPackageName());
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda0
            public final void runOrThrow() {
                DevicePolicyEngine.this.lambda$sendPolicyResultToAdmin$3(intent, enforcingAdmin, policyDefinition, i2, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendPolicyResultToAdmin$3(Intent intent, EnforcingAdmin enforcingAdmin, PolicyDefinition policyDefinition, int i, int i2) throws Exception {
        List queryBroadcastReceiversAsUser = this.mContext.getPackageManager().queryBroadcastReceiversAsUser(intent, PackageManager.ResolveInfoFlags.of(2L), enforcingAdmin.getUserId());
        if (queryBroadcastReceiversAsUser.isEmpty()) {
            Log.i(TAG, "Couldn't find any receivers that handle ACTION_DEVICE_POLICY_SET_RESULT in package " + enforcingAdmin.getPackageName());
            return;
        }
        Bundle bundle = new Bundle();
        policyDefinition.getPolicyKey().writeToBundle(bundle);
        bundle.putInt("android.app.admin.extra.POLICY_TARGET_USER_ID", getTargetUser(enforcingAdmin.getUserId(), i));
        bundle.putInt("android.app.admin.extra.POLICY_UPDATE_RESULT_KEY", i2);
        intent.putExtras(bundle);
        maybeSendIntentToAdminReceivers(intent, UserHandle.of(enforcingAdmin.getUserId()), queryBroadcastReceiversAsUser);
    }

    private <V> void sendPolicyChangedToAdminsLocked(PolicyState<V> policyState, EnforcingAdmin enforcingAdmin, PolicyDefinition<V> policyDefinition, int i) {
        for (EnforcingAdmin enforcingAdmin2 : policyState.getPoliciesSetByAdmins().keySet()) {
            if (!enforcingAdmin2.equals(enforcingAdmin)) {
                maybeSendOnPolicyChanged(enforcingAdmin2, policyDefinition, !Objects.equals(policyState.getPoliciesSetByAdmins().get(enforcingAdmin2), policyState.getCurrentResolvedPolicy()) ? 1 : 0, i);
            }
        }
    }

    private <V> void maybeSendOnPolicyChanged(final EnforcingAdmin enforcingAdmin, final PolicyDefinition<V> policyDefinition, final int i, final int i2) {
        final Intent intent = new Intent("android.app.admin.action.DEVICE_POLICY_CHANGED");
        intent.setPackage(enforcingAdmin.getPackageName());
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda7
            public final void runOrThrow() {
                DevicePolicyEngine.this.lambda$maybeSendOnPolicyChanged$4(intent, enforcingAdmin, policyDefinition, i2, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeSendOnPolicyChanged$4(Intent intent, EnforcingAdmin enforcingAdmin, PolicyDefinition policyDefinition, int i, int i2) throws Exception {
        List queryBroadcastReceiversAsUser = this.mContext.getPackageManager().queryBroadcastReceiversAsUser(intent, PackageManager.ResolveInfoFlags.of(2L), enforcingAdmin.getUserId());
        if (queryBroadcastReceiversAsUser.isEmpty()) {
            Log.i(TAG, "Couldn't find any receivers that handle ACTION_DEVICE_POLICY_CHANGED in package " + enforcingAdmin.getPackageName());
            return;
        }
        Bundle bundle = new Bundle();
        policyDefinition.getPolicyKey().writeToBundle(bundle);
        bundle.putInt("android.app.admin.extra.POLICY_TARGET_USER_ID", getTargetUser(enforcingAdmin.getUserId(), i));
        bundle.putInt("android.app.admin.extra.POLICY_UPDATE_RESULT_KEY", i2);
        intent.putExtras(bundle);
        intent.addFlags(AudioFormat.EVRC);
        maybeSendIntentToAdminReceivers(intent, UserHandle.of(enforcingAdmin.getUserId()), queryBroadcastReceiversAsUser);
    }

    private void maybeSendIntentToAdminReceivers(Intent intent, UserHandle userHandle, List<ResolveInfo> list) {
        for (ResolveInfo resolveInfo : list) {
            if ("android.permission.BIND_DEVICE_ADMIN".equals(resolveInfo.activityInfo.permission)) {
                this.mContext.sendBroadcastAsUser(intent, userHandle);
            } else {
                Log.w(TAG, "Receiver " + resolveInfo.activityInfo + " is not protected by BIND_DEVICE_ADMIN permission!");
            }
        }
    }

    private int getTargetUser(int i, int i2) {
        if (i2 == -1) {
            return -3;
        }
        if (i == i2) {
            return -1;
        }
        return getProfileParentId(i) == i2 ? -2 : -3;
    }

    private int getProfileParentId(final int i) {
        return ((Integer) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda4
            public final Object getOrThrow() {
                Integer lambda$getProfileParentId$5;
                lambda$getProfileParentId$5 = DevicePolicyEngine.this.lambda$getProfileParentId$5(i);
                return lambda$getProfileParentId$5;
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getProfileParentId$5(int i) throws Exception {
        UserInfo profileParent = this.mUserManager.getProfileParent(i);
        if (profileParent != null) {
            i = profileParent.id;
        }
        return Integer.valueOf(i);
    }

    private void updateDeviceAdminsServicesForUser(int i, boolean z, String str) {
        if (!z) {
            this.mDeviceAdminServiceController.stopServicesForUser(i, str);
            return;
        }
        for (EnforcingAdmin enforcingAdmin : getEnforcingAdminsOnUser(i)) {
            if (!enforcingAdmin.hasAuthority("enterprise")) {
                this.mDeviceAdminServiceController.startServiceForAdmin(enforcingAdmin.getPackageName(), i, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleStartUser(int i) {
        updateDeviceAdminsServicesForUser(i, true, "start-user");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleUnlockUser(int i) {
        updateDeviceAdminsServicesForUser(i, true, "unlock-user");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleStopUser(int i) {
        updateDeviceAdminsServicesForUser(i, false, "stop-user");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handlePackageChanged(final String str, final int i, final String str2) {
        Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda3
            public final void runOrThrow() {
                DevicePolicyEngine.this.lambda$handlePackageChanged$6(i, str2, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePackageChanged$6(int i, String str, String str2) throws Exception {
        Set<EnforcingAdmin> enforcingAdminsOnUser = getEnforcingAdminsOnUser(i);
        if (str != null) {
            for (EnforcingAdmin enforcingAdmin : enforcingAdminsOnUser) {
                if (str.equals(enforcingAdmin.getPackageName())) {
                    removePoliciesForAdmin(enforcingAdmin);
                    return;
                }
            }
        }
        for (EnforcingAdmin enforcingAdmin2 : enforcingAdminsOnUser) {
            if (str2 == null || str2.equals(enforcingAdmin2.getPackageName())) {
                if (!isPackageInstalled(enforcingAdmin2.getPackageName(), i)) {
                    Slogf.i(TAG, String.format("Admin package %s not found for user %d, removing admin policies", enforcingAdmin2.getPackageName(), Integer.valueOf(i)));
                    removePoliciesForAdmin(enforcingAdmin2);
                    return;
                }
            }
        }
        if (str2 != null) {
            updateDeviceAdminServiceOnPackageChanged(str2, i);
            removePersistentPreferredActivityPoliciesForPackage(str2, i);
        }
    }

    private void removePersistentPreferredActivityPoliciesForPackage(String str, int i) {
        Iterator<PolicyKey> it = getLocalPolicyKeysSetByAllAdmins(PolicyDefinition.GENERIC_PERSISTENT_PREFERRED_ACTIVITY, i).iterator();
        while (it.hasNext()) {
            IntentFilterPolicyKey intentFilterPolicyKey = (PolicyKey) it.next();
            if (!(intentFilterPolicyKey instanceof IntentFilterPolicyKey)) {
                throw new IllegalStateException("PolicyKey for PERSISTENT_PREFERRED_ACTIVITY is not of type IntentFilterPolicyKey");
            }
            IntentFilter intentFilter = intentFilterPolicyKey.getIntentFilter();
            Objects.requireNonNull(intentFilter);
            PolicyDefinition<ComponentName> PERSISTENT_PREFERRED_ACTIVITY = PolicyDefinition.PERSISTENT_PREFERRED_ACTIVITY(intentFilter);
            LinkedHashMap localPoliciesSetByAdmins = getLocalPoliciesSetByAdmins(PERSISTENT_PREFERRED_ACTIVITY, i);
            IPackageManager packageManager = AppGlobals.getPackageManager();
            for (EnforcingAdmin enforcingAdmin : localPoliciesSetByAdmins.keySet()) {
                if (((PolicyValue) localPoliciesSetByAdmins.get(enforcingAdmin)).getValue() != null && ((ComponentName) ((PolicyValue) localPoliciesSetByAdmins.get(enforcingAdmin)).getValue()).getPackageName().equals(str)) {
                    try {
                        if (packageManager.getPackageInfo(str, 0L, i) == null || packageManager.getActivityInfo((ComponentName) ((PolicyValue) localPoliciesSetByAdmins.get(enforcingAdmin)).getValue(), 0L, i) == null) {
                            Slogf.e(TAG, String.format("Persistent preferred activity in package %s not found for user %d, removing policy for admin", str, Integer.valueOf(i)));
                            removeLocalPolicy(PERSISTENT_PREFERRED_ACTIVITY, enforcingAdmin, i);
                        }
                    } catch (RemoteException e) {
                        Slogf.wtf(TAG, "Error handling package changes", e);
                    }
                }
            }
        }
    }

    private boolean isPackageInstalled(String str, int i) {
        try {
            return AppGlobals.getPackageManager().getPackageInfo(str, 0L, i) != null;
        } catch (RemoteException e) {
            Slogf.wtf(TAG, "Error handling package changes", e);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleUserRemoved(int i) {
        removeLocalPoliciesForUser(i);
        removePoliciesForAdminsOnUser(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleUserCreated(UserInfo userInfo) {
        enforcePoliciesOnInheritableProfilesIfApplicable(userInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleRoleChanged(String str, int i) {
        if (DEVICE_LOCK_CONTROLLER_ROLE.equals(str)) {
            String roleAuthorityOf = EnforcingAdmin.getRoleAuthorityOf(str);
            for (EnforcingAdmin enforcingAdmin : getEnforcingAdminsOnUser(i)) {
                if (enforcingAdmin.hasAuthority(roleAuthorityOf)) {
                    enforcingAdmin.reloadRoleAuthorities();
                    if (!enforcingAdmin.hasAuthority(roleAuthorityOf)) {
                        removePoliciesForAdmin(enforcingAdmin);
                    }
                }
            }
        }
    }

    private void enforcePoliciesOnInheritableProfilesIfApplicable(final UserInfo userInfo) {
        if (userInfo.isProfile()) {
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda6
                public final void runOrThrow() {
                    DevicePolicyEngine.this.lambda$enforcePoliciesOnInheritableProfilesIfApplicable$7(userInfo);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforcePoliciesOnInheritableProfilesIfApplicable$7(UserInfo userInfo) throws Exception {
        int i;
        UserInfo profileParent;
        UserProperties userProperties = this.mUserManager.getUserProperties(userInfo.getUserHandle());
        if (userProperties == null || userProperties.getInheritDevicePolicy() != 1 || (profileParent = this.mUserManager.getProfileParent((i = userInfo.id))) == null || profileParent.getUserHandle().getIdentifier() == i) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mLocalPolicies.contains(profileParent.getUserHandle().getIdentifier())) {
                Iterator<Map.Entry<PolicyKey, PolicyState<?>>> it = this.mLocalPolicies.get(profileParent.getUserHandle().getIdentifier()).entrySet().iterator();
                while (it.hasNext()) {
                    enforcePolicyOnUserLocked(i, it.next().getValue());
                }
            }
        }
    }

    private <V> void enforcePolicyOnUserLocked(int i, PolicyState<V> policyState) {
        if (policyState.getPolicyDefinition().isInheritable()) {
            for (Map.Entry<EnforcingAdmin, PolicyValue<V>> entry : policyState.getPoliciesSetByAdmins().entrySet()) {
                setLocalPolicy(policyState.getPolicyDefinition(), entry.getKey(), entry.getValue(), i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DevicePolicyState getDevicePolicyState() {
        DevicePolicyState devicePolicyState;
        synchronized (this.mLock) {
            HashMap hashMap = new HashMap();
            for (int i = 0; i < this.mLocalPolicies.size(); i++) {
                UserHandle of = UserHandle.of(this.mLocalPolicies.keyAt(i));
                hashMap.put(of, new HashMap());
                for (PolicyKey policyKey : this.mLocalPolicies.valueAt(i).keySet()) {
                    ((Map) hashMap.get(of)).put(policyKey, this.mLocalPolicies.valueAt(i).get(policyKey).getParcelablePolicyState());
                }
            }
            if (!this.mGlobalPolicies.isEmpty()) {
                hashMap.put(UserHandle.ALL, new HashMap());
                for (PolicyKey policyKey2 : this.mGlobalPolicies.keySet()) {
                    ((Map) hashMap.get(UserHandle.ALL)).put(policyKey2, this.mGlobalPolicies.get(policyKey2).getParcelablePolicyState());
                }
            }
            devicePolicyState = new DevicePolicyState(hashMap);
        }
        return devicePolicyState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removePoliciesForAdmin(EnforcingAdmin enforcingAdmin) {
        synchronized (this.mLock) {
            Iterator it = new HashSet(this.mGlobalPolicies.keySet()).iterator();
            while (it.hasNext()) {
                PolicyState<?> policyState = this.mGlobalPolicies.get((PolicyKey) it.next());
                if (policyState.getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                    removeGlobalPolicy(policyState.getPolicyDefinition(), enforcingAdmin);
                }
            }
            for (int i = 0; i < this.mLocalPolicies.size(); i++) {
                SparseArray<Map<PolicyKey, PolicyState<?>>> sparseArray = this.mLocalPolicies;
                for (PolicyKey policyKey : new HashSet(sparseArray.get(sparseArray.keyAt(i)).keySet())) {
                    SparseArray<Map<PolicyKey, PolicyState<?>>> sparseArray2 = this.mLocalPolicies;
                    PolicyState<?> policyState2 = sparseArray2.get(sparseArray2.keyAt(i)).get(policyKey);
                    if (policyState2.getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                        removeLocalPolicy(policyState2.getPolicyDefinition(), enforcingAdmin, this.mLocalPolicies.keyAt(i));
                    }
                }
            }
        }
    }

    private void removeLocalPoliciesForUser(int i) {
        synchronized (this.mLock) {
            if (this.mLocalPolicies.contains(i)) {
                Iterator it = new HashSet(this.mLocalPolicies.get(i).keySet()).iterator();
                while (it.hasNext()) {
                    PolicyState<?> policyState = this.mLocalPolicies.get(i).get((PolicyKey) it.next());
                    Iterator it2 = new HashSet(policyState.getPoliciesSetByAdmins().keySet()).iterator();
                    while (it2.hasNext()) {
                        removeLocalPolicy(policyState.getPolicyDefinition(), (EnforcingAdmin) it2.next(), i);
                    }
                }
                this.mLocalPolicies.remove(i);
            }
        }
    }

    private void removePoliciesForAdminsOnUser(int i) {
        Iterator<EnforcingAdmin> it = getEnforcingAdminsOnUser(i).iterator();
        while (it.hasNext()) {
            removePoliciesForAdmin(it.next());
        }
    }

    private void updateDeviceAdminServiceOnPackageChanged(String str, int i) {
        for (EnforcingAdmin enforcingAdmin : getEnforcingAdminsOnUser(i)) {
            if (!enforcingAdmin.hasAuthority("enterprise") && str.equals(enforcingAdmin.getPackageName())) {
                this.mDeviceAdminServiceController.startServiceForAdmin(str, i, "package-broadcast");
            }
        }
    }

    private void updateDeviceAdminServiceOnPolicyAddLocked(EnforcingAdmin enforcingAdmin) {
        int userId = enforcingAdmin.getUserId();
        if (this.mEnforcingAdmins.contains(userId) && this.mEnforcingAdmins.get(userId).contains(enforcingAdmin)) {
            return;
        }
        if (!this.mEnforcingAdmins.contains(enforcingAdmin.getUserId())) {
            this.mEnforcingAdmins.put(enforcingAdmin.getUserId(), new HashSet());
        }
        this.mEnforcingAdmins.get(enforcingAdmin.getUserId()).add(enforcingAdmin);
        if (enforcingAdmin.hasAuthority("enterprise")) {
            return;
        }
        this.mDeviceAdminServiceController.startServiceForAdmin(enforcingAdmin.getPackageName(), userId, "policy-added");
    }

    private void updateDeviceAdminServiceOnPolicyRemoveLocked(EnforcingAdmin enforcingAdmin) {
        if (doesAdminHavePoliciesLocked(enforcingAdmin)) {
            return;
        }
        int userId = enforcingAdmin.getUserId();
        if (this.mEnforcingAdmins.contains(userId)) {
            this.mEnforcingAdmins.get(userId).remove(enforcingAdmin);
            if (this.mEnforcingAdmins.get(userId).isEmpty()) {
                this.mEnforcingAdmins.remove(enforcingAdmin.getUserId());
            }
        }
        if (enforcingAdmin.hasAuthority("enterprise")) {
            return;
        }
        this.mDeviceAdminServiceController.stopServiceForAdmin(enforcingAdmin.getPackageName(), userId, "policy-removed");
    }

    private boolean doesAdminHavePoliciesLocked(EnforcingAdmin enforcingAdmin) {
        Iterator<PolicyKey> it = this.mGlobalPolicies.keySet().iterator();
        while (it.hasNext()) {
            if (this.mGlobalPolicies.get(it.next()).getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                return true;
            }
        }
        for (int i = 0; i < this.mLocalPolicies.size(); i++) {
            SparseArray<Map<PolicyKey, PolicyState<?>>> sparseArray = this.mLocalPolicies;
            for (PolicyKey policyKey : sparseArray.get(sparseArray.keyAt(i)).keySet()) {
                SparseArray<Map<PolicyKey, PolicyState<?>>> sparseArray2 = this.mLocalPolicies;
                if (sparseArray2.get(sparseArray2.keyAt(i)).get(policyKey).getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Set<EnforcingAdmin> getEnforcingAdminsOnUser(int i) {
        Set<EnforcingAdmin> emptySet;
        synchronized (this.mLock) {
            emptySet = this.mEnforcingAdmins.contains(i) ? this.mEnforcingAdmins.get(i) : Collections.emptySet();
        }
        return emptySet;
    }

    private void write() {
        synchronized (this.mLock) {
            Log.d(TAG, "Writing device policies to file.");
            new DevicePoliciesReaderWriter().writeToFileLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void load() {
        Log.d(TAG, "Reading device policies from file.");
        synchronized (this.mLock) {
            clear();
            new DevicePoliciesReaderWriter().readFromFileLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <V> void reapplyAllPoliciesLocked() {
        Iterator<PolicyKey> it = this.mGlobalPolicies.keySet().iterator();
        while (it.hasNext()) {
            PolicyState<?> policyState = this.mGlobalPolicies.get(it.next());
            enforcePolicy(policyState.getPolicyDefinition(), policyState.getCurrentResolvedPolicy(), -1);
        }
        for (int i = 0; i < this.mLocalPolicies.size(); i++) {
            int keyAt = this.mLocalPolicies.keyAt(i);
            Iterator<PolicyKey> it2 = this.mLocalPolicies.get(keyAt).keySet().iterator();
            while (it2.hasNext()) {
                PolicyState<?> policyState2 = this.mLocalPolicies.get(keyAt).get(it2.next());
                enforcePolicy(policyState2.getPolicyDefinition(), policyState2.getCurrentResolvedPolicy(), keyAt);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearAllPolicies() {
        clear();
        write();
    }

    private void clear() {
        synchronized (this.mLock) {
            this.mGlobalPolicies.clear();
            this.mLocalPolicies.clear();
            this.mEnforcingAdmins.clear();
        }
    }

    private <V> boolean checkFor2gFailure(PolicyDefinition<V> policyDefinition, EnforcingAdmin enforcingAdmin) {
        boolean z;
        if (!policyDefinition.getPolicyKey().getIdentifier().equals(CELLULAR_2G_USER_RESTRICTION_ID)) {
            return false;
        }
        try {
            z = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).isRadioInterfaceCapabilitySupported("CAPABILITY_USES_ALLOWED_NETWORK_TYPES_BITMASK");
        } catch (IllegalStateException unused) {
            z = false;
        }
        if (z) {
            return false;
        }
        sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, 4, -1);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DevicePoliciesReaderWriter {
        private static final String ATTR_USER_ID = "user-id";
        private static final String DEVICE_POLICIES_XML = "device_policy_state.xml";
        private static final String TAG_ENFORCING_ADMINS_ENTRY = "enforcing-admins-entry";
        private static final String TAG_GLOBAL_POLICY_ENTRY = "global-policy-entry";
        private static final String TAG_LOCAL_POLICY_ENTRY = "local-policy-entry";
        private static final String TAG_POLICY_KEY_ENTRY = "policy-key-entry";
        private static final String TAG_POLICY_STATE_ENTRY = "policy-state-entry";
        private final File mFile;

        private DevicePoliciesReaderWriter() {
            this.mFile = new File(Environment.getDataSystemDirectory(), DEVICE_POLICIES_XML);
        }

        void writeToFileLocked() {
            FileOutputStream startWrite;
            Log.d(DevicePolicyEngine.TAG, "Writing to " + this.mFile);
            AtomicFile atomicFile = new AtomicFile(this.mFile);
            FileOutputStream fileOutputStream = null;
            try {
                startWrite = atomicFile.startWrite();
            } catch (IOException e) {
                e = e;
            }
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                writeInner(resolveSerializer);
                resolveSerializer.endDocument();
                resolveSerializer.flush();
                atomicFile.finishWrite(startWrite);
            } catch (IOException e2) {
                e = e2;
                fileOutputStream = startWrite;
                Log.e(DevicePolicyEngine.TAG, "Exception when writing", e);
                if (fileOutputStream != null) {
                    atomicFile.failWrite(fileOutputStream);
                }
            }
        }

        void writeInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            writeLocalPoliciesInner(typedXmlSerializer);
            writeGlobalPoliciesInner(typedXmlSerializer);
            writeEnforcingAdminsInner(typedXmlSerializer);
        }

        private void writeLocalPoliciesInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            if (DevicePolicyEngine.this.mLocalPolicies != null) {
                for (int i = 0; i < DevicePolicyEngine.this.mLocalPolicies.size(); i++) {
                    int keyAt = DevicePolicyEngine.this.mLocalPolicies.keyAt(i);
                    for (Map.Entry entry : ((Map) DevicePolicyEngine.this.mLocalPolicies.get(keyAt)).entrySet()) {
                        typedXmlSerializer.startTag((String) null, TAG_LOCAL_POLICY_ENTRY);
                        typedXmlSerializer.attributeInt((String) null, ATTR_USER_ID, keyAt);
                        typedXmlSerializer.startTag((String) null, TAG_POLICY_KEY_ENTRY);
                        ((PolicyKey) entry.getKey()).saveToXml(typedXmlSerializer);
                        typedXmlSerializer.endTag((String) null, TAG_POLICY_KEY_ENTRY);
                        typedXmlSerializer.startTag((String) null, TAG_POLICY_STATE_ENTRY);
                        ((PolicyState) entry.getValue()).saveToXml(typedXmlSerializer);
                        typedXmlSerializer.endTag((String) null, TAG_POLICY_STATE_ENTRY);
                        typedXmlSerializer.endTag((String) null, TAG_LOCAL_POLICY_ENTRY);
                    }
                }
            }
        }

        private void writeGlobalPoliciesInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            if (DevicePolicyEngine.this.mGlobalPolicies != null) {
                for (Map.Entry entry : DevicePolicyEngine.this.mGlobalPolicies.entrySet()) {
                    typedXmlSerializer.startTag((String) null, TAG_GLOBAL_POLICY_ENTRY);
                    typedXmlSerializer.startTag((String) null, TAG_POLICY_KEY_ENTRY);
                    ((PolicyKey) entry.getKey()).saveToXml(typedXmlSerializer);
                    typedXmlSerializer.endTag((String) null, TAG_POLICY_KEY_ENTRY);
                    typedXmlSerializer.startTag((String) null, TAG_POLICY_STATE_ENTRY);
                    ((PolicyState) entry.getValue()).saveToXml(typedXmlSerializer);
                    typedXmlSerializer.endTag((String) null, TAG_POLICY_STATE_ENTRY);
                    typedXmlSerializer.endTag((String) null, TAG_GLOBAL_POLICY_ENTRY);
                }
            }
        }

        private void writeEnforcingAdminsInner(TypedXmlSerializer typedXmlSerializer) throws IOException {
            if (DevicePolicyEngine.this.mEnforcingAdmins != null) {
                for (int i = 0; i < DevicePolicyEngine.this.mEnforcingAdmins.size(); i++) {
                    for (EnforcingAdmin enforcingAdmin : (Set) DevicePolicyEngine.this.mEnforcingAdmins.get(DevicePolicyEngine.this.mEnforcingAdmins.keyAt(i))) {
                        typedXmlSerializer.startTag((String) null, TAG_ENFORCING_ADMINS_ENTRY);
                        enforcingAdmin.saveToXml(typedXmlSerializer);
                        typedXmlSerializer.endTag((String) null, TAG_ENFORCING_ADMINS_ENTRY);
                    }
                }
            }
        }

        void readFromFileLocked() {
            if (!this.mFile.exists()) {
                Log.d(DevicePolicyEngine.TAG, "" + this.mFile + " doesn't exist");
                return;
            }
            Log.d(DevicePolicyEngine.TAG, "Reading from " + this.mFile);
            FileInputStream fileInputStream = null;
            try {
                try {
                    fileInputStream = new AtomicFile(this.mFile).openRead();
                    readInner(Xml.resolvePullParser(fileInputStream));
                } catch (IOException | ClassNotFoundException | XmlPullParserException e) {
                    Slogf.wtf(DevicePolicyEngine.TAG, "Error parsing resources file", e);
                }
            } finally {
                IoUtils.closeQuietly(fileInputStream);
            }
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:5:0x0016. Please report as an issue. */
        private void readInner(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException, ClassNotFoundException {
            int depth = typedXmlPullParser.getDepth();
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                String name = typedXmlPullParser.getName();
                name.hashCode();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1900677631:
                        if (name.equals(TAG_GLOBAL_POLICY_ENTRY)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1329955015:
                        if (name.equals(TAG_LOCAL_POLICY_ENTRY)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1016501079:
                        if (name.equals(TAG_ENFORCING_ADMINS_ENTRY)) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        readGlobalPoliciesInner(typedXmlPullParser);
                        break;
                    case 1:
                        readLocalPoliciesInner(typedXmlPullParser);
                        break;
                    case 2:
                        readEnforcingAdminsInner(typedXmlPullParser);
                        break;
                    default:
                        Slogf.wtf(DevicePolicyEngine.TAG, "Unknown tag " + name);
                        break;
                }
            }
        }

        private void readLocalPoliciesInner(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
            PolicyKey policyKey = null;
            int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_USER_ID);
            int depth = typedXmlPullParser.getDepth();
            Object obj = null;
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                String name = typedXmlPullParser.getName();
                name.hashCode();
                if (name.equals(TAG_POLICY_KEY_ENTRY)) {
                    policyKey = PolicyDefinition.readPolicyKeyFromXml(typedXmlPullParser);
                } else if (name.equals(TAG_POLICY_STATE_ENTRY)) {
                    obj = PolicyState.readFromXml(typedXmlPullParser);
                } else {
                    Slogf.wtf(DevicePolicyEngine.TAG, "Unknown tag for local policy entry" + name);
                }
            }
            if (policyKey != null && obj != null) {
                if (!DevicePolicyEngine.this.mLocalPolicies.contains(attributeInt)) {
                    DevicePolicyEngine.this.mLocalPolicies.put(attributeInt, new HashMap());
                }
                ((Map) DevicePolicyEngine.this.mLocalPolicies.get(attributeInt)).put(policyKey, obj);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Error parsing local policy, policyKey is ");
            if (policyKey == null) {
                policyKey = "null";
            }
            sb.append(policyKey);
            sb.append(", and policyState is ");
            if (obj == null) {
                obj = "null";
            }
            sb.append(obj);
            sb.append(".");
            Slogf.wtf(DevicePolicyEngine.TAG, sb.toString());
        }

        private void readGlobalPoliciesInner(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
            int depth = typedXmlPullParser.getDepth();
            PolicyKey policyKey = null;
            Object obj = null;
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                String name = typedXmlPullParser.getName();
                name.hashCode();
                if (name.equals(TAG_POLICY_KEY_ENTRY)) {
                    policyKey = PolicyDefinition.readPolicyKeyFromXml(typedXmlPullParser);
                } else if (name.equals(TAG_POLICY_STATE_ENTRY)) {
                    obj = PolicyState.readFromXml(typedXmlPullParser);
                } else {
                    Slogf.wtf(DevicePolicyEngine.TAG, "Unknown tag for local policy entry" + name);
                }
            }
            if (policyKey != null && obj != null) {
                DevicePolicyEngine.this.mGlobalPolicies.put(policyKey, obj);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Error parsing global policy, policyKey is ");
            if (policyKey == null) {
                policyKey = "null";
            }
            sb.append(policyKey);
            sb.append(", and policyState is ");
            if (obj == null) {
                obj = "null";
            }
            sb.append(obj);
            sb.append(".");
            Slogf.wtf(DevicePolicyEngine.TAG, sb.toString());
        }

        private void readEnforcingAdminsInner(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException {
            EnforcingAdmin readFromXml = EnforcingAdmin.readFromXml(typedXmlPullParser);
            if (readFromXml == null) {
                Slogf.wtf(DevicePolicyEngine.TAG, "Error parsing enforcingAdmins, EnforcingAdmin is null.");
                return;
            }
            if (!DevicePolicyEngine.this.mEnforcingAdmins.contains(readFromXml.getUserId())) {
                DevicePolicyEngine.this.mEnforcingAdmins.put(readFromXml.getUserId(), new HashSet());
            }
            ((Set) DevicePolicyEngine.this.mEnforcingAdmins.get(readFromXml.getUserId())).add(readFromXml);
        }
    }
}
