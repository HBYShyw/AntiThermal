package com.oplus.statistics.record;

import android.content.Context;
import com.oplus.statistics.data.TrackEvent;
import com.oplus.statistics.util.VersionUtil;

/* loaded from: classes2.dex */
public class ProxyRecorder implements IRecorder {
    private IRecorder mRealRecorder;

    /* loaded from: classes2.dex */
    private static class SingletonHolder {
        private static ProxyRecorder instance = new ProxyRecorder();

        private SingletonHolder() {
        }
    }

    private ProxyRecorder() {
    }

    private void checkRecorder(Context context) {
        if (this.mRealRecorder != null) {
            return;
        }
        if (VersionUtil.isContentProviderRecorder(context)) {
            this.mRealRecorder = new ContentProviderRecorder();
        } else {
            this.mRealRecorder = new ServiceRecorder();
        }
    }

    public static ProxyRecorder getInstance() {
        return SingletonHolder.instance;
    }

    @Override // com.oplus.statistics.record.IRecorder
    public void addTrackEvent(Context context, TrackEvent trackEvent) {
        checkRecorder(context);
        this.mRealRecorder.addTrackEvent(context, trackEvent);
    }
}
