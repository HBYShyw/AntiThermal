package com.android.server.app;

import android.content.ComponentName;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class GameTaskInfo {
    final ComponentName mComponentName;
    final boolean mIsGameTask;
    final int mTaskId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GameTaskInfo(int i, boolean z, ComponentName componentName) {
        this.mTaskId = i;
        this.mIsGameTask = z;
        this.mComponentName = componentName;
    }

    public String toString() {
        return "GameTaskInfo{mTaskId=" + this.mTaskId + ", mIsGameTask=" + this.mIsGameTask + ", mComponentName=" + this.mComponentName + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GameTaskInfo)) {
            return false;
        }
        GameTaskInfo gameTaskInfo = (GameTaskInfo) obj;
        return this.mTaskId == gameTaskInfo.mTaskId && this.mIsGameTask == gameTaskInfo.mIsGameTask && this.mComponentName.equals(gameTaskInfo.mComponentName);
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mTaskId), Boolean.valueOf(this.mIsGameTask), this.mComponentName);
    }
}
