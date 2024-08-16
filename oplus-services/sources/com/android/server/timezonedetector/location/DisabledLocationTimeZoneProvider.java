package com.android.server.timezonedetector.location;

import android.service.timezone.TimeZoneProviderEvent;
import android.util.IndentingPrintWriter;
import com.android.server.timezonedetector.location.LocationTimeZoneProvider;
import java.time.Duration;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DisabledLocationTimeZoneProvider extends LocationTimeZoneProvider {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ TimeZoneProviderEvent lambda$new$0(TimeZoneProviderEvent timeZoneProviderEvent) {
        return timeZoneProviderEvent;
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onDestroy() {
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    boolean onInitialize() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisabledLocationTimeZoneProvider(LocationTimeZoneProvider.ProviderMetricsLogger providerMetricsLogger, ThreadingDomain threadingDomain, String str, boolean z) {
        super(providerMetricsLogger, threadingDomain, str, new TimeZoneProviderEventPreProcessor() { // from class: com.android.server.timezonedetector.location.DisabledLocationTimeZoneProvider$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor
            public final TimeZoneProviderEvent preProcess(TimeZoneProviderEvent timeZoneProviderEvent) {
                TimeZoneProviderEvent lambda$new$0;
                lambda$new$0 = DisabledLocationTimeZoneProvider.lambda$new$0(timeZoneProviderEvent);
                return lambda$new$0;
            }
        }, z);
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onStartUpdates(Duration duration, Duration duration2) {
        throw new UnsupportedOperationException("Provider is disabled");
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProvider
    void onStopUpdates() {
        throw new UnsupportedOperationException("Provider is disabled");
    }

    @Override // com.android.server.timezonedetector.Dumpable
    public void dump(IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        synchronized (this.mSharedLock) {
            indentingPrintWriter.println("{DisabledLocationTimeZoneProvider}");
            indentingPrintWriter.println("mProviderName=" + this.mProviderName);
            indentingPrintWriter.println("mCurrentState=" + this.mCurrentState);
        }
    }

    public String toString() {
        String str;
        synchronized (this.mSharedLock) {
            str = "DisabledLocationTimeZoneProvider{mProviderName=" + this.mProviderName + ", mCurrentState=" + this.mCurrentState + '}';
        }
        return str;
    }
}
