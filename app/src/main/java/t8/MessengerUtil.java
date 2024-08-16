package t8;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import b6.LocalLog;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import v4.GuardElfContext;

/* compiled from: MessengerUtil.java */
/* renamed from: t8.c, reason: use source file name */
/* loaded from: classes2.dex */
public class MessengerUtil {

    /* renamed from: f, reason: collision with root package name */
    private Context f18648f;

    /* renamed from: a, reason: collision with root package name */
    Handler f18643a = null;

    /* renamed from: b, reason: collision with root package name */
    Messenger f18644b = null;

    /* renamed from: c, reason: collision with root package name */
    Messenger f18645c = null;

    /* renamed from: d, reason: collision with root package name */
    AtomicBoolean f18646d = new AtomicBoolean(false);

    /* renamed from: e, reason: collision with root package name */
    AtomicInteger f18647e = new AtomicInteger(0);

    /* renamed from: g, reason: collision with root package name */
    private ServiceConnection f18649g = new a();

    /* compiled from: MessengerUtil.java */
    /* renamed from: t8.c$a */
    /* loaded from: classes2.dex */
    class a implements ServiceConnection {
        a() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocalLog.l("MessengerUtil", "onServiceConnected");
            MessengerUtil.this.f18646d.set(true);
            MessengerUtil.this.f18645c = new Messenger(iBinder);
            try {
                int intValue = MessengerUtil.this.f18647e.intValue();
                if ((intValue & 1) != 0) {
                    MessengerUtil.this.f18647e.set(intValue & (-2));
                    MessengerUtil.this.c();
                }
                if ((intValue & 2) != 0) {
                    MessengerUtil.this.f18647e.set(intValue & (-3));
                    MessengerUtil.this.d();
                }
            } catch (Exception unused) {
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            LocalLog.l("MessengerUtil", "onServiceDisconnected");
            MessengerUtil.this.f18646d.set(false);
            MessengerUtil.this.f18645c = null;
        }
    }

    public MessengerUtil() {
        this.f18648f = null;
        this.f18648f = GuardElfContext.e().c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        Message obtain = Message.obtain();
        obtain.arg1 = 1111;
        obtain.replyTo = this.f18644b;
        try {
            this.f18645c.send(obtain);
        } catch (RemoteException e10) {
            e10.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        Message obtain = Message.obtain();
        obtain.arg1 = 1112;
        obtain.replyTo = this.f18644b;
        try {
            this.f18645c.send(obtain);
        } catch (RemoteException e10) {
            e10.printStackTrace();
        }
    }

    public void e() {
        if (!this.f18646d.get()) {
            this.f18647e.set(this.f18647e.get() | 2);
        } else {
            d();
        }
    }
}
