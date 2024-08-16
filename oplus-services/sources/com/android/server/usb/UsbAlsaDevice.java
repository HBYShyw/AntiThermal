package com.android.server.usb;

import android.media.AudioDeviceAttributes;
import android.media.IAudioService;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.audio.AudioService;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbAlsaDevice {
    protected static final boolean DEBUG = false;
    private static final String[] DIRECTION_STR = {"INPUT", "OUTPUT"};
    private static final int INPUT = 0;
    private static final int NUM_DIRECTIONS = 2;
    private static final int OUTPUT = 1;
    private static final String OplusSpecialUsbDeviceName = "USB-Audio - MegaSig  U965";
    private static final String TAG = "UsbAlsaDevice";
    private final String mAlsaCardDeviceString;
    private IAudioService mAudioService;
    private final int mCardNum;
    private final String mDeviceAddress;
    private final int mDeviceNum;
    private final boolean[] mHasDevice;
    private final boolean mIsDock;
    private final boolean[] mIsHeadset;
    private UsbAlsaJackDetector mJackDetector;
    private final int[] mDeviceType = new int[2];
    private boolean[] mIsSelected = new boolean[2];
    private int[] mState = new int[2];
    private String mDeviceName = "";
    private String mDeviceDescription = "";
    private boolean mHasJackDetect = true;

    public UsbAlsaDevice(IAudioService iAudioService, int i, int i2, String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        this.mHasDevice = r1;
        this.mIsHeadset = r2;
        this.mAudioService = iAudioService;
        this.mCardNum = i;
        this.mDeviceNum = i2;
        this.mDeviceAddress = str;
        boolean[] zArr = {z2, z};
        boolean[] zArr2 = {z3, z4};
        this.mIsDock = z5;
        initDeviceType();
        this.mAlsaCardDeviceString = getAlsaCardDeviceString();
    }

    public int getCardNum() {
        return this.mCardNum;
    }

    public int getDeviceNum() {
        return this.mDeviceNum;
    }

    public String getDeviceAddress() {
        return this.mDeviceAddress;
    }

    public String getAlsaCardDeviceString() {
        int i;
        int i2 = this.mCardNum;
        if (i2 < 0 || (i = this.mDeviceNum) < 0) {
            Slog.e(TAG, "Invalid alsa card or device alsaCard: " + this.mCardNum + " alsaDevice: " + this.mDeviceNum);
            return null;
        }
        return AudioService.makeAlsaAddressString(i2, i);
    }

    public boolean hasOutput() {
        return this.mHasDevice[1];
    }

    public boolean hasInput() {
        return this.mHasDevice[0];
    }

    public boolean isOutputHeadset() {
        return this.mIsHeadset[1];
    }

    public boolean isInputHeadset() {
        return this.mIsHeadset[0];
    }

    public boolean isDock() {
        return this.mIsDock;
    }

    private synchronized boolean isInputJackConnected() {
        UsbAlsaJackDetector usbAlsaJackDetector = this.mJackDetector;
        if (usbAlsaJackDetector == null) {
            return true;
        }
        return usbAlsaJackDetector.isInputJackConnected();
    }

    private synchronized boolean isOutputJackConnected() {
        UsbAlsaJackDetector usbAlsaJackDetector = this.mJackDetector;
        if (usbAlsaJackDetector == null) {
            return true;
        }
        return usbAlsaJackDetector.isOutputJackConnected();
    }

    private synchronized void startJackDetect() {
        if (this.mJackDetector != null) {
            return;
        }
        if (this.mHasJackDetect) {
            UsbAlsaJackDetector startJackDetect = UsbAlsaJackDetector.startJackDetect(this);
            this.mJackDetector = startJackDetect;
            if (startJackDetect == null) {
                this.mHasJackDetect = false;
            }
        }
    }

    private synchronized void stopJackDetect() {
        UsbAlsaJackDetector usbAlsaJackDetector = this.mJackDetector;
        if (usbAlsaJackDetector != null) {
            usbAlsaJackDetector.pleaseStop();
        }
        this.mJackDetector = null;
    }

    public synchronized void start() {
        startOutput();
        startInput();
    }

    public synchronized void startInput() {
        startDevice(0);
    }

    public synchronized void startOutput() {
        startDevice(1);
    }

    private void startDevice(int i) {
        boolean[] zArr = this.mIsSelected;
        if (zArr[i]) {
            return;
        }
        zArr[i] = true;
        this.mState[i] = 0;
        startJackDetect();
        updateWiredDeviceConnectionState(i, true);
    }

    public synchronized void stop() {
        stopOutput();
        stopInput();
    }

    public synchronized void stopInput() {
        boolean[] zArr = this.mIsSelected;
        if (zArr[0]) {
            if (!zArr[1]) {
                stopJackDetect();
            }
            updateInputWiredDeviceConnectionState(false);
            this.mIsSelected[0] = false;
        }
    }

    public synchronized void stopOutput() {
        boolean[] zArr = this.mIsSelected;
        if (zArr[1]) {
            if (!zArr[0]) {
                stopJackDetect();
            }
            updateOutputWiredDeviceConnectionState(false);
            this.mIsSelected[1] = false;
        }
    }

    private void initDeviceType() {
        int i;
        int[] iArr = this.mDeviceType;
        boolean[] zArr = this.mHasDevice;
        int i2 = 0;
        if (zArr[0]) {
            i = this.mIsHeadset[0] ? -2113929216 : -2147479552;
        } else {
            i = 0;
        }
        iArr[0] = i;
        if (zArr[1]) {
            if (this.mIsDock) {
                i2 = 4096;
            } else {
                i2 = this.mIsHeadset[1] ? 67108864 : 16384;
            }
        }
        iArr[1] = i2;
    }

    public int getOutputDeviceType() {
        return this.mDeviceType[1];
    }

    public int getInputDeviceType() {
        return this.mDeviceType[0];
    }

    private boolean updateWiredDeviceConnectionState(int i, boolean z) {
        if (!this.mIsSelected[i]) {
            Slog.e(TAG, "Updating wired device connection state on unselected device");
            return false;
        }
        if (this.mDeviceType[i] == 0) {
            Slog.d(TAG, "Unable to set device connection state as " + DIRECTION_STR[i] + " device type is none");
            return false;
        }
        if (this.mAlsaCardDeviceString == null) {
            Slog.w(TAG, "Failed to update " + DIRECTION_STR[i] + " device connection state failed as alsa card device string is null");
            return false;
        }
        if (this.mDeviceName.equals(OplusSpecialUsbDeviceName)) {
            int[] iArr = this.mDeviceType;
            int i2 = iArr[i];
            if (i2 == -2147479552) {
                iArr[0] = -2113929216;
                Slog.d(TAG, "Force Usb In Headset");
            } else if (i2 == 16384) {
                iArr[1] = 67108864;
                Slog.d(TAG, "Force Usb Out Headset");
            }
        }
        boolean isInputJackConnected = i == 0 ? isInputJackConnected() : isOutputJackConnected();
        Slog.i(TAG, DIRECTION_STR[i] + " JACK connected: " + isInputJackConnected);
        int i3 = (z && isInputJackConnected) ? 1 : 0;
        int[] iArr2 = this.mState;
        if (i3 != iArr2[i]) {
            iArr2[i] = i3;
            try {
                this.mAudioService.setWiredDeviceConnectionState(new AudioDeviceAttributes(this.mDeviceType[i], this.mAlsaCardDeviceString, this.mDeviceName), i3, TAG);
            } catch (RemoteException unused) {
                Slog.e(TAG, "RemoteException in setWiredDeviceConnectionState for " + DIRECTION_STR[i]);
                return false;
            }
        }
        return true;
    }

    public synchronized boolean updateInputWiredDeviceConnectionState(boolean z) {
        return updateWiredDeviceConnectionState(0, z);
    }

    public synchronized boolean updateOutputWiredDeviceConnectionState(boolean z) {
        return updateWiredDeviceConnectionState(1, z);
    }

    public synchronized String toString() {
        return "UsbAlsaDevice: [card: " + this.mCardNum + ", device: " + this.mDeviceNum + ", name: " + this.mDeviceName + ", hasOutput: " + this.mHasDevice[1] + ", hasInput: " + this.mHasDevice[0] + "]";
    }

    public synchronized void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        long start = dualDumpOutputStream.start(str, j);
        dualDumpOutputStream.write("card", 1120986464257L, this.mCardNum);
        dualDumpOutputStream.write("device", 1120986464258L, this.mDeviceNum);
        dualDumpOutputStream.write("name", 1138166333443L, this.mDeviceName);
        dualDumpOutputStream.write("has_output", 1133871366148L, this.mHasDevice[1]);
        dualDumpOutputStream.write("has_input", 1133871366149L, this.mHasDevice[0]);
        dualDumpOutputStream.write("address", 1138166333446L, this.mDeviceAddress);
        dualDumpOutputStream.end(start);
    }

    synchronized String toShortString() {
        return "[card:" + this.mCardNum + " device:" + this.mDeviceNum + " " + this.mDeviceName + "]";
    }

    synchronized String getDeviceName() {
        return this.mDeviceName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setDeviceNameAndDescription(String str, String str2) {
        this.mDeviceName = str;
        this.mDeviceDescription = str2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof UsbAlsaDevice)) {
            return false;
        }
        UsbAlsaDevice usbAlsaDevice = (UsbAlsaDevice) obj;
        return this.mCardNum == usbAlsaDevice.mCardNum && this.mDeviceNum == usbAlsaDevice.mDeviceNum && Arrays.equals(this.mHasDevice, usbAlsaDevice.mHasDevice) && Arrays.equals(this.mIsHeadset, usbAlsaDevice.mIsHeadset) && this.mIsDock == usbAlsaDevice.mIsDock;
    }

    public int hashCode() {
        int i = (((this.mCardNum + 31) * 31) + this.mDeviceNum) * 31;
        boolean[] zArr = this.mHasDevice;
        int i2 = (((i + (!zArr[1] ? 1 : 0)) * 31) + (!zArr[0] ? 1 : 0)) * 31;
        boolean[] zArr2 = this.mIsHeadset;
        return ((((i2 + (!zArr2[0] ? 1 : 0)) * 31) + (!zArr2[1] ? 1 : 0)) * 31) + (!this.mIsDock ? 1 : 0);
    }
}
