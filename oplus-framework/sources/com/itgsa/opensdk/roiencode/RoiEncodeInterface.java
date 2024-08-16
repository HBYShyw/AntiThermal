package com.itgsa.opensdk.roiencode;

import android.media.MediaCodec;
import android.media.MediaFormat;

/* loaded from: classes.dex */
public interface RoiEncodeInterface {
    int enableRoiEncode(MediaFormat mediaFormat, int i);

    boolean isRoiEncodeEnable(MediaCodec mediaCodec);

    @Deprecated
    int isRoiEncodeSupported();

    int isRoiEncodeSupported(MediaCodec mediaCodec);

    int setRoiParameter(MediaCodec mediaCodec, String str, long j);
}
