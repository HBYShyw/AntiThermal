package com.android.server;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.BroadcastOptions;
import android.app.compat.CompatChanges;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.telephony.BarringInfo;
import android.telephony.CallQuality;
import android.telephony.CallState;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.LinkCapacityEstimate;
import android.telephony.LocationAccessPolicy;
import android.telephony.PhoneCapability;
import android.telephony.PhysicalChannelConfig;
import android.telephony.PreciseCallState;
import android.telephony.PreciseDataConnectionState;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.MediaQualityStatus;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.LocalLog;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.telephony.ICarrierConfigChangeListener;
import com.android.internal.telephony.ICarrierPrivilegesCallback;
import com.android.internal.telephony.IOnSubscriptionsChangedListener;
import com.android.internal.telephony.IPhoneStateListener;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.FunctionalUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.TelephonyRegistry;
import com.android.server.am.BatteryStatsService;
import com.android.server.autofill.HintsHelper;
import dalvik.annotation.optimization.NeverCompile;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import system.ext.loader.core.ExtLoader;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TelephonyRegistry extends ITelephonyRegistry.Stub {
    private static final String ACTION_ANY_DATA_CONNECTION_STATE_CHANGED = "android.intent.action.ANY_DATA_STATE";
    private static final String ACTION_RADIO_POWER_STATE_CHANGED = "org.codeaurora.intent.action.RADIO_POWER_STATE";
    public static final String ACTION_SIGNAL_STRENGTH_CHANGED = "android.intent.action.SIG_STR";
    private static final String ACTION_SUBSCRIPTION_PHONE_STATE_CHANGED = "android.intent.action.SUBSCRIPTION_PHONE_STATE";
    private static final boolean DBG = false;
    private static final boolean DBG_LOC = false;
    private static final long DISPLAY_INFO_NR_ADVANCED_SUPPORTED = 181658987;
    private static final String EXTRA_SUBSCRIPTION_INDEX = "android.telephony.extra.SUBSCRIPTION_INDEX";
    private static final List<LinkCapacityEstimate> INVALID_LCE_LIST = new ArrayList(Arrays.asList(new LinkCapacityEstimate(2, -1, -1)));
    private static final int MSG_UPDATE_DEFAULT_SUB = 2;
    private static final int MSG_USER_SWITCHED = 1;
    private static final String PHONE_CONSTANTS_DATA_APN_KEY = "apn";
    private static final String PHONE_CONSTANTS_DATA_APN_TYPE_KEY = "apnType";
    private static final String PHONE_CONSTANTS_SLOT_KEY = "slot";
    private static final String PHONE_CONSTANTS_STATE_KEY = "state";
    private static final String PHONE_CONSTANTS_SUBSCRIPTION_KEY = "subscription";
    private static final Set<Integer> REQUIRE_PRECISE_PHONE_STATE_PERMISSION;
    private static final long REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_ACTIVE_DATA_SUB_ID = 182478738;
    private static final long REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_CELL_INFO = 184323934;
    private static final long REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_DISPLAY_INFO = 183164979;
    private static final String TAG = "TelephonyRegistry";
    private static final boolean VDBG = false;
    private int[] mAllowedNetworkTypeReason;
    private long[] mAllowedNetworkTypeValue;
    private final AppOpsManager mAppOps;
    private int[] mBackgroundCallState;
    private List<BarringInfo> mBarringInfo;
    private int[] mCallDisconnectCause;
    private boolean[] mCallForwarding;
    private String[] mCallIncomingNumber;
    private int[] mCallNetworkType;
    private int[] mCallPreciseDisconnectCause;
    private CallQuality[] mCallQuality;
    private int[] mCallState;
    private ArrayList<List<CallState>> mCallStateLists;
    private boolean[] mCarrierNetworkChangeState;
    private List<Pair<List<String>, int[]>> mCarrierPrivilegeStates;
    private List<Pair<String, Integer>> mCarrierServiceStates;
    private CellIdentity[] mCellIdentity;
    private ArrayList<List<CellInfo>> mCellInfo;
    private ConfigurationProvider mConfigurationProvider;
    private final Context mContext;
    private int[] mDataActivationState;
    private int[] mDataActivity;
    private int[] mDataConnectionNetworkType;
    private int[] mDataConnectionState;
    private int[] mDataEnabledReason;
    private int[] mECBMReason;
    private boolean[] mECBMStarted;
    private Map<Integer, List<EmergencyNumber>> mEmergencyNumberList;
    private int[] mForegroundCallState;
    private List<ImsReasonInfo> mImsReasonInfo;
    private boolean[] mIsDataEnabled;
    private List<List<LinkCapacityEstimate>> mLinkCapacityEstimateLists;
    private List<SparseArray<MediaQualityStatus>> mMediaQualityStatus;
    private boolean[] mMessageWaiting;
    private int mNumPhones;
    private EmergencyNumber[] mOutgoingCallEmergencyNumber;
    private EmergencyNumber[] mOutgoingSmsEmergencyNumber;
    private List<List<PhysicalChannelConfig>> mPhysicalChannelConfigs;
    private PreciseCallState[] mPreciseCallState;
    private List<Map<Pair<Integer, ApnSetting>, PreciseDataConnectionState>> mPreciseDataConnectionStates;
    private int[] mRingingCallState;
    private int[] mSCBMReason;
    private boolean[] mSCBMStarted;
    private ServiceState[] mServiceState;
    private SignalStrength[] mSignalStrength;
    private int[] mSrvccState;
    private TelephonyDisplayInfo[] mTelephonyDisplayInfos;
    private boolean[] mUserMobileDataState;
    private int[] mVoiceActivationState;
    private ITelephonyRegistryExt mTelephonyRegistryExt = (ITelephonyRegistryExt) ExtLoader.type(ITelephonyRegistryExt.class).base(this).create();
    private final ArrayList<IBinder> mRemoveList = new ArrayList<>();
    private final ArrayList<Record> mRecords = new ArrayList<>();
    private boolean mHasNotifySubscriptionInfoChangedOccurred = false;
    private boolean mHasNotifyOpportunisticSubscriptionInfoChangedOccurred = false;
    private int mDefaultSubId = -1;
    private int mDefaultPhoneId = -1;
    private PhoneCapability mPhoneCapability = null;
    private int mActiveDataSubId = -1;
    private int mRadioPowerState = 2;
    private final LocalLog mLocalLog = new LocalLog(200);
    private final LocalLog mListenLog = new LocalLog(200);
    private final Handler mHandler = new Handler() { // from class: com.android.server.TelephonyRegistry.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                int activeModemCount = TelephonyRegistry.this.getTelephonyManager().getActiveModemCount();
                for (int i2 = 0; i2 < activeModemCount; i2++) {
                    int subscriptionId = SubscriptionManager.getSubscriptionId(i2);
                    if (!SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
                        subscriptionId = Integer.MAX_VALUE;
                    }
                    TelephonyRegistry telephonyRegistry = TelephonyRegistry.this;
                    telephonyRegistry.notifyCellLocationForSubscriber(subscriptionId, telephonyRegistry.mCellIdentity[i2], true);
                }
                return;
            }
            if (i != 2) {
                return;
            }
            int i3 = message.arg1;
            int i4 = message.arg2;
            synchronized (TelephonyRegistry.this.mRecords) {
                Iterator it = TelephonyRegistry.this.mRecords.iterator();
                while (it.hasNext()) {
                    Record record = (Record) it.next();
                    if (record.subId == Integer.MAX_VALUE) {
                        TelephonyRegistry.this.checkPossibleMissNotify(record, i3);
                    }
                }
                TelephonyRegistry.this.handleRemoveListLocked();
            }
            TelephonyRegistry.this.mDefaultSubId = i4;
            TelephonyRegistry.this.mDefaultPhoneId = i3;
            TelephonyRegistry.this.mLocalLog.log("Default subscription updated: mDefaultPhoneId=" + TelephonyRegistry.this.mDefaultPhoneId + ", mDefaultSubId=" + TelephonyRegistry.this.mDefaultSubId);
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.TelephonyRegistry.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.USER_SWITCHED".equals(action)) {
                TelephonyRegistry.this.mHandler.sendMessage(TelephonyRegistry.this.mHandler.obtainMessage(1, intent.getIntExtra("android.intent.extra.user_handle", 0), 0));
                return;
            }
            if (action.equals("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED")) {
                int intExtra = intent.getIntExtra(TelephonyRegistry.EXTRA_SUBSCRIPTION_INDEX, SubscriptionManager.getDefaultSubscriptionId());
                int intExtra2 = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", TelephonyRegistry.this.getPhoneIdFromSubId(intExtra));
                if (TelephonyRegistry.this.validatePhoneId(intExtra2)) {
                    if (intExtra == TelephonyRegistry.this.mDefaultSubId && intExtra2 == TelephonyRegistry.this.mDefaultPhoneId) {
                        return;
                    }
                    TelephonyRegistry.this.mHandler.sendMessage(TelephonyRegistry.this.mHandler.obtainMessage(2, intExtra2, intExtra));
                    return;
                }
                return;
            }
            if (action.equals("android.telephony.action.MULTI_SIM_CONFIG_CHANGED")) {
                TelephonyRegistry.this.onMultiSimConfigChanged();
            }
        }
    };
    private final IBatteryStats mBatteryStats = BatteryStatsService.getService();

    private boolean doesLimitApplyForListeners(int i, int i2) {
        return (i == 1000 || i == 1001 || i == i2) ? false : true;
    }

    private String getNetworkTypeName(int i) {
        switch (i) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 7:
                return "CDMA - 1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "iDEN";
            case 12:
                return "CDMA - EvDo rev. B";
            case 13:
                return "LTE";
            case 14:
                return "CDMA - eHRPD";
            case 15:
                return "HSPA+";
            case 16:
                return "GSM";
            case 17:
                return "TD_SCDMA";
            case 18:
                return "IWLAN";
            case 19:
            default:
                return "UNKNOWN";
            case 20:
                return "NR";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Record {
        IBinder binder;
        IPhoneStateListener callback;
        int callerPid;
        int callerUid;
        String callingFeatureId;
        String callingPackage;
        ICarrierConfigChangeListener carrierConfigChangeListener;
        ICarrierPrivilegesCallback carrierPrivilegesCallback;
        Context context;
        TelephonyRegistryDeathRecipient deathRecipient;
        Set<Integer> eventList;
        IOnSubscriptionsChangedListener onOpportunisticSubscriptionsChangedListenerCallback;
        IOnSubscriptionsChangedListener onSubscriptionsChangedListenerCallback;
        int phoneId;
        boolean renounceCoarseLocationAccess;
        boolean renounceFineLocationAccess;
        int subId;

        private Record() {
            this.subId = -1;
            this.phoneId = -1;
        }

        boolean matchTelephonyCallbackEvent(int i) {
            return this.callback != null && this.eventList.contains(Integer.valueOf(i));
        }

        boolean matchOnSubscriptionsChangedListener() {
            return this.onSubscriptionsChangedListenerCallback != null;
        }

        boolean matchOnOpportunisticSubscriptionsChangedListener() {
            return this.onOpportunisticSubscriptionsChangedListenerCallback != null;
        }

        boolean matchCarrierPrivilegesCallback() {
            return this.carrierPrivilegesCallback != null;
        }

        boolean matchCarrierConfigChangeListener() {
            return this.carrierConfigChangeListener != null;
        }

        boolean canReadCallLog() {
            try {
                return TelephonyPermissions.checkReadCallLog(this.context, this.subId, this.callerPid, this.callerUid, this.callingPackage, this.callingFeatureId);
            } catch (SecurityException unused) {
                return false;
            }
        }

        public String toString() {
            return "{callingPackage=" + TelephonyRegistry.pii(this.callingPackage) + " callerUid=" + this.callerUid + " binder=" + this.binder + " callback=" + this.callback + " onSubscriptionsChangedListenererCallback=" + this.onSubscriptionsChangedListenerCallback + " onOpportunisticSubscriptionsChangedListenererCallback=" + this.onOpportunisticSubscriptionsChangedListenerCallback + " carrierPrivilegesCallback=" + this.carrierPrivilegesCallback + " carrierConfigChangeListener=" + this.carrierConfigChangeListener + " subId=" + this.subId + " phoneId=" + this.phoneId + " events=" + this.eventList + "}";
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class ConfigurationProvider {
        public int getRegistrationLimit() {
            return ((Integer) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda2
                public final Object getOrThrow() {
                    Integer lambda$getRegistrationLimit$0;
                    lambda$getRegistrationLimit$0 = TelephonyRegistry.ConfigurationProvider.lambda$getRegistrationLimit$0();
                    return lambda$getRegistrationLimit$0;
                }
            })).intValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Integer lambda$getRegistrationLimit$0() throws Exception {
            return Integer.valueOf(DeviceConfig.getInt("telephony", "phone_state_listener_per_pid_registration_limit", 50));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean lambda$isRegistrationLimitEnabledInPlatformCompat$1(int i) throws Exception {
            return Boolean.valueOf(CompatChanges.isChangeEnabled(150880553L, i));
        }

        public boolean isRegistrationLimitEnabledInPlatformCompat(final int i) {
            return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda3
                public final Object getOrThrow() {
                    Boolean lambda$isRegistrationLimitEnabledInPlatformCompat$1;
                    lambda$isRegistrationLimitEnabledInPlatformCompat$1 = TelephonyRegistry.ConfigurationProvider.lambda$isRegistrationLimitEnabledInPlatformCompat$1(i);
                    return lambda$isRegistrationLimitEnabledInPlatformCompat$1;
                }
            })).booleanValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean lambda$isCallStateReadPhoneStateEnforcedInPlatformCompat$2(String str, UserHandle userHandle) throws Exception {
            return Boolean.valueOf(CompatChanges.isChangeEnabled(157233955L, str, userHandle));
        }

        public boolean isCallStateReadPhoneStateEnforcedInPlatformCompat(final String str, final UserHandle userHandle) {
            return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda5
                public final Object getOrThrow() {
                    Boolean lambda$isCallStateReadPhoneStateEnforcedInPlatformCompat$2;
                    lambda$isCallStateReadPhoneStateEnforcedInPlatformCompat$2 = TelephonyRegistry.ConfigurationProvider.lambda$isCallStateReadPhoneStateEnforcedInPlatformCompat$2(str, userHandle);
                    return lambda$isCallStateReadPhoneStateEnforcedInPlatformCompat$2;
                }
            })).booleanValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean lambda$isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat$3(String str, UserHandle userHandle) throws Exception {
            return Boolean.valueOf(CompatChanges.isChangeEnabled(TelephonyRegistry.REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_ACTIVE_DATA_SUB_ID, str, userHandle));
        }

        public boolean isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat(final String str, final UserHandle userHandle) {
            return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda0
                public final Object getOrThrow() {
                    Boolean lambda$isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat$3;
                    lambda$isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat$3 = TelephonyRegistry.ConfigurationProvider.lambda$isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat$3(str, userHandle);
                    return lambda$isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat$3;
                }
            })).booleanValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean lambda$isCellInfoReadPhoneStateEnforcedInPlatformCompat$4(String str, UserHandle userHandle) throws Exception {
            return Boolean.valueOf(CompatChanges.isChangeEnabled(TelephonyRegistry.REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_CELL_INFO, str, userHandle));
        }

        public boolean isCellInfoReadPhoneStateEnforcedInPlatformCompat(final String str, final UserHandle userHandle) {
            return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda4
                public final Object getOrThrow() {
                    Boolean lambda$isCellInfoReadPhoneStateEnforcedInPlatformCompat$4;
                    lambda$isCellInfoReadPhoneStateEnforcedInPlatformCompat$4 = TelephonyRegistry.ConfigurationProvider.lambda$isCellInfoReadPhoneStateEnforcedInPlatformCompat$4(str, userHandle);
                    return lambda$isCellInfoReadPhoneStateEnforcedInPlatformCompat$4;
                }
            })).booleanValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean lambda$isDisplayInfoReadPhoneStateEnforcedInPlatformCompat$5(String str, UserHandle userHandle) throws Exception {
            return Boolean.valueOf(CompatChanges.isChangeEnabled(TelephonyRegistry.REQUIRE_READ_PHONE_STATE_PERMISSION_FOR_DISPLAY_INFO, str, userHandle));
        }

        public boolean isDisplayInfoReadPhoneStateEnforcedInPlatformCompat(final String str, final UserHandle userHandle) {
            return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda6
                public final Object getOrThrow() {
                    Boolean lambda$isDisplayInfoReadPhoneStateEnforcedInPlatformCompat$5;
                    lambda$isDisplayInfoReadPhoneStateEnforcedInPlatformCompat$5 = TelephonyRegistry.ConfigurationProvider.lambda$isDisplayInfoReadPhoneStateEnforcedInPlatformCompat$5(str, userHandle);
                    return lambda$isDisplayInfoReadPhoneStateEnforcedInPlatformCompat$5;
                }
            })).booleanValue();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean lambda$isDisplayInfoNrAdvancedSupported$6(String str, UserHandle userHandle) throws Exception {
            return Boolean.valueOf(CompatChanges.isChangeEnabled(TelephonyRegistry.DISPLAY_INFO_NR_ADVANCED_SUPPORTED, str, userHandle));
        }

        public boolean isDisplayInfoNrAdvancedSupported(final String str, final UserHandle userHandle) {
            return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$ConfigurationProvider$$ExternalSyntheticLambda1
                public final Object getOrThrow() {
                    Boolean lambda$isDisplayInfoNrAdvancedSupported$6;
                    lambda$isDisplayInfoNrAdvancedSupported$6 = TelephonyRegistry.ConfigurationProvider.lambda$isDisplayInfoNrAdvancedSupported$6(str, userHandle);
                    return lambda$isDisplayInfoNrAdvancedSupported$6;
                }
            })).booleanValue();
        }
    }

    static {
        HashSet hashSet = new HashSet();
        REQUIRE_PRECISE_PHONE_STATE_PERMISSION = hashSet;
        hashSet.add(13);
        hashSet.add(14);
        hashSet.add(12);
        hashSet.add(26);
        hashSet.add(27);
        hashSet.add(28);
        hashSet.add(31);
        hashSet.add(32);
        hashSet.add(33);
        hashSet.add(34);
        hashSet.add(37);
        hashSet.add(39);
    }

    private boolean isLocationPermissionRequired(Set<Integer> set) {
        return set.contains(5) || set.contains(11);
    }

    private boolean isPhoneStatePermissionRequired(Set<Integer> set, String str, UserHandle userHandle) {
        if (set.contains(4) || set.contains(3) || set.contains(25)) {
            return true;
        }
        if ((set.contains(36) || set.contains(6)) && this.mConfigurationProvider.isCallStateReadPhoneStateEnforcedInPlatformCompat(str, userHandle)) {
            return true;
        }
        if (set.contains(23) && this.mConfigurationProvider.isActiveDataSubIdReadPhoneStateEnforcedInPlatformCompat(str, userHandle)) {
            return true;
        }
        if (set.contains(11) && this.mConfigurationProvider.isCellInfoReadPhoneStateEnforcedInPlatformCompat(str, userHandle)) {
            return true;
        }
        return set.contains(21) && !this.mConfigurationProvider.isDisplayInfoReadPhoneStateEnforcedInPlatformCompat(str, userHandle);
    }

    private boolean isPrecisePhoneStatePermissionRequired(Set<Integer> set) {
        Iterator<Integer> it = REQUIRE_PRECISE_PHONE_STATE_PERMISSION.iterator();
        while (it.hasNext()) {
            if (set.contains(it.next())) {
                return true;
            }
        }
        return false;
    }

    private boolean isActiveEmergencySessionPermissionRequired(Set<Integer> set) {
        return set.contains(29) || set.contains(30);
    }

    private boolean isPrivilegedPhoneStatePermissionRequired(Set<Integer> set) {
        return set.contains(16) || set.contains(18) || set.contains(24) || set.contains(35) || set.contains(40);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class TelephonyRegistryDeathRecipient implements IBinder.DeathRecipient {
        private final IBinder binder;

        TelephonyRegistryDeathRecipient(IBinder iBinder) {
            this.binder = iBinder;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            TelephonyRegistry.this.remove(this.binder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TelephonyManager getTelephonyManager() {
        return (TelephonyManager) this.mContext.getSystemService(HintsHelper.AUTOFILL_HINT_PHONE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMultiSimConfigChanged() {
        synchronized (this.mRecords) {
            int i = this.mNumPhones;
            int activeModemCount = getTelephonyManager().getActiveModemCount();
            this.mNumPhones = activeModemCount;
            if (i == activeModemCount) {
                return;
            }
            int[] copyOf = Arrays.copyOf(this.mCallState, activeModemCount);
            this.mCallState = copyOf;
            this.mDataActivity = Arrays.copyOf(copyOf, this.mNumPhones);
            this.mDataConnectionState = Arrays.copyOf(this.mCallState, this.mNumPhones);
            this.mDataConnectionNetworkType = Arrays.copyOf(this.mCallState, this.mNumPhones);
            this.mCallIncomingNumber = (String[]) Arrays.copyOf(this.mCallIncomingNumber, this.mNumPhones);
            this.mServiceState = (ServiceState[]) Arrays.copyOf(this.mServiceState, this.mNumPhones);
            this.mVoiceActivationState = Arrays.copyOf(this.mVoiceActivationState, this.mNumPhones);
            this.mDataActivationState = Arrays.copyOf(this.mDataActivationState, this.mNumPhones);
            this.mUserMobileDataState = Arrays.copyOf(this.mUserMobileDataState, this.mNumPhones);
            SignalStrength[] signalStrengthArr = this.mSignalStrength;
            if (signalStrengthArr != null) {
                this.mSignalStrength = (SignalStrength[]) Arrays.copyOf(signalStrengthArr, this.mNumPhones);
            } else {
                this.mSignalStrength = new SignalStrength[this.mNumPhones];
            }
            this.mMessageWaiting = Arrays.copyOf(this.mMessageWaiting, this.mNumPhones);
            this.mCallForwarding = Arrays.copyOf(this.mCallForwarding, this.mNumPhones);
            this.mCellIdentity = (CellIdentity[]) Arrays.copyOf(this.mCellIdentity, this.mNumPhones);
            this.mSrvccState = Arrays.copyOf(this.mSrvccState, this.mNumPhones);
            this.mPreciseCallState = (PreciseCallState[]) Arrays.copyOf(this.mPreciseCallState, this.mNumPhones);
            this.mForegroundCallState = Arrays.copyOf(this.mForegroundCallState, this.mNumPhones);
            this.mBackgroundCallState = Arrays.copyOf(this.mBackgroundCallState, this.mNumPhones);
            this.mRingingCallState = Arrays.copyOf(this.mRingingCallState, this.mNumPhones);
            this.mCallDisconnectCause = Arrays.copyOf(this.mCallDisconnectCause, this.mNumPhones);
            this.mCallPreciseDisconnectCause = Arrays.copyOf(this.mCallPreciseDisconnectCause, this.mNumPhones);
            this.mCallQuality = (CallQuality[]) Arrays.copyOf(this.mCallQuality, this.mNumPhones);
            this.mCallNetworkType = Arrays.copyOf(this.mCallNetworkType, this.mNumPhones);
            this.mOutgoingCallEmergencyNumber = (EmergencyNumber[]) Arrays.copyOf(this.mOutgoingCallEmergencyNumber, this.mNumPhones);
            this.mOutgoingSmsEmergencyNumber = (EmergencyNumber[]) Arrays.copyOf(this.mOutgoingSmsEmergencyNumber, this.mNumPhones);
            this.mTelephonyDisplayInfos = (TelephonyDisplayInfo[]) Arrays.copyOf(this.mTelephonyDisplayInfos, this.mNumPhones);
            this.mCarrierNetworkChangeState = Arrays.copyOf(this.mCarrierNetworkChangeState, this.mNumPhones);
            this.mIsDataEnabled = Arrays.copyOf(this.mIsDataEnabled, this.mNumPhones);
            this.mDataEnabledReason = Arrays.copyOf(this.mDataEnabledReason, this.mNumPhones);
            this.mAllowedNetworkTypeReason = Arrays.copyOf(this.mAllowedNetworkTypeReason, this.mNumPhones);
            this.mAllowedNetworkTypeValue = Arrays.copyOf(this.mAllowedNetworkTypeValue, this.mNumPhones);
            this.mECBMReason = Arrays.copyOf(this.mECBMReason, this.mNumPhones);
            this.mECBMStarted = Arrays.copyOf(this.mECBMStarted, this.mNumPhones);
            this.mSCBMReason = Arrays.copyOf(this.mSCBMReason, this.mNumPhones);
            this.mSCBMStarted = Arrays.copyOf(this.mSCBMStarted, this.mNumPhones);
            int i2 = this.mNumPhones;
            if (i2 < i) {
                cutListToSize(this.mCellInfo, i2);
                cutListToSize(this.mImsReasonInfo, this.mNumPhones);
                cutListToSize(this.mPreciseDataConnectionStates, this.mNumPhones);
                cutListToSize(this.mBarringInfo, this.mNumPhones);
                cutListToSize(this.mPhysicalChannelConfigs, this.mNumPhones);
                cutListToSize(this.mLinkCapacityEstimateLists, this.mNumPhones);
                cutListToSize(this.mCarrierPrivilegeStates, this.mNumPhones);
                cutListToSize(this.mCarrierServiceStates, this.mNumPhones);
                cutListToSize(this.mCallStateLists, this.mNumPhones);
                cutListToSize(this.mMediaQualityStatus, this.mNumPhones);
                return;
            }
            while (i < this.mNumPhones) {
                this.mCallState[i] = 0;
                this.mDataActivity[i] = 0;
                this.mDataConnectionState[i] = -1;
                this.mVoiceActivationState[i] = 0;
                this.mDataActivationState[i] = 0;
                this.mCallIncomingNumber[i] = "";
                this.mServiceState[i] = new ServiceState();
                this.mSignalStrength[i] = null;
                this.mUserMobileDataState[i] = false;
                this.mMessageWaiting[i] = false;
                this.mCallForwarding[i] = false;
                this.mCellIdentity[i] = null;
                this.mCellInfo.add(i, Collections.EMPTY_LIST);
                this.mImsReasonInfo.add(i, null);
                this.mSrvccState[i] = -1;
                this.mCallDisconnectCause[i] = -1;
                this.mCallPreciseDisconnectCause[i] = -1;
                this.mCallQuality[i] = createCallQuality();
                this.mMediaQualityStatus.add(i, new SparseArray<>());
                this.mCallStateLists.add(i, new ArrayList());
                this.mCallNetworkType[i] = 0;
                this.mPreciseCallState[i] = createPreciseCallState();
                this.mRingingCallState[i] = 0;
                this.mForegroundCallState[i] = 0;
                this.mBackgroundCallState[i] = 0;
                this.mPreciseDataConnectionStates.add(new ArrayMap());
                this.mBarringInfo.add(i, new BarringInfo());
                this.mCarrierNetworkChangeState[i] = false;
                this.mTelephonyDisplayInfos[i] = null;
                this.mIsDataEnabled[i] = false;
                this.mDataEnabledReason[i] = 0;
                this.mPhysicalChannelConfigs.add(i, new ArrayList());
                this.mAllowedNetworkTypeReason[i] = -1;
                this.mAllowedNetworkTypeValue[i] = -1;
                this.mLinkCapacityEstimateLists.add(i, INVALID_LCE_LIST);
                this.mCarrierPrivilegeStates.add(i, new Pair<>(Collections.emptyList(), new int[0]));
                this.mCarrierServiceStates.add(i, new Pair<>(null, -1));
                this.mECBMReason[i] = 0;
                this.mECBMStarted[i] = false;
                this.mSCBMReason[i] = 0;
                this.mSCBMStarted[i] = false;
                i++;
            }
        }
    }

    private void cutListToSize(List list, int i) {
        if (list == null) {
            return;
        }
        while (list.size() > i) {
            list.remove(list.size() - 1);
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public TelephonyRegistry(Context context, ConfigurationProvider configurationProvider) {
        this.mImsReasonInfo = null;
        this.mBarringInfo = null;
        this.mCarrierNetworkChangeState = null;
        this.mContext = context;
        this.mConfigurationProvider = configurationProvider;
        int activeModemCount = getTelephonyManager().getActiveModemCount();
        this.mNumPhones = activeModemCount;
        this.mCallState = new int[activeModemCount];
        this.mDataActivity = new int[activeModemCount];
        this.mDataConnectionState = new int[activeModemCount];
        this.mDataConnectionNetworkType = new int[activeModemCount];
        this.mCallIncomingNumber = new String[activeModemCount];
        this.mServiceState = new ServiceState[activeModemCount];
        this.mVoiceActivationState = new int[activeModemCount];
        this.mDataActivationState = new int[activeModemCount];
        this.mUserMobileDataState = new boolean[activeModemCount];
        this.mSignalStrength = new SignalStrength[activeModemCount];
        this.mMessageWaiting = new boolean[activeModemCount];
        this.mCallForwarding = new boolean[activeModemCount];
        this.mCellIdentity = new CellIdentity[activeModemCount];
        this.mSrvccState = new int[activeModemCount];
        this.mPreciseCallState = new PreciseCallState[activeModemCount];
        this.mForegroundCallState = new int[activeModemCount];
        this.mBackgroundCallState = new int[activeModemCount];
        this.mRingingCallState = new int[activeModemCount];
        this.mCallDisconnectCause = new int[activeModemCount];
        this.mCallPreciseDisconnectCause = new int[activeModemCount];
        this.mCallQuality = new CallQuality[activeModemCount];
        this.mMediaQualityStatus = new ArrayList();
        this.mCallNetworkType = new int[activeModemCount];
        this.mCallStateLists = new ArrayList<>();
        this.mPreciseDataConnectionStates = new ArrayList();
        this.mCellInfo = new ArrayList<>(activeModemCount);
        this.mImsReasonInfo = new ArrayList();
        this.mEmergencyNumberList = new HashMap();
        this.mOutgoingCallEmergencyNumber = new EmergencyNumber[activeModemCount];
        this.mOutgoingSmsEmergencyNumber = new EmergencyNumber[activeModemCount];
        this.mBarringInfo = new ArrayList();
        this.mCarrierNetworkChangeState = new boolean[activeModemCount];
        this.mTelephonyDisplayInfos = new TelephonyDisplayInfo[activeModemCount];
        this.mPhysicalChannelConfigs = new ArrayList();
        this.mAllowedNetworkTypeReason = new int[activeModemCount];
        this.mAllowedNetworkTypeValue = new long[activeModemCount];
        this.mIsDataEnabled = new boolean[activeModemCount];
        this.mDataEnabledReason = new int[activeModemCount];
        this.mLinkCapacityEstimateLists = new ArrayList();
        this.mCarrierPrivilegeStates = new ArrayList();
        this.mCarrierServiceStates = new ArrayList();
        this.mECBMReason = new int[activeModemCount];
        this.mECBMStarted = new boolean[activeModemCount];
        this.mSCBMReason = new int[activeModemCount];
        this.mSCBMStarted = new boolean[activeModemCount];
        for (int i = 0; i < activeModemCount; i++) {
            this.mCallState[i] = 0;
            this.mDataActivity[i] = 0;
            this.mDataConnectionState[i] = -1;
            this.mVoiceActivationState[i] = 0;
            this.mDataActivationState[i] = 0;
            this.mCallIncomingNumber[i] = "";
            this.mServiceState[i] = new ServiceState();
            this.mSignalStrength[i] = null;
            this.mUserMobileDataState[i] = false;
            this.mMessageWaiting[i] = false;
            this.mCallForwarding[i] = false;
            this.mCellIdentity[i] = null;
            this.mCellInfo.add(i, Collections.EMPTY_LIST);
            this.mImsReasonInfo.add(i, new ImsReasonInfo());
            this.mSrvccState[i] = -1;
            this.mCallDisconnectCause[i] = -1;
            this.mCallPreciseDisconnectCause[i] = -1;
            this.mCallQuality[i] = createCallQuality();
            this.mMediaQualityStatus.add(i, new SparseArray<>());
            this.mCallStateLists.add(i, new ArrayList());
            this.mCallNetworkType[i] = 0;
            this.mPreciseCallState[i] = createPreciseCallState();
            this.mRingingCallState[i] = 0;
            this.mForegroundCallState[i] = 0;
            this.mBackgroundCallState[i] = 0;
            this.mPreciseDataConnectionStates.add(new ArrayMap());
            this.mBarringInfo.add(i, new BarringInfo());
            this.mCarrierNetworkChangeState[i] = false;
            this.mTelephonyDisplayInfos[i] = null;
            this.mIsDataEnabled[i] = false;
            this.mDataEnabledReason[i] = 0;
            this.mPhysicalChannelConfigs.add(i, new ArrayList());
            this.mAllowedNetworkTypeReason[i] = -1;
            this.mAllowedNetworkTypeValue[i] = -1;
            this.mLinkCapacityEstimateLists.add(i, INVALID_LCE_LIST);
            this.mCarrierPrivilegeStates.add(i, new Pair<>(Collections.emptyList(), new int[0]));
            this.mCarrierServiceStates.add(i, new Pair<>(null, -1));
            this.mECBMReason[i] = 0;
            this.mECBMStarted[i] = false;
            this.mSCBMReason[i] = 0;
            this.mSCBMStarted[i] = false;
        }
        this.mAppOps = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
    }

    public void systemRunning() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.USER_REMOVED");
        intentFilter.addAction("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.telephony.action.MULTI_SIM_CONFIG_CHANGED");
        log("systemRunning register for intents");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    public void addOnSubscriptionsChangedListener(String str, String str2, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) {
        UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        synchronized (this.mRecords) {
            Record add = add(iOnSubscriptionsChangedListener.asBinder(), Binder.getCallingUid(), Binder.getCallingPid(), doesLimitApplyForListeners(Binder.getCallingUid(), Process.myUid()));
            if (add == null) {
                return;
            }
            add.context = this.mContext;
            add.onSubscriptionsChangedListenerCallback = iOnSubscriptionsChangedListener;
            add.callingPackage = str;
            add.callingFeatureId = str2;
            add.callerUid = Binder.getCallingUid();
            add.callerPid = Binder.getCallingPid();
            add.eventList = new ArraySet();
            if (this.mHasNotifySubscriptionInfoChangedOccurred) {
                try {
                    add.onSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                } catch (RemoteException unused) {
                    remove(add.binder);
                }
            } else {
                log("listen oscl: mHasNotifySubscriptionInfoChangedOccurred==false no callback");
            }
        }
    }

    public void removeOnSubscriptionsChangedListener(String str, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) {
        remove(iOnSubscriptionsChangedListener.asBinder());
    }

    public void addOnOpportunisticSubscriptionsChangedListener(String str, String str2, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) {
        UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        synchronized (this.mRecords) {
            Record add = add(iOnSubscriptionsChangedListener.asBinder(), Binder.getCallingUid(), Binder.getCallingPid(), doesLimitApplyForListeners(Binder.getCallingUid(), Process.myUid()));
            if (add == null) {
                return;
            }
            add.context = this.mContext;
            add.onOpportunisticSubscriptionsChangedListenerCallback = iOnSubscriptionsChangedListener;
            add.callingPackage = str;
            add.callingFeatureId = str2;
            add.callerUid = Binder.getCallingUid();
            add.callerPid = Binder.getCallingPid();
            add.eventList = new ArraySet();
            if (this.mHasNotifyOpportunisticSubscriptionInfoChangedOccurred) {
                try {
                    add.onOpportunisticSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                } catch (RemoteException unused) {
                    remove(add.binder);
                }
            } else {
                log("listen ooscl: hasNotifyOpptSubInfoChangedOccurred==false no callback");
            }
        }
    }

    public void notifySubscriptionInfoChanged() {
        if (checkNotifyPermission("notifySubscriptionInfoChanged()")) {
            synchronized (this.mRecords) {
                if (!this.mHasNotifySubscriptionInfoChangedOccurred) {
                    log("notifySubscriptionInfoChanged: first invocation mRecords.size=" + this.mRecords.size());
                }
                this.mHasNotifySubscriptionInfoChangedOccurred = true;
                this.mRemoveList.clear();
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchOnSubscriptionsChangedListener()) {
                        try {
                            next.onSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyOpportunisticSubscriptionInfoChanged() {
        if (checkNotifyPermission("notifyOpportunisticSubscriptionInfoChanged()")) {
            synchronized (this.mRecords) {
                if (!this.mHasNotifyOpportunisticSubscriptionInfoChangedOccurred) {
                    log("notifyOpptSubscriptionInfoChanged: first invocation mRecords.size=" + this.mRecords.size());
                }
                this.mHasNotifyOpportunisticSubscriptionInfoChangedOccurred = true;
                this.mRemoveList.clear();
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchOnOpportunisticSubscriptionsChangedListener()) {
                        try {
                            next.onOpportunisticSubscriptionsChangedListenerCallback.onSubscriptionsChanged();
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void listenWithEventList(boolean z, boolean z2, int i, String str, String str2, IPhoneStateListener iPhoneStateListener, int[] iArr, boolean z3) {
        listen(z, z2, str, str2, iPhoneStateListener, (Set) Arrays.stream(iArr).boxed().collect(Collectors.toSet()), z3, i);
    }

    private void listen(boolean z, boolean z2, String str, String str2, IPhoneStateListener iPhoneStateListener, Set<Integer> set, boolean z3, int i) {
        List<PhysicalChannelConfig> list;
        CallState callState;
        BarringInfo barringInfo;
        ImsReasonInfo imsReasonInfo;
        int callingUserId = UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        this.mListenLog.log("listen: E pkg=" + pii(str) + " uid=" + Binder.getCallingUid() + " events=" + set + " notifyNow=" + z3 + " subId=" + i + " myUserId=" + UserHandle.myUserId() + " callerUserId=" + callingUserId);
        if (set.isEmpty()) {
            set.clear();
            remove(iPhoneStateListener.asBinder());
            return;
        }
        if (checkListenerPermission(set, i, str, str2, "listen")) {
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            synchronized (this.mRecords) {
                Record add = add(iPhoneStateListener.asBinder(), Binder.getCallingUid(), Binder.getCallingPid(), doesLimitApplyForListeners(Binder.getCallingUid(), Process.myUid()));
                if (add == null) {
                    return;
                }
                add.context = this.mContext;
                add.callback = iPhoneStateListener;
                add.callingPackage = str;
                add.callingFeatureId = str2;
                add.renounceCoarseLocationAccess = z2;
                add.renounceFineLocationAccess = z;
                add.callerUid = Binder.getCallingUid();
                add.callerPid = Binder.getCallingPid();
                if (!SubscriptionManager.isValidSubscriptionId(i)) {
                    add.subId = Integer.MAX_VALUE;
                } else {
                    add.subId = i;
                }
                add.phoneId = phoneIdFromSubId;
                add.eventList = set;
                if (z3 && validatePhoneId(phoneIdFromSubId)) {
                    if (set.contains(1)) {
                        try {
                            ServiceState serviceState = new ServiceState(this.mServiceState[add.phoneId]);
                            if (checkFineLocationAccess(add, 29)) {
                                add.callback.onServiceStateChanged(serviceState);
                            } else if (checkCoarseLocationAccess(add, 29)) {
                                add.callback.onServiceStateChanged(serviceState.createLocationInfoSanitizedCopy(false));
                            } else {
                                add.callback.onServiceStateChanged(serviceState.createLocationInfoSanitizedCopy(true));
                            }
                        } catch (RemoteException unused) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(2)) {
                        try {
                            SignalStrength signalStrength = this.mSignalStrength[add.phoneId];
                            if (signalStrength != null) {
                                int gsmSignalStrength = signalStrength.getGsmSignalStrength();
                                IPhoneStateListener iPhoneStateListener2 = add.callback;
                                if (gsmSignalStrength == 99) {
                                    gsmSignalStrength = -1;
                                }
                                iPhoneStateListener2.onSignalStrengthChanged(gsmSignalStrength);
                            }
                        } catch (RemoteException unused2) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(3)) {
                        try {
                            add.callback.onMessageWaitingIndicatorChanged(this.mMessageWaiting[add.phoneId]);
                        } catch (RemoteException unused3) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(4)) {
                        try {
                            add.callback.onCallForwardingIndicatorChanged(this.mCallForwarding[add.phoneId]);
                        } catch (RemoteException unused4) {
                            remove(add.binder);
                        }
                    }
                    if (validateEventAndUserLocked(add, 5)) {
                        try {
                            if (checkCoarseLocationAccess(add, 1) && checkFineLocationAccess(add, 29)) {
                                add.callback.onCellLocationChanged(this.mCellIdentity[add.phoneId]);
                            }
                        } catch (RemoteException unused5) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(36)) {
                        try {
                            IPhoneStateListener iPhoneStateListener3 = add.callback;
                            int[] iArr = this.mCallState;
                            int i2 = add.phoneId;
                            iPhoneStateListener3.onLegacyCallStateChanged(iArr[i2], getCallIncomingNumber(add, i2));
                        } catch (RemoteException unused6) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(6)) {
                        try {
                            add.callback.onCallStateChanged(this.mCallState[add.phoneId]);
                        } catch (RemoteException unused7) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(7)) {
                        try {
                            IPhoneStateListener iPhoneStateListener4 = add.callback;
                            int[] iArr2 = this.mDataConnectionState;
                            int i3 = add.phoneId;
                            iPhoneStateListener4.onDataConnectionStateChanged(iArr2[i3], this.mDataConnectionNetworkType[i3]);
                        } catch (RemoteException unused8) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(8)) {
                        try {
                            add.callback.onDataActivity(this.mDataActivity[add.phoneId]);
                        } catch (RemoteException unused9) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(9)) {
                        try {
                            SignalStrength signalStrength2 = this.mSignalStrength[add.phoneId];
                            if (signalStrength2 != null) {
                                add.callback.onSignalStrengthsChanged(signalStrength2);
                            }
                        } catch (RemoteException unused10) {
                            remove(add.binder);
                        }
                    }
                    if (validateEventAndUserLocked(add, 11)) {
                        try {
                            if (checkCoarseLocationAccess(add, 1) && checkFineLocationAccess(add, 29)) {
                                add.callback.onCellInfoChanged(this.mCellInfo.get(add.phoneId));
                            }
                        } catch (RemoteException unused11) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(12)) {
                        try {
                            add.callback.onPreciseCallStateChanged(this.mPreciseCallState[add.phoneId]);
                        } catch (RemoteException unused12) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(26)) {
                        try {
                            IPhoneStateListener iPhoneStateListener5 = add.callback;
                            int[] iArr3 = this.mCallDisconnectCause;
                            int i4 = add.phoneId;
                            iPhoneStateListener5.onCallDisconnectCauseChanged(iArr3[i4], this.mCallPreciseDisconnectCause[i4]);
                        } catch (RemoteException unused13) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(28) && (imsReasonInfo = this.mImsReasonInfo.get(add.phoneId)) != null) {
                        try {
                            add.callback.onImsCallDisconnectCauseChanged(imsReasonInfo);
                        } catch (RemoteException unused14) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(13)) {
                        try {
                            Iterator<PreciseDataConnectionState> it = this.mPreciseDataConnectionStates.get(add.phoneId).values().iterator();
                            while (it.hasNext()) {
                                add.callback.onPreciseDataConnectionStateChanged(it.next());
                            }
                        } catch (RemoteException unused15) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(17)) {
                        try {
                            add.callback.onCarrierNetworkChange(this.mCarrierNetworkChangeState[add.phoneId]);
                        } catch (RemoteException unused16) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(18)) {
                        try {
                            add.callback.onVoiceActivationStateChanged(this.mVoiceActivationState[add.phoneId]);
                        } catch (RemoteException unused17) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(19)) {
                        try {
                            add.callback.onDataActivationStateChanged(this.mDataActivationState[add.phoneId]);
                        } catch (RemoteException unused18) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(20)) {
                        try {
                            add.callback.onUserMobileDataStateChanged(this.mUserMobileDataState[add.phoneId]);
                        } catch (RemoteException unused19) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(21)) {
                        try {
                            TelephonyDisplayInfo telephonyDisplayInfo = this.mTelephonyDisplayInfos[add.phoneId];
                            if (telephonyDisplayInfo != null) {
                                add.callback.onDisplayInfoChanged(telephonyDisplayInfo);
                            }
                        } catch (RemoteException unused20) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(25)) {
                        try {
                            add.callback.onEmergencyNumberListChanged(this.mEmergencyNumberList);
                        } catch (RemoteException unused21) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(22)) {
                        try {
                            add.callback.onPhoneCapabilityChanged(this.mPhoneCapability);
                        } catch (RemoteException unused22) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(23)) {
                        try {
                            add.callback.onActiveDataSubIdChanged(this.mActiveDataSubId);
                        } catch (RemoteException unused23) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(24)) {
                        try {
                            add.callback.onRadioPowerStateChanged(this.mRadioPowerState);
                        } catch (RemoteException unused24) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(16)) {
                        try {
                            add.callback.onSrvccStateChanged(this.mSrvccState[add.phoneId]);
                        } catch (RemoteException unused25) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(27)) {
                        try {
                            add.callback.onCallStatesChanged(this.mCallStateLists.get(add.phoneId));
                        } catch (RemoteException unused26) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(32) && (barringInfo = this.mBarringInfo.get(add.phoneId)) != null) {
                        BarringInfo createLocationInfoSanitizedCopy = barringInfo.createLocationInfoSanitizedCopy();
                        try {
                            IPhoneStateListener iPhoneStateListener6 = add.callback;
                            if (!checkFineLocationAccess(add, 1)) {
                                barringInfo = createLocationInfoSanitizedCopy;
                            }
                            iPhoneStateListener6.onBarringInfoChanged(barringInfo);
                        } catch (RemoteException unused27) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(33)) {
                        try {
                            IPhoneStateListener iPhoneStateListener7 = add.callback;
                            if (shouldSanitizeLocationForPhysicalChannelConfig(add)) {
                                list = getLocationSanitizedConfigs(this.mPhysicalChannelConfigs.get(add.phoneId));
                            } else {
                                list = this.mPhysicalChannelConfigs.get(add.phoneId);
                            }
                            iPhoneStateListener7.onPhysicalChannelConfigChanged(list);
                        } catch (RemoteException unused28) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(34)) {
                        try {
                            IPhoneStateListener iPhoneStateListener8 = add.callback;
                            boolean[] zArr = this.mIsDataEnabled;
                            int i5 = add.phoneId;
                            iPhoneStateListener8.onDataEnabledChanged(zArr[i5], this.mDataEnabledReason[i5]);
                        } catch (RemoteException unused29) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(37)) {
                        try {
                            if (this.mLinkCapacityEstimateLists.get(add.phoneId) != null) {
                                add.callback.onLinkCapacityEstimateChanged(this.mLinkCapacityEstimateLists.get(add.phoneId));
                            }
                        } catch (RemoteException unused30) {
                            remove(add.binder);
                        }
                    }
                    if (set.contains(39)) {
                        Iterator<CallState> it2 = this.mCallStateLists.get(add.phoneId).iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                callState = null;
                                break;
                            } else {
                                callState = it2.next();
                                if (callState.getCallState() == 1) {
                                    break;
                                }
                            }
                        }
                        if (callState != null) {
                            String imsCallSessionId = callState.getImsCallSessionId();
                            try {
                                MediaQualityStatus mediaQualityStatus = this.mMediaQualityStatus.get(add.phoneId).get(1);
                                if (mediaQualityStatus != null && mediaQualityStatus.getCallSessionId().equals(imsCallSessionId)) {
                                    add.callback.onMediaQualityStatusChanged(mediaQualityStatus);
                                }
                                MediaQualityStatus mediaQualityStatus2 = this.mMediaQualityStatus.get(add.phoneId).get(2);
                                if (mediaQualityStatus2 != null && mediaQualityStatus2.getCallSessionId().equals(imsCallSessionId)) {
                                    add.callback.onMediaQualityStatusChanged(mediaQualityStatus2);
                                }
                            } catch (RemoteException unused31) {
                                remove(add.binder);
                            }
                        }
                    }
                    if (set.contains(40)) {
                        try {
                            boolean[] zArr2 = this.mECBMStarted;
                            int i6 = add.phoneId;
                            if (zArr2[i6]) {
                                add.callback.onCallBackModeStarted(1);
                            } else {
                                add.callback.onCallBackModeStopped(1, this.mECBMReason[i6]);
                            }
                            boolean[] zArr3 = this.mSCBMStarted;
                            int i7 = add.phoneId;
                            if (zArr3[i7]) {
                                add.callback.onCallBackModeStarted(2);
                            } else {
                                add.callback.onCallBackModeStopped(2, this.mSCBMReason[i7]);
                            }
                        } catch (RemoteException unused32) {
                            remove(add.binder);
                        }
                    }
                }
            }
        }
    }

    private String getCallIncomingNumber(Record record, int i) {
        return record.canReadCallLog() ? this.mCallIncomingNumber[i] : "";
    }

    private Record add(IBinder iBinder, int i, int i2, boolean z) {
        synchronized (this.mRecords) {
            int size = this.mRecords.size();
            int i3 = 0;
            for (int i4 = 0; i4 < size; i4++) {
                Record record = this.mRecords.get(i4);
                if (iBinder == record.binder) {
                    return record;
                }
                if (record.callerPid == i2) {
                    i3++;
                }
            }
            int registrationLimit = this.mConfigurationProvider.getRegistrationLimit();
            if (z && registrationLimit >= 1 && i3 >= registrationLimit) {
                String str = "Pid " + i2 + " has exceeded the number of permissible registered listeners. Ignoring request to add.";
                loge(str);
                if (this.mConfigurationProvider.isRegistrationLimitEnabledInPlatformCompat(i)) {
                    throw new IllegalStateException(str);
                }
            } else if (i3 >= 25) {
                Rlog.w(TAG, "Pid " + i2 + " has exceeded half the number of permissible registered listeners. Now at " + i3);
            }
            Record record2 = new Record();
            record2.binder = iBinder;
            TelephonyRegistryDeathRecipient telephonyRegistryDeathRecipient = new TelephonyRegistryDeathRecipient(iBinder);
            record2.deathRecipient = telephonyRegistryDeathRecipient;
            try {
                iBinder.linkToDeath(telephonyRegistryDeathRecipient, 0);
                this.mRecords.add(record2);
                this.mTelephonyRegistryExt.addProxyBinder(iBinder, i, i2);
                return record2;
            } catch (RemoteException unused) {
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void remove(IBinder iBinder) {
        synchronized (this.mRecords) {
            int size = this.mRecords.size();
            for (int i = 0; i < size; i++) {
                Record record = this.mRecords.get(i);
                if (record.binder == iBinder) {
                    TelephonyRegistryDeathRecipient telephonyRegistryDeathRecipient = record.deathRecipient;
                    if (telephonyRegistryDeathRecipient != null) {
                        try {
                            iBinder.unlinkToDeath(telephonyRegistryDeathRecipient, 0);
                        } catch (NoSuchElementException unused) {
                        }
                    }
                    this.mRecords.remove(i);
                    this.mTelephonyRegistryExt.removeProxyBinder(iBinder, record.callerUid);
                    return;
                }
            }
        }
    }

    public void notifyCallStateForAllSubs(int i, String str) {
        if (checkNotifyPermission("notifyCallState()")) {
            synchronized (this.mRecords) {
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchTelephonyCallbackEvent(36) && next.subId == Integer.MAX_VALUE) {
                        try {
                            next.callback.onLegacyCallStateChanged(i, next.canReadCallLog() ? str : "");
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                    if (next.matchTelephonyCallbackEvent(6) && next.subId == Integer.MAX_VALUE) {
                        try {
                            next.callback.onCallStateChanged(i);
                        } catch (RemoteException unused2) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
            broadcastCallStateChanged(i, str, -1, -1);
        }
    }

    public void notifyCallState(int i, int i2, int i3, String str) {
        int i4;
        int i5;
        if (checkNotifyPermission("notifyCallState()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mCallState[i] = i3;
                    this.mCallIncomingNumber[i] = str;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(36) && (i5 = next.subId) == i2 && i5 != Integer.MAX_VALUE) {
                            try {
                                next.callback.onLegacyCallStateChanged(i3, getCallIncomingNumber(next, i));
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                        if (next.matchTelephonyCallbackEvent(6) && (i4 = next.subId) == i2 && i4 != Integer.MAX_VALUE) {
                            try {
                                next.callback.onCallStateChanged(i3);
                            } catch (RemoteException unused2) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
            broadcastCallStateChanged(i3, str, i, i2);
        }
    }

    public void notifyServiceStateForPhoneId(int i, int i2, ServiceState serviceState) {
        ServiceState createLocationInfoSanitizedCopy;
        if (checkNotifyPermission("notifyServiceState()")) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (this.mRecords) {
                    this.mLocalLog.log("notifyServiceStateForSubscriber: subId=" + i2 + " phoneId=" + i + " state=" + serviceState);
                    if (validatePhoneId(i) && SubscriptionManager.isValidSubscriptionId(i2)) {
                        this.mServiceState[i] = serviceState;
                        Iterator<Record> it = this.mRecords.iterator();
                        while (it.hasNext()) {
                            Record next = it.next();
                            if (next.matchTelephonyCallbackEvent(1) && idMatch(next, i2, i)) {
                                try {
                                    if (checkFineLocationAccess(next, 29)) {
                                        createLocationInfoSanitizedCopy = new ServiceState(serviceState);
                                    } else if (checkCoarseLocationAccess(next, 29)) {
                                        createLocationInfoSanitizedCopy = serviceState.createLocationInfoSanitizedCopy(false);
                                    } else {
                                        createLocationInfoSanitizedCopy = serviceState.createLocationInfoSanitizedCopy(true);
                                    }
                                    next.callback.onServiceStateChanged(createLocationInfoSanitizedCopy);
                                } catch (RemoteException unused) {
                                    this.mRemoveList.add(next.binder);
                                }
                            }
                        }
                    } else {
                        log("notifyServiceStateForSubscriber: INVALID phoneId=" + i + " or subId=" + i2);
                    }
                    handleRemoveListLocked();
                }
                broadcastServiceStateChanged(serviceState, i, i2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }

    public void notifySimActivationStateChangedForPhoneId(int i, int i2, int i3, int i4) {
        if (checkNotifyPermission("notifySimActivationState()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    if (i3 == 0) {
                        this.mVoiceActivationState[i] = i4;
                    } else if (i3 != 1) {
                        return;
                    } else {
                        this.mDataActivationState[i] = i4;
                    }
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (i3 == 0) {
                            try {
                                if (next.matchTelephonyCallbackEvent(18) && idMatch(next, i2, i)) {
                                    next.callback.onVoiceActivationStateChanged(i4);
                                }
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                        if (i3 == 1 && next.matchTelephonyCallbackEvent(19) && idMatch(next, i2, i)) {
                            next.callback.onDataActivationStateChanged(i4);
                        }
                    }
                } else {
                    log("notifySimActivationStateForPhoneId: INVALID phoneId=" + i);
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifySignalStrengthForPhoneId(int i, int i2, SignalStrength signalStrength) {
        if (checkNotifyPermission("notifySignalStrength()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mSignalStrength[i] = signalStrength;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(9) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onSignalStrengthsChanged(new SignalStrength(signalStrength));
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                        if (next.matchTelephonyCallbackEvent(2) && idMatch(next, i2, i)) {
                            try {
                                int gsmSignalStrength = signalStrength.getGsmSignalStrength();
                                if (gsmSignalStrength == 99) {
                                    gsmSignalStrength = -1;
                                }
                                next.callback.onSignalStrengthChanged(gsmSignalStrength);
                            } catch (RemoteException unused2) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                } else {
                    log("notifySignalStrengthForPhoneId: invalid phoneId=" + i);
                }
                handleRemoveListLocked();
            }
            broadcastSignalStrengthChanged(signalStrength, i, i2);
        }
    }

    public void notifyCarrierNetworkChange(boolean z) {
        int[] array = Arrays.stream(SubscriptionManager.from(this.mContext).getCompleteActiveSubscriptionIdList()).filter(new IntPredicate() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda3
            @Override // java.util.function.IntPredicate
            public final boolean test(int i) {
                boolean lambda$notifyCarrierNetworkChange$0;
                lambda$notifyCarrierNetworkChange$0 = TelephonyRegistry.this.lambda$notifyCarrierNetworkChange$0(i);
                return lambda$notifyCarrierNetworkChange$0;
            }
        }).toArray();
        if (ArrayUtils.isEmpty(array)) {
            loge("notifyCarrierNetworkChange without carrier privilege");
            throw new SecurityException("notifyCarrierNetworkChange without carrier privilege");
        }
        for (int i : array) {
            notifyCarrierNetworkChangeWithPermission(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$notifyCarrierNetworkChange$0(int i) {
        return TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, i);
    }

    public void notifyCarrierNetworkChangeWithSubId(int i, boolean z) {
        if (!TelephonyPermissions.checkCarrierPrivilegeForSubId(this.mContext, i)) {
            throw new SecurityException("notifyCarrierNetworkChange without carrier privilege on subId " + i);
        }
        notifyCarrierNetworkChangeWithPermission(i, z);
    }

    private void notifyCarrierNetworkChangeWithPermission(int i, boolean z) {
        synchronized (this.mRecords) {
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            this.mCarrierNetworkChangeState[phoneIdFromSubId] = z;
            Iterator<Record> it = this.mRecords.iterator();
            while (it.hasNext()) {
                Record next = it.next();
                if (next.matchTelephonyCallbackEvent(17) && idMatch(next, i, phoneIdFromSubId)) {
                    try {
                        next.callback.onCarrierNetworkChange(z);
                    } catch (RemoteException unused) {
                        this.mRemoveList.add(next.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyCellInfo(List<CellInfo> list) {
        notifyCellInfoForSubscriber(Integer.MAX_VALUE, list);
    }

    public void notifyCellInfoForSubscriber(int i, List<CellInfo> list) {
        if (checkNotifyPermission("notifyCellInfoForSubscriber()")) {
            if (list == null) {
                loge("notifyCellInfoForSubscriber() received a null list");
                list = Collections.EMPTY_LIST;
            }
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            synchronized (this.mRecords) {
                if (validatePhoneId(phoneIdFromSubId)) {
                    this.mCellInfo.set(phoneIdFromSubId, list);
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (validateEventAndUserLocked(next, 11) && idMatch(next, i, phoneIdFromSubId) && checkCoarseLocationAccess(next, 1) && checkFineLocationAccess(next, 29)) {
                            try {
                                next.callback.onCellInfoChanged(list);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyMessageWaitingChangedForPhoneId(int i, int i2, boolean z) {
        if (checkNotifyPermission("notifyMessageWaitingChanged()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mMessageWaiting[i] = z;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(3) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onMessageWaitingIndicatorChanged(z);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyUserMobileDataStateChangedForPhoneId(int i, int i2, boolean z) {
        if (checkNotifyPermission("notifyUserMobileDataStateChanged()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mUserMobileDataState[i] = z;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(20) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onUserMobileDataStateChanged(z);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyDisplayInfoChanged(int i, int i2, TelephonyDisplayInfo telephonyDisplayInfo) {
        if (checkNotifyPermission("notifyDisplayInfoChanged()")) {
            this.mLocalLog.log("notifyDisplayInfoChanged: PhoneId=" + i + " subId=" + i2 + " telephonyDisplayInfo=" + telephonyDisplayInfo);
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mTelephonyDisplayInfos[i] = telephonyDisplayInfo;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(21) && idMatch(next, i2, i)) {
                            try {
                                if (!this.mConfigurationProvider.isDisplayInfoNrAdvancedSupported(next.callingPackage, Binder.getCallingUserHandle())) {
                                    next.callback.onDisplayInfoChanged(getBackwardCompatibleTelephonyDisplayInfo(telephonyDisplayInfo));
                                } else {
                                    next.callback.onDisplayInfoChanged(telephonyDisplayInfo);
                                }
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    private TelephonyDisplayInfo getBackwardCompatibleTelephonyDisplayInfo(TelephonyDisplayInfo telephonyDisplayInfo) {
        int networkType = telephonyDisplayInfo.getNetworkType();
        int overrideNetworkType = telephonyDisplayInfo.getOverrideNetworkType();
        if (networkType == 20) {
            overrideNetworkType = 0;
        } else if (networkType == 13 && overrideNetworkType == 5) {
            overrideNetworkType = 4;
        }
        return new TelephonyDisplayInfo(networkType, overrideNetworkType, telephonyDisplayInfo.isRoaming());
    }

    public void notifyCallForwardingChanged(boolean z) {
        notifyCallForwardingChangedForSubscriber(Integer.MAX_VALUE, z);
    }

    public void notifyCallForwardingChangedForSubscriber(int i, boolean z) {
        if (checkNotifyPermission("notifyCallForwardingChanged()")) {
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            synchronized (this.mRecords) {
                if (validatePhoneId(phoneIdFromSubId)) {
                    this.mCallForwarding[phoneIdFromSubId] = z;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(4) && idMatch(next, i, phoneIdFromSubId)) {
                            try {
                                next.callback.onCallForwardingIndicatorChanged(z);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyDataActivity(int i) {
        notifyDataActivityForSubscriber(Integer.MAX_VALUE, i);
    }

    public void notifyDataActivityForSubscriber(int i, int i2) {
        if (checkNotifyPermission("notifyDataActivity()")) {
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            synchronized (this.mRecords) {
                if (validatePhoneId(phoneIdFromSubId)) {
                    this.mDataActivity[phoneIdFromSubId] = i2;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(8) && idMatch(next, i, phoneIdFromSubId)) {
                            try {
                                next.callback.onDataActivity(i2);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyDataConnectionForSubscriber(int i, int i2, PreciseDataConnectionState preciseDataConnectionState) {
        int i3;
        if (checkNotifyPermission("notifyDataConnection()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i) && preciseDataConnectionState.getApnSetting() != null) {
                    Pair<Integer, ApnSetting> create = Pair.create(Integer.valueOf(preciseDataConnectionState.getTransportType()), preciseDataConnectionState.getApnSetting());
                    if (!Objects.equals(this.mPreciseDataConnectionStates.get(i).remove(create), preciseDataConnectionState)) {
                        Iterator<Record> it = this.mRecords.iterator();
                        while (it.hasNext()) {
                            Record next = it.next();
                            if (next.matchTelephonyCallbackEvent(13) && idMatch(next, i2, i)) {
                                try {
                                    next.callback.onPreciseDataConnectionStateChanged(preciseDataConnectionState);
                                } catch (RemoteException unused) {
                                    this.mRemoveList.add(next.binder);
                                }
                            }
                        }
                        handleRemoveListLocked();
                        broadcastDataConnectionStateChanged(i, i2, preciseDataConnectionState);
                        String str = "notifyDataConnectionForSubscriber: phoneId=" + i + " subId=" + i2 + " " + preciseDataConnectionState;
                        log(str);
                        this.mLocalLog.log(str);
                    }
                    if (preciseDataConnectionState.getState() != 0) {
                        this.mPreciseDataConnectionStates.get(i).put(create, preciseDataConnectionState);
                    }
                    ArrayMap arrayMap = new ArrayMap();
                    int i4 = 0;
                    if (preciseDataConnectionState.getState() == 0 && preciseDataConnectionState.getApnSetting().getApnTypes().contains(17)) {
                        arrayMap.put(0, preciseDataConnectionState);
                    }
                    for (Map.Entry<Pair<Integer, ApnSetting>, PreciseDataConnectionState> entry : this.mPreciseDataConnectionStates.get(i).entrySet()) {
                        if (((Integer) entry.getKey().first).intValue() == 1 && ((ApnSetting) entry.getKey().second).getApnTypes().contains(17)) {
                            arrayMap.put(Integer.valueOf(entry.getValue().getState()), entry.getValue());
                        }
                    }
                    int[] iArr = {2, 3, 1, 4, 0};
                    int i5 = 0;
                    while (true) {
                        if (i5 >= 5) {
                            i3 = 0;
                            break;
                        }
                        int i6 = iArr[i5];
                        if (arrayMap.containsKey(Integer.valueOf(i6))) {
                            i3 = ((PreciseDataConnectionState) arrayMap.get(Integer.valueOf(i6))).getNetworkType();
                            i4 = i6;
                            break;
                        }
                        i5++;
                    }
                    if (this.mDataConnectionState[i] != i4 || this.mDataConnectionNetworkType[i] != i3) {
                        String str2 = "onDataConnectionStateChanged(" + TelephonyUtils.dataStateToString(i4) + ", " + TelephonyManager.getNetworkTypeName(i3) + ") subId=" + i2 + ", phoneId=" + i;
                        log(str2);
                        this.mLocalLog.log(str2);
                        Iterator<Record> it2 = this.mRecords.iterator();
                        while (it2.hasNext()) {
                            Record next2 = it2.next();
                            if (next2.matchTelephonyCallbackEvent(7) && idMatch(next2, i2, i)) {
                                try {
                                    next2.callback.onDataConnectionStateChanged(i4, i3);
                                } catch (RemoteException unused2) {
                                    this.mRemoveList.add(next2.binder);
                                }
                            }
                        }
                        this.mDataConnectionState[i] = i4;
                        this.mDataConnectionNetworkType[i] = i3;
                        handleRemoveListLocked();
                    }
                }
            }
        }
    }

    public void notifyCellLocationForSubscriber(int i, CellIdentity cellIdentity) {
        notifyCellLocationForSubscriber(i, cellIdentity, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCellLocationForSubscriber(int i, CellIdentity cellIdentity, boolean z) {
        log("notifyCellLocationForSubscriber: subId=" + i + " cellIdentity=" + Rlog.pii(false, cellIdentity));
        if (checkNotifyPermission("notifyCellLocation()")) {
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            synchronized (this.mRecords) {
                if (validatePhoneId(phoneIdFromSubId) && (z || !Objects.equals(cellIdentity, this.mCellIdentity[phoneIdFromSubId]))) {
                    this.mCellIdentity[phoneIdFromSubId] = cellIdentity;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (validateEventAndUserLocked(next, 5) && idMatch(next, i, phoneIdFromSubId) && checkCoarseLocationAccess(next, 1) && checkFineLocationAccess(next, 29)) {
                            try {
                                next.callback.onCellLocationChanged(cellIdentity);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyPreciseCallState(int i, int i2, int[] iArr, String[] strArr, int[] iArr2, int[] iArr3) {
        boolean z;
        if (checkNotifyPermission("notifyPreciseCallState()")) {
            boolean z2 = false;
            int i3 = iArr[0];
            int i4 = iArr[1];
            int i5 = iArr[2];
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mRingingCallState[i] = i3;
                    this.mForegroundCallState[i] = i4;
                    this.mBackgroundCallState[i] = i5;
                    PreciseCallState preciseCallState = new PreciseCallState(i3, i4, i5, -1, -1);
                    if (preciseCallState.equals(this.mPreciseCallState[i])) {
                        z = false;
                    } else {
                        this.mPreciseCallState[i] = preciseCallState;
                        z = true;
                    }
                    if (this.mCallQuality == null) {
                        log("notifyPreciseCallState: mCallQuality is null, skipping call attributes");
                    } else {
                        if (this.mPreciseCallState[i].getForegroundCallState() != 1) {
                            this.mCallNetworkType[i] = 0;
                            this.mCallQuality[i] = createCallQuality();
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(this.mCallStateLists.get(i));
                        this.mCallStateLists.get(i).clear();
                        if (i4 != -1 && i4 != 0) {
                            CallState.Builder callClassification = new CallState.Builder(iArr[1]).setNetworkType(this.mCallNetworkType[i]).setCallQuality(this.mCallQuality[i]).setCallClassification(1);
                            if (strArr != null && iArr2 != null && iArr3 != null) {
                                callClassification = callClassification.setImsCallSessionId(strArr[1]).setImsCallServiceType(iArr2[1]).setImsCallType(iArr3[1]);
                            }
                            this.mCallStateLists.get(i).add(callClassification.build());
                        }
                        if (i5 != -1 && i5 != 0) {
                            CallState.Builder callClassification2 = new CallState.Builder(iArr[2]).setNetworkType(this.mCallNetworkType[i]).setCallQuality(createCallQuality()).setCallClassification(2);
                            if (strArr != null && iArr2 != null && iArr3 != null) {
                                callClassification2 = callClassification2.setImsCallSessionId(strArr[2]).setImsCallServiceType(iArr2[2]).setImsCallType(iArr3[2]);
                            }
                            this.mCallStateLists.get(i).add(callClassification2.build());
                        }
                        if (i3 != -1 && i3 != 0) {
                            CallState.Builder callClassification3 = new CallState.Builder(iArr[0]).setNetworkType(this.mCallNetworkType[i]).setCallQuality(createCallQuality()).setCallClassification(0);
                            if (strArr != null && iArr2 != null && iArr3 != null) {
                                callClassification3 = callClassification3.setImsCallSessionId(strArr[0]).setImsCallServiceType(iArr2[0]).setImsCallType(iArr3[0]);
                            }
                            this.mCallStateLists.get(i).add(callClassification3.build());
                        }
                        boolean z3 = !arrayList.equals(this.mCallStateLists.get(i));
                        Iterator<CallState> it = this.mCallStateLists.get(i).iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            } else if (it.next().getCallState() != 7) {
                                z2 = true;
                                break;
                            }
                        }
                        if (!z2) {
                            this.mMediaQualityStatus.get(i).clear();
                        }
                        z2 = z3;
                    }
                    if (z) {
                        Iterator<Record> it2 = this.mRecords.iterator();
                        while (it2.hasNext()) {
                            Record next = it2.next();
                            if (next.matchTelephonyCallbackEvent(12) && idMatch(next, i2, i)) {
                                try {
                                    next.callback.onPreciseCallStateChanged(this.mPreciseCallState[i]);
                                } catch (RemoteException unused) {
                                    this.mRemoveList.add(next.binder);
                                }
                            }
                        }
                    }
                    if (z2) {
                        Iterator<Record> it3 = this.mRecords.iterator();
                        while (it3.hasNext()) {
                            Record next2 = it3.next();
                            if (next2.matchTelephonyCallbackEvent(27) && idMatch(next2, i2, i)) {
                                try {
                                    next2.callback.onCallStatesChanged(this.mCallStateLists.get(i));
                                } catch (RemoteException unused2) {
                                    this.mRemoveList.add(next2.binder);
                                }
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyDisconnectCause(int i, int i2, int i3, int i4) {
        if (checkNotifyPermission("notifyDisconnectCause()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mCallDisconnectCause[i] = i3;
                    this.mCallPreciseDisconnectCause[i] = i4;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(26) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onCallDisconnectCauseChanged(this.mCallDisconnectCause[i], this.mCallPreciseDisconnectCause[i]);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyImsDisconnectCause(int i, ImsReasonInfo imsReasonInfo) {
        if (checkNotifyPermission("notifyImsCallDisconnectCause()")) {
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            synchronized (this.mRecords) {
                if (validatePhoneId(phoneIdFromSubId)) {
                    if (imsReasonInfo == null) {
                        loge("ImsReasonInfo is null, subId=" + i + ", phoneId=" + phoneIdFromSubId);
                        this.mImsReasonInfo.set(phoneIdFromSubId, new ImsReasonInfo());
                        return;
                    }
                    this.mImsReasonInfo.set(phoneIdFromSubId, imsReasonInfo);
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(28) && idMatch(next, i, phoneIdFromSubId)) {
                            try {
                                next.callback.onImsCallDisconnectCauseChanged(this.mImsReasonInfo.get(phoneIdFromSubId));
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifySrvccStateChanged(int i, int i2) {
        if (checkNotifyPermission("notifySrvccStateChanged()")) {
            int phoneIdFromSubId = getPhoneIdFromSubId(i);
            synchronized (this.mRecords) {
                if (validatePhoneId(phoneIdFromSubId)) {
                    this.mSrvccState[phoneIdFromSubId] = i2;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(16) && idMatch(next, i, phoneIdFromSubId)) {
                            try {
                                next.callback.onSrvccStateChanged(i2);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyOemHookRawEventForSubscriber(int i, int i2, byte[] bArr) {
        if (checkNotifyPermission("notifyOemHookRawEventForSubscriber")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(15) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onOemHookRawEvent(bArr);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyPhoneCapabilityChanged(PhoneCapability phoneCapability) {
        if (checkNotifyPermission("notifyPhoneCapabilityChanged()")) {
            synchronized (this.mRecords) {
                this.mPhoneCapability = phoneCapability;
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchTelephonyCallbackEvent(22)) {
                        try {
                            next.callback.onPhoneCapabilityChanged(phoneCapability);
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyActiveDataSubIdChanged(int i) {
        if (checkNotifyPermission("notifyActiveDataSubIdChanged()")) {
            log("notifyActiveDataSubIdChanged: activeDataSubId=" + i);
            this.mLocalLog.log("notifyActiveDataSubIdChanged: activeDataSubId=" + i);
            this.mActiveDataSubId = i;
            synchronized (this.mRecords) {
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchTelephonyCallbackEvent(23)) {
                        try {
                            next.callback.onActiveDataSubIdChanged(i);
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyRadioPowerStateChanged(int i, int i2, int i3) {
        if (checkNotifyPermission("notifyRadioPowerStateChanged()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mRadioPowerState = i3;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(24) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onRadioPowerStateChanged(i3);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
            broadcastRadioPowerStateChanged(i3, i, i2);
        }
    }

    public void notifyEmergencyNumberList(int i, int i2) {
        if (checkNotifyPermission("notifyEmergencyNumberList()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mEmergencyNumberList = ((TelephonyManager) this.mContext.getSystemService(HintsHelper.AUTOFILL_HINT_PHONE)).getEmergencyNumberList();
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(25) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onEmergencyNumberListChanged(this.mEmergencyNumberList);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyOutgoingEmergencyCall(int i, int i2, EmergencyNumber emergencyNumber) {
        if (checkNotifyPermission("notifyOutgoingEmergencyCall()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mOutgoingCallEmergencyNumber[i] = emergencyNumber;
                }
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchTelephonyCallbackEvent(29)) {
                        try {
                            next.callback.onOutgoingEmergencyCall(emergencyNumber, i2);
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyOutgoingEmergencySms(int i, int i2, EmergencyNumber emergencyNumber) {
        if (checkNotifyPermission("notifyOutgoingEmergencySms()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mOutgoingSmsEmergencyNumber[i] = emergencyNumber;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(30)) {
                            try {
                                next.callback.onOutgoingEmergencySms(emergencyNumber, i2);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyCallQualityChanged(CallQuality callQuality, int i, int i2, int i3) {
        if (checkNotifyPermission("notifyCallQualityChanged()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mCallQuality[i] = callQuality;
                    this.mCallNetworkType[i] = i3;
                    if (this.mCallStateLists.get(i).size() > 0 && this.mCallStateLists.get(i).get(0).getCallState() == 1) {
                        CallState remove = this.mCallStateLists.get(i).remove(0);
                        this.mCallStateLists.get(i).add(0, new CallState.Builder(remove.getCallState()).setNetworkType(i3).setCallQuality(callQuality).setCallClassification(remove.getCallClassification()).setImsCallSessionId(remove.getImsCallSessionId()).setImsCallServiceType(remove.getImsCallServiceType()).setImsCallType(remove.getImsCallType()).build());
                        Iterator<Record> it = this.mRecords.iterator();
                        while (it.hasNext()) {
                            Record next = it.next();
                            if (next.matchTelephonyCallbackEvent(27) && idMatch(next, i2, i)) {
                                try {
                                    next.callback.onCallStatesChanged(this.mCallStateLists.get(i));
                                } catch (RemoteException unused) {
                                    this.mRemoveList.add(next.binder);
                                }
                            }
                        }
                    } else {
                        log("There is no active call to report CallQuality");
                        return;
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyRegistrationFailed(int i, int i2, CellIdentity cellIdentity, String str, int i3, int i4, int i5) {
        Record record;
        int i6 = i;
        if (checkNotifyPermission("notifyRegistrationFailed()")) {
            CellIdentity sanitizeLocationInfo = cellIdentity.sanitizeLocationInfo();
            this.mLocalLog.log("Registration Failed for phoneId=" + i6 + " subId=" + i2 + "primaryPlmn=" + cellIdentity.getPlmn() + " chosenPlmn=" + str + " domain=" + i3 + " causeCode=" + i4 + " additionalCauseCode=" + i5);
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(31) && idMatch(next, i2, i6)) {
                            try {
                                record = next;
                            } catch (RemoteException unused) {
                                record = next;
                            }
                            try {
                                next.callback.onRegistrationFailed(checkFineLocationAccess(next, 1) ? cellIdentity : sanitizeLocationInfo, str, i3, i4, i5);
                            } catch (RemoteException unused2) {
                                this.mRemoveList.add(record.binder);
                                i6 = i;
                            }
                        }
                        i6 = i;
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyBarringInfoChanged(int i, int i2, BarringInfo barringInfo) {
        if (checkNotifyPermission("notifyBarringInfo()")) {
            if (!validatePhoneId(i)) {
                loge("Received invalid phoneId for BarringInfo = " + i);
                return;
            }
            synchronized (this.mRecords) {
                if (barringInfo == null) {
                    loge("Received null BarringInfo for subId=" + i2 + ", phoneId=" + i);
                    this.mBarringInfo.set(i, new BarringInfo());
                    return;
                }
                if (barringInfo.equals(this.mBarringInfo.get(i))) {
                    return;
                }
                this.mBarringInfo.set(i, barringInfo);
                BarringInfo createLocationInfoSanitizedCopy = barringInfo.createLocationInfoSanitizedCopy();
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchTelephonyCallbackEvent(32) && idMatch(next, i2, i)) {
                        try {
                            next.callback.onBarringInfoChanged(checkFineLocationAccess(next, 1) ? barringInfo : createLocationInfoSanitizedCopy);
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyPhysicalChannelConfigForSubscriber(int i, int i2, List<PhysicalChannelConfig> list) {
        if (checkNotifyPermission("notifyPhysicalChannelConfig()")) {
            List<PhysicalChannelConfig> locationSanitizedConfigs = getLocationSanitizedConfigs(list);
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mPhysicalChannelConfigs.set(i, list);
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(33) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onPhysicalChannelConfigChanged(shouldSanitizeLocationForPhysicalChannelConfig(next) ? locationSanitizedConfigs : list);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    private static boolean shouldSanitizeLocationForPhysicalChannelConfig(Record record) {
        int i = record.callerUid;
        return (i == 1001 || i == 1000) ? false : true;
    }

    private static List<PhysicalChannelConfig> getLocationSanitizedConfigs(List<PhysicalChannelConfig> list) {
        ArrayList arrayList = new ArrayList(list.size());
        Iterator<PhysicalChannelConfig> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().createLocationInfoSanitizedCopy());
        }
        return arrayList;
    }

    public void notifyDataEnabled(int i, int i2, boolean z, int i3) {
        if (checkNotifyPermission("notifyDataEnabled()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mIsDataEnabled[i] = z;
                    this.mDataEnabledReason[i] = i3;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(34) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onDataEnabledChanged(z, i3);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyAllowedNetworkTypesChanged(int i, int i2, int i3, long j) {
        if (checkNotifyPermission("notifyAllowedNetworkTypesChanged()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mAllowedNetworkTypeReason[i] = i3;
                    this.mAllowedNetworkTypeValue[i] = j;
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(35) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onAllowedNetworkTypesChanged(i3, j);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyLinkCapacityEstimateChanged(int i, int i2, List<LinkCapacityEstimate> list) {
        if (checkNotifyPermission("notifyLinkCapacityEstimateChanged()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    this.mLinkCapacityEstimateLists.set(i, list);
                    Iterator<Record> it = this.mRecords.iterator();
                    while (it.hasNext()) {
                        Record next = it.next();
                        if (next.matchTelephonyCallbackEvent(37) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onLinkCapacityEstimateChanged(list);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void addCarrierPrivilegesCallback(int i, ICarrierPrivilegesCallback iCarrierPrivilegesCallback, String str, String str2) {
        UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "addCarrierPrivilegesCallback");
        onMultiSimConfigChanged();
        synchronized (this.mRecords) {
            if (!validatePhoneId(i)) {
                throw new IllegalArgumentException("Invalid slot index: " + i);
            }
            Record add = add(iCarrierPrivilegesCallback.asBinder(), Binder.getCallingUid(), Binder.getCallingPid(), false);
            if (add == null) {
                return;
            }
            add.context = this.mContext;
            add.carrierPrivilegesCallback = iCarrierPrivilegesCallback;
            add.callingPackage = str;
            add.callingFeatureId = str2;
            add.callerUid = Binder.getCallingUid();
            add.callerPid = Binder.getCallingPid();
            add.phoneId = i;
            add.eventList = new ArraySet();
            Pair<List<String>, int[]> pair = this.mCarrierPrivilegeStates.get(i);
            Pair<String, Integer> pair2 = this.mCarrierServiceStates.get(i);
            try {
                if (add.matchCarrierPrivilegesCallback()) {
                    ICarrierPrivilegesCallback iCarrierPrivilegesCallback2 = add.carrierPrivilegesCallback;
                    List unmodifiableList = Collections.unmodifiableList((List) pair.first);
                    Object obj = pair.second;
                    iCarrierPrivilegesCallback2.onCarrierPrivilegesChanged(unmodifiableList, Arrays.copyOf((int[]) obj, ((int[]) obj).length));
                    add.carrierPrivilegesCallback.onCarrierServiceChanged((String) pair2.first, ((Integer) pair2.second).intValue());
                }
            } catch (RemoteException unused) {
                remove(add.binder);
            }
        }
    }

    public void removeCarrierPrivilegesCallback(ICarrierPrivilegesCallback iCarrierPrivilegesCallback, String str) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", "removeCarrierPrivilegesCallback");
        remove(iCarrierPrivilegesCallback.asBinder());
    }

    public void notifyCarrierPrivilegesChanged(int i, List<String> list, int[] iArr) {
        if (checkNotifyPermission("notifyCarrierPrivilegesChanged")) {
            onMultiSimConfigChanged();
            synchronized (this.mRecords) {
                if (!validatePhoneId(i)) {
                    throw new IllegalArgumentException("Invalid slot index: " + i);
                }
                this.mCarrierPrivilegeStates.set(i, new Pair<>(list, iArr));
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchCarrierPrivilegesCallback() && idMatch(next, -1, i)) {
                        try {
                            next.carrierPrivilegesCallback.onCarrierPrivilegesChanged(Collections.unmodifiableList(list), Arrays.copyOf(iArr, iArr.length));
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyCarrierServiceChanged(int i, String str, int i2) {
        if (checkNotifyPermission("notifyCarrierServiceChanged") && validatePhoneId(i)) {
            onMultiSimConfigChanged();
            synchronized (this.mRecords) {
                this.mCarrierServiceStates.set(i, new Pair<>(str, Integer.valueOf(i2)));
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchCarrierPrivilegesCallback() && idMatch(next, -1, i)) {
                        try {
                            next.carrierPrivilegesCallback.onCarrierServiceChanged(str, i2);
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void addCarrierConfigChangeListener(ICarrierConfigChangeListener iCarrierConfigChangeListener, String str, String str2) {
        UserHandle.getCallingUserId();
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        synchronized (this.mRecords) {
            Record add = add(iCarrierConfigChangeListener.asBinder(), Binder.getCallingUid(), Binder.getCallingPid(), doesLimitApplyForListeners(Binder.getCallingUid(), Process.myUid()));
            if (add == null) {
                loge("Can not create Record instance!");
                return;
            }
            add.context = this.mContext;
            add.carrierConfigChangeListener = iCarrierConfigChangeListener;
            add.callingPackage = str;
            add.callingFeatureId = str2;
            add.callerUid = Binder.getCallingUid();
            add.callerPid = Binder.getCallingPid();
            add.eventList = new ArraySet();
        }
    }

    public void removeCarrierConfigChangeListener(ICarrierConfigChangeListener iCarrierConfigChangeListener, String str) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        remove(iCarrierConfigChangeListener.asBinder());
    }

    public void notifyCarrierConfigChanged(int i, int i2, int i3, int i4) {
        if (!validatePhoneId(i)) {
            throw new IllegalArgumentException("Invalid phoneId: " + i);
        }
        if (!checkNotifyPermission("notifyCarrierConfigChanged")) {
            loge("Caller has no notify permission!");
            return;
        }
        synchronized (this.mRecords) {
            this.mRemoveList.clear();
            Iterator<Record> it = this.mRecords.iterator();
            while (it.hasNext()) {
                Record next = it.next();
                if (next.matchCarrierConfigChangeListener()) {
                    try {
                        next.carrierConfigChangeListener.onCarrierConfigChanged(i, i2, i3, i4);
                    } catch (RemoteException unused) {
                        this.mRemoveList.add(next.binder);
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyMediaQualityStatusChanged(int i, int i2, MediaQualityStatus mediaQualityStatus) {
        CallState callState;
        if (checkNotifyPermission("notifyMediaQualityStatusChanged()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    if (this.mCallStateLists.get(i).size() > 0) {
                        Iterator<CallState> it = this.mCallStateLists.get(i).iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                callState = null;
                                break;
                            } else {
                                callState = it.next();
                                if (callState.getCallState() == 1) {
                                    break;
                                }
                            }
                        }
                        if (callState != null) {
                            String imsCallSessionId = callState.getImsCallSessionId();
                            if (imsCallSessionId != null && imsCallSessionId.equals(mediaQualityStatus.getCallSessionId())) {
                                this.mMediaQualityStatus.get(i).put(mediaQualityStatus.getMediaSessionType(), mediaQualityStatus);
                            } else {
                                log("SessionId mismatch active call:" + imsCallSessionId + " media quality:" + mediaQualityStatus.getCallSessionId());
                                return;
                            }
                        } else {
                            log("There is no active call to report CallQaulity");
                            return;
                        }
                    }
                    Iterator<Record> it2 = this.mRecords.iterator();
                    while (it2.hasNext()) {
                        Record next = it2.next();
                        if (next.matchTelephonyCallbackEvent(39) && idMatch(next, i2, i)) {
                            try {
                                next.callback.onMediaQualityStatusChanged(mediaQualityStatus);
                            } catch (RemoteException unused) {
                                this.mRemoveList.add(next.binder);
                            }
                        }
                    }
                }
                handleRemoveListLocked();
            }
        }
    }

    public void notifyCallbackModeStarted(int i, int i2, int i3) {
        if (checkNotifyPermission("notifyCallbackModeStarted()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    if (i3 == 1) {
                        this.mECBMStarted[i] = true;
                    } else if (i3 == 2) {
                        this.mSCBMStarted[i] = true;
                    }
                }
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchTelephonyCallbackEvent(40)) {
                        try {
                            next.callback.onCallBackModeStarted(i3);
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    public void notifyCallbackModeStopped(int i, int i2, int i3, int i4) {
        if (checkNotifyPermission("notifyCallbackModeStopped()")) {
            synchronized (this.mRecords) {
                if (validatePhoneId(i)) {
                    if (i3 == 1) {
                        this.mECBMStarted[i] = false;
                        this.mECBMReason[i] = i4;
                    } else if (i3 == 2) {
                        this.mSCBMStarted[i] = false;
                        this.mSCBMReason[i] = i4;
                    }
                }
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    Record next = it.next();
                    if (next.matchTelephonyCallbackEvent(40)) {
                        try {
                            next.callback.onCallBackModeStopped(i3, i4);
                        } catch (RemoteException unused) {
                            this.mRemoveList.add(next.binder);
                        }
                    }
                }
            }
            handleRemoveListLocked();
        }
    }

    @NeverCompile
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, indentingPrintWriter)) {
            synchronized (this.mRecords) {
                int size = this.mRecords.size();
                indentingPrintWriter.println("last known state:");
                indentingPrintWriter.increaseIndent();
                for (int i = 0; i < getTelephonyManager().getActiveModemCount(); i++) {
                    indentingPrintWriter.println("Phone Id=" + i);
                    indentingPrintWriter.increaseIndent();
                    indentingPrintWriter.println("mCallState=" + this.mCallState[i]);
                    indentingPrintWriter.println("mRingingCallState=" + this.mRingingCallState[i]);
                    indentingPrintWriter.println("mForegroundCallState=" + this.mForegroundCallState[i]);
                    indentingPrintWriter.println("mBackgroundCallState=" + this.mBackgroundCallState[i]);
                    indentingPrintWriter.println("mPreciseCallState=" + this.mPreciseCallState[i]);
                    indentingPrintWriter.println("mCallDisconnectCause=" + this.mCallDisconnectCause[i]);
                    indentingPrintWriter.println("mCallIncomingNumber=" + this.mCallIncomingNumber[i]);
                    indentingPrintWriter.println("mServiceState=" + this.mServiceState[i]);
                    indentingPrintWriter.println("mVoiceActivationState= " + this.mVoiceActivationState[i]);
                    indentingPrintWriter.println("mDataActivationState= " + this.mDataActivationState[i]);
                    indentingPrintWriter.println("mUserMobileDataState= " + this.mUserMobileDataState[i]);
                    indentingPrintWriter.println("mSignalStrength=" + this.mSignalStrength[i]);
                    indentingPrintWriter.println("mMessageWaiting=" + this.mMessageWaiting[i]);
                    indentingPrintWriter.println("mCallForwarding=" + this.mCallForwarding[i]);
                    indentingPrintWriter.println("mDataActivity=" + this.mDataActivity[i]);
                    indentingPrintWriter.println("mDataConnectionState=" + this.mDataConnectionState[i]);
                    indentingPrintWriter.println("mCellIdentity=" + this.mCellIdentity[i]);
                    indentingPrintWriter.println("mCellInfo=" + this.mCellInfo.get(i));
                    indentingPrintWriter.println("mImsCallDisconnectCause=" + this.mImsReasonInfo.get(i));
                    indentingPrintWriter.println("mSrvccState=" + this.mSrvccState[i]);
                    indentingPrintWriter.println("mCallPreciseDisconnectCause=" + this.mCallPreciseDisconnectCause[i]);
                    indentingPrintWriter.println("mCallQuality=" + this.mCallQuality[i]);
                    indentingPrintWriter.println("mCallNetworkType=" + this.mCallNetworkType[i]);
                    indentingPrintWriter.println("mPreciseDataConnectionStates=" + this.mPreciseDataConnectionStates.get(i));
                    indentingPrintWriter.println("mOutgoingCallEmergencyNumber=" + this.mOutgoingCallEmergencyNumber[i]);
                    indentingPrintWriter.println("mOutgoingSmsEmergencyNumber=" + this.mOutgoingSmsEmergencyNumber[i]);
                    indentingPrintWriter.println("mBarringInfo=" + this.mBarringInfo.get(i));
                    indentingPrintWriter.println("mCarrierNetworkChangeState=" + this.mCarrierNetworkChangeState[i]);
                    indentingPrintWriter.println("mTelephonyDisplayInfo=" + this.mTelephonyDisplayInfos[i]);
                    indentingPrintWriter.println("mIsDataEnabled=" + this.mIsDataEnabled[i]);
                    indentingPrintWriter.println("mDataEnabledReason=" + this.mDataEnabledReason[i]);
                    indentingPrintWriter.println("mAllowedNetworkTypeReason=" + this.mAllowedNetworkTypeReason[i]);
                    indentingPrintWriter.println("mAllowedNetworkTypeValue=" + this.mAllowedNetworkTypeValue[i]);
                    indentingPrintWriter.println("mPhysicalChannelConfigs=" + this.mPhysicalChannelConfigs.get(i));
                    indentingPrintWriter.println("mLinkCapacityEstimateList=" + this.mLinkCapacityEstimateLists.get(i));
                    indentingPrintWriter.println("mECBMReason=" + this.mECBMReason[i]);
                    indentingPrintWriter.println("mECBMStarted=" + this.mECBMStarted[i]);
                    indentingPrintWriter.println("mSCBMReason=" + this.mSCBMReason[i]);
                    indentingPrintWriter.println("mSCBMStarted=" + this.mSCBMStarted[i]);
                    Pair<List<String>, int[]> pair = this.mCarrierPrivilegeStates.get(i);
                    indentingPrintWriter.println("mCarrierPrivilegeState=<packages=" + pii((List<String>) pair.first) + ", uids=" + Arrays.toString((int[]) pair.second) + ">");
                    Pair<String, Integer> pair2 = this.mCarrierServiceStates.get(i);
                    indentingPrintWriter.println("mCarrierServiceState=<package=" + pii((String) pair2.first) + ", uid=" + pair2.second + ">");
                    indentingPrintWriter.decreaseIndent();
                }
                indentingPrintWriter.println("mPhoneCapability=" + this.mPhoneCapability);
                indentingPrintWriter.println("mActiveDataSubId=" + this.mActiveDataSubId);
                indentingPrintWriter.println("mRadioPowerState=" + this.mRadioPowerState);
                indentingPrintWriter.println("mEmergencyNumberList=" + this.mEmergencyNumberList);
                indentingPrintWriter.println("mDefaultPhoneId=" + this.mDefaultPhoneId);
                indentingPrintWriter.println("mDefaultSubId=" + this.mDefaultSubId);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("local logs:");
                indentingPrintWriter.increaseIndent();
                this.mLocalLog.dump(fileDescriptor, indentingPrintWriter, strArr);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("listen logs:");
                indentingPrintWriter.increaseIndent();
                this.mListenLog.dump(fileDescriptor, indentingPrintWriter, strArr);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("registrations: count=" + size);
                indentingPrintWriter.increaseIndent();
                Iterator<Record> it = this.mRecords.iterator();
                while (it.hasNext()) {
                    indentingPrintWriter.println(it.next());
                }
                indentingPrintWriter.decreaseIndent();
            }
        }
    }

    private void broadcastServiceStateChanged(ServiceState serviceState, int i, int i2) {
        try {
            this.mBatteryStats.notePhoneState(serviceState.getState());
        } catch (RemoteException unused) {
        }
        Context context = this.mContext;
        if (LocationAccessPolicy.isLocationModeEnabled(context, context.getUserId())) {
            Intent createServiceStateIntent = createServiceStateIntent(serviceState, i2, i, false);
            this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION"}, createServiceStateBroadcastOptions(i2, i, "I:RA"));
            this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent, new String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION"}, new String[]{"android.permission.READ_PHONE_STATE"}, null, createServiceStateBroadcastOptions(i2, i, "I:RPA,E:R"));
            Intent createServiceStateIntent2 = createServiceStateIntent(serviceState, i2, i, true);
            this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent2, new String[]{"android.permission.READ_PHONE_STATE"}, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, null, createServiceStateBroadcastOptions(i2, i, "I:R,E:A"));
            this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent2, new String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION"}, null, createServiceStateBroadcastOptions(i2, i, "I:RP,E:RA"));
            return;
        }
        String[] strArr = (String[]) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda1
            public final Object getOrThrow() {
                String[] lambda$broadcastServiceStateChanged$1;
                lambda$broadcastServiceStateChanged$1 = TelephonyRegistry.this.lambda$broadcastServiceStateChanged$1();
                return lambda$broadcastServiceStateChanged$1;
            }
        });
        for (String str : strArr) {
            Intent createServiceStateIntent3 = createServiceStateIntent(serviceState, i2, i, false);
            createServiceStateIntent3.setPackage(str);
            this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent3, new String[]{"android.permission.READ_PHONE_STATE"}, createServiceStateBroadcastOptions(i2, i, "I:R"));
            this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent3, new String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new String[]{"android.permission.READ_PHONE_STATE"}, null, createServiceStateBroadcastOptions(i2, i, "I:RP,E:R"));
        }
        Intent createServiceStateIntent4 = createServiceStateIntent(serviceState, i2, i, true);
        this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent4, new String[]{"android.permission.READ_PHONE_STATE"}, new String[0], strArr, createServiceStateBroadcastOptions(i2, i, "I:R,lbp"));
        this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(createServiceStateIntent4, new String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new String[]{"android.permission.READ_PHONE_STATE"}, strArr, createServiceStateBroadcastOptions(i2, i, "I:RP,E:R,lbp"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String[] lambda$broadcastServiceStateChanged$1() throws Exception {
        return LocationAccessPolicy.getLocationBypassPackages(this.mContext);
    }

    private Intent createServiceStateIntent(ServiceState serviceState, int i, int i2, boolean z) {
        Intent intent = new Intent("android.intent.action.SERVICE_STATE");
        intent.addFlags(16777216);
        Bundle bundle = new Bundle();
        if (z) {
            serviceState.createLocationInfoSanitizedCopy(true).fillInNotifierBundle(bundle);
        } else {
            serviceState.fillInNotifierBundle(bundle);
        }
        intent.putExtras(bundle);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, i);
        intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, i);
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, i2);
        intent.putExtra("android.telephony.extra.SLOT_INDEX", i2);
        return intent;
    }

    private void broadcastRadioPowerStateChanged(int i, int i2, int i3) {
        Intent intent = new Intent(ACTION_RADIO_POWER_STATE_CHANGED);
        intent.addFlags(16777216);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, i3);
        intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, i3);
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, i2);
        intent.putExtra("android.telephony.extra.SLOT_INDEX", i2);
        intent.putExtra(PHONE_CONSTANTS_STATE_KEY, i);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    private BroadcastOptions createServiceStateBroadcastOptions(int i, int i2, String str) {
        return new BroadcastOptions().setDeliveryGroupPolicy(1).setDeliveryGroupMatchingKey("android.intent.action.SERVICE_STATE", i + "-" + i2 + "-" + str).setDeferralPolicy(2);
    }

    private void broadcastSignalStrengthChanged(SignalStrength signalStrength, int i, int i2) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            this.mBatteryStats.notePhoneSignalStrength(signalStrength);
        } catch (RemoteException unused) {
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
        Intent intent = new Intent(ACTION_SIGNAL_STRENGTH_CHANGED);
        Bundle bundle = new Bundle();
        fillInSignalStrengthNotifierBundle(signalStrength, bundle);
        intent.putExtras(bundle);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, i2);
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, i);
        this.mContext.sendStickyBroadcastAsUser(intent, UserHandle.ALL);
    }

    private void fillInSignalStrengthNotifierBundle(SignalStrength signalStrength, Bundle bundle) {
        for (CellSignalStrength cellSignalStrength : signalStrength.getCellSignalStrengths()) {
            if (cellSignalStrength instanceof CellSignalStrengthLte) {
                bundle.putParcelable("Lte", (CellSignalStrengthLte) cellSignalStrength);
            } else if (cellSignalStrength instanceof CellSignalStrengthCdma) {
                bundle.putParcelable("Cdma", (CellSignalStrengthCdma) cellSignalStrength);
            } else if (cellSignalStrength instanceof CellSignalStrengthGsm) {
                bundle.putParcelable("Gsm", (CellSignalStrengthGsm) cellSignalStrength);
            } else if (cellSignalStrength instanceof CellSignalStrengthWcdma) {
                bundle.putParcelable("Wcdma", (CellSignalStrengthWcdma) cellSignalStrength);
            } else if (cellSignalStrength instanceof CellSignalStrengthTdscdma) {
                bundle.putParcelable("Tdscdma", (CellSignalStrengthTdscdma) cellSignalStrength);
            } else if (cellSignalStrength instanceof CellSignalStrengthNr) {
                bundle.putParcelable("Nr", (CellSignalStrengthNr) cellSignalStrength);
            }
        }
    }

    private void broadcastCallStateChanged(int i, String str, int i2, int i3) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (i == 0) {
                this.mBatteryStats.notePhoneOff();
                FrameworkStatsLog.write(95, 0);
            } else {
                this.mBatteryStats.notePhoneOn();
                FrameworkStatsLog.write(95, 1);
            }
        } catch (RemoteException unused) {
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
        Binder.restoreCallingIdentity(clearCallingIdentity);
        Intent intent = new Intent("android.intent.action.PHONE_STATE");
        intent.putExtra(PHONE_CONSTANTS_STATE_KEY, callStateToString(i));
        if (i3 != -1) {
            intent.setAction(ACTION_SUBSCRIPTION_PHONE_STATE_CHANGED);
            intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, i3);
            intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, i3);
        }
        if (i2 != -1) {
            intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, i2);
            intent.putExtra("android.telephony.extra.SLOT_INDEX", i2);
        }
        intent.addFlags(16777216);
        Intent intent2 = new Intent(intent);
        intent2.putExtra("incoming_number", str);
        this.mContext.sendBroadcastAsUser(intent2, UserHandle.ALL, "android.permission.READ_PRIVILEGED_PHONE_STATE");
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PHONE_STATE", 51);
        this.mContext.sendBroadcastAsUserMultiplePermissions(intent2, UserHandle.ALL, new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_CALL_LOG"});
    }

    private static String callStateToString(int i) {
        if (i == 1) {
            return TelephonyManager.EXTRA_STATE_RINGING;
        }
        if (i == 2) {
            return TelephonyManager.EXTRA_STATE_OFFHOOK;
        }
        return TelephonyManager.EXTRA_STATE_IDLE;
    }

    private void broadcastDataConnectionStateChanged(int i, int i2, PreciseDataConnectionState preciseDataConnectionState) {
        Intent intent = new Intent(ACTION_ANY_DATA_CONNECTION_STATE_CHANGED);
        intent.putExtra(PHONE_CONSTANTS_STATE_KEY, TelephonyUtils.dataStateToString(preciseDataConnectionState.getState()));
        intent.putExtra(PHONE_CONSTANTS_DATA_APN_KEY, preciseDataConnectionState.getApnSetting().getApnName());
        intent.putExtra(PHONE_CONSTANTS_DATA_APN_TYPE_KEY, getApnTypesStringFromBitmask(preciseDataConnectionState.getApnSetting().getApnTypeBitmask()));
        intent.putExtra(PHONE_CONSTANTS_SLOT_KEY, i);
        intent.putExtra(PHONE_CONSTANTS_SUBSCRIPTION_KEY, i2);
        intent.putExtra(EXTRA_SUBSCRIPTION_INDEX, i2);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, "android.permission.READ_PHONE_STATE");
        this.mContext.createContextAsUser(UserHandle.ALL, 0).sendBroadcastMultiplePermissions(intent, new String[]{"android.permission.READ_PRIVILEGED_PHONE_STATE"}, new String[]{"android.permission.READ_PHONE_STATE"});
    }

    @VisibleForTesting
    public static String getApnTypesStringFromBitmask(int i) {
        ArrayList arrayList = new ArrayList();
        if ((i & 17) == 17) {
            arrayList.add("default");
            i &= -18;
        }
        while (i != 0) {
            int highestOneBit = Integer.highestOneBit(i);
            String apnTypeString = ApnSetting.getApnTypeString(highestOneBit);
            if (!TextUtils.isEmpty(apnTypeString)) {
                arrayList.add(apnTypeString);
            }
            i &= ~highestOneBit;
        }
        return TextUtils.join(",", arrayList);
    }

    private void enforceNotifyPermissionOrCarrierPrivilege(String str) {
        if (checkNotifyPermission()) {
            return;
        }
        TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mContext, SubscriptionManager.getDefaultSubscriptionId(), str);
    }

    private boolean checkNotifyPermission(String str) {
        if (checkNotifyPermission()) {
            return true;
        }
        Binder.getCallingPid();
        Binder.getCallingUid();
        return false;
    }

    private boolean checkNotifyPermission() {
        return this.mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0;
    }

    private boolean checkListenerPermission(Set<Integer> set, int i, String str, String str2, String str3) {
        boolean z = true;
        if (isLocationPermissionRequired(set)) {
            LocationAccessPolicy.LocationPermissionQuery.Builder callingUid = new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(str).setCallingFeatureId(str2).setMethod(str3 + " events: " + set).setCallingPid(Binder.getCallingPid()).setCallingUid(Binder.getCallingUid());
            callingUid.setMinSdkVersionForFine(29);
            callingUid.setMinSdkVersionForCoarse(0);
            callingUid.setMinSdkVersionForEnforcement(0);
            int i2 = AnonymousClass3.$SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.checkLocationPermission(this.mContext, callingUid.build()).ordinal()];
            if (i2 == 1) {
                throw new SecurityException("Unable to listen for events " + set + " due to insufficient location permissions.");
            }
            if (i2 == 2) {
                z = false;
            }
        }
        boolean z2 = (!isPhoneStatePermissionRequired(set, str, Binder.getCallingUserHandle()) || TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mContext, i, str, str2, str3)) ? z : false;
        if (isPrecisePhoneStatePermissionRequired(set)) {
            try {
                this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRECISE_PHONE_STATE", null);
            } catch (SecurityException unused) {
                TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mContext, i, str3);
            }
        }
        if (isActiveEmergencySessionPermissionRequired(set)) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_ACTIVE_EMERGENCY_SESSION", null);
        }
        if (isPrivilegedPhoneStatePermissionRequired(set)) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", null);
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.TelephonyRegistry$3, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult;

        static {
            int[] iArr = new int[LocationAccessPolicy.LocationPermissionResult.values().length];
            $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult = iArr;
            try {
                iArr[LocationAccessPolicy.LocationPermissionResult.DENIED_HARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$telephony$LocationAccessPolicy$LocationPermissionResult[LocationAccessPolicy.LocationPermissionResult.DENIED_SOFT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRemoveListLocked() {
        if (this.mRemoveList.size() > 0) {
            Iterator<IBinder> it = this.mRemoveList.iterator();
            while (it.hasNext()) {
                remove(it.next());
            }
            this.mRemoveList.clear();
        }
    }

    private boolean validateEventAndUserLocked(Record record, int i) {
        boolean z;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            if (UserHandle.getUserId(record.callerUid) == ActivityManager.getCurrentUser()) {
                if (record.matchTelephonyCallbackEvent(i)) {
                    z = true;
                    return z;
                }
            }
            z = false;
            return z;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validatePhoneId(int i) {
        return i >= 0 && i < getTelephonyManager().getActiveModemCount();
    }

    private static void log(String str) {
        Rlog.d(TAG, str);
    }

    private static void loge(String str) {
        Rlog.e(TAG, str);
    }

    boolean idMatch(Record record, int i, int i2) {
        if (i < 0) {
            return record.phoneId == i2;
        }
        int i3 = record.subId;
        return i3 == Integer.MAX_VALUE ? i == this.mDefaultSubId : i3 == i;
    }

    private boolean checkFineLocationAccess(Record record) {
        return checkFineLocationAccess(record, 1);
    }

    private boolean checkCoarseLocationAccess(Record record) {
        return checkCoarseLocationAccess(record, 1);
    }

    private boolean checkFineLocationAccess(Record record, int i) {
        if (record.renounceFineLocationAccess) {
            return false;
        }
        final LocationAccessPolicy.LocationPermissionQuery build = new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(record.callingPackage).setCallingFeatureId(record.callingFeatureId).setCallingPid(record.callerPid).setCallingUid(record.callerUid).setMethod("TelephonyRegistry push").setLogAsInfo(true).setMinSdkVersionForFine(i).setMinSdkVersionForCoarse(i).setMinSdkVersionForEnforcement(i).build();
        return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda2
            public final Object getOrThrow() {
                Boolean lambda$checkFineLocationAccess$2;
                lambda$checkFineLocationAccess$2 = TelephonyRegistry.this.lambda$checkFineLocationAccess$2(build);
                return lambda$checkFineLocationAccess$2;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$checkFineLocationAccess$2(LocationAccessPolicy.LocationPermissionQuery locationPermissionQuery) throws Exception {
        return Boolean.valueOf(LocationAccessPolicy.checkLocationPermission(this.mContext, locationPermissionQuery) == LocationAccessPolicy.LocationPermissionResult.ALLOWED);
    }

    private boolean checkCoarseLocationAccess(Record record, int i) {
        if (record.renounceCoarseLocationAccess) {
            return false;
        }
        final LocationAccessPolicy.LocationPermissionQuery build = new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(record.callingPackage).setCallingFeatureId(record.callingFeatureId).setCallingPid(record.callerPid).setCallingUid(record.callerUid).setMethod("TelephonyRegistry push").setLogAsInfo(true).setMinSdkVersionForCoarse(i).setMinSdkVersionForFine(Integer.MAX_VALUE).setMinSdkVersionForEnforcement(i).build();
        return ((Boolean) Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.TelephonyRegistry$$ExternalSyntheticLambda0
            public final Object getOrThrow() {
                Boolean lambda$checkCoarseLocationAccess$3;
                lambda$checkCoarseLocationAccess$3 = TelephonyRegistry.this.lambda$checkCoarseLocationAccess$3(build);
                return lambda$checkCoarseLocationAccess$3;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$checkCoarseLocationAccess$3(LocationAccessPolicy.LocationPermissionQuery locationPermissionQuery) throws Exception {
        return Boolean.valueOf(LocationAccessPolicy.checkLocationPermission(this.mContext, locationPermissionQuery) == LocationAccessPolicy.LocationPermissionResult.ALLOWED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPossibleMissNotify(Record record, int i) {
        Set<Integer> set = record.eventList;
        if (set == null || set.isEmpty()) {
            log("checkPossibleMissNotify: events = null.");
            return;
        }
        if (set.contains(1)) {
            try {
                ServiceState serviceState = new ServiceState(this.mServiceState[i]);
                if (checkFineLocationAccess(record, 29)) {
                    record.callback.onServiceStateChanged(serviceState);
                } else if (checkCoarseLocationAccess(record, 29)) {
                    record.callback.onServiceStateChanged(serviceState.createLocationInfoSanitizedCopy(false));
                } else {
                    record.callback.onServiceStateChanged(serviceState.createLocationInfoSanitizedCopy(true));
                }
            } catch (RemoteException unused) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (set.contains(9)) {
            try {
                SignalStrength signalStrength = this.mSignalStrength[i];
                if (signalStrength != null) {
                    record.callback.onSignalStrengthsChanged(new SignalStrength(signalStrength));
                }
            } catch (RemoteException unused2) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (set.contains(2)) {
            try {
                SignalStrength signalStrength2 = this.mSignalStrength[i];
                if (signalStrength2 != null) {
                    int gsmSignalStrength = signalStrength2.getGsmSignalStrength();
                    IPhoneStateListener iPhoneStateListener = record.callback;
                    if (gsmSignalStrength == 99) {
                        gsmSignalStrength = -1;
                    }
                    iPhoneStateListener.onSignalStrengthChanged(gsmSignalStrength);
                }
            } catch (RemoteException unused3) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (validateEventAndUserLocked(record, 11)) {
            try {
                if (checkCoarseLocationAccess(record, 1) && checkFineLocationAccess(record, 29)) {
                    record.callback.onCellInfoChanged(this.mCellInfo.get(i));
                }
            } catch (RemoteException unused4) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (set.contains(20)) {
            try {
                record.callback.onUserMobileDataStateChanged(this.mUserMobileDataState[i]);
            } catch (RemoteException unused5) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (set.contains(21)) {
            try {
                TelephonyDisplayInfo telephonyDisplayInfo = this.mTelephonyDisplayInfos[i];
                if (telephonyDisplayInfo != null) {
                    record.callback.onDisplayInfoChanged(telephonyDisplayInfo);
                }
            } catch (RemoteException unused6) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (set.contains(3)) {
            try {
                record.callback.onMessageWaitingIndicatorChanged(this.mMessageWaiting[i]);
            } catch (RemoteException unused7) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (set.contains(4)) {
            try {
                record.callback.onCallForwardingIndicatorChanged(this.mCallForwarding[i]);
            } catch (RemoteException unused8) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (validateEventAndUserLocked(record, 5)) {
            try {
                if (checkCoarseLocationAccess(record, 1) && checkFineLocationAccess(record, 29)) {
                    record.callback.onCellLocationChanged(this.mCellIdentity[i]);
                }
            } catch (RemoteException unused9) {
                this.mRemoveList.add(record.binder);
            }
        }
        if (set.contains(7)) {
            try {
                record.callback.onDataConnectionStateChanged(this.mDataConnectionState[i], this.mDataConnectionNetworkType[i]);
            } catch (RemoteException unused10) {
                this.mRemoveList.add(record.binder);
            }
        }
    }

    private static PreciseCallState createPreciseCallState() {
        return new PreciseCallState(-1, -1, -1, -1, -1);
    }

    private static CallQuality createCallQuality() {
        return new CallQuality(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPhoneIdFromSubId(int i) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        if (subscriptionManager == null) {
            return -1;
        }
        if (i == Integer.MAX_VALUE) {
            i = SubscriptionManager.getDefaultSubscriptionId();
        }
        SubscriptionInfo activeSubscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(i);
        if (activeSubscriptionInfo == null) {
            return -1;
        }
        return activeSubscriptionInfo.getSimSlotIndex();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String pii(String str) {
        return Build.IS_DEBUGGABLE ? str : "***";
    }

    private static String pii(List<String> list) {
        if (list.isEmpty() || Build.IS_DEBUGGABLE) {
            return list.toString();
        }
        return "[***, size=" + list.size() + "]";
    }
}
