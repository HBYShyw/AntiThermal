package com.android.server.backup.remote;

import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class RemoteResult {
    private final int mType;
    private final long mValue;
    public static final RemoteResult FAILED_TIMED_OUT = new RemoteResult(1, 0);
    public static final RemoteResult FAILED_CANCELLED = new RemoteResult(2, 0);
    public static final RemoteResult FAILED_THREAD_INTERRUPTED = new RemoteResult(3, 0);

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private @interface Type {
        public static final int FAILED_CANCELLED = 2;
        public static final int FAILED_THREAD_INTERRUPTED = 3;
        public static final int FAILED_TIMED_OUT = 1;
        public static final int SUCCESS = 0;
    }

    public static RemoteResult of(long j) {
        return new RemoteResult(0, j);
    }

    private RemoteResult(int i, long j) {
        this.mType = i;
        this.mValue = j;
    }

    public boolean isPresent() {
        return this.mType == 0;
    }

    public long get() {
        Preconditions.checkState(isPresent(), "Can't obtain value of failed result");
        return this.mValue;
    }

    public String toString() {
        return "RemoteResult{" + toStringDescription() + "}";
    }

    private String toStringDescription() {
        int i = this.mType;
        if (i == 0) {
            return Long.toString(this.mValue);
        }
        if (i == 1) {
            return "FAILED_TIMED_OUT";
        }
        if (i == 2) {
            return "FAILED_CANCELLED";
        }
        if (i == 3) {
            return "FAILED_THREAD_INTERRUPTED";
        }
        throw new AssertionError("Unknown type");
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RemoteResult)) {
            return false;
        }
        RemoteResult remoteResult = (RemoteResult) obj;
        return this.mType == remoteResult.mType && this.mValue == remoteResult.mValue;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mType), Long.valueOf(this.mValue));
    }
}
