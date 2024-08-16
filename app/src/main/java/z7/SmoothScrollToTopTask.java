package z7;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import b6.LocalLog;
import z7.SmoothScrollToTopTask;

/* compiled from: SmoothScrollToTopTask.java */
/* renamed from: z7.e, reason: use source file name */
/* loaded from: classes.dex */
public class SmoothScrollToTopTask implements Runnable {

    /* renamed from: g, reason: collision with root package name */
    private final ListView f20259g;

    /* renamed from: h, reason: collision with root package name */
    private boolean f20260h = false;

    /* renamed from: i, reason: collision with root package name */
    private boolean f20261i = false;

    /* renamed from: j, reason: collision with root package name */
    private boolean f20262j = false;

    /* renamed from: k, reason: collision with root package name */
    private Handler f20263k = new a();

    /* renamed from: f, reason: collision with root package name */
    private final int f20258f = 0;

    /* renamed from: e, reason: collision with root package name */
    private final int f20257e = 300;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SmoothScrollToTopTask.java */
    /* renamed from: z7.e$a */
    /* loaded from: classes.dex */
    public class a extends Handler {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b() {
            SmoothScrollToTopTask.this.k();
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i10 = message.what;
            SmoothScrollToTopTask.this.f20259g.smoothScrollToPosition(i10);
            int i11 = i10 - 1;
            if (SmoothScrollToTopTask.this.f20262j) {
                LocalLog.a("SmoothScroolToTopTask", "force stop scroll");
                SmoothScrollToTopTask.this.f20260h = false;
                SmoothScrollToTopTask.this.f20262j = false;
                SmoothScrollToTopTask.this.f20259g.smoothScrollToPosition(i11);
                SmoothScrollToTopTask.this.k();
                return;
            }
            if (i11 != -1) {
                SmoothScrollToTopTask.this.f20263k.sendEmptyMessageDelayed(i11, 12L);
                return;
            }
            LocalLog.a("SmoothScroolToTopTask", "position " + i11);
            SmoothScrollToTopTask.this.f20259g.postDelayed(new Runnable() { // from class: z7.d
                @Override // java.lang.Runnable
                public final void run() {
                    SmoothScrollToTopTask.a.this.b();
                }
            }, (long) SmoothScrollToTopTask.this.f20257e);
        }
    }

    public SmoothScrollToTopTask(ListView listView) {
        this.f20259g = listView;
    }

    public boolean g() {
        return this.f20260h;
    }

    public void h(boolean z10) {
        this.f20261i = z10;
    }

    public void i(boolean z10) {
        this.f20262j = z10;
    }

    public void j() {
        if (this.f20260h) {
            return;
        }
        this.f20260h = true;
        ListView listView = this.f20259g;
        if (listView != null && listView.getAdapter() != null && this.f20259g.getAdapter().getCount() > 0) {
            boolean z10 = false;
            View childAt = this.f20259g.getChildAt(0);
            if (childAt == null) {
                k();
                return;
            }
            int firstVisiblePosition = this.f20259g.getFirstVisiblePosition();
            LocalLog.a("SmoothScroolToTopTask", "firstVisiblePosition=" + firstVisiblePosition + " firstVisiViewTop=" + childAt.getTop() + " listPaddingTop=" + this.f20259g.getPaddingTop() + " dividerHeight=" + this.f20259g.getDividerHeight() + " listViewHeight=" + this.f20259g.getHeight());
            if (firstVisiblePosition == 0) {
                z10 = childAt.getTop() == this.f20259g.getPaddingTop();
            }
            if (z10) {
                k();
                return;
            } else {
                this.f20259g.postOnAnimation(this);
                return;
            }
        }
        k();
    }

    public void k() {
        LocalLog.a("SmoothScroolToTopTask", "stop smooth");
        this.f20260h = false;
        this.f20262j = false;
        Handler handler = this.f20263k;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        int firstVisiblePosition = this.f20259g.getFirstVisiblePosition();
        if (firstVisiblePosition >= 0) {
            this.f20263k.sendEmptyMessageDelayed(firstVisiblePosition, 12L);
        }
    }
}
