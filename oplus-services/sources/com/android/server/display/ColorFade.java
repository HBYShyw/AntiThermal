package com.android.server.display;

import android.R;
import android.content.Context;
import android.graphics.BLASTBufferQueue;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManagerInternal;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.SystemProperties;
import android.util.Slog;
import android.view.DisplayInfo;
import android.view.Surface;
import android.view.SurfaceControl;
import android.window.ScreenCapture;
import com.android.internal.policy.TransitionAnimation;
import com.android.server.LocalServices;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import libcore.io.Streams;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class ColorFade {
    private static final int COLOR_FADE_LAYER = 1073741825;
    private static final int DEJANK_FRAMES = 3;
    private static final int EGL_GL_COLORSPACE_DISPLAY_P3_PASSTHROUGH_EXT = 13456;
    private static final int EGL_GL_COLORSPACE_KHR = 12445;
    private static final int EGL_PROTECTED_CONTENT_EXT = 12992;
    public static final int MODE_COOL_DOWN = 1;
    public static final int MODE_FADE = 2;
    public static final int MODE_WARM_UP = 0;
    private static final String TAG = "ColorFade";
    private BLASTBufferQueue mBLASTBufferQueue;
    private SurfaceControl mBLASTSurfaceControl;
    private boolean mCreatedResources;
    private int mDisplayHeight;
    private final int mDisplayId;
    private int mDisplayLayerStack;
    private int mDisplayWidth;
    private EGLConfig mEglConfig;
    private EGLContext mEglContext;
    private EGLDisplay mEglDisplay;
    private EGLSurface mEglSurface;
    private int mGammaLoc;
    private boolean mLastWasProtectedContent;
    private boolean mLastWasWideColor;
    private int mMode;
    private int mOpacityLoc;
    private boolean mPrepared;
    private int mProgram;
    private int mProjMatrixLoc;
    private Surface mSurface;
    private float mSurfaceAlpha;
    private SurfaceControl mSurfaceControl;
    private NaturalSurfaceLayout mSurfaceLayout;
    private boolean mSurfaceVisible;
    private int mTexCoordLoc;
    private int mTexMatrixLoc;
    private boolean mTexNamesGenerated;
    private int mTexUnitLoc;
    private int mVertexLoc;
    private static final boolean DEBUG = SystemProperties.getBoolean("dbg.dms.colorfade", false);
    private static final boolean VFXONLY = SystemProperties.getBoolean("persist.oplus.vfxeffect.only", false);
    private final int[] mTexNames = new int[1];
    private final float[] mTexMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final int[] mGLBuffers = new int[2];
    private final FloatBuffer mVertexBuffer = createNativeFloatBuffer(8);
    private final FloatBuffer mTexCoordBuffer = createNativeFloatBuffer(8);
    private final SurfaceControl.Transaction mTransaction = new SurfaceControl.Transaction();
    private final DisplayManagerInternal mDisplayManagerInternal = (DisplayManagerInternal) LocalServices.getService(DisplayManagerInternal.class);

    public ColorFade(int i) {
        this.mDisplayId = i;
        Slog.d(TAG, "create ColorFade for displayId =" + i);
    }

    public boolean prepare(Context context, int i) {
        if (DEBUG) {
            Slog.d(TAG, "prepare: mode=" + i);
        }
        this.mMode = i;
        DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(this.mDisplayId);
        if (displayInfo == null) {
            return false;
        }
        this.mDisplayLayerStack = displayInfo.layerStack;
        this.mDisplayWidth = displayInfo.getNaturalWidth();
        this.mDisplayHeight = displayInfo.getNaturalHeight();
        boolean z = displayInfo.colorMode == 9;
        this.mPrepared = true;
        ScreenCapture.ScreenshotHardwareBuffer captureScreen = captureScreen();
        if (captureScreen == null) {
            dismiss();
            return false;
        }
        boolean hasProtectedContent = TransitionAnimation.hasProtectedContent(captureScreen.getHardwareBuffer());
        if (!createSurfaceControl(captureScreen.containsSecureLayers())) {
            dismiss();
            return false;
        }
        if (this.mMode == 2) {
            return true;
        }
        if (!createEglContext(hasProtectedContent) || !createEglSurface(hasProtectedContent, z) || !setScreenshotTextureAndSetViewport(captureScreen, displayInfo.rotation)) {
            dismiss();
            return false;
        }
        if (!attachEglContext()) {
            return false;
        }
        try {
            if (!initGLShaders(context) || !initGLBuffers() || checkGlErrors("prepare")) {
                detachEglContext();
                dismiss();
                return false;
            }
            detachEglContext();
            this.mCreatedResources = true;
            this.mLastWasProtectedContent = hasProtectedContent;
            this.mLastWasWideColor = z;
            if (i == 1) {
                for (int i2 = 0; i2 < 3; i2++) {
                    draw(1.0f);
                }
            }
            return true;
        } finally {
            detachEglContext();
        }
    }

    private String readFile(Context context, int i) {
        try {
            return new String(Streams.readFully(new InputStreamReader(context.getResources().openRawResource(i))));
        } catch (IOException e) {
            Slog.e(TAG, "Unrecognized shader " + Integer.toString(i));
            throw new RuntimeException(e);
        }
    }

    private int loadShader(Context context, int i, int i2) {
        String readFile = readFile(context, i);
        int glCreateShader = GLES20.glCreateShader(i2);
        GLES20.glShaderSource(glCreateShader, readFile);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        Slog.e(TAG, "Could not compile shader " + glCreateShader + ", " + i2 + ":");
        Slog.e(TAG, GLES20.glGetShaderSource(glCreateShader));
        Slog.e(TAG, GLES20.glGetShaderInfoLog(glCreateShader));
        GLES20.glDeleteShader(glCreateShader);
        return 0;
    }

    private boolean initGLShaders(Context context) {
        int loadShader = loadShader(context, R.raw.color_fade_vert, 35633);
        int loadShader2 = loadShader(context, R.raw.color_fade_frag, 35632);
        GLES20.glReleaseShaderCompiler();
        if (loadShader == 0 || loadShader2 == 0) {
            return false;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        this.mProgram = glCreateProgram;
        GLES20.glAttachShader(glCreateProgram, loadShader);
        GLES20.glAttachShader(this.mProgram, loadShader2);
        GLES20.glDeleteShader(loadShader);
        GLES20.glDeleteShader(loadShader2);
        GLES20.glLinkProgram(this.mProgram);
        this.mVertexLoc = GLES20.glGetAttribLocation(this.mProgram, "position");
        this.mTexCoordLoc = GLES20.glGetAttribLocation(this.mProgram, "uv");
        this.mProjMatrixLoc = GLES20.glGetUniformLocation(this.mProgram, "proj_matrix");
        this.mTexMatrixLoc = GLES20.glGetUniformLocation(this.mProgram, "tex_matrix");
        this.mOpacityLoc = GLES20.glGetUniformLocation(this.mProgram, "opacity");
        this.mGammaLoc = GLES20.glGetUniformLocation(this.mProgram, "gamma");
        this.mTexUnitLoc = GLES20.glGetUniformLocation(this.mProgram, "texUnit");
        GLES20.glUseProgram(this.mProgram);
        GLES20.glUniform1i(this.mTexUnitLoc, 0);
        GLES20.glUseProgram(0);
        return true;
    }

    private void destroyGLShaders() {
        GLES20.glDeleteProgram(this.mProgram);
        checkGlErrors("glDeleteProgram");
    }

    private boolean initGLBuffers() {
        setQuad(this.mVertexBuffer, 0.0f, 0.0f, this.mDisplayWidth, this.mDisplayHeight);
        GLES20.glBindTexture(36197, this.mTexNames[0]);
        GLES20.glTexParameteri(36197, 10240, 9728);
        GLES20.glTexParameteri(36197, 10241, 9728);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        GLES20.glBindTexture(36197, 0);
        GLES20.glGenBuffers(2, this.mGLBuffers, 0);
        GLES20.glBindBuffer(34962, this.mGLBuffers[0]);
        GLES20.glBufferData(34962, this.mVertexBuffer.capacity() * 4, this.mVertexBuffer, 35044);
        GLES20.glBindBuffer(34962, this.mGLBuffers[1]);
        GLES20.glBufferData(34962, this.mTexCoordBuffer.capacity() * 4, this.mTexCoordBuffer, 35044);
        GLES20.glBindBuffer(34962, 0);
        return true;
    }

    private void destroyGLBuffers() {
        GLES20.glDeleteBuffers(2, this.mGLBuffers, 0);
        checkGlErrors("glDeleteBuffers");
    }

    private static void setQuad(FloatBuffer floatBuffer, float f, float f2, float f3, float f4) {
        if (DEBUG) {
            Slog.d(TAG, "setQuad: x=" + f + ", y=" + f2 + ", w=" + f3 + ", h=" + f4);
        }
        floatBuffer.put(0, f);
        floatBuffer.put(1, f2);
        floatBuffer.put(2, f);
        float f5 = f4 + f2;
        floatBuffer.put(3, f5);
        float f6 = f + f3;
        floatBuffer.put(4, f6);
        floatBuffer.put(5, f5);
        floatBuffer.put(6, f6);
        floatBuffer.put(7, f2);
    }

    public void dismissResources() {
        if (DEBUG) {
            Slog.d(TAG, "dismissResources");
        }
        if (this.mCreatedResources) {
            attachEglContext();
            try {
                destroyScreenshotTexture();
                destroyGLShaders();
                destroyGLBuffers();
                destroyEglSurface();
                detachEglContext();
                GLES20.glFlush();
                this.mCreatedResources = false;
            } catch (Throwable th) {
                detachEglContext();
                throw th;
            }
        }
    }

    public void dismiss() {
        if (DEBUG) {
            Slog.d(TAG, "dismiss");
        }
        if (!"PowerManagerService".equals(Thread.currentThread().getName())) {
            Slog.e(TAG, "dismiss in other thread", new Throwable());
        }
        if (this.mPrepared) {
            dismissResources();
            destroySurface();
            this.mPrepared = false;
        }
    }

    protected void finalize() throws Throwable {
        try {
            EGLContext eGLContext = this.mEglContext;
            if (eGLContext != null) {
                EGL14.eglDestroyContext(this.mEglDisplay, eGLContext);
                this.mEglContext = null;
            }
        } finally {
            super.finalize();
        }
    }

    public boolean draw(float f) {
        if (DEBUG) {
            Slog.d(TAG, "drawFrame: level=" + f);
        }
        if (!this.mPrepared) {
            return false;
        }
        if (this.mMode == 2) {
            return showSurface(1.0f - f);
        }
        if (!attachEglContext()) {
            return false;
        }
        if (VFXONLY && f != 0.0f) {
            return showSurface(0.0f);
        }
        try {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            double d = 1.0f - f;
            double cos = Math.cos(3.141592653589793d * d);
            drawFaded(((float) (-Math.pow(d, 2.0d))) + 1.0f, 1.0f / ((float) ((((((cos < 0.0d ? -1.0d : 1.0d) * 0.5d) * Math.pow(cos, 2.0d)) + 0.5d) * 0.9d) + 0.1d)));
            if (checkGlErrors("drawFrame")) {
                return false;
            }
            EGL14.eglSwapBuffers(this.mEglDisplay, this.mEglSurface);
            detachEglContext();
            return showSurface(1.0f);
        } finally {
            detachEglContext();
        }
    }

    private void drawFaded(float f, float f2) {
        if (DEBUG) {
            Slog.d(TAG, "drawFaded: opacity=" + f + ", gamma=" + f2);
        }
        GLES20.glUseProgram(this.mProgram);
        GLES20.glUniformMatrix4fv(this.mProjMatrixLoc, 1, false, this.mProjMatrix, 0);
        GLES20.glUniformMatrix4fv(this.mTexMatrixLoc, 1, false, this.mTexMatrix, 0);
        GLES20.glUniform1f(this.mOpacityLoc, f);
        GLES20.glUniform1f(this.mGammaLoc, f2);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, this.mTexNames[0]);
        GLES20.glBindBuffer(34962, this.mGLBuffers[0]);
        GLES20.glEnableVertexAttribArray(this.mVertexLoc);
        GLES20.glVertexAttribPointer(this.mVertexLoc, 2, 5126, false, 0, 0);
        GLES20.glBindBuffer(34962, this.mGLBuffers[1]);
        GLES20.glEnableVertexAttribArray(this.mTexCoordLoc);
        GLES20.glVertexAttribPointer(this.mTexCoordLoc, 2, 5126, false, 0, 0);
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glBindTexture(36197, 0);
        GLES20.glBindBuffer(34962, 0);
    }

    private void ortho(float f, float f2, float f3, float f4, float f5, float f6) {
        float[] fArr = this.mProjMatrix;
        float f7 = f2 - f;
        fArr[0] = 2.0f / f7;
        fArr[1] = 0.0f;
        fArr[2] = 0.0f;
        fArr[3] = 0.0f;
        fArr[4] = 0.0f;
        float f8 = f4 - f3;
        fArr[5] = 2.0f / f8;
        fArr[6] = 0.0f;
        fArr[7] = 0.0f;
        fArr[8] = 0.0f;
        fArr[9] = 0.0f;
        float f9 = f6 - f5;
        fArr[10] = (-2.0f) / f9;
        fArr[11] = 0.0f;
        fArr[12] = (-(f2 + f)) / f7;
        fArr[13] = (-(f4 + f3)) / f8;
        fArr[14] = (-(f6 + f5)) / f9;
        fArr[15] = 1.0f;
    }

    private boolean setScreenshotTextureAndSetViewport(ScreenCapture.ScreenshotHardwareBuffer screenshotHardwareBuffer, int i) {
        if (!attachEglContext()) {
            return false;
        }
        try {
            if (!this.mTexNamesGenerated) {
                GLES20.glGenTextures(1, this.mTexNames, 0);
                if (checkGlErrors("glGenTextures")) {
                    return false;
                }
                this.mTexNamesGenerated = true;
            }
            SurfaceTexture surfaceTexture = new SurfaceTexture(this.mTexNames[0]);
            Surface surface = new Surface(surfaceTexture);
            try {
                surface.attachAndQueueBufferWithColorSpace(screenshotHardwareBuffer.getHardwareBuffer(), screenshotHardwareBuffer.getColorSpace());
                surfaceTexture.updateTexImage();
                surfaceTexture.getTransformMatrix(this.mTexMatrix);
                surface.release();
                surfaceTexture.release();
                int i2 = 2;
                if (i != 1) {
                    i2 = i == 2 ? 4 : i == 3 ? 6 : 0;
                }
                this.mTexCoordBuffer.put(i2, 0.0f);
                this.mTexCoordBuffer.put(i2 + 1, 0.0f);
                this.mTexCoordBuffer.put((i2 + 2) % 8, 0.0f);
                this.mTexCoordBuffer.put((i2 + 3) % 8, 1.0f);
                this.mTexCoordBuffer.put((i2 + 4) % 8, 1.0f);
                this.mTexCoordBuffer.put((i2 + 5) % 8, 1.0f);
                this.mTexCoordBuffer.put((i2 + 6) % 8, 1.0f);
                this.mTexCoordBuffer.put((i2 + 7) % 8, 0.0f);
                GLES20.glViewport(0, 0, this.mDisplayWidth, this.mDisplayHeight);
                ortho(0.0f, this.mDisplayWidth, 0.0f, this.mDisplayHeight, -1.0f, 1.0f);
                return true;
            } catch (Throwable th) {
                surface.release();
                surfaceTexture.release();
                throw th;
            }
        } finally {
            detachEglContext();
        }
    }

    private void destroyScreenshotTexture() {
        if (this.mTexNamesGenerated) {
            this.mTexNamesGenerated = false;
            GLES20.glDeleteTextures(1, this.mTexNames, 0);
            checkGlErrors("glDeleteTextures");
        }
    }

    private ScreenCapture.ScreenshotHardwareBuffer captureScreen() {
        ScreenCapture.ScreenshotHardwareBuffer systemScreenshot = this.mDisplayManagerInternal.systemScreenshot(this.mDisplayId);
        if (systemScreenshot != null) {
            return systemScreenshot;
        }
        Slog.e(TAG, "Failed to take screenshot. Buffer is null");
        return null;
    }

    private boolean createSurfaceControl(boolean z) {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            this.mTransaction.setSecure(surfaceControl, z).apply();
            return true;
        }
        try {
            SurfaceControl.Builder callsite = new SurfaceControl.Builder().setName(TAG).setSecure(z).setCallsite("ColorFade.createSurface");
            if (this.mMode == 2) {
                callsite.setColorLayer();
            } else {
                callsite.setContainerLayer();
            }
            SurfaceControl build = callsite.build();
            this.mSurfaceControl = build;
            this.mTransaction.setLayerStack(build, this.mDisplayLayerStack);
            this.mTransaction.setWindowCrop(this.mSurfaceControl, this.mDisplayWidth, this.mDisplayHeight);
            NaturalSurfaceLayout naturalSurfaceLayout = new NaturalSurfaceLayout(this.mDisplayManagerInternal, this.mDisplayId, this.mSurfaceControl);
            this.mSurfaceLayout = naturalSurfaceLayout;
            naturalSurfaceLayout.onDisplayTransaction(this.mTransaction);
            this.mTransaction.apply();
            if (this.mMode != 2) {
                this.mBLASTSurfaceControl = new SurfaceControl.Builder().setName("ColorFade BLAST").setParent(this.mSurfaceControl).setHidden(false).setSecure(z).setBLASTLayer().build();
                BLASTBufferQueue bLASTBufferQueue = new BLASTBufferQueue(TAG, this.mBLASTSurfaceControl, this.mDisplayWidth, this.mDisplayHeight, -3);
                this.mBLASTBufferQueue = bLASTBufferQueue;
                this.mSurface = bLASTBufferQueue.createSurface();
            }
            return true;
        } catch (Surface.OutOfResourcesException e) {
            Slog.e(TAG, "Unable to create surface.", e);
            return false;
        }
    }

    private boolean createEglContext(boolean z) {
        if (this.mEglDisplay == null) {
            EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
            this.mEglDisplay = eglGetDisplay;
            if (eglGetDisplay == EGL14.EGL_NO_DISPLAY) {
                logEglError("eglGetDisplay");
                return false;
            }
            int[] iArr = new int[2];
            if (!EGL14.eglInitialize(eglGetDisplay, iArr, 0, iArr, 1)) {
                this.mEglDisplay = null;
                logEglError("eglInitialize");
                return false;
            }
        }
        if (this.mEglConfig == null) {
            int[] iArr2 = new int[1];
            EGLConfig[] eGLConfigArr = new EGLConfig[1];
            if (!EGL14.eglChooseConfig(this.mEglDisplay, new int[]{12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344}, 0, eGLConfigArr, 0, 1, iArr2, 0)) {
                logEglError("eglChooseConfig");
                return false;
            }
            if (iArr2[0] <= 0) {
                Slog.e(TAG, "no valid config found");
                return false;
            }
            this.mEglConfig = eGLConfigArr[0];
        }
        if (this.mEglContext != null && z != this.mLastWasProtectedContent) {
            Slog.i(TAG, "eglDestroyContext for mDisplayId = " + this.mDisplayId + " mEglContext " + this.mEglContext);
            EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext);
            this.mEglContext = null;
        }
        if (this.mEglContext == null) {
            int[] iArr3 = {12440, 2, 12344, 12344, 12344};
            if (z) {
                iArr3[2] = EGL_PROTECTED_CONTENT_EXT;
                iArr3[3] = 1;
            }
            this.mEglContext = EGL14.eglCreateContext(this.mEglDisplay, this.mEglConfig, EGL14.EGL_NO_CONTEXT, iArr3, 0);
            Slog.i(TAG, "eglCreateContext for mDisplayId = " + this.mDisplayId + " mEglContext " + this.mEglContext);
            if (this.mEglContext == null) {
                logEglError("eglCreateContext");
                return false;
            }
        }
        return true;
    }

    public void dismissEglContext() {
        if (this.mEglDisplay == null || this.mEglContext == null) {
            Slog.i(TAG, "force dismissEglContext failed of mDisplayId =" + this.mDisplayId + " for mEglDisplay = " + this.mEglDisplay + " mEglContext = " + this.mEglContext);
            return;
        }
        Slog.i(TAG, "force dismissEglContext(eglDestroyContext) for mDisplayId = " + this.mDisplayId + " mEglContext " + this.mEglContext);
        EGL14.eglDestroyContext(this.mEglDisplay, this.mEglContext);
        this.mEglContext = null;
    }

    private boolean createEglSurface(boolean z, boolean z2) {
        int i;
        boolean z3 = (z == this.mLastWasProtectedContent && z2 == this.mLastWasWideColor) ? false : true;
        EGLSurface eGLSurface = this.mEglSurface;
        if (eGLSurface != null && z3) {
            EGL14.eglDestroySurface(this.mEglDisplay, eGLSurface);
            this.mEglSurface = null;
        }
        Surface surface = this.mSurface;
        if (surface == null) {
            Slog.e(TAG, "createEglSurface but surface is null, please check dismiss()");
            return false;
        }
        if (this.mEglSurface == null) {
            int[] iArr = {12344, 12344, 12344, 12344, 12344};
            if (z2) {
                iArr[0] = EGL_GL_COLORSPACE_KHR;
                iArr[1] = EGL_GL_COLORSPACE_DISPLAY_P3_PASSTHROUGH_EXT;
                i = 2;
            } else {
                i = 0;
            }
            if (z) {
                iArr[i] = EGL_PROTECTED_CONTENT_EXT;
                iArr[i + 1] = 1;
            }
            EGLSurface eglCreateWindowSurface = EGL14.eglCreateWindowSurface(this.mEglDisplay, this.mEglConfig, surface, iArr, 0);
            this.mEglSurface = eglCreateWindowSurface;
            if (eglCreateWindowSurface == null) {
                logEglError("eglCreateWindowSurface");
                return false;
            }
        }
        return true;
    }

    private void destroyEglSurface() {
        EGLSurface eGLSurface = this.mEglSurface;
        if (eGLSurface != null) {
            if (!EGL14.eglDestroySurface(this.mEglDisplay, eGLSurface)) {
                logEglError("eglDestroySurface");
            }
            this.mEglSurface = null;
        }
    }

    private void destroySurface() {
        if (this.mSurfaceControl != null) {
            this.mSurfaceLayout.dispose();
            this.mSurfaceLayout = null;
            this.mTransaction.hide(this.mSurfaceControl).apply();
            this.mTransaction.remove(this.mSurfaceControl).apply();
            Surface surface = this.mSurface;
            if (surface != null) {
                surface.release();
                this.mSurface = null;
            }
            SurfaceControl surfaceControl = this.mBLASTSurfaceControl;
            if (surfaceControl != null) {
                surfaceControl.release();
                this.mBLASTSurfaceControl = null;
                this.mBLASTBufferQueue.destroy();
                this.mBLASTBufferQueue = null;
            }
            this.mSurfaceControl = null;
            this.mSurfaceVisible = false;
            this.mSurfaceAlpha = 0.0f;
        }
    }

    private boolean showSurface(float f) {
        if (!this.mSurfaceVisible || this.mSurfaceAlpha != f) {
            this.mTransaction.setLayer(this.mSurfaceControl, COLOR_FADE_LAYER).setAlpha(this.mSurfaceControl, f).show(this.mSurfaceControl).apply();
            this.mSurfaceVisible = true;
            this.mSurfaceAlpha = f;
        }
        return true;
    }

    private boolean attachEglContext() {
        EGLSurface eGLSurface = this.mEglSurface;
        if (eGLSurface == null) {
            return false;
        }
        if (EGL14.eglMakeCurrent(this.mEglDisplay, eGLSurface, eGLSurface, this.mEglContext)) {
            return true;
        }
        logEglError("eglMakeCurrent");
        return false;
    }

    private void detachEglContext() {
        EGLDisplay eGLDisplay = this.mEglDisplay;
        if (eGLDisplay != null) {
            EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
            EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
        }
    }

    private static FloatBuffer createNativeFloatBuffer(int i) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        return allocateDirect.asFloatBuffer();
    }

    private static void logEglError(String str) {
        Slog.e(TAG, str + " failed: error " + EGL14.eglGetError(), new Throwable());
    }

    private static boolean checkGlErrors(String str) {
        return checkGlErrors(str, true);
    }

    private static boolean checkGlErrors(String str, boolean z) {
        boolean z2 = false;
        while (true) {
            int glGetError = GLES20.glGetError();
            if (glGetError == 0) {
                return z2;
            }
            if (z) {
                Slog.e(TAG, str + " failed: error " + glGetError, new Throwable());
            }
            z2 = true;
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println();
        printWriter.println("Color Fade State:");
        printWriter.println("  mPrepared=" + this.mPrepared);
        printWriter.println("  mMode=" + this.mMode);
        printWriter.println("  mDisplayLayerStack=" + this.mDisplayLayerStack);
        printWriter.println("  mDisplayWidth=" + this.mDisplayWidth);
        printWriter.println("  mDisplayHeight=" + this.mDisplayHeight);
        printWriter.println("  mSurfaceVisible=" + this.mSurfaceVisible);
        printWriter.println("  mSurfaceAlpha=" + this.mSurfaceAlpha);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class NaturalSurfaceLayout implements DisplayManagerInternal.DisplayTransactionListener {
        private final int mDisplayId;
        private final DisplayManagerInternal mDisplayManagerInternal;
        private SurfaceControl mSurfaceControl;

        public NaturalSurfaceLayout(DisplayManagerInternal displayManagerInternal, int i, SurfaceControl surfaceControl) {
            this.mDisplayManagerInternal = displayManagerInternal;
            this.mDisplayId = i;
            this.mSurfaceControl = surfaceControl;
            displayManagerInternal.registerDisplayTransactionListener(this);
        }

        public void dispose() {
            synchronized (this) {
                this.mSurfaceControl = null;
            }
            this.mDisplayManagerInternal.unregisterDisplayTransactionListener(this);
        }

        public void onDisplayTransaction(SurfaceControl.Transaction transaction) {
            synchronized (this) {
                if (this.mSurfaceControl == null) {
                    return;
                }
                DisplayInfo displayInfo = this.mDisplayManagerInternal.getDisplayInfo(this.mDisplayId);
                if (displayInfo == null) {
                    return;
                }
                int i = displayInfo.rotation;
                if (i == 0) {
                    transaction.setPosition(this.mSurfaceControl, 0.0f, 0.0f);
                    transaction.setMatrix(this.mSurfaceControl, 1.0f, 0.0f, 0.0f, 1.0f);
                } else if (i == 1) {
                    transaction.setPosition(this.mSurfaceControl, 0.0f, displayInfo.logicalHeight);
                    transaction.setMatrix(this.mSurfaceControl, 0.0f, -1.0f, 1.0f, 0.0f);
                } else if (i == 2) {
                    transaction.setPosition(this.mSurfaceControl, displayInfo.logicalWidth, displayInfo.logicalHeight);
                    transaction.setMatrix(this.mSurfaceControl, -1.0f, 0.0f, 0.0f, -1.0f);
                } else if (i == 3) {
                    transaction.setPosition(this.mSurfaceControl, displayInfo.logicalWidth, 0.0f);
                    transaction.setMatrix(this.mSurfaceControl, 0.0f, 1.0f, -1.0f, 0.0f);
                }
            }
        }
    }
}
