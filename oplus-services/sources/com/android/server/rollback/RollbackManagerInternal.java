package com.android.server.rollback;

import android.os.UserHandle;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface RollbackManagerInternal {
    int notifyStagedSession(int i);

    void snapshotAndRestoreUserData(String str, List<UserHandle> list, int i, long j, String str2, int i2);
}
