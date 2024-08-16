package android.os.storage;

import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class StorageManagerExtImpl implements IStorageManagerExt {
    public StorageManagerExtImpl(Object obj) {
    }

    public boolean isCustomerSdcardWIPData() {
        return OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FFEATURE_CUSTOMER_SDCARD_WIPEDATA);
    }
}
