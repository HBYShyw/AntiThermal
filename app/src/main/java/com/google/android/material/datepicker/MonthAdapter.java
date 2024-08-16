package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.material.R$layout;
import java.util.Collection;
import java.util.Iterator;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MonthAdapter.java */
/* renamed from: com.google.android.material.datepicker.j, reason: use source file name */
/* loaded from: classes.dex */
public class MonthAdapter extends BaseAdapter {

    /* renamed from: j, reason: collision with root package name */
    static final int f8763j = UtcDates.l().getMaximum(4);

    /* renamed from: e, reason: collision with root package name */
    final Month f8764e;

    /* renamed from: f, reason: collision with root package name */
    final DateSelector<?> f8765f;

    /* renamed from: g, reason: collision with root package name */
    private Collection<Long> f8766g;

    /* renamed from: h, reason: collision with root package name */
    CalendarStyle f8767h;

    /* renamed from: i, reason: collision with root package name */
    final CalendarConstraints f8768i;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MonthAdapter(Month month, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints) {
        this.f8764e = month;
        this.f8765f = dateSelector;
        this.f8768i = calendarConstraints;
        this.f8766g = dateSelector.g();
    }

    private void e(Context context) {
        if (this.f8767h == null) {
            this.f8767h = new CalendarStyle(context);
        }
    }

    private boolean h(long j10) {
        Iterator<Long> it = this.f8765f.g().iterator();
        while (it.hasNext()) {
            if (UtcDates.a(j10) == UtcDates.a(it.next().longValue())) {
                return true;
            }
        }
        return false;
    }

    private void k(TextView textView, long j10) {
        CalendarItemStyle calendarItemStyle;
        if (textView == null) {
            return;
        }
        if (this.f8768i.o().e(j10)) {
            textView.setEnabled(true);
            if (h(j10)) {
                calendarItemStyle = this.f8767h.f8691b;
            } else if (UtcDates.j().getTimeInMillis() == j10) {
                calendarItemStyle = this.f8767h.f8692c;
            } else {
                calendarItemStyle = this.f8767h.f8690a;
            }
        } else {
            textView.setEnabled(false);
            calendarItemStyle = this.f8767h.f8696g;
        }
        calendarItemStyle.d(textView);
    }

    private void l(MaterialCalendarGridView materialCalendarGridView, long j10) {
        if (Month.m(j10).equals(this.f8764e)) {
            k((TextView) materialCalendarGridView.getChildAt(materialCalendarGridView.getAdapter().a(this.f8764e.q(j10)) - materialCalendarGridView.getFirstVisiblePosition()), j10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a(int i10) {
        return b() + (i10 - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f8764e.o();
    }

    @Override // android.widget.Adapter
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public Long getItem(int i10) {
        if (i10 < this.f8764e.o() || i10 > i()) {
            return null;
        }
        return Long.valueOf(this.f8764e.p(j(i10)));
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0083 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0084  */
    @Override // android.widget.Adapter
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public TextView getView(int i10, View view, ViewGroup viewGroup) {
        Long item;
        e(viewGroup.getContext());
        TextView textView = (TextView) view;
        if (view == null) {
            textView = (TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.mtrl_calendar_day, viewGroup, false);
        }
        int b10 = i10 - b();
        if (b10 >= 0) {
            Month month = this.f8764e;
            if (b10 < month.f8664i) {
                int i11 = b10 + 1;
                textView.setTag(month);
                textView.setText(String.format(textView.getResources().getConfiguration().locale, "%d", Integer.valueOf(i11)));
                long p10 = this.f8764e.p(i11);
                if (this.f8764e.f8662g == Month.n().f8662g) {
                    textView.setContentDescription(DateStrings.g(p10));
                } else {
                    textView.setContentDescription(DateStrings.l(p10));
                }
                textView.setVisibility(0);
                textView.setEnabled(true);
                item = getItem(i10);
                if (item != null) {
                    return textView;
                }
                k(textView, item.longValue());
                return textView;
            }
        }
        textView.setVisibility(8);
        textView.setEnabled(false);
        item = getItem(i10);
        if (item != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f(int i10) {
        return i10 % this.f8764e.f8663h == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean g(int i10) {
        return (i10 + 1) % this.f8764e.f8663h == 0;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.f8764e.f8664i + b();
    }

    @Override // android.widget.Adapter
    public long getItemId(int i10) {
        return i10 / this.f8764e.f8663h;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        return (this.f8764e.o() + this.f8764e.f8664i) - 1;
    }

    int j(int i10) {
        return (i10 - this.f8764e.o()) + 1;
    }

    public void m(MaterialCalendarGridView materialCalendarGridView) {
        Iterator<Long> it = this.f8766g.iterator();
        while (it.hasNext()) {
            l(materialCalendarGridView, it.next().longValue());
        }
        DateSelector<?> dateSelector = this.f8765f;
        if (dateSelector != null) {
            Iterator<Long> it2 = dateSelector.g().iterator();
            while (it2.hasNext()) {
                l(materialCalendarGridView, it2.next().longValue());
            }
            this.f8766g = this.f8765f.g();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean n(int i10) {
        return i10 >= b() && i10 <= i();
    }
}
