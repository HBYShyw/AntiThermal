package com.android.server.job.controllers;

import android.app.AppGlobals;
import android.app.job.JobInfo;
import android.app.job.JobWorkItem;
import android.app.job.UserVisibleJobSummary;
import android.content.ClipData;
import android.content.ComponentName;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Pair;
import android.util.Range;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.server.LocalServices;
import com.android.server.job.GrantedUriPermissions;
import com.android.server.job.JobSchedulerInternal;
import com.android.server.job.JobSchedulerService;
import com.android.server.job.controllers.ContentObserverController;
import com.android.server.location.contexthub.ContextHubStatsLog;
import com.android.server.sensorprivacy.SensorPrivacyService;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.usb.descriptors.UsbDescriptor;
import dalvik.annotation.optimization.NeverCompile;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class JobStatus {
    static final int CONSTRAINTS_OF_INTEREST = -1801421809;
    static final int CONSTRAINT_BACKGROUND_NOT_RESTRICTED = 4194304;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static final int CONSTRAINT_BATTERY_NOT_LOW = 2;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static final int CONSTRAINT_CHARGING = 1;
    public static final int CONSTRAINT_CONNECTIVITY = 268435456;
    public static final int CONSTRAINT_CONTENT_TRIGGER = 67108864;
    public static final int CONSTRAINT_DEADLINE = 1073741824;
    static final int CONSTRAINT_DEVICE_NOT_DOZING = 33554432;
    static final int CONSTRAINT_FLEXIBLE = 2097152;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static final int CONSTRAINT_IDLE = 4;
    static final int CONSTRAINT_PREFETCH = 8388608;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static final int CONSTRAINT_STORAGE_NOT_LOW = 8;
    static final int CONSTRAINT_TARE_WEALTH = 134217728;
    public static final int CONSTRAINT_TIMING_DELAY = Integer.MIN_VALUE;
    static final int CONSTRAINT_WITHIN_QUOTA = 16777216;
    static final boolean DEBUG_PREPARE = true;
    public static final long DEFAULT_TRIGGER_MAX_DELAY = 120000;
    public static final long DEFAULT_TRIGGER_UPDATE_DELAY = 10000;
    private static final int DYNAMIC_EXPEDITED_DEFERRAL_CONSTRAINTS = 37748736;
    private static final int DYNAMIC_RESTRICTED_CONSTRAINTS = 268435463;
    private static final int IMPLICIT_CONSTRAINTS = 188743680;
    public static final int INTERNAL_FLAG_DEMOTED_BY_SYSTEM_UIJ = 4;
    public static final int INTERNAL_FLAG_DEMOTED_BY_USER = 2;
    public static final int INTERNAL_FLAG_HAS_FOREGROUND_EXEMPTION = 1;
    private static final int MAX_NAMESPACE_CACHE_SIZE = 128;
    public static final long MIN_TRIGGER_MAX_DELAY = 1000;
    public static final long MIN_TRIGGER_UPDATE_DELAY = 500;

    @VisibleForTesting
    static final long MIN_WINDOW_FOR_FLEXIBILITY_MS = 3600000;
    public static final long NO_EARLIEST_RUNTIME = 0;
    public static final long NO_LATEST_RUNTIME = Long.MAX_VALUE;
    private static final int NUM_CONSTRAINT_CHANGE_HISTORY = 10;
    public static final int OVERRIDE_FULL = 3;
    public static final int OVERRIDE_NONE = 0;
    public static final int OVERRIDE_SOFT = 2;
    public static final int OVERRIDE_SORTING = 1;
    static final int SOFT_OVERRIDE_CONSTRAINTS = -2136997873;
    private static final int STATSD_CONSTRAINTS_TO_LOG = -847249408;
    private static final boolean STATS_LOG_ENABLED = false;
    private static final String TAG = "JobScheduler.JobStatus";
    public static final int TRACKING_BATTERY = 1;
    public static final int TRACKING_CONNECTIVITY = 2;
    public static final int TRACKING_CONTENT = 4;
    public static final int TRACKING_FLEXIBILITY = 128;
    public static final int TRACKING_IDLE = 8;
    public static final int TRACKING_QUOTA = 64;
    public static final int TRACKING_STORAGE = 16;
    public static final int TRACKING_TIME = 32;
    private static MessageDigest sMessageDigest;
    public boolean appHasDozeExemption;
    final String batteryName;
    final int callingUid;
    public ArraySet<String> changedAuthorities;
    public ArraySet<Uri> changedUris;
    ContentObserverController.JobInstance contentObserverJobInstance;
    private final long earliestRunTimeElapsedMillis;
    public long enqueueTime;
    public ArrayList<JobWorkItem> executingWork;
    final JobInfo job;
    public int lastEvaluatedBias;
    private final long latestRunTimeElapsedMillis;
    private int mConstraintChangeHistoryIndex;
    private final int[] mConstraintStatusHistory;
    private final long[] mConstraintUpdatedTimesElapsed;
    private long mCumulativeExecutionTimeMs;
    private int mDynamicConstraints;
    private boolean mExpeditedQuotaApproved;
    private boolean mExpeditedTareApproved;
    private long mFirstForceBatchedTimeElapsed;
    private boolean mHasAccessToUnmetered;
    private final boolean mHasExemptedMediaUrisOnly;
    private boolean mHasMediaBackupExemption;
    private int mInternalFlags;
    private boolean mIsUserBgRestricted;
    private JobStatusWrapper mJobStatusWrapper;
    private long mLastFailedRunTime;
    private long mLastSuccessfulRunTime;
    private boolean mLoggedBucketMismatch;
    private final long mLoggingJobId;
    private long mMinimumNetworkChunkBytes;
    private final String mNamespace;
    private final String mNamespaceHash;
    private int mNumDroppedFlexibleConstraints;
    private final int mNumRequiredFlexibleConstraints;
    private final int mNumSystemStops;
    private long mOriginalLatestRunTimeElapsedMillis;
    private Pair<Long, Long> mPersistedUtcTimes;
    private final boolean mPreferUnmetered;
    private final int mPreferredConstraints;
    private boolean mReadyDeadlineSatisfied;
    private boolean mReadyDynamicSatisfied;
    private boolean mReadyNotDozing;
    private boolean mReadyNotRestrictedInBg;
    private boolean mReadyTareWealth;
    private boolean mReadyWithinQuota;
    private int mReasonReadyToUnready;
    private final int mRequiredConstraintsOfInterest;
    private int mSatisfiedConstraintsOfInterest;
    private long mTotalNetworkDownloadBytes;
    private long mTotalNetworkUploadBytes;
    private UserVisibleJobSummary mUserVisibleJobSummary;
    public long madeActive;
    public long madePending;
    public Network network;
    public int nextPendingWorkId;
    private final int numFailures;
    public int overrideState;
    public ArrayList<JobWorkItem> pendingWork;
    private boolean prepared;
    final int requiredConstraints;
    int satisfiedConstraints;
    public String serviceProcessName;
    final String sourcePackageName;
    final String sourceTag;
    final int sourceUid;
    final int sourceUserId;
    private int standbyBucket;
    public boolean startedAsExpeditedJob;
    public boolean startedAsUserInitiatedJob;
    public boolean startedWithForegroundFlag;
    public boolean startedWithImmediacyPrivilege;
    final String tag;
    private int trackingControllers;
    public boolean uidActive;
    private Throwable unpreparedPoint;
    private GrantedUriPermissions uriPerms;
    private long whenStandbyDeferred;
    static final boolean DEBUG = JobSchedulerService.DEBUG;

    @GuardedBy({"sNamespaceHashCache"})
    private static final ArrayMap<String, String> sNamespaceHashCache = new ArrayMap<>();
    private static final Uri[] MEDIA_URIS_FOR_STANDBY_EXEMPTION = {MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.EXTERNAL_CONTENT_URI};
    private static IJobStatusExt mJobStatusExt = (IJobStatusExt) ExtLoader.type(IJobStatusExt.class).create();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getProtoConstraint(int i) {
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                switch (i) {
                    case Integer.MIN_VALUE:
                        return 4;
                    case 4:
                        return 6;
                    case 8:
                        return 3;
                    case 2097152:
                        return 15;
                    case 4194304:
                        return 11;
                    case 8388608:
                        return 14;
                    case 16777216:
                        return 10;
                    case 33554432:
                        return 9;
                    case 67108864:
                        return 8;
                    case 134217728:
                        return 13;
                    case 268435456:
                        return 7;
                    case 1073741824:
                        return 5;
                    default:
                        return 0;
                }
            }
        }
        return i2;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0134  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0152  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01c8  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01cf  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x018f  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x007b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private JobStatus(JobInfo jobInfo, int i, String str, int i2, int i3, String str2, String str3, int i4, int i5, long j, long j2, long j3, long j4, long j5, int i6, int i7) {
        int i8;
        boolean z;
        int preferredConstraintFlags;
        this.unpreparedPoint = null;
        this.satisfiedConstraints = 0;
        this.mSatisfiedConstraintsOfInterest = 0;
        this.mDynamicConstraints = 0;
        this.startedAsExpeditedJob = false;
        this.startedAsUserInitiatedJob = false;
        this.startedWithForegroundFlag = false;
        this.startedWithImmediacyPrivilege = false;
        this.nextPendingWorkId = 1;
        this.overrideState = 0;
        this.mConstraintChangeHistoryIndex = 0;
        this.mConstraintUpdatedTimesElapsed = new long[10];
        this.mConstraintStatusHistory = new int[10];
        this.mTotalNetworkDownloadBytes = -1L;
        this.mTotalNetworkUploadBytes = -1L;
        this.mMinimumNetworkChunkBytes = -1L;
        this.mReasonReadyToUnready = 0;
        this.mJobStatusWrapper = new JobStatusWrapper();
        this.job = jobInfo;
        this.callingUid = i;
        this.standbyBucket = i3;
        this.mNamespace = str2;
        this.mNamespaceHash = generateNamespaceHash(str2);
        this.mLoggingJobId = generateLoggingId(str2, jobInfo.getId());
        if (i2 != -1 && str != null) {
            try {
                i8 = AppGlobals.getPackageManager().getPackageUid(str, 0L, i2);
            } catch (RemoteException unused) {
            }
            if (i8 != -1) {
                this.sourceUid = i;
                this.sourceUserId = UserHandle.getUserId(i);
                this.sourcePackageName = jobInfo.getService().getPackageName();
                this.sourceTag = null;
                mJobStatusExt.setSyncJobAbnormal(jobInfo);
            } else {
                this.sourceUid = i8;
                this.sourceUserId = i2;
                this.sourcePackageName = str;
                this.sourceTag = str3;
            }
            String str4 = str2 != null ? "" : "@" + str2 + "@";
            String str5 = this.sourceTag == null ? str4 + this.sourceTag + ":" + jobInfo.getService().getPackageName() : str4 + jobInfo.getService().flattenToShortString();
            this.batteryName = str5;
            this.tag = "*job*/" + str5 + "#" + jobInfo.getId();
            this.earliestRunTimeElapsedMillis = j;
            this.latestRunTimeElapsedMillis = j2;
            this.mOriginalLatestRunTimeElapsedMillis = j2;
            this.numFailures = i4;
            this.mNumSystemStops = i5;
            int constraintFlags = jobInfo.getConstraintFlags();
            constraintFlags = jobInfo.getRequiredNetwork() != null ? constraintFlags | 268435456 : constraintFlags;
            constraintFlags = j != 0 ? constraintFlags | Integer.MIN_VALUE : constraintFlags;
            constraintFlags = j2 != NO_LATEST_RUNTIME ? constraintFlags | 1073741824 : constraintFlags;
            constraintFlags = jobInfo.isPrefetch() ? constraintFlags | 8388608 : constraintFlags;
            if (jobInfo.getTriggerContentUris() != null) {
                constraintFlags |= 67108864;
                for (JobInfo.TriggerContentUri triggerContentUri : jobInfo.getTriggerContentUris()) {
                    if (ArrayUtils.contains(MEDIA_URIS_FOR_STANDBY_EXEMPTION, triggerContentUri.getUri())) {
                    }
                }
                z = true;
                int initRequiredConstraints = constraintFlags | mJobStatusExt.initRequiredConstraints(jobInfo);
                this.mHasExemptedMediaUrisOnly = z;
                preferredConstraintFlags = jobInfo.getPreferredConstraintFlags();
                this.mPreferredConstraints = preferredConstraintFlags;
                this.mPreferUnmetered = false;
                boolean z2 = j2 - j >= 3600000;
                if (preferredConstraintFlags == 0 && !isRequestedExpeditedJob() && !jobInfo.isUserInitiated() && z2 && i4 + i5 != 1) {
                    this.mNumRequiredFlexibleConstraints = Integer.bitCount(preferredConstraintFlags);
                    initRequiredConstraints |= 2097152;
                } else {
                    this.mNumRequiredFlexibleConstraints = 0;
                }
                this.requiredConstraints = initRequiredConstraints;
                this.mRequiredConstraintsOfInterest = initRequiredConstraints & CONSTRAINTS_OF_INTEREST;
                addDynamicConstraints(i7);
                this.mReadyNotDozing = canRunInDoze();
                if (i3 == 5) {
                    addDynamicConstraints(DYNAMIC_RESTRICTED_CONSTRAINTS);
                } else {
                    this.mReadyDynamicSatisfied = false;
                }
                this.mCumulativeExecutionTimeMs = j5;
                this.mLastSuccessfulRunTime = j3;
                this.mLastFailedRunTime = j4;
                this.mInternalFlags = i6;
                updateNetworkBytesLocked();
                if (jobInfo.getRequiredNetwork() != null) {
                    JobInfo.Builder builder = new JobInfo.Builder(jobInfo);
                    NetworkRequest.Builder builder2 = new NetworkRequest.Builder(jobInfo.getRequiredNetwork());
                    builder2.setUids(Collections.singleton(new Range(Integer.valueOf(this.sourceUid), Integer.valueOf(this.sourceUid))));
                    builder.setRequiredNetwork(builder2.build());
                    builder.build(false, false);
                }
                updateMediaBackupExemptionStatus();
            }
            z = false;
            int initRequiredConstraints2 = constraintFlags | mJobStatusExt.initRequiredConstraints(jobInfo);
            this.mHasExemptedMediaUrisOnly = z;
            preferredConstraintFlags = jobInfo.getPreferredConstraintFlags();
            this.mPreferredConstraints = preferredConstraintFlags;
            this.mPreferUnmetered = false;
            if (j2 - j >= 3600000) {
            }
            if (preferredConstraintFlags == 0) {
            }
            this.mNumRequiredFlexibleConstraints = 0;
            this.requiredConstraints = initRequiredConstraints2;
            this.mRequiredConstraintsOfInterest = initRequiredConstraints2 & CONSTRAINTS_OF_INTEREST;
            addDynamicConstraints(i7);
            this.mReadyNotDozing = canRunInDoze();
            if (i3 == 5) {
            }
            this.mCumulativeExecutionTimeMs = j5;
            this.mLastSuccessfulRunTime = j3;
            this.mLastFailedRunTime = j4;
            this.mInternalFlags = i6;
            updateNetworkBytesLocked();
            if (jobInfo.getRequiredNetwork() != null) {
            }
            updateMediaBackupExemptionStatus();
        }
        i8 = -1;
        if (i8 != -1) {
        }
        if (str2 != null) {
        }
        if (this.sourceTag == null) {
        }
        this.batteryName = str5;
        this.tag = "*job*/" + str5 + "#" + jobInfo.getId();
        this.earliestRunTimeElapsedMillis = j;
        this.latestRunTimeElapsedMillis = j2;
        this.mOriginalLatestRunTimeElapsedMillis = j2;
        this.numFailures = i4;
        this.mNumSystemStops = i5;
        int constraintFlags2 = jobInfo.getConstraintFlags();
        if (jobInfo.getRequiredNetwork() != null) {
        }
        if (j != 0) {
        }
        if (j2 != NO_LATEST_RUNTIME) {
        }
        if (jobInfo.isPrefetch()) {
        }
        if (jobInfo.getTriggerContentUris() != null) {
        }
        z = false;
        int initRequiredConstraints22 = constraintFlags2 | mJobStatusExt.initRequiredConstraints(jobInfo);
        this.mHasExemptedMediaUrisOnly = z;
        preferredConstraintFlags = jobInfo.getPreferredConstraintFlags();
        this.mPreferredConstraints = preferredConstraintFlags;
        this.mPreferUnmetered = false;
        if (j2 - j >= 3600000) {
        }
        if (preferredConstraintFlags == 0) {
        }
        this.mNumRequiredFlexibleConstraints = 0;
        this.requiredConstraints = initRequiredConstraints22;
        this.mRequiredConstraintsOfInterest = initRequiredConstraints22 & CONSTRAINTS_OF_INTEREST;
        addDynamicConstraints(i7);
        this.mReadyNotDozing = canRunInDoze();
        if (i3 == 5) {
        }
        this.mCumulativeExecutionTimeMs = j5;
        this.mLastSuccessfulRunTime = j3;
        this.mLastFailedRunTime = j4;
        this.mInternalFlags = i6;
        updateNetworkBytesLocked();
        if (jobInfo.getRequiredNetwork() != null) {
        }
        updateMediaBackupExemptionStatus();
    }

    public JobStatus(JobStatus jobStatus) {
        this(jobStatus.getJob(), jobStatus.getUid(), jobStatus.getSourcePackageName(), jobStatus.getSourceUserId(), jobStatus.getStandbyBucket(), jobStatus.getNamespace(), jobStatus.getSourceTag(), jobStatus.getNumFailures(), jobStatus.getNumSystemStops(), jobStatus.getEarliestRunTime(), jobStatus.getLatestRunTimeElapsed(), jobStatus.getLastSuccessfulRunTime(), jobStatus.getLastFailedRunTime(), jobStatus.getCumulativeExecutionTimeMs(), jobStatus.getInternalFlags(), jobStatus.mDynamicConstraints);
        this.mPersistedUtcTimes = jobStatus.mPersistedUtcTimes;
        if (jobStatus.mPersistedUtcTimes != null && DEBUG) {
            Slog.i(TAG, "Cloning job with persisted run times", new RuntimeException("here"));
        }
        ArrayList<JobWorkItem> arrayList = jobStatus.executingWork;
        if (arrayList != null && arrayList.size() > 0) {
            this.executingWork = new ArrayList<>(jobStatus.executingWork);
        }
        ArrayList<JobWorkItem> arrayList2 = jobStatus.pendingWork;
        if (arrayList2 == null || arrayList2.size() <= 0) {
            return;
        }
        this.pendingWork = new ArrayList<>(jobStatus.pendingWork);
    }

    public JobStatus(JobInfo jobInfo, int i, String str, int i2, int i3, String str2, String str3, long j, long j2, long j3, long j4, long j5, Pair<Long, Long> pair, int i4, int i5) {
        this(jobInfo, i, str, i2, i3, str2, str3, 0, 0, j, j2, j3, j4, j5, i4, i5);
        this.mPersistedUtcTimes = pair;
        if (pair == null || !DEBUG) {
            return;
        }
        Slog.i(TAG, "+ restored job with RTC times because of bad boot clock");
    }

    public JobStatus(JobStatus jobStatus, long j, long j2, int i, int i2, long j3, long j4, long j5) {
        this(jobStatus.job, jobStatus.getUid(), jobStatus.getSourcePackageName(), jobStatus.getSourceUserId(), jobStatus.getStandbyBucket(), jobStatus.getNamespace(), jobStatus.getSourceTag(), i, i2, j, j2, j3, j4, j5, jobStatus.getInternalFlags(), jobStatus.mDynamicConstraints);
    }

    public static JobStatus createFromJobInfo(JobInfo jobInfo, int i, String str, int i2, String str2, String str3) {
        long minLatencyMillis;
        long maxExecutionDelayMillis;
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        if (jobInfo.isPeriodic()) {
            long max = Math.max(JobInfo.getMinPeriodMillis(), Math.min(31536000000L, jobInfo.getIntervalMillis()));
            maxExecutionDelayMillis = millis + max;
            minLatencyMillis = maxExecutionDelayMillis - Math.max(JobInfo.getMinFlexMillis(), Math.min(max, jobInfo.getFlexMillis()));
        } else {
            minLatencyMillis = jobInfo.hasEarlyConstraint() ? jobInfo.getMinLatencyMillis() + millis : 0L;
            maxExecutionDelayMillis = jobInfo.hasLateConstraint() ? jobInfo.getMaxExecutionDelayMillis() + millis : NO_LATEST_RUNTIME;
        }
        return new JobStatus(jobInfo, i, str, i2, JobSchedulerService.standbyBucketForPackage(str != null ? str : jobInfo.getService().getPackageName(), i2, millis), str2, str3, 0, 0, minLatencyMillis, maxExecutionDelayMillis, 0L, 0L, 0L, 0, 0);
    }

    private long generateLoggingId(String str, int i) {
        return str == null ? i : (str.hashCode() << 31) | i;
    }

    private static String generateNamespaceHash(String str) {
        String str2 = null;
        if (str == null) {
            return null;
        }
        if (str.trim().isEmpty()) {
            return str;
        }
        ArrayMap<String, String> arrayMap = sNamespaceHashCache;
        synchronized (arrayMap) {
            int indexOfKey = arrayMap.indexOfKey(str);
            if (indexOfKey >= 0) {
                return arrayMap.valueAt(indexOfKey);
            }
            try {
                if (sMessageDigest == null) {
                    sMessageDigest = MessageDigest.getInstance("SHA-256");
                }
                byte[] digest = sMessageDigest.digest(str.getBytes());
                StringBuilder sb = new StringBuilder(digest.length);
                for (byte b : digest) {
                    sb.append(String.format("%02X", Byte.valueOf(b)));
                }
                str2 = sb.toString();
            } catch (Exception e) {
                Slog.wtf(TAG, "Couldn't hash input", e);
            }
            if (str2 == null) {
                return "failed_namespace_hash";
            }
            String intern = str2.intern();
            ArrayMap<String, String> arrayMap2 = sNamespaceHashCache;
            synchronized (arrayMap2) {
                if (arrayMap2.size() >= 128) {
                    arrayMap2.removeAt(new Random().nextInt(128));
                }
                arrayMap2.put(str, intern);
            }
            return intern;
        }
    }

    public void enqueueWorkLocked(JobWorkItem jobWorkItem) {
        if (this.pendingWork == null) {
            this.pendingWork = new ArrayList<>();
        }
        jobWorkItem.setWorkId(this.nextPendingWorkId);
        this.nextPendingWorkId++;
        if (jobWorkItem.getIntent() != null && GrantedUriPermissions.checkGrantFlags(jobWorkItem.getIntent().getFlags())) {
            jobWorkItem.setGrants(GrantedUriPermissions.createFromIntent(jobWorkItem.getIntent(), this.sourceUid, this.sourcePackageName, this.sourceUserId, toShortString()));
        }
        this.pendingWork.add(jobWorkItem);
        updateNetworkBytesLocked();
    }

    public JobWorkItem dequeueWorkLocked() {
        ArrayList<JobWorkItem> arrayList = this.pendingWork;
        if (arrayList == null || arrayList.size() <= 0) {
            return null;
        }
        JobWorkItem remove = this.pendingWork.remove(0);
        if (remove != null) {
            if (this.executingWork == null) {
                this.executingWork = new ArrayList<>();
            }
            this.executingWork.add(remove);
            remove.bumpDeliveryCount();
        }
        return remove;
    }

    public int getWorkCount() {
        ArrayList<JobWorkItem> arrayList = this.pendingWork;
        int size = arrayList == null ? 0 : arrayList.size();
        ArrayList<JobWorkItem> arrayList2 = this.executingWork;
        return size + (arrayList2 != null ? arrayList2.size() : 0);
    }

    public boolean hasWorkLocked() {
        ArrayList<JobWorkItem> arrayList = this.pendingWork;
        return (arrayList != null && arrayList.size() > 0) || hasExecutingWorkLocked();
    }

    public boolean hasExecutingWorkLocked() {
        ArrayList<JobWorkItem> arrayList = this.executingWork;
        return arrayList != null && arrayList.size() > 0;
    }

    private static void ungrantWorkItem(JobWorkItem jobWorkItem) {
        if (jobWorkItem.getGrants() != null) {
            ((GrantedUriPermissions) jobWorkItem.getGrants()).revoke();
        }
    }

    public boolean completeWorkLocked(int i) {
        ArrayList<JobWorkItem> arrayList = this.executingWork;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                JobWorkItem jobWorkItem = this.executingWork.get(i2);
                if (jobWorkItem.getWorkId() == i) {
                    this.executingWork.remove(i2);
                    ungrantWorkItem(jobWorkItem);
                    updateNetworkBytesLocked();
                    return true;
                }
            }
        }
        return false;
    }

    private static void ungrantWorkList(ArrayList<JobWorkItem> arrayList) {
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ungrantWorkItem(arrayList.get(i));
            }
        }
    }

    public void stopTrackingJobLocked(JobStatus jobStatus) {
        if (jobStatus != null) {
            ArrayList<JobWorkItem> arrayList = this.executingWork;
            if (arrayList != null && arrayList.size() > 0) {
                jobStatus.pendingWork = this.executingWork;
            }
            if (jobStatus.pendingWork == null) {
                jobStatus.pendingWork = this.pendingWork;
            } else {
                ArrayList<JobWorkItem> arrayList2 = this.pendingWork;
                if (arrayList2 != null && arrayList2.size() > 0) {
                    jobStatus.pendingWork.addAll(this.pendingWork);
                }
            }
            this.pendingWork = null;
            this.executingWork = null;
            jobStatus.nextPendingWorkId = this.nextPendingWorkId;
            jobStatus.updateNetworkBytesLocked();
        } else {
            ungrantWorkList(this.pendingWork);
            this.pendingWork = null;
            ungrantWorkList(this.executingWork);
            this.executingWork = null;
        }
        updateNetworkBytesLocked();
    }

    public void prepareLocked() {
        if (this.prepared) {
            Slog.wtf(TAG, "Already prepared: " + this);
            return;
        }
        this.prepared = true;
        this.unpreparedPoint = null;
        ClipData clipData = this.job.getClipData();
        if (clipData != null) {
            this.uriPerms = GrantedUriPermissions.createFromClip(clipData, this.sourceUid, this.sourcePackageName, this.sourceUserId, this.job.getClipGrantFlags(), toShortString());
        }
    }

    public void unprepareLocked() {
        if (!this.prepared) {
            Slog.wtf(TAG, "Hasn't been prepared: " + this);
            Throwable th = this.unpreparedPoint;
            if (th != null) {
                Slog.e(TAG, "Was already unprepared at ", th);
                return;
            }
            return;
        }
        this.prepared = false;
        this.unpreparedPoint = new Throwable().fillInStackTrace();
        GrantedUriPermissions grantedUriPermissions = this.uriPerms;
        if (grantedUriPermissions != null) {
            grantedUriPermissions.revoke();
            this.uriPerms = null;
        }
    }

    public boolean isPreparedLocked() {
        return this.prepared;
    }

    public JobInfo getJob() {
        return this.job;
    }

    public int getJobId() {
        return this.job.getId();
    }

    public long getLoggingJobId() {
        return this.mLoggingJobId;
    }

    public void printUniqueId(PrintWriter printWriter) {
        String str = this.mNamespace;
        if (str != null) {
            printWriter.print(str);
            printWriter.print(":");
        } else {
            printWriter.print("#");
        }
        UserHandle.formatUid(printWriter, this.callingUid);
        printWriter.print(SliceClientPermissions.SliceAuthority.DELIMITER);
        printWriter.print(this.job.getId());
    }

    public int getNumFailures() {
        return this.numFailures;
    }

    public int getNumSystemStops() {
        return this.mNumSystemStops;
    }

    public int getNumPreviousAttempts() {
        return this.numFailures + this.mNumSystemStops;
    }

    public ComponentName getServiceComponent() {
        return this.job.getService();
    }

    public String getSourcePackageName() {
        return this.sourcePackageName;
    }

    public int getSourceUid() {
        return this.sourceUid;
    }

    public int getSourceUserId() {
        return this.sourceUserId;
    }

    public int getUserId() {
        return UserHandle.getUserId(this.callingUid);
    }

    private boolean shouldBlameSourceForTimeout() {
        return UserHandle.isCore(this.callingUid);
    }

    public String getTimeoutBlamePackageName() {
        if (shouldBlameSourceForTimeout()) {
            return this.sourcePackageName;
        }
        return getServiceComponent().getPackageName();
    }

    public int getTimeoutBlameUid() {
        if (shouldBlameSourceForTimeout()) {
            return this.sourceUid;
        }
        return this.callingUid;
    }

    public int getTimeoutBlameUserId() {
        if (shouldBlameSourceForTimeout()) {
            return this.sourceUserId;
        }
        return UserHandle.getUserId(this.callingUid);
    }

    public int getEffectiveStandbyBucket() {
        String str;
        boolean isAppConsideredBuggy = ((JobSchedulerInternal) LocalServices.getService(JobSchedulerInternal.class)).isAppConsideredBuggy(getUserId(), getServiceComponent().getPackageName(), getTimeoutBlameUserId(), getTimeoutBlamePackageName());
        int standbyBucket = getStandbyBucket();
        if (standbyBucket != 6) {
            if (this.uidActive || getJob().isExemptedFromAppStandby()) {
                return 0;
            }
            if (standbyBucket != 5 && standbyBucket != 4 && this.mHasMediaBackupExemption) {
                return Math.max(isAppConsideredBuggy ? 1 : 0, Math.min(1, standbyBucket));
            }
            return Math.max(isAppConsideredBuggy ? 1 : 0, standbyBucket);
        }
        if (isAppConsideredBuggy) {
            if (getServiceComponent().getPackageName().equals(this.sourcePackageName)) {
                str = this.sourcePackageName;
            } else {
                str = getServiceComponent().getPackageName() + SliceClientPermissions.SliceAuthority.DELIMITER + this.sourcePackageName;
            }
            Slog.w(TAG, "Exempted app " + str + " considered buggy");
        }
        return standbyBucket;
    }

    public int getStandbyBucket() {
        return this.standbyBucket;
    }

    public void setStandbyBucket(int i) {
        if (i == 5) {
            addDynamicConstraints(DYNAMIC_RESTRICTED_CONSTRAINTS);
        } else if (this.standbyBucket == 5) {
            removeDynamicConstraints(DYNAMIC_RESTRICTED_CONSTRAINTS);
        }
        this.standbyBucket = i;
        this.mLoggedBucketMismatch = false;
    }

    public void maybeLogBucketMismatch() {
        if (this.mLoggedBucketMismatch) {
            return;
        }
        Slog.wtf(TAG, "App " + getSourcePackageName() + " became active but still in NEVER bucket");
        this.mLoggedBucketMismatch = true;
    }

    public long getWhenStandbyDeferred() {
        return this.whenStandbyDeferred;
    }

    public void setWhenStandbyDeferred(long j) {
        this.whenStandbyDeferred = j;
    }

    public long getFirstForceBatchedTimeElapsed() {
        return this.mFirstForceBatchedTimeElapsed;
    }

    public void setFirstForceBatchedTimeElapsed(long j) {
        this.mFirstForceBatchedTimeElapsed = j;
    }

    public boolean updateMediaBackupExemptionStatus() {
        boolean z = this.mHasExemptedMediaUrisOnly && !this.job.hasLateConstraint() && this.job.getRequiredNetwork() != null && getEffectivePriority() >= 300 && this.sourcePackageName.equals(((JobSchedulerInternal) LocalServices.getService(JobSchedulerInternal.class)).getCloudMediaProviderPackage(this.sourceUserId));
        if (this.mHasMediaBackupExemption == z) {
            return false;
        }
        this.mHasMediaBackupExemption = z;
        return true;
    }

    public String getNamespace() {
        return this.mNamespace;
    }

    public String getNamespaceHash() {
        return this.mNamespaceHash;
    }

    public String getSourceTag() {
        return this.sourceTag;
    }

    public int getUid() {
        return this.callingUid;
    }

    public String getBatteryName() {
        return this.batteryName;
    }

    public String getTag() {
        return this.tag;
    }

    public int getBias() {
        return this.job.getBias();
    }

    public int getEffectivePriority() {
        int min = Math.min((getInternalFlags() & 2) != 0 || (this.job.isUserInitiated() && (getInternalFlags() & 4) != 0) ? 400 : SensorPrivacyService.REMINDER_DIALOG_DELAY_MILLIS, this.job.getPriority());
        if (this.numFailures < 2 || shouldTreatAsUserInitiatedJob()) {
            return min;
        }
        if (isRequestedExpeditedJob()) {
            return ContextHubStatsLog.CONTEXT_HUB_LOADED_NANOAPP_SNAPSHOT_REPORTED;
        }
        int i = this.numFailures / 2;
        if (i == 1) {
            return Math.min(300, min);
        }
        if (i != 2) {
            return 100;
        }
        return Math.min(UsbDescriptor.USB_CONTROL_TRANSFER_TIMEOUT_MS, min);
    }

    public int getFlags() {
        return this.job.getFlags();
    }

    public int getInternalFlags() {
        return this.mInternalFlags;
    }

    public void addInternalFlags(int i) {
        this.mInternalFlags = i | this.mInternalFlags;
    }

    public void removeInternalFlags(int i) {
        this.mInternalFlags = (~i) & this.mInternalFlags;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPreferredConstraintFlags() {
        return this.mPreferredConstraints;
    }

    public int getSatisfiedConstraintFlags() {
        return this.satisfiedConstraints;
    }

    public void maybeAddForegroundExemption(Predicate<Integer> predicate) {
        if (this.job.hasEarlyConstraint() || this.job.hasLateConstraint() || (this.mInternalFlags & 1) != 0 || !predicate.test(Integer.valueOf(getSourceUid()))) {
            return;
        }
        addInternalFlags(1);
    }

    private void updateNetworkBytesLocked() {
        long estimatedNetworkDownloadBytes = this.job.getEstimatedNetworkDownloadBytes();
        this.mTotalNetworkDownloadBytes = estimatedNetworkDownloadBytes;
        if (estimatedNetworkDownloadBytes < 0) {
            this.mTotalNetworkDownloadBytes = -1L;
        }
        long estimatedNetworkUploadBytes = this.job.getEstimatedNetworkUploadBytes();
        this.mTotalNetworkUploadBytes = estimatedNetworkUploadBytes;
        if (estimatedNetworkUploadBytes < 0) {
            this.mTotalNetworkUploadBytes = -1L;
        }
        this.mMinimumNetworkChunkBytes = this.job.getMinimumNetworkChunkBytes();
        if (this.pendingWork != null) {
            for (int i = 0; i < this.pendingWork.size(); i++) {
                long estimatedNetworkDownloadBytes2 = this.pendingWork.get(i).getEstimatedNetworkDownloadBytes();
                if (estimatedNetworkDownloadBytes2 != -1 && estimatedNetworkDownloadBytes2 > 0) {
                    long j = this.mTotalNetworkDownloadBytes;
                    if (j != -1) {
                        this.mTotalNetworkDownloadBytes = j + estimatedNetworkDownloadBytes2;
                    } else {
                        this.mTotalNetworkDownloadBytes = estimatedNetworkDownloadBytes2;
                    }
                }
                long estimatedNetworkUploadBytes2 = this.pendingWork.get(i).getEstimatedNetworkUploadBytes();
                if (estimatedNetworkUploadBytes2 != -1 && estimatedNetworkUploadBytes2 > 0) {
                    long j2 = this.mTotalNetworkUploadBytes;
                    if (j2 != -1) {
                        this.mTotalNetworkUploadBytes = j2 + estimatedNetworkUploadBytes2;
                    } else {
                        this.mTotalNetworkUploadBytes = estimatedNetworkUploadBytes2;
                    }
                }
                long minimumNetworkChunkBytes = this.pendingWork.get(i).getMinimumNetworkChunkBytes();
                long j3 = this.mMinimumNetworkChunkBytes;
                if (j3 == -1) {
                    this.mMinimumNetworkChunkBytes = minimumNetworkChunkBytes;
                } else if (minimumNetworkChunkBytes != -1) {
                    this.mMinimumNetworkChunkBytes = Math.min(j3, minimumNetworkChunkBytes);
                }
            }
        }
    }

    public long getEstimatedNetworkDownloadBytes() {
        return this.mTotalNetworkDownloadBytes;
    }

    public long getEstimatedNetworkUploadBytes() {
        return this.mTotalNetworkUploadBytes;
    }

    public long getMinimumNetworkChunkBytes() {
        return this.mMinimumNetworkChunkBytes;
    }

    public boolean hasConnectivityConstraint() {
        return (this.requiredConstraints & 268435456) != 0;
    }

    public boolean hasChargingConstraint() {
        return hasConstraint(1);
    }

    public boolean hasBatteryNotLowConstraint() {
        return hasConstraint(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPowerConstraint() {
        return hasConstraint(3);
    }

    public boolean hasStorageNotLowConstraint() {
        return hasConstraint(8);
    }

    public boolean hasTimingDelayConstraint() {
        return hasConstraint(Integer.MIN_VALUE);
    }

    public boolean hasDeadlineConstraint() {
        return hasConstraint(1073741824);
    }

    public boolean hasIdleConstraint() {
        return hasConstraint(4);
    }

    public boolean hasContentTriggerConstraint() {
        return (this.requiredConstraints & 67108864) != 0;
    }

    public boolean hasFlexibilityConstraint() {
        return (this.requiredConstraints & 2097152) != 0;
    }

    public int getNumRequiredFlexibleConstraints() {
        return this.mNumRequiredFlexibleConstraints - this.mNumDroppedFlexibleConstraints;
    }

    public int getNumDroppedFlexibleConstraints() {
        return this.mNumDroppedFlexibleConstraints;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasConstraint(int i) {
        return ((this.requiredConstraints & i) == 0 && (this.mDynamicConstraints & i) == 0) ? false : true;
    }

    public long getTriggerContentUpdateDelay() {
        long triggerContentUpdateDelay = this.job.getTriggerContentUpdateDelay();
        return triggerContentUpdateDelay < 0 ? DEFAULT_TRIGGER_UPDATE_DELAY : Math.max(triggerContentUpdateDelay, 500L);
    }

    public long getTriggerContentMaxDelay() {
        long triggerContentMaxDelay = this.job.getTriggerContentMaxDelay();
        if (triggerContentMaxDelay < 0) {
            return 120000L;
        }
        return Math.max(triggerContentMaxDelay, 1000L);
    }

    public boolean isPersisted() {
        return this.job.isPersisted();
    }

    public long getCumulativeExecutionTimeMs() {
        return this.mCumulativeExecutionTimeMs;
    }

    public void incrementCumulativeExecutionTime(long j) {
        this.mCumulativeExecutionTimeMs += j;
    }

    public long getEarliestRunTime() {
        return this.earliestRunTimeElapsedMillis;
    }

    public long getLatestRunTimeElapsed() {
        return this.latestRunTimeElapsedMillis;
    }

    public long getOriginalLatestRunTimeElapsed() {
        return this.mOriginalLatestRunTimeElapsedMillis;
    }

    public void setOriginalLatestRunTimeElapsed(long j) {
        this.mOriginalLatestRunTimeElapsedMillis = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHasAccessToUnmetered(boolean z) {
        this.mHasAccessToUnmetered = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getHasAccessToUnmetered() {
        return this.mHasAccessToUnmetered;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getPreferUnmetered() {
        return this.mPreferUnmetered;
    }

    public int getStopReason() {
        return this.mReasonReadyToUnready;
    }

    public float getFractionRunTime() {
        long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
        long j = this.earliestRunTimeElapsedMillis;
        if (j == 0 && this.latestRunTimeElapsedMillis == NO_LATEST_RUNTIME) {
            return 1.0f;
        }
        if (j == 0) {
            return millis >= this.latestRunTimeElapsedMillis ? 1.0f : 0.0f;
        }
        long j2 = this.latestRunTimeElapsedMillis;
        if (j2 == NO_LATEST_RUNTIME) {
            return millis >= j ? 1.0f : 0.0f;
        }
        if (millis <= j) {
            return 0.0f;
        }
        if (millis >= j2) {
            return 1.0f;
        }
        return ((float) (millis - j)) / ((float) (j2 - j));
    }

    public Pair<Long, Long> getPersistedUtcTimes() {
        return this.mPersistedUtcTimes;
    }

    public void clearPersistedUtcTimes() {
        this.mPersistedUtcTimes = null;
    }

    public boolean isRequestedExpeditedJob() {
        return (getFlags() & 16) != 0;
    }

    public boolean shouldTreatAsExpeditedJob() {
        return this.mExpeditedQuotaApproved && this.mExpeditedTareApproved && isRequestedExpeditedJob();
    }

    public boolean shouldTreatAsUserInitiatedJob() {
        return getJob().isUserInitiated() && (getInternalFlags() & 2) == 0 && (getInternalFlags() & 4) == 0;
    }

    public UserVisibleJobSummary getUserVisibleJobSummary() {
        if (this.mUserVisibleJobSummary == null) {
            this.mUserVisibleJobSummary = new UserVisibleJobSummary(this.callingUid, getServiceComponent().getPackageName(), getSourceUserId(), getSourcePackageName(), getNamespace(), getJobId());
        }
        return this.mUserVisibleJobSummary;
    }

    public boolean isUserVisibleJob() {
        return shouldTreatAsUserInitiatedJob() || this.startedAsUserInitiatedJob;
    }

    public boolean canRunInDoze() {
        if (this.appHasDozeExemption || (getFlags() & 1) != 0 || shouldTreatAsUserInitiatedJob()) {
            return true;
        }
        return (shouldTreatAsExpeditedJob() || this.startedAsExpeditedJob) && (this.mDynamicConstraints & 33554432) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canRunInBatterySaver() {
        if ((getInternalFlags() & 1) != 0 || shouldTreatAsUserInitiatedJob()) {
            return true;
        }
        return (shouldTreatAsExpeditedJob() || this.startedAsExpeditedJob) && (this.mDynamicConstraints & 4194304) == 0;
    }

    public boolean isUserBgRestricted() {
        return this.mIsUserBgRestricted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setChargingConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(1, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setBatteryNotLowConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(2, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setStorageNotLowConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(8, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setPrefetchConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(8388608, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setTimingDelayConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(Integer.MIN_VALUE, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setDeadlineConstraintSatisfied(long j, boolean z) {
        boolean z2 = false;
        if (!setConstraintSatisfied(1073741824, j, z)) {
            return false;
        }
        if (!this.job.isPeriodic() && hasDeadlineConstraint() && z) {
            z2 = true;
        }
        this.mReadyDeadlineSatisfied = z2;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setIdleConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(4, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setConnectivityConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(268435456, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setContentTriggerConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(67108864, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setDeviceNotDozingConstraintSatisfied(long j, boolean z, boolean z2) {
        this.appHasDozeExemption = z2;
        if (!setConstraintSatisfied(33554432, j, z)) {
            return false;
        }
        this.mReadyNotDozing = z || canRunInDoze();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setBackgroundNotRestrictedConstraintSatisfied(long j, boolean z, boolean z2) {
        this.mIsUserBgRestricted = z2;
        if (!setConstraintSatisfied(4194304, j, z)) {
            return false;
        }
        this.mReadyNotRestrictedInBg = z;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setQuotaConstraintSatisfied(long j, boolean z) {
        if (!setConstraintSatisfied(16777216, j, z)) {
            return false;
        }
        this.mReadyWithinQuota = z;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setTareWealthConstraintSatisfied(long j, boolean z) {
        if (!setConstraintSatisfied(134217728, j, z)) {
            return false;
        }
        this.mReadyTareWealth = z;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setFlexibilityConstraintSatisfied(long j, boolean z) {
        return setConstraintSatisfied(2097152, j, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setExpeditedJobQuotaApproved(long j, boolean z) {
        if (this.mExpeditedQuotaApproved == z) {
            return false;
        }
        boolean z2 = !z && isReady();
        this.mExpeditedQuotaApproved = z;
        updateExpeditedDependencies();
        boolean isReady = isReady();
        if (z2 && !isReady) {
            this.mReasonReadyToUnready = 10;
        } else if (!z2 && isReady) {
            this.mReasonReadyToUnready = 0;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setExpeditedJobTareApproved(long j, boolean z) {
        if (this.mExpeditedTareApproved == z) {
            return false;
        }
        boolean z2 = !z && isReady();
        this.mExpeditedTareApproved = z;
        updateExpeditedDependencies();
        boolean isReady = isReady();
        if (z2 && !isReady) {
            this.mReasonReadyToUnready = 10;
        } else if (!z2 && isReady) {
            this.mReasonReadyToUnready = 0;
        }
        return true;
    }

    private void updateExpeditedDependencies() {
        this.mReadyNotDozing = isConstraintSatisfied(33554432) || canRunInDoze();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setUidActive(boolean z) {
        if (z == this.uidActive) {
            return false;
        }
        this.uidActive = z;
        return true;
    }

    boolean setConstraintSatisfied(int i, long j, boolean z) {
        if (((this.satisfiedConstraints & i) != 0) == z) {
            return false;
        }
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            sb.append("Constraint ");
            sb.append(i);
            sb.append(" is ");
            sb.append(!z ? "NOT " : "");
            sb.append("satisfied for ");
            sb.append(toShortString());
            Slog.v(TAG, sb.toString());
        }
        boolean z2 = !z && isReady();
        int i2 = (this.satisfiedConstraints & (~i)) | (z ? i : 0);
        this.satisfiedConstraints = i2;
        this.mSatisfiedConstraintsOfInterest = CONSTRAINTS_OF_INTEREST & i2;
        int i3 = this.mDynamicConstraints;
        this.mReadyDynamicSatisfied = i3 != 0 && i3 == (i2 & i3);
        long[] jArr = this.mConstraintUpdatedTimesElapsed;
        int i4 = this.mConstraintChangeHistoryIndex;
        jArr[i4] = j;
        this.mConstraintStatusHistory[i4] = i2;
        this.mConstraintChangeHistoryIndex = (i4 + 1) % 10;
        boolean readinessStatusWithConstraint = readinessStatusWithConstraint(i, z);
        if (z2 && !readinessStatusWithConstraint) {
            this.mReasonReadyToUnready = constraintToStopReason(i);
        } else if (!z2 && readinessStatusWithConstraint) {
            this.mReasonReadyToUnready = 0;
        }
        return true;
    }

    private int constraintToStopReason(int i) {
        if (i == 1) {
            return (this.requiredConstraints & i) != 0 ? 6 : 12;
        }
        if (i == 2) {
            return (this.requiredConstraints & i) != 0 ? 5 : 12;
        }
        if (i == 4) {
            return (this.requiredConstraints & i) != 0 ? 8 : 12;
        }
        if (i == 8) {
            return 9;
        }
        if (i == 4194304) {
            return this.mIsUserBgRestricted ? 11 : 4;
        }
        if (i == 8388608) {
            return 15;
        }
        if (i == 16777216) {
            return 10;
        }
        if (i == 33554432) {
            return 4;
        }
        if (i == 134217728) {
            return 10;
        }
        if (i == 268435456) {
            return 7;
        }
        Slog.wtf(TAG, "Unsupported constraint (" + i + ") --stop reason mapping");
        return 0;
    }

    public int getPendingJobReason() {
        int i = ~this.satisfiedConstraints;
        int i2 = this.requiredConstraints;
        int i3 = i & (this.mDynamicConstraints | i2 | IMPLICIT_CONSTRAINTS);
        if ((4194304 & i3) != 0) {
            return this.mIsUserBgRestricted ? 3 : 12;
        }
        if ((i3 & 2) != 0) {
            return (i2 & 2) != 0 ? 4 : 2;
        }
        if ((i3 & 1) != 0) {
            return (i2 & 1) != 0 ? 5 : 2;
        }
        if ((268435456 & i3) != 0) {
            return 6;
        }
        if ((67108864 & i3) != 0) {
            return 7;
        }
        if ((33554432 & i3) != 0) {
            return 12;
        }
        if ((2097152 & i3) != 0) {
            return 13;
        }
        if ((i3 & 4) != 0) {
            return (i2 & 4) != 0 ? 8 : 2;
        }
        if ((8388608 & i3) != 0) {
            return 10;
        }
        if ((i3 & 8) != 0) {
            return 11;
        }
        if ((134217728 & i3) != 0) {
            return 14;
        }
        if ((Integer.MIN_VALUE & i3) != 0) {
            return 9;
        }
        if ((i3 & 16777216) != 0) {
            return 14;
        }
        if (getEffectiveStandbyBucket() == 4) {
            Slog.wtf(TAG, "App in NEVER bucket querying pending job reason");
            return 15;
        }
        if (this.serviceProcessName != null) {
            return 1;
        }
        if (isReady()) {
            return 0;
        }
        Slog.wtf(TAG, "Unknown reason job isn't ready");
        return 0;
    }

    public boolean isConstraintSatisfied(int i) {
        return (this.satisfiedConstraints & i) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isExpeditedQuotaApproved() {
        return this.mExpeditedQuotaApproved;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean clearTrackingController(int i) {
        int i2 = this.trackingControllers;
        if ((i2 & i) == 0) {
            return false;
        }
        this.trackingControllers = (~i) & i2;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTrackingController(int i) {
        this.trackingControllers = i | this.trackingControllers;
    }

    public void adjustNumRequiredFlexibleConstraints(int i) {
        this.mNumDroppedFlexibleConstraints = Math.max(0, Math.min(this.mNumRequiredFlexibleConstraints, this.mNumDroppedFlexibleConstraints - i));
    }

    public void disallowRunInBatterySaverAndDoze() {
        addDynamicConstraints(DYNAMIC_EXPEDITED_DEFERRAL_CONSTRAINTS);
    }

    @VisibleForTesting
    public void addDynamicConstraints(int i) {
        if ((16777216 & i) != 0) {
            Slog.wtf(TAG, "Tried to set quota as a dynamic constraint");
            i &= -16777217;
        }
        if ((134217728 & i) != 0) {
            Slog.wtf(TAG, "Tried to set TARE as a dynamic constraint");
            i &= -134217729;
        }
        if (!hasConnectivityConstraint()) {
            i &= -268435457;
        }
        if (!hasContentTriggerConstraint()) {
            i &= -67108865;
        }
        int i2 = i | this.mDynamicConstraints;
        this.mDynamicConstraints = i2;
        this.mReadyDynamicSatisfied = i2 != 0 && i2 == (this.satisfiedConstraints & i2);
    }

    private void removeDynamicConstraints(int i) {
        int i2 = (~i) & this.mDynamicConstraints;
        this.mDynamicConstraints = i2;
        this.mReadyDynamicSatisfied = i2 != 0 && i2 == (this.satisfiedConstraints & i2);
    }

    public long getLastSuccessfulRunTime() {
        return this.mLastSuccessfulRunTime;
    }

    public long getLastFailedRunTime() {
        return this.mLastFailedRunTime;
    }

    public boolean isReady() {
        return isReady(this.mSatisfiedConstraintsOfInterest);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean wouldBeReadyWithConstraint(int i) {
        return readinessStatusWithConstraint(i, true);
    }

    @VisibleForTesting
    boolean readinessStatusWithConstraint(int i, boolean z) {
        boolean z2;
        int i2 = this.mSatisfiedConstraintsOfInterest;
        if (i == 4194304) {
            z2 = this.mReadyNotRestrictedInBg;
            this.mReadyNotRestrictedInBg = z;
        } else if (i == 16777216) {
            z2 = this.mReadyWithinQuota;
            this.mReadyWithinQuota = z;
        } else if (i == 33554432) {
            z2 = this.mReadyNotDozing;
            this.mReadyNotDozing = z;
        } else if (i == 134217728) {
            z2 = this.mReadyTareWealth;
            this.mReadyTareWealth = z;
        } else if (i == 1073741824) {
            z2 = this.mReadyDeadlineSatisfied;
            this.mReadyDeadlineSatisfied = z;
        } else {
            i2 = z ? i2 | i : (~i) & i2;
            int i3 = this.mDynamicConstraints;
            this.mReadyDynamicSatisfied = i3 != 0 && i3 == (i2 & i3);
            z2 = false;
        }
        if (i != 2097152) {
            i2 |= 2097152;
        }
        boolean isReady = isReady(i2);
        if (i == 4194304) {
            this.mReadyNotRestrictedInBg = z2;
        } else if (i == 16777216) {
            this.mReadyWithinQuota = z2;
        } else if (i == 33554432) {
            this.mReadyNotDozing = z2;
        } else if (i == 134217728) {
            this.mReadyTareWealth = z2;
        } else if (i == 1073741824) {
            this.mReadyDeadlineSatisfied = z2;
        } else {
            int i4 = this.mDynamicConstraints;
            this.mReadyDynamicSatisfied = i4 != 0 && i4 == (this.satisfiedConstraints & i4);
        }
        return isReady;
    }

    private boolean isReady(int i) {
        if (((this.mReadyWithinQuota && this.mReadyTareWealth) || this.mReadyDynamicSatisfied || shouldTreatAsExpeditedJob()) && getEffectiveStandbyBucket() != 4 && mJobStatusExt.isReady(this.mReadyDeadlineSatisfied, this.mReadyNotDozing, this, this.requiredConstraints, i, 268435456, CONSTRAINTS_OF_INTEREST, SOFT_OVERRIDE_CONSTRAINTS) && this.mReadyNotDozing && this.mReadyNotRestrictedInBg && this.serviceProcessName != null) {
            return this.mReadyDeadlineSatisfied || isConstraintsSatisfied(i);
        }
        return false;
    }

    public boolean areDynamicConstraintsSatisfied() {
        return this.mReadyDynamicSatisfied;
    }

    public boolean isConstraintsSatisfied() {
        return isConstraintsSatisfied(this.mSatisfiedConstraintsOfInterest);
    }

    private boolean isConstraintsSatisfied(int i) {
        int i2 = this.overrideState;
        if (i2 == 3) {
            return true;
        }
        if (i2 == 2) {
            i |= this.requiredConstraints & SOFT_OVERRIDE_CONSTRAINTS;
        }
        int i3 = this.mRequiredConstraintsOfInterest;
        return (i & i3) == i3;
    }

    public boolean matches(int i, String str, int i2) {
        return this.job.getId() == i2 && this.callingUid == i && Objects.equals(this.mNamespace, str);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("JobStatus{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mNamespace != null) {
            sb.append(" ");
            sb.append(this.mNamespace);
            sb.append(":");
        } else {
            sb.append(" #");
        }
        UserHandle.formatUid(sb, this.callingUid);
        sb.append(SliceClientPermissions.SliceAuthority.DELIMITER);
        sb.append(this.job.getId());
        sb.append(' ');
        sb.append(this.batteryName);
        sb.append(" u=");
        sb.append(getUserId());
        sb.append(" s=");
        sb.append(getSourceUid());
        if (this.earliestRunTimeElapsedMillis != 0 || this.latestRunTimeElapsedMillis != NO_LATEST_RUNTIME) {
            long millis = JobSchedulerService.sElapsedRealtimeClock.millis();
            sb.append(" TIME=");
            formatRunTime(sb, this.earliestRunTimeElapsedMillis, 0L, millis);
            sb.append(":");
            formatRunTime(sb, this.latestRunTimeElapsedMillis, NO_LATEST_RUNTIME, millis);
        }
        if (this.job.getRequiredNetwork() != null) {
            sb.append(" NET");
        }
        if (this.job.isRequireCharging()) {
            sb.append(" CHARGING");
        }
        if (this.job.isRequireBatteryNotLow()) {
            sb.append(" BATNOTLOW");
        }
        if (this.job.isRequireStorageNotLow()) {
            sb.append(" STORENOTLOW");
        }
        if (this.job.isRequireDeviceIdle()) {
            sb.append(" IDLE");
        }
        if (this.job.isPeriodic()) {
            sb.append(" PERIODIC");
        }
        if (this.job.isPersisted()) {
            sb.append(" PERSISTED");
        }
        if ((this.satisfiedConstraints & 33554432) == 0) {
            sb.append(" WAIT:DEV_NOT_DOZING");
        }
        if (this.job.getTriggerContentUris() != null) {
            sb.append(" URIS=");
            sb.append(Arrays.toString(this.job.getTriggerContentUris()));
        }
        if (this.numFailures != 0) {
            sb.append(" failures=");
            sb.append(this.numFailures);
        }
        if (this.mNumSystemStops != 0) {
            sb.append(" system stops=");
            sb.append(this.mNumSystemStops);
        }
        if (isReady()) {
            sb.append(" READY");
        } else {
            sb.append(" satisfied:0x");
            sb.append(Integer.toHexString(this.satisfiedConstraints));
            int i = this.mRequiredConstraintsOfInterest | IMPLICIT_CONSTRAINTS;
            sb.append(" unsatisfied:0x");
            sb.append(Integer.toHexString((this.satisfiedConstraints & i) ^ i));
        }
        sb.append("}");
        return sb.toString();
    }

    private void formatRunTime(PrintWriter printWriter, long j, long j2, long j3) {
        if (j == j2) {
            printWriter.print("none");
        } else {
            TimeUtils.formatDuration(j - j3, printWriter);
        }
    }

    private void formatRunTime(StringBuilder sb, long j, long j2, long j3) {
        if (j == j2) {
            sb.append("none");
        } else {
            TimeUtils.formatDuration(j - j3, sb);
        }
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mNamespace != null) {
            sb.append(" {");
            sb.append(this.mNamespace);
            sb.append("}");
        }
        sb.append(" #");
        UserHandle.formatUid(sb, this.callingUid);
        sb.append(SliceClientPermissions.SliceAuthority.DELIMITER);
        sb.append(this.job.getId());
        sb.append(' ');
        sb.append(this.batteryName);
        return sb.toString();
    }

    public String toShortStringExceptUniqueId() {
        return Integer.toHexString(System.identityHashCode(this)) + ' ' + this.batteryName;
    }

    public void writeToShortProto(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, this.callingUid);
        protoOutputStream.write(1120986464258L, this.job.getId());
        protoOutputStream.write(1138166333443L, this.batteryName);
        protoOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void dumpConstraints(PrintWriter printWriter, int i) {
        if ((i & 1) != 0) {
            printWriter.print(" CHARGING");
        }
        if ((i & 2) != 0) {
            printWriter.print(" BATTERY_NOT_LOW");
        }
        if ((i & 8) != 0) {
            printWriter.print(" STORAGE_NOT_LOW");
        }
        if ((Integer.MIN_VALUE & i) != 0) {
            printWriter.print(" TIMING_DELAY");
        }
        if ((1073741824 & i) != 0) {
            printWriter.print(" DEADLINE");
        }
        if ((i & 4) != 0) {
            printWriter.print(" IDLE");
        }
        if ((268435456 & i) != 0) {
            printWriter.print(" CONNECTIVITY");
        }
        if ((2097152 & i) != 0) {
            printWriter.print(" FLEXIBILITY");
        }
        if ((67108864 & i) != 0) {
            printWriter.print(" CONTENT_TRIGGER");
        }
        if ((33554432 & i) != 0) {
            printWriter.print(" DEVICE_NOT_DOZING");
        }
        if ((4194304 & i) != 0) {
            printWriter.print(" BACKGROUND_NOT_RESTRICTED");
        }
        if ((8388608 & i) != 0) {
            printWriter.print(" PREFETCH");
        }
        if ((134217728 & i) != 0) {
            printWriter.print(" TARE_WEALTH");
        }
        if ((16777216 & i) != 0) {
            printWriter.print(" WITHIN_QUOTA");
        }
        mJobStatusExt.dumpConstraints(printWriter, i);
        if (i != 0) {
            printWriter.print(" [0x");
            printWriter.print(Integer.toHexString(i));
            printWriter.print("]");
        }
    }

    void dumpConstraints(ProtoOutputStream protoOutputStream, long j, int i) {
        if ((i & 1) != 0) {
            protoOutputStream.write(j, 1);
        }
        if ((i & 2) != 0) {
            protoOutputStream.write(j, 2);
        }
        if ((i & 8) != 0) {
            protoOutputStream.write(j, 3);
        }
        if ((Integer.MIN_VALUE & i) != 0) {
            protoOutputStream.write(j, 4);
        }
        if ((1073741824 & i) != 0) {
            protoOutputStream.write(j, 5);
        }
        if ((i & 4) != 0) {
            protoOutputStream.write(j, 6);
        }
        if ((268435456 & i) != 0) {
            protoOutputStream.write(j, 7);
        }
        if ((67108864 & i) != 0) {
            protoOutputStream.write(j, 8);
        }
        if ((33554432 & i) != 0) {
            protoOutputStream.write(j, 9);
        }
        if ((16777216 & i) != 0) {
            protoOutputStream.write(j, 10);
        }
        if ((4194304 & i) != 0) {
            protoOutputStream.write(j, 11);
        }
    }

    private void dumpJobWorkItem(IndentingPrintWriter indentingPrintWriter, JobWorkItem jobWorkItem, int i) {
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("#");
        indentingPrintWriter.print(i);
        indentingPrintWriter.print(": #");
        indentingPrintWriter.print(jobWorkItem.getWorkId());
        indentingPrintWriter.print(" ");
        indentingPrintWriter.print(jobWorkItem.getDeliveryCount());
        indentingPrintWriter.print("x ");
        indentingPrintWriter.println(jobWorkItem.getIntent());
        if (jobWorkItem.getGrants() != null) {
            indentingPrintWriter.println("URI grants:");
            indentingPrintWriter.increaseIndent();
            ((GrantedUriPermissions) jobWorkItem.getGrants()).dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
    }

    private void dumpJobWorkItem(ProtoOutputStream protoOutputStream, long j, JobWorkItem jobWorkItem) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1120986464257L, jobWorkItem.getWorkId());
        protoOutputStream.write(1120986464258L, jobWorkItem.getDeliveryCount());
        if (jobWorkItem.getIntent() != null) {
            jobWorkItem.getIntent().dumpDebug(protoOutputStream, 1146756268035L);
        }
        Object grants = jobWorkItem.getGrants();
        if (grants != null) {
            ((GrantedUriPermissions) grants).dump(protoOutputStream, 1146756268036L);
        }
        protoOutputStream.end(start);
    }

    String getBucketName() {
        return bucketName(this.standbyBucket);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String bucketName(int i) {
        switch (i) {
            case 0:
                return "ACTIVE";
            case 1:
                return "WORKING_SET";
            case 2:
                return "FREQUENT";
            case 3:
                return "RARE";
            case 4:
                return "NEVER";
            case 5:
                return "RESTRICTED";
            case 6:
                return "EXEMPTED";
            default:
                return "Unknown: " + i;
        }
    }

    @NeverCompile
    public void dump(IndentingPrintWriter indentingPrintWriter, boolean z, long j) {
        UserHandle.formatUid(indentingPrintWriter, this.callingUid);
        indentingPrintWriter.print(" tag=");
        indentingPrintWriter.println(this.tag);
        indentingPrintWriter.print("Source: uid=");
        UserHandle.formatUid(indentingPrintWriter, getSourceUid());
        indentingPrintWriter.print(" user=");
        indentingPrintWriter.print(getSourceUserId());
        indentingPrintWriter.print(" pkg=");
        indentingPrintWriter.println(getSourcePackageName());
        if (z) {
            indentingPrintWriter.println("JobInfo:");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.print("Service: ");
            indentingPrintWriter.println(this.job.getService().flattenToShortString());
            if (this.job.isPeriodic()) {
                indentingPrintWriter.print("PERIODIC: interval=");
                TimeUtils.formatDuration(this.job.getIntervalMillis(), indentingPrintWriter);
                indentingPrintWriter.print(" flex=");
                TimeUtils.formatDuration(this.job.getFlexMillis(), indentingPrintWriter);
                indentingPrintWriter.println();
            }
            if (this.job.isPersisted()) {
                indentingPrintWriter.println("PERSISTED");
            }
            if (this.job.getBias() != 0) {
                indentingPrintWriter.print("Bias: ");
                indentingPrintWriter.println(JobInfo.getBiasString(this.job.getBias()));
            }
            indentingPrintWriter.print("Priority: ");
            indentingPrintWriter.print(JobInfo.getPriorityString(this.job.getPriority()));
            int effectivePriority = getEffectivePriority();
            if (effectivePriority != this.job.getPriority()) {
                indentingPrintWriter.print(" effective=");
                indentingPrintWriter.print(JobInfo.getPriorityString(effectivePriority));
            }
            indentingPrintWriter.println();
            if (this.job.getFlags() != 0) {
                indentingPrintWriter.print("Flags: ");
                indentingPrintWriter.println(Integer.toHexString(this.job.getFlags()));
            }
            if (getInternalFlags() != 0) {
                indentingPrintWriter.print("Internal flags: ");
                indentingPrintWriter.print(Integer.toHexString(getInternalFlags()));
                if ((getInternalFlags() & 1) != 0) {
                    indentingPrintWriter.print(" HAS_FOREGROUND_EXEMPTION");
                }
                indentingPrintWriter.println();
            }
            indentingPrintWriter.print("Requires: charging=");
            indentingPrintWriter.print(this.job.isRequireCharging());
            indentingPrintWriter.print(" batteryNotLow=");
            indentingPrintWriter.print(this.job.isRequireBatteryNotLow());
            indentingPrintWriter.print(" deviceIdle=");
            indentingPrintWriter.println(this.job.isRequireDeviceIdle());
            if (this.job.getTriggerContentUris() != null) {
                indentingPrintWriter.println("Trigger content URIs:");
                indentingPrintWriter.increaseIndent();
                for (int i = 0; i < this.job.getTriggerContentUris().length; i++) {
                    JobInfo.TriggerContentUri triggerContentUri = this.job.getTriggerContentUris()[i];
                    indentingPrintWriter.print(Integer.toHexString(triggerContentUri.getFlags()));
                    indentingPrintWriter.print(' ');
                    indentingPrintWriter.println(triggerContentUri.getUri());
                }
                indentingPrintWriter.decreaseIndent();
                if (this.job.getTriggerContentUpdateDelay() >= 0) {
                    indentingPrintWriter.print("Trigger update delay: ");
                    TimeUtils.formatDuration(this.job.getTriggerContentUpdateDelay(), indentingPrintWriter);
                    indentingPrintWriter.println();
                }
                if (this.job.getTriggerContentMaxDelay() >= 0) {
                    indentingPrintWriter.print("Trigger max delay: ");
                    TimeUtils.formatDuration(this.job.getTriggerContentMaxDelay(), indentingPrintWriter);
                    indentingPrintWriter.println();
                }
                indentingPrintWriter.print("Has media backup exemption", Boolean.valueOf(this.mHasMediaBackupExemption)).println();
            }
            if (this.job.getExtras() != null && !this.job.getExtras().isDefinitelyEmpty()) {
                indentingPrintWriter.print("Extras: ");
                indentingPrintWriter.println(this.job.getExtras().toShortString());
            }
            if (this.job.getTransientExtras() != null && !this.job.getTransientExtras().isDefinitelyEmpty()) {
                indentingPrintWriter.print("Transient extras: ");
                indentingPrintWriter.println(this.job.getTransientExtras().toShortString());
            }
            if (this.job.getClipData() != null) {
                indentingPrintWriter.print("Clip data: ");
                StringBuilder sb = new StringBuilder(128);
                sb.append(this.job.getClipData());
                indentingPrintWriter.println(sb);
            }
            if (this.uriPerms != null) {
                indentingPrintWriter.println("Granted URI permissions:");
                this.uriPerms.dump(indentingPrintWriter);
            }
            if (this.job.getRequiredNetwork() != null) {
                indentingPrintWriter.print("Network type: ");
                indentingPrintWriter.println(this.job.getRequiredNetwork());
            }
            if (this.mTotalNetworkDownloadBytes != -1) {
                indentingPrintWriter.print("Network download bytes: ");
                indentingPrintWriter.println(this.mTotalNetworkDownloadBytes);
            }
            if (this.mTotalNetworkUploadBytes != -1) {
                indentingPrintWriter.print("Network upload bytes: ");
                indentingPrintWriter.println(this.mTotalNetworkUploadBytes);
            }
            if (this.mMinimumNetworkChunkBytes != -1) {
                indentingPrintWriter.print("Minimum network chunk bytes: ");
                indentingPrintWriter.println(this.mMinimumNetworkChunkBytes);
            }
            if (this.job.getMinLatencyMillis() != 0) {
                indentingPrintWriter.print("Minimum latency: ");
                TimeUtils.formatDuration(this.job.getMinLatencyMillis(), indentingPrintWriter);
                indentingPrintWriter.println();
            }
            if (this.job.getMaxExecutionDelayMillis() != 0) {
                indentingPrintWriter.print("Max execution delay: ");
                TimeUtils.formatDuration(this.job.getMaxExecutionDelayMillis(), indentingPrintWriter);
                indentingPrintWriter.println();
            }
            indentingPrintWriter.print("Backoff: policy=");
            indentingPrintWriter.print(this.job.getBackoffPolicy());
            indentingPrintWriter.print(" initial=");
            TimeUtils.formatDuration(this.job.getInitialBackoffMillis(), indentingPrintWriter);
            indentingPrintWriter.println();
            if (this.job.hasEarlyConstraint()) {
                indentingPrintWriter.println("Has early constraint");
            }
            if (this.job.hasLateConstraint()) {
                indentingPrintWriter.println("Has late constraint");
            }
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.print("Required constraints:");
        dumpConstraints(indentingPrintWriter, this.requiredConstraints);
        indentingPrintWriter.println();
        indentingPrintWriter.print("Preferred constraints:");
        dumpConstraints(indentingPrintWriter, this.mPreferredConstraints);
        indentingPrintWriter.println();
        indentingPrintWriter.print("Dynamic constraints:");
        dumpConstraints(indentingPrintWriter, this.mDynamicConstraints);
        indentingPrintWriter.println();
        if (z) {
            indentingPrintWriter.print("Satisfied constraints:");
            dumpConstraints(indentingPrintWriter, this.satisfiedConstraints);
            indentingPrintWriter.println();
            indentingPrintWriter.print("Unsatisfied constraints:");
            dumpConstraints(indentingPrintWriter, (this.requiredConstraints | 16777216 | 134217728) & (~this.satisfiedConstraints));
            indentingPrintWriter.println();
            if (hasFlexibilityConstraint()) {
                indentingPrintWriter.print("Num Required Flexible constraints: ");
                indentingPrintWriter.print(getNumRequiredFlexibleConstraints());
                indentingPrintWriter.println();
                indentingPrintWriter.print("Num Dropped Flexible constraints: ");
                indentingPrintWriter.print(getNumDroppedFlexibleConstraints());
                indentingPrintWriter.println();
            }
            indentingPrintWriter.println("Constraint history:");
            indentingPrintWriter.increaseIndent();
            for (int i2 = 0; i2 < 10; i2++) {
                int i3 = (this.mConstraintChangeHistoryIndex + i2) % 10;
                long j2 = this.mConstraintUpdatedTimesElapsed[i3];
                if (j2 != 0) {
                    TimeUtils.formatDuration(j2, j, indentingPrintWriter);
                    indentingPrintWriter.print(" =");
                    dumpConstraints(indentingPrintWriter, this.mConstraintStatusHistory[i3]);
                    indentingPrintWriter.println();
                }
            }
            indentingPrintWriter.decreaseIndent();
            if (this.appHasDozeExemption) {
                indentingPrintWriter.println("Doze whitelisted: true");
            }
            if (this.uidActive) {
                indentingPrintWriter.println("Uid: active");
            }
            if (this.job.isExemptedFromAppStandby()) {
                indentingPrintWriter.println("Is exempted from app standby");
            }
        }
        if (this.trackingControllers != 0) {
            indentingPrintWriter.print("Tracking:");
            if ((this.trackingControllers & 1) != 0) {
                indentingPrintWriter.print(" BATTERY");
            }
            if ((this.trackingControllers & 2) != 0) {
                indentingPrintWriter.print(" CONNECTIVITY");
            }
            if ((this.trackingControllers & 4) != 0) {
                indentingPrintWriter.print(" CONTENT");
            }
            if ((this.trackingControllers & 8) != 0) {
                indentingPrintWriter.print(" IDLE");
            }
            if ((this.trackingControllers & 16) != 0) {
                indentingPrintWriter.print(" STORAGE");
            }
            if ((this.trackingControllers & 32) != 0) {
                indentingPrintWriter.print(" TIME");
            }
            if ((this.trackingControllers & 64) != 0) {
                indentingPrintWriter.print(" QUOTA");
            }
            indentingPrintWriter.println();
        }
        indentingPrintWriter.println("Implicit constraints:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.print("readyNotDozing: ");
        indentingPrintWriter.println(this.mReadyNotDozing);
        indentingPrintWriter.print("readyNotRestrictedInBg: ");
        indentingPrintWriter.println(this.mReadyNotRestrictedInBg);
        if (!this.job.isPeriodic() && hasDeadlineConstraint()) {
            indentingPrintWriter.print("readyDeadlineSatisfied: ");
            indentingPrintWriter.println(this.mReadyDeadlineSatisfied);
        }
        if (this.mDynamicConstraints != 0) {
            indentingPrintWriter.print("readyDynamicSatisfied: ");
            indentingPrintWriter.println(this.mReadyDynamicSatisfied);
        }
        indentingPrintWriter.print("readyComponentEnabled: ");
        indentingPrintWriter.println(this.serviceProcessName != null);
        if ((getFlags() & 16) != 0) {
            indentingPrintWriter.print("expeditedQuotaApproved: ");
            indentingPrintWriter.print(this.mExpeditedQuotaApproved);
            indentingPrintWriter.print(" expeditedTareApproved: ");
            indentingPrintWriter.print(this.mExpeditedTareApproved);
            indentingPrintWriter.print(" (started as EJ: ");
            indentingPrintWriter.print(this.startedAsExpeditedJob);
            indentingPrintWriter.println(")");
        }
        if ((32 & getFlags()) != 0) {
            indentingPrintWriter.print("userInitiatedApproved: ");
            indentingPrintWriter.print(shouldTreatAsUserInitiatedJob());
            indentingPrintWriter.print(" (started as UIJ: ");
            indentingPrintWriter.print(this.startedAsUserInitiatedJob);
            indentingPrintWriter.println(")");
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.print("Started with foreground flag: ");
        indentingPrintWriter.println(this.startedWithForegroundFlag);
        if (this.mIsUserBgRestricted) {
            indentingPrintWriter.println("User BG restricted");
        }
        if (this.changedAuthorities != null) {
            indentingPrintWriter.println("Changed authorities:");
            indentingPrintWriter.increaseIndent();
            for (int i4 = 0; i4 < this.changedAuthorities.size(); i4++) {
                indentingPrintWriter.println(this.changedAuthorities.valueAt(i4));
            }
            indentingPrintWriter.decreaseIndent();
        }
        if (this.changedUris != null) {
            indentingPrintWriter.println("Changed URIs:");
            indentingPrintWriter.increaseIndent();
            for (int i5 = 0; i5 < this.changedUris.size(); i5++) {
                indentingPrintWriter.println(this.changedUris.valueAt(i5));
            }
            indentingPrintWriter.decreaseIndent();
        }
        if (this.network != null) {
            indentingPrintWriter.print("Network: ");
            indentingPrintWriter.println(this.network);
        }
        ArrayList<JobWorkItem> arrayList = this.pendingWork;
        if (arrayList != null && arrayList.size() > 0) {
            indentingPrintWriter.println("Pending work:");
            for (int i6 = 0; i6 < this.pendingWork.size(); i6++) {
                dumpJobWorkItem(indentingPrintWriter, this.pendingWork.get(i6), i6);
            }
        }
        ArrayList<JobWorkItem> arrayList2 = this.executingWork;
        if (arrayList2 != null && arrayList2.size() > 0) {
            indentingPrintWriter.println("Executing work:");
            for (int i7 = 0; i7 < this.executingWork.size(); i7++) {
                dumpJobWorkItem(indentingPrintWriter, this.executingWork.get(i7), i7);
            }
        }
        indentingPrintWriter.print("Standby bucket: ");
        indentingPrintWriter.println(getBucketName());
        indentingPrintWriter.increaseIndent();
        if (this.whenStandbyDeferred != 0) {
            indentingPrintWriter.print("Deferred since: ");
            TimeUtils.formatDuration(this.whenStandbyDeferred, j, indentingPrintWriter);
            indentingPrintWriter.println();
        }
        if (this.mFirstForceBatchedTimeElapsed != 0) {
            indentingPrintWriter.print("Time since first force batch attempt: ");
            TimeUtils.formatDuration(this.mFirstForceBatchedTimeElapsed, j, indentingPrintWriter);
            indentingPrintWriter.println();
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.print("Enqueue time: ");
        TimeUtils.formatDuration(this.enqueueTime, j, indentingPrintWriter);
        indentingPrintWriter.println();
        indentingPrintWriter.print("Run time: earliest=");
        formatRunTime((PrintWriter) indentingPrintWriter, this.earliestRunTimeElapsedMillis, 0L, j);
        indentingPrintWriter.print(", latest=");
        formatRunTime((PrintWriter) indentingPrintWriter, this.latestRunTimeElapsedMillis, NO_LATEST_RUNTIME, j);
        indentingPrintWriter.print(", original latest=");
        formatRunTime((PrintWriter) indentingPrintWriter, this.mOriginalLatestRunTimeElapsedMillis, NO_LATEST_RUNTIME, j);
        indentingPrintWriter.println();
        if (this.mCumulativeExecutionTimeMs != 0) {
            indentingPrintWriter.print("Cumulative execution time=");
            TimeUtils.formatDuration(this.mCumulativeExecutionTimeMs, indentingPrintWriter);
            indentingPrintWriter.println();
        }
        if (this.numFailures != 0) {
            indentingPrintWriter.print("Num failures: ");
            indentingPrintWriter.println(this.numFailures);
        }
        if (this.mNumSystemStops != 0) {
            indentingPrintWriter.print("Num system stops: ");
            indentingPrintWriter.println(this.mNumSystemStops);
        }
        if (this.mLastSuccessfulRunTime != 0) {
            indentingPrintWriter.print("Last successful run: ");
            indentingPrintWriter.println(formatTime(this.mLastSuccessfulRunTime));
        }
        if (this.mLastFailedRunTime != 0) {
            indentingPrintWriter.print("Last failed run: ");
            indentingPrintWriter.println(formatTime(this.mLastFailedRunTime));
        }
    }

    private static CharSequence formatTime(long j) {
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", j);
    }

    public void dump(ProtoOutputStream protoOutputStream, long j, boolean z, long j2) {
        long start = protoOutputStream.start(j);
        long j3 = 1120986464257L;
        protoOutputStream.write(1120986464257L, this.callingUid);
        protoOutputStream.write(1138166333442L, this.tag);
        protoOutputStream.write(1120986464259L, getSourceUid());
        protoOutputStream.write(1120986464260L, getSourceUserId());
        protoOutputStream.write(1138166333445L, getSourcePackageName());
        if (z) {
            long start2 = protoOutputStream.start(1146756268038L);
            this.job.getService().dumpDebug(protoOutputStream, 1146756268033L);
            protoOutputStream.write(1133871366146L, this.job.isPeriodic());
            protoOutputStream.write(1112396529667L, this.job.getIntervalMillis());
            protoOutputStream.write(1112396529668L, this.job.getFlexMillis());
            protoOutputStream.write(1133871366149L, this.job.isPersisted());
            protoOutputStream.write(1172526071814L, this.job.getBias());
            protoOutputStream.write(1120986464263L, this.job.getFlags());
            protoOutputStream.write(1112396529688L, getInternalFlags());
            protoOutputStream.write(1133871366152L, this.job.isRequireCharging());
            protoOutputStream.write(1133871366153L, this.job.isRequireBatteryNotLow());
            protoOutputStream.write(1133871366154L, this.job.isRequireDeviceIdle());
            if (this.job.getTriggerContentUris() != null) {
                int i = 0;
                while (i < this.job.getTriggerContentUris().length) {
                    long start3 = protoOutputStream.start(2246267895819L);
                    JobInfo.TriggerContentUri triggerContentUri = this.job.getTriggerContentUris()[i];
                    protoOutputStream.write(j3, triggerContentUri.getFlags());
                    Uri uri = triggerContentUri.getUri();
                    if (uri != null) {
                        protoOutputStream.write(1138166333442L, uri.toString());
                    }
                    protoOutputStream.end(start3);
                    i++;
                    j3 = 1120986464257L;
                }
                if (this.job.getTriggerContentUpdateDelay() >= 0) {
                    protoOutputStream.write(1112396529676L, this.job.getTriggerContentUpdateDelay());
                }
                if (this.job.getTriggerContentMaxDelay() >= 0) {
                    protoOutputStream.write(1112396529677L, this.job.getTriggerContentMaxDelay());
                }
            }
            if (this.job.getExtras() != null && !this.job.getExtras().isDefinitelyEmpty()) {
                this.job.getExtras().dumpDebug(protoOutputStream, 1146756268046L);
            }
            if (this.job.getTransientExtras() != null && !this.job.getTransientExtras().isDefinitelyEmpty()) {
                this.job.getTransientExtras().dumpDebug(protoOutputStream, 1146756268047L);
            }
            if (this.job.getClipData() != null) {
                this.job.getClipData().dumpDebug(protoOutputStream, 1146756268048L);
            }
            GrantedUriPermissions grantedUriPermissions = this.uriPerms;
            if (grantedUriPermissions != null) {
                grantedUriPermissions.dump(protoOutputStream, 1146756268049L);
            }
            long j4 = this.mTotalNetworkDownloadBytes;
            if (j4 != -1) {
                protoOutputStream.write(1112396529689L, j4);
            }
            long j5 = this.mTotalNetworkUploadBytes;
            if (j5 != -1) {
                protoOutputStream.write(1112396529690L, j5);
            }
            protoOutputStream.write(1112396529684L, this.job.getMinLatencyMillis());
            protoOutputStream.write(1112396529685L, this.job.getMaxExecutionDelayMillis());
            long start4 = protoOutputStream.start(1146756268054L);
            protoOutputStream.write(1159641169921L, this.job.getBackoffPolicy());
            protoOutputStream.write(1112396529666L, this.job.getInitialBackoffMillis());
            protoOutputStream.end(start4);
            protoOutputStream.write(1133871366167L, this.job.hasEarlyConstraint());
            protoOutputStream.write(1133871366168L, this.job.hasLateConstraint());
            protoOutputStream.end(start2);
        }
        dumpConstraints(protoOutputStream, 2259152797703L, this.requiredConstraints);
        dumpConstraints(protoOutputStream, 2259152797727L, this.mDynamicConstraints);
        if (z) {
            dumpConstraints(protoOutputStream, 2259152797704L, this.satisfiedConstraints);
            dumpConstraints(protoOutputStream, 2259152797705L, (this.requiredConstraints | 16777216) & (~this.satisfiedConstraints));
            protoOutputStream.write(1133871366154L, this.appHasDozeExemption);
            protoOutputStream.write(1133871366170L, this.uidActive);
            protoOutputStream.write(1133871366171L, this.job.isExemptedFromAppStandby());
        }
        if ((this.trackingControllers & 1) != 0) {
            protoOutputStream.write(2259152797707L, 0);
        }
        if ((this.trackingControllers & 2) != 0) {
            protoOutputStream.write(2259152797707L, 1);
        }
        if ((this.trackingControllers & 4) != 0) {
            protoOutputStream.write(2259152797707L, 2);
        }
        if ((this.trackingControllers & 8) != 0) {
            protoOutputStream.write(2259152797707L, 3);
        }
        if ((this.trackingControllers & 16) != 0) {
            protoOutputStream.write(2259152797707L, 4);
        }
        if ((this.trackingControllers & 32) != 0) {
            protoOutputStream.write(2259152797707L, 5);
        }
        if ((this.trackingControllers & 64) != 0) {
            protoOutputStream.write(2259152797707L, 6);
        }
        long start5 = protoOutputStream.start(1146756268057L);
        protoOutputStream.write(1133871366145L, this.mReadyNotDozing);
        protoOutputStream.write(1133871366146L, this.mReadyNotRestrictedInBg);
        protoOutputStream.write(1133871366147L, this.mReadyDynamicSatisfied);
        protoOutputStream.end(start5);
        if (this.changedAuthorities != null) {
            for (int i2 = 0; i2 < this.changedAuthorities.size(); i2++) {
                protoOutputStream.write(2237677961228L, this.changedAuthorities.valueAt(i2));
            }
        }
        if (this.changedUris != null) {
            for (int i3 = 0; i3 < this.changedUris.size(); i3++) {
                protoOutputStream.write(2237677961229L, this.changedUris.valueAt(i3).toString());
            }
        }
        if (this.pendingWork != null) {
            for (int i4 = 0; i4 < this.pendingWork.size(); i4++) {
                dumpJobWorkItem(protoOutputStream, 2246267895823L, this.pendingWork.get(i4));
            }
        }
        if (this.executingWork != null) {
            for (int i5 = 0; i5 < this.executingWork.size(); i5++) {
                dumpJobWorkItem(protoOutputStream, 2246267895824L, this.executingWork.get(i5));
            }
        }
        protoOutputStream.write(1159641169937L, this.standbyBucket);
        protoOutputStream.write(1112396529682L, j2 - this.enqueueTime);
        long j6 = this.whenStandbyDeferred;
        protoOutputStream.write(1112396529692L, j6 == 0 ? 0L : j2 - j6);
        long j7 = this.mFirstForceBatchedTimeElapsed;
        protoOutputStream.write(1112396529693L, j7 == 0 ? 0L : j2 - j7);
        long j8 = this.earliestRunTimeElapsedMillis;
        if (j8 == 0) {
            protoOutputStream.write(1176821039123L, 0);
        } else {
            protoOutputStream.write(1176821039123L, j8 - j2);
        }
        long j9 = this.latestRunTimeElapsedMillis;
        if (j9 == NO_LATEST_RUNTIME) {
            protoOutputStream.write(1176821039124L, 0);
        } else {
            protoOutputStream.write(1176821039124L, j9 - j2);
        }
        protoOutputStream.write(1116691496990L, this.mOriginalLatestRunTimeElapsedMillis);
        protoOutputStream.write(1120986464277L, this.numFailures + this.mNumSystemStops);
        protoOutputStream.write(1112396529686L, this.mLastSuccessfulRunTime);
        protoOutputStream.write(1112396529687L, this.mLastFailedRunTime);
        protoOutputStream.end(start);
    }

    public IJobStatusWrapper getWrapper() {
        return this.mJobStatusWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class JobStatusWrapper implements IJobStatusWrapper {
        private JobStatusWrapper() {
        }

        @Override // com.android.server.job.controllers.IJobStatusWrapper
        public IJobStatusExt getExtImpl() {
            return JobStatus.mJobStatusExt;
        }

        @Override // com.android.server.job.controllers.IJobStatusWrapper
        public boolean hasConstraint(int i) {
            return JobStatus.this.hasConstraint(i);
        }
    }
}
