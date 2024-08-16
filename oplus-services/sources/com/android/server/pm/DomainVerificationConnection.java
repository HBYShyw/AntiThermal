package com.android.server.pm;

import android.os.Binder;
import android.os.Message;
import android.os.UserHandle;
import com.android.server.DeviceIdleInternal;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.verify.domain.DomainVerificationManagerInternal;
import com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1;
import com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV2;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class DomainVerificationConnection implements DomainVerificationManagerInternal.Connection, DomainVerificationProxyV1.Connection, DomainVerificationProxyV2.Connection {
    final PackageManagerService mPm;
    final UserManagerInternal mUmInternal;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DomainVerificationConnection(PackageManagerService packageManagerService) {
        this.mPm = packageManagerService;
        this.mUmInternal = (UserManagerInternal) packageManagerService.mInjector.getLocalService(UserManagerInternal.class);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public void scheduleWriteSettings() {
        this.mPm.scheduleWriteSettings();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public int getCallingUid() {
        return Binder.getCallingUid();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public int getCallingUserId() {
        return UserHandle.getCallingUserId();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection, com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public void schedule(int i, Object obj) {
        Message obtainMessage = this.mPm.mHandler.obtainMessage(27);
        obtainMessage.arg1 = i;
        obtainMessage.obj = obj;
        this.mPm.mHandler.sendMessage(obtainMessage);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public long getPowerSaveTempWhitelistAppDuration() {
        return VerificationUtils.getDefaultVerificationTimeout(this.mPm.mContext);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public DeviceIdleInternal getDeviceIdleInternal() {
        return (DeviceIdleInternal) this.mPm.mInjector.getLocalService(DeviceIdleInternal.class);
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxy.BaseConnection
    public boolean isCallerPackage(int i, String str) {
        return i == this.mPm.snapshotComputer().getPackageUid(str, 0L, UserHandle.getUserId(i));
    }

    @Override // com.android.server.pm.verify.domain.proxy.DomainVerificationProxyV1.Connection
    public AndroidPackage getPackage(String str) {
        return this.mPm.snapshotComputer().getPackage(str);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationEnforcer.Callback
    public boolean filterAppAccess(String str, int i, int i2) {
        return this.mPm.snapshotComputer().filterAppAccess(str, i, i2, true);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public int[] getAllUserIds() {
        return this.mUmInternal.getUserIds();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationEnforcer.Callback
    public boolean doesUserExist(int i) {
        return this.mUmInternal.exists(i);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection
    public Computer snapshot() {
        return this.mPm.snapshotComputer();
    }
}
