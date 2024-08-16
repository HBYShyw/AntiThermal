package c8;

import android.text.TextUtils;

/* compiled from: WordQuery.java */
/* renamed from: c8.z0, reason: use source file name */
/* loaded from: classes.dex */
public class WordQuery {

    /* renamed from: a, reason: collision with root package name */
    static int f4963a = 12295;

    /* renamed from: b, reason: collision with root package name */
    static int f4964b = 63865;

    /* renamed from: c, reason: collision with root package name */
    static String f4965c = " ";

    /* renamed from: d, reason: collision with root package name */
    static Object[] f4966d = {-1, null, null};

    /* renamed from: e, reason: collision with root package name */
    static int[][] f4967e = {new int[]{12295, 20385}, new int[]{20386, 20801}, new int[]{20802, 21224}, new int[]{21225, 21642}, new int[]{21643, 22063}, new int[]{22064, 22480}, new int[]{22481, 22897}, new int[]{22898, 23313}, new int[]{23314, 23729}, new int[]{23730, 24146}, new int[]{24147, 24562}, new int[]{24563, 24979}, new int[]{24980, 25395}, new int[]{25396, 25811}, new int[]{25812, 26228}, new int[]{26229, 26649}, new int[]{26650, 27067}, new int[]{27068, 27483}, new int[]{27484, 27900}, new int[]{27901, 28316}, new int[]{28317, 28732}, new int[]{28733, 29149}, new int[]{29150, 29566}, new int[]{29567, 29989}, new int[]{29990, 30406}, new int[]{30407, 30823}, new int[]{30824, 31240}, new int[]{31241, 31657}, new int[]{31658, 32073}, new int[]{32074, 32490}, new int[]{32491, 32907}, new int[]{32908, 33324}, new int[]{33325, 33741}, new int[]{33742, 34158}, new int[]{34159, 34576}, new int[]{34577, 34993}, new int[]{34994, 35410}, new int[]{35411, 35826}, new int[]{35827, 36242}, new int[]{36243, 36658}, new int[]{36659, 37075}, new int[]{37076, 37491}, new int[]{37492, 37908}, new int[]{37909, 38325}, new int[]{38326, 38744}, new int[]{38745, 39160}, new int[]{39161, 39576}, new int[]{39577, 39992}, new int[]{39993, 40408}, new int[]{40409, 40824}, new int[]{40825, 63865}};

    public static char a(char c10) {
        String b10 = b(String.valueOf(c10));
        if (TextUtils.isEmpty(b10) || b10.length() <= 0) {
            return '0';
        }
        return b10.charAt(0);
    }

    public static String b(String str) {
        Object[] e10;
        try {
            if (!TextUtils.isEmpty(str) && str.length() >= 1) {
                String substring = str.substring(0, 1);
                if (f4965c.equals(substring) || (e10 = e(substring)) == f4966d) {
                    return null;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(e10[1]);
                return sb2.toString();
            }
            return null;
        } catch (Exception e11) {
            e11.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r3v2, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v0 */
    public static String c(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        int i10 = 0;
        while (i10 < str.length()) {
            int i11 = i10 + 1;
            String substring = str.substring(i10, i11);
            if (!f4965c.equals(substring)) {
                ?? e10 = e(substring);
                String str2 = substring;
                if (e10[1] != 0) {
                    str2 = e10[1];
                }
                sb2.append((Object) str2);
            }
            i10 = i11;
        }
        return sb2.toString();
    }

    private static Object[] d(int i10, Object[][] objArr) {
        int length = objArr.length - 1;
        int i11 = 0;
        while (i11 <= length) {
            int i12 = (i11 + length) >> 1;
            Object[] objArr2 = objArr[i12];
            int intValue = ((Integer) objArr2[0]).intValue();
            if (i10 == intValue) {
                return objArr2;
            }
            if (i10 < intValue) {
                length = i12 - 1;
            } else {
                i11 = i12 + 1;
            }
        }
        return f4966d;
    }

    private static Object[] e(String str) {
        int f10 = f(str);
        if (f10 >= f4963a && f10 <= f4964b) {
            int i10 = -1;
            int i11 = 0;
            while (true) {
                int[][] iArr = f4967e;
                if (i11 >= iArr.length) {
                    break;
                }
                int[] iArr2 = iArr[i11];
                if (f10 >= iArr2[0] && f10 <= iArr2[1]) {
                    i10 = i11;
                    break;
                }
                i11++;
            }
            switch (i10) {
                case 0:
                    return d(f10, a.f4912a);
                case 1:
                    return d(f10, l.f4934a);
                case 2:
                    return d(f10, w.f4956a);
                case 3:
                    return d(f10, h0.f4927a);
                case 4:
                    return d(f10, s0.f4949a);
                case 5:
                    return d(f10, u0.f4953a);
                case 6:
                    return d(f10, v0.f4955a);
                case 7:
                    return d(f10, w0.f4957a);
                case 8:
                    return d(f10, x0.f4959a);
                case 9:
                    return d(f10, y0.f4961a);
                case 10:
                    return d(f10, b.f4914a);
                case 11:
                    return d(f10, c.f4916a);
                case 12:
                    return d(f10, d.f4918a);
                case 13:
                    return d(f10, e.f4920a);
                case 14:
                    return d(f10, f.f4922a);
                case 15:
                    return d(f10, g.f4924a);
                case 16:
                    return d(f10, h.f4926a);
                case 17:
                    return d(f10, i.f4928a);
                case 18:
                    return d(f10, j.f4930a);
                case 19:
                    return d(f10, k.f4932a);
                case 20:
                    return d(f10, m.f4936a);
                case 21:
                    return d(f10, n.f4938a);
                case 22:
                    return d(f10, o.f4940a);
                case 23:
                    return d(f10, p.f4942a);
                case 24:
                    return d(f10, q.f4944a);
                case 25:
                    return d(f10, r.f4946a);
                case 26:
                    return d(f10, s.f4948a);
                case 27:
                    return d(f10, t.f4950a);
                case 28:
                    return d(f10, u.f4952a);
                case 29:
                    return d(f10, v.f4954a);
                case 30:
                    return d(f10, x.f4958a);
                case 31:
                    return d(f10, y.f4960a);
                case 32:
                    return d(f10, z.f4962a);
                case 33:
                    return d(f10, a0.f4913a);
                case 34:
                    return d(f10, b0.f4915a);
                case 35:
                    return d(f10, c0.f4917a);
                case 36:
                    return d(f10, d0.f4919a);
                case 37:
                    return d(f10, e0.f4921a);
                case 38:
                    return d(f10, f0.f4923a);
                case 39:
                    return d(f10, g0.f4925a);
                case 40:
                    return d(f10, i0.f4929a);
                case 41:
                    return d(f10, j0.f4931a);
                case 42:
                    return d(f10, k0.f4933a);
                case 43:
                    return d(f10, l0.f4935a);
                case 44:
                    return d(f10, m0.f4937a);
                case 45:
                    return d(f10, n0.f4939a);
                case 46:
                    return d(f10, o0.f4941a);
                case 47:
                    return d(f10, p0.f4943a);
                case 48:
                    return d(f10, q0.f4945a);
                case 49:
                    return d(f10, r0.f4947a);
                case 50:
                    return d(f10, t0.f4951a);
                default:
                    return f4966d;
            }
        }
        return f4966d;
    }

    private static int f(String str) {
        return str.charAt(0);
    }
}
