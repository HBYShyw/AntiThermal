package t1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.coui.appcompat.card.COUICardInstructionPreference;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.appcompat.R$attr;
import com.support.appcompat.R$style;
import com.support.component.R$id;
import com.support.component.R$layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import ma.Unit;
import t1.CardInstructionSelectorAdapter;
import t1.c;
import v1.COUIContextUtil;

/* compiled from: CardInstructionSelectorAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0019\u001aB\u0015\u0012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013¢\u0006\u0004\b\u0016\u0010\u0017B\t\b\u0016¢\u0006\u0004\b\u0016\u0010\u0018J\u001c\u0010\u0007\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H\u0016J\u001c\u0010\u000b\u001a\u00020\n2\n\u0010\b\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\t\u001a\u00020\u0005H\u0016R$\u0010\r\u001a\u0004\u0018\u00010\f8\u0006@\u0006X\u0086\u000e¢\u0006\u0012\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012¨\u0006\u001b"}, d2 = {"Lt1/i;", "Lt1/c;", "Lt1/i$b;", "Landroid/view/ViewGroup;", "parent", "", "viewType", "l", "holder", "position", "Lma/f0;", "k", "Lcom/coui/appcompat/card/COUICardInstructionPreference$b;", "onSelectedCardChangedListener", "Lcom/coui/appcompat/card/COUICardInstructionPreference$b;", "j", "()Lcom/coui/appcompat/card/COUICardInstructionPreference$b;", "setOnSelectedCardChangedListener", "(Lcom/coui/appcompat/card/COUICardInstructionPreference$b;)V", "", "Lt1/d;", "displayInfos", "<init>", "(Ljava/util/List;)V", "()V", "a", "b", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* renamed from: t1.i, reason: use source file name */
/* loaded from: classes.dex */
public final class CardInstructionSelectorAdapter extends c<b> {

    /* renamed from: d, reason: collision with root package name */
    private COUICardInstructionPreference.b f18543d;

    /* renamed from: e, reason: collision with root package name */
    private int f18544e;

    /* compiled from: CardInstructionSelectorAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u001c\u001a\u00020\u001b¢\u0006\u0004\b\u001d\u0010\u001eJ\u0016\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002R\u0017\u0010\b\u001a\u00020\u00078\u0006¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\r\u001a\u00020\f8\u0006¢\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0012\u001a\u00020\u00118\u0006¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0017\u001a\u00020\u00168\u0006¢\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0019\u0010\u001a¨\u0006\u001f"}, d2 = {"Lt1/i$a;", "", "", "width", "height", "Lma/f0;", "e", "Landroid/view/View;", "rootView", "Landroid/view/View;", "c", "()Landroid/view/View;", "Lcom/oplus/anim/EffectiveAnimationView;", "animView", "Lcom/oplus/anim/EffectiveAnimationView;", "a", "()Lcom/oplus/anim/EffectiveAnimationView;", "Landroid/widget/TextView;", "title", "Landroid/widget/TextView;", "d", "()Landroid/widget/TextView;", "Landroid/widget/RadioButton;", "radio", "Landroid/widget/RadioButton;", "b", "()Landroid/widget/RadioButton;", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: t1.i$a */
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final View f18545a;

        /* renamed from: b, reason: collision with root package name */
        private final EffectiveAnimationView f18546b;

        /* renamed from: c, reason: collision with root package name */
        private final TextView f18547c;

        /* renamed from: d, reason: collision with root package name */
        private final RadioButton f18548d;

        public a(Context context) {
            za.k.e(context, "context");
            View inflate = View.inflate(context, R$layout.coui_component_card_instruction_selector, null);
            za.k.d(inflate, "inflate(\n            context, R.layout.coui_component_card_instruction_selector, null\n        )");
            this.f18545a = inflate;
            View findViewById = inflate.findViewById(R$id.anim_view);
            za.k.d(findViewById, "rootView.findViewById(R.id.anim_view)");
            this.f18546b = (EffectiveAnimationView) findViewById;
            View findViewById2 = inflate.findViewById(R$id.title);
            za.k.d(findViewById2, "rootView.findViewById(R.id.title)");
            this.f18547c = (TextView) findViewById2;
            View findViewById3 = inflate.findViewById(R$id.radio);
            za.k.d(findViewById3, "rootView.findViewById(R.id.radio)");
            this.f18548d = (RadioButton) findViewById3;
        }

        /* renamed from: a, reason: from getter */
        public final EffectiveAnimationView getF18546b() {
            return this.f18546b;
        }

        /* renamed from: b, reason: from getter */
        public final RadioButton getF18548d() {
            return this.f18548d;
        }

        /* renamed from: c, reason: from getter */
        public final View getF18545a() {
            return this.f18545a;
        }

        /* renamed from: d, reason: from getter */
        public final TextView getF18547c() {
            return this.f18547c;
        }

        public final void e(int i10, int i11) {
            if (i10 <= 0 || i11 <= 0) {
                return;
            }
            EffectiveAnimationView effectiveAnimationView = this.f18546b;
            ViewGroup.LayoutParams layoutParams = effectiveAnimationView.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type androidx.constraintlayout.widget.ConstraintLayout.LayoutParams");
            ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
            ((ViewGroup.MarginLayoutParams) layoutParams2).height = i11;
            ((ViewGroup.MarginLayoutParams) layoutParams2).width = i10;
            Unit unit = Unit.f15173a;
            effectiveAnimationView.setLayoutParams(layoutParams2);
        }
    }

    /* compiled from: CardInstructionSelectorAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u0012\n\u0010\u0011\u001a\u0006\u0012\u0002\b\u00030\u0010¢\u0006\u0004\b\u0012\u0010\u0013J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H\u0002J\u0010\u0010\b\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0007H\u0002J\u0010\u0010\n\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\tH\u0016J\u0010\u0010\r\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\u000bH\u0007¨\u0006\u0014"}, d2 = {"Lt1/i$b;", "Lt1/c$a;", "Lma/f0;", "i", "Lt1/a;", "displayInfo", "g", "Lt1/k;", "h", "Lt1/d;", "b", "", ThermalBaseConfig.Item.ATTR_INDEX, "k", "Landroid/view/View;", "itemView", "Lt1/c;", "adapter", "<init>", "(Lt1/i;Landroid/view/View;Lt1/c;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* renamed from: t1.i$b */
    /* loaded from: classes.dex */
    public final class b extends c.a {

        /* renamed from: b, reason: collision with root package name */
        private final List<a> f18549b;

        /* renamed from: c, reason: collision with root package name */
        private final LinearLayout f18550c;

        /* renamed from: d, reason: collision with root package name */
        final /* synthetic */ CardInstructionSelectorAdapter f18551d;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public b(CardInstructionSelectorAdapter cardInstructionSelectorAdapter, View view, c<?> cVar) {
            super(view, cVar);
            za.k.e(cardInstructionSelectorAdapter, "this$0");
            za.k.e(view, "itemView");
            za.k.e(cVar, "adapter");
            this.f18551d = cardInstructionSelectorAdapter;
            this.f18549b = new ArrayList();
            View findViewById = view.findViewById(R$id.container);
            za.k.d(findViewById, "itemView.findViewById(R.id.container)");
            this.f18550c = (LinearLayout) findViewById;
        }

        private final void g(t1.a aVar) {
            if ((!aVar.h().isEmpty()) && (!aVar.i().isEmpty())) {
                throw new IllegalArgumentException("imageAssets and imageResources cannot be used at the same time. Please use only one at once.");
            }
            if (aVar.h().size() + aVar.i().size() == aVar.d().size()) {
                Iterator<Integer> it = aVar.i().iterator();
                int i10 = 0;
                int i11 = 0;
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    Context context = this.itemView.getContext();
                    za.k.d(context, "itemView.context");
                    a aVar2 = new a(context);
                    c.f18518c.a(aVar2.getF18547c(), aVar.d().get(i11));
                    aVar2.getF18546b().setAnimation(intValue);
                    aVar2.e(aVar.getF18528g(), aVar.getF18527f());
                    this.f18550c.addView(aVar2.getF18545a());
                    this.f18549b.add(aVar2);
                    i11++;
                }
                for (String str : aVar.h()) {
                    Context context2 = this.itemView.getContext();
                    za.k.d(context2, "itemView.context");
                    a aVar3 = new a(context2);
                    c.f18518c.a(aVar3.getF18547c(), aVar.d().get(i10));
                    aVar3.getF18546b().setAnimation(str);
                    aVar3.e(aVar.getF18528g(), aVar.getF18527f());
                    this.f18550c.addView(aVar3.getF18545a());
                    this.f18549b.add(aVar3);
                    i10++;
                }
                return;
            }
            throw new IllegalArgumentException("the anim count must equal to the choice count");
        }

        private final void h(k kVar) {
            if (kVar.getF18555h().length == kVar.d().size()) {
                Integer[] f18555h = kVar.getF18555h();
                int length = f18555h.length;
                for (int i10 = 0; i10 < length; i10++) {
                    int intValue = f18555h[i10].intValue();
                    Context context = this.itemView.getContext();
                    za.k.d(context, "itemView.context");
                    a aVar = new a(context);
                    c.f18518c.a(aVar.getF18547c(), kVar.d().get(i10));
                    aVar.getF18546b().setImageResource(intValue);
                    aVar.e(kVar.getF18528g(), kVar.getF18527f());
                    this.f18550c.addView(aVar.getF18545a());
                    this.f18549b.add(aVar);
                }
                return;
            }
            throw new IllegalArgumentException("the image count must equal to the choice count");
        }

        private final void i() {
            List<a> list = this.f18549b;
            final CardInstructionSelectorAdapter cardInstructionSelectorAdapter = this.f18551d;
            for (final a aVar : list) {
                aVar.getF18545a().setOnClickListener(new View.OnClickListener() { // from class: t1.j
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        CardInstructionSelectorAdapter.b.j(CardInstructionSelectorAdapter.b.this, aVar, cardInstructionSelectorAdapter, view);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void j(b bVar, a aVar, CardInstructionSelectorAdapter cardInstructionSelectorAdapter, View view) {
            za.k.e(bVar, "this$0");
            za.k.e(aVar, "$selector");
            za.k.e(cardInstructionSelectorAdapter, "this$1");
            int indexOf = bVar.f18549b.indexOf(aVar);
            if (indexOf != cardInstructionSelectorAdapter.f18544e) {
                cardInstructionSelectorAdapter.f18544e = indexOf;
                COUICardInstructionPreference.b f18543d = cardInstructionSelectorAdapter.getF18543d();
                if (f18543d != null) {
                    f18543d.a(indexOf);
                }
            }
            bVar.k(indexOf);
        }

        @Override // t1.c.a
        public void b(d dVar) {
            za.k.e(dVar, "displayInfo");
            this.f18549b.clear();
            this.f18550c.removeAllViews();
            if (dVar instanceof t1.a) {
                g((t1.a) dVar);
            } else if (dVar instanceof k) {
                h((k) dVar);
            }
            i();
            k(dVar.getF18526e());
        }

        @SuppressLint({"PrivateResource"})
        public final void k(int i10) {
            if (i10 < 0 || i10 >= this.f18549b.size()) {
                return;
            }
            a aVar = this.f18549b.get(i10);
            aVar.getF18548d().setChecked(true);
            aVar.getF18547c().setTextAppearance(R$style.couiTextAppearanceButton);
            aVar.getF18547c().setTextColor(COUIContextUtil.a(this.itemView.getContext(), R$attr.couiColorPrimary));
            List<a> list = this.f18549b;
            ArrayList<a> arrayList = new ArrayList();
            for (Object obj : list) {
                if (!za.k.a((a) obj, aVar)) {
                    arrayList.add(obj);
                }
            }
            for (a aVar2 : arrayList) {
                aVar2.getF18548d().setChecked(false);
                aVar.getF18547c().setTextAppearance(R$style.couiTextAppearanceBody);
                aVar2.getF18547c().setTextColor(COUIContextUtil.a(this.itemView.getContext(), R$attr.couiColorSecondNeutral));
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CardInstructionSelectorAdapter(List<d> list) {
        super(list);
        za.k.e(list, "displayInfos");
        this.f18544e = -1;
    }

    /* renamed from: j, reason: from getter */
    public final COUICardInstructionPreference.b getF18543d() {
        return this.f18543d;
    }

    @Override // t1.c, androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: k, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(b bVar, int i10) {
        za.k.e(bVar, "holder");
        super.onBindViewHolder(bVar, i10);
        bVar.k(this.f18544e);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public b onCreateViewHolder(ViewGroup parent, int viewType) {
        za.k.e(parent, "parent");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R$layout.coui_component_card_instruction_selector_page, parent, false);
        za.k.d(inflate, "from(parent.context)\n            .inflate(\n                R.layout.coui_component_card_instruction_selector_page,\n                parent,\n                false\n            )");
        return new b(this, inflate, this);
    }

    public CardInstructionSelectorAdapter() {
        this(new ArrayList());
    }
}
