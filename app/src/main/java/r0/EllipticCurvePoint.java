package r0;

import e1.HexStringUtils;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

/* compiled from: EllipticCurvePoint.java */
/* renamed from: r0.b, reason: use source file name */
/* loaded from: classes.dex */
public class EllipticCurvePoint {

    /* renamed from: a, reason: collision with root package name */
    private final ECPoint f17455a;

    /* renamed from: b, reason: collision with root package name */
    private final ECParameterSpec f17456b;

    private EllipticCurvePoint(ECParameterSpec eCParameterSpec, ECPoint eCPoint) {
        this.f17455a = eCPoint;
        this.f17456b = eCParameterSpec;
    }

    public static EllipticCurvePoint a(ECParameterSpec eCParameterSpec, byte[] bArr) {
        return new EllipticCurvePoint(eCParameterSpec, EllipticCurveOverFpHelper.a(eCParameterSpec.getCurve(), bArr));
    }

    private boolean b(ECParameterSpec eCParameterSpec) {
        return this.f17456b.getGenerator().equals(eCParameterSpec.getGenerator()) && this.f17456b.getOrder().equals(eCParameterSpec.getOrder()) && this.f17456b.getCofactor() == eCParameterSpec.getCofactor() && this.f17456b.getCurve().equals(eCParameterSpec.getCurve());
    }

    private byte[] g(BigInteger bigInteger, int i10) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length == i10) {
            return byteArray;
        }
        byte[] bArr = new byte[i10];
        int length = byteArray.length - i10;
        System.arraycopy(byteArray, Math.max(length, 0), bArr, length <= 0 ? -length : 0, Math.min(byteArray.length, i10));
        return bArr;
    }

    public byte[] c() {
        return g(this.f17455a.getAffineX(), (this.f17456b.getCurve().getField().getFieldSize() + 7) / 8);
    }

    public byte[] d() {
        return g(this.f17455a.getAffineY(), (this.f17456b.getCurve().getField().getFieldSize() + 7) / 8);
    }

    public ECPublicKey e() {
        try {
            return (ECPublicKey) KeyFactory.getInstance("EC").generatePublic(new ECPublicKeySpec(this.f17455a, this.f17456b));
        } catch (NoSuchAlgorithmException e10) {
            throw new InvalidKeySpecException(e10);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EllipticCurvePoint)) {
            return false;
        }
        EllipticCurvePoint ellipticCurvePoint = (EllipticCurvePoint) obj;
        return b(ellipticCurvePoint.f17456b) && this.f17455a.equals(ellipticCurvePoint.f17455a);
    }

    public boolean f() {
        return this.f17455a == ECPoint.POINT_INFINITY;
    }

    public int hashCode() {
        return Objects.hash(this.f17455a, this.f17456b);
    }

    public String toString() {
        if (f()) {
            return "INF";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        stringBuffer.append(HexStringUtils.a(c()));
        stringBuffer.append(',');
        stringBuffer.append(HexStringUtils.a(d()));
        stringBuffer.append(')');
        return stringBuffer.toString();
    }
}
