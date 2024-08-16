package com.oplus.oms.split.splitinstall;

import android.content.Context;
import com.oplus.oms.split.splitdownload.ISplitUpdateManager;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class VersionQuery {
    public static final int FAIL_STATUS = 3;
    public static final int SUCCESS_STATUS = 2;
    public static final String TAG = "VersionQuery";
    private final ThreadPoolExecutor mQueryExecutor;

    private VersionQuery() {
        this.mQueryExecutor = new ThreadPoolExecutor(0, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), new SplitQueryThread());
    }

    /* loaded from: classes.dex */
    private static class InnerHolder {
        private static final VersionQuery sInstance = new VersionQuery();

        private InnerHolder() {
        }
    }

    public static VersionQuery getInstance() {
        return InnerHolder.sInstance;
    }

    public void queryAsync(Context context, ISplitUpdateManager updateManager, QueryStatus status) {
        this.mQueryExecutor.execute(new NaviQuery(context, null));
        this.mQueryExecutor.execute(new NetQuery(context, updateManager, status));
    }

    public void querySync(Context context, ISplitUpdateManager updateManager, QueryStatus status) {
        NaviQuery naviQuery = new NaviQuery(context, null);
        NetQuery netQuery = new NetQuery(context, updateManager, status);
        naviQuery.processRunnable();
        netQuery.processRunnable();
    }
}
