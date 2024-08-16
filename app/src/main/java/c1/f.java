package c1;

import c1.ResultParser;
import d1.CeMemType;
import d1.CertType;
import d1.CryptoEngCmdType;
import e1.Base64Utils;
import e1.i;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* compiled from: Util.java */
/* loaded from: classes.dex */
public final class f {
    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v12, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v20 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r4v10, types: [java.lang.Object, java.lang.Exception] */
    /* JADX WARN: Type inference failed for: r4v6, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r5v2, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r8v0, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r8v11, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r8v12 */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v15 */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v18, types: [java.io.DataInputStream] */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v21 */
    /* JADX WARN: Type inference failed for: r8v22 */
    /* JADX WARN: Type inference failed for: r8v23 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5 */
    /* JADX WARN: Type inference failed for: r8v6, types: [java.io.DataInputStream] */
    /* JADX WARN: Type inference failed for: r8v9, types: [java.io.DataInputStream] */
    public static int a(byte[] bArr) {
        ByteArrayInputStream byteArrayInputStream;
        Throwable th;
        Exception e10;
        ?? r82;
        int i10;
        ByteArrayInputStream byteArrayInputStream2;
        String str;
        String str2 = "byteArrayToInt byteInput close e = ";
        try {
            try {
                byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    r82 = new DataInputStream(byteArrayInputStream);
                } catch (Exception e11) {
                    e10 = e11;
                    r82 = 0;
                } catch (Throwable th2) {
                    th = th2;
                    bArr = 0;
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e12) {
                            i.b("Util", str2 + e12);
                        }
                    }
                    if (bArr != 0) {
                        try {
                            bArr.close();
                            throw th;
                        } catch (Exception e13) {
                            i.b("Util", "byteArrayToInt dataInput close e = " + e13);
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (Exception e14) {
                byteArrayInputStream = null;
                e10 = e14;
                r82 = 0;
            } catch (Throwable th3) {
                byteArrayInputStream = null;
                th = th3;
                bArr = 0;
            }
            try {
                i10 = r82.readInt();
                try {
                    byteArrayInputStream.close();
                    str = str2;
                    byteArrayInputStream2 = byteArrayInputStream;
                } catch (Exception e15) {
                    String str3 = "byteArrayToInt byteInput close e = " + e15;
                    i.b("Util", str3);
                    str = str3;
                    byteArrayInputStream2 = e15;
                }
                try {
                    r82.close();
                    str2 = str;
                    byteArrayInputStream = byteArrayInputStream2;
                    bArr = r82;
                } catch (Exception e16) {
                    ?? sb2 = new StringBuilder();
                    sb2.append("byteArrayToInt dataInput close e = ");
                    sb2.append(e16);
                    String sb3 = sb2.toString();
                    i.b("Util", sb3);
                    str2 = sb2;
                    byteArrayInputStream = byteArrayInputStream2;
                    bArr = sb3;
                }
            } catch (Exception e17) {
                e10 = e17;
                i.b("Util", "byteArrayToInt: e = " + e10);
                ?? r12 = str2;
                ByteArrayInputStream byteArrayInputStream3 = byteArrayInputStream;
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                        r12 = str2;
                        byteArrayInputStream3 = byteArrayInputStream;
                    } catch (Exception e18) {
                        ?? sb4 = new StringBuilder();
                        sb4.append("byteArrayToInt byteInput close e = ");
                        sb4.append(e18);
                        String sb5 = sb4.toString();
                        i.b("Util", sb5);
                        r12 = sb5;
                        byteArrayInputStream3 = sb4;
                    }
                }
                if (r82 != 0) {
                    try {
                        r82.close();
                    } catch (Exception e19) {
                        r12 = new StringBuilder();
                        r12.append("byteArrayToInt dataInput close e = ");
                        r12.append(e19);
                        r82 = r12.toString();
                        i.b("Util", r82);
                    }
                }
                i10 = -1;
                str2 = r12;
                byteArrayInputStream = byteArrayInputStream3;
                bArr = r82;
                return i10;
            }
            return i10;
        } catch (Throwable th4) {
            th = th4;
        }
    }

    private static List<CeMemType> b(CertType certType) {
        ArrayList arrayList = new ArrayList();
        if (certType == CertType.ROOT_CA_CERT_LABEL) {
            arrayList.add(CeMemType.PKI_ROOT_CA_CERT_TYPE_T);
        } else if (certType == CertType.SERVICE_CA_CERT_LABEL) {
            arrayList.add(CeMemType.PKI_SERVICE_CA_CERT_TYPE_T);
        } else if (certType == CertType.DEVICE_CA_CERT_LABEL) {
            arrayList.add(CeMemType.PKI_DEVICE_CA_CERT_TYPE_T);
        } else if (certType == CertType.DEVICE_EE_CERT_LABEL) {
            arrayList.add(CeMemType.PKI_DEVICE_EE_CERT_TYPE_T);
        } else if (certType == CertType.ALL_CA_CERT_LABEL) {
            arrayList.add(0, CeMemType.PKI_ROOT_CA_CERT_TYPE_T);
            arrayList.add(1, CeMemType.PKI_DEVICE_CA_CERT_TYPE_T);
            arrayList.add(2, CeMemType.PKI_SERVICE_CA_CERT_TYPE_T);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<X509Certificate> c(ResultParser.b bVar, CertType certType) {
        ArrayList arrayList = new ArrayList();
        Iterator<CeMemType> it = b(certType).iterator();
        while (it.hasNext()) {
            ResultParser.a c10 = bVar.c(it.next());
            if (c10 != null) {
                byte[] a10 = c10.a();
                if (!f(a10)) {
                    arrayList.add(l(a10));
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00c3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static byte[] d(String str) {
        DataOutputStream dataOutputStream;
        byte[] bArr;
        StringBuilder sb2;
        ByteArrayOutputStream byteArrayOutputStream;
        if (str == null) {
            return new byte[0];
        }
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e10) {
            i.d("Util", "getUTF8Bytes UnsupportedEncodingException ue = " + e10);
            ByteArrayOutputStream byteArrayOutputStream2 = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                } catch (Exception unused) {
                    dataOutputStream = null;
                } catch (Throwable th) {
                    th = th;
                    dataOutputStream = null;
                }
            } catch (Exception unused2) {
                dataOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                dataOutputStream = null;
            }
            try {
                dataOutputStream.writeUTF(str);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                int length = byteArray.length - 2;
                bArr = new byte[length];
                System.arraycopy(byteArray, 2, bArr, 0, length);
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e11) {
                    i.b("Util", "getUTF8Bytes bos close e = " + e11);
                }
            } catch (Exception unused3) {
                byteArrayOutputStream2 = byteArrayOutputStream;
                try {
                    bArr = new byte[0];
                    if (byteArrayOutputStream2 != null) {
                        try {
                            byteArrayOutputStream2.close();
                        } catch (Exception e12) {
                            i.b("Util", "getUTF8Bytes bos close e = " + e12);
                        }
                    }
                    if (dataOutputStream != null) {
                        try {
                            dataOutputStream.close();
                        } catch (Exception e13) {
                            e = e13;
                            sb2 = new StringBuilder();
                            sb2.append("getUTF8Bytes dos close e = ");
                            sb2.append(e);
                            i.b("Util", sb2.toString());
                            return bArr;
                        }
                    }
                    return bArr;
                } catch (Throwable th3) {
                    th = th3;
                    if (byteArrayOutputStream2 != null) {
                        try {
                            byteArrayOutputStream2.close();
                        } catch (Exception e14) {
                            i.b("Util", "getUTF8Bytes bos close e = " + e14);
                        }
                    }
                    if (dataOutputStream == null) {
                        try {
                            dataOutputStream.close();
                            throw th;
                        } catch (Exception e15) {
                            i.b("Util", "getUTF8Bytes dos close e = " + e15);
                            throw th;
                        }
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                byteArrayOutputStream2 = byteArrayOutputStream;
                if (byteArrayOutputStream2 != null) {
                }
                if (dataOutputStream == null) {
                }
            }
            try {
                dataOutputStream.close();
            } catch (Exception e16) {
                e = e16;
                sb2 = new StringBuilder();
                sb2.append("getUTF8Bytes dos close e = ");
                sb2.append(e);
                i.b("Util", sb2.toString());
                return bArr;
            }
            return bArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:48:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0093 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static byte[] e(int i10) {
        ByteArrayOutputStream byteArrayOutputStream;
        DataOutputStream dataOutputStream;
        StringBuilder sb2;
        DataOutputStream dataOutputStream2 = null;
        r3 = null;
        r3 = null;
        byte[] bArr = null;
        dataOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e10) {
            e = e10;
            byteArrayOutputStream = null;
            dataOutputStream = null;
        } catch (Throwable th) {
            th = th;
            byteArrayOutputStream = null;
        }
        try {
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        } catch (Exception e11) {
            e = e11;
            dataOutputStream = null;
        } catch (Throwable th2) {
            th = th2;
            if (dataOutputStream2 != null) {
            }
            if (byteArrayOutputStream == null) {
            }
        }
        try {
            try {
                dataOutputStream.writeInt(i10);
                bArr = byteArrayOutputStream.toByteArray();
                try {
                    dataOutputStream.close();
                } catch (Exception e12) {
                    i.b("Util", "intToByteArray dataOut close e = " + e12);
                }
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e13) {
                    e = e13;
                    sb2 = new StringBuilder();
                    sb2.append("intToByteArray byteOut close e = ");
                    sb2.append(e);
                    i.b("Util", sb2.toString());
                    return bArr;
                }
            } catch (Throwable th3) {
                th = th3;
                dataOutputStream2 = dataOutputStream;
                if (dataOutputStream2 != null) {
                    try {
                        dataOutputStream2.close();
                    } catch (Exception e14) {
                        i.b("Util", "intToByteArray dataOut close e = " + e14);
                    }
                }
                if (byteArrayOutputStream == null) {
                    try {
                        byteArrayOutputStream.close();
                        throw th;
                    } catch (Exception e15) {
                        i.b("Util", "intToByteArray byteOut close e = " + e15);
                        throw th;
                    }
                }
                throw th;
            }
        } catch (Exception e16) {
            e = e16;
            i.b("Util", "intToByteArray e = " + e);
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (Exception e17) {
                    i.b("Util", "intToByteArray dataOut close e = " + e17);
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (Exception e18) {
                    e = e18;
                    sb2 = new StringBuilder();
                    sb2.append("intToByteArray byteOut close e = ");
                    sb2.append(e);
                    i.b("Util", sb2.toString());
                    return bArr;
                }
            }
            return bArr;
        }
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean f(byte[] bArr) {
        return bArr == null || bArr.length == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean g(ResultParser.b bVar, CryptoEngCmdType cryptoEngCmdType) {
        if (bVar == null || cryptoEngCmdType == null) {
            return false;
        }
        CryptoEngCmdType b10 = bVar.b();
        if (b10 == null) {
            i.d("Util", "isMethodExecuteSuccess resultMethodType is null");
            return false;
        }
        if (b10.b() != cryptoEngCmdType.b()) {
            i.d("Util", "isMethodExecuteSuccess resultMethodType = " + b10 + ", methodType = " + cryptoEngCmdType);
            return false;
        }
        if (bVar.d()) {
            return true;
        }
        i.d("Util", "isMethodExecuteSuccess isExeSuccess = false");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean h(ResultParser.b bVar, CryptoEngCmdType cryptoEngCmdType) {
        if (bVar == null || cryptoEngCmdType == null) {
            return false;
        }
        CryptoEngCmdType b10 = bVar.b();
        if (b10 == null) {
            i.d("Util", "isMethodExecuteSuccess resultMethodType is null");
            return false;
        }
        if (b10.b() == cryptoEngCmdType.b()) {
            return true;
        }
        i.d("Util", "isMethodExecuteSuccess resultMethodType = " + b10 + ", methodType = " + cryptoEngCmdType);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean i(byte[] bArr, byte[] bArr2) {
        if (f(bArr2)) {
            i.a("Util", "parse buffer is empty");
            return true;
        }
        if (!Arrays.equals(bArr, bArr2)) {
            return false;
        }
        i.a("Util", "ta unsupported");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String j(String str, byte[] bArr, byte[] bArr2, int i10, int i11) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("only_key_label", "0");
        jSONObject.put("key_label", str);
        jSONObject.put("info", Base64Utils.b(bArr2));
        jSONObject.put("salt", Base64Utils.b(bArr));
        jSONObject.put("okm_len", String.valueOf(i10));
        jSONObject.put("hash", String.valueOf(i11));
        return jSONObject.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String k(String str, int i10, int i11) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("data", str);
        jSONObject.put("key_label", String.valueOf(i10));
        jSONObject.put("sign_alg", String.valueOf(i11));
        return jSONObject.toString();
    }

    static X509Certificate l(byte[] bArr) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(byteArrayInputStream);
        byteArrayInputStream.close();
        return x509Certificate;
    }
}
