package com.android.server.accessibility.magnification;

import android.content.Context;
import android.provider.Settings;
import android.util.MathUtils;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BackgroundThread;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MagnificationScaleProvider {

    @VisibleForTesting
    protected static final float DEFAULT_MAGNIFICATION_SCALE = 2.0f;
    public static final float MAX_SCALE = 8.0f;
    public static final float MIN_SCALE = 1.0f;
    private final Context mContext;

    @GuardedBy({"mLock"})
    private final SparseArray<SparseArray<Float>> mUsersScales = new SparseArray<>();
    private int mCurrentUserId = 0;
    private final Object mLock = new Object();

    public MagnificationScaleProvider(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void putScale(final float f, int i) {
        if (i == 0) {
            BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationScaleProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MagnificationScaleProvider.this.lambda$putScale$0(f);
                }
            });
            return;
        }
        synchronized (this.mLock) {
            getScalesWithCurrentUser().put(i, Float.valueOf(f));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$putScale$0(float f) {
        Settings.Secure.putFloatForUser(this.mContext.getContentResolver(), "accessibility_display_magnification_scale", f, this.mCurrentUserId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getScale(int i) {
        float floatValue;
        if (i == 0) {
            return Settings.Secure.getFloatForUser(this.mContext.getContentResolver(), "accessibility_display_magnification_scale", DEFAULT_MAGNIFICATION_SCALE, this.mCurrentUserId);
        }
        synchronized (this.mLock) {
            floatValue = getScalesWithCurrentUser().get(i, Float.valueOf(DEFAULT_MAGNIFICATION_SCALE)).floatValue();
        }
        return floatValue;
    }

    @GuardedBy({"mLock"})
    private SparseArray<Float> getScalesWithCurrentUser() {
        SparseArray<Float> sparseArray = this.mUsersScales.get(this.mCurrentUserId);
        if (sparseArray != null) {
            return sparseArray;
        }
        SparseArray<Float> sparseArray2 = new SparseArray<>();
        this.mUsersScales.put(this.mCurrentUserId, sparseArray2);
        return sparseArray2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserChanged(int i) {
        synchronized (this.mLock) {
            this.mCurrentUserId = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onUserRemoved(int i) {
        synchronized (this.mLock) {
            this.mUsersScales.remove(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayRemoved(int i) {
        synchronized (this.mLock) {
            for (int size = this.mUsersScales.size() - 1; size >= 0; size--) {
                this.mUsersScales.get(size).remove(i);
            }
        }
    }

    public String toString() {
        String str;
        synchronized (this.mLock) {
            str = "MagnificationScaleProvider{mCurrentUserId=" + this.mCurrentUserId + "Scale on the default display=" + getScale(0) + "Scales on non-default displays=" + getScalesWithCurrentUser() + '}';
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static float constrainScale(float f) {
        return MathUtils.constrain(f, 1.0f, 8.0f);
    }
}
