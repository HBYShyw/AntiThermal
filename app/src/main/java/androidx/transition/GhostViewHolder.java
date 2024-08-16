package androidx.transition;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;

/* compiled from: GhostViewHolder.java */
@SuppressLint({"ViewConstructor"})
/* renamed from: androidx.transition.g, reason: use source file name */
/* loaded from: classes.dex */
class GhostViewHolder extends FrameLayout {

    /* renamed from: e, reason: collision with root package name */
    private ViewGroup f4111e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f4112f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GhostViewHolder(ViewGroup viewGroup) {
        super(viewGroup.getContext());
        setClipChildren(false);
        this.f4111e = viewGroup;
        viewGroup.setTag(R$id.ghost_view_holder, this);
        a0.b(this.f4111e).add(this);
        this.f4112f = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GhostViewHolder b(ViewGroup viewGroup) {
        return (GhostViewHolder) viewGroup.getTag(R$id.ghost_view_holder);
    }

    private int c(ArrayList<View> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        int childCount = getChildCount() - 1;
        int i10 = 0;
        while (i10 <= childCount) {
            int i11 = (i10 + childCount) / 2;
            d(((GhostViewPort) getChildAt(i11)).f4115g, arrayList2);
            if (f(arrayList, arrayList2)) {
                i10 = i11 + 1;
            } else {
                childCount = i11 - 1;
            }
            arrayList2.clear();
        }
        return i10;
    }

    private static void d(View view, ArrayList<View> arrayList) {
        Object parent = view.getParent();
        if (parent instanceof ViewGroup) {
            d((View) parent, arrayList);
        }
        arrayList.add(view);
    }

    private static boolean e(View view, View view2) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        int childCount = viewGroup.getChildCount();
        if (view.getZ() != view2.getZ()) {
            return view.getZ() > view2.getZ();
        }
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = viewGroup.getChildAt(a0.a(viewGroup, i10));
            if (childAt == view) {
                return false;
            }
            if (childAt == view2) {
                break;
            }
        }
        return true;
    }

    private static boolean f(ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        if (arrayList.isEmpty() || arrayList2.isEmpty() || arrayList.get(0) != arrayList2.get(0)) {
            return true;
        }
        int min = Math.min(arrayList.size(), arrayList2.size());
        for (int i10 = 1; i10 < min; i10++) {
            View view = arrayList.get(i10);
            View view2 = arrayList2.get(i10);
            if (view != view2) {
                return e(view, view2);
            }
        }
        return arrayList2.size() == min;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(GhostViewPort ghostViewPort) {
        ArrayList<View> arrayList = new ArrayList<>();
        d(ghostViewPort.f4115g, arrayList);
        int c10 = c(arrayList);
        if (c10 >= 0 && c10 < getChildCount()) {
            addView(ghostViewPort, c10);
        } else {
            addView(ghostViewPort);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        if (this.f4112f) {
            a0.b(this.f4111e).remove(this);
            a0.b(this.f4111e).add(this);
            return;
        }
        throw new IllegalStateException("This GhostViewHolder is detached!");
    }

    @Override // android.view.ViewGroup
    public void onViewAdded(View view) {
        if (this.f4112f) {
            super.onViewAdded(view);
            return;
        }
        throw new IllegalStateException("This GhostViewHolder is detached!");
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if ((getChildCount() == 1 && getChildAt(0) == view) || getChildCount() == 0) {
            this.f4111e.setTag(R$id.ghost_view_holder, null);
            a0.b(this.f4111e).remove(this);
            this.f4112f = false;
        }
    }
}
