package h6;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* compiled from: AppFeatureCache.java */
/* renamed from: h6.a, reason: use source file name */
/* loaded from: classes.dex */
public class AppFeatureCache {

    /* renamed from: a, reason: collision with root package name */
    private static final ArrayList<b> f11999a = new ArrayList<>();

    /* renamed from: b, reason: collision with root package name */
    private static final Uri f12000b = Uri.parse("content://com.oplus.customize.coreapp.configmanager.configprovider.AppFeatureProvider").buildUpon().appendPath("app_feature").build();

    /* renamed from: c, reason: collision with root package name */
    public static boolean f12001c = false;

    /* renamed from: d, reason: collision with root package name */
    public static c f12002d = c.CACHE_INVAILD;

    /* compiled from: AppFeatureCache.java */
    /* renamed from: h6.a$a */
    /* loaded from: classes.dex */
    private static class a {

        /* renamed from: a, reason: collision with root package name */
        static final AppFeatureCache f12003a = new AppFeatureCache();
    }

    /* compiled from: AppFeatureCache.java */
    /* renamed from: h6.a$b */
    /* loaded from: classes.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        private Integer f12004a;

        /* renamed from: b, reason: collision with root package name */
        private String f12005b;

        /* renamed from: c, reason: collision with root package name */
        private String f12006c;

        /* renamed from: d, reason: collision with root package name */
        private String f12007d;

        public b(Integer num, String str, String str2, String str3) {
            this.f12004a = num;
            this.f12005b = str;
            this.f12006c = str2;
            this.f12007d = str3;
        }

        public String a() {
            return this.f12005b;
        }

        public Integer b() {
            return this.f12004a;
        }

        public String c() {
            return this.f12007d;
        }

        public String d() {
            return this.f12006c;
        }

        public String toString() {
            return "AppFeatureData{_id='" + this.f12004a + "'featureName='" + this.f12005b + "', parameters='" + this.f12006c + "', jasonStr='" + this.f12007d + "'}";
        }
    }

    /* compiled from: AppFeatureCache.java */
    /* renamed from: h6.a$c */
    /* loaded from: classes.dex */
    public enum c {
        CACHE_ONLY,
        CACHE_AND_DB,
        CACHE_INVAILD
    }

    private void a() {
        synchronized (AppFeatureCache.class) {
            Log.i("AppFeatureCache", "clearCursorInCache");
            f11999a.clear();
        }
    }

    private Cursor d(String str) {
        MatrixCursor f10 = f();
        synchronized (AppFeatureCache.class) {
            Iterator<b> it = f11999a.iterator();
            while (it.hasNext()) {
                b next = it.next();
                if (f10 != null && next != null && next.a() != null && next.a().equals(str)) {
                    f10.addRow(new Object[]{next.b(), next.a(), next.d(), next.c()});
                }
            }
        }
        if (f10 == null || f10.getCount() != 0) {
            return f10;
        }
        f10.close();
        return null;
    }

    public static AppFeatureCache e() {
        return a.f12003a;
    }

    private MatrixCursor f() {
        return new MatrixCursor(new String[]{"_id", "featurename", "parameters", "lists"});
    }

    private void g(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }
        do {
            Integer valueOf = Integer.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
            String string = cursor.getString(cursor.getColumnIndex("featurename"));
            String string2 = cursor.getString(cursor.getColumnIndex("parameters"));
            String string3 = cursor.getString(cursor.getColumnIndex("lists"));
            synchronized (AppFeatureCache.class) {
                f11999a.add(new b(valueOf, string, string2, string3));
            }
        } while (cursor.moveToNext());
    }

    private void h(ContentResolver contentResolver, List<String> list) {
        Cursor query;
        Log.i("AppFeatureCache", "invalidateAppFeatureCache run in");
        if (list != null && list.size() != 0) {
            query = contentResolver.query(f12000b, null, "featurename in('" + TextUtils.join("','", list) + "')", null, null);
        } else {
            query = contentResolver.query(f12000b, null, null, null, null);
        }
        g(query);
        if (query != null) {
            query.close();
        }
        Log.i("AppFeatureCache", "invalidateAppFeatureCache size: " + f11999a.size());
    }

    public void b(ContentResolver contentResolver, List<String> list, c cVar) {
        Log.i("AppFeatureCache", "enableAppFeatureCache");
        a();
        h(contentResolver, list);
        f12001c = true;
        f12002d = cVar;
    }

    public Cursor c(String str) {
        if (!f12001c) {
            return null;
        }
        ArrayList<b> arrayList = f11999a;
        if (arrayList == null || arrayList.size() != 0) {
            return d(str);
        }
        return null;
    }
}
