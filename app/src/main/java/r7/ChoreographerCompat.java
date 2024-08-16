package r7;

import android.view.Choreographer;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ChoreographerCompat.java */
/* renamed from: r7.e, reason: use source file name */
/* loaded from: classes.dex */
public class ChoreographerCompat {

    /* renamed from: d, reason: collision with root package name */
    private a f17563d;

    /* renamed from: b, reason: collision with root package name */
    private Choreographer.FrameCallback f17561b = new Choreographer.FrameCallback() { // from class: r7.d
        @Override // android.view.Choreographer.FrameCallback
        public final void doFrame(long j10) {
            ChoreographerCompat.this.b(j10);
        }
    };

    /* renamed from: c, reason: collision with root package name */
    private boolean f17562c = false;

    /* renamed from: a, reason: collision with root package name */
    private Choreographer f17560a = Choreographer.getInstance();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: ChoreographerCompat.java */
    /* renamed from: r7.e$a */
    /* loaded from: classes.dex */
    public interface a {
        void doFrame(long j10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public void b(long j10) {
        this.f17562c = false;
        if (this.f17563d != null) {
            if (o7.b.a()) {
                o7.b.d("PhysicsWorld-Frame", "doFrame ----------------------- frameTime =:" + j10);
            }
            this.f17563d.doFrame(j10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        if (this.f17562c || this.f17563d == null) {
            return;
        }
        this.f17560a.postFrameCallback(this.f17561b);
        if (o7.b.a()) {
            o7.b.d("PhysicsWorld-Frame", "scheduleNextFrame ----------------------- ");
        }
        this.f17562c = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(a aVar) {
        this.f17563d = aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        if (this.f17562c) {
            if (o7.b.a()) {
                o7.b.d("PhysicsWorld-Frame", "unScheduleNextFrame ----------------------- ");
            }
            this.f17560a.removeFrameCallback(this.f17561b);
            this.f17562c = false;
        }
    }
}
