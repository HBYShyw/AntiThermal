package com.android.server.display;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.FloatProperty;
import android.util.Pair;
import android.util.Slog;
import android.view.Choreographer;
import android.view.Display;
import com.android.internal.display.BrightnessSynchronizer;
import java.io.PrintWriter;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DisplayPowerState {
    private final String TAG;
    private final DisplayBlanker mBlanker;
    private Runnable mCleanListener;
    private final ColorFade mColorFade;
    private boolean mColorFadeDrawPending;
    private float mColorFadeLevel;
    private boolean mColorFadePrepared;
    private boolean mColorFadeReady;
    private final int mDisplayId;
    private IOplusDisplayPowerControllerExt mDpcExt;
    private final PhotonicModulator mPhotonicModulator;
    private float mScreenBrightness;
    private boolean mScreenReady;
    private int mScreenState;
    private boolean mScreenUpdatePending;
    private float mSdrScreenBrightness;
    private volatile boolean mStopped;
    private static final boolean PANIC_DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static boolean DEBUG = SystemProperties.getBoolean("dbg.dms.dps", false);
    private static String COUNTER_COLOR_FADE = "ColorFadeLevel";
    public static final boolean VRR_BRIGHTNESS_RM = SystemProperties.getBoolean("ro.oplus.display.vrr.brightness.rm", false);
    public static final FloatProperty<DisplayPowerState> COLOR_FADE_LEVEL = new FloatProperty<DisplayPowerState>("electronBeamLevel") { // from class: com.android.server.display.DisplayPowerState.1
        @Override // android.util.FloatProperty
        public void setValue(DisplayPowerState displayPowerState, float f) {
            displayPowerState.setColorFadeLevel(f);
        }

        @Override // android.util.Property
        public Float get(DisplayPowerState displayPowerState) {
            return Float.valueOf(displayPowerState.getColorFadeLevel());
        }
    };
    public static final FloatProperty<DisplayPowerState> SCREEN_BRIGHTNESS_FLOAT = new FloatProperty<DisplayPowerState>("screenBrightnessFloat") { // from class: com.android.server.display.DisplayPowerState.2
        @Override // android.util.FloatProperty
        public void setValue(DisplayPowerState displayPowerState, float f) {
            displayPowerState.setScreenBrightness(f);
        }

        @Override // android.util.Property
        public Float get(DisplayPowerState displayPowerState) {
            return Float.valueOf(displayPowerState.getScreenBrightness());
        }
    };
    public static final FloatProperty<DisplayPowerState> SCREEN_SDR_BRIGHTNESS_FLOAT = new FloatProperty<DisplayPowerState>("sdrScreenBrightnessFloat") { // from class: com.android.server.display.DisplayPowerState.3
        @Override // android.util.FloatProperty
        public void setValue(DisplayPowerState displayPowerState, float f) {
            displayPowerState.setSdrScreenBrightness(f);
        }

        @Override // android.util.Property
        public Float get(DisplayPowerState displayPowerState) {
            return Float.valueOf(displayPowerState.getSdrScreenBrightness());
        }
    };
    private final Runnable mScreenUpdateRunnable = new Runnable() { // from class: com.android.server.display.DisplayPowerState.4
        @Override // java.lang.Runnable
        public void run() {
            DisplayPowerState.this.mScreenUpdatePending = false;
            float f = -1.0f;
            float f2 = (DisplayPowerState.this.mScreenState == 1 || DisplayPowerState.this.mColorFadeLevel <= 0.0f) ? -1.0f : DisplayPowerState.this.mScreenBrightness;
            if (DisplayPowerState.this.mScreenState != 1 && DisplayPowerState.this.mColorFadeLevel > 0.0f) {
                f = DisplayPowerState.this.mSdrScreenBrightness;
            }
            Pair<Float, Float> screenUpdateExt = DisplayPowerState.this.mDpsWrapper.getExtImpl().screenUpdateExt(DisplayPowerState.this.mScreenState, f2, f, DisplayPowerState.this.mScreenBrightness, DisplayPowerState.this.mColorFadeLevel, DisplayPowerState.this.mDisplayId);
            float floatValue = ((Float) screenUpdateExt.first).floatValue();
            float floatValue2 = ((Float) screenUpdateExt.second).floatValue();
            if (DisplayPowerState.this.mColorFadeLevel == 0.0f) {
                Slog.d(DisplayPowerState.this.TAG, "updateRunnable run fadeLevle=" + DisplayPowerState.this.mColorFadeLevel + " state=" + Display.stateToString(DisplayPowerState.this.mScreenState) + " brightness changed:" + DisplayPowerState.this.mScreenBrightness + "->" + floatValue);
            }
            if (DisplayPowerState.this.mPhotonicModulator.setState(DisplayPowerState.this.mScreenState, floatValue, floatValue2)) {
                if (DisplayPowerState.DEBUG) {
                    Slog.d(DisplayPowerState.this.TAG, "Screen ready");
                }
                DisplayPowerState.this.mScreenReady = true;
                DisplayPowerState.this.invokeCleanListenerIfNeeded();
                return;
            }
            if (DisplayPowerState.DEBUG) {
                Slog.d(DisplayPowerState.this.TAG, "Screen not ready");
            }
        }
    };
    private final Runnable mColorFadeDrawRunnable = new Runnable() { // from class: com.android.server.display.DisplayPowerState.5
        @Override // java.lang.Runnable
        public void run() {
            DisplayPowerState.this.mColorFadeDrawPending = false;
            if (DisplayPowerState.this.mColorFadePrepared) {
                DisplayPowerState.this.mColorFade.draw(DisplayPowerState.this.mColorFadeLevel);
                Trace.traceCounter(131072L, DisplayPowerState.COUNTER_COLOR_FADE, Math.round(DisplayPowerState.this.mColorFadeLevel * 100.0f));
            }
            DisplayPowerState.this.mColorFadeReady = true;
            DisplayPowerState.this.invokeCleanListenerIfNeeded();
        }
    };
    private DisplayPowerStateWrapper mDpsWrapper = new DisplayPowerStateWrapper();
    private final Handler mHandler = new Handler(true);
    private final Choreographer mChoreographer = Choreographer.getInstance();

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayPowerState(DisplayBlanker displayBlanker, ColorFade colorFade, int i, int i2, IOplusDisplayPowerControllerExt iOplusDisplayPowerControllerExt) {
        this.mDpcExt = null;
        this.mBlanker = displayBlanker;
        this.mColorFade = colorFade;
        PhotonicModulator photonicModulator = new PhotonicModulator();
        this.mPhotonicModulator = photonicModulator;
        photonicModulator.start();
        this.mDisplayId = i;
        this.mScreenState = i2;
        this.mScreenBrightness = this.mDpsWrapper.getExtImpl().getBootupBrightness();
        this.mDpcExt = iOplusDisplayPowerControllerExt;
        this.TAG = "DisplayPowerState[" + i + "]";
        this.mSdrScreenBrightness = this.mScreenBrightness;
        scheduleScreenUpdate();
        this.mColorFadePrepared = false;
        this.mColorFadeLevel = 1.0f;
        this.mColorFadeReady = true;
    }

    public void setScreenState(int i) {
        if (this.mScreenState != i) {
            if (DEBUG || PANIC_DEBUG) {
                Slog.d(this.TAG, "setScreenState changed:" + Display.stateToString(this.mScreenState) + "->" + Display.stateToString(i));
            }
            if (i == 2 || this.mScreenState == 2) {
                Slog.d(this.TAG, "setScreenState: state=" + i);
            }
            this.mScreenState = i;
            this.mScreenReady = false;
            scheduleScreenUpdate();
        }
    }

    public int getScreenState() {
        return this.mScreenState;
    }

    public void setSdrScreenBrightness(float f) {
        if (BrightnessSynchronizer.floatEquals(this.mSdrScreenBrightness, f)) {
            return;
        }
        if (DEBUG) {
            Slog.d(this.TAG, "setSdrScreenBrightness: brightness=" + f);
        }
        this.mSdrScreenBrightness = f;
        if (this.mScreenState != 1) {
            this.mScreenReady = false;
            scheduleScreenUpdate();
        }
    }

    public float getSdrScreenBrightness() {
        return this.mSdrScreenBrightness;
    }

    public void setScreenBrightness(float f) {
        if (BrightnessSynchronizer.floatEquals(this.mScreenBrightness, f)) {
            return;
        }
        if (DEBUG) {
            Slog.d(this.TAG, "setScreenBrightness: brightness=" + f);
        }
        this.mScreenBrightness = f;
        if (this.mScreenState != 1) {
            this.mScreenReady = false;
            scheduleScreenUpdate();
        }
    }

    public float getScreenBrightness() {
        return this.mScreenBrightness;
    }

    public boolean prepareColorFade(Context context, int i) {
        ColorFade colorFade = this.mColorFade;
        if (colorFade == null || !colorFade.prepare(context, i)) {
            this.mColorFadePrepared = false;
            this.mColorFadeReady = true;
            return false;
        }
        this.mColorFadePrepared = true;
        this.mColorFadeReady = false;
        scheduleColorFadeDraw();
        return true;
    }

    public void dismissColorFade() {
        Trace.traceCounter(131072L, COUNTER_COLOR_FADE, 100);
        ColorFade colorFade = this.mColorFade;
        if (colorFade != null) {
            colorFade.dismiss();
        }
        this.mColorFadePrepared = false;
        this.mColorFadeReady = true;
    }

    public void dismissColorFadeResources() {
        ColorFade colorFade = this.mColorFade;
        if (colorFade != null) {
            colorFade.dismissResources();
        }
    }

    public void setColorFadeLevel(float f) {
        if (this.mColorFadeLevel != f) {
            Slog.d(this.TAG, "setColorFadeLevel: level=" + f);
            this.mColorFadeLevel = f;
            if (this.mScreenState != 1) {
                this.mScreenReady = false;
                scheduleScreenUpdate();
            }
            if (this.mColorFadePrepared) {
                this.mColorFadeReady = false;
                scheduleColorFadeDraw();
            }
        }
        this.mDpsWrapper.getExtImpl().setColorFadeLevel(f);
    }

    public float getColorFadeLevel() {
        return this.mColorFadeLevel;
    }

    public boolean waitUntilClean(Runnable runnable) {
        if (!this.mScreenReady || !this.mColorFadeReady) {
            this.mCleanListener = runnable;
            return false;
        }
        this.mCleanListener = null;
        return true;
    }

    public void stop() {
        this.mStopped = true;
        this.mPhotonicModulator.interrupt();
        dismissColorFade();
        this.mCleanListener = null;
        this.mHandler.removeCallbacksAndMessages(null);
        this.mDpcExt.dismissEglContext(this.mColorFade, this.mDisplayId);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("Display Power State:");
        printWriter.println("  mDisplayId=" + this.mDisplayId);
        printWriter.println("  mStopped=" + this.mStopped);
        printWriter.println("  mScreenState=" + Display.stateToString(this.mScreenState));
        printWriter.println("  mScreenBrightness=" + this.mScreenBrightness);
        printWriter.println("  mSdrScreenBrightness=" + this.mSdrScreenBrightness);
        printWriter.println("  mScreenReady=" + this.mScreenReady);
        printWriter.println("  mScreenUpdatePending=" + this.mScreenUpdatePending);
        printWriter.println("  mColorFadePrepared=" + this.mColorFadePrepared);
        printWriter.println("  mColorFadeLevel=" + this.mColorFadeLevel);
        printWriter.println("  mColorFadeReady=" + this.mColorFadeReady);
        printWriter.println("  mColorFadeDrawPending=" + this.mColorFadeDrawPending);
        this.mPhotonicModulator.dump(printWriter);
        ColorFade colorFade = this.mColorFade;
        if (colorFade != null) {
            colorFade.dump(printWriter);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetScreenState() {
        this.mScreenState = 0;
        this.mScreenReady = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleScreenUpdate() {
        if (this.mScreenUpdatePending) {
            return;
        }
        this.mScreenUpdatePending = true;
        postScreenUpdateThreadSafe();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postScreenUpdateThreadSafe() {
        this.mHandler.removeCallbacks(this.mScreenUpdateRunnable);
        this.mHandler.post(this.mScreenUpdateRunnable);
    }

    private void scheduleColorFadeDraw() {
        if (this.mColorFadeDrawPending) {
            return;
        }
        this.mColorFadeDrawPending = true;
        this.mChoreographer.postCallback(3, this.mColorFadeDrawRunnable, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invokeCleanListenerIfNeeded() {
        Runnable runnable = this.mCleanListener;
        if (runnable != null && this.mScreenReady && this.mColorFadeReady) {
            this.mCleanListener = null;
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class PhotonicModulator extends Thread {
        private static final float INITIAL_BACKLIGHT_FLOAT = Float.NaN;
        private static final int INITIAL_SCREEN_STATE = 0;
        private float mActualBacklight;
        private float mActualSdrBacklight;
        private int mActualState;
        private boolean mBacklightChangeInProgress;
        private final Object mLock;
        private float mPendingBacklight;
        private float mPendingSdrBacklight;
        private int mPendingState;
        private boolean mStateChangeInProgress;

        public PhotonicModulator() {
            super("PhotonicModulator");
            this.mLock = new Object();
            this.mPendingState = 0;
            this.mPendingBacklight = Float.NaN;
            this.mPendingSdrBacklight = Float.NaN;
            this.mActualState = 0;
            this.mActualBacklight = Float.NaN;
            this.mActualSdrBacklight = Float.NaN;
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0084  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x002a A[Catch: all -> 0x0087, TryCatch #0 {, blocks: (B:4:0x0003, B:7:0x000c, B:9:0x0014, B:15:0x007f, B:18:0x0085, B:23:0x0024, B:25:0x002a, B:26:0x0050, B:28:0x005a, B:36:0x006a, B:38:0x006e, B:42:0x0076, B:44:0x007a), top: B:3:0x0003 }] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0064 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:38:0x006e A[Catch: all -> 0x0087, TryCatch #0 {, blocks: (B:4:0x0003, B:7:0x000c, B:9:0x0014, B:15:0x007f, B:18:0x0085, B:23:0x0024, B:25:0x002a, B:26:0x0050, B:28:0x005a, B:36:0x006a, B:38:0x006e, B:42:0x0076, B:44:0x007a), top: B:3:0x0003 }] */
        /* JADX WARN: Removed duplicated region for block: B:44:0x007a A[Catch: all -> 0x0087, TryCatch #0 {, blocks: (B:4:0x0003, B:7:0x000c, B:9:0x0014, B:15:0x007f, B:18:0x0085, B:23:0x0024, B:25:0x002a, B:26:0x0050, B:28:0x005a, B:36:0x006a, B:38:0x006e, B:42:0x0076, B:44:0x007a), top: B:3:0x0003 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean setState(int i, float f, float f2) {
            boolean z;
            boolean z2;
            boolean z3;
            boolean z4;
            boolean z5;
            boolean z6;
            synchronized (this.mLock) {
                z = true;
                boolean z7 = i != this.mPendingState;
                if (BrightnessSynchronizer.floatEquals(f, this.mPendingBacklight) && BrightnessSynchronizer.floatEquals(f2, this.mPendingSdrBacklight)) {
                    z2 = false;
                    if (!z7 || z2) {
                        if (DisplayPowerState.DEBUG) {
                            Slog.d(DisplayPowerState.this.TAG, "Requesting new screen state: state=" + Display.stateToString(i) + ", backlight=" + f);
                        }
                        this.mPendingState = i;
                        this.mPendingBacklight = f;
                        this.mPendingSdrBacklight = f2;
                        z3 = this.mStateChangeInProgress;
                        if (!z3 && !this.mBacklightChangeInProgress) {
                            z4 = false;
                            if (!z7 && !z3) {
                                z5 = false;
                                this.mStateChangeInProgress = z5;
                                if (!z2 && !this.mBacklightChangeInProgress) {
                                    z6 = false;
                                    this.mBacklightChangeInProgress = z6;
                                    if (!z4) {
                                        this.mLock.notifyAll();
                                    }
                                }
                                z6 = true;
                                this.mBacklightChangeInProgress = z6;
                                if (!z4) {
                                }
                            }
                            z5 = true;
                            this.mStateChangeInProgress = z5;
                            if (!z2) {
                                z6 = false;
                                this.mBacklightChangeInProgress = z6;
                                if (!z4) {
                                }
                            }
                            z6 = true;
                            this.mBacklightChangeInProgress = z6;
                            if (!z4) {
                            }
                        }
                        z4 = true;
                        if (!z7) {
                            z5 = false;
                            this.mStateChangeInProgress = z5;
                            if (!z2) {
                            }
                            z6 = true;
                            this.mBacklightChangeInProgress = z6;
                            if (!z4) {
                            }
                        }
                        z5 = true;
                        this.mStateChangeInProgress = z5;
                        if (!z2) {
                        }
                        z6 = true;
                        this.mBacklightChangeInProgress = z6;
                        if (!z4) {
                        }
                    }
                    if (this.mStateChangeInProgress) {
                        z = false;
                    }
                }
                z2 = true;
                if (!z7) {
                }
                if (DisplayPowerState.DEBUG) {
                }
                this.mPendingState = i;
                this.mPendingBacklight = f;
                this.mPendingSdrBacklight = f2;
                z3 = this.mStateChangeInProgress;
                if (!z3) {
                    z4 = false;
                    if (!z7) {
                    }
                    z5 = true;
                    this.mStateChangeInProgress = z5;
                    if (!z2) {
                    }
                    z6 = true;
                    this.mBacklightChangeInProgress = z6;
                    if (!z4) {
                    }
                    if (this.mStateChangeInProgress) {
                    }
                }
                z4 = true;
                if (!z7) {
                }
                z5 = true;
                this.mStateChangeInProgress = z5;
                if (!z2) {
                }
                z6 = true;
                this.mBacklightChangeInProgress = z6;
                if (!z4) {
                }
                if (this.mStateChangeInProgress) {
                }
            }
            return z;
        }

        public void dump(PrintWriter printWriter) {
            synchronized (this.mLock) {
                printWriter.println();
                printWriter.println("Photonic Modulator State:");
                printWriter.println("  mDisplayId=" + DisplayPowerState.this.mDisplayId);
                printWriter.println("  mPendingState=" + Display.stateToString(this.mPendingState));
                printWriter.println("  mPendingBacklight=" + this.mPendingBacklight);
                printWriter.println("  mPendingSdrBacklight=" + this.mPendingSdrBacklight);
                printWriter.println("  mActualState=" + Display.stateToString(this.mActualState));
                printWriter.println("  mActualBacklight=" + this.mActualBacklight);
                printWriter.println("  mActualSdrBacklight=" + this.mActualSdrBacklight);
                printWriter.println("  mStateChangeInProgress=" + this.mStateChangeInProgress);
                printWriter.println("  mBacklightChangeInProgress=" + this.mBacklightChangeInProgress);
            }
        }

        /* JADX WARN: Can't wrap try/catch for region: R(18:4|5|(1:7)(1:61)|8|(12:13|(1:15)|(1:17)|(1:59)(1:21)|(1:25)|58|30|31|32|33|34|35)|60|(0)|(0)|(1:19)|59|(1:25)|58|30|31|32|33|34|35) */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0066, code lost:
        
            if (r3 != false) goto L61;
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x00e6, code lost:
        
            if (r9.this$0.mStopped != false) goto L60;
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x00e9, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x0069, code lost:
        
            r9.mActualState = r1;
            r9.mActualBacklight = r5;
            r9.mActualSdrBacklight = r6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x0074, code lost:
        
            if (com.android.server.display.DisplayPowerState.DEBUG != false) goto L37;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x0076, code lost:
        
            if (r2 == false) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x00b4, code lost:
        
            r9.this$0.mBlanker.requestDisplayState(r9.this$0.mDisplayId, r1, r5, r6);
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x00c9, code lost:
        
            if (r9.this$0.mDpcExt == null) goto L64;
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x00cb, code lost:
        
            r9.this$0.mDpcExt.notifyBrightnessChange(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:57:0x0078, code lost:
        
            android.util.Slog.d(r9.this$0.TAG, "Updating screen state: id=" + r9.this$0.mDisplayId + ", state=" + android.view.Display.stateToString(r1) + ", backlight=" + r5 + ", sdrBacklight=" + r6);
         */
        /* JADX WARN: Removed duplicated region for block: B:15:0x0048 A[Catch: all -> 0x00ed, TryCatch #1 {, blocks: (B:5:0x0023, B:8:0x002e, B:10:0x003a, B:15:0x0048, B:17:0x0051, B:19:0x0055, B:46:0x0069, B:47:0x006f, B:30:0x00d6, B:32:0x00da, B:33:0x00ea, B:38:0x00e0, B:41:0x00e8), top: B:4:0x0023, inners: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0051 A[Catch: all -> 0x00ed, TryCatch #1 {, blocks: (B:5:0x0023, B:8:0x002e, B:10:0x003a, B:15:0x0048, B:17:0x0051, B:19:0x0055, B:46:0x0069, B:47:0x006f, B:30:0x00d6, B:32:0x00da, B:33:0x00ea, B:38:0x00e0, B:41:0x00e8), top: B:4:0x0023, inners: #0 }] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            boolean z;
            DisplayPowerState.this.mDpsWrapper.getExtImpl().setDisplayThreadSched(Process.myTid(), -10);
            DisplayPowerState.this.mDpsWrapper.getExtImpl().setUxThread();
            while (true) {
                synchronized (this.mLock) {
                    int i = this.mPendingState;
                    boolean z2 = true;
                    boolean z3 = i != this.mActualState;
                    float f = this.mPendingBacklight;
                    float f2 = this.mPendingSdrBacklight;
                    if (BrightnessSynchronizer.floatEquals(f, this.mActualBacklight) && BrightnessSynchronizer.floatEquals(f2, this.mActualSdrBacklight)) {
                        z = false;
                        if (!z3) {
                            DisplayPowerState.this.postScreenUpdateThreadSafe();
                            this.mStateChangeInProgress = false;
                        }
                        if (!z) {
                            this.mBacklightChangeInProgress = false;
                        }
                        boolean z4 = i == 0 && !Float.isNaN(f);
                        if (!z3 && !z) {
                            z2 = false;
                        }
                        this.mStateChangeInProgress = false;
                        this.mBacklightChangeInProgress = false;
                        this.mLock.wait();
                    }
                    z = true;
                    if (!z3) {
                    }
                    if (!z) {
                    }
                    if (i == 0) {
                    }
                    if (!z3) {
                        z2 = false;
                    }
                    this.mStateChangeInProgress = false;
                    this.mBacklightChangeInProgress = false;
                    this.mLock.wait();
                }
            }
        }
    }

    public IDisplayPowerStateWrapper getWrapper() {
        return this.mDpsWrapper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DisplayPowerStateWrapper implements IDisplayPowerStateWrapper {
        private IDisplayPowerStateExt mDpsExt;

        private DisplayPowerStateWrapper() {
            this.mDpsExt = (IDisplayPowerStateExt) ExtLoader.type(IDisplayPowerStateExt.class).base(DisplayPowerState.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IDisplayPowerStateExt getExtImpl() {
            return this.mDpsExt;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public boolean getDebug() {
            return DisplayPowerState.DEBUG;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public void setLoggingEnabled(boolean z) {
            DisplayPowerState.DEBUG = z;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public boolean getColorFadePrepared() {
            return DisplayPowerState.this.mColorFadePrepared;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public void setScreenReady(boolean z) {
            DisplayPowerState.this.mScreenReady = z;
        }

        @Override // com.android.server.display.IDisplayPowerStateWrapper
        public void scheduleScreenUpdate() {
            DisplayPowerState.this.scheduleScreenUpdate();
        }
    }
}
