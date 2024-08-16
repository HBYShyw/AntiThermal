package h5;

import android.os.Bundle;
import g5.CardUpdateEventAggregate;
import i5.CardUpdateCommand;
import k5.CardUpdateEvent;
import kotlin.Metadata;
import ma.o;
import n5.BaseDataPack;
import z4.CardDataRepository;
import za.k;

/* compiled from: CardUpdateCommandHandler.kt */
@Metadata(bv = {}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H\u0002J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0002H\u0016¨\u0006\r"}, d2 = {"Lh5/a;", "", "Li5/b;", "", "widgetCode", "Landroid/os/Bundle;", "bundle", "Lma/f0;", "b", "command", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: h5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class CardUpdateCommandHandler {

    /* renamed from: a, reason: collision with root package name */
    private final String f11997a = "Update.CardUpdateCommandHandler";

    private final void b(String str, Bundle bundle) {
        CardUpdateEventAggregate cardUpdateEventAggregate = new CardUpdateEventAggregate();
        CardUpdateEvent cardUpdateEvent = new CardUpdateEvent(str, bundle);
        StringBuilder sb2 = new StringBuilder();
        Thread currentThread = Thread.currentThread();
        k.d(currentThread, "Thread.currentThread()");
        sb2.append(currentThread.getName());
        sb2.append(this.f11997a);
        cardUpdateEvent.d(sb2.toString());
        cardUpdateEventAggregate.c(cardUpdateEvent);
    }

    public void a(CardUpdateCommand cardUpdateCommand) {
        k.e(cardUpdateCommand, "command");
        s5.b bVar = s5.b.f18066c;
        bVar.d(this.f11997a, cardUpdateCommand.getWidgetCode(), "handle command: " + cardUpdateCommand);
        CardDataRepository cardDataRepository = CardDataRepository.f20237d;
        o<byte[], Boolean> g6 = cardDataRepository.g(cardUpdateCommand.getWidgetCode());
        if (g6.c() == null) {
            bVar.e(this.f11997a, "command handle interrupt");
            return;
        }
        BaseDataPack data = cardUpdateCommand.getData();
        String widgetCode = cardUpdateCommand.getWidgetCode();
        byte[] c10 = g6.c();
        k.b(c10);
        Bundle e10 = data.e(widgetCode, c10, g6.d().booleanValue());
        if (e10 != null) {
            e10.putString("layoutName", cardDataRepository.d(cardUpdateCommand.getWidgetCode()));
            cardUpdateCommand.a(System.currentTimeMillis());
            b(cardUpdateCommand.getWidgetCode(), e10);
            return;
        }
        bVar.c(this.f11997a, "command is not be consumed");
    }
}
