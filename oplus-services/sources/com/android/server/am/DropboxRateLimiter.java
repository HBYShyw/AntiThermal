package com.android.server.am;

import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.modules.expresslog.Counter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DropboxRateLimiter {
    private static final int RATE_LIMIT_ALLOWED_ENTRIES = 6;
    private static final long RATE_LIMIT_BUFFER_DURATION = 600000;
    private static final long RATE_LIMIT_BUFFER_EXPIRY_FACTOR = 3;
    private static final int STRICT_RATE_LIMIT_ALLOWED_ENTRIES = 1;
    private static final long STRICT_RATE_LIMIT_BUFFER_DURATION = 1200000;
    private static final String TAG = "DropboxRateLimiter";
    private final Clock mClock;

    @GuardedBy({"mErrorClusterRecords"})
    private final ArrayMap<String, ErrorRecord> mErrorClusterRecords;
    private long mLastMapCleanUp;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Clock {
        long uptimeMillis();
    }

    public DropboxRateLimiter() {
        this(new DefaultClock());
    }

    public DropboxRateLimiter(Clock clock) {
        this.mErrorClusterRecords = new ArrayMap<>();
        this.mLastMapCleanUp = 0L;
        this.mClock = clock;
    }

    public RateLimitResult shouldRateLimit(String str, String str2) {
        if (str != null && "wtf".equals(str)) {
            long uptimeMillis = this.mClock.uptimeMillis();
            synchronized (this.mErrorClusterRecords) {
                maybeRemoveExpiredRecords(uptimeMillis);
                ErrorRecord errorRecord = this.mErrorClusterRecords.get(errorKey(str, str2));
                if (errorRecord == null) {
                    this.mErrorClusterRecords.put(errorKey(str, str2), new ErrorRecord(uptimeMillis, 1));
                    return new RateLimitResult(false, 0);
                }
                long startTime = uptimeMillis - errorRecord.getStartTime();
                if (startTime > errorRecord.getBufferDuration()) {
                    int recentlyDroppedCount = recentlyDroppedCount(errorRecord);
                    errorRecord.setStartTime(uptimeMillis);
                    errorRecord.setCount(1);
                    if (recentlyDroppedCount > 0 && startTime < errorRecord.getBufferDuration() * 2) {
                        errorRecord.incrementSuccessiveRateLimitCycles();
                    } else {
                        errorRecord.setSuccessiveRateLimitCycles(0);
                    }
                    return new RateLimitResult(false, recentlyDroppedCount);
                }
                errorRecord.incrementCount();
                if (errorRecord.getCount() > errorRecord.getAllowedEntries()) {
                    return new RateLimitResult(true, recentlyDroppedCount(errorRecord));
                }
            }
        }
        return new RateLimitResult(false, 0);
    }

    private int recentlyDroppedCount(ErrorRecord errorRecord) {
        if (errorRecord == null || errorRecord.getCount() < errorRecord.getAllowedEntries()) {
            return 0;
        }
        return errorRecord.getCount() - errorRecord.getAllowedEntries();
    }

    private void maybeRemoveExpiredRecords(long j) {
        if (j - this.mLastMapCleanUp <= 1800000) {
            return;
        }
        for (int size = this.mErrorClusterRecords.size() - 1; size >= 0; size--) {
            if (this.mErrorClusterRecords.valueAt(size).hasExpired(j)) {
                Counter.logIncrement("stability_errors.value_dropbox_buffer_expired_count", this.mErrorClusterRecords.valueAt(size).getCount());
                this.mErrorClusterRecords.removeAt(size);
            }
        }
        this.mLastMapCleanUp = j;
    }

    public void reset() {
        synchronized (this.mErrorClusterRecords) {
            this.mErrorClusterRecords.clear();
        }
        this.mLastMapCleanUp = 0L;
        Slog.i(TAG, "Rate limiter reset.");
    }

    String errorKey(String str, String str2) {
        return str + str2;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class RateLimitResult {
        final int mDroppedCountSinceRateLimitActivated;
        final boolean mShouldRateLimit;

        public RateLimitResult(boolean z, int i) {
            this.mShouldRateLimit = z;
            this.mDroppedCountSinceRateLimitActivated = i;
        }

        public boolean shouldRateLimit() {
            return this.mShouldRateLimit;
        }

        public int droppedCountSinceRateLimitActivated() {
            return this.mDroppedCountSinceRateLimitActivated;
        }

        public String createHeader() {
            return "Dropped-Count: " + this.mDroppedCountSinceRateLimitActivated + "\n";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ErrorRecord {
        int mCount;
        long mStartTime;
        int mSuccessiveRateLimitCycles = 0;

        ErrorRecord(long j, int i) {
            this.mStartTime = j;
            this.mCount = i;
        }

        public void setStartTime(long j) {
            this.mStartTime = j;
        }

        public void setCount(int i) {
            this.mCount = i;
        }

        public void incrementCount() {
            this.mCount++;
        }

        public void setSuccessiveRateLimitCycles(int i) {
            this.mSuccessiveRateLimitCycles = i;
        }

        public void incrementSuccessiveRateLimitCycles() {
            this.mSuccessiveRateLimitCycles++;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public int getCount() {
            return this.mCount;
        }

        public int getSuccessiveRateLimitCycles() {
            return this.mSuccessiveRateLimitCycles;
        }

        public boolean isRepeated() {
            return this.mSuccessiveRateLimitCycles >= 2;
        }

        public int getAllowedEntries() {
            return isRepeated() ? 1 : 6;
        }

        public long getBufferDuration() {
            if (isRepeated()) {
                return DropboxRateLimiter.STRICT_RATE_LIMIT_BUFFER_DURATION;
            }
            return 600000L;
        }

        public boolean hasExpired(long j) {
            return j - this.mStartTime > getBufferDuration() * DropboxRateLimiter.RATE_LIMIT_BUFFER_EXPIRY_FACTOR;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private static class DefaultClock implements Clock {
        private DefaultClock() {
        }

        @Override // com.android.server.am.DropboxRateLimiter.Clock
        public long uptimeMillis() {
            return SystemClock.uptimeMillis();
        }
    }
}
