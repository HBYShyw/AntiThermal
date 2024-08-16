package e7;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkInfo;
import android.net.NetworkStats;
import android.net.NetworkTemplate;
import android.net.TrafficStats;
import android.os.RemoteException;
import android.os.ServiceManager;
import b6.LocalLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/* compiled from: TrafficUtil.java */
/* renamed from: e7.c, reason: use source file name */
/* loaded from: classes.dex */
public class TrafficUtil {
    public static ArrayList<String> a(Context context) {
        LocalLog.a("SmartDoze:TrafficUtil", "getNetUsingList");
        if (context == null) {
            LocalLog.a("SmartDoze:TrafficUtil", "context is null");
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            LocalLog.a("SmartDoze:TrafficUtil", "cm is null");
            return null;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return arrayList;
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        if (networkInfo == null) {
            return null;
        }
        NetworkInfo.State state = networkInfo.getState();
        if (state == null) {
            LocalLog.a("SmartDoze:TrafficUtil", "wifiState is null");
            return null;
        }
        if (state != NetworkInfo.State.CONNECTED && state != NetworkInfo.State.CONNECTING) {
            return null;
        }
        LocalLog.a("SmartDoze:TrafficUtil", "wifi connected!  ");
        NetworkStats b10 = b(context, 0);
        LocalLog.a("SmartDoze:TrafficUtil", "stats1 = " + b10);
        if (b10 == null) {
            return null;
        }
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException unused) {
            LocalLog.a("SmartDoze:TrafficUtil", "thread sleep error");
        }
        NetworkStats b11 = b(context, 0);
        LocalLog.a("SmartDoze:TrafficUtil", "stats2 = " + b11);
        if (b11 == null) {
            return null;
        }
        return c(context, b10, b11, 0);
    }

    private static NetworkStats b(Context context, int i10) {
        INetworkStatsSession iNetworkStatsSession;
        if (context == null) {
            return null;
        }
        NetworkStats asInterface = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
        try {
            if (asInterface == null) {
                return null;
            }
            try {
                asInterface.forceUpdate();
                iNetworkStatsSession = asInterface.openSession();
                if (i10 != 0) {
                    if (iNetworkStatsSession != null) {
                        TrafficStats.closeQuietly(iNetworkStatsSession);
                    }
                    return null;
                }
                try {
                    r0 = iNetworkStatsSession != null ? iNetworkStatsSession.getSummaryForAllUid(NetworkTemplate.buildTemplateWifi(), System.currentTimeMillis() - 3600000, System.currentTimeMillis(), false) : null;
                    if (iNetworkStatsSession != null) {
                        TrafficStats.closeQuietly(iNetworkStatsSession);
                    }
                    return r0;
                } catch (RemoteException e10) {
                    e = e10;
                    LocalLog.a("SmartDoze:TrafficUtil", "get network data error: " + e);
                    if (iNetworkStatsSession != null) {
                        TrafficStats.closeQuietly(iNetworkStatsSession);
                    }
                    return null;
                }
            } catch (RemoteException e11) {
                e = e11;
                iNetworkStatsSession = null;
            } catch (Throwable th) {
                th = th;
                if (r0 != null) {
                    TrafficStats.closeQuietly(r0);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            r0 = asInterface;
        }
    }

    private static ArrayList<String> c(Context context, NetworkStats networkStats, NetworkStats networkStats2, int i10) {
        int i11;
        int i12;
        int i13;
        if (networkStats != null && networkStats2 != null && context != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            ArrayList arrayList5 = new ArrayList();
            PackageManager packageManager = context.getPackageManager();
            if (i10 == 1) {
                i11 = 20;
            } else if (i10 == 0) {
                i11 = 50;
            } else {
                LocalLog.a("SmartDoze:TrafficUtil", "wrong connection parameter!");
            }
            d(networkStats, arrayList2, arrayList3);
            d(networkStats2, arrayList4, arrayList5);
            int size = arrayList2.size();
            int size2 = arrayList4.size();
            int i14 = 0;
            while (i14 < size) {
                int intValue = ((Integer) arrayList2.get(i14)).intValue();
                long longValue = ((Long) arrayList3.get(i14)).longValue();
                if (intValue > 0 && intValue != 1000) {
                    for (int i15 = 0; i15 < size2; i15++) {
                        if (intValue == ((Integer) arrayList4.get(i15)).intValue()) {
                            long longValue2 = ((Long) arrayList5.get(i15)).longValue() - longValue;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(" uid: ");
                            sb2.append(intValue);
                            sb2.append(" data:(5s) ");
                            sb2.append(longValue2);
                            i12 = size;
                            sb2.append("K\n pkgName: ");
                            sb2.append(Arrays.toString(packageManager.getPackagesForUid(intValue)));
                            LocalLog.a("SmartDoze:TrafficUtil", sb2.toString());
                            i13 = size2;
                            if (longValue2 > i11) {
                                String[] packagesForUid = packageManager.getPackagesForUid(intValue);
                                LocalLog.a("SmartDoze:TrafficUtil", "using network!  uid: " + intValue + " data:(5s) " + longValue2 + "K");
                                if (packagesForUid != null) {
                                    Collections.addAll(arrayList, packagesForUid);
                                }
                            }
                            i14++;
                            size = i12;
                            size2 = i13;
                        }
                    }
                }
                i12 = size;
                i13 = size2;
                i14++;
                size = i12;
                size2 = i13;
            }
            return arrayList;
        }
        return null;
    }

    private static void d(NetworkStats networkStats, ArrayList<Integer> arrayList, ArrayList<Long> arrayList2) {
        int i10 = 0;
        for (int i11 = 0; i11 < networkStats.size(); i11++) {
            NetworkStats.Entry values = networkStats.getValues(i11, (NetworkStats.Entry) null);
            int i12 = values.uid;
            long j10 = (values.rxBytes / 1024) + (values.txBytes / 1024);
            arrayList.add(Integer.valueOf(i12));
            arrayList2.add(Long.valueOf(j10));
        }
        while (i10 < arrayList.size() - 1) {
            int intValue = arrayList.get(i10).intValue();
            int i13 = i10 + 1;
            int i14 = i13;
            while (i14 < arrayList.size()) {
                if (intValue == arrayList.get(i14).intValue()) {
                    arrayList2.set(i10, Long.valueOf(arrayList2.get(i10).longValue() + arrayList2.get(i14).longValue()));
                    arrayList.remove(i14);
                    arrayList2.remove(i14);
                    i14--;
                }
                i14++;
            }
            i10 = i13;
        }
    }
}
