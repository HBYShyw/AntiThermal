package com.android.server.pm;

import android.content.pm.ResolveInfo;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IComputerEngineWrapper {
    default List<ResolveInfo> filterIfNotSystemUser(List<ResolveInfo> list, int i) {
        return list;
    }
}
