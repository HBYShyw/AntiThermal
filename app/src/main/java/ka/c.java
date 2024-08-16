package ka;

import java.security.SecureRandom;
import java.util.ArrayList;

/* loaded from: classes2.dex */
final class c {

    /* renamed from: a, reason: collision with root package name */
    private final int f14231a;

    /* renamed from: b, reason: collision with root package name */
    private final double f14232b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(int i10, double d10) {
        this.f14231a = i10;
        double exp = Math.exp(d10);
        this.f14232b = exp / ((i10 + exp) - 1.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String a(ArrayList<String> arrayList, String str, SecureRandom secureRandom) {
        int nextInt;
        if (secureRandom.nextDouble() < this.f14232b) {
            return str;
        }
        do {
            nextInt = secureRandom.nextInt(this.f14231a);
        } while (nextInt == arrayList.indexOf(str));
        return arrayList.get(nextInt);
    }
}
