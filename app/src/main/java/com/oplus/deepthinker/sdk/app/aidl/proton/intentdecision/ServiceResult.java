package com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.reflect.TypeToken;
import com.oplus.deepthinker.sdk.app.deepthinkermanager.domainmanager.DeviceDomainManager;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import m6.a;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: ServiceResult.kt */
@Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0004\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\u0017\b\u0016\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u001a¢\u0006\u0004\b\u001b\u0010\u0012B\u0011\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0002¢\u0006\u0004\b\u001b\u0010\u001cJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016J\b\u0010\n\u001a\u00020\tH\u0016R(\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000b8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R$\u0010\u0014\u001a\u0004\u0018\u00010\u00138\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019¨\u0006\u001e"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/ServiceResult;", "Landroid/os/Parcelable;", "Landroid/os/Parcel;", "parcel", "", "flags", "Lma/f0;", "writeToParcel", "describeContents", "", "toString", "", "Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/Service;", "services", "Ljava/util/List;", "getServices", "()Ljava/util/List;", "setServices", "(Ljava/util/List;)V", "Landroid/os/Bundle;", DeviceDomainManager.ARG_EXTRA, "Landroid/os/Bundle;", "getExtra", "()Landroid/os/Bundle;", "setExtra", "(Landroid/os/Bundle;)V", "", "<init>", "(Landroid/os/Parcel;)V", "CREATOR", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes.dex */
public final class ServiceResult implements Parcelable {
    public static final String BUNDLE_KEY_SERVICE_RESULT = "service_result";
    public static final String BUNDLE_KEY_TOP_K_SERVICE = "top_k_service";

    /* renamed from: CREATOR, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private Bundle extra;
    private List<Service> services;

    /* compiled from: ServiceResult.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0016J\u001d\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016¢\u0006\u0002\u0010\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/ServiceResult$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/ServiceResult;", "()V", "BUNDLE_KEY_SERVICE_RESULT", "", "BUNDLE_KEY_TOP_K_SERVICE", "createFromParcel", "parcel", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/ServiceResult;", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* renamed from: com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult$CREATOR, reason: from kotlin metadata */
    /* loaded from: classes.dex */
    public static final class Companion implements Parcelable.Creator<ServiceResult> {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ServiceResult createFromParcel(Parcel parcel) {
            k.e(parcel, "parcel");
            return new ServiceResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ServiceResult[] newArray(int size) {
            return new ServiceResult[size];
        }
    }

    public ServiceResult(List<Service> list) {
        k.e(list, "services");
        this.services = new ArrayList();
        if (!list.isEmpty()) {
            this.services.addAll(list);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final Bundle getExtra() {
        return this.extra;
    }

    public final List<Service> getServices() {
        return this.services;
    }

    public final void setExtra(Bundle bundle) {
        this.extra = bundle;
    }

    public final void setServices(List<Service> list) {
        k.e(list, "<set-?>");
        this.services = list;
    }

    public String toString() {
        return "ServiceResult{services = " + this.services + ", extra = " + this.extra + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i10) {
        k.e(parcel, "parcel");
        parcel.writeString(a.b(this.services));
        parcel.writeBundle(this.extra);
    }

    public ServiceResult(Parcel parcel) {
        k.e(parcel, "parcel");
        this.services = new ArrayList();
        String readString = parcel.readString();
        this.extra = parcel.readBundle(ServiceResult.class.getClassLoader());
        Type type = new TypeToken<List<? extends Service>>() { // from class: com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision.ServiceResult$special$$inlined$genericType$1
        }.getType();
        k.d(type, "object : TypeToken<T>() {}.type");
        List list = (List) a.a(readString, type);
        if (list == null) {
            return;
        }
        getServices().addAll(list);
    }
}
