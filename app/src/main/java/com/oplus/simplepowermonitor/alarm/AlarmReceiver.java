package com.oplus.simplepowermonitor.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import b6.LocalLog;
import com.oplus.thermalcontrol.ThermalControlUtils;
import m9.AlarmSetter;
import o9.HighPowerHelper;
import p9.PowerMonitor;
import x4.AppSwitchObserverHelper;

/* loaded from: classes2.dex */
public class AlarmReceiver extends BroadcastReceiver {

    /* renamed from: a, reason: collision with root package name */
    private boolean f10475a = false;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        AlarmSetter alarmSetter = new AlarmSetter(context);
        PowerMonitor h10 = PowerMonitor.h(context);
        HighPowerHelper f10 = HighPowerHelper.f(context);
        String action = intent.getAction();
        action.hashCode();
        char c10 = 65535;
        switch (action.hashCode()) {
            case -2067533913:
                if (action.equals("com.oplus.app.spm.alarm.screen.on")) {
                    c10 = 0;
                    break;
                }
                break;
            case -1263111451:
                if (action.equals("com.oplus.app.spm.alarm.store.db")) {
                    c10 = 1;
                    break;
                }
                break;
            case 330957991:
                if (action.equals("com.oplus.app.spm.alarm.screen.off")) {
                    c10 = 2;
                    break;
                }
                break;
            case 1846265101:
                if (action.equals("com.oplus.app.spm.alarm.get.stats")) {
                    c10 = 3;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                this.f10475a = true;
                AppSwitchObserverHelper.d(context).g();
                alarmSetter.f();
                return;
            case 1:
                LocalLog.a("AlarmReceiver", "SimplePowerMonitorUtils.ACTION_ALARM_STORE_DB:");
                f10.p();
                alarmSetter.g();
                return;
            case 2:
                this.f10475a = false;
                if (ThermalControlUtils.getInstance(context).isScreenOn()) {
                    return;
                }
                h10.n(false);
                alarmSetter.e();
                return;
            case 3:
                LocalLog.a("AlarmReceiver", "startDectectAndNotificate");
                f10.m();
                alarmSetter.d();
                return;
            default:
                return;
        }
    }
}
