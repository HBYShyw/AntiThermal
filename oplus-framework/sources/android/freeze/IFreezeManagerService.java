package android.freeze;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.os.UserHandle;
import java.util.List;

/* loaded from: classes.dex */
public interface IFreezeManagerService extends IOplusCommonFeature {
    public static final IFreezeManagerService DEFAULT = new IFreezeManagerService() { // from class: android.freeze.IFreezeManagerService.1
    };

    default IFreezeManagerService getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IFreezeManagerService;
    }

    default boolean isFreezeEnabled() {
        return false;
    }

    default void setFreezeEnable(boolean enabled) {
    }

    default int getPackageFreezeState(String pkg, UserHandle userHandle) {
        return -1;
    }

    default int getPackageFreezeUserSetting(String pkg, UserHandle userHandle) {
        return -1;
    }

    default void setPackageFreezeState(String pkg, int state, UserHandle userHandle) {
    }

    default void setPackageFreezeUserSetting(String pkg, int setting, UserHandle userHandle) {
    }

    default List<String> getFreezedApplicationList(UserHandle userHandle) {
        return null;
    }

    default List<String> getUserSettingFreezeableApplicationList(UserHandle userHandle) {
        return null;
    }
}
