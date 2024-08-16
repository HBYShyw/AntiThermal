package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ActivityRecognizeEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\b\u0086\b\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u001f\u0012\u0006\u0010\r\u001a\u00020\u0004\u0012\u0006\u0010\u000e\u001a\u00020\u0004\u0012\u0006\u0010\u000f\u001a\u00020\u000b¢\u0006\u0004\b\u001f\u0010 B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u001f\u0010!J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\t\u0010\t\u001a\u00020\u0004HÆ\u0003J\t\u0010\n\u001a\u00020\u0004HÆ\u0003J\t\u0010\f\u001a\u00020\u000bHÆ\u0003J'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\u000e\u001a\u00020\u00042\b\b\u0002\u0010\u000f\u001a\u00020\u000bHÆ\u0001J\t\u0010\u0012\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\u0017\u001a\u00020\u00162\b\u0010\u0015\u001a\u0004\u0018\u00010\u0014HÖ\u0003R\u0017\u0010\r\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\r\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010\u000e\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u0018\u001a\u0004\b\u001b\u0010\u001aR\u0017\u0010\u000f\u001a\u00020\u000b8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u001c\u001a\u0004\b\u001d\u0010\u001e¨\u0006#"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ActivityRecognizeEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "component1", "component2", "", "component3", "activityType", "transitionType", "eventTimeMillis", "copy", "", "toString", "hashCode", "", "other", "", "equals", "I", "getActivityType", "()I", "getTransitionType", "J", "getEventTimeMillis", "()J", "<init>", "(IIJ)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class ActivityRecognizeEvent implements Parcelable {
    public static final int ACTIVITY_MODE_IN_ELEVATOR = 312;
    public static final int ACTIVITY_MODE_IN_FOUR_WHEELER_VEHICLE = 311;
    public static final int ACTIVITY_MODE_IN_RAIL_VEHICLE = 309;
    public static final int ACTIVITY_MODE_IN_ROAD_VEHICLE = 308;
    public static final int ACTIVITY_MODE_IN_TRANSPORTATION = 313;
    public static final int ACTIVITY_MODE_IN_TWO_WHEELER_VEHICLE = 310;
    public static final int ACTIVITY_MODE_IN_VEHICLE = 300;
    public static final int ACTIVITY_MODE_ON_BICYCLE = 301;
    public static final int ACTIVITY_MODE_ON_FOOT = 302;
    public static final int ACTIVITY_MODE_RUNNING = 307;
    public static final int ACTIVITY_MODE_STILL = 303;
    public static final int ACTIVITY_MODE_TILTING = 305;
    public static final int ACTIVITY_MODE_UNKNOWN_ACTIVITY = 304;
    public static final int ACTIVITY_MODE_WALKING = 306;
    public static final String BUNDLE_KEY_ACTIVITY_RECOGNIZE_EVENT = "activity_recognize_event";
    public static final String BUNDLE_KEY_ACTIVITY_TYPE = "activity_type";
    public static final String BUNDLE_KEY_EVENT_TIME = "event_time";
    public static final String BUNDLE_KEY_TRANSITION_TYPE = "transition_type";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final int activityType;
    private final long eventTimeMillis;
    private final int transitionType;

    /* compiled from: ActivityRecognizeEvent.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0018\u001a\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u001d\u0010\u001b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0005H\u0016¢\u0006\u0002\u0010\u001eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0014X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ActivityRecognizeEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ActivityRecognizeEvent;", "()V", "ACTIVITY_MODE_IN_ELEVATOR", "", "ACTIVITY_MODE_IN_FOUR_WHEELER_VEHICLE", "ACTIVITY_MODE_IN_RAIL_VEHICLE", "ACTIVITY_MODE_IN_ROAD_VEHICLE", "ACTIVITY_MODE_IN_TRANSPORTATION", "ACTIVITY_MODE_IN_TWO_WHEELER_VEHICLE", "ACTIVITY_MODE_IN_VEHICLE", "ACTIVITY_MODE_ON_BICYCLE", "ACTIVITY_MODE_ON_FOOT", "ACTIVITY_MODE_RUNNING", "ACTIVITY_MODE_STILL", "ACTIVITY_MODE_TILTING", "ACTIVITY_MODE_UNKNOWN_ACTIVITY", "ACTIVITY_MODE_WALKING", "BUNDLE_KEY_ACTIVITY_RECOGNIZE_EVENT", "", "BUNDLE_KEY_ACTIVITY_TYPE", "BUNDLE_KEY_EVENT_TIME", "BUNDLE_KEY_TRANSITION_TYPE", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/ActivityRecognizeEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.ActivityRecognizeEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<ActivityRecognizeEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityRecognizeEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new ActivityRecognizeEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityRecognizeEvent[] newArray(int size) {
            return new ActivityRecognizeEvent[size];
        }
    }

    public ActivityRecognizeEvent(int i10, int i11, long j10) {
        this.activityType = i10;
        this.transitionType = i11;
        this.eventTimeMillis = j10;
    }

    public static /* synthetic */ ActivityRecognizeEvent copy$default(ActivityRecognizeEvent activityRecognizeEvent, int i10, int i11, long j10, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = activityRecognizeEvent.activityType;
        }
        if ((i12 & 2) != 0) {
            i11 = activityRecognizeEvent.transitionType;
        }
        if ((i12 & 4) != 0) {
            j10 = activityRecognizeEvent.eventTimeMillis;
        }
        return activityRecognizeEvent.copy(i10, i11, j10);
    }

    /* renamed from: component1, reason: from getter */
    public final int getActivityType() {
        return this.activityType;
    }

    /* renamed from: component2, reason: from getter */
    public final int getTransitionType() {
        return this.transitionType;
    }

    /* renamed from: component3, reason: from getter */
    public final long getEventTimeMillis() {
        return this.eventTimeMillis;
    }

    public final ActivityRecognizeEvent copy(int activityType, int transitionType, long eventTimeMillis) {
        return new ActivityRecognizeEvent(activityType, transitionType, eventTimeMillis);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ActivityRecognizeEvent)) {
            return false;
        }
        ActivityRecognizeEvent activityRecognizeEvent = (ActivityRecognizeEvent) other;
        return this.activityType == activityRecognizeEvent.activityType && this.transitionType == activityRecognizeEvent.transitionType && this.eventTimeMillis == activityRecognizeEvent.eventTimeMillis;
    }

    public final int getActivityType() {
        return this.activityType;
    }

    public final long getEventTimeMillis() {
        return this.eventTimeMillis;
    }

    public final int getTransitionType() {
        return this.transitionType;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.activityType) * 31) + Integer.hashCode(this.transitionType)) * 31) + Long.hashCode(this.eventTimeMillis);
    }

    public String toString() {
        return "ActivityRecognizeEvent(activityType=" + this.activityType + ", transitionType=" + this.transitionType + ", eventTimeMillis=" + this.eventTimeMillis + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeInt(this.activityType);
        parcel.writeInt(this.transitionType);
        parcel.writeLong(this.eventTimeMillis);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public ActivityRecognizeEvent(Parcel parcel) {
        this(parcel.readInt(), parcel.readInt(), parcel.readLong());
        k.e(parcel, "parcel");
    }
}
