package com.oplus.oms.split.splitinstall;

import android.content.Context;
import com.oplus.oms.split.common.NetworkUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitdownload.DownloadRequest;
import com.oplus.oms.split.splitdownload.DownloadUtil;
import com.oplus.oms.split.splitdownload.ISplitUpdateManager;
import java.util.List;

/* loaded from: classes.dex */
public class NetQuery extends BaseQuery {
    private static final String TAG = "NetQuery";
    private final ISplitUpdateManager mUpdateManager;

    public NetQuery(Context context, ISplitUpdateManager updateManager, QueryStatus status) {
        super(context, status);
        this.mUpdateManager = updateManager;
    }

    @Override // com.oplus.oms.split.splitinstall.BaseQuery
    public void processRunnable() {
        if (!NetworkUtil.isAllowUseNet(this.mContext)) {
            SplitLog.i(TAG, "net work is unavailable", new Object[0]);
            pushQueryCallBack(3);
            return;
        }
        if (this.mUpdateManager == null) {
            SplitLog.i(TAG, "download is null", new Object[0]);
            pushQueryCallBack(3);
            return;
        }
        long timeOfQueryFromCloud = System.currentTimeMillis();
        int updateTime = NetworkUtil.getUpdateTimeByHours();
        long lastTime = this.mUpdateManager.getLastUpdateTime();
        if ((updateTime * SplitConstants.ONE_HOUR) + lastTime >= timeOfQueryFromCloud) {
            SplitLog.w(TAG, "queryFromNet no time to update", new Object[0]);
            pushQueryCallBack(2);
            return;
        }
        List<DownloadRequest> requests = DownloadUtil.queryAllRequests(this.mContext);
        if (this.mUpdateManager.queryVersionFromCloud(this.mContext, requests, false)) {
            pushQueryCallBack(2);
        } else {
            pushQueryCallBack(3);
        }
    }
}
