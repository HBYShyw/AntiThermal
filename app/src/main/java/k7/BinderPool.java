package k7;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.oplus.ovoicemanager.service.IBinderPool;

/* compiled from: BinderPool.java */
/* renamed from: k7.b, reason: use source file name */
/* loaded from: classes.dex */
public class BinderPool {

    /* renamed from: e, reason: collision with root package name */
    private static volatile BinderPool f14072e;

    /* renamed from: a, reason: collision with root package name */
    private Context f14073a;

    /* renamed from: b, reason: collision with root package name */
    private IBinderPool f14074b;

    /* renamed from: c, reason: collision with root package name */
    private IBinderCallback f14075c;

    /* renamed from: d, reason: collision with root package name */
    private ServiceConnection f14076d = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: BinderPool.java */
    /* renamed from: k7.b$a */
    /* loaded from: classes.dex */
    public class a implements ServiceConnection {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ IBinderCallback f14077a;

        /* renamed from: b, reason: collision with root package name */
        final /* synthetic */ Object f14078b;

        a(IBinderCallback iBinderCallback, Object obj) {
            this.f14077a = iBinderCallback;
            this.f14078b = obj;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("OVSS.BinderPool", "onServiceConnected");
            BinderPool.this.f14074b = IBinderPool.a.z(iBinder);
            this.f14077a.onServiceConnected(componentName, iBinder);
            Object obj = this.f14078b;
            if (obj != null) {
                synchronized (obj) {
                    this.f14078b.notifyAll();
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("OVSS.BinderPool", "onServiceDisconnected");
            this.f14077a.onServiceDisconnected(componentName);
            Object obj = this.f14078b;
            if (obj != null) {
                synchronized (obj) {
                    this.f14078b.notifyAll();
                }
            }
        }
    }

    private BinderPool(Context context, IBinderCallback iBinderCallback) {
        this.f14073a = context;
        this.f14075c = iBinderCallback;
    }

    private boolean c(IBinderCallback iBinderCallback, Object obj) {
        Log.d("OVSS.BinderPool", "connectService");
        IBinderPool iBinderPool = this.f14074b;
        if (iBinderPool != null && iBinderPool.asBinder().isBinderAlive() && this.f14074b.asBinder().pingBinder()) {
            Log.d("OVSS.BinderPool", "mBinderPool is alive");
            iBinderCallback.onServiceConnected(null, null);
            if (obj != null) {
                synchronized (obj) {
                    obj.notifyAll();
                }
            }
            return true;
        }
        if (this.f14073a == null) {
            return false;
        }
        Intent intent = new Intent("com.oplus.intent.action.OVoiceSkillService");
        intent.setPackage("com.oplus.ovoicemanager");
        String e10 = k7.a.e(this.f14073a);
        String a10 = k7.a.a(this.f14073a);
        Log.d("OVSS.BinderPool", String.format("connectService,packageName[%s]appName[%s]", e10, a10));
        intent.putExtra("packageName", e10);
        intent.putExtra("appName", a10);
        a aVar = new a(iBinderCallback, obj);
        this.f14076d = aVar;
        return this.f14073a.bindService(intent, aVar, 1);
    }

    public static BinderPool e(Context context, IBinderCallback iBinderCallback) {
        if (f14072e == null) {
            synchronized (BinderPool.class) {
                if (f14072e == null) {
                    f14072e = new BinderPool(context, iBinderCallback);
                }
            }
        }
        return f14072e;
    }

    public boolean b(Object obj) {
        return c(this.f14075c, obj);
    }

    public void d() {
        ServiceConnection serviceConnection = this.f14076d;
        if (serviceConnection != null) {
            Context context = this.f14073a;
            if (context != null) {
                context.unbindService(serviceConnection);
            }
            this.f14076d = null;
        }
        this.f14073a = null;
    }

    public IBinder f(String str) {
        Log.d("OVSS.BinderPool", "queryBinder: " + str);
        IBinderPool iBinderPool = this.f14074b;
        if (iBinderPool == null) {
            Log.e("OVSS.BinderPool", "mBinderPool == null");
            return null;
        }
        try {
            return iBinderPool.g(str);
        } catch (RemoteException e10) {
            e10.printStackTrace();
            return null;
        }
    }
}
