package android.media;

import android.app.ActivityThread;
import android.os.SystemProperties;
import android.util.Log;
import com.oplus.atlas.OplusAtlasManager;
import com.oplus.oms.split.splitrequest.SplitPathManager;
import java.util.Arrays;

/* loaded from: classes.dex */
public class MediaCodecListExtImpl implements IMediaCodecListExt {
    private static final String TAG = "MediaCodecListExtImpl";
    private static volatile MediaCodecListExtImpl sInstance;
    private boolean mIsDisableAv1 = false;

    public MediaCodecListExtImpl(Object base) {
    }

    public static MediaCodecListExtImpl getInstance(Object base) {
        if (sInstance == null) {
            synchronized (MediaCodecListExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new MediaCodecListExtImpl(base);
                }
            }
        }
        return sInstance;
    }

    public boolean isNeedDisableAv1() {
        String value;
        String hardware = SystemProperties.get("ro.hardware", SplitPathManager.DEFAULT);
        String packageName = ActivityThread.currentOpPackageName();
        if (packageName != null && packageName.length() > 0 && (value = OplusAtlasManager.getInstance().getAttributeByAppName("disable-av1", packageName)) != null) {
            String[] platforms = value.split(",");
            this.mIsDisableAv1 = Arrays.asList(platforms).contains(hardware);
        }
        Log.d(TAG, "mIsDisableAv1=" + this.mIsDisableAv1);
        return this.mIsDisableAv1;
    }
}
