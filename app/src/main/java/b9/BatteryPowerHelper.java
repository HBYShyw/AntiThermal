package b9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;
import android.util.Pair;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.userprofile.UserProfileConstants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import r8.LSLinkedHashMap;
import r8.StatusBean;
import x8.DataBaseUtil;

/* compiled from: BatteryPowerHelper.java */
/* renamed from: b9.a, reason: use source file name */
/* loaded from: classes2.dex */
public class BatteryPowerHelper {

    /* renamed from: a, reason: collision with root package name */
    public static final List<Long> f4596a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private static final Object f4597b = new Object();

    /* JADX WARN: Removed duplicated region for block: B:35:0x015e A[Catch: Exception -> 0x0162, all -> 0x0171, TRY_LEAVE, TryCatch #1 {Exception -> 0x0162, blocks: (B:7:0x0046, B:9:0x0054, B:13:0x005e, B:15:0x0064, B:18:0x00a3, B:22:0x00d0, B:25:0x0104, B:27:0x0125, B:29:0x0135, B:35:0x015e, B:42:0x0155), top: B:6:0x0046, outer: #0 }] */
    @SuppressLint({"Range"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static int a(Context context, HashMap<String, StatusBean> hashMap) {
        long j10 = 0;
        LocalLog.l("BatteryPowerHelper", "BATTERY_DATE_CHANGE_TIME:" + Settings.System.getLong(context.getContentResolver(), "OPLUS_BATTERY_DATE_CHANGE_TIME", 0L));
        int i10 = 1;
        int i11 = 0;
        String[] strArr = {String.valueOf(System.currentTimeMillis() - 108000000)};
        StringBuilder sb2 = new StringBuilder();
        synchronized (f4597b) {
            f4596a.clear();
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(DataBaseUtil.f19650b, null, "saved_time_millis>=? group by (saved_time)", strArr, null);
            } catch (Exception e10) {
                if (cursor != null) {
                    cursor.close();
                }
                e10.printStackTrace();
            }
            if (cursor != null && cursor.getCount() != 0) {
                long j11 = 0;
                int i12 = 0;
                while (cursor.moveToNext()) {
                    StringBuilder replace = sb2.replace(i11, sb2.length(), cursor.getString(cursor.getColumnIndex("saved_time")));
                    LocalLog.l("BatteryPowerHelper", "getBatteryLevelStats:" + ((Object) replace));
                    String str = Integer.parseInt(replace.substring(replace.length() + (-2))) >= 30 ? "30" : "00";
                    int i13 = cursor.getInt(cursor.getColumnIndex("battery_level"));
                    sb2 = replace.replace(replace.length() - 2, replace.length(), str);
                    long g6 = g(sb2.toString());
                    long j12 = g6 - j11;
                    if (j12 > 1800000 && j11 != j10) {
                        LocalLog.l("BatteryPowerHelper", "devices power off or change time:" + j11 + " " + g6);
                        long j13 = (j12 / 1800000) - 1;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        int i14 = i10;
                        while (j13 > j10) {
                            long j14 = (1800000 * i14) + j11;
                            j13--;
                            i14 += i10;
                            hashMap.put(simpleDateFormat.format(new Date(j14)), new StatusBean(i12, -1, -1.0f));
                            i10 = 1;
                            j10 = 0;
                        }
                        List<Long> list = f4596a;
                        list.add(Long.valueOf(j11));
                        list.add(Long.valueOf(g6));
                    }
                    hashMap.put(sb2.toString(), new StatusBean(i13, cursor.getInt(cursor.getColumnIndex("battery_status")), -1.0f));
                    i12 = i13;
                    j11 = g6;
                    i10 = 1;
                    j10 = 0;
                    i11 = 0;
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            LocalLog.b("BatteryPowerHelper", "Fail to getBatteryLevelStats cursor is null");
            if (cursor != null) {
            }
        }
        return hashMap.size();
    }

    public static Pair<Long, Long> b(Context context) {
        Map.Entry entry;
        LSLinkedHashMap lSLinkedHashMap = new LSLinkedHashMap();
        a(context, lSLinkedHashMap);
        Iterator it = lSLinkedHashMap.entrySet().iterator();
        Map.Entry entry2 = null;
        loop0: while (true) {
            entry = entry2;
            while (it.hasNext()) {
                entry2 = (Map.Entry) it.next();
                if (entry == null) {
                    break;
                }
            }
        }
        if (entry2 == null) {
            return null;
        }
        return new Pair<>(Long.valueOf(d(true, g((String) entry.getKey()))), Long.valueOf(d(false, g((String) entry2.getKey()))));
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0164 A[Catch: Exception -> 0x0140, TRY_LEAVE, TryCatch #0 {Exception -> 0x0140, blocks: (B:22:0x0031, B:25:0x0053, B:27:0x0059, B:32:0x0114, B:36:0x00fc, B:11:0x0164, B:9:0x0142, B:29:0x0061), top: B:21:0x0031, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    @SuppressLint({"Range"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void c(Context context, ArrayList<PowerSipper> arrayList, long j10, long j11, boolean z10) {
        String str;
        String str2 = "saved_time";
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(DataBaseUtil.f19650b, null, z10 ? "saved_time_millis>=? AND saved_time_millis<=? AND drain_type=?" : "saved_time_millis>=? AND saved_time_millis<=? AND drain_type!=?", new String[]{String.valueOf(j10), String.valueOf(j11), "SMALLAPP"}, null);
            if (query != null) {
                try {
                    if (query.getCount() != 0) {
                        long j12 = 0;
                        long j13 = 0;
                        long j14 = 0;
                        long j15 = 0;
                        double d10 = 0.0d;
                        int i10 = 0;
                        int i11 = 0;
                        int i12 = -1;
                        String str3 = "";
                        String str4 = str3;
                        String str5 = str4;
                        long j16 = 0;
                        long j17 = 0;
                        while (query.moveToNext()) {
                            String string = query.getString(query.getColumnIndex(str2));
                            try {
                                str3 = query.getString(query.getColumnIndex("drain_type"));
                                str4 = query.getString(query.getColumnIndex("packageWithHighestDrain"));
                                str5 = query.getString(query.getColumnIndex(UserProfileConstants.COLUMN_PKG_NAME));
                                j16 = query.getLong(query.getColumnIndex("usage_time"));
                                j17 = query.getLong(query.getColumnIndex("foreground_act_time"));
                                j12 = query.getLong(query.getColumnIndex("background_act_time"));
                                j13 = query.getLong(query.getColumnIndex("awake_time"));
                                j14 = query.getLong(query.getColumnIndex("wlan_tx_bytes"));
                                j15 = query.getLong(query.getColumnIndex("wlan_rx_bytes"));
                                d10 = query.getDouble(query.getColumnIndex("power"));
                                query.getBlob(query.getColumnIndex("icon"));
                                string = query.getString(query.getColumnIndex(str2));
                                i10 = query.getInt(query.getColumnIndex("battery_level"));
                                i11 = query.getInt(query.getColumnIndex("battery_status"));
                                i12 = query.getInt(query.getColumnIndex("sipper_uid"));
                                str = str2;
                            } catch (Throwable th) {
                                str = str2;
                                LocalLog.b("BatteryPowerHelper", "Fail to getColumnIndex e=" + th);
                                str3 = str3;
                            }
                            arrayList.add(new PowerSipper(str3, str4, str5, j16, j17, j12, j13, j14, j15, d10, string, i10, i11, i12));
                            str2 = str;
                        }
                        if (query == null) {
                            query.close();
                            return;
                        }
                        return;
                    }
                } catch (Exception unused) {
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                        return;
                    }
                    return;
                }
            }
            LocalLog.b("BatteryPowerHelper", "Fail to getStatsData cursor is null, startTime = " + j10 + "endTime = " + j11);
            if (query == null) {
            }
        } catch (Exception unused2) {
        }
    }

    public static long d(boolean z10, long j10) {
        long j11;
        long j12;
        synchronized (f4597b) {
            int i10 = 0;
            j11 = j10;
            j12 = j11;
            while (true) {
                int i11 = i10 + 1;
                List<Long> list = f4596a;
                if (i11 >= list.size()) {
                    break;
                }
                if (j10 > list.get(i10).longValue() && j10 < list.get(i11).longValue()) {
                    j11 = list.get(i10).longValue();
                    j12 = list.get(i11).longValue();
                }
                i10 += 2;
            }
        }
        return z10 ? j12 : j11;
    }

    public static void e(ArrayList<PowerSipper> arrayList, ArrayList<PowerSipper> arrayList2, ArrayList<PowerSipper> arrayList3) {
        int i10;
        boolean z10;
        int i11 = 0;
        while (i11 < arrayList.size()) {
            PowerSipper powerSipper = arrayList.get(i11);
            Iterator<PowerSipper> it = arrayList2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    i10 = i11;
                    z10 = false;
                    break;
                }
                PowerSipper next = it.next();
                if (powerSipper.f4601e.equals(next.f4601e) && powerSipper.f4603g.equals(next.f4603g) && powerSipper.f4616t == next.f4616t) {
                    i10 = i11;
                    arrayList3.add(new PowerSipper(powerSipper.f4601e, powerSipper.f4602f, powerSipper.f4603g, powerSipper.f4607k + next.f4607k, powerSipper.f4608l + next.f4608l, powerSipper.f4609m + next.f4609m, powerSipper.f4610n + next.f4610n, powerSipper.f4611o + next.f4611o, powerSipper.f4612p + next.f4612p, powerSipper.f4613q + next.f4613q, powerSipper.f4604h, powerSipper.f4605i, powerSipper.f4606j, powerSipper.f4616t));
                    it.remove();
                    z10 = true;
                    break;
                }
                i11 = i11;
            }
            if (!z10) {
                arrayList3.add(powerSipper);
            }
            i11 = i10 + 1;
        }
        if (arrayList2.size() > 0) {
            arrayList3.addAll(arrayList2);
        }
    }

    public static void f(ArrayList<PowerSipper> arrayList, ArrayList<PowerSipper> arrayList2, ArrayList<PowerSipper> arrayList3) {
        int i10;
        int i11 = 0;
        while (i11 < arrayList.size()) {
            PowerSipper powerSipper = arrayList.get(i11);
            Iterator<PowerSipper> it = arrayList2.iterator();
            boolean z10 = false;
            while (it.hasNext()) {
                PowerSipper next = it.next();
                if (powerSipper.f4601e.equals(next.f4601e) && powerSipper.f4603g.equals(next.f4603g) && powerSipper.f4616t == next.f4616t) {
                    i10 = i11;
                    arrayList3.add(new PowerSipper(powerSipper.f4601e, powerSipper.f4602f, powerSipper.f4603g, powerSipper.f4607k - next.f4607k, powerSipper.f4608l - next.f4608l, powerSipper.f4609m - next.f4609m, powerSipper.f4610n - next.f4610n, powerSipper.f4611o - next.f4611o, powerSipper.f4612p - next.f4612p, powerSipper.f4613q - next.f4613q, powerSipper.f4604h, powerSipper.f4605i, powerSipper.f4606j, powerSipper.f4616t));
                    it.remove();
                    z10 = true;
                } else {
                    i10 = i11;
                }
                i11 = i10;
            }
            int i12 = i11;
            if (!z10) {
                arrayList3.add(powerSipper);
            }
            i11 = i12 + 1;
        }
    }

    private static long g(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str).getTime();
        } catch (Exception e10) {
            e10.printStackTrace();
            return 0L;
        }
    }
}
