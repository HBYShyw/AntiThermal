package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.os.customize.IOplusCustomizeSettingsManagerService;
import android.util.Slog;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeSettingsManager {
    private static final String SERVICE_NAME = "OplusCustomizeSettingsManagerService";
    private static final String TAG = "OplusCustomizeSettingsManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeSettingsManager sInstance;
    private IOplusCustomizeSettingsManagerService mOplusCustomizeSettingsManagerService;

    private OplusCustomizeSettingsManager() {
        getOplusCustomizeSettingsManagerService();
    }

    public static final OplusCustomizeSettingsManager getInstance(Context context) {
        OplusCustomizeSettingsManager oplusCustomizeSettingsManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeSettingsManager();
                }
                oplusCustomizeSettingsManager = sInstance;
            }
            return oplusCustomizeSettingsManager;
        }
        return sInstance;
    }

    private IOplusCustomizeSettingsManagerService getOplusCustomizeSettingsManagerService() {
        IOplusCustomizeSettingsManagerService iOplusCustomizeSettingsManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeSettingsManagerService == null) {
                this.mOplusCustomizeSettingsManagerService = IOplusCustomizeSettingsManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            iOplusCustomizeSettingsManagerService = this.mOplusCustomizeSettingsManagerService;
        }
        return iOplusCustomizeSettingsManagerService;
    }

    public boolean turnOnProtectEyes(ComponentName componentName, boolean on) {
        try {
            return getOplusCustomizeSettingsManagerService().turnOnProtectEyes(componentName, on);
        } catch (Exception e) {
            Slog.e(TAG, "turnOnProtectEyes fail!", e);
            return false;
        }
    }

    public boolean isProtectEyesOn(ComponentName componentName) {
        try {
            return getOplusCustomizeSettingsManagerService().isProtectEyesOn(componentName);
        } catch (Exception e) {
            Slog.e(TAG, "isProtectEyesOn fail!", e);
            return false;
        }
    }

    public boolean setSIMLockDisabled(ComponentName componentName, boolean disabled) {
        try {
            return getOplusCustomizeSettingsManagerService().setSIMLockDisabled(componentName, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSIMLockDisabled fail!", e);
            return false;
        }
    }

    public boolean isSIMLockDisabled(ComponentName componentName) {
        try {
            return getOplusCustomizeSettingsManagerService().isSIMLockDisabled(componentName);
        } catch (Exception e) {
            Slog.e(TAG, "isSIMLockDisabled fail!", e);
            return false;
        }
    }

    public void setTopWatermarkEnable(String displayText) {
        try {
            getOplusCustomizeSettingsManagerService().setTopWatermarkEnable(displayText);
        } catch (Exception e) {
            Slog.e(TAG, "setTopWatermarkEnable fail!", e);
        }
    }

    public void setTopWatermarkDisable() {
        try {
            getOplusCustomizeSettingsManagerService().setTopWatermarkDisable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getTopWatermark() {
        try {
            return getOplusCustomizeSettingsManagerService().getTopWatermark();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setBackupRestoreDisabled(ComponentName componentName, boolean disabled) {
        try {
            return getOplusCustomizeSettingsManagerService().setBackupRestoreDisabled(componentName, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setBackupRestoreDisabled fail!", e);
            return false;
        }
    }

    public boolean isBackupRestoreDisabled(ComponentName componentName) {
        try {
            return getOplusCustomizeSettingsManagerService().isBackupRestoreDisabled(componentName);
        } catch (Exception e) {
            Slog.e(TAG, "isBackupRestoreDisabled fail!", e);
            return false;
        }
    }

    public boolean setAutoScreenOffTime(ComponentName componentName, long millis) {
        try {
            return getOplusCustomizeSettingsManagerService().setAutoScreenOffTime(componentName, millis);
        } catch (Exception e) {
            Slog.i(TAG, "setAutoScreenOffTime error!" + e);
            return false;
        }
    }

    public long getAutoScreenOffTime(ComponentName componentName) {
        try {
            return getOplusCustomizeSettingsManagerService().getAutoScreenOffTime(componentName);
        } catch (Exception e) {
            Slog.i(TAG, "getAutoScreenOffTime error!" + e);
            return 0L;
        }
    }

    public boolean isScreenOffTimeSetByPolicy(ComponentName componentName) {
        try {
            return getOplusCustomizeSettingsManagerService().isScreenOffTimeSetByPolicy(componentName);
        } catch (Exception e) {
            Slog.i(TAG, "getAutoScreenOffTime error!" + e);
            return false;
        }
    }

    public void storeLastManualScreenOffTimeout(ComponentName componentName, int value) {
        try {
            getOplusCustomizeSettingsManagerService().storeLastManualScreenOffTimeout(componentName, value);
        } catch (Exception e) {
            Slog.i(TAG, "getAutoScreenOffTime error!" + e);
        }
    }

    public boolean setSearchIndexDisabled(ComponentName componentName, boolean disabled) {
        try {
            return getOplusCustomizeSettingsManagerService().setSearchIndexDisabled(componentName, disabled);
        } catch (Exception e) {
            Slog.e(TAG, "setSearchIndexDisabled error!", e);
            return false;
        }
    }

    public boolean isSearchIndexDisabled(ComponentName componentName) {
        try {
            return getOplusCustomizeSettingsManagerService().isSearchIndexDisabled(componentName);
        } catch (Exception e) {
            Slog.e(TAG, "isSearchIndexDisabled error!", e);
            return false;
        }
    }

    public String getRomVersion(ComponentName componentName) {
        try {
            return getOplusCustomizeSettingsManagerService().getRomVersion(componentName);
        } catch (Exception e) {
            Slog.i(TAG, "getRomVersion error!" + e);
            return null;
        }
    }

    public boolean setRestoreFactoryDisabled(ComponentName admin, boolean disabled) {
        try {
            boolean result = getOplusCustomizeSettingsManagerService().setRestoreFactoryDisabled(admin, disabled);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setRestoreFactoryDisabled Error" + e);
            return false;
        }
    }

    public boolean isRestoreFactoryDisabled(ComponentName admin) {
        try {
            boolean result = getOplusCustomizeSettingsManagerService().isRestoreFactoryDisabled(admin);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isRestoreFactoryDisabled Error" + e);
            return false;
        }
    }

    public boolean setDevelopmentOptionsDisabled(ComponentName componentName, boolean disabled) {
        try {
            boolean result = getOplusCustomizeSettingsManagerService().setDevelopmentOptionsDisabled(componentName, disabled);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setDevelopmentOptionsDisabled Error" + e);
            return false;
        }
    }

    public boolean isDeveloperOptionsDisabled(ComponentName componentName) {
        try {
            boolean result = getOplusCustomizeSettingsManagerService().isDeveloperOptionsDisabled(componentName);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isDeveloperOptionsDisabled Error" + e);
            return false;
        }
    }

    public boolean setTimeAndDateSetDisabled(ComponentName componentName, boolean disabled) {
        try {
            boolean result = getOplusCustomizeSettingsManagerService().setTimeAndDateSetDisabled(componentName, disabled);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "setTimeAndDateSetDisabled Error" + e);
            return false;
        }
    }

    public boolean isTimeAndDateSetDisabled(ComponentName componentName) {
        try {
            boolean result = getOplusCustomizeSettingsManagerService().isTimeAndDateSetDisabled(componentName);
            return result;
        } catch (Exception e) {
            Slog.e(TAG, "isTimeAndDateSetDisabled Error" + e);
            return false;
        }
    }

    public void addManageNotificationToWhiteList(List<String> pkgList) {
        try {
            getOplusCustomizeSettingsManagerService().addManageNotificationToWhiteList(pkgList);
        } catch (Exception e) {
            Slog.e(TAG, "addManageNotificationToWhiteList fail!" + e.getMessage());
        }
    }

    public void removeManageNotificationFromWhiteList(List<String> pkgList) {
        try {
            getOplusCustomizeSettingsManagerService().removeManageNotificationFromWhiteList(pkgList);
        } catch (Exception e) {
            Slog.e(TAG, "removeManageNotificationFromWhiteList fail!" + e.getMessage());
        }
    }

    public List<String> getManageNotificationWhiteList() {
        try {
            return getOplusCustomizeSettingsManagerService().getManageNotificationWhiteList();
        } catch (Exception e) {
            Slog.e(TAG, "getManageNotificationWhiteList fail!" + e.getMessage());
            return null;
        }
    }

    public void deleteManageNotificationFromWhiteList() {
        try {
            getOplusCustomizeSettingsManagerService().deleteManageNotificationFromWhiteList();
        } catch (Exception e) {
            Slog.e(TAG, "deleteManageNotificationFromWhiteList fail!" + e.getMessage());
        }
    }

    public boolean setInterceptAllNotifications(boolean intercepted) {
        try {
            return getOplusCustomizeSettingsManagerService().setInterceptAllNotifications(intercepted);
        } catch (Exception e) {
            Slog.e(TAG, "setInterceptAllNotifications fail!" + e.getMessage());
            return false;
        }
    }

    public boolean shouldInterceptAllNotifications() {
        try {
            return getOplusCustomizeSettingsManagerService().shouldInterceptAllNotifications();
        } catch (Exception e) {
            Slog.e(TAG, "shouldInterceptAllNotifications fail!" + e.getMessage());
            return false;
        }
    }

    public boolean setInterceptNonSystemNotifications(boolean intercepted) {
        try {
            return getOplusCustomizeSettingsManagerService().setInterceptNonSystemNotifications(intercepted);
        } catch (Exception e) {
            Slog.e(TAG, "setInterceptNonSystemNotifications fail!" + e.getMessage());
            return false;
        }
    }

    public boolean shouldInterceptNonSystemNotifications() {
        try {
            return getOplusCustomizeSettingsManagerService().shouldInterceptNonSystemNotifications();
        } catch (Exception e) {
            Slog.e(TAG, "shouldInterceptNonSystemNotifications fail!" + e.getMessage());
            return false;
        }
    }
}
