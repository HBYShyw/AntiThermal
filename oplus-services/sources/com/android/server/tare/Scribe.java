package com.android.server.tare;

import android.os.Environment;
import android.os.SystemClock;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseArrayMap;
import android.util.SparseLongArray;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.job.controllers.JobStatus;
import com.android.server.tare.Analyst;
import com.android.server.tare.Ledger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class Scribe {
    private static final boolean DEBUG;
    private static final int MAX_NUM_TRANSACTION_DUMP = 25;
    private static final long MAX_TRANSACTION_AGE_MS = 691200000;
    private static final int STATE_FILE_VERSION = 0;
    private static final String TAG;
    private static final long WRITE_DELAY = 30000;
    private static final String XML_ATTR_CONSUMPTION_LIMIT = "consumptionLimit";
    private static final String XML_ATTR_CTP = "ctp";
    private static final String XML_ATTR_CURRENT_BALANCE = "currentBalance";
    private static final String XML_ATTR_DELTA = "delta";
    private static final String XML_ATTR_END_TIME = "endTime";
    private static final String XML_ATTR_EVENT_ID = "eventId";
    private static final String XML_ATTR_LAST_RECLAMATION_TIME = "lastReclamationTime";
    private static final String XML_ATTR_LAST_STOCK_RECALCULATION_TIME = "lastStockRecalculationTime";
    private static final String XML_ATTR_PACKAGE_NAME = "pkgName";
    private static final String XML_ATTR_PR_BATTERY_LEVEL = "batteryLevel";
    private static final String XML_ATTR_PR_DISCHARGE = "discharge";
    private static final String XML_ATTR_PR_LOSS = "loss";
    private static final String XML_ATTR_PR_NEG_REGULATIONS = "negRegulations";
    private static final String XML_ATTR_PR_NUM_LOSS = "numLoss";
    private static final String XML_ATTR_PR_NUM_NEG_REGULATIONS = "numNegRegulations";
    private static final String XML_ATTR_PR_NUM_POS_REGULATIONS = "numPosRegulations";
    private static final String XML_ATTR_PR_NUM_PROFIT = "numProfits";
    private static final String XML_ATTR_PR_NUM_REWARDS = "numRewards";
    private static final String XML_ATTR_PR_POS_REGULATIONS = "posRegulations";
    private static final String XML_ATTR_PR_PROFIT = "profit";
    private static final String XML_ATTR_PR_REWARDS = "rewards";
    private static final String XML_ATTR_PR_SCREEN_OFF_DISCHARGE_MAH = "screenOffDischargeMah";
    private static final String XML_ATTR_PR_SCREEN_OFF_DURATION_MS = "screenOffDurationMs";
    private static final String XML_ATTR_REMAINING_CONSUMABLE_CAKES = "remainingConsumableCakes";
    private static final String XML_ATTR_START_TIME = "startTime";
    private static final String XML_ATTR_TAG = "tag";
    private static final String XML_ATTR_TIME_SINCE_FIRST_SETUP_MS = "timeSinceFirstSetup";
    private static final String XML_ATTR_USER_ID = "userId";
    private static final String XML_ATTR_VERSION = "version";
    private static final String XML_TAG_HIGH_LEVEL_STATE = "irs-state";
    private static final String XML_TAG_LEDGER = "ledger";
    private static final String XML_TAG_PERIOD_REPORT = "report";
    private static final String XML_TAG_REWARD_BUCKET = "rewardBucket";
    private static final String XML_TAG_TARE = "tare";
    private static final String XML_TAG_TRANSACTION = "transaction";
    private static final String XML_TAG_USER = "user";
    private final Analyst mAnalyst;
    private final Runnable mCleanRunnable;
    private final InternalResourceService mIrs;

    @GuardedBy({"mIrs.getLock()"})
    private long mLastReclamationTime;

    @GuardedBy({"mIrs.getLock()"})
    private long mLastStockRecalculationTime;

    @GuardedBy({"mIrs.getLock()"})
    private final SparseArrayMap<String, Ledger> mLedgers;
    private long mLoadedTimeSinceFirstSetup;

    @GuardedBy({"mIrs.getLock()"})
    private final SparseLongArray mRealtimeSinceUsersAddedOffsets;

    @GuardedBy({"mIrs.getLock()"})
    private long mRemainingConsumableCakes;

    @GuardedBy({"mIrs.getLock()"})
    private long mSatiatedConsumptionLimit;
    private final AtomicFile mStateFile;
    private final Runnable mWriteRunnable;

    static {
        String str = "TARE-" + Scribe.class.getSimpleName();
        TAG = str;
        DEBUG = InternalResourceService.DEBUG || Log.isLoggable(str, 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Scribe(InternalResourceService internalResourceService, Analyst analyst) {
        this(internalResourceService, analyst, Environment.getDataSystemDirectory());
    }

    @VisibleForTesting
    Scribe(InternalResourceService internalResourceService, Analyst analyst, File file) {
        this.mLedgers = new SparseArrayMap<>();
        this.mRealtimeSinceUsersAddedOffsets = new SparseLongArray();
        this.mCleanRunnable = new Runnable() { // from class: com.android.server.tare.Scribe$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                Scribe.this.cleanupLedgers();
            }
        };
        this.mWriteRunnable = new Runnable() { // from class: com.android.server.tare.Scribe$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Scribe.this.writeState();
            }
        };
        this.mIrs = internalResourceService;
        this.mAnalyst = analyst;
        File file2 = new File(file, XML_TAG_TARE);
        file2.mkdirs();
        this.mStateFile = new AtomicFile(new File(file2, "state.xml"), XML_TAG_TARE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void adjustRemainingConsumableCakesLocked(long j) {
        long j2 = this.mRemainingConsumableCakes;
        long j3 = j + j2;
        this.mRemainingConsumableCakes = j3;
        if (j3 < 0) {
            Slog.w(TAG, "Overdrew consumable cakes by " + TareUtils.cakeToString(-this.mRemainingConsumableCakes));
            this.mRemainingConsumableCakes = 0L;
        }
        if (this.mRemainingConsumableCakes != j2) {
            postWrite();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void discardLedgerLocked(int i, String str) {
        this.mLedgers.delete(i, str);
        postWrite();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void onUserRemovedLocked(int i) {
        this.mLedgers.delete(i);
        this.mRealtimeSinceUsersAddedOffsets.delete(i);
        postWrite();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public long getSatiatedConsumptionLimitLocked() {
        return this.mSatiatedConsumptionLimit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public long getLastReclamationTimeLocked() {
        return this.mLastReclamationTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public long getLastStockRecalculationTimeLocked() {
        return this.mLastStockRecalculationTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public Ledger getLedgerLocked(int i, String str) {
        Ledger ledger = (Ledger) this.mLedgers.get(i, str);
        if (ledger != null) {
            return ledger;
        }
        Ledger ledger2 = new Ledger();
        this.mLedgers.add(i, str, ledger2);
        return ledger2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public SparseArrayMap<String, Ledger> getLedgersLocked() {
        return this.mLedgers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public long getCakesInCirculationForLoggingLocked() {
        long j = 0;
        for (int numMaps = this.mLedgers.numMaps() - 1; numMaps >= 0; numMaps--) {
            for (int numElementsForKeyAt = this.mLedgers.numElementsForKeyAt(numMaps) - 1; numElementsForKeyAt >= 0; numElementsForKeyAt--) {
                j += ((Ledger) this.mLedgers.valueAt(numMaps, numElementsForKeyAt)).getCurrentBalance();
            }
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getRealtimeSinceFirstSetupMs(long j) {
        return this.mLoadedTimeSinceFirstSetup + j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public long getRemainingConsumableCakesLocked() {
        return this.mRemainingConsumableCakes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public SparseLongArray getRealtimeSinceUsersAddedLocked(long j) {
        SparseLongArray sparseLongArray = new SparseLongArray();
        for (int size = this.mRealtimeSinceUsersAddedOffsets.size() - 1; size >= 0; size--) {
            sparseLongArray.put(this.mRealtimeSinceUsersAddedOffsets.keyAt(size), this.mRealtimeSinceUsersAddedOffsets.valueAt(size) + j);
        }
        return sparseLongArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0155 A[Catch: all -> 0x01b1, TryCatch #0 {all -> 0x01b1, blocks: (B:27:0x0073, B:31:0x0080, B:34:0x0087, B:36:0x008b, B:41:0x0098, B:43:0x00a6, B:46:0x00b1, B:51:0x00d2, B:57:0x0197, B:58:0x00e9, B:61:0x00f0, B:72:0x012b, B:73:0x0142, B:74:0x014a, B:75:0x0155, B:77:0x0104, B:80:0x010e, B:83:0x0119, B:87:0x01a3), top: B:26:0x0073 }] */
    @GuardedBy({"mIrs.getLock()"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadFromDiskLocked() {
        String name;
        int i;
        SparseArray<ArraySet<String>> sparseArray;
        int attributeInt;
        this.mLedgers.clear();
        if (!recordExists()) {
            this.mSatiatedConsumptionLimit = this.mIrs.getInitialSatiatedConsumptionLimitLocked();
            this.mRemainingConsumableCakes = this.mIrs.getConsumptionLimitLocked();
            return;
        }
        this.mSatiatedConsumptionLimit = 0L;
        this.mRemainingConsumableCakes = 0L;
        SparseArray<ArraySet<String>> sparseArray2 = new SparseArray<>();
        SparseArrayMap<String, InstalledPackageInfo> installedPackages = this.mIrs.getInstalledPackages();
        int i2 = 1;
        for (int numMaps = installedPackages.numMaps() - 1; numMaps >= 0; numMaps--) {
            int keyAt = installedPackages.keyAt(numMaps);
            for (int numElementsForKeyAt = installedPackages.numElementsForKeyAt(numMaps) - 1; numElementsForKeyAt >= 0; numElementsForKeyAt--) {
                InstalledPackageInfo installedPackageInfo = (InstalledPackageInfo) installedPackages.valueAt(numMaps, numElementsForKeyAt);
                if (installedPackageInfo.uid != -1) {
                    ArraySet<String> arraySet = sparseArray2.get(keyAt);
                    if (arraySet == null) {
                        arraySet = new ArraySet<>();
                        sparseArray2.put(keyAt, arraySet);
                    }
                    arraySet.add(installedPackageInfo.packageName);
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        try {
            FileInputStream openRead = this.mStateFile.openRead();
            try {
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                int eventType = resolvePullParser.getEventType();
                while (eventType != 2 && eventType != 1) {
                    eventType = resolvePullParser.next();
                }
                if (eventType == 1) {
                    if (DEBUG) {
                        Slog.w(TAG, "No persisted state.");
                    }
                    if (openRead != null) {
                        openRead.close();
                        return;
                    }
                    return;
                }
                if (XML_TAG_TARE.equals(resolvePullParser.getName()) && ((attributeInt = resolvePullParser.getAttributeInt((String) null, XML_ATTR_VERSION)) < 0 || attributeInt > 0)) {
                    Slog.e(TAG, "Invalid version number (" + attributeInt + "), aborting file read");
                    if (openRead != null) {
                        openRead.close();
                        return;
                    }
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis() - MAX_TRANSACTION_AGE_MS;
                long j = JobStatus.NO_LATEST_RUNTIME;
                for (int next = resolvePullParser.next(); next != i2; next = resolvePullParser.next()) {
                    if (next == 2 && (name = resolvePullParser.getName()) != null) {
                        int hashCode = name.hashCode();
                        if (hashCode == -934521548) {
                            if (name.equals(XML_TAG_PERIOD_REPORT)) {
                                i = 2;
                                if (i == 0) {
                                }
                            }
                            i = -1;
                            if (i == 0) {
                            }
                        } else if (hashCode != 3599307) {
                            if (hashCode == 689502574 && name.equals(XML_TAG_HIGH_LEVEL_STATE)) {
                                i = 0;
                                if (i == 0) {
                                    if (i == i2) {
                                        j = Math.min(j, readUserFromXmlLocked(resolvePullParser, sparseArray2, currentTimeMillis));
                                    } else if (i == 2) {
                                        arrayList.add(readReportFromXml(resolvePullParser));
                                    } else {
                                        Slog.e(TAG, "Unexpected tag: " + name);
                                    }
                                    sparseArray = sparseArray2;
                                    sparseArray2 = sparseArray;
                                    i2 = 1;
                                } else {
                                    this.mLastReclamationTime = resolvePullParser.getAttributeLong((String) null, XML_ATTR_LAST_RECLAMATION_TIME);
                                    sparseArray = sparseArray2;
                                    this.mLastStockRecalculationTime = resolvePullParser.getAttributeLong((String) null, XML_ATTR_LAST_STOCK_RECALCULATION_TIME, 0L);
                                    this.mLoadedTimeSinceFirstSetup = resolvePullParser.getAttributeLong((String) null, XML_ATTR_TIME_SINCE_FIRST_SETUP_MS, -SystemClock.elapsedRealtime());
                                    this.mSatiatedConsumptionLimit = resolvePullParser.getAttributeLong((String) null, XML_ATTR_CONSUMPTION_LIMIT, this.mIrs.getInitialSatiatedConsumptionLimitLocked());
                                    long consumptionLimitLocked = this.mIrs.getConsumptionLimitLocked();
                                    this.mRemainingConsumableCakes = Math.min(consumptionLimitLocked, resolvePullParser.getAttributeLong((String) null, XML_ATTR_REMAINING_CONSUMABLE_CAKES, consumptionLimitLocked));
                                    sparseArray2 = sparseArray;
                                    i2 = 1;
                                }
                            }
                            i = -1;
                            if (i == 0) {
                            }
                        } else {
                            if (name.equals(XML_TAG_USER)) {
                                i = i2;
                                if (i == 0) {
                                }
                            }
                            i = -1;
                            if (i == 0) {
                            }
                        }
                    }
                    sparseArray = sparseArray2;
                    sparseArray2 = sparseArray;
                    i2 = 1;
                }
                this.mAnalyst.loadReports(arrayList);
                scheduleCleanup(j);
                if (openRead != null) {
                    openRead.close();
                }
            } finally {
            }
        } catch (IOException | XmlPullParserException e) {
            Slog.wtf(TAG, "Error reading state from disk", e);
        }
    }

    @VisibleForTesting
    void postWrite() {
        TareHandlerThread.getHandler().postDelayed(this.mWriteRunnable, WRITE_DELAY);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean recordExists() {
        return this.mStateFile.exists();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void setConsumptionLimitLocked(long j) {
        long j2 = this.mRemainingConsumableCakes;
        if (j2 > j) {
            this.mRemainingConsumableCakes = j;
        } else {
            long j3 = this.mSatiatedConsumptionLimit;
            if (j > j3) {
                this.mRemainingConsumableCakes = j - (j3 - j2);
            }
        }
        this.mSatiatedConsumptionLimit = j;
        postWrite();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void setLastReclamationTimeLocked(long j) {
        this.mLastReclamationTime = j;
        postWrite();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void setLastStockRecalculationTimeLocked(long j) {
        this.mLastStockRecalculationTime = j;
        postWrite();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void setUserAddedTimeLocked(int i, long j) {
        this.mRealtimeSinceUsersAddedOffsets.put(i, -j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void tearDownLocked() {
        TareHandlerThread.getHandler().removeCallbacks(this.mCleanRunnable);
        TareHandlerThread.getHandler().removeCallbacks(this.mWriteRunnable);
        this.mLedgers.clear();
        this.mRemainingConsumableCakes = 0L;
        this.mSatiatedConsumptionLimit = 0L;
        this.mLastReclamationTime = 0L;
    }

    @VisibleForTesting
    void writeImmediatelyForTesting() {
        this.mWriteRunnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupLedgers() {
        synchronized (this.mIrs.getLock()) {
            TareHandlerThread.getHandler().removeCallbacks(this.mCleanRunnable);
            long j = JobStatus.NO_LATEST_RUNTIME;
            for (int numMaps = this.mLedgers.numMaps() - 1; numMaps >= 0; numMaps--) {
                int keyAt = this.mLedgers.keyAt(numMaps);
                for (int numElementsForKey = this.mLedgers.numElementsForKey(keyAt) - 1; numElementsForKey >= 0; numElementsForKey--) {
                    Ledger.Transaction removeOldTransactions = ((Ledger) this.mLedgers.get(keyAt, (String) this.mLedgers.keyAt(numMaps, numElementsForKey))).removeOldTransactions(MAX_TRANSACTION_AGE_MS);
                    if (removeOldTransactions != null) {
                        j = Math.min(j, removeOldTransactions.endTimeMs);
                    }
                }
            }
            scheduleCleanup(j);
        }
    }

    private static Pair<String, Ledger> readLedgerFromXml(TypedXmlPullParser typedXmlPullParser, ArraySet<String> arraySet, long j) throws XmlPullParserException, IOException {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, "pkgName");
        long attributeLong = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_CURRENT_BALANCE);
        boolean contains = arraySet.contains(attributeValue);
        if (!contains) {
            Slog.w(TAG, "Invalid pkg " + attributeValue + " is saved to disk");
        }
        int next = typedXmlPullParser.next();
        while (next != 1) {
            String name = typedXmlPullParser.getName();
            if (next == 3) {
                if (XML_TAG_LEDGER.equals(name)) {
                    break;
                }
            } else {
                if (next != 2 || name == null) {
                    Slog.e(TAG, "Unexpected event: (" + next + ") " + name);
                    return null;
                }
                if (contains) {
                    boolean z = DEBUG;
                    if (z) {
                        Slog.d(TAG, "Starting ledger tag: " + name);
                    }
                    if (name.equals(XML_TAG_REWARD_BUCKET)) {
                        arrayList2.add(readRewardBucketFromXml(typedXmlPullParser));
                    } else if (name.equals(XML_TAG_TRANSACTION)) {
                        long attributeLong2 = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_END_TIME);
                        if (attributeLong2 > j) {
                            arrayList.add(new Ledger.Transaction(typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_START_TIME), attributeLong2, typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_EVENT_ID), typedXmlPullParser.getAttributeValue((String) null, XML_ATTR_TAG), typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_DELTA), typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_CTP)));
                        } else if (z) {
                            Slog.d(TAG, "Skipping event because it's too old.");
                        }
                    } else {
                        Slog.e(TAG, "Unexpected event: (" + next + ") " + name);
                        return null;
                    }
                } else {
                    continue;
                }
            }
            next = typedXmlPullParser.next();
        }
        if (contains) {
            return Pair.create(attributeValue, new Ledger(attributeLong, arrayList, arrayList2));
        }
        return null;
    }

    @GuardedBy({"mIrs.getLock()"})
    private long readUserFromXmlLocked(TypedXmlPullParser typedXmlPullParser, SparseArray<ArraySet<String>> sparseArray, long j) throws XmlPullParserException, IOException {
        Pair<String, Ledger> readLedgerFromXml;
        Ledger ledger;
        int attributeInt = typedXmlPullParser.getAttributeInt((String) null, "userId");
        ArraySet<String> arraySet = sparseArray.get(attributeInt);
        if (arraySet == null) {
            Slog.w(TAG, "Invalid user " + attributeInt + " is saved to disk");
            attributeInt = -10000;
        }
        if (attributeInt != -10000) {
            this.mRealtimeSinceUsersAddedOffsets.put(attributeInt, typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_TIME_SINCE_FIRST_SETUP_MS, -SystemClock.elapsedRealtime()));
        }
        int next = typedXmlPullParser.next();
        long j2 = JobStatus.NO_LATEST_RUNTIME;
        while (next != 1) {
            String name = typedXmlPullParser.getName();
            if (next == 3) {
                if (XML_TAG_USER.equals(name)) {
                    break;
                }
            } else if (!XML_TAG_LEDGER.equals(name)) {
                Slog.e(TAG, "Unknown tag: " + name);
            } else if (attributeInt != -10000 && (readLedgerFromXml = readLedgerFromXml(typedXmlPullParser, arraySet, j)) != null && (ledger = (Ledger) readLedgerFromXml.second) != null) {
                this.mLedgers.add(attributeInt, (String) readLedgerFromXml.first, ledger);
                Ledger.Transaction earliestTransaction = ledger.getEarliestTransaction();
                if (earliestTransaction != null) {
                    j2 = Math.min(j2, earliestTransaction.endTimeMs);
                }
            }
            next = typedXmlPullParser.next();
        }
        return j2;
    }

    private static Analyst.Report readReportFromXml(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        Analyst.Report report = new Analyst.Report();
        report.cumulativeBatteryDischarge = typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_PR_DISCHARGE);
        report.currentBatteryLevel = typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_PR_BATTERY_LEVEL);
        report.cumulativeProfit = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_PR_PROFIT);
        report.numProfitableActions = typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_PR_NUM_PROFIT);
        report.cumulativeLoss = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_PR_LOSS);
        report.numUnprofitableActions = typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_PR_NUM_LOSS);
        report.cumulativeRewards = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_PR_REWARDS);
        report.numRewards = typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_PR_NUM_REWARDS);
        report.cumulativePositiveRegulations = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_PR_POS_REGULATIONS);
        report.numPositiveRegulations = typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_PR_NUM_POS_REGULATIONS);
        report.cumulativeNegativeRegulations = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_PR_NEG_REGULATIONS);
        report.numNegativeRegulations = typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_PR_NUM_NEG_REGULATIONS);
        report.screenOffDurationMs = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_PR_SCREEN_OFF_DURATION_MS, 0L);
        report.screenOffDischargeMah = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_PR_SCREEN_OFF_DISCHARGE_MAH, 0L);
        return report;
    }

    private static Ledger.RewardBucket readRewardBucketFromXml(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        Ledger.RewardBucket rewardBucket = new Ledger.RewardBucket();
        rewardBucket.startTimeMs = typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_START_TIME);
        int next = typedXmlPullParser.next();
        while (next != 1) {
            String name = typedXmlPullParser.getName();
            if (next == 3) {
                if (XML_TAG_REWARD_BUCKET.equals(name)) {
                    break;
                }
            } else {
                if (next != 2 || !XML_ATTR_DELTA.equals(name)) {
                    Slog.e(TAG, "Unexpected event: (" + next + ") " + name);
                    return null;
                }
                rewardBucket.cumulativeDelta.put(typedXmlPullParser.getAttributeInt((String) null, XML_ATTR_EVENT_ID), typedXmlPullParser.getAttributeLong((String) null, XML_ATTR_DELTA));
            }
            next = typedXmlPullParser.next();
        }
        return rewardBucket;
    }

    private void scheduleCleanup(long j) {
        if (j == JobStatus.NO_LATEST_RUNTIME) {
            return;
        }
        TareHandlerThread.getHandler().postDelayed(this.mCleanRunnable, Math.max(3600000L, (j + MAX_TRANSACTION_AGE_MS) - System.currentTimeMillis()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeState() {
        FileOutputStream startWrite;
        synchronized (this.mIrs.getLock()) {
            TareHandlerThread.getHandler().removeCallbacks(this.mWriteRunnable);
            TareHandlerThread.getHandler().removeCallbacks(this.mCleanRunnable);
            if (this.mIrs.getEnabledMode() == 0) {
                return;
            }
            long j = JobStatus.NO_LATEST_RUNTIME;
            try {
                startWrite = this.mStateFile.startWrite();
            } catch (IOException e) {
                Slog.e(TAG, "Error writing state to disk", e);
            }
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(startWrite);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                resolveSerializer.startTag((String) null, XML_TAG_TARE);
                resolveSerializer.attributeInt((String) null, XML_ATTR_VERSION, 0);
                resolveSerializer.startTag((String) null, XML_TAG_HIGH_LEVEL_STATE);
                resolveSerializer.attributeLong((String) null, XML_ATTR_LAST_RECLAMATION_TIME, this.mLastReclamationTime);
                resolveSerializer.attributeLong((String) null, XML_ATTR_LAST_STOCK_RECALCULATION_TIME, this.mLastStockRecalculationTime);
                resolveSerializer.attributeLong((String) null, XML_ATTR_TIME_SINCE_FIRST_SETUP_MS, this.mLoadedTimeSinceFirstSetup + SystemClock.elapsedRealtime());
                resolveSerializer.attributeLong((String) null, XML_ATTR_CONSUMPTION_LIMIT, this.mSatiatedConsumptionLimit);
                resolveSerializer.attributeLong((String) null, XML_ATTR_REMAINING_CONSUMABLE_CAKES, this.mRemainingConsumableCakes);
                resolveSerializer.endTag((String) null, XML_TAG_HIGH_LEVEL_STATE);
                for (int numMaps = this.mLedgers.numMaps() - 1; numMaps >= 0; numMaps--) {
                    j = Math.min(j, writeUserLocked(resolveSerializer, this.mLedgers.keyAt(numMaps)));
                }
                List<Analyst.Report> reports = this.mAnalyst.getReports();
                int size = reports.size();
                for (int i = 0; i < size; i++) {
                    writeReport(resolveSerializer, reports.get(i));
                }
                resolveSerializer.endTag((String) null, XML_TAG_TARE);
                resolveSerializer.endDocument();
                this.mStateFile.finishWrite(startWrite);
                if (startWrite != null) {
                    startWrite.close();
                }
                scheduleCleanup(j);
            } catch (Throwable th) {
                if (startWrite != null) {
                    try {
                        startWrite.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    @GuardedBy({"mIrs.getLock()"})
    private long writeUserLocked(TypedXmlSerializer typedXmlSerializer, int i) throws IOException {
        int indexOfKey = this.mLedgers.indexOfKey(i);
        String str = null;
        String str2 = XML_TAG_USER;
        typedXmlSerializer.startTag((String) null, XML_TAG_USER);
        typedXmlSerializer.attributeInt((String) null, "userId", i);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_TIME_SINCE_FIRST_SETUP_MS, this.mRealtimeSinceUsersAddedOffsets.get(i, this.mLoadedTimeSinceFirstSetup) + SystemClock.elapsedRealtime());
        int numElementsForKey = this.mLedgers.numElementsForKey(i) - 1;
        long j = JobStatus.NO_LATEST_RUNTIME;
        while (numElementsForKey >= 0) {
            String str3 = (String) this.mLedgers.keyAt(indexOfKey, numElementsForKey);
            Ledger ledger = (Ledger) this.mLedgers.get(i, str3);
            ledger.removeOldTransactions(MAX_TRANSACTION_AGE_MS);
            typedXmlSerializer.startTag(str, XML_TAG_LEDGER);
            typedXmlSerializer.attribute(str, "pkgName", str3);
            typedXmlSerializer.attributeLong(str, XML_ATTR_CURRENT_BALANCE, ledger.getCurrentBalance());
            List<Ledger.Transaction> transactions = ledger.getTransactions();
            int i2 = 0;
            while (i2 < transactions.size()) {
                Ledger.Transaction transaction = transactions.get(i2);
                String str4 = str2;
                if (i2 == 0) {
                    j = Math.min(j, transaction.endTimeMs);
                }
                writeTransaction(typedXmlSerializer, transaction);
                i2++;
                str2 = str4;
            }
            String str5 = str2;
            List<Ledger.RewardBucket> rewardBuckets = ledger.getRewardBuckets();
            for (int i3 = 0; i3 < rewardBuckets.size(); i3++) {
                writeRewardBucket(typedXmlSerializer, rewardBuckets.get(i3));
            }
            typedXmlSerializer.endTag((String) null, XML_TAG_LEDGER);
            numElementsForKey--;
            str = null;
            str2 = str5;
        }
        typedXmlSerializer.endTag(str, str2);
        return j;
    }

    private static void writeTransaction(TypedXmlSerializer typedXmlSerializer, Ledger.Transaction transaction) throws IOException {
        typedXmlSerializer.startTag((String) null, XML_TAG_TRANSACTION);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_START_TIME, transaction.startTimeMs);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_END_TIME, transaction.endTimeMs);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_EVENT_ID, transaction.eventId);
        String str = transaction.tag;
        if (str != null) {
            typedXmlSerializer.attribute((String) null, XML_ATTR_TAG, str);
        }
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_DELTA, transaction.delta);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_CTP, transaction.ctp);
        typedXmlSerializer.endTag((String) null, XML_TAG_TRANSACTION);
    }

    private static void writeRewardBucket(TypedXmlSerializer typedXmlSerializer, Ledger.RewardBucket rewardBucket) throws IOException {
        int size = rewardBucket.cumulativeDelta.size();
        if (size == 0) {
            return;
        }
        typedXmlSerializer.startTag((String) null, XML_TAG_REWARD_BUCKET);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_START_TIME, rewardBucket.startTimeMs);
        for (int i = 0; i < size; i++) {
            typedXmlSerializer.startTag((String) null, XML_ATTR_DELTA);
            typedXmlSerializer.attributeInt((String) null, XML_ATTR_EVENT_ID, rewardBucket.cumulativeDelta.keyAt(i));
            typedXmlSerializer.attributeLong((String) null, XML_ATTR_DELTA, rewardBucket.cumulativeDelta.valueAt(i));
            typedXmlSerializer.endTag((String) null, XML_ATTR_DELTA);
        }
        typedXmlSerializer.endTag((String) null, XML_TAG_REWARD_BUCKET);
    }

    private static void writeReport(TypedXmlSerializer typedXmlSerializer, Analyst.Report report) throws IOException {
        typedXmlSerializer.startTag((String) null, XML_TAG_PERIOD_REPORT);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_PR_DISCHARGE, report.cumulativeBatteryDischarge);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_PR_BATTERY_LEVEL, report.currentBatteryLevel);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_PR_PROFIT, report.cumulativeProfit);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_PR_NUM_PROFIT, report.numProfitableActions);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_PR_LOSS, report.cumulativeLoss);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_PR_NUM_LOSS, report.numUnprofitableActions);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_PR_REWARDS, report.cumulativeRewards);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_PR_NUM_REWARDS, report.numRewards);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_PR_POS_REGULATIONS, report.cumulativePositiveRegulations);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_PR_NUM_POS_REGULATIONS, report.numPositiveRegulations);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_PR_NEG_REGULATIONS, report.cumulativeNegativeRegulations);
        typedXmlSerializer.attributeInt((String) null, XML_ATTR_PR_NUM_NEG_REGULATIONS, report.numNegativeRegulations);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_PR_SCREEN_OFF_DURATION_MS, report.screenOffDurationMs);
        typedXmlSerializer.attributeLong((String) null, XML_ATTR_PR_SCREEN_OFF_DISCHARGE_MAH, report.screenOffDischargeMah);
        typedXmlSerializer.endTag((String) null, XML_TAG_PERIOD_REPORT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mIrs.getLock()"})
    public void dumpLocked(final IndentingPrintWriter indentingPrintWriter, final boolean z) {
        indentingPrintWriter.println("Ledgers:");
        indentingPrintWriter.increaseIndent();
        this.mLedgers.forEach(new SparseArrayMap.TriConsumer() { // from class: com.android.server.tare.Scribe$$ExternalSyntheticLambda2
            public final void accept(int i, Object obj, Object obj2) {
                Scribe.this.lambda$dumpLocked$0(indentingPrintWriter, z, i, (String) obj, (Ledger) obj2);
            }
        });
        indentingPrintWriter.decreaseIndent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpLocked$0(IndentingPrintWriter indentingPrintWriter, boolean z, int i, String str, Ledger ledger) {
        indentingPrintWriter.print(TareUtils.appToString(i, str));
        if (this.mIrs.isSystem(i, str)) {
            indentingPrintWriter.print(" (system)");
        }
        indentingPrintWriter.println();
        indentingPrintWriter.increaseIndent();
        ledger.dump(indentingPrintWriter, z ? Integer.MAX_VALUE : MAX_NUM_TRANSACTION_DUMP);
        indentingPrintWriter.decreaseIndent();
    }
}
