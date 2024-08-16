package com.android.server.hdmi;

import com.android.server.hdmi.HdmiCecMessageValidator;
import java.util.Arrays;
import java.util.Objects;
import libcore.util.EmptyArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiCecMessage {
    public static final byte[] EMPTY_PARAM = EmptyArray.BYTE;
    private final int mDestination;
    private final int mOpcode;
    private final byte[] mParams;
    private final int mSource;
    private final int mValidationResult;

    private static boolean filterMessageParameters(int i) {
        if (i == 69 || i == 71 || i == 100 || i == 160) {
            return true;
        }
        switch (i) {
            case 137:
            case 138:
            case 139:
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isCecTransportMessage(int i) {
        return i == 167 || i == 168 || i == 248;
    }

    private static boolean isUserControlPressedMessage(int i) {
        return 68 == i;
    }

    private static String validationResultToString(@HdmiCecMessageValidator.ValidationResult int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "unknown error" : "short parameters" : "invalid parameters" : "invalid destination" : "invalid source" : "ok";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HdmiCecMessage(int i, int i2, int i3, byte[] bArr, @HdmiCecMessageValidator.ValidationResult int i4) {
        this.mSource = i;
        this.mDestination = i2;
        this.mOpcode = i3 & 255;
        this.mParams = Arrays.copyOf(bArr, bArr.length);
        this.mValidationResult = i4;
    }

    private HdmiCecMessage(int i, int i2, int i3, byte[] bArr) {
        this(i, i2, i3, bArr, HdmiCecMessageValidator.validate(i, i2, i3 & 255, bArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage build(int i, int i2, int i3, byte[] bArr) {
        int i4 = i3 & 255;
        if (i4 == 115) {
            return SetAudioVolumeLevelMessage.build(i, i2, bArr);
        }
        if (i4 == 166) {
            return ReportFeaturesMessage.build(i, i2, bArr);
        }
        return new HdmiCecMessage(i, i2, i4, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HdmiCecMessage build(int i, int i2, int i3) {
        return new HdmiCecMessage(i, i2, i3, EMPTY_PARAM);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HdmiCecMessage)) {
            return false;
        }
        HdmiCecMessage hdmiCecMessage = (HdmiCecMessage) obj;
        return this.mSource == hdmiCecMessage.getSource() && this.mDestination == hdmiCecMessage.getDestination() && this.mOpcode == hdmiCecMessage.getOpcode() && Arrays.equals(this.mParams, hdmiCecMessage.getParams()) && this.mValidationResult == hdmiCecMessage.getValidationResult();
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mSource), Integer.valueOf(this.mDestination), Integer.valueOf(this.mOpcode), Integer.valueOf(Arrays.hashCode(this.mParams)));
    }

    public int getSource() {
        return this.mSource;
    }

    public int getDestination() {
        return this.mDestination;
    }

    public int getOpcode() {
        return this.mOpcode;
    }

    public byte[] getParams() {
        return this.mParams;
    }

    public int getValidationResult() {
        return this.mValidationResult;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<%s> %X%X:%02X", opcodeToString(this.mOpcode), Integer.valueOf(this.mSource), Integer.valueOf(this.mDestination), Integer.valueOf(this.mOpcode)));
        if (this.mParams.length > 0) {
            if (filterMessageParameters(this.mOpcode)) {
                sb.append(String.format(" <Redacted len=%d>", Integer.valueOf(this.mParams.length)));
            } else {
                if (isUserControlPressedMessage(this.mOpcode)) {
                    sb.append(String.format(" <Keycode type = %s>", HdmiCecKeycode.getKeycodeType(this.mParams[0])));
                } else {
                    for (byte b : this.mParams) {
                        sb.append(String.format(":%02X", Byte.valueOf(b)));
                    }
                }
            }
        }
        int i = this.mValidationResult;
        if (i != 0) {
            sb.append(String.format(" <Validation error: %s>", validationResultToString(i)));
        }
        return sb.toString();
    }

    private static String opcodeToString(int i) {
        if (i == 0) {
            return "Feature Abort";
        }
        if (i == 26) {
            return "Give Deck Status";
        }
        if (i == 27) {
            return "Deck Status";
        }
        if (i == 125) {
            return "Give System Audio Mode Status";
        }
        if (i == 126) {
            return "System Audio Mode Status";
        }
        if (i == 153) {
            return "Clear Digital Timer";
        }
        if (i == 154) {
            return "Set Audio Rate";
        }
        switch (i) {
            case 0:
                return "Feature Abort";
            case 13:
                return "Text View On";
            case 15:
                return "Record Tv Screen";
            case 100:
                return "Set Osd String";
            case HdmiCecKeycode.CEC_KEYCODE_TUNE_FUNCTION /* 103 */:
                return "Set Timer Program Title";
            case 122:
                return "Report Audio Status";
            case 151:
                return "Set Digital Timer";
            case 157:
                return "InActive Source";
            case 158:
                return "Cec Version";
            case 159:
                return "Get Cec Version";
            case 160:
                return "Vendor Command With Id";
            case 161:
                return "Clear External Timer";
            case 162:
                return "Set External Timer";
            case 163:
                return "Report Short Audio Descriptor";
            case 164:
                return "Request Short Audio Descriptor";
            case 165:
                return "Give Features";
            case 166:
                return "Report Features";
            case 167:
                return "Request Current Latency";
            case 168:
                return "Report Current Latency";
            case 192:
                return "Initiate ARC";
            case HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS /* 193 */:
                return "Report ARC Initiated";
            case HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_NEUTRAL /* 194 */:
                return "Report ARC Terminated";
            case HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_MINUS /* 195 */:
                return "Request ARC Initiation";
            case 196:
                return "Request ARC Termination";
            case 197:
                return "Terminate ARC";
            case 248:
                return "Cdc Message";
            case 255:
                return "Abort";
            default:
                switch (i) {
                    case 4:
                        return "Image View On";
                    case 5:
                        return "Tuner Step Increment";
                    case 6:
                        return "Tuner Step Decrement";
                    case 7:
                        return "Tuner Device Status";
                    case 8:
                        return "Give Tuner Device Status";
                    case 9:
                        return "Record On";
                    case 10:
                        return "Record Status";
                    case 11:
                        return "Record Off";
                    default:
                        switch (i) {
                            case 50:
                                return "Set Menu Language";
                            case HdmiCecKeycode.CEC_KEYCODE_SOUND_SELECT /* 51 */:
                                return "Clear Analog Timer";
                            case HdmiCecKeycode.CEC_KEYCODE_INPUT_SELECT /* 52 */:
                                return "Set Analog Timer";
                            case HdmiCecKeycode.CEC_KEYCODE_DISPLAY_INFORMATION /* 53 */:
                                return "Timer Status";
                            case HdmiCecKeycode.CEC_KEYCODE_HELP /* 54 */:
                                return "Standby";
                            default:
                                switch (i) {
                                    case HdmiCecKeycode.CEC_KEYCODE_VOLUME_UP /* 65 */:
                                        return "Play";
                                    case HdmiCecKeycode.CEC_KEYCODE_VOLUME_DOWN /* 66 */:
                                        return "Deck Control";
                                    case HdmiCecKeycode.CEC_KEYCODE_MUTE /* 67 */:
                                        return "Timer Cleared Status";
                                    case HdmiCecKeycode.CEC_KEYCODE_PLAY /* 68 */:
                                        return "User Control Pressed";
                                    case HdmiCecKeycode.CEC_KEYCODE_STOP /* 69 */:
                                        return "User Control Release";
                                    case HdmiCecKeycode.CEC_KEYCODE_PAUSE /* 70 */:
                                        return "Give Osd Name";
                                    case HdmiCecKeycode.CEC_KEYCODE_RECORD /* 71 */:
                                        return "Set Osd Name";
                                    default:
                                        switch (i) {
                                            case HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE /* 112 */:
                                                return "System Audio Mode Request";
                                            case HdmiCecKeycode.CEC_KEYCODE_F1_BLUE /* 113 */:
                                                return "Give Audio Status";
                                            case HdmiCecKeycode.CEC_KEYCODE_F2_RED /* 114 */:
                                                return "Set System Audio Mode";
                                            case HdmiCecKeycode.CEC_KEYCODE_F3_GREEN /* 115 */:
                                                return "Set Audio Volume Level";
                                            default:
                                                switch (i) {
                                                    case 128:
                                                        return "Routing Change";
                                                    case 129:
                                                        return "Routing Information";
                                                    case 130:
                                                        return "Active Source";
                                                    case 131:
                                                        return "Give Physical Address";
                                                    case 132:
                                                        return "Report Physical Address";
                                                    case 133:
                                                        return "Request Active Source";
                                                    case 134:
                                                        return "Set Stream Path";
                                                    case 135:
                                                        return "Device Vendor Id";
                                                    default:
                                                        switch (i) {
                                                            case 137:
                                                                return "Vendor Command";
                                                            case 138:
                                                                return "Vendor Remote Button Down";
                                                            case 139:
                                                                return "Vendor Remote Button Up";
                                                            case 140:
                                                                return "Give Device Vendor Id";
                                                            case 141:
                                                                return "Menu Request";
                                                            case 142:
                                                                return "Menu Status";
                                                            case 143:
                                                                return "Give Device Power Status";
                                                            case 144:
                                                                return "Report Power Status";
                                                            case HdmiCecKeycode.UI_BROADCAST_DIGITAL_COMMNICATIONS_SATELLITE_2 /* 145 */:
                                                                return "Get Menu Language";
                                                            case 146:
                                                                return "Select Analog Service";
                                                            case 147:
                                                                return "Select Digital Service";
                                                            default:
                                                                return String.format("Opcode: %02X", Integer.valueOf(i));
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }
    }
}
