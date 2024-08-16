package android.app;

import com.oplus.compactwindow.OplusCompactWindowManager;
import com.oplus.content.IOplusFeatureConfigList;
import com.oplus.content.OplusFeatureConfigManager;

/* loaded from: classes.dex */
public class EmbeddedActivityExtImpl implements IEmbeddedActivityExt {
    private static volatile EmbeddedActivityExtImpl sInstance = null;
    private static final boolean FEATURE_GOOGLE_EXTENSION_LAYOUT = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_GOOGLE_EXTENSION_LAYOUT);
    private static final boolean FEATURE_GOOGLE_EXTENSION_EMBEDDING = OplusFeatureConfigManager.getInstance().hasFeature(IOplusFeatureConfigList.FEATURE_GOOGLE_EXTENSION_EMBEDDING);

    public static EmbeddedActivityExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (EmbeddedActivityExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new EmbeddedActivityExtImpl();
                }
            }
        }
        return sInstance;
    }

    private EmbeddedActivityExtImpl() {
    }

    public boolean blockEmbeddingIfNeeded() {
        return OplusCompactWindowManager.getInstance().blockEmbeddingIfNeeded();
    }

    public boolean hasFeatureEmbedding() {
        return FEATURE_GOOGLE_EXTENSION_EMBEDDING;
    }

    public boolean hasFeatureLayout() {
        return FEATURE_GOOGLE_EXTENSION_LAYOUT;
    }
}
