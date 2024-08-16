package j5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import k5.CardEvent;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: EventPublisher.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0003:\u0001\rB\u0007¢\u0006\u0004\b\u000b\u0010\fJ\u0015\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00028\u0000¢\u0006\u0004\b\u0006\u0010\u0007J\u0014\u0010\n\u001a\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\b¨\u0006\u000e"}, d2 = {"Lj5/a;", "Lk5/a;", "T", "", "event", "Lma/f0;", "a", "(Lk5/a;)V", "Lj5/b;", "subscriber", "b", "<init>", "()V", "c", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: j5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class EventPublisher<T extends CardEvent> {

    /* renamed from: c, reason: collision with root package name */
    public static final c f13010c = new c(null);

    /* renamed from: a, reason: collision with root package name */
    private static final ThreadLocal<List<EventSubscriber<CardEvent>>> f13008a = new b();

    /* renamed from: b, reason: collision with root package name */
    private static final ThreadLocal<Boolean> f13009b = new a();

    /* compiled from: EventPublisher.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u000f\u0010\u0003\u001a\u00020\u0002H\u0014¢\u0006\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"j5/a$a", "Ljava/lang/ThreadLocal;", "", "a", "()Ljava/lang/Boolean;", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: j5.a$a */
    /* loaded from: classes.dex */
    public static final class a extends ThreadLocal<Boolean> {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Boolean initialValue() {
            return Boolean.FALSE;
        }
    }

    /* compiled from: EventPublisher.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u00020\u0001J\u0014\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u0002H\u0014¨\u0006\u0006"}, d2 = {"j5/a$b", "Ljava/lang/ThreadLocal;", "", "Lj5/b;", "Lk5/a;", "a", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: j5.a$b */
    /* loaded from: classes.dex */
    public static final class b extends ThreadLocal<List<EventSubscriber<CardEvent>>> {
        b() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public List<EventSubscriber<CardEvent>> initialValue() {
            return new ArrayList();
        }
    }

    /* compiled from: EventPublisher.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0003\u001a\u00020\u00028\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004¨\u0006\u0007"}, d2 = {"Lj5/a$c;", "", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: j5.a$c */
    /* loaded from: classes.dex */
    public static final class c {
        private c() {
        }

        public /* synthetic */ c(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public final void a(T event) {
        k.e(event, "event");
        ThreadLocal<Boolean> threadLocal = f13009b;
        Boolean bool = threadLocal.get();
        k.d(bool, "isPublishing.get()");
        if (bool.booleanValue()) {
            s5.b.f18066c.c("EventPublisher", "is publishing, not publish again");
            return;
        }
        try {
            threadLocal.set(Boolean.TRUE);
            s5.b.f18066c.c("EventPublisher", "event is publishing...");
            List<EventSubscriber<CardEvent>> list = f13008a.get();
            if (list != null) {
                Iterator<T> it = list.iterator();
                while (it.hasNext()) {
                    ((EventSubscriber) it.next()).a(event);
                }
            }
        } finally {
            f13009b.set(Boolean.FALSE);
        }
    }

    public final void b(EventSubscriber<T> eventSubscriber) {
        k.e(eventSubscriber, "subscriber");
        if (!f13009b.get().booleanValue()) {
            s5.b.f18066c.c("EventPublisher", "subscribe...");
            f13008a.get().add(eventSubscriber);
        } else {
            s5.b.f18066c.c("EventPublisher", "is publishing, not allow subscribe");
        }
    }
}
