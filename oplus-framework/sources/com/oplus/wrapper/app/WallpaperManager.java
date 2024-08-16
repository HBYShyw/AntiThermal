package com.oplus.wrapper.app;

import android.content.ComponentName;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import java.io.InputStream;

/* loaded from: classes.dex */
public class WallpaperManager {
    private final android.app.WallpaperManager mWallpaperManager;

    public WallpaperManager(android.app.WallpaperManager wallpaperManager) {
        this.mWallpaperManager = wallpaperManager;
    }

    public static InputStream openDefaultWallpaper(Context context, int which) {
        return android.app.WallpaperManager.openDefaultWallpaper(context, which);
    }

    public ParcelFileDescriptor getWallpaperFile(int which, int userId) {
        return this.mWallpaperManager.getWallpaperFile(which, userId);
    }

    public boolean setWallpaperComponent(ComponentName name) {
        return this.mWallpaperManager.setWallpaperComponent(name);
    }
}
