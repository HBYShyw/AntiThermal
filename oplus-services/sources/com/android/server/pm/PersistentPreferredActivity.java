package com.android.server.pm;

import android.content.ComponentName;
import android.content.IntentFilter;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.utils.SnapshotCache;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PersistentPreferredActivity extends WatchedIntentFilter {
    private static final String ATTR_FILTER = "filter";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_SET_BY_DPM = "set-by-dpm";
    private static final boolean DEBUG_FILTERS = false;
    private static final String TAG = "PersistentPreferredActivity";
    final ComponentName mComponent;
    final boolean mIsSetByDpm;
    final SnapshotCache<PersistentPreferredActivity> mSnapshot;

    private SnapshotCache makeCache() {
        return new SnapshotCache<PersistentPreferredActivity>(this, this) { // from class: com.android.server.pm.PersistentPreferredActivity.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public PersistentPreferredActivity createSnapshot() {
                PersistentPreferredActivity persistentPreferredActivity = new PersistentPreferredActivity();
                persistentPreferredActivity.seal();
                return persistentPreferredActivity;
            }
        };
    }

    PersistentPreferredActivity(IntentFilter intentFilter, ComponentName componentName, boolean z) {
        super(intentFilter);
        this.mComponent = componentName;
        this.mIsSetByDpm = z;
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersistentPreferredActivity(WatchedIntentFilter watchedIntentFilter, ComponentName componentName, boolean z) {
        this(watchedIntentFilter.mFilter, componentName, z);
    }

    private PersistentPreferredActivity(PersistentPreferredActivity persistentPreferredActivity) {
        super(persistentPreferredActivity);
        this.mComponent = persistentPreferredActivity.mComponent;
        this.mIsSetByDpm = persistentPreferredActivity.mIsSetByDpm;
        this.mSnapshot = new SnapshotCache.Sealed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersistentPreferredActivity(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, "name");
        ComponentName unflattenFromString = ComponentName.unflattenFromString(attributeValue);
        this.mComponent = unflattenFromString;
        if (unflattenFromString == null) {
            PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: Bad activity name " + attributeValue + " at " + typedXmlPullParser.getPositionDescription());
        }
        this.mIsSetByDpm = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_SET_BY_DPM, false);
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
                PackageManagerService.reportSettingsProblem(5, "Unknown element: " + name + " at " + typedXmlPullParser.getPositionDescription());
                XmlUtils.skipCurrentTag(typedXmlPullParser);
            }
        }
        if (name.equals(ATTR_FILTER)) {
            this.mFilter.readFromXml(typedXmlPullParser);
        } else {
            PackageManagerService.reportSettingsProblem(5, "Missing element filter at " + typedXmlPullParser.getPositionDescription());
            XmlUtils.skipCurrentTag(typedXmlPullParser);
        }
        this.mSnapshot = makeCache();
    }

    public void writeToXml(TypedXmlSerializer typedXmlSerializer) throws IOException {
        typedXmlSerializer.attribute((String) null, "name", this.mComponent.flattenToShortString());
        typedXmlSerializer.attributeBoolean((String) null, ATTR_SET_BY_DPM, this.mIsSetByDpm);
        typedXmlSerializer.startTag((String) null, ATTR_FILTER);
        this.mFilter.writeToXml(typedXmlSerializer);
        typedXmlSerializer.endTag((String) null, ATTR_FILTER);
    }

    @Override // com.android.server.pm.WatchedIntentFilter
    public IntentFilter getIntentFilter() {
        return this.mFilter;
    }

    public String toString() {
        return "PersistentPreferredActivity{0x" + Integer.toHexString(System.identityHashCode(this)) + " " + this.mComponent.flattenToShortString() + ", mIsSetByDpm=" + this.mIsSetByDpm + "}";
    }

    @Override // com.android.server.pm.WatchedIntentFilter, com.android.server.utils.Snappable
    public PersistentPreferredActivity snapshot() {
        return this.mSnapshot.snapshot();
    }
}
