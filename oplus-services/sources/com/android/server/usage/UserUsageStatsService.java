package com.android.server.usage;

import android.app.usage.ConfigurationStats;
import android.app.usage.EventList;
import android.app.usage.EventStats;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.SparseArrayMap;
import android.util.SparseIntArray;
import android.util.TimeSparseArray;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.hdmi.HdmiCecKeycode;
import com.android.server.net.IOplusNetworkPolicyManagerServiceEx;
import com.android.server.pm.PackageManagerService;
import com.android.server.policy.PhoneWindowManager;
import com.android.server.usage.IntervalStats;
import com.android.server.usage.UsageStatsDatabase;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UserUsageStatsService {
    private static final String TAG = "UsageStatsService";
    private static final int sDateFormatFlags = 131093;
    private final Context mContext;
    private final UsageStatsDatabase mDatabase;
    private String mLastBackgroundedPackage;
    private final StatsUpdatedListener mListener;
    private final String mLogPrefix;
    private final int mUserId;
    private static final boolean DEBUG = UsageStatsService.DEBUG;
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final long[] INTERVAL_LENGTH = {86400000, UnixCalendar.WEEK_IN_MILLIS, UnixCalendar.MONTH_IN_MILLIS, 31536000000L};
    private static final UsageStatsDatabase.StatCombiner<UsageStats> sUsageStatsCombiner = new UsageStatsDatabase.StatCombiner<UsageStats>() { // from class: com.android.server.usage.UserUsageStatsService.1
        @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
        public boolean combine(IntervalStats intervalStats, boolean z, List<UsageStats> list) {
            if (!z) {
                list.addAll(intervalStats.packageStats.values());
                return true;
            }
            int size = intervalStats.packageStats.size();
            for (int i = 0; i < size; i++) {
                list.add(new UsageStats(intervalStats.packageStats.valueAt(i)));
            }
            return true;
        }
    };
    private static final UsageStatsDatabase.StatCombiner<ConfigurationStats> sConfigStatsCombiner = new UsageStatsDatabase.StatCombiner<ConfigurationStats>() { // from class: com.android.server.usage.UserUsageStatsService.2
        @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
        public boolean combine(IntervalStats intervalStats, boolean z, List<ConfigurationStats> list) {
            if (!z) {
                list.addAll(intervalStats.configurations.values());
                return true;
            }
            int size = intervalStats.configurations.size();
            for (int i = 0; i < size; i++) {
                list.add(new ConfigurationStats(intervalStats.configurations.valueAt(i)));
            }
            return true;
        }
    };
    private static final UsageStatsDatabase.StatCombiner<EventStats> sEventStatsCombiner = new UsageStatsDatabase.StatCombiner<EventStats>() { // from class: com.android.server.usage.UserUsageStatsService.3
        @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
        public boolean combine(IntervalStats intervalStats, boolean z, List<EventStats> list) {
            intervalStats.addEventStatsTo(list);
            return true;
        }
    };
    private boolean mStatsChanged = false;
    private final SparseArrayMap<String, CachedEarlyEvents> mCachedEarlyEvents = new SparseArrayMap<>();
    private final UnixCalendar mDailyExpiryDate = new UnixCalendar(0);
    private final IntervalStats[] mCurrentStats = new IntervalStats[4];
    private long mRealTimeSnapshot = SystemClock.elapsedRealtime();
    private long mSystemTimeSnapshot = System.currentTimeMillis();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface StatsUpdatedListener {
        void onNewUpdate(int i);

        void onStatsReloaded();

        void onStatsUpdated();
    }

    public static String intervalToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "?" : "yearly" : "monthly" : "weekly" : IOplusNetworkPolicyManagerServiceEx.TYPE_DAILY;
    }

    private static boolean validRange(long j, long j2, long j3) {
        return j2 <= j && j2 < j3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class CachedEarlyEvents {
        public long eventTime;
        public List<UsageEvents.Event> events;
        public long searchBeginTime;

        private CachedEarlyEvents() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserUsageStatsService(Context context, int i, File file, StatsUpdatedListener statsUpdatedListener) {
        this.mContext = context;
        this.mDatabase = new UsageStatsDatabase(file);
        this.mListener = statsUpdatedListener;
        this.mLogPrefix = "User[" + Integer.toString(i) + "] ";
        this.mUserId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(long j, HashMap<String, Long> hashMap, boolean z) {
        IntervalStats[] intervalStatsArr;
        readPackageMappingsLocked(hashMap, z);
        this.mDatabase.init(j);
        if (this.mDatabase.wasUpgradePerformed()) {
            this.mDatabase.prunePackagesDataOnUpgrade(hashMap);
        }
        int i = 0;
        int i2 = 0;
        while (true) {
            intervalStatsArr = this.mCurrentStats;
            if (i >= intervalStatsArr.length) {
                break;
            }
            intervalStatsArr[i] = this.mDatabase.getLatestUsageStats(i);
            if (this.mCurrentStats[i] == null) {
                i2++;
            }
            i++;
        }
        if (i2 > 0) {
            if (i2 != intervalStatsArr.length) {
                Slog.w(TAG, this.mLogPrefix + "Some stats have no latest available");
            }
            loadActiveStats(j);
        } else {
            updateRolloverDeadline();
        }
        IntervalStats intervalStats = this.mCurrentStats[0];
        if (intervalStats != null) {
            UsageEvents.Event event = new UsageEvents.Event(26, Math.max(intervalStats.lastTimeSaved, intervalStats.endTime));
            event.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
            intervalStats.addEvent(event);
            UsageEvents.Event event2 = new UsageEvents.Event(27, System.currentTimeMillis());
            event2.mPackage = PackageManagerService.PLATFORM_PACKAGE_NAME;
            intervalStats.addEvent(event2);
        }
        if (this.mDatabase.isNewUpdate()) {
            notifyNewUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void userStopped() {
        persistActiveStats();
        this.mCachedEarlyEvents.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int onPackageRemoved(String str, long j) {
        for (int numMaps = this.mCachedEarlyEvents.numMaps() - 1; numMaps >= 0; numMaps--) {
            this.mCachedEarlyEvents.delete(this.mCachedEarlyEvents.keyAt(numMaps), str);
        }
        return this.mDatabase.onPackageRemoved(str, j);
    }

    private void readPackageMappingsLocked(HashMap<String, Long> hashMap, boolean z) {
        this.mDatabase.readMappingsLocked();
        if (this.mUserId == 0 || !z) {
            return;
        }
        updatePackageMappingsLocked(hashMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updatePackageMappingsLocked(HashMap<String, Long> hashMap) {
        if (ArrayUtils.isEmpty(hashMap)) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        for (int size = this.mDatabase.mPackagesTokenData.packagesToTokensMap.size() - 1; size >= 0; size--) {
            String keyAt = this.mDatabase.mPackagesTokenData.packagesToTokensMap.keyAt(size);
            if (!hashMap.containsKey(keyAt)) {
                arrayList.add(keyAt);
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            this.mDatabase.mPackagesTokenData.removePackage((String) arrayList.get(size2), currentTimeMillis);
        }
        try {
            this.mDatabase.writeMappingsLocked();
            return true;
        } catch (Exception unused) {
            Slog.w(TAG, "Unable to write updated package mappings file on service initialization.");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean pruneUninstalledPackagesData() {
        return this.mDatabase.pruneUninstalledPackagesData();
    }

    private void onTimeChanged(long j, long j2) {
        this.mCachedEarlyEvents.clear();
        persistActiveStats();
        this.mDatabase.onTimeChanged(j2 - j, j2);
        loadActiveStats(j2);
    }

    private long checkAndGetTimeLocked() {
        long currentTimeMillis = System.currentTimeMillis();
        if (!UsageStatsService.ENABLE_TIME_CHANGE_CORRECTION) {
            return currentTimeMillis;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = (elapsedRealtime - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot;
        long j2 = currentTimeMillis - j;
        if (Math.abs(j2) > 2000) {
            Slog.i(TAG, this.mLogPrefix + "Time changed in by " + (j2 / 1000) + " seconds");
            onTimeChanged(j, currentTimeMillis);
            this.mRealTimeSnapshot = elapsedRealtime;
            this.mSystemTimeSnapshot = currentTimeMillis;
        }
        return currentTimeMillis;
    }

    private void convertToSystemTimeLocked(UsageEvents.Event event) {
        event.mTimeStamp = Math.max(0L, event.mTimeStamp - this.mRealTimeSnapshot) + this.mSystemTimeSnapshot;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0098, code lost:
    
        if (r2.equals(r17.mLastBackgroundedPackage) == false) goto L40;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void reportEvent(UsageEvents.Event event) {
        String str;
        Configuration configuration;
        if (DEBUG) {
            Slog.d(TAG, this.mLogPrefix + "Got usage event for " + event.mPackage + "[" + event.mTimeStamp + "]: " + eventToString(event.mEventType));
        }
        int i = event.mEventType;
        if (i != 7 && i != 31) {
            checkAndGetTimeLocked();
            convertToSystemTimeLocked(event);
        }
        if (event.mTimeStamp >= this.mDailyExpiryDate.getTimeInMillis()) {
            rolloverStats(event.mTimeStamp);
        }
        IntervalStats intervalStats = this.mCurrentStats[0];
        Configuration configuration2 = event.mConfiguration;
        if (event.mEventType == 5 && (configuration = intervalStats.activeConfiguration) != null) {
            event.mConfiguration = Configuration.generateDelta(configuration, configuration2);
        }
        int i2 = event.mEventType;
        if (i2 != 6 && i2 != 24 && i2 != 25 && i2 != 26 && i2 != 31) {
            intervalStats.addEvent(event);
        }
        int i3 = event.mEventType;
        boolean z = true;
        if (i3 == 1) {
            String str2 = event.mPackage;
            if (str2 != null) {
            }
        } else if (i3 == 2 && (str = event.mPackage) != null) {
            this.mLastBackgroundedPackage = str;
        }
        z = false;
        for (IntervalStats intervalStats2 : this.mCurrentStats) {
            int i4 = event.mEventType;
            if (i4 != 5) {
                if (i4 == 9) {
                    intervalStats2.updateChooserCounts(event.mPackage, event.mContentType, event.mAction);
                    String[] strArr = event.mContentAnnotations;
                    if (strArr != null) {
                        for (String str3 : strArr) {
                            intervalStats2.updateChooserCounts(event.mPackage, str3, event.mAction);
                        }
                    }
                } else {
                    switch (i4) {
                        case 15:
                            intervalStats2.updateScreenInteractive(event.mTimeStamp);
                            break;
                        case 16:
                            intervalStats2.updateScreenNonInteractive(event.mTimeStamp);
                            break;
                        case 17:
                            intervalStats2.updateKeyguardShown(event.mTimeStamp);
                            break;
                        case 18:
                            intervalStats2.updateKeyguardHidden(event.mTimeStamp);
                            break;
                        default:
                            intervalStats2.update(event.mPackage, event.getClassName(), event.mTimeStamp, event.mEventType, event.mInstanceId);
                            if (z) {
                                intervalStats2.incrementAppLaunchCount(event.mPackage);
                                break;
                            } else {
                                break;
                            }
                    }
                }
            } else {
                intervalStats2.updateConfigurationStats(configuration2, event.mTimeStamp);
            }
        }
        notifyStatsChanged();
    }

    private <T> List<T> queryStats(int i, long j, long j2, UsageStatsDatabase.StatCombiner<T> statCombiner) {
        int i2;
        if (i == 4) {
            int findBestFitBucket = this.mDatabase.findBestFitBucket(j, j2);
            i2 = findBestFitBucket < 0 ? 0 : findBestFitBucket;
        } else {
            i2 = i;
        }
        if (i2 >= 0) {
            IntervalStats[] intervalStatsArr = this.mCurrentStats;
            if (i2 < intervalStatsArr.length) {
                IntervalStats intervalStats = intervalStatsArr[i2];
                boolean z = DEBUG;
                if (z) {
                    Slog.d(TAG, this.mLogPrefix + "SELECT * FROM " + i2 + " WHERE beginTime >= " + j + " AND endTime < " + j2);
                }
                if (j >= intervalStats.endTime) {
                    if (z) {
                        Slog.d(TAG, this.mLogPrefix + "Requesting stats after " + j + " but latest is " + intervalStats.endTime);
                    }
                    return null;
                }
                List<T> queryUsageStats = this.mDatabase.queryUsageStats(i2, j, Math.min(intervalStats.beginTime, j2), statCombiner);
                if (z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Got ");
                    sb.append(queryUsageStats != null ? queryUsageStats.size() : 0);
                    sb.append(" results from disk");
                    Slog.d(TAG, sb.toString());
                    Slog.d(TAG, "Current stats beginTime=" + intervalStats.beginTime + " endTime=" + intervalStats.endTime);
                }
                int size = queryUsageStats != null ? queryUsageStats.size() : 0;
                if (j < intervalStats.endTime && j2 > intervalStats.beginTime) {
                    if (z) {
                        Slog.d(TAG, this.mLogPrefix + "Returning in-memory stats");
                    }
                    if (queryUsageStats == null) {
                        queryUsageStats = new ArrayList<>();
                    }
                    this.mDatabase.filterStats(intervalStats);
                    statCombiner.combine(intervalStats, true, queryUsageStats);
                }
                if (z) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.mLogPrefix);
                    sb2.append("Results: ");
                    sb2.append(queryUsageStats != null ? queryUsageStats.size() : 0);
                    Slog.d(TAG, sb2.toString());
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(this.mLogPrefix);
                sb3.append("type=");
                sb3.append(i2);
                sb3.append(" b=");
                sb3.append(j);
                sb3.append(" e=");
                sb3.append(j2);
                sb3.append(" cb=");
                sb3.append(intervalStats.beginTime);
                sb3.append(" ce=");
                sb3.append(intervalStats.endTime);
                sb3.append(" disk=");
                sb3.append(size);
                sb3.append(" all=");
                sb3.append(queryUsageStats != null ? queryUsageStats.size() : 0);
                Slog.d(TAG, sb3.toString());
                return queryUsageStats;
            }
        }
        if (DEBUG) {
            Slog.d(TAG, this.mLogPrefix + "Bad intervalType used " + i2);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<UsageStats> queryUsageStats(int i, long j, long j2) {
        if (validRange(checkAndGetTimeLocked(), j, j2)) {
            return queryStats(i, j, j2, sUsageStatsCombiner);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<ConfigurationStats> queryConfigurationStats(int i, long j, long j2) {
        if (validRange(checkAndGetTimeLocked(), j, j2)) {
            return queryStats(i, j, j2, sConfigStatsCombiner);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<EventStats> queryEventStats(int i, long j, long j2) {
        if (validRange(checkAndGetTimeLocked(), j, j2)) {
            return queryStats(i, j, j2, sEventStatsCombiner);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsageEvents queryEvents(final long j, final long j2, final int i) {
        if (!validRange(checkAndGetTimeLocked(), j, j2)) {
            return null;
        }
        final ArraySet arraySet = new ArraySet();
        List queryStats = queryStats(0, j, j2, new UsageStatsDatabase.StatCombiner<UsageEvents.Event>() { // from class: com.android.server.usage.UserUsageStatsService.4
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public boolean combine(IntervalStats intervalStats, boolean z, List<UsageEvents.Event> list) {
                int size = intervalStats.events.size();
                for (int firstIndexOnOrAfter = intervalStats.events.firstIndexOnOrAfter(j); firstIndexOnOrAfter < size; firstIndexOnOrAfter++) {
                    UsageEvents.Event event = intervalStats.events.get(firstIndexOnOrAfter);
                    if (event.mTimeStamp >= j2) {
                        return false;
                    }
                    int i2 = event.mEventType;
                    if ((i2 != 8 || (i & 2) != 2) && (i2 != 30 || (i & 8) != 8)) {
                        if ((i2 == 10 || i2 == 12) && (i & 4) == 4) {
                            event = event.getObfuscatedNotificationEvent();
                        }
                        if ((i & 1) == 1) {
                            event = event.getObfuscatedIfInstantApp();
                        }
                        String str = event.mPackage;
                        if (str != null) {
                            arraySet.add(str);
                        }
                        String str2 = event.mClass;
                        if (str2 != null) {
                            arraySet.add(str2);
                        }
                        String str3 = event.mTaskRootPackage;
                        if (str3 != null) {
                            arraySet.add(str3);
                        }
                        String str4 = event.mTaskRootClass;
                        if (str4 != null) {
                            arraySet.add(str4);
                        }
                        list.add(event);
                    }
                }
                return true;
            }
        });
        if (queryStats == null || queryStats.isEmpty()) {
            return null;
        }
        String[] strArr = (String[]) arraySet.toArray(new String[arraySet.size()]);
        Arrays.sort(strArr);
        return new UsageEvents(queryStats, strArr, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsageEvents queryEarliestAppEvents(final long j, final long j2, final int i) {
        if (!validRange(checkAndGetTimeLocked(), j, j2)) {
            return null;
        }
        final ArraySet arraySet = new ArraySet();
        final ArraySet arraySet2 = new ArraySet();
        List queryStats = queryStats(0, j, j2, new UsageStatsDatabase.StatCombiner() { // from class: com.android.server.usage.UserUsageStatsService$$ExternalSyntheticLambda1
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public final boolean combine(IntervalStats intervalStats, boolean z, List list) {
                boolean lambda$queryEarliestAppEvents$0;
                lambda$queryEarliestAppEvents$0 = UserUsageStatsService.lambda$queryEarliestAppEvents$0(j, j2, arraySet2, arraySet, i, intervalStats, z, list);
                return lambda$queryEarliestAppEvents$0;
            }
        });
        if (queryStats == null || queryStats.isEmpty()) {
            return null;
        }
        if (DEBUG) {
            Slog.d(TAG, "Found " + queryStats.size() + " early events for " + arraySet.size() + " apps");
        }
        String[] strArr = (String[]) arraySet.toArray(new String[arraySet.size()]);
        Arrays.sort(strArr);
        return new UsageEvents(queryStats, strArr, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$queryEarliestAppEvents$0(long j, long j2, ArraySet arraySet, ArraySet arraySet2, int i, IntervalStats intervalStats, boolean z, List list) {
        int size = intervalStats.events.size();
        for (int firstIndexOnOrAfter = intervalStats.events.firstIndexOnOrAfter(j); firstIndexOnOrAfter < size; firstIndexOnOrAfter++) {
            UsageEvents.Event event = intervalStats.events.get(firstIndexOnOrAfter);
            if (event.getTimeStamp() >= j2) {
                return false;
            }
            if (event.getPackageName() != null && !arraySet.contains(event.getPackageName())) {
                boolean add = arraySet2.add(event.getPackageName());
                if (event.getEventType() == i) {
                    list.add(event);
                    arraySet.add(event.getPackageName());
                } else if (add) {
                    list.add(event);
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsageEvents queryEventsForPackage(final long j, final long j2, final String str, final boolean z) {
        if (!validRange(checkAndGetTimeLocked(), j, j2)) {
            return null;
        }
        final ArraySet arraySet = new ArraySet();
        arraySet.add(str);
        List queryStats = queryStats(0, j, j2, new UsageStatsDatabase.StatCombiner() { // from class: com.android.server.usage.UserUsageStatsService$$ExternalSyntheticLambda2
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public final boolean combine(IntervalStats intervalStats, boolean z2, List list) {
                boolean lambda$queryEventsForPackage$1;
                lambda$queryEventsForPackage$1 = UserUsageStatsService.lambda$queryEventsForPackage$1(j, j2, str, arraySet, z, intervalStats, z2, list);
                return lambda$queryEventsForPackage$1;
            }
        });
        if (queryStats == null || queryStats.isEmpty()) {
            return null;
        }
        String[] strArr = (String[]) arraySet.toArray(new String[arraySet.size()]);
        Arrays.sort(strArr);
        return new UsageEvents(queryStats, strArr, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$queryEventsForPackage$1(long j, long j2, String str, ArraySet arraySet, boolean z, IntervalStats intervalStats, boolean z2, List list) {
        String str2;
        String str3;
        int size = intervalStats.events.size();
        for (int firstIndexOnOrAfter = intervalStats.events.firstIndexOnOrAfter(j); firstIndexOnOrAfter < size; firstIndexOnOrAfter++) {
            UsageEvents.Event event = intervalStats.events.get(firstIndexOnOrAfter);
            if (event.mTimeStamp >= j2) {
                return false;
            }
            if (str.equals(event.mPackage)) {
                String str4 = event.mClass;
                if (str4 != null) {
                    arraySet.add(str4);
                }
                if (z && (str3 = event.mTaskRootPackage) != null) {
                    arraySet.add(str3);
                }
                if (z && (str2 = event.mTaskRootClass) != null) {
                    arraySet.add(str2);
                }
                list.add(event);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsageEvents queryEarliestEventsForPackage(long j, final long j2, final String str, final int i) {
        long j3;
        CachedEarlyEvents cachedEarlyEvents;
        List<UsageEvents.Event> queryStats;
        long checkAndGetTimeLocked = checkAndGetTimeLocked();
        if (!validRange(checkAndGetTimeLocked, j, j2)) {
            return null;
        }
        CachedEarlyEvents cachedEarlyEvents2 = (CachedEarlyEvents) this.mCachedEarlyEvents.get(i, str);
        if (cachedEarlyEvents2 == null) {
            cachedEarlyEvents2 = new CachedEarlyEvents();
            cachedEarlyEvents2.searchBeginTime = j;
            this.mCachedEarlyEvents.add(i, str, cachedEarlyEvents2);
        } else {
            if (cachedEarlyEvents2.searchBeginTime <= j && j <= cachedEarlyEvents2.eventTime) {
                List<UsageEvents.Event> list = cachedEarlyEvents2.events;
                int size = list == null ? 0 : list.size();
                if (size == 0 || cachedEarlyEvents2.events.get(size - 1).getEventType() != i) {
                    long j4 = cachedEarlyEvents2.eventTime;
                    if (j4 < j2) {
                        cachedEarlyEvents = cachedEarlyEvents2;
                        j3 = Math.min(checkAndGetTimeLocked, j4);
                        final long j5 = j3;
                        queryStats = queryStats(0, j5, j2, new UsageStatsDatabase.StatCombiner() { // from class: com.android.server.usage.UserUsageStatsService$$ExternalSyntheticLambda0
                            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
                            public final boolean combine(IntervalStats intervalStats, boolean z, List list2) {
                                boolean lambda$queryEarliestEventsForPackage$2;
                                lambda$queryEarliestEventsForPackage$2 = UserUsageStatsService.lambda$queryEarliestEventsForPackage$2(j5, j2, str, i, intervalStats, z, list2);
                                return lambda$queryEarliestEventsForPackage$2;
                            }
                        });
                        if (queryStats != null || queryStats.isEmpty()) {
                            cachedEarlyEvents.eventTime = Math.min(checkAndGetTimeLocked, j2);
                            cachedEarlyEvents.events = null;
                            return null;
                        }
                        cachedEarlyEvents.eventTime = queryStats.get(queryStats.size() - 1).getTimeStamp();
                        cachedEarlyEvents.events = queryStats;
                        return new UsageEvents(queryStats, new String[]{str}, false);
                    }
                }
                if (cachedEarlyEvents2.eventTime > j2 || cachedEarlyEvents2.events == null) {
                    return null;
                }
                return new UsageEvents(cachedEarlyEvents2.events, new String[]{str}, false);
            }
            cachedEarlyEvents2.searchBeginTime = j;
        }
        j3 = j;
        cachedEarlyEvents = cachedEarlyEvents2;
        final long j52 = j3;
        queryStats = queryStats(0, j52, j2, new UsageStatsDatabase.StatCombiner() { // from class: com.android.server.usage.UserUsageStatsService$$ExternalSyntheticLambda0
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public final boolean combine(IntervalStats intervalStats, boolean z, List list2) {
                boolean lambda$queryEarliestEventsForPackage$2;
                lambda$queryEarliestEventsForPackage$2 = UserUsageStatsService.lambda$queryEarliestEventsForPackage$2(j52, j2, str, i, intervalStats, z, list2);
                return lambda$queryEarliestEventsForPackage$2;
            }
        });
        if (queryStats != null) {
        }
        cachedEarlyEvents.eventTime = Math.min(checkAndGetTimeLocked, j2);
        cachedEarlyEvents.events = null;
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$queryEarliestEventsForPackage$2(long j, long j2, String str, int i, IntervalStats intervalStats, boolean z, List list) {
        int size = intervalStats.events.size();
        for (int firstIndexOnOrAfter = intervalStats.events.firstIndexOnOrAfter(j); firstIndexOnOrAfter < size; firstIndexOnOrAfter++) {
            UsageEvents.Event event = intervalStats.events.get(firstIndexOnOrAfter);
            if (event.getTimeStamp() >= j2) {
                return false;
            }
            if (str.equals(event.getPackageName())) {
                if (event.getEventType() == i) {
                    list.add(event);
                    return false;
                }
                if (list.size() == 0) {
                    list.add(event);
                }
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void persistActiveStats() {
        if (!this.mStatsChanged) {
            return;
        }
        Slog.i(TAG, this.mLogPrefix + "Flushing usage stats to disk");
        try {
            this.mDatabase.obfuscateCurrentStats(this.mCurrentStats);
            this.mDatabase.writeMappingsLocked();
            int i = 0;
            while (true) {
                IntervalStats[] intervalStatsArr = this.mCurrentStats;
                if (i < intervalStatsArr.length) {
                    this.mDatabase.putUsageStats(i, intervalStatsArr[i]);
                    i++;
                } else {
                    this.mStatsChanged = false;
                    return;
                }
            }
        } catch (IOException e) {
            Slog.e(TAG, this.mLogPrefix + "Failed to persist active stats", e);
        }
    }

    private void rolloverStats(long j) {
        int i;
        ArraySet arraySet;
        IntervalStats[] intervalStatsArr;
        int i2;
        int i3;
        IntervalStats intervalStats;
        UsageStats usageStats;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Slog.i(TAG, this.mLogPrefix + "Rolling over usage stats");
        int i4 = 0;
        Configuration configuration = this.mCurrentStats[0].activeConfiguration;
        ArraySet arraySet2 = new ArraySet();
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        IntervalStats[] intervalStatsArr2 = this.mCurrentStats;
        int length = intervalStatsArr2.length;
        int i5 = 0;
        while (i5 < length) {
            IntervalStats intervalStats2 = intervalStatsArr2[i5];
            int size = intervalStats2.packageStats.size();
            int i6 = i4;
            while (i6 < size) {
                UsageStats valueAt = intervalStats2.packageStats.valueAt(i6);
                if (valueAt.mActivities.size() > 0 || !valueAt.mForegroundServices.isEmpty()) {
                    if (valueAt.mActivities.size() > 0) {
                        intervalStatsArr = intervalStatsArr2;
                        arrayMap.put(valueAt.mPackageName, valueAt.mActivities);
                        i2 = i6;
                        i3 = size;
                        intervalStats = intervalStats2;
                        intervalStats2.update(valueAt.mPackageName, null, this.mDailyExpiryDate.getTimeInMillis() - 1, 3, 0);
                        usageStats = valueAt;
                    } else {
                        intervalStatsArr = intervalStatsArr2;
                        i2 = i6;
                        i3 = size;
                        intervalStats = intervalStats2;
                        usageStats = valueAt;
                    }
                    if (!usageStats.mForegroundServices.isEmpty()) {
                        arrayMap2.put(usageStats.mPackageName, usageStats.mForegroundServices);
                        intervalStats.update(usageStats.mPackageName, null, this.mDailyExpiryDate.getTimeInMillis() - 1, 22, 0);
                    }
                    arraySet2.add(usageStats.mPackageName);
                    notifyStatsChanged();
                } else {
                    intervalStatsArr = intervalStatsArr2;
                    i2 = i6;
                    i3 = size;
                    intervalStats = intervalStats2;
                }
                i6 = i2 + 1;
                size = i3;
                intervalStatsArr2 = intervalStatsArr;
                intervalStats2 = intervalStats;
            }
            IntervalStats intervalStats3 = intervalStats2;
            intervalStats3.updateConfigurationStats(null, this.mDailyExpiryDate.getTimeInMillis() - 1);
            intervalStats3.commitTime(this.mDailyExpiryDate.getTimeInMillis() - 1);
            i5++;
            intervalStatsArr2 = intervalStatsArr2;
            i4 = 0;
        }
        persistActiveStats();
        this.mDatabase.prune(j);
        loadActiveStats(j);
        int size2 = arraySet2.size();
        for (int i7 = 0; i7 < size2; i7++) {
            String str = (String) arraySet2.valueAt(i7);
            IntervalStats[] intervalStatsArr3 = this.mCurrentStats;
            long j2 = intervalStatsArr3[0].beginTime;
            int length2 = intervalStatsArr3.length;
            int i8 = 0;
            while (i8 < length2) {
                long j3 = j2;
                IntervalStats intervalStats4 = intervalStatsArr3[i8];
                if (arrayMap.containsKey(str)) {
                    SparseIntArray sparseIntArray = (SparseIntArray) arrayMap.get(str);
                    i = size2;
                    int size3 = sparseIntArray.size();
                    arraySet = arraySet2;
                    int i9 = 0;
                    while (i9 < size3) {
                        long j4 = j3;
                        intervalStats4.update(str, null, j4, sparseIntArray.valueAt(i9), sparseIntArray.keyAt(i9));
                        i9++;
                        intervalStats4 = intervalStats4;
                        intervalStatsArr3 = intervalStatsArr3;
                        i8 = i8;
                        j3 = j4;
                        sparseIntArray = sparseIntArray;
                        length2 = length2;
                    }
                } else {
                    i = size2;
                    arraySet = arraySet2;
                }
                int i10 = i8;
                IntervalStats intervalStats5 = intervalStats4;
                IntervalStats[] intervalStatsArr4 = intervalStatsArr3;
                int i11 = length2;
                long j5 = j3;
                if (arrayMap2.containsKey(str)) {
                    ArrayMap arrayMap3 = (ArrayMap) arrayMap2.get(str);
                    int size4 = arrayMap3.size();
                    for (int i12 = 0; i12 < size4; i12++) {
                        intervalStats5.update(str, (String) arrayMap3.keyAt(i12), j5, ((Integer) arrayMap3.valueAt(i12)).intValue(), 0);
                    }
                }
                intervalStats5.updateConfigurationStats(configuration, j5);
                notifyStatsChanged();
                i8 = i10 + 1;
                j2 = j5;
                arraySet2 = arraySet;
                intervalStatsArr3 = intervalStatsArr4;
                length2 = i11;
                size2 = i;
            }
        }
        persistActiveStats();
        Slog.i(TAG, this.mLogPrefix + "Rolling over usage stats complete. Took " + (SystemClock.elapsedRealtime() - elapsedRealtime) + " milliseconds");
    }

    private void notifyStatsChanged() {
        if (this.mStatsChanged) {
            return;
        }
        this.mStatsChanged = true;
        this.mListener.onStatsUpdated();
    }

    private void notifyNewUpdate() {
        this.mListener.onNewUpdate(this.mUserId);
    }

    private void loadActiveStats(long j) {
        for (int i = 0; i < this.mCurrentStats.length; i++) {
            IntervalStats latestUsageStats = this.mDatabase.getLatestUsageStats(i);
            if (latestUsageStats != null && j < latestUsageStats.beginTime + INTERVAL_LENGTH[i]) {
                if (DEBUG) {
                    Slog.d(TAG, this.mLogPrefix + "Loading existing stats @ " + sDateFormat.format(Long.valueOf(latestUsageStats.beginTime)) + "(" + latestUsageStats.beginTime + ") for interval " + i);
                }
                this.mCurrentStats[i] = latestUsageStats;
            } else {
                if (DEBUG) {
                    Slog.d(TAG, "Creating new stats @ " + sDateFormat.format(Long.valueOf(j)) + "(" + j + ") for interval " + i);
                }
                this.mCurrentStats[i] = new IntervalStats();
                IntervalStats intervalStats = this.mCurrentStats[i];
                intervalStats.beginTime = j;
                intervalStats.endTime = 1 + j;
            }
        }
        this.mStatsChanged = false;
        updateRolloverDeadline();
        this.mListener.onStatsReloaded();
    }

    private void updateRolloverDeadline() {
        this.mDailyExpiryDate.setTimeInMillis(this.mCurrentStats[0].beginTime);
        this.mDailyExpiryDate.addDays(1);
        Slog.i(TAG, this.mLogPrefix + "Rollover scheduled @ " + sDateFormat.format(Long.valueOf(this.mDailyExpiryDate.getTimeInMillis())) + "(" + this.mDailyExpiryDate.getTimeInMillis() + ")");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkin(final IndentingPrintWriter indentingPrintWriter) {
        this.mDatabase.checkinDailyFiles(new UsageStatsDatabase.CheckinAction() { // from class: com.android.server.usage.UserUsageStatsService.5
            @Override // com.android.server.usage.UsageStatsDatabase.CheckinAction
            public boolean checkin(IntervalStats intervalStats) {
                UserUsageStatsService.this.printIntervalStats(indentingPrintWriter, intervalStats, false, false, null);
                return true;
            }
        });
    }

    void dump(IndentingPrintWriter indentingPrintWriter, List<String> list) {
        dump(indentingPrintWriter, list, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(IndentingPrintWriter indentingPrintWriter, List<String> list, boolean z) {
        printLast24HrEvents(indentingPrintWriter, !z, list);
        for (int i = 0; i < this.mCurrentStats.length; i++) {
            indentingPrintWriter.print("In-memory ");
            indentingPrintWriter.print(intervalToString(i));
            indentingPrintWriter.println(" stats");
            printIntervalStats(indentingPrintWriter, this.mCurrentStats[i], !z, true, list);
        }
        if (CollectionUtils.isEmpty(list)) {
            this.mDatabase.dump(indentingPrintWriter, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDatabaseInfo(IndentingPrintWriter indentingPrintWriter) {
        this.mDatabase.dump(indentingPrintWriter, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpMappings(IndentingPrintWriter indentingPrintWriter) {
        this.mDatabase.dumpMappings(indentingPrintWriter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpFile(IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            int length = this.mDatabase.mSortedStatFiles.length;
            for (int i = 0; i < length; i++) {
                indentingPrintWriter.println("interval=" + intervalToString(i));
                indentingPrintWriter.increaseIndent();
                dumpFileDetailsForInterval(indentingPrintWriter, i);
                indentingPrintWriter.decreaseIndent();
            }
            return;
        }
        try {
            int stringToInterval = stringToInterval(strArr[0]);
            if (stringToInterval == -1) {
                stringToInterval = Integer.valueOf(strArr[0]).intValue();
            }
            if (stringToInterval < 0 || stringToInterval >= this.mDatabase.mSortedStatFiles.length) {
                indentingPrintWriter.println("the specified interval does not exist.");
                return;
            }
            if (strArr.length == 1) {
                dumpFileDetailsForInterval(indentingPrintWriter, stringToInterval);
                return;
            }
            try {
                IntervalStats readIntervalStatsForFile = this.mDatabase.readIntervalStatsForFile(stringToInterval, Long.valueOf(strArr[1]).longValue());
                if (readIntervalStatsForFile == null) {
                    indentingPrintWriter.println("the specified filename does not exist.");
                } else {
                    dumpFileDetails(indentingPrintWriter, readIntervalStatsForFile, Long.valueOf(strArr[1]).longValue());
                }
            } catch (NumberFormatException unused) {
                indentingPrintWriter.println("invalid filename specified.");
            }
        } catch (NumberFormatException unused2) {
            indentingPrintWriter.println("invalid interval specified.");
        }
    }

    private void dumpFileDetailsForInterval(IndentingPrintWriter indentingPrintWriter, int i) {
        TimeSparseArray<AtomicFile> timeSparseArray = this.mDatabase.mSortedStatFiles[i];
        int size = timeSparseArray.size();
        for (int i2 = 0; i2 < size; i2++) {
            long keyAt = timeSparseArray.keyAt(i2);
            dumpFileDetails(indentingPrintWriter, this.mDatabase.readIntervalStatsForFile(i, keyAt), keyAt);
            indentingPrintWriter.println();
        }
    }

    private void dumpFileDetails(IndentingPrintWriter indentingPrintWriter, IntervalStats intervalStats, long j) {
        indentingPrintWriter.println("file=" + j);
        indentingPrintWriter.increaseIndent();
        printIntervalStats(indentingPrintWriter, intervalStats, false, false, null);
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatDateTime(long j, boolean z) {
        if (z) {
            return "\"" + sDateFormat.format(Long.valueOf(j)) + "\"";
        }
        return Long.toString(j);
    }

    private String formatElapsedTime(long j, boolean z) {
        if (z) {
            return "\"" + DateUtils.formatElapsedTime(j / 1000) + "\"";
        }
        return Long.toString(j);
    }

    void printEvent(IndentingPrintWriter indentingPrintWriter, UsageEvents.Event event, boolean z) {
        indentingPrintWriter.printPair("time", formatDateTime(event.mTimeStamp, z));
        indentingPrintWriter.printPair("type", eventToString(event.mEventType));
        indentingPrintWriter.printPair("package", event.mPackage);
        String str = event.mClass;
        if (str != null) {
            indentingPrintWriter.printPair("class", str);
        }
        Configuration configuration = event.mConfiguration;
        if (configuration != null) {
            indentingPrintWriter.printPair("config", Configuration.resourceQualifierString(configuration));
        }
        String str2 = event.mShortcutId;
        if (str2 != null) {
            indentingPrintWriter.printPair("shortcutId", str2);
        }
        int i = event.mEventType;
        if (i == 11) {
            indentingPrintWriter.printPair("standbyBucket", Integer.valueOf(event.getAppStandbyBucket()));
            indentingPrintWriter.printPair(PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, UsageStatsManager.reasonToString(event.getStandbyReason()));
        } else if (i == 1 || i == 2 || i == 23) {
            indentingPrintWriter.printPair("instanceId", Integer.valueOf(event.getInstanceId()));
        }
        if (event.getTaskRootPackageName() != null) {
            indentingPrintWriter.printPair("taskRootPackage", event.getTaskRootPackageName());
        }
        if (event.getTaskRootClassName() != null) {
            indentingPrintWriter.printPair("taskRootClass", event.getTaskRootClassName());
        }
        String str3 = event.mNotificationChannelId;
        if (str3 != null) {
            indentingPrintWriter.printPair("channelId", str3);
        }
        indentingPrintWriter.printHexPair("flags", event.mFlags);
        indentingPrintWriter.println();
    }

    void printLast24HrEvents(IndentingPrintWriter indentingPrintWriter, boolean z, final List<String> list) {
        final long currentTimeMillis = System.currentTimeMillis();
        UnixCalendar unixCalendar = new UnixCalendar(currentTimeMillis);
        unixCalendar.addDays(-1);
        final long timeInMillis = unixCalendar.getTimeInMillis();
        List queryStats = queryStats(0, timeInMillis, currentTimeMillis, new UsageStatsDatabase.StatCombiner<UsageEvents.Event>() { // from class: com.android.server.usage.UserUsageStatsService.6
            @Override // com.android.server.usage.UsageStatsDatabase.StatCombiner
            public boolean combine(IntervalStats intervalStats, boolean z2, List<UsageEvents.Event> list2) {
                int size = intervalStats.events.size();
                for (int firstIndexOnOrAfter = intervalStats.events.firstIndexOnOrAfter(timeInMillis); firstIndexOnOrAfter < size; firstIndexOnOrAfter++) {
                    if (intervalStats.events.get(firstIndexOnOrAfter).mTimeStamp >= currentTimeMillis) {
                        return false;
                    }
                    UsageEvents.Event event = intervalStats.events.get(firstIndexOnOrAfter);
                    if (CollectionUtils.isEmpty(list) || list.contains(event.mPackage)) {
                        list2.add(event);
                    }
                }
                return true;
            }
        });
        indentingPrintWriter.print("Last 24 hour events (");
        if (z) {
            indentingPrintWriter.printPair("timeRange", "\"" + DateUtils.formatDateRange(this.mContext, timeInMillis, currentTimeMillis, sDateFormatFlags) + "\"");
        } else {
            indentingPrintWriter.printPair("beginTime", Long.valueOf(timeInMillis));
            indentingPrintWriter.printPair("endTime", Long.valueOf(currentTimeMillis));
        }
        indentingPrintWriter.println(")");
        if (queryStats != null) {
            indentingPrintWriter.increaseIndent();
            Iterator it = queryStats.iterator();
            while (it.hasNext()) {
                printEvent(indentingPrintWriter, (UsageEvents.Event) it.next(), z);
            }
            indentingPrintWriter.decreaseIndent();
        }
    }

    void printEventAggregation(IndentingPrintWriter indentingPrintWriter, String str, IntervalStats.EventTracker eventTracker, boolean z) {
        if (eventTracker.count == 0 && eventTracker.duration == 0) {
            return;
        }
        indentingPrintWriter.print(str);
        indentingPrintWriter.print(": ");
        indentingPrintWriter.print(eventTracker.count);
        indentingPrintWriter.print("x for ");
        indentingPrintWriter.print(formatElapsedTime(eventTracker.duration, z));
        if (eventTracker.curStartTime != 0) {
            indentingPrintWriter.print(" (now running, started at ");
            formatDateTime(eventTracker.curStartTime, z);
            indentingPrintWriter.print(")");
        }
        indentingPrintWriter.println();
    }

    void printIntervalStats(IndentingPrintWriter indentingPrintWriter, IntervalStats intervalStats, boolean z, boolean z2, List<String> list) {
        String str;
        String str2;
        if (z) {
            indentingPrintWriter.printPair("timeRange", "\"" + DateUtils.formatDateRange(this.mContext, intervalStats.beginTime, intervalStats.endTime, sDateFormatFlags) + "\"");
        } else {
            indentingPrintWriter.printPair("beginTime", Long.valueOf(intervalStats.beginTime));
            indentingPrintWriter.printPair("endTime", Long.valueOf(intervalStats.endTime));
        }
        indentingPrintWriter.println();
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("packages");
        indentingPrintWriter.increaseIndent();
        ArrayMap<String, UsageStats> arrayMap = intervalStats.packageStats;
        int size = arrayMap.size();
        int i = 0;
        while (true) {
            str = "errorCount";
            if (i >= size) {
                break;
            }
            UsageStats valueAt = arrayMap.valueAt(i);
            if (CollectionUtils.isEmpty(list) || list.contains(valueAt.mPackageName)) {
                indentingPrintWriter.printPair("package", valueAt.mPackageName);
                indentingPrintWriter.printPair("totalTimeUsed", formatElapsedTime(valueAt.mTotalTimeInForeground, z));
                indentingPrintWriter.printPair("lastTimeUsed", formatDateTime(valueAt.mLastTimeUsed, z));
                indentingPrintWriter.printPair("totalTimeVisible", formatElapsedTime(valueAt.mTotalTimeVisible, z));
                indentingPrintWriter.printPair("lastTimeVisible", formatDateTime(valueAt.mLastTimeVisible, z));
                indentingPrintWriter.printPair("lastTimeComponentUsed", formatDateTime(valueAt.mLastTimeComponentUsed, z));
                indentingPrintWriter.printPair("totalTimeFS", formatElapsedTime(valueAt.mTotalTimeForegroundServiceUsed, z));
                indentingPrintWriter.printPair("lastTimeFS", formatDateTime(valueAt.mLastTimeForegroundServiceUsed, z));
                indentingPrintWriter.printPair("appLaunchCount", Integer.valueOf(valueAt.mAppLaunchCount));
                indentingPrintWriter.printPair("errorCount", Integer.valueOf(valueAt.mErrorCount));
                indentingPrintWriter.println();
            }
            i++;
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
        indentingPrintWriter.println("ChooserCounts");
        indentingPrintWriter.increaseIndent();
        Iterator<UsageStats> it = arrayMap.values().iterator();
        while (it.hasNext()) {
            UsageStats next = it.next();
            if (CollectionUtils.isEmpty(list) || list.contains(next.mPackageName)) {
                indentingPrintWriter.printPair("package", next.mPackageName);
                indentingPrintWriter.printPair(str, Integer.valueOf(next.mErrorCount));
                ArrayMap arrayMap2 = next.mChooserCounts;
                if (arrayMap2 != null) {
                    int size2 = arrayMap2.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        String str3 = (String) next.mChooserCounts.keyAt(i2);
                        ArrayMap arrayMap3 = (ArrayMap) next.mChooserCounts.valueAt(i2);
                        int size3 = arrayMap3.size();
                        int i3 = 0;
                        while (i3 < size3) {
                            Iterator<UsageStats> it2 = it;
                            String str4 = (String) arrayMap3.keyAt(i3);
                            int intValue = ((Integer) arrayMap3.valueAt(i3)).intValue();
                            UsageStats usageStats = next;
                            if (intValue != 0) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(str3);
                                str2 = str;
                                sb.append(":");
                                sb.append(str4);
                                sb.append(" is ");
                                sb.append(Integer.toString(intValue));
                                indentingPrintWriter.printPair("ChooserCounts", sb.toString());
                                indentingPrintWriter.println();
                            } else {
                                str2 = str;
                            }
                            i3++;
                            it = it2;
                            next = usageStats;
                            str = str2;
                        }
                    }
                }
                indentingPrintWriter.println();
                it = it;
                str = str;
            }
        }
        indentingPrintWriter.decreaseIndent();
        if (CollectionUtils.isEmpty(list)) {
            indentingPrintWriter.println("configurations");
            indentingPrintWriter.increaseIndent();
            ArrayMap<Configuration, ConfigurationStats> arrayMap4 = intervalStats.configurations;
            int size4 = arrayMap4.size();
            for (int i4 = 0; i4 < size4; i4++) {
                ConfigurationStats valueAt2 = arrayMap4.valueAt(i4);
                indentingPrintWriter.printPair("config", Configuration.resourceQualifierString(valueAt2.mConfiguration));
                indentingPrintWriter.printPair("totalTime", formatElapsedTime(valueAt2.mTotalTimeActive, z));
                indentingPrintWriter.printPair("lastTime", formatDateTime(valueAt2.mLastTimeActive, z));
                indentingPrintWriter.printPair("count", Integer.valueOf(valueAt2.mActivationCount));
                indentingPrintWriter.println();
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("event aggregations");
            indentingPrintWriter.increaseIndent();
            printEventAggregation(indentingPrintWriter, "screen-interactive", intervalStats.interactiveTracker, z);
            printEventAggregation(indentingPrintWriter, "screen-non-interactive", intervalStats.nonInteractiveTracker, z);
            printEventAggregation(indentingPrintWriter, "keyguard-shown", intervalStats.keyguardShownTracker, z);
            printEventAggregation(indentingPrintWriter, "keyguard-hidden", intervalStats.keyguardHiddenTracker, z);
            indentingPrintWriter.decreaseIndent();
        }
        if (!z2) {
            indentingPrintWriter.println("events");
            indentingPrintWriter.increaseIndent();
            EventList eventList = intervalStats.events;
            int size5 = eventList != null ? eventList.size() : 0;
            for (int i5 = 0; i5 < size5; i5++) {
                UsageEvents.Event event = eventList.get(i5);
                if (CollectionUtils.isEmpty(list) || list.contains(event.mPackage)) {
                    printEvent(indentingPrintWriter, event, z);
                }
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static int stringToInterval(String str) {
        boolean z;
        String lowerCase = str.toLowerCase();
        lowerCase.hashCode();
        switch (lowerCase.hashCode()) {
            case -791707519:
                if (lowerCase.equals("weekly")) {
                    z = false;
                    break;
                }
                z = -1;
                break;
            case -734561654:
                if (lowerCase.equals("yearly")) {
                    z = true;
                    break;
                }
                z = -1;
                break;
            case 95346201:
                if (lowerCase.equals(IOplusNetworkPolicyManagerServiceEx.TYPE_DAILY)) {
                    z = 2;
                    break;
                }
                z = -1;
                break;
            case 1236635661:
                if (lowerCase.equals("monthly")) {
                    z = 3;
                    break;
                }
                z = -1;
                break;
            default:
                z = -1;
                break;
        }
        switch (z) {
            case false:
                return 1;
            case true:
                return 3;
            case true:
                return 0;
            case true:
                return 2;
            default:
                return -1;
        }
    }

    private static String eventToString(int i) {
        switch (i) {
            case 0:
                return "NONE";
            case 1:
                return "ACTIVITY_RESUMED";
            case 2:
                return "ACTIVITY_PAUSED";
            case 3:
                return "END_OF_DAY";
            case 4:
                return "CONTINUE_PREVIOUS_DAY";
            case 5:
                return "CONFIGURATION_CHANGE";
            case 6:
                return "SYSTEM_INTERACTION";
            case 7:
                return "USER_INTERACTION";
            case 8:
                return "SHORTCUT_INVOCATION";
            case 9:
                return "CHOOSER_ACTION";
            case 10:
                return "NOTIFICATION_SEEN";
            case 11:
                return "STANDBY_BUCKET_CHANGED";
            case 12:
                return "NOTIFICATION_INTERRUPTION";
            case 13:
                return "SLICE_PINNED_PRIV";
            case 14:
                return "SLICE_PINNED";
            case 15:
                return "SCREEN_INTERACTIVE";
            case 16:
                return "SCREEN_NON_INTERACTIVE";
            case 17:
                return "KEYGUARD_SHOWN";
            case 18:
                return "KEYGUARD_HIDDEN";
            case 19:
                return "FOREGROUND_SERVICE_START";
            case 20:
                return "FOREGROUND_SERVICE_STOP";
            case 21:
                return "CONTINUING_FOREGROUND_SERVICE";
            case 22:
                return "ROLLOVER_FOREGROUND_SERVICE";
            case PackageManagerService.MIN_INSTALLABLE_TARGET_SDK /* 23 */:
                return "ACTIVITY_STOPPED";
            case 24:
            case 25:
            default:
                return "UNKNOWN_TYPE_" + i;
            case 26:
                return "DEVICE_SHUTDOWN";
            case 27:
                return "DEVICE_STARTUP";
            case 28:
                return "USER_UNLOCKED";
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
                return "USER_STOPPED";
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_11 /* 30 */:
                return "LOCUS_ID_SET";
            case HdmiCecKeycode.CEC_KEYCODE_NUMBER_12 /* 31 */:
                return "APP_COMPONENT_USED";
            case 32:
                return "CRASH_OR_NOT_RESPONCE";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getBackupPayload(String str) {
        checkAndGetTimeLocked();
        persistActiveStats();
        return this.mDatabase.getBackupPayload(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> applyRestoredPayload(String str, byte[] bArr) {
        checkAndGetTimeLocked();
        return this.mDatabase.applyRestoredPayload(str, bArr);
    }
}
