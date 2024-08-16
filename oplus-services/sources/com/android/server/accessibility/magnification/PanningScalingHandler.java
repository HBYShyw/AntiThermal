package com.android.server.accessibility.magnification;

import android.R;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.Slog;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class PanningScalingHandler extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
    private final boolean mBlockScroll;
    private final int mDisplayId;
    private boolean mEnable;
    private float mInitialScaleFactor = -1.0f;
    private final MagnificationDelegate mMagnificationDelegate;
    private final float mMaxScale;
    private final float mMinScale;
    private final ScaleGestureDetector mScaleGestureDetector;
    private boolean mScaling;
    private final float mScalingThreshold;
    private final GestureDetector mScrollGestureDetector;
    private static final String TAG = "PanningScalingHandler";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    interface MagnificationDelegate {
        float getScale(int i);

        boolean processScroll(int i, float f, float f2);

        void setScale(int i, float f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PanningScalingHandler(Context context, float f, float f2, boolean z, MagnificationDelegate magnificationDelegate) {
        this.mDisplayId = context.getDisplayId();
        this.mMaxScale = f;
        this.mMinScale = f2;
        this.mBlockScroll = z;
        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, this, Handler.getMain());
        this.mScaleGestureDetector = scaleGestureDetector;
        this.mScrollGestureDetector = new GestureDetector(context, this, Handler.getMain());
        scaleGestureDetector.setQuickScaleEnabled(false);
        this.mMagnificationDelegate = magnificationDelegate;
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(R.dimen.conversation_icon_size_badged, typedValue, false);
        this.mScalingThreshold = typedValue.getFloat();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnabled(boolean z) {
        clear();
        this.mEnable = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTouchEvent(MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(motionEvent);
        this.mScrollGestureDetector.onTouchEvent(motionEvent);
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.mEnable) {
            return true;
        }
        if (this.mBlockScroll && this.mScaling) {
            return true;
        }
        return this.mMagnificationDelegate.processScroll(this.mDisplayId, f, f2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0057, code lost:
    
        if (r7 < r2) goto L21;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005c  */
    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        boolean z = DEBUG;
        if (z) {
            Slog.i(TAG, "onScale: triggered ");
        }
        if (!this.mScaling) {
            if (this.mInitialScaleFactor < 0.0f) {
                this.mInitialScaleFactor = scaleGestureDetector.getScaleFactor();
                return false;
            }
            boolean z2 = Math.abs(scaleGestureDetector.getScaleFactor() - this.mInitialScaleFactor) > this.mScalingThreshold;
            this.mScaling = z2;
            return z2;
        }
        float scale = this.mMagnificationDelegate.getScale(this.mDisplayId);
        float scaleFactor = scaleGestureDetector.getScaleFactor() * scale;
        float f = this.mMaxScale;
        if (scaleFactor <= f || scaleFactor <= scale) {
            f = this.mMinScale;
            if (scaleFactor < f) {
            }
            if (z) {
                Slog.i(TAG, "Scaled content to: " + scaleFactor + "x");
            }
            this.mMagnificationDelegate.setScale(this.mDisplayId, scaleFactor);
            return true;
        }
        scaleFactor = f;
        if (z) {
        }
        this.mMagnificationDelegate.setScale(this.mDisplayId, scaleFactor);
        return true;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return this.mEnable;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.mInitialScaleFactor = -1.0f;
        this.mScaling = false;
    }

    public String toString() {
        return "PanningScalingHandler{mInitialScaleFactor=" + this.mInitialScaleFactor + ", mScaling=" + this.mScaling + '}';
    }
}
