package android.view;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Region;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;

/* loaded from: classes.dex */
public class OplusSystemUINavigationGestureExtImpl implements IOplusSystemUINavigationGestureExt {
    private static final String SIDEBAR_SCENE_REGION = "sidebar_scene_region";
    private static final int SMART_SIDEBAR_REGION_STR_LENGTH = 4;
    private static final String TAG = "OplusSystemUINavigationGestureExtImpl";
    private ContentObserver mContentObserver;
    private Context mContext;
    private Region mSideBarSceneRegion = new Region();
    private boolean DEBUG = SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private boolean mIsRegisting = false;

    public void registerSmartSideBarRegion(Context context) {
        if (this.mIsRegisting) {
            return;
        }
        this.mContext = context;
        this.mIsRegisting = true;
        this.mContentObserver = new ContentObserver(this.mContext.getMainThreadHandler()) { // from class: android.view.OplusSystemUINavigationGestureExtImpl.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                OplusSystemUINavigationGestureExtImpl.this.updateSideBarSceneRegion();
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(SIDEBAR_SCENE_REGION), false, this.mContentObserver);
        updateSideBarSceneRegion();
    }

    public void unregisterSmartSideBarRegion() {
        if (this.mIsRegisting) {
            this.mIsRegisting = false;
            this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
        }
    }

    public Region getSmartSideBarRegion() {
        return this.mSideBarSceneRegion;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSideBarSceneRegion() {
        String regionString = null;
        try {
            regionString = Settings.Secure.getString(this.mContext.getContentResolver(), SIDEBAR_SCENE_REGION);
        } catch (Exception e) {
            Log.d(TAG, "get regionString is null " + e.getMessage());
        }
        if (regionString == null) {
            Log.d(TAG, "regionString = null");
            return;
        }
        String[] parts = regionString.split(" ");
        if (parts.length != 4) {
            Log.d(TAG, "parts length != 4");
            return;
        }
        try {
            this.mSideBarSceneRegion.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            if (this.DEBUG) {
                Log.d(TAG, "mSideBarSceneRegion = " + this.mSideBarSceneRegion);
            }
        } catch (NumberFormatException e2) {
            Log.d(TAG, "Failed to parse integers");
        }
    }
}
