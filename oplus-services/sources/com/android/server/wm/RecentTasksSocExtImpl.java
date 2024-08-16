package com.android.server.wm;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class RecentTasksSocExtImpl implements IRecentTasksSocExt {
    RecentTasks mRecentTasks;

    @Override // com.android.server.wm.IRecentTasksSocExt
    public void removeTaskUxPerf(Task task) {
    }

    public RecentTasksSocExtImpl(Object obj) {
        this.mRecentTasks = (RecentTasks) obj;
    }
}
