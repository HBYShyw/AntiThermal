package android.service.wallpaper;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.service.wallpaper.WallpaperService;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.IWindowSession;
import android.view.SurfaceControl;
import android.view.WindowManager;
import com.android.internal.view.BaseIWindow;
import com.oplus.widget.OplusMaxLinearLayout;

/* loaded from: classes.dex */
public class EngineExtImpl implements IEngineExt {
    private static final int FINISH_DRAWING_DELAY_IN_MILLIONS = 500;
    private static final float FLOAT_ROUND_NUM = 0.5f;
    private static final int HIDE_SURFACE_DELAY_IN_MILLIONS = 1500;
    private static final String TAG = "EngineExt";
    private static final String TIME_VIDEO_WALLPAPER = "com.android.wallpaper.livepicker.service.video.time.TimeVideoWallpaperService";
    private static final String WALLPAPER_CANCEL_TIMEOUT = "wallpaper.cancel.timeout";
    private static final String WALLPAPER_HIDE_WALLPAPER = "wallpaper.hide.surface";
    private static final String WALLPAPER_SHOW_TIMEOUT = "wallpaper.show.timeout";
    private static final String WALLPAPER_SHOW_WALLPAPER = "wallpaper.show.surface";
    private final WallpaperService.Engine mBase;
    private WallpaperService mWallpaperService;
    private final Runnable mShowSurfaceTask = new Runnable() { // from class: android.service.wallpaper.EngineExtImpl.1
        @Override // java.lang.Runnable
        public void run() {
            if (EngineExtImpl.this.mBase.mBbqSurfaceControl != null && EngineExtImpl.this.mBase.mBbqSurfaceControl.isValid()) {
                Log.d(EngineExtImpl.TAG, "mShowSurfaceTask show for time out");
                new SurfaceControl.Transaction().show(EngineExtImpl.this.mBase.mBbqSurfaceControl).apply();
            }
        }
    };
    private boolean mIsSupportReportFinishDrawing = false;
    private boolean mReportedFinishDrawing = false;

    public EngineExtImpl(Object base) {
        this.mBase = (WallpaperService.Engine) base;
    }

    public boolean reportEngineShown(WallpaperService.IWallpaperEngineWrapper engineWrapper, boolean waitForEngineShown) {
        if (OplusWallpaperLogUtils.DEBUG) {
            Log.d(TAG, "reportEngineShown isSupportReportFinishDrawing=" + this.mIsSupportReportFinishDrawing + " isFromSwitchingUser=" + isFromSwitchingUser() + " waitForEngineShown=" + waitForEngineShown);
        }
        if ((canReportFinishDrawing() || !waitForEngineShown) && isOnMainThread()) {
            Log.i(TAG, "reportEngineShown report immediately");
            engineWrapper.reportShown();
            return true;
        }
        return false;
    }

    private boolean isOnMainThread() {
        Looper curThreadLooper = Looper.myLooper();
        return curThreadLooper != null && curThreadLooper == Looper.getMainLooper();
    }

    public void reportFinishDrawing(Handler handler, IWindowSession windowSession, BaseIWindow window) {
        if (OplusWallpaperLogUtils.DEBUG) {
            Log.d(TAG, "reportFinishDrawing: mReportedFinishDrawing = " + this.mReportedFinishDrawing + " , mIsFromSwitchingUser = " + isFromSwitchingUser());
        }
        if (needDelayFinishDrawing()) {
            if (OplusWallpaperLogUtils.DEBUG) {
                Log.d(TAG, "reportFinishDrawing delay finishDrawing");
            }
            handler.postDelayed(new Runnable() { // from class: android.service.wallpaper.EngineExtImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    EngineExtImpl.this.finishDrawingIfNeed();
                }
            }, 500L);
        } else {
            finishDrawing();
            this.mReportedFinishDrawing = true;
        }
    }

    private boolean needDelayFinishDrawing() {
        boolean z;
        try {
            if (canReportFinishDrawing()) {
                if (!this.mReportedFinishDrawing) {
                    z = true;
                    boolean needDelay = z;
                    return needDelay;
                }
            }
            z = false;
            boolean needDelay2 = z;
            return needDelay2;
        } catch (Exception e) {
            Log.e(TAG, "needDelayFinishDrawing: e = " + e);
            return false;
        }
    }

    private boolean canReportFinishDrawing() {
        return !isPreview() && this.mIsSupportReportFinishDrawing && isFromSwitchingUser();
    }

    private boolean isFromSwitchingUser() {
        WallpaperService wallpaperService = this.mWallpaperService;
        return wallpaperService != null && wallpaperService.mOplusWallpaperServiceExt.isFromSwitchingUser();
    }

    private boolean isPreview() {
        return this.mBase.isPreview();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishDrawingIfNeed() {
        if (!this.mReportedFinishDrawing) {
            try {
                finishDrawing();
                this.mReportedFinishDrawing = true;
                if (OplusWallpaperLogUtils.DEBUG) {
                    Log.d(TAG, "finishDrawingIfNeed: finishDrawing done.");
                }
            } catch (Exception e) {
                Log.e(TAG, "finishDrawingIfNeed: e = " + e);
            }
        }
    }

    private void finishDrawing() {
        if (this.mBase.mSession != null) {
            try {
                this.mBase.mSession.finishDrawing(this.mBase.mWindow, (SurfaceControl.Transaction) null, OplusMaxLinearLayout.INVALID_MAX_VALUE);
            } catch (RemoteException e) {
            }
        } else {
            Log.w(TAG, "Fail to call finishDrawing");
        }
    }

    public void onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
        if (OplusWallpaperLogUtils.DEBUG) {
            Log.d(TAG, "onCommand: action = " + action);
        }
        if ("finishDrawing".equals(action)) {
            finishDrawingIfNeed();
            return;
        }
        if ("supportReportFinishDrawing".equals(action)) {
            this.mIsSupportReportFinishDrawing = true;
            return;
        }
        if (WALLPAPER_HIDE_WALLPAPER.equals(action) && this.mBase.mBbqSurfaceControl != null && this.mBase.mBbqSurfaceControl.isValid()) {
            new SurfaceControl.Transaction().hide(this.mBase.mBbqSurfaceControl).apply();
            return;
        }
        if (WALLPAPER_SHOW_WALLPAPER.equals(action) && this.mBase.mBbqSurfaceControl != null && this.mBase.mBbqSurfaceControl.isValid()) {
            new SurfaceControl.Transaction().show(this.mBase.mBbqSurfaceControl).apply();
            if (Handler.getMain().hasCallbacks(this.mShowSurfaceTask)) {
                Handler.getMain().removeCallbacks(this.mShowSurfaceTask);
                return;
            }
            return;
        }
        if (WALLPAPER_SHOW_TIMEOUT.equals(action) && this.mBase.mBbqSurfaceControl != null && this.mBase.mBbqSurfaceControl.isValid()) {
            Handler.getMain().postDelayed(this.mShowSurfaceTask, 1500L);
        } else if (WALLPAPER_CANCEL_TIMEOUT.equals(action) && Handler.getMain().hasCallbacks(this.mShowSurfaceTask)) {
            Handler.getMain().removeCallbacks(this.mShowSurfaceTask);
        }
    }

    public void setWallpaperService(WallpaperService wallpaperService) {
        this.mWallpaperService = wallpaperService;
    }

    public boolean initLayoutForInvalidMaxBounds(Rect maxBounds, WindowManager.LayoutParams layout, Display display, int myWidth, int myHeight) {
        if (maxBounds == null || layout == null || display == null) {
            Log.e(TAG, "initLayoutForInvalidMaxBounds illegal param maxBounds=" + maxBounds + " layout=" + layout + " display=" + display);
            return false;
        }
        if (maxBounds.height() != 0 || maxBounds.width() != 0) {
            return false;
        }
        Display.Mode mode = display.getMode();
        if (mode == null) {
            Log.e(TAG, "initLayoutForInvalidMaxBounds fail to get display mode");
            return false;
        }
        boolean isLandscape = isLandscape(display);
        Rect tmpBounds = new Rect(0, 0, isLandscape ? mode.getPhysicalHeight() : mode.getPhysicalWidth(), isLandscape ? mode.getPhysicalWidth() : mode.getPhysicalHeight());
        float layoutScale = Math.max(tmpBounds.width() / myWidth, tmpBounds.height() / myHeight);
        layout.width = (int) ((myWidth * layoutScale) + 0.5f);
        layout.height = (int) ((myHeight * layoutScale) + 0.5f);
        layout.flags |= 16384;
        Log.i(TAG, "initLayoutForInvalidMaxBounds bounds=" + tmpBounds + " width=" + layout.width + " height=" + layout.height);
        return true;
    }

    private boolean isLandscape(Display display) {
        if (display == null) {
            return false;
        }
        int rotation = display.getRotation();
        if (rotation != 1 && rotation != 3) {
            return false;
        }
        return true;
    }

    public String getBBQSurfaceControlName() {
        WallpaperService.Engine engine = this.mBase;
        if (engine == null) {
            return "Wallpaper BBQ wrapper";
        }
        String className = engine.getClass().getName();
        if (TextUtils.isEmpty(className) || !className.startsWith(TIME_VIDEO_WALLPAPER)) {
            return "Wallpaper BBQ wrapper";
        }
        return "OplusTimeVideoWallpaperService Wallpaper BBQ wrapper";
    }

    public Configuration getConfiguration(Configuration configuration, Context context, String reason) {
        if (context != null && WallpaperServiceExtImpl.REASON_CONFIG_CHANGE.equals(reason)) {
            return context.getResources().getConfiguration();
        }
        return configuration;
    }
}
