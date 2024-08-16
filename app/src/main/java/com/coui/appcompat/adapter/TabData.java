package com.coui.appcompat.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import za.k;

/* compiled from: COUIMultiTabAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0017\b\u0087\b\u0018\u0000 #2\u00020\u0001:\u0001$B+\u0012\b\b\u0001\u0010\u0014\u001a\u00020\u0004\u0012\b\u0010\u0019\u001a\u0004\u0018\u00010\u0002\u0012\u0006\u0010\u001c\u001a\u00020\b\u0012\u0006\u0010 \u001a\u00020\u0004¢\u0006\u0004\b!\u0010\"J\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006HÖ\u0003J\t\u0010\n\u001a\u00020\u0004HÖ\u0001J\u0019\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0004HÖ\u0001R\u0017\u0010\u0014\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u0019\u0010\u0019\u001a\u0004\u0018\u00010\u00028\u0006¢\u0006\f\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u001c\u001a\u00020\b8\u0006¢\u0006\f\n\u0004\b\u001a\u0010\u001b\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010 \u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u001e\u0010\u0011\u001a\u0004\b\u001f\u0010\u0013¨\u0006%"}, d2 = {"com/coui/appcompat/adapter/COUIMultiTabAdapter$TabData", "Landroid/os/Parcelable;", "", "toString", "", "hashCode", "", "other", "", "equals", "describeContents", "Landroid/os/Parcel;", "parcel", "flags", "Lma/f0;", "writeToParcel", "e", "I", "getResourceId", "()I", "resourceId", "f", "Ljava/lang/String;", "getName", "()Ljava/lang/String;", "name", "g", "Z", "isRedDot", "()Z", "h", "getPointNum", "pointNum", "<init>", "(ILjava/lang/String;ZI)V", "i", "a", "coui-support-nearx_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: com.coui.appcompat.adapter.COUIMultiTabAdapter$TabData, reason: from toString */
/* loaded from: classes.dex */
public final /* data */ class TabData implements Parcelable {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata and from toString */
    private final int resourceId;

    /* renamed from: f, reason: collision with root package name and from kotlin metadata and from toString */
    private final String name;

    /* renamed from: g, reason: collision with root package name and from kotlin metadata and from toString */
    private final boolean isRedDot;

    /* renamed from: h, reason: collision with root package name and from kotlin metadata and from toString */
    private final int pointNum;
    public static final Parcelable.Creator<TabData> CREATOR = new b();

    /* compiled from: COUIMultiTabAdapter.kt */
    @Metadata(k = 3, mv = {1, 5, 1}, xi = 48)
    /* renamed from: com.coui.appcompat.adapter.COUIMultiTabAdapter$TabData$b */
    /* loaded from: classes.dex */
    public static final class b implements Parcelable.Creator<TabData> {
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final TabData createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new TabData(parcel.readInt(), parcel.readString(), parcel.readInt() != 0, parcel.readInt());
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public final TabData[] newArray(int i10) {
            return new TabData[i10];
        }
    }

    public TabData(int i10, String str, boolean z10, int i11) {
        this.resourceId = i10;
        this.name = str;
        this.isRedDot = z10;
        this.pointNum = i11;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TabData)) {
            return false;
        }
        TabData tabData = (TabData) other;
        return this.resourceId == tabData.resourceId && k.a(this.name, tabData.name) && this.isRedDot == tabData.isRedDot && this.pointNum == tabData.pointNum;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int hashCode() {
        int hashCode = Integer.hashCode(this.resourceId) * 31;
        String str = this.name;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        boolean z10 = this.isRedDot;
        int i10 = z10;
        if (z10 != 0) {
            i10 = 1;
        }
        return ((hashCode2 + i10) * 31) + Integer.hashCode(this.pointNum);
    }

    public String toString() {
        return "TabData(resourceId=" + this.resourceId + ", name=" + ((Object) this.name) + ", isRedDot=" + this.isRedDot + ", pointNum=" + this.pointNum + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "out");
        parcel.writeInt(this.resourceId);
        parcel.writeString(this.name);
        parcel.writeInt(this.isRedDot ? 1 : 0);
        parcel.writeInt(this.pointNum);
    }
}
