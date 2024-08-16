package com.coui.appcompat.statement;

import android.widget.LinearLayout;
import com.coui.appcompat.checkbox.COUICheckBox;
import kotlin.Metadata;
import v2.PrivacyItem;
import za.k;

/* compiled from: COUICheckBoxItemView.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002R\u0016\u0010\t\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u0007\u0010\bR\u0017\u0010\u000b\u001a\u00020\n8\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lcom/coui/appcompat/statement/COUICheckBoxItemView;", "Landroid/widget/LinearLayout;", "Lcom/coui/appcompat/checkbox/COUICheckBox$b;", "listener", "Lma/f0;", "setOnStateChangeListener", "Lcom/coui/appcompat/checkbox/COUICheckBox;", "e", "Lcom/coui/appcompat/checkbox/COUICheckBox;", "checkBox", "Lv2/a;", "privacyItem", "Lv2/a;", "getPrivacyItem", "()Lv2/a;", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUICheckBoxItemView extends LinearLayout {

    /* renamed from: e, reason: collision with root package name and from kotlin metadata */
    private COUICheckBox checkBox;

    public final PrivacyItem getPrivacyItem() {
        return null;
    }

    public final void setOnStateChangeListener(COUICheckBox.b bVar) {
        k.e(bVar, "listener");
        this.checkBox.setOnStateChangeListener(bVar);
    }
}
