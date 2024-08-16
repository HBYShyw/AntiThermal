package com.oplus.pantanal.oassist.base;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.pantanal.oassist.base.OAssistOutputBodyContent;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: OAssistData.kt */
/* loaded from: classes.dex */
public final class OAssistOutputBody implements Parcelable {

    /* renamed from: e, reason: collision with root package name */
    private final OAssistOutputBodyContent f9969e;

    /* renamed from: f, reason: collision with root package name */
    private final int f9970f;

    /* renamed from: g, reason: collision with root package name */
    public static final a f9968g = new a(null);
    public static final Parcelable.Creator<OAssistOutputBody> CREATOR = new b();

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final OAssistOutputBody a() {
            return new OAssistOutputBody(new OAssistOutputBodyContent(new OAssistOutputBodyContent.HumanResponse(null, null, null, 7, null), null, 2, null), -1);
        }
    }

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class b implements Parcelable.Creator<OAssistOutputBody> {
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final OAssistOutputBody createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new OAssistOutputBody(OAssistOutputBodyContent.CREATOR.createFromParcel(parcel), parcel.readInt());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final OAssistOutputBody[] newArray(int i10) {
            return new OAssistOutputBody[i10];
        }
    }

    public OAssistOutputBody(OAssistOutputBodyContent oAssistOutputBodyContent, int i10) {
        k.e(oAssistOutputBodyContent, "content");
        this.f9969e = oAssistOutputBodyContent;
        this.f9970f = i10;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OAssistOutputBody)) {
            return false;
        }
        OAssistOutputBody oAssistOutputBody = (OAssistOutputBody) obj;
        return k.a(this.f9969e, oAssistOutputBody.f9969e) && this.f9970f == oAssistOutputBody.f9970f;
    }

    public int hashCode() {
        return (this.f9969e.hashCode() * 31) + Integer.hashCode(this.f9970f);
    }

    public String toString() {
        return "OAssistOutputBody(content=" + this.f9969e + ", code=" + this.f9970f + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "out");
        this.f9969e.writeToParcel(parcel, i10);
        parcel.writeInt(this.f9970f);
    }
}
