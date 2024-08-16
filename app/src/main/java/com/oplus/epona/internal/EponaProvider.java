package com.oplus.epona.internal;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.oplus.epona.Epona;
import com.oplus.epona.ipc.local.RemoteTransfer;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public class EponaProvider extends ContentProvider {
    public static final String KEY_LAUNCH_SUCCESS = "KEY_LAUNCH_SUCCESS";
    public static final String KEY_REMOTE_TRANSFER = "KEY_REMOTE_TRANSFER";
    public static final String LAUNCH_METHOD = "launchComponent";
    private static final String SECURITY_PERMISSION = "com.oplus.permission.safe.SECURITY";

    private boolean hasPermission() {
        return getContext().checkCallingPermission(SECURITY_PERMISSION) == 0;
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (hasPermission()) {
            bundle2.putBinder(KEY_REMOTE_TRANSFER, RemoteTransfer.getInstance());
        }
        if (LAUNCH_METHOD.equals(str)) {
            bundle2.putBoolean(KEY_LAUNCH_SUCCESS, true);
        }
        return bundle2;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (strArr != null && strArr.length > 0 && "oplus_epona".equals(strArr[0])) {
            Epona.dump(printWriter);
        } else {
            super.dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        Epona.init(getContext());
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
