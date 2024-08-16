package com.android.server.display;

import android.R;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayShape;
import android.view.Surface;
import android.view.SurfaceControl;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.display.DisplayAdapter;
import com.android.server.display.DisplayManagerService;
import com.android.server.display.OverlayDisplayWindow;
import com.android.server.display.mode.DisplayModeDirector;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import system.ext.loader.core.ExtLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class OverlayDisplayAdapter extends DisplayAdapter {
    static final boolean DEBUG = false;
    private static final String DISPLAY_SPLITTER = ";";
    private static final String FLAG_SPLITTER = ",";
    private static final int MAX_HEIGHT = 4096;
    private static final int MAX_WIDTH = 4096;
    private static final int MIN_HEIGHT = 100;
    private static final int MIN_WIDTH = 100;
    private static final String MODE_SPLITTER = "\\|";
    private static final String OVERLAY_DISPLAY_FLAG_OWN_CONTENT_ONLY = "own_content_only";
    private static final String OVERLAY_DISPLAY_FLAG_SECURE = "secure";
    private static final String OVERLAY_DISPLAY_FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS = "should_show_system_decorations";
    static final String TAG = "OverlayDisplayAdapter";
    private static final String UNIQUE_ID_PREFIX = "overlay:";
    private String mCurrentOverlaySetting;
    private final ArrayList<OverlayDisplayHandle> mOverlays;
    private final Handler mUiHandler;
    private static final Pattern DISPLAY_PATTERN = Pattern.compile("([^,]+)(,[,_a-z]+)*");
    private static final Pattern MODE_PATTERN = Pattern.compile("(\\d+)x(\\d+)/(\\d+)");

    private static int chooseOverlayGravity(int i) {
        if (i == 1) {
            return 51;
        }
        if (i != 2) {
            return i != 3 ? 83 : 53;
        }
        return 85;
    }

    public OverlayDisplayAdapter(DisplayManagerService.SyncRoot syncRoot, Context context, Handler handler, DisplayAdapter.Listener listener, Handler handler2) {
        super(syncRoot, context, handler, listener, TAG);
        this.mOverlays = new ArrayList<>();
        this.mCurrentOverlaySetting = "";
        this.mUiHandler = handler2;
        ((IMirageDisplayManagerExt) ExtLoader.type(IMirageDisplayManagerExt.class).create()).init(this, syncRoot, handler, context, handler2);
    }

    @Override // com.android.server.display.DisplayAdapter
    public void dumpLocked(PrintWriter printWriter) {
        super.dumpLocked(printWriter);
        printWriter.println("mCurrentOverlaySetting=" + this.mCurrentOverlaySetting);
        printWriter.println("mOverlays: size=" + this.mOverlays.size());
        Iterator<OverlayDisplayHandle> it = this.mOverlays.iterator();
        while (it.hasNext()) {
            it.next().dumpLocked(printWriter);
        }
    }

    @Override // com.android.server.display.DisplayAdapter
    public void registerLocked() {
        super.registerLocked();
        getHandler().post(new Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.1
            @Override // java.lang.Runnable
            public void run() {
                OverlayDisplayAdapter.this.getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("overlay_display_devices"), true, new ContentObserver(OverlayDisplayAdapter.this.getHandler()) { // from class: com.android.server.display.OverlayDisplayAdapter.1.1
                    @Override // android.database.ContentObserver
                    public void onChange(boolean z) {
                        OverlayDisplayAdapter.this.updateOverlayDisplayDevices();
                    }
                });
                OverlayDisplayAdapter.this.updateOverlayDisplayDevices();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOverlayDisplayDevices() {
        synchronized (getSyncRoot()) {
            updateOverlayDisplayDevicesLocked();
        }
    }

    private void updateOverlayDisplayDevicesLocked() {
        String[] strArr;
        int i;
        String string = Settings.Global.getString(getContext().getContentResolver(), "overlay_display_devices");
        if (string == null) {
            string = "";
        }
        String str = string;
        if (str.equals(this.mCurrentOverlaySetting)) {
            return;
        }
        this.mCurrentOverlaySetting = str;
        if (!this.mOverlays.isEmpty()) {
            Slog.i(TAG, "Dismissing all overlay display devices.");
            Iterator<OverlayDisplayHandle> it = this.mOverlays.iterator();
            while (it.hasNext()) {
                it.next().dismissLocked();
            }
            this.mOverlays.clear();
        }
        int i2 = 0;
        for (String str2 : str.split(DISPLAY_SPLITTER)) {
            Matcher matcher = DISPLAY_PATTERN.matcher(str2);
            if (matcher.matches()) {
                if (i2 >= 4) {
                    Slog.w(TAG, "Too many overlay display devices specified: " + str);
                    return;
                }
                int i3 = 1;
                String group = matcher.group(1);
                String group2 = matcher.group(2);
                ArrayList arrayList = new ArrayList();
                String[] split = group.split(MODE_SPLITTER);
                int length = split.length;
                int i4 = 0;
                while (i4 < length) {
                    String str3 = split[i4];
                    Matcher matcher2 = MODE_PATTERN.matcher(str3);
                    if (matcher2.matches()) {
                        try {
                            int parseInt = Integer.parseInt(matcher2.group(i3), 10);
                            strArr = split;
                            try {
                                int parseInt2 = Integer.parseInt(matcher2.group(2), 10);
                                int parseInt3 = Integer.parseInt(matcher2.group(3), 10);
                                i = length;
                                if (parseInt >= 100 && parseInt <= 4096 && parseInt2 >= 100 && parseInt2 <= 4096 && parseInt3 >= 120 && parseInt3 <= 640) {
                                    try {
                                        arrayList.add(new OverlayMode(parseInt, parseInt2, parseInt3));
                                    } catch (NumberFormatException unused) {
                                    }
                                } else {
                                    Slog.w(TAG, "Ignoring out-of-range overlay display mode: " + str3);
                                }
                            } catch (NumberFormatException unused2) {
                                i = length;
                                i4++;
                                split = strArr;
                                length = i;
                                i3 = 1;
                            }
                        } catch (NumberFormatException unused3) {
                            strArr = split;
                        }
                    } else {
                        strArr = split;
                        i = length;
                        str3.isEmpty();
                    }
                    i4++;
                    split = strArr;
                    length = i;
                    i3 = 1;
                }
                if (!arrayList.isEmpty()) {
                    int i5 = i2 + 1;
                    String string2 = getContext().getResources().getString(R.string.find, Integer.valueOf(i5));
                    int chooseOverlayGravity = chooseOverlayGravity(i5);
                    OverlayFlags parseFlags = OverlayFlags.parseFlags(group2);
                    Slog.i(TAG, "Showing overlay display device #" + i5 + ": name=" + string2 + ", modes=" + Arrays.toString(arrayList.toArray()) + ", flags=" + parseFlags);
                    this.mOverlays.add(new OverlayDisplayHandle(string2, arrayList, chooseOverlayGravity, parseFlags, i5));
                    i2 = i5;
                }
            }
            Slog.w(TAG, "Malformed overlay display devices setting: " + str);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private abstract class OverlayDisplayDevice extends DisplayDevice {
        private int mActiveMode;
        private final int mDefaultMode;
        private final long mDisplayPresentationDeadlineNanos;
        private final OverlayFlags mFlags;
        private DisplayDeviceInfo mInfo;
        private final Display.Mode[] mModes;
        private final String mName;
        private final List<OverlayMode> mRawModes;
        private final float mRefreshRate;
        private int mState;
        private Surface mSurface;
        private SurfaceTexture mSurfaceTexture;

        @Override // com.android.server.display.DisplayDevice
        public boolean hasStableUniqueId() {
            return false;
        }

        public abstract void onModeChangedLocked(int i);

        OverlayDisplayDevice(IBinder iBinder, String str, List<OverlayMode> list, int i, int i2, float f, long j, OverlayFlags overlayFlags, int i3, SurfaceTexture surfaceTexture, int i4) {
            super(OverlayDisplayAdapter.this, iBinder, OverlayDisplayAdapter.UNIQUE_ID_PREFIX + i4, OverlayDisplayAdapter.this.getContext());
            this.mName = str;
            this.mRefreshRate = f;
            this.mDisplayPresentationDeadlineNanos = j;
            this.mFlags = overlayFlags;
            this.mState = i3;
            this.mSurfaceTexture = surfaceTexture;
            this.mRawModes = list;
            this.mModes = new Display.Mode[list.size()];
            for (int i5 = 0; i5 < list.size(); i5++) {
                OverlayMode overlayMode = list.get(i5);
                this.mModes[i5] = DisplayAdapter.createMode(overlayMode.mWidth, overlayMode.mHeight, f);
            }
            this.mActiveMode = i;
            this.mDefaultMode = i2;
        }

        public void destroyLocked() {
            this.mSurfaceTexture = null;
            Surface surface = this.mSurface;
            if (surface != null) {
                surface.release();
                this.mSurface = null;
            }
            DisplayControl.destroyDisplay(getDisplayTokenLocked());
        }

        @Override // com.android.server.display.DisplayDevice
        public void performTraversalLocked(SurfaceControl.Transaction transaction) {
            if (this.mSurfaceTexture != null) {
                if (this.mSurface == null) {
                    this.mSurface = new Surface(this.mSurfaceTexture);
                }
                setSurfaceLocked(transaction, this.mSurface);
            }
        }

        public void setStateLocked(int i) {
            this.mState = i;
            this.mInfo = null;
        }

        @Override // com.android.server.display.DisplayDevice
        public DisplayDeviceInfo getDisplayDeviceInfoLocked() {
            if (this.mInfo == null) {
                Display.Mode[] modeArr = this.mModes;
                int i = this.mActiveMode;
                Display.Mode mode = modeArr[i];
                OverlayMode overlayMode = this.mRawModes.get(i);
                DisplayDeviceInfo displayDeviceInfo = new DisplayDeviceInfo();
                this.mInfo = displayDeviceInfo;
                displayDeviceInfo.name = this.mName;
                displayDeviceInfo.uniqueId = getUniqueId();
                this.mInfo.width = mode.getPhysicalWidth();
                this.mInfo.height = mode.getPhysicalHeight();
                this.mInfo.modeId = mode.getModeId();
                this.mInfo.renderFrameRate = mode.getRefreshRate();
                this.mInfo.defaultModeId = this.mModes[0].getModeId();
                DisplayDeviceInfo displayDeviceInfo2 = this.mInfo;
                displayDeviceInfo2.supportedModes = this.mModes;
                int i2 = overlayMode.mDensityDpi;
                displayDeviceInfo2.densityDpi = i2;
                displayDeviceInfo2.xDpi = i2;
                displayDeviceInfo2.yDpi = i2;
                displayDeviceInfo2.presentationDeadlineNanos = this.mDisplayPresentationDeadlineNanos + (1000000000 / ((int) this.mRefreshRate));
                displayDeviceInfo2.flags = 64;
                OverlayFlags overlayFlags = this.mFlags;
                if (overlayFlags.mSecure) {
                    displayDeviceInfo2.flags = 64 | 4;
                }
                if (overlayFlags.mOwnContentOnly) {
                    displayDeviceInfo2.flags |= 128;
                }
                if (overlayFlags.mShouldShowSystemDecorations) {
                    displayDeviceInfo2.flags |= 4096;
                }
                displayDeviceInfo2.type = 4;
                displayDeviceInfo2.touch = 3;
                displayDeviceInfo2.state = this.mState;
                displayDeviceInfo2.flags |= 8192;
                displayDeviceInfo2.displayShape = DisplayShape.createDefaultDisplayShape(displayDeviceInfo2.width, displayDeviceInfo2.height, false);
            }
            return this.mInfo;
        }

        @Override // com.android.server.display.DisplayDevice
        public void setDesiredDisplayModeSpecsLocked(DisplayModeDirector.DesiredDisplayModeSpecs desiredDisplayModeSpecs) {
            int i = desiredDisplayModeSpecs.baseModeId;
            int i2 = 0;
            if (i != 0) {
                while (true) {
                    Display.Mode[] modeArr = this.mModes;
                    if (i2 >= modeArr.length) {
                        i2 = -1;
                        break;
                    } else if (modeArr[i2].getModeId() == i) {
                        break;
                    } else {
                        i2++;
                    }
                }
            }
            if (i2 == -1) {
                Slog.w(OverlayDisplayAdapter.TAG, "Unable to locate mode " + i + ", reverting to default.");
                i2 = this.mDefaultMode;
            }
            if (this.mActiveMode == i2) {
                return;
            }
            this.mActiveMode = i2;
            this.mInfo = null;
            OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(this, 2);
            onModeChangedLocked(i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class OverlayDisplayHandle implements OverlayDisplayWindow.Listener {
        private static final int DEFAULT_MODE_INDEX = 0;
        private OverlayDisplayDevice mDevice;
        private final OverlayFlags mFlags;
        private final int mGravity;
        private final List<OverlayMode> mModes;
        private final String mName;
        private final int mNumber;
        private OverlayDisplayWindow mWindow;
        private final Runnable mShowRunnable = new Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.2
            @Override // java.lang.Runnable
            public void run() {
                OverlayMode overlayMode = (OverlayMode) OverlayDisplayHandle.this.mModes.get(OverlayDisplayHandle.this.mActiveMode);
                OverlayDisplayWindow overlayDisplayWindow = new OverlayDisplayWindow(OverlayDisplayAdapter.this.getContext(), OverlayDisplayHandle.this.mName, overlayMode.mWidth, overlayMode.mHeight, overlayMode.mDensityDpi, OverlayDisplayHandle.this.mGravity, OverlayDisplayHandle.this.mFlags.mSecure, OverlayDisplayHandle.this);
                overlayDisplayWindow.show();
                synchronized (OverlayDisplayAdapter.this.getSyncRoot()) {
                    OverlayDisplayHandle.this.mWindow = overlayDisplayWindow;
                }
            }
        };
        private final Runnable mDismissRunnable = new Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.3
            @Override // java.lang.Runnable
            public void run() {
                OverlayDisplayWindow overlayDisplayWindow;
                synchronized (OverlayDisplayAdapter.this.getSyncRoot()) {
                    overlayDisplayWindow = OverlayDisplayHandle.this.mWindow;
                    OverlayDisplayHandle.this.mWindow = null;
                }
                if (overlayDisplayWindow != null) {
                    overlayDisplayWindow.dismiss();
                }
            }
        };
        private final Runnable mResizeRunnable = new Runnable() { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.4
            @Override // java.lang.Runnable
            public void run() {
                synchronized (OverlayDisplayAdapter.this.getSyncRoot()) {
                    if (OverlayDisplayHandle.this.mWindow == null) {
                        return;
                    }
                    OverlayMode overlayMode = (OverlayMode) OverlayDisplayHandle.this.mModes.get(OverlayDisplayHandle.this.mActiveMode);
                    OverlayDisplayHandle.this.mWindow.resize(overlayMode.mWidth, overlayMode.mHeight, overlayMode.mDensityDpi);
                }
            }
        };
        private int mActiveMode = 0;

        OverlayDisplayHandle(String str, List<OverlayMode> list, int i, OverlayFlags overlayFlags, int i2) {
            this.mName = str;
            this.mModes = list;
            this.mGravity = i;
            this.mFlags = overlayFlags;
            this.mNumber = i2;
            showLocked();
        }

        private void showLocked() {
            OverlayDisplayAdapter.this.mUiHandler.post(this.mShowRunnable);
        }

        public void dismissLocked() {
            OverlayDisplayAdapter.this.mUiHandler.removeCallbacks(this.mShowRunnable);
            OverlayDisplayAdapter.this.mUiHandler.post(this.mDismissRunnable);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onActiveModeChangedLocked(int i) {
            OverlayDisplayAdapter.this.mUiHandler.removeCallbacks(this.mResizeRunnable);
            this.mActiveMode = i;
            if (this.mWindow != null) {
                OverlayDisplayAdapter.this.mUiHandler.post(this.mResizeRunnable);
            }
        }

        @Override // com.android.server.display.OverlayDisplayWindow.Listener
        public void onWindowCreated(SurfaceTexture surfaceTexture, float f, long j, int i) {
            synchronized (OverlayDisplayAdapter.this.getSyncRoot()) {
                OverlayDisplayDevice overlayDisplayDevice = new OverlayDisplayDevice(DisplayControl.createDisplay(this.mName, this.mFlags.mSecure), this.mName, this.mModes, this.mActiveMode, 0, f, j, this.mFlags, i, surfaceTexture, this.mNumber) { // from class: com.android.server.display.OverlayDisplayAdapter.OverlayDisplayHandle.1
                    {
                        OverlayDisplayAdapter overlayDisplayAdapter = OverlayDisplayAdapter.this;
                    }

                    @Override // com.android.server.display.OverlayDisplayAdapter.OverlayDisplayDevice
                    public void onModeChangedLocked(int i2) {
                        OverlayDisplayHandle.this.onActiveModeChangedLocked(i2);
                    }
                };
                this.mDevice = overlayDisplayDevice;
                OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(overlayDisplayDevice, 1);
            }
        }

        @Override // com.android.server.display.OverlayDisplayWindow.Listener
        public void onWindowDestroyed() {
            synchronized (OverlayDisplayAdapter.this.getSyncRoot()) {
                OverlayDisplayDevice overlayDisplayDevice = this.mDevice;
                if (overlayDisplayDevice != null) {
                    overlayDisplayDevice.destroyLocked();
                    OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(this.mDevice, 3);
                }
            }
        }

        @Override // com.android.server.display.OverlayDisplayWindow.Listener
        public void onStateChanged(int i) {
            synchronized (OverlayDisplayAdapter.this.getSyncRoot()) {
                OverlayDisplayDevice overlayDisplayDevice = this.mDevice;
                if (overlayDisplayDevice != null) {
                    overlayDisplayDevice.setStateLocked(i);
                    OverlayDisplayAdapter.this.sendDisplayDeviceEventLocked(this.mDevice, 2);
                }
            }
        }

        public void dumpLocked(PrintWriter printWriter) {
            printWriter.println("  " + this.mName + ":");
            StringBuilder sb = new StringBuilder();
            sb.append("    mModes=");
            sb.append(Arrays.toString(this.mModes.toArray()));
            printWriter.println(sb.toString());
            printWriter.println("    mActiveMode=" + this.mActiveMode);
            printWriter.println("    mGravity=" + this.mGravity);
            printWriter.println("    mFlags=" + this.mFlags);
            printWriter.println("    mNumber=" + this.mNumber);
            if (this.mWindow != null) {
                IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "    ");
                indentingPrintWriter.increaseIndent();
                DumpUtils.dumpAsync(OverlayDisplayAdapter.this.mUiHandler, this.mWindow, indentingPrintWriter, "", 200L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class OverlayMode {
        final int mDensityDpi;
        final int mHeight;
        final int mWidth;

        OverlayMode(int i, int i2, int i3) {
            this.mWidth = i;
            this.mHeight = i2;
            this.mDensityDpi = i3;
        }

        public String toString() {
            return "{width=" + this.mWidth + ", height=" + this.mHeight + ", densityDpi=" + this.mDensityDpi + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class OverlayFlags {
        final boolean mOwnContentOnly;
        final boolean mSecure;
        final boolean mShouldShowSystemDecorations;

        OverlayFlags(boolean z, boolean z2, boolean z3) {
            this.mSecure = z;
            this.mOwnContentOnly = z2;
            this.mShouldShowSystemDecorations = z3;
        }

        static OverlayFlags parseFlags(String str) {
            if (TextUtils.isEmpty(str)) {
                return new OverlayFlags(false, false, false);
            }
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            for (String str2 : str.split(OverlayDisplayAdapter.FLAG_SPLITTER)) {
                if (OverlayDisplayAdapter.OVERLAY_DISPLAY_FLAG_SECURE.equals(str2)) {
                    z = true;
                }
                if (OverlayDisplayAdapter.OVERLAY_DISPLAY_FLAG_OWN_CONTENT_ONLY.equals(str2)) {
                    z2 = true;
                }
                if (OverlayDisplayAdapter.OVERLAY_DISPLAY_FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS.equals(str2)) {
                    z3 = true;
                }
            }
            return new OverlayFlags(z, z2, z3);
        }

        public String toString() {
            return "{secure=" + this.mSecure + ", ownContentOnly=" + this.mOwnContentOnly + ", shouldShowSystemDecorations=" + this.mShouldShowSystemDecorations + "}";
        }
    }
}
