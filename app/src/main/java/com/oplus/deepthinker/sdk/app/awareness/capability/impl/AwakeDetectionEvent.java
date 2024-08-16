package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: AwakeDetectionEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\b\u0086\b\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u000f\u0012\u0006\u0010\u000b\u001a\u00020\t¢\u0006\u0004\b\u0017\u0010\u0018B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u0017\u0010\u0019J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\t\u0010\n\u001a\u00020\tHÆ\u0003J\u0013\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u000b\u001a\u00020\tHÆ\u0001J\t\u0010\u000e\u001a\u00020\rHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\u0013\u001a\u00020\u00122\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010HÖ\u0003R\u0017\u0010\u000b\u001a\u00020\t8\u0006¢\u0006\f\n\u0004\b\u000b\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016¨\u0006\u001b"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AwakeDetectionEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "component1", "awakeTimeStamp", "copy", "", "toString", "hashCode", "", "other", "", "equals", "J", "getAwakeTimeStamp", "()J", "<init>", "(J)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class AwakeDetectionEvent implements Parcelable {
    public static final String BUNDLE_KEY_AWAKE_DETECTION_EVENT = "awake_detection_event";
    public static final String BUNDLE_KEY_AWAKE_TIME_STAMP = "awake_time_stamp";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final long awakeTimeStamp;

    /* compiled from: AwakeDetectionEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0016J\u001d\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016¢\u0006\u0002\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AwakeDetectionEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AwakeDetectionEvent;", "()V", "BUNDLE_KEY_AWAKE_DETECTION_EVENT", "", "BUNDLE_KEY_AWAKE_TIME_STAMP", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AwakeDetectionEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.AwakeDetectionEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<AwakeDetectionEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AwakeDetectionEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new AwakeDetectionEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AwakeDetectionEvent[] newArray(int size) {
            return new AwakeDetectionEvent[size];
        }
    }

    public AwakeDetectionEvent(long j10) {
        this.awakeTimeStamp = j10;
    }

    public static /* synthetic */ AwakeDetectionEvent copy$default(AwakeDetectionEvent awakeDetectionEvent, long j10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            j10 = awakeDetectionEvent.awakeTimeStamp;
        }
        return awakeDetectionEvent.copy(j10);
    }

    /* renamed from: component1, reason: from getter */
    public final long getAwakeTimeStamp() {
        return this.awakeTimeStamp;
    }

    public final AwakeDetectionEvent copy(long awakeTimeStamp) {
        return new AwakeDetectionEvent(awakeTimeStamp);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof AwakeDetectionEvent) && this.awakeTimeStamp == ((AwakeDetectionEvent) other).awakeTimeStamp;
    }

    public final long getAwakeTimeStamp() {
        return this.awakeTimeStamp;
    }

    public int hashCode() {
        return Long.hashCode(this.awakeTimeStamp);
    }

    public String toString() {
        return "AwakeDetectionEvent(awakeTimeStamp=" + this.awakeTimeStamp + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeLong(this.awakeTimeStamp);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AwakeDetectionEvent(Parcel parcel) {
        this(parcel.readLong());
        k.e(parcel, "parcel");
    }
}
