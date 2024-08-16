package com.android.server.devicepolicy;

import android.app.admin.DeviceAdminInfo;
import android.content.ComponentName;
import android.os.FileUtils;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.IndentingPrintWriter;
import android.util.Xml;
import com.android.internal.util.JournaledFile;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.utils.Slogf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DevicePolicyData {
    private static final String ATTR_ALIAS = "alias";
    private static final String ATTR_DEVICE_PAIRED = "device-paired";
    private static final String ATTR_DEVICE_PROVISIONING_CONFIG_APPLIED = "device-provisioning-config-applied";
    private static final String ATTR_DISABLED = "disabled";
    private static final String ATTR_FACTORY_RESET_FLAGS = "factory-reset-flags";
    private static final String ATTR_FACTORY_RESET_REASON = "factory-reset-reason";
    private static final String ATTR_ID = "id";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_NEW_USER_DISCLAIMER = "new-user-disclaimer";
    private static final String ATTR_PERMISSION_POLICY = "permission-policy";
    private static final String ATTR_PERMISSION_PROVIDER = "permission-provider";
    private static final String ATTR_PROVISIONING_STATE = "provisioning-state";
    private static final String ATTR_SETUP_COMPLETE = "setup-complete";
    private static final String ATTR_VALUE = "value";
    public static final int FACTORY_RESET_FLAG_ON_BOOT = 1;
    public static final int FACTORY_RESET_FLAG_WIPE_EUICC = 4;
    public static final int FACTORY_RESET_FLAG_WIPE_EXTERNAL_STORAGE = 2;
    public static final int FACTORY_RESET_FLAG_WIPE_FACTORY_RESET_PROTECTION = 8;
    static final String NEW_USER_DISCLAIMER_ACKNOWLEDGED = "acked";
    static final String NEW_USER_DISCLAIMER_NEEDED = "needed";
    static final String NEW_USER_DISCLAIMER_NOT_NEEDED = "not_needed";
    private static final String TAG = "DevicePolicyManager";
    private static final String TAG_ACCEPTED_CA_CERTIFICATES = "accepted-ca-certificate";
    private static final String TAG_ADMIN_BROADCAST_PENDING = "admin-broadcast-pending";
    private static final String TAG_AFFILIATION_ID = "affiliation-id";
    private static final String TAG_APPS_SUSPENDED = "apps-suspended";
    private static final String TAG_BYPASS_ROLE_QUALIFICATIONS = "bypass-role-qualifications";
    private static final String TAG_CURRENT_INPUT_METHOD_SET = "current-ime-set";
    private static final String TAG_DO_NOT_ASK_CREDENTIALS_ON_BOOT = "do-not-ask-credentials-on-boot";
    private static final String TAG_INITIALIZATION_BUNDLE = "initialization-bundle";
    private static final String TAG_KEEP_PROFILES_RUNNING = "keep-profiles-running";
    private static final String TAG_LAST_BUG_REPORT_REQUEST = "last-bug-report-request";
    private static final String TAG_LAST_NETWORK_LOG_RETRIEVAL = "last-network-log-retrieval";
    private static final String TAG_LAST_SECURITY_LOG_RETRIEVAL = "last-security-log-retrieval";
    private static final String TAG_LOCK_TASK_COMPONENTS = "lock-task-component";
    private static final String TAG_LOCK_TASK_FEATURES = "lock-task-features";
    private static final String TAG_OWNER_INSTALLED_CA_CERT = "owner-installed-ca-cert";
    private static final String TAG_PASSWORD_TOKEN_HANDLE = "password-token";
    private static final String TAG_PROTECTED_PACKAGES = "protected-packages";
    private static final String TAG_SECONDARY_LOCK_SCREEN = "secondary-lock-screen";
    private static final String TAG_STATUS_BAR = "statusbar";
    private static final boolean VERBOSE_LOG = false;
    String mCurrentRoleHolder;
    int mFactoryResetFlags;
    String mFactoryResetReason;
    ActiveAdmin mPermissionBasedAdmin;
    int mPermissionPolicy;
    ComponentName mRestrictionsProvider;

    @Deprecated
    List<String> mUserControlDisabledPackages;
    final int mUserId;
    int mUserProvisioningState;
    int mFailedPasswordAttempts = 0;
    boolean mPasswordValidAtLastCheckpoint = true;
    int mPasswordOwner = -1;
    long mLastMaximumTimeToLock = -1;
    boolean mUserSetupComplete = false;
    boolean mBypassDevicePolicyManagementRoleQualifications = false;
    boolean mPaired = false;
    boolean mDeviceProvisioningConfigApplied = false;
    final ArrayMap<ComponentName, ActiveAdmin> mAdminMap = new ArrayMap<>();
    final ArrayList<ActiveAdmin> mAdminList = new ArrayList<>();
    final ArrayList<ComponentName> mRemovingAdmins = new ArrayList<>();
    final ArraySet<String> mAcceptedCaCertificates = new ArraySet<>();
    List<String> mLockTaskPackages = new ArrayList();
    int mLockTaskFeatures = 16;
    boolean mStatusBarDisabled = false;
    final ArrayMap<String, List<String>> mDelegationMap = new ArrayMap<>();
    boolean mDoNotAskCredentialsOnBoot = false;
    Set<String> mAffiliationIds = new ArraySet();
    long mLastSecurityLogRetrievalTime = -1;
    long mLastBugReportRequestTime = -1;
    long mLastNetworkLogsRetrievalTime = -1;
    boolean mCurrentInputMethodSet = false;
    boolean mSecondaryLockscreenEnabled = false;
    Set<String> mOwnerInstalledCaCerts = new ArraySet();
    boolean mAdminBroadcastPending = false;
    PersistableBundle mInitBundle = null;
    long mPasswordTokenHandle = 0;
    boolean mAppsSuspended = false;
    String mNewUserDisclaimer = NEW_USER_DISCLAIMER_NOT_NEEDED;
    boolean mEffectiveKeepProfilesRunning = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveAdmin createOrGetPermissionBasedAdmin(int i) {
        if (this.mPermissionBasedAdmin == null) {
            this.mPermissionBasedAdmin = new ActiveAdmin(i, true);
        }
        return this.mPermissionBasedAdmin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DevicePolicyData(int i) {
        this.mUserId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean store(DevicePolicyData devicePolicyData, JournaledFile journaledFile) {
        File file;
        FileOutputStream fileOutputStream;
        String str;
        TypedXmlSerializer resolveSerializer;
        String str2;
        String str3 = TAG_DO_NOT_ASK_CREDENTIALS_ON_BOOT;
        String str4 = TAG_STATUS_BAR;
        try {
            file = journaledFile.chooseForWrite();
            try {
                str = TAG_AFFILIATION_ID;
                FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
                try {
                    resolveSerializer = Xml.resolveSerializer(fileOutputStream2);
                    try {
                        fileOutputStream = fileOutputStream2;
                    } catch (IOException | XmlPullParserException e) {
                        e = e;
                        fileOutputStream = fileOutputStream2;
                    }
                } catch (IOException | XmlPullParserException e2) {
                    e = e2;
                    fileOutputStream = fileOutputStream2;
                }
            } catch (IOException | XmlPullParserException e3) {
                e = e3;
                fileOutputStream = null;
            }
        } catch (IOException | XmlPullParserException e4) {
            e = e4;
            file = null;
            fileOutputStream = null;
        }
        try {
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, "policies");
            ComponentName componentName = devicePolicyData.mRestrictionsProvider;
            if (componentName != null) {
                str2 = "policies";
                resolveSerializer.attribute((String) null, ATTR_PERMISSION_PROVIDER, componentName.flattenToString());
            } else {
                str2 = "policies";
            }
            if (devicePolicyData.mUserSetupComplete) {
                resolveSerializer.attributeBoolean((String) null, ATTR_SETUP_COMPLETE, true);
            }
            if (devicePolicyData.mPaired) {
                resolveSerializer.attributeBoolean((String) null, ATTR_DEVICE_PAIRED, true);
            }
            if (devicePolicyData.mDeviceProvisioningConfigApplied) {
                resolveSerializer.attributeBoolean((String) null, ATTR_DEVICE_PROVISIONING_CONFIG_APPLIED, true);
            }
            int i = devicePolicyData.mUserProvisioningState;
            if (i != 0) {
                resolveSerializer.attributeInt((String) null, ATTR_PROVISIONING_STATE, i);
            }
            int i2 = devicePolicyData.mPermissionPolicy;
            if (i2 != 0) {
                resolveSerializer.attributeInt((String) null, ATTR_PERMISSION_POLICY, i2);
            }
            if (NEW_USER_DISCLAIMER_NEEDED.equals(devicePolicyData.mNewUserDisclaimer)) {
                resolveSerializer.attribute((String) null, ATTR_NEW_USER_DISCLAIMER, devicePolicyData.mNewUserDisclaimer);
            }
            int i3 = devicePolicyData.mFactoryResetFlags;
            if (i3 != 0) {
                resolveSerializer.attributeInt((String) null, ATTR_FACTORY_RESET_FLAGS, i3);
            }
            String str5 = devicePolicyData.mFactoryResetReason;
            if (str5 != null) {
                resolveSerializer.attribute((String) null, ATTR_FACTORY_RESET_REASON, str5);
            }
            for (int i4 = 0; i4 < devicePolicyData.mDelegationMap.size(); i4++) {
                String keyAt = devicePolicyData.mDelegationMap.keyAt(i4);
                Iterator<String> it = devicePolicyData.mDelegationMap.valueAt(i4).iterator();
                while (it.hasNext()) {
                    Iterator<String> it2 = it;
                    String next = it.next();
                    resolveSerializer.startTag((String) null, "delegation");
                    resolveSerializer.attribute((String) null, "delegatePackage", keyAt);
                    resolveSerializer.attribute((String) null, "scope", next);
                    resolveSerializer.endTag((String) null, "delegation");
                    str3 = str3;
                    it = it2;
                    str4 = str4;
                }
            }
            String str6 = str3;
            String str7 = str4;
            int size = devicePolicyData.mAdminList.size();
            for (int i5 = 0; i5 < size; i5++) {
                ActiveAdmin activeAdmin = devicePolicyData.mAdminList.get(i5);
                if (activeAdmin != null) {
                    resolveSerializer.startTag((String) null, "admin");
                    resolveSerializer.attribute((String) null, ATTR_NAME, activeAdmin.info.getComponent().flattenToString());
                    activeAdmin.writeToXml(resolveSerializer);
                    resolveSerializer.endTag((String) null, "admin");
                }
            }
            if (devicePolicyData.mPermissionBasedAdmin != null) {
                resolveSerializer.startTag((String) null, "permission-based-admin");
                devicePolicyData.mPermissionBasedAdmin.writeToXml(resolveSerializer);
                resolveSerializer.endTag((String) null, "permission-based-admin");
            }
            if (devicePolicyData.mPasswordOwner >= 0) {
                resolveSerializer.startTag((String) null, "password-owner");
                resolveSerializer.attributeInt((String) null, ATTR_VALUE, devicePolicyData.mPasswordOwner);
                resolveSerializer.endTag((String) null, "password-owner");
            }
            if (devicePolicyData.mFailedPasswordAttempts != 0) {
                resolveSerializer.startTag((String) null, "failed-password-attempts");
                resolveSerializer.attributeInt((String) null, ATTR_VALUE, devicePolicyData.mFailedPasswordAttempts);
                resolveSerializer.endTag((String) null, "failed-password-attempts");
            }
            for (int i6 = 0; i6 < devicePolicyData.mAcceptedCaCertificates.size(); i6++) {
                resolveSerializer.startTag((String) null, TAG_ACCEPTED_CA_CERTIFICATES);
                resolveSerializer.attribute((String) null, ATTR_NAME, devicePolicyData.mAcceptedCaCertificates.valueAt(i6));
                resolveSerializer.endTag((String) null, TAG_ACCEPTED_CA_CERTIFICATES);
            }
            for (int i7 = 0; i7 < devicePolicyData.mLockTaskPackages.size(); i7++) {
                String str8 = devicePolicyData.mLockTaskPackages.get(i7);
                resolveSerializer.startTag((String) null, TAG_LOCK_TASK_COMPONENTS);
                resolveSerializer.attribute((String) null, ATTR_NAME, str8);
                resolveSerializer.endTag((String) null, TAG_LOCK_TASK_COMPONENTS);
            }
            if (devicePolicyData.mLockTaskFeatures != 0) {
                resolveSerializer.startTag((String) null, TAG_LOCK_TASK_FEATURES);
                resolveSerializer.attributeInt((String) null, ATTR_VALUE, devicePolicyData.mLockTaskFeatures);
                resolveSerializer.endTag((String) null, TAG_LOCK_TASK_FEATURES);
            }
            if (devicePolicyData.mSecondaryLockscreenEnabled) {
                resolveSerializer.startTag((String) null, TAG_SECONDARY_LOCK_SCREEN);
                resolveSerializer.attributeBoolean((String) null, ATTR_VALUE, true);
                resolveSerializer.endTag((String) null, TAG_SECONDARY_LOCK_SCREEN);
            }
            if (devicePolicyData.mStatusBarDisabled) {
                resolveSerializer.startTag((String) null, str7);
                resolveSerializer.attributeBoolean((String) null, ATTR_DISABLED, devicePolicyData.mStatusBarDisabled);
                resolveSerializer.endTag((String) null, str7);
            }
            if (devicePolicyData.mDoNotAskCredentialsOnBoot) {
                resolveSerializer.startTag((String) null, str6);
                resolveSerializer.endTag((String) null, str6);
            }
            for (String str9 : devicePolicyData.mAffiliationIds) {
                String str10 = str;
                resolveSerializer.startTag((String) null, str10);
                resolveSerializer.attribute((String) null, ATTR_ID, str9);
                resolveSerializer.endTag((String) null, str10);
                str = str10;
            }
            if (devicePolicyData.mLastSecurityLogRetrievalTime >= 0) {
                resolveSerializer.startTag((String) null, TAG_LAST_SECURITY_LOG_RETRIEVAL);
                resolveSerializer.attributeLong((String) null, ATTR_VALUE, devicePolicyData.mLastSecurityLogRetrievalTime);
                resolveSerializer.endTag((String) null, TAG_LAST_SECURITY_LOG_RETRIEVAL);
            }
            if (devicePolicyData.mLastBugReportRequestTime >= 0) {
                resolveSerializer.startTag((String) null, TAG_LAST_BUG_REPORT_REQUEST);
                resolveSerializer.attributeLong((String) null, ATTR_VALUE, devicePolicyData.mLastBugReportRequestTime);
                resolveSerializer.endTag((String) null, TAG_LAST_BUG_REPORT_REQUEST);
            }
            if (devicePolicyData.mLastNetworkLogsRetrievalTime >= 0) {
                resolveSerializer.startTag((String) null, TAG_LAST_NETWORK_LOG_RETRIEVAL);
                resolveSerializer.attributeLong((String) null, ATTR_VALUE, devicePolicyData.mLastNetworkLogsRetrievalTime);
                resolveSerializer.endTag((String) null, TAG_LAST_NETWORK_LOG_RETRIEVAL);
            }
            if (devicePolicyData.mAdminBroadcastPending) {
                resolveSerializer.startTag((String) null, TAG_ADMIN_BROADCAST_PENDING);
                resolveSerializer.attributeBoolean((String) null, ATTR_VALUE, devicePolicyData.mAdminBroadcastPending);
                resolveSerializer.endTag((String) null, TAG_ADMIN_BROADCAST_PENDING);
            }
            if (devicePolicyData.mInitBundle != null) {
                resolveSerializer.startTag((String) null, TAG_INITIALIZATION_BUNDLE);
                devicePolicyData.mInitBundle.saveToXml(resolveSerializer);
                resolveSerializer.endTag((String) null, TAG_INITIALIZATION_BUNDLE);
            }
            if (devicePolicyData.mPasswordTokenHandle != 0) {
                resolveSerializer.startTag((String) null, TAG_PASSWORD_TOKEN_HANDLE);
                resolveSerializer.attributeLong((String) null, ATTR_VALUE, devicePolicyData.mPasswordTokenHandle);
                resolveSerializer.endTag((String) null, TAG_PASSWORD_TOKEN_HANDLE);
            }
            if (devicePolicyData.mCurrentInputMethodSet) {
                resolveSerializer.startTag((String) null, TAG_CURRENT_INPUT_METHOD_SET);
                resolveSerializer.endTag((String) null, TAG_CURRENT_INPUT_METHOD_SET);
            }
            for (String str11 : devicePolicyData.mOwnerInstalledCaCerts) {
                resolveSerializer.startTag((String) null, TAG_OWNER_INSTALLED_CA_CERT);
                resolveSerializer.attribute((String) null, ATTR_ALIAS, str11);
                resolveSerializer.endTag((String) null, TAG_OWNER_INSTALLED_CA_CERT);
            }
            if (devicePolicyData.mAppsSuspended) {
                resolveSerializer.startTag((String) null, TAG_APPS_SUSPENDED);
                resolveSerializer.attributeBoolean((String) null, ATTR_VALUE, devicePolicyData.mAppsSuspended);
                resolveSerializer.endTag((String) null, TAG_APPS_SUSPENDED);
            }
            if (devicePolicyData.mBypassDevicePolicyManagementRoleQualifications) {
                resolveSerializer.startTag((String) null, TAG_BYPASS_ROLE_QUALIFICATIONS);
                resolveSerializer.attribute((String) null, ATTR_VALUE, devicePolicyData.mCurrentRoleHolder);
                resolveSerializer.endTag((String) null, TAG_BYPASS_ROLE_QUALIFICATIONS);
            }
            if (devicePolicyData.mEffectiveKeepProfilesRunning) {
                resolveSerializer.startTag((String) null, TAG_KEEP_PROFILES_RUNNING);
                resolveSerializer.attributeBoolean((String) null, ATTR_VALUE, devicePolicyData.mEffectiveKeepProfilesRunning);
                resolveSerializer.endTag((String) null, TAG_KEEP_PROFILES_RUNNING);
            }
            resolveSerializer.endTag((String) null, str2);
            resolveSerializer.endDocument();
            fileOutputStream.flush();
            FileUtils.sync(fileOutputStream);
            fileOutputStream.close();
            journaledFile.commit();
            return true;
        } catch (IOException | XmlPullParserException e5) {
            e = e5;
            file = file;
            Slogf.w(TAG, e, "failed writing file %s", new Object[]{file});
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException unused) {
                }
            }
            journaledFile.rollback();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0324 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void load(DevicePolicyData devicePolicyData, JournaledFile journaledFile, Function<ComponentName, DeviceAdminInfo> function, ComponentName componentName) {
        FileInputStream fileInputStream;
        TypedXmlPullParser resolvePullParser;
        int next;
        String name;
        File chooseForRead = journaledFile.chooseForRead();
        FileInputStream fileInputStream2 = null;
        try {
            fileInputStream = new FileInputStream(chooseForRead);
        } catch (FileNotFoundException unused) {
        } catch (IOException | IndexOutOfBoundsException | NullPointerException | NumberFormatException | XmlPullParserException e) {
            e = e;
        }
        try {
            try {
                resolvePullParser = Xml.resolvePullParser(fileInputStream);
                do {
                    next = resolvePullParser.next();
                    if (next == 1) {
                        break;
                    }
                } while (next != 2);
                name = resolvePullParser.getName();
            } catch (IOException | IndexOutOfBoundsException | NullPointerException | NumberFormatException | XmlPullParserException e2) {
                e = e2;
                fileInputStream2 = fileInputStream;
                Slogf.w(TAG, e, "failed parsing %s", new Object[]{chooseForRead});
                fileInputStream = fileInputStream2;
                if (fileInputStream != null) {
                }
                devicePolicyData.mAdminList.addAll(devicePolicyData.mAdminMap.values());
            }
        } catch (FileNotFoundException unused2) {
            fileInputStream2 = fileInputStream;
            fileInputStream = fileInputStream2;
            if (fileInputStream != null) {
            }
            devicePolicyData.mAdminList.addAll(devicePolicyData.mAdminMap.values());
        }
        if (!"policies".equals(name)) {
            throw new XmlPullParserException("Settings do not start with policies tag: found " + name);
        }
        String attributeValue = resolvePullParser.getAttributeValue((String) null, ATTR_PERMISSION_PROVIDER);
        if (attributeValue != null) {
            devicePolicyData.mRestrictionsProvider = ComponentName.unflattenFromString(attributeValue);
        }
        if (Boolean.toString(true).equals(resolvePullParser.getAttributeValue((String) null, ATTR_SETUP_COMPLETE))) {
            devicePolicyData.mUserSetupComplete = true;
        }
        if (Boolean.toString(true).equals(resolvePullParser.getAttributeValue((String) null, ATTR_DEVICE_PAIRED))) {
            devicePolicyData.mPaired = true;
        }
        if (Boolean.toString(true).equals(resolvePullParser.getAttributeValue((String) null, ATTR_DEVICE_PROVISIONING_CONFIG_APPLIED))) {
            devicePolicyData.mDeviceProvisioningConfigApplied = true;
        }
        int attributeInt = resolvePullParser.getAttributeInt((String) null, ATTR_PROVISIONING_STATE, -1);
        if (attributeInt != -1) {
            devicePolicyData.mUserProvisioningState = attributeInt;
        }
        int attributeInt2 = resolvePullParser.getAttributeInt((String) null, ATTR_PERMISSION_POLICY, -1);
        if (attributeInt2 != -1) {
            devicePolicyData.mPermissionPolicy = attributeInt2;
        }
        devicePolicyData.mNewUserDisclaimer = resolvePullParser.getAttributeValue((String) null, ATTR_NEW_USER_DISCLAIMER);
        devicePolicyData.mFactoryResetFlags = resolvePullParser.getAttributeInt((String) null, ATTR_FACTORY_RESET_FLAGS, 0);
        devicePolicyData.mFactoryResetReason = resolvePullParser.getAttributeValue((String) null, ATTR_FACTORY_RESET_REASON);
        int depth = resolvePullParser.getDepth();
        devicePolicyData.mLockTaskPackages.clear();
        devicePolicyData.mAdminList.clear();
        devicePolicyData.mAdminMap.clear();
        devicePolicyData.mPermissionBasedAdmin = null;
        devicePolicyData.mAffiliationIds.clear();
        devicePolicyData.mOwnerInstalledCaCerts.clear();
        devicePolicyData.mUserControlDisabledPackages = null;
        while (true) {
            int next2 = resolvePullParser.next();
            if (next2 == 1 || (next2 == 3 && resolvePullParser.getDepth() <= depth)) {
                break;
            }
            if (next2 != 3 && next2 != 4) {
                String name2 = resolvePullParser.getName();
                if ("admin".equals(name2)) {
                    String attributeValue2 = resolvePullParser.getAttributeValue((String) null, ATTR_NAME);
                    try {
                        DeviceAdminInfo apply = function.apply(ComponentName.unflattenFromString(attributeValue2));
                        if (apply != null) {
                            boolean z = !apply.getComponent().equals(componentName);
                            ActiveAdmin activeAdmin = new ActiveAdmin(apply, false);
                            activeAdmin.readFromXml(resolvePullParser, z);
                            devicePolicyData.mAdminMap.put(activeAdmin.info.getComponent(), activeAdmin);
                        }
                    } catch (RuntimeException e3) {
                        Slogf.w(TAG, e3, "Failed loading admin %s", new Object[]{attributeValue2});
                    }
                } else if ("permission-based-admin".equals(name2)) {
                    ActiveAdmin activeAdmin2 = new ActiveAdmin(devicePolicyData.mUserId, true);
                    activeAdmin2.readFromXml(resolvePullParser, false);
                    devicePolicyData.mPermissionBasedAdmin = activeAdmin2;
                } else if ("delegation".equals(name2)) {
                    String attributeValue3 = resolvePullParser.getAttributeValue((String) null, "delegatePackage");
                    String attributeValue4 = resolvePullParser.getAttributeValue((String) null, "scope");
                    List<String> list = devicePolicyData.mDelegationMap.get(attributeValue3);
                    if (list == null) {
                        list = new ArrayList<>();
                        devicePolicyData.mDelegationMap.put(attributeValue3, list);
                    }
                    if (!list.contains(attributeValue4)) {
                        list.add(attributeValue4);
                    }
                } else if ("failed-password-attempts".equals(name2)) {
                    devicePolicyData.mFailedPasswordAttempts = resolvePullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if ("password-owner".equals(name2)) {
                    devicePolicyData.mPasswordOwner = resolvePullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_ACCEPTED_CA_CERTIFICATES.equals(name2)) {
                    devicePolicyData.mAcceptedCaCertificates.add(resolvePullParser.getAttributeValue((String) null, ATTR_NAME));
                } else if (TAG_LOCK_TASK_COMPONENTS.equals(name2)) {
                    devicePolicyData.mLockTaskPackages.add(resolvePullParser.getAttributeValue((String) null, ATTR_NAME));
                } else if (TAG_LOCK_TASK_FEATURES.equals(name2)) {
                    devicePolicyData.mLockTaskFeatures = resolvePullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_SECONDARY_LOCK_SCREEN.equals(name2)) {
                    devicePolicyData.mSecondaryLockscreenEnabled = resolvePullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_STATUS_BAR.equals(name2)) {
                    devicePolicyData.mStatusBarDisabled = resolvePullParser.getAttributeBoolean((String) null, ATTR_DISABLED, false);
                } else if (TAG_DO_NOT_ASK_CREDENTIALS_ON_BOOT.equals(name2)) {
                    devicePolicyData.mDoNotAskCredentialsOnBoot = true;
                } else if (TAG_AFFILIATION_ID.equals(name2)) {
                    devicePolicyData.mAffiliationIds.add(resolvePullParser.getAttributeValue((String) null, ATTR_ID));
                } else if (TAG_LAST_SECURITY_LOG_RETRIEVAL.equals(name2)) {
                    devicePolicyData.mLastSecurityLogRetrievalTime = resolvePullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_LAST_BUG_REPORT_REQUEST.equals(name2)) {
                    devicePolicyData.mLastBugReportRequestTime = resolvePullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_LAST_NETWORK_LOG_RETRIEVAL.equals(name2)) {
                    devicePolicyData.mLastNetworkLogsRetrievalTime = resolvePullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_ADMIN_BROADCAST_PENDING.equals(name2)) {
                    devicePolicyData.mAdminBroadcastPending = Boolean.toString(true).equals(resolvePullParser.getAttributeValue((String) null, ATTR_VALUE));
                } else if (TAG_INITIALIZATION_BUNDLE.equals(name2)) {
                    devicePolicyData.mInitBundle = PersistableBundle.restoreFromXml(resolvePullParser);
                } else if (TAG_PASSWORD_TOKEN_HANDLE.equals(name2)) {
                    devicePolicyData.mPasswordTokenHandle = resolvePullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_CURRENT_INPUT_METHOD_SET.equals(name2)) {
                    devicePolicyData.mCurrentInputMethodSet = true;
                } else if (TAG_OWNER_INSTALLED_CA_CERT.equals(name2)) {
                    devicePolicyData.mOwnerInstalledCaCerts.add(resolvePullParser.getAttributeValue((String) null, ATTR_ALIAS));
                } else if (TAG_APPS_SUSPENDED.equals(name2)) {
                    devicePolicyData.mAppsSuspended = resolvePullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_BYPASS_ROLE_QUALIFICATIONS.equals(name2)) {
                    devicePolicyData.mBypassDevicePolicyManagementRoleQualifications = true;
                    devicePolicyData.mCurrentRoleHolder = resolvePullParser.getAttributeValue((String) null, ATTR_VALUE);
                } else if (TAG_KEEP_PROFILES_RUNNING.equals(name2)) {
                    devicePolicyData.mEffectiveKeepProfilesRunning = resolvePullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_PROTECTED_PACKAGES.equals(name2)) {
                    if (devicePolicyData.mUserControlDisabledPackages == null) {
                        devicePolicyData.mUserControlDisabledPackages = new ArrayList();
                    }
                    devicePolicyData.mUserControlDisabledPackages.add(resolvePullParser.getAttributeValue((String) null, ATTR_NAME));
                } else {
                    Slogf.w(TAG, "Unknown tag: %s", new Object[]{name2});
                    XmlUtils.skipCurrentTag(resolvePullParser);
                }
            }
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException unused3) {
            }
        }
        devicePolicyData.mAdminList.addAll(devicePolicyData.mAdminMap.values());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void validatePasswordOwner() {
        if (this.mPasswordOwner >= 0) {
            boolean z = true;
            int size = this.mAdminList.size() - 1;
            while (true) {
                if (size < 0) {
                    z = false;
                    break;
                } else if (this.mAdminList.get(size).getUid() == this.mPasswordOwner) {
                    break;
                } else {
                    size--;
                }
            }
            if (z) {
                return;
            }
            Slogf.w(TAG, "Previous password owner %s no longer active; disabling", new Object[]{Integer.valueOf(this.mPasswordOwner)});
            this.mPasswordOwner = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDelayedFactoryReset(String str, boolean z, boolean z2, boolean z3) {
        this.mFactoryResetReason = str;
        this.mFactoryResetFlags = 1;
        if (z) {
            this.mFactoryResetFlags = 1 | 2;
        }
        if (z2) {
            this.mFactoryResetFlags |= 4;
        }
        if (z3) {
            this.mFactoryResetFlags |= 8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNewUserDisclaimerAcknowledged() {
        String str = this.mNewUserDisclaimer;
        if (str == null) {
            int i = this.mUserId;
            if (i == 0) {
                return true;
            }
            Slogf.w(TAG, "isNewUserDisclaimerAcknowledged(%d): mNewUserDisclaimer is null", new Object[]{Integer.valueOf(i)});
            return false;
        }
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1238968671:
                if (str.equals(NEW_USER_DISCLAIMER_NOT_NEEDED)) {
                    c = 0;
                    break;
                }
                break;
            case -1049376843:
                if (str.equals(NEW_USER_DISCLAIMER_NEEDED)) {
                    c = 1;
                    break;
                }
                break;
            case 92636904:
                if (str.equals(NEW_USER_DISCLAIMER_ACKNOWLEDGED)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 2:
                return true;
            default:
                Slogf.w(TAG, "isNewUserDisclaimerAcknowledged(%d): invalid value %d", new Object[]{Integer.valueOf(this.mUserId), this.mNewUserDisclaimer});
            case 1:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println();
        indentingPrintWriter.println("Enabled Device Admins (User " + this.mUserId + ", provisioningState: " + this.mUserProvisioningState + "):");
        int size = this.mAdminList.size();
        for (int i = 0; i < size; i++) {
            ActiveAdmin activeAdmin = this.mAdminList.get(i);
            if (activeAdmin != null) {
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.print(activeAdmin.info.getComponent().flattenToShortString());
                indentingPrintWriter.println(":");
                indentingPrintWriter.increaseIndent();
                activeAdmin.dump(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.decreaseIndent();
            }
        }
        if (!this.mRemovingAdmins.isEmpty()) {
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("Removing Device Admins (User " + this.mUserId + "): " + this.mRemovingAdmins);
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.println();
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("mPasswordOwner=");
        indentingPrintWriter.println(this.mPasswordOwner);
        indentingPrintWriter.print("mPasswordTokenHandle=");
        indentingPrintWriter.println(Long.toHexString(this.mPasswordTokenHandle));
        indentingPrintWriter.print("mAppsSuspended=");
        indentingPrintWriter.println(this.mAppsSuspended);
        indentingPrintWriter.print("mUserSetupComplete=");
        indentingPrintWriter.println(this.mUserSetupComplete);
        indentingPrintWriter.print("mAffiliationIds=");
        indentingPrintWriter.println(this.mAffiliationIds);
        indentingPrintWriter.print("mNewUserDisclaimer=");
        indentingPrintWriter.println(this.mNewUserDisclaimer);
        if (this.mFactoryResetFlags != 0) {
            indentingPrintWriter.print("mFactoryResetFlags=");
            indentingPrintWriter.print(this.mFactoryResetFlags);
            indentingPrintWriter.print(" (");
            indentingPrintWriter.print(factoryResetFlagsToString(this.mFactoryResetFlags));
            indentingPrintWriter.println(')');
        }
        if (this.mFactoryResetReason != null) {
            indentingPrintWriter.print("mFactoryResetReason=");
            indentingPrintWriter.println(this.mFactoryResetReason);
        }
        if (this.mDelegationMap.size() != 0) {
            indentingPrintWriter.println("mDelegationMap=");
            indentingPrintWriter.increaseIndent();
            for (int i2 = 0; i2 < this.mDelegationMap.size(); i2++) {
                List<String> valueAt = this.mDelegationMap.valueAt(i2);
                indentingPrintWriter.println(this.mDelegationMap.keyAt(i2) + "[size=" + valueAt.size() + "]");
                indentingPrintWriter.increaseIndent();
                for (int i3 = 0; i3 < valueAt.size(); i3++) {
                    indentingPrintWriter.println(i3 + ": " + valueAt.get(i3));
                }
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
    }

    static String factoryResetFlagsToString(int i) {
        return DebugUtils.flagsToString(DevicePolicyData.class, "FACTORY_RESET_FLAG_", i);
    }
}
