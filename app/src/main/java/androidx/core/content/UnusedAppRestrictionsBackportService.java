package androidx.core.content;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import o.IUnusedAppRestrictionsBackportCallback;
import o.IUnusedAppRestrictionsBackportService;

/* loaded from: classes.dex */
public abstract class UnusedAppRestrictionsBackportService extends Service {

    /* renamed from: e, reason: collision with root package name */
    private IUnusedAppRestrictionsBackportService.a f2130e = new a();

    /* loaded from: classes.dex */
    class a extends IUnusedAppRestrictionsBackportService.a {
        a() {
        }

        @Override // o.IUnusedAppRestrictionsBackportService
        public void l(IUnusedAppRestrictionsBackportCallback iUnusedAppRestrictionsBackportCallback) {
            if (iUnusedAppRestrictionsBackportCallback == null) {
                return;
            }
            UnusedAppRestrictionsBackportService.this.a(new UnusedAppRestrictionsBackportCallback(iUnusedAppRestrictionsBackportCallback));
        }
    }

    protected abstract void a(UnusedAppRestrictionsBackportCallback unusedAppRestrictionsBackportCallback);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f2130e;
    }
}
