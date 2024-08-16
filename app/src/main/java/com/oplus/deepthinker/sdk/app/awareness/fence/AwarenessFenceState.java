package com.oplus.deepthinker.sdk.app.awareness.fence;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: AwarenessFenceState.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001e\b\u0086\b\u0018\u0000 :2\u00020\u0001:\u0001:BG\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\t\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u0015\u001a\u00020\r\u0012\b\b\u0002\u0010\u0016\u001a\u00020\r\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0010¢\u0006\u0004\b7\u00108B\u0011\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b7\u00109J\b\u0010\u0003\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00072\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0002H\u0016J\u000b\u0010\n\u001a\u0004\u0018\u00010\tHÆ\u0003J\t\u0010\u000b\u001a\u00020\u0002HÆ\u0003J\t\u0010\f\u001a\u00020\u0002HÆ\u0003J\t\u0010\u000e\u001a\u00020\rHÆ\u0003J\t\u0010\u000f\u001a\u00020\rHÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0010HÆ\u0003JI\u0010\u0018\u001a\u00020\u00002\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u0013\u001a\u00020\u00022\b\b\u0002\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\r2\b\b\u0002\u0010\u0016\u001a\u00020\r2\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0010HÆ\u0001J\t\u0010\u0019\u001a\u00020\tHÖ\u0001J\t\u0010\u001a\u001a\u00020\u0002HÖ\u0001J\u0013\u0010\u001e\u001a\u00020\u001d2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001bHÖ\u0003R$\u0010\u0012\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010\u001f\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\"\u0010\u0013\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0013\u0010$\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\"\u0010\u0014\u001a\u00020\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010$\u001a\u0004\b)\u0010&\"\u0004\b*\u0010(R\"\u0010\u0015\u001a\u00020\r8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010+\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\"\u0010\u0016\u001a\u00020\r8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0016\u0010+\u001a\u0004\b0\u0010-\"\u0004\b1\u0010/R$\u0010\u0017\u001a\u0004\u0018\u00010\u00108\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0017\u00102\u001a\u0004\b3\u00104\"\u0004\b5\u00106¨\u0006;"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceState;", "Landroid/os/Parcelable;", "", "describeContents", "Landroid/os/Parcel;", "parcel", "flags", "Lma/f0;", "writeToParcel", "", "component1", "component2", "component3", "", "component4", "component5", "Landroid/os/Bundle;", "component6", "fenceName", "currentState", "lastState", "currentUpdateTime", "lastUpdateTime", DeviceDomainManager.ARG_EXTRA, "copy", "toString", "hashCode", "", "other", "", "equals", "Ljava/lang/String;", "getFenceName", "()Ljava/lang/String;", "setFenceName", "(Ljava/lang/String;)V", "I", "getCurrentState", "()I", "setCurrentState", "(I)V", "getLastState", "setLastState", "J", "getCurrentUpdateTime", "()J", "setCurrentUpdateTime", "(J)V", "getLastUpdateTime", "setLastUpdateTime", "Landroid/os/Bundle;", "getExtra", "()Landroid/os/Bundle;", "setExtra", "(Landroid/os/Bundle;)V", "<init>", "(Ljava/lang/String;IIJJLandroid/os/Bundle;)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class AwarenessFenceState implements Parcelable {
    public static final String BUNDLE_KEY_FENCE_STATE = "awareness_fence_state";
    public static final String BUNDLE_KEY_TRIGGERED_STATE = "fence_trigger_state";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int FALSE = 1;
    public static final int TRUE = 0;
    public static final int UNKNOWN = -1;
    private int currentState;
    private long currentUpdateTime;
    private Bundle extra;
    private String fenceName;
    private int lastState;
    private long lastUpdateTime;

    /* compiled from: AwarenessFenceState.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u000b\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0016J\u001d\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016¢\u0006\u0002\u0010\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceState$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceState;", "()V", "BUNDLE_KEY_FENCE_STATE", "", "BUNDLE_KEY_TRIGGERED_STATE", "FALSE", "", "TRUE", "UNKNOWN", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFenceState;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFenceState$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<AwarenessFenceState> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AwarenessFenceState createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new AwarenessFenceState(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AwarenessFenceState[] newArray(int size) {
            return new AwarenessFenceState[size];
        }
    }

    public AwarenessFenceState() {
        this(null, 0, 0, 0L, 0L, null, 63, null);
    }

    public AwarenessFenceState(String str, int i10, int i11, long j10, long j11, Bundle bundle) {
        this.fenceName = str;
        this.currentState = i10;
        this.lastState = i11;
        this.currentUpdateTime = j10;
        this.lastUpdateTime = j11;
        this.extra = bundle;
    }

    /* renamed from: component1, reason: from getter */
    public final String getFenceName() {
        return this.fenceName;
    }

    /* renamed from: component2, reason: from getter */
    public final int getCurrentState() {
        return this.currentState;
    }

    /* renamed from: component3, reason: from getter */
    public final int getLastState() {
        return this.lastState;
    }

    /* renamed from: component4, reason: from getter */
    public final long getCurrentUpdateTime() {
        return this.currentUpdateTime;
    }

    /* renamed from: component5, reason: from getter */
    public final long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    /* renamed from: component6, reason: from getter */
    public final Bundle getExtra() {
        return this.extra;
    }

    public final AwarenessFenceState copy(String fenceName, int currentState, int lastState, long currentUpdateTime, long lastUpdateTime, Bundle extra) {
        return new AwarenessFenceState(fenceName, currentState, lastState, currentUpdateTime, lastUpdateTime, extra);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AwarenessFenceState)) {
            return false;
        }
        AwarenessFenceState awarenessFenceState = (AwarenessFenceState) other;
        return k.a(this.fenceName, awarenessFenceState.fenceName) && this.currentState == awarenessFenceState.currentState && this.lastState == awarenessFenceState.lastState && this.currentUpdateTime == awarenessFenceState.currentUpdateTime && this.lastUpdateTime == awarenessFenceState.lastUpdateTime && k.a(this.extra, awarenessFenceState.extra);
    }

    public final int getCurrentState() {
        return this.currentState;
    }

    public final long getCurrentUpdateTime() {
        return this.currentUpdateTime;
    }

    public final Bundle getExtra() {
        return this.extra;
    }

    public final String getFenceName() {
        return this.fenceName;
    }

    public final int getLastState() {
        return this.lastState;
    }

    public final long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public int hashCode() {
        String str = this.fenceName;
        int hashCode = (((((((((str == null ? 0 : str.hashCode()) * 31) + Integer.hashCode(this.currentState)) * 31) + Integer.hashCode(this.lastState)) * 31) + Long.hashCode(this.currentUpdateTime)) * 31) + Long.hashCode(this.lastUpdateTime)) * 31;
        Bundle bundle = this.extra;
        return hashCode + (bundle != null ? bundle.hashCode() : 0);
    }

    public final void setCurrentState(int i10) {
        this.currentState = i10;
    }

    public final void setCurrentUpdateTime(long j10) {
        this.currentUpdateTime = j10;
    }

    public final void setExtra(Bundle bundle) {
        this.extra = bundle;
    }

    public final void setFenceName(String str) {
        this.fenceName = str;
    }

    public final void setLastState(int i10) {
        this.lastState = i10;
    }

    public final void setLastUpdateTime(long j10) {
        this.lastUpdateTime = j10;
    }

    public String toString() {
        return "AwarenessFenceState(fenceName=" + ((Object) this.fenceName) + ", currentState=" + this.currentState + ", lastState=" + this.lastState + ", currentUpdateTime=" + this.currentUpdateTime + ", lastUpdateTime=" + this.lastUpdateTime + ", extra=" + this.extra + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeString(this.fenceName);
        parcel.writeInt(this.currentState);
        parcel.writeInt(this.lastState);
        parcel.writeLong(this.currentUpdateTime);
        parcel.writeLong(this.lastUpdateTime);
        parcel.writeBundle(this.extra);
    }

    public /* synthetic */ AwarenessFenceState(String str, int i10, int i11, long j10, long j11, Bundle bundle, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this((i12 & 1) != 0 ? null : str, (i12 & 2) != 0 ? -1 : i10, (i12 & 4) != 0 ? -1 : i11, (i12 & 8) != 0 ? 0L : j10, (i12 & 16) != 0 ? 0L : j11, (i12 & 32) != 0 ? null : bundle);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AwarenessFenceState(Parcel parcel) {
        this(null, 0, 0, 0L, 0L, null, 63, null);
        k.e(parcel, "parcel");
        this.fenceName = parcel.readString();
        this.currentState = parcel.readInt();
        this.lastState = parcel.readInt();
        this.currentUpdateTime = parcel.readLong();
        this.lastUpdateTime = parcel.readLong();
        this.extra = parcel.readBundle(AwarenessFenceState.class.getClassLoader());
    }
}
