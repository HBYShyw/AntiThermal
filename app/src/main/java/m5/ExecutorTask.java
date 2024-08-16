package m5;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import kotlin.Metadata;
import ma.Unit;
import s5.b;
import za.k;

/* compiled from: ExecutorTask.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\f\u0010\rJ\u001c\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J\u0016\u0010\n\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002¨\u0006\u000e"}, d2 = {"Lm5/a;", "", "", "widgetCode", "Lkotlin/Function0;", "Lma/f0;", "run", "b", "Ljava/util/concurrent/ExecutorService;", "task", "a", "c", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: m5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class ExecutorTask {

    /* renamed from: b, reason: collision with root package name */
    public static final ExecutorTask f14929b = new ExecutorTask();

    /* renamed from: a, reason: collision with root package name */
    private static final ConcurrentHashMap<String, ExecutorService> f14928a = new ConcurrentHashMap<>();

    /* compiled from: ExecutorTask.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* renamed from: m5.a$a */
    /* loaded from: classes.dex */
    static final class a implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ya.a f14930e;

        a(ya.a aVar) {
            this.f14930e = aVar;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.f14930e.invoke();
        }
    }

    private ExecutorTask() {
    }

    public final void a(String str, ExecutorService executorService) {
        k.e(str, "widgetCode");
        k.e(executorService, "task");
        b.f18066c.c("ExecutorTask", "registerDataTask widgetCode:" + str + " task:" + executorService);
        f14928a.put(str, executorService);
    }

    public final void b(String str, ya.a<Unit> aVar) {
        k.e(str, "widgetCode");
        k.e(aVar, "run");
        ExecutorService executorService = f14928a.get(str);
        if (executorService == null || executorService.submit(new a(aVar)) == null) {
            b.f18066c.e("ExecutorTask", "runOnDataThread widgetCode(" + str + ") is illegal or target card is destroy");
            Unit unit = Unit.f15173a;
        }
    }

    public final void c(String str) {
        k.e(str, "widgetCode");
        f14928a.remove(str);
    }
}
