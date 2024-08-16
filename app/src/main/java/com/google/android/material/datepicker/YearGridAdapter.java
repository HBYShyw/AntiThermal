package com.google.android.material.datepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.R$layout;
import com.google.android.material.R$string;
import com.google.android.material.datepicker.MaterialCalendar;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: YearGridAdapter.java */
/* renamed from: com.google.android.material.datepicker.q, reason: use source file name */
/* loaded from: classes.dex */
public class YearGridAdapter extends RecyclerView.h<b> {

    /* renamed from: a, reason: collision with root package name */
    private final MaterialCalendar<?> f8783a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: YearGridAdapter.java */
    /* renamed from: com.google.android.material.datepicker.q$a */
    /* loaded from: classes.dex */
    public class a implements View.OnClickListener {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ int f8784e;

        a(int i10) {
            this.f8784e = i10;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            YearGridAdapter.this.f8783a.x0(YearGridAdapter.this.f8783a.o0().n(Month.l(this.f8784e, YearGridAdapter.this.f8783a.q0().f8661f)));
            YearGridAdapter.this.f8783a.y0(MaterialCalendar.k.DAY);
        }
    }

    /* compiled from: YearGridAdapter.java */
    /* renamed from: com.google.android.material.datepicker.q$b */
    /* loaded from: classes.dex */
    public static class b extends RecyclerView.c0 {

        /* renamed from: a, reason: collision with root package name */
        final TextView f8786a;

        b(TextView textView) {
            super(textView);
            this.f8786a = textView;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public YearGridAdapter(MaterialCalendar<?> materialCalendar) {
        this.f8783a = materialCalendar;
    }

    private View.OnClickListener d(int i10) {
        return new a(i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int e(int i10) {
        return i10 - this.f8783a.o0().s().f8662g;
    }

    int f(int i10) {
        return this.f8783a.o0().s().f8662g + i10;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: g, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(b bVar, int i10) {
        int f10 = f(i10);
        String string = bVar.f8786a.getContext().getString(R$string.mtrl_picker_navigate_to_year_description);
        bVar.f8786a.setText(String.format(Locale.getDefault(), "%d", Integer.valueOf(f10)));
        bVar.f8786a.setContentDescription(String.format(string, Integer.valueOf(f10)));
        CalendarStyle p02 = this.f8783a.p0();
        Calendar j10 = UtcDates.j();
        CalendarItemStyle calendarItemStyle = j10.get(1) == f10 ? p02.f8695f : p02.f8693d;
        Iterator<Long> it = this.f8783a.r0().g().iterator();
        while (it.hasNext()) {
            j10.setTimeInMillis(it.next().longValue());
            if (j10.get(1) == f10) {
                calendarItemStyle = p02.f8694e;
            }
        }
        calendarItemStyle.d(bVar.f8786a);
        bVar.f8786a.setOnClickListener(d(f10));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public int getItemCount() {
        return this.f8783a.o0().t();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public b onCreateViewHolder(ViewGroup viewGroup, int i10) {
        return new b((TextView) LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.mtrl_calendar_year, viewGroup, false));
    }
}
