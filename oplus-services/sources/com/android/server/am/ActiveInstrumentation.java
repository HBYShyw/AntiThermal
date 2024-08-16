package com.android.server.am;

import android.app.IInstrumentationWatcher;
import android.app.IUiAutomationConnection;
import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.PrintWriterPrinter;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class ActiveInstrumentation {
    Bundle mArguments;
    ComponentName mClass;
    Bundle mCurResults;
    boolean mFinished;
    boolean mHasBackgroundActivityStartsPermission;
    boolean mHasBackgroundForegroundServiceStartsPermission;
    boolean mNoRestart;
    String mProfileFile;
    ComponentName mResultClass;
    final ArrayList<ProcessRecord> mRunningProcesses = new ArrayList<>();
    final ActivityManagerService mService;
    int mSourceUid;
    ApplicationInfo mTargetInfo;
    String[] mTargetProcesses;
    IUiAutomationConnection mUiAutomationConnection;
    IInstrumentationWatcher mWatcher;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveInstrumentation(ActivityManagerService activityManagerService) {
        this.mService = activityManagerService;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeProcess(ProcessRecord processRecord) {
        this.mFinished = true;
        this.mRunningProcesses.remove(processRecord);
        if (this.mRunningProcesses.size() == 0) {
            this.mService.mActiveInstrumentation.remove(this);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("ActiveInstrumentation{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.mClass.toShortString());
        if (this.mFinished) {
            sb.append(" FINISHED");
        }
        sb.append(" ");
        sb.append(this.mRunningProcesses.size());
        sb.append(" procs");
        sb.append('}');
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("mClass=");
        printWriter.print(this.mClass);
        printWriter.print(" mFinished=");
        printWriter.println(this.mFinished);
        printWriter.print(str);
        printWriter.println("mRunningProcesses:");
        for (int i = 0; i < this.mRunningProcesses.size(); i++) {
            printWriter.print(str);
            printWriter.print("  #");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.println(this.mRunningProcesses.get(i));
        }
        printWriter.print(str);
        printWriter.print("mTargetProcesses=");
        printWriter.println(Arrays.toString(this.mTargetProcesses));
        printWriter.print(str);
        printWriter.print("mTargetInfo=");
        printWriter.println(this.mTargetInfo);
        ApplicationInfo applicationInfo = this.mTargetInfo;
        if (applicationInfo != null) {
            applicationInfo.dump(new PrintWriterPrinter(printWriter), str + "  ", 0);
        }
        if (this.mProfileFile != null) {
            printWriter.print(str);
            printWriter.print("mProfileFile=");
            printWriter.println(this.mProfileFile);
        }
        if (this.mWatcher != null) {
            printWriter.print(str);
            printWriter.print("mWatcher=");
            printWriter.println(this.mWatcher);
        }
        if (this.mUiAutomationConnection != null) {
            printWriter.print(str);
            printWriter.print("mUiAutomationConnection=");
            printWriter.println(this.mUiAutomationConnection);
        }
        printWriter.print("mHasBackgroundActivityStartsPermission=");
        printWriter.println(this.mHasBackgroundActivityStartsPermission);
        printWriter.print("mHasBackgroundForegroundServiceStartsPermission=");
        printWriter.println(this.mHasBackgroundForegroundServiceStartsPermission);
        printWriter.print(str);
        printWriter.print("mArguments=");
        printWriter.println(this.mArguments);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        this.mClass.dumpDebug(protoOutputStream, 1146756268033L);
        protoOutputStream.write(1133871366146L, this.mFinished);
        for (int i = 0; i < this.mRunningProcesses.size(); i++) {
            this.mRunningProcesses.get(i).dumpDebug(protoOutputStream, 2246267895811L);
        }
        for (String str : this.mTargetProcesses) {
            protoOutputStream.write(2237677961220L, str);
        }
        ApplicationInfo applicationInfo = this.mTargetInfo;
        if (applicationInfo != null) {
            applicationInfo.dumpDebug(protoOutputStream, 1146756268037L, 0);
        }
        protoOutputStream.write(1138166333446L, this.mProfileFile);
        protoOutputStream.write(1138166333447L, this.mWatcher.toString());
        protoOutputStream.write(1138166333448L, this.mUiAutomationConnection.toString());
        Bundle bundle = this.mArguments;
        if (bundle != null) {
            bundle.dumpDebug(protoOutputStream, 1146756268042L);
        }
        protoOutputStream.end(start);
    }
}
