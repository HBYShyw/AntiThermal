package android.os;

/* loaded from: classes.dex */
public class BinderProxyExtImpl implements IBinderProxyExt {
    private static final boolean DEBUG_SWITCH = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String TAG = "BinderProxyExtImpl";
    private final BinderProxy mBinderProxy;
    private IBinderTransactProxy mBinderTransactProxy = null;

    public BinderProxyExtImpl(Object base) {
        this.mBinderProxy = (BinderProxy) base;
    }

    public boolean transact(IBinder iBinder, int code, Parcel data, Parcel reply, int flags) {
        JankFactorTracker.getInstance().binderStart();
        IBinderTransactProxy iBinderTransactProxy = this.mBinderTransactProxy;
        if (iBinderTransactProxy == null) {
            return false;
        }
        return iBinderTransactProxy.transact(this.mBinderProxy, code, data, reply, flags);
    }

    public void setBinderTransactProxy(IBinderTransactProxy proxy) {
        this.mBinderTransactProxy = proxy;
    }

    public IBinderTransactProxy getBinderTransactProxy() {
        return this.mBinderTransactProxy;
    }

    public void onBinderCall(int flags, Throwable tr) {
        Binder.getTransactionTracker().addTrace(tr);
        StackTraceElement stackTraceElement = tr.getStackTrace()[1];
        if (stackTraceElement != null && "handleApplicationStrictModeViolation".equals(stackTraceElement.getMethodName())) {
            return;
        }
        StrictMode.mStrictModeExt.onBinderCall();
    }

    public boolean isPerfMonitorEnable() {
        return StrictMode.mStrictModeExt.isPerfMonitorEnable();
    }

    public void transactEnd(int code, int flags) {
        if ((flags & 1) == 1) {
            return;
        }
        JankFactorTracker.getInstance().binderEnd(code);
    }
}
