package androidx.core.app;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobServiceEngine;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.HashMap;

@Deprecated
/* loaded from: classes.dex */
public abstract class JobIntentService extends Service {

    /* renamed from: l, reason: collision with root package name */
    static final Object f2087l = new Object();

    /* renamed from: m, reason: collision with root package name */
    static final HashMap<ComponentName, f> f2088m = new HashMap<>();

    /* renamed from: e, reason: collision with root package name */
    b f2089e;

    /* renamed from: f, reason: collision with root package name */
    f f2090f;

    /* renamed from: g, reason: collision with root package name */
    a f2091g;

    /* renamed from: h, reason: collision with root package name */
    boolean f2092h = false;

    /* renamed from: i, reason: collision with root package name */
    boolean f2093i = false;

    /* renamed from: j, reason: collision with root package name */
    boolean f2094j = false;

    /* renamed from: k, reason: collision with root package name */
    final ArrayList<c> f2095k = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class a extends AsyncTask<Void, Void, Void> {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(Void... voidArr) {
            while (true) {
                d a10 = JobIntentService.this.a();
                if (a10 == null) {
                    return null;
                }
                JobIntentService.this.d(a10.getIntent());
                a10.a();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: b, reason: merged with bridge method [inline-methods] */
        public void onCancelled(Void r12) {
            JobIntentService.this.f();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: c, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(Void r12) {
            JobIntentService.this.f();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface b {
        IBinder a();

        d b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class c implements d {

        /* renamed from: a, reason: collision with root package name */
        final Intent f2097a;

        /* renamed from: b, reason: collision with root package name */
        final int f2098b;

        c(Intent intent, int i10) {
            this.f2097a = intent;
            this.f2098b = i10;
        }

        @Override // androidx.core.app.JobIntentService.d
        public void a() {
            JobIntentService.this.stopSelf(this.f2098b);
        }

        @Override // androidx.core.app.JobIntentService.d
        public Intent getIntent() {
            return this.f2097a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface d {
        void a();

        Intent getIntent();
    }

    /* loaded from: classes.dex */
    static final class e extends JobServiceEngine implements b {

        /* renamed from: a, reason: collision with root package name */
        final JobIntentService f2100a;

        /* renamed from: b, reason: collision with root package name */
        final Object f2101b;

        /* renamed from: c, reason: collision with root package name */
        JobParameters f2102c;

        /* loaded from: classes.dex */
        final class a implements d {

            /* renamed from: a, reason: collision with root package name */
            final JobWorkItem f2103a;

            a(JobWorkItem jobWorkItem) {
                this.f2103a = jobWorkItem;
            }

            @Override // androidx.core.app.JobIntentService.d
            public void a() {
                synchronized (e.this.f2101b) {
                    JobParameters jobParameters = e.this.f2102c;
                    if (jobParameters != null) {
                        jobParameters.completeWork(this.f2103a);
                    }
                }
            }

            @Override // androidx.core.app.JobIntentService.d
            public Intent getIntent() {
                return this.f2103a.getIntent();
            }
        }

        e(JobIntentService jobIntentService) {
            super(jobIntentService);
            this.f2101b = new Object();
            this.f2100a = jobIntentService;
        }

        @Override // androidx.core.app.JobIntentService.b
        public IBinder a() {
            return getBinder();
        }

        @Override // androidx.core.app.JobIntentService.b
        public d b() {
            synchronized (this.f2101b) {
                JobParameters jobParameters = this.f2102c;
                if (jobParameters == null) {
                    return null;
                }
                JobWorkItem dequeueWork = jobParameters.dequeueWork();
                if (dequeueWork == null) {
                    return null;
                }
                dequeueWork.getIntent().setExtrasClassLoader(this.f2100a.getClassLoader());
                return new a(dequeueWork);
            }
        }

        @Override // android.app.job.JobServiceEngine
        public boolean onStartJob(JobParameters jobParameters) {
            this.f2102c = jobParameters;
            this.f2100a.c(false);
            return true;
        }

        @Override // android.app.job.JobServiceEngine
        public boolean onStopJob(JobParameters jobParameters) {
            boolean b10 = this.f2100a.b();
            synchronized (this.f2101b) {
                this.f2102c = null;
            }
            return b10;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class f {
        public abstract void a();

        public abstract void b();

        public abstract void c();
    }

    d a() {
        b bVar = this.f2089e;
        if (bVar != null) {
            return bVar.b();
        }
        synchronized (this.f2095k) {
            if (this.f2095k.size() <= 0) {
                return null;
            }
            return this.f2095k.remove(0);
        }
    }

    boolean b() {
        a aVar = this.f2091g;
        if (aVar != null) {
            aVar.cancel(this.f2092h);
        }
        this.f2093i = true;
        return e();
    }

    void c(boolean z10) {
        if (this.f2091g == null) {
            this.f2091g = new a();
            f fVar = this.f2090f;
            if (fVar != null && z10) {
                fVar.b();
            }
            this.f2091g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    protected abstract void d(Intent intent);

    public boolean e() {
        return true;
    }

    void f() {
        ArrayList<c> arrayList = this.f2095k;
        if (arrayList != null) {
            synchronized (arrayList) {
                this.f2091g = null;
                ArrayList<c> arrayList2 = this.f2095k;
                if (arrayList2 != null && arrayList2.size() > 0) {
                    c(false);
                } else if (!this.f2094j) {
                    this.f2090f.a();
                }
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        b bVar = this.f2089e;
        if (bVar != null) {
            return bVar.a();
        }
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f2089e = new e(this);
        this.f2090f = null;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        ArrayList<c> arrayList = this.f2095k;
        if (arrayList != null) {
            synchronized (arrayList) {
                this.f2094j = true;
                this.f2090f.a();
            }
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i10, int i11) {
        if (this.f2095k == null) {
            return 2;
        }
        this.f2090f.c();
        synchronized (this.f2095k) {
            ArrayList<c> arrayList = this.f2095k;
            if (intent == null) {
                intent = new Intent();
            }
            arrayList.add(new c(intent, i11));
            c(true);
        }
        return 3;
    }
}
