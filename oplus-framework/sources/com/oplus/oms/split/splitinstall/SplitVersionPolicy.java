package com.oplus.oms.split.splitinstall;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.ProcessInfoData;
import com.oplus.oms.split.common.ProcessUtil;
import com.oplus.oms.split.common.SharedPreferencesUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.common.SplitProcessUtils;
import com.oplus.oms.split.splitdownload.IProvider;
import com.oplus.oms.split.splitdownload.ISplitUpdateManager;
import com.oplus.oms.split.splitdownload.SplitUpdateInfo;
import com.oplus.oms.split.splitload.SplitLoadManagerImpl;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInstallUtil;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
final class SplitVersionPolicy {
    public static final int BASE_VERSION_NUMBER = 100;
    public static final int FROM_ASSETS = 1;
    public static final int FROM_CLOUD = 2;
    public static final int FROM_COMPONENT = 3;
    public static final int FROM_CUSTOM = 4;
    public static final int FROM_ERROE = -1;
    public static final int FROM_INSTALLED = 0;
    public static final String TAG = "SplitVersionPolicy";

    /* loaded from: classes.dex */
    public interface VersionPolicyCallBack {
        void setSplitVersionInfoList(int i, List<SplitVersionInfo> list);
    }

    SplitVersionPolicy() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void selectSplitVersionList(final Context context, final List<SplitInfo> infos, final ISplitUpdateManager updateManager, final VersionPolicyCallBack callBack) {
        if (infos == null || infos.isEmpty()) {
            SplitLog.w(TAG, "select split version list error", new Object[0]);
            callBack.setSplitVersionInfoList(-100, null);
        } else {
            VersionQuery.getInstance().querySync(context, updateManager, new QueryStatus() { // from class: com.oplus.oms.split.splitinstall.SplitVersionPolicy.1
                @Override // com.oplus.oms.split.splitinstall.QueryStatus
                public void setQueryResult(int status) {
                    boolean netResult = false;
                    if (status == 2) {
                        netResult = true;
                    }
                    List<SplitVersionInfo> infoList = new ArrayList<>();
                    for (SplitInfo info : infos) {
                        SplitVersionInfo split = SplitVersionPolicy.selectSplitVersion(context, updateManager, info, netResult);
                        if (split == null) {
                            callBack.setSplitVersionInfoList(-100, infoList);
                            return;
                        }
                        SplitVersionInfo split2 = SplitVersionPolicy.selectSplitVersionLocalFirst(split);
                        SplitVersionPolicy.processCustomInstall(context, split2);
                        if (SplitVersionPolicy.needReplaceInstalledApk(context, split2)) {
                            if (split2.getFrom() == -1) {
                                SplitLog.i(SplitVersionPolicy.TAG, "select split name = " + split2.getSplitInfo().getSplitName() + " error", new Object[0]);
                                callBack.setSplitVersionInfoList(-100, infoList);
                                return;
                            } else {
                                SplitLog.i(SplitVersionPolicy.TAG, "select split name = " + split2.getSplitInfo().getSplitName() + ", version code = " + split2.getInstallVersionCode() + ", isFrom = " + split2.getFrom(), new Object[0]);
                                infoList.add(split2);
                                SplitVersionPolicy.removeInstalledSplit(context, split2);
                            }
                        }
                    }
                    callBack.setSplitVersionInfoList(0, infoList);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeInstalledSplit(Context context, SplitVersionInfo versionInfo) {
        File splitApk = SplitPathManager.require().getSplitApkFile(versionInfo.getSplitInfo().getSplitName(), versionInfo.getInstallVersionCode(), false);
        if (!splitApk.exists()) {
            return;
        }
        SplitLog.i(TAG, "removeInstalledSplit - split:%s, version:%d", versionInfo.getSplitInfo().getSplitName(), Integer.valueOf(versionInfo.getInstallVersionCode()));
        SplitInstallUtil.removeSplitInstalledFile(versionInfo.getSplitInfo(), versionInfo.getInstallVersionCode(), context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processCustomInstall(Context context, SplitVersionInfo info) {
        IProvider provider = SplitApkLoadStrategy.getInstatnce().getCustomProvider();
        if (provider == null) {
            return;
        }
        String splitName = info.getSplitInfo().getSplitName();
        int baseVersionCode = info.getSplitInfo().getSplitVersionCode();
        int customVersionCode = provider.getSplitVersionCode(context, splitName);
        if (!isVersionCodeMatchVersionManager(baseVersionCode, customVersionCode)) {
            customVersionCode = -1;
        }
        if (customVersionCode > info.getInstallVersionCode()) {
            info.setInstalledVersionCode(customVersionCode);
            info.setInstallVersionName(provider.getSplitVersionName(context, splitName));
            info.setFrom(4);
            SplitLog.i(TAG, "install custom split apk", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean needReplaceInstalledApk(Context context, SplitVersionInfo info) {
        String splitName = info.getSplitInfo().getSplitName();
        int installedVersionCode = SplitInstallUtil.getSplitInstallVersion(context, splitName);
        if (installedVersionCode == -1) {
            return true;
        }
        File apkFile = SplitPathManager.require().getSplitApkFile(splitName, installedVersionCode, false);
        if (!apkFile.exists()) {
            SplitInstallUtil.putSplitInstallVersion(context, splitName, -1);
            return true;
        }
        if (SplitLoadManagerImpl.hasInstance() && SplitLoadManagerImpl.getInstance().getLoadedSplitNames().contains(splitName)) {
            SplitLog.d(TAG, "SplitLoadManagerImpl the split has loaded", new Object[0]);
            return false;
        }
        if (isSplitHasLoaded(context, splitName)) {
            SplitLog.d(TAG, "the split has loaded = " + splitName, new Object[0]);
            return false;
        }
        if (info.getInstallVersionCode() > installedVersionCode) {
            return true;
        }
        if (info.getInstallVersionCode() == installedVersionCode) {
            boolean replace = replaceInstalledSplitForSameVersion(context, info, installedVersionCode, apkFile);
            SplitLog.d(TAG, "need replace installed split for same version = " + replace, new Object[0]);
            return replace;
        }
        SplitLog.d(TAG, "the split has installed", new Object[0]);
        return false;
    }

    private static boolean replaceInstalledSplitForSameVersion(Context context, SplitVersionInfo info, int installedVersion, File installedFile) {
        String splitName = info.getSplitInfo().getSplitName();
        String splitMd5 = info.getSplitInfo().getMasterApkMd5();
        SplitUpdateInfo updateInfo = info.getSplitDownloadInfo();
        if (info.getFrom() == 2 && updateInfo != null) {
            splitMd5 = updateInfo.getMd5();
        }
        if (TextUtils.isEmpty(splitMd5) || TextUtils.isEmpty(splitName) || splitMd5.equals(FileUtil.getMD5(installedFile))) {
            return false;
        }
        SplitInstallUtil.removeSplitInstalledFile(info.getSplitInfo(), installedVersion, context);
        SplitInstallUtil.putSplitInstallVersion(context, splitName, -1);
        return true;
    }

    private static boolean isSplitHasLoaded(Context context, String splitName) {
        ProcessInfoData mainInfo = SplitProcessUtils.getMainProcessInfoData(splitName);
        List<ProcessInfoData> subInfoList = SplitProcessUtils.getSubProcessInfoData(splitName);
        List<String> processList = new ArrayList<>();
        for (ProcessInfoData sub : subInfoList) {
            String processName = sub.getProcessName();
            if (!TextUtils.isEmpty(processName) && !processList.contains(processName)) {
                processList.add(processName);
            }
        }
        if (mainInfo != null && !TextUtils.isEmpty(mainInfo.getProcessName())) {
            processList.add(mainInfo.getProcessName());
        }
        SplitLog.d(TAG, "isSplitHasLoaded - processList = " + processList, new Object[0]);
        if (processList.isEmpty()) {
            return false;
        }
        for (String process : processList) {
            if (ProcessUtil.isProcessAlive(context, process)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SplitVersionInfo selectSplitVersionLocalFirst(SplitVersionInfo info) {
        boolean needSelectLocalFirst = SplitApkLoadStrategy.getInstatnce().getLocalFirstStrategyStatus();
        if (!needSelectLocalFirst) {
            return info;
        }
        int from = info.getFrom();
        if (from != 2) {
            return info;
        }
        int versionCode = info.getInstallVersionCode();
        String splitName = info.getSplitInfo().getSplitName();
        File downloadFile = SplitPathManager.require().getDownloadApk(splitName, String.valueOf(versionCode), false);
        String downloadFileMd5 = FileUtil.getMD5(downloadFile);
        if (downloadFile.exists() && downloadFileMd5 != null && downloadFileMd5.equals(info.getSplitDownloadInfo().getMd5())) {
            return info;
        }
        SplitLog.i(TAG, "downloadFile is null or split apk md5 not match", new Object[0]);
        return info.getDefaultVersionInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SplitVersionInfo selectSplitVersion(Context context, ISplitUpdateManager updateManager, SplitInfo info, boolean checkCloudVersion) {
        if (info == null) {
            SplitLog.w(TAG, "select split version error", new Object[0]);
            return null;
        }
        int assetsVersion = getAssetsVersion(info);
        int baseVersionCode = info.getSplitVersionCode();
        String splitName = info.getSplitName();
        SplitVersionInfo versionInfo = new SplitVersionInfo(info);
        SplitUpdateInfo updateInfo = null;
        if (updateManager != null) {
            updateInfo = updateManager.getSplitUpdateInfo(splitName);
        }
        String cloudVersionName = null;
        int cloudVersionCode = -1;
        if (updateInfo != null) {
            cloudVersionName = updateInfo.getVersionName();
            cloudVersionCode = updateInfo.getVersionCode();
            if (!isVersionCodeMatchVersionManager(baseVersionCode, cloudVersionCode)) {
                cloudVersionCode = -1;
            }
        }
        String componentVersion = getComponentVersion(context, info);
        String componentVersionName = null;
        int componentVersionCode = -1;
        if (!TextUtils.isEmpty(componentVersion)) {
            String[] component_version = componentVersion.split("@");
            componentVersionName = component_version[0];
            componentVersionCode = Integer.parseInt(component_version[1]);
            if (!isVersionCodeMatchVersionManager(baseVersionCode, componentVersionCode)) {
                componentVersionCode = -1;
            }
        }
        SplitLog.w(TAG, "cloudVersion = " + cloudVersionCode + ", componentVersion=" + componentVersion, new Object[0]);
        if (!checkCloudVersion) {
            return getDefaultVersion(info, assetsVersion, componentVersionCode, componentVersionName);
        }
        if (cloudVersionCode > assetsVersion) {
            if (cloudVersionCode > componentVersionCode) {
                versionInfo.setFrom(2);
                versionInfo.setInstallVersionName(cloudVersionName);
                versionInfo.setInstalledVersionCode(cloudVersionCode);
                versionInfo.setSplitDownloadInfo(updateInfo);
                SplitVersionInfo defaultInfo = getDefaultVersion(info, assetsVersion, componentVersionCode, componentVersionName);
                versionInfo.setDefaultVersionInfo(defaultInfo);
            } else {
                versionInfo.setFrom(3);
                versionInfo.setInstallVersionName(componentVersionName);
                versionInfo.setInstalledVersionCode(componentVersionCode);
            }
            return versionInfo;
        }
        return getDefaultVersion(info, assetsVersion, componentVersionCode, componentVersionName);
    }

    private static SplitVersionInfo getDefaultVersion(SplitInfo info, int assetsVersion, int componentVersionCode, String componentVersionName) {
        SplitVersionInfo versionInfo = new SplitVersionInfo(info);
        if (assetsVersion == -1 && componentVersionCode == -1) {
            versionInfo.setFrom(-1);
            versionInfo.setInstalledVersionCode(-1);
            versionInfo.setInstallVersionName("");
            return versionInfo;
        }
        if (assetsVersion >= componentVersionCode) {
            versionInfo.setFrom(1);
            versionInfo.setInstallVersionName(info.getSplitVersionName());
            versionInfo.setInstalledVersionCode(assetsVersion);
        } else {
            versionInfo.setFrom(3);
            versionInfo.setInstallVersionName(componentVersionName);
            versionInfo.setInstalledVersionCode(componentVersionCode);
        }
        return versionInfo;
    }

    private static int getAssetsVersion(SplitInfo info) {
        if (!info.isBuiltIn()) {
            return -1;
        }
        int version = info.getSplitVersionCode();
        return version;
    }

    private static String getComponentVersion(Context context, SplitInfo info) {
        HashMap<String, String> infoForSplit = info.getInfoForSplit();
        if (infoForSplit == null) {
            return null;
        }
        String action = infoForSplit.get("componentAction");
        if (TextUtils.isEmpty(action)) {
            return null;
        }
        String version = (String) SharedPreferencesUtil.getInstance(context).get(action + SplitConstants.SUFFIX_COMPONENT_VERSION, "");
        return version;
    }

    private static boolean isVersionCodeMatchVersionManager(int baseVersonCode, int versionCode) {
        boolean match = false;
        int number = baseVersonCode / 100;
        if (versionCode >= number * 100 && versionCode < (number + 1) * 100) {
            match = true;
        }
        if (!match) {
            SplitLog.i(TAG, "split version code not match base version code", new Object[0]);
        }
        return match;
    }
}
