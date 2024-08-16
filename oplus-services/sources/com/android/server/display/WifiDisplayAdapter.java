package com.android.server.display;

import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.WifiDisplay;
import android.hardware.display.WifiDisplaySessionInfo;
import android.hardware.display.WifiDisplayStatus;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.UserHandle;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAddress;
import android.view.DisplayShape;
import android.view.Surface;
import android.view.SurfaceControl;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.display.DisplayAdapter;
import com.android.server.display.DisplayManagerService;
import com.android.server.display.WifiDisplayController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import system.ext.loader.core.ExtLoader;
import vendor.oplus.hardware.cwb.V1_0.ICwbService;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class WifiDisplayAdapter extends DisplayAdapter {
    private static final String ACTION_DISCONNECT = "android.server.display.wfd.DISCONNECT";
    private static final boolean DEBUG = false;
    private static final String DISPLAY_NAME_PREFIX = "wifi:";
    private static final int MSG_SEND_STATUS_CHANGE_BROADCAST = 1;
    private static final String TAG = "WifiDisplayAdapter";
    private long castTime;
    private WifiDisplay mActiveDisplay;
    private int mActiveDisplayState;
    private WifiDisplay[] mAvailableDisplays;
    private final BroadcastReceiver mBroadcastReceiver;
    private WifiDisplayStatus mCurrentStatus;
    private ICwbService mCwbService;
    private WifiDisplayController mDisplayController;
    private WifiDisplayDevice mDisplayDevice;
    private WifiDisplay[] mDisplays;
    private int mFeatureState;
    private final WifiDisplayHandler mHandler;
    private boolean mPendingStatusChangeBroadcast;
    private final PersistentDataStore mPersistentDataStore;
    private WifiDisplay[] mRememberedDisplays;
    private int mScanState;
    private WifiDisplaySessionInfo mSessionInfo;
    private final boolean mSupportsProtectedBuffers;
    private OplusWifiDisplayAdapterWrapper mWdaWrapper;
    private final WifiDisplayController.Listener mWifiDisplayListener;

    public WifiDisplayAdapter(DisplayManagerService.SyncRoot syncRoot, Context context, Handler handler, DisplayAdapter.Listener listener, PersistentDataStore persistentDataStore) {
        super(syncRoot, context, handler, listener, TAG);
        WifiDisplay[] wifiDisplayArr = WifiDisplay.EMPTY_ARRAY;
        this.mDisplays = wifiDisplayArr;
        this.mAvailableDisplays = wifiDisplayArr;
        this.mRememberedDisplays = wifiDisplayArr;
        this.mCwbService = null;
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.server.display.WifiDisplayAdapter.8
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent.getAction().equals(WifiDisplayAdapter.ACTION_DISCONNECT)) {
                    synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                        WifiDisplayAdapter.this.requestDisconnectLocked();
                    }
                }
            }
        };
        this.mWifiDisplayListener = new WifiDisplayController.Listener() { // from class: com.android.server.display.WifiDisplayAdapter.9
            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onFeatureStateChanged(int i) {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    if (WifiDisplayAdapter.this.mFeatureState != i) {
                        WifiDisplayAdapter.this.mFeatureState = i;
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onScanStarted() {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    if (WifiDisplayAdapter.this.mScanState != 1) {
                        WifiDisplayAdapter.this.mScanState = 1;
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                        WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState("MM_FB_EventID#25001#search_count#1");
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onScanResults(WifiDisplay[] wifiDisplayArr2) {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    WifiDisplay[] applyWifiDisplayAliases = WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAliases(wifiDisplayArr2);
                    boolean z = !Arrays.equals(WifiDisplayAdapter.this.mAvailableDisplays, applyWifiDisplayAliases);
                    for (int i = 0; !z && i < applyWifiDisplayAliases.length; i++) {
                        z = applyWifiDisplayAliases[i].canConnect() != WifiDisplayAdapter.this.mAvailableDisplays[i].canConnect();
                    }
                    if (z) {
                        WifiDisplayAdapter.this.mAvailableDisplays = applyWifiDisplayAliases;
                        WifiDisplayAdapter.this.fixRememberedDisplayNamesFromAvailableDisplaysLocked();
                        WifiDisplayAdapter.this.updateDisplaysLocked();
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onScanFinished() {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    if (WifiDisplayAdapter.this.mScanState != 0) {
                        WifiDisplayAdapter.this.mScanState = 0;
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayConnecting(WifiDisplay wifiDisplay) {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    WifiDisplay applyWifiDisplayAlias = WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAlias(wifiDisplay);
                    if (WifiDisplayAdapter.this.mActiveDisplayState != 1 || WifiDisplayAdapter.this.mActiveDisplay == null || !WifiDisplayAdapter.this.mActiveDisplay.equals(applyWifiDisplayAlias)) {
                        WifiDisplayAdapter.this.mActiveDisplayState = 1;
                        WifiDisplayAdapter.this.mActiveDisplay = applyWifiDisplayAlias;
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                        WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState("MM_FB_EventID#25002#connect_count#1#device_name#" + applyWifiDisplayAlias.getDeviceName());
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayConnectionFailed() {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    if (WifiDisplayAdapter.this.mActiveDisplayState != 0 || WifiDisplayAdapter.this.mActiveDisplay != null) {
                        WifiDisplayAdapter.this.mActiveDisplayState = 0;
                        WifiDisplayAdapter.this.mActiveDisplay = null;
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayConnected(WifiDisplay wifiDisplay, Surface surface, int i, int i2, int i3) {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    WifiDisplayAdapter.this.setCwbEnabled(false);
                    WifiDisplay applyWifiDisplayAlias = WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAlias(wifiDisplay);
                    WifiDisplayAdapter.this.addDisplayDeviceLocked(applyWifiDisplayAlias, surface, i, i2, i3);
                    if (WifiDisplayAdapter.this.mActiveDisplayState != 2 || WifiDisplayAdapter.this.mActiveDisplay == null || !WifiDisplayAdapter.this.mActiveDisplay.equals(applyWifiDisplayAlias)) {
                        WifiDisplayAdapter.this.mActiveDisplayState = 2;
                        WifiDisplayAdapter.this.mActiveDisplay = applyWifiDisplayAlias;
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                        WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState("MM_FB_EventID#25004#success_count#1");
                        WifiDisplayAdapter.this.castTime = System.currentTimeMillis();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplaySessionInfo(WifiDisplaySessionInfo wifiDisplaySessionInfo) {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    WifiDisplayAdapter.this.mSessionInfo = wifiDisplaySessionInfo;
                    WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayChanged(WifiDisplay wifiDisplay) {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    WifiDisplay applyWifiDisplayAlias = WifiDisplayAdapter.this.mPersistentDataStore.applyWifiDisplayAlias(wifiDisplay);
                    if (WifiDisplayAdapter.this.mActiveDisplay != null && WifiDisplayAdapter.this.mActiveDisplay.hasSameAddress(applyWifiDisplayAlias) && !WifiDisplayAdapter.this.mActiveDisplay.equals(applyWifiDisplayAlias)) {
                        WifiDisplayAdapter.this.mActiveDisplay = applyWifiDisplayAlias;
                        WifiDisplayAdapter.this.renameDisplayDeviceLocked(applyWifiDisplayAlias.getFriendlyDisplayName());
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                    }
                }
            }

            @Override // com.android.server.display.WifiDisplayController.Listener
            public void onDisplayDisconnected() {
                synchronized (WifiDisplayAdapter.this.getSyncRoot()) {
                    WifiDisplayAdapter.this.removeDisplayDeviceLocked();
                    WifiDisplayAdapter.this.setCwbEnabled(true);
                    if (WifiDisplayAdapter.this.mActiveDisplayState != 0 || WifiDisplayAdapter.this.mActiveDisplay != null) {
                        WifiDisplayAdapter.this.mActiveDisplayState = 0;
                        WifiDisplayAdapter.this.mActiveDisplay = null;
                        WifiDisplayAdapter.this.scheduleStatusChangedBroadcastLocked();
                        if (WifiDisplayAdapter.this.castTime != 0) {
                            long currentTimeMillis = System.currentTimeMillis();
                            WifiDisplayAdapter wifiDisplayAdapter = WifiDisplayAdapter.this;
                            wifiDisplayAdapter.castTime = (currentTimeMillis - wifiDisplayAdapter.castTime) / 1000;
                            WifiDisplayAdapter.this.mWdaWrapper.getExtImpl().reportWfdState("MM_FB_EventID#25003#disconnect_count#1#cast_time#" + WifiDisplayAdapter.this.castTime);
                            WifiDisplayAdapter.this.castTime = 0L;
                        }
                    }
                }
            }
        };
        this.mWdaWrapper = new OplusWifiDisplayAdapterWrapper();
        if (!context.getPackageManager().hasSystemFeature("android.hardware.wifi.direct")) {
            throw new RuntimeException("WiFi display was requested, but there is no WiFi Direct feature");
        }
        this.mHandler = new WifiDisplayHandler(handler.getLooper());
        this.mPersistentDataStore = persistentDataStore;
        this.mSupportsProtectedBuffers = context.getResources().getBoolean(17891909);
    }

    @Override // com.android.server.display.DisplayAdapter
    public void dumpLocked(PrintWriter printWriter) {
        super.dumpLocked(printWriter);
        printWriter.println("mCurrentStatus=" + getWifiDisplayStatusLocked());
        printWriter.println("mFeatureState=" + this.mFeatureState);
        printWriter.println("mScanState=" + this.mScanState);
        printWriter.println("mActiveDisplayState=" + this.mActiveDisplayState);
        printWriter.println("mActiveDisplay=" + this.mActiveDisplay);
        printWriter.println("mDisplays=" + Arrays.toString(this.mDisplays));
        printWriter.println("mAvailableDisplays=" + Arrays.toString(this.mAvailableDisplays));
        printWriter.println("mRememberedDisplays=" + Arrays.toString(this.mRememberedDisplays));
        printWriter.println("mPendingStatusChangeBroadcast=" + this.mPendingStatusChangeBroadcast);
        printWriter.println("mSupportsProtectedBuffers=" + this.mSupportsProtectedBuffers);
        if (this.mDisplayController == null) {
            printWriter.println("mDisplayController=null");
            return;
        }
        printWriter.println("mDisplayController:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        DumpUtils.dumpAsync(getHandler(), this.mDisplayController, indentingPrintWriter, "", 200L);
    }

    @Override // com.android.server.display.DisplayAdapter
    public void registerLocked() {
        super.registerLocked();
        updateRememberedDisplaysLocked();
        getHandler().post(new Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.1
            @Override // java.lang.Runnable
            public void run() {
                WifiDisplayAdapter.this.mDisplayController = new WifiDisplayController(WifiDisplayAdapter.this.getContext(), WifiDisplayAdapter.this.getHandler(), WifiDisplayAdapter.this.mWifiDisplayListener);
                WifiDisplayAdapter.this.getContext().registerReceiverAsUser(WifiDisplayAdapter.this.mBroadcastReceiver, UserHandle.ALL, new IntentFilter(WifiDisplayAdapter.ACTION_DISCONNECT), null, WifiDisplayAdapter.this.mHandler, 4);
            }
        });
    }

    public void requestStartScanLocked() {
        getHandler().post(new Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.2
            @Override // java.lang.Runnable
            public void run() {
                if (WifiDisplayAdapter.this.mDisplayController != null) {
                    WifiDisplayAdapter.this.mDisplayController.requestStartScan();
                }
            }
        });
    }

    public void requestStopScanLocked() {
        getHandler().post(new Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.3
            @Override // java.lang.Runnable
            public void run() {
                if (WifiDisplayAdapter.this.mDisplayController != null) {
                    WifiDisplayAdapter.this.mDisplayController.requestStopScan();
                }
            }
        });
    }

    public void requestConnectLocked(final String str) {
        getHandler().post(new Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.4
            @Override // java.lang.Runnable
            public void run() {
                if (WifiDisplayAdapter.this.mDisplayController != null) {
                    WifiDisplayAdapter.this.mDisplayController.requestConnect(str);
                }
            }
        });
    }

    public void requestPauseLocked() {
        getHandler().post(new Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.5
            @Override // java.lang.Runnable
            public void run() {
                if (WifiDisplayAdapter.this.mDisplayController != null) {
                    WifiDisplayAdapter.this.mDisplayController.requestPause();
                }
            }
        });
    }

    public void requestResumeLocked() {
        getHandler().post(new Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.6
            @Override // java.lang.Runnable
            public void run() {
                if (WifiDisplayAdapter.this.mDisplayController != null) {
                    WifiDisplayAdapter.this.mDisplayController.requestResume();
                }
            }
        });
    }

    public void requestDisconnectLocked() {
        getHandler().post(new Runnable() { // from class: com.android.server.display.WifiDisplayAdapter.7
            @Override // java.lang.Runnable
            public void run() {
                if (WifiDisplayAdapter.this.mDisplayController != null) {
                    WifiDisplayAdapter.this.mDisplayController.requestDisconnect();
                }
            }
        });
    }

    public void requestRenameLocked(String str, String str2) {
        if (str2 != null) {
            str2 = str2.trim();
            if (str2.isEmpty() || str2.equals(str)) {
                str2 = null;
            }
        }
        String str3 = str2;
        WifiDisplay rememberedWifiDisplay = this.mPersistentDataStore.getRememberedWifiDisplay(str);
        if (rememberedWifiDisplay != null && !Objects.equals(rememberedWifiDisplay.getDeviceAlias(), str3)) {
            if (this.mPersistentDataStore.rememberWifiDisplay(new WifiDisplay(str, rememberedWifiDisplay.getDeviceName(), str3, false, false, false))) {
                this.mPersistentDataStore.saveIfNeeded();
                updateRememberedDisplaysLocked();
                scheduleStatusChangedBroadcastLocked();
            }
        }
        WifiDisplay wifiDisplay = this.mActiveDisplay;
        if (wifiDisplay == null || !wifiDisplay.getDeviceAddress().equals(str)) {
            return;
        }
        renameDisplayDeviceLocked(this.mActiveDisplay.getFriendlyDisplayName());
    }

    public void requestForgetLocked(String str) {
        if (this.mPersistentDataStore.forgetWifiDisplay(str)) {
            this.mPersistentDataStore.saveIfNeeded();
            updateRememberedDisplaysLocked();
            scheduleStatusChangedBroadcastLocked();
        }
        WifiDisplay wifiDisplay = this.mActiveDisplay;
        if (wifiDisplay == null || !wifiDisplay.getDeviceAddress().equals(str)) {
            return;
        }
        requestDisconnectLocked();
    }

    public WifiDisplayStatus getWifiDisplayStatusLocked() {
        if (this.mCurrentStatus == null) {
            this.mCurrentStatus = new WifiDisplayStatus(this.mFeatureState, this.mScanState, this.mActiveDisplayState, this.mActiveDisplay, this.mDisplays, this.mSessionInfo);
        }
        return this.mCurrentStatus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDisplaysLocked() {
        boolean z;
        ArrayList arrayList = new ArrayList(this.mAvailableDisplays.length + this.mRememberedDisplays.length);
        boolean[] zArr = new boolean[this.mAvailableDisplays.length];
        int i = 0;
        for (WifiDisplay wifiDisplay : this.mRememberedDisplays) {
            int i2 = 0;
            while (true) {
                WifiDisplay[] wifiDisplayArr = this.mAvailableDisplays;
                if (i2 >= wifiDisplayArr.length) {
                    z = false;
                    break;
                } else {
                    if (wifiDisplay.equals(wifiDisplayArr[i2])) {
                        z = true;
                        zArr[i2] = true;
                        break;
                    }
                    i2++;
                }
            }
            if (!z) {
                arrayList.add(new WifiDisplay(wifiDisplay.getDeviceAddress(), wifiDisplay.getDeviceName(), wifiDisplay.getDeviceAlias(), false, false, true));
            }
        }
        while (true) {
            WifiDisplay[] wifiDisplayArr2 = this.mAvailableDisplays;
            if (i < wifiDisplayArr2.length) {
                WifiDisplay wifiDisplay2 = wifiDisplayArr2[i];
                arrayList.add(new WifiDisplay(wifiDisplay2.getDeviceAddress(), wifiDisplay2.getDeviceName(), wifiDisplay2.getDeviceAlias(), true, wifiDisplay2.canConnect(), zArr[i]));
                i++;
            } else {
                this.mDisplays = (WifiDisplay[]) arrayList.toArray(WifiDisplay.EMPTY_ARRAY);
                return;
            }
        }
    }

    private void updateRememberedDisplaysLocked() {
        this.mRememberedDisplays = this.mPersistentDataStore.getRememberedWifiDisplays();
        this.mActiveDisplay = this.mPersistentDataStore.applyWifiDisplayAlias(this.mActiveDisplay);
        this.mAvailableDisplays = this.mPersistentDataStore.applyWifiDisplayAliases(this.mAvailableDisplays);
        updateDisplaysLocked();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fixRememberedDisplayNamesFromAvailableDisplaysLocked() {
        int i = 0;
        boolean z = false;
        while (true) {
            WifiDisplay[] wifiDisplayArr = this.mRememberedDisplays;
            if (i >= wifiDisplayArr.length) {
                break;
            }
            WifiDisplay wifiDisplay = wifiDisplayArr[i];
            WifiDisplay findAvailableDisplayLocked = findAvailableDisplayLocked(wifiDisplay.getDeviceAddress());
            if (findAvailableDisplayLocked != null && !wifiDisplay.equals(findAvailableDisplayLocked)) {
                this.mRememberedDisplays[i] = findAvailableDisplayLocked;
                z |= this.mPersistentDataStore.rememberWifiDisplay(findAvailableDisplayLocked);
            }
            i++;
        }
        if (z) {
            this.mPersistentDataStore.saveIfNeeded();
        }
    }

    private WifiDisplay findAvailableDisplayLocked(String str) {
        for (WifiDisplay wifiDisplay : this.mAvailableDisplays) {
            if (wifiDisplay.getDeviceAddress().equals(str)) {
                return wifiDisplay;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addDisplayDeviceLocked(WifiDisplay wifiDisplay, Surface surface, int i, int i2, int i3) {
        int i4;
        IBinder createDisplay;
        removeDisplayDeviceLocked();
        if (this.mPersistentDataStore.rememberWifiDisplay(wifiDisplay)) {
            this.mPersistentDataStore.saveIfNeeded();
            updateRememberedDisplaysLocked();
            scheduleStatusChangedBroadcastLocked();
        }
        boolean z = (i3 & 1) != 0;
        if (z) {
            i4 = this.mSupportsProtectedBuffers ? 76 : 68;
        } else {
            i4 = 64;
        }
        int i5 = i4;
        String friendlyDisplayName = wifiDisplay.getFriendlyDisplayName();
        String deviceAddress = wifiDisplay.getDeviceAddress();
        if (Build.isMtkPlatform()) {
            createDisplay = DisplayControl.createDisplay(friendlyDisplayName + "isWifiDpyForHWC", z);
        } else {
            createDisplay = DisplayControl.createDisplay(friendlyDisplayName, z);
        }
        WifiDisplayDevice wifiDisplayDevice = new WifiDisplayDevice(createDisplay, friendlyDisplayName, i, i2, 60.0f, i5, deviceAddress, surface);
        this.mDisplayDevice = wifiDisplayDevice;
        sendDisplayDeviceEventLocked(wifiDisplayDevice, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDisplayDeviceLocked() {
        WifiDisplayDevice wifiDisplayDevice = this.mDisplayDevice;
        if (wifiDisplayDevice != null) {
            wifiDisplayDevice.destroyLocked();
            sendDisplayDeviceEventLocked(this.mDisplayDevice, 3);
            this.mDisplayDevice = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renameDisplayDeviceLocked(String str) {
        WifiDisplayDevice wifiDisplayDevice = this.mDisplayDevice;
        if (wifiDisplayDevice == null || wifiDisplayDevice.getNameLocked().equals(str)) {
            return;
        }
        this.mDisplayDevice.setNameLocked(str);
        sendDisplayDeviceEventLocked(this.mDisplayDevice, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleStatusChangedBroadcastLocked() {
        this.mCurrentStatus = null;
        if (this.mPendingStatusChangeBroadcast) {
            return;
        }
        this.mPendingStatusChangeBroadcast = true;
        this.mHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendStatusChangeBroadcast() {
        synchronized (getSyncRoot()) {
            if (this.mPendingStatusChangeBroadcast) {
                this.mPendingStatusChangeBroadcast = false;
                Intent intent = new Intent("android.hardware.display.action.WIFI_DISPLAY_STATUS_CHANGED");
                intent.addFlags(1073741824);
                intent.putExtra("android.hardware.display.extra.WIFI_DISPLAY_STATUS", (Parcelable) getWifiDisplayStatusLocked());
                BroadcastOptions makeBasic = BroadcastOptions.makeBasic();
                makeBasic.setDeliveryGroupPolicy(1);
                getContext().sendBroadcastAsUser(intent, UserHandle.ALL, null, makeBasic.toBundle());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class WifiDisplayDevice extends DisplayDevice {
        private final DisplayAddress mAddress;
        private final int mFlags;
        private final int mHeight;
        private DisplayDeviceInfo mInfo;
        private final Display.Mode mMode;
        private String mName;
        private final float mRefreshRate;
        private Surface mSurface;
        private final int mWidth;

        @Override // com.android.server.display.DisplayDevice
        public boolean hasStableUniqueId() {
            return true;
        }

        public WifiDisplayDevice(IBinder iBinder, String str, int i, int i2, float f, int i3, String str2, Surface surface) {
            super(WifiDisplayAdapter.this, iBinder, WifiDisplayAdapter.DISPLAY_NAME_PREFIX + str2, WifiDisplayAdapter.this.getContext());
            this.mName = str;
            this.mWidth = i;
            this.mHeight = i2;
            this.mRefreshRate = f;
            this.mFlags = i3;
            this.mAddress = DisplayAddress.fromMacAddress(str2);
            this.mSurface = surface;
            this.mMode = DisplayAdapter.createMode(i, i2, f);
        }

        public void destroyLocked() {
            Surface surface = this.mSurface;
            if (surface != null) {
                surface.release();
                this.mSurface = null;
            }
            DisplayControl.destroyDisplay(getDisplayTokenLocked());
        }

        public void setNameLocked(String str) {
            this.mName = str;
            this.mInfo = null;
        }

        @Override // com.android.server.display.DisplayDevice
        public void performTraversalLocked(SurfaceControl.Transaction transaction) {
            if (this.mSurface == null || this.mDisplayDeviceExt.getMirageSetSurfaceNull()) {
                return;
            }
            setSurfaceLocked(transaction, this.mSurface);
        }

        @Override // com.android.server.display.DisplayDevice
        public DisplayDeviceInfo getDisplayDeviceInfoLocked() {
            if (this.mInfo == null) {
                DisplayDeviceInfo displayDeviceInfo = new DisplayDeviceInfo();
                this.mInfo = displayDeviceInfo;
                displayDeviceInfo.name = this.mName;
                displayDeviceInfo.uniqueId = getUniqueId();
                DisplayDeviceInfo displayDeviceInfo2 = this.mInfo;
                displayDeviceInfo2.width = this.mWidth;
                displayDeviceInfo2.height = this.mHeight;
                displayDeviceInfo2.modeId = this.mMode.getModeId();
                this.mInfo.renderFrameRate = this.mMode.getRefreshRate();
                this.mInfo.defaultModeId = this.mMode.getModeId();
                DisplayDeviceInfo displayDeviceInfo3 = this.mInfo;
                displayDeviceInfo3.supportedModes = new Display.Mode[]{this.mMode};
                displayDeviceInfo3.presentationDeadlineNanos = 1000000000 / ((int) this.mRefreshRate);
                displayDeviceInfo3.flags = this.mFlags;
                displayDeviceInfo3.type = 3;
                displayDeviceInfo3.address = this.mAddress;
                displayDeviceInfo3.touch = 2;
                displayDeviceInfo3.setAssumedDensityForExternalDisplay(this.mWidth, this.mHeight);
                DisplayDeviceInfo displayDeviceInfo4 = this.mInfo;
                displayDeviceInfo4.flags |= 8192;
                displayDeviceInfo4.displayShape = DisplayShape.createDefaultDisplayShape(displayDeviceInfo4.width, displayDeviceInfo4.height, false);
            }
            return this.mInfo;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class WifiDisplayHandler extends Handler {
        public WifiDisplayHandler(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            WifiDisplayAdapter.this.handleSendStatusChangeBroadcast();
        }
    }

    void setCwbEnabled(boolean z) {
        try {
            ICwbService service = ICwbService.getService();
            this.mCwbService = service;
            if (service != null) {
                try {
                    if (z) {
                        service.enable();
                    } else {
                        service.disable();
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e) {
                            Slog.e(TAG, "Failed to wait CWB done" + e.toString());
                        }
                    }
                } catch (Exception e2) {
                    Slog.e(TAG, "Failed to enable/disable Cwb function" + e2.toString());
                }
            }
        } catch (Exception unused) {
            Slog.i(TAG, "No Cwb service fetched.");
        }
    }

    public IOplusWifiDisplayAdapterWrapper getWrapper() {
        return this.mWdaWrapper;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private class OplusWifiDisplayAdapterWrapper implements IOplusWifiDisplayAdapterWrapper {
        private IOplusWifiDisplayAdapterExt mWfdAdapterExt;

        private OplusWifiDisplayAdapterWrapper() {
            this.mWfdAdapterExt = (IOplusWifiDisplayAdapterExt) ExtLoader.type(IOplusWifiDisplayAdapterExt.class).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public IOplusWifiDisplayAdapterExt getExtImpl() {
            return this.mWfdAdapterExt;
        }
    }
}
