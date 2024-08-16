package com.android.server.accessibility;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.media.AudioPlaybackConfiguration;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.FrameworkStatsLog;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.accessibility.FlashNotificationsController;
import com.android.server.am.ProcessList;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class FlashNotificationsController {

    @VisibleForTesting
    static final String ACTION_FLASH_NOTIFICATION_START_PREVIEW = "com.android.internal.intent.action.FLASH_NOTIFICATION_START_PREVIEW";

    @VisibleForTesting
    static final String ACTION_FLASH_NOTIFICATION_STOP_PREVIEW = "com.android.internal.intent.action.FLASH_NOTIFICATION_STOP_PREVIEW";
    private static final boolean DEBUG = true;

    @VisibleForTesting
    static final String EXTRA_FLASH_NOTIFICATION_PREVIEW_COLOR = "com.android.internal.intent.extra.FLASH_NOTIFICATION_PREVIEW_COLOR";

    @VisibleForTesting
    static final String EXTRA_FLASH_NOTIFICATION_PREVIEW_TYPE = "com.android.internal.intent.extra.FLASH_NOTIFICATION_PREVIEW_TYPE";
    private static final String LOG_TAG = "FlashNotifController";

    @VisibleForTesting
    static final int PREVIEW_TYPE_LONG = 1;

    @VisibleForTesting
    static final int PREVIEW_TYPE_SHORT = 0;
    private static final int SCREEN_DEFAULT_ALPHA = 1711276032;
    private static final int SCREEN_DEFAULT_COLOR = 16776960;
    private static final int SCREEN_DEFAULT_COLOR_WITH_ALPHA = 1728052992;
    private static final int SCREEN_FADE_DURATION_MS = 200;
    private static final int SCREEN_FADE_OUT_TIMEOUT_MS = 10;

    @VisibleForTesting
    static final String SETTING_KEY_CAMERA_FLASH_NOTIFICATION = "camera_flash_notification";

    @VisibleForTesting
    static final String SETTING_KEY_SCREEN_FLASH_NOTIFICATION = "screen_flash_notification";

    @VisibleForTesting
    static final String SETTING_KEY_SCREEN_FLASH_NOTIFICATION_COLOR = "screen_flash_notification_color_global";
    private static final String TAG_ALARM = "alarm";
    private static final String TAG_PREVIEW = "preview";
    private static final int TYPE_DEFAULT = 1;
    private static final int TYPE_DEFAULT_OFF_MS = 250;
    private static final int TYPE_DEFAULT_ON_MS = 350;
    private static final int TYPE_DEFAULT_SCREEN_DELAY_MS = 300;
    private static final int TYPE_LONG_PREVIEW = 3;
    private static final int TYPE_LONG_PREVIEW_OFF_MS = 1000;
    private static final int TYPE_LONG_PREVIEW_ON_MS = 5000;
    private static final int TYPE_SEQUENCE = 2;
    private static final int TYPE_SEQUENCE_OFF_MS = 700;
    private static final int TYPE_SEQUENCE_ON_MS = 700;
    private static final String WAKE_LOCK_TAG = "a11y:FlashNotificationsController";
    private static final long WAKE_LOCK_TIMEOUT_MS = 300000;
    private final AudioManager.AudioPlaybackCallback mAudioPlaybackCallback;
    private final Handler mCallbackHandler;
    private String mCameraId;
    private CameraManager mCameraManager;
    private final Context mContext;
    private FlashNotification mCurrentFlashNotification;
    private final DisplayManager mDisplayManager;
    private int mDisplayState;

    @VisibleForTesting
    final FlashBroadcastReceiver mFlashBroadcastReceiver;
    private final Handler mFlashNotificationHandler;

    @GuardedBy({"mFlashNotifications"})
    private final LinkedList<FlashNotification> mFlashNotifications;
    private boolean mIsAlarming;
    private boolean mIsCameraFlashNotificationEnabled;
    private boolean mIsCameraOpened;
    private boolean mIsScreenFlashNotificationEnabled;
    private boolean mIsTorchOn;
    private boolean mIsTorchTouched;
    private final Handler mMainHandler;
    private View mScreenFlashNotificationOverlayView;
    private volatile FlashNotificationThread mThread;

    @VisibleForTesting
    final CameraManager.AvailabilityCallback mTorchAvailabilityCallback;
    private final CameraManager.TorchCallback mTorchCallback;
    private final PowerManager.WakeLock mWakeLock;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    @interface FlashNotificationType {
    }

    /* renamed from: com.android.server.accessibility.FlashNotificationsController$3, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class AnonymousClass3 extends AudioManager.AudioPlaybackCallback {
        AnonymousClass3() {
        }

        @Override // android.media.AudioManager.AudioPlaybackCallback
        public void onPlaybackConfigChanged(List<AudioPlaybackConfiguration> list) {
            boolean anyMatch = list != null ? list.stream().anyMatch(new Predicate() { // from class: com.android.server.accessibility.FlashNotificationsController$3$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onPlaybackConfigChanged$0;
                    lambda$onPlaybackConfigChanged$0 = FlashNotificationsController.AnonymousClass3.lambda$onPlaybackConfigChanged$0((AudioPlaybackConfiguration) obj);
                    return lambda$onPlaybackConfigChanged$0;
                }
            }) : false;
            if (FlashNotificationsController.this.mIsAlarming != anyMatch) {
                Log.d(FlashNotificationsController.LOG_TAG, "alarm state changed: " + anyMatch);
                if (anyMatch) {
                    FlashNotificationsController.this.startFlashNotificationSequenceForAlarm();
                } else {
                    FlashNotificationsController.this.stopFlashNotificationSequenceForAlarm();
                }
                FlashNotificationsController.this.mIsAlarming = anyMatch;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onPlaybackConfigChanged$0(AudioPlaybackConfiguration audioPlaybackConfiguration) {
            return audioPlaybackConfiguration.isActive() && audioPlaybackConfiguration.getAudioAttributes().getUsage() == 4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FlashNotificationsController(Context context) {
        this(context, getStartedHandler("FlashNotificationThread"), getStartedHandler(LOG_TAG));
    }

    @VisibleForTesting
    FlashNotificationsController(Context context, Handler handler, Handler handler2) {
        this.mFlashNotifications = new LinkedList<>();
        this.mIsTorchTouched = false;
        this.mIsTorchOn = false;
        this.mIsCameraFlashNotificationEnabled = false;
        this.mIsScreenFlashNotificationEnabled = false;
        this.mIsAlarming = false;
        this.mDisplayState = 1;
        this.mIsCameraOpened = false;
        this.mCameraId = null;
        this.mTorchCallback = new CameraManager.TorchCallback() { // from class: com.android.server.accessibility.FlashNotificationsController.1
            @Override // android.hardware.camera2.CameraManager.TorchCallback
            public void onTorchModeChanged(String str, boolean z) {
                if (FlashNotificationsController.this.mCameraId == null || !FlashNotificationsController.this.mCameraId.equals(str)) {
                    return;
                }
                FlashNotificationsController.this.mIsTorchOn = z;
                Log.d(FlashNotificationsController.LOG_TAG, "onTorchModeChanged, set mIsTorchOn=" + z);
            }
        };
        this.mTorchAvailabilityCallback = new CameraManager.AvailabilityCallback() { // from class: com.android.server.accessibility.FlashNotificationsController.2
            public void onCameraOpened(String str, String str2) {
                if (FlashNotificationsController.this.mCameraId == null || !FlashNotificationsController.this.mCameraId.equals(str)) {
                    return;
                }
                FlashNotificationsController.this.mIsCameraOpened = true;
            }

            public void onCameraClosed(String str) {
                if (FlashNotificationsController.this.mCameraId == null || !FlashNotificationsController.this.mCameraId.equals(str)) {
                    return;
                }
                FlashNotificationsController.this.mIsCameraOpened = false;
            }
        };
        this.mAudioPlaybackCallback = new AnonymousClass3();
        this.mContext = context;
        Handler handler3 = new Handler(context.getMainLooper());
        this.mMainHandler = handler3;
        this.mFlashNotificationHandler = handler;
        this.mCallbackHandler = handler2;
        new FlashContentObserver(handler3).register(context.getContentResolver());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction(ACTION_FLASH_NOTIFICATION_START_PREVIEW);
        intentFilter.addAction(ACTION_FLASH_NOTIFICATION_STOP_PREVIEW);
        FlashBroadcastReceiver flashBroadcastReceiver = new FlashBroadcastReceiver();
        this.mFlashBroadcastReceiver = flashBroadcastReceiver;
        context.registerReceiver(flashBroadcastReceiver, intentFilter, 4);
        this.mWakeLock = ((PowerManager) context.getSystemService(PowerManager.class)).newWakeLock(1, WAKE_LOCK_TAG);
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mDisplayManager = displayManager;
        DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() { // from class: com.android.server.accessibility.FlashNotificationsController.4
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
                Display display;
                if (FlashNotificationsController.this.mDisplayManager == null || (display = FlashNotificationsController.this.mDisplayManager.getDisplay(i)) == null) {
                    return;
                }
                FlashNotificationsController.this.mDisplayState = display.getState();
            }
        };
        if (displayManager != null) {
            displayManager.registerDisplayListener(displayListener, null);
        }
    }

    private static Handler getStartedHandler(String str) {
        HandlerThread handlerThread = new HandlerThread(str);
        handlerThread.start();
        return handlerThread.getThreadHandler();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startFlashNotificationSequence(final String str, int i, IBinder iBinder) {
        FlashNotification flashNotification = new FlashNotification(str, 2, getScreenFlashColorPreference(i), iBinder, new IBinder.DeathRecipient() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                FlashNotificationsController.this.lambda$startFlashNotificationSequence$0(str);
            }
        });
        if (!flashNotification.tryLinkToDeath()) {
            return false;
        }
        requestStartFlashNotification(flashNotification);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stopFlashNotificationSequence(String str) {
        lambda$startFlashNotificationSequence$0(str);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startFlashNotificationEvent(String str, int i, String str2) {
        requestStartFlashNotification(new FlashNotification(str, 1, getScreenFlashColorPreference(i, str2)));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotificationShortPreview() {
        requestStartFlashNotification(new FlashNotification(TAG_PREVIEW, 1, getScreenFlashColorPreference(4)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotificationLongPreview(int i) {
        requestStartFlashNotification(new FlashNotification(TAG_PREVIEW, 3, i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopFlashNotificationLongPreview() {
        lambda$startFlashNotificationSequence$0(TAG_PREVIEW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotificationSequenceForAlarm() {
        requestStartFlashNotification(new FlashNotification("alarm", 2, getScreenFlashColorPreference(2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopFlashNotificationSequenceForAlarm() {
        lambda$startFlashNotificationSequence$0("alarm");
    }

    private void requestStartFlashNotification(FlashNotification flashNotification) {
        Log.d(LOG_TAG, "requestStartFlashNotification");
        boolean isEnabled = FeatureFlagUtils.isEnabled(this.mContext, "settings_flash_notifications");
        boolean z = false;
        this.mIsCameraFlashNotificationEnabled = isEnabled && Settings.System.getIntForUser(this.mContext.getContentResolver(), SETTING_KEY_CAMERA_FLASH_NOTIFICATION, 0, -2) != 0;
        if (isEnabled && Settings.System.getIntForUser(this.mContext.getContentResolver(), SETTING_KEY_SCREEN_FLASH_NOTIFICATION, 0, -2) != 0) {
            z = true;
        }
        this.mIsScreenFlashNotificationEnabled = z;
        if (flashNotification.mType == 1 && this.mIsScreenFlashNotificationEnabled) {
            this.mMainHandler.sendMessageDelayed(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((FlashNotificationsController) obj).startFlashNotification((FlashNotificationsController.FlashNotification) obj2);
                }
            }, this, flashNotification), 300L);
            Log.i(LOG_TAG, "give some delay for flash notification");
        } else {
            startFlashNotification(flashNotification);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: stopFlashNotification, reason: merged with bridge method [inline-methods] */
    public void lambda$startFlashNotificationSequence$0(String str) {
        Log.i(LOG_TAG, "stopFlashNotification: tag=" + str);
        synchronized (this.mFlashNotifications) {
            FlashNotification removeFlashNotificationLocked = removeFlashNotificationLocked(str);
            FlashNotification flashNotification = this.mCurrentFlashNotification;
            if (flashNotification != null && removeFlashNotificationLocked == flashNotification) {
                stopFlashNotificationLocked();
                startNextFlashNotificationLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareForCameraFlashNotification() {
        CameraManager cameraManager = (CameraManager) this.mContext.getSystemService(CameraManager.class);
        this.mCameraManager = cameraManager;
        if (cameraManager != null) {
            try {
                this.mCameraId = getCameraId();
            } catch (CameraAccessException e) {
                Log.e(LOG_TAG, "CameraAccessException", e);
            }
            this.mCameraManager.registerTorchCallback(this.mTorchCallback, (Handler) null);
        }
    }

    private String getCameraId() throws CameraAccessException {
        for (String str : this.mCameraManager.getCameraIdList()) {
            CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(str);
            Boolean bool = (Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (bool != null && num != null && bool.booleanValue() && num.intValue() == 1) {
                Log.d(LOG_TAG, "Found valid camera, cameraId=" + str);
                return str;
            }
        }
        return null;
    }

    private void showScreenNotificationOverlayView(int i) {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((FlashNotificationsController) obj).showScreenNotificationOverlayViewMainThread(((Integer) obj2).intValue());
            }
        }, this, Integer.valueOf(i)));
    }

    private void hideScreenNotificationOverlayView() {
        this.mMainHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FlashNotificationsController) obj).fadeOutScreenNotificationOverlayViewMainThread();
            }
        }, this));
        this.mMainHandler.sendMessageDelayed(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.accessibility.FlashNotificationsController$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((FlashNotificationsController) obj).hideScreenNotificationOverlayViewMainThread();
            }
        }, this), 210L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showScreenNotificationOverlayViewMainThread(int i) {
        Log.d(LOG_TAG, "showScreenNotificationOverlayViewMainThread");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2015, FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_LOCATION_PROVIDER, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.layoutInDisplayCutoutMode = 1;
        layoutParams.inputFeatures |= 1;
        if (this.mScreenFlashNotificationOverlayView == null) {
            this.mScreenFlashNotificationOverlayView = getScreenNotificationOverlayView(i);
            ((WindowManager) this.mContext.getSystemService(WindowManager.class)).addView(this.mScreenFlashNotificationOverlayView, layoutParams);
            fadeScreenNotificationOverlayViewMainThread(this.mScreenFlashNotificationOverlayView, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fadeOutScreenNotificationOverlayViewMainThread() {
        Log.d(LOG_TAG, "fadeOutScreenNotificationOverlayViewMainThread");
        View view = this.mScreenFlashNotificationOverlayView;
        if (view != null) {
            fadeScreenNotificationOverlayViewMainThread(view, false);
        }
    }

    private void fadeScreenNotificationOverlayViewMainThread(View view, boolean z) {
        float[] fArr = new float[2];
        fArr[0] = z ? 0.0f : 1.0f;
        fArr[1] = z ? 1.0f : 0.0f;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", fArr);
        ofFloat.setInterpolator(new AccelerateInterpolator());
        ofFloat.setAutoCancel(true);
        ofFloat.setDuration(200L);
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideScreenNotificationOverlayViewMainThread() {
        Log.d(LOG_TAG, "hideScreenNotificationOverlayViewMainThread");
        View view = this.mScreenFlashNotificationOverlayView;
        if (view != null) {
            view.setVisibility(8);
            ((WindowManager) this.mContext.getSystemService(WindowManager.class)).removeView(this.mScreenFlashNotificationOverlayView);
            this.mScreenFlashNotificationOverlayView = null;
        }
    }

    private View getScreenNotificationOverlayView(int i) {
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        frameLayout.setBackgroundColor(i);
        frameLayout.setAlpha(0.0f);
        return frameLayout;
    }

    private int getScreenFlashColorPreference(int i, String str) {
        return getScreenFlashColorPreference();
    }

    private int getScreenFlashColorPreference(int i) {
        return getScreenFlashColorPreference();
    }

    private int getScreenFlashColorPreference() {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), SETTING_KEY_SCREEN_FLASH_NOTIFICATION_COLOR, SCREEN_DEFAULT_COLOR_WITH_ALPHA, -2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFlashNotification(FlashNotification flashNotification) {
        int i = flashNotification.mType;
        String str = flashNotification.mTag;
        Log.i(LOG_TAG, "startFlashNotification: type=" + i + ", tag=" + str);
        if (!this.mIsCameraFlashNotificationEnabled && !this.mIsScreenFlashNotificationEnabled && !flashNotification.mForceStartScreenFlash) {
            Log.d(LOG_TAG, "Flash notification is disabled");
            return;
        }
        if (this.mIsCameraOpened) {
            Log.d(LOG_TAG, "Since camera for torch is opened, block notification.");
            return;
        }
        if (this.mIsCameraFlashNotificationEnabled && this.mCameraId == null) {
            prepareForCameraFlashNotification();
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            synchronized (this.mFlashNotifications) {
                if (i == 1 || i == 3) {
                    if (this.mCurrentFlashNotification != null) {
                        Log.i(LOG_TAG, "Default type of flash notification can not work because previous flash notification is working");
                    } else {
                        startFlashNotificationLocked(flashNotification);
                    }
                } else if (i == 2) {
                    if (this.mCurrentFlashNotification != null) {
                        removeFlashNotificationLocked(str);
                        stopFlashNotificationLocked();
                    }
                    this.mFlashNotifications.addFirst(flashNotification);
                    startNextFlashNotificationLocked();
                } else {
                    Log.e(LOG_TAG, "Unavailable flash notification type");
                }
            }
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    @GuardedBy({"mFlashNotifications"})
    private FlashNotification removeFlashNotificationLocked(String str) {
        ListIterator<FlashNotification> listIterator = this.mFlashNotifications.listIterator(0);
        while (listIterator.hasNext()) {
            FlashNotification next = listIterator.next();
            if (next != null && next.mTag.equals(str)) {
                listIterator.remove();
                next.tryUnlinkToDeath();
                Log.i(LOG_TAG, "removeFlashNotificationLocked: tag=" + next.mTag);
                return next;
            }
        }
        FlashNotification flashNotification = this.mCurrentFlashNotification;
        if (flashNotification == null || !flashNotification.mTag.equals(str)) {
            return null;
        }
        this.mCurrentFlashNotification.tryUnlinkToDeath();
        return this.mCurrentFlashNotification;
    }

    @GuardedBy({"mFlashNotifications"})
    private void stopFlashNotificationLocked() {
        if (this.mThread != null) {
            Log.i(LOG_TAG, "stopFlashNotificationLocked: tag=" + this.mThread.mFlashNotification.mTag);
            this.mThread.cancel();
            this.mThread = null;
        }
        doCameraFlashNotificationOff();
        doScreenFlashNotificationOff();
    }

    @GuardedBy({"mFlashNotifications"})
    private void startNextFlashNotificationLocked() {
        Log.i(LOG_TAG, "startNextFlashNotificationLocked");
        if (this.mFlashNotifications.size() <= 0) {
            this.mCurrentFlashNotification = null;
        } else {
            startFlashNotificationLocked(this.mFlashNotifications.getFirst());
        }
    }

    @GuardedBy({"mFlashNotifications"})
    private void startFlashNotificationLocked(FlashNotification flashNotification) {
        Log.i(LOG_TAG, "startFlashNotificationLocked: type=" + flashNotification.mType + ", tag=" + flashNotification.mTag);
        this.mCurrentFlashNotification = flashNotification;
        this.mThread = new FlashNotificationThread(flashNotification);
        this.mFlashNotificationHandler.post(this.mThread);
    }

    private boolean isDozeMode() {
        int i = this.mDisplayState;
        return i == 3 || i == 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCameraFlashNotificationOn() {
        if (this.mIsCameraFlashNotificationEnabled && !this.mIsTorchOn) {
            doCameraFlashNotification(true);
        }
        Log.i(LOG_TAG, "doCameraFlashNotificationOn: isCameraFlashNotificationEnabled=" + this.mIsCameraFlashNotificationEnabled + ", isTorchOn=" + this.mIsTorchOn + ", isTorchTouched=" + this.mIsTorchTouched);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doCameraFlashNotificationOff() {
        if (this.mIsTorchTouched) {
            doCameraFlashNotification(false);
        }
        Log.i(LOG_TAG, "doCameraFlashNotificationOff: isCameraFlashNotificationEnabled=" + this.mIsCameraFlashNotificationEnabled + ", isTorchOn=" + this.mIsTorchOn + ", isTorchTouched=" + this.mIsTorchTouched);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doScreenFlashNotificationOn(int i, boolean z) {
        boolean isDozeMode = isDozeMode();
        if ((this.mIsScreenFlashNotificationEnabled || z) && !isDozeMode) {
            showScreenNotificationOverlayView(i);
        }
        Log.i(LOG_TAG, "doScreenFlashNotificationOn: isScreenFlashNotificationEnabled=" + this.mIsScreenFlashNotificationEnabled + ", isDozeMode=" + isDozeMode + ", color=" + Integer.toHexString(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doScreenFlashNotificationOff() {
        hideScreenNotificationOverlayView();
        Log.i(LOG_TAG, "doScreenFlashNotificationOff: isScreenFlashNotificationEnabled=" + this.mIsScreenFlashNotificationEnabled);
    }

    private void doCameraFlashNotification(boolean z) {
        String str;
        Log.d(LOG_TAG, "doCameraFlashNotification: " + z + " mCameraId : " + this.mCameraId);
        CameraManager cameraManager = this.mCameraManager;
        if (cameraManager != null && (str = this.mCameraId) != null) {
            try {
                cameraManager.setTorchMode(str, z);
                this.mIsTorchTouched = z;
                return;
            } catch (CameraAccessException e) {
                Log.e(LOG_TAG, "Failed to setTorchMode: " + e);
                return;
            }
        }
        Log.e(LOG_TAG, "Can not use camera flash notification, please check CameraManager!");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class FlashNotification {
        private final int mColor;
        private final IBinder.DeathRecipient mDeathRecipient;
        private final boolean mForceStartScreenFlash;
        private final int mOffDuration;
        private final int mOnDuration;
        private int mRepeat;
        private final String mTag;
        private final IBinder mToken;
        private final int mType;

        private FlashNotification(String str, int i, int i2) {
            this(str, i, i2, null, null);
        }

        private FlashNotification(String str, int i, int i2, IBinder iBinder, IBinder.DeathRecipient deathRecipient) {
            this.mType = i;
            this.mTag = str;
            this.mColor = i2;
            this.mToken = iBinder;
            this.mDeathRecipient = deathRecipient;
            if (i == 2) {
                this.mOnDuration = ProcessList.PREVIOUS_APP_ADJ;
                this.mOffDuration = ProcessList.PREVIOUS_APP_ADJ;
                this.mRepeat = 0;
                this.mForceStartScreenFlash = false;
                return;
            }
            if (i == 3) {
                this.mOnDuration = 5000;
                this.mOffDuration = 1000;
                this.mRepeat = 1;
                this.mForceStartScreenFlash = true;
                return;
            }
            this.mOnDuration = 350;
            this.mOffDuration = 250;
            this.mRepeat = 2;
            this.mForceStartScreenFlash = false;
        }

        boolean tryLinkToDeath() {
            IBinder.DeathRecipient deathRecipient;
            IBinder iBinder = this.mToken;
            if (iBinder != null && (deathRecipient = this.mDeathRecipient) != null) {
                try {
                    iBinder.linkToDeath(deathRecipient, 0);
                    return true;
                } catch (RemoteException e) {
                    Log.e(FlashNotificationsController.LOG_TAG, "RemoteException", e);
                }
            }
            return false;
        }

        boolean tryUnlinkToDeath() {
            IBinder.DeathRecipient deathRecipient;
            IBinder iBinder = this.mToken;
            if (iBinder != null && (deathRecipient = this.mDeathRecipient) != null) {
                try {
                    iBinder.unlinkToDeath(deathRecipient, 0);
                    return true;
                } catch (Exception unused) {
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class FlashNotificationThread extends Thread {
        private int mColor;
        private final FlashNotification mFlashNotification;
        private boolean mForceStop;
        private boolean mShouldDoCameraFlash;
        private boolean mShouldDoScreenFlash;

        private FlashNotificationThread(FlashNotification flashNotification) {
            this.mColor = 0;
            this.mShouldDoScreenFlash = false;
            this.mShouldDoCameraFlash = false;
            this.mFlashNotification = flashNotification;
            this.mForceStop = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Log.d(FlashNotificationsController.LOG_TAG, "run started: " + this.mFlashNotification.mTag);
            Process.setThreadPriority(-8);
            int i = this.mFlashNotification.mColor;
            this.mColor = i;
            this.mShouldDoScreenFlash = Color.alpha(i) != 0 || this.mFlashNotification.mForceStartScreenFlash;
            this.mShouldDoCameraFlash = this.mFlashNotification.mType != 3;
            synchronized (this) {
                FlashNotificationsController.this.mWakeLock.acquire(300000L);
                try {
                    startFlashNotification();
                } finally {
                    FlashNotificationsController.this.doScreenFlashNotificationOff();
                    FlashNotificationsController.this.doCameraFlashNotificationOff();
                    try {
                        FlashNotificationsController.this.mWakeLock.release();
                    } catch (RuntimeException unused) {
                        Log.e(FlashNotificationsController.LOG_TAG, "Error while releasing FlashNotificationsController wakelock (already released by the system?)");
                    }
                }
            }
            synchronized (FlashNotificationsController.this.mFlashNotifications) {
                if (FlashNotificationsController.this.mThread == this) {
                    FlashNotificationsController.this.mThread = null;
                }
                if (!this.mForceStop) {
                    this.mFlashNotification.tryUnlinkToDeath();
                    FlashNotificationsController.this.mCurrentFlashNotification = null;
                }
            }
            Log.d(FlashNotificationsController.LOG_TAG, "run finished: " + this.mFlashNotification.mTag);
        }

        private void startFlashNotification() {
            synchronized (this) {
                while (!this.mForceStop) {
                    if (this.mFlashNotification.mType != 2 && this.mFlashNotification.mRepeat >= 0) {
                        FlashNotification flashNotification = this.mFlashNotification;
                        int i = flashNotification.mRepeat;
                        flashNotification.mRepeat = i - 1;
                        if (i == 0) {
                            break;
                        }
                    }
                    if (this.mShouldDoScreenFlash) {
                        FlashNotificationsController.this.doScreenFlashNotificationOn(this.mColor, this.mFlashNotification.mForceStartScreenFlash);
                    }
                    if (this.mShouldDoCameraFlash) {
                        FlashNotificationsController.this.doCameraFlashNotificationOn();
                    }
                    delay(this.mFlashNotification.mOnDuration);
                    FlashNotificationsController.this.doScreenFlashNotificationOff();
                    FlashNotificationsController.this.doCameraFlashNotificationOff();
                    if (this.mForceStop) {
                        break;
                    } else {
                        delay(this.mFlashNotification.mOffDuration);
                    }
                }
            }
        }

        void cancel() {
            Log.d(FlashNotificationsController.LOG_TAG, "run canceled: " + this.mFlashNotification.mTag);
            synchronized (this) {
                FlashNotificationsController.this.mThread.mForceStop = true;
                FlashNotificationsController.this.mThread.notify();
            }
        }

        private void delay(long j) {
            if (j > 0) {
                long uptimeMillis = SystemClock.uptimeMillis() + j;
                do {
                    try {
                        wait(j);
                    } catch (InterruptedException unused) {
                    }
                    if (this.mForceStop) {
                        return;
                    } else {
                        j = uptimeMillis - SystemClock.uptimeMillis();
                    }
                } while (j > 0);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    class FlashBroadcastReceiver extends BroadcastReceiver {
        FlashBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                if (UserHandle.myUserId() != ActivityManager.getCurrentUser()) {
                    return;
                }
                FlashNotificationsController flashNotificationsController = FlashNotificationsController.this;
                flashNotificationsController.mIsCameraFlashNotificationEnabled = Settings.System.getIntForUser(flashNotificationsController.mContext.getContentResolver(), FlashNotificationsController.SETTING_KEY_CAMERA_FLASH_NOTIFICATION, 0, -2) != 0;
                if (FlashNotificationsController.this.mIsCameraFlashNotificationEnabled) {
                    FlashNotificationsController.this.prepareForCameraFlashNotification();
                } else if (FlashNotificationsController.this.mCameraManager != null) {
                    FlashNotificationsController.this.mCameraManager.unregisterTorchCallback(FlashNotificationsController.this.mTorchCallback);
                }
                AudioManager audioManager = (AudioManager) FlashNotificationsController.this.mContext.getSystemService(AudioManager.class);
                if (audioManager != null) {
                    audioManager.registerAudioPlaybackCallback(FlashNotificationsController.this.mAudioPlaybackCallback, FlashNotificationsController.this.mCallbackHandler);
                }
                FlashNotificationsController flashNotificationsController2 = FlashNotificationsController.this;
                flashNotificationsController2.mCameraManager = (CameraManager) flashNotificationsController2.mContext.getSystemService(CameraManager.class);
                CameraManager cameraManager = FlashNotificationsController.this.mCameraManager;
                FlashNotificationsController flashNotificationsController3 = FlashNotificationsController.this;
                cameraManager.registerAvailabilityCallback(flashNotificationsController3.mTorchAvailabilityCallback, flashNotificationsController3.mCallbackHandler);
                return;
            }
            if (FlashNotificationsController.ACTION_FLASH_NOTIFICATION_START_PREVIEW.equals(intent.getAction())) {
                Log.i(FlashNotificationsController.LOG_TAG, "ACTION_FLASH_NOTIFICATION_START_PREVIEW");
                int intExtra = intent.getIntExtra(FlashNotificationsController.EXTRA_FLASH_NOTIFICATION_PREVIEW_COLOR, 0);
                int intExtra2 = intent.getIntExtra(FlashNotificationsController.EXTRA_FLASH_NOTIFICATION_PREVIEW_TYPE, 0);
                if (intExtra2 == 1) {
                    FlashNotificationsController.this.startFlashNotificationLongPreview(intExtra);
                    return;
                } else {
                    if (intExtra2 == 0) {
                        FlashNotificationsController.this.startFlashNotificationShortPreview();
                        return;
                    }
                    return;
                }
            }
            if (FlashNotificationsController.ACTION_FLASH_NOTIFICATION_STOP_PREVIEW.equals(intent.getAction())) {
                Log.i(FlashNotificationsController.LOG_TAG, "ACTION_FLASH_NOTIFICATION_STOP_PREVIEW");
                FlashNotificationsController.this.stopFlashNotificationLongPreview();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class FlashContentObserver extends ContentObserver {
        private final Uri mCameraFlashNotificationUri;
        private final Uri mScreenFlashNotificationUri;

        FlashContentObserver(Handler handler) {
            super(handler);
            this.mCameraFlashNotificationUri = Settings.System.getUriFor(FlashNotificationsController.SETTING_KEY_CAMERA_FLASH_NOTIFICATION);
            this.mScreenFlashNotificationUri = Settings.System.getUriFor(FlashNotificationsController.SETTING_KEY_SCREEN_FLASH_NOTIFICATION);
        }

        void register(ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.mCameraFlashNotificationUri, false, this, -1);
            contentResolver.registerContentObserver(this.mScreenFlashNotificationUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (this.mCameraFlashNotificationUri.equals(uri)) {
                FlashNotificationsController flashNotificationsController = FlashNotificationsController.this;
                flashNotificationsController.mIsCameraFlashNotificationEnabled = Settings.System.getIntForUser(flashNotificationsController.mContext.getContentResolver(), FlashNotificationsController.SETTING_KEY_CAMERA_FLASH_NOTIFICATION, 0, -2) != 0;
                if (FlashNotificationsController.this.mIsCameraFlashNotificationEnabled) {
                    FlashNotificationsController.this.prepareForCameraFlashNotification();
                    return;
                }
                FlashNotificationsController.this.mIsTorchOn = false;
                if (FlashNotificationsController.this.mCameraManager != null) {
                    FlashNotificationsController.this.mCameraManager.unregisterTorchCallback(FlashNotificationsController.this.mTorchCallback);
                    return;
                }
                return;
            }
            if (this.mScreenFlashNotificationUri.equals(uri)) {
                FlashNotificationsController flashNotificationsController2 = FlashNotificationsController.this;
                flashNotificationsController2.mIsScreenFlashNotificationEnabled = Settings.System.getIntForUser(flashNotificationsController2.mContext.getContentResolver(), FlashNotificationsController.SETTING_KEY_SCREEN_FLASH_NOTIFICATION, 0, -2) != 0;
            }
        }
    }
}
