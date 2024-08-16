package com.android.internal.os;

import android.os.BatteryStats;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.health.TimerStat;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class UidSipper implements Parcelable {
    private static final int BINS_SCREEN_BRIGHTNESS = 40;
    private static final int BLUETOOTH_RX_BYTES = 32;
    private static final int BLUETOOTH_RX_PACKETS = 38;
    private static final int BLUETOOTH_TX_BYTES = 33;
    private static final int BLUETOOTH_TX_PACKETS = 39;
    private static final int CONTROLLER_BLUETOOTH = 4;
    private static final int CONTROLLER_MODEM = 5;
    private static final int CONTROLLER_WIFI = 3;
    private static final int COUNT_BUTTON_USER_ACTIVITY = 26;
    private static final int COUNT_MOBILE_RADIO_ACTIVE = 41;
    private static final int COUNT_OTHER_USER_ACTIVITY = 25;
    private static final int COUNT_TOUCH_USER_ACTIVITY = 27;
    private static final int COUNT_WAKEUP_ALARMS = 2;
    private static final int COUNT_WIFI_SCAN = 8;
    public static final Parcelable.Creator<UidSipper> CREATOR = new Parcelable.Creator<UidSipper>() { // from class: com.android.internal.os.UidSipper.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UidSipper createFromParcel(Parcel in) {
            return new UidSipper(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UidSipper[] newArray(int size) {
            return new UidSipper[size];
        }
    };
    private static final int MILLI_TO_MICRO_RATIO = 1000;
    private static final int MOBILE_RX_BYTES = 28;
    private static final int MOBILE_RX_PACKETS = 34;
    private static final int MOBILE_TX_BYTES = 29;
    private static final int MOBILE_TX_PACKETS = 35;
    private static final String TAG = "UidSipper";
    private static final int TIMER_AUDIO = 12;
    private static final int TIMER_BLUETOOTH_SCAN = 17;
    private static final int TIMER_FOREGROUND_ACTIVITY = 16;
    private static final int TIMER_VIBRATOR = 24;
    private static final int TIMER_VIDEO = 13;
    private static final int TIME_MS_BACKGROUND_WAKELOCK = 1;
    private static final int TIME_MS_CPU_ACTIVE = 45;
    private static final int TIME_MS_CPU_CLUSTER = 46;
    private static final int TIME_MS_PROCESS_STATE_BACKGROUND = 22;
    private static final int TIME_MS_PROCESS_STATE_CACHED = 23;
    private static final int TIME_MS_PROCESS_STATE_FOREGROUND = 21;
    private static final int TIME_MS_PROCESS_STATE_FOREGROUND_SERVICE = 19;
    private static final int TIME_MS_PROCESS_STATE_TOP = 18;
    private static final int TIME_MS_PROCESS_STATE_TOP_SLEEPING = 20;
    private static final int TIME_MS_TOTAL_WAKELOCK = 0;
    private static final int TIME_MS_WIFI_BATCHED_SCAN = 11;
    private static final int TIME_MS_WIFI_FULL_LOCK = 7;
    private static final int TIME_MS_WIFI_MULTICAST = 10;
    private static final int TIME_MS_WIFI_RUNNING = 6;
    private static final int TIME_US_CAMERA = 15;
    private static final int TIME_US_FLASHLIGHT = 14;
    private static final int TIME_US_MOBILE_RADIO_ACTIVE = 42;
    private static final int TIME_US_SYSTEM_CPU = 44;
    private static final int TIME_US_USER_CPU = 43;
    private static final int TIME_US_WIFI_SCAN = 9;
    private static final int WIFI_RX_BYTES = 30;
    private static final int WIFI_RX_PACKETS = 36;
    private static final int WIFI_TX_BYTES = 31;
    private static final int WIFI_TX_PACKETS = 37;
    private boolean DEBUG;
    private long[][] mCpuClusterSpeedTimesUs;
    private ArrayMap<String, ArrayMap<Integer, Integer>> mJobCompletionMap;
    private ArrayMap<String, TimerStat> mJobMap;
    private ArrayMap<Integer, long[]> mLongArrays;
    private ArrayMap<Integer, Long> mLongs;
    private ArrayMap<Integer, TimerStat> mSensorMap;
    private ArrayMap<String, TimerStat> mSyncMap;
    private ArrayMap<Integer, TimerStat> mTimers;
    private ArrayMap<Integer, TimerStat> mWakeTimers;

    public UidSipper() {
        this.DEBUG = false;
        this.mSyncMap = new ArrayMap<>();
        this.mJobMap = new ArrayMap<>();
        this.mWakeTimers = new ArrayMap<>();
        this.mSensorMap = new ArrayMap<>();
        this.mTimers = new ArrayMap<>();
        this.mJobCompletionMap = new ArrayMap<>();
        this.mLongs = new ArrayMap<>();
        this.mLongArrays = new ArrayMap<>();
        this.mCpuClusterSpeedTimesUs = new long[0];
    }

    protected UidSipper(Parcel in) {
        this.DEBUG = false;
        this.mSyncMap = new ArrayMap<>();
        this.mJobMap = new ArrayMap<>();
        this.mWakeTimers = new ArrayMap<>();
        this.mSensorMap = new ArrayMap<>();
        this.mTimers = new ArrayMap<>();
        this.mJobCompletionMap = new ArrayMap<>();
        this.mLongs = new ArrayMap<>();
        this.mLongArrays = new ArrayMap<>();
        this.mCpuClusterSpeedTimesUs = new long[0];
        this.mSyncMap = createParcelableMap(in, TimerStat.CREATOR);
        this.mJobMap = createParcelableMap(in, TimerStat.CREATOR);
        this.mWakeTimers = createIntParcelableMap(in, TimerStat.CREATOR);
        this.mSensorMap = createIntParcelableMap(in, TimerStat.CREATOR);
        this.mTimers = createIntParcelableMap(in, TimerStat.CREATOR);
        this.mJobCompletionMap = createParcelableMapMap(in);
        this.mLongs = createParcelableLongsMap(in);
        this.mLongArrays = createParcelableLongArrayMap(in);
        this.mCpuClusterSpeedTimesUs = createParcelableCpuClusterSpeedTimes(in);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        writeParcelableMap(out, this.mSyncMap);
        writeParcelableMap(out, this.mJobMap);
        writeIntParcelableMap(out, this.mWakeTimers);
        writeIntParcelableMap(out, this.mSensorMap);
        writeIntParcelableMap(out, this.mTimers);
        writeParcelableMapMap(out, this.mJobCompletionMap);
        writeParcelableLongsMap(out, this.mLongs);
        writeParcelableLongArrayMap(out, this.mLongArrays);
        writeParcelableCpuClusterSpeedTimes(out, this.mCpuClusterSpeedTimesUs);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public UidSipper initFromUid(BatteryStats.Uid uid, PowerProfile powerProfile, Object bsi, long elapsedRealtimeMs, int which) {
        long totalWakelockTimeMs;
        long totalTimeLocked;
        if (uid != null) {
            Iterator it = uid.getWakelockStats().entrySet().iterator();
            while (it.hasNext()) {
                BatteryStats.Uid.Wakelock wakelock = (BatteryStats.Uid.Wakelock) ((Map.Entry) it.next()).getValue();
                BatteryStats.Timer timer = wakelock.getWakeTime(1);
                addTimer(1, timer, this.mWakeTimers, elapsedRealtimeMs, which);
                BatteryStats.Timer timer2 = wakelock.getWakeTime(0);
                addTimer(0, timer2, this.mWakeTimers, elapsedRealtimeMs, which);
                BatteryStats.Timer timer3 = wakelock.getWakeTime(2);
                addTimer(2, timer3, this.mWakeTimers, elapsedRealtimeMs, which);
                BatteryStats.Timer timer4 = wakelock.getWakeTime(18);
                addTimer(18, timer4, this.mWakeTimers, elapsedRealtimeMs, which);
            }
            BatteryStats.Timer aggTimer = uid.getAggregatedPartialWakelockTimer();
            long bgWakelockTimeMs = 0;
            long j = 0;
            if (aggTimer == null) {
                totalWakelockTimeMs = 0;
            } else {
                long totalWakelockTimeMs2 = aggTimer.getTotalDurationMsLocked(elapsedRealtimeMs);
                BatteryStats.Timer subTimer = aggTimer.getSubTimer();
                bgWakelockTimeMs = subTimer == null ? 0L : subTimer.getTotalDurationMsLocked(elapsedRealtimeMs);
                totalWakelockTimeMs = totalWakelockTimeMs2;
            }
            addLong(0, totalWakelockTimeMs, this.mLongs);
            addLong(1, bgWakelockTimeMs, this.mLongs);
            for (Map.Entry<String, ? extends BatteryStats.Timer> entry : uid.getSyncStats().entrySet()) {
                addTimer(entry.getKey(), (BatteryStats.Timer) entry.getValue(), this.mSyncMap, elapsedRealtimeMs, which);
                totalWakelockTimeMs = totalWakelockTimeMs;
                bgWakelockTimeMs = bgWakelockTimeMs;
            }
            for (Map.Entry<String, ? extends BatteryStats.Timer> entry2 : uid.getJobStats().entrySet()) {
                addTimer(entry2.getKey(), (BatteryStats.Timer) entry2.getValue(), this.mJobMap, elapsedRealtimeMs, which);
            }
            for (Map.Entry<String, SparseIntArray> entry3 : uid.getJobCompletionStats().entrySet()) {
                addJob(entry3.getKey(), entry3.getValue(), this.mJobCompletionMap);
            }
            SparseArray<? extends BatteryStats.Uid.Sensor> sensors = uid.getSensorStats();
            int i = 0;
            for (int N = sensors.size(); i < N; N = N) {
                int sensorId = sensors.keyAt(i);
                addTimer(sensorId, ((BatteryStats.Uid.Sensor) sensors.valueAt(i)).getSensorTime(), this.mSensorMap, elapsedRealtimeMs, which);
                i++;
            }
            Iterator it2 = uid.getPackageStats().entrySet().iterator();
            while (it2.hasNext()) {
                addPkg((BatteryStats.Uid.Pkg) ((Map.Entry) it2.next()).getValue(), which);
            }
            BatteryStats.ControllerActivityCounter controller = uid.getWifiControllerActivity();
            if (controller != null) {
                long[] values = new long[4];
                values[0] = controller.getIdleTimeCounter().getCountLocked(which);
                values[1] = controller.getRxTimeCounter().getCountLocked(which);
                long sum = 0;
                for (BatteryStats.LongCounter counter : controller.getTxTimeCounters()) {
                    sum += counter.getCountLocked(which);
                }
                values[2] = sum;
                values[3] = controller.getPowerCounter().getCountLocked(which);
                addLongArray(3, values, this.mLongArrays);
            }
            BatteryStats.ControllerActivityCounter controller2 = uid.getModemControllerActivity();
            if (controller2 != null) {
                long[] values2 = new long[4];
                values2[0] = controller2.getIdleTimeCounter().getCountLocked(which);
                values2[1] = controller2.getRxTimeCounter().getCountLocked(which);
                long sum2 = 0;
                for (BatteryStats.LongCounter counter2 : controller2.getTxTimeCounters()) {
                    sum2 += counter2.getCountLocked(which);
                }
                values2[2] = sum2;
                values2[3] = controller2.getPowerCounter().getCountLocked(which);
                addLongArray(5, values2, this.mLongArrays);
            }
            BatteryStats.Timer flashlightTurnedOnTimer = uid.getFlashlightTurnedOnTimer();
            if (flashlightTurnedOnTimer != null) {
                totalTimeLocked = flashlightTurnedOnTimer.getTotalTimeLocked(elapsedRealtimeMs * 1000, which);
            } else {
                totalTimeLocked = 0;
            }
            addLong(14, totalTimeLocked, this.mLongs);
            BatteryStats.Timer cameraTurnedOnTimer = uid.getCameraTurnedOnTimer();
            if (cameraTurnedOnTimer != null) {
                j = cameraTurnedOnTimer.getTotalTimeLocked(elapsedRealtimeMs * 1000, which);
            }
            addLong(15, j, this.mLongs);
            addLong(28, uid.getNetworkActivityBytes(0, which), this.mLongs);
            addLong(29, uid.getNetworkActivityBytes(1, which), this.mLongs);
            addLong(34, uid.getNetworkActivityPackets(0, which), this.mLongs);
            addLong(35, uid.getNetworkActivityPackets(1, which), this.mLongs);
            addLong(41, uid.getMobileRadioActiveCount(which), this.mLongs);
            addLong(42, uid.getMobileRadioActiveTime(which), this.mLongs);
            addLong(30, uid.getNetworkActivityBytes(2, which), this.mLongs);
            addLong(31, uid.getNetworkActivityBytes(3, which), this.mLongs);
            addLong(36, uid.getNetworkActivityPackets(2, which), this.mLongs);
            addLong(37, uid.getNetworkActivityPackets(3, which), this.mLongs);
            addLong(6, uid.getWifiRunningTime(elapsedRealtimeMs * 1000, which), this.mLongs);
            addLong(7, uid.getFullWifiLockTime(elapsedRealtimeMs * 1000, which), this.mLongs);
            addLong(8, uid.getWifiScanCount(which), this.mLongs);
            addLong(9, uid.getWifiScanTime(elapsedRealtimeMs * 1000, which), this.mLongs);
            addLong(10, uid.getWifiMulticastTime(elapsedRealtimeMs * 1000, which), this.mLongs);
            long[] times = new long[5];
            int bin = 0;
            while (bin < 5) {
                times[bin] = uid.getWifiBatchedScanTime(bin, elapsedRealtimeMs * 1000, which);
                bin++;
                controller2 = controller2;
                flashlightTurnedOnTimer = flashlightTurnedOnTimer;
            }
            addLongArray(11, times, this.mLongArrays);
            addLong(43, uid.getUserCpuTimeUs(which), this.mLongs);
            addLong(44, uid.getSystemCpuTimeUs(which), this.mLongs);
            if (powerProfile != null) {
                int cluster = powerProfile.getNumCpuClusters();
                this.mCpuClusterSpeedTimesUs = new long[cluster];
                for (int i2 = 0; i2 < cluster; i2++) {
                    int step = powerProfile.getNumSpeedStepsInCpuCluster(i2);
                    this.mCpuClusterSpeedTimesUs[i2] = new long[step];
                    for (int j2 = 0; j2 < step; j2++) {
                        this.mCpuClusterSpeedTimesUs[i2][j2] = uid.getTimeAtCpuSpeed(i2, j2, which);
                    }
                }
            }
            addLong(45, uid.getCpuActiveTime(), this.mLongs);
            addLongArray(46, uid.getCpuClusterTimes(), this.mLongArrays);
        }
        return this;
    }

    private void addTimer(int key, BatteryStats.Timer timer, ArrayMap<Integer, TimerStat> map, long elapsedRealtimeMs, int which) {
        TimerStat timerStat = new TimerStat(0, 0L);
        if (timer != null) {
            timerStat.setCount(timer.getCountLocked(which));
            timerStat.setTime(timer.getTotalTimeLocked(1000 * elapsedRealtimeMs, which));
        }
        if (map == null) {
            map = new ArrayMap<>(1);
        }
        String name = "Unknown Timer";
        if (map == this.mTimers) {
            name = intKeyToStr(key);
        } else if (map == this.mWakeTimers) {
            name = intKeyToWakeStr(key);
        } else if (map == this.mSensorMap) {
            name = intKeyToSensorStr(key);
        }
        if (this.DEBUG) {
            Log.d(TAG, name + " count:" + timerStat.getCount() + " time:" + timerStat.getTime());
        }
        map.put(Integer.valueOf(key), timerStat);
    }

    private void addTimer(String key, BatteryStats.Timer timer, ArrayMap<String, TimerStat> map, long elapsedRealtimeMs, int which) {
        TimerStat timerStat = new TimerStat(0, 0L);
        if (timer != null) {
            timerStat.setCount(timer.getCountLocked(which));
            timerStat.setTime(timer.getTotalTimeLocked(1000 * elapsedRealtimeMs, which));
        }
        if (map == null) {
            map = new ArrayMap<>(1);
        }
        if (this.DEBUG) {
            Log.d(TAG, key + " count:" + timerStat.getCount() + " time:" + timerStat.getTime());
        }
        map.put(key, timerStat);
    }

    private void addJob(String name, SparseIntArray value, ArrayMap<String, ArrayMap<Integer, Integer>> map) {
        ArrayMap<Integer, Integer> aMap = new ArrayMap<>();
        if (value != null) {
            int NT = value.size();
            if (this.DEBUG) {
                Log.d(TAG, "Job size: " + NT);
            }
            for (int it = 0; it < NT; it++) {
                aMap.put(Integer.valueOf(value.keyAt(it)), Integer.valueOf(value.valueAt(it)));
                if (this.DEBUG) {
                    Log.d(TAG, "Job Map" + it + ": " + value.keyAt(it) + "-" + value.valueAt(it));
                }
            }
        }
        if (map == null) {
            map = new ArrayMap<>(1);
        }
        map.put(name, aMap);
    }

    public void addPkg(BatteryStats.Uid.Pkg pkg, int which) {
        long totalAlarmCounts = 0;
        for (Map.Entry<String, ? extends BatteryStats.Counter> entry : pkg.getWakeupAlarmStats().entrySet()) {
            BatteryStats.Counter counter = (BatteryStats.Counter) entry.getValue();
            if (counter != null) {
                totalAlarmCounts += counter.getCountLocked(which);
            }
        }
        addLong(2, totalAlarmCounts, this.mLongs);
    }

    private void addLongArray(int key, long[] values, ArrayMap<Integer, long[]> map) {
        if (map == null) {
            map = new ArrayMap<>(1);
        }
        if (this.DEBUG) {
            Log.d(TAG, intKeyToStr(key) + " : " + Arrays.toString(values));
        }
        map.put(Integer.valueOf(key), values);
    }

    private void addLong(int key, long value, ArrayMap<Integer, Long> map) {
        if (map == null) {
            map = new ArrayMap<>();
        }
        if (this.DEBUG) {
            Log.d(TAG, intKeyToStr(key) + " : " + value);
        }
        map.put(Integer.valueOf(key), Long.valueOf(value));
    }

    private static ArrayMap<Integer, Long> createParcelableLongsMap(Parcel in) {
        int size = in.readInt();
        ArrayMap<Integer, Long> result = new ArrayMap<>(size);
        for (int i = 0; i < size; i++) {
            result.put(Integer.valueOf(in.readInt()), Long.valueOf(in.readLong()));
        }
        return result;
    }

    private static void writeParcelableLongsMap(Parcel out, ArrayMap<Integer, Long> map) {
        int size = map.size();
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeInt(map.keyAt(i).intValue());
            out.writeLong(map.valueAt(i).longValue());
        }
    }

    private static ArrayMap<Integer, long[]> createParcelableLongArrayMap(Parcel in) {
        int size = in.readInt();
        ArrayMap<Integer, long[]> result = new ArrayMap<>(size);
        for (int i = 0; i < size; i++) {
            int key = in.readInt();
            int length = in.readInt();
            long[] array = new long[length];
            for (int j = 0; j < length; j++) {
                array[j] = in.readLong();
            }
            result.put(Integer.valueOf(key), array);
        }
        return result;
    }

    private static void writeParcelableLongArrayMap(Parcel out, ArrayMap<Integer, long[]> map) {
        int size = map.size();
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeInt(map.keyAt(i).intValue());
            long[] array = map.valueAt(i);
            int length = array == null ? 0 : array.length;
            out.writeInt(length);
            for (int j = 0; j < length; j++) {
                out.writeLong(array[j]);
            }
        }
    }

    private static <T extends Parcelable> ArrayMap<Integer, T> createIntParcelableMap(Parcel in, Parcelable.Creator<T> creator) {
        int size = in.readInt();
        ArrayMap<Integer, T> result = new ArrayMap<>(size);
        for (int i = 0; i < size; i++) {
            result.put(Integer.valueOf(in.readInt()), creator.createFromParcel(in));
        }
        return result;
    }

    private static <T extends Parcelable> ArrayMap<String, T> createParcelableMap(Parcel in, Parcelable.Creator<T> creator) {
        int size = in.readInt();
        ArrayMap<String, T> result = new ArrayMap<>(size);
        for (int i = 0; i < size; i++) {
            result.put(in.readString(), creator.createFromParcel(in));
        }
        return result;
    }

    private static <T extends Parcelable> void writeIntParcelableMap(Parcel out, ArrayMap<Integer, T> map) {
        int size = map.size();
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeInt(map.keyAt(i).intValue());
            map.valueAt(i).writeToParcel(out, 0);
        }
    }

    private static <T extends Parcelable> void writeParcelableMap(Parcel out, ArrayMap<String, T> map) {
        int size = map.size();
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeString(map.keyAt(i));
            map.valueAt(i).writeToParcel(out, 0);
        }
    }

    private static ArrayMap<String, ArrayMap<Integer, Integer>> createParcelableMapMap(Parcel in) {
        int mapSize = in.readInt();
        ArrayMap<String, ArrayMap<Integer, Integer>> result = new ArrayMap<>(mapSize);
        for (int j = 0; j < mapSize; j++) {
            String jobName = in.readString();
            int numTypes = in.readInt();
            if (numTypes > 0) {
                ArrayMap<Integer, Integer> types = new ArrayMap<>(numTypes);
                for (int k = 0; k < numTypes; k++) {
                    int type = in.readInt();
                    int count = in.readInt();
                    types.put(Integer.valueOf(type), Integer.valueOf(count));
                }
                result.put(jobName, types);
            }
        }
        return result;
    }

    private static void writeParcelableMapMap(Parcel out, ArrayMap<String, ArrayMap<Integer, Integer>> map) {
        int NJC = map.size();
        out.writeInt(NJC);
        for (int ijc = 0; ijc < NJC; ijc++) {
            out.writeString(map.keyAt(ijc));
            ArrayMap<Integer, Integer> types = map.valueAt(ijc);
            int NT = types.size();
            out.writeInt(NT);
            for (int it = 0; it < NT; it++) {
                out.writeInt(types.keyAt(it).intValue());
                out.writeInt(types.valueAt(it).intValue());
            }
        }
    }

    private long[][] createParcelableCpuClusterSpeedTimes(Parcel in) {
        int cluster = in.readInt();
        long[][] result = new long[cluster];
        for (int i = 0; i < cluster; i++) {
            int step = in.readInt();
            result[i] = new long[step];
            for (int j = 0; j < step; j++) {
                result[i][j] = in.readLong();
            }
        }
        return result;
    }

    private void writeParcelableCpuClusterSpeedTimes(Parcel out, long[][] cpuClusterSpeedTimesUs) {
        int cluster = cpuClusterSpeedTimesUs.length;
        out.writeInt(cluster);
        for (long[] speedTimes : cpuClusterSpeedTimesUs) {
            int step = speedTimes.length;
            out.writeInt(step);
            for (long speedTime : speedTimes) {
                out.writeLong(speedTime);
            }
        }
    }

    public long getUserCpuTimeUs() {
        if (this.mLongs.containsKey(43)) {
            return this.mLongs.get(43).longValue();
        }
        return 0L;
    }

    public long getSystemCpuTimeUs() {
        if (this.mLongs.containsKey(44)) {
            return this.mLongs.get(44).longValue();
        }
        return 0L;
    }

    public long getTimeAtCpuSpeed(int cluster, int step) {
        long[][] jArr = this.mCpuClusterSpeedTimesUs;
        if (cluster >= jArr.length) {
            return 0L;
        }
        long[] jArr2 = jArr[cluster];
        if (step < jArr2.length) {
            return jArr2[step];
        }
        return 0L;
    }

    public long getCpuActiveTime() {
        if (this.mLongs.containsKey(45)) {
            return this.mLongs.get(45).longValue();
        }
        return 0L;
    }

    public long[] getCpuClusterTimes() {
        return this.mLongArrays.get(46);
    }

    public TimerStat getWakelockTimer(int type) {
        if (this.mWakeTimers.containsKey(Integer.valueOf(type))) {
            return this.mWakeTimers.get(Integer.valueOf(type));
        }
        return new TimerStat(0, 0L);
    }

    public long getTotalWakelockTimeMs() {
        if (this.mLongs.containsKey(0)) {
            return this.mLongs.get(0).longValue();
        }
        return 0L;
    }

    public long getBackgroundWakelockTimeMs() {
        if (this.mLongs.containsKey(1)) {
            return this.mLongs.get(1).longValue();
        }
        return 0L;
    }

    public ArrayMap<String, TimerStat> getJobMap() {
        return this.mJobMap;
    }

    public ArrayMap<String, ArrayMap<Integer, Integer>> getJobCompletionMap() {
        return this.mJobCompletionMap;
    }

    public ArrayMap<String, TimerStat> getSyncMap() {
        return this.mSyncMap;
    }

    public long getAlarmCounts() {
        if (this.mLongs.containsKey(2)) {
            return this.mLongs.get(2).longValue();
        }
        return 0L;
    }

    public ArrayMap<Integer, TimerStat> getSensorMap() {
        return this.mSensorMap;
    }

    public long getScreenBrightnessTime(int brightnessBin) {
        if (this.mLongArrays.get(40) == null || !this.mLongArrays.containsKey(40)) {
            return 0L;
        }
        return this.mLongArrays.get(40)[brightnessBin];
    }

    public long getFlashlightTurnedOnTimeUs() {
        if (this.mLongs.containsKey(14)) {
            return this.mLongs.get(14).longValue();
        }
        return 0L;
    }

    public long getCameraTurnedOnTimeUs() {
        if (this.mLongs.containsKey(15)) {
            return this.mLongs.get(15).longValue();
        }
        return 0L;
    }

    public long[] getMobileBytes() {
        if (this.mLongs.containsKey(28) && this.mLongs.containsKey(29)) {
            return new long[]{this.mLongs.get(28).longValue(), this.mLongs.get(29).longValue()};
        }
        return new long[]{0, 0};
    }

    public long[] getMobilePackets() {
        if (this.mLongs.containsKey(34) && this.mLongs.containsKey(35)) {
            return new long[]{this.mLongs.get(34).longValue(), this.mLongs.get(35).longValue()};
        }
        return new long[]{0, 0};
    }

    public TimerStat getMobileRadioActive() {
        if (this.mLongs.containsKey(41) && this.mLongs.containsKey(42)) {
            return new TimerStat(Math.toIntExact(this.mLongs.get(41).longValue()), this.mLongs.get(42).longValue());
        }
        return new TimerStat(0, 0L);
    }

    public long[] getWifiBytes() {
        if (this.mLongs.containsKey(30) && this.mLongs.containsKey(31)) {
            return new long[]{this.mLongs.get(30).longValue(), this.mLongs.get(31).longValue()};
        }
        return new long[]{0, 0};
    }

    public long[] getWifiPackets() {
        if (this.mLongs.containsKey(36) && this.mLongs.containsKey(37)) {
            return new long[]{this.mLongs.get(36).longValue(), this.mLongs.get(37).longValue()};
        }
        return new long[]{0, 0};
    }

    public long getWifiRunningTime() {
        if (this.mLongs.containsKey(6)) {
            return this.mLongs.get(6).longValue();
        }
        return 0L;
    }

    public TimerStat getWifiScanTime() {
        int count = 0;
        long time = 0;
        if (this.mLongs.get(8) != null) {
            count = Math.toIntExact(this.mLongs.get(8).longValue());
        }
        if (this.mLongs.get(9) != null) {
            time = this.mLongs.get(9).longValue();
        }
        return new TimerStat(count, time);
    }

    public long getWifiBatchedScanTime(int bin) {
        if (this.mLongArrays.get(11) == null) {
            return 0L;
        }
        return this.mLongArrays.get(11)[bin];
    }

    private String intKeyToStr(int key) {
        switch (key) {
            case 0:
                return "TIME_MS_TOTAL_WAKELOCK";
            case 1:
                return "TIME_MS_BACKGROUND_WAKELOCK";
            case 2:
                return "COUNT_WAKEUP_ALARMS";
            case 3:
                return "CONTROLLER_WIFI";
            case 4:
                return "CONTROLLER_BLUETOOTH";
            case 5:
                return "CONTROLLER_MODEM";
            case 6:
                return "TIME_MS_WIFI_RUNNING";
            case 7:
                return "TIME_MS_WIFI_FULL_LOCK";
            case 8:
                return "COUNT_WIFI_SCAN";
            case 9:
                return "TIME_US_WIFI_SCAN";
            case 10:
                return "TIME_MS_WIFI_MULTICAST";
            case 11:
                return "TIME_MS_WIFI_BATCHED_SCAN";
            case 12:
                return "TIMER_AUDIO";
            case 13:
                return "TIMER_VIDEO";
            case 14:
                return "TIME_US_FLASHLIGHT";
            case 15:
                return "TIME_US_CAMERA";
            case 16:
                return "TIMER_FOREGROUND_ACTIVITY";
            case 17:
                return "TIMER_BLUETOOTH_SCAN";
            case 18:
                return "TIME_MS_PROCESS_STATE_TOP";
            case 19:
                return "TIME_MS_PROCESS_STATE_FOREGROUND_SERVICE";
            case 20:
                return "TIME_MS_PROCESS_STATE_TOP_SLEEPING";
            case 21:
                return "TIME_MS_PROCESS_STATE_FOREGROUND";
            case 22:
                return "TIME_MS_PROCESS_STATE_BACKGROUND";
            case 23:
                return "TIME_MS_PROCESS_STATE_CACHED";
            case 24:
                return "TIMER_VIBRATOR";
            case 25:
                return "COUNT_OTHER_USER_ACTIVITY";
            case 26:
                return "COUNT_BUTTON_USER_ACTIVITY";
            case 27:
                return "COUNT_TOUCH_USER_ACTIVITY";
            case 28:
                return "MOBILE_RX_BYTES";
            case 29:
                return "MOBILE_TX_BYTES";
            case 30:
                return "WIFI_RX_BYTES";
            case 31:
                return "WIFI_TX_BYTES";
            case 32:
                return "BLUETOOTH_RX_BYTES";
            case 33:
                return "BLUETOOTH_TX_BYTES";
            case 34:
                return "MOBILE_RX_PACKETS";
            case 35:
                return "MOBILE_TX_PACKETS";
            case 36:
                return "WIFI_RX_PACKETS";
            case 37:
                return "WIFI_TX_PACKETS";
            case 38:
                return "BLUETOOTH_RX_PACKETS";
            case 39:
                return "BLUETOOTH_TX_PACKETS";
            case 40:
                return "BINS_SCREEN_BRIGHTNESS";
            case 41:
                return "COUNT_MOBILE_RADIO_ACTIVE";
            case 42:
                return "TIME_US_MOBILE_RADIO_ACTIVE";
            case 43:
                return "TIME_US_USER_CPU";
            case 44:
                return "TIME_US_SYSTEM_CPU";
            case 45:
                return "TIME_MS_CPU_ACTIVE";
            case 46:
                return "TIME_MS_CPU_CLUSTER";
            default:
                return "UNKNOWN " + key;
        }
    }

    private String intKeyToWakeStr(int key) {
        switch (key) {
            case 0:
                return "WAKE_TYPE_PARTIAL";
            case 1:
                return "WAKE_TYPE_FULL";
            case 2:
                return "WAKE_TYPE_WINDOW";
            case 18:
                return "WAKE_TYPE_DRAW";
            default:
                return "WAKE_TYPE_UNKNOWN " + key;
        }
    }

    private String intKeyToSensorStr(int key) {
        if (key == -10000) {
            return "GPS";
        }
        return "SENSOR " + key;
    }
}
