package com.oplus.wallpaper;

import android.service.wallpaper.WallpaperService;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class OplusWallpaperServiceEngine {
    private OplusWallpaperServiceEngine() {
    }

    public static SurfaceControl getBBQSurfaceControl(WallpaperService.Engine engine) {
        if (engine == null) {
            return null;
        }
        return engine.getWrapper().getBBQSurfaceControl();
    }
}
