package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: TypeLocationEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\b\u0086\b\u0018\u0000  2\u00020\u0001:\u0001 B\u001f\u0012\u0006\u0010\r\u001a\u00020\u0004\u0012\u000e\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n¢\u0006\u0004\b\u001d\u0010\u001eB\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u001d\u0010\u001fJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\t\u0010\t\u001a\u00020\u0004HÆ\u0003J\u0011\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nHÆ\u0003J%\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\r\u001a\u00020\u00042\u0010\b\u0002\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nHÆ\u0001J\t\u0010\u0011\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\u0016\u001a\u00020\u00152\b\u0010\u0014\u001a\u0004\u0018\u00010\u0013HÖ\u0003R\u0017\u0010\r\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\r\u0010\u0017\u001a\u0004\b\u0018\u0010\u0019R\u001f\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c¨\u0006!"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/TypeLocationEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "component1", "Ljava/util/ArrayList;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;", "component2", "type", "poiList", "copy", "", "toString", "hashCode", "", "other", "", "equals", "I", "getType", "()I", "Ljava/util/ArrayList;", "getPoiList", "()Ljava/util/ArrayList;", "<init>", "(ILjava/util/ArrayList;)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class TypeLocationEvent implements Parcelable {
    public static final String BUNDLE_KEY_TYPE_LOCATION_EVENT = "type_location_event";
    public static final String BUNDLE_KEY_TYPE_VALUE = "poi_type";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final ArrayList<PlacePoi> poiList;
    private final int type;

    /* compiled from: TypeLocationEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0016J\u001d\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016¢\u0006\u0002\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/TypeLocationEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/TypeLocationEvent;", "()V", "BUNDLE_KEY_TYPE_LOCATION_EVENT", "", "BUNDLE_KEY_TYPE_VALUE", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/TypeLocationEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.TypeLocationEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<TypeLocationEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TypeLocationEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new TypeLocationEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TypeLocationEvent[] newArray(int size) {
            return new TypeLocationEvent[size];
        }
    }

    public TypeLocationEvent(int i10, ArrayList<PlacePoi> arrayList) {
        this.type = i10;
        this.poiList = arrayList;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ TypeLocationEvent copy$default(TypeLocationEvent typeLocationEvent, int i10, ArrayList arrayList, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = typeLocationEvent.type;
        }
        if ((i11 & 2) != 0) {
            arrayList = typeLocationEvent.poiList;
        }
        return typeLocationEvent.copy(i10, arrayList);
    }

    /* renamed from: component1, reason: from getter */
    public final int getType() {
        return this.type;
    }

    public final ArrayList<PlacePoi> component2() {
        return this.poiList;
    }

    public final TypeLocationEvent copy(int type, ArrayList<PlacePoi> poiList) {
        return new TypeLocationEvent(type, poiList);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TypeLocationEvent)) {
            return false;
        }
        TypeLocationEvent typeLocationEvent = (TypeLocationEvent) other;
        return this.type == typeLocationEvent.type && k.a(this.poiList, typeLocationEvent.poiList);
    }

    public final ArrayList<PlacePoi> getPoiList() {
        return this.poiList;
    }

    public final int getType() {
        return this.type;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.type) * 31;
        ArrayList<PlacePoi> arrayList = this.poiList;
        return hashCode + (arrayList == null ? 0 : arrayList.hashCode());
    }

    public String toString() {
        return "TypeLocationEvent(type=" + this.type + ", poiList=" + this.poiList + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeInt(this.type);
        parcel.writeTypedList(this.poiList);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public TypeLocationEvent(Parcel parcel) {
        this(parcel.readInt(), parcel.createTypedArrayList(PlacePoi.INSTANCE));
        k.e(parcel, "parcel");
    }
}
