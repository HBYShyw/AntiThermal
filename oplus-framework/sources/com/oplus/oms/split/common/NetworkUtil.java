package com.oplus.oms.split.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

/* loaded from: classes.dex */
public class NetworkUtil {
    public static final int CLOSE = 3;
    private static final int DEFAULT_UPDATE_TIME = 72;
    public static final int NETWORK_NORMAL = 1;
    private static final String TAG = "NetworkUtil";
    public static final int WIFI_ONLY = 2;
    private static int sNetWorkType = 0;
    private static int sUpdateTimeByHours = 72;

    private NetworkUtil() {
    }

    public static boolean isWlanConnected(ConnectivityManager connectivityManager) {
        Network network;
        NetworkCapabilities networkCapabilities;
        if (connectivityManager == null || (network = connectivityManager.getActiveNetwork()) == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(network)) == null) {
            return false;
        }
        boolean isNetworkConnected = networkCapabilities.hasTransport(1);
        boolean isAvailable = networkCapabilities.hasCapability(16);
        if (!isNetworkConnected || !isAvailable) {
            return false;
        }
        return true;
    }

    public static boolean isDataConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return isDataConnected(connectivityManager);
    }

    public static boolean isDataConnected(ConnectivityManager connectivityManager) {
        Network network;
        NetworkCapabilities networkCapabilities;
        if (connectivityManager == null || (network = connectivityManager.getActiveNetwork()) == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(network)) == null) {
            return false;
        }
        boolean isNetworkConnected = networkCapabilities.hasTransport(0);
        boolean isAvailable = networkCapabilities.hasCapability(16);
        return isNetworkConnected && isAvailable;
    }

    public static boolean isAllowUseNet(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        int networkType = getSplitLoadStrategy();
        if (networkType == 1) {
            if (!isWlanConnected(mConnectivityManager) && !isDataConnected(mConnectivityManager)) {
                return false;
            }
            return true;
        }
        if (networkType != 2) {
            return false;
        }
        return isWlanConnected(mConnectivityManager);
    }

    public static void setNetWorkStrategy(int networkType) {
        if (networkType != 1 && networkType != 2 && networkType != 3) {
            sNetWorkType = 3;
            SplitLog.w(TAG, "networkType is" + networkType + ", the setting parameter is wrong. take the default 3", new Object[0]);
        } else {
            sNetWorkType = networkType;
        }
    }

    public static int getSplitLoadStrategy() {
        return sNetWorkType;
    }

    public static void setNetworkUpdateTime(int time) {
        if (time >= 0) {
            sUpdateTimeByHours = time;
        } else {
            SplitLog.w(TAG, "UpdateTimeByHours is setting " + time + ", the setting parameter is wrong. take the default 72", new Object[0]);
            sUpdateTimeByHours = 72;
        }
    }

    public static int getUpdateTimeByHours() {
        return sUpdateTimeByHours;
    }
}
