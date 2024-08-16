package com.android.server.bluetooth;

import android.content.Context;
import android.os.UserManager;
import com.android.server.SystemService;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BluetoothService extends SystemService {
    private BluetoothManagerService mBluetoothManagerService;
    private boolean mInitialized;

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    public BluetoothService(Context context) {
        super(context);
        this.mInitialized = false;
        this.mBluetoothManagerService = new BluetoothManagerService(context);
    }

    private void initialize() {
        if (this.mInitialized) {
            return;
        }
        this.mBluetoothManagerService.handleOnBootPhase();
        this.mInitialized = true;
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int i) {
        if (i == 500) {
            publishBinderService("bluetooth_manager", this.mBluetoothManagerService);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(SystemService.TargetUser targetUser) {
        if (UserManager.isHeadlessSystemUserMode()) {
            return;
        }
        initialize();
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        if (!this.mInitialized) {
            initialize();
        } else {
            this.mBluetoothManagerService.onSwitchUser(targetUser2.getUserHandle());
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(SystemService.TargetUser targetUser) {
        this.mBluetoothManagerService.handleOnUnlockUser(targetUser.getUserHandle());
    }
}
