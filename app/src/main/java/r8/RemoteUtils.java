package r8;

import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import b6.LocalLog;
import b9.BatteryPowerHelper;
import b9.PowerSipper;
import com.oplus.battery.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import v4.GuardElfContext;

/* compiled from: RemoteUtils.java */
/* renamed from: r8.f, reason: use source file name */
/* loaded from: classes2.dex */
public class RemoteUtils {
    public static ArrayList<PowerSipper> b(long j10, long j11, Context context, boolean z10) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        final ArrayList<PowerSipper> arrayList3 = new ArrayList<>();
        LocalLog.a("RemoteUtils", "mSipperListStartTime:" + j10 + "mSipperListEndTime:" + j11);
        BatteryPowerHelper.c(context, arrayList, j10 - 60000, j10 + 1200000, false);
        BatteryPowerHelper.c(context, arrayList2, j11 - 60000, j11 + 1200000, false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j10);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j11);
        long j12 = 0;
        boolean z11 = false;
        while (calendar.getTimeInMillis() < calendar2.getTimeInMillis()) {
            if (calendar.get(11) == 18 && calendar.get(12) == 0) {
                j12 = calendar.getTimeInMillis();
                z11 = true;
            }
            calendar.add(12, 30);
        }
        LocalLog.a("RemoteUtils", "needAdd:" + z11);
        LocalLog.a("RemoteUtils", "time:" + j12);
        if (z11) {
            ArrayList arrayList4 = new ArrayList();
            ArrayList arrayList5 = new ArrayList();
            BatteryPowerHelper.c(context, arrayList5, j12 - 300000, j12 + 300000, false);
            BatteryPowerHelper.f(arrayList5, arrayList, arrayList4);
            LocalLog.a("RemoteUtils", "listBeforeReset size:" + arrayList4.size());
            LocalLog.a("RemoteUtils", "listReset size:" + arrayList5.size());
            LocalLog.a("RemoteUtils", "listEnd size:" + arrayList2.size());
            BatteryPowerHelper.e(arrayList4, arrayList2, arrayList3);
        } else {
            BatteryPowerHelper.f(arrayList2, arrayList, arrayList3);
        }
        HashMap hashMap = new HashMap();
        Iterator<PowerSipper> it = arrayList3.iterator();
        while (it.hasNext()) {
            PowerSipper next = it.next();
            int userId = UserHandle.getUserId(next.f4616t);
            if (userId != 0 && userId != 999) {
                it.remove();
                MultiUserBean multiUserBean = (MultiUserBean) hashMap.get(Integer.valueOf(userId));
                if (multiUserBean == null) {
                    multiUserBean = new MultiUserBean(userId, next.f4613q, next.f4608l);
                } else {
                    multiUserBean.c(multiUserBean.a() + next.f4613q);
                    multiUserBean.d(Math.min(multiUserBean.b() + next.f4608l, j11 - j10));
                }
                hashMap.put(Integer.valueOf(userId), multiUserBean);
            }
        }
        hashMap.forEach(new BiConsumer() { // from class: r8.e
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                RemoteUtils.d(arrayList3, (Integer) obj, (MultiUserBean) obj2);
            }
        });
        LocalLog.a("RemoteUtils", "listResult size:" + arrayList3.size());
        if (z10) {
            arrayList3.sort(PowerSipper.f4600z);
        } else {
            arrayList3.sort(PowerSipper.f4599y);
        }
        return arrayList3;
    }

    public static Bundle c() {
        Context c10 = GuardElfContext.e().c();
        Bundle bundle = new Bundle();
        LSLinkedHashMap lSLinkedHashMap = new LSLinkedHashMap();
        BatteryPowerHelper.a(c10, lSLinkedHashMap);
        Iterator it = lSLinkedHashMap.entrySet().iterator();
        long e10 = it.hasNext() ? e((String) ((Map.Entry) it.next()).getKey()) : 0L;
        long j10 = e10;
        Map.Entry entry = null;
        while (it.hasNext()) {
            entry = (Map.Entry) it.next();
        }
        if (entry != null) {
            j10 = e((String) entry.getKey());
        }
        ArrayList<PowerSipper> b10 = b(e10, j10, c10, false);
        for (int i10 = 0; i10 <= Math.min(29, b10.size() - 1); i10++) {
            PowerSipper powerSipper = b10.get(i10);
            if ("USER".equals(powerSipper.f4601e)) {
                try {
                    UserInfo userInfo = ((UserManager) c10.getSystemService("user")).getUserInfo(powerSipper.f4616t);
                    if (userInfo != null) {
                        bundle.putString("user_" + (userInfo.name + "-" + c10.getResources().getString(R.string.battery_ui_multi_user)), String.valueOf((powerSipper.f4613q * 100.0d) / f6.f.n(c10)));
                    }
                } catch (Exception e11) {
                    e11.printStackTrace();
                    LocalLog.a("RemoteUtils", "get user name error!");
                }
            } else {
                bundle.putString("pkg_" + powerSipper.f4616t + "_" + powerSipper.f4603g, String.valueOf((powerSipper.f4613q * 100.0d) / f6.f.n(c10)));
            }
        }
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void d(ArrayList arrayList, Integer num, MultiUserBean multiUserBean) {
        LocalLog.a("RemoteUtils", "multiUserMap: K:" + num + " power:" + multiUserBean.a() + " time" + multiUserBean.b());
        arrayList.add(new PowerSipper("USER", num.toString(), multiUserBean.b(), multiUserBean.a(), num.intValue()));
    }

    private static long e(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str).getTime();
        } catch (Exception e10) {
            e10.printStackTrace();
            return 0L;
        }
    }
}
