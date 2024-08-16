package com.oplus.wrapper.app;

import android.os.IBinder;
import android.util.Singleton;

/* loaded from: classes.dex */
public class ActivityClient {
    private static final Singleton<ActivityClient> SINSTANCE = new Singleton<ActivityClient>() { // from class: com.oplus.wrapper.app.ActivityClient.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* renamed from: create, reason: merged with bridge method [inline-methods] */
        public ActivityClient m1141create() {
            ActivityClient activityClient = new ActivityClient();
            activityClient.mActivityClient = android.app.ActivityClient.getInstance();
            return activityClient;
        }
    };
    private android.app.ActivityClient mActivityClient;

    private ActivityClient() {
    }

    public static ActivityClient getInstance() {
        return (ActivityClient) SINSTANCE.get();
    }

    public void invalidateHomeTaskSnapshot(IBinder homeToken) {
        this.mActivityClient.invalidateHomeTaskSnapshot(homeToken);
    }
}
