package com.android.server.ambientcontext;

import android.R;
import android.app.ambientcontext.AmbientContextEventRequest;
import android.app.ambientcontext.IAmbientContextObserver;
import android.content.ComponentName;
import android.os.RemoteCallback;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.ambientcontext.AmbientContextManagerPerUserService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class DefaultAmbientContextManagerPerUserService extends AmbientContextManagerPerUserService {
    private static final String TAG = "DefaultAmbientContextManagerPerUserService";
    private ComponentName mComponentName;

    @VisibleForTesting
    DefaultRemoteAmbientContextDetectionService mRemoteService;
    private final String mServiceName;
    private final AmbientContextManagerPerUserService.ServiceType mServiceType;

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    protected int getAmbientContextEventArrayExtraKeyConfig() {
        return R.string.config_defaultNetworkRecommendationProviderPackage;
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    protected int getAmbientContextPackageNameExtraKeyConfig() {
        return R.string.config_defaultNetworkScorerPackageName;
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    protected int getConsentComponentConfig() {
        return R.string.config_isoImagePath;
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    protected String getProtectedBindPermission() {
        return "android.permission.BIND_AMBIENT_CONTEXT_DETECTION_SERVICE";
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    public /* bridge */ /* synthetic */ void onQueryServiceStatus(int[] iArr, String str, RemoteCallback remoteCallback) {
        super.onQueryServiceStatus(iArr, str, remoteCallback);
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    public /* bridge */ /* synthetic */ void onRegisterObserver(AmbientContextEventRequest ambientContextEventRequest, String str, IAmbientContextObserver iAmbientContextObserver) {
        super.onRegisterObserver(ambientContextEventRequest, str, iAmbientContextObserver);
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    public /* bridge */ /* synthetic */ void onStartConsentActivity(int[] iArr, String str) {
        super.onStartConsentActivity(iArr, str);
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    public /* bridge */ /* synthetic */ void onUnregisterObserver(String str) {
        super.onUnregisterObserver(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultAmbientContextManagerPerUserService(AmbientContextManagerService ambientContextManagerService, Object obj, int i, AmbientContextManagerPerUserService.ServiceType serviceType, String str) {
        super(ambientContextManagerService, obj, i);
        this.mServiceType = serviceType;
        this.mServiceName = str;
        this.mComponentName = ComponentName.unflattenFromString(str);
        Slog.d(TAG, "Created DefaultAmbientContextManagerPerUserServiceand service type: " + serviceType.name() + " and service name: " + str);
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    @GuardedBy({"mLock"})
    protected void ensureRemoteServiceInitiated() {
        if (this.mRemoteService == null) {
            this.mRemoteService = new DefaultRemoteAmbientContextDetectionService(getContext(), this.mComponentName, getUserId());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    @VisibleForTesting
    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    protected void setComponentName(ComponentName componentName) {
        this.mComponentName = componentName;
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    protected RemoteAmbientDetectionService getRemoteService() {
        return this.mRemoteService;
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    public AmbientContextManagerPerUserService.ServiceType getServiceType() {
        return this.mServiceType;
    }

    @Override // com.android.server.ambientcontext.AmbientContextManagerPerUserService
    protected void clearRemoteService() {
        this.mRemoteService = null;
    }
}
