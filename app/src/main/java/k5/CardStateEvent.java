package k5;

import android.os.Bundle;
import kotlin.Metadata;
import za.k;

/* compiled from: CardStateEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0011\u001a\u00020\u0002\u0012\u0006\u0010\u0015\u001a\u00020\u0002¢\u0006\u0004\b\u0017\u0010\u0018J\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006HÖ\u0003R$\u0010\u000b\u001a\u0004\u0018\u00010\n8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u0013\u0010\u0014R\u0017\u0010\u0015\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u0015\u0010\u0012\u001a\u0004\b\u0016\u0010\u0014¨\u0006\u0019"}, d2 = {"Lk5/b;", "Lk5/a;", "", "toString", "", "hashCode", "", "other", "", "equals", "Landroid/os/Bundle;", "data", "Landroid/os/Bundle;", "e", "()Landroid/os/Bundle;", "h", "(Landroid/os/Bundle;)V", "widgetCode", "Ljava/lang/String;", "g", "()Ljava/lang/String;", "state", "f", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: k5.b, reason: use source file name and from toString */
/* loaded from: classes.dex */
public final /* data */ class CardStateEvent extends CardEvent {

    /* renamed from: e, reason: collision with root package name */
    private Bundle f14049e;

    /* renamed from: f, reason: collision with root package name and from toString */
    private final String widgetCode;

    /* renamed from: g, reason: collision with root package name and from toString */
    private final String state;

    public CardStateEvent(String str, String str2) {
        k.e(str, "widgetCode");
        k.e(str2, "state");
        this.widgetCode = str;
        this.state = str2;
        b(System.currentTimeMillis());
    }

    /* renamed from: e, reason: from getter */
    public final Bundle getF14049e() {
        return this.f14049e;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CardStateEvent)) {
            return false;
        }
        CardStateEvent cardStateEvent = (CardStateEvent) other;
        return k.a(this.widgetCode, cardStateEvent.widgetCode) && k.a(this.state, cardStateEvent.state);
    }

    /* renamed from: f, reason: from getter */
    public final String getState() {
        return this.state;
    }

    /* renamed from: g, reason: from getter */
    public final String getWidgetCode() {
        return this.widgetCode;
    }

    public final void h(Bundle bundle) {
        this.f14049e = bundle;
    }

    public int hashCode() {
        String str = this.widgetCode;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.state;
        return hashCode + (str2 != null ? str2.hashCode() : 0);
    }

    public String toString() {
        return "CardStateEvent(widgetCode=" + this.widgetCode + ", state=" + this.state + ")";
    }
}
