package com.oplus.oms.split.splitdownload;

import android.content.Context;
import com.oplus.oms.split.common.SplitBaseInfoProvider;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInfoManagerImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* loaded from: classes.dex */
public class DownloadUtil {
    private static final String TAG = "DownloadUtil";

    private DownloadUtil() {
    }

    public static List<DownloadRequest> queryAllRequests(Context context) {
        String[] dynamicModules = SplitBaseInfoProvider.getDynamicFeatures();
        if (dynamicModules == null) {
            SplitLog.w(TAG, "dynamic feature is null, ignore", new Object[0]);
            return null;
        }
        List<DownloadRequest> downloadRequestList = new ArrayList<>();
        for (String splitName : dynamicModules) {
            SplitInfo splitInfo = SplitInfoManagerImpl.getInstance().getSplitInfo(context, splitName);
            if (splitInfo == null) {
                SplitLog.w(TAG, "wrong split name: %s, ignore", splitName);
            } else {
                DownloadRequest request = DownloadRequest.newBuilder().url(splitInfo.getMasterApkUrl()).currentVersion(splitInfo.getSplitVersionNameAndVersionCode()).moduleName(splitInfo.getSplitName()).extra(splitInfo.getInfoForSplit()).build();
                downloadRequestList.add(request);
                SplitLog.i(TAG, splitInfo.getSplitVersionNameAndVersionCode() + ",queryRequests:" + splitInfo.getSplitName(), new Object[0]);
            }
        }
        return downloadRequestList;
    }

    public static List<DownloadRequest> buildDownloadRequests(Context context, Collection<String> splitNames) {
        if (splitNames == null) {
            SplitLog.w(TAG, "splitNames is null, ignore", new Object[0]);
            return null;
        }
        List<DownloadRequest> downloadRequestList = new ArrayList<>();
        for (String splitName : splitNames) {
            SplitInfo splitInfo = SplitInfoManagerImpl.getInstance().getSplitInfo(context, splitName);
            if (splitInfo == null) {
                SplitLog.w(TAG, "wrong split name: %s, ignore", splitName);
            } else {
                DownloadRequest request = DownloadRequest.newBuilder().url(splitInfo.getMasterApkUrl()).currentVersion(splitInfo.getSplitVersionNameAndVersionCode()).moduleName(splitInfo.getSplitName()).extra(splitInfo.getInfoForSplit()).build();
                downloadRequestList.add(request);
                SplitLog.i(TAG, splitInfo.getSplitVersionNameAndVersionCode() + ",queryRequests:" + splitInfo.getSplitName(), new Object[0]);
            }
        }
        return downloadRequestList;
    }
}
