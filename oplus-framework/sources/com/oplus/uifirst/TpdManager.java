package com.oplus.uifirst;

import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Message;
import android.os.Process;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.Log;
import android.util.Pair;
import com.oplus.filter.DynamicFilterManager;
import com.oplus.uifirst.Utils;
import com.oplus.util.OplusHoraeThermalHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class TpdManager {
    private static final String BAN_APP_TPD = "ban_app_tpd";
    private static final List<Utils.ThreadOp> EMPTY_TPD_THREAD_OP = new ArrayList(0);
    private static final int IM_FLAG_FORBID_SET_CPU_AFFINITY = 11;
    private static final int MAX_CLUSTER_NUMBER = 4;
    private static final int MAX_CPU_NUMBER = 10;
    private static final String TAG = "OplusUIFirst_TPD";
    private static final String TEMP_DECREASE = "de";
    private static final String TEMP_INCREASE = "in";
    private static final String TEMP_REACH_TO = "to";
    private static final int THERMAL_REGISTER_DELAY = 10000;
    private static final int THERMAL_REGISTER_RETRY = 3;
    private static final String THERMAL_TAG = "thermal ";
    private static final String mTpdTag = "tpd ";
    private int mClusterNum;
    private int[] mCpuClusters;
    private int mCpuNum;
    private final Handler mHandler;
    private int mLastThermalLevel = Integer.MIN_VALUE;
    private boolean mThermalRegistered = false;
    private int mThermalRegisteredRetry = 0;
    private ArrayMap<String, TpdThreadStat> mTpdThreadStat = new ArrayMap<>();
    private final OplusUIFirstManager mUifManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TpdManager(OplusUIFirstManager uifManager, Handler handler) {
        this.mClusterNum = 4;
        this.mCpuNum = 10;
        this.mUifManager = uifManager;
        this.mHandler = handler;
        lambda$registerThermalListenerIfNeed$0(handler);
        int[] iArr = new int[5];
        this.mCpuClusters = iArr;
        Process.getCpuClusters(iArr);
        int cpuNum = Process.getCpuNum();
        if (cpuNum > 0) {
            this.mCpuNum = cpuNum;
        }
        int clusterNum = 0;
        while (true) {
            int[] iArr2 = this.mCpuClusters;
            if (clusterNum >= iArr2.length) {
                break;
            }
            if (iArr2[clusterNum] != -1) {
                clusterNum++;
            } else {
                iArr2[clusterNum] = this.mCpuNum;
                break;
            }
        }
        if (clusterNum > 0) {
            this.mClusterNum = clusterNum;
        }
        if (Utils.DEBUG) {
            Log.d(TAG, "cpu clusters: " + Arrays.toString(this.mCpuClusters) + ", clusterNum: " + this.mClusterNum + ", cpuNum: " + this.mCpuNum);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: registerThermalListenerIfNeed, reason: merged with bridge method [inline-methods] */
    public void lambda$registerThermalListenerIfNeed$0(final Handler handler) {
        int i;
        if (this.mThermalRegistered) {
            return;
        }
        boolean addHoraeThermalStatusListener = OplusHoraeThermalHelper.getInstance().addHoraeThermalStatusListener((Executor) new HandlerExecutor(handler), new OplusHoraeThermalHelper.IThermalListener() { // from class: com.oplus.uifirst.TpdManager.1
            @Override // com.oplus.util.OplusHoraeThermalHelper.IThermalListener
            public void thermalLevel(int level) {
                if (TpdManager.this.mLastThermalLevel == Integer.MIN_VALUE) {
                    TpdManager.this.mLastThermalLevel = level;
                }
                if (level != TpdManager.this.mLastThermalLevel) {
                    TpdManager tpdManager = TpdManager.this;
                    tpdManager.handleThermalEvent(tpdManager.mLastThermalLevel, level);
                    TpdManager.this.mLastThermalLevel = level;
                }
            }

            @Override // com.oplus.util.OplusHoraeThermalHelper.IThermalListener
            public void notifyThermalBroadCast(int level, int temp) {
            }
        });
        this.mThermalRegistered = addHoraeThermalStatusListener;
        if (!addHoraeThermalStatusListener && (i = this.mThermalRegisteredRetry) <= 3) {
            this.mThermalRegisteredRetry = i + 1;
            handler.postDelayed(new Runnable() { // from class: com.oplus.uifirst.TpdManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TpdManager.this.lambda$registerThermalListenerIfNeed$0(handler);
                }
            }, this.mThermalRegisteredRetry * 2 * 10000);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setTpd(int pid, int tid, int decision) {
        int[] iArr;
        int i;
        BitSet cpuSet = new BitSet(this.mCpuNum);
        if (this.mClusterNum == 4) {
            int[] iArr2 = this.mCpuClusters;
            if (iArr2[0] == 0 && iArr2[1] == 2 && iArr2[2] == 5 && iArr2[3] == 7) {
                if (decision == 6) {
                    cpuSet.set(2, this.mCpuNum, true);
                } else if (decision == 3) {
                    cpuSet.set(0, 7, true);
                } else {
                    if ((decision & 1) != 0) {
                        cpuSet.set(0, 2, true);
                        cpuSet.set(5, 7, true);
                    }
                    if ((decision & 2) != 0) {
                        cpuSet.set(2, 7, true);
                    }
                    if ((decision & 4) != 0) {
                        cpuSet.set(7, this.mCpuNum, true);
                    }
                }
                if (!cpuSet.isEmpty()) {
                    Process.setThreadAffinity(tid, cpuSet.toByteArray());
                }
                if (!Utils.DEBUG) {
                    Log.d(TAG, "setTpd: " + decision + " tid:" + tid + " cpuset:" + cpuSet.toString());
                    return;
                }
                return;
            }
        }
        int clusterMask = 1;
        for (int i2 = 0; i2 < this.mClusterNum; i2++) {
            if ((decision & clusterMask) != 0 && (i = (iArr = this.mCpuClusters)[i2]) < iArr[i2 + 1]) {
                cpuSet.set(i, iArr[i2 + 1], true);
            }
            clusterMask <<= 1;
        }
        if (!cpuSet.isEmpty()) {
        }
        if (!Utils.DEBUG) {
        }
    }

    boolean thermalStateCheck(String state) {
        if (this.mLastThermalLevel != Integer.MIN_VALUE && state.startsWith(THERMAL_TAG)) {
            try {
                if (!state.regionMatches(THERMAL_TAG.length(), TEMP_INCREASE, 0, TEMP_INCREASE.length())) {
                    if (!state.regionMatches(THERMAL_TAG.length(), TEMP_DECREASE, 0, TEMP_DECREASE.length())) {
                        if (state.regionMatches(THERMAL_TAG.length(), TEMP_REACH_TO, 0, TEMP_REACH_TO.length())) {
                            int eventLevel = Integer.parseInt(state.substring(THERMAL_TAG.length() + TEMP_REACH_TO.length()));
                            if (eventLevel == this.mLastThermalLevel) {
                                return true;
                            }
                        }
                    } else {
                        int eventLevel2 = Integer.parseInt(state.substring(THERMAL_TAG.length() + TEMP_DECREASE.length()));
                        if (this.mLastThermalLevel <= eventLevel2) {
                            return true;
                        }
                    }
                } else {
                    int eventLevel3 = Integer.parseInt(state.substring(THERMAL_TAG.length() + TEMP_INCREASE.length()));
                    if (this.mLastThermalLevel >= eventLevel3) {
                        return true;
                    }
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Tpd thermal state error");
            }
        }
        return false;
    }

    synchronized void handleThermalEvent(int preLevel, int curLevel) {
        if (Utils.DEBUG) {
            Log.d(TAG, "Tpd Thermal level " + preLevel + " " + curLevel);
        }
        for (Map.Entry<String, TpdThreadStat> entry : this.mTpdThreadStat.entrySet()) {
            String packageName = entry.getKey();
            TpdThreadStat stat = entry.getValue();
            if (stat != null && stat.mOps != EMPTY_TPD_THREAD_OP) {
                if (Utils.DEBUG) {
                    Log.d(TAG, "Tpd ThermalEvent " + stat);
                }
                for (Utils.ThreadOp op : stat.mOps) {
                    if (op.mEvent.startsWith(THERMAL_TAG)) {
                        String event = op.mEvent;
                        try {
                            if (event.regionMatches(THERMAL_TAG.length(), TEMP_INCREASE, 0, TEMP_INCREASE.length())) {
                                if (curLevel > preLevel) {
                                    int eventLevel = Integer.parseInt(event.substring(THERMAL_TAG.length() + TEMP_INCREASE.length()));
                                    if (preLevel < eventLevel && curLevel >= eventLevel) {
                                        handleThreadOp(packageName, op);
                                    }
                                }
                            } else if (event.regionMatches(THERMAL_TAG.length(), TEMP_DECREASE, 0, TEMP_DECREASE.length())) {
                                if (curLevel < preLevel) {
                                    int eventLevel2 = Integer.parseInt(event.substring(THERMAL_TAG.length() + TEMP_DECREASE.length()));
                                    if (preLevel > eventLevel2 && curLevel <= eventLevel2) {
                                        handleThreadOp(packageName, op);
                                    }
                                }
                            } else if (event.regionMatches(THERMAL_TAG.length(), TEMP_REACH_TO, 0, TEMP_REACH_TO.length()) && Integer.parseInt(event.substring(THERMAL_TAG.length() + TEMP_REACH_TO.length())) == curLevel) {
                                handleThreadOp(packageName, op);
                            }
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Tpd handle event error");
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void handleActivityEvent(int status, String packageName, String activityName) {
        String event;
        switch (status) {
            case 0:
                event = Telephony.BaseMmsColumns.START;
                break;
            case 4:
                event = "resume";
                break;
            default:
                return;
        }
        updateTpdConfigIfNeed(packageName);
        TpdThreadStat stat = this.mTpdThreadStat.get(packageName);
        if (stat != null && stat.mOps != EMPTY_TPD_THREAD_OP) {
            if (Utils.DEBUG) {
                Log.d(TAG, "Tpd start activity " + Arrays.toString(stat.mOps.toArray()));
            }
            String activityTag = event + " ";
            for (Utils.ThreadOp op : stat.mOps) {
                if (op.mEvent != null && op.mEvent.startsWith(activityTag) && (TextUtils.isEmpty(op.mState) || thermalStateCheck(op.mState))) {
                    if (op.mEvent.regionMatches(activityTag.length(), activityName, 0, op.mEvent.length() - activityTag.length())) {
                        Message msg = Message.obtain();
                        msg.what = 7;
                        msg.obj = new Pair(packageName, op);
                        this.mHandler.sendMessageDelayed(msg, op.mDelay);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void handleProcessStart(String packageName, int uid, int pid, boolean isolated, String processName) {
        updateTpdConfigIfNeed(packageName);
        TpdThreadStat stat = this.mTpdThreadStat.get(packageName);
        if (stat != null && stat.mOps != EMPTY_TPD_THREAD_OP) {
            if (pid >= 0) {
                if (!isolated) {
                    stat.mPid.add(pid);
                } else {
                    stat.mIsolatedPid.add(pid);
                }
            }
            for (Utils.ThreadOp op : stat.mOps) {
                if (op.mEvent.startsWith("start_p") && (TextUtils.isEmpty(op.mState) || thermalStateCheck(op.mState))) {
                    if (op.mOp.equals(BAN_APP_TPD)) {
                        this.mUifManager.setImFlag(pid, 11);
                    } else {
                        Message msg = Message.obtain();
                        msg.what = 6;
                        msg.arg1 = pid;
                        msg.obj = new Pair(packageName, op);
                        this.mHandler.sendMessageDelayed(msg, op.mDelay);
                    }
                    if (Utils.DEBUG) {
                        Log.d(TAG, "tpd op " + op);
                    }
                }
            }
        }
    }

    private synchronized void updateTpdConfigIfNeed(String packageName) {
        TpdThreadStat stat;
        TpdThreadStat stat2 = this.mTpdThreadStat.get(packageName);
        if (stat2 == null) {
            if (!this.mUifManager.inFilter(DynamicFilterManager.FILTER_TPD, packageName)) {
                return;
            }
            String config = this.mUifManager.getFilterConfig(DynamicFilterManager.FILTER_TPD, packageName);
            if (Utils.DEBUG) {
                Log.d(TAG, "Tpd config " + config);
            }
            if (TextUtils.isEmpty(config)) {
                stat = new TpdThreadStat(EMPTY_TPD_THREAD_OP);
            } else {
                List<Utils.ThreadOp> ops = Utils.parseThreadOp(config);
                if (ops != null && ops.size() > 0) {
                    ops = Utils.filterOutPrj(ops);
                }
                if (Utils.DEBUG) {
                    String info = ops == null ? "null" : Arrays.toString(ops.toArray());
                    Log.d(TAG, "Tpd thread ops " + info);
                }
                if (ops != null && !ops.isEmpty()) {
                    stat = new TpdThreadStat(ops);
                }
                stat = new TpdThreadStat(EMPTY_TPD_THREAD_OP);
            }
            this.mTpdThreadStat.put(packageName, stat);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void moveToFore(String packageName, int appPid) {
        TpdThreadStat stat = this.mTpdThreadStat.get(packageName);
        if (stat != null && stat.mOps != EMPTY_TPD_THREAD_OP) {
            for (Utils.ThreadOp op : stat.mOps) {
                if (op.mEvent.startsWith("to_fore") && (TextUtils.isEmpty(op.mState) || thermalStateCheck(op.mState))) {
                    Message msg = Message.obtain();
                    msg.what = 6;
                    msg.arg1 = appPid;
                    msg.obj = new Pair(packageName, op);
                    this.mHandler.sendMessageDelayed(msg, op.mDelay);
                    if (Utils.DEBUG) {
                        Log.d(TAG, "tpd op " + op);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleThreadOp(String packageName, Utils.ThreadOp op) {
        TpdThreadStat stat;
        IntArray pidCloned;
        IntArray iolatedPidCloned;
        synchronized (this) {
            stat = this.mTpdThreadStat.get(packageName);
        }
        if (stat == null) {
            return;
        }
        if ("system".equals(packageName)) {
            handleSystemTaskOp(packageName, stat, op);
            return;
        }
        synchronized (this) {
            pidCloned = stat.mPid.clone();
            iolatedPidCloned = stat.mIsolatedPid.clone();
        }
        for (int i = 0; i < pidCloned.size(); i++) {
            handleThreadOp(packageName, pidCloned.get(i), op);
        }
        for (int i2 = 0; i2 < iolatedPidCloned.size(); i2++) {
            handleThreadOp(packageName, iolatedPidCloned.get(i2), op);
        }
    }

    void handleSystemTaskOp(String packageName, TpdThreadStat stat, Utils.ThreadOp op) {
        int size;
        IntArray pids;
        if (op.mOp.startsWith(mTpdTag)) {
            try {
                int value = Integer.parseInt(op.mOp.substring(mTpdTag.length()));
                synchronized (this) {
                    size = stat.mPid.size();
                }
                if (size <= 0) {
                    pids = Utils.findMatchedPids(op.mPattern, 0, 1000);
                    synchronized (this) {
                        stat.mPid.addAll(pids);
                    }
                } else {
                    synchronized (this) {
                        pids = stat.mPid.clone();
                    }
                }
                for (int i = 0; i < pids.size(); i++) {
                    int pid = pids.get(i);
                    setTpd(pid, pid, value);
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleThreadOp(String packageName, int pid, Utils.ThreadOp op) {
        if (op.mOp.startsWith(mTpdTag)) {
            try {
                int value = Integer.parseInt(op.mOp.substring(mTpdTag.length()));
                IntArray tids = Utils.findMatchedTids(pid, op.mPattern);
                if (Utils.DEBUG) {
                    Log.d(TAG, "TPD handleThreadOp find " + Arrays.toString(tids.toArray()));
                }
                if (tids.size() > 0) {
                    for (int i = 0; i < tids.size(); i++) {
                        int tid = tids.get(i);
                        setTpd(pid, tid, value);
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removePid(String packageName, int pid) {
        TpdThreadStat stat = this.mTpdThreadStat.get(packageName);
        if (stat != null) {
            int index = stat.mPid.indexOf(pid);
            if (index != -1) {
                stat.mPid.remove(index);
            }
            int index2 = stat.mIsolatedPid.indexOf(pid);
            if (index2 != -1) {
                stat.mIsolatedPid.remove(index2);
            }
            if (stat.mPid.size() == 0 && stat.mIsolatedPid.size() == 0) {
                this.mTpdThreadStat.remove(packageName);
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:95:0x02c3. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:59:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String verifyConfig(ArrayList<String> tags, ArrayList<String> values) {
        char c;
        Pattern pkgPn;
        char c2;
        char c3;
        if (tags.size() != values.size()) {
            return "config tags and values size don't match";
        }
        Pattern pkgPn2 = Pattern.compile("^[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*$");
        Iterator<String> it = tags.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (!pkgPn2.matcher(s).find()) {
                return s + " is invalid package name";
            }
        }
        for (int i = 0; i < tags.size(); i++) {
            for (int j = 0; j < tags.size(); j++) {
                if (i != j && tags.get(i).equals(tags.get(j))) {
                    return "duplicate package name: " + tags.get(i);
                }
            }
        }
        Iterator<String> it2 = values.iterator();
        while (it2.hasNext()) {
            String s2 = it2.next();
            if (!s2.startsWith("[{")) {
                return s2 + "\ndoesn't start with \"[{\"";
            }
            if (!s2.endsWith("}]")) {
                return s2 + "\ndoesn't end with \"}]\"";
            }
        }
        Pattern prjNumPn = Pattern.compile("^[0-9A-Z]+$");
        Iterator<String> it3 = values.iterator();
        while (it3.hasNext()) {
            String s3 = it3.next();
            try {
                Pair<List<Utils.ThreadOp>, String> p = Utils.parseThreadOpRaw(s3);
                if (p.second != null) {
                    try {
                        return s3 + "\n" + ((String) p.second);
                    } catch (IOException e) {
                        ioe = e;
                        return s3 + "\n" + ioe.getMessage();
                    }
                }
                List<Utils.ThreadOp> ops = (List) p.first;
                for (Utils.ThreadOp op : ops) {
                    if (!TextUtils.isEmpty(op.mPrjNum)) {
                        String[] projectNums = op.mPrjNum.split("\\|");
                        for (String prjNum : projectNums) {
                            if (!prjNumPn.matcher(prjNum).find()) {
                                return s3 + "\ncontains illegal project number: " + prjNum;
                            }
                        }
                    }
                    if (op.mOp.startsWith(mTpdTag)) {
                        try {
                            Integer.parseInt(op.mOp.substring(mTpdTag.length()));
                        } catch (NumberFormatException e2) {
                            return s3 + "\ncontains illegal op: " + op.mOp;
                        }
                    } else if (!op.mOp.equals(BAN_APP_TPD)) {
                        return s3 + "\ncontains unknown op: " + op.mOp;
                    }
                    try {
                        Pattern.compile(op.mPattern);
                        String event = op.mEvent;
                        if (event.endsWith(" ")) {
                            return s3 + "\nevent ends with space";
                        }
                        switch (event.hashCode()) {
                            case -1897185325:
                                if (event.equals("start_p")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case -1155036160:
                                if (event.equals("to_fore")) {
                                    c = 1;
                                    break;
                                }
                                break;
                        }
                        c = 65535;
                        Iterator<String> it4 = it3;
                        switch (c) {
                            case 0:
                            case 1:
                                pkgPn = pkgPn2;
                                break;
                            default:
                                if (event.startsWith(THERMAL_TAG)) {
                                    try {
                                        pkgPn = pkgPn2;
                                    } catch (NumberFormatException e3) {
                                    }
                                    try {
                                        if (event.regionMatches(THERMAL_TAG.length(), TEMP_INCREASE, 0, TEMP_INCREASE.length())) {
                                            Integer.parseInt(event.substring(THERMAL_TAG.length() + TEMP_INCREASE.length()));
                                        } else if (event.regionMatches(THERMAL_TAG.length(), TEMP_DECREASE, 0, TEMP_DECREASE.length())) {
                                            Integer.parseInt(event.substring(THERMAL_TAG.length() + TEMP_DECREASE.length()));
                                        } else if (event.regionMatches(THERMAL_TAG.length(), TEMP_REACH_TO, 0, TEMP_REACH_TO.length())) {
                                            Integer.parseInt(event.substring(THERMAL_TAG.length() + TEMP_REACH_TO.length()));
                                        } else {
                                            return s3 + "\ncontains unknown thermal event: " + event;
                                        }
                                        break;
                                    } catch (NumberFormatException e4) {
                                        return s3 + "\ncontains invalid thermal level: " + event;
                                    }
                                } else {
                                    pkgPn = pkgPn2;
                                    if (!event.startsWith(Telephony.BaseMmsColumns.START) && !event.startsWith("resume")) {
                                        return s3 + "\ncontains unknown event: " + event;
                                    }
                                }
                                break;
                        }
                        String str = op.mMode;
                        switch (str.hashCode()) {
                            case -1320294816:
                                if (str.equals("oneshot")) {
                                    c2 = 0;
                                    break;
                                }
                            default:
                                c2 = 65535;
                                break;
                        }
                        switch (c2) {
                            case 0:
                                if (op.mDelay > 10000) {
                                    return s3 + "\ncontains too long delay: " + op.mDelay;
                                }
                                String state = op.mState;
                                if (state.endsWith(" ")) {
                                    return s3 + "\nstate ends with space";
                                }
                                switch (state.hashCode()) {
                                    case 0:
                                        if (state.equals("")) {
                                            c3 = 0;
                                            break;
                                        }
                                        break;
                                }
                                c3 = 65535;
                                switch (c3) {
                                    case 0:
                                        break;
                                    default:
                                        if (state.startsWith(THERMAL_TAG)) {
                                            try {
                                                if (state.regionMatches(THERMAL_TAG.length(), TEMP_INCREASE, 0, TEMP_INCREASE.length())) {
                                                    Integer.parseInt(state.substring(THERMAL_TAG.length() + TEMP_INCREASE.length()));
                                                } else if (state.regionMatches(THERMAL_TAG.length(), TEMP_DECREASE, 0, TEMP_DECREASE.length())) {
                                                    Integer.parseInt(state.substring(THERMAL_TAG.length() + TEMP_DECREASE.length()));
                                                } else if (state.regionMatches(THERMAL_TAG.length(), TEMP_REACH_TO, 0, TEMP_REACH_TO.length())) {
                                                    Integer.parseInt(state.substring(THERMAL_TAG.length() + TEMP_REACH_TO.length()));
                                                } else {
                                                    return s3 + "\ncontains unknown thermal state: " + op.mState;
                                                }
                                                break;
                                            } catch (NumberFormatException e5) {
                                                return s3 + "\ncontains invalid thermal level: " + op.mState;
                                            }
                                        } else {
                                            continue;
                                        }
                                }
                                it3 = it4;
                                pkgPn2 = pkgPn;
                            default:
                                return s3 + "\ncontains unknown mode: " + op.mMode;
                        }
                        while (r7.hasNext()) {
                        }
                    } catch (Exception e6) {
                        return s3 + "\ncontains illegal pattern: " + op.mPattern;
                    }
                }
            } catch (IOException e7) {
                ioe = e7;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class TpdThreadStat {
        List<Utils.ThreadOp> mOps;
        IntArray mIsolatedPid = new IntArray();
        IntArray mPid = new IntArray();

        TpdThreadStat(List<Utils.ThreadOp> ops) {
            this.mOps = ops;
        }
    }
}
