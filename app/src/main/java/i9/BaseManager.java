package i9;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.util.ArrayList;

/* compiled from: BaseManager.java */
/* renamed from: i9.a, reason: use source file name */
/* loaded from: classes2.dex */
public abstract class BaseManager<T> {

    /* renamed from: a, reason: collision with root package name */
    private ContentResolver f12678a;

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseManager(Context context) {
        this.f12678a = null;
        if (context != null) {
            this.f12678a = context.getApplicationContext().getContentResolver();
        }
    }

    protected abstract T a(Cursor cursor);

    public abstract Uri b();

    /* JADX INFO: Access modifiers changed from: protected */
    public ArrayList<T> c(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        ArrayList<T> arrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = this.f12678a.query(uri, strArr, str, strArr2, str2);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    arrayList.add(a(cursor));
                }
            }
        } finally {
            try {
                return arrayList;
            } finally {
            }
        }
        return arrayList;
    }

    public ArrayList<T> d(String[] strArr, String str, String[] strArr2, String str2) {
        return c(b(), strArr, str, strArr2, str2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T e(String str, String[] strArr, String str2) {
        ArrayList<T> d10 = d(null, str, strArr, str2);
        if (l9.e.a(d10)) {
            return null;
        }
        return d10.get(0);
    }
}
