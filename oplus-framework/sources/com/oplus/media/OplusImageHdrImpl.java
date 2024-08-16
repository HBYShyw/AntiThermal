package com.oplus.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class OplusImageHdrImpl {
    private static final int ANDROID_BITMAP_COMPRESS_FORMAT_JPEG = 0;
    private static final int ANDROID_BITMAP_COMPRESS_FORMAT_PNG = 1;
    private static final int ANDROID_BITMAP_COMPRESS_FORMAT_WEBP_LOSSLESS = 4;
    private static final int ANDROID_BITMAP_COMPRESS_FORMAT_WEBP_LOSSY = 3;
    private static final int DECODE_BUFFER_SIZE = 16384;
    private static final int ILLEGAL_FORMAT = -1;
    private static final int QUALITY_MAX = 100;
    private static final int QUALITY_MIN = 0;
    private static final String TAG = "OplusImageHdrImpl_Java";
    private static final int WORKING_COMPRESS_STORAGE = 4096;

    /* loaded from: classes.dex */
    public static class GainmapInfo {
        public int mBaseImageType;
        public float mDisplayRatioHdr;
        public float mDisplayRatioSdr;
        public float[] mEpsilonHdr;
        public float[] mEpsilonSdr;
        public Bitmap mGainmap;
        public float[] mGainmapGamma;
        public float[] mGainmapRatioMax;
        public float[] mGainmapRatioMin;
        public float mHdrScale;
        public boolean mIsJpegR;
        public int mType;
    }

    private static native boolean nativeCompressAlpha8(Bitmap bitmap, int i, int i2, OutputStream outputStream, byte[] bArr);

    private static native Bitmap nativeDecodeBaseJpeg(InputStream inputStream, byte[] bArr, BitmapFactory.Options options);

    private static native GainmapInfo nativeDecodeGainmapAndMetadata(InputStream inputStream, byte[] bArr, int i);

    private static native GainmapInfo nativeDemuxFile(InputStream inputStream, byte[] bArr);

    static {
        Log.v(TAG, "loadLibrary");
        System.loadLibrary("oplusImageHdrImpl");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.media.OplusImageHdrImpl$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat;

        static {
            int[] iArr = new int[Bitmap.CompressFormat.values().length];
            $SwitchMap$android$graphics$Bitmap$CompressFormat = iArr;
            try {
                iArr[Bitmap.CompressFormat.JPEG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.PNG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSLESS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private static int nativeCompressFormat(Bitmap.CompressFormat format) {
        switch (AnonymousClass1.$SwitchMap$android$graphics$Bitmap$CompressFormat[format.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                Log.e("TAG", "format " + format + " has no corresponding native compress format!");
                return -1;
        }
    }

    public static GainmapInfo demuxFile(FileDescriptor fd) {
        StringBuilder sb;
        FileInputStream fIs = new FileInputStream(fd);
        GainmapInfo sGainmapInfo = null;
        byte[] tempStorage = null;
        try {
            if (0 == 0) {
                try {
                    tempStorage = new byte[16384];
                } catch (Exception e) {
                    Log.e(TAG, "Unable to parse this file: " + e);
                    try {
                        fIs.close();
                    } catch (IOException e2) {
                        e = e2;
                        sb = new StringBuilder();
                        Log.e(TAG, sb.append("Unable to close this file: ").append(e).toString());
                        return sGainmapInfo;
                    }
                }
            }
            sGainmapInfo = nativeDemuxFile(fIs, tempStorage);
            try {
                fIs.close();
            } catch (IOException e3) {
                e = e3;
                sb = new StringBuilder();
                Log.e(TAG, sb.append("Unable to close this file: ").append(e).toString());
                return sGainmapInfo;
            }
            return sGainmapInfo;
        } catch (Throwable th) {
            try {
                fIs.close();
            } catch (IOException e4) {
                Log.e(TAG, "Unable to close this file: " + e4);
            }
            throw th;
        }
    }

    public static GainmapInfo demuxFile(InputStream inputStream) {
        byte[] tempStorage = null;
        if (0 == 0) {
            try {
                tempStorage = new byte[16384];
            } catch (Exception e) {
                Log.e(TAG, "Unable to parse this file: " + e);
                return null;
            }
        }
        GainmapInfo sGainmapInfo = nativeDemuxFile(inputStream, tempStorage);
        return sGainmapInfo;
    }

    public static Bitmap decodeBaseJpeg(FileDescriptor fd, BitmapFactory.Options opts) {
        FileInputStream fIs = new FileInputStream(fd);
        byte[] tempStorage = null;
        Bitmap sBm = null;
        try {
            if (0 == 0) {
                try {
                    try {
                        tempStorage = new byte[16384];
                    } catch (Throwable th) {
                        try {
                            fIs.close();
                        } catch (IOException e) {
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    Log.e(TAG, "Unable to parse this file: " + e2);
                    fIs.close();
                }
            }
            sBm = nativeDecodeBaseJpeg(fIs, tempStorage, opts);
            fIs.close();
        } catch (IOException e3) {
        }
        return sBm;
    }

    public static Bitmap decodeBaseJpeg(InputStream inputStream, BitmapFactory.Options opts) {
        byte[] tempStorage = null;
        if (0 == 0) {
            try {
                tempStorage = new byte[16384];
            } catch (Exception e) {
                Log.e(TAG, "Unable to parse this file: " + e);
                return null;
            }
        }
        Bitmap sBm = nativeDecodeBaseJpeg(inputStream, tempStorage, opts);
        return sBm;
    }

    public static GainmapInfo decodeGainmapAndMetadata(FileDescriptor fd, int sampleSize) {
        FileInputStream fIs = new FileInputStream(fd);
        GainmapInfo sGainmapInfo = null;
        byte[] tempStorage = null;
        try {
            if (0 == 0) {
                try {
                    try {
                        tempStorage = new byte[16384];
                    } catch (Throwable th) {
                        try {
                            fIs.close();
                        } catch (IOException e) {
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    Log.e(TAG, "Unable to parse this file: " + e2);
                    fIs.close();
                }
            }
            sGainmapInfo = nativeDecodeGainmapAndMetadata(fIs, tempStorage, sampleSize);
            fIs.close();
        } catch (IOException e3) {
        }
        return sGainmapInfo;
    }

    public static GainmapInfo decodeGainmapAndMetadata(InputStream inputStream, int sampleSize) {
        byte[] tempStorage = null;
        if (0 == 0) {
            try {
                tempStorage = new byte[16384];
            } catch (Exception e) {
                Log.e(TAG, "Unable to parse this file: " + e);
                return null;
            }
        }
        GainmapInfo sGainmapInfo = nativeDecodeGainmapAndMetadata(inputStream, tempStorage, sampleSize);
        return sGainmapInfo;
    }

    public static boolean compressAlpha8(Bitmap bitmap, Bitmap.CompressFormat format, int quality, OutputStream stream) {
        if (bitmap == null || stream == null) {
            Log.e(TAG, "bitmap or stream is null");
            return false;
        }
        int nativeFormat = nativeCompressFormat(format);
        if (nativeFormat != 0 && nativeFormat != 1) {
            Log.e(TAG, "illegal format");
            return false;
        }
        if (quality < 0 || quality > 100) {
            Log.e(TAG, "quality must be 0..100");
            return false;
        }
        byte[] tempStorage = null;
        if (0 == 0) {
            try {
                tempStorage = new byte[4096];
            } catch (Exception e) {
                Log.e(TAG, "Unable to compress this bitmap: " + e);
                return false;
            }
        }
        boolean result = nativeCompressAlpha8(bitmap, nativeFormat, quality, stream, tempStorage);
        return result;
    }
}
