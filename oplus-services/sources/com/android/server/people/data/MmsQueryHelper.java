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
class MmsQueryHelper {
    private static final long MILLIS_PER_SECONDS = 1000;
    private static final SparseIntArray MSG_BOX_TO_EVENT_TYPE;
    private static final String TAG = "MmsQueryHelper";
    private final Context mContext;
    private String mCurrentCountryIso;
    private final BiConsumer<String, Event> mEventConsumer;
    private long mLastMessageTimestamp;

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        MSG_BOX_TO_EVENT_TYPE = sparseIntArray;
        sparseIntArray.put(1, 9);
        sparseIntArray.put(2, 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MmsQueryHelper(Context context, BiConsumer<String, Event> biConsumer) {
        this.mContext = context;
        this.mEventConsumer = biConsumer;
        this.mCurrentCountryIso = Utils.getCurrentCountryIso(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean querySince(long j) {
        Cursor query;
        String[] strArr = {"_id", "date", "msg_box"};
        String[] strArr2 = {Long.toString(j / 1000)};
        Binder.allowBlockingForCurrentThread();
        boolean z = false;
        try {
            try {
                query = this.mContext.getContentResolver().query(Telephony.Mms.CONTENT_URI, strArr, "date > ?", strArr2, null);
            } catch (SQLiteException e) {
                Slog.e(TAG, "querySince exception", e);
            }
            try {
                if (query == null) {
                    Slog.w(TAG, "Cursor is null when querying MMS table.");
                    if (query != null) {
                        query.close();
                    }
                    return false;
                }
                while (query.moveToNext()) {
                    String string = query.getString(query.getColumnIndex("_id"));
                    long j2 = query.getLong(query.getColumnIndex("date")) * 1000;
                    int i = query.getInt(query.getColumnIndex("msg_box"));
                    this.mLastMessageTimestamp = Math.max(this.mLastMessageTimestamp, j2);
                    String mmsAddress = getMmsAddress(string, i);
                    if (mmsAddress != null && addEvent(mmsAddress, j2, i)) {
                        z = true;
                    }
                }
                query.close();
                return z;
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
        } finally {
            Binder.defaultBlockingForCurrentThread();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastMessageTimestamp() {
        return this.mLastMessageTimestamp;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0072 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0073  */
    /* JADX WARN: Type inference failed for: r1v1, types: [android.content.ContentResolver] */
    /* JADX WARN: Type inference failed for: r2v0, types: [android.net.Uri] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getMmsAddress(String str, int i) {
        String str2;
        Cursor query;
        ?? addrUriForMessage = Telephony.Mms.Addr.getAddrUriForMessage(str);
        try {
            query = this.mContext.getContentResolver().query(addrUriForMessage, new String[]{"address", "type"}, null, null, null);
        } catch (SQLiteException e) {
            e = e;
            addrUriForMessage = 0;
        }
        try {
        } catch (SQLiteException e2) {
            e = e2;
            Slog.e(TAG, "getMmsAddress exception", e);
            str2 = addrUriForMessage;
            if (Telephony.Mms.isPhoneNumber(str2)) {
            }
        }
        if (query == null) {
            try {
                Slog.w(TAG, "Cursor is null when querying MMS address table.");
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th) {
                th = th;
                addrUriForMessage = 0;
            }
        } else {
            addrUriForMessage = 0;
            while (query.moveToNext()) {
                try {
                    int i2 = query.getInt(query.getColumnIndex("type"));
                    if ((i == 1 && i2 == 137) || (i == 2 && i2 == 151)) {
                        addrUriForMessage = query.getString(query.getColumnIndex("address"));
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            query.close();
            str2 = addrUriForMessage;
            if (Telephony.Mms.isPhoneNumber(str2)) {
                return null;
            }
            return PhoneNumberUtils.formatNumberToE164(str2, this.mCurrentCountryIso);
        }
        if (query != null) {
            try {
                query.close();
            } catch (Throwable th3) {
                th.addSuppressed(th3);
            }
        }
        throw th;
    }

    private boolean addEvent(String str, long j, int i) {
        if (!validateEvent(str, j, i)) {
            return false;
        }
        this.mEventConsumer.accept(str, new Event(j, MSG_BOX_TO_EVENT_TYPE.get(i)));
        return true;
    }

    private boolean validateEvent(String str, long j, int i) {
        return !TextUtils.isEmpty(str) && j > 0 && MSG_BOX_TO_EVENT_TYPE.indexOfKey(i) >= 0;
    }
}
