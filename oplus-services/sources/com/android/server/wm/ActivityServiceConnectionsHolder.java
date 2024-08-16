package com.android.server.wm;

import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;
import java.util.function.Consumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ActivityServiceConnectionsHolder<T> {
    private final ActivityRecord mActivity;

    @GuardedBy({"mActivity"})
    private ArraySet<T> mConnections;
    private volatile boolean mIsDisconnecting;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActivityServiceConnectionsHolder(ActivityRecord activityRecord) {
        this.mActivity = activityRecord;
    }

    public void addConnection(T t) {
        synchronized (this.mActivity) {
            if (this.mIsDisconnecting) {
                if (ActivityTaskManagerDebugConfig.DEBUG_CLEANUP) {
                    Slog.e("ActivityTaskManager", "Skip adding connection " + t + " to a disconnecting holder of " + this.mActivity);
                }
                return;
            }
            if (this.mConnections == null) {
                this.mConnections = new ArraySet<>();
            }
            this.mConnections.add(t);
        }
    }

    public void removeConnection(T t) {
        synchronized (this.mActivity) {
            if (this.mConnections == null) {
                return;
            }
            if (ActivityTaskManagerDebugConfig.DEBUG_CLEANUP && this.mIsDisconnecting) {
                Slog.v("ActivityTaskManager", "Remove pending disconnecting " + t + " of " + this.mActivity);
            }
            this.mConnections.remove(t);
        }
    }

    public boolean isActivityVisible() {
        return this.mActivity.mVisibleForServiceConnection;
    }

    public int getActivityPid() {
        WindowProcessController windowProcessController = this.mActivity.app;
        if (windowProcessController != null) {
            return windowProcessController.getPid();
        }
        return -1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void forEachConnection(Consumer<T> consumer) {
        synchronized (this.mActivity) {
            ArraySet<T> arraySet = this.mConnections;
            if (arraySet != null && !arraySet.isEmpty()) {
                ArraySet arraySet2 = new ArraySet((ArraySet) this.mConnections);
                for (int size = arraySet2.size() - 1; size >= 0; size--) {
                    consumer.accept(arraySet2.valueAt(size));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @GuardedBy({"mActivity"})
    public void disconnectActivityFromServices() {
        ArraySet<T> arraySet = this.mConnections;
        if (arraySet == null || arraySet.isEmpty() || this.mIsDisconnecting) {
            return;
        }
        this.mIsDisconnecting = true;
        this.mActivity.mAtmService.mH.post(new Runnable() { // from class: com.android.server.wm.ActivityServiceConnectionsHolder$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ActivityServiceConnectionsHolder.this.lambda$disconnectActivityFromServices$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disconnectActivityFromServices$0() {
        this.mActivity.mAtmService.mAmInternal.disconnectActivityFromServices(this);
        this.mIsDisconnecting = false;
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "activity=" + this.mActivity);
    }

    public String toString() {
        return String.valueOf(this.mConnections);
    }
}
