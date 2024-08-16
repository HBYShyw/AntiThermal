package com.oplus.ovoicemanager.service;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class ActionRequest implements Parcelable {
    public static final Parcelable.Creator<ActionRequest> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private String f9946e;

    /* renamed from: f, reason: collision with root package name */
    private String f9947f;

    /* renamed from: g, reason: collision with root package name */
    private String f9948g;

    /* renamed from: h, reason: collision with root package name */
    private String f9949h;

    /* renamed from: i, reason: collision with root package name */
    private String f9950i;

    /* loaded from: classes.dex */
    class a implements Parcelable.Creator<ActionRequest> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public ActionRequest createFromParcel(Parcel parcel) {
            return new ActionRequest(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public ActionRequest[] newArray(int i10) {
            return new ActionRequest[i10];
        }
    }

    public ActionRequest() {
    }

    private void n(Parcel parcel) {
        this.f9946e = parcel.readString();
        this.f9947f = parcel.readString();
        this.f9948g = parcel.readString();
        this.f9949h = parcel.readString();
        this.f9950i = parcel.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String j() {
        return this.f9950i;
    }

    public String k() {
        return this.f9949h;
    }

    public String l() {
        return this.f9946e;
    }

    public String m() {
        return this.f9948g;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        parcel.writeString(this.f9946e);
        parcel.writeString(this.f9947f);
        parcel.writeString(this.f9948g);
        parcel.writeString(this.f9949h);
        parcel.writeString(this.f9950i);
    }

    protected ActionRequest(Parcel parcel) {
        n(parcel);
    }
}
