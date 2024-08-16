package com.android.server.wm;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.util.Slog;
import android.view.DisplayCutout;
import android.view.DisplayInfo;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManagerPolicyConstants;
import android.widget.OverScroller;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.charger.ChargerErrorCode;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class SystemGesturesPointerEventListener implements WindowManagerPolicyConstants.PointerEventListener {
    private static final boolean DEBUG = false;
    static boolean DEBUG_PANIC = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final int MAX_FLING_TIME_MILLIS = 5000;
    private static final int MAX_TRACKED_POINTERS = 32;
    private static final int SWIPE_FROM_BOTTOM = 2;
    private static final int SWIPE_FROM_LEFT = 4;
    private static final int SWIPE_FROM_RIGHT = 3;
    private static final int SWIPE_FROM_TOP = 1;
    private static final int SWIPE_NONE = 0;
    private static final long SWIPE_TIMEOUT_MS = 500;
    private static final String TAG = "SystemGestures";
    private static final int UNTRACKED_POINTER = -1;
    private final Callbacks mCallbacks;
    private final Context mContext;
    private boolean mDebugFireable;
    private int mDisplayCutoutTouchableRegionSize;
    private int mDownPointers;
    private GestureDetector mGestureDetector;
    private final Handler mHandler;
    private long mLastFlingTime;
    private boolean mMouseHoveringAtBottom;
    private boolean mMouseHoveringAtLeft;
    private boolean mMouseHoveringAtRight;
    private boolean mMouseHoveringAtTop;
    private int mSwipeDistanceThreshold;
    private boolean mSwipeFireable;
    ISystemGesturesPointerEventListenerExt mSystemGesturesPointerEventListenerExt;
    int screenHeight;
    int screenWidth;
    private final Rect mSwipeStartThreshold = new Rect();
    private final int[] mDownPointerId = new int[32];
    private final float[] mDownX = new float[32];
    private final float[] mDownY = new float[32];
    private final long[] mDownTime = new long[32];
    ISystemGesturesPointerEventListenerSocExt mSocExt = (ISystemGesturesPointerEventListenerSocExt) ExtLoader.type(ISystemGesturesPointerEventListenerSocExt.class).base(this).create();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    interface Callbacks {
        void onDebug();

        void onDown();

        void onFling(int i);

        void onHorizontalFling(int i);

        void onMouseHoverAtBottom();

        void onMouseHoverAtLeft();

        void onMouseHoverAtRight();

        void onMouseHoverAtTop();

        void onMouseLeaveFromBottom();

        void onMouseLeaveFromLeft();

        void onMouseLeaveFromRight();

        void onMouseLeaveFromTop();

        void onScroll(boolean z);

        void onSwipeFromBottom();

        void onSwipeFromLeft();

        void onSwipeFromRight();

        void onSwipeFromTop();

        void onUpOrCancel();

        void onVerticalFling(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SystemGesturesPointerEventListener(Context context, Handler handler, Callbacks callbacks) {
        this.mContext = (Context) checkNull("context", context);
        this.mHandler = handler;
        this.mCallbacks = (Callbacks) checkNull("callbacks", callbacks);
        ISystemGesturesPointerEventListenerExt iSystemGesturesPointerEventListenerExt = (ISystemGesturesPointerEventListenerExt) ExtLoader.type(ISystemGesturesPointerEventListenerExt.class).base(this).create();
        this.mSystemGesturesPointerEventListenerExt = iSystemGesturesPointerEventListenerExt;
        iSystemGesturesPointerEventListenerExt.init(context);
        onConfigurationChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDisplayInfoChanged(DisplayInfo displayInfo) {
        this.screenWidth = displayInfo.logicalWidth;
        this.screenHeight = displayInfo.logicalHeight;
        onConfigurationChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onConfigurationChanged() {
        Resources resources = this.mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(17105586);
        this.mSwipeStartThreshold.set(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        this.mSwipeDistanceThreshold = dimensionPixelSize;
        DisplayCutout cutout = DisplayManagerGlobal.getInstance().getRealDisplay(0).getCutout();
        if (cutout != null) {
            this.mDisplayCutoutTouchableRegionSize = resources.getDimensionPixelSize(R.dimen.harmful_app_message_padding_bottom);
            Rect[] boundingRectsAll = cutout.getBoundingRectsAll();
            Rect rect = boundingRectsAll[0];
            if (rect != null) {
                Rect rect2 = this.mSwipeStartThreshold;
                rect2.left = Math.max(rect2.left, rect.width() + this.mDisplayCutoutTouchableRegionSize);
            }
            Rect rect3 = boundingRectsAll[1];
            if (rect3 != null) {
                Rect rect4 = this.mSwipeStartThreshold;
                rect4.top = Math.max(rect4.top, rect3.height() + this.mDisplayCutoutTouchableRegionSize);
            }
            Rect rect5 = boundingRectsAll[2];
            if (rect5 != null) {
                Rect rect6 = this.mSwipeStartThreshold;
                rect6.right = Math.max(rect6.right, rect5.width() + this.mDisplayCutoutTouchableRegionSize);
            }
            Rect rect7 = boundingRectsAll[3];
            if (rect7 != null) {
                Rect rect8 = this.mSwipeStartThreshold;
                rect8.bottom = Math.max(rect8.bottom, rect7.height() + this.mDisplayCutoutTouchableRegionSize);
            }
        }
        this.mSystemGesturesPointerEventListenerExt.updateDefaultSwipeDistance();
    }

    private static <T> T checkNull(String str, T t) {
        if (t != null) {
            return t;
        }
        throw new IllegalArgumentException(str + " must not be null");
    }

    public void systemReady() {
        this.mHandler.post(new Runnable() { // from class: com.android.server.wm.SystemGesturesPointerEventListener$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SystemGesturesPointerEventListener.this.lambda$systemReady$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemReady$0() {
        int displayId = this.mContext.getDisplayId();
        if (DisplayManagerGlobal.getInstance().getDisplayInfo(displayId) == null) {
            Slog.w(TAG, "Cannot create GestureDetector, display removed:" + displayId);
            return;
        }
        this.mGestureDetector = new GestureDetector(this.mContext, new FlingGestureDetector(), this.mSystemGesturesPointerEventListenerExt.getOplusUiHandler(this.mHandler)) { // from class: com.android.server.wm.SystemGesturesPointerEventListener.1
        };
    }

    public void onPointerEvent(MotionEvent motionEvent) {
        if (this.mGestureDetector != null && motionEvent.isTouchEvent()) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mSwipeFireable = true;
            if (this.mSystemGesturesPointerEventListenerExt.inSplitHandleRegion(motionEvent)) {
                Slog.d(TAG, "swipe gesture start in split-screen handle region");
                this.mSwipeFireable = false;
            }
            if (this.mSystemGesturesPointerEventListenerExt.isOnePuttHandleRegion(motionEvent)) {
                Slog.d(TAG, "swipe gesture start in one putt handle region");
                this.mSwipeFireable = false;
            }
            this.mSystemGesturesPointerEventListenerExt.notifyMotionDown();
            this.mDebugFireable = true;
            this.mSocExt.hookSetScrollFired(false);
            this.mDownPointers = 0;
            captureDown(motionEvent, 0);
            if (this.mMouseHoveringAtLeft) {
                this.mMouseHoveringAtLeft = false;
                this.mCallbacks.onMouseLeaveFromLeft();
            }
            if (this.mMouseHoveringAtTop) {
                this.mMouseHoveringAtTop = false;
                this.mCallbacks.onMouseLeaveFromTop();
            }
            if (this.mMouseHoveringAtRight) {
                this.mMouseHoveringAtRight = false;
                this.mCallbacks.onMouseLeaveFromRight();
            }
            if (this.mMouseHoveringAtBottom) {
                this.mMouseHoveringAtBottom = false;
                this.mCallbacks.onMouseLeaveFromBottom();
            }
            this.mCallbacks.onDown();
            return;
        }
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                if (this.mSwipeFireable) {
                    int detectSwipe = detectSwipe(motionEvent);
                    boolean z = detectSwipe == 0;
                    this.mSwipeFireable = z;
                    if (!z && DEBUG_PANIC) {
                        Slog.d(TAG, "detectSwipe: " + detectSwipe);
                    }
                    if (detectSwipe == 1) {
                        this.mCallbacks.onSwipeFromTop();
                        return;
                    }
                    if (detectSwipe == 2) {
                        this.mCallbacks.onSwipeFromBottom();
                        return;
                    } else if (detectSwipe == 3) {
                        this.mCallbacks.onSwipeFromRight();
                        return;
                    } else {
                        if (detectSwipe == 4) {
                            this.mCallbacks.onSwipeFromLeft();
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            if (actionMasked != 3) {
                if (actionMasked == 5) {
                    captureDown(motionEvent, motionEvent.getActionIndex());
                    if (this.mDebugFireable) {
                        boolean z2 = motionEvent.getPointerCount() < 5;
                        this.mDebugFireable = z2;
                        if (z2) {
                            return;
                        }
                        this.mCallbacks.onDebug();
                        return;
                    }
                    return;
                }
                if (actionMasked == 7 && motionEvent.isFromSource(8194)) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    boolean z3 = this.mMouseHoveringAtLeft;
                    if (!z3 && x == 0.0f) {
                        this.mCallbacks.onMouseHoverAtLeft();
                        this.mMouseHoveringAtLeft = true;
                    } else if (z3 && x > 0.0f) {
                        this.mCallbacks.onMouseLeaveFromLeft();
                        this.mMouseHoveringAtLeft = false;
                    }
                    boolean z4 = this.mMouseHoveringAtTop;
                    if (!z4 && y == 0.0f) {
                        this.mCallbacks.onMouseHoverAtTop();
                        this.mMouseHoveringAtTop = true;
                    } else if (z4 && y > 0.0f) {
                        this.mCallbacks.onMouseLeaveFromTop();
                        this.mMouseHoveringAtTop = false;
                    }
                    boolean z5 = this.mMouseHoveringAtRight;
                    if (!z5 && x >= this.screenWidth - 1) {
                        this.mCallbacks.onMouseHoverAtRight();
                        this.mMouseHoveringAtRight = true;
                    } else if (z5 && x < this.screenWidth - 1) {
                        this.mCallbacks.onMouseLeaveFromRight();
                        this.mMouseHoveringAtRight = false;
                    }
                    boolean z6 = this.mMouseHoveringAtBottom;
                    if (!z6 && y >= this.screenHeight - 1) {
                        this.mCallbacks.onMouseHoverAtBottom();
                        this.mMouseHoveringAtBottom = true;
                        return;
                    } else {
                        if (!z6 || y >= this.screenHeight - 1) {
                            return;
                        }
                        this.mCallbacks.onMouseLeaveFromBottom();
                        this.mMouseHoveringAtBottom = false;
                        return;
                    }
                }
                return;
            }
        }
        this.mSwipeFireable = false;
        this.mDebugFireable = false;
        if (this.mSocExt.hookGetScrollFired()) {
            this.mCallbacks.onScroll(false);
        }
        this.mSocExt.hookSetScrollFired(false);
        this.mSystemGesturesPointerEventListenerExt.notifyMotionUpOrCancel();
        this.mCallbacks.onUpOrCancel();
    }

    private void captureDown(MotionEvent motionEvent, int i) {
        int findIndex = findIndex(motionEvent.getPointerId(i));
        if (findIndex != -1) {
            this.mDownX[findIndex] = motionEvent.getX(i);
            this.mDownY[findIndex] = motionEvent.getY(i);
            this.mDownTime[findIndex] = motionEvent.getEventTime();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean currentGestureStartedInRegion(Region region) {
        return region.contains((int) this.mDownX[0], (int) this.mDownY[0]);
    }

    private int findIndex(int i) {
        int i2 = 0;
        while (true) {
            int i3 = this.mDownPointers;
            if (i2 >= i3) {
                if (i3 == 32 || i == -1) {
                    return -1;
                }
                int[] iArr = this.mDownPointerId;
                int i4 = i3 + 1;
                this.mDownPointers = i4;
                iArr[i3] = i;
                return i4 - 1;
            }
            if (this.mDownPointerId[i2] == i) {
                return i2;
            }
            i2++;
        }
    }

    private int detectSwipe(MotionEvent motionEvent) {
        int historySize = motionEvent.getHistorySize();
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int findIndex = findIndex(motionEvent.getPointerId(i));
            if (findIndex != -1) {
                for (int i2 = 0; i2 < historySize; i2++) {
                    int detectSwipe = detectSwipe(findIndex, motionEvent.getHistoricalEventTime(i2), motionEvent.getHistoricalX(i, i2), motionEvent.getHistoricalY(i, i2));
                    if (detectSwipe != 0) {
                        return detectSwipe;
                    }
                }
                int detectSwipe2 = detectSwipe(findIndex, motionEvent.getEventTime(), motionEvent.getX(i), motionEvent.getY(i));
                if (detectSwipe2 != 0) {
                    return detectSwipe2;
                }
            }
        }
        return 0;
    }

    private int detectSwipe(int i, long j, float f, float f2) {
        float f3 = this.mDownX[i];
        float f4 = this.mDownY[i];
        long j2 = j - this.mDownTime[i];
        Rect rect = this.mSwipeStartThreshold;
        if (f4 <= rect.top && f2 > this.mSwipeDistanceThreshold + f4 && j2 < SWIPE_TIMEOUT_MS) {
            return this.mSystemGesturesPointerEventListenerExt.hookSwipeFromTop(f3, f4) ? -1 : 1;
        }
        int i2 = this.screenHeight;
        if (f4 >= i2 - rect.bottom && f2 < f4 - this.mSwipeDistanceThreshold && j2 < SWIPE_TIMEOUT_MS) {
            return !this.mSystemGesturesPointerEventListenerExt.checkSwipeFromBottom(f3, f4, i2) ? 0 : 2;
        }
        if (f3 < this.screenWidth - rect.right || f >= f3 - this.mSwipeDistanceThreshold || j2 >= SWIPE_TIMEOUT_MS) {
            return (f3 > ((float) rect.left) || f <= f3 + ((float) this.mSwipeDistanceThreshold) || j2 >= SWIPE_TIMEOUT_MS) ? 0 : 4;
        }
        return 3;
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG + ":");
        printWriter.print(str2);
        printWriter.print("mDisplayCutoutTouchableRegionSize=");
        printWriter.println(this.mDisplayCutoutTouchableRegionSize);
        printWriter.print(str2);
        printWriter.print("mSwipeStartThreshold=");
        printWriter.println(this.mSwipeStartThreshold);
        printWriter.print(str2);
        printWriter.print("mSwipeDistanceThreshold=");
        printWriter.println(this.mSwipeDistanceThreshold);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class FlingGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private OverScroller mOverscroller;

        FlingGestureDetector() {
            this.mOverscroller = new OverScroller(SystemGesturesPointerEventListener.this.mContext);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (!this.mOverscroller.isFinished()) {
                this.mOverscroller.forceFinished(true);
            }
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            this.mOverscroller.computeScrollOffset();
            long uptimeMillis = SystemClock.uptimeMillis();
            if (SystemGesturesPointerEventListener.this.mLastFlingTime != 0 && uptimeMillis > SystemGesturesPointerEventListener.this.mLastFlingTime + 5000) {
                this.mOverscroller.forceFinished(true);
            }
            this.mOverscroller.fling(0, 0, (int) f, (int) f2, ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, Integer.MAX_VALUE, ChargerErrorCode.ERR_FILE_FAILURE_ACCESS, Integer.MAX_VALUE);
            int duration = this.mOverscroller.getDuration();
            if (duration > SystemGesturesPointerEventListener.MAX_FLING_TIME_MILLIS) {
                duration = SystemGesturesPointerEventListener.MAX_FLING_TIME_MILLIS;
            }
            SystemGesturesPointerEventListener systemGesturesPointerEventListener = SystemGesturesPointerEventListener.this;
            systemGesturesPointerEventListener.mSocExt.hookOnFling(systemGesturesPointerEventListener.mCallbacks, f, f2, duration);
            SystemGesturesPointerEventListener.this.mSystemGesturesPointerEventListenerExt.hookOnGlobalFlingGesture(duration);
            SystemGesturesPointerEventListener.this.mLastFlingTime = uptimeMillis;
            SystemGesturesPointerEventListener.this.mSystemGesturesPointerEventListenerExt.notifyFlingGestureStatus(duration);
            SystemGesturesPointerEventListener.this.mCallbacks.onFling(duration);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!SystemGesturesPointerEventListener.this.mSocExt.hookGetScrollFired()) {
                SystemGesturesPointerEventListener.this.mCallbacks.onScroll(true);
                SystemGesturesPointerEventListener.this.mSocExt.hookSetScrollFired(true);
            }
            SystemGesturesPointerEventListener.this.mSystemGesturesPointerEventListenerExt.notifyScrollGestureStatus();
            return true;
        }
    }
}
