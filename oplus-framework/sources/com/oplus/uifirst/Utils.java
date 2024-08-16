package com.oplus.uifirst;

import android.os.PerformanceManager;
import android.os.Process;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.IntArray;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.util.Pair;
import com.oplus.network.OlkConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class Utils {
    static final boolean DEBUG;
    static final int[] PROCESS_COMM_FORMAT;
    static final String PROJECT_NUMBER = "ro.separate.soft";
    public static final String TAG = "OplusUIFirst";
    static final String THREAD_EVENT_ADD_GLTHREAD = "add_gl";
    static final String THREAD_EVENT_LAUNCH = "launch";
    static final String THREAD_EVENT_PAUSE_ACTIVITY = "pause";
    static final String THREAD_EVENT_RESUME_ACTIVITY = "resume";
    static final String THREAD_EVENT_START_ACTIVITY = "start";
    static final String THREAD_EVENT_START_PROCESS = "start_p";
    static final String THREAD_EVENT_TO_FORE = "to_fore";
    static final String THREAD_EVENT_TO_TOP = "to_top";
    static final String THREAD_MODE_ONESHOT = "oneshot";
    static final String THREAD_MODE_REPEAT = "repeat";
    private static int[] mLinuxVersion;
    private static Platform mPlatform;
    private static String mProjectNumber;

    static {
        boolean z = false;
        if (SystemProperties.getBoolean("persist.sys.assert.panic", false) && "0".equals(SystemProperties.get("persist.sys.agingtest", "0"))) {
            z = true;
        }
        DEBUG = z;
        PROCESS_COMM_FORMAT = new int[]{4096};
        mPlatform = Platform.NONE;
        mProjectNumber = null;
        mLinuxVersion = new int[2];
    }

    public static int[] getLinuxVersion() {
        if (mLinuxVersion[0] == 0) {
            List<String> lines = null;
            try {
                lines = Files.readAllLines(Paths.get("/proc/version", new String[0]), StandardCharsets.UTF_8);
            } catch (IOException e) {
            }
            if (lines != null && lines.size() > 0) {
                String versionStr = lines.get(0).trim();
                Pattern p = Pattern.compile("Linux version ([0-9]+)\\.([0-9]+)");
                Matcher m = p.matcher(versionStr);
                if (m.find()) {
                    mLinuxVersion[0] = Integer.parseInt(m.group(1));
                    mLinuxVersion[1] = Integer.parseInt(m.group(2));
                }
            }
        }
        return mLinuxVersion;
    }

    public static Platform getVendorPlatform() {
        if (mPlatform == Platform.NONE) {
            String prop = SystemProperties.get("ro.boot.hardware", "").toLowerCase();
            if (prop.startsWith("qcom")) {
                mPlatform = Platform.QCOM;
            } else if (prop.startsWith("mt")) {
                mPlatform = Platform.MTK;
            } else {
                mPlatform = Platform.UNKNOWN;
            }
        }
        return mPlatform;
    }

    public static String getProjectNumber() {
        if (mProjectNumber == null) {
            mProjectNumber = SystemProperties.get("ro.separate.soft", "");
        }
        return mProjectNumber;
    }

    public static List<ThreadOp> filterOutPrj(List<ThreadOp> ops) {
        final String localPrjNum = getProjectNumber();
        Iterator<ThreadOp> iterator = ops.iterator();
        while (iterator.hasNext()) {
            ThreadOp op = iterator.next();
            if (!TextUtils.isEmpty(op.mPrjNum)) {
                String[] projectNums = op.mPrjNum.split("\\|");
                boolean contains = Arrays.stream(projectNums).anyMatch(new Predicate() { // from class: com.oplus.uifirst.Utils$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean equals;
                        equals = ((String) obj).equals(localPrjNum);
                        return equals;
                    }
                });
                if (!contains) {
                    iterator.remove();
                }
            }
        }
        return ops;
    }

    public static boolean writeProcNode(String filePath, String val) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        try {
            FileOutputStream os = new FileOutputStream(file);
            try {
                byte[] data = val.getBytes(StandardCharsets.UTF_8);
                os.write(data);
                os.close();
                return true;
            } finally {
            }
        } catch (IOException e) {
            Log.e(TAG, "writeProcNode: " + e.getMessage());
            return false;
        }
    }

    public static String readProcNode(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String res = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                res = br.readLine();
                br.close();
            } finally {
            }
        } catch (IOException e) {
            Log.e(TAG, "readProcNode: " + e.getMessage());
        }
        return res;
    }

    public static void setUxThreadValue(int pid, int tid, String value) {
        PerformanceManager.writeUxState(value, String.valueOf(pid), String.valueOf(tid));
        if (DEBUG) {
            Log.d(TAG, "setUxThread: " + pid + "@" + tid + " to " + value);
        }
    }

    public static void setUxThreadValue(int pid, int tid, int value) {
        PerformanceManager.writeUxState(String.valueOf(value), String.valueOf(pid), String.valueOf(tid));
        if (DEBUG) {
            Log.d(TAG, "setUxThread: " + pid + "@" + tid + " to " + value);
        }
    }

    public static void setUxThreadValueByFile(int pid, int tid, int value) {
        OplusUIFirstManager.nativeSetThreadUx(pid, tid, value);
    }

    public static String readProcComm(int pid) {
        String[] comm = new String[1];
        if (!Process.readProcFile("/proc/" + pid + "/comm", PROCESS_COMM_FORMAT, comm, null, null)) {
            return null;
        }
        if (!TextUtils.isEmpty(comm[0]) && comm[0].endsWith("\n")) {
            return comm[0].substring(0, comm[0].length() - 1);
        }
        return comm[0];
    }

    public static String readProcCmdline(int pid) {
        String[] comm = new String[1];
        if (!Process.readProcFile("/proc/" + pid + "/cmdline", PROCESS_COMM_FORMAT, comm, null, null)) {
            return null;
        }
        if (!TextUtils.isEmpty(comm[0]) && comm[0].endsWith("\n")) {
            return comm[0].substring(0, comm[0].length() - 1);
        }
        return comm[0];
    }

    public static boolean isMatchedTid(int tid, String pattern) {
        try {
            String comm = readProcComm(tid);
            if (comm != null) {
                return Pattern.matches(pattern, comm);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static IntArray findMatchedPids(String pattern, int... uids) {
        IntArray array = new IntArray();
        try {
            Pattern r = Pattern.compile(pattern);
            int[] pids = Process.getPids("/proc", new int[1024]);
            for (int pid : pids) {
                if (pid == -1) {
                    break;
                }
                String comm = readProcComm(pid);
                if (comm != null) {
                    Matcher m = r.matcher(comm);
                    if (m.find()) {
                        if (uids.length <= 0) {
                            array.add(pid);
                        } else {
                            int uid = Process.getUidForPid(pid);
                            int length = uids.length;
                            int i = 0;
                            while (true) {
                                if (i < length) {
                                    int id = uids[i];
                                    if (id != uid) {
                                        i++;
                                    } else {
                                        array.add(pid);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        if (DEBUG) {
            Log.d(TAG, "findMatchedPids:  pattern " + pattern + " array " + Arrays.toString(array.toArray()));
        }
        return array;
    }

    public static IntArray findMatchedTids(int pid, String pattern) {
        IntArray array = new IntArray();
        try {
            Pattern r = Pattern.compile(pattern);
            int[] tids = Process.getPids("/proc/" + pid + "/task", new int[128]);
            for (int tid : tids) {
                if (tid == -1) {
                    break;
                }
                String comm = readProcComm(tid);
                if (comm != null) {
                    Matcher m = r.matcher(comm);
                    if (m.find()) {
                        array.add(tid);
                    }
                }
            }
        } catch (Exception e) {
        }
        if (DEBUG) {
            Log.d(TAG, "findMatchedTids: " + pid + " pattern " + pattern + " array " + Arrays.toString(array.toArray()));
        }
        return array;
    }

    public static IntArray getPidsByUid(int uid) {
        IntArray array = new IntArray();
        try {
            int[] pids = Process.getPids("/proc/", new int[1024]);
            for (int pid : pids) {
                if (pid == -1) {
                    break;
                }
                if (Process.getUidForPid(pid) == uid) {
                    array.add(pid);
                }
            }
        } catch (Exception e) {
        }
        if (DEBUG) {
            Log.d(TAG, "getPidByUid: " + uid + " array " + Arrays.toString(array.toArray()));
        }
        return array;
    }

    public static Pair<List<ThreadOp>, String> parseThreadOpRaw(String config) throws IOException {
        char c;
        ArrayList<ThreadOp> ops = new ArrayList<>();
        String info = null;
        JsonReader reader = new JsonReader(new StringReader(config));
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            String op = "";
            String pattern = "";
            String mode = THREAD_MODE_ONESHOT;
            String event = "";
            int delay = 1000;
            String state = "";
            String prjNum = "";
            while (reader.peek() == JsonToken.NAME) {
                String jsonName = reader.nextName();
                switch (jsonName.hashCode()) {
                    case -791090288:
                        if (jsonName.equals("pattern")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -309310695:
                        if (jsonName.equals("project")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 3553:
                        if (jsonName.equals("op")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 3357091:
                        if (jsonName.equals("mode")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 95467907:
                        if (jsonName.equals(OlkConstants.DELAY)) {
                            c = 2;
                            break;
                        }
                        break;
                    case 96891546:
                        if (jsonName.equals("event")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 109757585:
                        if (jsonName.equals("state")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                c = 65535;
                switch (c) {
                    case 0:
                        op = reader.nextString();
                        break;
                    case 1:
                        pattern = reader.nextString();
                        break;
                    case 2:
                        delay = reader.nextInt();
                        break;
                    case 3:
                        mode = reader.nextString();
                        break;
                    case 4:
                        event = reader.nextString();
                        break;
                    case 5:
                        state = reader.nextString();
                        break;
                    case 6:
                        prjNum = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        info = "unknown name: " + jsonName;
                        break;
                }
            }
            ThreadOp threadOp = new ThreadOp();
            threadOp.mOp = op;
            threadOp.mPattern = pattern;
            threadOp.mMode = mode;
            threadOp.mDelay = delay;
            threadOp.mEvent = event;
            threadOp.mState = state;
            threadOp.mPrjNum = prjNum;
            ops.add(threadOp);
            reader.endObject();
        }
        reader.endArray();
        return new Pair<>(ops, info);
    }

    public static List<ThreadOp> parseThreadOp(String config) {
        try {
            Pair<List<ThreadOp>, String> p = parseThreadOpRaw(config);
            return (List) p.first;
        } catch (Exception e) {
            Log.e(TAG, "ThreadOp json config error:" + config, e);
            return null;
        }
    }

    public static void printThreadOps(String tag, List<ThreadOp> ops) {
        if (ops == null) {
            Log.d(tag, "List of ThreadOp is null");
        } else {
            Log.d(tag, "List of ThreadOp: size=" + ops.size() + " " + Arrays.toString(ops.toArray()));
        }
    }

    public static void printThreadOp(String tag, ThreadOp op) {
        if (op == null) {
            Log.d(tag, "ThreadOp is null");
        } else {
            Log.d(tag, op.toString());
        }
    }

    /* loaded from: classes.dex */
    public static class ThreadOp {
        int mDelay;
        String mEvent;
        String mMode;
        String mOp;
        String mPattern;
        String mPrjNum;
        String mState;

        public String toString() {
            return "ThreadOp:" + this.mOp + ", pattern:" + this.mPattern + ", mode:" + this.mMode + ", event:" + this.mEvent + ", delay:" + this.mDelay + ", state:" + this.mState + ", prj_num:" + this.mPrjNum;
        }
    }
}
