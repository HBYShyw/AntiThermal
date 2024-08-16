package com.android.server.pm;

import android.content.ComponentName;
import android.content.IntentFilter;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.pm.PreferredComponent;
import com.android.server.utils.SnapshotCache;
import java.io.IOException;
import java.io.PrintWriter;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class PreferredActivity extends WatchedIntentFilter implements PreferredComponent.Callbacks {
    private static final boolean DEBUG_FILTERS = false;
    private static final String TAG = "PreferredActivity";
    final PreferredComponent mPref;
    final SnapshotCache<PreferredActivity> mSnapshot;

    private SnapshotCache makeCache() {
        return new SnapshotCache<PreferredActivity>(this, this) { // from class: com.android.server.pm.PreferredActivity.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public PreferredActivity createSnapshot() {
                PreferredActivity preferredActivity = new PreferredActivity();
                preferredActivity.seal();
                return preferredActivity;
            }
        };
    }

    PreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, boolean z) {
        super(intentFilter);
        this.mPref = new PreferredComponent(this, i, componentNameArr, componentName, z);
        this.mSnapshot = makeCache();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreferredActivity(WatchedIntentFilter watchedIntentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName, boolean z) {
        this(watchedIntentFilter.mFilter, i, componentNameArr, componentName, z);
    }

    private PreferredActivity(PreferredActivity preferredActivity) {
        super(preferredActivity);
        this.mPref = preferredActivity.mPref;
        this.mSnapshot = new SnapshotCache.Sealed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PreferredActivity(TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        this.mPref = new PreferredComponent(this, typedXmlPullParser);
        this.mSnapshot = makeCache();
    }

    public void writeToXml(TypedXmlSerializer typedXmlSerializer, boolean z) throws IOException {
        this.mPref.writeToXml(typedXmlSerializer, z);
        typedXmlSerializer.startTag((String) null, "filter");
        this.mFilter.writeToXml(typedXmlSerializer);
        typedXmlSerializer.endTag((String) null, "filter");
    }

    @Override // com.android.server.pm.PreferredComponent.Callbacks
    public boolean onReadTag(String str, TypedXmlPullParser typedXmlPullParser) throws XmlPullParserException, IOException {
        if (str.equals("filter")) {
            this.mFilter.readFromXml(typedXmlPullParser);
            return true;
        }
        PackageManagerService.reportSettingsProblem(5, "Unknown element under <preferred-activities>: " + typedXmlPullParser.getName());
        XmlUtils.skipCurrentTag(typedXmlPullParser);
        return true;
    }

    public void dumpPref(PrintWriter printWriter, String str, PreferredActivity preferredActivity) {
        this.mPref.dump(printWriter, str, preferredActivity);
    }

    public String toString() {
        return "PreferredActivity{0x" + Integer.toHexString(System.identityHashCode(this)) + " " + this.mPref.mComponent.flattenToShortString() + "}";
    }

    @Override // com.android.server.pm.WatchedIntentFilter, com.android.server.utils.Snappable
    public PreferredActivity snapshot() {
        return this.mSnapshot.snapshot();
    }
}
