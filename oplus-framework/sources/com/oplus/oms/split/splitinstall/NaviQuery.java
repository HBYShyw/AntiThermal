package com.oplus.oms.split.splitinstall;

import android.content.Context;
import android.text.TextUtils;
import com.oplus.navi.oms.OmsPluginInfo;
import com.oplus.oms.split.common.SharedPreferencesUtil;
import com.oplus.oms.split.common.SplitConstants;
import com.oplus.oms.split.common.SplitLog;
import com.oplus.oms.split.splitrequest.SplitInfo;
import com.oplus.oms.split.splitrequest.SplitInfoManager;
import com.oplus.oms.split.splitrequest.SplitInfoManagerImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class NaviQuery extends BaseQuery {
    private static final String TAG = "NaviQuery";

    public NaviQuery(Context context, QueryStatus status) {
        super(context, status);
    }

    @Override // com.oplus.oms.split.splitinstall.BaseQuery
    public void processRunnable() {
        SplitInfoManager manager = SplitInfoManagerImpl.getInstance();
        if (manager == null) {
            SplitLog.w(TAG, "Failed to fetch SplitInfoManager instance!", new Object[0]);
            pushQueryCallBack(3);
            return;
        }
        Collection<SplitInfo> allSplits = manager.getAllSplitInfo(this.mContext);
        if (allSplits == null || allSplits.isEmpty()) {
            SplitLog.w(TAG, "Failed to parse json file of split info!", new Object[0]);
            pushQueryCallBack(3);
            return;
        }
        long lastTime = ((Long) SharedPreferencesUtil.getInstance(this.mContext).get(SplitConstants.COMPONENT_QUERY_TIME, 0L)).longValue();
        long currentTime = System.currentTimeMillis();
        if (SplitConstants.ONE_MONTH + lastTime >= currentTime) {
            SplitLog.w(TAG, "queryFromNavi no time to update", new Object[0]);
            pushQueryCallBack(2);
            return;
        }
        List<String> componentList = new ArrayList<>();
        for (SplitInfo split : allSplits) {
            HashMap<String, String> info = split.getInfoForSplit();
            if (info != null && !info.isEmpty()) {
                String action = info.get("componentAction");
                if (!TextUtils.isEmpty(action)) {
                    componentList.add(action);
                }
            }
        }
        if (componentList.isEmpty()) {
            SplitLog.i(TAG, "no componentList", new Object[0]);
            pushQueryCallBack(3);
        } else {
            putTimeOutCallBack();
            getComponentFeatureInfoList(this.mContext, componentList);
        }
    }

    private void getComponentFeatureInfoList(Context context, List<String> actions) {
        LoadComponentRemote remote;
        boolean z;
        SplitLog.d(TAG, "getComponentFeatureApkPath - action = " + actions, new Object[0]);
        LoadComponentRemote remote2 = new LoadComponentRemote(context);
        Map<String, OmsPluginInfo> pluginMap = remote2.queryMultiPlugin(actions);
        Map<String, Object> componentFeatrue = new HashMap<>();
        if (pluginMap == null || pluginMap.isEmpty()) {
            pushQueryCallBack(3);
            return;
        }
        long time = System.currentTimeMillis();
        componentFeatrue.put(SplitConstants.COMPONENT_QUERY_TIME, Long.valueOf(time));
        for (String action : pluginMap.keySet()) {
            OmsPluginInfo pluginInfo = pluginMap.get(action);
            int versionCode = (int) pluginInfo.getVersionCode();
            String versionName = pluginInfo.getVersionName();
            File dir = new File(pluginInfo.getApkPath());
            File apk = new File(dir, pluginInfo.getApkName());
            if (apk.exists()) {
                String apkPath = apk.getAbsolutePath();
                remote = remote2;
                String version = versionName + "@" + versionCode;
                componentFeatrue.put(action + SplitConstants.SUFFIX_COMPONENT_VERSION, version);
                componentFeatrue.put(action + SplitConstants.SUFFIX_COMPONENT_PATH, apkPath);
                z = false;
            } else {
                remote = remote2;
                z = false;
                SplitLog.w(TAG, "getComponentFeatureApkPath - apk is not existed", new Object[0]);
            }
            remote2 = remote;
        }
        SharedPreferencesUtil.getInstance(context).put(componentFeatrue);
        pushQueryCallBack(2);
    }
}
