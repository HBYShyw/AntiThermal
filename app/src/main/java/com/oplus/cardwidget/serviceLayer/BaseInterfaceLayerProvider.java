package com.oplus.cardwidget.serviceLayer;

import android.content.Context;
import c5.ICardLayout;
import g5.CardStateEventAggregate;
import gb.l;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import k5.CardStateEvent;
import kotlin.Metadata;
import kotlin.collections.CollectionsJVM;
import l5.CardUpdateProcessor;
import m5.ExecutorTask;
import ma.Unit;
import ma.j;
import r5.IClientFacade;
import s5.CardDataTranslater;
import t5.ClientChannel;
import ya.a;
import z4.CardDataRepository;
import za.Lambda;
import za.PropertyReference0Impl;
import za.Reflection;
import za.k;

/* compiled from: BaseInterfaceLayerProvider.kt */
@Metadata(bv = {}, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 +2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001\u0013B\u0007¢\u0006\u0004\b*\u0010\tJ\b\u0010\u0005\u001a\u00020\u0004H\u0002J\b\u0010\u0006\u001a\u00020\u0004H\u0002J\b\u0010\u0007\u001a\u00020\u0004H\u0002J\u000f\u0010\b\u001a\u00020\u0004H\u0000¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\nH\u0016J$\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\n2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00040\rH\u0016J$\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00102\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00040\rH\u0016J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0010H\u0016J\u0016\u0010\u0016\u001a\u00020\u00042\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\u0014H\u0016J+\u0010\u0019\u001a\u00020\u0004\"\u0004\b\u0000\u0010\u0017*\u00028\u00002\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00040\r¢\u0006\u0004\b\u0019\u0010\u001aR\u001b\u0010 \u001a\u00020\u001b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001fR#\u0010&\u001a\n \"*\u0004\u0018\u00010!0!8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b#\u0010\u001d\u001a\u0004\b$\u0010%R#\u0010)\u001a\n \"*\u0004\u0018\u00010!0!8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b'\u0010\u001d\u001a\u0004\b(\u0010%¨\u0006/²\u0006\u0012\u0010.\u001a\b\u0012\u0004\u0012\u00020-0,8\nX\u008a\u0084\u0002"}, d2 = {"Lcom/oplus/cardwidget/serviceLayer/BaseInterfaceLayerProvider;", "Lcom/oplus/cardwidget/serviceLayer/BaseCardStrategyProvider;", "Lt5/c;", "Lc5/a;", "Lma/f0;", "t", "v", "u", "w", "()V", "", "requestData", "g", "Lkotlin/Function1;", "callback", "l", "", "observeResStr", "b", "a", "", "ids", "c", "T", "run", "x", "(Ljava/lang/Object;Lya/l;)V", "Lg5/b;", "eventAggregate$delegate", "Lma/h;", "r", "()Lg5/b;", "eventAggregate", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "mainCardTask$delegate", "s", "()Ljava/util/concurrent/ExecutorService;", "mainCardTask", "cardDataTask$delegate", "q", "cardDataTask", "<init>", "o", "Lr5/e;", "Lk5/b;", "facade", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public abstract class BaseInterfaceLayerProvider extends BaseCardStrategyProvider implements t5.c, ICardLayout {

    /* renamed from: n, reason: collision with root package name */
    static final /* synthetic */ l[] f9825n = {Reflection.f(new PropertyReference0Impl(BaseInterfaceLayerProvider.class, "facade", "<v#0>", 0))};

    /* renamed from: j, reason: collision with root package name */
    private IClientFacade<CardStateEvent> f9827j;

    /* renamed from: k, reason: collision with root package name */
    private final ma.h f9828k;

    /* renamed from: l, reason: collision with root package name */
    private final ma.h f9829l;

    /* renamed from: m, reason: collision with root package name */
    private final ma.h f9830m;

    /* compiled from: BaseInterfaceLayerProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "a", "()Ljava/util/concurrent/ExecutorService;"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class b extends Lambda implements a<ExecutorService> {

        /* renamed from: e, reason: collision with root package name */
        public static final b f9831e = new b();

        b() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ExecutorService invoke() {
            return Executors.newSingleThreadExecutor();
        }
    }

    /* compiled from: BaseInterfaceLayerProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lg5/b;", "a", "()Lg5/b;"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class c extends Lambda implements a<CardStateEventAggregate> {

        /* renamed from: e, reason: collision with root package name */
        public static final c f9832e = new c();

        c() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final CardStateEventAggregate invoke() {
            return new CardStateEventAggregate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BaseInterfaceLayerProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0003\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    public static final class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            IClientFacade iClientFacade = BaseInterfaceLayerProvider.this.f9827j;
            if (iClientFacade != null) {
                CardUpdateProcessor.f14633b.a(iClientFacade);
            }
        }
    }

    /* compiled from: BaseInterfaceLayerProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "a", "()Ljava/util/concurrent/ExecutorService;"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class e extends Lambda implements a<ExecutorService> {

        /* renamed from: e, reason: collision with root package name */
        public static final e f9834e = new e();

        e() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ExecutorService invoke() {
            return Executors.newSingleThreadExecutor();
        }
    }

    /* compiled from: BaseInterfaceLayerProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Lk5/b;", "it", "Lma/f0;", "a", "(Lk5/b;)V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class f extends Lambda implements ya.l<CardStateEvent, Unit> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: BaseInterfaceLayerProvider.kt */
        @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Lcom/oplus/cardwidget/serviceLayer/BaseInterfaceLayerProvider;", "Lma/f0;", "a", "(Lcom/oplus/cardwidget/serviceLayer/BaseInterfaceLayerProvider;)V"}, k = 3, mv = {1, 4, 2})
        /* loaded from: classes.dex */
        public static final class a extends Lambda implements ya.l<BaseInterfaceLayerProvider, Unit> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ CardStateEvent f9836e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(CardStateEvent cardStateEvent) {
                super(1);
                this.f9836e = cardStateEvent;
            }

            public final void a(BaseInterfaceLayerProvider baseInterfaceLayerProvider) {
                k.e(baseInterfaceLayerProvider, "$receiver");
                CardStateEventAggregate r10 = baseInterfaceLayerProvider.r();
                if (r10 != null) {
                    r10.c(this.f9836e);
                }
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(BaseInterfaceLayerProvider baseInterfaceLayerProvider) {
                a(baseInterfaceLayerProvider);
                return Unit.f15173a;
            }
        }

        f() {
            super(1);
        }

        public final void a(CardStateEvent cardStateEvent) {
            k.e(cardStateEvent, "it");
            BaseInterfaceLayerProvider baseInterfaceLayerProvider = BaseInterfaceLayerProvider.this;
            baseInterfaceLayerProvider.x(baseInterfaceLayerProvider, new a(cardStateEvent));
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(CardStateEvent cardStateEvent) {
            a(cardStateEvent);
            return Unit.f15173a;
        }
    }

    /* compiled from: BaseInterfaceLayerProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Lk5/b;", "it", "Lma/f0;", "a", "(Lk5/b;)V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class g extends Lambda implements ya.l<CardStateEvent, Unit> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* compiled from: BaseInterfaceLayerProvider.kt */
        @Metadata(bv = {}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Lcom/oplus/cardwidget/serviceLayer/BaseInterfaceLayerProvider;", "Lma/f0;", "a", "(Lcom/oplus/cardwidget/serviceLayer/BaseInterfaceLayerProvider;)V"}, k = 3, mv = {1, 4, 2})
        /* loaded from: classes.dex */
        public static final class a extends Lambda implements ya.l<BaseInterfaceLayerProvider, Unit> {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ CardStateEvent f9838e;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(CardStateEvent cardStateEvent) {
                super(1);
                this.f9838e = cardStateEvent;
            }

            public final void a(BaseInterfaceLayerProvider baseInterfaceLayerProvider) {
                k.e(baseInterfaceLayerProvider, "$receiver");
                CardStateEventAggregate r10 = baseInterfaceLayerProvider.r();
                if (r10 != null) {
                    r10.c(this.f9838e);
                }
            }

            @Override // ya.l
            public /* bridge */ /* synthetic */ Unit invoke(BaseInterfaceLayerProvider baseInterfaceLayerProvider) {
                a(baseInterfaceLayerProvider);
                return Unit.f15173a;
            }
        }

        g() {
            super(1);
        }

        public final void a(CardStateEvent cardStateEvent) {
            k.e(cardStateEvent, "it");
            s5.b.f18066c.c("BaseInterfaceLayerProvider", "request: post data");
            BaseInterfaceLayerProvider baseInterfaceLayerProvider = BaseInterfaceLayerProvider.this;
            baseInterfaceLayerProvider.x(baseInterfaceLayerProvider, new a(cardStateEvent));
        }

        @Override // ya.l
        public /* bridge */ /* synthetic */ Unit invoke(CardStateEvent cardStateEvent) {
            a(cardStateEvent);
            return Unit.f15173a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BaseInterfaceLayerProvider.kt */
    @Metadata(bv = {}, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0004\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"T", "Lma/f0;", "run", "()V", "<anonymous>"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    public static final class h implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ Object f9839e;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ ya.l f9840f;

        h(Object obj, ya.l lVar) {
            this.f9839e = obj;
            this.f9840f = lVar;
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.f9840f.invoke(this.f9839e);
        }
    }

    public BaseInterfaceLayerProvider() {
        ma.h b10;
        ma.h b11;
        ma.h b12;
        b10 = j.b(c.f9832e);
        this.f9828k = b10;
        b11 = j.b(e.f9834e);
        this.f9829l = b11;
        b12 = j.b(b.f9831e);
        this.f9830m = b12;
    }

    private final ExecutorService q() {
        return (ExecutorService) this.f9830m.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CardStateEventAggregate r() {
        return (CardStateEventAggregate) this.f9828k.getValue();
    }

    private final ExecutorService s() {
        return (ExecutorService) this.f9829l.getValue();
    }

    private final void t() {
        q().submit(new d());
    }

    private final void u() {
        String canonicalName = getClass().getCanonicalName();
        Context context = getContext();
        if (context != null) {
            ClientChannel clientChannel = ClientChannel.f18590d;
            k.d(context, "it");
            Context applicationContext = context.getApplicationContext();
            k.d(applicationContext, "it.applicationContext");
            ClientChannel.d(clientChannel, applicationContext, null, 2, null);
            k.d(canonicalName, "clientName");
            clientChannel.e("com.oplus.cardservice.repository.provider.CardServiceServerProvider", canonicalName, this);
            s5.b.f18066c.c("BaseInterfaceLayerProvider", "provider create and initial ClientChannel: " + canonicalName);
        }
    }

    private final void v() {
        List<? extends Object> e10;
        v5.b bVar = v5.b.f19122c;
        Object[] objArr = new Object[0];
        if (bVar.a().get(Reflection.b(IClientFacade.class)) != null) {
            ya.l<List<? extends Object>, ?> lVar = bVar.a().get(Reflection.b(IClientFacade.class));
            if (lVar != null) {
                k.d(lVar, "factoryInstanceMap[T::cl…actory are not injected\")");
                e10 = CollectionsJVM.e(objArr);
                this.f9827j = (IClientFacade) new v5.c(lVar.invoke(e10)).a(null, f9825n[0]);
                return;
            }
            throw new IllegalStateException("the factory are not injected");
        }
        throw new IllegalStateException("the class are not injected");
    }

    public void a(String str) {
        Unit unit;
        k.e(str, "observeResStr");
        String d10 = CardDataTranslater.d(str);
        if (d10 != null) {
            CardDataRepository.f20237d.j(d10);
            ExecutorTask.f14929b.c(d10);
            IClientFacade<CardStateEvent> iClientFacade = this.f9827j;
            if (iClientFacade != null) {
                iClientFacade.a(d10);
                unit = Unit.f15173a;
            } else {
                unit = null;
            }
            if (unit != null) {
                return;
            }
        }
        s5.b.f18066c.e("BaseInterfaceLayerProvider", "unObserve widgetCode is error");
        Unit unit2 = Unit.f15173a;
    }

    public void b(String str, ya.l<? super byte[], Unit> lVar) {
        Unit unit;
        k.e(str, "observeResStr");
        k.e(lVar, "callback");
        String d10 = CardDataTranslater.d(str);
        if (d10 != null) {
            ExecutorTask executorTask = ExecutorTask.f14929b;
            ExecutorService q10 = q();
            k.d(q10, "cardDataTask");
            executorTask.a(d10, q10);
            CardDataRepository.f20237d.h(d10, this);
            IClientFacade<CardStateEvent> iClientFacade = this.f9827j;
            if (iClientFacade != null) {
                iClientFacade.b(d10, lVar);
                unit = Unit.f15173a;
            } else {
                unit = null;
            }
            if (unit != null) {
                return;
            }
        }
        s5.b.f18066c.e("BaseInterfaceLayerProvider", "observe widgetCode is error");
        Unit unit2 = Unit.f15173a;
    }

    @Override // t5.c
    public void c(List<String> list) {
        k.e(list, "ids");
        IClientFacade<CardStateEvent> iClientFacade = this.f9827j;
        if (iClientFacade != null) {
            iClientFacade.e(list, new f());
        }
    }

    public void g(byte[] bArr) {
        k.e(bArr, "requestData");
        IClientFacade<CardStateEvent> iClientFacade = this.f9827j;
        if (iClientFacade != null) {
            iClientFacade.c(bArr, new g());
        }
    }

    public void l(byte[] bArr, ya.l<? super byte[], Unit> lVar) {
        k.e(bArr, "requestData");
        k.e(lVar, "callback");
        s5.b.f18066c.c("BaseInterfaceLayerProvider", "requestOnce do nothing ");
    }

    public final void w() {
        s5.b.f18066c.c("BaseInterfaceLayerProvider", "on interface layer initial ...");
        v();
        u();
        t();
    }

    public final <T> void x(T t7, ya.l<? super T, Unit> lVar) {
        k.e(lVar, "run");
        s5.b.f18066c.c("BaseInterfaceLayerProvider", "runOnCardThread:" + t7);
        s().submit(new h(t7, lVar));
    }
}
