package android.hardware;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusCameraUtils implements IOplusCameraUtils {
    private static final String TAG = "OplusCameraUtils";
    private static OplusCameraUtils sInstance = new OplusCameraUtils();

    public static synchronized OplusCameraUtils getInstance() {
        OplusCameraUtils oplusCameraUtils;
        synchronized (OplusCameraUtils.class) {
            oplusCameraUtils = sInstance;
        }
        return oplusCameraUtils;
    }

    public static String getActivityName() {
        try {
            ActivityManager activityManager = (ActivityManager) ActivityThread.currentApplication().getApplicationContext().getSystemService("activity");
            String activityName = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            Log.i(TAG, "current activityName: " + activityName);
            return activityName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getStringResource(int id) {
        return ActivityThread.currentApplication().getApplicationContext().getString(id);
    }

    public int getIntegerResource(int id) {
        return ActivityThread.currentApplication().getApplicationContext().getResources().getInteger(id);
    }

    @Override // android.hardware.IOplusCameraUtils
    public String getComponentName() {
        String packageName = ActivityThread.currentOpPackageName();
        String activityName = "";
        try {
            activityName = getActivityName();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        String componentName = packageName + '/' + activityName;
        Log.i(TAG, "getComponentName, componentName: " + componentName + ", packageName:" + packageName + ", activityName:" + activityName);
        return componentName;
    }

    private OplusCameraUtils() {
        Log.d(TAG, "new OplusCameraUtils!");
    }
}
