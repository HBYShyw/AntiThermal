package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.datepicker.MaterialCalendar;

/* compiled from: MonthsPagerAdapter.java */
/* renamed from: com.google.android.material.datepicker.k, reason: use source file name */
/* loaded from: classes.dex */
class MonthsPagerAdapter extends RecyclerView.h<b> {

    /* renamed from: a, reason: collision with root package name */
    private final CalendarConstraints f8769a;

    /* renamed from: b, reason: collision with root package name */
    private final DateSelector<?> f8770b;

    /* renamed from: c, reason: collision with root package name */
    private final MaterialCalendar.l f8771c;

    /* renamed from: d, reason: collision with root package name */
    private final int f8772d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: MonthsPagerAdapter.java */
    /* renamed from: com.google.android.material.datepicker.k$a */
    /* loaded from: classes.dex */
    public class a implements AdapterView.OnItemClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ MaterialCalendarGridView f8773e;

        a(MaterialCalendarGridView materialCalendarGridView) {
            this.f8773e = materialCalendarGridView;
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i10, long j10) {
            if (this.f8773e.getAdapter().n(i10)) {
                MonthsPagerAdapter.this.f8771c.a(this.f8773e.getAdapter().getItem(i10).longValue());
            }
        }
    }

    /* compiled from: MonthsPagerAdapter.java */
    /* renamed from: com.google.android.material.datepicker.k$b */
    /* loaded from: classes.dex */
    public static class b extends RecyclerView.c0 {

        /* renamed from: a, reason: collision with root package name */
        final TextView f8775a;

        /* renamed from: b, reason: collision with root package name */
        final MaterialCalendarGridView f8776b;

        b(LinearLayout linearLayout, boolean z10) {
            super(linearLayout);
            TextView textView = (TextView) linearLayout.findViewById(R$id.month_title);
            this.f8775a = textView;
            ViewCompat.m0(textView, true);
            this.f8776b = (MaterialCalendarGridView) linearLayout.findViewById(R$id.month_grid);
            if (z10) {
                return;
            }
            textView.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MonthsPagerAdapter(Context context, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints, MaterialCalendar.l lVar) {
        Month s7 = calendarConstraints.s();
        Month p10 = calendarConstraints.p();
        Month r10 = calendarConstraints.r();
        if (s7.compareTo(r10) <= 0) {
            if (r10.compareTo(p10) <= 0) {
                this.f8772d = (MonthAdapter.f8763j * MaterialCalendar.s0(context)) + (MaterialDatePicker.H0(context) ? MaterialCalendar.s0(context) : 0);
                this.f8769a = calendarConstraints;
                this.f8770b = dateSelector;
                this.f8771c = lVar;
                setHasStableIds(true);
                return;
            }
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
        }
        throw new IllegalArgumentException("firstPage cannot be after currentPage");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month d(int i10) {
        return this.f8769a.s().t(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence e(int i10) {
        return d(i10).r();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int f(Month month) {
        return this.f8769a.s().u(month);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(b bVar, int i10) {
        Month t7 = this.f8769a.s().t(i10);
        bVar.f8775a.setText(t7.r());
        MaterialCalendarGridView materialCalendarGridView = (MaterialCalendarGridView) bVar.f8776b.findViewById(R$id.month_grid);
        if (materialCalendarGridView.getAdapter() != null && t7.equals(materialCalendarGridView.getAdapter().f8764e)) {
            materialCalendarGridView.invalidate();
            materialCalendarGridView.getAdapter().m(materialCalendarGridView);
        } else {
            MonthAdapter monthAdapter = new MonthAdapter(t7, this.f8770b, this.f8769a);
            materialCalendarGridView.setNumColumns(t7.f8663h);
            materialCalendarGridView.setAdapter((ListAdapter) monthAdapter);
        }
        materialCalendarGridView.setOnItemClickListener(new a(materialCalendarGridView));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public int getItemCount() {
        return this.f8769a.q();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public long getItemId(int i10) {
        return this.f8769a.s().t(i10).s();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public b onCreateViewHolder(ViewGroup viewGroup, int i10) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.mtrl_calendar_month_labeled, viewGroup, false);
        if (MaterialDatePicker.H0(viewGroup.getContext())) {
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, this.f8772d));
            return new b(linearLayout, true);
        }
        return new b(linearLayout, false);
    }
}
