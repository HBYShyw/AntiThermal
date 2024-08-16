package n3;

import android.annotation.TargetApi;
import android.os.SystemClock;
import android.view.Choreographer;

/* compiled from: AndroidSpringLooperFactory.java */
/* renamed from: n3.a, reason: use source file name */
/* loaded from: classes.dex */
abstract class AndroidSpringLooperFactory {

    /* compiled from: AndroidSpringLooperFactory.java */
    @TargetApi(16)
    /* renamed from: n3.a$a */
    /* loaded from: classes.dex */
    private static class a extends SpringLooper {

        /* renamed from: b, reason: collision with root package name */
        private final Choreographer f15715b;

        /* renamed from: c, reason: collision with root package name */
        private final Choreographer.FrameCallback f15716c = new ChoreographerFrameCallbackC0080a();

        /* renamed from: d, reason: collision with root package name */
        private boolean f15717d;

        /* renamed from: e, reason: collision with root package name */
        private long f15718e;

        /* compiled from: AndroidSpringLooperFactory.java */
        /* renamed from: n3.a$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class ChoreographerFrameCallbackC0080a implements Choreographer.FrameCallback {
            ChoreographerFrameCallbackC0080a() {
            }

            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long j10) {
                if (!a.this.f15717d || a.this.f15751a == null) {
                    return;
                }
                long uptimeMillis = SystemClock.uptimeMillis();
                a.this.f15751a.e(uptimeMillis - r0.f15718e);
                a.this.f15718e = uptimeMillis;
                a.this.f15715b.postFrameCallback(a.this.f15716c);
            }
        }

        public a(Choreographer choreographer) {
            this.f15715b = choreographer;
        }

        public static a i() {
            return new a(Choreographer.getInstance());
        }

        @Override // n3.SpringLooper
        public void b() {
            if (this.f15717d) {
                return;
            }
            this.f15717d = true;
            this.f15718e = SystemClock.uptimeMillis();
            this.f15715b.removeFrameCallback(this.f15716c);
            this.f15715b.postFrameCallback(this.f15716c);
        }

        @Override // n3.SpringLooper
        public void c() {
            this.f15717d = false;
            this.f15715b.removeFrameCallback(this.f15716c);
        }
    }

    public static SpringLooper a() {
        return a.i();
    }
}
