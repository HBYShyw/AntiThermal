package r5;

import j5.IClientEvent;
import java.util.List;
import k5.CardEvent;
import k5.CardStateEvent;
import kotlin.Metadata;
import ma.Unit;
import ya.l;

/* compiled from: IClientFacade.kt */
@Metadata(bv = {}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0003J$\u0010\t\u001a\u00020\u00072\u0006\u0010\u0005\u001a\u00020\u00042\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00070\u0006H&J$\u0010\r\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\n2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0006H&J*\u0010\u0011\u001a\u00020\u00072\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u000e2\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00070\u0006H&J\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\nH&¨\u0006\u0013"}, d2 = {"Lr5/e;", "Lk5/a;", "T", "Lj5/c;", "", "reqData", "Lkotlin/Function1;", "Lma/f0;", "call", "c", "", "widgetCode", "callback", "b", "", "observeIds", "Lk5/b;", "e", "a", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: r5.e, reason: use source file name */
/* loaded from: classes.dex */
public interface IClientFacade<T extends CardEvent> extends IClientEvent {
    void a(String str);

    void b(String str, l<? super byte[], Unit> lVar);

    void c(byte[] bArr, l<? super T, Unit> lVar);

    void e(List<String> list, l<? super CardStateEvent, Unit> lVar);
}
