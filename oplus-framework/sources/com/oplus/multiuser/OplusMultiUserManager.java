package com.oplus.multiuser;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.provider.oplus.Telephony;
import android.util.Log;
import android.util.Singleton;
import com.oplus.multiuser.IOplusMultiUserManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OplusMultiUserManager {
    public static final int FLAG_MULTI_SYSTEM = 536870912;
    public static final int FLAG_STUDY_USER = 1073741824;
    private static final Singleton<IOplusMultiUserManager> IOplusMultiUserManagerSingleton;
    private static final List<String> sForbiddenPkgList;
    private static final Singleton<OplusMultiUserManager> sOplusMultiUserManagerSingleton;
    private static String TAG = "OplusMultiUserManager";
    private static boolean sHasCheckClone = false;
    private static int sMultiSystemUserId = -10000;

    static {
        ArrayList arrayList = new ArrayList();
        sForbiddenPkgList = arrayList;
        arrayList.add("com.heytap.cloud");
        arrayList.add("com.oplus.gtmode");
        sOplusMultiUserManagerSingleton = new Singleton<OplusMultiUserManager>() { // from class: com.oplus.multiuser.OplusMultiUserManager.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* renamed from: create, reason: merged with bridge method [inline-methods] */
            public OplusMultiUserManager m729create() {
                return new OplusMultiUserManager();
            }
        };
        IOplusMultiUserManagerSingleton = new Singleton<IOplusMultiUserManager>() { // from class: com.oplus.multiuser.OplusMultiUserManager.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* renamed from: create, reason: merged with bridge method [inline-methods] */
            public IOplusMultiUserManager m730create() {
                try {
                    IBinder b = ServiceManager.getService(Telephony.Carriers.USER);
                    IOplusMultiUserManager oplusMultiUserManager = IOplusMultiUserManager.Stub.asInterface(b.getExtension());
                    return oplusMultiUserManager;
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        };
    }

    public static IOplusMultiUserManager getService() {
        return (IOplusMultiUserManager) IOplusMultiUserManagerSingleton.get();
    }

    public static OplusMultiUserManager getInstance() {
        return (OplusMultiUserManager) sOplusMultiUserManagerSingleton.get();
    }

    public boolean hasMultiSystemUser() {
        return -10000 != getMultiSystemUserIdNoCheck();
    }

    public int getMultiSystemUserId() {
        return getMultiSystemUserIdNoCheck();
    }

    public boolean isMultiSystemUserId(int userId) {
        int id = getMultiSystemUserIdNoCheck();
        if (-10000 != id && userId == id) {
            return true;
        }
        return false;
    }

    public boolean isMultiSystemUserHandle(UserHandle userHandle) {
        if (userHandle == null) {
            return false;
        }
        return isMultiSystemUserId(userHandle.getIdentifier());
    }

    public boolean isMultiSystemUser(UserInfo info) {
        if (info != null && (info.flags & 536870912) != 0) {
            return true;
        }
        return false;
    }

    public static boolean isStudyUser(Context context, int userId) {
        UserInfo info;
        UserManager userManager = (UserManager) context.getSystemService(Telephony.Carriers.USER);
        if (userManager != null && (info = userManager.getUserInfo(userId)) != null && (info.flags & 1073741824) != 0) {
            return true;
        }
        return false;
    }

    public static int getStudyUserId(Context context) {
        List<UserInfo> uis;
        UserManager userManager = (UserManager) context.getSystemService(Telephony.Carriers.USER);
        if (userManager != null && (uis = userManager.getUsers()) != null) {
            for (UserInfo ui : uis) {
                if (isStudyUser(context, ui.id)) {
                    return ui.id;
                }
            }
            return -1;
        }
        return -1;
    }

    public static boolean isMultiSystemForbiddenPkg(String pkgName) {
        return sForbiddenPkgList.contains(pkgName);
    }

    public static List<String> getForbiddenPkgList() {
        return sForbiddenPkgList;
    }

    private int getMultiSystemUserIdNoCheck() {
        int res = -10000;
        try {
            IOplusMultiUserManager service = getService();
            res = service.getMultiSystemUserIdNoCheck();
        } catch (RemoteException e) {
            Log.e(TAG, "getMultiSystemUserIdNoCheck: " + e.toString());
        } catch (Exception e2) {
            Log.e(TAG, Log.getStackTraceString(e2));
        }
        Log.d(TAG, "getMultiSystemUserIdNoCheck res: " + res);
        return res;
    }

    public boolean putConfigurationForUser(ContentResolver resolver, Configuration config, int userId) {
        if (!sHasCheckClone && sMultiSystemUserId == -10000 && userId == 0) {
            sMultiSystemUserId = getMultiSystemUserId();
            sHasCheckClone = true;
        }
        int i = sMultiSystemUserId;
        if (i == -10000 || (!(userId == 0 || userId == i) || isSystemUserOrMultiSystemUserInSimpleState(resolver, i))) {
            return false;
        }
        Settings.System.putConfigurationForUser(resolver, config, 0);
        Settings.System.putConfigurationForUser(resolver, config, sMultiSystemUserId);
        return true;
    }

    private boolean isSystemUserOrMultiSystemUserInSimpleState(ContentResolver resolver, int userId) {
        return Settings.Secure.getIntForUser(resolver, "simple_mode_enabled", 0, 0) == 1 || Settings.Secure.getIntForUser(resolver, "simple_mode_enabled", 0, userId) == 1;
    }

    public void adjustConfigurationForUser(ContentResolver resolver, Configuration config, int currentUserId) {
        if (isMultiSystemUserId(currentUserId)) {
            sMultiSystemUserId = currentUserId;
            if (!isSystemUserOrMultiSystemUserInSimpleState(resolver, currentUserId)) {
                config.fontScale = Settings.System.getFloatForUser(resolver, "font_scale", 1.0f, 0);
            }
        }
    }
}
