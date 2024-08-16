package com.android.server.pm.pkg;

import android.annotation.SystemApi;
import android.processor.immutability.Immutable;
import java.util.List;

@Immutable
@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface AndroidPackageSplit {
    String getClassLoaderName();

    List<AndroidPackageSplit> getDependencies();

    String getName();

    String getPath();

    int getRevisionCode();

    boolean isHasCode();
}
