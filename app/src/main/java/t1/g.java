package t1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.coui.appcompat.card.COUIMutableSizeScrollView;
import com.oplus.anim.EffectiveAnimationView;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import com.support.component.R$dimen;
import com.support.component.R$id;
import com.support.component.R$layout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.Metadata;
import ma.Unit;
import t1.c;
import za.DefaultConstructorMarker;

/* compiled from: CardInstructionDescriptionAdapter.kt */
@Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0003\u000e\u000f\u0010B\u0015\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\b¢\u0006\u0004\b\u000b\u0010\fB\t\b\u0016¢\u0006\u0004\b\u000b\u0010\rJ\u001c\u0010\u0007\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0011"}, d2 = {"Lt1/g;", "Lt1/c;", "Lt1/g$c;", "Landroid/view/ViewGroup;", "parent", "", "viewType", "h", "", "Lt1/d;", "displayInfos", "<init>", "(Ljava/util/List;)V", "()V", "a", "b", "c", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public final class g extends t1.c<c> {

    /* renamed from: d, reason: collision with root package name */
    public static final b f18533d = new b(null);

    /* compiled from: CardInstructionDescriptionAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u000f¢\u0006\u0004\b\u0011\u0010\u0012J\u0016\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004J\u0016\u0010\t\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0004R\u0017\u0010\u000b\u001a\u00020\n8\u0006¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\r\u0010\u000e¨\u0006\u0013"}, d2 = {"Lt1/g$a;", "", "Lt1/k;", "displayInfo", "", ThermalBaseConfig.Item.ATTR_INDEX, "Lma/f0;", "b", "Lt1/a;", "a", "Landroid/widget/LinearLayout;", "rootView", "Landroid/widget/LinearLayout;", "c", "()Landroid/widget/LinearLayout;", "Landroid/content/Context;", "context", "<init>", "(Landroid/content/Context;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class a {

        /* renamed from: a, reason: collision with root package name */
        private final LinearLayout f18534a;

        /* renamed from: b, reason: collision with root package name */
        private final EffectiveAnimationView f18535b;

        /* renamed from: c, reason: collision with root package name */
        private final TextView f18536c;

        public a(Context context) {
            za.k.e(context, "context");
            View inflate = View.inflate(context, R$layout.coui_component_card_instruction_anim, null);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.LinearLayout");
            LinearLayout linearLayout = (LinearLayout) inflate;
            this.f18534a = linearLayout;
            this.f18535b = (EffectiveAnimationView) linearLayout.findViewById(R$id.anim_view);
            this.f18536c = (TextView) linearLayout.findViewById(R$id.anim_title);
        }

        public final void a(t1.a aVar, int i10) {
            za.k.e(aVar, "displayInfo");
            if (aVar.b().size() > 0) {
                this.f18536c.setText(aVar.b().get(i10));
            } else {
                TextView textView = this.f18536c;
                za.k.d(textView, "animTitle");
                textView.setVisibility(8);
            }
            if (aVar.i().size() > 0) {
                this.f18535b.setAnimation(aVar.i().get(i10).intValue());
                this.f18535b.setLayoutParams(g.f18533d.b(aVar.getF18528g(), aVar.getF18527f()));
            } else {
                this.f18535b.setAnimation(aVar.h().get(i10));
                this.f18535b.setLayoutParams(g.f18533d.b(aVar.getF18528g(), aVar.getF18527f()));
            }
        }

        public final void b(k kVar, int i10) {
            za.k.e(kVar, "displayInfo");
            if (kVar.b().size() > 0) {
                TextView textView = this.f18536c;
                za.k.d(textView, "animTitle");
                textView.setVisibility(0);
                this.f18536c.setText(kVar.b().get(i10));
            }
            this.f18535b.setImageResource(kVar.getF18555h()[i10].intValue());
            this.f18535b.setLayoutParams(g.f18533d.b(kVar.getF18528g(), kVar.getF18527f()));
        }

        /* renamed from: c, reason: from getter */
        public final LinearLayout getF18534a() {
            return this.f18534a;
        }
    }

    /* compiled from: CardInstructionDescriptionAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u001c\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0003\u001a\u00020\u00022\b\b\u0002\u0010\u0004\u001a\u00020\u0002H\u0002¨\u0006\t"}, d2 = {"Lt1/g$b;", "", "", "width", "height", "Landroid/widget/LinearLayout$LayoutParams;", "b", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final LinearLayout.LayoutParams b(int width, int height) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            if (width > 0 && height > 0) {
                layoutParams.height = height;
                layoutParams.width = width;
            }
            return layoutParams;
        }
    }

    /* compiled from: CardInstructionDescriptionAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\f¢\u0006\u0004\b\u000e\u0010\u000fJ\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0002J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0006H\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\bH\u0016¨\u0006\u0010"}, d2 = {"Lt1/g$c;", "Lt1/c$a;", "Lt1/a;", "displayInfo", "Lma/f0;", "f", "Lt1/k;", "g", "Lt1/d;", "b", "Landroid/view/View;", "itemView", "Lt1/c;", "adapter", "<init>", "(Lt1/g;Landroid/view/View;Lt1/c;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public final class c extends c.a {

        /* renamed from: b, reason: collision with root package name */
        private final LinearLayout f18537b;

        /* renamed from: c, reason: collision with root package name */
        private final TextView f18538c;

        /* renamed from: d, reason: collision with root package name */
        private final TextView f18539d;

        /* renamed from: e, reason: collision with root package name */
        private final LinearLayout f18540e;

        /* renamed from: f, reason: collision with root package name */
        private final COUIMutableSizeScrollView f18541f;

        /* renamed from: g, reason: collision with root package name */
        final /* synthetic */ g f18542g;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(g gVar, View view, t1.c<?> cVar) {
            super(view, cVar);
            za.k.e(gVar, "this$0");
            za.k.e(view, "itemView");
            za.k.e(cVar, "adapter");
            this.f18542g = gVar;
            View findViewById = view.findViewById(R$id.anim_container);
            za.k.d(findViewById, "itemView.findViewById(R.id.anim_container)");
            this.f18537b = (LinearLayout) findViewById;
            View findViewById2 = view.findViewById(R$id.title);
            za.k.d(findViewById2, "itemView.findViewById(R.id.title)");
            this.f18538c = (TextView) findViewById2;
            View findViewById3 = view.findViewById(R$id.summary);
            za.k.d(findViewById3, "itemView.findViewById(R.id.summary)");
            this.f18539d = (TextView) findViewById3;
            View findViewById4 = view.findViewById(R$id.summary_container);
            za.k.d(findViewById4, "itemView.findViewById(R.id.summary_container)");
            this.f18540e = (LinearLayout) findViewById4;
            View findViewById5 = view.findViewById(R$id.content_container);
            za.k.d(findViewById5, "itemView.findViewById(R.id.content_container)");
            this.f18541f = (COUIMutableSizeScrollView) findViewById5;
        }

        private final void f(t1.a aVar) {
            if ((!aVar.h().isEmpty()) && (!aVar.i().isEmpty())) {
                throw new IllegalArgumentException("imageAssets and imageResources cannot be used at the same time. Please use only one at once.");
            }
            if (aVar.b().size() > 0 && aVar.h().size() + aVar.i().size() != aVar.b().size()) {
                throw new IllegalArgumentException("the image count must equals to the animTitle count");
            }
            int size = aVar.i().size() - 1;
            int i10 = 0;
            if (size >= 0) {
                int i11 = 0;
                while (true) {
                    int i12 = i11 + 1;
                    Context context = this.itemView.getContext();
                    za.k.d(context, "itemView.context");
                    a aVar2 = new a(context);
                    aVar2.a(aVar, i11);
                    this.f18537b.addView(aVar2.getF18534a());
                    if (i12 > size) {
                        break;
                    } else {
                        i11 = i12;
                    }
                }
            }
            int size2 = aVar.h().size() - 1;
            if (size2 < 0) {
                return;
            }
            while (true) {
                int i13 = i10 + 1;
                Context context2 = this.itemView.getContext();
                za.k.d(context2, "itemView.context");
                a aVar3 = new a(context2);
                aVar3.a(aVar, i10);
                this.f18537b.addView(aVar3.getF18534a());
                if (i13 > size2) {
                    return;
                } else {
                    i10 = i13;
                }
            }
        }

        private final void g(k kVar) {
            if (kVar.b().size() > 0 && kVar.getF18555h().length != kVar.b().size()) {
                throw new IllegalArgumentException("the anim count must equals to the animTitle count");
            }
            int i10 = 0;
            int length = kVar.getF18555h().length - 1;
            if (length < 0) {
                return;
            }
            while (true) {
                int i11 = i10 + 1;
                Context context = this.itemView.getContext();
                za.k.d(context, "itemView.context");
                a aVar = new a(context);
                aVar.b(kVar, i10);
                this.f18537b.addView(aVar.getF18534a());
                if (i11 > length) {
                    return;
                } else {
                    i10 = i11;
                }
            }
        }

        @Override // t1.c.a
        public void b(d dVar) {
            int i10;
            int b10;
            za.k.e(dVar, "displayInfo");
            c.b bVar = t1.c.f18518c;
            bVar.a(this.f18538c, dVar.getF18522a());
            bVar.b(this.f18539d, dVar.getF18523b(), this.f18540e);
            if (this.f18538c.getVisibility() == 0) {
                COUIMutableSizeScrollView cOUIMutableSizeScrollView = this.f18541f;
                cOUIMutableSizeScrollView.setMaxHeight(cOUIMutableSizeScrollView.getResources().getDimensionPixelSize(R$dimen.coui_component_card_instruction_content_height_complete));
                i10 = R$dimen.coui_component_card_instruction_summary_margin_top_small;
            } else {
                COUIMutableSizeScrollView cOUIMutableSizeScrollView2 = this.f18541f;
                cOUIMutableSizeScrollView2.setMaxHeight(cOUIMutableSizeScrollView2.getResources().getDimensionPixelSize(R$dimen.coui_component_card_instruction_content_height_part));
                i10 = R$dimen.coui_component_card_instruction_summary_margin_top_large;
            }
            LinearLayout linearLayout = this.f18540e;
            ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            b10 = h.b(this.f18540e, i10);
            layoutParams2.topMargin = b10;
            Unit unit = Unit.f15173a;
            linearLayout.setLayoutParams(layoutParams2);
            if (dVar instanceof t1.a) {
                f((t1.a) dVar);
            } else if (dVar instanceof k) {
                g((k) dVar);
            }
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(List<d> list) {
        super(list);
        za.k.e(list, "displayInfos");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: h, reason: merged with bridge method [inline-methods] */
    public c onCreateViewHolder(ViewGroup parent, int viewType) {
        za.k.e(parent, "parent");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R$layout.coui_component_card_instruction_description_page, parent, false);
        za.k.d(inflate, "from(parent.context)\n            .inflate(\n                R.layout.coui_component_card_instruction_description_page,\n                parent,\n                false\n            )");
        return new c(this, inflate, this);
    }

    public g() {
        this(new ArrayList());
    }
}
