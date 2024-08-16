package com.android.server.timezonedetector.location;

import android.content.Context;
import android.service.timezone.TimeZoneProviderEvent;
import com.android.internal.annotations.GuardedBy;
import com.android.server.timezonedetector.Dumpable;
import java.util.Objects;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class LocationTimeZoneProviderProxy implements Dumpable {
    protected final Context mContext;

    @GuardedBy({"mSharedLock"})
    protected Listener mListener;
    protected final Object mSharedLock;
    protected final ThreadingDomain mThreadingDomain;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface Listener {
        void onProviderBound();

        void onProviderUnbound();

        void onReportTimeZoneProviderEvent(TimeZoneProviderEvent timeZoneProviderEvent);
    }

    @GuardedBy({"mSharedLock"})
    abstract void onDestroy();

    @GuardedBy({"mSharedLock"})
    abstract void onInitialize();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void setRequest(TimeZoneProviderRequest timeZoneProviderRequest);

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationTimeZoneProviderProxy(Context context, ThreadingDomain threadingDomain) {
        Objects.requireNonNull(context);
        this.mContext = context;
        Objects.requireNonNull(threadingDomain);
        this.mThreadingDomain = threadingDomain;
        this.mSharedLock = threadingDomain.getLockObject();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initialize(Listener listener) {
        Objects.requireNonNull(listener);
        synchronized (this.mSharedLock) {
            if (this.mListener != null) {
                throw new IllegalStateException("listener already set");
            }
            this.mListener = listener;
            onInitialize();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        synchronized (this.mSharedLock) {
            onDestroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleTimeZoneProviderEvent$0(TimeZoneProviderEvent timeZoneProviderEvent) {
        this.mListener.onReportTimeZoneProviderEvent(timeZoneProviderEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void handleTimeZoneProviderEvent(final TimeZoneProviderEvent timeZoneProviderEvent) {
        this.mThreadingDomain.post(new Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                LocationTimeZoneProviderProxy.this.lambda$handleTimeZoneProviderEvent$0(timeZoneProviderEvent);
            }
        });
    }
}
