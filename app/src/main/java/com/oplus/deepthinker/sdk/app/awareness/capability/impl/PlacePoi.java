package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: PlacePoi.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0018\b\u0086\b\u0018\u0000 +2\u00020\u0001:\u0001+B3\u0012\b\u0010\u0016\u001a\u0004\u0018\u00010\u000e\u0012\b\u0010\u0017\u001a\u0004\u0018\u00010\u000e\u0012\u0006\u0010\u0018\u001a\u00020\u0004\u0012\u0006\u0010\u0019\u001a\u00020\u000b\u0012\u0006\u0010\u001a\u001a\u00020\u0014¢\u0006\u0004\b(\u0010)B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b(\u0010*J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\u0013\u0010\f\u001a\u00020\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0096\u0002J\b\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u000eHÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u000eHÆ\u0003J\t\u0010\u0012\u001a\u00020\u0004HÆ\u0003J\t\u0010\u0013\u001a\u00020\u000bHÆ\u0003J\t\u0010\u0015\u001a\u00020\u0014HÆ\u0003J?\u0010\u001b\u001a\u00020\u00002\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u0018\u001a\u00020\u00042\b\b\u0002\u0010\u0019\u001a\u00020\u000b2\b\b\u0002\u0010\u001a\u001a\u00020\u0014HÆ\u0001R\u0019\u0010\u0016\u001a\u0004\u0018\u00010\u000e8\u0006¢\u0006\f\n\u0004\b\u0016\u0010\u001c\u001a\u0004\b\u001d\u0010\u001eR\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u000e8\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u001c\u001a\u0004\b\u001f\u0010\u001eR\u0017\u0010\u0018\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0018\u0010 \u001a\u0004\b!\u0010\"R\u0017\u0010\u0019\u001a\u00020\u000b8\u0006¢\u0006\f\n\u0004\b\u0019\u0010#\u001a\u0004\b\u0019\u0010$R\u0017\u0010\u001a\u001a\u00020\u00148\u0006¢\u0006\f\n\u0004\b\u001a\u0010%\u001a\u0004\b&\u0010'¨\u0006,"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "other", "", "equals", "hashCode", "", "toString", "component1", "component2", "component3", "component4", "", "component5", "name", "poiUid", "type", "isInArea", "distance", "copy", "Ljava/lang/String;", "getName", "()Ljava/lang/String;", "getPoiUid", "I", "getType", "()I", "Z", "()Z", "D", "getDistance", "()D", "<init>", "(Ljava/lang/String;Ljava/lang/String;IZD)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class PlacePoi implements Parcelable {
    public static final int BASE_HASH_NUM = 17;

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int HASH_MULTIPLIER = 31;
    private final double distance;
    private final boolean isInArea;
    private final String name;
    private final String poiUid;
    private final int type;

    /* compiled from: PlacePoi.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0016J\u001d\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005H\u0016¢\u0006\u0002\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;", "()V", "BASE_HASH_NUM", "", "HASH_MULTIPLIER", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.PlacePoi$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<PlacePoi> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PlacePoi createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new PlacePoi(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PlacePoi[] newArray(int size) {
            return new PlacePoi[size];
        }
    }

    public PlacePoi(String str, String str2, int i10, boolean z10, double d10) {
        this.name = str;
        this.poiUid = str2;
        this.type = i10;
        this.isInArea = z10;
        this.distance = d10;
    }

    public static /* synthetic */ PlacePoi copy$default(PlacePoi placePoi, String str, String str2, int i10, boolean z10, double d10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            str = placePoi.name;
        }
        if ((i11 & 2) != 0) {
            str2 = placePoi.poiUid;
        }
        String str3 = str2;
        if ((i11 & 4) != 0) {
            i10 = placePoi.type;
        }
        int i12 = i10;
        if ((i11 & 8) != 0) {
            z10 = placePoi.isInArea;
        }
        boolean z11 = z10;
        if ((i11 & 16) != 0) {
            d10 = placePoi.distance;
        }
        return placePoi.copy(str, str3, i12, z11, d10);
    }

    /* renamed from: component1, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* renamed from: component2, reason: from getter */
    public final String getPoiUid() {
        return this.poiUid;
    }

    /* renamed from: component3, reason: from getter */
    public final int getType() {
        return this.type;
    }

    /* renamed from: component4, reason: from getter */
    public final boolean getIsInArea() {
        return this.isInArea;
    }

    /* renamed from: component5, reason: from getter */
    public final double getDistance() {
        return this.distance;
    }

    public final PlacePoi copy(String name, String poiUid, int type, boolean isInArea, double distance) {
        return new PlacePoi(name, poiUid, type, isInArea, distance);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (!(other instanceof PlacePoi)) {
            return false;
        }
        if (this != other) {
            PlacePoi placePoi = (PlacePoi) other;
            if (!k.a(this.poiUid, placePoi.poiUid) || !k.a(this.name, placePoi.name)) {
                return false;
            }
        }
        return true;
    }

    public final double getDistance() {
        return this.distance;
    }

    public final String getName() {
        return this.name;
    }

    public final String getPoiUid() {
        return this.poiUid;
    }

    public final int getType() {
        return this.type;
    }

    public int hashCode() {
        String str = this.name;
        int hashCode = (527 + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.poiUid;
        return hashCode + (str2 != null ? str2.hashCode() : 0);
    }

    public final boolean isInArea() {
        return this.isInArea;
    }

    public String toString() {
        return super.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeString(this.name);
        parcel.writeString(this.poiUid);
        parcel.writeInt(this.type);
        parcel.writeByte(this.isInArea ? (byte) 1 : (byte) 0);
        parcel.writeDouble(this.distance);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public PlacePoi(Parcel parcel) {
        this(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readByte() != 0, parcel.readDouble());
        k.e(parcel, "parcel");
    }
}
