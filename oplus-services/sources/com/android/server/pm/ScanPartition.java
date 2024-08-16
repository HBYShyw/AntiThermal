package com.android.server.pm;

import android.content.pm.PackagePartitions;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.pm.ApexManager;
import java.io.File;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ScanPartition extends PackagePartitions.SystemPartition {
    public final ApexManager.ActiveApexInfo apexInfo;
    public final int scanFlag;

    public ScanPartition(PackagePartitions.SystemPartition systemPartition) {
        super(systemPartition);
        this.scanFlag = scanFlagForPartition(systemPartition);
        this.apexInfo = null;
    }

    public ScanPartition(File file, ScanPartition scanPartition, ApexManager.ActiveApexInfo activeApexInfo) {
        super(file, scanPartition);
        int i = scanPartition.scanFlag;
        this.apexInfo = activeApexInfo;
        if (activeApexInfo != null) {
            i |= 8388608;
            i = activeApexInfo.isFactory ? i | DumpState.DUMP_APEX : i;
            if (activeApexInfo.activeApexChanged) {
                i |= DumpState.DUMP_SERVICE_PERMISSIONS;
            }
        }
        this.scanFlag = i;
    }

    private static int scanFlagForPartition(PackagePartitions.SystemPartition systemPartition) {
        int i = systemPartition.type;
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 524288;
        }
        if (i == 2) {
            return DumpState.DUMP_CHANGES;
        }
        if (i == 3) {
            return DumpState.DUMP_DOMAIN_PREFERRED;
        }
        if (i == 4) {
            return 1048576;
        }
        if (i == 5) {
            return DumpState.DUMP_COMPILER_STATS;
        }
        throw new IllegalStateException("Unable to determine scan flag for " + systemPartition.getFolder());
    }

    public String toString() {
        return getFolder().getAbsolutePath() + ":" + this.scanFlag;
    }
}
