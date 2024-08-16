package ha;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import b6.LocalLog;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.util.OplusUnitConversionUtils;
import java.util.ArrayList;
import java.util.Formatter;

/* compiled from: StorageUtils.java */
/* renamed from: ha.d, reason: use source file name */
/* loaded from: classes2.dex */
public class StorageUtils {

    /* renamed from: a, reason: collision with root package name */
    private static StatFs f12082a = null;

    /* renamed from: b, reason: collision with root package name */
    private static long f12083b = 0;

    /* renamed from: c, reason: collision with root package name */
    private static long f12084c = 0;

    /* renamed from: d, reason: collision with root package name */
    private static long f12085d = 0;

    /* renamed from: e, reason: collision with root package name */
    private static long f12086e = 0;

    /* renamed from: f, reason: collision with root package name */
    private static boolean f12087f = false;

    /* renamed from: g, reason: collision with root package name */
    private static long f12088g = 0;

    /* renamed from: h, reason: collision with root package name */
    private static long f12089h = 0;

    /* renamed from: i, reason: collision with root package name */
    private static long f12090i = 0;

    /* renamed from: j, reason: collision with root package name */
    private static double f12091j = 1.5d;

    /* JADX WARN: Removed duplicated region for block: B:10:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String a(long j10, Context context) {
        String str;
        if (context != null) {
            try {
                str = new OplusUnitConversionUtils(context).getUnitValue(j10);
            } catch (Exception e10) {
                LocalLog.b("StorageUtils", "byteCountToDisplaySize e:" + e10);
            }
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
            return "\u202d" + str + "\u202c";
        }
        str = "";
        if (!TextUtils.isEmpty(str)) {
        }
    }

    public static String b(long j10) {
        StringBuilder sb2 = new StringBuilder(32);
        Formatter formatter = new Formatter(sb2);
        sb2.setLength(0);
        if (j10 < 1024) {
            return j10 + "B";
        }
        if (j10 < 1048576) {
            formatter.format("%.2fKB", Double.valueOf(j10 / 1024.0d));
            return sb2.toString();
        }
        if (j10 < 1073741824) {
            formatter.format("%.2fMB", Double.valueOf(j10 / 1048576.0d));
            return sb2.toString();
        }
        formatter.format("%.2fGB", Double.valueOf(j10 / 1.073741824E9d));
        return sb2.toString();
    }

    public static long c() {
        return f12083b;
    }

    public static long d() {
        long j10;
        StatFs statFs = f12082a;
        if (statFs == null) {
            LocalLog.a("StorageUtils", "getDataFreeSpace: fileStatsData is null!!!");
            long j11 = f12084c + 629145600;
            f12086e = j11;
            return j11;
        }
        try {
            statFs.restat(Environment.getDataDirectory().getAbsolutePath());
            j10 = f12082a.getAvailableBlocksLong() * f12082a.getBlockSizeLong();
        } catch (IllegalArgumentException unused) {
            LocalLog.a("StorageUtils", "getDataFreeSpace: IllegalArgumentException.");
            j10 = -1;
        }
        LocalLog.a("StorageUtils", "Data Free Space = " + b(j10));
        f12086e = j10;
        return j10;
    }

    public static long e() {
        return f12085d;
    }

    public static long f() {
        return f12084c;
    }

    public static void g(Context context) {
        h();
        ArrayList<Long> e10 = StorageConfigManager.e(context);
        if (e10 != null && e10.size() == 3) {
            if (n(e10.get(0), e10.get(1))) {
                f12084c = e10.get(0).longValue() * 1048576;
                f12085d = e10.get(1).longValue() * 1048576;
                f12083b = e10.get(2).longValue() * 1073741824;
                LocalLog.d("StorageUtils", "use config data");
            } else {
                f12084c = f12089h;
                f12085d = f12090i;
                f12083b = f12088g;
                LocalLog.d("StorageUtils", "config data is invalid use default data");
            }
        } else {
            f12084c = f12089h;
            f12085d = f12090i;
            f12083b = f12088g;
            LocalLog.d("StorageUtils", "rus config not exists or parse error use StorageUtils default config");
        }
        LocalLog.a("StorageUtils", "DataLowThreshold = " + b(f12084c));
        LocalLog.a("StorageUtils", "DataFullThreshold = " + b(f12085d));
        LocalLog.a("StorageUtils", "TotalData = " + b(f12083b));
    }

    public static void h() {
        if (f12082a != null) {
            return;
        }
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        f12082a = statFs;
        long blockCountLong = statFs.getBlockCountLong() * f12082a.getBlockSizeLong();
        if (blockCountLong > 274877906944L) {
            f12089h = 7340032000L;
            f12090i = 1048576000L;
            f12088g = 549755813888L;
        } else if (blockCountLong > 137438953472L) {
            f12089h = 7340032000L;
            f12090i = 1048576000L;
            f12088g = 274877906944L;
        } else if (blockCountLong > 68719476736L) {
            f12089h = 7340032000L;
            f12090i = 1048576000L;
            f12088g = 137438953472L;
        } else if (blockCountLong > 34359738368L) {
            f12089h = 4718592000L;
            f12090i = 1048576000L;
            f12088g = 68719476736L;
        } else if (blockCountLong > 17179869184L) {
            f12089h = 3932160000L;
            f12090i = 1048576000L;
            f12088g = 34359738368L;
        } else {
            f12089h = 1310720000L;
            f12090i = 838860800L;
            f12088g = 17179869184L;
        }
        LocalLog.a("StorageUtils", "DataLowThreshold = " + b(f12084c));
        LocalLog.a("StorageUtils", "DataFullThreshold = " + b(f12085d));
        LocalLog.a("StorageUtils", "TotalData = " + b(f12083b));
    }

    public static boolean i() {
        return f12087f;
    }

    public static long j() {
        if (m()) {
            return 17179869184L;
        }
        return f12083b;
    }

    public static long k() {
        return f12090i;
    }

    public static long l() {
        return f12089h;
    }

    public static boolean m() {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.battery.show_confused_hardwareinfo");
    }

    public static boolean n(Long l10, Long l11) {
        h();
        return l10.longValue() * 1048576 >= l() && ((double) (l10.longValue() * 1048576)) <= f12091j * ((double) l()) && l11.longValue() * 1048576 >= k() && ((double) (l11.longValue() * 1048576)) <= f12091j * ((double) k());
    }

    public static void o(boolean z10) {
        f12087f = z10;
    }
}
