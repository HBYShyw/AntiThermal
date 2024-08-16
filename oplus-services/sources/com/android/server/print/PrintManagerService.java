package com.android.server.print;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManagerInternal;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.UserHandle;
import android.os.UserManager;
import android.print.IPrintDocumentAdapter;
import android.print.IPrintJobStateChangeListener;
import android.print.IPrintManager;
import android.print.IPrintServicesChangeListener;
import android.print.IPrinterDiscoveryObserver;
import android.print.PrintAttributes;
import android.print.PrintJobId;
import android.print.PrintJobInfo;
import android.print.PrinterId;
import android.printservice.PrintServiceInfo;
import android.printservice.recommendation.IRecommendationsChangeListener;
import android.printservice.recommendation.RecommendationInfo;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.widget.Toast;
import com.android.internal.content.PackageMonitor;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.internal.util.dump.DualDumpOutputStream;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class PrintManagerService extends SystemService {
    private static final String LOG_TAG = "PrintManagerService";
    private final PrintManagerImpl mPrintManagerImpl;

    public PrintManagerService(Context context) {
        super(context);
        this.mPrintManagerImpl = new PrintManagerImpl(context);
    }

    public void onStart() {
        publishBinderService("print", this.mPrintManagerImpl);
    }

    public void onUserUnlocking(SystemService.TargetUser targetUser) {
        this.mPrintManagerImpl.handleUserUnlocked(targetUser.getUserIdentifier());
    }

    public void onUserStopping(SystemService.TargetUser targetUser) {
        this.mPrintManagerImpl.handleUserStopped(targetUser.getUserIdentifier());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class PrintManagerImpl extends IPrintManager.Stub {
        private static final int BACKGROUND_USER_ID = -10;
        private final Context mContext;
        private final UserManager mUserManager;
        private final Object mLock = new Object();
        private final SparseArray<UserState> mUserStates = new SparseArray<>();

        PrintManagerImpl(Context context) {
            this.mContext = context;
            this.mUserManager = (UserManager) context.getSystemService("user");
            registerContentObservers();
            registerBroadcastReceivers();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new PrintShellCommand(this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        public Bundle print(String str, IPrintDocumentAdapter iPrintDocumentAdapter, PrintAttributes printAttributes, String str2, int i, int i2) {
            long clearCallingIdentity;
            Objects.requireNonNull(iPrintDocumentAdapter);
            if (!isPrintingEnabled()) {
                DevicePolicyManagerInternal devicePolicyManagerInternal = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
                int callingUserId = UserHandle.getCallingUserId();
                clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    CharSequence printingDisabledReasonForUser = devicePolicyManagerInternal.getPrintingDisabledReasonForUser(callingUserId);
                    if (printingDisabledReasonForUser != null) {
                        Toast.makeText(this.mContext, Looper.getMainLooper(), printingDisabledReasonForUser, 1).show();
                    }
                    try {
                        iPrintDocumentAdapter.start();
                    } catch (RemoteException unused) {
                        Log.e(PrintManagerService.LOG_TAG, "Error calling IPrintDocumentAdapter.start()");
                    }
                    try {
                        iPrintDocumentAdapter.finish();
                    } catch (RemoteException unused2) {
                        Log.e(PrintManagerService.LOG_TAG, "Error calling IPrintDocumentAdapter.finish()");
                    }
                    return null;
                } finally {
                }
            }
            String str3 = (String) Preconditions.checkStringNotEmpty(str);
            String str4 = (String) Preconditions.checkStringNotEmpty(str2);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i2);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return null;
                }
                int resolveCallingAppEnforcingPermissions = resolveCallingAppEnforcingPermissions(i);
                String resolveCallingPackageNameEnforcingSecurity = resolveCallingPackageNameEnforcingSecurity(str4);
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return orCreateUserStateLocked.print(str3, iPrintDocumentAdapter, printAttributes, resolveCallingPackageNameEnforcingSecurity, resolveCallingAppEnforcingPermissions);
                } finally {
                }
            }
        }

        public List<PrintJobInfo> getPrintJobInfos(int i, int i2) {
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i2);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return null;
                }
                int resolveCallingAppEnforcingPermissions = resolveCallingAppEnforcingPermissions(i);
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return orCreateUserStateLocked.getPrintJobInfos(resolveCallingAppEnforcingPermissions);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public PrintJobInfo getPrintJobInfo(PrintJobId printJobId, int i, int i2) {
            if (printJobId == null) {
                return null;
            }
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i2);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return null;
                }
                int resolveCallingAppEnforcingPermissions = resolveCallingAppEnforcingPermissions(i);
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return orCreateUserStateLocked.getPrintJobInfo(printJobId, resolveCallingAppEnforcingPermissions);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public Icon getCustomPrinterIcon(PrinterId printerId, int i) {
            Objects.requireNonNull(printerId);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return null;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return validateIconUserBoundary(orCreateUserStateLocked.getCustomPrinterIcon(printerId));
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        private Icon validateIconUserBoundary(Icon icon) {
            String encodedUserInfo;
            if (icon != null && ((icon.getType() == 4 || icon.getType() == 6) && (encodedUserInfo = icon.getUri().getEncodedUserInfo()) != null)) {
                int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(Integer.parseInt(encodedUserInfo));
                synchronized (this.mLock) {
                    if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                        return null;
                    }
                }
            }
            return icon;
        }

        public void cancelPrintJob(PrintJobId printJobId, int i, int i2) {
            if (printJobId == null) {
                return;
            }
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i2);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                int resolveCallingAppEnforcingPermissions = resolveCallingAppEnforcingPermissions(i);
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.cancelPrintJob(printJobId, resolveCallingAppEnforcingPermissions);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void restartPrintJob(PrintJobId printJobId, int i, int i2) {
            if (printJobId == null || !isPrintingEnabled()) {
                return;
            }
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i2);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                int resolveCallingAppEnforcingPermissions = resolveCallingAppEnforcingPermissions(i);
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.restartPrintJob(printJobId, resolveCallingAppEnforcingPermissions);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public List<PrintServiceInfo> getPrintServices(int i, int i2) {
            Preconditions.checkFlagsArgument(i, 3);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICES", null);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i2);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return null;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return orCreateUserStateLocked.getPrintServices(i);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void setPrintServiceEnabled(ComponentName componentName, boolean z, int i) {
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            int appId = UserHandle.getAppId(Binder.getCallingUid());
            if (appId != 1000) {
                try {
                    if (appId != UserHandle.getAppId(this.mContext.getPackageManager().getPackageUidAsUser("com.android.printspooler", resolveCallingUserEnforcingPermissions))) {
                        throw new SecurityException("Only system and print spooler can call this");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(PrintManagerService.LOG_TAG, "Could not verify caller", e);
                    return;
                }
            }
            Objects.requireNonNull(componentName);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.setPrintServiceEnabled(componentName, z);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public boolean isPrintServiceEnabled(ComponentName componentName, int i) {
            boolean z;
            String[] packagesForUid = this.mContext.getPackageManager().getPackagesForUid(Binder.getCallingUid());
            int i2 = 0;
            while (true) {
                if (i2 >= packagesForUid.length) {
                    z = false;
                    break;
                }
                if (packagesForUid[i2].equals(componentName.getPackageName())) {
                    z = true;
                    break;
                }
                i2++;
            }
            if (!z) {
                throw new SecurityException("PrintService does not share UID with caller.");
            }
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return false;
                }
                return getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false).isPrintServiceEnabled(componentName);
            }
        }

        public List<RecommendationInfo> getPrintServiceRecommendations(int i) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICE_RECOMMENDATIONS", null);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return null;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    return orCreateUserStateLocked.getPrintServiceRecommendations();
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void createPrinterDiscoverySession(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) {
            Objects.requireNonNull(iPrinterDiscoveryObserver);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.createPrinterDiscoverySession(iPrinterDiscoveryObserver);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void destroyPrinterDiscoverySession(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) {
            Objects.requireNonNull(iPrinterDiscoveryObserver);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.destroyPrinterDiscoverySession(iPrinterDiscoveryObserver);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void startPrinterDiscovery(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, List<PrinterId> list, int i) {
            Objects.requireNonNull(iPrinterDiscoveryObserver);
            if (list != null) {
                list = (List) Preconditions.checkCollectionElementsNotNull(list, "PrinterId");
            }
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.startPrinterDiscovery(iPrinterDiscoveryObserver, list);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void stopPrinterDiscovery(IPrinterDiscoveryObserver iPrinterDiscoveryObserver, int i) {
            Objects.requireNonNull(iPrinterDiscoveryObserver);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.stopPrinterDiscovery(iPrinterDiscoveryObserver);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void validatePrinters(List<PrinterId> list, int i) {
            List<PrinterId> list2 = (List) Preconditions.checkCollectionElementsNotNull(list, "PrinterId");
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.validatePrinters(list2);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void startPrinterStateTracking(PrinterId printerId, int i) {
            Objects.requireNonNull(printerId);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.startPrinterStateTracking(printerId);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void stopPrinterStateTracking(PrinterId printerId, int i) {
            Objects.requireNonNull(printerId);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.stopPrinterStateTracking(printerId);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void addPrintJobStateChangeListener(IPrintJobStateChangeListener iPrintJobStateChangeListener, int i, int i2) throws RemoteException {
            Objects.requireNonNull(iPrintJobStateChangeListener);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i2);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                int resolveCallingAppEnforcingPermissions = resolveCallingAppEnforcingPermissions(i);
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.addPrintJobStateChangeListener(iPrintJobStateChangeListener, resolveCallingAppEnforcingPermissions);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void removePrintJobStateChangeListener(IPrintJobStateChangeListener iPrintJobStateChangeListener, int i) {
            Objects.requireNonNull(iPrintJobStateChangeListener);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.removePrintJobStateChangeListener(iPrintJobStateChangeListener);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void addPrintServicesChangeListener(IPrintServicesChangeListener iPrintServicesChangeListener, int i) throws RemoteException {
            Objects.requireNonNull(iPrintServicesChangeListener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICES", null);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.addPrintServicesChangeListener(iPrintServicesChangeListener);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void removePrintServicesChangeListener(IPrintServicesChangeListener iPrintServicesChangeListener, int i) {
            Objects.requireNonNull(iPrintServicesChangeListener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICES", null);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.removePrintServicesChangeListener(iPrintServicesChangeListener);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void addPrintServiceRecommendationsChangeListener(IRecommendationsChangeListener iRecommendationsChangeListener, int i) throws RemoteException {
            Objects.requireNonNull(iRecommendationsChangeListener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICE_RECOMMENDATIONS", null);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.addPrintServiceRecommendationsChangeListener(iRecommendationsChangeListener);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void removePrintServiceRecommendationsChangeListener(IRecommendationsChangeListener iRecommendationsChangeListener, int i) {
            Objects.requireNonNull(iRecommendationsChangeListener);
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRINT_SERVICE_RECOMMENDATIONS", null);
            int resolveCallingUserEnforcingPermissions = resolveCallingUserEnforcingPermissions(i);
            synchronized (this.mLock) {
                if (resolveCallingProfileParentLocked(resolveCallingUserEnforcingPermissions) != getCurrentUserId()) {
                    return;
                }
                UserState orCreateUserStateLocked = getOrCreateUserStateLocked(resolveCallingUserEnforcingPermissions, false);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    orCreateUserStateLocked.removePrintServiceRecommendationsChangeListener(iRecommendationsChangeListener);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            String str;
            Objects.requireNonNull(fileDescriptor);
            if (DumpUtils.checkDumpPermission(this.mContext, PrintManagerService.LOG_TAG, printWriter)) {
                int i = 0;
                boolean z = false;
                while (i < strArr.length && (str = strArr[i]) != null && str.length() > 0 && str.charAt(0) == '-') {
                    i++;
                    if ("--proto".equals(str)) {
                        z = true;
                    } else {
                        printWriter.println("Unknown argument: " + str + "; use -h for help");
                    }
                }
                ArrayList<UserState> arrayList = new ArrayList<>();
                synchronized (this.mLock) {
                    int size = this.mUserStates.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        arrayList.add(this.mUserStates.valueAt(i2));
                    }
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    if (z) {
                        dump(new DualDumpOutputStream(new ProtoOutputStream(fileDescriptor)), arrayList);
                    } else {
                        printWriter.println("PRINT MANAGER STATE (dumpsys print)");
                        dump(new DualDumpOutputStream(new IndentingPrintWriter(printWriter, "  ")), arrayList);
                    }
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        public boolean getBindInstantServiceAllowed(int i) {
            UserState orCreateUserStateLocked;
            int callingUid = Binder.getCallingUid();
            if (callingUid != 2000 && callingUid != 0) {
                throw new SecurityException("Can only be called by uid 2000 or 0");
            }
            synchronized (this.mLock) {
                orCreateUserStateLocked = getOrCreateUserStateLocked(i, false);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return orCreateUserStateLocked.getBindInstantServiceAllowed();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void setBindInstantServiceAllowed(int i, boolean z) {
            UserState orCreateUserStateLocked;
            int callingUid = Binder.getCallingUid();
            if (callingUid != 2000 && callingUid != 0) {
                throw new SecurityException("Can only be called by uid 2000 or 0");
            }
            synchronized (this.mLock) {
                orCreateUserStateLocked = getOrCreateUserStateLocked(i, false);
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                orCreateUserStateLocked.setBindInstantServiceAllowed(z);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private boolean isPrintingEnabled() {
            return !this.mUserManager.hasUserRestriction("no_printing", Binder.getCallingUserHandle());
        }

        private void dump(DualDumpOutputStream dualDumpOutputStream, ArrayList<UserState> arrayList) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                long start = dualDumpOutputStream.start("user_states", 2246267895809L);
                arrayList.get(i).dump(dualDumpOutputStream);
                dualDumpOutputStream.end(start);
            }
            dualDumpOutputStream.flush();
        }

        private void registerContentObservers() {
            final Uri uriFor = Settings.Secure.getUriFor("disabled_print_services");
            this.mContext.getContentResolver().registerContentObserver(uriFor, false, new ContentObserver(BackgroundThread.getHandler()) { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.1
                @Override // android.database.ContentObserver
                public void onChange(boolean z, Uri uri, int i) {
                    if (uriFor.equals(uri)) {
                        synchronized (PrintManagerImpl.this.mLock) {
                            int size = PrintManagerImpl.this.mUserStates.size();
                            for (int i2 = 0; i2 < size; i2++) {
                                if (i == -1 || i == PrintManagerImpl.this.mUserStates.keyAt(i2)) {
                                    ((UserState) PrintManagerImpl.this.mUserStates.valueAt(i2)).updateIfNeededLocked();
                                }
                            }
                        }
                    }
                }
            }, -1);
        }

        private void registerBroadcastReceivers() {
            new PackageMonitor() { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.2
                private boolean hasPrintService(String str) {
                    Intent intent = new Intent("android.printservice.PrintService");
                    intent.setPackage(str);
                    List queryIntentServicesAsUser = PrintManagerImpl.this.mContext.getPackageManager().queryIntentServicesAsUser(intent, 276824068, getChangingUserId());
                    return (queryIntentServicesAsUser == null || queryIntentServicesAsUser.isEmpty()) ? false : true;
                }

                private boolean hadPrintService(UserState userState, String str) {
                    List<PrintServiceInfo> printServices = userState.getPrintServices(3);
                    if (printServices == null) {
                        return false;
                    }
                    int size = printServices.size();
                    for (int i = 0; i < size; i++) {
                        if (printServices.get(i).getResolveInfo().serviceInfo.packageName.equals(str)) {
                            return true;
                        }
                    }
                    return false;
                }

                public void onPackageModified(String str) {
                    if (PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        boolean z = false;
                        UserState orCreateUserStateLocked = PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false);
                        synchronized (PrintManagerImpl.this.mLock) {
                            if (hadPrintService(orCreateUserStateLocked, str) || hasPrintService(str)) {
                                orCreateUserStateLocked.updateIfNeededLocked();
                                z = true;
                            }
                        }
                        if (z) {
                            orCreateUserStateLocked.prunePrintServices();
                        }
                    }
                }

                public void onPackageRemoved(String str, int i) {
                    if (PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        boolean z = false;
                        UserState orCreateUserStateLocked = PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false);
                        synchronized (PrintManagerImpl.this.mLock) {
                            if (hadPrintService(orCreateUserStateLocked, str)) {
                                orCreateUserStateLocked.updateIfNeededLocked();
                                z = true;
                            }
                        }
                        if (z) {
                            orCreateUserStateLocked.prunePrintServices();
                        }
                    }
                }

                public boolean onHandleForceStop(Intent intent, String[] strArr, int i, boolean z) {
                    if (!PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        return false;
                    }
                    synchronized (PrintManagerImpl.this.mLock) {
                        UserState orCreateUserStateLocked = PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false);
                        List<PrintServiceInfo> printServices = orCreateUserStateLocked.getPrintServices(1);
                        if (printServices == null) {
                            return false;
                        }
                        Iterator<PrintServiceInfo> it = printServices.iterator();
                        boolean z2 = false;
                        while (it.hasNext()) {
                            String packageName = it.next().getComponentName().getPackageName();
                            int length = strArr.length;
                            int i2 = 0;
                            while (true) {
                                if (i2 >= length) {
                                    break;
                                }
                                if (!packageName.equals(strArr[i2])) {
                                    i2++;
                                } else {
                                    if (!z) {
                                        return true;
                                    }
                                    z2 = true;
                                }
                            }
                        }
                        if (z2) {
                            orCreateUserStateLocked.updateIfNeededLocked();
                        }
                        return false;
                    }
                }

                public void onPackageAdded(String str, int i) {
                    if (PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(getChangingUserId())) {
                        synchronized (PrintManagerImpl.this.mLock) {
                            if (hasPrintService(str)) {
                                PrintManagerImpl.this.getOrCreateUserStateLocked(getChangingUserId(), false, false).updateIfNeededLocked();
                            }
                        }
                    }
                }
            }.getWrapper().getExtImpl().register(this.mContext, BackgroundThread.getHandler().getLooper(), UserHandle.ALL, true, new int[]{5, 7, 12, 2});
        }

        private UserState getOrCreateUserStateLocked(int i, boolean z) {
            return getOrCreateUserStateLocked(i, z, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public UserState getOrCreateUserStateLocked(int i, boolean z, boolean z2) {
            return getOrCreateUserStateLocked(i, z, z2, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public UserState getOrCreateUserStateLocked(int i, boolean z, boolean z2, boolean z3) {
            if (z2 && !this.mUserManager.isUserUnlockingOrUnlocked(i)) {
                throw new IllegalStateException("User " + i + " must be unlocked for printing to be available");
            }
            UserState userState = this.mUserStates.get(i);
            if (userState == null) {
                userState = new UserState(this.mContext, i, this.mLock, z);
                this.mUserStates.put(i, userState);
            } else if (z3) {
                userState.updateIfNeededLocked();
            }
            if (!z) {
                userState.increasePriority();
            }
            return userState;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleUserUnlocked(final int i) {
            BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    UserState orCreateUserStateLocked;
                    if (PrintManagerImpl.this.mUserManager.isUserUnlockingOrUnlocked(i)) {
                        synchronized (PrintManagerImpl.this.mLock) {
                            orCreateUserStateLocked = PrintManagerImpl.this.getOrCreateUserStateLocked(i, true, false, true);
                        }
                        orCreateUserStateLocked.removeObsoletePrintJobs();
                    }
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleUserStopped(final int i) {
            BackgroundThread.getHandler().post(new Runnable() { // from class: com.android.server.print.PrintManagerService.PrintManagerImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (PrintManagerImpl.this.mLock) {
                        UserState userState = (UserState) PrintManagerImpl.this.mUserStates.get(i);
                        if (userState != null) {
                            userState.destroyLocked();
                            PrintManagerImpl.this.mUserStates.remove(i);
                        }
                    }
                }
            });
        }

        private int resolveCallingProfileParentLocked(int i) {
            if (i == getCurrentUserId()) {
                return i;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                UserInfo profileParent = this.mUserManager.getProfileParent(i);
                if (profileParent != null) {
                    return profileParent.getUserHandle().getIdentifier();
                }
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return -10;
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private int resolveCallingAppEnforcingPermissions(int i) {
            int appId;
            int callingUid = Binder.getCallingUid();
            if (callingUid == 0 || i == (appId = UserHandle.getAppId(callingUid)) || appId == 2000 || appId == 1000 || this.mContext.checkCallingPermission("com.android.printspooler.permission.ACCESS_ALL_PRINT_JOBS") == 0) {
                return i;
            }
            throw new SecurityException("Call from app " + appId + " as app " + i + " without com.android.printspooler.permission.ACCESS_ALL_PRINT_JOBS");
        }

        private int resolveCallingUserEnforcingPermissions(int i) {
            try {
                return ActivityManager.getService().handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, true, true, "", (String) null);
            } catch (RemoteException unused) {
                return i;
            }
        }

        private String resolveCallingPackageNameEnforcingSecurity(String str) {
            for (String str2 : this.mContext.getPackageManager().getPackagesForUid(Binder.getCallingUid())) {
                if (str.equals(str2)) {
                    return str;
                }
            }
            throw new IllegalArgumentException("packageName has to belong to the caller");
        }

        private int getCurrentUserId() {
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return ActivityManager.getCurrentUser();
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }
}
