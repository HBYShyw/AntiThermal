package com.oplus.oguard.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import c0.SupportSQLiteQueryBuilder;
import com.oplus.oguard.data.database.OGuardDataBase;

/* loaded from: classes.dex */
public class OGuardProvider extends ContentProvider {

    /* renamed from: g, reason: collision with root package name */
    private static UriMatcher f9940g;

    /* renamed from: e, reason: collision with root package name */
    private Context f9941e;

    /* renamed from: f, reason: collision with root package name */
    private ContentResolver f9942f;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        f9940g = uriMatcher;
        uriMatcher.addURI("com.oplus.oguard.provider", "app_power_record", 101);
        f9940g.addURI("com.oplus.oguard.provider", "app_guard_event", 102);
        f9940g.addURI("com.oplus.oguard.provider", "app_info", 103);
    }

    private String a(Uri uri) {
        switch (f9940g.match(uri)) {
            case 101:
                return "app_power_record";
            case 102:
                return "app_guard_event";
            case 103:
                return "app_info";
            default:
                throw new IllegalArgumentException("Error Uri: " + uri);
        }
    }

    private void b(Uri uri, ContentValues contentValues) {
        if (OGuardDataBase.v(this.f9941e).j().b().K(a(uri), 5, contentValues) >= 0) {
            return;
        }
        throw new SQLiteException("Unable to insert " + contentValues + " for " + uri);
    }

    @Override // android.content.ContentProvider
    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        for (ContentValues contentValues : contentValuesArr) {
            b(uri, contentValues);
        }
        ContentResolver contentResolver = this.f9942f;
        if (contentResolver != null) {
            contentResolver.notifyChange(uri, null);
        }
        return contentValuesArr.length;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        int i10;
        try {
            i10 = OGuardDataBase.v(this.f9941e).j().b().d(a(uri), str, strArr);
        } catch (Exception e10) {
            e10.printStackTrace();
            i10 = 0;
        }
        ContentResolver contentResolver = this.f9942f;
        if (contentResolver != null) {
            contentResolver.notifyChange(uri, null);
        }
        return i10;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        b(uri, contentValues);
        ContentResolver contentResolver = this.f9942f;
        if (contentResolver != null) {
            contentResolver.notifyChange(uri, null);
        }
        return uri;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        Context context = getContext();
        this.f9941e = context;
        this.f9942f = context.getContentResolver();
        return false;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return OGuardDataBase.v(this.f9941e).j().b().q(SupportSQLiteQueryBuilder.c(a(uri)).d(strArr).g(str2).h(str, strArr2).e());
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return OGuardDataBase.v(this.f9941e).j().b().B(a(uri), 5, contentValues, str, strArr);
    }
}
