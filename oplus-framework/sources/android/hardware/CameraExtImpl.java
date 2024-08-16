package android.hardware;

import android.common.OplusFeatureCache;
import android.hardware.Camera;
import android.os.Binder;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.permission.IOplusPermissionCheckInjector;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CameraExtImpl implements ICameraExt {
    private static final String TAG = "CameraExtImpl";
    private static CameraExtImpl sInstance;
    private Camera.Parameters mParameters = null;
    private volatile boolean mbIsCameraOpen = false;
    private volatile boolean mbIsPreviewStopped = true;

    public static synchronized CameraExtImpl getInstance() {
        CameraExtImpl cameraExtImpl;
        synchronized (CameraExtImpl.class) {
            if (sInstance == null) {
                sInstance = new CameraExtImpl();
                Log.i(TAG, "getInstance success!");
            }
            cameraExtImpl = sInstance;
        }
        return cameraExtImpl;
    }

    public String getComponentName() {
        return OplusCameraUtils.getInstance().getComponentName();
    }

    public void extendCamera(int cameraId, long connectTime) {
        this.mbIsCameraOpen = true;
        this.mbIsPreviewStopped = false;
        OplusCameraStatisticsManager.getInstance().setCameraId(cameraId);
        OplusCameraStatisticsManager.getInstance().addInfo(String.valueOf(cameraId), 1, connectTime);
    }

    public void extendRelease(long disconnectTime) {
        if (this.mbIsCameraOpen && this.mbIsPreviewStopped) {
            OplusCameraStatisticsManager.getInstance().addPreviewInfo(this.mParameters);
            this.mbIsCameraOpen = false;
        }
    }

    public void extendtakePicture(Camera.Parameters parameters) {
        OplusCameraStatisticsManager.getInstance().addCaptureInfo(parameters);
    }

    public void extendstopPreview(Camera.Parameters parameters) {
        this.mbIsPreviewStopped = true;
        OplusCameraStatisticsManager.getInstance().setActivityName(OplusCameraUtils.getActivityName());
        this.mParameters = parameters;
    }

    public void extendhandleMessage(int curFaceCount) {
        OplusCameraStatisticsManager.getInstance().setCurFaceCount(curFaceCount);
    }

    public int getNumPhysicalCameras(String packageName) {
        int numPhysicalCameras = 2;
        if (packageName == null) {
            return 2;
        }
        boolean exposeAuxCamera = false;
        String packageList = SystemProperties.get("vendor.camera.aux.packagelist");
        if (packageList.length() > 0) {
            TextUtils.StringSplitter splitter = new TextUtils.SimpleStringSplitter(PhoneNumberUtilsExtImpl.PAUSE);
            splitter.setString(packageList);
            Iterator it = splitter.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String str = (String) it.next();
                if (packageName.equals(str)) {
                    exposeAuxCamera = true;
                    break;
                }
            }
        }
        if (!exposeAuxCamera) {
            String packageList2 = OplusCameraUtils.getInstance().getStringResource(201589178);
            if (packageList2.length() > 0) {
                TextUtils.StringSplitter splitter2 = new TextUtils.SimpleStringSplitter(PhoneNumberUtilsExtImpl.PAUSE);
                splitter2.setString(packageList2);
                Iterator it2 = splitter2.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    String str2 = (String) it2.next();
                    if (packageName.equals(str2)) {
                        numPhysicalCameras = OplusCameraUtils.getInstance().getIntegerResource(202178563);
                        break;
                    }
                }
            }
            String packageList3 = OplusCameraUtils.getInstance().getStringResource(201589179);
            if (packageList3.length() > 0) {
                TextUtils.StringSplitter splitter3 = new TextUtils.SimpleStringSplitter(PhoneNumberUtilsExtImpl.PAUSE);
                splitter3.setString(packageList3);
                Iterator it3 = splitter3.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    String str3 = (String) it3.next();
                    if (packageName.startsWith(str3)) {
                        numPhysicalCameras = OplusCameraUtils.getInstance().getIntegerResource(202178563);
                        break;
                    }
                }
            }
        }
        if (numPhysicalCameras < 2) {
            return 2;
        }
        return numPhysicalCameras;
    }

    public boolean interceptTakePicture() {
        return !OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.CAMERA_TAKEPICTURE", Binder.getCallingPid(), Binder.getCallingUid(), "takePicture");
    }

    public static boolean interceptOpenWithCameraId() {
        return !OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.CAMERA", Binder.getCallingPid(), Binder.getCallingUid(), "openCamera");
    }

    public static boolean interceptOpen() {
        return !OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.CAMERA", Binder.getCallingPid(), Binder.getCallingUid(), "openCamera");
    }

    public static boolean interceptOpenLegacy() {
        return !OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.CAMERA", Binder.getCallingPid(), Binder.getCallingUid(), "openLegacyCamera");
    }
}
