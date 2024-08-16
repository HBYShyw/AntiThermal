package com.android.server.hdmi;

import java.io.UnsupportedEncodingException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiCecMessageBuilder {
    private static final int OSD_NAME_MAX_LENGTH = 14;

    private static byte[] physicalAddressToParam(int i) {
        return new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    private HdmiCecMessageBuilder() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildFeatureAbortCommand(int i, int i2, int i3, int i4) {
        return HdmiCecMessage.build(i, i2, 0, new byte[]{(byte) (i3 & 255), (byte) (i4 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildGivePhysicalAddress(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 131);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildGiveOsdNameCommand(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 70);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildGiveDeviceVendorIdCommand(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 140);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSetMenuLanguageCommand(int i, String str) {
        if (str.length() != 3) {
            return null;
        }
        String lowerCase = str.toLowerCase();
        return HdmiCecMessage.build(i, 15, 50, new byte[]{(byte) (lowerCase.charAt(0) & 255), (byte) (lowerCase.charAt(1) & 255), (byte) (lowerCase.charAt(2) & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSetOsdNameCommand(int i, int i2, String str) {
        try {
            return HdmiCecMessage.build(i, i2, 71, str.substring(0, Math.min(str.length(), 14)).getBytes("US-ASCII"));
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportPhysicalAddressCommand(int i, int i2, int i3) {
        return HdmiCecMessage.build(i, 15, 132, new byte[]{(byte) ((i2 >> 8) & 255), (byte) (i2 & 255), (byte) (i3 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildDeviceVendorIdCommand(int i, int i2) {
        return HdmiCecMessage.build(i, 15, 135, new byte[]{(byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildCecVersion(int i, int i2, int i3) {
        return HdmiCecMessage.build(i, i2, 158, new byte[]{(byte) (i3 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRequestArcInitiation(int i, int i2) {
        return HdmiCecMessage.build(i, i2, HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_MINUS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildInitiateArc(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 192);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildTerminateArc(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 197);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRequestArcTermination(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 196);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportArcInitiated(int i, int i2) {
        return HdmiCecMessage.build(i, i2, HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportArcTerminated(int i, int i2) {
        return HdmiCecMessage.build(i, i2, HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_NEUTRAL);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRequestShortAudioDescriptor(int i, int i2, int[] iArr) {
        int min = Math.min(iArr.length, 4);
        byte[] bArr = new byte[min];
        for (int i3 = 0; i3 < min; i3++) {
            bArr[i3] = (byte) (iArr[i3] & 255);
        }
        return HdmiCecMessage.build(i, i2, 164, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildTextViewOn(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 13);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRequestActiveSource(int i) {
        return HdmiCecMessage.build(i, 15, 133);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildActiveSource(int i, int i2) {
        return HdmiCecMessage.build(i, 15, 130, physicalAddressToParam(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildInactiveSource(int i, int i2) {
        return HdmiCecMessage.build(i, 0, 157, physicalAddressToParam(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSetStreamPath(int i, int i2) {
        return HdmiCecMessage.build(i, 15, 134, physicalAddressToParam(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRoutingChange(int i, int i2, int i3) {
        return HdmiCecMessage.build(i, 15, 128, new byte[]{(byte) ((i2 >> 8) & 255), (byte) (i2 & 255), (byte) ((i3 >> 8) & 255), (byte) (i3 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRoutingInformation(int i, int i2) {
        return HdmiCecMessage.build(i, 15, 129, physicalAddressToParam(i2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildGiveDevicePowerStatus(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 143);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportPowerStatus(int i, int i2, int i3) {
        return HdmiCecMessage.build(i, i2, 144, new byte[]{(byte) (i3 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportMenuStatus(int i, int i2, int i3) {
        return HdmiCecMessage.build(i, i2, 142, new byte[]{(byte) (i3 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSystemAudioModeRequest(int i, int i2, int i3, boolean z) {
        if (z) {
            return HdmiCecMessage.build(i, i2, HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE, physicalAddressToParam(i3));
        }
        return HdmiCecMessage.build(i, i2, HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSetSystemAudioMode(int i, int i2, boolean z) {
        return buildCommandWithBooleanParam(i, i2, HdmiCecKeycode.CEC_KEYCODE_F2_RED, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportSystemAudioMode(int i, int i2, boolean z) {
        return buildCommandWithBooleanParam(i, i2, 126, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportShortAudioDescriptor(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 163, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildGiveAudioStatus(int i, int i2) {
        return HdmiCecMessage.build(i, i2, HdmiCecKeycode.CEC_KEYCODE_F1_BLUE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildReportAudioStatus(int i, int i2, int i3, boolean z) {
        return HdmiCecMessage.build(i, i2, 122, new byte[]{(byte) ((((byte) i3) & Byte.MAX_VALUE) | ((byte) (z ? 128 : 0)))});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildUserControlPressed(int i, int i2, int i3) {
        return buildUserControlPressed(i, i2, new byte[]{(byte) (i3 & 255)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildUserControlPressed(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 68, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildUserControlReleased(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 69);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildGiveSystemAudioModeStatus(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 125);
    }

    public static HdmiCecMessage buildStandby(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 54);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildVendorCommand(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 137, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildVendorCommandWithId(int i, int i2, int i3, byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length + 3];
        bArr2[0] = (byte) ((i3 >> 16) & 255);
        bArr2[1] = (byte) ((i3 >> 8) & 255);
        bArr2[2] = (byte) (i3 & 255);
        System.arraycopy(bArr, 0, bArr2, 3, bArr.length);
        return HdmiCecMessage.build(i, i2, 160, bArr2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRecordOn(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 9, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildRecordOff(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSetDigitalTimer(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 151, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSetAnalogueTimer(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 52, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildSetExternalTimer(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 162, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildClearDigitalTimer(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 153, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildClearAnalogueTimer(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 51, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildClearExternalTimer(int i, int i2, byte[] bArr) {
        return HdmiCecMessage.build(i, i2, 161, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage buildGiveFeatures(int i, int i2) {
        return HdmiCecMessage.build(i, i2, 165);
    }

    private static HdmiCecMessage buildCommandWithBooleanParam(int i, int i2, int i3, boolean z) {
        return HdmiCecMessage.build(i, i2, i3, new byte[]{z ? (byte) 1 : (byte) 0});
    }
}
