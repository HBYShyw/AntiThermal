package com.oplus.app;

import android.content.Intent;
import android.os.Bundle;
import com.oplus.app.IOplusGameSpaceController;

/* loaded from: classes.dex */
public class OplusGameSpaceController extends IOplusGameSpaceController.Stub {
    @Override // com.oplus.app.IOplusGameSpaceController
    public void gameStarting(Intent intent, String pkg, boolean isResume) {
    }

    @Override // com.oplus.app.IOplusGameSpaceController
    public void gameExiting(String pkg) {
    }

    @Override // com.oplus.app.IOplusGameSpaceController
    public void videoStarting(Intent intent, String pkg) {
    }

    @Override // com.oplus.app.IOplusGameSpaceController
    public void dispatchGameDock(Bundle bundle) {
    }

    @Override // com.oplus.app.IOplusGameSpaceController
    public boolean isGameDockAllowed() {
        return true;
    }
}
