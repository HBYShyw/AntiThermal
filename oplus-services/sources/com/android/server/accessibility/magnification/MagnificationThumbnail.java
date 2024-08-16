package com.android.server.accessibility.magnification;

import android.R;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class MagnificationThumbnail {
    private static final float ASPECT_RATIO = 14.0f;
    private static final float BG_ASPECT_RATIO = 7.0f;
    private static final boolean DEBUG = false;
    private static final int FADE_IN_ANIMATION_DURATION_MS = 200;
    private static final int FADE_OUT_ANIMATION_DURATION_MS = 1000;
    private static final int LINGER_DURATION_MS = 500;
    private static final String LOG_TAG = "MagnificationThumbnail";
    private final WindowManager.LayoutParams mBackgroundParams;
    private final Context mContext;
    private final Handler mHandler;
    private boolean mIsFadingIn;
    private ObjectAnimator mThumbnailAnimator;
    private int mThumbnailHeight;

    @VisibleForTesting
    public final FrameLayout mThumbnailLayout;
    private final View mThumbnailView;
    private int mThumbnailWidth;
    private boolean mVisible = false;
    private Rect mWindowBounds;
    private final WindowManager mWindowManager;

    public MagnificationThumbnail(Context context, WindowManager windowManager, Handler handler) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mHandler = handler;
        this.mWindowBounds = windowManager.getCurrentWindowMetrics().getBounds();
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(17367362, (ViewGroup) null);
        this.mThumbnailLayout = frameLayout;
        this.mThumbnailView = frameLayout.findViewById(R.id.actionPrevious);
        this.mBackgroundParams = createLayoutParams();
        this.mThumbnailWidth = 0;
        this.mThumbnailHeight = 0;
    }

    public void setThumbnailBounds(final Rect rect, final float f, final float f2, final float f3) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationThumbnail.this.lambda$setThumbnailBounds$0(rect, f, f2, f3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setThumbnailBounds$0(Rect rect, float f, float f2, float f3) {
        this.mWindowBounds = rect;
        setBackgroundBounds();
        if (this.mVisible) {
            lambda$updateThumbnail$1(f, f2, f3);
        }
    }

    private void setBackgroundBounds() {
        Point magnificationThumbnailPadding = getMagnificationThumbnailPadding(this.mContext);
        this.mThumbnailWidth = (int) (this.mWindowBounds.width() / BG_ASPECT_RATIO);
        int height = (int) (this.mWindowBounds.height() / BG_ASPECT_RATIO);
        this.mThumbnailHeight = height;
        int i = magnificationThumbnailPadding.x;
        int i2 = magnificationThumbnailPadding.y;
        WindowManager.LayoutParams layoutParams = this.mBackgroundParams;
        layoutParams.width = this.mThumbnailWidth;
        layoutParams.height = height;
        layoutParams.x = i;
        layoutParams.y = i2;
    }

    private void showThumbnail() {
        animateThumbnail(true);
    }

    public void hideThumbnail() {
        this.mHandler.post(new MagnificationThumbnail$$ExternalSyntheticLambda1(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideThumbnailMainThread() {
        if (this.mVisible) {
            animateThumbnail(false);
        }
    }

    private void animateThumbnail(final boolean z) {
        this.mHandler.removeCallbacks(new MagnificationThumbnail$$ExternalSyntheticLambda1(this));
        if (z) {
            this.mHandler.postDelayed(new MagnificationThumbnail$$ExternalSyntheticLambda1(this), 500L);
        }
        if (z == this.mIsFadingIn) {
            return;
        }
        this.mIsFadingIn = z;
        if (z && !this.mVisible) {
            this.mWindowManager.addView(this.mThumbnailLayout, this.mBackgroundParams);
            this.mVisible = true;
        }
        ObjectAnimator objectAnimator = this.mThumbnailAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        FrameLayout frameLayout = this.mThumbnailLayout;
        float[] fArr = new float[1];
        fArr[0] = z ? 1.0f : 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(frameLayout, "alpha", fArr);
        this.mThumbnailAnimator = ofFloat;
        ofFloat.setDuration(z ? 200L : 1000L);
        this.mThumbnailAnimator.addListener(new Animator.AnimatorListener() { // from class: com.android.server.accessibility.magnification.MagnificationThumbnail.1
            private boolean mIsCancelled;

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(@NonNull Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(@NonNull Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@NonNull Animator animator) {
                if (this.mIsCancelled || z || !MagnificationThumbnail.this.mVisible) {
                    return;
                }
                MagnificationThumbnail.this.mWindowManager.removeView(MagnificationThumbnail.this.mThumbnailLayout);
                MagnificationThumbnail.this.mVisible = false;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(@NonNull Animator animator) {
                this.mIsCancelled = true;
            }
        });
        this.mThumbnailAnimator.start();
    }

    public void updateThumbnail(final float f, final float f2, final float f3) {
        this.mHandler.post(new Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MagnificationThumbnail.this.lambda$updateThumbnail$1(f, f2, f3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateThumbnailMainThread, reason: merged with bridge method [inline-methods] */
    public void lambda$updateThumbnail$1(float f, float f2, float f3) {
        float f4;
        int i;
        showThumbnail();
        float scaleX = Float.isNaN(f) ? this.mThumbnailView.getScaleX() : 1.0f / f;
        if (!Float.isNaN(f)) {
            this.mThumbnailView.setScaleX(scaleX);
            this.mThumbnailView.setScaleY(scaleX);
        }
        if (this.mThumbnailView.getWidth() == 0 || this.mThumbnailView.getHeight() == 0) {
            f4 = this.mThumbnailWidth;
            i = this.mThumbnailHeight;
        } else {
            f4 = this.mThumbnailView.getWidth();
            i = this.mThumbnailView.getHeight();
        }
        float f5 = i;
        if (Float.isNaN(f2)) {
            return;
        }
        float paddingTop = this.mThumbnailView.getPaddingTop();
        this.mThumbnailView.setTranslationX((f2 * 0.14285715f) - ((f4 / 2.0f) + paddingTop));
        this.mThumbnailView.setTranslationY((f3 * 0.14285715f) - ((f5 / 2.0f) + paddingTop));
    }

    private WindowManager.LayoutParams createLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2027, 24, -2);
        layoutParams.inputFeatures = 1;
        layoutParams.gravity = 83;
        layoutParams.setFitInsetsTypes(WindowInsets.Type.ime() | WindowInsets.Type.navigationBars());
        return layoutParams;
    }

    private Point getMagnificationThumbnailPadding(Context context) {
        Point point = new Point(0, 0);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.action_bar_default_padding_end_material);
        point.x = dimensionPixelSize;
        point.y = dimensionPixelSize;
        return point;
    }
}
