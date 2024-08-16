package com.android.server.wm;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.HardwareBuffer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.ITheiaManagerExt;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.view.AppTransitionAnimationSpec;
import android.view.IAppTransitionAnimationSpecsFuture;
import android.view.RemoteAnimationAdapter;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.TransitionAnimation;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.server.wm.ActivityRecord;
import com.android.server.wm.WindowManagerInternal;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class AppTransition implements DumpUtils.Dump {
    private static final int APP_STATE_IDLE = 0;
    private static final int APP_STATE_READY = 1;
    private static final int APP_STATE_RUNNING = 2;
    private static final int APP_STATE_TIMEOUT = 3;
    private static final long APP_TRANSITION_TIMEOUT_MS = 5000;
    static final int DEFAULT_APP_TRANSITION_DURATION = 336;
    static final int MAX_APP_TRANSITION_DURATION = 3000;
    private static final int NEXT_TRANSIT_TYPE_CLIP_REVEAL = 8;
    private static final int NEXT_TRANSIT_TYPE_CUSTOM = 1;
    private static final int NEXT_TRANSIT_TYPE_CUSTOM_IN_PLACE = 7;
    private static final int NEXT_TRANSIT_TYPE_NONE = 0;
    private static final int NEXT_TRANSIT_TYPE_OPEN_CROSS_PROFILE_APPS = 9;
    private static final int NEXT_TRANSIT_TYPE_REMOTE = 10;
    private static final int NEXT_TRANSIT_TYPE_SCALE_UP = 2;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_DOWN = 6;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_UP = 5;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_DOWN = 4;
    private static final int NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_UP = 3;
    private static final String TAG = "WindowManager";
    private static final ArrayList<Pair<Integer, String>> sFlagToString;
    private IRemoteCallback mAnimationFinishedCallback;
    private final Context mContext;
    private AppTransitionAnimationSpec mDefaultNextAppTransitionAnimationSpec;
    private final int mDefaultWindowAnimationStyleResId;
    private final DisplayContent mDisplayContent;
    final Handler mHandler;
    private WindowManagerInternal.KeyguardExitAnimationStartListener mKeyguardExitAnimationStartListener;
    private String mLastChangingApp;
    private String mLastClosingApp;
    private String mLastOpeningApp;
    private IAppTransitionAnimationSpecsFuture mNextAppTransitionAnimationsSpecsFuture;
    private boolean mNextAppTransitionAnimationsSpecsPending;
    private int mNextAppTransitionBackgroundColor;
    private IRemoteCallback mNextAppTransitionCallback;
    private int mNextAppTransitionEnter;
    private int mNextAppTransitionExit;
    private IRemoteCallback mNextAppTransitionFutureCallback;
    private int mNextAppTransitionInPlace;
    private boolean mNextAppTransitionIsSync;
    private boolean mNextAppTransitionOverrideRequested;
    private String mNextAppTransitionPackage;
    private boolean mNextAppTransitionScaleUp;
    private boolean mOverrideTaskTransition;
    private RemoteAnimationController mRemoteAnimationController;
    private final WindowManagerService mService;
    private ITheiaManagerExt mTheiaManagerExt;

    @VisibleForTesting
    final TransitionAnimation mTransitionAnimation;
    private int mNextAppTransitionFlags = 0;
    private final ArrayList<Integer> mNextAppTransitionRequests = new ArrayList<>();
    private int mLastUsedAppTransition = -1;
    private int mNextAppTransitionType = 0;
    private final SparseArray<AppTransitionAnimationSpec> mNextAppTransitionAnimationsSpecs = new SparseArray<>();
    private final Rect mTmpRect = new Rect();
    private int mAppTransitionState = 0;
    private final ArrayList<WindowManagerInternal.AppTransitionListener> mListeners = new ArrayList<>();
    private final ExecutorService mDefaultExecutor = Executors.newSingleThreadExecutor();
    final Runnable mHandleAppTransitionTimeoutRunnable = new Runnable() { // from class: com.android.server.wm.AppTransition$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            AppTransition.this.lambda$new$0();
        }
    };
    private AppTransitionWrapper mAppTransitionWrapper = new AppTransitionWrapper();
    private final boolean mGridLayoutRecentsEnabled = SystemProperties.getBoolean("ro.recents.grid", false);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isActivityTransitOld(int i) {
        return i == 6 || i == 7 || i == 18;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isChangeTransitOld(int i) {
        return i == 27 || i == 30;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isClosingTransitOld(int i) {
        return i == 7 || i == 9 || i == 12 || i == 15 || i == 25 || i == 26;
    }

    public static boolean isKeyguardGoingAwayTransitOld(int i) {
        return i == 20 || i == 21;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isKeyguardOccludeTransitOld(int i) {
        return i == 22 || i == 33 || i == 23;
    }

    static boolean isKeyguardTransit(int i) {
        return i == 7 || i == 8 || i == 9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isNormalTransit(int i) {
        return i == 1 || i == 2 || i == 3 || i == 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTaskCloseTransitOld(int i) {
        return i == 9 || i == 11;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTaskFragmentTransitOld(int i) {
        return i == 28 || i == 29 || i == 30;
    }

    private static boolean isTaskOpenTransitOld(int i) {
        return i == 8 || i == 16 || i == 10;
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x0076, code lost:
    
        if (r10 != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x007f, code lost:
    
        r3 = 7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:?, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0079, code lost:
    
        if (r10 != false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0085, code lost:
    
        r0 = 5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0087, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x007c, code lost:
    
        if (r10 != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0082, code lost:
    
        if (r10 != false) goto L62;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:13:0x001d. Please report as an issue. */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int mapOpenCloseTransitTypes(int i, boolean z) {
        int i2;
        int i3 = 4;
        if (i != 24) {
            int i4 = 6;
            if (i != 25) {
                if (i != 28) {
                    if (i != 29) {
                        if (i == 31) {
                            return z ? 28 : 29;
                        }
                        if (i != 32) {
                            switch (i) {
                                case 6:
                                    break;
                                case 7:
                                    break;
                                case 8:
                                    if (!z) {
                                        i2 = 9;
                                        break;
                                    } else {
                                        i2 = 8;
                                        break;
                                    }
                                case 9:
                                    if (!z) {
                                        i2 = 11;
                                        break;
                                    } else {
                                        i2 = 10;
                                        break;
                                    }
                                case 10:
                                    if (!z) {
                                        i2 = 13;
                                        break;
                                    } else {
                                        i2 = 12;
                                        break;
                                    }
                                case 11:
                                    if (!z) {
                                        i2 = 15;
                                        break;
                                    } else {
                                        i2 = 14;
                                        break;
                                    }
                                case 12:
                                    if (!z) {
                                        i2 = 19;
                                        break;
                                    } else {
                                        i2 = 18;
                                        break;
                                    }
                                case 13:
                                    if (!z) {
                                        i2 = 17;
                                        break;
                                    } else {
                                        i2 = 16;
                                        break;
                                    }
                                case 14:
                                    if (!z) {
                                        i2 = 21;
                                        break;
                                    } else {
                                        i2 = 20;
                                        break;
                                    }
                                case 15:
                                    if (!z) {
                                        i2 = 23;
                                        break;
                                    } else {
                                        i2 = 22;
                                        break;
                                    }
                                case 16:
                                    return z ? 25 : 24;
                                default:
                                    return 0;
                            }
                        } else {
                            if (z) {
                                return 0;
                            }
                            i2 = 27;
                        }
                        return i2;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppTransition(Context context, WindowManagerService windowManagerService, DisplayContent displayContent) {
        this.mTheiaManagerExt = null;
        this.mContext = context;
        this.mService = windowManagerService;
        this.mHandler = new Handler(windowManagerService.mH.getLooper());
        this.mDisplayContent = displayContent;
        this.mTransitionAnimation = new TransitionAnimation(context, ProtoLogImpl.isEnabled(ProtoLogGroup.WM_DEBUG_ANIM), TAG);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(R.styleable.Window);
        this.mDefaultWindowAnimationStyleResId = obtainStyledAttributes.getResourceId(8, 0);
        obtainStyledAttributes.recycle();
        this.mTheiaManagerExt = (ITheiaManagerExt) ExtLoader.type(ITheiaManagerExt.class).base(context).create();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTransitionSet() {
        return !this.mNextAppTransitionRequests.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUnoccluding() {
        return this.mNextAppTransitionRequests.contains(9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean transferFrom(AppTransition appTransition) {
        this.mNextAppTransitionRequests.addAll(appTransition.mNextAppTransitionRequests);
        return prepare();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastAppTransition(int i, ActivityRecord activityRecord, ActivityRecord activityRecord2, ActivityRecord activityRecord3) {
        this.mLastUsedAppTransition = i;
        this.mLastOpeningApp = "" + activityRecord;
        this.mLastClosingApp = "" + activityRecord2;
        this.mLastChangingApp = "" + activityRecord3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReady() {
        int i = this.mAppTransitionState;
        return i == 1 || i == 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setReady() {
        setAppTransitionState(1);
        fetchAppTransitionSpecsFromFuture();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isRunning() {
        return this.mAppTransitionState == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIdle() {
        setAppTransitionState(0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isIdle() {
        return this.mAppTransitionState == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTimeout() {
        return this.mAppTransitionState == 3;
    }

    void setTimeout() {
        setAppTransitionState(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Animation getNextAppRequestedAnimation(boolean z) {
        Animation loadAppTransitionAnimation = this.mTransitionAnimation.loadAppTransitionAnimation(this.mNextAppTransitionPackage, z ? this.mNextAppTransitionEnter : this.mNextAppTransitionExit);
        int i = this.mNextAppTransitionBackgroundColor;
        if (i != 0 && loadAppTransitionAnimation != null) {
            loadAppTransitionAnimation.setBackdropColor(i);
        }
        return loadAppTransitionAnimation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNextAppTransitionBackgroundColor() {
        return this.mNextAppTransitionBackgroundColor;
    }

    @VisibleForTesting
    boolean isNextAppTransitionOverrideRequested() {
        return this.mNextAppTransitionOverrideRequested;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HardwareBuffer getAppTransitionThumbnailHeader(WindowContainer windowContainer) {
        AppTransitionAnimationSpec appTransitionAnimationSpec = this.mNextAppTransitionAnimationsSpecs.get(windowContainer.hashCode());
        if (appTransitionAnimationSpec == null) {
            appTransitionAnimationSpec = this.mDefaultNextAppTransitionAnimationSpec;
        }
        if (appTransitionAnimationSpec != null) {
            return appTransitionAnimationSpec.buffer;
        }
        return null;
    }

    boolean isNextThumbnailTransitionAspectScaled() {
        int i = this.mNextAppTransitionType;
        return i == 5 || i == 6;
    }

    boolean isNextThumbnailTransitionScaleUp() {
        return this.mNextAppTransitionScaleUp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNextAppTransitionThumbnailUp() {
        int i = this.mNextAppTransitionType;
        return i == 3 || i == 5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNextAppTransitionThumbnailDown() {
        int i = this.mNextAppTransitionType;
        return i == 4 || i == 6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isNextAppTransitionOpenCrossProfileApps() {
        return this.mNextAppTransitionType == 9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFetchingAppTransitionsSpecs() {
        return this.mNextAppTransitionAnimationsSpecsPending;
    }

    private boolean prepare() {
        if (isRunning()) {
            return false;
        }
        setAppTransitionState(0);
        notifyAppTransitionPendingLocked();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int goodToGo(int i, ActivityRecord activityRecord) {
        long uptimeMillis;
        this.mAppTransitionWrapper.getExtImpl().hookgoodToGo(this.mDisplayContent, i);
        this.mNextAppTransitionFlags = 0;
        this.mNextAppTransitionRequests.clear();
        setAppTransitionState(2);
        WindowContainer animatingContainer = activityRecord != null ? activityRecord.getAnimatingContainer() : null;
        AnimationAdapter animation = animatingContainer != null ? animatingContainer.getAnimation() : null;
        if (animation != null) {
            uptimeMillis = animation.getStatusBarTransitionsStartTime();
        } else {
            uptimeMillis = SystemClock.uptimeMillis();
        }
        int notifyAppTransitionStartingLocked = notifyAppTransitionStartingLocked(uptimeMillis, 120L);
        RemoteAnimationController remoteAnimationController = this.mRemoteAnimationController;
        if (remoteAnimationController != null) {
            remoteAnimationController.goodToGo(i);
        } else if ((isTaskOpenTransitOld(i) || i == 12) && animation != null && this.mDisplayContent.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() && this.mService.getRecentsAnimationController() == null) {
            new NavBarFadeAnimationController(this.mDisplayContent).fadeOutAndInSequentially(animation.getDurationHint(), null, activityRecord.getSurfaceControl());
        }
        return notifyAppTransitionStartingLocked;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        clear(true);
    }

    private void clear(boolean z) {
        this.mNextAppTransitionType = 0;
        this.mNextAppTransitionOverrideRequested = false;
        this.mNextAppTransitionAnimationsSpecs.clear();
        this.mRemoteAnimationController = null;
        this.mNextAppTransitionAnimationsSpecsFuture = null;
        this.mDefaultNextAppTransitionAnimationSpec = null;
        this.mAnimationFinishedCallback = null;
        this.mOverrideTaskTransition = false;
        this.mNextAppTransitionIsSync = false;
        if (z) {
            this.mNextAppTransitionPackage = null;
            this.mNextAppTransitionEnter = 0;
            this.mNextAppTransitionExit = 0;
            this.mNextAppTransitionBackgroundColor = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void freeze() {
        boolean contains = this.mNextAppTransitionRequests.contains(7);
        RemoteAnimationController remoteAnimationController = this.mRemoteAnimationController;
        if (remoteAnimationController != null) {
            remoteAnimationController.cancelAnimation("freeze");
        }
        if (this.mRemoteAnimationController == null && isUnoccluding()) {
            Slog.d(TAG, "freeze mRemoteAnimationController is null!!! " + toString() + ",call=" + Debug.getCallers(3));
        }
        this.mNextAppTransitionRequests.clear();
        clear();
        setReady();
        notifyAppTransitionCancelledLocked(contains);
    }

    private void setAppTransitionState(int i) {
        this.mAppTransitionState = i;
        updateBooster();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateBooster() {
        WindowManagerService.sThreadPriorityBooster.setAppTransitionRunning(needsBoosting());
    }

    private boolean needsBoosting() {
        int i;
        return !this.mNextAppTransitionRequests.isEmpty() || (i = this.mAppTransitionState) == 1 || i == 2 || (this.mService.getRecentsAnimationController() != null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerListenerLocked(WindowManagerInternal.AppTransitionListener appTransitionListener) {
        this.mListeners.add(appTransitionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterListener(WindowManagerInternal.AppTransitionListener appTransitionListener) {
        this.mListeners.remove(appTransitionListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerKeygaurdExitAnimationStartListener(WindowManagerInternal.KeyguardExitAnimationStartListener keyguardExitAnimationStartListener) {
        this.mKeyguardExitAnimationStartListener = keyguardExitAnimationStartListener;
    }

    public void notifyAppTransitionFinishedLocked(IBinder iBinder) {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionFinishedLocked(iBinder);
        }
    }

    private void notifyAppTransitionPendingLocked() {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionPendingLocked();
        }
    }

    private void notifyAppTransitionCancelledLocked(boolean z) {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionCancelledLocked(z);
        }
    }

    private void notifyAppTransitionTimeoutLocked() {
        for (int i = 0; i < this.mListeners.size(); i++) {
            this.mListeners.get(i).onAppTransitionTimeoutLocked();
        }
    }

    private int notifyAppTransitionStartingLocked(long j, long j2) {
        int i = 0;
        for (int i2 = 0; i2 < this.mListeners.size(); i2++) {
            i |= this.mListeners.get(i2).onAppTransitionStartingLocked(j, j2);
        }
        return i;
    }

    @VisibleForTesting
    int getDefaultWindowAnimationStyleResId() {
        return this.mDefaultWindowAnimationStyleResId;
    }

    @VisibleForTesting
    int getAnimationStyleResId(WindowManager.LayoutParams layoutParams) {
        return this.mTransitionAnimation.getAnimationStyleResId(layoutParams);
    }

    @VisibleForTesting
    Animation loadAnimationSafely(Context context, int i) {
        try {
            return this.mAppTransitionWrapper.getExtImpl().hookloadAnimationSafely(context, this.mNextAppTransitionType == 1, i, this.mNextAppTransitionPackage, TAG);
        } catch (Resources.NotFoundException e) {
            Slog.w(TAG, "Unable to load animation resource", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "Unable to load animation resource", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Animation loadAnimationAttr(WindowManager.LayoutParams layoutParams, int i, int i2) {
        return this.mTransitionAnimation.loadAnimationAttr(layoutParams, i, i2);
    }

    private void getDefaultNextAppTransitionStartRect(Rect rect) {
        Rect rect2;
        AppTransitionAnimationSpec appTransitionAnimationSpec = this.mDefaultNextAppTransitionAnimationSpec;
        if (appTransitionAnimationSpec == null || (rect2 = appTransitionAnimationSpec.rect) == null) {
            Slog.e(TAG, "Starting rect for app requested, but none available", new Throwable());
            rect.setEmpty();
        } else {
            rect.set(rect2);
        }
    }

    private void putDefaultNextAppTransitionCoordinates(int i, int i2, int i3, int i4, HardwareBuffer hardwareBuffer) {
        this.mDefaultNextAppTransitionAnimationSpec = new AppTransitionAnimationSpec(-1, hardwareBuffer, new Rect(i, i2, i3 + i, i4 + i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HardwareBuffer createCrossProfileAppsThumbnail(Drawable drawable, Rect rect) {
        return this.mTransitionAnimation.createCrossProfileAppsThumbnail(drawable, rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Animation createCrossProfileAppsThumbnailAnimationLocked(Rect rect) {
        return this.mTransitionAnimation.createCrossProfileAppsThumbnailAnimationLocked(rect);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Animation createThumbnailAspectScaleAnimationLocked(Rect rect, Rect rect2, HardwareBuffer hardwareBuffer, WindowContainer windowContainer, int i) {
        AppTransitionAnimationSpec appTransitionAnimationSpec = this.mNextAppTransitionAnimationsSpecs.get(windowContainer.hashCode());
        TransitionAnimation transitionAnimation = this.mTransitionAnimation;
        Rect rect3 = appTransitionAnimationSpec != null ? appTransitionAnimationSpec.rect : null;
        AppTransitionAnimationSpec appTransitionAnimationSpec2 = this.mDefaultNextAppTransitionAnimationSpec;
        return transitionAnimation.createThumbnailAspectScaleAnimationLocked(rect, rect2, hardwareBuffer, i, rect3, appTransitionAnimationSpec2 != null ? appTransitionAnimationSpec2.rect : null, this.mNextAppTransitionScaleUp);
    }

    private AnimationSet createAspectScaledThumbnailFreeformAnimationLocked(Rect rect, Rect rect2, Rect rect3, boolean z) {
        ScaleAnimation scaleAnimation;
        TranslateAnimation translateAnimation;
        float width = rect.width();
        float height = rect.height();
        float width2 = rect2.width();
        float height2 = rect2.height();
        float f = z ? width / width2 : width2 / width;
        float f2 = z ? height / height2 : height2 / height;
        AnimationSet animationSet = new AnimationSet(true);
        int i = rect3 == null ? 0 : rect3.left + rect3.right;
        int i2 = rect3 != null ? rect3.top + rect3.bottom : 0;
        if (z) {
            width = width2;
        }
        float f3 = (width + i) / 2.0f;
        if (z) {
            height = height2;
        }
        float f4 = (height + i2) / 2.0f;
        if (z) {
            scaleAnimation = new ScaleAnimation(f, 1.0f, f2, 1.0f, f3, f4);
        } else {
            scaleAnimation = new ScaleAnimation(1.0f, f, 1.0f, f2, f3, f4);
        }
        int width3 = rect.left + (rect.width() / 2);
        int height3 = rect.top + (rect.height() / 2);
        int width4 = rect2.left + (rect2.width() / 2);
        int height4 = rect2.top + (rect2.height() / 2);
        int i3 = z ? width3 - width4 : width4 - width3;
        int i4 = z ? height3 - height4 : height4 - height3;
        if (z) {
            translateAnimation = new TranslateAnimation(i3, 0.0f, i4, 0.0f);
        } else {
            translateAnimation = new TranslateAnimation(0.0f, i3, 0.0f, i4);
        }
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        setAppTransitionFinishedCallbackIfNeeded(animationSet);
        return animationSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canSkipFirstFrame() {
        int i = this.mNextAppTransitionType;
        return (i == 1 || this.mNextAppTransitionOverrideRequested || i == 7 || i == 8 || this.mNextAppTransitionRequests.contains(7)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RemoteAnimationController getRemoteAnimationController() {
        return this.mRemoteAnimationController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Animation loadAnimation(WindowManager.LayoutParams layoutParams, int i, boolean z, int i2, int i3, Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5, boolean z2, boolean z3, WindowContainer windowContainer) {
        int i4;
        int i5;
        int i6;
        Animation createThumbnailEnterExitAnimationLockedCompat;
        Animation animation;
        Rect rect6;
        Rect rect7;
        boolean canCustomizeAppTransition = windowContainer.canCustomizeAppTransition();
        Animation animation2 = null;
        if (this.mNextAppTransitionOverrideRequested) {
            if (canCustomizeAppTransition || this.mOverrideTaskTransition || this.mAppTransitionWrapper.getExtImpl().canCustomizeAppTransition(layoutParams, i, z, windowContainer, this.mNextAppTransitionPackage)) {
                this.mNextAppTransitionType = 1;
            } else if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                ProtoLogImpl.e(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 2079410261, 0, (String) null, (Object[]) null);
            }
        }
        if (isKeyguardGoingAwayTransitOld(i) && z) {
            Animation createHiddenByKeyguardExit = this.mAppTransitionWrapper.getExtImpl().createHiddenByKeyguardExit(this.mNextAppTransitionFlags, i == 21, this.mService.mAtmService.mKeyguardController.getWrapper().getExtImpl().getKeyguardGoingAwayFlags(), windowContainer.isActivityTypeHome());
            if (createHiddenByKeyguardExit != null) {
                setAppTransitionFinishedCallbackIfNeeded(createHiddenByKeyguardExit);
                return createHiddenByKeyguardExit;
            }
            animation2 = this.mTransitionAnimation.loadKeyguardExitAnimation(this.mNextAppTransitionFlags, i == 21);
        } else if (i != 22 && i != 33) {
            if (i == 23 && !z) {
                Animation loadKeyguardUnoccludeAnimation = this.mAppTransitionWrapper.getExtImpl().loadKeyguardUnoccludeAnimation(windowContainer);
                if (loadKeyguardUnoccludeAnimation != null) {
                    setAppTransitionFinishedCallbackIfNeeded(loadKeyguardUnoccludeAnimation);
                    return loadKeyguardUnoccludeAnimation;
                }
                animation2 = this.mTransitionAnimation.loadKeyguardUnoccludeAnimation();
            } else if (i != 26) {
                if (z2 && (i == 6 || i == 8 || i == 10)) {
                    animation = this.mTransitionAnimation.loadVoiceActivityOpenAnimation(z);
                    if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 508887531, 48, (String) null, new Object[]{String.valueOf(animation), String.valueOf(appTransitionOldToString(i)), Boolean.valueOf(z), String.valueOf(Debug.getCallers(3))});
                    }
                } else if (z2 && (i == 7 || i == 9 || i == 11)) {
                    animation = this.mTransitionAnimation.loadVoiceActivityExitAnimation(z);
                    if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 508887531, 48, (String) null, new Object[]{String.valueOf(animation), String.valueOf(appTransitionOldToString(i)), Boolean.valueOf(z), String.valueOf(Debug.getCallers(3))});
                    }
                } else {
                    if (i == 18) {
                        TransitionAnimation transitionAnimation = this.mTransitionAnimation;
                        AppTransitionAnimationSpec appTransitionAnimationSpec = this.mDefaultNextAppTransitionAnimationSpec;
                        if (appTransitionAnimationSpec != null) {
                            rect7 = appTransitionAnimationSpec.rect;
                            rect6 = rect3;
                        } else {
                            rect6 = rect3;
                            rect7 = null;
                        }
                        createThumbnailEnterExitAnimationLockedCompat = transitionAnimation.createRelaunchAnimation(rect, rect6, rect7);
                        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -1800899273, 0, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), String.valueOf(appTransitionOldToString(i)), String.valueOf(Debug.getCallers(3))});
                        }
                    } else {
                        int i7 = this.mNextAppTransitionType;
                        if (i7 == 1) {
                            Animation checkAndLoadCustomAnimation = this.mAppTransitionWrapper.getExtImpl().checkAndLoadCustomAnimation(this.mNextAppTransitionPackage, i, z, z ? this.mNextAppTransitionEnter : this.mNextAppTransitionExit);
                            if (checkAndLoadCustomAnimation != null) {
                                setAppTransitionFinishedCallbackIfNeeded(checkAndLoadCustomAnimation);
                                return checkAndLoadCustomAnimation;
                            }
                            Animation loadTransitCustomCompactWindowAnimation = this.mAppTransitionWrapper.getExtImpl().loadTransitCustomCompactWindowAnimation(layoutParams, i, z, windowContainer);
                            if (loadTransitCustomCompactWindowAnimation != null) {
                                setAppTransitionFinishedCallbackIfNeeded(loadTransitCustomCompactWindowAnimation);
                                return loadTransitCustomCompactWindowAnimation;
                            }
                            getNextAppRequestedAnimation(z);
                            Animation loadAppTransitionAnimation = this.mTransitionAnimation.loadAppTransitionAnimation(this.mNextAppTransitionPackage, z ? this.mNextAppTransitionEnter : this.mNextAppTransitionExit);
                            int i8 = this.mNextAppTransitionBackgroundColor;
                            if (i8 != 0) {
                                loadAppTransitionAnimation.setBackdropColor(i8);
                            }
                            createThumbnailEnterExitAnimationLockedCompat = this.mAppTransitionWrapper.getExtImpl().loadCustomZoomAnimation(i, windowContainer, loadAppTransitionAnimation);
                            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -519504830, 48, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), String.valueOf(appTransitionOldToString(i)), Boolean.valueOf(z), String.valueOf(Debug.getCallers(3))});
                            }
                        } else if (i7 == 7) {
                            createThumbnailEnterExitAnimationLockedCompat = this.mTransitionAnimation.loadAppTransitionAnimation(this.mNextAppTransitionPackage, this.mNextAppTransitionInPlace);
                            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 1457990604, 0, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), String.valueOf(appTransitionOldToString(i)), String.valueOf(Debug.getCallers(3))});
                            }
                        } else if (i7 == 8) {
                            TransitionAnimation transitionAnimation2 = this.mTransitionAnimation;
                            AppTransitionAnimationSpec appTransitionAnimationSpec2 = this.mDefaultNextAppTransitionAnimationSpec;
                            createThumbnailEnterExitAnimationLockedCompat = transitionAnimation2.createClipRevealAnimationLockedCompat(i, z, rect, rect2, appTransitionAnimationSpec2 != null ? appTransitionAnimationSpec2.rect : null);
                            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 274773837, 0, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), String.valueOf(appTransitionOldToString(i)), String.valueOf(Debug.getCallers(3))});
                            }
                        } else if (i7 == 2) {
                            TransitionAnimation transitionAnimation3 = this.mTransitionAnimation;
                            AppTransitionAnimationSpec appTransitionAnimationSpec3 = this.mDefaultNextAppTransitionAnimationSpec;
                            createThumbnailEnterExitAnimationLockedCompat = transitionAnimation3.createScaleUpAnimationLockedCompat(i, z, rect, appTransitionAnimationSpec3 != null ? appTransitionAnimationSpec3.rect : null);
                            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 2028163120, 0, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), String.valueOf(appTransitionOldToString(i)), String.valueOf(z), String.valueOf(Debug.getCallers(3))});
                            }
                        } else {
                            if (i7 == 3) {
                                i4 = 192;
                                i5 = -1872288685;
                                i6 = 3;
                            } else if (i7 == 4) {
                                i6 = 3;
                                i4 = 192;
                                i5 = -1872288685;
                            } else if (i7 == 5 || i7 == 6) {
                                this.mNextAppTransitionScaleUp = i7 == 5;
                                AppTransitionAnimationSpec appTransitionAnimationSpec4 = this.mNextAppTransitionAnimationsSpecs.get(windowContainer.hashCode());
                                TransitionAnimation transitionAnimation4 = this.mTransitionAnimation;
                                boolean z4 = this.mNextAppTransitionScaleUp;
                                Rect rect8 = appTransitionAnimationSpec4 != null ? appTransitionAnimationSpec4.rect : null;
                                AppTransitionAnimationSpec appTransitionAnimationSpec5 = this.mDefaultNextAppTransitionAnimationSpec;
                                createThumbnailEnterExitAnimationLockedCompat = transitionAnimation4.createAspectScaledThumbnailEnterExitAnimationLocked(z, z4, i3, i, rect, rect3, rect4, rect5, z3, rect8, appTransitionAnimationSpec5 != null ? appTransitionAnimationSpec5.rect : null);
                                if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -1872288685, 192, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), this.mNextAppTransitionScaleUp ? "ANIM_THUMBNAIL_ASPECT_SCALE_UP" : "ANIM_THUMBNAIL_ASPECT_SCALE_DOWN", String.valueOf(appTransitionOldToString(i)), Boolean.valueOf(z), String.valueOf(Debug.getCallers(3))});
                                }
                            } else if (i7 == 9 && z) {
                                createThumbnailEnterExitAnimationLockedCompat = this.mTransitionAnimation.loadCrossProfileAppEnterAnimation();
                                if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, 1589610525, 0, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), String.valueOf(appTransitionOldToString(i)), String.valueOf(Debug.getCallers(3))});
                                }
                            } else if (isChangeTransitOld(i)) {
                                createThumbnailEnterExitAnimationLockedCompat = new AlphaAnimation(1.0f, 1.0f);
                                createThumbnailEnterExitAnimationLockedCompat.setDuration(336L);
                                if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -1862269827, 48, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), String.valueOf(appTransitionOldToString(i)), Boolean.valueOf(z), String.valueOf(Debug.getCallers(3))});
                                }
                            } else {
                                IAppTransitionExt extImpl = this.mAppTransitionWrapper.getExtImpl();
                                DisplayContent displayContent = this.mDisplayContent;
                                Animation loadFlexibleTaskTransitionAnimation = extImpl.loadFlexibleTaskTransitionAnimation(i, z, windowContainer, displayContent.mOpeningApps, displayContent.mClosingApps);
                                if (loadFlexibleTaskTransitionAnimation != null) {
                                    return loadFlexibleTaskTransitionAnimation;
                                }
                                IAppTransitionExt extImpl2 = this.mAppTransitionWrapper.getExtImpl();
                                DisplayContent displayContent2 = this.mDisplayContent;
                                Animation loadFlexibleActivityTransitionAnimation = extImpl2.loadFlexibleActivityTransitionAnimation(i, z, windowContainer, displayContent2.mOpeningApps, displayContent2.mClosingApps);
                                if (loadFlexibleActivityTransitionAnimation != null) {
                                    return loadFlexibleActivityTransitionAnimation;
                                }
                                Animation loadOnePuttTransitionAnimation = this.mAppTransitionWrapper.getExtImpl().loadOnePuttTransitionAnimation(i, z, windowContainer);
                                if (loadOnePuttTransitionAnimation != null) {
                                    return loadOnePuttTransitionAnimation;
                                }
                                Animation loadCompactWindowAnimation = this.mAppTransitionWrapper.getExtImpl().loadCompactWindowAnimation(layoutParams, i, z, windowContainer);
                                if (loadCompactWindowAnimation != null) {
                                    return loadCompactWindowAnimation;
                                }
                                Animation loadOplusStyleAnimation = this.mAppTransitionWrapper.getExtImpl().loadOplusStyleAnimation(layoutParams, i, z);
                                if (loadOplusStyleAnimation != null) {
                                    setAppTransitionFinishedCallbackIfNeeded(loadOplusStyleAnimation);
                                    return loadOplusStyleAnimation;
                                }
                                int mapOpenCloseTransitTypes = mapOpenCloseTransitTypes(i, z);
                                if (mapOpenCloseTransitTypes != 0) {
                                    ActivityRecord.CustomAppTransition customAppTransition = getCustomAppTransition(mapOpenCloseTransitTypes, windowContainer);
                                    if (customAppTransition != null) {
                                        animation = loadCustomActivityAnimation(customAppTransition, z, windowContainer);
                                    } else if (canCustomizeAppTransition) {
                                        animation = loadAnimationAttr(layoutParams, mapOpenCloseTransitTypes, i);
                                    } else {
                                        animation = this.mTransitionAnimation.loadDefaultAnimationAttr(mapOpenCloseTransitTypes, i);
                                    }
                                } else {
                                    animation = null;
                                }
                                this.mAppTransitionWrapper.getExtImpl().updateAnimationForZoom(i, windowContainer, animation);
                                if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                    ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, -57572004, 964, (String) null, new Object[]{String.valueOf(animation), Long.valueOf(mapOpenCloseTransitTypes), String.valueOf(appTransitionOldToString(i)), Boolean.valueOf(z), Boolean.valueOf(canCustomizeAppTransition), String.valueOf(Debug.getCallers(3))});
                                }
                            }
                            this.mNextAppTransitionScaleUp = i7 == i6;
                            HardwareBuffer appTransitionThumbnailHeader = getAppTransitionThumbnailHeader(windowContainer);
                            TransitionAnimation transitionAnimation5 = this.mTransitionAnimation;
                            boolean z5 = this.mNextAppTransitionScaleUp;
                            AppTransitionAnimationSpec appTransitionAnimationSpec6 = this.mDefaultNextAppTransitionAnimationSpec;
                            createThumbnailEnterExitAnimationLockedCompat = transitionAnimation5.createThumbnailEnterExitAnimationLockedCompat(z, z5, rect, i, appTransitionThumbnailHeader, appTransitionAnimationSpec6 != null ? appTransitionAnimationSpec6.rect : null);
                            if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_ANIM_enabled) {
                                ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS_ANIM, i5, i4, (String) null, new Object[]{String.valueOf(createThumbnailEnterExitAnimationLockedCompat), this.mNextAppTransitionScaleUp ? "ANIM_THUMBNAIL_SCALE_UP" : "ANIM_THUMBNAIL_SCALE_DOWN", String.valueOf(appTransitionOldToString(i)), Boolean.valueOf(z), String.valueOf(Debug.getCallers(i6))});
                            }
                        }
                    }
                    animation2 = createThumbnailEnterExitAnimationLockedCompat;
                }
                animation2 = animation;
            }
        }
        setAppTransitionFinishedCallbackIfNeeded(animation2);
        return animation2;
    }

    ActivityRecord.CustomAppTransition getCustomAppTransition(int i, WindowContainer windowContainer) {
        ActivityRecord asActivityRecord = windowContainer.asActivityRecord();
        if (asActivityRecord == null) {
            return null;
        }
        if ((i == 5 || i == 6) && (asActivityRecord = asActivityRecord.getTask().getActivityAbove(asActivityRecord)) == null) {
            return null;
        }
        if (i == 4 || i == 5) {
            return asActivityRecord.getCustomAnimation(true);
        }
        if (i == 6 || i == 7) {
            return asActivityRecord.getCustomAnimation(false);
        }
        return null;
    }

    private Animation loadCustomActivityAnimation(ActivityRecord.CustomAppTransition customAppTransition, boolean z, WindowContainer windowContainer) {
        int i;
        Animation loadAppTransitionAnimation = this.mTransitionAnimation.loadAppTransitionAnimation(windowContainer.asActivityRecord().packageName, z ? customAppTransition.mEnterAnim : customAppTransition.mExitAnim);
        if (loadAppTransitionAnimation != null && (i = customAppTransition.mBackgroundColor) != 0) {
            loadAppTransitionAnimation.setBackdropColor(i);
            loadAppTransitionAnimation.setShowBackdrop(true);
        }
        return loadAppTransitionAnimation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAppRootTaskClipMode() {
        return (this.mNextAppTransitionRequests.contains(5) || this.mNextAppTransitionRequests.contains(7) || this.mNextAppTransitionType == 8) ? 1 : 0;
    }

    public int getTransitFlags() {
        return this.mNextAppTransitionFlags;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postAnimationCallback() {
        if (this.mNextAppTransitionCallback != null) {
            this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.wm.AppTransition$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppTransition.doAnimationCallback((IRemoteCallback) obj);
                }
            }, this.mNextAppTransitionCallback));
            this.mNextAppTransitionCallback = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransition(String str, int i, int i2, int i3, IRemoteCallback iRemoteCallback, IRemoteCallback iRemoteCallback2, boolean z) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionOverrideRequested = true;
            this.mNextAppTransitionPackage = str;
            this.mNextAppTransitionEnter = i;
            this.mNextAppTransitionExit = i2;
            this.mNextAppTransitionBackgroundColor = i3;
            postAnimationCallback();
            this.mNextAppTransitionCallback = iRemoteCallback;
            this.mAnimationFinishedCallback = iRemoteCallback2;
            this.mOverrideTaskTransition = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionScaleUp(int i, int i2, int i3, int i4) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 2;
            putDefaultNextAppTransitionCoordinates(i, i2, i3, i4, null);
            postAnimationCallback();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionClipReveal(int i, int i2, int i3, int i4) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 8;
            putDefaultNextAppTransitionCoordinates(i, i2, i3, i4, null);
            postAnimationCallback();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionThumb(HardwareBuffer hardwareBuffer, int i, int i2, IRemoteCallback iRemoteCallback, boolean z) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = z ? 3 : 4;
            this.mNextAppTransitionScaleUp = z;
            putDefaultNextAppTransitionCoordinates(i, i2, 0, 0, hardwareBuffer);
            postAnimationCallback();
            this.mNextAppTransitionCallback = iRemoteCallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionAspectScaledThumb(HardwareBuffer hardwareBuffer, int i, int i2, int i3, int i4, IRemoteCallback iRemoteCallback, boolean z) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = z ? 5 : 6;
            this.mNextAppTransitionScaleUp = z;
            putDefaultNextAppTransitionCoordinates(i, i2, i3, i4, hardwareBuffer);
            postAnimationCallback();
            this.mNextAppTransitionCallback = iRemoteCallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionMultiThumb(AppTransitionAnimationSpec[] appTransitionAnimationSpecArr, IRemoteCallback iRemoteCallback, IRemoteCallback iRemoteCallback2, boolean z) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = z ? 5 : 6;
            this.mNextAppTransitionScaleUp = z;
            if (appTransitionAnimationSpecArr != null) {
                for (int i = 0; i < appTransitionAnimationSpecArr.length; i++) {
                    AppTransitionAnimationSpec appTransitionAnimationSpec = appTransitionAnimationSpecArr[i];
                    if (appTransitionAnimationSpec != null) {
                        Predicate<Task> obtainPredicate = PooledLambda.obtainPredicate(new AppTransition$$ExternalSyntheticLambda2(), PooledLambda.__(Task.class), Integer.valueOf(appTransitionAnimationSpec.taskId));
                        Task task = this.mDisplayContent.getTask(obtainPredicate);
                        obtainPredicate.recycle();
                        if (task != null) {
                            this.mNextAppTransitionAnimationsSpecs.put(task.hashCode(), appTransitionAnimationSpec);
                            if (i == 0) {
                                Rect rect = appTransitionAnimationSpec.rect;
                                putDefaultNextAppTransitionCoordinates(rect.left, rect.top, rect.width(), rect.height(), appTransitionAnimationSpec.buffer);
                            }
                        }
                    }
                }
            }
            postAnimationCallback();
            this.mNextAppTransitionCallback = iRemoteCallback;
            this.mAnimationFinishedCallback = iRemoteCallback2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionMultiThumbFuture(IAppTransitionAnimationSpecsFuture iAppTransitionAnimationSpecsFuture, IRemoteCallback iRemoteCallback, boolean z) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = z ? 5 : 6;
            this.mNextAppTransitionAnimationsSpecsFuture = iAppTransitionAnimationSpecsFuture;
            this.mNextAppTransitionScaleUp = z;
            this.mNextAppTransitionFutureCallback = iRemoteCallback;
            if (isReady()) {
                fetchAppTransitionSpecsFromFuture();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionRemote(RemoteAnimationAdapter remoteAnimationAdapter) {
        overridePendingAppTransitionRemote(remoteAnimationAdapter, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionRemote(RemoteAnimationAdapter remoteAnimationAdapter, boolean z, boolean z2) {
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 1448683958, 3, (String) null, new Object[]{Boolean.valueOf(isTransitionSet()), String.valueOf(remoteAnimationAdapter)});
        }
        if (!isTransitionSet() || this.mNextAppTransitionIsSync) {
            return;
        }
        clear(!z2);
        this.mNextAppTransitionType = 10;
        this.mRemoteAnimationController = new RemoteAnimationController(this.mService, this.mDisplayContent, remoteAnimationAdapter, this.mHandler, z2);
        Slog.e(TAG, "create RemoteAnimationController " + this.mRemoteAnimationController);
        this.mNextAppTransitionIsSync = z;
    }

    void overrideInPlaceAppTransition(String str, int i) {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 7;
            this.mNextAppTransitionPackage = str;
            this.mNextAppTransitionInPlace = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void overridePendingAppTransitionStartCrossProfileApps() {
        if (canOverridePendingAppTransition()) {
            clear();
            this.mNextAppTransitionType = 9;
            postAnimationCallback();
        }
    }

    private boolean canOverridePendingAppTransition() {
        return isTransitionSet() && this.mNextAppTransitionType != 10;
    }

    private void fetchAppTransitionSpecsFromFuture() {
        final IAppTransitionAnimationSpecsFuture iAppTransitionAnimationSpecsFuture = this.mNextAppTransitionAnimationsSpecsFuture;
        if (iAppTransitionAnimationSpecsFuture != null) {
            this.mNextAppTransitionAnimationsSpecsPending = true;
            this.mNextAppTransitionAnimationsSpecsFuture = null;
            this.mDefaultExecutor.execute(new Runnable() { // from class: com.android.server.wm.AppTransition$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    AppTransition.this.lambda$fetchAppTransitionSpecsFromFuture$1(iAppTransitionAnimationSpecsFuture);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fetchAppTransitionSpecsFromFuture$1(IAppTransitionAnimationSpecsFuture iAppTransitionAnimationSpecsFuture) {
        AppTransitionAnimationSpec[] appTransitionAnimationSpecArr;
        try {
            Binder.allowBlocking(iAppTransitionAnimationSpecsFuture.asBinder());
            appTransitionAnimationSpecArr = iAppTransitionAnimationSpecsFuture.get();
        } catch (RemoteException e) {
            Slog.w(TAG, "Failed to fetch app transition specs: " + e);
            appTransitionAnimationSpecArr = null;
        }
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mNextAppTransitionAnimationsSpecsPending = false;
                overridePendingAppTransitionMultiThumb(appTransitionAnimationSpecArr, this.mNextAppTransitionFutureCallback, null, this.mNextAppTransitionScaleUp);
                this.mNextAppTransitionFutureCallback = null;
                this.mService.requestTraversal();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        WindowManagerService.resetPriorityAfterLockedSection();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("mNextAppTransitionRequests=[");
        Iterator<Integer> it = this.mNextAppTransitionRequests.iterator();
        boolean z = false;
        while (it.hasNext()) {
            Integer next = it.next();
            if (z) {
                sb.append(", ");
            }
            sb.append(appTransitionToString(next.intValue()));
            z = true;
        }
        sb.append("]");
        sb.append(", mNextAppTransitionFlags=" + appTransitionFlagsToString(this.mNextAppTransitionFlags));
        sb.append(",mAppTransitionState=" + appStateToString());
        return sb.toString();
    }

    public static String appTransitionOldToString(int i) {
        switch (i) {
            case -1:
                return "TRANSIT_OLD_UNSET";
            case 0:
                return "TRANSIT_OLD_NONE";
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 17:
            case 19:
            case 27:
            default:
                return "<UNKNOWN: " + i + ">";
            case 6:
                return "TRANSIT_OLD_ACTIVITY_OPEN";
            case 7:
                return "TRANSIT_OLD_ACTIVITY_CLOSE";
            case 8:
                return "TRANSIT_OLD_TASK_OPEN";
            case 9:
                return "TRANSIT_OLD_TASK_CLOSE";
            case 10:
                return "TRANSIT_OLD_TASK_TO_FRONT";
            case 11:
                return "TRANSIT_OLD_TASK_TO_BACK";
            case 12:
                return "TRANSIT_OLD_WALLPAPER_CLOSE";
            case 13:
                return "TRANSIT_OLD_WALLPAPER_OPEN";
            case 14:
                return "TRANSIT_OLD_WALLPAPER_INTRA_OPEN";
            case 15:
                return "TRANSIT_OLD_WALLPAPER_INTRA_CLOSE";
            case 16:
                return "TRANSIT_OLD_TASK_OPEN_BEHIND";
            case 18:
                return "TRANSIT_OLD_ACTIVITY_RELAUNCH";
            case 20:
                return "TRANSIT_OLD_KEYGUARD_GOING_AWAY";
            case 21:
                return "TRANSIT_OLD_KEYGUARD_GOING_AWAY_ON_WALLPAPER";
            case 22:
                return "TRANSIT_OLD_KEYGUARD_OCCLUDE";
            case 23:
                return "TRANSIT_OLD_KEYGUARD_UNOCCLUDE";
            case 24:
                return "TRANSIT_OLD_TRANSLUCENT_ACTIVITY_OPEN";
            case 25:
                return "TRANSIT_OLD_TRANSLUCENT_ACTIVITY_CLOSE";
            case 26:
                return "TRANSIT_OLD_CRASHING_ACTIVITY_CLOSE";
            case 28:
                return "TRANSIT_OLD_TASK_FRAGMENT_OPEN";
            case 29:
                return "TRANSIT_OLD_TASK_FRAGMENT_CLOSE";
            case 30:
                return "TRANSIT_OLD_TASK_FRAGMENT_CHANGE";
            case 31:
                return "TRANSIT_OLD_DREAM_ACTIVITY_OPEN";
            case 32:
                return "TRANSIT_OLD_DREAM_ACTIVITY_CLOSE";
            case 33:
                return "TRANSIT_OLD_KEYGUARD_OCCLUDE_BY_DREAM";
        }
    }

    public static String appTransitionToString(int i) {
        switch (i) {
            case 0:
                return "TRANSIT_NONE";
            case 1:
                return "TRANSIT_OPEN";
            case 2:
                return "TRANSIT_CLOSE";
            case 3:
                return "TRANSIT_TO_FRONT";
            case 4:
                return "TRANSIT_TO_BACK";
            case 5:
                return "TRANSIT_RELAUNCH";
            case 6:
                return "TRANSIT_CHANGE";
            case 7:
                return "TRANSIT_KEYGUARD_GOING_AWAY";
            case 8:
                return "TRANSIT_KEYGUARD_OCCLUDE";
            case 9:
                return "TRANSIT_KEYGUARD_UNOCCLUDE";
            default:
                return "<UNKNOWN: " + i + ">";
        }
    }

    private String appStateToString() {
        int i = this.mAppTransitionState;
        if (i == 0) {
            return "APP_STATE_IDLE";
        }
        if (i == 1) {
            return "APP_STATE_READY";
        }
        if (i == 2) {
            return "APP_STATE_RUNNING";
        }
        if (i == 3) {
            return "APP_STATE_TIMEOUT";
        }
        return "unknown state=" + this.mAppTransitionState;
    }

    private String transitTypeToString() {
        switch (this.mNextAppTransitionType) {
            case 0:
                return "NEXT_TRANSIT_TYPE_NONE";
            case 1:
                return "NEXT_TRANSIT_TYPE_CUSTOM";
            case 2:
                return "NEXT_TRANSIT_TYPE_SCALE_UP";
            case 3:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_UP";
            case 4:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_SCALE_DOWN";
            case 5:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_UP";
            case 6:
                return "NEXT_TRANSIT_TYPE_THUMBNAIL_ASPECT_SCALE_DOWN";
            case 7:
                return "NEXT_TRANSIT_TYPE_CUSTOM_IN_PLACE";
            case 8:
            default:
                return "unknown type=" + this.mNextAppTransitionType;
            case 9:
                return "NEXT_TRANSIT_TYPE_OPEN_CROSS_PROFILE_APPS";
        }
    }

    static {
        ArrayList<Pair<Integer, String>> arrayList = new ArrayList<>();
        sFlagToString = arrayList;
        arrayList.add(new Pair<>(1, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_TO_SHADE"));
        arrayList.add(new Pair<>(2, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_NO_ANIMATION"));
        arrayList.add(new Pair<>(4, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_WITH_WALLPAPER"));
        arrayList.add(new Pair<>(8, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_SUBTLE_ANIMATION"));
        arrayList.add(new Pair<>(512, "TRANSIT_FLAG_KEYGUARD_GOING_AWAY_TO_LAUNCHER_WITH_IN_WINDOW_ANIMATIONS"));
        arrayList.add(new Pair<>(16, "TRANSIT_FLAG_APP_CRASHED"));
        arrayList.add(new Pair<>(32, "TRANSIT_FLAG_OPEN_BEHIND"));
    }

    public static String appTransitionFlagsToString(int i) {
        StringBuilder sb = new StringBuilder();
        Iterator<Pair<Integer, String>> it = sFlagToString.iterator();
        String str = "";
        while (it.hasNext()) {
            Pair<Integer, String> next = it.next();
            if ((((Integer) next.first).intValue() & i) != 0) {
                sb.append(str);
                sb.append((String) next.second);
                str = " | ";
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1159641169921L, this.mAppTransitionState);
        protoOutputStream.write(1159641169922L, this.mLastUsedAppTransition);
        protoOutputStream.end(start);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.println(this);
        printWriter.print(str);
        printWriter.print("mAppTransitionState=");
        printWriter.println(appStateToString());
        if (this.mNextAppTransitionType != 0) {
            printWriter.print(str);
            printWriter.print("mNextAppTransitionType=");
            printWriter.println(transitTypeToString());
        }
        if (this.mNextAppTransitionOverrideRequested || this.mNextAppTransitionType == 1) {
            printWriter.print(str);
            printWriter.print("mNextAppTransitionPackage=");
            printWriter.println(this.mNextAppTransitionPackage);
            printWriter.print(str);
            printWriter.print("mNextAppTransitionEnter=0x");
            printWriter.print(Integer.toHexString(this.mNextAppTransitionEnter));
            printWriter.print(" mNextAppTransitionExit=0x");
            printWriter.println(Integer.toHexString(this.mNextAppTransitionExit));
            printWriter.print(" mNextAppTransitionBackgroundColor=0x");
            printWriter.println(Integer.toHexString(this.mNextAppTransitionBackgroundColor));
        }
        switch (this.mNextAppTransitionType) {
            case 2:
                getDefaultNextAppTransitionStartRect(this.mTmpRect);
                printWriter.print(str);
                printWriter.print("mNextAppTransitionStartX=");
                printWriter.print(this.mTmpRect.left);
                printWriter.print(" mNextAppTransitionStartY=");
                printWriter.println(this.mTmpRect.top);
                printWriter.print(str);
                printWriter.print("mNextAppTransitionStartWidth=");
                printWriter.print(this.mTmpRect.width());
                printWriter.print(" mNextAppTransitionStartHeight=");
                printWriter.println(this.mTmpRect.height());
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                printWriter.print(str);
                printWriter.print("mDefaultNextAppTransitionAnimationSpec=");
                printWriter.println(this.mDefaultNextAppTransitionAnimationSpec);
                printWriter.print(str);
                printWriter.print("mNextAppTransitionAnimationsSpecs=");
                printWriter.println(this.mNextAppTransitionAnimationsSpecs);
                printWriter.print(str);
                printWriter.print("mNextAppTransitionScaleUp=");
                printWriter.println(this.mNextAppTransitionScaleUp);
                break;
            case 7:
                printWriter.print(str);
                printWriter.print("mNextAppTransitionPackage=");
                printWriter.println(this.mNextAppTransitionPackage);
                printWriter.print(str);
                printWriter.print("mNextAppTransitionInPlace=0x");
                printWriter.print(Integer.toHexString(this.mNextAppTransitionInPlace));
                break;
        }
        if (this.mNextAppTransitionCallback != null) {
            printWriter.print(str);
            printWriter.print("mNextAppTransitionCallback=");
            printWriter.println(this.mNextAppTransitionCallback);
        }
        if (this.mLastUsedAppTransition != 0) {
            printWriter.print(str);
            printWriter.print("mLastUsedAppTransition=");
            printWriter.println(appTransitionOldToString(this.mLastUsedAppTransition));
            printWriter.print(str);
            printWriter.print("mLastOpeningApp=");
            printWriter.println(this.mLastOpeningApp);
            printWriter.print(str);
            printWriter.print("mLastClosingApp=");
            printWriter.println(this.mLastClosingApp);
            printWriter.print(str);
            printWriter.print("mLastChangingApp=");
            printWriter.println(this.mLastChangingApp);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean prepareAppTransition(int i, int i2) {
        if (this.mDisplayContent.mTransitionController.isShellTransitionsEnabled()) {
            return false;
        }
        this.mNextAppTransitionRequests.add(Integer.valueOf(i));
        this.mNextAppTransitionFlags = i2 | this.mNextAppTransitionFlags;
        updateBooster();
        removeAppTransitionTimeoutCallbacks();
        this.mHandler.postDelayed(this.mHandleAppTransitionTimeoutRunnable, APP_TRANSITION_TIMEOUT_MS);
        this.mAppTransitionWrapper.getExtImpl().postAppTransitionDelayedCallback(this.mHandler, i, this.mRemoteAnimationController, this.mDisplayContent);
        return prepare();
    }

    static boolean isKeyguardTransitOld(int i) {
        return isKeyguardGoingAwayTransitOld(i) || isKeyguardOccludeTransitOld(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTaskTransitOld(int i) {
        return isTaskOpenTransitOld(i) || isTaskCloseTransitOld(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getKeyguardTransition() {
        if (this.mNextAppTransitionRequests.indexOf(7) != -1) {
            return 7;
        }
        int indexOf = this.mNextAppTransitionRequests.indexOf(9);
        int indexOf2 = this.mNextAppTransitionRequests.indexOf(8);
        if (indexOf == -1 && indexOf2 == -1) {
            return 0;
        }
        if (indexOf == -1 || indexOf >= indexOf2) {
            return indexOf != -1 ? 9 : 8;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getFirstAppTransition() {
        for (int i = 0; i < this.mNextAppTransitionRequests.size(); i++) {
            int intValue = this.mNextAppTransitionRequests.get(i).intValue();
            if (intValue != 0 && !isKeyguardTransit(intValue)) {
                return intValue;
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsTransitRequest(int i) {
        return this.mNextAppTransitionRequests.contains(Integer.valueOf(i));
    }

    private boolean shouldScaleDownThumbnailTransition(int i, int i2) {
        return this.mGridLayoutRecentsEnabled || i2 == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleAppTransitionTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$new$0() {
        WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                DisplayContent displayContent = this.mDisplayContent;
                if (displayContent == null) {
                    WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                notifyAppTransitionTimeoutLocked();
                if (isTransitionSet() || !displayContent.mOpeningApps.isEmpty() || !displayContent.mClosingApps.isEmpty() || !displayContent.mChangingContainers.isEmpty()) {
                    this.mAppTransitionWrapper.getExtImpl().appTransitionTimeout(this.mService, displayContent);
                    if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
                        ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, 344795667, 349, (String) null, new Object[]{Long.valueOf(displayContent.getDisplayId()), Boolean.valueOf(displayContent.mAppTransition.isTransitionSet()), Long.valueOf(displayContent.mOpeningApps.size()), Long.valueOf(displayContent.mClosingApps.size()), Long.valueOf(displayContent.mChangingContainers.size())});
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    ActivityRecord activityRecord = displayContent.mFocusedApp;
                    this.mTheiaManagerExt.sendEvent(259L, currentTimeMillis, 0, 0, 4099L, activityRecord != null ? activityRecord.packageName : null);
                    setTimeout();
                    this.mService.mWindowPlacerLocked.performSurfacePlacement();
                }
                WindowManagerService.resetPriorityAfterLockedSection();
            } catch (Throwable th) {
                WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doAnimationCallback(IRemoteCallback iRemoteCallback) {
        try {
            iRemoteCallback.sendResult((Bundle) null);
        } catch (RemoteException unused) {
        }
    }

    private void setAppTransitionFinishedCallbackIfNeeded(Animation animation) {
        IRemoteCallback iRemoteCallback = this.mAnimationFinishedCallback;
        if (iRemoteCallback == null || animation == null) {
            return;
        }
        animation.setAnimationListener(new AnonymousClass1(iRemoteCallback));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.server.wm.AppTransition$1, reason: invalid class name */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AnonymousClass1 implements Animation.AnimationListener {
        final /* synthetic */ IRemoteCallback val$callback;

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        AnonymousClass1(IRemoteCallback iRemoteCallback) {
            this.val$callback = iRemoteCallback;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            AppTransition.this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.server.wm.AppTransition$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AppTransition.doAnimationCallback((IRemoteCallback) obj);
                }
            }, this.val$callback));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAppTransitionTimeoutCallbacks() {
        this.mHandler.removeCallbacks(this.mHandleAppTransitionTimeoutRunnable);
        this.mAppTransitionWrapper.getExtImpl().removeAppTransitionDelayedCallback(this.mHandler);
    }

    public IAppTransitionWrapper getWrapper() {
        return this.mAppTransitionWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class AppTransitionWrapper implements IAppTransitionWrapper {
        private IAppTransitionExt mAppTransitionExt;

        private AppTransitionWrapper() {
            this.mAppTransitionExt = (IAppTransitionExt) ExtLoader.type(IAppTransitionExt.class).base(AppTransition.this).create();
        }

        @Override // com.android.server.wm.IAppTransitionWrapper
        public IAppTransitionExt getExtImpl() {
            return this.mAppTransitionExt;
        }

        @Override // com.android.server.wm.IAppTransitionWrapper
        public String getNextAppTransitionPackage() {
            return AppTransition.this.mNextAppTransitionPackage;
        }

        @Override // com.android.server.wm.IAppTransitionWrapper
        public int getNextAppTransitionType() {
            return AppTransition.this.mNextAppTransitionType;
        }
    }
}
