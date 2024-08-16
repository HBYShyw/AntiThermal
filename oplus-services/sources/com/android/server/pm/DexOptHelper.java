package com.android.server.pm;

import android.app.AppGlobals;
import android.app.role.RoleManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.dex.ArtManager;
import android.os.Binder;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.LocalManagerRegistry;
import com.android.server.LocalServices;
import com.android.server.PinnerService;
import com.android.server.SystemServerInitThreadPool$;
import com.android.server.art.ArtManagerLocal;
import com.android.server.art.DexUseManagerLocal;
import com.android.server.art.model.DexoptResult;
import com.android.server.pm.ApexManager;
import com.android.server.pm.CompilerStats;
import com.android.server.pm.Installer;
import com.android.server.pm.PackageDexOptimizer;
import com.android.server.pm.PackageManagerLocal;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.dex.DexoptOptions;
import com.android.server.pm.parsing.pkg.AndroidPackageInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageStateInternal;
import dalvik.system.DexFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DexOptHelper {
    private static final long SEVEN_DAYS_IN_MILLISECONDS = 604800000;
    private static boolean sArtManagerLocalIsInitialized = false;
    private volatile long mBootDexoptStartTime;
    private final PackageManagerService mPm;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPackagesForDexopt$6(PackageStateInternal packageStateInternal) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPackagesForDexopt$7(PackageStateInternal packageStateInternal) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DexOptHelper(PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
    }

    private static String getPrebuildProfilePath(AndroidPackage androidPackage) {
        return androidPackage.getBaseApkPath() + ".prof";
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x014c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0126 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int[] performDexOptUpgrade(List<PackageStateInternal> list, int i, boolean z) throws Installer.LegacyDexoptDisabledException {
        Iterator<PackageStateInternal> it;
        int i2;
        boolean z2;
        Installer.checkLegacyDexoptDisabled();
        int size = list.size();
        int onStartInPerformDexOptUpgrade = this.mPm.mPackageManagerServiceExt.onStartInPerformDexOptUpgrade(0);
        Iterator<PackageStateInternal> it2 = list.iterator();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (it2.hasNext()) {
            PackageStateInternal next = it2.next();
            AndroidPackage androidPackage = next.getAndroidPackage();
            int i6 = onStartInPerformDexOptUpgrade + 1;
            if ((this.mPm.isFirstBoot() || this.mPm.isDeviceUpgrading()) && next.isSystem()) {
                File file = new File(getPrebuildProfilePath(androidPackage));
                if (file.exists()) {
                    try {
                        it = it2;
                        try {
                            i2 = i5;
                        } catch (Installer.InstallerException | RuntimeException e) {
                            e = e;
                            i2 = i5;
                            Log.e("PackageManager", "Failed to copy profile " + file.getAbsolutePath() + " ", e);
                            z2 = false;
                            if (this.mPm.mPackageDexOptimizer.canOptimizePackage(androidPackage)) {
                            }
                        }
                        try {
                            if (!this.mPm.mInstaller.copySystemProfile(file.getAbsolutePath(), androidPackage.getUid(), androidPackage.getPackageName(), ArtManager.getProfileName((String) null))) {
                                Log.e("PackageManager", "Installer failed to copy system profile!");
                            }
                        } catch (Installer.InstallerException | RuntimeException e2) {
                            e = e2;
                            Log.e("PackageManager", "Failed to copy profile " + file.getAbsolutePath() + " ", e);
                            z2 = false;
                            if (this.mPm.mPackageDexOptimizer.canOptimizePackage(androidPackage)) {
                            }
                        }
                    } catch (Installer.InstallerException | RuntimeException e3) {
                        e = e3;
                        it = it2;
                    }
                } else {
                    it = it2;
                    i2 = i5;
                    PackageSetting disabledSystemPkgLPr = this.mPm.mSettings.getDisabledSystemPkgLPr(androidPackage.getPackageName());
                    if (disabledSystemPkgLPr != null && disabledSystemPkgLPr.getPkg().isStub()) {
                        File file2 = new File(getPrebuildProfilePath(disabledSystemPkgLPr.getPkg()).replace(PackageManagerService.STUB_SUFFIX, ""));
                        if (file2.exists()) {
                            try {
                            } catch (Installer.InstallerException | RuntimeException e4) {
                                Log.e("PackageManager", "Failed to copy profile " + file2.getAbsolutePath() + " ", e4);
                            }
                            if (!this.mPm.mInstaller.copySystemProfile(file2.getAbsolutePath(), androidPackage.getUid(), androidPackage.getPackageName(), ArtManager.getProfileName((String) null))) {
                                Log.e("PackageManager", "Failed to copy system profile for stub package!");
                            } else {
                                z2 = true;
                                if (this.mPm.mPackageDexOptimizer.canOptimizePackage(androidPackage)) {
                                    if (PackageManagerService.DEBUG_DEXOPT) {
                                        Log.i("PackageManager", "Skipping update of non-optimizable app " + androidPackage.getPackageName());
                                    }
                                    i4++;
                                    onStartInPerformDexOptUpgrade = i6;
                                    it2 = it;
                                    i5 = i2;
                                } else {
                                    if (PackageManagerService.DEBUG_DEXOPT) {
                                        Log.i("PackageManager", "Updating app " + i6 + " of " + size + ": " + androidPackage.getPackageName());
                                    }
                                    PackageManagerService packageManagerService = this.mPm;
                                    packageManagerService.mPackageManagerServiceExt.beforeShowBootMessageInPerformDexOptUpgrade(packageManagerService.isDeviceUpgrading(), i6, list.size());
                                    int i7 = z2 ? 9 : i;
                                    if (SystemProperties.getBoolean("pm.precompile_layouts", false)) {
                                        this.mPm.mArtManagerService.compileLayouts(next, androidPackage);
                                    }
                                    int i8 = z ? 4 : 0;
                                    String compilerFilterForReason = PackageManagerServiceCompilerMapping.getCompilerFilterForReason(i7);
                                    if (DexFile.isProfileGuidedCompilerFilter(compilerFilterForReason)) {
                                        i8 |= 1;
                                    }
                                    if (i == 0) {
                                        i8 |= 1024;
                                    }
                                    int performDexOptTraced = performDexOptTraced(new DexoptOptions(androidPackage.getPackageName(), i7, compilerFilterForReason, null, i8));
                                    if (performDexOptTraced != -1) {
                                        if (performDexOptTraced == 0) {
                                            i4++;
                                        } else if (performDexOptTraced == 1) {
                                            i3++;
                                        } else if (performDexOptTraced != 2) {
                                            Log.e("PackageManager", "Unexpected dexopt return code " + performDexOptTraced);
                                        }
                                        i5 = i2;
                                    } else {
                                        i5 = i2 + 1;
                                    }
                                    onStartInPerformDexOptUpgrade = i6;
                                    it2 = it;
                                }
                            }
                        }
                    }
                }
            } else {
                it = it2;
                i2 = i5;
            }
            z2 = false;
            if (this.mPm.mPackageDexOptimizer.canOptimizePackage(androidPackage)) {
            }
        }
        return new int[]{i3, i4, i5};
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:26:0x0096
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    private void checkAndDexOptSystemUi(int r11) throws com.android.server.pm.Installer.LegacyDexoptDisabledException {
        /*
            r10 = this;
            com.android.server.pm.PackageManagerService r0 = r10.mPm
            com.android.server.pm.Computer r0 = r0.snapshotComputer()
            com.android.server.pm.PackageManagerService r1 = r10.mPm
            android.content.Context r1 = r1.mContext
            r2 = 17039418(0x104003a, float:2.4244734E-38)
            java.lang.String r1 = r1.getString(r2)
            com.android.server.pm.pkg.AndroidPackage r0 = r0.getPackage(r1)
            if (r0 != 0) goto L33
            java.lang.String r10 = "PackageManager"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r0 = "System UI package "
            r11.append(r0)
            r11.append(r1)
            java.lang.String r0 = " is not found for dexopting"
            r11.append(r0)
            java.lang.String r11 = r11.toString()
            android.util.Log.w(r10, r11)
            return
        L33:
            java.lang.String r2 = com.android.server.pm.PackageManagerServiceCompilerMapping.getCompilerFilterForReason(r11)
            java.lang.String r3 = "dalvik.vm.systemuicompilerfilter"
            java.lang.String r2 = android.os.SystemProperties.get(r3, r2)
            boolean r3 = dalvik.system.DexFile.isProfileGuidedCompilerFilter(r2)
            if (r3 == 0) goto Lb5
            java.lang.String r3 = "verify"
            java.io.File r4 = new java.io.File
            java.lang.String r5 = getPrebuildProfilePath(r0)
            r4.<init>(r5)
            boolean r5 = r4.exists()
            if (r5 == 0) goto Lb4
            com.android.server.pm.PackageManagerService r5 = r10.mPm     // Catch: java.lang.Throwable -> L99
            java.lang.Object r5 = r5.mInstallLock     // Catch: java.lang.Throwable -> L99
            monitor-enter(r5)     // Catch: java.lang.Throwable -> L99
            com.android.server.pm.PackageManagerService r6 = r10.mPm     // Catch: java.lang.Throwable -> L96
            com.android.server.pm.Installer r6 = r6.mInstaller     // Catch: java.lang.Throwable -> L96
            java.lang.String r7 = r4.getAbsolutePath()     // Catch: java.lang.Throwable -> L96
            int r8 = r0.getUid()     // Catch: java.lang.Throwable -> L96
            java.lang.String r0 = r0.getPackageName()     // Catch: java.lang.Throwable -> L96
            r9 = 0
            java.lang.String r9 = android.content.pm.dex.ArtManager.getProfileName(r9)     // Catch: java.lang.Throwable -> L96
            boolean r0 = r6.copySystemProfile(r7, r8, r0, r9)     // Catch: java.lang.Throwable -> L96
            if (r0 == 0) goto L76
            goto L91
        L76:
            java.lang.String r0 = "PackageManager"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L96
            r2.<init>()     // Catch: java.lang.Throwable -> L96
            java.lang.String r6 = "Failed to copy profile "
            r2.append(r6)     // Catch: java.lang.Throwable -> L96
            java.lang.String r6 = r4.getAbsolutePath()     // Catch: java.lang.Throwable -> L96
            r2.append(r6)     // Catch: java.lang.Throwable -> L96
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L96
            android.util.Log.e(r0, r2)     // Catch: java.lang.Throwable -> L96
            r2 = r3
        L91:
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L93
            goto Lb5
        L93:
            r0 = move-exception
            r3 = r2
            goto L97
        L96:
            r0 = move-exception
        L97:
            monitor-exit(r5)     // Catch: java.lang.Throwable -> L96
            throw r0     // Catch: java.lang.Throwable -> L99 java.lang.Throwable -> L99
        L99:
            r0 = move-exception
            java.lang.String r2 = "PackageManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Failed to copy profile "
            r5.append(r6)
            java.lang.String r4 = r4.getAbsolutePath()
            r5.append(r4)
            java.lang.String r4 = r5.toString()
            android.util.Log.e(r2, r4, r0)
        Lb4:
            r2 = r3
        Lb5:
            r10.performDexoptPackage(r1, r11, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.DexOptHelper.checkAndDexOptSystemUi(int):void");
    }

    private void dexoptLauncher(int i) throws Installer.LegacyDexoptDisabledException {
        Computer snapshotComputer = this.mPm.snapshotComputer();
        for (String str : ((RoleManager) this.mPm.mContext.getSystemService(RoleManager.class)).getRoleHolders("android.app.role.HOME")) {
            if (snapshotComputer.getPackage(str) == null) {
                Log.w("PackageManager", "Launcher package " + str + " is not found for dexopting");
            } else {
                performDexoptPackage(str, i, "speed-profile");
            }
        }
    }

    private void performDexoptPackage(String str, int i, String str2) throws Installer.LegacyDexoptDisabledException {
        Installer.checkLegacyDexoptDisabled();
        performDexOptTraced(new DexoptOptions(str, i, str2, null, DexFile.isProfileGuidedCompilerFilter(str2) ? 1 : 0));
    }

    public void performPackageDexOptUpgradeIfNeeded() {
        int i;
        PackageManagerServiceUtils.enforceSystemOrRoot("Only the system can request package update");
        if (hasBcpApexesChanged()) {
            SystemProperties.set("sys.bcp_apex_changed", "1");
        }
        if (this.mPm.isFirstBoot()) {
            i = 0;
        } else if (this.mPm.isDeviceUpgrading()) {
            i = 1;
        } else if (!hasBcpApexesChanged()) {
            return;
        } else {
            i = 13;
        }
        Log.i("PackageManager", "Starting boot dexopt for reason " + DexoptOptions.convertToArtServiceDexoptReason(i));
        long nanoTime = System.nanoTime();
        ExecutorService executorService = (ExecutorService) this.mPm.mPackageManagerServiceExt.beforeOnBootUseArtService().first;
        Consumer consumer = (Consumer) this.mPm.mPackageManagerServiceExt.beforeOnBootUseArtService().second;
        if (useArtService()) {
            this.mBootDexoptStartTime = nanoTime;
            getArtManagerLocal().onBoot(DexoptOptions.convertToArtServiceDexoptReason(i), executorService, consumer);
            this.mPm.mPackageManagerServiceExt.afterOnBootUseArtService(executorService);
            return;
        }
        try {
            checkAndDexOptSystemUi(i);
            dexoptLauncher(i);
            if (i == 1 || i == 0) {
                List<PackageStateInternal> packagesForDexopt = getPackagesForDexopt(this.mPm.snapshotComputer().getPackageStates().values(), this.mPm);
                PackageManagerService packageManagerService = this.mPm;
                packageManagerService.mPackageManagerServiceExt.beforePerformDexOptUpgradeInUpdatePackagesIfNeeded(packageManagerService, packagesForDexopt.size());
                int[] performDexOptUpgrade = performDexOptUpgrade(packagesForDexopt, i, false);
                this.mPm.mPackageManagerServiceExt.afterPerformDexOptUpgradeInUpdatePackagesIfNeeded();
                reportBootDexopt(nanoTime, performDexOptUpgrade[0], performDexOptUpgrade[1], performDexOptUpgrade[2]);
            }
        } catch (Installer.LegacyDexoptDisabledException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportBootDexopt(long j, int i, int i2, int i3) {
        int seconds = (int) TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - j);
        Computer snapshotComputer = this.mPm.snapshotComputer();
        MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_dexopted", i);
        MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_skipped", i2);
        MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_failed", i3);
        MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_num_total", getOptimizablePackages(snapshotComputer).size());
        MetricsLogger.histogram(this.mPm.mContext, "opt_dialog_time_s", seconds);
    }

    public List<String> getOptimizablePackages(Computer computer) {
        final ArrayList arrayList = new ArrayList();
        this.mPm.forEachPackageState(computer, new Consumer() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                DexOptHelper.this.lambda$getOptimizablePackages$0(arrayList, (PackageStateInternal) obj);
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getOptimizablePackages$0(ArrayList arrayList, PackageStateInternal packageStateInternal) {
        AndroidPackageInternal pkg = packageStateInternal.getPkg();
        if (pkg == null || !this.mPm.mPackageDexOptimizer.canOptimizePackage(pkg)) {
            return;
        }
        arrayList.add(packageStateInternal.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean performDexOpt(DexoptOptions dexoptOptions) {
        int performDexOptWithStatus;
        Computer snapshotComputer = this.mPm.snapshotComputer();
        if (snapshotComputer.getInstantAppPackageName(Binder.getCallingUid()) != null || snapshotComputer.isInstantApp(dexoptOptions.getPackageName(), UserHandle.getCallingUserId())) {
            return false;
        }
        AndroidPackage androidPackage = snapshotComputer.getPackage(dexoptOptions.getPackageName());
        if (androidPackage != null && androidPackage.isApex()) {
            return true;
        }
        if (dexoptOptions.isDexoptOnlySecondaryDex()) {
            if (useArtService()) {
                performDexOptWithStatus = performDexOptWithArtService(dexoptOptions, 0);
            } else {
                try {
                    return this.mPm.getDexManager().dexoptSecondaryDex(dexoptOptions);
                } catch (Installer.LegacyDexoptDisabledException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            performDexOptWithStatus = performDexOptWithStatus(dexoptOptions);
        }
        return performDexOptWithStatus != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int performDexOptWithStatus(DexoptOptions dexoptOptions) {
        return performDexOptTraced(dexoptOptions);
    }

    private int performDexOptTraced(DexoptOptions dexoptOptions) {
        ((IPackageManagerServiceUtilsExt) ExtLoader.type(IPackageManagerServiceUtilsExt.class).create()).addBootEvent("PMS:performDexOpt:" + dexoptOptions.getPackageName());
        Trace.traceBegin(16384L, "dexopt");
        try {
            return performDexOptInternal(dexoptOptions);
        } finally {
            Trace.traceEnd(16384L);
        }
    }

    private int performDexOptInternal(DexoptOptions dexoptOptions) {
        if (useArtService()) {
            return performDexOptWithArtService(dexoptOptions, 4);
        }
        synchronized (this.mPm.mLock) {
            AndroidPackage androidPackage = this.mPm.mPackages.get(dexoptOptions.getPackageName());
            PackageSetting packageLPr = this.mPm.mSettings.getPackageLPr(dexoptOptions.getPackageName());
            if (androidPackage != null && packageLPr != null) {
                if (androidPackage.isApex()) {
                    return 0;
                }
                this.mPm.getPackageUsage().maybeWriteAsync(this.mPm.mSettings.getPackagesLocked());
                this.mPm.mCompilerStats.maybeWriteAsync();
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    try {
                        return performDexOptInternalWithDependenciesLI(androidPackage, packageLPr, dexoptOptions);
                    } catch (Installer.LegacyDexoptDisabledException e) {
                        throw new RuntimeException(e);
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
            return -1;
        }
    }

    private int performDexOptWithArtService(DexoptOptions dexoptOptions, int i) {
        try {
            PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
            try {
                PackageState packageState = withFilteredSnapshot.getPackageState(dexoptOptions.getPackageName());
                if (packageState != null) {
                    if (packageState.getAndroidPackage() != null) {
                        int dexoptInPerformDexOptWithArtService = this.mPm.mPackageManagerServiceExt.dexoptInPerformDexOptWithArtService(withFilteredSnapshot, dexoptOptions.getPackageName(), dexoptOptions.convertToDexoptParams(i));
                        withFilteredSnapshot.close();
                        return dexoptInPerformDexOptWithArtService;
                    }
                    withFilteredSnapshot.close();
                    return -1;
                }
                withFilteredSnapshot.close();
                return -1;
            } finally {
            }
        } catch (IllegalStateException e) {
            if (Build.MTK_HBT_ON_64BIT_ONLY_CHIP && e.getMessage().contains("Unsupported isa 'arm'")) {
                Slog.w("PackageManager", "Dexopt with art service is conflict with hbt_translator");
            } else if (Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP && e.getMessage().contains("Unsupported isa 'arm'")) {
                Slog.w("PackageManager", "Dexopt with art service is conflict with tango_translator");
            } else {
                throw e;
            }
            return -1;
        }
    }

    private int performDexOptInternalWithDependenciesLI(AndroidPackage androidPackage, PackageStateInternal packageStateInternal, DexoptOptions dexoptOptions) throws Installer.LegacyDexoptDisabledException {
        PackageDexOptimizer packageDexOptimizer;
        if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(androidPackage.getPackageName())) {
            throw new IllegalArgumentException("Cannot dexopt the system server");
        }
        if (dexoptOptions.isForce()) {
            packageDexOptimizer = new PackageDexOptimizer.ForcedUpdatePackageDexOptimizer(this.mPm.mPackageDexOptimizer);
        } else {
            packageDexOptimizer = this.mPm.mPackageDexOptimizer;
        }
        List<SharedLibraryInfo> findSharedLibraries = SharedLibraryUtils.findSharedLibraries(packageStateInternal);
        String[] appDexInstructionSets = InstructionSets.getAppDexInstructionSets(packageStateInternal.getPrimaryCpuAbi(), packageStateInternal.getSecondaryCpuAbi());
        if (!findSharedLibraries.isEmpty()) {
            DexoptOptions dexoptOptions2 = new DexoptOptions(dexoptOptions.getPackageName(), dexoptOptions.getCompilationReason(), dexoptOptions.getCompilerFilter(), dexoptOptions.getSplitName(), dexoptOptions.getFlags() | 64);
            for (SharedLibraryInfo sharedLibraryInfo : findSharedLibraries) {
                Computer snapshotComputer = this.mPm.snapshotComputer();
                AndroidPackage androidPackage2 = snapshotComputer.getPackage(sharedLibraryInfo.getPackageName());
                PackageStateInternal packageStateInternal2 = snapshotComputer.getPackageStateInternal(sharedLibraryInfo.getPackageName());
                if (androidPackage2 != null && packageStateInternal2 != null) {
                    packageDexOptimizer.performDexOpt(androidPackage2, packageStateInternal2, appDexInstructionSets, this.mPm.getOrCreateCompilerPackageStats(androidPackage2), this.mPm.getDexManager().getPackageUseInfoOrDefault(androidPackage2.getPackageName()), dexoptOptions2);
                }
            }
        }
        return packageDexOptimizer.performDexOpt(androidPackage, packageStateInternal, appDexInstructionSets, this.mPm.getOrCreateCompilerPackageStats(androidPackage), this.mPm.getDexManager().getPackageUseInfoOrDefault(androidPackage.getPackageName()), dexoptOptions);
    }

    @Deprecated
    public void forceDexOpt(Computer computer, String str) throws Installer.LegacyDexoptDisabledException {
        PackageManagerServiceUtils.enforceSystemOrRoot("forceDexOpt");
        PackageStateInternal packageStateInternal = computer.getPackageStateInternal(str);
        AndroidPackageInternal pkg = packageStateInternal == null ? null : packageStateInternal.getPkg();
        if (packageStateInternal == null || pkg == null) {
            throw new IllegalArgumentException("Unknown package: " + str);
        }
        if (pkg.isApex()) {
            throw new IllegalArgumentException("Can't dexopt APEX package: " + str);
        }
        Trace.traceBegin(16384L, "dexopt");
        int performDexOptInternalWithDependenciesLI = performDexOptInternalWithDependenciesLI(pkg, packageStateInternal, new DexoptOptions(str, 12, PackageManagerServiceCompilerMapping.getDefaultCompilerFilter(), null, 6));
        Trace.traceEnd(16384L);
        if (performDexOptInternalWithDependenciesLI == 1) {
            return;
        }
        throw new IllegalStateException("Failed to dexopt: " + performDexOptInternalWithDependenciesLI);
    }

    public boolean performDexOptMode(Computer computer, String str, String str2, boolean z, boolean z2, String str3) {
        if (!PackageManagerServiceUtils.isSystemOrRootOrShell() && !isCallerInstallerForPackage(computer, str)) {
            throw new SecurityException("performDexOptMode");
        }
        int i = (z2 ? 4 : 0) | (z ? 2 : 0);
        if (DexFile.isProfileGuidedCompilerFilter(str2)) {
            i |= 1;
        }
        int i2 = i;
        if (useArtService()) {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return performDexOpt(new DexoptOptions(str, 12, str2, str3, i2));
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
        return performDexOpt(new DexoptOptions(str, 12, str2, str3, i2));
    }

    private boolean isCallerInstallerForPackage(Computer computer, String str) {
        PackageStateInternal packageStateInternal;
        PackageStateInternal packageStateInternal2 = computer.getPackageStateInternal(str);
        return (packageStateInternal2 == null || (packageStateInternal = computer.getPackageStateInternal(packageStateInternal2.getInstallSource().mInstallerPackageName)) == null || packageStateInternal.getPkg().getUid() != Binder.getCallingUid()) ? false : true;
    }

    public boolean performDexOptSecondary(String str, String str2, boolean z) {
        if (this.mPm.mPackageManagerServiceExt.interceptPerformDexOptSecondary(str, str2, z)) {
            return false;
        }
        return performDexOpt(new DexoptOptions(str, 12, str2, null, (z ? 2 : 0) | 13));
    }

    public static List<PackageStateInternal> getPackagesForDexopt(Collection<? extends PackageStateInternal> collection, PackageManagerService packageManagerService) {
        return getPackagesForDexopt(collection, packageManagerService, PackageManagerService.DEBUG_DEXOPT);
    }

    public static List<PackageStateInternal> getPackagesForDexopt(Collection<? extends PackageStateInternal> collection, PackageManagerService packageManagerService, boolean z) {
        Predicate predicate;
        Predicate predicate2;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(collection);
        arrayList2.removeIf(PackageManagerServiceUtils.REMOVE_IF_NULL_PKG);
        arrayList2.removeIf(PackageManagerServiceUtils.REMOVE_IF_APEX_PKG);
        ArrayList arrayList3 = new ArrayList(arrayList2.size());
        Computer snapshotComputer = packageManagerService.snapshotComputer();
        applyPackageFilter(snapshotComputer, new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getPackagesForDexopt$1;
                lambda$getPackagesForDexopt$1 = DexOptHelper.lambda$getPackagesForDexopt$1((PackageStateInternal) obj);
                return lambda$getPackagesForDexopt$1;
            }
        }, arrayList, arrayList2, arrayList3, packageManagerService);
        final ArraySet<String> packageNamesForIntent = getPackageNamesForIntent(new Intent("android.intent.action.PRE_BOOT_COMPLETED"), 0);
        applyPackageFilter(snapshotComputer, new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getPackagesForDexopt$2;
                lambda$getPackagesForDexopt$2 = DexOptHelper.lambda$getPackagesForDexopt$2(packageNamesForIntent, (PackageStateInternal) obj);
                return lambda$getPackagesForDexopt$2;
            }
        }, arrayList, arrayList2, arrayList3, packageManagerService);
        final DexManager dexManager = packageManagerService.getDexManager();
        applyPackageFilter(snapshotComputer, new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getPackagesForDexopt$3;
                lambda$getPackagesForDexopt$3 = DexOptHelper.lambda$getPackagesForDexopt$3(DexManager.this, (PackageStateInternal) obj);
                return lambda$getPackagesForDexopt$3;
            }
        }, arrayList, arrayList2, arrayList3, packageManagerService);
        if (!arrayList2.isEmpty() && packageManagerService.isHistoricalPackageUsageAvailable()) {
            if (z) {
                Log.i("PackageManager", "Looking at historical package use");
            }
            PackageStateInternal packageStateInternal = (PackageStateInternal) Collections.max(arrayList2, Comparator.comparingLong(new ToLongFunction() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda4
                @Override // java.util.function.ToLongFunction
                public final long applyAsLong(Object obj) {
                    long lambda$getPackagesForDexopt$4;
                    lambda$getPackagesForDexopt$4 = DexOptHelper.lambda$getPackagesForDexopt$4((PackageStateInternal) obj);
                    return lambda$getPackagesForDexopt$4;
                }
            }));
            if (z) {
                Log.i("PackageManager", "Taking package " + packageStateInternal.getPackageName() + " as reference in time use");
            }
            long latestForegroundPackageUseTimeInMills = packageStateInternal.getTransientState().getLatestForegroundPackageUseTimeInMills();
            if (latestForegroundPackageUseTimeInMills != 0) {
                final long j = latestForegroundPackageUseTimeInMills - 604800000;
                predicate2 = new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda5
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$getPackagesForDexopt$5;
                        lambda$getPackagesForDexopt$5 = DexOptHelper.lambda$getPackagesForDexopt$5(j, (PackageStateInternal) obj);
                        return lambda$getPackagesForDexopt$5;
                    }
                };
            } else {
                predicate2 = new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda6
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean lambda$getPackagesForDexopt$6;
                        lambda$getPackagesForDexopt$6 = DexOptHelper.lambda$getPackagesForDexopt$6((PackageStateInternal) obj);
                        return lambda$getPackagesForDexopt$6;
                    }
                };
            }
            sortPackagesByUsageDate(arrayList2, packageManagerService);
            predicate = predicate2;
        } else {
            predicate = new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$getPackagesForDexopt$7;
                    lambda$getPackagesForDexopt$7 = DexOptHelper.lambda$getPackagesForDexopt$7((PackageStateInternal) obj);
                    return lambda$getPackagesForDexopt$7;
                }
            };
        }
        applyPackageFilter(snapshotComputer, predicate, arrayList, arrayList2, arrayList3, packageManagerService);
        arrayList.removeIf(new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getPackagesForDexopt$8;
                lambda$getPackagesForDexopt$8 = DexOptHelper.lambda$getPackagesForDexopt$8((PackageStateInternal) obj);
                return lambda$getPackagesForDexopt$8;
            }
        });
        if (z) {
            Log.i("PackageManager", "Packages to be dexopted: " + packagesToString(arrayList));
            Log.i("PackageManager", "Packages skipped from dexopt: " + packagesToString(arrayList2));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPackagesForDexopt$1(PackageStateInternal packageStateInternal) {
        return packageStateInternal.getPkg().isCoreApp();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPackagesForDexopt$2(ArraySet arraySet, PackageStateInternal packageStateInternal) {
        return arraySet.contains(packageStateInternal.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPackagesForDexopt$3(DexManager dexManager, PackageStateInternal packageStateInternal) {
        return dexManager.getPackageUseInfoOrDefault(packageStateInternal.getPackageName()).isAnyCodePathUsedByOtherApps();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ long lambda$getPackagesForDexopt$4(PackageStateInternal packageStateInternal) {
        return packageStateInternal.getTransientState().getLatestForegroundPackageUseTimeInMills();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPackagesForDexopt$5(long j, PackageStateInternal packageStateInternal) {
        return packageStateInternal.getTransientState().getLatestForegroundPackageUseTimeInMills() >= j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPackagesForDexopt$8(PackageStateInternal packageStateInternal) {
        return PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageStateInternal.getPackageName());
    }

    private static void applyPackageFilter(Computer computer, Predicate<PackageStateInternal> predicate, Collection<PackageStateInternal> collection, Collection<PackageStateInternal> collection2, List<PackageStateInternal> list, PackageManagerService packageManagerService) {
        for (PackageStateInternal packageStateInternal : collection2) {
            if (predicate.test(packageStateInternal)) {
                list.add(packageStateInternal);
            }
        }
        sortPackagesByUsageDate(list, packageManagerService);
        collection2.removeAll(list);
        for (PackageStateInternal packageStateInternal2 : list) {
            collection.add(packageStateInternal2);
            List<PackageStateInternal> findSharedNonSystemLibraries = computer.findSharedNonSystemLibraries(packageStateInternal2);
            if (!findSharedNonSystemLibraries.isEmpty()) {
                findSharedNonSystemLibraries.removeAll(collection);
                collection.addAll(findSharedNonSystemLibraries);
                collection2.removeAll(findSharedNonSystemLibraries);
            }
        }
        list.clear();
    }

    private static void sortPackagesByUsageDate(List<PackageStateInternal> list, PackageManagerService packageManagerService) {
        if (packageManagerService.isHistoricalPackageUsageAvailable()) {
            Collections.sort(list, new Comparator() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int lambda$sortPackagesByUsageDate$9;
                    lambda$sortPackagesByUsageDate$9 = DexOptHelper.lambda$sortPackagesByUsageDate$9((PackageStateInternal) obj, (PackageStateInternal) obj2);
                    return lambda$sortPackagesByUsageDate$9;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$sortPackagesByUsageDate$9(PackageStateInternal packageStateInternal, PackageStateInternal packageStateInternal2) {
        return Long.compare(packageStateInternal2.getTransientState().getLatestForegroundPackageUseTimeInMills(), packageStateInternal.getTransientState().getLatestForegroundPackageUseTimeInMills());
    }

    private static ArraySet<String> getPackageNamesForIntent(Intent intent, int i) {
        List list;
        try {
            list = AppGlobals.getPackageManager().queryIntentReceivers(intent, (String) null, 0L, i).getList();
        } catch (RemoteException unused) {
            list = null;
        }
        ArraySet<String> arraySet = new ArraySet<>();
        if (list != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                arraySet.add(((ResolveInfo) it.next()).activityInfo.packageName);
            }
        }
        return arraySet;
    }

    public static String packagesToString(List<PackageStateInternal> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(list.get(i).getPackageName());
        }
        return sb.toString();
    }

    public static void requestCopyPreoptedFiles() {
        if (SystemProperties.getInt("ro.cp_system_other_odex", 0) == 1) {
            SystemProperties.set("sys.cppreopt", "requested");
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = 100000 + uptimeMillis;
            long j2 = uptimeMillis;
            while (true) {
                if (SystemProperties.get("sys.cppreopt").equals("finished")) {
                    break;
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException unused) {
                }
                j2 = SystemClock.uptimeMillis();
                if (j2 > j) {
                    SystemProperties.set("sys.cppreopt", "timed-out");
                    Slog.wtf("PackageManager", "cppreopt did not finish!");
                    break;
                }
            }
            Slog.i("PackageManager", "cppreopts took " + (j2 - uptimeMillis) + " ms");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void controlDexOptBlocking(boolean z) throws Installer.LegacyDexoptDisabledException {
        this.mPm.mPackageDexOptimizer.controlDexOptBlocking(z);
    }

    public static List<PackageStateInternal> getPersistPackagesForDexopt(Collection<PackageSetting> collection, PackageManagerService packageManagerService) {
        ArrayList arrayList = new ArrayList(collection);
        LinkedList linkedList = new LinkedList();
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        Computer snapshotComputer = packageManagerService.snapshotComputer();
        final ArraySet<String> packageNamesForPersist = getPackageNamesForPersist();
        applyPackageFilter(snapshotComputer, new Predicate() { // from class: com.android.server.pm.DexOptHelper$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getPersistPackagesForDexopt$10;
                lambda$getPersistPackagesForDexopt$10 = DexOptHelper.lambda$getPersistPackagesForDexopt$10(packageNamesForPersist, (PackageStateInternal) obj);
                return lambda$getPersistPackagesForDexopt$10;
            }
        }, linkedList, arrayList, arrayList2, packageManagerService);
        if (PackageManagerService.DEBUG_DEXOPT) {
            Slog.i("PackageManager", "Persist Packages to be dexopted: " + packagesToString(linkedList));
            Slog.i("PackageManager", "Persist Packages skipped from dexopt: " + packagesToString(arrayList));
        }
        return linkedList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPersistPackagesForDexopt$10(ArraySet arraySet, PackageStateInternal packageStateInternal) {
        return arraySet.contains(packageStateInternal.getPackageName());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v17 */
    /* JADX WARN: Type inference failed for: r5v18 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v3, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r5v5 */
    /* JADX WARN: Type inference failed for: r5v7 */
    private static ArraySet<String> getPackageNamesForPersist() {
        StringBuilder sb;
        String str = SystemProperties.get("ro.commonsoft.product", "");
        String str2 = "/data/engineermode/persistname";
        String format = (str == null || str.isEmpty()) ? "/data/engineermode/persistname" : String.format("/data/engineermode/persistname_%s.txt", str.trim());
        if (new File(format).exists()) {
            str2 = format;
        } else {
            Slog.i("PackageManager", "common soft but same built in apps");
        }
        ArraySet<String> arraySet = new ArraySet<>();
        File file = new File(str2);
        if (file.exists()) {
            ?? r5 = 0;
            boolean z = false;
            BufferedReader bufferedReader = null;
            try {
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                    while (true) {
                        try {
                            String readLine = bufferedReader2.readLine();
                            if (readLine != null) {
                                z = readLine.isEmpty();
                                if (!z) {
                                    Slog.i("PackageManager", "getPackageNamesForPersist read package name from " + str2 + " is " + readLine);
                                    z = arraySet.contains(readLine);
                                    if (!z) {
                                        arraySet.add(readLine);
                                    }
                                }
                            } else {
                                try {
                                    break;
                                } catch (IOException e) {
                                    e = e;
                                    sb = new StringBuilder();
                                    sb.append("getPackageNamesForPersist get io close exception :");
                                    sb.append(e.getMessage());
                                    Slog.e("PackageManager", sb.toString());
                                    return arraySet;
                                }
                            }
                        } catch (IOException e2) {
                            e = e2;
                            bufferedReader = bufferedReader2;
                            Slog.e("PackageManager", "getPackageNamesForPersist error :" + e.getMessage());
                            r5 = bufferedReader;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                    r5 = bufferedReader;
                                } catch (IOException e3) {
                                    e = e3;
                                    sb = new StringBuilder();
                                    sb.append("getPackageNamesForPersist get io close exception :");
                                    sb.append(e.getMessage());
                                    Slog.e("PackageManager", sb.toString());
                                    return arraySet;
                                }
                            }
                            return arraySet;
                        } catch (Throwable th) {
                            th = th;
                            r5 = bufferedReader2;
                            if (r5 != 0) {
                                try {
                                    r5.close();
                                } catch (IOException e4) {
                                    Slog.e("PackageManager", "getPackageNamesForPersist get io close exception :" + e4.getMessage());
                                }
                            }
                            throw th;
                        }
                    }
                    bufferedReader2.close();
                    r5 = z;
                } catch (IOException e5) {
                    e = e5;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } else {
            Slog.e("PackageManager", "getPackageNamesForPersist file not exists : " + str2);
        }
        return arraySet;
    }

    public static void dumpDexoptState(IndentingPrintWriter indentingPrintWriter, String str) {
        try {
            PackageManagerLocal.FilteredSnapshot withFilteredSnapshot = PackageManagerServiceUtils.getPackageManagerLocal().withFilteredSnapshot();
            try {
                if (str != null) {
                    try {
                        getArtManagerLocal().dumpPackage(indentingPrintWriter, withFilteredSnapshot, str);
                    } catch (IllegalArgumentException e) {
                        indentingPrintWriter.println(e);
                    }
                } else {
                    getArtManagerLocal().dump(indentingPrintWriter, withFilteredSnapshot);
                }
                if (withFilteredSnapshot != null) {
                    withFilteredSnapshot.close();
                }
            } finally {
            }
        } catch (IllegalStateException e2) {
            if (Build.MTK_HBT_ON_64BIT_ONLY_CHIP && e2.getMessage().contains("Unsupported isa 'arm'")) {
                Slog.w("PackageManager", "Dexopt with art service is conflict with hbt_translator");
            } else {
                if (Build.QCOM_TANGO_ON_64BIT_ONLY_CHIP && e2.getMessage().contains("Unsupported isa 'arm'")) {
                    Slog.w("PackageManager", "Dexopt with art service is conflict with tango_translator");
                    return;
                }
                throw e2;
            }
        }
    }

    private static List<String> getBcpApexes() {
        String str = System.getenv("BOOTCLASSPATH");
        if (TextUtils.isEmpty(str)) {
            Log.e("PackageManager", "Unable to get BOOTCLASSPATH");
            return List.of();
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : str.split(":")) {
            Path path = Paths.get(str2, new String[0]);
            if (path.getNameCount() >= 2 && path.getName(0).toString().equals("apex")) {
                arrayList.add(path.getName(1).toString());
            }
        }
        return arrayList;
    }

    private static boolean hasBcpApexesChanged() {
        HashSet hashSet = new HashSet(getBcpApexes());
        for (ApexManager.ActiveApexInfo activeApexInfo : ApexManager.getInstance().getActiveApexInfos()) {
            if (hashSet.contains(activeApexInfo.apexModuleName) && activeApexInfo.activeApexChanged) {
                return true;
            }
        }
        return false;
    }

    public static boolean useArtService() {
        return SystemProperties.getBoolean("dalvik.vm.useartservice", false);
    }

    public static DexUseManagerLocal getDexUseManagerLocal() {
        if (!useArtService()) {
            return null;
        }
        try {
            return (DexUseManagerLocal) LocalManagerRegistry.getManagerOrThrow(DexUseManagerLocal.class);
        } catch (LocalManagerRegistry.ManagerNotFoundException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private class DexoptDoneHandler implements ArtManagerLocal.DexoptDoneCallback {
        private DexoptDoneHandler() {
        }

        public void onDexoptDone(DexoptResult dexoptResult) {
            String reason = dexoptResult.getReason();
            reason.hashCode();
            char c = 65535;
            switch (reason.hashCode()) {
                case -1205769507:
                    if (reason.equals("boot-after-mainline-update")) {
                        c = 0;
                        break;
                    }
                    break;
                case -587828592:
                    if (reason.equals("boot-after-ota")) {
                        c = 1;
                        break;
                    }
                    break;
                case -207505425:
                    if (reason.equals("first-boot")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                case 2:
                    Iterator it = dexoptResult.getPackageDexoptResults().iterator();
                    int i = 0;
                    int i2 = 0;
                    int i3 = 0;
                    while (it.hasNext()) {
                        int status = ((DexoptResult.PackageDexoptResult) it.next()).getStatus();
                        if (status == 10) {
                            i2++;
                        } else if (status == 20) {
                            i++;
                        } else if (status == 30) {
                            i3++;
                        }
                    }
                    DexOptHelper dexOptHelper = DexOptHelper.this;
                    dexOptHelper.reportBootDexopt(dexOptHelper.mBootDexoptStartTime, i, i2, i3);
                    break;
            }
            for (DexoptResult.PackageDexoptResult packageDexoptResult : dexoptResult.getPackageDexoptResults()) {
                CompilerStats.PackageStats orCreateCompilerPackageStats = DexOptHelper.this.mPm.getOrCreateCompilerPackageStats(packageDexoptResult.getPackageName());
                for (DexoptResult.DexContainerFileDexoptResult dexContainerFileDexoptResult : packageDexoptResult.getDexContainerFileDexoptResults()) {
                    orCreateCompilerPackageStats.setCompileTime(dexContainerFileDexoptResult.getDexContainerFile(), dexContainerFileDexoptResult.getDex2oatWallTimeMillis());
                }
            }
            synchronized (DexOptHelper.this.mPm.mLock) {
                DexOptHelper.this.mPm.getPackageUsage().maybeWriteAsync(DexOptHelper.this.mPm.mSettings.getPackagesLocked());
                DexOptHelper.this.mPm.mCompilerStats.maybeWriteAsync();
            }
            if (dexoptResult.getReason().equals("inactive")) {
                for (DexoptResult.PackageDexoptResult packageDexoptResult2 : dexoptResult.getPackageDexoptResults()) {
                    if (packageDexoptResult2.getStatus() == 20) {
                        long j = 0;
                        long j2 = 0;
                        for (DexoptResult.DexContainerFileDexoptResult dexContainerFileDexoptResult2 : packageDexoptResult2.getDexContainerFileDexoptResults()) {
                            long length = new File(dexContainerFileDexoptResult2.getDexContainerFile()).length();
                            j2 += dexContainerFileDexoptResult2.getSizeBytes() + length;
                            j += dexContainerFileDexoptResult2.getSizeBeforeBytes() + length;
                        }
                        FrameworkStatsLog.write(128, packageDexoptResult2.getPackageName(), j, j2, false);
                    }
                }
            }
            ArraySet arraySet = new ArraySet();
            for (DexoptResult.PackageDexoptResult packageDexoptResult3 : dexoptResult.getPackageDexoptResults()) {
                if (packageDexoptResult3.hasUpdatedArtifacts()) {
                    arraySet.add(packageDexoptResult3.getPackageName());
                }
            }
            if (arraySet.isEmpty()) {
                return;
            }
            ((PinnerService) LocalServices.getService(PinnerService.class)).update(arraySet, false);
        }
    }

    public static void initializeArtManagerLocal(Context context, PackageManagerService packageManagerService) {
        if (useArtService()) {
            final ArtManagerLocal artManagerLocal = new ArtManagerLocal(context);
            SystemServerInitThreadPool$.ExternalSyntheticLambda1 externalSyntheticLambda1 = new SystemServerInitThreadPool$.ExternalSyntheticLambda1();
            DexOptHelper dexOptHelper = packageManagerService.getDexOptHelper();
            Objects.requireNonNull(dexOptHelper);
            artManagerLocal.addDexoptDoneCallback(false, externalSyntheticLambda1, new DexoptDoneHandler());
            LocalManagerRegistry.addManager(ArtManagerLocal.class, artManagerLocal);
            sArtManagerLocalIsInitialized = true;
            context.registerReceiver(new BroadcastReceiver() { // from class: com.android.server.pm.DexOptHelper.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    context2.unregisterReceiver(this);
                    artManagerLocal.scheduleBackgroundDexoptJob();
                }
            }, new IntentFilter("android.intent.action.BOOT_COMPLETED"));
            if (useArtService()) {
                packageManagerService.mPackageManagerServiceExt.afterInitializeArtManagerLocal(context);
            }
        }
    }

    public static boolean artManagerLocalIsInitialized() {
        return sArtManagerLocalIsInitialized;
    }

    public static ArtManagerLocal getArtManagerLocal() {
        try {
            return (ArtManagerLocal) LocalManagerRegistry.getManagerOrThrow(ArtManagerLocal.class);
        } catch (LocalManagerRegistry.ManagerNotFoundException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    private static int convertToDexOptResult(DexoptResult dexoptResult) {
        int finalStatus = dexoptResult.getFinalStatus();
        if (finalStatus == 10) {
            return 0;
        }
        if (finalStatus == 20) {
            return 1;
        }
        if (finalStatus == 30) {
            return -1;
        }
        if (finalStatus == 40) {
            return 2;
        }
        throw new IllegalArgumentException("DexoptResult for " + ((DexoptResult.PackageDexoptResult) dexoptResult.getPackageDexoptResults().get(0)).getPackageName() + " has unsupported status " + finalStatus);
    }
}
