package com.android.server.usb;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbAlsaJackDetector implements Runnable {
    private static final String TAG = "UsbAlsaJackDetector";
    private UsbAlsaDevice mAlsaDevice;
    private boolean mStopJackDetect = false;

    private static native boolean nativeHasJackDetect(int i);

    private native boolean nativeInputJackConnected(int i);

    private native boolean nativeJackDetect(int i);

    private native boolean nativeOutputJackConnected(int i);

    private UsbAlsaJackDetector(UsbAlsaDevice usbAlsaDevice) {
        this.mAlsaDevice = usbAlsaDevice;
    }

    public static UsbAlsaJackDetector startJackDetect(UsbAlsaDevice usbAlsaDevice) {
        if (!nativeHasJackDetect(usbAlsaDevice.getCardNum())) {
            return null;
        }
        UsbAlsaJackDetector usbAlsaJackDetector = new UsbAlsaJackDetector(usbAlsaDevice);
        new Thread(usbAlsaJackDetector, "USB jack detect thread").start();
        return usbAlsaJackDetector;
    }

    public boolean isInputJackConnected() {
        return nativeInputJackConnected(this.mAlsaDevice.getCardNum());
    }

    public boolean isOutputJackConnected() {
        return nativeOutputJackConnected(this.mAlsaDevice.getCardNum());
    }

    public void pleaseStop() {
        synchronized (this) {
            this.mStopJackDetect = true;
        }
    }

    public boolean jackDetectCallback() {
        synchronized (this) {
            if (this.mStopJackDetect) {
                return false;
            }
            this.mAlsaDevice.updateOutputWiredDeviceConnectionState(true);
            this.mAlsaDevice.updateInputWiredDeviceConnectionState(true);
            return true;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        nativeJackDetect(this.mAlsaDevice.getCardNum());
    }
}
