package i9;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import h9.SettingsColumns;
import l9.LogUtils;

/* compiled from: SettingInterface.java */
/* renamed from: i9.e, reason: use source file name */
/* loaded from: classes2.dex */
public class SettingInterface {
    public static void a(Activity activity, int i10) {
        if (!c()) {
            Intent intent = new Intent();
            intent.setAction("oplus.intent.action.SCENE_SERVICE_STATEMENT");
            intent.setFlags(67108864);
            intent.putExtra("from_activity", activity.getComponentName());
            try {
                activity.startActivityForResult(l9.a.a(activity, intent), i10);
                return;
            } catch (Exception e10) {
                LogUtils.b("SettingInterface", "authorizeStatementState:" + e10);
                return;
            }
        }
        LogUtils.a("SettingInterface", "SceneService have privacy ");
    }

    private static String b(String str) {
        String[] strArr = {str};
        String str2 = null;
        try {
            Cursor e10 = g9.a.e(SettingsColumns.f12020a, new String[]{"key", ThermalBaseConfig.Item.ATTR_VALUE}, "key=?", strArr, null);
            if (e10 != null) {
                try {
                    if (e10.moveToNext()) {
                        str2 = e10.getString(1);
                    }
                } finally {
                }
            }
            if (e10 != null) {
                e10.close();
            }
        } catch (Throwable th) {
            LogUtils.b("SettingInterface", "getKeyValue: throwable = " + th);
        }
        return str2;
    }

    public static boolean c() {
        try {
            return "1".equals(b("scene_service_statement_state"));
        } catch (Exception e10) {
            LogUtils.b("SettingInterface", "" + e10);
            return true;
        }
    }
}
