package com.oplus.statistics.strategy;

import android.os.SystemClock;
import android.util.LruCache;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes2.dex */
public class RequestFireWall {
    private static final int CACHE_CAPACITY = 100;
    private static final String TAG = "FireWall";
    private final int mLimit;
    private final long mPeriod;
    private final LruCache<String, Queue<Long>> mRequestQueueMap;

    /* loaded from: classes2.dex */
    public static class Builder {
        private final int mLimit;
        private final long mPeriod;

        public Builder(int i10, long j10) {
            this.mLimit = Math.max(i10, 0);
            this.mPeriod = Math.max(j10, 0L);
        }

        public RequestFireWall build() {
            return new RequestFireWall(this);
        }
    }

    private long getRequestCountInWindowImpl(Queue<Long> queue, long j10) {
        Long peek = queue.peek();
        while (peek != null && peek.longValue() < j10 - this.mPeriod) {
            queue.poll();
            peek = queue.peek();
        }
        return queue.size();
    }

    private Queue<Long> getRequestQueue(String str) {
        Queue<Long> queue = this.mRequestQueueMap.get(str);
        if (queue != null) {
            return queue;
        }
        LinkedList linkedList = new LinkedList();
        this.mRequestQueueMap.put(str, linkedList);
        return linkedList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$handleRequest$0(String str, long j10) {
        return "Chatty!!! Allow " + this.mLimit + "/" + this.mPeriod + "ms, but " + str + " request " + j10 + " in the recent period.";
    }

    public boolean handleRequest(final String str) {
        Queue<Long> requestQueue = getRequestQueue(str);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        requestQueue.add(Long.valueOf(elapsedRealtime));
        final long requestCountInWindowImpl = getRequestCountInWindowImpl(requestQueue, elapsedRealtime);
        boolean z10 = requestCountInWindowImpl <= ((long) this.mLimit);
        if (!z10 && requestCountInWindowImpl % 10 == 1) {
            LogUtil.w(TAG, new Supplier() { // from class: com.oplus.statistics.strategy.d
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$handleRequest$0;
                    lambda$handleRequest$0 = RequestFireWall.this.lambda$handleRequest$0(str, requestCountInWindowImpl);
                    return lambda$handleRequest$0;
                }
            });
        }
        return z10;
    }

    private RequestFireWall(Builder builder) {
        this.mLimit = builder.mLimit;
        this.mPeriod = builder.mPeriod;
        this.mRequestQueueMap = new LruCache<>(100);
    }
}
