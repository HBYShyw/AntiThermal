package com.oplus.powermanager.fuelgaue;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import b6.LocalLog;
import com.coui.appcompat.panel.COUIBottomSheetDialog;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.battery.R;
import f2.COUINavigationBarUtil;
import f6.f;
import java.lang.ref.WeakReference;
import w1.COUIDarkModeUtil;

/* loaded from: classes.dex */
public class WirelessReverseChargingReminderActivity extends AppCompatActivity {

    /* renamed from: g, reason: collision with root package name */
    private EffectiveAnimationView f10192g;

    /* renamed from: h, reason: collision with root package name */
    private COUIBottomSheetDialog f10193h;

    /* renamed from: i, reason: collision with root package name */
    private Context f10194i;

    /* renamed from: j, reason: collision with root package name */
    private c f10195j;

    /* renamed from: e, reason: collision with root package name */
    private final String f10190e = "guide_animation.json";

    /* renamed from: f, reason: collision with root package name */
    private final String f10191f = "guide_animation_dark.json";

    /* renamed from: k, reason: collision with root package name */
    private BroadcastReceiver f10196k = new a();

    /* renamed from: l, reason: collision with root package name */
    private ContentObserver f10197l = new b(new Handler());

    /* loaded from: classes.dex */
    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                if (WirelessReverseChargingReminderActivity.this.isFinishing()) {
                    return;
                }
                WirelessReverseChargingReminderActivity.this.finish();
            } else if ("android.intent.action.BATTERY_CHANGED".equals(action) && intent.getIntExtra("wireless_reverse_chg_type", 3) == 1 && !WirelessReverseChargingReminderActivity.this.isFinishing()) {
                WirelessReverseChargingReminderActivity.this.finish();
            }
        }
    }

    /* loaded from: classes.dex */
    class b extends ContentObserver {
        b(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z10) {
            if (f.d1(WirelessReverseChargingReminderActivity.this.f10194i) || WirelessReverseChargingReminderActivity.this.isFinishing()) {
                return;
            }
            WirelessReverseChargingReminderActivity.this.finish();
        }
    }

    /* loaded from: classes.dex */
    public static class c implements DialogInterface.OnDismissListener {

        /* renamed from: e, reason: collision with root package name */
        WeakReference<Activity> f10200e;

        c(Activity activity) {
            this.f10200e = new WeakReference<>(activity);
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            if (this.f10200e.get() == null || this.f10200e.get().isFinishing()) {
                return;
            }
            this.f10200e.get().finish();
        }
    }

    private void g() {
        Window window = getWindow();
        if (window != null) {
            window.getDecorView();
            window.clearFlags(201326592);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.setNavigationBarColor(Color.parseColor("#01ffffff"));
        }
    }

    private void h() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.f10196k, intentFilter, "oplus.permission.OPLUS_COMPONENT_SAFE", null, 2);
    }

    private void i() {
        if (COUINavigationBarUtil.b(this)) {
            Window window = this.f10193h.getWindow();
            if (window != null) {
                View decorView = window.getDecorView();
                window.clearFlags(201326592);
                decorView.setSystemUiVisibility(772);
                window.setNavigationBarColor(this.f10194i.getColor(R.color.navigationbar_color));
            }
            this.f10193h.S0().setPadding(0, 0, 0, COUINavigationBarUtil.a(this.f10194i));
        }
    }

    private void k() {
        unregisterReceiver(this.f10196k);
    }

    @Override // android.app.Activity
    public void finish() {
        LocalLog.a("WirelessReverseChargingReminderActivity", "finish: ");
        super.finish();
        overridePendingTransition(0, 0);
    }

    public void j(int i10) {
        this.f10194i.getContentResolver().registerContentObserver(Settings.System.getUriFor("wireless_reverse_charging_state"), false, this.f10197l, 0);
        COUIBottomSheetDialog cOUIBottomSheetDialog = this.f10193h;
        if (cOUIBottomSheetDialog != null && cOUIBottomSheetDialog.isShowing()) {
            this.f10193h.dismiss();
        }
        View inflate = getLayoutInflater().inflate(i10, (ViewGroup) null);
        EffectiveAnimationView effectiveAnimationView = (EffectiveAnimationView) inflate.findViewById(R.id.wireless_reverse_guide_panel_animation);
        this.f10192g = effectiveAnimationView;
        effectiveAnimationView.s(true);
        if (COUIDarkModeUtil.a(this.f10194i)) {
            this.f10192g.setImageAssetsFolder("images/");
            this.f10192g.setAnimation("guide_animation_dark.json");
        } else {
            this.f10192g.setAnimation("guide_animation.json");
        }
        this.f10192g.u();
        COUIBottomSheetDialog cOUIBottomSheetDialog2 = new COUIBottomSheetDialog(this, R.style.DefaultBottomSheetDialog);
        this.f10193h = cOUIBottomSheetDialog2;
        cOUIBottomSheetDialog2.setContentView(inflate);
        this.f10193h.setCanceledOnTouchOutside(true);
        c cVar = new c(this);
        this.f10195j = cVar;
        this.f10193h.setOnDismissListener(cVar);
        this.f10193h.show();
        i();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        LocalLog.a("WirelessReverseChargingReminderActivity", "onCreate: ");
        g();
        super.onCreate(bundle);
        this.f10194i = getApplicationContext();
        h();
        j(R.layout.wireless_reverse_reminder);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        LocalLog.a("WirelessReverseChargingReminderActivity", "onDestroy: ");
        k();
        this.f10194i.getContentResolver().unregisterContentObserver(this.f10197l);
        super.onDestroy();
    }
}
