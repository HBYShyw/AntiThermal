package android.content.pm;

import android.os.RemoteException;

/* loaded from: classes.dex */
public class PackageInstallerExtImpl implements IPackageInstallerExt {
    private static final String ERROR_MSG_CHILDREN_MODE = "prevent createSession in children mode";

    public PackageInstallerExtImpl(Object base) {
    }

    public String interceptCreateSession(int userId) throws RemoteException {
        if (new OplusPackageManager().prohibitChildInstallation(userId, true)) {
            return ERROR_MSG_CHILDREN_MODE;
        }
        return null;
    }
}
