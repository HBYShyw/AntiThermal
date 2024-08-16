package com.oplus.network;

/* loaded from: classes.dex */
public class OlkUtils {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static int getCapId(String function) {
        char c;
        switch (function.hashCode()) {
            case -2141088032:
                if (function.equals(OlkConstants.FUN_SET_AP_STATE)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -2122530163:
                if (function.equals(OlkConstants.FUN_SET_REALTIME_EVENT)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -2036270469:
                if (function.equals(OlkConstants.FUN_DISABLE_QOE_MONITOR)) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -1848563983:
                if (function.equals(OlkConstants.FUN_GET_DUALSTA_ENABLE)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -1561659207:
                if (function.equals(OlkConstants.FUN_SEND_ABR_CHANGE)) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case -1402857987:
                if (function.equals(OlkConstants.FUN_RELEASE_DUALSTA)) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1344496665:
                if (function.equals(OlkConstants.FUN_GET_NETWORK_QUALITY)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1175378762:
                if (function.equals(OlkConstants.FUN_UPDATE_CELLULAR_ENABLE)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1128941738:
                if (function.equals(OlkConstants.FUN_ENABLE_QOE_MONITOR)) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -1119104128:
                if (function.equals(OlkConstants.FUN_SET_AP_Bandwidth)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -902595776:
                if (function.equals(OlkConstants.FUN_REQUEST_DUALPS)) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case -896380724:
                if (function.equals(OlkConstants.FUN_IS_PRIMARY_CELLULAR)) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case -419603800:
                if (function.equals(OlkConstants.FUN_SEND_SCORE_CHANGE)) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -388648271:
                if (function.equals(OlkConstants.FUN_GET_L2_PARAM)) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case -288435740:
                if (function.equals(OlkConstants.FUN_CLEAR_SOCKET_PRIORITY)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -113333042:
                if (function.equals(OlkConstants.FUN_GET_OROUSTBOOST_STATE)) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 142402165:
                if (function.equals(OlkConstants.FUN_REQUEST_DUALSTA)) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 463998777:
                if (function.equals(OlkConstants.FUN_SET_SOCKET_PRIORITY)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 645261512:
                if (function.equals(OlkConstants.FUN_RELEASE_DUALPS)) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 815102546:
                if (function.equals(OlkConstants.FUN_GET_ULL_STATE)) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 1001717667:
                if (function.equals(OlkConstants.FUN_GET_APP_DENY_FLAG)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 1096041592:
                if (function.equals(OlkConstants.FUN_GET_DUALPS_ENABLE)) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 2017459373:
                if (function.equals(OlkConstants.FUN_IS_PRIMARY_WIFI)) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 2018579846:
                if (function.equals(OlkConstants.FUN_GET_DUALSTA_ACTIVE_STATUS)) {
                    c = 11;
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
                return 1;
            case 6:
            case 7:
                return 2;
            case '\b':
            case '\t':
            case '\n':
            case 11:
            case '\f':
                return 4;
            case '\r':
            case 14:
            case 15:
            case 16:
            case 17:
                return 5;
            case 18:
            case 19:
                return 6;
            case 20:
            case 21:
            case 22:
            case 23:
                return 7;
            default:
                return -1;
        }
    }

    public static int getErrorId(int errorCode) {
        switch (errorCode) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 100:
                return 2;
            case 200:
                return 3;
            case 300:
                return 4;
            case 400:
                return 5;
            case 500:
                return 6;
            case 501:
                return 7;
            case 1000:
                return 8;
            case OlkConstants.DATA_LIMIT_ERROR /* 1100 */:
                return 9;
            case OlkConstants.LOW_LATENCY_ERROR /* 1200 */:
                return 10;
            default:
                return 11;
        }
    }
}
