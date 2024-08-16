package com.coui.appcompat.card;

import android.R;
import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.card.COUICardButtonPreference;
import com.support.component.R$layout;
import com.support.list.R$attr;
import java.util.Objects;
import kotlin.Metadata;
import w1.COUIDarkModeUtil;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUICardButtonPreference.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u00132\u00020\u0001:\u0001\u0014B1\b\u0007\u0012\u0006\u0010\f\u001a\u00020\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0005¢\u0006\u0004\b\u0011\u0010\u0012J\f\u0010\u0004\u001a\u00020\u0003*\u00020\u0002H\u0002J\f\u0010\u0006\u001a\u00020\u0005*\u00020\u0002H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\u0007H\u0016¨\u0006\u0015"}, d2 = {"Lcom/coui/appcompat/card/COUICardButtonPreference;", "Lcom/coui/appcompat/card/COUIPressFeedbackJumpPreference;", "Landroid/widget/TextView;", "", "n", "", "m", "Landroidx/preference/l;", "holder", "Lma/f0;", "onBindViewHolder", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "G", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUICardButtonPreference extends COUIPressFeedbackJumpPreference {
    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardButtonPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardButtonPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUICardButtonPreference(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? R$attr.couiJumpPreferenceStyle : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    private final int m(TextView textView) {
        return (textView.getWidth() - textView.getPaddingLeft()) - textView.getPaddingRight();
    }

    private final boolean n(TextView textView) {
        TextPaint paint = textView.getPaint();
        k.d(paint, "paint");
        return paint.measureText(textView.getText().toString()) > ((float) m(textView));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void o(COUICardButtonPreference cOUICardButtonPreference, TextView textView) {
        k.e(cOUICardButtonPreference, "this$0");
        k.e(textView, "$title");
        if (cOUICardButtonPreference.n(textView)) {
            textView.setTextSize(10.0f);
        } else {
            textView.setTextSize(12.0f);
        }
    }

    @Override // com.coui.appcompat.card.COUIPressFeedbackJumpPreference, com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        k.e(preferenceViewHolder, "holder");
        super.onBindViewHolder(preferenceViewHolder);
        COUIDarkModeUtil.b(preferenceViewHolder.itemView, false);
        View a10 = preferenceViewHolder.a(R.id.title);
        Objects.requireNonNull(a10, "null cannot be cast to non-null type android.widget.TextView");
        final TextView textView = (TextView) a10;
        textView.post(new Runnable() { // from class: t1.e
            @Override // java.lang.Runnable
            public final void run() {
                COUICardButtonPreference.o(COUICardButtonPreference.this, textView);
            }
        });
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUICardButtonPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
        setLayoutResource(R$layout.coui_component_card_button_preference);
    }
}
