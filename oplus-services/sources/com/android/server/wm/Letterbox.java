package com.android.server.wm;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.InputConstants;
import android.view.GestureDetector;
import android.view.InputApplicationHandle;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputWindowHandle;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import com.android.server.UiThread;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public class Letterbox {
    private static final Rect EMPTY_RECT = new Rect();
    private static final Point ZERO_POINT = new Point(0, 0);
    private final Supplier<Boolean> mAreCornersRounded;
    private final Supplier<Integer> mBlurRadiusSupplier;
    private final LetterboxSurface mBottom;
    private final Supplier<Color> mColorSupplier;
    private final Supplier<Float> mDarkScrimAlphaSupplier;
    private final IntConsumer mDoubleTapCallbackX;
    private final IntConsumer mDoubleTapCallbackY;
    private final LetterboxSurface mFullWindowSurface;
    private final Supplier<Boolean> mHasWallpaperBackgroundSupplier;
    private final LetterboxSurface mLeft;
    private final Supplier<SurfaceControl> mParentSurfaceSupplier;
    private final LetterboxSurface mRight;
    private final Supplier<SurfaceControl.Builder> mSurfaceControlFactory;
    private final LetterboxSurface[] mSurfaces;
    private final LetterboxSurface mTop;
    private final Supplier<SurfaceControl.Transaction> mTransactionFactory;
    private final Rect mOuter = new Rect();
    private final Rect mInner = new Rect();

    public Letterbox(Supplier<SurfaceControl.Builder> supplier, Supplier<SurfaceControl.Transaction> supplier2, Supplier<Boolean> supplier3, Supplier<Color> supplier4, Supplier<Boolean> supplier5, Supplier<Integer> supplier6, Supplier<Float> supplier7, IntConsumer intConsumer, IntConsumer intConsumer2, Supplier<SurfaceControl> supplier8) {
        LetterboxSurface letterboxSurface = new LetterboxSurface("top");
        this.mTop = letterboxSurface;
        LetterboxSurface letterboxSurface2 = new LetterboxSurface("left");
        this.mLeft = letterboxSurface2;
        LetterboxSurface letterboxSurface3 = new LetterboxSurface("bottom");
        this.mBottom = letterboxSurface3;
        LetterboxSurface letterboxSurface4 = new LetterboxSurface("right");
        this.mRight = letterboxSurface4;
        this.mFullWindowSurface = new LetterboxSurface("fullWindow");
        this.mSurfaces = new LetterboxSurface[]{letterboxSurface2, letterboxSurface, letterboxSurface4, letterboxSurface3};
        this.mSurfaceControlFactory = supplier;
        this.mTransactionFactory = supplier2;
        this.mAreCornersRounded = supplier3;
        this.mColorSupplier = supplier4;
        this.mHasWallpaperBackgroundSupplier = supplier5;
        this.mBlurRadiusSupplier = supplier6;
        this.mDarkScrimAlphaSupplier = supplier7;
        this.mDoubleTapCallbackX = intConsumer;
        this.mDoubleTapCallbackY = intConsumer2;
        this.mParentSurfaceSupplier = supplier8;
    }

    public void layout(Rect rect, Rect rect2, Point point) {
        this.mOuter.set(rect);
        this.mInner.set(rect2);
        this.mTop.layout(rect.left, rect.top, rect.right, rect2.top, point);
        this.mLeft.layout(rect.left, rect.top, rect2.left, rect.bottom, point);
        this.mBottom.layout(rect.left, rect2.bottom, rect.right, rect.bottom, point);
        this.mRight.layout(rect2.right, rect.top, rect.right, rect.bottom, point);
        this.mFullWindowSurface.layout(rect.left, rect.top, rect.right, rect.bottom, point);
    }

    public Rect getInsets() {
        return new Rect(this.mLeft.getWidth(), this.mTop.getHeight(), this.mRight.getWidth(), this.mBottom.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getInnerFrame() {
        return this.mInner;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getOuterFrame() {
        return this.mOuter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean notIntersectsOrFullyContains(Rect rect) {
        int i = 0;
        int i2 = 0;
        for (LetterboxSurface letterboxSurface : this.mSurfaces) {
            Rect rect2 = letterboxSurface.mLayoutFrameGlobal;
            if (rect2.isEmpty()) {
                i++;
            } else if (!Rect.intersects(rect2, rect)) {
                i2++;
            } else if (rect2.contains(rect)) {
                return true;
            }
        }
        return i + i2 == this.mSurfaces.length;
    }

    public void hide() {
        Rect rect = EMPTY_RECT;
        layout(rect, rect, ZERO_POINT);
    }

    public void destroy() {
        this.mOuter.setEmpty();
        this.mInner.setEmpty();
        for (LetterboxSurface letterboxSurface : this.mSurfaces) {
            letterboxSurface.remove();
        }
        this.mFullWindowSurface.remove();
    }

    public boolean needsApplySurfaceChanges() {
        if (useFullWindowSurface()) {
            return this.mFullWindowSurface.needsApplySurfaceChanges();
        }
        for (LetterboxSurface letterboxSurface : this.mSurfaces) {
            if (letterboxSurface.needsApplySurfaceChanges()) {
                return true;
            }
        }
        return false;
    }

    public void applySurfaceChanges(SurfaceControl.Transaction transaction) {
        int i = 0;
        if (useFullWindowSurface()) {
            this.mFullWindowSurface.applySurfaceChanges(transaction);
            LetterboxSurface[] letterboxSurfaceArr = this.mSurfaces;
            int length = letterboxSurfaceArr.length;
            while (i < length) {
                letterboxSurfaceArr[i].remove();
                i++;
            }
            return;
        }
        LetterboxSurface[] letterboxSurfaceArr2 = this.mSurfaces;
        int length2 = letterboxSurfaceArr2.length;
        while (i < length2) {
            letterboxSurfaceArr2[i].applySurfaceChanges(transaction);
            i++;
        }
        this.mFullWindowSurface.remove();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attachInput(WindowState windowState) {
        if (useFullWindowSurface()) {
            this.mFullWindowSurface.attachInput(windowState);
            return;
        }
        for (LetterboxSurface letterboxSurface : this.mSurfaces) {
            letterboxSurface.attachInput(windowState);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMovedToDisplay(int i) {
        for (LetterboxSurface letterboxSurface : this.mSurfaces) {
            if (letterboxSurface.mInputInterceptor != null) {
                letterboxSurface.mInputInterceptor.mWindowHandle.displayId = i;
            }
        }
        if (this.mFullWindowSurface.mInputInterceptor != null) {
            this.mFullWindowSurface.mInputInterceptor.mWindowHandle.displayId = i;
        }
    }

    private boolean useFullWindowSurface() {
        return this.mAreCornersRounded.get().booleanValue() || this.mHasWallpaperBackgroundSupplier.get().booleanValue();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private final class TapEventReceiver extends InputEventReceiver {
        private final GestureDetector mDoubleTapDetector;
        private final DoubleTapListener mDoubleTapListener;

        TapEventReceiver(InputChannel inputChannel, Context context) {
            super(inputChannel, UiThread.getHandler().getLooper());
            DoubleTapListener doubleTapListener = new DoubleTapListener();
            this.mDoubleTapListener = doubleTapListener;
            this.mDoubleTapDetector = new GestureDetector(context, doubleTapListener, UiThread.getHandler());
        }

        public void onInputEvent(InputEvent inputEvent) {
            finishInputEvent(inputEvent, this.mDoubleTapDetector.onTouchEvent((MotionEvent) inputEvent));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        private DoubleTapListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() != 1) {
                return false;
            }
            Letterbox.this.mDoubleTapCallbackX.accept((int) motionEvent.getRawX());
            Letterbox.this.mDoubleTapCallbackY.accept((int) motionEvent.getRawY());
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public final class InputInterceptor {
        private final InputChannel mClientChannel;
        private final InputEventReceiver mInputEventReceiver;
        private final IBinder mToken;
        private final InputWindowHandle mWindowHandle;
        private final WindowManagerService mWmService;

        InputInterceptor(String str, WindowState windowState) {
            WindowManagerService windowManagerService = windowState.mWmService;
            this.mWmService = windowManagerService;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            Object obj = windowState.mActivityRecord;
            sb.append(obj == null ? windowState : obj);
            String sb2 = sb.toString();
            InputChannel createInputChannel = windowManagerService.mInputManager.createInputChannel(sb2);
            this.mClientChannel = createInputChannel;
            this.mInputEventReceiver = new TapEventReceiver(createInputChannel, windowManagerService.mContext);
            IBinder token = createInputChannel.getToken();
            this.mToken = token;
            InputWindowHandle inputWindowHandle = new InputWindowHandle((InputApplicationHandle) null, windowState.getDisplayId());
            this.mWindowHandle = inputWindowHandle;
            inputWindowHandle.name = sb2;
            inputWindowHandle.token = token;
            inputWindowHandle.layoutParamsType = 2022;
            inputWindowHandle.dispatchingTimeoutMillis = InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
            inputWindowHandle.ownerPid = WindowManagerService.MY_PID;
            inputWindowHandle.ownerUid = WindowManagerService.MY_UID;
            inputWindowHandle.scaleFactor = 1.0f;
            inputWindowHandle.inputConfig = 1028;
        }

        void updateTouchableRegion(Rect rect) {
            if (rect.isEmpty()) {
                this.mWindowHandle.token = null;
                return;
            }
            InputWindowHandle inputWindowHandle = this.mWindowHandle;
            inputWindowHandle.token = this.mToken;
            inputWindowHandle.touchableRegion.set(rect);
            this.mWindowHandle.touchableRegion.translate(-rect.left, -rect.top);
        }

        void dispose() {
            this.mWmService.mInputManager.removeInputChannel(this.mToken);
            this.mInputEventReceiver.dispose();
            this.mClientChannel.dispose();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
    public class LetterboxSurface {
        private Color mColor;
        private boolean mHasWallpaperBackground;
        private InputInterceptor mInputInterceptor;
        private SurfaceControl mParentSurface;
        private SurfaceControl mSurface;
        private final String mType;
        private final Rect mSurfaceFrameRelative = new Rect();
        private final Rect mLayoutFrameGlobal = new Rect();
        private final Rect mLayoutFrameRelative = new Rect();

        public LetterboxSurface(String str) {
            this.mType = str;
        }

        public void layout(int i, int i2, int i3, int i4, Point point) {
            this.mLayoutFrameGlobal.set(i, i2, i3, i4);
            this.mLayoutFrameRelative.set(this.mLayoutFrameGlobal);
            this.mLayoutFrameRelative.offset(-point.x, -point.y);
        }

        private void createSurface(SurfaceControl.Transaction transaction) {
            SurfaceControl build = ((SurfaceControl.Builder) Letterbox.this.mSurfaceControlFactory.get()).setName("Letterbox - " + this.mType).setFlags(4).setColorLayer().setCallsite("LetterboxSurface.createSurface").build();
            this.mSurface = build;
            transaction.setLayer(build, -20000).setColorSpaceAgnostic(this.mSurface, true);
        }

        void attachInput(WindowState windowState) {
            InputInterceptor inputInterceptor = this.mInputInterceptor;
            if (inputInterceptor != null) {
                inputInterceptor.dispose();
            }
            this.mInputInterceptor = new InputInterceptor("Letterbox_" + this.mType + "_", windowState);
        }

        boolean isRemoved() {
            return (this.mSurface == null && this.mInputInterceptor == null) ? false : true;
        }

        public void remove() {
            if (this.mSurface != null) {
                ((SurfaceControl.Transaction) Letterbox.this.mTransactionFactory.get()).remove(this.mSurface).apply();
                this.mSurface = null;
            }
            InputInterceptor inputInterceptor = this.mInputInterceptor;
            if (inputInterceptor != null) {
                inputInterceptor.dispose();
                this.mInputInterceptor = null;
            }
        }

        public int getWidth() {
            return Math.max(0, this.mLayoutFrameGlobal.width());
        }

        public int getHeight() {
            return Math.max(0, this.mLayoutFrameGlobal.height());
        }

        public void applySurfaceChanges(SurfaceControl.Transaction transaction) {
            InputInterceptor inputInterceptor;
            if (needsApplySurfaceChanges()) {
                this.mSurfaceFrameRelative.set(this.mLayoutFrameRelative);
                if (!this.mSurfaceFrameRelative.isEmpty()) {
                    if (this.mSurface == null) {
                        createSurface(transaction);
                    }
                    this.mColor = (Color) Letterbox.this.mColorSupplier.get();
                    this.mParentSurface = (SurfaceControl) Letterbox.this.mParentSurfaceSupplier.get();
                    transaction.setColor(this.mSurface, getRgbColorArray());
                    SurfaceControl surfaceControl = this.mSurface;
                    Rect rect = this.mSurfaceFrameRelative;
                    transaction.setPosition(surfaceControl, rect.left, rect.top);
                    transaction.setWindowCrop(this.mSurface, this.mSurfaceFrameRelative.width(), this.mSurfaceFrameRelative.height());
                    transaction.reparent(this.mSurface, this.mParentSurface);
                    this.mHasWallpaperBackground = ((Boolean) Letterbox.this.mHasWallpaperBackgroundSupplier.get()).booleanValue();
                    updateAlphaAndBlur(transaction);
                    transaction.show(this.mSurface);
                } else {
                    SurfaceControl surfaceControl2 = this.mSurface;
                    if (surfaceControl2 != null) {
                        transaction.hide(surfaceControl2);
                    }
                }
                if (this.mSurface == null || (inputInterceptor = this.mInputInterceptor) == null) {
                    return;
                }
                inputInterceptor.updateTouchableRegion(this.mSurfaceFrameRelative);
                transaction.setInputWindowInfo(this.mSurface, this.mInputInterceptor.mWindowHandle);
            }
        }

        private void updateAlphaAndBlur(SurfaceControl.Transaction transaction) {
            if (!this.mHasWallpaperBackground) {
                transaction.setAlpha(this.mSurface, 1.0f);
                transaction.setBackgroundBlurRadius(this.mSurface, 0);
                return;
            }
            transaction.setAlpha(this.mSurface, ((Float) Letterbox.this.mDarkScrimAlphaSupplier.get()).floatValue());
            if (((Integer) Letterbox.this.mBlurRadiusSupplier.get()).intValue() <= 0) {
                transaction.setBackgroundBlurRadius(this.mSurface, 0);
            } else {
                transaction.setBackgroundBlurRadius(this.mSurface, ((Integer) Letterbox.this.mBlurRadiusSupplier.get()).intValue());
            }
        }

        private float[] getRgbColorArray() {
            return new float[]{this.mColor.red(), this.mColor.green(), this.mColor.blue()};
        }

        public boolean needsApplySurfaceChanges() {
            return (this.mSurfaceFrameRelative.equals(this.mLayoutFrameRelative) && (this.mSurfaceFrameRelative.isEmpty() || (((Boolean) Letterbox.this.mHasWallpaperBackgroundSupplier.get()).booleanValue() == this.mHasWallpaperBackground && ((Color) Letterbox.this.mColorSupplier.get()).equals(this.mColor) && Letterbox.this.mParentSurfaceSupplier.get() == this.mParentSurface))) ? false : true;
        }
    }
}
