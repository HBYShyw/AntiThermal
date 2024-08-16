package com.oplus.pantanal.oassist.base;

import android.os.Parcel;
import android.os.Parcelable;
import za.k;

/* compiled from: OAssistData.kt */
/* loaded from: classes.dex */
public final class OAssistInputParam implements Parcelable {
    public static final Parcelable.Creator<OAssistInputParam> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final String f9961e;

    /* renamed from: f, reason: collision with root package name */
    private final String f9962f;

    /* renamed from: g, reason: collision with root package name */
    private final String f9963g;

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class a implements Parcelable.Creator<OAssistInputParam> {
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final OAssistInputParam createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new OAssistInputParam(parcel.readString(), parcel.readString(), parcel.readString());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final OAssistInputParam[] newArray(int i10) {
            return new OAssistInputParam[i10];
        }
    }

    public OAssistInputParam(String str, String str2, String str3) {
        k.e(str, "paramValue");
        k.e(str2, "parameterName");
        k.e(str3, "parameterType");
        this.f9961e = str;
        this.f9962f = str2;
        this.f9963g = str3;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OAssistInputParam)) {
            return false;
        }
        OAssistInputParam oAssistInputParam = (OAssistInputParam) obj;
        return k.a(this.f9961e, oAssistInputParam.f9961e) && k.a(this.f9962f, oAssistInputParam.f9962f) && k.a(this.f9963g, oAssistInputParam.f9963g);
    }

    public int hashCode() {
        return (((this.f9961e.hashCode() * 31) + this.f9962f.hashCode()) * 31) + this.f9963g.hashCode();
    }

    public String toString() {
        return "OAssistInputParam(paramValue=" + this.f9961e + ", parameterName=" + this.f9962f + ", parameterType=" + this.f9963g + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "out");
        parcel.writeString(this.f9961e);
        parcel.writeString(this.f9962f);
        parcel.writeString(this.f9963g);
    }
}
