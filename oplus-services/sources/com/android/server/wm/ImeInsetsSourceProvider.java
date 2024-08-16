package com.android.server.wm;

import android.graphics.Rect;
import android.os.Trace;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.InsetsSource;
import android.view.InsetsSourceControl;
import android.view.WindowInsets;
import android.view.inputmethod.ImeTracker;
import android.window.TaskSnapshot;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.io.PrintWriter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class ImeInsetsSourceProvider extends InsetsSourceProvider {
    private static final String TAG = "ImeInsetsSourceProvider";
    private boolean mFrozen;
    private InsetsControlTarget mImeRequester;
    private ImeTracker.Token mImeRequesterStatsToken;
    private boolean mImeShowing;
    private boolean mIsImeLayoutDrawn;
    private final InsetsSource mLastSource;
    private boolean mServerVisible;
    private Runnable mShowImeRunner;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImeInsetsSourceProvider(InsetsSource insetsSource, InsetsStateController insetsStateController, DisplayContent displayContent) {
        super(insetsSource, insetsStateController, displayContent);
        this.mLastSource = new InsetsSource(InsetsSource.ID_IME, WindowInsets.Type.ime());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.InsetsSourceProvider
    public InsetsSourceControl getControl(InsetsControlTarget insetsControlTarget) {
        InsetsSourceControl control = super.getControl(insetsControlTarget);
        if (control != null && insetsControlTarget != null && insetsControlTarget.getWindow() != null) {
            WindowState window = insetsControlTarget.getWindow();
            boolean z = false;
            TaskSnapshot taskSnapshot = window.getRootTask() != null ? window.mWmService.getTaskSnapshot(window.getRootTask().mTaskId, 0, false, false) : null;
            ActivityRecord activityRecord = window.mActivityRecord;
            if (activityRecord != null && activityRecord.hasStartingWindow() && taskSnapshot != null && taskSnapshot.hasImeSurface()) {
                z = true;
            }
            control.setSkipAnimationOnce(z);
        }
        return control;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.InsetsSourceProvider
    public void setClientVisible(boolean z) {
        InsetsControlTarget controlTarget;
        boolean isClientVisible = isClientVisible();
        super.setClientVisible(z);
        if (isClientVisible || !isClientVisible() || (controlTarget = getControlTarget()) == null || controlTarget.getWindow() == null || controlTarget.getWindow().mActivityRecord != null) {
            return;
        }
        this.mDisplayContent.assignWindowLayers(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.InsetsSourceProvider
    public void setServerVisible(boolean z) {
        this.mServerVisible = z;
        if (this.mFrozen) {
            return;
        }
        super.setServerVisible(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFrozen(boolean z) {
        if (this.mFrozen == z) {
            return;
        }
        this.mFrozen = z;
        if (z) {
            return;
        }
        super.setServerVisible(this.mServerVisible);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.InsetsSourceProvider
    public void updateSourceFrame(Rect rect) {
        super.updateSourceFrame(rect);
        onSourceChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.InsetsSourceProvider
    public void updateVisibility() {
        super.updateVisibility();
        onSourceChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.InsetsSourceProvider
    public void updateControlForTarget(InsetsControlTarget insetsControlTarget, boolean z) {
        if (insetsControlTarget != null && insetsControlTarget.getWindow() != null) {
            insetsControlTarget = insetsControlTarget.getWindow().getImeControlTarget();
        }
        super.updateControlForTarget(insetsControlTarget, z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.InsetsSourceProvider
    public boolean updateClientVisibility(InsetsControlTarget insetsControlTarget) {
        if (insetsControlTarget != getControlTarget()) {
            return false;
        }
        boolean updateClientVisibility = super.updateClientVisibility(insetsControlTarget);
        if (updateClientVisibility && insetsControlTarget.isRequestedVisible(this.mSource.getType())) {
            reportImeDrawnForOrganizerIfNeeded(insetsControlTarget);
        }
        return this.mDisplayContent.onImeInsetsClientVisibilityUpdate() | updateClientVisibility;
    }

    private void reportImeDrawnForOrganizerIfNeeded(InsetsControlTarget insetsControlTarget) {
        if (insetsControlTarget.getWindow() == null) {
            return;
        }
        WindowToken windowToken = this.mWindowContainer.asWindowState() != null ? this.mWindowContainer.asWindowState().mToken : null;
        if (this.mDisplayContent.getAsyncRotationController() == null || !this.mDisplayContent.getAsyncRotationController().isTargetToken(windowToken)) {
            reportImeDrawnForOrganizer(insetsControlTarget);
        }
    }

    private void reportImeDrawnForOrganizer(InsetsControlTarget insetsControlTarget) {
        WindowState window = insetsControlTarget.getWindow();
        if (window == null || window.getTask() == null || !window.getTask().isOrganized()) {
            return;
        }
        this.mWindowContainer.mWmService.mAtmService.mTaskOrganizerController.reportImeDrawnOnTask(insetsControlTarget.getWindow().getTask());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportImeDrawnForOrganizer() {
        InsetsControlTarget controlTarget = getControlTarget();
        if (controlTarget != null) {
            reportImeDrawnForOrganizer(controlTarget);
        }
    }

    private void onSourceChanged() {
        if (this.mLastSource.equals(this.mSource)) {
            return;
        }
        this.mLastSource.set(this.mSource);
        DisplayContent displayContent = this.mDisplayContent;
        displayContent.mWmService.mH.obtainMessage(41, displayContent).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleShowImePostLayout(InsetsControlTarget insetsControlTarget, ImeTracker.Token token) {
        boolean isTargetChangedWithinActivity = isTargetChangedWithinActivity(insetsControlTarget);
        this.mImeRequester = insetsControlTarget;
        ImeTracker.forLogging().onFailed(this.mImeRequesterStatsToken, 18);
        this.mImeRequesterStatsToken = token;
        if (isTargetChangedWithinActivity) {
            if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, 140319294, 0, (String) null, (Object[]) null);
            }
            checkShowImePostLayout();
        } else {
            if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, -1410260105, 0, (String) null, new Object[]{String.valueOf(this.mImeRequester.getWindow() == null ? this.mImeRequester : this.mImeRequester.getWindow().getName())});
            }
            this.mShowImeRunner = new Runnable() { // from class: com.android.server.wm.ImeInsetsSourceProvider$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImeInsetsSourceProvider.this.lambda$scheduleShowImePostLayout$0();
                }
            };
            this.mDisplayContent.mWmService.requestTraversal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleShowImePostLayout$0() {
        ImeTracker.forLogging().onProgress(this.mImeRequesterStatsToken, 18);
        if (ProtoLogCache.WM_DEBUG_IME_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, 1928325128, 0, (String) null, (Object[]) null);
        }
        if (isReadyToShowIme()) {
            ImeTracker.forLogging().onProgress(this.mImeRequesterStatsToken, 19);
            InsetsControlTarget imeTarget = this.mDisplayContent.getImeTarget(2);
            if (ProtoLogCache.WM_DEBUG_IME_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_DEBUG_IME, 585839596, 0, (String) null, new Object[]{String.valueOf(imeTarget.getWindow() != null ? imeTarget.getWindow().getName() : "")});
            }
            setImeShowing(true);
            imeTarget.showInsets(WindowInsets.Type.ime(), true, this.mImeRequesterStatsToken);
            Trace.asyncTraceEnd(32L, "WMS.showImePostLayout", 0);
            InsetsControlTarget insetsControlTarget = this.mImeRequester;
            if (imeTarget != insetsControlTarget && insetsControlTarget != null && ProtoLogCache.WM_DEBUG_IME_enabled) {
                ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_IME, -1554521902, 0, (String) null, new Object[]{String.valueOf(insetsControlTarget.getWindow() != null ? this.mImeRequester.getWindow().getName() : "")});
            }
        } else {
            ImeTracker.forLogging().onFailed(this.mImeRequesterStatsToken, 19);
        }
        this.mImeRequesterStatsToken = null;
        abortShowImePostLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkShowImePostLayout() {
        WindowContainer windowContainer = this.mWindowContainer;
        if (windowContainer == null) {
            return;
        }
        WindowState asWindowState = windowContainer.asWindowState();
        if (asWindowState == null) {
            throw new IllegalArgumentException("IME insets must be provided by a window.");
        }
        if (this.mIsImeLayoutDrawn || (isReadyToShowIme() && asWindowState.isDrawn() && !asWindowState.mGivenInsetsPending)) {
            this.mIsImeLayoutDrawn = true;
            Runnable runnable = this.mShowImeRunner;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void abortShowImePostLayout() {
        if (ProtoLogCache.WM_DEBUG_IME_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, 1373000889, 0, (String) null, (Object[]) null);
        }
        this.mImeRequester = null;
        this.mIsImeLayoutDrawn = false;
        this.mShowImeRunner = null;
        ImeTracker.forLogging().onCancelled(this.mImeRequesterStatsToken, 18);
        this.mImeRequesterStatsToken = null;
    }

    @VisibleForTesting
    boolean isReadyToShowIme() {
        InsetsControlTarget imeTarget = this.mDisplayContent.getImeTarget(0);
        if (imeTarget == null || this.mImeRequester == null || this.mDisplayContent.getImeTarget(2) == null) {
            return false;
        }
        if (ProtoLogCache.WM_DEBUG_IME_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_IME, -856590985, 0, (String) null, new Object[]{String.valueOf(imeTarget.getWindow().getName()), String.valueOf(this.mImeRequester.getWindow() == null ? this.mImeRequester : this.mImeRequester.getWindow().getName())});
        }
        return (isImeLayeringTarget(this.mImeRequester, imeTarget) || isAboveImeLayeringTarget(this.mImeRequester, imeTarget) || isImeFallbackTarget(this.mImeRequester) || isImeInputTarget(this.mImeRequester) || sameAsImeControlTarget()) && !isInTransitionAnimation(imeTarget);
    }

    private boolean isInTransitionAnimation(InsetsControlTarget insetsControlTarget) {
        if (insetsControlTarget.getWindow() == null) {
            return false;
        }
        return insetsControlTarget.getWindow().getWrapper().getExtImpl().isFlexibleTaskInTransitionAnimation(insetsControlTarget.getWindow());
    }

    private static boolean isImeLayeringTarget(InsetsControlTarget insetsControlTarget, InsetsControlTarget insetsControlTarget2) {
        return !isImeTargetWindowClosing(insetsControlTarget2.getWindow()) && insetsControlTarget == insetsControlTarget2;
    }

    private static boolean isAboveImeLayeringTarget(InsetsControlTarget insetsControlTarget, InsetsControlTarget insetsControlTarget2) {
        return insetsControlTarget.getWindow() != null && insetsControlTarget2.getWindow().getParentWindow() == insetsControlTarget && insetsControlTarget2.getWindow().mSubLayer > insetsControlTarget.getWindow().mSubLayer;
    }

    private boolean isImeFallbackTarget(InsetsControlTarget insetsControlTarget) {
        return insetsControlTarget == this.mDisplayContent.getImeFallback();
    }

    private boolean isImeInputTarget(InsetsControlTarget insetsControlTarget) {
        return insetsControlTarget == this.mDisplayContent.getImeInputTarget();
    }

    private boolean sameAsImeControlTarget() {
        InsetsControlTarget imeTarget = this.mDisplayContent.getImeTarget(2);
        InsetsControlTarget insetsControlTarget = this.mImeRequester;
        return imeTarget == insetsControlTarget && (insetsControlTarget.getWindow() == null || !isImeTargetWindowClosing(this.mImeRequester.getWindow()));
    }

    private static boolean isImeTargetWindowClosing(WindowState windowState) {
        ActivityRecord activityRecord;
        return windowState.mAnimatingExit || ((activityRecord = windowState.mActivityRecord) != null && ((activityRecord.isInTransition() && !windowState.mActivityRecord.isVisibleRequested()) || windowState.mActivityRecord.willCloseOrEnterPip()));
    }

    private boolean isTargetChangedWithinActivity(InsetsControlTarget insetsControlTarget) {
        InsetsControlTarget insetsControlTarget2;
        return (insetsControlTarget == null || insetsControlTarget.getWindow() == null || (insetsControlTarget2 = this.mImeRequester) == insetsControlTarget || insetsControlTarget2 == null || this.mShowImeRunner == null || insetsControlTarget2.getWindow() == null || this.mImeRequester.getWindow().mActivityRecord != insetsControlTarget.getWindow().mActivityRecord) ? false : true;
    }

    @Override // com.android.server.wm.InsetsSourceProvider
    public void dump(PrintWriter printWriter, String str) {
        super.dump(printWriter, str);
        String str2 = str + "  ";
        printWriter.print(str2);
        printWriter.print("mImeShowing=");
        printWriter.print(this.mImeShowing);
        if (this.mImeRequester != null) {
            printWriter.print(str2);
            printWriter.print("showImePostLayout pending for mImeRequester=");
            printWriter.print(this.mImeRequester);
            printWriter.println();
        }
        printWriter.println();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.InsetsSourceProvider
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        long start = protoOutputStream.start(j);
        super.dumpDebug(protoOutputStream, 1146756268033L, i);
        InsetsControlTarget insetsControlTarget = this.mImeRequester;
        WindowState window = insetsControlTarget != null ? insetsControlTarget.getWindow() : null;
        if (window != null) {
            window.dumpDebug(protoOutputStream, 1146756268034L, i);
        }
        protoOutputStream.write(1133871366147L, this.mIsImeLayoutDrawn);
        protoOutputStream.end(start);
    }

    public void setImeShowing(boolean z) {
        if (ProtoLogGroup.WM_DEBUG_ADD_REMOVE.isLogToLogcat()) {
            Slog.i(TAG, "setImeShowing mImeShowing:" + z);
        }
        this.mImeShowing = z;
    }

    public boolean isImeShowing() {
        return this.mImeShowing;
    }
}
