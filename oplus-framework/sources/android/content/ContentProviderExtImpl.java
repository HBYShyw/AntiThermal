package android.content;

import android.app.OplusActivityManager;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;

/* loaded from: classes.dex */
public class ContentProviderExtImpl implements IContentProviderExt {
    private static final String FASFAST_APP_CARRIER_URI_PROPERTY = "persistent.sys.fastapp.authority";
    private static final String FAST_APP_CARRIER = "com.tencent.mm";
    private static final String FAST_APP_CARRIER_URI = "com.tencent.mm.sdk.comm.provider";
    private static final String FAST_APP_ENGINE = "com.nearme.instant.platform";
    private static final String TAG = "ContentProviderExtImpl";
    private ContentProvider mContentProvider;
    private boolean mSupportFastAppAdjust = false;
    private String mFastAppAuthrity = null;
    private OplusActivityManager mOplusActivityManager = null;

    public ContentProviderExtImpl(Object base) {
        this.mContentProvider = (ContentProvider) base;
    }

    public boolean skipMultiappHandleUri(int userId, Uri uri) {
        if (uri == null) {
            return false;
        }
        String auth = uri.getAuthority();
        if (999 != userId || !"media".equals(auth)) {
            return false;
        }
        return true;
    }

    public void init(ContentProvider cp) {
        try {
            if (cp.getContext() != null) {
                boolean equals = FAST_APP_CARRIER.equals(cp.getContext().getPackageName());
                this.mSupportFastAppAdjust = equals;
                if (equals && this.mOplusActivityManager == null) {
                    this.mOplusActivityManager = new OplusActivityManager();
                }
            }
        } catch (UnsupportedOperationException e) {
            this.mSupportFastAppAdjust = false;
            Log.w(TAG, "init got exception " + e);
        }
    }

    public String adjustCallingPkg(ContentProvider cp, AttributionSource callingAttributionSource) {
        if (!this.mSupportFastAppAdjust || callingAttributionSource == null || !FAST_APP_ENGINE.equals(callingAttributionSource.getPackageName()) || !cp.matchesOurAuthorities(SystemProperties.get(FASFAST_APP_CARRIER_URI_PROPERTY, FAST_APP_CARRIER_URI))) {
            return null;
        }
        try {
            OplusActivityManager oplusActivityManager = this.mOplusActivityManager;
            if (oplusActivityManager != null) {
                return oplusActivityManager.getFastAppReplacePkg(FAST_APP_CARRIER);
            }
        } catch (RemoteException e) {
            Log.w(TAG, "adjust calling pkg got exception " + e);
        }
        return null;
    }
}
