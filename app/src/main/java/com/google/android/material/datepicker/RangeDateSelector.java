package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import com.google.android.material.R$attr;
import com.google.android.material.R$dimen;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$string;
import com.google.android.material.internal.ManufacturerUtils;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import z3.MaterialAttributes;

/* loaded from: classes.dex */
public class RangeDateSelector implements DateSelector<Pair<Long, Long>> {
    public static final Parcelable.Creator<RangeDateSelector> CREATOR = new c();

    /* renamed from: e, reason: collision with root package name */
    private String f8667e;

    /* renamed from: f, reason: collision with root package name */
    private final String f8668f = " ";

    /* renamed from: g, reason: collision with root package name */
    private Long f8669g = null;

    /* renamed from: h, reason: collision with root package name */
    private Long f8670h = null;

    /* renamed from: i, reason: collision with root package name */
    private Long f8671i = null;

    /* renamed from: j, reason: collision with root package name */
    private Long f8672j = null;

    /* loaded from: classes.dex */
    class a extends DateFormatTextWatcher {

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ TextInputLayout f8673k;

        /* renamed from: l, reason: collision with root package name */
        final /* synthetic */ TextInputLayout f8674l;

        /* renamed from: m, reason: collision with root package name */
        final /* synthetic */ OnSelectionChangedListener f8675m;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints, TextInputLayout textInputLayout2, TextInputLayout textInputLayout3, OnSelectionChangedListener onSelectionChangedListener) {
            super(str, dateFormat, textInputLayout, calendarConstraints);
            this.f8673k = textInputLayout2;
            this.f8674l = textInputLayout3;
            this.f8675m = onSelectionChangedListener;
        }

        @Override // com.google.android.material.datepicker.DateFormatTextWatcher
        void e() {
            RangeDateSelector.this.f8671i = null;
            RangeDateSelector.this.s(this.f8673k, this.f8674l, this.f8675m);
        }

        @Override // com.google.android.material.datepicker.DateFormatTextWatcher
        void f(Long l10) {
            RangeDateSelector.this.f8671i = l10;
            RangeDateSelector.this.s(this.f8673k, this.f8674l, this.f8675m);
        }
    }

    /* loaded from: classes.dex */
    class b extends DateFormatTextWatcher {

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ TextInputLayout f8677k;

        /* renamed from: l, reason: collision with root package name */
        final /* synthetic */ TextInputLayout f8678l;

        /* renamed from: m, reason: collision with root package name */
        final /* synthetic */ OnSelectionChangedListener f8679m;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints, TextInputLayout textInputLayout2, TextInputLayout textInputLayout3, OnSelectionChangedListener onSelectionChangedListener) {
            super(str, dateFormat, textInputLayout, calendarConstraints);
            this.f8677k = textInputLayout2;
            this.f8678l = textInputLayout3;
            this.f8679m = onSelectionChangedListener;
        }

        @Override // com.google.android.material.datepicker.DateFormatTextWatcher
        void e() {
            RangeDateSelector.this.f8672j = null;
            RangeDateSelector.this.s(this.f8677k, this.f8678l, this.f8679m);
        }

        @Override // com.google.android.material.datepicker.DateFormatTextWatcher
        void f(Long l10) {
            RangeDateSelector.this.f8672j = l10;
            RangeDateSelector.this.s(this.f8677k, this.f8678l, this.f8679m);
        }
    }

    /* loaded from: classes.dex */
    class c implements Parcelable.Creator<RangeDateSelector> {
        c() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public RangeDateSelector createFromParcel(Parcel parcel) {
            RangeDateSelector rangeDateSelector = new RangeDateSelector();
            rangeDateSelector.f8669g = (Long) parcel.readValue(Long.class.getClassLoader());
            rangeDateSelector.f8670h = (Long) parcel.readValue(Long.class.getClassLoader());
            return rangeDateSelector;
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public RangeDateSelector[] newArray(int i10) {
            return new RangeDateSelector[i10];
        }
    }

    private void o(TextInputLayout textInputLayout, TextInputLayout textInputLayout2) {
        if (textInputLayout.getError() != null && this.f8667e.contentEquals(textInputLayout.getError())) {
            textInputLayout.setError(null);
        }
        if (textInputLayout2.getError() == null || !" ".contentEquals(textInputLayout2.getError())) {
            return;
        }
        textInputLayout2.setError(null);
    }

    private boolean q(long j10, long j11) {
        return j10 <= j11;
    }

    private void r(TextInputLayout textInputLayout, TextInputLayout textInputLayout2) {
        textInputLayout.setError(this.f8667e);
        textInputLayout2.setError(" ");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s(TextInputLayout textInputLayout, TextInputLayout textInputLayout2, OnSelectionChangedListener<Pair<Long, Long>> onSelectionChangedListener) {
        Long l10 = this.f8671i;
        if (l10 != null && this.f8672j != null) {
            if (q(l10.longValue(), this.f8672j.longValue())) {
                this.f8669g = this.f8671i;
                this.f8670h = this.f8672j;
                onSelectionChangedListener.b(h());
                return;
            } else {
                r(textInputLayout, textInputLayout2);
                onSelectionChangedListener.a();
                return;
            }
        }
        o(textInputLayout, textInputLayout2);
        onSelectionChangedListener.a();
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public String a(Context context) {
        Resources resources = context.getResources();
        Long l10 = this.f8669g;
        if (l10 == null && this.f8670h == null) {
            return resources.getString(R$string.mtrl_picker_range_header_unselected);
        }
        Long l11 = this.f8670h;
        if (l11 == null) {
            return resources.getString(R$string.mtrl_picker_range_header_only_start_selected, DateStrings.c(l10.longValue()));
        }
        if (l10 == null) {
            return resources.getString(R$string.mtrl_picker_range_header_only_end_selected, DateStrings.c(l11.longValue()));
        }
        Pair<String, String> a10 = DateStrings.a(l10, l11);
        return resources.getString(R$string.mtrl_picker_range_header_selected, a10.f2305a, a10.f2306b);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Pair<Long, Long>> b() {
        if (this.f8669g != null && this.f8670h != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new Pair(this.f8669g, this.f8670h));
            return arrayList;
        }
        return new ArrayList();
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public View c(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints calendarConstraints, OnSelectionChangedListener<Pair<Long, Long>> onSelectionChangedListener) {
        View inflate = layoutInflater.inflate(R$layout.mtrl_picker_text_input_date_range, viewGroup, false);
        TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R$id.mtrl_picker_text_input_range_start);
        TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(R$id.mtrl_picker_text_input_range_end);
        EditText editText = textInputLayout.getEditText();
        EditText editText2 = textInputLayout2.getEditText();
        if (ManufacturerUtils.isDateInputKeyboardMissingSeparatorCharacters()) {
            editText.setInputType(17);
            editText2.setInputType(17);
        }
        this.f8667e = inflate.getResources().getString(R$string.mtrl_picker_invalid_range);
        SimpleDateFormat f10 = UtcDates.f();
        Long l10 = this.f8669g;
        if (l10 != null) {
            editText.setText(f10.format(l10));
            this.f8671i = this.f8669g;
        }
        Long l11 = this.f8670h;
        if (l11 != null) {
            editText2.setText(f10.format(l11));
            this.f8672j = this.f8670h;
        }
        String g6 = UtcDates.g(inflate.getResources(), f10);
        textInputLayout.setPlaceholderText(g6);
        textInputLayout2.setPlaceholderText(g6);
        editText.addTextChangedListener(new a(g6, f10, textInputLayout, calendarConstraints, textInputLayout, textInputLayout2, onSelectionChangedListener));
        editText2.addTextChangedListener(new b(g6, f10, textInputLayout2, calendarConstraints, textInputLayout, textInputLayout2, onSelectionChangedListener));
        ViewUtils.requestFocusAndShowKeyboard(editText);
        return inflate;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public int d(Context context) {
        int i10;
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) > resources.getDimensionPixelSize(R$dimen.mtrl_calendar_maximum_default_fullscreen_minor_axis)) {
            i10 = R$attr.materialCalendarTheme;
        } else {
            i10 = R$attr.materialCalendarFullscreenTheme;
        }
        return MaterialAttributes.d(context, i10, MaterialDatePicker.class.getCanonicalName());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public boolean f() {
        Long l10 = this.f8669g;
        return (l10 == null || this.f8670h == null || !q(l10.longValue(), this.f8670h.longValue())) ? false : true;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Long> g() {
        ArrayList arrayList = new ArrayList();
        Long l10 = this.f8669g;
        if (l10 != null) {
            arrayList.add(l10);
        }
        Long l11 = this.f8670h;
        if (l11 != null) {
            arrayList.add(l11);
        }
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public void i(long j10) {
        Long l10 = this.f8669g;
        if (l10 == null) {
            this.f8669g = Long.valueOf(j10);
        } else if (this.f8670h == null && q(l10.longValue(), j10)) {
            this.f8670h = Long.valueOf(j10);
        } else {
            this.f8670h = null;
            this.f8669g = Long.valueOf(j10);
        }
    }

    @Override // com.google.android.material.datepicker.DateSelector
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public Pair<Long, Long> h() {
        return new Pair<>(this.f8669g, this.f8670h);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeValue(this.f8669g);
        parcel.writeValue(this.f8670h);
    }
}
