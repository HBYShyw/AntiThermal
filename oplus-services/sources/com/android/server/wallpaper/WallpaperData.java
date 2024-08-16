package com.android.server.wallpaper;

import android.app.IWallpaperManagerCallback;
import android.app.WallpaperColors;
import android.content.ComponentName;
import android.graphics.Rect;
import android.os.RemoteCallbackList;
import android.util.SparseArray;
import com.android.server.wallpaper.WallpaperManagerService;
import java.io.File;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class WallpaperData {
    boolean allowBackup;
    RemoteCallbackList<IWallpaperManagerCallback> callbacks;
    WallpaperManagerService.WallpaperConnection connection;
    final File cropFile;
    final Rect cropHint;
    public boolean fromForegroundApp;
    boolean imageWallpaperPending;
    long lastDiedTime;
    boolean mIsColorExtractedFromDim;
    boolean mSystemWasBoth;
    SparseArray<Float> mUidToDimAmount;
    public IWallpaperDataExt mWallpaperDataExt;
    float mWallpaperDimAmount;
    int mWhich;
    String name;
    ComponentName nextWallpaperComponent;
    WallpaperColors primaryColors;
    IWallpaperManagerCallback setComplete;
    int userId;
    ComponentName wallpaperComponent;
    final File wallpaperFile;
    int wallpaperId;
    WallpaperManagerService.WallpaperObserver wallpaperObserver;
    boolean wallpaperUpdating;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WallpaperData(int i, int i2) {
        this.name = "";
        this.mWallpaperDimAmount = 0.0f;
        this.mUidToDimAmount = new SparseArray<>();
        this.callbacks = new RemoteCallbackList<>();
        this.cropHint = new Rect(0, 0, 0, 0);
        IWallpaperDataExt iWallpaperDataExt = (IWallpaperDataExt) ExtLoader.type(IWallpaperDataExt.class).base(this).create();
        this.mWallpaperDataExt = iWallpaperDataExt;
        this.userId = i;
        this.mWhich = iWallpaperDataExt.getWallpaperType(i2);
        File wallpaperDir = this.mWallpaperDataExt.getWallpaperDir(i, i2, WallpaperUtils.getWallpaperDir(i));
        int wallpaperType = this.mWallpaperDataExt.getWallpaperType(i2);
        String str = wallpaperType == 2 ? "wallpaper_lock_orig" : "wallpaper_orig";
        String str2 = wallpaperType == 2 ? "wallpaper_lock" : "wallpaper";
        this.wallpaperFile = new File(wallpaperDir, str);
        this.cropFile = new File(wallpaperDir, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WallpaperData(WallpaperData wallpaperData) {
        this.name = "";
        this.mWallpaperDimAmount = 0.0f;
        this.mUidToDimAmount = new SparseArray<>();
        this.callbacks = new RemoteCallbackList<>();
        Rect rect = new Rect(0, 0, 0, 0);
        this.cropHint = rect;
        this.mWallpaperDataExt = (IWallpaperDataExt) ExtLoader.type(IWallpaperDataExt.class).base(this).create();
        this.userId = wallpaperData.userId;
        this.wallpaperFile = wallpaperData.wallpaperFile;
        this.cropFile = wallpaperData.cropFile;
        this.wallpaperComponent = wallpaperData.wallpaperComponent;
        this.mWhich = wallpaperData.mWhich;
        this.wallpaperId = wallpaperData.wallpaperId;
        rect.set(wallpaperData.cropHint);
        this.allowBackup = wallpaperData.allowBackup;
        this.primaryColors = wallpaperData.primaryColors;
        this.mWallpaperDimAmount = wallpaperData.mWallpaperDimAmount;
        WallpaperManagerService.WallpaperConnection wallpaperConnection = wallpaperData.connection;
        this.connection = wallpaperConnection;
        if (wallpaperConnection != null) {
            wallpaperConnection.mWallpaper = this;
        }
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder(defaultString(this));
        sb.append(", id: ");
        sb.append(this.wallpaperId);
        sb.append(", which: ");
        sb.append(this.mWhich);
        sb.append(", file mod: ");
        File file = this.wallpaperFile;
        sb.append(file != null ? Long.valueOf(file.lastModified()) : "null");
        if (this.connection == null) {
            sb.append(", no connection");
        } else {
            sb.append(", info: ");
            sb.append(this.connection.mInfo);
            sb.append(", engine(s):");
            this.connection.forEachDisplayConnector(new Consumer() { // from class: com.android.server.wallpaper.WallpaperData$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    WallpaperData.lambda$toString$0(sb, (WallpaperManagerService.DisplayConnector) obj);
                }
            });
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$toString$0(StringBuilder sb, WallpaperManagerService.DisplayConnector displayConnector) {
        if (displayConnector.mEngine != null) {
            sb.append(" ");
            sb.append(defaultString(displayConnector.mEngine));
        } else {
            sb.append(" null");
        }
    }

    private static String defaultString(Object obj) {
        return obj.getClass().getSimpleName() + "@" + Integer.toHexString(obj.hashCode());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cropExists() {
        return this.cropFile.exists();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sourceExists() {
        return this.wallpaperFile.exists();
    }
}
