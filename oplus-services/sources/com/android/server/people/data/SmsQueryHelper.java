package com.android.server.people.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Binder;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Slog;
import android.util.SparseIntArray;
import java.util.function.BiConsumer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class SmsQueryHelper {
    private static final SparseIntArray SMS_TYPE_TO_EVENT_TYPE;
    private static final String TAG = "SmsQueryHelper";
    private final Context mContext;
    private final String mCurrentCountryIso;
    private final BiConsumer<String, Event> mEventConsumer;
    private long mLastMessageTimestamp;

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        SMS_TYPE_TO_EVENT_TYPE = sparseIntArray;
        sparseIntArray.put(1, 9);
        sparseIntArray.put(2, 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SmsQueryHelper(Context context, BiConsumer<String, Event> biConsumer) {
        this.mContext = context;
        this.mEventConsumer = biConsumer;
        this.mCurrentCountryIso = Utils.getCurrentCountryIso(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean querySince(long j) {
        Cursor query;
        String[] strArr = {"_id", "date", "type", "address"};
        String[] strArr2 = {Long.toString(j)};
        Binder.allowBlockingForCurrentThread();
        boolean z = false;
        try {
            try {
                query = this.mContext.getContentResolver().query(Telephony.Sms.CONTENT_URI, strArr, "date > ?", strArr2, null);
                try {
                } catch (Throwable th) {
                    if (query != null) {
                        try {
                            query.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (SQLiteException e) {
                Slog.e(TAG, "querySince exception", e);
            }
            if (query == null) {
                Slog.w(TAG, "Cursor is null when querying SMS table.");
                if (query != null) {
                    query.close();
                }
                return false;
            }
            while (query.moveToNext()) {
                query.getString(query.getColumnIndex("_id"));
                long j2 = query.getLong(query.getColumnIndex("date"));
                int i = query.getInt(query.getColumnIndex("type"));
                String formatNumberToE164 = PhoneNumberUtils.formatNumberToE164(query.getString(query.getColumnIndex("address")), this.mCurrentCountryIso);
                this.mLastMessageTimestamp = Math.max(this.mLastMessageTimestamp, j2);
                if (formatNumberToE164 != null && addEvent(formatNumberToE164, j2, i)) {
                    z = true;
                }
            }
            query.close();
            return z;
        } finally {
            Binder.defaultBlockingForCurrentThread();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastMessageTimestamp() {
        return this.mLastMessageTimestamp;
    }

    private boolean addEvent(String str, long j, int i) {
        if (!validateEvent(str, j, i)) {
            return false;
        }
        this.mEventConsumer.accept(str, new Event(j, SMS_TYPE_TO_EVENT_TYPE.get(i)));
        return true;
    }

    private boolean validateEvent(String str, long j, int i) {
        return !TextUtils.isEmpty(str) && j > 0 && SMS_TYPE_TO_EVENT_TYPE.indexOfKey(i) >= 0;
    }
}
