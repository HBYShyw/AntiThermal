package com.oplus.statistics.record;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.oplus.statistics.data.TrackEvent;
import com.oplus.statistics.util.LogUtil;
import com.oplus.statistics.util.Supplier;
import java.util.Map;

/* loaded from: classes2.dex */
public class ContentProviderRecorder implements IRecorder {
    private static final String TAG = "ContentProviderRecorder";
    private static final String URI_STRING = "content://com.oplus.statistics.provider/track_event";
    private static final String URI_SUPPORT = "content://com.oplus.statistics.provider/support";

    private ContentValues getContentValues(TrackEvent trackEvent) {
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, Object> entry : trackEvent.getTrackInfo().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                contentValues.put(key, (String) value);
            } else if (value instanceof Integer) {
                contentValues.put(key, (Integer) value);
            } else if (value instanceof Long) {
                contentValues.put(key, (Long) value);
            } else if (value instanceof Boolean) {
                contentValues.put(key, (Boolean) value);
            }
        }
        return contentValues;
    }

    private static boolean insert(Context context, String str, ContentValues contentValues) {
        Uri parse = Uri.parse(str);
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.record.d
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$insert$1;
                    lambda$insert$1 = ContentProviderRecorder.lambda$insert$1();
                    return lambda$insert$1;
                }
            });
            return false;
        }
        ContentProviderClient contentProviderClient = null;
        try {
            try {
                ContentProviderClient acquireUnstableContentProviderClient = contentResolver.acquireUnstableContentProviderClient(parse);
                if (acquireUnstableContentProviderClient == null) {
                    LogUtil.d(TAG, new Supplier() { // from class: com.oplus.statistics.record.c
                        @Override // com.oplus.statistics.util.Supplier
                        public final Object get() {
                            String lambda$insert$2;
                            lambda$insert$2 = ContentProviderRecorder.lambda$insert$2();
                            return lambda$insert$2;
                        }
                    });
                    if (acquireUnstableContentProviderClient != null) {
                        acquireUnstableContentProviderClient.close();
                    }
                    return false;
                }
                acquireUnstableContentProviderClient.insert(parse, contentValues);
                acquireUnstableContentProviderClient.close();
                return true;
            } catch (Exception e10) {
                LogUtil.e(TAG, new Supplier() { // from class: com.oplus.statistics.record.a
                    @Override // com.oplus.statistics.util.Supplier
                    public final Object get() {
                        String lambda$insert$3;
                        lambda$insert$3 = ContentProviderRecorder.lambda$insert$3(e10);
                        return lambda$insert$3;
                    }
                });
                if (0 != 0) {
                    contentProviderClient.close();
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                contentProviderClient.close();
            }
            throw th;
        }
    }

    public static boolean isSupport(Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("", "");
        boolean insert = insert(context, URI_SUPPORT, contentValues);
        if (!insert) {
            LogUtil.w(TAG, new Supplier() { // from class: com.oplus.statistics.record.b
                @Override // com.oplus.statistics.util.Supplier
                public final Object get() {
                    String lambda$isSupport$0;
                    lambda$isSupport$0 = ContentProviderRecorder.lambda$isSupport$0();
                    return lambda$isSupport$0;
                }
            });
        }
        return insert;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$insert$1() {
        return "get resolver failed.";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$insert$2() {
        return "get provider client failed.";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$insert$3(Exception exc) {
        return "insert exception:" + exc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$isSupport$0() {
        return "not support content provider";
    }

    @Override // com.oplus.statistics.record.IRecorder
    public void addTrackEvent(Context context, TrackEvent trackEvent) {
        insert(context, URI_STRING, getContentValues(trackEvent));
    }
}
