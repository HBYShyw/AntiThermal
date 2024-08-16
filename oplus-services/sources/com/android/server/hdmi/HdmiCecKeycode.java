package com.android.server.hdmi;

import com.android.server.power.stats.BatteryStatsImpl;
import com.android.server.usb.descriptors.UsbDescriptor;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import java.util.Arrays;
import libcore.util.EmptyArray;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
final class HdmiCecKeycode {
    public static final int CEC_KEYCODE_ANGLE = 80;
    public static final int CEC_KEYCODE_BACKWARD = 76;
    public static final int CEC_KEYCODE_CHANNEL_DOWN = 49;
    public static final int CEC_KEYCODE_CHANNEL_UP = 48;
    public static final int CEC_KEYCODE_CLEAR = 44;
    public static final int CEC_KEYCODE_CONTENTS_MENU = 11;
    public static final int CEC_KEYCODE_DATA = 118;
    public static final int CEC_KEYCODE_DISPLAY_INFORMATION = 53;
    public static final int CEC_KEYCODE_DOT = 42;
    public static final int CEC_KEYCODE_DOWN = 2;
    public static final int CEC_KEYCODE_EJECT = 74;
    public static final int CEC_KEYCODE_ELECTRONIC_PROGRAM_GUIDE = 83;
    public static final int CEC_KEYCODE_ENTER = 43;
    public static final int CEC_KEYCODE_EXIT = 13;
    public static final int CEC_KEYCODE_F1_BLUE = 113;
    public static final int CEC_KEYCODE_F2_RED = 114;
    public static final int CEC_KEYCODE_F3_GREEN = 115;
    public static final int CEC_KEYCODE_F4_YELLOW = 116;
    public static final int CEC_KEYCODE_F5 = 117;
    public static final int CEC_KEYCODE_FAST_FORWARD = 73;
    public static final int CEC_KEYCODE_FAVORITE_MENU = 12;
    public static final int CEC_KEYCODE_FORWARD = 75;
    public static final int CEC_KEYCODE_HELP = 54;
    public static final int CEC_KEYCODE_INITIAL_CONFIGURATION = 85;
    public static final int CEC_KEYCODE_INPUT_SELECT = 52;
    public static final int CEC_KEYCODE_LEFT = 3;
    public static final int CEC_KEYCODE_LEFT_DOWN = 8;
    public static final int CEC_KEYCODE_LEFT_UP = 7;
    public static final int CEC_KEYCODE_MEDIA_CONTEXT_SENSITIVE_MENU = 17;
    public static final int CEC_KEYCODE_MEDIA_TOP_MENU = 16;
    public static final int CEC_KEYCODE_MUTE = 67;
    public static final int CEC_KEYCODE_MUTE_FUNCTION = 101;
    public static final int CEC_KEYCODE_NEXT_FAVORITE = 47;
    public static final int CEC_KEYCODE_NUMBERS_1 = 33;
    public static final int CEC_KEYCODE_NUMBERS_2 = 34;
    public static final int CEC_KEYCODE_NUMBERS_3 = 35;
    public static final int CEC_KEYCODE_NUMBERS_4 = 36;
    public static final int CEC_KEYCODE_NUMBERS_5 = 37;
    public static final int CEC_KEYCODE_NUMBERS_6 = 38;
    public static final int CEC_KEYCODE_NUMBERS_7 = 39;
    public static final int CEC_KEYCODE_NUMBERS_8 = 40;
    public static final int CEC_KEYCODE_NUMBERS_9 = 41;
    public static final int CEC_KEYCODE_NUMBER_0_OR_NUMBER_10 = 32;
    public static final int CEC_KEYCODE_NUMBER_11 = 30;
    public static final int CEC_KEYCODE_NUMBER_12 = 31;
    public static final int CEC_KEYCODE_NUMBER_ENTRY_MODE = 29;
    public static final int CEC_KEYCODE_PAGE_DOWN = 56;
    public static final int CEC_KEYCODE_PAGE_UP = 55;
    public static final int CEC_KEYCODE_PAUSE = 70;
    public static final int CEC_KEYCODE_PAUSE_PLAY_FUNCTION = 97;
    public static final int CEC_KEYCODE_PAUSE_RECORD = 78;
    public static final int CEC_KEYCODE_PAUSE_RECORD_FUNCTION = 99;
    public static final int CEC_KEYCODE_PLAY = 68;
    public static final int CEC_KEYCODE_PLAY_FUNCTION = 96;
    public static final int CEC_KEYCODE_POWER = 64;
    public static final int CEC_KEYCODE_POWER_OFF_FUNCTION = 108;
    public static final int CEC_KEYCODE_POWER_ON_FUNCTION = 109;
    public static final int CEC_KEYCODE_POWER_TOGGLE_FUNCTION = 107;
    public static final int CEC_KEYCODE_PREVIOUS_CHANNEL = 50;
    public static final int CEC_KEYCODE_RECORD = 71;
    public static final int CEC_KEYCODE_RECORD_FUNCTION = 98;
    public static final int CEC_KEYCODE_RESERVED = 79;
    public static final int CEC_KEYCODE_RESTORE_VOLUME_FUNCTION = 102;
    public static final int CEC_KEYCODE_REWIND = 72;
    public static final int CEC_KEYCODE_RIGHT = 4;
    public static final int CEC_KEYCODE_RIGHT_DOWN = 6;
    public static final int CEC_KEYCODE_RIGHT_UP = 5;
    public static final int CEC_KEYCODE_ROOT_MENU = 9;
    public static final int CEC_KEYCODE_SELECT = 0;
    public static final int CEC_KEYCODE_SELECT_AUDIO_INPUT_FUNCTION = 106;
    public static final int CEC_KEYCODE_SELECT_AV_INPUT_FUNCTION = 105;
    public static final int CEC_KEYCODE_SELECT_BROADCAST_TYPE = 86;
    public static final int CEC_KEYCODE_SELECT_MEDIA_FUNCTION = 104;
    public static final int CEC_KEYCODE_SELECT_SOUND_PRESENTATION = 87;
    public static final int CEC_KEYCODE_SETUP_MENU = 10;
    public static final int CEC_KEYCODE_SOUND_SELECT = 51;
    public static final int CEC_KEYCODE_STOP = 69;
    public static final int CEC_KEYCODE_STOP_FUNCTION = 100;
    public static final int CEC_KEYCODE_STOP_RECORD = 77;
    public static final int CEC_KEYCODE_SUB_PICTURE = 81;
    public static final int CEC_KEYCODE_TIMER_PROGRAMMING = 84;
    public static final int CEC_KEYCODE_TUNE_FUNCTION = 103;
    public static final int CEC_KEYCODE_UP = 1;
    public static final int CEC_KEYCODE_VIDEO_ON_DEMAND = 82;
    public static final int CEC_KEYCODE_VOLUME_DOWN = 66;
    public static final int CEC_KEYCODE_VOLUME_UP = 65;
    private static final KeycodeEntry[] KEYCODE_ENTRIES;
    public static final int NO_PARAM = -1;
    public static final int UI_BROADCAST_ANALOGUE = 16;
    public static final int UI_BROADCAST_ANALOGUE_CABLE = 48;
    public static final int UI_BROADCAST_ANALOGUE_SATELLITE = 64;
    public static final int UI_BROADCAST_ANALOGUE_TERRESTRIAL = 32;
    public static final int UI_BROADCAST_DIGITAL = 80;
    public static final int UI_BROADCAST_DIGITAL_CABLE = 112;
    public static final int UI_BROADCAST_DIGITAL_COMMNICATIONS_SATELLITE = 144;
    public static final int UI_BROADCAST_DIGITAL_COMMNICATIONS_SATELLITE_2 = 145;
    public static final int UI_BROADCAST_DIGITAL_SATELLITE = 128;
    public static final int UI_BROADCAST_DIGITAL_TERRESTRIAL = 96;
    public static final int UI_BROADCAST_IP = 160;
    public static final int UI_BROADCAST_TOGGLE_ALL = 0;
    public static final int UI_BROADCAST_TOGGLE_ANALOGUE_DIGITAL = 1;
    public static final int UI_SOUND_PRESENTATION_BASS_NEUTRAL = 178;
    public static final int UI_SOUND_PRESENTATION_BASS_STEP_MINUS = 179;
    public static final int UI_SOUND_PRESENTATION_BASS_STEP_PLUS = 177;
    public static final int UI_SOUND_PRESENTATION_SELECT_AUDIO_AUTO_EQUALIZER = 160;
    public static final int UI_SOUND_PRESENTATION_SELECT_AUDIO_AUTO_REVERBERATION = 144;
    public static final int UI_SOUND_PRESENTATION_SELECT_AUDIO_DOWN_MIX = 128;
    public static final int UI_SOUND_PRESENTATION_SOUND_MIX_DUAL_MONO = 32;
    public static final int UI_SOUND_PRESENTATION_SOUND_MIX_KARAOKE = 48;
    public static final int UI_SOUND_PRESENTATION_TREBLE_NEUTRAL = 194;
    public static final int UI_SOUND_PRESENTATION_TREBLE_STEP_MINUS = 195;
    public static final int UI_SOUND_PRESENTATION_TREBLE_STEP_PLUS = 193;
    public static final int UNSUPPORTED_KEYCODE = -1;

    public static String getKeycodeType(byte b) {
        if (b == 16 || b == 17) {
            return "Menu";
        }
        switch (b) {
            case 0:
                return "Select";
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 13:
                return "Navigation";
            case 9:
            case 10:
            case 11:
            case 12:
                return "Menu";
            default:
                switch (b) {
                    case CEC_KEYCODE_NUMBER_ENTRY_MODE /* 29 */:
                    case CEC_KEYCODE_DOT /* 42 */:
                    case CEC_KEYCODE_ENTER /* 43 */:
                    case CEC_KEYCODE_CLEAR /* 44 */:
                        return "General";
                    case CEC_KEYCODE_NUMBER_11 /* 30 */:
                    case CEC_KEYCODE_NUMBER_12 /* 31 */:
                    case 32:
                    case CEC_KEYCODE_NUMBERS_1 /* 33 */:
                    case 34:
                    case 35:
                    case 36:
                    case CEC_KEYCODE_NUMBERS_5 /* 37 */:
                    case 38:
                    case CEC_KEYCODE_NUMBERS_7 /* 39 */:
                    case CEC_KEYCODE_NUMBERS_8 /* 40 */:
                    case CEC_KEYCODE_NUMBERS_9 /* 41 */:
                        return "Number";
                    default:
                        switch (b) {
                            case CEC_KEYCODE_NEXT_FAVORITE /* 47 */:
                            case 48:
                            case CEC_KEYCODE_CHANNEL_DOWN /* 49 */:
                            case 50:
                                return "Channel";
                            case CEC_KEYCODE_SOUND_SELECT /* 51 */:
                            case CEC_KEYCODE_INPUT_SELECT /* 52 */:
                                return "Select";
                            case CEC_KEYCODE_DISPLAY_INFORMATION /* 53 */:
                            case CEC_KEYCODE_HELP /* 54 */:
                                return "General";
                            case CEC_KEYCODE_PAGE_UP /* 55 */:
                            case CEC_KEYCODE_PAGE_DOWN /* 56 */:
                                return "Navigation";
                            default:
                                switch (b) {
                                    case 64:
                                        return "Power";
                                    case CEC_KEYCODE_VOLUME_UP /* 65 */:
                                        return "Volume up";
                                    case CEC_KEYCODE_VOLUME_DOWN /* 66 */:
                                        return "Volume down";
                                    case CEC_KEYCODE_MUTE /* 67 */:
                                        return "Volume mute";
                                    case CEC_KEYCODE_PLAY /* 68 */:
                                    case CEC_KEYCODE_STOP /* 69 */:
                                    case CEC_KEYCODE_PAUSE /* 70 */:
                                    case CEC_KEYCODE_RECORD /* 71 */:
                                    case CEC_KEYCODE_REWIND /* 72 */:
                                    case CEC_KEYCODE_FAST_FORWARD /* 73 */:
                                    case CEC_KEYCODE_EJECT /* 74 */:
                                    case CEC_KEYCODE_FORWARD /* 75 */:
                                    case CEC_KEYCODE_BACKWARD /* 76 */:
                                    case CEC_KEYCODE_STOP_RECORD /* 77 */:
                                    case CEC_KEYCODE_PAUSE_RECORD /* 78 */:
                                        return "Media";
                                    default:
                                        switch (b) {
                                            case 80:
                                            case CEC_KEYCODE_SUB_PICTURE /* 81 */:
                                            case CEC_KEYCODE_VIDEO_ON_DEMAND /* 82 */:
                                                return "Media";
                                            case CEC_KEYCODE_ELECTRONIC_PROGRAM_GUIDE /* 83 */:
                                            case CEC_KEYCODE_TIMER_PROGRAMMING /* 84 */:
                                                return "Timer";
                                            case CEC_KEYCODE_INITIAL_CONFIGURATION /* 85 */:
                                                return "General";
                                            case CEC_KEYCODE_SELECT_BROADCAST_TYPE /* 86 */:
                                            case CEC_KEYCODE_SELECT_SOUND_PRESENTATION /* 87 */:
                                                return "Select";
                                            default:
                                                switch (b) {
                                                    case 96:
                                                    case CEC_KEYCODE_PAUSE_PLAY_FUNCTION /* 97 */:
                                                    case CEC_KEYCODE_RECORD_FUNCTION /* 98 */:
                                                    case CEC_KEYCODE_PAUSE_RECORD_FUNCTION /* 99 */:
                                                    case 100:
                                                    case CEC_KEYCODE_MUTE_FUNCTION /* 101 */:
                                                    case CEC_KEYCODE_RESTORE_VOLUME_FUNCTION /* 102 */:
                                                    case CEC_KEYCODE_TUNE_FUNCTION /* 103 */:
                                                    case CEC_KEYCODE_SELECT_MEDIA_FUNCTION /* 104 */:
                                                    case CEC_KEYCODE_SELECT_AV_INPUT_FUNCTION /* 105 */:
                                                    case CEC_KEYCODE_SELECT_AUDIO_INPUT_FUNCTION /* 106 */:
                                                        return "Functional";
                                                    case CEC_KEYCODE_POWER_TOGGLE_FUNCTION /* 107 */:
                                                        return "Power toggle";
                                                    case CEC_KEYCODE_POWER_OFF_FUNCTION /* 108 */:
                                                        return "Power off";
                                                    case CEC_KEYCODE_POWER_ON_FUNCTION /* 109 */:
                                                        return "Power on";
                                                    default:
                                                        switch (b) {
                                                            case CEC_KEYCODE_F1_BLUE /* 113 */:
                                                            case CEC_KEYCODE_F2_RED /* 114 */:
                                                            case CEC_KEYCODE_F3_GREEN /* 115 */:
                                                            case CEC_KEYCODE_F4_YELLOW /* 116 */:
                                                            case CEC_KEYCODE_F5 /* 117 */:
                                                                return "Function key";
                                                            case CEC_KEYCODE_DATA /* 118 */:
                                                                return "General";
                                                            default:
                                                                return "Unknown";
                                                        }
                                                }
                                        }
                                }
                        }
                }
        }
    }

    public static int getMuteKey(boolean z) {
        return 67;
    }

    private static byte[] intToSingleByteArray(int i) {
        return new byte[]{(byte) (i & 255)};
    }

    private HdmiCecKeycode() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class KeycodeEntry {
        private final int mAndroidKeycode;
        private final byte[] mCecKeycodeAndParams;
        private final boolean mIsRepeatable;

        private KeycodeEntry(int i, int i2, boolean z, byte[] bArr) {
            this.mAndroidKeycode = i;
            this.mIsRepeatable = z;
            byte[] bArr2 = new byte[bArr.length + 1];
            this.mCecKeycodeAndParams = bArr2;
            System.arraycopy(bArr, 0, bArr2, 1, bArr.length);
            bArr2[0] = (byte) (i2 & 255);
        }

        private KeycodeEntry(int i, int i2, boolean z) {
            this(i, i2, z, EmptyArray.BYTE);
        }

        private KeycodeEntry(int i, int i2, byte[] bArr) {
            this(i, i2, true, bArr);
        }

        private KeycodeEntry(int i, int i2) {
            this(i, i2, true, EmptyArray.BYTE);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public byte[] toCecKeycodeAndParamIfMatched(int i) {
            if (this.mAndroidKeycode == i) {
                return this.mCecKeycodeAndParams;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int toAndroidKeycodeIfMatched(byte[] bArr) {
            if (Arrays.equals(this.mCecKeycodeAndParams, bArr)) {
                return this.mAndroidKeycode;
            }
            return -1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Boolean isRepeatableIfMatched(int i) {
            if (this.mAndroidKeycode == i) {
                return Boolean.valueOf(this.mIsRepeatable);
            }
            return null;
        }
    }

    static {
        int i = 3;
        int i2 = -1;
        int i3 = 9;
        int i4 = 13;
        int i5 = 70;
        int i6 = -1;
        int i7 = -1;
        int i8 = 86;
        boolean z = true;
        int i9 = 86;
        boolean z2 = true;
        int i10 = -1;
        boolean z3 = false;
        KEYCODE_ENTRIES = new KeycodeEntry[]{new KeycodeEntry(23, 0), new KeycodeEntry(19, 1), new KeycodeEntry(20, 2), new KeycodeEntry(21, i), new KeycodeEntry(22, 4), new KeycodeEntry(i2, 5), new KeycodeEntry(i2, 6), new KeycodeEntry(i2, 7), new KeycodeEntry(i2, 8), new KeycodeEntry(i, i3), new KeycodeEntry(82, i3), new KeycodeEntry(176, 10), new KeycodeEntry(256, 11, false), new KeycodeEntry(-1, 12), new KeycodeEntry(4, i4), new KeycodeEntry(111, i4), new KeycodeEntry(226, 16), new KeycodeEntry(UsbTerminalTypes.TERMINAL_USB_STREAMING, 17), new KeycodeEntry(234, 29), new KeycodeEntry(227, 30), new KeycodeEntry(228, 31), new KeycodeEntry(7, 32), new KeycodeEntry(8, 33), new KeycodeEntry(9, 34), new KeycodeEntry(10, 35), new KeycodeEntry(11, 36), new KeycodeEntry(12, 37), new KeycodeEntry(13, 38), new KeycodeEntry(14, 39), new KeycodeEntry(15, 40), new KeycodeEntry(16, 41), new KeycodeEntry(56, 42), new KeycodeEntry(160, 43), new KeycodeEntry(28, 44), new KeycodeEntry(-1, 47), new KeycodeEntry(166, 48), new KeycodeEntry(167, 49), new KeycodeEntry(229, 50), new KeycodeEntry(222, 51), new KeycodeEntry(UI_SOUND_PRESENTATION_BASS_NEUTRAL, 52), new KeycodeEntry(165, 53), new KeycodeEntry(-1, 54), new KeycodeEntry(92, 55), new KeycodeEntry(93, 56), new KeycodeEntry(26, 64, false), new KeycodeEntry(24, 65), new KeycodeEntry(25, 66), new KeycodeEntry(164, 67, false), new KeycodeEntry(126, 68), new KeycodeEntry(86, 69), new KeycodeEntry(BatteryStatsImpl.ExternalStatsSync.UPDATE_ALL, i5), new KeycodeEntry(85, i5), new KeycodeEntry(130, 71), new KeycodeEntry(89, 72), new KeycodeEntry(90, 73), new KeycodeEntry(129, 74), new KeycodeEntry(87, 75), new KeycodeEntry(88, 76), new KeycodeEntry(i6, 77), new KeycodeEntry(i6, 78), new KeycodeEntry(i6, 79), new KeycodeEntry(i6, 80), new KeycodeEntry(175, 81), new KeycodeEntry(i6, 82), new KeycodeEntry(172, 83), new KeycodeEntry(258, 84), new KeycodeEntry(i7, 85), new KeycodeEntry(i7, 86), new KeycodeEntry(235, i8, z, intToSingleByteArray(16)), new KeycodeEntry(236, i9, z2, intToSingleByteArray(96)), new KeycodeEntry(238, i8, z, intToSingleByteArray(128)), new KeycodeEntry(UsbDescriptor.CLASSID_MISC, i9, z2, intToSingleByteArray(144)), new KeycodeEntry(241, i8, z, intToSingleByteArray(1)), new KeycodeEntry(i10, 87), new KeycodeEntry(i10, 96, z3), new KeycodeEntry(i10, 97, z3), new KeycodeEntry(i10, 98, z3), new KeycodeEntry(i10, 99, z3), new KeycodeEntry(i10, 100, z3), new KeycodeEntry(i10, CEC_KEYCODE_MUTE_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_RESTORE_VOLUME_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_TUNE_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_SELECT_MEDIA_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_SELECT_AV_INPUT_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_SELECT_AUDIO_INPUT_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_POWER_TOGGLE_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_POWER_OFF_FUNCTION, z3), new KeycodeEntry(i10, CEC_KEYCODE_POWER_ON_FUNCTION, z3), new KeycodeEntry(186, CEC_KEYCODE_F1_BLUE), new KeycodeEntry(183, CEC_KEYCODE_F2_RED), new KeycodeEntry(184, CEC_KEYCODE_F3_GREEN), new KeycodeEntry(185, CEC_KEYCODE_F4_YELLOW), new KeycodeEntry(135, CEC_KEYCODE_F5), new KeycodeEntry(230, CEC_KEYCODE_DATA)};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] androidKeyToCecKey(int i) {
        int i2 = 0;
        while (true) {
            KeycodeEntry[] keycodeEntryArr = KEYCODE_ENTRIES;
            if (i2 >= keycodeEntryArr.length) {
                return null;
            }
            byte[] cecKeycodeAndParamIfMatched = keycodeEntryArr[i2].toCecKeycodeAndParamIfMatched(i);
            if (cecKeycodeAndParamIfMatched != null) {
                return cecKeycodeAndParamIfMatched;
            }
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int cecKeycodeAndParamsToAndroidKey(byte[] bArr) {
        int i = 0;
        while (true) {
            KeycodeEntry[] keycodeEntryArr = KEYCODE_ENTRIES;
            if (i >= keycodeEntryArr.length) {
                return -1;
            }
            int androidKeycodeIfMatched = keycodeEntryArr[i].toAndroidKeycodeIfMatched(bArr);
            if (androidKeycodeIfMatched != -1) {
                return androidKeycodeIfMatched;
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isRepeatableKey(int i) {
        int i2 = 0;
        while (true) {
            KeycodeEntry[] keycodeEntryArr = KEYCODE_ENTRIES;
            if (i2 >= keycodeEntryArr.length) {
                return false;
            }
            Boolean isRepeatableIfMatched = keycodeEntryArr[i2].isRepeatableIfMatched(i);
            if (isRepeatableIfMatched != null) {
                return isRepeatableIfMatched.booleanValue();
            }
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isSupportedKeycode(int i) {
        return androidKeyToCecKey(i) != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isVolumeKeycode(int i) {
        byte b = androidKeyToCecKey(i)[0];
        if (isSupportedKeycode(i)) {
            return b == 65 || b == 66 || b == 67 || b == 101 || b == 102;
        }
        return false;
    }
}
