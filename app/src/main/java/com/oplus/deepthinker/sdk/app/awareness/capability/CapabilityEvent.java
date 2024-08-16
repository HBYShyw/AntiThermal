package com.oplus.deepthinker.sdk.app.awareness.capability;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CapabilityEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0016\u0018\u0000 \u0019*\b\b\u0000\u0010\u0002*\u00020\u00012\b\u0012\u0004\u0012\u00028\u00000\u00032\u00020\u0001:\u0001\u0019B'\b\u0016\u0012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u000f\u0012\u0006\u0010\u0012\u001a\u00028\u0000\u0012\b\b\u0002\u0010\u0014\u001a\u00020\n¢\u0006\u0004\b\u0016\u0010\u0017B\u0011\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0004\b\u0016\u0010\u0018J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u000f\u0010\b\u001a\u00028\u0000H\u0016¢\u0006\u0004\b\b\u0010\tJ\b\u0010\u000b\u001a\u00020\nH\u0016J\b\u0010\f\u001a\u00020\nH\u0016J\u0018\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\nH\u0016R\u001a\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u000f8\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0012\u001a\u00028\u00008\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b\u0012\u0010\u0013R\u0016\u0010\u0014\u001a\u00020\n8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0014\u0010\u0015¨\u0006\u001a"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEvent;", "Landroid/os/Parcelable;", "T", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/ICapabilityEvent;", "Landroid/os/Parcel;", "parcel", "Lma/f0;", "readFromParcel", "getCapabilityEvent", "()Landroid/os/Parcelable;", "", "getCapabilityEventId", "describeContents", "flags", "writeToParcel", "Ljava/lang/Class;", "classType", "Ljava/lang/Class;", "event", "Landroid/os/Parcelable;", "eventId", "I", "<init>", "(Ljava/lang/Class;Landroid/os/Parcelable;I)V", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public class CapabilityEvent<T extends Parcelable> implements ICapabilityEvent<T>, Parcelable {
    public static final String BUNDLE_KEY_CAPABILITY_EVENT = "capability_event";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private Class<?> classType;
    private T event;
    private int eventId;

    /* compiled from: CapabilityEvent.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0014\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u00022\u0006\u0010\u0007\u001a\u00020\bH\u0016J!\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016¢\u0006\u0002\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEvent$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEvent;", "()V", "BUNDLE_KEY_CAPABILITY_EVENT", "", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/capability/CapabilityEvent;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEvent$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<CapabilityEvent<?>> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CapabilityEvent<?> createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new CapabilityEvent<>(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CapabilityEvent<?>[] newArray(int size) {
            return new CapabilityEvent[size];
        }
    }

    public CapabilityEvent(Class<?> cls, T t7, int i10) {
        k.e(cls, "classType");
        k.e(t7, "event");
        this.classType = cls;
        this.event = t7;
        this.eventId = i10;
    }

    private final void readFromParcel(Parcel parcel) {
        Object readValue = parcel.readValue(getClass().getClassLoader());
        Objects.requireNonNull(readValue, "null cannot be cast to non-null type java.lang.Class<*>");
        Class<?> cls = (Class) readValue;
        this.classType = cls;
        Object readValue2 = parcel.readValue(cls.getClassLoader());
        Objects.requireNonNull(readValue2, "null cannot be cast to non-null type T of com.oplus.deepthinker.sdk.app.awareness.capability.CapabilityEvent");
        this.event = (T) readValue2;
        this.eventId = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.oplus.deepthinker.sdk.app.awareness.capability.ICapabilityEvent
    /* renamed from: getCapabilityEventId, reason: from getter */
    public int getEventId() {
        return this.eventId;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        Class<?> cls = this.classType;
        T t7 = null;
        if (cls == null) {
            k.s("classType");
            cls = null;
        }
        parcel.writeValue(cls);
        T t10 = this.event;
        if (t10 == null) {
            k.s("event");
        } else {
            t7 = t10;
        }
        parcel.writeValue(t7);
        parcel.writeInt(this.eventId);
    }

    @Override // com.oplus.deepthinker.sdk.app.awareness.capability.ICapabilityEvent
    public T getCapabilityEvent() {
        T t7 = this.event;
        if (t7 != null) {
            return t7;
        }
        k.s("event");
        return null;
    }

    public /* synthetic */ CapabilityEvent(Class cls, Parcelable parcelable, int i10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this(cls, parcelable, (i11 & 4) != 0 ? -1 : i10);
    }

    public CapabilityEvent(Parcel parcel) {
        k.e(parcel, "parcel");
        this.eventId = -1;
        readFromParcel(parcel);
    }
}
