package com.android.server.wm;

import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.proto.ProtoOutputStream;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import com.android.server.policy.WindowManagerPolicy;
import com.android.server.wm.SurfaceAnimator;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Consumer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class NonAppWindowAnimationAdapter implements AnimationAdapter {
    private SurfaceControl mCapturedLeash;
    private SurfaceAnimator.OnAnimationFinishedCallback mCapturedLeashFinishCallback;
    private long mDurationHint;
    private int mLastAnimationType;
    private long mStatusBarTransitionDelay;
    private Point mSurfacePosition = new Point();
    private RemoteAnimationTarget mTarget;
    private final WindowContainer mWindowContainer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean shouldStartNonAppWindowAnimationsForKeyguardExit(int i) {
        return i == 20 || i == 21;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public boolean getShowWallpaper() {
        return false;
    }

    NonAppWindowAnimationAdapter(WindowContainer windowContainer, long j, long j2) {
        this.mWindowContainer = windowContainer;
        this.mDurationHint = j;
        this.mStatusBarTransitionDelay = j2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RemoteAnimationTarget[] startNonAppWindowAnimations(WindowManagerService windowManagerService, DisplayContent displayContent, int i, long j, long j2, ArrayList<NonAppWindowAnimationAdapter> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        if (shouldStartNonAppWindowAnimationsForKeyguardExit(i)) {
            startNonAppWindowAnimationsForKeyguardExit(windowManagerService, j, j2, arrayList2, arrayList);
        } else if (shouldAttachNavBarToApp(windowManagerService, displayContent, i)) {
            startNavigationBarWindowAnimation(displayContent, j, j2, arrayList2, arrayList);
        }
        return (RemoteAnimationTarget[]) arrayList2.toArray(new RemoteAnimationTarget[arrayList2.size()]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean shouldAttachNavBarToApp(WindowManagerService windowManagerService, DisplayContent displayContent, int i) {
        return (i == 8 || i == 10 || i == 12) && displayContent.getDisplayPolicy().shouldAttachNavBarToAppDuringTransition() && windowManagerService.getRecentsAnimationController() == null && displayContent.getAsyncRotationController() == null;
    }

    private static void startNonAppWindowAnimationsForKeyguardExit(final WindowManagerService windowManagerService, final long j, final long j2, final ArrayList<RemoteAnimationTarget> arrayList, final ArrayList<NonAppWindowAnimationAdapter> arrayList2) {
        WindowManagerPolicy windowManagerPolicy = windowManagerService.mPolicy;
        windowManagerService.mRoot.forAllWindows(new Consumer() { // from class: com.android.server.wm.NonAppWindowAnimationAdapter$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NonAppWindowAnimationAdapter.lambda$startNonAppWindowAnimationsForKeyguardExit$0(WindowManagerService.this, j, j2, arrayList2, arrayList, (WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$startNonAppWindowAnimationsForKeyguardExit$0(WindowManagerService windowManagerService, long j, long j2, ArrayList arrayList, ArrayList arrayList2, WindowState windowState) {
        if (windowState.mActivityRecord == null && windowState.canBeHiddenByKeyguard() && windowState.wouldBeVisibleIfPolicyIgnored() && !windowState.isVisible() && windowState != windowManagerService.mRoot.getCurrentInputMethodWindow()) {
            NonAppWindowAnimationAdapter nonAppWindowAnimationAdapter = new NonAppWindowAnimationAdapter(windowState, j, j2);
            nonAppWindowAnimationAdapter.updateSurfacePosition(windowState);
            arrayList.add(nonAppWindowAnimationAdapter);
            windowState.startAnimation(windowState.getPendingTransaction(), nonAppWindowAnimationAdapter, false, 16);
            arrayList2.add(nonAppWindowAnimationAdapter.createRemoteAnimationTarget());
        }
    }

    private static void startNavigationBarWindowAnimation(DisplayContent displayContent, long j, long j2, ArrayList<RemoteAnimationTarget> arrayList, ArrayList<NonAppWindowAnimationAdapter> arrayList2) {
        WindowState navigationBar = displayContent.getDisplayPolicy().getNavigationBar();
        NonAppWindowAnimationAdapter nonAppWindowAnimationAdapter = new NonAppWindowAnimationAdapter(navigationBar.mToken, j, j2);
        nonAppWindowAnimationAdapter.updateSurfacePosition(navigationBar);
        arrayList2.add(nonAppWindowAnimationAdapter);
        WindowToken windowToken = navigationBar.mToken;
        windowToken.startAnimation(windowToken.getPendingTransaction(), nonAppWindowAnimationAdapter, false, 16);
        arrayList.add(nonAppWindowAnimationAdapter.createRemoteAnimationTarget());
    }

    private void updateSurfacePosition(WindowState windowState) {
        Point lastSurfacePosition = windowState.getLastSurfacePosition();
        this.mSurfacePosition.set(lastSurfacePosition.x, lastSurfacePosition.y);
    }

    RemoteAnimationTarget createRemoteAnimationTarget() {
        RemoteAnimationTarget remoteAnimationTarget = new RemoteAnimationTarget(-1, -1, getLeash(), false, new Rect(), (Rect) null, this.mWindowContainer.getPrefixOrderIndex(), this.mSurfacePosition, this.mWindowContainer.getBounds(), (Rect) null, this.mWindowContainer.getWindowConfiguration(), true, (SurfaceControl) null, (Rect) null, (ActivityManager.RunningTaskInfo) null, false, this.mWindowContainer.getWindowType());
        this.mTarget = remoteAnimationTarget;
        return remoteAnimationTarget;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void startAnimation(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, int i, SurfaceAnimator.OnAnimationFinishedCallback onAnimationFinishedCallback) {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, 1999594750, 0, (String) null, (Object[]) null);
        }
        this.mCapturedLeash = surfaceControl;
        this.mCapturedLeashFinishCallback = onAnimationFinishedCallback;
        this.mLastAnimationType = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceAnimator.OnAnimationFinishedCallback getLeashFinishedCallback() {
        return this.mCapturedLeashFinishCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLastAnimationType() {
        return this.mLastAnimationType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowContainer getWindowContainer() {
        return this.mWindowContainer;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getDurationHint() {
        return this.mDurationHint;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public long getStatusBarTransitionsStartTime() {
        return SystemClock.uptimeMillis() + this.mStatusBarTransitionDelay;
    }

    SurfaceControl getLeash() {
        return this.mCapturedLeash;
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void onAnimationCancelled(SurfaceControl surfaceControl) {
        if (ProtoLogCache.WM_DEBUG_REMOTE_ANIMATIONS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_REMOTE_ANIMATIONS, -1153814764, 0, (String) null, (Object[]) null);
        }
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void dump(PrintWriter printWriter, String str) {
        printWriter.print(str);
        printWriter.print("windowContainer=");
        printWriter.println(this.mWindowContainer);
        if (this.mTarget != null) {
            printWriter.print(str);
            printWriter.println("Target:");
            this.mTarget.dump(printWriter, str + "  ");
            return;
        }
        printWriter.print(str);
        printWriter.println("Target: null");
    }

    @Override // com.android.server.wm.AnimationAdapter
    public void dumpDebug(ProtoOutputStream protoOutputStream) {
        long start = protoOutputStream.start(1146756268034L);
        RemoteAnimationTarget remoteAnimationTarget = this.mTarget;
        if (remoteAnimationTarget != null) {
            remoteAnimationTarget.dumpDebug(protoOutputStream, 1146756268033L);
        }
        protoOutputStream.end(start);
    }
}
