package com.android.server.wm;

import android.R;
import android.content.Context;
import android.graphics.BLASTBufferQueue;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceControl;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class EmulatorDisplayOverlay {
    private static final String TAG = "WindowManager";
    private static final String TITLE = "EmulatorDisplayOverlay";
    private final BLASTBufferQueue mBlastBufferQueue;
    private boolean mDrawNeeded;
    private int mLastDH;
    private int mLastDW;
    private final Drawable mOverlay;
    private int mRotation;
    private Point mScreenSize;
    private final Surface mSurface;
    private final SurfaceControl mSurfaceControl;
    private boolean mVisible;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EmulatorDisplayOverlay(Context context, DisplayContent displayContent, int i, SurfaceControl.Transaction transaction) {
        Display display = displayContent.getDisplay();
        Point point = new Point();
        this.mScreenSize = point;
        display.getSize(point);
        SurfaceControl surfaceControl = null;
        try {
            surfaceControl = displayContent.makeOverlay().setName(TITLE).setBLASTLayer().setFormat(-3).setCallsite(TITLE).build();
            transaction.setLayer(surfaceControl, i);
            transaction.setPosition(surfaceControl, 0.0f, 0.0f);
            transaction.show(surfaceControl);
            InputMonitor.setTrustedOverlayInputInfo(surfaceControl, transaction, displayContent.getDisplayId(), TITLE);
        } catch (Surface.OutOfResourcesException unused) {
        }
        SurfaceControl surfaceControl2 = surfaceControl;
        this.mSurfaceControl = surfaceControl2;
        this.mDrawNeeded = true;
        this.mOverlay = context.getDrawable(R.drawable.expander_group_holo_dark);
        Point point2 = this.mScreenSize;
        BLASTBufferQueue bLASTBufferQueue = new BLASTBufferQueue(TITLE, surfaceControl2, point2.x, point2.y, 1);
        this.mBlastBufferQueue = bLASTBufferQueue;
        this.mSurface = bLASTBufferQueue.createSurface();
    }

    private void drawIfNeeded(SurfaceControl.Transaction transaction) {
        if (this.mDrawNeeded && this.mVisible) {
            this.mDrawNeeded = false;
            Canvas canvas = null;
            try {
                canvas = this.mSurface.lockCanvas(null);
            } catch (Surface.OutOfResourcesException | IllegalArgumentException unused) {
            }
            if (canvas == null) {
                return;
            }
            canvas.drawColor(0, PorterDuff.Mode.SRC);
            transaction.setPosition(this.mSurfaceControl, 0.0f, 0.0f);
            Point point = this.mScreenSize;
            int max = Math.max(point.x, point.y);
            this.mOverlay.setBounds(0, 0, max, max);
            this.mOverlay.draw(canvas);
            this.mSurface.unlockCanvasAndPost(canvas);
        }
    }

    public void setVisibility(boolean z, SurfaceControl.Transaction transaction) {
        if (this.mSurfaceControl == null) {
            return;
        }
        this.mVisible = z;
        drawIfNeeded(transaction);
        if (z) {
            transaction.show(this.mSurfaceControl);
        } else {
            transaction.hide(this.mSurfaceControl);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void positionSurface(int i, int i2, int i3, SurfaceControl.Transaction transaction) {
        if (this.mLastDW == i && this.mLastDH == i2 && this.mRotation == i3) {
            return;
        }
        this.mLastDW = i;
        this.mLastDH = i2;
        this.mDrawNeeded = true;
        this.mRotation = i3;
        drawIfNeeded(transaction);
    }
}
