package com.oplus.theme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;

/* loaded from: classes.dex */
public class OplusMaskBitmapUtilities {
    private static final String TAG = "MaskBitmapUtilities";
    private static OplusMaskBitmapUtilities sMaskBitmapUtilities = null;
    private BitmapFactory.Options mOpt;

    private static final native void nativeInit();

    public native Bitmap cutAndScaleBitmap(Bitmap bitmap);

    public native void releaseResouce();

    public native Bitmap scaleAndMaskBitmap(Context context, int i);

    public native Bitmap scaleAndMaskBitmap(Bitmap bitmap);

    public native void setCutAndScalePram(int i, int i2);

    public native void setMaskBitmap(Context context, int i);

    public native void setMaskBitmap(Bitmap bitmap);

    public native void setMaskBitmap(Bitmap bitmap, int i);

    static {
        System.loadLibrary("oplusgraphic");
        nativeInit();
    }

    private OplusMaskBitmapUtilities() {
    }

    public static synchronized OplusMaskBitmapUtilities getInstance() {
        OplusMaskBitmapUtilities oplusMaskBitmapUtilities;
        synchronized (OplusMaskBitmapUtilities.class) {
            if (sMaskBitmapUtilities == null) {
                sMaskBitmapUtilities = new OplusMaskBitmapUtilities();
            }
            oplusMaskBitmapUtilities = sMaskBitmapUtilities;
        }
        return oplusMaskBitmapUtilities;
    }

    private Bitmap readBitmap(Context context, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        this.mOpt = options;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        this.mOpt.inMutable = true;
        InputStream is = context.getResources().openRawResource(id);
        Bitmap b = BitmapFactory.decodeStream(is, null, this.mOpt);
        try {
            is.close();
        } catch (Exception e) {
        }
        return b;
    }
}
