package android.os;

import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class OplusBinderRecorder {
    static final int FLAG_ONEWAY = 1;
    static final int STATE_FINISH = 3;
    static final int STATE_FOUND_CONTEXT = 2;
    static final int STATE_FOUND_PROC = 1;
    static final int STATE_NOT_FOUND = 0;
    public static final String TAG = "OplusBinderRecorder";
    private static OplusBinderRecorder mInstance = null;
    private long mMaxTimeUsed = 0;
    private String mMaxTimeUsedDescriptor = null;

    /* loaded from: classes.dex */
    public final class ThreadUsage {
        final ArrayList<ThreadUsageElement> mUsageList = new ArrayList<>();

        public ThreadUsage() {
        }

        /* loaded from: classes.dex */
        public final class ThreadUsageElement {
            private int mCount = 0;
            private String mName = OplusPropertyList.UNKNOWN;
            private int mToPid;

            public ThreadUsageElement(int toPid) {
                this.mToPid = 0;
                this.mToPid = toPid;
                initName(toPid);
            }

            private void initName(int pid) {
                String cmdline = "/proc/" + pid + "/cmdline";
                FileInputStream fis = null;
                try {
                    try {
                        try {
                            fis = new FileInputStream(cmdline);
                            byte[] buffer = new byte[2048];
                            int count = fis.read(buffer);
                            if (count > 0) {
                                int i = 0;
                                while (i < count && buffer[i] != 0) {
                                    i++;
                                }
                                this.mName = new String(buffer, 0, i);
                            }
                            fis.close();
                        } catch (IOException e) {
                            Log.w(OplusBinderRecorder.TAG, "Failed to read " + cmdline);
                            Log.w(OplusBinderRecorder.TAG, e);
                            if (fis != null) {
                                fis.close();
                            }
                        }
                    } catch (Throwable th) {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (Exception e2) {
                            }
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                }
            }

            public final String getName() {
                return this.mName;
            }

            public void increase() {
                this.mCount++;
            }

            public int getUsage() {
                return this.mCount;
            }

            public int getToPid() {
                return this.mToPid;
            }
        }

        public void record(int pid) {
            ThreadUsageElement tu = null;
            for (int i = 0; i < this.mUsageList.size(); i++) {
                ThreadUsageElement tu2 = this.mUsageList.get(i);
                tu = tu2;
                if (tu.getToPid() == pid) {
                    break;
                }
                tu = null;
            }
            if (tu == null) {
                tu = new ThreadUsageElement(pid);
                this.mUsageList.add(tu);
            }
            tu.increase();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class UsageListComparator implements Comparator<ThreadUsageElement> {
            UsageListComparator() {
            }

            @Override // java.util.Comparator
            public int compare(ThreadUsageElement a, ThreadUsageElement b) {
                if (a.getUsage() < b.getUsage()) {
                    return 1;
                }
                if (a.getUsage() > b.getUsage()) {
                    return -1;
                }
                return 0;
            }
        }

        public int getLength() {
            return this.mUsageList.size();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sort() {
            Collections.sort(this.mUsageList, new UsageListComparator());
        }

        public void print() {
            for (int i = 0; i < this.mUsageList.size(); i++) {
                ThreadUsageElement tu = this.mUsageList.get(i);
                Log.i(OplusBinderRecorder.TAG, tu.getName() + "(" + tu.getToPid() + "):" + tu.getUsage());
            }
        }

        public final String getMapString() {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < this.mUsageList.size(); i++) {
                ThreadUsageElement tu = this.mUsageList.get(i);
                sb.append(tu.getName());
                sb.append(":");
                sb.append(Integer.toString(tu.getUsage()));
                sb.append(",");
            }
            return sb.toString();
        }
    }

    private OplusBinderRecorder() {
    }

    public static synchronized OplusBinderRecorder getInstance() {
        OplusBinderRecorder oplusBinderRecorder;
        synchronized (OplusBinderRecorder.class) {
            if (mInstance == null) {
                mInstance = new OplusBinderRecorder();
            }
            oplusBinderRecorder = mInstance;
        }
        return oplusBinderRecorder;
    }

    public synchronized void recordTimeUsed(Binder binder, long time) {
        if (this.mMaxTimeUsed < time) {
            this.mMaxTimeUsed = time;
            this.mMaxTimeUsedDescriptor = binder.getInterfaceDescriptor();
        }
    }

    public synchronized void uploadMaxTimeUsed() {
        Log.i(TAG, "max time used: " + this.mMaxTimeUsed + " desc: " + this.mMaxTimeUsedDescriptor);
    }

    public Map<String, String> getBinderUsageDscLogMap() {
        String str;
        String str2 = "DCS mapstring is: ";
        Map<String, String> logMap = null;
        try {
            int pid = Process.myPid();
            int state = 0;
            BufferedReader in = new BufferedReader(new FileReader("/sys/kernel/debug/binder/state"));
            ThreadUsage inComingThreadUsage = new ThreadUsage();
            ThreadUsage outGoingThreadUsage = new ThreadUsage();
            Log.i(TAG, "Uploading binder usage for process " + pid);
            while (true) {
                String line = in.readLine();
                if (line != null && state != 3) {
                    switch (state) {
                        case 0:
                            str = str2;
                            if (!line.equals("proc " + pid)) {
                                break;
                            } else {
                                state = 1;
                                break;
                            }
                        case 1:
                            str = str2;
                            if (line.equals("context binder")) {
                                state = 2;
                                break;
                            } else {
                                state = 0;
                                break;
                            }
                        case 2:
                            if (line.startsWith("    outgoing")) {
                                Log.i(TAG, "found transaction: " + line);
                                Pattern p = Pattern.compile("^    (outgoing|incoming).*from (\\d+):(\\d+) to (\\d+):(\\d+).*flags (\\d+).*");
                                Matcher m = p.matcher(line);
                                if (!m.find()) {
                                    str = str2;
                                } else {
                                    String direction = m.group(1);
                                    int from = Integer.parseInt(m.group(2));
                                    int to = Integer.parseInt(m.group(4));
                                    str = str2;
                                    long flags = Integer.parseInt(m.group(6));
                                    if ((flags & 1) == 0) {
                                        if (direction.equals("outgoing")) {
                                            outGoingThreadUsage.record(to);
                                        } else if (direction.equals("incoming")) {
                                            inComingThreadUsage.record(from);
                                        }
                                    }
                                }
                            } else {
                                str = str2;
                                if (line.startsWith("proc")) {
                                    state = 3;
                                    break;
                                }
                            }
                            break;
                        default:
                            str = str2;
                            break;
                    }
                    str2 = str;
                }
            }
            String str3 = str2;
            in.close();
            logMap = new HashMap<>();
            outGoingThreadUsage.sort();
            Log.i(TAG, "Print outgoing thread usage for " + Process.myPid());
            outGoingThreadUsage.print();
            String outGoingString = outGoingThreadUsage.getMapString();
            Log.i(TAG, str3 + outGoingString);
            logMap.put("outGoingThreadUsage", outGoingString);
            inComingThreadUsage.sort();
            Log.i(TAG, "Print incoming thread usage for " + Process.myPid());
            inComingThreadUsage.print();
            String inComingString = inComingThreadUsage.getMapString();
            Log.i(TAG, str3 + inComingString);
            logMap.put("inComingThreadUsage", inComingString);
            return logMap;
        } catch (IOException e) {
            Log.w(TAG, "Failed to read binder state");
            return logMap;
        }
    }
}
