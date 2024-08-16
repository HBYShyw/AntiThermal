package com.android.server.usb;

import android.content.Context;
import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiDeviceServer;
import android.media.midi.MidiDeviceStatus;
import android.media.midi.MidiManager;
import android.media.midi.MidiReceiver;
import android.os.Bundle;
import android.util.Log;
import com.android.internal.midi.MidiEventMultiScheduler;
import com.android.internal.midi.MidiEventScheduler;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.usb.descriptors.UsbACMidi10Endpoint;
import com.android.server.usb.descriptors.UsbDescriptor;
import com.android.server.usb.descriptors.UsbDescriptorParser;
import com.android.server.usb.descriptors.UsbEndpointDescriptor;
import com.android.server.usb.descriptors.UsbInterfaceDescriptor;
import com.android.server.usb.descriptors.UsbMidiBlockParser;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbDirectMidiDevice implements Closeable {
    private static final int BULK_TRANSFER_NUMBER_OF_RETRIES = 20;
    private static final int BULK_TRANSFER_TIMEOUT_MILLISECONDS = 50;
    private static final boolean DEBUG = true;
    private static final byte MESSAGE_TYPE_MIDI_1_CHANNEL_VOICE = 2;
    private static final byte MESSAGE_TYPE_MIDI_2_CHANNEL_VOICE = 4;
    private static final String TAG = "UsbDirectMidiDevice";
    private static final int THREAD_JOIN_TIMEOUT_MILLISECONDS = 200;
    private Context mContext;
    private ArrayList<ArrayList<Integer>> mInputUsbEndpointCableCounts;
    private ArrayList<ArrayList<UsbEndpoint>> mInputUsbEndpoints;
    private boolean mIsOpen;
    private final boolean mIsUniversalMidiDevice;
    private ArrayList<ArrayList<MidiEventMultiScheduler>> mMidiEventMultiSchedulers;
    private final InputReceiverProxy[] mMidiInputPortReceivers;
    private String mName;
    private final int mNumInputs;
    private final int mNumOutputs;
    private ArrayList<ArrayList<Integer>> mOutputUsbEndpointCableCounts;
    private ArrayList<ArrayList<UsbEndpoint>> mOutputUsbEndpoints;
    private UsbDescriptorParser mParser;
    private PowerBoostSetter mPowerBoostSetter;
    private MidiDeviceServer mServer;
    private boolean mServerAvailable;
    private final boolean mShouldCallSetInterface;
    private ArrayList<Thread> mThreads;
    private final String mUniqueUsbDeviceIdentifier;
    private UsbDevice mUsbDevice;
    private ArrayList<UsbDeviceConnection> mUsbDeviceConnections;
    private ArrayList<UsbInterfaceDescriptor> mUsbInterfaces;
    private UsbMidiBlockParser mMidiBlockParser = new UsbMidiBlockParser();
    private int mDefaultMidiProtocol = 1;
    private final Object mLock = new Object();
    private final MidiDeviceServer.Callback mCallback = new MidiDeviceServer.Callback() { // from class: com.android.server.usb.UsbDirectMidiDevice.1
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
            synchronized (UsbDirectMidiDevice.this.mLock) {
                Log.d(UsbDirectMidiDevice.TAG, "numOpenPorts: " + i + " isOpen: " + UsbDirectMidiDevice.this.mIsOpen + " mServerAvailable: " + UsbDirectMidiDevice.this.mServerAvailable);
                if (i > 0 && !UsbDirectMidiDevice.this.mIsOpen && UsbDirectMidiDevice.this.mServerAvailable) {
                    UsbDirectMidiDevice.this.openLocked();
                } else if (i == 0 && UsbDirectMidiDevice.this.mIsOpen) {
                    UsbDirectMidiDevice.this.closeLocked();
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class InputReceiverProxy extends MidiReceiver {
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

    public static UsbDirectMidiDevice create(Context context, UsbDevice usbDevice, UsbDescriptorParser usbDescriptorParser, boolean z, String str) {
        UsbDirectMidiDevice usbDirectMidiDevice = new UsbDirectMidiDevice(usbDevice, usbDescriptorParser, z, str);
        if (usbDirectMidiDevice.register(context)) {
            return usbDirectMidiDevice;
        }
        IoUtils.closeQuietly(usbDirectMidiDevice);
        Log.e(TAG, "createDeviceServer failed");
        return null;
    }

    private UsbDirectMidiDevice(UsbDevice usbDevice, UsbDescriptorParser usbDescriptorParser, boolean z, String str) {
        ArrayList<UsbInterfaceDescriptor> findLegacyMidiInterfaceDescriptors;
        this.mPowerBoostSetter = null;
        this.mUsbDevice = usbDevice;
        this.mParser = usbDescriptorParser;
        this.mUniqueUsbDeviceIdentifier = str;
        this.mIsUniversalMidiDevice = z;
        this.mShouldCallSetInterface = usbDescriptorParser.calculateMidiInterfaceDescriptorsCount() > 1;
        if (z) {
            findLegacyMidiInterfaceDescriptors = usbDescriptorParser.findUniversalMidiInterfaceDescriptors();
        } else {
            findLegacyMidiInterfaceDescriptors = usbDescriptorParser.findLegacyMidiInterfaceDescriptors();
        }
        this.mUsbInterfaces = new ArrayList<>();
        if (this.mUsbDevice.getConfigurationCount() > 0) {
            UsbConfiguration configuration = this.mUsbDevice.getConfiguration(0);
            for (int i = 0; i < configuration.getInterfaceCount(); i++) {
                UsbInterface usbInterface = configuration.getInterface(i);
                Iterator<UsbInterfaceDescriptor> it = findLegacyMidiInterfaceDescriptors.iterator();
                while (true) {
                    if (it.hasNext()) {
                        UsbInterfaceDescriptor next = it.next();
                        if (areEquivalent(usbInterface, next.toAndroid(this.mParser))) {
                            this.mUsbInterfaces.add(next);
                            break;
                        }
                    }
                }
            }
            if (this.mUsbDevice.getConfigurationCount() > 1) {
                Log.w(TAG, "Skipping some USB configurations. Count: " + this.mUsbDevice.getConfigurationCount());
            }
        }
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < this.mUsbInterfaces.size(); i4++) {
            UsbInterfaceDescriptor usbInterfaceDescriptor = this.mUsbInterfaces.get(i4);
            for (int i5 = 0; i5 < usbInterfaceDescriptor.getNumEndpoints(); i5++) {
                UsbEndpointDescriptor endpointDescriptor = usbInterfaceDescriptor.getEndpointDescriptor(i5);
                if (endpointDescriptor.getDirection() == 0) {
                    i3 += getNumJacks(endpointDescriptor);
                } else {
                    i2 += getNumJacks(endpointDescriptor);
                }
            }
        }
        this.mNumInputs = i2;
        this.mNumOutputs = i3;
        Log.d(TAG, "Created UsbDirectMidiDevice with " + i2 + " inputs and " + i3 + " outputs. isUniversalMidiDevice: " + z);
        this.mMidiInputPortReceivers = new InputReceiverProxy[i3];
        for (int i6 = 0; i6 < i3; i6++) {
            this.mMidiInputPortReceivers[i6] = new InputReceiverProxy();
        }
        this.mPowerBoostSetter = new PowerBoostSetter();
    }

    private int calculateDefaultMidiProtocol() {
        UsbManager usbManager = (UsbManager) this.mContext.getSystemService(UsbManager.class);
        for (int i = 0; i < this.mUsbInterfaces.size(); i++) {
            UsbInterfaceDescriptor usbInterfaceDescriptor = this.mUsbInterfaces.get(i);
            boolean z = false;
            boolean z2 = false;
            for (int i2 = 0; i2 < usbInterfaceDescriptor.getNumEndpoints() && (!z || !z2); i2++) {
                if (usbInterfaceDescriptor.getEndpointDescriptor(i2).getDirection() == 0) {
                    z2 = true;
                } else {
                    z = true;
                }
            }
            if (z && z2) {
                UsbDeviceConnection openDevice = usbManager.openDevice(this.mUsbDevice);
                if (updateUsbInterface(usbInterfaceDescriptor.toAndroid(this.mParser), openDevice)) {
                    int calculateMidiType = this.mMidiBlockParser.calculateMidiType(openDevice, usbInterfaceDescriptor.getInterfaceNumber(), usbInterfaceDescriptor.getAlternateSetting());
                    openDevice.close();
                    return calculateMidiType;
                }
            }
        }
        Log.w(TAG, "Cannot find interface with both input and output endpoints");
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean openLocked() {
        Log.d(TAG, "openLocked()");
        UsbManager usbManager = (UsbManager) this.mContext.getSystemService(UsbManager.class);
        this.mUsbDeviceConnections = new ArrayList<>();
        this.mInputUsbEndpoints = new ArrayList<>();
        this.mOutputUsbEndpoints = new ArrayList<>();
        this.mInputUsbEndpointCableCounts = new ArrayList<>();
        this.mOutputUsbEndpointCableCounts = new ArrayList<>();
        this.mMidiEventMultiSchedulers = new ArrayList<>();
        this.mThreads = new ArrayList<>();
        for (int i = 0; i < this.mUsbInterfaces.size(); i++) {
            ArrayList<UsbEndpoint> arrayList = new ArrayList<>();
            ArrayList<UsbEndpoint> arrayList2 = new ArrayList<>();
            ArrayList<Integer> arrayList3 = new ArrayList<>();
            ArrayList<Integer> arrayList4 = new ArrayList<>();
            ArrayList<MidiEventMultiScheduler> arrayList5 = new ArrayList<>();
            UsbInterfaceDescriptor usbInterfaceDescriptor = this.mUsbInterfaces.get(i);
            for (int i2 = 0; i2 < usbInterfaceDescriptor.getNumEndpoints(); i2++) {
                UsbEndpointDescriptor endpointDescriptor = usbInterfaceDescriptor.getEndpointDescriptor(i2);
                if (endpointDescriptor.getDirection() == 0) {
                    arrayList2.add(endpointDescriptor.toAndroid(this.mParser));
                    arrayList4.add(Integer.valueOf(getNumJacks(endpointDescriptor)));
                    arrayList5.add(new MidiEventMultiScheduler(getNumJacks(endpointDescriptor)));
                } else {
                    arrayList.add(endpointDescriptor.toAndroid(this.mParser));
                    arrayList3.add(Integer.valueOf(getNumJacks(endpointDescriptor)));
                }
            }
            if (!arrayList2.isEmpty() || !arrayList.isEmpty()) {
                UsbDeviceConnection openDevice = usbManager.openDevice(this.mUsbDevice);
                if (updateUsbInterface(usbInterfaceDescriptor.toAndroid(this.mParser), openDevice)) {
                    this.mUsbDeviceConnections.add(openDevice);
                    this.mInputUsbEndpoints.add(arrayList);
                    this.mOutputUsbEndpoints.add(arrayList2);
                    this.mInputUsbEndpointCableCounts.add(arrayList3);
                    this.mOutputUsbEndpointCableCounts.add(arrayList4);
                    this.mMidiEventMultiSchedulers.add(arrayList5);
                }
            }
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.mMidiEventMultiSchedulers.size(); i4++) {
            for (int i5 = 0; i5 < this.mMidiEventMultiSchedulers.get(i4).size(); i5++) {
                int intValue = this.mOutputUsbEndpointCableCounts.get(i4).get(i5).intValue();
                MidiEventMultiScheduler midiEventMultiScheduler = this.mMidiEventMultiSchedulers.get(i4).get(i5);
                for (int i6 = 0; i6 < intValue; i6++) {
                    this.mMidiInputPortReceivers[i3].setReceiver(midiEventMultiScheduler.getEventScheduler(i6).getReceiver());
                    i3++;
                }
            }
        }
        final MidiReceiver[] outputPortReceivers = this.mServer.getOutputPortReceivers();
        int i7 = 0;
        int i8 = 0;
        while (i8 < this.mInputUsbEndpoints.size()) {
            int i9 = i7;
            for (int i10 = 0; i10 < this.mInputUsbEndpoints.get(i8).size(); i10++) {
                final UsbDeviceConnection usbDeviceConnection = this.mUsbDeviceConnections.get(i8);
                final UsbEndpoint usbEndpoint = this.mInputUsbEndpoints.get(i8).get(i10);
                final int intValue2 = this.mInputUsbEndpointCableCounts.get(i8).get(i10).intValue();
                final int i11 = i9;
                Thread thread = new Thread("UsbDirectMidiDevice input thread " + i9) { // from class: com.android.server.usb.UsbDirectMidiDevice.2
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        byte[] pullDecodedMidiPackets;
                        int i12;
                        int i13;
                        UsbRequest usbRequest = new UsbRequest();
                        UsbMidiPacketConverter usbMidiPacketConverter = new UsbMidiPacketConverter();
                        usbMidiPacketConverter.createDecoders(intValue2);
                        try {
                            try {
                                usbRequest.initialize(usbDeviceConnection, usbEndpoint);
                                byte[] bArr = new byte[usbEndpoint.getMaxPacketSize()];
                                int i14 = 1;
                                while (true) {
                                    if (i14 == 0) {
                                        break;
                                    }
                                    Thread.currentThread();
                                    if (Thread.interrupted()) {
                                        Log.w(UsbDirectMidiDevice.TAG, "input thread interrupted");
                                        break;
                                    }
                                    ByteBuffer wrap = ByteBuffer.wrap(bArr);
                                    if (!usbRequest.queue(wrap)) {
                                        Log.w(UsbDirectMidiDevice.TAG, "Cannot queue request");
                                        break;
                                    }
                                    UsbRequest requestWait = usbDeviceConnection.requestWait();
                                    if (requestWait == null) {
                                        Log.w(UsbDirectMidiDevice.TAG, "Response is null");
                                        break;
                                    }
                                    if (usbRequest != requestWait) {
                                        Log.w(UsbDirectMidiDevice.TAG, "Skipping response");
                                    } else {
                                        long nanoTime = System.nanoTime();
                                        int position = wrap.position();
                                        if (position > 0) {
                                            int i15 = 0;
                                            UsbDirectMidiDevice.logByteArray("Input before conversion ", bArr, 0, position);
                                            if (!UsbDirectMidiDevice.this.mIsUniversalMidiDevice) {
                                                usbMidiPacketConverter.decodeMidiPackets(bArr, position);
                                            }
                                            int i16 = 0;
                                            while (i16 < intValue2) {
                                                if (UsbDirectMidiDevice.this.mIsUniversalMidiDevice) {
                                                    pullDecodedMidiPackets = UsbDirectMidiDevice.this.swapEndiannessPerWord(bArr, position);
                                                } else {
                                                    pullDecodedMidiPackets = usbMidiPacketConverter.pullDecodedMidiPackets(i16);
                                                }
                                                byte[] bArr2 = pullDecodedMidiPackets;
                                                UsbDirectMidiDevice.logByteArray("Input " + i16 + " after conversion ", bArr2, i15, bArr2.length);
                                                if (bArr2.length != 0) {
                                                    MidiReceiver[] midiReceiverArr = outputPortReceivers;
                                                    if (midiReceiverArr != null) {
                                                        int i17 = i11;
                                                        if (midiReceiverArr[i17 + i16] != null) {
                                                            i12 = i15;
                                                            i13 = i16;
                                                            midiReceiverArr[i17 + i16].send(bArr2, 0, bArr2.length, nanoTime);
                                                            if (UsbDirectMidiDevice.this.mPowerBoostSetter != null) {
                                                                if (bArr2.length > 1 && (!UsbDirectMidiDevice.this.mIsUniversalMidiDevice || UsbDirectMidiDevice.this.isChannelVoiceMessage(bArr2))) {
                                                                    UsbDirectMidiDevice.this.mPowerBoostSetter.boostPower();
                                                                }
                                                                i16 = i13 + 1;
                                                                i15 = i12;
                                                            }
                                                        }
                                                    }
                                                    int i18 = i15;
                                                    Log.w(UsbDirectMidiDevice.TAG, "outputReceivers is null");
                                                    i14 = i18;
                                                    break;
                                                }
                                                i12 = i15;
                                                i13 = i16;
                                                i16 = i13 + 1;
                                                i15 = i12;
                                            }
                                        }
                                    }
                                }
                            } catch (IOException unused) {
                                Log.d(UsbDirectMidiDevice.TAG, "reader thread exiting");
                            } catch (NullPointerException e) {
                                Log.e(UsbDirectMidiDevice.TAG, "input thread: ", e);
                            }
                            usbRequest.close();
                            Log.d(UsbDirectMidiDevice.TAG, "input thread exit");
                        } catch (Throwable th) {
                            usbRequest.close();
                            throw th;
                        }
                    }
                };
                thread.start();
                this.mThreads.add(thread);
                i9 += intValue2;
            }
            i8++;
            i7 = i9;
        }
        int i12 = 0;
        for (int i13 = 0; i13 < this.mOutputUsbEndpoints.size(); i13++) {
            for (int i14 = 0; i14 < this.mOutputUsbEndpoints.get(i13).size(); i14++) {
                final UsbDeviceConnection usbDeviceConnection2 = this.mUsbDeviceConnections.get(i13);
                final UsbEndpoint usbEndpoint2 = this.mOutputUsbEndpoints.get(i13).get(i14);
                final int intValue3 = this.mOutputUsbEndpointCableCounts.get(i13).get(i14).intValue();
                final MidiEventMultiScheduler midiEventMultiScheduler2 = this.mMidiEventMultiSchedulers.get(i13).get(i14);
                Thread thread2 = new Thread("UsbDirectMidiDevice output write thread " + i12) { // from class: com.android.server.usb.UsbDirectMidiDevice.3
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        byte[] pullEncodedMidiPackets;
                        try {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            UsbMidiPacketConverter usbMidiPacketConverter = new UsbMidiPacketConverter();
                            usbMidiPacketConverter.createEncoders(intValue3);
                            int i15 = 0;
                            boolean z = false;
                            while (true) {
                                if (z) {
                                    break;
                                }
                                if (!midiEventMultiScheduler2.waitNextEvent()) {
                                    Log.d(UsbDirectMidiDevice.TAG, "output thread closed");
                                    break;
                                }
                                long nanoTime = System.nanoTime();
                                for (int i16 = i15; i16 < intValue3; i16++) {
                                    MidiEventScheduler eventScheduler = midiEventMultiScheduler2.getEventScheduler(i16);
                                    for (MidiEventScheduler.MidiEvent nextEvent = eventScheduler.getNextEvent(nanoTime); nextEvent != null; nextEvent = (MidiEventScheduler.MidiEvent) eventScheduler.getNextEvent(nanoTime)) {
                                        UsbDirectMidiDevice.logByteArray("Output before conversion ", nextEvent.data, i15, nextEvent.count);
                                        if (UsbDirectMidiDevice.this.mIsUniversalMidiDevice) {
                                            byte[] swapEndiannessPerWord = UsbDirectMidiDevice.this.swapEndiannessPerWord(nextEvent.data, nextEvent.count);
                                            byteArrayOutputStream.write(swapEndiannessPerWord, i15, swapEndiannessPerWord.length);
                                        } else {
                                            usbMidiPacketConverter.encodeMidiPackets(nextEvent.data, nextEvent.count, i16);
                                        }
                                        eventScheduler.addEventToPool(nextEvent);
                                    }
                                }
                                Thread.currentThread();
                                if (Thread.interrupted()) {
                                    Log.d(UsbDirectMidiDevice.TAG, "output thread interrupted");
                                    break;
                                }
                                if (UsbDirectMidiDevice.this.mIsUniversalMidiDevice) {
                                    pullEncodedMidiPackets = byteArrayOutputStream.toByteArray();
                                    byteArrayOutputStream.reset();
                                } else {
                                    pullEncodedMidiPackets = usbMidiPacketConverter.pullEncodedMidiPackets();
                                }
                                UsbDirectMidiDevice.logByteArray("Output after conversion ", pullEncodedMidiPackets, i15, pullEncodedMidiPackets.length);
                                int i17 = i15;
                                while (i17 < pullEncodedMidiPackets.length && !z) {
                                    int min = Math.min(usbEndpoint2.getMaxPacketSize(), pullEncodedMidiPackets.length - i17);
                                    int i18 = -1;
                                    int i19 = i15;
                                    while (true) {
                                        if (i18 >= 0 || i19 > 20) {
                                            break;
                                        }
                                        i18 = usbDeviceConnection2.bulkTransfer(usbEndpoint2, pullEncodedMidiPackets, i17, min, 50);
                                        i19++;
                                        Thread.currentThread();
                                        if (Thread.interrupted()) {
                                            Log.w(UsbDirectMidiDevice.TAG, "output thread interrupted after send");
                                            z = true;
                                            break;
                                        } else if (i18 < 0) {
                                            Log.d(UsbDirectMidiDevice.TAG, "retrying packet. retryCount = " + i19 + " result = " + i18);
                                            if (i19 > 20) {
                                                Log.w(UsbDirectMidiDevice.TAG, "Skipping packet because timeout");
                                            }
                                        }
                                    }
                                    i17 += usbEndpoint2.getMaxPacketSize();
                                    i15 = 0;
                                }
                                i15 = 0;
                            }
                        } catch (InterruptedException e) {
                            Log.w(UsbDirectMidiDevice.TAG, "output thread: ", e);
                        } catch (NullPointerException e2) {
                            Log.e(UsbDirectMidiDevice.TAG, "output thread: ", e2);
                        }
                        Log.d(UsbDirectMidiDevice.TAG, "output thread exit");
                    }
                };
                thread2.start();
                this.mThreads.add(thread2);
                i12 += intValue3;
            }
        }
        this.mIsOpen = true;
        return true;
    }

    private boolean register(Context context) {
        String str;
        String str2;
        this.mContext = context;
        MidiManager midiManager = (MidiManager) context.getSystemService(MidiManager.class);
        if (midiManager == null) {
            Log.e(TAG, "No MidiManager in UsbDirectMidiDevice.register()");
            return false;
        }
        if (this.mIsUniversalMidiDevice) {
            this.mDefaultMidiProtocol = calculateDefaultMidiProtocol();
        } else {
            this.mDefaultMidiProtocol = -1;
        }
        Bundle bundle = new Bundle();
        String manufacturerName = this.mUsbDevice.getManufacturerName();
        String productName = this.mUsbDevice.getProductName();
        String version = this.mUsbDevice.getVersion();
        if (manufacturerName == null || manufacturerName.isEmpty()) {
            str = productName;
        } else if (productName == null || productName.isEmpty()) {
            str = manufacturerName;
        } else {
            str = manufacturerName + " " + productName;
        }
        String str3 = str + "#" + this.mUniqueUsbDeviceIdentifier;
        if (this.mIsUniversalMidiDevice) {
            str2 = str3 + " MIDI 2.0";
        } else {
            str2 = str3 + " MIDI 1.0";
        }
        this.mName = str2;
        bundle.putString("name", str2);
        bundle.putString("manufacturer", manufacturerName);
        bundle.putString("product", productName);
        bundle.putString("version", version);
        bundle.putString("serial_number", this.mUsbDevice.getSerialNumber());
        bundle.putParcelable("usb_device", this.mUsbDevice);
        this.mServerAvailable = true;
        MidiDeviceServer createDeviceServer = midiManager.createDeviceServer(this.mMidiInputPortReceivers, this.mNumInputs, null, null, bundle, 1, this.mDefaultMidiProtocol, this.mCallback);
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
        Log.d(TAG, "closeLocked()");
        Iterator<Thread> it = this.mThreads.iterator();
        while (it.hasNext()) {
            Thread next = it.next();
            if (next != null) {
                next.interrupt();
            }
        }
        Iterator<Thread> it2 = this.mThreads.iterator();
        while (it2.hasNext()) {
            Thread next2 = it2.next();
            if (next2 != null) {
                try {
                    next2.join(200L);
                } catch (InterruptedException unused) {
                    Log.w(TAG, "thread join interrupted");
                }
            }
        }
        this.mThreads = null;
        int i = 0;
        while (true) {
            InputReceiverProxy[] inputReceiverProxyArr = this.mMidiInputPortReceivers;
            if (i >= inputReceiverProxyArr.length) {
                break;
            }
            inputReceiverProxyArr[i].setReceiver(null);
            i++;
        }
        for (int i2 = 0; i2 < this.mMidiEventMultiSchedulers.size(); i2++) {
            for (int i3 = 0; i3 < this.mMidiEventMultiSchedulers.get(i2).size(); i3++) {
                this.mMidiEventMultiSchedulers.get(i2).get(i3).close();
            }
        }
        this.mMidiEventMultiSchedulers = null;
        Iterator<UsbDeviceConnection> it3 = this.mUsbDeviceConnections.iterator();
        while (it3.hasNext()) {
            it3.next().close();
        }
        this.mUsbDeviceConnections = null;
        this.mInputUsbEndpoints = null;
        this.mOutputUsbEndpoints = null;
        this.mInputUsbEndpointCableCounts = null;
        this.mOutputUsbEndpointCableCounts = null;
        this.mIsOpen = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] swapEndiannessPerWord(byte[] bArr, int i) {
        int i2 = i & 3;
        if (i2 != 0) {
            Log.e(TAG, "size not multiple of 4: " + i);
        }
        byte[] bArr2 = new byte[i - i2];
        int i3 = 0;
        while (true) {
            int i4 = i3 + 3;
            if (i4 >= i) {
                return bArr2;
            }
            bArr2[i3] = bArr[i4];
            int i5 = i3 + 1;
            int i6 = i3 + 2;
            bArr2[i5] = bArr[i6];
            bArr2[i6] = bArr[i5];
            bArr2[i4] = bArr[i3];
            i3 += 4;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logByteArray(String str, byte[] bArr, int i, int i2) {
        StringBuilder sb = new StringBuilder(str);
        for (int i3 = i; i3 < i + i2; i3++) {
            sb.append(String.format("0x%02X", Byte.valueOf(bArr[i3])));
            if (i3 != bArr.length - 1) {
                sb.append(", ");
            }
        }
        Log.d(TAG, sb.toString());
    }

    private boolean updateUsbInterface(UsbInterface usbInterface, UsbDeviceConnection usbDeviceConnection) {
        if (usbInterface == null) {
            Log.e(TAG, "Usb Interface is null");
            return false;
        }
        if (usbDeviceConnection == null) {
            Log.e(TAG, "UsbDeviceConnection is null");
            return false;
        }
        if (!usbDeviceConnection.claimInterface(usbInterface, true)) {
            Log.e(TAG, "Can't claim interface");
            return false;
        }
        if (this.mShouldCallSetInterface) {
            if (!usbDeviceConnection.setInterface(usbInterface)) {
                Log.w(TAG, "Can't set interface");
            }
        } else {
            Log.w(TAG, "no alternate interface");
        }
        return true;
    }

    private boolean areEquivalent(UsbInterface usbInterface, UsbInterface usbInterface2) {
        if (usbInterface.getId() != usbInterface2.getId() || usbInterface.getAlternateSetting() != usbInterface2.getAlternateSetting() || usbInterface.getInterfaceClass() != usbInterface2.getInterfaceClass() || usbInterface.getInterfaceSubclass() != usbInterface2.getInterfaceSubclass() || usbInterface.getInterfaceProtocol() != usbInterface2.getInterfaceProtocol() || usbInterface.getEndpointCount() != usbInterface2.getEndpointCount()) {
            return false;
        }
        if (usbInterface.getName() == null) {
            if (usbInterface2.getName() != null) {
                return false;
            }
        } else if (!usbInterface.getName().equals(usbInterface2.getName())) {
            return false;
        }
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint endpoint = usbInterface.getEndpoint(i);
            UsbEndpoint endpoint2 = usbInterface2.getEndpoint(i);
            if (endpoint.getAddress() != endpoint2.getAddress() || endpoint.getAttributes() != endpoint2.getAttributes() || endpoint.getMaxPacketSize() != endpoint2.getMaxPacketSize() || endpoint.getInterval() != endpoint2.getInterval()) {
                return false;
            }
        }
        return true;
    }

    public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        long start = dualDumpOutputStream.start(str, j);
        dualDumpOutputStream.write("num_inputs", 1120986464257L, this.mNumInputs);
        dualDumpOutputStream.write("num_outputs", 1120986464258L, this.mNumOutputs);
        dualDumpOutputStream.write("is_universal", 1133871366147L, this.mIsUniversalMidiDevice);
        dualDumpOutputStream.write("name", 1138166333444L, this.mName);
        if (this.mIsUniversalMidiDevice) {
            this.mMidiBlockParser.dump(dualDumpOutputStream, "block_parser", 1146756268037L);
        }
        dualDumpOutputStream.end(start);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChannelVoiceMessage(byte[] bArr) {
        byte b = (byte) ((bArr[0] >> 4) & 15);
        return b == 2 || b == 4;
    }

    private int getNumJacks(UsbEndpointDescriptor usbEndpointDescriptor) {
        UsbDescriptor classSpecificEndpointDescriptor = usbEndpointDescriptor.getClassSpecificEndpointDescriptor();
        if (classSpecificEndpointDescriptor == null || !(classSpecificEndpointDescriptor instanceof UsbACMidi10Endpoint)) {
            return 1;
        }
        return ((UsbACMidi10Endpoint) classSpecificEndpointDescriptor).getNumJacks();
    }
}
