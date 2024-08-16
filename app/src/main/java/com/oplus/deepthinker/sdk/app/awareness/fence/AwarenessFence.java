package com.oplus.deepthinker.sdk.app.awareness.fence;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import ma.Unit;
import ya.l;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: AwarenessFence.kt */
@Metadata(bv = {}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b#\b\u0086\b\u0018\u0000 <2\u00020\u0001:\u0002=<B?\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\t\u0012\b\b\u0002\u0010\u0013\u001a\u00020\f\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u000e\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0004¢\u0006\u0004\b9\u0010:B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b9\u0010;J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\u000b\u0010\n\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\tHÆ\u0003J\t\u0010\r\u001a\u00020\fHÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u000eHÆ\u0003J\t\u0010\u0010\u001a\u00020\u0004HÆ\u0003JA\u0010\u0016\u001a\u00020\u00002\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u0013\u001a\u00020\f2\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u0004HÆ\u0001J\t\u0010\u0017\u001a\u00020\tHÖ\u0001J\t\u0010\u0018\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\u001c\u001a\u00020\u001b2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0019HÖ\u0003R$\u0010\u0011\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0011\u0010\u001d\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R$\u0010\u0012\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0012\u0010\u001d\u001a\u0004\b\"\u0010\u001f\"\u0004\b#\u0010!R\"\u0010\u0013\u001a\u00020\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0013\u0010$\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R$\u0010\u0014\u001a\u0004\u0018\u00010\u000e8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010)\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\"\u0010\u0015\u001a\u00020\u00048\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010.\u001a\u0004\b/\u00100\"\u0004\b1\u00102R$\u00103\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b3\u0010\u001d\u001a\u0004\b4\u0010\u001f\"\u0004\b5\u0010!R$\u00106\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b6\u0010\u001d\u001a\u0004\b7\u0010\u001f\"\u0004\b8\u0010!¨\u0006>"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "component1", "component2", "", "component3", "Landroid/os/Bundle;", "component4", "component5", "fenceName", "fenceType", "fenceDuration", "fenceArgs", "fenceCategory", "copy", "toString", "hashCode", "", "other", "", "equals", "Ljava/lang/String;", "getFenceName", "()Ljava/lang/String;", "setFenceName", "(Ljava/lang/String;)V", "getFenceType", "setFenceType", "J", "getFenceDuration", "()J", "setFenceDuration", "(J)V", "Landroid/os/Bundle;", "getFenceArgs", "()Landroid/os/Bundle;", "setFenceArgs", "(Landroid/os/Bundle;)V", "I", "getFenceCategory", "()I", "setFenceCategory", "(I)V", "packageName", "getPackageName", "setPackageName", "fenceId", "getFenceId", "setFenceId", "<init>", "(Ljava/lang/String;Ljava/lang/String;JLandroid/os/Bundle;I)V", "(Landroid/os/Parcel;)V", "CREATOR", "Builder", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final /* data */ class AwarenessFence implements Parcelable {

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private Bundle fenceArgs;
    private int fenceCategory;
    private long fenceDuration;
    private String fenceId;
    private String fenceName;
    private String fenceType;
    private String packageName;

    /* compiled from: AwarenessFence.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\f\u001a\u00020\rJ\u0010\u0010\u000e\u001a\u00020\u00002\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u000f\u001a\u00020\u00002\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0010\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u0011\u001a\u00020\u00002\b\u0010\t\u001a\u0004\u0018\u00010\nJ\u0010\u0010\u0012\u001a\u00020\u00002\b\u0010\u000b\u001a\u0004\u0018\u00010\nR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence$Builder;", "", "()V", "fenceArgs", "Landroid/os/Bundle;", "fenceCategory", "", "fenceDuration", "", "fenceName", "", "fenceType", "build", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "setFenceArgs", "setFenceCategory", "setFenceDuration", "setFenceName", "setFenceType", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Builder {
        private Bundle fenceArgs;
        private int fenceCategory = 1;
        private long fenceDuration;
        private String fenceName;
        private String fenceType;

        public final AwarenessFence build() {
            return new AwarenessFence(this.fenceName, this.fenceType, this.fenceDuration, this.fenceArgs, this.fenceCategory);
        }

        public final Builder setFenceArgs(Bundle fenceArgs) {
            this.fenceArgs = fenceArgs;
            return this;
        }

        public final Builder setFenceCategory(int fenceCategory) {
            this.fenceCategory = fenceCategory;
            return this;
        }

        public final Builder setFenceDuration(long fenceDuration) {
            this.fenceDuration = fenceDuration;
            return this;
        }

        public final Builder setFenceName(String fenceName) {
            this.fenceName = fenceName;
            return this;
        }

        public final Builder setFenceType(String fenceType) {
            this.fenceType = fenceType;
            return this;
        }
    }

    /* compiled from: AwarenessFence.kt */
    @Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0010\u0010\u0011J \u0010\u0007\u001a\u00020\u00022\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003H\u0087\bø\u0001\u0000J\u0010\u0010\n\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bH\u0016J\u001f\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\r2\u0006\u0010\f\u001a\u00020\u000bH\u0016¢\u0006\u0004\b\u000e\u0010\u000f\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0012"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "Lkotlin/Function1;", "Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence$Builder;", "Lma/f0;", "block", "build", "Landroid/os/Parcel;", "parcel", "createFromParcel", "", "size", "", "newArray", "(I)[Lcom/oplus/deepthinker/sdk/app/awareness/fence/AwarenessFence;", "<init>", "()V", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
    /* renamed from: com.oplus.deepthinker.sdk.app.awareness.fence.AwarenessFence$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<AwarenessFence> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final AwarenessFence build(l<? super Builder, Unit> lVar) {
            k.e(lVar, "block");
            Builder builder = new Builder();
            lVar.invoke(builder);
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AwarenessFence createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new AwarenessFence(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AwarenessFence[] newArray(int size) {
            return new AwarenessFence[size];
        }
    }

    public AwarenessFence() {
        this(null, null, 0L, null, 0, 31, null);
    }

    public AwarenessFence(String str, String str2, long j10, Bundle bundle, int i10) {
        this.fenceName = str;
        this.fenceType = str2;
        this.fenceDuration = j10;
        this.fenceArgs = bundle;
        this.fenceCategory = i10;
    }

    public static final AwarenessFence build(l<? super Builder, Unit> lVar) {
        return INSTANCE.build(lVar);
    }

    public static /* synthetic */ AwarenessFence copy$default(AwarenessFence awarenessFence, String str, String str2, long j10, Bundle bundle, int i10, int i11, Object obj) {
        if ((i11 & 1) != 0) {
            str = awarenessFence.fenceName;
        }
        if ((i11 & 2) != 0) {
            str2 = awarenessFence.fenceType;
        }
        String str3 = str2;
        if ((i11 & 4) != 0) {
            j10 = awarenessFence.fenceDuration;
        }
        long j11 = j10;
        if ((i11 & 8) != 0) {
            bundle = awarenessFence.fenceArgs;
        }
        Bundle bundle2 = bundle;
        if ((i11 & 16) != 0) {
            i10 = awarenessFence.fenceCategory;
        }
        return awarenessFence.copy(str, str3, j11, bundle2, i10);
    }

    /* renamed from: component1, reason: from getter */
    public final String getFenceName() {
        return this.fenceName;
    }

    /* renamed from: component2, reason: from getter */
    public final String getFenceType() {
        return this.fenceType;
    }

    /* renamed from: component3, reason: from getter */
    public final long getFenceDuration() {
        return this.fenceDuration;
    }

    /* renamed from: component4, reason: from getter */
    public final Bundle getFenceArgs() {
        return this.fenceArgs;
    }

    /* renamed from: component5, reason: from getter */
    public final int getFenceCategory() {
        return this.fenceCategory;
    }

    public final AwarenessFence copy(String fenceName, String fenceType, long fenceDuration, Bundle fenceArgs, int fenceCategory) {
        return new AwarenessFence(fenceName, fenceType, fenceDuration, fenceArgs, fenceCategory);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AwarenessFence)) {
            return false;
        }
        AwarenessFence awarenessFence = (AwarenessFence) other;
        return k.a(this.fenceName, awarenessFence.fenceName) && k.a(this.fenceType, awarenessFence.fenceType) && this.fenceDuration == awarenessFence.fenceDuration && k.a(this.fenceArgs, awarenessFence.fenceArgs) && this.fenceCategory == awarenessFence.fenceCategory;
    }

    public final Bundle getFenceArgs() {
        return this.fenceArgs;
    }

    public final int getFenceCategory() {
        return this.fenceCategory;
    }

    public final long getFenceDuration() {
        return this.fenceDuration;
    }

    public final String getFenceId() {
        return this.fenceId;
    }

    public final String getFenceName() {
        return this.fenceName;
    }

    public final String getFenceType() {
        return this.fenceType;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public int hashCode() {
        String str = this.fenceName;
        int hashCode = (str == null ? 0 : str.hashCode()) * 31;
        String str2 = this.fenceType;
        int hashCode2 = (((hashCode + (str2 == null ? 0 : str2.hashCode())) * 31) + Long.hashCode(this.fenceDuration)) * 31;
        Bundle bundle = this.fenceArgs;
        return ((hashCode2 + (bundle != null ? bundle.hashCode() : 0)) * 31) + Integer.hashCode(this.fenceCategory);
    }

    public final void setFenceArgs(Bundle bundle) {
        this.fenceArgs = bundle;
    }

    public final void setFenceCategory(int i10) {
        this.fenceCategory = i10;
    }

    public final void setFenceDuration(long j10) {
        this.fenceDuration = j10;
    }

    public final void setFenceId(String str) {
        this.fenceId = str;
    }

    public final void setFenceName(String str) {
        this.fenceName = str;
    }

    public final void setFenceType(String str) {
        this.fenceType = str;
    }

    public final void setPackageName(String str) {
        this.packageName = str;
    }

    public String toString() {
        return "AwarenessFence(fenceName=" + ((Object) this.fenceName) + ", fenceType=" + ((Object) this.fenceType) + ", fenceDuration=" + this.fenceDuration + ", fenceArgs=" + this.fenceArgs + ", fenceCategory=" + this.fenceCategory + ')';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeString(this.fenceName);
        parcel.writeString(this.fenceType);
        parcel.writeLong(this.fenceDuration);
        parcel.writeBundle(this.fenceArgs);
        parcel.writeInt(this.fenceCategory);
        parcel.writeString(this.packageName);
        parcel.writeString(this.fenceId);
    }

    public /* synthetic */ AwarenessFence(String str, String str2, long j10, Bundle bundle, int i10, int i11, DefaultConstructorMarker defaultConstructorMarker) {
        this((i11 & 1) != 0 ? null : str, (i11 & 2) != 0 ? null : str2, (i11 & 4) != 0 ? 0L : j10, (i11 & 8) != 0 ? null : bundle, (i11 & 16) != 0 ? 1 : i10);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public AwarenessFence(Parcel parcel) {
        this(null, null, 0L, null, 0, 31, null);
        k.e(parcel, "parcel");
        this.fenceName = parcel.readString();
        this.fenceType = parcel.readString();
        this.fenceDuration = parcel.readLong();
        this.fenceArgs = parcel.readBundle(AwarenessFence.class.getClassLoader());
        this.fenceCategory = parcel.readInt();
        this.packageName = parcel.readString();
        this.fenceId = parcel.readString();
    }
}
