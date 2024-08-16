package android.view;

import android.content.Context;
import android.window.SurfaceSyncGroup;

/* loaded from: classes.dex */
public class SurfaceViewExtImpl implements ISurfaceViewExt {
    private boolean mShowSurfaceCornerRadius = true;
    private SurfaceView mSurfaceView;

    public SurfaceViewExtImpl(Object base) {
        if (base instanceof SurfaceView) {
            this.mSurfaceView = (SurfaceView) base;
        }
    }

    public void setShowSurfaceCornerRadius(boolean show) {
        this.mShowSurfaceCornerRadius = show;
    }

    public boolean isShowSurfaceCornerRadius() {
        return this.mShowSurfaceCornerRadius;
    }

    public void onDrawFinishedWithSync(SurfaceSyncGroup syncGroup) {
        SurfaceView surfaceView = this.mSurfaceView;
        if (surfaceView == null) {
            return;
        }
        surfaceView.getWrapper().onDrawFinished(syncGroup);
    }

    public boolean shouldDelaySync() {
        Context context;
        SurfaceView surfaceView = this.mSurfaceView;
        if (surfaceView == null || (context = surfaceView.getContext()) == null) {
            return false;
        }
        String packageName = context.getPackageName();
        return "com.coloros.weather2".equals(packageName) || "net.oneplus.weather".equals(packageName);
    }

    public boolean shouldApplyDirectly() {
        Context context;
        SurfaceView surfaceView = this.mSurfaceView;
        if (surfaceView == null || (context = surfaceView.getContext()) == null) {
            return false;
        }
        String packageName = context.getPackageName();
        return "com.amazon.avod.thirdpartyclient".equals(packageName);
    }
}
