package com.android.server.wm;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public interface IRecentTasksWrapper {
    default ArrayList<Task> getHiddenTasks() {
        return new ArrayList<>(0);
    }
}
