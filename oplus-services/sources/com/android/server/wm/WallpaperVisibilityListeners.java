package com.android.server.wm;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.SparseArray;
import android.view.IWallpaperVisibilityListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WallpaperVisibilityListeners {
    private final SparseArray<RemoteCallbackList<IWallpaperVisibilityListener>> mDisplayListeners = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerWallpaperVisibilityListener(IWallpaperVisibilityListener iWallpaperVisibilityListener, int i) {
        RemoteCallbackList<IWallpaperVisibilityListener> remoteCallbackList = this.mDisplayListeners.get(i);
        if (remoteCallbackList == null) {
            remoteCallbackList = new RemoteCallbackList<>();
            this.mDisplayListeners.append(i, remoteCallbackList);
        }
        remoteCallbackList.register(iWallpaperVisibilityListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener iWallpaperVisibilityListener, int i) {
        RemoteCallbackList<IWallpaperVisibilityListener> remoteCallbackList = this.mDisplayListeners.get(i);
        if (remoteCallbackList == null) {
            return;
        }
        remoteCallbackList.unregister(iWallpaperVisibilityListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyWallpaperVisibilityChanged(DisplayContent displayContent) {
        int displayId = displayContent.getDisplayId();
        boolean isWallpaperVisible = displayContent.mWallpaperController.isWallpaperVisible();
        RemoteCallbackList<IWallpaperVisibilityListener> remoteCallbackList = this.mDisplayListeners.get(displayId);
        if (remoteCallbackList == null) {
            return;
        }
        int beginBroadcast = remoteCallbackList.beginBroadcast();
        while (beginBroadcast > 0) {
            beginBroadcast--;
            try {
                remoteCallbackList.getBroadcastItem(beginBroadcast).onWallpaperVisibilityChanged(isWallpaperVisible, displayId);
            } catch (RemoteException unused) {
            }
        }
        remoteCallbackList.finishBroadcast();
    }
}
