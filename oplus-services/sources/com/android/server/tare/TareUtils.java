package com.android.server.tare;

import com.android.internal.annotations.VisibleForTesting;
import java.time.Clock;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class TareUtils {

    @VisibleForTesting
    static Clock sSystemClock = Clock.systemUTC();

    TareUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long getCurrentTimeMillis() {
        return sSystemClock.millis();
    }

    static int cakeToArc(long j) {
        return (int) (j / 1000000000);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String cakeToString(long j) {
        if (j == 0) {
            return "0 ARCs";
        }
        long j2 = j % 1000000000;
        long cakeToArc = cakeToArc(j);
        if (cakeToArc == 0) {
            if (j2 == 1) {
                return j2 + " cake";
            }
            return j2 + " cakes";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(cakeToArc);
        if (j2 != 0) {
            sb.append(".");
            sb.append(String.format("%03d", Long.valueOf(Math.abs(j2) / 1000000)));
        }
        sb.append(" ARC");
        if (cakeToArc != 1 || j2 != 0) {
            sb.append("s");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String appToString(int i, String str) {
        return "<" + i + ">" + str;
    }
}
