package g9;

import android.database.Cursor;
import android.net.Uri;
import f9.SDKInit;
import l9.LogUtils;

/* compiled from: ScenesProviderUtils.java */
/* loaded from: classes2.dex */
public class a {
    public static int a(Cursor cursor, String str) {
        try {
            if (cursor.getColumnIndex(str) < 0) {
                return 0;
            }
            return cursor.getInt(cursor.getColumnIndex(str));
        } catch (Exception e10) {
            LogUtils.b("ScenesProviderUtils", "getInt e = " + e10);
            return 0;
        }
    }

    public static Long b(Cursor cursor, String str) {
        try {
            if (cursor.getColumnIndex(str) < 0) {
                return 0L;
            }
            return Long.valueOf(cursor.getLong(cursor.getColumnIndex(str)));
        } catch (Exception e10) {
            LogUtils.b("ScenesProviderUtils", "getLong e = " + e10);
            return 0L;
        }
    }

    public static String c(Cursor cursor, String str) {
        try {
            int columnIndex = cursor.getColumnIndex(str);
            if (columnIndex < 0) {
                return null;
            }
            return cursor.getString(columnIndex);
        } catch (Exception e10) {
            LogUtils.b("ScenesProviderUtils", "getString e = " + e10);
            return null;
        }
    }

    public static Uri d(String str) {
        return Uri.parse("content://com.oplus.sceneservice.scenesprovider/" + str);
    }

    public static Cursor e(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return SDKInit.a().getContentResolver().query(uri, strArr, str, strArr2, str2);
    }
}
