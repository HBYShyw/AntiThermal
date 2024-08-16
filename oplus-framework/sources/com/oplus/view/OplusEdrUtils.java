package com.oplus.view;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.SystemProperties;
import android.util.Size;
import android.util.Slog;
import android.view.SurfaceControl;
import android.view.SurfaceView;
import android.view.View;

/* loaded from: classes.dex */
public final class OplusEdrUtils {
    private static final boolean DBG;
    private static final boolean DEBUG;
    public static final int DOLBY_OFF_WITHOUT_ANIMATION = 131073;
    public static final int DOLBY_OFF_WITH_ANIMATION = 131072;
    public static final int DOLBY_ON_WITHOUT_ANIMATION = 131329;
    public static final int DOLBY_ON_WITH_ANIMATION = 131328;
    public static final int EDR_FEATURE_DOLBY_VERSION = 1;
    public static final int EDR_FEATURE_HDR_VERSION = 5;
    public static final int EDR_FEATURE_LOCALHDR_VERSION = 0;
    public static final int LOCALHDR_OFF_WITHOUT_ANIMATION = 65536;
    public static final int LOCALHDR_OFF_WITH_ANIMATION = 65537;
    public static final int LOCALHDR_ON_WITHOUT_ANIMATION = 65792;
    public static final int LOCALHDR_ON_WITH_ANIMATION = 65793;
    public static final int SIZE_DISPLAY_MAX = 16;
    public static final int SIZE_POSITION_MAX = 2;
    private static final String TAG = "EDR";

    /* loaded from: classes.dex */
    public static class OplusEdrParameters {
        public int[] displayPosition;
        public Rect displayRect;
        public float[] displayTransform;
        public int imageRotation;
    }

    /* loaded from: classes.dex */
    public static class OplusSkGainmapInfo {
        public static final int SIZE_RGBA_MAX = 4;
        public int fType = 0;
        public int fBaseImageType = 0;
        public float scale = 1.0f;
        public float fDisplayRatioSdr = 1.0f;
        public float fDisplayRatioHdr = 2.0f;
        public float[] fEpsilonSdr = {0.0f, 0.0f, 0.0f, 1.0f};
        public float[] fEpsilonHdr = {0.0f, 0.0f, 0.0f, 1.0f};
        public float[] fGainmapRatioMin = {1.0f, 1.0f, 1.0f, 1.0f};
        public float[] fGainmapRatioMax = {2.0f, 2.0f, 2.0f, 1.0f};
        public float[] fGainmapGamma = {1.0f, 1.0f, 1.0f, 1.0f};
    }

    static {
        boolean z = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        DBG = z;
        DEBUG = z | false;
    }

    public static SurfaceControl getSurfaceControl(View view) {
        return view.getViewWrapper().getViewExt().getViewRootSurfaceControl();
    }

    public static SurfaceControl getBlastSurfaceControl(SurfaceView view) {
        return view.getWrapper().getBlastSurfaceControl();
    }

    public static int getLocalHdrVersion() {
        int version = SystemProperties.getInt("persist.sys.feature.localhdr_version", -1);
        return version;
    }

    public static boolean getLocalHdrSubDisplaySupport() {
        return SystemProperties.getBoolean("persist.oplus.display.fold.support", false) && SystemProperties.getBoolean("persist.sys.feature.localhdr_secondary_display_support", false);
    }

    public static int getDolbyEdrVersion() {
        int version = SystemProperties.getInt("persist.sys.feature.dolby_vision_app", -1);
        return version;
    }

    public static int getEdrFeatureVersion(int feature) {
        switch (feature) {
            case 0:
                int version = SystemProperties.getInt("persist.sys.feature.localhdr_version", -1);
                return version;
            case 1:
                int version2 = SystemProperties.getInt("persist.sys.feature.dolby_vision_app", -1);
                return version2;
            case 5:
                int version3 = SystemProperties.getInt("persist.sys.feature.hdr_vision_app", -1);
                return version3;
            default:
                Slog.d(TAG, "unknow feature: " + feature);
                return -1;
        }
    }

    public static boolean setEdrFlags(SurfaceControl sc, SurfaceControl.Transaction transaction, int flags) {
        transaction.setEdrFlags(sc, flags);
        return true;
    }

    public static boolean setEdrImageSize(SurfaceControl sc, SurfaceControl.Transaction transaction, Size imageSize, int index) {
        transaction.setEdrImageSize(sc, imageSize, index);
        return true;
    }

    public static boolean setEdrImageMetadata(SurfaceControl sc, SurfaceControl.Transaction transaction, byte[] metadata, int index) {
        transaction.setEdrImageMetadata(sc, metadata, index);
        return true;
    }

    public static boolean setEdrAuxiliaryImage(SurfaceControl sc, SurfaceControl.Transaction transaction, Bitmap bitmap, int index) {
        transaction.setEdrAuxiliaryImage(sc, bitmap, index);
        return true;
    }

    public static boolean setEdrViewTransform(SurfaceControl sc, SurfaceControl.Transaction transaction, OplusEdrParameters para, int index) {
        transaction.setEdrViewTransform(sc, para.imageRotation, para.displayPosition, para.displayRect, para.displayTransform, index);
        return true;
    }

    public static boolean isUHDRSupport() {
        return SystemProperties.getBoolean("persist.sys.feature.uhdr.support", false);
    }

    public static boolean setEdrGainmapInfo(SurfaceControl sc, SurfaceControl.Transaction transaction, OplusSkGainmapInfo info, int index) {
        Parcel gainmapInfo = Parcel.obtain();
        gainmapInfo.writeInt(info.fType);
        gainmapInfo.writeInt(info.fBaseImageType);
        gainmapInfo.writeFloat(info.scale);
        gainmapInfo.writeFloat(info.fDisplayRatioSdr);
        gainmapInfo.writeFloat(info.fDisplayRatioHdr);
        gainmapInfo.writeFloatArray(info.fEpsilonSdr);
        gainmapInfo.writeFloatArray(info.fEpsilonHdr);
        gainmapInfo.writeFloatArray(info.fGainmapRatioMin);
        gainmapInfo.writeFloatArray(info.fGainmapRatioMax);
        gainmapInfo.writeFloatArray(info.fGainmapGamma);
        gainmapInfo.setDataPosition(0);
        try {
            transaction.setEdrGainmapInfo(sc, gainmapInfo, index);
            gainmapInfo.recycle();
            return true;
        } catch (Throwable th) {
            gainmapInfo.recycle();
            throw th;
        }
    }
}
