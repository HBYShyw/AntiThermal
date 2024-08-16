package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.BatteryManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.seekbar.COUISeekBar;
import com.oplus.battery.R;
import f6.f;

/* loaded from: classes.dex */
public class PowerSaveLevelPicker extends Preference {

    /* renamed from: r, reason: collision with root package name */
    private static int f10260r = 15;

    /* renamed from: e, reason: collision with root package name */
    private BatteryManager f10261e;

    /* renamed from: f, reason: collision with root package name */
    private Context f10262f;

    /* renamed from: g, reason: collision with root package name */
    private float f10263g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f10264h;

    /* renamed from: i, reason: collision with root package name */
    private volatile boolean f10265i;

    /* renamed from: j, reason: collision with root package name */
    private COUISeekBar f10266j;

    /* renamed from: k, reason: collision with root package name */
    private LinearLayout f10267k;

    /* renamed from: l, reason: collision with root package name */
    private TextView f10268l;

    /* renamed from: m, reason: collision with root package name */
    private int f10269m;

    /* renamed from: n, reason: collision with root package name */
    private int f10270n;

    /* renamed from: o, reason: collision with root package name */
    private float f10271o;

    /* renamed from: p, reason: collision with root package name */
    private TextView f10272p;

    /* renamed from: q, reason: collision with root package name */
    private TextView f10273q;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements COUISeekBar.h {

        /* renamed from: a, reason: collision with root package name */
        private boolean f10274a = true;

        a() {
        }

        private void d(int i10) {
            if (this.f10274a) {
                if (PowerSaveLevelPicker.this.f10261e == null) {
                    LocalLog.d("PowerSaveLevelPicker", "NullPointerException !");
                    return;
                }
                Settings.System.putIntForUser(PowerSaveLevelPicker.this.f10262f.getContentResolver(), "power_save_open_level", i10, 0);
                f.E3("power_save_open_level", String.valueOf(i10), PowerSaveLevelPicker.this.f10262f);
                int intProperty = PowerSaveLevelPicker.this.f10261e.getIntProperty(4);
                LocalLog.k("PowerSaveLevelPicker", "onStopTrackingTouch : level = " + i10 + " currentLevel = " + intProperty);
                if (PowerSaveLevelPicker.this.f10264h) {
                    return;
                }
                if (intProperty <= i10) {
                    Settings.System.putIntForUser(PowerSaveLevelPicker.this.f10262f.getContentResolver(), "power_save_mode_switch", 1, 0);
                    LocalLog.a("PowerSaveLevelPicker", "Open PowerSaveMode currentLevel = " + intProperty);
                }
                if (intProperty > i10) {
                    Settings.System.putIntForUser(PowerSaveLevelPicker.this.f10262f.getContentResolver(), "power_save_mode_switch", 0, 0);
                    LocalLog.a("PowerSaveLevelPicker", "Close PowerSaveMode currentLevel = " + intProperty);
                }
            }
        }

        @Override // com.coui.appcompat.seekbar.COUISeekBar.h
        public void a(COUISeekBar cOUISeekBar) {
            int progress = cOUISeekBar.getProgress() + 5;
            this.f10274a = true;
            d(progress);
        }

        @Override // com.coui.appcompat.seekbar.COUISeekBar.h
        public void b(COUISeekBar cOUISeekBar, int i10, boolean z10) {
            PowerSaveLevelPicker.this.f10268l.setText(NumberFormat.getPercentInstance().format(r4 / 100.0f));
            PowerSaveLevelPicker.this.f10268l.setTextColor(PowerSaveLevelPicker.this.getContext().getColor(R.color.battery_ui_text_color_light));
            LocalLog.a("PowerSaveLevelPicker", "fromUser=" + z10);
            d(i10 + 5);
        }

        @Override // com.coui.appcompat.seekbar.COUISeekBar.h
        public void c(COUISeekBar cOUISeekBar) {
            PowerSaveLevelPicker.this.f10268l.setText(NumberFormat.getPercentInstance().format((cOUISeekBar.getProgress() + 5) / 100.0f));
            this.f10274a = false;
        }
    }

    public PowerSaveLevelPicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.f10261e = null;
        this.f10262f = null;
        this.f10263g = 0.0f;
        this.f10264h = false;
        this.f10265i = false;
        this.f10266j = null;
        this.f10268l = null;
        this.f10272p = null;
        this.f10273q = null;
        this.f10262f = context;
        this.f10263g = context.getResources().getDisplayMetrics().density;
        this.f10261e = (BatteryManager) this.f10262f.getSystemService("batterymanager");
        setLayoutResource(R.layout.power_save_level_picker);
    }

    public static int g() {
        return f10260r;
    }

    private void i(View view) {
        if (this.f10265i) {
            this.f10265i = false;
            return;
        }
        this.f10267k = (LinearLayout) view.findViewById(R.id.seekbar_layout);
        COUISeekBar cOUISeekBar = (COUISeekBar) view.findViewById(R.id.power_save_level_seek_bar);
        this.f10266j = cOUISeekBar;
        cOUISeekBar.setMax(70);
        int intForUser = Settings.System.getIntForUser(this.f10262f.getContentResolver(), "power_save_open_level", f10260r, 0);
        if (intForUser < 0) {
            intForUser = f10260r;
        }
        int i10 = intForUser - 5;
        this.f10266j.setProgress(i10);
        LocalLog.a("PowerSaveLevelPicker", "setProgress " + i10);
        this.f10263g = this.f10262f.getResources().getDisplayMetrics().density;
        TextView textView = (TextView) view.findViewById(R.id.power_save_level);
        this.f10268l = textView;
        textView.setVisibility(0);
        String format = NumberFormat.getPercentInstance().format(intForUser / 100.0f);
        String string = this.f10262f.getString(R.string.level_picker_middle, 5);
        String string2 = this.f10262f.getString(R.string.level_picker_middle, 75);
        this.f10272p = (TextView) view.findViewById(R.id.level_picker_begin);
        this.f10273q = (TextView) view.findViewById(R.id.level_picker_end);
        this.f10272p.setText(string);
        this.f10273q.setText(string2);
        this.f10268l.setText(format);
        if (view.isLayoutRequested()) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            this.f10269m = this.f10272p.getMeasuredWidth();
            this.f10270n = this.f10273q.getMeasuredWidth();
        }
        h(false);
        this.f10266j.setOnSeekBarChangeListener(new a());
    }

    public static void l(int i10) {
        f10260r = i10;
    }

    public void h(boolean z10) {
        if (this.f10267k != null) {
            this.f10271o = 0.0f;
        }
    }

    public void j(boolean z10) {
        this.f10265i = z10;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        i(preferenceViewHolder.itemView);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    public PowerSaveLevelPicker(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0);
    }

    public PowerSaveLevelPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }
}
