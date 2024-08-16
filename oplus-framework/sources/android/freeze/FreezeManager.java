package android.freeze;

import android.content.Context;
import android.content.pm.OplusPackageManager;
import android.os.Process;
import android.os.UserHandle;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;
import java.util.List;

/* loaded from: classes.dex */
public class FreezeManager {
    public static final int FREEZE_SETTING_CAN_REEZE = 1;
    public static final int FREEZE_SETTING_NOMAL = 0;
    public static final int FREEZE_SETTING_UNKNOW = -1;
    public static final int FREEZE_STATE_FREEZED = 1;
    public static final int FREEZE_STATE_NOMAL = 0;
    public static final int FREEZE_STATE_UNKNOW = -1;
    private static final String TAG = "FreezeManager";
    private static volatile FreezeManager sFreezeManager;
    private static volatile Boolean sFreezeSupport = null;
    private OplusPackageManager mOplusPackageManager = new OplusPackageManager();

    private FreezeManager() {
    }

    public static FreezeManager getInstance() {
        if (sFreezeManager == null) {
            synchronized (FreezeManager.class) {
                if (sFreezeManager == null) {
                    sFreezeManager = new FreezeManager();
                }
            }
        }
        return sFreezeManager;
    }

    public static boolean isFreezeSupport(Context context) {
        if (sFreezeSupport == null) {
            synchronized (FreezeManager.class) {
                if (sFreezeSupport == null) {
                    boolean support = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_FORWARDLY_FREEZE);
                    sFreezeSupport = Boolean.valueOf(support);
                }
            }
        }
        return sFreezeSupport.booleanValue();
    }

    public boolean isFreezeEnabled() {
        return this.mOplusPackageManager.isFreezeEnabled();
    }

    public void setFreezeEnable(boolean enabled) {
        this.mOplusPackageManager.setFreezeEnable(enabled);
    }

    public int getPackageFreezeState(String pkg) {
        UserHandle userHandle = Process.myUserHandle();
        return getPackageFreezeState(pkg, userHandle);
    }

    public int getPackageFreezeState(String pkg, UserHandle userHandle) {
        if (userHandle == null) {
            userHandle = Process.myUserHandle();
        }
        return this.mOplusPackageManager.getPackageFreezeState(pkg, userHandle);
    }

    public int getPackageFreezeUserSetting(String pkg) {
        UserHandle userHandle = Process.myUserHandle();
        return getPackageFreezeUserSetting(pkg, userHandle);
    }

    public int getPackageFreezeUserSetting(String pkg, UserHandle userHandle) {
        if (userHandle == null) {
            userHandle = Process.myUserHandle();
        }
        return this.mOplusPackageManager.getPackageFreezeUserSetting(pkg, userHandle);
    }

    public void setPackageFreezeState(String pkg, int state) {
        UserHandle userHandle = Process.myUserHandle();
        setPackageFreezeState(pkg, state, userHandle);
    }

    public void setPackageFreezeState(String pkg, int state, UserHandle userHandle) {
        if (userHandle == null) {
            userHandle = Process.myUserHandle();
        }
        this.mOplusPackageManager.setPackageFreezeState(pkg, state, userHandle);
    }

    public void setPackageFreezeUserSetting(String pkg, int setting) {
        UserHandle userHandle = Process.myUserHandle();
        setPackageFreezeUserSetting(pkg, setting, userHandle);
    }

    public void setPackageFreezeUserSetting(String pkg, int setting, UserHandle userHandle) {
        if (userHandle == null) {
            userHandle = Process.myUserHandle();
        }
        this.mOplusPackageManager.setPackageFreezeUserSetting(pkg, setting, userHandle);
    }

    public List<String> getFreezedApplicationList(UserHandle userHandle) {
        if (userHandle == null) {
            userHandle = Process.myUserHandle();
        }
        return this.mOplusPackageManager.getFreezedApplicationList(userHandle);
    }

    public List<String> getUserSettingFreezeableApplicationList(UserHandle userHandle) {
        if (userHandle == null) {
            userHandle = Process.myUserHandle();
        }
        return this.mOplusPackageManager.getUserSettingFreezeableApplicationList(userHandle);
    }
}
