package com.oplus.uifirst;

import android.os.Handler;
import android.os.Message;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.util.Pair;
import com.oplus.uifirst.OplusUIFirstManager;
import com.oplus.uifirst.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class FBThreadManager {
    private static final int ACTIVITY_PARA_SWITCH = 1;
    private static final String ALL_PACKAGE_NAME = "*";
    private static final String APP_PARA_OP_TAG = "app_para ";
    private static final boolean DEBUG = Utils.DEBUG;
    private static final String DISABLED_OP = "disabled";
    private static final String FILTER_OFB = "ofb";
    private static final String FILTER_OFB_PARA = "ofb_app_para";
    private static final String FILTER_OFB_TASK_PARA = "ofb_task_para";
    private static final String TAG = "OplusUIFirst_FB";
    private static final String TASK_PARA_OP_TAG = "task_para ";
    private static final int TOP_APP_PARA_SWITCH = 0;
    private final List<Utils.ThreadOp> DISABLED_FB_OP;
    private final List<Utils.ThreadOp> EMPTY_FB_THREAD_OP = new ArrayList(0);
    private FBPara mActivityPara;
    private String mActivityPkgName;
    private final ArrayMap<String, ArraySet<Integer>> mAppPidsMap;
    private final ArrayMap<String, FbThreadStat> mAppUxMap;
    private String mCurFbPkgName;
    private FBPara mDefaultPara;
    private boolean mFbFeatureDisabled;
    private final Handler mHandler;
    private boolean mIsDefaultPara;
    private FBPara mLastPara;
    private final ArrayMap<Integer, ArraySet<Integer>> mPidTidsMap;
    private boolean mPkgFbFeatureDisabled;
    private FBPara mTopAppPara;
    private final OplusUIFirstManager mUifManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FBThreadManager(OplusUIFirstManager uifManager, Handler handler) {
        List<Utils.ThreadOp> ops;
        ArrayList arrayList = new ArrayList(1);
        this.DISABLED_FB_OP = arrayList;
        this.mAppUxMap = new ArrayMap<>();
        this.mAppPidsMap = new ArrayMap<>();
        this.mPidTidsMap = new ArrayMap<>();
        this.mFbFeatureDisabled = false;
        this.mPkgFbFeatureDisabled = false;
        this.mCurFbPkgName = "";
        this.mUifManager = uifManager;
        this.mHandler = handler;
        Utils.ThreadOp disabledOp = new Utils.ThreadOp();
        disabledOp.mOp = DISABLED_OP;
        arrayList.add(disabledOp);
        String config = uifManager.getFilterConfig("ofb", ALL_PACKAGE_NAME);
        if (config != null && (ops = Utils.parseThreadOp(config)) != null && ops.size() > 0) {
            List<Utils.ThreadOp> ops2 = Utils.filterOutPrj(ops);
            if (ops2.size() > 0) {
                Optional<Utils.ThreadOp> result = ops2.stream().filter(new Predicate() { // from class: com.oplus.uifirst.FBThreadManager$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return FBThreadManager.lambda$new$0((Utils.ThreadOp) obj);
                    }
                }).findFirst();
                Utils.ThreadOp op = result.isPresent() ? result.get() : ops2.get(0);
                if (DISABLED_OP.equals(op.mOp)) {
                    this.mFbFeatureDisabled = true;
                    OplusUIFirstManager.nativeOfbCfgEnabled(false);
                } else {
                    OplusUIFirstManager.nativeOfbCfgEnabled(true);
                    setUpDefaultPara(op);
                }
            }
        }
        if (this.mDefaultPara == null) {
            this.mDefaultPara = new FBPara();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$new$0(Utils.ThreadOp op) {
        return !TextUtils.isEmpty(op.mPrjNum);
    }

    synchronized FbThreadStat getFbThreadStat(String packageName) {
        return this.mAppUxMap.get(packageName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addGlThread(String packageName, int pid, final int tid) {
        if (this.mFbFeatureDisabled) {
            return;
        }
        FbThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            return;
        }
        List<Utils.ThreadOp> ops = getFbThreadOps(packageName, "add_gl");
        if (ops != null && ops != this.EMPTY_FB_THREAD_OP && ops != this.DISABLED_FB_OP) {
            boolean matched = ops.stream().anyMatch(new Predicate() { // from class: com.oplus.uifirst.FBThreadManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean isMatchedTid;
                    isMatchedTid = Utils.isMatchedTid(tid, ((Utils.ThreadOp) obj).mPattern);
                    return isMatchedTid;
                }
            });
            if (matched) {
                addPid(packageName, pid);
                addTid2Pid(pid, tid);
                if (this.mCurFbPkgName.equals(packageName)) {
                    OplusUIFirstManager.nativeOfbCfgFrameTask(pid, tid, 1, 0, 0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeGlThread(String packageName, int pid, int tid) {
        if (this.mFbFeatureDisabled) {
            return;
        }
        FbThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            return;
        }
        removeTid4Pid(pid, tid);
        OplusUIFirstManager.nativeOfbCfgFrameTask(pid, tid, 0, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeAppPid(String packageName, int pid) {
        if (DEBUG) {
            Log.d(TAG, packageName + " remove pid: " + pid);
        }
        FbThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            return;
        }
        removePid(packageName, pid);
        this.mPidTidsMap.remove(Integer.valueOf(pid));
    }

    synchronized boolean appExists(String packageName) {
        return this.mAppUxMap.containsKey(packageName);
    }

    synchronized List<Utils.ThreadOp> getFbThreadOps(String packageName, final String event) {
        FbThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null && stat.mOps != null) {
            List<Utils.ThreadOp> list = stat.mOps;
            List<Utils.ThreadOp> list2 = this.EMPTY_FB_THREAD_OP;
            if (list == list2) {
                return list2;
            }
            List<Utils.ThreadOp> list3 = stat.mOps;
            List<Utils.ThreadOp> list4 = this.DISABLED_FB_OP;
            if (list3 == list4) {
                return list4;
            }
            return (List) stat.mOps.stream().filter(new Predicate() { // from class: com.oplus.uifirst.FBThreadManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean startsWith;
                    startsWith = ((Utils.ThreadOp) obj).mEvent.startsWith(event);
                    return startsWith;
                }
            }).collect(Collectors.toCollection(new Supplier() { // from class: com.oplus.uifirst.FBThreadManager$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final Object get() {
                    return new ArrayList();
                }
            }));
        }
        return null;
    }

    synchronized void setFbThreadOps(String packageName, List<Utils.ThreadOp> ops) {
        FbThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            this.mAppUxMap.put(packageName, new FbThreadStat(ops));
        } else {
            stat.mOps = ops;
        }
    }

    private void addPid(String packageName, int pid) {
        ArraySet<Integer> pids = this.mAppPidsMap.get(packageName);
        if (pids == null) {
            ArraySet<Integer> pids2 = new ArraySet<>();
            pids2.add(Integer.valueOf(pid));
            this.mAppPidsMap.put(packageName, pids2);
            return;
        }
        pids.add(Integer.valueOf(pid));
    }

    private void removePid(String packageName, int pid) {
        FbThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null) {
            int index = stat.mPid.indexOf(pid);
            if (index != -1) {
                stat.mPid.remove(index);
            }
            int index2 = stat.mIsolatedPid.indexOf(pid);
            if (index2 != -1) {
                stat.mIsolatedPid.remove(index2);
            }
        }
        ArraySet<Integer> pids = this.mAppPidsMap.get(packageName);
        if (pids != null) {
            pids.remove(Integer.valueOf(pid));
            if (pids.isEmpty()) {
                this.mAppPidsMap.remove(packageName);
            }
        }
    }

    private void addTid2Pid(int pid, int tid) {
        ArraySet<Integer> tids = this.mPidTidsMap.get(Integer.valueOf(pid));
        if (tids == null) {
            ArraySet<Integer> tids2 = new ArraySet<>();
            tids2.add(Integer.valueOf(tid));
            this.mPidTidsMap.put(Integer.valueOf(pid), tids2);
            return;
        }
        tids.add(Integer.valueOf(tid));
    }

    private void removeTid4Pid(int pid, int tid) {
        ArraySet<Integer> tids = this.mPidTidsMap.get(Integer.valueOf(pid));
        if (tids != null) {
            tids.remove(Integer.valueOf(tid));
            if (tids.isEmpty()) {
                this.mPidTidsMap.remove(Integer.valueOf(pid));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void moveToTop(OplusUIFirstManager.AppInfo info) {
        if (this.mFbFeatureDisabled) {
            return;
        }
        String packageName = info.mPackageName;
        List<Utils.ThreadOp> ops = getFbThreadOps(packageName, "to_top");
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, packageName + " moveToTop:" + ops);
            Utils.printThreadOps(TAG, ops);
        }
        if (ops == this.DISABLED_FB_OP) {
            this.mPkgFbFeatureDisabled = true;
            OplusUIFirstManager.nativeOfbCfgEnabled(false);
            this.mCurFbPkgName = "";
        } else {
            if (this.mPkgFbFeatureDisabled) {
                this.mPkgFbFeatureDisabled = false;
                OplusUIFirstManager.nativeOfbCfgEnabled(true);
            }
            int hwuitask0 = 0;
            int hwuitask1 = 0;
            if (!info.mInputMethodRtg && info.mHwuiTasks != null && info.mHwuiTasks.size() >= 2) {
                hwuitask0 = info.mHwuiTasks.get(0);
                hwuitask1 = info.mHwuiTasks.get(1);
            }
            this.mCurFbPkgName = packageName;
            this.mUifManager.ofbBoostHint(info.mAppPid, info.mRenderThreadTid, hwuitask0, hwuitask1, info.mInputMethodRtg ? 400 : 201, 0, 0L, 0L, 0L);
            if (z) {
                Log.d(TAG, packageName + " boost hint: " + info.mAppPid);
            }
        }
        FBPara topAppPara = null;
        if (ops != null && ops != this.EMPTY_FB_THREAD_OP && ops != this.DISABLED_FB_OP) {
            Iterator<Utils.ThreadOp> it = ops.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Utils.ThreadOp op = it.next();
                String opTag = op.mOp;
                if (opTag.startsWith(APP_PARA_OP_TAG)) {
                    topAppPara = fetchAppPara(opTag.substring(APP_PARA_OP_TAG.length()));
                    break;
                }
            }
        }
        handleParaSwitch(packageName, topAppPara, null, 0);
        ArraySet<Integer> pids = this.mAppPidsMap.get(packageName);
        if (pids != null) {
            Iterator<Integer> it2 = pids.iterator();
            while (it2.hasNext()) {
                int pid = it2.next().intValue();
                ArraySet<Integer> tids = this.mPidTidsMap.get(Integer.valueOf(pid));
                if (tids != null) {
                    Iterator<Integer> it3 = tids.iterator();
                    while (it3.hasNext()) {
                        int tid = it3.next().intValue();
                        if (DEBUG) {
                            Log.d(TAG, packageName + " boost task: " + pid + " " + tid);
                        }
                        OplusUIFirstManager.nativeOfbCfgFrameTask(pid, tid, 1, 0, 0);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveToBack(String packageName, int appPid) {
        if (!this.mFbFeatureDisabled && DEBUG) {
            Log.d(TAG, packageName + " moveToBack " + appPid);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveToFore(String packageName, int appPid) {
        char c;
        if (this.mFbFeatureDisabled) {
            return;
        }
        List<Utils.ThreadOp> toForeOps = getFbThreadOps(packageName, "to_fore");
        if (DEBUG) {
            Log.d(TAG, packageName + " toForeOps: " + toForeOps);
            Utils.printThreadOps(TAG, toForeOps);
        }
        if (toForeOps != null) {
            for (Utils.ThreadOp op : toForeOps) {
                if (op.mMode != null) {
                    String str = op.mMode;
                    switch (str.hashCode()) {
                        case -1320294816:
                            if (str.equals("oneshot")) {
                                c = 1;
                                break;
                            }
                            break;
                        case -934531685:
                            if (str.equals("repeat")) {
                                c = 0;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    switch (c) {
                        case 0:
                            Message msg = Message.obtain();
                            msg.what = 9;
                            msg.obj = new Pair(packageName, op);
                            this.mHandler.sendMessageDelayed(msg, op.mDelay);
                            if (DEBUG) {
                                Log.d(TAG, "repeat " + op);
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            FbThreadStat stat = getFbThreadStat(packageName);
                            if (stat != null && !stat.mHasChecked) {
                                stat.mHasChecked = true;
                                Message msg2 = Message.obtain();
                                msg2.what = 9;
                                msg2.obj = new Pair(packageName, op);
                                this.mHandler.sendMessageDelayed(msg2, op.mDelay);
                                if (DEBUG) {
                                    Log.d(TAG, "oneshot " + op);
                                    break;
                                } else {
                                    break;
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void handleActivityEvent(int status, String packageName, String activityName) {
        String event;
        FBPara activityPara;
        if (this.mFbFeatureDisabled) {
            return;
        }
        switch (status) {
            case 0:
                event = Telephony.BaseMmsColumns.START;
                break;
            case 4:
                event = "resume";
                break;
            case 6:
                event = "pause";
                break;
            default:
                return;
        }
        List<Utils.ThreadOp> ops = getFbThreadOps(packageName, event);
        if (DEBUG) {
            Log.d(TAG, packageName + ", activity event: " + ops);
            Utils.printThreadOps(TAG, ops);
        }
        if (ops != null && ops != this.EMPTY_FB_THREAD_OP) {
            if (ops != this.DISABLED_FB_OP) {
                FBPara activityPara2 = null;
                String activityTag = event + " ";
                for (Utils.ThreadOp op : ops) {
                    if (op.mEvent == null || !op.mEvent.startsWith(activityTag)) {
                        activityPara = activityPara2;
                    } else if (!op.mEvent.regionMatches(activityTag.length(), activityName, 0, op.mEvent.length() - activityTag.length())) {
                        activityPara = activityPara2;
                    } else {
                        String opTag = op.mOp;
                        if (opTag.startsWith(APP_PARA_OP_TAG)) {
                            activityPara2 = fetchAppPara(opTag.substring(APP_PARA_OP_TAG.length()));
                        } else if (!opTag.startsWith(TASK_PARA_OP_TAG)) {
                            activityPara = activityPara2;
                        } else {
                            Message msg = Message.obtain();
                            msg.what = 9;
                            msg.obj = new Pair(packageName, op);
                            activityPara = activityPara2;
                            this.mHandler.sendMessageDelayed(msg, op.mDelay);
                        }
                    }
                    activityPara2 = activityPara;
                }
                FBPara activityPara3 = activityPara2;
                if (4 == status) {
                    handleParaSwitch(packageName, null, activityPara3, 1);
                }
                return;
            }
        }
        if (4 == status) {
            handleParaSwitch(packageName, null, null, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleFbThreadOp(String packageName, Utils.ThreadOp op) {
        IntArray pidCloned;
        IntArray iolatedPidCloned;
        FbThreadStat stat = getFbThreadStat(packageName);
        if (stat == null) {
            return;
        }
        synchronized (this) {
            pidCloned = stat.mPid.clone();
            iolatedPidCloned = stat.mIsolatedPid.clone();
        }
        for (int i = 0; i < pidCloned.size(); i++) {
            handleFbThreadOp(packageName, pidCloned.get(i), op);
        }
        for (int i2 = 0; i2 < iolatedPidCloned.size(); i2++) {
            handleFbThreadOp(packageName, iolatedPidCloned.get(i2), op);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleFbThreadOp(String packageName, int pid, Utils.ThreadOp op) {
        FbThreadStat stat;
        synchronized (this) {
            stat = this.mAppUxMap.get(packageName);
        }
        if (stat == null) {
            return;
        }
        IntArray tids = Utils.findMatchedTids(pid, op.mPattern);
        if (tids.size() <= 0) {
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "handleFbThreadOp: " + op + ", find: " + Arrays.toString(tids.toArray()));
        }
        String[] section = op.mOp.split("\\s", 2);
        if (section.length >= 2) {
            String taskOp = section[1];
            if (taskOp.equals("def")) {
                for (int i = 0; i < tids.size(); i++) {
                    int tid = tids.get(i);
                    synchronized (this) {
                        addPid(packageName, pid);
                        addTid2Pid(pid, tid);
                    }
                    if (this.mCurFbPkgName.equals(packageName)) {
                        OplusUIFirstManager.nativeOfbCfgFrameTask(pid, tid, 1, 0, 0);
                    }
                }
                return;
            }
            if (taskOp.equals("remove")) {
                for (int i2 = 0; i2 < tids.size(); i2++) {
                    int tid2 = tids.get(i2);
                    synchronized (this) {
                        removeTid4Pid(pid, tid2);
                    }
                    OplusUIFirstManager.nativeOfbCfgFrameTask(pid, tid2, 0, 0, 0);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void handleProcessStart(String packageName, int uid, int pid, boolean isolated, String processName) {
        if (this.mFbFeatureDisabled) {
            return;
        }
        boolean z = DEBUG;
        if (z) {
            Log.d(TAG, packageName + " proc start: " + uid + " " + pid + " " + isolated + " " + processName);
        }
        if (!appExists(packageName)) {
            String config = this.mUifManager.getFilterConfig("ofb", packageName);
            if (z) {
                Log.d(TAG, packageName + " config: " + config);
            }
            if (config == null) {
                setFbThreadOps(packageName, this.EMPTY_FB_THREAD_OP);
            } else {
                List<Utils.ThreadOp> ops = Utils.parseThreadOp(config);
                if (ops != null && ops.size() > 0) {
                    ops = Utils.filterOutPrj(ops);
                }
                if (ops != null && !ops.isEmpty()) {
                    boolean disabled = false;
                    Iterator<Utils.ThreadOp> it = ops.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (DISABLED_OP.equals(it.next().mOp)) {
                            setFbThreadOps(packageName, this.DISABLED_FB_OP);
                            disabled = true;
                            break;
                        }
                    }
                    if (!disabled) {
                        setFbThreadOps(packageName, ops);
                    }
                }
                setFbThreadOps(packageName, this.EMPTY_FB_THREAD_OP);
            }
        }
        FbThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null) {
            if (!isolated) {
                stat.mUid = uid;
                stat.mPid.add(pid);
            } else {
                stat.mIsolatedPid.add(pid);
            }
            stat.mHasChecked = false;
        }
        List<Utils.ThreadOp> ops2 = getFbThreadOps(packageName, "start_p");
        if (DEBUG) {
            Log.d(TAG, packageName + " start process: " + ops2);
            Utils.printThreadOps(TAG, ops2);
        }
        if (ops2 != null && ops2 != this.EMPTY_FB_THREAD_OP && ops2 != this.DISABLED_FB_OP) {
            for (Utils.ThreadOp op : ops2) {
                Message msg = Message.obtain();
                msg.what = 8;
                msg.arg1 = pid;
                msg.obj = new Pair(packageName, op);
                this.mHandler.sendMessageDelayed(msg, op.mDelay);
                if (DEBUG) {
                    Log.d(TAG, "handleProcessStart " + op);
                }
            }
        }
    }

    synchronized void setUpDefaultPara(Utils.ThreadOp op) {
        if (DEBUG) {
            Log.d(TAG, "handleDefaultPara " + op);
        }
        String opTag = op.mOp;
        if (opTag.startsWith(APP_PARA_OP_TAG)) {
            this.mDefaultPara = fetchAppPara(opTag.substring(APP_PARA_OP_TAG.length()));
        }
        restoreDefaultPara();
    }

    synchronized void handleParaSwitch(String packageName, FBPara topPara, FBPara activityPara, int paraSwitch) {
        FBPara fBPara;
        if (DEBUG) {
            Log.d(TAG, "handleParaSwitch " + packageName + " " + topPara + " " + activityPara + " " + paraSwitch);
        }
        boolean isActivity = true;
        if (paraSwitch == 0) {
            this.mTopAppPara = topPara;
            if ("com.android.systemui".equals(packageName)) {
                isActivity = false;
            } else if (this.mActivityPara != null && packageName != null && !packageName.equals(this.mActivityPkgName)) {
                this.mActivityPkgName = null;
                this.mActivityPara = null;
            }
        } else {
            if (paraSwitch != 1) {
                return;
            }
            boolean isSamePkg = Objects.equals(this.mActivityPkgName, packageName);
            this.mActivityPara = activityPara;
            this.mActivityPkgName = packageName;
            if (!isSamePkg && activityPara == null) {
                return;
            }
        }
        if (isActivity && (fBPara = this.mActivityPara) != null) {
            setUpPara(fBPara);
        } else {
            FBPara fBPara2 = this.mTopAppPara;
            if (fBPara2 != null) {
                setUpPara(fBPara2);
            } else {
                restoreDefaultPara();
            }
        }
    }

    private FBPara fetchAppPara(String paraCfg) {
        String config = this.mUifManager.getFilterConfig("ofb_app_para", paraCfg);
        if (DEBUG) {
            Log.d(TAG, "handlePara " + paraCfg + " " + config);
        }
        if (config == null) {
            return null;
        }
        FBPara para = parseAppPara(config);
        return para;
    }

    private static Pair<FBPara, String> parseAppParaRaw(String config, FBPara defPara) throws IOException {
        char c;
        FBPara para = new FBPara(defPara);
        String info = null;
        JsonReader reader = new JsonReader(new StringReader(config));
        reader.beginObject();
        while (reader.peek() == JsonToken.NAME) {
            String jsonName = reader.nextName();
            switch (jsonName.hashCode()) {
                case -1569694451:
                    if (jsonName.equals("ed_boost_max_d")) {
                        c = '\t';
                        break;
                    }
                    break;
                case -1569475343:
                    if (jsonName.equals("ed_boost_mid_d")) {
                        c = 7;
                        break;
                    }
                    break;
                case -1225337771:
                    if (jsonName.equals("ed_boost_mid_util")) {
                        c = '\b';
                        break;
                    }
                    break;
                case -1035421231:
                    if (jsonName.equals("util_min_ov")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1035421090:
                    if (jsonName.equals("util_min_th")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1035421083:
                    if (jsonName.equals("util_min_to")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1020245412:
                    if (jsonName.equals("ed_boost_to_d")) {
                        c = 11;
                        break;
                    }
                    break;
                case -521578400:
                    if (jsonName.equals("sf_migr_gpu")) {
                        c = 15;
                        break;
                    }
                    break;
                case -496932143:
                    if (jsonName.equals("sf_freq_gpu")) {
                        c = 14;
                        break;
                    }
                    break;
                case -114988503:
                    if (jsonName.equals("util_fr")) {
                        c = 3;
                        break;
                    }
                    break;
                case 837150393:
                    if (jsonName.equals("ed_boost_max_util")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 1304176213:
                    if (jsonName.equals("vutil_margin")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1731557780:
                    if (jsonName.equals("boost_freq")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1731757731:
                    if (jsonName.equals("boost_migr")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1994767588:
                    if (jsonName.equals("sf_freq")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 1994967539:
                    if (jsonName.equals("sf_migr")) {
                        c = '\r';
                        break;
                    }
                    break;
            }
            c = 65535;
            switch (c) {
                case 0:
                    para.boostFreq = reader.nextInt();
                    break;
                case 1:
                    para.boostMigr = reader.nextInt();
                    break;
                case 2:
                    para.vutilMargin = reader.nextInt();
                    break;
                case 3:
                    para.utilFrameRate = reader.nextInt();
                    break;
                case 4:
                    para.utilMinThreshold = reader.nextInt();
                    break;
                case 5:
                    para.utilMinObtainView = reader.nextInt();
                    break;
                case 6:
                    para.utilMinTimeout = reader.nextInt();
                    break;
                case 7:
                    para.edBoostMidDuration = reader.nextInt();
                    break;
                case '\b':
                    para.edBoostMidUtil = reader.nextInt();
                    break;
                case '\t':
                    para.edBoostMaxDuration = reader.nextInt();
                    break;
                case '\n':
                    para.edBoostMaxUtil = reader.nextInt();
                    break;
                case 11:
                    para.edBoostTimeOutDuration = reader.nextInt();
                    break;
                case '\f':
                    para.boostSfFreqNongpu = reader.nextInt();
                    break;
                case '\r':
                    para.boostSfMigrNongpu = reader.nextInt();
                    break;
                case 14:
                    para.boostSfFreqGpu = reader.nextInt();
                    break;
                case 15:
                    para.boostSfMigrGpu = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
                    info = "unknown para: " + jsonName;
                    break;
            }
        }
        reader.endObject();
        return new Pair<>(para, info);
    }

    private FBPara parseAppPara(String config) {
        try {
            Pair<FBPara, String> p = parseAppParaRaw(config, this.mDefaultPara);
            return (FBPara) p.first;
        } catch (Exception e) {
            Log.e(TAG, "para json config error:" + config);
            return null;
        }
    }

    private TaskPara fetchTaskPara(String paraCfg) {
        String config = this.mUifManager.getFilterConfig("ofb_task_para", paraCfg);
        if (DEBUG) {
            Log.d(TAG, "handlePara " + paraCfg + " " + config);
        }
        if (config == null) {
            return null;
        }
        TaskPara para = parseTaskPara(config);
        return para;
    }

    private TaskPara parseTaskPara(String config) {
        char c;
        TaskPara para = new TaskPara();
        try {
            JsonReader reader = new JsonReader(new StringReader(config));
            try {
                reader.beginObject();
                while (reader.peek() == JsonToken.NAME) {
                    String jsonName = reader.nextName();
                    switch (jsonName.hashCode()) {
                        case 95472323:
                            if (jsonName.equals("depth")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 113126854:
                            if (jsonName.equals("width")) {
                                c = 1;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    switch (c) {
                        case 0:
                            para.depth = reader.nextInt();
                            break;
                        case 1:
                            para.width = reader.nextInt();
                            break;
                        default:
                            reader.skipValue();
                            Log.w(TAG, "unknown para: " + jsonName);
                            break;
                    }
                }
                reader.endObject();
                reader.close();
                return para;
            } finally {
            }
        } catch (Exception e) {
            Log.e(TAG, "para json config error:" + config);
            return null;
        }
    }

    private void setUpPara(FBPara para) {
        if (para == null) {
            return;
        }
        if (para != this.mDefaultPara) {
            this.mIsDefaultPara = false;
        }
        if (para.equals(this.mLastPara)) {
            return;
        }
        this.mLastPara = para;
        OplusUIFirstManager.nativeOfbCfgAppFrameParam(para.boostFreq, para.boostMigr, para.vutilMargin, para.utilFrameRate, para.utilMinThreshold, para.utilMinObtainView, para.utilMinTimeout, para.edBoostMidDuration, para.edBoostMidUtil, para.edBoostMaxDuration, para.edBoostMaxUtil, para.edBoostTimeOutDuration, para.boostSfFreqNongpu, para.boostSfMigrNongpu, para.boostSfFreqGpu, para.boostSfMigrGpu);
    }

    private void restoreDefaultPara() {
        if (!this.mIsDefaultPara) {
            setUpPara(this.mDefaultPara);
            this.mIsDefaultPara = true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:117:0x02ae, code lost:
    
        if (r12.equals("repeat") != false) goto L140;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:99:0x0243. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0130  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String verifyConfig(ArrayList<String> tags, ArrayList<String> values, ArrayList<String> appParaTags, ArrayList<String> appParaValues, ArrayList<String> taskParaTags, ArrayList<String> taskParaValues) {
        char c;
        char c2;
        if (tags.size() != values.size()) {
            return "config tags and values size don't match";
        }
        Pattern pkgPn = Pattern.compile("^[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*$");
        Iterator<String> it = tags.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (!s.equals(ALL_PACKAGE_NAME) && !s.equals("DUMMY_INPUT_METHOD") && !pkgPn.matcher(s).find()) {
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
        Pattern paraNamePn = Pattern.compile("^[A-Za-z0-9_]+$");
        Pattern prjNumPn = Pattern.compile("^[0-9A-Z]+$");
        Iterator<String> it3 = values.iterator();
        while (it3.hasNext()) {
            String s3 = it3.next();
            try {
                Pair<List<Utils.ThreadOp>, String> p = Utils.parseThreadOpRaw(s3);
                if (p.second != null) {
                    return s3 + "\n" + ((String) p.second);
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
                    String opTag = op.mOp;
                    switch (opTag.hashCode()) {
                        case 270940796:
                            if (opTag.equals(DISABLED_OP)) {
                                c = 0;
                                break;
                            }
                            break;
                    }
                    c = 65535;
                    char c3 = 1;
                    switch (c) {
                        case 0:
                            break;
                        default:
                            if (opTag.startsWith(APP_PARA_OP_TAG)) {
                                String appPara = opTag.substring(APP_PARA_OP_TAG.length());
                                Matcher m = paraNamePn.matcher(appPara);
                                if (m.find()) {
                                    if (!appParaTags.contains(appPara)) {
                                        return s3 + "\ncan't find config app_para: " + appPara;
                                    }
                                } else {
                                    return s3 + "\ncontains unknown op: " + opTag;
                                }
                            } else {
                                if (opTag.startsWith(TASK_PARA_OP_TAG)) {
                                    String[] section = opTag.split("\\s", 2);
                                    if (section.length >= 2) {
                                        String taskOp = section[1];
                                        if (!taskOp.equals("def") && !taskOp.equals("remove")) {
                                        }
                                    }
                                }
                                return s3 + "\ncontains unknown op: " + opTag;
                            }
                            break;
                    }
                    try {
                        Pattern.compile(op.mPattern);
                        String str = op.mEvent;
                        switch (str.hashCode()) {
                            case -1897185325:
                                if (str.equals("start_p")) {
                                    c2 = 1;
                                    break;
                                }
                                break;
                            case -1422513853:
                                if (str.equals("add_gl")) {
                                    c2 = 5;
                                    break;
                                }
                                break;
                            case -1155036160:
                                if (str.equals("to_fore")) {
                                    c2 = 2;
                                    break;
                                }
                                break;
                            case -1109843021:
                                if (str.equals("launch")) {
                                    c2 = 3;
                                    break;
                                }
                                break;
                            case -868529775:
                                if (str.equals("to_top")) {
                                    c2 = 4;
                                    break;
                                }
                                break;
                            case 0:
                                if (str.equals("")) {
                                    c2 = 0;
                                    break;
                                }
                                break;
                        }
                        c2 = 65535;
                        switch (c2) {
                            default:
                                if (!op.mEvent.startsWith(Telephony.BaseMmsColumns.START) && !op.mEvent.startsWith("resume") && !op.mEvent.startsWith("pause")) {
                                    return s3 + "\ncontains unknown event: " + op.mEvent;
                                }
                                if (op.mEvent.endsWith(" ")) {
                                    return s3 + "\nends with space";
                                }
                                break;
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                String str2 = op.mMode;
                                switch (str2.hashCode()) {
                                    case -1320294816:
                                        if (str2.equals("oneshot")) {
                                            c3 = 0;
                                            break;
                                        }
                                        break;
                                    case -934531685:
                                        break;
                                }
                                c3 = 65535;
                                switch (c3) {
                                    case 0:
                                    case 1:
                                        if (op.mDelay > 10000) {
                                            return s3 + "\ncontains too long delay: " + op.mDelay;
                                        }
                                    default:
                                        return s3 + "\ncontains unknown mode: " + op.mMode;
                                }
                                while (r8.hasNext()) {
                                }
                        }
                    } catch (Exception e) {
                        return s3 + "\ncontains illegal pattern: " + op.mPattern;
                    }
                }
            } catch (IOException ioe) {
                return s3 + "\n" + ioe.getMessage();
            }
        }
        return verifyAppParaConfig(appParaTags, appParaValues);
    }

    static String verifyAppParaConfig(ArrayList<String> tags, ArrayList<String> values) {
        if (tags.size() != values.size()) {
            return "config tags and values size don't match";
        }
        Pattern paraNamePn = Pattern.compile("^[A-Za-z0-9_]+$");
        Iterator<String> it = tags.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (!paraNamePn.matcher(s).find()) {
                return s + " is invalid para name";
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
            if (!s2.startsWith("{")) {
                return s2 + "\ndoesn't start with \"{\"";
            }
            if (!s2.endsWith("}")) {
                return s2 + "\ndoesn't end with \"}\"";
            }
        }
        Iterator<String> it3 = values.iterator();
        while (it3.hasNext()) {
            String s3 = it3.next();
            try {
                Pair<FBPara, String> p = parseAppParaRaw(s3, null);
                if (p.second != null) {
                    return s3 + "\n" + ((String) p.second);
                }
            } catch (IOException ioe) {
                return s3 + "\n" + ioe.getMessage();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FBPara {
        int boostFreq;
        int boostMigr;
        int boostSfFreqGpu;
        int boostSfFreqNongpu;
        int boostSfMigrGpu;
        int boostSfMigrNongpu;
        int edBoostMaxDuration;
        int edBoostMaxUtil;
        int edBoostMidDuration;
        int edBoostMidUtil;
        int edBoostTimeOutDuration;
        int utilFrameRate;
        int utilMinObtainView;
        int utilMinThreshold;
        int utilMinTimeout;
        int vutilMargin;

        FBPara() {
            this.boostFreq = 0;
            this.boostMigr = 0;
            this.vutilMargin = 0;
            this.utilFrameRate = 90;
            this.utilMinThreshold = 205;
            this.utilMinObtainView = 600;
            this.utilMinTimeout = 500;
            this.edBoostMidDuration = 60;
            this.edBoostMidUtil = 600;
            this.edBoostMaxDuration = 80;
            this.edBoostMaxUtil = 900;
            this.edBoostTimeOutDuration = 200;
            this.boostSfFreqNongpu = 0;
            this.boostSfMigrNongpu = 0;
            if (Utils.getVendorPlatform() == Platform.MTK) {
                this.boostSfFreqGpu = 60;
                this.boostSfMigrGpu = 60;
            } else {
                this.boostSfFreqGpu = 30;
                this.boostSfMigrGpu = 30;
            }
        }

        FBPara(FBPara para) {
            this();
            if (para != null) {
                this.boostFreq = para.boostFreq;
                this.boostMigr = para.boostMigr;
                this.vutilMargin = para.vutilMargin;
                this.utilFrameRate = para.utilFrameRate;
                this.utilMinThreshold = para.utilMinThreshold;
                this.utilMinObtainView = para.utilMinObtainView;
                this.utilMinTimeout = para.utilMinTimeout;
                this.edBoostMidDuration = para.edBoostMidDuration;
                this.edBoostMidUtil = para.edBoostMidUtil;
                this.edBoostMaxDuration = para.edBoostMaxDuration;
                this.edBoostMaxUtil = para.edBoostMaxUtil;
                this.edBoostTimeOutDuration = para.edBoostTimeOutDuration;
                this.boostSfFreqNongpu = para.boostSfFreqNongpu;
                this.boostSfMigrNongpu = para.boostSfMigrNongpu;
                this.boostSfFreqGpu = para.boostSfFreqGpu;
                this.boostSfMigrGpu = para.boostSfMigrGpu;
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FBPara)) {
                return false;
            }
            FBPara para = (FBPara) obj;
            return para.boostFreq == this.boostFreq && para.boostMigr == this.boostMigr && para.vutilMargin == this.vutilMargin && para.utilFrameRate == this.utilFrameRate && para.utilMinThreshold == this.utilMinThreshold && para.utilMinObtainView == this.utilMinObtainView && para.utilMinTimeout == this.utilMinTimeout && para.edBoostMidDuration == this.edBoostMidDuration && para.edBoostMidUtil == this.edBoostMidUtil && para.edBoostMaxDuration == this.edBoostMaxDuration && para.edBoostMaxUtil == this.edBoostMaxUtil && para.edBoostTimeOutDuration == this.edBoostTimeOutDuration && para.boostSfFreqNongpu == this.boostSfFreqNongpu && para.boostSfMigrNongpu == this.boostSfMigrNongpu && para.boostSfFreqGpu == this.boostSfFreqGpu && para.boostSfMigrGpu == this.boostSfMigrGpu;
        }

        public int hashCode() {
            return this.boostFreq | (this.boostMigr << 8) | (this.vutilMargin << 16) | (((((((((((((this.utilFrameRate ^ this.utilMinThreshold) ^ this.utilMinObtainView) ^ this.utilMinTimeout) ^ this.edBoostMidDuration) ^ this.edBoostMidUtil) ^ this.edBoostMaxDuration) ^ this.edBoostMaxUtil) ^ this.edBoostTimeOutDuration) ^ this.boostSfFreqNongpu) ^ this.boostSfMigrNongpu) ^ this.boostSfFreqGpu) ^ this.boostSfMigrGpu) << 24);
        }

        public String toString() {
            return "FBPara: boostFreq:" + this.boostFreq + ", boostMigr:" + this.boostMigr + ", vutilMargin:" + this.vutilMargin + ", utilFrameRate:" + this.utilFrameRate + ", utilMinThreshold:" + this.utilMinThreshold + ", utilMinObtainView:" + this.utilMinObtainView + ", utilMinTimeout:" + this.utilMinTimeout + ", edBoostMidDuration:" + this.edBoostMidDuration + ", edBoostMidUtil:" + this.edBoostMidUtil + ", edBoostMaxDuration:" + this.edBoostMaxDuration + ", edBoostMaxUtil:" + this.edBoostMaxUtil + ", edBoostTimeOutDuration:" + this.edBoostTimeOutDuration + ", boostSfFreqNongpu:" + this.boostSfFreqNongpu + ", boostSfMigrNongpu:" + this.boostSfMigrNongpu + ", boostSfFreqGpu:" + this.boostSfFreqGpu + ", boostSfMigrGpu:" + this.boostSfMigrGpu;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class TaskPara {
        int depth;
        int width;

        TaskPara() {
        }

        public String toString() {
            return "TaskPara: depth:" + this.depth + ", width:" + this.width;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FbThreadStat {
        boolean mHasChecked;
        List<Utils.ThreadOp> mOps;
        int mUid;
        IntArray mIsolatedPid = new IntArray();
        IntArray mPid = new IntArray();

        FbThreadStat(List<Utils.ThreadOp> ops) {
            this.mOps = ops;
        }
    }
}
