package j5;

import k5.CardEvent;
import kotlin.Metadata;

/* compiled from: EventSubscriber.kt */
@Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\u00020\u0003J\u0017\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00028\u0000H&¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lj5/b;", "Lk5/a;", "T", "", "event", "Lma/f0;", "a", "(Lk5/a;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: j5.b, reason: use source file name */
/* loaded from: classes.dex */
public interface EventSubscriber<T extends CardEvent> {
    void a(T event);
}
