package android.hardware.tv.cec.V1_0;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class CecMessageType {
    public static final int ABORT = 255;
    public static final int ACTIVE_SOURCE = 130;
    public static final int CEC_VERSION = 158;
    public static final int CLEAR_ANALOG_TIMER = 51;
    public static final int CLEAR_DIGITAL_TIMER = 153;
    public static final int CLEAR_EXTERNAL_TIMER = 161;
    public static final int DECK_CONTROL = 66;
    public static final int DECK_STATUS = 27;
    public static final int DEVICE_VENDOR_ID = 135;
    public static final int FEATURE_ABORT = 0;
    public static final int GET_CEC_VERSION = 159;
    public static final int GET_MENU_LANGUAGE = 145;
    public static final int GIVE_AUDIO_STATUS = 113;
    public static final int GIVE_DECK_STATUS = 26;
    public static final int GIVE_DEVICE_POWER_STATUS = 143;
    public static final int GIVE_DEVICE_VENDOR_ID = 140;
    public static final int GIVE_OSD_NAME = 70;
    public static final int GIVE_PHYSICAL_ADDRESS = 131;
    public static final int GIVE_SYSTEM_AUDIO_MODE_STATUS = 125;
    public static final int GIVE_TUNER_DEVICE_STATUS = 8;
    public static final int IMAGE_VIEW_ON = 4;
    public static final int INACTIVE_SOURCE = 157;
    public static final int INITIATE_ARC = 192;
    public static final int MENU_REQUEST = 141;
    public static final int MENU_STATUS = 142;
    public static final int PLAY = 65;
    public static final int RECORD_OFF = 11;
    public static final int RECORD_ON = 9;
    public static final int RECORD_STATUS = 10;
    public static final int RECORD_TV_SCREEN = 15;
    public static final int REPORT_ARC_INITIATED = 193;
    public static final int REPORT_ARC_TERMINATED = 194;
    public static final int REPORT_AUDIO_STATUS = 122;
    public static final int REPORT_PHYSICAL_ADDRESS = 132;
    public static final int REPORT_POWER_STATUS = 144;
    public static final int REPORT_SHORT_AUDIO_DESCRIPTOR = 163;
    public static final int REQUEST_ACTIVE_SOURCE = 133;
    public static final int REQUEST_ARC_INITIATION = 195;
    public static final int REQUEST_ARC_TERMINATION = 196;
    public static final int REQUEST_SHORT_AUDIO_DESCRIPTOR = 164;
    public static final int ROUTING_CHANGE = 128;
    public static final int ROUTING_INFORMATION = 129;
    public static final int SELECT_ANALOG_SERVICE = 146;
    public static final int SELECT_DIGITAL_SERVICE = 147;
    public static final int SET_ANALOG_TIMER = 52;
    public static final int SET_AUDIO_RATE = 154;
    public static final int SET_DIGITAL_TIMER = 151;
    public static final int SET_EXTERNAL_TIMER = 162;
    public static final int SET_MENU_LANGUAGE = 50;
    public static final int SET_OSD_NAME = 71;
    public static final int SET_OSD_STRING = 100;
    public static final int SET_STREAM_PATH = 134;
    public static final int SET_SYSTEM_AUDIO_MODE = 114;
    public static final int SET_TIMER_PROGRAM_TITLE = 103;
    public static final int STANDBY = 54;
    public static final int SYSTEM_AUDIO_MODE_REQUEST = 112;
    public static final int SYSTEM_AUDIO_MODE_STATUS = 126;
    public static final int TERMINATE_ARC = 197;
    public static final int TEXT_VIEW_ON = 13;
    public static final int TIMER_CLEARED_STATUS = 67;
    public static final int TIMER_STATUS = 53;
    public static final int TUNER_DEVICE_STATUS = 7;
    public static final int TUNER_STEP_DECREMENT = 6;
    public static final int TUNER_STEP_INCREMENT = 5;
    public static final int USER_CONTROL_PRESSED = 68;
    public static final int USER_CONTROL_RELEASED = 69;
    public static final int VENDOR_COMMAND = 137;
    public static final int VENDOR_COMMAND_WITH_ID = 160;
    public static final int VENDOR_REMOTE_BUTTON_DOWN = 138;
    public static final int VENDOR_REMOTE_BUTTON_UP = 139;

    public static final String toString(int i) {
        if (i == 0) {
            return "FEATURE_ABORT";
        }
        if (i == 4) {
            return "IMAGE_VIEW_ON";
        }
        if (i == 5) {
            return "TUNER_STEP_INCREMENT";
        }
        if (i == 6) {
            return "TUNER_STEP_DECREMENT";
        }
        if (i == 7) {
            return "TUNER_DEVICE_STATUS";
        }
        if (i == 8) {
            return "GIVE_TUNER_DEVICE_STATUS";
        }
        if (i == 9) {
            return "RECORD_ON";
        }
        if (i == 10) {
            return "RECORD_STATUS";
        }
        if (i == 11) {
            return "RECORD_OFF";
        }
        if (i == 13) {
            return "TEXT_VIEW_ON";
        }
        if (i == 15) {
            return "RECORD_TV_SCREEN";
        }
        if (i == 26) {
            return "GIVE_DECK_STATUS";
        }
        if (i == 27) {
            return "DECK_STATUS";
        }
        if (i == 50) {
            return "SET_MENU_LANGUAGE";
        }
        if (i == 51) {
            return "CLEAR_ANALOG_TIMER";
        }
        if (i == 52) {
            return "SET_ANALOG_TIMER";
        }
        if (i == 53) {
            return "TIMER_STATUS";
        }
        if (i == 54) {
            return "STANDBY";
        }
        if (i == 65) {
            return "PLAY";
        }
        if (i == 66) {
            return "DECK_CONTROL";
        }
        if (i == 67) {
            return "TIMER_CLEARED_STATUS";
        }
        if (i == 68) {
            return "USER_CONTROL_PRESSED";
        }
        if (i == 69) {
            return "USER_CONTROL_RELEASED";
        }
        if (i == 70) {
            return "GIVE_OSD_NAME";
        }
        if (i == 71) {
            return "SET_OSD_NAME";
        }
        if (i == 100) {
            return "SET_OSD_STRING";
        }
        if (i == 103) {
            return "SET_TIMER_PROGRAM_TITLE";
        }
        if (i == 112) {
            return "SYSTEM_AUDIO_MODE_REQUEST";
        }
        if (i == 113) {
            return "GIVE_AUDIO_STATUS";
        }
        if (i == 114) {
            return "SET_SYSTEM_AUDIO_MODE";
        }
        if (i == 122) {
            return "REPORT_AUDIO_STATUS";
        }
        if (i == 125) {
            return "GIVE_SYSTEM_AUDIO_MODE_STATUS";
        }
        if (i == 126) {
            return "SYSTEM_AUDIO_MODE_STATUS";
        }
        if (i == 128) {
            return "ROUTING_CHANGE";
        }
        if (i == 129) {
            return "ROUTING_INFORMATION";
        }
        if (i == 130) {
            return "ACTIVE_SOURCE";
        }
        if (i == 131) {
            return "GIVE_PHYSICAL_ADDRESS";
        }
        if (i == 132) {
            return "REPORT_PHYSICAL_ADDRESS";
        }
        if (i == 133) {
            return "REQUEST_ACTIVE_SOURCE";
        }
        if (i == 134) {
            return "SET_STREAM_PATH";
        }
        if (i == 135) {
            return "DEVICE_VENDOR_ID";
        }
        if (i == 137) {
            return "VENDOR_COMMAND";
        }
        if (i == 138) {
            return "VENDOR_REMOTE_BUTTON_DOWN";
        }
        if (i == 139) {
            return "VENDOR_REMOTE_BUTTON_UP";
        }
        if (i == 140) {
            return "GIVE_DEVICE_VENDOR_ID";
        }
        if (i == 141) {
            return "MENU_REQUEST";
        }
        if (i == 142) {
            return "MENU_STATUS";
        }
        if (i == 143) {
            return "GIVE_DEVICE_POWER_STATUS";
        }
        if (i == 144) {
            return "REPORT_POWER_STATUS";
        }
        if (i == 145) {
            return "GET_MENU_LANGUAGE";
        }
        if (i == 146) {
            return "SELECT_ANALOG_SERVICE";
        }
        if (i == 147) {
            return "SELECT_DIGITAL_SERVICE";
        }
        if (i == 151) {
            return "SET_DIGITAL_TIMER";
        }
        if (i == 153) {
            return "CLEAR_DIGITAL_TIMER";
        }
        if (i == 154) {
            return "SET_AUDIO_RATE";
        }
        if (i == 157) {
            return "INACTIVE_SOURCE";
        }
        if (i == 158) {
            return "CEC_VERSION";
        }
        if (i == 159) {
            return "GET_CEC_VERSION";
        }
        if (i == 160) {
            return "VENDOR_COMMAND_WITH_ID";
        }
        if (i == 161) {
            return "CLEAR_EXTERNAL_TIMER";
        }
        if (i == 162) {
            return "SET_EXTERNAL_TIMER";
        }
        if (i == 163) {
            return "REPORT_SHORT_AUDIO_DESCRIPTOR";
        }
        if (i == 164) {
            return "REQUEST_SHORT_AUDIO_DESCRIPTOR";
        }
        if (i == 192) {
            return "INITIATE_ARC";
        }
        if (i == 193) {
            return "REPORT_ARC_INITIATED";
        }
        if (i == 194) {
            return "REPORT_ARC_TERMINATED";
        }
        if (i == 195) {
            return "REQUEST_ARC_INITIATION";
        }
        if (i == 196) {
            return "REQUEST_ARC_TERMINATION";
        }
        if (i == 197) {
            return "TERMINATE_ARC";
        }
        if (i == 255) {
            return "ABORT";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("FEATURE_ABORT");
        int i2 = 4;
        if ((i & 4) == 4) {
            arrayList.add("IMAGE_VIEW_ON");
        } else {
            i2 = 0;
        }
        if ((i & 5) == 5) {
            arrayList.add("TUNER_STEP_INCREMENT");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("TUNER_STEP_DECREMENT");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("TUNER_DEVICE_STATUS");
            i2 |= 7;
        }
        if ((i & 8) == 8) {
            arrayList.add("GIVE_TUNER_DEVICE_STATUS");
            i2 |= 8;
        }
        if ((i & 9) == 9) {
            arrayList.add("RECORD_ON");
            i2 |= 9;
        }
        if ((i & 10) == 10) {
            arrayList.add("RECORD_STATUS");
            i2 |= 10;
        }
        if ((i & 11) == 11) {
            arrayList.add("RECORD_OFF");
            i2 |= 11;
        }
        if ((i & 13) == 13) {
            arrayList.add("TEXT_VIEW_ON");
            i2 |= 13;
        }
        if ((i & 15) == 15) {
            arrayList.add("RECORD_TV_SCREEN");
            i2 |= 15;
        }
        if ((i & 26) == 26) {
            arrayList.add("GIVE_DECK_STATUS");
            i2 |= 26;
        }
        if ((i & 27) == 27) {
            arrayList.add("DECK_STATUS");
            i2 |= 27;
        }
        if ((i & 50) == 50) {
            arrayList.add("SET_MENU_LANGUAGE");
            i2 |= 50;
        }
        if ((i & 51) == 51) {
            arrayList.add("CLEAR_ANALOG_TIMER");
            i2 |= 51;
        }
        if ((i & 52) == 52) {
            arrayList.add("SET_ANALOG_TIMER");
            i2 |= 52;
        }
        if ((i & 53) == 53) {
            arrayList.add("TIMER_STATUS");
            i2 |= 53;
        }
        if ((i & 54) == 54) {
            arrayList.add("STANDBY");
            i2 |= 54;
        }
        if ((i & 65) == 65) {
            arrayList.add("PLAY");
            i2 |= 65;
        }
        if ((i & 66) == 66) {
            arrayList.add("DECK_CONTROL");
            i2 |= 66;
        }
        if ((i & 67) == 67) {
            arrayList.add("TIMER_CLEARED_STATUS");
            i2 |= 67;
        }
        if ((i & 68) == 68) {
            arrayList.add("USER_CONTROL_PRESSED");
            i2 |= 68;
        }
        if ((i & 69) == 69) {
            arrayList.add("USER_CONTROL_RELEASED");
            i2 |= 69;
        }
        if ((i & 70) == 70) {
            arrayList.add("GIVE_OSD_NAME");
            i2 |= 70;
        }
        if ((i & 71) == 71) {
            arrayList.add("SET_OSD_NAME");
            i2 |= 71;
        }
        if ((i & 100) == 100) {
            arrayList.add("SET_OSD_STRING");
            i2 |= 100;
        }
        if ((i & 103) == 103) {
            arrayList.add("SET_TIMER_PROGRAM_TITLE");
            i2 |= 103;
        }
        if ((i & 112) == 112) {
            arrayList.add("SYSTEM_AUDIO_MODE_REQUEST");
            i2 |= 112;
        }
        if ((i & 113) == 113) {
            arrayList.add("GIVE_AUDIO_STATUS");
            i2 |= 113;
        }
        if ((i & 114) == 114) {
            arrayList.add("SET_SYSTEM_AUDIO_MODE");
            i2 |= 114;
        }
        if ((i & 122) == 122) {
            arrayList.add("REPORT_AUDIO_STATUS");
            i2 |= 122;
        }
        if ((i & 125) == 125) {
            arrayList.add("GIVE_SYSTEM_AUDIO_MODE_STATUS");
            i2 |= 125;
        }
        if ((i & 126) == 126) {
            arrayList.add("SYSTEM_AUDIO_MODE_STATUS");
            i2 |= 126;
        }
        if ((i & 128) == 128) {
            arrayList.add("ROUTING_CHANGE");
            i2 |= 128;
        }
        if ((i & 129) == 129) {
            arrayList.add("ROUTING_INFORMATION");
            i2 |= 129;
        }
        if ((i & 130) == 130) {
            arrayList.add("ACTIVE_SOURCE");
            i2 |= 130;
        }
        if ((i & 131) == 131) {
            arrayList.add("GIVE_PHYSICAL_ADDRESS");
            i2 |= 131;
        }
        if ((i & 132) == 132) {
            arrayList.add("REPORT_PHYSICAL_ADDRESS");
            i2 |= 132;
        }
        if ((i & 133) == 133) {
            arrayList.add("REQUEST_ACTIVE_SOURCE");
            i2 |= 133;
        }
        if ((i & 134) == 134) {
            arrayList.add("SET_STREAM_PATH");
            i2 |= 134;
        }
        if ((i & 135) == 135) {
            arrayList.add("DEVICE_VENDOR_ID");
            i2 |= 135;
        }
        if ((i & 137) == 137) {
            arrayList.add("VENDOR_COMMAND");
            i2 |= 137;
        }
        if ((i & 138) == 138) {
            arrayList.add("VENDOR_REMOTE_BUTTON_DOWN");
            i2 |= 138;
        }
        if ((i & 139) == 139) {
            arrayList.add("VENDOR_REMOTE_BUTTON_UP");
            i2 |= 139;
        }
        if ((i & 140) == 140) {
            arrayList.add("GIVE_DEVICE_VENDOR_ID");
            i2 |= 140;
        }
        if ((i & 141) == 141) {
            arrayList.add("MENU_REQUEST");
            i2 |= 141;
        }
        if ((i & 142) == 142) {
            arrayList.add("MENU_STATUS");
            i2 |= 142;
        }
        if ((i & 143) == 143) {
            arrayList.add("GIVE_DEVICE_POWER_STATUS");
            i2 |= 143;
        }
        if ((i & 144) == 144) {
            arrayList.add("REPORT_POWER_STATUS");
            i2 |= 144;
        }
        if ((i & 145) == 145) {
            arrayList.add("GET_MENU_LANGUAGE");
            i2 |= 145;
        }
        if ((i & 146) == 146) {
            arrayList.add("SELECT_ANALOG_SERVICE");
            i2 |= 146;
        }
        if ((i & 147) == 147) {
            arrayList.add("SELECT_DIGITAL_SERVICE");
            i2 |= 147;
        }
        if ((i & 151) == 151) {
            arrayList.add("SET_DIGITAL_TIMER");
            i2 |= 151;
        }
        if ((i & 153) == 153) {
            arrayList.add("CLEAR_DIGITAL_TIMER");
            i2 |= 153;
        }
        if ((i & 154) == 154) {
            arrayList.add("SET_AUDIO_RATE");
            i2 |= 154;
        }
        if ((i & 157) == 157) {
            arrayList.add("INACTIVE_SOURCE");
            i2 |= 157;
        }
        if ((i & 158) == 158) {
            arrayList.add("CEC_VERSION");
            i2 |= 158;
        }
        if ((i & 159) == 159) {
            arrayList.add("GET_CEC_VERSION");
            i2 |= 159;
        }
        if ((i & 160) == 160) {
            arrayList.add("VENDOR_COMMAND_WITH_ID");
            i2 |= 160;
        }
        if ((i & 161) == 161) {
            arrayList.add("CLEAR_EXTERNAL_TIMER");
            i2 |= 161;
        }
        if ((i & 162) == 162) {
            arrayList.add("SET_EXTERNAL_TIMER");
            i2 |= 162;
        }
        if ((i & 163) == 163) {
            arrayList.add("REPORT_SHORT_AUDIO_DESCRIPTOR");
            i2 |= 163;
        }
        if ((i & 164) == 164) {
            arrayList.add("REQUEST_SHORT_AUDIO_DESCRIPTOR");
            i2 |= 164;
        }
        if ((i & 192) == 192) {
            arrayList.add("INITIATE_ARC");
            i2 |= 192;
        }
        if ((i & 193) == 193) {
            arrayList.add("REPORT_ARC_INITIATED");
            i2 |= 193;
        }
        if ((i & 194) == 194) {
            arrayList.add("REPORT_ARC_TERMINATED");
            i2 |= 194;
        }
        if ((i & 195) == 195) {
            arrayList.add("REQUEST_ARC_INITIATION");
            i2 |= 195;
        }
        if ((i & 196) == 196) {
            arrayList.add("REQUEST_ARC_TERMINATION");
            i2 |= 196;
        }
        if ((i & 197) == 197) {
            arrayList.add("TERMINATE_ARC");
            i2 |= 197;
        }
        if ((i & 255) == 255) {
            arrayList.add("ABORT");
            i2 |= 255;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
