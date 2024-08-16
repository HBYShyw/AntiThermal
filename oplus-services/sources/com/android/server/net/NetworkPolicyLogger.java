package com.android.server.net;

import android.app.ActivityManager;
import android.net.NetworkPolicyManager;
import android.os.PowerExemptionManager;
import android.os.UserHandle;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.RingBuffer;
import com.android.server.am.ProcessList;
import com.android.server.health.HealthServiceWrapperHidl;
import com.android.server.net.NetworkPolicyManagerService;
import com.android.server.power.LowPowerStandbyController;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NetworkPolicyLogger {
    private static final int EVENT_APP_IDLE_STATE_CHANGED = 8;
    private static final int EVENT_APP_IDLE_WL_CHANGED = 14;
    private static final int EVENT_DEVICE_IDLE_MODE_ENABLED = 7;
    private static final int EVENT_FIREWALL_CHAIN_ENABLED = 12;
    private static final int EVENT_INTERFACES_CHANGED = 18;
    private static final int EVENT_METEREDNESS_CHANGED = 4;
    private static final int EVENT_METERED_ALLOWLIST_CHANGED = 15;
    private static final int EVENT_METERED_DENYLIST_CHANGED = 16;
    private static final int EVENT_NETWORK_BLOCKED = 1;
    private static final int EVENT_PAROLE_STATE_CHANGED = 9;
    private static final int EVENT_POLICIES_CHANGED = 3;
    private static final int EVENT_RESTRICT_BG_CHANGED = 6;
    private static final int EVENT_ROAMING_CHANGED = 17;
    private static final int EVENT_TEMP_POWER_SAVE_WL_CHANGED = 10;
    private static final int EVENT_TYPE_GENERIC = 0;
    private static final int EVENT_UID_FIREWALL_RULE_CHANGED = 11;
    private static final int EVENT_UID_STATE_CHANGED = 2;
    private static final int EVENT_UPDATE_METERED_RESTRICTED_PKGS = 13;
    private static final int EVENT_USER_STATE_REMOVED = 5;
    private static final int MAX_LOG_SIZE;
    private static final int MAX_NETWORK_BLOCKED_LOG_SIZE;
    private int mDebugUid;
    private final LogBuffer mEventsBuffer;
    private final Object mLock;
    private final LogBuffer mNetworkBlockedBuffer = new LogBuffer(MAX_NETWORK_BLOCKED_LOG_SIZE);
    private final LogBuffer mUidStateChangeBuffer;
    static final String TAG = "NetworkPolicy";
    static final boolean LOGD = Log.isLoggable(TAG, 3);
    static final boolean LOGV = Log.isLoggable(TAG, 2);

    public NetworkPolicyLogger() {
        int i = MAX_LOG_SIZE;
        this.mUidStateChangeBuffer = new LogBuffer(i);
        this.mEventsBuffer = new LogBuffer(i);
        this.mDebugUid = -1;
        this.mLock = new Object();
    }

    static {
        MAX_LOG_SIZE = ActivityManager.isLowRamDeviceStatic() ? 100 : 400;
        MAX_NETWORK_BLOCKED_LOG_SIZE = ActivityManager.isLowRamDeviceStatic() ? 100 : 400;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void networkBlocked(int i, NetworkPolicyManagerService.UidBlockedState uidBlockedState) {
        synchronized (this.mLock) {
            if (LOGD || i == this.mDebugUid) {
                Slog.d(TAG, "Blocked state of " + i + ": " + uidBlockedState);
            }
            if (uidBlockedState == null) {
                this.mNetworkBlockedBuffer.networkBlocked(i, 0, 0, 0);
            } else {
                this.mNetworkBlockedBuffer.networkBlocked(i, uidBlockedState.blockedReasons, uidBlockedState.allowedReasons, uidBlockedState.effectiveBlockedReasons);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void uidStateChanged(int i, int i2, long j, int i3) {
        synchronized (this.mLock) {
            if (LOGV || i == this.mDebugUid) {
                Slog.v(TAG, i + " state changed to " + ProcessList.makeProcStateString(i2) + ",seq=" + j + ",cap=" + ActivityManager.getCapabilitiesSummary(i3));
            }
            this.mUidStateChangeBuffer.uidStateChanged(i, i2, j, i3);
        }
    }

    void event(String str) {
        synchronized (this.mLock) {
            if (LOGV) {
                Slog.v(TAG, str);
            }
            this.mEventsBuffer.event(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void uidPolicyChanged(int i, int i2, int i3) {
        synchronized (this.mLock) {
            if (LOGV || i == this.mDebugUid) {
                Slog.v(TAG, getPolicyChangedLog(i, i2, i3));
            }
            this.mEventsBuffer.uidPolicyChanged(i, i2, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void meterednessChanged(int i, boolean z) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getMeterednessChangedLog(i, z));
            }
            this.mEventsBuffer.meterednessChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removingUserState(int i) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getUserRemovedLog(i));
            }
            this.mEventsBuffer.userRemoved(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restrictBackgroundChanged(boolean z, boolean z2) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getRestrictBackgroundChangedLog(z, z2));
            }
            this.mEventsBuffer.restrictBackgroundChanged(z, z2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deviceIdleModeEnabled(boolean z) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getDeviceIdleModeEnabled(z));
            }
            this.mEventsBuffer.deviceIdleModeEnabled(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appIdleStateChanged(int i, boolean z) {
        synchronized (this.mLock) {
            if (LOGD || i == this.mDebugUid) {
                Slog.d(TAG, getAppIdleChangedLog(i, z));
            }
            this.mEventsBuffer.appIdleStateChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appIdleWlChanged(int i, boolean z) {
        synchronized (this.mLock) {
            if (LOGD || i == this.mDebugUid) {
                Slog.d(TAG, getAppIdleWlChangedLog(i, z));
            }
            this.mEventsBuffer.appIdleWlChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void paroleStateChanged(boolean z) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getParoleStateChanged(z));
            }
            this.mEventsBuffer.paroleStateChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void tempPowerSaveWlChanged(int i, boolean z, int i2, String str) {
        synchronized (this.mLock) {
            if (LOGV || i == UserHandle.getAppId(this.mDebugUid)) {
                Slog.v(TAG, getTempPowerSaveWlChangedLog(i, z, i2, str));
            }
            this.mEventsBuffer.tempPowerSaveWlChanged(i, z, i2, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void uidFirewallRuleChanged(int i, int i2, int i3) {
        synchronized (this.mLock) {
            if (LOGV || i2 == this.mDebugUid) {
                Slog.v(TAG, getUidFirewallRuleChangedLog(i, i2, i3));
            }
            this.mEventsBuffer.uidFirewallRuleChanged(i, i2, i3);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void firewallChainEnabled(int i, boolean z) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getFirewallChainEnabledLog(i, z));
            }
            this.mEventsBuffer.firewallChainEnabled(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void firewallRulesChanged(int i, int[] iArr, int[] iArr2) {
        synchronized (this.mLock) {
            String str = "Firewall rules changed for " + getFirewallChainName(i) + "; uids=" + Arrays.toString(iArr) + "; rules=" + Arrays.toString(iArr2);
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, str);
            }
            this.mEventsBuffer.event(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void meteredRestrictedPkgsChanged(Set<Integer> set) {
        synchronized (this.mLock) {
            String str = "Metered restricted uids: " + set;
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, str);
            }
            this.mEventsBuffer.event(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void meteredAllowlistChanged(int i, boolean z) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid == i) {
                Slog.d(TAG, getMeteredAllowlistChangedLog(i, z));
            }
            this.mEventsBuffer.meteredAllowlistChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void meteredDenylistChanged(int i, boolean z) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid == i) {
                Slog.d(TAG, getMeteredDenylistChangedLog(i, z));
            }
            this.mEventsBuffer.meteredDenylistChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void roamingChanged(int i, boolean z) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getRoamingChangedLog(i, z));
            }
            this.mEventsBuffer.roamingChanged(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void interfacesChanged(int i, ArraySet<String> arraySet) {
        synchronized (this.mLock) {
            if (LOGD || this.mDebugUid != -1) {
                Slog.d(TAG, getInterfacesChangedLog(i, arraySet.toString()));
            }
            this.mEventsBuffer.interfacesChanged(i, arraySet.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDebugUid(int i) {
        this.mDebugUid = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpLogs(IndentingPrintWriter indentingPrintWriter) {
        synchronized (this.mLock) {
            indentingPrintWriter.println();
            indentingPrintWriter.println("mEventLogs (most recent first):");
            indentingPrintWriter.increaseIndent();
            this.mEventsBuffer.reverseDump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.println("mNetworkBlockedLogs (most recent first):");
            indentingPrintWriter.increaseIndent();
            this.mNetworkBlockedBuffer.reverseDump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.println("mUidStateChangeLogs (most recent first):");
            indentingPrintWriter.increaseIndent();
            this.mUidStateChangeBuffer.reverseDump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPolicyChangedLog(int i, int i2, int i3) {
        return "Policy for " + i + " changed from " + NetworkPolicyManager.uidPoliciesToString(i2) + " to " + NetworkPolicyManager.uidPoliciesToString(i3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getMeterednessChangedLog(int i, boolean z) {
        return "Meteredness of netId=" + i + " changed to " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getUserRemovedLog(int i) {
        return "Remove state for u" + i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getRestrictBackgroundChangedLog(boolean z, boolean z2) {
        return "Changed restrictBackground: " + z + "->" + z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getDeviceIdleModeEnabled(boolean z) {
        return "DeviceIdleMode enabled: " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getAppIdleChangedLog(int i, boolean z) {
        return "App idle state of uid " + i + ": " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getAppIdleWlChangedLog(int i, boolean z) {
        return "App idle whitelist state of uid " + i + ": " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getParoleStateChanged(boolean z) {
        return "Parole state: " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getTempPowerSaveWlChangedLog(int i, boolean z, int i2, String str) {
        return "temp-power-save whitelist for " + i + " changed to: " + z + "; reason=" + PowerExemptionManager.reasonCodeToString(i2) + " <" + str + ">";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getUidFirewallRuleChangedLog(int i, int i2, int i3) {
        return String.format("Firewall rule changed: %d-%s-%s", Integer.valueOf(i2), getFirewallChainName(i), getFirewallRuleName(i3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getFirewallChainEnabledLog(int i, boolean z) {
        return "Firewall chain " + getFirewallChainName(i) + " state: " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getMeteredAllowlistChangedLog(int i, boolean z) {
        return "metered-allowlist for " + i + " changed to " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getMeteredDenylistChangedLog(int i, boolean z) {
        return "metered-denylist for " + i + " changed to " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getRoamingChangedLog(int i, boolean z) {
        return "Roaming of netId=" + i + " changed to " + z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getInterfacesChangedLog(int i, String str) {
        return "Interfaces of netId=" + i + " changed to " + str;
    }

    private static String getFirewallChainName(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? String.valueOf(i) : LowPowerStandbyController.DeviceConfigWrapper.NAMESPACE : "restricted" : "powersave" : "standby" : "dozable";
    }

    private static String getFirewallRuleName(int i) {
        return i != 0 ? i != 1 ? i != 2 ? String.valueOf(i) : "deny" : "allow" : HealthServiceWrapperHidl.INSTANCE_VENDOR;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class LogBuffer extends RingBuffer<Data> {
        private static final SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS");
        private static final Date sDate = new Date();

        public LogBuffer(int i) {
            super(Data.class, i);
        }

        public void uidStateChanged(int i, int i2, long j, int i3) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 2;
            data.ifield1 = i;
            data.ifield2 = i2;
            data.ifield3 = i3;
            data.lfield1 = j;
            data.timeStamp = System.currentTimeMillis();
        }

        public void event(String str) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 0;
            data.sfield1 = str;
            data.timeStamp = System.currentTimeMillis();
        }

        public void networkBlocked(int i, int i2, int i3, int i4) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 1;
            data.ifield1 = i;
            data.ifield2 = i2;
            data.ifield3 = i3;
            data.ifield4 = i4;
            data.timeStamp = System.currentTimeMillis();
        }

        public void uidPolicyChanged(int i, int i2, int i3) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 3;
            data.ifield1 = i;
            data.ifield2 = i2;
            data.ifield3 = i3;
            data.timeStamp = System.currentTimeMillis();
        }

        public void meterednessChanged(int i, boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 4;
            data.ifield1 = i;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void userRemoved(int i) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 5;
            data.ifield1 = i;
            data.timeStamp = System.currentTimeMillis();
        }

        public void restrictBackgroundChanged(boolean z, boolean z2) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 6;
            data.bfield1 = z;
            data.bfield2 = z2;
            data.timeStamp = System.currentTimeMillis();
        }

        public void deviceIdleModeEnabled(boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 7;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void appIdleStateChanged(int i, boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 8;
            data.ifield1 = i;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void appIdleWlChanged(int i, boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 14;
            data.ifield1 = i;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void paroleStateChanged(boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 9;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void tempPowerSaveWlChanged(int i, boolean z, int i2, String str) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 10;
            data.ifield1 = i;
            data.ifield2 = i2;
            data.bfield1 = z;
            data.sfield1 = str;
            data.timeStamp = System.currentTimeMillis();
        }

        public void uidFirewallRuleChanged(int i, int i2, int i3) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 11;
            data.ifield1 = i;
            data.ifield2 = i2;
            data.ifield3 = i3;
            data.timeStamp = System.currentTimeMillis();
        }

        public void firewallChainEnabled(int i, boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 12;
            data.ifield1 = i;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void meteredAllowlistChanged(int i, boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 15;
            data.ifield1 = i;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void meteredDenylistChanged(int i, boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 16;
            data.ifield1 = i;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void roamingChanged(int i, boolean z) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 17;
            data.ifield1 = i;
            data.bfield1 = z;
            data.timeStamp = System.currentTimeMillis();
        }

        public void interfacesChanged(int i, String str) {
            Data data = (Data) getNextSlot();
            if (data == null) {
                return;
            }
            data.reset();
            data.type = 18;
            data.ifield1 = i;
            data.sfield1 = str;
            data.timeStamp = System.currentTimeMillis();
        }

        public void reverseDump(IndentingPrintWriter indentingPrintWriter) {
            Data[] dataArr = (Data[]) toArray();
            for (int length = dataArr.length - 1; length >= 0; length--) {
                Data data = dataArr[length];
                if (data == null) {
                    indentingPrintWriter.println("NULL");
                } else {
                    indentingPrintWriter.print(formatDate(data.timeStamp));
                    indentingPrintWriter.print(" - ");
                    indentingPrintWriter.println(getContent(dataArr[length]));
                }
            }
        }

        public String getContent(Data data) {
            int i = data.type;
            switch (i) {
                case 0:
                    return data.sfield1;
                case 1:
                    return data.ifield1 + "-" + NetworkPolicyManagerService.UidBlockedState.toString(data.ifield2, data.ifield3, data.ifield4);
                case 2:
                    return data.ifield1 + ":" + ProcessList.makeProcStateString(data.ifield2) + ":" + ActivityManager.getCapabilitiesSummary(data.ifield3) + ":" + data.lfield1;
                case 3:
                    return NetworkPolicyLogger.getPolicyChangedLog(data.ifield1, data.ifield2, data.ifield3);
                case 4:
                    return NetworkPolicyLogger.getMeterednessChangedLog(data.ifield1, data.bfield1);
                case 5:
                    return NetworkPolicyLogger.getUserRemovedLog(data.ifield1);
                case 6:
                    return NetworkPolicyLogger.getRestrictBackgroundChangedLog(data.bfield1, data.bfield2);
                case 7:
                    return NetworkPolicyLogger.getDeviceIdleModeEnabled(data.bfield1);
                case 8:
                    return NetworkPolicyLogger.getAppIdleChangedLog(data.ifield1, data.bfield1);
                case 9:
                    return NetworkPolicyLogger.getParoleStateChanged(data.bfield1);
                case 10:
                    return NetworkPolicyLogger.getTempPowerSaveWlChangedLog(data.ifield1, data.bfield1, data.ifield2, data.sfield1);
                case 11:
                    return NetworkPolicyLogger.getUidFirewallRuleChangedLog(data.ifield1, data.ifield2, data.ifield3);
                case 12:
                    return NetworkPolicyLogger.getFirewallChainEnabledLog(data.ifield1, data.bfield1);
                case 13:
                default:
                    return String.valueOf(i);
                case 14:
                    return NetworkPolicyLogger.getAppIdleWlChangedLog(data.ifield1, data.bfield1);
                case 15:
                    return NetworkPolicyLogger.getMeteredAllowlistChangedLog(data.ifield1, data.bfield1);
                case 16:
                    return NetworkPolicyLogger.getMeteredDenylistChangedLog(data.ifield1, data.bfield1);
                case 17:
                    return NetworkPolicyLogger.getRoamingChangedLog(data.ifield1, data.bfield1);
                case 18:
                    return NetworkPolicyLogger.getInterfacesChangedLog(data.ifield1, data.sfield1);
            }
        }

        private String formatDate(long j) {
            Date date = sDate;
            date.setTime(j);
            return sFormatter.format(date);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Data {
        public boolean bfield1;
        public boolean bfield2;
        public int ifield1;
        public int ifield2;
        public int ifield3;
        public int ifield4;
        public long lfield1;
        public String sfield1;
        public long timeStamp;
        public int type;

        public void reset() {
            this.sfield1 = null;
        }
    }
}
