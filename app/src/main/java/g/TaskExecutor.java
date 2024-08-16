package g;

/* compiled from: TaskExecutor.java */
/* renamed from: g.c, reason: use source file name */
/* loaded from: classes.dex */
public abstract class TaskExecutor {
    public abstract void a(Runnable runnable);

    public void b(Runnable runnable) {
        if (c()) {
            runnable.run();
        } else {
            d(runnable);
        }
    }

    public abstract boolean c();

    public abstract void d(Runnable runnable);
}
