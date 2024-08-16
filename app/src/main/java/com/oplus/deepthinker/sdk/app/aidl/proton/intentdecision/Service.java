package com.oplus.deepthinker.sdk.app.aidl.proton.intentdecision;

import com.google.gson.annotations.SerializedName;
import com.oplus.sceneservice.sdk.dataprovider.bean.UserProfileInfo;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: Service.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B3\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J7\u0010\u0013\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001R\u0018\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0016\u0010\u0006\u001a\u00020\u00078\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n¨\u0006\u001a"}, d2 = {"Lcom/oplus/deepthinker/sdk/app/aidl/proton/intentdecision/Service;", "", "serviceId", "", "serviceName", "serviceType", "serviceScore", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;D)V", "getServiceId", "()Ljava/lang/String;", "getServiceName", "getServiceScore", "()D", "getServiceType", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "com.oplus.deepthinker.sdk_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final /* data */ class Service {

    @SerializedName("service_id")
    private final String serviceId;

    @SerializedName("service_name")
    private final String serviceName;

    @SerializedName("service_score")
    private final double serviceScore;

    @SerializedName("service_type")
    private final String serviceType;

    public Service() {
        this(null, null, null, UserProfileInfo.Constant.NA_LAT_LON, 15, null);
    }

    public Service(String str, String str2, String str3, double d10) {
        this.serviceId = str;
        this.serviceName = str2;
        this.serviceType = str3;
        this.serviceScore = d10;
    }

    public static /* synthetic */ Service copy$default(Service service, String str, String str2, String str3, double d10, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            str = service.serviceId;
        }
        if ((i10 & 2) != 0) {
            str2 = service.serviceName;
        }
        String str4 = str2;
        if ((i10 & 4) != 0) {
            str3 = service.serviceType;
        }
        String str5 = str3;
        if ((i10 & 8) != 0) {
            d10 = service.serviceScore;
        }
        return service.copy(str, str4, str5, d10);
    }

    /* renamed from: component1, reason: from getter */
    public final String getServiceId() {
        return this.serviceId;
    }

    /* renamed from: component2, reason: from getter */
    public final String getServiceName() {
        return this.serviceName;
    }

    /* renamed from: component3, reason: from getter */
    public final String getServiceType() {
        return this.serviceType;
    }

    /* renamed from: component4, reason: from getter */
    public final double getServiceScore() {
        return this.serviceScore;
    }

    public final Service copy(String serviceId, String serviceName, String serviceType, double serviceScore) {
        return new Service(serviceId, serviceName, serviceType, serviceScore);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Service)) {
            return false;
        }
        Service service = (Service) other;
        return k.a(this.serviceId, service.serviceId) && k.a(this.serviceName, service.serviceName) && k.a(this.serviceType, service.serviceType) && k.a(Double.valueOf(this.serviceScore), Double.valueOf(service.serviceScore));
    }

    public final String getServiceId() {
        return this.serviceId;
    }

    public final String getServiceName() {
        return this.serviceName;
    }

    public final double getServiceScore() {
        return this.serviceScore;
    }

    public final String getServiceType() {
        return this.serviceType;
    }

    public int hashCode() {
        String str = this.serviceId;
        int hashCode = (str == null ? 0 : str.hashCode()) * 31;
        String str2 = this.serviceName;
        int hashCode2 = (hashCode + (str2 == null ? 0 : str2.hashCode())) * 31;
        String str3 = this.serviceType;
        return ((hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31) + Double.hashCode(this.serviceScore);
    }

    public String toString() {
        return "Service(serviceId=" + ((Object) this.serviceId) + ", serviceName=" + ((Object) this.serviceName) + ", serviceType=" + ((Object) this.serviceType) + ", serviceScore=" + this.serviceScore + ')';
    }

    public /* synthetic */ Service(String str, String str2, String str3, double d10, int i10, DefaultConstructorMarker defaultConstructorMarker) {
        this((i10 & 1) != 0 ? null : str, (i10 & 2) != 0 ? null : str2, (i10 & 4) != 0 ? null : str3, (i10 & 8) != 0 ? UserProfileInfo.Constant.NA_LAT_LON : d10);
    }
}
