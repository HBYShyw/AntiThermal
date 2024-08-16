package com.oplus.deepthinker.sdk.app.geofence;

import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import kotlin.Metadata;
import ma.Unit;
import ya.l;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: Geofence.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0006\n\u0002\b\f\n\u0002\u0010\t\n\u0002\b\r\u0018\u0000 &2\u00020\u0001:\u0003'&(BC\b\u0002\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002\u0012\u0006\u0010\u0010\u001a\u00020\u000f\u0012\u0006\u0010\u0014\u001a\u00020\u000f\u0012\u0006\u0010\u0016\u001a\u00020\u0006\u0012\u0006\u0010\u001a\u001a\u00020\u0006\u0012\u0006\u0010\u001d\u001a\u00020\u001c\u0012\u0006\u0010!\u001a\u00020\u001c¢\u0006\u0004\b#\u0010$B\u0011\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b#\u0010%J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0018\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\n\u001a\u00020\u0006H\u0016R\u0019\u0010\u000b\u001a\u0004\u0018\u00010\u00028\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0010\u001a\u00020\u000f8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0014\u001a\u00020\u000f8\u0006¢\u0006\f\n\u0004\b\u0014\u0010\u0011\u001a\u0004\b\u0015\u0010\u0013R\u0017\u0010\u0016\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0018\u0010\u0019R\u0017\u0010\u001a\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\u001a\u0010\u0017\u001a\u0004\b\u001b\u0010\u0019R\u0017\u0010\u001d\u001a\u00020\u001c8\u0006¢\u0006\f\n\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001f\u0010 R\u0017\u0010!\u001a\u00020\u001c8\u0006¢\u0006\f\n\u0004\b!\u0010\u001e\u001a\u0004\b\"\u0010 ¨\u0006)"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/Geofence;", "Landroid/os/Parcelable;", "", "toString", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "id", "Ljava/lang/String;", "getId", "()Ljava/lang/String;", "", "longitude", "D", "getLongitude", "()D", "latitude", "getLatitude", "radius", "I", "getRadius", "()I", "transitionTypes", "getTransitionTypes", "", "loiteringDelayMs", "J", "getLoiteringDelayMs", "()J", "expirationDurationInMillis", "getExpirationDurationInMillis", "<init>", "(Ljava/lang/String;DDIIJJ)V", "(Landroid/os/Parcel;)V", "CREATOR", "Builder", "TransitionType", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class Geofence implements Parcelable {

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int DWELL_VALUE = 4;
    private static final int ENTER_VALUE = 1;
    private static final int EXIT_VALUE = 2;
    public static final long LOITERING_DELAY_NOT_SET = 0;
    public static final int MIN_RADIUS = 150;
    public static final long NEVER_EXPIRE = 0;
    private static final int UNKNOWN_VALUE = 0;

    /* renamed from: expirationDurationInMillis, reason: from kotlin metadata and from toString */
    private final long Expiration;
    private final String id;

    /* renamed from: latitude, reason: from kotlin metadata and from toString */
    private final double Latitude;

    /* renamed from: loiteringDelayMs, reason: from kotlin metadata and from toString */
    private final long LoiteringDelay;

    /* renamed from: longitude, reason: from kotlin metadata and from toString */
    private final double Longitude;

    /* renamed from: radius, reason: from kotlin metadata and from toString */
    private final int Radius;

    /* renamed from: transitionTypes, reason: from kotlin metadata and from toString */
    private final int TransitionTypes;

    /* compiled from: Geofence.kt */
    @Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\u0010J \u0010\u0011\u001a\u00020\u00002\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\rJ\u000e\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0004J\u0010\u0010\u0014\u001a\u00020\u00002\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J\u000e\u0010\u0015\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0004J\u001f\u0010\u0016\u001a\u00020\u00002\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00180\u0017\"\u00020\u0018¢\u0006\u0002\u0010\u0019R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$Builder;", "", "()V", "expirationDurationInMillis", "", "geofenceId", "", "latitude", "", "Ljava/lang/Double;", "loiteringDelayMs", "longitude", "radius", "", "transitionTypes", "build", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence;", "setCircularRegion", "setExpirationDuration", "durationMillis", "setGeofenceId", "setLoiteringDelay", "setTransitionTypes", "", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$TransitionType;", "([Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$TransitionType;)Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$Builder;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Builder {
        private long expirationDurationInMillis;
        private String geofenceId;
        private Double latitude;
        private long loiteringDelayMs;
        private Double longitude;
        private int radius = Geofence.MIN_RADIUS;
        private int transitionTypes = TransitionType.UNKNOWN.getValue();

        public static /* synthetic */ Builder setCircularRegion$default(Builder builder, double d10, double d11, int i10, int i11, Object obj) {
            if ((i11 & 4) != 0) {
                i10 = Geofence.MIN_RADIUS;
            }
            return builder.setCircularRegion(d10, d11, i10);
        }

        public final Geofence build() {
            String str = this.geofenceId;
            Double d10 = this.longitude;
            k.b(d10);
            double doubleValue = d10.doubleValue();
            Double d11 = this.latitude;
            k.b(d11);
            return new Geofence(str, doubleValue, d11.doubleValue(), this.radius, this.transitionTypes, this.loiteringDelayMs, this.expirationDurationInMillis, null);
        }

        public final Builder setCircularRegion(double longitude, double latitude, int radius) {
            this.longitude = Double.valueOf(longitude);
            this.latitude = Double.valueOf(latitude);
            this.radius = radius;
            return this;
        }

        public final Builder setExpirationDuration(long durationMillis) {
            this.expirationDurationInMillis = durationMillis;
            return this;
        }

        public final Builder setGeofenceId(String geofenceId) {
            this.geofenceId = geofenceId;
            return this;
        }

        public final Builder setLoiteringDelay(long loiteringDelayMs) {
            this.loiteringDelayMs = loiteringDelayMs;
            return this;
        }

        public final Builder setTransitionTypes(TransitionType... transitionTypes) {
            k.e(transitionTypes, "transitionTypes");
            int length = transitionTypes.length;
            int i10 = 0;
            while (i10 < length) {
                TransitionType transitionType = transitionTypes[i10];
                i10++;
                this.transitionTypes = transitionType.getValue() | this.transitionTypes;
            }
            return this;
        }
    }

    /* compiled from: Geofence.kt */
    @Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\b\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u001a\u0010\u001bJ \u0010\u0007\u001a\u00020\u00022\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003H\u0087\bø\u0001\u0000J\u0010\u0010\n\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0016J\u001f\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r2\u0006\u0010\f\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u000b8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u000b8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0012\u0010\u0011R\u0014\u0010\u0013\u001a\u00020\u000b8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0013\u0010\u0011R\u0014\u0010\u0015\u001a\u00020\u00148\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u000b8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0017\u0010\u0011R\u0014\u0010\u0018\u001a\u00020\u00148\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0018\u0010\u0016R\u0014\u0010\u0019\u001a\u00020\u000b8\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0019\u0010\u0011\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001c"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence;", "Lkotlin/Function1;", "Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$Builder;", "Lma/f0;", "block", "build", "Landroid/os/Parcel;", "parcel", "createFromParcel", "", "size", "", "newArray", "(I)[Lcom/oplus/deepthinker/sdk/app/geofence/Geofence;", "DWELL_VALUE", "I", "ENTER_VALUE", "EXIT_VALUE", "", "LOITERING_DELAY_NOT_SET", "J", "MIN_RADIUS", "NEVER_EXPIRE", "UNKNOWN_VALUE", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: com.oplus.deepthinker.sdk.app.geofence.Geofence$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<Geofence> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Geofence build(l<? super Builder, Unit> lVar) {
            k.e(lVar, "block");
            Builder builder = new Builder();
            lVar.invoke(builder);
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Geofence createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new Geofence(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Geofence[] newArray(int size) {
            return new Geofence[size];
        }
    }

    /* compiled from: Geofence.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/geofence/Geofence$TransitionType;", "", ThermalBaseConfig.Item.ATTR_VALUE, "", "(Ljava/lang/String;II)V", "getValue", "()I", "ENTER", "EXIT", "DWELL", "UNKNOWN", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public enum TransitionType {
        ENTER(1),
        EXIT(2),
        DWELL(4),
        UNKNOWN(0);

        private final int value;

        TransitionType(int i10) {
            this.value = i10;
        }

        public final int getValue() {
            return this.value;
        }
    }

    private Geofence(String str, double d10, double d11, int i10, int i11, long j10, long j11) {
        this.id = str;
        this.Longitude = d10;
        this.Latitude = d11;
        this.Radius = i10;
        this.TransitionTypes = i11;
        this.LoiteringDelay = j10;
        this.Expiration = j11;
    }

    public /* synthetic */ Geofence(String str, double d10, double d11, int i10, int i11, long j10, long j11, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, d10, d11, i10, i11, j10, j11);
    }

    public static final Geofence build(l<? super Builder, Unit> lVar) {
        return INSTANCE.build(lVar);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* renamed from: getExpirationDurationInMillis, reason: from getter */
    public final long getExpiration() {
        return this.Expiration;
    }

    public final String getId() {
        return this.id;
    }

    public final double getLatitude() {
        return this.Latitude;
    }

    /* renamed from: getLoiteringDelayMs, reason: from getter */
    public final long getLoiteringDelay() {
        return this.LoiteringDelay;
    }

    public final double getLongitude() {
        return this.Longitude;
    }

    public final int getRadius() {
        return this.Radius;
    }

    public final int getTransitionTypes() {
        return this.TransitionTypes;
    }

    public String toString() {
        return "Geofence: GeofenceId=" + ((Object) this.id) + ", Longitude=" + this.Longitude + ", Latitude=" + this.Latitude + ", Radius=" + this.Radius + ", TransitionTypes=" + this.TransitionTypes + ", LoiteringDelay=" + this.LoiteringDelay + ", Expiration=" + this.Expiration;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeString(this.id);
        parcel.writeDouble(this.Longitude);
        parcel.writeDouble(this.Latitude);
        parcel.writeInt(this.Radius);
        parcel.writeInt(this.TransitionTypes);
        parcel.writeLong(this.LoiteringDelay);
        parcel.writeLong(this.Expiration);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public Geofence(Parcel parcel) {
        this(parcel.readString(), parcel.readDouble(), parcel.readDouble(), parcel.readInt(), parcel.readInt(), parcel.readLong(), parcel.readLong());
        k.e(parcel, "parcel");
    }
}
