package f5;

import android.content.Context;
import h5.CardUpdateCommandHandler;
import h5.UpdateLayoutCommandHandler;
import i5.CardUpdateCommand;
import i5.UpdateLayoutCommand;
import kotlin.Metadata;
import m5.ExecutorTask;
import ma.Unit;
import n5.BaseDataPack;
import za.Lambda;
import za.k;

/* compiled from: CardWidgetAction.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ \u0010\t\u001a\u00020\b2\b\u0010\u0003\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0006J\u0016\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0006¨\u0006\u000f"}, d2 = {"Lf5/a;", "", "Landroid/content/Context;", "context", "Ln5/a;", "data", "", "widgetCode", "Li5/b;", "a", "layoutName", "Li5/c;", "b", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: f5.a, reason: use source file name */
/* loaded from: classes.dex */
public final class CardWidgetAction {

    /* renamed from: a, reason: collision with root package name */
    public static final CardWidgetAction f11356a = new CardWidgetAction();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CardWidgetAction.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "a", "()V"}, k = 3, mv = {1, 4, 2})
    /* renamed from: f5.a$a */
    /* loaded from: classes.dex */
    public static final class a extends Lambda implements ya.a<Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ CardUpdateCommand f11357e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(CardUpdateCommand cardUpdateCommand) {
            super(0);
            this.f11357e = cardUpdateCommand;
        }

        public final void a() {
            new CardUpdateCommandHandler().a(this.f11357e);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    /* compiled from: CardWidgetAction.kt */
    @Metadata(bv = {}, d1 = {"\u0000\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0001\u001a\u00020\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002"}, d2 = {"Lma/f0;", "a", "()V"}, k = 3, mv = {1, 4, 2})
    /* renamed from: f5.a$b */
    /* loaded from: classes.dex */
    static final class b extends Lambda implements ya.a<Unit> {

        /* renamed from: e, reason: collision with root package name */
        final /* synthetic */ UpdateLayoutCommand f11358e;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        b(UpdateLayoutCommand updateLayoutCommand) {
            super(0);
            this.f11358e = updateLayoutCommand;
        }

        public final void a() {
            new UpdateLayoutCommandHandler().a(this.f11358e);
        }

        @Override // ya.a
        public /* bridge */ /* synthetic */ Unit invoke() {
            a();
            return Unit.f15173a;
        }
    }

    private CardWidgetAction() {
    }

    public final CardUpdateCommand a(Context context, BaseDataPack data, String widgetCode) {
        k.e(data, "data");
        k.e(widgetCode, "widgetCode");
        CardUpdateCommand cardUpdateCommand = new CardUpdateCommand(widgetCode, data);
        Thread currentThread = Thread.currentThread();
        k.d(currentThread, "Thread.currentThread()");
        cardUpdateCommand.c(currentThread.getName());
        ExecutorTask.f14929b.b(widgetCode, new a(cardUpdateCommand));
        s5.b.f18066c.c("CardWidgetAction", "postUpdateCommand widgetCode: " + widgetCode + " data is " + data);
        return cardUpdateCommand;
    }

    public final UpdateLayoutCommand b(String widgetCode, String layoutName) {
        k.e(widgetCode, "widgetCode");
        k.e(layoutName, "layoutName");
        UpdateLayoutCommand updateLayoutCommand = new UpdateLayoutCommand(widgetCode, layoutName);
        Thread currentThread = Thread.currentThread();
        k.d(currentThread, "Thread.currentThread()");
        updateLayoutCommand.c(currentThread.getName());
        ExecutorTask.f14929b.b(widgetCode, new b(updateLayoutCommand));
        s5.b.f18066c.c("CardWidgetAction", "switchLayoutCommand widgetCode:" + widgetCode + " layoutName:" + layoutName);
        return updateLayoutCommand;
    }
}
