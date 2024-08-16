package com.android.server.wm;

import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.DisplayInfo;
import android.view.InsetsState;
import android.view.SurfaceControl;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowToken extends WindowContainer<WindowState> {
    private static final String TAG = "WindowManager";
    private boolean mClientVisible;
    private SurfaceControl mFixedRotationTransformLeash;
    private FixedRotationTransformState mFixedRotationTransformState;
    private final boolean mFromClientToken;
    final Bundle mOptions;
    final boolean mOwnerCanManageAppTokens;
    boolean mPersistOnEmpty;
    final boolean mRoundedCornerOverlay;
    private final Comparator<WindowState> mWindowComparator;
    public IWindowTokenExt mWindowTokenExt;
    boolean paused;
    String stringName;
    final IBinder token;
    boolean waitingToShow;
    final int windowType;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public WindowToken asWindowToken() {
        return this;
    }

    @Override // com.android.server.wm.WindowContainer
    long getProtoFieldId() {
        return 1146756268039L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSizeCompatBounds() {
        return false;
    }

    void onCancelFixedRotationTransform(int i) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class FixedRotationTransformState {
        final DisplayFrames mDisplayFrames;
        final DisplayInfo mDisplayInfo;
        final Configuration mRotatedOverrideConfiguration;
        final ArrayList<WindowToken> mAssociatedTokens = new ArrayList<>(3);
        boolean mIsTransforming = true;

        void transform(WindowContainer<?> windowContainer) {
        }

        FixedRotationTransformState(DisplayInfo displayInfo, DisplayFrames displayFrames, Configuration configuration) {
            this.mDisplayInfo = displayInfo;
            this.mDisplayFrames = displayFrames;
            this.mRotatedOverrideConfiguration = configuration;
        }

        void resetTransform() {
            for (int size = this.mAssociatedTokens.size() - 1; size >= 0; size--) {
                this.mAssociatedTokens.get(size).removeFixedRotationLeash();
            }
        }

        void disassociate(WindowToken windowToken) {
            this.mAssociatedTokens.remove(windowToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class FixedRotationTransformStateLegacy extends FixedRotationTransformState {
        final ArrayList<WindowContainer<?>> mRotatedContainers;
        final SeamlessRotator mRotator;

        FixedRotationTransformStateLegacy(DisplayInfo displayInfo, DisplayFrames displayFrames, Configuration configuration, int i) {
            super(displayInfo, displayFrames, configuration);
            this.mRotatedContainers = new ArrayList<>(3);
            this.mRotator = new SeamlessRotator(displayInfo.rotation, i, displayInfo, true);
        }

        @Override // com.android.server.wm.WindowToken.FixedRotationTransformState
        void transform(WindowContainer<?> windowContainer) {
            this.mRotator.unrotate(windowContainer.getPendingTransaction(), windowContainer);
            if (this.mRotatedContainers.contains(windowContainer)) {
                return;
            }
            this.mRotatedContainers.add(windowContainer);
        }

        @Override // com.android.server.wm.WindowToken.FixedRotationTransformState
        void resetTransform() {
            for (int size = this.mRotatedContainers.size() - 1; size >= 0; size--) {
                WindowContainer<?> windowContainer = this.mRotatedContainers.get(size);
                if (windowContainer.getParent() != null) {
                    this.mRotator.finish(windowContainer.getPendingTransaction(), windowContainer);
                }
            }
        }

        @Override // com.android.server.wm.WindowToken.FixedRotationTransformState
        void disassociate(WindowToken windowToken) {
            super.disassociate(windowToken);
            this.mRotatedContainers.remove(windowToken);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$new$0(WindowState windowState, WindowState windowState2) {
        if (windowState.mToken != this) {
            throw new IllegalArgumentException("newWindow=" + windowState + " is not a child of token=" + this);
        }
        if (windowState2.mToken == this) {
            return isFirstChildWindowGreaterThanSecond(windowState, windowState2) ? 1 : -1;
        }
        throw new IllegalArgumentException("existingWindow=" + windowState2 + " is not a child of token=" + this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WindowToken(WindowManagerService windowManagerService, IBinder iBinder, int i, boolean z, DisplayContent displayContent, boolean z2) {
        this(windowManagerService, iBinder, i, z, displayContent, z2, false, false, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WindowToken(WindowManagerService windowManagerService, IBinder iBinder, int i, boolean z, DisplayContent displayContent, boolean z2, boolean z3, boolean z4, Bundle bundle) {
        super(windowManagerService);
        this.mWindowTokenExt = (IWindowTokenExt) ExtLoader.type(IWindowTokenExt.class).base(this).create();
        this.paused = false;
        this.mWindowComparator = new Comparator() { // from class: com.android.server.wm.WindowToken$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int lambda$new$0;
                lambda$new$0 = WindowToken.this.lambda$new$0((WindowState) obj, (WindowState) obj2);
                return lambda$new$0;
            }
        };
        this.token = iBinder;
        this.windowType = i;
        this.mOptions = bundle;
        this.mPersistOnEmpty = z;
        this.mOwnerCanManageAppTokens = z2;
        this.mRoundedCornerOverlay = z3;
        this.mFromClientToken = z4;
        if (displayContent != null) {
            displayContent.addWindowToken(iBinder, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeAllWindowsIfPossible() {
        int size = this.mChildren.size() - 1;
        while (size >= 0) {
            if (size >= this.mChildren.size()) {
                Slog.d(TAG, "removeAllWindowsIfPossible IndexOutOfBoundsE this = " + this);
            } else {
                WindowState windowState = (WindowState) this.mChildren.get(size);
                if (ProtoLogCache.WM_DEBUG_WINDOW_MOVEMENT_enabled) {
                    ProtoLogImpl.w(ProtoLogGroup.WM_DEBUG_WINDOW_MOVEMENT, 913494177, 0, (String) null, new Object[]{String.valueOf(windowState)});
                }
                windowState.removeIfPossible();
                if (size > this.mChildren.size()) {
                    size = this.mChildren.size();
                }
            }
            size--;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setExiting(boolean z) {
        if (isEmpty()) {
            super.removeImmediately();
            return;
        }
        this.mPersistOnEmpty = false;
        if (isVisible()) {
            int size = this.mChildren.size();
            boolean z2 = false;
            for (int i = 0; i < size; i++) {
                z2 |= ((WindowState) this.mChildren.get(i)).onSetAppExiting(z);
            }
            if (z2) {
                this.mWmService.mWindowPlacerLocked.performSurfacePlacement();
                this.mWmService.updateFocusedWindowLocked(0, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCompatScale() {
        return this.mDisplayContent.mCompatibleScreenScale;
    }

    protected boolean isFirstChildWindowGreaterThanSecond(WindowState windowState, WindowState windowState2) {
        return windowState.mBaseLayer >= windowState2.mBaseLayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addWindow(WindowState windowState) {
        if (ProtoLogCache.WM_DEBUG_FOCUS_enabled) {
            ProtoLogImpl.d(ProtoLogGroup.WM_DEBUG_FOCUS, 1219600119, 0, (String) null, new Object[]{String.valueOf(windowState), String.valueOf(Debug.getCallers(5))});
        }
        if (windowState.isChildWindow()) {
            return;
        }
        if (this.mSurfaceControl == null) {
            createSurfaceControl(true);
            reassignLayer(getSyncTransaction());
        }
        if (this.mChildren.contains(windowState)) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_ADD_REMOVE_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_ADD_REMOVE, 241961619, 0, (String) null, new Object[]{String.valueOf(windowState), String.valueOf(this)});
        }
        addChild((WindowToken) windowState, (Comparator<WindowToken>) this.mWindowComparator);
        this.mWmService.mWindowsChanged = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void createSurfaceControl(boolean z) {
        if (!this.mFromClientToken || z) {
            super.createSurfaceControl(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.mChildren.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean windowsCanBeWallpaperTarget() {
        for (int size = this.mChildren.size() - 1; size >= 0; size--) {
            if (((WindowState) this.mChildren.get(size)).hasWallpaper()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void removeImmediately() {
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent != null) {
            displayContent.removeWindowToken(this.token, true);
        }
        super.removeImmediately();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void onDisplayChanged(DisplayContent displayContent) {
        displayContent.reParentWindowToken(this);
        super.onDisplayChanged(displayContent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void assignLayer(SurfaceControl.Transaction transaction, int i) {
        if (this.mRoundedCornerOverlay) {
            super.assignLayer(transaction, 1073741826);
        } else if (this.mWindowTokenExt.canAssigFingerPrintLayer(this, this.windowType)) {
            super.assignLayer(transaction, 1073741827);
        } else {
            super.assignLayer(transaction, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public SurfaceControl.Builder makeSurface() {
        SurfaceControl.Builder makeSurface = super.makeSurface();
        if (!ActivityTaskManagerService.LTW_DISABLE) {
            if (this.mRoundedCornerOverlay || this.windowType == 2099) {
                makeSurface.setParent(null);
            }
        } else if (this.mRoundedCornerOverlay) {
            makeSurface.setParent(null);
        }
        this.mWindowTokenExt.makeSurface(makeSurface, this.windowType);
        return makeSurface;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isClientVisible() {
        return this.mClientVisible;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setClientVisible(boolean z) {
        if (this.mClientVisible == z) {
            return;
        }
        if (ProtoLogCache.WM_DEBUG_APP_TRANSITIONS_enabled) {
            ProtoLogImpl.v(ProtoLogGroup.WM_DEBUG_APP_TRANSITIONS, -2093859262, 12, (String) null, new Object[]{String.valueOf(this), Boolean.valueOf(z), String.valueOf(Debug.getCallers(5))});
        }
        this.mClientVisible = z;
        sendAppVisibilityToClients();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasFixedRotationTransform() {
        return this.mFixedRotationTransformState != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasFixedRotationTransform(WindowToken windowToken) {
        FixedRotationTransformState fixedRotationTransformState = this.mFixedRotationTransformState;
        if (fixedRotationTransformState == null || windowToken == null) {
            return false;
        }
        return this == windowToken || fixedRotationTransformState == windowToken.mFixedRotationTransformState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFinishingFixedRotationTransform() {
        FixedRotationTransformState fixedRotationTransformState = this.mFixedRotationTransformState;
        return (fixedRotationTransformState == null || fixedRotationTransformState.mIsTransforming) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFixedRotationTransforming() {
        FixedRotationTransformState fixedRotationTransformState = this.mFixedRotationTransformState;
        return fixedRotationTransformState != null && fixedRotationTransformState.mIsTransforming;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayInfo getFixedRotationTransformDisplayInfo() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mDisplayInfo;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayFrames getFixedRotationTransformDisplayFrames() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mDisplayFrames;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getFixedRotationTransformMaxBounds() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mRotatedOverrideConfiguration.windowConfiguration.getMaxBounds();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getFixedRotationTransformDisplayBounds() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mRotatedOverrideConfiguration.windowConfiguration.getBounds();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InsetsState getFixedRotationTransformInsetsState() {
        if (isFixedRotationTransforming()) {
            return this.mFixedRotationTransformState.mDisplayFrames.mInsetsState;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyFixedRotationTransform(DisplayInfo displayInfo, DisplayFrames displayFrames, Configuration configuration) {
        FixedRotationTransformState fixedRotationTransformStateLegacy;
        FixedRotationTransformState fixedRotationTransformState = this.mFixedRotationTransformState;
        if (fixedRotationTransformState != null) {
            fixedRotationTransformState.disassociate(this);
        }
        if (configuration != null && this.mDisplayContent != null && displayInfo != null) {
            Slog.d(TAG, "Apply fixed rotation transform to " + this + ". Preferred rotation:" + configuration.windowConfiguration.getRotation() + ", Current rotation:" + this.mDisplayContent.getRotation() + ", info.rotation:" + displayInfo.rotation);
        }
        Configuration configuration2 = new Configuration(configuration);
        this.mWindowTokenExt.resolveScreenConfigInSecondary(this, configuration2, displayInfo);
        if (this.mTransitionController.isShellTransitionsEnabled()) {
            fixedRotationTransformStateLegacy = new FixedRotationTransformState(displayInfo, displayFrames, configuration2);
        } else {
            fixedRotationTransformStateLegacy = new FixedRotationTransformStateLegacy(displayInfo, displayFrames, configuration2, this.mDisplayContent.getRotation());
        }
        this.mFixedRotationTransformState = fixedRotationTransformStateLegacy;
        this.mWindowTokenExt.resolveScreenConfigInSecondary(this, configuration2, displayInfo);
        this.mFixedRotationTransformState.mAssociatedTokens.add(this);
        this.mDisplayContent.getDisplayPolicy().simulateLayoutDisplay(displayFrames);
        onFixedRotationStatePrepared();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void linkFixedRotationTransform(WindowToken windowToken) {
        FixedRotationTransformState fixedRotationTransformState;
        FixedRotationTransformState fixedRotationTransformState2 = windowToken.mFixedRotationTransformState;
        if (fixedRotationTransformState2 == null || (fixedRotationTransformState = this.mFixedRotationTransformState) == fixedRotationTransformState2) {
            return;
        }
        if (fixedRotationTransformState != null) {
            fixedRotationTransformState.disassociate(this);
        }
        this.mFixedRotationTransformState = fixedRotationTransformState2;
        fixedRotationTransformState2.mAssociatedTokens.add(this);
        onFixedRotationStatePrepared();
    }

    private void onFixedRotationStatePrepared() {
        onConfigurationChanged(getParent().getConfiguration());
        ActivityRecord asActivityRecord = asActivityRecord();
        if (asActivityRecord == null || !asActivityRecord.hasProcess()) {
            return;
        }
        asActivityRecord.app.registerActivityConfigurationListener(asActivityRecord);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void prepareSurfaces() {
        this.mWindowTokenExt.updateSurfaceIfNeed();
        super.prepareSurfaces();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void onConfigurationChanged(Configuration configuration) {
        this.mWindowTokenExt.onConfigurationChanged(configuration);
        super.onConfigurationChanged(configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAnimatingFixedRotationTransition() {
        FixedRotationTransformState fixedRotationTransformState = this.mFixedRotationTransformState;
        if (fixedRotationTransformState == null) {
            return false;
        }
        for (int size = fixedRotationTransformState.mAssociatedTokens.size() - 1; size >= 0; size--) {
            ActivityRecord asActivityRecord = this.mFixedRotationTransformState.mAssociatedTokens.get(size).asActivityRecord();
            if (asActivityRecord != null && asActivityRecord.isInTransition()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishFixedRotationTransform() {
        finishFixedRotationTransform(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishFixedRotationTransform(Runnable runnable) {
        FixedRotationTransformState fixedRotationTransformState = this.mFixedRotationTransformState;
        if (fixedRotationTransformState == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Finish fixed rotation transform of ");
        sb.append(this);
        sb.append(WindowManagerDebugConfig.DEBUG_ANIM ? "; callers:" + Debug.getCallers(10) : "");
        Slog.d(TAG, sb.toString());
        this.mWindowTokenExt.recoveryFixedRotationConfig(this, this.mFixedRotationTransformState.mRotatedOverrideConfiguration);
        fixedRotationTransformState.resetTransform();
        fixedRotationTransformState.mIsTransforming = false;
        if (runnable != null) {
            runnable.run();
        }
        for (int size = fixedRotationTransformState.mAssociatedTokens.size() - 1; size >= 0; size--) {
            WindowToken windowToken = fixedRotationTransformState.mAssociatedTokens.get(size);
            windowToken.mFixedRotationTransformState = null;
            if (runnable == null) {
                windowToken.cancelFixedRotationTransform();
            }
        }
    }

    private void cancelFixedRotationTransform() {
        WindowContainer parent = getParent();
        if (parent == null) {
            return;
        }
        if (this.mTransitionController.isShellTransitionsEnabled() && asActivityRecord() != null && isVisible()) {
            this.mTransitionController.requestTransitionIfNeeded(6, this);
            this.mTransitionController.setReady(this);
        }
        int rotation = getWindowConfiguration().getRotation();
        onConfigurationChanged(parent.getConfiguration());
        onCancelFixedRotationTransform(rotation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getOrCreateFixedRotationLeash(SurfaceControl.Transaction transaction) {
        if (!this.mTransitionController.isShellTransitionsEnabled()) {
            return null;
        }
        int relativeDisplayRotation = getRelativeDisplayRotation();
        if (relativeDisplayRotation == 0) {
            return this.mFixedRotationTransformLeash;
        }
        SurfaceControl surfaceControl = this.mFixedRotationTransformLeash;
        if (surfaceControl != null) {
            return surfaceControl;
        }
        SurfaceControl build = makeSurface().setContainerLayer().setParent(getParentSurfaceControl()).setName(getSurfaceControl() + " - rotation-leash").setHidden(false).setCallsite("WindowToken.getOrCreateFixedRotationLeash").build();
        Point point = this.mLastSurfacePosition;
        transaction.setPosition(build, (float) point.x, (float) point.y);
        transaction.reparent(getSurfaceControl(), build);
        getPendingTransaction().setFixedTransformHint(build, getWindowConfiguration().getDisplayRotation());
        this.mFixedRotationTransformLeash = build;
        updateSurfaceRotation(transaction, relativeDisplayRotation, build);
        return this.mFixedRotationTransformLeash;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceControl getFixedRotationLeash() {
        return this.mFixedRotationTransformLeash;
    }

    void removeFixedRotationLeash() {
        if (this.mFixedRotationTransformLeash == null) {
            return;
        }
        SurfaceControl.Transaction syncTransaction = getSyncTransaction();
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            syncTransaction.reparent(surfaceControl, getParentSurfaceControl());
        }
        syncTransaction.remove(this.mFixedRotationTransformLeash);
        this.mFixedRotationTransformLeash = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public void resolveOverrideConfiguration(Configuration configuration) {
        super.resolveOverrideConfiguration(configuration);
        if (isFixedRotationTransforming()) {
            getResolvedOverrideConfiguration().updateFrom(this.mFixedRotationTransformState.mRotatedOverrideConfiguration);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void updateSurfacePosition(SurfaceControl.Transaction transaction) {
        super.updateSurfacePosition(transaction);
        if (this.mTransitionController.isShellTransitionsEnabled() || !isFixedRotationTransforming()) {
            return;
        }
        ActivityRecord asActivityRecord = asActivityRecord();
        Task rootTask = asActivityRecord != null ? asActivityRecord.getRootTask() : null;
        if (rootTask == null || !rootTask.inPinnedWindowingMode()) {
            this.mFixedRotationTransformState.transform(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.wm.WindowContainer
    public void updateSurfaceRotation(SurfaceControl.Transaction transaction, int i, SurfaceControl surfaceControl) {
        Task rootTask;
        ActivityRecord asActivityRecord = asActivityRecord();
        if (asActivityRecord == null || (rootTask = asActivityRecord.getRootTask()) == null || this.mTransitionController.getWindowingModeAtStart(rootTask) != 2) {
            super.updateSurfaceRotation(transaction, i, surfaceControl);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void resetSurfacePositionForAnimationLeash(SurfaceControl.Transaction transaction) {
        if (isFixedRotationTransforming()) {
            return;
        }
        super.resetSurfacePositionForAnimationLeash(transaction);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public boolean prepareSync() {
        DisplayContent displayContent = this.mDisplayContent;
        if (displayContent != null && displayContent.isRotationChanging() && AsyncRotationController.canBeAsync(this)) {
            return false;
        }
        return super.prepareSync();
    }

    @Override // com.android.server.wm.WindowContainer, com.android.server.wm.ConfigurationContainer
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j, int i) {
        if (i != 2 || isVisible()) {
            long start = protoOutputStream.start(j);
            super.dumpDebug(protoOutputStream, 1146756268033L, i);
            protoOutputStream.write(1120986464258L, System.identityHashCode(this));
            protoOutputStream.write(1133871366149L, this.waitingToShow);
            protoOutputStream.write(1133871366150L, this.paused);
            protoOutputStream.end(start);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public void dump(PrintWriter printWriter, String str, boolean z) {
        super.dump(printWriter, str, z);
        printWriter.print(str);
        printWriter.print("windows=");
        printWriter.println(this.mChildren);
        printWriter.print(str);
        printWriter.print("windowType=");
        printWriter.print(this.windowType);
        if (this.waitingToShow) {
            printWriter.print(" waitingToShow=true");
        }
        printWriter.println();
        if (hasFixedRotationTransform()) {
            printWriter.print(str);
            printWriter.print("fixedRotationConfig=");
            printWriter.println(this.mFixedRotationTransformState.mRotatedOverrideConfiguration);
        }
    }

    public String toString() {
        if (this.stringName == null) {
            this.stringName = "WindowToken{" + Integer.toHexString(System.identityHashCode(this)) + " type=" + this.windowType + " " + this.token + "}";
        }
        return this.stringName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.ConfigurationContainer
    public String getName() {
        return toString();
    }

    boolean canLayerAboveSystemBars() {
        return this.mOwnerCanManageAppTokens && getWindowLayerFromType() > this.mWmService.mPolicy.getWindowLayerFromTypeLw(2019, this.mOwnerCanManageAppTokens);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getWindowLayerFromType() {
        return this.mWmService.mPolicy.getWindowLayerFromTypeLw(this.windowType, this.mOwnerCanManageAppTokens, this.mRoundedCornerOverlay);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFromClient() {
        return this.mFromClientToken;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInsetsFrozen(final boolean z) {
        forAllWindows(new Consumer() { // from class: com.android.server.wm.WindowToken$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WindowToken.this.lambda$setInsetsFrozen$1(z, (WindowState) obj);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setInsetsFrozen$1(boolean z, WindowState windowState) {
        if (windowState.mToken == this) {
            if (z) {
                windowState.freezeInsetsState();
            } else {
                windowState.clearFrozenInsetsState();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.wm.WindowContainer
    public int getWindowType() {
        return this.windowType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public static class Builder {
        private DisplayContent mDisplayContent;
        private boolean mFromClientToken;
        private Bundle mOptions;
        private boolean mOwnerCanManageAppTokens;
        private boolean mPersistOnEmpty;
        private boolean mRoundedCornerOverlay;
        private final WindowManagerService mService;
        private final IBinder mToken;
        private final int mType;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(WindowManagerService windowManagerService, IBinder iBinder, int i) {
            this.mService = windowManagerService;
            this.mToken = iBinder;
            this.mType = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setPersistOnEmpty(boolean z) {
            this.mPersistOnEmpty = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setDisplayContent(DisplayContent displayContent) {
            this.mDisplayContent = displayContent;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setOwnerCanManageAppTokens(boolean z) {
            this.mOwnerCanManageAppTokens = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setRoundedCornerOverlay(boolean z) {
            this.mRoundedCornerOverlay = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setFromClientToken(boolean z) {
            this.mFromClientToken = z;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setOptions(Bundle bundle) {
            this.mOptions = bundle;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public WindowToken build() {
            return new WindowToken(this.mService, this.mToken, this.mType, this.mPersistOnEmpty, this.mDisplayContent, this.mOwnerCanManageAppTokens, this.mRoundedCornerOverlay, this.mFromClientToken, this.mOptions);
        }
    }
}
