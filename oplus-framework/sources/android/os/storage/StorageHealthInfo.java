package android.os.storage;

import java.util.List;

/* loaded from: classes.dex */
public class StorageHealthInfo {
    private List<StorageHealthInfoItem> mHealthInfoItems = null;
    private String mHealthInfoName;

    public StorageHealthInfo(String name) {
        this.mHealthInfoName = name;
    }

    public String getHealthInfoName() {
        return this.mHealthInfoName;
    }

    public void setHealthInfoItems(List<StorageHealthInfoItem> healthInfoItems) {
        this.mHealthInfoItems = healthInfoItems;
    }

    public List<StorageHealthInfoItem> getHealthInfoItems() {
        return this.mHealthInfoItems;
    }
}
