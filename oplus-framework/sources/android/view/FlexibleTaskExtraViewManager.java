package android.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Region;
import android.os.IBinder;
import android.util.Slog;
import android.view.SurfaceControl;
import android.view.WindowManager;

/* loaded from: classes.dex */
public class FlexibleTaskExtraViewManager extends WindowlessWindowManager {
    private static final int EXTRA_VIEW_LAYER = 2147483645;
    private static final String TAG = FlexibleTaskExtraViewManager.class.getSimpleName();
    private final Context mContext;
    private SurfaceControl mExtraViewLeash;
    private SurfaceControl mHostLeash;
    private final SurfaceSession mSurfaceSession;
    private SurfaceControl.Transaction mTransaction;
    private SurfaceControlViewHost mViewHost;

    public FlexibleTaskExtraViewManager(Context context, Configuration configuration, SurfaceSession surfaceSession) {
        super(configuration, (SurfaceControl) null, (IBinder) null);
        this.mSurfaceSession = surfaceSession;
        this.mContext = context;
        this.mTransaction = new SurfaceControl.Transaction();
    }

    protected SurfaceControl getParentSurface(IWindow window, WindowManager.LayoutParams attrs) {
        Slog.d(TAG, "attachToParentSurface");
        SurfaceControl.Builder builder = new SurfaceControl.Builder(this.mSurfaceSession).setContainerLayer().setName("FlexibleTaskExtraView").setHidden(true).setParent(this.mHostLeash).setCallsite("FlexibleTaskExtraViewManager#attachToParentSurface");
        SurfaceControl build = builder.build();
        this.mExtraViewLeash = build;
        return build;
    }

    public void setView(Context context, SurfaceControl rootLeash, View layout, WindowManager.LayoutParams lp, Region region) {
        if (layout == null || lp == null) {
            Slog.w(TAG, "no view layout or layout parameters, return");
            return;
        }
        if (this.mExtraViewLeash != null && this.mViewHost != null) {
            Slog.w(TAG, "the view already existed, return");
            return;
        }
        Context context2 = context.createWindowContext(context.getDisplay(), 2038, null);
        this.mHostLeash = rootLeash;
        if (this.mViewHost == null) {
            this.mViewHost = new SurfaceControlViewHost(context2, context2.getDisplay(), this, "FlexibleTaskExtraViewLayer");
        }
        Slog.d(TAG, "extra view layout params, width = " + lp.width + ", height = " + lp.height + ", position.x = " + lp.x + ", position.y = " + lp.y + ", region: " + region);
        this.mViewHost.setView(layout, lp);
        if (region != null && !region.isEmpty()) {
            setTouchRegion(this.mViewHost.getWindowToken().asBinder(), region);
        }
        SurfaceControl surfaceControl = this.mExtraViewLeash;
        if (surfaceControl != null) {
            this.mTransaction.setLayer(surfaceControl, EXTRA_VIEW_LAYER).setVisibility(this.mExtraViewLeash, true).apply();
        }
    }

    public void release() {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        SurfaceControl surfaceControl = this.mExtraViewLeash;
        if (surfaceControl != null) {
            this.mTransaction.remove(surfaceControl).apply();
            this.mExtraViewLeash = null;
        }
        this.mHostLeash = null;
    }

    public void setTouchRegion(Region region) {
        SurfaceControlViewHost surfaceControlViewHost;
        if (region != null && !region.isEmpty() && (surfaceControlViewHost = this.mViewHost) != null) {
            setTouchRegion(surfaceControlViewHost.getWindowToken().asBinder(), region);
        }
    }

    public void relayout(WindowManager.LayoutParams attrs) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.relayout(attrs);
        }
    }
}
