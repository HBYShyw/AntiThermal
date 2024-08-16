package com.oplus.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.IApplicationThread;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.HardwareRenderer;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import android.view.RemoteAnimationTarget;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.Window;
import android.window.IRemoteTransition;
import android.window.RemoteTransition;
import com.oplus.animation.RemoteTransitionManager;
import com.oplus.wrapper.window.IRemoteTransition;
import com.oplus.wrapper.window.IRemoteTransitionFinishedCallback;
import com.oplus.wrapper.window.TransitionInfo;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RemoteTransitionManager {
    private static final String TAG = "RemoteTransitionManager";
    private Activity mActivity;
    private RemoteAnimationDefinition mDefinition;
    private Handler mHandler;
    private ViewRootImpl mViewRootImpl;
    public static final int TRANSIT_UNSET = getOriginType("TRANSIT_UNSET");
    public static final int TRANSIT_NONE = getOriginType("TRANSIT_OLD_NONE");
    public static final int TRANSIT_ACTIVITY_OPEN = getOriginType("TRANSIT_OLD_ACTIVITY_OPEN");
    public static final int TRANSIT_ACTIVITY_CLOSE = getOriginType("TRANSIT_OLD_ACTIVITY_CLOSE");
    public static final int TRANSIT_TASK_OPEN = getOriginType("TRANSIT_OLD_TASK_OPEN");
    public static final int TRANSIT_TASK_CLOSE = getOriginType("TRANSIT_OLD_TASK_CLOSE");
    public static final int TRANSIT_TASK_TO_FRONT = getOriginType("TRANSIT_OLD_TASK_TO_FRONT");
    public static final int TRANSIT_TASK_TO_BACK = getOriginType("TRANSIT_OLD_TASK_TO_BACK");
    public static final int TRANSIT_WALLPAPER_CLOSE = getOriginType("TRANSIT_OLD_WALLPAPER_CLOSE");
    public static final int TRANSIT_WALLPAPER_OPEN = getOriginType("TRANSIT_OLD_WALLPAPER_OPEN");
    public static final int TRANSIT_WALLPAPER_INTRA_OPEN = getOriginType("TRANSIT_OLD_WALLPAPER_INTRA_OPEN");
    public static final int TRANSIT_WALLPAPER_INTRA_CLOSE = getOriginType("TRANSIT_OLD_WALLPAPER_INTRA_CLOSE");
    public static final int TRANSIT_TASK_OPEN_BEHIND = getOriginType("TRANSIT_OLD_TASK_OPEN_BEHIND");
    public static final int TRANSIT_ACTIVITY_RELAUNCH = getOriginType("TRANSIT_OLD_ACTIVITY_RELAUNCH");
    public static final int TRANSIT_KEYGUARD_GOING_AWAY = getOriginType("TRANSIT_OLD_KEYGUARD_GOING_AWAY");
    public static final int TRANSIT_KEYGUARD_GOING_AWAY_ON_WALLPAPER = getOriginType("TRANSIT_OLD_KEYGUARD_GOING_AWAY_ON_WALLPAPER");
    public static final int TRANSIT_KEYGUARD_OCCLUDE = getOriginType("TRANSIT_OLD_KEYGUARD_OCCLUDE");
    public static final int TRANSIT_KEYGUARD_UNOCCLUDE = getOriginType("TRANSIT_OLD_KEYGUARD_UNOCCLUDE");
    public static final int ACTIVITY_TYPE_STANDARD = getOriginType("ACTIVITY_TYPE_STANDARD");

    public RemoteTransitionManager(View rootView) {
        this.mViewRootImpl = null;
        this.mActivity = null;
        this.mHandler = null;
        this.mDefinition = null;
        if (rootView != null) {
            this.mViewRootImpl = rootView.getViewRootImpl();
        }
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mDefinition = new RemoteAnimationDefinition();
    }

    public RemoteTransitionManager(Activity activity) {
        View decorView;
        this.mViewRootImpl = null;
        this.mActivity = null;
        this.mHandler = null;
        this.mDefinition = null;
        this.mActivity = activity;
        Window window = activity != null ? activity.getWindow() : null;
        if (window != null && (decorView = window.getDecorView()) != null) {
            this.mViewRootImpl = decorView.getViewRootImpl();
        }
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mDefinition = new RemoteAnimationDefinition();
    }

    public ActivityOptions getActivityLaunchOptions(RemoteAnimationCallbackWrapper animationCallback, long duration, long statusBarTransitionDelay) {
        RemoteAnimationAdapter adapter = new RemoteAnimationAdapter(makeRemoteAnimationRunner(animationCallback), duration, statusBarTransitionDelay);
        Log.d(TAG, "getActivityLaunchOptions duration:" + duration + ", statusBarTransitionDelay:" + statusBarTransitionDelay);
        return ActivityOptions.makeRemoteAnimation(adapter);
    }

    public ActivityOptions getActivityLaunchOptionsForTransition(RemoteAnimationCallbackWrapper animationCallback, RemoteTransitionCallbackWrapper transitionCallback, long duration, long statusBarTransitionDelay) {
        IApplicationThread applicationThread;
        IRemoteAnimationRunner.Stub makeRemoteAnimationRunner = makeRemoteAnimationRunner(animationCallback);
        IRemoteTransition originTransition = makeRemoteTransition(transitionCallback).asBinder();
        ViewRootImpl viewRootImpl = this.mViewRootImpl;
        if (viewRootImpl != null) {
            IApplicationThread applicationThread2 = viewRootImpl.mContext.getIApplicationThread();
            applicationThread = applicationThread2;
        } else {
            Activity activity = this.mActivity;
            if (activity == null) {
                applicationThread = null;
            } else {
                IApplicationThread applicationThread3 = activity.getIApplicationThread();
                applicationThread = applicationThread3;
            }
        }
        RemoteTransition remoteTransition = new RemoteTransition(originTransition, applicationThread, "QuickstepLaunch");
        RemoteAnimationAdapter adapter = new RemoteAnimationAdapter(makeRemoteAnimationRunner, duration, statusBarTransitionDelay);
        Log.d(TAG, "getActivityLaunchOptionsForTransition duration:" + duration + ", statusBarTransitionDelay:" + statusBarTransitionDelay);
        return ActivityOptions.makeRemoteAnimation(adapter, remoteTransition);
    }

    public void addRemoteAnimation(int transition, int activityTypeFilter, RemoteAnimationCallbackWrapper animationCallback, long duration, long statusBarTransitionDelay) {
        RemoteAnimationAdapter adapter = new RemoteAnimationAdapter(makeRemoteAnimationRunner(animationCallback), duration, statusBarTransitionDelay);
        this.mDefinition.addRemoteAnimation(transition, activityTypeFilter, adapter);
        Log.d(TAG, "addRemoteAnimation transition:" + transition + ", activityTypeFilter:" + activityTypeFilter);
    }

    public void registerRemoteAnimationsForWindow(View rootView) {
        if (rootView == null) {
            Log.w(TAG, "registerRemoteAnimationsForWindow: rootView is null");
            return;
        }
        ViewRootImpl viewRoot = rootView.getViewRootImpl();
        if (viewRoot != null) {
            Log.d(TAG, "registerRemoteAnimationsForWindow " + viewRoot);
            viewRoot.getWrapper().getExtImpl().registerRemoteAnimationsForWindow(this.mDefinition);
        } else {
            Log.e(TAG, "registerRemoteAnimationsForWindow: viewRoot is null!");
        }
    }

    public void registerRemoteAnimationsForActivity() {
        if (this.mActivity != null) {
            Log.d(TAG, "registerRemoteAnimationsForActivity " + this.mActivity + ", definition:" + this.mDefinition);
            this.mActivity.registerRemoteAnimations(this.mDefinition);
        }
    }

    public void unregisterRemoteAnimationsForWindow(View rootView) {
        if (rootView == null) {
            Log.w(TAG, "unregisterRemoteAnimationsForWindow: rootView is null");
            return;
        }
        ViewRootImpl viewRoot = rootView.getViewRootImpl();
        if (viewRoot != null) {
            Log.d(TAG, "unregisterRemoteAnimationsForWindow " + viewRoot);
            viewRoot.getWrapper().getExtImpl().unregisterRemoteAnimationsForWindow();
        } else {
            Log.e(TAG, "unregisterRemoteAnimationsForWindow: viewRoot is null!");
        }
    }

    public void unregisterRemoteAnimationsForActivity() {
        if (this.mActivity != null) {
            Log.d(TAG, "unregisterRemoteAnimationsForActivity " + this.mActivity);
            this.mActivity.unregisterRemoteAnimations();
        }
    }

    public LaunchViewInfo createLaunchViewInfo(View launchView, String launchPackage, Point extLocation) {
        if (launchView == null || launchPackage == null || launchPackage.isEmpty()) {
            return null;
        }
        LaunchViewInfo launchViewInfo = null;
        Point viewLocation = new Point();
        if (extLocation != null) {
            viewLocation = extLocation;
        } else {
            int[] pts = new int[2];
            launchView.getLocationOnScreen(pts);
            viewLocation.x = pts[0];
            viewLocation.y = pts[1];
        }
        SurfaceControl viewSurface = transferViewToSurface(launchView);
        if (viewSurface != null) {
            launchViewInfo = new LaunchViewInfo(viewSurface, viewLocation, launchPackage);
        }
        Log.d(TAG, "createLaunchViewInfo: launchViewInfo=" + launchViewInfo);
        return launchViewInfo;
    }

    public void setLaunchViewInfoForWindow(LaunchViewInfo launchViewInfo) {
        if (this.mViewRootImpl == null) {
            return;
        }
        Log.d(TAG, "setLaunchViewInfoForWindow: launchViewInfo=" + launchViewInfo);
        this.mViewRootImpl.getWrapper().getExtImpl().setLaunchViewInfoForWindow(launchViewInfo);
    }

    public void clearLaunchViewInfoForWindow() {
        if (this.mViewRootImpl == null) {
            return;
        }
        Log.d(TAG, "clearLaunchViewInfoForWindow");
        this.mViewRootImpl.getWrapper().getExtImpl().clearLaunchViewInfoForWindow();
    }

    public void setWallpaperZoomOut(IBinder windowToken, float zoom, Context context) {
        if (context != null && windowToken != null) {
            WallpaperManager.getInstance(context).setWallpaperZoomOut(windowToken, zoom);
        }
    }

    public void scheduleApply(final ArrayList<SurfaceParamsWrapper> params) {
        ViewRootImpl viewRootImpl = this.mViewRootImpl;
        if (viewRootImpl != null && params != null) {
            viewRootImpl.registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: com.oplus.animation.RemoteTransitionManager$$ExternalSyntheticLambda0
                public final void onFrameDraw(long j) {
                    RemoteTransitionManager.this.lambda$scheduleApply$0(params, j);
                }
            });
            this.mViewRootImpl.getView().invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleApply$0(ArrayList params, long frame) {
        SurfaceControl.Transaction t = new SurfaceControl.Transaction();
        for (int i = params.size() - 1; i >= 0; i--) {
            SurfaceParamsWrapper surfaceParamsWrapper = (SurfaceParamsWrapper) params.get(i);
            if (surfaceParamsWrapper.mSurface.isValid()) {
                surfaceParamsWrapper.applyTo(t);
            }
        }
        this.mViewRootImpl.mergeWithNextTransaction(t, frame);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static int getOriginType(String typeName) {
        char c;
        switch (typeName.hashCode()) {
            case -1822685521:
                if (typeName.equals("TRANSIT_WALLPAPER_CLOSE")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1576518349:
                if (typeName.equals("TRANSIT_KEYGUARD_UNOCCLUDE")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -1552460630:
                if (typeName.equals("TRANSIT_TASK_CLOSE")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1439230595:
                if (typeName.equals("TRANSIT_UNSET")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1250860050:
                if (typeName.equals("TRANSIT_ACTIVITY_OPEN")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1246529568:
                if (typeName.equals("TRANSIT_KEYGUARD_GOING_AWAY")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -751171949:
                if (typeName.equals("TRANSIT_WALLPAPER_OPEN")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -488803282:
                if (typeName.equals("TRANSIT_WALLPAPER_INTRA_CLOSE")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -289257812:
                if (typeName.equals("TRANSIT_KEYGUARD_OCCLUDE")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -133147468:
                if (typeName.equals("TRANSIT_ACTIVITY_CLOSE")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 89478529:
                if (typeName.equals("TRANSIT_KEYGUARD_GOING_AWAY_ON_WALLPAPER")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 131983602:
                if (typeName.equals("ACTIVITY_TYPE_STANDARD")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 306398026:
                if (typeName.equals("TRANSIT_ACTIVITY_RELAUNCH")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 365923640:
                if (typeName.equals("TRANSIT_TASK_OPEN")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 741802621:
                if (typeName.equals("TRANSIT_TASK_TO_BACK")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 787366780:
                if (typeName.equals("TRANSIT_OLD_NONE")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1092971828:
                if (typeName.equals("TRANSIT_WALLPAPER_INTRA_OPEN")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1525257011:
                if (typeName.equals("TRANSIT_TASK_TO_FRONT")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1749526049:
                if (typeName.equals("TRANSIT_TASK_OPEN_BEHIND")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return -1;
            case 1:
                return 0;
            case 2:
                return 6;
            case 3:
                return 7;
            case 4:
                return 8;
            case 5:
                return 9;
            case 6:
                return 10;
            case 7:
                return 11;
            case '\b':
                return 12;
            case '\t':
                return 13;
            case '\n':
                return 14;
            case 11:
                return 15;
            case '\f':
                return 16;
            case '\r':
                return 18;
            case 14:
                return 20;
            case 15:
                return 21;
            case 16:
                return 22;
            case 17:
                return 23;
            case 18:
                return 1;
            default:
                return -1;
        }
    }

    private SurfaceControl transferViewToSurface(View launchView) {
        Canvas canvas;
        if (launchView == null) {
            return null;
        }
        ViewRootImpl root = launchView.getViewRootImpl();
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(launchView);
        Point shadowSize = new Point();
        Point shadowTouchPoint = new Point();
        shadowBuilder.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        if (shadowSize.x < 0 || shadowSize.y < 0 || shadowTouchPoint.x < 0 || shadowTouchPoint.y < 0) {
            Log.e(TAG, "transferViewToSurface: launchView dimensions must not be negative");
            return null;
        }
        if (shadowSize.x == 0 || shadowSize.y == 0) {
            shadowSize.x = 1;
            shadowSize.y = 1;
        }
        SurfaceSession session = new SurfaceSession();
        SurfaceControl surfaceControl = new SurfaceControl.Builder(session).setName("launchView_surface").setParent(root.getSurfaceControl()).setBufferSize(shadowSize.x, shadowSize.y).setCallsite("transferViewToSurface").build();
        Surface surface = new Surface();
        surface.copyFrom(surfaceControl);
        if (launchView.isHardwareAccelerated()) {
            canvas = surface.lockHardwareCanvas();
        } else {
            canvas = surface.lockCanvas(null);
        }
        try {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            shadowBuilder.onDrawShadow(canvas);
            return surfaceControl;
        } finally {
            surface.unlockCanvasAndPost(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oplus.animation.RemoteTransitionManager$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IRemoteAnimationRunner.Stub {
        final /* synthetic */ RemoteAnimationCallbackWrapper val$animationCallback;

        AnonymousClass1(RemoteAnimationCallbackWrapper remoteAnimationCallbackWrapper) {
            this.val$animationCallback = remoteAnimationCallbackWrapper;
        }

        public void onAnimationStart(final int transit, RemoteAnimationTarget[] apps, RemoteAnimationTarget[] wallpapers, RemoteAnimationTarget[] nonApps, final IRemoteAnimationFinishedCallback finishedCallback) {
            final RemoteAnimationTargetWrapper[] appsCompat = RemoteTransitionManager.this.wrapRemoteAnimationTarget(apps);
            final RemoteAnimationTargetWrapper[] wallpapersCompat = RemoteTransitionManager.this.wrapRemoteAnimationTarget(wallpapers);
            final RemoteAnimationTargetWrapper[] nonAppsCompat = RemoteTransitionManager.this.wrapRemoteAnimationTarget(nonApps);
            Log.d(RemoteTransitionManager.TAG, "onAnimationStart");
            final RemoteAnimationCallbackWrapper remoteAnimationCallbackWrapper = this.val$animationCallback;
            Runnable r = new Runnable() { // from class: com.oplus.animation.RemoteTransitionManager$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteTransitionManager.AnonymousClass1.this.lambda$onAnimationStart$0(remoteAnimationCallbackWrapper, transit, appsCompat, wallpapersCompat, nonAppsCompat, finishedCallback);
                }
            };
            RemoteTransitionManager remoteTransitionManager = RemoteTransitionManager.this;
            remoteTransitionManager.postAsyncCallback(remoteTransitionManager.mHandler, r);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationStart$0(RemoteAnimationCallbackWrapper animationCallback, int transit, RemoteAnimationTargetWrapper[] appsCompat, RemoteAnimationTargetWrapper[] wallpapersCompat, RemoteAnimationTargetWrapper[] nonAppsCompat, final IRemoteAnimationFinishedCallback finishedCallback) {
            AnimatorSet animation = animationCallback.onCreateAnimation(transit, appsCompat, wallpapersCompat, nonAppsCompat);
            animation.addListener(new AnimatorListenerAdapter() { // from class: com.oplus.animation.RemoteTransitionManager.1.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation2) {
                    try {
                        Log.e(RemoteTransitionManager.TAG, "onAnimationEnd");
                        finishedCallback.onAnimationFinished();
                    } catch (RemoteException e) {
                        Log.e(RemoteTransitionManager.TAG, "Failed to call finished callback ", e);
                    }
                }
            });
            animation.start();
        }

        public void onAnimationCancelled() {
            Log.d(RemoteTransitionManager.TAG, "onAnimationCancelled");
            RemoteTransitionManager remoteTransitionManager = RemoteTransitionManager.this;
            Handler handler = remoteTransitionManager.mHandler;
            final RemoteAnimationCallbackWrapper remoteAnimationCallbackWrapper = this.val$animationCallback;
            remoteTransitionManager.postAsyncCallback(handler, new Runnable() { // from class: com.oplus.animation.RemoteTransitionManager$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteAnimationCallbackWrapper.this.onAnimationCancelled();
                }
            });
        }
    }

    private IRemoteAnimationRunner.Stub makeRemoteAnimationRunner(RemoteAnimationCallbackWrapper animationCallback) {
        return new AnonymousClass1(animationCallback);
    }

    private IRemoteTransition.Stub makeRemoteTransition(final RemoteTransitionCallbackWrapper animationCallback) {
        return new IRemoteTransition.Stub() { // from class: com.oplus.animation.RemoteTransitionManager.2
            @Override // com.oplus.wrapper.window.IRemoteTransition
            public void startAnimation(IBinder token, TransitionInfo info, SurfaceControl.Transaction t, IRemoteTransitionFinishedCallback finishCallback) throws RemoteException {
                animationCallback.startAnimation(token, info, t, finishCallback);
            }

            @Override // com.oplus.wrapper.window.IRemoteTransition
            public void mergeAnimation(IBinder transition, TransitionInfo info, SurfaceControl.Transaction t, IBinder mergeTarget, IRemoteTransitionFinishedCallback finishCallback) throws RemoteException {
                animationCallback.mergeAnimation(transition, info, t, mergeTarget, finishCallback);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postAsyncCallback(Handler handler, Runnable callback) {
        Message msg = Message.obtain(handler, callback);
        msg.setAsynchronous(true);
        handler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RemoteAnimationTargetWrapper[] wrapRemoteAnimationTarget(RemoteAnimationTarget[] apps) {
        int length = apps != null ? apps.length : 0;
        RemoteAnimationTargetWrapper[] appsCompat = new RemoteAnimationTargetWrapper[length];
        for (int i = 0; i < length; i++) {
            appsCompat[i] = new RemoteAnimationTargetWrapper(apps[i]);
            Log.d(TAG, "client-wrapRemoteAnimationTarget, appsCompat" + appsCompat[i] + ", apps[i]=" + apps[i] + ", launchViewInfo=" + appsCompat[i].mLaunchViewInfo);
        }
        return appsCompat;
    }
}
