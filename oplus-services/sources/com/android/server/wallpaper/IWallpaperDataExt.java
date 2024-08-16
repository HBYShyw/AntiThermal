package com.android.server.wallpaper;

import java.io.File;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface IWallpaperDataExt {
    public static final int SET_WALLPAPER_FLAG_COPY = 1;

    default void addSetWallpaperFlagLocked(int i) {
    }

    default int getPhysicalDisplayId() {
        return 1;
    }

    default int getSetWallpaperFlagLocked() {
        return 0;
    }

    default File getWallpaperDir(int i, int i2, File file) {
        return file;
    }

    default int getWallpaperType(int i) {
        return i;
    }

    default boolean hasSetWallpaperFlagLocked(int i) {
        return false;
    }

    default boolean resetSetWallpaperFlagLocked(int i) {
        return false;
    }

    default void setPhysicalDisplayId(int i) {
    }
}
