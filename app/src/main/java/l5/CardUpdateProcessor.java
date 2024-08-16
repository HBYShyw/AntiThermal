package l5;

import j5.EventPublisher;
import j5.EventSubscriber;
import j5.IClientEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import k5.CardUpdateEvent;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CardUpdateProcessor.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u000bB\u0007¢\u0006\u0004\b\t\u0010\nJ\u000e\u0010\u0005\u001a\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u0003J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0002H\u0016¨\u0006\f"}, d2 = {"Ll5/b;", "Lj5/b;", "Lk5/c;", "Lj5/c;", "iClient", "c", "event", "Lma/f0;", "b", "<init>", "()V", "a", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: l5.b, reason: use source file name */
/* loaded from: classes.dex */
public final class CardUpdateProcessor implements EventSubscriber<CardUpdateEvent> {

    /* renamed from: b, reason: collision with root package name */
    public static final a f14633b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final List<IClientEvent> f14634a = new ArrayList();

    /* compiled from: CardUpdateProcessor.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\u0007\u001a\u00020\u00068\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0007\u0010\b¨\u0006\u000b"}, d2 = {"Ll5/b$a;", "", "Lj5/c;", "iClient", "Lma/f0;", "a", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: l5.b$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(IClientEvent iClientEvent) {
            k.e(iClientEvent, "iClient");
            new EventPublisher().b(new CardUpdateProcessor().c(iClientEvent));
        }
    }

    @Override // j5.EventSubscriber
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(CardUpdateEvent cardUpdateEvent) {
        k.e(cardUpdateEvent, "event");
        s5.b.f18066c.d("Update.CardUpdateProcessor", cardUpdateEvent.getWidgetCode(), "handleEvent event begin...");
        Iterator<T> it = this.f14634a.iterator();
        while (it.hasNext()) {
            ((IClientEvent) it.next()).d(cardUpdateEvent.getData());
        }
    }

    public final CardUpdateProcessor c(IClientEvent iClient) {
        k.e(iClient, "iClient");
        s5.b.f18066c.c("Update.CardUpdateProcessor", "listener state callback: " + iClient);
        this.f14634a.add(iClient);
        return this;
    }
}
