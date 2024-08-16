package com.oplus.powermanager.fuelgaue.basic.customized;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.util.AttributeSet;
import androidx.preference.PreferenceViewHolder;
import b6.LocalLog;
import com.coui.appcompat.picker.COUINumberPicker;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.battery.R;
import java.util.Locale;

/* loaded from: classes.dex */
public class WirelessReverseLevelPicker extends COUIPreference {
    private static String[] K;
    private final String[] F;
    private Locale G;
    private int H;
    private c I;
    private boolean J;

    /* loaded from: classes.dex */
    class a implements COUINumberPicker.f {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ COUINumberPicker f10276a;

        a(COUINumberPicker cOUINumberPicker) {
            this.f10276a = cOUINumberPicker;
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.f
        public void a(COUINumberPicker cOUINumberPicker, int i10, int i11) {
            if (LocalLog.f()) {
                LocalLog.a("WirelessReverseLevelPicker", "mIsScrollStopped=" + WirelessReverseLevelPicker.this.J + " val:" + this.f10276a.getValue());
            }
            if (WirelessReverseLevelPicker.this.J) {
                c cVar = WirelessReverseLevelPicker.this.I;
                COUINumberPicker cOUINumberPicker2 = this.f10276a;
                cVar.T(cOUINumberPicker2, cOUINumberPicker2.getValue());
            }
        }
    }

    /* loaded from: classes.dex */
    class b implements COUINumberPicker.d {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ COUINumberPicker f10278a;

        b(COUINumberPicker cOUINumberPicker) {
            this.f10278a = cOUINumberPicker;
        }

        @Override // com.coui.appcompat.picker.COUINumberPicker.d
        public void a(COUINumberPicker cOUINumberPicker, int i10) {
            if (LocalLog.f()) {
                LocalLog.a("WirelessReverseLevelPicker", "state=" + i10 + " val:" + this.f10278a.getValue());
            }
            WirelessReverseLevelPicker.this.J = i10 == 0;
            if (WirelessReverseLevelPicker.this.J) {
                c cVar = WirelessReverseLevelPicker.this.I;
                COUINumberPicker cOUINumberPicker2 = this.f10278a;
                cVar.T(cOUINumberPicker2, cOUINumberPicker2.getValue());
            }
        }
    }

    /* loaded from: classes.dex */
    public interface c {
        void T(COUINumberPicker cOUINumberPicker, int i10);
    }

    public WirelessReverseLevelPicker(Context context) {
        super(context);
        this.F = new String[]{"25%", "30%", "35%", "40%", "45%", "50%", "55%", "60%", "65%", "70%", "75%", "80%", "85%", "90%", "95%"};
        this.G = null;
        this.H = 25;
        l();
    }

    private void l() {
        setLayoutResource(R.layout.dialog_bottom_picker);
        m();
    }

    private void m() {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        K = new String[15];
        int i10 = 0;
        int i11 = 25;
        while (i11 <= 95 && i10 < 15) {
            K[i10] = percentInstance.format(i11 / 100.0f);
            i11 += 5;
            i10++;
        }
        if (i10 != 15) {
            K = this.F;
        }
        this.G = Locale.getDefault();
    }

    public void n(int i10) {
        this.H = i10;
    }

    public void o(c cVar) {
        this.I = cVar;
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        if (!Locale.getDefault().equals(this.G)) {
            m();
        }
        COUINumberPicker cOUINumberPicker = (COUINumberPicker) preferenceViewHolder.itemView.findViewById(R.id.normal_bottom_picker);
        if (cOUINumberPicker == null) {
            return;
        }
        cOUINumberPicker.setDisplayedValues(K);
        cOUINumberPicker.setMaxValue(K.length - 1);
        cOUINumberPicker.setMinValue(0);
        cOUINumberPicker.setValue((this.H - 25) / 5);
        cOUINumberPicker.setWrapSelectorWheel(true);
        cOUINumberPicker.setHasBackground(true);
        cOUINumberPicker.setDescendantFocusability(393216);
        cOUINumberPicker.setOnValueChangedListener(new a(cOUINumberPicker));
        cOUINumberPicker.setOnScrollListener(new b(cOUINumberPicker));
    }

    public WirelessReverseLevelPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.F = new String[]{"25%", "30%", "35%", "40%", "45%", "50%", "55%", "60%", "65%", "70%", "75%", "80%", "85%", "90%", "95%"};
        this.G = null;
        this.H = 25;
        l();
    }

    public WirelessReverseLevelPicker(Context context, AttributeSet attributeSet, int i10) {
        super(context, attributeSet, i10);
        this.F = new String[]{"25%", "30%", "35%", "40%", "45%", "50%", "55%", "60%", "65%", "70%", "75%", "80%", "85%", "90%", "95%"};
        this.G = null;
        this.H = 25;
        l();
    }

    public WirelessReverseLevelPicker(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        this.F = new String[]{"25%", "30%", "35%", "40%", "45%", "50%", "55%", "60%", "65%", "70%", "75%", "80%", "85%", "90%", "95%"};
        this.G = null;
        this.H = 25;
        l();
    }
}
