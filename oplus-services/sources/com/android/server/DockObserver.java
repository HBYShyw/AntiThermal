package com.android.server;

import android.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.hardware.audio.common.V2_0.AudioFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UEventObserver;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.ExtconUEventObserver;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class DockObserver extends SystemService {
    private static final int MSG_DOCK_STATE_CHANGED = 0;
    private static final String TAG = "DockObserver";
    private int mActualDockState;
    private final boolean mAllowTheaterModeWakeFromDock;
    private DeviceProvisionedObserver mDeviceProvisionedObserver;
    private final List<ExtconStateConfig> mExtconStateConfigs;
    private final ExtconUEventObserver mExtconUEventObserver;
    private final Handler mHandler;
    private final boolean mKeepDreamingWhenUnplugging;
    private final Object mLock;
    private final PowerManager mPowerManager;
    private int mPreviousDockState;
    private int mReportedDockState;
    private boolean mSystemReady;
    private boolean mUpdatesStopped;
    private final PowerManager.WakeLock mWakeLock;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ExtconStateProvider {
        private final Map<String, String> mState;

        ExtconStateProvider(Map<String, String> map) {
            this.mState = map;
        }

        String getValue(String str) {
            return this.mState.get(str);
        }

        static ExtconStateProvider fromString(String str) {
            HashMap hashMap = new HashMap();
            for (String str2 : str.split("\n")) {
                String[] split = str2.split("=");
                if (split.length == 2) {
                    hashMap.put(split[0], split[1]);
                } else {
                    Slog.e(DockObserver.TAG, "Invalid line: " + str2);
                }
            }
            return new ExtconStateProvider(hashMap);
        }

        static ExtconStateProvider fromFile(String str) {
            char[] cArr = new char[1024];
            try {
                FileReader fileReader = new FileReader(str);
                try {
                    ExtconStateProvider fromString = fromString(new String(cArr, 0, fileReader.read(cArr, 0, 1024)).trim());
                    fileReader.close();
                    return fromString;
                } finally {
                }
            } catch (FileNotFoundException unused) {
                Slog.w(DockObserver.TAG, "No state file found at: " + str);
                return new ExtconStateProvider(new HashMap());
            } catch (Exception e) {
                Slog.e(DockObserver.TAG, "", e);
                return new ExtconStateProvider(new HashMap());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static final class ExtconStateConfig {
        public final int extraStateValue;
        public final List<Pair<String, String>> keyValuePairs = new ArrayList();

        ExtconStateConfig(int i) {
            this.extraStateValue = i;
        }
    }

    private static List<ExtconStateConfig> loadExtconStateConfigs(Context context) {
        String[] stringArray = context.getResources().getStringArray(R.array.config_nonBlockableNotificationPackages);
        try {
            ArrayList arrayList = new ArrayList();
            for (String str : stringArray) {
                String[] split = str.split(",");
                ExtconStateConfig extconStateConfig = new ExtconStateConfig(Integer.parseInt(split[0]));
                for (int i = 1; i < split.length; i++) {
                    String[] split2 = split[i].split("=");
                    if (split2.length != 2) {
                        throw new IllegalArgumentException("Invalid key-value: " + split[i]);
                    }
                    extconStateConfig.keyValuePairs.add(Pair.create(split2[0], split2[1]));
                }
                arrayList.add(extconStateConfig);
            }
            return arrayList;
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            Slog.e(TAG, "Could not parse extcon state config", e);
            return new ArrayList();
        }
    }

    public DockObserver(Context context) {
        super(context);
        this.mLock = new Object();
        this.mActualDockState = 0;
        this.mReportedDockState = 0;
        this.mPreviousDockState = 0;
        Handler handler = new Handler(true) { // from class: com.android.server.DockObserver.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 0) {
                    return;
                }
                DockObserver.this.handleDockStateChange();
                DockObserver.this.mWakeLock.release();
            }
        };
        this.mHandler = handler;
        ExtconUEventObserver extconUEventObserver = new ExtconUEventObserver() { // from class: com.android.server.DockObserver.2
            @Override // com.android.server.ExtconUEventObserver
            public void onUEvent(ExtconUEventObserver.ExtconInfo extconInfo, UEventObserver.UEvent uEvent) {
                synchronized (DockObserver.this.mLock) {
                    String str = uEvent.get("STATE");
                    if (str != null) {
                        DockObserver.this.setDockStateFromProviderLocked(ExtconStateProvider.fromString(str));
                    } else {
                        Slog.e(DockObserver.TAG, "Extcon event missing STATE: " + uEvent);
                    }
                }
            }
        };
        this.mExtconUEventObserver = extconUEventObserver;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mPowerManager = powerManager;
        this.mWakeLock = powerManager.newWakeLock(1, TAG);
        this.mAllowTheaterModeWakeFromDock = context.getResources().getBoolean(R.bool.config_alwaysUseCdmaRssi);
        this.mKeepDreamingWhenUnplugging = context.getResources().getBoolean(17891729);
        this.mDeviceProvisionedObserver = new DeviceProvisionedObserver(handler);
        this.mExtconStateConfigs = loadExtconStateConfigs(context);
        List<ExtconUEventObserver.ExtconInfo> extconInfoForTypes = ExtconUEventObserver.ExtconInfo.getExtconInfoForTypes(new String[]{ExtconUEventObserver.ExtconInfo.EXTCON_DOCK});
        if (!extconInfoForTypes.isEmpty()) {
            ExtconUEventObserver.ExtconInfo extconInfo = extconInfoForTypes.get(0);
            Slog.i(TAG, "Found extcon info devPath: " + extconInfo.getDevicePath() + ", statePath: " + extconInfo.getStatePath());
            setDockStateFromProviderLocked(ExtconStateProvider.fromFile(extconInfo.getStatePath()));
            this.mPreviousDockState = this.mActualDockState;
            extconUEventObserver.startObserving(extconInfo);
            return;
        }
        Slog.i(TAG, "No extcon dock device found in this kernel.");
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(TAG, new BinderService());
        FrameworkStatsLog.write(FrameworkStatsLog.DOCK_STATE_CHANGED, this.mReportedDockState);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 550) {
            synchronized (this.mLock) {
                this.mSystemReady = true;
                this.mDeviceProvisionedObserver.onSystemReady();
                updateIfDockedLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIfDockedLocked() {
        if (this.mReportedDockState != 0) {
            updateLocked();
        }
    }

    private void setActualDockStateLocked(int i) {
        this.mActualDockState = i;
        if (this.mUpdatesStopped) {
            return;
        }
        setDockStateLocked(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDockStateLocked(int i) {
        if (i != this.mReportedDockState) {
            this.mReportedDockState = i;
            if (this.mSystemReady) {
                if (allowWakeFromDock()) {
                    this.mPowerManager.wakeUp(SystemClock.uptimeMillis(), "android.server:DOCK");
                }
                updateLocked();
            }
        }
    }

    private boolean allowWakeFromDock() {
        if (this.mKeepDreamingWhenUnplugging) {
            return false;
        }
        return this.mAllowTheaterModeWakeFromDock || Settings.Global.getInt(getContext().getContentResolver(), "theater_mode_on", 0) == 0;
    }

    private void updateLocked() {
        this.mWakeLock.acquire();
        this.mHandler.sendEmptyMessage(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a6 A[Catch: all -> 0x00e1, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x003b, B:7:0x0042, B:10:0x0044, B:13:0x0064, B:16:0x006f, B:22:0x00d6, B:23:0x00df, B:25:0x007e, B:34:0x00a6, B:36:0x00ac, B:38:0x00c3, B:40:0x00cd), top: B:3:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handleDockStateChange() {
        String str;
        String string;
        Uri parse;
        Ringtone ringtone;
        synchronized (this.mLock) {
            Slog.i(TAG, "Dock state changed from " + this.mPreviousDockState + " to " + this.mReportedDockState);
            int i = this.mPreviousDockState;
            this.mPreviousDockState = this.mReportedDockState;
            ContentResolver contentResolver = getContext().getContentResolver();
            if (!this.mDeviceProvisionedObserver.isDeviceProvisioned()) {
                Slog.i(TAG, "Device not provisioned, skipping dock broadcast");
                return;
            }
            Intent intent = new Intent("android.intent.action.DOCK_EVENT");
            intent.addFlags(AudioFormat.APTX);
            intent.putExtra("android.intent.extra.DOCK_STATE", this.mReportedDockState);
            boolean z = Settings.Global.getInt(contentResolver, "dock_sounds_enabled", 1) == 1;
            boolean z2 = Settings.Global.getInt(contentResolver, "dock_sounds_enabled_when_accessbility", 1) == 1;
            boolean z3 = Settings.Secure.getInt(contentResolver, "accessibility_enabled", 0) == 1;
            if (z || (z3 && z2)) {
                int i2 = this.mReportedDockState;
                if (i2 == 0) {
                    if (i != 1 && i != 3 && i != 4) {
                        if (i == 2) {
                            str = "car_undock_sound";
                            if (str != null && (string = Settings.Global.getString(contentResolver, str)) != null) {
                                parse = Uri.parse("file://" + string);
                                if (parse != null && (ringtone = RingtoneManager.getRingtone(getContext(), parse)) != null) {
                                    ringtone.setStreamType(1);
                                    ringtone.preferBuiltinDevice(true);
                                    ringtone.play();
                                }
                            }
                        }
                        str = null;
                        if (str != null) {
                            parse = Uri.parse("file://" + string);
                            if (parse != null) {
                                ringtone.setStreamType(1);
                                ringtone.preferBuiltinDevice(true);
                                ringtone.play();
                            }
                        }
                    }
                    str = "desk_undock_sound";
                    if (str != null) {
                    }
                } else {
                    if (i2 != 1 && i2 != 3 && i2 != 4) {
                        if (i2 == 2) {
                            str = "car_dock_sound";
                            if (str != null) {
                            }
                        }
                        str = null;
                        if (str != null) {
                        }
                    }
                    str = "desk_dock_sound";
                    if (str != null) {
                    }
                }
            }
            getContext().sendStickyBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    private int getDockedStateExtraValue(ExtconStateProvider extconStateProvider) {
        for (ExtconStateConfig extconStateConfig : this.mExtconStateConfigs) {
            boolean z = true;
            for (Pair<String, String> pair : extconStateConfig.keyValuePairs) {
                z = z && ((String) pair.second).equals(extconStateProvider.getValue((String) pair.first));
                if (!z) {
                    break;
                }
            }
            if (z) {
                return extconStateConfig.extraStateValue;
            }
        }
        return 1;
    }

    @VisibleForTesting
    void setDockStateFromProviderForTesting(ExtconStateProvider extconStateProvider) {
        synchronized (this.mLock) {
            setDockStateFromProviderLocked(extconStateProvider);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDockStateFromProviderLocked(ExtconStateProvider extconStateProvider) {
        setActualDockStateLocked("1".equals(extconStateProvider.getValue(ExtconUEventObserver.ExtconInfo.EXTCON_DOCK)) ? getDockedStateExtraValue(extconStateProvider) : 0);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    private final class BinderService extends Binder {
        private BinderService() {
        }

        @Override // android.os.Binder
        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(DockObserver.this.getContext(), DockObserver.TAG, printWriter)) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (DockObserver.this.mLock) {
                        if (strArr != null) {
                            if (strArr.length != 0 && !"-a".equals(strArr[0])) {
                                if (strArr.length == 3 && "set".equals(strArr[0])) {
                                    String str = strArr[1];
                                    String str2 = strArr[2];
                                    try {
                                        if ("state".equals(str)) {
                                            DockObserver.this.mUpdatesStopped = true;
                                            DockObserver.this.setDockStateLocked(Integer.parseInt(str2));
                                        } else {
                                            printWriter.println("Unknown set option: " + str);
                                        }
                                    } catch (NumberFormatException unused) {
                                        printWriter.println("Bad value: " + str2);
                                    }
                                } else if (strArr.length == 1 && "reset".equals(strArr[0])) {
                                    DockObserver.this.mUpdatesStopped = false;
                                    DockObserver dockObserver = DockObserver.this;
                                    dockObserver.setDockStateLocked(dockObserver.mActualDockState);
                                } else {
                                    printWriter.println("Dump current dock state, or:");
                                    printWriter.println("  set state <value>");
                                    printWriter.println("  reset");
                                }
                            }
                        }
                        printWriter.println("Current Dock Observer Service state:");
                        if (DockObserver.this.mUpdatesStopped) {
                            printWriter.println("  (UPDATES STOPPED -- use 'reset' to restart)");
                        }
                        printWriter.println("  reported state: " + DockObserver.this.mReportedDockState);
                        printWriter.println("  previous state: " + DockObserver.this.mPreviousDockState);
                        printWriter.println("  actual state: " + DockObserver.this.mActualDockState);
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class DeviceProvisionedObserver extends ContentObserver {
        private boolean mRegistered;

        public DeviceProvisionedObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            synchronized (DockObserver.this.mLock) {
                updateRegistration();
                if (isDeviceProvisioned()) {
                    DockObserver.this.updateIfDockedLocked();
                }
            }
        }

        void onSystemReady() {
            updateRegistration();
        }

        private void updateRegistration() {
            boolean z = !isDeviceProvisioned();
            if (z == this.mRegistered) {
                return;
            }
            ContentResolver contentResolver = DockObserver.this.getContext().getContentResolver();
            if (z) {
                contentResolver.registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this);
            } else {
                contentResolver.unregisterContentObserver(this);
            }
            this.mRegistered = z;
        }

        boolean isDeviceProvisioned() {
            return Settings.Global.getInt(DockObserver.this.getContext().getContentResolver(), "device_provisioned", 0) != 0;
        }
    }
}
