package android.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.PathInterpolator;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class OplusCursorFeedbackManager implements IOplusCursorFeedbackManager {
    private static final int EXPAND_ANIMATION_DURATION = 200;
    private static final int EXTRA_CURSOR_LEFT_OFFSET = 6;
    private static final int EXTRA_CURSOR_WIDTH_PX = 11;
    private static final int KEYCODE_DEL_INTERVAL = 500;
    private static final int SECOND_EXPAND_ANIMATION_DURATION = 183;
    private static final int SECOND_EXTRA_CURSOR_LEFT_OFFSET = 4;
    private static final int SECOND_EXTRA_CURSOR_WIDTH_PX = 7;
    private static final int SECOND_SHRINK_ANIMATION_DURATION = 300;
    private static final int SHRINK_ANIMATION_DURATION = 183;
    private static final String TAG = "OplusCursorFeedback";
    private AnimatorSet mCursorAnimatorSet;
    private float mCursorColorAnimatorRatio;
    private float mCursorHorizontalOffsetAnimatorRatio;
    private float mCursorWidthAnimatorRatio;
    private WeakReference<Editor> mEditorRef;
    private int mExtraCursorWidth;
    private int mExtraHorizontalOffset;
    private Handler mHandler;
    private boolean mKeepDeleting;
    private long mLastKeyCodeDelDownTime;
    private StopCursorRunnable mStopCursorRunnable;
    private static final PointF sExpandControlPoint1 = new PointF(0.3f, 0.0f);
    private static final PointF sExpandControlPoint2 = new PointF(0.83f, 1.0f);
    private static final PointF sShrinkControlPoint1 = new PointF(0.17f, 0.0f);
    private static final PointF sShrinkControlPoint2 = new PointF(0.83f, 1.05f);
    private static final PointF sSecondExpandControlPoint1 = new PointF(0.17f, 0.09f);
    private static final PointF sSecondExpandControlPoint2 = new PointF(0.83f, 0.28f);
    private static final PointF sSecondShrinkControlPoint1 = new PointF(0.3f, 0.0f);
    private static final PointF sSecondShrinkControlPoint2 = new PointF(0.1f, 1.0f);
    private static final PointF sAwayControlPoint1 = new PointF(0.3f, 0.0f);
    private static final PointF sAwayControlPoint2 = new PointF(0.83f, 1.0f);
    private static final PointF sNearControlPoint1 = new PointF(0.17f, 0.0f);
    private static final PointF sNearControlPoint2 = new PointF(0.83f, 1.05f);
    private static final PointF sSecondAwayControlPoint1 = new PointF(0.17f, -0.11f);
    private static final PointF sSecondAwayControlPoint2 = new PointF(0.83f, 1.0f);
    private static final PointF sSecondNearControlPoint1 = new PointF(0.3f, 0.0f);
    private static final PointF sSecondNearControlPoint2 = new PointF(0.1f, 1.0f);
    private static final PointF sLightenControlPoint1 = new PointF(0.33f, 0.0f);
    private static final PointF sLightenControlPoint2 = new PointF(0.67f, 1.0f);
    private static final PointF sDarkenControlPoint1 = new PointF(0.33f, 0.0f);
    private static final PointF sDarkenControlPoint2 = new PointF(0.67f, 1.0f);
    private static final PointF sSecondLightenControlPoint1 = new PointF(0.33f, 0.0f);
    private static final PointF sSecondLightenControlPoint2 = new PointF(0.67f, 1.0f);
    private static final PointF sSecondDarkenControlPoint1 = new PointF(0.33f, 0.0f);
    private static final PointF sSecondDarkenControlPoint2 = new PointF(0.67f, 1.0f);

    private int getExtraCursorWidth() {
        return (int) (this.mExtraCursorWidth * this.mCursorWidthAnimatorRatio);
    }

    private int getExtraCursorLeftOffset() {
        return (int) (this.mExtraHorizontalOffset * this.mCursorHorizontalOffsetAnimatorRatio);
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public boolean refreshCursorRenderTime(Editor editor) {
        if (this.mCursorWidthAnimatorRatio > 0.0f) {
            long currTime = SystemClock.uptimeMillis();
            editor.getWrapper().setShowCursor(currTime);
            return true;
        }
        return false;
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public int getExtraCursorWidth(Editor editor) {
        log("getExtraCursorWidth");
        int extraWidth = getExtraCursorWidth();
        return extraWidth;
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public int getExtraLeftOffset(Editor editor) {
        log("getExtraLeftOffset");
        int extraOffset = getExtraCursorLeftOffset();
        boolean isRtl = editor.getTextView().isLayoutRtl();
        return extraOffset * (isRtl ? -1 : 1);
    }

    private void startCursorOffsetAnimator(AnimatorSet.Builder builder, Editor editor) {
        ValueAnimator awayAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        PointF pointF = sAwayControlPoint1;
        float f = pointF.x;
        float f2 = pointF.y;
        PointF pointF2 = sAwayControlPoint2;
        PathInterpolator awayInterpolator = new PathInterpolator(f, f2, pointF2.x, pointF2.y);
        awayAnimator.setDuration(200L);
        awayAnimator.setInterpolator(awayInterpolator);
        awayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                OplusCursorFeedbackManager.this.mExtraHorizontalOffset = 6;
                float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                OplusCursorFeedbackManager.this.mCursorHorizontalOffsetAnimatorRatio = currentValue;
            }
        });
        builder.with(awayAnimator);
        ValueAnimator nearAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        PointF pointF3 = sNearControlPoint1;
        float f3 = pointF3.x;
        float f4 = pointF3.y;
        PointF pointF4 = sNearControlPoint2;
        PathInterpolator nearInterpolator = new PathInterpolator(f3, f4, pointF4.x, pointF4.y);
        nearAnimator.setDuration(183L);
        nearAnimator.setStartDelay(200L);
        nearAnimator.setInterpolator(nearInterpolator);
        nearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                OplusCursorFeedbackManager.this.mCursorHorizontalOffsetAnimatorRatio = currentValue;
            }
        });
        builder.with(nearAnimator);
        ValueAnimator secondAwayAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        PointF pointF5 = sSecondAwayControlPoint1;
        float f5 = pointF5.x;
        float f6 = pointF5.y;
        PointF pointF6 = sSecondAwayControlPoint2;
        PathInterpolator secondAwayInterpolator = new PathInterpolator(f5, f6, pointF6.x, pointF6.y);
        secondAwayAnimator.setDuration(183L);
        secondAwayAnimator.setStartDelay(383L);
        secondAwayAnimator.setInterpolator(secondAwayInterpolator);
        secondAwayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                OplusCursorFeedbackManager.this.mExtraHorizontalOffset = 4;
                float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                OplusCursorFeedbackManager.this.mCursorHorizontalOffsetAnimatorRatio = currentValue;
            }
        });
        builder.with(secondAwayAnimator);
        ValueAnimator secondNearAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        PointF pointF7 = sSecondNearControlPoint1;
        float f7 = pointF7.x;
        float f8 = pointF7.y;
        PointF pointF8 = sSecondNearControlPoint2;
        PathInterpolator secondNearInterpolator = new PathInterpolator(f7, f8, pointF8.x, pointF8.y);
        secondNearAnimator.setDuration(300L);
        secondNearAnimator.setStartDelay(566L);
        secondNearAnimator.setInterpolator(secondNearInterpolator);
        secondNearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                OplusCursorFeedbackManager.this.mCursorHorizontalOffsetAnimatorRatio = currentValue;
            }
        });
        builder.with(secondNearAnimator);
    }

    private void startCursorWidthAnimator(AnimatorSet.Builder builder, final Editor editor) {
        ValueAnimator expandAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        PointF pointF = sExpandControlPoint1;
        float f = pointF.x;
        float f2 = pointF.y;
        PointF pointF2 = sExpandControlPoint2;
        PathInterpolator expandInterpolator = new PathInterpolator(f, f2, pointF2.x, pointF2.y);
        expandAnimator.setDuration(200L);
        expandAnimator.setInterpolator(expandInterpolator);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    OplusCursorFeedbackManager.this.mExtraCursorWidth = 11;
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    OplusCursorFeedbackManager.this.mCursorWidthAnimatorRatio = currentValue;
                    editor.updateCursorPosition();
                    TextView textView = editor.getWrapper().getTextView();
                    if (textView.getLayout() != null) {
                        textView.invalidateCursorPath();
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(expandAnimator);
        ValueAnimator shrinkAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        PointF pointF3 = sShrinkControlPoint1;
        float f3 = pointF3.x;
        float f4 = pointF3.y;
        PointF pointF4 = sShrinkControlPoint2;
        PathInterpolator shrinkInterpolator = new PathInterpolator(f3, f4, pointF4.x, pointF4.y);
        shrinkAnimator.setDuration(183L);
        shrinkAnimator.setStartDelay(200L);
        shrinkAnimator.setInterpolator(shrinkInterpolator);
        shrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    OplusCursorFeedbackManager.this.mCursorWidthAnimatorRatio = currentValue;
                    editor.updateCursorPosition();
                    TextView textView = editor.getWrapper().getTextView();
                    if (textView.getLayout() != null) {
                        textView.invalidateCursorPath();
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(shrinkAnimator);
        ValueAnimator secondExpandAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        PointF pointF5 = sSecondExpandControlPoint1;
        float f5 = pointF5.x;
        float f6 = pointF5.y;
        PointF pointF6 = sSecondExpandControlPoint2;
        PathInterpolator secondExpandInterpolator = new PathInterpolator(f5, f6, pointF6.x, pointF6.y);
        secondExpandAnimator.setDuration(183L);
        secondExpandAnimator.setStartDelay(383L);
        secondExpandAnimator.setInterpolator(secondExpandInterpolator);
        secondExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    OplusCursorFeedbackManager.this.mExtraCursorWidth = 7;
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    OplusCursorFeedbackManager.this.mCursorWidthAnimatorRatio = currentValue;
                    editor.updateCursorPosition();
                    TextView textView = editor.getWrapper().getTextView();
                    if (textView.getLayout() != null) {
                        textView.invalidateCursorPath();
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(secondExpandAnimator);
        ValueAnimator secondShrinkAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        PointF pointF7 = sSecondShrinkControlPoint1;
        float f7 = pointF7.x;
        float f8 = pointF7.y;
        PointF pointF8 = sSecondShrinkControlPoint2;
        PathInterpolator secondShrinkInterpolator = new PathInterpolator(f7, f8, pointF8.x, pointF8.y);
        secondShrinkAnimator.setDuration(300L);
        secondShrinkAnimator.setStartDelay(566L);
        secondShrinkAnimator.setInterpolator(secondShrinkInterpolator);
        secondShrinkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    OplusCursorFeedbackManager.this.mCursorWidthAnimatorRatio = currentValue;
                    editor.updateCursorPosition();
                    TextView textView = editor.getWrapper().getTextView();
                    if (textView.getLayout() != null) {
                        textView.invalidateCursorPath();
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(secondShrinkAnimator);
    }

    private void startCursorColorAnimator(AnimatorSet.Builder builder, final Editor editor) {
        ValueAnimator lightenAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        PointF pointF = sLightenControlPoint1;
        float f = pointF.x;
        float f2 = pointF.y;
        PointF pointF2 = sLightenControlPoint2;
        PathInterpolator lightenInterpolator = new PathInterpolator(f, f2, pointF2.x, pointF2.y);
        lightenAnimator.setDuration(200L);
        lightenAnimator.setInterpolator(lightenInterpolator);
        lightenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    if (editor.mDrawableForCursor != null) {
                        int alpha = (int) ((100.0f * currentValue) + 155.0f);
                        int color = Color.argb(alpha, 255, 255, 255);
                        BlendModeColorFilter filter = new BlendModeColorFilter(color, BlendMode.SRC_ATOP);
                        editor.mDrawableForCursor.setColorFilter(filter);
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(lightenAnimator);
        ValueAnimator darkenAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        PointF pointF3 = sDarkenControlPoint1;
        float f3 = pointF3.x;
        float f4 = pointF3.y;
        PointF pointF4 = sDarkenControlPoint2;
        PathInterpolator darkenInterpolator = new PathInterpolator(f3, f4, pointF4.x, pointF4.y);
        darkenAnimator.setDuration(183L);
        darkenAnimator.setStartDelay(200L);
        darkenAnimator.setInterpolator(darkenInterpolator);
        darkenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    if (editor.mDrawableForCursor != null) {
                        int alpha = (int) ((100.0f * currentValue) + 155.0f);
                        int color = Color.argb(alpha, 255, 255, 255);
                        BlendModeColorFilter filter = new BlendModeColorFilter(color, BlendMode.SRC_ATOP);
                        editor.mDrawableForCursor.setColorFilter(filter);
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(darkenAnimator);
        ValueAnimator secondLightenAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        PointF pointF5 = sSecondLightenControlPoint1;
        float f5 = pointF5.x;
        float f6 = pointF5.y;
        PointF pointF6 = sSecondLightenControlPoint2;
        PathInterpolator secondLightenInterpolator = new PathInterpolator(f5, f6, pointF6.x, pointF6.y);
        secondLightenAnimator.setDuration(183L);
        secondLightenAnimator.setStartDelay(383L);
        secondLightenAnimator.setInterpolator(secondLightenInterpolator);
        secondLightenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.11
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    if (editor.mDrawableForCursor != null) {
                        int alpha = (int) ((100.0f * currentValue) + 155.0f);
                        int color = Color.argb(alpha, 255, 255, 255);
                        BlendModeColorFilter filter = new BlendModeColorFilter(color, BlendMode.SRC_ATOP);
                        editor.mDrawableForCursor.setColorFilter(filter);
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(secondLightenAnimator);
        ValueAnimator secondDarkenAnimator = ValueAnimator.ofFloat(1.0f, 0.0f);
        PointF pointF7 = sSecondDarkenControlPoint1;
        float f7 = pointF7.x;
        float f8 = pointF7.y;
        PointF pointF8 = sSecondDarkenControlPoint2;
        PathInterpolator secondDarkenInterpolator = new PathInterpolator(f7, f8, pointF8.x, pointF8.y);
        secondDarkenAnimator.setDuration(300L);
        secondDarkenAnimator.setStartDelay(566L);
        secondDarkenAnimator.setInterpolator(secondDarkenInterpolator);
        secondDarkenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.OplusCursorFeedbackManager.12
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    float currentValue = ((Float) animation.getAnimatedValue()).floatValue();
                    if (editor.mDrawableForCursor != null) {
                        int alpha = (int) ((100.0f * currentValue) + 155.0f);
                        int color = Color.argb(alpha, 255, 255, 255);
                        BlendModeColorFilter filter = new BlendModeColorFilter(color, BlendMode.SRC_ATOP);
                        editor.mDrawableForCursor.setColorFilter(filter);
                    }
                } catch (Exception e) {
                    OplusCursorFeedbackManager.this.log("startCursorWidthAnimator update failed " + e.getMessage());
                    OplusCursorFeedbackManager.this.stopCursorAnimator(editor);
                }
            }
        });
        builder.with(secondDarkenAnimator);
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public void startCursorAnimator(final Editor editor) {
        log("startCursorAnimator");
        AnimatorSet animatorSet = this.mCursorAnimatorSet;
        if (animatorSet != null && animatorSet.isRunning()) {
            return;
        }
        editor.getWrapper().suspendBlink();
        ValueAnimator emptyAnimator = ValueAnimator.ofInt(0, 1);
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mCursorAnimatorSet = animatorSet2;
        AnimatorSet.Builder builder = animatorSet2.play(emptyAnimator);
        startCursorOffsetAnimator(builder, editor);
        startCursorColorAnimator(builder, editor);
        startCursorWidthAnimator(builder, editor);
        this.mCursorAnimatorSet.addListener(new Animator.AnimatorListener() { // from class: android.widget.OplusCursorFeedbackManager.13
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                if (editor.mDrawableForCursor != null) {
                    editor.mDrawableForCursor.setColorFilter(null);
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.mCursorAnimatorSet.start();
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public void stopCursorAnimator(Editor editor) {
        log("stopCursorAnimator");
        AnimatorSet animatorSet = this.mCursorAnimatorSet;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mCursorAnimatorSet.end();
        }
        this.mCursorHorizontalOffsetAnimatorRatio = 0.0f;
        this.mCursorWidthAnimatorRatio = 0.0f;
        this.mCursorColorAnimatorRatio = 0.0f;
        if (editor == null) {
            return;
        }
        TextView textView = editor.getTextView();
        if (textView.getLayout() != null) {
            editor.updateCursorPosition();
            textView.invalidateCursorPath();
        }
        editor.getWrapper().resumeBlink();
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public void handleKeyCodeDelDown(Editor editor, KeyEvent keyEvent) {
        log("handleKeyCodeDelDown");
        if (editor == null) {
            return;
        }
        this.mEditorRef = new WeakReference<>(editor);
        TextView textView = editor.getTextView();
        CharSequence text = textView.getText();
        if (text != null && text.length() > 0) {
            return;
        }
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.myLooper());
        }
        if (this.mStopCursorRunnable == null) {
            this.mStopCursorRunnable = new StopCursorRunnable();
        }
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - this.mLastKeyCodeDelDownTime < 866) {
            this.mKeepDeleting = true;
        } else {
            this.mKeepDeleting = false;
        }
        this.mLastKeyCodeDelDownTime = currentTime;
        if (!this.mKeepDeleting) {
            startCursorAnimator(editor);
            this.mHandler.removeCallbacks(this.mStopCursorRunnable);
            this.mHandler.postDelayed(this.mStopCursorRunnable, 866L);
        }
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public void handleKeyCodeDelUp(Editor editor, KeyEvent keyEvent) {
    }

    @Override // android.widget.IOplusCursorFeedbackManager
    public void editorDetachFromWindow(Editor editor) {
        log("editorDetachFromWindow " + editor);
        WeakReference<Editor> weakReference = this.mEditorRef;
        if (weakReference == null || weakReference.get() == null || this.mEditorRef.get() != editor) {
            return;
        }
        clear();
    }

    private void clear() {
        Handler handler;
        log("clear");
        AnimatorSet animatorSet = this.mCursorAnimatorSet;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mCursorAnimatorSet.cancel();
        }
        WeakReference<Editor> weakReference = this.mEditorRef;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.mCursorAnimatorSet = null;
        StopCursorRunnable stopCursorRunnable = this.mStopCursorRunnable;
        if (stopCursorRunnable != null && (handler = this.mHandler) != null) {
            handler.removeCallbacks(stopCursorRunnable);
        }
        this.mStopCursorRunnable = null;
        this.mHandler = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StopCursorRunnable implements Runnable {
        private StopCursorRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (OplusCursorFeedbackManager.this.mEditorRef != null) {
                OplusCursorFeedbackManager oplusCursorFeedbackManager = OplusCursorFeedbackManager.this;
                oplusCursorFeedbackManager.stopCursorAnimator((Editor) oplusCursorFeedbackManager.mEditorRef.get());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String content) {
        Log.d(TAG, content);
    }
}
