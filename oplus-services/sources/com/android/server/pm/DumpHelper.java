package com.android.server.pm;

import android.content.ComponentName;
import android.content.pm.FeatureInfo;
import android.os.Binder;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.incremental.PerUidReadTimeouts;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.pm.permission.PermissionManagerServiceInternal;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import com.android.server.pm.resolution.ComponentResolverApi;
import com.android.server.pm.verify.domain.DomainVerificationManagerInternal;
import dalvik.annotation.optimization.NeverCompile;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.function.BiConsumer;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class DumpHelper {
    private final ArrayMap<String, FeatureInfo> mAvailableFeatures;
    private final ChangedPackagesTracker mChangedPackagesTracker;
    private final DomainVerificationManagerInternal mDomainVerificationManager;
    public IDumpHelperExt mDumpHelperExt = (IDumpHelperExt) ExtLoader.type(IDumpHelperExt.class).base(this).create();
    private final PackageInstallerService mInstallerService;
    private final KnownPackages mKnownPackages;
    private final PerUidReadTimeouts[] mPerUidReadTimeouts;
    private final PermissionManagerServiceInternal mPermissionManager;
    private final ArraySet<String> mProtectedBroadcasts;
    private final String[] mRequiredVerifierPackages;
    private final SnapshotStatistics mSnapshotStatistics;
    private final StorageEventHelper mStorageEventHelper;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DumpHelper(PermissionManagerServiceInternal permissionManagerServiceInternal, StorageEventHelper storageEventHelper, DomainVerificationManagerInternal domainVerificationManagerInternal, PackageInstallerService packageInstallerService, String[] strArr, KnownPackages knownPackages, ChangedPackagesTracker changedPackagesTracker, ArrayMap<String, FeatureInfo> arrayMap, ArraySet<String> arraySet, PerUidReadTimeouts[] perUidReadTimeoutsArr, SnapshotStatistics snapshotStatistics) {
        this.mPermissionManager = permissionManagerServiceInternal;
        this.mStorageEventHelper = storageEventHelper;
        this.mDomainVerificationManager = domainVerificationManagerInternal;
        this.mInstallerService = packageInstallerService;
        this.mRequiredVerifierPackages = strArr;
        this.mKnownPackages = knownPackages;
        this.mChangedPackagesTracker = changedPackagesTracker;
        this.mAvailableFeatures = arrayMap;
        this.mProtectedBroadcasts = arraySet;
        this.mPerUidReadTimeouts = perUidReadTimeoutsArr;
        this.mSnapshotStatistics = snapshotStatistics;
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x0479  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x049e  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x04b5  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0510  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x051e  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x05e4  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x05e9  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x05fe  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x06c2  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x06ee  */
    /* JADX WARN: Removed duplicated region for block: B:262:0x070d  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0772  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x078b  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x07a3  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x07d3  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x082d A[LOOP:6: B:316:0x082b->B:317:0x082d, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:325:0x08b3  */
    /* JADX WARN: Removed duplicated region for block: B:343:0x06d5  */
    @NeverCompile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void doDump(Computer computer, FileDescriptor fileDescriptor, final PrintWriter printWriter, String[] strArr) {
        ArraySet<String> arraySet;
        String targetPackageName;
        boolean isCheckIn;
        ComponentResolverApi componentResolver;
        String str;
        int i;
        int length;
        int i2;
        String[] strArr2;
        int i3;
        String str2;
        DumpState dumpState = new DumpState();
        int i4 = 0;
        while (i4 < strArr.length && (str2 = strArr[i4]) != null && str2.length() > 0 && str2.charAt(0) == '-') {
            i4++;
            if (!"-a".equals(str2)) {
                if ("-h".equals(str2)) {
                    printHelp(printWriter);
                    return;
                }
                if ("--checkin".equals(str2)) {
                    dumpState.setCheckIn(true);
                } else if ("--all-components".equals(str2)) {
                    dumpState.setOptionEnabled(2);
                } else if ("-f".equals(str2)) {
                    dumpState.setOptionEnabled(1);
                } else if ("--include-apex".equals(str2)) {
                    dumpState.setOptionEnabled(8);
                } else {
                    if ("--proto".equals(str2)) {
                        dumpProto(computer, fileDescriptor);
                        return;
                    }
                    printWriter.println("Unknown argument: " + str2 + "; use -h for help");
                }
            }
        }
        if (i4 < strArr.length) {
            String str3 = strArr[i4];
            int i5 = i4 + 1;
            if (PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str3) || str3.contains(".") || this.mDumpHelperExt.hasOplusPackageName(str3)) {
                dumpState.setTargetPackageName(str3);
                dumpState.setOptionEnabled(1);
            } else {
                if ("check-permission".equals(str3)) {
                    if (i5 >= strArr.length) {
                        printWriter.println("Error: check-permission missing permission argument");
                        return;
                    }
                    String str4 = strArr[i5];
                    int i6 = i5 + 1;
                    if (i6 >= strArr.length) {
                        printWriter.println("Error: check-permission missing package argument");
                        return;
                    }
                    String str5 = strArr[i6];
                    int i7 = i6 + 1;
                    int userId = UserHandle.getUserId(Binder.getCallingUid());
                    if (i7 < strArr.length) {
                        try {
                            userId = Integer.parseInt(strArr[i7]);
                        } catch (NumberFormatException unused) {
                            printWriter.println("Error: check-permission user argument is not a number: " + strArr[i7]);
                            return;
                        }
                    }
                    printWriter.println(this.mPermissionManager.checkPermission(str4, computer.resolveInternalPackageName(str5, -1L), userId));
                    return;
                }
                if ("l".equals(str3) || "libraries".equals(str3)) {
                    dumpState.setDump(1);
                } else if ("f".equals(str3) || "features".equals(str3)) {
                    dumpState.setDump(2);
                } else if ("r".equals(str3) || "resolvers".equals(str3)) {
                    if (i5 >= strArr.length) {
                        dumpState.setDump(60);
                    } else {
                        while (i5 < strArr.length) {
                            String str6 = strArr[i5];
                            if ("a".equals(str6) || "activity".equals(str6)) {
                                dumpState.setDump(4);
                            } else if ("s".equals(str6) || "service".equals(str6)) {
                                dumpState.setDump(8);
                            } else if ("r".equals(str6) || ParsingPackageUtils.TAG_RECEIVER.equals(str6)) {
                                dumpState.setDump(16);
                            } else {
                                if (!"c".equals(str6) && !"content".equals(str6)) {
                                    printWriter.println("Error: unknown resolver table type: " + str6);
                                    return;
                                }
                                dumpState.setDump(32);
                            }
                            i5++;
                        }
                    }
                } else if ("perm".equals(str3) || "permissions".equals(str3)) {
                    dumpState.setDump(64);
                } else {
                    if (ParsingPackageUtils.TAG_PERMISSION.equals(str3)) {
                        if (i5 >= strArr.length) {
                            printWriter.println("Error: permission requires permission name");
                            return;
                        }
                        ArraySet<String> arraySet2 = new ArraySet<>();
                        while (i5 < strArr.length) {
                            arraySet2.add(strArr[i5]);
                            i5++;
                        }
                        dumpState.setDump(448);
                        arraySet = arraySet2;
                        targetPackageName = dumpState.getTargetPackageName();
                        isCheckIn = dumpState.isCheckIn();
                        if (targetPackageName == null && computer.getPackageStateInternal(targetPackageName) == null && !computer.isApexPackage(targetPackageName)) {
                            printWriter.println("Unable to find package: " + targetPackageName);
                            return;
                        }
                        if (isCheckIn) {
                            printWriter.println("vers,1");
                        }
                        if (!isCheckIn && dumpState.isDumping(32768) && targetPackageName == null) {
                            computer.dump(32768, fileDescriptor, printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(DumpState.DUMP_KNOWN_PACKAGES) && targetPackageName == null) {
                            if (dumpState.onTitlePrinted()) {
                                printWriter.println();
                            }
                            IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ", 120);
                            indentingPrintWriter.println("Known Packages:");
                            indentingPrintWriter.increaseIndent();
                            for (i3 = 0; i3 <= 19; i3++) {
                                indentingPrintWriter.print(KnownPackages.knownPackageToString(i3));
                                indentingPrintWriter.println(":");
                                String[] knownPackageNames = this.mKnownPackages.getKnownPackageNames(computer, i3, 0);
                                indentingPrintWriter.increaseIndent();
                                if (ArrayUtils.isEmpty(knownPackageNames)) {
                                    indentingPrintWriter.println("none");
                                } else {
                                    for (String str7 : knownPackageNames) {
                                        indentingPrintWriter.println(str7);
                                    }
                                }
                                indentingPrintWriter.decreaseIndent();
                            }
                            indentingPrintWriter.decreaseIndent();
                        }
                        if (dumpState.isDumping(2048) && targetPackageName == null) {
                            if (!isCheckIn && this.mRequiredVerifierPackages.length > 0) {
                                if (dumpState.onTitlePrinted()) {
                                    printWriter.println();
                                }
                                printWriter.println("Verifiers:");
                            }
                            String[] strArr3 = this.mRequiredVerifierPackages;
                            length = strArr3.length;
                            i2 = 0;
                            while (i2 < length) {
                                String str8 = strArr3[i2];
                                if (!isCheckIn) {
                                    printWriter.print("  Required: ");
                                    printWriter.print(str8);
                                    printWriter.print(" (uid=");
                                    strArr2 = strArr3;
                                    printWriter.print(computer.getPackageUid(str8, 268435456L, 0));
                                    printWriter.println(")");
                                } else {
                                    strArr2 = strArr3;
                                    printWriter.print("vrfy,");
                                    printWriter.print(str8);
                                    printWriter.print(",");
                                    printWriter.println(computer.getPackageUid(str8, 268435456L, 0));
                                }
                                i2++;
                                strArr3 = strArr2;
                            }
                        }
                        if (!dumpState.isDumping(131072) && targetPackageName == null) {
                            ComponentName componentName = this.mDomainVerificationManager.getProxy().getComponentName();
                            if (componentName != null) {
                                String packageName = componentName.getPackageName();
                                if (!isCheckIn) {
                                    if (dumpState.onTitlePrinted()) {
                                        printWriter.println();
                                    }
                                    printWriter.println("Domain Verifier:");
                                    printWriter.print("  Using: ");
                                    printWriter.print(packageName);
                                    printWriter.print(" (uid=");
                                    printWriter.print(computer.getPackageUid(packageName, 268435456L, 0));
                                    printWriter.println(")");
                                } else if (packageName != null) {
                                    printWriter.print("dv,");
                                    printWriter.print(packageName);
                                    printWriter.print(",");
                                    printWriter.println(computer.getPackageUid(packageName, 268435456L, 0));
                                }
                            } else {
                                printWriter.println();
                                printWriter.println("No Domain Verifier available!");
                            }
                        }
                        if (dumpState.isDumping(1) && targetPackageName == null) {
                            computer.dump(1, fileDescriptor, printWriter, dumpState);
                        }
                        if (dumpState.isDumping(2) && targetPackageName == null) {
                            if (dumpState.onTitlePrinted()) {
                                printWriter.println();
                            }
                            if (!isCheckIn) {
                                printWriter.println("Features:");
                            }
                            for (FeatureInfo featureInfo : this.mAvailableFeatures.values()) {
                                if (!isCheckIn) {
                                    printWriter.print("  ");
                                    printWriter.print(featureInfo.name);
                                    if (featureInfo.version > 0) {
                                        printWriter.print(" version=");
                                        printWriter.print(featureInfo.version);
                                    }
                                    printWriter.println();
                                } else {
                                    printWriter.print("feat,");
                                    printWriter.print(featureInfo.name);
                                    printWriter.print(",");
                                    printWriter.println(featureInfo.version);
                                }
                            }
                        }
                        componentResolver = computer.getComponentResolver();
                        if (!isCheckIn && dumpState.isDumping(4)) {
                            componentResolver.dumpActivityResolvers(printWriter, dumpState, targetPackageName);
                        }
                        if (!isCheckIn && dumpState.isDumping(16)) {
                            componentResolver.dumpReceiverResolvers(printWriter, dumpState, targetPackageName);
                        }
                        if (!isCheckIn && dumpState.isDumping(8)) {
                            componentResolver.dumpServiceResolvers(printWriter, dumpState, targetPackageName);
                        }
                        if (!isCheckIn && dumpState.isDumping(32)) {
                            componentResolver.dumpProviderResolvers(printWriter, dumpState, targetPackageName);
                        }
                        if (!isCheckIn && dumpState.isDumping(4096)) {
                            computer.dump(4096, fileDescriptor, printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(8192) && targetPackageName == null) {
                            computer.dump(8192, fileDescriptor, printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(DumpState.DUMP_DOMAIN_PREFERRED)) {
                            computer.dump(DumpState.DUMP_DOMAIN_PREFERRED, fileDescriptor, printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(64)) {
                            computer.dumpPermissions(printWriter, targetPackageName, arraySet, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(1024)) {
                            componentResolver.dumpContentProviders(computer, printWriter, dumpState, targetPackageName);
                        }
                        if (!isCheckIn && dumpState.isDumping(16384)) {
                            computer.dumpKeySet(printWriter, targetPackageName, dumpState);
                        }
                        if (dumpState.isDumping(128)) {
                            str = targetPackageName;
                            i = 524288;
                        } else {
                            str = targetPackageName;
                            i = 524288;
                            computer.dumpPackages(printWriter, targetPackageName, arraySet, dumpState, isCheckIn);
                        }
                        if (!isCheckIn && dumpState.isDumping(67108864)) {
                            computer.dump(67108864, fileDescriptor, printWriter, dumpState);
                        }
                        if (dumpState.isDumping(256)) {
                            computer.dumpSharedUsers(printWriter, str, arraySet, dumpState, isCheckIn);
                        }
                        if (!isCheckIn && dumpState.isDumping(DumpState.DUMP_CHANGES) && str == null) {
                            if (dumpState.onTitlePrinted()) {
                                printWriter.println();
                            }
                            printWriter.println("Package Changes:");
                            this.mChangedPackagesTracker.iterateAll(new BiConsumer() { // from class: com.android.server.pm.DumpHelper$$ExternalSyntheticLambda0
                                @Override // java.util.function.BiConsumer
                                public final void accept(Object obj, Object obj2) {
                                    DumpHelper.lambda$doDump$0(printWriter, (Integer) obj, (SparseArray) obj2);
                                }
                            });
                        }
                        if (!isCheckIn && dumpState.isDumping(i) && str == null) {
                            computer.dump(i, fileDescriptor, printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(8388608) && str == null) {
                            this.mStorageEventHelper.dumpLoadedVolumes(printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(DumpState.DUMP_SERVICE_PERMISSIONS) && str == null) {
                            componentResolver.dumpServicePermissions(printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(1048576)) {
                            computer.dump(1048576, fileDescriptor, printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(DumpState.DUMP_COMPILER_STATS)) {
                            computer.dump(DumpState.DUMP_COMPILER_STATS, fileDescriptor, printWriter, dumpState);
                        }
                        if (dumpState.isDumping(512) && str == null) {
                            if (isCheckIn) {
                                if (dumpState.onTitlePrinted()) {
                                    printWriter.println();
                                }
                                computer.dump(512, fileDescriptor, printWriter, dumpState);
                                printWriter.println();
                                printWriter.println("Package warning messages:");
                                PackageManagerServiceUtils.dumpCriticalInfo(printWriter, null);
                            } else {
                                PackageManagerServiceUtils.dumpCriticalInfo(printWriter, "msg,");
                            }
                        }
                        if (!isCheckIn && dumpState.isDumping(65536) && str == null) {
                            if (dumpState.onTitlePrinted()) {
                                printWriter.println();
                            }
                            this.mInstallerService.dump(new IndentingPrintWriter(printWriter, "  ", 120));
                        }
                        if (!isCheckIn && dumpState.isDumping(DumpState.DUMP_APEX)) {
                            computer.dump(DumpState.DUMP_APEX, fileDescriptor, printWriter, dumpState);
                        }
                        if (!isCheckIn && dumpState.isDumping(268435456) && str == null) {
                            if (dumpState.onTitlePrinted()) {
                                printWriter.println();
                            }
                            printWriter.println("Per UID read timeouts:");
                            printWriter.println("    Default timeouts flag: " + PackageManagerService.getDefaultTimeouts());
                            printWriter.println("    Known digesters list flag: " + PackageManagerService.getKnownDigestersList());
                            printWriter.println("    Timeouts (" + this.mPerUidReadTimeouts.length + "):");
                            for (PerUidReadTimeouts perUidReadTimeouts : this.mPerUidReadTimeouts) {
                                printWriter.print("        (");
                                printWriter.print("uid=" + perUidReadTimeouts.uid + ", ");
                                printWriter.print("minTimeUs=" + perUidReadTimeouts.minTimeUs + ", ");
                                printWriter.print("minPendingTimeUs=" + perUidReadTimeouts.minPendingTimeUs + ", ");
                                StringBuilder sb = new StringBuilder();
                                sb.append("maxPendingTimeUs=");
                                sb.append(perUidReadTimeouts.maxPendingTimeUs);
                                printWriter.print(sb.toString());
                                printWriter.println(")");
                            }
                        }
                        if (!isCheckIn && dumpState.isDumping(536870912) && str == null) {
                            if (dumpState.onTitlePrinted()) {
                                printWriter.println();
                            }
                            printWriter.println("Snapshot statistics:");
                            this.mSnapshotStatistics.dump(printWriter, "   ", SystemClock.currentTimeMicro(), computer.getUsed(), dumpState.isBrief());
                        }
                        if (isCheckIn && dumpState.isDumping(1073741824) && str == null) {
                            if (dumpState.onTitlePrinted()) {
                                printWriter.println();
                            }
                            printWriter.println("Protected broadcast actions:");
                            for (int i8 = 0; i8 < this.mProtectedBroadcasts.size(); i8++) {
                                printWriter.print("  ");
                                printWriter.println(this.mProtectedBroadcasts.valueAt(i8));
                            }
                            return;
                        }
                        return;
                    }
                    if ("pref".equals(str3) || "preferred".equals(str3)) {
                        dumpState.setDump(4096);
                    } else if ("preferred-xml".equals(str3)) {
                        dumpState.setDump(8192);
                        if (i5 < strArr.length && "--full".equals(strArr[i5])) {
                            dumpState.setFullPreferred(true);
                        }
                    } else if ("d".equals(str3) || "domain-preferred-apps".equals(str3)) {
                        dumpState.setDump(DumpState.DUMP_DOMAIN_PREFERRED);
                    } else if ("p".equals(str3) || "packages".equals(str3)) {
                        dumpState.setDump(128);
                    } else if ("q".equals(str3) || ParsingPackageUtils.TAG_QUERIES.equals(str3)) {
                        dumpState.setDump(67108864);
                    } else if ("s".equals(str3) || "shared-users".equals(str3)) {
                        dumpState.setDump(256);
                        if (i5 < strArr.length && "noperm".equals(strArr[i5])) {
                            dumpState.setOptionEnabled(4);
                        }
                    } else if ("prov".equals(str3) || "providers".equals(str3)) {
                        dumpState.setDump(1024);
                    } else if ("m".equals(str3) || "messages".equals(str3)) {
                        dumpState.setDump(512);
                    } else if ("v".equals(str3) || "verifiers".equals(str3)) {
                        dumpState.setDump(2048);
                    } else if ("dv".equals(str3) || "domain-verifier".equals(str3)) {
                        dumpState.setDump(131072);
                    } else if ("version".equals(str3)) {
                        dumpState.setDump(32768);
                    } else if ("k".equals(str3) || "keysets".equals(str3)) {
                        dumpState.setDump(16384);
                    } else if ("installs".equals(str3)) {
                        dumpState.setDump(65536);
                    } else if ("frozen".equals(str3)) {
                        dumpState.setDump(524288);
                    } else if ("volumes".equals(str3)) {
                        dumpState.setDump(8388608);
                    } else if ("dexopt".equals(str3)) {
                        dumpState.setDump(1048576);
                    } else if ("compiler-stats".equals(str3)) {
                        dumpState.setDump(DumpState.DUMP_COMPILER_STATS);
                    } else if ("changes".equals(str3)) {
                        dumpState.setDump(DumpState.DUMP_CHANGES);
                    } else if ("service-permissions".equals(str3)) {
                        dumpState.setDump(DumpState.DUMP_SERVICE_PERMISSIONS);
                    } else if ("known-packages".equals(str3)) {
                        dumpState.setDump(DumpState.DUMP_KNOWN_PACKAGES);
                    } else if ("t".equals(str3) || "timeouts".equals(str3)) {
                        dumpState.setDump(268435456);
                    } else if ("snapshot".equals(str3)) {
                        dumpState.setDump(536870912);
                        if (i5 < strArr.length) {
                            if ("--full".equals(strArr[i5])) {
                                dumpState.setBrief(false);
                            } else if ("--brief".equals(strArr[i5])) {
                                dumpState.setBrief(true);
                            }
                        }
                    } else if ("protected-broadcasts".equals(str3)) {
                        dumpState.setDump(1073741824);
                    } else if (this.mDumpHelperExt.customLogicInDump(str3, printWriter, strArr, i5)) {
                        return;
                    }
                }
            }
        }
        arraySet = null;
        targetPackageName = dumpState.getTargetPackageName();
        isCheckIn = dumpState.isCheckIn();
        if (targetPackageName == null) {
        }
        if (isCheckIn) {
        }
        if (!isCheckIn) {
            computer.dump(32768, fileDescriptor, printWriter, dumpState);
        }
        if (!isCheckIn) {
            if (dumpState.onTitlePrinted()) {
            }
            IndentingPrintWriter indentingPrintWriter2 = new IndentingPrintWriter(printWriter, "  ", 120);
            indentingPrintWriter2.println("Known Packages:");
            indentingPrintWriter2.increaseIndent();
            while (i3 <= 19) {
            }
            indentingPrintWriter2.decreaseIndent();
        }
        if (dumpState.isDumping(2048)) {
            if (!isCheckIn) {
                if (dumpState.onTitlePrinted()) {
                }
                printWriter.println("Verifiers:");
            }
            String[] strArr32 = this.mRequiredVerifierPackages;
            length = strArr32.length;
            i2 = 0;
            while (i2 < length) {
            }
        }
        if (!dumpState.isDumping(131072)) {
        }
        if (dumpState.isDumping(1)) {
            computer.dump(1, fileDescriptor, printWriter, dumpState);
        }
        if (dumpState.isDumping(2)) {
            if (dumpState.onTitlePrinted()) {
            }
            if (!isCheckIn) {
            }
            while (r1.hasNext()) {
            }
        }
        componentResolver = computer.getComponentResolver();
        if (!isCheckIn) {
            componentResolver.dumpActivityResolvers(printWriter, dumpState, targetPackageName);
        }
        if (!isCheckIn) {
            componentResolver.dumpReceiverResolvers(printWriter, dumpState, targetPackageName);
        }
        if (!isCheckIn) {
            componentResolver.dumpServiceResolvers(printWriter, dumpState, targetPackageName);
        }
        if (!isCheckIn) {
            componentResolver.dumpProviderResolvers(printWriter, dumpState, targetPackageName);
        }
        if (!isCheckIn) {
            computer.dump(4096, fileDescriptor, printWriter, dumpState);
        }
        if (!isCheckIn) {
            computer.dump(8192, fileDescriptor, printWriter, dumpState);
        }
        if (!isCheckIn) {
            computer.dump(DumpState.DUMP_DOMAIN_PREFERRED, fileDescriptor, printWriter, dumpState);
        }
        if (!isCheckIn) {
            computer.dumpPermissions(printWriter, targetPackageName, arraySet, dumpState);
        }
        if (!isCheckIn) {
            componentResolver.dumpContentProviders(computer, printWriter, dumpState, targetPackageName);
        }
        if (!isCheckIn) {
            computer.dumpKeySet(printWriter, targetPackageName, dumpState);
        }
        if (dumpState.isDumping(128)) {
        }
        if (!isCheckIn) {
            computer.dump(67108864, fileDescriptor, printWriter, dumpState);
        }
        if (dumpState.isDumping(256)) {
        }
        if (!isCheckIn) {
            if (dumpState.onTitlePrinted()) {
            }
            printWriter.println("Package Changes:");
            this.mChangedPackagesTracker.iterateAll(new BiConsumer() { // from class: com.android.server.pm.DumpHelper$$ExternalSyntheticLambda0
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    DumpHelper.lambda$doDump$0(printWriter, (Integer) obj, (SparseArray) obj2);
                }
            });
        }
        if (!isCheckIn) {
            computer.dump(i, fileDescriptor, printWriter, dumpState);
        }
        if (!isCheckIn) {
            this.mStorageEventHelper.dumpLoadedVolumes(printWriter, dumpState);
        }
        if (!isCheckIn) {
            componentResolver.dumpServicePermissions(printWriter, dumpState);
        }
        if (!isCheckIn) {
            computer.dump(1048576, fileDescriptor, printWriter, dumpState);
        }
        if (!isCheckIn) {
            computer.dump(DumpState.DUMP_COMPILER_STATS, fileDescriptor, printWriter, dumpState);
        }
        if (dumpState.isDumping(512)) {
            if (isCheckIn) {
            }
        }
        if (!isCheckIn) {
            if (dumpState.onTitlePrinted()) {
            }
            this.mInstallerService.dump(new IndentingPrintWriter(printWriter, "  ", 120));
        }
        if (!isCheckIn) {
            computer.dump(DumpState.DUMP_APEX, fileDescriptor, printWriter, dumpState);
        }
        if (!isCheckIn) {
            if (dumpState.onTitlePrinted()) {
            }
            printWriter.println("Per UID read timeouts:");
            printWriter.println("    Default timeouts flag: " + PackageManagerService.getDefaultTimeouts());
            printWriter.println("    Known digesters list flag: " + PackageManagerService.getKnownDigestersList());
            printWriter.println("    Timeouts (" + this.mPerUidReadTimeouts.length + "):");
            while (r3 < r2) {
            }
        }
        if (!isCheckIn) {
            if (dumpState.onTitlePrinted()) {
            }
            printWriter.println("Snapshot statistics:");
            this.mSnapshotStatistics.dump(printWriter, "   ", SystemClock.currentTimeMicro(), computer.getUsed(), dumpState.isBrief());
        }
        if (isCheckIn) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$doDump$0(PrintWriter printWriter, Integer num, SparseArray sparseArray) {
        printWriter.print("  Sequence number=");
        printWriter.println(num);
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            SparseArray sparseArray2 = (SparseArray) sparseArray.valueAt(i);
            printWriter.print("  User ");
            printWriter.print(sparseArray.keyAt(i));
            printWriter.println(":");
            int size2 = sparseArray2.size();
            if (size2 == 0) {
                printWriter.print("    ");
                printWriter.println("No packages changed");
            } else {
                for (int i2 = 0; i2 < size2; i2++) {
                    String str = (String) sparseArray2.valueAt(i2);
                    int keyAt = sparseArray2.keyAt(i2);
                    printWriter.print("    ");
                    printWriter.print("seq=");
                    printWriter.print(keyAt);
                    printWriter.print(", package=");
                    printWriter.println(str);
                }
            }
        }
    }

    private void printHelp(PrintWriter printWriter) {
        printWriter.println("Package manager dump options:");
        printWriter.println("  [-h] [-f] [--checkin] [--all-components] [cmd] ...");
        printWriter.println("    --checkin: dump for a checkin");
        printWriter.println("    -f: print details of intent filters");
        printWriter.println("    -h: print this help");
        printWriter.println("    --all-components: include all component names in package dump");
        printWriter.println("  cmd may be one of:");
        printWriter.println("    apex: list active APEXes and APEX session state");
        printWriter.println("    l[ibraries]: list known shared libraries");
        printWriter.println("    f[eatures]: list device features");
        printWriter.println("    k[eysets]: print known keysets");
        printWriter.println("    r[esolvers] [activity|service|receiver|content]: dump intent resolvers");
        printWriter.println("    perm[issions]: dump permissions");
        printWriter.println("    permission [name ...]: dump declaration and use of given permission");
        printWriter.println("    pref[erred]: print preferred package settings");
        printWriter.println("    preferred-xml [--full]: print preferred package settings as xml");
        printWriter.println("    prov[iders]: dump content providers");
        printWriter.println("    p[ackages]: dump installed packages");
        printWriter.println("    q[ueries]: dump app queryability calculations");
        printWriter.println("    s[hared-users]: dump shared user IDs");
        printWriter.println("    m[essages]: print collected runtime messages");
        printWriter.println("    v[erifiers]: print package verifier info");
        printWriter.println("    d[omain-preferred-apps]: print domains preferred apps");
        printWriter.println("    i[ntent-filter-verifiers]|ifv: print intent filter verifier info");
        printWriter.println("    t[imeouts]: print read timeouts for known digesters");
        printWriter.println("    version: print database version info");
        printWriter.println("    write: write current settings now");
        printWriter.println("    installs: details about install sessions");
        printWriter.println("    check-permission <permission> <package> [<user>]: does pkg hold perm?");
        printWriter.println("    dexopt: dump dexopt state");
        printWriter.println("    compiler-stats: dump compiler statistics");
        printWriter.println("    service-permissions: dump permissions required by services");
        printWriter.println("    snapshot: dump snapshot statistics");
        printWriter.println("    protected-broadcasts: print list of protected broadcast actions");
        printWriter.println("    known-packages: dump known packages");
        printWriter.println("    <package.name>: info about given package");
    }

    private void dumpProto(Computer computer, FileDescriptor fileDescriptor) {
        ProtoOutputStream protoOutputStream = new ProtoOutputStream(fileDescriptor);
        for (String str : this.mRequiredVerifierPackages) {
            long start = protoOutputStream.start(1146756268033L);
            protoOutputStream.write(1138166333441L, str);
            protoOutputStream.write(1120986464258L, computer.getPackageUid(str, 268435456L, 0));
            protoOutputStream.end(start);
        }
        ComponentName componentName = this.mDomainVerificationManager.getProxy().getComponentName();
        if (componentName != null) {
            String packageName = componentName.getPackageName();
            long start2 = protoOutputStream.start(1146756268034L);
            protoOutputStream.write(1138166333441L, packageName);
            protoOutputStream.write(1120986464258L, computer.getPackageUid(packageName, 268435456L, 0));
            protoOutputStream.end(start2);
        }
        computer.dumpSharedLibrariesProto(protoOutputStream);
        dumpAvailableFeaturesProto(protoOutputStream);
        computer.dumpPackagesProto(protoOutputStream);
        computer.dumpSharedUsersProto(protoOutputStream);
        PackageManagerServiceUtils.dumpCriticalInfo(protoOutputStream);
        protoOutputStream.flush();
    }

    private void dumpAvailableFeaturesProto(ProtoOutputStream protoOutputStream) {
        int size = this.mAvailableFeatures.size();
        for (int i = 0; i < size; i++) {
            this.mAvailableFeatures.valueAt(i).dumpDebug(protoOutputStream, 2246267895812L);
        }
    }
}
