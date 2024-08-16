package com.android.server.tracing;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IMessenger;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.tracing.ITracingServiceProxy;
import android.tracing.TraceReportParams;
import android.util.Log;
import android.util.LruCache;
import android.util.Slog;
import com.android.internal.infra.ServiceConnector;
import com.android.internal.util.FrameworkStatsLog;
import com.android.server.SystemService;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class TracingServiceProxy extends SystemService {
    private static final String INTENT_ACTION_NOTIFY_SESSION_STOLEN = "com.android.traceur.NOTIFY_SESSION_STOLEN";
    private static final String INTENT_ACTION_NOTIFY_SESSION_STOPPED = "com.android.traceur.NOTIFY_SESSION_STOPPED";
    private static final int MAX_CACHED_REPORTER_SERVICES = 8;
    private static final int MAX_FILE_SIZE_BYTES_TO_PIPE = 1024;
    private static final int REPORT_BEGIN = 1;
    private static final int REPORT_BIND_PERM_INCORRECT = 3;
    private static final int REPORT_SVC_COMM_ERROR = 5;
    private static final int REPORT_SVC_HANDOFF = 2;
    private static final int REPORT_SVC_PERM_MISSING = 4;
    private static final String TAG = "TracingServiceProxy";
    private static final String TRACING_APP_ACTIVITY = "com.android.traceur.StopTraceService";
    private static final String TRACING_APP_PACKAGE_NAME = "com.android.traceur";
    public static final String TRACING_SERVICE_PROXY_BINDER_NAME = "tracing.proxy";
    private final LruCache<ComponentName, ServiceConnector<IMessenger>> mCachedReporterServices;
    private final Context mContext;
    private final PackageManager mPackageManager;
    private final ITracingServiceProxy.Stub mTracingServiceProxy;

    public TracingServiceProxy(Context context) {
        super(context);
        this.mTracingServiceProxy = new ITracingServiceProxy.Stub() { // from class: com.android.server.tracing.TracingServiceProxy.1
            public void notifyTraceSessionEnded(boolean z) {
                TracingServiceProxy.this.notifyTraceur(z);
            }

            public void reportTrace(TraceReportParams traceReportParams) {
                TracingServiceProxy.this.reportTrace(traceReportParams);
            }
        };
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mCachedReporterServices = new LruCache<>(8);
    }

    public void onStart() {
        publishBinderService(TRACING_SERVICE_PROXY_BINDER_NAME, this.mTracingServiceProxy);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyTraceur(boolean z) {
        Intent intent = new Intent();
        try {
            intent.setClassName(this.mPackageManager.getPackageInfo(TRACING_APP_PACKAGE_NAME, 1048576).packageName, TRACING_APP_ACTIVITY);
            if (z) {
                intent.setAction(INTENT_ACTION_NOTIFY_SESSION_STOLEN);
            } else {
                intent.setAction(INTENT_ACTION_NOTIFY_SESSION_STOPPED);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    this.mContext.startForegroundServiceAsUser(intent, UserHandle.SYSTEM);
                } catch (RuntimeException e) {
                    Log.e(TAG, "Failed to notifyTraceSessionEnded", e);
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        } catch (PackageManager.NameNotFoundException e2) {
            Log.e(TAG, "Failed to locate Traceur", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportTrace(TraceReportParams traceReportParams) {
        FrameworkStatsLog.write(424, 1, traceReportParams.uuidLsb, traceReportParams.uuidMsb);
        ComponentName componentName = new ComponentName(traceReportParams.reporterPackageName, traceReportParams.reporterClassName);
        if (!hasBindServicePermission(componentName)) {
            FrameworkStatsLog.write(424, 3, traceReportParams.uuidLsb, traceReportParams.uuidMsb);
            return;
        }
        boolean hasPermission = hasPermission(componentName, "android.permission.DUMP");
        boolean hasPermission2 = hasPermission(componentName, "android.permission.PACKAGE_USAGE_STATS");
        if (!hasPermission || !hasPermission2) {
            FrameworkStatsLog.write(424, 4, traceReportParams.uuidLsb, traceReportParams.uuidMsb);
            return;
        }
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            reportTrace(getOrCreateReporterService(componentName), traceReportParams);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void reportTrace(ServiceConnector<IMessenger> serviceConnector, final TraceReportParams traceReportParams) {
        serviceConnector.post(new ServiceConnector.VoidJob() { // from class: com.android.server.tracing.TracingServiceProxy$$ExternalSyntheticLambda0
            public final void runNoResult(Object obj) {
                TracingServiceProxy.lambda$reportTrace$0(traceReportParams, (IMessenger) obj);
            }
        }).whenComplete(new BiConsumer() { // from class: com.android.server.tracing.TracingServiceProxy$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                TracingServiceProxy.lambda$reportTrace$1(traceReportParams, (Void) obj, (Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$reportTrace$0(TraceReportParams traceReportParams, IMessenger iMessenger) throws Exception {
        if (traceReportParams.usePipeForTesting) {
            ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
            ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(traceReportParams.fd);
            try {
                ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(createPipe[1]);
                try {
                    byte[] readNBytes = autoCloseInputStream.readNBytes(1024);
                    if (readNBytes.length == 1024) {
                        throw new IllegalArgumentException("Trace file too large when |usePipeForTesting| is set.");
                    }
                    autoCloseOutputStream.write(readNBytes);
                    autoCloseOutputStream.close();
                    autoCloseInputStream.close();
                    traceReportParams.fd = createPipe[0];
                } finally {
                }
            } catch (Throwable th) {
                try {
                    autoCloseInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        Message obtain = Message.obtain();
        obtain.what = 1;
        obtain.obj = traceReportParams;
        iMessenger.send(obtain);
        FrameworkStatsLog.write(424, 2, traceReportParams.uuidLsb, traceReportParams.uuidMsb);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$reportTrace$1(TraceReportParams traceReportParams, Void r7, Throwable th) {
        if (th != null) {
            FrameworkStatsLog.write(424, 5, traceReportParams.uuidLsb, traceReportParams.uuidMsb);
            Slog.e(TAG, "Failed to report trace", th);
        }
        try {
            traceReportParams.fd.close();
        } catch (IOException unused) {
        }
    }

    private ServiceConnector<IMessenger> getOrCreateReporterService(ComponentName componentName) {
        ServiceConnector<IMessenger> serviceConnector = this.mCachedReporterServices.get(componentName);
        if (serviceConnector != null) {
            return serviceConnector;
        }
        Intent intent = new Intent();
        intent.setComponent(componentName);
        Context context = this.mContext;
        ServiceConnector<IMessenger> serviceConnector2 = new ServiceConnector.Impl<IMessenger>(context, intent, 33, context.getUser().getIdentifier(), new Function() { // from class: com.android.server.tracing.TracingServiceProxy$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return IMessenger.Stub.asInterface((IBinder) obj);
            }
        }) { // from class: com.android.server.tracing.TracingServiceProxy.2
            private static final long DISCONNECT_TIMEOUT_MS = 15000;
            private static final long REQUEST_TIMEOUT_MS = 10000;

            protected long getAutoDisconnectTimeoutMs() {
                return DISCONNECT_TIMEOUT_MS;
            }

            protected long getRequestTimeoutMs() {
                return 10000L;
            }
        };
        this.mCachedReporterServices.put(intent.getComponent(), serviceConnector2);
        return serviceConnector2;
    }

    private boolean hasPermission(ComponentName componentName, String str) throws SecurityException {
        if (this.mPackageManager.checkPermission(str, componentName.getPackageName()) == 0) {
            return true;
        }
        Slog.e(TAG, "Trace reporting service " + componentName.toShortString() + " does not have " + str + " permission");
        return false;
    }

    private boolean hasBindServicePermission(ComponentName componentName) {
        try {
            ServiceInfo serviceInfo = this.mPackageManager.getServiceInfo(componentName, 0);
            if ("android.permission.BIND_TRACE_REPORT_SERVICE".equals(serviceInfo.permission)) {
                return true;
            }
            Slog.e(TAG, "Trace reporting service " + componentName.toShortString() + " does not request android.permission.BIND_TRACE_REPORT_SERVICE permission; instead requests " + serviceInfo.permission);
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.e(TAG, "Trace reporting service " + componentName.toShortString() + " does not exist");
            return false;
        }
    }
}
