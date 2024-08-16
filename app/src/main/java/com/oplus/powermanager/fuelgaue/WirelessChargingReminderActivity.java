package com.oplus.powermanager.fuelgaue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import b6.LocalLog;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.battery.R;
import f6.ChargeUtil;
import f6.f;
import java.util.Collections;
import java.util.Map;
import y5.AppFeature;

/* loaded from: classes.dex */
public class WirelessChargingReminderActivity extends Activity {

    /* renamed from: e, reason: collision with root package name */
    private String f10185e;

    /* renamed from: f, reason: collision with root package name */
    private EffectiveAnimationView f10186f;

    /* renamed from: g, reason: collision with root package name */
    private ImageView f10187g;

    /* renamed from: h, reason: collision with root package name */
    private Context f10188h;

    /* loaded from: classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            WirelessChargingReminderActivity.this.finish();
        }
    }

    @Override // android.app.Activity
    public void finish() {
        LocalLog.a("WirelessChargingReminderActivity", "finish: ");
        this.f10186f.j();
        super.finish();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        int i10;
        int i11;
        Float f10;
        LocalLog.a("WirelessChargingReminderActivity", "onCreate: ");
        super.onCreate(bundle);
        this.f10188h = getApplicationContext();
        String h10 = ChargeUtil.h();
        Map unmodifiableMap = Collections.unmodifiableMap(ChargeUtil.o());
        this.f10185e = "wireless_charge_guide_animation.json";
        int w10 = AppFeature.w();
        int i12 = R.layout.wireless_charging_reminder_normal;
        boolean z10 = false;
        if (w10 != 1) {
            if (w10 == 2 && (f10 = (Float) unmodifiableMap.get(h10)) != null) {
                r5 = f10.floatValue();
            }
            i10 = R.layout.wireless_charging_reminder_normal;
        } else if (f.g1(this.f10188h)) {
            i10 = R.layout.wireless_charging_reminder_fold_big;
            this.f10185e = "wireless_charge_guide_animation_no_line.json";
            h10 = h10 + "_BIG";
            Float f11 = (Float) unmodifiableMap.get(h10);
            r5 = f11 != null ? f11.floatValue() : -1.0f;
            z10 = true;
        } else {
            i10 = R.layout.wireless_charging_reminder_fold_small;
            h10 = h10 + "_SMALL";
            Float f12 = (Float) unmodifiableMap.get(h10);
            if (f12 != null) {
                r5 = f12.floatValue();
            }
        }
        LocalLog.a("WirelessChargingReminderActivity", "getWirelessChargeGuideConfigInfo: model = " + h10 + ", marginBottomDp = " + r5);
        if (!y5.b.D()) {
            i12 = i10;
        }
        setContentView(i12);
        this.f10186f = (EffectiveAnimationView) findViewById(R.id.wireless_charging_guide_panel_animation);
        if (r5 >= 0.0f) {
            DisplayMetrics displayMetrics = this.f10188h.getResources().getDisplayMetrics();
            if (this.f10188h.getResources().getConfiguration().orientation == 1) {
                i11 = displayMetrics.widthPixels;
            } else {
                i11 = displayMetrics.heightPixels;
            }
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.f10186f.getLayoutParams();
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = ChargeUtil.b(r5, i11);
            ((ViewGroup.MarginLayoutParams) layoutParams).width = i11;
            if (z10) {
                i11 = (i11 * 1080) / 2094;
            }
            ((ViewGroup.MarginLayoutParams) layoutParams).height = i11;
            LocalLog.a("WirelessChargingReminderActivity", "getWirelessChargeGuideConfigInfo: bottomMargin = " + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + ", width = " + ((ViewGroup.MarginLayoutParams) layoutParams).width + ", height = " + ((ViewGroup.MarginLayoutParams) layoutParams).height);
        }
        if (!this.f10186f.r()) {
            this.f10186f.setAnimation(this.f10185e);
            this.f10186f.u();
        }
        ImageView imageView = (ImageView) findViewById(R.id.close);
        this.f10187g = imageView;
        imageView.setOnClickListener(new a());
    }

    @Override // android.app.Activity
    public void onDestroy() {
        LocalLog.a("WirelessChargingReminderActivity", "onDestroy: ");
        this.f10186f.j();
        super.onDestroy();
    }

    @Override // android.app.Activity
    public void onPause() {
        LocalLog.b("WirelessChargingReminderActivity", "onPause");
        this.f10186f.j();
        super.onPause();
    }

    @Override // android.app.Activity
    public void onResume() {
        LocalLog.a("WirelessChargingReminderActivity", "onResume");
        super.onResume();
        if (this.f10186f.r()) {
            return;
        }
        this.f10186f.setAnimation(this.f10185e);
        this.f10186f.u();
    }

    @Override // android.app.Activity
    public void onStop() {
        LocalLog.b("WirelessChargingReminderActivity", "onStop");
        super.onStop();
    }
}
