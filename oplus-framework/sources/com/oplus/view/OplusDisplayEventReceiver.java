package com.oplus.view;

import android.os.Looper;
import android.util.Log;
import android.view.DisplayEventReceiver;

/* loaded from: classes.dex */
public final class OplusDisplayEventReceiver {
    public static final int EVENT_REGISTRATION_BRIGHTNESS_SCALE_FLAG = 4;
    public static final int EVENT_REGISTRATION_FRAME_RATE_OVERRIDE_FLAG = 2;
    public static final int EVENT_REGISTRATION_MODE_CHANGED_FLAG = 1;
    private static String TAG = "OplusDisplayEventReceiver";
    public static final int VSYNC_SOURCE_APP = 0;
    public static final int VSYNC_SOURCE_SURFACE_FLINGER = 1;
    private IBrightnessScaleListener mBrightnessScaleListener = null;
    private InnerDisplayEventReceiver mDisplayEventReceiver;

    public OplusDisplayEventReceiver(Looper looper) {
        this.mDisplayEventReceiver = null;
        this.mDisplayEventReceiver = new InnerDisplayEventReceiver(looper);
    }

    public OplusDisplayEventReceiver(Looper looper, int vsyncSource, int eventRegistration) {
        this.mDisplayEventReceiver = null;
        this.mDisplayEventReceiver = new InnerDisplayEventReceiver(looper, vsyncSource, eventRegistration);
    }

    public void setBrightnessScaleListener(IBrightnessScaleListener listener) {
        Log.i(TAG, "setBrightnessScaleListener listener " + listener);
        this.mBrightnessScaleListener = listener;
    }

    public void dispose() {
        Log.i(TAG, "dispose");
        this.mDisplayEventReceiver.dispose();
    }

    /* loaded from: classes.dex */
    private class InnerDisplayEventReceiver extends DisplayEventReceiver {
        public InnerDisplayEventReceiver(Looper looper) {
            super(looper);
            Log.i(OplusDisplayEventReceiver.TAG, "InnerDisplayEventReceiver looper = " + looper);
        }

        public InnerDisplayEventReceiver(Looper looper, int vsyncSource, int eventRegistration) {
            super(looper, vsyncSource, eventRegistration);
            Log.i(OplusDisplayEventReceiver.TAG, "InnerDisplayEventReceiver looper = " + looper + ", vsyncSource = " + vsyncSource + ", eventRegistration = " + eventRegistration);
        }

        public void onBrightnessScale(long timestampNanos, long physicalDisplayId, int toggle, float scale, boolean scaleApplyImmediately, int backlightType) {
            super.onBrightnessScale(timestampNanos, physicalDisplayId, toggle, scale, scaleApplyImmediately, backlightType);
            Log.i(OplusDisplayEventReceiver.TAG, "onBrightnessScale listener " + OplusDisplayEventReceiver.this.mBrightnessScaleListener + ", toggle = " + toggle + ", scale = " + scale + ", backlightType = " + backlightType);
            if (OplusDisplayEventReceiver.this.mBrightnessScaleListener != null) {
                OplusDisplayEventReceiver.this.mBrightnessScaleListener.onBrightnessScale(timestampNanos, physicalDisplayId, toggle, scale, scaleApplyImmediately);
            }
        }
    }
}
