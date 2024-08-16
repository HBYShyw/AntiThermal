package com.oplus.pantanal.oassist.base;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import za.k;

/* compiled from: OAssistData.kt */
/* loaded from: classes.dex */
public final class OAssistInput implements Parcelable {
    public static final Parcelable.Creator<OAssistInput> CREATOR = new a();

    /* renamed from: e, reason: collision with root package name */
    private final String f9957e;

    /* renamed from: f, reason: collision with root package name */
    private final String f9958f;

    /* renamed from: g, reason: collision with root package name */
    private final List<OAssistInputParam> f9959g;

    /* renamed from: h, reason: collision with root package name */
    private final String f9960h;

    /* compiled from: OAssistData.kt */
    /* loaded from: classes.dex */
    public static final class a implements Parcelable.Creator<OAssistInput> {
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final OAssistInput createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            int readInt = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt);
            for (int i10 = 0; i10 != readInt; i10++) {
                arrayList.add(OAssistInputParam.CREATOR.createFromParcel(parcel));
            }
            return new OAssistInput(readString, readString2, arrayList, parcel.readString());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final OAssistInput[] newArray(int i10) {
            return new OAssistInput[i10];
        }
    }

    public OAssistInput(String str, String str2, List<OAssistInputParam> list, String str3) {
        k.e(str, "apiName");
        k.e(str2, "packageName");
        k.e(list, "params");
        k.e(str3, "requestId");
        this.f9957e = str;
        this.f9958f = str2;
        this.f9959g = list;
        this.f9960h = str3;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OAssistInput)) {
            return false;
        }
        OAssistInput oAssistInput = (OAssistInput) obj;
        return k.a(this.f9957e, oAssistInput.f9957e) && k.a(this.f9958f, oAssistInput.f9958f) && k.a(this.f9959g, oAssistInput.f9959g) && k.a(this.f9960h, oAssistInput.f9960h);
    }

    public int hashCode() {
        return (((((this.f9957e.hashCode() * 31) + this.f9958f.hashCode()) * 31) + this.f9959g.hashCode()) * 31) + this.f9960h.hashCode();
    }

    public final String j() {
        return this.f9957e;
    }

    public final List<OAssistInputParam> k() {
        return this.f9959g;
    }

    public String toString() {
        return "OAssistInput(apiName=" + this.f9957e + ", packageName=" + this.f9958f + ", params=" + this.f9959g + ", requestId=" + this.f9960h + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "out");
        parcel.writeString(this.f9957e);
        parcel.writeString(this.f9958f);
        List<OAssistInputParam> list = this.f9959g;
        parcel.writeInt(list.size());
        Iterator<OAssistInputParam> it = list.iterator();
        while (it.hasNext()) {
            it.next().writeToParcel(parcel, i10);
        }
        parcel.writeString(this.f9960h);
    }
}
