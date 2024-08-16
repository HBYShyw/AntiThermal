package t5;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;
import gb.KClass;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import ma.h;
import ma.j;
import v5.e;
import v5.f;
import za.Lambda;
import za.Reflection;
import za.k;

/* compiled from: ClientChannel.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0004J\u001e\u0010\r\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u000bR\u001d\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e8\u0006¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0016"}, d2 = {"Lt5/a;", "", "Landroid/content/Context;", "context", "Ljava/util/concurrent/ExecutorService;", "executorService", "Lma/f0;", "c", "", "serverAuthority", "clientName", "Lt5/c;", "client", "e", "", "Lt5/b;", "clientList", "Ljava/util/List;", "b", "()Ljava/util/List;", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: t5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class ClientChannel {

    /* renamed from: d, reason: collision with root package name */
    public static final ClientChannel f18590d = new ClientChannel();

    /* renamed from: a, reason: collision with root package name */
    private static final HandlerThread f18587a = new HandlerThread("DataChannel.ClientChannel");

    /* renamed from: b, reason: collision with root package name */
    private static final AtomicBoolean f18588b = new AtomicBoolean(false);

    /* renamed from: c, reason: collision with root package name */
    private static final List<ClientProxy> f18589c = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientChannel.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Landroid/content/Context;", "a", "()Landroid/content/Context;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.a$a */
    /* loaded from: classes.dex */
    public static final class a extends Lambda implements ya.a<Context> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Context f18591e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(Context context) {
            super(0);
            this.f18591e = context;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final Context invoke() {
            return this.f18591e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientChannel.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lv5/f;", "a", "()Lv5/f;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.a$b */
    /* loaded from: classes.dex */
    public static final class b extends Lambda implements ya.a<f> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f18592e = new b();

        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final f invoke() {
            Looper looper = ClientChannel.a(ClientChannel.f18590d).getLooper();
            k.d(looper, "handlerThread.looper");
            return new f(looper);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ClientChannel.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Ljava/util/concurrent/ExecutorService;", "a", "()Ljava/util/concurrent/ExecutorService;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: t5.a$c */
    /* loaded from: classes.dex */
    public static final class c extends Lambda implements ya.a<ExecutorService> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ ExecutorService f18593e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(ExecutorService executorService) {
            super(0);
            this.f18593e = executorService;
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ExecutorService invoke() {
            return this.f18593e;
        }
    }

    private ClientChannel() {
    }

    public static final /* synthetic */ HandlerThread a(ClientChannel clientChannel) {
        return f18587a;
    }

    public static /* synthetic */ void d(ClientChannel clientChannel, Context context, ExecutorService executorService, int i10, Object obj) {
        if ((i10 & 2) != 0) {
            executorService = Executors.newFixedThreadPool(1);
            k.d(executorService, "Executors.newFixedThreadPool(DEFAULT_THREAD_NUM)");
        }
        clientChannel.c(context, executorService);
    }

    public final List<ClientProxy> b() {
        return f18589c;
    }

    public final void c(Context context, ExecutorService executorService) {
        h<?> b10;
        h<?> b11;
        h<?> b12;
        k.e(context, "context");
        k.e(executorService, "executorService");
        if (f18588b.compareAndSet(false, true)) {
            f18587a.start();
            v5.b bVar = v5.b.f19122c;
            a aVar = new a(context);
            if (bVar.b().get(Reflection.b(Context.class)) == null) {
                ConcurrentHashMap<KClass<?>, h<?>> b13 = bVar.b();
                KClass<?> b14 = Reflection.b(Context.class);
                b10 = j.b(new v5.a(aVar));
                b13.put(b14, b10);
                b bVar2 = b.f18592e;
                if (bVar.b().get(Reflection.b(f.class)) == null) {
                    ConcurrentHashMap<KClass<?>, h<?>> b15 = bVar.b();
                    KClass<?> b16 = Reflection.b(f.class);
                    b11 = j.b(new v5.a(bVar2));
                    b15.put(b16, b11);
                    c cVar = new c(executorService);
                    if (bVar.b().get(Reflection.b(ExecutorService.class)) == null) {
                        ConcurrentHashMap<KClass<?>, h<?>> b17 = bVar.b();
                        KClass<?> b18 = Reflection.b(ExecutorService.class);
                        b12 = j.b(new v5.a(cVar));
                        b17.put(b18, b12);
                        return;
                    }
                    throw new IllegalStateException("Object of the same class type are injected");
                }
                throw new IllegalStateException("Object of the same class type are injected");
            }
            throw new IllegalStateException("Object of the same class type are injected");
        }
    }

    public final void e(String str, String str2, t5.c cVar) {
        k.e(str, "serverAuthority");
        k.e(str2, "clientName");
        k.e(cVar, "client");
        List<ClientProxy> list = f18589c;
        synchronized (list) {
            e eVar = e.f19125b;
            if (eVar.c()) {
                eVar.a("DataChannel.ClientChannel", "initClientImpl serverAuthority = [" + str + "], clientName = [" + str2 + "], client = [" + cVar + ']');
            }
            list.add(new ClientProxy(str, str2, cVar));
        }
    }
}
