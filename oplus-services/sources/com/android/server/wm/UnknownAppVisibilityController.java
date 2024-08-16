package com.android.server.wm;

import android.util.ArrayMap;
import android.util.Slog;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class UnknownAppVisibilityController {
    private static final String TAG = "WindowManager";
    private static final int UNKNOWN_STATE_WAITING_RELAYOUT = 2;
    private static final int UNKNOWN_STATE_WAITING_RESUME = 1;
    private static final int UNKNOWN_STATE_WAITING_VISIBILITY_UPDATE = 3;
    private final DisplayContent mDisplayContent;
    private final WindowManagerService mService;
    private final ArrayMap<ActivityRecord, Integer> mUnknownApps = new ArrayMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnknownAppVisibilityController(WindowManagerService windowManagerService, DisplayContent displayContent) {
        this.mService = windowManagerService;
        this.mDisplayContent = displayContent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean allResolved() {
        return this.mUnknownApps.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isVisibilityUnknown(ActivityRecord activityRecord) {
        return this.mUnknownApps.containsKey(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.mUnknownApps.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getDebugMessage() {
        StringBuilder sb = new StringBuilder();
        for (int size = this.mUnknownApps.size() - 1; size >= 0; size--) {
            sb.append("app=");
            sb.append(this.mUnknownApps.keyAt(size));
            sb.append(" state=");
            sb.append(this.mUnknownApps.valueAt(size));
            if (size != 0) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void appRemovedOrHidden(ActivityRecord activityRecord) {
        if (WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
            Slog.d(TAG, "App removed or hidden activity=" + activityRecord);
        }
        this.mUnknownApps.remove(activityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyLaunched(ActivityRecord activityRecord) {
        if (WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
            Slog.d(TAG, "App launched activity=" + activityRecord);
        }
        if (!activityRecord.mLaunchTaskBehind) {
            this.mUnknownApps.put(activityRecord, 1);
        } else {
            this.mUnknownApps.put(activityRecord, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyAppResumedFinished(ActivityRecord activityRecord) {
        if (this.mUnknownApps.containsKey(activityRecord) && this.mUnknownApps.get(activityRecord).intValue() == 1) {
            if (WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
                Slog.d(TAG, "App resume finished activity=" + activityRecord);
            }
            this.mUnknownApps.put(activityRecord, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyRelayouted(ActivityRecord activityRecord) {
        if (this.mUnknownApps.containsKey(activityRecord)) {
            if (WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
                Slog.d(TAG, "App relayouted appWindow=" + activityRecord);
            }
            if (this.mUnknownApps.get(activityRecord).intValue() == 2 || activityRecord.mStartingWindow != null) {
                this.mUnknownApps.put(activityRecord, 3);
                this.mDisplayContent.notifyKeyguardFlagsChanged();
                notifyVisibilitiesUpdated();
            }
        }
    }

    private void notifyVisibilitiesUpdated() {
        if (WindowManagerDebugConfig.DEBUG_UNKNOWN_APP_VISIBILITY) {
            Slog.d(TAG, "Visibility updated DONE");
        }
        boolean z = false;
        for (int size = this.mUnknownApps.size() - 1; size >= 0; size--) {
            if (this.mUnknownApps.valueAt(size).intValue() == 3) {
                this.mUnknownApps.removeAt(size);
                z = true;
            }
        }
        if (z) {
            this.mService.mWindowPlacerLocked.performSurfacePlacement();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        if (this.mUnknownApps.isEmpty()) {
            return;
        }
        printWriter.println(str + "Unknown visibilities:");
        for (int size = this.mUnknownApps.size() + (-1); size >= 0; size += -1) {
            printWriter.println(str + "  app=" + this.mUnknownApps.keyAt(size) + " state=" + this.mUnknownApps.valueAt(size));
        }
    }
}
