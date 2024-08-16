package com.android.server.appop;

import android.os.Trace;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppOpsCheckingServiceTracingDecorator implements AppOpsCheckingServiceInterface {
    private static final long TRACE_TAG = 64;
    private final AppOpsCheckingServiceInterface mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppOpsCheckingServiceTracingDecorator(AppOpsCheckingServiceInterface appOpsCheckingServiceInterface) {
        this.mService = appOpsCheckingServiceInterface;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void writeState() {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#writeState");
        try {
            this.mService.writeState();
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void readState() {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#readState");
        try {
            this.mService.readState();
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void shutdown() {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#shutdown");
        try {
            this.mService.shutdown();
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void systemReady() {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#systemReady");
        try {
            this.mService.systemReady();
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseIntArray getNonDefaultUidModes(int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getNonDefaultUidModes");
        try {
            return this.mService.getNonDefaultUidModes(i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseIntArray getNonDefaultPackageModes(String str, int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getNonDefaultPackageModes");
        try {
            return this.mService.getNonDefaultPackageModes(str, i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getUidMode(int i, int i2) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getUidMode");
        try {
            return this.mService.getUidMode(i, i2);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean setUidMode(int i, int i2, int i3) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#setUidMode");
        try {
            return this.mService.setUidMode(i, i2, i3);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getPackageMode(String str, int i, int i2) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getPackageMode");
        try {
            return this.mService.getPackageMode(str, i, i2);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void setPackageMode(String str, int i, int i2, int i3) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#setPackageMode");
        try {
            this.mService.setPackageMode(str, i, i2, i3);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removePackage(String str, int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#removePackage");
        try {
            return this.mService.removePackage(str, i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeUid(int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#removeUid");
        try {
            this.mService.removeUid(i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean areUidModesDefault(int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#areUidModesDefault");
        try {
            return this.mService.areUidModesDefault(i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean arePackageModesDefault(String str, int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#arePackageModesDefault");
        try {
            return this.mService.arePackageModesDefault(str, i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void clearAllModes() {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#clearAllModes");
        try {
            this.mService.clearAllModes();
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void startWatchingOpModeChanged(OnOpModeChangedListener onOpModeChangedListener, int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#startWatchingOpModeChanged");
        try {
            this.mService.startWatchingOpModeChanged(onOpModeChangedListener, i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void startWatchingPackageModeChanged(OnOpModeChangedListener onOpModeChangedListener, String str) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#startWatchingPackageModeChanged");
        try {
            this.mService.startWatchingPackageModeChanged(onOpModeChangedListener, str);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeListener(OnOpModeChangedListener onOpModeChangedListener) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#removeListener");
        try {
            this.mService.removeListener(onOpModeChangedListener);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public ArraySet<OnOpModeChangedListener> getOpModeChangedListeners(int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getOpModeChangedListeners");
        try {
            return this.mService.getOpModeChangedListeners(i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public ArraySet<OnOpModeChangedListener> getPackageModeChangedListeners(String str) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getPackageModeChangedListeners");
        try {
            return this.mService.getPackageModeChangedListeners(str);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void notifyWatchersOfChange(int i, int i2) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#notifyWatchersOfChange");
        try {
            this.mService.notifyWatchersOfChange(i, i2);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void notifyOpChanged(OnOpModeChangedListener onOpModeChangedListener, int i, int i2, String str) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#notifyOpChanged");
        try {
            this.mService.notifyOpChanged(onOpModeChangedListener, i, i2, str);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void notifyOpChangedForAllPkgsInUid(int i, int i2, boolean z, OnOpModeChangedListener onOpModeChangedListener) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#notifyOpChangedForAllPkgsInUid");
        try {
            this.mService.notifyOpChangedForAllPkgsInUid(i, i2, z, onOpModeChangedListener);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseBooleanArray evalForegroundUidOps(int i, SparseBooleanArray sparseBooleanArray) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#evalForegroundUidOps");
        try {
            return this.mService.evalForegroundUidOps(i, sparseBooleanArray);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseBooleanArray evalForegroundPackageOps(String str, SparseBooleanArray sparseBooleanArray, int i) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#evalForegroundPackageOps");
        try {
            return this.mService.evalForegroundPackageOps(str, sparseBooleanArray, i);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean dumpListeners(int i, int i2, String str, PrintWriter printWriter) {
        Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#dumpListeners");
        try {
            return this.mService.dumpListeners(i, i2, str, printWriter);
        } finally {
            Trace.traceEnd(TRACE_TAG);
        }
    }
}
