package com.android.server.wm;

import android.app.BackgroundStartPrivileges;
import android.app.compat.CompatChanges;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.IntPredicate;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class BackgroundLaunchProcessController {
    private static final long DEFAULT_RESCIND_BAL_FG_PRIVILEGES_BOUND_SERVICE = 261072174;
    private static final String TAG = "ActivityTaskManager";
    private final BackgroundActivityStartCallback mBackgroundActivityStartCallback;
    public IBackgroundLaunchProcessControllerExt mBackgroundLaunchProcessControllerExt = (IBackgroundLaunchProcessControllerExt) ExtLoader.type(IBackgroundLaunchProcessControllerExt.class).base(this).create();

    @GuardedBy({"this"})
    private ArrayMap<Binder, BackgroundStartPrivileges> mBackgroundStartPrivileges;

    @GuardedBy({"this"})
    private IntArray mBalOptInBoundClientUids;
    private final IntPredicate mUidHasActiveVisibleWindowPredicate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BackgroundLaunchProcessController(IntPredicate intPredicate, BackgroundActivityStartCallback backgroundActivityStartCallback) {
        this.mUidHasActiveVisibleWindowPredicate = intPredicate;
        this.mBackgroundActivityStartCallback = backgroundActivityStartCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int areBackgroundActivityStartsAllowed(int i, int i2, String str, int i3, boolean z, boolean z2, boolean z3, long j, long j2, long j3) {
        if (z3) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_instrumentingWithBackgroundActivityStartPrivileges", z);
            return BackgroundActivityStartController.logStartAllowedAndReturnCode(6, true, i2, i2, (Intent) null, i, "Activity start allowed: process instrumenting with background activity starts privileges");
        }
        if (isBackgroundStartAllowedByToken(i2, str, z)) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerisBackgroundStartAllowedByToken", z);
            return BackgroundActivityStartController.logStartAllowedAndReturnCode(6, true, i2, i2, (Intent) null, i, "Activity start allowed: process allowed by token");
        }
        if (isBoundByForegroundUid()) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerIsBoundByForegroundUid", z);
            return BackgroundActivityStartController.logStartAllowedAndReturnCode(4, false, i2, i2, (Intent) null, i, "Activity start allowed: process bound by foreground uid");
        }
        if (z2 && (i3 == 2 || i3 == 1)) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerHasActivityInVisibleTask", z);
            return BackgroundActivityStartController.logStartAllowedAndReturnCode(9, false, i2, i2, (Intent) null, i, "Activity start allowed: process has activity in foreground task");
        }
        if (i3 != 2) {
            return 0;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - j2 >= 10000 && uptimeMillis - j3 >= 10000) {
            return 0;
        }
        if (j2 > j || j3 > j) {
            this.mBackgroundLaunchProcessControllerExt.monitorActivityStartInfo("S_CallerActivityStartOrFinishRecently", z);
            return BackgroundActivityStartController.logStartAllowedAndReturnCode(8, true, i2, i2, (Intent) null, i, "Activity start allowed: within 10000ms grace period");
        }
        if (!ActivityTaskManagerDebugConfig.DEBUG_ACTIVITY_STARTS) {
            return 0;
        }
        Slog.d(TAG, "[Process(" + i + ")] Activity start within 10000ms grace period but also within stop app switch window");
        return 0;
    }

    private boolean isBackgroundStartAllowedByToken(int i, String str, boolean z) {
        synchronized (this) {
            ArrayMap<Binder, BackgroundStartPrivileges> arrayMap = this.mBackgroundStartPrivileges;
            if (arrayMap != null && !arrayMap.isEmpty()) {
                if (z) {
                    int size = this.mBackgroundStartPrivileges.size();
                    while (true) {
                        int i2 = size - 1;
                        if (size <= 0) {
                            return false;
                        }
                        if (this.mBackgroundStartPrivileges.valueAt(i2).allowsBackgroundFgsStarts()) {
                            return true;
                        }
                        size = i2;
                    }
                } else if (this.mBackgroundActivityStartCallback == null) {
                    int size2 = this.mBackgroundStartPrivileges.size();
                    while (true) {
                        int i3 = size2 - 1;
                        if (size2 <= 0) {
                            return false;
                        }
                        if (this.mBackgroundStartPrivileges.valueAt(i3).allowsBackgroundActivityStarts()) {
                            return true;
                        }
                        size2 = i3;
                    }
                } else {
                    List<IBinder> originatingTokensThatAllowBal = getOriginatingTokensThatAllowBal();
                    if (originatingTokensThatAllowBal.isEmpty()) {
                        return false;
                    }
                    return this.mBackgroundActivityStartCallback.isActivityStartAllowed(originatingTokensThatAllowBal, i, str);
                }
            }
            return false;
        }
    }

    private List<IBinder> getOriginatingTokensThatAllowBal() {
        ArrayList arrayList = new ArrayList();
        int size = this.mBackgroundStartPrivileges.size();
        while (true) {
            int i = size - 1;
            if (size <= 0) {
                return arrayList;
            }
            BackgroundStartPrivileges valueAt = this.mBackgroundStartPrivileges.valueAt(i);
            if (valueAt.allowsBackgroundActivityStarts()) {
                arrayList.add(valueAt.getOriginatingToken());
            }
            size = i;
        }
    }

    private boolean isBoundByForegroundUid() {
        synchronized (this) {
            IntArray intArray = this.mBalOptInBoundClientUids;
            if (intArray != null) {
                for (int size = intArray.size() - 1; size >= 0; size--) {
                    if (this.mUidHasActiveVisibleWindowPredicate.test(this.mBalOptInBoundClientUids.get(size))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearBalOptInBoundClientUids() {
        synchronized (this) {
            IntArray intArray = this.mBalOptInBoundClientUids;
            if (intArray == null) {
                this.mBalOptInBoundClientUids = new IntArray();
            } else {
                intArray.clear();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addBoundClientUid(int i, String str, long j) {
        if (CompatChanges.isChangeEnabled(DEFAULT_RESCIND_BAL_FG_PRIVILEGES_BOUND_SERVICE, str, UserHandle.getUserHandleForUid(i)) && (j & 512) == 0) {
            return;
        }
        if (this.mBalOptInBoundClientUids == null) {
            this.mBalOptInBoundClientUids = new IntArray();
        }
        if (this.mBalOptInBoundClientUids.indexOf(i) == -1) {
            this.mBalOptInBoundClientUids.add(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOrUpdateAllowBackgroundStartPrivileges(Binder binder, BackgroundStartPrivileges backgroundStartPrivileges) {
        Objects.requireNonNull(binder, "entity");
        Objects.requireNonNull(backgroundStartPrivileges, "backgroundStartPrivileges");
        Preconditions.checkArgument(backgroundStartPrivileges.allowsAny(), "backgroundStartPrivileges does not allow anything");
        synchronized (this) {
            if (this.mBackgroundStartPrivileges == null) {
                this.mBackgroundStartPrivileges = new ArrayMap<>();
            }
            this.mBackgroundStartPrivileges.put(binder, backgroundStartPrivileges);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAllowBackgroundStartPrivileges(Binder binder) {
        Objects.requireNonNull(binder, "entity");
        synchronized (this) {
            ArrayMap<Binder, BackgroundStartPrivileges> arrayMap = this.mBackgroundStartPrivileges;
            if (arrayMap != null) {
                arrayMap.remove(binder);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canCloseSystemDialogsByToken(int i) {
        if (this.mBackgroundActivityStartCallback == null) {
            return false;
        }
        synchronized (this) {
            ArrayMap<Binder, BackgroundStartPrivileges> arrayMap = this.mBackgroundStartPrivileges;
            if (arrayMap != null && !arrayMap.isEmpty()) {
                return this.mBackgroundActivityStartCallback.canCloseSystemDialogs(getOriginatingTokensThatAllowBal(), i);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        synchronized (this) {
            ArrayMap<Binder, BackgroundStartPrivileges> arrayMap = this.mBackgroundStartPrivileges;
            if (arrayMap != null && !arrayMap.isEmpty()) {
                printWriter.print(str);
                printWriter.println("Background activity start tokens (token: originating token):");
                for (int size = this.mBackgroundStartPrivileges.size() - 1; size >= 0; size--) {
                    printWriter.print(str);
                    printWriter.print("  - ");
                    printWriter.print(this.mBackgroundStartPrivileges.keyAt(size));
                    printWriter.print(": ");
                    printWriter.println(this.mBackgroundStartPrivileges.valueAt(size));
                }
            }
            IntArray intArray = this.mBalOptInBoundClientUids;
            if (intArray != null && intArray.size() > 0) {
                printWriter.print(str);
                printWriter.print("BoundClientUids:");
                printWriter.println(Arrays.toString(this.mBalOptInBoundClientUids.toArray()));
            }
        }
    }
}
