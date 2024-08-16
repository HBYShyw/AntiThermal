package com.oplus.oms.split.splitinstall;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.oplus.navi.oms.IOmsPluginLoader;
import com.oplus.navi.oms.OmsConstants;
import com.oplus.navi.oms.OmsPluginInfo;
import com.oplus.oms.split.common.SplitLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes.dex */
public class LoadComponentRemote {
    private static final int CODE_ERROR = -1;
    private static final int CODE_SUCCESS = 0;
    private static final String NAVI_PROVIDER_URI = "content://com.oplus.appplatform.navi";
    private static final String REPLY_CODE = "reply_code";
    private static final String TAG = "LoadComponentRemote";
    private final Context mContext;

    public LoadComponentRemote(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    public Map<String, OmsPluginInfo> queryMultiPlugin(List<String> actions) {
        if (actions == null || actions.size() == 0) {
            SplitLog.d(TAG, "queryMultiPlugin error", new Object[0]);
            return null;
        }
        QueryCallback queryCallback = getQueryCallback(actions);
        Bundle extras = new Bundle();
        extras.putBoolean(OmsConstants.OMS_FROM, true);
        extras.putStringArrayList(OmsConstants.OMS_PLUGIN_ACTION, new ArrayList<>(actions));
        extras.putBinder(OmsConstants.OMS_PLUGIN_CALLBACK, queryCallback.asBinder());
        Bundle result = callProvider(Uri.parse(NAVI_PROVIDER_URI), extras);
        if (result != null) {
            int code = result.getInt(REPLY_CODE, -1);
            if (code == 0) {
                try {
                    queryCallback.await();
                } catch (InterruptedException e) {
                    SplitLog.e(TAG, "Failed to await , message = " + e.getMessage(), new Object[0]);
                }
            }
        }
        return queryCallback.getPluginMap();
    }

    private QueryCallback getQueryCallback(List<String> actions) {
        CountDownLatch signal = new CountDownLatch(1);
        QueryCallback queryCallback = new QueryCallback(signal, actions);
        return queryCallback;
    }

    private Bundle callProvider(Uri uri, Bundle extras) {
        Cursor cursor;
        try {
            cursor = this.mContext.getContentResolver().query(uri, null, extras, null);
        } catch (Exception e) {
            SplitLog.e(TAG, "Failed to query Provider @ " + uri + ", message = " + e.getMessage(), new Object[0]);
        }
        if (cursor != null) {
            try {
                Bundle extras2 = cursor.getExtras();
                if (cursor != null) {
                    cursor.close();
                }
                return extras2;
            } finally {
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class QueryCallback extends IOmsPluginLoader.Stub {
        private List<String> mActions;
        private Map<String, OmsPluginInfo> mPluginMap = new HashMap();
        private final CountDownLatch mSignal;

        QueryCallback(CountDownLatch signal, List<String> actions) {
            this.mSignal = signal;
            this.mActions = actions;
        }

        @Override // com.oplus.navi.oms.IOmsPluginLoader
        public void onQueryCompleted(Bundle pluginInfoBundle) {
            List<String> list;
            if (pluginInfoBundle == null || (list = this.mActions) == null || list.isEmpty()) {
                return;
            }
            try {
                pluginInfoBundle.setClassLoader(OmsPluginInfo.class.getClassLoader());
                for (String action : this.mActions) {
                    OmsPluginInfo pluginInfo = (OmsPluginInfo) pluginInfoBundle.getParcelable(action);
                    if (pluginInfo != null) {
                        this.mPluginMap.put(action, pluginInfo);
                    }
                }
            } finally {
                this.mSignal.countDown();
            }
        }

        public Map<String, OmsPluginInfo> getPluginMap() {
            return this.mPluginMap;
        }

        public void await() throws InterruptedException {
            this.mSignal.await();
        }
    }
}
