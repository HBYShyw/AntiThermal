package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.core.util.Pair;
import com.google.android.material.R$attr;
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
public class SingleDateSelector implements DateSelector<Long> {
    public static final Parcelable.Creator<SingleDateSelector> CREATOR = new b();

    /* renamed from: e, reason: collision with root package name */
    private Long f8681e;

    /* loaded from: classes.dex */
    class a extends DateFormatTextWatcher {

        /* renamed from: k, reason: collision with root package name */
        final /* synthetic */ OnSelectionChangedListener f8682k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints, OnSelectionChangedListener onSelectionChangedListener) {
            super(str, dateFormat, textInputLayout, calendarConstraints);
            this.f8682k = onSelectionChangedListener;
        }

        @Override // com.google.android.material.datepicker.DateFormatTextWatcher
        void e() {
            this.f8682k.a();
        }

        @Override // com.google.android.material.datepicker.DateFormatTextWatcher
        void f(Long l10) {
            if (l10 == null) {
                SingleDateSelector.this.l();
            } else {
                SingleDateSelector.this.i(l10.longValue());
            }
            this.f8682k.b(SingleDateSelector.this.h());
        }
    }

    /* loaded from: classes.dex */
    class b implements Parcelable.Creator<SingleDateSelector> {
        b() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public SingleDateSelector createFromParcel(Parcel parcel) {
            SingleDateSelector singleDateSelector = new SingleDateSelector();
            singleDateSelector.f8681e = (Long) parcel.readValue(Long.class.getClassLoader());
            return singleDateSelector;
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public SingleDateSelector[] newArray(int i10) {
            return new SingleDateSelector[i10];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        this.f8681e = null;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public String a(Context context) {
        Resources resources = context.getResources();
        Long l10 = this.f8681e;
        if (l10 == null) {
            return resources.getString(R$string.mtrl_picker_date_header_unselected);
        }
        return resources.getString(R$string.mtrl_picker_date_header_selected, DateStrings.j(l10.longValue()));
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Pair<Long, Long>> b() {
        return new ArrayList();
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public View c(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints calendarConstraints, OnSelectionChangedListener<Long> onSelectionChangedListener) {
        View inflate = layoutInflater.inflate(R$layout.mtrl_picker_text_input_date, viewGroup, false);
        TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R$id.mtrl_picker_text_input_date);
        EditText editText = textInputLayout.getEditText();
        if (ManufacturerUtils.isDateInputKeyboardMissingSeparatorCharacters()) {
            editText.setInputType(17);
        }
        SimpleDateFormat f10 = UtcDates.f();
        String g6 = UtcDates.g(inflate.getResources(), f10);
        textInputLayout.setPlaceholderText(g6);
        Long l10 = this.f8681e;
        if (l10 != null) {
            editText.setText(f10.format(l10));
        }
        editText.addTextChangedListener(new a(g6, f10, textInputLayout, calendarConstraints, onSelectionChangedListener));
        ViewUtils.requestFocusAndShowKeyboard(editText);
        return inflate;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public int d(Context context) {
        return MaterialAttributes.d(context, R$attr.materialCalendarTheme, MaterialDatePicker.class.getCanonicalName());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public boolean f() {
        return this.f8681e != null;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Long> g() {
        ArrayList arrayList = new ArrayList();
        Long l10 = this.f8681e;
        if (l10 != null) {
            arrayList.add(l10);
        }
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public void i(long j10) {
        this.f8681e = Long.valueOf(j10);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    /* renamed from: m, reason: merged with bridge method [inline-methods] */
    public Long h() {
        return this.f8681e;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeValue(this.f8681e);
    }
}
