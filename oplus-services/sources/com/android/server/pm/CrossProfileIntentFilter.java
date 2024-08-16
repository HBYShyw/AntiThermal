package com.android.server.pm;

import android.content.IntentFilter;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.utils.SnapshotCache;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class CrossProfileIntentFilter extends WatchedIntentFilter {
    public static final int ACCESS_LEVEL_ALL = 0;
    public static final int ACCESS_LEVEL_SYSTEM = 10;
    public static final int ACCESS_LEVEL_SYSTEM_ADD_ONLY = 20;
    private static final String ATTR_ACCESS_CONTROL = "accessControl";
    private static final String ATTR_FILTER = "filter";
    private static final String ATTR_FLAGS = "flags";
    private static final String ATTR_OWNER_PACKAGE = "ownerPackage";
    private static final String ATTR_TARGET_USER_ID = "targetUserId";
    public static final int FLAG_ALLOW_CHAINED_RESOLUTION = 16;
    public static final int FLAG_IS_PACKAGE_FOR_FILTER = 8;
    private static final String TAG = "CrossProfileIntentFilter";
    final int mAccessControlLevel;
    final int mFlags;
    final String mOwnerPackage;
    final SnapshotCache<CrossProfileIntentFilter> mSnapshot;
    final int mTargetUserId;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public @interface AccessControlLevel {
    }

    private SnapshotCache makeCache() {
        return new SnapshotCache<CrossProfileIntentFilter>(this, this) { // from class: com.android.server.pm.CrossProfileIntentFilter.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public CrossProfileIntentFilter createSnapshot() {
                CrossProfileIntentFilter crossProfileIntentFilter = new CrossProfileIntentFilter();
                crossProfileIntentFilter.seal();
                return crossProfileIntentFilter;
            }
        };
    }

    CrossProfileIntentFilter(IntentFilter intentFilter, String str, int i, int i2) {
        this(intentFilter, str, i, i2, 0);
    }

    CrossProfileIntentFilter(IntentFilter intentFilter, String str, int i, int i2, int i3) {
        super(intentFilter);
        this.mTargetUserId = i;
        this.mOwnerPackage = str;
        this.mFlags = i2;
        this.mAccessControlLevel = i3;
        this.mSnapshot = makeCache();
    }

    CrossProfileIntentFilter(WatchedIntentFilter watchedIntentFilter, String str, int i, int i2) {
        this(watchedIntentFilter.mFilter, str, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CrossProfileIntentFilter(WatchedIntentFilter watchedIntentFilter, String str, int i, int i2, int i3) {
        this(watchedIntentFilter.mFilter, str, i, i2, i3);
    }

    private CrossProfileIntentFilter(CrossProfileIntentFilter crossProfileIntentFilter) {
        super(crossProfileIntentFilter);
        this.mTargetUserId = crossProfileIntentFilter.mTargetUserId;
        this.mOwnerPackage = crossProfileIntentFilter.mOwnerPackage;
        this.mFlags = crossProfileIntentFilter.mFlags;
        this.mAccessControlLevel = crossProfileIntentFilter.mAccessControlLevel;
        this.mSnapshot = new SnapshotCache.Sealed();
    }

    public int getTargetUserId() {
        return this.mTargetUserId;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public String getOwnerPackage() {
        return this.mOwnerPackage;
    }

    public int getAccessControlLevel() {
        return this.mAccessControlLevel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CrossProfileIntentFilter(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        this.mTargetUserId = typedXmlPullParser.getAttributeInt((String) null, ATTR_TARGET_USER_ID, -10000);
        this.mOwnerPackage = getStringFromXml(typedXmlPullParser, ATTR_OWNER_PACKAGE, "");
        this.mAccessControlLevel = typedXmlPullParser.getAttributeInt((String) null, ATTR_ACCESS_CONTROL, 0);
        this.mFlags = typedXmlPullParser.getAttributeInt((String) null, ATTR_FLAGS, 0);
        this.mSnapshot = makeCache();
        int depth = typedXmlPullParser.getDepth();
        String name = typedXmlPullParser.getName();
        while (true) {
            int next = typedXmlPullParser.next();
            if (next == 1 || (next == 3 && typedXmlPullParser.getDepth() <= depth)) {
                break;
            }
            name = typedXmlPullParser.getName();
            if (next != 3 && next != 4 && next == 2) {
                if (name.equals(ATTR_FILTER)) {
                    break;
                }
                PackageManagerService.reportSettingsProblem(5, "Unknown element under crossProfile-intent-filters: " + name + " at " + typedXmlPullParser.getPositionDescription());
                XmlUtils.skipCurrentTag(typedXmlPullParser);
            }
        }
        if (name.equals(ATTR_FILTER)) {
            this.mFilter.readFromXml(typedXmlPullParser);
            return;
        }
        PackageManagerService.reportSettingsProblem(5, "Missing element under CrossProfileIntentFilter: filter at " + typedXmlPullParser.getPositionDescription());
        XmlUtils.skipCurrentTag(typedXmlPullParser);
    }

    private String getStringFromXml(TypedXmlPullParser typedXmlPullParser, String str, String str2) {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, str);
        if (attributeValue != null) {
            return attributeValue;
        }
        PackageManagerService.reportSettingsProblem(5, "Missing element under CrossProfileIntentFilter: " + str + " at " + typedXmlPullParser.getPositionDescription());
        return str2;
    }

    public void writeToXml(TypedXmlSerializer typedXmlSerializer) throws IOException {
        typedXmlSerializer.attributeInt((String) null, ATTR_TARGET_USER_ID, this.mTargetUserId);
        typedXmlSerializer.attributeInt((String) null, ATTR_FLAGS, this.mFlags);
        typedXmlSerializer.attribute((String) null, ATTR_OWNER_PACKAGE, this.mOwnerPackage);
        typedXmlSerializer.attributeInt((String) null, ATTR_ACCESS_CONTROL, this.mAccessControlLevel);
        typedXmlSerializer.startTag((String) null, ATTR_FILTER);
        this.mFilter.writeToXml(typedXmlSerializer);
        typedXmlSerializer.endTag((String) null, ATTR_FILTER);
    }

    public String toString() {
        return "CrossProfileIntentFilter{0x" + Integer.toHexString(System.identityHashCode(this)) + " " + Integer.toString(this.mTargetUserId) + "}";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean equalsIgnoreFilter(CrossProfileIntentFilter crossProfileIntentFilter) {
        return this.mTargetUserId == crossProfileIntentFilter.mTargetUserId && this.mOwnerPackage.equals(crossProfileIntentFilter.mOwnerPackage) && this.mFlags == crossProfileIntentFilter.mFlags && this.mAccessControlLevel == crossProfileIntentFilter.mAccessControlLevel;
    }

    @Override // com.android.server.pm.WatchedIntentFilter, com.android.server.utils.Snappable
    public CrossProfileIntentFilter snapshot() {
        return this.mSnapshot.snapshot();
    }
}
