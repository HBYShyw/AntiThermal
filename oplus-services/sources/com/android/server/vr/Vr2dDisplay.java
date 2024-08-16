package com.android.server.vr;

import android.app.ActivityManagerInternal;
import android.app.Vr2dDisplayProperties;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.hardware.display.VirtualDisplayConfig;
import android.media.ImageReader;
import android.os.Handler;
import android.os.RemoteException;
import android.service.vr.IPersistentVrStateCallbacks;
import android.service.vr.IVrManager;
import android.util.Log;
import android.view.Surface;
import com.android.server.LocalServices;
import com.android.server.wm.ActivityTaskManagerInternal;
import com.android.server.wm.WindowManagerInternal;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class Vr2dDisplay {
    private static final boolean DEBUG = false;
    private static final String DEBUG_ACTION_SET_MODE = "com.android.server.vr.Vr2dDisplay.SET_MODE";
    private static final String DEBUG_ACTION_SET_SURFACE = "com.android.server.vr.Vr2dDisplay.SET_SURFACE";
    private static final String DEBUG_EXTRA_MODE_ON = "com.android.server.vr.Vr2dDisplay.EXTRA_MODE_ON";
    private static final String DEBUG_EXTRA_SURFACE = "com.android.server.vr.Vr2dDisplay.EXTRA_SURFACE";
    public static final int DEFAULT_VIRTUAL_DISPLAY_DPI = 320;
    public static final int DEFAULT_VIRTUAL_DISPLAY_HEIGHT = 1800;
    public static final int DEFAULT_VIRTUAL_DISPLAY_WIDTH = 1400;
    private static final String DISPLAY_NAME = "VR 2D Display";
    public static final int MIN_VR_DISPLAY_DPI = 1;
    public static final int MIN_VR_DISPLAY_HEIGHT = 1;
    public static final int MIN_VR_DISPLAY_WIDTH = 1;
    private static final int STOP_VIRTUAL_DISPLAY_DELAY_MILLIS = 2000;
    private static final String TAG = "Vr2dDisplay";
    private static final String UNIQUE_DISPLAY_ID = "277f1a09-b88d-4d1e-8716-796f114d080b";
    private final ActivityManagerInternal mActivityManagerInternal;
    private final DisplayManager mDisplayManager;
    private ImageReader mImageReader;
    private boolean mIsPersistentVrModeEnabled;
    private boolean mIsVrModeOverrideEnabled;
    private Runnable mStopVDRunnable;
    private Surface mSurface;
    private VirtualDisplay mVirtualDisplay;
    private final IVrManager mVrManager;
    private final WindowManagerInternal mWindowManagerInternal;
    private final Object mVdLock = new Object();
    private final Handler mHandler = new Handler();
    private final IPersistentVrStateCallbacks mVrStateCallbacks = new IPersistentVrStateCallbacks.Stub() { // from class: com.android.server.vr.Vr2dDisplay.1
        public void onPersistentVrStateChanged(boolean z) {
            if (z != Vr2dDisplay.this.mIsPersistentVrModeEnabled) {
                Vr2dDisplay.this.mIsPersistentVrModeEnabled = z;
                Vr2dDisplay.this.updateVirtualDisplay();
            }
        }
    };
    private boolean mIsVirtualDisplayAllowed = true;
    private boolean mBootsToVr = false;
    private int mVirtualDisplayWidth = DEFAULT_VIRTUAL_DISPLAY_WIDTH;
    private int mVirtualDisplayHeight = 1800;
    private int mVirtualDisplayDpi = DEFAULT_VIRTUAL_DISPLAY_DPI;

    private void startDebugOnlyBroadcastReceiver(Context context) {
    }

    public Vr2dDisplay(DisplayManager displayManager, ActivityManagerInternal activityManagerInternal, WindowManagerInternal windowManagerInternal, IVrManager iVrManager) {
        this.mDisplayManager = displayManager;
        this.mActivityManagerInternal = activityManagerInternal;
        this.mWindowManagerInternal = windowManagerInternal;
        this.mVrManager = iVrManager;
    }

    public void init(Context context, boolean z) {
        startVrModeListener();
        startDebugOnlyBroadcastReceiver(context);
        this.mBootsToVr = z;
        if (z) {
            updateVirtualDisplay();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateVirtualDisplay() {
        if (shouldRunVirtualDisplay()) {
            Log.i(TAG, "Attempting to start virtual display");
            startVirtualDisplay();
        } else {
            stopVirtualDisplay();
        }
    }

    private void startVrModeListener() {
        IVrManager iVrManager = this.mVrManager;
        if (iVrManager != null) {
            try {
                iVrManager.registerPersistentVrStateListener(this.mVrStateCallbacks);
            } catch (RemoteException e) {
                Log.e(TAG, "Could not register VR State listener.", e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0074 A[Catch: all -> 0x00a3, TryCatch #0 {, blocks: (B:4:0x0003, B:9:0x0018, B:10:0x006d, B:12:0x0074, B:13:0x0080, B:16:0x0086, B:18:0x008a, B:19:0x009e, B:20:0x00a1, B:24:0x0077, B:26:0x007e, B:27:0x0046), top: B:3:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0077 A[Catch: all -> 0x00a3, TryCatch #0 {, blocks: (B:4:0x0003, B:9:0x0018, B:10:0x006d, B:12:0x0074, B:13:0x0080, B:16:0x0086, B:18:0x008a, B:19:0x009e, B:20:0x00a1, B:24:0x0077, B:26:0x007e, B:27:0x0046), top: B:3:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setVirtualDisplayProperties(Vr2dDisplayProperties vr2dDisplayProperties) {
        boolean z;
        VirtualDisplay virtualDisplay;
        synchronized (this.mVdLock) {
            int width = vr2dDisplayProperties.getWidth();
            int height = vr2dDisplayProperties.getHeight();
            int dpi = vr2dDisplayProperties.getDpi();
            if (width >= 1 && height >= 1 && dpi >= 1) {
                Log.i(TAG, "Setting width/height/dpi to " + width + "," + height + "," + dpi);
                this.mVirtualDisplayWidth = width;
                this.mVirtualDisplayHeight = height;
                this.mVirtualDisplayDpi = dpi;
                z = true;
                if ((vr2dDisplayProperties.getAddedFlags() & 1) != 1) {
                    this.mIsVirtualDisplayAllowed = true;
                } else if ((vr2dDisplayProperties.getRemovedFlags() & 1) == 1) {
                    this.mIsVirtualDisplayAllowed = false;
                }
                virtualDisplay = this.mVirtualDisplay;
                if (virtualDisplay != null && z && this.mIsVirtualDisplayAllowed) {
                    virtualDisplay.resize(this.mVirtualDisplayWidth, this.mVirtualDisplayHeight, this.mVirtualDisplayDpi);
                    ImageReader imageReader = this.mImageReader;
                    this.mImageReader = null;
                    startImageReader();
                    imageReader.close();
                }
                updateVirtualDisplay();
            }
            Log.i(TAG, "Ignoring Width/Height/Dpi values of " + width + "," + height + "," + dpi);
            z = false;
            if ((vr2dDisplayProperties.getAddedFlags() & 1) != 1) {
            }
            virtualDisplay = this.mVirtualDisplay;
            if (virtualDisplay != null) {
                virtualDisplay.resize(this.mVirtualDisplayWidth, this.mVirtualDisplayHeight, this.mVirtualDisplayDpi);
                ImageReader imageReader2 = this.mImageReader;
                this.mImageReader = null;
                startImageReader();
                imageReader2.close();
            }
            updateVirtualDisplay();
        }
    }

    public int getVirtualDisplayId() {
        synchronized (this.mVdLock) {
            VirtualDisplay virtualDisplay = this.mVirtualDisplay;
            if (virtualDisplay == null) {
                return -1;
            }
            return virtualDisplay.getDisplay().getDisplayId();
        }
    }

    private void startVirtualDisplay() {
        if (this.mDisplayManager == null) {
            Log.w(TAG, "Cannot create virtual display because mDisplayManager == null");
            return;
        }
        synchronized (this.mVdLock) {
            if (this.mVirtualDisplay != null) {
                Log.i(TAG, "VD already exists, ignoring request");
                return;
            }
            VirtualDisplayConfig.Builder builder = new VirtualDisplayConfig.Builder(DISPLAY_NAME, this.mVirtualDisplayWidth, this.mVirtualDisplayHeight, this.mVirtualDisplayDpi);
            builder.setUniqueId(UNIQUE_DISPLAY_ID);
            builder.setFlags(1485);
            VirtualDisplay createVirtualDisplay = this.mDisplayManager.createVirtualDisplay(null, builder.build(), null, null);
            this.mVirtualDisplay = createVirtualDisplay;
            if (createVirtualDisplay != null) {
                updateDisplayId(createVirtualDisplay.getDisplay().getDisplayId());
                startImageReader();
                Log.i(TAG, "VD created: " + this.mVirtualDisplay);
                return;
            }
            Log.w(TAG, "Virtual display id is null after createVirtualDisplay");
            updateDisplayId(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplayId(int i) {
        ((ActivityTaskManagerInternal) LocalServices.getService(ActivityTaskManagerInternal.class)).setVr2dDisplayId(i);
        this.mWindowManagerInternal.setVr2dDisplayId(i);
    }

    private void stopVirtualDisplay() {
        if (this.mStopVDRunnable == null) {
            this.mStopVDRunnable = new Runnable() { // from class: com.android.server.vr.Vr2dDisplay.3
                @Override // java.lang.Runnable
                public void run() {
                    if (Vr2dDisplay.this.shouldRunVirtualDisplay()) {
                        Log.i(Vr2dDisplay.TAG, "Virtual Display destruction stopped: VrMode is back on.");
                        return;
                    }
                    Log.i(Vr2dDisplay.TAG, "Stopping Virtual Display");
                    synchronized (Vr2dDisplay.this.mVdLock) {
                        Vr2dDisplay.this.updateDisplayId(-1);
                        Vr2dDisplay.this.setSurfaceLocked(null);
                        if (Vr2dDisplay.this.mVirtualDisplay != null) {
                            Vr2dDisplay.this.mVirtualDisplay.release();
                            Vr2dDisplay.this.mVirtualDisplay = null;
                        }
                        Vr2dDisplay.this.stopImageReader();
                    }
                }
            };
        }
        this.mHandler.removeCallbacks(this.mStopVDRunnable);
        this.mHandler.postDelayed(this.mStopVDRunnable, 2000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSurfaceLocked(Surface surface) {
        if (this.mSurface != surface) {
            if (surface == null || surface.isValid()) {
                Log.i(TAG, "Setting the new surface from " + this.mSurface + " to " + surface);
                VirtualDisplay virtualDisplay = this.mVirtualDisplay;
                if (virtualDisplay != null) {
                    virtualDisplay.setSurface(surface);
                }
                Surface surface2 = this.mSurface;
                if (surface2 != null) {
                    surface2.release();
                }
                this.mSurface = surface;
            }
        }
    }

    private void startImageReader() {
        if (this.mImageReader == null) {
            this.mImageReader = ImageReader.newInstance(this.mVirtualDisplayWidth, this.mVirtualDisplayHeight, 1, 2);
            Log.i(TAG, "VD startImageReader: res = " + this.mVirtualDisplayWidth + "X" + this.mVirtualDisplayHeight + ", dpi = " + this.mVirtualDisplayDpi);
        }
        synchronized (this.mVdLock) {
            setSurfaceLocked(this.mImageReader.getSurface());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopImageReader() {
        ImageReader imageReader = this.mImageReader;
        if (imageReader != null) {
            imageReader.close();
            this.mImageReader = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldRunVirtualDisplay() {
        return this.mIsVirtualDisplayAllowed && (this.mBootsToVr || this.mIsPersistentVrModeEnabled || this.mIsVrModeOverrideEnabled);
    }
}
