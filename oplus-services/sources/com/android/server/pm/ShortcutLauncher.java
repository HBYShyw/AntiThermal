package com.android.server.pm;

import android.content.pm.PackageInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.UserPackage;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AtomicFile;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.pm.ShortcutService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import libcore.io.IoUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class ShortcutLauncher extends ShortcutPackageItem {
    private static final String ATTR_LAUNCHER_USER_ID = "launcher-user";
    private static final String ATTR_PACKAGE_NAME = "package-name";
    private static final String ATTR_PACKAGE_USER_ID = "package-user";
    private static final String ATTR_VALUE = "value";
    private static final String TAG = "ShortcutService";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_PIN = "pin";
    static final String TAG_ROOT = "launcher-pins";
    private final int mOwnerUserId;
    private final ArrayMap<UserPackage, ArraySet<String>> mPinnedShortcuts;

    @Override // com.android.server.pm.ShortcutPackageItem
    protected boolean canRestoreAnyVersion() {
        return true;
    }

    private ShortcutLauncher(ShortcutUser shortcutUser, int i, String str, int i2, ShortcutPackageInfo shortcutPackageInfo) {
        super(shortcutUser, i2, str, shortcutPackageInfo == null ? ShortcutPackageInfo.newEmpty() : shortcutPackageInfo);
        this.mPinnedShortcuts = new ArrayMap<>();
        this.mOwnerUserId = i;
    }

    public ShortcutLauncher(ShortcutUser shortcutUser, int i, String str, int i2) {
        this(shortcutUser, i, str, i2, null);
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public int getOwnerUserId() {
        return this.mOwnerUserId;
    }

    private void onRestoreBlocked() {
        ArrayList arrayList = new ArrayList(this.mPinnedShortcuts.keySet());
        this.mPinnedShortcuts.clear();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ShortcutPackage packageShortcutsIfExists = this.mShortcutUser.getPackageShortcutsIfExists(((UserPackage) arrayList.get(size)).packageName);
            if (packageShortcutsIfExists != null) {
                packageShortcutsIfExists.refreshPinnedFlags();
            }
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected void onRestored(int i) {
        if (i != 0) {
            onRestoreBlocked();
        }
    }

    public void pinShortcuts(int i, String str, List<String> list, boolean z) {
        ShortcutPackage packageShortcutsIfExists = this.mShortcutUser.getPackageShortcutsIfExists(str);
        if (packageShortcutsIfExists == null) {
            return;
        }
        UserPackage of = UserPackage.of(i, str);
        int size = list.size();
        if (size == 0) {
            this.mPinnedShortcuts.remove(of);
        } else {
            ArraySet<String> arraySet = this.mPinnedShortcuts.get(of);
            ArraySet<String> arraySet2 = new ArraySet<>();
            for (int i2 = 0; i2 < size; i2++) {
                String str2 = list.get(i2);
                ShortcutInfo findShortcutById = packageShortcutsIfExists.findShortcutById(str2);
                if (findShortcutById != null && (findShortcutById.isDynamic() || findShortcutById.isLongLived() || findShortcutById.isManifestShortcut() || ((arraySet != null && arraySet.contains(str2)) || z))) {
                    arraySet2.add(str2);
                }
            }
            this.mPinnedShortcuts.put(of, arraySet2);
        }
        packageShortcutsIfExists.refreshPinnedFlags();
    }

    public ArraySet<String> getPinnedShortcutIds(String str, int i) {
        return this.mPinnedShortcuts.get(UserPackage.of(i, str));
    }

    public boolean hasPinned(ShortcutInfo shortcutInfo) {
        ArraySet<String> pinnedShortcutIds = getPinnedShortcutIds(shortcutInfo.getPackage(), shortcutInfo.getUserId());
        return pinnedShortcutIds != null && pinnedShortcutIds.contains(shortcutInfo.getId());
    }

    public void addPinnedShortcut(String str, int i, String str2, boolean z) {
        ArrayList arrayList;
        ArraySet<String> pinnedShortcutIds = getPinnedShortcutIds(str, i);
        if (pinnedShortcutIds != null) {
            arrayList = new ArrayList(pinnedShortcutIds.size() + 1);
            arrayList.addAll(pinnedShortcutIds);
        } else {
            arrayList = new ArrayList(1);
        }
        arrayList.add(str2);
        pinShortcuts(i, str, arrayList, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cleanUpPackage(String str, int i) {
        return this.mPinnedShortcuts.remove(UserPackage.of(i, str)) != null;
    }

    public void ensurePackageInfo() {
        PackageInfo packageInfoWithSignatures = this.mShortcutUser.mService.getPackageInfoWithSignatures(getPackageName(), getPackageUserId());
        if (packageInfoWithSignatures == null) {
            Slog.w(TAG, "Package not found: " + getPackageName());
            return;
        }
        getPackageInfo().updateFromPackageInfo(packageInfoWithSignatures);
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public void saveToXml(TypedXmlSerializer typedXmlSerializer, boolean z) throws IOException {
        if ((!z || getPackageInfo().isBackupAllowed()) && this.mPinnedShortcuts.size() != 0) {
            typedXmlSerializer.startTag((String) null, TAG_ROOT);
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_PACKAGE_NAME, getPackageName());
            ShortcutService.writeAttr(typedXmlSerializer, ATTR_LAUNCHER_USER_ID, getPackageUserId());
            getPackageInfo().saveToXml(this.mShortcutUser.mService, typedXmlSerializer, z);
            for (Map.Entry entry : new ArrayMap(this.mPinnedShortcuts).entrySet()) {
                UserPackage userPackage = (UserPackage) entry.getKey();
                if (userPackage != null && (!z || userPackage.userId == getOwnerUserId())) {
                    typedXmlSerializer.startTag((String) null, "package");
                    ShortcutService.writeAttr(typedXmlSerializer, ATTR_PACKAGE_NAME, userPackage.packageName);
                    ShortcutService.writeAttr(typedXmlSerializer, ATTR_PACKAGE_USER_ID, userPackage.userId);
                    ArraySet arraySet = new ArraySet((ArraySet) entry.getValue());
                    int size = arraySet.size();
                    for (int i = 0; i < size; i++) {
                        ShortcutService.writeTagValue(typedXmlSerializer, TAG_PIN, (String) arraySet.valueAt(i));
                    }
                    typedXmlSerializer.endTag((String) null, "package");
                }
            }
            typedXmlSerializer.endTag((String) null, TAG_ROOT);
        }
    }

    public static ShortcutLauncher loadFromFile(File file, ShortcutUser shortcutUser, int i, boolean z) {
        AtomicFile atomicFile = new AtomicFile(file);
        try {
            FileInputStream openRead = atomicFile.openRead();
            try {
                TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(openRead);
                ShortcutLauncher shortcutLauncher = null;
                while (true) {
                    int next = resolvePullParser.next();
                    if (next == 1) {
                        return shortcutLauncher;
                    }
                    if (next == 2) {
                        int depth = resolvePullParser.getDepth();
                        String name = resolvePullParser.getName();
                        if (depth == 1 && TAG_ROOT.equals(name)) {
                            shortcutLauncher = loadFromXml(resolvePullParser, shortcutUser, i, z);
                        } else {
                            ShortcutService.throwForInvalidTag(depth, name);
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                Slog.e(TAG, "Failed to read file " + atomicFile.getBaseFile(), e);
                return null;
            } finally {
                IoUtils.closeQuietly(openRead);
            }
        } catch (FileNotFoundException unused) {
            return null;
        }
    }

    public static ShortcutLauncher loadFromXml(TypedXmlPullParser typedXmlPullParser, ShortcutUser shortcutUser, int i, boolean z) throws IOException, XmlPullParserException {
        ShortcutLauncher shortcutLauncher = new ShortcutLauncher(shortcutUser, i, shortcutUser.mService.getWrapper().getExtImpl().hookGetLauncherPkgNameInLoadFromXml(ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PACKAGE_NAME)), z ? i : ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_LAUNCHER_USER_ID, i));
        int depth = typedXmlPullParser.getDepth();
        ArraySet<String> arraySet = null;
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            if (next == 2) {
                int depth2 = typedXmlPullParser.getDepth();
                String name = typedXmlPullParser.getName();
                if (depth2 == depth + 1) {
                    name.hashCode();
                    if (name.equals("package-info")) {
                        shortcutLauncher.getPackageInfo().loadFromXml(typedXmlPullParser, z);
                    } else if (name.equals("package")) {
                        String parseStringAttribute = ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_PACKAGE_NAME);
                        int parseIntAttribute = z ? i : ShortcutService.parseIntAttribute(typedXmlPullParser, ATTR_PACKAGE_USER_ID, i);
                        ArraySet<String> arraySet2 = new ArraySet<>();
                        shortcutLauncher.mPinnedShortcuts.put(UserPackage.of(parseIntAttribute, parseStringAttribute), arraySet2);
                        arraySet = arraySet2;
                    }
                }
                if (depth2 == depth + 2) {
                    name.hashCode();
                    if (name.equals(TAG_PIN)) {
                        if (arraySet == null) {
                            Slog.w(TAG, "pin in invalid place");
                        } else {
                            arraySet.add(ShortcutService.parseStringAttribute(typedXmlPullParser, ATTR_VALUE));
                        }
                    }
                }
                ShortcutService.warnForInvalidTag(depth2, name);
            }
        }
        return shortcutLauncher;
    }

    public void dump(PrintWriter printWriter, String str, ShortcutService.DumpFilter dumpFilter) {
        printWriter.println();
        printWriter.print(str);
        printWriter.print("Launcher: ");
        printWriter.print(getPackageName());
        printWriter.print("  Package user: ");
        printWriter.print(getPackageUserId());
        printWriter.print("  Owner user: ");
        printWriter.print(getOwnerUserId());
        printWriter.println();
        getPackageInfo().dump(printWriter, str + "  ");
        printWriter.println();
        int size = this.mPinnedShortcuts.size();
        for (int i = 0; i < size; i++) {
            printWriter.println();
            UserPackage keyAt = this.mPinnedShortcuts.keyAt(i);
            printWriter.print(str);
            printWriter.print("  ");
            printWriter.print("Package: ");
            printWriter.print(keyAt.packageName);
            printWriter.print("  User: ");
            printWriter.println(keyAt.userId);
            ArraySet<String> valueAt = this.mPinnedShortcuts.valueAt(i);
            int size2 = valueAt.size();
            for (int i2 = 0; i2 < size2; i2++) {
                printWriter.print(str);
                printWriter.print("    Pinned: ");
                printWriter.print(valueAt.valueAt(i2));
                printWriter.println();
            }
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public JSONObject dumpCheckin(boolean z) throws JSONException {
        return super.dumpCheckin(z);
    }

    @VisibleForTesting
    ArraySet<String> getAllPinnedShortcutsForTest(String str, int i) {
        return new ArraySet<>((ArraySet) this.mPinnedShortcuts.get(UserPackage.of(i, str)));
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected File getShortcutPackageItemFile() {
        ShortcutUser shortcutUser = this.mShortcutUser;
        return new File(new File(shortcutUser.mService.injectUserDataPath(shortcutUser.getUserId()), "launchers"), getPackageName() + getPackageUserId() + ".xml");
    }
}
