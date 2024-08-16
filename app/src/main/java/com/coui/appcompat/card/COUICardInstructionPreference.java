package com.coui.appcompat.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import androidx.viewpager2.widget.ViewPager2;
import com.coui.appcompat.indicator.COUIPageIndicator;
import com.coui.appcompat.preference.COUIPreference;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.component.R$id;
import com.support.component.R$layout;
import com.support.component.R$styleable;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import t1.CardInstructionSelectorAdapter;
import t1.c;
import t1.d;
import t1.g;
import w1.COUIDarkModeUtil;
import za.DefaultConstructorMarker;
import za.k;

/* compiled from: COUICardInstructionPreference.kt */
@Metadata(bv = {}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u00162\u00020\u0001:\u0002%&B1\b\u0007\u0012\u0006\u0010\u001e\u001a\u00020\u001d\u0012\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u001f\u0012\b\b\u0002\u0010!\u001a\u00020\u0002\u0012\b\b\u0002\u0010\"\u001a\u00020\u0002¢\u0006\u0004\b#\u0010$J\u0018\u0010\u0006\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0018\u0010\f\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\tH\u0002J\u0014\u0010\u0010\u001a\u00020\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rJ\u0010\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u0011H\u0016R*\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u00028\u0006@FX\u0086\u000e¢\u0006\u0012\n\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0016\u0010\u001c\u001a\u00020\u00028\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u001b\u0010\u0016¨\u0006'"}, d2 = {"Lcom/coui/appcompat/card/COUICardInstructionPreference;", "Lcom/coui/appcompat/preference/COUIPreference;", "", "cardType", "Lt1/c;", "Lt1/c$a;", "i", "Landroidx/viewpager2/widget/ViewPager2;", "viewPager", "Lcom/coui/appcompat/indicator/COUIPageIndicator;", "indicator", "Lma/f0;", "m", "", "Lt1/d;", "displayInfos", "l", "Landroidx/preference/l;", "holder", "onBindViewHolder", ThermalBaseConfig.Item.ATTR_VALUE, "F", "I", "getCardType", "()I", "j", "(I)V", "H", "lastPagerItem", "Landroid/content/Context;", "context", "Landroid/util/AttributeSet;", "attrs", "defStyleAttr", "defStyleRes", "<init>", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "a", "b", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class COUICardInstructionPreference extends COUIPreference {

    /* renamed from: F, reason: from kotlin metadata */
    private int cardType;
    private t1.c<?> G;

    /* renamed from: H, reason: from kotlin metadata */
    private int lastPagerItem;

    /* compiled from: COUICardInstructionPreference.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bæ\u0080\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H&¨\u0006\u0006"}, d2 = {"Lcom/coui/appcompat/card/COUICardInstructionPreference$b;", "", "", ThermalBaseConfig.Item.ATTR_INDEX, "Lma/f0;", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public interface b {
        void a(int i10);
    }

    /* compiled from: COUICardInstructionPreference.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\b\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0002H\u0016J\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0002H\u0016J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0002H\u0016¨\u0006\f"}, d2 = {"com/coui/appcompat/card/COUICardInstructionPreference$c", "Landroidx/viewpager2/widget/ViewPager2$i;", "", "position", "", "positionOffset", "positionOffsetPixels", "Lma/f0;", "b", "c", "state", "a", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class c extends ViewPager2.i {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ COUIPageIndicator f5538a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ COUICardInstructionPreference f5539b;

        c(COUIPageIndicator cOUIPageIndicator, COUICardInstructionPreference cOUICardInstructionPreference) {
            this.f5538a = cOUIPageIndicator;
            this.f5539b = cOUICardInstructionPreference;
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void a(int i10) {
            super.a(i10);
            this.f5538a.h0(i10);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void b(int i10, float f10, int i11) {
            super.b(i10, f10, i11);
            this.f5538a.i0(i10, f10, i11);
        }

        @Override // androidx.viewpager2.widget.ViewPager2.i
        public void c(int i10) {
            super.c(i10);
            this.f5538a.j0(i10);
            this.f5539b.lastPagerItem = i10;
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context) {
        this(context, null, 0, 0, 14, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        k.e(context, "context");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context, AttributeSet attributeSet, int i10) {
        this(context, attributeSet, i10, 0, 8, null);
        k.e(context, "context");
    }

    public /* synthetic */ COUICardInstructionPreference(Context context, AttributeSet attributeSet, int i10, int i11, int i12, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i12 & 2) != 0 ? null : attributeSet, (i12 & 4) != 0 ? R$attr.preferenceStyle : i10, (i12 & 8) != 0 ? 0 : i11);
    }

    private final t1.c<? extends c.a> i(int cardType) {
        if (cardType == 1) {
            return new g();
        }
        if (cardType != 2) {
            return new g();
        }
        return new CardInstructionSelectorAdapter();
    }

    private final void m(ViewPager2 viewPager2, COUIPageIndicator cOUIPageIndicator) {
        viewPager2.j(new c(cOUIPageIndicator, this));
    }

    public final void j(int i10) {
        this.cardType = i10;
        this.G = i(i10);
        notifyChanged();
    }

    public final void l(List<? extends d> list) {
        k.e(list, "displayInfos");
        this.G.g(list);
        notifyChanged();
    }

    @Override // com.coui.appcompat.preference.COUIPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        k.e(preferenceViewHolder, "holder");
        super.onBindViewHolder(preferenceViewHolder);
        COUIDarkModeUtil.b(preferenceViewHolder.itemView, false);
        View a10 = preferenceViewHolder.a(R$id.pager);
        Objects.requireNonNull(a10, "null cannot be cast to non-null type androidx.viewpager2.widget.ViewPager2");
        ViewPager2 viewPager2 = (ViewPager2) a10;
        View a11 = preferenceViewHolder.a(R$id.indicator);
        Objects.requireNonNull(a11, "null cannot be cast to non-null type com.coui.appcompat.indicator.COUIPageIndicator");
        COUIPageIndicator cOUIPageIndicator = (COUIPageIndicator) a11;
        cOUIPageIndicator.setVisibility(this.G.getItemCount() > 1 ? 0 : 8);
        if (this.G.getItemCount() > 0) {
            viewPager2.setAdapter(this.G);
            viewPager2.setCurrentItem(this.lastPagerItem);
            viewPager2.setOffscreenPageLimit(this.G.getItemCount());
            cOUIPageIndicator.setDotsCount(this.G.getItemCount());
            m(viewPager2, cOUIPageIndicator);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public COUICardInstructionPreference(Context context, AttributeSet attributeSet, int i10, int i11) {
        super(context, attributeSet, i10, i11);
        k.e(context, "context");
        this.cardType = 1;
        setLayoutResource(R$layout.coui_component_card_instruction_preference);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.COUICardInstructionPreference, i10, i11);
        j(obtainStyledAttributes.getInteger(R$styleable.COUICardInstructionPreference_instructionCardType, 1));
        obtainStyledAttributes.recycle();
        this.G = i(this.cardType);
    }
}
