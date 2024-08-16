package ja;

import android.text.TextUtils;
import android.util.Log;

/* compiled from: WordQueryCompat.java */
/* renamed from: ja.b1, reason: use source file name */
/* loaded from: classes2.dex */
public class WordQueryCompat {

    /* renamed from: a, reason: collision with root package name */
    static int f13078a = 12295;

    /* renamed from: b, reason: collision with root package name */
    static int f13079b = 63865;

    /* renamed from: c, reason: collision with root package name */
    static String f13080c = " ";

    /* renamed from: d, reason: collision with root package name */
    static Object[] f13081d = {-1, null, null};

    /* renamed from: e, reason: collision with root package name */
    static int[][] f13082e = {new int[]{12295, 20385}, new int[]{20386, 20801}, new int[]{20802, 21224}, new int[]{21225, 21642}, new int[]{21643, 22063}, new int[]{22064, 22480}, new int[]{22481, 22897}, new int[]{22898, 23313}, new int[]{23314, 23729}, new int[]{23730, 24146}, new int[]{24147, 24562}, new int[]{24563, 24979}, new int[]{24980, 25395}, new int[]{25396, 25811}, new int[]{25812, 26228}, new int[]{26229, 26649}, new int[]{26650, 27067}, new int[]{27068, 27483}, new int[]{27484, 27900}, new int[]{27901, 28316}, new int[]{28317, 28732}, new int[]{28733, 29149}, new int[]{29150, 29566}, new int[]{29567, 29989}, new int[]{29990, 30406}, new int[]{30407, 30823}, new int[]{30824, 31240}, new int[]{31241, 31657}, new int[]{31658, 32073}, new int[]{32074, 32490}, new int[]{32491, 32907}, new int[]{32908, 33324}, new int[]{33325, 33741}, new int[]{33742, 34158}, new int[]{34159, 34576}, new int[]{34577, 34993}, new int[]{34994, 35410}, new int[]{35411, 35826}, new int[]{35827, 36242}, new int[]{36243, 36658}, new int[]{36659, 37075}, new int[]{37076, 37491}, new int[]{37492, 37908}, new int[]{37909, 38325}, new int[]{38326, 38744}, new int[]{38745, 39160}, new int[]{39161, 39576}, new int[]{39577, 39992}, new int[]{39993, 40408}, new int[]{40409, 40824}, new int[]{40825, 63865}};

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
                if (f13080c.equals(substring) || (e10 = e(substring)) == f13081d) {
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
            if (!f13080c.equals(substring)) {
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
        return f13081d;
    }

    private static Object[] e(String str) {
        int g6 = g(str);
        if (g6 >= f13078a && g6 <= f13079b) {
            if (z0.f13133b) {
                Object[] f10 = f(g6, NameDictData.f13075a);
                if (f10 != f13081d) {
                    return f10;
                }
                Log.w(z0.f13132a, "use Name mode, but can't find Name Pinyin in Name Dict-->" + str);
            }
            int i10 = -1;
            int i11 = 0;
            while (true) {
                int[][] iArr = f13082e;
                if (i11 >= iArr.length) {
                    break;
                }
                int[] iArr2 = iArr[i11];
                if (g6 >= iArr2[0] && g6 <= iArr2[1]) {
                    i10 = i11;
                    break;
                }
                i11++;
            }
            switch (i10) {
                case 0:
                    return d(g6, a.f13073a);
                case 1:
                    return d(g6, l.f13103a);
                case 2:
                    return d(g6, w.f13125a);
                case 3:
                    return d(g6, h0.f13096a);
                case 4:
                    return d(g6, s0.f13118a);
                case 5:
                    return d(g6, u0.f13122a);
                case 6:
                    return d(g6, v0.f13124a);
                case 7:
                    return d(g6, w0.f13126a);
                case 8:
                    return d(g6, x0.f13128a);
                case 9:
                    return d(g6, y0.f13130a);
                case 10:
                    return d(g6, b.f13076a);
                case 11:
                    return d(g6, c.f13083a);
                case 12:
                    return d(g6, d.f13087a);
                case 13:
                    return d(g6, e.f13089a);
                case 14:
                    return d(g6, f.f13091a);
                case 15:
                    return d(g6, g.f13093a);
                case 16:
                    return d(g6, h.f13095a);
                case 17:
                    return d(g6, i.f13097a);
                case 18:
                    return d(g6, j.f13099a);
                case 19:
                    return d(g6, k.f13101a);
                case 20:
                    return d(g6, m.f13105a);
                case 21:
                    return d(g6, n.f13107a);
                case 22:
                    return d(g6, o.f13109a);
                case 23:
                    return d(g6, p.f13111a);
                case 24:
                    return d(g6, q.f13113a);
                case 25:
                    return d(g6, r.f13115a);
                case 26:
                    return d(g6, s.f13117a);
                case 27:
                    return d(g6, t.f13119a);
                case 28:
                    return d(g6, u.f13121a);
                case 29:
                    return d(g6, v.f13123a);
                case 30:
                    return d(g6, x.f13127a);
                case 31:
                    return d(g6, y.f13129a);
                case 32:
                    return d(g6, z.f13131a);
                case 33:
                    return d(g6, a0.f13074a);
                case 34:
                    return d(g6, b0.f13077a);
                case 35:
                    return d(g6, c0.f13084a);
                case 36:
                    return d(g6, d0.f13088a);
                case 37:
                    return d(g6, e0.f13090a);
                case 38:
                    return d(g6, f0.f13092a);
                case 39:
                    return d(g6, g0.f13094a);
                case 40:
                    return d(g6, i0.f13098a);
                case 41:
                    return d(g6, j0.f13100a);
                case 42:
                    return d(g6, k0.f13102a);
                case 43:
                    return d(g6, l0.f13104a);
                case 44:
                    return d(g6, m0.f13106a);
                case 45:
                    return d(g6, n0.f13108a);
                case 46:
                    return d(g6, o0.f13110a);
                case 47:
                    return d(g6, p0.f13112a);
                case 48:
                    return d(g6, q0.f13114a);
                case 49:
                    return d(g6, r0.f13116a);
                case 50:
                    return d(g6, t0.f13120a);
                default:
                    return f13081d;
            }
        }
        return f13081d;
    }

    private static Object[] f(int i10, Object[][] objArr) {
        for (int i11 = 0; i11 < objArr.length; i11++) {
            if (i10 == ((Integer) objArr[i11][0]).intValue()) {
                return objArr[i11];
            }
        }
        return f13081d;
    }

    private static int g(String str) {
        return str.charAt(0);
    }
}
