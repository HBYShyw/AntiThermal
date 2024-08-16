package android.os;

import android.content.ComponentName;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.android.internal.util.OplusFrameworkStatsLog;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class OplusJankMonitor {
    private static final String TAG = "OplusJankMonitor";
    private static Method methodLogP = null;
    private static String OPLUS_FILED_NOT_USED = "OPLUS_FILED_NOT_USED";

    /* renamed from: -$$Nest$smisForegroundApp, reason: not valid java name */
    static /* bridge */ /* synthetic */ boolean m151$$Nest$smisForegroundApp() {
        return isForegroundApp();
    }

    /* loaded from: classes.dex */
    public static class LaunchTracker {
        private static final int INVALID_TIME = -1;
        private static final int INVALID_TRANSITION_TYPE = -1;
        private static long sActivityLaunchingIdentify = -1;
        private static LaunchTracker sLaunchTracker = null;
        private OplusActivityLaunchInfo oplusActivityLaunchInfo = null;
        private String mMemoryShortComponentName = null;
        private int mMemoryPid = -1;
        private String mMemoryState = null;

        public static LaunchTracker getInstance() {
            LaunchTracker launchTracker;
            synchronized (LaunchTracker.class) {
                if (sLaunchTracker == null) {
                    sLaunchTracker = new LaunchTracker();
                }
                launchTracker = sLaunchTracker;
            }
            return launchTracker;
        }

        /* loaded from: classes.dex */
        class OplusActivityLaunchInfo {
            long mOplusCurrentTransitionStartTime;
            long mOplusLaunchType = -1;
            String mPkgName;

            OplusActivityLaunchInfo(long time, String pkg) {
                this.mOplusCurrentTransitionStartTime = -1L;
                this.mPkgName = "";
                this.mPkgName = pkg;
                this.mOplusCurrentTransitionStartTime = time;
            }
        }

        public void appStartMemoryStateCapture(String shortComponentName, int pid, String memoryState) {
            this.mMemoryShortComponentName = shortComponentName;
            this.mMemoryPid = pid;
            this.mMemoryState = memoryState;
        }

        public synchronized void stopLaunchTrace(String shortComponentName, int pid, long timestamp, String launchedFromPackage, String stageInfo) {
            OplusActivityLaunchInfo oplusActivityLaunchInfo;
            String memoryState;
            ComponentName componentName = ComponentName.unflattenFromString(shortComponentName);
            if (componentName != null && (oplusActivityLaunchInfo = this.oplusActivityLaunchInfo) != null) {
                long delay = timestamp - oplusActivityLaunchInfo.mOplusCurrentTransitionStartTime;
                if (delay > 0 && this.oplusActivityLaunchInfo.mOplusCurrentTransitionStartTime != -1 && this.oplusActivityLaunchInfo.mOplusLaunchType != -1) {
                    if (this.oplusActivityLaunchInfo.mPkgName.equals(componentName.getPackageName()) && this.oplusActivityLaunchInfo.mOplusCurrentTransitionStartTime == sActivityLaunchingIdentify) {
                        PerformanceManager.addTaskTrackPid(1, pid, true);
                        if (this.oplusActivityLaunchInfo.mOplusLaunchType != 3) {
                            String launchContent = "LaunchTime2.0: " + shortComponentName + " " + this.oplusActivityLaunchInfo.mOplusLaunchType + " " + delay + " " + launchedFromPackage + " " + stageInfo;
                            StringBuilder sb = new StringBuilder();
                            sb.append(launchContent);
                            String memoryState2 = OplusJankMonitor.OPLUS_FILED_NOT_USED;
                            if (shortComponentName.equals(this.mMemoryShortComponentName) && pid == this.mMemoryPid) {
                                sb.append(" ");
                                sb.append(this.mMemoryState);
                                String memoryState3 = this.mMemoryState;
                                memoryState = memoryState3;
                            } else {
                                memoryState = memoryState2;
                            }
                            OplusJankMonitor.logP("Quality", sb.toString());
                            OplusFrameworkStatsLog.write(100034, System.currentTimeMillis(), shortComponentName, this.oplusActivityLaunchInfo.mOplusLaunchType, delay, launchedFromPackage, stageInfo, memoryState, OplusJankMonitor.OPLUS_FILED_NOT_USED);
                        }
                    }
                }
                sActivityLaunchingIdentify = -1L;
                this.oplusActivityLaunchInfo = null;
            }
        }

        public synchronized void startLaunchTrace(boolean processRunning, String shortComponentName, int pid, int resultCode) {
            ComponentName componentName = ComponentName.unflattenFromString(shortComponentName);
            if (componentName != null) {
                sActivityLaunchingIdentify = SystemClock.uptimeMillis();
                if (pid != 0) {
                    PerformanceManager.addTaskTrackPid(1, pid, false);
                }
                OplusActivityLaunchInfo oplusActivityLaunchInfo = new OplusActivityLaunchInfo(sActivityLaunchingIdentify, componentName.getPackageName());
                this.oplusActivityLaunchInfo = oplusActivityLaunchInfo;
                oplusActivityLaunchInfo.mOplusLaunchType = getLaunchTransitionType(processRunning, resultCode);
            }
        }

        public void setLaunchStageTime(String packageName, long time, String type) {
            if (TextUtils.isEmpty(packageName) || !OplusJankMonitor.m151$$Nest$smisForegroundApp()) {
                return;
            }
            OplusJankMonitor.logP("Quality", type + " delay " + time + " " + packageName + " " + Process.myPid());
            OplusFrameworkStatsLog.write(100008, System.currentTimeMillis(), type, time, packageName, Process.myPid());
        }

        public void setLaunchStageTime(String packageName, long time, String className, int what) {
            String type = null;
            if (TextUtils.isEmpty(packageName) || !OplusJankMonitor.m151$$Nest$smisForegroundApp()) {
                return;
            }
            if (what == 110 && className.contains("android.app.ActivityThread$H")) {
                type = "ActivityThread: bindApplication";
            } else if (what == 159 && className.contains("android.app.ActivityThread$H")) {
                type = "ActivityThread: activityLifecycle";
            } else if (what == 0 && className.contains("android.view.Choreographer$FrameHandler")) {
                type = "ActivityThread: doFrame";
            }
            setLaunchStageTime(packageName, time, type);
        }

        private int getLaunchTransitionType(boolean currentTransitionProcessRunning, int startResult) {
            if (currentTransitionProcessRunning) {
                if (startResult == 0) {
                    return 2;
                }
                if (startResult == 2) {
                    return 3;
                }
                return -1;
            }
            if (startResult == 0 || startResult == 2) {
                return 1;
            }
            return -1;
        }
    }

    private static boolean isScreenOn() {
        int state = SystemProperties.getInt("debug.tracing.screen_state", 2);
        return state == 2;
    }

    private static boolean isForegroundApp() {
        int forepid = Process.myPid();
        return isScreenOn() && forepid == Process.myTid();
    }

    private static Object callDeclaredMethod(Object target, String clsName, String methodName, Class[] parameterTypes, Object[] args) {
        try {
            Class cls = Class.forName(clsName);
            Method method = cls.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(target, args);
            return result;
        } catch (ClassNotFoundException e) {
            Log.i(TAG, "ClassNotFoundException : " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            Log.i(TAG, "NoSuchMethodException : " + e3.getMessage());
            e3.printStackTrace();
            return null;
        } catch (SecurityException e4) {
            e4.printStackTrace();
            return null;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logP(String tag, String content) {
        if (methodLogP == null) {
            try {
                Class cls = Class.forName("android.util.Log");
                Method declaredMethod = cls.getDeclaredMethod("p", String.class, String.class);
                methodLogP = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (ClassNotFoundException e) {
                Log.i(TAG, "ClassNotFoundException : " + e.getMessage());
                e.printStackTrace();
            } catch (NoSuchMethodException e2) {
                Log.i(TAG, "NoSuchMethodException : " + e2.getMessage());
                e2.printStackTrace();
            } catch (SecurityException e3) {
                e3.printStackTrace();
            }
        }
        Method method = methodLogP;
        if (method != null) {
            try {
                method.invoke(null, tag, content);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
    }
}
