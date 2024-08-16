package com.oplus.epona.ipc.cursor;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;

/* loaded from: classes.dex */
public class ProviderCursor extends MatrixCursor {
    public static final String[] DEFAULT_COLUMNS = {"col"};
    public static final String TAG = "ProviderCursor";
    private Bundle binderExtras;

    public ProviderCursor(String[] strArr, Bundle bundle) {
        super(strArr);
        this.binderExtras = new Bundle(bundle);
    }

    public static ProviderCursor generateCursor(Bundle bundle) {
        return new ProviderCursor(DEFAULT_COLUMNS, bundle);
    }

    public static Bundle stripBundle(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        return cursor.getExtras();
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public Bundle getExtras() {
        return this.binderExtras;
    }
}
