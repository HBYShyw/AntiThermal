package com.oplus.powermanager.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import b6.LocalLog;
import f6.f;
import java.util.ArrayList;
import v4.CustmizeAllowBgRunable;
import x8.DatabaseHelper;

/* loaded from: classes2.dex */
public class BatteryStatsProvider extends ContentProvider {

    /* renamed from: g, reason: collision with root package name */
    private static UriMatcher f10422g;

    /* renamed from: e, reason: collision with root package name */
    private DatabaseHelper f10423e = null;

    /* renamed from: f, reason: collision with root package name */
    private ContentResolver f10424f = null;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        f10422g = uriMatcher;
        uriMatcher.addURI("com.oplus.powermanager", "battery_stats_list", 200);
        f10422g.addURI("com.oplus.powermanager", "app_label_list", 300);
    }

    private String a(Uri uri) {
        int match = f10422g.match(uri);
        if (match == 200) {
            return "battery_stats_list";
        }
        if (match == 300) {
            return "app_label_list";
        }
        throw new IllegalArgumentException("Error Uri: " + uri);
    }

    @Override // android.content.ContentProvider
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arrayList) {
        DatabaseHelper databaseHelper = this.f10423e;
        if (databaseHelper == null) {
            return null;
        }
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            ContentProviderResult[] applyBatch = super.applyBatch(arrayList);
            writableDatabase.setTransactionSuccessful();
            return applyBatch;
        } finally {
            writableDatabase.endTransaction();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0072, code lost:
    
        return r2;
     */
    @Override // android.content.ContentProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Bundle call(String str, String str2, String str3, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        str2.hashCode();
        char c10 = 65535;
        switch (str2.hashCode()) {
            case -807684916:
                if (str2.equals("getPowerControlList")) {
                    c10 = 0;
                    break;
                }
                break;
            case 1026062451:
                if (str2.equals("power_control_remove_white_list")) {
                    c10 = 1;
                    break;
                }
                break;
            case 1157869806:
                if (str2.equals("power_control_add_white_list")) {
                    c10 = 2;
                    break;
                }
                break;
        }
        switch (c10) {
            case 0:
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<String> arrayList2 = new ArrayList<>();
                f.l(arrayList, arrayList2, getContext());
                bundle2.putStringArrayList("allow_list", arrayList);
                bundle2.putStringArrayList("prohibit_list", arrayList2);
                break;
            case 1:
                CustmizeAllowBgRunable.i(getContext()).n(bundle.getStringArrayList("pc_white_list"));
                break;
            case 2:
                CustmizeAllowBgRunable.i(getContext()).h(bundle.getStringArrayList("pc_white_list"));
                break;
        }
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        try {
            int delete = this.f10423e.getWritableDatabase().delete(a(uri), str, strArr);
            ContentResolver contentResolver = this.f10424f;
            if (contentResolver != null) {
                contentResolver.notifyChange(uri, null);
            }
            return delete;
        } catch (SQLException unused) {
            Log.e("BatteryStatsProvider", "delete entry error!");
            return 0;
        }
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        long insert = this.f10423e.getWritableDatabase().insert(a(uri), null, contentValues);
        if (insert >= 0) {
            Uri withAppendedId = ContentUris.withAppendedId(uri, insert);
            if (withAppendedId != null) {
                this.f10424f.notifyChange(withAppendedId, null);
            }
            return withAppendedId;
        }
        throw new SQLiteException("Unable to insert " + contentValues + " for " + uri);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        LocalLog.a("BatteryStatsProvider", "BatteryStatsProvider--111--onCreate!");
        Context context = getContext();
        this.f10423e = new DatabaseHelper(context);
        this.f10424f = context.getContentResolver();
        this.f10423e.getWritableDatabase();
        return false;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor query = this.f10423e.getWritableDatabase().query(a(uri), strArr, str, strArr2, null, null, str2);
        query.setNotificationUri(this.f10424f, uri);
        return query;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        DatabaseHelper databaseHelper = this.f10423e;
        if (databaseHelper == null) {
            Log.e("BatteryStatsProvider", "create DatabaseHelper is null pointer!");
            return -1;
        }
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        if (writableDatabase == null) {
            Log.e("BatteryStatsProvider", "create DatabaseHelper is null pointer!");
            return -1;
        }
        int update = writableDatabase.update(a(uri), contentValues, str, strArr);
        ContentResolver contentResolver = this.f10424f;
        if (contentResolver != null) {
            contentResolver.notifyChange(uri, null);
        }
        return update;
    }
}
