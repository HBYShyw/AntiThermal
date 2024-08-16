package com.android.server.appop;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.ArraySet;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.AtomicDirectory;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.FgThread;
import com.android.server.app.GameManagerService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class HistoricalRegistry {
    private static final boolean DEBUG = false;
    private static final long DEFAULT_COMPRESSION_STEP = 10;
    private static final int DEFAULT_MODE = 1;
    private static final String HISTORY_FILE_SUFFIX = ".xml";
    private static final int MSG_WRITE_PENDING_HISTORY = 1;
    private static final String PARAMETER_ASSIGNMENT = "=";
    private static final String PARAMETER_DELIMITER = ",";
    private static final String PROPERTY_PERMISSIONS_HUB_ENABLED = "permissions_hub_enabled";

    @GuardedBy({"mInMemoryLock"})
    private long mBaseSnapshotInterval;

    @GuardedBy({"mInMemoryLock"})
    private AppOpsManager.HistoricalOps mCurrentHistoricalOps;
    private volatile DiscreteRegistry mDiscreteRegistry;
    private final Object mInMemoryLock;

    @GuardedBy({"mInMemoryLock"})
    private long mIntervalCompressionMultiplier;

    @GuardedBy({"mInMemoryLock"})
    private int mMode;

    @GuardedBy({"mInMemoryLock"})
    private long mNextPersistDueTimeMillis;
    private final Object mOnDiskLock;

    @GuardedBy({"mInMemoryLock"})
    private long mPendingHistoryOffsetMillis;

    @GuardedBy({"mLock"})
    private LinkedList<AppOpsManager.HistoricalOps> mPendingWrites;

    @GuardedBy({"mOnDiskLock"})
    private Persistence mPersistence;
    private static final boolean KEEP_WTF_LOG = Build.IS_DEBUGGABLE;
    private static final String LOG_TAG = HistoricalRegistry.class.getSimpleName();
    private static final long DEFAULT_SNAPSHOT_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(15);

    /* JADX INFO: Access modifiers changed from: package-private */
    public HistoricalRegistry(Object obj) {
        this.mPendingWrites = new LinkedList<>();
        this.mOnDiskLock = new Object();
        this.mMode = 1;
        this.mBaseSnapshotInterval = DEFAULT_SNAPSHOT_INTERVAL_MILLIS;
        this.mIntervalCompressionMultiplier = DEFAULT_COMPRESSION_STEP;
        this.mInMemoryLock = obj;
        this.mDiscreteRegistry = new DiscreteRegistry(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HistoricalRegistry(HistoricalRegistry historicalRegistry) {
        this(historicalRegistry.mInMemoryLock);
        this.mMode = historicalRegistry.mMode;
        this.mBaseSnapshotInterval = historicalRegistry.mBaseSnapshotInterval;
        this.mIntervalCompressionMultiplier = historicalRegistry.mIntervalCompressionMultiplier;
        this.mDiscreteRegistry = historicalRegistry.mDiscreteRegistry;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady(final ContentResolver contentResolver) {
        this.mDiscreteRegistry.systemReady();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("appop_history_parameters"), false, new ContentObserver(FgThread.getHandler()) { // from class: com.android.server.appop.HistoricalRegistry.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                HistoricalRegistry.this.updateParametersFromSetting(contentResolver);
            }
        });
        updateParametersFromSetting(contentResolver);
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (this.mMode != 0) {
                    if (!isPersistenceInitializedMLocked()) {
                        this.mPersistence = new Persistence(this.mBaseSnapshotInterval, this.mIntervalCompressionMultiplier);
                    }
                    long lastPersistTimeMillisDLocked = this.mPersistence.getLastPersistTimeMillisDLocked();
                    if (lastPersistTimeMillisDLocked > 0) {
                        this.mPendingHistoryOffsetMillis = System.currentTimeMillis() - lastPersistTimeMillisDLocked;
                    }
                }
            }
        }
    }

    private boolean isPersistenceInitializedMLocked() {
        return this.mPersistence != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003d, code lost:
    
        if (r9.equals("baseIntervalMillis") == false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateParametersFromSetting(ContentResolver contentResolver) {
        String string = Settings.Global.getString(contentResolver, "appop_history_parameters");
        if (string == null) {
            return;
        }
        String str = null;
        String str2 = null;
        String str3 = null;
        for (String str4 : string.split(PARAMETER_DELIMITER)) {
            String[] split = str4.split(PARAMETER_ASSIGNMENT);
            char c = 2;
            if (split.length == 2) {
                String trim = split[0].trim();
                trim.hashCode();
                switch (trim.hashCode()) {
                    case -190198682:
                        if (trim.equals("intervalMultiplier")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 3357091:
                        if (trim.equals(GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 245634204:
                        break;
                }
                c = 65535;
                switch (c) {
                    case 0:
                        str3 = split[1].trim();
                        break;
                    case 1:
                        str = split[1].trim();
                        break;
                    case 2:
                        str2 = split[1].trim();
                        break;
                    default:
                        Slog.w(LOG_TAG, "Unknown parameter: " + str4);
                        break;
                }
            }
        }
        if (str != null && str2 != null && str3 != null) {
            try {
                setHistoryParameters(AppOpsManager.parseHistoricalMode(str), Long.parseLong(str2), Integer.parseInt(str3));
                return;
            } catch (NumberFormatException unused) {
            }
        }
        Slog.w(LOG_TAG, "Bad value forappop_history_parameters=" + string + " resetting!");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(String str, PrintWriter printWriter, int i, String str2, String str3, int i2, int i3) {
        if (isApiEnabled()) {
            synchronized (this.mOnDiskLock) {
                synchronized (this.mInMemoryLock) {
                    printWriter.println();
                    printWriter.print(str);
                    printWriter.print("History:");
                    printWriter.print("  mode=");
                    printWriter.println(AppOpsManager.historicalModeToString(this.mMode));
                    StringDumpVisitor stringDumpVisitor = new StringDumpVisitor(str + "  ", printWriter, i, str2, str3, i2, i3);
                    long currentTimeMillis = System.currentTimeMillis();
                    AppOpsManager.HistoricalOps updatedPendingHistoricalOpsMLocked = getUpdatedPendingHistoricalOpsMLocked(currentTimeMillis);
                    makeRelativeToEpochStart(updatedPendingHistoricalOpsMLocked, currentTimeMillis);
                    updatedPendingHistoricalOpsMLocked.accept(stringDumpVisitor);
                    if (!isPersistenceInitializedMLocked()) {
                        Slog.e(LOG_TAG, "Interaction before persistence initialized");
                        return;
                    }
                    List<AppOpsManager.HistoricalOps> readHistoryDLocked = this.mPersistence.readHistoryDLocked();
                    if (readHistoryDLocked != null) {
                        long j = (this.mNextPersistDueTimeMillis - currentTimeMillis) - this.mBaseSnapshotInterval;
                        int size = readHistoryDLocked.size();
                        for (int i4 = 0; i4 < size; i4++) {
                            AppOpsManager.HistoricalOps historicalOps = readHistoryDLocked.get(i4);
                            historicalOps.offsetBeginAndEndTime(j);
                            makeRelativeToEpochStart(historicalOps, currentTimeMillis);
                            historicalOps.accept(stringDumpVisitor);
                        }
                    } else {
                        printWriter.println("  Empty");
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDiscreteData(PrintWriter printWriter, int i, String str, String str2, int i2, int i3, SimpleDateFormat simpleDateFormat, Date date, String str3, int i4) {
        this.mDiscreteRegistry.dump(printWriter, i, str, str2, i2, i3, simpleDateFormat, date, str3, i4);
    }

    int getMode() {
        int i;
        synchronized (this.mInMemoryLock) {
            i = this.mMode;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getHistoricalOpsFromDiskRaw(int i, String str, String str2, String[] strArr, int i2, int i3, long j, long j2, int i4, String[] strArr2, RemoteCallback remoteCallback) {
        if (!isApiEnabled()) {
            remoteCallback.sendResult(new Bundle());
            return;
        }
        AppOpsManager.HistoricalOps historicalOps = new AppOpsManager.HistoricalOps(j, j2);
        if ((i2 & 1) != 0) {
            synchronized (this.mOnDiskLock) {
                try {
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    synchronized (this.mInMemoryLock) {
                        try {
                            if (!isPersistenceInitializedMLocked()) {
                                Slog.e(LOG_TAG, "Interaction before persistence initialized");
                                remoteCallback.sendResult(new Bundle());
                                return;
                            }
                            this.mPersistence.collectHistoricalOpsDLocked(historicalOps, i, str, str2, strArr, i3, j, j2, i4);
                        } catch (Throwable th2) {
                            th = th2;
                            while (true) {
                                try {
                                    break;
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                            }
                            throw th;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    throw th;
                }
            }
        }
        if ((i2 & 2) != 0) {
            this.mDiscreteRegistry.addFilteredDiscreteOpsToHistoricalOps(historicalOps, j, j2, i3, i, str, strArr, str2, i4, new ArraySet(strArr2));
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("historical_ops", historicalOps);
        remoteCallback.sendResult(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getHistoricalOps(int i, String str, String str2, String[] strArr, int i2, int i3, long j, long j2, int i4, String[] strArr2, RemoteCallback remoteCallback) {
        long j3;
        long j4;
        AppOpsManager.HistoricalOps historicalOps;
        Bundle bundle;
        long j5;
        long j6;
        RemoteCallback remoteCallback2;
        AppOpsManager.HistoricalOps historicalOps2;
        long j7;
        if (!isApiEnabled()) {
            remoteCallback.sendResult(new Bundle());
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j8 = j2 == Long.MAX_VALUE ? currentTimeMillis : j2;
        Bundle bundle2 = new Bundle();
        long max = Math.max(currentTimeMillis - j8, 0L);
        long max2 = Math.max(currentTimeMillis - j, 0L);
        AppOpsManager.HistoricalOps historicalOps3 = new AppOpsManager.HistoricalOps(max, max2);
        if ((i2 & 2) != 0) {
            DiscreteRegistry discreteRegistry = this.mDiscreteRegistry;
            j3 = max2;
            ArraySet arraySet = new ArraySet(strArr2);
            j4 = max;
            j5 = j8;
            historicalOps = historicalOps3;
            bundle = bundle2;
            j6 = currentTimeMillis;
            remoteCallback2 = remoteCallback;
            discreteRegistry.addFilteredDiscreteOpsToHistoricalOps(historicalOps3, j, j8, i3, i, str, strArr, str2, i4, arraySet);
        } else {
            j3 = max2;
            j4 = max;
            historicalOps = historicalOps3;
            bundle = bundle2;
            j5 = j8;
            j6 = currentTimeMillis;
            remoteCallback2 = remoteCallback;
        }
        if ((i2 & 1) != 0) {
            synchronized (this.mOnDiskLock) {
                synchronized (this.mInMemoryLock) {
                    if (!isPersistenceInitializedMLocked()) {
                        Slog.e(LOG_TAG, "Interaction before persistence initialized");
                        remoteCallback2.sendResult(new Bundle());
                        return;
                    }
                    long j9 = j6;
                    AppOpsManager.HistoricalOps updatedPendingHistoricalOpsMLocked = getUpdatedPendingHistoricalOpsMLocked(j9);
                    if (j4 >= updatedPendingHistoricalOpsMLocked.getEndTimeMillis() || j3 <= updatedPendingHistoricalOpsMLocked.getBeginTimeMillis()) {
                        historicalOps2 = historicalOps;
                        j7 = j9;
                    } else {
                        AppOpsManager.HistoricalOps historicalOps4 = new AppOpsManager.HistoricalOps(updatedPendingHistoricalOpsMLocked);
                        j7 = j9;
                        historicalOps4.filter(i, str, str2, strArr, i2, i3, j4, j3);
                        historicalOps2 = historicalOps;
                        historicalOps2.merge(historicalOps4);
                    }
                    ArrayList arrayList = new ArrayList(this.mPendingWrites);
                    this.mPendingWrites.clear();
                    boolean z = j3 > updatedPendingHistoricalOpsMLocked.getEndTimeMillis();
                    if (z) {
                        persistPendingHistory(arrayList);
                        long j10 = (j7 - this.mNextPersistDueTimeMillis) + this.mBaseSnapshotInterval;
                        this.mPersistence.collectHistoricalOpsDLocked(historicalOps2, i, str, str2, strArr, i3, Math.max(j4 - j10, 0L), Math.max(j3 - j10, 0L), i4);
                    }
                }
            }
        } else {
            historicalOps2 = historicalOps;
        }
        historicalOps2.setBeginAndEndTime(j, j5);
        Bundle bundle3 = bundle;
        bundle3.putParcelable("historical_ops", historicalOps2);
        remoteCallback.sendResult(bundle3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementOpAccessedCount(int i, int i2, String str, String str2, int i3, int i4, long j, int i5, int i6) {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 1) {
                if (!isPersistenceInitializedMLocked()) {
                    Slog.v(LOG_TAG, "Interaction before persistence initialized");
                } else {
                    getUpdatedPendingHistoricalOpsMLocked(System.currentTimeMillis()).increaseAccessCount(i, i2, str, str2, i3, i4, 1L);
                    this.mDiscreteRegistry.recordDiscreteAccess(i2, str, i, str2, i4, i3, j, -1L, i5, i6);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementOpRejected(int i, int i2, String str, String str2, int i3, int i4) {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 1) {
                if (!isPersistenceInitializedMLocked()) {
                    Slog.v(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                getUpdatedPendingHistoricalOpsMLocked(System.currentTimeMillis()).increaseRejectCount(i, i2, str, str2, i3, i4, 1L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void increaseOpAccessDuration(int i, int i2, String str, String str2, int i3, int i4, long j, long j2, int i5, int i6) {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 1) {
                if (!isPersistenceInitializedMLocked()) {
                    Slog.v(LOG_TAG, "Interaction before persistence initialized");
                } else {
                    getUpdatedPendingHistoricalOpsMLocked(System.currentTimeMillis()).increaseAccessDuration(i, i2, str, str2, i3, i4, j2);
                    this.mDiscreteRegistry.recordDiscreteAccess(i2, str, i, str2, i4, i3, j, j2, i5, i6);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHistoryParameters(int i, long j, long j2) {
        boolean z;
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                Slog.i(LOG_TAG, "New history parameters: mode:" + AppOpsManager.historicalModeToString(i) + " baseSnapshotInterval:" + j + " intervalCompressionMultiplier:" + j2);
                boolean z2 = true;
                if (this.mMode != i) {
                    this.mMode = i;
                    if (i == 0) {
                        clearHistoryOnDiskDLocked();
                    }
                    if (this.mMode == 2) {
                        this.mDiscreteRegistry.setDebugMode(true);
                    }
                }
                if (this.mBaseSnapshotInterval != j) {
                    this.mBaseSnapshotInterval = j;
                    z = true;
                } else {
                    z = false;
                }
                if (this.mIntervalCompressionMultiplier != j2) {
                    this.mIntervalCompressionMultiplier = j2;
                } else {
                    z2 = z;
                }
                if (z2) {
                    resampleHistoryOnDiskInMemoryDMLocked(0L);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void offsetHistory(long j) {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (!isPersistenceInitializedMLocked()) {
                    Slog.e(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                List<AppOpsManager.HistoricalOps> readHistoryDLocked = this.mPersistence.readHistoryDLocked();
                clearHistoricalRegistry();
                if (readHistoryDLocked != null) {
                    int size = readHistoryDLocked.size();
                    for (int i = 0; i < size; i++) {
                        readHistoryDLocked.get(i).offsetBeginAndEndTime(j);
                    }
                    if (j < 0) {
                        pruneFutureOps(readHistoryDLocked);
                    }
                    this.mPersistence.persistHistoricalOpsDLocked(readHistoryDLocked);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void offsetDiscreteHistory(long j) {
        this.mDiscreteRegistry.offsetHistory(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addHistoricalOps(AppOpsManager.HistoricalOps historicalOps) {
        synchronized (this.mInMemoryLock) {
            if (!isPersistenceInitializedMLocked()) {
                Slog.d(LOG_TAG, "Interaction before persistence initialized");
                return;
            }
            historicalOps.offsetBeginAndEndTime(this.mBaseSnapshotInterval);
            this.mPendingWrites.offerFirst(historicalOps);
            ArrayList arrayList = new ArrayList(this.mPendingWrites);
            this.mPendingWrites.clear();
            persistPendingHistory(arrayList);
        }
    }

    private void resampleHistoryOnDiskInMemoryDMLocked(long j) {
        this.mPersistence = new Persistence(this.mBaseSnapshotInterval, this.mIntervalCompressionMultiplier);
        offsetHistory(j);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetHistoryParameters() {
        if (!isPersistenceInitializedMLocked()) {
            Slog.d(LOG_TAG, "Interaction before persistence initialized");
        } else {
            setHistoryParameters(1, DEFAULT_SNAPSHOT_INTERVAL_MILLIS, DEFAULT_COMPRESSION_STEP);
            this.mDiscreteRegistry.setDebugMode(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearHistory(int i, String str) {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (!isPersistenceInitializedMLocked()) {
                    Slog.d(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                if (this.mMode != 1) {
                    return;
                }
                for (int i2 = 0; i2 < this.mPendingWrites.size(); i2++) {
                    this.mPendingWrites.get(i2).clearHistory(i, str);
                }
                getUpdatedPendingHistoricalOpsMLocked(System.currentTimeMillis()).clearHistory(i, str);
                this.mPersistence.clearHistoryDLocked(i, str);
                this.mDiscreteRegistry.clearHistory(i, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeAndClearDiscreteHistory() {
        this.mDiscreteRegistry.writeAndClearAccessHistory();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearAllHistory() {
        clearHistoricalRegistry();
        this.mDiscreteRegistry.clearHistory();
    }

    void clearHistoricalRegistry() {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                if (!isPersistenceInitializedMLocked()) {
                    Slog.d(LOG_TAG, "Interaction before persistence initialized");
                    return;
                }
                clearHistoryOnDiskDLocked();
                this.mNextPersistDueTimeMillis = 0L;
                this.mPendingHistoryOffsetMillis = 0L;
                this.mCurrentHistoricalOps = null;
            }
        }
    }

    private void clearHistoryOnDiskDLocked() {
        BackgroundThread.getHandler().removeMessages(1);
        synchronized (this.mInMemoryLock) {
            this.mCurrentHistoricalOps = null;
            this.mNextPersistDueTimeMillis = System.currentTimeMillis();
            this.mPendingWrites.clear();
        }
        Persistence.clearHistoryDLocked();
    }

    private AppOpsManager.HistoricalOps getUpdatedPendingHistoricalOpsMLocked(long j) {
        AppOpsManager.HistoricalOps historicalOps = this.mCurrentHistoricalOps;
        if (historicalOps != null) {
            long j2 = this.mNextPersistDueTimeMillis - j;
            long j3 = this.mBaseSnapshotInterval;
            if (j2 > j3) {
                this.mPendingHistoryOffsetMillis = j2 - j3;
            }
            historicalOps.setEndTime(j3 - j2);
            if (j2 > 0) {
                return this.mCurrentHistoricalOps;
            }
            if (this.mCurrentHistoricalOps.isEmpty()) {
                this.mCurrentHistoricalOps.setBeginAndEndTime(0L, 0L);
                this.mNextPersistDueTimeMillis = j + this.mBaseSnapshotInterval;
                return this.mCurrentHistoricalOps;
            }
            this.mCurrentHistoricalOps.offsetBeginAndEndTime(this.mBaseSnapshotInterval);
            AppOpsManager.HistoricalOps historicalOps2 = this.mCurrentHistoricalOps;
            historicalOps2.setBeginTime(historicalOps2.getEndTimeMillis() - this.mBaseSnapshotInterval);
            this.mCurrentHistoricalOps.offsetBeginAndEndTime(Math.abs(j2));
            schedulePersistHistoricalOpsMLocked(this.mCurrentHistoricalOps);
        }
        AppOpsManager.HistoricalOps historicalOps3 = new AppOpsManager.HistoricalOps(0L, 0L);
        this.mCurrentHistoricalOps = historicalOps3;
        this.mNextPersistDueTimeMillis = j + this.mBaseSnapshotInterval;
        return historicalOps3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void shutdown() {
        synchronized (this.mInMemoryLock) {
            if (this.mMode == 0) {
                return;
            }
            persistPendingHistory();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void persistPendingHistory() {
        ArrayList arrayList;
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                arrayList = new ArrayList(this.mPendingWrites);
                this.mPendingWrites.clear();
                long j = this.mPendingHistoryOffsetMillis;
                if (j != 0) {
                    resampleHistoryOnDiskInMemoryDMLocked(j);
                    this.mPendingHistoryOffsetMillis = 0L;
                }
            }
            persistPendingHistory(arrayList);
        }
        this.mDiscreteRegistry.writeAndClearAccessHistory();
    }

    private void persistPendingHistory(List<AppOpsManager.HistoricalOps> list) {
        synchronized (this.mOnDiskLock) {
            BackgroundThread.getHandler().removeMessages(1);
            if (list.isEmpty()) {
                return;
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                AppOpsManager.HistoricalOps historicalOps = list.get(i);
                if (i > 0) {
                    historicalOps.offsetBeginAndEndTime(list.get(i - 1).getBeginTimeMillis());
                }
            }
            this.mPersistence.persistHistoricalOpsDLocked(list);
        }
    }

    private void schedulePersistHistoricalOpsMLocked(AppOpsManager.HistoricalOps historicalOps) {
        Message obtainMessage = PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.appop.HistoricalRegistry$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((HistoricalRegistry) obj).persistPendingHistory();
            }
        }, this);
        obtainMessage.what = 1;
        BackgroundThread.getHandler().sendMessage(obtainMessage);
        this.mPendingWrites.offerFirst(historicalOps);
    }

    private static void makeRelativeToEpochStart(AppOpsManager.HistoricalOps historicalOps, long j) {
        historicalOps.setBeginAndEndTime(j - historicalOps.getEndTimeMillis(), j - historicalOps.getBeginTimeMillis());
    }

    private void pruneFutureOps(List<AppOpsManager.HistoricalOps> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            AppOpsManager.HistoricalOps historicalOps = list.get(size);
            if (historicalOps.getEndTimeMillis() <= this.mBaseSnapshotInterval) {
                list.remove(size);
            } else if (historicalOps.getBeginTimeMillis() < this.mBaseSnapshotInterval) {
                Persistence.spliceFromBeginning(historicalOps, (historicalOps.getEndTimeMillis() - this.mBaseSnapshotInterval) / historicalOps.getDurationMillis());
            }
        }
    }

    private static boolean isApiEnabled() {
        return Binder.getCallingUid() == Process.myUid() || DeviceConfig.getBoolean("privacy", PROPERTY_PERMISSIONS_HUB_ENABLED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class Persistence {
        private static final String ATTR_ACCESS_COUNT = "ac";
        private static final String ATTR_ACCESS_DURATION = "du";
        private static final String ATTR_BEGIN_TIME = "beg";
        private static final String ATTR_END_TIME = "end";
        private static final String ATTR_NAME = "na";
        private static final String ATTR_OVERFLOW = "ov";
        private static final String ATTR_REJECT_COUNT = "rc";
        private static final String ATTR_VERSION = "ver";
        private static final int CURRENT_VERSION = 2;
        private static final boolean DEBUG = false;
        private static final String LOG_TAG = "Persistence";
        private static final String TAG_ATTRIBUTION = "ftr";
        private static final String TAG_OP = "op";
        private static final String TAG_OPS = "ops";
        private static final String TAG_PACKAGE = "pkg";
        private static final String TAG_STATE = "st";
        private static final String TAG_UID = "uid";
        private final long mBaseSnapshotInterval;
        private final long mIntervalCompressionMultiplier;
        private static final String TAG_HISTORY = "history";
        private static final AtomicDirectory sHistoricalAppOpsDir = new AtomicDirectory(new File(new File(Environment.getDataSystemDirectory(), "appops"), TAG_HISTORY));

        Persistence(long j, long j2) {
            this.mBaseSnapshotInterval = j;
            this.mIntervalCompressionMultiplier = j2;
        }

        private File generateFile(File file, int i) {
            return new File(file, Long.toString(computeGlobalIntervalBeginMillis(i)) + HistoricalRegistry.HISTORY_FILE_SUFFIX);
        }

        void clearHistoryDLocked(int i, String str) {
            List<AppOpsManager.HistoricalOps> readHistoryDLocked = readHistoryDLocked();
            if (readHistoryDLocked == null) {
                return;
            }
            for (int i2 = 0; i2 < readHistoryDLocked.size(); i2++) {
                readHistoryDLocked.get(i2).clearHistory(i, str);
            }
            clearHistoryDLocked();
            persistHistoricalOpsDLocked(readHistoryDLocked);
        }

        static void clearHistoryDLocked() {
            sHistoricalAppOpsDir.delete();
        }

        void persistHistoricalOpsDLocked(List<AppOpsManager.HistoricalOps> list) {
            try {
                AtomicDirectory atomicDirectory = sHistoricalAppOpsDir;
                File startWrite = atomicDirectory.startWrite();
                File backupDirectory = atomicDirectory.getBackupDirectory();
                handlePersistHistoricalOpsRecursiveDLocked(startWrite, backupDirectory, list, getHistoricalFileNames(backupDirectory), 0);
                atomicDirectory.finishWrite();
            } catch (Throwable th) {
                HistoricalRegistry.wtf("Failed to write historical app ops, restoring backup", th, null);
                sHistoricalAppOpsDir.failWrite();
            }
        }

        List<AppOpsManager.HistoricalOps> readHistoryRawDLocked() {
            return collectHistoricalOpsBaseDLocked(-1, null, null, null, 0, 0L, Long.MAX_VALUE, 31);
        }

        List<AppOpsManager.HistoricalOps> readHistoryDLocked() {
            List<AppOpsManager.HistoricalOps> readHistoryRawDLocked = readHistoryRawDLocked();
            if (readHistoryRawDLocked != null) {
                int size = readHistoryRawDLocked.size();
                for (int i = 0; i < size; i++) {
                    readHistoryRawDLocked.get(i).offsetBeginAndEndTime(this.mBaseSnapshotInterval);
                }
            }
            return readHistoryRawDLocked;
        }

        long getLastPersistTimeMillisDLocked() {
            File file;
            Throwable th;
            AtomicDirectory atomicDirectory;
            File[] listFiles;
            File file2 = null;
            try {
                atomicDirectory = sHistoricalAppOpsDir;
                file = atomicDirectory.startRead();
            } catch (Throwable th2) {
                file = null;
                th = th2;
            }
            try {
                listFiles = file.listFiles();
            } catch (Throwable th3) {
                th = th3;
                HistoricalRegistry.wtf("Error reading historical app ops. Deleting history.", th, file);
                sHistoricalAppOpsDir.delete();
                return 0L;
            }
            if (listFiles != null && listFiles.length > 0) {
                for (File file3 : listFiles) {
                    String name = file3.getName();
                    if (name.endsWith(HistoricalRegistry.HISTORY_FILE_SUFFIX)) {
                        if (file2 != null && name.length() >= file2.getName().length()) {
                        }
                        file2 = file3;
                    }
                }
                if (file2 == null) {
                    return 0L;
                }
                return file2.lastModified();
            }
            atomicDirectory.finishRead();
            return 0L;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void collectHistoricalOpsDLocked(AppOpsManager.HistoricalOps historicalOps, int i, String str, String str2, String[] strArr, int i2, long j, long j2, int i3) {
            LinkedList<AppOpsManager.HistoricalOps> collectHistoricalOpsBaseDLocked = collectHistoricalOpsBaseDLocked(i, str, str2, strArr, i2, j, j2, i3);
            if (collectHistoricalOpsBaseDLocked != null) {
                int size = collectHistoricalOpsBaseDLocked.size();
                for (int i4 = 0; i4 < size; i4++) {
                    historicalOps.merge(collectHistoricalOpsBaseDLocked.get(i4));
                }
            }
        }

        private LinkedList<AppOpsManager.HistoricalOps> collectHistoricalOpsBaseDLocked(int i, String str, String str2, String[] strArr, int i2, long j, long j2, int i3) {
            Throwable th;
            File file;
            AtomicDirectory atomicDirectory;
            File startRead;
            try {
                atomicDirectory = sHistoricalAppOpsDir;
                startRead = atomicDirectory.startRead();
            } catch (Throwable th2) {
                th = th2;
                file = null;
            }
            try {
                LinkedList<AppOpsManager.HistoricalOps> collectHistoricalOpsRecursiveDLocked = collectHistoricalOpsRecursiveDLocked(startRead, i, str, str2, strArr, i2, j, j2, i3, new long[]{0}, null, 0, getHistoricalFileNames(startRead));
                atomicDirectory.finishRead();
                return collectHistoricalOpsRecursiveDLocked;
            } catch (Throwable th3) {
                th = th3;
                file = startRead;
                HistoricalRegistry.wtf("Error reading historical app ops. Deleting history.", th, file);
                sHistoricalAppOpsDir.delete();
                return null;
            }
        }

        private LinkedList<AppOpsManager.HistoricalOps> collectHistoricalOpsRecursiveDLocked(File file, int i, String str, String str2, String[] strArr, int i2, long j, long j2, int i3, long[] jArr, LinkedList<AppOpsManager.HistoricalOps> linkedList, int i4, Set<String> set) throws IOException, XmlPullParserException {
            long pow = ((long) Math.pow(this.mIntervalCompressionMultiplier, i4)) * this.mBaseSnapshotInterval;
            int i5 = i4 + 1;
            long pow2 = this.mBaseSnapshotInterval * ((long) Math.pow(this.mIntervalCompressionMultiplier, i5));
            long max = Math.max(j - pow, 0L);
            long j3 = j2 - pow;
            List<AppOpsManager.HistoricalOps> readHistoricalOpsLocked = readHistoricalOpsLocked(file, pow, pow2, i, str, str2, strArr, i2, max, j3, i3, jArr, i4, set);
            if (readHistoricalOpsLocked != null && readHistoricalOpsLocked.isEmpty()) {
                return linkedList;
            }
            LinkedList<AppOpsManager.HistoricalOps> collectHistoricalOpsRecursiveDLocked = collectHistoricalOpsRecursiveDLocked(file, i, str, str2, strArr, i2, max, j3, i3, jArr, linkedList, i5, set);
            if (collectHistoricalOpsRecursiveDLocked != null) {
                int size = collectHistoricalOpsRecursiveDLocked.size();
                for (int i6 = 0; i6 < size; i6++) {
                    collectHistoricalOpsRecursiveDLocked.get(i6).offsetBeginAndEndTime(pow2);
                }
            }
            if (readHistoricalOpsLocked != null) {
                if (collectHistoricalOpsRecursiveDLocked == null) {
                    collectHistoricalOpsRecursiveDLocked = new LinkedList<>();
                }
                for (int size2 = readHistoricalOpsLocked.size() - 1; size2 >= 0; size2--) {
                    collectHistoricalOpsRecursiveDLocked.offerFirst(readHistoricalOpsLocked.get(size2));
                }
            }
            return collectHistoricalOpsRecursiveDLocked;
        }

        private void handlePersistHistoricalOpsRecursiveDLocked(File file, File file2, List<AppOpsManager.HistoricalOps> list, Set<String> set, int i) throws IOException, XmlPullParserException {
            List<AppOpsManager.HistoricalOps> list2;
            long j;
            AppOpsManager.HistoricalOps historicalOps;
            int size;
            long pow = ((long) Math.pow(this.mIntervalCompressionMultiplier, i)) * this.mBaseSnapshotInterval;
            int i2 = i + 1;
            long pow2 = ((long) Math.pow(this.mIntervalCompressionMultiplier, i2)) * this.mBaseSnapshotInterval;
            if (list == null || list.isEmpty()) {
                if (set.isEmpty()) {
                    return;
                }
                File generateFile = generateFile(file2, i);
                if (set.remove(generateFile.getName())) {
                    Files.createLink(generateFile(file, i).toPath(), generateFile.toPath());
                }
                handlePersistHistoricalOpsRecursiveDLocked(file, file2, list, set, i2);
                return;
            }
            int size2 = list.size();
            for (int i3 = 0; i3 < size2; i3++) {
                list.get(i3).offsetBeginAndEndTime(-pow);
            }
            long j2 = pow;
            List<AppOpsManager.HistoricalOps> readHistoricalOpsLocked = readHistoricalOpsLocked(file2, j2, pow2, -1, null, null, null, 0, Long.MIN_VALUE, Long.MAX_VALUE, 31, null, i, null);
            if (readHistoricalOpsLocked == null || (size = readHistoricalOpsLocked.size()) <= 0) {
                list2 = list;
            } else {
                list2 = list;
                long endTimeMillis = list2.get(list.size() - 1).getEndTimeMillis();
                for (int i4 = 0; i4 < size; i4++) {
                    readHistoricalOpsLocked.get(i4).offsetBeginAndEndTime(endTimeMillis);
                }
            }
            LinkedList linkedList = new LinkedList(list2);
            if (readHistoricalOpsLocked != null) {
                linkedList.addAll(readHistoricalOpsLocked);
            }
            int size3 = linkedList.size();
            long j3 = 0;
            ArrayList arrayList = null;
            ArrayList arrayList2 = null;
            int i5 = 0;
            while (i5 < size3) {
                AppOpsManager.HistoricalOps historicalOps2 = (AppOpsManager.HistoricalOps) linkedList.get(i5);
                if (historicalOps2.getEndTimeMillis() <= pow2) {
                    historicalOps = null;
                    j = j2;
                } else if (historicalOps2.getBeginTimeMillis() < pow2) {
                    j3 = historicalOps2.getEndTimeMillis() - pow2;
                    j = j2;
                    if (j3 > j) {
                        historicalOps = spliceFromEnd(historicalOps2, j3 / historicalOps2.getDurationMillis());
                        j3 = historicalOps2.getEndTimeMillis() - pow2;
                    } else {
                        historicalOps = null;
                    }
                } else {
                    j = j2;
                    historicalOps = historicalOps2;
                    historicalOps2 = null;
                }
                if (historicalOps2 != null) {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add(historicalOps2);
                }
                if (historicalOps != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(historicalOps);
                }
                i5++;
                j2 = j;
            }
            long j4 = j2;
            File generateFile2 = generateFile(file, i);
            set.remove(generateFile2.getName());
            if (arrayList2 != null) {
                normalizeSnapshotForSlotDuration(arrayList2, j4);
                writeHistoricalOpsDLocked(arrayList2, j3, generateFile2);
            }
            handlePersistHistoricalOpsRecursiveDLocked(file, file2, arrayList, set, i2);
        }

        private List<AppOpsManager.HistoricalOps> readHistoricalOpsLocked(File file, long j, long j2, int i, String str, String str2, String[] strArr, int i2, long j3, long j4, int i3, long[] jArr, int i4, Set<String> set) throws IOException, XmlPullParserException {
            File generateFile = generateFile(file, i4);
            if (set != null) {
                set.remove(generateFile.getName());
            }
            if (j3 >= j4 || j4 < j) {
                return Collections.emptyList();
            }
            if (j3 >= j2 + ((j2 - j) / this.mIntervalCompressionMultiplier) + (jArr != null ? jArr[0] : 0L) || !generateFile.exists()) {
                if (set == null || set.isEmpty()) {
                    return Collections.emptyList();
                }
                return null;
            }
            return readHistoricalOpsLocked(generateFile, i, str, str2, strArr, i2, j3, j4, i3, jArr);
        }

        private List<AppOpsManager.HistoricalOps> readHistoricalOpsLocked(File file, int i, String str, String str2, String[] strArr, int i2, long j, long j2, int i3, long[] jArr) throws IOException, XmlPullParserException {
            int i4;
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                    XmlUtils.beginDocument(resolvePullParser, TAG_HISTORY);
                    if (resolvePullParser.getAttributeInt((String) null, ATTR_VERSION) < 2) {
                        throw new IllegalStateException("Dropping unsupported history version 1 for file:" + file);
                    }
                    long attributeLong = resolvePullParser.getAttributeLong((String) null, ATTR_OVERFLOW, 0L);
                    int depth = resolvePullParser.getDepth();
                    ArrayList arrayList = null;
                    while (XmlUtils.nextElementWithin(resolvePullParser, depth)) {
                        if (TAG_OPS.equals(resolvePullParser.getName())) {
                            i4 = depth;
                            AppOpsManager.HistoricalOps readeHistoricalOpsDLocked = readeHistoricalOpsDLocked(resolvePullParser, i, str, str2, strArr, i2, j, j2, i3, jArr);
                            if (readeHistoricalOpsDLocked != null) {
                                if (readeHistoricalOpsDLocked.isEmpty()) {
                                    XmlUtils.skipCurrentTag(resolvePullParser);
                                } else {
                                    ArrayList arrayList2 = arrayList == null ? new ArrayList() : arrayList;
                                    arrayList2.add(readeHistoricalOpsDLocked);
                                    arrayList = arrayList2;
                                }
                            }
                        } else {
                            i4 = depth;
                        }
                        depth = i4;
                    }
                    if (jArr != null) {
                        jArr[0] = jArr[0] + attributeLong;
                    }
                    fileInputStream.close();
                    return arrayList;
                } finally {
                }
            } catch (FileNotFoundException unused) {
                Slog.i(LOG_TAG, "No history file: " + file.getName());
                return Collections.emptyList();
            }
        }

        private AppOpsManager.HistoricalOps readeHistoricalOpsDLocked(TypedXmlPullParser typedXmlPullParser, int i, String str, String str2, String[] strArr, int i2, long j, long j2, int i3, long[] jArr) throws IOException, XmlPullParserException {
            TypedXmlPullParser typedXmlPullParser2 = typedXmlPullParser;
            long attributeLong = typedXmlPullParser2.getAttributeLong((String) null, ATTR_BEGIN_TIME, 0L) + (jArr != null ? jArr[0] : 0L);
            long attributeLong2 = typedXmlPullParser2.getAttributeLong((String) null, ATTR_END_TIME, 0L) + (jArr != null ? jArr[0] : 0L);
            if (j2 < attributeLong) {
                return null;
            }
            if (j > attributeLong2) {
                return new AppOpsManager.HistoricalOps(0L, 0L);
            }
            long max = Math.max(attributeLong, j);
            long min = Math.min(attributeLong2, j2);
            double d = (min - max) / (attributeLong2 - attributeLong);
            int depth = typedXmlPullParser.getDepth();
            AppOpsManager.HistoricalOps historicalOps = null;
            while (XmlUtils.nextElementWithin(typedXmlPullParser2, depth)) {
                if (TAG_UID.equals(typedXmlPullParser.getName())) {
                    long j3 = min;
                    int i4 = depth;
                    AppOpsManager.HistoricalOps historicalOps2 = historicalOps;
                    historicalOps = historicalOps2 == null ? readHistoricalUidOpsDLocked(historicalOps, typedXmlPullParser, i, str, str2, strArr, i2, i3, d) : historicalOps2;
                    typedXmlPullParser2 = typedXmlPullParser;
                    min = j3;
                    depth = i4;
                } else {
                    typedXmlPullParser2 = typedXmlPullParser;
                }
            }
            long j4 = min;
            AppOpsManager.HistoricalOps historicalOps3 = historicalOps;
            if (historicalOps3 != null) {
                historicalOps3.setBeginAndEndTime(max, j4);
            }
            return historicalOps3;
        }

        private AppOpsManager.HistoricalOps readHistoricalUidOpsDLocked(AppOpsManager.HistoricalOps historicalOps, TypedXmlPullParser typedXmlPullParser, int i, String str, String str2, String[] strArr, int i2, int i3, double d) throws IOException, XmlPullParserException {
            int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_NAME);
            if ((i2 & 1) != 0 && i != attributeInt) {
                XmlUtils.skipCurrentTag(typedXmlPullParser);
                return null;
            }
            int depth = typedXmlPullParser.getDepth();
            AppOpsManager.HistoricalOps historicalOps2 = historicalOps;
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if (TAG_PACKAGE.equals(typedXmlPullParser.getName())) {
                    AppOpsManager.HistoricalOps readHistoricalPackageOpsDLocked = readHistoricalPackageOpsDLocked(historicalOps2, attributeInt, typedXmlPullParser, str, str2, strArr, i2, i3, d);
                    if (historicalOps2 == null) {
                        historicalOps2 = readHistoricalPackageOpsDLocked;
                    }
                }
            }
            return historicalOps2;
        }

        private AppOpsManager.HistoricalOps readHistoricalPackageOpsDLocked(AppOpsManager.HistoricalOps historicalOps, int i, TypedXmlPullParser typedXmlPullParser, String str, String str2, String[] strArr, int i2, int i3, double d) throws IOException, XmlPullParserException {
            String readStringAttribute = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_NAME);
            if ((i2 & 2) != 0 && !str.equals(readStringAttribute)) {
                XmlUtils.skipCurrentTag(typedXmlPullParser);
                return null;
            }
            int depth = typedXmlPullParser.getDepth();
            AppOpsManager.HistoricalOps historicalOps2 = historicalOps;
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if (TAG_ATTRIBUTION.equals(typedXmlPullParser.getName())) {
                    AppOpsManager.HistoricalOps readHistoricalAttributionOpsDLocked = readHistoricalAttributionOpsDLocked(historicalOps2, i, readStringAttribute, typedXmlPullParser, str2, strArr, i2, i3, d);
                    if (historicalOps2 == null) {
                        historicalOps2 = readHistoricalAttributionOpsDLocked;
                    }
                }
            }
            return historicalOps2;
        }

        private AppOpsManager.HistoricalOps readHistoricalAttributionOpsDLocked(AppOpsManager.HistoricalOps historicalOps, int i, String str, TypedXmlPullParser typedXmlPullParser, String str2, String[] strArr, int i2, int i3, double d) throws IOException, XmlPullParserException {
            String readStringAttribute = XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_NAME);
            if ((i2 & 4) != 0 && !Objects.equals(str2, readStringAttribute)) {
                XmlUtils.skipCurrentTag(typedXmlPullParser);
                return null;
            }
            int depth = typedXmlPullParser.getDepth();
            AppOpsManager.HistoricalOps historicalOps2 = historicalOps;
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if (TAG_OP.equals(typedXmlPullParser.getName())) {
                    AppOpsManager.HistoricalOps readHistoricalOpDLocked = readHistoricalOpDLocked(historicalOps2, i, str, readStringAttribute, typedXmlPullParser, strArr, i2, i3, d);
                    if (historicalOps2 == null) {
                        historicalOps2 = readHistoricalOpDLocked;
                    }
                }
            }
            return historicalOps2;
        }

        private AppOpsManager.HistoricalOps readHistoricalOpDLocked(AppOpsManager.HistoricalOps historicalOps, int i, String str, String str2, TypedXmlPullParser typedXmlPullParser, String[] strArr, int i2, int i3, double d) throws IOException, XmlPullParserException {
            int attributeInt = typedXmlPullParser.getAttributeInt((String) null, ATTR_NAME);
            if ((i2 & 8) != 0 && !ArrayUtils.contains(strArr, AppOpsManager.opToPublicName(attributeInt))) {
                XmlUtils.skipCurrentTag(typedXmlPullParser);
                return null;
            }
            int depth = typedXmlPullParser.getDepth();
            AppOpsManager.HistoricalOps historicalOps2 = historicalOps;
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if (TAG_STATE.equals(typedXmlPullParser.getName())) {
                    AppOpsManager.HistoricalOps readStateDLocked = readStateDLocked(historicalOps2, i, str, str2, attributeInt, typedXmlPullParser, i3, d);
                    if (historicalOps2 == null) {
                        historicalOps2 = readStateDLocked;
                    }
                }
            }
            return historicalOps2;
        }

        private AppOpsManager.HistoricalOps readStateDLocked(AppOpsManager.HistoricalOps historicalOps, int i, String str, String str2, int i2, TypedXmlPullParser typedXmlPullParser, int i3, double d) throws IOException, XmlPullParserException {
            AppOpsManager.HistoricalOps historicalOps2;
            long attributeLong = typedXmlPullParser.getAttributeLong((String) null, ATTR_NAME);
            int extractFlagsFromKey = AppOpsManager.extractFlagsFromKey(attributeLong) & i3;
            if (extractFlagsFromKey == 0) {
                return null;
            }
            int extractUidStateFromKey = AppOpsManager.extractUidStateFromKey(attributeLong);
            long attributeLong2 = typedXmlPullParser.getAttributeLong((String) null, ATTR_ACCESS_COUNT, 0L);
            if (attributeLong2 > 0) {
                if (!Double.isNaN(d)) {
                    attributeLong2 = (long) AppOpsManager.HistoricalOps.round(attributeLong2 * d);
                }
                long j = attributeLong2;
                historicalOps2 = historicalOps == null ? new AppOpsManager.HistoricalOps(0L, 0L) : historicalOps;
                historicalOps2.increaseAccessCount(i2, i, str, str2, extractUidStateFromKey, extractFlagsFromKey, j);
            } else {
                historicalOps2 = historicalOps;
            }
            long attributeLong3 = typedXmlPullParser.getAttributeLong((String) null, ATTR_REJECT_COUNT, 0L);
            if (attributeLong3 > 0) {
                if (!Double.isNaN(d)) {
                    attributeLong3 = (long) AppOpsManager.HistoricalOps.round(attributeLong3 * d);
                }
                long j2 = attributeLong3;
                if (historicalOps2 == null) {
                    historicalOps2 = new AppOpsManager.HistoricalOps(0L, 0L);
                }
                historicalOps2.increaseRejectCount(i2, i, str, str2, extractUidStateFromKey, extractFlagsFromKey, j2);
            }
            long attributeLong4 = typedXmlPullParser.getAttributeLong((String) null, ATTR_ACCESS_DURATION, 0L);
            if (attributeLong4 > 0) {
                if (!Double.isNaN(d)) {
                    attributeLong4 = (long) AppOpsManager.HistoricalOps.round(attributeLong4 * d);
                }
                long j3 = attributeLong4;
                if (historicalOps2 == null) {
                    historicalOps2 = new AppOpsManager.HistoricalOps(0L, 0L);
                }
                historicalOps2.increaseAccessDuration(i2, i, str, str2, extractUidStateFromKey, extractFlagsFromKey, j3);
            }
            return historicalOps2;
        }

        private void writeHistoricalOpsDLocked(List<AppOpsManager.HistoricalOps> list, long j, File file) throws IOException {
            FileOutputStream openWrite = sHistoricalAppOpsDir.openWrite(file);
            try {
                TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(openWrite);
                resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                resolveSerializer.startDocument((String) null, Boolean.TRUE);
                resolveSerializer.startTag((String) null, TAG_HISTORY);
                resolveSerializer.attributeInt((String) null, ATTR_VERSION, 2);
                if (j != 0) {
                    resolveSerializer.attributeLong((String) null, ATTR_OVERFLOW, j);
                }
                if (list != null) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        writeHistoricalOpDLocked(list.get(i), resolveSerializer);
                    }
                }
                resolveSerializer.endTag((String) null, TAG_HISTORY);
                resolveSerializer.endDocument();
                sHistoricalAppOpsDir.closeWrite(openWrite);
            } catch (IOException e) {
                sHistoricalAppOpsDir.failWrite(openWrite);
                throw e;
            }
        }

        private void writeHistoricalOpDLocked(AppOpsManager.HistoricalOps historicalOps, TypedXmlSerializer typedXmlSerializer) throws IOException {
            typedXmlSerializer.startTag((String) null, TAG_OPS);
            typedXmlSerializer.attributeLong((String) null, ATTR_BEGIN_TIME, historicalOps.getBeginTimeMillis());
            typedXmlSerializer.attributeLong((String) null, ATTR_END_TIME, historicalOps.getEndTimeMillis());
            int uidCount = historicalOps.getUidCount();
            for (int i = 0; i < uidCount; i++) {
                writeHistoricalUidOpsDLocked(historicalOps.getUidOpsAt(i), typedXmlSerializer);
            }
            typedXmlSerializer.endTag((String) null, TAG_OPS);
        }

        private void writeHistoricalUidOpsDLocked(AppOpsManager.HistoricalUidOps historicalUidOps, TypedXmlSerializer typedXmlSerializer) throws IOException {
            typedXmlSerializer.startTag((String) null, TAG_UID);
            typedXmlSerializer.attributeInt((String) null, ATTR_NAME, historicalUidOps.getUid());
            int packageCount = historicalUidOps.getPackageCount();
            for (int i = 0; i < packageCount; i++) {
                writeHistoricalPackageOpsDLocked(historicalUidOps.getPackageOpsAt(i), typedXmlSerializer);
            }
            typedXmlSerializer.endTag((String) null, TAG_UID);
        }

        private void writeHistoricalPackageOpsDLocked(AppOpsManager.HistoricalPackageOps historicalPackageOps, TypedXmlSerializer typedXmlSerializer) throws IOException {
            typedXmlSerializer.startTag((String) null, TAG_PACKAGE);
            typedXmlSerializer.attributeInterned((String) null, ATTR_NAME, historicalPackageOps.getPackageName());
            int attributedOpsCount = historicalPackageOps.getAttributedOpsCount();
            for (int i = 0; i < attributedOpsCount; i++) {
                writeHistoricalAttributionOpsDLocked(historicalPackageOps.getAttributedOpsAt(i), typedXmlSerializer);
            }
            typedXmlSerializer.endTag((String) null, TAG_PACKAGE);
        }

        private void writeHistoricalAttributionOpsDLocked(AppOpsManager.AttributedHistoricalOps attributedHistoricalOps, TypedXmlSerializer typedXmlSerializer) throws IOException {
            typedXmlSerializer.startTag((String) null, TAG_ATTRIBUTION);
            XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_NAME, attributedHistoricalOps.getTag());
            int opCount = attributedHistoricalOps.getOpCount();
            for (int i = 0; i < opCount; i++) {
                writeHistoricalOpDLocked(attributedHistoricalOps.getOpAt(i), typedXmlSerializer);
            }
            typedXmlSerializer.endTag((String) null, TAG_ATTRIBUTION);
        }

        private void writeHistoricalOpDLocked(AppOpsManager.HistoricalOp historicalOp, TypedXmlSerializer typedXmlSerializer) throws IOException {
            LongSparseArray collectKeys = historicalOp.collectKeys();
            if (collectKeys == null || collectKeys.size() <= 0) {
                return;
            }
            typedXmlSerializer.startTag((String) null, TAG_OP);
            typedXmlSerializer.attributeInt((String) null, ATTR_NAME, historicalOp.getOpCode());
            int size = collectKeys.size();
            for (int i = 0; i < size; i++) {
                writeStateOnLocked(historicalOp, collectKeys.keyAt(i), typedXmlSerializer);
            }
            typedXmlSerializer.endTag((String) null, TAG_OP);
        }

        private void writeStateOnLocked(AppOpsManager.HistoricalOp historicalOp, long j, TypedXmlSerializer typedXmlSerializer) throws IOException {
            int extractUidStateFromKey = AppOpsManager.extractUidStateFromKey(j);
            int extractFlagsFromKey = AppOpsManager.extractFlagsFromKey(j);
            long accessCount = historicalOp.getAccessCount(extractUidStateFromKey, extractUidStateFromKey, extractFlagsFromKey);
            long rejectCount = historicalOp.getRejectCount(extractUidStateFromKey, extractUidStateFromKey, extractFlagsFromKey);
            long accessDuration = historicalOp.getAccessDuration(extractUidStateFromKey, extractUidStateFromKey, extractFlagsFromKey);
            if (accessCount > 0 || rejectCount > 0 || accessDuration > 0) {
                typedXmlSerializer.startTag((String) null, TAG_STATE);
                typedXmlSerializer.attributeLong((String) null, ATTR_NAME, j);
                if (accessCount > 0) {
                    typedXmlSerializer.attributeLong((String) null, ATTR_ACCESS_COUNT, accessCount);
                }
                if (rejectCount > 0) {
                    typedXmlSerializer.attributeLong((String) null, ATTR_REJECT_COUNT, rejectCount);
                }
                if (accessDuration > 0) {
                    typedXmlSerializer.attributeLong((String) null, ATTR_ACCESS_DURATION, accessDuration);
                }
                typedXmlSerializer.endTag((String) null, TAG_STATE);
            }
        }

        private static void enforceOpsWellFormed(List<AppOpsManager.HistoricalOps> list) {
            if (list == null) {
                return;
            }
            int size = list.size();
            AppOpsManager.HistoricalOps historicalOps = null;
            int i = 0;
            while (i < size) {
                AppOpsManager.HistoricalOps historicalOps2 = list.get(i);
                if (historicalOps2.isEmpty()) {
                    throw new IllegalStateException("Empty ops:\n" + opsToDebugString(list));
                }
                if (historicalOps2.getEndTimeMillis() < historicalOps2.getBeginTimeMillis()) {
                    throw new IllegalStateException("Begin after end:\n" + opsToDebugString(list));
                }
                if (historicalOps != null) {
                    if (historicalOps.getEndTimeMillis() > historicalOps2.getBeginTimeMillis()) {
                        throw new IllegalStateException("Intersecting ops:\n" + opsToDebugString(list));
                    }
                    if (historicalOps.getBeginTimeMillis() > historicalOps2.getBeginTimeMillis()) {
                        throw new IllegalStateException("Non increasing ops:\n" + opsToDebugString(list));
                    }
                }
                i++;
                historicalOps = historicalOps2;
            }
        }

        private long computeGlobalIntervalBeginMillis(int i) {
            long j = 0;
            for (int i2 = 0; i2 < i + 1; i2++) {
                j = (long) (j + Math.pow(this.mIntervalCompressionMultiplier, i2));
            }
            return j * this.mBaseSnapshotInterval;
        }

        private static AppOpsManager.HistoricalOps spliceFromEnd(AppOpsManager.HistoricalOps historicalOps, double d) {
            return historicalOps.spliceFromEnd(d);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static AppOpsManager.HistoricalOps spliceFromBeginning(AppOpsManager.HistoricalOps historicalOps, double d) {
            return historicalOps.spliceFromBeginning(d);
        }

        private static void normalizeSnapshotForSlotDuration(List<AppOpsManager.HistoricalOps> list, long j) {
            int size = list.size() - 1;
            while (size >= 0) {
                AppOpsManager.HistoricalOps historicalOps = list.get(size);
                long max = Math.max(historicalOps.getEndTimeMillis() - j, 0L);
                for (int i = size - 1; i >= 0; i--) {
                    AppOpsManager.HistoricalOps historicalOps2 = list.get(i);
                    long endTimeMillis = historicalOps2.getEndTimeMillis() - Math.min(max, historicalOps.getBeginTimeMillis());
                    if (endTimeMillis <= 0) {
                        break;
                    }
                    float durationMillis = ((float) endTimeMillis) / ((float) historicalOps2.getDurationMillis());
                    if (Float.compare(durationMillis, 1.0f) >= 0) {
                        list.remove(i);
                        size--;
                        historicalOps.merge(historicalOps2);
                    } else {
                        AppOpsManager.HistoricalOps spliceFromEnd = spliceFromEnd(historicalOps2, durationMillis);
                        if (spliceFromEnd != null) {
                            historicalOps.merge(spliceFromEnd);
                        }
                        if (historicalOps2.isEmpty()) {
                            list.remove(i);
                            size--;
                        }
                    }
                }
                size--;
            }
        }

        private static String opsToDebugString(List<AppOpsManager.HistoricalOps> list) {
            StringBuilder sb = new StringBuilder();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                sb.append("  ");
                sb.append(list.get(i));
                if (i < size - 1) {
                    sb.append('\n');
                }
            }
            return sb.toString();
        }

        private static Set<String> getHistoricalFileNames(File file) {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                return Collections.emptySet();
            }
            ArraySet arraySet = new ArraySet(listFiles.length);
            for (File file2 : listFiles) {
                arraySet.add(file2.getName());
            }
            return arraySet;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class HistoricalFilesInvariant {
        private final List<File> mBeginFiles = new ArrayList();

        private HistoricalFilesInvariant() {
        }

        public void startTracking(File file) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                Collections.addAll(this.mBeginFiles, listFiles);
            }
        }

        public void stopTracking(File file) {
            ArrayList arrayList = new ArrayList();
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                Collections.addAll(arrayList, listFiles);
            }
            if (getOldestFileOffsetMillis(arrayList) >= getOldestFileOffsetMillis(this.mBeginFiles)) {
                return;
            }
            String str = "History loss detected!\nold files: " + this.mBeginFiles;
            HistoricalRegistry.wtf(str, null, file);
            throw new IllegalStateException(str);
        }

        private static long getOldestFileOffsetMillis(List<File> list) {
            if (list.isEmpty()) {
                return 0L;
            }
            String name = list.get(0).getName();
            int size = list.size();
            for (int i = 1; i < size; i++) {
                File file = list.get(i);
                if (file.getName().length() > name.length()) {
                    name = file.getName();
                }
            }
            return Long.parseLong(name.replace(HistoricalRegistry.HISTORY_FILE_SUFFIX, ""));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class StringDumpVisitor implements AppOpsManager.HistoricalOpsVisitor {
        private final String mAttributionPrefix;
        private final String mEntryPrefix;
        private final int mFilter;
        private final String mFilterAttributionTag;
        private final int mFilterOp;
        private final String mFilterPackage;
        private final int mFilterUid;
        private final String mOpsPrefix;
        private final String mPackagePrefix;
        private final String mUidPrefix;
        private final String mUidStatePrefix;
        private final PrintWriter mWriter;
        private final long mNow = System.currentTimeMillis();
        private final SimpleDateFormat mDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private final Date mDate = new Date();

        StringDumpVisitor(String str, PrintWriter printWriter, int i, String str2, String str3, int i2, int i3) {
            String str4 = str + "  ";
            this.mOpsPrefix = str4;
            String str5 = str4 + "  ";
            this.mUidPrefix = str5;
            String str6 = str5 + "  ";
            this.mPackagePrefix = str6;
            String str7 = str6 + "  ";
            this.mAttributionPrefix = str7;
            String str8 = str7 + "  ";
            this.mEntryPrefix = str8;
            this.mUidStatePrefix = str8 + "  ";
            this.mWriter = printWriter;
            this.mFilterUid = i;
            this.mFilterPackage = str2;
            this.mFilterAttributionTag = str3;
            this.mFilterOp = i2;
            this.mFilter = i3;
        }

        public void visitHistoricalOps(AppOpsManager.HistoricalOps historicalOps) {
            this.mWriter.println();
            this.mWriter.print(this.mOpsPrefix);
            this.mWriter.println("snapshot:");
            this.mWriter.print(this.mUidPrefix);
            this.mWriter.print("begin = ");
            this.mDate.setTime(historicalOps.getBeginTimeMillis());
            this.mWriter.print(this.mDateFormatter.format(this.mDate));
            this.mWriter.print("  (");
            TimeUtils.formatDuration(historicalOps.getBeginTimeMillis() - this.mNow, this.mWriter);
            this.mWriter.println(")");
            this.mWriter.print(this.mUidPrefix);
            this.mWriter.print("end = ");
            this.mDate.setTime(historicalOps.getEndTimeMillis());
            this.mWriter.print(this.mDateFormatter.format(this.mDate));
            this.mWriter.print("  (");
            TimeUtils.formatDuration(historicalOps.getEndTimeMillis() - this.mNow, this.mWriter);
            this.mWriter.println(")");
        }

        public void visitHistoricalUidOps(AppOpsManager.HistoricalUidOps historicalUidOps) {
            if ((this.mFilter & 1) == 0 || this.mFilterUid == historicalUidOps.getUid()) {
                this.mWriter.println();
                this.mWriter.print(this.mUidPrefix);
                this.mWriter.print("Uid ");
                UserHandle.formatUid(this.mWriter, historicalUidOps.getUid());
                this.mWriter.println(":");
            }
        }

        public void visitHistoricalPackageOps(AppOpsManager.HistoricalPackageOps historicalPackageOps) {
            if ((this.mFilter & 2) == 0 || this.mFilterPackage.equals(historicalPackageOps.getPackageName())) {
                this.mWriter.print(this.mPackagePrefix);
                this.mWriter.print("Package ");
                this.mWriter.print(historicalPackageOps.getPackageName());
                this.mWriter.println(":");
            }
        }

        public void visitHistoricalAttributionOps(AppOpsManager.AttributedHistoricalOps attributedHistoricalOps) {
            if ((this.mFilter & 4) == 0 || Objects.equals(this.mFilterPackage, attributedHistoricalOps.getTag())) {
                this.mWriter.print(this.mAttributionPrefix);
                this.mWriter.print("Attribution ");
                this.mWriter.print(attributedHistoricalOps.getTag());
                this.mWriter.println(":");
            }
        }

        public void visitHistoricalOp(AppOpsManager.HistoricalOp historicalOp) {
            boolean z;
            if ((this.mFilter & 8) == 0 || this.mFilterOp == historicalOp.getOpCode()) {
                this.mWriter.print(this.mEntryPrefix);
                this.mWriter.print(AppOpsManager.opToName(historicalOp.getOpCode()));
                this.mWriter.println(":");
                LongSparseArray collectKeys = historicalOp.collectKeys();
                int size = collectKeys.size();
                for (int i = 0; i < size; i++) {
                    long keyAt = collectKeys.keyAt(i);
                    int extractUidStateFromKey = AppOpsManager.extractUidStateFromKey(keyAt);
                    int extractFlagsFromKey = AppOpsManager.extractFlagsFromKey(keyAt);
                    long accessCount = historicalOp.getAccessCount(extractUidStateFromKey, extractUidStateFromKey, extractFlagsFromKey);
                    boolean z2 = true;
                    if (accessCount > 0) {
                        this.mWriter.print(this.mUidStatePrefix);
                        this.mWriter.print(AppOpsManager.keyToString(keyAt));
                        this.mWriter.print(" = ");
                        this.mWriter.print("access=");
                        this.mWriter.print(accessCount);
                        z = true;
                    } else {
                        z = false;
                    }
                    long rejectCount = historicalOp.getRejectCount(extractUidStateFromKey, extractUidStateFromKey, extractFlagsFromKey);
                    if (rejectCount > 0) {
                        if (!z) {
                            this.mWriter.print(this.mUidStatePrefix);
                            this.mWriter.print(AppOpsManager.keyToString(keyAt));
                            this.mWriter.print(" = ");
                            z = true;
                        } else {
                            this.mWriter.print(", ");
                        }
                        this.mWriter.print("reject=");
                        this.mWriter.print(rejectCount);
                    }
                    long accessDuration = historicalOp.getAccessDuration(extractUidStateFromKey, extractUidStateFromKey, extractFlagsFromKey);
                    if (accessDuration > 0) {
                        if (!z) {
                            this.mWriter.print(this.mUidStatePrefix);
                            this.mWriter.print(AppOpsManager.keyToString(keyAt));
                            this.mWriter.print(" = ");
                        } else {
                            this.mWriter.print(", ");
                            z2 = z;
                        }
                        this.mWriter.print("duration=");
                        TimeUtils.formatDuration(accessDuration, this.mWriter);
                        z = z2;
                    }
                    if (z) {
                        this.mWriter.println("");
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void wtf(String str, Throwable th, File file) {
        Slog.wtf(LOG_TAG, str, th);
        if (KEEP_WTF_LOG) {
            try {
                File file2 = new File(new File(Environment.getDataSystemDirectory(), "appops"), "wtf" + TimeUtils.formatForLogging(System.currentTimeMillis()));
                if (file2.createNewFile()) {
                    PrintWriter printWriter = new PrintWriter(file2);
                    if (th != null) {
                        try {
                            printWriter.append('\n').append((CharSequence) th.toString());
                        } finally {
                        }
                    }
                    printWriter.append('\n').append((CharSequence) Debug.getCallers(10));
                    if (file != null) {
                        printWriter.append((CharSequence) ("\nfiles: " + Arrays.toString(file.listFiles())));
                    } else {
                        printWriter.append((CharSequence) "\nfiles: none");
                    }
                    printWriter.close();
                }
            } catch (IOException unused) {
            }
        }
    }
}
