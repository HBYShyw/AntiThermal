package com.android.server.hdmi;

import android.util.SparseArray;
import com.android.server.usb.descriptors.UsbDescriptor;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class HdmiCecMessageValidator {
    static final int ADDR_ALL = 65535;
    static final int ADDR_AUDIO_SYSTEM = 32;
    static final int ADDR_BACKUP_1 = 4096;
    static final int ADDR_BACKUP_2 = 8192;
    static final int ADDR_BROADCAST = 32768;
    static final int ADDR_DIRECT = 32767;
    static final int ADDR_NOT_UNREGISTERED = 32767;
    static final int ADDR_PLAYBACK_1 = 16;
    static final int ADDR_PLAYBACK_2 = 256;
    static final int ADDR_PLAYBACK_3 = 2048;
    static final int ADDR_RECORDER_1 = 2;
    static final int ADDR_RECORDER_2 = 4;
    static final int ADDR_RECORDER_3 = 512;
    static final int ADDR_SPECIFIC_USE = 16384;
    static final int ADDR_TUNER_1 = 8;
    static final int ADDR_TUNER_2 = 64;
    static final int ADDR_TUNER_3 = 128;
    static final int ADDR_TUNER_4 = 1024;
    static final int ADDR_TV = 1;
    static final int ADDR_UNREGISTERED = 32768;
    static final int ERROR_DESTINATION = 2;
    static final int ERROR_PARAMETER = 3;
    static final int ERROR_PARAMETER_LONG = 5;
    static final int ERROR_PARAMETER_SHORT = 4;
    static final int ERROR_SOURCE = 1;
    static final int OK = 0;
    private static final String TAG = "HdmiCecMessageValidator";
    private static final SparseArray<ValidationInfo> sValidationInfo = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ParameterValidator {
        int isValid(byte[] bArr);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface ValidationResult {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidAnalogueFrequency(int i) {
        int i2 = i & 65535;
        return (i2 == 0 || i2 == 65535) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDisplayControl(int i) {
        int i2 = i & 255;
        return i2 == 0 || i2 == 64 || i2 == 128 || i2 == 192;
    }

    static boolean isValidType(int i) {
        return i >= 0 && i <= 7 && i != 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidUiBroadcastType(int i) {
        return i == 0 || i == 1 || i == 16 || i == 32 || i == 48 || i == 64 || i == 80 || i == 96 || i == 112 || i == 128 || i == 144 || i == 145 || i == 160;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWithinRange(int i, int i2, int i3) {
        int i4 = i & 255;
        return i4 >= i2 && i4 <= i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int toErrorCode(boolean z) {
        return z ? 0 : 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ValidationResult
    public static int validateAddress(int i, int i2, int i3, int i4) {
        if (((1 << i) & i3) == 0) {
            return 1;
        }
        return ((1 << i2) & i4) == 0 ? 2 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ValidationInfo {
        public final ParameterValidator parameterValidator;
        public final int validDestinations;
        public final int validSources;

        ValidationInfo(ParameterValidator parameterValidator, int i, int i2) {
            this.parameterValidator = parameterValidator;
            this.validSources = i;
            this.validDestinations = i2;
        }
    }

    private HdmiCecMessageValidator() {
    }

    static {
        PhysicalAddressValidator physicalAddressValidator = new PhysicalAddressValidator();
        addValidationInfo(130, physicalAddressValidator, 64985, 32768);
        addValidationInfo(157, physicalAddressValidator, 32767, 32767);
        addValidationInfo(132, new ReportPhysicalAddressValidator(), 65535, 32768);
        addValidationInfo(128, new RoutingChangeValidator(), 65535, 32768);
        addValidationInfo(129, physicalAddressValidator, 65535, 32768);
        addValidationInfo(134, physicalAddressValidator, 32767, 32768);
        addValidationInfo(HdmiCecKeycode.UI_BROADCAST_DIGITAL_CABLE, new SystemAudioModeRequestValidator(), 32767, 32767);
        FixedLengthValidator fixedLengthValidator = new FixedLengthValidator(0);
        addValidationInfo(255, fixedLengthValidator, 32767, 32767);
        addValidationInfo(159, fixedLengthValidator, 32767, 32767);
        addValidationInfo(HdmiCecKeycode.UI_BROADCAST_DIGITAL_COMMNICATIONS_SATELLITE_2, fixedLengthValidator, 65535, 32767);
        addValidationInfo(HdmiCecKeycode.CEC_KEYCODE_F1_BLUE, fixedLengthValidator, 32767, 32767);
        addValidationInfo(143, fixedLengthValidator, 32767, 32767);
        addValidationInfo(140, fixedLengthValidator, 65535, 32767);
        addValidationInfo(70, fixedLengthValidator, 32767, 32767);
        addValidationInfo(131, fixedLengthValidator, 65535, 32767);
        addValidationInfo(125, fixedLengthValidator, 32767, 32767);
        addValidationInfo(4, fixedLengthValidator, 32767, 32767);
        addValidationInfo(192, fixedLengthValidator, 32767, 32767);
        addValidationInfo(11, fixedLengthValidator, 32767, 32767);
        addValidationInfo(15, fixedLengthValidator, 32767, 32767);
        addValidationInfo(HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS, fixedLengthValidator, 32767, 32767);
        addValidationInfo(HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_NEUTRAL, fixedLengthValidator, 32767, 32767);
        addValidationInfo(HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_MINUS, fixedLengthValidator, 32767, 32767);
        addValidationInfo(196, fixedLengthValidator, 32767, 32767);
        addValidationInfo(133, fixedLengthValidator, 65535, 32768);
        addValidationInfo(54, fixedLengthValidator, 65535, 65535);
        addValidationInfo(197, fixedLengthValidator, 32767, 32767);
        addValidationInfo(13, fixedLengthValidator, 32767, 32767);
        addValidationInfo(6, fixedLengthValidator, 32767, 32767);
        addValidationInfo(5, fixedLengthValidator, 32767, 32767);
        addValidationInfo(69, fixedLengthValidator, 32767, 32767);
        addValidationInfo(139, fixedLengthValidator, 32767, 65535);
        addValidationInfo(9, new VariableLengthValidator(1, 8), 32767, 32767);
        addValidationInfo(10, new RecordStatusInfoValidator(), 32767, 32767);
        addValidationInfo(51, new AnalogueTimerValidator(), 32767, 32767);
        addValidationInfo(153, new DigitalTimerValidator(), 32767, 32767);
        addValidationInfo(161, new ExternalTimerValidator(), 32767, 32767);
        addValidationInfo(52, new AnalogueTimerValidator(), 32767, 32767);
        addValidationInfo(151, new DigitalTimerValidator(), 32767, 32767);
        addValidationInfo(162, new ExternalTimerValidator(), 32767, 32767);
        addValidationInfo(HdmiCecKeycode.CEC_KEYCODE_TUNE_FUNCTION, new AsciiValidator(1, 14), 32767, 32767);
        addValidationInfo(67, new TimerClearedStatusValidator(), 32767, 32767);
        addValidationInfo(53, new TimerStatusValidator(), 32767, 32767);
        FixedLengthValidator fixedLengthValidator2 = new FixedLengthValidator(1);
        addValidationInfo(158, fixedLengthValidator2, 32767, 32767);
        addValidationInfo(50, new AsciiValidator(3), 32767, 32768);
        MinimumOneByteRangeValidator minimumOneByteRangeValidator = new MinimumOneByteRangeValidator(1, 3);
        addValidationInfo(66, new MinimumOneByteRangeValidator(1, 4), 32767, 32767);
        addValidationInfo(27, new MinimumOneByteRangeValidator(17, 31), 32767, 32767);
        addValidationInfo(26, minimumOneByteRangeValidator, 32767, 32767);
        addValidationInfo(65, new PlayModeValidator(), 32767, 32767);
        addValidationInfo(8, minimumOneByteRangeValidator, 32767, 32767);
        addValidationInfo(146, new SelectAnalogueServiceValidator(), 32767, 32767);
        addValidationInfo(147, new SelectDigitalServiceValidator(), 32767, 32767);
        addValidationInfo(7, new TunerDeviceStatusValidator(), 32767, 32767);
        VariableLengthValidator variableLengthValidator = new VariableLengthValidator(0, 14);
        addValidationInfo(135, new FixedLengthValidator(3), 32767, 32768);
        addValidationInfo(137, new VariableLengthValidator(1, 14), 65535, 32767);
        addValidationInfo(160, new VariableLengthValidator(4, 14), 65535, 65535);
        addValidationInfo(138, variableLengthValidator, 65535, 65535);
        addValidationInfo(100, new OsdStringValidator(), 32767, 32767);
        addValidationInfo(71, new AsciiValidator(1, 14), 32767, 32767);
        addValidationInfo(141, new MinimumOneByteRangeValidator(0, 2), 32767, 32767);
        addValidationInfo(142, new MinimumOneByteRangeValidator(0, 1), 32767, 32767);
        addValidationInfo(68, new UserControlPressedValidator(), 32767, 32767);
        addValidationInfo(144, new MinimumOneByteRangeValidator(0, 3), 32767, 65535);
        addValidationInfo(0, new FixedLengthValidator(2), 32767, 32767);
        addValidationInfo(122, fixedLengthValidator2, 32767, 32767);
        addValidationInfo(163, new FixedLengthValidator(3), 32767, 32767);
        addValidationInfo(164, fixedLengthValidator2, 32767, 32767);
        addValidationInfo(HdmiCecKeycode.CEC_KEYCODE_F2_RED, new MinimumOneByteRangeValidator(0, 1), 32767, 65535);
        addValidationInfo(126, new SingleByteRangeValidator(0, 1), 32767, 32767);
        addValidationInfo(154, new MinimumOneByteRangeValidator(0, 6), 32767, 32767);
        addValidationInfo(165, fixedLengthValidator, 65535, 32767);
        addValidationInfo(167, physicalAddressValidator, 32767, 32768);
        addValidationInfo(168, new VariableLengthValidator(4, 14), 32767, 32768);
        addValidationInfo(248, variableLengthValidator, 65535, 32768);
    }

    private static void addValidationInfo(int i, ParameterValidator parameterValidator, int i2, int i3) {
        sValidationInfo.append(i, new ValidationInfo(parameterValidator, i2, i3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ValidationResult
    public static int validate(int i, int i2, int i3, byte[] bArr) {
        ValidationInfo validationInfo = sValidationInfo.get(i3);
        if (validationInfo == null) {
            HdmiLogger.warning("No validation information for the opcode: " + i3, new Object[0]);
            return 0;
        }
        int validateAddress = validateAddress(i, i2, validationInfo.validSources, validationInfo.validDestinations);
        if (validateAddress != 0) {
            return validateAddress;
        }
        int isValid = validationInfo.parameterValidator.isValid(bArr);
        if (isValid != 0) {
            return isValid;
        }
        return 0;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class FixedLengthValidator implements ParameterValidator {
        private final int mLength;

        public FixedLengthValidator(int i) {
            this.mLength = i;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            return bArr.length < this.mLength ? 4 : 0;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class VariableLengthValidator implements ParameterValidator {
        private final int mMaxLength;
        private final int mMinLength;

        public VariableLengthValidator(int i, int i2) {
            this.mMinLength = i;
            this.mMaxLength = i2;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            return bArr.length < this.mMinLength ? 4 : 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidPhysicalAddress(byte[] bArr, int i) {
        int twoBytesToInt = HdmiUtils.twoBytesToInt(bArr, i);
        while (twoBytesToInt != 0) {
            int i2 = 61440 & twoBytesToInt;
            twoBytesToInt = (twoBytesToInt << 4) & 65535;
            if (i2 == 0 && twoBytesToInt != 0) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidAsciiString(byte[] bArr, int i, int i2) {
        while (i < bArr.length && i < i2) {
            if (!isWithinRange(bArr[i], 32, 126)) {
                return false;
            }
            i++;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDayOfMonth(int i) {
        return isWithinRange(i, 1, 31);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidMonthOfYear(int i) {
        return isWithinRange(i, 1, 12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidHour(int i) {
        return isWithinRange(i, 0, 23);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidMinute(int i) {
        return isWithinRange(i, 0, 59);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDurationHours(int i) {
        return isWithinRange(i, 0, 99);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidRecordingSequence(int i) {
        int i2 = i & 255;
        return (i2 & 128) == 0 && Integer.bitCount(i2) <= 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidAnalogueBroadcastType(int i) {
        return isWithinRange(i, 0, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidBroadcastSystem(int i) {
        return isWithinRange(i, 0, 31);
    }

    private static boolean isAribDbs(int i) {
        return i == 0 || isWithinRange(i, 8, 10);
    }

    private static boolean isAtscDbs(int i) {
        return i == 1 || isWithinRange(i, 16, 18);
    }

    private static boolean isDvbDbs(int i) {
        return i == 2 || isWithinRange(i, 24, 27);
    }

    private static boolean isValidDigitalBroadcastSystem(int i) {
        return isAribDbs(i) || isAtscDbs(i) || isDvbDbs(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidChannelIdentifier(byte[] bArr, int i) {
        int i2 = bArr[i] & 252;
        return i2 == 4 ? bArr.length - i >= 3 : i2 == 8 && bArr.length - i >= 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDigitalServiceIdentification(byte[] bArr, int i) {
        byte b = bArr[i];
        int i2 = b & 128;
        int i3 = b & Byte.MAX_VALUE;
        int i4 = i + 1;
        if (i2 == 0) {
            return isAribDbs(i3) ? bArr.length - i4 >= 6 : isAtscDbs(i3) ? bArr.length - i4 >= 4 : isDvbDbs(i3) && bArr.length - i4 >= 6;
        }
        if (i2 == 128 && isValidDigitalBroadcastSystem(i3)) {
            return isValidChannelIdentifier(bArr, i4);
        }
        return false;
    }

    private static boolean isValidExternalPlug(int i) {
        return isWithinRange(i, 1, 255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidExternalSource(byte[] bArr, int i) {
        byte b = bArr[i];
        int i2 = i + 1;
        if (b == 4) {
            return isValidExternalPlug(bArr[i2]);
        }
        if (b != 5 || bArr.length - i2 < 2) {
            return false;
        }
        return isValidPhysicalAddress(bArr, i2);
    }

    private static boolean isValidProgrammedInfo(int i) {
        return isWithinRange(i, 0, 11);
    }

    private static boolean isValidNotProgrammedErrorInfo(int i) {
        return isWithinRange(i, 0, 14);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidTimerStatusData(byte[] bArr, int i) {
        boolean z;
        byte b = bArr[i];
        if ((b & UsbDescriptor.DESCRIPTORTYPE_CAPABILITY) == 16) {
            int i2 = b & UsbDescriptor.DESCRIPTORTYPE_BOS;
            if (isValidProgrammedInfo(i2)) {
                if (i2 != 9 && i2 != 11) {
                    return true;
                }
                z = true;
            }
            z = false;
        } else {
            int i3 = b & UsbDescriptor.DESCRIPTORTYPE_BOS;
            if (isValidNotProgrammedErrorInfo(i3)) {
                if (i3 != 14) {
                    return true;
                }
                z = true;
            }
            z = false;
        }
        int i4 = i + 1;
        if (!z || bArr.length - i4 < 2) {
            return false;
        }
        return isValidDurationHours(bArr[i4]) && isValidMinute(bArr[i4 + 1]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidPlayMode(int i) {
        return isWithinRange(i, 5, 7) || isWithinRange(i, 9, 11) || isWithinRange(i, 21, 23) || isWithinRange(i, 25, 27) || isWithinRange(i, 36, 37) || i == 32;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidUiSoundPresenationControl(int i) {
        int i2 = i & 255;
        return i2 == 32 || i2 == 48 || i2 == 128 || i2 == 144 || i2 == 160 || isWithinRange(i2, HdmiCecKeycode.UI_SOUND_PRESENTATION_BASS_STEP_PLUS, HdmiCecKeycode.UI_SOUND_PRESENTATION_BASS_STEP_MINUS) || isWithinRange(i2, HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS, HdmiCecKeycode.UI_SOUND_PRESENTATION_TREBLE_STEP_MINUS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidTunerDeviceInfo(byte[] bArr) {
        int i = bArr[0] & Byte.MAX_VALUE;
        if (i == 0) {
            if (bArr.length >= 5) {
                return isValidDigitalServiceIdentification(bArr, 1);
            }
            return false;
        }
        if (i == 1) {
            return true;
        }
        return i == 2 && bArr.length >= 5 && isValidAnalogueBroadcastType(bArr[1]) && isValidAnalogueFrequency(HdmiUtils.twoBytesToInt(bArr, 2)) && isValidBroadcastSystem(bArr[4]);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class PhysicalAddressValidator implements ParameterValidator {
        private PhysicalAddressValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 2) {
                return 4;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidPhysicalAddress(bArr, 0));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class SystemAudioModeRequestValidator extends PhysicalAddressValidator {
        private SystemAudioModeRequestValidator() {
            super();
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.PhysicalAddressValidator, com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length == 0) {
                return 0;
            }
            return super.isValid(bArr);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class ReportPhysicalAddressValidator implements ParameterValidator {
        private ReportPhysicalAddressValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 3) {
                return 4;
            }
            boolean z = false;
            if (HdmiCecMessageValidator.isValidPhysicalAddress(bArr, 0) && HdmiCecMessageValidator.isValidType(bArr[2])) {
                z = true;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class RoutingChangeValidator implements ParameterValidator {
        private RoutingChangeValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 4) {
                return 4;
            }
            boolean z = false;
            if (HdmiCecMessageValidator.isValidPhysicalAddress(bArr, 0) && HdmiCecMessageValidator.isValidPhysicalAddress(bArr, 2)) {
                z = true;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class RecordStatusInfoValidator implements ParameterValidator {
        private RecordStatusInfoValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            boolean z = true;
            if (bArr.length < 1) {
                return 4;
            }
            if (!HdmiCecMessageValidator.isWithinRange(bArr[0], 1, 7) && !HdmiCecMessageValidator.isWithinRange(bArr[0], 9, 14) && !HdmiCecMessageValidator.isWithinRange(bArr[0], 16, 23) && !HdmiCecMessageValidator.isWithinRange(bArr[0], 26, 27) && bArr[0] != 31) {
                z = false;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class AsciiValidator implements ParameterValidator {
        private final int mMaxLength;
        private final int mMinLength;

        AsciiValidator(int i) {
            this.mMinLength = i;
            this.mMaxLength = i;
        }

        AsciiValidator(int i, int i2) {
            this.mMinLength = i;
            this.mMaxLength = i2;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < this.mMinLength) {
                return 4;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidAsciiString(bArr, 0, this.mMaxLength));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class OsdStringValidator implements ParameterValidator {
        private OsdStringValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 2) {
                return 4;
            }
            boolean z = false;
            if (HdmiCecMessageValidator.isValidDisplayControl(bArr[0]) && HdmiCecMessageValidator.isValidAsciiString(bArr, 1, 14)) {
                z = true;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class MinimumOneByteRangeValidator implements ParameterValidator {
        private final int mMaxValue;
        private final int mMinValue;

        MinimumOneByteRangeValidator(int i, int i2) {
            this.mMinValue = i;
            this.mMaxValue = i2;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 1) {
                return 4;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isWithinRange(bArr[0], this.mMinValue, this.mMaxValue));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class SingleByteRangeValidator implements ParameterValidator {
        private final int mMaxValue;
        private final int mMinValue;

        SingleByteRangeValidator(int i, int i2) {
            this.mMinValue = i;
            this.mMaxValue = i2;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 1) {
                return 4;
            }
            if (bArr.length > 1) {
                return 5;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isWithinRange(bArr[0], this.mMinValue, this.mMaxValue));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class AnalogueTimerValidator implements ParameterValidator {
        private AnalogueTimerValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 11) {
                return 4;
            }
            boolean z = false;
            if (HdmiCecMessageValidator.isValidDayOfMonth(bArr[0]) && HdmiCecMessageValidator.isValidMonthOfYear(bArr[1]) && HdmiCecMessageValidator.isValidHour(bArr[2]) && HdmiCecMessageValidator.isValidMinute(bArr[3]) && HdmiCecMessageValidator.isValidDurationHours(bArr[4]) && HdmiCecMessageValidator.isValidMinute(bArr[5]) && HdmiCecMessageValidator.isValidRecordingSequence(bArr[6]) && HdmiCecMessageValidator.isValidAnalogueBroadcastType(bArr[7]) && HdmiCecMessageValidator.isValidAnalogueFrequency(HdmiUtils.twoBytesToInt(bArr, 8)) && HdmiCecMessageValidator.isValidBroadcastSystem(bArr[10])) {
                z = true;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class DigitalTimerValidator implements ParameterValidator {
        private DigitalTimerValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 11) {
                return 4;
            }
            boolean z = false;
            if (HdmiCecMessageValidator.isValidDayOfMonth(bArr[0]) && HdmiCecMessageValidator.isValidMonthOfYear(bArr[1]) && HdmiCecMessageValidator.isValidHour(bArr[2]) && HdmiCecMessageValidator.isValidMinute(bArr[3]) && HdmiCecMessageValidator.isValidDurationHours(bArr[4]) && HdmiCecMessageValidator.isValidMinute(bArr[5]) && HdmiCecMessageValidator.isValidRecordingSequence(bArr[6]) && HdmiCecMessageValidator.isValidDigitalServiceIdentification(bArr, 7)) {
                z = true;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class ExternalTimerValidator implements ParameterValidator {
        private ExternalTimerValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 9) {
                return 4;
            }
            boolean z = false;
            if (HdmiCecMessageValidator.isValidDayOfMonth(bArr[0]) && HdmiCecMessageValidator.isValidMonthOfYear(bArr[1]) && HdmiCecMessageValidator.isValidHour(bArr[2]) && HdmiCecMessageValidator.isValidMinute(bArr[3]) && HdmiCecMessageValidator.isValidDurationHours(bArr[4]) && HdmiCecMessageValidator.isValidMinute(bArr[5]) && HdmiCecMessageValidator.isValidRecordingSequence(bArr[6]) && HdmiCecMessageValidator.isValidExternalSource(bArr, 7)) {
                z = true;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class TimerClearedStatusValidator implements ParameterValidator {
        private TimerClearedStatusValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            boolean z = true;
            if (bArr.length < 1) {
                return 4;
            }
            if (!HdmiCecMessageValidator.isWithinRange(bArr[0], 0, 2) && (bArr[0] & 255) != 128) {
                z = false;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class TimerStatusValidator implements ParameterValidator {
        private TimerStatusValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 1) {
                return 4;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidTimerStatusData(bArr, 0));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class PlayModeValidator implements ParameterValidator {
        private PlayModeValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 1) {
                return 4;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidPlayMode(bArr[0]));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class SelectAnalogueServiceValidator implements ParameterValidator {
        private SelectAnalogueServiceValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 4) {
                return 4;
            }
            boolean z = false;
            if (HdmiCecMessageValidator.isValidAnalogueBroadcastType(bArr[0]) && HdmiCecMessageValidator.isValidAnalogueFrequency(HdmiUtils.twoBytesToInt(bArr, 1)) && HdmiCecMessageValidator.isValidBroadcastSystem(bArr[3])) {
                z = true;
            }
            return HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class SelectDigitalServiceValidator implements ParameterValidator {
        private SelectDigitalServiceValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 4) {
                return 4;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidDigitalServiceIdentification(bArr, 0));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class TunerDeviceStatusValidator implements ParameterValidator {
        private TunerDeviceStatusValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 1) {
                return 4;
            }
            return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidTunerDeviceInfo(bArr));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class UserControlPressedValidator implements ParameterValidator {
        private UserControlPressedValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] bArr) {
            if (bArr.length < 1) {
                return 4;
            }
            if (bArr.length == 1) {
                return 0;
            }
            byte b = bArr[0];
            if (b == 86) {
                return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidUiBroadcastType(bArr[1]));
            }
            if (b == 87) {
                return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidUiSoundPresenationControl(bArr[1]));
            }
            if (b == 96) {
                return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidPlayMode(bArr[1]));
            }
            if (b != 103) {
                return 0;
            }
            if (bArr.length >= 4) {
                return HdmiCecMessageValidator.toErrorCode(HdmiCecMessageValidator.isValidChannelIdentifier(bArr, 1));
            }
            return 4;
        }
    }
}
