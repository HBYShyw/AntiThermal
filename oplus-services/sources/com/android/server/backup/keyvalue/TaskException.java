package com.android.server.backup.keyvalue;

import com.android.internal.util.Preconditions;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
class TaskException extends BackupException {
    private static final int DEFAULT_STATUS = -1000;
    private final boolean mStateCompromised;
    private final int mStatus;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskException stateCompromised() {
        return new TaskException(true, -1000);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskException stateCompromised(Exception exc) {
        if (exc instanceof TaskException) {
            return new TaskException(exc, true, ((TaskException) exc).getStatus());
        }
        return new TaskException(exc, true, -1000);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskException forStatus(int i) {
        Preconditions.checkArgument(i != 0, "Exception based on TRANSPORT_OK");
        return new TaskException(false, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskException causedBy(Exception exc) {
        if (exc instanceof TaskException) {
            return (TaskException) exc;
        }
        return new TaskException(exc, false, -1000);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TaskException create() {
        return new TaskException(false, -1000);
    }

    private TaskException(Exception exc, boolean z, int i) {
        super(exc);
        this.mStateCompromised = z;
        this.mStatus = i;
    }

    private TaskException(boolean z, int i) {
        this.mStateCompromised = z;
        this.mStatus = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isStateCompromised() {
        return this.mStateCompromised;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getStatus() {
        return this.mStatus;
    }
}
