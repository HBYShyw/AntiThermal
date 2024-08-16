package com.android.server.servicewatcher;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import com.android.server.FgThread;
import com.android.server.slice.SliceClientPermissions;
import java.io.PrintWriter;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface ServiceWatcher {

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface BinderOperation {
        default void onError(Throwable th) {
        }

        void run(IBinder iBinder) throws RemoteException;
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ServiceChangedListener {
        void onServiceChanged();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ServiceListener<TBoundServiceInfo extends BoundServiceInfo> {
        void onBind(IBinder iBinder, TBoundServiceInfo tboundserviceinfo) throws RemoteException;

        void onUnbind();
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ServiceSupplier<TBoundServiceInfo extends BoundServiceInfo> {
        TBoundServiceInfo getServiceInfo();

        boolean hasMatchingService();

        void register(ServiceChangedListener serviceChangedListener);

        void unregister();
    }

    boolean checkServiceResolves();

    void dump(PrintWriter printWriter);

    void register();

    void runOnBinder(BinderOperation binderOperation);

    void unregister();

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class BoundServiceInfo {
        protected final String mAction;
        protected final ComponentName mComponentName;
        protected final int mUid;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected BoundServiceInfo(String str, ResolveInfo resolveInfo) {
            this(str, r3.applicationInfo.uid, r3.getComponentName());
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public BoundServiceInfo(String str, int i, ComponentName componentName) {
            this.mAction = str;
            this.mUid = i;
            Objects.requireNonNull(componentName);
            this.mComponentName = componentName;
        }

        public String getAction() {
            return this.mAction;
        }

        public ComponentName getComponentName() {
            return this.mComponentName;
        }

        public int getUserId() {
            return UserHandle.getUserId(this.mUid);
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof BoundServiceInfo)) {
                return false;
            }
            BoundServiceInfo boundServiceInfo = (BoundServiceInfo) obj;
            return this.mUid == boundServiceInfo.mUid && Objects.equals(this.mAction, boundServiceInfo.mAction) && this.mComponentName.equals(boundServiceInfo.mComponentName);
        }

        public final int hashCode() {
            return Objects.hash(this.mAction, Integer.valueOf(this.mUid), this.mComponentName);
        }

        public String toString() {
            if (this.mComponentName == null) {
                return "none";
            }
            return this.mUid + SliceClientPermissions.SliceAuthority.DELIMITER + this.mComponentName.flattenToShortString();
        }
    }

    static <TBoundServiceInfo extends BoundServiceInfo> ServiceWatcher create(Context context, String str, ServiceSupplier<TBoundServiceInfo> serviceSupplier, ServiceListener<? super TBoundServiceInfo> serviceListener) {
        return create(context, FgThread.getHandler(), str, serviceSupplier, serviceListener);
    }

    static <TBoundServiceInfo extends BoundServiceInfo> ServiceWatcher create(Context context, Handler handler, String str, ServiceSupplier<TBoundServiceInfo> serviceSupplier, ServiceListener<? super TBoundServiceInfo> serviceListener) {
        return new ServiceWatcherImpl(context, handler, str, serviceSupplier, serviceListener);
    }
}
