package com.oplus.resolver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import com.oplus.resolver.OplusOShareManager;
import com.oplus.resolver.OplusResolverOshare;
import com.oplus.util.OplusChangeTextUtil;
import com.oplus.util.OplusRoundRectUtil;

/* loaded from: classes.dex */
public class OplusResolverOshare {
    private static final boolean DEBUG = true;
    private static final float DEFAULT_SCALE_PERCENT = 0.4f;
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static final int PANEL_VIEW_TEXT_CHANGE_DURATION = 200;
    private static final String TAG = "OplusResolverOshare";
    private Context mContext;
    private ImageView mDeviceIcon;
    private boolean mIsNeedToDelayCancelScaleAnim;
    private boolean mIsPause;
    private OplusOShareManager mOShareManager;
    private TextView mOpenOshareSummary;
    private Intent mOriginIntent;
    private TextView mOshareClosedTitle;
    private AnimatorSet mOshareFindingToFoundAnimator;
    private AnimatorSet mOshareFoundToFindingAnimator;
    private OplusResolverOshareLayout mOshareLayout;
    private RelativeLayout mOshareOpenedTextLayout;
    private View mOsharePanelView;
    private AnimatorSet mOsharePanelViewTextChangeAnimator;
    private ValueAnimator mPressAnimationRecorder;
    private float mPressValue;
    private OplusResolverScanLoadingIcon mScanLoadingIcon;
    private ValueAnimator mTouchAnimator;
    private static long sLastClickTime = 0;
    private static final PathInterpolator DEFAUT_PATHINTERPOLATOR = new PathInterpolator(0.3f, 0.0f, 0.1f, 1.0f);
    private boolean mHasInitView = false;
    private boolean mIsSplitScreenMode = false;

    public OplusResolverOshare(Context context, Intent intent) {
        this.mContext = context;
        this.mOriginIntent = intent;
    }

    private void logIfNeeded(String message) {
        Log.d(TAG, message);
    }

    public void setIsSplitScreenMode(boolean value) {
        this.mIsSplitScreenMode = value;
        OplusResolverOshareLayout oplusResolverOshareLayout = this.mOshareLayout;
        if (oplusResolverOshareLayout != null) {
            oplusResolverOshareLayout.setIsSplitScreenMode(value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initOShareService() {
        if (OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_RESOLVER_SHARE_EMAIL)) {
            Log.e(TAG, "initOShareService has FEATURE_RESOLVER_SHARE_EMAIL");
        } else {
            if (OplusOShareUtil.isNoOshareApplication(this.mContext, this.mOriginIntent)) {
                return;
            }
            OplusOShareManager oplusOShareManager = new OplusOShareManager(this.mContext);
            this.mOShareManager = oplusOShareManager;
            oplusOShareManager.initOShareService();
            this.mOShareManager.addListener(new OplusOShareManager.OShareListener() { // from class: com.oplus.resolver.OplusResolverOshare$$ExternalSyntheticLambda3
                @Override // com.oplus.resolver.OplusOShareManager.OShareListener
                public final void onDevicesChange(boolean z, int i) {
                    OplusResolverOshare.this.updateOShareUI(z, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initOShareView(View oShareView) {
        if (OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_RESOLVER_SHARE_EMAIL)) {
            Log.e(TAG, "initOShareView has FEATURE_RESOLVER_SHARE_EMAIL");
            return;
        }
        if (OplusOShareUtil.isNoOshareApplication(this.mContext, this.mOriginIntent)) {
            return;
        }
        this.mIsPause = false;
        OplusResolverOshareLayout oplusResolverOshareLayout = (OplusResolverOshareLayout) oShareView.findViewById(201457739);
        this.mOshareLayout = oplusResolverOshareLayout;
        if (oplusResolverOshareLayout != null) {
            oplusResolverOshareLayout.setIsSplitScreenMode(this.mIsSplitScreenMode);
            this.mOshareLayout.setVisibility(0);
        }
        if (this.mHasInitView) {
            logIfNeeded("the view has init");
            OplusOShareManager oplusOShareManager = this.mOShareManager;
            boolean z = oplusOShareManager != null && oplusOShareManager.isSwitchSendOn();
            OplusOShareManager oplusOShareManager2 = this.mOShareManager;
            updateOShareUI(z, oplusOShareManager2 != null ? oplusOShareManager2.getDeviceSize() : 0);
            return;
        }
        View findViewById = oShareView.findViewById(201457760);
        this.mOsharePanelView = findViewById;
        if (findViewById instanceof ViewStub) {
            View inflate = ((ViewStub) findViewById).inflate();
            this.mOsharePanelView = inflate;
            ViewTreeObserver observer = inflate.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.oplus.resolver.OplusResolverOshare.1
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    if (OplusResolverOshare.this.mOsharePanelView.getWidth() > 0 && OplusResolverOshare.this.mOsharePanelView.getHeight() > 0) {
                        OplusResolverOshare.this.mOsharePanelView.getViewTreeObserver().removeOnPreDrawListener(this);
                        OplusResolverOshare oplusResolverOshare = OplusResolverOshare.this;
                        Drawable background = oplusResolverOshare.getBackgroundDrawable(oplusResolverOshare.mOsharePanelView.getWidth(), OplusResolverOshare.this.mOsharePanelView.getHeight());
                        OplusResolverOshare.this.mOsharePanelView.setBackground(background);
                        return true;
                    }
                    return true;
                }
            });
        }
        View view = this.mOsharePanelView;
        if (view == null) {
            Log.e(TAG, "initOShareView: mOsharePanelView is null");
        } else {
            initOSharePanel(view);
            this.mHasInitView = true;
        }
    }

    private void initOSharePanel(View oSharePanel) {
        TextView textView = (TextView) oSharePanel.findViewById(201457778);
        this.mOshareClosedTitle = textView;
        if (textView != null) {
            formatTextViewSize(textView, 201654289);
        }
        TextView openOshareTitle = (TextView) oSharePanel.findViewById(201457779);
        if (openOshareTitle != null) {
            formatTextViewSize(openOshareTitle, 201654289);
        }
        TextView textView2 = (TextView) oSharePanel.findViewById(201457765);
        this.mOpenOshareSummary = textView2;
        if (textView2 != null) {
            formatTextViewSize(textView2, 201654290);
        }
        this.mOshareOpenedTextLayout = (RelativeLayout) oSharePanel.findViewById(201457740);
        this.mScanLoadingIcon = (OplusResolverScanLoadingIcon) oSharePanel.findViewById(201457738);
        this.mDeviceIcon = (ImageView) oSharePanel.findViewById(201457841);
        initOsharePanelViewAnimator();
        oSharePanel.setOnTouchListener(new View.OnTouchListener() { // from class: com.oplus.resolver.OplusResolverOshare$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$initOSharePanel$0;
                lambda$initOSharePanel$0 = OplusResolverOshare.this.lambda$initOSharePanel$0(view, motionEvent);
                return lambda$initOSharePanel$0;
            }
        });
        oSharePanel.setOnClickListener(new View.OnClickListener() { // from class: com.oplus.resolver.OplusResolverOshare$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                OplusResolverOshare.this.lambda$initOSharePanel$2(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initOSharePanel$0(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            animatePress();
            return false;
        }
        if (motionEvent.getAction() == 1) {
            animateNormal();
            return false;
        }
        if (motionEvent.getAction() == 3) {
            animateNormal();
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initOSharePanel$2(View v) {
        if (isFastClick()) {
            return;
        }
        String packageName = this.mContext.getString(201588967);
        if (!ApplicationEnableUtil.applicationEnable(this.mContext.getPackageManager(), packageName)) {
            ApplicationEnableUtil.showApplicationEnableDialog(this.mContext, packageName, new DialogInterface.OnClickListener() { // from class: com.oplus.resolver.OplusResolverOshare$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    OplusResolverOshare.this.lambda$initOSharePanel$1(dialogInterface, i);
                }
            });
        } else {
            OplusOShareUtil.startOshareActivity(this.mContext, this.mOriginIntent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initOSharePanel$1(DialogInterface dialog, int which) {
        OplusOShareUtil.startOshareActivity(this.mContext, this.mOriginIntent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.resolver.OplusResolverOshare$2, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends AnimatorListenerAdapter {
        AnonymousClass2() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animation) {
            OplusResolverOshare.this.mPressAnimationRecorder = COUIPressFeedbackUtil.generatePressAnimationRecord();
            OplusResolverOshare.this.mPressAnimationRecorder.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.oplus.resolver.OplusResolverOshare$2$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    OplusResolverOshare.AnonymousClass2.this.lambda$onAnimationStart$0(valueAnimator);
                }
            });
            OplusResolverOshare.this.mPressAnimationRecorder.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationStart$0(ValueAnimator animation1) {
            OplusResolverOshare.this.mPressValue = ((Float) animation1.getAnimatedValue()).floatValue();
            if (OplusResolverOshare.this.mIsNeedToDelayCancelScaleAnim && ((float) animation1.getCurrentPlayTime()) > ((float) animation1.getDuration()) * OplusResolverOshare.DEFAULT_SCALE_PERCENT) {
                OplusResolverOshare.this.mIsNeedToDelayCancelScaleAnim = false;
                OplusResolverOshare.this.animateNormal();
            }
        }
    }

    private void animatePress() {
        cancelAnimation(true);
        ValueAnimator generatePressAnimation = COUIPressFeedbackUtil.generatePressAnimation(this.mOsharePanelView, new AnonymousClass2());
        this.mTouchAnimator = generatePressAnimation;
        generatePressAnimation.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateNormal() {
        cancelAnimation(false);
        if (this.mIsNeedToDelayCancelScaleAnim) {
            return;
        }
        ValueAnimator generateResumeAnimation = COUIPressFeedbackUtil.generateResumeAnimation(this.mOsharePanelView, this.mPressValue, null);
        this.mTouchAnimator = generateResumeAnimation;
        generateResumeAnimation.start();
    }

    private void cancelAnimation(boolean isPressed) {
        boolean z = false;
        this.mIsNeedToDelayCancelScaleAnim = false;
        ValueAnimator valueAnimator = this.mPressAnimationRecorder;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            if (!isPressed && ((float) this.mPressAnimationRecorder.getCurrentPlayTime()) < ((float) this.mPressAnimationRecorder.getDuration()) * DEFAULT_SCALE_PERCENT) {
                z = true;
            }
            this.mIsNeedToDelayCancelScaleAnim = z;
            if (!z) {
                this.mPressAnimationRecorder.cancel();
            }
        }
        if (!this.mIsNeedToDelayCancelScaleAnim) {
            COUIPressFeedbackUtil.cancelAnim(this.mTouchAnimator);
        }
    }

    private void formatTextViewSize(TextView view, int id) {
        float fontScale = this.mContext.getResources().getConfiguration().fontScale;
        int textSize = (int) OplusChangeTextUtil.getSuitableFontSize(this.mContext.getResources().getDimensionPixelSize(id), fontScale, 4);
        view.setTextSize(0, textSize);
    }

    private void initOsharePanelViewAnimator() {
        ObjectAnimator oshareClosedTitleDisappearAnimator = ObjectAnimator.ofFloat(this.mOshareClosedTitle, "alpha", 1.0f, 0.0f);
        ObjectAnimator oshareOpenedTextLayoutAppearAnimator = ObjectAnimator.ofFloat(this.mOshareOpenedTextLayout, "alpha", 0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mOsharePanelViewTextChangeAnimator = animatorSet;
        animatorSet.playTogether(oshareClosedTitleDisappearAnimator, oshareOpenedTextLayoutAppearAnimator);
        this.mOsharePanelViewTextChangeAnimator.setDuration(200L);
        AnimatorSet animatorSet2 = this.mOsharePanelViewTextChangeAnimator;
        PathInterpolator pathInterpolator = DEFAUT_PATHINTERPOLATOR;
        animatorSet2.setInterpolator(pathInterpolator);
        this.mOsharePanelViewTextChangeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.resolver.OplusResolverOshare.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                OplusResolverOshare.this.mOshareLayout.setShareOpenStatus(1);
                OplusResolverOshare.this.mOshareOpenedTextLayout.setVisibility(0);
                OplusResolverOshare.this.mOshareClosedTitle.setVisibility(0);
                OplusResolverOshare.this.mScanLoadingIcon.startAnimator();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                OplusResolverOshare.this.mOshareClosedTitle.setVisibility(8);
                OplusResolverOshare.this.mOshareOpenedTextLayout.setAlpha(1.0f);
                OplusResolverOshare.this.mOshareClosedTitle.setAlpha(1.0f);
            }
        });
        ObjectAnimator oshareScanLoadingIconDisappearAnimator = ObjectAnimator.ofFloat(this.mScanLoadingIcon, "alpha", 1.0f, 0.0f);
        ObjectAnimator oshareDeviceIconAppearAnimator = ObjectAnimator.ofFloat(this.mDeviceIcon, "alpha", 0.0f, 1.0f);
        AnimatorSet animatorSet3 = new AnimatorSet();
        this.mOshareFindingToFoundAnimator = animatorSet3;
        animatorSet3.playTogether(oshareScanLoadingIconDisappearAnimator, oshareDeviceIconAppearAnimator);
        this.mOshareFindingToFoundAnimator.setDuration(200L);
        this.mOshareFindingToFoundAnimator.setInterpolator(pathInterpolator);
        this.mOshareFindingToFoundAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.resolver.OplusResolverOshare.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                OplusResolverOshare.this.mOshareClosedTitle.setVisibility(8);
                OplusResolverOshare.this.mOshareOpenedTextLayout.setVisibility(0);
                OplusResolverOshare.this.mScanLoadingIcon.setVisibility(0);
                OplusResolverOshare.this.mDeviceIcon.setVisibility(0);
                OplusResolverOshare.this.mScanLoadingIcon.stopAnimator();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                OplusResolverOshare.this.mScanLoadingIcon.setAlpha(1.0f);
                OplusResolverOshare.this.mDeviceIcon.setAlpha(1.0f);
                OplusResolverOshare.this.mScanLoadingIcon.setVisibility(8);
            }
        });
        ObjectAnimator oshareDeviceIconDisappearAnimator = ObjectAnimator.ofFloat(this.mDeviceIcon, "alpha", 1.0f, 0.0f);
        ObjectAnimator oshareScanLoadingIconAppearAnimator = ObjectAnimator.ofFloat(this.mScanLoadingIcon, "alpha", 0.0f, 1.0f);
        AnimatorSet animatorSet4 = new AnimatorSet();
        this.mOshareFoundToFindingAnimator = animatorSet4;
        animatorSet4.playTogether(oshareDeviceIconDisappearAnimator, oshareScanLoadingIconAppearAnimator);
        this.mOshareFoundToFindingAnimator.setDuration(200L);
        this.mOshareFoundToFindingAnimator.setInterpolator(pathInterpolator);
        this.mOshareFoundToFindingAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.resolver.OplusResolverOshare.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                OplusResolverOshare.this.mOshareClosedTitle.setVisibility(8);
                OplusResolverOshare.this.mOshareOpenedTextLayout.setVisibility(0);
                OplusResolverOshare.this.mScanLoadingIcon.setVisibility(0);
                OplusResolverOshare.this.mDeviceIcon.setVisibility(0);
                OplusResolverOshare.this.mScanLoadingIcon.startAnimator();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                OplusResolverOshare.this.mDeviceIcon.setVisibility(8);
                OplusResolverOshare.this.mScanLoadingIcon.setAlpha(1.0f);
                OplusResolverOshare.this.mDeviceIcon.setAlpha(1.0f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getBackgroundDrawable(int width, int height) {
        int radius = this.mContext.getResources().getDimensionPixelOffset(201654425);
        int backgroundColor = this.mContext.getResources().getColor(201719864);
        int foregroundColor = this.mContext.getResources().getColor(201719871);
        return OplusRoundRectUtil.getInstance().getRoundRectDrawable(width, height, radius, backgroundColor, foregroundColor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dismissOShareView() {
        logIfNeeded("dismissOShareView");
        if (!this.mHasInitView) {
            Log.w(TAG, "the view has not init");
        } else {
            this.mOshareLayout.setVisibility(8);
            this.mIsPause = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onResume() {
        OplusOShareManager oplusOShareManager = this.mOShareManager;
        if (oplusOShareManager != null) {
            oplusOShareManager.onResume();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPause() {
        OplusOShareManager oplusOShareManager = this.mOShareManager;
        if (oplusOShareManager != null) {
            oplusOShareManager.onPause();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDestroy() {
        OplusOShareManager oplusOShareManager = this.mOShareManager;
        if (oplusOShareManager != null) {
            oplusOShareManager.onDestroy();
        }
        this.mContext = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOShareUI(boolean oShareOpened, int deviceSize) {
        if (this.mIsPause || !this.mHasInitView) {
            logIfNeeded("the view is pause or has not init view");
            return;
        }
        if (oShareOpened) {
            if (deviceSize < 1) {
                oshareFindingDevices();
                return;
            } else {
                oshareFoundDevices(deviceSize);
                return;
            }
        }
        oshareClose();
    }

    private void oshareFindingDevices() {
        logIfNeeded("show opened panel");
        this.mOpenOshareSummary.setText(this.mContext.getString(201589208));
        if (this.mOshareClosedTitle.getVisibility() == 0) {
            this.mScanLoadingIcon.setVisibility(0);
            this.mDeviceIcon.setVisibility(8);
            if (!this.mOsharePanelViewTextChangeAnimator.isRunning()) {
                this.mOshareFoundToFindingAnimator.cancel();
                this.mOshareFindingToFoundAnimator.cancel();
                logIfNeeded("start text change animator");
                this.mOsharePanelViewTextChangeAnimator.start();
                return;
            }
            logIfNeeded("text change animator keep running");
            return;
        }
        if (this.mDeviceIcon.getVisibility() == 0) {
            if (!this.mOshareFoundToFindingAnimator.isRunning()) {
                this.mOsharePanelViewTextChangeAnimator.cancel();
                this.mOshareFindingToFoundAnimator.cancel();
                this.mOshareFoundToFindingAnimator.start();
                return;
            }
            return;
        }
        this.mScanLoadingIcon.startAnimator();
    }

    private void oshareFoundDevices(int deviceSize) {
        this.mOpenOshareSummary.setText(this.mContext.getResources().getQuantityString(202440711, deviceSize, Integer.valueOf(deviceSize)));
        if (this.mOshareClosedTitle.getVisibility() == 0) {
            this.mScanLoadingIcon.setVisibility(8);
            this.mDeviceIcon.setVisibility(0);
            if (!this.mOsharePanelViewTextChangeAnimator.isRunning()) {
                this.mOsharePanelViewTextChangeAnimator.start();
            }
        } else if (this.mScanLoadingIcon.getVisibility() == 0 && !this.mOshareFindingToFoundAnimator.isRunning()) {
            this.mOshareFoundToFindingAnimator.cancel();
            this.mOsharePanelViewTextChangeAnimator.cancel();
            this.mOshareFindingToFoundAnimator.start();
        }
        logIfNeeded("show sharing view");
    }

    private void oshareClose() {
        this.mOshareLayout.setShareOpenStatus(0);
        this.mOshareOpenedTextLayout.setVisibility(8);
        this.mOshareClosedTitle.setVisibility(0);
        this.mScanLoadingIcon.setVisibility(0);
        this.mDeviceIcon.setVisibility(8);
        this.mScanLoadingIcon.stopAnimator();
        ViewTreeObserver observer = this.mOsharePanelView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.oplus.resolver.OplusResolverOshare.6
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                if (OplusResolverOshare.this.mOsharePanelView.getWidth() > 0 && OplusResolverOshare.this.mOsharePanelView.getHeight() > 0) {
                    OplusResolverOshare.this.mOsharePanelView.getViewTreeObserver().removeOnPreDrawListener(this);
                    OplusResolverOshare oplusResolverOshare = OplusResolverOshare.this;
                    Drawable background = oplusResolverOshare.getBackgroundDrawable(oplusResolverOshare.mOsharePanelView.getWidth(), OplusResolverOshare.this.mOsharePanelView.getHeight());
                    OplusResolverOshare.this.mOsharePanelView.setBackground(background);
                    return true;
                }
                return true;
            }
        });
    }

    public static boolean isFastClick() {
        boolean flag;
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - sLastClickTime > 500) {
            flag = false;
        } else {
            flag = true;
        }
        sLastClickTime = curClickTime;
        return flag;
    }
}
