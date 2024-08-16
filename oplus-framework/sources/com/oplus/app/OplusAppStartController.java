package com.oplus.app;

import com.oplus.app.IOplusAppStartController;

/* loaded from: classes.dex */
public class OplusAppStartController extends IOplusAppStartController.Stub {
    @Override // com.oplus.app.IOplusAppStartController
    public void appStartMonitor(String pkgName, String exceptionClass, String exceptionMsg, String exceptionTrace, String monitorType) {
    }

    @Override // com.oplus.app.IOplusAppStartController
    public void preventStartMonitor(String callerPkg, String calledPkg, String startMode, String preventMode, String reason) {
    }

    @Override // com.oplus.app.IOplusAppStartController
    public void notifyPreventIndulge(String pkgName) {
    }
}
