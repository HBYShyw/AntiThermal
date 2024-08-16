package com.android.server.am;

import android.os.BatteryStats;
import android.os.SystemClock;
import android.os.health.HealthStatsWriter;
import android.os.health.PackageHealthStats;
import android.os.health.PidHealthStats;
import android.os.health.ProcessHealthStats;
import android.os.health.ServiceHealthStats;
import android.os.health.TimerStat;
import android.util.SparseArray;
import com.android.internal.util.FrameworkStatsLog;
import java.util.Map;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class HealthStatsBatteryStatsWriter {
    private final long mNowRealtimeMs = SystemClock.elapsedRealtime();
    private final long mNowUptimeMs = SystemClock.uptimeMillis();

    public void writeUid(HealthStatsWriter healthStatsWriter, BatteryStats batteryStats, BatteryStats.Uid uid) {
        healthStatsWriter.addMeasurement(FrameworkStatsLog.WIFI_BYTES_TRANSFER_BY_FG_BG, batteryStats.computeBatteryRealtime(this.mNowRealtimeMs * 1000, 0) / 1000);
        healthStatsWriter.addMeasurement(FrameworkStatsLog.MOBILE_BYTES_TRANSFER, batteryStats.computeBatteryUptime(this.mNowUptimeMs * 1000, 0) / 1000);
        healthStatsWriter.addMeasurement(10003, batteryStats.computeBatteryScreenOffRealtime(this.mNowRealtimeMs * 1000, 0) / 1000);
        healthStatsWriter.addMeasurement(FrameworkStatsLog.KERNEL_WAKELOCK, batteryStats.computeBatteryScreenOffUptime(this.mNowUptimeMs * 1000, 0) / 1000);
        for (Map.Entry entry : uid.getWakelockStats().entrySet()) {
            String str = (String) entry.getKey();
            BatteryStats.Uid.Wakelock wakelock = (BatteryStats.Uid.Wakelock) entry.getValue();
            addTimers(healthStatsWriter, FrameworkStatsLog.SUBSYSTEM_SLEEP_STATE, str, wakelock.getWakeTime(1));
            addTimers(healthStatsWriter, FrameworkStatsLog.BLUETOOTH_BYTES_TRANSFER, str, wakelock.getWakeTime(0));
            addTimers(healthStatsWriter, FrameworkStatsLog.BLUETOOTH_ACTIVITY_INFO, str, wakelock.getWakeTime(2));
            addTimers(healthStatsWriter, 10008, str, wakelock.getWakeTime(18));
        }
        for (Map.Entry entry2 : uid.getSyncStats().entrySet()) {
            addTimers(healthStatsWriter, FrameworkStatsLog.CPU_TIME_PER_UID, (String) entry2.getKey(), (BatteryStats.Timer) entry2.getValue());
        }
        for (Map.Entry entry3 : uid.getJobStats().entrySet()) {
            addTimers(healthStatsWriter, FrameworkStatsLog.CPU_TIME_PER_UID_FREQ, (String) entry3.getKey(), (BatteryStats.Timer) entry3.getValue());
        }
        SparseArray sensorStats = uid.getSensorStats();
        int size = sensorStats.size();
        for (int i = 0; i < size; i++) {
            int keyAt = sensorStats.keyAt(i);
            if (keyAt == -10000) {
                addTimer(healthStatsWriter, FrameworkStatsLog.WIFI_ACTIVITY_INFO, ((BatteryStats.Uid.Sensor) sensorStats.valueAt(i)).getSensorTime());
            } else {
                addTimers(healthStatsWriter, FrameworkStatsLog.MODEM_ACTIVITY_INFO, Integer.toString(keyAt), ((BatteryStats.Uid.Sensor) sensorStats.valueAt(i)).getSensorTime());
            }
        }
        SparseArray pidStats = uid.getPidStats();
        int size2 = pidStats.size();
        for (int i2 = 0; i2 < size2; i2++) {
            HealthStatsWriter healthStatsWriter2 = new HealthStatsWriter(PidHealthStats.CONSTANTS);
            writePid(healthStatsWriter2, (BatteryStats.Uid.Pid) pidStats.valueAt(i2));
            healthStatsWriter.addStats(FrameworkStatsLog.PROCESS_MEMORY_STATE, Integer.toString(pidStats.keyAt(i2)), healthStatsWriter2);
        }
        for (Map.Entry entry4 : uid.getProcessStats().entrySet()) {
            HealthStatsWriter healthStatsWriter3 = new HealthStatsWriter(ProcessHealthStats.CONSTANTS);
            writeProc(healthStatsWriter3, (BatteryStats.Uid.Proc) entry4.getValue());
            healthStatsWriter.addStats(FrameworkStatsLog.SYSTEM_ELAPSED_REALTIME, (String) entry4.getKey(), healthStatsWriter3);
        }
        for (Map.Entry entry5 : uid.getPackageStats().entrySet()) {
            HealthStatsWriter healthStatsWriter4 = new HealthStatsWriter(PackageHealthStats.CONSTANTS);
            writePkg(healthStatsWriter4, (BatteryStats.Uid.Pkg) entry5.getValue());
            healthStatsWriter.addStats(FrameworkStatsLog.SYSTEM_UPTIME, (String) entry5.getKey(), healthStatsWriter4);
        }
        BatteryStats.ControllerActivityCounter wifiControllerActivity = uid.getWifiControllerActivity();
        if (wifiControllerActivity != null) {
            healthStatsWriter.addMeasurement(FrameworkStatsLog.CPU_ACTIVE_TIME, wifiControllerActivity.getIdleTimeCounter().getCountLocked(0));
            healthStatsWriter.addMeasurement(FrameworkStatsLog.CPU_CLUSTER_TIME, wifiControllerActivity.getRxTimeCounter().getCountLocked(0));
            long j = 0;
            for (BatteryStats.LongCounter longCounter : wifiControllerActivity.getTxTimeCounters()) {
                j += longCounter.getCountLocked(0);
            }
            healthStatsWriter.addMeasurement(10018, j);
            healthStatsWriter.addMeasurement(FrameworkStatsLog.REMAINING_BATTERY_CAPACITY, wifiControllerActivity.getPowerCounter().getCountLocked(0));
        }
        BatteryStats.ControllerActivityCounter bluetoothControllerActivity = uid.getBluetoothControllerActivity();
        if (bluetoothControllerActivity != null) {
            healthStatsWriter.addMeasurement(FrameworkStatsLog.FULL_BATTERY_CAPACITY, bluetoothControllerActivity.getIdleTimeCounter().getCountLocked(0));
            healthStatsWriter.addMeasurement(FrameworkStatsLog.TEMPERATURE, bluetoothControllerActivity.getRxTimeCounter().getCountLocked(0));
            long j2 = 0;
            for (BatteryStats.LongCounter longCounter2 : bluetoothControllerActivity.getTxTimeCounters()) {
                j2 += longCounter2.getCountLocked(0);
            }
            healthStatsWriter.addMeasurement(FrameworkStatsLog.BINDER_CALLS, j2);
            healthStatsWriter.addMeasurement(FrameworkStatsLog.BINDER_CALLS_EXCEPTIONS, bluetoothControllerActivity.getPowerCounter().getCountLocked(0));
        }
        BatteryStats.ControllerActivityCounter modemControllerActivity = uid.getModemControllerActivity();
        if (modemControllerActivity != null) {
            healthStatsWriter.addMeasurement(FrameworkStatsLog.LOOPER_STATS, modemControllerActivity.getIdleTimeCounter().getCountLocked(0));
            healthStatsWriter.addMeasurement(FrameworkStatsLog.DISK_STATS, modemControllerActivity.getRxTimeCounter().getCountLocked(0));
            long j3 = 0;
            for (BatteryStats.LongCounter longCounter3 : modemControllerActivity.getTxTimeCounters()) {
                j3 += longCounter3.getCountLocked(0);
            }
            healthStatsWriter.addMeasurement(FrameworkStatsLog.DIRECTORY_USAGE, j3);
            healthStatsWriter.addMeasurement(FrameworkStatsLog.APP_SIZE, modemControllerActivity.getPowerCounter().getCountLocked(0));
        }
        healthStatsWriter.addMeasurement(FrameworkStatsLog.CATEGORY_SIZE, uid.getWifiRunningTime(this.mNowRealtimeMs * 1000, 0) / 1000);
        healthStatsWriter.addMeasurement(FrameworkStatsLog.PROC_STATS, uid.getFullWifiLockTime(this.mNowRealtimeMs * 1000, 0) / 1000);
        healthStatsWriter.addTimer(FrameworkStatsLog.BATTERY_VOLTAGE, uid.getWifiScanCount(0), uid.getWifiScanTime(this.mNowRealtimeMs * 1000, 0) / 1000);
        healthStatsWriter.addMeasurement(FrameworkStatsLog.NUM_FINGERPRINTS_ENROLLED, uid.getWifiMulticastTime(this.mNowRealtimeMs * 1000, 0) / 1000);
        addTimer(healthStatsWriter, FrameworkStatsLog.DISK_IO, uid.getAudioTurnedOnTimer());
        addTimer(healthStatsWriter, FrameworkStatsLog.POWER_PROFILE, uid.getVideoTurnedOnTimer());
        addTimer(healthStatsWriter, FrameworkStatsLog.PROC_STATS_PKG_PROC, uid.getFlashlightTurnedOnTimer());
        addTimer(healthStatsWriter, FrameworkStatsLog.PROCESS_CPU_TIME, uid.getCameraTurnedOnTimer());
        addTimer(healthStatsWriter, 10036, uid.getForegroundActivityTimer());
        addTimer(healthStatsWriter, FrameworkStatsLog.CPU_TIME_PER_THREAD_FREQ, uid.getBluetoothScanTimer());
        addTimer(healthStatsWriter, FrameworkStatsLog.ON_DEVICE_POWER_MEASUREMENT, uid.getProcessStateTimer(0));
        addTimer(healthStatsWriter, FrameworkStatsLog.DEVICE_CALCULATED_POWER_USE, uid.getProcessStateTimer(1));
        addTimer(healthStatsWriter, 10040, uid.getProcessStateTimer(4));
        addTimer(healthStatsWriter, 10041, uid.getProcessStateTimer(2));
        addTimer(healthStatsWriter, FrameworkStatsLog.PROCESS_MEMORY_HIGH_WATER_MARK, uid.getProcessStateTimer(3));
        addTimer(healthStatsWriter, FrameworkStatsLog.BATTERY_LEVEL, uid.getProcessStateTimer(6));
        addTimer(healthStatsWriter, FrameworkStatsLog.BUILD_INFORMATION, uid.getVibratorOnTimer());
        healthStatsWriter.addMeasurement(FrameworkStatsLog.BATTERY_CYCLE_COUNT, uid.getUserActivityCount(0, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.DEBUG_ELAPSED_CLOCK, uid.getUserActivityCount(1, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.DEBUG_FAILING_ELAPSED_CLOCK, uid.getUserActivityCount(2, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.NUM_FACES_ENROLLED, uid.getNetworkActivityBytes(0, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.ROLE_HOLDER, uid.getNetworkActivityBytes(1, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.DANGEROUS_PERMISSION_STATE, uid.getNetworkActivityBytes(2, 0));
        healthStatsWriter.addMeasurement(10051, uid.getNetworkActivityBytes(3, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.TIME_ZONE_DATA_INFO, uid.getNetworkActivityBytes(4, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.EXTERNAL_STORAGE_INFO, uid.getNetworkActivityBytes(5, 0));
        healthStatsWriter.addMeasurement(10054, uid.getNetworkActivityPackets(0, 0));
        healthStatsWriter.addMeasurement(10055, uid.getNetworkActivityPackets(1, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.SYSTEM_ION_HEAP_SIZE, uid.getNetworkActivityPackets(2, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.APPS_ON_EXTERNAL_STORAGE_INFO, uid.getNetworkActivityPackets(3, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.FACE_SETTINGS, uid.getNetworkActivityPackets(4, 0));
        healthStatsWriter.addMeasurement(FrameworkStatsLog.COOLING_DEVICE, uid.getNetworkActivityPackets(5, 0));
        healthStatsWriter.addTimer(FrameworkStatsLog.PROCESS_SYSTEM_ION_HEAP_SIZE, uid.getMobileRadioActiveCount(0), uid.getMobileRadioActiveTime(0));
        healthStatsWriter.addMeasurement(10062, uid.getUserCpuTimeUs(0) / 1000);
        healthStatsWriter.addMeasurement(10063, uid.getSystemCpuTimeUs(0) / 1000);
        healthStatsWriter.addMeasurement(FrameworkStatsLog.PROCESS_MEMORY_SNAPSHOT, 0L);
    }

    public void writePid(HealthStatsWriter healthStatsWriter, BatteryStats.Uid.Pid pid) {
        if (pid == null) {
            return;
        }
        healthStatsWriter.addMeasurement(20001, pid.mWakeNesting);
        healthStatsWriter.addMeasurement(20002, pid.mWakeSumMs);
        healthStatsWriter.addMeasurement(20002, pid.mWakeStartMs);
    }

    public void writeProc(HealthStatsWriter healthStatsWriter, BatteryStats.Uid.Proc proc) {
        healthStatsWriter.addMeasurement(30001, proc.getUserTime(0));
        healthStatsWriter.addMeasurement(30002, proc.getSystemTime(0));
        healthStatsWriter.addMeasurement(30003, proc.getStarts(0));
        healthStatsWriter.addMeasurement(30004, proc.getNumCrashes(0));
        healthStatsWriter.addMeasurement(30005, proc.getNumAnrs(0));
        healthStatsWriter.addMeasurement(30006, proc.getForegroundTime(0));
    }

    public void writePkg(HealthStatsWriter healthStatsWriter, BatteryStats.Uid.Pkg pkg) {
        for (Map.Entry entry : pkg.getServiceStats().entrySet()) {
            HealthStatsWriter healthStatsWriter2 = new HealthStatsWriter(ServiceHealthStats.CONSTANTS);
            writeServ(healthStatsWriter2, (BatteryStats.Uid.Pkg.Serv) entry.getValue());
            healthStatsWriter.addStats(com.android.server.EventLogTags.STREAM_DEVICES_CHANGED, (String) entry.getKey(), healthStatsWriter2);
        }
        for (Map.Entry entry2 : pkg.getWakeupAlarmStats().entrySet()) {
            if (((BatteryStats.Counter) entry2.getValue()) != null) {
                healthStatsWriter.addMeasurements(40002, (String) entry2.getKey(), r0.getCountLocked(0));
            }
        }
    }

    public void writeServ(HealthStatsWriter healthStatsWriter, BatteryStats.Uid.Pkg.Serv serv) {
        healthStatsWriter.addMeasurement(50001, serv.getStarts(0));
        healthStatsWriter.addMeasurement(50002, serv.getLaunches(0));
    }

    private void addTimer(HealthStatsWriter healthStatsWriter, int i, BatteryStats.Timer timer) {
        if (timer != null) {
            healthStatsWriter.addTimer(i, timer.getCountLocked(0), timer.getTotalTimeLocked(this.mNowRealtimeMs * 1000, 0) / 1000);
        }
    }

    private void addTimers(HealthStatsWriter healthStatsWriter, int i, String str, BatteryStats.Timer timer) {
        if (timer != null) {
            healthStatsWriter.addTimers(i, str, new TimerStat(timer.getCountLocked(0), timer.getTotalTimeLocked(this.mNowRealtimeMs * 1000, 0) / 1000));
        }
    }
}
