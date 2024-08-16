package com.android.server.usb;

import android.content.Context;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiDeviceServer;
import android.media.midi.MidiDeviceStatus;
import android.media.midi.MidiManager;
import android.media.midi.MidiReceiver;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructPollfd;
import android.util.Log;
import com.android.internal.midi.MidiEventScheduler;
import com.android.internal.util.dump.DualDumpOutputStream;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbAlsaMidiDevice implements Closeable {
    private static final int BUFFER_SIZE = 512;
    private static final String TAG = "UsbAlsaMidiDevice";
    private final int mAlsaCard;
    private final int mAlsaDevice;
    private MidiEventScheduler[] mEventSchedulers;
    private FileDescriptor[] mFileDescriptors;
    private FileInputStream[] mInputStreams;
    private boolean mIsOpen;
    private final InputReceiverProxy[] mMidiInputPortReceivers;
    private final int mNumInputs;
    private final int mNumOutputs;
    private FileOutputStream[] mOutputStreams;
    private StructPollfd[] mPollFDs;
    private PowerBoostSetter mPowerBoostSetter;
    private MidiDeviceServer mServer;
    private boolean mServerAvailable;
    private final Object mLock = new Object();
    private int mPipeFD = -1;
    private final MidiDeviceServer.Callback mCallback = new MidiDeviceServer.Callback() { // from class: com.android.server.usb.UsbAlsaMidiDevice.1
        public void onClose() {
        }

        public void onDeviceStatusChanged(MidiDeviceServer midiDeviceServer, MidiDeviceStatus midiDeviceStatus) {
            MidiDeviceInfo deviceInfo = midiDeviceStatus.getDeviceInfo();
            int inputPortCount = deviceInfo.getInputPortCount();
            int outputPortCount = deviceInfo.getOutputPortCount();
            int i = 0;
            for (int i2 = 0; i2 < inputPortCount; i2++) {
                if (midiDeviceStatus.isInputPortOpen(i2)) {
                    i++;
                }
            }
            for (int i3 = 0; i3 < outputPortCount; i3++) {
                if (midiDeviceStatus.getOutputPortOpenCount(i3) > 0) {
                    i += midiDeviceStatus.getOutputPortOpenCount(i3);
                }
            }
            synchronized (UsbAlsaMidiDevice.this.mLock) {
                Log.d(UsbAlsaMidiDevice.TAG, "numOpenPorts: " + i + " isOpen: " + UsbAlsaMidiDevice.this.mIsOpen + " mServerAvailable: " + UsbAlsaMidiDevice.this.mServerAvailable);
                if (i > 0 && !UsbAlsaMidiDevice.this.mIsOpen && UsbAlsaMidiDevice.this.mServerAvailable) {
                    UsbAlsaMidiDevice.this.openLocked();
                } else if (i == 0 && UsbAlsaMidiDevice.this.mIsOpen) {
                    UsbAlsaMidiDevice.this.closeLocked();
                }
            }
        }
    };

    private native void nativeClose(FileDescriptor[] fileDescriptorArr);

    private native FileDescriptor[] nativeOpen(int i, int i2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public final class InputReceiverProxy extends MidiReceiver {
        private MidiReceiver mReceiver;

        private InputReceiverProxy() {
        }

        @Override // android.media.midi.MidiReceiver
        public void onSend(byte[] bArr, int i, int i2, long j) throws IOException {
            MidiReceiver midiReceiver = this.mReceiver;
            if (midiReceiver != null) {
                midiReceiver.send(bArr, i, i2, j);
            }
        }

        public void setReceiver(MidiReceiver midiReceiver) {
            this.mReceiver = midiReceiver;
        }

        @Override // android.media.midi.MidiReceiver
        public void onFlush() throws IOException {
            MidiReceiver midiReceiver = this.mReceiver;
            if (midiReceiver != null) {
                midiReceiver.flush();
            }
        }
    }

    public static UsbAlsaMidiDevice create(Context context, Bundle bundle, int i, int i2, int i3, int i4) {
        UsbAlsaMidiDevice usbAlsaMidiDevice = new UsbAlsaMidiDevice(i, i2, i3, i4);
        if (usbAlsaMidiDevice.register(context, bundle)) {
            return usbAlsaMidiDevice;
        }
        IoUtils.closeQuietly(usbAlsaMidiDevice);
        Log.e(TAG, "createDeviceServer failed");
        return null;
    }

    private UsbAlsaMidiDevice(int i, int i2, int i3, int i4) {
        this.mPowerBoostSetter = null;
        this.mAlsaCard = i;
        this.mAlsaDevice = i2;
        this.mNumInputs = i3;
        this.mNumOutputs = i4;
        this.mMidiInputPortReceivers = new InputReceiverProxy[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            this.mMidiInputPortReceivers[i5] = new InputReceiverProxy();
        }
        this.mPowerBoostSetter = new PowerBoostSetter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean openLocked() {
        int i = this.mNumInputs;
        if (i > 0) {
            i++;
        }
        int i2 = this.mNumOutputs;
        FileDescriptor[] nativeOpen = nativeOpen(this.mAlsaCard, this.mAlsaDevice, i, i2);
        if (nativeOpen == null) {
            Log.e(TAG, "nativeOpen failed");
            return false;
        }
        this.mFileDescriptors = nativeOpen;
        this.mPollFDs = new StructPollfd[i];
        this.mInputStreams = new FileInputStream[i];
        for (int i3 = 0; i3 < i; i3++) {
            FileDescriptor fileDescriptor = nativeOpen[i3];
            StructPollfd structPollfd = new StructPollfd();
            structPollfd.fd = fileDescriptor;
            structPollfd.events = (short) OsConstants.POLLIN;
            this.mPollFDs[i3] = structPollfd;
            this.mInputStreams[i3] = new FileInputStream(fileDescriptor);
        }
        this.mOutputStreams = new FileOutputStream[i2];
        this.mEventSchedulers = new MidiEventScheduler[i2];
        for (int i4 = 0; i4 < i2; i4++) {
            this.mOutputStreams[i4] = new FileOutputStream(nativeOpen[i + i4]);
            MidiEventScheduler midiEventScheduler = new MidiEventScheduler();
            this.mEventSchedulers[i4] = midiEventScheduler;
            this.mMidiInputPortReceivers[i4].setReceiver(midiEventScheduler.getReceiver());
        }
        final MidiReceiver[] outputPortReceivers = this.mServer.getOutputPortReceivers();
        if (i > 0) {
            new Thread("UsbAlsaMidiDevice input thread") { // from class: com.android.server.usb.UsbAlsaMidiDevice.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    byte[] bArr = new byte[512];
                    while (true) {
                        try {
                            long nanoTime = System.nanoTime();
                            synchronized (UsbAlsaMidiDevice.this.mLock) {
                                if (!UsbAlsaMidiDevice.this.mIsOpen) {
                                    break;
                                }
                                for (int i5 = 0; i5 < UsbAlsaMidiDevice.this.mPollFDs.length; i5++) {
                                    StructPollfd structPollfd2 = UsbAlsaMidiDevice.this.mPollFDs[i5];
                                    short s = structPollfd2.revents;
                                    if (((OsConstants.POLLERR | OsConstants.POLLHUP) & s) != 0) {
                                        break;
                                    }
                                    if ((s & OsConstants.POLLIN) != 0) {
                                        structPollfd2.revents = (short) 0;
                                        if (i5 == UsbAlsaMidiDevice.this.mInputStreams.length - 1) {
                                            break;
                                        }
                                        int read = UsbAlsaMidiDevice.this.mInputStreams[i5].read(bArr);
                                        outputPortReceivers[i5].send(bArr, 0, read, nanoTime);
                                        if (UsbAlsaMidiDevice.this.mPowerBoostSetter != null && read > 1) {
                                            UsbAlsaMidiDevice.this.mPowerBoostSetter.boostPower();
                                        }
                                    }
                                }
                            }
                            Os.poll(UsbAlsaMidiDevice.this.mPollFDs, -1);
                        } catch (ErrnoException unused) {
                            Log.d(UsbAlsaMidiDevice.TAG, "reader thread exiting");
                        } catch (IOException unused2) {
                            Log.d(UsbAlsaMidiDevice.TAG, "reader thread exiting");
                        }
                    }
                    Log.d(UsbAlsaMidiDevice.TAG, "input thread exit");
                }
            }.start();
        }
        for (int i5 = 0; i5 < i2; i5++) {
            final MidiEventScheduler midiEventScheduler2 = this.mEventSchedulers[i5];
            final FileOutputStream fileOutputStream = this.mOutputStreams[i5];
            final int i6 = i5;
            new Thread("UsbAlsaMidiDevice output thread " + i5) { // from class: com.android.server.usb.UsbAlsaMidiDevice.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    MidiEventScheduler.MidiEvent waitNextEvent;
                    while (true) {
                        try {
                            waitNextEvent = midiEventScheduler2.waitNextEvent();
                        } catch (InterruptedException unused) {
                        }
                        if (waitNextEvent != null) {
                            try {
                                fileOutputStream.write(waitNextEvent.data, 0, waitNextEvent.count);
                            } catch (IOException unused2) {
                                Log.e(UsbAlsaMidiDevice.TAG, "write failed for port " + i6);
                            }
                            midiEventScheduler2.addEventToPool(waitNextEvent);
                        } else {
                            Log.d(UsbAlsaMidiDevice.TAG, "output thread exit");
                            return;
                        }
                    }
                }
            }.start();
        }
        this.mIsOpen = true;
        return true;
    }

    private boolean register(Context context, Bundle bundle) {
        MidiManager midiManager = (MidiManager) context.getSystemService(MidiManager.class);
        if (midiManager == null) {
            Log.e(TAG, "No MidiManager in UsbAlsaMidiDevice.register()");
            return false;
        }
        this.mServerAvailable = true;
        MidiDeviceServer createDeviceServer = midiManager.createDeviceServer(this.mMidiInputPortReceivers, this.mNumInputs, null, null, bundle, 1, -1, this.mCallback);
        this.mServer = createDeviceServer;
        return createDeviceServer != null;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.mLock) {
            if (this.mIsOpen) {
                closeLocked();
            }
            this.mServerAvailable = false;
        }
        MidiDeviceServer midiDeviceServer = this.mServer;
        if (midiDeviceServer != null) {
            IoUtils.closeQuietly(midiDeviceServer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeLocked() {
        for (int i = 0; i < this.mEventSchedulers.length; i++) {
            this.mMidiInputPortReceivers[i].setReceiver(null);
            this.mEventSchedulers[i].close();
        }
        this.mEventSchedulers = null;
        int i2 = 0;
        while (true) {
            FileInputStream[] fileInputStreamArr = this.mInputStreams;
            if (i2 >= fileInputStreamArr.length) {
                break;
            }
            IoUtils.closeQuietly(fileInputStreamArr[i2]);
            i2++;
        }
        this.mInputStreams = null;
        int i3 = 0;
        while (true) {
            FileOutputStream[] fileOutputStreamArr = this.mOutputStreams;
            if (i3 < fileOutputStreamArr.length) {
                IoUtils.closeQuietly(fileOutputStreamArr[i3]);
                i3++;
            } else {
                this.mOutputStreams = null;
                nativeClose(this.mFileDescriptors);
                this.mFileDescriptors = null;
                this.mIsOpen = false;
                return;
            }
        }
    }

    public void dump(String str, DualDumpOutputStream dualDumpOutputStream, String str2, long j) {
        long start = dualDumpOutputStream.start(str2, j);
        dualDumpOutputStream.write("device_address", 1138166333443L, str);
        dualDumpOutputStream.write("card", 1120986464257L, this.mAlsaCard);
        dualDumpOutputStream.write("device", 1120986464258L, this.mAlsaDevice);
        dualDumpOutputStream.end(start);
    }
}
