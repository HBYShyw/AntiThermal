package com.oplus.ota;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Slog;
import com.oplus.romupdate.RomUpdateObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusSystemUpdateInfoHelper {
    private static final String COLUMN_NAME_1 = "version";
    private static final String COLUMN_NAME_2 = "xml";
    private static final String CONFIG_FILE_PATH = "data/oplus/os/config/sys_update_info.xml";
    private static final String FILTER_NAME = "sys_update_info";
    private static final String FILTER_RECOVERY_ERROR_SUB_TAG_NAME = "Msg";
    private static final String FILTER_RECOVERY_ERROR_TAG_NAME = "RecoveryErrorMsg";
    private static final String NORMAL_RECOVERY_ERROR_MSG_1 = "ERROR: Open file";
    private static final String NORMAL_RECOVERY_ERROR_MSG_10 = "This package expects the value";
    private static final String NORMAL_RECOVERY_ERROR_MSG_11 = "Package expects build fingerprint of";
    private static final String NORMAL_RECOVERY_ERROR_MSG_12 = "Package expects build thumbprint of";
    private static final String NORMAL_RECOVERY_ERROR_MSG_13 = "Can't install this package";
    private static final String NORMAL_RECOVERY_ERROR_MSG_14 = "This package is for";
    private static final String NORMAL_RECOVERY_ERROR_MSG_15 = "Failed to tune partition";
    private static final String NORMAL_RECOVERY_ERROR_MSG_16 = "Failed to apply patch to";
    private static final String NORMAL_RECOVERY_ERROR_MSG_17 = "oplus_ota_verify failed, abort install";
    private static final String NORMAL_RECOVERY_ERROR_MSG_2 = "signature verification failed";
    private static final String NORMAL_RECOVERY_ERROR_MSG_3 = "has unexpected contents";
    private static final String NORMAL_RECOVERY_ERROR_MSG_4 = "Not enough free space on";
    private static final String NORMAL_RECOVERY_ERROR_MSG_5 = "decryptFile file fail, stop install";
    private static final String NORMAL_RECOVERY_ERROR_MSG_6 = "verify components_prop error";
    private static final String NORMAL_RECOVERY_ERROR_MSG_7 = "Failed to update";
    private static final String NORMAL_RECOVERY_ERROR_MSG_8 = "partition fails to recover";
    private static final String NORMAL_RECOVERY_ERROR_MSG_9 = "partition has unexpected non-zero contents after";
    private static final String OPLUS_COMPONENT_SAFE_PERMISSION = "oplus.permission.OPLUS_COMPONENT_SAFE";
    private static final String OPLUS_UPDATE_SAFE_PERMISSION = "com.oplus.permission.safe.UPDATE";
    private static final String OTA_UPDATE_FAILED = "1";
    private static final String OTA_UPDATE_OK = "0";
    private static final String RECOVER_UPDATE_FAILED = "3";
    private static final String RECOVER_UPDATE_OK = "2";
    private static final String SPLIT_TAG = "#";
    private static final String TAG = "OplusSystemUpdateInfoHelper";
    private static volatile OplusSystemUpdateInfoHelper sHelper;
    private Context mContext;
    private OplusSystemUpdateInfo mInfo;
    private static final Uri CONTENT_URI_WHITE_LIST = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");
    private static boolean isAlreadyCopyRecoveryLog = false;

    private OplusSystemUpdateInfoHelper() {
    }

    public static OplusSystemUpdateInfoHelper getInstance() {
        if (sHelper == null) {
            synchronized (OplusSystemUpdateInfoHelper.class) {
                if (sHelper == null) {
                    sHelper = new OplusSystemUpdateInfoHelper();
                }
            }
        }
        return sHelper;
    }

    public void init(Context context) {
        Slog.d(TAG, "init");
        this.mContext = context;
        OplusSystemUpdateInfo info = readSystemUpdateInfo(readErrorMapFromConfig());
        synchronized (this) {
            this.mInfo = info;
        }
    }

    public OplusSystemUpdateInfo getInfo() {
        OplusSystemUpdateInfo oplusSystemUpdateInfo;
        Context context = this.mContext;
        if (context == null) {
            Slog.e(TAG, "context is null. Helper may not init");
            return null;
        }
        if (context.checkCallingOrSelfPermission("oplus.permission.OPLUS_COMPONENT_SAFE") == -1 && this.mContext.checkCallingOrSelfPermission(OPLUS_UPDATE_SAFE_PERMISSION) == -1) {
            throw new SecurityException("Neither user " + Binder.getCallingUid() + " nor current process has " + OPLUS_UPDATE_SAFE_PERMISSION + ".");
        }
        synchronized (this) {
            oplusSystemUpdateInfo = this.mInfo;
        }
        return oplusSystemUpdateInfo;
    }

    private static OplusSystemUpdateInfo readSystemUpdateInfo(HashMap<Integer, String> map) {
        OplusSystemUpdateInfo info = new OplusSystemUpdateInfo();
        boolean isSauUpdate = false;
        File lastInstallFile = new File("/cache/recovery/last_install");
        if (lastInstallFile.exists()) {
            String otaResultStr = readOTAUpdateResult("/cache/recovery/last_install");
            if (!TextUtils.isEmpty(otaResultStr) && otaResultStr.contains("/.SAU/zip/")) {
                Slog.i(TAG, "SAU update.");
                isSauUpdate = true;
            } else {
                Slog.d(TAG, "not SAU update.");
            }
        }
        Slog.d(TAG, "check /cache/recovery/intent");
        File file = new File("/cache/recovery/intent");
        if (!file.exists()) {
            Slog.d(TAG, "intent file not exists.");
            return info;
        }
        Slog.d(TAG, "intent file is exist.");
        String otaResultStr2 = readOTAUpdateResult("/cache/recovery/intent");
        if (TextUtils.isEmpty(otaResultStr2)) {
            Slog.d(TAG, "otaResultStr is null.");
            return info;
        }
        if ("0".equals(otaResultStr2)) {
            Slog.i(TAG, "OTA update success!!!");
            info.setUpdateSucc(true);
            info.setUpdateType(isSauUpdate ? 2 : 1);
        } else if ("1".equals(otaResultStr2)) {
            Slog.i(TAG, "OTA update failed!!!");
            info.setUpdateSucc(false);
            info.setUpdateType(isSauUpdate ? 2 : 1);
            Pair<Integer, String> failedMsg = readOTAUpdateFailedTypeFromLastLog(map);
            if (failedMsg == null) {
                info.setFailedType(0);
            } else {
                info.setFailedType(((Integer) failedMsg.first).intValue());
                info.setFailedMsg((String) failedMsg.second);
            }
        } else if ("2".equals(otaResultStr2)) {
            Slog.i(TAG, "recovery update success!!!");
            info.setUpdateSucc(true);
            info.setUpdateType(3);
        } else if ("3".equals(otaResultStr2)) {
            Slog.i(TAG, "recovery update failed!!!");
            info.setUpdateSucc(false);
            info.setUpdateType(3);
            Pair<Integer, String> failedMsg2 = readOTAUpdateFailedTypeFromLastLog(map);
            if (failedMsg2 == null) {
                info.setFailedType(0);
            } else {
                info.setFailedType(((Integer) failedMsg2.first).intValue());
                info.setFailedMsg((String) failedMsg2.second);
            }
        } else {
            Slog.i(TAG, "OTA update file's date is invalid!!!");
        }
        Slog.d(TAG, info.toString());
        return info;
    }

    private static String readOTAUpdateResult(String fileName) {
        String resultStr = null;
        File file = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                resultStr = reader.readLine();
                reader.close();
            } finally {
            }
        } catch (IOException e) {
            Slog.e(TAG, "readOTAUpdateResult failed!!!", e);
        }
        return resultStr;
    }

    public static Pair<Integer, String> readOTAUpdateFailedTypeFromLastLog(Map<Integer, String> map) {
        if (map == null || map.isEmpty()) {
            Slog.i(TAG, "map is null or empty");
            return null;
        }
        File otaLogfile = new File("/cache/recovery/last_log");
        Slog.d(TAG, "check last_log");
        if (!otaLogfile.exists()) {
            Slog.i(TAG, "last_log file is not exist!");
            return null;
        }
        Slog.d(TAG, "last_log file is exist!!!");
        if (!isAlreadyCopyRecoveryLog) {
            Slog.d(TAG, "start copy recovery log");
            isAlreadyCopyRecoveryLog = true;
            feedbackRecoveryLogToDCS();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(otaLogfile));
            while (true) {
                try {
                    String tmpStr = reader.readLine();
                    if (tmpStr != null) {
                        if (!TextUtils.isEmpty(tmpStr)) {
                            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                                if (tmpStr.contains(entry.getValue())) {
                                    Pair<Integer, String> pair = new Pair<>(entry.getKey(), entry.getValue());
                                    reader.close();
                                    return pair;
                                }
                            }
                        }
                    } else {
                        reader.close();
                        break;
                    }
                } finally {
                }
            }
        } catch (Exception e) {
            Slog.e(TAG, "get OTA error message failed!!!", e);
            return null;
        }
    }

    public static HashMap<Integer, String> readErrorMapFromConfig() {
        HashMap<Integer, String> map = new HashMap<>(8);
        map.put(-1, NORMAL_RECOVERY_ERROR_MSG_1);
        map.put(-2, NORMAL_RECOVERY_ERROR_MSG_2);
        map.put(-3, NORMAL_RECOVERY_ERROR_MSG_3);
        map.put(-4, NORMAL_RECOVERY_ERROR_MSG_4);
        map.put(-5, NORMAL_RECOVERY_ERROR_MSG_5);
        map.put(-6, NORMAL_RECOVERY_ERROR_MSG_6);
        map.put(-7, NORMAL_RECOVERY_ERROR_MSG_7);
        map.put(-8, NORMAL_RECOVERY_ERROR_MSG_8);
        map.put(-9, NORMAL_RECOVERY_ERROR_MSG_9);
        map.put(-10, NORMAL_RECOVERY_ERROR_MSG_10);
        map.put(-11, NORMAL_RECOVERY_ERROR_MSG_11);
        map.put(-12, NORMAL_RECOVERY_ERROR_MSG_12);
        map.put(-13, NORMAL_RECOVERY_ERROR_MSG_13);
        map.put(-14, NORMAL_RECOVERY_ERROR_MSG_14);
        map.put(-15, NORMAL_RECOVERY_ERROR_MSG_15);
        map.put(-16, NORMAL_RECOVERY_ERROR_MSG_16);
        map.put(-17, NORMAL_RECOVERY_ERROR_MSG_17);
        File file = new File(CONFIG_FILE_PATH);
        if (!file.exists()) {
            Slog.d(TAG, "config file not exists.");
            return map;
        }
        Slog.d(TAG, "config file is exists.");
        ArrayList<String> parseList = parseErrorMsgListFromXML(readStringFromFile(file));
        if (parseList == null || parseList.isEmpty()) {
            Slog.d(TAG, "parseList is null.");
            return map;
        }
        for (int i = 0; i < parseList.size(); i++) {
            String tmpMsg = parseList.get(i);
            if (!TextUtils.isEmpty(tmpMsg)) {
                String[] tmpSplit = tmpMsg.split(SPLIT_TAG);
                if (tmpSplit.length == 2) {
                    try {
                        int type = Integer.parseInt(tmpSplit[0]);
                        String msg = tmpSplit[1];
                        map.put(Integer.valueOf(type), msg);
                    } catch (Exception e) {
                        Slog.e(TAG, e.getMessage());
                    }
                }
            }
        }
        Slog.d(TAG, "map size:" + map.size());
        return map;
    }

    public void initUpdateBroadcastReceiver() {
        RomUpdateObserver.getInstance().register(FILTER_NAME, new RomUpdateObserver.OnReceiveListener() { // from class: com.oplus.ota.OplusSystemUpdateInfoHelper.1
            public void onReceive(Context context) {
                try {
                    Slog.d(OplusSystemUpdateInfoHelper.TAG, "onReceive");
                    OplusSystemUpdateInfoHelper.this.dealConfigFromProvider();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dealConfigFromProvider() {
        String configStr = getDataFromProvider();
        if (TextUtils.isEmpty(configStr)) {
            Slog.d(TAG, "config str is null");
        } else {
            saveConfigToFile(configStr, CONFIG_FILE_PATH);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x005d, code lost:
    
        if (r1 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x005f, code lost:
    
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0081, code lost:
    
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x007e, code lost:
    
        if (r1 == null) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getDataFromProvider() {
        Cursor cursor = null;
        String returnStr = null;
        String[] projection = {"version", "xml"};
        try {
            try {
                Context context = this.mContext;
                if (context == null) {
                    return null;
                }
                cursor = context.getContentResolver().query(CONTENT_URI_WHITE_LIST, projection, "filtername=\"sys_update_info\"", null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    int versioncolumnIndex = cursor.getColumnIndex("version");
                    int xmlcolumnIndex = cursor.getColumnIndex("xml");
                    cursor.moveToNext();
                    int configVersion = cursor.getInt(versioncolumnIndex);
                    returnStr = cursor.getString(xmlcolumnIndex);
                    Slog.d(TAG, "config updated, version = " + configVersion);
                }
            } catch (Exception e) {
                Slog.e(TAG, "We can not get white list data from provider, because of " + e);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static String readStringFromFile(File file) {
        StringBuilder buffer = new StringBuilder();
        if (file == null || !file.exists()) {
            Slog.e(TAG, "file is null or not exists.");
            return buffer.toString();
        }
        try {
            InputStream is = new FileInputStream(file);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                while (true) {
                    try {
                        String line = in.readLine();
                        if (line == null) {
                            break;
                        }
                        buffer.append(line);
                    } finally {
                    }
                }
                in.close();
                is.close();
            } finally {
            }
        } catch (Exception e) {
            Slog.e(TAG, e.getMessage());
        }
        return buffer.toString();
    }

    private boolean saveConfigToFile(String content, String filePath) {
        if (TextUtils.isEmpty(content)) {
            Slog.d(TAG, "content is null.");
            return false;
        }
        File file = new File(filePath);
        File parent = new File(file.getParent());
        if (!parent.isDirectory()) {
            parent.mkdirs();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            try {
                outStream.write(content.getBytes());
                Slog.d(TAG, "saveConfigToFile done.");
                outStream.close();
                return true;
            } finally {
            }
        } catch (Exception e) {
            Slog.e(TAG, e.getMessage());
            return false;
        }
    }

    private static ArrayList<String> parseErrorMsgListFromXML(String content) {
        int type;
        ArrayList<String> result = new ArrayList<>();
        if (TextUtils.isEmpty(content)) {
            Slog.e(TAG, "content is null.");
            return result;
        }
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(content));
            parser.nextTag();
            boolean isMsgTag = false;
            do {
                type = parser.next();
                switch (type) {
                    case 2:
                        if (!isMsgTag || !parser.getName().equals(FILTER_RECOVERY_ERROR_SUB_TAG_NAME)) {
                            isMsgTag = parser.getName().equals(FILTER_RECOVERY_ERROR_TAG_NAME);
                        } else {
                            String str = parser.nextText();
                            if (!TextUtils.isEmpty(str)) {
                                result.add(str);
                            }
                        }
                        break;
                    case 3:
                        if (parser.getName().equals(FILTER_RECOVERY_ERROR_TAG_NAME)) {
                            isMsgTag = false;
                        }
                        break;
                }
            } while (type != 1);
        } catch (Exception e) {
            Slog.e(TAG, e.getMessage());
        }
        Slog.d(TAG, "result:" + result);
        return result;
    }

    public static void feedbackRecoveryLogToDCS() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ZipRecoveryLogRunnable zipRunnable = new ZipRecoveryLogRunnable();
        executor.execute(zipRunnable);
    }
}
