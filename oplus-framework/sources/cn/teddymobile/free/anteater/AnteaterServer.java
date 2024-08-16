package cn.teddymobile.free.anteater;

import android.content.Context;
import android.os.Handler;
import cn.teddymobile.free.anteater.logger.Logger;
import cn.teddymobile.free.anteater.resources.RuleResourcesServer;

/* loaded from: classes.dex */
public class AnteaterServer {
    private static final String TAG = AnteaterServer.class.getSimpleName();
    private static volatile AnteaterServer sInstance = null;
    private RuleResourcesServer mResources = null;
    private Handler mWorkHandler = null;

    public static AnteaterServer getInstance() {
        if (sInstance == null) {
            synchronized (AnteaterServer.class) {
                if (sInstance == null) {
                    sInstance = new AnteaterServer();
                }
            }
        }
        return sInstance;
    }

    private AnteaterServer() {
        Logger.d(TAG, "<init>()");
    }

    public void startQuery(Context context, String packageName, RuleResourcesServer.QueryCallback callback) {
        try {
            init(context, callback);
            this.mResources.startQuery(context, packageName, callback);
        } catch (Exception e) {
            Logger.e(TAG, "startQuery failed : " + e.toString());
        }
    }

    public void release() {
        try {
            RuleResourcesServer ruleResourcesServer = this.mResources;
            if (ruleResourcesServer != null) {
                ruleResourcesServer.unregisterObserver();
            }
            Handler handler = this.mWorkHandler;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
        } catch (Exception e) {
            Logger.e(TAG, "release failed : " + e.toString());
        }
    }

    private void init(Context context, RuleResourcesServer.QueryCallback callback) {
        Logger.d(TAG, "init");
        if (this.mWorkHandler == null) {
            this.mWorkHandler = callback.createWorkHandler(context, "anteater", 5);
        }
        if (this.mResources == null) {
            this.mResources = new RuleResourcesServer(this.mWorkHandler);
        }
    }
}
