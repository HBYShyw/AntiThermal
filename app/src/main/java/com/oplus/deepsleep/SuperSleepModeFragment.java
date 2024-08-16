package com.oplus.deepsleep;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import com.coui.appcompat.preference.COUIMarkPreference;
import com.oplus.battery.R;
import com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment;
import com.oplus.util.OplusLog;
import f6.f;
import x1.COUIAlertDialogBuilder;

/* loaded from: classes.dex */
public class SuperSleepModeFragment extends BasePreferenceFragment implements Preference.c {
    private static final String KEY_SLEEP_MODE_BALANCE = "sleep_balance_mode";
    private static final String KEY_SLEEP_MODE_CLOSE = "sleep_close_mode";
    private static final String KEY_SLEEP_MODE_SUPER = "sleep_super_mode";
    private static final int STATE_BALANCE = 1;
    private static final int STATE_CLOSE = 2;
    private static final int STATE_SUPER = 0;
    private static final String TAG = "SuperSleepModeFragment";
    private Context mContext;
    private DeepSleepUtils mDeepSleepUtils;
    private AlertDialog mSuperStateDialog;
    private COUIMarkPreference mSleepBalanceMode = null;
    private COUIMarkPreference mSleepSuperMode = null;
    private COUIMarkPreference mSleepCloseMode = null;

    private void initPreference() {
        this.mSleepSuperMode = (COUIMarkPreference) findPreference(KEY_SLEEP_MODE_SUPER);
        this.mSleepBalanceMode = (COUIMarkPreference) findPreference(KEY_SLEEP_MODE_BALANCE);
        this.mSleepCloseMode = (COUIMarkPreference) findPreference(KEY_SLEEP_MODE_CLOSE);
    }

    private void openSuperModeDailog() {
        if (f.p0(this.mContext) == 0) {
            return;
        }
        Context context = this.mContext;
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(context, f.k(context));
        cOUIAlertDialogBuilder.h0(R.string.intelligent_sleep_super_dialog_title);
        cOUIAlertDialogBuilder.Y(R.string.intelligent_sleep_super_dialog_content);
        cOUIAlertDialogBuilder.e0(R.string.intelligent_sleep_super_dialog_open, new DialogInterface.OnClickListener() { // from class: com.oplus.deepsleep.SuperSleepModeFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i10) {
                f.E3("intelligent_deep_sleep_mode", "0", SuperSleepModeFragment.this.mContext.getApplicationContext());
                f.f2(SuperSleepModeFragment.this.mContext, false);
                SuperSleepModeFragment.this.mDeepSleepUtils.setExtremeSleepStatus(1);
                f.L2(SuperSleepModeFragment.this.mContext, 0);
                SuperSleepModeFragment.this.updatePreference();
            }
        });
        cOUIAlertDialogBuilder.a0(R.string.high_performance_cancel, new DialogInterface.OnClickListener() { // from class: com.oplus.deepsleep.SuperSleepModeFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i10) {
            }
        });
        cOUIAlertDialogBuilder.d(true);
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        this.mSuperStateDialog = a10;
        Window window = a10.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        try {
            f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        this.mSuperStateDialog.show();
    }

    private void updateIntellState(int i10) {
        if (i10 == 0) {
            openSuperModeDailog();
        } else if (i10 == 1) {
            f.E3("intelligent_deep_sleep_mode", "1", this.mContext.getApplicationContext());
            f.f2(this.mContext, true);
            this.mDeepSleepUtils.setExtremeSleepStatus(0);
            f.L2(this.mContext, 1);
        } else if (i10 == 2) {
            f.E3("intelligent_deep_sleep_mode", "0", this.mContext.getApplicationContext());
            f.f2(this.mContext, false);
            this.mDeepSleepUtils.setExtremeSleepStatus(0);
            f.L2(this.mContext, 2);
        }
        updatePreference();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePreference() {
        if (this.mSleepSuperMode == null || this.mSleepBalanceMode == null || this.mSleepCloseMode == null) {
            return;
        }
        int p02 = f.p0(this.mContext);
        if (this.mDeepSleepUtils.getExtremeSleepStatus()) {
            p02 = 0;
        }
        OplusLog.d(TAG, "updatePreference mode = " + p02);
        if (p02 == 0) {
            this.mSleepSuperMode.setChecked(true);
            this.mSleepBalanceMode.setChecked(false);
            this.mSleepCloseMode.setChecked(false);
        } else if (p02 == 1) {
            this.mSleepSuperMode.setChecked(false);
            this.mSleepBalanceMode.setChecked(true);
            this.mSleepCloseMode.setChecked(false);
        } else {
            if (p02 != 2) {
                return;
            }
            this.mSleepSuperMode.setChecked(false);
            this.mSleepBalanceMode.setChecked(false);
            this.mSleepCloseMode.setChecked(true);
        }
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.lifecycle.HasDefaultViewModelProviderFactory
    public /* bridge */ /* synthetic */ w.a getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment
    public String getTitle() {
        return getString(R.string.intelligent_sleep_mode);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDeepSleepUtils = DeepSleepUtils.getInstance(this.mContext);
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        addPreferencesFromResource(R.xml.intelligent_sleep_mode_preference);
    }

    @Override // com.oplus.powermanager.fuelgaue.base.BasePreferenceFragment, com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.preference.Preference.c
    public boolean onPreferenceChange(Preference preference, Object obj) {
        return false;
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.c
    public boolean onPreferenceTreeClick(Preference preference) {
        int i10;
        String key = preference.getKey();
        OplusLog.d(TAG, "onPreferenceClick key = " + key);
        if (KEY_SLEEP_MODE_SUPER.equals(key)) {
            i10 = 0;
        } else if (KEY_SLEEP_MODE_BALANCE.equals(key)) {
            i10 = 1;
        } else {
            i10 = KEY_SLEEP_MODE_CLOSE.equals(key) ? 2 : -1;
        }
        updateIntellState(i10);
        return true;
    }

    @Override // com.oplus.powermanager.fuelgaue.base.OplusHighlightPreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        updatePreference();
    }

    @Override // com.coui.appcompat.preference.COUIPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initPreference();
    }
}
