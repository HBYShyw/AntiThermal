package l5;

import android.content.Context;
import android.os.Bundle;
import j5.EventPublisher;
import j5.EventSubscriber;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import k5.CardStateEvent;
import kotlin.Metadata;
import ma.h;
import p5.ICardState;
import za.DefaultConstructorMarker;
import za.Reflection;
import za.k;

/* compiled from: CardStateProcessor.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u000eB\u0007¢\u0006\u0004\b\f\u0010\rJ\u0018\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0002H\u0002J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u0002H\u0016J\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\t¨\u0006\u000f"}, d2 = {"Ll5/a;", "Lj5/b;", "Lk5/b;", "Landroid/content/Context;", "context", "event", "Lma/f0;", "c", "b", "Lp5/a;", "state", "d", "<init>", "()V", "a", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: l5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class CardStateProcessor implements EventSubscriber<CardStateEvent> {

    /* renamed from: b, reason: collision with root package name */
    public static final a f14631b = new a(null);

    /* renamed from: a, reason: collision with root package name */
    private final List<ICardState> f14632a = new ArrayList();

    /* compiled from: CardStateProcessor.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\t\u0010\nJ\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002R\u0014\u0010\u0007\u001a\u00020\u00068\u0002X\u0082T¢\u0006\u0006\n\u0004\b\u0007\u0010\b¨\u0006\u000b"}, d2 = {"Ll5/a$a;", "", "Lp5/a;", "state", "Lma/f0;", "a", "", "TAG", "Ljava/lang/String;", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: l5.a$a */
    /* loaded from: classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(ICardState iCardState) {
            k.e(iCardState, "state");
            new CardStateProcessor().d(iCardState);
        }
    }

    public CardStateProcessor() {
        new EventPublisher().b(this);
    }

    private final void c(Context context, CardStateEvent cardStateEvent) {
        ArrayList<String> stringArrayList;
        s5.b.f18066c.d("State.CardStateProcessor", cardStateEvent.getWidgetCode(), "handlerStateEvent event: " + cardStateEvent);
        String state = cardStateEvent.getState();
        switch (state.hashCode()) {
            case -1651322596:
                if (state.equals("observe")) {
                    for (ICardState iCardState : this.f14632a) {
                        Bundle f14049e = cardStateEvent.getF14049e();
                        if (f14049e != null && (stringArrayList = f14049e.getStringArrayList("observe_card_list")) != null) {
                            k.d(stringArrayList, "it");
                            iCardState.f(context, stringArrayList);
                        }
                    }
                    return;
                }
                return;
            case -1352294148:
                if (state.equals("create")) {
                    Iterator<T> it = this.f14632a.iterator();
                    while (it.hasNext()) {
                        ((ICardState) it.next()).e(context, cardStateEvent.getWidgetCode());
                    }
                    return;
                }
                return;
            case -1219769254:
                if (state.equals("subscribed")) {
                    Iterator<T> it2 = this.f14632a.iterator();
                    while (it2.hasNext()) {
                        ((ICardState) it2.next()).d(context, cardStateEvent.getWidgetCode());
                    }
                    return;
                }
                return;
            case -934426579:
                if (state.equals("resume")) {
                    Iterator<T> it3 = this.f14632a.iterator();
                    while (it3.hasNext()) {
                        ((ICardState) it3.next()).m(context, cardStateEvent.getWidgetCode());
                    }
                    return;
                }
                return;
            case 106440182:
                if (state.equals("pause")) {
                    Iterator<T> it4 = this.f14632a.iterator();
                    while (it4.hasNext()) {
                        ((ICardState) it4.next()).j(context, cardStateEvent.getWidgetCode());
                    }
                    return;
                }
                return;
            case 901853107:
                if (state.equals("unsubscribed")) {
                    Iterator<T> it5 = this.f14632a.iterator();
                    while (it5.hasNext()) {
                        ((ICardState) it5.next()).h(context, cardStateEvent.getWidgetCode());
                    }
                    return;
                }
                return;
            case 1557372922:
                if (state.equals("destroy")) {
                    Iterator<T> it6 = this.f14632a.iterator();
                    while (it6.hasNext()) {
                        ((ICardState) it6.next()).k(context, cardStateEvent.getWidgetCode());
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Override // j5.EventSubscriber
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(CardStateEvent cardStateEvent) {
        k.e(cardStateEvent, "event");
        v5.b bVar = v5.b.f19122c;
        if (bVar.b().get(Reflection.b(Context.class)) != null) {
            h<?> hVar = bVar.b().get(Reflection.b(Context.class));
            Objects.requireNonNull(hVar, "null cannot be cast to non-null type kotlin.Lazy<T>");
            c((Context) hVar.getValue(), cardStateEvent);
            return;
        }
        throw new IllegalStateException("the class are not injected");
    }

    public final void d(ICardState iCardState) {
        k.e(iCardState, "state");
        s5.b.f18066c.c("State.CardStateProcessor", "listener state callback: " + iCardState);
        this.f14632a.add(iCardState);
    }
}
