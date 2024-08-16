package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ProvinceCityEvent.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\b\u0086\b\u0018\u0000 !2\u00020\u0001:\u0001!B#\u0012\u0006\u0010\r\u001a\u00020\u0004\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\n\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\n¢\u0006\u0004\b\u001e\u0010\u001fB\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u001e\u0010 J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\t\u0010\t\u001a\u00020\u0004HÆ\u0003J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\nHÆ\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\nHÆ\u0003J+\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\r\u001a\u00020\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\nHÆ\u0001J\t\u0010\u0011\u001a\u00020\nHÖ\u0001J\t\u0010\u0012\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\u0016\u001a\u00020\u00152\b\u0010\u0014\u001a\u0004\u0018\u00010\u0013HÖ\u0003R\u0017\u0010\r\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\r\u0010\u0017\u001a\u0004\b\u0018\u0010\u0019R\u0019\u0010\u000e\u001a\u0004\u0018\u00010\n8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u001a\u001a\u0004\b\u001b\u0010\u001cR\u0019\u0010\u000f\u001a\u0004\u0018\u00010\n8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u001a\u001a\u0004\b\u001d\u0010\u001c¨\u0006\""}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ProvinceCityEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "component1", "", "component2", "component3", "type", EventType.CityFenceExtra.BUNDLE_KEY_NEW, EventType.CityFenceExtra.BUNDLE_KEY_OLD, "copy", "toString", "hashCode", "", "other", "", "equals", "I", "getType", "()I", "Ljava/lang/String;", "getNew", "()Ljava/lang/String;", "getOld", "<init>", "(ILjava/lang/String;Ljava/lang/String;)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class ProvinceCityEvent implements Parcelable {
    public static final String BUNDLE_KEY_PROVINCE_CITY_EVENT = "province_city_event";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final String new;
    private final String old;
    private final int type;

    /* compiled from: ProvinceCityEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u001d\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016¢\u0006\u0002\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ProvinceCityEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ProvinceCityEvent;", "()V", "BUNDLE_KEY_PROVINCE_CITY_EVENT", "", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ProvinceCityEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.ProvinceCityEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<ProvinceCityEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProvinceCityEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new ProvinceCityEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProvinceCityEvent[] newArray(int size) {
            return new ProvinceCityEvent[size];
        }
    }

    public ProvinceCityEvent(int i10, String str, String str2) {
        this.type = i10;
        this.new = str;
        this.old = str2;
    }

    public static /* synthetic */ ProvinceCityEvent copy$default(ProvinceCityEvent provinceCityEvent, int i10, String str, String str2, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            i10 = provinceCityEvent.type;
        }
        if ((i11 & 2) != 0) {
            str = provinceCityEvent.new;
        }
        if ((i11 & 4) != 0) {
            str2 = provinceCityEvent.old;
        }
        return provinceCityEvent.copy(i10, str, str2);
    }

    /* renamed from: component1, reason: from getter */
    public final int getType() {
        return this.type;
    }

    /* renamed from: component2, reason: from getter */
    public final String getNew() {
        return this.new;
    }

    /* renamed from: component3, reason: from getter */
    public final String getOld() {
        return this.old;
    }

    public final ProvinceCityEvent copy(int type, String r22, String old) {
        return new ProvinceCityEvent(type, r22, old);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProvinceCityEvent)) {
            return false;
        }
        ProvinceCityEvent provinceCityEvent = (ProvinceCityEvent) other;
        return this.type == provinceCityEvent.type && k.a(this.new, provinceCityEvent.new) && k.a(this.old, provinceCityEvent.old);
    }

    public final String getNew() {
        return this.new;
    }

    public final String getOld() {
        return this.old;
    }

    public final int getType() {
        return this.type;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.type) * 31;
        String str = this.new;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.old;
        return hashCode2 + (str2 != null ? str2.hashCode() : 0);
    }

    public String toString() {
        return "ProvinceCityEvent(type=" + this.type + ", new=" + ((Object) this.new) + ", old=" + ((Object) this.old) + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeInt(this.type);
        parcel.writeString(this.new);
        parcel.writeString(this.old);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ProvinceCityEvent(Parcel parcel) {
        this(parcel.readInt(), parcel.readString(), parcel.readString());
        k.e(parcel, "parcel");
    }
}
