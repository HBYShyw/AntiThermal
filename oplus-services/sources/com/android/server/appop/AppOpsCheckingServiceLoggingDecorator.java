package com.android.server.appop;

import android.util.ArraySet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import java.io.PrintWriter;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppOpsCheckingServiceLoggingDecorator implements AppOpsCheckingServiceInterface {
    private static final String LOG_TAG = "AppOpsCheckingServiceLoggingDecorator";
    private final AppOpsCheckingServiceInterface mService;

    public AppOpsCheckingServiceLoggingDecorator(AppOpsCheckingServiceInterface appOpsCheckingServiceInterface) {
        this.mService = appOpsCheckingServiceInterface;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void writeState() {
        Log.i(LOG_TAG, "writeState()");
        this.mService.writeState();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void readState() {
        Log.i(LOG_TAG, "readState()");
        this.mService.readState();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void shutdown() {
        Log.i(LOG_TAG, "shutdown()");
        this.mService.shutdown();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void systemReady() {
        Log.i(LOG_TAG, "systemReady()");
        this.mService.systemReady();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseIntArray getNonDefaultUidModes(int i) {
        Log.i(LOG_TAG, "getNonDefaultUidModes(uid = " + i + ")");
        return this.mService.getNonDefaultUidModes(i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseIntArray getNonDefaultPackageModes(String str, int i) {
        Log.i(LOG_TAG, "getNonDefaultPackageModes(packageName = " + str + ", userId = " + i + ") ");
        return this.mService.getNonDefaultPackageModes(str, i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getUidMode(int i, int i2) {
        Log.i(LOG_TAG, "getUidMode(uid = " + i + ", op = " + i2 + ")");
        return this.mService.getUidMode(i, i2);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean setUidMode(int i, int i2, int i3) {
        Log.i(LOG_TAG, "setUidMode(uid = " + i + ", op = " + i2 + ", mode = " + i3 + ")");
        return this.mService.setUidMode(i, i2, i3);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getPackageMode(String str, int i, int i2) {
        Log.i(LOG_TAG, "getPackageMode(packageName = " + str + ", op = " + i + ", userId = " + i2 + ")");
        return this.mService.getPackageMode(str, i, i2);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void setPackageMode(String str, int i, int i2, int i3) {
        Log.i(LOG_TAG, "setPackageMode(packageName = " + str + ", op = " + i + ", mode = " + i2 + ", userId = " + i3 + ")");
        this.mService.setPackageMode(str, i, i2, i3);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removePackage(String str, int i) {
        Log.i(LOG_TAG, "removePackage(packageName = " + str + ", userId = " + i + ")");
        return this.mService.removePackage(str, i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeUid(int i) {
        Log.i(LOG_TAG, "removeUid(uid = " + i + ")");
        this.mService.removeUid(i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean areUidModesDefault(int i) {
        Log.i(LOG_TAG, "areUidModesDefault(uid = " + i + ")");
        return this.mService.areUidModesDefault(i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean arePackageModesDefault(String str, int i) {
        Log.i(LOG_TAG, "arePackageModesDefault(packageName = " + str + ", userId = " + i + ")");
        return this.mService.arePackageModesDefault(str, i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void clearAllModes() {
        Log.i(LOG_TAG, "clearAllModes()");
        this.mService.clearAllModes();
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void startWatchingOpModeChanged(OnOpModeChangedListener onOpModeChangedListener, int i) {
        Log.i(LOG_TAG, "startWatchingOpModeChanged(changedListener = " + onOpModeChangedListener + ", op = " + i + ")");
        this.mService.startWatchingOpModeChanged(onOpModeChangedListener, i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void startWatchingPackageModeChanged(OnOpModeChangedListener onOpModeChangedListener, String str) {
        Log.i(LOG_TAG, "startWatchingPackageModeChanged(changedListener = " + onOpModeChangedListener + ", packageName = " + str + ")");
        this.mService.startWatchingPackageModeChanged(onOpModeChangedListener, str);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeListener(OnOpModeChangedListener onOpModeChangedListener) {
        Log.i(LOG_TAG, "removeListener(changedListener = " + onOpModeChangedListener + ")");
        this.mService.removeListener(onOpModeChangedListener);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public ArraySet<OnOpModeChangedListener> getOpModeChangedListeners(int i) {
        Log.i(LOG_TAG, "getOpModeChangedListeners(op = " + i + ")");
        return this.mService.getOpModeChangedListeners(i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public ArraySet<OnOpModeChangedListener> getPackageModeChangedListeners(String str) {
        Log.i(LOG_TAG, "getPackageModeChangedListeners(packageName = " + str + ")");
        return this.mService.getPackageModeChangedListeners(str);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void notifyWatchersOfChange(int i, int i2) {
        Log.i(LOG_TAG, "notifyWatchersOfChange(op = " + i + ", uid = " + i2 + ")");
        this.mService.notifyWatchersOfChange(i, i2);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void notifyOpChanged(OnOpModeChangedListener onOpModeChangedListener, int i, int i2, String str) {
        Log.i(LOG_TAG, "notifyOpChanged(changedListener = " + onOpModeChangedListener + ", op = " + i + ", uid = " + i2 + ", packageName = " + str + ")");
        this.mService.notifyOpChanged(onOpModeChangedListener, i, i2, str);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void notifyOpChangedForAllPkgsInUid(int i, int i2, boolean z, OnOpModeChangedListener onOpModeChangedListener) {
        Log.i(LOG_TAG, "notifyOpChangedForAllPkgsInUid(op = " + i + ", uid = " + i2 + ", onlyForeground = " + z + ", callbackToIgnore = " + onOpModeChangedListener + ")");
        this.mService.notifyOpChangedForAllPkgsInUid(i, i2, z, onOpModeChangedListener);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseBooleanArray evalForegroundUidOps(int i, SparseBooleanArray sparseBooleanArray) {
        Log.i(LOG_TAG, "evalForegroundUidOps(uid = " + i + ", foregroundOps = " + sparseBooleanArray + ")");
        return this.mService.evalForegroundUidOps(i, sparseBooleanArray);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public SparseBooleanArray evalForegroundPackageOps(String str, SparseBooleanArray sparseBooleanArray, int i) {
        Log.i(LOG_TAG, "evalForegroundPackageOps(packageName = " + str + ", foregroundOps = " + sparseBooleanArray + ", userId = " + i + ")");
        return this.mService.evalForegroundPackageOps(str, sparseBooleanArray, i);
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean dumpListeners(int i, int i2, String str, PrintWriter printWriter) {
        Log.i(LOG_TAG, "dumpListeners(dumpOp = " + i + ", dumpUid = " + i2 + ", dumpPackage = " + str + ", printWriter = " + printWriter + ")");
        return this.mService.dumpListeners(i, i2, str, printWriter);
    }
}
