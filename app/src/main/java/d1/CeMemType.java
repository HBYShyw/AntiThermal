package d1;

import com.oplus.deepthinker.sdk.app.DataLinkConstants;

/* compiled from: CeMemType.java */
/* renamed from: d1.a, reason: use source file name */
/* loaded from: classes.dex */
public enum CeMemType {
    PKI_CAL_TYPE_T(DataLinkConstants.THUMBNAIL_HEALTH),
    PKI_CERT_TYPE_T(DataLinkConstants.THUMBNAIL_CALENDAR),
    PKI_RSP_TYPE_T(DataLinkConstants.THUMBNAIL_LOCATION),
    PKI_ROOT_CA_CERT_TYPE_T(DataLinkConstants.THUMBNAIL_DEVICE_USAGE),
    PKI_DEVICE_CA_CERT_TYPE_T(DataLinkConstants.THUMBNAIL_COMMUNITE),
    PKI_SERVICE_CA_CERT_TYPE_T(DataLinkConstants.THUMBNAIL_WEATHER),
    PKI_DEVICE_EE_CERT_TYPE_T(DataLinkConstants.THUMBNAIL_LIVING_INDEX);


    /* renamed from: e, reason: collision with root package name */
    private final int f10674e;

    CeMemType(int i10) {
        this.f10674e = i10;
    }

    public static CeMemType a(int i10) {
        for (CeMemType ceMemType : values()) {
            if (ceMemType.b() == i10) {
                return ceMemType;
            }
        }
        return null;
    }

    public int b() {
        return this.f10674e;
    }
}
