package com.android.server.locksettings;

import android.app.backup.BackupManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.UserInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.server.LocalServices;
import com.android.server.PersistentDataBlockManagerInternal;
import com.android.server.locksettings.SyntheticPasswordManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class LockSettingsStorage {
    private static final String CHILD_PROFILE_LOCK_FILE = "gatekeeper.profile.key";
    private static final String COLUMN_KEY = "name";
    private static final String COLUMN_USERID = "user";
    private static final String REBOOT_ESCROW_FILE = "reboot.escrow.key";
    private static final String REBOOT_ESCROW_SERVER_BLOB_FILE = "reboot.escrow.server.blob.key";
    private static final String SYNTHETIC_PASSWORD_DIRECTORY = "spblob/";
    private static final String TABLE = "locksettings";
    private static final String TAG = "LockSettingsStorage";
    private final Context mContext;
    private final DatabaseHelper mOpenHelper;
    private PersistentDataBlockManagerInternal mPersistentDataBlockManagerInternal;
    private static final String COLUMN_VALUE = "value";
    private static final String[] COLUMNS_FOR_QUERY = {COLUMN_VALUE};
    private static final String[] COLUMNS_FOR_PREFETCH = {"name", COLUMN_VALUE};
    private static final Object DEFAULT = new Object();
    private static final String[] SETTINGS_TO_BACKUP = {"lock_screen_owner_info_enabled", "lock_screen_owner_info", "lock_pattern_visible_pattern", "lockscreen.power_button_instantly_locks"};
    private final Cache mCache = new Cache();
    private final Object mFileWriteLock = new Object();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Callback {
        void initialize(SQLiteDatabase sQLiteDatabase);
    }

    public LockSettingsStorage(Context context) {
        this.mContext = context;
        this.mOpenHelper = new DatabaseHelper(context);
    }

    public void setDatabaseOnCreateCallback(Callback callback) {
        this.mOpenHelper.setCallback(callback);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void writeKeyValue(String str, String str2, int i) {
        writeKeyValue(this.mOpenHelper.getWritableDatabase(), str, str2, i);
    }

    @VisibleForTesting
    public boolean isAutoPinConfirmSettingEnabled(int i) {
        return getBoolean("lockscreen.auto_pin_confirm", false, i);
    }

    @VisibleForTesting
    public void writeKeyValue(SQLiteDatabase sQLiteDatabase, String str, String str2, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", str);
        contentValues.put(COLUMN_USERID, Integer.valueOf(i));
        contentValues.put(COLUMN_VALUE, str2);
        sQLiteDatabase.beginTransaction();
        try {
            sQLiteDatabase.delete(TABLE, "name=? AND user=?", new String[]{str, Integer.toString(i)});
            sQLiteDatabase.insert(TABLE, null, contentValues);
            sQLiteDatabase.setTransactionSuccessful();
            this.mCache.putKeyValue(str, str2, i);
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }

    @VisibleForTesting
    public String readKeyValue(String str, String str2, int i) {
        Object obj;
        synchronized (this.mCache) {
            if (this.mCache.hasKeyValue(str, i)) {
                return this.mCache.peekKeyValue(str, str2, i);
            }
            int version = this.mCache.getVersion();
            Object obj2 = DEFAULT;
            Cursor query = this.mOpenHelper.getReadableDatabase().query(TABLE, COLUMNS_FOR_QUERY, "user=? AND name=?", new String[]{Integer.toString(i), str}, null, null, null);
            if (query != null) {
                obj = query.moveToFirst() ? query.getString(0) : obj2;
                query.close();
            } else {
                obj = obj2;
            }
            this.mCache.putKeyValueIfUnchanged(str, obj, i, version);
            return obj == obj2 ? str2 : (String) obj;
        }
    }

    @VisibleForTesting
    boolean isKeyValueCached(String str, int i) {
        return this.mCache.hasKeyValue(str, i);
    }

    @VisibleForTesting
    boolean isUserPrefetched(int i) {
        return this.mCache.isFetched(i);
    }

    @VisibleForTesting
    public void removeKey(String str, int i) {
        removeKey(this.mOpenHelper.getWritableDatabase(), str, i);
    }

    private void removeKey(SQLiteDatabase sQLiteDatabase, String str, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", str);
        contentValues.put(COLUMN_USERID, Integer.valueOf(i));
        sQLiteDatabase.beginTransaction();
        try {
            sQLiteDatabase.delete(TABLE, "name=? AND user=?", new String[]{str, Integer.toString(i)});
            sQLiteDatabase.setTransactionSuccessful();
            this.mCache.removeKey(str, i);
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }

    public void prefetchUser(int i) {
        synchronized (this.mCache) {
            if (this.mCache.isFetched(i)) {
                return;
            }
            this.mCache.setFetched(i);
            int version = this.mCache.getVersion();
            Cursor query = this.mOpenHelper.getReadableDatabase().query(TABLE, COLUMNS_FOR_PREFETCH, "user=?", new String[]{Integer.toString(i)}, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    this.mCache.putKeyValueIfUnchanged(query.getString(0), query.getString(1), i, version);
                }
                query.close();
            }
        }
    }

    public void removeChildProfileLock(int i) {
        deleteFile(getChildProfileLockFile(i));
    }

    public void writeChildProfileLock(int i, byte[] bArr) {
        writeFile(getChildProfileLockFile(i), bArr);
    }

    public byte[] readChildProfileLock(int i) {
        return readFile(getChildProfileLockFile(i));
    }

    public boolean hasChildProfileLock(int i) {
        return hasFile(getChildProfileLockFile(i));
    }

    public void writeRebootEscrow(int i, byte[] bArr) {
        writeFile(getRebootEscrowFile(i), bArr);
    }

    public byte[] readRebootEscrow(int i) {
        return readFile(getRebootEscrowFile(i));
    }

    public boolean hasRebootEscrow(int i) {
        return hasFile(getRebootEscrowFile(i));
    }

    public void removeRebootEscrow(int i) {
        deleteFile(getRebootEscrowFile(i));
    }

    public void writeRebootEscrowServerBlob(byte[] bArr) {
        writeFile(getRebootEscrowServerBlobFile(), bArr);
    }

    public byte[] readRebootEscrowServerBlob() {
        return readFile(getRebootEscrowServerBlobFile());
    }

    public boolean hasRebootEscrowServerBlob() {
        return hasFile(getRebootEscrowServerBlobFile());
    }

    public void removeRebootEscrowServerBlob() {
        deleteFile(getRebootEscrowServerBlobFile());
    }

    private boolean hasFile(File file) {
        byte[] readFile = readFile(file);
        return readFile != null && readFile.length > 0;
    }

    private byte[] readFile(File file) {
        synchronized (this.mCache) {
            if (this.mCache.hasFile(file)) {
                return this.mCache.peekFile(file);
            }
            int version = this.mCache.getVersion();
            byte[] bArr = null;
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                try {
                    int length = (int) randomAccessFile.length();
                    bArr = new byte[length];
                    randomAccessFile.readFully(bArr, 0, length);
                    randomAccessFile.close();
                    randomAccessFile.close();
                } finally {
                }
            } catch (FileNotFoundException e) {
                Slog.e(TAG, "File not found " + e);
            } catch (IOException e2) {
                Slog.e(TAG, "Cannot read file " + e2);
            }
            this.mCache.putFileIfUnchanged(file, bArr, version);
            return bArr;
        }
    }

    private void fsyncDirectory(File file) {
        try {
            FileChannel open = FileChannel.open(file.toPath(), StandardOpenOption.READ);
            try {
                open.force(true);
                open.close();
            } finally {
            }
        } catch (IOException e) {
            Slog.e(TAG, "Error syncing directory: " + file, e);
        }
    }

    private void writeFile(File file, byte[] bArr) {
        writeFile(file, bArr, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003d A[Catch: all -> 0x004f, TryCatch #4 {, blocks: (B:4:0x0003, B:10:0x0013, B:12:0x003d, B:13:0x0044, B:14:0x0049, B:24:0x004b, B:25:0x004e, B:20:0x0038), top: B:3:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeFile(File file, byte[] bArr, boolean z) {
        FileOutputStream fileOutputStream;
        IOException e;
        synchronized (this.mFileWriteLock) {
            AtomicFile atomicFile = new AtomicFile(file);
            FileOutputStream fileOutputStream2 = null;
            try {
                fileOutputStream = atomicFile.startWrite();
            } catch (IOException e2) {
                fileOutputStream = null;
                e = e2;
            } catch (Throwable th) {
                th = th;
                atomicFile.failWrite(fileOutputStream2);
                throw th;
            }
            try {
                try {
                    fileOutputStream.write(bArr);
                    atomicFile.finishWrite(fileOutputStream);
                    atomicFile.failWrite(null);
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream2 = fileOutputStream;
                    atomicFile.failWrite(fileOutputStream2);
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                Slog.e(TAG, "Error writing file " + file, e);
                atomicFile.failWrite(fileOutputStream);
                if (z) {
                }
                this.mCache.putFile(file, bArr);
            }
            if (z) {
                fsyncDirectory(file.getParentFile());
            }
            this.mCache.putFile(file, bArr);
        }
    }

    private void deleteFile(File file) {
        synchronized (this.mFileWriteLock) {
            if (file.exists()) {
                try {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
                    try {
                        randomAccessFile.write(new byte[(int) randomAccessFile.length()]);
                        randomAccessFile.close();
                    } catch (Throwable th) {
                        try {
                            randomAccessFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Exception e) {
                    Slog.w(TAG, "Failed to zeroize " + file, e);
                }
            }
            new AtomicFile(file).delete();
            this.mCache.putFile(file, null);
        }
    }

    @VisibleForTesting
    File getChildProfileLockFile(int i) {
        return getLockCredentialFileForUser(i, CHILD_PROFILE_LOCK_FILE);
    }

    @VisibleForTesting
    File getRebootEscrowFile(int i) {
        return getLockCredentialFileForUser(i, REBOOT_ESCROW_FILE);
    }

    @VisibleForTesting
    File getRebootEscrowServerBlobFile() {
        return getLockCredentialFileForUser(0, REBOOT_ESCROW_SERVER_BLOB_FILE);
    }

    private File getLockCredentialFileForUser(int i, String str) {
        if (i == 0) {
            return new File(Environment.getDataSystemDirectory(), str);
        }
        return new File(Environment.getUserSystemDirectory(i), str);
    }

    public void writeSyntheticPasswordState(int i, long j, String str, byte[] bArr) {
        Slog.d(TAG, "[writeSyntheticPasswordState] userId = " + i + ", name = " + str + ", data = " + Arrays.toString(bArr));
        ensureSyntheticPasswordDirectoryForUser(i);
        writeFile(getSyntheticPasswordStateFileForUser(i, j, str), bArr, false);
    }

    public byte[] readSyntheticPasswordState(int i, long j, String str) {
        Slog.d(TAG, "[readSyntheticPasswordState] userId = " + i + ", name = " + str);
        return readFile(getSyntheticPasswordStateFileForUser(i, j, str));
    }

    public void deleteSyntheticPasswordState(int i, long j, String str) {
        Slog.d(TAG, "[deleteSyntheticPasswordState] userId = " + i + ", name = " + str);
        deleteFile(getSyntheticPasswordStateFileForUser(i, j, str));
    }

    public void syncSyntheticPasswordState(int i) {
        fsyncDirectory(getSyntheticPasswordDirectoryForUser(i));
    }

    public Map<Integer, List<Long>> listSyntheticPasswordProtectorsForAllUsers(String str) {
        ArrayMap arrayMap = new ArrayMap();
        for (UserInfo userInfo : UserManager.get(this.mContext).getUsers()) {
            arrayMap.put(Integer.valueOf(userInfo.id), listSyntheticPasswordProtectorsForUser(str, userInfo.id));
        }
        return arrayMap;
    }

    public List<Long> listSyntheticPasswordProtectorsForUser(String str, int i) {
        File syntheticPasswordDirectoryForUser = getSyntheticPasswordDirectoryForUser(i);
        ArrayList arrayList = new ArrayList();
        File[] listFiles = syntheticPasswordDirectoryForUser.listFiles();
        if (listFiles == null) {
            return arrayList;
        }
        for (File file : listFiles) {
            String[] split = file.getName().split("\\.");
            if (split.length == 2 && split[1].equals(str)) {
                try {
                    Slog.d(TAG, "[listSyntheticPasswordHandlesForUser] userId = " + i + ", stateName = " + str + ", filename = " + file.getName());
                    arrayList.add(Long.valueOf(Long.parseUnsignedLong(split[0], 16)));
                } catch (NumberFormatException unused) {
                    Slog.e(TAG, "Failed to parse protector ID " + split[0]);
                }
            }
        }
        return arrayList;
    }

    @VisibleForTesting
    protected File getSyntheticPasswordDirectoryForUser(int i) {
        return new File(Environment.getDataSystemDeDirectory(i), SYNTHETIC_PASSWORD_DIRECTORY);
    }

    private void ensureSyntheticPasswordDirectoryForUser(int i) {
        File syntheticPasswordDirectoryForUser = getSyntheticPasswordDirectoryForUser(i);
        if (syntheticPasswordDirectoryForUser.exists()) {
            return;
        }
        syntheticPasswordDirectoryForUser.mkdir();
    }

    private File getSyntheticPasswordStateFileForUser(int i, long j, String str) {
        return new File(getSyntheticPasswordDirectoryForUser(i), TextUtils.formatSimple("%016x.%s", new Object[]{Long.valueOf(j), str}));
    }

    public void removeUser(int i) {
        SQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
        if (((UserManager) this.mContext.getSystemService(COLUMN_USERID)).getProfileParent(i) == null) {
            deleteFile(getRebootEscrowFile(i));
        } else {
            removeChildProfileLock(i);
        }
        File syntheticPasswordDirectoryForUser = getSyntheticPasswordDirectoryForUser(i);
        try {
            writableDatabase.beginTransaction();
            writableDatabase.delete(TABLE, "user='" + i + "'", null);
            writableDatabase.setTransactionSuccessful();
            this.mCache.removeUser(i);
            this.mCache.purgePath(syntheticPasswordDirectoryForUser);
            Slog.d(TAG, "[removeUser] userId = " + i + ", spStateDir = " + syntheticPasswordDirectoryForUser.getAbsolutePath());
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void setBoolean(String str, boolean z, int i) {
        setString(str, z ? "1" : "0", i);
    }

    public void setLong(String str, long j, int i) {
        setString(str, Long.toString(j), i);
    }

    public void setInt(String str, int i, int i2) {
        setString(str, Integer.toString(i), i2);
    }

    public void setString(String str, String str2, int i) {
        Preconditions.checkArgument(i != -9999, "cannot store lock settings for FRP user");
        writeKeyValue(str, str2, i);
        if (ArrayUtils.contains(SETTINGS_TO_BACKUP, str)) {
            BackupManager.dataChanged("com.android.providers.settings");
        }
    }

    public boolean getBoolean(String str, boolean z, int i) {
        String string = getString(str, null, i);
        return TextUtils.isEmpty(string) ? z : string.equals("1") || string.equals("true");
    }

    public long getLong(String str, long j, int i) {
        String string = getString(str, null, i);
        return TextUtils.isEmpty(string) ? j : Long.parseLong(string);
    }

    public int getInt(String str, int i, int i2) {
        String string = getString(str, null, i2);
        return TextUtils.isEmpty(string) ? i : Integer.parseInt(string);
    }

    public String getString(String str, String str2, int i) {
        if (i == -9999) {
            return null;
        }
        return readKeyValue(str, str2, i);
    }

    @VisibleForTesting
    void closeDatabase() {
        this.mOpenHelper.close();
    }

    @VisibleForTesting
    void clearCache() {
        this.mCache.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersistentDataBlockManagerInternal getPersistentDataBlockManager() {
        if (this.mPersistentDataBlockManagerInternal == null) {
            this.mPersistentDataBlockManagerInternal = (PersistentDataBlockManagerInternal) LocalServices.getService(PersistentDataBlockManagerInternal.class);
        }
        return this.mPersistentDataBlockManagerInternal;
    }

    public void writePersistentDataBlock(int i, int i2, int i3, byte[] bArr) {
        PersistentDataBlockManagerInternal persistentDataBlockManager = getPersistentDataBlockManager();
        if (persistentDataBlockManager == null) {
            return;
        }
        persistentDataBlockManager.setFrpCredentialHandle(PersistentData.toBytes(i, i2, i3, bArr));
    }

    public PersistentData readPersistentDataBlock() {
        PersistentDataBlockManagerInternal persistentDataBlockManager = getPersistentDataBlockManager();
        if (persistentDataBlockManager == null) {
            return PersistentData.NONE;
        }
        try {
            return PersistentData.fromBytes(persistentDataBlockManager.getFrpCredentialHandle());
        } catch (IllegalStateException e) {
            Slog.e(TAG, "Error reading persistent data block", e);
            return PersistentData.NONE;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PersistentData {
        public static final PersistentData NONE = new PersistentData(0, -10000, 0, null);
        public static final int TYPE_NONE = 0;
        public static final int TYPE_SP_GATEKEEPER = 1;
        public static final int TYPE_SP_WEAVER = 2;
        static final byte VERSION_1 = 1;
        static final int VERSION_1_HEADER_SIZE = 10;
        final byte[] payload;
        final int qualityForUi;
        final int type;
        final int userId;

        private PersistentData(int i, int i2, int i3, byte[] bArr) {
            this.type = i;
            this.userId = i2;
            this.qualityForUi = i3;
            this.payload = bArr;
        }

        public boolean isBadFormatFromAndroid14Beta() {
            int i = this.type;
            return (i == 1 || i == 2) && SyntheticPasswordManager.PasswordData.isBadFormatFromAndroid14Beta(this.payload);
        }

        public static PersistentData fromBytes(byte[] bArr) {
            if (bArr == null || bArr.length == 0) {
                return NONE;
            }
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
            try {
                byte readByte = dataInputStream.readByte();
                if (readByte == 1) {
                    int readByte2 = dataInputStream.readByte() & 255;
                    int readInt = dataInputStream.readInt();
                    int readInt2 = dataInputStream.readInt();
                    int length = bArr.length - 10;
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, 10, bArr2, 0, length);
                    return new PersistentData(readByte2, readInt, readInt2, bArr2);
                }
                Slog.wtf(LockSettingsStorage.TAG, "Unknown PersistentData version code: " + ((int) readByte));
                return NONE;
            } catch (IOException e) {
                Slog.wtf(LockSettingsStorage.TAG, "Could not parse PersistentData", e);
                return NONE;
            }
        }

        public static byte[] toBytes(int i, int i2, int i3, byte[] bArr) {
            if (i == 0) {
                Preconditions.checkArgument(bArr == null, "TYPE_NONE must have empty payload");
                return null;
            }
            if (bArr != null && bArr.length > 0) {
                r0 = true;
            }
            Preconditions.checkArgument(r0, "empty payload must only be used with TYPE_NONE");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length + 10);
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeByte(1);
                dataOutputStream.writeByte(i);
                dataOutputStream.writeInt(i2);
                dataOutputStream.writeInt(i3);
                dataOutputStream.write(bArr);
                return byteArrayOutputStream.toByteArray();
            } catch (IOException unused) {
                throw new IllegalStateException("ByteArrayOutputStream cannot throw IOException");
            }
        }
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        for (UserInfo userInfo : UserManager.get(this.mContext).getUsers()) {
            File syntheticPasswordDirectoryForUser = getSyntheticPasswordDirectoryForUser(userInfo.id);
            indentingPrintWriter.println(TextUtils.formatSimple("User %d [%s]:", new Object[]{Integer.valueOf(userInfo.id), syntheticPasswordDirectoryForUser}));
            indentingPrintWriter.increaseIndent();
            File[] listFiles = syntheticPasswordDirectoryForUser.listFiles();
            if (listFiles != null) {
                Arrays.sort(listFiles);
                for (File file : listFiles) {
                    indentingPrintWriter.println(TextUtils.formatSimple("%6d %s %s", new Object[]{Long.valueOf(file.length()), LockSettingsService.timestampToString(file.lastModified()), file.getName()}));
                }
            } else {
                indentingPrintWriter.println("[Not found]");
            }
            indentingPrintWriter.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "locksettings.db";
        private static final int DATABASE_VERSION = 2;
        private static final int IDLE_CONNECTION_TIMEOUT_MS = 30000;
        private static final String TAG = "LockSettingsDB";
        private Callback mCallback;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 2);
            setWriteAheadLoggingEnabled(false);
            setIdleConnectionTimeout(30000L);
        }

        public void setCallback(Callback callback) {
            this.mCallback = callback;
        }

        private void createTable(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE locksettings (_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,user INTEGER,value TEXT);");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            createTable(sQLiteDatabase);
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.initialize(sQLiteDatabase);
            }
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            if (i == 1) {
                i = 2;
            }
            if (i != 2) {
                Slog.w(TAG, "Failed to upgrade database!");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Cache {
        private final ArrayMap<CacheKey, Object> mCache;
        private final CacheKey mCacheKey;
        private int mVersion;

        private Cache() {
            this.mCache = new ArrayMap<>();
            this.mCacheKey = new CacheKey();
            this.mVersion = 0;
        }

        String peekKeyValue(String str, String str2, int i) {
            Object peek = peek(0, str, i);
            return peek == LockSettingsStorage.DEFAULT ? str2 : (String) peek;
        }

        boolean hasKeyValue(String str, int i) {
            return contains(0, str, i);
        }

        void putKeyValue(String str, String str2, int i) {
            put(0, str, str2, i);
        }

        void putKeyValueIfUnchanged(String str, Object obj, int i, int i2) {
            putIfUnchanged(0, str, obj, i, i2);
        }

        void removeKey(String str, int i) {
            remove(0, str, i);
        }

        byte[] peekFile(File file) {
            return copyOf((byte[]) peek(1, file.toString(), -1));
        }

        boolean hasFile(File file) {
            return contains(1, file.toString(), -1);
        }

        void putFile(File file, byte[] bArr) {
            put(1, file.toString(), copyOf(bArr), -1);
        }

        void putFileIfUnchanged(File file, byte[] bArr, int i) {
            putIfUnchanged(1, file.toString(), copyOf(bArr), -1, i);
        }

        void setFetched(int i) {
            put(2, "", "true", i);
        }

        boolean isFetched(int i) {
            return contains(2, "", i);
        }

        private synchronized void remove(int i, String str, int i2) {
            this.mCache.remove(this.mCacheKey.set(i, str, i2));
            this.mVersion++;
        }

        private synchronized void put(int i, String str, Object obj, int i2) {
            this.mCache.put(new CacheKey().set(i, str, i2), obj);
            this.mVersion++;
        }

        private synchronized void putIfUnchanged(int i, String str, Object obj, int i2, int i3) {
            if (!contains(i, str, i2) && this.mVersion == i3) {
                this.mCache.put(new CacheKey().set(i, str, i2), obj);
            }
        }

        private synchronized boolean contains(int i, String str, int i2) {
            return this.mCache.containsKey(this.mCacheKey.set(i, str, i2));
        }

        private synchronized Object peek(int i, String str, int i2) {
            return this.mCache.get(this.mCacheKey.set(i, str, i2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int getVersion() {
            return this.mVersion;
        }

        synchronized void removeUser(int i) {
            for (int size = this.mCache.size() - 1; size >= 0; size--) {
                if (this.mCache.keyAt(size).userId == i) {
                    this.mCache.removeAt(size);
                }
            }
            this.mVersion++;
        }

        private byte[] copyOf(byte[] bArr) {
            if (bArr != null) {
                return Arrays.copyOf(bArr, bArr.length);
            }
            return null;
        }

        synchronized void purgePath(File file) {
            String file2 = file.toString();
            for (int size = this.mCache.size() - 1; size >= 0; size--) {
                CacheKey keyAt = this.mCache.keyAt(size);
                if (keyAt.type == 1 && keyAt.key.startsWith(file2)) {
                    this.mCache.removeAt(size);
                }
            }
            this.mVersion++;
        }

        synchronized void clear() {
            this.mCache.clear();
            this.mVersion++;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static final class CacheKey {
            static final int TYPE_FETCHED = 2;
            static final int TYPE_FILE = 1;
            static final int TYPE_KEY_VALUE = 0;
            String key;
            int type;
            int userId;

            private CacheKey() {
            }

            public CacheKey set(int i, String str, int i2) {
                this.type = i;
                this.key = str;
                this.userId = i2;
                return this;
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof CacheKey)) {
                    return false;
                }
                CacheKey cacheKey = (CacheKey) obj;
                return this.userId == cacheKey.userId && this.type == cacheKey.type && Objects.equals(this.key, cacheKey.key);
            }

            public int hashCode() {
                return (((Objects.hashCode(this.key) * 31) + this.userId) * 31) + this.type;
            }
        }
    }
}
