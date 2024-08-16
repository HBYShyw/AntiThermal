package com.android.server.incident;

import android.R;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IIncidentAuthListener;
import android.os.IIncidentCompanion;
import android.os.IIncidentManager;
import android.os.IncidentManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.util.DumpUtils;
import com.android.server.SystemService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class IncidentCompanionService extends SystemService {
    static final String TAG = "IncidentCompanionService";
    private PendingReports mPendingReports;
    private static String[] RESTRICTED_IMAGE_DUMP_ARGS = {"--hal", "--restricted_image"};
    private static final String[] DUMP_AND_USAGE_STATS_PERMISSIONS = {"android.permission.DUMP", "android.permission.PACKAGE_USAGE_STATS"};

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class BinderService extends IIncidentCompanion.Stub {
        private BinderService() {
        }

        public void authorizeReport(int i, String str, String str2, String str3, int i2, IIncidentAuthListener iIncidentAuthListener) {
            enforceRequestAuthorizationPermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                IncidentCompanionService.this.mPendingReports.authorizeReport(i, str, str2, str3, i2, iIncidentAuthListener);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void cancelAuthorization(IIncidentAuthListener iIncidentAuthListener) {
            enforceRequestAuthorizationPermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                IncidentCompanionService.this.mPendingReports.cancelAuthorization(iIncidentAuthListener);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void sendReportReadyBroadcast(String str, String str2) {
            enforceRequestAuthorizationPermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                Context context = IncidentCompanionService.this.getContext();
                int currentUserIfAdmin = IncidentCompanionService.getCurrentUserIfAdmin();
                if (currentUserIfAdmin == -10000) {
                    return;
                }
                Intent intent = new Intent("android.intent.action.INCIDENT_REPORT_READY");
                intent.setComponent(new ComponentName(str, str2));
                Log.d(IncidentCompanionService.TAG, "sendReportReadyBroadcast sending currentUser=" + currentUserIfAdmin + " userHandle=" + UserHandle.of(currentUserIfAdmin) + " intent=" + intent);
                context.sendBroadcastAsUserMultiplePermissions(intent, UserHandle.of(currentUserIfAdmin), IncidentCompanionService.DUMP_AND_USAGE_STATS_PERMISSIONS);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public List<String> getPendingReports() {
            enforceAuthorizePermission();
            return IncidentCompanionService.this.mPendingReports.getPendingReports();
        }

        public void approveReport(String str) {
            enforceAuthorizePermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                IncidentCompanionService.this.mPendingReports.approveReport(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void denyReport(String str) {
            enforceAuthorizePermission();
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                IncidentCompanionService.this.mPendingReports.denyReport(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public List<String> getIncidentReportList(String str, String str2) throws RemoteException {
            enforceAccessReportsPermissions(null);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return IncidentCompanionService.this.getIIncidentManager().getIncidentReportList(str, str2);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void deleteIncidentReports(String str, String str2, String str3) throws RemoteException {
            if (str == null || str2 == null || str3 == null || str.length() == 0 || str2.length() == 0 || str3.length() == 0) {
                throw new RuntimeException("Invalid pkg, cls or id");
            }
            enforceAccessReportsPermissions(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                IncidentCompanionService.this.getIIncidentManager().deleteIncidentReports(str, str2, str3);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void deleteAllIncidentReports(String str) throws RemoteException {
            if (str == null || str.length() == 0) {
                throw new RuntimeException("Invalid pkg");
            }
            enforceAccessReportsPermissions(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                IncidentCompanionService.this.getIIncidentManager().deleteAllIncidentReports(str);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public IncidentManager.IncidentReport getIncidentReport(String str, String str2, String str3) throws RemoteException {
            if (str == null || str2 == null || str3 == null || str.length() == 0 || str2.length() == 0 || str3.length() == 0) {
                throw new RuntimeException("Invalid pkg, cls or id");
            }
            enforceAccessReportsPermissions(str);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return IncidentCompanionService.this.getIIncidentManager().getIncidentReport(str, str2, str3);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (DumpUtils.checkDumpPermission(IncidentCompanionService.this.getContext(), IncidentCompanionService.TAG, printWriter)) {
                if (strArr.length == 1 && "--restricted_image".equals(strArr[0])) {
                    dumpRestrictedImages(fileDescriptor);
                } else {
                    IncidentCompanionService.this.mPendingReports.dump(fileDescriptor, printWriter, strArr);
                }
            }
        }

        private void dumpRestrictedImages(FileDescriptor fileDescriptor) {
            if (Build.IS_ENG || Build.IS_USERDEBUG) {
                for (String str : IncidentCompanionService.this.getContext().getResources().getStringArray(R.array.special_locale_codes)) {
                    Log.d(IncidentCompanionService.TAG, "Looking up service " + str);
                    IBinder service = ServiceManager.getService(str);
                    if (service != null) {
                        Log.d(IncidentCompanionService.TAG, "Calling dump on service: " + str);
                        try {
                            service.dump(fileDescriptor, IncidentCompanionService.RESTRICTED_IMAGE_DUMP_ARGS);
                        } catch (RemoteException e) {
                            Log.w(IncidentCompanionService.TAG, "dump --restricted_image of " + str + " threw", e);
                        }
                    }
                }
            }
        }

        private void enforceRequestAuthorizationPermission() {
            IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.REQUEST_INCIDENT_REPORT_APPROVAL", null);
        }

        private void enforceAuthorizePermission() {
            IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.APPROVE_INCIDENT_REPORTS", null);
        }

        private void enforceAccessReportsPermissions(String str) {
            if (IncidentCompanionService.this.getContext().checkCallingPermission("android.permission.APPROVE_INCIDENT_REPORTS") != 0) {
                IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.DUMP", null);
                IncidentCompanionService.this.getContext().enforceCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS", null);
                if (str != null) {
                    enforceCallerIsSameApp(str);
                }
            }
        }

        private void enforceCallerIsSameApp(String str) throws SecurityException {
            try {
                int callingUid = Binder.getCallingUid();
                ApplicationInfo applicationInfoAsUser = IncidentCompanionService.this.getContext().getPackageManager().getApplicationInfoAsUser(str, 0, UserHandle.getCallingUserId());
                if (applicationInfoAsUser == null) {
                    throw new SecurityException("Unknown package " + str);
                }
                if (UserHandle.isSameApp(applicationInfoAsUser.uid, callingUid)) {
                    return;
                }
                throw new SecurityException("Calling uid " + callingUid + " gave package " + str + " which is owned by uid " + applicationInfoAsUser.uid);
            } catch (PackageManager.NameNotFoundException e) {
                throw new SecurityException("Unknown package " + str + "\n" + e);
            }
        }
    }

    public IncidentCompanionService(Context context) {
        super(context);
        this.mPendingReports = new PendingReports(context);
    }

    public void onStart() {
        publishBinderService("incidentcompanion", new BinderService());
    }

    public void onBootPhase(int i) {
        super.onBootPhase(i);
        if (i != 1000) {
            return;
        }
        this.mPendingReports.onBootCompleted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IIncidentManager getIIncidentManager() throws RemoteException {
        return IIncidentManager.Stub.asInterface(ServiceManager.getService("incident"));
    }

    public static int getCurrentUserIfAdmin() {
        try {
            UserInfo currentUser = ActivityManager.getService().getCurrentUser();
            if (currentUser == null) {
                Log.w(TAG, "No current user.  Nobody to approve the report. The report will be denied.");
                return -10000;
            }
            if (!currentUser.isAdmin()) {
                Log.w(TAG, "Only an admin user running in foreground can approve bugreports, but the current foreground user is not an admin user. The report will be denied.");
                return -10000;
            }
            return currentUser.id;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
