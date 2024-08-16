package android.freeze;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.util.Log;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import java.util.List;

/* loaded from: classes.dex */
public class FreezeManagerHelp implements IFreezeManagerHelp {
    private static final String TAG = "FreezeManagerHelp";
    private static volatile Boolean sFreezeSupport = null;
    private static volatile FreezeManagerHelp sInstance;

    private FreezeManagerHelp() {
    }

    public static FreezeManagerHelp getInstance() {
        if (sInstance == null) {
            synchronized (FreezeManagerHelp.class) {
                if (sInstance == null) {
                    sInstance = new FreezeManagerHelp();
                }
            }
        }
        return sInstance;
    }

    @Override // android.freeze.IFreezeManagerHelp
    public boolean isFreezeSupport(Context context) {
        if (sFreezeSupport == null) {
            synchronized (FreezeManager.class) {
                if (sFreezeSupport == null) {
                    sFreezeSupport = Boolean.valueOf(OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FORWARDLY_FREEZE));
                }
            }
        }
        return sFreezeSupport.booleanValue();
    }

    @Override // android.freeze.IFreezeManagerHelp
    public boolean handleStartForUid(Context context, String pkg, int callerUid, int uid) {
        return handleStartForUserId(context, pkg, callerUid, UserHandle.getUserId(uid));
    }

    @Override // android.freeze.IFreezeManagerHelp
    public void handleRemoveTask(Context context, boolean killProcess, boolean removeFromRecents, String pkg, int userId) {
        if (pkg == null || !killProcess || !removeFromRecents || !isFreezeSupport(context)) {
            return;
        }
        boolean canFreeze = FreezeManager.getInstance().getPackageFreezeUserSetting(pkg, UserHandle.of(userId)) == 1;
        if (canFreeze) {
            Log.d(TAG, "handleRemoveTask : pkg" + pkg + userId);
            FreezeManager.getInstance().setPackageFreezeState(pkg, 1, UserHandle.of(userId));
        }
    }

    @Override // android.freeze.IFreezeManagerHelp
    public boolean handleStartForUserId(Context context, String pkg, int callerUid, int userId) {
        boolean handle = FreezeManager.getInstance().getPackageFreezeState(pkg, UserHandle.of(userId)) == 1;
        if (handle) {
            Log.d(TAG, "handleStartForUserId : pkg" + pkg + userId);
            if (isFromPendingIntent(context, callerUid, pkg)) {
                Log.d(TAG, "handleStartForUserId : pkg" + pkg + userId + "isFromPendingIntent");
                FreezeManager.getInstance().setPackageFreezeState(pkg, 0, UserHandle.of(userId));
                return false;
            }
            return handle;
        }
        return handle;
    }

    @Override // android.freeze.IFreezeManagerHelp
    public boolean handleStartProcForUserId(Context context, String pkg, int userId) {
        boolean handle = FreezeManager.getInstance().getPackageFreezeState(pkg, UserHandle.of(userId)) == 1;
        if (handle) {
            Log.d(TAG, "handleStartProcForUserId : pkg" + pkg + userId);
        }
        return handle;
    }

    private boolean isCallerFromLauncher(Context context, int uid) {
        String[] pkgs = context.getPackageManager().getPackagesForUid(uid);
        if (pkgs != null && pkgs.length > 0) {
            Intent homeIntent = new Intent("android.intent.action.MAIN");
            homeIntent.addCategory("android.intent.category.HOME");
            List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(homeIntent, 65536);
            if (resolveInfos != null) {
                for (ResolveInfo ri : resolveInfos) {
                    for (String pkg : pkgs) {
                        if (ri.activityInfo.packageName.equals(pkg)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isFromPendingIntent(Context context, int uid, String toPackage) {
        String[] pkgs = context.getPackageManager().getPackagesForUid(uid);
        if (pkgs != null && pkgs.length > 0) {
            for (String pkg : pkgs) {
                if (pkg.equals(toPackage)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // android.freeze.IFreezeManagerHelp
    public boolean handleActivityStart(Context context, String pkg, String callingPackage, int uid) {
        int state = FreezeManager.getInstance().getPackageFreezeState(pkg, UserHandle.getUserHandleForUid(uid));
        if (state == 1) {
            Log.d(TAG, "handleActivityStart : pkg" + pkg);
            FreezeManager.getInstance().setPackageFreezeState(pkg, 0, UserHandle.getUserHandleForUid(uid));
        }
        return false;
    }
}
