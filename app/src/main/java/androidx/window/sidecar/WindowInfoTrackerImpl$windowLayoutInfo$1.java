package androidx.window.sidecar;

import android.app.Activity;
import androidx.core.util.Consumer;
import java.util.concurrent.Executor;
import kotlin.BufferOverflow;
import kotlin.Metadata;
import kotlin.g;
import kotlin.h;
import ma.Unit;
import ma.q;
import qa.d;
import sa.f;
import sa.k;
import wd.FlowCollector;
import ya.p;

/* compiled from: WindowInfoTrackerImpl.kt */
@Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00010\u0000H\u008a@"}, d2 = {"Lwd/c;", "Landroidx/window/layout/WindowLayoutInfo;", "Lma/f0;", "<anonymous>"}, k = 3, mv = {1, 6, 0})
@f(c = "androidx.window.layout.WindowInfoTrackerImpl$windowLayoutInfo$1", f = "WindowInfoTrackerImpl.kt", l = {54, 55}, m = "invokeSuspend")
/* loaded from: classes.dex */
final class WindowInfoTrackerImpl$windowLayoutInfo$1 extends k implements p<FlowCollector<? super WindowLayoutInfo>, d<? super Unit>, Object> {

    /* renamed from: i, reason: collision with root package name */
    Object f4497i;

    /* renamed from: j, reason: collision with root package name */
    Object f4498j;

    /* renamed from: k, reason: collision with root package name */
    int f4499k;

    /* renamed from: l, reason: collision with root package name */
    private /* synthetic */ Object f4500l;

    /* renamed from: m, reason: collision with root package name */
    final /* synthetic */ WindowInfoTrackerImpl f4501m;

    /* renamed from: n, reason: collision with root package name */
    final /* synthetic */ Activity f4502n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WindowInfoTrackerImpl$windowLayoutInfo$1(WindowInfoTrackerImpl windowInfoTrackerImpl, Activity activity, d<? super WindowInfoTrackerImpl$windowLayoutInfo$1> dVar) {
        super(2, dVar);
        this.f4501m = windowInfoTrackerImpl;
        this.f4502n = activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void o(kotlin.f fVar, WindowLayoutInfo windowLayoutInfo) {
        za.k.d(windowLayoutInfo, "info");
        fVar.a(windowLayoutInfo);
    }

    @Override // sa.a
    public final d<Unit> create(Object obj, d<?> dVar) {
        WindowInfoTrackerImpl$windowLayoutInfo$1 windowInfoTrackerImpl$windowLayoutInfo$1 = new WindowInfoTrackerImpl$windowLayoutInfo$1(this.f4501m, this.f4502n, dVar);
        windowInfoTrackerImpl$windowLayoutInfo$1.f4500l = obj;
        return windowInfoTrackerImpl$windowLayoutInfo$1;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x006c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0078 A[Catch: all -> 0x0099, TRY_LEAVE, TryCatch #0 {all -> 0x0099, blocks: (B:7:0x001a, B:9:0x005e, B:14:0x0070, B:16:0x0078, B:25:0x0033, B:27:0x005a), top: B:2:0x0008 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x008d  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:17:0x008a -> B:8:0x001d). Please report as a decompilation issue!!! */
    @Override // sa.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Object c10;
        WindowBackend windowBackend;
        FlowCollector flowCollector;
        Consumer<WindowLayoutInfo> consumer;
        WindowBackend windowBackend2;
        g it;
        FlowCollector flowCollector2;
        WindowBackend windowBackend3;
        Object a10;
        c10 = ra.d.c();
        int i10 = this.f4499k;
        try {
            if (i10 == 0) {
                q.b(obj);
                flowCollector = (FlowCollector) this.f4500l;
                final kotlin.f b10 = h.b(10, BufferOverflow.DROP_OLDEST, null, 4, null);
                consumer = new Consumer() { // from class: androidx.window.layout.b
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj2) {
                        WindowInfoTrackerImpl$windowLayoutInfo$1.o(kotlin.f.this, (WindowLayoutInfo) obj2);
                    }
                };
                windowBackend2 = this.f4501m.windowBackend;
                windowBackend2.b(this.f4502n, new Executor() { // from class: androidx.window.layout.c
                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        runnable.run();
                    }
                }, consumer);
                it = b10.iterator();
                this.f4500l = flowCollector;
                this.f4497i = consumer;
                this.f4498j = it;
                this.f4499k = 1;
                a10 = it.a(this);
                if (a10 == c10) {
                }
            } else if (i10 == 1) {
                it = (g) this.f4498j;
                consumer = (Consumer) this.f4497i;
                flowCollector2 = (FlowCollector) this.f4500l;
                q.b(obj);
                if (!((Boolean) obj).booleanValue()) {
                }
            } else {
                if (i10 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                it = (g) this.f4498j;
                consumer = (Consumer) this.f4497i;
                flowCollector2 = (FlowCollector) this.f4500l;
                q.b(obj);
                flowCollector = flowCollector2;
                this.f4500l = flowCollector;
                this.f4497i = consumer;
                this.f4498j = it;
                this.f4499k = 1;
                a10 = it.a(this);
                if (a10 == c10) {
                    return c10;
                }
                flowCollector2 = flowCollector;
                obj = a10;
                if (!((Boolean) obj).booleanValue()) {
                    WindowLayoutInfo windowLayoutInfo = (WindowLayoutInfo) it.next();
                    this.f4500l = flowCollector2;
                    this.f4497i = consumer;
                    this.f4498j = it;
                    this.f4499k = 2;
                    if (flowCollector2.emit(windowLayoutInfo, this) == c10) {
                        return c10;
                    }
                    flowCollector = flowCollector2;
                    this.f4500l = flowCollector;
                    this.f4497i = consumer;
                    this.f4498j = it;
                    this.f4499k = 1;
                    a10 = it.a(this);
                    if (a10 == c10) {
                    }
                } else {
                    windowBackend3 = this.f4501m.windowBackend;
                    windowBackend3.a(consumer);
                    return Unit.f15173a;
                }
            }
        } catch (Throwable th) {
            windowBackend = this.f4501m.windowBackend;
            windowBackend.a(consumer);
            throw th;
        }
    }

    @Override // ya.p
    /* renamed from: n, reason: merged with bridge method [inline-methods] */
    public final Object invoke(FlowCollector<? super WindowLayoutInfo> flowCollector, d<? super Unit> dVar) {
        return ((WindowInfoTrackerImpl$windowLayoutInfo$1) create(flowCollector, dVar)).invokeSuspend(Unit.f15173a);
    }
}
