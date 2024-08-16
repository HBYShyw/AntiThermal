package com.oplus.ota;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Pair;
import android.util.Slog;
import com.oplus.content.OplusIntent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

/* loaded from: classes.dex */
public class OplusOtaUtils {
    private static final String OTA_UPDATE_FAILED = "1";
    private static final String OTA_UPDATE_OK = "0";
    private static final String RECOVER_UPDATE_FAILED = "3";
    private static final String RECOVER_UPDATE_OK = "2";
    private static final String TAG = "OplusOtaUtils";

    public static void notifyOTAUpdateResult(Context context) {
        String otaResultStr;
        boolean isSauUpdate = false;
        File lastInstallFile = new File("/cache/recovery/last_install");
        if (lastInstallFile.exists() && (otaResultStr = readOTAUpdateResult("/cache/recovery/last_install")) != null && otaResultStr.contains("/.SAU/zip/")) {
            isSauUpdate = true;
        }
        File file = new File("/cache/recovery/intent");
        File fileForOpProject = new File("/cache/recovery/intent_from_op");
        if (fileForOpProject.exists() && fileForOpProject.isFile()) {
            Slog.i(TAG, "delete /cache/recovery/intent_from_op file");
            fileForOpProject.delete();
        }
        Slog.d(TAG, "check /cache/recovery/intent");
        if (file.exists()) {
            Slog.i(TAG, "/cache/recovery/intent file is exist!!!");
            String otaResultStr2 = readOTAUpdateResult("/cache/recovery/intent");
            if ("0".equals(otaResultStr2)) {
                Slog.i(TAG, "OTA update successed!!!");
                Intent otaIntent = new Intent(isSauUpdate ? OplusIntent.ACTION_SAU_UPDATE_SUCCESSED : "oplus.intent.action.OPLUS_OTA_UPDATE_SUCCESSED");
                otaIntent.addFlags(16777216);
                String ffuresult = readFFUUpdateResult();
                if (ffuresult != null) {
                    otaIntent.putExtra("ffuresult", ffuresult);
                }
                context.sendBroadcast(otaIntent);
                SystemProperties.set("persist.sys.panictime", Integer.toString(0));
                return;
            }
            if ("1".equals(otaResultStr2)) {
                Slog.i(TAG, "OTA update failed!!!");
                Intent otaIntent2 = new Intent(isSauUpdate ? OplusIntent.ACTION_SAU_UPDATE_FAILED : OplusIntent.ACTION_OPLUS_OTA_UPDATE_FAILED);
                otaIntent2.addFlags(16777216);
                sendOTAFailLogIntent(context, otaIntent2);
                return;
            }
            if ("2".equals(otaResultStr2)) {
                Slog.i(TAG, "Recover update ok!!!");
                Intent otaIntent3 = new Intent("oplus.intent.action.OPLUS_RECOVER_UPDATE_SUCCESSED");
                String ffuresult2 = readFFUUpdateResult();
                if (ffuresult2 != null) {
                    otaIntent3.putExtra("ffuresult", ffuresult2);
                }
                otaIntent3.addFlags(16777216);
                context.sendBroadcast(otaIntent3);
                return;
            }
            if ("3".equals(otaResultStr2)) {
                Slog.i(TAG, "Recover update failed!!!");
                Intent otaIntent4 = new Intent(OplusIntent.ACTION_OPLUS_RECOVER_UPDATE_FAILED);
                otaIntent4.addFlags(16777216);
                sendOTAFailLogIntent(context, otaIntent4);
                return;
            }
            Slog.i(TAG, "OTA update file's date is invalid!!!");
        }
    }

    private static void sendOTAFailLogIntent(Context context, Intent otaIntent) {
        Pair<Integer, String> failedMsg = OplusSystemUpdateInfoHelper.readOTAUpdateFailedTypeFromLastLog(OplusSystemUpdateInfoHelper.readErrorMapFromConfig());
        if (failedMsg == null) {
            Slog.e(TAG, "failed msg is null");
        } else {
            otaIntent.putExtra("errType", (Serializable) failedMsg.first);
            otaIntent.putExtra("errLine", (String) failedMsg.second);
        }
        context.sendBroadcast(otaIntent);
        Slog.d(TAG, "deal ota log pass!!!");
    }

    private static String readOTAUpdateResult(String fileName) {
        String resultStr = null;
        BufferedReader reader = null;
        File file = new File(fileName);
        try {
        } catch (IOException e1) {
            Slog.e(TAG, "readOTAUpdateResult close the reader failed!!!", e1);
        }
        try {
            try {
                reader = new BufferedReader(new FileReader(file));
                resultStr = reader.readLine();
                reader.close();
            } catch (IOException e) {
                Slog.e(TAG, "readOTAUpdateResult failed!!!", e);
                if (reader != null) {
                    reader.close();
                }
            }
            return resultStr;
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e12) {
                    Slog.e(TAG, "readOTAUpdateResult close the reader failed!!!", e12);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x003f -> B:7:0x005d). Please report as a decompilation issue!!! */
    private static String readFFUUpdateResult() {
        String resultStr = null;
        BufferedReader reader = null;
        File file = new File("/cache/recovery/last_ffu");
        try {
            try {
            } catch (IOException e1) {
                Slog.e(TAG, "readFFUUpdateResult close the reader failed!!!", e1);
            }
            if (file.exists()) {
                try {
                    reader = new BufferedReader(new FileReader(file));
                    resultStr = reader.readLine();
                    Slog.i(TAG, "readFFUUpdateResult resultStr=" + resultStr);
                    reader.close();
                } catch (IOException e) {
                    Slog.e(TAG, "readFFUUpdateResult failed!!!", e);
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
            return resultStr;
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e12) {
                    Slog.e(TAG, "readFFUUpdateResult close the reader failed!!!", e12);
                }
            }
            throw th;
        }
    }
}
