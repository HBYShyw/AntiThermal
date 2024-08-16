package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: NearFieldRecommendShopData.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0086\b\u0018\u0000 +2\u00020\u0001:\u0001+B-\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\t\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\u0011\u001a\u00020\u0004\u0012\b\u0010\u0012\u001a\u0004\u0018\u00010\t¢\u0006\u0004\b(\u0010)B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b(\u0010*J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\b\u0010\n\u001a\u00020\tH\u0016J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\tHÆ\u0003J\t\u0010\r\u001a\u00020\u0004HÆ\u0003J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\tHÆ\u0003J7\u0010\u0013\u001a\u00020\u00002\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u0011\u001a\u00020\u00042\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\tHÆ\u0001J\t\u0010\u0014\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\u0018\u001a\u00020\u00172\b\u0010\u0016\u001a\u0004\u0018\u00010\u0015HÖ\u0003R\u0019\u0010\u000f\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u0019\u001a\u0004\b\u001a\u0010\u001bR\u0019\u0010\u0010\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0019\u001a\u0004\b\u001c\u0010\u001bR\u0017\u0010\u0011\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0011\u0010\u001d\u001a\u0004\b\u001e\u0010\u001fR\u0019\u0010\u0012\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0019\u001a\u0004\b \u0010\u001bR$\u0010\"\u001a\u0004\u0018\u00010!8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\"\u0010#\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'¨\u0006,"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldRecommendShopData;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "toString", "component1", "component2", "component3", "component4", "poiId", "shopName", "brandId", "brandName", "copy", "hashCode", "", "other", "", "equals", "Ljava/lang/String;", "getPoiId", "()Ljava/lang/String;", "getShopName", "I", "getBrandId", "()I", "getBrandName", "Landroid/os/Bundle;", DeviceDomainManager.ARG_EXTRA, "Landroid/os/Bundle;", "getExtra", "()Landroid/os/Bundle;", "setExtra", "(Landroid/os/Bundle;)V", "<init>", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class NearFieldRecommendShopData implements Parcelable {

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final int brandId;
    private final String brandName;
    private Bundle extra;
    private final String poiId;
    private final String shopName;

    /* compiled from: NearFieldRecommendShopData.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u001d\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016¢\u0006\u0002\u0010\u000b¨\u0006\f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldRecommendShopData$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldRecommendShopData;", "()V", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldRecommendShopData;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.NearFieldRecommendShopData$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<NearFieldRecommendShopData> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NearFieldRecommendShopData createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new NearFieldRecommendShopData(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NearFieldRecommendShopData[] newArray(int size) {
            return new NearFieldRecommendShopData[size];
        }
    }

    public NearFieldRecommendShopData(String str, String str2, int i10, String str3) {
        this.poiId = str;
        this.shopName = str2;
        this.brandId = i10;
        this.brandName = str3;
    }

    public static /* synthetic */ NearFieldRecommendShopData copy$default(NearFieldRecommendShopData nearFieldRecommendShopData, String str, String str2, int i10, String str3, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            str = nearFieldRecommendShopData.poiId;
        }
        if ((i11 & 2) != 0) {
            str2 = nearFieldRecommendShopData.shopName;
        }
        if ((i11 & 4) != 0) {
            i10 = nearFieldRecommendShopData.brandId;
        }
        if ((i11 & 8) != 0) {
            str3 = nearFieldRecommendShopData.brandName;
        }
        return nearFieldRecommendShopData.copy(str, str2, i10, str3);
    }

    /* renamed from: component1, reason: from getter */
    public final String getPoiId() {
        return this.poiId;
    }

    /* renamed from: component2, reason: from getter */
    public final String getShopName() {
        return this.shopName;
    }

    /* renamed from: component3, reason: from getter */
    public final int getBrandId() {
        return this.brandId;
    }

    /* renamed from: component4, reason: from getter */
    public final String getBrandName() {
        return this.brandName;
    }

    public final NearFieldRecommendShopData copy(String poiId, String shopName, int brandId, String brandName) {
        return new NearFieldRecommendShopData(poiId, shopName, brandId, brandName);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NearFieldRecommendShopData)) {
            return false;
        }
        NearFieldRecommendShopData nearFieldRecommendShopData = (NearFieldRecommendShopData) other;
        return k.a(this.poiId, nearFieldRecommendShopData.poiId) && k.a(this.shopName, nearFieldRecommendShopData.shopName) && this.brandId == nearFieldRecommendShopData.brandId && k.a(this.brandName, nearFieldRecommendShopData.brandName);
    }

    public final int getBrandId() {
        return this.brandId;
    }

    public final String getBrandName() {
        return this.brandName;
    }

    public final Bundle getExtra() {
        return this.extra;
    }

    public final String getPoiId() {
        return this.poiId;
    }

    public final String getShopName() {
        return this.shopName;
    }

    public int hashCode() {
        String str = this.poiId;
        int hashCode = (str == null ? 0 : str.hashCode()) * 31;
        String str2 = this.shopName;
        int hashCode2 = (((hashCode + (str2 == null ? 0 : str2.hashCode())) * 31) + Integer.hashCode(this.brandId)) * 31;
        String str3 = this.brandName;
        return hashCode2 + (str3 != null ? str3.hashCode() : 0);
    }

    public final void setExtra(Bundle bundle) {
        this.extra = bundle;
    }

    public String toString() {
        return "NearFieldRecommendShopData(poiId=" + ((Object) this.poiId) + ", brandId=" + this.brandId + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeString(this.poiId);
        parcel.writeString(this.shopName);
        parcel.writeInt(this.brandId);
        parcel.writeString(this.brandName);
        parcel.writeBundle(this.extra);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public NearFieldRecommendShopData(Parcel parcel) {
        this(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readString());
        k.e(parcel, "parcel");
        this.extra = parcel.readBundle(NearFieldRecommendShopData.class.getClassLoader());
    }
}
