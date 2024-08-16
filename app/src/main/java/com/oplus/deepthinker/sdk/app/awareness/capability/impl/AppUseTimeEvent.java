package com.oplus.deepthinker.sdk.app.awareness.capability.impl;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: AppUseTimeEvent.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\b\u0086\b\u0018\u0000 \"2\u00020\u0001:\u0001\"B%\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\t\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000b¢\u0006\u0004\b\u001f\u0010 B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u001f\u0010!J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\u000b\u0010\n\u001a\u0004\u0018\u00010\tHÆ\u0003J\t\u0010\f\u001a\u00020\u000bHÆ\u0003J\t\u0010\r\u001a\u00020\u000bHÆ\u0003J)\u0010\u0011\u001a\u00020\u00002\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000bHÆ\u0001J\t\u0010\u0012\u001a\u00020\tHÖ\u0001J\t\u0010\u0013\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\u0017\u001a\u00020\u00162\b\u0010\u0015\u001a\u0004\u0018\u00010\u0014HÖ\u0003R\u0019\u0010\u000e\u001a\u0004\u0018\u00010\t8\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010\u000f\u001a\u00020\u000b8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u001b\u001a\u0004\b\u001c\u0010\u001dR\u0017\u0010\u0010\u001a\u00020\u000b8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u001b\u001a\u0004\b\u001e\u0010\u001d¨\u0006#"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AppUseTimeEvent;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "component1", "", "component2", "component3", "packageName", "startTime", "endTime", "copy", "toString", "hashCode", "", "other", "", "equals", "Ljava/lang/String;", "getPackageName", "()Ljava/lang/String;", "J", "getStartTime", "()J", "getEndTime", "<init>", "(Ljava/lang/String;JJ)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class AppUseTimeEvent implements Parcelable {
    public static final String BUNDLE_KEY_APP_USE_TIME_EVENT = "app_use_time_event";
    public static final String BUNDLE_KEY_END_TIME = "end_time";
    public static final String BUNDLE_KEY_PACKAGE_NAME = "package_name";
    public static final String BUNDLE_KEY_START_TIME = "start_time";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final long endTime;
    private final String packageName;
    private final long startTime;

    /* compiled from: AppUseTimeEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\t\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u001d\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016¢\u0006\u0002\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AppUseTimeEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AppUseTimeEvent;", "()V", "BUNDLE_KEY_APP_USE_TIME_EVENT", "", "BUNDLE_KEY_END_TIME", "BUNDLE_KEY_PACKAGE_NAME", "BUNDLE_KEY_START_TIME", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/impl/AppUseTimeEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.impl.AppUseTimeEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<AppUseTimeEvent> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppUseTimeEvent createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new AppUseTimeEvent(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AppUseTimeEvent[] newArray(int size) {
            return new AppUseTimeEvent[size];
        }
    }

    public AppUseTimeEvent(String str, long j10, long j11) {
        this.packageName = str;
        this.startTime = j10;
        this.endTime = j11;
    }

    public static /* synthetic */ AppUseTimeEvent copy$default(AppUseTimeEvent appUseTimeEvent, String str, long j10, long j11, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            str = appUseTimeEvent.packageName;
        }
        if ((i10 & 2) != 0) {
            j10 = appUseTimeEvent.startTime;
        }
        long j12 = j10;
        if ((i10 & 4) != 0) {
            j11 = appUseTimeEvent.endTime;
        }
        return appUseTimeEvent.copy(str, j12, j11);
    }

    /* renamed from: component1, reason: from getter */
    public final String getPackageName() {
        return this.packageName;
    }

    /* renamed from: component2, reason: from getter */
    public final long getStartTime() {
        return this.startTime;
    }

    /* renamed from: component3, reason: from getter */
    public final long getEndTime() {
        return this.endTime;
    }

    public final AppUseTimeEvent copy(String packageName, long startTime, long endTime) {
        return new AppUseTimeEvent(packageName, startTime, endTime);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AppUseTimeEvent)) {
            return false;
        }
        AppUseTimeEvent appUseTimeEvent = (AppUseTimeEvent) other;
        return k.a(this.packageName, appUseTimeEvent.packageName) && this.startTime == appUseTimeEvent.startTime && this.endTime == appUseTimeEvent.endTime;
    }

    public final long getEndTime() {
        return this.endTime;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final long getStartTime() {
        return this.startTime;
    }

    public int hashCode() {
        String str = this.packageName;
        return ((((str == null ? 0 : str.hashCode()) * 31) + Long.hashCode(this.startTime)) * 31) + Long.hashCode(this.endTime);
    }

    public String toString() {
        return "AppUseTimeEvent(packageName=" + ((Object) this.packageName) + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeString(this.packageName);
        parcel.writeLong(this.startTime);
        parcel.writeLong(this.endTime);
    }

    public /* synthetic */ AppUseTimeEvent(String str, long j10, long j11, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i10 & 2) != 0 ? 0L : j10, (i10 & 4) != 0 ? 0L : j11);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AppUseTimeEvent(Parcel parcel) {
        this(parcel.readString(), parcel.readLong(), parcel.readLong());
        k.e(parcel, "parcel");
    }
}
