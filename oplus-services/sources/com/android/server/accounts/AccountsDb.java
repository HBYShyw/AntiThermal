package com.android.server.accounts;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AccountsDb implements AutoCloseable {
    private static final String ACCOUNTS_ID = "_id";
    private static final String ACCOUNTS_LAST_AUTHENTICATE_TIME_EPOCH_MILLIS = "last_password_entry_time_millis_epoch";
    private static final String ACCOUNTS_NAME = "name";
    private static final String ACCOUNTS_PASSWORD = "password";
    private static final String ACCOUNTS_PREVIOUS_NAME = "previous_name";
    private static final String ACCOUNTS_TYPE = "type";
    private static final String ACCOUNT_ACCESS_GRANTS = "SELECT name, uid FROM accounts, grants WHERE accounts_id=_id";
    private static final String AUTHTOKENS_ACCOUNTS_ID = "accounts_id";
    private static final String AUTHTOKENS_ID = "_id";
    private static final String AUTHTOKENS_TYPE = "type";
    static final String CE_DATABASE_NAME = "accounts_ce.db";
    private static final int CE_DATABASE_VERSION = 10;
    private static final String CE_DB_PREFIX = "ceDb.";
    private static final String CE_TABLE_ACCOUNTS = "ceDb.accounts";
    private static final String CE_TABLE_AUTHTOKENS = "ceDb.authtokens";
    private static final String CE_TABLE_EXTRAS = "ceDb.extras";
    private static final String COUNT_OF_MATCHING_GRANTS = "SELECT COUNT(*) FROM grants, accounts WHERE accounts_id=_id AND uid=? AND auth_token_type=? AND name=? AND type=?";
    private static final String COUNT_OF_MATCHING_GRANTS_ANY_TOKEN = "SELECT COUNT(*) FROM grants, accounts WHERE accounts_id=_id AND uid=? AND name=? AND type=?";
    private static final String DATABASE_NAME = "accounts.db";
    static String DEBUG_ACTION_ACCOUNT_ADD = "action_account_add";
    static String DEBUG_ACTION_ACCOUNT_REMOVE = "action_account_remove";
    static String DEBUG_ACTION_ACCOUNT_REMOVE_DE = "action_account_remove_de";
    static String DEBUG_ACTION_ACCOUNT_RENAME = "action_account_rename";
    static String DEBUG_ACTION_AUTHENTICATOR_REMOVE = "action_authenticator_remove";
    static String DEBUG_ACTION_CALLED_ACCOUNT_ADD = "action_called_account_add";
    static String DEBUG_ACTION_CALLED_ACCOUNT_REMOVE = "action_called_account_remove";
    static String DEBUG_ACTION_CALLED_ACCOUNT_SESSION_FINISH = "action_called_account_session_finish";
    static String DEBUG_ACTION_CALLED_START_ACCOUNT_ADD = "action_called_start_account_add";
    static String DEBUG_ACTION_CLEAR_PASSWORD = "action_clear_password";
    static String DEBUG_ACTION_SET_PASSWORD = "action_set_password";
    static String DEBUG_ACTION_SYNC_DE_CE_ACCOUNTS = "action_sync_de_ce_accounts";
    private static String DEBUG_TABLE_ACTION_TYPE = "action_type";
    private static String DEBUG_TABLE_CALLER_UID = "caller_uid";
    private static String DEBUG_TABLE_KEY = "primary_key";
    private static String DEBUG_TABLE_TABLE_NAME = "table_name";
    private static String DEBUG_TABLE_TIMESTAMP = "time";
    static final String DE_DATABASE_NAME = "accounts_de.db";
    private static final int DE_DATABASE_VERSION = 3;
    private static final String EXTRAS_ACCOUNTS_ID = "accounts_id";
    private static final String EXTRAS_ID = "_id";
    private static final String EXTRAS_KEY = "key";
    private static final String EXTRAS_VALUE = "value";
    private static final String GRANTS_ACCOUNTS_ID = "accounts_id";
    private static final String GRANTS_AUTH_TOKEN_TYPE = "auth_token_type";
    private static final String GRANTS_GRANTEE_UID = "uid";
    static final int MAX_DEBUG_DB_SIZE = 64;
    private static final String META_KEY = "key";
    private static final String META_KEY_DELIMITER = ":";
    private static final String META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX = "auth_uid_for_type:";
    private static final String META_VALUE = "value";
    private static final int PRE_N_DATABASE_VERSION = 9;
    private static final String SELECTION_ACCOUNTS_ID_BY_ACCOUNT = "accounts_id=(select _id FROM accounts WHERE name=? AND type=?)";
    private static final String SELECTION_META_BY_AUTHENTICATOR_TYPE = "key LIKE ?";
    private static final String SHARED_ACCOUNTS_ID = "_id";
    static final String TABLE_ACCOUNTS = "accounts";
    private static final String TABLE_AUTHTOKENS = "authtokens";
    private static String TABLE_DEBUG = "debug_table";
    private static final String TABLE_EXTRAS = "extras";
    private static final String TABLE_GRANTS = "grants";
    private static final String TABLE_META = "meta";
    static final String TABLE_SHARED_ACCOUNTS = "shared_accounts";
    private static final String TABLE_VISIBILITY = "visibility";
    private static final String TAG = "AccountsDb";
    private static final String VISIBILITY_ACCOUNTS_ID = "accounts_id";
    private static final String VISIBILITY_PACKAGE = "_package";
    private static final String VISIBILITY_VALUE = "value";
    private final Context mContext;
    private final DeDatabaseHelper mDeDatabase;
    private volatile SQLiteStatement mDebugStatementForLogging;
    private final File mPreNDatabaseFile;
    private static final String ACCOUNTS_TYPE_COUNT = "count(type)";
    private static final String[] ACCOUNT_TYPE_COUNT_PROJECTION = {"type", ACCOUNTS_TYPE_COUNT};
    private static final String AUTHTOKENS_AUTHTOKEN = "authtoken";
    private static final String[] COLUMNS_AUTHTOKENS_TYPE_AND_AUTHTOKEN = {"type", AUTHTOKENS_AUTHTOKEN};
    private static final String[] COLUMNS_EXTRAS_KEY_AND_VALUE = {"key", "value"};
    final Object mDebugStatementLock = new Object();
    private volatile long mDebugDbInsertionPoint = -1;

    AccountsDb(DeDatabaseHelper deDatabaseHelper, Context context, File file) {
        this.mDeDatabase = deDatabaseHelper;
        this.mContext = context;
        this.mPreNDatabaseFile = file;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class CeDatabaseHelper extends SQLiteOpenHelper {
        CeDatabaseHelper(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 10);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            Log.i(AccountsDb.TAG, "Creating CE database " + getDatabaseName());
            sQLiteDatabase.execSQL("CREATE TABLE accounts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, type TEXT NOT NULL, password TEXT, UNIQUE(name,type))");
            sQLiteDatabase.execSQL("CREATE TABLE authtokens (  _id INTEGER PRIMARY KEY AUTOINCREMENT,  accounts_id INTEGER NOT NULL, type TEXT NOT NULL,  authtoken TEXT,  UNIQUE (accounts_id,type))");
            sQLiteDatabase.execSQL("CREATE TABLE extras ( _id INTEGER PRIMARY KEY AUTOINCREMENT, accounts_id INTEGER, key TEXT NOT NULL, value TEXT, UNIQUE(accounts_id,key))");
            createAccountsDeletionTrigger(sQLiteDatabase);
        }

        private void createAccountsDeletionTrigger(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(" CREATE TRIGGER accountsDelete DELETE ON accounts BEGIN   DELETE FROM authtokens     WHERE accounts_id=OLD._id ;   DELETE FROM extras     WHERE accounts_id=OLD._id ; END");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.i(AccountsDb.TAG, "Upgrade CE from version " + i + " to version " + i2);
            if (i == 9) {
                if (Log.isLoggable(AccountsDb.TAG, 2)) {
                    Log.v(AccountsDb.TAG, "onUpgrade upgrading to v10");
                }
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS meta");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS shared_accounts");
                sQLiteDatabase.execSQL("DROP TRIGGER IF EXISTS accountsDelete");
                createAccountsDeletionTrigger(sQLiteDatabase);
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS grants");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AccountsDb.TABLE_DEBUG);
                i++;
            }
            if (i != i2) {
                Log.e(AccountsDb.TAG, "failed to upgrade version " + i + " to version " + i2);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.e(AccountsDb.TAG, "onDowngrade: recreate accounts CE table");
            AccountsDb.resetDatabase(sQLiteDatabase);
            onCreate(sQLiteDatabase);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            if (Log.isLoggable(AccountsDb.TAG, 2)) {
                Log.v(AccountsDb.TAG, "opened database accounts_ce.db");
            }
        }

        static CeDatabaseHelper create(Context context, File file, File file2) {
            boolean exists = file2.exists();
            if (Log.isLoggable(AccountsDb.TAG, 2)) {
                Log.v(AccountsDb.TAG, "CeDatabaseHelper.create ceDatabaseFile=" + file2 + " oldDbExists=" + file.exists() + " newDbExists=" + exists);
            }
            boolean migratePreNDbToCe = (exists || !file.exists()) ? false : migratePreNDbToCe(file, file2);
            CeDatabaseHelper ceDatabaseHelper = new CeDatabaseHelper(context, file2.getPath());
            ceDatabaseHelper.getWritableDatabase();
            ceDatabaseHelper.close();
            if (migratePreNDbToCe) {
                Slog.i(AccountsDb.TAG, "Migration complete - removing pre-N db " + file);
                if (!SQLiteDatabase.deleteDatabase(file)) {
                    Slog.e(AccountsDb.TAG, "Cannot remove pre-N db " + file);
                }
            }
            return ceDatabaseHelper;
        }

        private static boolean migratePreNDbToCe(File file, File file2) {
            Slog.i(AccountsDb.TAG, "Moving pre-N DB " + file + " to CE " + file2);
            try {
                FileUtils.copyFileOrThrow(file, file2);
                return true;
            } catch (IOException e) {
                Slog.e(AccountsDb.TAG, "Cannot copy file to " + file2 + " from " + file, e);
                AccountsDb.deleteDbFileWarnIfFailed(file2);
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Cursor findAuthtokenForAllAccounts(String str, String str2) {
        return this.mDeDatabase.getReadableDatabaseUserIsUnlocked().rawQuery("SELECT ceDb.authtokens._id, ceDb.accounts.name, ceDb.authtokens.type FROM ceDb.accounts JOIN ceDb.authtokens ON ceDb.accounts._id = ceDb.authtokens.accounts_id WHERE ceDb.authtokens.authtoken = ? AND ceDb.accounts.type = ?", new String[]{str2, str});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, String> findAuthTokensByAccount(Account account) {
        SQLiteDatabase readableDatabaseUserIsUnlocked = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        HashMap hashMap = new HashMap();
        Cursor query = readableDatabaseUserIsUnlocked.query(CE_TABLE_AUTHTOKENS, COLUMNS_AUTHTOKENS_TYPE_AND_AUTHTOKEN, SELECTION_ACCOUNTS_ID_BY_ACCOUNT, new String[]{account.name, account.type}, null, null, null);
        while (query.moveToNext()) {
            try {
                hashMap.put(query.getString(0), query.getString(1));
            } finally {
                query.close();
            }
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteAuthtokensByAccountIdAndType(long j, String str) {
        return this.mDeDatabase.getWritableDatabaseUserIsUnlocked().delete(CE_TABLE_AUTHTOKENS, "accounts_id=? AND type=?", new String[]{String.valueOf(j), str}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteAuthToken(String str) {
        return this.mDeDatabase.getWritableDatabaseUserIsUnlocked().delete(CE_TABLE_AUTHTOKENS, "_id= ?", new String[]{str}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long insertAuthToken(long j, String str, String str2) {
        SQLiteDatabase writableDatabaseUserIsUnlocked = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accounts_id", Long.valueOf(j));
        contentValues.put("type", str);
        contentValues.put(AUTHTOKENS_AUTHTOKEN, str2);
        return writableDatabaseUserIsUnlocked.insert(CE_TABLE_AUTHTOKENS, AUTHTOKENS_AUTHTOKEN, contentValues);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int updateCeAccountPassword(long j, String str) {
        SQLiteDatabase writableDatabaseUserIsUnlocked = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", str);
        return writableDatabaseUserIsUnlocked.update(CE_TABLE_ACCOUNTS, contentValues, "_id=?", new String[]{String.valueOf(j)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean renameCeAccount(long j, String str) {
        SQLiteDatabase writableDatabaseUserIsUnlocked = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNTS_NAME, str);
        return writableDatabaseUserIsUnlocked.update(CE_TABLE_ACCOUNTS, contentValues, "_id=?", new String[]{String.valueOf(j)}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteAuthTokensByAccountId(long j) {
        return this.mDeDatabase.getWritableDatabaseUserIsUnlocked().delete(CE_TABLE_AUTHTOKENS, "accounts_id=?", new String[]{String.valueOf(j)}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long findExtrasIdByAccountId(long j, String str) {
        Cursor query = this.mDeDatabase.getReadableDatabaseUserIsUnlocked().query(CE_TABLE_EXTRAS, new String[]{"_id"}, "accounts_id=" + j + " AND key=?", new String[]{str}, null, null, null);
        try {
            if (query.moveToNext()) {
                return query.getLong(0);
            }
            query.close();
            return -1L;
        } finally {
            query.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateExtra(long j, String str) {
        SQLiteDatabase writableDatabaseUserIsUnlocked = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", str);
        return writableDatabaseUserIsUnlocked.update(TABLE_EXTRAS, contentValues, "_id=?", new String[]{String.valueOf(j)}) == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long insertExtra(long j, String str, String str2) {
        SQLiteDatabase writableDatabaseUserIsUnlocked = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", str);
        contentValues.put("accounts_id", Long.valueOf(j));
        contentValues.put("value", str2);
        return writableDatabaseUserIsUnlocked.insert(CE_TABLE_EXTRAS, "key", contentValues);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, String> findUserExtrasForAccount(Account account) {
        SQLiteDatabase readableDatabaseUserIsUnlocked = this.mDeDatabase.getReadableDatabaseUserIsUnlocked();
        HashMap hashMap = new HashMap();
        Cursor query = readableDatabaseUserIsUnlocked.query(CE_TABLE_EXTRAS, COLUMNS_EXTRAS_KEY_AND_VALUE, SELECTION_ACCOUNTS_ID_BY_ACCOUNT, new String[]{account.name, account.type}, null, null, null);
        while (query.moveToNext()) {
            try {
                hashMap.put(query.getString(0), query.getString(1));
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
        }
        query.close();
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long findCeAccountId(Account account) {
        Cursor query = this.mDeDatabase.getReadableDatabaseUserIsUnlocked().query(CE_TABLE_ACCOUNTS, new String[]{"_id"}, "name=? AND type=?", new String[]{account.name, account.type}, null, null, null);
        try {
            if (!query.moveToNext()) {
                query.close();
                return -1L;
            }
            long j = query.getLong(0);
            query.close();
            return j;
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String findAccountPasswordByNameAndType(String str, String str2) {
        Cursor query = this.mDeDatabase.getReadableDatabaseUserIsUnlocked().query(CE_TABLE_ACCOUNTS, new String[]{"password"}, "name=? AND type=?", new String[]{str, str2}, null, null, null);
        try {
            if (!query.moveToNext()) {
                query.close();
                return null;
            }
            String string = query.getString(0);
            query.close();
            return string;
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long insertCeAccount(Account account, String str) {
        SQLiteDatabase writableDatabaseUserIsUnlocked = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNTS_NAME, account.name);
        contentValues.put("type", account.type);
        contentValues.put("password", str);
        return writableDatabaseUserIsUnlocked.insert(CE_TABLE_ACCOUNTS, ACCOUNTS_NAME, contentValues);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class DeDatabaseHelper extends SQLiteOpenHelper {
        private volatile boolean mCeAttached;
        private final int mUserId;

        private DeDatabaseHelper(Context context, int i, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 3);
            this.mUserId = i;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            Log.i(AccountsDb.TAG, "Creating DE database for user " + this.mUserId);
            sQLiteDatabase.execSQL("CREATE TABLE accounts ( _id INTEGER PRIMARY KEY, name TEXT NOT NULL, type TEXT NOT NULL, previous_name TEXT, last_password_entry_time_millis_epoch INTEGER DEFAULT 0, UNIQUE(name,type))");
            sQLiteDatabase.execSQL("CREATE TABLE meta ( key TEXT PRIMARY KEY NOT NULL, value TEXT)");
            createGrantsTable(sQLiteDatabase);
            createSharedAccountsTable(sQLiteDatabase);
            createAccountsDeletionTrigger(sQLiteDatabase);
            createDebugTable(sQLiteDatabase);
            createAccountsVisibilityTable(sQLiteDatabase);
            createAccountsDeletionVisibilityCleanupTrigger(sQLiteDatabase);
        }

        private void createSharedAccountsTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE shared_accounts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, type TEXT NOT NULL, UNIQUE(name,type))");
        }

        private void createAccountsDeletionTrigger(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(" CREATE TRIGGER accountsDelete DELETE ON accounts BEGIN   DELETE FROM grants     WHERE accounts_id=OLD._id ; END");
        }

        private void createGrantsTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE grants (  accounts_id INTEGER NOT NULL, auth_token_type STRING NOT NULL,  uid INTEGER NOT NULL,  UNIQUE (accounts_id,auth_token_type,uid))");
        }

        private void createAccountsVisibilityTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE visibility ( accounts_id INTEGER NOT NULL, _package TEXT NOT NULL, value INTEGER, PRIMARY KEY(accounts_id,_package))");
        }

        static void createDebugTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE " + AccountsDb.TABLE_DEBUG + " ( _id INTEGER," + AccountsDb.DEBUG_TABLE_ACTION_TYPE + " TEXT NOT NULL, " + AccountsDb.DEBUG_TABLE_TIMESTAMP + " DATETIME," + AccountsDb.DEBUG_TABLE_CALLER_UID + " INTEGER NOT NULL," + AccountsDb.DEBUG_TABLE_TABLE_NAME + " TEXT NOT NULL," + AccountsDb.DEBUG_TABLE_KEY + " INTEGER PRIMARY KEY)");
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE INDEX timestamp_index ON ");
            sb.append(AccountsDb.TABLE_DEBUG);
            sb.append(" (");
            sb.append(AccountsDb.DEBUG_TABLE_TIMESTAMP);
            sb.append(")");
            sQLiteDatabase.execSQL(sb.toString());
        }

        private void createAccountsDeletionVisibilityCleanupTrigger(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(" CREATE TRIGGER accountsDeleteVisibility DELETE ON accounts BEGIN   DELETE FROM visibility     WHERE accounts_id=OLD._id ; END");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.i(AccountsDb.TAG, "upgrade from version " + i + " to version " + i2);
            if (i == 1) {
                createAccountsVisibilityTable(sQLiteDatabase);
                createAccountsDeletionVisibilityCleanupTrigger(sQLiteDatabase);
                i = 3;
            }
            if (i == 2) {
                sQLiteDatabase.execSQL("DROP TRIGGER IF EXISTS accountsDeleteVisibility");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS visibility");
                createAccountsVisibilityTable(sQLiteDatabase);
                createAccountsDeletionVisibilityCleanupTrigger(sQLiteDatabase);
                i++;
            }
            if (i != i2) {
                Log.e(AccountsDb.TAG, "failed to upgrade version " + i + " to version " + i2);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.e(AccountsDb.TAG, "onDowngrade: recreate accounts DE table");
            AccountsDb.resetDatabase(sQLiteDatabase);
            onCreate(sQLiteDatabase);
        }

        public SQLiteDatabase getReadableDatabaseUserIsUnlocked() {
            if (!this.mCeAttached) {
                Log.wtf(AccountsDb.TAG, "getReadableDatabaseUserIsUnlocked called while user " + this.mUserId + " is still locked. CE database is not yet available.", new Throwable());
            }
            return super.getReadableDatabase();
        }

        public SQLiteDatabase getWritableDatabaseUserIsUnlocked() {
            if (!this.mCeAttached) {
                Log.wtf(AccountsDb.TAG, "getWritableDatabaseUserIsUnlocked called while user " + this.mUserId + " is still locked. CE database is not yet available.", new Throwable());
            }
            return super.getWritableDatabase();
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            if (Log.isLoggable(AccountsDb.TAG, 2)) {
                Log.v(AccountsDb.TAG, "opened database accounts_de.db");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void migratePreNDbToDe(File file) {
            Log.i(AccountsDb.TAG, "Migrate pre-N database to DE preNDbFile=" + file);
            SQLiteDatabase writableDatabase = getWritableDatabase();
            writableDatabase.execSQL("ATTACH DATABASE '" + file.getPath() + "' AS preNDb");
            writableDatabase.beginTransaction();
            writableDatabase.execSQL("INSERT INTO accounts(_id,name,type, previous_name, last_password_entry_time_millis_epoch) SELECT _id,name,type, previous_name, last_password_entry_time_millis_epoch FROM preNDb.accounts");
            writableDatabase.execSQL("INSERT INTO shared_accounts(_id,name,type) SELECT _id,name,type FROM preNDb.shared_accounts");
            writableDatabase.execSQL("INSERT INTO " + AccountsDb.TABLE_DEBUG + "(_id," + AccountsDb.DEBUG_TABLE_ACTION_TYPE + "," + AccountsDb.DEBUG_TABLE_TIMESTAMP + "," + AccountsDb.DEBUG_TABLE_CALLER_UID + "," + AccountsDb.DEBUG_TABLE_TABLE_NAME + "," + AccountsDb.DEBUG_TABLE_KEY + ") SELECT _id," + AccountsDb.DEBUG_TABLE_ACTION_TYPE + "," + AccountsDb.DEBUG_TABLE_TIMESTAMP + "," + AccountsDb.DEBUG_TABLE_CALLER_UID + "," + AccountsDb.DEBUG_TABLE_TABLE_NAME + "," + AccountsDb.DEBUG_TABLE_KEY + " FROM preNDb." + AccountsDb.TABLE_DEBUG);
            writableDatabase.execSQL("INSERT INTO grants(accounts_id,auth_token_type,uid) SELECT accounts_id,auth_token_type,uid FROM preNDb.grants");
            writableDatabase.execSQL("INSERT INTO meta(key,value) SELECT key,value FROM preNDb.meta");
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.execSQL("DETACH DATABASE preNDb");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteDeAccount(long j) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("_id=");
        sb.append(j);
        return writableDatabase.delete(TABLE_ACCOUNTS, sb.toString(), null) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long insertSharedAccount(Account account) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNTS_NAME, account.name);
        contentValues.put("type", account.type);
        return writableDatabase.insert(TABLE_SHARED_ACCOUNTS, ACCOUNTS_NAME, contentValues);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteSharedAccount(Account account) {
        return this.mDeDatabase.getWritableDatabase().delete(TABLE_SHARED_ACCOUNTS, "name=? AND type=?", new String[]{account.name, account.type}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int renameSharedAccount(Account account, String str) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNTS_NAME, str);
        return writableDatabase.update(TABLE_SHARED_ACCOUNTS, contentValues, "name=? AND type=?", new String[]{account.name, account.type});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Account> getSharedAccounts() {
        SQLiteDatabase readableDatabase = this.mDeDatabase.getReadableDatabase();
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = readableDatabase.query(TABLE_SHARED_ACCOUNTS, new String[]{ACCOUNTS_NAME, "type"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(ACCOUNTS_NAME);
                int columnIndex2 = cursor.getColumnIndex("type");
                do {
                    arrayList.add(new Account(cursor.getString(columnIndex), cursor.getString(columnIndex2)));
                } while (cursor.moveToNext());
            }
            return arrayList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long findSharedAccountId(Account account) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_SHARED_ACCOUNTS, new String[]{"_id"}, "name=? AND type=?", new String[]{account.name, account.type}, null, null, null);
        try {
            if (query.moveToNext()) {
                return query.getLong(0);
            }
            query.close();
            return -1L;
        } finally {
            query.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long findAccountLastAuthenticatedTime(Account account) {
        return DatabaseUtils.longForQuery(this.mDeDatabase.getReadableDatabase(), "SELECT last_password_entry_time_millis_epoch FROM accounts WHERE name=? AND type=?", new String[]{account.name, account.type});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateAccountLastAuthenticatedTime(Account account) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNTS_LAST_AUTHENTICATE_TIME_EPOCH_MILLIS, Long.valueOf(System.currentTimeMillis()));
        return writableDatabase.update(TABLE_ACCOUNTS, contentValues, "name=? AND type=?", new String[]{account.name, account.type}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDeAccountsTable(PrintWriter printWriter) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_ACCOUNTS, ACCOUNT_TYPE_COUNT_PROJECTION, null, null, "type", null, null);
        while (query.moveToNext()) {
            try {
                printWriter.println(query.getString(0) + "," + query.getString(1));
            } catch (Throwable th) {
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        }
        query.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long findDeAccountId(Account account) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_ACCOUNTS, new String[]{"_id"}, "name=? AND type=?", new String[]{account.name, account.type}, null, null, null);
        try {
            if (!query.moveToNext()) {
                query.close();
                return -1L;
            }
            long j = query.getLong(0);
            query.close();
            return j;
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<Long, Account> findAllDeAccounts() {
        SQLiteDatabase readableDatabase = this.mDeDatabase.getReadableDatabase();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Cursor query = readableDatabase.query(TABLE_ACCOUNTS, new String[]{"_id", "type", ACCOUNTS_NAME}, null, null, null, null, "_id");
        while (query.moveToNext()) {
            try {
                long j = query.getLong(0);
                linkedHashMap.put(Long.valueOf(j), new Account(query.getString(2), query.getString(1)));
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
        }
        query.close();
        return linkedHashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String findDeAccountPreviousName(Account account) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_ACCOUNTS, new String[]{ACCOUNTS_PREVIOUS_NAME}, "name=? AND type=?", new String[]{account.name, account.type}, null, null, null);
        try {
            if (!query.moveToNext()) {
                query.close();
                return null;
            }
            String string = query.getString(0);
            query.close();
            return string;
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
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long insertDeAccount(Account account, long j) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", Long.valueOf(j));
        contentValues.put(ACCOUNTS_NAME, account.name);
        contentValues.put("type", account.type);
        contentValues.put(ACCOUNTS_LAST_AUTHENTICATE_TIME_EPOCH_MILLIS, Long.valueOf(System.currentTimeMillis()));
        return writableDatabase.insert(TABLE_ACCOUNTS, ACCOUNTS_NAME, contentValues);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean renameDeAccount(long j, String str, String str2) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNTS_NAME, str);
        contentValues.put(ACCOUNTS_PREVIOUS_NAME, str2);
        return writableDatabase.update(TABLE_ACCOUNTS, contentValues, "_id=?", new String[]{String.valueOf(j)}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteGrantsByAccountIdAuthTokenTypeAndUid(long j, String str, long j2) {
        return this.mDeDatabase.getWritableDatabase().delete(TABLE_GRANTS, "accounts_id=? AND auth_token_type=? AND uid=?", new String[]{String.valueOf(j), str, String.valueOf(j2)}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Integer> findAllUidGrants() {
        ArrayList arrayList = new ArrayList();
        try {
            Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_GRANTS, new String[]{GRANTS_GRANTEE_UID}, null, null, GRANTS_GRANTEE_UID, null, null);
            while (query.moveToNext()) {
                try {
                    arrayList.add(Integer.valueOf(query.getInt(0)));
                } finally {
                    query.close();
                }
            }
            return arrayList;
        } catch (SQLiteException e) {
            Log.e(TAG, "Fail to open grants table " + e);
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long findMatchingGrantsCount(int i, String str, Account account) {
        return DatabaseUtils.longForQuery(this.mDeDatabase.getReadableDatabase(), COUNT_OF_MATCHING_GRANTS, new String[]{String.valueOf(i), str, account.name, account.type});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long findMatchingGrantsCountAnyToken(int i, Account account) {
        return DatabaseUtils.longForQuery(this.mDeDatabase.getReadableDatabase(), COUNT_OF_MATCHING_GRANTS_ANY_TOKEN, new String[]{String.valueOf(i), account.name, account.type});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long insertGrant(long j, String str, int i) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accounts_id", Long.valueOf(j));
        contentValues.put(GRANTS_AUTH_TOKEN_TYPE, str);
        contentValues.put(GRANTS_GRANTEE_UID, Integer.valueOf(i));
        return writableDatabase.insert(TABLE_GRANTS, "accounts_id", contentValues);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteGrantsByUid(int i) {
        return this.mDeDatabase.getWritableDatabase().delete(TABLE_GRANTS, "uid=?", new String[]{Integer.toString(i)}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setAccountVisibility(long j, String str, int i) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accounts_id", String.valueOf(j));
        contentValues.put(VISIBILITY_PACKAGE, str);
        contentValues.put("value", String.valueOf(i));
        return writableDatabase.replace(TABLE_VISIBILITY, "value", contentValues) != -1;
    }

    Integer findAccountVisibility(Account account, String str) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_VISIBILITY, new String[]{"value"}, "accounts_id=(select _id FROM accounts WHERE name=? AND type=?) AND _package=? ", new String[]{account.name, account.type, str}, null, null, null);
        try {
            if (query.moveToNext()) {
                return Integer.valueOf(query.getInt(0));
            }
            query.close();
            return null;
        } finally {
            query.close();
        }
    }

    Integer findAccountVisibility(long j, String str) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_VISIBILITY, new String[]{"value"}, "accounts_id=? AND _package=? ", new String[]{String.valueOf(j), str}, null, null, null);
        try {
            if (query.moveToNext()) {
                return Integer.valueOf(query.getInt(0));
            }
            query.close();
            return null;
        } finally {
            query.close();
        }
    }

    Account findDeAccountByAccountId(long j) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_ACCOUNTS, new String[]{ACCOUNTS_NAME, "type"}, "_id=? ", new String[]{String.valueOf(j)}, null, null, null);
        try {
            if (query.moveToNext()) {
                return new Account(query.getString(0), query.getString(1));
            }
            query.close();
            return null;
        } finally {
            query.close();
        }
    }

    Map<String, Integer> findAllVisibilityValuesForAccount(Account account) {
        SQLiteDatabase readableDatabase = this.mDeDatabase.getReadableDatabase();
        HashMap hashMap = new HashMap();
        Cursor query = readableDatabase.query(TABLE_VISIBILITY, new String[]{VISIBILITY_PACKAGE, "value"}, SELECTION_ACCOUNTS_ID_BY_ACCOUNT, new String[]{account.name, account.type}, null, null, null);
        while (query.moveToNext()) {
            try {
                hashMap.put(query.getString(0), Integer.valueOf(query.getInt(1)));
            } finally {
                query.close();
            }
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<Account, Map<String, Integer>> findAllVisibilityValues() {
        SQLiteDatabase readableDatabase = this.mDeDatabase.getReadableDatabase();
        HashMap hashMap = new HashMap();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT visibility._package, visibility.value, accounts.name, accounts.type FROM visibility JOIN accounts ON accounts._id = visibility.accounts_id", null);
        while (rawQuery.moveToNext()) {
            try {
                String string = rawQuery.getString(0);
                Integer valueOf = Integer.valueOf(rawQuery.getInt(1));
                Account account = new Account(rawQuery.getString(2), rawQuery.getString(3));
                Map map = (Map) hashMap.get(account);
                if (map == null) {
                    map = new HashMap();
                    hashMap.put(account, map);
                }
                map.put(string, valueOf);
            } finally {
                rawQuery.close();
            }
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteAccountVisibilityForPackage(String str) {
        return this.mDeDatabase.getWritableDatabase().delete(TABLE_VISIBILITY, "_package=? ", new String[]{str}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long insertOrReplaceMetaAuthTypeAndUid(String str, int i) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX + str);
        contentValues.put("value", Integer.valueOf(i));
        return writableDatabase.insertWithOnConflict(TABLE_META, null, contentValues, 5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, Integer> findMetaAuthUid() {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_META, new String[]{"key", "value"}, SELECTION_META_BY_AUTHENTICATOR_TYPE, new String[]{"auth_uid_for_type:%"}, null, null, "key");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        while (query.moveToNext()) {
            try {
                String str = TextUtils.split(query.getString(0), META_KEY_DELIMITER)[1];
                String string = query.getString(1);
                if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(string)) {
                    linkedHashMap.put(str, Integer.valueOf(Integer.parseInt(query.getString(1))));
                }
                Slog.e(TAG, "Auth type empty: " + TextUtils.isEmpty(str) + ", uid empty: " + TextUtils.isEmpty(string));
            } finally {
                query.close();
            }
        }
        return linkedHashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteMetaByAuthTypeAndUid(String str, int i) {
        SQLiteDatabase writableDatabase = this.mDeDatabase.getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append(META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX);
        sb.append(str);
        return writableDatabase.delete(TABLE_META, "key=? AND value=?", new String[]{sb.toString(), String.valueOf(i)}) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Pair<String, Integer>> findAllAccountGrants() {
        Cursor rawQuery = this.mDeDatabase.getReadableDatabase().rawQuery(ACCOUNT_ACCESS_GRANTS, null);
        if (rawQuery != null) {
            try {
                if (rawQuery.moveToFirst()) {
                    ArrayList arrayList = new ArrayList();
                    do {
                        arrayList.add(Pair.create(rawQuery.getString(0), Integer.valueOf(rawQuery.getInt(1))));
                    } while (rawQuery.moveToNext());
                    rawQuery.close();
                    return arrayList;
                }
            } catch (Throwable th) {
                if (rawQuery != null) {
                    try {
                        rawQuery.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        List<Pair<String, Integer>> emptyList = Collections.emptyList();
        if (rawQuery != null) {
            rawQuery.close();
        }
        return emptyList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class PreNDatabaseHelper extends SQLiteOpenHelper {
        private final Context mContext;
        private final int mUserId;

        PreNDatabaseHelper(Context context, int i, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 9);
            this.mContext = context;
            this.mUserId = i;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            throw new IllegalStateException("Legacy database cannot be created - only upgraded!");
        }

        private void createSharedAccountsTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE shared_accounts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, type TEXT NOT NULL, UNIQUE(name,type))");
        }

        private void addLastSuccessfullAuthenticatedTimeColumn(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("ALTER TABLE accounts ADD COLUMN last_password_entry_time_millis_epoch DEFAULT 0");
        }

        private void addOldAccountNameColumn(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("ALTER TABLE accounts ADD COLUMN previous_name");
        }

        private void addDebugTable(SQLiteDatabase sQLiteDatabase) {
            DeDatabaseHelper.createDebugTable(sQLiteDatabase);
        }

        private void createAccountsDeletionTrigger(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(" CREATE TRIGGER accountsDelete DELETE ON accounts BEGIN   DELETE FROM authtokens     WHERE accounts_id=OLD._id ;   DELETE FROM extras     WHERE accounts_id=OLD._id ;   DELETE FROM grants     WHERE accounts_id=OLD._id ; END");
        }

        private void createGrantsTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE grants (  accounts_id INTEGER NOT NULL, auth_token_type STRING NOT NULL,  uid INTEGER NOT NULL,  UNIQUE (accounts_id,auth_token_type,uid))");
        }

        static long insertMetaAuthTypeAndUid(SQLiteDatabase sQLiteDatabase, String str, int i) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("key", AccountsDb.META_KEY_FOR_AUTHENTICATOR_UID_FOR_TYPE_PREFIX + str);
            contentValues.put("value", Integer.valueOf(i));
            return sQLiteDatabase.insert(AccountsDb.TABLE_META, null, contentValues);
        }

        private void populateMetaTableWithAuthTypeAndUID(SQLiteDatabase sQLiteDatabase, Map<String, Integer> map) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                insertMetaAuthTypeAndUid(sQLiteDatabase, entry.getKey(), entry.getValue().intValue());
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.e(AccountsDb.TAG, "upgrade from version " + i + " to version " + i2);
            if (i == 1) {
                i++;
            }
            if (i == 2) {
                createGrantsTable(sQLiteDatabase);
                sQLiteDatabase.execSQL("DROP TRIGGER accountsDelete");
                createAccountsDeletionTrigger(sQLiteDatabase);
                i++;
            }
            if (i == 3) {
                sQLiteDatabase.execSQL("UPDATE accounts SET type = 'com.google' WHERE type == 'com.google.GAIA'");
                i++;
            }
            if (i == 4) {
                createSharedAccountsTable(sQLiteDatabase);
                i++;
            }
            if (i == 5) {
                addOldAccountNameColumn(sQLiteDatabase);
                i++;
            }
            if (i == 6) {
                addLastSuccessfullAuthenticatedTimeColumn(sQLiteDatabase);
                i++;
            }
            if (i == 7) {
                addDebugTable(sQLiteDatabase);
                i++;
            }
            if (i == 8) {
                populateMetaTableWithAuthTypeAndUID(sQLiteDatabase, AccountManagerService.getAuthenticatorTypeAndUIDForUser(this.mContext, this.mUserId));
                i++;
            }
            if (i != i2) {
                Log.e(AccountsDb.TAG, "failed to upgrade version " + i + " to version " + i2);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            if (Log.isLoggable(AccountsDb.TAG, 2)) {
                Log.v(AccountsDb.TAG, "opened database accounts.db");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Account> findCeAccountsNotInDe() {
        Cursor rawQuery = this.mDeDatabase.getReadableDatabaseUserIsUnlocked().rawQuery("SELECT name,type FROM ceDb.accounts WHERE NOT EXISTS  (SELECT _id FROM accounts WHERE _id=ceDb.accounts._id )", null);
        try {
            ArrayList arrayList = new ArrayList(rawQuery.getCount());
            while (rawQuery.moveToNext()) {
                arrayList.add(new Account(rawQuery.getString(0), rawQuery.getString(1)));
            }
            return arrayList;
        } finally {
            rawQuery.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean deleteCeAccount(long j) {
        SQLiteDatabase writableDatabaseUserIsUnlocked = this.mDeDatabase.getWritableDatabaseUserIsUnlocked();
        StringBuilder sb = new StringBuilder();
        sb.append("_id=");
        sb.append(j);
        return writableDatabaseUserIsUnlocked.delete(CE_TABLE_ACCOUNTS, sb.toString(), null) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCeDatabaseAttached() {
        return this.mDeDatabase.mCeAttached;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void beginTransaction() {
        this.mDeDatabase.getWritableDatabase().beginTransaction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTransactionSuccessful() {
        this.mDeDatabase.getWritableDatabase().setTransactionSuccessful();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void endTransaction() {
        this.mDeDatabase.getWritableDatabase().endTransaction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attachCeDatabase(File file) {
        CeDatabaseHelper.create(this.mContext, this.mPreNDatabaseFile, file);
        this.mDeDatabase.getWritableDatabase().execSQL("ATTACH DATABASE '" + file.getPath() + "' AS ceDb");
        this.mDeDatabase.mCeAttached = true;
    }

    long calculateDebugTableInsertionPoint() {
        try {
            SQLiteDatabase readableDatabase = this.mDeDatabase.getReadableDatabase();
            int longForQuery = (int) DatabaseUtils.longForQuery(readableDatabase, "SELECT COUNT(*) FROM " + TABLE_DEBUG, null);
            if (longForQuery < 64) {
                return longForQuery;
            }
            return DatabaseUtils.longForQuery(readableDatabase, "SELECT " + DEBUG_TABLE_KEY + " FROM " + TABLE_DEBUG + " ORDER BY " + DEBUG_TABLE_TIMESTAMP + "," + DEBUG_TABLE_KEY + " LIMIT 1", null);
        } catch (SQLiteException e) {
            Log.e(TAG, "Failed to open debug table" + e);
            return -1L;
        }
    }

    SQLiteStatement compileSqlStatementForLogging() {
        return this.mDeDatabase.getWritableDatabase().compileStatement("INSERT OR REPLACE INTO " + TABLE_DEBUG + " VALUES (?,?,?,?,?,?)");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SQLiteStatement getStatementForLogging() {
        if (this.mDebugStatementForLogging != null) {
            return this.mDebugStatementForLogging;
        }
        try {
            this.mDebugStatementForLogging = compileSqlStatementForLogging();
            return this.mDebugStatementForLogging;
        } catch (SQLiteException e) {
            Log.e(TAG, "Failed to open debug table" + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeDebugStatement() {
        synchronized (this.mDebugStatementLock) {
            if (this.mDebugStatementForLogging != null) {
                this.mDebugStatementForLogging.close();
                this.mDebugStatementForLogging = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long reserveDebugDbInsertionPoint() {
        if (this.mDebugDbInsertionPoint == -1) {
            this.mDebugDbInsertionPoint = calculateDebugTableInsertionPoint();
            return this.mDebugDbInsertionPoint;
        }
        this.mDebugDbInsertionPoint = (this.mDebugDbInsertionPoint + 1) % 64;
        return this.mDebugDbInsertionPoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebugTable(PrintWriter printWriter) {
        Cursor query = this.mDeDatabase.getReadableDatabase().query(TABLE_DEBUG, null, null, null, null, null, DEBUG_TABLE_TIMESTAMP);
        printWriter.println("AccountId, Action_Type, timestamp, UID, TableName, Key");
        printWriter.println("Accounts History");
        while (query.moveToNext()) {
            try {
                printWriter.println(query.getString(0) + "," + query.getString(1) + "," + query.getString(2) + "," + query.getString(3) + "," + query.getString(4) + "," + query.getString(5));
            } finally {
                query.close();
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mDeDatabase.close();
    }

    static void deleteDbFileWarnIfFailed(File file) {
        if (SQLiteDatabase.deleteDatabase(file)) {
            return;
        }
        Log.w(TAG, "Database at " + file + " was not deleted successfully");
    }

    public static AccountsDb create(Context context, int i, File file, File file2) {
        boolean exists = file2.exists();
        DeDatabaseHelper deDatabaseHelper = new DeDatabaseHelper(context, i, file2.getPath());
        if (!exists && file.exists()) {
            PreNDatabaseHelper preNDatabaseHelper = new PreNDatabaseHelper(context, i, file.getPath());
            preNDatabaseHelper.getWritableDatabase();
            preNDatabaseHelper.close();
            deDatabaseHelper.migratePreNDbToDe(file);
        }
        return new AccountsDb(deDatabaseHelper, context, file);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetDatabase(SQLiteDatabase sQLiteDatabase) {
        Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type ='table'", null);
        while (rawQuery.moveToNext()) {
            try {
                String string = rawQuery.getString(0);
                if (!"android_metadata".equals(string) && !"sqlite_sequence".equals(string)) {
                    sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + string);
                }
            } finally {
            }
        }
        rawQuery.close();
        rawQuery = sQLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type ='trigger'", null);
        while (rawQuery.moveToNext()) {
            try {
                sQLiteDatabase.execSQL("DROP TRIGGER IF EXISTS " + rawQuery.getString(0));
            } finally {
            }
        }
        rawQuery.close();
    }
}
