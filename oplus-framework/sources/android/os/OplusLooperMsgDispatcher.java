package android.os;

import android.util.Log;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class OplusLooperMsgDispatcher {
    private static final int BACKWARD_MSG_CUPLOAD = 80;
    private static final int BACKWARD_MSG_LEVEL = 3;
    public static final int BACKWARD_MSG_MERGER_THRESHOLD = 25;
    private static final int BACKWARD_MSG_THRESHOLD_ONE = 300;
    private static final int BACKWARD_MSG_THRESHOLD_TWO = 600;
    private static final long CHECK_DUMP_STATUS_GAP = 10000;
    private static final long CHECK_DUMP_STOP_GAP = 30000;
    private static final long CHECK_LOGGING_STATUS_GAP = 60000;
    private static final long CHECK_TOP_APP_TIME_GAP = 60000;
    private static final String DELIMITER = "_";
    private static final int LOOPER_LEVEL_ONE = 1;
    private static final int LOOPER_LEVEL_THREE = 3;
    private static final int LOOPER_LEVEL_TWO = 2;
    private static final int MSG_ANR_DUMP = 10244;
    private static final int MSG_LOOP_DONE_PENDING = 10243;
    public static final String MSG_TYPE_DISPATCH_HMAIN_SLOW = "msg_h_handler_slow";
    public static final String MSG_TYPE_DISPATCH_SLOW = "msg_dispatch_slow";
    private static final String PROP_STOCK_TYPE = "ro.oplus.image.my_stock.type";
    private static final long REAL_ANR_DUMP_INTERVAL = 1000;
    private static final long WAIT_MSG_FINISH_TIME_ONE = 5000;
    private static final long WAIT_MSG_FINISH_TIME_TWO = 7000;
    private boolean mIsDomesticBuild;
    private String mStockType;
    public static final String TAG = OplusLooperMsgDispatcher.class.getSimpleName();
    private static volatile OplusLooperMsgDispatcher sInstance = null;
    private static Object sLock = new Object();
    private long mWaitMsgFinishTime = WAIT_MSG_FINISH_TIME_ONE;
    private int mBackwardMsgThreshold = 600;
    private boolean mIsBrandSupported = false;
    private Handler mHistoryLooperHandler = null;
    private DumpQueue mDumpQueue = new DumpQueue();
    private boolean mIsTopApp = false;
    private long mLastCheckTopTime = 0;
    private boolean mIsWhiteProcess = false;
    private boolean mIsLogkitLogging = false;
    private long mLastCheckLoggingTime = 0;
    private long mStartTime = 0;
    private long mLastDumpTime = 0;

    private void initWhiteProcess() {
        this.mIsWhiteProcess = false;
    }

    private OplusLooperMsgDispatcher() {
        initWhiteProcess();
        initBuildTypes();
    }

    public static OplusLooperMsgDispatcher getInstance() {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = new OplusLooperMsgDispatcher();
                }
            }
        }
        return sInstance;
    }

    private void initLooper() {
        if (this.mHistoryLooperHandler != null) {
            return;
        }
        this.mHistoryLooperHandler = new Handler(OplusAppBackgroundThread.get().getLooper()) { // from class: android.os.OplusLooperMsgDispatcher.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                DumpQueue tmpQueue;
                DumpQueue anrQueue;
                switch (msg.what) {
                    case OplusLooperMsgDispatcher.MSG_LOOP_DONE_PENDING /* 10243 */:
                        synchronized (OplusLooperMsgDispatcher.sLock) {
                            tmpQueue = OplusLooperMsgDispatcher.this.mDumpQueue;
                            OplusLooperMsgDispatcher oplusLooperMsgDispatcher = OplusLooperMsgDispatcher.this;
                            oplusLooperMsgDispatcher.mDumpQueue = new DumpQueue();
                        }
                        tmpQueue.doDumpMerge();
                        OplusAppBackgroundThread.get().getLooper().mLooperExt.updatOplusMessage(SystemClock.uptimeMillis());
                        return;
                    case OplusLooperMsgDispatcher.MSG_ANR_DUMP /* 10244 */:
                        synchronized (OplusLooperMsgDispatcher.sLock) {
                            anrQueue = OplusLooperMsgDispatcher.this.mDumpQueue;
                            OplusLooperMsgDispatcher oplusLooperMsgDispatcher2 = OplusLooperMsgDispatcher.this;
                            oplusLooperMsgDispatcher2.mDumpQueue = new DumpQueue();
                        }
                        anrQueue.doDumpMergeWhenRealAnr();
                        OplusAppBackgroundThread.get().getLooper().mLooperExt.updatOplusMessage(SystemClock.uptimeMillis());
                        return;
                    default:
                        Log.e(OplusLooperMsgDispatcher.TAG, "INVALID COMMAND MSG: " + msg.what);
                        return;
                }
            }
        };
    }

    private void releaseLooper() {
        Handler handler = this.mHistoryLooperHandler;
        if (handler == null) {
            return;
        }
        handler.removeCallbacksAndMessages(null);
        this.mHistoryLooperHandler = null;
        DumpQueue dumpQueue = this.mDumpQueue;
        if (dumpQueue != null) {
            dumpQueue.clear();
        }
    }

    private boolean isEnable(int level, boolean bForegroudApp) {
        boolean isEnable;
        if (level == 3 && isTopApp(bForegroudApp)) {
            isEnable = true;
        } else {
            isEnable = isTopApp(bForegroudApp) && isLogging();
        }
        return isEnable && this.mIsBrandSupported;
    }

    private boolean isTopApp(boolean bForegroudApp) {
        if (this.mIsWhiteProcess) {
            return true;
        }
        if (this.mIsTopApp && System.currentTimeMillis() - this.mLastCheckTopTime <= 60000) {
            return true;
        }
        this.mLastCheckTopTime = System.currentTimeMillis();
        this.mIsTopApp = bForegroudApp;
        return bForegroudApp;
    }

    private boolean isLogging() {
        if (System.currentTimeMillis() - this.mLastCheckLoggingTime <= 60000) {
            return this.mIsLogkitLogging;
        }
        if ("junk".equals(SystemProperties.get("persist.sys.debuglog.config"))) {
            this.mIsLogkitLogging = false;
        } else {
            this.mIsLogkitLogging = "true".equals(SystemProperties.get("persist.sys.assert.panic"));
        }
        this.mLastCheckLoggingTime = System.currentTimeMillis();
        return this.mIsLogkitLogging;
    }

    private void initBuildTypes() {
        String str = SystemProperties.get(PROP_STOCK_TYPE);
        this.mStockType = str;
        if (isValidStockType(str)) {
            String[] typeArr = this.mStockType.split(DELIMITER);
            boolean z = true;
            String brand = typeArr[1];
            if (!"OPPO".equalsIgnoreCase(brand) && !"OnePlus".equalsIgnoreCase(brand) && !"OPPO-Light".equalsIgnoreCase(brand) && !"OnePlus-Light".equalsIgnoreCase(brand)) {
                z = false;
            }
            this.mIsBrandSupported = z;
        }
    }

    private boolean isValidStockType(String type) {
        return type != null && type.matches("^[A-Za-z]+_[A-Za-z]+$|^[A-Za-z]+_[A-Za-z]+-[A-Za-z]+$");
    }

    public void startRegisterCurrentMsg(OplusLooperEntry msg, boolean bForegroudApp, int level, int cpuload) {
        synchronized (sLock) {
            if (isEnable(level, bForegroudApp)) {
                setValueForLooperLevel(level);
                initLooper();
                if (this.mHistoryLooperHandler == null) {
                    return;
                }
                this.mDumpQueue.mCurrentMsg = null;
                if (msg.callback == null && msg.target == null) {
                    return;
                }
                if (filterMsg(msg)) {
                    return;
                }
                this.mDumpQueue.startRegisterCurrentMsg(msg);
                this.mStartTime = SystemClock.uptimeMillis();
                return;
            }
            releaseLooper();
        }
    }

    private boolean filterMsg(OplusLooperEntry oplusMsg) {
        if (oplusMsg.callback != null) {
            return oplusMsg.callback.contains("android.graphics.HardwareRendererObserver") || oplusMsg.callback.contains("android.view.Choreographer");
        }
        return false;
    }

    public void processRegisterFinishMsg(OplusLooperEntry msg, boolean bForegroudApp, int level, int cpuload) {
        synchronized (sLock) {
            if (this.mHistoryLooperHandler == null) {
                return;
            }
            if (msg.callback == null && msg.target == null) {
                return;
            }
            if (this.mDumpQueue.mCurrentMsg == null) {
                return;
            }
            long currentTime = SystemClock.uptimeMillis();
            this.mDumpQueue.processRegisterFinishMsg();
            if (currentTime - this.mStartTime > this.mWaitMsgFinishTime && cpuload < 80) {
                Message delayMsg = Message.obtain();
                delayMsg.what = MSG_LOOP_DONE_PENDING;
                this.mHistoryLooperHandler.sendMessage(delayMsg);
            }
        }
    }

    private void setValueForLooperLevel(int oplusLooperLevel) {
        switch (oplusLooperLevel) {
            case 1:
                this.mWaitMsgFinishTime = WAIT_MSG_FINISH_TIME_TWO;
                this.mBackwardMsgThreshold = 300;
                return;
            case 2:
                this.mWaitMsgFinishTime = WAIT_MSG_FINISH_TIME_TWO;
                this.mBackwardMsgThreshold = 600;
                return;
            case 3:
                this.mWaitMsgFinishTime = WAIT_MSG_FINISH_TIME_ONE;
                this.mBackwardMsgThreshold = 600;
                return;
            default:
                this.mWaitMsgFinishTime = WAIT_MSG_FINISH_TIME_TWO;
                this.mBackwardMsgThreshold = 300;
                return;
        }
    }

    public void dumpMsgWhenAnr() {
        Log.d(TAG, "dumpMsgWhenAnr");
        if (this.mHistoryLooperHandler == null) {
            return;
        }
        Message msg = Message.obtain();
        msg.what = MSG_ANR_DUMP;
        this.mHistoryLooperHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DumpQueue {
        private OplusLooperEntry mCurrentMsg;
        private LinkedList<OplusLooperEntry> mMsgDoingList;
        private LinkedList<OplusLooperEntry> mMsgDumpList;
        private LinkedList<OplusLooperEntry> mMsgHHandlerSlowList;
        private LinkedList<OplusLooperEntry> mMsgMergeList;
        private LinkedList<OplusLooperEntry> mMsgSlowList;

        private DumpQueue() {
            this.mMsgDumpList = new LinkedList<>();
            this.mMsgSlowList = new LinkedList<>();
            this.mMsgHHandlerSlowList = new LinkedList<>();
            this.mMsgMergeList = new LinkedList<>();
            this.mMsgDoingList = new LinkedList<>();
        }

        public void clear() {
            this.mCurrentMsg = null;
            this.mMsgDumpList.clear();
        }

        public void startRegisterCurrentMsg(OplusLooperEntry msg) {
            if (this.mMsgDumpList.size() >= OplusLooperMsgDispatcher.this.mBackwardMsgThreshold) {
                OplusLooperEntry popMsg = this.mMsgDumpList.pop();
                popMsg.upDataLooperEntry(msg);
                this.mCurrentMsg = popMsg;
                return;
            }
            this.mCurrentMsg = new OplusLooperEntry();
        }

        public void processRegisterFinishMsg() {
            OplusLooperEntry oplusLooperEntry = this.mCurrentMsg;
            if (oplusLooperEntry == null) {
                return;
            }
            this.mMsgDumpList.add(oplusLooperEntry);
            if (this.mMsgDumpList.size() > OplusLooperMsgDispatcher.this.mBackwardMsgThreshold) {
                this.mMsgDumpList.pop();
            }
            this.mCurrentMsg = null;
        }

        public void doDumpMerge() {
            Log.w(OplusLooperMsgDispatcher.TAG, "---doDumpMerge----mMsgDumpList.size " + this.mMsgDumpList.size());
            OplusLooperMsgDispatcher.this.mLastDumpTime = SystemClock.uptimeMillis();
            while (this.mMsgDumpList.size() > 0) {
                OplusLooperEntry msg = this.mMsgDumpList.pop();
                if ("msg_dispatch_slow".equals(msg.mHistoryType)) {
                    this.mMsgSlowList.add(msg);
                } else if ("msg_h_handler_slow".equals(msg.mHistoryType)) {
                    this.mMsgHHandlerSlowList.add(msg);
                } else {
                    addToMergeList(msg);
                }
            }
            this.mMsgDoingList.clear();
            printCurrentAnr(this.mCurrentMsg);
        }

        public void doDumpMergeWhenRealAnr() {
            Log.w(OplusLooperMsgDispatcher.TAG, "---doDumpMerge----ANR occurred");
            long currentTime = SystemClock.uptimeMillis();
            if (currentTime - OplusLooperMsgDispatcher.this.mLastDumpTime <= 1000) {
                Log.w(OplusLooperMsgDispatcher.TAG, "doDumpMerge too frequently");
            } else {
                doDumpMerge();
            }
        }

        private void addToMergeList(OplusLooperEntry msg) {
            boolean hasAddToMergeList = false;
            Iterator<OplusLooperEntry> it = this.mMsgMergeList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                OplusLooperEntry mergeMsg = it.next();
                if (mergeMsg.equals(msg)) {
                    mergeMsg.mWalltime += msg.mWalltime;
                    mergeMsg.mCount++;
                    hasAddToMergeList = true;
                    break;
                }
            }
            if (!hasAddToMergeList) {
                boolean hasAddToDoingList = false;
                OplusLooperEntry tmpDoMsg = null;
                Iterator<OplusLooperEntry> it2 = this.mMsgDoingList.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    OplusLooperEntry doMsg = it2.next();
                    if (doMsg.equals(msg)) {
                        doMsg.mWalltime += msg.mWalltime;
                        doMsg.mCount++;
                        hasAddToDoingList = true;
                        tmpDoMsg = doMsg;
                        break;
                    }
                }
                if (hasAddToDoingList && tmpDoMsg != null) {
                    if (tmpDoMsg.mCount >= 25) {
                        this.mMsgDoingList.remove(tmpDoMsg);
                        this.mMsgMergeList.add(tmpDoMsg);
                        return;
                    }
                    return;
                }
                msg.mCount++;
                this.mMsgDoingList.add(msg);
            }
        }

        public void printCurrentAnr(OplusLooperEntry msg) {
            Log.w(OplusLooperMsgDispatcher.TAG, "==========================MsgBlockInfo==========================");
            if (msg != null) {
                Log.w(OplusLooperMsgDispatcher.TAG, "current msg is " + msg.toStringWithoutCount());
            } else if (msg == null) {
                Log.w(OplusLooperMsgDispatcher.TAG, "current msg is null");
            }
            dumpTrackedMessageLock();
        }

        public String dumpTrackedMessageLock() {
            Log.w(OplusLooperMsgDispatcher.TAG, "---------------main thread looper tracked messasge---------------");
            StringBuilder info = new StringBuilder();
            int j = 0;
            info.append("## mMsgSlowList. " + this.mMsgSlowList.size() + "\n");
            Iterator<OplusLooperEntry> it = this.mMsgSlowList.iterator();
            while (it.hasNext()) {
                OplusLooperEntry msg = it.next();
                info.append(" " + msg.toStringWithoutCount() + "\n");
                j++;
            }
            info.append("## mMsgMergeList." + this.mMsgMergeList.size() + "\n");
            Iterator<OplusLooperEntry> it2 = this.mMsgMergeList.iterator();
            while (it2.hasNext()) {
                OplusLooperEntry msg2 = it2.next();
                info.append(" " + msg2.toString() + "\n");
                j++;
            }
            info.append("## mMsgHSlowList." + this.mMsgHHandlerSlowList.size() + "\n");
            Iterator<OplusLooperEntry> it3 = this.mMsgHHandlerSlowList.iterator();
            while (it3.hasNext()) {
                OplusLooperEntry msg3 = it3.next();
                info.append(" " + msg3.toStringWithoutCount() + "\n");
                j++;
            }
            info.append("Finish dumping main looper message.\n");
            Log.e(OplusLooperMsgDispatcher.TAG, "dumpTrackedMessageLock MSG: \n" + info.toString());
            return info.toString();
        }
    }
}
