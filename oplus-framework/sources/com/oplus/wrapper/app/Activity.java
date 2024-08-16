package com.oplus.wrapper.app;

import android.app.Activity;
import android.os.IBinder;
import com.oplus.wrapper.app.Activity;
import java.util.Objects;

/* loaded from: classes.dex */
public class Activity {
    private final android.app.Activity mActivity;

    /* loaded from: classes.dex */
    public interface TranslucentConversionListener {
        void onTranslucentConversionComplete(boolean z);
    }

    public Activity(android.app.Activity activity) {
        this.mActivity = activity;
    }

    public final IBinder getActivityToken() {
        return this.mActivity.getActivityToken();
    }

    public boolean convertToTranslucent(final TranslucentConversionListener callback, android.app.ActivityOptions options) {
        Activity.TranslucentConversionListener conversionListener = null;
        if (callback != null) {
            Objects.requireNonNull(callback);
            conversionListener = new Activity.TranslucentConversionListener() { // from class: com.oplus.wrapper.app.Activity$$ExternalSyntheticLambda0
                public final void onTranslucentConversionComplete(boolean z) {
                    Activity.TranslucentConversionListener.this.onTranslucentConversionComplete(z);
                }
            };
        }
        return this.mActivity.convertToTranslucent(conversionListener, options);
    }

    public void convertFromTranslucent() {
        this.mActivity.convertFromTranslucent();
    }

    public final boolean isResumed() {
        return this.mActivity.isResumed();
    }
}
