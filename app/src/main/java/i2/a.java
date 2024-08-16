package i2;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.support.component.R$dimen;
import kotlin.Metadata;

/* compiled from: COUIPrivacyPolicyView.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a\u0014\u0010\u0004\u001a\u00020\u0003*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0001H\u0002\u001a\u001f\u0010\b\u001a\u00020\u0007*\u00020\u00012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0004\b\b\u0010\t\u001a\u0014\u0010\u000b\u001a\u00020\u0005*\u00020\u00012\u0006\u0010\n\u001a\u00020\u0005H\u0002\"\u0018\u0010\u000e\u001a\u00020\u0005*\u00020\u00018BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r\"\u001a\u0010\u0011\u001a\u00020\u0005*\u0004\u0018\u00010\u00008BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0012"}, d2 = {"Landroid/view/ViewGroup;", "Landroid/view/View;", "child", "Lma/f0;", "g", "", "dimRes", "Landroid/widget/LinearLayout$LayoutParams;", "e", "(Landroid/view/View;Ljava/lang/Integer;)Landroid/widget/LinearLayout$LayoutParams;", "resId", "b", "c", "(Landroid/view/View;)I", "intTag", "d", "(Landroid/view/ViewGroup;)I", "lastIntTag", "coui-support-component_release"}, k = 2, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class a {
    private static final int b(View view, int i10) {
        return view.getContext().getResources().getDimensionPixelSize(i10);
    }

    private static final int c(View view) {
        Object tag = view.getTag();
        Integer num = tag instanceof Integer ? (Integer) tag : null;
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    private static final int d(ViewGroup viewGroup) {
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return -1;
        }
        return c(androidx.core.view.ViewGroup.a(viewGroup, viewGroup.getChildCount() - 1));
    }

    private static final LinearLayout.LayoutParams e(View view, Integer num) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.topMargin = num == null ? 0 : b(view, num.intValue());
        return layoutParams;
    }

    static /* synthetic */ LinearLayout.LayoutParams f(View view, Integer num, int i10, Object obj) {
        if ((i10 & 1) != 0) {
            num = null;
        }
        return e(view, num);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void g(ViewGroup viewGroup, View view) {
        LinearLayout.LayoutParams e10;
        if (viewGroup.getChildCount() == 0) {
            e10 = f(viewGroup, null, 1, null);
        } else if (c(view) == 2) {
            e10 = e(viewGroup, Integer.valueOf(R$dimen.coui_component_privacy_policy_small_title_margin_top));
        } else if ((c(view) == 3 && viewGroup.getChildCount() > 0) || d(viewGroup) == 3) {
            e10 = e(viewGroup, Integer.valueOf(R$dimen.coui_component_privacy_policy_table_margin_vertical));
        } else {
            e10 = e(viewGroup, Integer.valueOf(R$dimen.coui_component_privacy_policy_body_margin_top));
        }
        view.setLayoutParams(e10);
    }
}
