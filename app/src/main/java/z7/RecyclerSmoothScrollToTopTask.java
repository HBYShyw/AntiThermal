package z7;

import android.view.View;
import androidx.recyclerview.widget.COUIRecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import b6.LocalLog;

/* compiled from: RecyclerSmoothScrollToTopTask.java */
/* renamed from: z7.c, reason: use source file name */
/* loaded from: classes.dex */
public class RecyclerSmoothScrollToTopTask implements Runnable {

    /* renamed from: e, reason: collision with root package name */
    private final COUIRecyclerView f20253e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f20254f = false;

    /* renamed from: g, reason: collision with root package name */
    private boolean f20255g = false;

    public RecyclerSmoothScrollToTopTask(COUIRecyclerView cOUIRecyclerView) {
        this.f20253e = cOUIRecyclerView;
    }

    private boolean b() {
        View C;
        RecyclerView.p layoutManager = this.f20253e.getLayoutManager();
        boolean z10 = false;
        if ((layoutManager instanceof LinearLayoutManager) && (C = layoutManager.C(0)) != null) {
            z10 = layoutManager.A0(C, true, true);
        }
        LocalLog.a("SmoothScroolToTopTask", "isFirstItemShow: firstItemShow: " + z10);
        return z10;
    }

    public boolean a() {
        return this.f20254f;
    }

    public void c(boolean z10) {
        this.f20255g = z10;
    }

    public void d() {
        COUIRecyclerView cOUIRecyclerView = this.f20253e;
        if (cOUIRecyclerView != null && cOUIRecyclerView.getAdapter() != null && this.f20253e.getAdapter().getItemCount() > 0) {
            int scrollState = this.f20253e.getScrollState();
            if (scrollState == 1) {
                LocalLog.a("SmoothScroolToTopTask", "start: scroll state is dragging, will return;");
                return;
            }
            if (scrollState == 2 && b()) {
                LocalLog.a("SmoothScroolToTopTask", "start: first item is show, will return;");
                return;
            }
            if (this.f20254f) {
                return;
            }
            this.f20254f = true;
            boolean z10 = false;
            View childAt = this.f20253e.getChildAt(0);
            if (childAt == null) {
                e();
                return;
            }
            int b22 = ((LinearLayoutManager) this.f20253e.getLayoutManager()).b2();
            LocalLog.a("SmoothScroolToTopTask", "firstVisiblePosition=" + b22 + " firstVisiViewTop=" + childAt.getTop() + " listPaddingTop=" + this.f20253e.getPaddingTop() + " dividerHeight=0 COUIRecyclerViewHeight=" + this.f20253e.getHeight());
            if (b22 == 0) {
                z10 = childAt.getTop() == this.f20253e.getPaddingTop();
            }
            if (z10) {
                e();
                return;
            } else {
                this.f20253e.postOnAnimation(this);
                return;
            }
        }
        e();
    }

    public void e() {
        LocalLog.a("SmoothScroolToTopTask", "stop smooth");
        this.f20254f = false;
        this.f20255g = false;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f20253e.smoothScrollToPosition(0);
        e();
    }
}
