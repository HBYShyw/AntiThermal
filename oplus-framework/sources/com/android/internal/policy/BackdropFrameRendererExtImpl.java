package com.android.internal.policy;

import android.common.OplusFeatureCache;
import android.graphics.RecordingCanvas;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.util.Slog;
import com.oplus.darkmode.IOplusDarkModeManager;

/* loaded from: classes.dex */
public class BackdropFrameRendererExtImpl implements IBackdropFrameRendererExt {
    private static final String TAG = "BackdropFrameRendererExtImpl";
    private boolean mDebug = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public BackdropFrameRendererExtImpl(Object base) {
    }

    public void drawDarkModeBackground(DecorView decorView, Drawable drawable, int lastCaptionHeight, int top, int left, int height, int width, RecordingCanvas canvas, RenderNode renderNode) {
        if (this.mDebug) {
            Slog.d(TAG, "drawDarkModeBackground in impl");
        }
        if (((IOplusDarkModeManager) OplusFeatureCache.getOrCreate(IOplusDarkModeManager.DEFAULT, new Object[0])).darkenSplitScreenDrawable(decorView, drawable, 0, lastCaptionHeight, left + width, top + height, canvas, renderNode)) {
            drawable.setBounds(0, lastCaptionHeight, left + width, top + height);
            drawable.draw(canvas);
        }
    }

    public void checkSystemBarForceDark(DecorView decorView, RenderNode systemBarBackgroundNode) {
        if (decorView != null && (decorView.getResources().getConfiguration().uiMode & 48) == 32) {
            systemBarBackgroundNode.setForceDarkAllowed(false);
        }
    }
}
