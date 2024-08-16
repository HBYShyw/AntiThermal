package android.hardware.camera2;

import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.hardware.OplusCameraUtils;
import android.os.Binder;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.text.TextUtils;
import android.util.Log;
import com.oplus.orms.IOplusResourceManager;
import com.oplus.orms.info.OrmsSaParam;
import com.oplus.permission.IOplusPermissionCheckInjector;
import java.util.Iterator;

/* loaded from: classes.dex */
public class CameraManagerExtImpl implements ICameraManagerExt {
    private static final String TAG = "CameraManagerExtImpl";
    private static CameraManagerExtImpl sInstance;

    public static synchronized CameraManagerExtImpl getInstance() {
        CameraManagerExtImpl cameraManagerExtImpl;
        synchronized (CameraManagerExtImpl.class) {
            if (sInstance == null) {
                sInstance = new CameraManagerExtImpl();
                Log.i(TAG, "getInstance success!");
            }
            cameraManagerExtImpl = sInstance;
        }
        return cameraManagerExtImpl;
    }

    public void extendCameraManager(String packageName) {
        Log.i(TAG, "packagename is " + packageName);
        OplusCameraManager.getInstance().saveOpPackageName(packageName);
    }

    public String extendGetComponentName() {
        return OplusCameraUtils.getInstance().getComponentName();
    }

    public void extendSetPackageName() {
        OplusCameraManager.getInstance().setPackageName();
    }

    public int getNumPhysicalCameras(String packageName) {
        int numPhysicalCameras = -1;
        if (packageName == null) {
            return -1;
        }
        String packageList = OplusCameraUtils.getInstance().getStringResource(201589178);
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
                    numPhysicalCameras = OplusCameraUtils.getInstance().getIntegerResource(202178563);
                    break;
                }
            }
        }
        String packageList2 = OplusCameraUtils.getInstance().getStringResource(201589179);
        if (packageList2.length() > 0) {
            TextUtils.StringSplitter<String> splitter2 = new TextUtils.SimpleStringSplitter(PhoneNumberUtilsExtImpl.PAUSE);
            splitter2.setString(packageList2);
            for (String str2 : splitter2) {
                if (packageName.startsWith(str2)) {
                    return OplusCameraUtils.getInstance().getIntegerResource(202178563);
                }
            }
            return numPhysicalCameras;
        }
        return numPhysicalCameras;
    }

    public boolean interceptOpenCameraForUid() {
        return !OplusFeatureCache.getOrCreate(IOplusPermissionCheckInjector.DEFAULT, new Object[0]).checkPermission("android.permission.CAMERA", Binder.getCallingPid(), Binder.getCallingUid(), "openCamera");
    }

    public void ormsSetSceneAction(String action, int timeout) {
        long rc = ((IOplusResourceManager) OplusFrameworkFactory.getInstance().getFeature(IOplusResourceManager.DEFAULT, new Object[]{CameraManagerExtImpl.class})).ormsSetSceneAction(new OrmsSaParam("", action, timeout));
        Log.i(TAG, "ormsSetSceneAction " + action + " for " + timeout + " ms rc = " + rc);
    }
}
