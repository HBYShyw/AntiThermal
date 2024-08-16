package com.android.server.pm.verify.domain.proxy;

import android.content.ComponentName;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DomainVerificationProxyCombined implements DomainVerificationProxy {
    private final DomainVerificationProxy mProxyV1;
    private final DomainVerificationProxy mProxyV2;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DomainVerificationProxyCombined(DomainVerificationProxy domainVerificationProxy, DomainVerificationProxy domainVerificationProxy2) {
        this.mProxyV1 = domainVerificationProxy;
        this.mProxyV2 = domainVerificationProxy2;
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public void sendBroadcastForPackages(Set<String> set) {
        this.mProxyV2.sendBroadcastForPackages(set);
        this.mProxyV1.sendBroadcastForPackages(set);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean runMessage(int i, Object obj) {
        return this.mProxyV2.runMessage(i, obj) || this.mProxyV1.runMessage(i, obj);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public boolean isCallerVerifier(int i) {
        return this.mProxyV2.isCallerVerifier(i) || this.mProxyV1.isCallerVerifier(i);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy
    public ComponentName getComponentName() {
        return this.mProxyV2.getComponentName();
    }
}
