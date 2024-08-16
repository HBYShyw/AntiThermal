package com.android.server.infra;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.server.infra.ServiceNameResolver;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class ServiceNameBaseResolver implements ServiceNameResolver {
    private static final int MSG_RESET_TEMPORARY_SERVICE = 0;
    private static final String TAG = "ServiceNameBaseResolver";
    protected final Context mContext;
    protected final boolean mIsMultiple;
    private ServiceNameResolver.NameResolverListener mOnSetCallback;

    @GuardedBy({"mLock"})
    private Handler mTemporaryHandler;

    @GuardedBy({"mLock"})
    private long mTemporaryServiceExpiration;
    protected final Object mLock = new Object();

    @GuardedBy({"mLock"})
    protected final SparseArray<String[]> mTemporaryServiceNamesList = new SparseArray<>();

    @GuardedBy({"mLock"})
    protected final SparseBooleanArray mDefaultServicesDisabled = new SparseBooleanArray();

    public abstract String readServiceName(int i);

    public abstract String[] readServiceNameList(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public ServiceNameBaseResolver(Context context, boolean z) {
        this.mContext = context;
        this.mIsMultiple = z;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void setOnTemporaryServiceNameChangedCallback(ServiceNameResolver.NameResolverListener nameResolverListener) {
        synchronized (this.mLock) {
            this.mOnSetCallback = nameResolverListener;
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public String getServiceName(int i) {
        String[] serviceNameList = getServiceNameList(i);
        if (serviceNameList == null || serviceNameList.length == 0) {
            return null;
        }
        return serviceNameList[0];
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public String getDefaultServiceName(int i) {
        String[] defaultServiceNameList = getDefaultServiceNameList(i);
        if (defaultServiceNameList == null || defaultServiceNameList.length == 0) {
            return null;
        }
        return defaultServiceNameList[0];
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public String[] getServiceNameList(int i) {
        synchronized (this.mLock) {
            String[] strArr = this.mTemporaryServiceNamesList.get(i);
            if (strArr != null) {
                Slog.w(TAG, "getServiceName(): using temporary name " + Arrays.toString(strArr) + " for user " + i);
                return strArr;
            }
            if (this.mDefaultServicesDisabled.get(i)) {
                Slog.w(TAG, "getServiceName(): temporary name not set and default disabled for user " + i);
                return null;
            }
            return getDefaultServiceNameList(i);
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public String[] getDefaultServiceNameList(int i) {
        synchronized (this.mLock) {
            if (this.mIsMultiple) {
                String[] readServiceNameList = readServiceNameList(i);
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < readServiceNameList.length; i2++) {
                    try {
                        if (!TextUtils.isEmpty(readServiceNameList[i2])) {
                            if (AppGlobals.getPackageManager().getServiceInfo(ComponentName.unflattenFromString(readServiceNameList[i2]), 786432L, i) != null) {
                                arrayList.add(readServiceNameList[i2]);
                            }
                        }
                    } catch (Exception e) {
                        Slog.e(TAG, "Could not validate provided services.", e);
                    }
                }
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
            String readServiceName = readServiceName(i);
            return TextUtils.isEmpty(readServiceName) ? new String[0] : new String[]{readServiceName};
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean isConfiguredInMultipleMode() {
        return this.mIsMultiple;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean isTemporary(int i) {
        boolean z;
        synchronized (this.mLock) {
            z = this.mTemporaryServiceNamesList.get(i) != null;
        }
        return z;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void setTemporaryService(int i, String str, int i2) {
        setTemporaryServices(i, new String[]{str}, i2);
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void setTemporaryServices(final int i, String[] strArr, int i2) {
        synchronized (this.mLock) {
            this.mTemporaryServiceNamesList.put(i, strArr);
            Handler handler = this.mTemporaryHandler;
            if (handler == null) {
                this.mTemporaryHandler = new Handler(Looper.getMainLooper(), null, true) { // from class: com.android.server.infra.ServiceNameBaseResolver.1
                    @Override // android.os.Handler
                    public void handleMessage(Message message) {
                        if (message.what == 0) {
                            synchronized (ServiceNameBaseResolver.this.mLock) {
                                ServiceNameBaseResolver.this.resetTemporaryService(i);
                            }
                        } else {
                            Slog.wtf(ServiceNameBaseResolver.TAG, "invalid handler msg: " + message);
                        }
                    }
                };
            } else {
                handler.removeMessages(0);
            }
            long j = i2;
            this.mTemporaryServiceExpiration = SystemClock.elapsedRealtime() + j;
            this.mTemporaryHandler.sendEmptyMessageDelayed(0, j);
            for (String str : strArr) {
                notifyTemporaryServiceNameChangedLocked(i, str, true);
            }
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void resetTemporaryService(int i) {
        synchronized (this.mLock) {
            Slog.i(TAG, "resetting temporary service for user " + i + " from " + Arrays.toString(this.mTemporaryServiceNamesList.get(i)));
            this.mTemporaryServiceNamesList.remove(i);
            Handler handler = this.mTemporaryHandler;
            if (handler != null) {
                handler.removeMessages(0);
                this.mTemporaryHandler = null;
            }
            notifyTemporaryServiceNameChangedLocked(i, null, false);
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean setDefaultServiceEnabled(int i, boolean z) {
        synchronized (this.mLock) {
            if (isDefaultServiceEnabledLocked(i) == z) {
                Slog.i(TAG, "setDefaultServiceEnabled(" + i + "): already " + z);
                return false;
            }
            if (z) {
                Slog.i(TAG, "disabling default service for user " + i);
                this.mDefaultServicesDisabled.delete(i);
            } else {
                Slog.i(TAG, "enabling default service for user " + i);
                this.mDefaultServicesDisabled.put(i, true);
            }
            return true;
        }
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public boolean isDefaultServiceEnabled(int i) {
        boolean isDefaultServiceEnabledLocked;
        synchronized (this.mLock) {
            isDefaultServiceEnabledLocked = isDefaultServiceEnabledLocked(i);
        }
        return isDefaultServiceEnabledLocked;
    }

    @GuardedBy({"mLock"})
    private boolean isDefaultServiceEnabledLocked(int i) {
        return !this.mDefaultServicesDisabled.get(i);
    }

    public String toString() {
        String str;
        synchronized (this.mLock) {
            str = "FrameworkResourcesServiceNamer[temps=" + this.mTemporaryServiceNamesList + "]";
        }
        return str;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void dumpShort(PrintWriter printWriter, int i) {
        synchronized (this.mLock) {
            String[] strArr = this.mTemporaryServiceNamesList.get(i);
            if (strArr != null) {
                printWriter.print("tmpName=");
                printWriter.print(Arrays.toString(strArr));
                long elapsedRealtime = this.mTemporaryServiceExpiration - SystemClock.elapsedRealtime();
                printWriter.print(" (expires in ");
                TimeUtils.formatDuration(elapsedRealtime, printWriter);
                printWriter.print("), ");
            }
            printWriter.print("defaultName=");
            printWriter.print(getDefaultServiceName(i));
            printWriter.println(this.mDefaultServicesDisabled.get(i) ? " (disabled)" : " (enabled)");
        }
    }

    private void notifyTemporaryServiceNameChangedLocked(int i, String str, boolean z) {
        ServiceNameResolver.NameResolverListener nameResolverListener = this.mOnSetCallback;
        if (nameResolverListener != null) {
            nameResolverListener.onNameResolved(i, str, z);
        }
    }
}
