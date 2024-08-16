package com.oplus.wrapper.hardware.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.os.Handler;
import java.util.List;

/* loaded from: classes.dex */
public class CameraDevice {
    private final android.hardware.camera2.CameraDevice mCameraDevice;

    public CameraDevice(android.hardware.camera2.CameraDevice cameraDevice) {
        this.mCameraDevice = cameraDevice;
    }

    public void createCustomCaptureSession(InputConfiguration inputConfig, List<OutputConfiguration> outputs, int operatingMode, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        this.mCameraDevice.createCustomCaptureSession(inputConfig, outputs, operatingMode, callback, handler);
    }
}
