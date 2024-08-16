package com.android.server.wm;

import android.os.Debug;
import android.os.Trace;
import android.util.EventLog;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import android.view.WindowContentFrameStats;
import com.android.internal.protolog.ProtoLogGroup;
import com.android.internal.protolog.ProtoLogImpl;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class WindowSurfaceController {
    static final String TAG = "WindowManager";
    final WindowStateAnimator mAnimator;
    boolean mChildrenDetached;
    private final WindowManagerService mService;
    SurfaceControl mSurfaceControl;
    private final Session mWindowSession;
    private IWindowSurfaceControllerExt mWindowSurfaceControllerExtImpl;
    private final int mWindowType;
    private final String title;
    private boolean mSurfaceShown = false;
    private float mSurfaceX = 0.0f;
    private float mSurfaceY = 0.0f;
    private float mLastDsdx = 1.0f;
    private float mLastDtdx = 0.0f;
    private float mLastDsdy = 0.0f;
    private float mLastDtdy = 1.0f;
    private float mSurfaceAlpha = 0.0f;
    private int mSurfaceLayer = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowSurfaceController(String str, int i, int i2, WindowStateAnimator windowStateAnimator, int i3) {
        boolean z = false;
        this.mAnimator = windowStateAnimator;
        this.title = str;
        WindowManagerService windowManagerService = windowStateAnimator.mService;
        this.mService = windowManagerService;
        WindowState windowState = windowStateAnimator.mWin;
        this.mWindowType = i3;
        Session session = windowState.mSession;
        this.mWindowSession = session;
        this.mWindowSurfaceControllerExtImpl = (IWindowSurfaceControllerExt) ExtLoader.type(IWindowSurfaceControllerExt.class).base(this).create();
        Trace.traceBegin(32L, "new SurfaceControl");
        SurfaceControl.Builder callsite = windowState.makeSurface().setParent(windowState.getSurfaceControl()).setName(str).setFormat(i).setFlags(i2).setMetadata(2, i3).setMetadata(1, session.mUid).setMetadata(6, session.mPid).setCallsite("WindowSurfaceController");
        if (windowManagerService.mUseBLAST && (windowState.getAttrs().privateFlags & 33554432) != 0) {
            z = true;
        }
        if (z) {
            callsite.setBLASTLayer();
        }
        this.mSurfaceControl = callsite.build();
        Trace.traceEnd(32L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hide(SurfaceControl.Transaction transaction, String str) {
        if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -1259022216, 0, (String) null, new Object[]{String.valueOf(str), String.valueOf(this.title)});
        }
        if (this.mSurfaceShown) {
            hideSurface(transaction);
        }
    }

    private void hideSurface(SurfaceControl.Transaction transaction) {
        if (this.mSurfaceControl == null) {
            return;
        }
        setShown(false);
        try {
            transaction.hide(this.mSurfaceControl);
            WindowStateAnimator windowStateAnimator = this.mAnimator;
            if (windowStateAnimator.mIsWallpaper) {
                DisplayContent displayContent = windowStateAnimator.mWin.getDisplayContent();
                EventLog.writeEvent(EventLogTags.WM_WALLPAPER_SURFACE, Integer.valueOf(displayContent.mDisplayId), 0, String.valueOf(displayContent.mWallpaperController.getWallpaperTarget()));
            }
        } catch (RuntimeException unused) {
            Slog.w(TAG, "Exception hiding surface in " + this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy(SurfaceControl.Transaction transaction) {
        if (ProtoLogCache.WM_SHOW_SURFACE_ALLOC_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, -861707633, 0, (String) null, new Object[]{String.valueOf(this), String.valueOf(Debug.getCallers(8))});
        }
        try {
            try {
                if (this.mSurfaceControl != null) {
                    WindowStateAnimator windowStateAnimator = this.mAnimator;
                    if (windowStateAnimator.mIsWallpaper) {
                        WindowState windowState = windowStateAnimator.mWin;
                        if (!windowState.mWindowRemovalAllowed && !windowState.mRemoveOnExit) {
                            Slog.e(TAG, "Unexpected removing wallpaper surface of " + this.mAnimator.mWin + " by " + Debug.getCallers(8));
                        }
                    }
                    transaction.remove(this.mSurfaceControl);
                }
            } catch (RuntimeException e) {
                Slog.w(TAG, "Error destroying surface in: " + this, e);
            }
        } finally {
            setShown(false);
            this.mSurfaceControl = null;
        }
    }

    void setPosition(SurfaceControl.Transaction transaction, float f, float f2) {
        if ((this.mSurfaceX == f && this.mSurfaceY == f2) ? false : true) {
            this.mSurfaceX = f;
            this.mSurfaceY = f2;
            if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, 633654009, 10, (String) null, new Object[]{Double.valueOf(f), Double.valueOf(f2), String.valueOf(this.title)});
            }
            transaction.setPosition(this.mSurfaceControl, f, f2);
        }
    }

    void setMatrix(SurfaceControl.Transaction transaction, float f, float f2, float f3, float f4) {
        if ((this.mLastDsdx == f && this.mLastDtdx == f2 && this.mLastDtdy == f3 && this.mLastDsdy == f4) ? false : true) {
            this.mLastDsdx = f;
            this.mLastDtdx = f2;
            this.mLastDtdy = f3;
            this.mLastDsdy = f4;
            if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
                ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, 309039362, 170, (String) null, new Object[]{Double.valueOf(f), Double.valueOf(f2), Double.valueOf(f3), Double.valueOf(f4), String.valueOf(this.title)});
            }
            transaction.setMatrix(this.mSurfaceControl, f, f2, f3, f4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean prepareToShowInTransaction(SurfaceControl.Transaction transaction, float f) {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl == null) {
            return false;
        }
        this.mSurfaceAlpha = f;
        transaction.setAlpha(surfaceControl, f);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOpaque(boolean z) {
        if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, 558823034, 3, (String) null, new Object[]{Boolean.valueOf(z), String.valueOf(this.title)});
        }
        if (this.mSurfaceControl == null) {
            return;
        }
        if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
            Slog.i(TAG, ">>> OPEN TRANSACTION setOpaqueLocked");
        }
        this.mService.openSurfaceTransaction();
        try {
            SurfaceControl.getGlobalTransaction().setOpaque(this.mSurfaceControl, z);
        } finally {
            this.mService.closeSurfaceTransaction("setOpaqueLocked");
            if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                Slog.i(TAG, "<<< CLOSE TRANSACTION setOpaqueLocked");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSecure(boolean z) {
        WindowState windowState;
        if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -1176488860, 3, (String) null, new Object[]{Boolean.valueOf(z), String.valueOf(this.title)});
        }
        if (this.mSurfaceControl == null) {
            return;
        }
        if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
            Slog.i(TAG, ">>> OPEN TRANSACTION setSecureLocked");
        }
        this.mService.openSurfaceTransaction();
        try {
            SurfaceControl.getGlobalTransaction().setSecure(this.mSurfaceControl, z);
            DisplayContent displayContent = this.mAnimator.mWin.mDisplayContent;
            if (displayContent != null) {
                displayContent.refreshImeSecureFlag(SurfaceControl.getGlobalTransaction());
            }
            WindowStateAnimator windowStateAnimator = this.mAnimator;
            if (windowStateAnimator == null || (windowState = windowStateAnimator.mWin) == null) {
                return;
            }
            this.mWindowSurfaceControllerExtImpl.onSecurityPageFlagChanged(windowState, z, true);
        } finally {
            this.mService.closeSurfaceTransaction("setSecure");
            if (WindowManagerDebugConfig.SHOW_LIGHT_TRANSACTIONS) {
                Slog.i(TAG, "<<< CLOSE TRANSACTION setSecureLocked");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setColorSpaceAgnostic(SurfaceControl.Transaction transaction, boolean z) {
        if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, 585096182, 3, (String) null, new Object[]{Boolean.valueOf(z), String.valueOf(this.title)});
        }
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl == null) {
            return;
        }
        transaction.setColorSpaceAgnostic(surfaceControl, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void showRobustly(SurfaceControl.Transaction transaction) {
        if (ProtoLogCache.WM_SHOW_TRANSACTIONS_enabled) {
            ProtoLogImpl.i(ProtoLogGroup.WM_SHOW_TRANSACTIONS, -1089874824, 0, (String) null, new Object[]{String.valueOf(this.title)});
        }
        if (WindowManagerDebugConfig.DEBUG_VISIBILITY) {
            Slog.v(TAG, "Showing " + this + " during relayout");
        }
        if (this.mSurfaceShown) {
            return;
        }
        setShown(true);
        transaction.show(this.mSurfaceControl);
        WindowStateAnimator windowStateAnimator = this.mAnimator;
        if (windowStateAnimator.mIsWallpaper) {
            DisplayContent displayContent = windowStateAnimator.mWin.getDisplayContent();
            EventLog.writeEvent(EventLogTags.WM_WALLPAPER_SURFACE, Integer.valueOf(displayContent.mDisplayId), 1, String.valueOf(displayContent.mWallpaperController.getWallpaperTarget()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean clearWindowContentFrameStats() {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl == null) {
            return false;
        }
        return surfaceControl.clearContentFrameStats();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getWindowContentFrameStats(WindowContentFrameStats windowContentFrameStats) {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl == null) {
            return false;
        }
        return surfaceControl.getContentFrameStats(windowContentFrameStats);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSurface() {
        return this.mSurfaceControl != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getSurfaceControl(SurfaceControl surfaceControl) {
        surfaceControl.copyFrom(this.mSurfaceControl, "WindowSurfaceController.getSurfaceControl");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getShown() {
        return this.mSurfaceShown;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShown(boolean z) {
        WindowState windowState;
        Session session;
        int i;
        boolean z2 = this.mSurfaceShown != z;
        this.mSurfaceShown = z;
        this.mService.updateNonSystemOverlayWindowsVisibilityIfNeeded(this.mAnimator.mWin, z);
        this.mAnimator.mWin.onSurfaceShownChanged(z);
        Session session2 = this.mWindowSession;
        if (session2 != null) {
            session2.onWindowSurfaceVisibilityChanged(this, this.mSurfaceShown, this.mWindowType);
        }
        WindowState windowState2 = this.mAnimator.mWin;
        if (windowState2 != null && (session = this.mWindowSession) != null && windowState2.mAttrs != null && (i = session.mUid) > 10000) {
            IWindowSurfaceControllerExt iWindowSurfaceControllerExt = this.mWindowSurfaceControllerExtImpl;
            int i2 = session.mPid;
            int hashCode = windowState2.hashCode();
            WindowState windowState3 = this.mAnimator.mWin;
            iWindowSurfaceControllerExt.updateWindowState(i, i2, hashCode, windowState3.mAttrs.type, windowState3.mHasSurface, z);
        }
        this.mWindowSurfaceControllerExtImpl.setShown(z, this.mAnimator.mWin, this.mSurfaceControl);
        WindowStateAnimator windowStateAnimator = this.mAnimator;
        if (windowStateAnimator != null && windowStateAnimator.mWin != null) {
            ((IMirageWindowManagerExt) ExtLoader.type(IMirageWindowManagerExt.class).create()).applySurfacePrivacyProtectionPolicy(z2, this.mSurfaceShown, this.mAnimator.mWin);
        }
        WindowStateAnimator windowStateAnimator2 = this.mAnimator;
        if (windowStateAnimator2 == null || (windowState = windowStateAnimator2.mWin) == null) {
            return;
        }
        this.mWindowSurfaceControllerExtImpl.onSecurityPageFlagChanged(windowState, z, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1133871366145L, this.mSurfaceShown);
        protoOutputStream.write(1120986464258L, this.mSurfaceLayer);
        protoOutputStream.end(start);
    }

    public void dump(PrintWriter printWriter, String str, boolean z) {
        if (z) {
            printWriter.print(str);
            printWriter.print("mSurface=");
            printWriter.println(this.mSurfaceControl);
        }
        printWriter.print(str);
        printWriter.print("Surface: shown=");
        printWriter.print(this.mSurfaceShown);
        printWriter.print(" layer=");
        printWriter.print(this.mSurfaceLayer);
        printWriter.print(" alpha=");
        printWriter.print(this.mSurfaceAlpha);
        printWriter.print(" rect=(");
        printWriter.print(this.mSurfaceX);
        printWriter.print(",");
        printWriter.print(this.mSurfaceY);
        printWriter.print(") ");
        printWriter.print(" transform=(");
        printWriter.print(this.mLastDsdx);
        printWriter.print(", ");
        printWriter.print(this.mLastDtdx);
        printWriter.print(", ");
        printWriter.print(this.mLastDsdy);
        printWriter.print(", ");
        printWriter.print(this.mLastDtdy);
        printWriter.println(")");
    }

    public String toString() {
        return this.mSurfaceControl.toString();
    }
}
