package android.content.pm;

import android.net.Uri;
import android.os.IBinder;
import android.os.ISecurityPermissionService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class OplusPermissionManager {
    public static final int ACCEPT = 0;
    public static final String AUTHORITY = "com.oplusos.provider.PermissionProvider";
    public static final int FIRST_MASK = 1;
    public static final int INVALID_RES = 3;
    public static final int PROMPT = 2;
    public static final int REJECT = 1;
    public static final String SERVICE_NAME = "security_permission";
    private static final String TAG = "OplusPermissionManager";
    private ISecurityPermissionService mSecurityPermissionService;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    public static final Uri PERMISSIONS_PROVIDER_URI = Uri.parse("content://com.oplusos.provider.PermissionProvider/pp_permission");
    private static OplusPermissionManager mInstance = null;
    public static final String WRITE_MMS_PERMISSION = "android.permission.WRITE_MMS";
    public static final List<String> WRITE_PERMISSIONS = Arrays.asList("android.permission.WRITE_CALL_LOG", "android.permission.WRITE_CONTACTS", "android.permission.WRITE_SMS", WRITE_MMS_PERMISSION, "android.permission.WRITE_CALENDAR");
    public static final String READ_MMS_PERMISSION = "android.permission.READ_MMS";
    public static final String SEND_MMS_PERMISSION = "android.permission.SEND_MMS";
    public static final String PERMISSION_WR_EXTERNAL_STORAGE = "android.permission.WR_EXTERNAL_STORAGE";
    public static final String PERMISSION_ACCESS_MEDIA_PROVIDER = "android.permission.ACCESS_MEDIA_PROVIDER";
    public static final List<String> OPLUS_DEFINED_PERMISSIONS = Arrays.asList(READ_MMS_PERMISSION, WRITE_MMS_PERMISSION, SEND_MMS_PERMISSION, PERMISSION_WR_EXTERNAL_STORAGE, PERMISSION_ACCESS_MEDIA_PROVIDER);
    public static final List<String> OPLUS_PRIVACY_PROTECT_PERMISSIONS = Arrays.asList("android.permission.READ_SMS", "android.permission.WRITE_SMS", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR");
    public static final List<String> OPLUS_DETECT_FREQUENT_CHECK_PERMISSIONS = Arrays.asList("android.permission.READ_PHONE_STATE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.READ_CONTACTS#com.callershow.colorcaller");
    public static final String PERMISSION_DELETE_CALL = "android.permission.WRITE_CALL_LOG_DELETE";
    public static final String PERMISSION_DELETE_CONTACTS = "android.permission.WRITE_CONTACTS_DELETE";
    public static final String PERMISSION_DELETE_SMS = "android.permission.WRITE_SMS_DELETE";
    public static final String PERMISSION_DELETE_MMS = "android.permission.WRITE_MMS_DELETE";
    public static final String PERMISSION_DELETE_CALENDAR = "android.permission.WRITE_CALENDAR_DELETE";
    public static final String PERMISSION_CALL_FORWARDING = "oplus.permission.call.FORWARDING";
    public static final String PERMISSION_READ_APPS = "com.android.permission.GET_INSTALLED_APPS";
    public static List<String> sInterceptingPermissions = Arrays.asList("android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.READ_CONTACTS", "android.permission.READ_SMS", "android.permission.SEND_SMS", SEND_MMS_PERMISSION, "android.permission.CHANGE_NETWORK_STATE", "android.permission.CHANGE_WIFI_STATE", "android.permission.BLUETOOTH_ADMIN", "android.permission.ACCESS_FINE_LOCATION", "android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.NFC", "android.permission.WRITE_CALL_LOG", "android.permission.WRITE_CONTACTS", "android.permission.WRITE_SMS", WRITE_MMS_PERMISSION, READ_MMS_PERMISSION, "com.android.browser.permission.READ_HISTORY_BOOKMARKS", "android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR", PERMISSION_DELETE_CALL, PERMISSION_DELETE_CONTACTS, PERMISSION_DELETE_SMS, PERMISSION_DELETE_MMS, PERMISSION_DELETE_CALENDAR, "android.permission.GET_ACCOUNTS", "android.permission.READ_PHONE_STATE", "com.android.voicemail.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.RECEIVE_SMS", "android.permission.RECEIVE_MMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.BODY_SENSORS", PERMISSION_WR_EXTERNAL_STORAGE, PERMISSION_ACCESS_MEDIA_PROVIDER, "android.permission.BIND_VPN_SERVICE", PERMISSION_CALL_FORWARDING, "android.permission.ACTIVITY_RECOGNITION", "android.permission.READ_PHONE_NUMBERS", PERMISSION_READ_APPS);
    public static int[] sPermissionsDefaultChoices = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    public static List<String> sAlwaysInterceptingPermissions = Arrays.asList("android.permission.SEND_SMS");

    private OplusPermissionManager() {
        this.mSecurityPermissionService = null;
        IBinder serviceBinder = ServiceManager.getService(SERVICE_NAME);
        this.mSecurityPermissionService = ISecurityPermissionService.Stub.asInterface(serviceBinder);
    }

    public static long getPermissionMask(String permission) {
        long mask = 0;
        int index = sInterceptingPermissions.indexOf(permission);
        if (index > -1) {
            mask = 1 << index;
        }
        if (DEBUG) {
            Log.d(TAG, "getPermissionMask, permission=" + permission + ", mask=" + mask + ", index=" + index);
        }
        return mask;
    }

    public static OplusPermissionManager getInstance() {
        if (mInstance == null) {
            mInstance = new OplusPermissionManager();
        }
        return mInstance;
    }

    public int queryPermissionAsUser(String pkgName, String permissionName, int userId) {
        checkSecurityPermissionService();
        ISecurityPermissionService iSecurityPermissionService = this.mSecurityPermissionService;
        if (iSecurityPermissionService != null) {
            try {
                return iSecurityPermissionService.queryPermissionAsUser(pkgName, permissionName, userId);
            } catch (RemoteException exce) {
                Log.e(TAG, "queryPermission failed!", exce);
                return -1;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        Log.w(TAG, "queryPermission:oplus_permission not publish!");
        return -1;
    }

    public PackagePermission queryPackagePermissionsAsUser(String pkgName, int userId) {
        checkSecurityPermissionService();
        ISecurityPermissionService iSecurityPermissionService = this.mSecurityPermissionService;
        if (iSecurityPermissionService != null) {
            try {
                return iSecurityPermissionService.queryPackagePermissionsAsUser(pkgName, userId);
            } catch (RemoteException exce) {
                Log.e(TAG, "queryPackagePermissionsAsUser failed!", exce);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        Log.w(TAG, "queryPermission:oplus_permission not publish!");
        return null;
    }

    public void updateCachedPermission(String pkgName, int userId, boolean delete) {
        checkSecurityPermissionService();
        ISecurityPermissionService iSecurityPermissionService = this.mSecurityPermissionService;
        if (iSecurityPermissionService != null) {
            try {
                iSecurityPermissionService.updateCachedPermission(pkgName, userId, delete);
                return;
            } catch (RemoteException exce) {
                Log.e(TAG, "updateCachedPermission failed!", exce);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Log.w(TAG, "updateCachedPermission:oplus_permission not publish!");
    }

    public boolean checkOplusPermission(String permission, int pid, int uid) {
        checkSecurityPermissionService();
        ISecurityPermissionService iSecurityPermissionService = this.mSecurityPermissionService;
        if (iSecurityPermissionService != null) {
            try {
                return iSecurityPermissionService.checkOplusPermission(permission, pid, uid);
            } catch (RemoteException exce) {
                Log.e(TAG, "checkOplusPermission failed!", exce);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        Log.w(TAG, "checkOplusPermission:oplus_permission not publish!");
        return true;
    }

    public void writeRecommendPermissions(String recommendBody, boolean fromLocal) {
        checkSecurityPermissionService();
        ISecurityPermissionService iSecurityPermissionService = this.mSecurityPermissionService;
        if (iSecurityPermissionService != null) {
            try {
                iSecurityPermissionService.writeRecommendPermissions(recommendBody, fromLocal);
                return;
            } catch (RemoteException exce) {
                Log.e(TAG, "writeRecommendPermissions failed!", exce);
                return;
            } catch (Exception e) {
                Log.e(TAG, "writeRecommendPermissions exception: " + e.getMessage());
                return;
            }
        }
        Log.w(TAG, "checkOplusPermission:oplus_permission not publish!");
    }

    public Map<String, String> readRecommendPermissions(String packageName) {
        checkSecurityPermissionService();
        ISecurityPermissionService iSecurityPermissionService = this.mSecurityPermissionService;
        if (iSecurityPermissionService != null) {
            try {
                return iSecurityPermissionService.readRecommendPermissions(packageName);
            } catch (RemoteException exce) {
                Log.e(TAG, "readRecommendPermissions failed!", exce);
                return null;
            } catch (Exception e) {
                Log.e(TAG, "readRecommendPermissions exception: " + e.getMessage());
                return null;
            }
        }
        Log.w(TAG, "checkOplusPermission:oplus_permission not publish!");
        return null;
    }

    public long getLastUpdateTime() {
        checkSecurityPermissionService();
        ISecurityPermissionService iSecurityPermissionService = this.mSecurityPermissionService;
        if (iSecurityPermissionService != null) {
            try {
                return iSecurityPermissionService.getLastUpdateTime();
            } catch (RemoteException exce) {
                Log.e(TAG, "getLastUpdateTime failed!", exce);
                return 0L;
            } catch (Exception e) {
                Log.e(TAG, "getLastUpdateTime exception: " + e.getMessage());
                return 0L;
            }
        }
        Log.w(TAG, "checkOplusPermission:oplus_permission not publish!");
        return 0L;
    }

    private void checkSecurityPermissionService() {
        if (this.mSecurityPermissionService == null) {
            IBinder serviceBinder = ServiceManager.getService(SERVICE_NAME);
            this.mSecurityPermissionService = ISecurityPermissionService.Stub.asInterface(serviceBinder);
        }
    }
}
