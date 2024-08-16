package com.android.server.accessibility.magnification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.MathUtils;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.accessibility.IWindowMagnificationConnection;
import android.view.accessibility.IWindowMagnificationConnectionCallback;
import android.view.accessibility.MagnificationAnimationCallback;
import com.android.internal.accessibility.util.AccessibilityStatsLogUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import com.android.server.accessibility.AccessibilityTraceManager;
import com.android.server.accessibility.magnification.PanningScalingHandler;
import com.android.server.statusbar.StatusBarManagerInternal;
import com.android.server.wm.WindowManagerInternal;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class WindowMagnificationManager implements PanningScalingHandler.MagnificationDelegate, WindowManagerInternal.AccessibilityControllerInternal.UiChangesForAccessibilityCallbacks {
    private static final int CONNECTED = 1;
    private static final int CONNECTING = 0;
    private static final boolean DBG = false;
    private static final int DISCONNECTED = 3;
    private static final int DISCONNECTING = 2;
    private static final String TAG = "WindowMagnificationMgr";
    private static final int WAIT_CONNECTION_TIMEOUT_MILLIS = 100;
    public static final int WINDOW_POSITION_AT_CENTER = 0;
    public static final int WINDOW_POSITION_AT_TOP_LEFT = 1;
    private final Callback mCallback;

    @GuardedBy({"mLock"})
    private ConnectionCallback mConnectionCallback;

    @GuardedBy({"mLock"})
    @VisibleForTesting
    WindowMagnificationConnectionWrapper mConnectionWrapper;
    private final Context mContext;
    private final Object mLock;
    private final MagnificationScaleProvider mScaleProvider;
    private final AccessibilityTraceManager mTrace;
    private int mConnectionState = 3;

    @GuardedBy({"mLock"})
    private SparseArray<WindowMagnifier> mWindowMagnifiers = new SparseArray<>();
    private boolean mMagnificationFollowTypingEnabled = true;

    @GuardedBy({"mLock"})
    private final SparseBooleanArray mIsImeVisibleArray = new SparseBooleanArray();

    @GuardedBy({"mLock"})
    private final SparseArray<Float> mLastActivatedScale = new SparseArray<>();
    private boolean mReceiverRegistered = false;

    @VisibleForTesting
    protected final BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() { // from class: com.android.server.accessibility.magnification.WindowMagnificationManager.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int displayId = context.getDisplayId();
            WindowMagnificationManager.this.removeMagnificationButton(displayId);
            WindowMagnificationManager.this.disableWindowMagnification(displayId, false, null);
        }
    };

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public interface Callback {
        void onAccessibilityActionPerformed(int i);

        void onChangeMagnificationMode(int i, int i2);

        void onPerformScaleAction(int i, float f);

        void onSourceBoundsChanged(int i, Rect rect);

        void onWindowMagnificationActivationState(int i, boolean z);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private @interface ConnectionState {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public @interface WindowPosition {
    }

    private static String connectionStateToString(int i) {
        if (i == 0) {
            return "CONNECTING";
        }
        if (i == 1) {
            return "CONNECTED";
        }
        if (i == 2) {
            return "DISCONNECTING";
        }
        if (i == 3) {
            return "DISCONNECTED";
        }
        return "UNKNOWN:" + i;
    }

    public WindowMagnificationManager(Context context, Object obj, Callback callback, AccessibilityTraceManager accessibilityTraceManager, MagnificationScaleProvider magnificationScaleProvider) {
        this.mContext = context;
        this.mLock = obj;
        this.mCallback = callback;
        this.mTrace = accessibilityTraceManager;
        this.mScaleProvider = magnificationScaleProvider;
    }

    public void setConnection(IWindowMagnificationConnection iWindowMagnificationConnection) {
        Object obj;
        synchronized (this.mLock) {
            WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
            if (windowMagnificationConnectionWrapper != null) {
                windowMagnificationConnectionWrapper.setConnectionCallback(null);
                ConnectionCallback connectionCallback = this.mConnectionCallback;
                if (connectionCallback != null) {
                    connectionCallback.mExpiredDeathRecipient = true;
                }
                this.mConnectionWrapper.unlinkToDeath(this.mConnectionCallback);
                this.mConnectionWrapper = null;
                if (this.mConnectionState != 0) {
                    setConnectionState(3);
                }
            }
            if (iWindowMagnificationConnection != null) {
                this.mConnectionWrapper = new WindowMagnificationConnectionWrapper(iWindowMagnificationConnection, this.mTrace);
            }
            if (this.mConnectionWrapper != null) {
                try {
                    try {
                        ConnectionCallback connectionCallback2 = new ConnectionCallback();
                        this.mConnectionCallback = connectionCallback2;
                        this.mConnectionWrapper.linkToDeath(connectionCallback2);
                        this.mConnectionWrapper.setConnectionCallback(this.mConnectionCallback);
                        setConnectionState(1);
                        obj = this.mLock;
                    } catch (Throwable th) {
                        this.mLock.notify();
                        throw th;
                    }
                } catch (RemoteException e) {
                    Slog.e(TAG, "setConnection failed", e);
                    this.mConnectionWrapper = null;
                    setConnectionState(3);
                    obj = this.mLock;
                }
                obj.notify();
            }
        }
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionWrapper != null;
        }
        return z;
    }

    public boolean requestConnection(boolean z) {
        int i;
        if (this.mTrace.isA11yTracingEnabledForTypes(128L)) {
            this.mTrace.logTrace("WindowMagnificationMgr.requestWindowMagnificationConnection", 128L, "connect=" + z);
        }
        synchronized (this.mLock) {
            if (z) {
                try {
                    int i2 = this.mConnectionState;
                    if (i2 != 1 && i2 != 0) {
                    }
                    Slog.w(TAG, "requestConnection duplicated request: connect=" + z + ", mConnectionState=" + connectionStateToString(this.mConnectionState));
                    return false;
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (z || ((i = this.mConnectionState) != 3 && i != 2)) {
                if (z) {
                    IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
                    if (!this.mReceiverRegistered) {
                        this.mContext.registerReceiver(this.mScreenStateReceiver, intentFilter);
                        this.mReceiverRegistered = true;
                    }
                } else {
                    disableAllWindowMagnifiers();
                    if (this.mReceiverRegistered) {
                        this.mContext.unregisterReceiver(this.mScreenStateReceiver);
                        this.mReceiverRegistered = false;
                    }
                }
                if (requestConnectionInternal(z)) {
                    setConnectionState(z ? 0 : 2);
                    return true;
                }
                setConnectionState(3);
                return false;
            }
            Slog.w(TAG, "requestConnection duplicated request: connect=" + z + ", mConnectionState=" + connectionStateToString(this.mConnectionState));
            return false;
        }
    }

    private boolean requestConnectionInternal(boolean z) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            StatusBarManagerInternal statusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
            if (statusBarManagerInternal != null) {
                return statusBarManagerInternal.requestWindowMagnificationConnection(z);
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return false;
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    public String getConnectionState() {
        return connectionStateToString(this.mConnectionState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setConnectionState(int i) {
        this.mConnectionState = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void disableAllWindowMagnifiers() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mWindowMagnifiers.size(); i++) {
                this.mWindowMagnifiers.valueAt(i).disableWindowMagnificationInternal(null);
            }
            this.mWindowMagnifiers.clear();
        }
    }

    public void resetAllIfNeeded(int i) {
        synchronized (this.mLock) {
            for (int i2 = 0; i2 < this.mWindowMagnifiers.size(); i2++) {
                WindowMagnifier valueAt = this.mWindowMagnifiers.valueAt(i2);
                if (valueAt != null && valueAt.mEnabled && i == valueAt.getIdOfLastServiceToControl()) {
                    valueAt.disableWindowMagnificationInternal(null);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetWindowMagnifiers() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mWindowMagnifiers.size(); i++) {
                this.mWindowMagnifiers.valueAt(i).reset();
            }
        }
    }

    public void onRectangleOnScreenRequested(int i, int i2, int i3, int i4, int i5) {
        if (this.mMagnificationFollowTypingEnabled) {
            float f = (i2 + i4) / 2.0f;
            float f2 = (i3 + i5) / 2.0f;
            synchronized (this.mLock) {
                if (this.mIsImeVisibleArray.get(i, false) && !isPositionInSourceBounds(i, f, f2) && isTrackingTypingFocusEnabled(i)) {
                    moveWindowMagnifierToPositionInternal(i, f, f2, MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMagnificationFollowTypingEnabled(boolean z) {
        this.mMagnificationFollowTypingEnabled = z;
    }

    boolean isMagnificationFollowTypingEnabled() {
        return this.mMagnificationFollowTypingEnabled;
    }

    public int getIdOfLastServiceToMagnify(int i) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return -1;
            }
            return windowMagnifier.mIdOfLastServiceToControl;
        }
    }

    void setTrackingTypingFocusEnabled(int i, boolean z) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return;
            }
            windowMagnifier.setTrackingTypingFocusEnabled(z);
        }
    }

    private void enableAllTrackingTypingFocus() {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mWindowMagnifiers.size(); i++) {
                this.mWindowMagnifiers.valueAt(i).setTrackingTypingFocusEnabled(true);
            }
        }
    }

    private void pauseTrackingTypingFocusRecord(int i) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return;
            }
            windowMagnifier.pauseTrackingTypingFocusRecord();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onImeWindowVisibilityChanged(int i, boolean z) {
        synchronized (this.mLock) {
            this.mIsImeVisibleArray.put(i, z);
        }
        if (z) {
            enableAllTrackingTypingFocus();
        } else {
            pauseTrackingTypingFocusRecord(i);
        }
    }

    boolean isImeVisible(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsImeVisibleArray.get(i);
        }
        return z;
    }

    void logTrackingTypingFocus(long j) {
        AccessibilityStatsLogUtils.logMagnificationFollowTypingFocusSession(j);
    }

    @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
    public boolean processScroll(int i, float f, float f2) {
        moveWindowMagnification(i, -f, -f2);
        setTrackingTypingFocusEnabled(i, false);
        return true;
    }

    @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
    public void setScale(int i, float f) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return;
            }
            windowMagnifier.setScale(f);
            this.mLastActivatedScale.put(i, Float.valueOf(f));
        }
    }

    public boolean enableWindowMagnification(int i, float f, float f2, float f3) {
        return enableWindowMagnification(i, f, f2, f3, MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK, 0);
    }

    public boolean enableWindowMagnification(int i, float f, float f2, float f3, MagnificationAnimationCallback magnificationAnimationCallback, int i2) {
        return enableWindowMagnification(i, f, f2, f3, magnificationAnimationCallback, 0, i2);
    }

    public boolean enableWindowMagnification(int i, float f, float f2, float f3, int i2) {
        return enableWindowMagnification(i, f, f2, f3, MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK, i2, 0);
    }

    public boolean enableWindowMagnification(int i, float f, float f2, float f3, MagnificationAnimationCallback magnificationAnimationCallback, int i2, int i3) {
        boolean z;
        boolean enableWindowMagnificationInternal;
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                windowMagnifier = createWindowMagnifier(i);
            }
            WindowMagnifier windowMagnifier2 = windowMagnifier;
            z = windowMagnifier2.mEnabled;
            enableWindowMagnificationInternal = windowMagnifier2.enableWindowMagnificationInternal(f, f2, f3, magnificationAnimationCallback, i2, i3);
            if (enableWindowMagnificationInternal) {
                this.mLastActivatedScale.put(i, Float.valueOf(getScale(i)));
            }
        }
        if (enableWindowMagnificationInternal) {
            setTrackingTypingFocusEnabled(i, true);
            if (!z) {
                this.mCallback.onWindowMagnificationActivationState(i, true);
            }
        }
        return enableWindowMagnificationInternal;
    }

    public boolean disableWindowMagnification(int i, boolean z) {
        return disableWindowMagnification(i, z, MagnificationAnimationCallback.STUB_ANIMATION_CALLBACK);
    }

    public boolean disableWindowMagnification(int i, boolean z, MagnificationAnimationCallback magnificationAnimationCallback) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return false;
            }
            boolean disableWindowMagnificationInternal = windowMagnifier.disableWindowMagnificationInternal(magnificationAnimationCallback);
            if (z) {
                this.mWindowMagnifiers.delete(i);
            }
            if (disableWindowMagnificationInternal) {
                this.mCallback.onWindowMagnificationActivationState(i, false);
            }
            return disableWindowMagnificationInternal;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int pointersInWindow(int i, MotionEvent motionEvent) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return 0;
            }
            return windowMagnifier.pointersInWindow(motionEvent);
        }
    }

    @GuardedBy({"mLock"})
    boolean isPositionInSourceBounds(int i, float f, float f2) {
        WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
        if (windowMagnifier == null) {
            return false;
        }
        return windowMagnifier.isPositionInSourceBounds(f, f2);
    }

    public boolean isWindowMagnifierEnabled(int i) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return false;
            }
            return windowMagnifier.isEnabled();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getPersistedScale(int i) {
        return MathUtils.constrain(this.mScaleProvider.getScale(i), 1.3f, 8.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void persistScale(int i) {
        float scale = getScale(i);
        if (scale < 1.3f) {
            return;
        }
        this.mScaleProvider.putScale(scale, i);
    }

    @Override // com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate
    public float getScale(int i) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier != null && windowMagnifier.mEnabled) {
                return windowMagnifier.getScale();
            }
            return 1.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public float getLastActivatedScale(int i) {
        synchronized (this.mLock) {
            if (!this.mLastActivatedScale.contains(i)) {
                return -1.0f;
            }
            return this.mLastActivatedScale.get(i).floatValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveWindowMagnification(int i, float f, float f2) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return;
            }
            windowMagnifier.move(f, f2);
        }
    }

    public boolean showMagnificationButton(int i, int i2) {
        boolean z;
        synchronized (this.mLock) {
            WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
            z = windowMagnificationConnectionWrapper != null && windowMagnificationConnectionWrapper.showMagnificationButton(i, i2);
        }
        return z;
    }

    public boolean removeMagnificationButton(int i) {
        boolean z;
        synchronized (this.mLock) {
            WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
            z = windowMagnificationConnectionWrapper != null && windowMagnificationConnectionWrapper.removeMagnificationButton(i);
        }
        return z;
    }

    public boolean removeMagnificationSettingsPanel(int i) {
        boolean z;
        synchronized (this.mLock) {
            WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
            z = windowMagnificationConnectionWrapper != null && windowMagnificationConnectionWrapper.removeMagnificationSettingsPanel(i);
        }
        return z;
    }

    public float getCenterX(int i) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier != null && windowMagnifier.mEnabled) {
                return windowMagnifier.getCenterX();
            }
            return Float.NaN;
        }
    }

    public float getCenterY(int i) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier != null && windowMagnifier.mEnabled) {
                return windowMagnifier.getCenterY();
            }
            return Float.NaN;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isTrackingTypingFocusEnabled(int i) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier == null) {
                return false;
            }
            return windowMagnifier.isTrackingTypingFocusEnabled();
        }
    }

    public void getMagnificationSourceBounds(int i, Region region) {
        synchronized (this.mLock) {
            WindowMagnifier windowMagnifier = this.mWindowMagnifiers.get(i);
            if (windowMagnifier != null && windowMagnifier.mEnabled) {
                region.set(windowMagnifier.mSourceBounds);
            }
            region.setEmpty();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public WindowMagnifier createWindowMagnifier(int i) {
        WindowMagnifier windowMagnifier = new WindowMagnifier(i, this);
        this.mWindowMagnifiers.put(i, windowMagnifier);
        return windowMagnifier;
    }

    public void onDisplayRemoved(int i) {
        disableWindowMagnification(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class ConnectionCallback extends IWindowMagnificationConnectionCallback.Stub implements IBinder.DeathRecipient {
        private boolean mExpiredDeathRecipient;

        private ConnectionCallback() {
            this.mExpiredDeathRecipient = false;
        }

        public void onWindowMagnifierBoundsChanged(int i, Rect rect) {
            if (WindowMagnificationManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                WindowMagnificationManager.this.mTrace.logTrace("WindowMagnificationMgrConnectionCallback.onWindowMagnifierBoundsChanged", 256L, "displayId=" + i + ";bounds=" + rect);
            }
            synchronized (WindowMagnificationManager.this.mLock) {
                WindowMagnifier windowMagnifier = (WindowMagnifier) WindowMagnificationManager.this.mWindowMagnifiers.get(i);
                if (windowMagnifier == null) {
                    windowMagnifier = WindowMagnificationManager.this.createWindowMagnifier(i);
                }
                windowMagnifier.setMagnifierLocation(rect);
            }
        }

        public void onChangeMagnificationMode(int i, int i2) throws RemoteException {
            if (WindowMagnificationManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                WindowMagnificationManager.this.mTrace.logTrace("WindowMagnificationMgrConnectionCallback.onChangeMagnificationMode", 256L, "displayId=" + i + ";mode=" + i2);
            }
            WindowMagnificationManager.this.mCallback.onChangeMagnificationMode(i, i2);
        }

        public void onSourceBoundsChanged(int i, Rect rect) {
            if (WindowMagnificationManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                WindowMagnificationManager.this.mTrace.logTrace("WindowMagnificationMgrConnectionCallback.onSourceBoundsChanged", 256L, "displayId=" + i + ";source=" + rect);
            }
            synchronized (WindowMagnificationManager.this.mLock) {
                WindowMagnifier windowMagnifier = (WindowMagnifier) WindowMagnificationManager.this.mWindowMagnifiers.get(i);
                if (windowMagnifier == null) {
                    windowMagnifier = WindowMagnificationManager.this.createWindowMagnifier(i);
                }
                windowMagnifier.onSourceBoundsChanged(rect);
            }
            WindowMagnificationManager.this.mCallback.onSourceBoundsChanged(i, rect);
        }

        public void onPerformScaleAction(int i, float f) {
            if (WindowMagnificationManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                WindowMagnificationManager.this.mTrace.logTrace("WindowMagnificationMgrConnectionCallback.onPerformScaleAction", 256L, "displayId=" + i + ";scale=" + f);
            }
            WindowMagnificationManager.this.mCallback.onPerformScaleAction(i, f);
        }

        public void onAccessibilityActionPerformed(int i) {
            if (WindowMagnificationManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                WindowMagnificationManager.this.mTrace.logTrace("WindowMagnificationMgrConnectionCallback.onAccessibilityActionPerformed", 256L, "displayId=" + i);
            }
            WindowMagnificationManager.this.mCallback.onAccessibilityActionPerformed(i);
        }

        public void onMove(int i) {
            if (WindowMagnificationManager.this.mTrace.isA11yTracingEnabledForTypes(256L)) {
                WindowMagnificationManager.this.mTrace.logTrace("WindowMagnificationMgrConnectionCallback.onMove", 256L, "displayId=" + i);
            }
            WindowMagnificationManager.this.setTrackingTypingFocusEnabled(i, false);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (WindowMagnificationManager.this.mLock) {
                Slog.w(WindowMagnificationManager.TAG, "binderDied DeathRecipient :" + this.mExpiredDeathRecipient);
                if (this.mExpiredDeathRecipient) {
                    return;
                }
                WindowMagnificationManager.this.mConnectionWrapper.unlinkToDeath(this);
                WindowMagnificationManager windowMagnificationManager = WindowMagnificationManager.this;
                windowMagnificationManager.mConnectionWrapper = null;
                windowMagnificationManager.mConnectionCallback = null;
                WindowMagnificationManager.this.setConnectionState(3);
                WindowMagnificationManager.this.resetWindowMagnifiers();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class WindowMagnifier {
        private static final AtomicLongFieldUpdater<WindowMagnifier> SUM_TIME_UPDATER = AtomicLongFieldUpdater.newUpdater(WindowMagnifier.class, "mTrackingTypingFocusSumTime");
        private final int mDisplayId;
        private boolean mEnabled;
        private final WindowMagnificationManager mWindowMagnificationManager;
        private float mScale = 1.0f;
        private final Rect mBounds = new Rect();
        private final Rect mSourceBounds = new Rect();
        private int mIdOfLastServiceToControl = -1;
        private final PointF mMagnificationFrameOffsetRatio = new PointF(0.0f, 0.0f);
        private boolean mTrackingTypingFocusEnabled = true;
        private volatile long mTrackingTypingFocusStartTime = 0;
        private volatile long mTrackingTypingFocusSumTime = 0;

        WindowMagnifier(int i, WindowMagnificationManager windowMagnificationManager) {
            this.mDisplayId = i;
            this.mWindowMagnificationManager = windowMagnificationManager;
        }

        boolean enableWindowMagnificationInternal(float f, float f2, float f3, MagnificationAnimationCallback magnificationAnimationCallback, int i, int i2) {
            if (Float.isNaN(f)) {
                f = getScale();
            }
            float constrainScale = MagnificationScaleProvider.constrainScale(f);
            setMagnificationFrameOffsetRatioByWindowPosition(i);
            WindowMagnificationManager windowMagnificationManager = this.mWindowMagnificationManager;
            int i3 = this.mDisplayId;
            PointF pointF = this.mMagnificationFrameOffsetRatio;
            if (!windowMagnificationManager.enableWindowMagnificationInternal(i3, constrainScale, f2, f3, pointF.x, pointF.y, magnificationAnimationCallback)) {
                return false;
            }
            this.mScale = constrainScale;
            this.mEnabled = true;
            this.mIdOfLastServiceToControl = i2;
            return true;
        }

        void setMagnificationFrameOffsetRatioByWindowPosition(int i) {
            if (i == 0) {
                this.mMagnificationFrameOffsetRatio.set(0.0f, 0.0f);
            } else {
                if (i != 1) {
                    return;
                }
                this.mMagnificationFrameOffsetRatio.set(-1.0f, -1.0f);
            }
        }

        boolean disableWindowMagnificationInternal(MagnificationAnimationCallback magnificationAnimationCallback) {
            if (!this.mEnabled || !this.mWindowMagnificationManager.disableWindowMagnificationInternal(this.mDisplayId, magnificationAnimationCallback)) {
                return false;
            }
            this.mEnabled = false;
            this.mIdOfLastServiceToControl = -1;
            this.mTrackingTypingFocusEnabled = false;
            pauseTrackingTypingFocusRecord();
            return true;
        }

        @GuardedBy({"mLock"})
        void setScale(float f) {
            if (this.mEnabled) {
                float constrainScale = MagnificationScaleProvider.constrainScale(f);
                if (Float.compare(this.mScale, constrainScale) == 0 || !this.mWindowMagnificationManager.setScaleInternal(this.mDisplayId, f)) {
                    return;
                }
                this.mScale = constrainScale;
            }
        }

        @GuardedBy({"mLock"})
        float getScale() {
            return this.mScale;
        }

        @GuardedBy({"mLock"})
        void setMagnifierLocation(Rect rect) {
            this.mBounds.set(rect);
        }

        int getIdOfLastServiceToControl() {
            return this.mIdOfLastServiceToControl;
        }

        int pointersInWindow(MotionEvent motionEvent) {
            int pointerCount = motionEvent.getPointerCount();
            int i = 0;
            for (int i2 = 0; i2 < pointerCount; i2++) {
                if (this.mBounds.contains((int) motionEvent.getX(i2), (int) motionEvent.getY(i2))) {
                    i++;
                }
            }
            return i;
        }

        boolean isPositionInSourceBounds(float f, float f2) {
            return this.mSourceBounds.contains((int) f, (int) f2);
        }

        void setTrackingTypingFocusEnabled(boolean z) {
            if (this.mWindowMagnificationManager.isWindowMagnifierEnabled(this.mDisplayId) && this.mWindowMagnificationManager.isImeVisible(this.mDisplayId) && z) {
                startTrackingTypingFocusRecord();
            }
            if (this.mTrackingTypingFocusEnabled && !z) {
                stopAndLogTrackingTypingFocusRecordIfNeeded();
            }
            this.mTrackingTypingFocusEnabled = z;
        }

        boolean isTrackingTypingFocusEnabled() {
            return this.mTrackingTypingFocusEnabled;
        }

        void startTrackingTypingFocusRecord() {
            if (this.mTrackingTypingFocusStartTime == 0) {
                this.mTrackingTypingFocusStartTime = SystemClock.uptimeMillis();
            }
        }

        void pauseTrackingTypingFocusRecord() {
            if (this.mTrackingTypingFocusStartTime != 0) {
                SUM_TIME_UPDATER.addAndGet(this, SystemClock.uptimeMillis() - this.mTrackingTypingFocusStartTime);
                this.mTrackingTypingFocusStartTime = 0L;
            }
        }

        void stopAndLogTrackingTypingFocusRecordIfNeeded() {
            if (this.mTrackingTypingFocusStartTime == 0 && this.mTrackingTypingFocusSumTime == 0) {
                return;
            }
            this.mWindowMagnificationManager.logTrackingTypingFocus(this.mTrackingTypingFocusSumTime + (this.mTrackingTypingFocusStartTime != 0 ? SystemClock.uptimeMillis() - this.mTrackingTypingFocusStartTime : 0L));
            this.mTrackingTypingFocusStartTime = 0L;
            this.mTrackingTypingFocusSumTime = 0L;
        }

        boolean isEnabled() {
            return this.mEnabled;
        }

        @GuardedBy({"mLock"})
        void move(float f, float f2) {
            this.mWindowMagnificationManager.moveWindowMagnifierInternal(this.mDisplayId, f, f2);
        }

        @GuardedBy({"mLock"})
        void reset() {
            this.mEnabled = false;
            this.mIdOfLastServiceToControl = -1;
            this.mSourceBounds.setEmpty();
        }

        @GuardedBy({"mLock"})
        public void onSourceBoundsChanged(Rect rect) {
            this.mSourceBounds.set(rect);
        }

        @GuardedBy({"mLock"})
        float getCenterX() {
            return this.mSourceBounds.exactCenterX();
        }

        @GuardedBy({"mLock"})
        float getCenterY() {
            return this.mSourceBounds.exactCenterY();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean enableWindowMagnificationInternal(int i, float f, float f2, float f3, float f4, float f5, MagnificationAnimationCallback magnificationAnimationCallback) {
        long uptimeMillis = SystemClock.uptimeMillis() + 100;
        while (this.mConnectionState == 0 && SystemClock.uptimeMillis() < uptimeMillis) {
            try {
                this.mLock.wait(uptimeMillis - SystemClock.uptimeMillis());
            } catch (InterruptedException unused) {
            }
        }
        WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
        if (windowMagnificationConnectionWrapper == null) {
            Slog.w(TAG, "enableWindowMagnificationInternal mConnectionWrapper is null. mConnectionState=" + connectionStateToString(this.mConnectionState));
            return false;
        }
        return windowMagnificationConnectionWrapper.enableWindowMagnification(i, f, f2, f3, f4, f5, magnificationAnimationCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setScaleInternal(int i, float f) {
        WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
        return windowMagnificationConnectionWrapper != null && windowMagnificationConnectionWrapper.setScale(i, f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean disableWindowMagnificationInternal(int i, MagnificationAnimationCallback magnificationAnimationCallback) {
        WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
        if (windowMagnificationConnectionWrapper == null) {
            Slog.w(TAG, "mConnectionWrapper is null");
            return false;
        }
        return windowMagnificationConnectionWrapper.disableWindowMagnification(i, magnificationAnimationCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public boolean moveWindowMagnifierInternal(int i, float f, float f2) {
        WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
        return windowMagnificationConnectionWrapper != null && windowMagnificationConnectionWrapper.moveWindowMagnifier(i, f, f2);
    }

    @GuardedBy({"mLock"})
    private boolean moveWindowMagnifierToPositionInternal(int i, float f, float f2, MagnificationAnimationCallback magnificationAnimationCallback) {
        WindowMagnificationConnectionWrapper windowMagnificationConnectionWrapper = this.mConnectionWrapper;
        return windowMagnificationConnectionWrapper != null && windowMagnificationConnectionWrapper.moveWindowMagnifierToPosition(i, f, f2, magnificationAnimationCallback);
    }
}
