package com.android.server.location.injector;

import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.server.location.LocationManagerService;
import com.android.server.location.eventlog.LocationEventLog;
import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class UserInfoHelper {
    private final CopyOnWriteArrayList<UserListener> mListeners = new CopyOnWriteArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface UserListener {
        public static final int CURRENT_USER_CHANGED = 1;
        public static final int USER_STARTED = 2;
        public static final int USER_STOPPED = 3;
        public static final int USER_VISIBILITY_CHANGED = 4;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public @interface UserChange {
        }

        void onUserChanged(int i, int i2);
    }

    public abstract void dump(FileDescriptor fileDescriptor, IndentingPrintWriter indentingPrintWriter, String[] strArr);

    public abstract int getCurrentUserId();

    protected abstract int[] getProfileIds(int i);

    public abstract int[] getRunningUserIds();

    public abstract boolean isCurrentUserId(int i);

    public abstract boolean isVisibleUserId(int i);

    public final void addListener(UserListener userListener) {
        this.mListeners.add(userListener);
    }

    public final void removeListener(UserListener userListener) {
        this.mListeners.remove(userListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void dispatchOnUserStarted(int i) {
        if (LocationManagerService.D) {
            Log.d(LocationManagerService.TAG, "u" + i + " started");
        }
        Iterator<UserListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onUserChanged(i, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void dispatchOnUserStopped(int i) {
        if (LocationManagerService.D) {
            Log.d(LocationManagerService.TAG, "u" + i + " stopped");
        }
        Iterator<UserListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onUserChanged(i, 3);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void dispatchOnCurrentUserChanged(int i, int i2) {
        int[] profileIds = getProfileIds(i);
        int[] profileIds2 = getProfileIds(i2);
        if (LocationManagerService.D) {
            Log.d(LocationManagerService.TAG, "current user changed from u" + Arrays.toString(profileIds) + " to u" + Arrays.toString(profileIds2));
        }
        LocationEventLog.EVENT_LOG.logUserSwitched(i, i2);
        Iterator<UserListener> it = this.mListeners.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            UserListener next = it.next();
            for (int i3 : profileIds) {
                next.onUserChanged(i3, 1);
            }
        }
        Iterator<UserListener> it2 = this.mListeners.iterator();
        while (it2.hasNext()) {
            UserListener next2 = it2.next();
            for (int i4 : profileIds2) {
                next2.onUserChanged(i4, 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void dispatchOnVisibleUserChanged(int i, boolean z) {
        if (LocationManagerService.D) {
            StringBuilder sb = new StringBuilder();
            sb.append("visibility of u");
            sb.append(i);
            sb.append(" changed to ");
            sb.append(z ? "visible" : "invisible");
            Log.d(LocationManagerService.TAG, sb.toString());
        }
        LocationEventLog.EVENT_LOG.logUserVisibilityChanged(i, z);
        Iterator<UserListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onUserChanged(i, 4);
        }
    }
}
