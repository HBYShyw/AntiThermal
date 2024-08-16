package com.oplus.statistics.record;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import com.oplus.statistics.data.TrackEvent;
import com.oplus.statistics.util.Constant;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/* loaded from: classes2.dex */
public class ServiceRecorder implements IRecorder {
    private static final String DCS_PKG_NAME = new String(Base64.decode(Constant.DCS_PKG, 0), StandardCharsets.UTF_8);
    private static final String DCS_SERVICE_NAME = new String(Base64.decode(Constant.DCS_SERVICE, 0), StandardCharsets.UTF_8);
    private static final String TAG = "ServiceRecorder";

    private Intent getIntent(TrackEvent trackEvent) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(DCS_PKG_NAME, DCS_SERVICE_NAME));
        for (Map.Entry<String, Object> entry : trackEvent.getTrackInfo().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            } else if (value instanceof Integer) {
                intent.putExtra(key, (Integer) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            }
        }
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$addTrackEvent$0(Context context) {
        return "add Task failed: bean or context is null. context=" + context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$addTrackEvent$1(Exception exc) {
        return "startService exception=" + exc;
    }

    @Override // com.oplus.statistics.record.IRecorder
    public void addTrackEvent(final Context context, TrackEvent trackEvent) {
        if (trackEvent != null && context != null) {
            try {
                context.startService(getIntent(trackEvent));
                return;
            } catch (Exception e10) {
                LogUtil.w(TAG, new Supplier() { // from class: com.oplus.statistics.record.f
                    @Override // com.oplus.statistics.util.Supplier
                    public final Object get() {
                        String lambda$addTrackEvent$1;
                        lambda$addTrackEvent$1 = ServiceRecorder.lambda$addTrackEvent$1(e10);
                        return lambda$addTrackEvent$1;
                    }
                });
                return;
            }
        }
        LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.record.e
            @Override // com.oplus.statistics.util.Supplier
            public final Object get() {
                String lambda$addTrackEvent$0;
                lambda$addTrackEvent$0 = ServiceRecorder.lambda$addTrackEvent$0(context);
                return lambda$addTrackEvent$0;
            }
        });
    }
}
