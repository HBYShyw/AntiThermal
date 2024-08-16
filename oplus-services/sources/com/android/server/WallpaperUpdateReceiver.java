package com.android.server;

import android.R;
import android.annotation.RequiresPermission;
import android.app.ActivityThread;
import android.app.ContextImpl;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class WallpaperUpdateReceiver extends BroadcastReceiver {
    private static final boolean DEBUG = false;
    private static final String TAG = "WallpaperUpdateReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !"android.intent.action.DEVICE_CUSTOMIZATION_READY".equals(intent.getAction())) {
            return;
        }
        AsyncTask.execute(new Runnable() { // from class: com.android.server.WallpaperUpdateReceiver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WallpaperUpdateReceiver.this.updateWallpaper();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateWallpaper() {
        try {
            ContextImpl systemUiContext = ActivityThread.currentActivityThread().getSystemUiContext();
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(systemUiContext);
            if (isUserSetWallpaper(wallpaperManager, systemUiContext)) {
                Slog.i(TAG, "User has set wallpaper, skip to resetting");
            } else {
                wallpaperManager.setBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8));
                wallpaperManager.setResource(R.drawable.dialog_divider_horizontal_holo_light);
            }
        } catch (Exception e) {
            Slog.w(TAG, "Failed to customize system wallpaper." + e);
        }
    }

    @RequiresPermission("android.permission.READ_WALLPAPER_INTERNAL")
    private boolean isUserSetWallpaper(WallpaperManager wallpaperManager, Context context) {
        WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();
        if (wallpaperInfo == null) {
            return (wallpaperManager.getWallpaperFile(1) == null && wallpaperManager.getWallpaperFile(2) == null) ? false : true;
        }
        return !wallpaperInfo.getComponent().equals(WallpaperManager.getDefaultWallpaperComponent(context));
    }
}
