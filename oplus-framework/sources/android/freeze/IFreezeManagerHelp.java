package android.freeze;

import android.common.IOplusCommonFeature;
import android.common.OplusFeatureList;
import android.content.Context;
import android.os.UserHandle;

/* loaded from: classes.dex */
public interface IFreezeManagerHelp extends IOplusCommonFeature {
    public static final IFreezeManagerHelp DEFAULT = new IFreezeManagerHelp() { // from class: android.freeze.IFreezeManagerHelp.1
    };

    default IFreezeManagerHelp getDefault() {
        return DEFAULT;
    }

    default OplusFeatureList.OplusIndex index() {
        return OplusFeatureList.OplusIndex.IFreezeManagerHelp;
    }

    default boolean isFreezeSupport(Context context) {
        return false;
    }

    default boolean handleStartForUid(Context context, String pkg, int callerUid, int uid) {
        return handleStartForUserId(context, pkg, callerUid, UserHandle.getUserId(uid));
    }

    default void handleRemoveTask(Context context, boolean killProcess, boolean removeFromRecents, String pkg, int userId) {
    }

    default boolean handleStartForUserId(Context context, String pkg, int callerUid, int userId) {
        return false;
    }

    default boolean handleStartProcForUserId(Context context, String pkg, int userId) {
        return false;
    }

    default boolean handleActivityStart(Context context, String pkg, String callingPackage, int uid) {
        return false;
    }
}
