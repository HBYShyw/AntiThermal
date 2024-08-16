package android.os;

import android.os.OplusJankMonitor;

/* loaded from: classes.dex */
public class OplusJankMonitorExtImpl implements IOplusJankMonitorExt {
    private static final String TYPE_ACTIVITYLIFECYCLE = "activityLifecycle";
    private static final String TYPE_ACTIVITYLIFECYCLE_ACTIVITYONSTART = "callActivityOnStart";
    private static final String TYPE_ACTIVITYLIFECYCLE_ACTIVITYRESUME = "activityResume";
    private static final String TYPE_ACTIVITYLIFECYCLE_ACTIVITYSTART = "activityStart";
    private static final String TYPE_ACTIVITYLIFECYCLE_ACTIVITYSTOP = "activityPause";
    private static final String TYPE_ACTIVITYTHREADATTACH = "attach";
    private static final String TYPE_ACTIVITYTHREADMAIN = "activityThreadMain";
    private static final String TYPE_BINDAPPLICATION = "bindApplication";
    private static final String TYPE_BINDAPPLICATION_APPLICATIONCREATE = "callApplicationOnCreate";
    private static final String TYPE_BINDAPPLICATION_LOADZIP = "createClassLoader";
    private static final String TYPE_BINDAPPLICATION_MAKEAPPLICATION = "makeApplication";
    private static final String TYPE_BINDAPPLICATION_OPENDEX = "createOrUpdateClassLoaderLocked";
    private static final String TYPE_DOFRAME = "doFrame";
    private static final String TYPE_VIEW_DRAW = "performDraw";
    private static final String TYPE_VIEW_INFLATE = "viewInflate";
    private static final String TYPE_VIEW_LAYOUT = "performLayout";
    private static final String TYPE_VIEW_MEASURE = "performMeasure";
    private static volatile OplusJankMonitorExtImpl sInstance = null;

    private OplusJankMonitorExtImpl() {
    }

    public static OplusJankMonitorExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (OplusJankMonitorExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new OplusJankMonitorExtImpl();
                }
            }
        }
        return sInstance;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void setLaunchStageTime(String processName, String type, long startTime) {
        char c;
        long thresholdMillis;
        long now = SystemClock.uptimeMillis();
        long delay = now - startTime;
        switch (type.hashCode()) {
            case -1825565413:
                if (type.equals(TYPE_ACTIVITYLIFECYCLE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1631780373:
                if (type.equals(TYPE_VIEW_LAYOUT)) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -1481248699:
                if (type.equals(TYPE_VIEW_DRAW)) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -1407259067:
                if (type.equals(TYPE_ACTIVITYTHREADATTACH)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1161036974:
                if (type.equals(TYPE_ACTIVITYTHREADMAIN)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1050553305:
                if (type.equals(TYPE_ACTIVITYLIFECYCLE_ACTIVITYSTOP)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1047235949:
                if (type.equals(TYPE_ACTIVITYLIFECYCLE_ACTIVITYSTART)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -912788106:
                if (type.equals(TYPE_ACTIVITYLIFECYCLE_ACTIVITYONSTART)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -470729267:
                if (type.equals(TYPE_BINDAPPLICATION_OPENDEX)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -200827633:
                if (type.equals(TYPE_BINDAPPLICATION_LOADZIP)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1203073235:
                if (type.equals(TYPE_BINDAPPLICATION)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1365067394:
                if (type.equals(TYPE_BINDAPPLICATION_MAKEAPPLICATION)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1673680365:
                if (type.equals(TYPE_BINDAPPLICATION_APPLICATIONCREATE)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1802029986:
                if (type.equals(TYPE_DOFRAME)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1853480988:
                if (type.equals(TYPE_ACTIVITYLIFECYCLE_ACTIVITYRESUME)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1934390973:
                if (type.equals(TYPE_VIEW_MEASURE)) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 1935760290:
                if (type.equals(TYPE_VIEW_INFLATE)) {
                    c = 16;
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
            case 1:
            case 2:
            case 3:
            case 4:
                thresholdMillis = OplusDebug.LAUNCH_DELAY;
                break;
            case 5:
            case 6:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case 14:
            case 15:
                thresholdMillis = OplusDebug.LAUNCH_SECOND_DELAY;
                break;
            case 16:
                thresholdMillis = OplusDebug.VIEW_DELAY;
                break;
            default:
                thresholdMillis = Long.MAX_VALUE;
                break;
        }
        if (delay >= thresholdMillis) {
            OplusJankMonitor.LaunchTracker.getInstance().setLaunchStageTime(processName, delay, "ActivityThread: " + type);
        }
    }

    public void stopLaunchTrace(String shortComponentName, int pid, long timestamp, String launchedFromPackage, String stageInfo) {
        OplusJankMonitor.LaunchTracker.getInstance().stopLaunchTrace(shortComponentName, pid, timestamp, launchedFromPackage, stageInfo);
    }

    public void startLaunchTrace(boolean processRunning, String shortComponentName, int pid, int resultCode) {
        OplusJankMonitor.LaunchTracker.getInstance().startLaunchTrace(processRunning, shortComponentName, pid, resultCode);
    }

    public void appStartMemoryStateCapture(String shortComponentName, int pid, String memoryState) {
        OplusJankMonitor.LaunchTracker.getInstance().appStartMemoryStateCapture(shortComponentName, pid, memoryState);
    }
}
