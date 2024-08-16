package com.android.server.pm;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.IPinItemRequest;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.util.Pair;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShortcutRequestPinProcessor {
    private static final boolean DEBUG = false;
    private static final String TAG = "ShortcutService";
    private final Object mLock;
    private final ShortcutService mService;

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static abstract class PinItemRequestInner extends IPinItemRequest.Stub {

        @GuardedBy({"this"})
        private boolean mAccepted;
        private final int mLauncherUid;
        protected final ShortcutRequestPinProcessor mProcessor;
        private final IntentSender mResultIntent;

        public AppWidgetProviderInfo getAppWidgetProviderInfo() {
            return null;
        }

        public Bundle getExtras() {
            return null;
        }

        public ShortcutInfo getShortcutInfo() {
            return null;
        }

        protected boolean tryAccept() {
            return true;
        }

        private PinItemRequestInner(ShortcutRequestPinProcessor shortcutRequestPinProcessor, IntentSender intentSender, int i) {
            this.mProcessor = shortcutRequestPinProcessor;
            this.mResultIntent = intentSender;
            this.mLauncherUid = i;
        }

        private boolean isCallerValid() {
            return this.mProcessor.isCallerUid(this.mLauncherUid);
        }

        public boolean isValid() {
            boolean z;
            if (!isCallerValid()) {
                return false;
            }
            synchronized (this) {
                z = this.mAccepted ? false : true;
            }
            return z;
        }

        public boolean accept(Bundle bundle) {
            Intent putExtras;
            if (!isCallerValid()) {
                throw new SecurityException("Calling uid mismatch");
            }
            if (bundle != null) {
                try {
                    bundle.size();
                    putExtras = new Intent().putExtras(bundle);
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException("options cannot be unparceled", e);
                }
            } else {
                putExtras = null;
            }
            synchronized (this) {
                if (this.mAccepted) {
                    throw new IllegalStateException("accept() called already");
                }
                this.mAccepted = true;
            }
            if (!tryAccept()) {
                return false;
            }
            this.mProcessor.sendResultIntent(this.mResultIntent, putExtras);
            return true;
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class PinAppWidgetRequestInner extends PinItemRequestInner {
        final AppWidgetProviderInfo mAppWidgetProviderInfo;
        final Bundle mExtras;

        private PinAppWidgetRequestInner(ShortcutRequestPinProcessor shortcutRequestPinProcessor, IntentSender intentSender, int i, AppWidgetProviderInfo appWidgetProviderInfo, Bundle bundle) {
            super(intentSender, i);
            this.mAppWidgetProviderInfo = appWidgetProviderInfo;
            this.mExtras = bundle;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        public AppWidgetProviderInfo getAppWidgetProviderInfo() {
            return this.mAppWidgetProviderInfo;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        public Bundle getExtras() {
            return this.mExtras;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class PinShortcutRequestInner extends PinItemRequestInner {
        public final String launcherPackage;
        public final int launcherUserId;
        public final boolean preExisting;
        public final ShortcutInfo shortcutForLauncher;
        public final ShortcutInfo shortcutOriginal;

        private PinShortcutRequestInner(ShortcutRequestPinProcessor shortcutRequestPinProcessor, ShortcutInfo shortcutInfo, ShortcutInfo shortcutInfo2, IntentSender intentSender, String str, int i, int i2, boolean z) {
            super(intentSender, i2);
            this.shortcutOriginal = shortcutInfo;
            this.shortcutForLauncher = shortcutInfo2;
            this.launcherPackage = str;
            this.launcherUserId = i;
            this.preExisting = z;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        public ShortcutInfo getShortcutInfo() {
            return this.shortcutForLauncher;
        }

        @Override // com.android.server.pm.ShortcutRequestPinProcessor.PinItemRequestInner
        protected boolean tryAccept() {
            return this.mProcessor.directPinShortcut(this);
        }
    }

    public ShortcutRequestPinProcessor(ShortcutService shortcutService, Object obj) {
        this.mService = shortcutService;
        this.mLock = obj;
    }

    public boolean isRequestPinItemSupported(int i, int i2) {
        return getRequestPinConfirmationActivity(i, i2) != null;
    }

    public boolean requestPinItemLocked(ShortcutInfo shortcutInfo, AppWidgetProviderInfo appWidgetProviderInfo, Bundle bundle, int i, IntentSender intentSender) {
        int i2;
        int i3;
        LauncherApps.PinItemRequest pinItemRequest;
        if (shortcutInfo != null) {
            i3 = 1;
            i2 = i;
        } else {
            i2 = i;
            i3 = 2;
        }
        Pair<ComponentName, Integer> requestPinConfirmationActivity = getRequestPinConfirmationActivity(i2, i3);
        if (requestPinConfirmationActivity == null) {
            Log.w(TAG, "Launcher doesn't support requestPinnedShortcut(). Shortcut not created.");
            return false;
        }
        int intValue = ((Integer) requestPinConfirmationActivity.second).intValue();
        this.mService.throwIfUserLockedL(intValue);
        if (shortcutInfo != null) {
            pinItemRequest = requestPinShortcutLocked(shortcutInfo, intentSender, ((ComponentName) requestPinConfirmationActivity.first).getPackageName(), ((Integer) requestPinConfirmationActivity.second).intValue());
        } else {
            pinItemRequest = new LauncherApps.PinItemRequest(new PinAppWidgetRequestInner(intentSender, this.mService.injectGetPackageUid(((ComponentName) requestPinConfirmationActivity.first).getPackageName(), intValue), appWidgetProviderInfo, bundle), 2);
        }
        return startRequestConfirmActivity((ComponentName) requestPinConfirmationActivity.first, intValue, pinItemRequest, i3);
    }

    public Intent createShortcutResultIntent(ShortcutInfo shortcutInfo, int i) {
        int parentOrSelfUserId = this.mService.getParentOrSelfUserId(i);
        String defaultLauncher = this.mService.getDefaultLauncher(parentOrSelfUserId);
        if (defaultLauncher == null) {
            Log.e(TAG, "Default launcher not found.");
            return null;
        }
        this.mService.throwIfUserLockedL(parentOrSelfUserId);
        return new Intent().putExtra("android.content.pm.extra.PIN_ITEM_REQUEST", requestPinShortcutLocked(shortcutInfo, null, defaultLauncher, parentOrSelfUserId));
    }

    private LauncherApps.PinItemRequest requestPinShortcutLocked(ShortcutInfo shortcutInfo, IntentSender intentSender, String str, int i) {
        ShortcutInfo clone;
        IntentSender intentSender2;
        ShortcutInfo findShortcutById = this.mService.getPackageShortcutsForPublisherLocked(shortcutInfo.getPackage(), shortcutInfo.getUserId()).findShortcutById(shortcutInfo.getId());
        boolean z = findShortcutById != null;
        if (z) {
            findShortcutById.isVisibleToPublisher();
        }
        if (z) {
            validateExistingShortcut(findShortcutById);
            boolean hasPinned = this.mService.getLauncherShortcutsLocked(str, findShortcutById.getUserId(), i).hasPinned(findShortcutById);
            if (hasPinned) {
                intentSender2 = null;
                sendResultIntent(intentSender, null);
            } else {
                intentSender2 = intentSender;
            }
            ShortcutInfo clone2 = findShortcutById.clone(27);
            if (!hasPinned) {
                clone2.clearFlags(2);
            }
            clone = clone2;
        } else {
            if (shortcutInfo.getActivity() == null) {
                shortcutInfo.setActivity(this.mService.injectGetDefaultMainActivity(shortcutInfo.getPackage(), shortcutInfo.getUserId()));
            }
            this.mService.validateShortcutForPinRequest(shortcutInfo);
            shortcutInfo.resolveResourceStrings(this.mService.injectGetResourcesForApplicationAsUser(shortcutInfo.getPackage(), shortcutInfo.getUserId()));
            clone = shortcutInfo.clone(26);
            intentSender2 = intentSender;
        }
        return new LauncherApps.PinItemRequest(new PinShortcutRequestInner(shortcutInfo, clone, intentSender2, str, i, this.mService.injectGetPackageUid(str, i), z), 1);
    }

    private void validateExistingShortcut(ShortcutInfo shortcutInfo) {
        Preconditions.checkArgument(shortcutInfo.isEnabled(), "Shortcut ID=" + shortcutInfo + " already exists but disabled.");
    }

    private boolean startRequestConfirmActivity(ComponentName componentName, int i, LauncherApps.PinItemRequest pinItemRequest, int i2) {
        Intent intent = new Intent(i2 == 1 ? "android.content.pm.action.CONFIRM_PIN_SHORTCUT" : "android.content.pm.action.CONFIRM_PIN_APPWIDGET");
        intent.setComponent(componentName);
        intent.putExtra("android.content.pm.extra.PIN_ITEM_REQUEST", pinItemRequest);
        intent.addFlags(268468224);
        this.mService.getWrapper().getExtImpl().startRequestConfirmActivity(pinItemRequest, intent);
        long injectClearCallingIdentity = this.mService.injectClearCallingIdentity();
        try {
            try {
                this.mService.mContext.startActivityAsUser(intent, UserHandle.of(i));
                return true;
            } catch (RuntimeException e) {
                Log.e(TAG, "Unable to start activity " + componentName, e);
                this.mService.injectRestoreCallingIdentity(injectClearCallingIdentity);
                return false;
            }
        } finally {
            this.mService.injectRestoreCallingIdentity(injectClearCallingIdentity);
        }
    }

    @VisibleForTesting
    Pair<ComponentName, Integer> getRequestPinConfirmationActivity(int i, int i2) {
        int parentOrSelfUserId = this.mService.getParentOrSelfUserId(i);
        String defaultLauncher = this.mService.getDefaultLauncher(parentOrSelfUserId);
        if (defaultLauncher == null) {
            Log.e(TAG, "Default launcher not found.");
            return null;
        }
        ComponentName injectGetPinConfirmationActivity = this.mService.injectGetPinConfirmationActivity(defaultLauncher, parentOrSelfUserId, i2);
        if (injectGetPinConfirmationActivity == null) {
            return null;
        }
        return Pair.create(injectGetPinConfirmationActivity, Integer.valueOf(parentOrSelfUserId));
    }

    public void sendResultIntent(IntentSender intentSender, Intent intent) {
        this.mService.injectSendIntentSender(intentSender, intent);
    }

    public boolean isCallerUid(int i) {
        return i == this.mService.injectBinderCallingUid();
    }

    public boolean directPinShortcut(PinShortcutRequestInner pinShortcutRequestInner) {
        ShortcutInfo shortcutInfo = pinShortcutRequestInner.shortcutOriginal;
        int userId = shortcutInfo.getUserId();
        String str = shortcutInfo.getPackage();
        int i = pinShortcutRequestInner.launcherUserId;
        String str2 = pinShortcutRequestInner.launcherPackage;
        String id = shortcutInfo.getId();
        synchronized (this.mLock) {
            if (this.mService.isUserUnlockedL(userId) && this.mService.isUserUnlockedL(pinShortcutRequestInner.launcherUserId)) {
                ShortcutLauncher launcherShortcutsLocked = this.mService.getLauncherShortcutsLocked(str2, userId, i);
                launcherShortcutsLocked.attemptToRestoreIfNeededAndSave();
                if (launcherShortcutsLocked.hasPinned(shortcutInfo)) {
                    return true;
                }
                ShortcutPackage packageShortcutsForPublisherLocked = this.mService.getPackageShortcutsForPublisherLocked(str, userId);
                ShortcutInfo findShortcutById = packageShortcutsForPublisherLocked.findShortcutById(id);
                try {
                    if (findShortcutById == null) {
                        this.mService.validateShortcutForPinRequest(shortcutInfo);
                    } else {
                        validateExistingShortcut(findShortcutById);
                    }
                    if (findShortcutById == null) {
                        if (shortcutInfo.getActivity() == null) {
                            shortcutInfo.setActivity(this.mService.getDummyMainActivity(str));
                        }
                        packageShortcutsForPublisherLocked.addOrReplaceDynamicShortcut(shortcutInfo);
                    }
                    launcherShortcutsLocked.addPinnedShortcut(str, userId, id, true);
                    if (findShortcutById == null) {
                        packageShortcutsForPublisherLocked.deleteDynamicWithId(id, false, false);
                    }
                    packageShortcutsForPublisherLocked.adjustRanks();
                    List<ShortcutInfo> singletonList = Collections.singletonList(packageShortcutsForPublisherLocked.findShortcutById(id));
                    this.mService.verifyStates();
                    this.mService.packageShortcutsChanged(packageShortcutsForPublisherLocked, singletonList, null);
                    return true;
                } catch (RuntimeException e) {
                    Log.w(TAG, "Unable to pin shortcut: " + e.getMessage());
                    return false;
                }
            }
            Log.w(TAG, "User is locked now.");
            return false;
        }
    }
}
