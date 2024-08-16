package android.view;

import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import com.oplus.favorite.IOplusFavoriteManager;
import com.oplus.screenshot.OplusLongshotViewRoot;
import com.oplus.util.OplusTypeCastingHelper;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class OplusViewRootImplHooks {
    private final String TAG = "OplusViewRootImplHooks";
    private final OplusLongshotViewRoot mLongshotViewRoot = new OplusLongshotViewRoot();
    private final ViewRootImpl mViewRootImpl;

    public OplusViewRootImplHooks(ViewRootImpl viewRootImpl, Context context) {
        this.mViewRootImpl = viewRootImpl;
    }

    public OplusLongshotViewRoot getLongshotViewRoot() {
        return this.mLongshotViewRoot;
    }

    public void setView(View view) {
    }

    public void markUserDefinedToast(View view, WindowManager.LayoutParams attrs) {
        OplusBaseLayoutParams params;
        if (view == null || attrs == null) {
            Log.w("OplusViewRootImplHooks", "markUserDefinedToast invalid args, view=" + view + ", attrs=" + attrs);
        } else if (attrs.type == 2005 && view.mID != 201457796 && (params = (OplusBaseLayoutParams) OplusTypeCastingHelper.typeCasting(OplusBaseLayoutParams.class, attrs)) != null) {
            params.oplusFlags |= 1;
        }
    }

    public void dispatchDetachedFromWindow(View view) {
        if (view == null) {
            return;
        }
        IOplusFavoriteManager favoriteManager = (IOplusFavoriteManager) OplusFeatureCache.getOrCreate(IOplusFavoriteManager.DEFAULT, new Object[0]);
        favoriteManager.release();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ViewRootImpl.W createWindowClient(ViewRootImpl viewAncestor) {
        return new ColorW(viewAncestor);
    }

    public MotionEvent updatePointerEvent(MotionEvent event, View mView, Configuration mLastConfiguration) {
        return ((IOplusAccidentallyTouchHelper) OplusFeatureCache.getOrCreate(IOplusAccidentallyTouchHelper.DEFAULT, new Object[0])).updatePointerEvent(event, mView, mLastConfiguration);
    }

    /* loaded from: classes.dex */
    static class ColorW extends ViewRootImpl.W {
        private IOplusDirectViewHelper mDirectHelper;
        private IOplusLongshotViewHelper mLongshotHelper;

        ColorW(ViewRootImpl viewAncestor) {
            super(viewAncestor);
            WeakReference<ViewRootImpl> reference = null;
            try {
                Field viewAncestorField = ColorW.class.getSuperclass().getDeclaredField("mViewAncestor");
                viewAncestorField.setAccessible(true);
                reference = (WeakReference) viewAncestorField.get(this);
            } catch (IllegalAccessException e) {
                Log.w("OplusViewRootImplHooks", "IllegalAccessException reflect to get mViewAncestor from ViewRootImpl");
            } catch (NoSuchFieldException e2) {
                Log.w("OplusViewRootImplHooks", "NoSuchFieldException reflect to get mViewAncestor from ViewRootImpl");
            }
            if (reference != null) {
                this.mLongshotHelper = ((IOplusViewRootUtil) OplusFeatureCache.getOrCreate(IOplusViewRootUtil.DEFAULT, new Object[0])).getOplusLongshotViewHelper(reference);
                this.mDirectHelper = (IOplusDirectViewHelper) OplusFrameworkFactory.getInstance().getFeature(IOplusDirectViewHelper.DEFAULT, new Object[]{reference});
            }
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IOplusLongshotViewHelper iOplusLongshotViewHelper = this.mLongshotHelper;
            if (iOplusLongshotViewHelper != null && iOplusLongshotViewHelper.onTransact(code, data, reply, flags)) {
                return true;
            }
            IOplusDirectViewHelper iOplusDirectViewHelper = this.mDirectHelper;
            if (iOplusDirectViewHelper == null || !iOplusDirectViewHelper.onTransact(code, data, reply, flags)) {
                return super.onTransact(code, data, reply, flags);
            }
            return true;
        }
    }
}
