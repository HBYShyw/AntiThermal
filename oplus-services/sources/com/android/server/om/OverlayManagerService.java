package com.android.server.om;

import android.R;
import android.app.ActivityManager;
import android.app.ActivityManagerInternal;
import android.app.AppGlobals;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.IOverlayManager;
import android.content.om.OverlayIdentifier;
import android.content.om.OverlayInfo;
import android.content.om.OverlayManagerTransaction;
import android.content.om.OverlayableInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManagerInternal;
import android.content.pm.UserInfo;
import android.content.pm.UserPackage;
import android.content.pm.overlay.OverlayPaths;
import android.content.res.ApkAssets;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FabricatedOverlayInternal;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ShellCallback;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.EventLog;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.content.om.OverlayConfig;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.FunctionalUtils;
import com.android.server.FgThread;
import com.android.server.LocalServices;
import com.android.server.SystemConfig;
import com.android.server.SystemService;
import com.android.server.om.OverlayManagerService;
import com.android.server.om.OverlayManagerServiceImpl;
import com.android.server.pm.PackageManagerService;
import com.android.server.pm.UserManagerService;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.PackageState;
import com.android.server.pm.pkg.PackageStateInternal;
import com.android.server.pm.pkg.parsing.ParsingPackageUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParserException;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class OverlayManagerService extends SystemService {
    static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final String DEFAULT_OVERLAYS_PROP = "ro.boot.vendor.overlay.theme";
    static final String TAG = "OverlayManager";
    private final OverlayActorEnforcer mActorEnforcer;
    private final OverlayManagerServiceImpl mImpl;
    private final Object mLock;
    private IOverlayManagerServiceExt mOmServiceExt;
    private final PackageManagerHelperImpl mPackageManager;
    private final IBinder mService;
    private final OverlayManagerSettings mSettings;
    private final AtomicFile mSettingsFile;
    private final UserManagerService mUserManager;

    private void onSwitchUser(int i) {
    }

    public void onStart() {
    }

    public OverlayManagerService(Context context) {
        super(context);
        this.mOmServiceExt = (IOverlayManagerServiceExt) ExtLoader.type(IOverlayManagerServiceExt.class).base(this).create();
        this.mLock = new Object();
        IOverlayManager.Stub anonymousClass1 = new AnonymousClass1();
        this.mService = anonymousClass1;
        try {
            Trace.traceBegin(67108864L, "OMS#OverlayManagerService");
            this.mSettingsFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), "overlays.xml"), "overlays");
            PackageManagerHelperImpl packageManagerHelperImpl = new PackageManagerHelperImpl(context);
            this.mPackageManager = packageManagerHelperImpl;
            this.mUserManager = UserManagerService.getInstance();
            IdmapManager idmapManager = new IdmapManager(IdmapDaemon.getInstance(), packageManagerHelperImpl);
            OverlayManagerSettings overlayManagerSettings = new OverlayManagerSettings();
            this.mSettings = overlayManagerSettings;
            this.mImpl = new OverlayManagerServiceImpl(packageManagerHelperImpl, idmapManager, overlayManagerSettings, OverlayConfig.getSystemInstance(), getDefaultOverlayPackages());
            this.mActorEnforcer = new OverlayActorEnforcer(packageManagerHelperImpl);
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
            intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@EXTRA=android.intent.extra.REASON#String#android.intent.action.OVERLAY_CHANGED");
            getContext().registerReceiverAsUser(new PackageReceiver(), UserHandle.ALL, intentFilter, null, handlerThread.getThreadHandler());
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("android.intent.action.USER_ADDED");
            intentFilter2.addAction("android.intent.action.USER_REMOVED");
            getContext().registerReceiverAsUser(new UserReceiver(), UserHandle.ALL, intentFilter2, null, null);
            restoreSettings();
            final String emptyIfNull = TextUtils.emptyIfNull(getContext().getString(R.string.CLIRDefaultOffNextCallOn));
            overlayManagerSettings.removeIf(new Predicate() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$new$0;
                    lambda$new$0 = OverlayManagerService.lambda$new$0(emptyIfNull, (OverlayInfo) obj);
                    return lambda$new$0;
                }
            });
            initIfNeeded();
            onStartUser(0);
            publishBinderService(ParsingPackageUtils.TAG_OVERLAY, anonymousClass1);
            publishLocalService(OverlayManagerService.class, this);
        } finally {
            Trace.traceEnd(67108864L);
        }
    }

    public static /* synthetic */ boolean lambda$new$0(String str, OverlayInfo overlayInfo) {
        return overlayInfo.isFabricated && str.equals(overlayInfo.packageName);
    }

    private void initIfNeeded() {
        List aliveUsers = ((UserManager) getContext().getSystemService(UserManager.class)).getAliveUsers();
        synchronized (this.mLock) {
            int size = aliveUsers.size();
            for (int i = 0; i < size; i++) {
                UserInfo userInfo = (UserInfo) aliveUsers.get(i);
                if (!userInfo.supportsSwitchTo() && userInfo.id != 0) {
                    updatePackageManagerLocked(this.mImpl.updateOverlaysForUser(((UserInfo) aliveUsers.get(i)).id));
                }
            }
        }
    }

    public void onUserSwitching(final SystemService.TargetUser targetUser, final SystemService.TargetUser targetUser2) {
        FgThread.getHandler().post(new Runnable() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                OverlayManagerService.this.lambda$onUserSwitching$1(targetUser, targetUser2);
            }
        });
    }

    public /* synthetic */ void lambda$onUserSwitching$1(SystemService.TargetUser targetUser, SystemService.TargetUser targetUser2) {
        onSwitchUser(targetUser.getUserIdentifier(), targetUser2.getUserIdentifier());
    }

    private void onSwitchUser(int i, int i2) {
        try {
            Trace.traceBegin(67108864L, "OMS#onSwitchUser " + i2);
            synchronized (this.mLock) {
                updateTargetPackagesLocked(this.mImpl.updateOverlaysForUser(i2), true, i);
            }
        } finally {
            Trace.traceEnd(67108864L);
        }
    }

    public void onUserStarting(final SystemService.TargetUser targetUser) {
        FgThread.getHandler().postAtFrontOfQueue(new Runnable() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                OverlayManagerService.this.lambda$onUserStarting$2(targetUser);
            }
        });
    }

    public /* synthetic */ void lambda$onUserStarting$2(SystemService.TargetUser targetUser) {
        onStartUser(targetUser.getUserIdentifier());
    }

    private void onStartUser(int i) {
        try {
            Trace.traceBegin(67108864L, "OMS#onStartUser " + i);
            synchronized (this.mLock) {
                updateTargetPackagesLocked(this.mImpl.updateOverlaysForUser(i), true, -10000);
            }
        } finally {
            Trace.traceEnd(67108864L);
        }
    }

    private static String[] getDefaultOverlayPackages() {
        String str = SystemProperties.get(DEFAULT_OVERLAYS_PROP);
        if (TextUtils.isEmpty(str)) {
            return EmptyArray.STRING;
        }
        ArraySet arraySet = new ArraySet();
        for (String str2 : str.split(";")) {
            if (!TextUtils.isEmpty(str2)) {
                arraySet.add(str2);
            }
        }
        return (String[]) arraySet.toArray(new String[arraySet.size()]);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class PackageReceiver extends BroadcastReceiver {
        /* synthetic */ PackageReceiver(OverlayManagerService overlayManagerService, PackageReceiverIA packageReceiverIA) {
            this();
        }

        private PackageReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x006e, code lost:
        
            if (r8.equals("android.intent.action.PACKAGE_CHANGED") == false) goto L62;
         */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onReceive(Context context, Intent intent) {
            int[] iArr;
            String action = intent.getAction();
            if (action == null) {
                Slog.e(OverlayManagerService.TAG, "Cannot handle package broadcast with null action");
                return;
            }
            Uri data = intent.getData();
            if (data == null) {
                Slog.e(OverlayManagerService.TAG, "Cannot handle package broadcast with null data");
                return;
            }
            String schemeSpecificPart = data.getSchemeSpecificPart();
            char c = 0;
            boolean booleanExtra = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
            boolean booleanExtra2 = intent.getBooleanExtra("android.intent.extra.SYSTEM_UPDATE_UNINSTALL", false);
            int intExtra = intent.getIntExtra("android.intent.extra.UID", -10000);
            if (intExtra == -10000) {
                iArr = OverlayManagerService.this.mUserManager.getUserIds();
            } else {
                iArr = new int[]{UserHandle.getUserId(intExtra)};
            }
            switch (action.hashCode()) {
                case 172491798:
                    break;
                case 525384130:
                    if (action.equals("android.intent.action.PACKAGE_REMOVED")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 1544582882:
                    if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    if ("android.intent.action.OVERLAY_CHANGED".equals(intent.getStringExtra("android.intent.extra.REASON"))) {
                        return;
                    }
                    onPackageChanged(schemeSpecificPart, iArr);
                    return;
                case 1:
                    if (booleanExtra) {
                        onPackageReplacing(schemeSpecificPart, booleanExtra2, iArr);
                        return;
                    } else {
                        onPackageRemoved(schemeSpecificPart, iArr);
                        return;
                    }
                case 2:
                    if (booleanExtra) {
                        onPackageReplaced(schemeSpecificPart, iArr);
                        return;
                    } else {
                        onPackageAdded(schemeSpecificPart, iArr);
                        return;
                    }
                default:
                    return;
            }
        }

        private void onPackageAdded(String str, int[] iArr) {
            try {
                Trace.traceBegin(67108864L, "OMS#onPackageAdded " + str);
                for (int i : iArr) {
                    synchronized (OverlayManagerService.this.mLock) {
                        if (OverlayManagerService.this.mPackageManager.onPackageAdded(str, i) != null && !OverlayManagerService.this.mPackageManager.isInstantApp(str, i)) {
                            try {
                                OverlayManagerService overlayManagerService = OverlayManagerService.this;
                                overlayManagerService.updateTargetPackagesLocked(overlayManagerService.mImpl.onPackageAdded(str, i));
                            } catch (OverlayManagerServiceImpl.OperationFailedException e) {
                                Slog.e(OverlayManagerService.TAG, "onPackageAdded internal error", e);
                            }
                        }
                    }
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        private void onPackageChanged(String str, int[] iArr) {
            try {
                Trace.traceBegin(67108864L, "OMS#onPackageChanged " + str);
                for (int i : iArr) {
                    synchronized (OverlayManagerService.this.mLock) {
                        if (OverlayManagerService.this.mPackageManager.onPackageUpdated(str, i) != null && !OverlayManagerService.this.mPackageManager.isInstantApp(str, i)) {
                            try {
                                OverlayManagerService overlayManagerService = OverlayManagerService.this;
                                overlayManagerService.updateTargetPackagesLocked(overlayManagerService.mImpl.onPackageChanged(str, i));
                            } catch (OverlayManagerServiceImpl.OperationFailedException e) {
                                Slog.e(OverlayManagerService.TAG, "onPackageChanged internal error", e);
                            }
                        }
                    }
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        private void onPackageReplacing(String str, boolean z, int[] iArr) {
            try {
                Trace.traceBegin(67108864L, "OMS#onPackageReplacing " + str);
                for (int i : iArr) {
                    synchronized (OverlayManagerService.this.mLock) {
                        if (OverlayManagerService.this.mPackageManager.onPackageUpdated(str, i) != null && !OverlayManagerService.this.mPackageManager.isInstantApp(str, i)) {
                            try {
                                OverlayManagerService overlayManagerService = OverlayManagerService.this;
                                overlayManagerService.updateTargetPackagesLocked(overlayManagerService.mImpl.onPackageReplacing(str, z, i));
                            } catch (OverlayManagerServiceImpl.OperationFailedException e) {
                                Slog.e(OverlayManagerService.TAG, "onPackageReplacing internal error", e);
                            }
                        }
                    }
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        private void onPackageReplaced(String str, int[] iArr) {
            try {
                Trace.traceBegin(67108864L, "OMS#onPackageReplaced " + str);
                for (int i : iArr) {
                    synchronized (OverlayManagerService.this.mLock) {
                        if (OverlayManagerService.this.mPackageManager.onPackageUpdated(str, i) != null && !OverlayManagerService.this.mPackageManager.isInstantApp(str, i)) {
                            try {
                                OverlayManagerService overlayManagerService = OverlayManagerService.this;
                                overlayManagerService.updateTargetPackagesLocked(overlayManagerService.mImpl.onPackageReplaced(str, i));
                            } catch (OverlayManagerServiceImpl.OperationFailedException e) {
                                Slog.e(OverlayManagerService.TAG, "onPackageReplaced internal error", e);
                            }
                        }
                    }
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        private void onPackageRemoved(String str, int[] iArr) {
            try {
                Trace.traceBegin(67108864L, "OMS#onPackageRemoved " + str);
                for (int i : iArr) {
                    synchronized (OverlayManagerService.this.mLock) {
                        OverlayManagerService.this.mPackageManager.onPackageRemoved(str, i);
                        OverlayManagerService overlayManagerService = OverlayManagerService.this;
                        overlayManagerService.updateTargetPackagesLocked(overlayManagerService.mImpl.onPackageRemoved(str, i));
                    }
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private final class UserReceiver extends BroadcastReceiver {
        /* synthetic */ UserReceiver(OverlayManagerService overlayManagerService, UserReceiverIA userReceiverIA) {
            this();
        }

        private UserReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000);
            String action = intent.getAction();
            action.hashCode();
            if (action.equals("android.intent.action.USER_REMOVED")) {
                if (intExtra != -10000) {
                    try {
                        Trace.traceBegin(67108864L, "OMS ACTION_USER_REMOVED");
                        synchronized (OverlayManagerService.this.mLock) {
                            OverlayManagerService.this.mImpl.onUserRemoved(intExtra);
                            OverlayManagerService.this.mPackageManager.forgetAllPackageInfos(intExtra);
                        }
                        return;
                    } finally {
                    }
                }
                return;
            }
            if (action.equals("android.intent.action.USER_ADDED") && intExtra != -10000) {
                try {
                    Trace.traceBegin(67108864L, "OMS ACTION_USER_ADDED");
                    synchronized (OverlayManagerService.this.mLock) {
                        OverlayManagerService overlayManagerService = OverlayManagerService.this;
                        overlayManagerService.updatePackageManagerLocked(overlayManagerService.mImpl.updateOverlaysForUser(intExtra));
                    }
                } finally {
                }
            }
        }
    }

    /* renamed from: com.android.server.om.OverlayManagerService$1 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class AnonymousClass1 extends IOverlayManager.Stub {
        AnonymousClass1() {
        }

        public Map<String, List<OverlayInfo>> getAllOverlays(int i) {
            Map<String, List<OverlayInfo>> overlaysForUser;
            try {
                Trace.traceBegin(67108864L, "OMS#getAllOverlays " + i);
                int handleIncomingUser = handleIncomingUser(i, "getAllOverlays");
                synchronized (OverlayManagerService.this.mLock) {
                    overlaysForUser = OverlayManagerService.this.mImpl.getOverlaysForUser(handleIncomingUser);
                }
                return overlaysForUser;
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public List<OverlayInfo> getOverlayInfosForTarget(String str, int i) {
            List<OverlayInfo> overlayInfosForTarget;
            if (str == null) {
                return Collections.emptyList();
            }
            try {
                Trace.traceBegin(67108864L, "OMS#getOverlayInfosForTarget " + str);
                int handleIncomingUser = handleIncomingUser(i, "getOverlayInfosForTarget");
                synchronized (OverlayManagerService.this.mLock) {
                    overlayInfosForTarget = OverlayManagerService.this.mImpl.getOverlayInfosForTarget(str, handleIncomingUser);
                }
                return overlayInfosForTarget;
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public OverlayInfo getOverlayInfo(String str, int i) {
            return getOverlayInfoByIdentifier(new OverlayIdentifier(str), i);
        }

        public OverlayInfo getOverlayInfoByIdentifier(OverlayIdentifier overlayIdentifier, int i) {
            OverlayInfo overlayInfo;
            if (overlayIdentifier == null || overlayIdentifier.getPackageName() == null) {
                return null;
            }
            try {
                Trace.traceBegin(67108864L, "OMS#getOverlayInfo " + overlayIdentifier);
                int handleIncomingUser = handleIncomingUser(i, "getOverlayInfo");
                synchronized (OverlayManagerService.this.mLock) {
                    overlayInfo = OverlayManagerService.this.mImpl.getOverlayInfo(overlayIdentifier, handleIncomingUser);
                }
                return overlayInfo;
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public boolean setEnabled(String str, boolean z, int i) {
            if (str == null) {
                return false;
            }
            try {
                Trace.traceBegin(67108864L, "OMS#setEnabled " + str + " " + z);
                OverlayIdentifier overlayIdentifier = new OverlayIdentifier(str);
                int handleIncomingUser = handleIncomingUser(i, "setEnabled");
                enforceActor(overlayIdentifier, "setEnabled", handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (OverlayManagerService.this.mLock) {
                        try {
                            OverlayManagerService overlayManagerService = OverlayManagerService.this;
                            overlayManagerService.updateTargetPackagesLocked(overlayManagerService.mImpl.setEnabled(overlayIdentifier, z, handleIncomingUser));
                        } catch (OverlayManagerServiceImpl.OperationFailedException unused) {
                            return false;
                        }
                    }
                    Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public boolean setEnabledExclusive(String str, boolean z, int i) {
            if (str == null || !z) {
                return false;
            }
            try {
                Trace.traceBegin(67108864L, "OMS#setEnabledExclusive " + str + " " + z);
                OverlayIdentifier overlayIdentifier = new OverlayIdentifier(str);
                int handleIncomingUser = handleIncomingUser(i, "setEnabledExclusive");
                enforceActor(overlayIdentifier, "setEnabledExclusive", handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (OverlayManagerService.this.mLock) {
                        try {
                            OverlayManagerService.this.mImpl.setEnabledExclusive(overlayIdentifier, false, handleIncomingUser).ifPresent(new OverlayManagerService$1$$ExternalSyntheticLambda0(OverlayManagerService.this));
                        } catch (OverlayManagerServiceImpl.OperationFailedException unused) {
                            Trace.traceEnd(67108864L);
                            return false;
                        }
                    }
                    Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } catch (Throwable th) {
                Trace.traceEnd(67108864L);
                throw th;
            }
        }

        public boolean setEnabledExclusiveInCategory(String str, int i) {
            if (str == null) {
                return false;
            }
            try {
                Trace.traceBegin(67108864L, "OMS#setEnabledExclusiveInCategory " + str);
                OverlayIdentifier overlayIdentifier = new OverlayIdentifier(str);
                int handleIncomingUser = handleIncomingUser(i, "setEnabledExclusiveInCategory");
                enforceActor(overlayIdentifier, "setEnabledExclusiveInCategory", handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (OverlayManagerService.this.mLock) {
                        try {
                            OverlayManagerService.this.mImpl.setEnabledExclusive(overlayIdentifier, true, handleIncomingUser).ifPresent(new OverlayManagerService$1$$ExternalSyntheticLambda0(OverlayManagerService.this));
                        } catch (OverlayManagerServiceImpl.OperationFailedException unused) {
                            return false;
                        }
                    }
                    return true;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public boolean setPriority(String str, String str2, int i) {
            if (str == null || str2 == null) {
                return false;
            }
            try {
                Trace.traceBegin(67108864L, "OMS#setPriority " + str + " " + str2);
                OverlayIdentifier overlayIdentifier = new OverlayIdentifier(str);
                OverlayIdentifier overlayIdentifier2 = new OverlayIdentifier(str2);
                int handleIncomingUser = handleIncomingUser(i, "setPriority");
                enforceActor(overlayIdentifier, "setPriority", handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (OverlayManagerService.this.mLock) {
                        try {
                            OverlayManagerService.this.mImpl.setPriority(overlayIdentifier, overlayIdentifier2, handleIncomingUser).ifPresent(new OverlayManagerService$1$$ExternalSyntheticLambda0(OverlayManagerService.this));
                        } catch (OverlayManagerServiceImpl.OperationFailedException unused) {
                            return false;
                        }
                    }
                    Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public boolean setHighestPriority(String str, int i) {
            if (str == null) {
                return false;
            }
            try {
                Trace.traceBegin(67108864L, "OMS#setHighestPriority " + str);
                OverlayIdentifier overlayIdentifier = new OverlayIdentifier(str);
                int handleIncomingUser = handleIncomingUser(i, "setHighestPriority");
                enforceActor(overlayIdentifier, "setHighestPriority", handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (OverlayManagerService.this.mLock) {
                        try {
                            OverlayManagerService overlayManagerService = OverlayManagerService.this;
                            overlayManagerService.updateTargetPackagesLocked(overlayManagerService.mImpl.setHighestPriority(overlayIdentifier, handleIncomingUser));
                        } catch (OverlayManagerServiceImpl.OperationFailedException unused) {
                            return false;
                        }
                    }
                    Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public boolean setLowestPriority(String str, int i) {
            if (str == null) {
                return false;
            }
            try {
                Trace.traceBegin(67108864L, "OMS#setLowestPriority " + str);
                OverlayIdentifier overlayIdentifier = new OverlayIdentifier(str);
                int handleIncomingUser = handleIncomingUser(i, "setLowestPriority");
                enforceActor(overlayIdentifier, "setLowestPriority", handleIncomingUser);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (OverlayManagerService.this.mLock) {
                        try {
                            OverlayManagerService.this.mImpl.setLowestPriority(overlayIdentifier, handleIncomingUser).ifPresent(new OverlayManagerService$1$$ExternalSyntheticLambda0(OverlayManagerService.this));
                        } catch (OverlayManagerServiceImpl.OperationFailedException unused) {
                            return false;
                        }
                    }
                    Trace.traceEnd(67108864L);
                    return true;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public String[] getDefaultOverlayPackages() {
            String[] defaultOverlayPackages;
            try {
                Trace.traceBegin(67108864L, "OMS#getDefaultOverlayPackages");
                OverlayManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.MODIFY_THEME_OVERLAY", null);
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    synchronized (OverlayManagerService.this.mLock) {
                        defaultOverlayPackages = OverlayManagerService.this.mImpl.getDefaultOverlayPackages();
                    }
                    return defaultOverlayPackages;
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        public void invalidateCachesForOverlay(String str, int i) {
            if (str == null) {
                return;
            }
            OverlayIdentifier overlayIdentifier = new OverlayIdentifier(str);
            int handleIncomingUser = handleIncomingUser(i, "invalidateCachesForOverlay");
            enforceActor(overlayIdentifier, "invalidateCachesForOverlay", handleIncomingUser);
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                synchronized (OverlayManagerService.this.mLock) {
                    try {
                        OverlayManagerService.this.mImpl.removeIdmapForOverlay(overlayIdentifier, handleIncomingUser);
                    } catch (OverlayManagerServiceImpl.OperationFailedException e) {
                        Slog.w(OverlayManagerService.TAG, "invalidate caches for overlay '" + overlayIdentifier + "' failed", e);
                    }
                }
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        public void commit(OverlayManagerTransaction overlayManagerTransaction) throws RemoteException {
            String str;
            try {
                Trace.traceBegin(67108864L, "OMS#commit " + overlayManagerTransaction);
                try {
                    executeAllRequests(overlayManagerTransaction);
                } catch (Exception e) {
                    long clearCallingIdentity = Binder.clearCallingIdentity();
                    try {
                        OverlayManagerService.this.restoreSettings();
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        Slog.d(OverlayManagerService.TAG, "commit failed: " + e.getMessage(), e);
                        StringBuilder sb = new StringBuilder();
                        sb.append("commit failed");
                        if (!OverlayManagerService.DEBUG && !Build.IS_DEBUGGABLE) {
                            str = "";
                            sb.append(str);
                            throw new SecurityException(sb.toString());
                        }
                        str = ": " + e.getMessage();
                        sb.append(str);
                        throw new SecurityException(sb.toString());
                    } catch (Throwable th) {
                        Binder.restoreCallingIdentity(clearCallingIdentity);
                        throw th;
                    }
                }
            } finally {
                Trace.traceEnd(67108864L);
            }
        }

        private Set<UserPackage> executeRequest(OverlayManagerTransaction.Request request) throws OverlayManagerServiceImpl.OperationFailedException {
            int i;
            Objects.requireNonNull(request, "Transaction contains a null request");
            Objects.requireNonNull(request.overlay, "Transaction overlay identifier must be non-null");
            int callingUid = Binder.getCallingUid();
            int i2 = request.type;
            if (i2 != 2 && i2 != 3) {
                i = handleIncomingUser(request.userId, request.typeToString());
                enforceActor(request.overlay, request.typeToString(), i);
            } else {
                if (request.userId != -1) {
                    throw new IllegalArgumentException(request.typeToString() + " unsupported for user " + request.userId);
                }
                if (callingUid == 2000) {
                    EventLog.writeEvent(1397638484, "202768292", -1, "");
                    throw new IllegalArgumentException("Non-root shell cannot fabricate overlays");
                }
                String packageName = request.overlay.getPackageName();
                if (callingUid != 0 && !ArrayUtils.contains(OverlayManagerService.this.mPackageManager.getPackagesForUid(callingUid), packageName)) {
                    throw new IllegalArgumentException("UID " + callingUid + " does own packagename " + packageName);
                }
                i = -1;
            }
            long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                int i3 = request.type;
                if (i3 == 0) {
                    return CollectionUtils.emptyIfNull(CollectionUtils.addAll(CollectionUtils.addAll((Set) null, OverlayManagerService.this.mImpl.setEnabled(request.overlay, true, i)), OverlayManagerService.this.mImpl.setHighestPriority(request.overlay, i)));
                }
                if (i3 == 1) {
                    return OverlayManagerService.this.mImpl.setEnabled(request.overlay, false, i);
                }
                if (i3 == 2) {
                    FabricatedOverlayInternal fabricatedOverlayInternal = (FabricatedOverlayInternal) request.extras.getParcelable("fabricated_overlay", FabricatedOverlayInternal.class);
                    Objects.requireNonNull(fabricatedOverlayInternal, "no fabricated overlay attached to request");
                    return OverlayManagerService.this.mImpl.registerFabricatedOverlay(fabricatedOverlayInternal);
                }
                if (i3 == 3) {
                    return OverlayManagerService.this.mImpl.unregisterFabricatedOverlay(request.overlay);
                }
                throw new IllegalArgumentException("unsupported request: " + request);
            } finally {
                Binder.restoreCallingIdentity(clearCallingIdentity);
            }
        }

        private void executeAllRequests(OverlayManagerTransaction overlayManagerTransaction) throws OverlayManagerServiceImpl.OperationFailedException {
            if (OverlayManagerService.DEBUG) {
                Slog.d(OverlayManagerService.TAG, "commit " + overlayManagerTransaction);
            }
            if (overlayManagerTransaction == null) {
                throw new IllegalArgumentException("null transaction");
            }
            synchronized (OverlayManagerService.this.mLock) {
                Iterator requests = overlayManagerTransaction.getRequests();
                Set<UserPackage> set = null;
                while (requests.hasNext()) {
                    set = CollectionUtils.addAll(set, executeRequest((OverlayManagerTransaction.Request) requests.next()));
                }
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    OverlayManagerService.this.updateTargetPackagesLocked(set);
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, ShellCallback shellCallback, ResultReceiver resultReceiver) {
            new OverlayManagerShellCommand(OverlayManagerService.this.getContext(), this).exec(this, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, shellCallback, resultReceiver);
        }

        protected void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            String str;
            DumpState dumpState = new DumpState();
            char c = 65535;
            dumpState.setUserId(-1);
            int i = 0;
            while (i < strArr.length && (str = strArr[i]) != null && str.length() > 0 && str.charAt(0) == '-') {
                i++;
                if (!"-a".equals(str)) {
                    if ("-h".equals(str)) {
                        printWriter.println("dump [-h] [--verbose] [--user USER_ID] [[FIELD] PACKAGE]");
                        printWriter.println("  Print debugging information about the overlay manager.");
                        printWriter.println("  With optional parameter PACKAGE, limit output to the specified");
                        printWriter.println("  package. With optional parameter FIELD, limit output to");
                        printWriter.println("  the value of that SettingsItem field. Field names are");
                        printWriter.println("  case insensitive and out.println the m prefix can be omitted,");
                        printWriter.println("  so the following are equivalent: mState, mstate, State, state.");
                        return;
                    }
                    if ("--user".equals(str)) {
                        if (i >= strArr.length) {
                            printWriter.println("Error: user missing argument");
                            return;
                        }
                        try {
                            dumpState.setUserId(Integer.parseInt(strArr[i]));
                            i++;
                        } catch (NumberFormatException unused) {
                            printWriter.println("Error: user argument is not a number: " + strArr[i]);
                            return;
                        }
                    } else if ("--verbose".equals(str)) {
                        dumpState.setVerbose(true);
                    } else {
                        printWriter.println("Unknown argument: " + str + "; use -h for help");
                    }
                }
            }
            if (i < strArr.length) {
                String str2 = strArr[i];
                i++;
                str2.hashCode();
                switch (str2.hashCode()) {
                    case -1750736508:
                        if (str2.equals("targetoverlayablename")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1248283232:
                        if (str2.equals("targetpackagename")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1165461084:
                        if (str2.equals("priority")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -836029914:
                        if (str2.equals("userid")) {
                            c = 3;
                            break;
                        }
                        break;
                    case -831052100:
                        if (str2.equals("ismutable")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 50511102:
                        if (str2.equals("category")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 109757585:
                        if (str2.equals("state")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 440941271:
                        if (str2.equals("isenabled")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 909712337:
                        if (str2.equals("packagename")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case 1693907299:
                        if (str2.equals("basecodepath")) {
                            c = '\t';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case '\b':
                    case '\t':
                        dumpState.setField(str2);
                        break;
                    default:
                        dumpState.setOverlyIdentifier(str2);
                        break;
                }
            }
            if (dumpState.getPackageName() == null && i < strArr.length) {
                dumpState.setOverlyIdentifier(strArr[i]);
            }
            enforceDumpPermission("dump");
            synchronized (OverlayManagerService.this.mLock) {
                OverlayManagerService.this.mImpl.dump(printWriter, dumpState);
                if (dumpState.getPackageName() == null) {
                    OverlayManagerService.this.mPackageManager.dump(printWriter, dumpState);
                }
            }
        }

        private int handleIncomingUser(int i, String str) {
            return ActivityManager.handleIncomingUser(Binder.getCallingPid(), Binder.getCallingUid(), i, false, true, str, null);
        }

        private void enforceDumpPermission(String str) {
            OverlayManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.DUMP", str);
        }

        private void enforceActor(OverlayIdentifier overlayIdentifier, String str, int i) throws SecurityException {
            OverlayInfo overlayInfo = OverlayManagerService.this.mImpl.getOverlayInfo(overlayIdentifier, i);
            if (overlayInfo == null) {
                throw new IllegalArgumentException("Unable to retrieve overlay information for " + overlayIdentifier);
            }
            OverlayManagerService.this.mActorEnforcer.enforceActor(overlayInfo, str, Binder.getCallingUid(), i);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class PackageManagerHelperImpl implements PackageManagerHelper {
        private static final String TAB1 = "    ";
        private final Context mContext;
        private final ArrayMap<String, PackageStateUsers> mCache = new ArrayMap<>();
        private final Set<Integer> mInitializedUsers = new ArraySet();
        private final IPackageManager mPackageManager = AppGlobals.getPackageManager();
        private final PackageManagerInternal mPackageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);

        /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
        public static class PackageStateUsers {
            private final Set<Integer> mInstalledUsers;
            private PackageState mPackageState;

            /* synthetic */ PackageStateUsers(PackageState packageState, PackageStateUsersIA packageStateUsersIA) {
                this(packageState);
            }

            private PackageStateUsers(PackageState packageState) {
                this.mInstalledUsers = new ArraySet();
                this.mPackageState = packageState;
            }
        }

        PackageManagerHelperImpl(Context context) {
            this.mContext = context;
        }

        @Override // com.android.server.om.PackageManagerHelper
        public ArrayMap<String, PackageState> initializeForUser(final int i) {
            if (!this.mInitializedUsers.contains(Integer.valueOf(i))) {
                this.mInitializedUsers.add(Integer.valueOf(i));
                this.mPackageManagerInternal.forEachPackageState(new Consumer() { // from class: com.android.server.om.OverlayManagerService$PackageManagerHelperImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        OverlayManagerService.PackageManagerHelperImpl.this.lambda$initializeForUser$0(i, (PackageStateInternal) obj);
                    }
                });
            }
            ArrayMap<String, PackageState> arrayMap = new ArrayMap<>();
            int size = this.mCache.size();
            for (int i2 = 0; i2 < size; i2++) {
                PackageStateUsers valueAt = this.mCache.valueAt(i2);
                if (valueAt.mInstalledUsers.contains(Integer.valueOf(i))) {
                    arrayMap.put(this.mCache.keyAt(i2), valueAt.mPackageState);
                }
            }
            return arrayMap;
        }

        public /* synthetic */ void lambda$initializeForUser$0(int i, PackageStateInternal packageStateInternal) {
            if (packageStateInternal.getPkg() == null || !packageStateInternal.getUserStateOrDefault(i).isInstalled()) {
                return;
            }
            addPackageUser(packageStateInternal, i);
        }

        @Override // com.android.server.om.PackageManagerHelper
        public PackageState getPackageStateForUser(String str, int i) {
            PackageStateUsers packageStateUsers = this.mCache.get(str);
            if (packageStateUsers != null && packageStateUsers.mInstalledUsers.contains(Integer.valueOf(i))) {
                return packageStateUsers.mPackageState;
            }
            try {
                if (this.mPackageManager.isPackageAvailable(str, i)) {
                    return addPackageUser(str, i);
                }
                return null;
            } catch (RemoteException e) {
                Slog.w(OverlayManagerService.TAG, "Failed to check availability of package '" + str + "' for user " + i, e);
                return null;
            }
        }

        private PackageState addPackageUser(String str, int i) {
            PackageStateInternal packageStateInternal = this.mPackageManagerInternal.getPackageStateInternal(str);
            if (packageStateInternal == null) {
                Slog.w(OverlayManagerService.TAG, "Android package for '" + str + "' could not be found; continuing as if package was never added", new Throwable());
                return null;
            }
            return addPackageUser(packageStateInternal, i);
        }

        private PackageState addPackageUser(PackageState packageState, int i) {
            PackageStateUsers packageStateUsers = this.mCache.get(packageState.getPackageName());
            if (packageStateUsers == null) {
                packageStateUsers = new PackageStateUsers(packageState);
                this.mCache.put(packageState.getPackageName(), packageStateUsers);
            } else {
                packageStateUsers.mPackageState = packageState;
            }
            packageStateUsers.mInstalledUsers.add(Integer.valueOf(i));
            return packageStateUsers.mPackageState;
        }

        private void removePackageUser(String str, int i) {
            PackageStateUsers packageStateUsers = this.mCache.get(str);
            if (packageStateUsers == null) {
                return;
            }
            removePackageUser(packageStateUsers, i);
        }

        private void removePackageUser(PackageStateUsers packageStateUsers, int i) {
            packageStateUsers.mInstalledUsers.remove(Integer.valueOf(i));
            if (packageStateUsers.mInstalledUsers.isEmpty()) {
                this.mCache.remove(packageStateUsers.mPackageState.getPackageName());
            }
        }

        public PackageState onPackageAdded(String str, int i) {
            return addPackageUser(str, i);
        }

        public PackageState onPackageUpdated(String str, int i) {
            return addPackageUser(str, i);
        }

        public void onPackageRemoved(String str, int i) {
            removePackageUser(str, i);
        }

        @Override // com.android.server.om.PackageManagerHelper
        public boolean isInstantApp(String str, int i) {
            return this.mPackageManagerInternal.isInstantApp(str, i);
        }

        @Override // com.android.server.om.PackageManagerHelper
        public Map<String, Map<String, String>> getNamedActors() {
            return SystemConfig.getInstance().getNamedActors();
        }

        @Override // com.android.server.om.PackageManagerHelper
        public boolean signaturesMatching(String str, String str2, int i) {
            try {
                return this.mPackageManager.checkSignatures(str, str2, i) == 0;
            } catch (RemoteException unused) {
                return false;
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public String getConfigSignaturePackage() {
            String[] knownPackageNames = this.mPackageManagerInternal.getKnownPackageNames(13, 0);
            if (knownPackageNames.length == 0) {
                return null;
            }
            return knownPackageNames[0];
        }

        @Override // com.android.server.om.PackageManagerHelper
        public OverlayableInfo getOverlayableForTarget(String str, String str2, int i) throws IOException {
            PackageState packageStateForUser = getPackageStateForUser(str, i);
            ApkAssets apkAssets = null;
            AndroidPackage androidPackage = packageStateForUser == null ? null : packageStateForUser.getAndroidPackage();
            if (androidPackage == null) {
                throw new IOException("Unable to get target package");
            }
            try {
                apkAssets = ApkAssets.loadFromPath(androidPackage.getSplits().get(0).getPath());
                OverlayableInfo overlayableInfo = apkAssets.getOverlayableInfo(str2);
                try {
                    apkAssets.close();
                } catch (Throwable unused) {
                }
                return overlayableInfo;
            } catch (Throwable th) {
                if (apkAssets != null) {
                    try {
                        apkAssets.close();
                    } catch (Throwable unused2) {
                    }
                }
                throw th;
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public boolean doesTargetDefineOverlayable(String str, int i) throws IOException {
            PackageState packageStateForUser = getPackageStateForUser(str, i);
            ApkAssets apkAssets = null;
            AndroidPackage androidPackage = packageStateForUser == null ? null : packageStateForUser.getAndroidPackage();
            if (androidPackage == null) {
                throw new IOException("Unable to get target package");
            }
            try {
                apkAssets = ApkAssets.loadFromPath(androidPackage.getSplits().get(0).getPath());
                boolean definesOverlayable = apkAssets.definesOverlayable();
                try {
                    apkAssets.close();
                } catch (Throwable unused) {
                }
                return definesOverlayable;
            } catch (Throwable th) {
                if (apkAssets != null) {
                    try {
                        apkAssets.close();
                    } catch (Throwable unused2) {
                    }
                }
                throw th;
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public void enforcePermission(String str, String str2) throws SecurityException {
            this.mContext.enforceCallingOrSelfPermission(str, str2);
        }

        public void forgetAllPackageInfos(int i) {
            for (int size = this.mCache.size() - 1; size >= 0; size--) {
                removePackageUser(this.mCache.valueAt(size), i);
            }
        }

        @Override // com.android.server.om.PackageManagerHelper
        public String[] getPackagesForUid(int i) {
            try {
                return this.mPackageManager.getPackagesForUid(i);
            } catch (RemoteException unused) {
                return null;
            }
        }

        public void dump(PrintWriter printWriter, DumpState dumpState) {
            printWriter.println("AndroidPackage cache");
            if (!dumpState.isVerbose()) {
                printWriter.println(TAB1 + this.mCache.size() + " package(s)");
                return;
            }
            if (this.mCache.size() == 0) {
                printWriter.println("    <empty>");
                return;
            }
            int size = this.mCache.size();
            for (int i = 0; i < size; i++) {
                String keyAt = this.mCache.keyAt(i);
                PackageStateUsers valueAt = this.mCache.valueAt(i);
                printWriter.print(TAB1 + keyAt + ": " + valueAt.mPackageState + " users=");
                printWriter.println(TextUtils.join(", ", valueAt.mInstalledUsers));
            }
        }
    }

    public void updateTargetPackagesLocked(UserPackage userPackage) {
        if (userPackage != null) {
            updateTargetPackagesLocked(Set.of(userPackage));
        }
    }

    public void updateTargetPackagesLocked(Set<UserPackage> set) {
        updateTargetPackagesLocked(set, false, -10000);
    }

    private void updateTargetPackagesLocked(Set<UserPackage> set, boolean z, int i) {
        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        persistSettingsLocked();
        SparseArray<ArraySet<String>> groupTargetsByUserId = groupTargetsByUserId(set);
        int size = groupTargetsByUserId.size();
        for (int i2 = 0; i2 < size; i2++) {
            ArraySet<String> valueAt = groupTargetsByUserId.valueAt(i2);
            int keyAt = groupTargetsByUserId.keyAt(i2);
            List<String> updatePackageManagerLocked = updatePackageManagerLocked(valueAt, keyAt);
            if (!updatePackageManagerLocked.isEmpty()) {
                this.mOmServiceExt.hookAffectedPackages(updatePackageManagerLocked, z, i, keyAt);
                AnonymousClass2 anonymousClass2 = new Runnable() { // from class: com.android.server.om.OverlayManagerService.2
                    final /* synthetic */ List val$affectedPackages;
                    final /* synthetic */ boolean val$isSwitchUser;
                    final /* synthetic */ ArraySet val$targets;
                    final /* synthetic */ int val$userId;

                    AnonymousClass2(List updatePackageManagerLocked2, int keyAt2, boolean z2, ArraySet valueAt2) {
                        r2 = updatePackageManagerLocked2;
                        r3 = keyAt2;
                        r4 = z2;
                        r5 = valueAt2;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        OverlayManagerService.this.updateActivityManager(r2, r3);
                        if (r4) {
                            return;
                        }
                        OverlayManagerService.broadcastActionOverlayChanged(r5, r3);
                    }
                };
                if (!this.mOmServiceExt.postAtFrontOfQueue(FgThread.getHandler(), anonymousClass2, z2, keyAt2)) {
                    FgThread.getHandler().post(anonymousClass2);
                }
            }
        }
    }

    /* renamed from: com.android.server.om.OverlayManagerService$2 */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ List val$affectedPackages;
        final /* synthetic */ boolean val$isSwitchUser;
        final /* synthetic */ ArraySet val$targets;
        final /* synthetic */ int val$userId;

        AnonymousClass2(List updatePackageManagerLocked2, int keyAt2, boolean z2, ArraySet valueAt2) {
            r2 = updatePackageManagerLocked2;
            r3 = keyAt2;
            r4 = z2;
            r5 = valueAt2;
        }

        @Override // java.lang.Runnable
        public void run() {
            OverlayManagerService.this.updateActivityManager(r2, r3);
            if (r4) {
                return;
            }
            OverlayManagerService.broadcastActionOverlayChanged(r5, r3);
        }
    }

    private static SparseArray<ArraySet<String>> groupTargetsByUserId(Set<UserPackage> set) {
        final SparseArray<ArraySet<String>> sparseArray = new SparseArray<>();
        CollectionUtils.forEach(set, new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda3
            public final void acceptOrThrow(Object obj) {
                OverlayManagerService.lambda$groupTargetsByUserId$3(sparseArray, (UserPackage) obj);
            }
        });
        return sparseArray;
    }

    public static /* synthetic */ void lambda$groupTargetsByUserId$3(SparseArray sparseArray, UserPackage userPackage) throws Exception {
        ArraySet arraySet = (ArraySet) sparseArray.get(userPackage.userId);
        if (arraySet == null) {
            arraySet = new ArraySet();
            sparseArray.put(userPackage.userId, arraySet);
        }
        arraySet.add(userPackage.packageName);
    }

    public static void broadcastActionOverlayChanged(Set<String> set, final int i) {
        final ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
        CollectionUtils.forEach(set, new FunctionalUtils.ThrowingConsumer() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda2
            public final void acceptOrThrow(Object obj) {
                OverlayManagerService.lambda$broadcastActionOverlayChanged$4(i, activityManagerInternal, (String) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$broadcastActionOverlayChanged$4(int i, ActivityManagerInternal activityManagerInternal, String str) throws Exception {
        Intent intent = new Intent("android.intent.action.OVERLAY_CHANGED", Uri.fromParts("package", str, null));
        intent.setFlags(67108864);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", str);
        intent.putExtra("android.intent.extra.USER_ID", i);
        activityManagerInternal.broadcastIntent(intent, (IIntentReceiver) null, (String[]) null, false, i, (int[]) null, new BiFunction() { // from class: com.android.server.om.OverlayManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Bundle filterReceiverAccess;
                filterReceiverAccess = OverlayManagerService.filterReceiverAccess(((Integer) obj).intValue(), (Bundle) obj2);
                return filterReceiverAccess;
            }
        }, (Bundle) null);
    }

    public static Bundle filterReceiverAccess(int i, Bundle bundle) {
        if (((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).filterAppAccess(bundle.getString("android.intent.extra.PACKAGE_NAME"), i, bundle.getInt("android.intent.extra.USER_ID"), false)) {
            return null;
        }
        return bundle;
    }

    public void updateActivityManager(List<String> list, int i) {
        try {
            ActivityManager.getService().scheduleApplicationInfoChanged(list, i);
        } catch (RemoteException e) {
            Slog.e(TAG, "updateActivityManager remote exception", e);
        }
    }

    public SparseArray<List<String>> updatePackageManagerLocked(Set<UserPackage> set) {
        if (CollectionUtils.isEmpty(set)) {
            return new SparseArray<>();
        }
        SparseArray<List<String>> sparseArray = new SparseArray<>();
        SparseArray<ArraySet<String>> groupTargetsByUserId = groupTargetsByUserId(set);
        int size = groupTargetsByUserId.size();
        for (int i = 0; i < size; i++) {
            int keyAt = groupTargetsByUserId.keyAt(i);
            sparseArray.put(keyAt, updatePackageManagerLocked(groupTargetsByUserId.valueAt(i), keyAt));
        }
        return sparseArray;
    }

    private List<String> updatePackageManagerLocked(Collection<String> collection, int i) {
        try {
            Trace.traceBegin(67108864L, "OMS#updatePackageManagerLocked " + collection);
            if (DEBUG) {
                Slog.d(TAG, "Update package manager about changed overlays");
            }
            PackageManagerInternal packageManagerInternal = (PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class);
            if (collection.contains(PackageManagerService.PLATFORM_PACKAGE_NAME)) {
                collection = packageManagerInternal.getTargetPackageNames(i);
            }
            ArrayMap arrayMap = new ArrayMap(collection.size());
            synchronized (this.mLock) {
                OverlayPaths enabledOverlayPaths = this.mImpl.getEnabledOverlayPaths(PackageManagerService.PLATFORM_PACKAGE_NAME, i, false);
                for (String str : collection) {
                    OverlayPaths.Builder builder = new OverlayPaths.Builder();
                    builder.addAll(enabledOverlayPaths);
                    if (!PackageManagerService.PLATFORM_PACKAGE_NAME.equals(str)) {
                        builder.addAll(this.mImpl.getEnabledOverlayPaths(str, i, true));
                    }
                    arrayMap.put(str, builder.build());
                }
            }
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet();
            packageManagerInternal.setEnabledOverlayPackages(i, arrayMap, hashSet, hashSet2);
            for (String str2 : collection) {
                if (DEBUG) {
                    Slog.d(TAG, "-> Updating overlay: target=" + str2 + " overlays=[" + arrayMap.get(str2) + "] userId=" + i);
                }
                if (hashSet2.contains(str2)) {
                    Slog.e(TAG, TextUtils.formatSimple("Failed to change enabled overlays for %s user %d", new Object[]{str2, Integer.valueOf(i)}));
                }
            }
            return new ArrayList(hashSet);
        } finally {
            Trace.traceEnd(67108864L);
        }
    }

    private void persistSettingsLocked() {
        if (DEBUG) {
            Slog.d(TAG, "Writing overlay settings");
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = this.mSettingsFile.startWrite();
            this.mSettings.persist(fileOutputStream);
            this.mSettingsFile.finishWrite(fileOutputStream);
        } catch (IOException | XmlPullParserException e) {
            this.mSettingsFile.failWrite(fileOutputStream);
            Slog.e(TAG, "failed to persist overlay state", e);
        }
    }

    public void restoreSettings() {
        FileInputStream openRead;
        try {
            Trace.traceBegin(67108864L, "OMS#restoreSettings");
            synchronized (this.mLock) {
                if (this.mSettingsFile.getBaseFile().exists()) {
                    try {
                        openRead = this.mSettingsFile.openRead();
                    } catch (IOException | XmlPullParserException e) {
                        Slog.e(TAG, "failed to restore overlay state", e);
                    }
                    try {
                        this.mSettings.restore(openRead);
                        List<UserInfo> users = this.mUserManager.getUsers(true);
                        int[] iArr = new int[users.size()];
                        for (int i = 0; i < users.size(); i++) {
                            iArr[i] = users.get(i).getUserHandle().getIdentifier();
                        }
                        Arrays.sort(iArr);
                        for (int i2 : this.mSettings.getUsers()) {
                            if (Arrays.binarySearch(iArr, i2) < 0) {
                                this.mSettings.removeUser(i2);
                            }
                        }
                        if (openRead != null) {
                            openRead.close();
                        }
                    } catch (Throwable th) {
                        if (openRead != null) {
                            try {
                                openRead.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                }
            }
        } finally {
            Trace.traceEnd(67108864L);
        }
    }
}
