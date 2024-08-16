package com.oplus.deepthinker.sdk.app.awareness.capability;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CapabilityEventCategory.kt */
@Metadata(bv = {}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0010\b\u0086\b\u0018\u0000 %2\u00020\u0001:\u0001%B\u0019\u0012\u0006\u0010\u0013\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0006¢\u0006\u0004\b\"\u0010#B\u0011\b\u0016\u0012\u0006\u0010\t\u001a\u00020\b¢\u0006\u0004\b\"\u0010$J\u0016\u0010\u0005\u001a\u00020\u0004*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002H\u0002J\f\u0010\u0007\u001a\u00020\u0006*\u00020\u0002H\u0002J\u0018\u0010\f\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u0006H\u0016J\b\u0010\r\u001a\u00020\u0006H\u0016J\u0013\u0010\u000f\u001a\u00020\u00042\b\u0010\u0003\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J\b\u0010\u0010\u001a\u00020\u0006H\u0016J\t\u0010\u0011\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0006HÆ\u0003J\u001d\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0013\u001a\u00020\u00062\b\b\u0002\u0010\u0014\u001a\u00020\u0006HÆ\u0001J\t\u0010\u0017\u001a\u00020\u0016HÖ\u0001R\u0017\u0010\u0013\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\u0013\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010\u0014\u001a\u00020\u00068\u0006¢\u0006\f\n\u0004\b\u0014\u0010\u0018\u001a\u0004\b\u001b\u0010\u001aR$\u0010\u001c\u001a\u0004\u0018\u00010\u00028\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!¨\u0006&"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEventCategory;", "Landroid/os/Parcelable;", "Landroid/os/Bundle;", "other", "", "checkEquals", "", "calHashCode", "Landroid/os/Parcel;", "parcel", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "equals", "hashCode", "component1", "component2", "eventId", "eventMode", "copy", "", "toString", "I", "getEventId", "()I", "getEventMode", "capabilityArgs", "Landroid/os/Bundle;", "getCapabilityArgs", "()Landroid/os/Bundle;", "setCapabilityArgs", "(Landroid/os/Bundle;)V", "<init>", "(II)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class CapabilityEventCategory implements Parcelable {
    public static final int CAPABILITY_EVENT_MODE_OPTIONAL = 1;
    public static final int CAPABILITY_EVENT_MODE_REQUIRED = 2;

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int HASH_NUM = 31;
    private Bundle capabilityArgs;
    private final int eventId;
    private final int eventMode;

    /* compiled from: CapabilityEventCategory.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\b\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\nH\u0016J\u001d\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\f2\u0006\u0010\r\u001a\u00020\u0005H\u0016¢\u0006\u0002\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEventCategory$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEventCategory;", "()V", "CAPABILITY_EVENT_MODE_OPTIONAL", "", "CAPABILITY_EVENT_MODE_REQUIRED", "HASH_NUM", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEventCategory;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEventCategory$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<CapabilityEventCategory> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CapabilityEventCategory createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new CapabilityEventCategory(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CapabilityEventCategory[] newArray(int size) {
            return new CapabilityEventCategory[size];
        }
    }

    public CapabilityEventCategory(int i10, int i11) {
        this.eventId = i10;
        this.eventMode = i11;
    }

    private final int calHashCode(Bundle bundle) {
        Iterator<String> it = bundle.keySet().iterator();
        int i10 = 1;
        while (it.hasNext()) {
            Object obj = bundle.get(it.next());
            i10 = (i10 * 31) + (obj == null ? 0 : obj.hashCode());
        }
        return i10;
    }

    private final boolean checkEquals(Bundle bundle, Bundle bundle2) {
        boolean z10;
        if (bundle2 == null || bundle.size() != bundle2.size()) {
            return false;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Set<String> keySet = bundle.keySet();
        k.d(keySet, "this.keySet()");
        linkedHashSet.addAll(keySet);
        Set<String> keySet2 = bundle2.keySet();
        k.d(keySet2, "other.keySet()");
        linkedHashSet.addAll(keySet2);
        Iterator it = linkedHashSet.iterator();
        do {
            z10 = true;
            if (!it.hasNext()) {
                return true;
            }
            String str = (String) it.next();
            if (!bundle.containsKey(str) || !bundle2.containsKey(str)) {
                break;
            }
            Object obj = bundle.get(str);
            Object obj2 = bundle2.get(str);
            if ((obj instanceof Bundle) && (obj2 instanceof Bundle) && !checkEquals((Bundle) obj, (Bundle) obj2)) {
                return false;
            }
            if (obj == null && obj2 != null) {
                return false;
            }
            if (obj == null || obj.equals(obj2)) {
                z10 = false;
            }
        } while (!z10);
        return false;
    }

    public static /* synthetic */ CapabilityEventCategory copy$default(CapabilityEventCategory capabilityEventCategory, int i10, int i11, int i12, Object obj) {
        if ((i12 & 1) != 0) {
            i10 = capabilityEventCategory.eventId;
        }
        if ((i12 & 2) != 0) {
            i11 = capabilityEventCategory.eventMode;
        }
        return capabilityEventCategory.copy(i10, i11);
    }

    /* renamed from: component1, reason: from getter */
    public final int getEventId() {
        return this.eventId;
    }

    /* renamed from: component2, reason: from getter */
    public final int getEventMode() {
        return this.eventMode;
    }

    public final CapabilityEventCategory copy(int eventId, int eventMode) {
        return new CapabilityEventCategory(eventId, eventMode);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!(other instanceof CapabilityEventCategory)) {
            return false;
        }
        CapabilityEventCategory capabilityEventCategory = (CapabilityEventCategory) other;
        if (capabilityEventCategory.eventId != this.eventId) {
            return false;
        }
        Bundle bundle = capabilityEventCategory.capabilityArgs;
        if (bundle != null || this.capabilityArgs != null) {
            if ((bundle == null || checkEquals(bundle, this.capabilityArgs)) ? false : true) {
                return false;
            }
        }
        return true;
    }

    public final Bundle getCapabilityArgs() {
        return this.capabilityArgs;
    }

    public final int getEventId() {
        return this.eventId;
    }

    public final int getEventMode() {
        return this.eventMode;
    }

    public int hashCode() {
        int hashCode = (Integer.hashCode(this.eventId) + 31) * 31;
        Bundle bundle = this.capabilityArgs;
        return hashCode + (bundle == null ? 0 : calHashCode(bundle));
    }

    public final void setCapabilityArgs(Bundle bundle) {
        this.capabilityArgs = bundle;
    }

    public String toString() {
        return "CapabilityEventCategory(eventId=" + this.eventId + ", eventMode=" + this.eventMode + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeInt(this.eventId);
        parcel.writeInt(this.eventMode);
        parcel.writeBundle(this.capabilityArgs);
    }

    public /* synthetic */ CapabilityEventCategory(int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(i10, (i12 & 2) != 0 ? 2 : i11);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public CapabilityEventCategory(Parcel parcel) {
        this(parcel.readInt(), parcel.readInt());
        k.e(parcel, "parcel");
        this.capabilityArgs = parcel.readBundle(CapabilityEventCategory.class.getClassLoader());
    }
}
