package com.android.server;

import android.content.Context;
import android.hardware.audio.common.V2_0.AudioDevice;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.UEventObserver;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import com.android.server.ExtconUEventObserver;
import com.android.server.input.InputManagerService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class WiredAccessoryManager implements InputManagerService.WiredAccessoryCallbacks {
    private static final int BIT_HDMI_AUDIO = 16;
    private static final int BIT_HEADSET = 2;
    private static final int BIT_HEADSET_NO_MIC = 1;
    private static final int BIT_LINEOUT = 32;
    private static final int BIT_USB_HEADSET_ANLG = 4;
    private static final int BIT_USB_HEADSET_DGTL = 8;
    private static final String[] DP_AUDIO_CONNS;
    private static final String INTF_DP = "DP";
    private static final String INTF_HDMI = "HDMI";
    private static final boolean LOG;
    private static final int MSG_NEW_DEVICE_STATE = 1;
    private static final int MSG_SYSTEM_READY = 2;
    private static final String NAME_DP_AUDIO = "soc:qcom,msm-ext-disp";
    private static final String NAME_H2W = "h2w";
    private static final String NAME_HDMI = "hdmi";
    private static final String NAME_HDMI_AUDIO = "hdmi_audio";
    private static final String NAME_USB_AUDIO = "usb_audio";
    private static final int SUPPORTED_HEADSETS = 63;
    private static final String TAG = "WiredAccessoryManager";
    private final AudioManager mAudioManager;
    private int mDpCount;
    private final WiredAccessoryExtconObserver mExtconObserver;
    private int mHeadsetState;
    private final InputManagerService mInputManager;
    private final WiredAccessoryObserver mObserver;
    private int mSwitchValues;
    private final boolean mUseDevInputEventForAudioJack;
    private final PowerManager.WakeLock mWakeLock;
    private final Object mLock = new Object();
    private String mDetectedIntf = INTF_DP;
    private final Handler mHandler = new Handler(Looper.myLooper(), null, true) { // from class: com.android.server.WiredAccessoryManager.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                WiredAccessoryManager.this.setDevicesState(message.arg1, message.arg2, (String) message.obj);
                WiredAccessoryManager.this.mWakeLock.release();
            } else {
                if (i != 2) {
                    return;
                }
                WiredAccessoryManager.this.onSystemReady();
                WiredAccessoryManager.this.mWakeLock.release();
            }
        }
    };

    static {
        String str = Build.TYPE;
        LOG = "eng".equals(str) || "userdebug".equals(str);
        DP_AUDIO_CONNS = new String[]{"soc:qcom,msm-ext-disp/3/1", "soc:qcom,msm-ext-disp/2/1", "soc:qcom,msm-ext-disp/1/1", "soc:qcom,msm-ext-disp/0/1", "soc:qcom,msm-ext-disp/3/0", "soc:qcom,msm-ext-disp/2/0", "soc:qcom,msm-ext-disp/1/0", "soc:qcom,msm-ext-disp/0/0"};
    }

    public WiredAccessoryManager(Context context, InputManagerService inputManagerService) {
        PowerManager.WakeLock newWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, TAG);
        this.mWakeLock = newWakeLock;
        newWakeLock.setReferenceCounted(false);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        this.mInputManager = inputManagerService;
        this.mUseDevInputEventForAudioJack = context.getResources().getBoolean(17891884);
        this.mExtconObserver = new WiredAccessoryExtconObserver();
        this.mObserver = new WiredAccessoryObserver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSystemReady() {
        if (this.mUseDevInputEventForAudioJack) {
            int i = this.mInputManager.getSwitchState(-1, -256, 2) == 1 ? 4 : 0;
            if (this.mInputManager.getSwitchState(-1, -256, 4) == 1) {
                i |= 16;
            }
            if (this.mInputManager.getSwitchState(-1, -256, 6) == 1) {
                i |= 64;
            }
            notifyWiredAccessoryChanged(0L, i, 84);
        }
        if (ExtconUEventObserver.extconExists()) {
            this.mExtconObserver.uEventCount();
        }
        this.mObserver.init();
    }

    public void notifyWiredAccessoryChanged(long j, int i, int i2) {
        if (LOG) {
            Slog.v(TAG, "notifyWiredAccessoryChanged: when=" + j + " bits=" + switchCodeToString(i, i2) + " mask=" + Integer.toHexString(i2));
        }
        synchronized (this.mLock) {
            int i3 = (this.mSwitchValues & (~i2)) | i;
            this.mSwitchValues = i3;
            int i4 = i3 & 84;
            int i5 = 0;
            if (i4 != 0) {
                if (i4 == 4) {
                    i5 = 1;
                } else if (i4 == 16 || i4 == 20) {
                    i5 = 2;
                } else if (i4 == 64) {
                    i5 = 32;
                }
            }
            updateLocked(NAME_H2W, "", i5 | (this.mHeadsetState & (-36)));
        }
    }

    public void systemReady() {
        synchronized (this.mLock) {
            this.mWakeLock.acquire();
            this.mHandler.sendMessage(this.mHandler.obtainMessage(2, 0, 0, null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocked(String str, String str2, int i) {
        boolean z;
        Message obtainMessage;
        int i2;
        int i3;
        int i4 = i & 63;
        int i5 = i & 16;
        int i6 = i4 & 4;
        int i7 = i4 & 8;
        int i8 = i4 & 35;
        boolean z2 = false;
        boolean z3 = (this.mHeadsetState & 16) > 0;
        boolean z4 = this.mDpCount != 0;
        if (LOG) {
            Slog.v(TAG, "newName=" + str + " newState=" + i + " headsetState=" + i4 + " prev headsetState=" + this.mHeadsetState + " num of active dp conns= " + this.mDpCount);
        }
        if (this.mHeadsetState == i4 && !str.startsWith(NAME_DP_AUDIO)) {
            Log.e(TAG, "No state change.");
            return;
        }
        if (i8 == 35) {
            Log.e(TAG, "Invalid combination, unsetting h2w flag");
            z = false;
        } else {
            z = true;
        }
        if (i6 == 4 && i7 == 8) {
            Log.e(TAG, "Invalid combination, unsetting usb flag");
        } else {
            z2 = true;
        }
        if (!z && !z2) {
            Log.e(TAG, "invalid transition, returning ...");
            return;
        }
        if (str.startsWith(NAME_DP_AUDIO)) {
            if (i5 > 0 && (i3 = this.mDpCount) < DP_AUDIO_CONNS.length && z3 == z4) {
                this.mDpCount = i3 + 1;
            } else if (i5 == 0 && (i2 = this.mDpCount) > 0) {
                this.mDpCount = i2 - 1;
            } else {
                Log.e(TAG, "No state change for DP.");
                return;
            }
        }
        this.mWakeLock.acquire();
        Log.i(TAG, "MSG_NEW_DEVICE_STATE");
        if (str.startsWith(NAME_DP_AUDIO)) {
            int i9 = this.mHeadsetState;
            if (z3 && i5 != 0) {
                i9 &= -17;
            }
            obtainMessage = this.mHandler.obtainMessage(1, i4, i9, "soc:qcom,msm-ext-disp/" + str2);
            if (i4 == 0 && this.mDpCount != 0) {
                i4 |= 16;
            }
        } else {
            obtainMessage = this.mHandler.obtainMessage(1, i4, this.mHeadsetState, str + "/" + str2);
        }
        this.mHandler.sendMessage(obtainMessage);
        this.mHeadsetState = i4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDevicesState(int i, int i2, String str) {
        synchronized (this.mLock) {
            int i3 = 1;
            int i4 = 63;
            while (i4 != 0) {
                if ((i3 & i4) != 0) {
                    setDeviceStateLocked(i3, i, i2, str);
                    i4 &= ~i3;
                }
                i3 <<= 1;
            }
        }
    }

    private void setDeviceStateLocked(int i, int i2, int i3, String str) {
        int i4;
        int i5 = i2 & i;
        if (i5 != (i3 & i)) {
            int i6 = i5 != 0 ? 1 : 0;
            int i7 = 4;
            if (i == 2) {
                i4 = AudioDevice.IN_WIRED_HEADSET;
            } else if (i == 1) {
                i4 = 0;
                i7 = 8;
            } else {
                if (i == 32) {
                    i7 = 131072;
                } else if (i == 4) {
                    i7 = 2048;
                } else if (i == 8) {
                    i7 = 4096;
                } else {
                    if (i != 16) {
                        Slog.e(TAG, "setDeviceState() invalid headset type: " + i);
                        return;
                    }
                    i7 = 1024;
                }
                i4 = 0;
            }
            boolean z = LOG;
            if (z) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("headset: ");
                sb.append(str);
                sb.append(i6 == 1 ? " connected" : " disconnected");
                Slog.v(str2, sb.toString());
            }
            String[] split = str.split("/");
            if (i7 != 0) {
                if (z) {
                    String str3 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Output device address ");
                    sb2.append(split.length > 1 ? split[1] : "");
                    sb2.append(" name ");
                    sb2.append(split[0]);
                    Slog.v(str3, sb2.toString());
                }
                if (i3 == 1 && i2 == 2 && i7 == 8) {
                    i6 = 2;
                }
                this.mAudioManager.setWiredDeviceConnectionState(i7, i6, split.length > 1 ? split[1] : "", split[0]);
            }
            if (i4 != 0) {
                this.mAudioManager.setWiredDeviceConnectionState(i4, i6, split.length > 1 ? split[1] : "", split[0]);
            }
        }
    }

    private String switchCodeToString(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        if ((i2 & 4) != 0 && (i & 4) != 0) {
            sb.append("SW_HEADPHONE_INSERT ");
        }
        if ((i2 & 16) != 0 && (i & 16) != 0) {
            sb.append("SW_MICROPHONE_INSERT");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class WiredAccessoryObserver extends UEventObserver {
        private List<String> mDevPath = new ArrayList();
        private final List<UEventInfo> mUEventInfo = makeObservedUEventList();

        public WiredAccessoryObserver() {
        }

        void init() {
            int i;
            synchronized (WiredAccessoryManager.this.mLock) {
                if (WiredAccessoryManager.LOG) {
                    Slog.v(WiredAccessoryManager.TAG, "init()");
                }
                char[] cArr = new char[1024];
                for (int i2 = 0; i2 < this.mUEventInfo.size(); i2++) {
                    UEventInfo uEventInfo = this.mUEventInfo.get(i2);
                    try {
                        try {
                            String switchStatePath = uEventInfo.getSwitchStatePath();
                            FileReader fileReader = new FileReader(switchStatePath);
                            int read = fileReader.read(cArr, 0, 1024);
                            fileReader.close();
                            int parseInt = Integer.parseInt(new String(cArr, 0, read).trim());
                            if (parseInt > 0) {
                                int lastIndexOf = switchStatePath.lastIndexOf(".");
                                if (switchStatePath.substring(lastIndexOf + 1, lastIndexOf + 2).equals("1")) {
                                    WiredAccessoryManager.this.mDetectedIntf = "HDMI";
                                }
                                updateStateLocked(uEventInfo.getDevPath(), uEventInfo.getDevName(), parseInt);
                            }
                        } catch (Exception e) {
                            Slog.e(WiredAccessoryManager.TAG, "Error while attempting to determine initial switch state for " + uEventInfo.getDevName(), e);
                        }
                    } catch (FileNotFoundException unused) {
                        Slog.w(WiredAccessoryManager.TAG, uEventInfo.getSwitchStatePath() + " not found while attempting to determine initial switch state");
                    }
                }
            }
            for (i = 0; i < this.mUEventInfo.size(); i++) {
                UEventInfo uEventInfo2 = this.mUEventInfo.get(i);
                String devPath = uEventInfo2.getDevPath();
                if (!this.mDevPath.contains(devPath)) {
                    startObserving("DEVPATH=" + uEventInfo2.getDevPath());
                    this.mDevPath.add(devPath);
                }
            }
        }

        private List<UEventInfo> makeObservedUEventList() {
            ArrayList arrayList = new ArrayList();
            if (!WiredAccessoryManager.this.mUseDevInputEventForAudioJack) {
                UEventInfo uEventInfo = new UEventInfo(WiredAccessoryManager.NAME_H2W, 2, 1, 32);
                if (uEventInfo.checkSwitchExists()) {
                    arrayList.add(uEventInfo);
                } else {
                    Slog.w(WiredAccessoryManager.TAG, "This kernel does not have wired headset support");
                }
            }
            UEventInfo uEventInfo2 = new UEventInfo(WiredAccessoryManager.NAME_USB_AUDIO, 4, 8, 0);
            if (uEventInfo2.checkSwitchExists()) {
                arrayList.add(uEventInfo2);
            } else {
                Slog.w(WiredAccessoryManager.TAG, "This kernel does not have usb audio support");
            }
            UEventInfo uEventInfo3 = new UEventInfo(WiredAccessoryManager.NAME_HDMI_AUDIO, 16, 0, 0);
            if (uEventInfo3.checkSwitchExists()) {
                arrayList.add(uEventInfo3);
            } else {
                UEventInfo uEventInfo4 = new UEventInfo(WiredAccessoryManager.NAME_HDMI, 16, 0, 0);
                if (uEventInfo4.checkSwitchExists()) {
                    arrayList.add(uEventInfo4);
                } else {
                    Slog.w(WiredAccessoryManager.TAG, "This kernel does not have HDMI audio support");
                }
            }
            for (String str : WiredAccessoryManager.DP_AUDIO_CONNS) {
                if (WiredAccessoryManager.LOG) {
                    Slog.v(WiredAccessoryManager.TAG, "Monitor DP conn " + str);
                }
                UEventInfo uEventInfo5 = new UEventInfo(str, 16, 0, 0);
                if (uEventInfo5.checkSwitchExists()) {
                    arrayList.add(uEventInfo5);
                } else {
                    Slog.w(WiredAccessoryManager.TAG, "Conn " + str + " does not have DP audio support");
                }
            }
            return arrayList;
        }

        /* JADX WARN: Removed duplicated region for block: B:47:0x00e0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onUEvent(UEventObserver.UEvent uEvent) {
            int i;
            int i2;
            FileReader fileReader;
            String str = uEvent.get("DEVPATH");
            String str2 = uEvent.get("NAME");
            if (str2 == null) {
                str2 = uEvent.get("SWITCH_NAME");
            }
            try {
                if (str2.startsWith(WiredAccessoryManager.NAME_DP_AUDIO)) {
                    String str3 = uEvent.get("STATE");
                    int length = str3.length();
                    int i3 = 0;
                    i = 0;
                    while (i3 < length) {
                        try {
                            int indexOf = str3.indexOf(61, i3);
                            if (indexOf > i3) {
                                String substring = str3.substring(i3, indexOf);
                                if ((substring.equals(WiredAccessoryManager.INTF_DP) || substring.equals("HDMI")) && (i = Integer.parseInt(str3.substring(indexOf + 1, indexOf + 2))) == 1) {
                                    WiredAccessoryManager.this.mDetectedIntf = substring;
                                    break;
                                }
                            }
                            i3 = indexOf + 3;
                        } catch (NumberFormatException unused) {
                            Slog.i(WiredAccessoryManager.TAG, "couldn't get state from event, checking node");
                            int i4 = 0;
                            while (true) {
                                if (i4 >= this.mUEventInfo.size()) {
                                    break;
                                }
                                UEventInfo uEventInfo = this.mUEventInfo.get(i4);
                                if (str2.equals(uEventInfo.getDevName())) {
                                    char[] cArr = new char[1024];
                                    try {
                                        try {
                                            fileReader = new FileReader(uEventInfo.getSwitchStatePath());
                                            i2 = fileReader.read(cArr, 0, 1024);
                                        } catch (FileNotFoundException unused2) {
                                            Slog.e(WiredAccessoryManager.TAG, "file not found");
                                        }
                                        try {
                                            fileReader.close();
                                        } catch (Exception e) {
                                            e = e;
                                            Slog.e(WiredAccessoryManager.TAG, "", e);
                                            i = Integer.parseInt(new String(cArr, 0, i2).trim());
                                            synchronized (WiredAccessoryManager.this.mLock) {
                                            }
                                        }
                                    } catch (Exception e2) {
                                        e = e2;
                                        i2 = 0;
                                    }
                                    try {
                                        i = Integer.parseInt(new String(cArr, 0, i2).trim());
                                        break;
                                    } catch (NumberFormatException unused3) {
                                        Slog.e(WiredAccessoryManager.TAG, "could not convert to number");
                                    }
                                } else {
                                    i4++;
                                }
                            }
                            synchronized (WiredAccessoryManager.this.mLock) {
                            }
                        }
                    }
                } else {
                    i = Integer.parseInt(uEvent.get("SWITCH_STATE"));
                }
            } catch (NumberFormatException unused4) {
                i = 0;
            }
            synchronized (WiredAccessoryManager.this.mLock) {
                updateStateLocked(str, str2, i);
            }
        }

        private void updateStateLocked(String str, String str2, int i) {
            for (int i2 = 0; i2 < this.mUEventInfo.size(); i2++) {
                UEventInfo uEventInfo = this.mUEventInfo.get(i2);
                if (WiredAccessoryManager.LOG) {
                    Slog.v(WiredAccessoryManager.TAG, "uei.getDevPath=" + uEventInfo.getDevPath());
                    Slog.v(WiredAccessoryManager.TAG, "uevent.getDevPath=" + str);
                }
                if (str.equals(uEventInfo.getDevPath())) {
                    if (i == 1 && WiredAccessoryManager.this.mDpCount > 0) {
                        uEventInfo.setStreamIndex(WiredAccessoryManager.this.mDpCount);
                    }
                    if (i == 1) {
                        uEventInfo.setCableIndex(1 ^ (WiredAccessoryManager.this.mDetectedIntf.equals(WiredAccessoryManager.INTF_DP) ? 1 : 0));
                    }
                    WiredAccessoryManager.this.updateLocked(str2, uEventInfo.getDevAddress(), uEventInfo.computeNewHeadsetState(WiredAccessoryManager.this.mHeadsetState, i));
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
        public final class UEventInfo {
            static final /* synthetic */ boolean $assertionsDisabled = false;
            private final String mDevName;
            private final int mState1Bits;
            private final int mState2Bits;
            private final int mStateNbits;
            private String mDevAddress = "controller=0;stream=0";
            private int mDevIndex = -1;
            private int mCableIndex = -1;

            public UEventInfo(String str, int i, int i2, int i3) {
                int indexOf;
                this.mDevName = str;
                this.mState1Bits = i;
                this.mState2Bits = i2;
                this.mStateNbits = i3;
                if (!str.startsWith(WiredAccessoryManager.NAME_DP_AUDIO) || (indexOf = str.indexOf("/")) == -1) {
                    return;
                }
                int i4 = indexOf + 1;
                int indexOf2 = str.indexOf("/", i4);
                int parseInt = Integer.parseInt(str.substring(i4, indexOf2));
                int parseInt2 = Integer.parseInt(str.substring(indexOf2 + 1));
                if (WiredAccessoryManager.LOG) {
                    Slog.v(WiredAccessoryManager.TAG, "UEvent dev address " + this.mDevAddress);
                }
                checkDevIndex(parseInt);
                checkCableIndex(parseInt2);
            }

            private void checkDevIndex(int i) {
                char[] cArr = new char[1024];
                int i2 = 0;
                while (true) {
                    String format = String.format(Locale.US, "/sys/devices/platform/soc/%s/extcon/extcon%d/name", WiredAccessoryManager.NAME_DP_AUDIO, Integer.valueOf(i));
                    if (WiredAccessoryManager.LOG) {
                        Slog.v(WiredAccessoryManager.TAG, "checkDevIndex " + format);
                    }
                    File file = new File(format);
                    if (!file.exists()) {
                        Slog.e(WiredAccessoryManager.TAG, "file " + format + " not found");
                        return;
                    }
                    try {
                        FileReader fileReader = new FileReader(file);
                        int read = fileReader.read(cArr, 0, 1024);
                        fileReader.close();
                        if (new String(cArr, 0, read).trim().startsWith(WiredAccessoryManager.NAME_DP_AUDIO) && i2 == i) {
                            Slog.e(WiredAccessoryManager.TAG, "set dev_index " + i);
                            this.mDevIndex = i;
                            return;
                        }
                        i2++;
                    } catch (Exception e) {
                        Slog.e(WiredAccessoryManager.TAG, "checkDevIndex exception ", e);
                        return;
                    }
                }
            }

            private void checkCableIndex(int i) {
                if (this.mDevIndex == -1) {
                    return;
                }
                char[] cArr = new char[1024];
                int i2 = 0;
                while (true) {
                    String format = String.format(Locale.US, "/sys/devices/platform/soc/%s/extcon/extcon%d/cable.%d/name", WiredAccessoryManager.NAME_DP_AUDIO, Integer.valueOf(this.mDevIndex), Integer.valueOf(i2));
                    if (WiredAccessoryManager.LOG) {
                        Slog.v(WiredAccessoryManager.TAG, "checkCableIndex " + format);
                    }
                    File file = new File(format);
                    if (!file.exists()) {
                        Slog.e(WiredAccessoryManager.TAG, "file " + format + " not found");
                        return;
                    }
                    try {
                        FileReader fileReader = new FileReader(file);
                        int read = fileReader.read(cArr, 0, 1024);
                        fileReader.close();
                        String trim = new String(cArr, 0, read).trim();
                        if (trim.equals("HDMI") && i2 == i) {
                            this.mCableIndex = i2;
                            Slog.w(WiredAccessoryManager.TAG, "checkCableIndex set cable " + i);
                            return;
                        }
                        if (trim.equals(WiredAccessoryManager.INTF_DP) && i2 == i) {
                            this.mCableIndex = i2;
                            Slog.w(WiredAccessoryManager.TAG, "checkCableIndex set cable " + i);
                            return;
                        }
                        Slog.w(WiredAccessoryManager.TAG, "checkCableIndex no name match, skip ");
                        i2++;
                    } catch (Exception e) {
                        Slog.e(WiredAccessoryManager.TAG, "checkCableIndex exception", e);
                        return;
                    }
                }
            }

            public void setStreamIndex(int i) {
                this.mDevAddress = this.mDevAddress.substring(0, this.mDevAddress.indexOf("=", this.mDevAddress.indexOf("=") + 1) + 1) + String.valueOf(i);
            }

            public void setCableIndex(int i) {
                int indexOf = this.mDevAddress.indexOf("=");
                this.mDevAddress = this.mDevAddress.substring(0, indexOf + 1) + i + this.mDevAddress.substring(indexOf + 2);
            }

            public String getDevName() {
                return this.mDevName;
            }

            public String getDevAddress() {
                return this.mDevAddress;
            }

            public String getDevPath() {
                if (this.mDevName.startsWith(WiredAccessoryManager.NAME_DP_AUDIO)) {
                    return String.format(Locale.US, "/devices/platform/soc/%s/extcon/extcon%d", WiredAccessoryManager.NAME_DP_AUDIO, Integer.valueOf(this.mDevIndex));
                }
                return String.format(Locale.US, "/devices/virtual/switch/%s", this.mDevName);
            }

            public String getSwitchStatePath() {
                if (this.mDevName.startsWith(WiredAccessoryManager.NAME_DP_AUDIO)) {
                    return String.format(Locale.US, "/sys/devices/platform/soc/%s/extcon/extcon%d/cable.%d/state", WiredAccessoryManager.NAME_DP_AUDIO, Integer.valueOf(this.mDevIndex), Integer.valueOf(this.mCableIndex));
                }
                return String.format(Locale.US, "/sys/class/switch/%s/state", this.mDevName);
            }

            public boolean checkSwitchExists() {
                return new File(getSwitchStatePath()).exists();
            }

            public int computeNewHeadsetState(int i, int i2) {
                int i3 = this.mState1Bits;
                int i4 = this.mState2Bits;
                int i5 = this.mStateNbits;
                int i6 = ~(i3 | i4 | i5);
                if (i2 != 1) {
                    i3 = i2 == 2 ? i4 : i2 == i5 ? i5 : 0;
                }
                return (i & i6) | i3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public class WiredAccessoryExtconObserver extends ExtconStateObserver<Pair<Integer, Integer>> {
        private final List<ExtconUEventObserver.ExtconInfo> mExtconInfos = ExtconUEventObserver.ExtconInfo.getExtconInfoForTypes(new String[]{ExtconUEventObserver.ExtconInfo.EXTCON_HEADPHONE, ExtconUEventObserver.ExtconInfo.EXTCON_MICROPHONE, "HDMI", ExtconUEventObserver.ExtconInfo.EXTCON_LINE_OUT});

        WiredAccessoryExtconObserver() {
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0069  */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0086 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:8:0x005c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void init() {
            Pair<Integer, Integer> pair;
            for (ExtconUEventObserver.ExtconInfo extconInfo : this.mExtconInfos) {
                try {
                    pair = parseStateFromFile(extconInfo);
                } catch (FileNotFoundException e) {
                    Slog.w(WiredAccessoryManager.TAG, extconInfo.getStatePath() + " not found while attempting to determine initial state", e);
                    pair = null;
                    if (pair != null) {
                    }
                    if (WiredAccessoryManager.LOG) {
                    }
                    startObserving(extconInfo);
                } catch (IOException e2) {
                    Slog.e(WiredAccessoryManager.TAG, "Error reading " + extconInfo.getStatePath() + " while attempting to determine initial state", e2);
                    pair = null;
                    if (pair != null) {
                    }
                    if (WiredAccessoryManager.LOG) {
                    }
                    startObserving(extconInfo);
                }
                if (pair != null) {
                    updateStateInt(extconInfo, extconInfo.getName(), pair);
                }
                if (WiredAccessoryManager.LOG) {
                    Slog.d(WiredAccessoryManager.TAG, "observing " + extconInfo.getName());
                }
                startObserving(extconInfo);
            }
        }

        public int uEventCount() {
            return this.mExtconInfos.size();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.ExtconStateObserver
        public Pair<Integer, Integer> parseState(ExtconUEventObserver.ExtconInfo extconInfo, String str) {
            if (WiredAccessoryManager.LOG) {
                Slog.v(WiredAccessoryManager.TAG, "status  " + str);
            }
            int[] iArr = {0, 0};
            if (extconInfo.hasCableType(ExtconUEventObserver.ExtconInfo.EXTCON_HEADPHONE)) {
                WiredAccessoryManager.updateBit(iArr, 1, str, ExtconUEventObserver.ExtconInfo.EXTCON_HEADPHONE);
            }
            if (extconInfo.hasCableType(ExtconUEventObserver.ExtconInfo.EXTCON_MICROPHONE)) {
                WiredAccessoryManager.updateBit(iArr, 2, str, ExtconUEventObserver.ExtconInfo.EXTCON_MICROPHONE);
            }
            if (extconInfo.hasCableType("HDMI")) {
                WiredAccessoryManager.updateBit(iArr, 16, str, "HDMI");
            }
            if (extconInfo.hasCableType(ExtconUEventObserver.ExtconInfo.EXTCON_LINE_OUT)) {
                WiredAccessoryManager.updateBit(iArr, 32, str, ExtconUEventObserver.ExtconInfo.EXTCON_LINE_OUT);
            }
            if (WiredAccessoryManager.LOG) {
                Slog.v(WiredAccessoryManager.TAG, "mask " + iArr[0] + " state " + iArr[1]);
            }
            return Pair.create(Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]));
        }

        @Override // com.android.server.ExtconStateObserver
        public void updateState(ExtconUEventObserver.ExtconInfo extconInfo, String str, Pair<Integer, Integer> pair) {
            synchronized (WiredAccessoryManager.this.mLock) {
                int intValue = ((Integer) pair.first).intValue();
                int intValue2 = ((Integer) pair.second).intValue();
                WiredAccessoryManager wiredAccessoryManager = WiredAccessoryManager.this;
                wiredAccessoryManager.updateLocked(str, "", (intValue2 & intValue) | (wiredAccessoryManager.mHeadsetState & (~((~intValue2) & intValue))));
            }
        }

        private void updateStateInt(ExtconUEventObserver.ExtconInfo extconInfo, String str, Pair<Integer, Integer> pair) {
            synchronized (WiredAccessoryManager.this.mLock) {
                int intValue = ((Integer) pair.first).intValue();
                int intValue2 = ((Integer) pair.second).intValue();
                if (WiredAccessoryManager.this.mHeadsetState == 0) {
                    WiredAccessoryManager wiredAccessoryManager = WiredAccessoryManager.this;
                    wiredAccessoryManager.updateLocked(str, "", (intValue2 & intValue) | (wiredAccessoryManager.mHeadsetState & (~((~intValue2) & intValue))));
                } else {
                    WiredAccessoryManager wiredAccessoryManager2 = WiredAccessoryManager.this;
                    wiredAccessoryManager2.updateLocked(str, "", ((~((~intValue2) & intValue)) & intValue & intValue2) | wiredAccessoryManager2.mHeadsetState);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateBit(int[] iArr, int i, String str, String str2) {
        iArr[0] = iArr[0] | i;
        if (str.contains(str2 + "=1")) {
            iArr[0] = iArr[0] | i;
            iArr[1] = i | iArr[1];
            return;
        }
        if (str.contains(str2 + "=0")) {
            iArr[0] = iArr[0] | i;
            iArr[1] = (~i) & iArr[1];
        }
    }
}
