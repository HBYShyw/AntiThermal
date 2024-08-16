package com.oplus.permission;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.OplusPermissionManager;
import android.net.Uri;
import android.os.SystemProperties;
import android.util.Log;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class OplusPermissionCheckInjector implements IOplusPermissionCheckInjector {
    private static final String CONTENT_QUERY = "query";
    private static final boolean DEBUG = false;
    private static final String TAG = "PermissionInjector";
    private static volatile OplusPermissionCheckInjector sInstance = null;
    private static final boolean ENABLE = SystemProperties.getBoolean("persist.sys.permission.enable", true);

    public static OplusPermissionCheckInjector getInstance() {
        if (sInstance == null) {
            synchronized (OplusPermissionCheckInjector.class) {
                if (sInstance == null) {
                    sInstance = new OplusPermissionCheckInjector();
                }
            }
        }
        return sInstance;
    }

    public boolean checkPermission(String permission, int pid, int uid, String access) {
        if (ENABLE) {
            OplusPermissionManager opm = OplusPermissionManager.getInstance();
            if (!opm.checkOplusPermission(permission, pid, uid)) {
                Log.w(TAG, "Permission denied: uid: " + uid + " or pid: " + pid + " have no permission: " + permission + " to access: " + access);
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean checkPermission(Intent intent, int pid, int uid, String access) {
        if (ENABLE && intent != null && "android.intent.action.CALL".equals(intent.getAction())) {
            OplusPermissionManager opm = OplusPermissionManager.getInstance();
            Uri uri = intent.getData();
            if (uri == null) {
                Log.v(TAG, "intent uri is null");
                return true;
            }
            String uriString = uri.toString();
            String permission = uri.isHierarchical() ? "android.permission.CALL_PHONE" : "android.permission.CALL_PHONE" + uriString;
            if (!opm.checkOplusPermission(permission, pid, uid)) {
                Log.w(TAG, "Permission denied: uid: " + uid + " or pid: " + pid + " have no permission: " + permission + " to access: " + access + ": " + intent.getAction());
                return false;
            }
        }
        return true;
    }

    public boolean checkUriPermission(Uri uri, int pid, int uid, String access) {
        String permission;
        if (uri == null) {
            Log.w(TAG, "uri is null");
            return true;
        }
        if (ENABLE) {
            OplusPermissionManager opm = OplusPermissionManager.getInstance();
            String uriString = uri.toString();
            boolean readPermission = CONTENT_QUERY.equals(access);
            if (uriString.startsWith("content://call_log/calls")) {
                permission = readPermission ? "android.permission.READ_CALL_LOG" : "android.permission.WRITE_CALL_LOG";
            } else if (uriString.startsWith("content://sms") || uriString.startsWith("content://mms-sms")) {
                permission = readPermission ? "android.permission.READ_SMS" : "android.permission.WRITE_SMS";
            } else if (uriString.startsWith("content://mms")) {
                permission = readPermission ? OplusPermissionManager.READ_MMS_PERMISSION : OplusPermissionManager.WRITE_MMS_PERMISSION;
            } else if (uriString.startsWith("content://com.android.contacts")) {
                permission = readPermission ? "android.permission.READ_CONTACTS" : "android.permission.WRITE_CONTACTS";
            } else if (uriString.startsWith("content://com.android.calendar")) {
                permission = readPermission ? "android.permission.READ_CALENDAR" : "android.permission.WRITE_CALENDAR";
            } else if (uriString.startsWith("content://com.android.voicemail/voicemail")) {
                permission = "com.android.voicemail.permission.ADD_VOICEMAIL";
            } else {
                if (!uriString.startsWith("content://com.android.browser") && !uriString.startsWith("content://com.heytap.browser/bookmarks") && !uriString.startsWith("content://com.heytap.browser/history")) {
                    return true;
                }
                permission = "com.android.browser.permission.READ_HISTORY_BOOKMARKS";
            }
            if (!opm.checkOplusPermission(permission, pid, uid)) {
                Log.w(TAG, "Permission denied: uid: " + uid + " or pid: " + pid + " have no permission: " + permission + " to access: " + access + " provider: " + uriString);
                return false;
            }
        }
        return true;
    }

    public boolean checkApplyBatchPermission(ArrayList<ContentProviderOperation> operations, int pid, int uid, String access) {
        if (ENABLE && operations != null && operations.size() > 0) {
            ContentProviderOperation operation = operations.get(0);
            if (operation.isWriteOperation()) {
                int type = operation.getType();
                switch (type) {
                    case 1:
                    case 3:
                        return checkUriPermission(operation.getUri(), pid, uid, access);
                    case 2:
                    default:
                        Log.w(TAG, "applyBatch in invalid type: " + type);
                        return true;
                }
            }
            return true;
        }
        return true;
    }
}
