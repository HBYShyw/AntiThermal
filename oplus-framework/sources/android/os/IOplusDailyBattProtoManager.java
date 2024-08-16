package android.os;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.util.Slog;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public interface IOplusDailyBattProtoManager extends IOplusCommonFeature {
    public static final IOplusDailyBattProtoManager DEFAULT = new IOplusDailyBattProtoManager() { // from class: android.os.IOplusDailyBattProtoManager.1
    };
    public static final int STEP_LEVEL_MODE_NETWORK_SHIFT = 4;
    public static final int STEP_LEVEL_MODE_NETWORK_STATE = 240;
    public static final String TAG = "IOplusDailyBattProtoManager";
    public static final int TYPE_NETWORK_DATA_NR = 3;
    public static final int TYPE_NETWORK_DATA_REST = 2;
    public static final int TYPE_NETWORK_OFF = 0;
    public static final int TYPE_NETWORK_OFF_FROM_DATA_NR = 6;
    public static final int TYPE_NETWORK_OFF_FROM_DATA_REST = 5;
    public static final int TYPE_NETWORK_OFF_FROM_WIFI = 4;
    public static final int TYPE_NETWORK_WIFI = 1;
    public static final int VALUE_FOUR = 4;
    public static final int VALUE_SIXTEEN = 16;

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusDailyBattProtoManager;
    }

    default IOplusDailyBattProtoManager getDefault() {
        return DEFAULT;
    }

    default void init(Context context) {
        Slog.d(TAG, "default init");
    }

    default void registerRomUpdate() {
        Slog.d(TAG, "default registerRomUpdate");
    }

    default void reportDailyProto() {
        Slog.d(TAG, "default reportDailyProto");
    }

    static boolean ifHaveNetworkDetail(PrintWriter pw, boolean haveModes, long initMode, long modMode) {
        if ((modMode >> 4) == 0) {
            pw.print(haveModes ? ", " : " (");
            int networkType = (int) (initMode >> 4);
            String networkStr = "NA";
            switch (networkType) {
                case 0:
                    networkStr = "network-off";
                    break;
                case 1:
                    networkStr = "network-wifi";
                    break;
                case 2:
                    networkStr = "network-data-rest";
                    break;
                case 3:
                    networkStr = "network-data-nr";
                    break;
                case 4:
                    networkStr = "network-off-from-wifi";
                    break;
                case 5:
                    networkStr = "network-off-from-data-rest";
                    break;
                case 6:
                    networkStr = "network-off-from-data-nr";
                    break;
            }
            pw.print(networkStr);
            return true;
        }
        return haveModes;
    }

    default int[] noteConnectivityChangedLocked(int type, String extra, int phoneDataConnectionType, int modStepMode, int curStepMode) {
        Slog.d(TAG, "default noteConnectivityChangedLocked");
        return new int[]{modStepMode, curStepMode};
    }

    default int[] notePhoneDataConnectionStateLocked(int dataType, boolean hasData, int bin, int modStepMode, int curStepMode) {
        Slog.d(TAG, "default notePhoneDataConnectionStateLocked");
        return new int[]{modStepMode, curStepMode};
    }
}
