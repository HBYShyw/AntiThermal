package com.oplus.pantanal.oassist.base;

import android.os.Parcel;
import android.os.Parcelable;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: OAssistData.kt */
/* loaded from: classes.dex */
public final class OAssistOutput implements Parcelable {

    /* renamed from: e, reason: collision with root package name */
    private final OAssistInput f9965e;

    /* renamed from: f, reason: collision with root package name */
    private final int f9966f;

    /* renamed from: g, reason: collision with root package name */
    private final OAssistOutputBody f9967g;

    /* renamed from: h, reason: collision with root package name */
    public static final a f9964h = new a(null);
    public static final Parcelable.Creator<OAssistOutput> CREATOR = new b();

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class b implements Parcelable.Creator<OAssistOutput> {
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final OAssistOutput createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new OAssistOutput(OAssistInput.CREATOR.createFromParcel(parcel), parcel.readInt(), OAssistOutputBody.CREATOR.createFromParcel(parcel));
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final OAssistOutput[] newArray(int i10) {
            return new OAssistOutput[i10];
        }
    }

    public OAssistOutput(OAssistInput oAssistInput, int i10, OAssistOutputBody oAssistOutputBody) {
        k.e(oAssistInput, "input");
        k.e(oAssistOutputBody, "body");
        this.f9965e = oAssistInput;
        this.f9966f = i10;
        this.f9967g = oAssistOutputBody;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OAssistOutput)) {
            return false;
        }
        OAssistOutput oAssistOutput = (OAssistOutput) obj;
        return k.a(this.f9965e, oAssistOutput.f9965e) && this.f9966f == oAssistOutput.f9966f && k.a(this.f9967g, oAssistOutput.f9967g);
    }

    public int hashCode() {
        return (((this.f9965e.hashCode() * 31) + Integer.hashCode(this.f9966f)) * 31) + this.f9967g.hashCode();
    }

    public String toString() {
        return "OAssistOutput(input=" + this.f9965e + ", errCode=" + this.f9966f + ", body=" + this.f9967g + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "out");
        this.f9965e.writeToParcel(parcel, i10);
        parcel.writeInt(this.f9966f);
        this.f9967g.writeToParcel(parcel, i10);
    }
}
