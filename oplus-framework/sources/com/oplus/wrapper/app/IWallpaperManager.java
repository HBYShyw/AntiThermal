package com.oplus.wrapper.app;

import android.app.ILocalWallpaperColorConsumer;
import android.app.IWallpaperManager;
import android.app.IWallpaperManagerCallback;
import android.app.WallpaperColors;
import android.app.WallpaperInfo;
import android.content.ComponentName;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IWallpaperManager {
    int getHeightHint(int i) throws RemoteException;

    int getWidthHint(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub implements IInterface, IWallpaperManager {
        private final android.app.IWallpaperManager mTarget = new IWallpaperManager.Stub() { // from class: com.oplus.wrapper.app.IWallpaperManager.Stub.1
            public ParcelFileDescriptor setWallpaper(String s, String s1, Rect rect, boolean b, Bundle bundle, int i, IWallpaperManagerCallback iWallpaperManagerCallback, int i1) throws RemoteException {
                return null;
            }

            public void setWallpaperComponentChecked(ComponentName componentName, String s, int i, int userId) throws RemoteException {
            }

            public void setWallpaperComponent(ComponentName componentName) throws RemoteException {
            }

            public boolean isMultiCropEnabled() throws RemoteException {
                return false;
            }

            public boolean isStaticWallpaper(int which) throws RemoteException {
                return false;
            }

            public ParcelFileDescriptor getWallpaperInfoFile(int userId) throws RemoteException {
                return null;
            }

            public ParcelFileDescriptor getWallpaper(String s, IWallpaperManagerCallback iWallpaperManagerCallback, int i, Bundle bundle, int i1) throws RemoteException {
                return null;
            }

            public ParcelFileDescriptor getWallpaperWithFeature(String callingPkg, String callingFeatureId, IWallpaperManagerCallback cb, int which, Bundle outParams, int userId, boolean getCropped) throws RemoteException {
                return null;
            }

            public int getWallpaperIdForUser(int i, int i1) throws RemoteException {
                return 0;
            }

            public WallpaperInfo getWallpaperInfo(int i) throws RemoteException {
                return null;
            }

            public WallpaperInfo getWallpaperInfoWithFlags(int which, int userId) {
                return null;
            }

            public void clearWallpaper(String s, int i, int i1) throws RemoteException {
            }

            public boolean hasNamedWallpaper(String s) throws RemoteException {
                return false;
            }

            public void setDimensionHints(int i, int i1, String s, int i2) throws RemoteException {
            }

            public int getWidthHint(int displayId) throws RemoteException {
                return Stub.this.getWidthHint(displayId);
            }

            public int getHeightHint(int displayId) throws RemoteException {
                return Stub.this.getHeightHint(displayId);
            }

            public void setDisplayPadding(Rect rect, String s, int i) throws RemoteException {
            }

            public String getName() throws RemoteException {
                return null;
            }

            public void settingsRestored() throws RemoteException {
            }

            public boolean isWallpaperSupported(String s) throws RemoteException {
                return false;
            }

            public boolean isSetWallpaperAllowed(String s) throws RemoteException {
                return false;
            }

            public boolean isWallpaperBackupEligible(int i, int i1) throws RemoteException {
                return false;
            }

            public boolean setLockWallpaperCallback(IWallpaperManagerCallback iWallpaperManagerCallback) throws RemoteException {
                return false;
            }

            public WallpaperColors getWallpaperColors(int i, int i1, int i2) throws RemoteException {
                return null;
            }

            public void removeOnLocalColorsChangedListener(ILocalWallpaperColorConsumer iLocalWallpaperColorConsumer, List<RectF> list, int i, int i1, int i2) throws RemoteException {
            }

            public void addOnLocalColorsChangedListener(ILocalWallpaperColorConsumer iLocalWallpaperColorConsumer, List<RectF> list, int i, int i1, int i2) throws RemoteException {
            }

            public void registerWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i, int i1) throws RemoteException {
            }

            public void unregisterWallpaperColorsCallback(IWallpaperManagerCallback iWallpaperManagerCallback, int i, int i1) throws RemoteException {
            }

            public void setInAmbientMode(boolean b, long l) throws RemoteException {
            }

            public void notifyWakingUp(int i, int i1, Bundle bundle) throws RemoteException {
            }

            public void notifyGoingToSleep(int i, int i1, Bundle bundle) throws RemoteException {
            }

            public void setWallpaperDimAmount(float v) throws RemoteException {
            }

            public float getWallpaperDimAmount() throws RemoteException {
                return 0.0f;
            }

            public boolean lockScreenWallpaperExists() throws RemoteException {
                return false;
            }

            public boolean isLockscreenLiveWallpaperEnabled() throws RemoteException {
                return false;
            }
        };

        public static IWallpaperManager asInterface(IBinder obj) {
            return new Proxy(IWallpaperManager.Stub.asInterface(obj));
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this.mTarget.asBinder();
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IWallpaperManager {
            private final android.app.IWallpaperManager mTarget;

            public Proxy(android.app.IWallpaperManager target) {
                this.mTarget = target;
            }

            @Override // com.oplus.wrapper.app.IWallpaperManager
            public int getHeightHint(int displayId) throws RemoteException {
                return this.mTarget.getHeightHint(displayId);
            }

            @Override // com.oplus.wrapper.app.IWallpaperManager
            public int getWidthHint(int displayId) throws RemoteException {
                return this.mTarget.getWidthHint(displayId);
            }
        }
    }
}
