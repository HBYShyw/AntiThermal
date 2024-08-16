package com.oplus.app;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import com.oplus.app.IOplusAccessControlManager;
import com.oplus.content.OplusContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OPlusAccessControlManager {
    public static final String ACCESS_CONTROL_FROM_POCKET_STUDIO = "Access_Control_From_Pocket_Studio";
    public static final String ACCESS_CONTROL_LOCK_ENABLED = "access_control_lock_enabled";
    public static final String ACCESS_CONTROL_LOCK_MODE = "access_control_lock_mode";
    public static final String ACCESS_CONTROL_PACKAGES_LIST_FROM_POCKET_STUDIO = "Access_Control_Packages_From_Pocket_Studio";
    public static final String ACCESS_CONTROL_PACKAGE_NAME = "Access_Control_Package_Name";
    public static final String ACCESS_CONTROL_PACKAGE_USERID = "Access_Control_Package_UserId";
    public static final int FLAG_ENCRYPTED = 8;
    public static final int FLAG_HIDE_ICON = 1;
    public static final int FLAG_HIDE_IN_RECENT = 2;
    public static final int FLAG_HIDE_NOTICE = 4;
    public static final String INITIALIZED_FROM_TWO_FINGER_SWIPE = "Initialized_From_Two_Finger_Swipe";
    public static final String LAUNCH_ACTIVITY_OPTIONS = "Launch_Activity_Options";
    public static final String LAUNCH_WINDOWING_MODE = "Launch_Windowing_Mode";
    public static final int MODE_EACH = 0;
    public static final int MODE_LOCK_SCREEN = 1;
    public static final String NEED_TRAVERSE_PACKAGES_WHEN_ACCESS_CHECK = "Need_Traverse_Packages_When_Access_Check";
    public static final int RUS_TYPE_FILTER = 0;
    public static final int RUS_TYPE_HIDE_KEYGUARD_LOCK = 1;
    public static final String SHOW_WHEN_LOCK = "show_when_lock";
    public static final String SOURCE_BUNDLE_FROM_POCKET_STUDIO = "Source_Bundle_from_Pocket_Studio";
    private static final String TAG = "OPlusAccessControlManager";
    public static final String TASK_ID = "task_id";
    public static final String TYPE_ENCRYPT = "type_encrypt";
    public static final String TYPE_ENCRYPT_IGNORE_ENABLE = "type_encrypt_ignore_enable";
    public static final String TYPE_HIDE = "type_hide";
    public static final String TYPE_HIDE_IGNORE_ENABLE = "type_hide_ignore_enable";
    public static final int USER_XSPACE = 999;
    private final IOplusAccessControlManager mService = IOplusAccessControlManager.Stub.asInterface(ServiceManager.getService(OplusContext.ACCESS_CONTROL_SERVICE));
    private static volatile OPlusAccessControlManager sInstance = null;
    public static final int USER_CURRENT = UserHandle.myUserId();

    private OPlusAccessControlManager() {
    }

    public static OPlusAccessControlManager getInstance() {
        if (sInstance == null) {
            synchronized (OPlusAccessControlManager.class) {
                if (sInstance == null) {
                    sInstance = new OPlusAccessControlManager();
                }
            }
        }
        return sInstance;
    }

    public void setAccessControlAppsInfo(String type, Map<String, Integer> accessControlInfo, int userId) {
        if (userId != 999) {
            try {
                userId = UserHandle.myUserId();
            } catch (RemoteException e) {
                throw new RuntimeException("setAccessControlAppsInfo exception", e);
            }
        }
        IOplusAccessControlManager iOplusAccessControlManager = this.mService;
        if (iOplusAccessControlManager != null) {
            iOplusAccessControlManager.setAccessControlAppsInfo(type, accessControlInfo, userId);
        } else {
            Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
        }
    }

    public Map<String, Integer> getAccessControlAppsInfo(String type, int userId) {
        if (userId != 999) {
            try {
                userId = UserHandle.myUserId();
            } catch (RemoteException e) {
                throw new RuntimeException("getAccessControlAppsInfo exception", e);
            }
        }
        IOplusAccessControlManager iOplusAccessControlManager = this.mService;
        if (iOplusAccessControlManager != null) {
            return iOplusAccessControlManager.getAccessControlAppsInfo(type, userId);
        }
        Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
        return null;
    }

    public void setAccessControlEnabled(String type, boolean enable, int userId) {
        if (userId != 999) {
            try {
                userId = UserHandle.myUserId();
            } catch (RemoteException e) {
                throw new RuntimeException("setAccessControlEnabled exception", e);
            }
        }
        IOplusAccessControlManager iOplusAccessControlManager = this.mService;
        if (iOplusAccessControlManager != null) {
            iOplusAccessControlManager.setAccessControlEnabled(type, enable, userId);
        } else {
            Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
        }
    }

    public boolean getAccessControlEnabled(String type, int userId) {
        if (userId != 999) {
            try {
                userId = UserHandle.myUserId();
            } catch (RemoteException e) {
                throw new RuntimeException("getAccessControlEnabled exception", e);
            }
        }
        IOplusAccessControlManager iOplusAccessControlManager = this.mService;
        if (iOplusAccessControlManager != null) {
            boolean enable = iOplusAccessControlManager.getAccessControlEnabled(type, userId);
            return enable;
        }
        Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
        return false;
    }

    public void addEncryptPass(String packageName, int windowMode, int userId) {
        if (userId != 999) {
            try {
                userId = UserHandle.myUserId();
            } catch (RemoteException e) {
                throw new RuntimeException("addEncryptPass exception", e);
            }
        }
        IOplusAccessControlManager iOplusAccessControlManager = this.mService;
        if (iOplusAccessControlManager != null) {
            iOplusAccessControlManager.addEncryptPass(packageName, windowMode, userId);
        } else {
            Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
        }
    }

    public boolean isEncryptPass(String packageName, int userId) {
        if (userId != 999) {
            try {
                userId = UserHandle.myUserId();
            } catch (RemoteException e) {
                throw new RuntimeException("isEncryptPass exception", e);
            }
        }
        IOplusAccessControlManager iOplusAccessControlManager = this.mService;
        if (iOplusAccessControlManager != null) {
            boolean pass = iOplusAccessControlManager.isEncryptPass(packageName, userId);
            return pass;
        }
        Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
        return false;
    }

    public boolean isEncryptedPackage(String packageName, int userId) {
        if (userId != 999) {
            try {
                userId = UserHandle.myUserId();
            } catch (RemoteException e) {
                throw new RuntimeException("isEncryptedPackage remoteException ", e);
            }
        }
        IOplusAccessControlManager iOplusAccessControlManager = this.mService;
        if (iOplusAccessControlManager != null) {
            boolean isEncrypted = iOplusAccessControlManager.isEncryptedPackage(packageName, userId);
            return isEncrypted;
        }
        Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
        return false;
    }

    public boolean registerAccessControlObserver(String type, IOplusAccessControlObserver observer) {
        try {
            IOplusAccessControlManager iOplusAccessControlManager = this.mService;
            if (iOplusAccessControlManager == null) {
                Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
                return false;
            }
            return iOplusAccessControlManager.registerAccessControlObserver(type, observer);
        } catch (RemoteException e) {
            Log.e(TAG, "registerAccessControlObserver remoteException ");
            e.printStackTrace();
            return false;
        }
    }

    public boolean unregisterAccessControlObserver(String type, IOplusAccessControlObserver observer) {
        try {
            IOplusAccessControlManager iOplusAccessControlManager = this.mService;
            if (iOplusAccessControlManager == null) {
                Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
                return false;
            }
            return iOplusAccessControlManager.unregisterAccessControlObserver(type, observer);
        } catch (RemoteException e) {
            Log.e(TAG, "unregisterAccessControlObserver remoteException ");
            e.printStackTrace();
            return false;
        }
    }

    public void updateRusList(int type, List<String> addList, List<String> deleteList) {
        try {
            IOplusAccessControlManager iOplusAccessControlManager = this.mService;
            if (iOplusAccessControlManager != null) {
                iOplusAccessControlManager.updateRusList(type, addList, deleteList);
            } else {
                Log.w(TAG, "setAccessControlAppsInfo: AccessControl Service is null");
            }
        } catch (RemoteException e) {
            throw new RuntimeException("updateRusList exception", e);
        }
    }

    public void setPrivacyAppsInfoForUser(Map<String, Integer> privacyInfo, boolean enabled, int userId) {
    }

    public boolean getApplicationAccessControlEnabledAsUser(String packageName, int userId) {
        return false;
    }

    public void addAccessControlPassForUser(String packageName, int windowMode, int userId) {
    }

    public Map<String, Integer> getPrivacyAppInfo(int userId) {
        HashMap<String, Integer> accessControlInfo = new HashMap<>();
        return accessControlInfo;
    }

    public boolean isAccessControlPassForUser(String packageName, int userId) {
        return false;
    }
}
