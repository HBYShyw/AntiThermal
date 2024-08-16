package com.oplus.deepthinker.sdk.app.geofence;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.r;
import ma.Unit;
import ya.l;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: GeofenceRequest.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\n\u0018\u0000 \u00192\u00020\u0001:\u0002\u001a\u0019B\u001f\b\u0002\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u0012\u0006\u0010\u0012\u001a\u00020\u0011¢\u0006\u0004\b\u0016\u0010\u0017B\u0011\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b\u0016\u0010\u0018J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0018\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\n\u001a\u00020\u0006H\u0016R\u001d\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000b8\u0006¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0012\u001a\u00020\u00118\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u001b"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest;", "Landroid/os/Parcelable;", "", "toString", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence;", "geofenceList", "Ljava/util/List;", "getGeofenceList", "()Ljava/util/List;", "", "callbackTimeInMillis", "J", "getCallbackTimeInMillis", "()J", "<init>", "(Ljava/util/List;J)V", "(Landroid/os/Parcel;)V", "CREATOR", "Builder", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class GeofenceRequest implements Parcelable {
    public static final long CALLBACK_TIME_NOT_SET = 0;

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final long callbackTimeInMillis;
    private final List<Geofence> geofenceList;

    /* compiled from: GeofenceRequest.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0007J\u0014\u0010\n\u001a\u00020\u00002\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u000bJ\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u000f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest$Builder;", "", "()V", "callbackTimeInMillis", "", "geofenceList", "", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence;", "addGeofence", "geofence", "addGeofences", "", "build", "Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest;", "setCallbackTime", "millis", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Builder {
        private long callbackTimeInMillis;
        private List<Geofence> geofenceList = new ArrayList();

        public final Builder addGeofence(Geofence geofence) {
            k.e(geofence, "geofence");
            this.geofenceList.add(geofence);
            return this;
        }

        public final Builder addGeofences(List<Geofence> geofenceList) {
            k.e(geofenceList, "geofenceList");
            this.geofenceList.addAll(geofenceList);
            return this;
        }

        public final GeofenceRequest build() {
            if (!this.geofenceList.isEmpty()) {
                return new GeofenceRequest(this.geofenceList, this.callbackTimeInMillis, null);
            }
            throw new IllegalArgumentException("geofenceList must not be empty.");
        }

        public final Builder setCallbackTime(long millis) {
            this.callbackTimeInMillis = millis;
            return this;
        }
    }

    /* compiled from: GeofenceRequest.kt */
    @Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0013\u0010\u0014J \u0010\u0007\u001a\u00020\u00022\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003H\u0087\bø\u0001\u0000J\u0010\u0010\n\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0016J\u001f\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r2\u0006\u0010\f\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0011\u001a\u00020\u00108\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0011\u0010\u0012\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0015"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest;", "Lkotlin/Function1;", "Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest$Builder;", "Lma/f0;", "block", "build", "Landroid/os/Parcel;", "parcel", "createFromParcel", "", "size", "", "newArray", "(I)[Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceRequest;", "", "CALLBACK_TIME_NOT_SET", "J", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: com.oplus.deepthinker.sdk.app.geofence.GeofenceRequest$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<GeofenceRequest> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final GeofenceRequest build(l<? super Builder, Unit> lVar) {
            k.e(lVar, "block");
            Builder builder = new Builder();
            lVar.invoke(builder);
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeofenceRequest createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new GeofenceRequest(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeofenceRequest[] newArray(int size) {
            return new GeofenceRequest[size];
        }
    }

    private GeofenceRequest(List<Geofence> list, long j10) {
        this.geofenceList = list;
        this.callbackTimeInMillis = j10;
    }

    public /* synthetic */ GeofenceRequest(List list, long j10, DefaultConstructorMarker defaultConstructorMarker) {
        this(list, j10);
    }

    public static final GeofenceRequest build(l<? super Builder, Unit> lVar) {
        return INSTANCE.build(lVar);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final long getCallbackTimeInMillis() {
        return this.callbackTimeInMillis;
    }

    public final List<Geofence> getGeofenceList() {
        return this.geofenceList;
    }

    public String toString() {
        return "GeofenceRequest: geofenceList=" + this.geofenceList + ", callbackTimeInMillis=" + this.callbackTimeInMillis;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeTypedList(this.geofenceList);
        parcel.writeLong(this.callbackTimeInMillis);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public GeofenceRequest(Parcel parcel) {
        this(r0 == null ? r.j() : r0, parcel.readLong());
        k.e(parcel, "parcel");
        List createTypedArrayList = parcel.createTypedArrayList(Geofence.INSTANCE);
    }
}
