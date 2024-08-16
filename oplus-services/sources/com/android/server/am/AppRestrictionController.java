package com.android.server.am;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppOpsManager;
import android.app.IActivityManager;
import android.app.IUidObserver;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.UidObserver;
import android.app.role.OnRoleHoldersChangedListener;
import android.app.role.RoleManager;
import android.app.usage.AppStandbyInfo;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ModuleInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManagerInternal;
import android.graphics.drawable.Icon;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerExemptionManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseArrayMap;
import android.util.TimeUtils;
import android.util.Xml;
import android.util.proto.ProtoOutputStream;
import com.android.bluetooth.BluetoothStatsLog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.notification.SystemNotificationChannels;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.function.TriConsumer;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.AppStateTracker;
import com.android.server.LocalServices;
import com.android.server.SystemConfig;
import com.android.server.am.AppBatteryTracker;
import com.android.server.am.AppRestrictionController;
import com.android.server.am.IAppRestrictionControllerExt;
import com.android.server.apphibernation.AppHibernationManagerInternal;
import com.android.server.pm.UserManagerInternal;
import com.android.server.usage.AppStandbyInternal;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppRestrictionController {
    private static final String APP_RESTRICTION_SETTINGS_DIRNAME = "apprestriction";
    private static final String APP_RESTRICTION_SETTINGS_FILENAME = "settings.xml";
    private static final String ATTR_CUR_LEVEL = "curlevel";
    private static final String ATTR_LEVEL_TS = "levelts";
    private static final String ATTR_PACKAGE = "package";
    private static final String ATTR_REASON = "reason";
    private static final String ATTR_UID = "uid";
    static final boolean DEBUG_BG_RESTRICTION_CONTROLLER = false;
    static final String DEVICE_CONFIG_SUBNAMESPACE_PREFIX = "bg_";
    private static final boolean ENABLE_SHOW_FGS_MANAGER_ACTION_ON_BG_RESTRICTION = false;
    private static final boolean ENABLE_SHOW_FOREGROUND_SERVICE_MANAGER = true;
    private static final String[] ROLES_IN_INTEREST = {"android.app.role.DIALER", "android.app.role.EMERGENCY"};
    static final int STOCK_PM_FLAGS = 819200;
    static final String TAG = "ActivityManager";
    private static final String TAG_SETTINGS = "settings";
    static final int TRACKER_TYPE_BATTERY = 1;
    static final int TRACKER_TYPE_BATTERY_EXEMPTION = 2;
    static final int TRACKER_TYPE_BIND_SERVICE_EVENTS = 7;
    static final int TRACKER_TYPE_BROADCAST_EVENTS = 6;
    static final int TRACKER_TYPE_FGS = 3;
    static final int TRACKER_TYPE_MEDIA_SESSION = 4;
    static final int TRACKER_TYPE_PERMISSION = 5;
    static final int TRACKER_TYPE_UNKNOWN = 0;

    @GuardedBy({"mSettingsLock"})
    private final SparseArrayMap<String, Runnable> mActiveUids;
    final ActivityManagerService mActivityManagerService;
    private final AppStandbyInternal.AppIdleStateChangeListener mAppIdleStateChangeListener;
    private IAppRestrictionControllerExt mAppRestrictionControllerExt;
    private AppRestrictionControllerWrapper mAppRestrictionControllerWrapper;
    private final ArrayList<BaseAppStateTracker> mAppStateTrackers;
    private final AppStateTracker.BackgroundRestrictedAppListener mBackgroundRestrictionListener;
    private final HandlerExecutor mBgExecutor;
    private final BgHandler mBgHandler;
    private final HandlerThread mBgHandlerThread;
    ArraySet<String> mBgRestrictionExemptioFromSysConfig;
    private final BroadcastReceiver mBootReceiver;
    private final BroadcastReceiver mBroadcastReceiver;

    @GuardedBy({"mCarrierPrivilegedLock"})
    private final SparseArray<Set<String>> mCarrierPrivilegedApps;
    private final Object mCarrierPrivilegedLock;
    private volatile ArrayList<PhoneCarrierPrivilegesCallback> mCarrierPrivilegesCallbacks;
    private final ConstantsObserver mConstantsObserver;
    private final Context mContext;
    private int[] mDeviceIdleAllowlist;
    private int[] mDeviceIdleExceptIdleAllowlist;
    private final TrackerInfo mEmptyTrackerInfo;
    private final Injector mInjector;
    private final Object mLock;
    private final NotificationHelper mNotificationHelper;
    private final CopyOnWriteArraySet<ActivityManagerInternal.AppBackgroundRestrictionListener> mRestrictionListeners;

    @GuardedBy({"mSettingsLock"})
    @VisibleForTesting
    final RestrictionSettings mRestrictionSettings;
    private final AtomicBoolean mRestrictionSettingsXmlLoaded;
    private final OnRoleHoldersChangedListener mRoleHolderChangedListener;
    private final Object mSettingsLock;
    private IAppRestrictionControllerExt.IStaticExt mStaticExt;
    private final ArraySet<Integer> mSystemDeviceIdleAllowlist;
    private final ArraySet<Integer> mSystemDeviceIdleExceptIdleAllowlist;

    @GuardedBy({"mLock"})
    private final HashMap<String, Boolean> mSystemModulesCache;
    private final ArrayList<Runnable> mTmpRunnables;
    private final IUidObserver mUidObserver;

    @GuardedBy({"mLock"})
    private final SparseArray<ArrayList<String>> mUidRolesMapping;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface TrackerType {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface UidBatteryUsageProvider {
        AppBatteryTracker.ImmutableBatteryUsage getUidBatteryUsage(int i);
    }

    private int getOptimizationLevelStatsd(int i) {
        if (i == 10) {
            return 3;
        }
        if (i != 30) {
            return i != 50 ? 0 : 2;
        }
        return 1;
    }

    private int getRestrictionLevelStatsd(int i) {
        if (i == 10) {
            return 1;
        }
        if (i == 20) {
            return 2;
        }
        if (i == 30) {
            return 3;
        }
        if (i == 40) {
            return 4;
        }
        if (i != 50) {
            return i != 60 ? 0 : 6;
        }
        return 5;
    }

    private int getThresholdStatsd(int i) {
        if (i != 1024) {
            return i != 1536 ? 0 : 1;
        }
        return 2;
    }

    private int getTrackerTypeStatsd(@TrackerType int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            default:
                return 0;
        }
    }

    private static int standbyBucketToRestrictionLevel(int i) {
        if (i == 5) {
            return 20;
        }
        if (i != 10 && i != 20 && i != 30) {
            int i2 = 40;
            if (i != 40) {
                if (i != 45) {
                    i2 = 50;
                    if (i != 50) {
                        return 0;
                    }
                }
                return i2;
            }
        }
        return 30;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class RestrictionSettings {

        @GuardedBy({"mSettingsLock"})
        final SparseArrayMap<String, PkgSettings> mRestrictionLevels = new SparseArrayMap<>();

        RestrictionSettings() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public final class PkgSettings {
            private long[] mLastNotificationShownTime;
            private long mLevelChangeTime;
            private int[] mNotificationId;
            private final String mPackageName;
            private int mReason;
            private final int mUid;
            private int mLastRestrictionLevel = 0;
            private int mCurrentRestrictionLevel = 0;

            PkgSettings(String str, int i) {
                this.mPackageName = str;
                this.mUid = i;
            }

            @GuardedBy({"mSettingsLock"})
            int update(int i, int i2, int i3) {
                int i4 = this.mCurrentRestrictionLevel;
                if (i != i4) {
                    this.mLastRestrictionLevel = i4;
                    this.mCurrentRestrictionLevel = i;
                    this.mLevelChangeTime = AppRestrictionController.this.mInjector.currentTimeMillis();
                    this.mReason = (i2 & BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_COMMAND__CMD_PAIRING_COMPLETE) | (i3 & 255);
                    AppRestrictionController.this.mBgHandler.obtainMessage(1, this.mUid, i, this.mPackageName).sendToTarget();
                }
                return this.mLastRestrictionLevel;
            }

            @GuardedBy({"mSettingsLock"})
            public String toString() {
                StringBuilder sb = new StringBuilder(128);
                sb.append("RestrictionLevel{");
                sb.append(Integer.toHexString(System.identityHashCode(this)));
                sb.append(':');
                sb.append(this.mPackageName);
                sb.append('/');
                sb.append(UserHandle.formatUid(this.mUid));
                sb.append('}');
                sb.append(' ');
                sb.append(ActivityManager.restrictionLevelToName(this.mCurrentRestrictionLevel));
                sb.append('(');
                sb.append(UsageStatsManager.reasonToString(this.mReason));
                sb.append(')');
                return sb.toString();
            }

            void dump(PrintWriter printWriter, long j) {
                synchronized (AppRestrictionController.this.mSettingsLock) {
                    printWriter.print(toString());
                    if (this.mLastRestrictionLevel != 0) {
                        printWriter.print('/');
                        printWriter.print(ActivityManager.restrictionLevelToName(this.mLastRestrictionLevel));
                    }
                    printWriter.print(" levelChange=");
                    TimeUtils.formatDuration(this.mLevelChangeTime - j, printWriter);
                    if (this.mLastNotificationShownTime != null) {
                        int i = 0;
                        while (true) {
                            long[] jArr = this.mLastNotificationShownTime;
                            if (i >= jArr.length) {
                                break;
                            }
                            if (jArr[i] > 0) {
                                printWriter.print(" lastNoti(");
                                NotificationHelper unused = AppRestrictionController.this.mNotificationHelper;
                                printWriter.print(NotificationHelper.notificationTypeToString(i));
                                printWriter.print(")=");
                                TimeUtils.formatDuration(this.mLastNotificationShownTime[i] - j, printWriter);
                            }
                            i++;
                        }
                    }
                }
                printWriter.print(" effectiveExemption=");
                printWriter.print(PowerExemptionManager.reasonCodeToString(AppRestrictionController.this.getBackgroundRestrictionExemptionReason(this.mUid)));
            }

            String getPackageName() {
                return this.mPackageName;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public int getUid() {
                return this.mUid;
            }

            @GuardedBy({"mSettingsLock"})
            int getCurrentRestrictionLevel() {
                return this.mCurrentRestrictionLevel;
            }

            @GuardedBy({"mSettingsLock"})
            int getLastRestrictionLevel() {
                return this.mLastRestrictionLevel;
            }

            @GuardedBy({"mSettingsLock"})
            int getReason() {
                return this.mReason;
            }

            @GuardedBy({"mSettingsLock"})
            long getLastNotificationTime(int i) {
                long[] jArr = this.mLastNotificationShownTime;
                if (jArr == null) {
                    return 0L;
                }
                return jArr[i];
            }

            @GuardedBy({"mSettingsLock"})
            void setLastNotificationTime(int i, long j) {
                setLastNotificationTime(i, j, true);
            }

            @GuardedBy({"mSettingsLock"})
            @VisibleForTesting
            void setLastNotificationTime(int i, long j, boolean z) {
                if (this.mLastNotificationShownTime == null) {
                    this.mLastNotificationShownTime = new long[2];
                }
                this.mLastNotificationShownTime[i] = j;
                if (z && AppRestrictionController.this.mRestrictionSettingsXmlLoaded.get()) {
                    RestrictionSettings.this.schedulePersistToXml(UserHandle.getUserId(this.mUid));
                }
            }

            @GuardedBy({"mSettingsLock"})
            int getNotificationId(int i) {
                int[] iArr = this.mNotificationId;
                if (iArr == null) {
                    return 0;
                }
                return iArr[i];
            }

            @GuardedBy({"mSettingsLock"})
            void setNotificationId(int i, int i2) {
                if (this.mNotificationId == null) {
                    this.mNotificationId = new int[2];
                }
                this.mNotificationId[i] = i2;
            }

            @GuardedBy({"mSettingsLock"})
            @VisibleForTesting
            void setLevelChangeTime(long j) {
                this.mLevelChangeTime = j;
            }

            @GuardedBy({"mSettingsLock"})
            public Object clone() {
                PkgSettings pkgSettings = new PkgSettings(this.mPackageName, this.mUid);
                pkgSettings.mCurrentRestrictionLevel = this.mCurrentRestrictionLevel;
                pkgSettings.mLastRestrictionLevel = this.mLastRestrictionLevel;
                pkgSettings.mLevelChangeTime = this.mLevelChangeTime;
                pkgSettings.mReason = this.mReason;
                long[] jArr = this.mLastNotificationShownTime;
                if (jArr != null) {
                    pkgSettings.mLastNotificationShownTime = Arrays.copyOf(jArr, jArr.length);
                }
                int[] iArr = this.mNotificationId;
                if (iArr != null) {
                    pkgSettings.mNotificationId = Arrays.copyOf(iArr, iArr.length);
                }
                return pkgSettings;
            }

            @GuardedBy({"mSettingsLock"})
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (obj == null || !(obj instanceof PkgSettings)) {
                    return false;
                }
                PkgSettings pkgSettings = (PkgSettings) obj;
                return pkgSettings.mUid == this.mUid && pkgSettings.mCurrentRestrictionLevel == this.mCurrentRestrictionLevel && pkgSettings.mLastRestrictionLevel == this.mLastRestrictionLevel && pkgSettings.mLevelChangeTime == this.mLevelChangeTime && pkgSettings.mReason == this.mReason && TextUtils.equals(pkgSettings.mPackageName, this.mPackageName) && Arrays.equals(pkgSettings.mLastNotificationShownTime, this.mLastNotificationShownTime) && Arrays.equals(pkgSettings.mNotificationId, this.mNotificationId);
            }
        }

        int update(String str, int i, int i2, int i3, int i4) {
            int update;
            synchronized (AppRestrictionController.this.mSettingsLock) {
                PkgSettings restrictionSettingsLocked = getRestrictionSettingsLocked(i, str);
                if (restrictionSettingsLocked == null) {
                    restrictionSettingsLocked = new PkgSettings(str, i);
                    this.mRestrictionLevels.add(i, str, restrictionSettingsLocked);
                }
                update = restrictionSettingsLocked.update(i2, i3, i4);
            }
            return update;
        }

        int getReason(String str, int i) {
            int reason;
            synchronized (AppRestrictionController.this.mSettingsLock) {
                PkgSettings pkgSettings = (PkgSettings) this.mRestrictionLevels.get(i, str);
                reason = pkgSettings != null ? pkgSettings.getReason() : 256;
            }
            return reason;
        }

        int getRestrictionLevel(int i) {
            synchronized (AppRestrictionController.this.mSettingsLock) {
                int indexOfKey = this.mRestrictionLevels.indexOfKey(i);
                if (indexOfKey < 0) {
                    return 0;
                }
                int numElementsForKeyAt = this.mRestrictionLevels.numElementsForKeyAt(indexOfKey);
                if (numElementsForKeyAt == 0) {
                    return 0;
                }
                int i2 = 0;
                for (int i3 = 0; i3 < numElementsForKeyAt; i3++) {
                    PkgSettings pkgSettings = (PkgSettings) this.mRestrictionLevels.valueAt(indexOfKey, i3);
                    if (pkgSettings != null) {
                        int currentRestrictionLevel = pkgSettings.getCurrentRestrictionLevel();
                        if (i2 != 0) {
                            currentRestrictionLevel = Math.min(i2, currentRestrictionLevel);
                        }
                        i2 = currentRestrictionLevel;
                    }
                }
                return i2;
            }
        }

        int getRestrictionLevel(int i, String str) {
            int restrictionLevel;
            synchronized (AppRestrictionController.this.mSettingsLock) {
                PkgSettings restrictionSettingsLocked = getRestrictionSettingsLocked(i, str);
                restrictionLevel = restrictionSettingsLocked == null ? getRestrictionLevel(i) : restrictionSettingsLocked.getCurrentRestrictionLevel();
            }
            return restrictionLevel;
        }

        int getRestrictionLevel(String str, int i) {
            return getRestrictionLevel(AppRestrictionController.this.mInjector.getPackageManagerInternal().getPackageUid(str, 819200L, i), str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLastRestrictionLevel(int i, String str) {
            int lastRestrictionLevel;
            synchronized (AppRestrictionController.this.mSettingsLock) {
                PkgSettings pkgSettings = (PkgSettings) this.mRestrictionLevels.get(i, str);
                lastRestrictionLevel = pkgSettings == null ? 0 : pkgSettings.getLastRestrictionLevel();
            }
            return lastRestrictionLevel;
        }

        @GuardedBy({"mSettingsLock"})
        void forEachPackageInUidLocked(int i, TriConsumer<String, Integer, Integer> triConsumer) {
            int indexOfKey = this.mRestrictionLevels.indexOfKey(i);
            if (indexOfKey < 0) {
                return;
            }
            int numElementsForKeyAt = this.mRestrictionLevels.numElementsForKeyAt(indexOfKey);
            for (int i2 = 0; i2 < numElementsForKeyAt; i2++) {
                PkgSettings pkgSettings = (PkgSettings) this.mRestrictionLevels.valueAt(indexOfKey, i2);
                triConsumer.accept((String) this.mRestrictionLevels.keyAt(indexOfKey, i2), Integer.valueOf(pkgSettings.getCurrentRestrictionLevel()), Integer.valueOf(pkgSettings.getReason()));
            }
        }

        @GuardedBy({"mSettingsLock"})
        void forEachUidLocked(Consumer<Integer> consumer) {
            for (int numMaps = this.mRestrictionLevels.numMaps() - 1; numMaps >= 0; numMaps--) {
                consumer.accept(Integer.valueOf(this.mRestrictionLevels.keyAt(numMaps)));
            }
        }

        @GuardedBy({"mSettingsLock"})
        PkgSettings getRestrictionSettingsLocked(int i, String str) {
            return (PkgSettings) this.mRestrictionLevels.get(i, str);
        }

        void removeUser(int i) {
            synchronized (AppRestrictionController.this.mSettingsLock) {
                for (int numMaps = this.mRestrictionLevels.numMaps() - 1; numMaps >= 0; numMaps--) {
                    if (UserHandle.getUserId(this.mRestrictionLevels.keyAt(numMaps)) == i) {
                        this.mRestrictionLevels.deleteAt(numMaps);
                    }
                }
            }
        }

        void removePackage(String str, int i) {
            removePackage(str, i, true);
        }

        void removePackage(String str, int i, boolean z) {
            synchronized (AppRestrictionController.this.mSettingsLock) {
                int indexOfKey = this.mRestrictionLevels.indexOfKey(i);
                this.mRestrictionLevels.delete(i, str);
                if (indexOfKey >= 0 && this.mRestrictionLevels.numElementsForKeyAt(indexOfKey) == 0) {
                    this.mRestrictionLevels.deleteAt(indexOfKey);
                }
            }
            if (z && AppRestrictionController.this.mRestrictionSettingsXmlLoaded.get()) {
                schedulePersistToXml(UserHandle.getUserId(i));
            }
        }

        void removeUid(int i) {
            removeUid(i, true);
        }

        void removeUid(int i, boolean z) {
            synchronized (AppRestrictionController.this.mSettingsLock) {
                this.mRestrictionLevels.delete(i);
            }
            if (z && AppRestrictionController.this.mRestrictionSettingsXmlLoaded.get()) {
                schedulePersistToXml(UserHandle.getUserId(i));
            }
        }

        @VisibleForTesting
        void reset() {
            synchronized (AppRestrictionController.this.mSettingsLock) {
                for (int numMaps = this.mRestrictionLevels.numMaps() - 1; numMaps >= 0; numMaps--) {
                    this.mRestrictionLevels.deleteAt(numMaps);
                }
            }
        }

        @VisibleForTesting
        void resetToDefault() {
            synchronized (AppRestrictionController.this.mSettingsLock) {
                this.mRestrictionLevels.forEach(new Consumer() { // from class: com.android.server.am.AppRestrictionController$RestrictionSettings$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        AppRestrictionController.RestrictionSettings.lambda$resetToDefault$0((AppRestrictionController.RestrictionSettings.PkgSettings) obj);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$resetToDefault$0(PkgSettings pkgSettings) {
            pkgSettings.mCurrentRestrictionLevel = 0;
            pkgSettings.mLastRestrictionLevel = 0;
            pkgSettings.mLevelChangeTime = 0L;
            pkgSettings.mReason = 256;
            if (pkgSettings.mLastNotificationShownTime != null) {
                for (int i = 0; i < pkgSettings.mLastNotificationShownTime.length; i++) {
                    pkgSettings.mLastNotificationShownTime[i] = 0;
                }
            }
        }

        void dump(PrintWriter printWriter, String str) {
            final ArrayList arrayList = new ArrayList();
            synchronized (AppRestrictionController.this.mSettingsLock) {
                this.mRestrictionLevels.forEach(new Consumer() { // from class: com.android.server.am.AppRestrictionController$RestrictionSettings$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        arrayList.add((AppRestrictionController.RestrictionSettings.PkgSettings) obj);
                    }
                });
            }
            Collections.sort(arrayList, Comparator.comparingInt(new ToIntFunction() { // from class: com.android.server.am.AppRestrictionController$RestrictionSettings$$ExternalSyntheticLambda2
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((AppRestrictionController.RestrictionSettings.PkgSettings) obj).getUid();
                }
            }));
            long currentTimeMillis = AppRestrictionController.this.mInjector.currentTimeMillis();
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                printWriter.print(str);
                printWriter.print('#');
                printWriter.print(i);
                printWriter.print(' ');
                ((PkgSettings) arrayList.get(i)).dump(printWriter, currentTimeMillis);
                printWriter.println();
            }
        }

        @VisibleForTesting
        void schedulePersistToXml(int i) {
            AppRestrictionController.this.mBgHandler.obtainMessage(11, i, 0).sendToTarget();
        }

        @VisibleForTesting
        void scheduleLoadFromXml() {
            AppRestrictionController.this.mBgHandler.sendEmptyMessage(10);
        }

        @VisibleForTesting
        File getXmlFileNameForUser(int i) {
            return new File(new File(AppRestrictionController.this.mInjector.getDataSystemDeDirectory(i), AppRestrictionController.APP_RESTRICTION_SETTINGS_DIRNAME), AppRestrictionController.APP_RESTRICTION_SETTINGS_FILENAME);
        }

        @VisibleForTesting
        void loadFromXml(boolean z) {
            for (int i : AppRestrictionController.this.mInjector.getUserManagerInternal().getUserIds()) {
                loadFromXml(i, z);
            }
            AppRestrictionController.this.mRestrictionSettingsXmlLoaded.set(true);
        }

        void loadFromXml(int i, boolean z) {
            File xmlFileNameForUser = getXmlFileNameForUser(i);
            if (!xmlFileNameForUser.exists()) {
                return;
            }
            long[] jArr = new long[2];
            try {
                try {
                    FileInputStream fileInputStream = new FileInputStream(xmlFileNameForUser);
                    try {
                        TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        while (true) {
                            int next = resolvePullParser.next();
                            if (next == 1) {
                                fileInputStream.close();
                                return;
                            }
                            if (next == 2) {
                                String name = resolvePullParser.getName();
                                if (!AppRestrictionController.TAG_SETTINGS.equals(name)) {
                                    Slog.w("ActivityManager", "Unexpected tag name: " + name);
                                } else {
                                    loadOneFromXml(resolvePullParser, elapsedRealtime, jArr, z);
                                }
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (IOException | XmlPullParserException unused) {
                }
            } catch (ArrayIndexOutOfBoundsException unused2) {
                Slog.e("ActivityManager", "loadFromXml trigger ArrayIndexOutOfBoundsException");
            }
        }

        private void loadOneFromXml(TypedXmlPullParser typedXmlPullParser, long j, long[] jArr, boolean z) {
            char c;
            for (int i = 0; i < jArr.length; i++) {
                jArr[i] = 0;
            }
            String str = null;
            int i2 = 256;
            long j2 = 0;
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < typedXmlPullParser.getAttributeCount(); i5++) {
                try {
                    String attributeName = typedXmlPullParser.getAttributeName(i5);
                    String attributeValue = typedXmlPullParser.getAttributeValue(i5);
                    switch (attributeName.hashCode()) {
                        case -934964668:
                            if (attributeName.equals(AppRestrictionController.ATTR_REASON)) {
                                c = 4;
                                break;
                            }
                            break;
                        case -807062458:
                            if (attributeName.equals(AppRestrictionController.ATTR_PACKAGE)) {
                                c = 1;
                                break;
                            }
                            break;
                        case 115792:
                            if (attributeName.equals(AppRestrictionController.ATTR_UID)) {
                                c = 0;
                                break;
                            }
                            break;
                        case 69785859:
                            if (attributeName.equals(AppRestrictionController.ATTR_LEVEL_TS)) {
                                c = 3;
                                break;
                            }
                            break;
                        case 569868612:
                            if (attributeName.equals(AppRestrictionController.ATTR_CUR_LEVEL)) {
                                c = 2;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    if (c == 0) {
                        i3 = Integer.parseInt(attributeValue);
                    } else if (c == 1) {
                        str = attributeValue;
                    } else if (c == 2) {
                        i4 = Integer.parseInt(attributeValue);
                    } else if (c == 3) {
                        j2 = Long.parseLong(attributeValue);
                    } else if (c == 4) {
                        i2 = Integer.parseInt(attributeValue);
                    } else {
                        jArr[NotificationHelper.notificationTimeAttrToType(attributeName)] = Long.parseLong(attributeValue);
                    }
                } catch (IllegalArgumentException unused) {
                }
            }
            if (i3 != 0) {
                synchronized (AppRestrictionController.this.mSettingsLock) {
                    PkgSettings restrictionSettingsLocked = getRestrictionSettingsLocked(i3, str);
                    if (restrictionSettingsLocked == null) {
                        return;
                    }
                    for (int i6 = 0; i6 < jArr.length; i6++) {
                        if (restrictionSettingsLocked.getLastNotificationTime(i6) == 0) {
                            long j3 = jArr[i6];
                            if (j3 != 0) {
                                restrictionSettingsLocked.setLastNotificationTime(i6, j3, false);
                            }
                        }
                    }
                    if (restrictionSettingsLocked.mCurrentRestrictionLevel >= i4) {
                        return;
                    }
                    long j4 = j2;
                    int appStandbyBucket = AppRestrictionController.this.mInjector.getAppStandbyInternal().getAppStandbyBucket(str, UserHandle.getUserId(i3), j, false);
                    if (z) {
                        AppRestrictionController appRestrictionController = AppRestrictionController.this;
                        appRestrictionController.applyRestrictionLevel(str, i3, i4, appRestrictionController.mEmptyTrackerInfo, appStandbyBucket, true, 65280 & i2, i2 & 255);
                    } else {
                        restrictionSettingsLocked.update(i4, 65280 & i2, i2 & 255);
                    }
                    synchronized (AppRestrictionController.this.mSettingsLock) {
                        restrictionSettingsLocked.setLevelChangeTime(j4);
                    }
                }
            }
        }

        @VisibleForTesting
        void persistToXml(int i) {
            FileOutputStream fileOutputStream;
            File xmlFileNameForUser = getXmlFileNameForUser(i);
            File parentFile = xmlFileNameForUser.getParentFile();
            if (!parentFile.isDirectory() && !parentFile.mkdirs()) {
                Slog.w("ActivityManager", "Failed to create folder for " + i);
                return;
            }
            AtomicFile atomicFile = new AtomicFile(xmlFileNameForUser);
            try {
                fileOutputStream = atomicFile.startWrite();
            } catch (Exception e) {
                e = e;
                fileOutputStream = null;
            }
            try {
                fileOutputStream.write(toXmlByteArray(i));
                atomicFile.finishWrite(fileOutputStream);
            } catch (Exception e2) {
                e = e2;
                Slog.e("ActivityManager", "Failed to write file " + xmlFileNameForUser, e);
                if (fileOutputStream != null) {
                    atomicFile.failWrite(fileOutputStream);
                }
            }
        }

        private byte[] toXmlByteArray(int i) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(byteArrayOutputStream);
                    resolveSerializer.startDocument((String) null, Boolean.TRUE);
                    synchronized (AppRestrictionController.this.mSettingsLock) {
                        for (int numMaps = this.mRestrictionLevels.numMaps() - 1; numMaps >= 0; numMaps--) {
                            for (int numElementsForKeyAt = this.mRestrictionLevels.numElementsForKeyAt(numMaps) - 1; numElementsForKeyAt >= 0; numElementsForKeyAt--) {
                                PkgSettings pkgSettings = (PkgSettings) this.mRestrictionLevels.valueAt(numMaps, numElementsForKeyAt);
                                int uid = pkgSettings.getUid();
                                if (UserHandle.getUserId(uid) == i) {
                                    resolveSerializer.startTag((String) null, AppRestrictionController.TAG_SETTINGS);
                                    resolveSerializer.attributeInt((String) null, AppRestrictionController.ATTR_UID, uid);
                                    resolveSerializer.attribute((String) null, AppRestrictionController.ATTR_PACKAGE, pkgSettings.getPackageName());
                                    resolveSerializer.attributeInt((String) null, AppRestrictionController.ATTR_CUR_LEVEL, pkgSettings.mCurrentRestrictionLevel);
                                    resolveSerializer.attributeLong((String) null, AppRestrictionController.ATTR_LEVEL_TS, pkgSettings.mLevelChangeTime);
                                    resolveSerializer.attributeInt((String) null, AppRestrictionController.ATTR_REASON, pkgSettings.mReason);
                                    for (int i2 = 0; i2 < 2; i2++) {
                                        resolveSerializer.attributeLong((String) null, NotificationHelper.notificationTypeToTimeAttr(i2), pkgSettings.getLastNotificationTime(i2));
                                    }
                                    resolveSerializer.endTag((String) null, AppRestrictionController.TAG_SETTINGS);
                                }
                            }
                        }
                    }
                    resolveSerializer.endDocument();
                    resolveSerializer.flush();
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    return byteArray;
                } finally {
                }
            } catch (IOException unused) {
                return null;
            }
        }

        @VisibleForTesting
        void removeXml() {
            for (int i : AppRestrictionController.this.mInjector.getUserManagerInternal().getUserIds()) {
                getXmlFileNameForUser(i).delete();
            }
        }

        public Object clone() {
            RestrictionSettings restrictionSettings = new RestrictionSettings();
            synchronized (AppRestrictionController.this.mSettingsLock) {
                for (int numMaps = this.mRestrictionLevels.numMaps() - 1; numMaps >= 0; numMaps--) {
                    for (int numElementsForKeyAt = this.mRestrictionLevels.numElementsForKeyAt(numMaps) - 1; numElementsForKeyAt >= 0; numElementsForKeyAt--) {
                        restrictionSettings.mRestrictionLevels.add(this.mRestrictionLevels.keyAt(numMaps), (String) this.mRestrictionLevels.keyAt(numMaps, numElementsForKeyAt), (PkgSettings) ((PkgSettings) this.mRestrictionLevels.valueAt(numMaps, numElementsForKeyAt)).clone());
                    }
                }
            }
            return restrictionSettings;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || !(obj instanceof RestrictionSettings)) {
                return false;
            }
            SparseArrayMap<String, PkgSettings> sparseArrayMap = ((RestrictionSettings) obj).mRestrictionLevels;
            synchronized (AppRestrictionController.this.mSettingsLock) {
                if (sparseArrayMap.numMaps() != this.mRestrictionLevels.numMaps()) {
                    return false;
                }
                for (int numMaps = this.mRestrictionLevels.numMaps() - 1; numMaps >= 0; numMaps--) {
                    int keyAt = this.mRestrictionLevels.keyAt(numMaps);
                    if (sparseArrayMap.numElementsForKey(keyAt) != this.mRestrictionLevels.numElementsForKeyAt(numMaps)) {
                        return false;
                    }
                    for (int numElementsForKeyAt = this.mRestrictionLevels.numElementsForKeyAt(numMaps) - 1; numElementsForKeyAt >= 0; numElementsForKeyAt--) {
                        PkgSettings pkgSettings = (PkgSettings) this.mRestrictionLevels.valueAt(numMaps, numElementsForKeyAt);
                        if (!pkgSettings.equals(sparseArrayMap.get(keyAt, pkgSettings.getPackageName()))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class ConstantsObserver implements DeviceConfig.OnPropertiesChangedListener {
        static final long DEFAULT_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL_MS = 2592000000L;
        static final boolean DEFAULT_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION = false;
        static final boolean DEFAULT_BG_AUTO_RESTRICT_ABUSIVE_APPS = true;
        static final long DEFAULT_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL_MS = 2592000000L;
        static final boolean DEFAULT_BG_PROMPT_FGS_ON_LONG_RUNNING = false;
        static final boolean DEFAULT_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING = false;
        static final String KEY_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL = "bg_abusive_notification_minimal_interval";
        static final String KEY_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION = "bg_auto_restricted_bucket_on_bg_restricted";
        static final String KEY_BG_AUTO_RESTRICT_ABUSIVE_APPS = "bg_auto_restrict_abusive_apps";
        static final String KEY_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL = "bg_long_fgs_notification_minimal_interval";
        static final String KEY_BG_PROMPT_ABUSIVE_APPS_TO_BG_RESTRICTED = "bg_prompt_abusive_apps_to_bg_restricted";
        static final String KEY_BG_PROMPT_FGS_ON_LONG_RUNNING = "bg_prompt_fgs_on_long_running";
        static final String KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING = "bg_prompt_fgs_with_noti_on_long_running";
        static final String KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_TO_BG_RESTRICTED = "bg_prompt_fgs_with_noti_to_bg_restricted";
        static final String KEY_BG_RESTRICTION_EXEMPTED_PACKAGES = "bg_restriction_exempted_packages";
        volatile long mBgAbusiveNotificationMinIntervalMs;
        volatile boolean mBgAutoRestrictAbusiveApps;
        volatile boolean mBgAutoRestrictedBucket;
        volatile long mBgLongFgsNotificationMinIntervalMs;
        volatile boolean mBgPromptAbusiveAppsToBgRestricted;
        volatile boolean mBgPromptFgsOnLongRunning;
        volatile boolean mBgPromptFgsWithNotiOnLongRunning;
        volatile boolean mBgPromptFgsWithNotiToBgRestricted;
        volatile Set<String> mBgRestrictionExemptedPackages = Collections.emptySet();
        final boolean mDefaultBgPromptAbusiveAppToBgRestricted;
        final boolean mDefaultBgPromptFgsWithNotiToBgRestricted;

        ConstantsObserver(Handler handler, Context context) {
            this.mDefaultBgPromptFgsWithNotiToBgRestricted = context.getResources().getBoolean(R.bool.config_checkWallpaperAtBoot);
            this.mDefaultBgPromptAbusiveAppToBgRestricted = context.getResources().getBoolean(R.bool.config_cellBroadcastAppLinks);
        }

        /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0025. Please report as an issue. */
        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            String str;
            Iterator it = properties.getKeyset().iterator();
            while (it.hasNext() && (str = (String) it.next()) != null && str.startsWith(AppRestrictionController.DEVICE_CONFIG_SUBNAMESPACE_PREFIX)) {
                char c = 65535;
                switch (str.hashCode()) {
                    case -1918659497:
                        if (str.equals(KEY_BG_PROMPT_ABUSIVE_APPS_TO_BG_RESTRICTED)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1199889595:
                        if (str.equals(KEY_BG_AUTO_RESTRICT_ABUSIVE_APPS)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -582264882:
                        if (str.equals(KEY_BG_PROMPT_FGS_ON_LONG_RUNNING)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -395763044:
                        if (str.equals(KEY_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION)) {
                            c = 3;
                            break;
                        }
                        break;
                    case -157665503:
                        if (str.equals(KEY_BG_RESTRICTION_EXEMPTED_PACKAGES)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 854605367:
                        if (str.equals(KEY_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL)) {
                            c = 5;
                            break;
                        }
                        break;
                    case 892275457:
                        if (str.equals(KEY_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL)) {
                            c = 6;
                            break;
                        }
                        break;
                    case 1771474142:
                        if (str.equals(KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING)) {
                            c = 7;
                            break;
                        }
                        break;
                    case 1965398671:
                        if (str.equals(KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_TO_BG_RESTRICTED)) {
                            c = '\b';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        updateBgPromptAbusiveAppToBgRestricted();
                        break;
                    case 1:
                        updateBgAutoRestrictAbusiveApps();
                        break;
                    case 2:
                        updateBgPromptFgsOnLongRunning();
                        break;
                    case 3:
                        updateBgAutoRestrictedBucketChanged();
                        break;
                    case 4:
                        updateBgRestrictionExemptedPackages();
                        break;
                    case 5:
                        updateBgAbusiveNotificationMinimalInterval();
                        break;
                    case 6:
                        updateBgLongFgsNotificationMinimalInterval();
                        break;
                    case 7:
                        updateBgPromptFgsWithNotiOnLongRunning();
                        break;
                    case '\b':
                        updateBgPromptFgsWithNotiToBgRestricted();
                        break;
                }
                AppRestrictionController.this.onPropertiesChanged(str);
            }
        }

        public void start() {
            updateDeviceConfig();
        }

        void updateDeviceConfig() {
            updateBgAutoRestrictedBucketChanged();
            updateBgAutoRestrictAbusiveApps();
            updateBgAbusiveNotificationMinimalInterval();
            updateBgLongFgsNotificationMinimalInterval();
            updateBgPromptFgsWithNotiToBgRestricted();
            updateBgPromptFgsWithNotiOnLongRunning();
            updateBgPromptFgsOnLongRunning();
            updateBgPromptAbusiveAppToBgRestricted();
            updateBgRestrictionExemptedPackages();
        }

        private void updateBgAutoRestrictedBucketChanged() {
            boolean z = this.mBgAutoRestrictedBucket;
            this.mBgAutoRestrictedBucket = DeviceConfig.getBoolean("activity_manager", KEY_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION, false);
            if (z != this.mBgAutoRestrictedBucket) {
                AppRestrictionController.this.dispatchAutoRestrictedBucketFeatureFlagChanged(this.mBgAutoRestrictedBucket);
            }
        }

        private void updateBgAutoRestrictAbusiveApps() {
            this.mBgAutoRestrictAbusiveApps = DeviceConfig.getBoolean("activity_manager", KEY_BG_AUTO_RESTRICT_ABUSIVE_APPS, true);
        }

        private void updateBgAbusiveNotificationMinimalInterval() {
            this.mBgAbusiveNotificationMinIntervalMs = DeviceConfig.getLong("activity_manager", KEY_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL, 2592000000L);
        }

        private void updateBgLongFgsNotificationMinimalInterval() {
            this.mBgLongFgsNotificationMinIntervalMs = DeviceConfig.getLong("activity_manager", KEY_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL, 2592000000L);
        }

        private void updateBgPromptFgsWithNotiToBgRestricted() {
            this.mBgPromptFgsWithNotiToBgRestricted = DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_TO_BG_RESTRICTED, this.mDefaultBgPromptFgsWithNotiToBgRestricted);
        }

        private void updateBgPromptFgsWithNotiOnLongRunning() {
            this.mBgPromptFgsWithNotiOnLongRunning = DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING, false);
        }

        private void updateBgPromptFgsOnLongRunning() {
            this.mBgPromptFgsOnLongRunning = DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_FGS_ON_LONG_RUNNING, false);
        }

        private void updateBgPromptAbusiveAppToBgRestricted() {
            this.mBgPromptAbusiveAppsToBgRestricted = DeviceConfig.getBoolean("activity_manager", KEY_BG_PROMPT_ABUSIVE_APPS_TO_BG_RESTRICTED, this.mDefaultBgPromptAbusiveAppToBgRestricted);
        }

        private void updateBgRestrictionExemptedPackages() {
            String string = DeviceConfig.getString("activity_manager", KEY_BG_RESTRICTION_EXEMPTED_PACKAGES, (String) null);
            if (string == null) {
                this.mBgRestrictionExemptedPackages = Collections.emptySet();
                return;
            }
            String[] split = string.split(",");
            ArraySet arraySet = new ArraySet();
            for (String str : split) {
                arraySet.add(str);
            }
            this.mBgRestrictionExemptedPackages = Collections.unmodifiableSet(arraySet);
        }

        void dump(PrintWriter printWriter, String str) {
            printWriter.print(str);
            printWriter.println("BACKGROUND RESTRICTION POLICY SETTINGS:");
            String str2 = "  " + str;
            printWriter.print(str2);
            printWriter.print(KEY_BG_AUTO_RESTRICTED_BUCKET_ON_BG_RESTRICTION);
            printWriter.print('=');
            printWriter.println(this.mBgAutoRestrictedBucket);
            printWriter.print(str2);
            printWriter.print(KEY_BG_AUTO_RESTRICT_ABUSIVE_APPS);
            printWriter.print('=');
            printWriter.println(this.mBgAutoRestrictAbusiveApps);
            printWriter.print(str2);
            printWriter.print(KEY_BG_ABUSIVE_NOTIFICATION_MINIMAL_INTERVAL);
            printWriter.print('=');
            printWriter.println(this.mBgAbusiveNotificationMinIntervalMs);
            printWriter.print(str2);
            printWriter.print(KEY_BG_LONG_FGS_NOTIFICATION_MINIMAL_INTERVAL);
            printWriter.print('=');
            printWriter.println(this.mBgLongFgsNotificationMinIntervalMs);
            printWriter.print(str2);
            printWriter.print(KEY_BG_PROMPT_FGS_ON_LONG_RUNNING);
            printWriter.print('=');
            printWriter.println(this.mBgPromptFgsOnLongRunning);
            printWriter.print(str2);
            printWriter.print(KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_ON_LONG_RUNNING);
            printWriter.print('=');
            printWriter.println(this.mBgPromptFgsWithNotiOnLongRunning);
            printWriter.print(str2);
            printWriter.print(KEY_BG_PROMPT_FGS_WITH_NOTIFICATION_TO_BG_RESTRICTED);
            printWriter.print('=');
            printWriter.println(this.mBgPromptFgsWithNotiToBgRestricted);
            printWriter.print(str2);
            printWriter.print(KEY_BG_PROMPT_ABUSIVE_APPS_TO_BG_RESTRICTED);
            printWriter.print('=');
            printWriter.println(this.mBgPromptAbusiveAppsToBgRestricted);
            printWriter.print(str2);
            printWriter.print(KEY_BG_RESTRICTION_EXEMPTED_PACKAGES);
            printWriter.print('=');
            printWriter.println(this.mBgRestrictionExemptedPackages.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class TrackerInfo {
        final byte[] mInfo;
        final int mType;

        TrackerInfo() {
            this.mType = 0;
            this.mInfo = null;
        }

        TrackerInfo(int i, byte[] bArr) {
            this.mType = i;
            this.mInfo = bArr;
        }
    }

    public void addAppBackgroundRestrictionListener(ActivityManagerInternal.AppBackgroundRestrictionListener appBackgroundRestrictionListener) {
        this.mRestrictionListeners.add(appBackgroundRestrictionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppRestrictionController(Context context, ActivityManagerService activityManagerService) {
        this(new Injector(context), activityManagerService);
    }

    AppRestrictionController(Injector injector, ActivityManagerService activityManagerService) {
        this.mAppStateTrackers = new ArrayList<>();
        this.mRestrictionSettings = new RestrictionSettings();
        this.mRestrictionListeners = new CopyOnWriteArraySet<>();
        this.mActiveUids = new SparseArrayMap<>();
        this.mTmpRunnables = new ArrayList<>();
        this.mDeviceIdleAllowlist = new int[0];
        this.mDeviceIdleExceptIdleAllowlist = new int[0];
        this.mSystemDeviceIdleAllowlist = new ArraySet<>();
        this.mSystemDeviceIdleExceptIdleAllowlist = new ArraySet<>();
        this.mLock = new Object();
        this.mSettingsLock = new Object();
        this.mRoleHolderChangedListener = new OnRoleHoldersChangedListener() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda5
            public final void onRoleHoldersChanged(String str, UserHandle userHandle) {
                AppRestrictionController.this.onRoleHoldersChanged(str, userHandle);
            }
        };
        this.mUidRolesMapping = new SparseArray<>();
        this.mSystemModulesCache = new HashMap<>();
        this.mCarrierPrivilegedLock = new Object();
        this.mCarrierPrivilegedApps = new SparseArray<>();
        this.mRestrictionSettingsXmlLoaded = new AtomicBoolean();
        this.mEmptyTrackerInfo = new TrackerInfo();
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.am.AppRestrictionController.1
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                char c;
                int intExtra;
                int intExtra2;
                String schemeSpecificPart;
                intent.getAction();
                String action = intent.getAction();
                action.hashCode();
                switch (action.hashCode()) {
                    case -2061058799:
                        if (action.equals("android.intent.action.USER_REMOVED")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1749672628:
                        if (action.equals("android.intent.action.UID_REMOVED")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case -755112654:
                        if (action.equals("android.intent.action.USER_STARTED")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case -742246786:
                        if (action.equals("android.intent.action.USER_STOPPED")) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1093296680:
                        if (action.equals("android.telephony.action.MULTI_SIM_CONFIG_CHANGED")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1121780209:
                        if (action.equals("android.intent.action.USER_ADDED")) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1544582882:
                        if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case 1580442797:
                        if (action.equals("android.intent.action.PACKAGE_FULLY_REMOVED")) {
                            c = 7;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        int intExtra3 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (intExtra3 >= 0) {
                            AppRestrictionController.this.onUserRemoved(intExtra3);
                            return;
                        }
                        return;
                    case 1:
                        if (intent.getBooleanExtra("android.intent.extra.REPLACING", false) || (intExtra = intent.getIntExtra("android.intent.extra.UID", -1)) < 0) {
                            return;
                        }
                        AppRestrictionController.this.onUidRemoved(intExtra);
                        return;
                    case 2:
                        int intExtra4 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (intExtra4 >= 0) {
                            AppRestrictionController.this.onUserStarted(intExtra4);
                            return;
                        }
                        return;
                    case 3:
                        int intExtra5 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (intExtra5 >= 0) {
                            AppRestrictionController.this.onUserStopped(intExtra5);
                            return;
                        }
                        return;
                    case 4:
                        AppRestrictionController.this.unregisterCarrierPrivilegesCallbacks();
                        AppRestrictionController.this.registerCarrierPrivilegesCallbacks();
                        return;
                    case 5:
                        int intExtra6 = intent.getIntExtra("android.intent.extra.user_handle", -1);
                        if (intExtra6 >= 0) {
                            AppRestrictionController.this.onUserAdded(intExtra6);
                            return;
                        }
                        return;
                    case 6:
                        if (intent.getBooleanExtra("android.intent.extra.REPLACING", false) || (intExtra2 = intent.getIntExtra("android.intent.extra.UID", -1)) < 0) {
                            return;
                        }
                        AppRestrictionController.this.onUidAdded(intExtra2);
                        return;
                    case 7:
                        int intExtra7 = intent.getIntExtra("android.intent.extra.UID", -1);
                        Uri data = intent.getData();
                        if (intExtra7 < 0 || data == null || (schemeSpecificPart = data.getSchemeSpecificPart()) == null) {
                            return;
                        }
                        AppRestrictionController.this.onPackageRemoved(schemeSpecificPart, intExtra7);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mBootReceiver = new BroadcastReceiver() { // from class: com.android.server.am.AppRestrictionController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                intent.getAction();
                String action = intent.getAction();
                action.hashCode();
                if (action.equals("android.intent.action.LOCKED_BOOT_COMPLETED")) {
                    AppRestrictionController.this.onLockedBootCompleted();
                }
            }
        };
        this.mBackgroundRestrictionListener = new AppStateTracker.BackgroundRestrictedAppListener() { // from class: com.android.server.am.AppRestrictionController.3
            public void updateBackgroundRestrictedForUidPackage(int i, String str, boolean z) {
                AppRestrictionController.this.mBgHandler.obtainMessage(0, i, z ? 1 : 0, str).sendToTarget();
            }
        };
        this.mAppIdleStateChangeListener = new AppStandbyInternal.AppIdleStateChangeListener() { // from class: com.android.server.am.AppRestrictionController.4
            public void onAppIdleStateChanged(String str, int i, boolean z, int i2, int i3) {
                AppRestrictionController.this.mBgHandler.obtainMessage(2, i, i2, str).sendToTarget();
            }

            public void onUserInteractionStarted(String str, int i) {
                AppRestrictionController.this.mBgHandler.obtainMessage(3, i, 0, str).sendToTarget();
            }
        };
        this.mUidObserver = new UidObserver() { // from class: com.android.server.am.AppRestrictionController.5
            public void onUidStateChanged(int i, int i2, long j, int i3) {
                AppRestrictionController.this.mBgHandler.obtainMessage(8, i, i2).sendToTarget();
            }

            public void onUidIdle(int i, boolean z) {
                AppRestrictionController.this.mBgHandler.obtainMessage(5, i, z ? 1 : 0).sendToTarget();
            }

            public void onUidGone(int i, boolean z) {
                AppRestrictionController.this.mBgHandler.obtainMessage(7, i, z ? 1 : 0).sendToTarget();
            }

            public void onUidActive(int i) {
                AppRestrictionController.this.mBgHandler.obtainMessage(6, i, 0).sendToTarget();
            }
        };
        this.mAppRestrictionControllerWrapper = new AppRestrictionControllerWrapper();
        this.mAppRestrictionControllerExt = (IAppRestrictionControllerExt) ExtLoader.type(IAppRestrictionControllerExt.class).base(this).create();
        this.mStaticExt = (IAppRestrictionControllerExt.IStaticExt) ExtLoader.type(IAppRestrictionControllerExt.IStaticExt.class).base(this).create();
        this.mInjector = injector;
        Context context = injector.getContext();
        this.mContext = context;
        this.mActivityManagerService = activityManagerService;
        HandlerThread handlerThread = new HandlerThread("bgres-controller", 10);
        this.mBgHandlerThread = handlerThread;
        handlerThread.start();
        BgHandler bgHandler = new BgHandler(handlerThread.getLooper(), injector);
        this.mBgHandler = bgHandler;
        this.mBgExecutor = new HandlerExecutor(bgHandler);
        this.mConstantsObserver = new ConstantsObserver(bgHandler, context);
        this.mNotificationHelper = new NotificationHelper(this);
        injector.initAppStateTrackers(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSystemReady() {
        DeviceConfig.addOnPropertiesChangedListener("activity_manager", this.mBgExecutor, this.mConstantsObserver);
        this.mConstantsObserver.start();
        initBgRestrictionExemptioFromSysConfig();
        initRestrictionStates();
        initSystemModuleNames();
        initRolesInInterest();
        registerForUidObservers();
        registerForSystemBroadcasts();
        registerCarrierPrivilegesCallbacks();
        this.mNotificationHelper.onSystemReady();
        this.mInjector.getAppStateTracker().addBackgroundRestrictedAppListener(this.mBackgroundRestrictionListener);
        this.mInjector.getAppStandbyInternal().addListener(this.mAppIdleStateChangeListener);
        this.mInjector.getRoleManager().addOnRoleHoldersChangedListenerAsUser(this.mBgExecutor, this.mRoleHolderChangedListener, UserHandle.ALL);
        this.mInjector.scheduleInitTrackers(this.mBgHandler, new Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                AppRestrictionController.this.lambda$onSystemReady$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0() {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onSystemReady();
        }
    }

    @VisibleForTesting
    void resetRestrictionSettings() {
        synchronized (this.mSettingsLock) {
            this.mRestrictionSettings.reset();
        }
        initRestrictionStates();
    }

    @VisibleForTesting
    void tearDown() {
        DeviceConfig.removeOnPropertiesChangedListener(this.mConstantsObserver);
        unregisterForUidObservers();
        unregisterForSystemBroadcasts();
        this.mRestrictionSettings.removeXml();
    }

    private void initBgRestrictionExemptioFromSysConfig() {
        SystemConfig systemConfig = SystemConfig.getInstance();
        this.mBgRestrictionExemptioFromSysConfig = systemConfig.getBgRestrictionExemption();
        loadAppIdsFromPackageList(systemConfig.getAllowInPowerSaveExceptIdle(), this.mSystemDeviceIdleExceptIdleAllowlist);
        loadAppIdsFromPackageList(systemConfig.getAllowInPowerSave(), this.mSystemDeviceIdleAllowlist);
    }

    private void loadAppIdsFromPackageList(ArraySet<String> arraySet, ArraySet<Integer> arraySet2) {
        PackageManager packageManager = this.mInjector.getPackageManager();
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(arraySet.valueAt(size), AudioDevice.OUT_FM);
                if (applicationInfo != null) {
                    arraySet2.add(Integer.valueOf(UserHandle.getAppId(applicationInfo.uid)));
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
    }

    private boolean isExemptedFromSysConfig(String str) {
        ArraySet<String> arraySet = this.mBgRestrictionExemptioFromSysConfig;
        return arraySet != null && arraySet.contains(str);
    }

    private void initRestrictionStates() {
        int[] userIds = this.mInjector.getUserManagerInternal().getUserIds();
        for (int i : userIds) {
            refreshAppRestrictionLevelForUser(i, 1024, 2);
        }
        if (this.mInjector.isTest()) {
            return;
        }
        this.mRestrictionSettings.scheduleLoadFromXml();
        for (int i2 : userIds) {
            this.mRestrictionSettings.schedulePersistToXml(i2);
        }
    }

    private void initSystemModuleNames() {
        List<ModuleInfo> installedModules = this.mInjector.getPackageManager().getInstalledModules(0);
        if (installedModules == null) {
            return;
        }
        synchronized (this.mLock) {
            Iterator<ModuleInfo> it = installedModules.iterator();
            while (it.hasNext()) {
                this.mSystemModulesCache.put(it.next().getPackageName(), Boolean.TRUE);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x003d, code lost:
    
        if (r0.applicationInfo.sourceDir.startsWith(android.os.Environment.getApexDirectory().getAbsolutePath()) != false) goto L22;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0045 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0027 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean isSystemModule(String str) {
        boolean z;
        synchronized (this.mLock) {
            Boolean bool = this.mSystemModulesCache.get(str);
            if (bool != null) {
                return bool.booleanValue();
            }
            PackageManager packageManager = this.mInjector.getPackageManager();
            boolean z2 = true;
            if (packageManager.getModuleInfo(str, 0) != null) {
                z = true;
                if (!z) {
                    try {
                        PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
                        if (packageInfo != null) {
                        }
                        z2 = false;
                        z = z2;
                    } catch (PackageManager.NameNotFoundException unused) {
                    }
                }
                synchronized (this.mLock) {
                    this.mSystemModulesCache.put(str, Boolean.valueOf(z));
                }
                return z;
            }
            z = false;
            if (!z) {
            }
            synchronized (this.mLock) {
            }
        }
    }

    private void registerForUidObservers() {
        try {
            this.mInjector.getIActivityManager().registerUidObserver(this.mUidObserver, 15, 4, "android");
        } catch (RemoteException unused) {
        }
    }

    private void unregisterForUidObservers() {
        try {
            this.mInjector.getIActivityManager().unregisterUidObserver(this.mUidObserver);
        } catch (RemoteException unused) {
        }
    }

    private void refreshAppRestrictionLevelForUser(int i, int i2, int i3) {
        List<AppStandbyInfo> appStandbyBuckets = this.mInjector.getAppStandbyInternal().getAppStandbyBuckets(i);
        if (ArrayUtils.isEmpty(appStandbyBuckets)) {
            return;
        }
        PackageManagerInternal packageManagerInternal = this.mInjector.getPackageManagerInternal();
        for (AppStandbyInfo appStandbyInfo : appStandbyBuckets) {
            int packageUid = packageManagerInternal.getPackageUid(appStandbyInfo.mPackageName, 819200L, i);
            if (packageUid < 0) {
                Slog.e("ActivityManager", "Unable to find " + appStandbyInfo.mPackageName + "/u" + i);
            } else {
                Pair<Integer, TrackerInfo> calcAppRestrictionLevel = calcAppRestrictionLevel(i, packageUid, appStandbyInfo.mPackageName, appStandbyInfo.mStandbyBucket, false, false);
                applyRestrictionLevel(appStandbyInfo.mPackageName, packageUid, ((Integer) calcAppRestrictionLevel.first).intValue(), (TrackerInfo) calcAppRestrictionLevel.second, appStandbyInfo.mStandbyBucket, true, i2, i3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshAppRestrictionLevelForUid(int i, int i2, int i3, boolean z) {
        String[] packagesForUid = this.mInjector.getPackageManager().getPackagesForUid(i);
        if (ArrayUtils.isEmpty(packagesForUid)) {
            return;
        }
        AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        int userId = UserHandle.getUserId(i);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int i4 = 0;
        for (int length = packagesForUid.length; i4 < length; length = length) {
            String str = packagesForUid[i4];
            int appStandbyBucket = appStandbyInternal.getAppStandbyBucket(str, userId, elapsedRealtime, false);
            Pair<Integer, TrackerInfo> calcAppRestrictionLevel = calcAppRestrictionLevel(userId, i, str, appStandbyBucket, z, true);
            applyRestrictionLevel(str, i, ((Integer) calcAppRestrictionLevel.first).intValue(), (TrackerInfo) calcAppRestrictionLevel.second, appStandbyBucket, true, i2, i3);
            i4++;
        }
    }

    private Pair<Integer, TrackerInfo> calcAppRestrictionLevel(int i, int i2, String str, int i3, boolean z, boolean z2) {
        if (this.mInjector.getAppHibernationInternal().isHibernatingForUser(str, i)) {
            return new Pair<>(60, this.mEmptyTrackerInfo);
        }
        int i4 = 20;
        TrackerInfo trackerInfo = null;
        if (i3 != 5) {
            if (i3 == 50) {
                i4 = 50;
            } else {
                if (this.mInjector.getAppStateTracker().isAppBackgroundRestricted(i2, str)) {
                    return new Pair<>(50, this.mEmptyTrackerInfo);
                }
                int i5 = i3 == 45 ? 40 : 30;
                if (z2) {
                    Pair<Integer, TrackerInfo> calcAppRestrictionLevelFromTackers = calcAppRestrictionLevelFromTackers(i2, str, 100);
                    int intValue = ((Integer) calcAppRestrictionLevelFromTackers.first).intValue();
                    if (intValue == 20) {
                        return new Pair<>(20, (TrackerInfo) calcAppRestrictionLevelFromTackers.second);
                    }
                    if (intValue > i5) {
                        trackerInfo = (TrackerInfo) calcAppRestrictionLevelFromTackers.second;
                        i4 = intValue;
                    } else {
                        i4 = i5;
                    }
                    if (i4 == 50) {
                        if (z) {
                            this.mBgHandler.obtainMessage(4, i2, 0, str).sendToTarget();
                        }
                        Pair<Integer, TrackerInfo> calcAppRestrictionLevelFromTackers2 = calcAppRestrictionLevelFromTackers(i2, str, 50);
                        i4 = ((Integer) calcAppRestrictionLevelFromTackers2.first).intValue();
                        trackerInfo = (TrackerInfo) calcAppRestrictionLevelFromTackers2.second;
                    }
                } else {
                    i4 = i5;
                }
            }
        }
        return new Pair<>(Integer.valueOf(i4), trackerInfo);
    }

    private Pair<Integer, TrackerInfo> calcAppRestrictionLevelFromTackers(int i, String str, int i2) {
        TrackerInfo trackerInfo;
        int i3 = 0;
        BaseAppStateTracker baseAppStateTracker = null;
        int i4 = 0;
        for (int size = this.mAppStateTrackers.size() - 1; size >= 0; size--) {
            i3 = Math.max(i3, this.mAppStateTrackers.get(size).getPolicy().getProposedRestrictionLevel(str, i, i2));
            if (i3 != i4) {
                baseAppStateTracker = this.mAppStateTrackers.get(size);
                i4 = i3;
            }
        }
        if (baseAppStateTracker == null) {
            trackerInfo = this.mEmptyTrackerInfo;
        } else {
            trackerInfo = new TrackerInfo(baseAppStateTracker.getType(), baseAppStateTracker.getTrackerInfoForStatsd(i));
        }
        return new Pair<>(Integer.valueOf(i3), trackerInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRestrictionLevel(int i) {
        return this.mRestrictionSettings.getRestrictionLevel(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRestrictionLevel(int i, String str) {
        return this.mRestrictionSettings.getRestrictionLevel(i, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getRestrictionLevel(String str, int i) {
        return this.mRestrictionSettings.getRestrictionLevel(str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAutoRestrictAbusiveAppEnabled() {
        return this.mConstantsObserver.mBgAutoRestrictAbusiveApps;
    }

    long getForegroundServiceTotalDurations(String str, int i, long j, int i2) {
        return this.mInjector.getAppFGSTracker().getTotalDurations(str, i, j, AppFGSTracker.foregroundServiceTypeToIndex(i2));
    }

    long getForegroundServiceTotalDurations(int i, long j, int i2) {
        return this.mInjector.getAppFGSTracker().getTotalDurations(i, j, AppFGSTracker.foregroundServiceTypeToIndex(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getForegroundServiceTotalDurationsSince(String str, int i, long j, long j2, int i2) {
        return this.mInjector.getAppFGSTracker().getTotalDurationsSince(str, i, j, j2, AppFGSTracker.foregroundServiceTypeToIndex(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getForegroundServiceTotalDurationsSince(int i, long j, long j2, int i2) {
        return this.mInjector.getAppFGSTracker().getTotalDurationsSince(i, j, j2, AppFGSTracker.foregroundServiceTypeToIndex(i2));
    }

    long getMediaSessionTotalDurations(String str, int i, long j) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurations(str, i, j);
    }

    long getMediaSessionTotalDurations(int i, long j) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurations(i, j);
    }

    long getMediaSessionTotalDurationsSince(String str, int i, long j, long j2) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurationsSince(str, i, j, j2);
    }

    long getMediaSessionTotalDurationsSince(int i, long j, long j2) {
        return this.mInjector.getAppMediaSessionTracker().getTotalDurationsSince(i, j, j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCompositeMediaPlaybackDurations(String str, int i, long j, long j2) {
        long max = Math.max(0L, j - j2);
        return Math.max(getMediaSessionTotalDurationsSince(str, i, max, j), getForegroundServiceTotalDurationsSince(str, i, max, j, 2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCompositeMediaPlaybackDurations(int i, long j, long j2) {
        long max = Math.max(0L, j - j2);
        return Math.max(getMediaSessionTotalDurationsSince(i, max, j), getForegroundServiceTotalDurationsSince(i, max, j, 2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasForegroundServices(String str, int i) {
        return this.mInjector.getAppFGSTracker().hasForegroundServices(str, i);
    }

    boolean hasForegroundServices(int i) {
        return this.mInjector.getAppFGSTracker().hasForegroundServices(i);
    }

    boolean hasForegroundServiceNotifications(String str, int i) {
        return this.mInjector.getAppFGSTracker().hasForegroundServiceNotifications(str, i);
    }

    boolean hasForegroundServiceNotifications(int i) {
        return this.mInjector.getAppFGSTracker().hasForegroundServiceNotifications(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppBatteryTracker.ImmutableBatteryUsage getUidBatteryExemptedUsageSince(int i, long j, long j2, int i2) {
        return this.mInjector.getAppBatteryExemptionTracker().getUidBatteryExemptedUsageSince(i, j, j2, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppBatteryTracker.ImmutableBatteryUsage getUidBatteryUsage(int i) {
        return this.mInjector.getUidBatteryUsageProvider().getUidBatteryUsage(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.println("APP BACKGROUND RESTRICTIONS");
        String str2 = "  " + str;
        printWriter.print(str2);
        printWriter.println("BACKGROUND RESTRICTION LEVEL SETTINGS");
        this.mRestrictionSettings.dump(printWriter, "  " + str2);
        this.mConstantsObserver.dump(printWriter, "  " + str2);
        this.mAppRestrictionControllerWrapper.getExtImpl().dump(printWriter, "  " + str2);
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            printWriter.println();
            this.mAppStateTrackers.get(i).dump(printWriter, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpAsProto(ProtoOutputStream protoOutputStream, int i) {
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).dumpAsProto(protoOutputStream, i);
        }
    }

    private int getExemptionReasonStatsd(int i, int i2) {
        if (i2 != 20) {
            return 1;
        }
        return PowerExemptionManager.getExemptionReasonForStatsd(getBackgroundRestrictionExemptionReason(i));
    }

    private int getTargetSdkStatsd(String str) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = this.mInjector.getPackageManager();
        if (packageManager == null) {
            return 0;
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
            if (packageInfo != null && (applicationInfo = packageInfo.applicationInfo) != null) {
                int i = applicationInfo.targetSdkVersion;
                if (i < 31) {
                    return 1;
                }
                if (i < 33) {
                    return 2;
                }
                if (i == 33) {
                    return 3;
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r16v1 */
    /* JADX WARN: Type inference failed for: r16v2 */
    public void applyRestrictionLevel(final String str, final int i, final int i2, TrackerInfo trackerInfo, int i3, boolean z, int i4, int i5) {
        int i6;
        int i7;
        Object obj;
        int i8;
        boolean z2;
        int appStandbyBucketReason;
        final AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        TrackerInfo trackerInfo2 = trackerInfo == null ? this.mEmptyTrackerInfo : trackerInfo;
        synchronized (this.mSettingsLock) {
            final int restrictionLevel = getRestrictionLevel(i, str);
            if (restrictionLevel == i2) {
                return;
            }
            if (standbyBucketToRestrictionLevel(i3) != i2 || (appStandbyBucketReason = appStandbyInternal.getAppStandbyBucketReason(str, UserHandle.getUserId(i), SystemClock.elapsedRealtime())) == 0) {
                i6 = i4;
                i7 = i5;
            } else {
                i7 = appStandbyBucketReason & 255;
                i6 = appStandbyBucketReason & BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_COMMAND__CMD_PAIRING_COMPLETE;
            }
            ?? reason = this.mRestrictionSettings.getReason(str, i);
            final int i9 = i7;
            final int i10 = i6;
            this.mRestrictionSettings.update(str, i, i2, i6, i9);
            if (!z || i3 == 5) {
                return;
            }
            if (i2 < 40 || restrictionLevel >= 40) {
                if (restrictionLevel < 40 || i2 >= 40) {
                    return;
                }
                synchronized (this.mSettingsLock) {
                    if (this.mActiveUids.indexOfKey(i, str) >= 0) {
                        this.mActiveUids.add(i, str, (Object) null);
                    }
                }
                appStandbyInternal.maybeUnrestrictApp(str, UserHandle.getUserId(i), reason & BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_COMMAND__CMD_PAIRING_COMPLETE, reason & 255, i10, i9);
                logAppBackgroundRestrictionInfo(str, i, restrictionLevel, i2, trackerInfo2, i10);
                return;
            }
            if (i3 != 45) {
                if (this.mConstantsObserver.mBgAutoRestrictedBucket || i2 == 40) {
                    Object obj2 = this.mSettingsLock;
                    synchronized (obj2) {
                        try {
                        } catch (Throwable th) {
                            th = th;
                        }
                        try {
                            if (this.mActiveUids.indexOfKey(i, str) >= 0) {
                                obj = obj2;
                                i8 = restrictionLevel;
                                final TrackerInfo trackerInfo3 = trackerInfo2;
                                this.mActiveUids.add(i, str, new Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda10
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        AppRestrictionController.this.lambda$applyRestrictionLevel$1(appStandbyInternal, str, i, i10, i9, restrictionLevel, i2, trackerInfo3);
                                    }
                                });
                                z2 = false;
                            } else {
                                obj = obj2;
                                i8 = restrictionLevel;
                                z2 = true;
                            }
                            if (z2) {
                                appStandbyInternal.restrictApp(str, UserHandle.getUserId(i), i10, i9);
                                logAppBackgroundRestrictionInfo(str, i, i8, i2, trackerInfo2, i10);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            reason = obj2;
                            throw th;
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyRestrictionLevel$1(AppStandbyInternal appStandbyInternal, String str, int i, int i2, int i3, int i4, int i5, TrackerInfo trackerInfo) {
        appStandbyInternal.restrictApp(str, UserHandle.getUserId(i), i2, i3);
        logAppBackgroundRestrictionInfo(str, i, i4, i5, trackerInfo, i2);
    }

    private void logAppBackgroundRestrictionInfo(String str, int i, int i2, int i3, TrackerInfo trackerInfo, int i4) {
        int restrictionLevelStatsd = getRestrictionLevelStatsd(i3);
        int thresholdStatsd = getThresholdStatsd(i4);
        int trackerTypeStatsd = getTrackerTypeStatsd(trackerInfo.mType);
        int i5 = trackerInfo.mType;
        FrameworkStatsLog.write(FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO, i, restrictionLevelStatsd, thresholdStatsd, trackerTypeStatsd, i5 == 3 ? trackerInfo.mInfo : null, i5 == 1 ? trackerInfo.mInfo : null, i5 == 6 ? trackerInfo.mInfo : null, i5 == 7 ? trackerInfo.mInfo : null, getExemptionReasonStatsd(i, i3), getOptimizationLevelStatsd(i3), getTargetSdkStatsd(str), ActivityManager.isLowRamDeviceStatic(), getRestrictionLevelStatsd(i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBackgroundRestrictionChanged(int i, String str, boolean z) {
        int i2;
        int size = this.mAppStateTrackers.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mAppStateTrackers.get(i3).onBackgroundRestrictionChanged(i, str, z);
        }
        int appStandbyBucket = this.mInjector.getAppStandbyInternal().getAppStandbyBucket(str, UserHandle.getUserId(i), SystemClock.elapsedRealtime(), false);
        if (z) {
            applyRestrictionLevel(str, i, 50, this.mEmptyTrackerInfo, appStandbyBucket, true, 1024, 2);
            this.mBgHandler.obtainMessage(9, i, 0, str).sendToTarget();
            return;
        }
        int lastRestrictionLevel = this.mRestrictionSettings.getLastRestrictionLevel(i, str);
        int i4 = 5;
        if (appStandbyBucket != 5) {
            i4 = 40;
            if (lastRestrictionLevel == 40) {
                i2 = 45;
                Pair<Integer, TrackerInfo> calcAppRestrictionLevel = calcAppRestrictionLevel(UserHandle.getUserId(i), i, str, i2, false, true);
                applyRestrictionLevel(str, i, ((Integer) calcAppRestrictionLevel.first).intValue(), (TrackerInfo) calcAppRestrictionLevel.second, appStandbyBucket, true, FrameworkStatsLog.APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_USAGE, 3);
            }
        }
        i2 = i4;
        Pair<Integer, TrackerInfo> calcAppRestrictionLevel2 = calcAppRestrictionLevel(UserHandle.getUserId(i), i, str, i2, false, true);
        applyRestrictionLevel(str, i, ((Integer) calcAppRestrictionLevel2.first).intValue(), (TrackerInfo) calcAppRestrictionLevel2.second, appStandbyBucket, true, FrameworkStatsLog.APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_USAGE, 3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAppRestrictionLevelChanges(final int i, final String str, final int i2) {
        this.mRestrictionListeners.forEach(new Consumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ActivityManagerInternal.AppBackgroundRestrictionListener) obj).onRestrictionLevelChanged(i, str, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchAutoRestrictedBucketFeatureFlagChanged(final boolean z) {
        final AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        final ArrayList arrayList = new ArrayList();
        synchronized (this.mSettingsLock) {
            this.mRestrictionSettings.forEachUidLocked(new Consumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppRestrictionController.this.lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$6(arrayList, z, appStandbyInternal, (Integer) obj);
                }
            });
        }
        for (int i = 0; i < arrayList.size(); i++) {
            ((Runnable) arrayList.get(i)).run();
        }
        this.mRestrictionListeners.forEach(new Consumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ActivityManagerInternal.AppBackgroundRestrictionListener) obj).onAutoRestrictedBucketFeatureFlagChanged(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$6(final ArrayList arrayList, final boolean z, final AppStandbyInternal appStandbyInternal, final Integer num) {
        this.mRestrictionSettings.forEachPackageInUidLocked(num.intValue(), new TriConsumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda7
            public final void accept(Object obj, Object obj2, Object obj3) {
                AppRestrictionController.lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$5(arrayList, z, appStandbyInternal, num, (String) obj, (Integer) obj2, (Integer) obj3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$5(ArrayList arrayList, boolean z, final AppStandbyInternal appStandbyInternal, final Integer num, final String str, Integer num2, final Integer num3) {
        Runnable runnable;
        if (num2.intValue() == 50) {
            if (z) {
                runnable = new Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppRestrictionController.lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$3(appStandbyInternal, str, num, num3);
                    }
                };
            } else {
                runnable = new Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppRestrictionController.lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$4(appStandbyInternal, str, num, num3);
                    }
                };
            }
            arrayList.add(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$3(AppStandbyInternal appStandbyInternal, String str, Integer num, Integer num2) {
        appStandbyInternal.restrictApp(str, UserHandle.getUserId(num.intValue()), num2.intValue() & BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_COMMAND__CMD_PAIRING_COMPLETE, num2.intValue() & 255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$dispatchAutoRestrictedBucketFeatureFlagChanged$4(AppStandbyInternal appStandbyInternal, String str, Integer num, Integer num2) {
        appStandbyInternal.maybeUnrestrictApp(str, UserHandle.getUserId(num.intValue()), num2.intValue() & BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_COMMAND__CMD_PAIRING_COMPLETE, num2.intValue() & 255, FrameworkStatsLog.APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_USAGE, 6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAppStandbyBucketChanged(int i, String str, int i2) {
        int packageUid = this.mInjector.getPackageManagerInternal().getPackageUid(str, 819200L, i2);
        Pair<Integer, TrackerInfo> calcAppRestrictionLevel = calcAppRestrictionLevel(i2, packageUid, str, i, false, false);
        applyRestrictionLevel(str, packageUid, ((Integer) calcAppRestrictionLevel.first).intValue(), (TrackerInfo) calcAppRestrictionLevel.second, i, false, 256, 0);
    }

    void handleRequestBgRestricted(String str, int i) {
        this.mNotificationHelper.postRequestBgRestrictedIfNecessary(str, i);
    }

    void handleCancelRequestBgRestricted(String str, int i) {
        this.mNotificationHelper.cancelRequestBgRestrictedIfNecessary(str, i);
    }

    void handleUidProcStateChanged(int i, int i2) {
        int size = this.mAppStateTrackers.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mAppStateTrackers.get(i3).onUidProcStateChanged(i, i2);
        }
    }

    void handleUidGone(int i) {
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUidGone(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class NotificationHelper {
        static final String ACTION_FGS_MANAGER_TRAMPOLINE = "com.android.server.am.ACTION_FGS_MANAGER_TRAMPOLINE";
        static final String GROUP_KEY = "com.android.app.abusive_bg_apps";
        static final int NOTIFICATION_TYPE_ABUSIVE_CURRENT_DRAIN = 0;
        static final int NOTIFICATION_TYPE_LAST = 2;
        static final int NOTIFICATION_TYPE_LONG_RUNNING_FGS = 1;
        static final String PACKAGE_SCHEME = "package";
        static final int SUMMARY_NOTIFICATION_ID = 203105544;
        private final AppRestrictionController mBgController;
        private final Context mContext;
        private final Injector mInjector;
        private final Object mLock;
        private final NotificationManager mNotificationManager;
        private final Object mSettingsLock;
        static final String[] NOTIFICATION_TYPE_STRINGS = {"Abusive current drain", "Long-running FGS"};
        static final String ATTR_LAST_BATTERY_NOTIFICATION_TIME = "last_batt_noti_ts";
        static final String ATTR_LAST_LONG_FGS_NOTIFICATION_TIME = "last_long_fgs_noti_ts";
        static final String[] NOTIFICATION_TIME_ATTRS = {ATTR_LAST_BATTERY_NOTIFICATION_TIME, ATTR_LAST_LONG_FGS_NOTIFICATION_TIME};
        private final BroadcastReceiver mActionButtonReceiver = new BroadcastReceiver() { // from class: com.android.server.am.AppRestrictionController.NotificationHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                intent.getAction();
                String action = intent.getAction();
                action.hashCode();
                if (action.equals(NotificationHelper.ACTION_FGS_MANAGER_TRAMPOLINE)) {
                    NotificationHelper.this.cancelRequestBgRestrictedIfNecessary(intent.getStringExtra("android.intent.extra.PACKAGE_NAME"), intent.getIntExtra("android.intent.extra.UID", 0));
                    Intent intent2 = new Intent("android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER");
                    intent2.addFlags(16777216);
                    NotificationHelper.this.mContext.sendBroadcastAsUser(intent2, UserHandle.SYSTEM);
                }
            }
        };

        @GuardedBy({"mSettingsLock"})
        private int mNotificationIDStepper = 203105545;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        @interface NotificationType {
        }

        static int notificationTimeAttrToType(String str) {
            str.hashCode();
            if (str.equals(ATTR_LAST_LONG_FGS_NOTIFICATION_TIME)) {
                return 1;
            }
            if (str.equals(ATTR_LAST_BATTERY_NOTIFICATION_TIME)) {
                return 0;
            }
            throw new IllegalArgumentException();
        }

        static String notificationTypeToTimeAttr(int i) {
            return NOTIFICATION_TIME_ATTRS[i];
        }

        static String notificationTypeToString(int i) {
            return NOTIFICATION_TYPE_STRINGS[i];
        }

        NotificationHelper(AppRestrictionController appRestrictionController) {
            this.mBgController = appRestrictionController;
            Injector injector = appRestrictionController.mInjector;
            this.mInjector = injector;
            this.mNotificationManager = injector.getNotificationManager();
            this.mLock = appRestrictionController.mLock;
            this.mSettingsLock = appRestrictionController.mSettingsLock;
            this.mContext = injector.getContext();
        }

        void onSystemReady() {
            this.mContext.registerReceiverForAllUsers(this.mActionButtonReceiver, new IntentFilter(ACTION_FGS_MANAGER_TRAMPOLINE), "android.permission.MANAGE_ACTIVITY_TASKS", this.mBgController.mBgHandler, 4);
            this.mBgController.getWrapper().getStaticExtImpl().registerReceiverForDeleteNotification(this.mContext, this.mBgController.mBgHandler);
        }

        void postRequestBgRestrictedIfNecessary(String str, int i) {
            if (this.mBgController.mConstantsObserver.mBgPromptAbusiveAppsToBgRestricted) {
                Intent intent = new Intent("android.settings.VIEW_ADVANCED_POWER_USAGE_DETAIL");
                intent.setData(Uri.fromParts(PACKAGE_SCHEME, str, null));
                intent.addFlags(AudioFormat.AAC_ADIF);
                PendingIntent activityAsUser = PendingIntent.getActivityAsUser(this.mContext, 0, intent, AudioFormat.DTS_HD, null, UserHandle.of(UserHandle.getUserId(i)));
                boolean hasForegroundServices = this.mBgController.hasForegroundServices(str, i);
                boolean hasForegroundServiceNotifications = this.mBgController.hasForegroundServiceNotifications(str, i);
                if (!this.mBgController.mConstantsObserver.mBgPromptFgsWithNotiToBgRestricted && hasForegroundServices && hasForegroundServiceNotifications) {
                    return;
                }
                postNotificationIfNecessary(0, R.string.permlab_requestPasswordComplexity, R.string.permlab_readNetworkUsageHistory, activityAsUser, str, i, null);
            }
        }

        void postLongRunningFgsIfNecessary(String str, int i) {
            FrameworkStatsLog.write(FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO, i, this.mBgController.getRestrictionLevel(i), 0, 3, this.mInjector.getAppFGSTracker().getTrackerInfoForStatsd(i), (byte[]) null, (byte[]) null, (byte[]) null, PowerExemptionManager.getExemptionReasonForStatsd(this.mBgController.getBackgroundRestrictionExemptionReason(i)), 0, 0, ActivityManager.isLowRamDeviceStatic(), this.mBgController.getRestrictionLevel(i));
            if (this.mBgController.mConstantsObserver.mBgPromptFgsOnLongRunning) {
                if (!this.mBgController.mConstantsObserver.mBgPromptFgsWithNotiOnLongRunning && this.mBgController.hasForegroundServiceNotifications(str, i)) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER");
                intent.addFlags(16777216);
                postNotificationIfNecessary(1, R.string.permlab_route_media_output, R.string.permlab_readPhoneNumbers, PendingIntent.getBroadcastAsUser(this.mContext, 0, intent, AudioFormat.DTS_HD, UserHandle.SYSTEM), str, i, null);
            }
        }

        long getNotificationMinInterval(int i) {
            if (i == 0) {
                return this.mBgController.mConstantsObserver.mBgAbusiveNotificationMinIntervalMs;
            }
            if (i != 1) {
                return 0L;
            }
            return this.mBgController.mConstantsObserver.mBgLongFgsNotificationMinIntervalMs;
        }

        int getNotificationIdIfNecessary(int i, String str, int i2) {
            synchronized (this.mSettingsLock) {
                RestrictionSettings.PkgSettings restrictionSettingsLocked = this.mBgController.mRestrictionSettings.getRestrictionSettingsLocked(i2, str);
                if (restrictionSettingsLocked == null) {
                    return 0;
                }
                long currentTimeMillis = this.mInjector.currentTimeMillis();
                long lastNotificationTime = restrictionSettingsLocked.getLastNotificationTime(i);
                if (lastNotificationTime != 0 && lastNotificationTime + getNotificationMinInterval(i) > currentTimeMillis) {
                    return 0;
                }
                restrictionSettingsLocked.setLastNotificationTime(i, currentTimeMillis);
                int notificationId = restrictionSettingsLocked.getNotificationId(i);
                if (notificationId <= 0) {
                    notificationId = this.mNotificationIDStepper;
                    this.mNotificationIDStepper = notificationId + 1;
                    restrictionSettingsLocked.setNotificationId(i, notificationId);
                }
                if (notificationId > 0) {
                    this.mBgController.getWrapper().getStaticExtImpl().incrementCount();
                }
                return notificationId;
            }
        }

        void postNotificationIfNecessary(int i, int i2, int i3, PendingIntent pendingIntent, String str, int i4, Notification.Action[] actionArr) {
            int notificationIdIfNecessary = getNotificationIdIfNecessary(i, str, i4);
            if (notificationIdIfNecessary <= 0) {
                return;
            }
            PackageManagerInternal packageManagerInternal = this.mInjector.getPackageManagerInternal();
            PackageManager packageManager = this.mInjector.getPackageManager();
            ApplicationInfo applicationInfo = packageManagerInternal.getApplicationInfo(str, 819200L, 1000, UserHandle.getUserId(i4));
            String string = this.mContext.getString(i2);
            Context context = this.mContext;
            Object[] objArr = new Object[1];
            objArr[0] = applicationInfo != null ? applicationInfo.loadLabel(packageManager) : str;
            postNotification(notificationIdIfNecessary, str, i4, string, context.getString(i3, objArr), applicationInfo != null ? Icon.createWithResource(str, applicationInfo.icon) : null, pendingIntent, actionArr);
        }

        void postNotification(int i, String str, int i2, String str2, String str3, Icon icon, PendingIntent pendingIntent, Notification.Action[] actionArr) {
            UserHandle of = UserHandle.of(UserHandle.getUserId(i2));
            postSummaryNotification(of);
            Notification.Builder contentIntent = new Notification.Builder(this.mContext, SystemNotificationChannels.ABUSIVE_BACKGROUND_APPS).setAutoCancel(true).setGroup(GROUP_KEY).setWhen(this.mInjector.currentTimeMillis()).setSmallIcon(R.drawable.stat_sys_warning).setColor(this.mContext.getColor(R.color.system_notification_accent_color)).setContentTitle(str2).setContentText(str3).setContentIntent(pendingIntent);
            this.mBgController.getWrapper().getStaticExtImpl().setDeleteIntent(contentIntent, this.mContext);
            if (icon != null) {
                contentIntent.setLargeIcon(icon);
            }
            if (actionArr != null) {
                for (Notification.Action action : actionArr) {
                    contentIntent.addAction(action);
                }
            }
            Notification build = contentIntent.build();
            build.extras.putString("android.intent.extra.PACKAGE_NAME", str);
            this.mNotificationManager.notifyAsUser(null, i, build, of);
        }

        private void postSummaryNotification(UserHandle userHandle) {
            this.mNotificationManager.notifyAsUser(null, SUMMARY_NOTIFICATION_ID, new Notification.Builder(this.mContext, SystemNotificationChannels.ABUSIVE_BACKGROUND_APPS).setGroup(GROUP_KEY).setGroupSummary(true).setStyle(new Notification.BigTextStyle()).setSmallIcon(R.drawable.stat_sys_warning).setColor(this.mContext.getColor(R.color.system_notification_accent_color)).build(), userHandle);
        }

        void cancelRequestBgRestrictedIfNecessary(String str, int i) {
            int notificationId;
            synchronized (this.mSettingsLock) {
                RestrictionSettings.PkgSettings restrictionSettingsLocked = this.mBgController.mRestrictionSettings.getRestrictionSettingsLocked(i, str);
                if (restrictionSettingsLocked != null && (notificationId = restrictionSettingsLocked.getNotificationId(0)) > 0) {
                    this.mNotificationManager.cancel(notificationId);
                    this.mBgController.getWrapper().getStaticExtImpl().decrementCount();
                    this.mBgController.getWrapper().getStaticExtImpl().cancelSummaryNotificationIfNecessary();
                }
            }
        }

        void cancelLongRunningFGSNotificationIfNecessary(String str, int i) {
            int notificationId;
            synchronized (this.mSettingsLock) {
                RestrictionSettings.PkgSettings restrictionSettingsLocked = this.mBgController.mRestrictionSettings.getRestrictionSettingsLocked(i, str);
                if (restrictionSettingsLocked != null && (notificationId = restrictionSettingsLocked.getNotificationId(1)) > 0) {
                    this.mNotificationManager.cancel(notificationId);
                    this.mBgController.getWrapper().getStaticExtImpl().decrementCount();
                    this.mBgController.getWrapper().getStaticExtImpl().cancelSummaryNotificationIfNecessary();
                }
            }
        }
    }

    void handleUidInactive(int i, boolean z) {
        ArrayList<Runnable> arrayList = this.mTmpRunnables;
        synchronized (this.mSettingsLock) {
            int indexOfKey = this.mActiveUids.indexOfKey(i);
            if (indexOfKey < 0) {
                return;
            }
            int numElementsForKeyAt = this.mActiveUids.numElementsForKeyAt(indexOfKey);
            for (int i2 = 0; i2 < numElementsForKeyAt; i2++) {
                Runnable runnable = (Runnable) this.mActiveUids.valueAt(indexOfKey, i2);
                if (runnable != null) {
                    arrayList.add(runnable);
                }
            }
            this.mActiveUids.deleteAt(indexOfKey);
            int size = arrayList.size();
            for (int i3 = 0; i3 < size; i3++) {
                arrayList.get(i3).run();
            }
            arrayList.clear();
        }
    }

    void handleUidActive(final int i) {
        synchronized (this.mSettingsLock) {
            final AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
            final int userId = UserHandle.getUserId(i);
            this.mRestrictionSettings.forEachPackageInUidLocked(i, new TriConsumer() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda6
                public final void accept(Object obj, Object obj2, Object obj3) {
                    AppRestrictionController.this.lambda$handleUidActive$9(i, appStandbyInternal, userId, (String) obj, (Integer) obj2, (Integer) obj3);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleUidActive$9(int i, final AppStandbyInternal appStandbyInternal, final int i2, final String str, Integer num, final Integer num2) {
        if (this.mConstantsObserver.mBgAutoRestrictedBucket && num.intValue() == 50) {
            this.mActiveUids.add(i, str, new Runnable() { // from class: com.android.server.am.AppRestrictionController$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AppRestrictionController.lambda$handleUidActive$8(appStandbyInternal, str, i2, num2);
                }
            });
        } else {
            this.mActiveUids.add(i, str, (Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$handleUidActive$8(AppStandbyInternal appStandbyInternal, String str, int i, Integer num) {
        appStandbyInternal.restrictApp(str, i, num.intValue() & BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_COMMAND__CMD_PAIRING_COMPLETE, num.intValue() & 255);
    }

    boolean isOnDeviceIdleAllowlist(int i) {
        int appId = UserHandle.getAppId(i);
        return Arrays.binarySearch(this.mDeviceIdleAllowlist, appId) >= 0 || Arrays.binarySearch(this.mDeviceIdleExceptIdleAllowlist, appId) >= 0;
    }

    boolean isOnSystemDeviceIdleAllowlist(int i) {
        int appId = UserHandle.getAppId(i);
        return this.mSystemDeviceIdleAllowlist.contains(Integer.valueOf(appId)) || this.mSystemDeviceIdleExceptIdleAllowlist.contains(Integer.valueOf(appId));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDeviceIdleAllowlist(int[] iArr, int[] iArr2) {
        this.mDeviceIdleAllowlist = iArr;
        this.mDeviceIdleExceptIdleAllowlist = iArr2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBackgroundRestrictionExemptionReason(int i) {
        int potentialSystemExemptionReason = getPotentialSystemExemptionReason(i);
        if (potentialSystemExemptionReason != -1) {
            return potentialSystemExemptionReason;
        }
        String[] packagesForUid = this.mInjector.getPackageManager().getPackagesForUid(i);
        if (packagesForUid != null) {
            for (String str : packagesForUid) {
                int potentialSystemExemptionReason2 = getPotentialSystemExemptionReason(i, str);
                if (potentialSystemExemptionReason2 != -1) {
                    return potentialSystemExemptionReason2;
                }
            }
            for (String str2 : packagesForUid) {
                int potentialUserAllowedExemptionReason = getPotentialUserAllowedExemptionReason(i, str2);
                if (potentialUserAllowedExemptionReason != -1) {
                    return potentialUserAllowedExemptionReason;
                }
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPotentialSystemExemptionReason(int i) {
        if (UserHandle.isCore(i)) {
            return 51;
        }
        if (isOnSystemDeviceIdleAllowlist(i)) {
            return 300;
        }
        if (UserManager.isDeviceInDemoMode(this.mContext)) {
            return 63;
        }
        if (this.mInjector.getUserManagerInternal().hasUserRestriction("no_control_apps", UserHandle.getUserId(i))) {
            return 323;
        }
        ActivityManagerInternal activityManagerInternal = this.mInjector.getActivityManagerInternal();
        if (activityManagerInternal.isDeviceOwner(i)) {
            return 55;
        }
        if (activityManagerInternal.isProfileOwner(i)) {
            return 56;
        }
        int uidProcessState = activityManagerInternal.getUidProcessState(i);
        if (uidProcessState <= 0) {
            return 10;
        }
        return uidProcessState <= 1 ? 11 : -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPotentialSystemExemptionReason(int i, String str) {
        PackageManagerInternal packageManagerInternal = this.mInjector.getPackageManagerInternal();
        AppStandbyInternal appStandbyInternal = this.mInjector.getAppStandbyInternal();
        AppOpsManager appOpsManager = this.mInjector.getAppOpsManager();
        ActivityManagerService activityManagerService = this.mInjector.getActivityManagerService();
        int userId = UserHandle.getUserId(i);
        if (isSystemModule(str)) {
            return 320;
        }
        if (isCarrierApp(str)) {
            return 321;
        }
        if (isExemptedFromSysConfig(str) || this.mConstantsObserver.mBgRestrictionExemptedPackages.contains(str)) {
            return 300;
        }
        if (packageManagerInternal.isPackageStateProtected(str, userId)) {
            return 322;
        }
        if (appStandbyInternal.isActiveDeviceAdmin(str, userId)) {
            return FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ACTIVE_DEVICE_ADMIN;
        }
        if (activityManagerService.mConstants.mFlagSystemExemptPowerRestrictionsEnabled && appOpsManager.checkOpNoThrow(128, i, str) == 0) {
            return FrameworkStatsLog.TIF_TUNE_CHANGED;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPotentialUserAllowedExemptionReason(int i, String str) {
        AppOpsManager appOpsManager = this.mInjector.getAppOpsManager();
        if (appOpsManager.checkOpNoThrow(47, i, str) == 0) {
            return 68;
        }
        if (appOpsManager.checkOpNoThrow(94, i, str) == 0) {
            return 69;
        }
        if (isRoleHeldByUid("android.app.role.DIALER", i)) {
            return FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ROLE_DIALER;
        }
        if (isRoleHeldByUid("android.app.role.EMERGENCY", i)) {
            return 319;
        }
        if (isOnDeviceIdleAllowlist(i)) {
            return 65;
        }
        return this.mInjector.getActivityManagerInternal().isAssociatedCompanionApp(UserHandle.getUserId(i), i) ? 57 : -1;
    }

    private boolean isCarrierApp(String str) {
        synchronized (this.mCarrierPrivilegedLock) {
            SparseArray<Set<String>> sparseArray = this.mCarrierPrivilegedApps;
            if (sparseArray != null) {
                for (int size = sparseArray.size() - 1; size >= 0; size--) {
                    if (this.mCarrierPrivilegedApps.valueAt(size).contains(str)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerCarrierPrivilegesCallbacks() {
        TelephonyManager telephonyManager = this.mInjector.getTelephonyManager();
        if (telephonyManager == null) {
            return;
        }
        int activeModemCount = telephonyManager.getActiveModemCount();
        ArrayList<PhoneCarrierPrivilegesCallback> arrayList = new ArrayList<>();
        for (int i = 0; i < activeModemCount; i++) {
            PhoneCarrierPrivilegesCallback phoneCarrierPrivilegesCallback = new PhoneCarrierPrivilegesCallback(i);
            arrayList.add(phoneCarrierPrivilegesCallback);
            telephonyManager.registerCarrierPrivilegesCallback(i, this.mBgExecutor, phoneCarrierPrivilegesCallback);
        }
        this.mCarrierPrivilegesCallbacks = arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterCarrierPrivilegesCallbacks() {
        ArrayList<PhoneCarrierPrivilegesCallback> arrayList;
        TelephonyManager telephonyManager = this.mInjector.getTelephonyManager();
        if (telephonyManager == null || (arrayList = this.mCarrierPrivilegesCallbacks) == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            telephonyManager.unregisterCarrierPrivilegesCallback(arrayList.get(size));
        }
        this.mCarrierPrivilegesCallbacks = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class PhoneCarrierPrivilegesCallback implements TelephonyManager.CarrierPrivilegesCallback {
        private final int mPhoneId;

        PhoneCarrierPrivilegesCallback(int i) {
            this.mPhoneId = i;
        }

        public void onCarrierPrivilegesChanged(Set<String> set, Set<Integer> set2) {
            synchronized (AppRestrictionController.this.mCarrierPrivilegedLock) {
                AppRestrictionController.this.mCarrierPrivilegedApps.put(this.mPhoneId, Collections.unmodifiableSet(set));
            }
        }
    }

    private boolean isRoleHeldByUid(String str, int i) {
        boolean z;
        synchronized (this.mLock) {
            ArrayList<String> arrayList = this.mUidRolesMapping.get(i);
            z = arrayList != null && arrayList.indexOf(str) >= 0;
        }
        return z;
    }

    private void initRolesInInterest() {
        int[] userIds = this.mInjector.getUserManagerInternal().getUserIds();
        for (String str : ROLES_IN_INTEREST) {
            if (this.mInjector.getRoleManager().isRoleAvailable(str)) {
                for (int i : userIds) {
                    onRoleHoldersChanged(str, UserHandle.of(i));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRoleHoldersChanged(String str, UserHandle userHandle) {
        List roleHoldersAsUser = this.mInjector.getRoleManager().getRoleHoldersAsUser(str, userHandle);
        ArraySet arraySet = new ArraySet();
        int identifier = userHandle.getIdentifier();
        if (roleHoldersAsUser != null) {
            PackageManagerInternal packageManagerInternal = this.mInjector.getPackageManagerInternal();
            Iterator it = roleHoldersAsUser.iterator();
            while (it.hasNext()) {
                arraySet.add(Integer.valueOf(packageManagerInternal.getPackageUid((String) it.next(), 819200L, identifier)));
            }
        }
        synchronized (this.mLock) {
            for (int size = this.mUidRolesMapping.size() - 1; size >= 0; size--) {
                int keyAt = this.mUidRolesMapping.keyAt(size);
                if (UserHandle.getUserId(keyAt) == identifier) {
                    ArrayList<String> valueAt = this.mUidRolesMapping.valueAt(size);
                    int indexOf = valueAt.indexOf(str);
                    boolean contains = arraySet.contains(Integer.valueOf(keyAt));
                    if (indexOf >= 0) {
                        if (!contains) {
                            valueAt.remove(indexOf);
                            if (valueAt.isEmpty()) {
                                this.mUidRolesMapping.removeAt(size);
                            }
                        }
                    } else if (contains) {
                        valueAt.add(str);
                        arraySet.remove(Integer.valueOf(keyAt));
                    }
                }
            }
            for (int size2 = arraySet.size() - 1; size2 >= 0; size2--) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(str);
                this.mUidRolesMapping.put(((Integer) arraySet.valueAt(size2)).intValue(), arrayList);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Handler getBackgroundHandler() {
        return this.mBgHandler;
    }

    @VisibleForTesting
    HandlerThread getBackgroundHandlerThread() {
        return this.mBgHandlerThread;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object getLock() {
        return this.mLock;
    }

    @VisibleForTesting
    void addAppStateTracker(BaseAppStateTracker baseAppStateTracker) {
        this.mAppStateTrackers.add(baseAppStateTracker);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T extends BaseAppStateTracker> T getAppStateTracker(Class<T> cls) {
        Iterator<BaseAppStateTracker> it = this.mAppStateTrackers.iterator();
        while (it.hasNext()) {
            T t = (T) it.next();
            if (cls.isAssignableFrom(t.getClass())) {
                return t;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postLongRunningFgsIfNecessary(String str, int i) {
        this.mNotificationHelper.postLongRunningFgsIfNecessary(str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelLongRunningFGSNotificationIfNecessary(String str, int i) {
        this.mNotificationHelper.cancelLongRunningFGSNotificationIfNecessary(str, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getPackageName(int i) {
        return this.mInjector.getPackageName(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class BgHandler extends Handler {
        static final int MSG_APP_RESTRICTION_LEVEL_CHANGED = 1;
        static final int MSG_APP_STANDBY_BUCKET_CHANGED = 2;
        static final int MSG_BACKGROUND_RESTRICTION_CHANGED = 0;
        static final int MSG_CANCEL_REQUEST_BG_RESTRICTED = 9;
        static final int MSG_LOAD_RESTRICTION_SETTINGS = 10;
        static final int MSG_PERSIST_RESTRICTION_SETTINGS = 11;
        static final int MSG_REQUEST_BG_RESTRICTED = 4;
        static final int MSG_UID_ACTIVE = 6;
        static final int MSG_UID_GONE = 7;
        static final int MSG_UID_IDLE = 5;
        static final int MSG_UID_PROC_STATE_CHANGED = 8;
        static final int MSG_USER_INTERACTION_STARTED = 3;
        private final Injector mInjector;

        BgHandler(Looper looper, Injector injector) {
            super(looper);
            this.mInjector = injector;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AppRestrictionController appRestrictionController = this.mInjector.getAppRestrictionController();
            switch (message.what) {
                case 0:
                    appRestrictionController.handleBackgroundRestrictionChanged(message.arg1, (String) message.obj, message.arg2 == 1);
                    return;
                case 1:
                    appRestrictionController.dispatchAppRestrictionLevelChanges(message.arg1, (String) message.obj, message.arg2);
                    return;
                case 2:
                    appRestrictionController.handleAppStandbyBucketChanged(message.arg2, (String) message.obj, message.arg1);
                    return;
                case 3:
                    appRestrictionController.onUserInteractionStarted((String) message.obj, message.arg1);
                    return;
                case 4:
                    appRestrictionController.handleRequestBgRestricted((String) message.obj, message.arg1);
                    return;
                case 5:
                    appRestrictionController.handleUidInactive(message.arg1, message.arg2 == 1);
                    return;
                case 6:
                    appRestrictionController.handleUidActive(message.arg1);
                    return;
                case 7:
                    appRestrictionController.handleUidInactive(message.arg1, message.arg2 == 1);
                    appRestrictionController.handleUidGone(message.arg1);
                    return;
                case 8:
                    appRestrictionController.handleUidProcStateChanged(message.arg1, message.arg2);
                    return;
                case 9:
                    appRestrictionController.handleCancelRequestBgRestricted((String) message.obj, message.arg1);
                    return;
                case 10:
                    appRestrictionController.mRestrictionSettings.loadFromXml(true);
                    return;
                case 11:
                    appRestrictionController.mRestrictionSettings.persistToXml(message.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        private ActivityManagerInternal mActivityManagerInternal;
        private AppBatteryExemptionTracker mAppBatteryExemptionTracker;
        private AppBatteryTracker mAppBatteryTracker;
        private AppFGSTracker mAppFGSTracker;
        private AppHibernationManagerInternal mAppHibernationInternal;
        private AppMediaSessionTracker mAppMediaSessionTracker;
        private AppOpsManager mAppOpsManager;
        private AppPermissionTracker mAppPermissionTracker;
        private AppRestrictionController mAppRestrictionController;
        private AppStandbyInternal mAppStandbyInternal;
        private AppStateTracker mAppStateTracker;
        private final Context mContext;
        private IActivityManager mIActivityManager;
        private NotificationManager mNotificationManager;
        private PackageManagerInternal mPackageManagerInternal;
        private RoleManager mRoleManager;
        private TelephonyManager mTelephonyManager;
        private UserManagerInternal mUserManagerInternal;

        boolean isTest() {
            return false;
        }

        Injector(Context context) {
            this.mContext = context;
        }

        Context getContext() {
            return this.mContext;
        }

        void initAppStateTrackers(AppRestrictionController appRestrictionController) {
            this.mAppRestrictionController = appRestrictionController;
            this.mAppBatteryTracker = new AppBatteryTracker(this.mContext, appRestrictionController);
            this.mAppBatteryExemptionTracker = new AppBatteryExemptionTracker(this.mContext, appRestrictionController);
            this.mAppFGSTracker = new AppFGSTracker(this.mContext, appRestrictionController);
            this.mAppMediaSessionTracker = new AppMediaSessionTracker(this.mContext, appRestrictionController);
            this.mAppPermissionTracker = new AppPermissionTracker(this.mContext, appRestrictionController);
            appRestrictionController.mAppStateTrackers.add(this.mAppBatteryTracker);
            appRestrictionController.mAppStateTrackers.add(this.mAppBatteryExemptionTracker);
            appRestrictionController.mAppStateTrackers.add(this.mAppFGSTracker);
            appRestrictionController.mAppStateTrackers.add(this.mAppMediaSessionTracker);
            appRestrictionController.mAppStateTrackers.add(this.mAppPermissionTracker);
            appRestrictionController.mAppStateTrackers.add(new AppBroadcastEventsTracker(this.mContext, appRestrictionController));
            appRestrictionController.mAppStateTrackers.add(new AppBindServiceEventsTracker(this.mContext, appRestrictionController));
        }

        ActivityManagerInternal getActivityManagerInternal() {
            if (this.mActivityManagerInternal == null) {
                this.mActivityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
            }
            return this.mActivityManagerInternal;
        }

        AppRestrictionController getAppRestrictionController() {
            return this.mAppRestrictionController;
        }

        AppOpsManager getAppOpsManager() {
            if (this.mAppOpsManager == null) {
                this.mAppOpsManager = (AppOpsManager) getContext().getSystemService(AppOpsManager.class);
            }
            return this.mAppOpsManager;
        }

        AppStandbyInternal getAppStandbyInternal() {
            if (this.mAppStandbyInternal == null) {
                this.mAppStandbyInternal = (AppStandbyInternal) LocalServices.getService(AppStandbyInternal.class);
            }
            return this.mAppStandbyInternal;
        }

        AppHibernationManagerInternal getAppHibernationInternal() {
            if (this.mAppHibernationInternal == null) {
                this.mAppHibernationInternal = (AppHibernationManagerInternal) LocalServices.getService(AppHibernationManagerInternal.class);
            }
            return this.mAppHibernationInternal;
        }

        AppStateTracker getAppStateTracker() {
            if (this.mAppStateTracker == null) {
                this.mAppStateTracker = (AppStateTracker) LocalServices.getService(AppStateTracker.class);
            }
            return this.mAppStateTracker;
        }

        IActivityManager getIActivityManager() {
            return ActivityManager.getService();
        }

        UserManagerInternal getUserManagerInternal() {
            if (this.mUserManagerInternal == null) {
                this.mUserManagerInternal = (UserManagerInternal) LocalServices.getService(UserManagerInternal.class);
            }
            return this.mUserManagerInternal;
        }

        PackageManagerInternal getPackageManagerInternal() {
            if (this.mPackageManagerInternal == null) {
                this.mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
            }
            return this.mPackageManagerInternal;
        }

        PackageManager getPackageManager() {
            return getContext().getPackageManager();
        }

        NotificationManager getNotificationManager() {
            if (this.mNotificationManager == null) {
                this.mNotificationManager = (NotificationManager) getContext().getSystemService(NotificationManager.class);
            }
            return this.mNotificationManager;
        }

        RoleManager getRoleManager() {
            if (this.mRoleManager == null) {
                this.mRoleManager = (RoleManager) getContext().getSystemService(RoleManager.class);
            }
            return this.mRoleManager;
        }

        TelephonyManager getTelephonyManager() {
            if (this.mTelephonyManager == null) {
                this.mTelephonyManager = (TelephonyManager) getContext().getSystemService(TelephonyManager.class);
            }
            return this.mTelephonyManager;
        }

        AppFGSTracker getAppFGSTracker() {
            return this.mAppFGSTracker;
        }

        AppMediaSessionTracker getAppMediaSessionTracker() {
            return this.mAppMediaSessionTracker;
        }

        ActivityManagerService getActivityManagerService() {
            return this.mAppRestrictionController.mActivityManagerService;
        }

        UidBatteryUsageProvider getUidBatteryUsageProvider() {
            return this.mAppBatteryTracker;
        }

        AppBatteryExemptionTracker getAppBatteryExemptionTracker() {
            return this.mAppBatteryExemptionTracker;
        }

        AppPermissionTracker getAppPermissionTracker() {
            return this.mAppPermissionTracker;
        }

        String getPackageName(int i) {
            ApplicationInfo applicationInfo;
            ActivityManagerService activityManagerService = getActivityManagerService();
            synchronized (activityManagerService.mPidsSelfLocked) {
                ProcessRecord processRecord = activityManagerService.mPidsSelfLocked.get(i);
                if (processRecord == null || (applicationInfo = processRecord.info) == null) {
                    return null;
                }
                return applicationInfo.packageName;
            }
        }

        void scheduleInitTrackers(Handler handler, Runnable runnable) {
            handler.post(runnable);
        }

        File getDataSystemDeDirectory(int i) {
            return Environment.getDataSystemDeDirectory(i);
        }

        long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    private void registerForSystemBroadcasts() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_FULLY_REMOVED");
        intentFilter.addDataScheme(ATTR_PACKAGE);
        this.mContext.registerReceiverForAllUsers(this.mBroadcastReceiver, intentFilter, null, this.mBgHandler);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_ADDED");
        intentFilter2.addAction("android.intent.action.USER_REMOVED");
        intentFilter2.addAction("android.intent.action.UID_REMOVED");
        this.mContext.registerReceiverForAllUsers(this.mBroadcastReceiver, intentFilter2, null, this.mBgHandler);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction("android.intent.action.LOCKED_BOOT_COMPLETED");
        this.mContext.registerReceiverAsUser(this.mBootReceiver, UserHandle.SYSTEM, intentFilter3, null, this.mBgHandler);
        this.mContext.registerReceiverForAllUsers(this.mBroadcastReceiver, new IntentFilter("android.telephony.action.MULTI_SIM_CONFIG_CHANGED"), null, this.mBgHandler);
    }

    private void unregisterForSystemBroadcasts() {
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mContext.unregisterReceiver(this.mBootReceiver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forEachTracker(Consumer<BaseAppStateTracker> consumer) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            consumer.accept(this.mAppStateTrackers.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserAdded(int i) {
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUserAdded(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStarted(int i) {
        refreshAppRestrictionLevelForUser(i, 1024, 2);
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUserStarted(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserStopped(int i) {
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUserStopped(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserRemoved(int i) {
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUserRemoved(i);
        }
        this.mRestrictionSettings.removeUser(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUidAdded(int i) {
        refreshAppRestrictionLevelForUid(i, FrameworkStatsLog.APP_STANDBY_BUCKET_CHANGED__MAIN_REASON__MAIN_FORCED_BY_SYSTEM, 0, false);
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUidAdded(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageRemoved(String str, int i) {
        this.mRestrictionSettings.removePackage(str, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUidRemoved(int i) {
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUidRemoved(i);
        }
        this.mRestrictionSettings.removeUid(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLockedBootCompleted() {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onLockedBootCompleted();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBgAutoRestrictedBucketFeatureFlagEnabled() {
        return this.mConstantsObserver.mBgAutoRestrictedBucket;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPropertiesChanged(String str) {
        int size = this.mAppStateTrackers.size();
        for (int i = 0; i < size; i++) {
            this.mAppStateTrackers.get(i).onPropertiesChanged(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserInteractionStarted(String str, int i) {
        int packageUid = this.mInjector.getPackageManagerInternal().getPackageUid(str, 819200L, i);
        int size = this.mAppStateTrackers.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.mAppStateTrackers.get(i2).onUserInteractionStarted(str, packageUid);
        }
    }

    public IAppRestrictionControllerWrapper getWrapper() {
        return this.mAppRestrictionControllerWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class AppRestrictionControllerWrapper implements IAppRestrictionControllerWrapper {
        private AppRestrictionControllerWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IAppRestrictionControllerExt getExtImpl() {
            return AppRestrictionController.this.mAppRestrictionControllerExt;
        }

        @Override // com.android.server.am.IAppRestrictionControllerWrapper
        public IAppRestrictionControllerExt.IStaticExt getStaticExtImpl() {
            return AppRestrictionController.this.mStaticExt;
        }

        @Override // com.android.server.am.IAppRestrictionControllerWrapper
        public Injector getInjector() {
            return AppRestrictionController.this.mInjector;
        }

        @Override // com.android.server.am.IAppRestrictionControllerWrapper
        public Object getSettingsLock() {
            return AppRestrictionController.this.mSettingsLock;
        }
    }
}
