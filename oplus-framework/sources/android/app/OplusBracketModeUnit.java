package android.app;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class OplusBracketModeUnit implements Parcelable {
    public static final Parcelable.Creator<OplusBracketModeUnit> CREATOR = new Parcelable.Creator<OplusBracketModeUnit>() { // from class: android.app.OplusBracketModeUnit.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusBracketModeUnit createFromParcel(Parcel in) {
            return new OplusBracketModeUnit(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OplusBracketModeUnit[] newArray(int size) {
            return new OplusBracketModeUnit[size];
        }
    };
    public static final String TAG = "OplusBracketModeUnit";
    private static final String XML_TAG_ACTIVITY_NAME = "activityName";
    private static final String XML_TAG_ACTIVITY_PAIRS = "activityPairs";
    private static final String XML_TAG_CLASS = "class";
    private static final String XML_TAG_GLOBAL_VIEW_LIST = "globalViewList";
    private static final String XML_TAG_ID = "id";
    private static final String XML_TAG_LABEL = "label";
    private static final String XML_TAG_MAX_VERSION = "maxVersion";
    private static final String XML_TAG_MIN_VERSION = "minVersion";
    private static final String XML_TAG_NORMALUI_DEVICES = "normalUiDevices";
    private static final String XML_TAG_SEPARATEUI_DEVICES = "separateUiDevices";
    private static final String XML_TAG_SEPARATEUI_LIST = "separateUiList";
    private static final String XML_TAG_SEPARATEUI_VERSIONS = "separateUiVersions";
    private static final String XML_TAG_SUPPORT_SEPARATEUI = "supportSeparateUi";
    private static final String XML_TAG_SUPPORT_TOUCH = "supportTouch";
    private static final String XML_TAG_VIEW_LIST = "viewList";
    private static final String XML_TAG_VIEW_SIZE_LIMIT = "viewSizeLimit";
    public String mCustomConfigBody;
    public long mMaxVersion;
    public long mMinVersion;
    public String mPackageName;
    public int mStatus;
    public boolean mSupportSeparateUi;
    public boolean mSupportTouchPanel;
    public ArrayMap<String, ActivityUnit> mChildActivities = new ArrayMap<>();
    public ArrayMap<String, SeparateActivityUnit> mSeparateActivityUnitList = new ArrayMap<>();
    public ArrayList<String> mSeparateUiVersion = new ArrayList<>();
    public ArrayList<SeparateUiView> mSeparateGlobalViewList = new ArrayList<>();
    public boolean mViewSizeLimit = false;
    public ArrayList<String> mSupportNormalUiDevices = new ArrayList<>();
    public ArrayList<String> mSupportSeparateUiDevices = new ArrayList<>();

    public OplusBracketModeUnit() {
    }

    public OplusBracketModeUnit(String pkgName, String customBody, int status) {
        this.mPackageName = pkgName;
        this.mCustomConfigBody = customBody;
        this.mStatus = status;
    }

    OplusBracketModeUnit(Parcel source) {
        readFromParcel(source);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ActivityUnit implements Parcelable {
        public static final Parcelable.Creator<ActivityUnit> CREATOR = new Parcelable.Creator<ActivityUnit>() { // from class: android.app.OplusBracketModeUnit.ActivityUnit.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ActivityUnit createFromParcel(Parcel in) {
                return new ActivityUnit(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ActivityUnit[] newArray(int size) {
                return new ActivityUnit[size];
            }
        };
        public String name;
        public int re;

        public ActivityUnit(String name, int re) {
            this.name = name;
            this.re = re;
        }

        private ActivityUnit(Parcel in) {
            this.name = in.readString();
            this.re = in.readInt();
        }

        public String toString() {
            return this.name + " re = " + this.re;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeInt(this.re);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public static class SeparateUiView implements Parcelable {
        public static final Parcelable.Creator<SeparateUiView> CREATOR = new Parcelable.Creator<SeparateUiView>() { // from class: android.app.OplusBracketModeUnit.SeparateUiView.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SeparateUiView createFromParcel(Parcel in) {
                return new SeparateUiView(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SeparateUiView[] newArray(int size) {
                return new SeparateUiView[size];
            }
        };
        public String key;
        public String value;

        public SeparateUiView(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public SeparateUiView(String keyValue) {
            String[] results = keyValue.split(":", 2);
            this.key = results[0];
            this.value = results[1];
        }

        private SeparateUiView(Parcel in) {
            this.key = in.readString();
            this.value = in.readString();
        }

        public String toString() {
            return this.key + ":" + this.value;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.key);
            dest.writeString(this.value);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }
    }

    /* loaded from: classes.dex */
    public static class SeparateActivityUnit implements Parcelable {
        public static final Parcelable.Creator<SeparateActivityUnit> CREATOR = new Parcelable.Creator<SeparateActivityUnit>() { // from class: android.app.OplusBracketModeUnit.SeparateActivityUnit.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SeparateActivityUnit createFromParcel(Parcel in) {
                return new SeparateActivityUnit(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SeparateActivityUnit[] newArray(int size) {
                return new SeparateActivityUnit[size];
            }
        };
        public ArrayList<SeparateUiView> separateUiViewList;

        public SeparateActivityUnit(ArrayList<SeparateUiView> viewList) {
            this.separateUiViewList = viewList;
        }

        private SeparateActivityUnit(Parcel in) {
            this.separateUiViewList = in.createTypedArrayList(SeparateUiView.CREATOR);
        }

        public String toString() {
            return "viewList:" + this.separateUiViewList;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.separateUiViewList, 0);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }
    }

    public void parseContent() {
        try {
            String formatedConfig = this.mCustomConfigBody.replace("^", "\"");
            JSONObject customConfig = new JSONObject(formatedConfig);
            if (customConfig.has(XML_TAG_SUPPORT_SEPARATEUI) && customConfig.getBoolean(XML_TAG_SUPPORT_SEPARATEUI)) {
                this.mSupportSeparateUi = true;
            }
            if (customConfig.has(XML_TAG_VIEW_SIZE_LIMIT) && customConfig.getBoolean(XML_TAG_VIEW_SIZE_LIMIT)) {
                this.mViewSizeLimit = true;
            }
            parseViewList(customConfig, XML_TAG_GLOBAL_VIEW_LIST, this.mSeparateGlobalViewList);
            parseSeparateUiList(customConfig, this.mSeparateActivityUnitList);
            parseSeparateUiVersion(customConfig);
            parseActivityPairs(customConfig);
            parseNormalUiDevices(customConfig);
            parseSeparateUiDevices(customConfig);
            if (customConfig.has(XML_TAG_SUPPORT_TOUCH) && customConfig.getBoolean(XML_TAG_SUPPORT_TOUCH)) {
                this.mSupportTouchPanel = true;
            }
        } catch (Exception ex) {
            Log.e(TAG, "parseContent error " + this.mPackageName + " " + ex);
        }
    }

    void parseViewList(JSONObject jasonObject, String name, ArrayList<SeparateUiView> viewList) {
        JSONArray viewListArray;
        try {
            if (jasonObject.has(name) && (viewListArray = jasonObject.getJSONArray(name)) != null) {
                for (int i = 0; i < viewListArray.length(); i++) {
                    JSONObject viewObject = viewListArray.getJSONObject(i);
                    SeparateUiView separateUiView = null;
                    if (viewObject.has("id")) {
                        separateUiView = new SeparateUiView("id", viewObject.getString("id"));
                    }
                    if (viewObject.has(XML_TAG_LABEL)) {
                        separateUiView = new SeparateUiView(XML_TAG_LABEL, viewObject.getString(XML_TAG_LABEL));
                    }
                    if (viewObject.has(XML_TAG_CLASS)) {
                        separateUiView = new SeparateUiView(XML_TAG_CLASS, viewObject.getString(XML_TAG_CLASS));
                    }
                    if (separateUiView != null) {
                        viewList.add(separateUiView);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "parseViewList error " + name + " " + ex);
        }
    }

    void parseSeparateUiList(JSONObject jasonObject, ArrayMap<String, SeparateActivityUnit> separateActivityUnitList) {
        JSONArray separateUiList;
        try {
            if (jasonObject.has(XML_TAG_SEPARATEUI_LIST) && (separateUiList = jasonObject.getJSONArray(XML_TAG_SEPARATEUI_LIST)) != null) {
                for (int j = 0; j < separateUiList.length(); j++) {
                    String separateUi = separateUiList.getString(j);
                    JSONObject separateUiObject = new JSONObject(separateUi);
                    String activityName = null;
                    ArrayList<SeparateUiView> viewList = new ArrayList<>();
                    if (separateUiObject.has("activityName")) {
                        String name = separateUiObject.getString("activityName");
                        ComponentName componentName = new ComponentName(this.mPackageName, name);
                        activityName = componentName.flattenToShortString();
                    }
                    parseViewList(separateUiObject, XML_TAG_VIEW_LIST, viewList);
                    if (activityName != null) {
                        separateActivityUnitList.put(activityName, new SeparateActivityUnit(viewList));
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "parseSeparateUiList error  " + ex);
        }
    }

    void parseSeparateUiVersion(JSONObject jasonObject) {
        JSONArray separateUiVersionList;
        try {
            if (jasonObject.has(XML_TAG_SEPARATEUI_VERSIONS) && (separateUiVersionList = jasonObject.getJSONArray(XML_TAG_SEPARATEUI_VERSIONS)) != null) {
                for (int j = 0; j < separateUiVersionList.length(); j++) {
                    JSONObject versionConfig = separateUiVersionList.getJSONObject(j);
                    if (versionConfig != null) {
                        if (versionConfig.has(XML_TAG_MIN_VERSION)) {
                            this.mMinVersion = versionConfig.getLong(XML_TAG_MIN_VERSION);
                        } else if (versionConfig.has(XML_TAG_MAX_VERSION)) {
                            this.mMaxVersion = versionConfig.getLong(XML_TAG_MAX_VERSION);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "parseSeparateUiVersion error " + ex);
        }
    }

    void parseActivityPairs(JSONObject jasonObject) {
        JSONArray activityPairs;
        try {
            if (jasonObject.has(XML_TAG_ACTIVITY_PAIRS) && (activityPairs = jasonObject.getJSONArray(XML_TAG_ACTIVITY_PAIRS)) != null) {
                for (int j = 0; j < activityPairs.length(); j++) {
                    ComponentName componentName = new ComponentName(this.mPackageName, activityPairs.getString(j));
                    this.mChildActivities.put(componentName.flattenToShortString(), new ActivityUnit(componentName.flattenToShortString(), 0));
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "parseActivityPairs error " + ex);
        }
    }

    void parseNormalUiDevices(JSONObject jasonObject) {
        JSONArray devices;
        try {
            if (jasonObject.has(XML_TAG_NORMALUI_DEVICES) && (devices = jasonObject.getJSONArray(XML_TAG_NORMALUI_DEVICES)) != null) {
                ArrayList<String> temp = new ArrayList<>();
                for (int j = 0; j < devices.length(); j++) {
                    temp.add(devices.getString(j));
                }
                this.mSupportNormalUiDevices = temp;
            }
        } catch (Exception ex) {
            Log.e(TAG, "parseSupportDevices error " + ex);
        }
    }

    void parseSeparateUiDevices(JSONObject jasonObject) {
        JSONArray devices;
        try {
            if (jasonObject.has(XML_TAG_SEPARATEUI_DEVICES) && (devices = jasonObject.getJSONArray(XML_TAG_SEPARATEUI_DEVICES)) != null) {
                ArrayList<String> temp = new ArrayList<>();
                for (int j = 0; j < devices.length(); j++) {
                    temp.add(devices.getString(j));
                }
                this.mSupportSeparateUiDevices = temp;
            }
        } catch (Exception ex) {
            Log.e(TAG, "parseSeparateUiDevices error " + ex);
        }
    }

    private void readFromParcel(Parcel source) {
        this.mPackageName = source.readString();
        this.mCustomConfigBody = source.readString();
        this.mChildActivities = source.createTypedArrayMap(ActivityUnit.CREATOR);
        this.mStatus = source.readInt();
        this.mSupportTouchPanel = source.readBoolean();
        this.mSupportSeparateUi = source.readBoolean();
        this.mViewSizeLimit = source.readBoolean();
        source.readStringList(this.mSeparateUiVersion);
        this.mSeparateGlobalViewList = source.createTypedArrayList(SeparateUiView.CREATOR);
        this.mSeparateActivityUnitList = source.createTypedArrayMap(SeparateActivityUnit.CREATOR);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeString(this.mCustomConfigBody);
        dest.writeTypedArrayMap(this.mChildActivities, 0);
        dest.writeInt(this.mStatus);
        dest.writeBoolean(this.mSupportTouchPanel);
        dest.writeBoolean(this.mSupportSeparateUi);
        dest.writeBoolean(this.mViewSizeLimit);
        dest.writeStringList(this.mSeparateUiVersion);
        dest.writeTypedList(this.mSeparateGlobalViewList);
        dest.writeTypedArrayMap(this.mSeparateActivityUnitList, 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "App:" + this.mPackageName + ", status:" + this.mStatus + ", support touch:" + this.mSupportTouchPanel + ", support separateUi:" + this.mSupportSeparateUi + ", viewSizeLimit:" + this.mViewSizeLimit + ", global viewList:" + this.mSeparateGlobalViewList.toString() + ", version list:" + this.mSeparateUiVersion.size() + ", activityUnitList:" + this.mSeparateActivityUnitList.toString() + ", child activitys:" + this.mChildActivities.toString() + ", minVersion:" + this.mMinVersion + ", maxVersion:" + this.mMaxVersion + ", SupportSeparateUiDevices:" + this.mSupportSeparateUiDevices + ", SupportNormalUiDevices:" + this.mSupportNormalUiDevices + "\n";
    }
}
