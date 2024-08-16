package com.android.server.wallpaper;

import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WallpaperUtils {
    static final String RECORD_FILE = "decode_record";
    static final String RECORD_LOCK_FILE = "decode_lock_record";
    private static int sWallpaperId;
    static final String WALLPAPER = "wallpaper_orig";
    static final String WALLPAPER_CROP = "wallpaper";
    static final String WALLPAPER_LOCK_ORIG = "wallpaper_lock_orig";
    static final String WALLPAPER_LOCK_CROP = "wallpaper_lock";
    static final String WALLPAPER_INFO = "wallpaper_info.xml";
    private static final String[] sPerUserFiles = {WALLPAPER, WALLPAPER_CROP, WALLPAPER_LOCK_ORIG, WALLPAPER_LOCK_CROP, WALLPAPER_INFO};

    WallpaperUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static File getWallpaperDir(int i) {
        return Environment.getUserSystemDirectory(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int makeWallpaperIdLocked() {
        int i;
        do {
            i = sWallpaperId + 1;
            sWallpaperId = i;
        } while (i == 0);
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getCurrentWallpaperId() {
        return sWallpaperId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setCurrentWallpaperId(int i) {
        sWallpaperId = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<File> getWallpaperFiles(int i) {
        File wallpaperDir = getWallpaperDir(i);
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (true) {
            String[] strArr = sPerUserFiles;
            if (i2 >= strArr.length) {
                return arrayList;
            }
            arrayList.add(new File(wallpaperDir, strArr[i2]));
            i2++;
        }
    }
}
