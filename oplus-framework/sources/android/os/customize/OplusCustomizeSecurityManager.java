package android.os.customize;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.customize.IOplusCustomizeSecurityManagerService;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusCustomizeSecurityManager {
    private static final String SERVICE_NAME = "OplusCustomizeSecurityManagerService";
    private static final String TAG = "OplusCustomizeSecurityManager";
    private static final Object mLock = new Object();
    private static final Object mServiceLock = new Object();
    private static volatile OplusCustomizeSecurityManager sInstance;
    private IOplusCustomizeSecurityManagerService mOplusCustomizeSecurityManagerService;

    private OplusCustomizeSecurityManager() {
        getOplusCustomizeSecurityManagerService();
    }

    public static final OplusCustomizeSecurityManager getInstance(Context context) {
        OplusCustomizeSecurityManager oplusCustomizeSecurityManager;
        if (sInstance == null) {
            synchronized (mLock) {
                if (sInstance == null) {
                    sInstance = new OplusCustomizeSecurityManager();
                }
                oplusCustomizeSecurityManager = sInstance;
            }
            return oplusCustomizeSecurityManager;
        }
        return sInstance;
    }

    private IOplusCustomizeSecurityManagerService getOplusCustomizeSecurityManagerService() {
        IOplusCustomizeSecurityManagerService iOplusCustomizeSecurityManagerService;
        synchronized (mServiceLock) {
            if (this.mOplusCustomizeSecurityManagerService == null) {
                this.mOplusCustomizeSecurityManagerService = IOplusCustomizeSecurityManagerService.Stub.asInterface(OplusCustomizeManager.getInstance().getDeviceManagerServiceByName(SERVICE_NAME));
            }
            if (this.mOplusCustomizeSecurityManagerService == null) {
                Slog.e(TAG, "mOplusCustomizeSecurityManagerService is null");
            }
            iOplusCustomizeSecurityManagerService = this.mOplusCustomizeSecurityManagerService;
        }
        return iOplusCustomizeSecurityManagerService;
    }

    public void setEmmAdmin(ComponentName admin, boolean enable) {
        try {
            getOplusCustomizeSecurityManagerService().setEmmAdmin(admin, enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setEmmAdmin fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setEmmAdmin Error" + e2);
        }
    }

    public List<ComponentName> getEmmAdmin() {
        List<ComponentName> emmAdminList = new ArrayList<>();
        try {
            return getOplusCustomizeSecurityManagerService().getEmmAdmin();
        } catch (RemoteException e) {
            Slog.e(TAG, "getEmmAdmin fail!", e);
            return emmAdminList;
        } catch (Exception e2) {
            Slog.e(TAG, "getEmmAdmin Error" + e2);
            return emmAdminList;
        }
    }

    public boolean setDeviceOwner(ComponentName admin) {
        if (admin != null) {
            try {
                return getOplusCustomizeSecurityManagerService().setDeviceOwner(admin);
            } catch (RemoteException e) {
                Slog.e(TAG, "setDeviceOwner fail!", e);
                return false;
            } catch (Exception e2) {
                Slog.e(TAG, "setDeviceOwner Error" + e2);
                return false;
            }
        }
        return false;
    }

    public ComponentName getDeviceOwner() {
        try {
            return getOplusCustomizeSecurityManagerService().getDeviceOwner();
        } catch (RemoteException e) {
            Slog.e(TAG, "getDeviceOwner fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getDeviceOwner Error" + e2);
            return null;
        }
    }

    public void clearDeviceOwner(String packageName) {
        try {
            getOplusCustomizeSecurityManagerService().clearDeviceOwner(packageName);
        } catch (RemoteException e) {
            Slog.e(TAG, "clearDeviceOwner fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "clearDeviceOwner Error" + e2);
        }
    }

    public void setCustomDevicePolicyEnabled(boolean enable) {
        try {
            getOplusCustomizeSecurityManagerService().setCustomDevicePolicyEnabled(enable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setCustomDevicePolicyEnabled fail!", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setCustomDevicePolicyEnabled Error" + e2);
        }
    }

    public boolean isCustomDevicePolicyEnabled() {
        try {
            return getOplusCustomizeSecurityManagerService().isCustomDevicePolicyEnabled();
        } catch (RemoteException e) {
            Slog.e(TAG, "isCustomDevicePolicyEnabled fail!", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isCustomDevicePolicyEnabled Error" + e2);
            return false;
        }
    }

    public List<String> getDeviceInfo(ComponentName admin) {
        List<String> devInfo = new ArrayList<>();
        try {
            IOplusCustomizeSecurityManagerService iService = getOplusCustomizeSecurityManagerService();
            return iService != null ? iService.getDeviceInfo(admin) : devInfo;
        } catch (RemoteException e) {
            Slog.e(TAG, "getDeviceInfo error!" + e);
            return devInfo;
        }
    }

    public Bitmap captureFullScreen() {
        try {
            IOplusCustomizeSecurityManagerService iService = getOplusCustomizeSecurityManagerService();
            if (iService != null) {
                return iService.captureFullScreen();
            }
            return null;
        } catch (RemoteException e) {
            Slog.e(TAG, "captureFullScreen error!" + e);
            return null;
        }
    }

    public String executeShellToSetIptables(String commandline) {
        try {
            String result = getOplusCustomizeSecurityManagerService().executeShellToSetIptables(commandline);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "setCustomDevicePolicyEnabled fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "setCustomDevicePolicyEnabled Error" + e2);
            return null;
        }
    }

    public String getPhoneNumber(int subId) {
        try {
            String result = getOplusCustomizeSecurityManagerService().getPhoneNumber(subId);
            return result;
        } catch (RemoteException e) {
            Slog.e(TAG, "getPhoneNumber fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getPhoneNumber Error" + e2);
            return null;
        }
    }

    public Bundle getMobileCommSettings(ComponentName admin, String business, String setting) {
        try {
            return getOplusCustomizeSecurityManagerService().getMobileCommSettings(admin, business, setting);
        } catch (Exception e) {
            Slog.d(TAG, "getMobileCommSettings:err", e);
            return null;
        }
    }

    public void setMobileCommSettings(ComponentName admin, String business, Bundle bundle) {
        try {
            getOplusCustomizeSecurityManagerService().setMobileCommSettings(admin, business, bundle);
        } catch (Exception e) {
            Slog.d(TAG, "setMobileCommSettings:err", e);
        }
    }

    public boolean setServerType(int serverType) {
        try {
            boolean ret = getOplusCustomizeSecurityManagerService().setServerType(serverType);
            return ret;
        } catch (Exception e) {
            Slog.d(TAG, "setServerType:err", e);
            return false;
        }
    }

    public int getServerType() {
        try {
            int ret = getOplusCustomizeSecurityManagerService().getServerType();
            return ret;
        } catch (Exception e) {
            Slog.d(TAG, "getServerType:err", e);
            return 0;
        }
    }

    public boolean setDeviceLocked(ComponentName cn2) {
        try {
            IOplusCustomizeSecurityManagerService manager = getOplusCustomizeSecurityManagerService();
            if (manager != null) {
                Slog.d(TAG, "mdm service IDeviceSecurityManager manager:" + manager);
                return manager.setDeviceLocked(cn2);
            }
            Slog.e(TAG, "mdm service IDeviceSecurityManager manager is null");
            return false;
        } catch (RemoteException e) {
            Slog.e(TAG, "setDeviceLocked error!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean setDeviceUnLocked(ComponentName cn2) {
        try {
            IOplusCustomizeSecurityManagerService manager = getOplusCustomizeSecurityManagerService();
            if (manager != null) {
                Slog.d(TAG, "mdm service IDeviceSecurityManager manager:" + manager);
                return manager.setDeviceUnLocked(cn2);
            }
            Slog.e(TAG, "mdm service IDeviceSecurityManager manager is null");
            return false;
        } catch (RemoteException e) {
            Slog.e(TAG, "setDeviceUnLocked error!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean needHideKeyguardByMdm() {
        try {
            IOplusCustomizeSecurityManagerService manager = getOplusCustomizeSecurityManagerService();
            if (manager != null) {
                Slog.d(TAG, "mdm service IDeviceSecurityManager manager:" + manager);
                return manager.needHideKeyguardByMdm();
            }
            Slog.e(TAG, "mdm service IDeviceSecurityManager manager is null");
            return false;
        } catch (RemoteException e) {
            Slog.e(TAG, "needHideKeyguardByMdm error!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean enableThirdRecord(boolean isEnable) {
        try {
            return getOplusCustomizeSecurityManagerService().enableThirdRecord(isEnable);
        } catch (RemoteException e) {
            Slog.e(TAG, "enableThirdRecord Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "enableThirdRecord Error" + e2);
            return false;
        }
    }

    public boolean isEnableThirdRecord() {
        try {
            return getOplusCustomizeSecurityManagerService().isEnableThirdRecord();
        } catch (RemoteException e) {
            Slog.e(TAG, "isEnableThirdRecord Error");
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "isEnableThirdRecord Error" + e2);
            return false;
        }
    }

    public boolean setPasswordDisable(boolean disable) {
        try {
            IOplusCustomizeSecurityManagerService manager = getOplusCustomizeSecurityManagerService();
            if (manager == null) {
                return false;
            }
            return manager.setPasswordDisable(disable);
        } catch (RemoteException e) {
            Slog.e(TAG, "setPasswordDisable Error");
            return false;
        }
    }

    public boolean isSetPasswordDisable() {
        try {
            IOplusCustomizeSecurityManagerService manager = getOplusCustomizeSecurityManagerService();
            if (manager == null) {
                return false;
            }
            return manager.isSetPasswordDisable();
        } catch (RemoteException e) {
            Slog.e(TAG, "isSetPasswordDisable Error");
            return false;
        }
    }

    public void backupAppData(int rootPathMode, String src, String packageName, String dest, int requestId) {
        Slog.d(TAG, "backupAppData src:" + src + " rootPathMode: " + rootPathMode + " packageName:" + packageName + " dest:" + dest + " requestId:" + requestId);
        try {
            getOplusCustomizeSecurityManagerService().backupAppData(rootPathMode, src, packageName, dest, requestId);
        } catch (RemoteException e) {
            Slog.e(TAG, "backupAppData Error");
        } catch (Exception e2) {
            Slog.e(TAG, "backupAppData Error" + e2);
        }
    }

    public boolean setProfileOwner(ComponentName admin) {
        if (admin != null) {
            try {
                return getOplusCustomizeSecurityManagerService().setProfileOwner(admin);
            } catch (RemoteException e) {
                Slog.e(TAG, "setProfileOwner fail!", e);
                return false;
            } catch (Exception e2) {
                Slog.e(TAG, "setProfileOwner Error" + e2);
                return false;
            }
        }
        return false;
    }

    public ComponentName getProfileOwner() {
        try {
            return getOplusCustomizeSecurityManagerService().getProfileOwner();
        } catch (RemoteException e) {
            Slog.e(TAG, "getProfileOwner fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getProfileOwner Error" + e2);
            return null;
        }
    }

    public void clearProfileOwner(ComponentName admin) {
        if (admin != null) {
            try {
                getOplusCustomizeSecurityManagerService().clearProfileOwner(admin);
            } catch (RemoteException e) {
                Slog.e(TAG, "clearProfileOwner fail!", e);
            } catch (Exception e2) {
                Slog.e(TAG, "clearProfileOwner Error" + e2);
            }
        }
    }

    public void setOrganizationName(ComponentName admin, CharSequence name) {
        if (admin != null) {
            try {
                getOplusCustomizeSecurityManagerService().setOrganizationName(admin, name);
            } catch (RemoteException e) {
                Slog.e(TAG, "setOrganizationName fail!", e);
            } catch (Exception e2) {
                Slog.e(TAG, "setOrganizationName Error" + e2);
            }
        }
    }

    public CharSequence getOrganizationName(ComponentName admin) {
        if (admin != null) {
            try {
                return getOplusCustomizeSecurityManagerService().getOrganizationName(admin);
            } catch (RemoteException e) {
                Slog.e(TAG, "getOrganizationName fail!", e);
                return null;
            } catch (Exception e2) {
                Slog.e(TAG, "getOrganizationName Error" + e2);
                return null;
            }
        }
        return null;
    }

    public CharSequence getDeviceOwnerOrganizationName() {
        try {
            return getOplusCustomizeSecurityManagerService().getDeviceOwnerOrganizationName();
        } catch (RemoteException e) {
            Slog.e(TAG, "getDeviceOwnerOrganizationName fail!", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "getDeviceOwnerOrganizationName Error" + e2);
            return null;
        }
    }

    public void setVerificationSkip(boolean skip) {
        try {
            IOplusCustomizeSecurityManagerService iService = getOplusCustomizeSecurityManagerService();
            if (iService == null) {
                Slog.e(TAG, "mdm service IDeviceSecurityManager manager is null");
            } else {
                iService.setVerificationSkip(skip);
            }
        } catch (RemoteException e) {
            Slog.d(TAG, "setVerificationSkip: fail", e);
        } catch (Exception e2) {
            Slog.e(TAG, "setVerificationSkip Error" + e2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean isVerificationSkip() {
        String str = TAG;
        boolean z = false;
        try {
            IOplusCustomizeSecurityManagerService oplusCustomizeSecurityManagerService = getOplusCustomizeSecurityManagerService();
            if (oplusCustomizeSecurityManagerService == null) {
                Slog.e(TAG, "mdm service IDeviceSecurityManager manager is null");
                str = str;
            } else {
                boolean isVerificationSkip = oplusCustomizeSecurityManagerService.isVerificationSkip();
                z = isVerificationSkip;
                str = isVerificationSkip;
            }
        } catch (RemoteException e) {
            Slog.d(str, "isVerificationSkip: fail", e);
        } catch (Exception e2) {
            Slog.e(str, "isVerificationSkip Error" + e2);
        }
        return z;
    }

    public String readMdmLog() {
        try {
            return getOplusCustomizeSecurityManagerService().readMdmLog();
        } catch (RemoteException e) {
            Slog.d(TAG, "readMdmLog: fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "readMdmLog Error" + e2);
            return null;
        }
    }

    public boolean writeMdmLog(String event, String result, String describe) {
        try {
            return getOplusCustomizeSecurityManagerService().writeMdmLog(event, result, describe);
        } catch (RemoteException e) {
            Slog.d(TAG, "writeMdmLog: fail", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "writeMdmLog Error" + e2);
            return false;
        }
    }

    public boolean clearMdmLog() {
        try {
            return getOplusCustomizeSecurityManagerService().clearMdmLog();
        } catch (RemoteException e) {
            Slog.d(TAG, "clearMdmLog: fail", e);
            return false;
        } catch (Exception e2) {
            Slog.e(TAG, "clearMdmLog Error" + e2);
            return false;
        }
    }

    public void deleteAppData(boolean isDir, String src, String packageName, int requestId) {
        Slog.d(TAG, "deleteAppData packageName:" + packageName + " src:" + src + " requestId:" + requestId);
        try {
            getOplusCustomizeSecurityManagerService().deleteAppData(isDir, src, packageName, requestId);
        } catch (RemoteException e) {
            Slog.e(TAG, "deleteAppData Error");
        } catch (Exception e2) {
            Slog.e(TAG, "deleteAppData Error" + e2);
        }
    }

    public void copyFileToAppData(String from, String src, String packageName, int requestId) {
        Slog.d(TAG, "copyFileToAppData from:" + from + " packageName:" + packageName + " src:" + src + " requestId:" + requestId);
        try {
            getOplusCustomizeSecurityManagerService().copyFileToAppData(from, src, packageName, requestId);
        } catch (RemoteException e) {
            Slog.e(TAG, "copyFileToAppData Error");
        } catch (Exception e2) {
            Slog.e(TAG, "copyFileToAppData Error" + e2);
        }
    }

    public String[] listImei() {
        try {
            return getOplusCustomizeSecurityManagerService().listImei();
        } catch (RemoteException e) {
            Slog.d(TAG, "list fail", e);
            return null;
        } catch (Exception e2) {
            Slog.e(TAG, "list Error" + e2);
            return null;
        }
    }
}
