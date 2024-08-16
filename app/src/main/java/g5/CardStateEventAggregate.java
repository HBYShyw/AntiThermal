package g5;

import j5.IEventStore;
import k5.CardStateEvent;
import kotlin.Metadata;
import za.k;

/* compiled from: CardStateEventAggregate.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\b"}, d2 = {"Lg5/b;", "Lg5/a;", "Lk5/b;", "event", "Lma/f0;", "c", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: g5.b, reason: use source file name */
/* loaded from: classes.dex */
public final class CardStateEventAggregate extends BaseCardEventAggregate<CardStateEvent> {

    /* renamed from: c, reason: collision with root package name */
    private final String f11575c = "State.CardStateEventAggregate";

    public void c(CardStateEvent cardStateEvent) {
        k.e(cardStateEvent, "event");
        cardStateEvent.c(System.currentTimeMillis());
        IEventStore<CardStateEvent> b10 = b();
        if (b10 != null) {
            b10.b(cardStateEvent);
        }
        a().a(cardStateEvent);
        s5.b.f18066c.c(this.f11575c, "CardEvent process : " + cardStateEvent);
    }
}
