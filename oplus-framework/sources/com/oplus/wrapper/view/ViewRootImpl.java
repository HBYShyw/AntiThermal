package com.oplus.wrapper.view;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.ViewRootImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class ViewRootImpl {
    private static final String TAG = "ViewRootImpl";
    private final Map<SurfaceChangedCallback, ViewRootImpl.SurfaceChangedCallback> mCallBackMap = new ConcurrentHashMap();
    private final android.view.ViewRootImpl mViewRootImpl;

    /* loaded from: classes.dex */
    public interface SurfaceChangedCallback {
        void surfaceCreated(SurfaceControl.Transaction transaction);

        void surfaceDestroyed();

        void surfaceReplaced(SurfaceControl.Transaction transaction);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewRootImpl(android.view.ViewRootImpl viewRoot) {
        this.mViewRootImpl = viewRoot;
    }

    public void addSurfaceChangedCallback(final SurfaceChangedCallback changedCallback) {
        ViewRootImpl.SurfaceChangedCallback surfaceChangedCallback = null;
        if (changedCallback != null) {
            surfaceChangedCallback = new ViewRootImpl.SurfaceChangedCallback() { // from class: com.oplus.wrapper.view.ViewRootImpl.1
                public void surfaceCreated(SurfaceControl.Transaction transaction) {
                    changedCallback.surfaceCreated(transaction);
                }

                public void surfaceReplaced(SurfaceControl.Transaction transaction) {
                    changedCallback.surfaceReplaced(transaction);
                }

                public void surfaceDestroyed() {
                    changedCallback.surfaceDestroyed();
                }
            };
            this.mCallBackMap.put(changedCallback, surfaceChangedCallback);
        }
        this.mViewRootImpl.addSurfaceChangedCallback(surfaceChangedCallback);
    }

    public void removeSurfaceChangedCallback(SurfaceChangedCallback changedCallback) {
        if (changedCallback == null) {
            Log.w(TAG, "removeSurfaceChangedCallback SurfaceChangedCallback is null");
            return;
        }
        ViewRootImpl.SurfaceChangedCallback surfaceChangedCallback = this.mCallBackMap.get(changedCallback);
        if (surfaceChangedCallback == null) {
            return;
        }
        this.mViewRootImpl.removeSurfaceChangedCallback(surfaceChangedCallback);
        this.mCallBackMap.remove(changedCallback);
    }

    public Drawable createBackgroundBlurDrawable() {
        return this.mViewRootImpl.createBackgroundBlurDrawable();
    }

    public android.view.SurfaceControl getSurfaceControl() {
        return this.mViewRootImpl.getSurfaceControl();
    }
}
