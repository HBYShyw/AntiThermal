package com.oplus.animation;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.SurfaceControl;

/* loaded from: classes.dex */
public class SurfaceParamsWrapper {
    private static final int FLAG_ALL = -1;
    private static final int FLAG_ALPHA = 1;
    private static final int FLAG_BACKGROUND_BLUR_RADIUS = 32;
    private static final int FLAG_CORNER_RADIUS = 16;
    private static final int FLAG_LAYER = 8;
    private static final int FLAG_MATRIX = 2;
    private static final int FLAG_RELATIVE_LAYER = 128;
    private static final int FLAG_REPARENT = 512;
    private static final int FLAG_SHADOW_RADIUS = 256;
    private static final int FLAG_VISIBILITY = 64;
    private static final int FLAG_WINDOW_CROP = 4;
    private static final String TAG = "SurfaceParamsWrapper";
    float mAlpha;
    int mBackgroundBlurRadius;
    float mCornerRadius;
    int mFlags;
    int mLayer;
    Matrix mMatrix;
    SurfaceControl mNewParent;
    int mRelativeLayer;
    SurfaceControl mRelativeTo;
    float mShadowRadius;
    SurfaceControl mSurface;
    float[] mTmpValues = new float[9];
    boolean mVisible;
    Rect mWindowCrop;

    public SurfaceParamsWrapper(SurfaceControl leash) {
        this.mSurface = leash;
    }

    public void withAlpha(float alpha) {
        this.mAlpha = alpha;
        this.mFlags |= 1;
    }

    public void withMatrix(Matrix matrix) {
        this.mMatrix = new Matrix(matrix);
        this.mFlags |= 2;
    }

    public void withWindowCrop(Rect windowCrop) {
        this.mWindowCrop = new Rect(windowCrop);
        this.mFlags |= 4;
    }

    public void withLayer(int layer) {
        this.mLayer = layer;
        this.mFlags |= 8;
    }

    public void withRelativeLayerTo(SurfaceControl relativeTo, int relativeLayer) {
        this.mRelativeTo = relativeTo;
        this.mRelativeLayer = relativeLayer;
        this.mFlags |= 128;
    }

    public void withCornerRadius(float radius) {
        this.mCornerRadius = radius;
        this.mFlags |= 16;
    }

    public void withShadowRadius(float radius) {
        this.mShadowRadius = radius;
        this.mFlags |= 256;
    }

    public void withBackgroundBlur(int radius) {
        this.mBackgroundBlurRadius = radius;
        this.mFlags |= 32;
    }

    public void withVisibility(boolean visible) {
        this.mVisible = visible;
        this.mFlags |= 64;
    }

    public void withReparent(SurfaceControl newParent) {
        this.mNewParent = newParent;
        this.mFlags |= 512;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyTo(SurfaceControl.Transaction t) {
        SurfaceControl surfaceControl = this.mSurface;
        if (surfaceControl == null || !surfaceControl.isValid()) {
            return;
        }
        if ((this.mFlags & 2) != 0) {
            t.setMatrix(this.mSurface, this.mMatrix, this.mTmpValues);
        }
        if ((this.mFlags & 4) != 0) {
            t.setWindowCrop(this.mSurface, this.mWindowCrop);
        }
        if ((this.mFlags & 1) != 0) {
            t.setAlpha(this.mSurface, this.mAlpha);
        }
        if ((this.mFlags & 8) != 0) {
            t.setLayer(this.mSurface, this.mLayer);
        }
        if ((this.mFlags & 16) != 0) {
            t.setCornerRadius(this.mSurface, this.mCornerRadius);
        }
        if ((this.mFlags & 32) != 0) {
            t.setBackgroundBlurRadius(this.mSurface, this.mBackgroundBlurRadius);
        }
        if ((this.mFlags & 64) != 0) {
            if (this.mVisible) {
                t.show(this.mSurface);
            } else {
                t.hide(this.mSurface);
            }
        }
        if ((this.mFlags & 128) != 0) {
            t.setRelativeLayer(this.mSurface, this.mRelativeTo, this.mRelativeLayer);
        }
        if ((this.mFlags & 256) != 0) {
            t.setShadowRadius(this.mSurface, this.mShadowRadius);
        }
        if ((this.mFlags & 512) != 0) {
            t.reparent(this.mSurface, this.mNewParent);
        }
    }
}
