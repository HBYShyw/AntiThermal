package android.os;

import android.content.ContentResolver;
import android.content.Context;
import android.net.NetworkInfo;
import android.os.BatteryStats;
import android.os.BatteryStatsExtImpl;
import android.provider.Settings;
import android.service.batterystats.BatteryStatsServiceDumpDailyProto;
import android.service.batterystats.DailyProto;
import android.service.batterystats.OplusBatteryLevelStep;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.MemInfoReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public class OplusDailyBattProtoManager implements IOplusDailyBattProtoManager {
    private static final String APP_ID = "30406";
    private static final String CATEGORY_ID = "DailyBattProto";
    private static final String DAILY_BATT_PROTO_SWITCH = "daily_batt_proto_switch";
    private static final String EVENT_ID = "DailyBattProto";
    private static final String LATEST_DAILY_PROTO_FILE = "/data/system/latest_daily_proto.bin";
    private static final String MDM_DAILY_PROTO = "proto";
    private static final String MDM_DAILY_PROTO_VER = "ver";
    private static final String PROTO_FILTER_NAME = "sys_daily_proto_config";
    public static final int STEP_LEVEL_MODE_DEVICE_IDLE = 8;
    public static final int STEP_LEVEL_MODE_POWER_SAVE = 4;
    public static final int STEP_LEVEL_MODE_SCREEN_STATE = 3;
    private static final String STR_DEEPSLEEP_SWITCH = "oppoguardelf_deepsleep_switch_state";
    private static final String TAG = "OplusDailyBattProtoManager";
    private static final int VALUE_FOUR = 4;
    private static final int VALUE_SIXTEEN = 16;
    private static final String VERSION_CODE = "2.1";
    private static volatile OplusDailyBattProtoManager sInstance;
    private BatteryStats mBatteryStatsImpl;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String FAKE_NETWORK_TYPE_NR_TO = "persist.sys.fake_nr_to";
    private static final int MY_LOCAL_NETWORK_TYPE_NR = SystemProperties.getInt(FAKE_NETWORK_TYPE_NR_TO, 20);
    private Context mContext = null;
    private int mPreiousNetworkType = 0;
    private OplusBaseBatteryStatsRUSHelper mOplusBaseBatteryStatsRUSHelper = null;
    private long mTotalRAM = 0;

    private static void myLog(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static OplusDailyBattProtoManager getInstance(BatteryStats impl) {
        if (sInstance == null) {
            synchronized (OplusDailyBattProtoManager.class) {
                if (sInstance == null) {
                    sInstance = new OplusDailyBattProtoManager(impl);
                }
            }
        }
        return sInstance;
    }

    private OplusDailyBattProtoManager(BatteryStats impl) {
        this.mBatteryStatsImpl = null;
        myLog("OplusDailyBattProtoManager constructed with impl: " + impl);
        this.mBatteryStatsImpl = impl;
        Log.d(TAG, "MY_LOCAL_NETWORK_TYPE_NR: " + MY_LOCAL_NETWORK_TYPE_NR);
    }

    @Override // android.os.IOplusDailyBattProtoManager
    public void init(Context context) {
        myLog("init with [" + context + "]");
        this.mContext = context;
    }

    @Override // android.os.IOplusDailyBattProtoManager
    public void registerRomUpdate() {
        myLog("registerRomUpdate");
        if (this.mContext != null) {
            OplusBaseBatteryStatsRUSHelper oplusBaseBatteryStatsRUSHelper = new OplusBaseBatteryStatsRUSHelper(this.mContext, PROTO_FILTER_NAME);
            this.mOplusBaseBatteryStatsRUSHelper = oplusBaseBatteryStatsRUSHelper;
            oplusBaseBatteryStatsRUSHelper.registerRomUpdate();
            Log.d(TAG, "registerRomUpdate ok");
            return;
        }
        Log.d(TAG, "mContext is still null, can't registerRomUpdate");
    }

    private boolean dumpLatestDailyInfo(PrintWriter pw, ProtoOutputStream proto) {
        boolean z;
        BatteryStats.DailyItem dit;
        int[] outInt;
        myLog("dumpLastestDailyInfo");
        boolean doHaveContent = false;
        if (this.mBatteryStatsImpl == null) {
            Log.d(TAG, "mBatteryStatsImpl is NOT inited");
            return false;
        }
        StringBuilder sb = new StringBuilder(64);
        int[] outInt2 = new int[1];
        BatteryStats.DailyItem dit2 = this.mBatteryStatsImpl.getDailyItemLocked(0);
        if (dit2 == null) {
            z = true;
        } else {
            int i = 0 + 1;
            StringBuilder desc = new StringBuilder();
            desc.append("  Daily from ");
            desc.append(DateFormat.format("yyyy-MM-dd-HH-mm-ss", dit2.mStartTime).toString());
            desc.append(" to ");
            desc.append(DateFormat.format("yyyy-MM-dd-HH-mm-ss", dit2.mEndTime).toString());
            if (pw == null) {
                z = true;
                if (proto != null) {
                    long bToken = proto.start(BatteryStatsServiceDumpDailyProto.DAILYSTATS);
                    proto.write(DailyProto.DESC, desc.toString());
                    proto.write(DailyProto.START_TIME, dit2.mStartTime);
                    proto.write(DailyProto.END_TIME, dit2.mEndTime);
                    dumpDurationSteps(proto, DailyProto.BATTERY_DISCHARGE, dit2.mDischargeSteps);
                    dumpDurationSteps(proto, DailyProto.BATTERY_CHARGE, dit2.mChargeSteps);
                    proto.write(DailyProto.TOTAL_RAM_SIZE, getTotalRAM());
                    proto.write(DailyProto.SLEEP_STANDBY_OPTION, getDeepSleepOption());
                    proto.end(bToken);
                    proto.flush();
                    doHaveContent = true;
                }
            } else {
                pw.println();
                pw.print(desc.toString());
                pw.println(":");
                if (this.mBatteryStatsImpl.getBatteryStatsWrapper().dumpDurationSteps(pw, "      ", "    Discharge step durations:", dit2.mDischargeSteps, false)) {
                    dit = dit2;
                    outInt = outInt2;
                    z = true;
                    this.mBatteryStatsImpl.getBatteryStatsWrapper().dumpDailyLevelStepSummary(pw, "        ", "Discharge", dit2.mDischargeSteps, sb, outInt);
                } else {
                    dit = dit2;
                    outInt = outInt2;
                    z = true;
                }
                BatteryStats.DailyItem dit3 = dit;
                if (this.mBatteryStatsImpl.getBatteryStatsWrapper().dumpDurationSteps(pw, "      ", "    Charge step durations:", dit.mChargeSteps, false)) {
                    this.mBatteryStatsImpl.getBatteryStatsWrapper().dumpDailyLevelStepSummary(pw, "        ", "Charge", dit3.mChargeSteps, sb, outInt);
                }
                pw.println("Total RAM size: " + getTotalRAM());
                pw.println("DeepSleep option: " + getDeepSleepOption());
            }
            doHaveContent = true;
        }
        if (doHaveContent) {
            return z;
        }
        return false;
    }

    @Override // android.os.IOplusDailyBattProtoManager
    public void reportDailyProto() {
        myLog("reportDailyProto");
        if (!ifDailyProtoEnabled()) {
            myLog("oops, this proto reporting feature is off, just return...");
            return;
        }
        boolean hasContent = false;
        FileOutputStream protOut = null;
        try {
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            try {
                File protoFile = new File(LATEST_DAILY_PROTO_FILE);
                protOut = new FileOutputStream(protoFile);
                ProtoOutputStream proto = new ProtoOutputStream(protOut.getFD());
                hasContent = dumpLatestDailyInfo(null, proto);
                protOut.close();
            } catch (FileNotFoundException ex2) {
                Log.w(TAG, "reportDailyProto FileNotFoundException, error = ", ex2);
                ex2.printStackTrace();
                if (protOut != null) {
                    protOut.close();
                }
            } catch (IOException ex3) {
                Log.w(TAG, "reportDailyProto IOException, error = ", ex3);
                ex3.printStackTrace();
                if (protOut != null) {
                    protOut.close();
                }
            }
            if (DEBUG) {
                Log.d(TAG, "hasContent = " + hasContent);
            }
            if (!hasContent) {
                return;
            }
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            try {
                try {
                    try {
                        try {
                            InputStream is2 = new FileInputStream(LATEST_DAILY_PROTO_FILE);
                            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                            byte[] buffer = new byte[512];
                            while (true) {
                                int length = is2.read(buffer);
                                if (length == -1) {
                                    break;
                                }
                                myLog("read in length:" + length);
                                baos2.write(buffer, 0, length);
                            }
                            byte[] bytes = baos2.toByteArray();
                            myLog("bytes.length = " + bytes.length);
                            String hexString = encodeHexString(bytes);
                            myLog("hexString = " + hexString);
                            if (this.mContext != null) {
                                Map<String, String> eventMap = new HashMap<>();
                                eventMap.put(MDM_DAILY_PROTO_VER, VERSION_CODE);
                                eventMap.put(MDM_DAILY_PROTO, hexString);
                                OplusStatistics.onCommon(this.mContext, APP_ID, "DailyBattProto", "DailyBattProto", eventMap, false);
                                myLog("insert into OplusStatistics...");
                            } else {
                                Log.d(TAG, "mContext is still null");
                            }
                            is2.close();
                            baos2.close();
                        } catch (FileNotFoundException ex4) {
                            Log.w(TAG, "reportDailyProto FileNotFoundException, error = ", ex4);
                            ex4.printStackTrace();
                            if (0 != 0) {
                                is.close();
                            }
                            if (0 != 0) {
                                baos.close();
                            }
                        }
                    } catch (IOException ex5) {
                        Log.w(TAG, "reportDailyProto IOException, error = ", ex5);
                        ex5.printStackTrace();
                        if (0 != 0) {
                            is.close();
                        }
                        if (0 != 0) {
                            baos.close();
                        }
                    }
                } finally {
                }
            } catch (IOException ex6) {
                ex6.printStackTrace();
            }
        } finally {
        }
    }

    @Override // android.os.IOplusDailyBattProtoManager
    public int[] noteConnectivityChangedLocked(int type, String extra, int phoneDataConnectionType, int modStepMode, int curStepMode) {
        boolean z = DEBUG;
        if (z) {
            Slog.i(TAG, "noteConnectivityChangedLocked - type: " + type + " " + extra + ", phoneDataConnectionType: " + phoneDataConnectionType);
            Slog.i(TAG, "noteConnectivityChangedLocked - (before) modStepMode: " + modStepMode + "(" + (modStepMode >> 4) + "), curStepMode: " + curStepMode + "(" + (curStepMode >> 4) + ")");
        }
        if (NetworkInfo.State.DISCONNECTED.toString().equals(extra)) {
            if (z) {
                Slog.i(TAG, "noteConnectivityChangedLocked - Update to TYPE_NETWORK_OFF(0) accordingly");
            }
            switch (type) {
                case 0:
                case 1:
                    this.mPreiousNetworkType = curStepMode >> 4;
                    if (z) {
                        Slog.i(TAG, "Store previous network type : " + this.mPreiousNetworkType);
                    }
                    int finalStepMode = 0;
                    switch (this.mPreiousNetworkType) {
                        case 0:
                            Slog.i(TAG, "### Why change to disconnected from TYPE_NETWORK_OFF(0)");
                            break;
                        case 1:
                            finalStepMode = 4;
                            break;
                        case 2:
                            finalStepMode = 5;
                            break;
                        case 3:
                            finalStepMode = 6;
                            break;
                        case 4:
                            Slog.i(TAG, "### Why change to disconnected from TYPE_NETWORK_OFF_FROM_WIFI(4)");
                            break;
                        case 5:
                            Slog.i(TAG, "### Why change to disconnected from TYPE_NETWORK_OFF_FROM_DATA_REST(5)");
                            break;
                        case 6:
                            Slog.i(TAG, "### Why change to disconnected from TYPE_NETWORK_OFF_FROM_DATA_NR(6)");
                            break;
                        default:
                            Slog.i(TAG, "### Why change to disconnected from unexpected type: " + this.mPreiousNetworkType);
                            break;
                    }
                    Slog.i(TAG, "finalStepMode: " + finalStepMode);
                    modStepMode |= ((curStepMode >> 4) ^ finalStepMode) << 4;
                    curStepMode = (curStepMode & (-241)) | (finalStepMode << 4);
                    break;
                default:
                    Slog.i(TAG, "### Not my intresting type, ignore it");
                    break;
            }
        } else if (NetworkInfo.State.CONNECTED.toString().equals(extra)) {
            if (type == 1) {
                if (z) {
                    Slog.i(TAG, "noteConnectivityChangedLocked - Update to TYPE_NETWORK_WIFI(1)");
                }
                modStepMode |= (1 ^ (curStepMode >> 4)) << 4;
                curStepMode = (curStepMode & (-241)) | 16;
            } else if (type == 0) {
                if (phoneDataConnectionType == MY_LOCAL_NETWORK_TYPE_NR) {
                    if (z) {
                        Slog.i(TAG, "noteConnectivityChangedLocked - Update to TYPE_NETWORK_DATA_NR(3)");
                    }
                    modStepMode |= ((curStepMode >> 4) ^ 3) << 4;
                    curStepMode = (curStepMode & (-241)) | 48;
                } else {
                    if (z) {
                        Slog.i(TAG, "noteConnectivityChangedLocked - Update to TYPE_NETWORK_DATA_REST(2)");
                    }
                    modStepMode |= ((curStepMode >> 4) ^ 2) << 4;
                    curStepMode = (curStepMode & (-241)) | 32;
                }
            }
        }
        if (z) {
            Slog.i(TAG, "noteConnectivityChangedLocked - (after) modStepMode: " + modStepMode + "(" + (modStepMode >> 4) + "), curStepMode: " + curStepMode + "(" + (curStepMode >> 4) + ")");
        }
        return new int[]{modStepMode, curStepMode};
    }

    @Override // android.os.IOplusDailyBattProtoManager
    public int[] notePhoneDataConnectionStateLocked(int dataType, boolean hasData, int bin, int modStepMode, int curStepMode) {
        int modStepMode2 = modStepMode;
        int curStepMode2 = curStepMode;
        boolean z = DEBUG;
        if (z) {
            Slog.i(TAG, "Update phone Data connection " + bin + ", hasData: " + hasData);
            Slog.i(TAG, "notePhoneDataConnectionStateLocked - (before) modStepMode: " + modStepMode2 + "(" + (modStepMode2 >> 4) + "), curStepMode: " + curStepMode2 + "(" + (curStepMode2 >> 4) + ")");
        }
        boolean ifDataRest = (curStepMode2 >> 4) == 2;
        boolean ifDataDR = (curStepMode2 >> 4) == 3;
        if (ifDataRest || ifDataDR) {
            if (bin == MY_LOCAL_NETWORK_TYPE_NR) {
                if (z) {
                    Slog.i(TAG, "notePhoneDataConnectionStateLocked - Update to NETWORK_TYPE_NR(3)");
                }
                modStepMode2 |= ((curStepMode2 >> 4) ^ 3) << 4;
                curStepMode2 = (curStepMode2 & (-241)) | 48;
            } else {
                if (z) {
                    Slog.i(TAG, "notePhoneDataConnectionStateLocked - Update to TYPE_NETWORK_DATA_REST(2)");
                }
                modStepMode2 |= ((curStepMode2 >> 4) ^ 2) << 4;
                curStepMode2 = (curStepMode2 & (-241)) | 32;
            }
        }
        if (z) {
            Slog.i(TAG, "notePhoneDataConnectionStateLocked - (after) modStepMode: " + modStepMode2 + "(" + (modStepMode2 >> 4) + "), curStepMode: " + curStepMode2 + "(" + (curStepMode2 >> 4) + ")");
        }
        return new int[]{modStepMode2, curStepMode2};
    }

    private void dumpDurationSteps(ProtoOutputStream proto, long fieldId, BatteryStats.LevelStepTracker steps) {
        int ds;
        int psm;
        if (steps == null) {
            return;
        }
        int count = steps.mNumStepDurations;
        for (int i = 0; i < count; i++) {
            long token = proto.start(fieldId);
            proto.write(OplusBatteryLevelStep.DURATION_MS, steps.getDurationAt(i));
            proto.write(1120986464258L, steps.getLevelAt(i));
            long initMode = steps.getInitModeAt(i);
            long modMode = steps.getModModeAt(i);
            if ((modMode & 3) != 0) {
                ds = 0;
            } else {
                switch (((int) (3 & initMode)) + 1) {
                    case 1:
                        ds = 2;
                        break;
                    case 2:
                        ds = 1;
                        break;
                    case 3:
                        ds = 3;
                        break;
                    case 4:
                        ds = 4;
                        break;
                    default:
                        ds = 5;
                        break;
                }
            }
            proto.write(OplusBatteryLevelStep.DISPLAY_STATE, ds);
            if ((modMode & 4) != 0) {
                psm = 0;
            } else {
                psm = (4 & initMode) == 0 ? 2 : 1;
            }
            proto.write(OplusBatteryLevelStep.POWER_SAVE_MODE, psm);
            int im = 0;
            if ((modMode & 8) == 0) {
                im = (8 & initMode) == 0 ? 3 : 2;
            }
            proto.write(OplusBatteryLevelStep.IDLE_MODE, im);
            handleNetworkProtoDetail(proto, initMode, modMode);
            proto.end(token);
        }
    }

    private void handleNetworkProtoDetail(ProtoOutputStream proto, long initMode, long modMode) {
        int net = 0;
        if ((modMode >> 4) == 0) {
            int networkType = (int) (initMode >> 4);
            switch (networkType) {
                case 0:
                    net = 1;
                    break;
                case 1:
                    net = 2;
                    break;
                case 2:
                    net = 3;
                    break;
                case 3:
                    net = 4;
                    break;
                case 4:
                    net = 5;
                    break;
                case 5:
                    net = 6;
                    break;
                case 6:
                    net = 7;
                    break;
            }
        }
        proto.write(OplusBatteryLevelStep.NETWORK_TYPE, net);
    }

    private String byteToHex(byte num) {
        char[] hexDigits = {Character.forDigit((num >> 4) & 15, 16), Character.forDigit(num & BatteryStatsExtImpl.ThermalItem.CMD_JOBINFO, 16)};
        return new String(hexDigits);
    }

    private String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }

    private int getDeepSleepOption() {
        ContentResolver resolver;
        Context context = this.mContext;
        if (context == null || (resolver = context.getContentResolver()) == null) {
            return 2;
        }
        int option = Settings.Secure.getInt(resolver, STR_DEEPSLEEP_SWITCH, 2);
        return option;
    }

    private long getTotalRAM() {
        if (this.mTotalRAM == 0) {
            MemInfoReader memInfo = new MemInfoReader();
            memInfo.readMemInfo();
            this.mTotalRAM = memInfo.getTotalSizeKb();
        }
        return this.mTotalRAM;
    }

    private boolean ifDailyProtoEnabled() {
        Context context = this.mContext;
        return context != null && Settings.System.getInt(context.getContentResolver(), DAILY_BATT_PROTO_SWITCH, 1) == 1;
    }
}
