package com.android.server.voiceinteraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.soundtrigger.SoundTrigger;
import android.text.TextUtils;
import android.util.Slog;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DatabaseHelper extends SQLiteOpenHelper implements IEnrolledModelDb {
    private static final String CREATE_TABLE_SOUND_MODEL = "CREATE TABLE sound_model(model_uuid TEXT,vendor_uuid TEXT,keyphrase_id INTEGER,type INTEGER,data BLOB,recognition_modes INTEGER,locale TEXT,hint_text TEXT,users TEXT,model_version INTEGER,PRIMARY KEY (keyphrase_id,locale,users))";
    static final boolean DBG = false;
    private static final String NAME = "sound_model.db";
    static final String TAG = "SoundModelDBHelper";
    private static final int VERSION = 7;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface SoundModelContract {
        public static final String KEY_DATA = "data";
        public static final String KEY_HINT_TEXT = "hint_text";
        public static final String KEY_KEYPHRASE_ID = "keyphrase_id";
        public static final String KEY_LOCALE = "locale";
        public static final String KEY_MODEL_UUID = "model_uuid";
        public static final String KEY_MODEL_VERSION = "model_version";
        public static final String KEY_RECOGNITION_MODES = "recognition_modes";
        public static final String KEY_TYPE = "type";
        public static final String KEY_USERS = "users";
        public static final String KEY_VENDOR_UUID = "vendor_uuid";
        public static final String TABLE = "sound_model";
    }

    public DatabaseHelper(Context context) {
        super(context, NAME, (SQLiteDatabase.CursorFactory) null, 7);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(CREATE_TABLE_SOUND_MODEL);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0032, code lost:
    
        r4.add(new com.android.server.voiceinteraction.DatabaseHelper.SoundModelRecord(5, r3));
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x003b, code lost:
    
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x003c, code lost:
    
        android.util.Slog.e(com.android.server.voiceinteraction.DatabaseHelper.TAG, "Failed to extract V5 record", r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0030, code lost:
    
        if (r3.moveToFirst() != false) goto L38;
     */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    @Override // android.database.sqlite.SQLiteOpenHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Cursor rawQuery;
        ArrayList<SoundModelRecord> arrayList;
        if (i < 4) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS sound_model");
            onCreate(sQLiteDatabase);
        } else if (i == 4) {
            Slog.d(TAG, "Adding vendor UUID column");
            sQLiteDatabase.execSQL("ALTER TABLE sound_model ADD COLUMN vendor_uuid TEXT");
            i++;
        }
        if (i == 5) {
            rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM sound_model", null);
            arrayList = new ArrayList();
            try {
            } catch (Throwable th) {
                rawQuery.close();
                throw th;
            }
        }
        if (i != 6) {
            Slog.d(TAG, "Adding model version column");
            sQLiteDatabase.execSQL("ALTER TABLE sound_model ADD COLUMN model_version INTEGER DEFAULT -1");
            return;
        }
        return;
        if (!rawQuery.moveToNext()) {
            rawQuery.close();
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS sound_model");
            onCreate(sQLiteDatabase);
            for (SoundModelRecord soundModelRecord : arrayList) {
                if (soundModelRecord.ifViolatesV6PrimaryKeyIsFirstOfAnyDuplicates(arrayList)) {
                    try {
                        long writeToDatabase = soundModelRecord.writeToDatabase(6, sQLiteDatabase);
                        if (writeToDatabase == -1) {
                            Slog.e(TAG, "Database write failed " + soundModelRecord.modelUuid + ": " + writeToDatabase);
                        }
                    } catch (Exception e) {
                        Slog.e(TAG, "Failed to update V6 record " + soundModelRecord.modelUuid, e);
                    }
                }
            }
            i++;
            if (i != 6) {
            }
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean updateKeyphraseSoundModel(SoundTrigger.KeyphraseSoundModel keyphraseSoundModel) {
        synchronized (this) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("model_uuid", keyphraseSoundModel.getUuid().toString());
            if (keyphraseSoundModel.getVendorUuid() != null) {
                contentValues.put("vendor_uuid", keyphraseSoundModel.getVendorUuid().toString());
            }
            contentValues.put("type", (Integer) 0);
            contentValues.put("data", keyphraseSoundModel.getData());
            contentValues.put("model_version", Integer.valueOf(keyphraseSoundModel.getVersion()));
            if (keyphraseSoundModel.getKeyphrases() == null || keyphraseSoundModel.getKeyphrases().length != 1) {
                return false;
            }
            contentValues.put(SoundModelContract.KEY_KEYPHRASE_ID, Integer.valueOf(keyphraseSoundModel.getKeyphrases()[0].getId()));
            contentValues.put(SoundModelContract.KEY_RECOGNITION_MODES, Integer.valueOf(keyphraseSoundModel.getKeyphrases()[0].getRecognitionModes()));
            contentValues.put(SoundModelContract.KEY_USERS, getCommaSeparatedString(keyphraseSoundModel.getKeyphrases()[0].getUsers()));
            contentValues.put(SoundModelContract.KEY_LOCALE, keyphraseSoundModel.getKeyphrases()[0].getLocale().toLanguageTag());
            contentValues.put(SoundModelContract.KEY_HINT_TEXT, keyphraseSoundModel.getKeyphrases()[0].getText());
            try {
                return writableDatabase.insertWithOnConflict(SoundModelContract.TABLE, null, contentValues, 5) != -1;
            } finally {
                writableDatabase.close();
            }
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean deleteKeyphraseSoundModel(int i, int i2, String str) {
        String languageTag = Locale.forLanguageTag(str).toLanguageTag();
        synchronized (this) {
            SoundTrigger.KeyphraseSoundModel keyphraseSoundModel = getKeyphraseSoundModel(i, i2, languageTag);
            if (keyphraseSoundModel == null) {
                return false;
            }
            SQLiteDatabase writableDatabase = getWritableDatabase();
            StringBuilder sb = new StringBuilder();
            sb.append("model_uuid='");
            sb.append(keyphraseSoundModel.getUuid().toString());
            sb.append("'");
            try {
                return writableDatabase.delete(SoundModelContract.TABLE, sb.toString(), null) != 0;
            } finally {
                writableDatabase.close();
            }
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(int i, int i2, String str) {
        SoundTrigger.KeyphraseSoundModel validKeyphraseSoundModelForUser;
        String languageTag = Locale.forLanguageTag(str).toLanguageTag();
        synchronized (this) {
            validKeyphraseSoundModelForUser = getValidKeyphraseSoundModelForUser("SELECT  * FROM sound_model WHERE keyphrase_id= '" + i + "' AND " + SoundModelContract.KEY_LOCALE + "='" + languageTag + "'", i2);
        }
        return validKeyphraseSoundModelForUser;
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(String str, int i, String str2) {
        SoundTrigger.KeyphraseSoundModel validKeyphraseSoundModelForUser;
        String languageTag = Locale.forLanguageTag(str2).toLanguageTag();
        synchronized (this) {
            validKeyphraseSoundModelForUser = getValidKeyphraseSoundModelForUser("SELECT  * FROM sound_model WHERE hint_text= '" + str + "' AND " + SoundModelContract.KEY_LOCALE + "='" + languageTag + "'", i);
        }
        return validKeyphraseSoundModelForUser;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c1, code lost:
    
        r13 = new android.hardware.soundtrigger.SoundTrigger.Keyphrase[]{new android.hardware.soundtrigger.SoundTrigger.Keyphrase(r8, r9, r10, r11, r12)};
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00cc, code lost:
    
        if (r5 == null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00ce, code lost:
    
        r11 = java.util.UUID.fromString(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00e6, code lost:
    
        return new android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel(java.util.UUID.fromString(r3), r11, r6, r13, r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00d4, code lost:
    
        r11 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x00be A[LOOP:0: B:5:0x0011->B:10:0x00be, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x00bd A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private SoundTrigger.KeyphraseSoundModel getValidKeyphraseSoundModelForUser(String str, int i) {
        boolean z;
        SQLiteDatabase readableDatabase = getReadableDatabase();
        String str2 = null;
        Cursor rawQuery = readableDatabase.rawQuery(str, null);
        try {
            if (rawQuery.moveToFirst()) {
                while (true) {
                    if (rawQuery.getInt(rawQuery.getColumnIndex("type")) == 0) {
                        String string = rawQuery.getString(rawQuery.getColumnIndex("model_uuid"));
                        if (string == null) {
                            Slog.w(TAG, "Ignoring SoundModel since it doesn't specify an ID");
                        } else {
                            int columnIndex = rawQuery.getColumnIndex("vendor_uuid");
                            String string2 = columnIndex != -1 ? rawQuery.getString(columnIndex) : str2;
                            int i2 = rawQuery.getInt(rawQuery.getColumnIndex(SoundModelContract.KEY_KEYPHRASE_ID));
                            byte[] blob = rawQuery.getBlob(rawQuery.getColumnIndex("data"));
                            int i3 = rawQuery.getInt(rawQuery.getColumnIndex(SoundModelContract.KEY_RECOGNITION_MODES));
                            int[] arrayForCommaSeparatedString = getArrayForCommaSeparatedString(rawQuery.getString(rawQuery.getColumnIndex(SoundModelContract.KEY_USERS)));
                            Locale forLanguageTag = Locale.forLanguageTag(rawQuery.getString(rawQuery.getColumnIndex(SoundModelContract.KEY_LOCALE)));
                            String string3 = rawQuery.getString(rawQuery.getColumnIndex(SoundModelContract.KEY_HINT_TEXT));
                            int i4 = rawQuery.getInt(rawQuery.getColumnIndex("model_version"));
                            if (arrayForCommaSeparatedString == null) {
                                Slog.w(TAG, "Ignoring SoundModel since it doesn't specify users");
                            } else {
                                int length = arrayForCommaSeparatedString.length;
                                int i5 = 0;
                                while (true) {
                                    if (i5 >= length) {
                                        z = false;
                                        break;
                                    }
                                    if (i == arrayForCommaSeparatedString[i5]) {
                                        z = true;
                                        break;
                                    }
                                    i5++;
                                }
                                if (z) {
                                    break;
                                }
                                if (rawQuery.moveToNext()) {
                                    break;
                                }
                                str2 = null;
                            }
                        }
                    }
                    if (rawQuery.moveToNext()) {
                    }
                }
            }
            rawQuery.close();
            readableDatabase.close();
            return null;
        } finally {
            rawQuery.close();
            readableDatabase.close();
        }
    }

    private static String getCommaSeparatedString(int[] iArr) {
        if (iArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iArr.length; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(iArr[i]);
        }
        return sb.toString();
    }

    private static int[] getArrayForCommaSeparatedString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split(",");
        int[] iArr = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            iArr[i] = Integer.parseInt(split[i]);
        }
        return iArr;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class SoundModelRecord {
        public final byte[] data;
        public final String hintText;
        public final int keyphraseId;
        public final String locale;
        public final String modelUuid;
        public final int recognitionModes;
        public final int type;
        public final String users;
        public final String vendorUuid;

        public SoundModelRecord(int i, Cursor cursor) {
            this.modelUuid = cursor.getString(cursor.getColumnIndex("model_uuid"));
            if (i >= 5) {
                this.vendorUuid = cursor.getString(cursor.getColumnIndex("vendor_uuid"));
            } else {
                this.vendorUuid = null;
            }
            this.keyphraseId = cursor.getInt(cursor.getColumnIndex(SoundModelContract.KEY_KEYPHRASE_ID));
            this.type = cursor.getInt(cursor.getColumnIndex("type"));
            this.data = cursor.getBlob(cursor.getColumnIndex("data"));
            this.recognitionModes = cursor.getInt(cursor.getColumnIndex(SoundModelContract.KEY_RECOGNITION_MODES));
            this.locale = cursor.getString(cursor.getColumnIndex(SoundModelContract.KEY_LOCALE));
            this.hintText = cursor.getString(cursor.getColumnIndex(SoundModelContract.KEY_HINT_TEXT));
            this.users = cursor.getString(cursor.getColumnIndex(SoundModelContract.KEY_USERS));
        }

        private boolean V6PrimaryKeyMatches(SoundModelRecord soundModelRecord) {
            return this.keyphraseId == soundModelRecord.keyphraseId && stringComparisonHelper(this.locale, soundModelRecord.locale) && stringComparisonHelper(this.users, soundModelRecord.users);
        }

        public boolean ifViolatesV6PrimaryKeyIsFirstOfAnyDuplicates(List<SoundModelRecord> list) {
            for (SoundModelRecord soundModelRecord : list) {
                if (this != soundModelRecord && V6PrimaryKeyMatches(soundModelRecord) && !Arrays.equals(this.data, soundModelRecord.data)) {
                    return false;
                }
            }
            Iterator<SoundModelRecord> it = list.iterator();
            while (it.hasNext()) {
                SoundModelRecord next = it.next();
                if (V6PrimaryKeyMatches(next)) {
                    return this == next;
                }
            }
            return true;
        }

        public long writeToDatabase(int i, SQLiteDatabase sQLiteDatabase) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("model_uuid", this.modelUuid);
            if (i >= 5) {
                contentValues.put("vendor_uuid", this.vendorUuid);
            }
            contentValues.put(SoundModelContract.KEY_KEYPHRASE_ID, Integer.valueOf(this.keyphraseId));
            contentValues.put("type", Integer.valueOf(this.type));
            contentValues.put("data", this.data);
            contentValues.put(SoundModelContract.KEY_RECOGNITION_MODES, Integer.valueOf(this.recognitionModes));
            contentValues.put(SoundModelContract.KEY_LOCALE, this.locale);
            contentValues.put(SoundModelContract.KEY_HINT_TEXT, this.hintText);
            contentValues.put(SoundModelContract.KEY_USERS, this.users);
            return sQLiteDatabase.insertWithOnConflict(SoundModelContract.TABLE, null, contentValues, 5);
        }

        private static boolean stringComparisonHelper(String str, String str2) {
            if (str != null) {
                return str.equals(str2);
            }
            return str == str2;
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public void dump(PrintWriter printWriter) {
        synchronized (this) {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            Cursor rawQuery = readableDatabase.rawQuery("SELECT  * FROM sound_model", null);
            try {
                printWriter.println("  Enrolled KeyphraseSoundModels:");
                if (rawQuery.moveToFirst()) {
                    String[] columnNames = rawQuery.getColumnNames();
                    do {
                        for (String str : columnNames) {
                            int columnIndex = rawQuery.getColumnIndex(str);
                            int type = rawQuery.getType(columnIndex);
                            if (type == 0) {
                                printWriter.printf("    %s: null\n", str);
                            } else if (type == 1) {
                                printWriter.printf("    %s: %d\n", str, Integer.valueOf(rawQuery.getInt(columnIndex)));
                            } else if (type == 2) {
                                printWriter.printf("    %s: %f\n", str, Float.valueOf(rawQuery.getFloat(columnIndex)));
                            } else if (type == 3) {
                                printWriter.printf("    %s: %s\n", str, rawQuery.getString(columnIndex));
                            } else if (type == 4) {
                                printWriter.printf("    %s: data blob\n", str);
                            }
                        }
                        printWriter.println();
                    } while (rawQuery.moveToNext());
                }
            } finally {
                rawQuery.close();
                readableDatabase.close();
            }
        }
    }
}
