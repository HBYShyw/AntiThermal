package android.os.customize;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.ComponentName;
import java.util.List;

/* loaded from: classes.dex */
public interface IOplusCustomizeVpnManager extends IOplusCommonFeature {
    public static final IOplusCustomizeVpnManager DEFAULT = new IOplusCustomizeVpnManager() { // from class: android.os.customize.IOplusCustomizeVpnManager.1
    };

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IOplusCustomizeVpnManager;
    }

    default IOplusCustomizeVpnManager getDefault() {
        return DEFAULT;
    }

    default int getVpnServiceState() {
        return 0;
    }

    default void setVpnDisabled(ComponentName admin, boolean disabled) {
    }

    default boolean isVpnDisabled(ComponentName admin) {
        return false;
    }

    default void setAlwaysOnVpnPackage(ComponentName admin, String vpnPackage, boolean lockdown) {
    }

    default List<String> getVpnList(ComponentName componentName) {
        return null;
    }

    default boolean deleteVpnProfile(ComponentName componentName, String key) {
        return false;
    }

    default int disestablishVpnConnection(ComponentName admin) {
        return -1;
    }

    default String getAlwaysOnVpnPackage(ComponentName admin) {
        return null;
    }

    default void setVpnAlwaysOnPersis(boolean lockdown) {
    }

    default boolean getVpnAlwaysOnPersis(String defval) {
        return false;
    }
}
