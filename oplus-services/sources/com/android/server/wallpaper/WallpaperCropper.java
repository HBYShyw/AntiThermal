package com.android.server.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.os.FileUtils;
import android.os.SELinux;
import android.util.Slog;
import android.view.DisplayInfo;
import com.android.server.utils.TimingsTraceAndSlog;
import com.android.server.wallpaper.WallpaperDisplayHelper;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import libcore.io.IoUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WallpaperCropper {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_CROP = true;
    private static final String TAG = "WallpaperCropper";
    private final WallpaperDisplayHelper mWallpaperDisplayHelper;
    private IWallpaperManagerServiceExt mWallpaperManagerServiceExt;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WallpaperCropper(WallpaperDisplayHelper wallpaperDisplayHelper, IWallpaperManagerServiceExt iWallpaperManagerServiceExt) {
        this.mWallpaperDisplayHelper = wallpaperDisplayHelper;
        this.mWallpaperManagerServiceExt = iWallpaperManagerServiceExt;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void generateCrop(WallpaperData wallpaperData) {
        TimingsTraceAndSlog timingsTraceAndSlog = new TimingsTraceAndSlog(TAG);
        timingsTraceAndSlog.traceBegin("WPMS.generateCrop");
        generateCropInternal(wallpaperData);
        timingsTraceAndSlog.traceEnd();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(22:5|(1:7)(10:73|(1:75)(1:91)|76|(1:78)(1:90)|79|(1:81)|82|(1:84)|85|(9:89|9|(1:72)(1:15)|(2:17|(1:19))|20|(10:34|35|(2:36|(1:38)(1:39))|40|(1:42)|43|(1:45)(1:68)|46|(1:48)(6:50|51|52|54|55|56)|49)(2:23|(1:25))|(1:27)|28|(2:30|31)(1:33)))|8|9|(1:11)|72|(0)|20|(0)|34|35|(3:36|(0)(0)|38)|40|(0)|43|(0)(0)|46|(0)(0)|49|(0)|28|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0328, code lost:
    
        r3 = null;
        r16 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x031d, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x031e, code lost:
    
        r3 = null;
        r16 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x033c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0350  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0173 A[LOOP:0: B:36:0x016f->B:38:0x0173, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0175 A[EDGE_INSN: B:39:0x0175->B:40:0x0175 BREAK  A[LOOP:0: B:36:0x016f->B:38:0x0173], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01a5 A[Catch: all -> 0x031d, Exception -> 0x0328, TryCatch #5 {Exception -> 0x0328, all -> 0x031d, blocks: (B:35:0x0167, B:36:0x016f, B:40:0x0175, B:42:0x01a5, B:43:0x01e0, B:46:0x028f, B:48:0x02dc, B:50:0x02e6), top: B:34:0x0167 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x028a  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x02dc A[Catch: all -> 0x031d, Exception -> 0x0328, TryCatch #5 {Exception -> 0x0328, all -> 0x031d, blocks: (B:35:0x0167, B:36:0x016f, B:40:0x0175, B:42:0x01a5, B:43:0x01e0, B:46:0x028f, B:48:0x02dc, B:50:0x02e6), top: B:34:0x0167 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x02e6 A[Catch: all -> 0x031d, Exception -> 0x0328, TRY_LEAVE, TryCatch #5 {Exception -> 0x0328, all -> 0x031d, blocks: (B:35:0x0167, B:36:0x016f, B:40:0x0175, B:42:0x01a5, B:43:0x01e0, B:46:0x028f, B:48:0x02dc, B:50:0x02e6), top: B:34:0x0167 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x028d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void generateCropInternal(WallpaperData wallpaperData) {
        boolean z;
        boolean z2;
        boolean z3;
        BufferedOutputStream bufferedOutputStream;
        FileOutputStream fileOutputStream;
        FileOutputStream fileOutputStream2;
        final int i;
        int i2;
        int width;
        Bitmap decodeBitmap;
        FileOutputStream fileOutputStream3;
        int displayIdFromPhysicalDisplayId = this.mWallpaperManagerServiceExt.getDisplayIdFromPhysicalDisplayId(wallpaperData.mWallpaperDataExt.getPhysicalDisplayId(), this.mWallpaperDisplayHelper.getDisplayManager());
        WallpaperDisplayHelper.DisplayData displayDataOrCreate = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(displayIdFromPhysicalDisplayId);
        Rect rect = new Rect(wallpaperData.cropHint);
        DisplayInfo displayInfo = this.mWallpaperDisplayHelper.getDisplayInfo(displayIdFromPhysicalDisplayId);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        boolean z4 = true;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(wallpaperData.wallpaperFile.getAbsolutePath(), options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            Slog.w(TAG, "Invalid wallpaper data");
        } else {
            if (rect.isEmpty()) {
                rect.top = 0;
                rect.left = 0;
                rect.right = options.outWidth;
                rect.bottom = options.outHeight;
            } else {
                int i3 = rect.right;
                int i4 = options.outWidth;
                int i5 = i3 > i4 ? i4 - i3 : 0;
                int i6 = rect.bottom;
                int i7 = options.outHeight;
                rect.offset(i5, i6 > i7 ? i7 - i6 : 0);
                if (rect.left < 0) {
                    rect.left = 0;
                }
                if (rect.top < 0) {
                    rect.top = 0;
                }
                if (options.outHeight > rect.height() || options.outWidth > rect.width()) {
                    z2 = true;
                    z3 = rect.height() <= displayDataOrCreate.mHeight || rect.height() > GLHelper.getMaxTextureSize() || rect.width() > GLHelper.getMaxTextureSize();
                    if (z3) {
                        int width2 = (int) (rect.width() * (displayDataOrCreate.mHeight / rect.height()));
                        int i8 = displayInfo.logicalWidth;
                        if (width2 < i8) {
                            rect.bottom = (int) (rect.width() * (displayInfo.logicalHeight / i8));
                            z2 = true;
                        }
                    }
                    String str = TAG;
                    Slog.v(str, "crop: w=" + rect.width() + " h=" + rect.height());
                    Slog.v(str, "dims: w=" + displayDataOrCreate.mWidth + " h=" + displayDataOrCreate.mHeight);
                    Slog.v(str, "meas: w=" + options.outWidth + " h=" + options.outHeight);
                    Slog.v(str, "crop?=" + z2 + " scale?=" + z3);
                    if (z2 && !z3) {
                        z = FileUtils.copyFile(wallpaperData.wallpaperFile, wallpaperData.cropFile);
                        if (!z) {
                            wallpaperData.cropFile.delete();
                        }
                    } else {
                        i = 1;
                        while (true) {
                            i2 = i * 2;
                            if (i2 <= rect.height() / displayDataOrCreate.mHeight) {
                                break;
                            } else {
                                i = i2;
                            }
                        }
                        options.inSampleSize = i;
                        options.inJustDecodeBounds = false;
                        final Rect rect2 = new Rect(rect);
                        rect2.scale(1.0f / options.inSampleSize);
                        float height = displayDataOrCreate.mHeight / rect2.height();
                        int height2 = (int) (rect2.height() * height);
                        width = (int) (rect2.width() * height);
                        if (width > GLHelper.getMaxTextureSize()) {
                            int i9 = (int) (displayDataOrCreate.mHeight / height);
                            int i10 = (int) (displayDataOrCreate.mWidth / height);
                            rect2.set(rect);
                            rect2.left += (rect.width() - i10) / 2;
                            int height3 = rect2.top + ((rect.height() - i9) / 2);
                            rect2.top = height3;
                            rect2.right = rect2.left + i10;
                            rect2.bottom = height3 + i9;
                            rect.set(rect2);
                            rect2.scale(1.0f / options.inSampleSize);
                        }
                        int height4 = (int) (rect2.height() * height);
                        int width3 = (int) (rect2.width() * height);
                        String str2 = TAG;
                        Slog.v(str2, "Decode parameters:");
                        Slog.v(str2, "  cropHint=" + rect + ", estimateCrop=" + rect2);
                        Slog.v(str2, "  down sampling=" + options.inSampleSize + ", hRatio=" + height);
                        Slog.v(str2, "  dest=" + width + "x" + height2);
                        Slog.v(str2, "  safe=" + width3 + "x" + height4);
                        StringBuilder sb = new StringBuilder();
                        sb.append("  maxTextureSize=");
                        sb.append(GLHelper.getMaxTextureSize());
                        Slog.v(str2, sb.toString());
                        String str3 = !wallpaperData.wallpaperFile.getName().equals("wallpaper_orig") ? "decode_record" : "decode_lock_record";
                        File recordFile = this.mWallpaperManagerServiceExt.getRecordFile(wallpaperData, str3, new File(WallpaperUtils.getWallpaperDir(wallpaperData.userId), str3));
                        recordFile.createNewFile();
                        Slog.v(str2, "record path =" + recordFile.getPath() + ", record name =" + recordFile.getName());
                        decodeBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(wallpaperData.wallpaperFile), new ImageDecoder.OnHeaderDecodedListener() { // from class: com.android.server.wallpaper.WallpaperCropper$$ExternalSyntheticLambda0
                            @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                            public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                                WallpaperCropper.this.lambda$generateCropInternal$0(i, options, rect2, imageDecoder, imageInfo, source);
                            }
                        });
                        recordFile.delete();
                        if (decodeBitmap != null) {
                            Slog.e(str2, "Could not decode new wallpaper");
                            bufferedOutputStream = null;
                            z4 = false;
                            fileOutputStream3 = null;
                        } else {
                            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(decodeBitmap, width3, height4, true);
                            FileOutputStream fileOutputStream4 = new FileOutputStream(wallpaperData.cropFile);
                            try {
                                bufferedOutputStream = new BufferedOutputStream(fileOutputStream4, 32768);
                                try {
                                    createScaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
                                    bufferedOutputStream.flush();
                                    fileOutputStream3 = fileOutputStream4;
                                } catch (Exception unused) {
                                    fileOutputStream2 = fileOutputStream4;
                                    IoUtils.closeQuietly(bufferedOutputStream);
                                    IoUtils.closeQuietly(fileOutputStream2);
                                    z = false;
                                    if (!z) {
                                    }
                                    if (wallpaperData.cropFile.exists()) {
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    fileOutputStream = fileOutputStream4;
                                    IoUtils.closeQuietly(bufferedOutputStream);
                                    IoUtils.closeQuietly(fileOutputStream);
                                    throw th;
                                }
                            } catch (Exception unused2) {
                                fileOutputStream2 = fileOutputStream4;
                                bufferedOutputStream = null;
                            } catch (Throwable th2) {
                                th = th2;
                                fileOutputStream = fileOutputStream4;
                                bufferedOutputStream = null;
                            }
                        }
                        IoUtils.closeQuietly(bufferedOutputStream);
                        IoUtils.closeQuietly(fileOutputStream3);
                        z = z4;
                    }
                    if (!z) {
                        Slog.e(TAG, "Unable to apply new wallpaper");
                        wallpaperData.cropFile.delete();
                    }
                    if (wallpaperData.cropFile.exists()) {
                        return;
                    }
                    SELinux.restorecon(wallpaperData.cropFile.getAbsoluteFile());
                    return;
                }
            }
            z2 = false;
            if (rect.height() <= displayDataOrCreate.mHeight) {
            }
            if (z3) {
            }
            String str4 = TAG;
            Slog.v(str4, "crop: w=" + rect.width() + " h=" + rect.height());
            Slog.v(str4, "dims: w=" + displayDataOrCreate.mWidth + " h=" + displayDataOrCreate.mHeight);
            Slog.v(str4, "meas: w=" + options.outWidth + " h=" + options.outHeight);
            Slog.v(str4, "crop?=" + z2 + " scale?=" + z3);
            if (z2) {
            }
            i = 1;
            while (true) {
                i2 = i * 2;
                if (i2 <= rect.height() / displayDataOrCreate.mHeight) {
                }
                i = i2;
            }
            options.inSampleSize = i;
            options.inJustDecodeBounds = false;
            final Rect rect22 = new Rect(rect);
            rect22.scale(1.0f / options.inSampleSize);
            float height5 = displayDataOrCreate.mHeight / rect22.height();
            int height22 = (int) (rect22.height() * height5);
            width = (int) (rect22.width() * height5);
            if (width > GLHelper.getMaxTextureSize()) {
            }
            int height42 = (int) (rect22.height() * height5);
            int width32 = (int) (rect22.width() * height5);
            String str22 = TAG;
            Slog.v(str22, "Decode parameters:");
            Slog.v(str22, "  cropHint=" + rect + ", estimateCrop=" + rect22);
            Slog.v(str22, "  down sampling=" + options.inSampleSize + ", hRatio=" + height5);
            Slog.v(str22, "  dest=" + width + "x" + height22);
            Slog.v(str22, "  safe=" + width32 + "x" + height42);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  maxTextureSize=");
            sb2.append(GLHelper.getMaxTextureSize());
            Slog.v(str22, sb2.toString());
            if (!wallpaperData.wallpaperFile.getName().equals("wallpaper_orig")) {
            }
            File recordFile2 = this.mWallpaperManagerServiceExt.getRecordFile(wallpaperData, str3, new File(WallpaperUtils.getWallpaperDir(wallpaperData.userId), str3));
            recordFile2.createNewFile();
            Slog.v(str22, "record path =" + recordFile2.getPath() + ", record name =" + recordFile2.getName());
            decodeBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(wallpaperData.wallpaperFile), new ImageDecoder.OnHeaderDecodedListener() { // from class: com.android.server.wallpaper.WallpaperCropper$$ExternalSyntheticLambda0
                @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                    WallpaperCropper.this.lambda$generateCropInternal$0(i, options, rect22, imageDecoder, imageInfo, source);
                }
            });
            recordFile2.delete();
            if (decodeBitmap != null) {
            }
            IoUtils.closeQuietly(bufferedOutputStream);
            IoUtils.closeQuietly(fileOutputStream3);
            z = z4;
            if (!z) {
            }
            if (wallpaperData.cropFile.exists()) {
            }
        }
        z = false;
        if (!z) {
        }
        if (wallpaperData.cropFile.exists()) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$generateCropInternal$0(int i, BitmapFactory.Options options, Rect rect, ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setTargetSampleSize(i);
        if (!this.mWallpaperManagerServiceExt.setDecoderSampleSize(imageDecoder, i, options)) {
            imageDecoder.setTargetSampleSize(i);
        }
        imageDecoder.setCrop(rect);
    }
}
