package com.itgsa.opensdk.roiencode;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaFormat;

/* loaded from: classes.dex */
public class RoiEncodeUnitClient {
    private static final int ROI_ENCODE_SUPPORT = 1;
    private static final int ROI_ENCODE_UNSUPPORT = 0;
    private static final int SDK_VERSION = 10000;
    private static final String TAG = RoiEncodeUnitClient.class.getSimpleName();
    private Context mContext;
    private RoiEncodeInterface mRoiEncodeInterface = new RoiEncodeUnitClientImpl();

    public RoiEncodeUnitClient(Context context) {
        this.mContext = context;
    }

    public int getVersion() {
        return 10000;
    }

    @Deprecated
    public int isRoiEncodeSupported() {
        boolean isRoiSupport = this.mContext.getPackageManager().hasSystemFeature("oplus.software.video.roiencode_support");
        if (isRoiSupport) {
            return 1;
        }
        return 0;
    }

    public int isRoiEncodeSupported(MediaCodec mediaCodec) {
        boolean isRoiSupport = this.mContext.getPackageManager().hasSystemFeature("oplus.software.video.roiencode_support");
        if (isRoiSupport) {
            return 1;
        }
        return 0;
    }

    public int enableRoiEncode(MediaFormat mediaFormat, int roiEnable) {
        RoiEncodeInterface roiEncodeInterface = this.mRoiEncodeInterface;
        if (roiEncodeInterface == null) {
            return -1;
        }
        int ext = roiEncodeInterface.enableRoiEncode(mediaFormat, roiEnable);
        return ext;
    }

    public boolean isRoiEncodeEnable(MediaCodec mediaCodec) {
        RoiEncodeInterface roiEncodeInterface = this.mRoiEncodeInterface;
        if (roiEncodeInterface == null) {
            return false;
        }
        boolean enable = roiEncodeInterface.isRoiEncodeEnable(mediaCodec);
        return enable;
    }

    public int setRoiParameter(MediaCodec mediaCodec, String roiInfo, long pts) {
        RoiEncodeInterface roiEncodeInterface = this.mRoiEncodeInterface;
        if (roiEncodeInterface == null) {
            return -1;
        }
        int ext = roiEncodeInterface.setRoiParameter(mediaCodec, roiInfo, pts);
        return ext;
    }
}
