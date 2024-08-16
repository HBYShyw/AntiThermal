package androidx.room;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import androidx.room.IMultiInstanceInvalidationService;
import j.SparseArrayCompat;

/* loaded from: classes.dex */
public class MultiInstanceInvalidationService extends Service {

    /* renamed from: e, reason: collision with root package name */
    int f3823e = 0;

    /* renamed from: f, reason: collision with root package name */
    final SparseArrayCompat<String> f3824f = new SparseArrayCompat<>();

    /* renamed from: g, reason: collision with root package name */
    final RemoteCallbackList<IMultiInstanceInvalidationCallback> f3825g = new a();

    /* renamed from: h, reason: collision with root package name */
    private final IMultiInstanceInvalidationService.a f3826h = new b();

    /* loaded from: classes.dex */
    class a extends RemoteCallbackList<IMultiInstanceInvalidationCallback> {
        a() {
        }

        @Override // android.os.RemoteCallbackList
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public void onCallbackDied(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, Object obj) {
            MultiInstanceInvalidationService.this.f3824f.j(((Integer) obj).intValue());
        }
    }

    /* loaded from: classes.dex */
    class b extends IMultiInstanceInvalidationService.a {
        b() {
        }

        @Override // androidx.room.IMultiInstanceInvalidationService
        public int f(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, String str) {
            if (str == null) {
                return 0;
            }
            synchronized (MultiInstanceInvalidationService.this.f3825g) {
                MultiInstanceInvalidationService multiInstanceInvalidationService = MultiInstanceInvalidationService.this;
                int i10 = multiInstanceInvalidationService.f3823e + 1;
                multiInstanceInvalidationService.f3823e = i10;
                if (multiInstanceInvalidationService.f3825g.register(iMultiInstanceInvalidationCallback, Integer.valueOf(i10))) {
                    MultiInstanceInvalidationService.this.f3824f.a(i10, str);
                    return i10;
                }
                MultiInstanceInvalidationService multiInstanceInvalidationService2 = MultiInstanceInvalidationService.this;
                multiInstanceInvalidationService2.f3823e--;
                return 0;
            }
        }

        @Override // androidx.room.IMultiInstanceInvalidationService
        public void s(int i10, String[] strArr) {
            synchronized (MultiInstanceInvalidationService.this.f3825g) {
                String e10 = MultiInstanceInvalidationService.this.f3824f.e(i10);
                if (e10 == null) {
                    Log.w("ROOM", "Remote invalidation client ID not registered");
                    return;
                }
                int beginBroadcast = MultiInstanceInvalidationService.this.f3825g.beginBroadcast();
                for (int i11 = 0; i11 < beginBroadcast; i11++) {
                    try {
                        int intValue = ((Integer) MultiInstanceInvalidationService.this.f3825g.getBroadcastCookie(i11)).intValue();
                        String e11 = MultiInstanceInvalidationService.this.f3824f.e(intValue);
                        if (i10 != intValue && e10.equals(e11)) {
                            try {
                                MultiInstanceInvalidationService.this.f3825g.getBroadcastItem(i11).c(strArr);
                            } catch (RemoteException e12) {
                                Log.w("ROOM", "Error invoking a remote callback", e12);
                            }
                        }
                    } finally {
                        MultiInstanceInvalidationService.this.f3825g.finishBroadcast();
                    }
                }
            }
        }

        @Override // androidx.room.IMultiInstanceInvalidationService
        public void v(IMultiInstanceInvalidationCallback iMultiInstanceInvalidationCallback, int i10) {
            synchronized (MultiInstanceInvalidationService.this.f3825g) {
                MultiInstanceInvalidationService.this.f3825g.unregister(iMultiInstanceInvalidationCallback);
                MultiInstanceInvalidationService.this.f3824f.j(i10);
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f3826h;
    }
}
