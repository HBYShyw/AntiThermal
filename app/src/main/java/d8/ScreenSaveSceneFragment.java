package d8;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.Preference;
import b6.LocalLog;
import com.coui.appcompat.preference.COUIMarkPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;

/* compiled from: ScreenSaveSceneFragment.java */
/* renamed from: d8.e, reason: use source file name */
/* loaded from: classes.dex */
public class ScreenSaveSceneFragment extends BasePreferenceFragment implements Preference.c {

    /* renamed from: g, reason: collision with root package name */
    private COUIMarkPreference f10821g;

    /* renamed from: h, reason: collision with root package name */
    private COUIMarkPreference f10822h;

    /* renamed from: i, reason: collision with root package name */
    private COUIMarkPreference f10823i;

    /* renamed from: e, reason: collision with root package name */
    private ContentResolver f10819e = null;

    /* renamed from: f, reason: collision with root package name */
    private int f10820f = 1;

    /* renamed from: j, reason: collision with root package name */
    private Activity f10824j = null;

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.screen_save_title);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        LocalLog.a("ScreenSaveSceneFragment", "onAttach");
        super.onAttach(activity);
        this.f10824j = activity;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        COUIMarkPreference cOUIMarkPreference = (COUIMarkPreference) findPreference("screen_save_balance");
        this.f10822h = cOUIMarkPreference;
        cOUIMarkPreference.setOnPreferenceChangeListener(this);
        COUIMarkPreference cOUIMarkPreference2 = (COUIMarkPreference) findPreference("screen_save_close");
        this.f10821g = cOUIMarkPreference2;
        cOUIMarkPreference2.setOnPreferenceChangeListener(this);
        COUIMarkPreference cOUIMarkPreference3 = (COUIMarkPreference) findPreference("screen_save_power");
        this.f10823i = cOUIMarkPreference3;
        cOUIMarkPreference3.setOnPreferenceChangeListener(this);
        ContentResolver contentResolver = this.f10824j.getContentResolver();
        this.f10819e = contentResolver;
        try {
            this.f10820f = Settings.System.getIntForUser(contentResolver, "lcd_cabc_mode", 0);
        } catch (Settings.SettingNotFoundException unused) {
            this.f10820f = 1;
            Settings.System.putIntForUser(this.f10819e, "lcd_cabc_mode", 1, 0);
        }
        LocalLog.a("ScreenSaveSceneFragment", "get Settings Provider lcd cabc mode: " + this.f10820f);
        int i10 = this.f10820f;
        if (i10 == 1) {
            this.f10821g.setSelectable(false);
            this.f10822h.setSelectable(true);
            this.f10823i.setSelectable(true);
            this.f10821g.setChecked(true);
            this.f10822h.setChecked(false);
            this.f10823i.setChecked(false);
            return;
        }
        if (i10 == 2) {
            this.f10822h.setSelectable(false);
            this.f10821g.setSelectable(true);
            this.f10823i.setSelectable(true);
            this.f10822h.setChecked(true);
            this.f10821g.setChecked(false);
            this.f10823i.setChecked(false);
            return;
        }
        if (i10 != 3) {
            return;
        }
        this.f10823i.setSelectable(false);
        this.f10822h.setSelectable(true);
        this.f10821g.setSelectable(true);
        this.f10823i.setChecked(true);
        this.f10821g.setChecked(false);
        this.f10822h.setChecked(false);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.screen_save_scene);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        LocalLog.a("ScreenSaveSceneFragment", "onDetach");
        super.onDetach();
        this.f10824j = null;
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            String key = preference.getKey();
            if (key == null) {
                LocalLog.a("ScreenSaveSceneFragment", "onPreferenceChange: key is null.");
                return false;
            }
            LocalLog.a("ScreenSaveSceneFragment", "onPreferenceChange: key=" + key + ", check=" + booleanValue);
            if ("screen_save_close".equals(key)) {
                if (booleanValue) {
                    this.f10821g.setSelectable(false);
                    this.f10822h.setSelectable(true);
                    this.f10823i.setSelectable(true);
                    this.f10821g.setChecked(true);
                    this.f10822h.setChecked(false);
                    this.f10823i.setChecked(false);
                    Settings.System.putIntForUser(this.f10819e, "lcd_cabc_mode", 1, 0);
                }
            } else if ("screen_save_balance".equals(key)) {
                if (booleanValue) {
                    this.f10822h.setSelectable(false);
                    this.f10821g.setSelectable(true);
                    this.f10823i.setSelectable(true);
                    this.f10822h.setChecked(true);
                    this.f10821g.setChecked(false);
                    this.f10823i.setChecked(false);
                    Settings.System.putIntForUser(this.f10819e, "lcd_cabc_mode", 2, 0);
                }
            } else if ("screen_save_power".equals(key) && booleanValue) {
                this.f10823i.setSelectable(false);
                this.f10822h.setSelectable(true);
                this.f10821g.setSelectable(true);
                this.f10823i.setChecked(true);
                this.f10821g.setChecked(false);
                this.f10822h.setChecked(false);
                Settings.System.putIntForUser(this.f10819e, "lcd_cabc_mode", 3, 0);
            }
            return true;
        }
        LocalLog.a("ScreenSaveSceneFragment", "onPreferenceChange: value is not Boolean.");
        return false;
    }
}
