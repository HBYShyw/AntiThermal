package com.android.server.usage;

import android.app.usage.AppStandbyInfo;
import android.app.usage.UsageStatsManager;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.IndentingPrintWriter;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseLongArray;
import android.util.TimeUtils;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.XmlUtils;
import com.android.server.job.JobPackageTracker;
import com.android.server.job.controllers.JobStatus;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import com.android.server.voiceinteraction.DatabaseHelper;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class AppIdleHistory {

    @VisibleForTesting
    static final String APP_IDLE_FILENAME = "app_idle_stats.xml";
    private static final String ATTR_BUCKET = "bucket";
    private static final String ATTR_BUCKETING_REASON = "bucketReason";
    private static final String ATTR_BUCKET_ACTIVE_TIMEOUT_TIME = "activeTimeoutTime";
    private static final String ATTR_BUCKET_WORKING_SET_TIMEOUT_TIME = "workingSetTimeoutTime";
    private static final String ATTR_CURRENT_BUCKET = "appLimitBucket";
    private static final String ATTR_ELAPSED_IDLE = "elapsedIdleTime";
    private static final String ATTR_EXPIRY_TIME = "expiry";
    private static final String ATTR_LAST_PREDICTED_TIME = "lastPredictedTime";
    private static final String ATTR_LAST_RESTRICTION_ATTEMPT_ELAPSED = "lastRestrictionAttemptElapsedTime";
    private static final String ATTR_LAST_RESTRICTION_ATTEMPT_REASON = "lastRestrictionAttemptReason";
    private static final String ATTR_LAST_RUN_JOB_TIME = "lastJobRunTime";
    private static final String ATTR_LAST_USED_BY_USER_ELAPSED = "lastUsedByUserElapsedTime";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_NEXT_ESTIMATED_APP_LAUNCH_TIME = "nextEstimatedAppLaunchTime";
    private static final String ATTR_SCREEN_IDLE = "screenIdleTime";
    private static final String ATTR_VERSION = "version";
    private static final boolean DEBUG = false;
    static final int IDLE_BUCKET_CUTOFF = 40;
    private static final long ONE_MINUTE = 60000;
    static final int STANDBY_BUCKET_UNKNOWN = -1;
    private static final String TAG = "AppIdleHistory";
    private static final String TAG_BUCKET_EXPIRY_TIMES = "expiryTimes";
    private static final String TAG_ITEM = "item";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_PACKAGES = "packages";
    private static final int XML_VERSION_ADD_BUCKET_EXPIRY_TIMES = 1;
    private static final int XML_VERSION_CURRENT = 1;
    private static final int XML_VERSION_INITIAL = 0;
    private long mElapsedDuration;
    private long mElapsedSnapshot;
    private SparseArray<ArrayMap<String, AppUsageHistory>> mIdleHistory = new SparseArray<>();
    private boolean mScreenOn;
    private long mScreenOnDuration;
    private long mScreenOnSnapshot;
    private final File mStorageDir;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class AppUsageHistory {
        SparseLongArray bucketExpiryTimesMs;
        int bucketingReason;
        int currentBucket;
        int lastInformedBucket;
        long lastJobRunTime;
        int lastPredictedBucket = -1;
        long lastPredictedTime;
        long lastRestrictAttemptElapsedTime;
        int lastRestrictReason;
        long lastUsedByUserElapsedTime;
        long lastUsedElapsedTime;
        long lastUsedScreenTime;
        long nextEstimatedLaunchTime;

        AppUsageHistory() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppIdleHistory(File file, long j) {
        this.mElapsedSnapshot = j;
        this.mScreenOnSnapshot = j;
        this.mStorageDir = file;
        readScreenOnTime();
    }

    public void updateDisplay(boolean z, long j) {
        if (z == this.mScreenOn) {
            return;
        }
        this.mScreenOn = z;
        if (z) {
            this.mScreenOnSnapshot = j;
            return;
        }
        this.mScreenOnDuration += j - this.mScreenOnSnapshot;
        this.mElapsedDuration += j - this.mElapsedSnapshot;
        this.mElapsedSnapshot = j;
    }

    public long getScreenOnTime(long j) {
        long j2 = this.mScreenOnDuration;
        return this.mScreenOn ? j2 + (j - this.mScreenOnSnapshot) : j2;
    }

    @VisibleForTesting
    File getScreenOnTimeFile() {
        return new File(this.mStorageDir, "screen_on_time");
    }

    private void readScreenOnTime() {
        File screenOnTimeFile = getScreenOnTimeFile();
        if (screenOnTimeFile.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(screenOnTimeFile));
                this.mScreenOnDuration = Long.parseLong(bufferedReader.readLine());
                this.mElapsedDuration = Long.parseLong(bufferedReader.readLine());
                bufferedReader.close();
                return;
            } catch (IOException | NumberFormatException unused) {
                return;
            }
        }
        writeScreenOnTime();
    }

    private void writeScreenOnTime() {
        FileOutputStream fileOutputStream;
        AtomicFile atomicFile = new AtomicFile(getScreenOnTimeFile());
        try {
            fileOutputStream = atomicFile.startWrite();
            try {
                fileOutputStream.write((Long.toString(this.mScreenOnDuration) + "\n" + Long.toString(this.mElapsedDuration) + "\n").getBytes());
                atomicFile.finishWrite(fileOutputStream);
            } catch (IOException unused) {
                atomicFile.failWrite(fileOutputStream);
            }
        } catch (IOException unused2) {
            fileOutputStream = null;
        }
    }

    public void writeAppIdleDurations() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mElapsedDuration += elapsedRealtime - this.mElapsedSnapshot;
        this.mElapsedSnapshot = elapsedRealtime;
        writeScreenOnTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public AppUsageHistory reportUsage(AppUsageHistory appUsageHistory, String str, int i, int i2, int i3, long j, long j2) {
        int i4;
        int i5 = i3 | UsbTerminalTypes.TERMINAL_OUT_UNDEFINED;
        boolean isUserUsage = AppStandbyController.isUserUsage(i5);
        if (appUsageHistory.currentBucket == 45 && !isUserUsage) {
            int i6 = appUsageHistory.bucketingReason;
            if ((65280 & i6) != 512) {
                i5 = i6;
                i2 = 45;
                if (j != 0) {
                    long j3 = this.mElapsedDuration + (j - this.mElapsedSnapshot);
                    appUsageHistory.lastUsedElapsedTime = j3;
                    if (isUserUsage) {
                        appUsageHistory.lastUsedByUserElapsedTime = j3;
                    }
                    appUsageHistory.lastUsedScreenTime = getScreenOnTime(j);
                }
                i4 = appUsageHistory.currentBucket;
                if (i4 >= i2) {
                    if (i4 > i2) {
                        appUsageHistory.currentBucket = i2;
                        logAppStandbyBucketChanged(str, i, i2, i5);
                    }
                    appUsageHistory.bucketingReason = i5;
                }
                return appUsageHistory;
            }
        }
        if (j2 > j) {
            long elapsedTime = getElapsedTime(j2);
            if (appUsageHistory.bucketExpiryTimesMs == null) {
                appUsageHistory.bucketExpiryTimesMs = new SparseLongArray();
            }
            appUsageHistory.bucketExpiryTimesMs.put(i2, Math.max(elapsedTime, appUsageHistory.bucketExpiryTimesMs.get(i2)));
            removeElapsedExpiryTimes(appUsageHistory, getElapsedTime(j));
        }
        if (j != 0) {
        }
        i4 = appUsageHistory.currentBucket;
        if (i4 >= i2) {
        }
        return appUsageHistory;
    }

    private void removeElapsedExpiryTimes(AppUsageHistory appUsageHistory, long j) {
        SparseLongArray sparseLongArray = appUsageHistory.bucketExpiryTimesMs;
        if (sparseLongArray == null) {
            return;
        }
        for (int size = sparseLongArray.size() - 1; size >= 0; size--) {
            if (appUsageHistory.bucketExpiryTimesMs.valueAt(size) < j) {
                appUsageHistory.bucketExpiryTimesMs.removeAt(size);
            }
        }
    }

    public AppUsageHistory reportUsage(String str, int i, int i2, int i3, long j, long j2) {
        return reportUsage(getPackageHistory(getUserHistory(i), str, j, true), str, i, i2, i3, j, j2);
    }

    private ArrayMap<String, AppUsageHistory> getUserHistory(int i) {
        ArrayMap<String, AppUsageHistory> arrayMap = this.mIdleHistory.get(i);
        if (arrayMap != null) {
            return arrayMap;
        }
        ArrayMap<String, AppUsageHistory> arrayMap2 = new ArrayMap<>();
        this.mIdleHistory.put(i, arrayMap2);
        readAppIdleTimes(i, arrayMap2);
        return arrayMap2;
    }

    private AppUsageHistory getPackageHistory(ArrayMap<String, AppUsageHistory> arrayMap, String str, long j, boolean z) {
        AppUsageHistory appUsageHistory = arrayMap.get(str);
        if (appUsageHistory != null || !z) {
            return appUsageHistory;
        }
        AppUsageHistory appUsageHistory2 = new AppUsageHistory();
        appUsageHistory2.lastUsedByUserElapsedTime = -2147483648L;
        appUsageHistory2.lastUsedElapsedTime = -2147483648L;
        appUsageHistory2.lastUsedScreenTime = -2147483648L;
        appUsageHistory2.lastPredictedTime = -2147483648L;
        appUsageHistory2.currentBucket = 50;
        appUsageHistory2.bucketingReason = 256;
        appUsageHistory2.lastInformedBucket = -1;
        appUsageHistory2.lastJobRunTime = Long.MIN_VALUE;
        arrayMap.put(str, appUsageHistory2);
        return appUsageHistory2;
    }

    public void onUserRemoved(int i) {
        this.mIdleHistory.remove(i);
    }

    public boolean isIdle(String str, int i, long j) {
        return getPackageHistory(getUserHistory(i), str, j, true).currentBucket >= 40;
    }

    public AppUsageHistory getAppUsageHistory(String str, int i, long j) {
        return getPackageHistory(getUserHistory(i), str, j, true);
    }

    public void setAppStandbyBucket(String str, int i, long j, int i2, int i3) {
        setAppStandbyBucket(str, i, j, i2, i3, false);
    }

    public void setAppStandbyBucket(String str, int i, long j, int i2, int i3, boolean z) {
        SparseLongArray sparseLongArray;
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, true);
        boolean z2 = packageHistory.currentBucket != i2;
        packageHistory.currentBucket = i2;
        packageHistory.bucketingReason = i3;
        long elapsedTime = getElapsedTime(j);
        if ((65280 & i3) == 1280) {
            packageHistory.lastPredictedTime = elapsedTime;
            packageHistory.lastPredictedBucket = i2;
        }
        if (z && (sparseLongArray = packageHistory.bucketExpiryTimesMs) != null) {
            sparseLongArray.clear();
        }
        if (z2) {
            logAppStandbyBucketChanged(str, i, i2, i3);
        }
    }

    public void updateLastPrediction(AppUsageHistory appUsageHistory, long j, int i) {
        appUsageHistory.lastPredictedTime = j;
        appUsageHistory.lastPredictedBucket = i;
    }

    public void setEstimatedLaunchTime(String str, int i, long j, long j2) {
        getPackageHistory(getUserHistory(i), str, j, true).nextEstimatedLaunchTime = j2;
    }

    public void setLastJobRunTime(String str, int i, long j) {
        getPackageHistory(getUserHistory(i), str, j, true).lastJobRunTime = getElapsedTime(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void noteRestrictionAttempt(String str, int i, long j, int i2) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, true);
        packageHistory.lastRestrictAttemptElapsedTime = getElapsedTime(j);
        packageHistory.lastRestrictReason = i2;
    }

    public long getEstimatedLaunchTime(String str, int i, long j) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, false);
        return (packageHistory == null || packageHistory.nextEstimatedLaunchTime < System.currentTimeMillis()) ? JobStatus.NO_LATEST_RUNTIME : packageHistory.nextEstimatedLaunchTime;
    }

    public long getTimeSinceLastJobRun(String str, int i, long j) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, false);
        return (packageHistory == null || packageHistory.lastJobRunTime == Long.MIN_VALUE) ? JobStatus.NO_LATEST_RUNTIME : getElapsedTime(j) - packageHistory.lastJobRunTime;
    }

    public long getTimeSinceLastUsedByUser(String str, int i, long j) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, false);
        if (packageHistory == null) {
            return JobStatus.NO_LATEST_RUNTIME;
        }
        long j2 = packageHistory.lastUsedByUserElapsedTime;
        return (j2 == Long.MIN_VALUE || j2 <= 0) ? JobStatus.NO_LATEST_RUNTIME : getElapsedTime(j) - packageHistory.lastUsedByUserElapsedTime;
    }

    public int getAppStandbyBucket(String str, int i, long j) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, false);
        if (packageHistory == null) {
            return 50;
        }
        return packageHistory.currentBucket;
    }

    public ArrayList<AppStandbyInfo> getAppStandbyBuckets(int i, boolean z) {
        ArrayMap<String, AppUsageHistory> userHistory = getUserHistory(i);
        int size = userHistory.size();
        ArrayList<AppStandbyInfo> arrayList = new ArrayList<>(size);
        for (int i2 = 0; i2 < size; i2++) {
            arrayList.add(new AppStandbyInfo(userHistory.keyAt(i2), z ? userHistory.valueAt(i2).currentBucket : 10));
        }
        return arrayList;
    }

    public int getAppStandbyReason(String str, int i, long j) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, false);
        if (packageHistory != null) {
            return packageHistory.bucketingReason;
        }
        return 0;
    }

    public long getElapsedTime(long j) {
        return (j - this.mElapsedSnapshot) + this.mElapsedDuration;
    }

    public int setIdle(String str, int i, boolean z, long j) {
        int i2;
        int i3;
        if (z) {
            i2 = 40;
            i3 = 1024;
        } else {
            i2 = 10;
            i3 = UsbTerminalTypes.TERMINAL_OUT_HEADMOUNTED;
        }
        setAppStandbyBucket(str, i, j, i2, i3, false);
        return i2;
    }

    public void clearUsage(String str, int i) {
        getUserHistory(i).remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldInformListeners(String str, int i, long j, int i2) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, true);
        if (packageHistory.lastInformedBucket == i2) {
            return false;
        }
        packageHistory.lastInformedBucket = i2;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getThresholdIndex(String str, int i, long j, long[] jArr, long[] jArr2) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, false);
        if (packageHistory == null || packageHistory.lastUsedElapsedTime < 0 || packageHistory.lastUsedScreenTime < 0) {
            return -1;
        }
        long screenOnTime = getScreenOnTime(j) - packageHistory.lastUsedScreenTime;
        long elapsedTime = getElapsedTime(j) - packageHistory.lastUsedElapsedTime;
        for (int length = jArr.length - 1; length >= 0; length--) {
            if (screenOnTime >= jArr[length] && elapsedTime >= jArr2[length]) {
                return length;
            }
        }
        return 0;
    }

    private void logAppStandbyBucketChanged(String str, int i, int i2, int i3) {
        FrameworkStatsLog.write(258, str, i, i2, i3 & JobPackageTracker.EVENT_STOP_REASON_MASK, i3 & 255);
    }

    @VisibleForTesting
    long getBucketExpiryTimeMs(String str, int i, int i2, long j) {
        SparseLongArray sparseLongArray;
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, j, false);
        if (packageHistory == null || (sparseLongArray = packageHistory.bucketExpiryTimesMs) == null) {
            return 0L;
        }
        return sparseLongArray.get(i2, 0L);
    }

    @VisibleForTesting
    File getUserFile(int i) {
        return new File(new File(new File(this.mStorageDir, DatabaseHelper.SoundModelContract.KEY_USERS), Integer.toString(i)), APP_IDLE_FILENAME);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearLastUsedTimestamps(String str, int i) {
        AppUsageHistory packageHistory = getPackageHistory(getUserHistory(i), str, SystemClock.elapsedRealtime(), false);
        if (packageHistory != null) {
            packageHistory.lastUsedByUserElapsedTime = -2147483648L;
            packageHistory.lastUsedElapsedTime = -2147483648L;
            packageHistory.lastUsedScreenTime = -2147483648L;
        }
    }

    public boolean userFileExists(int i) {
        return getUserFile(i).exists();
    }

    private void readAppIdleTimes(int i, ArrayMap<String, AppUsageHistory> arrayMap) {
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2;
        int next;
        int i2;
        int i3;
        String str = null;
        try {
            FileInputStream openRead = new AtomicFile(getUserFile(i)).openRead();
            try {
                try {
                    XmlPullParser newPullParser = Xml.newPullParser();
                    newPullParser.setInput(openRead, StandardCharsets.UTF_8.name());
                    do {
                        next = newPullParser.next();
                        i2 = 1;
                        i3 = 2;
                        if (next == 2) {
                            break;
                        }
                    } while (next != 1);
                    if (next != 2) {
                        try {
                            Slog.e(TAG, "Unable to read app idle file for user " + i);
                            IoUtils.closeQuietly(openRead);
                            return;
                        } catch (IOException | XmlPullParserException e) {
                            e = e;
                            fileInputStream = openRead;
                            try {
                                Slog.e(TAG, "Unable to read app idle file for user " + i, e);
                                IoUtils.closeQuietly(fileInputStream);
                                return;
                            } catch (Throwable th) {
                                th = th;
                                IoUtils.closeQuietly(fileInputStream);
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            fileInputStream = openRead;
                            IoUtils.closeQuietly(fileInputStream);
                            throw th;
                        }
                    }
                    if (!newPullParser.getName().equals(TAG_PACKAGES)) {
                        IoUtils.closeQuietly(openRead);
                        return;
                    }
                    int intValue = getIntValue(newPullParser, ATTR_VERSION, 0);
                    while (true) {
                        int next2 = newPullParser.next();
                        if (next2 == i2) {
                            IoUtils.closeQuietly(openRead);
                            return;
                        }
                        if (next2 == i3) {
                            if (newPullParser.getName().equals("package")) {
                                String attributeValue = newPullParser.getAttributeValue(str, "name");
                                AppUsageHistory appUsageHistory = new AppUsageHistory();
                                long parseLong = Long.parseLong(newPullParser.getAttributeValue(str, ATTR_ELAPSED_IDLE));
                                appUsageHistory.lastUsedElapsedTime = parseLong;
                                appUsageHistory.lastUsedByUserElapsedTime = getLongValue(newPullParser, ATTR_LAST_USED_BY_USER_ELAPSED, parseLong);
                                appUsageHistory.lastUsedScreenTime = Long.parseLong(newPullParser.getAttributeValue(str, ATTR_SCREEN_IDLE));
                                appUsageHistory.lastPredictedTime = getLongValue(newPullParser, ATTR_LAST_PREDICTED_TIME, 0L);
                                String attributeValue2 = newPullParser.getAttributeValue(str, ATTR_CURRENT_BUCKET);
                                appUsageHistory.currentBucket = attributeValue2 == null ? 10 : Integer.parseInt(attributeValue2);
                                String attributeValue3 = newPullParser.getAttributeValue(str, ATTR_BUCKETING_REASON);
                                fileInputStream2 = openRead;
                                try {
                                    try {
                                        appUsageHistory.lastJobRunTime = getLongValue(newPullParser, ATTR_LAST_RUN_JOB_TIME, Long.MIN_VALUE);
                                        appUsageHistory.bucketingReason = 256;
                                        if (attributeValue3 != null) {
                                            try {
                                                appUsageHistory.bucketingReason = Integer.parseInt(attributeValue3, 16);
                                            } catch (NumberFormatException e2) {
                                                Slog.wtf(TAG, "Unable to read bucketing reason", e2);
                                            }
                                        }
                                        appUsageHistory.lastRestrictAttemptElapsedTime = getLongValue(newPullParser, ATTR_LAST_RESTRICTION_ATTEMPT_ELAPSED, 0L);
                                        String attributeValue4 = newPullParser.getAttributeValue(null, ATTR_LAST_RESTRICTION_ATTEMPT_REASON);
                                        if (attributeValue4 != null) {
                                            try {
                                                appUsageHistory.lastRestrictReason = Integer.parseInt(attributeValue4, 16);
                                            } catch (NumberFormatException e3) {
                                                Slog.wtf(TAG, "Unable to read last restrict reason", e3);
                                            }
                                        }
                                        appUsageHistory.nextEstimatedLaunchTime = getLongValue(newPullParser, ATTR_NEXT_ESTIMATED_APP_LAUNCH_TIME, 0L);
                                        appUsageHistory.lastInformedBucket = -1;
                                        arrayMap.put(attributeValue, appUsageHistory);
                                        if (intValue >= 1) {
                                            int depth = newPullParser.getDepth();
                                            while (XmlUtils.nextElementWithin(newPullParser, depth)) {
                                                if (TAG_BUCKET_EXPIRY_TIMES.equals(newPullParser.getName())) {
                                                    readBucketExpiryTimes(newPullParser, appUsageHistory);
                                                }
                                            }
                                        } else {
                                            long longValue = getLongValue(newPullParser, ATTR_BUCKET_ACTIVE_TIMEOUT_TIME, 0L);
                                            long longValue2 = getLongValue(newPullParser, ATTR_BUCKET_WORKING_SET_TIMEOUT_TIME, 0L);
                                            if (longValue != 0 || longValue2 != 0) {
                                                insertBucketExpiryTime(appUsageHistory, 10, longValue);
                                                insertBucketExpiryTime(appUsageHistory, 20, longValue2);
                                            }
                                        }
                                    } catch (IOException | XmlPullParserException e4) {
                                        e = e4;
                                        fileInputStream = fileInputStream2;
                                        Slog.e(TAG, "Unable to read app idle file for user " + i, e);
                                        IoUtils.closeQuietly(fileInputStream);
                                        return;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    fileInputStream = fileInputStream2;
                                    IoUtils.closeQuietly(fileInputStream);
                                    throw th;
                                }
                            } else {
                                fileInputStream2 = openRead;
                            }
                            openRead = fileInputStream2;
                            str = null;
                            i2 = 1;
                            i3 = 2;
                        }
                    }
                } catch (IOException | XmlPullParserException e5) {
                    e = e5;
                    fileInputStream2 = openRead;
                }
            } catch (Throwable th4) {
                th = th4;
                fileInputStream2 = openRead;
            }
        } catch (IOException | XmlPullParserException e6) {
            e = e6;
            fileInputStream = null;
        } catch (Throwable th5) {
            th = th5;
            fileInputStream = null;
        }
    }

    private void readBucketExpiryTimes(XmlPullParser xmlPullParser, AppUsageHistory appUsageHistory) throws IOException, XmlPullParserException {
        int depth = xmlPullParser.getDepth();
        while (XmlUtils.nextElementWithin(xmlPullParser, depth)) {
            if ("item".equals(xmlPullParser.getName())) {
                int intValue = getIntValue(xmlPullParser, ATTR_BUCKET, -1);
                if (intValue == -1) {
                    Slog.e(TAG, "Error reading the buckets expiry times");
                } else {
                    insertBucketExpiryTime(appUsageHistory, intValue, getLongValue(xmlPullParser, ATTR_EXPIRY_TIME, 0L));
                }
            }
        }
    }

    private void insertBucketExpiryTime(AppUsageHistory appUsageHistory, int i, long j) {
        if (j == 0) {
            return;
        }
        if (appUsageHistory.bucketExpiryTimesMs == null) {
            appUsageHistory.bucketExpiryTimesMs = new SparseLongArray();
        }
        appUsageHistory.bucketExpiryTimesMs.put(i, j);
    }

    private long getLongValue(XmlPullParser xmlPullParser, String str, long j) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? j : Long.parseLong(attributeValue);
    }

    private int getIntValue(XmlPullParser xmlPullParser, String str, int i) {
        String attributeValue = xmlPullParser.getAttributeValue(null, str);
        return attributeValue == null ? i : Integer.parseInt(attributeValue);
    }

    public void writeAppIdleTimes(long j) {
        int size = this.mIdleHistory.size();
        for (int i = 0; i < size; i++) {
            writeAppIdleTimes(this.mIdleHistory.keyAt(i), j);
        }
    }

    public void writeAppIdleTimes(int i, long j) {
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2;
        ArrayMap<String, AppUsageHistory> arrayMap;
        FastXmlSerializer fastXmlSerializer;
        long j2;
        int i2;
        AtomicFile atomicFile = new AtomicFile(getUserFile(i));
        FileOutputStream fileOutputStream3 = null;
        String str = null;
        try {
            FileOutputStream startWrite = atomicFile.startWrite();
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(startWrite);
                FastXmlSerializer fastXmlSerializer2 = new FastXmlSerializer();
                fastXmlSerializer2.setOutput(bufferedOutputStream, StandardCharsets.UTF_8.name());
                fastXmlSerializer2.startDocument((String) null, Boolean.TRUE);
                fastXmlSerializer2.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                fastXmlSerializer2.startTag((String) null, TAG_PACKAGES);
                fastXmlSerializer2.attribute((String) null, ATTR_VERSION, String.valueOf(1));
                long elapsedTime = getElapsedTime(j);
                ArrayMap<String, AppUsageHistory> userHistory = getUserHistory(i);
                int size = userHistory.size();
                int i3 = 0;
                while (i3 < size) {
                    try {
                        String keyAt = userHistory.keyAt(i3);
                        if (keyAt == null) {
                            try {
                                Slog.w(TAG, "Skipping App Idle write for unexpected null package");
                                fileOutputStream2 = startWrite;
                                arrayMap = userHistory;
                                fastXmlSerializer = fastXmlSerializer2;
                                j2 = elapsedTime;
                            } catch (Exception e) {
                                e = e;
                                fileOutputStream3 = startWrite;
                                atomicFile.failWrite(fileOutputStream3);
                                Slog.e(TAG, "Error writing app idle file for user " + i, e);
                            }
                        } else {
                            AppUsageHistory valueAt = userHistory.valueAt(i3);
                            fastXmlSerializer2.startTag(str, "package");
                            arrayMap = userHistory;
                            fastXmlSerializer2.attribute(str, "name", keyAt);
                            fileOutputStream2 = startWrite;
                            try {
                                fastXmlSerializer2.attribute((String) null, ATTR_ELAPSED_IDLE, Long.toString(valueAt.lastUsedElapsedTime));
                                fastXmlSerializer2.attribute((String) null, ATTR_LAST_USED_BY_USER_ELAPSED, Long.toString(valueAt.lastUsedByUserElapsedTime));
                                fastXmlSerializer = fastXmlSerializer2;
                                fastXmlSerializer.attribute((String) null, ATTR_SCREEN_IDLE, Long.toString(valueAt.lastUsedScreenTime));
                                j2 = elapsedTime;
                                fastXmlSerializer.attribute((String) null, ATTR_LAST_PREDICTED_TIME, Long.toString(valueAt.lastPredictedTime));
                                fastXmlSerializer.attribute((String) null, ATTR_CURRENT_BUCKET, Integer.toString(valueAt.currentBucket));
                                fastXmlSerializer.attribute((String) null, ATTR_BUCKETING_REASON, Integer.toHexString(valueAt.bucketingReason));
                                long j3 = valueAt.lastJobRunTime;
                                if (j3 != Long.MIN_VALUE) {
                                    fastXmlSerializer.attribute((String) null, ATTR_LAST_RUN_JOB_TIME, Long.toString(j3));
                                }
                                long j4 = valueAt.lastRestrictAttemptElapsedTime;
                                if (j4 > 0) {
                                    fastXmlSerializer.attribute((String) null, ATTR_LAST_RESTRICTION_ATTEMPT_ELAPSED, Long.toString(j4));
                                }
                                fastXmlSerializer.attribute((String) null, ATTR_LAST_RESTRICTION_ATTEMPT_REASON, Integer.toHexString(valueAt.lastRestrictReason));
                                long j5 = valueAt.nextEstimatedLaunchTime;
                                if (j5 > 0) {
                                    fastXmlSerializer.attribute((String) null, ATTR_NEXT_ESTIMATED_APP_LAUNCH_TIME, Long.toString(j5));
                                }
                                if (valueAt.bucketExpiryTimesMs != null) {
                                    fastXmlSerializer.startTag((String) null, TAG_BUCKET_EXPIRY_TIMES);
                                    int size2 = valueAt.bucketExpiryTimesMs.size();
                                    int i4 = 0;
                                    while (i4 < size2) {
                                        long valueAt2 = valueAt.bucketExpiryTimesMs.valueAt(i4);
                                        if (valueAt2 < j2) {
                                            i2 = size2;
                                        } else {
                                            int keyAt2 = valueAt.bucketExpiryTimesMs.keyAt(i4);
                                            fastXmlSerializer.startTag((String) null, "item");
                                            i2 = size2;
                                            fastXmlSerializer.attribute((String) null, ATTR_BUCKET, String.valueOf(keyAt2));
                                            fastXmlSerializer.attribute((String) null, ATTR_EXPIRY_TIME, String.valueOf(valueAt2));
                                            fastXmlSerializer.endTag((String) null, "item");
                                        }
                                        i4++;
                                        size2 = i2;
                                    }
                                    fastXmlSerializer.endTag((String) null, TAG_BUCKET_EXPIRY_TIMES);
                                }
                                fastXmlSerializer.endTag((String) null, "package");
                            } catch (Exception e2) {
                                e = e2;
                                fileOutputStream3 = fileOutputStream2;
                                atomicFile.failWrite(fileOutputStream3);
                                Slog.e(TAG, "Error writing app idle file for user " + i, e);
                            }
                        }
                        i3++;
                        userHistory = arrayMap;
                        fastXmlSerializer2 = fastXmlSerializer;
                        startWrite = fileOutputStream2;
                        elapsedTime = j2;
                        str = null;
                    } catch (Exception e3) {
                        e = e3;
                        fileOutputStream2 = startWrite;
                    }
                }
                FileOutputStream fileOutputStream4 = startWrite;
                FastXmlSerializer fastXmlSerializer3 = fastXmlSerializer2;
                try {
                    fastXmlSerializer3.endTag(str, TAG_PACKAGES);
                    fastXmlSerializer3.endDocument();
                    fileOutputStream = fileOutputStream4;
                    try {
                        atomicFile.finishWrite(fileOutputStream);
                    } catch (Exception e4) {
                        e = e4;
                        fileOutputStream3 = fileOutputStream;
                        atomicFile.failWrite(fileOutputStream3);
                        Slog.e(TAG, "Error writing app idle file for user " + i, e);
                    }
                } catch (Exception e5) {
                    e = e5;
                    fileOutputStream = fileOutputStream4;
                }
            } catch (Exception e6) {
                e = e6;
                fileOutputStream = startWrite;
            }
        } catch (Exception e7) {
            e = e7;
        }
    }

    public void dumpUsers(IndentingPrintWriter indentingPrintWriter, int[] iArr, List<String> list) {
        for (int i : iArr) {
            indentingPrintWriter.println();
            dumpUser(indentingPrintWriter, i, list);
        }
    }

    private void dumpUser(IndentingPrintWriter indentingPrintWriter, int i, List<String> list) {
        ArrayMap<String, AppUsageHistory> arrayMap;
        int i2;
        int i3;
        int i4;
        int i5 = i;
        indentingPrintWriter.print("User ");
        indentingPrintWriter.print(i);
        indentingPrintWriter.println(" App Standby States:");
        indentingPrintWriter.increaseIndent();
        ArrayMap<String, AppUsageHistory> arrayMap2 = this.mIdleHistory.get(i5);
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long elapsedTime = getElapsedTime(elapsedRealtime);
        getScreenOnTime(elapsedRealtime);
        if (arrayMap2 == null) {
            return;
        }
        int size = arrayMap2.size();
        int i6 = 0;
        while (i6 < size) {
            String keyAt = arrayMap2.keyAt(i6);
            AppUsageHistory valueAt = arrayMap2.valueAt(i6);
            if (CollectionUtils.isEmpty(list) || list.contains(keyAt)) {
                indentingPrintWriter.print("package=" + keyAt);
                indentingPrintWriter.print(" u=" + i5);
                indentingPrintWriter.print(" bucket=" + valueAt.currentBucket + " reason=" + UsageStatsManager.reasonToString(valueAt.bucketingReason));
                indentingPrintWriter.print(" used=");
                arrayMap = arrayMap2;
                i2 = size;
                i3 = i6;
                printLastActionElapsedTime(indentingPrintWriter, elapsedTime, valueAt.lastUsedElapsedTime);
                indentingPrintWriter.print(" usedByUser=");
                printLastActionElapsedTime(indentingPrintWriter, elapsedTime, valueAt.lastUsedByUserElapsedTime);
                indentingPrintWriter.print(" usedScr=");
                printLastActionElapsedTime(indentingPrintWriter, elapsedTime, valueAt.lastUsedScreenTime);
                indentingPrintWriter.print(" lastPred=");
                printLastActionElapsedTime(indentingPrintWriter, elapsedTime, valueAt.lastPredictedTime);
                dumpBucketExpiryTimes(indentingPrintWriter, valueAt, elapsedTime);
                indentingPrintWriter.print(" lastJob=");
                TimeUtils.formatDuration(elapsedTime - valueAt.lastJobRunTime, indentingPrintWriter);
                indentingPrintWriter.print(" lastInformedBucket=" + valueAt.lastInformedBucket);
                if (valueAt.lastRestrictAttemptElapsedTime > 0) {
                    indentingPrintWriter.print(" lastRestrictAttempt=");
                    TimeUtils.formatDuration(elapsedTime - valueAt.lastRestrictAttemptElapsedTime, indentingPrintWriter);
                    indentingPrintWriter.print(" lastRestrictReason=" + UsageStatsManager.reasonToString(valueAt.lastRestrictReason));
                }
                if (valueAt.nextEstimatedLaunchTime > 0) {
                    indentingPrintWriter.print(" nextEstimatedLaunchTime=");
                    TimeUtils.formatDuration(valueAt.nextEstimatedLaunchTime - currentTimeMillis, indentingPrintWriter);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(" idle=");
                i4 = i;
                sb.append(isIdle(keyAt, i4, elapsedRealtime) ? "y" : "n");
                indentingPrintWriter.print(sb.toString());
                indentingPrintWriter.println();
            } else {
                i2 = size;
                i3 = i6;
                i4 = i5;
                arrayMap = arrayMap2;
            }
            i6 = i3 + 1;
            i5 = i4;
            arrayMap2 = arrayMap;
            size = i2;
        }
        indentingPrintWriter.println();
        indentingPrintWriter.print("totalElapsedTime=");
        TimeUtils.formatDuration(getElapsedTime(elapsedRealtime), indentingPrintWriter);
        indentingPrintWriter.println();
        indentingPrintWriter.print("totalScreenOnTime=");
        TimeUtils.formatDuration(getScreenOnTime(elapsedRealtime), indentingPrintWriter);
        indentingPrintWriter.println();
        indentingPrintWriter.decreaseIndent();
    }

    private void printLastActionElapsedTime(IndentingPrintWriter indentingPrintWriter, long j, long j2) {
        if (j2 < 0) {
            indentingPrintWriter.print("<uninitialized>");
        } else {
            TimeUtils.formatDuration(j - j2, indentingPrintWriter);
        }
    }

    private void dumpBucketExpiryTimes(IndentingPrintWriter indentingPrintWriter, AppUsageHistory appUsageHistory, long j) {
        indentingPrintWriter.print(" expiryTimes=");
        SparseLongArray sparseLongArray = appUsageHistory.bucketExpiryTimesMs;
        if (sparseLongArray == null || sparseLongArray.size() == 0) {
            indentingPrintWriter.print("<none>");
            return;
        }
        indentingPrintWriter.print("(");
        int size = appUsageHistory.bucketExpiryTimesMs.size();
        for (int i = 0; i < size; i++) {
            int keyAt = appUsageHistory.bucketExpiryTimesMs.keyAt(i);
            long valueAt = appUsageHistory.bucketExpiryTimesMs.valueAt(i);
            if (i != 0) {
                indentingPrintWriter.print(",");
            }
            indentingPrintWriter.print(keyAt + ":");
            TimeUtils.formatDuration(j - valueAt, indentingPrintWriter);
        }
        indentingPrintWriter.print(")");
    }
}
