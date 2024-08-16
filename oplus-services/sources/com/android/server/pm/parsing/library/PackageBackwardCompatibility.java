package com.android.server.pm.parsing.library;

import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.SystemConfig;
import com.android.server.pm.parsing.pkg.ParsedPackage;
import com.android.server.pm.pkg.parsing.ParsingPackage;
import java.util.ArrayList;
import java.util.List;

@VisibleForTesting
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PackageBackwardCompatibility extends PackageSharedLibraryUpdater {
    private static final PackageBackwardCompatibility INSTANCE;
    private static final String TAG = "PackageBackwardCompatibility";
    private final boolean mBootClassPathContainsATB;
    private final PackageSharedLibraryUpdater[] mPackageUpdaters;

    static {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AndroidNetIpSecIkeUpdater());
        arrayList.add(new ComGoogleAndroidMapsUpdater());
        arrayList.add(new OrgApacheHttpLegacyUpdater());
        arrayList.add(new AndroidHidlUpdater());
        arrayList.add(new AndroidTestRunnerSplitUpdater());
        boolean z = !addUpdaterForAndroidTestBase(arrayList);
        arrayList.add(new ApexSharedLibraryUpdater(SystemConfig.getInstance().getSharedLibraries()));
        INSTANCE = new PackageBackwardCompatibility(z, (PackageSharedLibraryUpdater[]) arrayList.toArray(new PackageSharedLibraryUpdater[0]));
    }

    private static boolean addUpdaterForAndroidTestBase(List<PackageSharedLibraryUpdater> list) {
        try {
            r1 = ParsingPackage.class.getClassLoader().loadClass("android.content.pm.AndroidTestBaseUpdater") != null;
            Log.i(TAG, "Loaded android.content.pm.AndroidTestBaseUpdater");
        } catch (ClassNotFoundException unused) {
            Log.i(TAG, "Could not find android.content.pm.AndroidTestBaseUpdater, ignoring");
        }
        if (r1) {
            list.add(new AndroidTestBaseUpdater());
        } else {
            list.add(new RemoveUnnecessaryAndroidTestBaseLibrary());
        }
        return r1;
    }

    @VisibleForTesting
    public static PackageSharedLibraryUpdater getInstance() {
        return INSTANCE;
    }

    @VisibleForTesting
    PackageSharedLibraryUpdater[] getPackageUpdaters() {
        return this.mPackageUpdaters;
    }

    private PackageBackwardCompatibility(boolean z, PackageSharedLibraryUpdater[] packageSharedLibraryUpdaterArr) {
        this.mBootClassPathContainsATB = z;
        this.mPackageUpdaters = packageSharedLibraryUpdaterArr;
    }

    @VisibleForTesting
    public static void modifySharedLibraries(ParsedPackage parsedPackage, boolean z, boolean z2) {
        INSTANCE.updatePackage(parsedPackage, z, z2);
    }

    @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
    public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
        for (PackageSharedLibraryUpdater packageSharedLibraryUpdater : this.mPackageUpdaters) {
            packageSharedLibraryUpdater.updatePackage(parsedPackage, z, z2);
        }
    }

    @VisibleForTesting
    public static boolean bootClassPathContainsATB() {
        return INSTANCE.mBootClassPathContainsATB;
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class AndroidTestRunnerSplitUpdater extends PackageSharedLibraryUpdater {
        @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
        public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
            prefixImplicitDependency(parsedPackage, "android.test.runner", "android.test.mock");
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class RemoveUnnecessaryOrgApacheHttpLegacyLibrary extends PackageSharedLibraryUpdater {
        @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
        public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
            PackageSharedLibraryUpdater.removeLibrary(parsedPackage, SharedLibraryNames.ORG_APACHE_HTTP_LEGACY);
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class RemoveUnnecessaryAndroidTestBaseLibrary extends PackageSharedLibraryUpdater {
        @Override // com.android.server.pm.parsing.library.PackageSharedLibraryUpdater
        public void updatePackage(ParsedPackage parsedPackage, boolean z, boolean z2) {
            PackageSharedLibraryUpdater.removeLibrary(parsedPackage, "android.test.base");
        }
    }
}
