package com.android.server.location.injector;

import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.server.location.LocationManagerService;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class PackageResetHelper {
    private final CopyOnWriteArrayList<Responder> mResponders = new CopyOnWriteArrayList<>();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Responder {
        boolean isResetableForPackage(String str);

        void onPackageReset(String str);
    }

    @GuardedBy({"this"})
    protected abstract void onRegister();

    @GuardedBy({"this"})
    protected abstract void onUnregister();

    public synchronized void register(Responder responder) {
        boolean isEmpty = this.mResponders.isEmpty();
        this.mResponders.add(responder);
        if (isEmpty) {
            onRegister();
        }
    }

    public synchronized void unregister(Responder responder) {
        this.mResponders.remove(responder);
        if (this.mResponders.isEmpty()) {
            onUnregister();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void notifyPackageReset(String str) {
        if (LocationManagerService.D) {
            Log.d(LocationManagerService.TAG, "package " + str + " reset");
        }
        Iterator<Responder> it = this.mResponders.iterator();
        while (it.hasNext()) {
            it.next().onPackageReset(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean queryResetableForPackage(String str) {
        Iterator<Responder> it = this.mResponders.iterator();
        while (it.hasNext()) {
            if (it.next().isResetableForPackage(str)) {
                return true;
            }
        }
        return false;
    }
}
