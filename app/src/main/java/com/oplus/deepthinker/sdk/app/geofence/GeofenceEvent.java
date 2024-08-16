package com.oplus.deepthinker.sdk.app.geofence;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.deepthinker.sdk.app.geofence.Geofence;
import java.util.List;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: GeofenceEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB)\u0012\u000e\u0010\r\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b\u0012\b\u0010\u0012\u001a\u0004\u0018\u00010\u0011\u0012\u0006\u0010\u0017\u001a\u00020\u0016¢\u0006\u0004\b\u001b\u0010\u001cB\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u001b\u0010\u001dJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\b\u0010\n\u001a\u00020\tH\u0016R\u001f\u0010\r\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b8\u0006¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u0019\u0010\u0012\u001a\u0004\u0018\u00010\u00118\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0017\u001a\u00020\u00168\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001a¨\u0006\u001f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "toString", "", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence;", "triggeringGeofences", "Ljava/util/List;", "getTriggeringGeofences", "()Ljava/util/List;", "Landroid/location/Location;", "triggeringLocation", "Landroid/location/Location;", "getTriggeringLocation", "()Landroid/location/Location;", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$TransitionType;", "geofenceTransition", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$TransitionType;", "getGeofenceTransition", "()Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$TransitionType;", "<init>", "(Ljava/util/List;Landroid/location/Location;Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$TransitionType;)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class GeofenceEvent implements Parcelable {

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final Geofence.TransitionType geofenceTransition;
    private final List<Geofence> triggeringGeofences;
    private final Location triggeringLocation;

    /* compiled from: GeofenceEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0014\u0010\u0007\u001a\u0004\u0018\u00010\u00022\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0007J\u001d\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceEvent;", "()V", "createFromParcel", "parcel", "Landroid/os/Parcel;", "fromBundle", "bundle", "Landroid/os/Bundle;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/geofence/GeofenceEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.geofence.GeofenceEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<GeofenceEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final GeofenceEvent fromBundle(Bundle bundle) {
            if (bundle == null) {
                return null;
            }
            return (GeofenceEvent) bundle.getParcelable(EventType.GeoFenceExtra.BUNDLE_KEY_GEOFENCE_EVENT);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeofenceEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new GeofenceEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeofenceEvent[] newArray(int size) {
            return new GeofenceEvent[size];
        }
    }

    public GeofenceEvent(List<Geofence> list, Location location, Geofence.TransitionType transitionType) {
        k.e(transitionType, "geofenceTransition");
        this.triggeringGeofences = list;
        this.triggeringLocation = location;
        this.geofenceTransition = transitionType;
    }

    public static final GeofenceEvent fromBundle(Bundle bundle) {
        return INSTANCE.fromBundle(bundle);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final Geofence.TransitionType getGeofenceTransition() {
        return this.geofenceTransition;
    }

    public final List<Geofence> getTriggeringGeofences() {
        return this.triggeringGeofences;
    }

    public final Location getTriggeringLocation() {
        return this.triggeringLocation;
    }

    public String toString() {
        return "GeofenceEvent(triggeringGeofences=" + this.triggeringGeofences + ", triggeringLocation=" + this.triggeringLocation + ", geofenceTransition=" + this.geofenceTransition + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeTypedList(this.triggeringGeofences);
        parcel.writeParcelable(this.triggeringLocation, i10);
        parcel.writeInt(this.geofenceTransition.ordinal());
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public GeofenceEvent(Parcel parcel) {
        this(parcel.createTypedArrayList(Geofence.INSTANCE), (Location) parcel.readParcelable(Location.class.getClassLoader()), Geofence.TransitionType.values()[parcel.readInt()]);
        k.e(parcel, "parcel");
    }
}
