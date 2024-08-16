package t8;

import android.content.Context;
import java.util.ArrayList;
import java.util.Comparator;

/* compiled from: PowerCpuUtil.java */
/* renamed from: t8.d, reason: use source file name */
/* loaded from: classes2.dex */
public class PowerCpuUtil {

    /* renamed from: a, reason: collision with root package name */
    private static Object f18651a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private static ArrayList<String> f18652b = new ArrayList<>();

    /* renamed from: c, reason: collision with root package name */
    private static ArrayList<b> f18653c = new ArrayList<>();

    /* renamed from: d, reason: collision with root package name */
    private static ArrayList<String> f18654d = new ArrayList<>();

    /* renamed from: e, reason: collision with root package name */
    public static final Comparator<b> f18655e = new a();

    /* compiled from: PowerCpuUtil.java */
    /* renamed from: t8.d$a */
    /* loaded from: classes2.dex */
    class a implements Comparator<b> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(b bVar, b bVar2) {
            if (bVar.f18657b.floatValue() < bVar2.f18657b.floatValue()) {
                return 1;
            }
            return bVar.f18657b.floatValue() > bVar2.f18657b.floatValue() ? -1 : 0;
        }
    }

    /* compiled from: PowerCpuUtil.java */
    /* renamed from: t8.d$b */
    /* loaded from: classes2.dex */
    public static class b {

        /* renamed from: a, reason: collision with root package name */
        public String f18656a;

        /* renamed from: b, reason: collision with root package name */
        Float f18657b;

        public b(String str, Float f10) {
            this.f18656a = str;
            this.f18657b = f10;
        }
    }

    public static void a(Context context) {
    }

    public static void b(Context context, ArrayList<b> arrayList) {
    }

    public static void c(Context context) {
        for (int size = f18653c.size() - 1; size >= 0; size--) {
            if (!f18654d.contains(f18653c.get(size).f18656a)) {
                f18653c.remove(size);
            }
        }
        b(context, f18653c);
        f18653c.clear();
        f18654d.clear();
    }

    public static void d(String str) {
        f18654d.remove(str);
    }

    public static void e(String str, float f10) {
        if (f18654d.contains(str)) {
            return;
        }
        f18654d.add(str);
        f18653c.add(new b(str, Float.valueOf(f10)));
    }
}
