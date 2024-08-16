package com.android.server.slice;

import android.net.Uri;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.slice.DirtyTracker;
import com.android.server.slice.SliceClientPermissions;
import com.android.server.slice.SlicePermissionManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SliceClientPermissions implements DirtyTracker, DirtyTracker.Persistable {
    private static final String ATTR_AUTHORITY = "authority";
    private static final String ATTR_FULL_ACCESS = "fullAccess";
    private static final String ATTR_PKG = "pkg";
    private static final String NAMESPACE = null;
    private static final String TAG = "SliceClientPermissions";
    private static final String TAG_AUTHORITY = "authority";
    static final String TAG_CLIENT = "client";
    private static final String TAG_PATH = "path";
    private final ArrayMap<SlicePermissionManager.PkgUser, SliceAuthority> mAuths = new ArrayMap<>();
    private boolean mHasFullAccess;
    private final SlicePermissionManager.PkgUser mPkg;
    private final DirtyTracker mTracker;

    public SliceClientPermissions(SlicePermissionManager.PkgUser pkgUser, DirtyTracker dirtyTracker) {
        this.mPkg = pkgUser;
        this.mTracker = dirtyTracker;
    }

    public SlicePermissionManager.PkgUser getPkg() {
        return this.mPkg;
    }

    public synchronized Collection<SliceAuthority> getAuthorities() {
        return new ArrayList(this.mAuths.values());
    }

    public synchronized SliceAuthority getOrCreateAuthority(SlicePermissionManager.PkgUser pkgUser, SlicePermissionManager.PkgUser pkgUser2) {
        SliceAuthority sliceAuthority;
        sliceAuthority = this.mAuths.get(pkgUser);
        if (sliceAuthority == null) {
            sliceAuthority = new SliceAuthority(pkgUser.getPkg(), pkgUser2, this);
            this.mAuths.put(pkgUser, sliceAuthority);
            onPersistableDirty(sliceAuthority);
        }
        return sliceAuthority;
    }

    public synchronized SliceAuthority getAuthority(SlicePermissionManager.PkgUser pkgUser) {
        return this.mAuths.get(pkgUser);
    }

    public boolean hasFullAccess() {
        return this.mHasFullAccess;
    }

    public void setHasFullAccess(boolean z) {
        if (this.mHasFullAccess == z) {
            return;
        }
        this.mHasFullAccess = z;
        this.mTracker.onPersistableDirty(this);
    }

    public void removeAuthority(String str, int i) {
        if (this.mAuths.remove(new SlicePermissionManager.PkgUser(str, i)) != null) {
            this.mTracker.onPersistableDirty(this);
        }
    }

    public synchronized boolean hasPermission(Uri uri, int i) {
        boolean z = false;
        if (!"content".equals(uri.getScheme())) {
            return false;
        }
        SliceAuthority authority = getAuthority(new SlicePermissionManager.PkgUser(uri.getAuthority(), i));
        if (authority != null) {
            if (authority.hasPermission(uri.getPathSegments())) {
                z = true;
            }
        }
        return z;
    }

    public void grantUri(Uri uri, SlicePermissionManager.PkgUser pkgUser) {
        getOrCreateAuthority(new SlicePermissionManager.PkgUser(uri.getAuthority(), pkgUser.getUserId()), pkgUser).addPath(uri.getPathSegments());
    }

    public void revokeUri(Uri uri, SlicePermissionManager.PkgUser pkgUser) {
        getOrCreateAuthority(new SlicePermissionManager.PkgUser(uri.getAuthority(), pkgUser.getUserId()), pkgUser).removePath(uri.getPathSegments());
    }

    public void clear() {
        if (this.mHasFullAccess || !this.mAuths.isEmpty()) {
            this.mHasFullAccess = false;
            this.mAuths.clear();
            onPersistableDirty(this);
        }
    }

    @Override // com.android.server.slice.DirtyTracker
    public void onPersistableDirty(DirtyTracker.Persistable persistable) {
        this.mTracker.onPersistableDirty(this);
    }

    @Override // com.android.server.slice.DirtyTracker.Persistable
    public String getFileName() {
        return getFileName(this.mPkg);
    }

    @Override // com.android.server.slice.DirtyTracker.Persistable
    public synchronized void writeTo(XmlSerializer xmlSerializer) throws IOException {
        String str = NAMESPACE;
        xmlSerializer.startTag(str, TAG_CLIENT);
        xmlSerializer.attribute(str, ATTR_PKG, this.mPkg.toString());
        xmlSerializer.attribute(str, ATTR_FULL_ACCESS, this.mHasFullAccess ? "1" : "0");
        int size = this.mAuths.size();
        for (int i = 0; i < size; i++) {
            String str2 = NAMESPACE;
            xmlSerializer.startTag(str2, "authority");
            xmlSerializer.attribute(str2, "authority", this.mAuths.valueAt(i).mAuthority);
            xmlSerializer.attribute(str2, ATTR_PKG, this.mAuths.valueAt(i).mPkg.toString());
            this.mAuths.valueAt(i).writeTo(xmlSerializer);
            xmlSerializer.endTag(str2, "authority");
        }
        xmlSerializer.endTag(NAMESPACE, TAG_CLIENT);
    }

    public static SliceClientPermissions createFrom(XmlPullParser xmlPullParser, DirtyTracker dirtyTracker) throws XmlPullParserException, IOException {
        while (true) {
            if (xmlPullParser.getEventType() != 2 || !TAG_CLIENT.equals(xmlPullParser.getName())) {
                if (xmlPullParser.getEventType() == 1) {
                    throw new XmlPullParserException("Can't find client tag in xml");
                }
                xmlPullParser.next();
            } else {
                int depth = xmlPullParser.getDepth();
                String str = NAMESPACE;
                SliceClientPermissions sliceClientPermissions = new SliceClientPermissions(new SlicePermissionManager.PkgUser(xmlPullParser.getAttributeValue(str, ATTR_PKG)), dirtyTracker);
                String attributeValue = xmlPullParser.getAttributeValue(str, ATTR_FULL_ACCESS);
                if (attributeValue == null) {
                    attributeValue = "0";
                }
                sliceClientPermissions.mHasFullAccess = Integer.parseInt(attributeValue) != 0;
                xmlPullParser.next();
                while (xmlPullParser.getDepth() > depth && xmlPullParser.getEventType() != 1) {
                    if (xmlPullParser.getEventType() == 2 && "authority".equals(xmlPullParser.getName())) {
                        try {
                            String str2 = NAMESPACE;
                            SlicePermissionManager.PkgUser pkgUser = new SlicePermissionManager.PkgUser(xmlPullParser.getAttributeValue(str2, ATTR_PKG));
                            SliceAuthority sliceAuthority = new SliceAuthority(xmlPullParser.getAttributeValue(str2, "authority"), pkgUser, sliceClientPermissions);
                            sliceAuthority.readFrom(xmlPullParser);
                            sliceClientPermissions.mAuths.put(new SlicePermissionManager.PkgUser(sliceAuthority.getAuthority(), pkgUser.getUserId()), sliceAuthority);
                        } catch (IllegalArgumentException e) {
                            Slog.e(TAG, "Couldn't read PkgUser", e);
                        }
                    }
                    xmlPullParser.next();
                }
                return sliceClientPermissions;
            }
        }
    }

    public static String getFileName(SlicePermissionManager.PkgUser pkgUser) {
        return String.format("client_%s", pkgUser.toString());
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class SliceAuthority implements DirtyTracker.Persistable {
        public static final String DELIMITER = "/";
        private final String mAuthority;
        private final ArraySet<String[]> mPaths = new ArraySet<>();
        private final SlicePermissionManager.PkgUser mPkg;
        private final DirtyTracker mTracker;

        @Override // com.android.server.slice.DirtyTracker.Persistable
        public String getFileName() {
            return null;
        }

        public SliceAuthority(String str, SlicePermissionManager.PkgUser pkgUser, DirtyTracker dirtyTracker) {
            this.mAuthority = str;
            this.mPkg = pkgUser;
            this.mTracker = dirtyTracker;
        }

        public String getAuthority() {
            return this.mAuthority;
        }

        public SlicePermissionManager.PkgUser getPkg() {
            return this.mPkg;
        }

        void addPath(List<String> list) {
            String[] strArr = (String[]) list.toArray(new String[list.size()]);
            for (int size = this.mPaths.size() - 1; size >= 0; size--) {
                String[] valueAt = this.mPaths.valueAt(size);
                if (isPathPrefixMatch(valueAt, strArr)) {
                    return;
                }
                if (isPathPrefixMatch(strArr, valueAt)) {
                    this.mPaths.removeAt(size);
                }
            }
            this.mPaths.add(strArr);
            this.mTracker.onPersistableDirty(this);
        }

        void removePath(List<String> list) {
            String[] strArr = (String[]) list.toArray(new String[list.size()]);
            boolean z = false;
            for (int size = this.mPaths.size() - 1; size >= 0; size--) {
                if (isPathPrefixMatch(strArr, this.mPaths.valueAt(size))) {
                    this.mPaths.removeAt(size);
                    z = true;
                }
            }
            if (z) {
                this.mTracker.onPersistableDirty(this);
            }
        }

        public synchronized Collection<String[]> getPaths() {
            return new ArraySet((ArraySet) this.mPaths);
        }

        public boolean hasPermission(List<String> list) {
            Iterator<String[]> it = this.mPaths.iterator();
            while (it.hasNext()) {
                if (isPathPrefixMatch(it.next(), (String[]) list.toArray(new String[list.size()]))) {
                    return true;
                }
            }
            return false;
        }

        private boolean isPathPrefixMatch(String[] strArr, String[] strArr2) {
            int length = strArr.length;
            if (strArr2.length < length) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (!Objects.equals(strArr2[i], strArr[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.android.server.slice.DirtyTracker.Persistable
        public synchronized void writeTo(XmlSerializer xmlSerializer) throws IOException {
            int size = this.mPaths.size();
            for (int i = 0; i < size; i++) {
                String[] valueAt = this.mPaths.valueAt(i);
                if (valueAt != null) {
                    xmlSerializer.startTag(SliceClientPermissions.NAMESPACE, SliceClientPermissions.TAG_PATH);
                    xmlSerializer.text(encodeSegments(valueAt));
                    xmlSerializer.endTag(SliceClientPermissions.NAMESPACE, SliceClientPermissions.TAG_PATH);
                }
            }
        }

        public synchronized void readFrom(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
            xmlPullParser.next();
            int depth = xmlPullParser.getDepth();
            while (xmlPullParser.getDepth() >= depth) {
                if (xmlPullParser.getEventType() == 2 && SliceClientPermissions.TAG_PATH.equals(xmlPullParser.getName())) {
                    this.mPaths.add(decodeSegments(xmlPullParser.nextText()));
                }
                xmlPullParser.next();
            }
        }

        private String encodeSegments(String[] strArr) {
            String[] strArr2 = new String[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                strArr2[i] = Uri.encode(strArr[i]);
            }
            return TextUtils.join(DELIMITER, strArr2);
        }

        private String[] decodeSegments(String str) {
            String[] split = str.split(DELIMITER, -1);
            for (int i = 0; i < split.length; i++) {
                split[i] = Uri.decode(split[i]);
            }
            return split;
        }

        public boolean equals(Object obj) {
            if (!getClass().equals(obj != null ? obj.getClass() : null)) {
                return false;
            }
            SliceAuthority sliceAuthority = (SliceAuthority) obj;
            if (this.mPaths.size() != sliceAuthority.mPaths.size()) {
                return false;
            }
            ArrayList arrayList = new ArrayList(this.mPaths);
            ArrayList arrayList2 = new ArrayList(sliceAuthority.mPaths);
            arrayList.sort(Comparator.comparing(new Function() { // from class: com.android.server.slice.SliceClientPermissions$SliceAuthority$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj2) {
                    String lambda$equals$0;
                    lambda$equals$0 = SliceClientPermissions.SliceAuthority.lambda$equals$0((String[]) obj2);
                    return lambda$equals$0;
                }
            }));
            arrayList2.sort(Comparator.comparing(new Function() { // from class: com.android.server.slice.SliceClientPermissions$SliceAuthority$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj2) {
                    String lambda$equals$1;
                    lambda$equals$1 = SliceClientPermissions.SliceAuthority.lambda$equals$1((String[]) obj2);
                    return lambda$equals$1;
                }
            }));
            for (int i = 0; i < arrayList.size(); i++) {
                String[] strArr = (String[]) arrayList.get(i);
                String[] strArr2 = (String[]) arrayList2.get(i);
                if (strArr.length != strArr2.length) {
                    return false;
                }
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    if (!Objects.equals(strArr[i2], strArr2[i2])) {
                        return false;
                    }
                }
            }
            return Objects.equals(this.mAuthority, sliceAuthority.mAuthority) && Objects.equals(this.mPkg, sliceAuthority.mPkg);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ String lambda$equals$0(String[] strArr) {
            return TextUtils.join(",", strArr);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ String lambda$equals$1(String[] strArr) {
            return TextUtils.join(",", strArr);
        }

        public String toString() {
            return String.format("(%s, %s: %s)", this.mAuthority, this.mPkg.toString(), pathToString(this.mPaths));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ String lambda$pathToString$2(String[] strArr) {
            return TextUtils.join(DELIMITER, strArr);
        }

        private String pathToString(ArraySet<String[]> arraySet) {
            return TextUtils.join(", ", (Iterable) arraySet.stream().map(new Function() { // from class: com.android.server.slice.SliceClientPermissions$SliceAuthority$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    String lambda$pathToString$2;
                    lambda$pathToString$2 = SliceClientPermissions.SliceAuthority.lambda$pathToString$2((String[]) obj);
                    return lambda$pathToString$2;
                }
            }).collect(Collectors.toList()));
        }
    }
}
