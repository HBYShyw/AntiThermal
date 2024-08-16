package com.android.server.accounts;

import android.accounts.Account;
import android.accounts.AccountManagerInternal;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.PackageUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.content.PackageMonitor;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.accounts.AccountManagerService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class AccountManagerBackupHelper {
    private static final String ATTR_ACCOUNT_SHA_256 = "account-sha-256";
    private static final String ATTR_DIGEST = "digest";
    private static final String ATTR_PACKAGE = "package";
    private static final long PENDING_RESTORE_TIMEOUT_MILLIS = 3600000;
    private static final String TAG = "AccountManagerBackupHelper";
    private static final String TAG_PERMISSION = "permission";
    private static final String TAG_PERMISSIONS = "permissions";
    private final AccountManagerInternal mAccountManagerInternal;
    private final AccountManagerService mAccountManagerService;
    private final Object mLock = new Object();

    @GuardedBy({"mLock"})
    private Runnable mRestoreCancelCommand;

    @GuardedBy({"mLock"})
    private RestorePackageMonitor mRestorePackageMonitor;

    @GuardedBy({"mLock"})
    private List<PendingAppPermission> mRestorePendingAppPermissions;

    public AccountManagerBackupHelper(AccountManagerService accountManagerService, AccountManagerInternal accountManagerInternal) {
        this.mAccountManagerService = accountManagerService;
        this.mAccountManagerInternal = accountManagerInternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class PendingAppPermission {
        private final String accountDigest;
        private final String certDigest;
        private final String packageName;
        private final int userId;

        public PendingAppPermission(String str, String str2, String str3, int i) {
            this.accountDigest = str;
            this.packageName = str2;
            this.certDigest = str3;
            this.userId = i;
        }

        public boolean apply(PackageManager packageManager) {
            Account account;
            AccountManagerService.UserAccounts userAccounts = AccountManagerBackupHelper.this.mAccountManagerService.getUserAccounts(this.userId);
            synchronized (userAccounts.dbLock) {
                synchronized (userAccounts.cacheLock) {
                    account = null;
                    for (Account[] accountArr : userAccounts.accountCache.values()) {
                        int length = accountArr.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            }
                            Account account2 = accountArr[i];
                            if (this.accountDigest.equals(PackageUtils.computeSha256Digest(account2.name.getBytes()))) {
                                account = account2;
                                break;
                            }
                            i++;
                        }
                        if (account != null) {
                            break;
                        }
                    }
                }
            }
            if (account == null) {
                return false;
            }
            try {
                PackageInfo packageInfoAsUser = packageManager.getPackageInfoAsUser(this.packageName, 64, this.userId);
                String[] computeSignaturesSha256Digests = PackageUtils.computeSignaturesSha256Digests(packageInfoAsUser.signatures);
                if (!this.certDigest.equals(PackageUtils.computeSignaturesSha256Digest(computeSignaturesSha256Digests)) && (packageInfoAsUser.signatures.length <= 1 || !this.certDigest.equals(computeSignaturesSha256Digests[0]))) {
                    return false;
                }
                int i2 = packageInfoAsUser.applicationInfo.uid;
                if (!AccountManagerBackupHelper.this.mAccountManagerInternal.hasAccountAccess(account, i2)) {
                    AccountManagerBackupHelper.this.mAccountManagerService.grantAppPermission(account, "com.android.AccountManager.ACCOUNT_ACCESS_TOKEN_TYPE", i2);
                }
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }
    }

    public byte[] backupAccountAccessPermissions(int i) {
        AccountManagerService.UserAccounts userAccounts = this.mAccountManagerService.getUserAccounts(i);
        synchronized (userAccounts.dbLock) {
            synchronized (userAccounts.cacheLock) {
                List<Pair<String, Integer>> findAllAccountGrants = userAccounts.accountsDb.findAllAccountGrants();
                if (findAllAccountGrants.isEmpty()) {
                    return null;
                }
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    TypedXmlSerializer newFastSerializer = Xml.newFastSerializer();
                    newFastSerializer.setOutput(byteArrayOutputStream, StandardCharsets.UTF_8.name());
                    newFastSerializer.startDocument((String) null, Boolean.TRUE);
                    newFastSerializer.startTag((String) null, TAG_PERMISSIONS);
                    PackageManager packageManager = this.mAccountManagerService.mContext.getPackageManager();
                    for (Pair<String, Integer> pair : findAllAccountGrants) {
                        String str = (String) pair.first;
                        String[] packagesForUid = packageManager.getPackagesForUid(((Integer) pair.second).intValue());
                        if (packagesForUid != null) {
                            for (String str2 : packagesForUid) {
                                try {
                                    String computeSignaturesSha256Digest = PackageUtils.computeSignaturesSha256Digest(packageManager.getPackageInfoAsUser(str2, 64, i).signatures);
                                    if (computeSignaturesSha256Digest != null) {
                                        newFastSerializer.startTag((String) null, TAG_PERMISSION);
                                        newFastSerializer.attribute((String) null, ATTR_ACCOUNT_SHA_256, PackageUtils.computeSha256Digest(str.getBytes()));
                                        newFastSerializer.attribute((String) null, ATTR_PACKAGE, str2);
                                        newFastSerializer.attribute((String) null, ATTR_DIGEST, computeSignaturesSha256Digest);
                                        newFastSerializer.endTag((String) null, TAG_PERMISSION);
                                    }
                                } catch (PackageManager.NameNotFoundException unused) {
                                    Slog.i(TAG, "Skipping backup of account access grant for non-existing package: " + str2);
                                }
                            }
                        }
                    }
                    newFastSerializer.endTag((String) null, TAG_PERMISSIONS);
                    newFastSerializer.endDocument();
                    newFastSerializer.flush();
                    return byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    Log.e(TAG, "Error backing up account access grants", e);
                    return null;
                }
            }
        }
    }

    public void restoreAccountAccessPermissions(byte[] bArr, int i) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            TypedXmlPullParser newFastPullParser = Xml.newFastPullParser();
            newFastPullParser.setInput(byteArrayInputStream, StandardCharsets.UTF_8.name());
            PackageManager packageManager = this.mAccountManagerService.mContext.getPackageManager();
            int depth = newFastPullParser.getDepth();
            while (true) {
                byte b = 0;
                if (XmlUtils.nextElementWithin(newFastPullParser, depth)) {
                    if (TAG_PERMISSIONS.equals(newFastPullParser.getName())) {
                        int depth2 = newFastPullParser.getDepth();
                        while (XmlUtils.nextElementWithin(newFastPullParser, depth2)) {
                            if (TAG_PERMISSION.equals(newFastPullParser.getName())) {
                                String attributeValue = newFastPullParser.getAttributeValue((String) null, ATTR_ACCOUNT_SHA_256);
                                if (TextUtils.isEmpty(attributeValue)) {
                                    XmlUtils.skipCurrentTag(newFastPullParser);
                                }
                                String attributeValue2 = newFastPullParser.getAttributeValue((String) null, ATTR_PACKAGE);
                                if (TextUtils.isEmpty(attributeValue2)) {
                                    XmlUtils.skipCurrentTag(newFastPullParser);
                                }
                                String attributeValue3 = newFastPullParser.getAttributeValue((String) null, ATTR_DIGEST);
                                if (TextUtils.isEmpty(attributeValue3)) {
                                    XmlUtils.skipCurrentTag(newFastPullParser);
                                }
                                PendingAppPermission pendingAppPermission = new PendingAppPermission(attributeValue, attributeValue2, attributeValue3, i);
                                if (pendingAppPermission.apply(packageManager)) {
                                    continue;
                                } else {
                                    synchronized (this.mLock) {
                                        if (this.mRestorePackageMonitor == null) {
                                            RestorePackageMonitor restorePackageMonitor = new RestorePackageMonitor();
                                            this.mRestorePackageMonitor = restorePackageMonitor;
                                            AccountManagerService accountManagerService = this.mAccountManagerService;
                                            restorePackageMonitor.register(accountManagerService.mContext, accountManagerService.mHandler.getLooper(), true);
                                        }
                                        if (this.mRestorePendingAppPermissions == null) {
                                            this.mRestorePendingAppPermissions = new ArrayList();
                                        }
                                        this.mRestorePendingAppPermissions.add(pendingAppPermission);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    CancelRestoreCommand cancelRestoreCommand = new CancelRestoreCommand();
                    this.mRestoreCancelCommand = cancelRestoreCommand;
                    this.mAccountManagerService.mHandler.postDelayed(cancelRestoreCommand, 3600000L);
                    return;
                }
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e(TAG, "Error restoring app permissions", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class RestorePackageMonitor extends PackageMonitor {
        private RestorePackageMonitor() {
        }

        public void onPackageAdded(String str, int i) {
            synchronized (AccountManagerBackupHelper.this.mLock) {
                if (AccountManagerBackupHelper.this.mRestorePendingAppPermissions == null) {
                    return;
                }
                if (UserHandle.getUserId(i) != 0) {
                    return;
                }
                for (int size = AccountManagerBackupHelper.this.mRestorePendingAppPermissions.size() - 1; size >= 0; size--) {
                    PendingAppPermission pendingAppPermission = (PendingAppPermission) AccountManagerBackupHelper.this.mRestorePendingAppPermissions.get(size);
                    if (pendingAppPermission.packageName.equals(str) && pendingAppPermission.apply(AccountManagerBackupHelper.this.mAccountManagerService.mContext.getPackageManager())) {
                        AccountManagerBackupHelper.this.mRestorePendingAppPermissions.remove(size);
                    }
                }
                if (AccountManagerBackupHelper.this.mRestorePendingAppPermissions.isEmpty() && AccountManagerBackupHelper.this.mRestoreCancelCommand != null) {
                    AccountManagerBackupHelper.this.mAccountManagerService.mHandler.removeCallbacks(AccountManagerBackupHelper.this.mRestoreCancelCommand);
                    AccountManagerBackupHelper.this.mRestoreCancelCommand.run();
                    AccountManagerBackupHelper.this.mRestoreCancelCommand = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public final class CancelRestoreCommand implements Runnable {
        private CancelRestoreCommand() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (AccountManagerBackupHelper.this.mLock) {
                AccountManagerBackupHelper.this.mRestorePendingAppPermissions = null;
                if (AccountManagerBackupHelper.this.mRestorePackageMonitor != null) {
                    AccountManagerBackupHelper.this.mRestorePackageMonitor.unregister();
                    AccountManagerBackupHelper.this.mRestorePackageMonitor = null;
                }
            }
        }
    }
}
