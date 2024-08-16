package com.android.server.usb.descriptors;

import android.hardware.usb.UsbDevice;
import android.util.Log;
import com.android.server.usb.descriptors.report.Reporting;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbDescriptorParser {
    public static final boolean DEBUG = false;
    private static final int DESCRIPTORS_ALLOC_SIZE = 128;
    private static final float IN_HEADSET_TRIGGER = 0.75f;
    private static final int MS_MIDI_1_0 = 256;
    private static final int MS_MIDI_2_0 = 512;
    private static final float OUT_HEADSET_TRIGGER = 0.75f;
    private static final String TAG = "UsbDescriptorParser";
    private int mACInterfacesSpec;
    private UsbConfigDescriptor mCurConfigDescriptor;
    private UsbEndpointDescriptor mCurEndpointDescriptor;
    private UsbInterfaceDescriptor mCurInterfaceDescriptor;
    private final ArrayList<UsbDescriptor> mDescriptors;
    private final String mDeviceAddr;
    private UsbDeviceDescriptor mDeviceDescriptor;
    private int mVCInterfacesSpec;

    private native String getDescriptorString_native(String str, int i);

    private native byte[] getRawDescriptors_native(String str);

    public UsbDescriptorParser(String str, ArrayList<UsbDescriptor> arrayList) {
        this.mACInterfacesSpec = 256;
        this.mVCInterfacesSpec = 256;
        this.mDeviceAddr = str;
        this.mDescriptors = arrayList;
        this.mDeviceDescriptor = (UsbDeviceDescriptor) arrayList.get(0);
    }

    public UsbDescriptorParser(String str, byte[] bArr) {
        this.mACInterfacesSpec = 256;
        this.mVCInterfacesSpec = 256;
        this.mDeviceAddr = str;
        this.mDescriptors = new ArrayList<>(128);
        parseDescriptors(bArr);
    }

    public String getDeviceAddr() {
        return this.mDeviceAddr;
    }

    public int getUsbSpec() {
        UsbDeviceDescriptor usbDeviceDescriptor = this.mDeviceDescriptor;
        if (usbDeviceDescriptor != null) {
            return usbDeviceDescriptor.getSpec();
        }
        throw new IllegalArgumentException();
    }

    public void setACInterfaceSpec(int i) {
        this.mACInterfacesSpec = i;
    }

    public int getACInterfaceSpec() {
        return this.mACInterfacesSpec;
    }

    public void setVCInterfaceSpec(int i) {
        this.mVCInterfacesSpec = i;
    }

    public int getVCInterfaceSpec() {
        return this.mVCInterfacesSpec;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class UsbDescriptorsStreamFormatException extends Exception {
        String mMessage;

        UsbDescriptorsStreamFormatException(String str) {
            this.mMessage = str;
        }

        @Override // java.lang.Throwable
        public String toString() {
            return "Descriptor Stream Format Exception: " + this.mMessage;
        }
    }

    private UsbDescriptor allocDescriptor(ByteStream byteStream) throws UsbDescriptorsStreamFormatException {
        UsbDescriptor usbDescriptor;
        UsbInterfaceDescriptor usbInterfaceDescriptor;
        UsbDescriptor allocDescriptor;
        UsbEndpointDescriptor usbEndpointDescriptor;
        byteStream.resetReadCount();
        int unsignedByte = byteStream.getUnsignedByte();
        byte b = byteStream.getByte();
        UsbDescriptor.logDescriptorName(b, unsignedByte);
        if (b == 1) {
            UsbDeviceDescriptor usbDeviceDescriptor = new UsbDeviceDescriptor(unsignedByte, b);
            this.mDeviceDescriptor = usbDeviceDescriptor;
            usbDescriptor = usbDeviceDescriptor;
        } else if (b == 2) {
            UsbConfigDescriptor usbConfigDescriptor = new UsbConfigDescriptor(unsignedByte, b);
            this.mCurConfigDescriptor = usbConfigDescriptor;
            UsbDeviceDescriptor usbDeviceDescriptor2 = this.mDeviceDescriptor;
            if (usbDeviceDescriptor2 != null) {
                usbDeviceDescriptor2.addConfigDescriptor(usbConfigDescriptor);
                usbDescriptor = usbConfigDescriptor;
            } else {
                Log.e(TAG, "Config Descriptor found with no associated Device Descriptor!");
                throw new UsbDescriptorsStreamFormatException("Config Descriptor found with no associated Device Descriptor!");
            }
        } else if (b == 4) {
            UsbInterfaceDescriptor usbInterfaceDescriptor2 = new UsbInterfaceDescriptor(unsignedByte, b);
            this.mCurInterfaceDescriptor = usbInterfaceDescriptor2;
            UsbConfigDescriptor usbConfigDescriptor2 = this.mCurConfigDescriptor;
            if (usbConfigDescriptor2 != null) {
                usbConfigDescriptor2.addInterfaceDescriptor(usbInterfaceDescriptor2);
                usbDescriptor = usbInterfaceDescriptor2;
            } else {
                Log.e(TAG, "Interface Descriptor found with no associated Config Descriptor!");
                throw new UsbDescriptorsStreamFormatException("Interface Descriptor found with no associated Config Descriptor!");
            }
        } else if (b == 5) {
            UsbEndpointDescriptor usbEndpointDescriptor2 = new UsbEndpointDescriptor(unsignedByte, b);
            this.mCurEndpointDescriptor = usbEndpointDescriptor2;
            UsbInterfaceDescriptor usbInterfaceDescriptor3 = this.mCurInterfaceDescriptor;
            if (usbInterfaceDescriptor3 != null) {
                usbInterfaceDescriptor3.addEndpointDescriptor(usbEndpointDescriptor2);
                usbDescriptor = usbEndpointDescriptor2;
            } else {
                Log.e(TAG, "Endpoint Descriptor found with no associated Interface Descriptor!");
                throw new UsbDescriptorsStreamFormatException("Endpoint Descriptor found with no associated Interface Descriptor!");
            }
        } else if (b == 11) {
            usbDescriptor = new UsbInterfaceAssoc(unsignedByte, b);
        } else if (b != 33) {
            usbDescriptor = null;
            r7 = null;
            UsbDescriptor usbDescriptor2 = null;
            usbDescriptor = null;
            usbDescriptor = null;
            usbDescriptor = null;
            usbDescriptor = null;
            if (b == 36) {
                UsbInterfaceDescriptor usbInterfaceDescriptor4 = this.mCurInterfaceDescriptor;
                if (usbInterfaceDescriptor4 != null) {
                    int usbClass = usbInterfaceDescriptor4.getUsbClass();
                    if (usbClass == 1) {
                        UsbDescriptor allocDescriptor2 = UsbACInterface.allocDescriptor(this, byteStream, unsignedByte, b);
                        boolean z = allocDescriptor2 instanceof UsbMSMidiHeader;
                        usbDescriptor = allocDescriptor2;
                        if (z) {
                            this.mCurInterfaceDescriptor.setMidiHeaderInterfaceDescriptor(allocDescriptor2);
                            usbDescriptor = allocDescriptor2;
                        }
                    } else if (usbClass == 14) {
                        usbDescriptor = UsbVCInterface.allocDescriptor(this, byteStream, unsignedByte, b);
                    } else if (usbClass != 16) {
                        Log.w(TAG, "  Unparsed Class-specific");
                    }
                }
            } else if (b == 37 && (usbInterfaceDescriptor = this.mCurInterfaceDescriptor) != null) {
                int usbClass2 = usbInterfaceDescriptor.getUsbClass();
                if (usbClass2 == 1) {
                    allocDescriptor = UsbACEndpoint.allocDescriptor(this, unsignedByte, b, byteStream.getByte());
                } else if (usbClass2 == 14) {
                    allocDescriptor = UsbVCEndpoint.allocDescriptor(this, unsignedByte, b, byteStream.getByte());
                } else {
                    if (usbClass2 != 16) {
                        Log.w(TAG, "  Unparsed Class-specific Endpoint:0x" + Integer.toHexString(usbClass2));
                    }
                    usbEndpointDescriptor = this.mCurEndpointDescriptor;
                    usbDescriptor = usbDescriptor2;
                    usbDescriptor = usbDescriptor2;
                    if (usbEndpointDescriptor != null && usbDescriptor2 != null) {
                        usbEndpointDescriptor.setClassSpecificEndpointDescriptor(usbDescriptor2);
                        usbDescriptor = usbDescriptor2;
                    }
                }
                usbDescriptor2 = allocDescriptor;
                usbEndpointDescriptor = this.mCurEndpointDescriptor;
                usbDescriptor = usbDescriptor2;
                usbDescriptor = usbDescriptor2;
                if (usbEndpointDescriptor != null) {
                    usbEndpointDescriptor.setClassSpecificEndpointDescriptor(usbDescriptor2);
                    usbDescriptor = usbDescriptor2;
                }
            }
        } else {
            usbDescriptor = new UsbHIDDescriptor(unsignedByte, b);
        }
        return usbDescriptor == null ? new UsbUnknown(unsignedByte, b) : usbDescriptor;
    }

    public UsbDeviceDescriptor getDeviceDescriptor() {
        return this.mDeviceDescriptor;
    }

    public UsbInterfaceDescriptor getCurInterface() {
        return this.mCurInterfaceDescriptor;
    }

    public void parseDescriptors(byte[] bArr) {
        UsbDescriptor usbDescriptor;
        ByteStream byteStream = new ByteStream(bArr);
        while (byteStream.available() > 0) {
            try {
                usbDescriptor = allocDescriptor(byteStream);
            } catch (Exception e) {
                Log.e(TAG, "Exception allocating USB descriptor.", e);
                usbDescriptor = null;
            }
            if (usbDescriptor != null) {
                try {
                    try {
                        usbDescriptor.parseRawDescriptors(byteStream);
                        usbDescriptor.postParse(byteStream);
                    } catch (Exception e2) {
                        usbDescriptor.postParse(byteStream);
                        Log.w(TAG, "Exception parsing USB descriptors. type:0x" + ((int) usbDescriptor.getType()) + " status:" + usbDescriptor.getStatus());
                        StackTraceElement[] stackTrace = e2.getStackTrace();
                        if (stackTrace.length > 0) {
                            Log.i(TAG, "  class:" + stackTrace[0].getClassName() + " @ " + stackTrace[0].getLineNumber());
                        }
                        if (stackTrace.length > 1) {
                            Log.i(TAG, "  class:" + stackTrace[1].getClassName() + " @ " + stackTrace[1].getLineNumber());
                        }
                        usbDescriptor.setStatus(4);
                    }
                } finally {
                    this.mDescriptors.add(usbDescriptor);
                }
            }
        }
    }

    public byte[] getRawDescriptors() {
        return getRawDescriptors_native(this.mDeviceAddr);
    }

    public String getDescriptorString(int i) {
        return getDescriptorString_native(this.mDeviceAddr, i);
    }

    public int getParsingSpec() {
        UsbDeviceDescriptor usbDeviceDescriptor = this.mDeviceDescriptor;
        if (usbDeviceDescriptor != null) {
            return usbDeviceDescriptor.getSpec();
        }
        return 0;
    }

    public ArrayList<UsbDescriptor> getDescriptors() {
        return this.mDescriptors;
    }

    public UsbDevice.Builder toAndroidUsbDeviceBuilder() {
        UsbDeviceDescriptor usbDeviceDescriptor = this.mDeviceDescriptor;
        if (usbDeviceDescriptor == null) {
            Log.e(TAG, "toAndroidUsbDevice() ERROR - No Device Descriptor");
            return null;
        }
        UsbDevice.Builder android = usbDeviceDescriptor.toAndroid(this);
        if (android == null) {
            Log.e(TAG, "toAndroidUsbDevice() ERROR Creating Device");
        }
        return android;
    }

    public ArrayList<UsbDescriptor> getDescriptors(byte b) {
        ArrayList<UsbDescriptor> arrayList = new ArrayList<>();
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next.getType() == b) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public ArrayList<UsbDescriptor> getInterfaceDescriptorsForClass(int i) {
        ArrayList<UsbDescriptor> arrayList = new ArrayList<>();
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next.getType() == 4) {
                if (next instanceof UsbInterfaceDescriptor) {
                    if (((UsbInterfaceDescriptor) next).getUsbClass() == i) {
                        arrayList.add(next);
                    }
                } else {
                    Log.w(TAG, "Unrecognized Interface l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<UsbDescriptor> getACInterfaceDescriptors(byte b, int i) {
        ArrayList<UsbDescriptor> arrayList = new ArrayList<>();
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next.getType() == 36) {
                if (next instanceof UsbACInterface) {
                    UsbACInterface usbACInterface = (UsbACInterface) next;
                    if (usbACInterface.getSubtype() == b && usbACInterface.getSubclass() == i) {
                        arrayList.add(next);
                    }
                } else {
                    Log.w(TAG, "Unrecognized Audio Interface len: " + next.getLength() + " type:0x" + Integer.toHexString(next.getType()));
                }
            }
        }
        return arrayList;
    }

    public boolean hasInput() {
        Iterator<UsbDescriptor> it = getACInterfaceDescriptors((byte) 2, 1).iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbACTerminal) {
                int terminalType = ((UsbACTerminal) next).getTerminalType() & (-256);
                if (terminalType != 256 && terminalType != 768) {
                    return true;
                }
            } else {
                Log.w(TAG, "Undefined Audio Input terminal l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        return false;
    }

    public boolean hasOutput() {
        Iterator<UsbDescriptor> it = getACInterfaceDescriptors((byte) 3, 1).iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbACTerminal) {
                int terminalType = ((UsbACTerminal) next).getTerminalType() & (-256);
                if (terminalType != 256 && terminalType != 512) {
                    return true;
                }
            } else {
                Log.w(TAG, "Undefined Audio Input terminal l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        return false;
    }

    public boolean hasMic() {
        Iterator<UsbDescriptor> it = getACInterfaceDescriptors((byte) 2, 1).iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbACTerminal) {
                UsbACTerminal usbACTerminal = (UsbACTerminal) next;
                if (usbACTerminal.getTerminalType() == 513 || usbACTerminal.getTerminalType() == 1026 || usbACTerminal.getTerminalType() == 1024 || usbACTerminal.getTerminalType() == 1539) {
                    return true;
                }
            } else {
                Log.w(TAG, "Undefined Audio Input terminal l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        return false;
    }

    public boolean hasSpeaker() {
        Iterator<UsbDescriptor> it = getACInterfaceDescriptors((byte) 3, 1).iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbACTerminal) {
                UsbACTerminal usbACTerminal = (UsbACTerminal) next;
                if (usbACTerminal.getTerminalType() == 769 || usbACTerminal.getTerminalType() == 770 || usbACTerminal.getTerminalType() == 1026) {
                    return true;
                }
            } else {
                Log.w(TAG, "Undefined Audio Output terminal l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        return false;
    }

    public boolean hasAudioInterface() {
        return !getInterfaceDescriptorsForClass(1).isEmpty();
    }

    public boolean hasAudioTerminal(int i, int i2) {
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbACTerminal) {
                UsbACTerminal usbACTerminal = (UsbACTerminal) next;
                if (usbACTerminal.getSubclass() == 1 && usbACTerminal.getSubtype() == i && usbACTerminal.getTerminalType() == i2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAudioTerminalExcludeType(int i, int i2) {
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbACTerminal) {
                UsbACTerminal usbACTerminal = (UsbACTerminal) next;
                if (usbACTerminal.getSubclass() == 1 && usbACTerminal.getSubtype() == i && usbACTerminal.getTerminalType() != i2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAudioPlayback() {
        return hasAudioTerminalExcludeType(3, UsbTerminalTypes.TERMINAL_USB_STREAMING) && hasAudioTerminal(2, UsbTerminalTypes.TERMINAL_USB_STREAMING);
    }

    public boolean hasAudioCapture() {
        return hasAudioTerminalExcludeType(2, UsbTerminalTypes.TERMINAL_USB_STREAMING) && hasAudioTerminal(3, UsbTerminalTypes.TERMINAL_USB_STREAMING);
    }

    public boolean hasVideoCapture() {
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof UsbVCInputTerminal) {
                return true;
            }
        }
        return false;
    }

    public boolean hasVideoPlayback() {
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (it.hasNext()) {
            if (it.next() instanceof UsbVCOutputTerminal) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHIDInterface() {
        return !getInterfaceDescriptorsForClass(3).isEmpty();
    }

    public boolean hasStorageInterface() {
        return !getInterfaceDescriptorsForClass(8).isEmpty();
    }

    public boolean hasMIDIInterface() {
        Iterator<UsbDescriptor> it = getInterfaceDescriptorsForClass(1).iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbInterfaceDescriptor) {
                if (((UsbInterfaceDescriptor) next).getUsbSubclass() == 3) {
                    return true;
                }
            } else {
                Log.w(TAG, "Undefined Audio Class Interface l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        return false;
    }

    public boolean containsUniversalMidiDeviceEndpoint() {
        return doesInterfaceContainEndpoint(findUniversalMidiInterfaceDescriptors());
    }

    public boolean containsLegacyMidiDeviceEndpoint() {
        return doesInterfaceContainEndpoint(findLegacyMidiInterfaceDescriptors());
    }

    public boolean doesInterfaceContainEndpoint(ArrayList<UsbInterfaceDescriptor> arrayList) {
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            UsbInterfaceDescriptor usbInterfaceDescriptor = arrayList.get(i3);
            for (int i4 = 0; i4 < usbInterfaceDescriptor.getNumEndpoints(); i4++) {
                if (usbInterfaceDescriptor.getEndpointDescriptor(i4).getDirection() == 0) {
                    i++;
                } else {
                    i2++;
                }
            }
        }
        return i > 0 || i2 > 0;
    }

    public ArrayList<UsbInterfaceDescriptor> findUniversalMidiInterfaceDescriptors() {
        return findMidiInterfaceDescriptors(512);
    }

    public ArrayList<UsbInterfaceDescriptor> findLegacyMidiInterfaceDescriptors() {
        return findMidiInterfaceDescriptors(256);
    }

    private ArrayList<UsbInterfaceDescriptor> findMidiInterfaceDescriptors(int i) {
        UsbDescriptor midiHeaderInterfaceDescriptor;
        ArrayList<UsbDescriptor> interfaceDescriptorsForClass = getInterfaceDescriptorsForClass(1);
        ArrayList<UsbInterfaceDescriptor> arrayList = new ArrayList<>();
        Iterator<UsbDescriptor> it = interfaceDescriptorsForClass.iterator();
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbInterfaceDescriptor) {
                UsbInterfaceDescriptor usbInterfaceDescriptor = (UsbInterfaceDescriptor) next;
                if (usbInterfaceDescriptor.getUsbSubclass() == 3 && (midiHeaderInterfaceDescriptor = usbInterfaceDescriptor.getMidiHeaderInterfaceDescriptor()) != null && (midiHeaderInterfaceDescriptor instanceof UsbMSMidiHeader) && ((UsbMSMidiHeader) midiHeaderInterfaceDescriptor).getMidiStreamingClass() == i) {
                    arrayList.add(usbInterfaceDescriptor);
                }
            } else {
                Log.w(TAG, "Undefined Audio Class Interface l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        return arrayList;
    }

    public int calculateMidiInterfaceDescriptorsCount() {
        UsbDescriptor midiHeaderInterfaceDescriptor;
        Iterator<UsbDescriptor> it = getInterfaceDescriptorsForClass(1).iterator();
        int i = 0;
        while (it.hasNext()) {
            UsbDescriptor next = it.next();
            if (next instanceof UsbInterfaceDescriptor) {
                UsbInterfaceDescriptor usbInterfaceDescriptor = (UsbInterfaceDescriptor) next;
                if (usbInterfaceDescriptor.getUsbSubclass() == 3 && (midiHeaderInterfaceDescriptor = usbInterfaceDescriptor.getMidiHeaderInterfaceDescriptor()) != null && (midiHeaderInterfaceDescriptor instanceof UsbMSMidiHeader)) {
                    i++;
                }
            } else {
                Log.w(TAG, "Undefined Audio Class Interface l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        return i;
    }

    private int calculateNumLegacyMidiPorts(boolean z) {
        UsbConfigDescriptor usbConfigDescriptor;
        UsbDescriptor classSpecificEndpointDescriptor;
        UsbDescriptor midiHeaderInterfaceDescriptor;
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        while (true) {
            if (!it.hasNext()) {
                usbConfigDescriptor = null;
                break;
            }
            UsbDescriptor next = it.next();
            if (next.getType() == 2) {
                if (next instanceof UsbConfigDescriptor) {
                    usbConfigDescriptor = (UsbConfigDescriptor) next;
                    break;
                }
                Log.w(TAG, "Unrecognized Config l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
            }
        }
        if (usbConfigDescriptor == null) {
            Log.w(TAG, "Config not found");
            return 0;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<UsbInterfaceDescriptor> it2 = usbConfigDescriptor.getInterfaceDescriptors().iterator();
        while (it2.hasNext()) {
            UsbInterfaceDescriptor next2 = it2.next();
            if (next2.getUsbClass() == 1 && next2.getUsbSubclass() == 3 && (midiHeaderInterfaceDescriptor = next2.getMidiHeaderInterfaceDescriptor()) != null && (midiHeaderInterfaceDescriptor instanceof UsbMSMidiHeader) && ((UsbMSMidiHeader) midiHeaderInterfaceDescriptor).getMidiStreamingClass() == 256) {
                arrayList.add(next2);
            }
        }
        Iterator it3 = arrayList.iterator();
        int i = 0;
        while (it3.hasNext()) {
            UsbInterfaceDescriptor usbInterfaceDescriptor = (UsbInterfaceDescriptor) it3.next();
            for (int i2 = 0; i2 < usbInterfaceDescriptor.getNumEndpoints(); i2++) {
                UsbEndpointDescriptor endpointDescriptor = usbInterfaceDescriptor.getEndpointDescriptor(i2);
                if ((endpointDescriptor.getDirection() == 0) == z && (classSpecificEndpointDescriptor = endpointDescriptor.getClassSpecificEndpointDescriptor()) != null && (classSpecificEndpointDescriptor instanceof UsbACMidi10Endpoint)) {
                    i += ((UsbACMidi10Endpoint) classSpecificEndpointDescriptor).getNumJacks();
                }
            }
        }
        return i;
    }

    public int calculateNumLegacyMidiInputs() {
        return calculateNumLegacyMidiPorts(false);
    }

    public int calculateNumLegacyMidiOutputs() {
        return calculateNumLegacyMidiPorts(true);
    }

    public float getInputHeadsetProbability() {
        float f = 0.0f;
        if (hasMIDIInterface()) {
            return 0.0f;
        }
        boolean hasMic = hasMic();
        boolean hasSpeaker = hasSpeaker();
        if (hasMic && hasSpeaker) {
            f = 0.75f;
        }
        return (hasMic && hasHIDInterface()) ? f + 0.25f : f;
    }

    public boolean isInputHeadset() {
        return getInputHeadsetProbability() >= 0.75f;
    }

    private int getMaximumChannelCount() {
        Iterator<UsbDescriptor> it = this.mDescriptors.iterator();
        int i = 0;
        while (it.hasNext()) {
            Reporting reporting = (UsbDescriptor) it.next();
            if (reporting instanceof UsbAudioChannelCluster) {
                i = Math.max(i, (int) ((UsbAudioChannelCluster) reporting).getChannelCount());
            }
        }
        return i;
    }

    public float getOutputHeadsetLikelihood() {
        boolean z;
        float f = 0.0f;
        if (hasMIDIInterface()) {
            return 0.0f;
        }
        Iterator<UsbDescriptor> it = getACInterfaceDescriptors((byte) 3, 1).iterator();
        boolean z2 = false;
        boolean z3 = false;
        loop0: while (true) {
            z = z3;
            while (it.hasNext()) {
                UsbDescriptor next = it.next();
                if (next instanceof UsbACTerminal) {
                    UsbACTerminal usbACTerminal = (UsbACTerminal) next;
                    if (usbACTerminal.getTerminalType() == 769) {
                        if (usbACTerminal.getAssocTerminal() != 0) {
                            break;
                        }
                        z3 = true;
                    } else if (usbACTerminal.getTerminalType() == 770 || usbACTerminal.getTerminalType() == 1026) {
                        z2 = true;
                    }
                } else {
                    Log.w(TAG, "Undefined Audio Output terminal l: " + next.getLength() + " t:0x" + Integer.toHexString(next.getType()));
                }
            }
            z3 = true;
        }
        if (z2) {
            f = 0.75f;
        } else if (z3) {
            f = z ? 0.75f : 0.5f;
            if (getMaximumChannelCount() > 2) {
                f -= 0.25f;
            }
        }
        return ((z2 || z3) && hasHIDInterface()) ? f + 0.25f : f;
    }

    public boolean isOutputHeadset() {
        return getOutputHeadsetLikelihood() >= 0.75f;
    }

    public boolean isDock() {
        if (!hasMIDIInterface() && !hasHIDInterface()) {
            ArrayList<UsbDescriptor> aCInterfaceDescriptors = getACInterfaceDescriptors((byte) 3, 1);
            if (aCInterfaceDescriptors.size() != 1) {
                return false;
            }
            if (aCInterfaceDescriptors.get(0) instanceof UsbACTerminal) {
                if (((UsbACTerminal) aCInterfaceDescriptors.get(0)).getTerminalType() == 1538) {
                    return true;
                }
            } else {
                Log.w(TAG, "Undefined Audio Output terminal l: " + aCInterfaceDescriptors.get(0).getLength() + " t:0x" + Integer.toHexString(aCInterfaceDescriptors.get(0).getType()));
            }
        }
        return false;
    }
}
