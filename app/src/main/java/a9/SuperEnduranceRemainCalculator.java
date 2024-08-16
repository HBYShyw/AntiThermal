package a9;

import java.util.ArrayList;

/* compiled from: SuperEnduranceRemainCalculator.java */
/* renamed from: a9.b, reason: use source file name */
/* loaded from: classes2.dex */
public class SuperEnduranceRemainCalculator {

    /* renamed from: a, reason: collision with root package name */
    private static ArrayList<Double> f116a;

    static {
        ArrayList<Double> arrayList = new ArrayList<>();
        f116a = arrayList;
        arrayList.add(Double.valueOf(0.7d));
        f116a.add(Double.valueOf(0.77d));
        f116a.add(Double.valueOf(1.0d));
        f116a.add(Double.valueOf(1.18d));
        f116a.add(Double.valueOf(1.3d));
    }

    public static long a(int i10) {
        long j10 = 0;
        for (int i11 = 1; i11 <= i10; i11++) {
            j10 = i11 == 1 ? j10 + 240 : (long) (j10 + (f116a.get((i11 - 1) / 20).doubleValue() * 110.0d));
        }
        return j10;
    }
}
