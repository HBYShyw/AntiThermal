package r0;

import java.math.BigInteger;

/* compiled from: QuadraticResidue.java */
/* renamed from: r0.c, reason: use source file name */
/* loaded from: classes.dex */
public class QuadraticResidue {

    /* renamed from: a, reason: collision with root package name */
    private BigInteger f17457a;

    /* renamed from: b, reason: collision with root package name */
    private BigInteger f17458b;

    /* renamed from: c, reason: collision with root package name */
    private BigInteger f17459c;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: QuadraticResidue.java */
    /* renamed from: r0.c$a */
    /* loaded from: classes.dex */
    public static class a {

        /* renamed from: a, reason: collision with root package name */
        private final BigInteger f17460a;

        /* renamed from: b, reason: collision with root package name */
        private final BigInteger f17461b;

        public a(BigInteger bigInteger, BigInteger bigInteger2) {
            this.f17460a = bigInteger;
            this.f17461b = bigInteger2;
        }

        public BigInteger a() {
            return this.f17460a;
        }

        public BigInteger b() {
            return this.f17461b;
        }
    }

    private BigInteger b(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger bigInteger3 = BigInteger.ZERO;
        while (bigInteger3.compareTo(bigInteger2) < 0) {
            BigInteger c10 = c(bigInteger3.multiply(bigInteger3).subtract(bigInteger), bigInteger2);
            BigInteger bigInteger4 = BigInteger.ONE;
            if (c10.compareTo(bigInteger2.subtract(bigInteger4)) == 0) {
                return bigInteger3;
            }
            bigInteger3 = bigInteger3.add(bigInteger4);
        }
        return BigInteger.valueOf(-1L);
    }

    private BigInteger c(BigInteger bigInteger, BigInteger bigInteger2) {
        return f(d(bigInteger, bigInteger2), bigInteger2.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2L)), bigInteger2);
    }

    private BigInteger d(BigInteger bigInteger, BigInteger bigInteger2) {
        return bigInteger.mod(bigInteger2).add(bigInteger2).mod(bigInteger2);
    }

    private a e(a aVar, a aVar2) {
        BigInteger bigInteger = this.f17457a;
        return new a(aVar.a().multiply(aVar2.a()).add(aVar.b().multiply(aVar2.b()).mod(this.f17458b).multiply(bigInteger.multiply(bigInteger).subtract(this.f17459c))).mod(this.f17458b), aVar.b().multiply(aVar2.a()).add(aVar.a().multiply(aVar2.b())).mod(this.f17458b));
    }

    private BigInteger f(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        BigInteger bigInteger4 = BigInteger.ONE;
        BigInteger mod = bigInteger.mod(bigInteger3);
        while (true) {
            BigInteger bigInteger5 = BigInteger.ZERO;
            if (bigInteger2.compareTo(bigInteger5) == 0) {
                return bigInteger4;
            }
            if (bigInteger2.and(BigInteger.ONE).compareTo(bigInteger5) != 0) {
                bigInteger4 = bigInteger4.multiply(mod).mod(bigInteger3);
            }
            mod = mod.multiply(mod).mod(bigInteger3);
            bigInteger2 = bigInteger2.shiftRight(1);
        }
    }

    private a g(a aVar, BigInteger bigInteger) {
        a aVar2 = new a(BigInteger.ONE, BigInteger.ZERO);
        while (true) {
            BigInteger bigInteger2 = BigInteger.ZERO;
            if (bigInteger.compareTo(bigInteger2) == 0) {
                return aVar2;
            }
            if (bigInteger.and(BigInteger.ONE).compareTo(bigInteger2) != 0) {
                aVar2 = e(aVar2, aVar);
            }
            aVar = e(aVar, aVar);
            bigInteger = bigInteger.shiftRight(1);
        }
    }

    public BigInteger a(BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger mod = bigInteger.mod(bigInteger2);
        BigInteger bigInteger3 = BigInteger.ZERO;
        if (mod.compareTo(bigInteger3) == 0) {
            return bigInteger3;
        }
        BigInteger c10 = c(bigInteger, bigInteger2);
        BigInteger bigInteger4 = BigInteger.ONE;
        if (c10.compareTo(bigInteger4) != 0) {
            return BigInteger.valueOf(-1L);
        }
        this.f17459c = bigInteger;
        this.f17458b = bigInteger2;
        BigInteger b10 = b(bigInteger, bigInteger2);
        this.f17457a = b10;
        return d(g(new a(b10, bigInteger4), bigInteger2.add(bigInteger4).divide(BigInteger.valueOf(2L))).a(), bigInteger2);
    }
}
