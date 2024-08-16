package com.google.android.material.datepicker;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.ObjectsCompat;
import com.coui.appcompat.calendar.COUIDateMonthView;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class CalendarConstraints implements Parcelable {
    public static final Parcelable.Creator<CalendarConstraints> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final Month f8639e;

    /* renamed from: f, reason: collision with root package name */
    private final Month f8640f;

    /* renamed from: g, reason: collision with root package name */
    private final DateValidator f8641g;

    /* renamed from: h, reason: collision with root package name */
    private Month f8642h;

    /* renamed from: i, reason: collision with root package name */
    private final int f8643i;

    /* renamed from: j, reason: collision with root package name */
    private final int f8644j;

    /* loaded from: classes.dex */
    public interface DateValidator extends Parcelable {
        boolean e(long j10);
    }

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<CalendarConstraints> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public CalendarConstraints createFromParcel(Parcel parcel) {
            return new CalendarConstraints((Month) parcel.readParcelable(Month.class.getClassLoader()), (Month) parcel.readParcelable(Month.class.getClassLoader()), (DateValidator) parcel.readParcelable(DateValidator.class.getClassLoader()), (Month) parcel.readParcelable(Month.class.getClassLoader()), null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public CalendarConstraints[] newArray(int i10) {
            return new CalendarConstraints[i10];
        }
    }

    /* loaded from: classes.dex */
    public static final class b {

        /* renamed from: e, reason: collision with root package name */
        static final long f8645e = UtcDates.a(Month.l(COUIDateMonthView.MIN_YEAR, 0).f8665j);

        /* renamed from: f, reason: collision with root package name */
        static final long f8646f = UtcDates.a(Month.l(COUIDateMonthView.MAX_YEAR, 11).f8665j);

        /* renamed from: a, reason: collision with root package name */
        private long f8647a;

        /* renamed from: b, reason: collision with root package name */
        private long f8648b;

        /* renamed from: c, reason: collision with root package name */
        private Long f8649c;

        /* renamed from: d, reason: collision with root package name */
        private DateValidator f8650d;

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(CalendarConstraints calendarConstraints) {
            this.f8647a = f8645e;
            this.f8648b = f8646f;
            this.f8650d = DateValidatorPointForward.j(Long.MIN_VALUE);
            this.f8647a = calendarConstraints.f8639e.f8665j;
            this.f8648b = calendarConstraints.f8640f.f8665j;
            this.f8649c = Long.valueOf(calendarConstraints.f8642h.f8665j);
            this.f8650d = calendarConstraints.f8641g;
        }

        public CalendarConstraints a() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("DEEP_COPY_VALIDATOR_KEY", this.f8650d);
            Month m10 = Month.m(this.f8647a);
            Month m11 = Month.m(this.f8648b);
            DateValidator dateValidator = (DateValidator) bundle.getParcelable("DEEP_COPY_VALIDATOR_KEY");
            Long l10 = this.f8649c;
            return new CalendarConstraints(m10, m11, dateValidator, l10 == null ? null : Month.m(l10.longValue()), null);
        }

        public b b(long j10) {
            this.f8649c = Long.valueOf(j10);
            return this;
        }
    }

    /* synthetic */ CalendarConstraints(Month month, Month month2, DateValidator dateValidator, Month month3, a aVar) {
        this(month, month2, dateValidator, month3);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CalendarConstraints)) {
            return false;
        }
        CalendarConstraints calendarConstraints = (CalendarConstraints) obj;
        return this.f8639e.equals(calendarConstraints.f8639e) && this.f8640f.equals(calendarConstraints.f8640f) && ObjectsCompat.a(this.f8642h, calendarConstraints.f8642h) && this.f8641g.equals(calendarConstraints.f8641g);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f8639e, this.f8640f, this.f8642h, this.f8641g});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month n(Month month) {
        if (month.compareTo(this.f8639e) < 0) {
            return this.f8639e;
        }
        return month.compareTo(this.f8640f) > 0 ? this.f8640f : month;
    }

    public DateValidator o() {
        return this.f8641g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month p() {
        return this.f8640f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int q() {
        return this.f8644j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month r() {
        return this.f8642h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month s() {
        return this.f8639e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int t() {
        return this.f8643i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean u(long j10) {
        if (this.f8639e.p(1) <= j10) {
            Month month = this.f8640f;
            if (j10 <= month.p(month.f8664i)) {
                return true;
            }
        }
        return false;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeParcelable(this.f8639e, 0);
        parcel.writeParcelable(this.f8640f, 0);
        parcel.writeParcelable(this.f8642h, 0);
        parcel.writeParcelable(this.f8641g, 0);
    }

    private CalendarConstraints(Month month, Month month2, DateValidator dateValidator, Month month3) {
        this.f8639e = month;
        this.f8640f = month2;
        this.f8642h = month3;
        this.f8641g = dateValidator;
        if (month3 != null && month.compareTo(month3) > 0) {
            throw new IllegalArgumentException("start Month cannot be after current Month");
        }
        if (month3 != null && month3.compareTo(month2) > 0) {
            throw new IllegalArgumentException("current Month cannot be after end Month");
        }
        this.f8644j = month.u(month2) + 1;
        this.f8643i = (month2.f8662g - month.f8662g) + 1;
    }
}
