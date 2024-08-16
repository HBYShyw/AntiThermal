package d1;

import com.oplus.deepthinker.sdk.app.DataLinkConstants;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;

/* compiled from: CryptoEngCmdType.java */
/* renamed from: d1.c, reason: use source file name */
/* loaded from: classes.dex */
public enum CryptoEngCmdType {
    CE_CMD_RUN_PKI_FIRST_CMD(DataLinkConstants.RUS_UPDATE),
    CE_CMD_RUN_PKI_HASH(10001),
    CE_CMD_RUN_PKI_HMAC(EventType.GEOFENCE),
    CE_CMD_RUN_PKI_HKDF(EventType.ARRIVE_HOME),
    CE_CMD_RUN_PKI_AES(EventType.LEAVE_HOME),
    CE_CMD_RUN_PKI_RSA(EventType.ARRIVE_COMPANY),
    CE_CMD_RUN_PKI_ECDH(EventType.LEAVE_COMPANY),
    CE_CMD_RUN_PKI_ECIES(EventType.EVENT_ASSOCIATION),
    CE_CMD_RUN_PKI_ECDSA(EventType.AWARENESS_CAPABILITY),
    CE_CMD_RUN_PKI_CERT_VERIFY(EventType.AWARENESS_FENCE),
    CE_CMD_RUN_PKI_SIGN(10010),
    CE_CMD_RUN_PKI_EXPORT_CERT(10020),
    CE_CMD_RUN_PKI_MAX(10021);


    /* renamed from: e, reason: collision with root package name */
    private final int f10696e;

    CryptoEngCmdType(int i10) {
        this.f10696e = i10;
    }

    public static CryptoEngCmdType a(int i10) {
        for (CryptoEngCmdType cryptoEngCmdType : values()) {
            if (cryptoEngCmdType.b() == i10) {
                return cryptoEngCmdType;
            }
        }
        return null;
    }

    public int b() {
        return this.f10696e;
    }
}
