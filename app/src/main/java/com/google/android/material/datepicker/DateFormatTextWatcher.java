package com.google.android.material.datepicker;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.google.android.material.R$string;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/* compiled from: DateFormatTextWatcher.java */
/* renamed from: com.google.android.material.datepicker.c, reason: use source file name */
/* loaded from: classes.dex */
abstract class DateFormatTextWatcher extends TextWatcherAdapter {

    /* renamed from: e, reason: collision with root package name */
    private final TextInputLayout f8698e;

    /* renamed from: f, reason: collision with root package name */
    private final DateFormat f8699f;

    /* renamed from: g, reason: collision with root package name */
    private final CalendarConstraints f8700g;

    /* renamed from: h, reason: collision with root package name */
    private final String f8701h;

    /* renamed from: i, reason: collision with root package name */
    private final Runnable f8702i;

    /* renamed from: j, reason: collision with root package name */
    private Runnable f8703j;

    /* compiled from: DateFormatTextWatcher.java */
    /* renamed from: com.google.android.material.datepicker.c$a */
    /* loaded from: classes.dex */
    class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ String f8704e;

        a(String str) {
            this.f8704e = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            TextInputLayout textInputLayout = DateFormatTextWatcher.this.f8698e;
            DateFormat dateFormat = DateFormatTextWatcher.this.f8699f;
            Context context = textInputLayout.getContext();
            textInputLayout.setError(context.getString(R$string.mtrl_picker_invalid_format) + "\n" + String.format(context.getString(R$string.mtrl_picker_invalid_format_use), this.f8704e) + "\n" + String.format(context.getString(R$string.mtrl_picker_invalid_format_example), dateFormat.format(new Date(UtcDates.j().getTimeInMillis()))));
            DateFormatTextWatcher.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DateFormatTextWatcher.java */
    /* renamed from: com.google.android.material.datepicker.c$b */
    /* loaded from: classes.dex */
    public class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ long f8706e;

        b(long j10) {
            this.f8706e = j10;
        }

        @Override // java.lang.Runnable
        public void run() {
            DateFormatTextWatcher.this.f8698e.setError(String.format(DateFormatTextWatcher.this.f8701h, DateStrings.c(this.f8706e)));
            DateFormatTextWatcher.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DateFormatTextWatcher(String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints) {
        this.f8699f = dateFormat;
        this.f8698e = textInputLayout;
        this.f8700g = calendarConstraints;
        this.f8701h = textInputLayout.getContext().getString(R$string.mtrl_picker_out_of_range);
        this.f8702i = new a(str);
    }

    private Runnable d(long j10) {
        return new b(j10);
    }

    abstract void e();

    abstract void f(Long l10);

    public void g(View view, Runnable runnable) {
        view.postDelayed(runnable, 1000L);
    }

    @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i10, int i11, int i12) {
        this.f8698e.removeCallbacks(this.f8702i);
        this.f8698e.removeCallbacks(this.f8703j);
        this.f8698e.setError(null);
        f(null);
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        try {
            Date parse = this.f8699f.parse(charSequence.toString());
            this.f8698e.setError(null);
            long time = parse.getTime();
            if (this.f8700g.o().e(time) && this.f8700g.u(time)) {
                f(Long.valueOf(parse.getTime()));
                return;
            }
            Runnable d10 = d(time);
            this.f8703j = d10;
            g(this.f8698e, d10);
        } catch (ParseException unused) {
            g(this.f8698e, this.f8702i);
        }
    }
}
