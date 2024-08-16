package com.android.server.am;

import android.util.ArraySet;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AppBindRecord {
    final ProcessRecord attributedClient;
    final ProcessRecord client;
    final ArraySet<ConnectionRecord> connections = new ArraySet<>();
    final IntentBindRecord intent;
    final ServiceRecord service;

    void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "service=" + this.service);
        printWriter.println(str + "client=" + this.client);
        printWriter.println(str + "attributedClient=" + this.attributedClient);
        dumpInIntentBind(printWriter, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpInIntentBind(PrintWriter printWriter, String str) {
        int size = this.connections.size();
        if (size > 0) {
            printWriter.println(str + "Per-process Connections:");
            for (int i = 0; i < size; i++) {
                printWriter.println(str + "  " + this.connections.valueAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppBindRecord(ServiceRecord serviceRecord, IntentBindRecord intentBindRecord, ProcessRecord processRecord, ProcessRecord processRecord2) {
        this.service = serviceRecord;
        this.intent = intentBindRecord;
        this.client = processRecord;
        this.attributedClient = processRecord2;
    }

    public String toString() {
        return "AppBindRecord{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.service.shortInstanceName + ":" + this.client.processName + "}";
    }

    void logOutIntentBindWithTypeInfo() {
        int size = this.connections.size();
        Slog.d("AppBindRecord", "size:" + size);
        if (size > 0) {
            Slog.d("AppBindRecord", "Per-process Connections:");
            for (int i = 0; i < size; i++) {
                ConnectionRecord valueAt = this.connections.valueAt(i);
                if (valueAt == null) {
                    Slog.d("AppBindRecord", "Connections null at: " + i);
                } else if (valueAt instanceof ConnectionRecord) {
                    Slog.d("AppBindRecord", "Connections at: " + i + " = " + valueAt);
                } else {
                    Slog.d("AppBindRecord", "Connections at: " + i + " is not ConnectionRecord. " + valueAt);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1138166333441L, this.service.shortInstanceName);
        protoOutputStream.write(1138166333442L, this.client.processName);
        int size = this.connections.size();
        for (int i = 0; i < size; i++) {
            protoOutputStream.write(2237677961219L, Integer.toHexString(System.identityHashCode(this.connections.valueAt(i))));
        }
        protoOutputStream.end(start);
    }
}
