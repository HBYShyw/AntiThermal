package com.oplus.powermanager.fuelgaue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.coui.appcompat.cardlist.COUICardListHelper;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.battery.R;
import f6.f;

/* loaded from: classes.dex */
public class SmartChargeIncomePreference extends COUIPreference {
    private boolean F;
    private Context G;
    private TextView H;
    private TextView I;
    private BroadcastReceiver J;

    /* loaded from: classes.dex */
    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                SmartChargeIncomePreference.this.n();
            }
        }
    }

    public SmartChargeIncomePreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.F = false;
        this.J = new a();
        i(context);
    }

    private SpannableStringBuilder[] h(Context context, int i10) {
        int i11 = i10 % 60;
        int i12 = i10 / 60;
        int i13 = i12 / 24;
        int i14 = i12 % 24;
        int i15 = i11 > 0 ? 1 : 0;
        if (i14 > 0) {
            i15 += 2;
        }
        if (i13 > 0) {
            i15 += 4;
        }
        while (i15 > 0) {
            i15 &= i15 - 1;
        }
        int i16 = i14 > 9 ? 2 : 1;
        int i17 = i11 > 9 ? 2 : 1;
        String format = String.format(context.getString(R.string.smart_charge_mode_total_income_hour), Integer.valueOf(i14));
        String format2 = String.format(context.getString(R.string.smart_charge_mode_total_income_minute), Integer.valueOf(i11));
        String language = context.getResources().getConfiguration().locale.getLanguage();
        boolean z10 = language == "bo" || language == "sw" || language == "si";
        LocalLog.a("SmartChargeIncomePreference", "localLanguage: " + language);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(format2);
        if (i12 == 0) {
            spannableStringBuilder = null;
        } else {
            spannableStringBuilder.setSpan(new ForegroundColorSpan(this.G.getColor(R.color.reminder_text)), z10 ? format.length() - i16 : 0, z10 ? format.length() : i16, 34);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(40, true);
            int length = z10 ? format.length() - i16 : 0;
            if (z10) {
                i16 = format.length();
            }
            spannableStringBuilder.setSpan(absoluteSizeSpan, length, i16, 34);
        }
        spannableStringBuilder2.setSpan(new ForegroundColorSpan(this.G.getColor(R.color.reminder_text)), z10 ? format2.length() - i17 : 0, z10 ? format2.length() : i17, 34);
        AbsoluteSizeSpan absoluteSizeSpan2 = new AbsoluteSizeSpan(40, true);
        int length2 = z10 ? format2.length() - i17 : 0;
        if (z10) {
            i17 = format2.length();
        }
        spannableStringBuilder2.setSpan(absoluteSizeSpan2, length2, i17, 34);
        return new SpannableStringBuilder[]{spannableStringBuilder, spannableStringBuilder2};
    }

    private void i(Context context) {
        this.G = context;
        setLayoutResource(R.layout.smart_charge_income);
    }

    private void j(View view) {
        view.setForceDarkAllowed(false);
        this.H = (TextView) view.findViewById(R.id.income_hour);
        this.I = (TextView) view.findViewById(R.id.income_minute);
        n();
    }

    public void l() {
        if (this.F) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        this.G.registerReceiver(this.J, intentFilter, "oplus.permission.OPLUS_COMPONENT_SAFE", null, 2);
        this.F = true;
    }

    public void m() {
        if (this.F) {
            this.G.unregisterReceiver(this.J);
            this.F = false;
        }
    }

    public void n() {
        Typeface typeface;
        int max = Math.max(f.z0(this.G), 0) / 60;
        LocalLog.a("SmartChargeIncomePreference", "totalSmartChargeIncomeTimeMinute: " + max);
        SpannableStringBuilder[] h10 = h(this.G, max);
        try {
            typeface = Typeface.createFromAsset(this.G.getAssets(), "font/OplusSans2.14_No.ttf");
        } catch (Exception unused) {
            typeface = Typeface.DEFAULT;
        }
        TextView textView = this.H;
        if (textView != null) {
            if (h10[0] == null) {
                textView.setVisibility(4);
            } else {
                textView.setText(h10[0]);
                this.H.setTypeface(typeface);
            }
        }
        TextView textView2 = this.I;
        if (textView2 != null) {
            textView2.setText(h10[1]);
            this.I.setTypeface(typeface);
        }
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        j(preferenceViewHolder.itemView);
        COUICardListHelper.d(preferenceViewHolder.itemView, COUICardListHelper.b(this));
    }

    public SmartChargeIncomePreference(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.F = false;
        this.J = new a();
        i(context);
    }

    public SmartChargeIncomePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.F = false;
        this.J = new a();
        i(context);
    }

    public SmartChargeIncomePreference(Context context) {
        super(context);
        this.F = false;
        this.J = new a();
        i(context);
    }
}
