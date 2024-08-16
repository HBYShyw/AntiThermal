package r5;

import android.os.Bundle;
import b5.CardAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import k5.CardStateEvent;
import kotlin.Metadata;
import ma.Unit;
import ma.h;
import ma.j;
import s5.CardDataTranslater;
import ya.l;
import za.Lambda;
import za.Reflection;
import za.k;

/* compiled from: CardClientFacade.kt */
@Metadata(bv = {}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007¢\u0006\u0004\b\u001e\u0010\u001fJ\u0016\u0010\u0006\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0002J$\u0010\u000b\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00072\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00040\tH\u0016J$\u0010\u000f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\f2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00040\tH\u0016J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\fH\u0016J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0011H\u0016J*\u0010\u0016\u001a\u00020\u00042\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u00142\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00040\tH\u0016R#\u0010\u001d\u001a\n \u0018*\u0004\u0018\u00010\u00170\u00178BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c¨\u0006 "}, d2 = {"Lr5/a;", "Lr5/e;", "Lk5/b;", "Lkotlin/Function0;", "Lma/f0;", "run", "i", "", "reqData", "Lkotlin/Function1;", "call", "c", "", "widgetCode", "callback", "b", "a", "Landroid/os/Bundle;", "data", "d", "", "observeIds", "e", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "dataTask$delegate", "Lma/h;", "h", "()Ljava/util/concurrent/ExecutorService;", "dataTask", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* loaded from: classes.dex */
public final class a implements IClientFacade<CardStateEvent> {

    /* renamed from: a, reason: collision with root package name */
    private final String f17514a = "Facade.CardClientFacade";

    /* renamed from: b, reason: collision with root package name */
    private final h f17515b;

    /* renamed from: c, reason: collision with root package name */
    private final Map<String, l<byte[], Unit>> f17516c;

    /* compiled from: CardClientFacade.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0002\u001a\n \u0001*\u0004\u0018\u00010\u00000\u0000H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "a", "()Ljava/util/concurrent/ExecutorService;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: r5.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    static final class C0096a extends Lambda implements ya.a<ExecutorService> {

        /* renamed from: e, reason: collision with root package name */
        public static final C0096a f17517e = new C0096a();

        C0096a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final ExecutorService invoke() {
            return Executors.newSingleThreadExecutor();
        }
    }

    /* compiled from: CardClientFacade.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "a", "()V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class b extends Lambda implements ya.a<Unit> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ l f17519f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ String f17520g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(l lVar, String str) {
            super(0);
            this.f17519f = lVar;
            this.f17520g = str;
        }

        public final void a() {
            a.this.f17516c.put(this.f17520g, this.f17519f);
            s5.b.f18066c.c(a.this.f17514a, "--observe : widgetCode : " + this.f17520g);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* compiled from: CardClientFacade.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "a", "()V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class c extends Lambda implements ya.a<Unit> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ List f17522f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ l f17523g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(List list, l lVar) {
            super(0);
            this.f17522f = list;
            this.f17523g = lVar;
        }

        public final void a() {
            s5.b.f18066c.c(a.this.f17514a, "observes ids size is:" + this.f17522f.size());
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator it = this.f17522f.iterator();
            while (it.hasNext()) {
                String d10 = CardDataTranslater.d((String) it.next());
                if (d10 != null) {
                    arrayList.add(d10);
                }
            }
            CardStateEvent cardStateEvent = new CardStateEvent("", "observe");
            cardStateEvent.h(new Bundle());
            Bundle f14049e = cardStateEvent.getF14049e();
            if (f14049e != null) {
                f14049e.putStringArrayList("observe_card_list", arrayList);
            }
            StringBuilder sb2 = new StringBuilder();
            Thread currentThread = Thread.currentThread();
            k.d(currentThread, "Thread.currentThread()");
            sb2.append(currentThread.getName());
            sb2.append(a.this.f17514a);
            cardStateEvent.d(sb2.toString());
            cardStateEvent.b(System.currentTimeMillis());
            this.f17523g.invoke(cardStateEvent);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* compiled from: CardClientFacade.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "a", "()V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class d extends Lambda implements ya.a<Unit> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ Bundle f17525f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(Bundle bundle) {
            super(0);
            this.f17525f = bundle;
        }

        public final void a() {
            Unit unit;
            String string = this.f17525f.getString("widget_code");
            if (string != null) {
                l lVar = (l) a.this.f17516c.get(string);
                s5.b bVar = s5.b.f18066c;
                String str = a.this.f17514a;
                k.d(string, "widgetCode");
                bVar.d(str, string, "post result to service: " + lVar);
                if (lVar != null) {
                    v5.b bVar2 = v5.b.f19122c;
                    if (bVar2.b().get(Reflection.b(IDataHandle.class)) != null) {
                        h<?> hVar = bVar2.b().get(Reflection.b(IDataHandle.class));
                        Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
                        lVar.invoke(((IDataHandle) hVar.getValue()).b(this.f17525f));
                        unit = Unit.f15173a;
                    } else {
                        throw new IllegalStateException("the class are not injected");
                    }
                } else {
                    unit = null;
                }
                if (unit != null) {
                    return;
                }
            }
            s5.b.f18066c.e(a.this.f17514a, "widgetCode is null when post data");
            Unit unit2 = Unit.f15173a;
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* compiled from: CardClientFacade.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "a", "()V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class e extends Lambda implements ya.a<Unit> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ byte[] f17527f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ l f17528g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        e(byte[] bArr, l lVar) {
            super(0);
            this.f17527f = bArr;
            this.f17528g = lVar;
        }

        public final void a() {
            Map<String, String> b10;
            String str;
            v5.b bVar = v5.b.f19122c;
            if (bVar.b().get(Reflection.b(IDataHandle.class)) != null) {
                h<?> hVar = bVar.b().get(Reflection.b(IDataHandle.class));
                Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
                CardAction a10 = ((IDataHandle) hVar.getValue()).a(this.f17527f);
                if (a10.getAction() != 2 || (b10 = a10.b()) == null || (str = b10.get("life_circle")) == null) {
                    return;
                }
                CardStateEvent cardStateEvent = new CardStateEvent(a10.getWidgetCode(), str);
                StringBuilder sb2 = new StringBuilder();
                Thread currentThread = Thread.currentThread();
                k.d(currentThread, "Thread.currentThread()");
                sb2.append(currentThread.getName());
                sb2.append(a.this.f17514a);
                cardStateEvent.d(sb2.toString());
                cardStateEvent.b(System.currentTimeMillis());
                this.f17528g.invoke(cardStateEvent);
                s5.b.f18066c.d(a.this.f17514a, a10.getWidgetCode(), "request action: " + str);
                return;
            }
            throw new IllegalStateException("the class are not injected");
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* compiled from: CardClientFacade.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "a", "()V"}, k = 3, mv = {1, 4, 2})
    /* loaded from: classes.dex */
    static final class f extends Lambda implements ya.a<Unit> {

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f17530f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        f(String str) {
            super(0);
            this.f17530f = str;
        }

        public final void a() {
            s5.b.f18066c.c(a.this.f17514a, "--unObserve : widgetCode : " + this.f17530f);
            a.this.f17516c.remove(this.f17530f);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    public a() {
        h b10;
        b10 = j.b(C0096a.f17517e);
        this.f17515b = b10;
        this.f17516c = new LinkedHashMap();
    }

    private final ExecutorService h() {
        return (ExecutorService) this.f17515b.getValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [r5.b] */
    private final void i(ya.a<Unit> aVar) {
        ExecutorService h10 = h();
        if (aVar != null) {
            aVar = new r5.b(aVar);
        }
        h10.submit((Runnable) aVar);
    }

    @Override // r5.IClientFacade
    public void a(String str) {
        k.e(str, "widgetCode");
        i(new f(str));
    }

    @Override // r5.IClientFacade
    public void b(String str, l<? super byte[], Unit> lVar) {
        k.e(str, "widgetCode");
        k.e(lVar, "callback");
        i(new b(lVar, str));
    }

    @Override // r5.IClientFacade
    public void c(byte[] bArr, l<? super CardStateEvent, Unit> lVar) {
        k.e(bArr, "reqData");
        k.e(lVar, "call");
        i(new e(bArr, lVar));
    }

    @Override // j5.IClientEvent
    public void d(Bundle bundle) {
        k.e(bundle, "data");
        i(new d(bundle));
    }

    @Override // r5.IClientFacade
    public void e(List<String> list, l<? super CardStateEvent, Unit> lVar) {
        k.e(list, "observeIds");
        k.e(lVar, "call");
        i(new c(list, lVar));
    }
}
