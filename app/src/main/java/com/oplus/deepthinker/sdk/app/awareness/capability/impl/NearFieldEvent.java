package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.SDKLog;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: NearFieldEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0086\b\u0018\u0000 72\u00020\u0001:\u00017BS\u0012\u0006\u0010\u0013\u001a\u00020\u0004\u0012\u0006\u0010\u0014\u001a\u00020\u0004\u0012\b\u0010\u0015\u001a\u0004\u0018\u00010\t\u0012\b\u0010\u0016\u001a\u0004\u0018\u00010\t\u0012\b\u0010\u0017\u001a\u0004\u0018\u00010\t\u0012\b\u0010\u0018\u001a\u0004\u0018\u00010\t\u0012\b\u0010\u0019\u001a\u0004\u0018\u00010\t\u0012\b\u0010\u001a\u001a\u0004\u0018\u00010\t¢\u0006\u0004\b4\u00105B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b4\u00106J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\b\u0010\n\u001a\u00020\tH\u0016J\t\u0010\u000b\u001a\u00020\u0004HÆ\u0003J\t\u0010\f\u001a\u00020\u0004HÆ\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\tHÆ\u0003Je\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0013\u001a\u00020\u00042\b\b\u0002\u0010\u0014\u001a\u00020\u00042\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u001a\u001a\u0004\u0018\u00010\tHÆ\u0001J\t\u0010\u001c\u001a\u00020\u0004HÖ\u0001J\u0013\u0010 \u001a\u00020\u001f2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001dHÖ\u0003R\u0017\u0010\u0013\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0013\u0010!\u001a\u0004\b\"\u0010#R\u0017\u0010\u0014\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u0014\u0010!\u001a\u0004\b$\u0010#R\u0019\u0010\u0015\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u0015\u0010%\u001a\u0004\b&\u0010'R\u0019\u0010\u0016\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u0016\u0010%\u001a\u0004\b(\u0010'R\u0019\u0010\u0017\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u0017\u0010%\u001a\u0004\b)\u0010'R\u0019\u0010\u0018\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u0018\u0010%\u001a\u0004\b*\u0010'R\u0019\u0010\u0019\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u0019\u0010%\u001a\u0004\b+\u0010'R\u0019\u0010\u001a\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u001a\u0010%\u001a\u0004\b,\u0010'R$\u0010.\u001a\u0004\u0018\u00010-8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b.\u0010/\u001a\u0004\b0\u00101\"\u0004\b2\u00103¨\u00068"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "toString", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "type", "state", "name", "poiId", "categoryId", "categoryName", "longitude", "latitude", "copy", "hashCode", "", "other", "", "equals", "I", "getType", "()I", "getState", "Ljava/lang/String;", "getName", "()Ljava/lang/String;", "getPoiId", "getCategoryId", "getCategoryName", "getLongitude", "getLatitude", "Landroid/os/Bundle;", DeviceDomainManager.ARG_EXTRA, "Landroid/os/Bundle;", "getExtra", "()Landroid/os/Bundle;", "setExtra", "(Landroid/os/Bundle;)V", "<init>", "(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class NearFieldEvent implements Parcelable {
    public static final String BUNDLE_KEY_CATEGORY_LIST = "category_list";
    public static final String BUNDLE_KEY_NEAR_FIELD_EVENT = "near_field_event";
    public static final String BUNDLE_KEY_RECOMMEND_SHOP_LIST = "recommend_shop_list";
    public static final String BUNDLE_KEY_TAG_LIST = "tag_list";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final String TAG = "NearFieldEvent";
    private final String categoryId;
    private final String categoryName;
    private Bundle extra;
    private final String latitude;
    private final String longitude;
    private final String name;
    private final String poiId;
    private final int state;
    private final int type;

    /* compiled from: NearFieldEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\n\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u001d\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016¢\u0006\u0002\u0010\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldEvent;", "()V", "BUNDLE_KEY_CATEGORY_LIST", "", "BUNDLE_KEY_NEAR_FIELD_EVENT", "BUNDLE_KEY_RECOMMEND_SHOP_LIST", "BUNDLE_KEY_TAG_LIST", "TAG", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/NearFieldEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.NearFieldEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<NearFieldEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NearFieldEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new NearFieldEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NearFieldEvent[] newArray(int size) {
            return new NearFieldEvent[size];
        }
    }

    public NearFieldEvent(int i10, int i11, String str, String str2, String str3, String str4, String str5, String str6) {
        this.type = i10;
        this.state = i11;
        this.name = str;
        this.poiId = str2;
        this.categoryId = str3;
        this.categoryName = str4;
        this.longitude = str5;
        this.latitude = str6;
    }

    /* renamed from: component1, reason: from getter */
    public final int getType() {
        return this.type;
    }

    /* renamed from: component2, reason: from getter */
    public final int getState() {
        return this.state;
    }

    /* renamed from: component3, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* renamed from: component4, reason: from getter */
    public final String getPoiId() {
        return this.poiId;
    }

    /* renamed from: component5, reason: from getter */
    public final String getCategoryId() {
        return this.categoryId;
    }

    /* renamed from: component6, reason: from getter */
    public final String getCategoryName() {
        return this.categoryName;
    }

    /* renamed from: component7, reason: from getter */
    public final String getLongitude() {
        return this.longitude;
    }

    /* renamed from: component8, reason: from getter */
    public final String getLatitude() {
        return this.latitude;
    }

    public final NearFieldEvent copy(int type, int state, String name, String poiId, String categoryId, String categoryName, String longitude, String latitude) {
        return new NearFieldEvent(type, state, name, poiId, categoryId, categoryName, longitude, latitude);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NearFieldEvent)) {
            return false;
        }
        NearFieldEvent nearFieldEvent = (NearFieldEvent) other;
        return this.type == nearFieldEvent.type && this.state == nearFieldEvent.state && k.a(this.name, nearFieldEvent.name) && k.a(this.poiId, nearFieldEvent.poiId) && k.a(this.categoryId, nearFieldEvent.categoryId) && k.a(this.categoryName, nearFieldEvent.categoryName) && k.a(this.longitude, nearFieldEvent.longitude) && k.a(this.latitude, nearFieldEvent.latitude);
    }

    public final String getCategoryId() {
        return this.categoryId;
    }

    public final String getCategoryName() {
        return this.categoryName;
    }

    public final Bundle getExtra() {
        return this.extra;
    }

    public final String getLatitude() {
        return this.latitude;
    }

    public final String getLongitude() {
        return this.longitude;
    }

    public final String getName() {
        return this.name;
    }

    public final String getPoiId() {
        return this.poiId;
    }

    public final int getState() {
        return this.state;
    }

    public final int getType() {
        return this.type;
    }

    public int hashCode() {
        int hashCode = ((Integer.hashCode(this.type) * 31) + Integer.hashCode(this.state)) * 31;
        String str = this.name;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.poiId;
        int hashCode3 = (hashCode2 + (str2 == null ? 0 : str2.hashCode())) * 31;
        String str3 = this.categoryId;
        int hashCode4 = (hashCode3 + (str3 == null ? 0 : str3.hashCode())) * 31;
        String str4 = this.categoryName;
        int hashCode5 = (hashCode4 + (str4 == null ? 0 : str4.hashCode())) * 31;
        String str5 = this.longitude;
        int hashCode6 = (hashCode5 + (str5 == null ? 0 : str5.hashCode())) * 31;
        String str6 = this.latitude;
        return hashCode6 + (str6 != null ? str6.hashCode() : 0);
    }

    public final void setExtra(Bundle bundle) {
        this.extra = bundle;
    }

    public String toString() {
        return "NearFieldEvent(type=" + this.type + ", state=" + this.state + ", poiId=" + ((Object) this.poiId) + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeInt(this.type);
        parcel.writeInt(this.state);
        parcel.writeString(this.name);
        parcel.writeString(this.poiId);
        parcel.writeString(this.categoryId);
        parcel.writeString(this.categoryName);
        parcel.writeString(this.longitude);
        parcel.writeString(this.latitude);
        parcel.writeBundle(this.extra);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public NearFieldEvent(Parcel parcel) {
        this(parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
        k.e(parcel, "parcel");
        try {
            this.extra = parcel.readBundle(NearFieldEvent.class.getClassLoader());
        } catch (IllegalStateException e10) {
            SDKLog.w(TAG, k.l("readBundle warning, ", e10));
        }
    }
}
