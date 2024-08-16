package p2;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import o2.COUIScrollViewProxy;

/* compiled from: COUIRVScrollViewProxy.java */
/* renamed from: p2.c, reason: use source file name */
/* loaded from: classes.dex */
public class COUIRVScrollViewProxy extends COUIScrollViewProxy<RecyclerView> {
    public COUIRVScrollViewProxy(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override // o2.IScrollableView
    public int a() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) ((RecyclerView) this.f16134a).getLayoutManager();
        if (linearLayoutManager != null) {
            return linearLayoutManager.p2();
        }
        return 1;
    }

    @Override // o2.IScrollableView
    public boolean b(int i10, int i11) {
        int i12 = (int) (-Math.signum(i11));
        if (i10 == 1) {
            return ((RecyclerView) this.f16134a).canScrollVertically(i12);
        }
        return ((RecyclerView) this.f16134a).canScrollHorizontally(i12);
    }
}
