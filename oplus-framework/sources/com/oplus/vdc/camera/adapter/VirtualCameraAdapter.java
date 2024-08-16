package com.oplus.vdc.camera.adapter;

import android.util.Log;
import android.util.Range;
import android.view.Surface;

/* loaded from: classes.dex */
public class VirtualCameraAdapter {
    private static final int ERROR_NOT_SUPPORTED = -100;
    private static final String OPLUS_VIRTUAL_CAMERA = "OplusVirtualCamera";
    private static final String TAG = "VirtualCameraAdapterController";
    private volatile boolean mHasLoaded;

    /* loaded from: classes.dex */
    public interface StateCallback {
        void onConfigurationChanged(int i, int i2, int i3, Range<Integer> range, Surface surface);

        void onDisconnected(int i);

        void onError(int i, int i2);

        void onOpened(int i, int i2, int i3, Range<Integer> range, Surface surface);

        void onSettingChanged(int i, byte[] bArr);
    }

    private native int native_attachCamera(String str, StateCallback stateCallback);

    private native void native_detachCamera(int i);

    private native int native_getNumberOfAttachedCameras();

    private native int native_getNumberOfCameras();

    private native boolean native_isSupported();

    private native void native_onStateChanged(int i, int i2);

    private native boolean native_switchTo(int i);

    /* loaded from: classes.dex */
    private static class VirtualCameraAdapterHolder {
        static VirtualCameraAdapter sHolder = new VirtualCameraAdapter();

        private VirtualCameraAdapterHolder() {
        }
    }

    private VirtualCameraAdapter() {
        this.mHasLoaded = false;
    }

    private void loadLibrary() {
        try {
            System.loadLibrary(OPLUS_VIRTUAL_CAMERA);
            this.mHasLoaded = true;
            Log.d(TAG, "osdk load library done");
        } catch (Exception | UnsatisfiedLinkError e) {
            Log.e(TAG, "loadLibrary: " + e.getMessage());
        }
    }

    public static VirtualCameraAdapter getInstance() {
        if (!VirtualCameraAdapterHolder.sHolder.mHasLoaded) {
            VirtualCameraAdapterHolder.sHolder.loadLibrary();
        }
        return VirtualCameraAdapterHolder.sHolder;
    }

    public boolean isValid() {
        return this.mHasLoaded;
    }

    public boolean isSupported() {
        if (this.mHasLoaded) {
            return native_isSupported();
        }
        return false;
    }

    public int getNumberOfCameras() {
        if (this.mHasLoaded) {
            return native_getNumberOfCameras();
        }
        return -100;
    }

    public int getNumberOfAttachedCameras() {
        if (this.mHasLoaded) {
            return native_getNumberOfAttachedCameras();
        }
        return -100;
    }

    public int attachCamera(String metadata, StateCallback callback) {
        if (this.mHasLoaded) {
            return native_attachCamera(metadata, callback);
        }
        return -100;
    }

    public void detachCamera(int cameraId) {
        if (this.mHasLoaded) {
            native_detachCamera(cameraId);
        }
    }

    public boolean switchTo(int cameraId) {
        if (this.mHasLoaded) {
            return native_switchTo(cameraId);
        }
        return false;
    }

    public void onStateChanged(int cameraId, int state) {
        if (this.mHasLoaded) {
            native_onStateChanged(cameraId, state);
        }
    }
}
