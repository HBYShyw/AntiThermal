package com.oplus.oms.split.splitreport;

import com.oplus.oms.split.splitreport.model.SplitReporterInfo;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class SplitReporterManager {
    private static final AtomicReference<SplitReporter> sSplitReporterRef = new AtomicReference<>();
    private static final AtomicReference<SplitReporterInfo> sSplitReporterInfoRef = new AtomicReference<>();

    private SplitReporterManager() {
    }

    public static void install(SplitReporter splitReporter) {
        sSplitReporterRef.compareAndSet(null, splitReporter);
    }

    public static void install(SplitReporterInfo reporterInfo) {
        sSplitReporterInfoRef.compareAndSet(null, reporterInfo);
    }

    public static SplitReporter getSplitReporter() {
        return sSplitReporterRef.get();
    }

    public static SplitReporterInfo getSplitReporterInfo() {
        return sSplitReporterInfoRef.get();
    }
}
