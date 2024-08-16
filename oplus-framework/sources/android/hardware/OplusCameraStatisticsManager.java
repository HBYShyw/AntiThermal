package android.hardware;

import android.app.ActivityThread;
import android.batterySipper.OplusBaseBatterySipper;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.customize.OplusCustomizeConnectivityManager;
import android.util.Log;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public final class OplusCameraStatisticsManager extends BaseOplusCameraStatisticsManager implements IOplusCameraStatisticsManager {
    private static final String TAG = "OplusCameraStatisticsManager";
    private static OplusCameraStatisticsManager sInstance = new OplusCameraStatisticsManager();
    private int mCameraId = 0;
    private int mCurFaceCount = -1;

    private OplusCameraStatisticsManager() {
    }

    public static synchronized OplusCameraStatisticsManager getInstance() {
        OplusCameraStatisticsManager oplusCameraStatisticsManager;
        synchronized (OplusCameraStatisticsManager.class) {
            oplusCameraStatisticsManager = sInstance;
        }
        return oplusCameraStatisticsManager;
    }

    public void setCameraId(int cameraId) {
        this.mCameraId = cameraId;
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    public void setCurFaceCount(int curFaceCount) {
        this.mCurFaceCount = curFaceCount;
    }

    public void addPreviewInfo(final Camera.Parameters parameters) {
        initHandlerThread();
        this.mDataReportThread.post(new Runnable() { // from class: android.hardware.OplusCameraStatisticsManager.1
            @Override // java.lang.Runnable
            public void run() {
                Rect rect;
                try {
                    HashMap<String, String> eventMap = new HashMap<>();
                    OplusCameraStatisticsManager.this.buildCommonPreviewInfo(eventMap, System.currentTimeMillis(), String.valueOf(OplusCameraStatisticsManager.this.mCameraId), 1);
                    eventMap.put("face_count", String.valueOf(OplusCameraStatisticsManager.this.mCurFaceCount));
                    Camera.Size previewSize = parameters.getPreviewSize();
                    List<Camera.Area> focusAreas = parameters.getFocusAreas();
                    if (previewSize != null) {
                        eventMap.put("width", String.valueOf(previewSize.width));
                        eventMap.put("height", String.valueOf(previewSize.height));
                    }
                    if (focusAreas != null && focusAreas.size() > 0 && (rect = focusAreas.get(0).rect) != null) {
                        eventMap.put("touchxy_value", String.valueOf((rect.left + rect.right) / 2) + "," + String.valueOf((rect.top + rect.bottom) / 2));
                    }
                    OplusStatistics.onCommon(ActivityThread.currentApplication().getApplicationContext(), "2012002", "preview", (Map<String, String>) eventMap, false);
                    if (BaseOplusCameraStatisticsManager.LOG_PANIC) {
                        Log.d(OplusCameraStatisticsManager.TAG, "addPreviewInfo, eventMap: " + eventMap.toString());
                    }
                } catch (Exception exception) {
                    Log.e(OplusCameraStatisticsManager.TAG, "failure in addPreviewInfo", exception);
                }
            }
        });
    }

    public void addCaptureInfo(final Camera.Parameters parameters) {
        if (parameters == null) {
            return;
        }
        initHandlerThread();
        this.mDataReportThread.post(new Runnable() { // from class: android.hardware.OplusCameraStatisticsManager.2
            @Override // java.lang.Runnable
            public void run() {
                Rect rect;
                try {
                    HashMap<String, String> eventMap = new HashMap<>();
                    Camera.Size pictureSize = parameters.getPictureSize();
                    int zoom = parameters.getZoom();
                    String flashMode = parameters.getFlashMode();
                    List<Camera.Area> focusAreas = parameters.getFocusAreas();
                    eventMap.put(OplusBaseBatterySipper.BundlePkgName, ActivityThread.currentOpPackageName());
                    eventMap.put("camera_id", String.valueOf(OplusCameraStatisticsManager.this.mCameraId));
                    eventMap.put("apLevel", "1");
                    eventMap.put("halLevel", OplusCustomizeConnectivityManager.WLAN_POLICY_STRING_OFF);
                    if (OplusCameraStatisticsManager.this.mCameraId == 0) {
                        eventMap.put("rear_front", "rear");
                    } else {
                        eventMap.put("rear_front", "front");
                    }
                    if (pictureSize != null) {
                        eventMap.put("width", String.valueOf(pictureSize.width));
                        eventMap.put("height", String.valueOf(pictureSize.height));
                    }
                    eventMap.put("zoom", String.valueOf(zoom));
                    eventMap.put("iso_value", parameters.get("iso"));
                    eventMap.put("exp_value", parameters.get("exposure-time"));
                    if (flashMode != null) {
                        if (flashMode.equals("off")) {
                            eventMap.put("flash_trigger", "0");
                        } else if (flashMode.equals("on")) {
                            eventMap.put("flash_trigger", "1");
                        } else if (flashMode.equals("torch")) {
                            eventMap.put("flash_trigger", "2");
                        }
                    }
                    if (focusAreas != null && focusAreas.size() > 0 && (rect = focusAreas.get(0).rect) != null) {
                        eventMap.put("touchxy_value", String.valueOf((rect.left + rect.right) / 2) + "," + String.valueOf((rect.top + rect.bottom) / 2));
                    }
                    eventMap.put("face_count", String.valueOf(OplusCameraStatisticsManager.this.mCurFaceCount));
                    OplusStatistics.onCommon(ActivityThread.currentApplication().getApplicationContext(), "2012002", "photograph", (Map<String, String>) eventMap, false);
                    if (BaseOplusCameraStatisticsManager.LOG_PANIC) {
                        Log.d(OplusCameraStatisticsManager.TAG, "addCaptureInfo, eventMap: " + eventMap.toString());
                    }
                } catch (Exception exception) {
                    Log.e(OplusCameraStatisticsManager.TAG, "failure in addCaptureInfo", exception);
                }
            }
        });
    }
}
