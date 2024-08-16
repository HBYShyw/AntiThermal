package com.android.server.usb;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.usb.UsbDevice;
import android.media.AudioManager;
import android.media.IAudioService;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.alsa.AlsaCardsParser;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.usb.descriptors.UsbDescriptorParser;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import libcore.io.IoUtils;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbAlsaManager {
    private static final int ALSA_DEVICE_TYPE_CAPTURE = 2;
    private static final int ALSA_DEVICE_TYPE_MIDI = 3;
    private static final int ALSA_DEVICE_TYPE_PLAYBACK = 1;
    private static final int ALSA_DEVICE_TYPE_UNKNOWN = 0;
    private static final String ALSA_DIRECTORY = "/dev/snd/";
    private static final boolean DEBUG = false;
    private static final String TAG = "UsbAlsaManager";
    private static final int USB_DENYLIST_INPUT = 2;
    private static final int USB_DENYLIST_OUTPUT = 1;
    private IAudioService mAudioService;
    private final Context mContext;
    private final boolean mHasMidiFeature;
    private static final boolean IS_MULTI_MODE = SystemProperties.getBoolean("ro.audio.multi_usb_mode", false);
    private static final int USB_VENDORID_SONY = 1356;
    private static final int USB_PRODUCTID_PS4CONTROLLER_ZCT1 = 1476;
    private static final int USB_PRODUCTID_PS4CONTROLLER_ZCT2 = 2508;
    private static final int USB_PRODUCTID_PS5CONTROLLER = 3302;
    static final List<DenyListEntry> sDeviceDenylist = Arrays.asList(new DenyListEntry(USB_VENDORID_SONY, USB_PRODUCTID_PS4CONTROLLER_ZCT1, 1), new DenyListEntry(USB_VENDORID_SONY, USB_PRODUCTID_PS4CONTROLLER_ZCT2, 1), new DenyListEntry(USB_VENDORID_SONY, USB_PRODUCTID_PS5CONTROLLER, 1));
    private final AlsaCardsParser mCardsParser = new AlsaCardsParser();
    private final ArrayList<UsbAlsaDevice> mAlsaDevices = new ArrayList<>();
    private HashMap<Integer, Stack<UsbAlsaDevice>> mAttachedDevices = new HashMap<>();
    private final HashMap<String, UsbAlsaMidiDevice> mMidiDevices = new HashMap<>();
    private UsbAlsaMidiDevice mPeripheralMidiDevice = null;
    private final HashSet<Integer> mAlsaCards = new HashSet<>();
    private final FileObserver mAlsaObserver = new FileObserver(new File(ALSA_DIRECTORY), UsbTerminalTypes.TERMINAL_OUT_UNDEFINED) { // from class: com.android.server.usb.UsbAlsaManager.1
        @Override // android.os.FileObserver
        public void onEvent(int i, String str) {
            if (i == 256) {
                UsbAlsaManager.this.alsaFileAdded(str);
            } else {
                if (i != 512) {
                    return;
                }
                UsbAlsaManager.this.alsaFileRemoved(str);
            }
        }
    };

    public void logDevices(String str) {
    }

    public void logDevicesList(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class DenyListEntry {
        final int mFlags;
        final int mProductId;
        final int mVendorId;

        DenyListEntry(int i, int i2, int i3) {
            this.mVendorId = i;
            this.mProductId = i2;
            this.mFlags = i3;
        }
    }

    private static boolean isDeviceDenylisted(int i, int i2, int i3) {
        for (DenyListEntry denyListEntry : sDeviceDenylist) {
            if (denyListEntry.mVendorId == i && denyListEntry.mProductId == i2) {
                return (denyListEntry.mFlags & i3) != 0;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UsbAlsaManager(Context context) {
        this.mContext = context;
        this.mHasMidiFeature = context.getPackageManager().hasSystemFeature("android.software.midi");
    }

    public void systemReady() {
        this.mAudioService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
        this.mAlsaObserver.startWatching();
    }

    private synchronized void selectAlsaDevice(UsbAlsaDevice usbAlsaDevice) {
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "usb_audio_automatic_routing_disabled", 0) != 0) {
            return;
        }
        usbAlsaDevice.start();
    }

    private synchronized void deselectAlsaDevice(UsbAlsaDevice usbAlsaDevice) {
        usbAlsaDevice.stop();
    }

    private int getAlsaDeviceListIndexFor(String str) {
        for (int i = 0; i < this.mAlsaDevices.size(); i++) {
            if (this.mAlsaDevices.get(i).getDeviceAddress().equals(str)) {
                return i;
            }
        }
        return -1;
    }

    private void addDeviceToAttachedDevicesMap(int i, UsbAlsaDevice usbAlsaDevice) {
        if (i == 0) {
            Slog.i(TAG, "Ignore caching device as the type is NONE, device=" + usbAlsaDevice);
            return;
        }
        Stack<UsbAlsaDevice> stack = this.mAttachedDevices.get(Integer.valueOf(i));
        if (stack == null) {
            this.mAttachedDevices.put(Integer.valueOf(i), new Stack<>());
            stack = this.mAttachedDevices.get(Integer.valueOf(i));
        }
        stack.push(usbAlsaDevice);
    }

    private void addAlsaDevice(UsbAlsaDevice usbAlsaDevice) {
        this.mAlsaDevices.add(0, usbAlsaDevice);
        addDeviceToAttachedDevicesMap(usbAlsaDevice.getInputDeviceType(), usbAlsaDevice);
        addDeviceToAttachedDevicesMap(usbAlsaDevice.getOutputDeviceType(), usbAlsaDevice);
    }

    private void removeDeviceFromAttachedDevicesMap(int i, UsbAlsaDevice usbAlsaDevice) {
        Stack<UsbAlsaDevice> stack = this.mAttachedDevices.get(Integer.valueOf(i));
        if (stack == null) {
            return;
        }
        stack.remove(usbAlsaDevice);
        if (stack.isEmpty()) {
            this.mAttachedDevices.remove(Integer.valueOf(i));
        }
    }

    private UsbAlsaDevice removeAlsaDevice(String str) {
        int alsaDeviceListIndexFor = getAlsaDeviceListIndexFor(str);
        if (alsaDeviceListIndexFor <= -1) {
            return null;
        }
        UsbAlsaDevice remove = this.mAlsaDevices.remove(alsaDeviceListIndexFor);
        removeDeviceFromAttachedDevicesMap(remove.getOutputDeviceType(), remove);
        removeDeviceFromAttachedDevicesMap(remove.getInputDeviceType(), remove);
        return remove;
    }

    private UsbAlsaDevice selectDefaultDevice(int i) {
        Stack<UsbAlsaDevice> stack = this.mAttachedDevices.get(Integer.valueOf(i));
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        UsbAlsaDevice peek = stack.peek();
        Slog.d(TAG, "select default device:" + peek);
        if (AudioManager.isInputDevice(i)) {
            peek.startInput();
        } else {
            peek.startOutput();
        }
        return peek;
    }

    private void deselectCurrentDevice(int i) {
        Stack<UsbAlsaDevice> stack;
        if (i == 0 || (stack = this.mAttachedDevices.get(Integer.valueOf(i))) == null || stack.isEmpty()) {
            return;
        }
        UsbAlsaDevice peek = stack.peek();
        Slog.d(TAG, "deselect current device:" + peek);
        if (AudioManager.isInputDevice(i)) {
            peek.stopInput();
        } else {
            peek.stopOutput();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void usbDeviceAdded(String str, UsbDevice usbDevice, UsbDescriptorParser usbDescriptorParser) {
        this.mCardsParser.scan();
        AlsaCardsParser.AlsaCardRecord findCardNumFor = this.mCardsParser.findCardNumFor(str);
        if (findCardNumFor == null) {
            Slog.e(TAG, "usbDeviceAdded(): cannot find sound card for " + str);
            return;
        }
        waitForAlsaDevice(findCardNumFor.getCardNum(), true);
        boolean z = usbDescriptorParser.hasInput() && !isDeviceDenylisted(usbDevice.getVendorId(), usbDevice.getProductId(), 2);
        boolean z2 = usbDescriptorParser.hasOutput() && !isDeviceDenylisted(usbDevice.getVendorId(), usbDevice.getProductId(), 1);
        if (z || z2) {
            boolean isInputHeadset = usbDescriptorParser.isInputHeadset();
            boolean isOutputHeadset = usbDescriptorParser.isOutputHeadset();
            boolean isDock = usbDescriptorParser.isDock();
            if (this.mAudioService == null) {
                Slog.e(TAG, "no AudioService");
                return;
            }
            UsbAlsaDevice usbAlsaDevice = new UsbAlsaDevice(this.mAudioService, findCardNumFor.getCardNum(), 0, str, z2, z, isInputHeadset, isOutputHeadset, isDock);
            usbAlsaDevice.setDeviceNameAndDescription(findCardNumFor.getCardName(), findCardNumFor.getCardDescription());
            if (IS_MULTI_MODE) {
                deselectCurrentDevice(usbAlsaDevice.getInputDeviceType());
                deselectCurrentDevice(usbAlsaDevice.getOutputDeviceType());
            } else if (!this.mAlsaDevices.isEmpty()) {
                deselectAlsaDevice(this.mAlsaDevices.get(0));
            }
            addAlsaDevice(usbAlsaDevice);
            selectAlsaDevice(usbAlsaDevice);
        }
        addMidiDevice(str, usbDevice, usbDescriptorParser, findCardNumFor);
        logDevices("deviceAdded()");
    }

    private void addMidiDevice(String str, UsbDevice usbDevice, UsbDescriptorParser usbDescriptorParser, AlsaCardsParser.AlsaCardRecord alsaCardRecord) {
        String str2;
        boolean hasMIDIInterface = usbDescriptorParser.hasMIDIInterface();
        boolean containsUniversalMidiDeviceEndpoint = usbDescriptorParser.containsUniversalMidiDeviceEndpoint();
        if (this.mHasMidiFeature && hasMIDIInterface && !containsUniversalMidiDeviceEndpoint) {
            Bundle bundle = new Bundle();
            String manufacturerName = usbDevice.getManufacturerName();
            String productName = usbDevice.getProductName();
            String version = usbDevice.getVersion();
            if (manufacturerName == null || manufacturerName.isEmpty()) {
                str2 = productName;
            } else if (productName == null || productName.isEmpty()) {
                str2 = manufacturerName;
            } else {
                str2 = manufacturerName + " " + productName;
            }
            bundle.putString("name", str2);
            bundle.putString("manufacturer", manufacturerName);
            bundle.putString("product", productName);
            bundle.putString("version", version);
            bundle.putString("serial_number", usbDevice.getSerialNumber());
            bundle.putInt("alsa_card", alsaCardRecord.getCardNum());
            bundle.putInt("alsa_device", 0);
            bundle.putParcelable("usb_device", usbDevice);
            UsbAlsaMidiDevice create = UsbAlsaMidiDevice.create(this.mContext, bundle, alsaCardRecord.getCardNum(), 0, usbDescriptorParser.calculateNumLegacyMidiInputs(), usbDescriptorParser.calculateNumLegacyMidiOutputs());
            if (create != null) {
                this.mMidiDevices.put(str, create);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void usbDeviceRemoved(String str) {
        UsbAlsaDevice removeAlsaDevice = removeAlsaDevice(str);
        String str2 = TAG;
        Slog.i(str2, "USB Audio Device Removed: " + removeAlsaDevice);
        if (removeAlsaDevice != null) {
            waitForAlsaDevice(removeAlsaDevice.getCardNum(), false);
            deselectAlsaDevice(removeAlsaDevice);
            if (IS_MULTI_MODE) {
                selectDefaultDevice(removeAlsaDevice.getOutputDeviceType());
                selectDefaultDevice(removeAlsaDevice.getInputDeviceType());
            } else if (!this.mAlsaDevices.isEmpty() && this.mAlsaDevices.get(0) != null) {
                selectAlsaDevice(this.mAlsaDevices.get(0));
            }
        }
        UsbAlsaMidiDevice remove = this.mMidiDevices.remove(str);
        if (remove != null) {
            Slog.i(str2, "USB MIDI Device Removed: " + str);
            IoUtils.closeQuietly(remove);
        }
        logDevices("usbDeviceRemoved()");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPeripheralMidiState(boolean z, int i, int i2) {
        UsbAlsaMidiDevice usbAlsaMidiDevice;
        if (this.mHasMidiFeature) {
            if (!z || this.mPeripheralMidiDevice != null) {
                if (z || (usbAlsaMidiDevice = this.mPeripheralMidiDevice) == null) {
                    return;
                }
                IoUtils.closeQuietly(usbAlsaMidiDevice);
                this.mPeripheralMidiDevice = null;
                return;
            }
            Bundle bundle = new Bundle();
            Resources resources = this.mContext.getResources();
            bundle.putString("name", resources.getString(17041792));
            bundle.putString("manufacturer", resources.getString(17041791));
            bundle.putString("product", resources.getString(17041793));
            bundle.putInt("alsa_card", i);
            bundle.putInt("alsa_device", i2);
            this.mPeripheralMidiDevice = UsbAlsaMidiDevice.create(this.mContext, bundle, i, i2, 1, 1);
        }
    }

    private boolean waitForAlsaDevice(int i, boolean z) {
        boolean contains;
        synchronized (this.mAlsaCards) {
            long elapsedRealtime = SystemClock.elapsedRealtime() + 2500;
            while ((this.mAlsaCards.contains(Integer.valueOf(i)) ^ z) && elapsedRealtime > SystemClock.elapsedRealtime()) {
                long elapsedRealtime2 = elapsedRealtime - SystemClock.elapsedRealtime();
                if (elapsedRealtime2 > 0) {
                    try {
                        this.mAlsaCards.wait(elapsedRealtime2);
                    } catch (InterruptedException unused) {
                        Slog.d(TAG, "usb: InterruptedException while waiting for ALSA file.");
                    }
                }
            }
            contains = this.mAlsaCards.contains(Integer.valueOf(i));
            if ((z ^ contains) && elapsedRealtime > SystemClock.elapsedRealtime()) {
                Slog.e(TAG, "waitForAlsaDevice(" + i + ") timeout");
            } else {
                Slog.i(TAG, "waitForAlsaDevice for device card=" + i + ", isAdded=" + z + ", found=" + contains);
            }
        }
        return contains;
    }

    private int getCardNumberFromAlsaFilePath(String str) {
        char c;
        if (str.startsWith("pcmC")) {
            if (str.endsWith("p")) {
                c = 1;
            } else {
                if (str.endsWith("c")) {
                    c = 2;
                }
                c = 0;
            }
        } else {
            if (str.startsWith("midiC")) {
                c = 3;
            }
            c = 0;
        }
        if (c == 0) {
            Slog.i(TAG, "Unknown type file(" + str + ") added.");
            return -1;
        }
        try {
            return Integer.parseInt(str.substring(str.indexOf(67) + 1, str.indexOf(68)));
        } catch (Exception e) {
            Slog.e(TAG, "Could not parse ALSA file name " + str, e);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alsaFileAdded(String str) {
        String str2 = TAG;
        Slog.i(str2, "alsaFileAdded(" + str + ")");
        int cardNumberFromAlsaFilePath = getCardNumberFromAlsaFilePath(str);
        if (cardNumberFromAlsaFilePath == -1) {
            return;
        }
        synchronized (this.mAlsaCards) {
            if (!this.mAlsaCards.contains(Integer.valueOf(cardNumberFromAlsaFilePath))) {
                Slog.d(str2, "Adding ALSA device card=" + cardNumberFromAlsaFilePath);
                this.mAlsaCards.add(Integer.valueOf(cardNumberFromAlsaFilePath));
                this.mAlsaCards.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alsaFileRemoved(String str) {
        int cardNumberFromAlsaFilePath = getCardNumberFromAlsaFilePath(str);
        if (cardNumberFromAlsaFilePath == -1) {
            return;
        }
        synchronized (this.mAlsaCards) {
            this.mAlsaCards.remove(Integer.valueOf(cardNumberFromAlsaFilePath));
        }
    }

    public void dump(DualDumpOutputStream dualDumpOutputStream, String str, long j) {
        long start = dualDumpOutputStream.start(str, j);
        dualDumpOutputStream.write("cards_parser", 1120986464257L, this.mCardsParser.getScanStatus());
        Iterator<UsbAlsaDevice> it = this.mAlsaDevices.iterator();
        while (it.hasNext()) {
            it.next().dump(dualDumpOutputStream, "alsa_devices", 2246267895810L);
        }
        for (String str2 : this.mMidiDevices.keySet()) {
            this.mMidiDevices.get(str2).dump(str2, dualDumpOutputStream, "alsa_midi_devices", 2246267895812L);
        }
        dualDumpOutputStream.end(start);
    }
}
