package b5;

import java.util.Map;
import kotlin.Metadata;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: CardAction.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010$\n\u0002\b\u0007\b\u0086\b\u0018\u00002\u00020\u0001:\u0001\u000fB-\u0012\u0006\u0010\t\u001a\u00020\u0002\u0012\u0006\u0010\r\u001a\u00020\u0004\u0012\u0014\u0010\u0012\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u0011¢\u0006\u0004\b\u0016\u0010\u0017J\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\b\u001a\u00020\u00072\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001HÖ\u0003R\u0017\u0010\t\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\u00020\u00048\u0006¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R%\u0010\u0012\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u00118\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015¨\u0006\u0018"}, d2 = {"Lb5/a;", "", "", "toString", "", "hashCode", "other", "", "equals", "widgetCode", "Ljava/lang/String;", "c", "()Ljava/lang/String;", "action", "I", "a", "()I", "", "param", "Ljava/util/Map;", "b", "()Ljava/util/Map;", "<init>", "(Ljava/lang/String;ILjava/util/Map;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: b5.a, reason: from toString */
/* loaded from: classes.dex */
public final /* data */ class CardAction {

    /* renamed from: d, reason: collision with root package name */
    public static final C0012a f4567d = new C0012a(null);

    /* renamed from: a, reason: collision with root package name and from toString */
    private final String widgetCode;

    /* renamed from: b, reason: collision with root package name and from toString */
    private final int action;

    /* renamed from: c, reason: collision with root package name and from toString */
    private final Map<String, String> param;

    /* compiled from: CardAction.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0003\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0003\u0010\u0004R\u0014\u0010\u0005\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0005\u0010\u0004R\u0014\u0010\u0006\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0006\u0010\u0004R\u0014\u0010\u0007\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u0007\u0010\u0004R\u0014\u0010\b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\b\u0010\u0004R\u0014\u0010\t\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\t\u0010\u0004R\u0014\u0010\n\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\n\u0010\u0004R\u0014\u0010\u000b\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\u0004R\u0014\u0010\f\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\f\u0010\u0004R\u0014\u0010\r\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\r\u0010\u0004R\u0014\u0010\u000e\u001a\u00020\u00028\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000e\u0010\u0004¨\u0006\u0011"}, d2 = {"Lb5/a$a;", "", "", "ACTION_CARD_UPDATE_REQUEST", "Ljava/lang/String;", "KEY_DATA_OBSERVE_CARD_LIST", "LIFE_CIRCLE_KEY", "LIFE_CIRCLE_VALUE_CARD_OBSERVE", "LIFE_CIRCLE_VALUE_CREATE", "LIFE_CIRCLE_VALUE_DESTROY", "LIFE_CIRCLE_VALUE_PAUSE", "LIFE_CIRCLE_VALUE_RESUME", "LIFE_CIRCLE_VALUE_SUBSCRIBED", "LIFE_CIRCLE_VALUE_UNSUBSCRIBED", "WIDGET_ID_KEY", "<init>", "()V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
    /* renamed from: b5.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static final class C0012a {
        private C0012a() {
        }

        public /* synthetic */ C0012a(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public CardAction(String str, int i10, Map<String, String> map) {
        k.e(str, "widgetCode");
        this.widgetCode = str;
        this.action = i10;
        this.param = map;
    }

    /* renamed from: a, reason: from getter */
    public final int getAction() {
        return this.action;
    }

    public final Map<String, String> b() {
        return this.param;
    }

    /* renamed from: c, reason: from getter */
    public final String getWidgetCode() {
        return this.widgetCode;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CardAction)) {
            return false;
        }
        CardAction cardAction = (CardAction) other;
        return k.a(this.widgetCode, cardAction.widgetCode) && this.action == cardAction.action && k.a(this.param, cardAction.param);
    }

    public int hashCode() {
        String str = this.widgetCode;
        int hashCode = (((str != null ? str.hashCode() : 0) * 31) + Integer.hashCode(this.action)) * 31;
        Map<String, String> map = this.param;
        return hashCode + (map != null ? map.hashCode() : 0);
    }

    public String toString() {
        return "CardAction(widgetCode=" + this.widgetCode + ", action=" + this.action + ", param=" + this.param + ")";
    }
}
