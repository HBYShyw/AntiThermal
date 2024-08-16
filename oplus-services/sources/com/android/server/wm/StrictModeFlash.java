package com.android.server.wm;

import android.graphics.BLASTBufferQueue;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Surface;
import android.view.SurfaceControl;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class StrictModeFlash {
    private static final String TAG = "WindowManager";
    private static final String TITLE = "StrictModeFlash";
    private final BLASTBufferQueue mBlastBufferQueue;
    private boolean mDrawNeeded;
    private int mLastDH;
    private int mLastDW;
    private final Surface mSurface;
    private final SurfaceControl mSurfaceControl;
    private final int mThickness = 20;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StrictModeFlash(DisplayContent displayContent, SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = null;
        try {
            surfaceControl = displayContent.makeOverlay().setName(TITLE).setBLASTLayer().setFormat(-3).setCallsite(TITLE).build();
            transaction.setLayer(surfaceControl, 1010000);
            transaction.setPosition(surfaceControl, 0.0f, 0.0f);
            transaction.show(surfaceControl);
            InputMonitor.setTrustedOverlayInputInfo(surfaceControl, transaction, displayContent.getDisplayId(), TITLE);
        } catch (Surface.OutOfResourcesException unused) {
        }
        SurfaceControl surfaceControl2 = surfaceControl;
        this.mSurfaceControl = surfaceControl2;
        this.mDrawNeeded = true;
        BLASTBufferQueue bLASTBufferQueue = new BLASTBufferQueue(TITLE, surfaceControl2, 1, 1, 1);
        this.mBlastBufferQueue = bLASTBufferQueue;
        this.mSurface = bLASTBufferQueue.createSurface();
    }

    private void drawIfNeeded() {
        if (this.mDrawNeeded) {
            this.mDrawNeeded = false;
            int i = this.mLastDW;
            int i2 = this.mLastDH;
            this.mBlastBufferQueue.update(this.mSurfaceControl, i, i2, 1);
            Canvas canvas = null;
            try {
                canvas = this.mSurface.lockCanvas(null);
            } catch (Surface.OutOfResourcesException | IllegalArgumentException unused) {
            }
            if (canvas == null) {
                return;
            }
            canvas.save();
            canvas.clipRect(new Rect(0, 0, i, 20));
            canvas.drawColor(-65536);
            canvas.restore();
            canvas.save();
            canvas.clipRect(new Rect(0, 0, 20, i2));
            canvas.drawColor(-65536);
            canvas.restore();
            canvas.save();
            canvas.clipRect(new Rect(i - 20, 0, i, i2));
            canvas.drawColor(-65536);
            canvas.restore();
            canvas.save();
            canvas.clipRect(new Rect(0, i2 - 20, i, i2));
            canvas.drawColor(-65536);
            canvas.restore();
            this.mSurface.unlockCanvasAndPost(canvas);
        }
    }

    public void setVisibility(boolean z, SurfaceControl.Transaction transaction) {
        if (this.mSurfaceControl == null) {
            return;
        }
        drawIfNeeded();
        if (z) {
            transaction.show(this.mSurfaceControl);
        } else {
            transaction.hide(this.mSurfaceControl);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void positionSurface(int i, int i2, SurfaceControl.Transaction transaction) {
        if (this.mLastDW == i && this.mLastDH == i2) {
            return;
        }
        this.mLastDW = i;
        this.mLastDH = i2;
        transaction.setBufferSize(this.mSurfaceControl, i, i2);
        this.mDrawNeeded = true;
    }
}
