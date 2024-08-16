package com.oplus.thermalcontrol;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.util.ArraySet;
import b6.LocalLog;

/* loaded from: classes2.dex */
public class ThermalControlMonitor {
    private static final String CAMERA_FLASH_STATE_TORCH = "torch";
    private static final int CLEAR_LEVEL = 5;
    private static final String EXTRA_KEY_CAMERA_FLASH_STATE = "temperature_o_camera_flash_status";
    private static final String EXTRA_KEY_TEMPERATURE_CAMERA_OPEN = "temperature_o_camera_open";
    private static final String EXTRA_KEY_TEMPERATURE_CAMERA_VIDEO_SCENE = "temperature_o_camera_video_scene";
    private static final int FIVE_SECOND = 5000;
    private static final int HIGH_TEMP_CLOSE = 0;
    private static final int HIGH_TEMP_OPEN = 1;
    private static final int MSG_RESET_HIGHTEMP = 100;
    private static final int MSG_SET_HIGHTEMP = 101;
    private static final String TAG = "ThermalControlMonitor";
    private static volatile ThermalControlMonitor sThermalControlMonitor;
    private Handler mCameraHandler;
    private final CameraManager mCameraManager;
    private ContentObserver mCameraModelObserver;
    private ContentObserver mCameraVideoObserver;
    private final ContentResolver mContentResolver;
    private Context mContext;
    private ContentObserver mFlashStatsObserver;
    private ContentObserver mHighTempProtectObserver;
    private boolean mCameraOn = false;
    private boolean mCameraVideoOn = false;
    private ArraySet<String> mTorchOnCameraIds = new ArraySet<>();
    private boolean mFlashState = false;
    private boolean mRegister = false;
    private boolean mSendFlash = false;
    private CameraManager.TorchCallback mCallback = new CameraManager.TorchCallback() { // from class: com.oplus.thermalcontrol.ThermalControlMonitor.3
        @Override // android.hardware.camera2.CameraManager.TorchCallback
        public void onTorchModeChanged(String str, boolean z10) {
            boolean isTorchOn = ThermalControlMonitor.this.isTorchOn();
            if (z10) {
                ThermalControlMonitor.this.mTorchOnCameraIds.add(str);
            } else {
                ThermalControlMonitor.this.mTorchOnCameraIds.remove(str);
            }
            boolean isTorchOn2 = ThermalControlMonitor.this.isTorchOn();
            if (isTorchOn != isTorchOn2) {
                ThermalControlUtils.getInstance(ThermalControlMonitor.this.mContext).sendFlashTorch(isTorchOn2);
            }
        }
    };
    private Handler mHandler = new Handler() { // from class: com.oplus.thermalcontrol.ThermalControlMonitor.6
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i10 = message.what;
            if (i10 == 100) {
                Settings.System.putIntForUser(ThermalControlMonitor.this.mContext.getContentResolver(), "oplus_settings_hightemp_protect", 0, ThermalControlMonitor.this.mContext.getUserId());
                Settings.System.putIntForUser(ThermalControlMonitor.this.mContext.getContentResolver(), "oplus_settings_hightemp_protect", 0, 0);
            } else {
                if (i10 != 101) {
                    return;
                }
                Settings.System.putIntForUser(ThermalControlMonitor.this.mContext.getContentResolver(), "oplus_settings_hightemp_protect", 1, ThermalControlMonitor.this.mContext.getUserId());
                Settings.System.putIntForUser(ThermalControlMonitor.this.mContext.getContentResolver(), "oplus_settings_hightemp_protect", 1, 0);
            }
        }
    };

    public ThermalControlMonitor(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mCameraManager = (CameraManager) context.getSystemService(CameraManager.class);
        initCameraHandler();
        this.mCameraHandler.post(new Runnable() { // from class: com.oplus.thermalcontrol.a
            @Override // java.lang.Runnable
            public final void run() {
                ThermalControlMonitor.this.tryRegisterTorchCallback();
            }
        });
    }

    public static ThermalControlMonitor getInstance(Context context) {
        if (sThermalControlMonitor == null) {
            synchronized (ThermalControlMonitor.class) {
                if (sThermalControlMonitor == null) {
                    sThermalControlMonitor = new ThermalControlMonitor(context);
                }
            }
        }
        return sThermalControlMonitor;
    }

    private void initCameraHandler() {
        if (this.mCameraHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            this.mCameraHandler = new Handler(handlerThread.getLooper());
        }
    }

    private void registerCameraModelObserver() {
        this.mCameraModelObserver = new ContentObserver(this.mCameraHandler) { // from class: com.oplus.thermalcontrol.ThermalControlMonitor.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z10) {
                try {
                    ThermalControlMonitor thermalControlMonitor = ThermalControlMonitor.this;
                    thermalControlMonitor.mCameraOn = Settings.System.getIntForUser(thermalControlMonitor.mContentResolver, ThermalControlMonitor.EXTRA_KEY_TEMPERATURE_CAMERA_OPEN, -2) != 0;
                } catch (Settings.SettingNotFoundException unused) {
                    LocalLog.d(ThermalControlMonitor.TAG, "Camera mode settings is not found!");
                }
            }
        };
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor(EXTRA_KEY_TEMPERATURE_CAMERA_OPEN), false, this.mCameraModelObserver, -2);
    }

    private void registerFlashStatsObserver() {
        this.mFlashStatsObserver = new ContentObserver(this.mCameraHandler) { // from class: com.oplus.thermalcontrol.ThermalControlMonitor.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z10) {
                String stringForUser = Settings.System.getStringForUser(ThermalControlMonitor.this.mContentResolver, ThermalControlMonitor.EXTRA_KEY_CAMERA_FLASH_STATE, -2);
                if (stringForUser != null) {
                    ThermalControlMonitor.this.mFlashState = stringForUser.equals(ThermalControlMonitor.CAMERA_FLASH_STATE_TORCH);
                    if (ThermalControlMonitor.this.mFlashState) {
                        ThermalControlMonitor.this.mSendFlash = true;
                        ThermalControlUtils.getInstance(ThermalControlMonitor.this.mContext).sendFlashTorch(true);
                        return;
                    } else {
                        if (ThermalControlMonitor.this.mSendFlash) {
                            ThermalControlMonitor.this.mSendFlash = false;
                            ThermalControlUtils.getInstance(ThermalControlMonitor.this.mContext).sendFlashTorch(false);
                            return;
                        }
                        return;
                    }
                }
                ThermalControlMonitor.this.mFlashState = false;
            }
        };
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor(EXTRA_KEY_CAMERA_FLASH_STATE), false, this.mFlashStatsObserver, -2);
    }

    private void registerHighTempProtectObserver() {
        this.mHighTempProtectObserver = new ContentObserver(this.mCameraHandler) { // from class: com.oplus.thermalcontrol.ThermalControlMonitor.4
            @Override // android.database.ContentObserver
            public void onChange(boolean z10) {
                super.onChange(z10);
                if (ActivityManager.getCurrentUser() != ThermalControlMonitor.this.mContext.getUserId()) {
                    LocalLog.a(ThermalControlMonitor.TAG, "high temp change and user " + ThermalControlMonitor.this.mContext.getUserId() + " not foreground!");
                    return;
                }
                int intForUser = Settings.System.getIntForUser(ThermalControlMonitor.this.mContext.getContentResolver(), "oplus_settings_hightemp_protect", 0, 0);
                int appControlLevel = ThermalControlUtils.getInstance(ThermalControlMonitor.this.mContext).getAppControlLevel();
                LocalLog.a(ThermalControlMonitor.TAG, "SETTING_PROVIDER_KEY_HIGH_TEMPE_PROTECT change, status = " + intForUser + " level = " + appControlLevel);
                if (intForUser == 1 && appControlLevel != 5) {
                    ThermalControlMonitor.this.mHandler.sendEmptyMessageDelayed(100, 5000L);
                } else if (intForUser == 0 && appControlLevel == 5) {
                    ThermalControlMonitor.this.mHandler.sendEmptyMessageDelayed(101, 5000L);
                } else {
                    ThermalControlMonitor.this.mHandler.removeMessages(100);
                    ThermalControlMonitor.this.mHandler.removeMessages(101);
                }
            }
        };
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor("oplus_settings_hightemp_protect"), false, this.mHighTempProtectObserver, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryRegisterTorchCallback() {
        this.mCameraManager.registerTorchCallback(this.mCallback, this.mCameraHandler);
        try {
            boolean z10 = true;
            this.mCameraOn = Settings.System.getIntForUser(this.mContentResolver, EXTRA_KEY_TEMPERATURE_CAMERA_OPEN, -2) != 0;
            if (Settings.System.getIntForUser(this.mContentResolver, EXTRA_KEY_TEMPERATURE_CAMERA_VIDEO_SCENE, -2) == 0) {
                z10 = false;
            }
            this.mCameraVideoOn = z10;
        } catch (Settings.SettingNotFoundException unused) {
            LocalLog.d(TAG, "Camera mode settings is not found!");
        }
    }

    public void clearHighTempMsg() {
        this.mHandler.removeMessages(100);
        this.mHandler.removeMessages(101);
    }

    public boolean isOplusCameraOn() {
        return this.mCameraOn;
    }

    public boolean isOplusCameraVideoOn() {
        return this.mCameraVideoOn;
    }

    public boolean isTorchOn() {
        return this.mTorchOnCameraIds.size() > 0 || this.mFlashState;
    }

    public void startMonitor() {
        if (this.mRegister) {
            return;
        }
        registerCameraModelObserver();
        registerFlashStatsObserver();
        registerHighTempProtectObserver();
        this.mCameraVideoObserver = new ContentObserver(this.mCameraHandler) { // from class: com.oplus.thermalcontrol.ThermalControlMonitor.5
            @Override // android.database.ContentObserver
            public void onChange(boolean z10) {
                try {
                    ThermalControlMonitor thermalControlMonitor = ThermalControlMonitor.this;
                    thermalControlMonitor.mCameraVideoOn = Settings.System.getIntForUser(thermalControlMonitor.mContentResolver, ThermalControlMonitor.EXTRA_KEY_TEMPERATURE_CAMERA_VIDEO_SCENE, -2) != 0;
                } catch (Settings.SettingNotFoundException unused) {
                    LocalLog.d(ThermalControlMonitor.TAG, "Camera video settings is not found!");
                }
            }
        };
        this.mContentResolver.registerContentObserver(Settings.System.getUriFor(EXTRA_KEY_TEMPERATURE_CAMERA_VIDEO_SCENE), false, this.mCameraVideoObserver, -2);
        this.mRegister = true;
    }

    public void stopMonitor() {
        if (this.mRegister) {
            this.mContentResolver.unregisterContentObserver(this.mCameraModelObserver);
            this.mCameraManager.unregisterTorchCallback(this.mCallback);
            this.mContentResolver.unregisterContentObserver(this.mFlashStatsObserver);
            this.mContentResolver.unregisterContentObserver(this.mHighTempProtectObserver);
            this.mContentResolver.unregisterContentObserver(this.mCameraVideoObserver);
            this.mRegister = false;
        }
    }
}
