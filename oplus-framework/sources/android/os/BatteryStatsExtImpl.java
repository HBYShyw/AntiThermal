package android.os;

import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.os.BatteryStats;
import android.os.IBatteryStatsExt;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.LongSparseArray;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class BatteryStatsExtImpl implements IBatteryStatsExt {
    private static final long MAX_BUCKET_TIME_MS = 3000;
    private static final String TAG = "BatteryStatsExtImpl";
    private HashSet<Long> mHistoryItemsKeySet;
    private LongSparseArray<ArrayList<BatteryStats.HistoryItem>> mHistoryItemsMap;
    private long mLastStoreHistoryItemTime;
    private boolean mShouldSortItems = true;
    protected Context mContext = null;
    private IOplusDailyBattProtoManager mOplusDailyBattProtoManager = null;

    public BatteryStatsExtImpl(Object base) {
        Log.w(TAG, "BatteryStatsExtImpl is inited");
    }

    public void setContext(Context context) {
        this.mContext = context;
        IOplusDailyBattProtoManager iOplusDailyBattProtoManager = this.mOplusDailyBattProtoManager;
        if (iOplusDailyBattProtoManager != null) {
            iOplusDailyBattProtoManager.init(context);
        } else {
            Log.w(TAG, "mOplusDailyBattProtoManager is null when setContext");
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    protected void setInBatteryStatsImplInstance(BatteryStats impl) {
        Log.d(TAG, "init local BatteryStats instance...");
        this.mOplusDailyBattProtoManager = (IOplusDailyBattProtoManager) OplusFrameworkFactory.getInstance().getFeature(IOplusDailyBattProtoManager.DEFAULT, new Object[]{impl});
    }

    public void setInBatteryStatsImplInstance(Object impl) {
        Log.d(TAG, "init local BatteryStats instance with " + impl);
        this.mOplusDailyBattProtoManager = (IOplusDailyBattProtoManager) OplusFrameworkFactory.getInstance().getFeature(IOplusDailyBattProtoManager.DEFAULT, new Object[]{(BatteryStats) impl});
    }

    public void registerRomUpdate() {
        IOplusDailyBattProtoManager iOplusDailyBattProtoManager = this.mOplusDailyBattProtoManager;
        if (iOplusDailyBattProtoManager != null) {
            iOplusDailyBattProtoManager.registerRomUpdate();
        } else {
            Log.w(TAG, "mOplusDailyBattProtoManager is null when registerRomUpdate");
        }
    }

    public void reportDailyProto() {
        IOplusDailyBattProtoManager iOplusDailyBattProtoManager = this.mOplusDailyBattProtoManager;
        if (iOplusDailyBattProtoManager != null) {
            iOplusDailyBattProtoManager.reportDailyProto();
        } else {
            Log.w(TAG, "mOplusDailyBattProtoManager is null when reportDailyProto");
        }
    }

    public int[] noteConnectivityChangedLocked(int type, String extra, int phoneDataConnectionType, int modStepMode, int curStepMode) {
        IOplusDailyBattProtoManager iOplusDailyBattProtoManager = this.mOplusDailyBattProtoManager;
        if (iOplusDailyBattProtoManager != null) {
            return iOplusDailyBattProtoManager.noteConnectivityChangedLocked(type, extra, phoneDataConnectionType, modStepMode, curStepMode);
        }
        Log.w(TAG, "mOplusDailyBattProtoManager is null when noteConnectivityChangedLocked");
        return new int[]{modStepMode, curStepMode};
    }

    public int[] notePhoneDataConnectionStateLocked(int dataType, boolean hasData, int bin, int modStepMode, int curStepMode) {
        IOplusDailyBattProtoManager iOplusDailyBattProtoManager = this.mOplusDailyBattProtoManager;
        if (iOplusDailyBattProtoManager != null) {
            return iOplusDailyBattProtoManager.notePhoneDataConnectionStateLocked(dataType, hasData, bin, modStepMode, curStepMode);
        }
        Log.w(TAG, "mOplusDailyBattProtoManager is null when notePhoneDataConnectionStateLocked");
        return new int[]{modStepMode, curStepMode};
    }

    public boolean shouldSortItemsLocked() {
        Log.d(TAG, "shouldSortItemsLocked: " + this.mShouldSortItems);
        return this.mShouldSortItems;
    }

    public void setSortItemsLocked(boolean should) {
        Log.d(TAG, "setSortItemsLocked: " + should);
        this.mShouldSortItems = should;
    }

    public void prepareStoreAndDumpItemLocked() {
        Log.d(TAG, "prepareStoreAndDumpItemLocked");
        this.mHistoryItemsKeySet = new HashSet<>();
        this.mHistoryItemsMap = new LongSparseArray<>();
        this.mLastStoreHistoryItemTime = -1L;
    }

    public void storeHistoryItemLocked(PrintWriter pw, BatteryStats.HistoryItem rec, long baseTime, boolean checkin, boolean verbose, BatteryStats.HistoryPrinter hprinter) {
        long currentTime = rec.currentTime;
        long j = this.mLastStoreHistoryItemTime;
        if (j != -1 && currentTime - j > MAX_BUCKET_TIME_MS) {
            printStoredHistoryItemLocked(pw, baseTime, checkin, verbose, hprinter);
        }
        this.mLastStoreHistoryItemTime = currentTime;
        BatteryStats.HistoryItem copyHistoryItem = new BatteryStats.HistoryItem();
        copyHistoryItem.setTo(rec);
        this.mHistoryItemsKeySet.add(Long.valueOf(currentTime));
        ArrayList<BatteryStats.HistoryItem> historyItems = this.mHistoryItemsMap.get(currentTime);
        if (historyItems == null) {
            historyItems = new ArrayList<>(1);
            this.mHistoryItemsMap.put(currentTime, historyItems);
        }
        historyItems.add(copyHistoryItem);
    }

    public void printStoredHistoryItemLocked(PrintWriter pw, long baseTime, boolean checkin, boolean verbose, BatteryStats.HistoryPrinter hprinter) {
        ArrayList<Long> keyList = new ArrayList<>(this.mHistoryItemsKeySet);
        Collections.sort(keyList);
        Iterator<Long> it = keyList.iterator();
        while (it.hasNext()) {
            Long key = it.next();
            ArrayList<BatteryStats.HistoryItem> historyItems = this.mHistoryItemsMap.get(key.longValue());
            if (historyItems != null) {
                for (int i = 0; i < historyItems.size(); i++) {
                    hprinter.printNextItem(pw, historyItems.get(i), baseTime, checkin, verbose);
                }
            }
        }
        this.mHistoryItemsKeySet.clear();
        this.mHistoryItemsMap.clear();
    }

    public void finishStoreAndDumpItemLocked() {
        Log.d(TAG, "finishStoreAndDumpItemLocked");
        this.mHistoryItemsKeySet = null;
        this.mHistoryItemsMap = null;
        this.mLastStoreHistoryItemTime = -1L;
    }

    /* loaded from: classes.dex */
    public static final class ThermalItem implements Parcelable {
        public static final byte CMD_AUDIOONOFF = 11;
        public static final byte CMD_BACKLIGHTINFO = 3;
        public static final byte CMD_BAT_INFO = 1;
        public static final byte CMD_CAMEARAONOFF = 10;
        public static final byte CMD_COMMON_UPDATE = 26;
        public static final byte CMD_CONNECTNETTYPE = 9;
        public static final byte CMD_ENVITEMP = 24;
        public static final byte CMD_FLASHLIGHTONOFF = 14;
        public static final byte CMD_FOREPRCINFO = 17;
        public static final byte CMD_GPSONOFF = 13;
        public static final byte CMD_JOBINFO = 15;
        public static final byte CMD_NETSTATE = 8;
        public static final byte CMD_NETSYNCINFO = 16;
        public static final byte CMD_NULL = 0;
        public static final byte CMD_PHONE_ONFF = 5;
        public static final byte CMD_PHONE_SIGNAL = 7;
        public static final byte CMD_PHONE_STATE = 6;
        public static final byte CMD_RESET = 19;
        public static final byte CMD_TEMPINFO = 2;
        public static final byte CMD_THERMALRATIO = 20;
        public static final byte CMD_THERMALRATIO1 = 21;
        public static final byte CMD_THERMALRATIO2 = 22;
        public static final byte CMD_THERMALRATIO3 = 23;
        public static final byte CMD_TOPPROCINFO = 18;
        public static final byte CMD_UPDATE_TIME = 25;
        public static final byte CMD_VIDEOONOFF = 12;
        public static final byte CMD_WIFIINFO = 4;
        public static final int CONNECT_MOBILE = 0;
        public static final int CONNECT_NONE = -1;
        public static final int CONNECT_WIFI = 1;
        public static final int INVALID_DATA = -1023;
        public static final byte NETWORK_CLASS_2_G = 2;
        public static final byte NETWORK_CLASS_3_G = 3;
        public static final byte NETWORK_CLASS_4_G = 4;
        public static final byte NETWORK_CLASS_UNKNOWN = 0;
        public static final byte NETWORK_CLASS_WIFI = 1;
        public static final int WIFI_OFF = 0;
        public static final int WIFI_ON = 1;
        public static final int WIFI_RUN = 2;
        public static final int WIFI_STOP = 3;
        public boolean audioOn;
        public int backlight;
        public long baseElapsedRealtime;
        public int batPercent;
        public int batRm;
        public int batTemp;
        public boolean cameraOn;
        public int chargePlug;
        public byte connectNetType;
        public int cpuLoading;
        public long currentTime;
        public boolean dataNetStatus;
        public long elapsedRealtime;
        public boolean flashlightOn;
        public boolean gpsOn;
        public boolean isAutoBrightness;
        public ThermalItem next;
        public int numReadInts;
        public boolean phoneOnff;
        public byte phoneSignal;
        public byte phoneState;
        public byte thermalRatio;
        public byte thermalRatio1;
        public byte thermalRatio2;
        public byte thermalRatio3;
        public int topCpu;
        public long upTime;
        public String versionName;
        public boolean videoOn;
        public int volume;
        public int wifiSignal;
        public int wifiStats;
        public byte cmd = 0;
        public int phoneTemp = INVALID_DATA;
        public int phoneTemp1 = INVALID_DATA;
        public int phoneTemp2 = INVALID_DATA;
        public int phoneTemp3 = INVALID_DATA;
        public int enviTemp = INVALID_DATA;
        public String jobSchedule = "null";
        public String netSync = "null";
        public String foreProc = "null";
        public String topProc = "null";

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.cmd);
            dest.writeLong(this.currentTime);
            dest.writeLong(this.elapsedRealtime);
            dest.writeLong(this.upTime);
            dest.writeLong(this.baseElapsedRealtime);
            dest.writeInt(this.batRm);
            dest.writeInt(this.batTemp);
            dest.writeInt(this.phoneTemp);
            dest.writeInt(this.phoneTemp1);
            dest.writeInt(this.phoneTemp2);
            dest.writeInt(this.phoneTemp3);
            dest.writeByte(this.thermalRatio);
            dest.writeByte(this.thermalRatio1);
            dest.writeByte(this.thermalRatio2);
            dest.writeByte(this.thermalRatio3);
            dest.writeInt(this.enviTemp);
            dest.writeInt(this.batPercent);
            dest.writeInt(this.chargePlug);
            dest.writeInt(this.backlight);
            dest.writeInt(this.volume);
            dest.writeInt(this.wifiStats);
            dest.writeInt(this.wifiSignal);
            dest.writeBoolean(this.phoneOnff);
            dest.writeByte(this.phoneState);
            dest.writeByte(this.phoneSignal);
            dest.writeBoolean(this.dataNetStatus);
            dest.writeByte(this.connectNetType);
            dest.writeBoolean(this.cameraOn);
            dest.writeBoolean(this.audioOn);
            dest.writeBoolean(this.videoOn);
            dest.writeBoolean(this.gpsOn);
            dest.writeBoolean(this.flashlightOn);
            dest.writeString(this.jobSchedule);
            dest.writeString(this.netSync);
            dest.writeString(this.foreProc);
            dest.writeString(this.topProc);
            dest.writeInt(this.cpuLoading);
            dest.writeInt(this.topCpu);
        }

        public void readFromParcel(Parcel src) {
            int start = src.dataPosition();
            this.cmd = src.readByte();
            this.currentTime = src.readLong();
            this.elapsedRealtime = src.readLong();
            this.upTime = src.readLong();
            this.batRm = src.readInt();
            this.batTemp = src.readInt();
            this.phoneTemp = src.readInt();
            this.phoneTemp1 = src.readInt();
            this.phoneTemp2 = src.readInt();
            this.phoneTemp3 = src.readInt();
            this.thermalRatio = src.readByte();
            this.thermalRatio1 = src.readByte();
            this.thermalRatio2 = src.readByte();
            this.thermalRatio3 = src.readByte();
            this.enviTemp = src.readInt();
            this.batPercent = src.readInt();
            this.chargePlug = src.readInt();
            this.backlight = src.readInt();
            this.volume = src.readInt();
            this.wifiStats = src.readInt();
            this.wifiSignal = src.readInt();
            this.phoneOnff = src.readBoolean();
            this.phoneState = src.readByte();
            this.phoneSignal = src.readByte();
            this.dataNetStatus = src.readBoolean();
            this.connectNetType = src.readByte();
            this.cameraOn = src.readBoolean();
            this.audioOn = src.readBoolean();
            this.videoOn = src.readBoolean();
            this.gpsOn = src.readBoolean();
            this.flashlightOn = src.readBoolean();
            this.jobSchedule = src.readString();
            this.netSync = src.readString();
            this.foreProc = src.readString();
            this.topProc = src.readString();
            this.cpuLoading = src.readInt();
            this.topCpu = src.readInt();
            this.numReadInts += (src.dataPosition() - start) / 4;
        }

        public void clear() {
            this.cmd = (byte) 0;
            this.currentTime = -1L;
            this.elapsedRealtime = -1L;
            this.upTime = -1L;
            this.batRm = -1;
            this.batTemp = -1;
            this.phoneTemp = INVALID_DATA;
            this.phoneTemp1 = INVALID_DATA;
            this.phoneTemp2 = INVALID_DATA;
            this.phoneTemp3 = INVALID_DATA;
            this.thermalRatio = (byte) -127;
            this.thermalRatio1 = (byte) -127;
            this.thermalRatio2 = (byte) -127;
            this.thermalRatio3 = (byte) -127;
            this.enviTemp = INVALID_DATA;
            this.batPercent = -1;
            this.chargePlug = -1;
            this.backlight = -1;
            this.volume = 0;
            this.wifiStats = -1;
            this.wifiSignal = -1;
            this.phoneState = (byte) -1;
            this.phoneOnff = false;
            this.phoneSignal = (byte) 0;
            this.dataNetStatus = false;
            this.connectNetType = (byte) 0;
            this.cameraOn = false;
            this.audioOn = false;
            this.videoOn = false;
            this.gpsOn = false;
            this.flashlightOn = false;
            this.jobSchedule = "null";
            this.netSync = "null";
            this.foreProc = "null";
            this.topProc = "null";
            this.cpuLoading = 0;
            this.topCpu = 0;
            this.isAutoBrightness = false;
        }

        public void setTo(ThermalItem o) {
            setToCommon(o);
        }

        private void setToCommon(ThermalItem o) {
            this.cmd = o.cmd;
            this.currentTime = o.currentTime;
            this.elapsedRealtime = o.elapsedRealtime;
            this.upTime = o.upTime;
            this.baseElapsedRealtime = o.baseElapsedRealtime;
            this.batRm = o.batRm;
            this.batTemp = o.batTemp;
            this.phoneTemp = o.phoneTemp;
            this.phoneTemp1 = o.phoneTemp1;
            this.phoneTemp2 = o.phoneTemp2;
            this.phoneTemp3 = o.phoneTemp3;
            this.thermalRatio = o.thermalRatio;
            this.thermalRatio1 = o.thermalRatio1;
            this.thermalRatio2 = o.thermalRatio2;
            this.thermalRatio3 = o.thermalRatio3;
            this.enviTemp = o.enviTemp;
            this.batPercent = o.batPercent;
            this.chargePlug = o.chargePlug;
            this.backlight = o.backlight;
            this.volume = o.volume;
            this.wifiStats = o.wifiStats;
            this.wifiSignal = o.wifiSignal;
            this.phoneOnff = o.phoneOnff;
            this.phoneState = o.phoneState;
            this.phoneSignal = o.phoneSignal;
            this.dataNetStatus = o.dataNetStatus;
            this.connectNetType = o.connectNetType;
            this.cameraOn = o.cameraOn;
            this.audioOn = o.audioOn;
            this.videoOn = o.videoOn;
            this.gpsOn = o.gpsOn;
            this.flashlightOn = o.flashlightOn;
            this.jobSchedule = o.jobSchedule;
            this.netSync = o.netSync;
            this.foreProc = o.foreProc;
            this.versionName = o.versionName;
            this.topProc = o.topProc;
            this.cpuLoading = o.cpuLoading;
            this.topCpu = o.topCpu;
            this.isAutoBrightness = o.isAutoBrightness;
        }

        public boolean same(ThermalItem o) {
            return this.currentTime == o.currentTime && this.elapsedRealtime == o.elapsedRealtime && this.upTime == o.upTime && this.baseElapsedRealtime == o.baseElapsedRealtime && this.batRm == o.batRm && this.batTemp == o.batTemp && this.phoneTemp == o.phoneTemp && this.phoneTemp1 == o.phoneTemp1 && this.phoneTemp2 == o.phoneTemp2 && this.phoneTemp3 == o.phoneTemp3 && this.thermalRatio == o.thermalRatio && this.thermalRatio1 == o.thermalRatio1 && this.thermalRatio2 == o.thermalRatio2 && this.thermalRatio3 == o.thermalRatio3 && this.enviTemp == o.enviTemp && this.batPercent == o.batPercent && this.chargePlug == o.chargePlug && this.backlight == o.backlight && this.volume == o.volume && this.wifiStats == o.wifiStats && this.wifiSignal == o.wifiSignal && this.phoneOnff == o.phoneOnff && this.phoneState == o.phoneState && this.phoneSignal == o.phoneSignal && this.dataNetStatus == o.dataNetStatus && this.connectNetType == o.connectNetType && this.audioOn == o.audioOn && this.videoOn == o.videoOn && this.gpsOn == o.gpsOn && this.flashlightOn == o.flashlightOn && this.jobSchedule == o.jobSchedule && this.netSync == o.netSync && this.foreProc == o.foreProc && this.versionName == o.versionName && this.topProc == o.topProc && this.cpuLoading == o.cpuLoading && this.topCpu == o.topCpu && this.isAutoBrightness == o.isAutoBrightness;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public static class ThermalHistoryPrinter {
        public void printNextItem(PrintWriter pw, ThermalItem rec) {
            StringBuilder sb = new StringBuilder();
            sb.append(DateFormat.format("yyyy-MM-dd-HH-mm-ss", (rec.elapsedRealtime - rec.baseElapsedRealtime) + rec.currentTime).toString());
            sb.append(" ");
            BatteryStatsExtImpl.formatThermalTimeMsNoSpace(sb, rec.elapsedRealtime);
            sb.append(" ");
            BatteryStatsExtImpl.formatThermalTimeMsNoSpace(sb, rec.upTime);
            sb.append(" ");
            sb.append(Integer.toString(rec.batPercent));
            sb.append(" ");
            sb.append(Integer.toString(rec.backlight));
            sb.append(" ");
            sb.append(Integer.toString(rec.volume));
            if (rec.cmd == 0) {
                sb.append(" START RECORD");
                pw.print(sb.toString());
                pw.println();
                return;
            }
            if (rec.cmd == 19) {
                sb.append(" THERMAL HISTORY RESET");
                pw.print(sb.toString());
                pw.println();
                return;
            }
            sb.append(" batTemp=");
            sb.append(Integer.toString(rec.batTemp));
            sb.append(" phoneTemp=");
            sb.append(Integer.toString(rec.phoneTemp));
            sb.append(" thermalRatio=");
            sb.append(Float.toString(rec.thermalRatio / 10.0f));
            if (rec.enviTemp == -1023) {
                sb.append(" enviTemp=");
                sb.append("unknow");
            } else {
                sb.append(" enviTemp=");
                sb.append(Integer.toString(rec.enviTemp));
            }
            sb.append(" batRm=");
            sb.append(Integer.toString(rec.batRm));
            sb.append(" plug=");
            switch (rec.chargePlug) {
                case 0:
                    sb.append("none");
                    break;
                case 1:
                    sb.append("ac");
                    break;
                case 2:
                    sb.append("usb");
                    break;
                case 3:
                default:
                    sb.append("none");
                    break;
                case 4:
                    sb.append("wireless");
                    break;
            }
            sb.append(" wifiStats=");
            sb.append(Integer.toString(rec.wifiStats));
            sb.append(" wifiSignal=");
            sb.append(Integer.toString(rec.wifiSignal));
            sb.append(" phoneOnff=");
            sb.append(Boolean.toString(rec.phoneOnff));
            int simState = (rec.phoneState >> 4) & 15;
            int state = rec.phoneState & ThermalItem.CMD_JOBINFO;
            sb.append(" simState=");
            sb.append(BatteryStatsExtImpl.formatSimState(simState));
            sb.append(" phoneState=");
            sb.append(BatteryStatsExtImpl.formatPhoneState(state));
            sb.append(" phoneSignal=");
            sb.append(" dataNetStatus=");
            sb.append(Boolean.toString(rec.dataNetStatus));
            sb.append(" connectNetType=");
            sb.append(BatteryStatsExtImpl.formatNetType(rec.connectNetType));
            sb.append(" cameraOn=");
            sb.append(Boolean.toString(rec.cameraOn));
            sb.append(" audioOn=");
            sb.append(Boolean.toString(rec.audioOn));
            sb.append(" videoOn=");
            sb.append(Boolean.toString(rec.videoOn));
            sb.append(" gpsOn=");
            sb.append(Boolean.toString(rec.gpsOn));
            sb.append(" flashlightOn=");
            sb.append(Boolean.toString(rec.flashlightOn));
            sb.append(" jobSchedule=");
            sb.append(rec.jobSchedule);
            sb.append(" netSync=");
            sb.append(rec.netSync);
            sb.append(" foreProc=");
            sb.append(rec.foreProc);
            sb.append(" cpuLoading=");
            sb.append(Float.toString(rec.cpuLoading / 10.0f));
            sb.append("%");
            sb.append(" topProc=");
            sb.append(rec.topProc);
            sb.append(" topCpu=");
            sb.append(Float.toString(rec.topCpu / 10.0f));
            sb.append("%");
            sb.append(" isAutoBrightness=");
            sb.append(Boolean.toString(rec.isAutoBrightness));
            sb.append(" version=");
            sb.append(rec.versionName);
            pw.print(sb.toString());
            pw.println();
        }
    }

    private static final void formatThermalTimeRaw(StringBuilder out, long seconds) {
        long days = seconds / 86400;
        if (days != 0) {
            out.append(days);
            out.append("d ");
        }
        long used = days * 60 * 60 * 24;
        long hours = (seconds - used) / 3600;
        if (hours != 0 || used != 0) {
            out.append(hours);
            out.append("h");
        }
        long used2 = used + (hours * 60 * 60);
        long mins = (seconds - used2) / 60;
        if (mins != 0 || used2 != 0) {
            out.append(mins);
            out.append("m");
        }
        long used3 = used2 + (60 * mins);
        if (seconds != 0 || used3 != 0) {
            out.append(seconds - used3);
            out.append("s");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String formatNetType(int netType) {
        switch (netType) {
            case 0:
                return "none";
            case 1:
                return "wifi";
            case 2:
                return "2g";
            case 3:
                return "3g";
            case 4:
                return "4g";
            default:
                return "none";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String formatSimState(int simState) {
        switch (simState) {
            case 0:
                return "unknow";
            case 1:
                return "absent";
            case 2:
                return "pin_required";
            case 3:
                return "puk_required";
            case 4:
                return "network_locked";
            case 5:
                return "ready";
            case 6:
                return "not_ready";
            case 7:
                return "perm_disabled";
            case 8:
                return "card_io_error";
            case 9:
                return "card_restricted";
            default:
                return "unknow";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String formatPhoneState(int state) {
        switch (state) {
            case 0:
                return "in_service";
            case 1:
                return "out_of_service";
            case 2:
                return "emergency_only";
            case 3:
                return "state_power_off";
            default:
                return "out_of_service";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void formatThermalTimeMsNoSpace(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatThermalTimeRaw(sb, sec);
        sb.append(time - (1000 * sec));
        sb.append("ms");
    }

    /* loaded from: classes.dex */
    public static class StaticExtImpl implements IBatteryStatsExt.IStaticExt {
        private StaticExtImpl() {
            Log.w(BatteryStatsExtImpl.TAG, "IBatteryStatsExt.StaticExtImpl is inited");
        }

        public static StaticExtImpl getInstance(Object obj) {
            return LazyHolder.INSTANCE;
        }

        /* loaded from: classes.dex */
        private static class LazyHolder {
            private static final StaticExtImpl INSTANCE = new StaticExtImpl();

            private LazyHolder() {
            }
        }

        public boolean haveNetworkMode(PrintWriter pw, boolean haveModes, long initMode, long modMode) {
            return ifHaveNetworkDetail(pw, haveModes, initMode, modMode) | haveModes;
        }

        private boolean ifHaveNetworkDetail(PrintWriter pw, boolean haveModes, long initMode, long modMode) {
            return IOplusDailyBattProtoManager.ifHaveNetworkDetail(pw, haveModes, initMode, modMode);
        }
    }
}
