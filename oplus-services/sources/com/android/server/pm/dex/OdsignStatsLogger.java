package com.android.server.pm.dex;

import android.util.Slog;
import com.android.internal.art.ArtStatsLog;
import com.android.internal.os.BackgroundThread;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class OdsignStatsLogger {
    private static final String COMPOS_METRIC_NAME = "comp_os_artifacts_check_record";
    private static final String METRICS_FILE = "/data/misc/odsign/metrics/odsign-metrics.txt";
    private static final String ODSIGN_METRIC_NAME = "odsign_record";
    private static final String TAG = "OdsignStatsLogger";

    public static void triggerStatsWrite() {
        BackgroundThread.getExecutor().execute(new Runnable() { // from class: com.android.server.pm.dex.OdsignStatsLogger$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OdsignStatsLogger.writeStats();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00bd A[Catch: IOException -> 0x00fb, FileNotFoundException -> 0x0101, TryCatch #3 {FileNotFoundException -> 0x0101, IOException -> 0x00fb, blocks: (B:3:0x0006, B:5:0x0015, B:6:0x001a, B:8:0x0025, B:10:0x0033, B:13:0x0039, B:22:0x0067, B:25:0x0080, B:28:0x0085, B:31:0x009b, B:34:0x00a7, B:36:0x00bd, B:38:0x00c1, B:40:0x00d9, B:42:0x004a, B:45:0x0054, B:48:0x00f2), top: B:2:0x0006 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void writeStats() {
        char c;
        try {
            String readFileAsString = IoUtils.readFileAsString(METRICS_FILE);
            if (!new File(METRICS_FILE).delete()) {
                Slog.w(TAG, "Failed to delete metrics file");
            }
            for (String str : readFileAsString.split("\n")) {
                String[] split = str.split(" ");
                if (!str.isEmpty() && split.length >= 1) {
                    String str2 = split[0];
                    int hashCode = str2.hashCode();
                    if (hashCode != 890271774) {
                        if (hashCode == 1023928721 && str2.equals(COMPOS_METRIC_NAME)) {
                            c = 0;
                            if (c == 0) {
                                if (c != 1) {
                                    Slog.w(TAG, "Malformed metrics line '" + str + "'");
                                } else if (split.length != 2) {
                                    Slog.w(TAG, "Malformed odsign metrics line '" + str + "'");
                                } else {
                                    try {
                                        ArtStatsLog.write(548, Integer.parseInt(split[1]));
                                    } catch (NumberFormatException unused) {
                                        Slog.w(TAG, "Malformed odsign metrics line '" + str + "'");
                                    }
                                }
                            } else if (split.length != 4) {
                                Slog.w(TAG, "Malformed CompOS metrics line '" + str + "'");
                            } else {
                                ArtStatsLog.write(419, split[1].equals("1"), split[2].equals("1"), split[3].equals("1"));
                            }
                        }
                        c = 65535;
                        if (c == 0) {
                        }
                    } else {
                        if (str2.equals(ODSIGN_METRIC_NAME)) {
                            c = 1;
                            if (c == 0) {
                            }
                        }
                        c = 65535;
                        if (c == 0) {
                        }
                    }
                }
                Slog.w(TAG, "Empty metrics line");
            }
        } catch (FileNotFoundException unused2) {
        } catch (IOException e) {
            Slog.w(TAG, "Reading metrics file failed", e);
        }
    }
}
