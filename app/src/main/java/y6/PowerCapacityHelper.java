package y6;

import android.content.Context;
import b6.LocalLog;
import f6.f;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import libcore.io.IoUtils;

/* compiled from: PowerCapacityHelper.java */
/* renamed from: y6.a, reason: use source file name */
/* loaded from: classes.dex */
public class PowerCapacityHelper {
    /* JADX WARN: Code restructure failed: missing block: B:29:0x005b, code lost:
    
        if (r5 == null) goto L24;
     */
    /* JADX WARN: Not initialized variable reg: 5, insn: 0x006a: MOVE (r0 I:??[OBJECT, ARRAY]) = (r5 I:??[OBJECT, ARRAY]), block:B:32:0x006a */
    /* JADX WARN: Removed duplicated region for block: B:34:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int a(Context context, int i10) {
        BufferedReader bufferedReader;
        Exception e10;
        AutoCloseable autoCloseable;
        if (!new File("/sys/class/power_supply/battery/batt_rm").exists()) {
            LocalLog.a("PowerCapacityHelper", "batt_rm is not exist, use battery capacity instead!");
            return (int) ((f.n(context) * i10) / 100.0d);
        }
        LocalLog.a("PowerCapacityHelper", "batt_rm is exist!");
        AutoCloseable autoCloseable2 = null;
        int i11 = -1;
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/sys/class/power_supply/battery/batt_rm"), "utf-8"));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        if (readLine.length() != 0) {
                            i11 = Integer.parseInt(readLine.trim());
                        }
                    } catch (Exception e11) {
                        e10 = e11;
                        e10.printStackTrace();
                    }
                }
            } catch (Exception e12) {
                bufferedReader = null;
                e10 = e12;
            } catch (Throwable th) {
                th = th;
                if (autoCloseable2 != null) {
                }
                throw th;
            }
            IoUtils.closeQuietly(bufferedReader);
            return i11 <= 0 ? (int) ((f.n(context) * i10) / 100.0d) : i11;
        } catch (Throwable th2) {
            th = th2;
            autoCloseable2 = autoCloseable;
            if (autoCloseable2 != null) {
                IoUtils.closeQuietly(autoCloseable2);
            }
            throw th;
        }
    }
}
