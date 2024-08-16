package g5;

import j5.EventPublisher;
import j5.IEventStore;
import k5.CardEvent;
import kotlin.Metadata;
import ma.h;
import ma.j;
import za.Lambda;

/* compiled from: BaseCardEventAggregate.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\b&\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\b\u0012\u0004\u0012\u00028\u00000\u0003B\u0007¢\u0006\u0004\b\u000f\u0010\u0010R\u001f\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00048\u0006¢\u0006\f\n\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR!\u0010\u000e\u001a\b\u0012\u0004\u0012\u00028\u00000\t8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r¨\u0006\u0011"}, d2 = {"Lg5/a;", "Lk5/a;", "E", "", "Lj5/d;", "eventStore", "Lj5/d;", "b", "()Lj5/d;", "Lj5/a;", "cardEventPublisher$delegate", "Lma/h;", "a", "()Lj5/a;", "cardEventPublisher", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: g5.a, reason: use source file name */
/* loaded from: classes.dex */
public abstract class BaseCardEventAggregate<E extends CardEvent> {

    /* renamed from: a, reason: collision with root package name */
    private final IEventStore<E> f11572a;

    /* renamed from: b, reason: collision with root package name */
    private final h f11573b;

    /* compiled from: BaseCardEventAggregate.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\"\b\b\u0000\u0010\u0001*\u00020\u0000H\n¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"Lk5/a;", "E", "Lj5/a;", "a", "()Lj5/a;"}, k = 3, mv = {1, 4, 2})
    /* renamed from: g5.a$a */
    /* loaded from: classes.dex */
    static final class a extends Lambda implements ya.a<EventPublisher<E>> {

        /* renamed from: e, reason: collision with root package name */
        public static final a f11574e = new a();

        a() {
            super(0);
        }

        @Override // ya.a
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public final EventPublisher<E> invoke() {
            return new EventPublisher<>();
        }
    }

    public BaseCardEventAggregate() {
        h b10;
        b10 = j.b(a.f11574e);
        this.f11573b = b10;
    }

    public final EventPublisher<E> a() {
        return (EventPublisher) this.f11573b.getValue();
    }

    public final IEventStore<E> b() {
        return this.f11572a;
    }
}
