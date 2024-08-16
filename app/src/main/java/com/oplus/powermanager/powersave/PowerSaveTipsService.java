package com.oplus.powermanager.powersave;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.battery.R;
import com.oplus.content.OplusFeatureConfigManager;
import f6.f;
import java.util.ArrayList;
import x1.COUIAlertDialogBuilder;

/* loaded from: classes2.dex */
public class PowerSaveTipsService extends Service {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class a implements DialogInterface.OnClickListener {
        a() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i10) {
        }
    }

    private static int a(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_backlight_switch_state", 1, 0);
    }

    private static int b(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_screen_refresh_rate", 1, 0);
    }

    private static int c(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_screenoff_time_state", 1, 0);
    }

    private static int d(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(), "power_save_sync_state", 1, 0);
    }

    private Context e(Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.Theme_Demo);
        COUIThemeOverlay.i().b(contextThemeWrapper);
        return contextThemeWrapper;
    }

    private boolean f(Context context) {
        return OplusFeatureConfigManager.getInstance().hasFeature("oplus.software.display.dynamic_fps_switch");
    }

    private void g() {
        COUIAlertDialogBuilder cOUIAlertDialogBuilder = new COUIAlertDialogBuilder(e(this), f.k(this));
        cOUIAlertDialogBuilder.h0(R.string.power_save_tips_title);
        View inflate = LayoutInflater.from(this).inflate(R.layout.power_save_tips_dialog, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.suggest_content);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        String[] stringArray = getResources().getStringArray(R.array.power_saving_mode_tips_items);
        StringBuilder sb2 = new StringBuilder();
        ArrayList arrayList = new ArrayList();
        boolean z10 = f.t1(this) && f.G(this) == 1;
        boolean z11 = a(this) == 1;
        boolean z12 = c(this) == 1;
        boolean z13 = d(this) == 1;
        boolean z14 = f(this) && b(this) == 1;
        if (z10) {
            arrayList.add(0);
        }
        if (z11) {
            arrayList.add(1);
        }
        if (z12) {
            arrayList.add(2);
        }
        if (z13) {
            arrayList.add(3);
        }
        if (z14) {
            arrayList.add(4);
        }
        for (int i10 = 0; i10 < arrayList.size(); i10++) {
            sb2.append(stringArray[((Integer) arrayList.get(i10)).intValue()] + "\n");
        }
        sb2.append("\n");
        sb2.append(getResources().getString(R.string.power_save_tips_more));
        textView.setText(sb2.toString());
        cOUIAlertDialogBuilder.v(inflate);
        cOUIAlertDialogBuilder.c0(R.string.power_save_tips_known, new a());
        AlertDialog a10 = cOUIAlertDialogBuilder.a();
        Window window = a10.getWindow();
        a10.setCancelable(false);
        window.setType(2038);
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (window.getDecorView() != null) {
            window.getDecorView().setSystemUiVisibility(1280);
        }
        try {
            f.n2(attributes, 1);
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        window.setAttributes(attributes);
        a10.show();
        f.v3(a10.findViewById(R.id.customPanel), 1.0f);
        f.v3(a10.findViewById(R.id.buttonPanel), 0.0f);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i10, int i11) {
        g();
        return 2;
    }
}
