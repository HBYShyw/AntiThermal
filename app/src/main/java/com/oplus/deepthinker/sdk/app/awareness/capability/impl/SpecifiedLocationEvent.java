package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: SpecifiedLocationEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0019\u0012\u0006\u0010\t\u001a\u00020\u0004\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\r¢\u0006\u0004\b\u0012\u0010\u0013B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0012\u0010\u0014J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016R\u0017\u0010\t\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0019\u0010\u000e\u001a\u0004\u0018\u00010\r8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u0016"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/SpecifiedLocationEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "type", "I", "getType", "()I", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;", "placePoi", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;", "getPlacePoi", "()Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;", "<init>", "(ILcom/oplus/deepthinker/sdk/app/awareness/capability/impl/PlacePoi;)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class SpecifiedLocationEvent implements Parcelable {
    public static final String BUNDLE_KEY_TYPE_LOCATION_EVENT = "specified_location_event";
    public static final String BUNDLE_KEY_TYPE_VALUE = "poi_type";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final PlacePoi placePoi;
    private final int type;

    /* compiled from: SpecifiedLocationEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0016J\u001d\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016¢\u0006\u0002\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/SpecifiedLocationEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/SpecifiedLocationEvent;", "()V", "BUNDLE_KEY_TYPE_LOCATION_EVENT", "", "BUNDLE_KEY_TYPE_VALUE", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/SpecifiedLocationEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.SpecifiedLocationEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<SpecifiedLocationEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpecifiedLocationEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new SpecifiedLocationEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SpecifiedLocationEvent[] newArray(int size) {
            return new SpecifiedLocationEvent[size];
        }
    }

    public SpecifiedLocationEvent(int i10, PlacePoi placePoi) {
        this.type = i10;
        this.placePoi = placePoi;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final PlacePoi getPlacePoi() {
        return this.placePoi;
    }

    public final int getType() {
        return this.type;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeInt(this.type);
        parcel.writeParcelable(this.placePoi, i10);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public SpecifiedLocationEvent(Parcel parcel) {
        this(parcel.readInt(), (PlacePoi) parcel.readParcelable(PlacePoi.class.getClassLoader()));
        k.e(parcel, "parcel");
    }
}
