package h5;

import i5.UpdateLayoutCommand;
import kotlin.Metadata;
import z4.CardDataRepository;
import za.k;

/* compiled from: UpdateLayoutCommandHandler.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\b"}, d2 = {"Lh5/b;", "", "Li5/c;", "command", "Lma/f0;", "a", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: h5.b, reason: use source file name */
/* loaded from: classes.dex */
public final class UpdateLayoutCommandHandler {

    /* renamed from: a, reason: collision with root package name */
    private final String f11998a = "Update.SwitchLayoutCommandHandler";

    public void a(UpdateLayoutCommand updateLayoutCommand) {
        k.e(updateLayoutCommand, "command");
        s5.b.f18066c.d(this.f11998a, updateLayoutCommand.getWidgetCode(), "handle command is: " + updateLayoutCommand);
        CardDataRepository cardDataRepository = CardDataRepository.f20237d;
        cardDataRepository.i(updateLayoutCommand.getWidgetCode(), null);
        cardDataRepository.k(updateLayoutCommand.getWidgetCode(), null);
        cardDataRepository.l(updateLayoutCommand.getWidgetCode(), updateLayoutCommand.getLayoutName());
        updateLayoutCommand.a(System.currentTimeMillis());
    }
}
