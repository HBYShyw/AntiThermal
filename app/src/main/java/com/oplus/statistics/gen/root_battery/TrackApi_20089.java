package com.oplus.statistics.gen.root_battery;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.oplus.statistics.OTrackConfig;
import com.oplus.statistics.OTrackContext;
import com.oplus.statistics.OplusTrack;
import com.oplus.statistics.data.CommonBean;
import com.oplus.statistics.record.AppLifecycleCallbacks;
import com.oplus.statistics.strategy.BaseTracker;
import java.util.Objects;

/* loaded from: classes2.dex */
public class TrackApi_20089 {
    private static final String APP_ID = "20089";
    private static final String TAG = "OplusTrack_TrackApi_20089";

    /* loaded from: classes2.dex */
    public static class CommonTracker extends BaseTracker {
        private CommonTracker(String str, String str2) {
            super(str, str2);
        }

        public static CommonTracker obtain(String str, String str2) {
            return new CommonTracker(str, str2);
        }

        @Override // com.oplus.statistics.strategy.BaseTracker
        public void commit() {
            OTrackContext oTrackContext = OTrackContext.get(TrackApi_20089.APP_ID);
            Objects.requireNonNull(oTrackContext, "Must call init() first.");
            CommonBean commonBean = new CommonBean(oTrackContext.getContext(), TextUtils.isEmpty(this.mAppId) ? oTrackContext.getAppId() : this.mAppId, this.mLogTag, this.mEventId);
            commonBean.setLogMap(this.mLogMap);
            commonBean.setHeaderFlag(oTrackContext.getConfig().getHeaderFlag());
            OplusTrack.onCommon(commonBean, this.mSendFlag);
        }
    }

    private TrackApi_20089() {
    }

    public static void init(Context context) {
        init(context, null);
    }

    public static void init(Context context, OTrackConfig oTrackConfig) {
        OTrackContext.createIfNeed(APP_ID, context, oTrackConfig);
        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            AppLifecycleCallbacks.getInstance().init((Application) applicationContext);
        }
        OplusTrack.init(context, APP_ID, oTrackConfig);
    }
}
