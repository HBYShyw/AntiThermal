package com.android.server.usb.descriptors.report;

import com.android.server.usb.descriptors.UsbACInterface;
import com.android.server.usb.descriptors.UsbDescriptor;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import java.util.HashMap;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class UsbStrings {
    private static final String TAG = "UsbStrings";
    private static HashMap<Byte, String> sACControlInterfaceNames;
    private static HashMap<Byte, String> sACStreamingInterfaceNames;
    private static HashMap<Integer, String> sAudioEncodingNames;
    private static HashMap<Integer, String> sAudioSubclassNames;
    private static HashMap<Integer, String> sClassNames;
    private static HashMap<Byte, String> sDescriptorNames;
    private static HashMap<Integer, String> sFormatNames;
    private static HashMap<Integer, String> sTerminalNames;

    public static String getACInterfaceSubclassName(int i) {
        return i == 1 ? "AC Control" : "AC Streaming";
    }

    static {
        allocUsbStrings();
    }

    private static void initDescriptorNames() {
        HashMap<Byte, String> hashMap = new HashMap<>();
        sDescriptorNames = hashMap;
        hashMap.put((byte) 1, "Device");
        sDescriptorNames.put((byte) 2, "Config");
        sDescriptorNames.put((byte) 3, "String");
        sDescriptorNames.put((byte) 4, "Interface");
        sDescriptorNames.put((byte) 5, "Endpoint");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_BOS), "BOS (whatever that means)");
        sDescriptorNames.put((byte) 11, "Interface Association");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_CAPABILITY), "Capability");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_HID), "HID");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_REPORT), "Report");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_PHYSICAL), "Physical");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_INTERFACE), "Class-specific Interface");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_CLASSSPECIFIC_ENDPOINT), "Class-specific Endpoint");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_HUB), "Hub");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_SUPERSPEED_HUB), "Superspeed Hub");
        sDescriptorNames.put(Byte.valueOf(UsbDescriptor.DESCRIPTORTYPE_ENDPOINT_COMPANION), "Endpoint Companion");
    }

    private static void initACControlInterfaceNames() {
        HashMap<Byte, String> hashMap = new HashMap<>();
        sACControlInterfaceNames = hashMap;
        hashMap.put((byte) 0, "Undefined");
        sACControlInterfaceNames.put((byte) 1, "Header");
        sACControlInterfaceNames.put((byte) 2, "Input Terminal");
        sACControlInterfaceNames.put((byte) 3, "Output Terminal");
        sACControlInterfaceNames.put((byte) 4, "Mixer Unit");
        sACControlInterfaceNames.put((byte) 5, "Selector Unit");
        sACControlInterfaceNames.put((byte) 6, "Feature Unit");
        sACControlInterfaceNames.put((byte) 7, "Processing Unit");
        sACControlInterfaceNames.put((byte) 8, "Extension Unit");
        sACControlInterfaceNames.put((byte) 10, "Clock Source");
        sACControlInterfaceNames.put((byte) 11, "Clock Selector");
        sACControlInterfaceNames.put((byte) 12, "Clock Multiplier");
        sACControlInterfaceNames.put(Byte.valueOf(UsbACInterface.ACI_SAMPLE_RATE_CONVERTER), "Sample Rate Converter");
    }

    private static void initACStreamingInterfaceNames() {
        HashMap<Byte, String> hashMap = new HashMap<>();
        sACStreamingInterfaceNames = hashMap;
        hashMap.put((byte) 0, "Undefined");
        sACStreamingInterfaceNames.put((byte) 1, "General");
        sACStreamingInterfaceNames.put((byte) 2, "Format Type");
        sACStreamingInterfaceNames.put((byte) 3, "Format Specific");
    }

    private static void initClassNames() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        sClassNames = hashMap;
        hashMap.put(0, "Device");
        sClassNames.put(1, "Audio");
        sClassNames.put(2, "Communications");
        sClassNames.put(3, "HID");
        sClassNames.put(5, "Physical");
        sClassNames.put(6, "Image");
        sClassNames.put(7, "Printer");
        sClassNames.put(8, "Storage");
        sClassNames.put(9, "Hub");
        sClassNames.put(10, "CDC Control");
        sClassNames.put(11, "Smart Card");
        sClassNames.put(13, "Security");
        sClassNames.put(14, "Video");
        sClassNames.put(15, "Healthcare");
        sClassNames.put(16, "Audio/Video");
        sClassNames.put(17, "Billboard");
        sClassNames.put(18, "Type C Bridge");
        sClassNames.put(Integer.valueOf(UsbDescriptor.CLASSID_DIAGNOSTIC), "Diagnostic");
        sClassNames.put(Integer.valueOf(UsbDescriptor.CLASSID_WIRELESS), "Wireless");
        sClassNames.put(Integer.valueOf(UsbDescriptor.CLASSID_MISC), "Misc");
        sClassNames.put(Integer.valueOf(UsbDescriptor.CLASSID_APPSPECIFIC), "Application Specific");
        sClassNames.put(255, "Vendor Specific");
    }

    private static void initAudioSubclassNames() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        sAudioSubclassNames = hashMap;
        hashMap.put(0, "Undefinded");
        sAudioSubclassNames.put(1, "Audio Control");
        sAudioSubclassNames.put(2, "Audio Streaming");
        sAudioSubclassNames.put(3, "MIDI Streaming");
    }

    private static void initAudioEncodingNames() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        sAudioEncodingNames = hashMap;
        hashMap.put(0, "Format I Undefined");
        sAudioEncodingNames.put(1, "Format I PCM");
        sAudioEncodingNames.put(2, "Format I PCM8");
        sAudioEncodingNames.put(3, "Format I FLOAT");
        sAudioEncodingNames.put(4, "Format I ALAW");
        sAudioEncodingNames.put(5, "Format I MuLAW");
        sAudioEncodingNames.put(4096, "FORMAT_II Undefined");
        sAudioEncodingNames.put(Integer.valueOf(UsbACInterface.FORMAT_II_MPEG), "FORMAT_II MPEG");
        sAudioEncodingNames.put(Integer.valueOf(UsbACInterface.FORMAT_II_AC3), "FORMAT_II AC3");
        sAudioEncodingNames.put(8192, "FORMAT_III Undefined");
        sAudioEncodingNames.put(Integer.valueOf(UsbACInterface.FORMAT_III_IEC1937AC3), "FORMAT_III IEC1937 AC3");
        sAudioEncodingNames.put(Integer.valueOf(UsbACInterface.FORMAT_III_IEC1937_MPEG1_Layer1), "FORMAT_III MPEG1 Layer 1");
        sAudioEncodingNames.put(Integer.valueOf(UsbACInterface.FORMAT_III_IEC1937_MPEG1_Layer2), "FORMAT_III MPEG1 Layer 2");
        sAudioEncodingNames.put(Integer.valueOf(UsbACInterface.FORMAT_III_IEC1937_MPEG2_EXT), "FORMAT_III MPEG2 EXT");
        sAudioEncodingNames.put(Integer.valueOf(UsbACInterface.FORMAT_III_IEC1937_MPEG2_Layer1LS), "FORMAT_III MPEG2 Layer1LS");
    }

    private static void initTerminalNames() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        sTerminalNames = hashMap;
        hashMap.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_USB_STREAMING), "USB Streaming");
        sTerminalNames.put(512, "Undefined");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_IN_MIC), "Microphone");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_IN_DESKTOP_MIC), "Desktop Microphone");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_IN_PERSONAL_MIC), "Personal (headset) Microphone");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_IN_OMNI_MIC), "Omni Microphone");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_IN_MIC_ARRAY), "Microphone Array");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_IN_PROC_MIC_ARRAY), "Proecessing Microphone Array");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_UNDEFINED), "Undefined");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_SPEAKER), "Speaker");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_HEADPHONES), "Headphones");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_HEADMOUNTED), "Head Mounted Speaker");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_DESKTOPSPEAKER), "Desktop Speaker");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_ROOMSPEAKER), "Room Speaker");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_COMSPEAKER), "Communications Speaker");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_OUT_LFSPEAKER), "Low Frequency Speaker");
        sTerminalNames.put(1024, "Undefined");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_BIDIR_HANDSET), "Handset");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_BIDIR_HEADSET), "Headset");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_BIDIR_SKRPHONE), "Speaker Phone");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_BIDIR_SKRPHONE_SUPRESS), "Speaker Phone (echo supressing)");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_BIDIR_SKRPHONE_CANCEL), "Speaker Phone (echo canceling)");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_TELE_UNDEFINED), "Undefined");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_TELE_PHONELINE), "Phone Line");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_TELE_PHONE), "Telephone");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_TELE_DOWNLINEPHONE), "Down Line Phone");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_UNDEFINED), "Undefined");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_ANALOG), "Analog Connector");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_DIGITAL), "Digital Connector");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_LINE), "Line Connector");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_LEGACY), "Legacy Audio Connector");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_SPIDF), "S/PIDF Interface");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_1394DA), "1394 Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EXTERN_1394DV), "1394 Audio/Video");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_UNDEFINED), "Undefined");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_CALNOISE), "Calibration Nose");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_EQNOISE), "EQ Noise");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_CDPLAYER), "CD Player");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_DAT), "DAT");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_DCC), "DCC");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_MINIDISK), "Mini Disk");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_ANALOGTAPE), "Analog Tap");
        sTerminalNames.put(1800, "Phonograph");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_VCRAUDIO), "VCR Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_VIDDISKAUDIO), "Video Disk Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_DVDAUDIO), "DVD Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_TVAUDIO), "TV Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_SATELLITEAUDIO), "Satellite Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_CABLEAUDIO), "Cable Tuner Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_DSSAUDIO), "DSS Audio");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_RADIOTRANSMITTER), "Radio Transmitter");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_MULTITRACK), "Multitrack Recorder");
        sTerminalNames.put(Integer.valueOf(UsbTerminalTypes.TERMINAL_EMBED_SYNTHESIZER), "Synthesizer");
    }

    public static String getTerminalName(int i) {
        String str = sTerminalNames.get(Integer.valueOf(i));
        if (str != null) {
            return str;
        }
        return "Unknown Terminal Type 0x" + Integer.toHexString(i);
    }

    private static void initFormatNames() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        sFormatNames = hashMap;
        hashMap.put(1, "FORMAT_TYPE_I");
        sFormatNames.put(2, "FORMAT_TYPE_II");
        sFormatNames.put(3, "FORMAT_TYPE_III");
        sFormatNames.put(4, "FORMAT_TYPE_IV");
        sFormatNames.put(-127, "EXT_FORMAT_TYPE_I");
        sFormatNames.put(-126, "EXT_FORMAT_TYPE_II");
        sFormatNames.put(-125, "EXT_FORMAT_TYPE_III");
    }

    public static String getFormatName(int i) {
        String str = sFormatNames.get(Integer.valueOf(i));
        if (str != null) {
            return str;
        }
        return "Unknown Format Type 0x" + Integer.toHexString(i);
    }

    private static void allocUsbStrings() {
        initDescriptorNames();
        initACControlInterfaceNames();
        initACStreamingInterfaceNames();
        initClassNames();
        initAudioSubclassNames();
        initAudioEncodingNames();
        initTerminalNames();
        initFormatNames();
    }

    public static String getDescriptorName(byte b) {
        String str = sDescriptorNames.get(Byte.valueOf(b));
        int i = b & 255;
        if (str != null) {
            return str;
        }
        return "Unknown Descriptor [0x" + Integer.toHexString(i) + ":" + i + "]";
    }

    public static String getACControlInterfaceName(byte b) {
        String str = sACControlInterfaceNames.get(Byte.valueOf(b));
        int i = b & 255;
        if (str != null) {
            return str;
        }
        return "Unknown subtype [0x" + Integer.toHexString(i) + ":" + i + "]";
    }

    public static String getACStreamingInterfaceName(byte b) {
        String str = sACStreamingInterfaceNames.get(Byte.valueOf(b));
        int i = b & 255;
        if (str != null) {
            return str;
        }
        return "Unknown Subtype [0x" + Integer.toHexString(i) + ":" + i + "]";
    }

    public static String getClassName(int i) {
        String str = sClassNames.get(Integer.valueOf(i));
        int i2 = i & 255;
        if (str != null) {
            return str;
        }
        return "Unknown Class ID [0x" + Integer.toHexString(i2) + ":" + i2 + "]";
    }

    public static String getAudioSubclassName(int i) {
        String str = sAudioSubclassNames.get(Integer.valueOf(i));
        int i2 = i & 255;
        if (str != null) {
            return str;
        }
        return "Unknown Audio Subclass [0x" + Integer.toHexString(i2) + ":" + i2 + "]";
    }

    public static String getAudioFormatName(int i) {
        String str = sAudioEncodingNames.get(Integer.valueOf(i));
        if (str != null) {
            return str;
        }
        return "Unknown Format (encoding) ID [0x" + Integer.toHexString(i) + ":" + i + "]";
    }
}
