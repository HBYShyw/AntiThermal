package com.google.android.material.timepicker;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TimeModel implements Parcelable {
    public static final Parcelable.Creator<TimeModel> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final MaxInputValidator f9518e;

    /* renamed from: f, reason: collision with root package name */
    private final MaxInputValidator f9519f;

    /* renamed from: g, reason: collision with root package name */
    final int f9520g;

    /* renamed from: h, reason: collision with root package name */
    int f9521h;

    /* renamed from: i, reason: collision with root package name */
    int f9522i;

    /* renamed from: j, reason: collision with root package name */
    int f9523j;

    /* renamed from: k, reason: collision with root package name */
    int f9524k;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<TimeModel> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public TimeModel createFromParcel(Parcel parcel) {
            return new TimeModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public TimeModel[] newArray(int i10) {
            return new TimeModel[i10];
        }
    }

    public TimeModel() {
        this(0);
    }

    public static String j(Resources resources, CharSequence charSequence) {
        return k(resources, charSequence, "%02d");
    }

    public static String k(Resources resources, CharSequence charSequence, String str) {
        return String.format(resources.getConfiguration().locale, str, Integer.valueOf(Integer.parseInt(String.valueOf(charSequence))));
    }

    private static int l(int i10) {
        return i10 >= 12 ? 1 : 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TimeModel)) {
            return false;
        }
        TimeModel timeModel = (TimeModel) obj;
        return this.f9521h == timeModel.f9521h && this.f9522i == timeModel.f9522i && this.f9520g == timeModel.f9520g && this.f9523j == timeModel.f9523j;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.f9520g), Integer.valueOf(this.f9521h), Integer.valueOf(this.f9522i), Integer.valueOf(this.f9523j)});
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeInt(this.f9521h);
        parcel.writeInt(this.f9522i);
        parcel.writeInt(this.f9523j);
        parcel.writeInt(this.f9520g);
    }

    public TimeModel(int i10) {
        this(0, 0, 10, i10);
    }

    public TimeModel(int i10, int i11, int i12, int i13) {
        this.f9521h = i10;
        this.f9522i = i11;
        this.f9523j = i12;
        this.f9520g = i13;
        this.f9524k = l(i10);
        this.f9518e = new MaxInputValidator(59);
        this.f9519f = new MaxInputValidator(i13 == 1 ? 24 : 12);
    }

    protected TimeModel(Parcel parcel) {
        this(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
    }
}
