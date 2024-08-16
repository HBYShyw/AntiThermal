package com.oplus.pantanal.oassist.base;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: OAssistData.kt */
/* loaded from: classes.dex */
public final class OAssistOutputBodyContent implements Parcelable {
    public static final Parcelable.Creator<OAssistOutputBodyContent> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final HumanResponse f9971e;

    /* renamed from: f, reason: collision with root package name */
    private final String f9972f;

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class a implements Parcelable.Creator<OAssistOutputBodyContent> {
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final OAssistOutputBodyContent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new OAssistOutputBodyContent(HumanResponse.CREATOR.createFromParcel(parcel), parcel.readString());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final OAssistOutputBodyContent[] newArray(int i10) {
            return new OAssistOutputBodyContent[i10];
        }
    }

    public OAssistOutputBodyContent(HumanResponse humanResponse, String str) {
        k.e(humanResponse, "humanResponse");
        this.f9971e = humanResponse;
        this.f9972f = str;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OAssistOutputBodyContent)) {
            return false;
        }
        OAssistOutputBodyContent oAssistOutputBodyContent = (OAssistOutputBodyContent) obj;
        return k.a(this.f9971e, oAssistOutputBodyContent.f9971e) && k.a(this.f9972f, oAssistOutputBodyContent.f9972f);
    }

    public int hashCode() {
        int hashCode = this.f9971e.hashCode() * 31;
        String str = this.f9972f;
        return hashCode + (str == null ? 0 : str.hashCode());
    }

    public String toString() {
        return "OAssistOutputBodyContent(humanResponse=" + this.f9971e + ", functionResponse=" + this.f9972f + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "out");
        this.f9971e.writeToParcel(parcel, i10);
        parcel.writeString(this.f9972f);
    }

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class HumanResponse implements Parcelable {
        public static final Parcelable.Creator<HumanResponse> CREATOR = new a();

        /* renamed from: e, reason: collision with root package name */
        private final String f9973e;

        /* renamed from: f, reason: collision with root package name */
        private final String f9974f;

        /* renamed from: g, reason: collision with root package name */
        private final Uri f9975g;

        /* compiled from: OAssistData.kt */
        /* loaded from: classes.dex */
        public static final class a implements Parcelable.Creator<HumanResponse> {
            @Override // android.os.Parcelable.Creator
            /* renamed from: a, reason: merged with bridge method [inline-methods] */
            public final HumanResponse createFromParcel(Parcel parcel) {
                k.e(parcel, "parcel");
                return new HumanResponse(parcel.readString(), parcel.readString(), (Uri) parcel.readParcelable(HumanResponse.class.getClassLoader()));
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b, reason: merged with bridge method [inline-methods] */
            public final HumanResponse[] newArray(int i10) {
                return new HumanResponse[i10];
            }
        }

        public HumanResponse() {
            this(null, null, null, 7, null);
        }

        public HumanResponse(String str, String str2, Uri uri) {
            k.e(str, "text");
            k.e(str2, "tts");
            k.e(uri, "iconUri");
            this.f9973e = str;
            this.f9974f = str2;
            this.f9975g = uri;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof HumanResponse)) {
                return false;
            }
            HumanResponse humanResponse = (HumanResponse) obj;
            return k.a(this.f9973e, humanResponse.f9973e) && k.a(this.f9974f, humanResponse.f9974f) && k.a(this.f9975g, humanResponse.f9975g);
        }

        public int hashCode() {
            return (((this.f9973e.hashCode() * 31) + this.f9974f.hashCode()) * 31) + this.f9975g.hashCode();
        }

        public String toString() {
            return "HumanResponse(text=" + this.f9973e + ", tts=" + this.f9974f + ", iconUri=" + this.f9975g + ')';
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i10) {
            k.e(parcel, "out");
            parcel.writeString(this.f9973e);
            parcel.writeString(this.f9974f);
            parcel.writeParcelable(this.f9975g, i10);
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ HumanResponse(String str, String str2, Uri uri, int i10, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, str2, uri);
            str = (i10 & 1) != 0 ? "" : str;
            str2 = (i10 & 2) != 0 ? "" : str2;
            if ((i10 & 4) != 0) {
                uri = Uri.EMPTY;
                k.d(uri, "EMPTY");
            }
        }
    }

    public /* synthetic */ OAssistOutputBodyContent(HumanResponse humanResponse, String str, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(humanResponse, (i10 & 2) != 0 ? null : str);
    }
}
