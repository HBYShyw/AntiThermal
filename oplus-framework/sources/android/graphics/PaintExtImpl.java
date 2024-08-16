package android.graphics;

import android.common.OplusFeatureCache;
import android.common.OplusFrameworkFactory;
import android.view.IOplusBurmeseZgHooks;
import com.oplus.font.IOplusFontManager;

/* loaded from: classes.dex */
public class PaintExtImpl implements IPaintExt {
    private static final String TAG = "PaintExtImpl";
    private static boolean sPreStatus = false;
    private IOplusBurmeseZgHooks mBurmeseZgHooks = (IOplusBurmeseZgHooks) OplusFrameworkFactory.getInstance().getFeature(IOplusBurmeseZgHooks.DEFAULT, new Object[0]);
    private IOplusFontManager mFontManager = (IOplusFontManager) OplusFeatureCache.getOrCreate(IOplusFontManager.DEFAULT, new Object[0]);
    private Paint mPaint;

    private static native void nSetForceZgFont(long j, boolean z);

    static {
        System.loadLibrary("oplusextzawgyi");
    }

    public PaintExtImpl(Object base) {
        this.mPaint = (Paint) base;
    }

    public boolean getZgFlag() {
        return this.mBurmeseZgHooks.getZgFlag();
    }

    public Typeface flipTypeface(Typeface typeface) {
        return this.mFontManager.flipTypeface(typeface == null ? ITypefaceExt.DEFAULT : typeface.mTypefaceExt, this.mPaint);
    }

    public void injectedByOemOS(Typeface typeface, long paintPtr, Paint paint) {
        replaceTypeface(typeface, paint, paintPtr);
        setForceZgFont(paintPtr);
    }

    public void setForceZgFont(long paintPtr) {
        boolean flag = getZgFlag();
        if (sPreStatus != flag) {
            sPreStatus = flag;
            nSetForceZgFont(paintPtr, flag);
        }
    }

    private void replaceTypeface(Typeface typeface, Paint paint, long paintPtr) {
        IPaintWrapper wrapper;
        Typeface tf = flipTypeface(typeface);
        if (tf != null && (wrapper = paint.getWrapper()) != null) {
            wrapper.getSetTypeface(paintPtr, tf.native_instance);
            wrapper.setTypeface(tf);
        }
    }
}
