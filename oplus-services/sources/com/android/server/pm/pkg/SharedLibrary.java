package com.android.server.pm.pkg;

import android.annotation.SystemApi;
import android.content.pm.VersionedPackage;
import android.processor.immutability.Immutable;
import java.util.List;

@Immutable
@SystemApi(client = SystemApi.Client.SYSTEM_SERVER)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface SharedLibrary {
    List<String> getAllCodePaths();

    @Immutable.Policy(exceptions = {Immutable.Policy.Exception.FINAL_CLASSES_WITH_FINAL_FIELDS})
    VersionedPackage getDeclaringPackage();

    List<SharedLibrary> getDependencies();

    @Immutable.Policy(exceptions = {Immutable.Policy.Exception.FINAL_CLASSES_WITH_FINAL_FIELDS})
    List<VersionedPackage> getDependentPackages();

    String getName();

    String getPackageName();

    String getPath();

    int getType();

    long getVersion();

    boolean isNative();
}
