package com.android.server.people.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Slog;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class ContactsQueryHelper {
    private static final String TAG = "ContactsQueryHelper";
    private Uri mContactUri;
    private final Context mContext;
    private boolean mIsStarred;
    private long mLastUpdatedTimestamp;
    private String mPhoneNumber;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContactsQueryHelper(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean query(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Uri parse = Uri.parse(str);
        if ("tel".equals(parse.getScheme())) {
            return queryWithPhoneNumber(parse.getSchemeSpecificPart());
        }
        if ("mailto".equals(parse.getScheme())) {
            return queryWithEmail(parse.getSchemeSpecificPart());
        }
        if (str.startsWith(ContactsContract.Contacts.CONTENT_LOOKUP_URI.toString())) {
            return queryWithUri(parse);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean querySince(long j) {
        String[] strArr = {Long.toString(j)};
        return queryContact(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id", "lookup", "starred", "has_phone_number", "contact_last_updated_timestamp"}, "contact_last_updated_timestamp > ?", strArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Uri getContactUri() {
        return this.mContactUri;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isStarred() {
        return this.mIsStarred;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getPhoneNumber() {
        return this.mPhoneNumber;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLastUpdatedTimestamp() {
        return this.mLastUpdatedTimestamp;
    }

    private boolean queryWithPhoneNumber(String str) {
        return queryWithUri(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(str)));
    }

    private boolean queryWithEmail(String str) {
        return queryWithUri(Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, Uri.encode(str)));
    }

    private boolean queryWithUri(Uri uri) {
        return queryContact(uri, new String[]{"_id", "lookup", "starred", "has_phone_number"}, null, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x00a6 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean queryContact(Uri uri, String[] strArr, String str, String[] strArr2) {
        boolean z;
        boolean z2;
        Cursor query;
        String str2 = null;
        boolean z3 = false;
        try {
            query = this.mContext.getContentResolver().query(uri, strArr, str, strArr2, null);
        } catch (SQLiteException e) {
            e = e;
            z = false;
        } catch (IllegalArgumentException e2) {
            e = e2;
            z = false;
        }
        if (query == null) {
            try {
                Slog.w(TAG, "Cursor is null when querying contact.");
                if (query != null) {
                    query.close();
                }
                return false;
            } catch (Throwable th) {
                th = th;
                z = false;
            }
        } else {
            z2 = false;
            z = false;
            while (query.moveToNext()) {
                try {
                    long j = query.getLong(query.getColumnIndex("_id"));
                    str2 = query.getString(query.getColumnIndex("lookup"));
                    this.mContactUri = ContactsContract.Contacts.getLookupUri(j, str2);
                    this.mIsStarred = query.getInt(query.getColumnIndex("starred")) != 0;
                    z2 = query.getInt(query.getColumnIndex("has_phone_number")) != 0;
                    int columnIndex = query.getColumnIndex("contact_last_updated_timestamp");
                    if (columnIndex >= 0) {
                        this.mLastUpdatedTimestamp = query.getLong(columnIndex);
                    }
                    z = true;
                } catch (Throwable th2) {
                    z3 = z2;
                    th = th2;
                }
            }
            try {
                query.close();
            } catch (SQLiteException e3) {
                e = e3;
                z3 = z2;
                Slog.w("SQLite exception when querying contacts.", e);
                z2 = z3;
                if (z) {
                }
            } catch (IllegalArgumentException e4) {
                e = e4;
                z3 = z2;
                Slog.w("Illegal Argument exception when querying contacts.", e);
                z2 = z3;
                if (z) {
                }
            }
            return (z || str2 == null || !z2) ? z : queryPhoneNumber(str2);
        }
        if (query == null) {
            throw th;
        }
        try {
            try {
                query.close();
                throw th;
            } catch (SQLiteException e5) {
                e = e5;
                Slog.w("SQLite exception when querying contacts.", e);
                z2 = z3;
                if (z) {
                }
            } catch (IllegalArgumentException e6) {
                e = e6;
                Slog.w("Illegal Argument exception when querying contacts.", e);
                z2 = z3;
                if (z) {
                }
            }
        } catch (Throwable th3) {
            th.addSuppressed(th3);
            throw th;
        }
    }

    private boolean queryPhoneNumber(String str) {
        try {
            Cursor query = this.mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"data4"}, "lookup = ?", new String[]{str}, null);
            try {
                if (query == null) {
                    Slog.w(TAG, "Cursor is null when querying contact phone number.");
                    if (query != null) {
                        query.close();
                    }
                    return false;
                }
                while (query.moveToNext()) {
                    int columnIndex = query.getColumnIndex("data4");
                    if (columnIndex >= 0) {
                        this.mPhoneNumber = query.getString(columnIndex);
                    }
                }
                query.close();
                return true;
            } finally {
            }
        } catch (Exception e) {
            Slog.e(TAG, "queryPhoneNumber error:" + e);
            return false;
        }
    }
}
