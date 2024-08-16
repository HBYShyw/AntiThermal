package com.android.internal.telephony.nrNetwork;

import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.telephony.nrNetwork.OplusNrModeConstant;

/* loaded from: classes.dex */
public class OplusNrUtils {
    private static String TAG = "OplusNrModeUtils";

    public static int getModeWithSa(int mode) {
        int ret = mode;
        switch (mode) {
            case 0:
            case 1:
                ret = 0;
                break;
            case 2:
                ret = 2;
                break;
            case 3:
                ret = 3;
                break;
        }
        logd("getModeWithSa : mode = " + mode + " , ret = " + ret);
        return ret;
    }

    public static int getModeWithoutSa(int mode) {
        int ret = mode;
        switch (mode) {
            case 0:
            case 1:
            case 2:
            case 3:
                ret = 1;
                break;
        }
        logd("getModeWithoutSa : mode = " + mode + " , ret = " + ret);
        return ret;
    }

    public static boolean isSaModeEnabled(int mode) {
        return mode == 0 || mode == 2 || mode == 3;
    }

    public static boolean isSaPrefered(int mode) {
        return mode == 2 || mode == 3;
    }

    public static boolean isNsaPrefered(int mode) {
        return mode == 0 || mode == 1;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static OplusNrModeConstant.SimType getSimTypeForKey(String key) {
        char c;
        OplusNrModeConstant.SimType simType = OplusNrModeConstant.SimType.SIM_TYPE_OTHER;
        switch (key.hashCode()) {
            case -2098790669:
                if (key.equals(OplusNrModeConstant.KEY_CT_SA_PRO_LIST)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1540936291:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CB)) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -1540936273:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CT)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1540936272:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CU)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1046896402:
                if (key.equals(OplusNrModeConstant.KEY_CU_SA_CITY_LIST)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -541337777:
                if (key.equals(OplusNrModeConstant.KEY_CT_SA_CITY_LIST)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 483247638:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_SA_CITY_LIST)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 705207116:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_SA_PRO_LIST)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 923955208:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CMCC)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1197180542:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CMCC)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1838115219:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CB)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1838115237:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CT)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1838115238:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CU)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1902773620:
                if (key.equals(OplusNrModeConstant.KEY_CU_SA_PRO_LIST)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CMCC;
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CU;
                break;
            case '\b':
            case '\t':
            case '\n':
            case 11:
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CT;
                break;
            case '\f':
            case '\r':
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CB;
                break;
        }
        logd("getSimTypeForKey : key = " + key + " ,simType = " + simType);
        return simType;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static OplusNrModeConstant.CfgType getCfgTypeForKey(String key) {
        char c;
        OplusNrModeConstant.CfgType cfgType = OplusNrModeConstant.CfgType.NONE;
        switch (key.hashCode()) {
            case -2098790669:
                if (key.equals(OplusNrModeConstant.KEY_CT_SA_PRO_LIST)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -1540936291:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CB)) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -1540936273:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CT)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case -1540936272:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CU)) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case -1046896402:
                if (key.equals(OplusNrModeConstant.KEY_CU_SA_CITY_LIST)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -541337777:
                if (key.equals(OplusNrModeConstant.KEY_CT_SA_CITY_LIST)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 483247638:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_SA_CITY_LIST)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 705207116:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_SA_PRO_LIST)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 923955208:
                if (key.equals(OplusVoNrConstant.VONR_PROVINCES_CMCC)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case 1197180542:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CMCC)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1838115219:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CB)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 1838115237:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CT)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1838115238:
                if (key.equals(OplusVoNrConstant.VONR_CITIES_CU)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1902773620:
                if (key.equals(OplusNrModeConstant.KEY_CU_SA_PRO_LIST)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                cfgType = OplusNrModeConstant.CfgType.CITY;
                break;
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
                cfgType = OplusNrModeConstant.CfgType.PROVINCE;
                break;
        }
        logd("getCfgTypeForKey : key = " + key + " ,cfgType = " + cfgType);
        return cfgType;
    }

    public static boolean isAutoNrMode(int mode) {
        return mode == 4;
    }

    public static boolean isValidNrMode(int mode) {
        return mode > -1 && mode <= 4;
    }

    public static OplusNrModeConstant.SimType getSimType(String plmn) {
        OplusNrModeConstant.SimType simType = OplusNrModeConstant.SimType.SIM_TYPE_OTHER;
        if (!TextUtils.isEmpty(plmn)) {
            if (plmn.equals("00101")) {
                simType = OplusNrModeConstant.SimType.SIM_TYPE_TEST;
            } else if (plmn.equals("46000") || plmn.equals("46002") || plmn.equals("46004") || plmn.equals("46007") || plmn.equals("46008")) {
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CMCC;
            } else if (plmn.equals("46001") || plmn.equals("46006") || plmn.equals("46009") || plmn.equals("46030")) {
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CU;
            } else if (plmn.equals("46003") || plmn.equals("46011")) {
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CT;
            } else if (plmn.equals("46015")) {
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CB;
            } else if (plmn.equals("405854") || plmn.equals("405855") || plmn.equals("405856") || plmn.equals("405872") || plmn.equals("405857") || plmn.equals("405858") || plmn.equals("405859") || plmn.equals("405860") || plmn.equals("405861") || plmn.equals("405862") || plmn.equals("405873") || plmn.equals("405863") || plmn.equals("405864") || plmn.equals("405874") || plmn.equals("405865") || plmn.equals("405866") || plmn.equals("405867") || plmn.equals("405868") || plmn.equals("405869") || plmn.equals("405871") || plmn.equals("405870") || plmn.equals("405840")) {
                simType = OplusNrModeConstant.SimType.SIM_TYPE_JIO;
            }
        }
        logd("getSimType: SIM_TYPE = " + simType);
        return simType;
    }

    public static int converMtkModeToAp(int nrMode) {
        int mode = nrMode;
        switch (mode) {
            case 3:
                mode = 2;
                break;
            case 5:
                mode = 1;
                break;
            case 7:
                mode = 0;
                break;
        }
        logd("converMtkMode: from nrMode = " + nrMode + " ,to mode = " + mode);
        return mode;
    }

    public static int coverApToMtkMode(int mode) {
        int nrMode = mode;
        switch (nrMode) {
            case 0:
            case 3:
                nrMode = 7;
                break;
            case 1:
                nrMode = 5;
                break;
            case 2:
                nrMode = 3;
                break;
        }
        logd("coverApToMtkMode : from mode = " + mode + " ,to nrMode = " + nrMode);
        return nrMode;
    }

    public static String stringToUnicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            try {
                char c = string.charAt(i);
                unicode.append("\\u" + Integer.toHexString(c));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return unicode.toString();
    }

    public static boolean isChinese(String data) {
        return data.matches("^[一-龥]+$");
    }

    public static String[] coverStringToArray(String str) {
        if (str != null && str.length() > 0) {
            String[] tmp = str.split(",");
            return tmp;
        }
        return null;
    }

    public static int getPhoneIdForSubSlot(int ddsSlot) {
        return ddsSlot == 0 ? 1 : 0;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int i = str.length();
        do {
            i--;
            if (i < 0) {
                if (str.isEmpty()) {
                    return false;
                }
                return true;
            }
        } while (Character.isDigit(str.charAt(i)));
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static OplusNrModeConstant.CfgType getLocTypeForKey(String key) {
        char c;
        OplusNrModeConstant.CfgType type = OplusNrModeConstant.CfgType.NONE;
        switch (key.hashCode()) {
            case -694032220:
                if (key.equals(OplusNrModeConstant.KEY_CT_DIS_SMARTSA_CITY_LIST)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -637483203:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_DIS_SMARTSA_CITY_LIST)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 55074845:
                if (key.equals(OplusNrModeConstant.KEY_CU_DIS_SMARTSA_PRO_LIST)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 251588350:
                if (key.equals(OplusNrModeConstant.KEY_CT_DIS_SMARTSA_PRO_LIST)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1500338501:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_DIS_SMARTSA_PRO_LIST)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1803983717:
                if (key.equals(OplusNrModeConstant.KEY_CU_DIS_SMARTSA_CITY_LIST)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                type = OplusNrModeConstant.CfgType.CITY;
                break;
            case 3:
            case 4:
            case 5:
                type = OplusNrModeConstant.CfgType.PROVINCE;
                break;
        }
        logd("getLocTypeForKey : key = " + key + " ,locType = " + type);
        return type;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static OplusNrModeConstant.SimType getSimTypeForLocKey(String key) {
        char c;
        OplusNrModeConstant.SimType simType = OplusNrModeConstant.SimType.SIM_TYPE_OTHER;
        switch (key.hashCode()) {
            case -694032220:
                if (key.equals(OplusNrModeConstant.KEY_CT_DIS_SMARTSA_CITY_LIST)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -637483203:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_DIS_SMARTSA_CITY_LIST)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 55074845:
                if (key.equals(OplusNrModeConstant.KEY_CU_DIS_SMARTSA_PRO_LIST)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 251588350:
                if (key.equals(OplusNrModeConstant.KEY_CT_DIS_SMARTSA_PRO_LIST)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1500338501:
                if (key.equals(OplusNrModeConstant.KEY_CMCC_DIS_SMARTSA_PRO_LIST)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 1803983717:
                if (key.equals(OplusNrModeConstant.KEY_CU_DIS_SMARTSA_CITY_LIST)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
            case 1:
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CMCC;
                break;
            case 2:
            case 3:
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CU;
                break;
            case 4:
            case 5:
                simType = OplusNrModeConstant.SimType.SIM_TYPE_CT;
                break;
        }
        logd("getSimTypeForKey : key = " + key + " ,simType = " + simType);
        return simType;
    }

    public static int getSubIdForPhone(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length < 1) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    public static String getNrModeString(int nrMode) {
        switch (nrMode) {
            case 0:
                return "NSA_PRE";
            case 1:
                return "NSA_ONLY";
            case 2:
                return "SA_ONLY";
            case 3:
                return "SA_PRE";
            default:
                return "INVALID";
        }
    }

    public static void logd(String msg) {
        Rlog.d(TAG, msg);
    }
}
