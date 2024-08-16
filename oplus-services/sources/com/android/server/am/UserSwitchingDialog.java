package com.android.server.am;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Slog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.bluetooth.BluetoothStatsLog;
import com.android.internal.util.ObjectUtils;
import com.android.internal.util.UserIcons;
import com.android.server.wm.WindowManagerService;
import java.util.concurrent.atomic.AtomicBoolean;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class UserSwitchingDialog extends AlertDialog {
    private static final int ANIMATION_TIMEOUT_MS = 1000;
    protected static final boolean DEBUG = true;
    private static final long DIALOG_SHOW_HIDE_ANIMATION_DURATION_MS = 300;
    private static final String TAG = "UserSwitchingDialog";
    private static final long TRACE_TAG = 64;
    protected final Context mContext;
    private final boolean mDisableAnimations;
    private final Handler mHandler;
    private final boolean mNeedToFreezeScreen;
    protected final UserInfo mNewUser;
    protected final UserInfo mOldUser;
    private final String mSwitchingFromSystemUserMessage;
    private final String mSwitchingToSystemUserMessage;
    private final int mTraceCookie;
    public IUserSwitchingDialogExt mUserSwitchingDialogExt;
    private final WindowManagerService mWindowManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserSwitchingDialog(Context context, UserInfo userInfo, UserInfo userInfo2, String str, String str2, WindowManagerService windowManagerService) {
        super(context);
        this.mHandler = new Handler(Looper.myLooper());
        this.mUserSwitchingDialogExt = (IUserSwitchingDialogExt) ExtLoader.type(IUserSwitchingDialogExt.class).create();
        this.mContext = context;
        this.mOldUser = userInfo;
        this.mNewUser = userInfo2;
        this.mSwitchingFromSystemUserMessage = str;
        this.mSwitchingToSystemUserMessage = str2;
        boolean z = SystemProperties.getBoolean("debug.usercontroller.disable_user_switching_dialog_animations", false);
        this.mDisableAnimations = z;
        this.mWindowManager = windowManagerService;
        this.mNeedToFreezeScreen = !z;
        this.mTraceCookie = (userInfo.id * 21474) + userInfo2.id;
        inflateContent();
        configureWindow();
    }

    private void configureWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.privateFlags = BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_FAIL_REASON__PAIRING_FAIL_REASON_UNKNOWN_IO_CAP;
        if (this.mNewUser.id == 888) {
            attributes.privateFlags = 524288 | BluetoothStatsLog.BLUETOOTH_SMP_PAIRING_EVENT_REPORTED__SMP_FAIL_REASON__PAIRING_FAIL_REASON_UNKNOWN_IO_CAP;
        }
        attributes.layoutInDisplayCutoutMode = 3;
        window.setAttributes(attributes);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setType(2010);
        window.setDecorFitsSystemWindows(false);
        window.getInsetsController().hide(WindowInsets.Type.navigationBars());
    }

    void inflateContent() {
        setCancelable(false);
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(201917506, (ViewGroup) null);
        if (textView != null) {
            String textMessage = getTextMessage();
            textView.setAccessibilityPaneTitle(textMessage);
            textView.setText(textMessage);
            if (!UserManager.isDeviceInDemoMode(this.mContext)) {
                textView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, getContext().getDrawable(R.drawable.item_background_holo_dark), (Drawable) null, (Drawable) null);
            }
            setView(textView);
        }
        ImageView imageView = (ImageView) findViewById(R.id.icon);
        if (imageView != null) {
            imageView.setImageBitmap(getUserIconRounded());
        }
        ImageView imageView2 = (ImageView) findViewById(R.id.select_dialog_listview);
        if (imageView2 != null) {
            if (this.mDisableAnimations) {
                imageView2.setVisibility(8);
                return;
            }
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.^attr-private.colorProgressBackgroundNormal, typedValue, true);
            imageView2.setColorFilter(typedValue.data);
        }
    }

    private Bitmap getUserIconRounded() {
        Bitmap bitmap = (Bitmap) ObjectUtils.getOrElse(BitmapFactory.decodeFile(this.mNewUser.iconPath), defaultUserIcon(this.mNewUser.id));
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Paint paint = new Paint(1);
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        paint.setShader(new BitmapShader(bitmap, tileMode, tileMode));
        float f = width;
        float f2 = height;
        new Canvas(createBitmap).drawRoundRect(new RectF(0.0f, 0.0f, f, f2), f / 2.0f, f2 / 2.0f, paint);
        return createBitmap;
    }

    private Bitmap defaultUserIcon(int i) {
        Resources resources = getContext().getResources();
        return UserIcons.convertToBitmapAtUserIconSize(resources, UserIcons.getDefaultUserIcon(resources, i, false));
    }

    private String getTextMessage() {
        String str;
        Resources resources = getContext().getResources();
        if (UserManager.isDeviceInDemoMode(this.mContext)) {
            return resources.getString(this.mOldUser.isDemo() ? R.string.face_error_timeout : R.string.face_error_unable_to_process);
        }
        if (this.mOldUser.id == 0) {
            str = this.mSwitchingFromSystemUserMessage;
        } else {
            str = this.mNewUser.id == 0 ? this.mSwitchingToSystemUserMessage : null;
        }
        return str != null ? str : this.mUserSwitchingDialogExt.fixSwitchingMessage(17041812, this.mNewUser.name, 201588903, resources);
    }

    private boolean isUserSetupComplete(UserInfo userInfo) {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 0, userInfo.id) == 1;
    }

    @Override // android.app.Dialog
    public void show() {
        asyncTraceBegin("dialog", 0);
        super.show();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        stopFreezingScreen();
        asyncTraceEnd("dialog", 0);
    }

    public void show(final Runnable runnable) {
        Slog.d(TAG, "show called");
        show();
        startShowAnimation(new Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                UserSwitchingDialog.this.lambda$show$0(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$0(Runnable runnable) {
        startFreezingScreen();
        runnable.run();
    }

    public void dismiss(final Runnable runnable) {
        Slog.d(TAG, "dismiss called");
        if (runnable == null) {
            dismiss();
        } else {
            startDismissAnimation(new Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    UserSwitchingDialog.this.lambda$dismiss$1(runnable);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$1(Runnable runnable) {
        dismiss();
        runnable.run();
    }

    private void startFreezingScreen() {
        if (this.mNeedToFreezeScreen) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            traceBegin("startFreezingScreen");
            this.mWindowManager.startFreezingScreen(0, 0);
            traceEnd("startFreezingScreen");
            long elapsedRealtime2 = SystemClock.elapsedRealtime() - elapsedRealtime;
            this.mUserSwitchingDialogExt.startFreezingScreenInStartUser(this.mOldUser.id, this.mNewUser.id);
            this.mUserSwitchingDialogExt.startUserInternalEnter(true, this.mOldUser.id, this.mNewUser.id, -1L, elapsedRealtime, elapsedRealtime2, true);
        }
    }

    private void stopFreezingScreen() {
        if (this.mNeedToFreezeScreen) {
            traceBegin("stopFreezingScreen");
            this.mWindowManager.stopFreezingScreen();
            traceEnd("stopFreezingScreen");
        }
    }

    private void startShowAnimation(final Runnable runnable) {
        if (this.mDisableAnimations) {
            runnable.run();
        } else {
            asyncTraceBegin("showAnimation", 1);
            startDialogAnimation("show", new AlphaAnimation(0.0f, 1.0f), new Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    UserSwitchingDialog.this.lambda$startShowAnimation$3(runnable);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startShowAnimation$3(final Runnable runnable) {
        asyncTraceEnd("showAnimation", 1);
        asyncTraceBegin("spinnerAnimation", 2);
        startProgressAnimation(new Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UserSwitchingDialog.this.lambda$startShowAnimation$2(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startShowAnimation$2(Runnable runnable) {
        asyncTraceEnd("spinnerAnimation", 2);
        runnable.run();
    }

    private void startDismissAnimation(final Runnable runnable) {
        if (this.mDisableAnimations || this.mNeedToFreezeScreen) {
            runnable.run();
        } else {
            asyncTraceBegin("dismissAnimation", 3);
            startDialogAnimation("dismiss", new AlphaAnimation(1.0f, 0.0f), new Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    UserSwitchingDialog.this.lambda$startDismissAnimation$4(runnable);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDismissAnimation$4(Runnable runnable) {
        asyncTraceEnd("dismissAnimation", 3);
        runnable.run();
    }

    private void startProgressAnimation(Runnable runnable) {
        AnimatedVectorDrawable spinnerAVD = getSpinnerAVD();
        if (this.mDisableAnimations || spinnerAVD == null) {
            runnable.run();
            return;
        }
        final Runnable animationWithTimeout = animationWithTimeout("spinner", runnable);
        spinnerAVD.registerAnimationCallback(new Animatable2.AnimationCallback() { // from class: com.android.server.am.UserSwitchingDialog.1
            @Override // android.graphics.drawable.Animatable2.AnimationCallback
            public void onAnimationEnd(Drawable drawable) {
                animationWithTimeout.run();
            }
        });
        spinnerAVD.start();
    }

    private AnimatedVectorDrawable getSpinnerAVD() {
        ImageView imageView = (ImageView) findViewById(R.id.select_dialog_listview);
        if (imageView == null) {
            return null;
        }
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AnimatedVectorDrawable) {
            return (AnimatedVectorDrawable) drawable;
        }
        return null;
    }

    private void startDialogAnimation(String str, Animation animation, Runnable runnable) {
        View findViewById = findViewById(R.id.content);
        if (this.mDisableAnimations || findViewById == null) {
            runnable.run();
            return;
        }
        final Runnable animationWithTimeout = animationWithTimeout(str, runnable);
        animation.setDuration(DIALOG_SHOW_HIDE_ANIMATION_DURATION_MS);
        animation.setAnimationListener(new Animation.AnimationListener() { // from class: com.android.server.am.UserSwitchingDialog.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation2) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation2) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation2) {
                animationWithTimeout.run();
            }
        });
        findViewById.startAnimation(animation);
    }

    private Runnable animationWithTimeout(final String str, final Runnable runnable) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        final Runnable runnable2 = new Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                UserSwitchingDialog.this.lambda$animationWithTimeout$5(atomicBoolean, runnable);
            }
        };
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                UserSwitchingDialog.lambda$animationWithTimeout$6(str, runnable2);
            }
        }, 1000L);
        return runnable2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animationWithTimeout$5(AtomicBoolean atomicBoolean, Runnable runnable) {
        if (atomicBoolean.getAndSet(false)) {
            this.mHandler.removeCallbacksAndMessages(null);
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animationWithTimeout$6(String str, Runnable runnable) {
        Slog.w(TAG, str + " animation not completed in 1000 ms");
        runnable.run();
    }

    private void asyncTraceBegin(String str, int i) {
        Slog.d(TAG, "asyncTraceBegin-" + str);
        Trace.asyncTraceBegin(TRACE_TAG, TAG + str, this.mTraceCookie + i);
    }

    private void asyncTraceEnd(String str, int i) {
        Trace.asyncTraceEnd(TRACE_TAG, TAG + str, this.mTraceCookie + i);
        Slog.d(TAG, "asyncTraceEnd-" + str);
    }

    private void traceBegin(String str) {
        Slog.d(TAG, "traceBegin-" + str);
        Trace.traceBegin(TRACE_TAG, str);
    }

    private void traceEnd(String str) {
        Trace.traceEnd(TRACE_TAG);
        Slog.d(TAG, "traceEnd-" + str);
    }
}
