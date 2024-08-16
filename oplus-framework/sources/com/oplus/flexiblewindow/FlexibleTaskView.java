package com.oplus.flexiblewindow;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.Application;
import android.app.OplusActivityTaskManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.HardwareBuffer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Slog;
import android.util.SparseArray;
import android.view.FlexibleTaskExtraViewManager;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceSession;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.window.TaskSnapshot;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer;
import java.util.HashMap;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public class FlexibleTaskView extends SurfaceView implements SurfaceHolder.Callback, EmbeddedWindowTaskOrganizer.TaskListener, Application.ActivityLifecycleCallbacks {
    private static final String DARK_BACKGROUND_COLOR = "#FF191919";
    private static final float DELTA = 0.01f;
    public static final String KEY_ALLOW_TASK_DETACH_FROM_EMBEDDING = "allow_task_detach_from_embedding";
    public static final String KEY_CORNE_RADIUS = "cornerRadius";
    public static final String KEY_FLEXIBLE_EMBEDDING = "flexible_embedding";
    public static final String KEY_INTENT = "intent";
    public static final String KEY_INTERCEPT_INPUT_EVENT = "intercept_input_event";
    public static final String KEY_LAUNCH_BOUNDS = "launchBounds";
    public static final String KEY_NEED_ROTATE_TASK_LEASH = "need_rotate_task_leash";
    public static final String KEY_SCENARIO = "scenario";
    public static final String KEY_SHADOW_RADIUS = "shadowRadius";
    public static final String KEY_SHOW_SURFACE_CORNER_RADIUS = "show_surface_corner_radius";
    public static final String KEY_SUPER_LOCK = "super_locked";
    public static final String KEY_TASK_ID = "taskId";
    public static final String KEY_USER_BACKGROUND_COLOR = "use_default_background_color";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_ZORDER_ON_TOP = "zorder_on_top";
    private static final int MATRIX_LENGTH = 9;
    private static final float ONE = 1.0f;
    private static final int REORDER_MOVE_TO_ORIGINAL_POSITION = 2;
    private static final int ROTATION_ANGLE_90 = 90;
    private static final float ZERO = 0.0f;
    private boolean mAllowTaskDetachFromEmbedding;
    private int mBackgroundColor;
    private float mCornerRadius;
    private boolean mEnforceStart;
    private Executor mExecutor;
    private View mExtraViewLayout;
    private FlexibleTaskExtraViewManager mExtraViewManager;
    private FlexibleOnComputeInternalInsetsListener mFlexibleOnComputeInternalInsetsListener;
    private Intent mIntent;
    private boolean mInterceptInputEvent;
    private Rect mLaunchBounds;
    private WindowManager.LayoutParams mLayoutParams;
    private Rect mLayoutRect;
    private Listener mListener;
    private Executor mListenerExecutor;
    private final Object mLock;
    private boolean mNeedRotateTaskLeash;
    private boolean mNeedZoderOnTop;
    private boolean mPaused;
    private Region mRegion;
    private int mScenario;
    private float mShadowRadius;
    private boolean mShowSurfaceCornerRadius;
    private boolean mStartEmbedd;
    private boolean mSuperLocked;
    private boolean mSurfaceCreated;
    private SparseArray<TaskFlexibleFrameInfo> mTaskFlexibleFrames;
    private int mTaskId;
    private ActivityManager.RunningTaskInfo mTaskInfo;
    private SurfaceControl mTaskLeash;
    private WindowContainerToken mTaskToken;
    private final int[] mTmpLocation;
    private final Rect mTmpRect;
    private final Rect mTmpRootRect;
    private SurfaceControl.Transaction mTransaction;
    private boolean mUserDefaultBackgroudColor;
    private int mUserId;
    private int mWhiteColor;
    private static final String TAG = FlexibleTaskView.class.getSimpleName();
    private static final HashMap<Context, EmbeddedWindowTaskOrganizer> mTaskOrganizerMap = new HashMap<>();
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);

    public void setEnforceStart(boolean mEnforceStart) {
        this.mEnforceStart = mEnforceStart;
    }

    public FlexibleTaskView(Context context) {
        this(context, null);
    }

    public FlexibleTaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexibleTaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FlexibleTaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes, true);
        this.mLock = new Object();
        this.mTmpRect = new Rect();
        this.mTmpRootRect = new Rect();
        this.mTmpLocation = new int[2];
        this.mTransaction = new SurfaceControl.Transaction();
        this.mTaskFlexibleFrames = new SparseArray<>();
        this.mTaskId = -1;
        this.mBackgroundColor = Color.argb(0.1f, 1.0f, 1.0f, 1.0f);
        this.mWhiteColor = Color.argb(1.0f, 1.0f, 1.0f, 1.0f);
        this.mRegion = new Region();
        this.mLayoutRect = new Rect();
        this.mPaused = false;
        this.mFlexibleOnComputeInternalInsetsListener = new FlexibleOnComputeInternalInsetsListener();
        this.mSuperLocked = false;
        this.mNeedZoderOnTop = true;
        this.mInterceptInputEvent = false;
        this.mUserDefaultBackgroudColor = true;
        this.mNeedRotateTaskLeash = false;
        this.mStartEmbedd = false;
        this.mAllowTaskDetachFromEmbedding = false;
        this.mEnforceStart = false;
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("flexibleTaskView may only be run on Looper threads");
        }
        this.mExecutor = new HandlerExecutor(new Handler(Looper.myLooper()));
    }

    public void init(Bundle bundle) {
        if (bundle == null) {
            throw new IllegalArgumentException("Expected non-null bundle params");
        }
        int scenario = bundle.getInt(KEY_SCENARIO, 0);
        float cornerRadius = bundle.getFloat(KEY_CORNE_RADIUS, 0.0f);
        float shadowRadius = bundle.getFloat(KEY_SHADOW_RADIUS, 0.0f);
        Rect launchBounds = (Rect) bundle.getParcelable(KEY_LAUNCH_BOUNDS, Rect.class);
        Intent intent = (Intent) bundle.getParcelable(KEY_INTENT, Intent.class);
        int taskId = bundle.getInt(KEY_TASK_ID, -1);
        int userId = bundle.getInt(KEY_USER_ID, getContext().getUserId());
        this.mAllowTaskDetachFromEmbedding = bundle.getBoolean(KEY_ALLOW_TASK_DETACH_FROM_EMBEDDING, false);
        this.mSuperLocked = bundle.getBoolean(KEY_SUPER_LOCK, false);
        this.mNeedZoderOnTop = bundle.getBoolean(KEY_ZORDER_ON_TOP, true);
        this.mInterceptInputEvent = bundle.getBoolean(KEY_INTERCEPT_INPUT_EVENT, false);
        this.mUserDefaultBackgroudColor = bundle.getBoolean(KEY_USER_BACKGROUND_COLOR, true);
        this.mShowSurfaceCornerRadius = bundle.getBoolean(KEY_SHOW_SURFACE_CORNER_RADIUS, false);
        this.mNeedRotateTaskLeash = bundle.getBoolean(KEY_NEED_ROTATE_TASK_LEASH, false);
        if (DEBUG) {
            Slog.d(TAG, " bundle " + bundle.toString());
        }
        boolean z = this.mSuperLocked;
        if (z) {
            this.mStartEmbedd = z;
        }
        init(scenario, cornerRadius, shadowRadius, launchBounds, intent, taskId, userId);
    }

    public void init(int scenario, float cornerRadius, float shadowRadius, Rect launchBounds, Intent intent, int userId) {
        if (intent == null) {
            throw new IllegalArgumentException("Expected non-null intent params");
        }
        init(scenario, cornerRadius, shadowRadius, launchBounds, intent, -1, userId);
    }

    public void init(int scenario, float cornerRadius, float shadowRadius, Rect launchBounds, int taskId) {
        if (taskId <= -1) {
            throw new IllegalArgumentException("Expected valid taskId params");
        }
        init(scenario, cornerRadius, shadowRadius, launchBounds, null, taskId, getContext().getUserId());
    }

    public void init(int scenario, float cornerRadius, float shadowRadius, Rect launchBounds, Intent intent, int taskId, int userId) {
        if (scenario <= 0) {
            throw new IllegalArgumentException("Expected greater than zero scenario params");
        }
        if (intent == null && taskId <= -1) {
            throw new IllegalArgumentException("Expected intent or taskId params should be valid");
        }
        if (launchBounds == null || launchBounds.isEmpty()) {
            throw new IllegalArgumentException("Expected non-empty launchBounds params");
        }
        this.mScenario = scenario;
        this.mCornerRadius = cornerRadius;
        this.mShadowRadius = shadowRadius;
        this.mLaunchBounds = new Rect(launchBounds);
        this.mTaskFlexibleFrames.clear();
        this.mIntent = intent;
        this.mTaskId = taskId;
        this.mUserId = userId;
        if (this.mUserDefaultBackgroudColor) {
            setBackgroundColor(this.mWhiteColor);
        }
        setUseAlpha();
        setCornerRadius(cornerRadius);
        setTaskViewCornerRadius(cornerRadius);
        if (!this.mShowSurfaceCornerRadius) {
            getWrapper().getExtImpl().setShowSurfaceCornerRadius(false);
        }
        getHolder().addCallback(this);
        getHolder().setFormat(-3);
    }

    public void setShadowRadius(float shadowRadius) {
        this.mShadowRadius = shadowRadius;
        this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                FlexibleTaskView.this.lambda$setShadowRadius$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setShadowRadius$0() {
        if (isSurfaceControlValid()) {
            this.mTransaction.setShadowRadius(getSurfaceControl(), this.mShadowRadius).apply();
        }
    }

    private boolean isSurfaceControlValid() {
        boolean z;
        synchronized (this.mLock) {
            z = getSurfaceControl() != null && getSurfaceControl().isValid();
        }
        return z;
    }

    public void setListener(Executor executor, Listener listener) {
        if (this.mListener != null) {
            throw new IllegalStateException("Trying to set a listener when one has already been set");
        }
        this.mListener = listener;
        this.mListenerExecutor = executor;
    }

    private void startActivity(Intent fillInIntent, Bundle options) {
        try {
            fillInIntent.addFlags(65536);
            getContext().startActivityAsUser(fillInIntent, options, new UserHandle(this.mUserId));
            if (DEBUG) {
                Slog.d(TAG, "startActivity intent=" + fillInIntent);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startActivityFromRecents(int taskId, Bundle options) {
        try {
            ActivityTaskManager.getService().startActivityFromRecents(taskId, options);
            if (DEBUG) {
                Slog.d(TAG, "startActivityFromRecents taskId=" + taskId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ActivityOptions prepareActivityOptions() {
        final Binder launchCookie = new Binder();
        this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                FlexibleTaskView.this.lambda$prepareActivityOptions$1(launchCookie);
            }
        });
        ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), 0, 0);
        options.setLaunchCookie(launchCookie);
        this.mLaunchBounds.offsetTo(0, 0);
        options.setLaunchBounds(this.mLaunchBounds);
        options.setLaunchDisplayId(getContext().getDisplayId());
        Bundle bundle = new Bundle();
        bundle.putInt(FlexibleWindowManager.KEY_LAUNCH_TASK_SCENARIO, this.mScenario);
        bundle.putInt(FlexibleWindowManager.KEY_LAUNCH_CONTAINER_TASK_ID, unwrap(getContext()).getTaskId());
        bundle.putBoolean(FlexibleWindowManager.KEY_LAUNCH_TASK_EMBEDDED, true);
        bundle.putBoolean(FlexibleWindowManager.KEY_ACTIVITY_NO_ANIM, true);
        bundle.putBoolean(KEY_INTERCEPT_INPUT_EVENT, this.mInterceptInputEvent);
        bundle.putBoolean(KEY_ALLOW_TASK_DETACH_FROM_EMBEDDING, this.mAllowTaskDetachFromEmbedding);
        bundle.putBoolean(KEY_SUPER_LOCK, this.mSuperLocked);
        options.setExtraBundle(bundle);
        return options;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareActivityOptions$1(Binder launchCookie) {
        if (getTaskOrganizer() != null) {
            getTaskOrganizer().setPendingLaunchCookieListener(launchCookie, this);
        }
    }

    private EmbeddedWindowTaskOrganizer getTaskOrganizer() {
        return mTaskOrganizerMap.get(getContext());
    }

    public Rect getLaunchBounds() {
        return this.mLaunchBounds;
    }

    public void setFlexibleFrame() {
        if (this.mTaskInfo == null) {
            if (DEBUG) {
                Slog.d(TAG, "no need set flexible frame when task is null");
            }
        } else if (!this.mSurfaceCreated) {
            if (DEBUG) {
                Slog.d(TAG, "no need set flexible frame when surface is destroyed");
            }
        } else {
            Rect frame = calculateFrame();
            Rect handleFrame = calculateHandleFrame(frame);
            if (DEBUG) {
                Slog.d(TAG, "set flexible frame for task=" + this.mTaskInfo.taskId + ", frame=" + frame + ", lastReportedFrame=" + (this.mTaskFlexibleFrames.get(this.mTaskInfo.taskId) == null ? null : this.mTaskFlexibleFrames.get(this.mTaskInfo.taskId).getLastReportedFrame()) + ", handleFrame=" + handleFrame + ", mLastReportedHandleFrame=" + (this.mTaskFlexibleFrames.get(this.mTaskInfo.taskId) != null ? this.mTaskFlexibleFrames.get(this.mTaskInfo.taskId).getLastReportedHandleFrame() : null));
            }
            setFlexibleFrameBundle(frame, handleFrame);
        }
    }

    private boolean isTaskMatchView() {
        int launchBoundsWidth = this.mLaunchBounds.width();
        int taskViewWidth = getWidth();
        float scaleX = taskViewWidth / launchBoundsWidth;
        int launchBoundsHeight = this.mLaunchBounds.height();
        int taskViewHeight = getHeight();
        float scaleY = taskViewHeight / launchBoundsHeight;
        return Math.abs(scaleX - scaleY) < DELTA;
    }

    private Rect calculateFrame() {
        getBoundsOnScreen(this.mTmpRect);
        int launchBoundsWidth = this.mLaunchBounds.width();
        int taskViewWidth = this.mTmpRect.width();
        float scaleX = taskViewWidth / launchBoundsWidth;
        int launchBoundsHeight = this.mLaunchBounds.height();
        int taskViewHeight = this.mTmpRect.height();
        float scaleY = taskViewHeight / launchBoundsHeight;
        Rect frame = new Rect();
        if (Math.abs(scaleX - scaleY) >= DELTA) {
            float width = scaleX <= scaleY ? taskViewWidth : (this.mLaunchBounds.width() * taskViewHeight) / launchBoundsHeight;
            float height = scaleX <= scaleY ? (this.mLaunchBounds.height() * taskViewWidth) / launchBoundsWidth : taskViewHeight;
            float tx = (taskViewWidth - width) / 2.0f;
            float ty = (taskViewHeight - height) / 2.0f;
            frame.set((int) (this.mTmpRect.left + tx), (int) (this.mTmpRect.top + ty), (int) (this.mTmpRect.left + tx + width), (int) (this.mTmpRect.top + ty + height));
        } else {
            frame.set(this.mTmpRect);
        }
        return frame;
    }

    private Rect calculateHandleFrame(Rect frame) {
        Region region;
        Rect handleFrame = new Rect();
        if (this.mExtraViewLayout != null && (region = this.mRegion) != null) {
            Rect touchBounds = region.getBounds();
            int taskWidth = getWidth();
            float scale = (this.mTmpRect.width() * 1.0f) / taskWidth;
            handleFrame.set(frame.centerX() - ((int) ((touchBounds.width() / 2) * scale)), frame.top, frame.centerX() + ((int) ((touchBounds.width() / 2) * scale)), frame.top + ((int) (touchBounds.height() * scale)));
            if (this.mExtraViewLayout.getVisibility() != 0) {
                handleFrame.setEmpty();
            }
        }
        return handleFrame;
    }

    private void setFlexibleFrameBundle(Rect frame, Rect handleFrame) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        if (runningTaskInfo == null) {
            return;
        }
        TaskFlexibleFrameInfo info = this.mTaskFlexibleFrames.get(runningTaskInfo.taskId);
        if (info == null || !frame.equals(info.getLastReportedFrame()) || !handleFrame.equals(info.getLastReportedHandleFrame())) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(FlexibleWindowManager.KEY_TASK_FRAME, frame);
            bundle.putParcelable(FlexibleWindowManager.KEY_TASK_HANDLE_FRAME, handleFrame);
            FlexibleWindowManager.getInstance().setFlexibleFrame(this.mTaskInfo.taskId, bundle);
            if (info == null) {
                TaskFlexibleFrameInfo frameInfo = new TaskFlexibleFrameInfo(this.mTaskInfo.taskId, frame, handleFrame);
                this.mTaskFlexibleFrames.put(this.mTaskInfo.taskId, frameInfo);
            } else {
                info.setLastReportedFrame(frame);
                info.setLastReportedHandleFrame(handleFrame);
            }
        }
    }

    public void setExtraViewInfo(View layout, WindowManager.LayoutParams lp, Region region) {
        this.mExtraViewLayout = layout;
        this.mLayoutParams = lp;
        this.mRegion = region;
    }

    public void resize(Rect bounds) {
        if (DEBUG) {
            Slog.d(TAG, "resize for mTaskToken=" + this.mTaskToken + ", bounds:" + bounds + " mLaunchBounds:" + this.mLaunchBounds + " layoutRect:" + new Rect(this.mLeft, this.mTop, this.mRight, this.mBottom) + " mLayoutRect:" + this.mLayoutRect);
        }
        if (this.mTaskToken == null || bounds == null || bounds.isEmpty()) {
            return;
        }
        if (unwrap(getContext()).isFinishing()) {
            Slog.d(TAG, "resize return when activity is finishing " + getContext());
            return;
        }
        boolean isChanged = !bounds.equals(this.mLaunchBounds);
        if (isChanged) {
            WindowContainerTransaction wct = new WindowContainerTransaction();
            bounds.offsetTo(0, 0);
            wct.setBounds(this.mTaskToken, bounds);
            this.mLaunchBounds = new Rect(bounds);
            if (getTaskOrganizer() != null) {
                getTaskOrganizer().applyTransaction(wct);
            }
        }
        Rect layoutRect = new Rect(this.mLeft, this.mTop, this.mRight, this.mBottom);
        if (isChanged || !layoutRect.equals(this.mLayoutRect)) {
            if (getSurfaceControl() != null && getSurfaceControl().isValid()) {
                reparent();
            }
            this.mLayoutRect.set(layoutRect);
        }
        setExtraView();
    }

    public void release() {
        performRelease();
    }

    protected void finalize() throws Throwable {
        try {
            performRelease();
        } finally {
            super.finalize();
        }
    }

    @Override // com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer.TaskListener
    public void onTaskAppeared(final ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
        if (taskInfo == null) {
            return;
        }
        boolean shouldResetBackground = taskInfo.isVisible && isTaskMatchView();
        Rect appBounds = new Rect();
        this.mTaskInfo = taskInfo;
        this.mTaskToken = taskInfo.token;
        this.mTaskLeash = leash;
        this.mTaskId = taskInfo.taskId;
        if (this.mSuperLocked && taskInfo.configuration != null && taskInfo.configuration.windowConfiguration != null && (appBounds = taskInfo.configuration.windowConfiguration.getAppBounds()) != null && appBounds.width() > appBounds.height()) {
            this.mLaunchBounds = appBounds;
            this.mNeedRotateTaskLeash = true;
        }
        if (DEBUG) {
            Slog.d(TAG, "onTaskAppeared: taskId=" + taskInfo.taskId + ", mTaskToken=" + this.mTaskToken + ", mTaskLeash=" + this.mTaskLeash + ", mSurfaceCreated=" + this.mSurfaceCreated + ",appBounds=" + appBounds + ", this=" + this);
        }
        if (this.mSurfaceCreated) {
            if (DEBUG) {
                Slog.d(TAG, "onTaskAppeared: reparent task when surface is created, this=" + this);
            }
            reparent();
            if (shouldResetBackground) {
                post(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        FlexibleTaskView.this.lambda$onTaskAppeared$2();
                    }
                });
            }
        } else {
            updateTaskVisibility();
        }
        if (taskInfo.taskDescription != null && this.mUserDefaultBackgroudColor) {
            post(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$onTaskAppeared$3(taskInfo);
                }
            });
        }
        if (this.mListener != null) {
            final int taskId = taskInfo.taskId;
            final ComponentName baseActivity = taskInfo.baseActivity;
            this.mListenerExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$onTaskAppeared$4(taskId, baseActivity);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$2() {
        setBackground(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$3(ActivityManager.RunningTaskInfo taskInfo) {
        setResizeBackgroundColor(taskInfo.taskDescription.getBackgroundColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskAppeared$4(int taskId, ComponentName baseActivity) {
        this.mListener.onTaskCreated(taskId, baseActivity);
    }

    @Override // com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer.TaskListener
    public void onTaskVanished(ActivityManager.RunningTaskInfo taskInfo) {
        if (DEBUG) {
            Slog.d(TAG, "onTaskVanished: taskId=" + taskInfo.taskId + ", mTaskToken=" + this.mTaskToken + ", taskInfo.token=" + taskInfo.token + ", this=" + this);
        }
        this.mTaskId = -1;
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken == null || !windowContainerToken.equals(taskInfo.token)) {
            return;
        }
        if (this.mListener != null) {
            final int taskId = taskInfo.taskId;
            this.mListenerExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$onTaskVanished$5(taskId);
                }
            });
        }
        this.mTransaction.reparent(this.mTaskLeash, null).setScale(this.mTaskLeash, 1.0f, 1.0f).apply();
        resetTaskInfo();
        FlexibleTaskExtraViewManager flexibleTaskExtraViewManager = this.mExtraViewManager;
        if (flexibleTaskExtraViewManager != null) {
            flexibleTaskExtraViewManager.release();
            this.mExtraViewManager = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskVanished$5(int taskId) {
        this.mListener.onTaskRemovalStarted(taskId);
    }

    @Override // com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer.TaskListener
    public void onTaskInfoChanged(final ActivityManager.RunningTaskInfo taskInfo, SurfaceControl leash) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        if ((runningTaskInfo == null || !runningTaskInfo.isVisible) && taskInfo.isVisible && isTaskMatchView()) {
            post(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$onTaskInfoChanged$6();
                }
            });
        }
        this.mTaskInfo = taskInfo;
        if (taskInfo.taskDescription != null && this.mUserDefaultBackgroudColor) {
            post(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$onTaskInfoChanged$7(taskInfo);
                }
            });
        }
        final Rect bounds = taskInfo.configuration.windowConfiguration.getBounds();
        if (DEBUG) {
            Slog.d(TAG, "onTaskInfoChanged: taskId=" + taskInfo.taskId + ", mSurfaceCreated=" + this.mSurfaceCreated + ", isVisible=" + taskInfo.isVisible + ", mLaunchBounds=" + this.mLaunchBounds + ", bounds=" + bounds + ", this=" + this);
        }
        if (this.mSurfaceCreated && taskInfo.isVisible) {
            if (!this.mLaunchBounds.equals(bounds) || this.mSuperLocked) {
                this.mLaunchBounds.set(bounds);
                setFlexibleFrame();
                if (this.mListener != null) {
                    final int taskId = taskInfo.taskId;
                    final ComponentName baseActivity = taskInfo.baseActivity;
                    this.mListenerExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            FlexibleTaskView.this.lambda$onTaskInfoChanged$8(taskId, baseActivity, bounds);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskInfoChanged$6() {
        setBackground(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskInfoChanged$7(ActivityManager.RunningTaskInfo taskInfo) {
        setResizeBackgroundColor(taskInfo.taskDescription.getBackgroundColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTaskInfoChanged$8(int taskId, ComponentName baseActivity, Rect bounds) {
        this.mListener.onTaskChanged(taskId, baseActivity, new Rect(bounds));
    }

    @Override // com.oplus.flexiblewindow.EmbeddedWindowTaskOrganizer.TaskListener
    public void onBackPressedOnTaskRoot(ActivityManager.RunningTaskInfo taskInfo) {
        if (DEBUG) {
            Slog.d(TAG, "onBackPressedOnTaskRoot: taskId=" + taskInfo.taskId + ", mTaskToken=" + this.mTaskToken + ", taskInfo.token=" + taskInfo.token + ", this=" + this);
        }
        WindowContainerToken windowContainerToken = this.mTaskToken;
        if (windowContainerToken != null && windowContainerToken.equals(taskInfo.token) && this.mListener != null) {
            final int taskId = taskInfo.taskId;
            this.mListenerExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$onBackPressedOnTaskRoot$9(taskId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBackPressedOnTaskRoot$9(int taskId) {
        this.mListener.onBackPressedOnTaskRoot(taskId);
    }

    @Override // android.view.View
    public String toString() {
        StringBuilder append = new StringBuilder().append("TaskView->").append(hashCode()).append(", bind taskId=");
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        return append.append(runningTaskInfo != null ? Integer.valueOf(runningTaskInfo.taskId) : "null").append("->").append(super.toString()).toString();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        if (unwrap(getContext()).isFinishing()) {
            Slog.d(TAG, "surfaceCreated return when activity is finishing " + getContext());
            return;
        }
        this.mSurfaceCreated = true;
        if (DEBUG) {
            Slog.d(TAG, "surfaceCreated: mTaskToken=" + this.mTaskToken + ", mTaskLeash=" + this.mTaskLeash + ", mSurfaceCreated=" + this.mSurfaceCreated + ", this=" + this);
        }
        this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                FlexibleTaskView.this.lambda$surfaceCreated$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceCreated$10() {
        registerTaskOrganizer();
        if (this.mScenario == 2) {
            if (unwrap(getContext()).isResumed() || this.mPaused || this.mEnforceStart) {
                startActivityAndReparent();
                return;
            }
            return;
        }
        startActivityAndReparent();
    }

    private void startActivityAndReparent() {
        boolean isStartSuccess = false;
        if (this.mSuperLocked && !this.mStartEmbedd) {
            return;
        }
        Bundle bundle = prepareActivityOptions().toBundle();
        bundle.putInt("android:activity.mZoomLaunchFlags", 8);
        int i = this.mTaskId;
        if (i != -1) {
            try {
                startActivityFromRecents(i, bundle);
                isStartSuccess = true;
            } catch (Exception e) {
                if (DEBUG) {
                    Slog.d(TAG, "startActivityFromRecents: Task " + this.mTaskId + " not found.");
                }
            }
        }
        if (!isStartSuccess) {
            this.mTaskId = -1;
            try {
                this.mIntent.getIntentExt().addOplusFlags(2048);
                startActivity(this.mIntent, bundle);
                isStartSuccess = true;
            } catch (Exception e2) {
                if (DEBUG) {
                    Slog.d(TAG, "startActivity exception happened: " + e2);
                }
            }
            if (!isStartSuccess) {
                try {
                    int startStatus = FlexibleWindowManager.getInstance().startAnyActivity(this.mIntent, bundle);
                    Slog.d(TAG, "startAnyActivity result:" + startStatus);
                    if (startStatus == 0) {
                        isStartSuccess = true;
                    }
                } catch (Exception e3) {
                    if (DEBUG) {
                        Slog.d(TAG, "startAnyActivity exception happened: " + e3);
                    }
                }
            }
        }
        if (this.mListener != null) {
            final boolean initSuccess = isStartSuccess;
            this.mListenerExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$startActivityAndReparent$11(initSuccess);
                }
            });
        }
        this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                FlexibleTaskView.this.lambda$startActivityAndReparent$12();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startActivityAndReparent$11(boolean initSuccess) {
        this.mListener.onInitialized(initSuccess);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startActivityAndReparent$12() {
        if (this.mShadowRadius != 0.0f && isSurfaceControlValid()) {
            this.mTransaction.setShadowRadius(getSurfaceControl(), this.mShadowRadius).apply();
        }
        if (this.mTaskToken == null) {
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, "surfaceCreated: reparent task when surface is created, this=" + this);
        }
        reparent();
        updateTaskVisibility();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (DEBUG) {
            Slog.d(TAG, "surfaceDestroyed this=" + this);
        }
        this.mSurfaceCreated = false;
        this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                FlexibleTaskView.this.lambda$surfaceDestroyed$13();
            }
        });
        FlexibleTaskExtraViewManager flexibleTaskExtraViewManager = this.mExtraViewManager;
        if (flexibleTaskExtraViewManager != null) {
            flexibleTaskExtraViewManager.release();
            this.mExtraViewManager = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$surfaceDestroyed$13() {
        Activity activity;
        if (this.mTaskToken == null || (activity = unwrap(getContext())) == null) {
            return;
        }
        int containerTaskId = activity.getTaskId();
        if (DEBUG) {
            Slog.d(TAG, "surfaceDestroyed containerTaskId=" + containerTaskId + ",task: " + this.mTaskInfo.taskId + ",mTaskToken:" + this.mTaskToken);
        }
        if (!this.mSuperLocked) {
            FlexibleWindowManager.getInstance().updateTaskVisibility(this.mTaskToken, containerTaskId, false);
        }
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (DEBUG) {
            Slog.d(TAG, "onAttachedToWindow for this=" + this);
        }
        getViewTreeObserver().addOnComputeInternalInsetsListener(this.mFlexibleOnComputeInternalInsetsListener);
        ((Application) getContext().getApplicationContext()).registerActivityLifecycleCallbacks(this);
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (DEBUG) {
            Slog.d(TAG, "onDetachedFromWindow for this=" + this + ", context=" + getContext());
        }
        release();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mFlexibleOnComputeInternalInsetsListener);
        ((Application) getContext().getApplicationContext()).unregisterActivityLifecycleCallbacks(this);
        if (getTaskOrganizer() != null && getTaskOrganizer().isEmptyListener()) {
            Slog.d(TAG, "onDetachedFromWindow for this = " + this + ", context=" + getContext() + " stop organizer");
            getTaskOrganizer().stop();
            mTaskOrganizerMap.remove(getContext());
        }
    }

    private void registerTaskOrganizer() {
        HashMap<Context, EmbeddedWindowTaskOrganizer> hashMap = mTaskOrganizerMap;
        EmbeddedWindowTaskOrganizer organizer = hashMap.get(getContext());
        if (organizer == null) {
            organizer = new EmbeddedWindowTaskOrganizer(this.mExecutor);
            hashMap.put(getContext(), organizer);
        }
        organizer.start();
        this.mExecutor = organizer.getExecutor();
        if (DEBUG) {
            Slog.d(TAG, "registerTaskOrganizer for this=" + this + ", organizer=" + organizer + ", context=" + getContext());
        }
    }

    private void unregisterTaskOrganizer() {
        EmbeddedWindowTaskOrganizer organizer = mTaskOrganizerMap.get(getContext());
        if (organizer != null) {
            organizer.removeListener(this);
            organizer.stop();
            this.mTaskFlexibleFrames.clear();
            if (DEBUG) {
                Slog.d(TAG, "unregisterTaskOrganizer for this=" + this + ", organizer=" + organizer + ", context=" + getContext());
            }
        }
    }

    private void releaseTaskOrganizer() {
        HashMap<Context, EmbeddedWindowTaskOrganizer> hashMap = mTaskOrganizerMap;
        EmbeddedWindowTaskOrganizer organizer = hashMap.get(getContext());
        if (organizer != null) {
            organizer.stop();
            hashMap.remove(getContext());
            if (DEBUG) {
                Slog.d(TAG, "releaseTaskOrganizer for this=" + this + ", organizer=" + organizer + ", context=" + getContext());
            }
        }
    }

    private void reparent() {
        if (isSurfaceControlValid() && getVisibility() == 0 && this.mSurfaceCreated && this.mTaskLeash != null) {
            this.mLayoutRect.set(this.mLeft, this.mTop, this.mRight, this.mBottom);
            if (this.mTaskInfo != null) {
                int containerTaskId = unwrap(getContext()).getTaskId();
                if (DEBUG) {
                    Slog.d(TAG, "setEmbeddedContainerTask set containerTaskId=" + containerTaskId + " for task: " + this.mTaskInfo.taskId);
                }
                FlexibleWindowManager.getInstance().setEmbeddedContainerTask(this.mTaskInfo.taskId, containerTaskId);
            }
            int launchBoundsWidth = this.mLaunchBounds.width();
            int taskViewWidth = getWidth();
            float scaleX = taskViewWidth / launchBoundsWidth;
            int launchBoundsHeight = this.mLaunchBounds.height();
            int taskViewHeight = getHeight();
            float scaleY = taskViewHeight / launchBoundsHeight;
            float minScale = Math.min(scaleX, scaleY);
            float width = this.mLaunchBounds.width() * minScale;
            float height = this.mLaunchBounds.height() * minScale;
            float tx = (taskViewWidth - width) / 2.0f;
            float ty = (taskViewHeight - height) / 2.0f;
            if (Math.abs(scaleX - scaleY) >= DELTA) {
                if (this.mUserDefaultBackgroudColor) {
                    setBackgroundColor(this.mBackgroundColor);
                }
                if (this.mNeedZoderOnTop) {
                    setZOrderOnTop(true);
                }
            } else {
                tx = 0.0f;
                ty = 0.0f;
                if (this.mNeedZoderOnTop) {
                    setZOrderOnTop(false);
                }
            }
            float tx2 = tx;
            float ty2 = ty;
            setTaskViewCornerRadius(this.mCornerRadius);
            float cropScale = Math.max(minScale, 1.0f);
            if (DEBUG) {
                Slog.d(TAG, "reparentTaskView, launchBoundsWidth=" + this.mLaunchBounds.width() + ", launchBoundsHeight=" + this.mLaunchBounds.height() + ", taskViewWidth=" + taskViewWidth + ", taskViewHeight=" + taskViewHeight + ", cropScale=" + cropScale + ", this=" + this);
            }
            if (this.mNeedRotateTaskLeash) {
                Matrix matrix = new Matrix();
                float minScale2 = taskViewWidth / this.mLaunchBounds.height();
                matrix.reset();
                matrix.setTranslate(0.0f, -taskViewWidth);
                matrix.preScale(minScale2, minScale2);
                matrix.postRotate(90.0f);
                this.mTransaction.setCornerRadius(getSurfaceControl(), this.mCornerRadius);
                this.mTransaction.reparent(this.mTaskLeash, getSurfaceControl()).setShadowRadius(getSurfaceControl(), this.mShadowRadius).setWindowCrop(this.mTaskLeash, (int) (this.mLaunchBounds.width() * cropScale), (int) (this.mLaunchBounds.height() * cropScale)).setPosition(this.mTaskLeash, tx2, ty2).setMatrix(this.mTaskLeash, matrix, new float[9]).show(this.mTaskLeash).apply();
                setFlexibleFrame();
                return;
            }
            this.mTransaction.reparent(this.mTaskLeash, getSurfaceControl()).setShadowRadius(getSurfaceControl(), this.mShadowRadius).setWindowCrop(this.mTaskLeash, (int) (this.mLaunchBounds.width() * cropScale), (int) (this.mLaunchBounds.height() * cropScale)).setPosition(this.mTaskLeash, tx2, ty2).setMatrix(this.mTaskLeash, minScale, 0.0f, 0.0f, minScale).setAlpha(this.mTaskLeash, 1.0f).show(this.mTaskLeash).apply();
            setExtraView();
            setFlexibleFrame();
            return;
        }
        if (DEBUG) {
            Slog.d(TAG, "reparent surface null, or view not VISIBLE");
        }
    }

    private void setExtraView() {
        FlexibleTaskExtraViewManager flexibleTaskExtraViewManager = this.mExtraViewManager;
        if (flexibleTaskExtraViewManager != null) {
            flexibleTaskExtraViewManager.release();
        }
        Configuration configuration = new Configuration();
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
        if (runningTaskInfo != null) {
            configuration = runningTaskInfo.getConfiguration();
        }
        if (this.mExtraViewManager == null) {
            this.mExtraViewManager = new FlexibleTaskExtraViewManager(getContext(), configuration, new SurfaceSession());
        }
        if (isSurfaceControlValid()) {
            this.mExtraViewManager.setView(getContext(), getSurfaceControl(), this.mExtraViewLayout, this.mLayoutParams, this.mRegion);
        }
    }

    private void setTaskViewCornerRadius(final float cornerRadius) {
        if (cornerRadius != 0.0f) {
            setOutlineProvider(new ViewOutlineProvider() { // from class: com.oplus.flexiblewindow.FlexibleTaskView.1
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
                }
            });
            setClipToOutline(true);
        }
    }

    public void detachFromTaskView() {
        if (DEBUG) {
            Slog.d(TAG, "detachFromTaskView: taskId= " + this.mTaskId + ", this=" + this);
        }
        if (this.mTaskToken == null || this.mTaskId == -1) {
            return;
        }
        int containerTaskId = unwrap(getContext()).getTaskId();
        if (DEBUG) {
            Slog.d(TAG, "removeEmbeddedContainerTask remove containerTaskId= " + containerTaskId + " for task: " + this.mTaskInfo.taskId);
        }
        if (getTaskOrganizer() != null) {
            getTaskOrganizer().setInterceptBackPressedOnTaskRoot(this.mTaskToken, false);
        }
        FlexibleWindowManager.getInstance().removeEmbeddedContainerTask(this.mTaskId, containerTaskId);
        FlexibleWindowManager.getInstance().resetFlexibleTask(this.mTaskId, false, true);
        this.mTaskId = -1;
        resetTaskInfo();
        FlexibleTaskExtraViewManager flexibleTaskExtraViewManager = this.mExtraViewManager;
        if (flexibleTaskExtraViewManager != null) {
            flexibleTaskExtraViewManager.release();
            this.mExtraViewManager = null;
        }
    }

    public void interceptBackPressedOnTaskRoot(boolean intercept) {
        if (this.mTaskToken != null && getTaskOrganizer() != null) {
            getTaskOrganizer().setInterceptBackPressedOnTaskRoot(this.mTaskToken, intercept);
        }
    }

    private Activity unwrap(Context context) {
        while (!(context instanceof Activity) && (context instanceof ContextWrapper)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (Activity) context;
    }

    private void performRelease() {
        if (DEBUG) {
            Slog.d(TAG, "performRelease this=" + this);
        }
        resetTaskInfo();
        this.mSuperLocked = false;
        this.mStartEmbedd = false;
        if (this.mListener != null) {
            this.mListenerExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$performRelease$14();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performRelease$14() {
        this.mListener.onReleased();
    }

    private void resetTaskInfo() {
        try {
            if (DEBUG) {
                Slog.d(TAG, "resetTaskInfo taskOrganizer: " + getTaskOrganizer());
            }
            if (getTaskOrganizer() != null) {
                getTaskOrganizer().removeListener(this);
            }
        } catch (Exception e) {
            Slog.e(TAG, "remove task organizer listener error", e);
        }
        this.mTaskInfo = null;
        this.mTaskToken = null;
        this.mTaskLeash = null;
    }

    private void updateTaskVisibility() {
        WindowContainerTransaction wct = new WindowContainerTransaction();
        wct.setHidden(this.mTaskToken, !this.mSurfaceCreated);
        if (DEBUG) {
            Slog.d(TAG, "updateTaskVisibility set hidden=" + (!this.mSurfaceCreated) + " for task: " + this.mTaskInfo.taskId);
        }
        if (getTaskOrganizer() != null) {
            getTaskOrganizer().applyTransaction(wct);
        }
        if (this.mListener != null) {
            final int taskId = this.mTaskInfo.taskId;
            this.mListenerExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    FlexibleTaskView.this.lambda$updateTaskVisibility$15(taskId);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTaskVisibility$15(int taskId) {
        this.mListener.onTaskVisibilityChanged(taskId, this.mSurfaceCreated);
    }

    private int getBackgroudColor() {
        int uiMode = getContext().getResources().getConfiguration().uiMode;
        if ((uiMode & 32) != 0) {
            return Color.parseColor(DARK_BACKGROUND_COLOR);
        }
        return this.mWhiteColor;
    }

    /* loaded from: classes.dex */
    public interface Listener {
        default void onInitialized(boolean isStartSuccess) {
        }

        default void onReleased() {
        }

        default void onTaskCreated(int taskId, ComponentName name) {
        }

        default void onTaskChanged(int taskId, ComponentName name, Rect rect) {
        }

        default void onTaskVisibilityChanged(int taskId, boolean visible) {
        }

        default void onTaskRemovalStarted(int taskId) {
        }

        default void onBackPressedOnTaskRoot(int taskId) {
        }

        default void updateTouchRegion(Region region) {
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        this.mPaused = false;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        if (activity == unwrap(getContext())) {
            if (DEBUG) {
                Slog.d(TAG, "onActivityResumed, surfaceCreated:" + this.mSurfaceCreated);
            }
            if (this.mSurfaceCreated) {
                setBackground(null);
                this.mExecutor.execute(new Runnable() { // from class: com.oplus.flexiblewindow.FlexibleTaskView$$ExternalSyntheticLambda13
                    @Override // java.lang.Runnable
                    public final void run() {
                        FlexibleTaskView.this.lambda$onActivityResumed$16();
                    }
                });
            }
        }
        this.mPaused = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityResumed$16() {
        if (this.mTaskInfo != null && !this.mSuperLocked) {
            startActivityAndReparent();
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        if (activity == unwrap(getContext())) {
            setViewSnapshot();
        }
        this.mPaused = true;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        if (DEBUG) {
            Slog.d(TAG, "onActivityStopped " + activity);
        }
        if (activity == unwrap(getContext())) {
            unregisterTaskOrganizer();
        }
        this.mPaused = false;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
        if (DEBUG) {
            Slog.d(TAG, "onActivityDestroyed " + activity);
        }
        if (activity == unwrap(getContext())) {
            releaseTaskOrganizer();
        }
    }

    public void onRecentsAnimationExecuting(boolean executing, int reorderMode) {
        if (DEBUG) {
            String str = TAG;
            StringBuilder append = new StringBuilder().append("onRecentsAnimationExecuting executing: ").append(executing).append(", reorderMode: ").append(reorderMode).append(", mTaskId: ");
            ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskInfo;
            Slog.d(str, append.append(runningTaskInfo != null ? runningTaskInfo.taskId : -1).append(", mSurfaceCreated: ").append(this.mSurfaceCreated).append(", surface is valid: ").append(getSurfaceControl() != null && getSurfaceControl().isValid()).toString());
        }
        if (this.mSurfaceCreated && getSurfaceControl() != null && getSurfaceControl().isValid()) {
            if (executing) {
                if (this.mShowSurfaceCornerRadius) {
                    this.mTransaction.setCornerRadius(getSurfaceControl(), 0.0f).apply();
                }
                float scaleX = getWidth() / this.mLaunchBounds.width();
                float scaleY = getHeight() / this.mLaunchBounds.height();
                if (Math.abs(scaleX - scaleY) >= DELTA) {
                    setOutlineProvider(null);
                    setClipToOutline(true);
                    return;
                }
                return;
            }
            if (reorderMode == 2) {
                if (this.mShowSurfaceCornerRadius) {
                    this.mTransaction.setCornerRadius(getSurfaceControl(), this.mCornerRadius).apply();
                }
                float scaleX2 = getWidth() / this.mLaunchBounds.width();
                float scaleY2 = getHeight() / this.mLaunchBounds.height();
                if (Math.abs(scaleX2 - scaleY2) >= DELTA) {
                    setTaskViewCornerRadius(this.mCornerRadius);
                }
            }
        }
    }

    private void setViewSnapshot() {
        Bitmap bitmap = getSnapBitMap();
        if (bitmap == null) {
            return;
        }
        float scaleX = getWidth() / this.mLaunchBounds.width();
        float scaleY = getHeight() / this.mLaunchBounds.height();
        boolean isTaskNotMatchView = Math.abs(scaleX - scaleY) >= DELTA;
        if (isTaskNotMatchView) {
            Picture picture = new Picture();
            Canvas canvas = picture.beginRecording(bitmap.getWidth(), bitmap.getHeight());
            canvas.drawColor(this.mBackgroundColor);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            picture.endRecording();
            bitmap = Bitmap.createBitmap(picture);
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        setBackground(bitmapDrawable);
        if (DEBUG) {
            Slog.d(TAG, "setViewSnapshot getWindowVisibility() == VISIBLE && isShown() : " + (getWindowVisibility() == 0 && isShown()));
        }
    }

    private boolean notMatchBounds(TaskSnapshot taskSnapshot) {
        Point size = taskSnapshot.getTaskSize();
        if (size == null || size.x == 0 || size.y == 0) {
            return true;
        }
        float ratio1 = size.x / size.y;
        float ratio2 = this.mLaunchBounds.width() / this.mLaunchBounds.height();
        return Math.abs(ratio1 - ratio2) >= DELTA;
    }

    private Bitmap getSnapBitMap() {
        TaskSnapshot taskSnapshot;
        try {
            if (this.mTaskId <= 0 || (taskSnapshot = OplusActivityTaskManager.getService().getEmbeddedChildrenSnapshot(this.mTaskId, false, true)) == null || notMatchBounds(taskSnapshot)) {
                return null;
            }
            Bitmap thumbnail = null;
            try {
                HardwareBuffer buffer = taskSnapshot.getHardwareBuffer();
                if (buffer != null) {
                    try {
                        thumbnail = Bitmap.wrapHardwareBuffer(buffer, taskSnapshot.getColorSpace());
                    } catch (Throwable th) {
                        if (buffer != null) {
                            try {
                                buffer.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                }
                if (buffer != null) {
                    buffer.close();
                }
            } catch (IllegalArgumentException e) {
                if (DEBUG) {
                    Slog.d(TAG, "wrapHardwareBuffer IllegalArgumentException");
                }
            }
            return thumbnail;
        } catch (RemoteException e2) {
            if (DEBUG) {
                Slog.d(TAG, "takeTaskSnapshot error");
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    private class FlexibleOnComputeInternalInsetsListener implements ViewTreeObserver.OnComputeInternalInsetsListener {
        private FlexibleOnComputeInternalInsetsListener() {
        }

        public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo inoutInfo) {
            if (FlexibleTaskView.this.mInterceptInputEvent) {
                return;
            }
            if (inoutInfo.touchableRegion.isEmpty()) {
                inoutInfo.setTouchableInsets(3);
                View root = FlexibleTaskView.this.getRootView();
                root.getLocationInWindow(FlexibleTaskView.this.mTmpLocation);
                FlexibleTaskView.this.mTmpRootRect.set(FlexibleTaskView.this.mTmpLocation[0], FlexibleTaskView.this.mTmpLocation[1], root.getWidth(), root.getHeight());
                inoutInfo.touchableRegion.set(FlexibleTaskView.this.mTmpRootRect);
            }
            FlexibleTaskView flexibleTaskView = FlexibleTaskView.this;
            flexibleTaskView.getBoundsOnScreen(flexibleTaskView.mTmpRect);
            Rect rootWindowBound = FlexibleTaskView.this.getContext().getResources().getConfiguration().windowConfiguration.getBounds();
            int densityDpi = FlexibleTaskView.this.getContext().getResources().getConfiguration().densityDpi;
            int bias = densityDpi / 160;
            FlexibleTaskView.this.mTmpRect.inset(bias, bias);
            boolean insideScreen = rootWindowBound.contains(FlexibleTaskView.this.mTmpRect);
            if (!insideScreen) {
                return;
            }
            FlexibleTaskView.this.mTmpRect.inset(-bias, -bias);
            inoutInfo.touchableRegion.op(FlexibleTaskView.this.mTmpRect, Region.Op.DIFFERENCE);
            if (FlexibleTaskView.this.mListener != null) {
                FlexibleTaskView.this.mListener.updateTouchRegion(inoutInfo.touchableRegion);
            }
        }
    }

    @Override // android.view.SurfaceView, android.view.View
    public boolean gatherTransparentRegion(Region region) {
        boolean result = super.gatherTransparentRegion(region);
        if (region != null && this.mCornerRadius > 0.0f) {
            Rect maxBounds = getContext().getResources().getConfiguration().windowConfiguration.getMaxBounds();
            region.op(maxBounds.left, maxBounds.top, maxBounds.right, maxBounds.bottom, Region.Op.DIFFERENCE);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TaskFlexibleFrameInfo {
        private Rect mLastReportedFrame;
        private Rect mLastReportedHandleFrame;
        private int mTaskId;

        TaskFlexibleFrameInfo(int taskId, Rect lastReportedFrame, Rect lastReportedHandleFrame) {
            this.mTaskId = taskId;
            this.mLastReportedFrame = lastReportedFrame;
            this.mLastReportedHandleFrame = lastReportedHandleFrame;
        }

        public int getTaskId() {
            return this.mTaskId;
        }

        public void setTaskId(int taskId) {
            this.mTaskId = taskId;
        }

        public Rect getLastReportedFrame() {
            return this.mLastReportedFrame;
        }

        public void setLastReportedFrame(Rect lastReportedFrame) {
            this.mLastReportedFrame = lastReportedFrame;
        }

        public Rect getLastReportedHandleFrame() {
            return this.mLastReportedHandleFrame;
        }

        public void setLastReportedHandleFrame(Rect lastReportedHandleFrame) {
            this.mLastReportedHandleFrame = lastReportedHandleFrame;
        }
    }
}
