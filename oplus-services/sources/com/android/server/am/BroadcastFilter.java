package com.android.server.am;

import android.content.IntentFilter;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.proto.ProtoOutputStream;
import dalvik.annotation.optimization.NeverCompile;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class BroadcastFilter extends IntentFilter {
    final boolean exported;
    final String featureId;
    final boolean instantApp;
    final int owningUid;
    final int owningUserId;
    final String packageName;
    final String receiverId;
    final ReceiverList receiverList;
    final String requiredPermission;
    final boolean visibleToInstantApp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastFilter(IntentFilter intentFilter, ReceiverList receiverList, String str, String str2, String str3, String str4, int i, int i2, boolean z, boolean z2, boolean z3) {
        super(intentFilter);
        this.receiverList = receiverList;
        this.packageName = str;
        this.featureId = str2;
        this.receiverId = str3;
        this.requiredPermission = str4;
        this.owningUid = i;
        this.owningUserId = i2;
        this.instantApp = z;
        this.visibleToInstantApp = z2;
        this.exported = z3;
    }

    public String getReceiverClassName() {
        int lastIndexOf;
        String str = this.receiverId;
        if (str == null || (lastIndexOf = str.lastIndexOf(64)) <= 0) {
            return null;
        }
        return this.receiverId.substring(0, lastIndexOf);
    }

    @NeverCompile
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        super.dumpDebug(protoOutputStream, 1146756268033L);
        String str = this.requiredPermission;
        if (str != null) {
            protoOutputStream.write(1138166333442L, str);
        }
        protoOutputStream.write(1138166333443L, Integer.toHexString(System.identityHashCode(this)));
        protoOutputStream.write(1120986464260L, this.owningUserId);
        protoOutputStream.end(start);
    }

    @NeverCompile
    public void dump(PrintWriter printWriter, String str) {
        dumpInReceiverList(printWriter, new PrintWriterPrinter(printWriter), str);
        this.receiverList.dumpLocal(printWriter, str);
    }

    @NeverCompile
    public void dumpBrief(PrintWriter printWriter, String str) {
        dumpBroadcastFilterState(printWriter, str);
    }

    @NeverCompile
    public void dumpInReceiverList(PrintWriter printWriter, Printer printer, String str) {
        super.dump(printer, str);
        dumpBroadcastFilterState(printWriter, str);
    }

    @NeverCompile
    void dumpBroadcastFilterState(PrintWriter printWriter, String str) {
        if (this.requiredPermission != null) {
            printWriter.print(str);
            printWriter.print("requiredPermission=");
            printWriter.println(this.requiredPermission);
        }
    }

    public String toString() {
        return "BroadcastFilter{" + Integer.toHexString(System.identityHashCode(this)) + ' ' + this.owningUid + "/u" + this.owningUserId + ' ' + this.receiverList + '}';
    }
}
