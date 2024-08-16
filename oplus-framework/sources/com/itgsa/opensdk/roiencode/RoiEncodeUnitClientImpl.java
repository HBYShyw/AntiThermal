package com.itgsa.opensdk.roiencode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.util.Log;
import com.oplus.gwsd.GwsdStatus;

/* loaded from: classes.dex */
public class RoiEncodeUnitClientImpl implements RoiEncodeInterface {
    public static final int TYPE_LENGTH = 6;
    public static boolean sRoiEnable = false;

    /* loaded from: classes.dex */
    public enum ErrorCode {
        ERROR_NON(0, "all param are supported"),
        ERROR_ENCODE_TYPE(-10001, "do not support this encode type"),
        ERROR_BIT_MODE(-10002, "do not support this bitrate mode"),
        ERROR_ENABLE_PARAMETER(-10003, "do not support this enable parameter"),
        ERROR_ROI_REGION_NUM(-10004, "roi nums is out of range"),
        ERROR_ROI_DELTAQP(-10005, "delta qp is out of range"),
        ERROR_ROI_REGION(-10006, "region is out of range"),
        ERROR_UNKNOWN(-10007, GwsdStatus.ERROR_UNKNOW);

        private int mCode;
        private String message;

        ErrorCode(int code, String message) {
            this.mCode = code;
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

        public int getCode() {
            return this.mCode;
        }
    }

    @Override // com.itgsa.opensdk.roiencode.RoiEncodeInterface
    @Deprecated
    public int isRoiEncodeSupported() {
        return 1;
    }

    @Override // com.itgsa.opensdk.roiencode.RoiEncodeInterface
    public int isRoiEncodeSupported(MediaCodec mediaCodec) {
        return 1;
    }

    @Override // com.itgsa.opensdk.roiencode.RoiEncodeInterface
    public int enableRoiEncode(MediaFormat mediaFormat, int roiEnable) {
        if (mediaFormat == null) {
            return ErrorCode.ERROR_ENABLE_PARAMETER.getCode();
        }
        sRoiEnable = false;
        String encodeType = mediaFormat.getString("mime");
        if (!encodeType.equals("video/avc") && !encodeType.equals("video/hevc")) {
            return ErrorCode.ERROR_ENCODE_TYPE.getCode();
        }
        int bitrateMode = mediaFormat.getInteger("bitrate-mode");
        if (bitrateMode != 1) {
            return ErrorCode.ERROR_BIT_MODE.getCode();
        }
        if (roiEnable == 0 || roiEnable == 1) {
            if (roiEnable == 1) {
                Log.d("RoiEncodeUnitClientImpl", "enable ROI");
                sRoiEnable = true;
                mediaFormat.setInteger("roi-on", roiEnable);
                mediaFormat.setString("vendor.qti-ext-extradata-enable.types", "roiinfo");
                mediaFormat.setInteger("vendor.qti-ext-enc-roiinfo.enable", 1);
            }
            return ErrorCode.ERROR_NON.getCode();
        }
        return ErrorCode.ERROR_ENABLE_PARAMETER.getCode();
    }

    @Override // com.itgsa.opensdk.roiencode.RoiEncodeInterface
    public boolean isRoiEncodeEnable(MediaCodec mediaCodec) {
        if (!sRoiEnable || mediaCodec == null) {
            return false;
        }
        return true;
    }

    @Override // com.itgsa.opensdk.roiencode.RoiEncodeInterface
    public int setRoiParameter(MediaCodec mediaCodec, String roiInfo, long pts) {
        if (sRoiEnable && roiInfo != null && mediaCodec != null) {
            Bundle param = new Bundle();
            String codecName = mediaCodec.getName();
            if (codecName != null && codecName.length() > 6) {
                String codecType = codecName.substring(0, 6);
                if (codecType.equals("c2.qti")) {
                    param.putLong("vendor.qti-ext-enc-roiinfo.timestamp", pts);
                    param.putString("vendor.qti-ext-enc-roiinfo.type", "rect");
                    param.putString("vendor.qti-ext-enc-roiinfo.rect-payload", roiInfo);
                } else {
                    param.putString("roi-rect", roiInfo);
                    param.putLong("roi-timestamp", pts);
                }
            }
            mediaCodec.setParameters(param);
            return ErrorCode.ERROR_NON.getCode();
        }
        return ErrorCode.ERROR_UNKNOWN.getCode();
    }
}
