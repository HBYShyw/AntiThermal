package android.hardware.camera2.impl;

import android.common.OplusFrameworkFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.OplusCamera2StatisticsManager;
import android.hardware.camera2.OplusCameraManager;
import android.hardware.camera2.TotalCaptureResult;
import android.util.Log;
import com.oplus.orms.IOplusResourceManager;
import com.oplus.orms.info.OrmsSaParam;

/* loaded from: classes.dex */
public class CameraDeviceImplExtImpl implements ICameraDeviceImplExt {
    private static final String TAG = "CameraDeviceImplExtImpl";
    private static CameraDeviceImplExtImpl sInstance;
    private volatile boolean mbIsCameraOpen = false;

    public static synchronized CameraDeviceImplExtImpl getInstance() {
        CameraDeviceImplExtImpl cameraDeviceImplExtImpl;
        synchronized (CameraDeviceImplExtImpl.class) {
            if (sInstance == null) {
                sInstance = new CameraDeviceImplExtImpl();
                Log.i(TAG, "getInstance success!");
            }
            cameraDeviceImplExtImpl = sInstance;
        }
        return cameraDeviceImplExtImpl;
    }

    public void extendParseSessionParameters(CaptureRequest sessionParams) {
        OplusCameraManager.getInstance().parseSessionParameters(sessionParams);
    }

    public boolean extendPrivilegedAppList(String packageName) {
        return OplusCameraManager.getInstance().isPrivilegedApp(packageName);
    }

    public boolean extendSession() {
        return OplusCameraManager.getInstance().isCameraUnitSession();
    }

    public void extendsetInfo(String cameraId, long connectTime) {
        this.mbIsCameraOpen = true;
        OplusCamera2StatisticsManager.getInstance().addInfo(cameraId, 2, connectTime);
    }

    public void extendsetCloseInfo(String cameraId, long connectTime) {
        if (this.mbIsCameraOpen) {
            OplusCamera2StatisticsManager.getInstance().addPreviewInfo(cameraId, connectTime);
            this.mbIsCameraOpen = false;
        }
    }

    public void extendaddCaptureInfo(String cameraId, CameraCharacteristics characteristics, TotalCaptureResult resultAsCapture) {
        OplusCamera2StatisticsManager.getInstance().addCaptureInfo(cameraId, characteristics, resultAsCapture);
        OplusCamera2StatisticsManager.getInstance().statisticFrameRate();
    }

    public void ormsSetSceneAction(String action, int timeout) {
        long rc = ((IOplusResourceManager) OplusFrameworkFactory.getInstance().getFeature(IOplusResourceManager.DEFAULT, new Object[]{CameraDeviceImplExtImpl.class})).ormsSetSceneAction(new OrmsSaParam("", action, timeout));
        Log.i(TAG, "ormsSetSceneAction " + action + " for " + timeout + " ms rc = " + rc);
    }
}
