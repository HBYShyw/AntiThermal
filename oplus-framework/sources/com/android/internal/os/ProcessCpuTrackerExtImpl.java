package com.android.internal.os;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.OplusThermalManager;
import android.os.Process;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.os.ProcessCpuTracker;
import com.oplus.util.OplusHoraeThermalHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import libcore.io.IoUtils;

/* loaded from: classes.dex */
public class ProcessCpuTrackerExtImpl implements IProcessCpuTrackerExt {
    private static final int COLOR_AMS_MSG_INDEX = 1001;
    public static final String CPU_RECORD_FILE = "/data/system/cputrack.log";
    public static final String CPU_RECORD_OLD_FILE = "/data/system/cputrack.log.old";
    private static final String MDPWR_UPDATE_TRAFFIC_KERNEL_INFO = "oplus.intent.action.MDPWR_UPDATE_TRAFFIC_KERNEL_INFO";
    private static final String NHS_MD_ACI_SAFE_PERMISSION = "com.oplus.nhs.permission.NHS_MD_ACI_SAFE_PERMISSION";
    private static final int PEEK_SYSTEM_TEMP = 1008;
    private static final int PERSIST_CPU_TRACKER = 1007;
    private static final String TAG = "ProcessCpuTrackerExtImpl";
    private static final String THERMAL_ZONE_PATH = "/sys/class/thermal/thermal_zone83/temp";
    private static final int THERMAL_ZONE_PRECISION = 1000;
    private ProcessCpuTracker mBase;
    private Handler mBgHandler;
    private long mCurrentSampleWallTime;
    private long mLastSampleWallTime;
    private ArrayList<ProcessCpuTracker.Stats> mProcStats;
    private boolean mWorkingProcsSorted;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", true);
    private static boolean sDebug = SystemProperties.getBoolean("persist.sys.cputrack.debug", false);
    private static int sLimit = SystemProperties.getInt("persist.sys.cputrack.limit", 10);
    private static int sCommuLimit = SystemProperties.getInt("persist.sys.cputrack.limit", 5);
    private static int sMyUid = -1;
    private static ArrayList<String> sRecordList = new ArrayList<>();
    private static long sLastWriteTime = SystemClock.uptimeMillis();
    public static float sSystemTempreture = 0.0f;
    private static final Comparator<ProcessCpuTracker.Stats> sLoadComparator = new Comparator<ProcessCpuTracker.Stats>() { // from class: com.android.internal.os.ProcessCpuTrackerExtImpl.1
        @Override // java.util.Comparator
        public final int compare(ProcessCpuTracker.Stats sta, ProcessCpuTracker.Stats stb) {
            int ta = sta.rel_utime + sta.rel_stime;
            int tb = stb.rel_utime + stb.rel_stime;
            if (ta != tb) {
                return ta > tb ? -1 : 1;
            }
            if (sta.added != stb.added) {
                return sta.added ? -1 : 1;
            }
            if (sta.removed != stb.removed) {
                return sta.added ? -1 : 1;
            }
            return 0;
        }
    };
    private String mThermalZonePath = THERMAL_ZONE_PATH;
    private int mThermalZonePrecision = 1000;
    private boolean mInited = false;
    private boolean mShouldReport2Nhs = false;
    private Context mContext = null;
    private long mCpuUpTime = 0;
    private HashMap<String, Long> mProcStTimeRecordMap = new HashMap<>();
    private HashMap<String, Long> mProcUtTimeRecordMap = new HashMap<>();
    private final ArrayList<ProcessCpuTracker.Stats> mWorkingProcs = new ArrayList<>();
    private float mLoad1 = 0.0f;
    private float mLoad5 = 0.0f;
    private float mLoad15 = 0.0f;
    private int maxCpuThousandths = -1;
    private String maxCpuProName = "null";
    private ArrayMap<String, String> mTopThreeProcessesSnapShot = new ArrayMap<>();
    private String mSimpleTopProcessInfo = "";
    private long mLastTopSampleTime = -1;

    public ProcessCpuTrackerExtImpl(Object base) {
        this.mBase = (ProcessCpuTracker) base;
    }

    public void init(ArrayList<ProcessCpuTracker.Stats> procStats) {
        Slog.d(TAG, "constructor with parameter: " + procStats);
        this.mProcStats = procStats;
        sSystemTempreture = OplusHoraeThermalHelper.getInstance().getCurrentThermal();
    }

    public void initHandler(Handler handler) {
        Slog.d(TAG, "initHandler with " + handler);
        this.mBgHandler = handler;
        if (handler != null) {
            handler.sendEmptyMessageDelayed(1008, 30000L);
        }
        this.mInited = true;
    }

    public void setContext(Context context) {
        if (this.mContext != null) {
            Slog.d(TAG, "mContext is already set, return now! ");
            return;
        }
        Slog.d(TAG, "setContext this: " + this + " context: " + context);
        this.mContext = context;
        initBroadcastReceiver();
    }

    public void handleMessage(Message msg) {
        if (sDebug) {
            Slog.d(TAG, "handleMessage to be called");
        }
        switch (msg.what) {
            case 1007:
                tryPersistToDisk();
                return;
            case 1008:
                updateSystemTempreture();
                return;
            default:
                return;
        }
    }

    public void initBroadcastReceiver() {
        if (this.mContext != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(MDPWR_UPDATE_TRAFFIC_KERNEL_INFO);
            BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.os.ProcessCpuTrackerExtImpl.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(ProcessCpuTrackerExtImpl.MDPWR_UPDATE_TRAFFIC_KERNEL_INFO)) {
                        if (ProcessCpuTrackerExtImpl.sDebug) {
                            Slog.d(ProcessCpuTrackerExtImpl.TAG, " receive MDPWR_UPDATE_TRAFFIC_KERNEL_INFO !");
                        }
                        ProcessCpuTrackerExtImpl.this.mShouldReport2Nhs = true;
                        ProcessCpuTrackerExtImpl.this.sendCpuInfo2Nhs();
                    }
                }
            };
            this.mContext.registerReceiver(mBroadcastReceiver, filter);
            return;
        }
        Slog.d(TAG, "mContext is null!");
    }

    public void sendCpuInfo2Nhs() {
        if (this.mShouldReport2Nhs && this.mContext != null) {
            new Thread(new Runnable() { // from class: com.android.internal.os.ProcessCpuTrackerExtImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ProcessCpuTrackerExtImpl.this.lambda$sendCpuInfo2Nhs$0();
                }
            }, "Send2NhsThread").start();
            this.mShouldReport2Nhs = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendCpuInfo2Nhs$0() {
        if (sDebug) {
            Slog.d(TAG, "enter : sendTread!");
        }
        Intent intentSend2Nhs = new Intent("oplus.intent.action.MSG_REPORT_CPU_INFO");
        intentSend2Nhs.putExtra("myProcessUtTime", this.mProcUtTimeRecordMap);
        intentSend2Nhs.putExtra("myProcessStTime", this.mProcStTimeRecordMap);
        intentSend2Nhs.putExtra("myCpuTime", this.mCpuUpTime);
        this.mContext.sendBroadcastAsUser(intentSend2Nhs, UserHandle.ALL, NHS_MD_ACI_SAFE_PERMISSION);
    }

    public void collectAnbormalStats() {
        Handler handler;
        SimpleDateFormat sdf;
        int size;
        boolean noRecord;
        SimpleDateFormat sdf2;
        int size2;
        boolean noRecord2;
        long percent;
        String[] myProcName;
        if (sDebug) {
            Slog.d(TAG, "collectAnbormalStats to be called");
        }
        if (!this.mInited) {
            Slog.d(TAG, "collectAnbormalStats, but init is not ready, skip");
            return;
        }
        if (sDebug) {
            sLimit = SystemProperties.getInt("persist.sys.cputrack.limit", 10);
        }
        if (!checkPermission()) {
            return;
        }
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        sb.append(sdf3.format(new Date(this.mLastSampleWallTime)));
        sb.append(" to ");
        sb.append(sdf3.format(new Date(this.mCurrentSampleWallTime)));
        sb.append(" ");
        sb.append(this.mCurrentSampleWallTime - this.mLastSampleWallTime);
        sb.append("ms ");
        sb.append(sSystemTempreture);
        sb.append("C]\n");
        if (sDebug) {
            Slog.d(TAG, "collectAnbormalStats # " + sb.toString());
            Slog.d(TAG, "mProcStats.size(): " + this.mProcStats.size());
        }
        int size3 = this.mProcStats.size();
        boolean noRecord3 = true;
        int i = 0;
        while (i < size3) {
            ProcessCpuTracker.Stats st = this.mProcStats.get(i);
            if (st == null || st.rel_uptime <= 0) {
                sdf = sdf3;
                size = size3;
            } else {
                long percent2 = ((st.rel_utime + st.rel_stime) * 100) / st.rel_uptime;
                if (sDebug) {
                    Slog.d(TAG, "st.uid: " + st.uid + ", pid: " + st.pid + ", st.name: " + st.name + ", percent: " + percent2 + ", sLimit: " + sLimit);
                }
                if (percent2 > sLimit) {
                    if (noRecord3) {
                        noRecord3 = false;
                    }
                    sb.append(st.uid);
                    sb.append(" ");
                    sb.append(st.pid);
                    sb.append(" ");
                    sb.append(st.name);
                    sb.append(" ");
                    sb.append(percent2);
                    sb.append(" ");
                    sb.append(st.rel_utime);
                    sb.append(" ");
                    sb.append(st.rel_stime);
                    sb.append(" ");
                    sb.append(st.rel_uptime);
                    sb.append("\n");
                }
                if (percent2 <= sCommuLimit) {
                    sdf = sdf3;
                    size = size3;
                    noRecord = noRecord3;
                } else {
                    String[] myProcName2 = {"diag-router", "dpmd", "networkwatchlis"};
                    int length = myProcName2.length;
                    int i2 = 0;
                    while (i2 < length) {
                        String name = myProcName2[i2];
                        if (name.equals(st.name)) {
                            size2 = size3;
                            noRecord2 = noRecord3;
                            this.mCpuUpTime += st.rel_uptime;
                            if (!this.mProcStTimeRecordMap.containsKey(name) || !this.mProcUtTimeRecordMap.containsKey(name)) {
                                sdf2 = sdf3;
                                percent = percent2;
                                myProcName = myProcName2;
                                this.mProcStTimeRecordMap.put(name, Long.valueOf(st.rel_stime));
                                this.mProcUtTimeRecordMap.put(name, Long.valueOf(st.rel_utime));
                                if (sDebug) {
                                    Slog.d(TAG, "ELSE: myProcName: " + name + " UTtime: " + st.rel_utime + " STtime: " + st.rel_stime + " Cputime: " + this.mCpuUpTime);
                                }
                                this.mShouldReport2Nhs = true;
                            } else {
                                long procRelutime = this.mProcUtTimeRecordMap.get(name).longValue() + st.rel_utime;
                                sdf2 = sdf3;
                                percent = percent2;
                                long procRelstime = this.mProcStTimeRecordMap.get(name).longValue() + st.rel_stime;
                                if (!sDebug) {
                                    myProcName = myProcName2;
                                } else {
                                    myProcName = myProcName2;
                                    Slog.d(TAG, " myProcName: " + name + " UTtime: " + procRelutime + " STtime: " + procRelstime + " Cputime: " + this.mCpuUpTime);
                                }
                                this.mProcStTimeRecordMap.put(name, Long.valueOf(procRelstime));
                                this.mProcUtTimeRecordMap.put(name, Long.valueOf(procRelutime));
                            }
                        } else {
                            sdf2 = sdf3;
                            size2 = size3;
                            noRecord2 = noRecord3;
                            percent = percent2;
                            myProcName = myProcName2;
                        }
                        i2++;
                        size3 = size2;
                        noRecord3 = noRecord2;
                        sdf3 = sdf2;
                        percent2 = percent;
                        myProcName2 = myProcName;
                    }
                    sdf = sdf3;
                    size = size3;
                    noRecord = noRecord3;
                    if (sDebug) {
                        Slog.d(TAG, " in collectAnbormalStats,  before send to NHS: mShouldReport2Nhs: " + this.mShouldReport2Nhs);
                    }
                    sendCpuInfo2Nhs();
                }
                noRecord3 = noRecord;
            }
            i++;
            size3 = size;
            sdf3 = sdf;
        }
        if (!noRecord3) {
            synchronized (sRecordList) {
                if (sDebug) {
                    Slog.d(TAG, "have record, collectAnbormalStats # " + sb.toString());
                }
                sRecordList.add(sb.toString());
            }
        }
        boolean needSave = false;
        long now = SystemClock.uptimeMillis();
        if (now - sLastWriteTime > 1800000) {
            if (sRecordList.size() > 0) {
                needSave = true;
            }
        } else if (sRecordList.size() > 30) {
            needSave = true;
        }
        if (needSave && (handler = this.mBgHandler) != null) {
            handler.sendEmptyMessage(1007);
        }
    }

    public void printCpuTrack(final PrintWriter pw) {
        Thread thread = new Thread(new Runnable() { // from class: com.android.internal.os.ProcessCpuTrackerExtImpl.3
            @Override // java.lang.Runnable
            public void run() {
                ProcessCpuTrackerExtImpl.this.tryPersistToDisk();
                pw.print("$CPU TRACK:v2\t uid pid name percent utime stime uptime\n");
                StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
                FileInputStream is = null;
                File[] filelist = {new File(ProcessCpuTrackerExtImpl.CPU_RECORD_OLD_FILE), new File(ProcessCpuTrackerExtImpl.CPU_RECORD_FILE)};
                byte[] buffer = new byte[4096];
                for (File file : filelist) {
                    try {
                        try {
                        } catch (FileNotFoundException e) {
                            Slog.w(ProcessCpuTrackerExtImpl.TAG, "FileNotFoundException: " + e);
                        } catch (IOException e2) {
                            Slog.w(ProcessCpuTrackerExtImpl.TAG, "IOException: " + e2);
                        }
                        if (file.exists()) {
                            is = new FileInputStream(file);
                            int len = 1;
                            while (len > 0) {
                                len = is.read(buffer);
                                if (len > 0) {
                                    pw.print(new String(buffer, 0, len));
                                }
                            }
                            is.close();
                            IoUtils.closeQuietly(is);
                            StrictMode.setThreadPolicy(savedPolicy);
                        }
                    } finally {
                        IoUtils.closeQuietly(is);
                        StrictMode.setThreadPolicy(savedPolicy);
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateLastSampleWallTime(long lastSampleWallTime) {
        this.mLastSampleWallTime = lastSampleWallTime;
    }

    public void updateCurrentSampleWallTime(long currentSampleWallTime) {
        this.mCurrentSampleWallTime = currentSampleWallTime;
    }

    private static boolean checkPermission() {
        if (sMyUid < 0) {
            sMyUid = Process.myUid();
        }
        if (sMyUid == 1000) {
            return true;
        }
        return false;
    }

    private void updateSystemTempreture() {
        sSystemTempreture = OplusHoraeThermalHelper.getInstance().getCurrentThermal();
        if (DEBUG) {
            Slog.d(TAG, "updateSystemTempreture: " + sSystemTempreture);
        }
        Handler handler = this.mBgHandler;
        if (handler != null) {
            handler.sendEmptyMessageDelayed(1008, 60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryPersistToDisk() {
        if (sDebug) {
            Slog.d(TAG, "tryPersistToDisk to be called, sRecordList.size(): " + sRecordList.size());
        }
        if (!checkPermission() || sRecordList.size() < 1) {
            return;
        }
        StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
        try {
            persistToDisk();
            sLastWriteTime = SystemClock.uptimeMillis();
        } finally {
            StrictMode.setThreadPolicy(savedPolicy);
        }
    }

    private boolean persistToDisk() {
        FileOutputStream fos;
        File logFile = new File(CPU_RECORD_FILE);
        File dir = logFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            if (logFile.length() < 204800) {
                fos = new FileOutputStream(logFile, true);
            } else {
                File oldFile = new File(CPU_RECORD_OLD_FILE);
                if (logFile.exists()) {
                    oldFile.delete();
                    logFile.renameTo(oldFile);
                }
                fos = new FileOutputStream(logFile);
            }
            try {
                try {
                    synchronized (sRecordList) {
                        Iterator<String> it = sRecordList.iterator();
                        while (it.hasNext()) {
                            String record = it.next();
                            fos.write(record.getBytes());
                        }
                        sRecordList.clear();
                    }
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    try {
                        fos.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return false;
                }
            } catch (Throwable th) {
                try {
                    fos.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            e5.printStackTrace();
            return false;
        }
    }

    public void updateProcStats(ArrayList<ProcessCpuTracker.Stats> stats) {
        this.mProcStats = stats;
    }

    public void updateLoad(float load1, float load5, float load15) {
        this.mLoad1 = load1;
        this.mLoad5 = load5;
        this.mLoad15 = load15;
    }

    public void updateMaxCpuInfo(ProcessCpuTracker.Stats st, int denom) {
        if (denom > 0) {
            st.cpuThousandths = ((st.rel_utime + st.rel_stime) * 1000) / denom;
        } else {
            st.cpuThousandths = 0;
        }
        if (this.maxCpuThousandths < st.cpuThousandths) {
            this.maxCpuThousandths = st.cpuThousandths;
            this.maxCpuProName = st.name;
        }
    }

    public void initMaxCpuInfo() {
        this.maxCpuProName = "null";
        this.maxCpuThousandths = -1;
    }

    public int getLoad1() {
        int load1 = (int) (this.mLoad1 * 10.0f);
        return load1;
    }

    public int getLoad5() {
        int load5 = (int) (this.mLoad5 * 10.0f);
        return load5;
    }

    public int getLoad15() {
        int load15 = (int) (this.mLoad15 * 10.0f);
        return load15;
    }

    public void buildWorkingProcs() {
        if (!this.mWorkingProcsSorted) {
            this.mWorkingProcs.clear();
            int N = this.mProcStats.size();
            for (int i = 0; i < N; i++) {
                ProcessCpuTracker.Stats stats = this.mProcStats.get(i);
                if (stats.working) {
                    this.mWorkingProcs.add(stats);
                    if (stats.threadStats != null && stats.threadStats.size() > 1) {
                        stats.workingThreads.clear();
                        int M = stats.threadStats.size();
                        for (int j = 0; j < M; j++) {
                            ProcessCpuTracker.Stats tstats = (ProcessCpuTracker.Stats) stats.threadStats.get(j);
                            if (tstats.working) {
                                stats.workingThreads.add(tstats);
                            }
                        }
                        Collections.sort(stats.workingThreads, sLoadComparator);
                    }
                }
            }
            Collections.sort(this.mWorkingProcs, sLoadComparator);
            this.mWorkingProcsSorted = true;
        }
    }

    public ArrayMap<String, String> getSimpleTopProcessesSnapShot() {
        long topInterval = OplusThermalManager.mHeatTopProInterval * 1000;
        boolean topEnabled = OplusThermalManager.mHeatTopProFeatureOn;
        if (topInterval < 60000) {
            topInterval = 3 * 60000;
        }
        if (topEnabled && SystemClock.elapsedRealtime() - this.mLastTopSampleTime >= topInterval) {
            this.mWorkingProcsSorted = false;
            this.mLastTopSampleTime = SystemClock.elapsedRealtime();
            buildWorkingProcs();
        }
        return this.mTopThreeProcessesSnapShot;
    }

    public void collectSimpleTopThreeProcessesInfo(ArrayList<ProcessCpuTracker.Stats> workingProcs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (this.mTopThreeProcessesSnapShot != null && workingProcs != null && workingProcs.size() >= OplusThermalManager.mHeatTopProCounts) {
            this.mTopThreeProcessesSnapShot.clear();
            int len = OplusThermalManager.mHeatTopProCounts;
            for (int i = 0; i < len; i++) {
                ProcessCpuTracker.Stats st = workingProcs.get(i);
                String ra = calcuteRatio(st);
                this.mTopThreeProcessesSnapShot.put(st.name, ra);
            }
            String lastSampleTime = sdf.format(Long.valueOf(this.mLastSampleWallTime));
            this.mTopThreeProcessesSnapShot.put("lastSamepleWallTime", lastSampleTime);
        }
    }

    public String getRatioString(long numerator, long denominator) {
        long thousands = (1000 * numerator) / denominator;
        long hundreds = thousands / 10;
        String ratio = Long.toString(hundreds);
        if (hundreds < 10) {
            long remainder = thousands - (10 * hundreds);
            if (remainder != 0) {
                return ratio + "." + Long.toString(remainder);
            }
            return ratio;
        }
        return ratio;
    }

    private String calcuteRatio(ProcessCpuTracker.Stats st) {
        int totalTime = (int) st.rel_uptime;
        if (totalTime == 0) {
            totalTime = 1;
        }
        int userTime = st.rel_utime;
        int systemTime = st.rel_stime;
        String ratio = getRatioString(userTime + systemTime, totalTime);
        return ratio;
    }

    public final int getMaxCpuThousandths() {
        return this.maxCpuThousandths;
    }

    public final String getMaxCpuProName() {
        return this.maxCpuProName;
    }
}
