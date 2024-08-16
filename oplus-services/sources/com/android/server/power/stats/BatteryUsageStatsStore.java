package com.android.server.power.stats;

import android.content.Context;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import android.os.Handler;
import android.util.AtomicFile;
import android.util.Log;
import android.util.LongArray;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.power.stats.BatteryStatsImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class BatteryUsageStatsStore {
    private static final String BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP_PROPERTY = "BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP";
    private static final String BATTERY_USAGE_STATS_DIR = "battery-usage-stats";
    private static final List<BatteryUsageStatsQuery> BATTERY_USAGE_STATS_QUERY = List.of(new BatteryUsageStatsQuery.Builder().setMaxStatsAgeMs(0).includePowerModels().includeProcessStateData().build());
    private static final String CONFIG_FILENAME = "config";
    private static final String DIR_LOCK_FILENAME = ".lock";
    private static final long MAX_BATTERY_STATS_SNAPSHOT_STORAGE_BYTES = 102400;
    private static final String SNAPSHOT_FILE_EXTENSION = ".bus";
    private static final String TAG = "BatteryUsageStatsStore";
    private final BatteryStatsImpl mBatteryStats;
    private final BatteryUsageStatsProvider mBatteryUsageStatsProvider;
    private final AtomicFile mConfigFile;
    private final Context mContext;
    private final ReentrantLock mFileLock;
    private final Handler mHandler;
    private FileLock mJvmLock;
    private final File mLockFile;
    private final long mMaxStorageBytes;
    private final File mStoreDir;
    private boolean mSystemReady;

    public BatteryUsageStatsStore(Context context, BatteryStatsImpl batteryStatsImpl, File file, Handler handler) {
        this(context, batteryStatsImpl, file, handler, MAX_BATTERY_STATS_SNAPSHOT_STORAGE_BYTES);
    }

    @VisibleForTesting
    public BatteryUsageStatsStore(Context context, BatteryStatsImpl batteryStatsImpl, File file, Handler handler, long j) {
        this.mFileLock = new ReentrantLock();
        this.mContext = context;
        this.mBatteryStats = batteryStatsImpl;
        File file2 = new File(file, BATTERY_USAGE_STATS_DIR);
        this.mStoreDir = file2;
        this.mLockFile = new File(file2, DIR_LOCK_FILENAME);
        this.mConfigFile = new AtomicFile(new File(file2, CONFIG_FILENAME));
        this.mHandler = handler;
        this.mMaxStorageBytes = j;
        batteryStatsImpl.setBatteryResetListener(new BatteryStatsImpl.BatteryResetListener() { // from class: com.android.server.power.stats.BatteryUsageStatsStore$$ExternalSyntheticLambda0
            @Override // com.android.server.power.stats.BatteryStatsImpl.BatteryResetListener
            public final void prepareForBatteryStatsReset(int i) {
                BatteryUsageStatsStore.this.prepareForBatteryStatsReset(i);
            }
        });
        this.mBatteryUsageStatsProvider = new BatteryUsageStatsProvider(context, batteryStatsImpl);
    }

    public void onSystemReady() {
        this.mSystemReady = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareForBatteryStatsReset(int i) {
        if (i == 1 || !this.mSystemReady) {
            return;
        }
        final List<BatteryUsageStats> batteryUsageStats = this.mBatteryUsageStatsProvider.getBatteryUsageStats(BATTERY_USAGE_STATS_QUERY);
        if (batteryUsageStats.isEmpty()) {
            Slog.wtf(TAG, "No battery usage stats generated");
        } else {
            this.mHandler.post(new Runnable() { // from class: com.android.server.power.stats.BatteryUsageStatsStore$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    BatteryUsageStatsStore.this.lambda$prepareForBatteryStatsReset$0(batteryUsageStats);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareForBatteryStatsReset$0(List list) {
        storeBatteryUsageStats((BatteryUsageStats) list.get(0));
    }

    private void storeBatteryUsageStats(BatteryUsageStats batteryUsageStats) {
        lockSnapshotDirectory();
        try {
            if (!this.mStoreDir.exists() && !this.mStoreDir.mkdirs()) {
                Slog.e(TAG, "Could not create a directory for battery usage stats snapshots");
                return;
            }
            try {
                writeXmlFileLocked(batteryUsageStats, makeSnapshotFilename(batteryUsageStats.getStatsEndTimestamp()));
            } catch (Exception e) {
                Slog.e(TAG, "Cannot save battery usage stats", e);
            }
            removeOldSnapshotsLocked();
        } finally {
            unlockSnapshotDirectory();
        }
    }

    public long[] listBatteryUsageStatsTimestamps() {
        LongArray longArray = new LongArray(100);
        lockSnapshotDirectory();
        try {
            for (File file : this.mStoreDir.listFiles()) {
                String name = file.getName();
                if (name.endsWith(SNAPSHOT_FILE_EXTENSION)) {
                    try {
                        longArray.add(Long.parseLong(name.substring(0, name.length() - 4)));
                    } catch (NumberFormatException unused) {
                        Slog.wtf(TAG, "Invalid format of BatteryUsageStats snapshot file name: " + name);
                    }
                }
            }
            unlockSnapshotDirectory();
            return longArray.toArray();
        } catch (Throwable th) {
            unlockSnapshotDirectory();
            throw th;
        }
    }

    public BatteryUsageStats loadBatteryUsageStats(long j) {
        lockSnapshotDirectory();
        try {
            try {
                return readXmlFileLocked(makeSnapshotFilename(j));
            } catch (Exception e) {
                Slog.e(TAG, "Cannot read battery usage stats", e);
                unlockSnapshotDirectory();
                return null;
            }
        } finally {
            unlockSnapshotDirectory();
        }
    }

    public void setLastBatteryUsageStatsBeforeResetAtomPullTimestamp(long j) {
        Properties properties = new Properties();
        lockSnapshotDirectory();
        try {
            try {
                FileInputStream openRead = this.mConfigFile.openRead();
                try {
                    properties.load(openRead);
                    if (openRead != null) {
                        openRead.close();
                    }
                } catch (Throwable th) {
                    if (openRead != null) {
                        try {
                            openRead.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e) {
                Slog.e(TAG, "Cannot load config file " + this.mConfigFile, e);
            }
            properties.put(BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP_PROPERTY, String.valueOf(j));
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = this.mConfigFile.startWrite();
                properties.store(fileOutputStream, "Statsd atom pull timestamps");
                this.mConfigFile.finishWrite(fileOutputStream);
            } catch (IOException e2) {
                this.mConfigFile.failWrite(fileOutputStream);
                Slog.e(TAG, "Cannot save config file " + this.mConfigFile, e2);
            }
        } finally {
            unlockSnapshotDirectory();
        }
    }

    public long getLastBatteryUsageStatsBeforeResetAtomPullTimestamp() {
        Properties properties = new Properties();
        lockSnapshotDirectory();
        try {
            try {
                FileInputStream openRead = this.mConfigFile.openRead();
                try {
                    properties.load(openRead);
                    if (openRead != null) {
                        openRead.close();
                    }
                } catch (Throwable th) {
                    if (openRead != null) {
                        try {
                            openRead.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (IOException e) {
                Slog.e(TAG, "Cannot load config file " + this.mConfigFile, e);
            }
            unlockSnapshotDirectory();
            return Long.parseLong(properties.getProperty(BATTERY_USAGE_STATS_BEFORE_RESET_TIMESTAMP_PROPERTY, "0"));
        } catch (Throwable th3) {
            unlockSnapshotDirectory();
            throw th3;
        }
    }

    private void lockSnapshotDirectory() {
        this.mFileLock.lock();
        try {
            this.mLockFile.getParentFile().mkdirs();
            this.mLockFile.createNewFile();
            this.mJvmLock = FileChannel.open(this.mLockFile.toPath(), StandardOpenOption.WRITE).lock();
        } catch (IOException e) {
            Log.e(TAG, "Cannot lock snapshot directory", e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.util.concurrent.locks.ReentrantLock] */
    private void unlockSnapshotDirectory() {
        try {
            try {
                this.mJvmLock.close();
            } catch (IOException e) {
                Log.e(TAG, "Cannot unlock snapshot directory", e);
            }
        } finally {
            this.mFileLock.unlock();
        }
    }

    private File makeSnapshotFilename(long j) {
        return new File(this.mStoreDir, String.format(Locale.ENGLISH, "%019d", Long.valueOf(j)) + SNAPSHOT_FILE_EXTENSION);
    }

    private void writeXmlFileLocked(BatteryUsageStats batteryUsageStats, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            TypedXmlSerializer newBinarySerializer = Xml.newBinarySerializer();
            newBinarySerializer.setOutput(fileOutputStream, StandardCharsets.UTF_8.name());
            newBinarySerializer.startDocument((String) null, Boolean.TRUE);
            batteryUsageStats.writeXml(newBinarySerializer);
            newBinarySerializer.endDocument();
            fileOutputStream.close();
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private BatteryUsageStats readXmlFileLocked(File file) throws IOException, XmlPullParserException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            TypedXmlPullParser newBinaryPullParser = Xml.newBinaryPullParser();
            newBinaryPullParser.setInput(fileInputStream, StandardCharsets.UTF_8.name());
            BatteryUsageStats createFromXml = BatteryUsageStats.createFromXml(newBinaryPullParser);
            fileInputStream.close();
            return createFromXml;
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void removeOldSnapshotsLocked() {
        Map.Entry firstEntry;
        TreeMap treeMap = new TreeMap();
        long j = 0;
        for (File file : this.mStoreDir.listFiles()) {
            long length = file.length();
            j += length;
            if (file.getName().endsWith(SNAPSHOT_FILE_EXTENSION)) {
                treeMap.put(file, Long.valueOf(length));
            }
        }
        while (j > this.mMaxStorageBytes && (firstEntry = treeMap.firstEntry()) != null) {
            File file2 = (File) firstEntry.getKey();
            if (!file2.delete()) {
                Slog.e(TAG, "Cannot delete battery usage stats " + file2);
            }
            j -= ((Long) firstEntry.getValue()).longValue();
            treeMap.remove(file2);
        }
    }

    public void removeAllSnapshots() {
        lockSnapshotDirectory();
        try {
            for (File file : this.mStoreDir.listFiles()) {
                if (file.getName().endsWith(SNAPSHOT_FILE_EXTENSION) && !file.delete()) {
                    Slog.e(TAG, "Cannot delete battery usage stats " + file);
                }
            }
        } finally {
            unlockSnapshotDirectory();
        }
    }
}
