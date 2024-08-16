package com.oplus.media;

import android.util.Log;
import java.io.FileDescriptor;

/* loaded from: classes.dex */
public class OplusHeifWriter {
    public static final int COLOR_FMT_MAX = 5;
    public static final int COLOR_FMT_NV12 = 3;
    public static final int COLOR_FMT_NV21 = 4;
    public static final int COLOR_FMT_P010 = 1;
    public static final int COLOR_FMT_RGBA8888 = 2;
    public static final int COLOR_FMT_YUV420Planar = 0;
    private static final String TAG = "OplusHeifWriter_Java";
    static final int maxValue = 100;
    static final int minValue = 0;
    private long mNativeObject = nativeSetup();

    private static native long nativeCreate(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native void nativeDestory(long j);

    private static native long nativeProcessHeicPhotoFrame(long j, byte[] bArr, byte[] bArr2, FileDescriptor fileDescriptor);

    private static native long nativeSetup();

    static {
        Log.v(TAG, "loadLibrary");
        System.loadLibrary("oplusheifwriter");
    }

    public boolean createPrimaryImage(int width, int height, int strideWidth, int strideHeight, int fmt, int quality, int rotation) {
        if (quality > 0 && quality <= 100) {
            if (width > 0 && height > 0 && strideWidth > 0 && strideHeight > 0 && fmt >= 0) {
                if (fmt < 5) {
                    long ret = nativeCreate(this.mNativeObject, width, height, strideWidth, strideHeight, fmt, quality, rotation);
                    Log.i(TAG, " OplusHeifWriter start! quality: " + quality);
                    if (ret < 0) {
                        return false;
                    }
                    return true;
                }
            }
            Log.i(TAG, "Input param error.");
            return false;
        }
        IllegalArgumentException e = new IllegalArgumentException("quality range error");
        throw e;
    }

    private void createTrack() {
    }

    private void addTrackSample() {
    }

    public boolean processPrimaryImage(byte[] yuvBuffer, byte[] exifData, FileDescriptor fd) {
        long ret = nativeProcessHeicPhotoFrame(this.mNativeObject, yuvBuffer, exifData, fd);
        if (ret < 0) {
            return false;
        }
        return true;
    }

    private void destory() {
        Log.i(TAG, " OplusHeifWriter destory!");
        nativeDestory(this.mNativeObject);
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mNativeObject != 0) {
                destory();
                this.mNativeObject = 0L;
            }
        } finally {
            super.finalize();
        }
    }
}
