package de;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import kotlin.Metadata;
import ma.Unit;
import za.k;

/* compiled from: TaskQueue.kt */
@Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010!\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0019\b\u0000\u0012\u0006\u0010\u0013\u001a\u00020\u0012\u0012\u0006\u0010\u0017\u001a\u00020\u0010¢\u0006\u0004\b.\u0010/J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0004J'\u0010\n\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\bH\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\f\u001a\u00020\u0006J\u0006\u0010\r\u001a\u00020\u0006J\u000f\u0010\u000e\u001a\u00020\bH\u0000¢\u0006\u0004\b\u000e\u0010\u000fJ\b\u0010\u0011\u001a\u00020\u0010H\u0016R\u001a\u0010\u0013\u001a\u00020\u00128\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u00108\u0000X\u0080\u0004¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001aR\"\u0010\u001b\u001a\u00020\b8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u001d\u0010\u000f\"\u0004\b\u001e\u0010\u001fR$\u0010 \u001a\u0004\u0018\u00010\u00028\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b \u0010!\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R \u0010'\u001a\b\u0012\u0004\u0012\u00020\u00020&8\u0000X\u0080\u0004¢\u0006\f\n\u0004\b'\u0010(\u001a\u0004\b)\u0010*R\"\u0010+\u001a\u00020\b8\u0000@\u0000X\u0080\u000e¢\u0006\u0012\n\u0004\b+\u0010\u001c\u001a\u0004\b,\u0010\u000f\"\u0004\b-\u0010\u001f¨\u00060"}, d2 = {"Lde/d;", "", "Lde/a;", "task", "", "delayNanos", "Lma/f0;", "i", "", "recurrence", "k", "(Lde/a;JZ)Z", "a", "o", "b", "()Z", "", "toString", "Lde/e;", "taskRunner", "Lde/e;", "h", "()Lde/e;", "name", "Ljava/lang/String;", "f", "()Ljava/lang/String;", "shutdown", "Z", "g", "n", "(Z)V", "activeTask", "Lde/a;", "c", "()Lde/a;", "l", "(Lde/a;)V", "", "futureTasks", "Ljava/util/List;", "e", "()Ljava/util/List;", "cancelActiveTask", "d", "m", "<init>", "(Lde/e;Ljava/lang/String;)V", "okhttp"}, k = 1, mv = {1, 6, 0})
/* loaded from: classes2.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    private final TaskRunner f10937a;

    /* renamed from: b, reason: collision with root package name */
    private final String f10938b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f10939c;

    /* renamed from: d, reason: collision with root package name */
    private a f10940d;

    /* renamed from: e, reason: collision with root package name */
    private final List<a> f10941e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f10942f;

    public d(TaskRunner taskRunner, String str) {
        k.e(taskRunner, "taskRunner");
        k.e(str, "name");
        this.f10937a = taskRunner;
        this.f10938b = str;
        this.f10941e = new ArrayList();
    }

    public static /* synthetic */ void j(d dVar, a aVar, long j10, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            j10 = 0;
        }
        dVar.i(aVar, j10);
    }

    public final void a() {
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        synchronized (this.f10937a) {
            if (b()) {
                getF10937a().h(this);
            }
            Unit unit = Unit.f15173a;
        }
    }

    public final boolean b() {
        a aVar = this.f10940d;
        if (aVar != null) {
            k.b(aVar);
            if (aVar.getF10931b()) {
                this.f10942f = true;
            }
        }
        boolean z10 = false;
        int size = this.f10941e.size() - 1;
        if (size >= 0) {
            while (true) {
                int i10 = size - 1;
                if (this.f10941e.get(size).getF10931b()) {
                    a aVar2 = this.f10941e.get(size);
                    if (TaskRunner.f10943h.a().isLoggable(Level.FINE)) {
                        TaskLogger.a(aVar2, this, "canceled");
                    }
                    this.f10941e.remove(size);
                    z10 = true;
                }
                if (i10 < 0) {
                    break;
                }
                size = i10;
            }
        }
        return z10;
    }

    /* renamed from: c, reason: from getter */
    public final a getF10940d() {
        return this.f10940d;
    }

    /* renamed from: d, reason: from getter */
    public final boolean getF10942f() {
        return this.f10942f;
    }

    public final List<a> e() {
        return this.f10941e;
    }

    /* renamed from: f, reason: from getter */
    public final String getF10938b() {
        return this.f10938b;
    }

    /* renamed from: g, reason: from getter */
    public final boolean getF10939c() {
        return this.f10939c;
    }

    /* renamed from: h, reason: from getter */
    public final TaskRunner getF10937a() {
        return this.f10937a;
    }

    public final void i(a aVar, long j10) {
        k.e(aVar, "task");
        synchronized (this.f10937a) {
            if (getF10939c()) {
                if (aVar.getF10931b()) {
                    if (TaskRunner.f10943h.a().isLoggable(Level.FINE)) {
                        TaskLogger.a(aVar, this, "schedule canceled (queue is shutdown)");
                    }
                    return;
                } else {
                    if (TaskRunner.f10943h.a().isLoggable(Level.FINE)) {
                        TaskLogger.a(aVar, this, "schedule failed (queue is shutdown)");
                    }
                    throw new RejectedExecutionException();
                }
            }
            if (k(aVar, j10, false)) {
                getF10937a().h(this);
            }
            Unit unit = Unit.f15173a;
        }
    }

    public final boolean k(a task, long delayNanos, boolean recurrence) {
        String l10;
        k.e(task, "task");
        task.e(this);
        long c10 = this.f10937a.getF10946a().c();
        long j10 = c10 + delayNanos;
        int indexOf = this.f10941e.indexOf(task);
        if (indexOf != -1) {
            if (task.getF10933d() <= j10) {
                if (TaskRunner.f10943h.a().isLoggable(Level.FINE)) {
                    TaskLogger.a(task, this, "already scheduled");
                }
                return false;
            }
            this.f10941e.remove(indexOf);
        }
        task.g(j10);
        if (TaskRunner.f10943h.a().isLoggable(Level.FINE)) {
            if (recurrence) {
                l10 = k.l("run again after ", TaskLogger.b(j10 - c10));
            } else {
                l10 = k.l("scheduled after ", TaskLogger.b(j10 - c10));
            }
            TaskLogger.a(task, this, l10);
        }
        Iterator<a> it = this.f10941e.iterator();
        int i10 = 0;
        while (true) {
            if (!it.hasNext()) {
                i10 = -1;
                break;
            }
            if (it.next().getF10933d() - c10 > delayNanos) {
                break;
            }
            i10++;
        }
        if (i10 == -1) {
            i10 = this.f10941e.size();
        }
        this.f10941e.add(i10, task);
        return i10 == 0;
    }

    public final void l(a aVar) {
        this.f10940d = aVar;
    }

    public final void m(boolean z10) {
        this.f10942f = z10;
    }

    public final void n(boolean z10) {
        this.f10939c = z10;
    }

    public final void o() {
        if (ae.d.f244h && Thread.holdsLock(this)) {
            throw new AssertionError("Thread " + ((Object) Thread.currentThread().getName()) + " MUST NOT hold lock on " + this);
        }
        synchronized (this.f10937a) {
            n(true);
            if (b()) {
                getF10937a().h(this);
            }
            Unit unit = Unit.f15173a;
        }
    }

    public String toString() {
        return this.f10938b;
    }
}
