package com.android.server.appop;

import android.app.AppOpsManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DeviceConfig;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.appop.DiscreteRegistry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DiscreteRegistry {
    private static final String ATTR_ATTRIBUTION_FLAGS = "af";
    private static final String ATTR_CHAIN_ID = "ci";
    private static final String ATTR_FLAGS = "f";
    private static final String ATTR_LARGEST_CHAIN_ID = "lc";
    private static final String ATTR_NOTE_DURATION = "nd";
    private static final String ATTR_NOTE_TIME = "nt";
    private static final String ATTR_OP_ID = "op";
    private static final String ATTR_PACKAGE_NAME = "pn";
    private static final String ATTR_TAG = "at";
    private static final String ATTR_UID = "ui";
    private static final String ATTR_UID_STATE = "us";
    private static final String ATTR_VERSION = "v";
    private static final int CURRENT_VERSION = 1;
    private static final String DEFAULT_DISCRETE_OPS = "1,0,26,27,100,101,120";
    static final String DISCRETE_HISTORY_FILE_SUFFIX = "tl";
    private static final int OP_FLAGS_DISCRETE = 11;
    private static final String PROPERTY_DISCRETE_FLAGS = "discrete_history_op_flags";
    private static final String PROPERTY_DISCRETE_HISTORY_CUTOFF = "discrete_history_cutoff_millis";
    private static final String PROPERTY_DISCRETE_HISTORY_QUANTIZATION = "discrete_history_quantization_millis";
    private static final String PROPERTY_DISCRETE_OPS_LIST = "discrete_history_ops_cslist";
    private static final String TAG = "DiscreteRegistry";
    private static final String TAG_ENTRY = "e";
    private static final String TAG_HISTORY = "h";
    private static final String TAG_OP = "o";
    private static final String TAG_PACKAGE = "p";
    private static final String TAG_TAG = "a";
    private static final String TAG_UID = "u";
    private static int sDiscreteFlags;
    private static long sDiscreteHistoryCutoff;
    private static long sDiscreteHistoryQuantization;
    private static int[] sDiscreteOps;

    @GuardedBy({"mOnDiskLock"})
    private DiscreteOps mCachedOps;
    private boolean mDebugMode;

    @GuardedBy({"mOnDiskLock"})
    private File mDiscreteAccessDir;

    @GuardedBy({"mInMemoryLock"})
    private DiscreteOps mDiscreteOps;
    private final Object mInMemoryLock;
    private final Object mOnDiskLock;
    private static final long DEFAULT_DISCRETE_HISTORY_CUTOFF = Duration.ofDays(7).toMillis();
    private static final long MAXIMUM_DISCRETE_HISTORY_CUTOFF = Duration.ofDays(30).toMillis();
    private static final long DEFAULT_DISCRETE_HISTORY_QUANTIZATION = Duration.ofMinutes(1).toMillis();

    /* JADX INFO: Access modifiers changed from: package-private */
    public DiscreteRegistry(Object obj) {
        Object obj2 = new Object();
        this.mOnDiskLock = obj2;
        this.mCachedOps = null;
        this.mDebugMode = false;
        this.mInMemoryLock = obj;
        synchronized (obj2) {
            this.mDiscreteAccessDir = new File(new File(Environment.getDataSystemDirectory(), "appops"), "discrete");
            createDiscreteAccessDirLocked();
            int readLargestChainIdFromDiskLocked = readLargestChainIdFromDiskLocked();
            synchronized (obj) {
                this.mDiscreteOps = new DiscreteOps(readLargestChainIdFromDiskLocked);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void systemReady() {
        DeviceConfig.addOnPropertiesChangedListener("privacy", AsyncTask.THREAD_POOL_EXECUTOR, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.appop.DiscreteRegistry$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                DiscreteRegistry.this.lambda$systemReady$0(properties);
            }
        });
        lambda$systemReady$0(DeviceConfig.getProperties("privacy", new String[0]));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: setDiscreteHistoryParameters, reason: merged with bridge method [inline-methods] */
    public void lambda$systemReady$0(DeviceConfig.Properties properties) {
        int[] parseOpsList;
        if (properties.getKeyset().contains(PROPERTY_DISCRETE_HISTORY_CUTOFF)) {
            sDiscreteHistoryCutoff = properties.getLong(PROPERTY_DISCRETE_HISTORY_CUTOFF, DEFAULT_DISCRETE_HISTORY_CUTOFF);
            if (!Build.IS_DEBUGGABLE && !this.mDebugMode) {
                sDiscreteHistoryCutoff = Long.min(MAXIMUM_DISCRETE_HISTORY_CUTOFF, sDiscreteHistoryCutoff);
            }
        } else {
            sDiscreteHistoryCutoff = DEFAULT_DISCRETE_HISTORY_CUTOFF;
        }
        if (properties.getKeyset().contains(PROPERTY_DISCRETE_HISTORY_QUANTIZATION)) {
            long j = DEFAULT_DISCRETE_HISTORY_QUANTIZATION;
            sDiscreteHistoryQuantization = properties.getLong(PROPERTY_DISCRETE_HISTORY_QUANTIZATION, j);
            if (!Build.IS_DEBUGGABLE && !this.mDebugMode) {
                sDiscreteHistoryQuantization = Math.max(j, sDiscreteHistoryQuantization);
            }
        } else {
            sDiscreteHistoryQuantization = DEFAULT_DISCRETE_HISTORY_QUANTIZATION;
        }
        int i = 11;
        if (properties.getKeyset().contains(PROPERTY_DISCRETE_FLAGS)) {
            i = properties.getInt(PROPERTY_DISCRETE_FLAGS, 11);
            sDiscreteFlags = i;
        }
        sDiscreteFlags = i;
        if (properties.getKeyset().contains(PROPERTY_DISCRETE_OPS_LIST)) {
            parseOpsList = parseOpsList(properties.getString(PROPERTY_DISCRETE_OPS_LIST, DEFAULT_DISCRETE_OPS));
        } else {
            parseOpsList = parseOpsList(DEFAULT_DISCRETE_OPS);
        }
        sDiscreteOps = parseOpsList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void recordDiscreteAccess(int i, String str, int i2, String str2, int i3, int i4, long j, long j2, int i5, int i6) {
        if (isDiscreteOp(i2, i3)) {
            synchronized (this.mInMemoryLock) {
                this.mDiscreteOps.addDiscreteAccess(i2, i, str, str2, i3, i4, j, j2, i5, i6);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeAndClearAccessHistory() {
        DiscreteOps discreteOps;
        synchronized (this.mOnDiskLock) {
            if (this.mDiscreteAccessDir == null) {
                Slog.d(TAG, "State not saved - persistence not initialized.");
                return;
            }
            synchronized (this.mInMemoryLock) {
                discreteOps = this.mDiscreteOps;
                this.mDiscreteOps = new DiscreteOps(discreteOps.mChainIdOffset);
                this.mCachedOps = null;
            }
            deleteOldDiscreteHistoryFilesLocked();
            if (!discreteOps.isEmpty()) {
                persistDiscreteOpsLocked(discreteOps);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addFilteredDiscreteOpsToHistoricalOps(AppOpsManager.HistoricalOps historicalOps, long j, long j2, int i, int i2, String str, String[] strArr, String str2, int i3, Set<String> set) {
        boolean z = set != null;
        DiscreteOps allDiscreteOps = getAllDiscreteOps();
        ArrayMap<Integer, AttributionChain> arrayMap = new ArrayMap<>();
        if (z) {
            arrayMap = createAttributionChains(allDiscreteOps, set);
        }
        ArrayMap<Integer, AttributionChain> arrayMap2 = arrayMap;
        allDiscreteOps.filter(Math.max(j, Instant.now().minus(sDiscreteHistoryCutoff, (TemporalUnit) ChronoUnit.MILLIS).toEpochMilli()), j2, i, i2, str, strArr, str2, i3, arrayMap2);
        allDiscreteOps.applyToHistoricalOps(historicalOps, arrayMap2);
    }

    private int readLargestChainIdFromDiskLocked() {
        File[] listFiles = this.mDiscreteAccessDir.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            long j = 0;
            File file = null;
            for (File file2 : listFiles) {
                String name = file2.getName();
                if (name.endsWith(DISCRETE_HISTORY_FILE_SUFFIX)) {
                    long longValue = Long.valueOf(name.substring(0, name.length() - 2)).longValue();
                    if (j < longValue) {
                        file = file2;
                        j = longValue;
                    }
                }
            }
            if (file == null) {
                return 0;
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(fileInputStream);
                    XmlUtils.beginDocument(resolvePullParser, TAG_HISTORY);
                    int attributeInt = resolvePullParser.getAttributeInt((String) null, ATTR_LARGEST_CHAIN_ID, 0);
                    try {
                        fileInputStream.close();
                    } catch (IOException unused) {
                    }
                    return attributeInt;
                } catch (Throwable unused2) {
                    fileInputStream.close();
                }
            } catch (FileNotFoundException | IOException unused3) {
            }
        }
        return 0;
    }

    private ArrayMap<Integer, AttributionChain> createAttributionChains(DiscreteOps discreteOps, Set<String> set) {
        ArrayMap<String, DiscretePackageOps> arrayMap;
        List<DiscreteOpEvent> list;
        int i;
        int i2;
        ArrayMap<String, List<DiscreteOpEvent>> arrayMap2;
        int i3;
        int i4;
        int i5;
        DiscreteOps discreteOps2 = discreteOps;
        ArrayMap<Integer, AttributionChain> arrayMap3 = new ArrayMap<>();
        int size = discreteOps2.mUids.size();
        int i6 = 0;
        while (i6 < size) {
            ArrayMap<String, DiscretePackageOps> arrayMap4 = discreteOps2.mUids.valueAt(i6).mPackages;
            int intValue = discreteOps2.mUids.keyAt(i6).intValue();
            int size2 = arrayMap4.size();
            int i7 = 0;
            while (i7 < size2) {
                ArrayMap<Integer, DiscreteOp> arrayMap5 = arrayMap4.valueAt(i7).mPackageOps;
                String keyAt = arrayMap4.keyAt(i7);
                int size3 = arrayMap5.size();
                int i8 = 0;
                while (i8 < size3) {
                    ArrayMap<String, List<DiscreteOpEvent>> arrayMap6 = arrayMap5.valueAt(i8).mAttributedOps;
                    int intValue2 = arrayMap5.keyAt(i8).intValue();
                    int size4 = arrayMap6.size();
                    int i9 = 0;
                    while (i9 < size4) {
                        List<DiscreteOpEvent> valueAt = arrayMap6.valueAt(i9);
                        String keyAt2 = arrayMap6.keyAt(i9);
                        int size5 = valueAt.size();
                        int i10 = 0;
                        while (i10 < size5) {
                            int i11 = size;
                            DiscreteOpEvent discreteOpEvent = valueAt.get(i10);
                            int i12 = size5;
                            if (discreteOpEvent != null) {
                                int i13 = discreteOpEvent.mAttributionChainId;
                                arrayMap = arrayMap4;
                                if (i13 != -1 && (discreteOpEvent.mAttributionFlags & 8) != 0) {
                                    if (arrayMap3.containsKey(Integer.valueOf(i13))) {
                                        i5 = size2;
                                    } else {
                                        i5 = size2;
                                        arrayMap3.put(Integer.valueOf(discreteOpEvent.mAttributionChainId), new AttributionChain(set));
                                    }
                                    list = valueAt;
                                    i = i9;
                                    i2 = size4;
                                    arrayMap2 = arrayMap6;
                                    i3 = i8;
                                    i4 = size3;
                                    arrayMap3.get(Integer.valueOf(discreteOpEvent.mAttributionChainId)).addEvent(keyAt, intValue, keyAt2, intValue2, discreteOpEvent);
                                    i10++;
                                    i9 = i;
                                    valueAt = list;
                                    size5 = i12;
                                    size = i11;
                                    arrayMap4 = arrayMap;
                                    size2 = i5;
                                    size4 = i2;
                                    arrayMap6 = arrayMap2;
                                    i8 = i3;
                                    size3 = i4;
                                }
                            } else {
                                arrayMap = arrayMap4;
                            }
                            list = valueAt;
                            i = i9;
                            i2 = size4;
                            arrayMap2 = arrayMap6;
                            i3 = i8;
                            i4 = size3;
                            i5 = size2;
                            i10++;
                            i9 = i;
                            valueAt = list;
                            size5 = i12;
                            size = i11;
                            arrayMap4 = arrayMap;
                            size2 = i5;
                            size4 = i2;
                            arrayMap6 = arrayMap2;
                            i8 = i3;
                            size3 = i4;
                        }
                        i9++;
                        size2 = size2;
                    }
                    i8++;
                    size2 = size2;
                }
                i7++;
                size2 = size2;
            }
            i6++;
            discreteOps2 = discreteOps;
        }
        return arrayMap3;
    }

    private void readDiscreteOpsFromDisk(DiscreteOps discreteOps) {
        synchronized (this.mOnDiskLock) {
            long epochMilli = Instant.now().minus(sDiscreteHistoryCutoff, (TemporalUnit) ChronoUnit.MILLIS).toEpochMilli();
            File[] listFiles = this.mDiscreteAccessDir.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    String name = file.getName();
                    if (name.endsWith(DISCRETE_HISTORY_FILE_SUFFIX) && Long.valueOf(name.substring(0, name.length() - 2)).longValue() >= epochMilli) {
                        discreteOps.readFromFile(file, epochMilli);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearHistory() {
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                this.mDiscreteOps = new DiscreteOps(0);
            }
            clearOnDiskHistoryLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearHistory(int i, String str) {
        DiscreteOps allDiscreteOps;
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                allDiscreteOps = getAllDiscreteOps();
                clearHistory();
            }
            allDiscreteOps.clearHistory(i, str);
            persistDiscreteOpsLocked(allDiscreteOps);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void offsetHistory(long j) {
        DiscreteOps allDiscreteOps;
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                allDiscreteOps = getAllDiscreteOps();
                clearHistory();
            }
            allDiscreteOps.offsetHistory(j);
            persistDiscreteOpsLocked(allDiscreteOps);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, int i, String str, String str2, int i2, int i3, SimpleDateFormat simpleDateFormat, Date date, String str3, int i4) {
        DiscreteOps allDiscreteOps = getAllDiscreteOps();
        allDiscreteOps.filter(0L, Instant.now().toEpochMilli(), i2, i, str, i3 == -1 ? null : new String[]{AppOpsManager.opToPublicName(i3)}, str2, 31, new ArrayMap());
        printWriter.print(str3);
        printWriter.print("Largest chain id: ");
        printWriter.print(this.mDiscreteOps.mLargestChainId);
        printWriter.println();
        allDiscreteOps.dump(printWriter, simpleDateFormat, date, str3, i4);
    }

    private void clearOnDiskHistoryLocked() {
        this.mCachedOps = null;
        FileUtils.deleteContentsAndDir(this.mDiscreteAccessDir);
        createDiscreteAccessDir();
    }

    private DiscreteOps getAllDiscreteOps() {
        DiscreteOps discreteOps = new DiscreteOps(0);
        synchronized (this.mOnDiskLock) {
            synchronized (this.mInMemoryLock) {
                discreteOps.merge(this.mDiscreteOps);
            }
            if (this.mCachedOps == null) {
                DiscreteOps discreteOps2 = new DiscreteOps(0);
                this.mCachedOps = discreteOps2;
                readDiscreteOpsFromDisk(discreteOps2);
            }
            discreteOps.merge(this.mCachedOps);
        }
        return discreteOps;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class AttributionChain {
        Set<String> mExemptPkgs;
        ArrayList<OpEvent> mChain = new ArrayList<>();
        OpEvent mStartEvent = null;
        OpEvent mLastVisibleEvent = null;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public static final class OpEvent {
            String mAttributionTag;
            int mOpCode;
            DiscreteOpEvent mOpEvent;
            String mPkgName;
            int mUid;

            OpEvent(String str, int i, String str2, int i2, DiscreteOpEvent discreteOpEvent) {
                this.mPkgName = str;
                this.mUid = i;
                this.mAttributionTag = str2;
                this.mOpCode = i2;
                this.mOpEvent = discreteOpEvent;
            }

            public boolean matches(String str, int i, String str2, int i2, DiscreteOpEvent discreteOpEvent) {
                if (Objects.equals(str, this.mPkgName) && this.mUid == i && Objects.equals(str2, this.mAttributionTag) && this.mOpCode == i2) {
                    DiscreteOpEvent discreteOpEvent2 = this.mOpEvent;
                    if (discreteOpEvent2.mAttributionChainId == discreteOpEvent.mAttributionChainId && discreteOpEvent2.mAttributionFlags == discreteOpEvent.mAttributionFlags && discreteOpEvent2.mNoteTime == discreteOpEvent.mNoteTime) {
                        return true;
                    }
                }
                return false;
            }

            public boolean packageOpEquals(OpEvent opEvent) {
                return Objects.equals(opEvent.mPkgName, this.mPkgName) && opEvent.mUid == this.mUid && Objects.equals(opEvent.mAttributionTag, this.mAttributionTag) && this.mOpCode == opEvent.mOpCode;
            }

            public boolean equalsExceptDuration(OpEvent opEvent) {
                return opEvent.mOpEvent.mNoteDuration != this.mOpEvent.mNoteDuration && packageOpEquals(opEvent) && this.mOpEvent.equalsExceptDuration(opEvent.mOpEvent);
            }
        }

        AttributionChain(Set<String> set) {
            this.mExemptPkgs = set;
        }

        boolean isComplete() {
            if (!this.mChain.isEmpty() && getStart() != null) {
                ArrayList<OpEvent> arrayList = this.mChain;
                if (isEnd(arrayList.get(arrayList.size() - 1))) {
                    return true;
                }
            }
            return false;
        }

        boolean isStart(String str, int i, String str2, int i2, DiscreteOpEvent discreteOpEvent) {
            OpEvent opEvent = this.mStartEvent;
            if (opEvent == null || discreteOpEvent == null) {
                return false;
            }
            return opEvent.matches(str, i, str2, i2, discreteOpEvent);
        }

        private OpEvent getStart() {
            if (this.mChain.isEmpty() || !isStart(this.mChain.get(0))) {
                return null;
            }
            return this.mChain.get(0);
        }

        private OpEvent getLastVisible() {
            for (int size = this.mChain.size() - 1; size > 0; size--) {
                OpEvent opEvent = this.mChain.get(size);
                if (!this.mExemptPkgs.contains(opEvent.mPkgName)) {
                    return opEvent;
                }
            }
            return null;
        }

        void addEvent(String str, int i, String str2, int i2, DiscreteOpEvent discreteOpEvent) {
            OpEvent opEvent = new OpEvent(str, i, str2, i2, discreteOpEvent);
            int i3 = 0;
            for (int i4 = 0; i4 < this.mChain.size(); i4++) {
                OpEvent opEvent2 = this.mChain.get(i4);
                if (opEvent2.equalsExceptDuration(opEvent)) {
                    DiscreteOpEvent discreteOpEvent2 = opEvent.mOpEvent;
                    if (discreteOpEvent2.mNoteDuration != -1) {
                        opEvent2.mOpEvent = discreteOpEvent2;
                        return;
                    }
                    return;
                }
            }
            if (this.mChain.isEmpty() || isEnd(opEvent)) {
                this.mChain.add(opEvent);
            } else if (isStart(opEvent)) {
                this.mChain.add(0, opEvent);
            } else {
                while (true) {
                    if (i3 >= this.mChain.size()) {
                        break;
                    }
                    OpEvent opEvent3 = this.mChain.get(i3);
                    if ((isStart(opEvent3) || opEvent3.mOpEvent.mNoteTime <= opEvent.mOpEvent.mNoteTime) && (i3 != this.mChain.size() - 1 || !isEnd(opEvent3))) {
                        if (i3 == this.mChain.size() - 1) {
                            this.mChain.add(opEvent);
                            break;
                        }
                        i3++;
                    }
                }
                this.mChain.add(i3, opEvent);
            }
            this.mStartEvent = isComplete() ? getStart() : null;
            this.mLastVisibleEvent = isComplete() ? getLastVisible() : null;
        }

        private boolean isEnd(OpEvent opEvent) {
            return (opEvent == null || (opEvent.mOpEvent.mAttributionFlags & 1) == 0) ? false : true;
        }

        private boolean isStart(OpEvent opEvent) {
            return (opEvent == null || (opEvent.mOpEvent.mAttributionFlags & 4) == 0) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DiscreteOps {
        int mChainIdOffset;
        int mLargestChainId;
        ArrayMap<Integer, DiscreteUidOps> mUids = new ArrayMap<>();

        DiscreteOps(int i) {
            this.mChainIdOffset = i;
            this.mLargestChainId = i;
        }

        boolean isEmpty() {
            return this.mUids.isEmpty();
        }

        void merge(DiscreteOps discreteOps) {
            this.mLargestChainId = Math.max(this.mLargestChainId, discreteOps.mLargestChainId);
            int size = discreteOps.mUids.size();
            for (int i = 0; i < size; i++) {
                int intValue = discreteOps.mUids.keyAt(i).intValue();
                getOrCreateDiscreteUidOps(intValue).merge(discreteOps.mUids.valueAt(i));
            }
        }

        void addDiscreteAccess(int i, int i2, String str, String str2, int i3, int i4, long j, long j2, int i5, int i6) {
            int i7;
            int i8;
            if (i6 != -1) {
                int i9 = this.mChainIdOffset + i6;
                if (i9 > this.mLargestChainId) {
                    this.mLargestChainId = i9;
                } else if (i9 < 0) {
                    i9 = 0;
                    this.mLargestChainId = 0;
                    this.mChainIdOffset = i6 * (-1);
                }
                i8 = i2;
                i7 = i9;
            } else {
                i7 = i6;
                i8 = i2;
            }
            getOrCreateDiscreteUidOps(i8).addDiscreteAccess(i, str, str2, i3, i4, j, j2, i5, i7);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long j, long j2, int i, int i2, String str, String[] strArr, String str2, int i3, ArrayMap<Integer, AttributionChain> arrayMap) {
            if ((i & 1) != 0) {
                ArrayMap<Integer, DiscreteUidOps> arrayMap2 = new ArrayMap<>();
                arrayMap2.put(Integer.valueOf(i2), getOrCreateDiscreteUidOps(i2));
                this.mUids = arrayMap2;
            }
            for (int size = this.mUids.size() - 1; size >= 0; size--) {
                this.mUids.valueAt(size).filter(j, j2, i, str, strArr, str2, i3, this.mUids.keyAt(size).intValue(), arrayMap);
                if (this.mUids.valueAt(size).isEmpty()) {
                    this.mUids.removeAt(size);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long j) {
            int size = this.mUids.size();
            for (int i = 0; i < size; i++) {
                this.mUids.valueAt(i).offsetHistory(j);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearHistory(int i, String str) {
            if (this.mUids.containsKey(Integer.valueOf(i))) {
                this.mUids.get(Integer.valueOf(i)).clearPackage(str);
                if (this.mUids.get(Integer.valueOf(i)).isEmpty()) {
                    this.mUids.remove(Integer.valueOf(i));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistoricalOps(AppOpsManager.HistoricalOps historicalOps, ArrayMap<Integer, AttributionChain> arrayMap) {
            int size = this.mUids.size();
            for (int i = 0; i < size; i++) {
                this.mUids.valueAt(i).applyToHistory(historicalOps, this.mUids.keyAt(i).intValue(), arrayMap);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToStream(FileOutputStream fileOutputStream) throws Exception {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(fileOutputStream);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.startTag((String) null, DiscreteRegistry.TAG_HISTORY);
            resolveSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_VERSION, 1);
            resolveSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_LARGEST_CHAIN_ID, this.mLargestChainId);
            int size = this.mUids.size();
            for (int i = 0; i < size; i++) {
                resolveSerializer.startTag((String) null, DiscreteRegistry.TAG_UID);
                resolveSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_UID, this.mUids.keyAt(i).intValue());
                this.mUids.valueAt(i).serialize(resolveSerializer);
                resolveSerializer.endTag((String) null, DiscreteRegistry.TAG_UID);
            }
            resolveSerializer.endTag((String) null, DiscreteRegistry.TAG_HISTORY);
            resolveSerializer.endDocument();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter, SimpleDateFormat simpleDateFormat, Date date, String str, int i) {
            int size = this.mUids.size();
            for (int i2 = 0; i2 < size; i2++) {
                printWriter.print(str);
                printWriter.print("Uid: ");
                printWriter.print(this.mUids.keyAt(i2));
                printWriter.println();
                this.mUids.valueAt(i2).dump(printWriter, simpleDateFormat, date, str + "  ", i);
            }
        }

        private DiscreteUidOps getOrCreateDiscreteUidOps(int i) {
            DiscreteUidOps discreteUidOps = this.mUids.get(Integer.valueOf(i));
            if (discreteUidOps != null) {
                return discreteUidOps;
            }
            DiscreteUidOps discreteUidOps2 = new DiscreteUidOps();
            this.mUids.put(Integer.valueOf(i), discreteUidOps2);
            return discreteUidOps2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void readFromFile(File file, long j) {
            TypedXmlPullParser resolvePullParser;
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    resolvePullParser = Xml.resolvePullParser(fileInputStream);
                    XmlUtils.beginDocument(resolvePullParser, DiscreteRegistry.TAG_HISTORY);
                } finally {
                    try {
                        try {
                        } catch (IOException unused) {
                            return;
                        }
                    } finally {
                        try {
                            fileInputStream.close();
                        } catch (IOException unused2) {
                        }
                    }
                }
                if (resolvePullParser.getAttributeInt((String) null, DiscreteRegistry.ATTR_VERSION) != 1) {
                    throw new IllegalStateException("Dropping unsupported discrete history " + file);
                }
                int depth = resolvePullParser.getDepth();
                while (XmlUtils.nextElementWithin(resolvePullParser, depth)) {
                    if (DiscreteRegistry.TAG_UID.equals(resolvePullParser.getName())) {
                        getOrCreateDiscreteUidOps(resolvePullParser.getAttributeInt((String) null, DiscreteRegistry.ATTR_UID, -1)).deserialize(resolvePullParser, j);
                    }
                }
            } catch (FileNotFoundException unused3) {
            }
        }
    }

    private void createDiscreteAccessDir() {
        if (this.mDiscreteAccessDir.exists()) {
            return;
        }
        if (!this.mDiscreteAccessDir.mkdirs()) {
            Slog.e(TAG, "Failed to create DiscreteRegistry directory");
        }
        FileUtils.setPermissions(this.mDiscreteAccessDir.getPath(), 505, -1, -1);
    }

    private void persistDiscreteOpsLocked(DiscreteOps discreteOps) {
        FileOutputStream fileOutputStream;
        long epochMilli = Instant.now().toEpochMilli();
        AtomicFile atomicFile = new AtomicFile(new File(this.mDiscreteAccessDir, epochMilli + DISCRETE_HISTORY_FILE_SUFFIX));
        try {
            fileOutputStream = atomicFile.startWrite();
        } catch (Throwable th) {
            th = th;
            fileOutputStream = null;
        }
        try {
            discreteOps.writeToStream(fileOutputStream);
            atomicFile.finishWrite(fileOutputStream);
        } catch (Throwable th2) {
            th = th2;
            Slog.e(TAG, "Error writing timeline state: " + th.getMessage() + " " + Arrays.toString(th.getStackTrace()));
            if (fileOutputStream != null) {
                atomicFile.failWrite(fileOutputStream);
            }
        }
    }

    private void deleteOldDiscreteHistoryFilesLocked() {
        File[] listFiles = this.mDiscreteAccessDir.listFiles();
        if (listFiles == null || listFiles.length <= 0) {
            return;
        }
        for (File file : listFiles) {
            String name = file.getName();
            if (name.endsWith(DISCRETE_HISTORY_FILE_SUFFIX)) {
                try {
                    if (Instant.now().minus(sDiscreteHistoryCutoff, (TemporalUnit) ChronoUnit.MILLIS).toEpochMilli() > Long.valueOf(name.substring(0, name.length() - 2)).longValue()) {
                        file.delete();
                        Slog.e(TAG, "Deleting file " + name);
                    }
                } catch (Throwable th) {
                    Slog.e(TAG, "Error while cleaning timeline files: ", th);
                }
            }
        }
    }

    private void createDiscreteAccessDirLocked() {
        if (this.mDiscreteAccessDir.exists()) {
            return;
        }
        if (!this.mDiscreteAccessDir.mkdirs()) {
            Slog.e(TAG, "Failed to create DiscreteRegistry directory");
        }
        FileUtils.setPermissions(this.mDiscreteAccessDir.getPath(), 505, -1, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DiscreteUidOps {
        ArrayMap<String, DiscretePackageOps> mPackages = new ArrayMap<>();

        DiscreteUidOps() {
        }

        boolean isEmpty() {
            return this.mPackages.isEmpty();
        }

        void merge(DiscreteUidOps discreteUidOps) {
            int size = discreteUidOps.mPackages.size();
            for (int i = 0; i < size; i++) {
                String keyAt = discreteUidOps.mPackages.keyAt(i);
                getOrCreateDiscretePackageOps(keyAt).merge(discreteUidOps.mPackages.valueAt(i));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long j, long j2, int i, String str, String[] strArr, String str2, int i2, int i3, ArrayMap<Integer, AttributionChain> arrayMap) {
            if ((i & 2) != 0) {
                ArrayMap<String, DiscretePackageOps> arrayMap2 = new ArrayMap<>();
                arrayMap2.put(str, getOrCreateDiscretePackageOps(str));
                this.mPackages = arrayMap2;
            }
            for (int size = this.mPackages.size() - 1; size >= 0; size--) {
                this.mPackages.valueAt(size).filter(j, j2, i, strArr, str2, i2, i3, this.mPackages.keyAt(size), arrayMap);
                if (this.mPackages.valueAt(size).isEmpty()) {
                    this.mPackages.removeAt(size);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long j) {
            int size = this.mPackages.size();
            for (int i = 0; i < size; i++) {
                this.mPackages.valueAt(i).offsetHistory(j);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearPackage(String str) {
            this.mPackages.remove(str);
        }

        void addDiscreteAccess(int i, String str, String str2, int i2, int i3, long j, long j2, int i4, int i5) {
            getOrCreateDiscretePackageOps(str).addDiscreteAccess(i, str2, i2, i3, j, j2, i4, i5);
        }

        private DiscretePackageOps getOrCreateDiscretePackageOps(String str) {
            DiscretePackageOps discretePackageOps = this.mPackages.get(str);
            if (discretePackageOps != null) {
                return discretePackageOps;
            }
            DiscretePackageOps discretePackageOps2 = new DiscretePackageOps();
            this.mPackages.put(str, discretePackageOps2);
            return discretePackageOps2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistory(AppOpsManager.HistoricalOps historicalOps, int i, ArrayMap<Integer, AttributionChain> arrayMap) {
            int size = this.mPackages.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mPackages.valueAt(i2).applyToHistory(historicalOps, i, this.mPackages.keyAt(i2), arrayMap);
            }
        }

        void serialize(TypedXmlSerializer typedXmlSerializer) throws Exception {
            int size = this.mPackages.size();
            for (int i = 0; i < size; i++) {
                typedXmlSerializer.startTag((String) null, DiscreteRegistry.TAG_PACKAGE);
                typedXmlSerializer.attribute((String) null, DiscreteRegistry.ATTR_PACKAGE_NAME, this.mPackages.keyAt(i));
                this.mPackages.valueAt(i).serialize(typedXmlSerializer);
                typedXmlSerializer.endTag((String) null, DiscreteRegistry.TAG_PACKAGE);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter, SimpleDateFormat simpleDateFormat, Date date, String str, int i) {
            int size = this.mPackages.size();
            for (int i2 = 0; i2 < size; i2++) {
                printWriter.print(str);
                printWriter.print("Package: ");
                printWriter.print(this.mPackages.keyAt(i2));
                printWriter.println();
                this.mPackages.valueAt(i2).dump(printWriter, simpleDateFormat, date, str + "  ", i);
            }
        }

        void deserialize(TypedXmlPullParser typedXmlPullParser, long j) throws Exception {
            int depth = typedXmlPullParser.getDepth();
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if (DiscreteRegistry.TAG_PACKAGE.equals(typedXmlPullParser.getName())) {
                    getOrCreateDiscretePackageOps(typedXmlPullParser.getAttributeValue((String) null, DiscreteRegistry.ATTR_PACKAGE_NAME)).deserialize(typedXmlPullParser, j);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DiscretePackageOps {
        ArrayMap<Integer, DiscreteOp> mPackageOps = new ArrayMap<>();

        DiscretePackageOps() {
        }

        boolean isEmpty() {
            return this.mPackageOps.isEmpty();
        }

        void addDiscreteAccess(int i, String str, int i2, int i3, long j, long j2, int i4, int i5) {
            getOrCreateDiscreteOp(i).addDiscreteAccess(str, i2, i3, j, j2, i4, i5);
        }

        void merge(DiscretePackageOps discretePackageOps) {
            int size = discretePackageOps.mPackageOps.size();
            for (int i = 0; i < size; i++) {
                int intValue = discretePackageOps.mPackageOps.keyAt(i).intValue();
                getOrCreateDiscreteOp(intValue).merge(discretePackageOps.mPackageOps.valueAt(i));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long j, long j2, int i, String[] strArr, String str, int i2, int i3, String str2, ArrayMap<Integer, AttributionChain> arrayMap) {
            for (int size = this.mPackageOps.size() - 1; size >= 0; size--) {
                int intValue = this.mPackageOps.keyAt(size).intValue();
                if ((i & 8) != 0 && !ArrayUtils.contains(strArr, AppOpsManager.opToPublicName(intValue))) {
                    this.mPackageOps.removeAt(size);
                }
                this.mPackageOps.valueAt(size).filter(j, j2, i, str, i2, i3, str2, this.mPackageOps.keyAt(size).intValue(), arrayMap);
                if (this.mPackageOps.valueAt(size).isEmpty()) {
                    this.mPackageOps.removeAt(size);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long j) {
            int size = this.mPackageOps.size();
            for (int i = 0; i < size; i++) {
                this.mPackageOps.valueAt(i).offsetHistory(j);
            }
        }

        private DiscreteOp getOrCreateDiscreteOp(int i) {
            DiscreteOp discreteOp = this.mPackageOps.get(Integer.valueOf(i));
            if (discreteOp != null) {
                return discreteOp;
            }
            DiscreteOp discreteOp2 = new DiscreteOp();
            this.mPackageOps.put(Integer.valueOf(i), discreteOp2);
            return discreteOp2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistory(AppOpsManager.HistoricalOps historicalOps, int i, String str, ArrayMap<Integer, AttributionChain> arrayMap) {
            int size = this.mPackageOps.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mPackageOps.valueAt(i2).applyToHistory(historicalOps, i, str, this.mPackageOps.keyAt(i2).intValue(), arrayMap);
            }
        }

        void serialize(TypedXmlSerializer typedXmlSerializer) throws Exception {
            int size = this.mPackageOps.size();
            for (int i = 0; i < size; i++) {
                typedXmlSerializer.startTag((String) null, DiscreteRegistry.TAG_OP);
                typedXmlSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_OP_ID, this.mPackageOps.keyAt(i).intValue());
                this.mPackageOps.valueAt(i).serialize(typedXmlSerializer);
                typedXmlSerializer.endTag((String) null, DiscreteRegistry.TAG_OP);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter, SimpleDateFormat simpleDateFormat, Date date, String str, int i) {
            int size = this.mPackageOps.size();
            for (int i2 = 0; i2 < size; i2++) {
                printWriter.print(str);
                printWriter.print(AppOpsManager.opToName(this.mPackageOps.keyAt(i2).intValue()));
                printWriter.println();
                this.mPackageOps.valueAt(i2).dump(printWriter, simpleDateFormat, date, str + "  ", i);
            }
        }

        void deserialize(TypedXmlPullParser typedXmlPullParser, long j) throws Exception {
            int depth = typedXmlPullParser.getDepth();
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if (DiscreteRegistry.TAG_OP.equals(typedXmlPullParser.getName())) {
                    getOrCreateDiscreteOp(typedXmlPullParser.getAttributeInt((String) null, DiscreteRegistry.ATTR_OP_ID)).deserialize(typedXmlPullParser, j);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DiscreteOp {
        ArrayMap<String, List<DiscreteOpEvent>> mAttributedOps = new ArrayMap<>();

        DiscreteOp() {
        }

        boolean isEmpty() {
            return this.mAttributedOps.isEmpty();
        }

        void merge(DiscreteOp discreteOp) {
            int size = discreteOp.mAttributedOps.size();
            for (int i = 0; i < size; i++) {
                String keyAt = discreteOp.mAttributedOps.keyAt(i);
                List<DiscreteOpEvent> valueAt = discreteOp.mAttributedOps.valueAt(i);
                this.mAttributedOps.put(keyAt, DiscreteRegistry.stableListMerge(getOrCreateDiscreteOpEventsList(keyAt), valueAt));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void filter(long j, long j2, int i, String str, int i2, int i3, String str2, int i4, ArrayMap<Integer, AttributionChain> arrayMap) {
            if ((i & 4) != 0) {
                ArrayMap<String, List<DiscreteOpEvent>> arrayMap2 = new ArrayMap<>();
                arrayMap2.put(str, getOrCreateDiscreteOpEventsList(str));
                this.mAttributedOps = arrayMap2;
            }
            for (int size = this.mAttributedOps.size() - 1; size >= 0; size--) {
                String keyAt = this.mAttributedOps.keyAt(size);
                List<DiscreteOpEvent> filterEventsList = DiscreteRegistry.filterEventsList(this.mAttributedOps.valueAt(size), j, j2, i2, i3, str2, i4, this.mAttributedOps.keyAt(size), arrayMap);
                this.mAttributedOps.put(keyAt, filterEventsList);
                if (filterEventsList.size() == 0) {
                    this.mAttributedOps.removeAt(size);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void offsetHistory(long j) {
            DiscreteOp discreteOp = this;
            int size = discreteOp.mAttributedOps.size();
            int i = 0;
            while (i < size) {
                List<DiscreteOpEvent> valueAt = discreteOp.mAttributedOps.valueAt(i);
                int size2 = valueAt.size();
                int i2 = 0;
                while (i2 < size2) {
                    DiscreteOpEvent discreteOpEvent = valueAt.get(i2);
                    valueAt.set(i2, new DiscreteOpEvent(discreteOpEvent.mNoteTime - j, discreteOpEvent.mNoteDuration, discreteOpEvent.mUidState, discreteOpEvent.mOpFlag, discreteOpEvent.mAttributionFlags, discreteOpEvent.mAttributionChainId));
                    i2++;
                    discreteOp = this;
                }
                i++;
                discreteOp = this;
            }
        }

        void addDiscreteAccess(String str, int i, int i2, long j, long j2, int i3, int i4) {
            List<DiscreteOpEvent> orCreateDiscreteOpEventsList = getOrCreateDiscreteOpEventsList(str);
            for (int size = orCreateDiscreteOpEventsList.size(); size > 0; size--) {
                DiscreteOpEvent discreteOpEvent = orCreateDiscreteOpEventsList.get(size - 1);
                if (DiscreteRegistry.discretizeTimeStamp(discreteOpEvent.mNoteTime) < DiscreteRegistry.discretizeTimeStamp(j)) {
                    break;
                }
                if (discreteOpEvent.mOpFlag == i && discreteOpEvent.mUidState == i2) {
                    if (discreteOpEvent.mAttributionFlags == i3) {
                        if (discreteOpEvent.mAttributionChainId == i4) {
                            if (DiscreteRegistry.discretizeDuration(j2) == DiscreteRegistry.discretizeDuration(discreteOpEvent.mNoteDuration)) {
                                return;
                            }
                            orCreateDiscreteOpEventsList.add(size, new DiscreteOpEvent(j, j2, i2, i, i3, i4));
                        }
                    }
                }
            }
            orCreateDiscreteOpEventsList.add(size, new DiscreteOpEvent(j, j2, i2, i, i3, i4));
        }

        private List<DiscreteOpEvent> getOrCreateDiscreteOpEventsList(String str) {
            List<DiscreteOpEvent> list = this.mAttributedOps.get(str);
            if (list != null) {
                return list;
            }
            ArrayList arrayList = new ArrayList();
            this.mAttributedOps.put(str, arrayList);
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void applyToHistory(AppOpsManager.HistoricalOps historicalOps, int i, String str, int i2, ArrayMap<Integer, AttributionChain> arrayMap) {
            AttributionChain attributionChain;
            AttributionChain.OpEvent opEvent;
            int size = this.mAttributedOps.size();
            for (int i3 = 0; i3 < size; i3++) {
                String keyAt = this.mAttributedOps.keyAt(i3);
                List<DiscreteOpEvent> valueAt = this.mAttributedOps.valueAt(i3);
                int size2 = valueAt.size();
                int i4 = 0;
                while (i4 < size2) {
                    DiscreteOpEvent discreteOpEvent = valueAt.get(i4);
                    int i5 = discreteOpEvent.mAttributionChainId;
                    historicalOps.addDiscreteAccess(i2, i, str, keyAt, discreteOpEvent.mUidState, discreteOpEvent.mOpFlag, DiscreteRegistry.discretizeTimeStamp(discreteOpEvent.mNoteTime), DiscreteRegistry.discretizeDuration(discreteOpEvent.mNoteDuration), (i5 == -1 || arrayMap == null || (attributionChain = arrayMap.get(Integer.valueOf(i5))) == null || !attributionChain.isComplete() || !attributionChain.isStart(str, i, keyAt, i2, discreteOpEvent) || (opEvent = attributionChain.mLastVisibleEvent) == null) ? null : new AppOpsManager.OpEventProxyInfo(opEvent.mUid, opEvent.mPkgName, opEvent.mAttributionTag));
                    i4++;
                    size2 = size2;
                    valueAt = valueAt;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter, SimpleDateFormat simpleDateFormat, Date date, String str, int i) {
            int size = this.mAttributedOps.size();
            for (int i2 = 0; i2 < size; i2++) {
                printWriter.print(str);
                printWriter.print("Attribution: ");
                printWriter.print(this.mAttributedOps.keyAt(i2));
                printWriter.println();
                List<DiscreteOpEvent> valueAt = this.mAttributedOps.valueAt(i2);
                int size2 = valueAt.size();
                for (int max = i < 1 ? 0 : Math.max(0, size2 - i); max < size2; max++) {
                    valueAt.get(max).dump(printWriter, simpleDateFormat, date, str + "  ");
                }
            }
        }

        void serialize(TypedXmlSerializer typedXmlSerializer) throws Exception {
            int size = this.mAttributedOps.size();
            for (int i = 0; i < size; i++) {
                typedXmlSerializer.startTag((String) null, DiscreteRegistry.TAG_TAG);
                if (this.mAttributedOps.keyAt(i) != null) {
                    typedXmlSerializer.attribute((String) null, DiscreteRegistry.ATTR_TAG, this.mAttributedOps.keyAt(i));
                }
                List<DiscreteOpEvent> valueAt = this.mAttributedOps.valueAt(i);
                int size2 = valueAt.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    typedXmlSerializer.startTag((String) null, DiscreteRegistry.TAG_ENTRY);
                    valueAt.get(i2).serialize(typedXmlSerializer);
                    typedXmlSerializer.endTag((String) null, DiscreteRegistry.TAG_ENTRY);
                }
                typedXmlSerializer.endTag((String) null, DiscreteRegistry.TAG_TAG);
            }
        }

        void deserialize(TypedXmlPullParser typedXmlPullParser, long j) throws Exception {
            int depth = typedXmlPullParser.getDepth();
            while (XmlUtils.nextElementWithin(typedXmlPullParser, depth)) {
                if (DiscreteRegistry.TAG_TAG.equals(typedXmlPullParser.getName())) {
                    List<DiscreteOpEvent> orCreateDiscreteOpEventsList = getOrCreateDiscreteOpEventsList(typedXmlPullParser.getAttributeValue((String) null, DiscreteRegistry.ATTR_TAG));
                    int depth2 = typedXmlPullParser.getDepth();
                    while (XmlUtils.nextElementWithin(typedXmlPullParser, depth2)) {
                        if (DiscreteRegistry.TAG_ENTRY.equals(typedXmlPullParser.getName())) {
                            long attributeLong = typedXmlPullParser.getAttributeLong((String) null, DiscreteRegistry.ATTR_NOTE_TIME);
                            long attributeLong2 = typedXmlPullParser.getAttributeLong((String) null, DiscreteRegistry.ATTR_NOTE_DURATION, -1L);
                            int attributeInt = typedXmlPullParser.getAttributeInt((String) null, DiscreteRegistry.ATTR_UID_STATE);
                            int attributeInt2 = typedXmlPullParser.getAttributeInt((String) null, DiscreteRegistry.ATTR_FLAGS);
                            int attributeInt3 = typedXmlPullParser.getAttributeInt((String) null, DiscreteRegistry.ATTR_ATTRIBUTION_FLAGS, 0);
                            int attributeInt4 = typedXmlPullParser.getAttributeInt((String) null, DiscreteRegistry.ATTR_CHAIN_ID, -1);
                            if (attributeLong + attributeLong2 >= j) {
                                orCreateDiscreteOpEventsList.add(new DiscreteOpEvent(attributeLong, attributeLong2, attributeInt, attributeInt2, attributeInt3, attributeInt4));
                            }
                        }
                    }
                    Collections.sort(orCreateDiscreteOpEventsList, new Comparator() { // from class: com.android.server.appop.DiscreteRegistry$DiscreteOp$$ExternalSyntheticLambda0
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            int lambda$deserialize$0;
                            lambda$deserialize$0 = DiscreteRegistry.DiscreteOp.lambda$deserialize$0((DiscreteRegistry.DiscreteOpEvent) obj, (DiscreteRegistry.DiscreteOpEvent) obj2);
                            return lambda$deserialize$0;
                        }
                    });
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$deserialize$0(DiscreteOpEvent discreteOpEvent, DiscreteOpEvent discreteOpEvent2) {
            long j = discreteOpEvent.mNoteTime;
            long j2 = discreteOpEvent2.mNoteTime;
            if (j < j2) {
                return -1;
            }
            return j == j2 ? 0 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DiscreteOpEvent {
        final int mAttributionChainId;
        final int mAttributionFlags;
        final long mNoteDuration;
        final long mNoteTime;
        final int mOpFlag;
        final int mUidState;

        DiscreteOpEvent(long j, long j2, int i, int i2, int i3, int i4) {
            this.mNoteTime = j;
            this.mNoteDuration = j2;
            this.mUidState = i;
            this.mOpFlag = i2;
            this.mAttributionFlags = i3;
            this.mAttributionChainId = i4;
        }

        public boolean equalsExceptDuration(DiscreteOpEvent discreteOpEvent) {
            return this.mNoteTime == discreteOpEvent.mNoteTime && this.mUidState == discreteOpEvent.mUidState && this.mOpFlag == discreteOpEvent.mOpFlag && this.mAttributionFlags == discreteOpEvent.mAttributionFlags && this.mAttributionChainId == discreteOpEvent.mAttributionChainId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(PrintWriter printWriter, SimpleDateFormat simpleDateFormat, Date date, String str) {
            printWriter.print(str);
            printWriter.print("Access [");
            printWriter.print(AppOpsManager.getUidStateName(this.mUidState));
            printWriter.print("-");
            printWriter.print(AppOpsManager.flagsToString(this.mOpFlag));
            printWriter.print("] at ");
            date.setTime(DiscreteRegistry.discretizeTimeStamp(this.mNoteTime));
            printWriter.print(simpleDateFormat.format(date));
            if (this.mNoteDuration != -1) {
                printWriter.print(" for ");
                printWriter.print(DiscreteRegistry.discretizeDuration(this.mNoteDuration));
                printWriter.print(" milliseconds ");
            }
            if (this.mAttributionFlags != 0) {
                printWriter.print(" attribution flags=");
                printWriter.print(this.mAttributionFlags);
                printWriter.print(" with chainId=");
                printWriter.print(this.mAttributionChainId);
            }
            printWriter.println();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void serialize(TypedXmlSerializer typedXmlSerializer) throws Exception {
            typedXmlSerializer.attributeLong((String) null, DiscreteRegistry.ATTR_NOTE_TIME, this.mNoteTime);
            long j = this.mNoteDuration;
            if (j != -1) {
                typedXmlSerializer.attributeLong((String) null, DiscreteRegistry.ATTR_NOTE_DURATION, j);
            }
            int i = this.mAttributionFlags;
            if (i != 0) {
                typedXmlSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_ATTRIBUTION_FLAGS, i);
            }
            int i2 = this.mAttributionChainId;
            if (i2 != -1) {
                typedXmlSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_CHAIN_ID, i2);
            }
            typedXmlSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_UID_STATE, this.mUidState);
            typedXmlSerializer.attributeInt((String) null, DiscreteRegistry.ATTR_FLAGS, this.mOpFlag);
        }
    }

    private static int[] parseOpsList(String str) {
        String[] split = str.isEmpty() ? new String[0] : str.split(",");
        int length = split.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            try {
                iArr[i] = Integer.parseInt(split[i]);
            } catch (NumberFormatException e) {
                Slog.e(TAG, "Failed to parse Discrete ops list: " + e.getMessage());
                return parseOpsList(DEFAULT_DISCRETE_OPS);
            }
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<DiscreteOpEvent> stableListMerge(List<DiscreteOpEvent> list, List<DiscreteOpEvent> list2) {
        int i;
        int i2;
        int size = list.size();
        int size2 = list2.size();
        ArrayList arrayList = new ArrayList(size + size2);
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i3 >= size && i4 >= size2) {
                return arrayList;
            }
            if (i3 == size) {
                i = i4 + 1;
                arrayList.add(list2.get(i4));
            } else {
                if (i4 == size2) {
                    i2 = i3 + 1;
                    arrayList.add(list.get(i3));
                } else if (list.get(i3).mNoteTime < list2.get(i4).mNoteTime) {
                    i2 = i3 + 1;
                    arrayList.add(list.get(i3));
                } else {
                    i = i4 + 1;
                    arrayList.add(list2.get(i4));
                }
                i3 = i2;
            }
            i4 = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<DiscreteOpEvent> filterEventsList(List<DiscreteOpEvent> list, long j, long j2, int i, int i2, String str, int i3, String str2, ArrayMap<Integer, AttributionChain> arrayMap) {
        int size = list.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i4 = 0; i4 < size; i4++) {
            DiscreteOpEvent discreteOpEvent = list.get(i4);
            AttributionChain attributionChain = arrayMap.get(Integer.valueOf(discreteOpEvent.mAttributionChainId));
            if ((attributionChain == null || attributionChain.isStart(str, i2, str2, i3, discreteOpEvent) || !attributionChain.isComplete() || discreteOpEvent.mAttributionChainId == -1) && (discreteOpEvent.mOpFlag & i) != 0) {
                long j3 = discreteOpEvent.mNoteTime;
                if (discreteOpEvent.mNoteDuration + j3 > j && j3 < j2) {
                    arrayList.add(discreteOpEvent);
                }
            }
        }
        return arrayList;
    }

    private static boolean isDiscreteOp(int i, int i2) {
        return ArrayUtils.contains(sDiscreteOps, i) && (sDiscreteFlags & i2) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long discretizeTimeStamp(long j) {
        long j2 = sDiscreteHistoryQuantization;
        return (j / j2) * j2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long discretizeDuration(long j) {
        if (j == -1) {
            return -1L;
        }
        long j2 = sDiscreteHistoryQuantization;
        return j2 * (((j + j2) - 1) / j2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDebugMode(boolean z) {
        this.mDebugMode = z;
    }
}
