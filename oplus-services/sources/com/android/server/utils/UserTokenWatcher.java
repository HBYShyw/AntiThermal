package com.android.server.utils;

import android.os.Handler;
import android.os.IBinder;
import android.os.TokenWatcher;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.IndentingPrintWriter;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UserTokenWatcher {
    private final Callback mCallback;
    private final Handler mHandler;
    private final String mTag;

    @GuardedBy({"mWatchers"})
    private final SparseArray<TokenWatcher> mWatchers = new SparseArray<>(1);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callback {
        void acquired(int i);

        void released(int i);
    }

    public UserTokenWatcher(Callback callback, Handler handler, String str) {
        this.mCallback = callback;
        this.mHandler = handler;
        this.mTag = str;
    }

    public void acquire(IBinder iBinder, String str, int i) {
        synchronized (this.mWatchers) {
            TokenWatcher tokenWatcher = this.mWatchers.get(i);
            if (tokenWatcher == null) {
                tokenWatcher = new InnerTokenWatcher(i, this.mHandler, this.mTag);
                this.mWatchers.put(i, tokenWatcher);
            }
            tokenWatcher.acquire(iBinder, str);
        }
    }

    public void release(IBinder iBinder, int i) {
        synchronized (this.mWatchers) {
            TokenWatcher tokenWatcher = this.mWatchers.get(i);
            if (tokenWatcher != null) {
                tokenWatcher.release(iBinder);
            }
        }
    }

    public boolean isAcquired(int i) {
        boolean z;
        synchronized (this.mWatchers) {
            TokenWatcher tokenWatcher = this.mWatchers.get(i);
            z = tokenWatcher != null && tokenWatcher.isAcquired();
        }
        return z;
    }

    public void dump(PrintWriter printWriter) {
        synchronized (this.mWatchers) {
            for (int i = 0; i < this.mWatchers.size(); i++) {
                int keyAt = this.mWatchers.keyAt(i);
                TokenWatcher valueAt = this.mWatchers.valueAt(i);
                if (valueAt.isAcquired()) {
                    printWriter.print("User ");
                    printWriter.print(keyAt);
                    printWriter.println(":");
                    valueAt.dump(new IndentingPrintWriter(printWriter, " "));
                }
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class InnerTokenWatcher extends TokenWatcher {
        private final int mUserId;

        private InnerTokenWatcher(int i, Handler handler, String str) {
            super(handler, str);
            this.mUserId = i;
        }

        @Override // android.os.TokenWatcher
        public void acquired() {
            UserTokenWatcher.this.mCallback.acquired(this.mUserId);
        }

        @Override // android.os.TokenWatcher
        public void released() {
            UserTokenWatcher.this.mCallback.released(this.mUserId);
            synchronized (UserTokenWatcher.this.mWatchers) {
                TokenWatcher tokenWatcher = (TokenWatcher) UserTokenWatcher.this.mWatchers.get(this.mUserId);
                if (tokenWatcher != null && !tokenWatcher.isAcquired()) {
                    UserTokenWatcher.this.mWatchers.remove(this.mUserId);
                }
            }
        }
    }
}
