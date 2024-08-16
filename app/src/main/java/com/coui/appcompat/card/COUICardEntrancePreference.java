package com.coui.appcompat.card;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.coui.appcompat.theme.COUIThemeOverlay;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.component.R$layout;
import com.support.component.R$styleable;
import com.support.list.R$attr;
import java.util.Objects;
import kotlin.Metadata;
import v1.COUIContextUtil;
import w1.COUIDarkModeUtil;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUICardEntrancePreference.kt */
@Metadata(bv = {}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u0000 22\u00020\u0001:\u00013B1\b\u0007\u0012\u0006\u0010+\u001a\u00020*\u0012\n\b\u0002\u0010-\u001a\u0004\u0018\u00010,\u0012\b\b\u0002\u0010.\u001a\u00020\u0006\u0012\b\b\u0002\u0010/\u001a\u00020\u0006¢\u0006\u0004\b0\u00101J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0002J\u0012\u0010\u000b\u001a\u00020\u00042\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0016J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0006H\u0016J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0004J\u0010\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u000fH\u0007R*\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00068\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R*\u0010\u001f\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u000f8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR*\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u000f8\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0014\u0010\u001a\u001a\u0004\b \u0010\u001c\"\u0004\b!\u0010\u001eR*\u0010%\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00068\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\"\u0010\u0014\u001a\u0004\b#\u0010\u0016\"\u0004\b$\u0010\u0018R\u0018\u0010)\u001a\u0004\u0018\u00010&8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b'\u0010(¨\u00064"}, d2 = {"Lcom/coui/appcompat/card/COUICardEntrancePreference;", "Lcom/coui/appcompat/card/COUIPressFeedbackJumpPreference;", "Landroidx/preference/l;", "holder", "Lma/f0;", "l", "", "cardType", "m", "", "summary", "setSummary", "summaryResId", "n", "onBindViewHolder", "", "statusOn", "q", ThermalBaseConfig.Item.ATTR_VALUE, "G", "I", "getCardType", "()I", "o", "(I)V", "H", "Z", "getShowSummary", "()Z", "p", "(Z)V", "showSummary", "getStatusOn", "setStatusOn", "J", "getTintIcon", "r", "tintIcon", "Landroid/widget/TextView;", "K", "Landroid/widget/TextView;", "summaryView", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "L", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public class COUICardEntrancePreference extends COUIPressFeedbackJumpPreference {
    private static final int M = R$layout.coui_component_card_entrance_preference_type_small;
    private static final int N = R$layout.coui_component_card_entrance_preference_type_large;

    /* renamed from: G, reason: from kotlin metadata */
    private int cardType;

    /* renamed from: H, reason: from kotlin metadata */
    private boolean showSummary;

    /* renamed from: I, reason: from kotlin metadata */
    private boolean statusOn;

    /* renamed from: J, reason: from kotlin metadata */
    private int tintIcon;

    /* renamed from: K, reason: from kotlin metadata */
    private TextView summaryView;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardEntrancePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardEntrancePreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUICardEntrancePreference(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? R$attr.couiJumpPreferenceStyle : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    private final void l(PreferenceViewHolder preferenceViewHolder) {
        int i10 = this.tintIcon;
        if (i10 == 2 || i10 == 1) {
            COUIThemeOverlay i11 = COUIThemeOverlay.i();
            Context context = getContext();
            View a10 = preferenceViewHolder.a(R.id.icon);
            i11.a(context, a10 instanceof ImageView ? (ImageView) a10 : null, this.tintIcon == 2);
        }
    }

    private final int m(int cardType) {
        if (cardType == 1) {
            return M;
        }
        if (cardType != 2) {
            return M;
        }
        return N;
    }

    protected final void n(PreferenceViewHolder preferenceViewHolder) {
        k.e(preferenceViewHolder, "holder");
        View a10 = preferenceViewHolder.a(R.id.summary);
        Objects.requireNonNull(a10, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) a10;
        this.summaryView = textView;
        COUIDarkModeUtil.b(textView, false);
        q(this.statusOn);
    }

    public final void o(int i10) {
        setLayoutResource(m(i10));
        this.cardType = i10;
        notifyChanged();
    }

    @Override // com.coui.appcompat.card.COUIPressFeedbackJumpPreference, com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        k.e(preferenceViewHolder, "holder");
        super.onBindViewHolder(preferenceViewHolder);
        COUIDarkModeUtil.b(preferenceViewHolder.itemView, false);
        n(preferenceViewHolder);
        l(preferenceViewHolder);
    }

    public final void p(boolean z10) {
        this.showSummary = z10;
        notifyChanged();
    }

    @SuppressLint({"PrivateResource"})
    public final void q(boolean z10) {
        int b10 = COUIContextUtil.b(getContext(), com.support.appcompat.R$attr.couiColorSecondNeutral, 0);
        int b11 = COUIContextUtil.b(getContext(), com.support.appcompat.R$attr.couiColorPrimaryText, 0);
        TextView textView = this.summaryView;
        if (textView == null) {
            return;
        }
        if (z10) {
            b10 = b11;
        }
        textView.setTextColor(b10);
    }

    public final void r(int i10) {
        this.tintIcon = i10;
        notifyChanged();
    }

    @Override // androidx.preference.Preference
    public void setSummary(CharSequence charSequence) {
        if (this.showSummary) {
            super.setSummary(charSequence);
        } else {
            g(charSequence);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUICardEntrancePreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
        this.cardType = 1;
        this.showSummary = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICardEntrancePreference, i10, i11);
        o(obtainStyledAttributes.getInteger(R$styleable.COUICardEntrancePreference_entranceCardType, 1));
        p(obtainStyledAttributes.getBoolean(R$styleable.COUICardEntrancePreference_showSummary, true));
        r(obtainStyledAttributes.getInteger(R$styleable.COUICardEntrancePreference_tintIcon, 0));
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.preference.Preference
    public void setSummary(int i10) {
        setSummary(getContext().getString(i10));
    }
}
