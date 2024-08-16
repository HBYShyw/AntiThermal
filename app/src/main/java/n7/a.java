package n7;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.oplus.thermalcontrol.ThermalControlConfig;
import z5.LocalFileUtil;

/* compiled from: ListUtils.java */
/* loaded from: classes.dex */
public class a {

    /* renamed from: b, reason: collision with root package name */
    private static final Uri f15878b = Uri.parse("content://com.oplus.romupdate.provider.db/update_list");

    /* renamed from: c, reason: collision with root package name */
    private static a f15879c;

    /* renamed from: a, reason: collision with root package name */
    private Context f15880a;

    private a(Context context) {
        this.f15880a = context;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x005a A[Catch: Exception -> 0x0041, TRY_LEAVE, TryCatch #1 {Exception -> 0x0041, blocks: (B:18:0x002f, B:20:0x0035, B:7:0x005a, B:5:0x0043), top: B:17:0x002f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String a(String str) {
        Cursor cursor;
        String string;
        String[] strArr = {ThermalControlConfig.COLUMN_NAME_XML};
        try {
            cursor = this.f15880a.getContentResolver().query(f15878b, strArr, "filtername=\"" + str + "\"", null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() > 0) {
                        int columnIndex = cursor.getColumnIndex(ThermalControlConfig.COLUMN_NAME_XML);
                        cursor.moveToNext();
                        string = cursor.getString(columnIndex);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (Exception e10) {
                    e = e10;
                    if (cursor != null) {
                        cursor.close();
                    }
                    Log.e("ListUtils", "We can not get Filtrate app data from provider,because of " + e);
                    return null;
                }
            }
            Log.e("ListUtils", "The Filtrate app cursor is null !!!  filterName=" + str);
            string = null;
            if (cursor != null) {
            }
            return string;
        } catch (Exception e11) {
            e = e11;
            cursor = null;
        }
    }

    public static a b(Context context) {
        if (f15879c == null) {
            f15879c = new a(context);
        }
        return f15879c;
    }

    public void c() {
        String a10 = a("sys_secure_keyboard_config");
        if (a10 != null) {
            LocalFileUtil.c().m("battery", "sys_secure_keyboard_config.xml", a10, this.f15880a);
        }
    }
}
