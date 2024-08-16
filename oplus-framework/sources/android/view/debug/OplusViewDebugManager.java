package android.view.debug;

import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.ResourcesImpl;
import android.graphics.Rect;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LogPrinter;
import android.util.Printer;
import android.view.Choreographer;
import android.view.DisplayEventReceiver;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.android.internal.policy.DecorView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/* loaded from: classes.dex */
public class OplusViewDebugManager implements IOplusViewDebugManager {
    private static final String DEBUG_MSG_PROPERTY_NAME = "oplus_property_view_debug_msg";
    private static final String DEBUG_SCROLL_PROPERTY_NAME = "oplus_property_view_debug_scroll";
    private long mCurrTime;
    private boolean mEnabled;
    private int mInputCount;
    private Printer mLogPrinter;
    private int mScrollCount;
    private int mUpdateCount;
    private int mVsyncCount;
    private static final String TAG = OplusViewDebugManager.class.getSimpleName();
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final boolean EXTRAINFOENABLE = SystemProperties.getBoolean("persist.sys.view.extrainfo", false);
    private static final boolean LIGHT_OS = SystemProperties.getBoolean("ro.oplus.lightos", false);
    private boolean mHasViewDebugProperty = SystemProperties.getBoolean(DEBUG_SCROLL_PROPERTY_NAME, true);
    private boolean mHasMsgDebugProperty = SystemProperties.getBoolean(DEBUG_MSG_PROPERTY_NAME, false);
    private DateTimeFormatter mDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
    private long mMainThreadId = Looper.getMainLooper().getThread().getId();

    public OplusViewDebugManager() {
        Log.d(TAG, "OplusViewDebugManager Constructor " + this + " [mHasViewDebugProperty " + this.mHasViewDebugProperty + "] [mHasMsgDebugProperty " + this.mHasMsgDebugProperty + "] [DEBUG " + DEBUG + "] [EXTRAINFOENABLE " + EXTRAINFOENABLE + "] [LIGHT_OS " + LIGHT_OS + "]");
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public boolean isEnabled() {
        return this.mHasViewDebugProperty && this.mEnabled && !LIGHT_OS;
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnTouchEventMove(int y, int deltaY, int lastMotionY) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "AAonTouchEvent " + this.mScrollCount + " deltaY " + deltaY + " y " + y + " lastMotionY " + lastMotionY);
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnOverScrolled(int deltaY) {
        this.mScrollCount++;
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "AAonOverScrolled " + this.mScrollCount + " deltaY = " + deltaY);
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnStartScroll() {
        if (!isEnabled()) {
            return;
        }
        this.mCurrTime = 0L;
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnFling() {
        if (!isEnabled()) {
            return;
        }
        this.mCurrTime = 0L;
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public long markOnUpdateStart(long startTime, long currentTime) {
        if (!isEnabled()) {
            return currentTime;
        }
        long frameInterval = Choreographer.getInstance().getFrameIntervalNanos() / 1000000;
        long j = this.mCurrTime + frameInterval;
        this.mCurrTime = j;
        return j;
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnUpdateSpline(int splineDuration, float t, long currentTime, int offset) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            long frameInterval = Choreographer.getInstance().getFrameIntervalNanos() / 1000000;
            Trace.traceBegin(8L, "AASPLINE " + this.mUpdateCount + ";currentTime = " + currentTime + ";mSplineDuration = " + splineDuration + ";t = " + t + "; deltaTime = " + (currentTime - this.mCurrTime) + "; frameInterval = " + frameInterval + " update offset = " + offset);
        }
        this.mCurrTime = currentTime;
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnUpdateEnd() {
        this.mUpdateCount++;
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markBeforeScroll(int y, int lastY, int touchMode, int rawDeltaY) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            StringBuilder sb = new StringBuilder("AbsListView#scrollIfNeeded:y=" + y + " lastY=" + lastY + " touchMode=" + touchMode + " rawDeltaY=" + rawDeltaY);
            Trace.traceBegin(8L, sb.toString());
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markAfterScroll() {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markBeforeDispatchTouchEvent(MotionEvent event, String title) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "VRI#processPointerEvent title = " + title + "; event = " + event);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markAfterDispatchTouchEvent(MotionEvent event) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markAndDumpWindowFocusChangeMsg(String tag, Handler handler) {
        if (!DEBUG) {
            return;
        }
        Log.d(tag, "send msg MSG_WINDOW_FOCUS_CHANGED with caller " + Debug.getCallers(20));
        if (!this.mHasMsgDebugProperty || handler == null) {
            return;
        }
        if (this.mLogPrinter == null) {
            this.mLogPrinter = new LogPrinter(3, tag);
        }
        handler.dump(this.mLogPrinter, tag);
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnHandleMessageImpl(String msgName) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        String str = "[MSG] " + msgName;
        StringBuilder builder = getStringBuilder().append(str);
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public boolean markOnDispatchTouchEvent(MotionEvent event, View view) {
        return DEBUG && isEnabled() && event != null && view != null && event.getActionMasked() == 2 && (view instanceof DecorView);
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnAddView(View child) {
        if (DEBUG && EXTRAINFOENABLE) {
            if (child == null) {
                StringBuilder builder = getStringBuilder().append("[WARNING] markOnAddView null");
                OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
                return;
            }
            long currThreadId = Thread.currentThread().getId();
            if (currThreadId != this.mMainThreadId) {
                String str = "[WARNING] async markOnAddView " + child.getClass().getSimpleName();
                StringBuilder builder2 = getStringBuilder().append(str);
                OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder2.toString());
            }
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnRemoveView(View child) {
        if (DEBUG && EXTRAINFOENABLE) {
            if (child == null) {
                StringBuilder builder = getStringBuilder().append("[WARNING] markOnRemoveView null");
                OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
                return;
            }
            long currThreadId = Thread.currentThread().getId();
            if (currThreadId != this.mMainThreadId) {
                String str = "[WARNING] async markOnRemoveView " + child.getClass().getSimpleName();
                StringBuilder builder2 = getStringBuilder().append(str);
                OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder2.toString());
            }
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnRequestLayout() {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        long currThreadId = Thread.currentThread().getId();
        if (currThreadId != this.mMainThreadId) {
            StringBuilder builder = getStringBuilder().append("[WARNING] async markOnRequestLayout");
            OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnInvalidate() {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        long currThreadId = Thread.currentThread().getId();
        if (currThreadId != this.mMainThreadId) {
            StringBuilder builder = getStringBuilder().append("[WARNING] async markOnInvalidate ");
            OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnFocusChange(boolean gainFocus, boolean hasWindowFocus, View view) {
        if (!DEBUG || !EXTRAINFOENABLE || view == null) {
            return;
        }
        String viewName = view.getClass().getSimpleName();
        String str = "[FOCUS] " + viewName + " focus change to " + gainFocus + " with windowFocus " + hasWindowFocus;
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, getStringBuilder().append(str).toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markMeasureStart(View view, int widthMeasureSpec, int heightMeasureSpec) {
        if (!DEBUG) {
            return;
        }
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        if (height > 10000) {
            StringBuilder builder = getStringBuilder();
            builder.append("[WARNING]").append("abnormal height ").append(height).append(" for ").append(view.getClass().getSimpleName()).append(" parent ").append(view.getParent() == null ? "null" : view.getParent().getClass().getSimpleName());
            if (EXTRAINFOENABLE) {
                OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
            }
            Log.w(TAG, builder.toString());
        }
        if (width > 10000) {
            StringBuilder builder2 = getStringBuilder();
            builder2.append("[WARNING]").append("abnormal width ").append(height).append(" for ").append(view.getClass().getSimpleName()).append(" parent ").append(view.getParent() != null ? view.getParent().getClass().getSimpleName() : "null");
            if (EXTRAINFOENABLE) {
                OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder2.toString());
            }
            Log.w(TAG, builder2.toString());
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnInputEvent(InputEvent event) {
        this.mInputCount++;
        if (Trace.isTagEnabled(8L) && (event instanceof MotionEvent)) {
            Trace.traceBegin(8L, "AAInputEventReceiver dispatchInputEvent " + this.mInputCount + " y " + ((MotionEvent) event).getY());
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public String markOnVsync(DisplayEventReceiver.VsyncEventData vsyncEventData, long timestampNanos, long lastFrameTimeNanos) {
        if (!DEBUG || vsyncEventData == null) {
            return "";
        }
        this.mVsyncCount++;
        return this.mVsyncCount + " " + vsyncEventData.preferredFrameTimeline().vsyncId + " time " + (((float) (timestampNanos - lastFrameTimeNanos)) * 1.0E-6f);
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public String markOnDoframe(DisplayEventReceiver.VsyncEventData vsyncEventData, long timestampNanos, long lastFrameTimeNanos) {
        if (!DEBUG) {
            return "";
        }
        return " Frame time delta " + (((float) (timestampNanos - lastFrameTimeNanos)) * 1.0E-6f) + " ms";
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnBindApplication(ApplicationInfo applicationInfo) {
        if (!DEBUG || applicationInfo == null) {
            return;
        }
        if (isSystemApp(applicationInfo) || isWhiteListThirdPartyApp(applicationInfo)) {
            Log.d(TAG, "for " + applicationInfo.packageName + ", enable view debug");
            this.mEnabled = true;
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public String markPerformLayout(View hostView, WindowManager.LayoutParams windowAttrs) {
        if (!DEBUG) {
            return "";
        }
        String windowTitle = getVRITitle(windowAttrs);
        int measuredWidth = hostView.getMeasuredWidth();
        int measuredHeight = hostView.getMeasuredHeight();
        if (EXTRAINFOENABLE) {
            String str = "[LAYOUT] for " + windowTitle + " width " + measuredWidth + " height " + measuredHeight;
            StringBuilder logBuilder = getStringBuilder().append(str);
            OplusViewExtraInfoHelper.getInstance().pushInfo(0, logBuilder.toString());
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" for " + windowTitle);
        builder.append(" width " + measuredWidth);
        builder.append(" height " + measuredHeight);
        return builder.toString();
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public String markPerformMeasure(View hostView, WindowManager.LayoutParams windowAttrs, int childWidthMeasureSpec, int childHeightMeasureSpec) {
        if (!DEBUG) {
            return "";
        }
        String windowTitle = getVRITitle(windowAttrs);
        int width = View.MeasureSpec.getSize(childWidthMeasureSpec);
        int height = View.MeasureSpec.getSize(childHeightMeasureSpec);
        String str = "[MEASURE] for " + windowTitle + " width " + width + " height " + height;
        StringBuilder logBuilder = getStringBuilder().append(str);
        if (EXTRAINFOENABLE) {
            OplusViewExtraInfoHelper.getInstance().pushInfo(0, logBuilder.toString());
        }
        Log.w(TAG, logBuilder.toString());
        if (LIGHT_OS) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" for " + windowTitle);
        builder.append(" width " + View.MeasureSpec.getSize(childWidthMeasureSpec));
        builder.append(" widthMode " + View.MeasureSpec.getMode(childWidthMeasureSpec));
        builder.append(" height " + View.MeasureSpec.getSize(childHeightMeasureSpec));
        builder.append(" heightMode " + View.MeasureSpec.getMode(childHeightMeasureSpec));
        return builder.toString();
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public String markPerformDraw(View hostView, WindowManager.LayoutParams windowAttrs) {
        if (!DEBUG) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" for " + getVRITitle(windowAttrs));
        return builder.toString();
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public String markScheduleTraversals(View hostView, WindowManager.LayoutParams windowAttrs) {
        return "";
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markPerformMeasureReason(String reason) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, getStringBuilder().append("[MEASURE] markPerformMeasureReason ").append(reason).toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markPerformLayoutReason(String reason) {
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnSetFrame(Rect frame, WindowManager.LayoutParams windowAttrs) {
        if (!DEBUG) {
            return;
        }
        String fullTitle = getVRITitle(windowAttrs);
        if (EXTRAINFOENABLE) {
            OplusViewExtraInfoHelper.getInstance().pushInfo(0, getStringBuilder().append("[FRAME] setFrame for ").append(fullTitle).append(" ").append(frame.toString()).toString());
        }
        Log.d(TAG, "setFrame for " + fullTitle + " " + frame.toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markHandleAppVisibility(boolean visible, WindowManager.LayoutParams windowAttrs) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        StringBuilder builder = getStringBuilder();
        builder.append("[VISIBILITY] visible ").append(visible);
        if (windowAttrs != null) {
            String fullTitle = getVRITitle(windowAttrs);
            builder.append(" for ").append(fullTitle);
        }
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
        if (!visible) {
            OplusViewExtraInfoHelper.resetInstance();
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markShowInsets(int insetsType, boolean fromIme) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        boolean systemBars = (WindowInsets.Type.systemBars() & insetsType) != 0;
        boolean captionBar = (WindowInsets.Type.captionBar() & insetsType) != 0;
        boolean displayCutout = (WindowInsets.Type.displayCutout() & insetsType) != 0;
        boolean ime = (WindowInsets.Type.ime() & insetsType) != 0;
        boolean mandatorySystemGestures = (WindowInsets.Type.mandatorySystemGestures() & insetsType) != 0;
        boolean navigationBars = (WindowInsets.Type.navigationBars() & insetsType) != 0;
        boolean statusBars = (WindowInsets.Type.statusBars() & insetsType) != 0;
        boolean systemGestures = (WindowInsets.Type.systemGestures() & insetsType) != 0;
        boolean tappableElement = (WindowInsets.Type.tappableElement() & insetsType) != 0;
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, getStringBuilder().append("markShowInsets ").append("fromIme ").append(fromIme).append(" systemBars ").append(systemBars).append(" captionBar ").append(captionBar).append(" displayCutout ").append(displayCutout).append(" ime ").append(ime).append(" mandatorySystemGestures ").append(mandatorySystemGestures).append(" navigationBars ").append(navigationBars).append(" statusBars ").append(statusBars).append(" systemGestures ").append(systemGestures).append(" tappableElement ").append(tappableElement).toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markHideInsets(int insetsType, boolean fromIme) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        boolean systemBars = (WindowInsets.Type.systemBars() & insetsType) != 0;
        boolean captionBar = (WindowInsets.Type.captionBar() & insetsType) != 0;
        boolean displayCutout = (WindowInsets.Type.displayCutout() & insetsType) != 0;
        boolean ime = (WindowInsets.Type.ime() & insetsType) != 0;
        boolean mandatorySystemGestures = (WindowInsets.Type.mandatorySystemGestures() & insetsType) != 0;
        boolean navigationBars = (WindowInsets.Type.navigationBars() & insetsType) != 0;
        boolean statusBars = (WindowInsets.Type.statusBars() & insetsType) != 0;
        boolean systemGestures = (WindowInsets.Type.systemGestures() & insetsType) != 0;
        boolean tappableElement = (WindowInsets.Type.tappableElement() & insetsType) != 0;
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, getStringBuilder().append("markHideInsets ").append("fromIme ").append(fromIme).append(" systemBars ").append(systemBars).append(" captionBar ").append(captionBar).append(" displayCutout ").append(displayCutout).append(" ime ").append(ime).append(" mandatorySystemGestures ").append(mandatorySystemGestures).append(" navigationBars ").append(navigationBars).append(" statusBars ").append(statusBars).append(" systemGestures ").append(systemGestures).append(" tappableElement ").append(tappableElement).toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnPerformTraversalsStart(View hostView, boolean first) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        StringBuilder builder = getStringBuilder();
        builder.append("[TRAVERSAL START]").append(" isFirst? ").append(first);
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnPerformTraversalsEnd(View hostView) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        StringBuilder builder = getStringBuilder();
        builder.append("[TRAVERSAL END]");
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markHandleWindowFocusChange(boolean windowFocusChanged, boolean upcomingWindowFocus, boolean added, WindowManager.LayoutParams windowAttributes) {
        if (!DEBUG) {
            return;
        }
        String title = getVRITitle(windowAttributes);
        Log.d(title, "handleWindowFocusChanged mWindowFocusChanged " + windowFocusChanged + " mUpcomingWindowFocus " + upcomingWindowFocus + " mAdded " + added);
        if (!EXTRAINFOENABLE) {
            return;
        }
        StringBuilder builder = getStringBuilder();
        builder.append("[FOCUS] ").append(title).append(" focus ").append(upcomingWindowFocus);
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnDecorMeasure(DecorView decorView, int widthMeasuredSpec, int heightMeasuredSpec) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "DecorView measure  width " + View.MeasureSpec.getSize(widthMeasuredSpec) + " height " + View.MeasureSpec.getSize(heightMeasuredSpec));
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnDecorLayout(DecorView decorView, int left, int top, int right, int bottom) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "DecorView layout " + left + "," + top + "," + right + "," + bottom);
            Trace.traceEnd(8L);
        }
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnDecorSetFrame(DecorView decorView, int l, int t, int r, int b) {
        if (DEBUG && Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "DecorView setFrame " + l + "," + t + "," + r + "," + b);
            Trace.traceEnd(8L);
        }
    }

    private boolean isSystemApp(ApplicationInfo applicationInfo) {
        boolean platformSigned = applicationInfo.isSignedWithPlatformKey();
        boolean preload = applicationInfo.isSystemApp() || applicationInfo.isProduct() || applicationInfo.isPrivilegedApp() || applicationInfo.isVendor() || applicationInfo.isSystemExt();
        return platformSigned || preload;
    }

    private boolean isWhiteListThirdPartyApp(ApplicationInfo applicationInfo) {
        String packageName = applicationInfo.packageName;
        return packageName.equals("com.netease.cloudmusic");
    }

    private StringBuilder getStringBuilder() {
        StringBuilder builder = new StringBuilder();
        String dateTime = LocalDateTime.now().format(this.mDateTimeFormatter);
        builder.append(dateTime);
        builder.append(" ");
        builder.append(this.mMainThreadId);
        builder.append(",");
        builder.append(Thread.currentThread().getId());
        builder.append(" ");
        builder.append(Thread.currentThread().getName());
        builder.append(" ");
        return builder;
    }

    private String getVRITitle(WindowManager.LayoutParams windowAttributes) {
        if (windowAttributes == null || windowAttributes.getTitle() == null) {
            return "VRI[unknown]";
        }
        String[] split = windowAttributes.getTitle().toString().split("\\.");
        if (split.length <= 0) {
            return "VRI[unknown]";
        }
        return "VRI[" + split[split.length - 1] + "]";
    }

    @Override // android.view.debug.IOplusViewDebugManager
    public void markOnConfigChange(ResourcesImpl impl, DisplayMetrics displayMetrics, Configuration configuration) {
        if (!DEBUG || !EXTRAINFOENABLE) {
            return;
        }
        StringBuilder builder = getStringBuilder().append("[CONFIG] change");
        builder.append(impl);
        builder.append(" ");
        builder.append(displayMetrics);
        builder.append(" ");
        builder.append(configuration);
        OplusViewExtraInfoHelper.getInstance().pushInfo(0, builder.toString());
    }
}
