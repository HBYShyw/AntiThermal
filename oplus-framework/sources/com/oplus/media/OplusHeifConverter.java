package com.oplus.media;

import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.util.Log;
import android.view.Surface;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

/* loaded from: classes.dex */
public class OplusHeifConverter {
    private static final int DECODE_BUFFER_SIZE = 16384;
    private static final String TAG = "OplusHeifConverter_Java";
    private static final int ftyp_box = 1718909296;
    private static final int heic_box = 1751476579;
    private static final int mif1_box = 1835623985;
    private long m10BitObject;
    private Surface mSurface;

    private static native boolean nativeCheckPPS(byte[] bArr);

    private static native long nativeCreateDecoder();

    private static native boolean nativeDecode(long j, InputStream inputStream, Surface surface, int i);

    private static native boolean nativeDecodeRegion(long j, InputStream inputStream, int i, int i2, int i3, int i4, Surface surface, int i5, int i6);

    private static native void nativeDestroyDecoder(long j);

    private static native HeifDecodedFrame nativeGetDecodeFrame(long j, InputStream inputStream, int i, boolean z);

    private static native HeifDecodedFrame nativeGetRegionDecodeFrame(long j, InputStream inputStream, int i, int i2, int i3, int i4, int i5, boolean z, int i6);

    private static native boolean nativeHeifConvert(InputStream inputStream, byte[] bArr, int i, OutputStream outputStream, byte[] bArr2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeRecycle(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeRender(byte[] bArr, int i, int i2, Surface surface, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeRenderDirectBuffer(long j, long j2, int i, int i2, Surface surface, int i3);

    static {
        Log.v(TAG, "loadLibrary");
        System.loadLibrary("oplus_heifconverter");
    }

    /* loaded from: classes.dex */
    public static class HeifDecodedFrame {
        public ColorSpace m_ColorSpace;
        public long m_buffer_id;
        public long m_buffer_id_sub;
        public int m_frame_height;
        public int m_frame_width;
        public boolean m_recycled;
        public byte[] m_yuvdata;

        public final boolean isRecycled() {
            return this.m_recycled;
        }

        public void recycle() {
            if (this.m_recycled) {
                return;
            }
            OplusHeifConverter.nativeRecycle(this.m_buffer_id, this.m_buffer_id_sub);
            this.m_yuvdata = null;
            this.m_recycled = true;
        }

        public boolean render(Surface sur, boolean isDirectBuffer, ColorSpace colorSpace) {
            int dataSpace;
            boolean result = false;
            if (this.m_recycled) {
                return true;
            }
            try {
                if (colorSpace.getId() == ColorSpace.Named.DISPLAY_P3.ordinal()) {
                    dataSpace = 143261696;
                } else {
                    dataSpace = 142671872;
                }
                if (isDirectBuffer) {
                    result = OplusHeifConverter.nativeRenderDirectBuffer(this.m_buffer_id, this.m_buffer_id_sub, this.m_frame_width, this.m_frame_height, sur, dataSpace);
                } else {
                    result = OplusHeifConverter.nativeRender(this.m_yuvdata, this.m_frame_width, this.m_frame_height, sur, dataSpace);
                }
            } catch (Exception e) {
                Log.e(OplusHeifConverter.TAG, "Unable to native10BitSetSurfaceYUVdata stream: " + e);
            }
            return result;
        }

        public boolean render(Surface sur, boolean isDirectBuffer) {
            boolean result = false;
            if (this.m_recycled) {
                return true;
            }
            try {
                if (isDirectBuffer) {
                    result = OplusHeifConverter.nativeRenderDirectBuffer(this.m_buffer_id, this.m_buffer_id_sub, this.m_frame_width, this.m_frame_height, sur, 143261696);
                } else {
                    result = OplusHeifConverter.nativeRender(this.m_yuvdata, this.m_frame_width, this.m_frame_height, sur, 143261696);
                }
            } catch (Exception e) {
                Log.e(OplusHeifConverter.TAG, "Unable to native10BitSetSurfaceYUVdata stream: " + e);
            }
            return result;
        }

        public HeifDecodedFrame(byte[] yuv, int width, int height, long id) {
            this.m_yuvdata = yuv;
            this.m_frame_width = width;
            this.m_frame_height = height;
            this.m_buffer_id = id;
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x0055 -> B:10:0x0077). Please report as a decompilation issue!!! */
    public static boolean convertHeifToJpegFromPath(String pathName, int quality, OutputStream Outstream) {
        byte[] tempStorage = new byte[16384];
        Log.e(TAG, " ConvertHeif2JPEG start! quality ###" + quality);
        if (Outstream == null) {
            throw new NullPointerException();
        }
        if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        }
        boolean result = false;
        InputStream Instream = null;
        try {
            try {
                try {
                    Instream = new FileInputStream(pathName);
                    Log.e(TAG, " ConvertHeif2JPEG start");
                    result = nativeHeifConvert(Instream, tempStorage, quality, Outstream, new byte[4096]);
                    Log.e(TAG, " ConvertHeif2JPEG result: " + result);
                    Instream.close();
                } catch (Throwable th) {
                    if (Instream != null) {
                        try {
                            Instream.close();
                        } catch (IOException e) {
                        }
                    }
                    throw th;
                }
            } catch (Exception e2) {
                Log.e(TAG, "Unable to ConvertHeif2JPEG stream: " + e2);
                if (Instream != null) {
                    Instream.close();
                }
            }
        } catch (IOException e3) {
        }
        return result;
    }

    public static boolean convertHeifToJpegFromStream(InputStream is, int quality, OutputStream Outstream) {
        Log.e(TAG, " convertHeif2JPEGFromStream start! quality ###" + quality);
        byte[] tempStorage = new byte[16384];
        if (Outstream == null) {
            throw new NullPointerException();
        }
        if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        }
        boolean result = false;
        try {
            Log.e(TAG, " ConvertHeif2JPEG start");
            result = nativeHeifConvert(is, tempStorage, quality, Outstream, new byte[4096]);
            Log.e(TAG, " ConvertHeif2JPEG result: " + result);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Unable to ConvertHeif2JPEG stream: " + e);
            return result;
        }
    }

    public int byteArrayToInt(byte[] b, int offset) {
        return (b[offset + 3] & 255) | ((b[offset + 2] & 255) << 8) | ((b[offset + 1] & 255) << 16) | ((b[offset] & 255) << 24);
    }

    private int getBoxInfo(HashSet set) {
        set.add(Integer.valueOf(heic_box));
        set.add(Integer.valueOf(mif1_box));
        return ftyp_box;
    }

    public boolean isHEIFFile(InputStream is) {
        byte[] fileData;
        int available;
        int i;
        HashSet set = new HashSet();
        int ftyp = getBoxInfo(set);
        try {
            try {
                DataInputStream data = new DataInputStream(is);
                fileData = new byte[1024];
                available = data.read(fileData);
            } catch (FileNotFoundException e) {
                e = e;
                e.printStackTrace();
                return false;
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e3) {
            e = e3;
        } catch (IOException e4) {
            e = e4;
        }
        if (available <= 8 || byteArrayToInt(fileData, 4) != ftyp) {
            Log.d(TAG, "Not mov file.");
            return false;
        }
        int size = byteArrayToInt(fileData, 0);
        if (size <= 8) {
            Log.d(TAG, "buffer not enought.");
            return false;
        }
        int brandCnt = (size - 8) / 4;
        Log.d(TAG, "brandCnt " + brandCnt + " size " + size);
        int i2 = 0;
        for (i = 8; i2 < brandCnt && (i2 * 4) + i + 4 < available; i = 8) {
            int box = byteArrayToInt(fileData, (i2 * 4) + i);
            set.remove(Integer.valueOf(box));
            Log.d(TAG, "remove box " + box);
            i2++;
        }
        if (!set.isEmpty()) {
            return false;
        }
        return true;
    }

    public int getFormat(InputStream is) throws IOException {
        int meta_box;
        int iprp_box;
        OplusHeifConverter oplusHeifConverter = this;
        int meta_box2 = 1835365473;
        int iprp_box2 = 1768977008;
        boolean keepParse = true;
        int len = is.available();
        byte[] data = new byte[len];
        BufferedInputStream Bis = new BufferedInputStream(is);
        Bis.read(data);
        int size = oplusHeifConverter.byteArrayToInt(data, 0);
        int cur = 0 + 4;
        int type = oplusHeifConverter.byteArrayToInt(data, cur);
        int cur2 = cur + 4;
        if (type != ftyp_box) {
            Log.e(TAG, "not a heif file!");
            Bis.close();
            return 0;
        }
        int ftyp_box2 = size - 8;
        int cur3 = cur2 + ftyp_box2;
        while (keepParse) {
            int size2 = oplusHeifConverter.byteArrayToInt(data, cur3);
            int cur4 = cur3 + 4;
            int type2 = oplusHeifConverter.byteArrayToInt(data, cur4);
            cur3 = cur4 + 4;
            boolean keepParse2 = keepParse;
            if (type2 == 1835295092) {
                if (size2 == 1) {
                    int size_h = oplusHeifConverter.byteArrayToInt(data, cur3);
                    int cur5 = cur3 + 4;
                    int size_l = oplusHeifConverter.byteArrayToInt(data, cur5);
                    int size3 = (size_h << 8) + size_l;
                    cur3 = cur5 + 4 + (size3 - 16);
                    meta_box = meta_box2;
                    iprp_box = iprp_box2;
                }
                meta_box = meta_box2;
                iprp_box = iprp_box2;
            } else if (type2 == meta_box2) {
                cur3 += 4;
                Log.d(TAG, "parse meta_box cur " + cur3);
                meta_box = meta_box2;
                iprp_box = iprp_box2;
            } else {
                if (type2 == iprp_box2) {
                    Log.d(TAG, "parse iprp_box cur  " + cur3);
                } else if (type2 == 1768973167) {
                    Log.d(TAG, "parse ipco_box cur  " + cur3);
                } else {
                    if (type2 == 1752589123) {
                        Log.d(TAG, "cur hvcC_box  " + cur3);
                        int cur6 = cur3 + 17;
                        int bitDepthLumaMinus8 = data[cur6] & 3;
                        int cur7 = cur6 + 1;
                        int meta_box3 = data[cur7] & 3;
                        int i = cur7 + 1;
                        if (bitDepthLumaMinus8 == 2 && meta_box3 == 2) {
                            Bis.close();
                            Log.d(TAG, "It is 10Bit Heif!");
                            return 1;
                        }
                        Bis.close();
                        Log.d(TAG, "It is 8Bit Heif!");
                        return 0;
                    }
                    meta_box = meta_box2;
                    iprp_box = iprp_box2;
                    cur3 += size2 - 8;
                }
                meta_box = meta_box2;
                iprp_box = iprp_box2;
            }
            if (cur3 <= len) {
                oplusHeifConverter = this;
                keepParse = keepParse2;
                meta_box2 = meta_box;
                iprp_box2 = iprp_box;
            } else {
                keepParse = false;
                oplusHeifConverter = this;
                meta_box2 = meta_box;
                iprp_box2 = iprp_box;
            }
        }
        Bis.close();
        Log.d(TAG, "It is 8Bit Heif!");
        return 0;
    }

    public int getFormat(FileDescriptor fd) throws IOException {
        int meta_box;
        int iprp_box;
        OplusHeifConverter oplusHeifConverter = this;
        FileInputStream is = new FileInputStream(fd);
        int meta_box2 = 1835365473;
        int iprp_box2 = 1768977008;
        boolean keepParse = true;
        int len = is.available();
        byte[] data = new byte[len];
        BufferedInputStream Bis = new BufferedInputStream(is);
        Bis.read(data);
        int size = oplusHeifConverter.byteArrayToInt(data, 0);
        int cur = 0 + 4;
        int type = oplusHeifConverter.byteArrayToInt(data, cur);
        int cur2 = cur + 4;
        if (type != ftyp_box) {
            Log.e(TAG, "not a heif file!");
            Bis.close();
            is.close();
            return 0;
        }
        int ftyp_box2 = size - 8;
        int cur3 = cur2 + ftyp_box2;
        while (keepParse) {
            int size2 = oplusHeifConverter.byteArrayToInt(data, cur3);
            int cur4 = cur3 + 4;
            int type2 = oplusHeifConverter.byteArrayToInt(data, cur4);
            cur3 = cur4 + 4;
            boolean keepParse2 = keepParse;
            if (type2 == 1835295092) {
                if (size2 == 1) {
                    int size_h = oplusHeifConverter.byteArrayToInt(data, cur3);
                    int cur5 = cur3 + 4;
                    int size_l = oplusHeifConverter.byteArrayToInt(data, cur5);
                    int size3 = (size_h << 8) + size_l;
                    cur3 = cur5 + 4 + (size3 - 16);
                    meta_box = meta_box2;
                    iprp_box = iprp_box2;
                }
                meta_box = meta_box2;
                iprp_box = iprp_box2;
            } else if (type2 == meta_box2) {
                cur3 += 4;
                Log.d(TAG, "parse meta_box cur " + cur3);
                meta_box = meta_box2;
                iprp_box = iprp_box2;
            } else {
                if (type2 == iprp_box2) {
                    Log.d(TAG, "parse iprp_box cur  " + cur3);
                } else if (type2 == 1768973167) {
                    Log.d(TAG, "parse ipco_box cur  " + cur3);
                } else {
                    if (type2 == 1752589123) {
                        Log.d(TAG, "cur hvcC_box  " + cur3);
                        int cur6 = cur3 + 17;
                        int bitDepthLumaMinus8 = data[cur6] & 3;
                        int cur7 = cur6 + 1;
                        int meta_box3 = data[cur7] & 3;
                        int i = cur7 + 1;
                        if (bitDepthLumaMinus8 == 2 && meta_box3 == 2) {
                            Bis.close();
                            is.close();
                            Log.d(TAG, "It is 10Bit Heif!");
                            return 1;
                        }
                        Bis.close();
                        is.close();
                        Log.d(TAG, "It is 8Bit Heif!");
                        return 0;
                    }
                    meta_box = meta_box2;
                    iprp_box = iprp_box2;
                    cur3 += size2 - 8;
                }
                meta_box = meta_box2;
                iprp_box = iprp_box2;
            }
            if (cur3 <= len) {
                oplusHeifConverter = this;
                keepParse = keepParse2;
                meta_box2 = meta_box;
                iprp_box2 = iprp_box;
            } else {
                keepParse = false;
                oplusHeifConverter = this;
                meta_box2 = meta_box;
                iprp_box2 = iprp_box;
            }
        }
        Bis.close();
        is.close();
        Log.d(TAG, "It is 8Bit Heif!");
        return 0;
    }

    public boolean createDecoder() {
        this.m10BitObject = nativeCreateDecoder();
        return true;
    }

    public boolean destroyDecoder() {
        nativeDestroyDecoder(this.m10BitObject);
        return true;
    }

    public boolean decode(InputStream is, int SampleSize, Surface sur) {
        this.mSurface = sur;
        if (sur == null) {
            Log.e(TAG, "sur is NULL!");
        }
        try {
            boolean result = nativeDecode(this.m10BitObject, is, sur, SampleSize);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Unable to nativeShow10BitHEIF stream: " + e);
            return false;
        }
    }

    public HeifDecodedFrame decode(InputStream is, int SampleSize, boolean isDirectBuffer) {
        HeifDecodedFrame heif_frame = null;
        try {
            heif_frame = nativeGetDecodeFrame(this.m10BitObject, is, SampleSize, isDirectBuffer);
            heif_frame.m_recycled = false;
            Log.e(TAG, "width: " + heif_frame.m_frame_width + " height:" + heif_frame.m_frame_height + " isDirectBuffer: " + isDirectBuffer);
            return heif_frame;
        } catch (Exception e) {
            Log.e(TAG, "Unable to nativeGet10BitYUVdata stream: " + e);
            return heif_frame;
        }
    }

    public boolean decodeRegion(InputStream is, Rect rect, int SampleSize, Surface sur) {
        Rect rect2;
        int flag;
        this.mSurface = sur;
        if (rect == null) {
            Log.e(TAG, "rect is null,decode whole image!");
            flag = 1;
            rect2 = new Rect(0, 0, 0, 0);
        } else {
            rect2 = rect;
            flag = 0;
        }
        try {
            boolean result = nativeDecodeRegion(this.m10BitObject, is, rect2.left, rect2.top, rect2.right - rect2.left, rect2.bottom - rect2.top, sur, SampleSize, flag);
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Unable to nativeShow10BitHEIF stream: " + e);
            return false;
        }
    }

    public HeifDecodedFrame decodeRegion(InputStream is, Rect rect, int SampleSize, boolean isDirectBuffer) {
        Rect rect2;
        int flag;
        HeifDecodedFrame heif_frame = null;
        if (rect == null) {
            Log.e(TAG, "rect is NULL!,decode whole image");
            rect2 = new Rect(0, 0, 0, 0);
            flag = 1;
        } else {
            rect2 = rect;
            flag = 0;
        }
        try {
            heif_frame = nativeGetRegionDecodeFrame(this.m10BitObject, is, rect2.left, rect2.top, rect2.right - rect2.left, rect2.bottom - rect2.top, SampleSize, isDirectBuffer, flag);
            heif_frame.m_recycled = false;
            Log.e(TAG, "width: " + heif_frame.m_frame_width + " height:" + heif_frame.m_frame_height);
            return heif_frame;
        } catch (Exception e) {
            Log.e(TAG, "Unable to nativeGet10BitYUVdata stream: " + e);
            return heif_frame;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:17:0x001e
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    public boolean decode(java.io.FileDescriptor r7, int r8, android.view.Surface r9) {
        /*
            r6 = this;
            r0 = 0
            r6.mSurface = r9
            java.lang.String r1 = "OplusHeifConverter_Java"
            if (r9 != 0) goto Ld
            java.lang.String r2 = "sur is NULL!"
            android.util.Log.e(r1, r2)
        Ld:
            java.io.FileInputStream r2 = new java.io.FileInputStream
            r2.<init>(r7)
            long r3 = r6.m10BitObject     // Catch: java.lang.Throwable -> L20 java.lang.Exception -> L22
            boolean r1 = nativeDecode(r3, r2, r9, r8)     // Catch: java.lang.Throwable -> L20 java.lang.Exception -> L22
            r0 = r1
            r2.close()     // Catch: java.io.IOException -> L1e
        L1d:
            goto L3e
        L1e:
            r1 = move-exception
            goto L1d
        L20:
            r1 = move-exception
            goto L3f
        L22:
            r3 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L20
            r4.<init>()     // Catch: java.lang.Throwable -> L20
            java.lang.String r5 = "Unable to nativeShow10BitHEIF stream: "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Throwable -> L20
            java.lang.StringBuilder r4 = r4.append(r3)     // Catch: java.lang.Throwable -> L20
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L20
            android.util.Log.e(r1, r4)     // Catch: java.lang.Throwable -> L20
            r2.close()     // Catch: java.io.IOException -> L1e
            goto L1d
        L3e:
            return r0
        L3f:
            r2.close()     // Catch: java.io.IOException -> L43
            goto L44
        L43:
            r3 = move-exception
        L44:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oplus.media.OplusHeifConverter.decode(java.io.FileDescriptor, int, android.view.Surface):boolean");
    }

    public HeifDecodedFrame decode(FileDescriptor fd, int SampleSize, boolean isDirectBuffer) {
        HeifDecodedFrame heif_frame = null;
        FileInputStream fis = new FileInputStream(fd);
        try {
            try {
                try {
                    heif_frame = nativeGetDecodeFrame(this.m10BitObject, fis, SampleSize, isDirectBuffer);
                    heif_frame.m_recycled = false;
                    fis.close();
                } catch (Throwable th) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                    throw th;
                }
            } catch (Exception e2) {
                Log.e(TAG, "Unable to nativeGet10BitYUVdata stream: " + e2);
                fis.close();
            }
        } catch (IOException e3) {
        }
        return heif_frame;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:18:0x004b
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    public boolean decodeRegion(java.io.FileDescriptor r19, android.graphics.Rect r20, int r21, android.view.Surface r22) {
        /*
            r18 = this;
            r1 = r18
            r2 = 0
            r13 = r22
            r1.mSurface = r13
            r0 = 0
            java.io.FileInputStream r5 = new java.io.FileInputStream
            r14 = r19
            r5.<init>(r14)
            java.lang.String r15 = "OplusHeifConverter_Java"
            if (r20 != 0) goto L23
            java.lang.String r3 = "rect is null!,decode whole image"
            android.util.Log.e(r15, r3)
            r0 = 1
            android.graphics.Rect r3 = new android.graphics.Rect
            r4 = 0
            r3.<init>(r4, r4, r4, r4)
            r16 = r0
            r12 = r3
            goto L27
        L23:
            r12 = r20
            r16 = r0
        L27:
            long r3 = r1.m10BitObject     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L54
            int r6 = r12.left     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L54
            int r7 = r12.top     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L54
            int r0 = r12.right     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L54
            int r8 = r12.left     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L54
            int r8 = r0 - r8
            int r0 = r12.bottom     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L54
            int r9 = r12.top     // Catch: java.lang.Throwable -> L4f java.lang.Exception -> L54
            int r9 = r0 - r9
            r10 = r22
            r11 = r21
            r17 = r12
            r12 = r16
            boolean r0 = nativeDecodeRegion(r3, r5, r6, r7, r8, r9, r10, r11, r12)     // Catch: java.lang.Exception -> L4d java.lang.Throwable -> L73
            r2 = r0
            r5.close()     // Catch: java.io.IOException -> L4b
        L4a:
            goto L72
        L4b:
            r0 = move-exception
            goto L4a
        L4d:
            r0 = move-exception
            goto L57
        L4f:
            r0 = move-exception
            r17 = r12
            r3 = r0
            goto L75
        L54:
            r0 = move-exception
            r17 = r12
        L57:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L73
            r3.<init>()     // Catch: java.lang.Throwable -> L73
            java.lang.String r4 = "Unable to nativeShow10BitHEIF stream: "
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L73
            java.lang.StringBuilder r3 = r3.append(r0)     // Catch: java.lang.Throwable -> L73
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L73
            android.util.Log.e(r15, r3)     // Catch: java.lang.Throwable -> L73
            r5.close()     // Catch: java.io.IOException -> L4b
            goto L4a
        L72:
            return r2
        L73:
            r0 = move-exception
            r3 = r0
        L75:
            r5.close()     // Catch: java.io.IOException -> L79
            goto L7a
        L79:
            r0 = move-exception
        L7a:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oplus.media.OplusHeifConverter.decodeRegion(java.io.FileDescriptor, android.graphics.Rect, int, android.view.Surface):boolean");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:16:0x006c
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1166)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:1022)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:55)
        */
    public com.oplus.media.OplusHeifConverter.HeifDecodedFrame decodeRegion(java.io.FileDescriptor r18, android.graphics.Rect r19, int r20, boolean r21) {
        /*
            r17 = this;
            r1 = 0
            r0 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream
            r12 = r18
            r4.<init>(r12)
            java.lang.String r13 = "OplusHeifConverter_Java"
            r14 = 0
            if (r19 != 0) goto L1d
            java.lang.String r2 = "rect is NULL!,decode whole image"
            android.util.Log.e(r13, r2)
            r0 = 1
            android.graphics.Rect r2 = new android.graphics.Rect
            r2.<init>(r14, r14, r14, r14)
            r16 = r0
            r15 = r2
            goto L21
        L1d:
            r15 = r19
            r16 = r0
        L21:
            r11 = r17
            long r2 = r11.m10BitObject     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r5 = r15.left     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r6 = r15.top     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r0 = r15.right     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r7 = r15.left     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r7 = r0 - r7
            int r0 = r15.bottom     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r8 = r15.top     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r8 = r0 - r8
            r9 = r20
            r10 = r21
            r11 = r16
            com.oplus.media.OplusHeifConverter$HeifDecodedFrame r0 = nativeGetRegionDecodeFrame(r2, r4, r5, r6, r7, r8, r9, r10, r11)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            r1 = r0
            r1.m_recycled = r14     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            r0.<init>()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            java.lang.String r2 = "width: "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r2 = r1.m_frame_width     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            java.lang.String r2 = " height: "
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            int r2 = r1.m_frame_height     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            java.lang.StringBuilder r0 = r0.append(r2)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            android.util.Log.e(r13, r0)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L71
            r4.close()     // Catch: java.io.IOException -> L6c
        L6b:
            goto L8d
        L6c:
            r0 = move-exception
            goto L6b
        L6e:
            r0 = move-exception
            r2 = r0
            goto L8e
        L71:
            r0 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6e
            r2.<init>()     // Catch: java.lang.Throwable -> L6e
            java.lang.String r3 = "Unable to nativeGet10BitYUVdata stream: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L6e
            java.lang.StringBuilder r2 = r2.append(r0)     // Catch: java.lang.Throwable -> L6e
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L6e
            android.util.Log.e(r13, r2)     // Catch: java.lang.Throwable -> L6e
            r4.close()     // Catch: java.io.IOException -> L6c
            goto L6b
        L8d:
            return r1
        L8e:
            r4.close()     // Catch: java.io.IOException -> L92
            goto L93
        L92:
            r0 = move-exception
        L93:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oplus.media.OplusHeifConverter.decodeRegion(java.io.FileDescriptor, android.graphics.Rect, int, boolean):com.oplus.media.OplusHeifConverter$HeifDecodedFrame");
    }
}
