package android.hardware.camera2;

import android.app.ActivityThread;
import android.batterySipper.OplusBaseBatterySipper;
import android.graphics.Rect;
import android.hardware.BaseOplusCameraStatisticsManager;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.customize.OplusCustomizeConnectivityManager;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import oplus.util.OplusStatistics;

/* loaded from: classes.dex */
public final class OplusCamera2StatisticsManager extends BaseOplusCameraStatisticsManager implements IOplusCamera2StatisticsManager {
    private static final String TAG = "OplusCamera2StatisticsManager";
    private static OplusCamera2StatisticsManager sInstance = new OplusCamera2StatisticsManager();
    private Face[] mFaces = null;
    private MeteringRectangle[] mAfRegions = null;
    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;
    private Method mGetTargetsMethod = null;

    private OplusCamera2StatisticsManager() {
    }

    public static synchronized OplusCamera2StatisticsManager getInstance() {
        OplusCamera2StatisticsManager oplusCamera2StatisticsManager;
        synchronized (OplusCamera2StatisticsManager.class) {
            oplusCamera2StatisticsManager = sInstance;
        }
        return oplusCamera2StatisticsManager;
    }

    public void addPreviewInfo(final String cameraId, final long disconnectTime) {
        initHandlerThread();
        this.mDataReportThread.post(new Runnable() { // from class: android.hardware.camera2.OplusCamera2StatisticsManager.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    HashMap<String, String> eventMap = new HashMap<>();
                    OplusCamera2StatisticsManager.this.buildCommonPreviewInfo(eventMap, disconnectTime, cameraId, 2);
                    if (OplusCamera2StatisticsManager.this.mFaces != null) {
                        eventMap.put("face_count", String.valueOf(OplusCamera2StatisticsManager.this.mFaces.length));
                    }
                    if (OplusCamera2StatisticsManager.this.mAfRegions != null && OplusCamera2StatisticsManager.this.mAfRegions.length > 0) {
                        MeteringRectangle afRegion = OplusCamera2StatisticsManager.this.mAfRegions[0];
                        eventMap.put("touchxy_value", String.valueOf(afRegion.getX()) + "," + String.valueOf(afRegion.getY()));
                    }
                    eventMap.put("width", String.valueOf(OplusCamera2StatisticsManager.this.mPreviewWidth));
                    eventMap.put("height", String.valueOf(OplusCamera2StatisticsManager.this.mPreviewHeight));
                    OplusStatistics.onCommon(ActivityThread.currentApplication().getApplicationContext(), "2012002", "preview", (Map<String, String>) eventMap, false);
                    if (BaseOplusCameraStatisticsManager.LOG_PANIC) {
                        Log.d(OplusCamera2StatisticsManager.TAG, "addPreviewInfo, eventMap: " + eventMap.toString());
                    }
                } catch (Exception exception) {
                    Log.e(OplusCamera2StatisticsManager.TAG, "failure in addPreviewInfo", exception);
                }
            }
        });
    }

    public void addCaptureInfo(final String cameraId, final CameraCharacteristics characteristics, TotalCaptureResult resultAsCapture) {
        try {
            Integer controlCaptureIntent = (Integer) resultAsCapture.get(CaptureResult.CONTROL_CAPTURE_INTENT);
            getSurface(resultAsCapture);
            if (controlCaptureIntent != null) {
                if (1 == controlCaptureIntent.intValue()) {
                    this.mFaces = (Face[]) resultAsCapture.get(CaptureResult.STATISTICS_FACES);
                    this.mAfRegions = (MeteringRectangle[]) resultAsCapture.get(CaptureResult.CONTROL_AF_REGIONS);
                }
                if (2 == controlCaptureIntent.intValue()) {
                    final Face[] faces = (Face[]) resultAsCapture.get(CaptureResult.STATISTICS_FACES);
                    final Integer iso = (Integer) resultAsCapture.get(CaptureResult.SENSOR_SENSITIVITY);
                    final Long exposureTime = (Long) resultAsCapture.get(CaptureResult.SENSOR_EXPOSURE_TIME);
                    final Integer flashMode = (Integer) resultAsCapture.get(CaptureResult.FLASH_MODE);
                    final Rect cropRegion = (Rect) resultAsCapture.get(CaptureResult.SCALER_CROP_REGION);
                    final MeteringRectangle[] afRegions = (MeteringRectangle[]) resultAsCapture.get(CaptureResult.CONTROL_AF_REGIONS);
                    initHandlerThread();
                    this.mDataReportThread.post(new Runnable() { // from class: android.hardware.camera2.OplusCamera2StatisticsManager.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                HashMap<String, String> eventMap = new HashMap<>();
                                eventMap.put("cameraId", String.valueOf(cameraId));
                                if (((Integer) characteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 0) {
                                    eventMap.put("rear_front", "front");
                                } else {
                                    eventMap.put("rear_front", "rear");
                                }
                                eventMap.put(OplusBaseBatterySipper.BundlePkgName, ActivityThread.currentOpPackageName());
                                eventMap.put("halLevel", OplusCustomizeConnectivityManager.WLAN_POLICY_STRING_OFF);
                                eventMap.put("apLevel", "2");
                                OplusCamera2StatisticsManager.this.buildCaptureEventMap(eventMap, faces, iso, exposureTime, flashMode, afRegions);
                                Rect activeRegion = (Rect) characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                                Rect rect = cropRegion;
                                if (rect != null && activeRegion != null && rect.width() > 0 && activeRegion.width() > 0) {
                                    float zoomvalue = activeRegion.width() / cropRegion.width();
                                    eventMap.put("zoom_value", String.valueOf(zoomvalue));
                                }
                                OplusStatistics.onCommon(ActivityThread.currentApplication().getApplicationContext(), "2012002", "photograph", (Map<String, String>) eventMap, false);
                                if (BaseOplusCameraStatisticsManager.LOG_PANIC) {
                                    Log.d(OplusCamera2StatisticsManager.TAG, "addCaptureInfo, eventMap: " + eventMap.toString());
                                }
                            } catch (Exception exception) {
                                Log.e(OplusCamera2StatisticsManager.TAG, "failure in addCaptureInfo", exception);
                            }
                        }
                    });
                }
            }
        } catch (Exception exception) {
            Log.e(TAG, "failure in addCaptureInfo", exception);
        }
    }

    private void getSurface(TotalCaptureResult resultAsCapture) {
        try {
            if (this.mGetTargetsMethod == null) {
                this.mGetTargetsMethod = CaptureRequest.class.getMethod("getTargets", new Class[0]);
            }
            Collection<Surface> targets = (Collection) this.mGetTargetsMethod.invoke(resultAsCapture.getRequest(), new Object[0]);
            long minSize = Long.MAX_VALUE;
            for (Surface target : targets) {
                Size size = SurfaceUtils.getSurfaceSize(target);
                long currentSize = size.getWidth() * size.getHeight();
                if (currentSize < minSize) {
                    minSize = currentSize;
                    this.mPreviewWidth = size.getWidth();
                    this.mPreviewHeight = size.getHeight();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "failure in getSurface");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildCaptureEventMap(HashMap<String, String> eventMap, Face[] faces, Integer iso, Long exposureTime, Integer flashMode, MeteringRectangle[] afRegions) {
        if (faces != null) {
            eventMap.put("face_count", String.valueOf(faces.length));
        }
        if (iso != null) {
            eventMap.put("iso_value", String.valueOf(iso));
        }
        if (exposureTime != null) {
            eventMap.put("exp_value", String.valueOf(exposureTime));
        }
        if (flashMode != null) {
            if (flashMode.intValue() == 0) {
                eventMap.put("flash_trigger", "0");
            } else if (1 == flashMode.intValue()) {
                eventMap.put("flash_trigger", "1");
            } else if (2 == flashMode.intValue()) {
                eventMap.put("flash_trigger", "2");
            }
        }
        if (afRegions != null && afRegions.length > 0) {
            MeteringRectangle afRegion = afRegions[0];
            eventMap.put("touchxy_value", String.valueOf(afRegion.getX()) + "," + String.valueOf(afRegion.getY()));
        }
    }
}
