package android.app;

import android.os.RemoteCallbackList;
import com.oplus.wallpaper.IOplusWallpaperObserver;
import com.oplus.wallpaper.IWallpaperCallbackExt;

/* loaded from: classes.dex */
public class OplusWallpaperCallbackExtImpl implements IWallpaperCallbackExt {
    private final RemoteCallbackList<IOplusWallpaperObserver> mIOplusWallpaperObservers = new RemoteCallbackList<>();

    public boolean registerWallpaperObserver(IOplusWallpaperObserver observer) {
        return this.mIOplusWallpaperObservers.register(observer);
    }

    public boolean unregisterWallpaperObserver(IOplusWallpaperObserver observer) {
        return this.mIOplusWallpaperObservers.unregister(observer);
    }

    public RemoteCallbackList<IOplusWallpaperObserver> getOplusWallpaperObservers() {
        return this.mIOplusWallpaperObservers;
    }

    public boolean isCallbackExtImpl() {
        return true;
    }
}
