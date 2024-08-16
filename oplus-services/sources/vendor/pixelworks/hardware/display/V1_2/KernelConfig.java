package vendor.pixelworks.hardware.display.V1_2;

import java.util.ArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes3.dex */
public final class KernelConfig {
    public static final int AL_ENABLE = 44;
    public static final int ANALOG_BYPASS_MODE = 56;
    public static final int APP_FILTER = 111;
    public static final int AP_TE = 129;
    public static final int BLACK_BORDER = 22;
    public static final int BLACK_LIST_ADD = 24;
    public static final int BLACK_LIST_RST = 25;
    public static final int BLC_PWM_ENABLE = 68;
    public static final int BRIGHTNESS = 7;
    public static final int BRIGHTNESS_CHIP = 82;
    public static final int BYPASS_ENABLE = 30;
    public static final int CCF1_UPDATE = 71;
    public static final int CCF2_UPDATE = 72;
    public static final int CCT_VALUE = 35;
    public static final int CHIP_VERSION = 33;
    public static final int CINEMA_MODE = 23;
    public static final int CLEAR_TRIGGER = 88;
    public static final int CM_6AXES = 37;
    public static final int CM_BLUE_GAIN = 66;
    public static final int CM_COLOR_GAMUT = 40;
    public static final int CM_COLOR_GAMUT_PRE = 51;
    public static final int CM_COLOR_TEMP_MODE = 39;
    public static final int CM_CYAN_GAIN = 67;
    public static final int CM_FTC_ENABLE = 38;
    public static final int CM_GREEN_GAIN = 65;
    public static final int CM_MAGENTA_GAIN = 62;
    public static final int CM_PARA_SET = 138;
    public static final int CM_RATIO_SET = 210;
    public static final int CM_RED_GAIN = 63;
    public static final int CM_SETTING = 32;
    public static final int CM_YELLOW_GAIN = 64;
    public static final int COLOR_ADJUST = 26;
    public static final int COLOR_TEMP_VALUE = 48;
    public static final int CONTRAST = 6;
    public static final int CONTRAST_DIMMING = 80;
    public static final int CSC_MATRIX = 75;
    public static final int DBC_CONFIG = 11;
    public static final int DBC_LCE_DATA_PATH = 53;
    public static final int DBC_LCE_POWER = 52;
    public static final int DBC_LED_GAIN = 69;
    public static final int DBC_LEVEL = 45;
    public static final int DBC_QUALITY = 9;
    public static final int DBG_KERNEL_LOG_LEVEL = 106;
    public static final int DBG_LOOP_BACK_MODE = 108;
    public static final int DBG_LOOP_BACK_MODE_RES = 109;
    public static final int DBG_SEND_PACKAGE = 107;
    public static final int DBG_TARGET_PI_REGADDR_SET = 102;
    public static final int DBG_TARGET_REGADDR_VALUE_GET = 103;
    public static final int DBG_TARGET_REGADDR_VALUE_SET = 105;
    public static final int DBG_TARGET_REGADDR_VALUE_SET2 = 112;
    public static final int DCE_LEVEL = 16;
    public static final int DC_DIMMING = 87;
    public static final int DEBUG_CAP = 113;
    public static final int DEBUG_GET = 176;
    public static final int DEBUG_SET = 175;
    public static final int DEMO_MODE = 46;
    public static final int DEMURA_ENABLE = 194;
    public static final int DEMURA_LUT_SET = 193;
    public static final int DEMURA_XY_LUT_SET = 195;
    public static final int DLV_SENSITIVITY = 10;
    public static final int DMA_LOAD = 55;
    public static final int DPORT_DISABLE = 159;
    public static final int DPORT_DIS_DEBUG = 160;
    public static final int DPP_3DLUT_GAIN = 163;
    public static final int DPP_CSC_SET = 191;
    public static final int DPP_DEMO_WINDOW = 139;
    public static final int DPP_FADE_INOUT = 141;
    public static final int DPP_ONLY = 59;
    public static final int DPP_PATH_MUX = 142;
    public static final int DUAL2SINGLE_ST = 134;
    public static final int DUAL_CH_CTRL = 172;
    public static final int DYNAMIC_POWER_CTRL = 54;
    public static final int EXTERNAL_PWM = 8;
    public static final int FINGER_DISPLAY = 140;
    public static final int FRC_LOW_LATENCY = 127;
    public static final int FW_UPDATE = 73;
    public static final int GAMMA = 4;
    public static final int GAMMA_MODE = 143;
    public static final int GAMMA_TABLE_ENABLE = 58;
    public static final int GET_METADATA = 171;
    public static final int GET_PANEL_INFO = 220;
    public static final int GRAPHIC_DET_ENABLE = 43;
    public static final int HDR10PLUS = 161;
    public static final int HDR_COMPLETE = 91;
    public static final int HDR_DATA_PATH = 190;
    public static final int HDR_MAXCLL = 49;
    public static final int HDR_PANEL_NITES_SET = 60;
    public static final int HDR_PREPARE = 90;
    public static final int HDR_SETTING = 50;
    public static final int HUE_SAT_ADJ = 74;
    public static final int KERNEL_STATUS_GET = 177;
    public static final int LAYER_SIZE = 21;
    public static final int LCE_DEMO_WINDOW = 137;
    public static final int LCE_GAIN = 98;
    public static final int LCE_LEVEL = 42;
    public static final int LCE_MODE = 41;
    public static final int LCE_SETTING = 31;
    public static final int LUX_VALUE = 34;
    public static final int MCF_DATA = 92;
    public static final int MEMC_ACTIVE = 18;
    public static final int MEMC_CTRL = 173;
    public static final int MEMC_DEMO = 2;
    public static final int MEMC_ENABLE = 13;
    public static final int MEMC_ENABLE_FOR_ASUS_CAMERA = 104;
    public static final int MEMC_INFO_SET = 174;
    public static final int MEMC_LEVEL = 5;
    public static final int MEMC_LOWPOWER = 15;
    public static final int MEMC_OPTION = 14;
    public static final int MEMC_OSD = 135;
    public static final int MEMC_OSD_PROTECT = 136;
    public static final int MIPI2RX_PWRST = 133;
    public static final int MODE_SET = 120;
    public static final int N2M_ENABLE = 130;
    public static final int OSD_AUTOREFRESH = 124;
    public static final int OSD_ENABLE = 123;
    public static final int OSD_LAYER_EMPTY = 168;
    public static final int OSD_OVERFLOW_ST = 125;
    public static final int OSD_PATTERN_SHOW = 101;
    public static final int OUT_FRAME_RATE_SET = 122;
    public static final int PANEL_NITS = 99;
    public static final int PANEL_TE = 128;
    public static final int PANEL_TYPE = 57;
    public static final int PARAM_VALID = 144;
    public static final int PEAKING = 0;
    public static final int PEAKING_DEMO = 3;
    public static final int PEAKING_IDLE_CLK_ENABLE = 61;
    public static final int PQ_CONFIG = 12;
    public static final int PT_ENABLE = 29;
    public static final int PT_SR_SET = 192;
    public static final int PWIL_DPORT_DISABLE = 189;
    public static final int READING_MODE = 36;
    public static final int SCALER_FILTER_LEVEL = 70;
    public static final int SCALER_PP_FILTER_LEVEL = 76;
    public static final int SDR2HDR = 47;
    public static final int SDR2HDR_AI_DE = 187;
    public static final int SDR2HDR_AI_ENALE = 145;
    public static final int SDR2HDR_AI_GRAPHIC = 188;
    public static final int SDR2HDR_AI_INPUT_AMBIENTLIGHT = 146;
    public static final int SDR2HDR_AI_INPUT_BACKLIGHT = 148;
    public static final int SDR2HDR_AI_LCE = 186;
    public static final int SDR2HDR_AI_SWITCH = 162;
    public static final int SDR2HDR_AI_SWITCH_PRE = 165;
    public static final int SDR2HDR_AI_TM = 185;
    public static final int SDR2HDR_DE = 156;
    public static final int SDR2HDR_FTC = 158;
    public static final int SDR2HDR_LCE = 155;
    public static final int SDR2HDR_TF_COEF = 157;
    public static final int SDR2HDR_UPDATE = 197;
    public static final int SEND_FRAME = 100;
    public static final int SET_DSI_MODE_INFO = 167;
    public static final int SET_METADATA = 169;
    public static final int SET_METADATA_LOCK = 170;
    public static final int SET_MVD_META = 178;
    public static final int SHARPNESS = 1;
    public static final int S_CURVE = 81;
    public static final int TRUE_CUT_CAP = 27;
    public static final int TRUE_CUT_DET = 28;
    public static final int TYPE_MAX = 139;
    public static final int TYPE_MAX_V1_1 = 255;
    public static final int TYPE_MAX_V1_2 = 1023;
    public static final int TYPE_MIN_V1_2 = 768;
    public static final int USER_DEMO_WND = 17;
    public static final int VIDEO_FRAME_RATE_SET = 121;
    public static final int WAIT_VSYNC = 132;
    public static final int WHITE_LIST_ADD = 19;
    public static final int WHITE_LIST_RST = 20;
    public static final int WORK_MODE = 126;
    public static final int Y5P = 93;

    public static final String toString(int i) {
        if (i == 0) {
            return "PEAKING";
        }
        if (i == 1) {
            return "SHARPNESS";
        }
        if (i == 2) {
            return "MEMC_DEMO";
        }
        if (i == 3) {
            return "PEAKING_DEMO";
        }
        if (i == 4) {
            return "GAMMA";
        }
        if (i == 5) {
            return "MEMC_LEVEL";
        }
        if (i == 6) {
            return "CONTRAST";
        }
        if (i == 7) {
            return "BRIGHTNESS";
        }
        if (i == 8) {
            return "EXTERNAL_PWM";
        }
        if (i == 9) {
            return "DBC_QUALITY";
        }
        if (i == 10) {
            return "DLV_SENSITIVITY";
        }
        if (i == 11) {
            return "DBC_CONFIG";
        }
        if (i == 12) {
            return "PQ_CONFIG";
        }
        if (i == 13) {
            return "MEMC_ENABLE";
        }
        if (i == 14) {
            return "MEMC_OPTION";
        }
        if (i == 15) {
            return "MEMC_LOWPOWER";
        }
        if (i == 16) {
            return "DCE_LEVEL";
        }
        if (i == 17) {
            return "USER_DEMO_WND";
        }
        if (i == 18) {
            return "MEMC_ACTIVE";
        }
        if (i == 19) {
            return "WHITE_LIST_ADD";
        }
        if (i == 20) {
            return "WHITE_LIST_RST";
        }
        if (i == 21) {
            return "LAYER_SIZE";
        }
        if (i == 22) {
            return "BLACK_BORDER";
        }
        if (i == 23) {
            return "CINEMA_MODE";
        }
        if (i == 24) {
            return "BLACK_LIST_ADD";
        }
        if (i == 25) {
            return "BLACK_LIST_RST";
        }
        if (i == 26) {
            return "COLOR_ADJUST";
        }
        if (i == 27) {
            return "TRUE_CUT_CAP";
        }
        if (i == 28) {
            return "TRUE_CUT_DET";
        }
        if (i == 29) {
            return "PT_ENABLE";
        }
        if (i == 30) {
            return "BYPASS_ENABLE";
        }
        if (i == 31) {
            return "LCE_SETTING";
        }
        if (i == 32) {
            return "CM_SETTING";
        }
        if (i == 33) {
            return "CHIP_VERSION";
        }
        if (i == 34) {
            return "LUX_VALUE";
        }
        if (i == 35) {
            return "CCT_VALUE";
        }
        if (i == 36) {
            return "READING_MODE";
        }
        if (i == 37) {
            return "CM_6AXES";
        }
        if (i == 38) {
            return "CM_FTC_ENABLE";
        }
        if (i == 39) {
            return "CM_COLOR_TEMP_MODE";
        }
        if (i == 40) {
            return "CM_COLOR_GAMUT";
        }
        if (i == 41) {
            return "LCE_MODE";
        }
        if (i == 42) {
            return "LCE_LEVEL";
        }
        if (i == 43) {
            return "GRAPHIC_DET_ENABLE";
        }
        if (i == 44) {
            return "AL_ENABLE";
        }
        if (i == 45) {
            return "DBC_LEVEL";
        }
        if (i == 46) {
            return "DEMO_MODE";
        }
        if (i == 47) {
            return "SDR2HDR";
        }
        if (i == 48) {
            return "COLOR_TEMP_VALUE";
        }
        if (i == 49) {
            return "HDR_MAXCLL";
        }
        if (i == 50) {
            return "HDR_SETTING";
        }
        if (i == 51) {
            return "CM_COLOR_GAMUT_PRE";
        }
        if (i == 52) {
            return "DBC_LCE_POWER";
        }
        if (i == 53) {
            return "DBC_LCE_DATA_PATH";
        }
        if (i == 54) {
            return "DYNAMIC_POWER_CTRL";
        }
        if (i == 55) {
            return "DMA_LOAD";
        }
        if (i == 56) {
            return "ANALOG_BYPASS_MODE";
        }
        if (i == 57) {
            return "PANEL_TYPE";
        }
        if (i == 58) {
            return "GAMMA_TABLE_ENABLE";
        }
        if (i == 60) {
            return "HDR_PANEL_NITES_SET";
        }
        if (i == 61) {
            return "PEAKING_IDLE_CLK_ENABLE";
        }
        if (i == 62) {
            return "CM_MAGENTA_GAIN";
        }
        if (i == 63) {
            return "CM_RED_GAIN";
        }
        if (i == 64) {
            return "CM_YELLOW_GAIN";
        }
        if (i == 65) {
            return "CM_GREEN_GAIN";
        }
        if (i == 66) {
            return "CM_BLUE_GAIN";
        }
        if (i == 67) {
            return "CM_CYAN_GAIN";
        }
        if (i == 68) {
            return "BLC_PWM_ENABLE";
        }
        if (i == 69) {
            return "DBC_LED_GAIN";
        }
        if (i == 70) {
            return "SCALER_FILTER_LEVEL";
        }
        if (i == 71) {
            return "CCF1_UPDATE";
        }
        if (i == 72) {
            return "CCF2_UPDATE";
        }
        if (i == 73) {
            return "FW_UPDATE";
        }
        if (i == 74) {
            return "HUE_SAT_ADJ";
        }
        if (i == 75) {
            return "CSC_MATRIX";
        }
        if (i == 76) {
            return "SCALER_PP_FILTER_LEVEL";
        }
        if (i == 80) {
            return "CONTRAST_DIMMING";
        }
        if (i == 81) {
            return "S_CURVE";
        }
        if (i == 82) {
            return "BRIGHTNESS_CHIP";
        }
        if (i == 90) {
            return "HDR_PREPARE";
        }
        if (i == 91) {
            return "HDR_COMPLETE";
        }
        if (i == 92) {
            return "MCF_DATA";
        }
        if (i == 93) {
            return "Y5P";
        }
        if (i == 98) {
            return "LCE_GAIN";
        }
        if (i == 99) {
            return "PANEL_NITS";
        }
        if (i == 100) {
            return "SEND_FRAME";
        }
        if (i == 101) {
            return "OSD_PATTERN_SHOW";
        }
        if (i == 102) {
            return "DBG_TARGET_PI_REGADDR_SET";
        }
        if (i == 103) {
            return "DBG_TARGET_REGADDR_VALUE_GET";
        }
        if (i == 104) {
            return "MEMC_ENABLE_FOR_ASUS_CAMERA";
        }
        if (i == 105) {
            return "DBG_TARGET_REGADDR_VALUE_SET";
        }
        if (i == 106) {
            return "DBG_KERNEL_LOG_LEVEL";
        }
        if (i == 107) {
            return "DBG_SEND_PACKAGE";
        }
        if (i == 108) {
            return "DBG_LOOP_BACK_MODE";
        }
        if (i == 109) {
            return "DBG_LOOP_BACK_MODE_RES";
        }
        if (i == 111) {
            return "APP_FILTER";
        }
        if (i == 112) {
            return "DBG_TARGET_REGADDR_VALUE_SET2";
        }
        if (i == 113) {
            return "DEBUG_CAP";
        }
        if (i == 120) {
            return "MODE_SET";
        }
        if (i == 121) {
            return "VIDEO_FRAME_RATE_SET";
        }
        if (i == 122) {
            return "OUT_FRAME_RATE_SET";
        }
        if (i == 123) {
            return "OSD_ENABLE";
        }
        if (i == 124) {
            return "OSD_AUTOREFRESH";
        }
        if (i == 125) {
            return "OSD_OVERFLOW_ST";
        }
        if (i == 126) {
            return "WORK_MODE";
        }
        if (i == 127) {
            return "FRC_LOW_LATENCY";
        }
        if (i == 128) {
            return "PANEL_TE";
        }
        if (i == 129) {
            return "AP_TE";
        }
        if (i == 130) {
            return "N2M_ENABLE";
        }
        if (i == 132) {
            return "WAIT_VSYNC";
        }
        if (i == 133) {
            return "MIPI2RX_PWRST";
        }
        if (i == 134) {
            return "DUAL2SINGLE_ST";
        }
        if (i == 135) {
            return "MEMC_OSD";
        }
        if (i == 136) {
            return "MEMC_OSD_PROTECT";
        }
        if (i == 137) {
            return "LCE_DEMO_WINDOW";
        }
        if (i == 138) {
            return "CM_PARA_SET";
        }
        if (i == 139) {
            return "TYPE_MAX";
        }
        if (i == 59) {
            return "DPP_ONLY";
        }
        if (i == 87) {
            return "DC_DIMMING";
        }
        if (i == 88) {
            return "CLEAR_TRIGGER";
        }
        if (i == 139) {
            return "DPP_DEMO_WINDOW";
        }
        if (i == 140) {
            return "FINGER_DISPLAY";
        }
        if (i == 141) {
            return "DPP_FADE_INOUT";
        }
        if (i == 142) {
            return "DPP_PATH_MUX";
        }
        if (i == 143) {
            return "GAMMA_MODE";
        }
        if (i == 144) {
            return "PARAM_VALID";
        }
        if (i == 159) {
            return "DPORT_DISABLE";
        }
        if (i == 160) {
            return "DPORT_DIS_DEBUG";
        }
        if (i == 161) {
            return "HDR10PLUS";
        }
        if (i == 163) {
            return "DPP_3DLUT_GAIN";
        }
        if (i == 162) {
            return "SDR2HDR_AI_SWITCH";
        }
        if (i == 165) {
            return "SDR2HDR_AI_SWITCH_PRE";
        }
        if (i == 167) {
            return "SET_DSI_MODE_INFO";
        }
        if (i == 168) {
            return "OSD_LAYER_EMPTY";
        }
        if (i == 169) {
            return "SET_METADATA";
        }
        if (i == 170) {
            return "SET_METADATA_LOCK";
        }
        if (i == 171) {
            return "GET_METADATA";
        }
        if (i == 172) {
            return "DUAL_CH_CTRL";
        }
        if (i == 173) {
            return "MEMC_CTRL";
        }
        if (i == 174) {
            return "MEMC_INFO_SET";
        }
        if (i == 175) {
            return "DEBUG_SET";
        }
        if (i == 176) {
            return "DEBUG_GET";
        }
        if (i == 177) {
            return "KERNEL_STATUS_GET";
        }
        if (i == 178) {
            return "SET_MVD_META";
        }
        if (i == 189) {
            return "PWIL_DPORT_DISABLE";
        }
        if (i == 190) {
            return "HDR_DATA_PATH";
        }
        if (i == 191) {
            return "DPP_CSC_SET";
        }
        if (i == 192) {
            return "PT_SR_SET";
        }
        if (i == 193) {
            return "DEMURA_LUT_SET";
        }
        if (i == 194) {
            return "DEMURA_ENABLE";
        }
        if (i == 195) {
            return "DEMURA_XY_LUT_SET";
        }
        if (i == 210) {
            return "CM_RATIO_SET";
        }
        if (i == 220) {
            return "GET_PANEL_INFO";
        }
        if (i == 255) {
            return "TYPE_MAX_V1_1";
        }
        if (i == 145) {
            return "SDR2HDR_AI_ENALE";
        }
        if (i == 146) {
            return "SDR2HDR_AI_INPUT_AMBIENTLIGHT";
        }
        if (i == 148) {
            return "SDR2HDR_AI_INPUT_BACKLIGHT";
        }
        if (i == 155) {
            return "SDR2HDR_LCE";
        }
        if (i == 156) {
            return "SDR2HDR_DE";
        }
        if (i == 157) {
            return "SDR2HDR_TF_COEF";
        }
        if (i == 158) {
            return "SDR2HDR_FTC";
        }
        if (i == 185) {
            return "SDR2HDR_AI_TM";
        }
        if (i == 186) {
            return "SDR2HDR_AI_LCE";
        }
        if (i == 187) {
            return "SDR2HDR_AI_DE";
        }
        if (i == 188) {
            return "SDR2HDR_AI_GRAPHIC";
        }
        if (i == 197) {
            return "SDR2HDR_UPDATE";
        }
        if (i == 768) {
            return "TYPE_MIN_V1_2";
        }
        if (i == 1023) {
            return "TYPE_MAX_V1_2";
        }
        return "0x" + Integer.toHexString(i);
    }

    public static final String dumpBitfield(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("PEAKING");
        int i2 = 1;
        if ((i & 1) == 1) {
            arrayList.add("SHARPNESS");
        } else {
            i2 = 0;
        }
        if ((i & 2) == 2) {
            arrayList.add("MEMC_DEMO");
            i2 |= 2;
        }
        if ((i & 3) == 3) {
            arrayList.add("PEAKING_DEMO");
            i2 |= 3;
        }
        if ((i & 4) == 4) {
            arrayList.add("GAMMA");
            i2 |= 4;
        }
        if ((i & 5) == 5) {
            arrayList.add("MEMC_LEVEL");
            i2 |= 5;
        }
        if ((i & 6) == 6) {
            arrayList.add("CONTRAST");
            i2 |= 6;
        }
        if ((i & 7) == 7) {
            arrayList.add("BRIGHTNESS");
            i2 |= 7;
        }
        if ((i & 8) == 8) {
            arrayList.add("EXTERNAL_PWM");
            i2 |= 8;
        }
        if ((i & 9) == 9) {
            arrayList.add("DBC_QUALITY");
            i2 |= 9;
        }
        if ((i & 10) == 10) {
            arrayList.add("DLV_SENSITIVITY");
            i2 |= 10;
        }
        if ((i & 11) == 11) {
            arrayList.add("DBC_CONFIG");
            i2 |= 11;
        }
        if ((i & 12) == 12) {
            arrayList.add("PQ_CONFIG");
            i2 |= 12;
        }
        if ((i & 13) == 13) {
            arrayList.add("MEMC_ENABLE");
            i2 |= 13;
        }
        if ((i & 14) == 14) {
            arrayList.add("MEMC_OPTION");
            i2 |= 14;
        }
        if ((i & 15) == 15) {
            arrayList.add("MEMC_LOWPOWER");
            i2 |= 15;
        }
        if ((i & 16) == 16) {
            arrayList.add("DCE_LEVEL");
            i2 |= 16;
        }
        if ((i & 17) == 17) {
            arrayList.add("USER_DEMO_WND");
            i2 |= 17;
        }
        if ((i & 18) == 18) {
            arrayList.add("MEMC_ACTIVE");
            i2 |= 18;
        }
        if ((i & 19) == 19) {
            arrayList.add("WHITE_LIST_ADD");
            i2 |= 19;
        }
        if ((i & 20) == 20) {
            arrayList.add("WHITE_LIST_RST");
            i2 |= 20;
        }
        if ((i & 21) == 21) {
            arrayList.add("LAYER_SIZE");
            i2 |= 21;
        }
        if ((i & 22) == 22) {
            arrayList.add("BLACK_BORDER");
            i2 |= 22;
        }
        if ((i & 23) == 23) {
            arrayList.add("CINEMA_MODE");
            i2 |= 23;
        }
        if ((i & 24) == 24) {
            arrayList.add("BLACK_LIST_ADD");
            i2 |= 24;
        }
        if ((i & 25) == 25) {
            arrayList.add("BLACK_LIST_RST");
            i2 |= 25;
        }
        if ((i & 26) == 26) {
            arrayList.add("COLOR_ADJUST");
            i2 |= 26;
        }
        if ((i & 27) == 27) {
            arrayList.add("TRUE_CUT_CAP");
            i2 |= 27;
        }
        if ((i & 28) == 28) {
            arrayList.add("TRUE_CUT_DET");
            i2 |= 28;
        }
        if ((i & 29) == 29) {
            arrayList.add("PT_ENABLE");
            i2 |= 29;
        }
        if ((i & 30) == 30) {
            arrayList.add("BYPASS_ENABLE");
            i2 |= 30;
        }
        if ((i & 31) == 31) {
            arrayList.add("LCE_SETTING");
            i2 |= 31;
        }
        if ((i & 32) == 32) {
            arrayList.add("CM_SETTING");
            i2 |= 32;
        }
        if ((i & 33) == 33) {
            arrayList.add("CHIP_VERSION");
            i2 |= 33;
        }
        if ((i & 34) == 34) {
            arrayList.add("LUX_VALUE");
            i2 |= 34;
        }
        if ((i & 35) == 35) {
            arrayList.add("CCT_VALUE");
            i2 |= 35;
        }
        if ((i & 36) == 36) {
            arrayList.add("READING_MODE");
            i2 |= 36;
        }
        if ((i & 37) == 37) {
            arrayList.add("CM_6AXES");
            i2 |= 37;
        }
        if ((i & 38) == 38) {
            arrayList.add("CM_FTC_ENABLE");
            i2 |= 38;
        }
        if ((i & 39) == 39) {
            arrayList.add("CM_COLOR_TEMP_MODE");
            i2 |= 39;
        }
        if ((i & 40) == 40) {
            arrayList.add("CM_COLOR_GAMUT");
            i2 |= 40;
        }
        if ((i & 41) == 41) {
            arrayList.add("LCE_MODE");
            i2 |= 41;
        }
        if ((i & 42) == 42) {
            arrayList.add("LCE_LEVEL");
            i2 |= 42;
        }
        if ((i & 43) == 43) {
            arrayList.add("GRAPHIC_DET_ENABLE");
            i2 |= 43;
        }
        if ((i & 44) == 44) {
            arrayList.add("AL_ENABLE");
            i2 |= 44;
        }
        if ((i & 45) == 45) {
            arrayList.add("DBC_LEVEL");
            i2 |= 45;
        }
        if ((i & 46) == 46) {
            arrayList.add("DEMO_MODE");
            i2 |= 46;
        }
        if ((i & 47) == 47) {
            arrayList.add("SDR2HDR");
            i2 |= 47;
        }
        if ((i & 48) == 48) {
            arrayList.add("COLOR_TEMP_VALUE");
            i2 |= 48;
        }
        if ((i & 49) == 49) {
            arrayList.add("HDR_MAXCLL");
            i2 |= 49;
        }
        if ((i & 50) == 50) {
            arrayList.add("HDR_SETTING");
            i2 |= 50;
        }
        if ((i & 51) == 51) {
            arrayList.add("CM_COLOR_GAMUT_PRE");
            i2 |= 51;
        }
        if ((i & 52) == 52) {
            arrayList.add("DBC_LCE_POWER");
            i2 |= 52;
        }
        if ((i & 53) == 53) {
            arrayList.add("DBC_LCE_DATA_PATH");
            i2 |= 53;
        }
        if ((i & 54) == 54) {
            arrayList.add("DYNAMIC_POWER_CTRL");
            i2 |= 54;
        }
        if ((i & 55) == 55) {
            arrayList.add("DMA_LOAD");
            i2 |= 55;
        }
        if ((i & 56) == 56) {
            arrayList.add("ANALOG_BYPASS_MODE");
            i2 |= 56;
        }
        if ((i & 57) == 57) {
            arrayList.add("PANEL_TYPE");
            i2 |= 57;
        }
        if ((i & 58) == 58) {
            arrayList.add("GAMMA_TABLE_ENABLE");
            i2 |= 58;
        }
        if ((i & 60) == 60) {
            arrayList.add("HDR_PANEL_NITES_SET");
            i2 |= 60;
        }
        if ((i & 61) == 61) {
            arrayList.add("PEAKING_IDLE_CLK_ENABLE");
            i2 |= 61;
        }
        if ((i & 62) == 62) {
            arrayList.add("CM_MAGENTA_GAIN");
            i2 |= 62;
        }
        if ((i & 63) == 63) {
            arrayList.add("CM_RED_GAIN");
            i2 |= 63;
        }
        if ((i & 64) == 64) {
            arrayList.add("CM_YELLOW_GAIN");
            i2 |= 64;
        }
        if ((i & 65) == 65) {
            arrayList.add("CM_GREEN_GAIN");
            i2 |= 65;
        }
        if ((i & 66) == 66) {
            arrayList.add("CM_BLUE_GAIN");
            i2 |= 66;
        }
        if ((i & 67) == 67) {
            arrayList.add("CM_CYAN_GAIN");
            i2 |= 67;
        }
        if ((i & 68) == 68) {
            arrayList.add("BLC_PWM_ENABLE");
            i2 |= 68;
        }
        if ((i & 69) == 69) {
            arrayList.add("DBC_LED_GAIN");
            i2 |= 69;
        }
        if ((i & 70) == 70) {
            arrayList.add("SCALER_FILTER_LEVEL");
            i2 |= 70;
        }
        if ((i & 71) == 71) {
            arrayList.add("CCF1_UPDATE");
            i2 |= 71;
        }
        if ((i & 72) == 72) {
            arrayList.add("CCF2_UPDATE");
            i2 |= 72;
        }
        if ((i & 73) == 73) {
            arrayList.add("FW_UPDATE");
            i2 |= 73;
        }
        if ((i & 74) == 74) {
            arrayList.add("HUE_SAT_ADJ");
            i2 |= 74;
        }
        if ((i & 75) == 75) {
            arrayList.add("CSC_MATRIX");
            i2 |= 75;
        }
        if ((i & 76) == 76) {
            arrayList.add("SCALER_PP_FILTER_LEVEL");
            i2 |= 76;
        }
        if ((i & 80) == 80) {
            arrayList.add("CONTRAST_DIMMING");
            i2 |= 80;
        }
        if ((i & 81) == 81) {
            arrayList.add("S_CURVE");
            i2 |= 81;
        }
        if ((i & 82) == 82) {
            arrayList.add("BRIGHTNESS_CHIP");
            i2 |= 82;
        }
        if ((i & 90) == 90) {
            arrayList.add("HDR_PREPARE");
            i2 |= 90;
        }
        if ((i & 91) == 91) {
            arrayList.add("HDR_COMPLETE");
            i2 |= 91;
        }
        if ((i & 92) == 92) {
            arrayList.add("MCF_DATA");
            i2 |= 92;
        }
        if ((i & 93) == 93) {
            arrayList.add("Y5P");
            i2 |= 93;
        }
        if ((i & 98) == 98) {
            arrayList.add("LCE_GAIN");
            i2 |= 98;
        }
        if ((i & 99) == 99) {
            arrayList.add("PANEL_NITS");
            i2 |= 99;
        }
        if ((i & 100) == 100) {
            arrayList.add("SEND_FRAME");
            i2 |= 100;
        }
        if ((i & 101) == 101) {
            arrayList.add("OSD_PATTERN_SHOW");
            i2 |= 101;
        }
        if ((i & 102) == 102) {
            arrayList.add("DBG_TARGET_PI_REGADDR_SET");
            i2 |= 102;
        }
        if ((i & 103) == 103) {
            arrayList.add("DBG_TARGET_REGADDR_VALUE_GET");
            i2 |= 103;
        }
        if ((i & 104) == 104) {
            arrayList.add("MEMC_ENABLE_FOR_ASUS_CAMERA");
            i2 |= 104;
        }
        if ((i & 105) == 105) {
            arrayList.add("DBG_TARGET_REGADDR_VALUE_SET");
            i2 |= 105;
        }
        if ((i & 106) == 106) {
            arrayList.add("DBG_KERNEL_LOG_LEVEL");
            i2 |= 106;
        }
        if ((i & 107) == 107) {
            arrayList.add("DBG_SEND_PACKAGE");
            i2 |= 107;
        }
        if ((i & 108) == 108) {
            arrayList.add("DBG_LOOP_BACK_MODE");
            i2 |= 108;
        }
        if ((i & 109) == 109) {
            arrayList.add("DBG_LOOP_BACK_MODE_RES");
            i2 |= 109;
        }
        if ((i & 111) == 111) {
            arrayList.add("APP_FILTER");
            i2 |= 111;
        }
        if ((i & 112) == 112) {
            arrayList.add("DBG_TARGET_REGADDR_VALUE_SET2");
            i2 |= 112;
        }
        if ((i & 113) == 113) {
            arrayList.add("DEBUG_CAP");
            i2 |= 113;
        }
        if ((i & 120) == 120) {
            arrayList.add("MODE_SET");
            i2 |= 120;
        }
        if ((i & 121) == 121) {
            arrayList.add("VIDEO_FRAME_RATE_SET");
            i2 |= 121;
        }
        if ((i & 122) == 122) {
            arrayList.add("OUT_FRAME_RATE_SET");
            i2 |= 122;
        }
        if ((i & 123) == 123) {
            arrayList.add("OSD_ENABLE");
            i2 |= 123;
        }
        if ((i & 124) == 124) {
            arrayList.add("OSD_AUTOREFRESH");
            i2 |= 124;
        }
        if ((i & 125) == 125) {
            arrayList.add("OSD_OVERFLOW_ST");
            i2 |= 125;
        }
        if ((i & 126) == 126) {
            arrayList.add("WORK_MODE");
            i2 |= 126;
        }
        if ((i & 127) == 127) {
            arrayList.add("FRC_LOW_LATENCY");
            i2 |= 127;
        }
        if ((i & 128) == 128) {
            arrayList.add("PANEL_TE");
            i2 |= 128;
        }
        if ((i & 129) == 129) {
            arrayList.add("AP_TE");
            i2 |= 129;
        }
        if ((i & 130) == 130) {
            arrayList.add("N2M_ENABLE");
            i2 |= 130;
        }
        if ((i & 132) == 132) {
            arrayList.add("WAIT_VSYNC");
            i2 |= 132;
        }
        if ((i & 133) == 133) {
            arrayList.add("MIPI2RX_PWRST");
            i2 |= 133;
        }
        if ((i & 134) == 134) {
            arrayList.add("DUAL2SINGLE_ST");
            i2 |= 134;
        }
        if ((i & 135) == 135) {
            arrayList.add("MEMC_OSD");
            i2 |= 135;
        }
        if ((i & 136) == 136) {
            arrayList.add("MEMC_OSD_PROTECT");
            i2 |= 136;
        }
        if ((i & 137) == 137) {
            arrayList.add("LCE_DEMO_WINDOW");
            i2 |= 137;
        }
        if ((i & 138) == 138) {
            arrayList.add("CM_PARA_SET");
            i2 |= 138;
        }
        int i3 = i & 139;
        if (i3 == 139) {
            arrayList.add("TYPE_MAX");
            i2 |= 139;
        }
        if ((i & 59) == 59) {
            arrayList.add("DPP_ONLY");
            i2 |= 59;
        }
        if ((i & 87) == 87) {
            arrayList.add("DC_DIMMING");
            i2 |= 87;
        }
        if ((i & 88) == 88) {
            arrayList.add("CLEAR_TRIGGER");
            i2 |= 88;
        }
        if (i3 == 139) {
            arrayList.add("DPP_DEMO_WINDOW");
            i2 |= 139;
        }
        if ((i & 140) == 140) {
            arrayList.add("FINGER_DISPLAY");
            i2 |= 140;
        }
        if ((i & 141) == 141) {
            arrayList.add("DPP_FADE_INOUT");
            i2 |= 141;
        }
        if ((i & 142) == 142) {
            arrayList.add("DPP_PATH_MUX");
            i2 |= 142;
        }
        if ((i & 143) == 143) {
            arrayList.add("GAMMA_MODE");
            i2 |= 143;
        }
        if ((i & 144) == 144) {
            arrayList.add("PARAM_VALID");
            i2 |= 144;
        }
        if ((i & 159) == 159) {
            arrayList.add("DPORT_DISABLE");
            i2 |= 159;
        }
        if ((i & 160) == 160) {
            arrayList.add("DPORT_DIS_DEBUG");
            i2 |= 160;
        }
        if ((i & 161) == 161) {
            arrayList.add("HDR10PLUS");
            i2 |= 161;
        }
        if ((i & 163) == 163) {
            arrayList.add("DPP_3DLUT_GAIN");
            i2 |= 163;
        }
        if ((i & 162) == 162) {
            arrayList.add("SDR2HDR_AI_SWITCH");
            i2 |= 162;
        }
        if ((i & 165) == 165) {
            arrayList.add("SDR2HDR_AI_SWITCH_PRE");
            i2 |= 165;
        }
        if ((i & 167) == 167) {
            arrayList.add("SET_DSI_MODE_INFO");
            i2 |= 167;
        }
        if ((i & 168) == 168) {
            arrayList.add("OSD_LAYER_EMPTY");
            i2 |= 168;
        }
        if ((i & 169) == 169) {
            arrayList.add("SET_METADATA");
            i2 |= 169;
        }
        if ((i & 170) == 170) {
            arrayList.add("SET_METADATA_LOCK");
            i2 |= 170;
        }
        if ((i & 171) == 171) {
            arrayList.add("GET_METADATA");
            i2 |= 171;
        }
        if ((i & 172) == 172) {
            arrayList.add("DUAL_CH_CTRL");
            i2 |= 172;
        }
        if ((i & 173) == 173) {
            arrayList.add("MEMC_CTRL");
            i2 |= 173;
        }
        if ((i & 174) == 174) {
            arrayList.add("MEMC_INFO_SET");
            i2 |= 174;
        }
        if ((i & 175) == 175) {
            arrayList.add("DEBUG_SET");
            i2 |= 175;
        }
        if ((i & 176) == 176) {
            arrayList.add("DEBUG_GET");
            i2 |= 176;
        }
        if ((i & 177) == 177) {
            arrayList.add("KERNEL_STATUS_GET");
            i2 |= 177;
        }
        if ((i & 178) == 178) {
            arrayList.add("SET_MVD_META");
            i2 |= 178;
        }
        if ((i & 189) == 189) {
            arrayList.add("PWIL_DPORT_DISABLE");
            i2 |= 189;
        }
        if ((i & 190) == 190) {
            arrayList.add("HDR_DATA_PATH");
            i2 |= 190;
        }
        if ((i & 191) == 191) {
            arrayList.add("DPP_CSC_SET");
            i2 |= 191;
        }
        if ((i & 192) == 192) {
            arrayList.add("PT_SR_SET");
            i2 |= 192;
        }
        if ((i & 193) == 193) {
            arrayList.add("DEMURA_LUT_SET");
            i2 |= 193;
        }
        if ((i & 194) == 194) {
            arrayList.add("DEMURA_ENABLE");
            i2 |= 194;
        }
        if ((i & 195) == 195) {
            arrayList.add("DEMURA_XY_LUT_SET");
            i2 |= 195;
        }
        if ((i & 210) == 210) {
            arrayList.add("CM_RATIO_SET");
            i2 |= 210;
        }
        if ((i & 220) == 220) {
            arrayList.add("GET_PANEL_INFO");
            i2 |= 220;
        }
        if ((i & 255) == 255) {
            arrayList.add("TYPE_MAX_V1_1");
            i2 |= 255;
        }
        if ((i & 145) == 145) {
            arrayList.add("SDR2HDR_AI_ENALE");
            i2 |= 145;
        }
        if ((i & 146) == 146) {
            arrayList.add("SDR2HDR_AI_INPUT_AMBIENTLIGHT");
            i2 |= 146;
        }
        if ((i & 148) == 148) {
            arrayList.add("SDR2HDR_AI_INPUT_BACKLIGHT");
            i2 |= 148;
        }
        if ((i & 155) == 155) {
            arrayList.add("SDR2HDR_LCE");
            i2 |= 155;
        }
        if ((i & 156) == 156) {
            arrayList.add("SDR2HDR_DE");
            i2 |= 156;
        }
        if ((i & 157) == 157) {
            arrayList.add("SDR2HDR_TF_COEF");
            i2 |= 157;
        }
        if ((i & 158) == 158) {
            arrayList.add("SDR2HDR_FTC");
            i2 |= 158;
        }
        if ((i & 185) == 185) {
            arrayList.add("SDR2HDR_AI_TM");
            i2 |= 185;
        }
        if ((i & 186) == 186) {
            arrayList.add("SDR2HDR_AI_LCE");
            i2 |= 186;
        }
        if ((i & 187) == 187) {
            arrayList.add("SDR2HDR_AI_DE");
            i2 |= 187;
        }
        if ((i & 188) == 188) {
            arrayList.add("SDR2HDR_AI_GRAPHIC");
            i2 |= 188;
        }
        if ((i & 197) == 197) {
            arrayList.add("SDR2HDR_UPDATE");
            i2 |= 197;
        }
        if ((i & 768) == 768) {
            arrayList.add("TYPE_MIN_V1_2");
            i2 |= 768;
        }
        if ((i & 1023) == 1023) {
            arrayList.add("TYPE_MAX_V1_2");
            i2 |= 1023;
        }
        if (i != i2) {
            arrayList.add("0x" + Integer.toHexString(i & (~i2)));
        }
        return String.join(" | ", arrayList);
    }
}
