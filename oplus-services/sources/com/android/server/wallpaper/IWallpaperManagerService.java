package com.android.server.wallpaper;

import android.app.IWallpaperManager;
import android.os.IBinder;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
interface IWallpaperManagerService extends IWallpaperManager, IBinder {
    void onBootPhase(int i);

    void onUnlockUser(int i);
}
