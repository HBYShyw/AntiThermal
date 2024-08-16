package com.android.server.search;

import android.app.ISearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.content.PackageMonitor;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.server.IoThread;
import com.android.server.LocalServices;
import com.android.server.SystemService;
import com.android.server.search.SearchManagerService;
import com.android.server.statusbar.StatusBarManagerInternal;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import system.ext.loader.core.ExtLoader;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SearchManagerService extends ISearchManager.Stub {
    private static final String TAG = "SearchManagerService";
    private final Context mContext;
    final Handler mHandler;
    private final PackageMonitor mMyPackageMonitor;

    @GuardedBy({"mSearchables"})
    private final SparseArray<Searchables> mSearchables;
    private final ISearchManagerServiceExt mServiceExtImpl;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Lifecycle extends SystemService {
        private SearchManagerService mService;

        public Lifecycle(Context context) {
            super(context);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.search.SearchManagerService, android.os.IBinder] */
        public void onStart() {
            ?? searchManagerService = new SearchManagerService(getContext());
            this.mService = searchManagerService;
            publishBinderService("search", (IBinder) searchManagerService);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserUnlocking$0(SystemService.TargetUser targetUser) {
            this.mService.onUnlockUser(targetUser.getUserIdentifier());
        }

        public void onUserUnlocking(final SystemService.TargetUser targetUser) {
            this.mService.mHandler.post(new Runnable() { // from class: com.android.server.search.SearchManagerService$Lifecycle$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SearchManagerService.Lifecycle.this.lambda$onUserUnlocking$0(targetUser);
                }
            });
        }

        public void onUserStopped(SystemService.TargetUser targetUser) {
            this.mService.onCleanupUser(targetUser.getUserIdentifier());
        }
    }

    public SearchManagerService(Context context) {
        ISearchManagerServiceExt iSearchManagerServiceExt = (ISearchManagerServiceExt) ExtLoader.type(ISearchManagerServiceExt.class).create();
        this.mServiceExtImpl = iSearchManagerServiceExt;
        this.mSearchables = new SparseArray<>();
        this.mContext = context;
        if (iSearchManagerServiceExt.loadingReductionEnable()) {
            MyPackageMonitorExt myPackageMonitorExt = new MyPackageMonitorExt();
            this.mMyPackageMonitor = myPackageMonitorExt;
            myPackageMonitorExt.getWrapper().getExtImpl().register(context, (Looper) null, UserHandle.ALL, true, new int[]{5, 17, 18, 15, 16, 2, 7});
        } else {
            MyPackageMonitor myPackageMonitor = new MyPackageMonitor();
            this.mMyPackageMonitor = myPackageMonitor;
            myPackageMonitor.getWrapper().getExtImpl().register(context, (Looper) null, UserHandle.ALL, true, new int[]{19, 5});
        }
        new GlobalSearchProviderObserver(context.getContentResolver());
        this.mHandler = IoThread.getHandler();
    }

    private Searchables getSearchables(int i) {
        return getSearchables(i, false);
    }

    private Searchables getSearchables(int i, boolean z) {
        Searchables searchables;
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            UserManager userManager = (UserManager) this.mContext.getSystemService(UserManager.class);
            if (userManager.getUserInfo(i) == null) {
                throw new IllegalStateException("User " + i + " doesn't exist");
            }
            if (!userManager.isUserUnlockingOrUnlocked(i)) {
                throw new IllegalStateException("User " + i + " isn't unlocked");
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
            synchronized (this.mSearchables) {
                searchables = this.mSearchables.get(i);
                if (searchables == null) {
                    searchables = new Searchables(this.mContext, i);
                    searchables.updateSearchableList();
                    this.mSearchables.append(i, searchables);
                } else if (z) {
                    searchables.updateSearchableList();
                }
            }
            return searchables;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUnlockUser(int i) {
        try {
            getSearchables(i, true);
        } catch (IllegalStateException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCleanupUser(int i) {
        synchronized (this.mSearchables) {
            this.mSearchables.remove(i);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class MyPackageMonitor extends PackageMonitor {
        MyPackageMonitor() {
        }

        public void onSomePackagesChanged() {
            updateSearchables();
        }

        public void onPackageModified(String str) {
            updateSearchables();
        }

        private void updateSearchables() {
            int changingUserId = getChangingUserId();
            synchronized (SearchManagerService.this.mSearchables) {
                int i = 0;
                while (true) {
                    if (i >= SearchManagerService.this.mSearchables.size()) {
                        break;
                    }
                    if (changingUserId == SearchManagerService.this.mSearchables.keyAt(i)) {
                        ((Searchables) SearchManagerService.this.mSearchables.valueAt(i)).updateSearchableList();
                        break;
                    }
                    i++;
                }
            }
            Intent intent = new Intent("android.search.action.SEARCHABLES_CHANGED");
            intent.addFlags(603979776);
            SearchManagerService.this.mContext.sendBroadcastAsUser(intent, new UserHandle(changingUserId));
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class MyPackageMonitorExt extends PackageMonitor {
        MyPackageMonitorExt() {
        }

        public void onPackageModified(String str) {
            updateSearchables(str);
        }

        public void onPackagesSuspended(String[] strArr) {
            updateSearchables(strArr);
        }

        public void onPackagesUnsuspended(String[] strArr) {
            updateSearchables(strArr);
        }

        public void onPackagesAvailable(String[] strArr) {
            updateSearchables(strArr);
        }

        public void onPackagesUnavailable(String[] strArr) {
            updateSearchables(true, strArr);
        }

        public void onPackageAdded(String str, int i) {
            updateSearchables(str);
        }

        public void onPackageRemoved(String str, int i) {
            updateSearchables(true, str);
        }

        private void updateSearchables(String... strArr) {
            updateSearchables(false, strArr);
        }

        private void updateSearchables(boolean z, String... strArr) {
            int changingUserId = getChangingUserId();
            Trace.traceBegin(524288L, "updateSearchables");
            synchronized (SearchManagerService.this.mSearchables) {
                Searchables searchables = (Searchables) SearchManagerService.this.mSearchables.get(changingUserId);
                if (searchables != null) {
                    ISearchablesExt iSearchablesExt = (ISearchablesExt) ExtLoader.type(ISearchablesExt.class).create();
                    if (z) {
                        iSearchablesExt.removeFromSearchableList(searchables, strArr);
                    } else {
                        iSearchablesExt.updateSearchableList(SearchManagerService.this.mContext, changingUserId, searchables, strArr);
                    }
                }
            }
            Intent intent = new Intent("android.search.action.SEARCHABLES_CHANGED");
            intent.addFlags(603979776);
            SearchManagerService.this.mContext.sendBroadcastAsUser(intent, new UserHandle(changingUserId));
            Trace.traceEnd(524288L);
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    class GlobalSearchProviderObserver extends ContentObserver {
        private final ContentResolver mResolver;

        public GlobalSearchProviderObserver(ContentResolver contentResolver) {
            super(null);
            this.mResolver = contentResolver;
            contentResolver.registerContentObserver(Settings.Secure.getUriFor("search_global_search_activity"), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            synchronized (SearchManagerService.this.mSearchables) {
                for (int i = 0; i < SearchManagerService.this.mSearchables.size(); i++) {
                    ((Searchables) SearchManagerService.this.mSearchables.valueAt(i)).updateSearchableList();
                }
            }
            Intent intent = new Intent("android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED");
            intent.addFlags(536870912);
            SearchManagerService.this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        }
    }

    public SearchableInfo getSearchableInfo(ComponentName componentName) {
        if (componentName == null) {
            Log.e(TAG, "getSearchableInfo(), activity == null");
            return null;
        }
        return getSearchables(UserHandle.getCallingUserId()).getSearchableInfo(componentName);
    }

    public List<SearchableInfo> getSearchablesInGlobalSearch() {
        return getSearchables(UserHandle.getCallingUserId()).getSearchablesInGlobalSearchList();
    }

    public List<ResolveInfo> getGlobalSearchActivities() {
        return getSearchables(UserHandle.getCallingUserId()).getGlobalSearchActivities();
    }

    public ComponentName getGlobalSearchActivity() {
        return getSearchables(UserHandle.getCallingUserId()).getGlobalSearchActivity();
    }

    public ComponentName getWebSearchActivity() {
        return getSearchables(UserHandle.getCallingUserId()).getWebSearchActivity();
    }

    public void launchAssist(int i, Bundle bundle) {
        StatusBarManagerInternal statusBarManagerInternal = (StatusBarManagerInternal) LocalServices.getService(StatusBarManagerInternal.class);
        if (statusBarManagerInternal != null) {
            statusBarManagerInternal.startAssist(bundle);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (DumpUtils.checkDumpPermission(this.mContext, TAG, printWriter)) {
            PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
            synchronized (this.mSearchables) {
                for (int i = 0; i < this.mSearchables.size(); i++) {
                    indentingPrintWriter.print("\nUser: ");
                    indentingPrintWriter.println(this.mSearchables.keyAt(i));
                    indentingPrintWriter.increaseIndent();
                    this.mSearchables.valueAt(i).dump(fileDescriptor, indentingPrintWriter, strArr);
                    indentingPrintWriter.decreaseIndent();
                }
            }
            if (SystemProperties.getBoolean("persist.sys.osense.dump", false)) {
                if (this.mServiceExtImpl.loadingReductionEnable()) {
                    indentingPrintWriter.println("SearchManagerService:load reduction enabled");
                } else {
                    indentingPrintWriter.println("SearchManagerService:load reduction disabled");
                }
            }
        }
    }
}
