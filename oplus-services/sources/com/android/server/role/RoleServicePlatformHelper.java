package com.android.server.role;

import android.annotation.SystemApi;
import java.util.Map;
import java.util.Set;

@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface RoleServicePlatformHelper {
    String computePackageStateHash(int i);

    Map<String, Set<String>> getLegacyRoleState(int i);
}
