package com.android.server.wm;

import android.R;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Insets;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class ImmersiveModeConfirmation {
    private static final String CONFIRMED = "confirmed";
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_SHOW_EVERY_TIME = false;
    private static final int IMMERSIVE_MODE_CONFIRMATION_WINDOW_TYPE = 2017;
    private static final String TAG = "ImmersiveModeConfirmation";
    private static boolean sConfirmed;
    private boolean mCanSystemBarsBeShownByUser;
    private ClingWindowView mClingWindow;
    private final Context mContext;
    private final H mHandler;
    private final long mPanicThresholdMs;
    private long mPanicTime;
    private final long mShowDelayMs;
    private boolean mVrModeEnabled;
    private Context mWindowContext;
    private WindowManager mWindowManager;
    private final IBinder mWindowToken = new Binder();
    private int mLockTaskState = 0;
    private final Runnable mConfirm = new Runnable() { // from class: com.android.server.wm.ImmersiveModeConfirmation.1
        @Override // java.lang.Runnable
        public void run() {
            if (!ImmersiveModeConfirmation.sConfirmed) {
                ImmersiveModeConfirmation.sConfirmed = true;
                ImmersiveModeConfirmation.saveSetting(ImmersiveModeConfirmation.this.mContext);
            }
            ImmersiveModeConfirmation.this.handleHide();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmersiveModeConfirmation(Context context, Looper looper, boolean z, boolean z2) {
        Display display = context.getDisplay();
        Context systemUiContext = ActivityThread.currentActivityThread().getSystemUiContext();
        this.mContext = display.getDisplayId() != 0 ? systemUiContext.createDisplayContext(display) : systemUiContext;
        this.mHandler = new H(looper);
        this.mShowDelayMs = context.getResources().getInteger(17695051) * 3;
        this.mPanicThresholdMs = context.getResources().getInteger(R.integer.config_networkMeteredMultipathPreference);
        this.mVrModeEnabled = z;
        this.mCanSystemBarsBeShownByUser = z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean loadSetting(int i, Context context) {
        boolean z = sConfirmed;
        sConfirmed = false;
        String str = null;
        try {
            str = Settings.Secure.getStringForUser(context.getContentResolver(), "immersive_mode_confirmations", -2);
            sConfirmed = CONFIRMED.equals(str);
        } catch (Throwable th) {
            Slog.w(TAG, "Error loading confirmations, value=" + str, th);
        }
        return sConfirmed != z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void saveSetting(Context context) {
        try {
            Settings.Secure.putStringForUser(context.getContentResolver(), "immersive_mode_confirmations", sConfirmed ? CONFIRMED : null, -2);
        } catch (Throwable th) {
            Slog.w(TAG, "Error saving confirmations, sConfirmed=" + sConfirmed, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onSettingChanged(int i) {
        boolean loadSetting = loadSetting(i, this.mContext);
        if (loadSetting && sConfirmed) {
            this.mHandler.sendEmptyMessage(2);
        }
        return loadSetting;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void immersiveModeChangedLw(int i, boolean z, boolean z2, boolean z3) {
        this.mHandler.removeMessages(1);
        if (z) {
            if (sConfirmed || !z2 || this.mVrModeEnabled || !this.mCanSystemBarsBeShownByUser || z3 || UserManager.isDeviceInDemoMode(this.mContext) || this.mLockTaskState == 1) {
                return;
            }
            Message obtainMessage = this.mHandler.obtainMessage(1);
            obtainMessage.arg1 = i;
            this.mHandler.sendMessageDelayed(obtainMessage, this.mShowDelayMs);
            return;
        }
        this.mHandler.sendEmptyMessage(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onPowerKeyDown(boolean z, long j, boolean z2, boolean z3) {
        if (!z && j - this.mPanicTime < this.mPanicThresholdMs) {
            return this.mClingWindow == null;
        }
        if (z && z2 && !z3) {
            this.mPanicTime = j;
        } else {
            this.mPanicTime = 0L;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void confirmCurrentPrompt() {
        if (this.mClingWindow != null) {
            this.mHandler.post(this.mConfirm);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHide() {
        if (this.mClingWindow != null) {
            try {
                getWindowManager(-1).removeView(this.mClingWindow);
                this.mClingWindow = null;
            } catch (WindowManager.InvalidDisplayException e) {
                Slog.w(TAG, "Fail to hide the immersive confirmation window because of " + e);
            }
        }
    }

    private WindowManager.LayoutParams getClingWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, IMMERSIVE_MODE_CONFIRMATION_WINDOW_TYPE, 16777504, -3);
        layoutParams.setFitInsetsTypes(layoutParams.getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
        layoutParams.privateFlags |= 536870928;
        layoutParams.setTitle(TAG);
        layoutParams.windowAnimations = R.style.Animation.ImmersiveModeConfirmation;
        layoutParams.token = getWindowToken();
        return layoutParams;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FrameLayout.LayoutParams getBubbleLayoutParams() {
        return new FrameLayout.LayoutParams(this.mContext.getResources().getDimensionPixelSize(R.dimen.kg_status_clock_font_size), -2, 49);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder getWindowToken() {
        return this.mWindowToken;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class ClingWindowView extends FrameLayout {
        private static final int ANIMATION_DURATION = 250;
        private static final int BGCOLOR = Integer.MIN_VALUE;
        private static final int OFFSET_DP = 96;
        private ViewGroup mClingLayout;
        private final ColorDrawable mColor;
        private ValueAnimator mColorAnim;
        private final Runnable mConfirm;
        private ViewTreeObserver.OnComputeInternalInsetsListener mInsetsListener;
        private final Interpolator mInterpolator;
        private BroadcastReceiver mReceiver;
        private Runnable mUpdateLayoutRunnable;

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        ClingWindowView(Context context, Runnable runnable) {
            super(context);
            ColorDrawable colorDrawable = new ColorDrawable(0);
            this.mColor = colorDrawable;
            this.mUpdateLayoutRunnable = new Runnable() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ClingWindowView.this.mClingLayout == null || ClingWindowView.this.mClingLayout.getParent() == null) {
                        return;
                    }
                    ClingWindowView.this.mClingLayout.setLayoutParams(ImmersiveModeConfirmation.this.getBubbleLayoutParams());
                }
            };
            this.mInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.2
                private final int[] mTmpInt2 = new int[2];

                public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
                    ClingWindowView.this.mClingLayout.getLocationInWindow(this.mTmpInt2);
                    internalInsetsInfo.setTouchableInsets(3);
                    Region region = internalInsetsInfo.touchableRegion;
                    int[] iArr = this.mTmpInt2;
                    int i = iArr[0];
                    region.set(i, iArr[1], ClingWindowView.this.mClingLayout.getWidth() + i, this.mTmpInt2[1] + ClingWindowView.this.mClingLayout.getHeight());
                }
            };
            this.mReceiver = new BroadcastReceiver() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    if (intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
                        ClingWindowView clingWindowView = ClingWindowView.this;
                        clingWindowView.post(clingWindowView.mUpdateLayoutRunnable);
                    }
                }
            };
            this.mConfirm = runnable;
            setBackground(colorDrawable);
            setImportantForAccessibility(2);
            this.mInterpolator = AnimationUtils.loadInterpolator(((FrameLayout) this).mContext, R.interpolator.linear_out_slow_in);
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((FrameLayout) this).mContext.getDisplay().getMetrics(displayMetrics);
            float f = displayMetrics.density;
            getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsListener);
            ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(), R.layout.language_picker_item, null);
            this.mClingLayout = viewGroup;
            ((Button) viewGroup.findViewById(R.id.progress_percent)).setOnClickListener(new View.OnClickListener() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ClingWindowView.this.mConfirm.run();
                }
            });
            addView(this.mClingLayout, ImmersiveModeConfirmation.this.getBubbleLayoutParams());
            if (ActivityManager.isHighEndGfx()) {
                final ViewGroup viewGroup2 = this.mClingLayout;
                viewGroup2.setAlpha(0.0f);
                viewGroup2.setTranslationY(f * (-96.0f));
                postOnAnimation(new Runnable() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.5
                    @Override // java.lang.Runnable
                    public void run() {
                        viewGroup2.animate().alpha(1.0f).translationY(0.0f).setDuration(250L).setInterpolator(ClingWindowView.this.mInterpolator).withLayer().start();
                        ClingWindowView.this.mColorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), 0, Integer.MIN_VALUE);
                        ClingWindowView.this.mColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.5.1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                ClingWindowView.this.mColor.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
                            }
                        });
                        ClingWindowView.this.mColorAnim.setDuration(250L);
                        ClingWindowView.this.mColorAnim.setInterpolator(ClingWindowView.this.mInterpolator);
                        ClingWindowView.this.mColorAnim.start();
                    }
                });
            } else {
                this.mColor.setColor(Integer.MIN_VALUE);
            }
            ((FrameLayout) this).mContext.registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            ((FrameLayout) this).mContext.unregisterReceiver(this.mReceiver);
        }

        @Override // android.view.View
        public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
            return new WindowInsets.Builder(windowInsets).setInsets(WindowInsets.Type.systemBars(), Insets.NONE).build();
        }
    }

    private WindowManager getWindowManager(int i) {
        if (this.mWindowManager == null || this.mWindowContext == null) {
            Context createWindowContext = this.mContext.createWindowContext(IMMERSIVE_MODE_CONFIRMATION_WINDOW_TYPE, getOptionsForWindowContext(i));
            this.mWindowContext = createWindowContext;
            WindowManager windowManager = (WindowManager) createWindowContext.getSystemService(WindowManager.class);
            this.mWindowManager = windowManager;
            return windowManager;
        }
        Bundle optionsForWindowContext = getOptionsForWindowContext(i);
        try {
            WindowManagerGlobal.getWindowManagerService().attachWindowContextToDisplayArea(this.mWindowContext.getWindowContextToken(), IMMERSIVE_MODE_CONFIRMATION_WINDOW_TYPE, this.mContext.getDisplayId(), optionsForWindowContext);
            return this.mWindowManager;
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    private Bundle getOptionsForWindowContext(int i) {
        if (i == -1) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("root_display_area_id", i);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShow(int i) {
        if (this.mClingWindow != null) {
            Slog.d(TAG, "mClingWindow was not null ,we should not show it again,just return");
            return;
        }
        this.mClingWindow = new ClingWindowView(this.mContext, this.mConfirm);
        try {
            getWindowManager(i).addView(this.mClingWindow, getClingWindowLayoutParams());
        } catch (WindowManager.InvalidDisplayException e) {
            Slog.w(TAG, "Fail to show the immersive confirmation window because of " + e);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private final class H extends Handler {
        private static final int HIDE = 2;
        private static final int SHOW = 1;

        H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                ImmersiveModeConfirmation.this.handleShow(message.arg1);
            } else {
                if (i != 2) {
                    return;
                }
                ImmersiveModeConfirmation.this.handleHide();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onVrStateChangedLw(boolean z) {
        this.mVrModeEnabled = z;
        if (z) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessage(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLockTaskModeChangedLw(int i) {
        this.mLockTaskState = i;
    }
}
