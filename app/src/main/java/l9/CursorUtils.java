package l9;

import android.database.Cursor;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;

/* compiled from: CursorUtils.java */
/* renamed from: l9.c, reason: use source file name */
/* loaded from: classes2.dex */
public class CursorUtils {
    public static Double a(Cursor cursor, String str) {
        try {
            if (cursor.getColumnIndex(str) < 0) {
                return Double.valueOf(UserProfileInfo.Constant.NA_LAT_LON);
            }
            return Double.valueOf(cursor.getDouble(cursor.getColumnIndex(str)));
        } catch (Exception e10) {
            LogUtils.b("CursorUtils", "getDouble e = " + e10);
            return Double.valueOf(UserProfileInfo.Constant.NA_LAT_LON);
        }
    }

    public static int b(Cursor cursor, String str) {
        try {
            if (cursor.getColumnIndex(str) < 0) {
                return 0;
            }
            return cursor.getInt(cursor.getColumnIndex(str));
        } catch (Exception e10) {
            LogUtils.b("CursorUtils", "getInt e = " + e10);
            return 0;
        }
    }

    public static Long c(Cursor cursor, String str) {
        try {
            if (cursor.getColumnIndex(str) < 0) {
                return 0L;
            }
            return Long.valueOf(cursor.getLong(cursor.getColumnIndex(str)));
        } catch (Exception e10) {
            LogUtils.b("CursorUtils", "getLong e = " + e10);
            return 0L;
        }
    }

    public static String d(Cursor cursor, String str) {
        try {
            int columnIndex = cursor.getColumnIndex(str);
            if (columnIndex < 0) {
                return null;
            }
            return cursor.getString(columnIndex);
        } catch (Exception e10) {
            LogUtils.b("CursorUtils", "getString e = " + e10);
            return null;
        }
    }
}
