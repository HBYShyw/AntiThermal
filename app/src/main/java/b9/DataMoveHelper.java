package b9;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import b6.LocalLog;
import com.oplus.deepsleep.DeepSleepUtils;
import com.oplus.deepsleep.data.DeepSleepSharepref;

/* compiled from: DataMoveHelper.java */
/* renamed from: b9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class DataMoveHelper {

    /* renamed from: a, reason: collision with root package name */
    private Context f4598a;

    public DataMoveHelper(Context context) {
        this.f4598a = context;
    }

    private void b() {
        LocalLog.a("DataMoveHelper", "move data start");
        SharedPreferences sharedPreferences = this.f4598a.getSharedPreferences(DeepSleepSharepref.DEEP_SLEEP_SHAREPREF_FILE, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(DeepSleepUtils.STR_NETWORK_TYPE, sharedPreferences.getInt("oppoguardelf_deepsleep_network_type", 0));
        edit.putInt(DeepSleepUtils.STR_NETWORK_TYPE_BAK, sharedPreferences.getInt("oppoguardelf_deepsleep_network_type_bak", 0));
        edit.putInt(DeepSleepUtils.STR_NETWORK_SWITCH_STATE, sharedPreferences.getInt("oppoguardelf_deepsleep_network_switch", 0));
        edit.remove("oppoguardelf_deepsleep_network_type");
        edit.remove("oppoguardelf_deepsleep_network_type_bak");
        edit.remove("oppoguardelf_deepsleep_network_switch");
        edit.apply();
        SharedPreferences sharedPreferences2 = this.f4598a.getSharedPreferences("superPowersaveSharePref", 0);
        SharedPreferences.Editor edit2 = sharedPreferences2.edit();
        edit2.putInt("backup_oplus_color_mode_state", sharedPreferences2.getInt("backup_oppo_color_mode_state", 0));
        edit2.putBoolean("backup_oplus_color_mode_set", sharedPreferences2.getBoolean("backup_oppo_color_mode_set", false));
        edit2.remove("backup_oppo_color_mode_state");
        edit2.remove("backup_oppo_color_mode_set");
        edit2.apply();
    }

    public boolean a() {
        if (Settings.System.getIntForUser(this.f4598a.getContentResolver(), "settings_oplus_battery_need_move_data", -100, 0) == -100) {
            b();
            Settings.System.putIntForUser(this.f4598a.getContentResolver(), "settings_oplus_battery_need_move_data", 1, 0);
            return true;
        }
        LocalLog.a("DataMoveHelper", "don't need move data");
        return false;
    }
}
