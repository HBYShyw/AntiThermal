package com.oplus.app;

import android.R;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.common.OplusFeatureCache;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.oplus.theme.IOplusThemeStyle;
import com.oplus.theme.OplusThemeStyle;
import com.oplus.util.OplusContextUtil;
import com.oplus.util.OplusLog;

/* loaded from: classes.dex */
public class OplusBootMessageDialog extends ProgressDialog {
    private static final float ALPHA_0 = 0.0f;
    private static final float ALPHA_1 = 1.0f;
    private static final boolean DBG = false;
    private static final long HIDE_TIME = 333;
    private static final long MAX_TIME_OUT_DISPLAYED_BOOT_DIALOG = 60000;
    private static final long SHOW_TIME = 833;
    private static final int SPLIT_COUNT = 2;
    private static final float START_LOADING_ALPHA = 0.9f;
    private static final String TAG = "OplusBootMessageDialog";
    private AnimationDrawable mAnimationDrawable;
    private long mBootDialogShowTimeBegin;
    private final int mIdProgressPercent;
    private boolean mIsStartAnimation;
    private PathInterpolator mPathInterpolator;
    private ValueAnimator mValueAnimator;

    protected OplusBootMessageDialog(Context context) {
        super(context, ((IOplusThemeStyle) OplusFeatureCache.getOrCreate(IOplusThemeStyle.DEFAULT, new Object[0])).getDialogBootMessageThemeStyle(OplusThemeStyle.DEFAULT_DIALOG_BOOTMSG_THEME));
        this.mBootDialogShowTimeBegin = 0L;
        this.mIsStartAnimation = false;
        this.mPathInterpolator = new PathInterpolator(0.33f, 0.0f, 0.67f, 1.0f);
        OplusLog.i(TAG, "new OplusBootMessageDialog");
        this.mIdProgressPercent = OplusContextUtil.getResId(getContext(), 201457791);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent event) {
        long displayedTime = System.currentTimeMillis() - this.mBootDialogShowTimeBegin;
        Log.v(TAG, "oat boot optmize dispatchKeyEvent event " + event + " " + displayedTime);
        if (isShowing() && displayedTime > MAX_TIME_OUT_DISPLAYED_BOOT_DIALOG) {
            dismiss();
            return true;
        }
        return true;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return true;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent ev) {
        long displayedTime = System.currentTimeMillis() - this.mBootDialogShowTimeBegin;
        Log.v(TAG, "oat boot optmize touch event " + displayedTime);
        if (isShowing() && displayedTime > MAX_TIME_OUT_DISPLAYED_BOOT_DIALOG) {
            dismiss();
            return true;
        }
        return true;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return true;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return true;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return true;
    }

    @Override // android.app.ProgressDialog, android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow(getWindow());
    }

    @Override // android.app.ProgressDialog, android.app.AlertDialog
    public void setMessage(CharSequence msg) {
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        this.mBootDialogShowTimeBegin = System.currentTimeMillis();
    }

    public static ProgressDialog create(Context context) {
        ProgressDialog dialog = new OplusBootMessageDialog(context);
        dialog.setProgressStyle(1);
        dialog.setCancelable(false);
        return dialog;
    }

    protected void onInitWindowParams(WindowManager.LayoutParams lp) {
        lp.type = 2021;
    }

    protected int getWindowFlags() {
        return -2013264128;
    }

    private void initWindow(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.layoutInDisplayCutoutMode = 3;
        lp.gravity = 119;
        lp.screenOrientation = 5;
        lp.width = -1;
        lp.height = -1;
        onInitWindowParams(lp);
        window.addFlags(getWindowFlags());
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(1280);
        window.setAttributes(lp);
        updateMessage();
    }

    private void updateMessage() {
        TextView text = (TextView) findViewById(R.id.message);
        String message = getContext().getString(201588969);
        text.setText(message);
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        ValueAnimator endAnimation = ValueAnimator.ofFloat(1.0f, 0.0f);
        endAnimation.setDuration(HIDE_TIME);
        endAnimation.setInterpolator(this.mPathInterpolator);
        endAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.app.OplusBootMessageDialog.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                Float alpha = (Float) animation.getAnimatedValue();
                OplusBootMessageDialog.this.getWindow().getDecorView().setAlpha(alpha.floatValue());
                if (alpha.floatValue() == 0.0f) {
                    OplusBootMessageDialog.super.dismiss();
                }
            }
        });
        endAnimation.start();
    }

    public void startProgress(long timeMillis) {
        Log.d(TAG, "oat boot optmize startProgress " + timeMillis + " " + Thread.currentThread());
        final ImageView icon = (ImageView) findViewById(201457725);
        final TextView message = (TextView) findViewById(R.id.message);
        this.mAnimationDrawable = (AnimationDrawable) icon.getDrawable();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mValueAnimator = ofFloat;
        ofFloat.setDuration(SHOW_TIME);
        this.mValueAnimator.setInterpolator(this.mPathInterpolator);
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.app.OplusBootMessageDialog.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator animation) {
                Float alpha = (Float) animation.getAnimatedValue();
                icon.setAlpha(alpha.floatValue());
                message.setAlpha(alpha.floatValue());
                if (alpha.floatValue() > OplusBootMessageDialog.START_LOADING_ALPHA && !OplusBootMessageDialog.this.mIsStartAnimation) {
                    OplusBootMessageDialog.this.mIsStartAnimation = true;
                    OplusBootMessageDialog.this.mAnimationDrawable.start();
                }
            }
        });
        this.mValueAnimator.start();
    }

    public void stopProgress(boolean isFromLauncher) {
        AnimationDrawable animationDrawable = this.mAnimationDrawable;
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}
