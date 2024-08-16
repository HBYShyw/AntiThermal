package com.oplus.uifirst;

import android.os.Handler;
import android.os.Message;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IntArray;
import android.util.Log;
import android.util.Pair;
import com.oplus.filter.DynamicFilterManager;
import com.oplus.uifirst.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class GlThreadManager {
    private static final List<Utils.ThreadOp> EMPTY_GL_THREAD_OP = new ArrayList(0);
    private static GlThreadManager mInstance;
    private final Handler mHandler;
    private final OplusUIFirstManager mUifManager;
    private final ArrayMap<String, GlThreadStat> mAppUxMap = new ArrayMap<>();
    private final ArrayMap<String, ArraySet<Integer>> mAppPidsMap = new ArrayMap<>();
    private final ArrayMap<Integer, ArraySet<Integer>> mPidTidsMap = new ArrayMap<>();

    public static void initialize(OplusUIFirstManager uifManager, Handler handler) {
        mInstance = new GlThreadManager(uifManager, handler);
    }

    public static GlThreadManager getInstance() {
        return mInstance;
    }

    private GlThreadManager(OplusUIFirstManager uifManager, Handler handler) {
        this.mUifManager = uifManager;
        this.mHandler = handler;
    }

    synchronized GlThreadStat getGlThreadStat(String packageName) {
        return this.mAppUxMap.get(packageName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addGlThread(String packageName, int pid, int tid) {
        addGlThread(packageName, pid, tid, true);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x0030. Please report as an issue. */
    synchronized void addGlThread(String packageName, int pid, int tid, boolean needFilter) {
        List<Utils.ThreadOp> addGlOp;
        char c;
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null && stat.mOps != null) {
            if (needFilter && (addGlOp = getGlThreadOps(packageName, "add_gl")) != null) {
                for (Utils.ThreadOp op : addGlOp) {
                    String str = op.mOp;
                    switch (str.hashCode()) {
                        case -1039877803:
                            if (str.equals("non_ux")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            if (!Utils.isMatchedTid(tid, op.mPattern)) {
                                break;
                            } else {
                                return;
                            }
                    }
                }
            }
            addPid(packageName, pid);
            addTid2Pid(pid, tid);
            if (stat.mPidUx.containsKey(Integer.valueOf(pid))) {
                Utils.setUxThreadValueByFile(pid, tid, stat.mPidUx.get(Integer.valueOf(pid)).intValue());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeGlThread(String packageName, int pid, int tid) {
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null && stat.mOps != null) {
            removeTid4Pid(pid, tid);
            if (stat.mPidUx.containsKey(Integer.valueOf(pid))) {
                Utils.setUxThreadValueByFile(pid, tid, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeAppPid(String packageName, int pid) {
        if (this.mAppUxMap.get(packageName) == null) {
            return;
        }
        removePid(packageName, pid);
        this.mPidTidsMap.remove(Integer.valueOf(pid));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addHwuiTaskThread(String packageName, int pid, int tid) {
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            return;
        }
        if (stat.mPidUx.containsKey(Integer.valueOf(pid))) {
            int ux = stat.mPidUx.get(Integer.valueOf(pid)).intValue();
            Utils.setUxThreadValueByFile(pid, tid, (ux & (-3)) | 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setRenderThreadTid(String packageName, int pid, int tid) {
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null && stat.mOps != null) {
            removeTid4Pid(pid, tid);
        }
    }

    synchronized boolean appExists(String packageName) {
        return this.mAppUxMap.containsKey(packageName);
    }

    synchronized int getGlThreadValue(String packageName, int pid) {
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            return 0;
        }
        return stat.mPidUx.containsKey(Integer.valueOf(pid)) ? stat.mPidUx.get(Integer.valueOf(pid)).intValue() : 0;
    }

    synchronized List<Utils.ThreadOp> getGlThreadOps(String packageName, String event) {
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null && stat.mOps != null) {
            List<Utils.ThreadOp> list = stat.mOps;
            List<Utils.ThreadOp> list2 = EMPTY_GL_THREAD_OP;
            if (list == list2) {
                return list2;
            }
            ArrayList<Utils.ThreadOp> list3 = new ArrayList<>();
            for (Utils.ThreadOp op : stat.mOps) {
                if (op.mEvent.startsWith(event)) {
                    list3.add(op);
                }
            }
            return list3;
        }
        return null;
    }

    synchronized void setGlThreadOps(String packageName, List<Utils.ThreadOp> ops) {
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            this.mAppUxMap.put(packageName, new GlThreadStat(ops));
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
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null) {
            int index = stat.mPid.indexOf(pid);
            if (index != -1) {
                stat.mPid.remove(index);
            }
            int index2 = stat.mIsolatedPid.indexOf(pid);
            if (index2 != -1) {
                stat.mIsolatedPid.remove(index2);
            }
            int index3 = stat.mPidUx.indexOfKey(Integer.valueOf(pid));
            if (index3 >= 0) {
                stat.mPidUx.removeAt(index3);
            }
        }
        ArraySet<Integer> pids = this.mAppPidsMap.get(packageName);
        if (pids != null) {
            pids.remove(Integer.valueOf(pid));
            if (pids.isEmpty()) {
                this.mAppPidsMap.remove(packageName);
                this.mAppUxMap.remove(packageName);
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

    public synchronized void updateUxForPkg(String packageName, int pid, int ux) {
        ArraySet<Integer> tids;
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat == null) {
            return;
        }
        int mainPid = -1;
        int uxStateOfMainPid = -1;
        if (Utils.DEBUG) {
            Log.d(Utils.TAG, "updateUxForPkg pkg:" + packageName + ", pid:" + pid + ", ux:" + ux);
        }
        if (ux == 0) {
            stat.mPidUx.put(Integer.valueOf(pid), 0);
            int size = stat.mPidUx.size();
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    int key = stat.mPidUx.keyAt(i).intValue();
                    int value = stat.mPidUx.valueAt(i).intValue();
                    if (stat.mIsolatedPid.indexOf(key) == -1 && value > uxStateOfMainPid) {
                        mainPid = key;
                        uxStateOfMainPid = value;
                    }
                }
                ux = uxStateOfMainPid;
                if (Utils.DEBUG) {
                    Log.d(Utils.TAG, "updateUxForPkg mainPid:" + mainPid + ", ux:" + uxStateOfMainPid);
                }
            }
        } else {
            stat.mPidUx.put(Integer.valueOf(pid), Integer.valueOf(ux));
        }
        if (mainPid != pid && uxStateOfMainPid != 0) {
            ArraySet<Integer> pids = this.mAppPidsMap.get(packageName);
            if (pids != null) {
                ArraySet<Integer> tids2 = this.mPidTidsMap.get(Integer.valueOf(pid));
                if (tids2 != null) {
                    Iterator<Integer> it = tids2.iterator();
                    while (it.hasNext()) {
                        Integer tid = it.next();
                        Utils.setUxThreadValueByFile(pid, tid.intValue(), ux);
                    }
                }
                if (Utils.DEBUG) {
                    Log.d(Utils.TAG, "updateUxForPkg auxiliary pid follows ux:" + ux);
                }
            }
            if (ux == 0) {
                stat.mPidUx.remove(Integer.valueOf(pid));
            }
        }
        Iterator<Map.Entry<Integer, Integer>> iterator = stat.mPidUx.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            int auxiliaryPid = entry.getKey().intValue();
            int value2 = entry.getValue().intValue();
            if (auxiliaryPid == mainPid || value2 == 0) {
                ArraySet<Integer> pids2 = this.mAppPidsMap.get(packageName);
                if (pids2 != null && (tids = this.mPidTidsMap.get(Integer.valueOf(auxiliaryPid))) != null) {
                    Iterator<Integer> it2 = tids.iterator();
                    while (it2.hasNext()) {
                        Integer tid2 = it2.next();
                        Utils.setUxThreadValueByFile(auxiliaryPid, tid2.intValue(), ux);
                    }
                }
                if (ux == 0) {
                    iterator.remove();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveToBack(String packageName, int appPid) {
        if (Utils.DEBUG) {
            Log.d(Utils.TAG, "moveToBack " + packageName);
        }
        updateUxForPkg(packageName, appPid, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveToFore(String packageName, int appPid) {
        char c;
        List<Utils.ThreadOp> to_fore_ops = getGlThreadOps(packageName, "to_fore");
        if (Utils.DEBUG) {
            Log.d(Utils.TAG, "to_fore_ops " + to_fore_ops);
        }
        if (to_fore_ops != null) {
            for (Utils.ThreadOp op : to_fore_ops) {
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
                            msg.what = 5;
                            msg.obj = new Pair(packageName, op);
                            this.mHandler.sendMessageDelayed(msg, op.mDelay);
                            if (Utils.DEBUG) {
                                Log.d(Utils.TAG, "repeat " + op);
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            GlThreadStat stat = getGlThreadStat(packageName);
                            if (stat != null && !stat.mHasChecked) {
                                stat.mHasChecked = true;
                                Message msg2 = Message.obtain();
                                msg2.what = 5;
                                msg2.obj = new Pair(packageName, op);
                                this.mHandler.sendMessageDelayed(msg2, op.mDelay);
                                if (Utils.DEBUG) {
                                    Log.d(Utils.TAG, "oneshot " + op);
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
        List<Utils.ThreadOp> ops = getGlThreadOps(packageName, event);
        if (Utils.DEBUG) {
            Log.d(Utils.TAG, "glThreadStartActivity " + ops);
        }
        if (ops != null && ops != EMPTY_GL_THREAD_OP) {
            String activityTag = event + " ";
            for (Utils.ThreadOp op : ops) {
                if (op.mEvent != null && op.mEvent.startsWith(activityTag) && op.mEvent.regionMatches(activityTag.length(), activityName, 0, op.mEvent.length() - activityTag.length())) {
                    Message msg = Message.obtain();
                    msg.what = 5;
                    msg.obj = new Pair(packageName, op);
                    this.mHandler.sendMessageDelayed(msg, op.mDelay);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleGlThreadOp(String packageName, Utils.ThreadOp op) {
        IntArray pidCloned;
        IntArray iolatedPidCloned;
        GlThreadStat stat = getGlThreadStat(packageName);
        if (stat == null || stat.mOps == null) {
            return;
        }
        synchronized (this) {
            pidCloned = stat.mPid.clone();
            iolatedPidCloned = stat.mIsolatedPid.clone();
        }
        for (int i = 0; i < pidCloned.size(); i++) {
            handleGlThreadOp(packageName, pidCloned.get(i), op);
        }
        for (int i2 = 0; i2 < iolatedPidCloned.size(); i2++) {
            handleGlThreadOp(packageName, iolatedPidCloned.get(i2), op);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleGlThreadOp(String packageName, int pid, Utils.ThreadOp op) {
        char c;
        if (!appExists(packageName)) {
            return;
        }
        IntArray tids = Utils.findMatchedTids(pid, op.mPattern);
        if (tids.size() <= 0) {
            return;
        }
        if (Utils.DEBUG) {
            Log.d(Utils.TAG, "handleGlThreadOp find " + Arrays.toString(tids.toArray()));
        }
        String[] subOps = op.mOp.split("\\+");
        for (String subOp : subOps) {
            switch (subOp.hashCode()) {
                case 3747:
                    if (subOp.equals("ux")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            c = 65535;
            switch (c) {
                case 0:
                    for (int i = 0; i < tids.size(); i++) {
                        int tid = tids.get(i);
                        addGlThread(packageName, pid, tid, false);
                    }
                    break;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006a A[Catch: all -> 0x00f5, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0007, B:7:0x0011, B:11:0x0017, B:13:0x0023, B:15:0x003d, B:16:0x0043, B:18:0x0049, B:20:0x004f, B:22:0x0056, B:25:0x005d, B:26:0x0066, B:28:0x006a, B:29:0x0061, B:30:0x0082, B:33:0x008e, B:34:0x009b, B:35:0x0096, B:36:0x009e, B:38:0x00a7, B:41:0x00ac, B:42:0x00b0, B:44:0x00b6, B:47:0x00d8), top: B:2:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized void handleProcessStart(String packageName, int uid, int pid, boolean isolated, String processName) {
        if (!appExists(packageName)) {
            boolean ingl = this.mUifManager.inFilter(DynamicFilterManager.FILTER_GL_THREAD_UX, packageName);
            if (!ingl) {
                setGlThreadOps(packageName, null);
                return;
            }
            String config = this.mUifManager.getFilterConfig(DynamicFilterManager.FILTER_GL_THREAD_UX, packageName);
            if (Utils.DEBUG) {
                Log.d(Utils.TAG, "config " + config);
            }
            if (config == null) {
                setGlThreadOps(packageName, EMPTY_GL_THREAD_OP);
            } else {
                List<Utils.ThreadOp> ops = Utils.parseThreadOp(config);
                if (ops != null && ops.size() > 0) {
                    ops = Utils.filterOutPrj(ops);
                }
                if (ops != null && !ops.isEmpty()) {
                    setGlThreadOps(packageName, ops);
                    if (Utils.DEBUG) {
                        Log.d(Utils.TAG, "parseGlThreadOp " + ops);
                    }
                }
                setGlThreadOps(packageName, EMPTY_GL_THREAD_OP);
                if (Utils.DEBUG) {
                }
            }
        }
        GlThreadStat stat = this.mAppUxMap.get(packageName);
        if (stat != null) {
            if (!isolated) {
                stat.mUid = uid;
                stat.mPid.add(pid);
            } else {
                stat.mIsolatedPid.add(pid);
            }
            stat.mHasChecked = false;
        }
        List<Utils.ThreadOp> ops2 = getGlThreadOps(packageName, "start_p");
        if (ops2 != null && ops2 != EMPTY_GL_THREAD_OP) {
            for (Utils.ThreadOp op : ops2) {
                Message msg = Message.obtain();
                msg.what = 4;
                msg.arg1 = pid;
                msg.obj = new Pair(packageName, op);
                this.mHandler.sendMessageDelayed(msg, op.mDelay);
                if (Utils.DEBUG) {
                    Log.d(Utils.TAG, "handleProcessStart " + op);
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x023b, code lost:
    
        if (r8.equals("oneshot") != false) goto L116;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:85:0x01ab. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:89:0x01d0. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:65:0x011d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String verifyConfig(ArrayList<String> tags, ArrayList<String> values) {
        boolean z;
        char c;
        String str = "\n";
        if (tags.size() != values.size()) {
            return "config tags and values size don't match";
        }
        Pattern pkgPn = Pattern.compile("^[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*$");
        Iterator<String> it = tags.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (!pkgPn.matcher(s).find()) {
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
            if (s2 != null) {
                if (!s2.startsWith("[{")) {
                    return s2 + "\ndoesn't start with \"[{\"";
                }
                if (!s2.endsWith("}]")) {
                    return s2 + "\ndoesn't end with \"}]\"";
                }
            }
        }
        Pattern prjNumPn = Pattern.compile("^[0-9A-Z]+$");
        Iterator<String> it3 = values.iterator();
        while (it3.hasNext()) {
            String s3 = it3.next();
            if (s3 != null) {
                try {
                    Pair<List<Utils.ThreadOp>, String> p = Utils.parseThreadOpRaw(s3);
                    if (p.second != null) {
                        str = s3 + "\n" + ((String) p.second);
                        return str;
                    }
                    List<Utils.ThreadOp> ops = (List) p.first;
                    for (Utils.ThreadOp op : ops) {
                        boolean z2 = false;
                        if (!TextUtils.isEmpty(op.mPrjNum)) {
                            String[] projectNums = op.mPrjNum.split("\\|");
                            for (String prjNum : projectNums) {
                                if (!prjNumPn.matcher(prjNum).find()) {
                                    return s3 + "\ncontains illegal project number: " + prjNum;
                                }
                            }
                        }
                        String str2 = op.mOp;
                        switch (str2.hashCode()) {
                            case -1039877803:
                                if (str2.equals("non_ux")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 3747:
                                if (str2.equals("ux")) {
                                    z = false;
                                    break;
                                }
                                break;
                        }
                        z = -1;
                        switch (z) {
                            case false:
                            case true:
                                try {
                                    Pattern.compile(op.mPattern);
                                    String str3 = op.mEvent;
                                    switch (str3.hashCode()) {
                                        case -1897185325:
                                            if (str3.equals("start_p")) {
                                                c = 1;
                                                break;
                                            }
                                            c = 65535;
                                            break;
                                        case -1422513853:
                                            if (str3.equals("add_gl")) {
                                                c = 0;
                                                break;
                                            }
                                            c = 65535;
                                            break;
                                        case -1155036160:
                                            if (str3.equals("to_fore")) {
                                                c = 2;
                                                break;
                                            }
                                            c = 65535;
                                            break;
                                        default:
                                            c = 65535;
                                            break;
                                    }
                                    switch (c) {
                                        default:
                                            if (!op.mEvent.startsWith(Telephony.BaseMmsColumns.START) && !op.mEvent.startsWith("resume")) {
                                                return s3 + "\ncontains unknown event: " + op.mEvent;
                                            }
                                            if (op.mEvent.endsWith(" ")) {
                                                return s3 + "\nends with space";
                                            }
                                            break;
                                        case 0:
                                        case 1:
                                        case 2:
                                            String str4 = op.mMode;
                                            switch (str4.hashCode()) {
                                                case -1320294816:
                                                    break;
                                                case -934531685:
                                                    if (str4.equals("repeat")) {
                                                        z2 = true;
                                                        break;
                                                    }
                                                    break;
                                            }
                                            z2 = -1;
                                            switch (z2) {
                                                case false:
                                                case true:
                                                    if (op.mDelay > 10000) {
                                                        return s3 + "\ncontains too long delay: " + op.mDelay;
                                                    }
                                                default:
                                                    return s3 + "\ncontains unknown mode: " + op.mMode;
                                            }
                                    }
                                } catch (Exception e) {
                                    return s3 + "\ncontains illegal pattern: " + op.mPattern;
                                }
                                break;
                            default:
                                return s3 + "\ncontains unknown op: " + op.mOp;
                        }
                        while (r6.hasNext()) {
                        }
                    }
                } catch (IOException ioe) {
                    return s3 + str + ioe.getMessage();
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class GlThreadStat {
        boolean mHasChecked;
        List<Utils.ThreadOp> mOps;
        int mUid;
        IntArray mIsolatedPid = new IntArray();
        IntArray mPid = new IntArray();
        ArrayMap<Integer, Integer> mPidUx = new ArrayMap<>();

        GlThreadStat(List<Utils.ThreadOp> ops) {
            this.mOps = ops;
        }
    }
}
