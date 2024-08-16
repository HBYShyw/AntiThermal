package com.android.server.power;

import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.slice.SliceClientPermissions;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class WakeLockLog {
    private static final boolean DEBUG = false;
    private static final int FLAG_ACQUIRE_CAUSES_WAKEUP = 16;
    private static final int FLAG_ON_AFTER_RELEASE = 8;
    private static final int FLAG_SYSTEM_WAKELOCK = 32;
    private static final int LEVEL_DOZE_WAKE_LOCK = 6;
    private static final int LEVEL_DRAW_WAKE_LOCK = 7;
    private static final int LEVEL_FULL_WAKE_LOCK = 2;
    private static final int LEVEL_PARTIAL_WAKE_LOCK = 1;
    private static final int LEVEL_PROXIMITY_SCREEN_OFF_WAKE_LOCK = 5;
    private static final int LEVEL_SCREEN_BRIGHT_WAKE_LOCK = 4;
    private static final int LEVEL_SCREEN_DIM_WAKE_LOCK = 3;
    private static final int LEVEL_UNKNOWN = 0;
    private static final int LOG_SIZE = 10240;
    private static final int LOG_SIZE_MIN = 10;
    private static final int MASK_LOWER_6_BITS = 63;
    private static final int MASK_LOWER_7_BITS = 127;
    private static final int MAX_LOG_ENTRY_BYTE_SIZE = 9;
    private static final String TAG = "PowerManagerService.WLLog";
    private static final int TAG_DATABASE_SIZE = 128;
    private static final int TAG_DATABASE_SIZE_MAX = 128;
    private static final int TYPE_ACQUIRE = 1;
    private static final int TYPE_RELEASE = 2;
    private static final int TYPE_TIME_RESET = 0;
    private final SimpleDateFormat mDumpsysDateFormat;
    private final Injector mInjector;
    private final Object mLock;
    private final TheLog mLog;
    private final TagDatabase mTagDatabase;
    private static final String[] LEVEL_TO_STRING = {"unknown", "partial", "full", "screen-dim", "screen-bright", "prox", "doze", "draw"};
    private static final String[] REDUCED_TAG_PREFIXES = {"*job*/", "*gms_scheduler*/", "IntentOp:"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    /* JADX INFO: Access modifiers changed from: package-private */
    public WakeLockLog() {
        this(new Injector());
    }

    @VisibleForTesting
    WakeLockLog(Injector injector) {
        this.mLock = new Object();
        this.mInjector = injector;
        TagDatabase tagDatabase = new TagDatabase(injector);
        this.mTagDatabase = tagDatabase;
        this.mLog = new TheLog(injector, new EntryByteTranslator(tagDatabase), tagDatabase);
        this.mDumpsysDateFormat = injector.getDateFormat();
    }

    public void onWakeLockAcquired(String str, int i, int i2) {
        onWakeLockEvent(1, str, i, i2);
    }

    public void onWakeLockReleased(String str, int i) {
        onWakeLockEvent(2, str, i, 0);
    }

    public void dump(PrintWriter printWriter) {
        dump(printWriter, false);
    }

    @VisibleForTesting
    void dump(PrintWriter printWriter, boolean z) {
        try {
            synchronized (this.mLock) {
                printWriter.println("Wake Lock Log");
                Iterator<LogEntry> allItems = this.mLog.getAllItems(new LogEntry());
                int i = 0;
                int i2 = 0;
                while (allItems.hasNext()) {
                    LogEntry next = allItems.next();
                    if (next != null) {
                        if (next.type == 0) {
                            i2++;
                        } else {
                            i++;
                            next.dump(printWriter, this.mDumpsysDateFormat);
                        }
                    }
                }
                printWriter.println("  -");
                printWriter.println("  Events: " + i + ", Time-Resets: " + i2);
                StringBuilder sb = new StringBuilder();
                sb.append("  Buffer, Bytes used: ");
                sb.append(this.mLog.getUsedBufferSize());
                printWriter.println(sb.toString());
                if (z) {
                    printWriter.println("  " + this.mTagDatabase);
                }
            }
        } catch (Exception e) {
            printWriter.println("Exception dumping wake-lock log: " + e.toString());
        }
    }

    private void onWakeLockEvent(int i, String str, int i2, int i3) {
        if (str == null) {
            Slog.w(TAG, "Insufficient data to log wakelock [tag: " + str + ", ownerUid: " + i2 + ", flags: 0x" + Integer.toHexString(i3));
            return;
        }
        handleWakeLockEventInternal(i, tagNameReducer(str), i2, i == 1 ? translateFlagsFromPowerManager(i3) : 0, this.mInjector.currentTimeMillis());
    }

    private void handleWakeLockEventInternal(int i, String str, int i2, int i3, long j) {
        synchronized (this.mLock) {
            this.mLog.addEntry(new LogEntry(j, i, this.mTagDatabase.findOrCreateTag(str, i2, true), i3));
        }
    }

    int translateFlagsFromPowerManager(int i) {
        int i2 = 65535 & i;
        int i3 = 1;
        if (i2 != 1) {
            i3 = 6;
            if (i2 == 6) {
                i3 = 3;
            } else if (i2 == 10) {
                i3 = 4;
            } else if (i2 == 26) {
                i3 = 2;
            } else if (i2 == 32) {
                i3 = 5;
            } else if (i2 != 64) {
                if (i2 != 128) {
                    Slog.w(TAG, "Unsupported lock level for logging, flags: " + i);
                    i3 = 0;
                } else {
                    i3 = 7;
                }
            }
        }
        if ((268435456 & i) != 0) {
            i3 |= 16;
        }
        if ((536870912 & i) != 0) {
            i3 |= 8;
        }
        return (Integer.MIN_VALUE & i) != 0 ? i3 | 32 : i3;
    }

    private String tagNameReducer(String str) {
        String str2 = null;
        if (str == null) {
            return null;
        }
        String[] strArr = REDUCED_TAG_PREFIXES;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String str3 = strArr[i];
            if (str.startsWith(str3)) {
                str2 = str3;
                break;
            }
            i++;
        }
        if (str2 == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append((CharSequence) str, 0, str2.length());
        int max = Math.max(str.lastIndexOf(SliceClientPermissions.SliceAuthority.DELIMITER), str.lastIndexOf("."));
        int length2 = sb.length();
        boolean z = true;
        while (length2 < max) {
            char charAt = str.charAt(length2);
            boolean z2 = charAt == '.' || charAt == '/';
            if (z2 || z) {
                sb.append(charAt);
            }
            length2++;
            z = z2;
        }
        sb.append(str.substring(length2));
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class LogEntry {
        public int flags;
        public TagData tag;
        public long time;
        public int type;

        LogEntry() {
        }

        LogEntry(long j, int i, TagData tagData, int i2) {
            set(j, i, tagData, i2);
        }

        public void set(long j, int i, TagData tagData, int i2) {
            this.time = j;
            this.type = i;
            this.tag = tagData;
            this.flags = i2;
        }

        public void dump(PrintWriter printWriter, SimpleDateFormat simpleDateFormat) {
            printWriter.println("  " + toStringInternal(simpleDateFormat));
        }

        public String toString() {
            return toStringInternal(WakeLockLog.DATE_FORMAT);
        }

        private String toStringInternal(SimpleDateFormat simpleDateFormat) {
            StringBuilder sb = new StringBuilder();
            if (this.type == 0) {
                return simpleDateFormat.format(new Date(this.time)) + " - RESET";
            }
            sb.append(simpleDateFormat.format(new Date(this.time)));
            sb.append(" - ");
            TagData tagData = this.tag;
            sb.append(tagData == null ? "---" : Integer.valueOf(tagData.ownerUid));
            sb.append(" - ");
            sb.append(this.type == 1 ? "ACQ" : "REL");
            sb.append(" ");
            TagData tagData2 = this.tag;
            sb.append(tagData2 == null ? "UNKNOWN" : tagData2.tag);
            if (this.type == 1) {
                sb.append(" (");
                flagsToString(sb);
                sb.append(")");
            }
            return sb.toString();
        }

        private void flagsToString(StringBuilder sb) {
            sb.append(WakeLockLog.LEVEL_TO_STRING[this.flags & 7]);
            if ((this.flags & 8) == 8) {
                sb.append(",on-after-release");
            }
            if ((this.flags & 16) == 16) {
                sb.append(",acq-causes-wake");
            }
            if ((this.flags & 32) == 32) {
                sb.append(",system-wakelock");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class EntryByteTranslator {
        static final int ERROR_TIME_IS_NEGATIVE = -1;
        static final int ERROR_TIME_TOO_LARGE = -2;
        private final TagDatabase mTagDatabase;

        private int getRelativeTime(long j, long j2) {
            if (j2 < j) {
                return -1;
            }
            long j3 = j2 - j;
            if (j3 > 255) {
                return -2;
            }
            return (int) j3;
        }

        EntryByteTranslator(TagDatabase tagDatabase) {
            this.mTagDatabase = tagDatabase;
        }

        LogEntry fromBytes(byte[] bArr, long j, LogEntry logEntry) {
            if (bArr == null || bArr.length == 0) {
                return null;
            }
            if (logEntry == null) {
                logEntry = new LogEntry();
            }
            int i = bArr[0];
            int i2 = (i >> 6) & 3;
            if ((i2 & 2) == 2) {
                i2 = 2;
            }
            if (i2 != 0) {
                if (i2 != 1) {
                    if (i2 == 2) {
                        if (bArr.length >= 2) {
                            logEntry.set((bArr[1] & 255) + j, 2, this.mTagDatabase.getTag(i & 127), 0);
                            return logEntry;
                        }
                    } else {
                        Slog.w(WakeLockLog.TAG, "Type not recognized [" + i2 + "]", new Exception());
                    }
                } else if (bArr.length >= 3) {
                    int i3 = i & WakeLockLog.MASK_LOWER_6_BITS;
                    logEntry.set((bArr[2] & 255) + j, 1, this.mTagDatabase.getTag(bArr[1] & Byte.MAX_VALUE), i3);
                    return logEntry;
                }
            } else if (bArr.length >= 9) {
                logEntry.set(((bArr[1] & 255) << 56) | ((bArr[2] & 255) << 48) | ((bArr[3] & 255) << 40) | ((bArr[4] & 255) << 32) | ((bArr[5] & 255) << 24) | ((bArr[6] & 255) << 16) | ((bArr[7] & 255) << 8) | (bArr[8] & 255), 0, null, 0);
                return logEntry;
            }
            return null;
        }

        int toBytes(LogEntry logEntry, byte[] bArr, long j) {
            int i = logEntry.type;
            if (i == 0) {
                long j2 = logEntry.time;
                if (bArr != null && bArr.length >= 9) {
                    bArr[0] = 0;
                    bArr[1] = (byte) ((j2 >> 56) & 255);
                    bArr[2] = (byte) ((j2 >> 48) & 255);
                    bArr[3] = (byte) ((j2 >> 40) & 255);
                    bArr[4] = (byte) ((j2 >> 32) & 255);
                    bArr[5] = (byte) ((j2 >> 24) & 255);
                    bArr[6] = (byte) ((j2 >> 16) & 255);
                    bArr[7] = (byte) ((j2 >> 8) & 255);
                    bArr[8] = (byte) (j2 & 255);
                }
                return 9;
            }
            if (i == 1) {
                if (bArr == null || bArr.length < 3) {
                    return 3;
                }
                int relativeTime = getRelativeTime(j, logEntry.time);
                if (relativeTime < 0) {
                    return relativeTime;
                }
                bArr[0] = (byte) ((logEntry.flags & WakeLockLog.MASK_LOWER_6_BITS) | 64);
                bArr[1] = (byte) this.mTagDatabase.getTagIndex(logEntry.tag);
                bArr[2] = (byte) (relativeTime & 255);
                return 3;
            }
            if (i == 2) {
                if (bArr != null && bArr.length >= 2) {
                    int relativeTime2 = getRelativeTime(j, logEntry.time);
                    if (relativeTime2 < 0) {
                        return relativeTime2;
                    }
                    bArr[0] = (byte) (this.mTagDatabase.getTagIndex(logEntry.tag) | 128);
                    bArr[1] = (byte) (relativeTime2 & 255);
                }
                return 2;
            }
            throw new RuntimeException("Unknown type " + logEntry);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TheLog {
        private final byte[] mBuffer;
        private final TagDatabase mTagDatabase;
        private final EntryByteTranslator mTranslator;
        private final byte[] mTempBuffer = new byte[9];
        private final byte[] mReadWriteTempBuffer = new byte[9];
        private int mStart = 0;
        private int mEnd = 0;
        private long mStartTime = 0;
        private long mLatestTime = 0;
        private long mChangeCount = 0;

        TheLog(Injector injector, EntryByteTranslator entryByteTranslator, TagDatabase tagDatabase) {
            this.mBuffer = new byte[Math.max(injector.getLogSize(), 10)];
            this.mTranslator = entryByteTranslator;
            this.mTagDatabase = tagDatabase;
            tagDatabase.setCallback(new TagDatabase.Callback() { // from class: com.android.server.power.WakeLockLog.TheLog.1
                @Override // com.android.server.power.WakeLockLog.TagDatabase.Callback
                public void onIndexRemoved(int i) {
                    TheLog.this.removeTagIndex(i);
                }
            });
        }

        int getUsedBufferSize() {
            return this.mBuffer.length - getAvailableSpace();
        }

        void addEntry(LogEntry logEntry) {
            if (isBufferEmpty()) {
                long j = logEntry.time;
                this.mLatestTime = j;
                this.mStartTime = j;
            }
            int bytes = this.mTranslator.toBytes(logEntry, this.mTempBuffer, this.mLatestTime);
            if (bytes == -1) {
                return;
            }
            if (bytes == -2) {
                addEntry(new LogEntry(logEntry.time, 0, null, 0));
                bytes = this.mTranslator.toBytes(logEntry, this.mTempBuffer, this.mLatestTime);
            }
            if (bytes > 9 || bytes <= 0) {
                Slog.w(WakeLockLog.TAG, "Log entry size is out of expected range: " + bytes);
                return;
            }
            if (makeSpace(bytes)) {
                writeBytesAt(this.mEnd, this.mTempBuffer, bytes);
                this.mEnd = (this.mEnd + bytes) % this.mBuffer.length;
                long j2 = logEntry.time;
                this.mLatestTime = j2;
                TagDatabase.updateTagTime(logEntry.tag, j2);
                this.mChangeCount++;
            }
        }

        Iterator<LogEntry> getAllItems(final LogEntry logEntry) {
            return new Iterator<LogEntry>() { // from class: com.android.server.power.WakeLockLog.TheLog.2
                private final long mChangeValue;
                private int mCurrent;
                private long mCurrentTimeReference;

                {
                    this.mCurrent = TheLog.this.mStart;
                    this.mCurrentTimeReference = TheLog.this.mStartTime;
                    this.mChangeValue = TheLog.this.mChangeCount;
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    checkState();
                    return this.mCurrent != TheLog.this.mEnd;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public LogEntry next() {
                    checkState();
                    if (!hasNext()) {
                        throw new NoSuchElementException("No more entries left.");
                    }
                    LogEntry readEntryAt = TheLog.this.readEntryAt(this.mCurrent, this.mCurrentTimeReference, logEntry);
                    this.mCurrent = (this.mCurrent + TheLog.this.mTranslator.toBytes(readEntryAt, null, TheLog.this.mStartTime)) % TheLog.this.mBuffer.length;
                    this.mCurrentTimeReference = readEntryAt.time;
                    return readEntryAt;
                }

                public String toString() {
                    return "@" + this.mCurrent;
                }

                private void checkState() {
                    if (this.mChangeValue == TheLog.this.mChangeCount) {
                        return;
                    }
                    throw new ConcurrentModificationException("Buffer modified, old change: " + this.mChangeValue + ", new change: " + TheLog.this.mChangeCount);
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeTagIndex(int i) {
            if (isBufferEmpty()) {
                return;
            }
            int i2 = this.mStart;
            long j = this.mStartTime;
            LogEntry logEntry = new LogEntry();
            while (i2 != this.mEnd) {
                LogEntry readEntryAt = readEntryAt(i2, j, logEntry);
                if (readEntryAt == null) {
                    Slog.w(WakeLockLog.TAG, "Entry is unreadable - Unexpected @ " + i2);
                    return;
                }
                TagData tagData = readEntryAt.tag;
                if (tagData != null && tagData.index == i) {
                    readEntryAt.tag = null;
                    writeEntryAt(i2, readEntryAt, j);
                }
                j = readEntryAt.time;
                i2 = (i2 + this.mTranslator.toBytes(readEntryAt, null, 0L)) % this.mBuffer.length;
            }
        }

        private boolean makeSpace(int i) {
            int i2 = i + 1;
            if (this.mBuffer.length < i2) {
                return false;
            }
            while (getAvailableSpace() < i2) {
                removeOldestItem();
            }
            return true;
        }

        private int getAvailableSpace() {
            int i = this.mEnd;
            int i2 = this.mStart;
            return i > i2 ? this.mBuffer.length - (i - i2) : i < i2 ? i2 - i : this.mBuffer.length;
        }

        private void removeOldestItem() {
            if (isBufferEmpty()) {
                return;
            }
            LogEntry readEntryAt = readEntryAt(this.mStart, this.mStartTime, null);
            this.mStart = (this.mStart + this.mTranslator.toBytes(readEntryAt, null, this.mStartTime)) % this.mBuffer.length;
            this.mStartTime = readEntryAt.time;
            this.mChangeCount++;
        }

        private boolean isBufferEmpty() {
            return this.mStart == this.mEnd;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public LogEntry readEntryAt(int i, long j, LogEntry logEntry) {
            for (int i2 = 0; i2 < 9; i2++) {
                byte[] bArr = this.mBuffer;
                int length = (i + i2) % bArr.length;
                if (length == this.mEnd) {
                    break;
                }
                this.mReadWriteTempBuffer[i2] = bArr[length];
            }
            return this.mTranslator.fromBytes(this.mReadWriteTempBuffer, j, logEntry);
        }

        private void writeEntryAt(int i, LogEntry logEntry, long j) {
            int bytes = this.mTranslator.toBytes(logEntry, this.mReadWriteTempBuffer, j);
            if (bytes > 0) {
                writeBytesAt(i, this.mReadWriteTempBuffer, bytes);
            }
        }

        private void writeBytesAt(int i, byte[] bArr, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                byte[] bArr2 = this.mBuffer;
                bArr2[(i + i3) % bArr2.length] = bArr[i3];
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TagDatabase {
        private final TagData[] mArray;
        private Callback mCallback;
        private final int mInvalidIndex;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public interface Callback {
            void onIndexRemoved(int i);
        }

        TagDatabase(Injector injector) {
            int min = Math.min(injector.getTagDatabaseSize(), 128) - 1;
            this.mArray = new TagData[min];
            this.mInvalidIndex = min;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Tag Database: size(");
            sb.append(this.mArray.length);
            sb.append(")");
            int i = 0;
            int i2 = 0;
            for (TagData tagData : this.mArray) {
                i2 += 8;
                if (tagData != null) {
                    i++;
                    i2 += tagData.getByteSize();
                    String str = tagData.tag;
                    if (str != null) {
                        str.length();
                    }
                }
            }
            sb.append(", entries: ");
            sb.append(i);
            sb.append(", Bytes used: ");
            sb.append(i2);
            return sb.toString();
        }

        public void setCallback(Callback callback) {
            this.mCallback = callback;
        }

        public TagData getTag(int i) {
            if (i < 0) {
                return null;
            }
            TagData[] tagDataArr = this.mArray;
            if (i >= tagDataArr.length || i == this.mInvalidIndex) {
                return null;
            }
            return tagDataArr[i];
        }

        public TagData getTag(String str, int i) {
            return findOrCreateTag(str, i, false);
        }

        public int getTagIndex(TagData tagData) {
            return tagData == null ? this.mInvalidIndex : tagData.index;
        }

        public TagData findOrCreateTag(String str, int i, boolean z) {
            Callback callback;
            TagData tagData = new TagData(str, i);
            int i2 = -1;
            int i3 = -1;
            TagData tagData2 = null;
            int i4 = 0;
            while (true) {
                TagData[] tagDataArr = this.mArray;
                if (i4 >= tagDataArr.length) {
                    if (!z) {
                        return null;
                    }
                    if ((i2 == -1) && (callback = this.mCallback) != null) {
                        callback.onIndexRemoved(i3);
                    }
                    if (i2 == -1) {
                        i2 = i3;
                    }
                    setToIndex(tagData, i2);
                    return tagData;
                }
                TagData tagData3 = tagDataArr[i4];
                if (tagData.equals(tagData3)) {
                    return tagData3;
                }
                if (z) {
                    if (tagData3 != null) {
                        if (tagData2 == null || tagData3.lastUsedTime < tagData2.lastUsedTime) {
                            i3 = i4;
                            tagData2 = tagData3;
                        }
                    } else if (i2 == -1) {
                        i2 = i4;
                    }
                }
                i4++;
            }
        }

        public static void updateTagTime(TagData tagData, long j) {
            if (tagData != null) {
                tagData.lastUsedTime = j;
            }
        }

        private void setToIndex(TagData tagData, int i) {
            if (i >= 0) {
                TagData[] tagDataArr = this.mArray;
                if (i >= tagDataArr.length) {
                    return;
                }
                TagData tagData2 = tagDataArr[i];
                if (tagData2 != null) {
                    tagData2.index = this.mInvalidIndex;
                }
                tagDataArr[i] = tagData;
                tagData.index = i;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class TagData {
        public int index;
        public long lastUsedTime;
        public int ownerUid;
        public String tag;

        TagData(String str, int i) {
            this.tag = str;
            this.ownerUid = i;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TagData)) {
                return false;
            }
            TagData tagData = (TagData) obj;
            return TextUtils.equals(this.tag, tagData.tag) && this.ownerUid == tagData.ownerUid;
        }

        public String toString() {
            return "[" + this.ownerUid + " ; " + this.tag + "]";
        }

        int getByteSize() {
            String str = this.tag;
            return (str == null ? 0 : str.length() * 2) + 8 + 4 + 4 + 8;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Injector {
        public int getLogSize() {
            return WakeLockLog.LOG_SIZE;
        }

        public int getTagDatabaseSize() {
            return 128;
        }

        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }

        public SimpleDateFormat getDateFormat() {
            return WakeLockLog.DATE_FORMAT;
        }
    }
}
