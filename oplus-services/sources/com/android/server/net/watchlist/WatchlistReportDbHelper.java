package com.android.server.net.watchlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Slog;
import com.android.internal.util.HexDump;
import com.android.server.net.watchlist.WatchlistLoggingHandler;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
class WatchlistReportDbHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_MODEL = "CREATE TABLE records(app_digest BLOB,cnc_domain TEXT,timestamp INTEGER DEFAULT 0 )";
    private static final String[] DIGEST_DOMAIN_PROJECTION = {"app_digest", "cnc_domain"};
    private static final int IDLE_CONNECTION_TIMEOUT_MS = 30000;
    private static final int INDEX_CNC_DOMAIN = 1;
    private static final int INDEX_DIGEST = 0;
    private static final int INDEX_TIMESTAMP = 2;
    private static final String NAME = "watchlist_report.db";
    private static final String TAG = "WatchlistReportDbHelper";
    private static final int VERSION = 2;
    private static WatchlistReportDbHelper sInstance;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class WhiteListReportContract {
        private static final String APP_DIGEST = "app_digest";
        private static final String CNC_DOMAIN = "cnc_domain";
        private static final String TABLE = "records";
        private static final String TIMESTAMP = "timestamp";

        private WhiteListReportContract() {
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class AggregatedResult {
        final HashMap<String, String> appDigestCNCList;
        final Set<String> appDigestList;
        final String cncDomainVisited;

        public AggregatedResult(Set<String> set, String str, HashMap<String, String> hashMap) {
            this.appDigestList = set;
            this.cncDomainVisited = str;
            this.appDigestCNCList = hashMap;
        }
    }

    static File getSystemWatchlistDbFile() {
        return new File(Environment.getDataSystemDirectory(), NAME);
    }

    private WatchlistReportDbHelper(Context context) {
        super(context, getSystemWatchlistDbFile().getAbsolutePath(), (SQLiteDatabase.CursorFactory) null, 2);
        setIdleConnectionTimeout(30000L);
    }

    public static synchronized WatchlistReportDbHelper getInstance(Context context) {
        synchronized (WatchlistReportDbHelper.class) {
            WatchlistReportDbHelper watchlistReportDbHelper = sInstance;
            if (watchlistReportDbHelper != null) {
                return watchlistReportDbHelper;
            }
            WatchlistReportDbHelper watchlistReportDbHelper2 = new WatchlistReportDbHelper(context);
            sInstance = watchlistReportDbHelper2;
            return watchlistReportDbHelper2;
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE_MODEL);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS records");
        onCreate(sQLiteDatabase);
    }

    public boolean insertNewRecord(byte[] bArr, String str, long j) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_digest", bArr);
            contentValues.put("cnc_domain", str);
            contentValues.put(WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, Long.valueOf(j));
            return writableDatabase.insert("records", null, contentValues) != -1;
        } catch (SQLiteException e) {
            Slog.e(TAG, "Error opening the database to insert a new record", e);
            return false;
        }
    }

    public AggregatedResult getAggregatedRecords(long j) {
        Cursor cursor = null;
        try {
            try {
                Cursor query = getReadableDatabase().query(true, "records", DIGEST_DOMAIN_PROJECTION, "timestamp < ?", new String[]{Long.toString(j)}, null, null, null, null);
                if (query != null) {
                    try {
                        if (query.getCount() > 0) {
                            HashSet hashSet = new HashSet();
                            HashMap hashMap = new HashMap();
                            while (query.moveToNext()) {
                                String hexString = HexDump.toHexString(query.getBlob(0));
                                String string = query.getString(1);
                                hashSet.add(hexString);
                                hashMap.put(hexString, string);
                            }
                            AggregatedResult aggregatedResult = new AggregatedResult(hashSet, null, hashMap);
                            query.close();
                            return aggregatedResult;
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (SQLiteException e) {
            Slog.e(TAG, "Error opening the database", e);
            return null;
        }
    }

    public boolean cleanup(long j) {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            StringBuilder sb = new StringBuilder();
            sb.append("timestamp< ");
            sb.append(j);
            return writableDatabase.delete("records", sb.toString(), null) != 0;
        } catch (SQLiteException e) {
            Slog.e(TAG, "Error opening the database to cleanup", e);
            return false;
        }
    }
}
