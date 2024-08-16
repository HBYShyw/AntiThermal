package com.oplus.view;

import android.graphics.drawable.Drawable;
import android.telecom.Log;
import android.view.View;
import android.view.ViewRootImpl;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;

/* loaded from: classes.dex */
public class ViewRootManager {
    private static final String TAG = "ViewRootManager";
    private BackgroundBlurDrawable mBackgroundBlurDrawable;

    public ViewRootManager(View view) {
        this.mBackgroundBlurDrawable = null;
        ViewRootImpl viewRootImpl = view.getViewRootImpl();
        if (viewRootImpl != null) {
            this.mBackgroundBlurDrawable = viewRootImpl.createBackgroundBlurDrawable();
        } else {
            Log.d(TAG, "viewRootImpl is null return null", new Object[0]);
        }
    }

    public Drawable getBackgroundBlurDrawable() {
        return this.mBackgroundBlurDrawable;
    }

    public void setBlurRadius(int blurRadius) {
        BackgroundBlurDrawable backgroundBlurDrawable = this.mBackgroundBlurDrawable;
        if (backgroundBlurDrawable == null) {
            Log.d(TAG, "BackgroundBlurDrawable is null return null", new Object[0]);
        } else {
            backgroundBlurDrawable.setBlurRadius(blurRadius);
        }
    }

    public void setCornerRadius(float cornerRadius) {
        BackgroundBlurDrawable backgroundBlurDrawable = this.mBackgroundBlurDrawable;
        if (backgroundBlurDrawable == null) {
            Log.d(TAG, "BackgroundBlurDrawable is null return null", new Object[0]);
        } else {
            backgroundBlurDrawable.setCornerRadius(cornerRadius);
        }
    }

    public void setColor(int color) {
        BackgroundBlurDrawable backgroundBlurDrawable = this.mBackgroundBlurDrawable;
        if (backgroundBlurDrawable == null) {
            Log.d(TAG, "BackgroundBlurDrawable is null return null", new Object[0]);
        } else {
            backgroundBlurDrawable.setColor(color);
        }
    }
}
