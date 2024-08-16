package com.android.server.om;

import android.content.om.OverlayableInfo;
import android.util.ArrayMap;
import com.android.server.pm.pkg.PackageState;
import java.io.IOException;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface PackageManagerHelper {
    boolean doesTargetDefineOverlayable(String str, int i) throws IOException;

    void enforcePermission(String str, String str2) throws SecurityException;

    String getConfigSignaturePackage();

    Map<String, Map<String, String>> getNamedActors();

    OverlayableInfo getOverlayableForTarget(String str, String str2, int i) throws IOException;

    PackageState getPackageStateForUser(String str, int i);

    String[] getPackagesForUid(int i);

    ArrayMap<String, PackageState> initializeForUser(int i);

    boolean isInstantApp(String str, int i);

    boolean signaturesMatching(String str, String str2, int i);
}
