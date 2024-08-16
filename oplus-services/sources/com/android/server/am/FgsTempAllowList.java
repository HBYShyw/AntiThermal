package com.android.server.am;

import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FgsTempAllowList<E> {
    private static final int DEFAULT_MAX_SIZE = 100;
    private final Object mLock;
    private int mMaxSize;
    private final SparseArray<Pair<Long, E>> mTempAllowList;

    public FgsTempAllowList() {
        this.mTempAllowList = new SparseArray<>();
        this.mMaxSize = 100;
        this.mLock = new Object();
    }

    public FgsTempAllowList(int i) {
        this.mTempAllowList = new SparseArray<>();
        this.mMaxSize = 100;
        this.mLock = new Object();
        if (i <= 0) {
            Slog.e(IActivityManagerServiceExt.TAG, "Invalid FgsTempAllowList maxSize:" + i + ", force default maxSize:100");
            this.mMaxSize = 100;
            return;
        }
        this.mMaxSize = i;
    }

    public void add(int i, long j, E e) {
        synchronized (this.mLock) {
            if (j <= 0) {
                Slog.e(IActivityManagerServiceExt.TAG, "FgsTempAllowList bad duration:" + j + " key: " + i);
                return;
            }
            long elapsedRealtime = SystemClock.elapsedRealtime();
            int size = this.mTempAllowList.size();
            if (size > this.mMaxSize) {
                Slog.w(IActivityManagerServiceExt.TAG, "FgsTempAllowList length:" + size + " exceeds maxSize" + this.mMaxSize);
                for (int i2 = size + (-1); i2 >= 0; i2--) {
                    if (((Long) this.mTempAllowList.valueAt(i2).first).longValue() < elapsedRealtime) {
                        this.mTempAllowList.removeAt(i2);
                    }
                }
            }
            Pair<Long, E> pair = this.mTempAllowList.get(i);
            long j2 = elapsedRealtime + j;
            if (pair == null || ((Long) pair.first).longValue() < j2) {
                this.mTempAllowList.put(i, new Pair<>(Long.valueOf(j2), e));
            }
        }
    }

    public Pair<Long, E> get(int i) {
        synchronized (this.mLock) {
            int indexOfKey = this.mTempAllowList.indexOfKey(i);
            if (indexOfKey < 0) {
                return null;
            }
            if (((Long) this.mTempAllowList.valueAt(indexOfKey).first).longValue() < SystemClock.elapsedRealtime()) {
                this.mTempAllowList.removeAt(indexOfKey);
                return null;
            }
            return this.mTempAllowList.valueAt(indexOfKey);
        }
    }

    public boolean isAllowed(int i) {
        return get(i) != null;
    }

    public void removeUid(int i) {
        synchronized (this.mLock) {
            this.mTempAllowList.remove(i);
        }
    }

    public void removeAppId(int i) {
        synchronized (this.mLock) {
            for (int size = this.mTempAllowList.size() - 1; size >= 0; size--) {
                if (UserHandle.getAppId(this.mTempAllowList.keyAt(size)) == i) {
                    this.mTempAllowList.removeAt(size);
                }
            }
        }
    }

    public void forEach(BiConsumer<Integer, Pair<Long, E>> biConsumer) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mTempAllowList.size(); i++) {
                int keyAt = this.mTempAllowList.keyAt(i);
                Pair<Long, E> valueAt = this.mTempAllowList.valueAt(i);
                if (valueAt != null) {
                    biConsumer.accept(Integer.valueOf(keyAt), valueAt);
                }
            }
        }
    }
}
