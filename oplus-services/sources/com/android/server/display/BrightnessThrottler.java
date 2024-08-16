package com.android.server.display;

import android.hardware.display.BrightnessInfo;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IThermalEventListener;
import android.os.IThermalService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Temperature;
import android.provider.DeviceConfig;
import android.provider.DeviceConfigInterface;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.display.BrightnessThrottler;
import com.android.server.display.DisplayDeviceConfig;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BrightnessThrottler {
    private static final boolean DEBUG = false;
    private static final String TAG = "BrightnessThrottler";
    private static final int THROTTLING_INVALID = -1;
    private float mBrightnessCap;
    private int mBrightnessMaxReason;
    private HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData> mDdcThermalThrottlingDataMap;
    private final DeviceConfigInterface mDeviceConfig;
    private final Handler mDeviceConfigHandler;
    private final DeviceConfigListener mDeviceConfigListener;
    private final Handler mHandler;
    private final Injector mInjector;
    private final SkinThermalStatusObserver mSkinThermalStatusObserver;
    private String mThermalBrightnessThrottlingDataId;
    private final HashMap<String, HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData>> mThermalBrightnessThrottlingDataOverride;
    private String mThermalBrightnessThrottlingDataString;
    private DisplayDeviceConfig.ThermalBrightnessThrottlingData mThermalThrottlingData;
    private final Runnable mThrottlingChangeCallback;
    private int mThrottlingStatus;
    private String mUniqueDisplayId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BrightnessThrottler(Handler handler, Runnable runnable, String str, String str2, HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData> hashMap) {
        this(new Injector(), handler, handler, runnable, str, str2, hashMap);
    }

    @VisibleForTesting
    BrightnessThrottler(Injector injector, Handler handler, Handler handler2, Runnable runnable, String str, String str2, HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData> hashMap) {
        this.mBrightnessCap = 1.0f;
        this.mBrightnessMaxReason = 0;
        this.mThermalBrightnessThrottlingDataOverride = new HashMap<>(1);
        this.mInjector = injector;
        this.mHandler = handler;
        this.mDeviceConfigHandler = handler2;
        this.mDdcThermalThrottlingDataMap = hashMap;
        this.mThrottlingChangeCallback = runnable;
        this.mSkinThermalStatusObserver = new SkinThermalStatusObserver(injector, handler);
        this.mUniqueDisplayId = str;
        this.mDeviceConfig = injector.getDeviceConfig();
        this.mDeviceConfigListener = new DeviceConfigListener();
        this.mThermalBrightnessThrottlingDataId = str2;
        this.mDdcThermalThrottlingDataMap = hashMap;
        loadThermalBrightnessThrottlingDataFromDeviceConfig();
        loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(this.mDdcThermalThrottlingDataMap, this.mThermalBrightnessThrottlingDataId, this.mUniqueDisplayId);
    }

    boolean deviceSupportsThrottling() {
        return this.mThermalThrottlingData != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getBrightnessCap() {
        return this.mBrightnessCap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBrightnessMaxReason() {
        return this.mBrightnessMaxReason;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isThrottled() {
        return this.mBrightnessMaxReason != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        this.mSkinThermalStatusObserver.stopObserving();
        this.mDeviceConfig.removeOnPropertiesChangedListener(this.mDeviceConfigListener);
        this.mBrightnessCap = 1.0f;
        this.mBrightnessMaxReason = 0;
        this.mThrottlingStatus = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadThermalBrightnessThrottlingDataFromDisplayDeviceConfig(HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData> hashMap, String str, String str2) {
        this.mDdcThermalThrottlingDataMap = hashMap;
        this.mThermalBrightnessThrottlingDataId = str;
        this.mUniqueDisplayId = str2;
        resetThermalThrottlingData();
    }

    private float verifyAndConstrainBrightnessCap(float f) {
        if (f < 0.0f) {
            Slog.e(TAG, "brightness " + f + " is lower than the minimum possible brightness 0.0");
            f = 0.0f;
        }
        if (f <= 1.0f) {
            return f;
        }
        Slog.e(TAG, "brightness " + f + " is higher than the maximum possible brightness 1.0");
        return 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void thermalStatusChanged(int i) {
        if (this.mThrottlingStatus != i) {
            this.mThrottlingStatus = i;
            updateThermalThrottling();
        }
    }

    private void updateThermalThrottling() {
        DisplayDeviceConfig.ThermalBrightnessThrottlingData thermalBrightnessThrottlingData;
        if (deviceSupportsThrottling()) {
            float f = 1.0f;
            int i = 0;
            if (this.mThrottlingStatus != -1 && (thermalBrightnessThrottlingData = this.mThermalThrottlingData) != null) {
                for (DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel throttlingLevel : thermalBrightnessThrottlingData.throttlingLevels) {
                    if (throttlingLevel.thermalStatus > this.mThrottlingStatus) {
                        break;
                    }
                    f = throttlingLevel.brightness;
                    i = 1;
                }
            }
            if (this.mBrightnessCap == f && this.mBrightnessMaxReason == i) {
                return;
            }
            this.mBrightnessCap = verifyAndConstrainBrightnessCap(f);
            this.mBrightnessMaxReason = i;
            Runnable runnable = this.mThrottlingChangeCallback;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(final PrintWriter printWriter) {
        this.mHandler.runWithScissors(new Runnable() { // from class: com.android.server.display.BrightnessThrottler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BrightnessThrottler.this.lambda$dump$0(printWriter);
            }
        }, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$0(PrintWriter printWriter) {
        printWriter.println("BrightnessThrottler:");
        printWriter.println("  mThermalBrightnessThrottlingDataId=" + this.mThermalBrightnessThrottlingDataId);
        printWriter.println("  mThermalThrottlingData=" + this.mThermalThrottlingData);
        printWriter.println("  mUniqueDisplayId=" + this.mUniqueDisplayId);
        printWriter.println("  mThrottlingStatus=" + this.mThrottlingStatus);
        printWriter.println("  mBrightnessCap=" + this.mBrightnessCap);
        printWriter.println("  mBrightnessMaxReason=" + BrightnessInfo.briMaxReasonToString(this.mBrightnessMaxReason));
        printWriter.println("  mDdcThermalThrottlingDataMap=" + this.mDdcThermalThrottlingDataMap);
        printWriter.println("  mThermalBrightnessThrottlingDataOverride=" + this.mThermalBrightnessThrottlingDataOverride);
        printWriter.println("  mThermalBrightnessThrottlingDataString=" + this.mThermalBrightnessThrottlingDataString);
        this.mSkinThermalStatusObserver.dump(printWriter);
    }

    private String getThermalBrightnessThrottlingDataString() {
        return this.mDeviceConfig.getString("display_manager", "brightness_throttling_data", (String) null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean parseAndAddData(String str, HashMap<String, HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData>> hashMap) {
        int i;
        String str2;
        String[] split = str.split(",");
        int i2 = 1;
        try {
            String str3 = split[0];
            i = 2;
            try {
                int parseInt = Integer.parseInt(split[1]);
                ArrayList arrayList = new ArrayList(parseInt);
                int i3 = 0;
                while (i3 < parseInt) {
                    int i4 = i + 1;
                    try {
                        int i5 = i4 + 1;
                        try {
                            arrayList.add(new DisplayDeviceConfig.ThermalBrightnessThrottlingData.ThrottlingLevel(parseThermalStatus(split[i]), parseBrightness(split[i4])));
                            i3++;
                            i = i5;
                        } catch (UnknownThermalStatusException | IndexOutOfBoundsException | NumberFormatException e) {
                            e = e;
                            i2 = i5;
                            Slog.e(TAG, "Throttling data is invalid array: '" + str + "'", e);
                            i = i2;
                            i2 = 0;
                            if (i != split.length) {
                            }
                        }
                    } catch (UnknownThermalStatusException | IndexOutOfBoundsException | NumberFormatException e2) {
                        e = e2;
                        i2 = i4;
                    }
                }
                if (i < split.length) {
                    int i6 = i + 1;
                    try {
                        str2 = split[i];
                        i = i6;
                    } catch (UnknownThermalStatusException | IndexOutOfBoundsException | NumberFormatException e3) {
                        e = e3;
                        i2 = i6;
                        Slog.e(TAG, "Throttling data is invalid array: '" + str + "'", e);
                        i = i2;
                        i2 = 0;
                        if (i != split.length) {
                        }
                    }
                } else {
                    str2 = "default";
                }
                DisplayDeviceConfig.ThermalBrightnessThrottlingData create = DisplayDeviceConfig.ThermalBrightnessThrottlingData.create(arrayList);
                HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData> hashMap2 = hashMap.get(str3);
                if (hashMap2 == null) {
                    HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData> hashMap3 = new HashMap<>();
                    hashMap3.put(str2, create);
                    hashMap.put(str3, hashMap3);
                } else {
                    if (hashMap2.containsKey(str2)) {
                        Slog.e(TAG, "Throttling data for display " + str3 + "contains duplicate throttling ids: '" + str2 + "'");
                        return false;
                    }
                    hashMap2.put(str2, create);
                }
            } catch (UnknownThermalStatusException | IndexOutOfBoundsException | NumberFormatException e4) {
                e = e4;
                i2 = 2;
            }
        } catch (UnknownThermalStatusException | IndexOutOfBoundsException | NumberFormatException e5) {
            e = e5;
        }
        if (i != split.length) {
            return false;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadThermalBrightnessThrottlingDataFromDeviceConfig() {
        boolean z = true;
        HashMap<String, HashMap<String, DisplayDeviceConfig.ThermalBrightnessThrottlingData>> hashMap = new HashMap<>(1);
        this.mThermalBrightnessThrottlingDataString = getThermalBrightnessThrottlingDataString();
        this.mThermalBrightnessThrottlingDataOverride.clear();
        String str = this.mThermalBrightnessThrottlingDataString;
        if (str != null) {
            String[] split = str.split(";");
            int length = split.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (!parseAndAddData(split[i], hashMap)) {
                    z = false;
                    break;
                }
                i++;
            }
            if (z) {
                this.mThermalBrightnessThrottlingDataOverride.putAll(hashMap);
                hashMap.clear();
                return;
            }
            return;
        }
        Slog.w(TAG, "DeviceConfig ThermalBrightnessThrottlingData is null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetThermalThrottlingData() {
        stop();
        this.mDeviceConfigListener.startListening();
        this.mThermalThrottlingData = getConfigFromId(this.mThermalBrightnessThrottlingDataId);
        if (!"default".equals(this.mThermalBrightnessThrottlingDataId) && this.mThermalThrottlingData == null) {
            this.mThermalThrottlingData = getConfigFromId("default");
            Slog.d(TAG, "Falling back to default throttling Id");
        }
        if (deviceSupportsThrottling()) {
            this.mSkinThermalStatusObserver.startObserving();
        }
    }

    private DisplayDeviceConfig.ThermalBrightnessThrottlingData getConfigFromId(String str) {
        DisplayDeviceConfig.ThermalBrightnessThrottlingData thermalBrightnessThrottlingData = this.mThermalBrightnessThrottlingDataOverride.get(this.mUniqueDisplayId) == null ? null : this.mThermalBrightnessThrottlingDataOverride.get(this.mUniqueDisplayId).get(str);
        return thermalBrightnessThrottlingData == null ? this.mDdcThermalThrottlingDataMap.get(str) : thermalBrightnessThrottlingData;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class DeviceConfigListener implements DeviceConfig.OnPropertiesChangedListener {
        public Executor mExecutor;

        public DeviceConfigListener() {
            this.mExecutor = new HandlerExecutor(BrightnessThrottler.this.mDeviceConfigHandler);
        }

        public void startListening() {
            BrightnessThrottler.this.mDeviceConfig.addOnPropertiesChangedListener("display_manager", this.mExecutor, this);
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            BrightnessThrottler.this.loadThermalBrightnessThrottlingDataFromDeviceConfig();
            BrightnessThrottler.this.resetThermalThrottlingData();
        }
    }

    private float parseBrightness(String str) throws NumberFormatException {
        float parseFloat = Float.parseFloat(str);
        if (parseFloat < 0.0f || parseFloat > 1.0f) {
            throw new NumberFormatException("Brightness constraint value out of bounds.");
        }
        return parseFloat;
    }

    private int parseThermalStatus(String str) throws UnknownThermalStatusException {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -905723276:
                if (str.equals("severe")) {
                    c = 0;
                    break;
                }
                break;
            case -618857213:
                if (str.equals("moderate")) {
                    c = 1;
                    break;
                }
                break;
            case -169343402:
                if (str.equals("shutdown")) {
                    c = 2;
                    break;
                }
                break;
            case 3387192:
                if (str.equals("none")) {
                    c = 3;
                    break;
                }
                break;
            case 102970646:
                if (str.equals("light")) {
                    c = 4;
                    break;
                }
                break;
            case 1629013393:
                if (str.equals("emergency")) {
                    c = 5;
                    break;
                }
                break;
            case 1952151455:
                if (str.equals("critical")) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 3;
            case 1:
                return 2;
            case 2:
                return 6;
            case 3:
                return 0;
            case 4:
                return 1;
            case 5:
                return 5;
            case 6:
                return 4;
            default:
                throw new UnknownThermalStatusException("Invalid Thermal Status: " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class UnknownThermalStatusException extends Exception {
        UnknownThermalStatusException(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class SkinThermalStatusObserver extends IThermalEventListener.Stub {
        private final Handler mHandler;
        private final Injector mInjector;
        private boolean mStarted;
        private IThermalService mThermalService;

        SkinThermalStatusObserver(Injector injector, Handler handler) {
            this.mInjector = injector;
            this.mHandler = handler;
        }

        public void notifyThrottling(final Temperature temperature) {
            this.mHandler.post(new Runnable() { // from class: com.android.server.display.BrightnessThrottler$SkinThermalStatusObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BrightnessThrottler.SkinThermalStatusObserver.this.lambda$notifyThrottling$0(temperature);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$notifyThrottling$0(Temperature temperature) {
            BrightnessThrottler.this.thermalStatusChanged(temperature.getStatus());
        }

        void startObserving() {
            if (this.mStarted) {
                return;
            }
            IThermalService thermalService = this.mInjector.getThermalService();
            this.mThermalService = thermalService;
            if (thermalService == null) {
                Slog.e(BrightnessThrottler.TAG, "Could not observe thermal status. Service not available");
                return;
            }
            try {
                thermalService.registerThermalEventListenerWithType(this, 3);
                this.mStarted = true;
            } catch (RemoteException e) {
                Slog.e(BrightnessThrottler.TAG, "Failed to register thermal status listener", e);
            }
        }

        void stopObserving() {
            if (this.mStarted) {
                try {
                    this.mThermalService.unregisterThermalEventListener(this);
                    this.mStarted = false;
                } catch (RemoteException e) {
                    Slog.e(BrightnessThrottler.TAG, "Failed to unregister thermal status listener", e);
                }
                this.mThermalService = null;
            }
        }

        void dump(PrintWriter printWriter) {
            printWriter.println("  SkinThermalStatusObserver:");
            printWriter.println("    mStarted: " + this.mStarted);
            if (this.mThermalService != null) {
                printWriter.println("    ThermalService available");
            } else {
                printWriter.println("    ThermalService not available");
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class Injector {
        public IThermalService getThermalService() {
            return IThermalService.Stub.asInterface(ServiceManager.getService("thermalservice"));
        }

        public DeviceConfigInterface getDeviceConfig() {
            return DeviceConfigInterface.REAL;
        }
    }
}
