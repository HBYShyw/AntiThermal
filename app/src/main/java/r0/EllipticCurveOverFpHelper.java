package r0;

import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;

/* compiled from: EllipticCurveOverFpHelper.java */
/* renamed from: r0.a, reason: use source file name */
/* loaded from: classes.dex */
public class EllipticCurveOverFpHelper {
    public static ECPoint a(EllipticCurve ellipticCurve, byte[] bArr) {
        int fieldSize = (ellipticCurve.getField().getFieldSize() + 7) / 8;
        BigInteger p10 = ((ECFieldFp) ellipticCurve.getField()).getP();
        byte b10 = bArr[0];
        if (b10 == 2 || b10 == 3) {
            if (bArr.length == fieldSize + 1) {
                BigInteger c10 = c(bArr, 1, fieldSize);
                if (c10.signum() >= 0 && c10.compareTo(p10) < 0) {
                    BigInteger a10 = new QuadraticResidue().a(c10.pow(3).add(ellipticCurve.getA().multiply(c10)).add(ellipticCurve.getB()).mod(p10), p10);
                    if (a10.testBit(0) == (b10 == 2)) {
                        a10 = f(p10, a10);
                    }
                    return new ECPoint(c10, a10);
                }
                throw new InvalidKeySpecException("x value invalid for current curve field element");
            }
            throw new InvalidKeySpecException("Incorrect length for compressed encoding");
        }
        if (b10 == 4) {
            if (bArr.length == (fieldSize * 2) + 1) {
                BigInteger c11 = c(bArr, 1, fieldSize);
                if (c11.signum() >= 0 && c11.compareTo(p10) < 0) {
                    ECPoint eCPoint = new ECPoint(c11, c(bArr, fieldSize + 1, fieldSize));
                    if (e(ellipticCurve, eCPoint)) {
                        return eCPoint;
                    }
                    throw new InvalidKeySpecException("Invalid point coordinates");
                }
                throw new InvalidKeySpecException("x value invalid for current curve field element");
            }
            throw new InvalidKeySpecException("Incorrect length for compressed encoding");
        }
        throw new InvalidKeySpecException("Invalid point encoding 0x" + Integer.toString(b10, 16));
    }

    public static byte[] b(EllipticCurve ellipticCurve, ECPoint eCPoint, boolean z10) {
        byte[] bArr;
        if (d(eCPoint)) {
            return new byte[1];
        }
        int fieldSize = (ellipticCurve.getField().getFieldSize() + 7) / 8;
        BigInteger affineX = eCPoint.getAffineX();
        BigInteger affineY = eCPoint.getAffineY();
        if (z10) {
            bArr = new byte[fieldSize + 1];
            if (affineY.testBit(0)) {
                bArr[0] = 3;
            } else {
                bArr[0] = 2;
            }
            byte[] byteArray = affineX.toByteArray();
            System.arraycopy(byteArray, byteArray.length > fieldSize ? byteArray.length - fieldSize : 0, bArr, (byteArray.length < fieldSize ? fieldSize - byteArray.length : 0) + 1, Math.min(byteArray.length, fieldSize));
        } else {
            bArr = new byte[(fieldSize * 2) + 1];
            bArr[0] = 4;
            byte[] byteArray2 = affineX.toByteArray();
            byte[] byteArray3 = affineY.toByteArray();
            System.arraycopy(byteArray2, byteArray2.length > fieldSize ? byteArray2.length - fieldSize : 0, bArr, (byteArray2.length < fieldSize ? fieldSize - byteArray2.length : 0) + 1, Math.min(byteArray2.length, fieldSize));
            System.arraycopy(byteArray3, byteArray3.length > fieldSize ? byteArray3.length - fieldSize : 0, bArr, (byteArray3.length < fieldSize ? fieldSize - byteArray3.length : 0) + 1 + fieldSize, Math.min(byteArray3.length, fieldSize));
        }
        return bArr;
    }

    public static BigInteger c(byte[] bArr, int i10, int i11) {
        if (i10 != 0 || i11 != bArr.length) {
            byte[] bArr2 = new byte[i11];
            System.arraycopy(bArr, i10, bArr2, 0, i11);
            bArr = bArr2;
        }
        return new BigInteger(1, bArr);
    }

    public static boolean d(ECPoint eCPoint) {
        return eCPoint == ECPoint.POINT_INFINITY;
    }

    public static boolean e(EllipticCurve ellipticCurve, ECPoint eCPoint) {
        if (d(eCPoint)) {
            return true;
        }
        BigInteger affineX = eCPoint.getAffineX();
        BigInteger affineY = eCPoint.getAffineY();
        BigInteger p10 = ((ECFieldFp) ellipticCurve.getField()).getP();
        if (affineX.signum() < 0 || affineY.signum() < 0 || affineX.compareTo(p10) >= 0 || affineY.compareTo(p10) >= 0) {
            return false;
        }
        return affineX.pow(3).add(ellipticCurve.getA().multiply(affineX)).add(ellipticCurve.getB()).mod(p10).compareTo(affineY.pow(2).mod(p10)) == 0;
    }

    public static BigInteger f(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger2.signum() == 0) {
            return bigInteger2;
        }
        BigInteger subtract = bigInteger.subtract(bigInteger2);
        return subtract.signum() < 0 ? subtract.add(bigInteger) : subtract;
    }
}
