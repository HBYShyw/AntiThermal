package androidx.room;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.room.IMultiInstanceInvalidationCallback;
import androidx.room.IMultiInstanceInvalidationService;
import androidx.room.InvalidationTracker;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MultiInstanceInvalidationClient.java */
/* renamed from: androidx.room.f, reason: use source file name */
/* loaded from: classes.dex */
public class MultiInstanceInvalidationClient {

    /* renamed from: a, reason: collision with root package name */
    Context f3872a;

    /* renamed from: b, reason: collision with root package name */
    final String f3873b;

    /* renamed from: c, reason: collision with root package name */
    int f3874c;

    /* renamed from: d, reason: collision with root package name */
    final InvalidationTracker f3875d;

    /* renamed from: e, reason: collision with root package name */
    final InvalidationTracker.c f3876e;

    /* renamed from: f, reason: collision with root package name */
    IMultiInstanceInvalidationService f3877f;

    /* renamed from: g, reason: collision with root package name */
    final Executor f3878g;

    /* renamed from: h, reason: collision with root package name */
    final IMultiInstanceInvalidationCallback f3879h = new a();

    /* renamed from: i, reason: collision with root package name */
    final AtomicBoolean f3880i = new AtomicBoolean(false);

    /* renamed from: j, reason: collision with root package name */
    final ServiceConnection f3881j;

    /* renamed from: k, reason: collision with root package name */
    final Runnable f3882k;

    /* renamed from: l, reason: collision with root package name */
    final Runnable f3883l;

    /* renamed from: m, reason: collision with root package name */
    private final Runnable f3884m;

    /* compiled from: MultiInstanceInvalidationClient.java */
    /* renamed from: androidx.room.f$a */
    /* loaded from: classes.dex */
    class a extends IMultiInstanceInvalidationCallback.a {

        /* compiled from: MultiInstanceInvalidationClient.java */
        /* renamed from: androidx.room.f$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        class RunnableC0010a implements Runnable {

            /* renamed from: e, reason: collision with root package name */
            final /* synthetic */ String[] f3886e;

            RunnableC0010a(String[] strArr) {
                this.f3886e = strArr;
            }

            @Override // java.lang.Runnable
            public void run() {
                MultiInstanceInvalidationClient.this.f3875d.g(this.f3886e);
            }
        }

        a() {
        }

        @Override // androidx.room.IMultiInstanceInvalidationCallback
        public void c(String[] strArr) {
            MultiInstanceInvalidationClient.this.f3878g.execute(new RunnableC0010a(strArr));
        }
    }

    /* compiled from: MultiInstanceInvalidationClient.java */
    /* renamed from: androidx.room.f$b */
    /* loaded from: classes.dex */
    class b implements ServiceConnection {
        b() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MultiInstanceInvalidationClient.this.f3877f = IMultiInstanceInvalidationService.a.z(iBinder);
            MultiInstanceInvalidationClient multiInstanceInvalidationClient = MultiInstanceInvalidationClient.this;
            multiInstanceInvalidationClient.f3878g.execute(multiInstanceInvalidationClient.f3882k);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            MultiInstanceInvalidationClient multiInstanceInvalidationClient = MultiInstanceInvalidationClient.this;
            multiInstanceInvalidationClient.f3878g.execute(multiInstanceInvalidationClient.f3883l);
            MultiInstanceInvalidationClient multiInstanceInvalidationClient2 = MultiInstanceInvalidationClient.this;
            multiInstanceInvalidationClient2.f3877f = null;
            multiInstanceInvalidationClient2.f3872a = null;
        }
    }

    /* compiled from: MultiInstanceInvalidationClient.java */
    /* renamed from: androidx.room.f$c */
    /* loaded from: classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                MultiInstanceInvalidationClient multiInstanceInvalidationClient = MultiInstanceInvalidationClient.this;
                IMultiInstanceInvalidationService iMultiInstanceInvalidationService = multiInstanceInvalidationClient.f3877f;
                if (iMultiInstanceInvalidationService != null) {
                    multiInstanceInvalidationClient.f3874c = iMultiInstanceInvalidationService.f(multiInstanceInvalidationClient.f3879h, multiInstanceInvalidationClient.f3873b);
                    MultiInstanceInvalidationClient multiInstanceInvalidationClient2 = MultiInstanceInvalidationClient.this;
                    multiInstanceInvalidationClient2.f3875d.a(multiInstanceInvalidationClient2.f3876e);
                }
            } catch (RemoteException e10) {
                Log.w("ROOM", "Cannot register multi-instance invalidation callback", e10);
            }
        }
    }

    /* compiled from: MultiInstanceInvalidationClient.java */
    /* renamed from: androidx.room.f$d */
    /* loaded from: classes.dex */
    class d implements Runnable {
        d() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MultiInstanceInvalidationClient multiInstanceInvalidationClient = MultiInstanceInvalidationClient.this;
            multiInstanceInvalidationClient.f3875d.i(multiInstanceInvalidationClient.f3876e);
        }
    }

    /* compiled from: MultiInstanceInvalidationClient.java */
    /* renamed from: androidx.room.f$e */
    /* loaded from: classes.dex */
    class e implements Runnable {
        e() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MultiInstanceInvalidationClient multiInstanceInvalidationClient = MultiInstanceInvalidationClient.this;
            multiInstanceInvalidationClient.f3875d.i(multiInstanceInvalidationClient.f3876e);
            try {
                MultiInstanceInvalidationClient multiInstanceInvalidationClient2 = MultiInstanceInvalidationClient.this;
                IMultiInstanceInvalidationService iMultiInstanceInvalidationService = multiInstanceInvalidationClient2.f3877f;
                if (iMultiInstanceInvalidationService != null) {
                    iMultiInstanceInvalidationService.v(multiInstanceInvalidationClient2.f3879h, multiInstanceInvalidationClient2.f3874c);
                }
            } catch (RemoteException e10) {
                Log.w("ROOM", "Cannot unregister multi-instance invalidation callback", e10);
            }
            MultiInstanceInvalidationClient multiInstanceInvalidationClient3 = MultiInstanceInvalidationClient.this;
            Context context = multiInstanceInvalidationClient3.f3872a;
            if (context != null) {
                context.unbindService(multiInstanceInvalidationClient3.f3881j);
                MultiInstanceInvalidationClient.this.f3872a = null;
            }
        }
    }

    /* compiled from: MultiInstanceInvalidationClient.java */
    /* renamed from: androidx.room.f$f */
    /* loaded from: classes.dex */
    class f extends InvalidationTracker.c {
        f(String[] strArr) {
            super(strArr);
        }

        @Override // androidx.room.InvalidationTracker.c
        boolean a() {
            return true;
        }

        @Override // androidx.room.InvalidationTracker.c
        public void b(Set<String> set) {
            if (MultiInstanceInvalidationClient.this.f3880i.get()) {
                return;
            }
            try {
                MultiInstanceInvalidationClient multiInstanceInvalidationClient = MultiInstanceInvalidationClient.this;
                multiInstanceInvalidationClient.f3877f.s(multiInstanceInvalidationClient.f3874c, (String[]) set.toArray(new String[0]));
            } catch (RemoteException e10) {
                Log.w("ROOM", "Cannot broadcast invalidation", e10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiInstanceInvalidationClient(Context context, String str, InvalidationTracker invalidationTracker, Executor executor) {
        b bVar = new b();
        this.f3881j = bVar;
        this.f3882k = new c();
        this.f3883l = new d();
        this.f3884m = new e();
        this.f3872a = context.getApplicationContext();
        this.f3873b = str;
        this.f3875d = invalidationTracker;
        this.f3878g = executor;
        this.f3876e = new f(invalidationTracker.f3848b);
        this.f3872a.bindService(new Intent(this.f3872a, (Class<?>) MultiInstanceInvalidationService.class), bVar, 1);
    }
}
