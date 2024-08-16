package w2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import b2.COUILog;
import java.lang.ref.WeakReference;

/* compiled from: COUIStatusBarResponseUtil.java */
/* renamed from: w2.a, reason: use source file name */
/* loaded from: classes.dex */
public class COUIStatusBarResponseUtil {

    /* renamed from: a, reason: collision with root package name */
    private BroadcastReceiver f19323a;

    /* renamed from: b, reason: collision with root package name */
    private Activity f19324b;

    /* renamed from: d, reason: collision with root package name */
    private c f19326d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f19327e;

    /* renamed from: f, reason: collision with root package name */
    private Handler f19328f;

    /* renamed from: g, reason: collision with root package name */
    private b f19329g;

    /* renamed from: c, reason: collision with root package name */
    private final String f19325c = "COUIStatusBarResponseUtil";

    /* renamed from: h, reason: collision with root package name */
    private int f19330h = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: COUIStatusBarResponseUtil.java */
    /* renamed from: w2.a$a */
    /* loaded from: classes.dex */
    public class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            COUILog.c("COUIStatusBarResponseUtil", "The broadcast receiver was registered successfully and receives the broadcast");
            if (COUIStatusBarResponseUtil.this.f19326d != null) {
                COUIStatusBarResponseUtil.this.f19326d.b();
                COUILog.c("COUIStatusBarResponseUtil", "onStatusBarClicked is called at time :" + System.currentTimeMillis());
            }
        }
    }

    /* compiled from: COUIStatusBarResponseUtil.java */
    /* renamed from: w2.a$b */
    /* loaded from: classes.dex */
    private class b implements Runnable {

        /* renamed from: e, reason: collision with root package name */
        private int f19332e;

        /* renamed from: f, reason: collision with root package name */
        private WeakReference<Context> f19333f;

        public b(Context context) {
            this.f19333f = new WeakReference<>(context);
        }

        public void a(int i10) {
            this.f19332e = i10;
        }

        @Override // java.lang.Runnable
        public void run() {
            Context context = this.f19333f.get();
            if (context == null) {
                COUILog.b("COUIStatusBarResponseUtil", "lost mContextRef , failed to execute mBroadcastDelayRunnable");
            } else if (this.f19332e == 0) {
                COUIStatusBarResponseUtil.this.d(context);
            } else {
                COUIStatusBarResponseUtil.this.h(context);
                this.f19333f.clear();
            }
        }
    }

    /* compiled from: COUIStatusBarResponseUtil.java */
    /* renamed from: w2.a$c */
    /* loaded from: classes.dex */
    public interface c {
        void b();
    }

    public COUIStatusBarResponseUtil(Activity activity) {
        this.f19324b = activity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(Context context) {
        if (this.f19327e) {
            return;
        }
        this.f19323a = new a();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.oplus.clicktop");
        intentFilter.addAction(l3.a.b().a());
        this.f19327e = true;
        if (context.getApplicationInfo().targetSdkVersion > 31) {
            context.registerReceiver(this.f19323a, intentFilter, 2);
        } else {
            context.registerReceiver(this.f19323a, intentFilter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h(Context context) {
        if (this.f19327e) {
            this.f19327e = false;
            context.unregisterReceiver(this.f19323a);
        }
    }

    public void e() {
        Handler handler = this.f19328f;
        if (handler != null) {
            handler.removeCallbacks(this.f19329g);
            this.f19329g.a(1);
            this.f19328f.postDelayed(this.f19329g, this.f19330h);
            this.f19328f = null;
            this.f19329g = null;
        }
    }

    public void f() {
        if (this.f19328f == null && this.f19329g == null) {
            this.f19328f = new Handler(Looper.myLooper());
            b bVar = new b(this.f19324b);
            this.f19329g = bVar;
            bVar.a(0);
            this.f19328f.postDelayed(this.f19329g, this.f19330h);
            return;
        }
        COUILog.b("COUIStatusBarResponseUtil", "onResume call multiple times");
    }

    public void g(c cVar) {
        this.f19326d = cVar;
    }
}
