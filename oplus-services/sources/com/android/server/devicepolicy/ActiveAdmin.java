package com.android.server.devicepolicy;

import android.app.admin.DeviceAdminInfo;
import android.app.admin.FactoryResetProtectionPolicy;
import android.app.admin.ManagedSubscriptionsPolicy;
import android.app.admin.PackagePolicy;
import android.app.admin.PasswordPolicy;
import android.app.admin.PreferentialNetworkServiceConfig;
import android.app.admin.WifiSsidPolicy;
import android.graphics.Color;
import android.net.wifi.WifiSsid;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.pm.UserRestrictionsUtils;
import com.android.server.utils.Slogf;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ActiveAdmin {
    private static final String ATTR_LAST_NETWORK_LOGGING_NOTIFICATION = "last-notification";
    private static final String ATTR_NUM_NETWORK_LOGGING_NOTIFICATIONS = "num-notifications";
    private static final String ATTR_PACKAGE_POLICY_MODE = "package-policy-type";
    private static final String ATTR_VALUE = "value";
    static final int DEF_KEYGUARD_FEATURES_DISABLED = 0;
    static final int DEF_MAXIMUM_FAILED_PASSWORDS_FOR_WIPE = 0;
    static final int DEF_MAXIMUM_NETWORK_LOGGING_NOTIFICATIONS_SHOWN = 2;
    static final long DEF_MAXIMUM_TIME_TO_UNLOCK = 0;
    static final int DEF_ORGANIZATION_COLOR = Color.parseColor("#00796B");
    static final long DEF_PASSWORD_EXPIRATION_DATE = 0;
    static final long DEF_PASSWORD_EXPIRATION_TIMEOUT = 0;
    static final int DEF_PASSWORD_HISTORY_LENGTH = 0;
    private static final String TAG_ACCOUNT_TYPE = "account-type";
    private static final String TAG_ADMIN_CAN_GRANT_SENSORS_PERMISSIONS = "admin-can-grant-sensors-permissions";
    private static final String TAG_ALWAYS_ON_VPN_LOCKDOWN = "vpn-lockdown";
    private static final String TAG_ALWAYS_ON_VPN_PACKAGE = "vpn-package";
    private static final String TAG_COMMON_CRITERIA_MODE = "common-criteria-mode";
    private static final String TAG_CREDENTIAL_MANAGER_POLICY = "credential-manager-policy";
    private static final String TAG_CROSS_PROFILE_CALENDAR_PACKAGES = "cross-profile-calendar-packages";
    private static final String TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL = "cross-profile-calendar-packages-null";
    private static final String TAG_CROSS_PROFILE_CALLER_ID_POLICY = "caller-id-policy";
    private static final String TAG_CROSS_PROFILE_CONTACTS_SEARCH_POLICY = "contacts-policy";
    private static final String TAG_CROSS_PROFILE_PACKAGES = "cross-profile-packages";
    private static final String TAG_CROSS_PROFILE_WIDGET_PROVIDERS = "cross-profile-widget-providers";
    private static final String TAG_DEFAULT_ENABLED_USER_RESTRICTIONS = "default-enabled-user-restrictions";
    private static final String TAG_DIALER_PACKAGE = "dialer_package";
    private static final String TAG_DISABLE_ACCOUNT_MANAGEMENT = "disable-account-management";
    private static final String TAG_DISABLE_BLUETOOTH_CONTACT_SHARING = "disable-bt-contacts-sharing";
    private static final String TAG_DISABLE_CALLER_ID = "disable-caller-id";
    private static final String TAG_DISABLE_CAMERA = "disable-camera";
    private static final String TAG_DISABLE_CONTACTS_SEARCH = "disable-contacts-search";
    private static final String TAG_DISABLE_KEYGUARD_FEATURES = "disable-keyguard-features";
    private static final String TAG_DISABLE_SCREEN_CAPTURE = "disable-screen-capture";
    private static final String TAG_ENCRYPTION_REQUESTED = "encryption-requested";
    private static final String TAG_END_USER_SESSION_MESSAGE = "end_user_session_message";
    private static final String TAG_ENROLLMENT_SPECIFIC_ID = "enrollment-specific-id";
    private static final String TAG_FACTORY_RESET_PROTECTION_POLICY = "factory_reset_protection_policy";
    private static final String TAG_FORCE_EPHEMERAL_USERS = "force_ephemeral_users";
    private static final String TAG_GLOBAL_PROXY_EXCLUSION_LIST = "global-proxy-exclusion-list";
    private static final String TAG_GLOBAL_PROXY_SPEC = "global-proxy-spec";
    private static final String TAG_IS_LOGOUT_ENABLED = "is_logout_enabled";
    private static final String TAG_IS_NETWORK_LOGGING_ENABLED = "is_network_logging_enabled";
    private static final String TAG_KEEP_UNINSTALLED_PACKAGES = "keep-uninstalled-packages";
    private static final String TAG_LONG_SUPPORT_MESSAGE = "long-support-message";
    private static final String TAG_MANAGED_SUBSCRIPTIONS_POLICY = "managed_subscriptions_policy";
    private static final String TAG_MANAGE_TRUST_AGENT_FEATURES = "manage-trust-agent-features";
    private static final String TAG_MAX_FAILED_PASSWORD_WIPE = "max-failed-password-wipe";
    private static final String TAG_MAX_TIME_TO_UNLOCK = "max-time-to-unlock";
    private static final String TAG_METERED_DATA_DISABLED_PACKAGES = "metered_data_disabled_packages";
    private static final String TAG_MIN_PASSWORD_LENGTH = "min-password-length";
    private static final String TAG_MIN_PASSWORD_LETTERS = "min-password-letters";
    private static final String TAG_MIN_PASSWORD_LOWERCASE = "min-password-lowercase";
    private static final String TAG_MIN_PASSWORD_NONLETTER = "min-password-nonletter";
    private static final String TAG_MIN_PASSWORD_NUMERIC = "min-password-numeric";
    private static final String TAG_MIN_PASSWORD_SYMBOLS = "min-password-symbols";
    private static final String TAG_MIN_PASSWORD_UPPERCASE = "min-password-uppercase";
    private static final String TAG_MTE_POLICY = "mte-policy";
    private static final String TAG_NEARBY_APP_STREAMING_POLICY = "nearby-app-streaming-policy";
    private static final String TAG_NEARBY_NOTIFICATION_STREAMING_POLICY = "nearby-notification-streaming-policy";
    private static final String TAG_ORGANIZATION_COLOR = "organization-color";
    private static final String TAG_ORGANIZATION_ID = "organization-id";
    private static final String TAG_ORGANIZATION_NAME = "organization-name";
    private static final String TAG_PACKAGE_LIST_ITEM = "item";
    private static final String TAG_PACKAGE_POLICY_PACKAGE_NAMES = "package-policy-packages";
    private static final String TAG_PARENT_ADMIN = "parent-admin";
    private static final String TAG_PASSWORD_COMPLEXITY = "password-complexity";
    private static final String TAG_PASSWORD_EXPIRATION_DATE = "password-expiration-date";
    private static final String TAG_PASSWORD_EXPIRATION_TIMEOUT = "password-expiration-timeout";
    private static final String TAG_PASSWORD_HISTORY_LENGTH = "password-history-length";
    private static final String TAG_PASSWORD_QUALITY = "password-quality";
    private static final String TAG_PERMITTED_ACCESSIBILITY_SERVICES = "permitted-accessiblity-services";
    private static final String TAG_PERMITTED_IMES = "permitted-imes";
    private static final String TAG_PERMITTED_NOTIFICATION_LISTENERS = "permitted-notification-listeners";
    private static final String TAG_POLICIES = "policies";
    private static final String TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIG = "preferential_network_service_config";
    private static final String TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS = "preferential_network_service_configs";
    private static final String TAG_PREFERENTIAL_NETWORK_SERVICE_ENABLED = "preferential-network-service-enabled";
    private static final String TAG_PROFILE_MAXIMUM_TIME_OFF = "profile-max-time-off";
    private static final String TAG_PROFILE_OFF_DEADLINE = "profile-off-deadline";
    private static final String TAG_PROTECTED_PACKAGES = "protected_packages";
    private static final String TAG_PROVIDER = "provider";
    private static final String TAG_REQUIRE_AUTO_TIME = "require_auto_time";
    private static final String TAG_RESTRICTION = "restriction";
    private static final String TAG_SHORT_SUPPORT_MESSAGE = "short-support-message";
    private static final String TAG_SMS_PACKAGE = "sms_package";
    private static final String TAG_SPECIFIES_GLOBAL_PROXY = "specifies-global-proxy";
    private static final String TAG_SSID = "ssid";
    private static final String TAG_SSID_ALLOWLIST = "ssid-allowlist";
    private static final String TAG_SSID_DENYLIST = "ssid-denylist";
    private static final String TAG_START_USER_SESSION_MESSAGE = "start_user_session_message";
    private static final String TAG_STRONG_AUTH_UNLOCK_TIMEOUT = "strong-auth-unlock-timeout";
    private static final String TAG_SUSPENDED_PACKAGES = "suspended-packages";
    private static final String TAG_SUSPEND_PERSONAL_APPS = "suspend-personal-apps";
    private static final String TAG_TEST_ONLY_ADMIN = "test-only-admin";
    private static final String TAG_TRUST_AGENT_COMPONENT = "component";
    private static final String TAG_TRUST_AGENT_COMPONENT_OPTIONS = "trust-agent-component-options";
    private static final String TAG_USB_DATA_SIGNALING = "usb-data-signaling";
    private static final String TAG_USER_RESTRICTIONS = "user-restrictions";
    private static final String TAG_WIFI_MIN_SECURITY = "wifi-min-security";
    private static final boolean USB_DATA_SIGNALING_ENABLED_DEFAULT = true;
    final Set<String> accountTypesWithManagementDisabled;
    List<String> crossProfileWidgetProviders;
    final Set<String> defaultEnabledRestrictionsAlreadySet;
    boolean disableBluetoothContactSharing;
    boolean disableCallerId;
    boolean disableCamera;
    boolean disableContactsSearch;
    boolean disableScreenCapture;
    int disabledKeyguardFeatures;
    boolean encryptionRequested;
    String endUserSessionMessage;
    boolean forceEphemeralUsers;
    String globalProxyExclusionList;
    String globalProxySpec;
    DeviceAdminInfo info;
    boolean isLogoutEnabled;
    boolean isNetworkLoggingEnabled;
    final boolean isParent;
    public final boolean isPermissionBased;
    List<String> keepUninstalledPackages;
    long lastNetworkLoggingNotificationTimeMs;
    CharSequence longSupportMessage;
    public boolean mAdminCanGrantSensorsPermissions;
    public boolean mAlwaysOnVpnLockdown;
    public String mAlwaysOnVpnPackage;
    boolean mCommonCriteriaMode;
    PackagePolicy mCredentialManagerPolicy;
    List<String> mCrossProfileCalendarPackages;
    List<String> mCrossProfilePackages;
    String mDialerPackage;
    public String mEnrollmentSpecificId;
    FactoryResetProtectionPolicy mFactoryResetProtectionPolicy;
    PackagePolicy mManagedProfileCallerIdAccess;
    PackagePolicy mManagedProfileContactsAccess;
    ManagedSubscriptionsPolicy mManagedSubscriptionsPolicy;
    int mNearbyAppStreamingPolicy;
    int mNearbyNotificationStreamingPolicy;
    public String mOrganizationId;
    int mPasswordComplexity;
    PasswordPolicy mPasswordPolicy;
    public List<PreferentialNetworkServiceConfig> mPreferentialNetworkServiceConfigs;
    long mProfileMaximumTimeOffMillis;
    long mProfileOffDeadline;
    String mSmsPackage;
    boolean mSuspendPersonalApps;
    boolean mUsbDataSignalingEnabled;
    int mWifiMinimumSecurityLevel;
    WifiSsidPolicy mWifiSsidPolicy;
    int maximumFailedPasswordsForWipe;
    long maximumTimeToUnlock;
    List<String> meteredDisabledPackages;
    int mtePolicy;
    int numNetworkLoggingNotifications;
    int organizationColor;
    String organizationName;
    ActiveAdmin parentAdmin;
    long passwordExpirationDate;
    long passwordExpirationTimeout;
    int passwordHistoryLength;
    List<String> permittedAccessiblityServices;
    List<String> permittedInputMethods;
    List<String> permittedNotificationListeners;
    List<String> protectedPackages;
    boolean requireAutoTime;
    CharSequence shortSupportMessage;
    boolean specifiesGlobalProxy;
    String startUserSessionMessage;
    long strongAuthUnlockTimeout;
    List<String> suspendedPackages;
    boolean testOnlyAdmin;
    ArrayMap<String, TrustAgentInfo> trustAgentInfos;
    private final int userId;
    Bundle userRestrictions;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class TrustAgentInfo {
        public PersistableBundle options;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TrustAgentInfo(PersistableBundle persistableBundle) {
            this.options = persistableBundle;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveAdmin(DeviceAdminInfo deviceAdminInfo, boolean z) {
        this.passwordHistoryLength = 0;
        this.mPasswordPolicy = new PasswordPolicy();
        this.mPasswordComplexity = 0;
        this.mNearbyNotificationStreamingPolicy = 3;
        this.mNearbyAppStreamingPolicy = 3;
        this.mFactoryResetProtectionPolicy = null;
        this.maximumTimeToUnlock = 0L;
        this.strongAuthUnlockTimeout = 0L;
        this.maximumFailedPasswordsForWipe = 0;
        this.passwordExpirationTimeout = 0L;
        this.passwordExpirationDate = 0L;
        this.disabledKeyguardFeatures = 0;
        this.encryptionRequested = false;
        this.testOnlyAdmin = false;
        this.disableCamera = false;
        this.disableCallerId = false;
        this.disableContactsSearch = false;
        this.disableBluetoothContactSharing = true;
        this.disableScreenCapture = false;
        this.requireAutoTime = false;
        this.forceEphemeralUsers = false;
        this.isNetworkLoggingEnabled = false;
        this.isLogoutEnabled = false;
        this.numNetworkLoggingNotifications = 0;
        this.lastNetworkLoggingNotificationTimeMs = 0L;
        this.mtePolicy = 0;
        this.accountTypesWithManagementDisabled = new ArraySet();
        this.specifiesGlobalProxy = false;
        this.globalProxySpec = null;
        this.globalProxyExclusionList = null;
        this.trustAgentInfos = new ArrayMap<>();
        this.defaultEnabledRestrictionsAlreadySet = new ArraySet();
        this.shortSupportMessage = null;
        this.longSupportMessage = null;
        this.organizationColor = DEF_ORGANIZATION_COLOR;
        this.organizationName = null;
        this.startUserSessionMessage = null;
        this.endUserSessionMessage = null;
        this.mCrossProfileCalendarPackages = Collections.emptyList();
        this.mCrossProfilePackages = Collections.emptyList();
        this.mSuspendPersonalApps = false;
        this.mProfileMaximumTimeOffMillis = 0L;
        this.mProfileOffDeadline = 0L;
        this.mManagedProfileCallerIdAccess = null;
        this.mManagedProfileContactsAccess = null;
        this.mCredentialManagerPolicy = null;
        this.mPreferentialNetworkServiceConfigs = List.of(PreferentialNetworkServiceConfig.DEFAULT);
        this.mUsbDataSignalingEnabled = true;
        this.mWifiMinimumSecurityLevel = 0;
        this.userId = -1;
        this.info = deviceAdminInfo;
        this.isParent = z;
        this.isPermissionBased = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveAdmin(int i, boolean z) {
        this.passwordHistoryLength = 0;
        this.mPasswordPolicy = new PasswordPolicy();
        this.mPasswordComplexity = 0;
        this.mNearbyNotificationStreamingPolicy = 3;
        this.mNearbyAppStreamingPolicy = 3;
        this.mFactoryResetProtectionPolicy = null;
        this.maximumTimeToUnlock = 0L;
        this.strongAuthUnlockTimeout = 0L;
        this.maximumFailedPasswordsForWipe = 0;
        this.passwordExpirationTimeout = 0L;
        this.passwordExpirationDate = 0L;
        this.disabledKeyguardFeatures = 0;
        this.encryptionRequested = false;
        this.testOnlyAdmin = false;
        this.disableCamera = false;
        this.disableCallerId = false;
        this.disableContactsSearch = false;
        this.disableBluetoothContactSharing = true;
        this.disableScreenCapture = false;
        this.requireAutoTime = false;
        this.forceEphemeralUsers = false;
        this.isNetworkLoggingEnabled = false;
        this.isLogoutEnabled = false;
        this.numNetworkLoggingNotifications = 0;
        this.lastNetworkLoggingNotificationTimeMs = 0L;
        this.mtePolicy = 0;
        this.accountTypesWithManagementDisabled = new ArraySet();
        this.specifiesGlobalProxy = false;
        this.globalProxySpec = null;
        this.globalProxyExclusionList = null;
        this.trustAgentInfos = new ArrayMap<>();
        this.defaultEnabledRestrictionsAlreadySet = new ArraySet();
        this.shortSupportMessage = null;
        this.longSupportMessage = null;
        this.organizationColor = DEF_ORGANIZATION_COLOR;
        this.organizationName = null;
        this.startUserSessionMessage = null;
        this.endUserSessionMessage = null;
        this.mCrossProfileCalendarPackages = Collections.emptyList();
        this.mCrossProfilePackages = Collections.emptyList();
        this.mSuspendPersonalApps = false;
        this.mProfileMaximumTimeOffMillis = 0L;
        this.mProfileOffDeadline = 0L;
        this.mManagedProfileCallerIdAccess = null;
        this.mManagedProfileContactsAccess = null;
        this.mCredentialManagerPolicy = null;
        this.mPreferentialNetworkServiceConfigs = List.of(PreferentialNetworkServiceConfig.DEFAULT);
        this.mUsbDataSignalingEnabled = true;
        this.mWifiMinimumSecurityLevel = 0;
        if (!z) {
            throw new IllegalArgumentException("Can only pass true for permissionBased admin");
        }
        this.userId = i;
        this.isPermissionBased = z;
        this.isParent = false;
        this.info = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveAdmin getParentActiveAdmin() {
        Preconditions.checkState(!this.isParent);
        if (this.parentAdmin == null) {
            this.parentAdmin = new ActiveAdmin(this.info, true);
        }
        return this.parentAdmin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasParentActiveAdmin() {
        return this.parentAdmin != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getUid() {
        if (this.isPermissionBased) {
            return -1;
        }
        return this.info.getActivityInfo().applicationInfo.uid;
    }

    public UserHandle getUserHandle() {
        if (this.isPermissionBased) {
            return UserHandle.of(this.userId);
        }
        return UserHandle.of(UserHandle.getUserId(this.info.getActivityInfo().applicationInfo.uid));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeToXml(TypedXmlSerializer typedXmlSerializer) throws IllegalArgumentException, IllegalStateException, IOException {
        if (this.info != null) {
            typedXmlSerializer.startTag((String) null, TAG_POLICIES);
            this.info.writePoliciesToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((String) null, TAG_POLICIES);
        }
        int i = this.mPasswordPolicy.quality;
        if (i != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_PASSWORD_QUALITY, i);
            int i2 = this.mPasswordPolicy.length;
            if (i2 != 0) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_MIN_PASSWORD_LENGTH, i2);
            }
            int i3 = this.mPasswordPolicy.upperCase;
            if (i3 != 0) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_MIN_PASSWORD_UPPERCASE, i3);
            }
            int i4 = this.mPasswordPolicy.lowerCase;
            if (i4 != 0) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_MIN_PASSWORD_LOWERCASE, i4);
            }
            int i5 = this.mPasswordPolicy.letters;
            if (i5 != 1) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_MIN_PASSWORD_LETTERS, i5);
            }
            int i6 = this.mPasswordPolicy.numeric;
            if (i6 != 1) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_MIN_PASSWORD_NUMERIC, i6);
            }
            int i7 = this.mPasswordPolicy.symbols;
            if (i7 != 1) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_MIN_PASSWORD_SYMBOLS, i7);
            }
            int i8 = this.mPasswordPolicy.nonLetter;
            if (i8 > 0) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_MIN_PASSWORD_NONLETTER, i8);
            }
        }
        int i9 = this.passwordHistoryLength;
        if (i9 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_PASSWORD_HISTORY_LENGTH, i9);
        }
        long j = this.maximumTimeToUnlock;
        if (j != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_MAX_TIME_TO_UNLOCK, j);
        }
        long j2 = this.strongAuthUnlockTimeout;
        if (j2 != 259200000) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_STRONG_AUTH_UNLOCK_TIMEOUT, j2);
        }
        int i10 = this.maximumFailedPasswordsForWipe;
        if (i10 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_MAX_FAILED_PASSWORD_WIPE, i10);
        }
        boolean z = this.specifiesGlobalProxy;
        if (z) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_SPECIFIES_GLOBAL_PROXY, z);
            String str = this.globalProxySpec;
            if (str != null) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_GLOBAL_PROXY_SPEC, str);
            }
            String str2 = this.globalProxyExclusionList;
            if (str2 != null) {
                writeAttributeValueToXml(typedXmlSerializer, TAG_GLOBAL_PROXY_EXCLUSION_LIST, str2);
            }
        }
        long j3 = this.passwordExpirationTimeout;
        if (j3 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_PASSWORD_EXPIRATION_TIMEOUT, j3);
        }
        long j4 = this.passwordExpirationDate;
        if (j4 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_PASSWORD_EXPIRATION_DATE, j4);
        }
        boolean z2 = this.encryptionRequested;
        if (z2) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_ENCRYPTION_REQUESTED, z2);
        }
        boolean z3 = this.testOnlyAdmin;
        if (z3) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_TEST_ONLY_ADMIN, z3);
        }
        boolean z4 = this.disableCamera;
        if (z4) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_DISABLE_CAMERA, z4);
        }
        boolean z5 = this.disableCallerId;
        if (z5) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_DISABLE_CALLER_ID, z5);
        }
        boolean z6 = this.disableContactsSearch;
        if (z6) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_DISABLE_CONTACTS_SEARCH, z6);
        }
        boolean z7 = this.disableBluetoothContactSharing;
        if (!z7) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_DISABLE_BLUETOOTH_CONTACT_SHARING, z7);
        }
        boolean z8 = this.disableScreenCapture;
        if (z8) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_DISABLE_SCREEN_CAPTURE, z8);
        }
        boolean z9 = this.requireAutoTime;
        if (z9) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_REQUIRE_AUTO_TIME, z9);
        }
        boolean z10 = this.forceEphemeralUsers;
        if (z10) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_FORCE_EPHEMERAL_USERS, z10);
        }
        if (this.isNetworkLoggingEnabled) {
            typedXmlSerializer.startTag((String) null, TAG_IS_NETWORK_LOGGING_ENABLED);
            typedXmlSerializer.attributeBoolean((String) null, ATTR_VALUE, this.isNetworkLoggingEnabled);
            typedXmlSerializer.attributeInt((String) null, ATTR_NUM_NETWORK_LOGGING_NOTIFICATIONS, this.numNetworkLoggingNotifications);
            typedXmlSerializer.attributeLong((String) null, ATTR_LAST_NETWORK_LOGGING_NOTIFICATION, this.lastNetworkLoggingNotificationTimeMs);
            typedXmlSerializer.endTag((String) null, TAG_IS_NETWORK_LOGGING_ENABLED);
        }
        int i11 = this.disabledKeyguardFeatures;
        if (i11 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_DISABLE_KEYGUARD_FEATURES, i11);
        }
        if (!this.accountTypesWithManagementDisabled.isEmpty()) {
            writeAttributeValuesToXml(typedXmlSerializer, TAG_DISABLE_ACCOUNT_MANAGEMENT, TAG_ACCOUNT_TYPE, this.accountTypesWithManagementDisabled);
        }
        if (!this.trustAgentInfos.isEmpty()) {
            Set<Map.Entry<String, TrustAgentInfo>> entrySet = this.trustAgentInfos.entrySet();
            typedXmlSerializer.startTag((String) null, TAG_MANAGE_TRUST_AGENT_FEATURES);
            for (Map.Entry<String, TrustAgentInfo> entry : entrySet) {
                TrustAgentInfo value = entry.getValue();
                typedXmlSerializer.startTag((String) null, TAG_TRUST_AGENT_COMPONENT);
                typedXmlSerializer.attribute((String) null, ATTR_VALUE, entry.getKey());
                if (value.options != null) {
                    typedXmlSerializer.startTag((String) null, TAG_TRUST_AGENT_COMPONENT_OPTIONS);
                    try {
                        value.options.saveToXml(typedXmlSerializer);
                    } catch (XmlPullParserException e) {
                        Slogf.e("DevicePolicyManager", e, "Failed to save TrustAgent options", new Object[0]);
                    }
                    typedXmlSerializer.endTag((String) null, TAG_TRUST_AGENT_COMPONENT_OPTIONS);
                }
                typedXmlSerializer.endTag((String) null, TAG_TRUST_AGENT_COMPONENT);
            }
            typedXmlSerializer.endTag((String) null, TAG_MANAGE_TRUST_AGENT_FEATURES);
        }
        List<String> list = this.crossProfileWidgetProviders;
        if (list != null && !list.isEmpty()) {
            writeAttributeValuesToXml(typedXmlSerializer, TAG_CROSS_PROFILE_WIDGET_PROVIDERS, TAG_PROVIDER, this.crossProfileWidgetProviders);
        }
        writePackageListToXml(typedXmlSerializer, TAG_PERMITTED_ACCESSIBILITY_SERVICES, this.permittedAccessiblityServices);
        writePackageListToXml(typedXmlSerializer, TAG_PERMITTED_IMES, this.permittedInputMethods);
        writePackageListToXml(typedXmlSerializer, TAG_PERMITTED_NOTIFICATION_LISTENERS, this.permittedNotificationListeners);
        writePackageListToXml(typedXmlSerializer, TAG_KEEP_UNINSTALLED_PACKAGES, this.keepUninstalledPackages);
        writePackageListToXml(typedXmlSerializer, TAG_METERED_DATA_DISABLED_PACKAGES, this.meteredDisabledPackages);
        writePackageListToXml(typedXmlSerializer, TAG_PROTECTED_PACKAGES, this.protectedPackages);
        writePackageListToXml(typedXmlSerializer, TAG_SUSPENDED_PACKAGES, this.suspendedPackages);
        if (hasUserRestrictions()) {
            UserRestrictionsUtils.writeRestrictions(typedXmlSerializer, this.userRestrictions, TAG_USER_RESTRICTIONS);
        }
        if (!this.defaultEnabledRestrictionsAlreadySet.isEmpty()) {
            writeAttributeValuesToXml(typedXmlSerializer, TAG_DEFAULT_ENABLED_USER_RESTRICTIONS, TAG_RESTRICTION, this.defaultEnabledRestrictionsAlreadySet);
        }
        if (!TextUtils.isEmpty(this.shortSupportMessage)) {
            writeTextToXml(typedXmlSerializer, TAG_SHORT_SUPPORT_MESSAGE, this.shortSupportMessage.toString());
        }
        if (!TextUtils.isEmpty(this.longSupportMessage)) {
            writeTextToXml(typedXmlSerializer, TAG_LONG_SUPPORT_MESSAGE, this.longSupportMessage.toString());
        }
        if (this.parentAdmin != null) {
            typedXmlSerializer.startTag((String) null, TAG_PARENT_ADMIN);
            this.parentAdmin.writeToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((String) null, TAG_PARENT_ADMIN);
        }
        int i12 = this.organizationColor;
        if (i12 != DEF_ORGANIZATION_COLOR) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_ORGANIZATION_COLOR, i12);
        }
        String str3 = this.organizationName;
        if (str3 != null) {
            writeTextToXml(typedXmlSerializer, TAG_ORGANIZATION_NAME, str3);
        }
        boolean z11 = this.isLogoutEnabled;
        if (z11) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_IS_LOGOUT_ENABLED, z11);
        }
        String str4 = this.startUserSessionMessage;
        if (str4 != null) {
            writeTextToXml(typedXmlSerializer, TAG_START_USER_SESSION_MESSAGE, str4);
        }
        String str5 = this.endUserSessionMessage;
        if (str5 != null) {
            writeTextToXml(typedXmlSerializer, TAG_END_USER_SESSION_MESSAGE, str5);
        }
        List<String> list2 = this.mCrossProfileCalendarPackages;
        if (list2 == null) {
            typedXmlSerializer.startTag((String) null, TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL);
            typedXmlSerializer.endTag((String) null, TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL);
        } else {
            writePackageListToXml(typedXmlSerializer, TAG_CROSS_PROFILE_CALENDAR_PACKAGES, list2);
        }
        writePackageListToXml(typedXmlSerializer, TAG_CROSS_PROFILE_PACKAGES, this.mCrossProfilePackages);
        if (this.mFactoryResetProtectionPolicy != null) {
            typedXmlSerializer.startTag((String) null, TAG_FACTORY_RESET_PROTECTION_POLICY);
            this.mFactoryResetProtectionPolicy.writeToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((String) null, TAG_FACTORY_RESET_PROTECTION_POLICY);
        }
        boolean z12 = this.mSuspendPersonalApps;
        if (z12) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_SUSPEND_PERSONAL_APPS, z12);
        }
        long j5 = this.mProfileMaximumTimeOffMillis;
        if (j5 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_PROFILE_MAXIMUM_TIME_OFF, j5);
        }
        if (this.mProfileMaximumTimeOffMillis != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_PROFILE_OFF_DEADLINE, this.mProfileOffDeadline);
        }
        if (!TextUtils.isEmpty(this.mAlwaysOnVpnPackage)) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_ALWAYS_ON_VPN_PACKAGE, this.mAlwaysOnVpnPackage);
        }
        boolean z13 = this.mAlwaysOnVpnLockdown;
        if (z13) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_ALWAYS_ON_VPN_LOCKDOWN, z13);
        }
        boolean z14 = this.mCommonCriteriaMode;
        if (z14) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_COMMON_CRITERIA_MODE, z14);
        }
        int i13 = this.mPasswordComplexity;
        if (i13 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_PASSWORD_COMPLEXITY, i13);
        }
        int i14 = this.mNearbyNotificationStreamingPolicy;
        if (i14 != 3) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_NEARBY_NOTIFICATION_STREAMING_POLICY, i14);
        }
        int i15 = this.mNearbyAppStreamingPolicy;
        if (i15 != 3) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_NEARBY_APP_STREAMING_POLICY, i15);
        }
        if (!TextUtils.isEmpty(this.mOrganizationId)) {
            writeTextToXml(typedXmlSerializer, TAG_ORGANIZATION_ID, this.mOrganizationId);
        }
        if (!TextUtils.isEmpty(this.mEnrollmentSpecificId)) {
            writeTextToXml(typedXmlSerializer, TAG_ENROLLMENT_SPECIFIC_ID, this.mEnrollmentSpecificId);
        }
        writeAttributeValueToXml(typedXmlSerializer, TAG_ADMIN_CAN_GRANT_SENSORS_PERMISSIONS, this.mAdminCanGrantSensorsPermissions);
        boolean z15 = this.mUsbDataSignalingEnabled;
        if (!z15) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_USB_DATA_SIGNALING, z15);
        }
        int i16 = this.mWifiMinimumSecurityLevel;
        if (i16 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_WIFI_MIN_SECURITY, i16);
        }
        WifiSsidPolicy wifiSsidPolicy = this.mWifiSsidPolicy;
        if (wifiSsidPolicy != null) {
            List<String> ssidsToStrings = ssidsToStrings(wifiSsidPolicy.getSsids());
            if (this.mWifiSsidPolicy.getPolicyType() == 0) {
                writeAttributeValuesToXml(typedXmlSerializer, TAG_SSID_ALLOWLIST, TAG_SSID, ssidsToStrings);
            } else if (this.mWifiSsidPolicy.getPolicyType() == 1) {
                writeAttributeValuesToXml(typedXmlSerializer, TAG_SSID_DENYLIST, TAG_SSID, ssidsToStrings);
            }
        }
        if (!this.mPreferentialNetworkServiceConfigs.isEmpty()) {
            typedXmlSerializer.startTag((String) null, TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS);
            Iterator<PreferentialNetworkServiceConfig> it = this.mPreferentialNetworkServiceConfigs.iterator();
            while (it.hasNext()) {
                it.next().writeToXml(typedXmlSerializer);
            }
            typedXmlSerializer.endTag((String) null, TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS);
        }
        int i17 = this.mtePolicy;
        if (i17 != 0) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_MTE_POLICY, i17);
        }
        writePackagePolicy(typedXmlSerializer, TAG_CROSS_PROFILE_CALLER_ID_POLICY, this.mManagedProfileCallerIdAccess);
        writePackagePolicy(typedXmlSerializer, TAG_CROSS_PROFILE_CONTACTS_SEARCH_POLICY, this.mManagedProfileContactsAccess);
        writePackagePolicy(typedXmlSerializer, TAG_CREDENTIAL_MANAGER_POLICY, this.mCredentialManagerPolicy);
        if (this.mManagedSubscriptionsPolicy != null) {
            typedXmlSerializer.startTag((String) null, TAG_MANAGED_SUBSCRIPTIONS_POLICY);
            this.mManagedSubscriptionsPolicy.saveToXml(typedXmlSerializer);
            typedXmlSerializer.endTag((String) null, TAG_MANAGED_SUBSCRIPTIONS_POLICY);
        }
        if (!TextUtils.isEmpty(this.mDialerPackage)) {
            writeAttributeValueToXml(typedXmlSerializer, TAG_DIALER_PACKAGE, this.mDialerPackage);
        }
        if (TextUtils.isEmpty(this.mSmsPackage)) {
            return;
        }
        writeAttributeValueToXml(typedXmlSerializer, TAG_SMS_PACKAGE, this.mSmsPackage);
    }

    private void writePackagePolicy(TypedXmlSerializer typedXmlSerializer, String str, PackagePolicy packagePolicy) throws IOException {
        if (packagePolicy == null) {
            return;
        }
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.attributeInt((String) null, ATTR_PACKAGE_POLICY_MODE, packagePolicy.getPolicyType());
        writePackageListToXml(typedXmlSerializer, TAG_PACKAGE_POLICY_PACKAGE_NAMES, new ArrayList(packagePolicy.getPackageNames()));
        typedXmlSerializer.endTag((String) null, str);
    }

    private List<String> ssidsToStrings(Set<WifiSsid> set) {
        return (List) set.stream().map(new Function() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$ssidsToStrings$0;
                lambda$ssidsToStrings$0 = ActiveAdmin.lambda$ssidsToStrings$0((WifiSsid) obj);
                return lambda$ssidsToStrings$0;
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$ssidsToStrings$0(WifiSsid wifiSsid) {
        return new String(wifiSsid.getBytes(), StandardCharsets.UTF_8);
    }

    void writeTextToXml(TypedXmlSerializer typedXmlSerializer, String str, String str2) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.text(str2);
        typedXmlSerializer.endTag((String) null, str);
    }

    void writePackageListToXml(TypedXmlSerializer typedXmlSerializer, String str, List<String> list) throws IllegalArgumentException, IllegalStateException, IOException {
        if (list == null) {
            return;
        }
        writeAttributeValuesToXml(typedXmlSerializer, str, TAG_PACKAGE_LIST_ITEM, list);
    }

    void writeAttributeValueToXml(TypedXmlSerializer typedXmlSerializer, String str, String str2) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.attribute((String) null, ATTR_VALUE, str2);
        typedXmlSerializer.endTag((String) null, str);
    }

    void writeAttributeValueToXml(TypedXmlSerializer typedXmlSerializer, String str, int i) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.attributeInt((String) null, ATTR_VALUE, i);
        typedXmlSerializer.endTag((String) null, str);
    }

    void writeAttributeValueToXml(TypedXmlSerializer typedXmlSerializer, String str, long j) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.attributeLong((String) null, ATTR_VALUE, j);
        typedXmlSerializer.endTag((String) null, str);
    }

    void writeAttributeValueToXml(TypedXmlSerializer typedXmlSerializer, String str, boolean z) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_VALUE, z);
        typedXmlSerializer.endTag((String) null, str);
    }

    void writeAttributeValuesToXml(TypedXmlSerializer typedXmlSerializer, String str, String str2, Collection<String> collection) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        for (String str3 : collection) {
            typedXmlSerializer.startTag((String) null, str2);
            typedXmlSerializer.attribute((String) null, ATTR_VALUE, str3);
            typedXmlSerializer.endTag((String) null, str2);
        }
        typedXmlSerializer.endTag((String) null, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void readFromXml(TypedXmlPullParser typedXmlPullParser, boolean z) throws XmlPullParserException, IOException {
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && typedXmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4) {
                String name = typedXmlPullParser.getName();
                if (TAG_POLICIES.equals(name)) {
                    if (z) {
                        Slogf.d("DevicePolicyManager", "Overriding device admin policies from XML.");
                        this.info.readPoliciesFromXml(typedXmlPullParser);
                    }
                } else if (TAG_PASSWORD_QUALITY.equals(name)) {
                    this.mPasswordPolicy.quality = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MIN_PASSWORD_LENGTH.equals(name)) {
                    this.mPasswordPolicy.length = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_PASSWORD_HISTORY_LENGTH.equals(name)) {
                    this.passwordHistoryLength = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MIN_PASSWORD_UPPERCASE.equals(name)) {
                    this.mPasswordPolicy.upperCase = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MIN_PASSWORD_LOWERCASE.equals(name)) {
                    this.mPasswordPolicy.lowerCase = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MIN_PASSWORD_LETTERS.equals(name)) {
                    this.mPasswordPolicy.letters = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MIN_PASSWORD_NUMERIC.equals(name)) {
                    this.mPasswordPolicy.numeric = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MIN_PASSWORD_SYMBOLS.equals(name)) {
                    this.mPasswordPolicy.symbols = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MIN_PASSWORD_NONLETTER.equals(name)) {
                    this.mPasswordPolicy.nonLetter = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_MAX_TIME_TO_UNLOCK.equals(name)) {
                    this.maximumTimeToUnlock = typedXmlPullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_STRONG_AUTH_UNLOCK_TIMEOUT.equals(name)) {
                    this.strongAuthUnlockTimeout = typedXmlPullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_MAX_FAILED_PASSWORD_WIPE.equals(name)) {
                    this.maximumFailedPasswordsForWipe = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_SPECIFIES_GLOBAL_PROXY.equals(name)) {
                    this.specifiesGlobalProxy = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_GLOBAL_PROXY_SPEC.equals(name)) {
                    this.globalProxySpec = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE);
                } else if (TAG_GLOBAL_PROXY_EXCLUSION_LIST.equals(name)) {
                    this.globalProxyExclusionList = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE);
                } else if (TAG_PASSWORD_EXPIRATION_TIMEOUT.equals(name)) {
                    this.passwordExpirationTimeout = typedXmlPullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_PASSWORD_EXPIRATION_DATE.equals(name)) {
                    this.passwordExpirationDate = typedXmlPullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_ENCRYPTION_REQUESTED.equals(name)) {
                    this.encryptionRequested = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_TEST_ONLY_ADMIN.equals(name)) {
                    this.testOnlyAdmin = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_DISABLE_CAMERA.equals(name)) {
                    this.disableCamera = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_DISABLE_CALLER_ID.equals(name)) {
                    this.disableCallerId = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_DISABLE_CONTACTS_SEARCH.equals(name)) {
                    this.disableContactsSearch = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_DISABLE_BLUETOOTH_CONTACT_SHARING.equals(name)) {
                    this.disableBluetoothContactSharing = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_DISABLE_SCREEN_CAPTURE.equals(name)) {
                    this.disableScreenCapture = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_REQUIRE_AUTO_TIME.equals(name)) {
                    this.requireAutoTime = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_FORCE_EPHEMERAL_USERS.equals(name)) {
                    this.forceEphemeralUsers = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_IS_NETWORK_LOGGING_ENABLED.equals(name)) {
                    this.isNetworkLoggingEnabled = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                    this.lastNetworkLoggingNotificationTimeMs = typedXmlPullParser.getAttributeLong((String) null, ATTR_LAST_NETWORK_LOGGING_NOTIFICATION);
                    this.numNetworkLoggingNotifications = typedXmlPullParser.getAttributeInt((String) null, ATTR_NUM_NETWORK_LOGGING_NOTIFICATIONS);
                } else if (TAG_DISABLE_KEYGUARD_FEATURES.equals(name)) {
                    this.disabledKeyguardFeatures = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_DISABLE_ACCOUNT_MANAGEMENT.equals(name)) {
                    readAttributeValues(typedXmlPullParser, TAG_ACCOUNT_TYPE, this.accountTypesWithManagementDisabled);
                } else if (TAG_MANAGE_TRUST_AGENT_FEATURES.equals(name)) {
                    this.trustAgentInfos = getAllTrustAgentInfos(typedXmlPullParser, name);
                } else if (TAG_CROSS_PROFILE_WIDGET_PROVIDERS.equals(name)) {
                    ArrayList arrayList = new ArrayList();
                    this.crossProfileWidgetProviders = arrayList;
                    readAttributeValues(typedXmlPullParser, TAG_PROVIDER, arrayList);
                } else if (TAG_PERMITTED_ACCESSIBILITY_SERVICES.equals(name)) {
                    this.permittedAccessiblityServices = readPackageList(typedXmlPullParser, name);
                } else if (TAG_PERMITTED_IMES.equals(name)) {
                    this.permittedInputMethods = readPackageList(typedXmlPullParser, name);
                } else if (TAG_PERMITTED_NOTIFICATION_LISTENERS.equals(name)) {
                    this.permittedNotificationListeners = readPackageList(typedXmlPullParser, name);
                } else if (TAG_KEEP_UNINSTALLED_PACKAGES.equals(name)) {
                    this.keepUninstalledPackages = readPackageList(typedXmlPullParser, name);
                } else if (TAG_METERED_DATA_DISABLED_PACKAGES.equals(name)) {
                    this.meteredDisabledPackages = readPackageList(typedXmlPullParser, name);
                } else if (TAG_PROTECTED_PACKAGES.equals(name)) {
                    this.protectedPackages = readPackageList(typedXmlPullParser, name);
                } else if (TAG_SUSPENDED_PACKAGES.equals(name)) {
                    this.suspendedPackages = readPackageList(typedXmlPullParser, name);
                } else if (TAG_USER_RESTRICTIONS.equals(name)) {
                    this.userRestrictions = UserRestrictionsUtils.readRestrictions(typedXmlPullParser);
                } else if (TAG_DEFAULT_ENABLED_USER_RESTRICTIONS.equals(name)) {
                    readAttributeValues(typedXmlPullParser, TAG_RESTRICTION, this.defaultEnabledRestrictionsAlreadySet);
                } else if (TAG_SHORT_SUPPORT_MESSAGE.equals(name)) {
                    if (typedXmlPullParser.next() == 4) {
                        this.shortSupportMessage = typedXmlPullParser.getText();
                    } else {
                        Slogf.w("DevicePolicyManager", "Missing text when loading short support message");
                    }
                } else if (TAG_LONG_SUPPORT_MESSAGE.equals(name)) {
                    if (typedXmlPullParser.next() == 4) {
                        this.longSupportMessage = typedXmlPullParser.getText();
                    } else {
                        Slogf.w("DevicePolicyManager", "Missing text when loading long support message");
                    }
                } else if (TAG_PARENT_ADMIN.equals(name)) {
                    Preconditions.checkState(!this.isParent);
                    ActiveAdmin activeAdmin = new ActiveAdmin(this.info, true);
                    this.parentAdmin = activeAdmin;
                    activeAdmin.readFromXml(typedXmlPullParser, z);
                } else if (TAG_ORGANIZATION_COLOR.equals(name)) {
                    this.organizationColor = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_ORGANIZATION_NAME.equals(name)) {
                    if (typedXmlPullParser.next() == 4) {
                        this.organizationName = typedXmlPullParser.getText();
                    } else {
                        Slogf.w("DevicePolicyManager", "Missing text when loading organization name");
                    }
                } else if (TAG_IS_LOGOUT_ENABLED.equals(name)) {
                    this.isLogoutEnabled = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_START_USER_SESSION_MESSAGE.equals(name)) {
                    if (typedXmlPullParser.next() == 4) {
                        this.startUserSessionMessage = typedXmlPullParser.getText();
                    } else {
                        Slogf.w("DevicePolicyManager", "Missing text when loading start session message");
                    }
                } else if (TAG_END_USER_SESSION_MESSAGE.equals(name)) {
                    if (typedXmlPullParser.next() == 4) {
                        this.endUserSessionMessage = typedXmlPullParser.getText();
                    } else {
                        Slogf.w("DevicePolicyManager", "Missing text when loading end session message");
                    }
                } else if (TAG_CROSS_PROFILE_CALENDAR_PACKAGES.equals(name)) {
                    this.mCrossProfileCalendarPackages = readPackageList(typedXmlPullParser, name);
                } else if (TAG_CROSS_PROFILE_CALENDAR_PACKAGES_NULL.equals(name)) {
                    this.mCrossProfileCalendarPackages = null;
                } else if (TAG_CROSS_PROFILE_PACKAGES.equals(name)) {
                    this.mCrossProfilePackages = readPackageList(typedXmlPullParser, name);
                } else if (TAG_FACTORY_RESET_PROTECTION_POLICY.equals(name)) {
                    this.mFactoryResetProtectionPolicy = FactoryResetProtectionPolicy.readFromXml(typedXmlPullParser);
                } else if (TAG_SUSPEND_PERSONAL_APPS.equals(name)) {
                    this.mSuspendPersonalApps = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_PROFILE_MAXIMUM_TIME_OFF.equals(name)) {
                    this.mProfileMaximumTimeOffMillis = typedXmlPullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_PROFILE_OFF_DEADLINE.equals(name)) {
                    this.mProfileOffDeadline = typedXmlPullParser.getAttributeLong((String) null, ATTR_VALUE);
                } else if (TAG_ALWAYS_ON_VPN_PACKAGE.equals(name)) {
                    this.mAlwaysOnVpnPackage = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE);
                } else if (TAG_ALWAYS_ON_VPN_LOCKDOWN.equals(name)) {
                    this.mAlwaysOnVpnLockdown = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_PREFERENTIAL_NETWORK_SERVICE_ENABLED.equals(name)) {
                    boolean attributeBoolean = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                    if (attributeBoolean) {
                        PreferentialNetworkServiceConfig.Builder builder = new PreferentialNetworkServiceConfig.Builder();
                        builder.setEnabled(attributeBoolean);
                        builder.setNetworkId(1);
                        this.mPreferentialNetworkServiceConfigs = List.of(builder.build());
                    }
                } else if (TAG_COMMON_CRITERIA_MODE.equals(name)) {
                    this.mCommonCriteriaMode = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_PASSWORD_COMPLEXITY.equals(name)) {
                    this.mPasswordComplexity = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_NEARBY_NOTIFICATION_STREAMING_POLICY.equals(name)) {
                    this.mNearbyNotificationStreamingPolicy = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_NEARBY_APP_STREAMING_POLICY.equals(name)) {
                    this.mNearbyAppStreamingPolicy = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_ORGANIZATION_ID.equals(name)) {
                    if (typedXmlPullParser.next() == 4) {
                        this.mOrganizationId = typedXmlPullParser.getText();
                    } else {
                        Slogf.w("DevicePolicyManager", "Missing Organization ID.");
                    }
                } else if (TAG_ENROLLMENT_SPECIFIC_ID.equals(name)) {
                    if (typedXmlPullParser.next() == 4) {
                        this.mEnrollmentSpecificId = typedXmlPullParser.getText();
                    } else {
                        Slogf.w("DevicePolicyManager", "Missing Enrollment-specific ID.");
                    }
                } else if (TAG_ADMIN_CAN_GRANT_SENSORS_PERMISSIONS.equals(name)) {
                    this.mAdminCanGrantSensorsPermissions = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, false);
                } else if (TAG_USB_DATA_SIGNALING.equals(name)) {
                    this.mUsbDataSignalingEnabled = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_VALUE, true);
                } else if (TAG_WIFI_MIN_SECURITY.equals(name)) {
                    this.mWifiMinimumSecurityLevel = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_SSID_ALLOWLIST.equals(name)) {
                    this.mWifiSsidPolicy = new WifiSsidPolicy(0, new ArraySet(readWifiSsids(typedXmlPullParser, TAG_SSID)));
                } else if (TAG_SSID_DENYLIST.equals(name)) {
                    this.mWifiSsidPolicy = new WifiSsidPolicy(1, new ArraySet(readWifiSsids(typedXmlPullParser, TAG_SSID)));
                } else if (TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIGS.equals(name)) {
                    List<PreferentialNetworkServiceConfig> preferentialNetworkServiceConfigs = getPreferentialNetworkServiceConfigs(typedXmlPullParser, name);
                    if (!preferentialNetworkServiceConfigs.isEmpty()) {
                        this.mPreferentialNetworkServiceConfigs = preferentialNetworkServiceConfigs;
                    }
                } else if (TAG_MTE_POLICY.equals(name)) {
                    this.mtePolicy = typedXmlPullParser.getAttributeInt((String) null, ATTR_VALUE);
                } else if (TAG_CROSS_PROFILE_CALLER_ID_POLICY.equals(name)) {
                    this.mManagedProfileCallerIdAccess = readPackagePolicy(typedXmlPullParser);
                } else if (TAG_CROSS_PROFILE_CONTACTS_SEARCH_POLICY.equals(name)) {
                    this.mManagedProfileContactsAccess = readPackagePolicy(typedXmlPullParser);
                } else if (TAG_MANAGED_SUBSCRIPTIONS_POLICY.equals(name)) {
                    this.mManagedSubscriptionsPolicy = ManagedSubscriptionsPolicy.readFromXml(typedXmlPullParser);
                } else if (TAG_CREDENTIAL_MANAGER_POLICY.equals(name)) {
                    this.mCredentialManagerPolicy = readPackagePolicy(typedXmlPullParser);
                } else if (TAG_DIALER_PACKAGE.equals(name)) {
                    this.mDialerPackage = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE);
                } else if (TAG_SMS_PACKAGE.equals(name)) {
                    this.mSmsPackage = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE);
                } else {
                    Slogf.w("DevicePolicyManager", "Unknown admin tag: %s", new Object[]{name});
                    XmlUtils.skipCurrentTag(typedXmlPullParser);
                }
            }
        }
    }

    private PackagePolicy readPackagePolicy(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        return new PackagePolicy(typedXmlPullParser.getAttributeInt((String) null, ATTR_PACKAGE_POLICY_MODE), new ArraySet(readPackageList(typedXmlPullParser, TAG_PACKAGE_POLICY_PACKAGE_NAMES)));
    }

    private List<WifiSsid> readWifiSsids(TypedXmlPullParser typedXmlPullParser, String str) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        readAttributeValues(typedXmlPullParser, str, arrayList);
        return (List) arrayList.stream().map(new Function() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                WifiSsid lambda$readWifiSsids$1;
                lambda$readWifiSsids$1 = ActiveAdmin.lambda$readWifiSsids$1((String) obj);
                return lambda$readWifiSsids$1;
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WifiSsid lambda$readWifiSsids$1(String str) {
        return WifiSsid.fromBytes(str.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> readPackageList(TypedXmlPullParser typedXmlPullParser, String str) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next != 3 && next != 4) {
                String name = typedXmlPullParser.getName();
                if (TAG_PACKAGE_LIST_ITEM.equals(name)) {
                    String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE);
                    if (attributeValue != null) {
                        arrayList.add(attributeValue);
                    } else {
                        Slogf.w("DevicePolicyManager", "Package name missing under %s", new Object[]{name});
                    }
                } else {
                    Slogf.w("DevicePolicyManager", "Unknown tag under %s: ", new Object[]{str, name});
                }
            }
        }
        return arrayList;
    }

    private void readAttributeValues(TypedXmlPullParser typedXmlPullParser, String str, Collection<String> collection) throws XmlPullParserException, IOException {
        collection.clear();
        int depth = typedXmlPullParser.getDepth();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && typedXmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next != 3 && next != 4) {
                String name = typedXmlPullParser.getName();
                if (str.equals(name)) {
                    collection.add(typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE));
                } else {
                    Slogf.e("DevicePolicyManager", "Expected tag %s but found %s", new Object[]{str, name});
                }
            }
        }
    }

    private ArrayMap<String, TrustAgentInfo> getAllTrustAgentInfos(TypedXmlPullParser typedXmlPullParser, String str) throws XmlPullParserException, IOException {
        int depth = typedXmlPullParser.getDepth();
        ArrayMap<String, TrustAgentInfo> arrayMap = new ArrayMap<>();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next != 3 && next != 4) {
                String name = typedXmlPullParser.getName();
                if (TAG_TRUST_AGENT_COMPONENT.equals(name)) {
                    arrayMap.put(typedXmlPullParser.getAttributeValue((String) null, ATTR_VALUE), getTrustAgentInfo(typedXmlPullParser, str));
                } else {
                    Slogf.w("DevicePolicyManager", "Unknown tag under %s: %s", new Object[]{str, name});
                }
            }
        }
        return arrayMap;
    }

    private TrustAgentInfo getTrustAgentInfo(TypedXmlPullParser typedXmlPullParser, String str) throws XmlPullParserException, IOException {
        int depth = typedXmlPullParser.getDepth();
        TrustAgentInfo trustAgentInfo = new TrustAgentInfo(null);
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next != 3 && next != 4) {
                String name = typedXmlPullParser.getName();
                if (TAG_TRUST_AGENT_COMPONENT_OPTIONS.equals(name)) {
                    trustAgentInfo.options = PersistableBundle.restoreFromXml(typedXmlPullParser);
                } else {
                    Slogf.w("DevicePolicyManager", "Unknown tag under %s: %s", new Object[]{str, name});
                }
            }
        }
        return trustAgentInfo;
    }

    private List<PreferentialNetworkServiceConfig> getPreferentialNetworkServiceConfigs(TypedXmlPullParser typedXmlPullParser, String str) throws XmlPullParserException, IOException {
        int depth = typedXmlPullParser.getDepth();
        ArrayList arrayList = new ArrayList();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next != 3 && next != 4) {
                String name = typedXmlPullParser.getName();
                if (TAG_PREFERENTIAL_NETWORK_SERVICE_CONFIG.equals(name)) {
                    arrayList.add(PreferentialNetworkServiceConfig.getPreferentialNetworkServiceConfig(typedXmlPullParser, str));
                } else {
                    Slogf.w("DevicePolicyManager", "Unknown tag under %s: %s", new Object[]{str, name});
                }
            }
        }
        return arrayList;
    }

    boolean hasUserRestrictions() {
        Bundle bundle = this.userRestrictions;
        return bundle != null && bundle.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle ensureUserRestrictions() {
        if (this.userRestrictions == null) {
            this.userRestrictions = new Bundle();
        }
        return this.userRestrictions;
    }

    public void transfer(DeviceAdminInfo deviceAdminInfo) {
        if (hasParentActiveAdmin()) {
            this.parentAdmin.info = deviceAdminInfo;
        }
        this.info = deviceAdminInfo;
    }

    Bundle addSyntheticRestrictions(Bundle bundle) {
        if (this.disableCamera) {
            bundle.putBoolean("no_camera", true);
        }
        if (this.requireAutoTime) {
            bundle.putBoolean("no_config_date_time", true);
        }
        return bundle;
    }

    static Bundle removeDeprecatedRestrictions(Bundle bundle) {
        Iterator it = UserRestrictionsUtils.DEPRECATED_USER_RESTRICTIONS.iterator();
        while (it.hasNext()) {
            bundle.remove((String) it.next());
        }
        return bundle;
    }

    static Bundle filterRestrictions(Bundle bundle, Predicate<String> predicate) {
        Bundle bundle2 = new Bundle();
        for (String str : bundle.keySet()) {
            if (bundle.getBoolean(str) && predicate.test(str)) {
                bundle2.putBoolean(str, true);
            }
        }
        return bundle2;
    }

    Bundle getEffectiveRestrictions() {
        return addSyntheticRestrictions(removeDeprecatedRestrictions(new Bundle(ensureUserRestrictions())));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle getLocalUserRestrictions(final int i) {
        return filterRestrictions(getEffectiveRestrictions(), new Predicate() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isLocal;
                isLocal = UserRestrictionsUtils.isLocal(i, (String) obj);
                return isLocal;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle getGlobalUserRestrictions(final int i) {
        return filterRestrictions(getEffectiveRestrictions(), new Predicate() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean isGlobal;
                isGlobal = UserRestrictionsUtils.isGlobal(i, (String) obj);
                return isGlobal;
            }
        });
    }

    void dumpPackagePolicy(final IndentingPrintWriter indentingPrintWriter, String str, PackagePolicy packagePolicy) {
        indentingPrintWriter.print(str);
        indentingPrintWriter.println(":");
        if (packagePolicy != null) {
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print("policyType=");
            indentingPrintWriter.println(packagePolicy.getPolicyType());
            indentingPrintWriter.println("packageNames:");
            indentingPrintWriter.increaseIndent();
            packagePolicy.getPackageNames().forEach(new Consumer() { // from class: com.android.server.devicepolicy.ActiveAdmin$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    indentingPrintWriter.println((String) obj);
                }
            });
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.print("uid=");
        indentingPrintWriter.println(getUid());
        indentingPrintWriter.print("testOnlyAdmin=");
        indentingPrintWriter.println(this.testOnlyAdmin);
        if (this.info != null) {
            indentingPrintWriter.println("policies:");
            ArrayList usedPolicies = this.info.getUsedPolicies();
            if (usedPolicies != null) {
                indentingPrintWriter.increaseIndent();
                for (int i = 0; i < usedPolicies.size(); i++) {
                    indentingPrintWriter.println(((DeviceAdminInfo.PolicyInfo) usedPolicies.get(i)).tag);
                }
                indentingPrintWriter.decreaseIndent();
            }
        }
        indentingPrintWriter.print("passwordQuality=0x");
        indentingPrintWriter.println(Integer.toHexString(this.mPasswordPolicy.quality));
        indentingPrintWriter.print("minimumPasswordLength=");
        indentingPrintWriter.println(this.mPasswordPolicy.length);
        indentingPrintWriter.print("passwordHistoryLength=");
        indentingPrintWriter.println(this.passwordHistoryLength);
        indentingPrintWriter.print("minimumPasswordUpperCase=");
        indentingPrintWriter.println(this.mPasswordPolicy.upperCase);
        indentingPrintWriter.print("minimumPasswordLowerCase=");
        indentingPrintWriter.println(this.mPasswordPolicy.lowerCase);
        indentingPrintWriter.print("minimumPasswordLetters=");
        indentingPrintWriter.println(this.mPasswordPolicy.letters);
        indentingPrintWriter.print("minimumPasswordNumeric=");
        indentingPrintWriter.println(this.mPasswordPolicy.numeric);
        indentingPrintWriter.print("minimumPasswordSymbols=");
        indentingPrintWriter.println(this.mPasswordPolicy.symbols);
        indentingPrintWriter.print("minimumPasswordNonLetter=");
        indentingPrintWriter.println(this.mPasswordPolicy.nonLetter);
        indentingPrintWriter.print("maximumTimeToUnlock=");
        indentingPrintWriter.println(this.maximumTimeToUnlock);
        indentingPrintWriter.print("strongAuthUnlockTimeout=");
        indentingPrintWriter.println(this.strongAuthUnlockTimeout);
        indentingPrintWriter.print("maximumFailedPasswordsForWipe=");
        indentingPrintWriter.println(this.maximumFailedPasswordsForWipe);
        indentingPrintWriter.print("specifiesGlobalProxy=");
        indentingPrintWriter.println(this.specifiesGlobalProxy);
        indentingPrintWriter.print("passwordExpirationTimeout=");
        indentingPrintWriter.println(this.passwordExpirationTimeout);
        indentingPrintWriter.print("passwordExpirationDate=");
        indentingPrintWriter.println(this.passwordExpirationDate);
        if (this.globalProxySpec != null) {
            indentingPrintWriter.print("globalProxySpec=");
            indentingPrintWriter.println(this.globalProxySpec);
        }
        if (this.globalProxyExclusionList != null) {
            indentingPrintWriter.print("globalProxyEclusionList=");
            indentingPrintWriter.println(this.globalProxyExclusionList);
        }
        indentingPrintWriter.print("encryptionRequested=");
        indentingPrintWriter.println(this.encryptionRequested);
        indentingPrintWriter.print("disableCamera=");
        indentingPrintWriter.println(this.disableCamera);
        indentingPrintWriter.print("disableCallerId=");
        indentingPrintWriter.println(this.disableCallerId);
        indentingPrintWriter.print("disableContactsSearch=");
        indentingPrintWriter.println(this.disableContactsSearch);
        indentingPrintWriter.print("disableBluetoothContactSharing=");
        indentingPrintWriter.println(this.disableBluetoothContactSharing);
        indentingPrintWriter.print("disableScreenCapture=");
        indentingPrintWriter.println(this.disableScreenCapture);
        indentingPrintWriter.print("requireAutoTime=");
        indentingPrintWriter.println(this.requireAutoTime);
        indentingPrintWriter.print("forceEphemeralUsers=");
        indentingPrintWriter.println(this.forceEphemeralUsers);
        indentingPrintWriter.print("isNetworkLoggingEnabled=");
        indentingPrintWriter.println(this.isNetworkLoggingEnabled);
        indentingPrintWriter.print("disabledKeyguardFeatures=");
        indentingPrintWriter.println(this.disabledKeyguardFeatures);
        indentingPrintWriter.print("crossProfileWidgetProviders=");
        indentingPrintWriter.println(this.crossProfileWidgetProviders);
        if (this.permittedAccessiblityServices != null) {
            indentingPrintWriter.print("permittedAccessibilityServices=");
            indentingPrintWriter.println(this.permittedAccessiblityServices);
        }
        if (this.permittedInputMethods != null) {
            indentingPrintWriter.print("permittedInputMethods=");
            indentingPrintWriter.println(this.permittedInputMethods);
        }
        if (this.permittedNotificationListeners != null) {
            indentingPrintWriter.print("permittedNotificationListeners=");
            indentingPrintWriter.println(this.permittedNotificationListeners);
        }
        if (this.keepUninstalledPackages != null) {
            indentingPrintWriter.print("keepUninstalledPackages=");
            indentingPrintWriter.println(this.keepUninstalledPackages);
        }
        if (this.meteredDisabledPackages != null) {
            indentingPrintWriter.print("meteredDisabledPackages=");
            indentingPrintWriter.println(this.meteredDisabledPackages);
        }
        if (this.protectedPackages != null) {
            indentingPrintWriter.print("protectedPackages=");
            indentingPrintWriter.println(this.protectedPackages);
        }
        if (this.suspendedPackages != null) {
            indentingPrintWriter.print("suspendedPackages=");
            indentingPrintWriter.println(this.suspendedPackages);
        }
        indentingPrintWriter.print("organizationColor=");
        indentingPrintWriter.println(this.organizationColor);
        if (this.organizationName != null) {
            indentingPrintWriter.print("organizationName=");
            indentingPrintWriter.println(this.organizationName);
        }
        indentingPrintWriter.println("userRestrictions:");
        UserRestrictionsUtils.dumpRestrictions(indentingPrintWriter, "  ", this.userRestrictions);
        indentingPrintWriter.print("defaultEnabledRestrictionsAlreadySet=");
        indentingPrintWriter.println(this.defaultEnabledRestrictionsAlreadySet);
        dumpPackagePolicy(indentingPrintWriter, "managedProfileCallerIdPolicy", this.mManagedProfileCallerIdAccess);
        dumpPackagePolicy(indentingPrintWriter, "managedProfileContactsPolicy", this.mManagedProfileContactsAccess);
        dumpPackagePolicy(indentingPrintWriter, "credentialManagerPolicy", this.mCredentialManagerPolicy);
        indentingPrintWriter.print("isParent=");
        indentingPrintWriter.println(this.isParent);
        if (this.parentAdmin != null) {
            indentingPrintWriter.println("parentAdmin:");
            indentingPrintWriter.increaseIndent();
            this.parentAdmin.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
        if (this.mCrossProfileCalendarPackages != null) {
            indentingPrintWriter.print("mCrossProfileCalendarPackages=");
            indentingPrintWriter.println(this.mCrossProfileCalendarPackages);
        }
        indentingPrintWriter.print("mCrossProfilePackages=");
        indentingPrintWriter.println(this.mCrossProfilePackages);
        indentingPrintWriter.print("mSuspendPersonalApps=");
        indentingPrintWriter.println(this.mSuspendPersonalApps);
        indentingPrintWriter.print("mProfileMaximumTimeOffMillis=");
        indentingPrintWriter.println(this.mProfileMaximumTimeOffMillis);
        indentingPrintWriter.print("mProfileOffDeadline=");
        indentingPrintWriter.println(this.mProfileOffDeadline);
        indentingPrintWriter.print("mAlwaysOnVpnPackage=");
        indentingPrintWriter.println(this.mAlwaysOnVpnPackage);
        indentingPrintWriter.print("mAlwaysOnVpnLockdown=");
        indentingPrintWriter.println(this.mAlwaysOnVpnLockdown);
        indentingPrintWriter.print("mCommonCriteriaMode=");
        indentingPrintWriter.println(this.mCommonCriteriaMode);
        indentingPrintWriter.print("mPasswordComplexity=");
        indentingPrintWriter.println(this.mPasswordComplexity);
        indentingPrintWriter.print("mNearbyNotificationStreamingPolicy=");
        indentingPrintWriter.println(this.mNearbyNotificationStreamingPolicy);
        indentingPrintWriter.print("mNearbyAppStreamingPolicy=");
        indentingPrintWriter.println(this.mNearbyAppStreamingPolicy);
        if (!TextUtils.isEmpty(this.mOrganizationId)) {
            indentingPrintWriter.print("mOrganizationId=");
            indentingPrintWriter.println(this.mOrganizationId);
        }
        if (!TextUtils.isEmpty(this.mEnrollmentSpecificId)) {
            indentingPrintWriter.print("mEnrollmentSpecificId=");
            indentingPrintWriter.println(this.mEnrollmentSpecificId);
        }
        indentingPrintWriter.print("mAdminCanGrantSensorsPermissions=");
        indentingPrintWriter.println(this.mAdminCanGrantSensorsPermissions);
        indentingPrintWriter.print("mUsbDataSignaling=");
        indentingPrintWriter.println(this.mUsbDataSignalingEnabled);
        indentingPrintWriter.print("mWifiMinimumSecurityLevel=");
        indentingPrintWriter.println(this.mWifiMinimumSecurityLevel);
        WifiSsidPolicy wifiSsidPolicy = this.mWifiSsidPolicy;
        if (wifiSsidPolicy != null) {
            if (wifiSsidPolicy.getPolicyType() == 0) {
                indentingPrintWriter.print("mSsidAllowlist=");
            } else {
                indentingPrintWriter.print("mSsidDenylist=");
            }
            indentingPrintWriter.println(ssidsToStrings(this.mWifiSsidPolicy.getSsids()));
        }
        if (this.mFactoryResetProtectionPolicy != null) {
            indentingPrintWriter.println("mFactoryResetProtectionPolicy:");
            indentingPrintWriter.increaseIndent();
            this.mFactoryResetProtectionPolicy.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
        if (this.mPreferentialNetworkServiceConfigs != null) {
            indentingPrintWriter.println("mPreferentialNetworkServiceConfigs:");
            indentingPrintWriter.increaseIndent();
            Iterator<PreferentialNetworkServiceConfig> it = this.mPreferentialNetworkServiceConfigs.iterator();
            while (it.hasNext()) {
                it.next().dump(indentingPrintWriter);
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.print("mtePolicy=");
        indentingPrintWriter.println(this.mtePolicy);
        indentingPrintWriter.print("accountTypesWithManagementDisabled=");
        indentingPrintWriter.println(this.accountTypesWithManagementDisabled);
        if (this.mManagedSubscriptionsPolicy != null) {
            indentingPrintWriter.println("mManagedSubscriptionsPolicy:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println(this.mManagedSubscriptionsPolicy);
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.print("mDialerPackage=");
        indentingPrintWriter.println(this.mDialerPackage);
        indentingPrintWriter.print("mSmsPackage=");
        indentingPrintWriter.println(this.mSmsPackage);
    }
}
