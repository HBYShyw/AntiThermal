package i5;

import kotlin.Metadata;
import za.k;

/* compiled from: UpdateLayoutCommand.kt */
@Metadata(bv = {}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\n\u001a\u00020\u0002\u0012\u0006\u0010\u000e\u001a\u00020\u0002¢\u0006\u0004\b\u0010\u0010\u0011J\t\u0010\u0003\u001a\u00020\u0002HÖ\u0001J\t\u0010\u0005\u001a\u00020\u0004HÖ\u0001J\u0013\u0010\t\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006HÖ\u0003R\u0017\u0010\n\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\u00020\u00028\u0006¢\u0006\f\n\u0004\b\u000e\u0010\u000b\u001a\u0004\b\u000f\u0010\r¨\u0006\u0012"}, d2 = {"Li5/c;", "Li5/a;", "", "toString", "", "hashCode", "", "other", "", "equals", "widgetCode", "Ljava/lang/String;", "e", "()Ljava/lang/String;", "layoutName", "d", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", "com.oplus.card.widget.cardwidget"}, k = 1, mv = {1, 4, 2})
/* renamed from: i5.c, reason: use source file name and from toString */
/* loaded from: classes.dex */
public final /* data */ class UpdateLayoutCommand extends BaseCardCommand {

    /* renamed from: d, reason: collision with root package name and from toString */
    private final String widgetCode;

    /* renamed from: e, reason: collision with root package name and from toString */
    private final String layoutName;

    public UpdateLayoutCommand(String str, String str2) {
        k.e(str, "widgetCode");
        k.e(str2, "layoutName");
        this.widgetCode = str;
        this.layoutName = str2;
        b(System.currentTimeMillis());
    }

    /* renamed from: d, reason: from getter */
    public final String getLayoutName() {
        return this.layoutName;
    }

    /* renamed from: e, reason: from getter */
    public final String getWidgetCode() {
        return this.widgetCode;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UpdateLayoutCommand)) {
            return false;
        }
        UpdateLayoutCommand updateLayoutCommand = (UpdateLayoutCommand) other;
        return k.a(this.widgetCode, updateLayoutCommand.widgetCode) && k.a(this.layoutName, updateLayoutCommand.layoutName);
    }

    public int hashCode() {
        String str = this.widgetCode;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.layoutName;
        return hashCode + (str2 != null ? str2.hashCode() : 0);
    }

    public String toString() {
        return "UpdateLayoutCommand(widgetCode=" + this.widgetCode + ", layoutName=" + this.layoutName + ")";
    }
}
