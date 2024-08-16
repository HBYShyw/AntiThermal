package androidx.core.view;

import android.view.View;
import com.oplus.deepthinker.sdk.app.aidl.eventfountain.EventType;
import com.oplus.thermalcontrol.config.ThermalBaseConfig;
import java.util.Iterator;
import kotlin.Metadata;
import rd.Sequence;

/* compiled from: ViewGroup.kt */
@Metadata(bv = {}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0015\u0010\u0004\u001a\u00020\u0003*\u00020\u00002\u0006\u0010\u0002\u001a\u00020\u0001H\u0086\u0002\u001a\u0013\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005*\u00020\u0000H\u0086\u0002\"\u001b\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007*\u00020\u00008F¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u000b"}, d2 = {"Landroid/view/ViewGroup;", "", ThermalBaseConfig.Item.ATTR_INDEX, "Landroid/view/View;", "a", "", "c", "Lrd/h;", "b", "(Landroid/view/ViewGroup;)Lrd/h;", "children", "core-ktx_release"}, k = 2, mv = {1, 7, 1})
/* renamed from: androidx.core.view.c0, reason: use source file name */
/* loaded from: classes.dex */
public final class ViewGroup {

    /* compiled from: ViewGroup.kt */
    @Metadata(bv = {}, d1 = {"\u0000\u0015\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u000f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00020\u0003H\u0096\u0002¨\u0006\u0005"}, d2 = {"androidx/core/view/c0$a", "Lrd/h;", "Landroid/view/View;", "", "iterator", "core-ktx_release"}, k = 1, mv = {1, 7, 1})
    /* renamed from: androidx.core.view.c0$a */
    /* loaded from: classes.dex */
    public static final class a implements Sequence<View> {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ android.view.ViewGroup f2354a;

        a(android.view.ViewGroup viewGroup) {
            this.f2354a = viewGroup;
        }

        @Override // rd.Sequence
        public Iterator<View> iterator() {
            return ViewGroup.c(this.f2354a);
        }
    }

    /* compiled from: ViewGroup.kt */
    @Metadata(bv = {}, d1 = {"\u0000#\n\u0000\n\u0002\u0010)\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\t\u0010\u0004\u001a\u00020\u0003H\u0096\u0002J\t\u0010\u0005\u001a\u00020\u0002H\u0096\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0016R\u0016\u0010\u000b\u001a\u00020\b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\t\u0010\n¨\u0006\f"}, d2 = {"androidx/core/view/c0$b", "", "Landroid/view/View;", "", "hasNext", "b", "Lma/f0;", EventType.STATE_PACKAGE_CHANGED_REMOVE, "", "e", "I", ThermalBaseConfig.Item.ATTR_INDEX, "core-ktx_release"}, k = 1, mv = {1, 7, 1})
    /* renamed from: androidx.core.view.c0$b */
    /* loaded from: classes.dex */
    public static final class b implements Iterator<View>, ab.a {

        /* renamed from: e, reason: collision with root package name and from kotlin metadata */
        private int index;

        /* renamed from: f, reason: collision with root package name */
        final /* synthetic */ android.view.ViewGroup f2356f;

        b(android.view.ViewGroup viewGroup) {
            this.f2356f = viewGroup;
        }

        @Override // java.util.Iterator
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public View next() {
            android.view.ViewGroup viewGroup = this.f2356f;
            int i10 = this.index;
            this.index = i10 + 1;
            View childAt = viewGroup.getChildAt(i10);
            if (childAt != null) {
                return childAt;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < this.f2356f.getChildCount();
        }

        @Override // java.util.Iterator
        public void remove() {
            android.view.ViewGroup viewGroup = this.f2356f;
            int i10 = this.index - 1;
            this.index = i10;
            viewGroup.removeViewAt(i10);
        }
    }

    public static final View a(android.view.ViewGroup viewGroup, int i10) {
        za.k.e(viewGroup, "<this>");
        View childAt = viewGroup.getChildAt(i10);
        if (childAt != null) {
            return childAt;
        }
        throw new IndexOutOfBoundsException("Index: " + i10 + ", Size: " + viewGroup.getChildCount());
    }

    public static final Sequence<View> b(android.view.ViewGroup viewGroup) {
        za.k.e(viewGroup, "<this>");
        return new a(viewGroup);
    }

    public static final Iterator<View> c(android.view.ViewGroup viewGroup) {
        za.k.e(viewGroup, "<this>");
        return new b(viewGroup);
    }
}
