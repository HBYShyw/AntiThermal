package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Month implements Comparable<Month>, Parcelable {
    public static final Parcelable.Creator<Month> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final Calendar f8660e;

    /* renamed from: f, reason: collision with root package name */
    final int f8661f;

    /* renamed from: g, reason: collision with root package name */
    final int f8662g;

    /* renamed from: h, reason: collision with root package name */
    final int f8663h;

    /* renamed from: i, reason: collision with root package name */
    final int f8664i;

    /* renamed from: j, reason: collision with root package name */
    final long f8665j;

    /* renamed from: k, reason: collision with root package name */
    private String f8666k;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<Month> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Month createFromParcel(Parcel parcel) {
            return Month.l(parcel.readInt(), parcel.readInt());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public Month[] newArray(int i10) {
            return new Month[i10];
        }
    }

    private Month(Calendar calendar) {
        calendar.set(5, 1);
        Calendar e10 = UtcDates.e(calendar);
        this.f8660e = e10;
        this.f8661f = e10.get(2);
        this.f8662g = e10.get(1);
        this.f8663h = e10.getMaximum(7);
        this.f8664i = e10.getActualMaximum(5);
        this.f8665j = e10.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Month l(int i10, int i11) {
        Calendar l10 = UtcDates.l();
        l10.set(1, i10);
        l10.set(2, i11);
        return new Month(l10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Month m(long j10) {
        Calendar l10 = UtcDates.l();
        l10.setTimeInMillis(j10);
        return new Month(l10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Month n() {
        return new Month(UtcDates.j());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Month)) {
            return false;
        }
        Month month = (Month) obj;
        return this.f8661f == month.f8661f && this.f8662g == month.f8662g;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.f8661f), Integer.valueOf(this.f8662g)});
    }

    @Override // java.lang.Comparable
    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public int compareTo(Month month) {
        return this.f8660e.compareTo(month.f8660e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o() {
        int firstDayOfWeek = this.f8660e.get(7) - this.f8660e.getFirstDayOfWeek();
        return firstDayOfWeek < 0 ? firstDayOfWeek + this.f8663h : firstDayOfWeek;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long p(int i10) {
        Calendar e10 = UtcDates.e(this.f8660e);
        e10.set(5, i10);
        return e10.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int q(long j10) {
        Calendar e10 = UtcDates.e(this.f8660e);
        e10.setTimeInMillis(j10);
        return e10.get(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String r() {
        if (this.f8666k == null) {
            this.f8666k = DateStrings.i(this.f8660e.getTimeInMillis());
        }
        return this.f8666k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long s() {
        return this.f8660e.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month t(int i10) {
        Calendar e10 = UtcDates.e(this.f8660e);
        e10.add(2, i10);
        return new Month(e10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int u(Month month) {
        if (this.f8660e instanceof GregorianCalendar) {
            return ((month.f8662g - this.f8662g) * 12) + (month.f8661f - this.f8661f);
        }
        throw new IllegalArgumentException("Only Gregorian calendars are supported.");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.f8662g);
        parcel.writeInt(this.f8661f);
    }
}
