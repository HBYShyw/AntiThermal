package android.view.debug;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.ResourcesImpl;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.DisplayEventReceiver;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.android.internal.policy.DecorView;

/* loaded from: classes.dex */
public interface IOplusViewDebugManager extends IOplusCommonFeature {
    public static final IOplusViewDebugManager mDefault = new IOplusViewDebugManager() { // from class: android.view.debug.IOplusViewDebugManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusViewDebugManager;
    }

    default IOplusViewDebugManager getDefault() {
        return mDefault;
    }

    default boolean isEnabled() {
        return false;
    }

    default void markOnTouchEventMove(int y, int deltaY, int lastMotionY) {
    }

    default void markOnOverScrolled(int deltaY) {
    }

    default void markOnStartScroll() {
    }

    default void markOnFling() {
    }

    default long markOnUpdateStart(long startTime, long currentTime) {
        return currentTime;
    }

    default void markOnUpdateSpline(int splineDuration, float t, long currentTime, int offset) {
    }

    default void markOnUpdateEnd() {
    }

    default void markBeforeScroll(int y, int lastY, int touchMode, int rawDeltaY) {
    }

    default void markAfterScroll() {
    }

    default void markBeforeDispatchTouchEvent(MotionEvent event, String title) {
    }

    default void markAfterDispatchTouchEvent(MotionEvent event) {
    }

    default void markAndDumpWindowFocusChangeMsg(String tag, Handler handler) {
    }

    default void markOnHandleMessageImpl(String msgName) {
    }

    default boolean markOnDispatchTouchEvent(MotionEvent event, View view) {
        return false;
    }

    default void markOnAddView(View child) {
    }

    default void markOnRemoveView(View child) {
    }

    default void markOnRequestLayout() {
    }

    default void markOnInvalidate() {
    }

    default void markOnFocusChange(boolean gainFocus, boolean hasWindowFocus, View view) {
    }

    default void markMeasureStart(View view, int widthMeasureSpec, int heightMeasureSpec) {
    }

    default void markOnInputEvent(InputEvent event) {
    }

    default String markOnVsync(DisplayEventReceiver.VsyncEventData vsyncEventData, long timestampNanos, long lastFrameTimeNanos) {
        return "";
    }

    default String markOnDoframe(DisplayEventReceiver.VsyncEventData vsyncEventData, long timestampNanos, long lastFrameTimeNanos) {
        return "";
    }

    default void markOnBindApplication(ApplicationInfo applicationInfo) {
    }

    default String markPerformLayout(View hostView, WindowManager.LayoutParams windowAttrs) {
        return "";
    }

    default String markPerformMeasure(View hostView, WindowManager.LayoutParams windowAttrs, int childWidthMeasureSpec, int childHeightMeasureSpec) {
        return "";
    }

    default String markPerformDraw(View hostView, WindowManager.LayoutParams windowAttrs) {
        return "";
    }

    default String markScheduleTraversals(View hostView, WindowManager.LayoutParams windowAttrs) {
        return "";
    }

    default void markPerformMeasureReason(String reason) {
    }

    default void markPerformLayoutReason(String reason) {
    }

    default void markOnSetFrame(Rect frame, WindowManager.LayoutParams windowAttrs) {
    }

    default void markHandleAppVisibility(boolean visible, WindowManager.LayoutParams windowAttrs) {
    }

    default void markShowInsets(int insetsType, boolean fromIme) {
    }

    default void markHideInsets(int insetsType, boolean fromIme) {
    }

    default void markOnPerformTraversalsStart(View hostView, boolean first) {
    }

    default void markOnPerformTraversalsEnd(View hostView) {
    }

    default void markHandleWindowFocusChange(boolean windowFocusChanged, boolean upcomingWindowFocus, boolean added, WindowManager.LayoutParams windowAttributes) {
    }

    default void markOnDecorMeasure(DecorView decorView, int widthMeasuredSpec, int heightMeasuredSpec) {
    }

    default void markOnDecorLayout(DecorView decorView, int left, int top, int right, int bottom) {
    }

    default void markOnDecorSetFrame(DecorView decorView, int l, int t, int r, int b) {
    }

    default void markOnConfigChange(ResourcesImpl impl, DisplayMetrics displayMetrics, Configuration configuration) {
    }
}
