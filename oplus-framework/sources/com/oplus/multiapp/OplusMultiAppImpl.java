package com.oplus.multiapp;

import android.app.ActivityManager;
import android.app.AppGlobals;
import android.app.IActivityManager;
import android.app.OplusActivityManager;
import android.content.pm.IPackageManager;
import android.os.IBinder;
import android.os.IUserManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.oplus.Telephony;
import android.text.TextUtils;
import android.util.Log;
import android.util.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusMultiAppImpl implements IOplusMultiApp {
    private static boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final List<String> FILTER_PROFILE_PACKAGE;
    private static final String TAG = "MultiApp.Impl";
    private static final Singleton<IUserManager> iUserManagerSingleton;
    private Boolean mIsSupportCache = null;
    private OplusActivityManager oplusActivityManager;

    static {
        ArrayList arrayList = new ArrayList();
        FILTER_PROFILE_PACKAGE = arrayList;
        arrayList.add("com.android.settings");
        arrayList.add("com.android.managedprovisioning");
        iUserManagerSingleton = new Singleton<IUserManager>() { // from class: com.oplus.multiapp.OplusMultiAppImpl.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* renamed from: create, reason: merged with bridge method [inline-methods] */
            public IUserManager m725create() {
                IBinder b = ServiceManager.getService(Telephony.Carriers.USER);
                IUserManager userManager = IUserManager.Stub.asInterface(b);
                return userManager;
            }
        };
    }

    public OplusMultiAppImpl() {
        Log.e(TAG, "OplusMultiAppImpl");
    }

    private synchronized boolean enforceActivityManager() {
        if (this.oplusActivityManager == null) {
            IActivityManager am = null;
            try {
                am = ActivityManager.getService();
            } catch (Exception e) {
                Log.e(TAG, "enforceActivityManager error, ams not ready yet!", e);
            }
            if (am == null) {
                Log.e(TAG, "enforceActivityManager error, ams not ready yet!");
                return false;
            }
            this.oplusActivityManager = new OplusActivityManager();
        }
        return true;
    }

    private static IUserManager getUserManagerService() {
        return (IUserManager) iUserManagerSingleton.get();
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public List<String> getMultiAppList(int type) {
        if (!isMultiAppSupport()) {
            return new ArrayList();
        }
        try {
            return this.oplusActivityManager.getMultiAppList(type);
        } catch (Exception e) {
            Log.e(TAG, "getMultiAppList ", e);
            return new ArrayList();
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean setMultiAppConfig(OplusMultiAppConfig config) {
        if (!isMultiAppSupport()) {
            return false;
        }
        try {
            return this.oplusActivityManager.setMultiAppConfig(config) >= 0;
        } catch (Exception e) {
            Log.e(TAG, "setMultiAppConfig", e);
            return false;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public OplusMultiAppConfig getMultiAppConfig() {
        if (!isMultiAppSupport()) {
            return null;
        }
        try {
            return this.oplusActivityManager.getMultiAppConfig();
        } catch (Exception e) {
            Log.e(TAG, "setMultiAppConfig", e);
            return null;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public String getMultiAppAlias(String pkgName) {
        if (!isMultiAppSupport()) {
            return null;
        }
        try {
            return this.oplusActivityManager.getMultiAppAlias(pkgName);
        } catch (Exception e) {
            Log.e(TAG, "getMultiAppAlias ", e);
            return null;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean setMultiAppAlias(String pkgName, String alias) {
        if (!isMultiAppSupport()) {
            return false;
        }
        try {
            return this.oplusActivityManager.setMultiAppAlias(pkgName, alias) >= 0;
        } catch (Exception e) {
            Log.e(TAG, "setMultiAppAlias", e);
            return false;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public int getMultiAppAccessMode(String pkgName) {
        if (!isMultiAppSupport()) {
            return 0;
        }
        try {
            return this.oplusActivityManager.getMultiAppAccessMode(pkgName);
        } catch (Exception e) {
            Log.e(TAG, "getMultiAppAccessMode ", e);
            return 0;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean setMultiAppAccessMode(String pkgName, int accessMode) {
        if (!isMultiAppSupport()) {
            return false;
        }
        try {
            return this.oplusActivityManager.setMultiAppAccessMode(pkgName, accessMode) >= 0;
        } catch (Exception e) {
            Log.e(TAG, "setMultiAppAccessMode", e);
            return false;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public Map<String, Integer> getMultiAppAllAccessMode() {
        Map<String, Integer> resMap = new HashMap<>();
        if (!isMultiAppSupport()) {
            return resMap;
        }
        try {
            return this.oplusActivityManager.getMultiAppAllAccessMode();
        } catch (Exception e) {
            Log.e(TAG, "getMultiAppAllAccessMode", e);
            return resMap;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean isMultiApp(int userId, String pkgName) {
        if (!isMultiAppSupport()) {
            return false;
        }
        try {
            return this.oplusActivityManager.isMultiApp(userId, pkgName);
        } catch (Exception e) {
            Log.e(TAG, "isMultiApp", e);
            return false;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public int setMultiAppPackageStatus(String pkgName, int status) {
        if (!isMultiAppSupport()) {
            return -2;
        }
        if (TextUtils.isEmpty(pkgName)) {
            Log.e(TAG, "setMultiAppPackageStatus pkgName is null");
            return -1;
        }
        try {
            return this.oplusActivityManager.setMultiAppStatus(pkgName, status);
        } catch (Exception e) {
            Log.e(TAG, "setMultiAppStatus", e);
            return -1;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public int getMaxCreateNum() {
        if (!isMultiAppSupport()) {
            return 0;
        }
        try {
            return this.oplusActivityManager.getMultiAppMaxCreateNum();
        } catch (Exception e) {
            Log.e(TAG, "getMaxCreateNum ", e);
            return 0;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean isMultiAppSupport() {
        try {
            if (this.mIsSupportCache == null) {
                if (!enforceActivityManager()) {
                    return false;
                }
                this.mIsSupportCache = Boolean.valueOf(this.oplusActivityManager.getIsSupportMultiApp());
            }
            return this.mIsSupportCache.booleanValue();
        } catch (Exception e) {
            Log.e(TAG, "getMultiAppList ", e);
            return false;
        }
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean isMultiAppUserHandle(UserHandle userHandle) {
        if (!isMultiAppSupport()) {
            return false;
        }
        if (userHandle != null) {
            return userHandle.getIdentifier() == 999;
        }
        Log.e(TAG, "isMultiAppUserHandle userHandle is null error!");
        return false;
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public UserHandle getMultiAppUserHandle() {
        if (!isMultiAppSupport()) {
            return null;
        }
        IUserManager userManager = getUserManagerService();
        if (userManager == null) {
            Log.e(TAG, "getMultiAppUserHandle userManager is null");
            return null;
        }
        try {
            int[] userIds = userManager.getProfileIds(0, true);
            for (int id : userIds) {
                if (999 == id) {
                    return new UserHandle(id);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getMultiAppUserHandle", e);
        }
        Log.v(TAG, "getMultiAppUserHandle return null");
        return null;
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean isMultiAppUserId(int userId) {
        return isMultiAppSupport() && 999 == userId;
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean isCrossUserAuthority(String authority, int userId) {
        if (!isMultiAppSupport() || !isMultiAppUserId(userId)) {
            return false;
        }
        List<String> crossAuthorityList = getMultiAppList(4);
        if (crossAuthorityList == null || !crossAuthorityList.contains(authority)) {
            Log.d(TAG, "multi app ->  isCrossUserAuthority not allow for " + authority);
            return false;
        }
        Log.d(TAG, "multi app ->  isCrossUserAuthority allow for " + authority);
        return true;
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public boolean isProfileFilterPackage(String pkgName) {
        if (!isMultiAppSupport()) {
            return false;
        }
        return FILTER_PROFILE_PACKAGE.contains(pkgName);
    }

    @Override // com.oplus.multiapp.IOplusMultiApp
    public void scanFileIfNeed(int userId, String path) {
        if (!isMultiAppSupport()) {
            return;
        }
        try {
            this.oplusActivityManager.scanFileIfNeed(userId, path);
        } catch (RemoteException e) {
            Log.e(TAG, "scanFileIfNeed", e);
        }
    }

    private boolean isStorageLow() {
        IPackageManager pm = AppGlobals.getPackageManager();
        if (pm == null) {
            Log.e(TAG, "isStorageLow pm is null");
            return true;
        }
        try {
            return pm.isStorageLow();
        } catch (Exception e) {
            Log.e(TAG, "isStorageLow", e);
            return true;
        }
    }
}
