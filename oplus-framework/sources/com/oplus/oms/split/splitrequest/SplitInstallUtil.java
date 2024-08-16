package com.oplus.oms.split.splitrequest;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.ProcessInfoData;
import com.oplus.oms.split.common.ProcessUtil;
import com.oplus.oms.split.common.SharedPreferencesUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.common.SplitProcessUtils;
import com.oplus.oms.split.splitrequest.SplitInfo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: classes.dex */
public class SplitInstallUtil {
    private static final String TAG = "SplitInstallUtils";

    private SplitInstallUtil() {
    }

    public static void putSplitLoadedFailMD5(Context context, String splitName, int version, String md5) {
        if (context == null || TextUtils.isEmpty(splitName)) {
            SplitLog.w(TAG, "markSplitLoadedFailMD5 fail, SplitName:%s , version:%d", splitName, Integer.valueOf(version));
        } else {
            String Key = splitName + SplitConstants.SUFFIX_LOADED_FAIL_MD5 + version;
            SharedPreferencesUtil.getInstance(context).put(Key, md5);
        }
    }

    public static String getSplitLoadFailMD5(Context context, String splitName, int version) {
        if (context == null || TextUtils.isEmpty(splitName)) {
            SplitLog.w(TAG, "markSplitLoadedFailMD5 fail, SplitName:%s , version:%d", splitName, Integer.valueOf(version));
            return null;
        }
        String md5 = (String) SharedPreferencesUtil.getInstance(context).get(splitName + SplitConstants.SUFFIX_LOADED_FAIL_MD5 + version, "");
        return md5;
    }

    public static void putSplitInstallVersion(Context context, String splitName, int version) {
        if (context == null || TextUtils.isEmpty(splitName)) {
            SplitLog.w(TAG, "setSplitInstallStatus fail, SplitName:%s , version:%d", splitName, Integer.valueOf(version));
        } else {
            SharedPreferencesUtil.getInstance(context).put(splitName + SplitConstants.SUFFIX_INSTALL_STATUS, Integer.valueOf(version));
            putFileSplitVersion(splitName, version);
        }
    }

    private static void putFileSplitVersion(String splitName, int version) {
        File splitDir = SplitPathManager.require().getSplitDir(splitName);
        File versionCodeFile = new File(splitDir, "SplitCopier.lock");
        try {
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(versionCodeFile), StandardCharsets.UTF_8));
            try {
                fileWriter.write(version);
                fileWriter.flush();
                fileWriter.close();
            } finally {
            }
        } catch (FileNotFoundException e) {
            SplitLog.e(TAG, "SplitCopier.lock no found", e);
        } catch (IOException e2) {
            SplitLog.e(TAG, "clean up split version code error", e2);
        }
    }

    public static int getSplitInstallVersion(Context context, String splitName) {
        if (context == null || TextUtils.isEmpty(splitName)) {
            SplitLog.w(TAG, "getSplitInstallStatus fail, SplitName = " + splitName, new Object[0]);
            return -1;
        }
        return ((Integer) SharedPreferencesUtil.getInstance(context).get(splitName + SplitConstants.SUFFIX_INSTALL_STATUS, -1)).intValue();
    }

    public static void putSplitLoadSuccessVersion(Context context, String splitName, int version) {
        if (context == null || TextUtils.isEmpty(splitName)) {
            SplitLog.w(TAG, "putSplitLoadSuccessVersion fail, SplitName = " + splitName, new Object[0]);
        } else {
            SharedPreferencesUtil.getInstance(context).put(splitName + SplitConstants.SUFFIX_LOADED_SUCCESS, Integer.valueOf(version));
        }
    }

    public static int getSplitLoadSuccessVersion(Context context, String splitName) {
        if (context == null || TextUtils.isEmpty(splitName)) {
            SplitLog.w(TAG, "getSplitLoadSuccessVersion fail, SplitName = " + splitName, new Object[0]);
            return -1;
        }
        return ((Integer) SharedPreferencesUtil.getInstance(context).get(splitName + SplitConstants.SUFFIX_LOADED_SUCCESS, -1)).intValue();
    }

    public static void removeInstallOtherFile(SplitInfo splitInfo, int installedVersion, Context context) {
        if (splitInfo == null || context == null || installedVersion == -1) {
            SplitLog.w(TAG, "removeInstallOtherFile failed", new Object[0]);
            return;
        }
        String splitName = splitInfo.getSplitName();
        File splitApkDir = SplitPathManager.require().getSplitApkDir(splitName, installedVersion, false);
        String abi = getSplitNativeLibAbi(splitInfo, context);
        FileUtil.deleteOthersDirs(splitApkDir, abi);
        List<ProcessInfoData> subInfoList = SplitProcessUtils.getSubProcessInfoData(splitName);
        Set<String> subProcessSet = new HashSet<>();
        for (ProcessInfoData sub : subInfoList) {
            String processName = sub.getProcessName();
            if (!TextUtils.isEmpty(processName) && !subProcessSet.contains(processName)) {
                subProcessSet.add(processName);
            }
        }
        if (!subProcessSet.isEmpty()) {
            ProcessUtil.killProcess(context, subProcessSet);
        }
    }

    public static void removeSplitInstalledFile(SplitInfo splitInfo, int installedVersion, Context context) {
        if (splitInfo == null || context == null || installedVersion == -1) {
            SplitLog.w(TAG, "removeSplitInstalledFile failed", new Object[0]);
            return;
        }
        String splitName = splitInfo.getSplitName();
        File installedFile = SplitPathManager.require().getSplitApkDir(splitName, installedVersion, false);
        if (installedFile == null || !installedFile.exists()) {
            SplitLog.w(TAG, "removeSplitInstalledFile installed file is not existed", new Object[0]);
            return;
        }
        String abi = getSplitNativeLibAbi(splitInfo, context);
        putNativeLibDirAccessStatus(splitName, abi, installedVersion);
        FileUtil.deleteDir(installedFile);
        if (installedFile.exists()) {
            SplitLog.w(TAG, "Failed to delete corrupted split files", new Object[0]);
        } else {
            SplitLog.d(TAG, "delete success", new Object[0]);
        }
    }

    private static String getSplitNativeLibAbi(SplitInfo splitInfo, Context context) {
        if (splitInfo == null || context == null) {
            SplitLog.w(TAG, "getSplitNativeLibAbi fail", new Object[0]);
            return null;
        }
        splitInfo.getSplitName();
        try {
            SplitInfo.LibData libData = splitInfo.getPrimaryLibData(context);
            if (libData == null) {
                SplitLog.e(TAG, "getSplitNativeLibAbi libData is null", new Object[0]);
                return null;
            }
            return libData.getAbi();
        } catch (IOException e) {
            SplitLog.e(TAG, "getSplitNativeLibAbi error " + e.getMessage(), new Object[0]);
            return null;
        }
    }

    private static void putNativeLibDirAccessStatus(String splitName, String abi, int installedVersion) {
        if (TextUtils.isEmpty(splitName) || TextUtils.isEmpty(abi) || installedVersion == -1) {
            SplitLog.e(TAG, "setNativeLibDirAccessStatus failed", new Object[0]);
            return;
        }
        File splitLibDir = SplitPathManager.require().getSplitLibDir(splitName, abi, installedVersion);
        if (splitLibDir.exists()) {
            splitLibDir.setWritable(true);
            splitLibDir.setExecutable(true);
        }
    }
}
