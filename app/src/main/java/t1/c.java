package t1;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import fb._Ranges;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import ma.Unit;
import t1.c;
import t1.c.a;
import za.DefaultConstructorMarker;

/* compiled from: BaseCardInstructionAdapter.kt */
@Metadata(bv = {}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0004\b&\u0018\u0000*\b\b\u0000\u0010\u0002*\u00020\u00012\b\u0012\u0004\u0012\u00028\u00000\u0003:\u0002\u0013\u0014B\u0007¢\u0006\u0004\b\u000f\u0010\u0010B\u0019\b\u0016\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u0011¢\u0006\u0004\b\u000f\u0010\u0012J\b\u0010\u0005\u001a\u00020\u0004H\u0016J\u001f\u0010\t\u001a\u00020\b2\u0006\u0010\u0006\u001a\u00028\u00002\u0006\u0010\u0007\u001a\u00020\u0004H\u0016¢\u0006\u0004\b\t\u0010\nJ\u0016\u0010\u000e\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0007¨\u0006\u0015"}, d2 = {"Lt1/c;", "Lt1/c$a;", "HOLDER", "Landroidx/recyclerview/widget/RecyclerView$h;", "", "getItemCount", "holder", "position", "Lma/f0;", "f", "(Lt1/c$a;I)V", "", "Lt1/d;", "displayInfos", "g", "<init>", "()V", "", "(Ljava/util/List;)V", "a", "b", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public abstract class c<HOLDER extends a> extends RecyclerView.h<HOLDER> {

    /* renamed from: c, reason: collision with root package name */
    public static final b f18518c = new b(null);

    /* renamed from: a, reason: collision with root package name */
    private final List<d> f18519a;

    /* renamed from: b, reason: collision with root package name */
    private int f18520b;

    /* compiled from: BaseCardInstructionAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\r\u001a\u00020\f\u0012\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\u0007¢\u0006\u0004\b\u000e\u0010\u000fJ\u0006\u0010\u0003\u001a\u00020\u0002J\u0010\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0004H&R\u001e\u0010\b\u001a\u0006\u0012\u0002\b\u00030\u00078\u0004X\u0084\u0004¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b¨\u0006\u0010"}, d2 = {"Lt1/c$a;", "Landroidx/recyclerview/widget/RecyclerView$c0;", "Lma/f0;", "d", "Lt1/d;", "displayInfo", "b", "Lt1/c;", "adapter", "Lt1/c;", "c", "()Lt1/c;", "Landroid/view/View;", "itemView", "<init>", "(Landroid/view/View;Lt1/c;)V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static abstract class a extends RecyclerView.c0 {

        /* renamed from: a, reason: collision with root package name */
        private final c<?> f18521a;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public a(View view, c<?> cVar) {
            super(view);
            za.k.e(view, "itemView");
            za.k.e(cVar, "adapter");
            this.f18521a = cVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void e(a aVar) {
            int c10;
            int c11;
            za.k.e(aVar, "this$0");
            aVar.itemView.measure(View.MeasureSpec.makeMeasureSpec(aVar.itemView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(0, Integer.MIN_VALUE));
            ViewParent parent = aVar.itemView.getParent();
            ViewParent parent2 = parent == null ? null : parent.getParent();
            if (parent2 instanceof ViewPager2) {
                ViewPager2 viewPager2 = (ViewPager2) parent2;
                int i10 = viewPager2.getLayoutParams().height;
                c10 = _Ranges.c(((c) aVar.c()).f18520b, aVar.itemView.getMeasuredHeight());
                if (i10 != c10) {
                    ViewGroup.LayoutParams layoutParams = viewPager2.getLayoutParams();
                    c11 = _Ranges.c(((c) aVar.c()).f18520b, aVar.itemView.getMeasuredHeight());
                    layoutParams.height = c11;
                    ((c) aVar.c()).f18520b = layoutParams.height;
                    Unit unit = Unit.f15173a;
                    viewPager2.setLayoutParams(layoutParams);
                }
            }
        }

        public abstract void b(d dVar);

        protected final c<?> c() {
            return this.f18521a;
        }

        public final void d() {
            if (((c) this.f18521a).f18519a.size() <= 1) {
                return;
            }
            this.itemView.post(new Runnable() { // from class: t1.b
                @Override // java.lang.Runnable
                public final void run() {
                    c.a.e(c.a.this);
                }
            });
        }
    }

    /* compiled from: BaseCardInstructionAdapter.kt */
    @Metadata(bv = {}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\r\u0010\u000eJ\u0012\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0003J\u001a\u0010\t\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0007R\u0014\u0010\u000b\u001a\u00020\n8\u0006X\u0086T¢\u0006\u0006\n\u0004\b\u000b\u0010\f¨\u0006\u000f"}, d2 = {"Lt1/c$b;", "", "Landroid/widget/TextView;", "", "content", "Lma/f0;", "a", "Landroid/view/View;", "view", "b", "", "EMPTY_STRING", "Ljava/lang/String;", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
    /* loaded from: classes.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void a(TextView textView, CharSequence charSequence) {
            za.k.e(textView, "<this>");
            za.k.e(charSequence, "content");
            b(textView, charSequence, textView);
        }

        public final void b(TextView textView, CharSequence charSequence, View view) {
            za.k.e(textView, "<this>");
            za.k.e(charSequence, "content");
            za.k.e(view, "view");
            if (charSequence.length() > 0) {
                view.setVisibility(0);
                textView.setText(charSequence);
            } else {
                view.setVisibility(8);
            }
        }
    }

    public c() {
        this.f18519a = new ArrayList();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    /* renamed from: f, reason: merged with bridge method [inline-methods] */
    public void onBindViewHolder(HOLDER holder, int position) {
        za.k.e(holder, "holder");
        holder.b(this.f18519a.get(position));
        holder.d();
    }

    @SuppressLint({"NotifyDataSetChanged"})
    public final void g(List<? extends d> list) {
        za.k.e(list, "displayInfos");
        this.f18519a.clear();
        this.f18519a.addAll(list);
        this.f18520b = 0;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.h
    public int getItemCount() {
        return this.f18519a.size();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public c(List<d> list) {
        this();
        za.k.e(list, "displayInfos");
        this.f18519a.clear();
        this.f18519a.addAll(list);
    }
}
