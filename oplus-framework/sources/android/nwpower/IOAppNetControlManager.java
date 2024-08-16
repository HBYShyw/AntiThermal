package android.nwpower;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import java.util.List;

/* loaded from: classes.dex */
public interface IOAppNetControlManager extends IOplusCommonFeature {
    public static final IOAppNetControlManager DEFAULT = new IOAppNetControlManager() { // from class: android.nwpower.IOAppNetControlManager.1
    };
    public static final String NAME = "IOAppNetControlManager";

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOAppNetControlManager;
    }

    default IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean getStaticOAppNetControlEnable() {
        return false;
    }

    default void setFirewall(int uid, boolean allow) {
    }

    default void setFirewallWithArgs(int uid, boolean allow, int netrestore, int scenes) {
    }

    default void destroySocket(int uid) {
    }

    default void legacyDestroySocket() {
    }

    default long[] requestAppFireWallHistoryStamp(int uid) {
        return new long[]{0, 0, 0};
    }

    default int networkDisableWhiteList(List<String> appConfigUids, int enable) {
        return 0;
    }

    default void destroySocketForProc(int uid, int pid) {
    }

    default void notifyUnFreezeForProc(int uid, int pid) {
    }
}
