package c1;

import com.oplus.hardware.cryptoeng.CryptoEngManager;
import com.oplus.os.OplusBuild;
import e1.i;
import java.util.ArrayList;

/* compiled from: SystemChannel.java */
/* renamed from: c1.d, reason: use source file name */
/* loaded from: classes.dex */
public class SystemChannel {

    /* renamed from: a, reason: collision with root package name */
    private static volatile SystemChannel f4752a;

    private SystemChannel() {
    }

    private static ArrayList<Byte> a(ArrayList<Byte> arrayList) {
        try {
            Class<?> cls = Class.forName("vendor.oplus.hardware.cryptoeng.V1_0.ICryptoeng");
            Object b10 = b();
            if (b10 != null) {
                return (ArrayList) cls.getMethod("cryptoeng_invoke_command", ArrayList.class).invoke(b10, arrayList);
            }
            return null;
        } catch (Exception e10) {
            i.d("SystemChannel", "cryptoengInvokeCommand error. " + e10);
            return null;
        }
    }

    private static Object b() {
        try {
            Class<?> cls = Class.forName("vendor.oplus.hardware.cryptoeng.V1_0.ICryptoeng");
            return cls.getMethod("getService", new Class[0]).invoke(cls, new Object[0]);
        } catch (Exception e10) {
            i.a("SystemChannel", "getCryptoEngService error. " + e10);
            return null;
        }
    }

    public static SystemChannel c() {
        if (f4752a == null) {
            synchronized (SystemChannel.class) {
                if (f4752a == null) {
                    f4752a = new SystemChannel();
                }
            }
        }
        return f4752a;
    }

    private static boolean d() {
        int i10 = OplusBuild.VERSION.SDK_VERSION;
        if (i10 > 29) {
            return true;
        }
        return i10 == 29 && OplusBuild.VERSION.SDK_SUB_VERSION >= 31;
    }

    public byte[] e(byte[] bArr) {
        try {
            if (OplusBuild.getOplusOSVERSION() >= 26 && d()) {
                return CryptoEngManager.getInstance().cryptoEngCommand(bArr);
            }
            return f(bArr);
        } catch (NoClassDefFoundError e10) {
            i.b("SystemChannel", "processCmdV2 error. " + e10);
            return f(bArr);
        }
    }

    public byte[] f(byte[] bArr) {
        ArrayList<Byte> arrayList;
        int size;
        ArrayList arrayList2 = new ArrayList();
        for (byte b10 : bArr) {
            arrayList2.add(Byte.valueOf(b10));
        }
        byte[] bArr2 = null;
        try {
            arrayList = a(arrayList2);
        } catch (Exception e10) {
            i.d("SystemChannel", "processCmdV2HIDL failed, try again. " + e10);
            try {
                arrayList = a(arrayList2);
            } catch (Exception e11) {
                i.b("SystemChannel", "processCmdV2HIDL failed again. " + e11);
                arrayList = null;
            }
        }
        if (arrayList != null && (size = arrayList.size()) > 0) {
            bArr2 = new byte[size];
            for (int i10 = 0; i10 < size; i10++) {
                bArr2[i10] = arrayList.get(i10).byteValue();
            }
        }
        return bArr2;
    }
}
