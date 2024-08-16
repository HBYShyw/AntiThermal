package k5;

import android.os.Bundle;
import kotlin.Metadata;
import za.k;

/* compiled from: CardUpdateEvent.kt */
@Metadata(bv = {}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\n\u001a\u00020\u0002\u0012\u0006\u0010\u000f\u001a\u00020\u000e¢\u0006\u0004\b\u0013\u0010\u0014J\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006HÖ\u0003R\u0017\u0010\n\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000f\u001a\u00020\u000e8\u0006¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0015"}, d2 = {"Lk5/c;", "Lk5/a;", "", "toString", "", "hashCode", "", "other", "", "equals", "widgetCode", "Ljava/lang/String;", "f", "()Ljava/lang/String;", "Landroid/os/Bundle;", "data", "Landroid/os/Bundle;", "e", "()Landroid/os/Bundle;", "<init>", "(Ljava/lang/String;Landroid/os/Bundle;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: k5.c, reason: use source file name and from toString */
/* loaded from: classes.dex */
public final /* data */ class CardUpdateEvent extends CardEvent {

    /* renamed from: e, reason: collision with root package name and from toString */
    private final String widgetCode;

    /* renamed from: f, reason: collision with root package name and from toString */
    private final Bundle data;

    public CardUpdateEvent(String str, Bundle bundle) {
        k.e(str, "widgetCode");
        k.e(bundle, "data");
        this.widgetCode = str;
        this.data = bundle;
        b(System.currentTimeMillis());
    }

    /* renamed from: e, reason: from getter */
    public final Bundle getData() {
        return this.data;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CardUpdateEvent)) {
            return false;
        }
        CardUpdateEvent cardUpdateEvent = (CardUpdateEvent) other;
        return k.a(this.widgetCode, cardUpdateEvent.widgetCode) && k.a(this.data, cardUpdateEvent.data);
    }

    /* renamed from: f, reason: from getter */
    public final String getWidgetCode() {
        return this.widgetCode;
    }

    public int hashCode() {
        String str = this.widgetCode;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        Bundle bundle = this.data;
        return hashCode + (bundle != null ? bundle.hashCode() : 0);
    }

    public String toString() {
        return "CardUpdateEvent(widgetCode=" + this.widgetCode + ", data=" + this.data + ")";
    }
}
