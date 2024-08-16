package com.android.internal.os;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.BatteryStats;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.ModemActivityInfo;
import android.text.format.DateFormat;
import android.util.Slog;
import com.oplus.multiuser.IOplusMultiUserStatisticsManager;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class OplusDevicePowerStats {
    public static final boolean DEBUG = false;
    private static final double MILLISECONDS_IN_HOUR = 3600000.0d;
    private static final long MILLISECONDS_IN_MINUTE = 60000;
    private static final long MILLISECONDS_IN_YEAR = 31536000000L;
    static final int MSG_NOTIFY_MOBILE_ACTIVTY_UPDATED = 1;
    static final int MSG_NOTIFY_POWER_DRAIN_UPDATED = 2;
    static final int MSG_NOTIFY_TOP_ACTIVITY_UPDATED = 3;
    static final int MSG_NOTIFY_TRAFFIC_UPDATED = 4;
    private static final String NHS_MD_ACI_SAFE_PERMISSION = "com.oplus.nhs.permission.NHS_MD_ACI_SAFE_PERMISSION";
    private static final int ONE_THOUSAND = 1000;
    public static final String TAG = "OplusDevicePowerStats";
    private static final int TRAN_CODE = 1013;
    private static final int TRAN_FLAG = 0;
    private static final long UPDATE_INTERVAL = 60000;
    private boolean mBTHeadsetConnected;
    private long mBluetoothPowerDrainMaMs;
    private Context mContext;
    private long mCurStepBatteryUpTime;
    private int mCurrScreenBrightness;
    private long mGpsPowerDrainMaMs;
    private long mLastStepBatteryUpTime;
    private long mMobilePowerDrainMaMs;
    private long mMobileRxTotalBytes;
    private long mMobileTxTotalBytes;
    private OplusRpmSubsystemManager mOplusRpmManager;
    private PowerDetailsConstants mPowerConstants;
    private PowerDetailsReceiver mPowerDetailsReceiver;
    private PowerProfile mPowerProfile;
    private int mScreenRefreshMode;
    private int mVolumeMusicSpeaker;
    private boolean mWifiApStateEnabled;
    private long mWifiPowerDrainMaMs;
    private long mWifiRxTotalBytes;
    private long mWifiTxTotalBytes;
    private boolean mWiredHeadsetConnected;
    private final Object mLock = new Object();
    private String mCurrentTopActivity = "unknow";
    private String mCurModemInfoSummary = "0";
    private String mLastModemInfoSummary = "0";
    private String mCurEndcInfoSummary = "0";
    private String mLastEndcInfoSummary = "0";
    private int[] mCurModemTxTimes = new int[ModemActivityInfo.getNumTxPowerLevels()];
    private boolean mMeaturing = false;
    private DevicePowerDetails mCurOplusDevicePowerDetails = new DevicePowerDetails();
    private DevicePowerDetails mLastOplusDevicePowerDetails = new DevicePowerDetails();
    private DevicePowerDetails mLastLastOplusDevicePowerDetails = new DevicePowerDetails();
    private DevicePowerDetails mDevicePowerDetailsDelta = new DevicePowerDetails();
    private SmartEndcStatus mCurSmartEndcStatus = new SmartEndcStatus();

    public OplusDevicePowerStats(Context context, Handler handler) {
        this.mContext = context;
        this.mPowerConstants = new PowerDetailsConstants(handler);
        this.mOplusRpmManager = new OplusRpmSubsystemManager(this.mContext, handler);
    }

    public void onSystemServicesReady(Context context) {
        this.mContext = context;
        this.mPowerConstants.startObserving(context, context.getContentResolver());
        PowerDetailsReceiver powerDetailsReceiver = new PowerDetailsReceiver(context);
        this.mPowerDetailsReceiver = powerDetailsReceiver;
        powerDetailsReceiver.register();
    }

    /* loaded from: classes.dex */
    class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
        }
    }

    public void clear() {
        this.mCurOplusDevicePowerDetails.clear();
        this.mLastOplusDevicePowerDetails.clear();
        this.mLastLastOplusDevicePowerDetails.clear();
        this.mDevicePowerDetailsDelta.clear();
    }

    public void dumpHistory() {
        Slog.d("OplusDevicePowerStats", "current:" + this.mCurOplusDevicePowerDetails.toString());
        Slog.d("OplusDevicePowerStats", "last0:" + this.mLastOplusDevicePowerDetails.toString());
        Slog.d("OplusDevicePowerStats", "last1:" + this.mLastLastOplusDevicePowerDetails.toString());
        Slog.d("OplusDevicePowerStats", "delta:" + this.mDevicePowerDetailsDelta.toString());
    }

    public void recordBluetoothPowerDrainMaMs(long powerdrains) {
        synchronized (this.mLock) {
            this.mCurOplusDevicePowerDetails.mBluetoothPowerDrainMaMs += powerdrains;
        }
    }

    public void recordMobilePowerDrainMaMs(long powerdrains) {
        synchronized (this.mLock) {
            this.mCurOplusDevicePowerDetails.mMobilePowerDrainMaMs += powerdrains;
        }
    }

    public void recordWifiPowerDrainMaMs(long powerdrains) {
        synchronized (this.mLock) {
            this.mCurOplusDevicePowerDetails.mWifiPowerDrainMaMs += powerdrains;
        }
    }

    public void recordGpsPowerDrainMaMs(long powerdrains) {
        synchronized (this.mLock) {
            this.mCurOplusDevicePowerDetails.mGpsPowerDrainMaMs = powerdrains;
        }
    }

    public void recordBrightness(int level) {
        synchronized (this.mLock) {
            this.mCurOplusDevicePowerDetails.mBrightness = level;
        }
    }

    public void recordResumeActivity(String activity) {
        synchronized (this.mLock) {
            this.mCurOplusDevicePowerDetails.mActivtiyName = activity;
        }
    }

    public void recordNetworkActivityBytes(int type, long deltaBytes) {
        synchronized (this.mLock) {
            switch (type) {
                case 0:
                    this.mCurOplusDevicePowerDetails.mMobileRxTotalBytes += deltaBytes;
                    break;
                case 1:
                    this.mCurOplusDevicePowerDetails.mMobileTxTotalBytes += deltaBytes;
                    break;
                case 2:
                    this.mCurOplusDevicePowerDetails.mWifiRxTotalBytes += deltaBytes;
                    break;
                case 3:
                    this.mCurOplusDevicePowerDetails.mWifiTxTotalBytes += deltaBytes;
                    break;
            }
        }
    }

    public void updateMobileRadioState(ModemActivityInfo deltaInfo) {
        synchronized (this.mLock) {
            if (deltaInfo == null) {
                return;
            }
            int[] iArr = new int[ModemActivityInfo.getNumTxPowerLevels()];
            for (int i0 = 0; i0 < ModemActivityInfo.getNumTxPowerLevels(); i0++) {
            }
        }
    }

    /* loaded from: classes.dex */
    private class PowerDetailsConstants extends ContentObserver {
        public static final String KEY_DISPLAY_REF_MODE = "coloros_screen_refresh_rate";
        public static final String KEY_VOL_MUSIC_SPK = "volume_music_speaker";
        public static final String TAG = "OplusDevicePowerStats";
        private Context mContext;
        private ContentResolver mResolver;

        public PowerDetailsConstants(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            updateConstants();
        }

        public void startObserving(Context context, ContentResolver resolver) {
            this.mContext = context;
            this.mResolver = resolver;
            resolver.registerContentObserver(Settings.Secure.getUriFor(KEY_DISPLAY_REF_MODE), false, this);
            this.mResolver.registerContentObserver(Settings.System.getUriFor(KEY_VOL_MUSIC_SPK), false, this);
            updateConstants();
        }

        private void updateConstants() {
            Context context = this.mContext;
            if (context == null) {
                Slog.d("OplusDevicePowerStats", "Context is null");
                return;
            }
            OplusDevicePowerStats.this.mScreenRefreshMode = Settings.Secure.getInt(context.getContentResolver(), KEY_DISPLAY_REF_MODE, 0);
            OplusDevicePowerStats.this.mVolumeMusicSpeaker = Settings.System.getInt(this.mContext.getContentResolver(), KEY_VOL_MUSIC_SPK, -1);
            OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mRefreshRateSetting = OplusDevicePowerStats.this.mScreenRefreshMode;
            OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mVolumeMusicSpeaker = OplusDevicePowerStats.this.mVolumeMusicSpeaker;
        }
    }

    /* loaded from: classes.dex */
    private class PowerDetailsReceiver extends BroadcastReceiver {
        public static final String ACTION_MDPWR_REPORT_TO_BATTERY_STATES = "oplus.intent.action.MDPWR_REPORT_TO_BATTERY_STATES";
        public static final String ACTION_POWERSTATS_FORECE_UPDATE = "oplus.intent.action.powerstats.FORECE_UPDATE";
        public static final String ACTION_ROM_UPDATE_CONFIG_SUCCES = "oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS";
        public static final String ACTION_SMART5G_KEY_INFO = "oplus.intent.action.SMART5G_KEYINFO";
        public static final String TAG = "OplusDevicePowerStats";
        private Context mContext;
        private boolean mRegistered;

        public PowerDetailsReceiver(Context context) {
            this.mContext = context;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Slog.d("OplusDevicePowerStats", "on receive " + action);
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {
                int state = intent.getIntExtra("wifi_state", 11);
                Slog.d("OplusDevicePowerStats", "wifi ap state = " + state);
                if (state == 13) {
                    OplusDevicePowerStats.this.mWifiApStateEnabled = true;
                } else {
                    OplusDevicePowerStats.this.mWifiApStateEnabled = false;
                }
                Slog.d("OplusDevicePowerStats", "wifi ap enabled " + OplusDevicePowerStats.this.mWifiApStateEnabled);
            } else if ("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED".equals(action)) {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                int state2 = adapter.getProfileConnectionState(1);
                Slog.d("OplusDevicePowerStats", "BT headset connected state " + state2);
                if (2 == state2) {
                    OplusDevicePowerStats.this.mBTHeadsetConnected = true;
                } else {
                    OplusDevicePowerStats.this.mBTHeadsetConnected = false;
                }
            } else if ("android.intent.action.HEADSET_PLUG".equals(action)) {
                if (intent.hasExtra("state")) {
                    Slog.d("OplusDevicePowerStats", "wired headset connected " + intent.getIntExtra("state", 1));
                    OplusDevicePowerStats.this.mWiredHeadsetConnected = true;
                } else {
                    OplusDevicePowerStats.this.mWiredHeadsetConnected = false;
                }
            } else if (ACTION_MDPWR_REPORT_TO_BATTERY_STATES.equals(action)) {
                String modemInfo = intent.getStringExtra("ModemActivityInfo");
                if (modemInfo != null && !modemInfo.equals(OplusDevicePowerStats.this.mCurModemInfoSummary)) {
                    OplusDevicePowerStats oplusDevicePowerStats = OplusDevicePowerStats.this;
                    oplusDevicePowerStats.mLastModemInfoSummary = oplusDevicePowerStats.mCurModemInfoSummary;
                    OplusDevicePowerStats.this.mCurModemInfoSummary = modemInfo;
                }
            } else if (ACTION_SMART5G_KEY_INFO.equals(action)) {
                OplusDevicePowerStats.this.mCurSmartEndcStatus = SmartEndcStatus.creatEndcStatusFormIntent(intent);
                String endcInfo = OplusDevicePowerStats.this.mCurSmartEndcStatus.toStringLite();
                if (endcInfo != null && !OplusDevicePowerStats.this.mCurEndcInfoSummary.equals(OplusDevicePowerStats.this.mLastEndcInfoSummary)) {
                    OplusDevicePowerStats oplusDevicePowerStats2 = OplusDevicePowerStats.this;
                    oplusDevicePowerStats2.mLastEndcInfoSummary = oplusDevicePowerStats2.mCurEndcInfoSummary;
                    OplusDevicePowerStats.this.mCurEndcInfoSummary = endcInfo;
                }
            } else if (!ACTION_POWERSTATS_FORECE_UPDATE.equals(action)) {
                if ("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS".equals(action)) {
                    OplusDevicePowerStats.this.mOplusRpmManager.scheduleUpdateRpmPath(60000L);
                } else if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
                    OplusDevicePowerStats.this.mOplusRpmManager.onBootCompleted();
                }
            }
            if (OplusDevicePowerStats.this.mBTHeadsetConnected) {
                OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mHeadsetConnectedType = 2;
            } else if (OplusDevicePowerStats.this.mWiredHeadsetConnected) {
                OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mHeadsetConnectedType = 1;
            } else {
                OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mHeadsetConnectedType = 0;
            }
            OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mVolumeMusicSpeaker = OplusDevicePowerStats.this.mVolumeMusicSpeaker;
            OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mWifiApEnabled = OplusDevicePowerStats.this.mWifiApStateEnabled;
            OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mModemActivityInfo = OplusDevicePowerStats.this.mCurModemInfoSummary;
            OplusDevicePowerStats.this.mCurOplusDevicePowerDetails.mEndcInfoSummary = OplusDevicePowerStats.this.mCurEndcInfoSummary;
        }

        public void register() {
            if (!this.mRegistered && this.mContext != null) {
                Slog.d("OplusDevicePowerStats", "registerReceiver");
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
                intentFilter.addAction("android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED");
                intentFilter.addAction("android.intent.action.HEADSET_PLUG");
                intentFilter.addAction(ACTION_MDPWR_REPORT_TO_BATTERY_STATES);
                intentFilter.addAction(ACTION_POWERSTATS_FORECE_UPDATE);
                intentFilter.addAction(ACTION_SMART5G_KEY_INFO);
                intentFilter.addAction("oplus.intent.action.ROM_UPDATE_CONFIG_SUCCESS");
                intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
                this.mContext.registerReceiver(this, intentFilter, OplusDevicePowerStats.NHS_MD_ACI_SAFE_PERMISSION, null);
                this.mRegistered = true;
            }
        }

        public void unregister() {
            Context context;
            if (this.mRegistered && (context = this.mContext) != null) {
                context.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }
    }

    public String getDevicePowerStatsDeltaString() {
        synchronized (this.mLock) {
            DevicePowerDetails devicePowerDetails = this.mDevicePowerDetailsDelta;
            if (devicePowerDetails != null) {
                String powerdetails = devicePowerDetails.toString();
                return powerdetails;
            }
            return "";
        }
    }

    private SmartEndcStatus creatEndcStatusFormIntent(Intent action) {
        SmartEndcStatus status = new SmartEndcStatus(action.getBooleanExtra(IOplusMultiUserStatisticsManager.SWITCH, false), action.getLongExtra("EndcDura", 0L), action.getLongExtra("NoEndcDura", 0L), action.getLongExtra("EnEndcTime", 0L), action.getLongExtra("DisEndcTime", 0L), action.getIntExtra("LteSpeedCntL0", 0), action.getIntExtra("LteSpeedCntL1", 0), action.getIntExtra("LteSpeedCntL2", 0), action.getIntExtra("LteSpeedCntL3", 0), action.getIntExtra("LteSpeedCntL4", 0), action.getIntExtra("EnEndcSpeedHighCnt", 0), action.getIntExtra("EnEndcSwitchOffCnt", 0), action.getIntExtra("EnEndcLtePoorCnt", 0), action.getIntExtra("EnEndcLteJamCnt", 0), action.getIntExtra("EnEndcProhibitCnt", 0));
        return status;
    }

    private int getSurfaceFlingerRefreshCounts() {
        try {
            IBinder flinger = ServiceManager.getService("SurfaceFlinger");
            if (flinger == null) {
                return 0;
            }
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken("android.ui.ISurfaceComposer");
            flinger.transact(TRAN_CODE, data, reply, 0);
            int counts = reply.readInt();
            data.recycle();
            reply.recycle();
            return counts;
        } catch (RemoteException e) {
            Slog.e("OplusDevicePowerStats", "get RefreshCounts failed");
            return 0;
        }
    }

    public void onBatteryStepDrained() {
        updateDevicePowerDetails(true);
    }

    public void onChagrgeStepChanged() {
    }

    private void updateDevicePowerDetails(boolean computeDelta) {
        Slog.d("OplusDevicePowerStats", "updateDevicePowerDetails");
        synchronized (this.mLock) {
            DevicePowerDetails devicePowerDetails = this.mCurOplusDevicePowerDetails;
            if (devicePowerDetails == null) {
                return;
            }
            devicePowerDetails.mCurrentTime = System.currentTimeMillis();
            this.mCurOplusDevicePowerDetails.mRefreshCounts = getSurfaceFlingerRefreshCounts();
            if (computeDelta) {
                this.mOplusRpmManager.onBatteryDrained();
                this.mCurOplusDevicePowerDetails.mLastStepRpmStatsSummary = this.mOplusRpmManager.getLastStepRpmSuspendRatioSummary();
                this.mLastLastOplusDevicePowerDetails.setTo(this.mLastOplusDevicePowerDetails);
                this.mLastOplusDevicePowerDetails.setTo(this.mCurOplusDevicePowerDetails);
                computeDelta(this.mLastOplusDevicePowerDetails, this.mLastLastOplusDevicePowerDetails, this.mDevicePowerDetailsDelta);
            }
        }
    }

    private void computeDelta(DevicePowerDetails current, DevicePowerDetails last, DevicePowerDetails delta) {
        this.mLastStepBatteryUpTime = this.mCurStepBatteryUpTime;
        this.mCurStepBatteryUpTime = SystemClock.uptimeMillis();
        delta.setTo(current);
        int[] lastModemTxTimes = new int[ModemActivityInfo.getNumTxPowerLevels()];
        int[] curModemTxTimes = this.mLastOplusDevicePowerDetails.mTxTimeMs;
        int[] detaModemTxTimes = new int[ModemActivityInfo.getNumTxPowerLevels()];
        long detaTimeSec = this.mCurStepBatteryUpTime;
        long lastRefCounts = this.mLastStepBatteryUpTime;
        long detaTimeSec2 = (detaTimeSec - lastRefCounts) / 1000;
        Arrays.fill(lastModemTxTimes, 0);
        Arrays.fill(curModemTxTimes, 0);
        Arrays.fill(detaModemTxTimes, 0);
        long curRefCounts = current.mRefreshCounts;
        long curRefCounts2 = last.mRefreshCounts;
        double refsPerSec = detaTimeSec2 > 0 ? (curRefCounts - curRefCounts2) / detaTimeSec2 : 0.0d;
        delta.mRefreshesPerSecond = refsPerSec;
        int[] curModemTxTimes2 = current.mTxTimeMs;
        int[] lastModemTxTimes2 = last.mTxTimeMs;
        if (curModemTxTimes2 != null && lastModemTxTimes2 != null) {
            int curLen = curModemTxTimes2.length;
            int lastLen = lastModemTxTimes2.length;
            if (curLen == lastLen) {
                for (int lastLen2 = 0; lastLen2 < curLen; lastLen2++) {
                    detaModemTxTimes[lastLen2] = curModemTxTimes2[lastLen2] - lastModemTxTimes2[lastLen2];
                }
            }
        }
        delta.mDetaTxTimeMs = detaModemTxTimes;
        delta.mMobileRxTotalBytes = last.mMobileRxTotalBytes;
        delta.mMobileTxTotalBytes = last.mMobileTxTotalBytes;
        delta.mWifiRxTotalBytes = last.mWifiRxTotalBytes;
        delta.mWifiTxTotalBytes = last.mWifiTxTotalBytes;
        delta.mMobilePowerDrainMaMs = last.mMobilePowerDrainMaMs;
        delta.mWifiPowerDrainMaMs = last.mWifiPowerDrainMaMs;
        delta.mGpsPowerDrainMaMs = last.mGpsPowerDrainMaMs;
        delta.mBluetoothPowerDrainMaMs = last.mBluetoothPowerDrainMaMs;
    }

    /* loaded from: classes.dex */
    public static final class DevicePowerDetails implements Parcelable {
        public String mActivtiyName;
        public long mBluetoothPowerDrainMaMs;
        public int mBrightness;
        public long mCurrentTime;
        public int[] mDetaTxTimeMs;
        public String mEndcInfoSummary;
        public long mGpsPowerDrainMaMs;
        public int mHeadsetConnectedType;
        public String mLastStepRpmStatsSummary;
        public long mMobilePowerDrainMaMs;
        public long mMobileRxTotalBytes;
        public long mMobileTxTotalBytes;
        public String mModemActivityInfo;
        public long mRefreshCounts;
        public int mRefreshRateSetting;
        public double mRefreshesPerSecond;
        public int[] mTxTimeMs;
        public int mVolumeMusicSpeaker;
        public boolean mWifiApEnabled;
        public long mWifiPowerDrainMaMs;
        public long mWifiRxTotalBytes;
        public long mWifiTxTotalBytes;

        public DevicePowerDetails() {
            clear();
        }

        public DevicePowerDetails(Parcel src) {
            readFromParcel(src);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public void clear() {
            this.mCurrentTime = 0L;
            this.mRefreshesPerSecond = 0.0d;
            this.mRefreshCounts = 0L;
            this.mRefreshRateSetting = 0;
            this.mBrightness = 0;
            this.mTxTimeMs = new int[ModemActivityInfo.getNumTxPowerLevels()];
            this.mDetaTxTimeMs = new int[ModemActivityInfo.getNumTxPowerLevels()];
            Arrays.fill(this.mTxTimeMs, 0);
            Arrays.fill(this.mDetaTxTimeMs, 0);
            this.mMobileRxTotalBytes = 0L;
            this.mMobileTxTotalBytes = 0L;
            this.mWifiRxTotalBytes = 0L;
            this.mWifiTxTotalBytes = 0L;
            this.mMobilePowerDrainMaMs = 0L;
            this.mWifiPowerDrainMaMs = 0L;
            this.mGpsPowerDrainMaMs = 0L;
            this.mBluetoothPowerDrainMaMs = 0L;
            this.mVolumeMusicSpeaker = 0;
            this.mHeadsetConnectedType = 0;
            this.mModemActivityInfo = "0";
            this.mEndcInfoSummary = "0";
            this.mLastStepRpmStatsSummary = "0";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            writeToParcel(out);
        }

        public void writeToParcel(Parcel out) {
            out.writeLong(this.mCurrentTime);
            out.writeLong(this.mRefreshCounts);
            out.writeInt(this.mRefreshRateSetting);
            out.writeInt(this.mBrightness);
            out.writeString(this.mActivtiyName);
            out.writeDouble(this.mRefreshesPerSecond);
            int len = this.mDetaTxTimeMs.length;
            for (int i = 0; i < len; i++) {
                out.writeLong(this.mDetaTxTimeMs[i]);
            }
            out.writeLong(this.mMobileRxTotalBytes);
            out.writeLong(this.mMobileTxTotalBytes);
            out.writeLong(this.mWifiRxTotalBytes);
            out.writeLong(this.mWifiTxTotalBytes);
            out.writeLong(this.mMobilePowerDrainMaMs);
            out.writeLong(this.mWifiPowerDrainMaMs);
            out.writeLong(this.mGpsPowerDrainMaMs);
            out.writeLong(this.mBluetoothPowerDrainMaMs);
            out.writeBoolean(this.mWifiApEnabled);
            out.writeString(this.mLastStepRpmStatsSummary);
            out.writeString(this.mModemActivityInfo);
            out.writeString(this.mEndcInfoSummary);
        }

        public void readFromParcel(Parcel in) {
            try {
                this.mCurrentTime = in.readLong();
                this.mRefreshCounts = in.readLong();
                this.mRefreshRateSetting = in.readInt();
                this.mBrightness = in.readInt();
                this.mActivtiyName = in.readString();
                this.mRefreshesPerSecond = in.readDouble();
                int len = this.mDetaTxTimeMs.length;
                for (int i = 0; i < len; i++) {
                    this.mDetaTxTimeMs[i] = in.readInt();
                }
                this.mMobileRxTotalBytes = in.readLong();
                this.mMobileTxTotalBytes = in.readLong();
                this.mWifiRxTotalBytes = in.readLong();
                this.mWifiTxTotalBytes = in.readLong();
                this.mMobilePowerDrainMaMs = in.readLong();
                this.mWifiPowerDrainMaMs = in.readLong();
                this.mGpsPowerDrainMaMs = in.readLong();
                this.mBluetoothPowerDrainMaMs = in.readLong();
                this.mWifiApEnabled = in.readBoolean();
                this.mLastStepRpmStatsSummary = in.readString();
                this.mModemActivityInfo = in.readString();
                this.mEndcInfoSummary = in.readString();
            } catch (Exception e) {
                Slog.e("read DevicePowerDetails", "Error reading fromParcel ", e);
            }
        }

        public void setTo(DevicePowerDetails out) {
            this.mCurrentTime = out.mCurrentTime;
            this.mRefreshCounts = out.mRefreshCounts;
            this.mRefreshRateSetting = out.mRefreshRateSetting;
            this.mBrightness = out.mBrightness;
            this.mActivtiyName = out.mActivtiyName;
            this.mRefreshesPerSecond = out.mRefreshesPerSecond;
            this.mDetaTxTimeMs = out.mDetaTxTimeMs;
            this.mMobileRxTotalBytes = out.mMobileRxTotalBytes;
            this.mMobileTxTotalBytes = out.mMobileTxTotalBytes;
            this.mWifiRxTotalBytes = out.mWifiRxTotalBytes;
            this.mWifiTxTotalBytes = out.mWifiTxTotalBytes;
            this.mMobilePowerDrainMaMs = out.mMobilePowerDrainMaMs;
            this.mWifiPowerDrainMaMs = out.mWifiPowerDrainMaMs;
            this.mGpsPowerDrainMaMs = out.mGpsPowerDrainMaMs;
            this.mBluetoothPowerDrainMaMs = out.mBluetoothPowerDrainMaMs;
            this.mWifiApEnabled = out.mWifiApEnabled;
            this.mLastStepRpmStatsSummary = out.mLastStepRpmStatsSummary;
            this.mModemActivityInfo = out.mModemActivityInfo;
            this.mEndcInfoSummary = out.mEndcInfoSummary;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("PowerDetails: time=" + DateFormat.format("yyyy-MM-dd-HH-mm-ss", this.mCurrentTime).toString());
            builder.append(", top=");
            builder.append(this.mActivtiyName);
            builder.append(", ref_counts=");
            builder.append(this.mRefreshCounts);
            builder.append(", ref_mode=");
            builder.append(this.mRefreshRateSetting);
            builder.append(", brightness=");
            builder.append(this.mBrightness);
            builder.append(", refs_per_sec=");
            builder.append(this.mRefreshesPerSecond);
            builder.append(", modem_txTimeMs=");
            builder.append(Arrays.toString(this.mDetaTxTimeMs));
            builder.append(", traffic=");
            builder.append("[" + this.mMobileRxTotalBytes + "," + this.mMobileTxTotalBytes + "," + this.mWifiRxTotalBytes + "," + this.mWifiTxTotalBytes + "]");
            builder.append(", ctr_drain=");
            builder.append("[");
            builder.append(BatteryStats.formatCharge(this.mMobilePowerDrainMaMs / OplusDevicePowerStats.MILLISECONDS_IN_HOUR) + ",");
            builder.append(BatteryStats.formatCharge(this.mWifiPowerDrainMaMs / OplusDevicePowerStats.MILLISECONDS_IN_HOUR) + ",");
            builder.append(BatteryStats.formatCharge(this.mGpsPowerDrainMaMs / OplusDevicePowerStats.MILLISECONDS_IN_HOUR) + ",");
            builder.append(BatteryStats.formatCharge(this.mBluetoothPowerDrainMaMs / OplusDevicePowerStats.MILLISECONDS_IN_HOUR) + "]");
            builder.append(", rpmstat=");
            builder.append(this.mLastStepRpmStatsSummary);
            builder.append(", sap=");
            builder.append(this.mWifiApEnabled);
            builder.append(", audio=[");
            builder.append("spk:" + this.mVolumeMusicSpeaker);
            builder.append(", headset:" + this.mHeadsetConnectedType);
            builder.append("]");
            builder.append("\n");
            builder.append(", modem=[");
            builder.append("activity:" + this.mModemActivityInfo);
            builder.append("]");
            builder.append("\n");
            builder.append(", endc=");
            builder.append(this.mEndcInfoSummary);
            return builder.toString();
        }

        public String getJsonString() {
            return null;
        }
    }
}
