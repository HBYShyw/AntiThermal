package com.coui.appcompat.privacypolicy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import i2.a;
import kotlin.Metadata;
import w1.COUIDarkModeUtil;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUIPrivacyPolicyView.kt */
@Metadata(bv = {}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u00002\u00020\u0001:\u0001\u0011B1\b\u0007\u0012\u0006\u0010\n\u001a\u00020\t\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0002\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0002¢\u0006\u0004\b\u000f\u0010\u0010J&\u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u0002¨\u0006\u0012"}, d2 = {"Lcom/coui/appcompat/privacypolicy/COUIPrivacyPolicyView;", "Landroid/widget/LinearLayout;", "", "left", "top", "right", "bottom", "Lma/f0;", "setPadding", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "Section", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class COUIPrivacyPolicyView extends LinearLayout {

    /* compiled from: COUIPrivacyPolicyView.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016¨\u0006\n"}, d2 = {"Lcom/coui/appcompat/privacypolicy/COUIPrivacyPolicyView$Section;", "Landroid/widget/LinearLayout;", "Landroid/view/View;", "child", "Lma/f0;", "addView", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class Section extends LinearLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Section(Context context) {
            super(context);
            k.e(context, "context");
        }

        @Override // android.view.ViewGroup
        public void addView(View view) {
            k.e(view, "child");
            a.g(this, view);
            super.addView(view);
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPrivacyPolicyView(Context context) {
        this(context, null, 0, 0, 14, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPrivacyPolicyView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUIPrivacyPolicyView(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUIPrivacyPolicyView(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? 0 : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    @Override // android.view.View
    public final void setPadding(int i10, int i11, int i12, int i13) {
        super.setPadding(i10, i11, i12, i13);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUIPrivacyPolicyView(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
        setOrientation(1);
        COUIDarkModeUtil.b(this, false);
    }
}
