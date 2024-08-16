package com.google.android.material.datepicker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R$layout;
import com.google.android.material.R$string;
import java.util.Calendar;
import java.util.Locale;

/* compiled from: DaysOfWeekAdapter.java */
/* renamed from: com.google.android.material.datepicker.e, reason: use source file name */
/* loaded from: classes.dex */
class DaysOfWeekAdapter extends BaseAdapter {

    /* renamed from: h, reason: collision with root package name */
    private static final int f8708h = 4;

    /* renamed from: e, reason: collision with root package name */
    private final Calendar f8709e;

    /* renamed from: f, reason: collision with root package name */
    private final int f8710f;

    /* renamed from: g, reason: collision with root package name */
    private final int f8711g;

    public DaysOfWeekAdapter() {
        Calendar l10 = UtcDates.l();
        this.f8709e = l10;
        this.f8710f = l10.getMaximum(7);
        this.f8711g = l10.getFirstDayOfWeek();
    }

    private int b(int i10) {
        int i11 = i10 + this.f8711g;
        int i12 = this.f8710f;
        return i11 > i12 ? i11 - i12 : i11;
    }

    @Override // android.widget.Adapter
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public Integer getItem(int i10) {
        if (i10 >= this.f8710f) {
            return null;
        }
        return Integer.valueOf(b(i10));
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.f8710f;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i10) {
        return 0L;
    }

    @Override // android.widget.Adapter
    @SuppressLint({"WrongConstant"})
    public View getView(int i10, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) view;
        if (view == null) {
            textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.mtrl_calendar_day_of_week, viewGroup, false);
        }
        this.f8709e.set(7, b(i10));
        textView.setText(this.f8709e.getDisplayName(7, f8708h, textView.getResources().getConfiguration().locale));
        textView.setContentDescription(String.format(viewGroup.getContext().getString(R$string.mtrl_picker_day_of_week_column_header), this.f8709e.getDisplayName(7, 2, Locale.getDefault())));
        return textView;
    }
}
