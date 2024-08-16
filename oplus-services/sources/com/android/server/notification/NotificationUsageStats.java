package com.android.server.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.logging.MetricsLogger;
import com.android.server.health.HealthServiceWrapperHidl;
import com.android.server.notification.NotificationManagerService;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class NotificationUsageStats {
    private static final boolean DEBUG = false;
    private static final String DEVICE_GLOBAL_STATS = "__global";
    private static final long EMIT_PERIOD = 14400000;
    private static final AggregatedStats[] EMPTY_AGGREGATED_STATS = new AggregatedStats[0];
    private static final boolean ENABLE_AGGREGATED_IN_MEMORY_STATS = true;
    private static final boolean ENABLE_SQLITE_LOG = true;
    public static final int FOUR_HOURS = 14400000;
    private static final int MSG_EMIT = 1;
    private static final String TAG = "NotificationUsageStats";
    public static final int TEN_SECONDS = 10000;
    private final Context mContext;
    private final Handler mHandler;
    private final Map<String, AggregatedStats> mStats = new HashMap();
    private final ArrayDeque<AggregatedStats[]> mStatsArrays = new ArrayDeque<>();
    private ArraySet<String> mStatExpiredkeys = new ArraySet<>();
    private long mLastEmitTime = SystemClock.elapsedRealtime();

    public NotificationUsageStats(Context context) {
        this.mContext = context;
        Handler handler = new Handler(context.getMainLooper()) { // from class: com.android.server.notification.NotificationUsageStats.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    NotificationUsageStats.this.emit();
                    return;
                }
                Log.wtf(NotificationUsageStats.TAG, "Unknown message type: " + message.what);
            }
        };
        this.mHandler = handler;
        handler.sendEmptyMessageDelayed(1, 14400000L);
    }

    public synchronized float getAppEnqueueRate(String str) {
        AggregatedStats orCreateAggregatedStatsLocked = getOrCreateAggregatedStatsLocked(str);
        if (orCreateAggregatedStatsLocked == null) {
            return 0.0f;
        }
        return orCreateAggregatedStatsLocked.getEnqueueRate(SystemClock.elapsedRealtime());
    }

    public synchronized boolean isAlertRateLimited(String str) {
        AggregatedStats orCreateAggregatedStatsLocked = getOrCreateAggregatedStatsLocked(str);
        if (orCreateAggregatedStatsLocked == null) {
            return false;
        }
        return orCreateAggregatedStatsLocked.isAlertRateLimited();
    }

    public synchronized void registerEnqueuedByApp(String str) {
        AggregatedStats[] aggregatedStatsLocked = getAggregatedStatsLocked(str);
        for (AggregatedStats aggregatedStats : aggregatedStatsLocked) {
            aggregatedStats.numEnqueuedByApp++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsLocked);
    }

    public synchronized void registerPostedByApp(NotificationRecord notificationRecord) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        notificationRecord.stats.posttimeElapsedMs = elapsedRealtime;
        AggregatedStats[] aggregatedStatsLocked = getAggregatedStatsLocked(notificationRecord);
        for (AggregatedStats aggregatedStats : aggregatedStatsLocked) {
            int i = 1;
            aggregatedStats.numPostedByApp++;
            aggregatedStats.updateInterarrivalEstimate(elapsedRealtime);
            aggregatedStats.countApiUse(notificationRecord);
            int i2 = aggregatedStats.numUndecoratedRemoteViews;
            if (!notificationRecord.hasUndecoratedRemoteView()) {
                i = 0;
            }
            aggregatedStats.numUndecoratedRemoteViews = i2 + i;
        }
        releaseAggregatedStatsLocked(aggregatedStatsLocked);
    }

    public synchronized void registerUpdatedByApp(NotificationRecord notificationRecord, NotificationRecord notificationRecord2) {
        notificationRecord.stats.updateFrom(notificationRecord2.stats);
        AggregatedStats[] aggregatedStatsLocked = getAggregatedStatsLocked(notificationRecord);
        for (AggregatedStats aggregatedStats : aggregatedStatsLocked) {
            aggregatedStats.numUpdatedByApp++;
            aggregatedStats.updateInterarrivalEstimate(SystemClock.elapsedRealtime());
            aggregatedStats.countApiUse(notificationRecord);
        }
        releaseAggregatedStatsLocked(aggregatedStatsLocked);
    }

    public synchronized void registerRemovedByApp(NotificationRecord notificationRecord) {
        notificationRecord.stats.onRemoved();
        AggregatedStats[] aggregatedStatsLocked = getAggregatedStatsLocked(notificationRecord);
        for (AggregatedStats aggregatedStats : aggregatedStatsLocked) {
            aggregatedStats.numRemovedByApp++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsLocked);
    }

    public synchronized void registerDismissedByUser(NotificationRecord notificationRecord) {
        MetricsLogger.histogram(this.mContext, "note_dismiss_longevity", ((int) (System.currentTimeMillis() - notificationRecord.getRankingTimeMs())) / 60000);
        notificationRecord.stats.onDismiss();
    }

    public synchronized void registerClickedByUser(NotificationRecord notificationRecord) {
        MetricsLogger.histogram(this.mContext, "note_click_longevity", ((int) (System.currentTimeMillis() - notificationRecord.getRankingTimeMs())) / 60000);
        notificationRecord.stats.onClick();
    }

    public synchronized void registerPeopleAffinity(NotificationRecord notificationRecord, boolean z, boolean z2, boolean z3) {
        AggregatedStats[] aggregatedStatsLocked = getAggregatedStatsLocked(notificationRecord);
        for (AggregatedStats aggregatedStats : aggregatedStatsLocked) {
            if (z) {
                aggregatedStats.numWithValidPeople++;
            }
            if (z2) {
                aggregatedStats.numWithStaredPeople++;
            }
            if (z3) {
                aggregatedStats.numPeopleCacheHit++;
            } else {
                aggregatedStats.numPeopleCacheMiss++;
            }
        }
        releaseAggregatedStatsLocked(aggregatedStatsLocked);
    }

    public synchronized void registerBlocked(NotificationRecord notificationRecord) {
        AggregatedStats[] aggregatedStatsLocked = getAggregatedStatsLocked(notificationRecord);
        for (AggregatedStats aggregatedStats : aggregatedStatsLocked) {
            aggregatedStats.numBlocked++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsLocked);
    }

    public synchronized void registerSuspendedByAdmin(NotificationRecord notificationRecord) {
        AggregatedStats[] aggregatedStatsLocked = getAggregatedStatsLocked(notificationRecord);
        for (AggregatedStats aggregatedStats : aggregatedStatsLocked) {
            aggregatedStats.numSuspendedByAdmin++;
        }
        releaseAggregatedStatsLocked(aggregatedStatsLocked);
    }

    public synchronized void registerOverRateQuota(String str) {
        for (AggregatedStats aggregatedStats : getAggregatedStatsLocked(str)) {
            aggregatedStats.numRateViolations++;
        }
    }

    public synchronized void registerOverCountQuota(String str) {
        for (AggregatedStats aggregatedStats : getAggregatedStatsLocked(str)) {
            aggregatedStats.numQuotaViolations++;
        }
    }

    public synchronized void registerImageRemoved(String str) {
        for (AggregatedStats aggregatedStats : getAggregatedStatsLocked(str)) {
            aggregatedStats.numImagesRemoved++;
        }
    }

    private AggregatedStats[] getAggregatedStatsLocked(NotificationRecord notificationRecord) {
        return getAggregatedStatsLocked(notificationRecord.getSbn().getPackageName());
    }

    private AggregatedStats[] getAggregatedStatsLocked(String str) {
        AggregatedStats[] poll = this.mStatsArrays.poll();
        if (poll == null) {
            poll = new AggregatedStats[2];
        }
        poll[0] = getOrCreateAggregatedStatsLocked(DEVICE_GLOBAL_STATS);
        poll[1] = getOrCreateAggregatedStatsLocked(str);
        return poll;
    }

    private void releaseAggregatedStatsLocked(AggregatedStats[] aggregatedStatsArr) {
        for (int i = 0; i < aggregatedStatsArr.length; i++) {
            aggregatedStatsArr[i] = null;
        }
        this.mStatsArrays.offer(aggregatedStatsArr);
    }

    private AggregatedStats getOrCreateAggregatedStatsLocked(String str) {
        AggregatedStats aggregatedStats = this.mStats.get(str);
        if (aggregatedStats == null) {
            aggregatedStats = new AggregatedStats(this.mContext, str);
            this.mStats.put(str, aggregatedStats);
        }
        aggregatedStats.mLastAccessTime = SystemClock.elapsedRealtime();
        return aggregatedStats;
    }

    public synchronized JSONObject dumpJson(NotificationManagerService.DumpFilter dumpFilter) {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            for (AggregatedStats aggregatedStats : this.mStats.values()) {
                if (dumpFilter == null || dumpFilter.matches(aggregatedStats.key)) {
                    jSONArray.put(aggregatedStats.dumpJson());
                }
            }
            jSONObject.put("current", jSONArray);
        } catch (JSONException unused) {
        }
        return jSONObject;
    }

    public PulledStats remoteViewStats(long j, boolean z) {
        PulledStats pulledStats = new PulledStats(j);
        for (AggregatedStats aggregatedStats : this.mStats.values()) {
            if (aggregatedStats.numUndecoratedRemoteViews > 0) {
                pulledStats.addUndecoratedPackage(aggregatedStats.key, aggregatedStats.mCreated);
            }
        }
        return pulledStats;
    }

    public synchronized void dump(PrintWriter printWriter, String str, NotificationManagerService.DumpFilter dumpFilter) {
        for (AggregatedStats aggregatedStats : this.mStats.values()) {
            if (dumpFilter == null || dumpFilter.matches(aggregatedStats.key)) {
                aggregatedStats.dump(printWriter, str);
            }
        }
        printWriter.println(str + "mStatsArrays.size(): " + this.mStatsArrays.size());
        printWriter.println(str + "mStats.size(): " + this.mStats.size());
    }

    public synchronized void emit() {
        getOrCreateAggregatedStatsLocked(DEVICE_GLOBAL_STATS).emit();
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 14400000L);
        for (String str : this.mStats.keySet()) {
            if (this.mStats.get(str).mLastAccessTime < this.mLastEmitTime) {
                this.mStatExpiredkeys.add(str);
            }
        }
        Iterator<String> it = this.mStatExpiredkeys.iterator();
        while (it.hasNext()) {
            this.mStats.remove(it.next());
        }
        this.mStatExpiredkeys.clear();
        this.mLastEmitTime = SystemClock.elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class AggregatedStats {
        public ImportanceHistogram finalImportance;
        public final String key;
        private final Context mContext;
        public long mLastAccessTime;
        private AggregatedStats mPrevious;
        public ImportanceHistogram noisyImportance;
        public int numAlertViolations;
        public int numAutoCancel;
        public int numBlocked;
        public int numEnqueuedByApp;
        public int numForegroundService;
        public int numImagesRemoved;
        public int numInterrupt;
        public int numOngoing;
        public int numPeopleCacheHit;
        public int numPeopleCacheMiss;
        public int numPostedByApp;
        public int numPrivate;
        public int numQuotaViolations;
        public int numRateViolations;
        public int numRemovedByApp;
        public int numSecret;
        public int numSuspendedByAdmin;
        public int numUndecoratedRemoteViews;
        public int numUpdatedByApp;
        public int numUserInitiatedJob;
        public int numWithActions;
        public int numWithBigPicture;
        public int numWithBigText;
        public int numWithInbox;
        public int numWithInfoText;
        public int numWithLargeIcon;
        public int numWithMediaSession;
        public int numWithStaredPeople;
        public int numWithSubText;
        public int numWithText;
        public int numWithTitle;
        public int numWithValidPeople;
        public ImportanceHistogram quietImportance;
        private final long mCreated = SystemClock.elapsedRealtime();
        public RateEstimator enqueueRate = new RateEstimator();
        public AlertRateLimiter alertRate = new AlertRateLimiter();

        public AggregatedStats(Context context, String str) {
            this.key = str;
            this.mContext = context;
            this.noisyImportance = new ImportanceHistogram(context, "note_imp_noisy_");
            this.quietImportance = new ImportanceHistogram(context, "note_imp_quiet_");
            this.finalImportance = new ImportanceHistogram(context, "note_importance_");
        }

        public AggregatedStats getPrevious() {
            if (this.mPrevious == null) {
                this.mPrevious = new AggregatedStats(this.mContext, this.key);
            }
            return this.mPrevious;
        }

        public void countApiUse(NotificationRecord notificationRecord) {
            Notification notification = notificationRecord.getNotification();
            if (notification.actions != null) {
                this.numWithActions++;
            }
            int i = notification.flags;
            if ((i & 64) != 0) {
                this.numForegroundService++;
            }
            if ((32768 & i) != 0) {
                this.numUserInitiatedJob++;
            }
            if ((i & 2) != 0) {
                this.numOngoing++;
            }
            if ((i & 16) != 0) {
                this.numAutoCancel++;
            }
            int i2 = notification.defaults;
            if ((i2 & 1) != 0 || (i2 & 2) != 0 || notification.sound != null || notification.vibrate != null) {
                this.numInterrupt++;
            }
            int i3 = notification.visibility;
            if (i3 == -1) {
                this.numSecret++;
            } else if (i3 == 0) {
                this.numPrivate++;
            }
            SingleNotificationStats singleNotificationStats = notificationRecord.stats;
            if (singleNotificationStats.isNoisy) {
                this.noisyImportance.increment(singleNotificationStats.requestedImportance);
            } else {
                this.quietImportance.increment(singleNotificationStats.requestedImportance);
            }
            this.finalImportance.increment(notificationRecord.getImportance());
            Set<String> keySet = notification.extras.keySet();
            if (keySet.contains("android.bigText")) {
                this.numWithBigText++;
            }
            if (keySet.contains("android.picture")) {
                this.numWithBigPicture++;
            }
            if (keySet.contains("android.largeIcon")) {
                this.numWithLargeIcon++;
            }
            if (keySet.contains("android.textLines")) {
                this.numWithInbox++;
            }
            if (keySet.contains("android.mediaSession")) {
                this.numWithMediaSession++;
            }
            if (keySet.contains("android.title") && !TextUtils.isEmpty(notification.extras.getCharSequence("android.title"))) {
                this.numWithTitle++;
            }
            if (keySet.contains("android.text") && !TextUtils.isEmpty(notification.extras.getCharSequence("android.text"))) {
                this.numWithText++;
            }
            if (keySet.contains("android.subText") && !TextUtils.isEmpty(notification.extras.getCharSequence("android.subText"))) {
                this.numWithSubText++;
            }
            if (!keySet.contains("android.infoText") || TextUtils.isEmpty(notification.extras.getCharSequence("android.infoText"))) {
                return;
            }
            this.numWithInfoText++;
        }

        public void emit() {
            AggregatedStats previous = getPrevious();
            maybeCount("note_enqueued", this.numEnqueuedByApp - previous.numEnqueuedByApp);
            maybeCount("note_post", this.numPostedByApp - previous.numPostedByApp);
            maybeCount("note_update", this.numUpdatedByApp - previous.numUpdatedByApp);
            maybeCount("note_remove", this.numRemovedByApp - previous.numRemovedByApp);
            maybeCount("note_with_people", this.numWithValidPeople - previous.numWithValidPeople);
            maybeCount("note_with_stars", this.numWithStaredPeople - previous.numWithStaredPeople);
            maybeCount("people_cache_hit", this.numPeopleCacheHit - previous.numPeopleCacheHit);
            maybeCount("people_cache_miss", this.numPeopleCacheMiss - previous.numPeopleCacheMiss);
            maybeCount("note_blocked", this.numBlocked - previous.numBlocked);
            maybeCount("note_suspended", this.numSuspendedByAdmin - previous.numSuspendedByAdmin);
            maybeCount("note_with_actions", this.numWithActions - previous.numWithActions);
            maybeCount("note_private", this.numPrivate - previous.numPrivate);
            maybeCount("note_secret", this.numSecret - previous.numSecret);
            maybeCount("note_interupt", this.numInterrupt - previous.numInterrupt);
            maybeCount("note_big_text", this.numWithBigText - previous.numWithBigText);
            maybeCount("note_big_pic", this.numWithBigPicture - previous.numWithBigPicture);
            maybeCount("note_fg", this.numForegroundService - previous.numForegroundService);
            maybeCount("note_uij", this.numUserInitiatedJob - previous.numUserInitiatedJob);
            maybeCount("note_ongoing", this.numOngoing - previous.numOngoing);
            maybeCount("note_auto", this.numAutoCancel - previous.numAutoCancel);
            maybeCount("note_large_icon", this.numWithLargeIcon - previous.numWithLargeIcon);
            maybeCount("note_inbox", this.numWithInbox - previous.numWithInbox);
            maybeCount("note_media", this.numWithMediaSession - previous.numWithMediaSession);
            maybeCount("note_title", this.numWithTitle - previous.numWithTitle);
            maybeCount("note_text", this.numWithText - previous.numWithText);
            maybeCount("note_sub_text", this.numWithSubText - previous.numWithSubText);
            maybeCount("note_info_text", this.numWithInfoText - previous.numWithInfoText);
            maybeCount("note_over_rate", this.numRateViolations - previous.numRateViolations);
            maybeCount("note_over_alert_rate", this.numAlertViolations - previous.numAlertViolations);
            maybeCount("note_over_quota", this.numQuotaViolations - previous.numQuotaViolations);
            maybeCount("note_images_removed", this.numImagesRemoved - previous.numImagesRemoved);
            this.noisyImportance.maybeCount(previous.noisyImportance);
            this.quietImportance.maybeCount(previous.quietImportance);
            this.finalImportance.maybeCount(previous.finalImportance);
            previous.numEnqueuedByApp = this.numEnqueuedByApp;
            previous.numPostedByApp = this.numPostedByApp;
            previous.numUpdatedByApp = this.numUpdatedByApp;
            previous.numRemovedByApp = this.numRemovedByApp;
            previous.numPeopleCacheHit = this.numPeopleCacheHit;
            previous.numPeopleCacheMiss = this.numPeopleCacheMiss;
            previous.numWithStaredPeople = this.numWithStaredPeople;
            previous.numWithValidPeople = this.numWithValidPeople;
            previous.numBlocked = this.numBlocked;
            previous.numSuspendedByAdmin = this.numSuspendedByAdmin;
            previous.numWithActions = this.numWithActions;
            previous.numPrivate = this.numPrivate;
            previous.numSecret = this.numSecret;
            previous.numInterrupt = this.numInterrupt;
            previous.numWithBigText = this.numWithBigText;
            previous.numWithBigPicture = this.numWithBigPicture;
            previous.numForegroundService = this.numForegroundService;
            previous.numUserInitiatedJob = this.numUserInitiatedJob;
            previous.numOngoing = this.numOngoing;
            previous.numAutoCancel = this.numAutoCancel;
            previous.numWithLargeIcon = this.numWithLargeIcon;
            previous.numWithInbox = this.numWithInbox;
            previous.numWithMediaSession = this.numWithMediaSession;
            previous.numWithTitle = this.numWithTitle;
            previous.numWithText = this.numWithText;
            previous.numWithSubText = this.numWithSubText;
            previous.numWithInfoText = this.numWithInfoText;
            previous.numRateViolations = this.numRateViolations;
            previous.numAlertViolations = this.numAlertViolations;
            previous.numQuotaViolations = this.numQuotaViolations;
            previous.numImagesRemoved = this.numImagesRemoved;
            this.noisyImportance.update(previous.noisyImportance);
            this.quietImportance.update(previous.quietImportance);
            this.finalImportance.update(previous.finalImportance);
        }

        void maybeCount(String str, int i) {
            if (i > 0) {
                MetricsLogger.count(this.mContext, str, i);
            }
        }

        public void dump(PrintWriter printWriter, String str) {
            printWriter.println(toStringWithIndent(str));
        }

        public String toString() {
            return toStringWithIndent("");
        }

        public float getEnqueueRate() {
            return getEnqueueRate(SystemClock.elapsedRealtime());
        }

        public float getEnqueueRate(long j) {
            return this.enqueueRate.getRate(j);
        }

        public void updateInterarrivalEstimate(long j) {
            this.enqueueRate.update(j);
        }

        public boolean isAlertRateLimited() {
            boolean shouldRateLimitAlert = this.alertRate.shouldRateLimitAlert(SystemClock.elapsedRealtime());
            if (shouldRateLimitAlert) {
                this.numAlertViolations++;
            }
            return shouldRateLimitAlert;
        }

        private String toStringWithIndent(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("AggregatedStats{\n");
            String str2 = str + "  ";
            sb.append(str2);
            sb.append("key='");
            sb.append(this.key);
            sb.append("',\n");
            sb.append(str2);
            sb.append("numEnqueuedByApp=");
            sb.append(this.numEnqueuedByApp);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numPostedByApp=");
            sb.append(this.numPostedByApp);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numUpdatedByApp=");
            sb.append(this.numUpdatedByApp);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numRemovedByApp=");
            sb.append(this.numRemovedByApp);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numPeopleCacheHit=");
            sb.append(this.numPeopleCacheHit);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numWithStaredPeople=");
            sb.append(this.numWithStaredPeople);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numWithValidPeople=");
            sb.append(this.numWithValidPeople);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numPeopleCacheMiss=");
            sb.append(this.numPeopleCacheMiss);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numBlocked=");
            sb.append(this.numBlocked);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numSuspendedByAdmin=");
            sb.append(this.numSuspendedByAdmin);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numWithActions=");
            sb.append(this.numWithActions);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numPrivate=");
            sb.append(this.numPrivate);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numSecret=");
            sb.append(this.numSecret);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numInterrupt=");
            sb.append(this.numInterrupt);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numWithBigText=");
            sb.append(this.numWithBigText);
            sb.append(",\n");
            sb.append(str2);
            sb.append("numWithBigPicture=");
            sb.append(this.numWithBigPicture);
            sb.append("\n");
            sb.append(str2);
            sb.append("numForegroundService=");
            sb.append(this.numForegroundService);
            sb.append("\n");
            sb.append(str2);
            sb.append("numUserInitiatedJob=");
            sb.append(this.numUserInitiatedJob);
            sb.append("\n");
            sb.append(str2);
            sb.append("numOngoing=");
            sb.append(this.numOngoing);
            sb.append("\n");
            sb.append(str2);
            sb.append("numAutoCancel=");
            sb.append(this.numAutoCancel);
            sb.append("\n");
            sb.append(str2);
            sb.append("numWithLargeIcon=");
            sb.append(this.numWithLargeIcon);
            sb.append("\n");
            sb.append(str2);
            sb.append("numWithInbox=");
            sb.append(this.numWithInbox);
            sb.append("\n");
            sb.append(str2);
            sb.append("numWithMediaSession=");
            sb.append(this.numWithMediaSession);
            sb.append("\n");
            sb.append(str2);
            sb.append("numWithTitle=");
            sb.append(this.numWithTitle);
            sb.append("\n");
            sb.append(str2);
            sb.append("numWithText=");
            sb.append(this.numWithText);
            sb.append("\n");
            sb.append(str2);
            sb.append("numWithSubText=");
            sb.append(this.numWithSubText);
            sb.append("\n");
            sb.append(str2);
            sb.append("numWithInfoText=");
            sb.append(this.numWithInfoText);
            sb.append("\n");
            sb.append(str2);
            sb.append("numRateViolations=");
            sb.append(this.numRateViolations);
            sb.append("\n");
            sb.append(str2);
            sb.append("numAlertViolations=");
            sb.append(this.numAlertViolations);
            sb.append("\n");
            sb.append(str2);
            sb.append("numQuotaViolations=");
            sb.append(this.numQuotaViolations);
            sb.append("\n");
            sb.append(str2);
            sb.append("numImagesRemoved=");
            sb.append(this.numImagesRemoved);
            sb.append("\n");
            sb.append(str2);
            sb.append(this.noisyImportance.toString());
            sb.append("\n");
            sb.append(str2);
            sb.append(this.quietImportance.toString());
            sb.append("\n");
            sb.append(str2);
            sb.append(this.finalImportance.toString());
            sb.append("\n");
            sb.append(str2);
            sb.append("numUndecorateRVs=");
            sb.append(this.numUndecoratedRemoteViews);
            sb.append("\n");
            sb.append(str);
            sb.append("}");
            return sb.toString();
        }

        public JSONObject dumpJson() throws JSONException {
            AggregatedStats previous = getPrevious();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("key", this.key);
            jSONObject.put("duration", SystemClock.elapsedRealtime() - this.mCreated);
            maybePut(jSONObject, "numEnqueuedByApp", this.numEnqueuedByApp);
            maybePut(jSONObject, "numPostedByApp", this.numPostedByApp);
            maybePut(jSONObject, "numUpdatedByApp", this.numUpdatedByApp);
            maybePut(jSONObject, "numRemovedByApp", this.numRemovedByApp);
            maybePut(jSONObject, "numPeopleCacheHit", this.numPeopleCacheHit);
            maybePut(jSONObject, "numPeopleCacheMiss", this.numPeopleCacheMiss);
            maybePut(jSONObject, "numWithStaredPeople", this.numWithStaredPeople);
            maybePut(jSONObject, "numWithValidPeople", this.numWithValidPeople);
            maybePut(jSONObject, "numBlocked", this.numBlocked);
            maybePut(jSONObject, "numSuspendedByAdmin", this.numSuspendedByAdmin);
            maybePut(jSONObject, "numWithActions", this.numWithActions);
            maybePut(jSONObject, "numPrivate", this.numPrivate);
            maybePut(jSONObject, "numSecret", this.numSecret);
            maybePut(jSONObject, "numInterrupt", this.numInterrupt);
            maybePut(jSONObject, "numWithBigText", this.numWithBigText);
            maybePut(jSONObject, "numWithBigPicture", this.numWithBigPicture);
            maybePut(jSONObject, "numForegroundService", this.numForegroundService);
            maybePut(jSONObject, "numUserInitiatedJob", this.numUserInitiatedJob);
            maybePut(jSONObject, "numOngoing", this.numOngoing);
            maybePut(jSONObject, "numAutoCancel", this.numAutoCancel);
            maybePut(jSONObject, "numWithLargeIcon", this.numWithLargeIcon);
            maybePut(jSONObject, "numWithInbox", this.numWithInbox);
            maybePut(jSONObject, "numWithMediaSession", this.numWithMediaSession);
            maybePut(jSONObject, "numWithTitle", this.numWithTitle);
            maybePut(jSONObject, "numWithText", this.numWithText);
            maybePut(jSONObject, "numWithSubText", this.numWithSubText);
            maybePut(jSONObject, "numWithInfoText", this.numWithInfoText);
            maybePut(jSONObject, "numRateViolations", this.numRateViolations);
            maybePut(jSONObject, "numQuotaLViolations", this.numQuotaViolations);
            maybePut(jSONObject, "notificationEnqueueRate", getEnqueueRate());
            maybePut(jSONObject, "numAlertViolations", this.numAlertViolations);
            maybePut(jSONObject, "numImagesRemoved", this.numImagesRemoved);
            this.noisyImportance.maybePut(jSONObject, previous.noisyImportance);
            this.quietImportance.maybePut(jSONObject, previous.quietImportance);
            this.finalImportance.maybePut(jSONObject, previous.finalImportance);
            return jSONObject;
        }

        private void maybePut(JSONObject jSONObject, String str, int i) throws JSONException {
            if (i > 0) {
                jSONObject.put(str, i);
            }
        }

        private void maybePut(JSONObject jSONObject, String str, float f) throws JSONException {
            double d = f;
            if (d > 0.0d) {
                jSONObject.put(str, d);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ImportanceHistogram {
        private static final String[] IMPORTANCE_NAMES = {"none", "min", "low", HealthServiceWrapperHidl.INSTANCE_VENDOR, "high", "max"};
        private static final int NUM_IMPORTANCES = 6;
        private final Context mContext;
        private int[] mCount = new int[6];
        private final String[] mCounterNames = new String[6];
        private final String mPrefix;

        ImportanceHistogram(Context context, String str) {
            this.mContext = context;
            this.mPrefix = str;
            for (int i = 0; i < 6; i++) {
                this.mCounterNames[i] = this.mPrefix + IMPORTANCE_NAMES[i];
            }
        }

        void increment(int i) {
            int max = Math.max(0, Math.min(i, this.mCount.length - 1));
            int[] iArr = this.mCount;
            iArr[max] = iArr[max] + 1;
        }

        void maybeCount(ImportanceHistogram importanceHistogram) {
            for (int i = 0; i < 6; i++) {
                int i2 = this.mCount[i] - importanceHistogram.mCount[i];
                if (i2 > 0) {
                    MetricsLogger.count(this.mContext, this.mCounterNames[i], i2);
                }
            }
        }

        void update(ImportanceHistogram importanceHistogram) {
            for (int i = 0; i < 6; i++) {
                this.mCount[i] = importanceHistogram.mCount[i];
            }
        }

        public void maybePut(JSONObject jSONObject, ImportanceHistogram importanceHistogram) throws JSONException {
            jSONObject.put(this.mPrefix, new JSONArray(this.mCount));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mPrefix);
            sb.append(": [");
            for (int i = 0; i < 6; i++) {
                sb.append(this.mCount[i]);
                if (i < 5) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SingleNotificationStats {
        public boolean isNoisy;
        public int naturalImportance;
        public int requestedImportance;
        private boolean isVisible = false;
        private boolean isExpanded = false;
        public long posttimeElapsedMs = -1;
        public long posttimeToFirstClickMs = -1;
        public long posttimeToDismissMs = -1;
        public long airtimeCount = 0;
        public long posttimeToFirstAirtimeMs = -1;
        public long currentAirtimeStartElapsedMs = -1;
        public long airtimeMs = 0;
        public long posttimeToFirstVisibleExpansionMs = -1;
        public long currentAirtimeExpandedStartElapsedMs = -1;
        public long airtimeExpandedMs = 0;
        public long userExpansionCount = 0;

        public long getCurrentPosttimeMs() {
            if (this.posttimeElapsedMs < 0) {
                return 0L;
            }
            return SystemClock.elapsedRealtime() - this.posttimeElapsedMs;
        }

        public long getCurrentAirtimeMs() {
            long j = this.airtimeMs;
            return this.currentAirtimeStartElapsedMs >= 0 ? j + (SystemClock.elapsedRealtime() - this.currentAirtimeStartElapsedMs) : j;
        }

        public long getCurrentAirtimeExpandedMs() {
            long j = this.airtimeExpandedMs;
            return this.currentAirtimeExpandedStartElapsedMs >= 0 ? j + (SystemClock.elapsedRealtime() - this.currentAirtimeExpandedStartElapsedMs) : j;
        }

        public void onClick() {
            if (this.posttimeToFirstClickMs < 0) {
                this.posttimeToFirstClickMs = SystemClock.elapsedRealtime() - this.posttimeElapsedMs;
            }
        }

        public void onDismiss() {
            if (this.posttimeToDismissMs < 0) {
                this.posttimeToDismissMs = SystemClock.elapsedRealtime() - this.posttimeElapsedMs;
            }
            finish();
        }

        public void onCancel() {
            finish();
        }

        public void onRemoved() {
            finish();
        }

        public void onVisibilityChanged(boolean z) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            boolean z2 = this.isVisible;
            this.isVisible = z;
            if (z) {
                if (this.currentAirtimeStartElapsedMs < 0) {
                    this.airtimeCount++;
                    this.currentAirtimeStartElapsedMs = elapsedRealtime;
                }
                if (this.posttimeToFirstAirtimeMs < 0) {
                    this.posttimeToFirstAirtimeMs = elapsedRealtime - this.posttimeElapsedMs;
                }
            } else {
                long j = this.currentAirtimeStartElapsedMs;
                if (j >= 0) {
                    this.airtimeMs += elapsedRealtime - j;
                    this.currentAirtimeStartElapsedMs = -1L;
                }
            }
            if (z2 != z) {
                updateVisiblyExpandedStats();
            }
        }

        public void onExpansionChanged(boolean z, boolean z2) {
            this.isExpanded = z2;
            if (z2 && z) {
                this.userExpansionCount++;
            }
            updateVisiblyExpandedStats();
        }

        public boolean hasBeenVisiblyExpanded() {
            return this.posttimeToFirstVisibleExpansionMs >= 0;
        }

        private void updateVisiblyExpandedStats() {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (this.isExpanded && this.isVisible) {
                if (this.currentAirtimeExpandedStartElapsedMs < 0) {
                    this.currentAirtimeExpandedStartElapsedMs = elapsedRealtime;
                }
                if (this.posttimeToFirstVisibleExpansionMs < 0) {
                    this.posttimeToFirstVisibleExpansionMs = elapsedRealtime - this.posttimeElapsedMs;
                    return;
                }
                return;
            }
            long j = this.currentAirtimeExpandedStartElapsedMs;
            if (j >= 0) {
                this.airtimeExpandedMs += elapsedRealtime - j;
                this.currentAirtimeExpandedStartElapsedMs = -1L;
            }
        }

        public void finish() {
            onVisibilityChanged(false);
        }

        public String toString() {
            return "SingleNotificationStats{posttimeElapsedMs=" + this.posttimeElapsedMs + ", posttimeToFirstClickMs=" + this.posttimeToFirstClickMs + ", posttimeToDismissMs=" + this.posttimeToDismissMs + ", airtimeCount=" + this.airtimeCount + ", airtimeMs=" + this.airtimeMs + ", currentAirtimeStartElapsedMs=" + this.currentAirtimeStartElapsedMs + ", airtimeExpandedMs=" + this.airtimeExpandedMs + ", posttimeToFirstVisibleExpansionMs=" + this.posttimeToFirstVisibleExpansionMs + ", currentAirtimeExpandedStartElapsedMs=" + this.currentAirtimeExpandedStartElapsedMs + ", requestedImportance=" + this.requestedImportance + ", naturalImportance=" + this.naturalImportance + ", isNoisy=" + this.isNoisy + '}';
        }

        public void updateFrom(SingleNotificationStats singleNotificationStats) {
            this.posttimeElapsedMs = singleNotificationStats.posttimeElapsedMs;
            this.posttimeToFirstClickMs = singleNotificationStats.posttimeToFirstClickMs;
            this.airtimeCount = singleNotificationStats.airtimeCount;
            this.posttimeToFirstAirtimeMs = singleNotificationStats.posttimeToFirstAirtimeMs;
            this.currentAirtimeStartElapsedMs = singleNotificationStats.currentAirtimeStartElapsedMs;
            this.airtimeMs = singleNotificationStats.airtimeMs;
            this.posttimeToFirstVisibleExpansionMs = singleNotificationStats.posttimeToFirstVisibleExpansionMs;
            this.currentAirtimeExpandedStartElapsedMs = singleNotificationStats.currentAirtimeExpandedStartElapsedMs;
            this.airtimeExpandedMs = singleNotificationStats.airtimeExpandedMs;
            this.userExpansionCount = singleNotificationStats.userExpansionCount;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Aggregate {
        double avg;
        long numSamples;
        double sum2;
        double var;

        public void addSample(long j) {
            long j2 = this.numSamples + 1;
            this.numSamples = j2;
            double d = j2;
            double d2 = this.avg;
            double d3 = j - d2;
            this.avg = d2 + ((1.0d / d) * d3);
            double d4 = d - 1.0d;
            double d5 = this.sum2 + ((d4 / d) * d3 * d3);
            this.sum2 = d5;
            this.var = d5 / (j2 != 1 ? d4 : 1.0d);
        }

        public String toString() {
            return "Aggregate{numSamples=" + this.numSamples + ", avg=" + this.avg + ", var=" + this.var + '}';
        }
    }
}
