package p6;

import android.content.Context;
import android.os.UidBatteryConsumer;
import android.os.health.HealthStats;
import android.os.health.SystemHealthManager;
import android.os.health.TimerStat;
import android.util.Log;
import b6.LocalLog;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.statistics.util.TimeInfoUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import v4.BatteryStatsManager;
import x5.UploadDataUtil;

/* compiled from: AppStandbyStatistic.java */
/* renamed from: p6.b, reason: use source file name */
/* loaded from: classes.dex */
public class AppStandbyStatistic {
    public void a(Context context) {
        HealthStats[] healthStatsArr;
        long j10;
        int i10;
        List uidBatteryConsumers = BatteryStatsManager.i().f().getUidBatteryConsumers();
        if (uidBatteryConsumers == null) {
            return;
        }
        int size = uidBatteryConsumers.size();
        int[] iArr = new int[size];
        int i11 = 0;
        for (int i12 = 0; i12 < size; i12++) {
            iArr[i12] = ((UidBatteryConsumer) uidBatteryConsumers.get(i12)).getUid();
        }
        HealthStats[] takeUidSnapshots = new SystemHealthManager().takeUidSnapshots(iArr);
        int length = takeUidSnapshots.length;
        int i13 = 0;
        long j11 = 0;
        long j12 = 0;
        long j13 = 0;
        long j14 = 0;
        int i14 = 0;
        int i15 = 0;
        while (i11 < length) {
            int i16 = length;
            int i17 = iArr[i11];
            int[] iArr2 = iArr;
            if (i17 < 10000) {
                healthStatsArr = takeUidSnapshots;
            } else {
                HealthStats healthStats = takeUidSnapshots[i11];
                healthStatsArr = takeUidSnapshots;
                if (healthStats.hasTimer(10038)) {
                    j10 = j14;
                    long timerTime = healthStats.getTimerTime(10038);
                    StringBuilder sb2 = new StringBuilder();
                    i10 = i15;
                    sb2.append("uid:");
                    sb2.append(i17);
                    sb2.append(", topActivityTime = ");
                    sb2.append(timerTime);
                    LocalLog.a("AppStandbyStatistic", sb2.toString());
                    if (TimeInfoUtil.MILLISECOND_OF_A_DAY - timerTime >= 50400000) {
                        i13++;
                        if (healthStats.hasTimers(EventType.LEAVE_COMPANY)) {
                            Map<String, TimerStat> timers = healthStats.getTimers(EventType.LEAVE_COMPANY);
                            Iterator<String> it = timers.keySet().iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                String next = it.next();
                                if ("*alarm*".equals(next)) {
                                    i14 += timers.get(next).getCount();
                                    break;
                                }
                            }
                        }
                        if (healthStats.hasTimers(10010)) {
                            Map<String, TimerStat> timers2 = healthStats.getTimers(10010);
                            Iterator<String> it2 = timers2.keySet().iterator();
                            i15 = i10;
                            while (it2.hasNext()) {
                                i15 += timers2.get(it2.next()).getCount();
                            }
                        } else {
                            i15 = i10;
                        }
                        if (healthStats.hasMeasurement(10048)) {
                            j11 += healthStats.getMeasurement(10048);
                        }
                        if (healthStats.hasMeasurement(10049)) {
                            j11 += healthStats.getMeasurement(10049);
                        }
                        if (healthStats.hasTimer(10061)) {
                            j12 += healthStats.getTimer(10061).getTime();
                        }
                        if (healthStats.hasMeasurement(10050)) {
                            j13 += healthStats.getMeasurement(10050);
                        }
                        if (healthStats.hasMeasurement(10051)) {
                            j13 += healthStats.getMeasurement(10051);
                        }
                        j14 = healthStats.hasMeasurement(10028) ? j10 + healthStats.getMeasurement(10028) : j10;
                        i11++;
                        length = i16;
                        iArr = iArr2;
                        takeUidSnapshots = healthStatsArr;
                    }
                    j14 = j10;
                    i15 = i10;
                    i11++;
                    length = i16;
                    iArr = iArr2;
                    takeUidSnapshots = healthStatsArr;
                }
            }
            i10 = i15;
            j10 = j14;
            j14 = j10;
            i15 = i10;
            i11++;
            length = i16;
            iArr = iArr2;
            takeUidSnapshots = healthStatsArr;
        }
        int i18 = i15;
        Log.d("AppStandbyStatistic", "alarm_count = " + i14 + ", job_count = " + i18 + ", mobile_byte = " + j11 + ", mobile_active_time = " + j12 + ", wifi_byte = " + j13 + ", wifi_running_time = " + j14 + ", uid_count = " + i13);
        HashMap hashMap = new HashMap();
        hashMap.put("alarm_count", String.valueOf(i14));
        hashMap.put("job_count", String.valueOf(i18));
        hashMap.put("mobile_byte", String.valueOf(j11));
        hashMap.put("mobile_active_time", String.valueOf(j12));
        hashMap.put("wifi_byte", String.valueOf(j13));
        hashMap.put("wifi_running_time", String.valueOf(j14));
        hashMap.put("uid_count", String.valueOf(i13));
        UploadDataUtil.S0(context).f(hashMap);
    }
}
