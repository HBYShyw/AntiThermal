package com.oplus.oms.split.splitdownload;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.oms.split.common.FileUtil;
import com.oplus.oms.split.common.NetworkUtil;
import com.oplus.oms.split.common.SharedPreferencesUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitdownload.DownloadRequest;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInfoManagerImpl;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class BaseSplitUpdateManager implements ISplitUpdateManager {
    private static final String CLOUD_TIME = "CLOUD_TIME";
    protected static final String KEY_MD5 = "md5";
    protected static final String KEY_NAME = "name";
    protected static final String KEY_SIZE = "size";
    protected static final String KEY_URL = "url";
    protected static final String KEY_VERSION_CODE = "version_code";
    protected static final String KEY_VERSION_NAME = "version_name";
    private static final String TAG = "UpdateManager";
    protected final Context mContext;

    protected BaseSplitUpdateManager(Context context) {
        this.mContext = context;
    }

    protected void saveSplitUpdateInfo(SplitUpdateInfo info) {
        if (info == null) {
            return;
        }
        SharedPreferencesUtil.getInstance(this.mContext).put(toMap(info));
    }

    protected void saveSplitUpdateInfo(Map<String, Object> infoMap) {
        if (infoMap == null || infoMap.isEmpty()) {
            return;
        }
        SharedPreferencesUtil.getInstance(this.mContext).put(infoMap);
    }

    protected long getUpdateTimeByHours() {
        return NetworkUtil.getUpdateTimeByHours();
    }

    @Override // com.oplus.oms.split.splitdownload.ISplitUpdateManager
    public SplitUpdateInfo getSplitUpdateInfo(String splitName) {
        SharedPreferencesUtil util = SharedPreferencesUtil.getInstance(this.mContext);
        String name = (String) util.get(splitName + "name", "");
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        SplitUpdateInfo info = new SplitUpdateInfo();
        try {
            info.setSplitName(name);
            info.setVersionName((String) util.get(splitName + KEY_VERSION_NAME, ""));
            info.setVersionCode(((Integer) util.get(splitName + KEY_VERSION_CODE, -1)).intValue());
            info.setUrl((String) util.get(splitName + "url", ""));
            info.setSize(((Long) util.get(splitName + KEY_SIZE, 0L)).longValue());
            info.setMd5((String) util.get(splitName + KEY_MD5, ""));
        } catch (Exception e) {
            SplitLog.w(TAG, "getSplitUpdateInfo error. split: %s, err: %s", splitName, e.getMessage());
        }
        return info;
    }

    private boolean needReplaceInstalledSplit(String splitName, int downloadVersion) {
        SplitInfo info;
        SplitLog.d(TAG, "needReplaceInstalledSplit - splitName = " + splitName + " , downloadVersion = " + downloadVersion, new Object[0]);
        if (TextUtils.isEmpty(splitName)) {
            return false;
        }
        if (this.mContext != null && (info = SplitInfoManagerImpl.getInstance().getSplitInfo(this.mContext, splitName)) != null && downloadVersion <= info.getSplitVersionCode()) {
            SplitLog.d(TAG, "download version < local version", new Object[0]);
            return false;
        }
        SharedPreferencesUtil util = SharedPreferencesUtil.getInstance(this.mContext);
        int installedVersionCode = ((Integer) util.get(splitName + SplitConstants.SUFFIX_INSTALL_STATUS, -1)).intValue();
        if (installedVersionCode == -1) {
            SplitLog.d(TAG, "splitName = " + splitName + " has not installer", new Object[0]);
            return true;
        }
        File apkFile = SplitPathManager.require().getSplitApkFile(splitName, installedVersionCode, false);
        if (!apkFile.exists()) {
            SplitLog.d(TAG, "splitName = " + splitName + "  apk is delete", new Object[0]);
            return true;
        }
        if (downloadVersion <= installedVersionCode) {
            return false;
        }
        SplitLog.d(TAG, "splitName = " + splitName + "  download > installed", new Object[0]);
        return true;
    }

    private boolean isSplitApkHasDownloaded(String splitName, int version, String md5) {
        File downloadFile = SplitPathManager.require().getDownloadApk(splitName, String.valueOf(version), false);
        if (!downloadFile.exists()) {
            return false;
        }
        String downloadFileMd5 = FileUtil.getMD5(downloadFile);
        if (TextUtils.isEmpty(downloadFileMd5) || !downloadFileMd5.equals(md5)) {
            return false;
        }
        return true;
    }

    private Map<String, Object> toMap(SplitUpdateInfo info) {
        if (info == null) {
            return Collections.emptyMap();
        }
        String keyPrefix = info.getSplitName();
        Map<String, Object> map = new HashMap<>();
        map.put(keyPrefix + "name", info.getSplitName());
        map.put(keyPrefix + KEY_VERSION_NAME, info.getVersionName());
        map.put(keyPrefix + KEY_VERSION_CODE, Integer.valueOf(info.getVersionCode()));
        map.put(keyPrefix + "url", info.getUrl());
        map.put(keyPrefix + KEY_MD5, info.getMd5());
        map.put(keyPrefix + KEY_SIZE, Long.valueOf(info.getSize()));
        return map;
    }

    protected boolean isAllowUseNet() {
        return NetworkUtil.isAllowUseNet(this.mContext);
    }

    @Override // com.oplus.oms.split.splitdownload.ISplitUpdateManager
    public long getLastUpdateTime() {
        return ((Long) SharedPreferencesUtil.getInstance(this.mContext).get(CLOUD_TIME, -1L)).longValue();
    }

    protected void setLastUpdateTime(long time) {
        SharedPreferencesUtil.getInstance(this.mContext).put(CLOUD_TIME, Long.valueOf(time));
    }

    protected List<DownloadRequest> updateDownloadRequest(List<DownloadRequest> old) {
        List<DownloadRequest> requests = new ArrayList<>(old.size());
        SplitPathManager pathManager = SplitPathManager.require();
        for (DownloadRequest req : old) {
            String splitName = req.getModuleName();
            SplitUpdateInfo updateInfo = getSplitUpdateInfo(splitName);
            if (updateInfo == null) {
                SplitLog.w(TAG, splitName + " update indo is null", new Object[0]);
            } else if (!needReplaceInstalledSplit(splitName, updateInfo.getVersionCode())) {
                SplitLog.w(TAG, splitName + " is newest apk", new Object[0]);
            } else if (isSplitApkHasDownloaded(splitName, updateInfo.getVersionCode(), updateInfo.getMd5())) {
                SplitLog.w(TAG, splitName + " has downloaded", new Object[0]);
            } else {
                DownloadRequest newReq = new DownloadRequest.Builder().moduleName(splitName).url(updateInfo.getUrl()).md5(updateInfo.getMd5()).size(updateInfo.getSize()).currentVersion(updateInfo.getVersionName() + "@" + updateInfo.getVersionCode()).extra(req.getExtra()).savePath(pathManager.getSplitDirDownloadDir(splitName, "" + updateInfo.getVersionCode(), true).getAbsolutePath()).saveFileName(pathManager.getSplitApkFileName(splitName)).build();
                requests.add(newReq);
            }
        }
        return requests;
    }
}
