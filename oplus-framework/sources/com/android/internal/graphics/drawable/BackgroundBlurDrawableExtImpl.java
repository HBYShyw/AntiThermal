package com.android.internal.graphics.drawable;

import android.util.Log;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;

/* loaded from: classes.dex */
public class BackgroundBlurDrawableExtImpl implements IBackgroundBlurDrawableExt {
    private static final String TAG = BackgroundBlurDrawableExtImpl.class.getSimpleName();
    private BackgroundBlurDrawable mBase;
    private String mPackageName;

    public BackgroundBlurDrawableExtImpl(Object object) {
        if (object != null && (object instanceof BackgroundBlurDrawable)) {
            this.mBase = (BackgroundBlurDrawable) object;
        }
    }

    public BackgroundBlurDrawable.BlurRegion[] sortBlurRegionsIfNeeded(BackgroundBlurDrawable.BlurRegion[] tmpBlurRegions) {
        if (!"com.oplus.secondaryhome".equals(this.mPackageName)) {
            return tmpBlurRegions;
        }
        int length = tmpBlurRegions.length;
        for (int left = 0; left < length; left++) {
            for (int right = left + 1; right < length; right++) {
                int leftValue = tmpBlurRegions[left].blurRadius;
                int rightValue = tmpBlurRegions[right].blurRadius;
                if (leftValue > rightValue) {
                    BackgroundBlurDrawable.BlurRegion tmpRegion = tmpBlurRegions[left];
                    tmpBlurRegions[left] = tmpBlurRegions[right];
                    tmpBlurRegions[right] = tmpRegion;
                }
            }
        }
        Log.d(TAG, "resort background blur drawable regions for secondaryhome");
        return tmpBlurRegions;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }
}
