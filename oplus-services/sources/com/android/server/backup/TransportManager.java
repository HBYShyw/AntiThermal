package com.android.server.backup;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.server.backup.transport.BackupTransportClient;
import com.android.server.backup.transport.OnTransportRegisteredListener;
import com.android.server.backup.transport.TransportConnection;
import com.android.server.backup.transport.TransportConnectionManager;
import com.android.server.backup.transport.TransportNotAvailableException;
import com.android.server.backup.transport.TransportNotRegisteredException;
import com.android.server.backup.transport.TransportStats;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class TransportManager {
    private static final boolean MORE_DEBUG = false;

    @VisibleForTesting
    public static final String SERVICE_ACTION_TRANSPORT_HOST = "android.backup.TRANSPORT_HOST";
    private static final String TAG = "BackupTransportManager";

    @GuardedBy({"mTransportLock"})
    private volatile String mCurrentTransportName;
    private OnTransportRegisteredListener mOnTransportRegisteredListener;
    private final PackageManager mPackageManager;

    @GuardedBy({"mTransportLock"})
    private final Map<ComponentName, TransportDescription> mRegisteredTransportsDescriptionMap;
    private final TransportConnectionManager mTransportConnectionManager;
    private final Object mTransportLock;
    private final Intent mTransportServiceIntent;
    private final TransportStats mTransportStats;
    private final Set<ComponentName> mTransportWhitelist;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$new$0(String str, String str2) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onPackageAdded$1(ComponentName componentName) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$registerTransports$2(ComponentName componentName) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransportManager(int i, Context context, Set<ComponentName> set, String str) {
        this.mTransportServiceIntent = new Intent(SERVICE_ACTION_TRANSPORT_HOST);
        this.mOnTransportRegisteredListener = new OnTransportRegisteredListener() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda1
            @Override // com.android.server.backup.transport.OnTransportRegisteredListener
            public final void onTransportRegistered(String str2, String str3) {
                TransportManager.lambda$new$0(str2, str3);
            }
        };
        this.mTransportLock = new Object();
        this.mRegisteredTransportsDescriptionMap = new ArrayMap();
        this.mUserId = i;
        this.mPackageManager = context.getPackageManager();
        this.mTransportWhitelist = (Set) Preconditions.checkNotNull(set);
        this.mCurrentTransportName = str;
        TransportStats transportStats = new TransportStats();
        this.mTransportStats = transportStats;
        this.mTransportConnectionManager = new TransportConnectionManager(i, context, transportStats);
    }

    @VisibleForTesting
    TransportManager(int i, Context context, Set<ComponentName> set, String str, TransportConnectionManager transportConnectionManager) {
        this.mTransportServiceIntent = new Intent(SERVICE_ACTION_TRANSPORT_HOST);
        this.mOnTransportRegisteredListener = new OnTransportRegisteredListener() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda1
            @Override // com.android.server.backup.transport.OnTransportRegisteredListener
            public final void onTransportRegistered(String str2, String str3) {
                TransportManager.lambda$new$0(str2, str3);
            }
        };
        this.mTransportLock = new Object();
        this.mRegisteredTransportsDescriptionMap = new ArrayMap();
        this.mUserId = i;
        this.mPackageManager = context.getPackageManager();
        this.mTransportWhitelist = (Set) Preconditions.checkNotNull(set);
        this.mCurrentTransportName = str;
        this.mTransportStats = new TransportStats();
        this.mTransportConnectionManager = transportConnectionManager;
    }

    public void setOnTransportRegisteredListener(OnTransportRegisteredListener onTransportRegisteredListener) {
        this.mOnTransportRegisteredListener = onTransportRegisteredListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageAdded(String str) {
        registerTransportsFromPackage(str, new Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$onPackageAdded$1;
                lambda$onPackageAdded$1 = TransportManager.lambda$onPackageAdded$1((ComponentName) obj);
                return lambda$onPackageAdded$1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageRemoved(String str) {
        synchronized (this.mTransportLock) {
            this.mRegisteredTransportsDescriptionMap.keySet().removeIf(fromPackageFilter(str));
        }
    }

    void onPackageEnabled(String str) {
        onPackageAdded(str);
    }

    void onPackageDisabled(String str) {
        onPackageRemoved(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPackageChanged(String str, String... strArr) {
        if (strArr.length == 1 && strArr[0].equals(str)) {
            try {
                int applicationEnabledSetting = this.mPackageManager.getApplicationEnabledSetting(str);
                if (applicationEnabledSetting == 0) {
                    onPackageEnabled(str);
                    return;
                }
                if (applicationEnabledSetting == 1) {
                    onPackageEnabled(str);
                    return;
                }
                if (applicationEnabledSetting == 2) {
                    onPackageDisabled(str);
                    return;
                }
                if (applicationEnabledSetting == 3) {
                    onPackageDisabled(str);
                    return;
                }
                Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Package " + str + " enabled setting: " + applicationEnabledSetting));
                return;
            } catch (IllegalArgumentException unused) {
                return;
            }
        }
        final ArraySet arraySet = new ArraySet(strArr.length);
        for (String str2 : strArr) {
            arraySet.add(new ComponentName(str, str2));
        }
        if (arraySet.isEmpty()) {
            return;
        }
        synchronized (this.mTransportLock) {
            this.mRegisteredTransportsDescriptionMap.keySet().removeIf(new Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return arraySet.contains((ComponentName) obj);
                }
            });
        }
        registerTransportsFromPackage(str, new Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return arraySet.contains((ComponentName) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ComponentName[] getRegisteredTransportComponents() {
        ComponentName[] componentNameArr;
        synchronized (this.mTransportLock) {
            componentNameArr = (ComponentName[]) this.mRegisteredTransportsDescriptionMap.keySet().toArray(new ComponentName[this.mRegisteredTransportsDescriptionMap.size()]);
        }
        return componentNameArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String[] getRegisteredTransportNames() {
        String[] strArr;
        synchronized (this.mTransportLock) {
            strArr = new String[this.mRegisteredTransportsDescriptionMap.size()];
            Iterator<TransportDescription> it = this.mRegisteredTransportsDescriptionMap.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                strArr[i] = it.next().name;
                i++;
            }
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<ComponentName> getTransportWhitelist() {
        return this.mTransportWhitelist;
    }

    public String getCurrentTransportName() {
        return this.mCurrentTransportName;
    }

    public ComponentName getCurrentTransportComponent() throws TransportNotRegisteredException {
        synchronized (this.mTransportLock) {
            if (this.mCurrentTransportName == null) {
                return null;
            }
            return getRegisteredTransportComponentOrThrowLocked(this.mCurrentTransportName);
        }
    }

    public String getTransportName(ComponentName componentName) throws TransportNotRegisteredException {
        String str;
        synchronized (this.mTransportLock) {
            str = getRegisteredTransportDescriptionOrThrowLocked(componentName).name;
        }
        return str;
    }

    public String getTransportDirName(ComponentName componentName) throws TransportNotRegisteredException {
        String str;
        synchronized (this.mTransportLock) {
            str = getRegisteredTransportDescriptionOrThrowLocked(componentName).transportDirName;
        }
        return str;
    }

    public String getTransportDirName(String str) throws TransportNotRegisteredException {
        String str2;
        synchronized (this.mTransportLock) {
            str2 = getRegisteredTransportDescriptionOrThrowLocked(str).transportDirName;
        }
        return str2;
    }

    public Intent getTransportConfigurationIntent(String str) throws TransportNotRegisteredException {
        Intent intent;
        synchronized (this.mTransportLock) {
            intent = getRegisteredTransportDescriptionOrThrowLocked(str).configurationIntent;
        }
        return intent;
    }

    public String getTransportCurrentDestinationString(String str) throws TransportNotRegisteredException {
        String str2;
        synchronized (this.mTransportLock) {
            str2 = getRegisteredTransportDescriptionOrThrowLocked(str).currentDestinationString;
        }
        return str2;
    }

    public Intent getTransportDataManagementIntent(String str) throws TransportNotRegisteredException {
        Intent intent;
        synchronized (this.mTransportLock) {
            intent = getRegisteredTransportDescriptionOrThrowLocked(str).dataManagementIntent;
        }
        return intent;
    }

    public CharSequence getTransportDataManagementLabel(String str) throws TransportNotRegisteredException {
        CharSequence charSequence;
        synchronized (this.mTransportLock) {
            charSequence = getRegisteredTransportDescriptionOrThrowLocked(str).dataManagementLabel;
        }
        return charSequence;
    }

    public boolean isTransportRegistered(String str) {
        boolean z;
        synchronized (this.mTransportLock) {
            z = getRegisteredTransportEntryLocked(str) != null;
        }
        return z;
    }

    public void forEachRegisteredTransport(Consumer<String> consumer) {
        synchronized (this.mTransportLock) {
            Iterator<TransportDescription> it = this.mRegisteredTransportsDescriptionMap.values().iterator();
            while (it.hasNext()) {
                consumer.accept(it.next().name);
            }
        }
    }

    public void updateTransportAttributes(ComponentName componentName, String str, Intent intent, String str2, Intent intent2, CharSequence charSequence) {
        synchronized (this.mTransportLock) {
            TransportDescription transportDescription = this.mRegisteredTransportsDescriptionMap.get(componentName);
            if (transportDescription == null) {
                Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + str + " not registered tried to change description"));
                return;
            }
            transportDescription.name = str;
            transportDescription.configurationIntent = intent;
            transportDescription.currentDestinationString = str2;
            transportDescription.dataManagementIntent = intent2;
            transportDescription.dataManagementLabel = charSequence;
            Slog.d(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + str + " updated its attributes"));
        }
    }

    @GuardedBy({"mTransportLock"})
    private ComponentName getRegisteredTransportComponentOrThrowLocked(String str) throws TransportNotRegisteredException {
        ComponentName registeredTransportComponentLocked = getRegisteredTransportComponentLocked(str);
        if (registeredTransportComponentLocked != null) {
            return registeredTransportComponentLocked;
        }
        throw new TransportNotRegisteredException(str);
    }

    @GuardedBy({"mTransportLock"})
    private TransportDescription getRegisteredTransportDescriptionOrThrowLocked(ComponentName componentName) throws TransportNotRegisteredException {
        TransportDescription transportDescription = this.mRegisteredTransportsDescriptionMap.get(componentName);
        if (transportDescription != null) {
            return transportDescription;
        }
        throw new TransportNotRegisteredException(componentName);
    }

    @GuardedBy({"mTransportLock"})
    private TransportDescription getRegisteredTransportDescriptionOrThrowLocked(String str) throws TransportNotRegisteredException {
        TransportDescription registeredTransportDescriptionLocked = getRegisteredTransportDescriptionLocked(str);
        if (registeredTransportDescriptionLocked != null) {
            return registeredTransportDescriptionLocked;
        }
        throw new TransportNotRegisteredException(str);
    }

    @GuardedBy({"mTransportLock"})
    private ComponentName getRegisteredTransportComponentLocked(String str) {
        Map.Entry<ComponentName, TransportDescription> registeredTransportEntryLocked = getRegisteredTransportEntryLocked(str);
        if (registeredTransportEntryLocked == null) {
            return null;
        }
        return registeredTransportEntryLocked.getKey();
    }

    @GuardedBy({"mTransportLock"})
    private TransportDescription getRegisteredTransportDescriptionLocked(String str) {
        Map.Entry<ComponentName, TransportDescription> registeredTransportEntryLocked = getRegisteredTransportEntryLocked(str);
        if (registeredTransportEntryLocked == null) {
            return null;
        }
        return registeredTransportEntryLocked.getValue();
    }

    @GuardedBy({"mTransportLock"})
    private Map.Entry<ComponentName, TransportDescription> getRegisteredTransportEntryLocked(String str) {
        for (Map.Entry<ComponentName, TransportDescription> entry : this.mRegisteredTransportsDescriptionMap.entrySet()) {
            if (str.equals(entry.getValue().name)) {
                return entry;
            }
        }
        return null;
    }

    public TransportConnection getTransportClient(String str, String str2) {
        try {
            return getTransportClientOrThrow(str, str2);
        } catch (TransportNotRegisteredException unused) {
            Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + str + " not registered"));
            return null;
        }
    }

    public TransportConnection getTransportClientOrThrow(String str, String str2) throws TransportNotRegisteredException {
        TransportConnection transportClient;
        synchronized (this.mTransportLock) {
            ComponentName registeredTransportComponentLocked = getRegisteredTransportComponentLocked(str);
            if (registeredTransportComponentLocked == null) {
                throw new TransportNotRegisteredException(str);
            }
            transportClient = this.mTransportConnectionManager.getTransportClient(registeredTransportComponentLocked, str2);
        }
        return transportClient;
    }

    public TransportConnection getCurrentTransportClient(String str) {
        TransportConnection transportClient;
        if (this.mCurrentTransportName == null) {
            throw new IllegalStateException("No transport selected");
        }
        synchronized (this.mTransportLock) {
            transportClient = getTransportClient(this.mCurrentTransportName, str);
        }
        return transportClient;
    }

    public TransportConnection getCurrentTransportClientOrThrow(String str) throws TransportNotRegisteredException {
        TransportConnection transportClientOrThrow;
        if (this.mCurrentTransportName == null) {
            throw new IllegalStateException("No transport selected");
        }
        synchronized (this.mTransportLock) {
            transportClientOrThrow = getTransportClientOrThrow(this.mCurrentTransportName, str);
        }
        return transportClientOrThrow;
    }

    public void disposeOfTransportClient(TransportConnection transportConnection, String str) {
        this.mTransportConnectionManager.disposeOfTransportClient(transportConnection, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public String selectTransport(String str) {
        String str2;
        synchronized (this.mTransportLock) {
            str2 = this.mCurrentTransportName;
            this.mCurrentTransportName = str;
        }
        return str2;
    }

    public int registerAndSelectTransport(ComponentName componentName) {
        synchronized (this.mTransportLock) {
            try {
                try {
                    selectTransport(getTransportName(componentName));
                } catch (Throwable th) {
                    throw th;
                }
            } catch (TransportNotRegisteredException unused) {
                int registerTransport = registerTransport(componentName);
                if (registerTransport != 0) {
                    return registerTransport;
                }
                synchronized (this.mTransportLock) {
                    try {
                        try {
                            selectTransport(getTransportName(componentName));
                            return 0;
                        } catch (TransportNotRegisteredException unused2) {
                            Slog.wtf(TAG, addUserIdToLogMessage(this.mUserId, "Transport got unregistered"));
                            return -1;
                        }
                    } finally {
                    }
                }
            }
        }
        return 0;
    }

    public void registerTransports() {
        registerTransportsForIntent(this.mTransportServiceIntent, new Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$registerTransports$2;
                lambda$registerTransports$2 = TransportManager.lambda$registerTransports$2((ComponentName) obj);
                return lambda$registerTransports$2;
            }
        });
    }

    private void registerTransportsFromPackage(String str, Predicate<ComponentName> predicate) {
        try {
            this.mPackageManager.getPackageInfoAsUser(str, 0, this.mUserId);
            registerTransportsForIntent(new Intent(this.mTransportServiceIntent).setPackage(str), predicate.and(fromPackageFilter(str)));
        } catch (PackageManager.NameNotFoundException unused) {
            Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Trying to register transports from package not found " + str));
        }
    }

    private void registerTransportsForIntent(Intent intent, Predicate<ComponentName> predicate) {
        List queryIntentServicesAsUser = this.mPackageManager.queryIntentServicesAsUser(intent, 0, this.mUserId);
        if (queryIntentServicesAsUser == null) {
            return;
        }
        Iterator it = queryIntentServicesAsUser.iterator();
        while (it.hasNext()) {
            ComponentName componentName = ((ResolveInfo) it.next()).serviceInfo.getComponentName();
            if (predicate.test(componentName) && isTransportTrusted(componentName)) {
                registerTransport(componentName);
            }
        }
    }

    private boolean isTransportTrusted(ComponentName componentName) {
        if (!this.mTransportWhitelist.contains(componentName)) {
            Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "BackupTransport " + componentName.flattenToShortString() + " not whitelisted."));
            return false;
        }
        try {
            if ((this.mPackageManager.getPackageInfoAsUser(componentName.getPackageName(), 0, this.mUserId).applicationInfo.privateFlags & 8) != 0) {
                return true;
            }
            Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Transport package " + componentName.getPackageName() + " not privileged"));
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Package not found."), e);
            return false;
        }
    }

    private int registerTransport(ComponentName componentName) {
        String name;
        String transportDirName;
        checkCanUseTransport();
        if (!isTransportTrusted(componentName)) {
            return -2;
        }
        String flattenToShortString = componentName.flattenToShortString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("android.app.backup.extra.TRANSPORT_REGISTRATION", true);
        TransportConnection transportClient = this.mTransportConnectionManager.getTransportClient(componentName, bundle, "TransportManager.registerTransport()");
        int i = -1;
        try {
            BackupTransportClient connectOrThrow = transportClient.connectOrThrow("TransportManager.registerTransport()");
            try {
                name = connectOrThrow.name();
                transportDirName = connectOrThrow.transportDirName();
                Slog.d(TAG, addUserIdToLogMessage(this.mUserId, "transportName " + name + " transportDirName " + transportDirName));
            } catch (RemoteException unused) {
                Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + flattenToShortString + " died while registering"));
            }
            if (name != null && transportDirName != null) {
                registerTransport(componentName, connectOrThrow);
                Slog.d(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + flattenToShortString + " registered"));
                this.mOnTransportRegisteredListener.onTransportRegistered(name, transportDirName);
                i = 0;
                this.mTransportConnectionManager.disposeOfTransportClient(transportClient, "TransportManager.registerTransport()");
                return i;
            }
            return -2;
        } catch (TransportNotAvailableException unused2) {
            Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Couldn't connect to transport " + flattenToShortString + " for registration"));
            this.mTransportConnectionManager.disposeOfTransportClient(transportClient, "TransportManager.registerTransport()");
            return -1;
        }
    }

    private void registerTransport(ComponentName componentName, BackupTransportClient backupTransportClient) throws RemoteException {
        checkCanUseTransport();
        TransportDescription transportDescription = new TransportDescription(backupTransportClient.name(), backupTransportClient.transportDirName(), backupTransportClient.configurationIntent(), backupTransportClient.currentDestinationString(), backupTransportClient.dataManagementIntent(), backupTransportClient.dataManagementIntentLabel());
        synchronized (this.mTransportLock) {
            this.mRegisteredTransportsDescriptionMap.put(componentName, transportDescription);
        }
    }

    private void checkCanUseTransport() {
        Preconditions.checkState(!Thread.holdsLock(this.mTransportLock), "Can't call transport with transport lock held");
    }

    public void dumpTransportClients(PrintWriter printWriter) {
        this.mTransportConnectionManager.dump(printWriter);
    }

    public void dumpTransportStats(PrintWriter printWriter) {
        this.mTransportStats.dump(printWriter);
    }

    private static Predicate<ComponentName> fromPackageFilter(final String str) {
        return new Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$fromPackageFilter$3;
                lambda$fromPackageFilter$3 = TransportManager.lambda$fromPackageFilter$3(str, (ComponentName) obj);
                return lambda$fromPackageFilter$3;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$fromPackageFilter$3(String str, ComponentName componentName) {
        return str.equals(componentName.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class TransportDescription {
        private Intent configurationIntent;
        private String currentDestinationString;
        private Intent dataManagementIntent;
        private CharSequence dataManagementLabel;
        private String name;
        private final String transportDirName;

        private TransportDescription(String str, String str2, Intent intent, String str3, Intent intent2, CharSequence charSequence) {
            this.name = str;
            this.transportDirName = str2;
            this.configurationIntent = intent;
            this.currentDestinationString = str3;
            this.dataManagementIntent = intent2;
            this.dataManagementLabel = charSequence;
        }
    }

    private static String addUserIdToLogMessage(int i, String str) {
        return "[UserID:" + i + "] " + str;
    }
}
