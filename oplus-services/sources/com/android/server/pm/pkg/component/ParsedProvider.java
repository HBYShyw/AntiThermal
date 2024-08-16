package com.android.server.pm.pkg.component;

import android.content.pm.PathPermission;
import android.os.PatternMatcher;
import android.processor.immutability.Immutable;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ParsedProvider extends ParsedMainComponent {
    String getAuthority();

    int getInitOrder();

    @Immutable.Ignore
    List<PathPermission> getPathPermissions();

    String getReadPermission();

    @Immutable.Ignore
    List<PatternMatcher> getUriPermissionPatterns();

    String getWritePermission();

    boolean isForceUriPermissions();

    boolean isGrantUriPermissions();

    boolean isMultiProcess();

    boolean isSyncable();
}
