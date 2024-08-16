package com.aiunit.aon;

import android.app.ActivityManagerNative;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.provider.oplus.Telephony;
import android.util.Log;
import com.aiunit.aon.utils.IAONEventListener;
import com.aiunit.aon.utils.IAONService;
import com.aiunit.aon.utils.core.FaceInfo;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes.dex */
public class AonSmartRotation {
    private static final int ACCEPT_STATUS_NUM = 5;
    private static final int AON_FACE_DIRECTION_ENGINE = 393218;
    private static final int AON_LOW_LIGHT = 10000;
    private static final String AON_PKG_NAME = "com.aiunit.aon";
    private static final String AON_SERVICE = "com.aiunit.aon.AONservice";
    private static final int CLOSE_FUNCTION_TIME_GAP = 20;
    private static final String CLS_NAME = "com.aiunit.aon.AONService";
    public static final int DEFAULT_FACE_ANGLE = -2;
    private static final int DEFAULT_TIME_GAP = 400;
    private static final int ERR_LAUNCH_CAMERA_ONE = 20512;
    private static final int ERR_REMOTE_EXCEPTION = 4099;
    private static final float FACE_ORIENTATION_0 = 0.0f;
    private static final float FACE_ORIENTATION_1 = 1.0f;
    private static final float FACE_ORIENTATION_2 = 2.0f;
    private static final float FACE_ORIENTATION_3 = 3.0f;
    private static final int LANDSCAPE_BOUNDARY_LEFT = 120;
    private static final int LANDSCAPE_BOUNDARY_RIGHT = 240;
    private static final int LAY_DOWN = 4;
    private static final int LEGAL_BOUNDARY_LEFT = 0;
    private static final int LEGAL_BOUNDARY_RIGHT = 360;
    private static final int LOW_LIGHT_TIME_GAP = 100;
    private static final int MAX_ORIENTATION_STATUS = 9;
    private static final int MAX_TIME_GAP = 1000;
    private static final int MIN_TIME_GAP = 0;
    private static final int MSG_INIT_SUCCESS = 10001;
    private static final int MSG_REGISTER = 10003;
    private static final int MSG_START_CAMERA = 10005;
    private static final int MSG_STOP_CAMERA = 10006;
    private static final int MSG_TESTALL = 10002;
    private static final int MSG_UNREGISTER = 10004;
    private static final int NORMAL_PORTRAIT = 0;
    private static final int NORMAL_PORTRAIT_BOUNDARY_LEFT = 60;
    private static final int NORMAL_PORTRAIT_BOUNDARY_RIGHT = 300;
    private static final long PRE_OPEN_STATUS_TIME_GAP = 800;
    private static final float PRE_STATUS_ONE = 6.0f;
    private static final float PRE_STATUS_THREE = 8.0f;
    private static final float PRE_STATUS_TWO = 7.0f;
    private static final String SMART_ROTATION_ENABLE_SWITCH = "persist.sys.oplus.smartrotation";
    private static final String SMART_ROTATION_TAG = "SmartRotationDebug";
    private static final String SMART_ROTATION_TESTEVENT = "com.aon.smartrotation.testevent";
    private static final String SMART_ROTATION_TESTMODE_STATUS = "sys.oplus.smartrotation.testmode";
    private static final String SMART_ROTATION_TIME_GAP = "persist.sys.oplus.smartrotation.timegap";
    private static final String WMS_LISTENER = "com.android.server.policy.WindowOrientationListener$OrientationSensorJudge";
    private static ServiceConnection sServiceConnection;
    private static Handler sSmartRotationHandler;
    private static HandlerThread sSmartRotationHandlerThread;
    private static Looper sSmartRotationLooper;
    private Context mContext;
    private IntentFilter mSmartRotationFilter;
    private UserManager mUserManager;
    public static int sTestFaceAngle = -2;
    public static float sTestOrientationHardwareValue = -1.0f;
    public static float sTestOrientationResult = 0.0f;
    public static int sFaceAngle = -2;
    public static int stuckInPreStatusCount = 0;
    public static ArrayList<Integer> stopCameraErrorCodes = new ArrayList<>(Arrays.asList(20513, 24577, 24578, 24579, 24580, 24581, 24582, 24583, 24624, 24625));
    public static boolean sIsCameraOn = false;
    public static int sAonSmartRotationConnectionCount = 0;
    private static float sOrientationResult = 0.0f;
    private static long sPreOpenCameraTime = 0;
    private static long sPreOpenEventFetchTime = 0;
    private static long sNormalStatusTime = 0;
    private static long sReceiveEventTime = 0;
    private static boolean DEBUG_CONFIGURATION = false;
    private static boolean sIsWaitingFaceAngle = false;
    private static boolean sLaunchCameraAhead = false;
    private static boolean sIsTestMode = false;
    private static boolean sIsLowLight = false;
    private static IAONService sAONService = null;
    private static boolean sCanUseSmartRotation = false;
    private static int sFaceEventTimeGap = 400;
    private static int sCurrentUserId = 0;
    private static IAONEventListener sAonlistener = new IAONEventListener.Stub() { // from class: com.aiunit.aon.AonSmartRotation.2
        @Override // com.aiunit.aon.utils.IAONEventListener
        public void onEvent(int eventType, int event) throws RemoteException {
            AonSmartRotation.sReceiveEventTime = System.currentTimeMillis();
            if (event == 10000) {
                AonSmartRotation.sIsLowLight = true;
                AonSmartRotation.printDetailLog("AON_LOW_LIGHT, keep waiting for second value.");
                return;
            }
            if (AonSmartRotation.sIsLowLight) {
                AonSmartRotation.sIsLowLight = false;
            }
            AonSmartRotation.printDetailLog("nash_debug, AON face angle event = " + event + ", sReceiveEventTime: " + AonSmartRotation.sReceiveEventTime);
            if (AonSmartRotation.sIsWaitingFaceAngle) {
                if (AonSmartRotation.sLaunchCameraAhead) {
                    AonSmartRotation.sLaunchCameraAhead = false;
                }
                AonSmartRotation.sFaceAngle = event;
                if (AonSmartRotation.isErrorCodeNeedStopCamera(AonSmartRotation.sFaceAngle)) {
                    Log.w(AonSmartRotation.SMART_ROTATION_TAG, "Receive AON error code:" + AonSmartRotation.sFaceAngle + " here, reset it as default value.");
                    AonSmartRotation.sFaceAngle = -2;
                }
                AonSmartRotation.releaseCamera();
                Log.d(AonSmartRotation.SMART_ROTATION_TAG, "nash_debug, get the sFaceAngle:" + AonSmartRotation.sFaceAngle + ", ready to compare with hardware value.");
            } else if (AonSmartRotation.sLaunchCameraAhead) {
                AonSmartRotation.sPreOpenEventFetchTime = System.currentTimeMillis();
                if (AonSmartRotation.sPreOpenEventFetchTime < AonSmartRotation.sPreOpenCameraTime) {
                    Log.w(AonSmartRotation.SMART_ROTATION_TAG, "Two continuous pre-status here, it is dangerous, notice this.");
                } else if (AonSmartRotation.sPreOpenEventFetchTime - AonSmartRotation.sPreOpenCameraTime >= AonSmartRotation.PRE_OPEN_STATUS_TIME_GAP) {
                    AonSmartRotation.stuckInPreStatusCount++;
                    Log.w(AonSmartRotation.SMART_ROTATION_TAG, "We may stuck in a pre-status, release the AON camera to save power. The counts of stuck pre-status is " + AonSmartRotation.stuckInPreStatusCount);
                    AonSmartRotation.sFaceAngle = -2;
                    AonSmartRotation.releaseCamera();
                } else {
                    AonSmartRotation.printDetailLog("nash_debug, Open camera ahead, early data here, we may use next dace data, keep camera on.");
                }
            } else {
                AonSmartRotation.sFaceAngle = -2;
                AonSmartRotation.printDetailLog("nash_debug, We are not waiting, reset sFaceAngle to default.");
                AonSmartRotation.releaseCamera();
            }
            if (AonSmartRotation.sPreOpenCameraTime == 0) {
                if (AonSmartRotation.sNormalStatusTime == 0) {
                    AonSmartRotation.printDetailLog("nash_debug, Time Status, Never receive a normal status, ignore this time.");
                    return;
                }
                long currentTimeGap = AonSmartRotation.sReceiveEventTime - AonSmartRotation.sNormalStatusTime;
                if (currentTimeGap < 0) {
                    AonSmartRotation.printDetailLog("nash_debug, Time Status, AON event faster than normal status, we see currentTimeGap as 0.");
                    return;
                } else {
                    AonSmartRotation.printDetailLog("nash_debug, Time Status, currentTimeGap is " + currentTimeGap + ".");
                    return;
                }
            }
            if (AonSmartRotation.sPreOpenCameraTime >= AonSmartRotation.sReceiveEventTime) {
                AonSmartRotation.printDetailLog("nash_debug, Time Status, We may receive more than one prestatus, ignore this time.");
                return;
            }
            if (AonSmartRotation.sNormalStatusTime == 0) {
                AonSmartRotation.printDetailLog("nash_debug, Time Status, Never receive a normal status, ignore this time.");
                return;
            }
            long firstPicTime = AonSmartRotation.sReceiveEventTime - AonSmartRotation.sPreOpenCameraTime;
            long currentTimeGap2 = AonSmartRotation.sReceiveEventTime - AonSmartRotation.sNormalStatusTime;
            if (currentTimeGap2 < 0) {
                AonSmartRotation.printDetailLog("nash_debug, Time Status, AON event faster than normal status, we see currentTimeGap as 0, firstPicTime is " + firstPicTime + ".");
            } else {
                AonSmartRotation.printDetailLog("nash_debug, Time Status, currentTimeGap is " + currentTimeGap2 + ", firstPicTime is " + firstPicTime + ".");
            }
        }

        @Override // com.aiunit.aon.utils.IAONEventListener
        public void onEventParam(int eventType, int event, FaceInfo faceInfo) throws RemoteException {
        }
    };
    private static IBinder.DeathRecipient sDeathRecipient = new IBinder.DeathRecipient() { // from class: com.aiunit.aon.AonSmartRotation.3
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.w(AonSmartRotation.SMART_ROTATION_TAG, "AON binderDied");
            AonSmartRotation.sAONService.asBinder().unlinkToDeath(AonSmartRotation.sDeathRecipient, 0);
            AonSmartRotation.sAonSmartRotationConnectionCount = 0;
            AonSmartRotation.sAONService = null;
        }
    };
    private int mSwitchValue = 0;
    private int mAutoRotationSwitchValue = 0;
    private int mSmartRotationSwitchValue = 0;
    private BroadcastReceiver mSmartRotationEnableReceiver = new BroadcastReceiver() { // from class: com.aiunit.aon.AonSmartRotation.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AonSmartRotation.SMART_ROTATION_TESTEVENT)) {
                AonSmartRotation.printDetailLog("TestMode, Smart_Rotation_Testmode receive test event.");
                if (AonSmartRotation.sIsTestMode) {
                    try {
                        AonSmartRotation.sTestFaceAngle = Integer.parseInt(intent.getStringExtra("sFaceAngle"));
                        AonSmartRotation.sTestOrientationHardwareValue = Float.parseFloat(intent.getStringExtra("hardwareValue"));
                        AonSmartRotation.printDetailLog("TestMode, sTestFaceAngle is " + AonSmartRotation.sTestFaceAngle + " and  sTestOrientationHardwareValue is " + AonSmartRotation.sTestOrientationHardwareValue);
                        return;
                    } catch (Exception e) {
                        Log.e(AonSmartRotation.SMART_ROTATION_TAG, "TestMode, Get test event failed.");
                        return;
                    }
                }
                AonSmartRotation.printDetailLog("SmartRotationDebug, Test mode is not enable, ignore this event.");
            }
        }
    };

    public AonSmartRotation(Context context) {
        this.mContext = context;
        try {
            int parseInt = Integer.parseInt(SystemProperties.get(SMART_ROTATION_TIME_GAP, "400"));
            sFaceEventTimeGap = parseInt;
            if (parseInt > 1000) {
                sFaceEventTimeGap = 400;
                Log.w(SMART_ROTATION_TAG, "sFaceEventTimeGap can not be too long, reset it as 400ms.");
            }
            DEBUG_CONFIGURATION = SystemProperties.get("persist.sys.assert.panic", "false").equals("true");
            this.mUserManager = (UserManager) this.mContext.getSystemService(Telephony.Carriers.USER);
            sCurrentUserId = ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "Fetch time gap failed, use default value.");
        }
        Log.d(SMART_ROTATION_TAG, "Init AonSmartRotation successed.");
    }

    public int getStatus() {
        try {
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "getStatus failed.");
        }
        if (!this.mUserManager.isUserUnlocked()) {
            Log.d(SMART_ROTATION_TAG, "User change did not finish, upload old value.");
            return this.mSwitchValue;
        }
        this.mAutoRotationSwitchValue = Settings.System.getIntForUser(this.mContext.getContentResolver(), "accelerometer_rotation", 0, -2);
        int intForUser = Settings.System.getIntForUser(this.mContext.getContentResolver(), "device_orientation_intelligent_auto_rotation", 0, -2);
        this.mSmartRotationSwitchValue = intForUser;
        if (this.mAutoRotationSwitchValue == 1 && intForUser == 1) {
            this.mSwitchValue = 1;
        } else {
            if (this.mSwitchValue == 1) {
                while (sAonSmartRotationConnectionCount > 0) {
                    destroySmartRotationConnection();
                }
                printDetailLog("Ready to shut down AON smart rotation, reset sAonSmartRotationConnectionCount and the connections.");
            }
            this.mSwitchValue = 0;
        }
        if (this.mUserManager.isUserUnlocked() && sCurrentUserId != ActivityManagerNative.getDefault().getCurrentUser().id) {
            sCurrentUserId = ActivityManagerNative.getDefault().getCurrentUser().id;
            Log.d(SMART_ROTATION_TAG, "User handle changed to " + sCurrentUserId);
            while (sAonSmartRotationConnectionCount > 0) {
                destroySmartRotationConnection();
            }
            if (sAONService != null) {
                dropAONSubThread();
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e2) {
                    Log.e(SMART_ROTATION_TAG, "sleep 20ms error.");
                }
                unbindAONService();
            }
            if (this.mSwitchValue == 1) {
                createSmartRotationConnection();
            }
        }
        return this.mSwitchValue;
    }

    public static void printDetailLog(String logInfo) {
        if (logInfo != null && DEBUG_CONFIGURATION) {
            Log.d(SMART_ROTATION_TAG, logInfo);
        }
    }

    public void registerSmartRotationReceiver() {
        if (this.mSmartRotationFilter == null) {
            this.mSmartRotationFilter = new IntentFilter(SMART_ROTATION_TESTEVENT);
        }
        this.mContext.registerReceiver(this.mSmartRotationEnableReceiver, this.mSmartRotationFilter);
    }

    public void unRegisterSmartRotationReceiver() {
        BroadcastReceiver broadcastReceiver = this.mSmartRotationEnableReceiver;
        if (broadcastReceiver != null) {
            try {
                this.mContext.unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
            }
        }
    }

    public void updateTimeGap(int newTimeGap) {
        if (newTimeGap >= 0 && newTimeGap <= 1000) {
            sFaceEventTimeGap = newTimeGap;
            printDetailLog("Update Smart Rotation sFaceEventTimeGap as " + sFaceEventTimeGap);
        }
    }

    public void updateDebugConfiguration(boolean debugConfiguration) {
        DEBUG_CONFIGURATION = debugConfiguration;
        printDetailLog("Update Smart Rotation DEBUG_CONFIGURATION as " + DEBUG_CONFIGURATION);
    }

    public float createSmartRotationConnection() {
        try {
            printDetailLog("Register SmartRotation Connection.");
            int parseInt = Integer.parseInt(SystemProperties.get(SMART_ROTATION_TIME_GAP, "400"));
            sFaceEventTimeGap = parseInt;
            if (parseInt > 1000) {
                sFaceEventTimeGap = 400;
                Log.w(SMART_ROTATION_TAG, "sFaceEventTimeGap can not be too long, reset it as 400ms.");
            }
            DEBUG_CONFIGURATION = SystemProperties.get("persist.sys.assert.panic", "false").equals("true");
            sAonSmartRotationConnectionCount++;
            if (sSmartRotationHandlerThread == null) {
                initAONSubThread();
                printDetailLog("First AonSmartRotation Connection init.");
            } else {
                printDetailLog("We have " + sAonSmartRotationConnectionCount + " AonSmartRotation Connections here.");
            }
            if (sAONService == null && sServiceConnection == null) {
                bindAONService();
            } else {
                printDetailLog("Already have AONService here, do not bind it again.");
            }
            if (!sIsTestMode) {
                boolean equals = SystemProperties.get(SMART_ROTATION_TESTMODE_STATUS, "false").equals("true");
                sIsTestMode = equals;
                if (equals) {
                    registerSmartRotationReceiver();
                    Log.w(SMART_ROTATION_TAG, "TestMode, AON smart rotation testmode on.");
                }
            }
            printDetailLog("Finish Register SmartRotation Connection.");
            return 0.0f;
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "AON smart rotation error in createSmartRotationConnection(), e = " + e.toString());
            return 0.0f;
        }
    }

    public float destroySmartRotationConnection() {
        int i;
        try {
            printDetailLog("Unregister SmartRotation Connection.");
            i = sAonSmartRotationConnectionCount;
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "AON smart rotation error in destroySmartRotationConnection(), e = " + e.toString());
        }
        if (i == 0) {
            printDetailLog("No thing left while unregister, finish it.");
            return 0.0f;
        }
        int i2 = i - 1;
        sAonSmartRotationConnectionCount = i2;
        if (i2 == 0) {
            if (sIsTestMode) {
                sIsTestMode = false;
                unRegisterSmartRotationReceiver();
                Log.w(SMART_ROTATION_TAG, "TestMode, AON smart rotation testmode off.");
            }
            dropAONSubThread();
            try {
                Thread.sleep(20L);
            } catch (InterruptedException e2) {
                Log.e(SMART_ROTATION_TAG, "sleep 20ms error.");
            }
            unbindAONService();
        } else {
            printDetailLog("We have " + sAonSmartRotationConnectionCount + " AonSmartRotation Connections left.");
        }
        printDetailLog("Finish Unregister SmartRotation Connection.");
        return 0.0f;
    }

    public float makeDecisionBySmartRotation(float[] values) {
        try {
            if (sAONService == null) {
                Log.d(SMART_ROTATION_TAG, "nash_debug, sAONService is not init, dispatch orientation directlly. values[0] = " + values[0]);
            } else {
                if (sIsTestMode) {
                    return testModeData(values);
                }
                if (((int) values[0]) % 5 != 4 && ((int) values[0]) % 5 != 0) {
                    if (values[0] != PRE_STATUS_ONE && values[0] != PRE_STATUS_TWO && values[0] != 8.0f) {
                        sNormalStatusTime = System.currentTimeMillis();
                        printDetailLog("nash_debug, sNormalStatusTime: " + sNormalStatusTime + ", orientation: " + values[0]);
                        if (!sLaunchCameraAhead) {
                            printDetailLog("nash_debug, begin launch camera.");
                            launchCamera();
                            printDetailLog("nash_debug, finish launch camera, begin wait face event.");
                        }
                        sIsWaitingFaceAngle = true;
                        try {
                            Thread.sleep(sFaceEventTimeGap);
                        } catch (InterruptedException e) {
                            Log.e(SMART_ROTATION_TAG, "sleep sFaceEventTimeGap error.");
                        }
                        if (sIsLowLight) {
                            printDetailLog("AON_LOW_LIGHT, wait another 100ms for low light situation.");
                            try {
                                Thread.sleep(100L);
                            } catch (InterruptedException e2) {
                                Log.e(SMART_ROTATION_TAG, "sleep for AON_LOW_LIGHT error.");
                            }
                            sIsLowLight = false;
                        }
                        sIsWaitingFaceAngle = false;
                        Log.d(SMART_ROTATION_TAG, "nash_debug, time is up, sFaceAngle is " + sFaceAngle);
                        if (sIsCameraOn) {
                            releaseCamera();
                            Log.w(SMART_ROTATION_TAG, "Normal Orientation: " + values[0] + " here, and we can not receive a face angle, stop camera for next data.");
                        }
                        int i = sFaceAngle;
                        if (i >= 0 && i <= 360) {
                            sOrientationResult = values[0];
                            if ((i >= 0 && i < 60) || (i >= 300 && i <= 360)) {
                                Log.d(SMART_ROTATION_TAG, "nash_debug, Hardware orientation: " + values[0] + " sFaceAngle: " + sFaceAngle + ", may upload FACE_ORIENTATION_0");
                                sOrientationResult = 0.0f;
                            }
                            sFaceAngle = -2;
                            if (sLaunchCameraAhead) {
                                sLaunchCameraAhead = false;
                            }
                            return sOrientationResult;
                        }
                        if (i == -1) {
                            printDetailLog("nash_debug, we lost a face, maybe there is nobody here.");
                        } else if (i != -2) {
                            Log.w(SMART_ROTATION_TAG, "Illegal sFaceAngle: " + sFaceAngle);
                        } else {
                            printDetailLog("nash_debug, Default face angle, use hardware data.");
                        }
                    }
                    if (!sLaunchCameraAhead) {
                        printDetailLog("nash_debug, pre status for orientation, begin launch camera.");
                        sPreOpenCameraTime = System.currentTimeMillis();
                        launchCamera();
                        sLaunchCameraAhead = true;
                    }
                    return values[0];
                }
                printDetailLog("nash_debug, Ignore lay down, 0 and their pre status.");
                return values[0];
            }
            if (sLaunchCameraAhead) {
                sLaunchCameraAhead = false;
            }
            sFaceAngle = -2;
        } catch (Exception e3) {
            Log.e(SMART_ROTATION_TAG, "nash_debug, smart rotation dispatch event error e = " + e3.toString());
        }
        return values[0];
    }

    public static float testModeData(float[] values) {
        int i = sTestFaceAngle;
        if (i != -2) {
            float f = sTestOrientationHardwareValue;
            if (f != -1.0f) {
                if (i >= 0 && i <= 360 && f <= 9.0f && f >= 0.0f) {
                    if ((i >= 0 && i < 60) || (i >= 300 && i <= 360)) {
                        Log.d(SMART_ROTATION_TAG, "TestMode, sTestOrientationHardwareValue: " + sTestOrientationHardwareValue + " sTestFaceAngle: " + sTestFaceAngle + ", may upload FACE_ORIENTATION_0");
                        sTestOrientationResult = 0.0f;
                    } else if (i >= 240 && i < 300) {
                        Log.d(SMART_ROTATION_TAG, "TestMode, sTestOrientationHardwareValue: " + sTestOrientationHardwareValue + " sTestFaceAngle: " + sTestFaceAngle + ", may upload FACE_ORIENTATION_3");
                        sTestOrientationResult = FACE_ORIENTATION_3;
                    } else if (i < 120 || i >= 240) {
                        Log.d(SMART_ROTATION_TAG, "TestMode, sTestOrientationHardwareValue: " + sTestOrientationHardwareValue + " sTestFaceAngle: " + sTestFaceAngle + ", may upload FACE_ORIENTATION_1");
                        sTestOrientationResult = 1.0f;
                    } else {
                        Log.d(SMART_ROTATION_TAG, "TestMode, sTestOrientationHardwareValue: " + sTestOrientationHardwareValue + " sTestFaceAngle: " + sTestFaceAngle + ", may upload FACE_ORIENTATION_2");
                        sTestOrientationResult = FACE_ORIENTATION_2;
                    }
                    sTestFaceAngle = -2;
                    sTestOrientationHardwareValue = -1.0f;
                    if (sLaunchCameraAhead) {
                        sLaunchCameraAhead = false;
                    }
                    return sTestOrientationResult;
                }
                printDetailLog("TestMode, Ignore Illegal test data.");
            }
        }
        sTestFaceAngle = -2;
        sTestOrientationHardwareValue = -1.0f;
        sTestOrientationResult = 0.0f;
        if (sLaunchCameraAhead) {
            sLaunchCameraAhead = false;
        }
        printDetailLog("TestMode, No test data, upload real hardware value: " + values[0]);
        return values[0];
    }

    private void bindAONService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(AON_PKG_NAME, CLS_NAME));
        printDetailLog("nash_debug, Bind AON service intent: " + intent);
        this.mContext.bindServiceAsUser(intent, new AonSmartRotationServiceConnection(), 1, UserHandle.of(sCurrentUserId));
    }

    private void unbindAONService() {
        ServiceConnection serviceConnection;
        printDetailLog("nash_debug, Unbind AON service.");
        sAONService.asBinder().unlinkToDeath(sDeathRecipient, 0);
        Context context = this.mContext;
        if (context != null && (serviceConnection = sServiceConnection) != null) {
            context.unbindService(serviceConnection);
        }
        sAONService = null;
        sServiceConnection = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AonSmartRotationServiceConnection implements ServiceConnection {
        private AonSmartRotationServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(AonSmartRotation.SMART_ROTATION_TAG, "AON onServiceConnected");
            AonSmartRotation.sServiceConnection = this;
            AonSmartRotation.sAONService = IAONService.Stub.asInterface(iBinder);
            try {
                if (AonSmartRotation.sAONService != null) {
                    AonSmartRotation.printDetailLog("nash_debug, fetch AON Service successed.");
                    AonSmartRotation.sAONService.asBinder().linkToDeath(AonSmartRotation.sDeathRecipient, 0);
                    if (AonSmartRotation.sSmartRotationHandlerThread == null) {
                        AonSmartRotation.initAONSubThread();
                    }
                    AonSmartRotation.askForConnection();
                    return;
                }
                Log.w(AonSmartRotation.SMART_ROTATION_TAG, "fetch AON Service failed.");
            } catch (Exception e) {
                Log.e(AonSmartRotation.SMART_ROTATION_TAG, "AON smart rotation on Service connected error e = " + e.toString());
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            AonSmartRotation.sServiceConnection = null;
            AonSmartRotation.sAONService = null;
            AonSmartRotation.printDetailLog("AON onServiceDisconnected");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SmartRotationHandler extends Handler {
        SmartRotationHandler(Looper l) {
            super(l);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10001:
                    AonSmartRotation.printDetailLog("init SmartRotationHandler success");
                    return;
                case 10002:
                    AonSmartRotation.useAllProtocol();
                    return;
                case 10003:
                    AonSmartRotation.askForConnectionInner();
                    return;
                case 10004:
                    AonSmartRotation.dropConnectionInner();
                    return;
                case 10005:
                    AonSmartRotation.launchCameraInner();
                    return;
                case 10006:
                    AonSmartRotation.releaseCameraInner();
                    return;
                default:
                    return;
            }
        }
    }

    public static void initAONSubThread() {
        initHandlerThread();
        Message msg = Message.obtain(sSmartRotationHandler, 10001);
        msg.sendToTarget();
    }

    public static void initHandlerThread() {
        if (sSmartRotationHandlerThread == null) {
            printDetailLog("init SmartRotationHandlerThread");
            HandlerThread handlerThread = new HandlerThread("SmartRotationHandlerThread");
            sSmartRotationHandlerThread = handlerThread;
            handlerThread.start();
            sSmartRotationLooper = sSmartRotationHandlerThread.getLooper();
            sSmartRotationHandler = new SmartRotationHandler(sSmartRotationLooper);
        }
    }

    public static void dropAONSubThread() {
        printDetailLog("nash_debug, AON disconnect AON Service.");
        dropConnection();
        destroyHandlerThread();
    }

    public static void destroyHandlerThread() {
        try {
            Looper looper = sSmartRotationLooper;
            if (looper != null) {
                looper.quitSafely();
                sSmartRotationLooper = null;
            }
            HandlerThread handlerThread = sSmartRotationHandlerThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
                sSmartRotationHandlerThread = null;
            }
        } catch (Exception e) {
            Log.w(SMART_ROTATION_TAG, "stop SmartRotationHandlerThread error e = " + e.toString());
        }
    }

    public static int registerDirection() throws RemoteException {
        printDetailLog("nash_debug, registerDirection");
        IAONService iAONService = sAONService;
        if (iAONService == null) {
            Log.w(SMART_ROTATION_TAG, "nash_debug, sAONService is null, registerDirection failed");
            return -1;
        }
        try {
            return iAONService.registerListener(sAonlistener, AON_FACE_DIRECTION_ENGINE);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ERR_REMOTE_EXCEPTION;
        }
    }

    public static int unRegisterDirection() throws RemoteException {
        printDetailLog("nash_debug, unRegisterDirection");
        IAONService iAONService = sAONService;
        if (iAONService == null) {
            Log.w(SMART_ROTATION_TAG, "nash_debug, sAONService is null, unRegisterDirection failed");
            return -1;
        }
        try {
            return iAONService.unRegisterListener(sAonlistener, AON_FACE_DIRECTION_ENGINE);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ERR_REMOTE_EXCEPTION;
        }
    }

    public static int start() {
        printDetailLog("nash_debug, start camera");
        IAONService iAONService = sAONService;
        if (iAONService == null) {
            Log.w(SMART_ROTATION_TAG, "nash_debug, sAONService is null, start failed");
            return -1;
        }
        try {
            sIsCameraOn = true;
            return iAONService.start(AON_FACE_DIRECTION_ENGINE);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ERR_REMOTE_EXCEPTION;
        }
    }

    public static int stop() {
        printDetailLog("nash_debug, stop camera");
        IAONService iAONService = sAONService;
        if (iAONService == null) {
            Log.w(SMART_ROTATION_TAG, "nash_debug, sAONService is null, stop failed");
            return -1;
        }
        try {
            sIsCameraOn = false;
            return iAONService.stop(AON_FACE_DIRECTION_ENGINE);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ERR_REMOTE_EXCEPTION;
        }
    }

    public static void testAllProtocol() {
        printDetailLog("nash_debug, test All protocols.");
        Message msg = Message.obtain(sSmartRotationHandler, 10002);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void useAllProtocol() {
        try {
            int result = registerDirection();
            printDetailLog("nash_debug, registerDirection result is " + result);
            int result2 = start();
            printDetailLog("nash_debug, start result is " + result2);
            int result3 = stop();
            printDetailLog("nash_debug, stop result is " + result3);
            int result4 = unRegisterDirection();
            printDetailLog("nash_debug, unRegisterDirection result is " + result4);
        } catch (RemoteException e) {
            Log.e(SMART_ROTATION_TAG, "use All protocols catch RemoteException.");
            e.printStackTrace();
        }
        printDetailLog("nash_debug, use All protocols finished.");
    }

    public static void askForConnection() {
        printDetailLog("nash_debug, ask for connection.");
        Message msg = Message.obtain(sSmartRotationHandler, 10003);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void askForConnectionInner() {
        try {
            int result = registerDirection();
            printDetailLog("nash_debug, registerDirection result is " + result);
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "nash_debug, AON smart rotation askForConnectionInner error e = " + e.toString());
        }
    }

    public static void dropConnection() {
        printDetailLog("nash_debug, drop connection.");
        Message msg = Message.obtain(sSmartRotationHandler, 10004);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dropConnectionInner() {
        try {
            if (sIsCameraOn) {
                Log.w(SMART_ROTATION_TAG, "nash_debug, About to dropConnection but camera still on, stop it.");
                stop();
            }
            int result = unRegisterDirection();
            printDetailLog("nash_debug, unRegisterDirection result is " + result);
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "nash_debug, AON smart rotation dropConnectionInner error e = " + e.toString());
        }
    }

    public static void launchCamera() {
        printDetailLog("nash_debug, launch Camera.");
        Message msg = Message.obtain(sSmartRotationHandler, 10005);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void launchCameraInner() {
        try {
            int result = start();
            printDetailLog("nash_debug, start result is " + result);
            if (isErrorCodeNeedStopCamera(result)) {
                releaseCamera();
                Log.w(SMART_ROTATION_TAG, "Stop camera for special AON error code.");
            }
            if (result == ERR_LAUNCH_CAMERA_ONE) {
                askForConnection();
                Log.w(SMART_ROTATION_TAG, "Start before register a connection, ask for a new one.");
            }
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "nash_debug, AON smart rotation launchCameraInner error e = " + e.toString());
        }
    }

    public static void releaseCamera() {
        printDetailLog("nash_debug, release Camera.");
        Message msg = Message.obtain(sSmartRotationHandler, 10006);
        msg.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void releaseCameraInner() {
        try {
            int result = stop();
            printDetailLog("nash_debug, stop result is " + result);
        } catch (Exception e) {
            Log.e(SMART_ROTATION_TAG, "nash_debug, AON smart rotation releaseCameraInner error e = " + e.toString());
        }
    }

    public static boolean isErrorCodeNeedStopCamera(int errorCode) {
        if (stopCameraErrorCodes.contains(Integer.valueOf(errorCode))) {
            Log.w(SMART_ROTATION_TAG, "nash_debug, AON return error, errorCode is " + errorCode);
            return true;
        }
        return false;
    }
}
