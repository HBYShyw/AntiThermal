package h6;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import h6.AppFeatureCache;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

/* compiled from: AppFeatureProviderUtils.java */
/* renamed from: h6.b, reason: use source file name */
/* loaded from: classes.dex */
public class AppFeatureProviderUtils {

    /* renamed from: a, reason: collision with root package name */
    private static final Uri f12012a = Uri.parse("content://com.oplus.customize.coreapp.configmanager.configprovider.AppFeatureProvider").buildUpon().appendPath("app_feature").build();

    public static void a(ContentResolver contentResolver, List<String> list, AppFeatureCache.c cVar) {
        AppFeatureCache.e().b(contentResolver, list, cVar);
    }

    private static Cursor b(ContentResolver contentResolver, String str) {
        Cursor c10 = AppFeatureCache.f12001c ? AppFeatureCache.e().c(str) : null;
        return (c10 != null || AppFeatureCache.f12002d == AppFeatureCache.c.CACHE_ONLY) ? c10 : contentResolver.query(f12012a, null, "featurename=?", new String[]{str}, null);
    }

    public static boolean c(ContentResolver contentResolver, String str, boolean z10) {
        String e10 = e(contentResolver, str, "boolean");
        if (e10 == null) {
            return z10;
        }
        try {
            return Boolean.parseBoolean(e10);
        } catch (Exception e11) {
            Log.i("AppFeatureProviderUtils", "getBoolean error:" + e11.toString());
            return z10;
        }
    }

    public static int d(ContentResolver contentResolver, String str, int i10) {
        String e10 = e(contentResolver, str, "int");
        if (e10 == null) {
            return i10;
        }
        try {
            return Integer.parseInt(e10);
        } catch (Exception e11) {
            Log.i("AppFeatureProviderUtils", "getInt error:" + e11.toString());
            return i10;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0017, code lost:
    
        r8 = r7.getString(r7.getColumnIndex("parameters"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0021, code lost:
    
        if (r8 == null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0027, code lost:
    
        if (r8.isEmpty() != false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0029, code lost:
    
        r8 = r8.split(";");
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
    
        if (r8 == null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0032, code lost:
    
        if (r8.length <= 0) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0034, code lost:
    
        r1 = r8.length;
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0037, code lost:
    
        if (r3 >= r1) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0039, code lost:
    
        r4 = r8[r3];
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003b, code lost:
    
        if (r4 == null) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0041, code lost:
    
        if (r4.isEmpty() != false) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0043, code lost:
    
        r5 = r4.indexOf(":");
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0049, code lost:
    
        if (r5 <= 0) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x004b, code lost:
    
        r6 = r4.substring(0, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x004f, code lost:
    
        if (r6 == null) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0055, code lost:
    
        if (r6.equals(r9) == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0057, code lost:
    
        r0 = r4.substring(r5 + 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0061, code lost:
    
        if (r0 == null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x005e, code lost:
    
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0068, code lost:
    
        if (r7.moveToNext() != false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0015, code lost:
    
        if (r7.moveToFirst() != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static String e(ContentResolver contentResolver, String str, String str2) {
        String str3 = null;
        if (contentResolver != null && !TextUtils.isEmpty(str) && str2 != null) {
            Cursor b10 = b(contentResolver, str);
            if (b10 != null) {
            }
            if (b10 != null) {
                b10.close();
            }
        }
        Log.d("AppFeatureProviderUtils", "getStringForFeature: " + str3);
        return str3;
    }

    public static List<String> f(ContentResolver contentResolver, String str) {
        return g(contentResolver, str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static List<String> g(ContentResolver contentResolver, String str) {
        List arrayList = new ArrayList();
        if (contentResolver != null && !TextUtils.isEmpty(str)) {
            Cursor b10 = b(contentResolver, str);
            if (b10 != null && b10.moveToFirst()) {
                try {
                    arrayList = i(b10.getString(b10.getColumnIndex("lists")));
                } catch (Exception e10) {
                    Log.e("AppFeatureProviderUtils", "getStringListForFeature error: " + e10.toString());
                }
            }
            if (b10 != null) {
                b10.close();
            }
        }
        Log.d("AppFeatureProviderUtils", "getStringListForFeature: " + arrayList);
        return arrayList;
    }

    public static boolean h(ContentResolver contentResolver, String str) {
        Cursor b10 = b(contentResolver, str);
        boolean z10 = b10 != null && b10.getCount() > 0;
        if (b10 != null) {
            b10.close();
        }
        return z10;
    }

    private static List<String> i(String str) {
        ArrayList arrayList = new ArrayList();
        JSONArray jSONArray = new JSONArray(str);
        for (int i10 = 0; i10 < jSONArray.length(); i10++) {
            arrayList.add(jSONArray.getString(i10));
        }
        return arrayList;
    }

    public static void j(ContentResolver contentResolver, boolean z10, ContentObserver contentObserver) {
        if (contentResolver == null || contentObserver == null) {
            return;
        }
        contentResolver.registerContentObserver(f12012a, z10, contentObserver);
    }
}
