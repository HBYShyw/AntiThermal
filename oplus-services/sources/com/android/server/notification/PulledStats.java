package com.android.server.notification;

import android.os.ParcelFileDescriptor;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.notification.NotificationManagerService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PulledStats {
    static final String TAG = "PulledStats";
    private long mTimePeriodEndMs;
    private final long mTimePeriodStartMs;
    private List<String> mUndecoratedPackageNames = new ArrayList();

    public PulledStats(long j) {
        this.mTimePeriodStartMs = j;
        this.mTimePeriodEndMs = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParcelFileDescriptor toParcelFileDescriptor(final int i) throws IOException {
        final ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
        if (i == 1) {
            new Thread("NotificationManager pulled metric output") { // from class: com.android.server.notification.PulledStats.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(createPipe[1]);
                        ProtoOutputStream protoOutputStream = new ProtoOutputStream(autoCloseOutputStream);
                        PulledStats.this.writeToProto(i, protoOutputStream);
                        protoOutputStream.flush();
                        autoCloseOutputStream.close();
                    } catch (IOException e) {
                        Slog.w(PulledStats.TAG, "Failure writing pipe", e);
                    }
                }
            }.start();
        } else {
            Slog.w(TAG, "Unknown pulled stats request: " + i);
        }
        return createPipe[0];
    }

    public long endTimeMs() {
        return this.mTimePeriodEndMs;
    }

    public void dump(int i, PrintWriter printWriter, NotificationManagerService.DumpFilter dumpFilter) {
        if (i == 1) {
            printWriter.print("  Packages with undecordated notifications (");
            printWriter.print(this.mTimePeriodStartMs);
            printWriter.print(" - ");
            printWriter.print(this.mTimePeriodEndMs);
            printWriter.println("):");
            if (this.mUndecoratedPackageNames.size() == 0) {
                printWriter.println("    none");
                return;
            }
            for (String str : this.mUndecoratedPackageNames) {
                if (!dumpFilter.filtered || str.equals(dumpFilter.pkgFilter)) {
                    printWriter.println("    " + str);
                }
            }
            return;
        }
        printWriter.println("Unknown pulled stats request: " + i);
    }

    @VisibleForTesting
    void writeToProto(int i, ProtoOutputStream protoOutputStream) {
        if (i == 1) {
            for (String str : this.mUndecoratedPackageNames) {
                long start = protoOutputStream.start(2246267895809L);
                protoOutputStream.write(1138166333441L, str);
                protoOutputStream.end(start);
            }
            return;
        }
        Slog.w(TAG, "Unknown pulled stats request: " + i);
    }

    public void addUndecoratedPackage(String str, long j) {
        this.mUndecoratedPackageNames.add(str);
        this.mTimePeriodEndMs = Math.max(this.mTimePeriodEndMs, j);
    }
}
