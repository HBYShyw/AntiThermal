package com.oplus.wrapper.hardware.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class CameraManager {
    private final android.hardware.camera2.CameraManager mCameraManager;

    public CameraManager(android.hardware.camera2.CameraManager cameraManager) {
        this.mCameraManager = cameraManager;
    }

    public void openCamera(String cameraId, int oomScoreOffset, Executor executor, CameraDevice.StateCallback callback) throws CameraAccessException {
        this.mCameraManager.openCamera(cameraId, oomScoreOffset, executor, callback);
    }
}
