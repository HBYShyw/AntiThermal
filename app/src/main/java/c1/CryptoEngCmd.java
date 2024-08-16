package c1;

import c1.ResultParser;
import com.oplus.os.OplusBuild;
import com.oplusx.sysapi.cryptoeng.CryptoengNative;
import d1.CeMemType;
import d1.CertType;
import d1.CryptoEngCmdType;
import d1.HmacAlg;
import d1.PrivKeyLabelType;
import d1.SignAlgType;
import e1.i;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;

/* compiled from: CryptoEngCmd.java */
/* renamed from: c1.a, reason: use source file name */
/* loaded from: classes.dex */
public class CryptoEngCmd {

    /* renamed from: a, reason: collision with root package name */
    private static boolean f4741a = true;

    public static boolean a(X509Certificate x509Certificate) {
        CryptoEngCmdType cryptoEngCmdType = CryptoEngCmdType.CE_CMD_RUN_PKI_CERT_VERIFY;
        MethodBuffer methodBuffer = new MethodBuffer(cryptoEngCmdType);
        methodBuffer.b(CeMemType.PKI_CAL_TYPE_T, x509Certificate.getEncoded());
        byte[] e10 = methodBuffer.e();
        byte[] f10 = f(e10);
        if (!f.i(e10, f10)) {
            ResultParser.b a10 = ResultParser.a(f10);
            if (f.h(a10, cryptoEngCmdType)) {
                return a10.d();
            }
            throw new TAInterfaceException(g("pkiCertVerify"));
        }
        throw new TAInterfaceException(g("pkiCertVerify"));
    }

    public static List<X509Certificate> b(CertType certType) {
        CryptoEngCmdType cryptoEngCmdType = CryptoEngCmdType.CE_CMD_RUN_PKI_EXPORT_CERT;
        MethodBuffer methodBuffer = new MethodBuffer(cryptoEngCmdType);
        methodBuffer.c(CeMemType.PKI_CERT_TYPE_T, certType.a());
        ResultParser.b a10 = ResultParser.a(f(methodBuffer.e()));
        if (f.g(a10, cryptoEngCmdType)) {
            return f.c(a10, certType);
        }
        throw new TAInterfaceException(g("pkiExportCert"));
    }

    public static String c(String str) {
        ResultParser.a c10;
        CryptoEngCmdType cryptoEngCmdType = CryptoEngCmdType.CE_CMD_RUN_PKI_HKDF;
        MethodBuffer methodBuffer = new MethodBuffer(cryptoEngCmdType);
        methodBuffer.d(CeMemType.PKI_CAL_TYPE_T, str);
        ResultParser.b a10 = ResultParser.a(f(methodBuffer.e()));
        if (f.g(a10, cryptoEngCmdType) && (c10 = a10.c(CeMemType.PKI_RSP_TYPE_T)) != null) {
            byte[] a11 = c10.a();
            if (!f.f(a11)) {
                return new String(a11, StandardCharsets.UTF_8);
            }
        }
        throw new TAInterfaceException(g("pkiHkdf"));
    }

    public static String d(String str, byte[] bArr, byte[] bArr2, int i10, HmacAlg hmacAlg) {
        return c(f.j(str, bArr, bArr2, i10, hmacAlg.b()));
    }

    public static String e(String str, PrivKeyLabelType privKeyLabelType, SignAlgType signAlgType) {
        ResultParser.a c10;
        String k10 = f.k(str, privKeyLabelType.a(), signAlgType.a());
        CryptoEngCmdType cryptoEngCmdType = CryptoEngCmdType.CE_CMD_RUN_PKI_SIGN;
        MethodBuffer methodBuffer = new MethodBuffer(cryptoEngCmdType);
        methodBuffer.d(CeMemType.PKI_CAL_TYPE_T, k10);
        ResultParser.b a10 = ResultParser.a(f(methodBuffer.e()));
        if (f.g(a10, cryptoEngCmdType) && (c10 = a10.c(CeMemType.PKI_RSP_TYPE_T)) != null) {
            byte[] a11 = c10.a();
            if (!f.f(a11)) {
                return new String(a11, StandardCharsets.UTF_8);
            }
        }
        throw new TAInterfaceException(g("pkiSign"));
    }

    private static byte[] f(byte[] bArr) {
        byte[] processCmdV2;
        if (bArr == null) {
            return null;
        }
        byte[] e10 = SystemChannel.c().e(bArr);
        if (e10 != null || !f4741a) {
            return e10;
        }
        try {
            if (OplusBuild.getOplusOSVERSION() >= 22) {
                processCmdV2 = CryptoengNative.processCmdV2(bArr);
            } else if (OplusBuild.getOplusOSVERSION() >= 19) {
                processCmdV2 = com.oplus.compat.cryptoeng.CryptoengNative.processCmdV2(bArr);
            } else {
                i.a("CryptoEngCmd", "The api only supports os version 11.0 and above");
                return e10;
            }
            return processCmdV2;
        } catch (Exception e11) {
            i.b("CryptoEngCmd", "processCmdV2 error. " + e11);
            f4741a = false;
            return e10;
        } catch (NoClassDefFoundError unused) {
            i.a("CryptoEngCmd", "processCmdV2 error, NoClassDefFound.");
            f4741a = false;
            return e10;
        }
    }

    private static String g(String str) {
        return "Failed to call the " + str + " function of the ta program and returned an abnormal result.";
    }
}
