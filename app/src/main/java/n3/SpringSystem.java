package n3;

/* compiled from: SpringSystem.java */
/* renamed from: n3.k, reason: use source file name */
/* loaded from: classes.dex */
public class SpringSystem extends BaseSpringSystem {
    private SpringSystem(SpringLooper springLooper) {
        super(springLooper);
    }

    public static SpringSystem g() {
        return new SpringSystem(AndroidSpringLooperFactory.a());
    }
}
