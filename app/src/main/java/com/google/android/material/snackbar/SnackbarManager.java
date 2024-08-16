package com.google.android.material.snackbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

/* compiled from: SnackbarManager.java */
/* renamed from: com.google.android.material.snackbar.b, reason: use source file name */
/* loaded from: classes.dex */
class SnackbarManager {

    /* renamed from: e, reason: collision with root package name */
    private static SnackbarManager f9245e;

    /* renamed from: a, reason: collision with root package name */
    private final Object f9246a = new Object();

    /* renamed from: b, reason: collision with root package name */
    private final Handler f9247b = new Handler(Looper.getMainLooper(), new a());

    /* renamed from: c, reason: collision with root package name */
    private c f9248c;

    /* renamed from: d, reason: collision with root package name */
    private c f9249d;

    /* compiled from: SnackbarManager.java */
    /* renamed from: com.google.android.material.snackbar.b$a */
    /* loaded from: classes.dex */
    class a implements Handler.Callback {
        a() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what != 0) {
                return false;
            }
            SnackbarManager.this.d((c) message.obj);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: SnackbarManager.java */
    /* renamed from: com.google.android.material.snackbar.b$b */
    /* loaded from: classes.dex */
    public interface b {
        void a(int i10);

        void b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SnackbarManager.java */
    /* renamed from: com.google.android.material.snackbar.b$c */
    /* loaded from: classes.dex */
    public static class c {

        /* renamed from: a, reason: collision with root package name */
        final WeakReference<b> f9251a;

        /* renamed from: b, reason: collision with root package name */
        int f9252b;

        /* renamed from: c, reason: collision with root package name */
        boolean f9253c;

        boolean a(b bVar) {
            return bVar != null && this.f9251a.get() == bVar;
        }
    }

    private SnackbarManager() {
    }

    private boolean a(c cVar, int i10) {
        b bVar = cVar.f9251a.get();
        if (bVar == null) {
            return false;
        }
        this.f9247b.removeCallbacksAndMessages(cVar);
        bVar.a(i10);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SnackbarManager c() {
        if (f9245e == null) {
            f9245e = new SnackbarManager();
        }
        return f9245e;
    }

    private boolean f(b bVar) {
        c cVar = this.f9248c;
        return cVar != null && cVar.a(bVar);
    }

    private boolean g(b bVar) {
        c cVar = this.f9249d;
        return cVar != null && cVar.a(bVar);
    }

    private void l(c cVar) {
        int i10 = cVar.f9252b;
        if (i10 == -2) {
            return;
        }
        if (i10 <= 0) {
            i10 = i10 == -1 ? 1500 : 2750;
        }
        this.f9247b.removeCallbacksAndMessages(cVar);
        Handler handler = this.f9247b;
        handler.sendMessageDelayed(Message.obtain(handler, 0, cVar), i10);
    }

    private void m() {
        c cVar = this.f9249d;
        if (cVar != null) {
            this.f9248c = cVar;
            this.f9249d = null;
            b bVar = cVar.f9251a.get();
            if (bVar != null) {
                bVar.b();
            } else {
                this.f9248c = null;
            }
        }
    }

    public void b(b bVar, int i10) {
        synchronized (this.f9246a) {
            if (f(bVar)) {
                a(this.f9248c, i10);
            } else if (g(bVar)) {
                a(this.f9249d, i10);
            }
        }
    }

    void d(c cVar) {
        synchronized (this.f9246a) {
            if (this.f9248c == cVar || this.f9249d == cVar) {
                a(cVar, 2);
            }
        }
    }

    public boolean e(b bVar) {
        boolean z10;
        synchronized (this.f9246a) {
            z10 = f(bVar) || g(bVar);
        }
        return z10;
    }

    public void h(b bVar) {
        synchronized (this.f9246a) {
            if (f(bVar)) {
                this.f9248c = null;
                if (this.f9249d != null) {
                    m();
                }
            }
        }
    }

    public void i(b bVar) {
        synchronized (this.f9246a) {
            if (f(bVar)) {
                l(this.f9248c);
            }
        }
    }

    public void j(b bVar) {
        synchronized (this.f9246a) {
            if (f(bVar)) {
                c cVar = this.f9248c;
                if (!cVar.f9253c) {
                    cVar.f9253c = true;
                    this.f9247b.removeCallbacksAndMessages(cVar);
                }
            }
        }
    }

    public void k(b bVar) {
        synchronized (this.f9246a) {
            if (f(bVar)) {
                c cVar = this.f9248c;
                if (cVar.f9253c) {
                    cVar.f9253c = false;
                    l(cVar);
                }
            }
        }
    }
}
