package com.oplus.debug;

import android.os.Debug;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.oplus.uah.info.UAHPerfConstants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public class InputLog {
    public static final int CALL_STACK = 3;
    public static final String DEFAULT_CHILD_TAG_STRING = "DEFAULT_LABEL";
    public static final String ENGINEERING_TYPE_RELEASE = "release";
    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_DISABLE = 0;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_VERBOSE = 2;
    public static final int LOG_LEVEL_WARN = 4;
    public static final String LOG_TAG_STRING = "InputLog";
    public static final String OPLUS_IMAGE_ENGINEERING_TYPE = "ro.oplus.image.my_engineering.type";
    public static final String PERSIST_INPUT_JAVA_LEVEL = "persist.sys.input_java_level";
    public static final String PERSIST_INPUT_NATIVE_LEVEL = "persist.sys.input_native_level";
    public static final String PERSIST_TP_INPUT_TRACE = "persist.sys.tp_input.trace";
    private static final DebugEvent sEvent = new DebugEvent();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final List<String> INSECURE_LABELS = new ArrayList(Arrays.asList("WLAN"));
    private static int sCurrLevel = 5;
    private static boolean sDebug = false;
    private static boolean sVerbose = false;
    private static boolean sInfo = false;
    private static boolean sWarn = false;
    private static boolean sError = true;
    private static boolean sEngineeringRelease = false;

    public static void dynamicLog(int level) {
        Log.d(LOG_TAG_STRING, "dynamicLog, level = " + level);
        writeLevel(level);
        updateLogLevel();
    }

    private static void writeLevel(int level) {
        try {
            SystemProperties.set(PERSIST_INPUT_JAVA_LEVEL, String.valueOf(level));
        } catch (Exception e) {
            Log.e(LOG_TAG_STRING, "writeLevel error:" + e);
        }
    }

    private static int readLevel() {
        int result = 0;
        try {
            result = SystemProperties.getInt(PERSIST_INPUT_JAVA_LEVEL, 5);
        } catch (Exception e) {
            Log.e(LOG_TAG_STRING, "readLevel error:" + e);
        }
        d(LOG_TAG_STRING, "readLevel result:" + result);
        return result;
    }

    public static int getCurrentLogSwitchValue() {
        return readLevel();
    }

    public static void updateLogLevel() {
        int readLevel = readLevel();
        sCurrLevel = readLevel;
        sDebug = readLevel == 1;
        sVerbose = readLevel >= 1 && readLevel <= 2;
        sInfo = readLevel >= 1 && readLevel <= 3;
        sWarn = readLevel >= 1 && readLevel <= 4;
        sError = readLevel >= 1 && readLevel <= 5;
        try {
            sEngineeringRelease = ENGINEERING_TYPE_RELEASE.equals(SystemProperties.get(OPLUS_IMAGE_ENGINEERING_TYPE));
        } catch (Exception e) {
            Log.e(LOG_TAG_STRING, "read engineering type error:" + e);
        }
        d(LOG_TAG_STRING, "updateLogLevel value=" + sCurrLevel + ",engineering=" + sEngineeringRelease);
    }

    protected void finalize() throws Throwable {
        Log.d(LOG_TAG_STRING, this + " finalized");
        super.finalize();
    }

    /* loaded from: classes.dex */
    public static class DebugEvent {
        private static final int DEFAULT_STATE_FLAG = -1;
        private static final String INPUT_DISPATCH_STATE_ENQUEUE = "ENQUEUE_EVENT";
        private static final String INPUT_DISPATCH_STATE_FINISHED = "FINISH_EVENT";
        private static final String INPUT_DISPATCH_STATE_STARTED = "START_EVENT";
        private static final String SEPARATOR = "|";
        private static final int TYPE_KEY = 1;
        private static final int TYPE_MOTION = 2;
        private int action;
        private long downTime;
        private long eventTime;
        private final StringBuilder handleDetail = new StringBuilder(256);
        private final CopyOnWriteArrayList<String> stateRecord = new CopyOnWriteArrayList<>();
        private int type;

        public void start(InputEvent event) {
            if (event instanceof KeyEvent) {
                KeyEvent key = (KeyEvent) event;
                this.type = 1;
                this.action = key.getAction();
                this.downTime = key.getDownTime();
                this.eventTime = key.getEventTime();
            } else if (event instanceof MotionEvent) {
                MotionEvent motion = (MotionEvent) event;
                this.type = 2;
                this.action = motion.getAction();
                this.downTime = motion.getDownTime();
                this.eventTime = motion.getEventTime();
            }
            try {
                StringBuilder sb = this.handleDetail;
                sb.delete(0, sb.length());
            } catch (Exception e) {
                Log.e(InputLog.LOG_TAG_STRING, "handleDetail reset failed:" + e);
            }
            this.stateRecord.clear();
            this.stateRecord.add(formatRecord(INPUT_DISPATCH_STATE_STARTED, -1));
        }

        public void enqueue(boolean immediately, boolean scheduled) {
            this.stateRecord.add(formatRecord("ENQUEUE_EVENT|" + immediately + SEPARATOR + scheduled, -1));
        }

        public void deliverd(String stage, int flag) {
            this.stateRecord.add(formatRecord(stage, flag));
        }

        public void handled(String detail) {
            try {
                this.handleDetail.append(SEPARATOR);
                this.handleDetail.append(detail);
            } catch (Exception e) {
                Log.e(InputLog.LOG_TAG_STRING, "handled error:" + e);
            }
        }

        public void finish(int flag) {
            this.stateRecord.add(formatRecord(INPUT_DISPATCH_STATE_FINISHED, flag));
        }

        public boolean match(InputEvent event) {
            if (event == null) {
                return false;
            }
            if (event instanceof KeyEvent) {
                KeyEvent key = (KeyEvent) event;
                return this.type == 1 && this.action == key.getAction() && this.downTime == key.getDownTime() && this.eventTime == key.getEventTime();
            }
            if (!(event instanceof MotionEvent)) {
                return false;
            }
            MotionEvent motion = (MotionEvent) event;
            return this.type == 2 && this.action == motion.getAction() && this.downTime == motion.getDownTime() && this.eventTime == motion.getEventTime();
        }

        private String formatRecord(String state, int flag) {
            StringBuilder record = new StringBuilder();
            try {
                record.append(InputLog.currDate());
                record.append(SEPARATOR).append(state);
                if (-1 != flag) {
                    record.append(SEPARATOR).append("0x").append(Integer.toHexString(flag));
                }
            } catch (Exception e) {
                Log.e(InputLog.LOG_TAG_STRING, "formatRecord error:" + e);
            }
            return record.toString();
        }

        public String toString() {
            try {
                StringBuilder result = new StringBuilder("DebugEvent [ type=");
                result.append(this.type);
                result.append(", action=").append(this.action);
                result.append(", downTime=").append(this.downTime);
                result.append(", eventTime=").append(this.eventTime);
                result.append(", stateRecord=").append(this.stateRecord);
                result.append(", handleDetail=").append(this.handleDetail.toString());
                result.append(" ]");
                return result.toString();
            } catch (Exception e) {
                Log.e(InputLog.LOG_TAG_STRING, "toString error:" + e);
                return "E";
            }
        }
    }

    private static String processTag(String tag) {
        if (TextUtils.isEmpty(tag) || sDebug || !sEngineeringRelease) {
            return tag;
        }
        int i = 0;
        while (true) {
            try {
                List<String> list = INSECURE_LABELS;
                if (i >= list.size()) {
                    break;
                }
                String label = list.get(i);
                if (!tag.contains(label)) {
                    i++;
                } else {
                    return DEFAULT_CHILD_TAG_STRING;
                }
            } catch (Exception e) {
                Log.e(LOG_TAG_STRING, "processTag error:" + e);
            }
        }
        return tag;
    }

    public static void debugInputEventStart(String tag, InputEvent event) {
        if (canBePrinted(event)) {
            sEvent.start(event);
            v(processTag(tag), "debugInputEventStart event=" + event);
        }
    }

    public static void debugInputEventEnqueue(String tag, InputEvent event, boolean immediately, boolean scheduled) {
        if (canBePrinted(event)) {
            DebugEvent debugEvent = sEvent;
            if (debugEvent.match(event)) {
                debugEvent.enqueue(immediately, scheduled);
            }
        }
        if (sDebug) {
            d(tag, "enqueueInputEvent, call from:" + Debug.getCallers(3));
        }
    }

    public static void debugInputStageDeliverd(String tag, int flag, InputEvent event, String stage, String detail) {
        if (canBePrinted(event)) {
            DebugEvent debugEvent = sEvent;
            if (debugEvent.match(event)) {
                debugEvent.deliverd(stage, flag);
            }
        }
    }

    public static void debugEventHandled(String tag, InputEvent event, String detail) {
        if (canBePrinted(event)) {
            DebugEvent debugEvent = sEvent;
            if (debugEvent.match(event)) {
                debugEvent.handled(detail);
            }
            if (sDebug) {
                d(tag, "debugEventHandled detail:" + detail + ", sEvent=" + debugEvent);
            }
        }
    }

    public static void debugInputEventFinished(String tag, int flag, InputEvent event) {
        if (canBePrinted(event)) {
            DebugEvent debugEvent = sEvent;
            if (debugEvent.match(event)) {
                debugEvent.finish(flag);
                v(processTag(tag), "debugInputEventFinished event:" + event + ", sEvent=" + debugEvent);
            }
        }
    }

    public static boolean canBePrinted(InputEvent event) {
        if (sDebug) {
            return true;
        }
        if (sVerbose) {
            try {
                if (event instanceof KeyEvent) {
                    return true;
                }
                if (event instanceof MotionEvent) {
                    MotionEvent motion = (MotionEvent) event;
                    return isVerboseAction(motion.getActionMasked());
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean isVerboseAction(int action) {
        return action == 0 || action == 1 || action == 3;
    }

    public static boolean isVolumeKey(int keyCode) {
        switch (keyCode) {
            case 24:
            case 25:
            case UAHPerfConstants.UAH_EVENT_CAMERA_NIGHT_CAPTURE /* 164 */:
                return true;
            default:
                return false;
        }
    }

    public static String currDate() {
        return sDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static void i(String tag, String msg) {
        if (sInfo) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg);
        }
    }

    public static void i(String tag, String msg, Throwable error) {
        if (sInfo) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg, error);
        }
    }

    public static void v(String tag, String msg) {
        if (sVerbose) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg);
        }
    }

    public static void v(String tag, String msg, Throwable error) {
        if (sVerbose) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg, error);
        }
    }

    public static void d(String tag, String msg) {
        if (sDebug) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg);
        }
    }

    public static void d(String tag, String msg, Throwable error) {
        if (sDebug) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg, error);
        }
    }

    public static void w(String tag, String msg) {
        if (sWarn) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg);
        }
    }

    public static void w(String tag, String msg, Throwable error) {
        if (sWarn) {
            Log.w(LOG_TAG_STRING, tag + " : " + msg, error);
        }
    }

    public static void e(String tag, String msg) {
        if (sError) {
            Log.e(LOG_TAG_STRING, tag + " : " + msg);
        }
    }

    public static void e(String tag, String msg, Throwable error) {
        if (sError) {
            Log.e(LOG_TAG_STRING, tag + " : " + msg, error);
        }
    }

    public static void wtf(String tag, String msg) {
        if (sError) {
            Log.wtf(LOG_TAG_STRING, tag + " : " + msg);
        }
    }

    public static void wtf(String tag, String msg, Throwable error) {
        if (sError) {
            Log.wtf(LOG_TAG_STRING, tag + " : " + msg, error);
        }
    }

    public static boolean isLevelDebug() {
        return sDebug;
    }

    public static boolean isLevelVerbose() {
        return sVerbose;
    }
}
